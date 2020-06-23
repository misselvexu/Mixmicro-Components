# Mixmicro CircuitBreaker
The CircuitBreaker is implemented via Resilience4j.  

## Getting started

1. Add maven dependency
```xml
<dependency>
    <groupId>com.yunlsp.framework.components</groupId>
    <artifactId>mixmicro-components-starter-circuitbreaker</artifactId>
</dependency>
```

2. Configuration 
```properties
mixmicro:
  circuit-breaker:
   type: r4j
   instances:
     test1: # circuitbreaker 的名字
       slowCallRateThreshold: 100   # 慢请求比例达到100% 发生熔断
       slowCallDurationThreshold: 60000   #一个请求持续60000ms 算是慢请求
       slidingWindowSize: 3 # 滑动窗口的统计数量
       permittedNumberOfCallsInHalfOpenState: 3  #在半开模式下允许通过的请求数
       slidingWindowType: COUNT_BASED   # 滑动窗口的统计方式  COUNT_BASED 计数        TIME_BASED 这是基于时间
       minimumNumberOfCalls: 3   #最小请求次数用于计算，也就是说只有请求次数达到这个配置后才开始计算是否要熔断
       waitDurationInOpenState: 50s #熔断开启后等待的时间
       failureRateThreshold: 50  # 请求失败的比例
       eventConsumerBufferSize: 5   #用于消费事件的配置
       automaticTransitionFromOpenToHalfOpenEnabled: true  #如果设置为true，则表示断路器将自动从开到半开状态转换，不需要调用触发转换。
```

## Examples

```java
@Service
public class V2CircuitBreakerTestImpl extends AbstractMixmicroResilience4jCircuitBreaker {

    @MixmicroCircuitBreakerAction(name = "test1",fallbackMethod = "v2fallback")
    public Object test1() {
        int i = 1 / 0;
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
        return "hello";
    }

    public String v2Test2fallback(String name,String password) throws Exception{
        return name + "服务降级";
    }

    public String v2fallback() throws Exception{
        return "服务降级";
    }
}
```

## Attention
* MixmicroCircuitBreakerAction annotations in the name of the configuration must be you the properties of the corresponding circuitbreaker name, or use the default configuration.
* MixmicroCircuitBreakerAction annotations configured in fallback method name must be implemented in this way, you call the default implementation if cannot find.
* fallback methods must have the same arguments as you business methods,or the default implementation will be called.