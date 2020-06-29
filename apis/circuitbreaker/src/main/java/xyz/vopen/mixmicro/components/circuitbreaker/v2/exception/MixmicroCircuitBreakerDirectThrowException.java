package xyz.vopen.mixmicro.components.circuitbreaker.v2.exception;

/**
 * @author: siran.yao
 * @date: 2020/6/29 21:13
 */
public class MixmicroCircuitBreakerDirectThrowException extends RuntimeException{
    public MixmicroCircuitBreakerDirectThrowException(Throwable cause) {
        super(cause);
    }
}
