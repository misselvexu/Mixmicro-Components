/*
 * MIT License
 *
 * <p>Copyright (c) 2021 mixmicro
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package xyz.vopen.mixmicro.kits.llc.impls;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import xyz.vopen.mixmicro.kits.llc.AbstractLlcImageCompress;
import xyz.vopen.mixmicro.kits.llc.Injection;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * {@link LlcImageCompress} The image related.
 *
 * @author <a href="mailto:siran0611@gmail.com">Elias.Yao</a>
 * @version ${project.version} - 2021/4/14
 */
@Injection(name = "Image")
public class LlcImageCompress extends AbstractLlcImageCompress {

  public LlcImageCompress(){
  }

  @Override
  public boolean compressImage(String originPath, String outputPath, float scale, float outputQuality) {
    try {
      Thumbnails.of(originPath)
          .scale(scale)
          .outputQuality(outputQuality)
          .toFile(outputPath);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public boolean compressImage(String originPath, String outputPath, int length, int width) {
    try {
      Thumbnails.of(originPath)
          .size(length, width)
          .toFile(outputPath);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public boolean rotateImage(String originPath, String outputPath, int rotate) {
    try {
      Thumbnails.of(originPath)
          .rotate(rotate)
          .toFile(outputPath);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public boolean tailorImage(String originPath, String outputPath, Positions positions, int length, int width) {
    try {
      Thumbnails.of(originPath)
          .sourceRegion(positions, length, width)
          .toFile(outputPath);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public boolean convertImageFormat(String originPath, String outputPath, String outputFormat) {
    try {
      Thumbnails.of(originPath)
          .outputFormat(outputFormat)
          .toFile(outputPath);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public boolean watermarkImage(String originPath, String outputPath, Positions positions, String watermarkPath,
      float outputQuality) {
    try {
      Thumbnails.of(originPath)
          .watermark(positions, ImageIO.read(new File(watermarkPath)), outputQuality)
          .toFile(outputPath);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
