package pl.czyz.gamification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.czyz.gamification.domain.LeaderBoardRow;
import pl.czyz.gamification.service.LeaderBoardService;

import java.util.List;

@RestController
@RequestMapping("/leaders")
class LeaderBoardController {

    private LeaderBoardService leaderBoardService;

    public LeaderBoardController(final LeaderBoardService leaderBoardService) {
        this.leaderBoardService = leaderBoardService;
    }

    @GetMapping
    public List<LeaderBoardRow> getLeaderBoard(){
        return leaderBoardService.getCurrentLeaderBoard();
    }
}
