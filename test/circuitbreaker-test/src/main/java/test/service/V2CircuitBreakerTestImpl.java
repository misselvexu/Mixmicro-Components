package test.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.stereotype.Service;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.AbstractMixmicroResilience4jCircuitBreaker;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.CircuitBreakerStatus;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.MixmicroCircuitBreakerAction;

import java.util.concurrent.TimeUnit;

/**
 * @author: siran.yao
 * @date: 2020/6/23 10:40
 */
@Service
public class V2CircuitBreakerTestImpl extends AbstractMixmicroResilience4jCircuitBreaker {

    @MixmicroCircuitBreakerAction(name = "test1",fallbackMethod = "v2fallback")
    public Object test1() {
//        int i = 1 / 0;
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello";
    }

    @MixmicroCircuitBreakerAction(name = "test1",fallbackMethod = "v2Test2fallback")
    public String test2(String name,String password){
        int i = 1 / 0;
        return "hello";
    }

    @MixmicroCircuitBreakerAction(name = "test1",fallbackMethod = "v2fallback")
    public String test3(){
        firing(121,new Throwable());
        CircuitBreakerStatus status = getStatus();
        System.out.println(status.toString());
        return "hello";
    }

    public String v2Test2fallback(String name,String password) throws Exception{
        return name + "服务降级";
    }

    public String v2fallback() throws Exception{
        return "服务降级";
    }


}
