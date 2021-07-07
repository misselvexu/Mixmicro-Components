package xyz.vopen.mixmicro.components.enhance.apidoc.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Type;
import java.util.*;

/**
 * {@link GsonKit} google gson tools
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/10/19
 */
public abstract class GsonKit {

  private static Gson gson = null;

  private GsonKit() {
    throw new InstantiationError("Must not instantiate this class");
  }

  /**
   * object to json string
   *
   * @param object object data
   * @return json string
   */
  public static String beanToString(Object object) {
    return gson.toJson(object);
  }

  /**
   * json string to object
   *
   * @param gsonString json string
   * @param cls object class
   * @param <T> class type
   * @return object
   */
  public static @NotNull <T> T stringToBean(String gsonString, Class<T> cls) {
    return gson.fromJson(gsonString, cls);
  }

  /**
   * json string to list
   *
   * @param gsonString json string
   * @return list
   */
  public static @NotNull <T> List<T> stringToList(String gsonString) {
    return gson.fromJson(gsonString, (new TypeToken<List<T>>() {}).getType());
  }

  /**
   * json string to set
   *
   * @param gsonString json string
   * @return list
   */
  public static @NotNull <T> Set<T> stringToSet(String gsonString) {
    return gson.fromJson(gsonString, (new TypeToken<Set<T>>() {}).getType());
  }

  /**
   * json string to class list
   *
   * @param gsonString json string
   * @param cls object class
   * @param <T> class type
   * @return object list
   */
  public static <T> List<T> stringToBeanList(String gsonString, Class<T> cls) {
    List<T> list = new ArrayList<>();
    JsonArray array = (new JsonParser()).parse(gsonString).getAsJsonArray();
    Iterator<JsonElement> var4 = array.iterator();
    if (var4.hasNext()) {
      do {
        JsonElement elem = var4.next();
        list.add(gson.fromJson(elem, cls));
      } while (var4.hasNext());
    }
    return list;
  }

  /**
   * json string to class set
   *
   * @param gsonString json string
   * @param cls object class
   * @param <T> class type
   * @return object list
   */
  public static <T> Set<T> stringToBeanSet(String gsonString, Class<T> cls) {
    Set<T> set = new HashSet<>();
    JsonArray array = (new JsonParser()).parse(gsonString).getAsJsonArray();
    Iterator<JsonElement> var4 = array.iterator();
    if (var4.hasNext()) {
      do {
        JsonElement elem = var4.next();
        set.add(gson.fromJson(elem, cls));
      } while (var4.hasNext());
    }
    return set;
  }

  /**
   * json string to map list
   *
   * @param gsonString json string
   * @param <T> class type
   * @return map list
   */
  public static @NotNull <T> List<Map<String, T>> stringToListMaps(String gsonString) {
    return gson.fromJson(gsonString, (new TypeToken<List<HashMap<String, T>>>() {}).getType());
  }

  /**
   * json string to map
   *
   * @param gsonString json string
   * @param <T> class type
   * @return map
   */
  public static @NotNull <T> Map<String, T> stringToMap(String gsonString) {
    Type typeToken = new TypeToken<HashMap<String, Object>>() {}.getType();
    return gson.fromJson(gsonString, typeToken);
  }

  /**
   * map to object
   *
   * @param map map
   * @param <T> class type
   * @return map
   */
  public static @NotNull <E, T> T mapToBean(Map<String, E> map, Class<T> cls) {
    String gsonString = gson.toJson(map);
    return stringToBean(gsonString, cls);
  }

  /**
   * object to map
   *
   * @param object object
   * @param <T> class type
   * @return map
   */
  public static @NotNull <T> Map<String, T> beanToMap(Object object) {
    Type typeToken = new TypeToken<HashMap<String, Object>>() {}.getType();
    return gson.fromJson(gson.toJson(object), typeToken);
  }

  /**
   * deep copy bean to another bean
   *
   * @param e param object
   * @param cls result type
   * @param <E> param type
   * @param <T> result type
   * @return target bean response
   */
  public static @NotNull <E, T> T deepCopyBean(E e, Class<T> cls) {
    String gsonString = gson.toJson(e);
    return gson.fromJson(gsonString, cls);
  }

  static {
    // 解决gson格式化日期时，无法序列化timestamp的问题
    gson =
        new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(
                new TypeToken<HashMap<String, Object>>() {}.getType(),
                (JsonDeserializer<HashMap<String, Object>>)
                    (json, typeOfT, context) -> {
                      HashMap<String, Object> hashMap = new HashMap<>();
                      JsonObject jsonObject = json.getAsJsonObject();
                      Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                      for (Map.Entry<String, JsonElement> entry : entrySet) {
                        Object ot = entry.getValue();
                        if (ot instanceof JsonPrimitive) {
                          hashMap.put(entry.getKey(), ((JsonPrimitive) ot).getAsString());
                        } else {
                          hashMap.put(entry.getKey(), ot);
                        }
                      }
                      return hashMap;
                    })
            .create();
  }
}
