package pl.czyz.multiplication.service;

import pl.czyz.multiplication.domain.Multiplication;
import pl.czyz.multiplication.domain.MultiplicationResultAttempt;

import java.util.List;

public interface MultiplicationService {

    /**
     * Crates a Multiplication object with two randomly generated factors between 11 and 99.
     *
     * @return a Multiplication object with random factors.
     */
    Multiplication createRandomMultiplication();

    /**
     * @return true if the attempt matches the result of the multiplication, false otherwise
     */
    boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);

    List<MultiplicationResultAttempt> getStatsForUser(String userAlias);
}
