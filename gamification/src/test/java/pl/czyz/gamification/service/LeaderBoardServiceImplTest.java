package pl.czyz.gamification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.czyz.gamification.domain.LeaderBoardRow;
import pl.czyz.gamification.repository.ScoreCardRepository;

public class LeaderBoardServiceImplTest {

  private LeaderBoardService leaderBoardService;

  @Mock private ScoreCardRepository scoreCardRepository;

  @Before
  public void setUp() {
    initMocks(this);
    leaderBoardService = new LeaderBoardServiceImpl(scoreCardRepository);
  }

  @Test
  public void shouldRetrieveLeaderBoard() {
    final Long userId = 1L;
    final Long totalScore = 10L;
    LeaderBoardRow leaderBoardRow = new LeaderBoardRow(userId, totalScore);
    final List<LeaderBoardRow> expectedLeaderBoard = Collections.singletonList(leaderBoardRow);
    given(scoreCardRepository.findFirst10()).willReturn(expectedLeaderBoard);

    List<LeaderBoardRow> retrivedLeaderBoard = leaderBoardService.getCurrentLeaderBoard();

    assertThat(retrivedLeaderBoard).isEqualTo(expectedLeaderBoard);
  }
}
