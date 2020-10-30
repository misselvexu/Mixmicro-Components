package xyz.vopen.framework.logging.admin.mongodb.helper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link MongoHelper} mongodb tools
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
public class MongoHelper {

  private static final Logger logger = LoggerFactory.getLogger(MongoHelper.class);
  private static final Pattern COLLECTION_NAME_PATTERN = Pattern.compile("(\\$\\{.*?\\})");

  private MongoHelper() {}

  /**
   * get mongo collection name
   *
   * @param entity mapping entity
   * @param <E> class type
   * @return collection name
   */
  public static <E> String getCollectionName(E entity) {
    Class<E> clazz = getEntityClass(entity);
    String name = clazz.getAnnotation(Document.class).collection();
    Map<String, String> propMap = new HashMap<>(1);
    name = formatCollectionName(name, propMap);
    return name;
  }

  /**
   * get mongo collection name
   *
   * @param entity mapping entity
   * @param <E> class type
   * @return entity class
   */
  @SuppressWarnings("unchecked")
  public static <E> Class<E> getEntityClass(E entity) {
    return (Class<E>) entity.getClass();
  }

  /**
   * 配置文件中如果有${}包含的内容，需要进行替换 如果没有获取到值，则抛系统异常
   *
   * @param content collection name
   * @param propMap collection property
   * @return format collection name
   */
  private static String formatCollectionName(String content, Map<String, String> propMap) {
    // 1. 获取逻辑表名中的占位符列表
    List<String> placeholders = new ArrayList<>();
    Matcher matcher = COLLECTION_NAME_PATTERN.matcher(content);
    while (matcher.find()) {
      placeholders.add(matcher.group());
    }
    if (!CollectionUtils.isEmpty(placeholders)) {
      // 2. 从数据对象中获取占位符对应的值
      List<String> placeholderValues = new ArrayList<>();
      for (String placeholder : placeholders) {
        String placeholderKey = placeholder.substring(2, placeholder.length() - 1);
        String placeholderValue = propMap.get(placeholderKey);
        if (StringUtils.isEmpty(placeholderValue)) {
          logger.warn("Can not get placeholder value for: {}", placeholder);
        }
        placeholderValues.add(placeholderValue);
      }
      // 3. 将逻辑表名中的占位符全部替换成对应的值
      content =
          StringUtils.replaceEach(
              content,
              placeholders.toArray(new String[0]),
              placeholderValues.toArray(new String[0]));
    }
    return content;
  }
}
