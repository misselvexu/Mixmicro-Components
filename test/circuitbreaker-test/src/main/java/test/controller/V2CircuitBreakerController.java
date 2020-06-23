package test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.service.V2CircuitBreakerTestImpl;

/**
 * @author: siran.yao
 * @date: 2020/6/23 10:40
 */
@RestController
@RequestMapping("/v2")
public class V2CircuitBreakerController {
    @Autowired
    private V2CircuitBreakerTestImpl v2CircuitBreakerTest;

    @RequestMapping("/test1")
    public Object test1(){
        return v2CircuitBreakerTest.test1();
    }

    @RequestMapping("/test2")
    public Object test2(){
        return v2CircuitBreakerTest.test2("hello","12");
    }

    @RequestMapping("/test3")
    public Object test3(){
        return v2CircuitBreakerTest.test3();
    }
}
