package pl.czyz.socialmultiplication.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public class Multiplication {

    private final int factorA;
    private final int factorB;
    @Id
    @GeneratedValue
    @Column(name = "MULTIPLICATION_ID")
    private Long id;

    Multiplication() {
        this(0, 0);
    }

}
