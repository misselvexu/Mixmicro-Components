package test.service;

import org.springframework.stereotype.Service;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.AbstractMixmicroResilience4jCircuitBreaker;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.MixmicroCircuitBreakerAction;

/**
 * @author: siran.yao
 * @date: 2020/6/22 17:43
 */
@Service
public class CircuitBreakerTestImpl2 extends AbstractMixmicroResilience4jCircuitBreaker {

  @MixmicroCircuitBreakerAction(fallbackMethod = "fallback")
  public void doSomething(String args) {

    if ("exception".equalsIgnoreCase(args)) {
      firing(1000, new RuntimeException("executed happened exception ."));
    }

    System.out.println("executed with :" + args);
  }

  public void fallback() {
    System.out.println("circuit breaker process fail");
  }
}
