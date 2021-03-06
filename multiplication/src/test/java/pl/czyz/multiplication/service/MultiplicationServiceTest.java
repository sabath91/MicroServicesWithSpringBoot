package pl.czyz.multiplication.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.czyz.multiplication.domain.Multiplication;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore("Not needed since, we have unit test for implementation")
public class MultiplicationServiceTest {

  @MockBean private RandomGeneratorService randomGeneratorService;

  @Autowired private MultiplicationService multiplicationService;

  @Test
  public void createRandomMultiplicationTest() {
    given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

    Multiplication multiplication = multiplicationService.createRandomMultiplication();

    assertThat(multiplication.getFactorA()).isEqualTo(50);
    assertThat(multiplication.getFactorB()).isEqualTo(30);
  }
}
