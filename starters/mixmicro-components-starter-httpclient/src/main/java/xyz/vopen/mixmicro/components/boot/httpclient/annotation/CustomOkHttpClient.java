package xyz.vopen.mixmicro.components.boot.httpclient.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface CustomOkHttpClient {}
