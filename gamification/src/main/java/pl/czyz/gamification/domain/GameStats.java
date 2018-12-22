package pl.czyz.gamification.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class GameStats {
  private final Long userId;
  private final int score;
  private final List<Badge> badges;

  GameStats() {
    this(null, 0, new ArrayList<>());
  }

  public static GameStats epmtyStats(final Long userId) {
    return new GameStats(userId, 0, Collections.emptyList());
  }

  public List<Badge> getBadges() {
    return Collections.unmodifiableList(badges);
  }
}
