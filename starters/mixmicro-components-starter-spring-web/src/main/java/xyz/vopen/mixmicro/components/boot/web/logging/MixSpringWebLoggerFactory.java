package xyz.vopen.mixmicro.components.boot.web.logging;

import xyz.vopen.mixmicro.components.logger.core.LoggerSpaceManager;
import xyz.vopen.mixmicro.components.logger.core.SpaceId;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link MixSpringWebLoggerFactory}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-16.
 */
public class MixSpringWebLoggerFactory {

  /**
   * Spring Web Space Defined.
   *
   * <p>
   */
  public static final String SPRING_WEB_REQUEST_SPACE = "spring.web.request";

  private static final String APPNAME = "appname";

  private static final String DEFAULT_NAME = "mix-framework";

  /**
   * * 获取日志对象
   *
   * @param clazz 日志的名字
   * @return 日志实现
   */
  public static org.slf4j.Logger getLogger(Class<?> clazz) {
    if (clazz == null) {
      return null;
    }
    return getLogger(clazz.getCanonicalName(), DEFAULT_NAME);
  }

  /**
   * 获取日志对象
   *
   * @param name 日志的名字
   * @return 日志实现
   */
  public static org.slf4j.Logger getLogger(String name, String appname) {

    if (name == null || name.isEmpty()) {
      return null;
    }

    Map<String, String> properties = new HashMap<>();

    properties.put(APPNAME, appname == null ? "" : appname);

    SpaceId spaceId = new SpaceId(SPRING_WEB_REQUEST_SPACE);

    if (appname != null) {
      spaceId.withTag(APPNAME, appname);
    }

    return LoggerSpaceManager.getLoggerBySpace(name, spaceId, properties);
  }
}
