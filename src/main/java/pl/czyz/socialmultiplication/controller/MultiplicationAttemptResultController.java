package pl.czyz.socialmultiplication.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.czyz.socialmultiplication.domain.MultiplicationResultAttempt;
import pl.czyz.socialmultiplication.service.MultiplicationService;

import java.util.List;

@RestController
@RequestMapping("/results")
public class MultiplicationAttemptResultController {

    private final MultiplicationService multiplicationService;

    @Autowired
    public MultiplicationAttemptResultController(MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }

    @PostMapping
    ResponseEntity<MultiplicationResultAttempt> postResult(@RequestBody MultiplicationResultAttempt multiplicationResultAttempt) {
        boolean isCorrect = multiplicationService.checkAttempt(multiplicationResultAttempt);
        MultiplicationResultAttempt attemptCopy = new MultiplicationResultAttempt(multiplicationResultAttempt.getUser(),
                multiplicationResultAttempt.getMultiplication(), multiplicationResultAttempt.getResultAttempt(), isCorrect);

        return ResponseEntity.ok(attemptCopy);
    }

    @GetMapping
    ResponseEntity<List<MultiplicationResultAttempt>> getStatistic(@RequestParam("alias") String alias) {
        return ResponseEntity.ok(multiplicationService.getStatsForUser(alias));
    }

    @RequiredArgsConstructor
    @NoArgsConstructor(force = true)
    @Getter
    private static final class ResultResponse {
        private final boolean correct;
    }
}
