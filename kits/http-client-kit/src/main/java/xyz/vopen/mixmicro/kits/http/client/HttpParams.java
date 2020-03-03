/*
 * Copyright 2006-2017 vopen.xyz
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.vopen.mixmicro.kits.http.client;

import com.google.common.collect.Maps;
import java.io.File;
import java.util.Map;
import org.apache.http.entity.ContentType;

/** @author Zero.zhao */
public class HttpParams {

  private Map<String, String> params = Maps.newIdentityHashMap();
  private ENTITY entity = ENTITY.FORM;
  private ContentType contentType;
  private String value;

  private File file;

  /**
   * 输出map
   *
   * @return
   */
  public Map<String, String> toMap() {
    return params;
  }

  /**
   * 设置参数
   *
   * @param key
   * @param value
   */
  public void put(String key, Object value) {
    params.put(key, String.valueOf(value));
  }

  public void put(Map<String, String> params) {
    if (params != null && params.size() > 0) {
      for (Map.Entry<String, String> entry : params.entrySet()) {
        this.params.put(entry.getKey(), entry.getValue());
      }
    }
  }

  /**
   * 获取参数实体类型
   *
   * @return
   */
  public ENTITY getEntity() {
    return entity;
  }

  /**
   * 设置参数实体类型
   *
   * @param entity
   */
  public void setEntity(ENTITY entity) {
    this.entity = entity;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public ContentType getContentType() {
    return contentType;
  }

  public void setContentType(ContentType contentType) {
    this.contentType = contentType;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public enum ENTITY {
    FORM,
    BYTE,
    STRING,
    MULTIPART
  }
}
