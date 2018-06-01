package pl.czyz.socialmultiplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.czyz.socialmultiplication.domain.Multiplication;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class MultiplicationServiceImplTest {

    private MultiplicationService multiplicationService;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Before
    public void setUp(){
        initMocks(this);
        this.multiplicationService = new MultiplicationServiceImpl(randomGeneratorService);
    }

    @Test
    public void createRandomMultiplicationTest(){
        given(randomGeneratorService.generateRandomFactor()).willReturn(50,30);

        Multiplication multiplication = multiplicationService.createRandomMultiplication();

        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
        assertThat(multiplication.getResult()).isEqualTo(1500);
    }

}