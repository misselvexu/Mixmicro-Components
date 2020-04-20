package xyz.vopen.mixmicro.components.boot.web.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import xyz.vopen.mixmicro.components.boot.web.MixmicroWebConfigProperties;
import xyz.vopen.mixmicro.components.boot.web.logging.MixSpringWebLoggerFactory;
import xyz.vopen.mixmicro.kits.executor.AsyncRuntimeExecutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static xyz.vopen.mixmicro.components.common.SerializableBean.encode;

/**
 * {@link WebApiAspect}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-16.
 */
@Aspect
public class WebApiAspect {

  private final MixmicroWebConfigProperties properties;

  private final Logger log;

  private ThreadLocal<WebApiBean.WebApiBeanBuilder> local = new ThreadLocal<>();

  public WebApiAspect(MixmicroWebConfigProperties properties) {
    this.properties = properties;
    this.log =
        MixSpringWebLoggerFactory.getLogger(
            WebApiAspect.class.getCanonicalName(), this.properties.getAppname());
  }

  /**
   * Defined {@link WebApi} annotation pointcut
   *
   * <p>
   */
  @Pointcut("@annotation(xyz.vopen.mixmicro.components.boot.web.aspect.WebApi)")
  public void webApiAnnotationPointcut() {}

  @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
  public void requestMappingPointcut() {}

  @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
  public void getMappingPointcut() {}

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
  public void postMappingPointcut() {}

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
  public void putMappingPointcut() {}

  @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
  public void deleteMappingPointcut() {}

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
  public void patchMappingPointcut() {}

  @Pointcut("requestMappingPointcut() || getMappingPointcut() || postMappingPointcut() " +
      "|| putMappingPointcut() || deleteMappingPointcut() || patchMappingPointcut()")
  public void requests() {}

  // ==

  @Around("requests() || webApiAnnotationPointcut()")
  public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

    try {
      ServletRequestAttributes attributes =
          (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attributes != null) {
        HttpServletRequest request = attributes.getRequest();
        String methodDescription = getAspectLogDescription(proceedingJoinPoint);
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String className = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        String remoteAddress = request.getRemoteAddr();
        String params = getParams(proceedingJoinPoint);

        WebApiBean.WebApiBeanBuilder builder =
            WebApiBean.builder()
                .className(className)
                .method(method)
                .url(url)
                .methodDescription(methodDescription)
                .methodName(methodName)
                .params(params)
                .startTime(System.currentTimeMillis())
                .remoteAddress(remoteAddress);

        local.set(builder);
      }
    } catch (Exception ignored) {
    }

    // EXECUTE
    Object o = proceedingJoinPoint.proceed();

    try{
      WebApiBean.WebApiBeanBuilder builder = local.get();
      if (builder != null) {
        builder.response(o);
      }
    } catch (Exception ignored) {
    }
    return o;
  }

  @After("requests() || webApiAnnotationPointcut()")
  public void doAfter() {
    try {
      WebApiBean.WebApiBeanBuilder builder = local.get();
      builder.endTime(System.currentTimeMillis());
      WebApiBean bean = builder.build();

      // buffered .
      AsyncRuntimeExecutor.getAsyncThreadPool().execute(() -> log.info(encode(bean)));

    } catch (Exception ignore) {
    } finally{
      local.remove();
    }
  }

  /**
   * Get Aspect Web Api Description
   *
   * @param joinPoint point
   * @return description
   * @throws Exception maybe thrown exception
   */
  public String getAspectLogDescription(JoinPoint joinPoint) throws Exception {
    String targetName = joinPoint.getTarget().getClass().getName();
    String methodName = joinPoint.getSignature().getName();
    Object[] arguments = joinPoint.getArgs();
    Class<?> targetClass = Class.forName(targetName);
    Method[] methods = targetClass.getMethods();
    StringBuilder description = new StringBuilder("");
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        Class<?>[] classes = method.getParameterTypes();
        if (classes.length == arguments.length) {
          if(method.isAnnotationPresent(WebApi.class)) {
            description.append(method.getAnnotation(WebApi.class).description());
            break;
          }
        }
      }
    }
    return description.toString();
  }

  /**
   * Parse Method Args .
   *
   * @param joinPoint point
   * @return args json string
   */
  private String getParams(JoinPoint joinPoint) {
    StringBuilder params = new StringBuilder();
    if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
      for (int i = 0; i < joinPoint.getArgs().length; i++) {
        Object arg = joinPoint.getArgs()[i];
        if ((arg instanceof HttpServletResponse)
            || (arg instanceof HttpServletRequest)
            || (arg instanceof MultipartFile)
            || (arg instanceof MultipartFile[])) {
          continue;
        }
        try {
          params.append(encode(joinPoint.getArgs()[i]));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return params.toString();
  }
}
