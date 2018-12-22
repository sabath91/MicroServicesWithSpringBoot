package pl.czyz.gamification.service;

import java.util.List;
import org.springframework.stereotype.Service;
import pl.czyz.gamification.domain.LeaderBoardRow;
import pl.czyz.gamification.repository.ScoreCardRepository;

@Service
public class LeaderBoardServiceImpl implements LeaderBoardService {

  private ScoreCardRepository scoreCardRepository;

  LeaderBoardServiceImpl(final ScoreCardRepository scoreCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
  }

  @Override
  public List<LeaderBoardRow> getCurrentLeaderBoard() {
    return scoreCardRepository.findFirst10();
  }
}
