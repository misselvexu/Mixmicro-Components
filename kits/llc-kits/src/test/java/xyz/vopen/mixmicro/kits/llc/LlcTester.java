package xyz.vopen.mixmicro.kits.llc;

import java.io.File;
import xyz.vopen.mixmicro.kits.llc.Llc.CompressType;

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
      File[] files = new File[]{new File("/xxx/xxx.json")};
      compress.compress(files, new File("/xxx/xxx/xxx.zip"), false);

      // 显示文件列表
      compress.listFiles(new File("/xxx/xxx/xxx.zip"));

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
      File[] files = new File[]{new File("/xxx/xxx.json")};
      compress.compress(files, new File("/xxx/xxx/xxx.7z"), false);

      // 测试解压
      compress.decompress(new File("/xxx/xxx/xxx.7z"), "/xxx/xxx/xxx.json");

    }

  }

  /**
   * RAR5解压
   */
  public static class RAR5Tester {

    public static void main(String[] args) {

      // 压缩包
      Compress compress =
          Llc.builder().outputSiz(1024 * 4).type(CompressType.RAR5).ignoreFolder(true).build().getCompress();

      compress.listFiles(new File("/Users/eliasyao/Desktop/asa.rar"));
      // 测试解压
      compress.decompress(new File("/Users/eliasyao/Desktop/asa.rar"), "/Users/eliasyao/Desktop/temp");

    }

  }


  /**
   * pdf解压缩
   */
  public static class PdfTester {

    public static void main(String[] args) {

      // 压缩包
      Compress compress =
          Llc.builder().outputSiz(1024 * 4).type(Llc.CompressType.PDF).build().getCompress();

      // 测试压缩pdf
      File[] files = new File[]{new File("/xxx/xxx.pdf")};
      compress.compress(files, new File("/xxx/xxx/xxx.pdf"), false);

    }

  }

}
