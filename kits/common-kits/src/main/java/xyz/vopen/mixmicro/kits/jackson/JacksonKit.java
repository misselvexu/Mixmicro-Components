/*
 * Copyright 1999-2018 Mixmicro+ Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.vopen.mixmicro.kits.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.experimental.UtilityClass;

/**
 * Jackson Utils
 *
 * @author Elve.Xu [iskp.me<at>gmail.com]
 * @version 2.1.0 - 2018/5/24.
 */
@UtilityClass
public final class JacksonKit {

  private static ObjectMapper getInstance() {
    return InstanceHolder.INSTANCE;
  }

  /** javaBean,list,array convert to json string */
  public static String objectToJson(Object obj) {
    try {
      return InstanceHolder.INSTANCE.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "";
  }

  /** json string convert to javaBean */
  public static <T> T jsonToObject(String jsonStr, Class<T> clazz) {
    try {
      return InstanceHolder.INSTANCE.readValue(jsonStr, clazz);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /** json string convert to map */
  public static <T> Map<String, Object> jsonToMap(String jsonStr) {
    try {
      //noinspection unchecked
      return InstanceHolder.INSTANCE.readValue(jsonStr, Map.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /** json string convert to map with javaBean */
  public static <T> Map<String, T> jsonToMap(String jsonStr, Class<T> clazz) {
    try {
      Map<String, Map<String, Object>> map =
          InstanceHolder.INSTANCE.readValue(jsonStr, new TypeReference<Map<String, T>>() {});
      Map<String, T> result = new HashMap<String, T>();
      for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
        result.put(entry.getKey(), mapToObject(entry.getValue(), clazz));
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /** json array string convert to list with javaBean */
  public static <T> List<T> jsonToList(String jsonArrayStr, Class<T> clazz) {
    try {
      List<Map<String, Object>> list =
          InstanceHolder.INSTANCE.readValue(jsonArrayStr, new TypeReference() {});
      List<T> result = new ArrayList<T>();
      for (Map<String, Object> map : list) {
        result.add(mapToObject(map, clazz));
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /** map convert to javaBean */
  public static <T> T mapToObject(Map map, Class<T> clazz) {
    return InstanceHolder.INSTANCE.convertValue(map, clazz);
  }

  private static class InstanceHolder {

    private static final ObjectMapper INSTANCE = new ObjectMapper();
  }
}
