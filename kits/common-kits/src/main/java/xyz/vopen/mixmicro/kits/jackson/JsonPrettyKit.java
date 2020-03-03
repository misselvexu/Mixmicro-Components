/*
 * Copyright vopen.xyz
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
package xyz.vopen.mixmicro.kits.jackson;

/**
 * Json pretty print
 *
 * @author misselvexu
 */
public final class JsonPrettyKit {

  /** 打印输入到控制台 */
  public static void printJson(String jsonStr) {
    System.out.println(formatJson(jsonStr));
  }

  /** 格式化 */
  public static String formatJson(String jsonStr) {
    if (null == jsonStr || "".equals(jsonStr)) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    char last = '\0';
    char current = '\0';
    int indent = 0;
    for (int i = 0; i < jsonStr.length(); i++) {
      last = current;
      current = jsonStr.charAt(i);
      switch (current) {
        case '{':
        case '[':
          sb.append(current);
          sb.append('\n');
          indent++;
          addIndentBlank(sb, indent);
          break;
        case '}':
        case ']':
          sb.append('\n');
          indent--;
          addIndentBlank(sb, indent);
          sb.append(current);
          break;
        case ',':
          sb.append(current);
          if (last != '\\') {
            sb.append('\n');
            addIndentBlank(sb, indent);
          }
          break;
        default:
          sb.append(current);
      }
    }

    return sb.toString();
  }

  /** 添加space */
  private static void addIndentBlank(StringBuilder sb, int indent) {
    for (int i = 0; i < indent; i++) {
      sb.append('\t');
    }
  }
}
