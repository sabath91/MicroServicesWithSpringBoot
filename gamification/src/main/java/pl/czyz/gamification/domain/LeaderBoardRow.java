package pl.czyz.gamification.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class LeaderBoardRow {
    private final Long userId;
    private final Long totalScore;

    LeaderBoardRow() {
        this(null, null);
    }
}
