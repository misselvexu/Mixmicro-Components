package xyz.vopen.framework.logging.client.notice.support;

import com.alibaba.fastjson.JSON;
import xyz.vopen.framework.logging.client.LoggingFactoryBean;
import xyz.vopen.framework.logging.client.notice.LoggingNotice;
import xyz.vopen.framework.logging.core.MixmicroLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.framework.util.JsonUtil;

/**
 * Local console log notification
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LoggingLocalNotice implements LoggingNotice {
  /** the bean name of {@link LoggingLocalNotice} */
  public static final String BEAN_NAME = "loggingLocalNotice";
  /** logger instance */
  static Logger logger = LoggerFactory.getLogger(LoggingLocalNotice.class);
  /** Logging factory bean {@link LoggingFactoryBean} */
  private LoggingFactoryBean loggingFactoryBean;

  public LoggingLocalNotice(LoggingFactoryBean loggingFactoryBean) {
    this.loggingFactoryBean = loggingFactoryBean;
  }

  /**
   * Output formatted log information according to configuration in console {@link LoggingNotice}
   *
   * @param mixmicroLog Mixmicro Boot Log
   */
  @Override
  public void notice(MixmicroLog mixmicroLog) {
    if (loggingFactoryBean.isShowConsoleLog()) {
      logger.info(
          "Request Uri：{}， Logging：\n{}",
          mixmicroLog.getRequestUri(),
          loggingFactoryBean.isFormatConsoleLog()
              ? JsonUtil.beautifyJson(mixmicroLog)
              : JSON.toJSONString(mixmicroLog));
    }
  }

  @Override
  public int getOrder() {
    return HIGHEST_PRECEDENCE;
  }
}
