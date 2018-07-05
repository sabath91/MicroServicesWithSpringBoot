package pl.czyz.socialmultiplication.service;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.czyz.socialmultiplication.domain.Multiplication;
import pl.czyz.socialmultiplication.domain.MultiplicationResultAttempt;
import pl.czyz.socialmultiplication.domain.User;
import pl.czyz.socialmultiplication.repository.MultiplicationResultAttemptRepository;
import pl.czyz.socialmultiplication.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class MultiplicationServiceImplTest {

    private MultiplicationService multiplicationService;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        initMocks(this);
        this.multiplicationService = new MultiplicationServiceImpl(randomGeneratorService, attemptRepository, userRepository);
    }

    @Test
    public void createRandomMultiplicationTest() {
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        Multiplication multiplication = multiplicationService.createRandomMultiplication();

        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
    }

    @Test
    public void checkCorrectAttemptTest() {
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000, false);
        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user, multiplication, 3000, true);
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());

        boolean attemptResult = multiplicationService.checkAttempt(attempt);


        assertThat(attemptResult).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
    }

    @Test
    public void checkWrongAttemptTest() {
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 4212, false);
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());


        boolean attemptResult = multiplicationService.checkAttempt(attempt);

        assertThat(attemptResult).isFalse();
        verify(attemptRepository).save(attempt);
    }

    @Test
    public void retrieveStatsTest() {
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_doe");
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user, multiplication, 3051, false);
        MultiplicationResultAttempt attempt3 = new MultiplicationResultAttempt(user, multiplication, 3013, false);
        MultiplicationResultAttempt attempt4 = new MultiplicationResultAttempt(user, multiplication, 3011, false);


        List<MultiplicationResultAttempt> lastAttempts = Lists.newArrayList(attempt1, attempt2, attempt3, attempt4);
        //         Code below do not work in inteliji..
        //        List<MultiplicationResultAttempt> lastAttempts = List.of(attempt1, attempt2, attempt3, attempt4);


        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("john_doe")).willReturn(lastAttempts);

        //when
        List<MultiplicationResultAttempt> latestAttemptsResult = multiplicationService.getStatsForUser("john_doe");

        //then
        assertThat(latestAttemptsResult).isEqualTo(lastAttempts);

    }

}