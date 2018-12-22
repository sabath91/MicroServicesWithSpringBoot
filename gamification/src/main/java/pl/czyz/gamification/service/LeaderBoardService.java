package pl.czyz.gamification.service;

import java.util.List;
import pl.czyz.gamification.domain.LeaderBoardRow;

public interface LeaderBoardService {
  List<LeaderBoardRow> getCurrentLeaderBoard();
}
