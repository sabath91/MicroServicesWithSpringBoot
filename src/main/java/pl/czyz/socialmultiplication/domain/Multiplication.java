package pl.czyz.socialmultiplication.domain;

import lombok.*;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Multiplication {

    private final int factorA;
    private final int factorB;

    Multiplication(){
        this(0,0);
    }

}
