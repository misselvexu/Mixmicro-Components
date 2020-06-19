package xyz.vopen.mixmicro.components.boot.openfeign.core;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.collect.Maps;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * {@link FeignAttributes}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/19
 */
public final class FeignAttributes {

  private static final TransmittableThreadLocal<Map<String, String>> FEIGN_TRANSPORT_ATTRIBUTES = new TransmittableThreadLocal<>();

  public static Map<String, String> getAttributes() {
    return FEIGN_TRANSPORT_ATTRIBUTES.get();
  }

  public static void addAttributes(Map<String, String> data) {
    Map<String, String> originData = getAttributes();
    if (ObjectUtils.isEmpty(originData)) {
      originData = Maps.newConcurrentMap();
    }
    originData.putAll(data);
    FEIGN_TRANSPORT_ATTRIBUTES.set(originData);
  }

  public static void remove() {
    try {
      FEIGN_TRANSPORT_ATTRIBUTES.remove();
    } catch (Exception ignore) {
    }
  }
}
