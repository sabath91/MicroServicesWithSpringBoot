package pl.czyz.multiplication.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import org.apache.http.HttpStatus;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.czyz.multiplication.domain.Multiplication;
import pl.czyz.multiplication.domain.MultiplicationResultAttempt;
import pl.czyz.multiplication.domain.User;
import pl.czyz.multiplication.excepiotns.MultiplicationResultAttemptNotFoundException;
import pl.czyz.multiplication.service.MultiplicationService;

public class MultiplicationAttemptResultControllerTest {

  @Mock private MultiplicationService multiplicationService;

  @Before
  public void setUp() {
    initMocks(this);
    RestAssuredMockMvc.standaloneSetup(
        new MultiplicationAttemptResultController(multiplicationService));
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
    given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class)))
        .willReturn(correct);

    User user = new User("john");
    Multiplication multiplication = new Multiplication(70, 50);
    MultiplicationResultAttempt attempt =
        new MultiplicationResultAttempt(user, multiplication, 3500, correct);

    // @formatter:off
    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(attempt)
        .when()
        .post("/results")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("correct", equalTo(correct));
    // @formatter:on
  }

  @Test
  public void getUserStats() throws Exception {
    // given
    User user = new User("john");
    Multiplication multiplication = new Multiplication(50, 70);
    MultiplicationResultAttempt attempt =
        new MultiplicationResultAttempt(user, multiplication, 3500, true);
    List<MultiplicationResultAttempt> recentAttempts = Lists.newArrayList(attempt, attempt);
    given(multiplicationService.getStatsForUser("john")).willReturn(recentAttempts);
    ObjectMapper mapper = new ObjectMapper();

    // @formatter:off
    RestAssuredMockMvc.given()
        .param("alias", "john")
        .get("/results")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(is(mapper.writeValueAsString(recentAttempts)));
    //       @formatter:on
  }

  @Test
  public void shouldRespondWith404ErrorWhenResultNotExistsInDatabase()
      throws MultiplicationResultAttemptNotFoundException {

    given(multiplicationService.getResultById(any()))
        .willThrow(new MultiplicationResultAttemptNotFoundException());

    // @formatter:off
    RestAssuredMockMvc.when().get("/results/121").then().statusCode(HttpStatus.SC_NOT_FOUND);
    // @formatter:on

  }

  @Test
  public void shouldGetAttemptDetails() throws MultiplicationResultAttemptNotFoundException {
    final User user = new User("john");
    final Multiplication multiplication = new Multiplication(50, 70);
    final MultiplicationResultAttempt multiplicationResultAttempt =
        new MultiplicationResultAttempt(user, multiplication, 3500, true);

    given(multiplicationService.getResultById(any())).willReturn(multiplicationResultAttempt);

    // @formatter:off
    RestAssuredMockMvc.when()
        .get("/results/1")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("correct", equalTo(true))
        .body("resultAttempt", equalTo(3500))
        .body("user.alias", equalTo("john"))
        .body("multiplication.factorA", equalTo(50))
        .body("multiplication.factorB", equalTo(70));
    // @formatter:on

  }
}
