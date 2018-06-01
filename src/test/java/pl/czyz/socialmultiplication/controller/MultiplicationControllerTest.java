package pl.czyz.socialmultiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.czyz.socialmultiplication.domain.Multiplication;
import pl.czyz.socialmultiplication.service.MultiplicationService;

import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static io.restassured.module.mockmvc.matcher.RestAssuredMockMvcMatchers.*;


public class MultiplicationControllerTest {

    @Mock
    private MultiplicationService multiplicationService;

    @Before
    public void setUp() {
        initMocks(this);
        RestAssuredMockMvc.standaloneSetup(new MultiplicationController(multiplicationService));
    }

    @Test
    public void getRandomMultiplicationTest() {
        given(multiplicationService.createRandomMultiplication()).willReturn(new Multiplication(70, 20));

        RestAssuredMockMvc
            .given()
                .get("/multiplications/random")
            .then()
                .statusCode(SC_OK)
                .body("factorA", equalTo(70))
                .body("factorB", equalTo(20));
    }


}