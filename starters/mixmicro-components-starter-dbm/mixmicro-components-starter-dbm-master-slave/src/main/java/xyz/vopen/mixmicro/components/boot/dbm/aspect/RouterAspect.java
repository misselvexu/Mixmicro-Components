package xyz.vopen.mixmicro.components.boot.dbm.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import xyz.vopen.mixmicro.components.boot.dbm.MixmicroDBMRouter;
import xyz.vopen.mixmicro.kits.ReflectUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;

/**
 * {@link RouterAspect}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 7/17/20
 */
@Aspect
public class RouterAspect {

  private static final Logger log = LoggerFactory.getLogger(RouterAspect.class);

  private static final String DATASOURCE_SWITCH_COMMAND = "sctl:hint set MASTER_ONLY=true";

  private static final String DATASOURCE_CLEAR_COMMAND = "sctl:hint clear";

  final private JdbcTemplate jdbcTemplate;

  public RouterAspect(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  // ======= DEFINE DBM ROUTER SWITCH ANNOTATION POINT .

  @Pointcut("@annotation(xyz.vopen.mixmicro.components.boot.dbm.MixmicroDBMRouter)")
  public void dataSourceRouterPointCut() {
  }

  // ======= REAL PROXY METHODS

  /**
   * Process Biz Around Target Proxy Method .
   *
   * @param point instance of {@link ProceedingJoinPoint}
   * @return target method execute result .
   * @throws Throwable maybe thrown {@link Throwable}
   */
  @Around("dataSourceRouterPointCut()")
  public Object doAround(ProceedingJoinPoint point) throws Throwable {

    boolean isSwitch = false;

    try {
      Method method = fetchTargetMethod(point);

      if (method.isAnnotationPresent(MixmicroDBMRouter.class)) {
        MixmicroDBMRouter router = method.getAnnotation(MixmicroDBMRouter.class);
        if (router != null) {
          isSwitch = router.isMasterRouteOnly();
        }
      }
    } catch (Exception e) {
      log.warn("[DBM] -ignore- parse target object real method object exception . ", e);
    }

    if (isSwitch) {
      try {
        log.debug("[DBM] trying switch master datasource .");
        jdbcTemplate.execute(DATASOURCE_SWITCH_COMMAND);
      } catch (Exception e) {
        log.warn("[DBM] -ignore- hint command execute exception . ", e);
      }
    }
    try {
      return point.proceed();
    } finally {
      if (isSwitch) {
        try {
          jdbcTemplate.execute(DATASOURCE_CLEAR_COMMAND);
          log.debug("[DBM] clear-ed switch master datasource status .");
        } catch (Exception e) {
          log.warn("[DBM] -ignore- hint command clear exception . ", e);
        }
      }
    }

  }

  /**
   * Parse Target Method Instance
   *
   * @param point instance of {@link ProceedingJoinPoint}
   * @return target method execute result .
   */
  private Method fetchTargetMethod(ProceedingJoinPoint point) {

    String methodName = point.getSignature().getName();
    Object object = point.getTarget();
    Object[] args = point.getArgs();
    Class<?>[] argTypes = new Class[args.length];
    for (int i = 0; i < args.length; i++) {
      argTypes[i] = args[i].getClass();
    }
    return ReflectUtils.getMethod(object.getClass(), methodName, argTypes);
  }

}
