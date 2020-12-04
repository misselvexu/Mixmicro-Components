package xyz.vopen.mixmicro.components.boot.httpclient;

import okhttp3.Interceptor;

/**
 * Interceptor marking interface The scope of the implementation class of this interface in the
 * spring container will be automatically modified to prototype
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface PrototypeInterceptor extends Interceptor {}
