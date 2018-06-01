package pl.czyz.socialmultiplication.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.czyz.socialmultiplication.domain.Multiplication;
import pl.czyz.socialmultiplication.domain.MultiplicationResultAttempt;
import pl.czyz.socialmultiplication.domain.User;
import pl.czyz.socialmultiplication.service.MultiplicationService;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class MultiplicationAttemptResultControllerTest {

    @Mock
    private MultiplicationService multiplicationService;

    @Before
    public void setUp() {
        initMocks(this);
        RestAssuredMockMvc.standaloneSetup(new MultiplicationAttemptResultController(multiplicationService));
    }

    @Test
    public void postResultReturnCorrect() {
        genericParametrizedTest(true);
    }

    @Test
    public void postResultReturnIncorrect() {
        genericParametrizedTest(false);
    }

    private void genericParametrizedTest(final boolean correct) {
        given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class))).willReturn(correct);

        User user = new User("john");
        Multiplication multiplication = new Multiplication(70, 50);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3500, correct);


        //@formatter:off
        RestAssuredMockMvc
                .given()
                    .contentType("application/json")
                    .body(attempt)
                .when()
                    .post("/results")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("correct", equalTo(correct));
        //@formatter:on
    }
}