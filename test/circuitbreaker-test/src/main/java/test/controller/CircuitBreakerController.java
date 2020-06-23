package test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.service.CircuitBreakerTestImpl;
import test.service.CircuitBreakerTestImpl2;

/**
 * @author: siran.yao
 * @date: 2020/6/22 17:59
 */
@RestController
@RequestMapping("/")
public class CircuitBreakerController {

  private final CircuitBreakerTestImpl circuitBreakerTest;
  private final CircuitBreakerTestImpl2 circuitBreakerTest2;

  public CircuitBreakerController(CircuitBreakerTestImpl circuitBreakerTest, CircuitBreakerTestImpl2 circuitBreakerTest2) {
    this.circuitBreakerTest = circuitBreakerTest;
    this.circuitBreakerTest2 = circuitBreakerTest2;
  }

  @RequestMapping("/t1")
  public void test1() {
    circuitBreakerTest.doSomething("normal");
  }

  @RequestMapping("/exception")
  public void test2() {
    circuitBreakerTest.doSomething("exception");
  }

  @RequestMapping("/t3")
  public void test3() {
    circuitBreakerTest2.doSomething("exception");
  }
}
