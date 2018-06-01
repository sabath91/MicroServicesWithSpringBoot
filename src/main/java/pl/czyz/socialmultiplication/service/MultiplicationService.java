package pl.czyz.socialmultiplication.service;

import pl.czyz.socialmultiplication.domain.Multiplication;

public interface MultiplicationService {

    /**
     * Crates a Multiplication object with two randomly generated factors between 11 and 99.
     *
     * @return a Multiplication object with random factors.
     */
    Multiplication createRandomMultiplication();

}
