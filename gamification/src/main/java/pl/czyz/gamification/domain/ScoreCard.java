package pl.czyz.gamification.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class ScoreCard {
    public static final int DEFAULT_SCORE = 10;

    private final Long socreCardId;
    private final Long userId;
    private final Long attemptId;
    private final long scoreTimestamp;
    private final int score;

    //emptu contrucotr for JSON?JPA
    ScoreCard() {
        this(null, null, null, 0, 0);
    }

    public ScoreCard(final Long userId, final Long attemptId) {
        this(null, userId, attemptId, System.currentTimeMillis(), DEFAULT_SCORE);
    }
}
