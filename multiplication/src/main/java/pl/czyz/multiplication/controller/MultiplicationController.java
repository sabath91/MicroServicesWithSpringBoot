package pl.czyz.multiplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.czyz.multiplication.domain.Multiplication;
import pl.czyz.multiplication.service.MultiplicationService;

@CrossOrigin("*")
@RestController
@RequestMapping("/multiplications")
public class MultiplicationController {

  private final MultiplicationService multiplicationService;

  @Autowired
  public MultiplicationController(final MultiplicationService multiplicationService) {
    this.multiplicationService = multiplicationService;
  }

  @GetMapping("/random")
  Multiplication getRandomMultiplication() {
    return multiplicationService.createRandomMultiplication();
  }
}
