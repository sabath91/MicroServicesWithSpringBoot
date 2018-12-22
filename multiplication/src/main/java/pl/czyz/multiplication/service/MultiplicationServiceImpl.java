package pl.czyz.multiplication.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.czyz.multiplication.domain.Multiplication;
import pl.czyz.multiplication.domain.MultiplicationResultAttempt;
import pl.czyz.multiplication.domain.User;
import pl.czyz.multiplication.event.EventDispatcher;
import pl.czyz.multiplication.event.MultiplicationSolvedEvent;
import pl.czyz.multiplication.excepiotns.MultiplicationResultAttemptNotFoundException;
import pl.czyz.multiplication.repository.MultiplicationResultAttemptRepository;
import pl.czyz.multiplication.repository.UserRepository;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

  private final MultiplicationResultAttemptRepository attemptRepository;
  private final UserRepository userRepository;
  private RandomGeneratorService randomGeneratorService;
  private EventDispatcher eventDispatcher;

  @Autowired
  public MultiplicationServiceImpl(
      RandomGeneratorService randomGeneratorService,
      MultiplicationResultAttemptRepository attemptRepository,
      UserRepository userRepository,
      final EventDispatcher eventDispatcher) {
    this.randomGeneratorService = randomGeneratorService;
    this.attemptRepository = attemptRepository;
    this.userRepository = userRepository;
    this.eventDispatcher = eventDispatcher;
  }

  @Override
  public Multiplication createRandomMultiplication() {
    int factorA = randomGeneratorService.generateRandomFactor();
    int factorB = randomGeneratorService.generateRandomFactor();
    return new Multiplication(factorA, factorB);
  }

  @Transactional
  @Override
  public boolean checkAttempt(MultiplicationResultAttempt attempt) {
    Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias());

    Assert.isTrue(!attempt.isCorrect(), "You can't send an attempt marked as correct");

    boolean isCorrect =
        attempt.getResultAttempt()
            == attempt.getMultiplication().getFactorA() * attempt.getMultiplication().getFactorB();

    MultiplicationResultAttempt checkedAttempt =
        new MultiplicationResultAttempt(
            user.orElse(attempt.getUser()),
            attempt.getMultiplication(),
            attempt.getResultAttempt(),
            isCorrect);

    attemptRepository.save(checkedAttempt);

    eventDispatcher.send(
        new MultiplicationSolvedEvent(
            checkedAttempt.getId(), checkedAttempt.getUser().getId(), checkedAttempt.isCorrect()));

    return isCorrect;
  }

  @Override
  public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
    return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
  }

  @Override
  public MultiplicationResultAttempt getResultById(final Long resultId)
      throws MultiplicationResultAttemptNotFoundException {
    return attemptRepository
        .findById(resultId)
        .orElseThrow(MultiplicationResultAttemptNotFoundException::new);
  }
}
