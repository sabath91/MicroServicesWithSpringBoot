package pl.czyz.multiplication.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class MultiplicationResultAttempt {

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "USER_ID")
    private final User user;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MULTIPLICATION_ID")

    private final Multiplication multiplication;
    private final int resultAttempt;
    private final boolean correct;
    @Id
    @GeneratedValue
    private Long id;

    MultiplicationResultAttempt() {
        this(null, null, -1, false);
    }

}
