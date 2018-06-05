package pl.czyz.socialmultiplication.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Multiplication {

    private Long id;
    private final int factorA;
    private final int factorB;

    Multiplication() {
        this(0, 0);
    }

}
