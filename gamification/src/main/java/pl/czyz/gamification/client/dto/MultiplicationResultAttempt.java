package pl.czyz.gamification.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonDeserialize(using = MultiplicationResultAttemptDeserializer.class)
public final class MultiplicationResultAttempt {

  private final String userAlias;
  private final int multiplicationFactorA;
  private final int multiplicationFactorB;
  private final int resultAttempt;
  private final boolean correct;

  MultiplicationResultAttempt() {
    this(null, -1, -1, -1, false);
  }
}
