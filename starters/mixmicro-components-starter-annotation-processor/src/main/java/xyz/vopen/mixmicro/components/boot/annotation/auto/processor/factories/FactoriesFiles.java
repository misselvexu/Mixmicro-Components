package xyz.vopen.mixmicro.components.boot.annotation.auto.processor.factories;

import lombok.experimental.UtilityClass;
import xyz.vopen.mixmicro.components.boot.annotation.auto.processor.common.MultiSetMap;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.StringJoiner;

/**
 * spring boot 自动化配置工具类
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@UtilityClass
class FactoriesFiles {

  /**
   * 写出 spring.factories 文件
   *
   * @param factories factories 信息
   * @param output 输出流
   * @throws IOException 异常信息
   */
  static void writeFactoriesFile(MultiSetMap<String, String> factories, OutputStream output)
      throws IOException {
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
    Set<String> keySet = factories.keySet();
    for (String key : keySet) {
      Set<String> values = factories.get(key);
      if (values == null || values.isEmpty()) {
        continue;
      }
      writer.write(key);
      writer.write("=\\\n  ");
      StringJoiner joiner = new StringJoiner(",\\\n  ");
      for (String value : values) {
        joiner.add(value);
      }
      writer.write(joiner.toString());
      writer.newLine();
    }
    writer.flush();
    output.close();
  }

  /**
   * 写出 spring-devtools.properties
   *
   * @param projectName 项目名
   * @param output 输出流
   * @throws IOException 异常信息
   */
  static void writeDevToolsFile(String projectName, OutputStream output) throws IOException {
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
    // restart.include.mica-cloud=/mica-cloud[\\w-]+\.jar
    String format = "restart.include.%s=/%s[\\\\w-]+\\.jar";
    writer.write(String.format(format, projectName, projectName));
    writer.flush();
    output.close();
  }
}
