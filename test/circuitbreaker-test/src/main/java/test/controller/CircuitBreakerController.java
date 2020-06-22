package test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.service.CircuitBreakerTestImpl;

/**
 * @author: siran.yao
 * @date: 2020/6/22 17:59
 */
@RestController
@RequestMapping("/")
public class CircuitBreakerController {
    @Autowired
    private CircuitBreakerTestImpl circuitBreakerTest;

    @RequestMapping("/test1")
    public Object test1(){
        return circuitBreakerTest.executeWrapper("normal");
    }

    @RequestMapping("/test2")
    public Object test2(){
        return circuitBreakerTest.executeWrapper("abnormal");
    }
}
