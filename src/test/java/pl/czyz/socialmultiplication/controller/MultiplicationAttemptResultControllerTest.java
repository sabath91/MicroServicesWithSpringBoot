package pl.czyz.socialmultiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.http.HttpStatus;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.czyz.socialmultiplication.domain.Multiplication;
import pl.czyz.socialmultiplication.domain.MultiplicationResultAttempt;
import pl.czyz.socialmultiplication.domain.User;
import pl.czyz.socialmultiplication.service.MultiplicationService;

import java.util.List;

import static org.hamcrest.Matchers.is;
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

    @Test
    public void getUserStats() throws Exception {
        //given
        User user = new User("john");
        Multiplication multiplication = new Multiplication(50, 70);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3500, true);
        List<MultiplicationResultAttempt> recentAttempts = Lists.newArrayList(attempt, attempt);
        given(multiplicationService.getStatsForUser("john")).willReturn(recentAttempts);
        ObjectMapper mapper = new ObjectMapper();

        //@formatter:off
        RestAssuredMockMvc
                .given()
                    .param("alias", "john")
                .get("/results")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body(is(mapper.writeValueAsString(recentAttempts)));
//       @formatter:on

    }

}