package pl.czyz.socialmultiplication.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class MultiplicationResutlAttempt {

    private final User user;
    private final Multiplication multiplication;
    private final int resultAttempt;

    MultiplicationResutlAttempt(){
        this(null, null, -1);
    }

}