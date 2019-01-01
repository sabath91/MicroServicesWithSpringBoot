package pl.czyz.gamification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.czyz.gamification.domain.GameStats;
import pl.czyz.gamification.service.GameService;

/**
 * This class implements a REST API for the Gamification User statistics service.
 */
@RestController
@RequestMapping("/stats")
public class UserStatsController {

  private final GameService gameService;

  public UserStatsController(final GameService gameService) {
    this.gameService = gameService;
  }

  @GetMapping
  public GameStats getStatsForUser(@RequestParam("userId") final Long userId) {
    return gameService.retrieveStatsForUser(userId);
  }
}
