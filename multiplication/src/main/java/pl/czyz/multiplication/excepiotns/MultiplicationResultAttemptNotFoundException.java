package pl.czyz.multiplication.excepiotns;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
    value = HttpStatus.NOT_FOUND,
    reason = "attempt with such id do not exists in database")
public class MultiplicationResultAttemptNotFoundException extends Exception {}
