package pl.czyz.multiplication.controller;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.czyz.multiplication.domain.Multiplication;
import pl.czyz.multiplication.service.MultiplicationService;

public class MultiplicationControllerTest {

  @Mock private MultiplicationService multiplicationService;

  @Before
  public void setUp() {
    initMocks(this);
    RestAssuredMockMvc.standaloneSetup(new MultiplicationController(multiplicationService));
  }

  @Test
  public void getRandomMultiplicationTest() {
    given(multiplicationService.createRandomMultiplication())
        .willReturn(new Multiplication(70, 20));

    // @formatter:off
    RestAssuredMockMvc.given()
        .get("/multiplications/random")
        .then()
        .statusCode(SC_OK)
        .body("factorA", equalTo(70))
        .body("factorB", equalTo(20));
    // @formatter:on
  }
}
