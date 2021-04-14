package xyz.vopen.mixmicro.kits.llc;

import java.io.File;

/**
 * {@link LlcTester}
 *
 * <p>Class LlcTester Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2021/4/14
 */
public class LlcTester {


  /**
   * Zip解压缩
   */
  public static class ZipTester {

    public static void main(String[] args) {

      // 压缩包
      Compress compress =
          Llc.builder().outputSiz(1024 * 4).type(Llc.CompressType.ZIP).build().getCompress();

      // 测试压缩
      File[] files = new File[] {new File("/xxx/xxx.json")};
      compress.compress(files, new File("/xxx/xxx/xxx.zip"), false);

      // 测试解压
      compress.decompress(new File("/xxx/xxx/xxx.zip"), "/xxx/xxx/xxx.json");

    }

  }

  /**
   * 7z解压缩
   */
  public static class X7ZTester {

    public static void main(String[] args) {

      // 压缩包
      Compress compress =
          Llc.builder().outputSiz(1024 * 4).type(Llc.CompressType.SEVENZ).build().getCompress();

      // 测试压缩
      File[] files = new File[] {new File("/xxx/xxx.json")};
      compress.compress(files, new File("/xxx/xxx/xxx.7z"), false);

      // 测试解压
      compress.decompress(new File("/xxx/xxx/xxx.7z"), "/xxx/xxx/xxx.json");

    }

  }

}
