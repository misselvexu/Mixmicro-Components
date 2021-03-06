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

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import xyz.vopen.mixmicro.kits.llc.Injection;
import xyz.vopen.mixmicro.kits.llc.LlcParallelCompress;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link LlcZipCompress}
 *
 * @author <a href="mailto:siran0611@gmail.com">Elias.Yao</a>
 * @version ${project.version} - 2021/4/14
 */
@Injection(name = "Zip")
public class LlcZipCompress extends LlcParallelCompress {

  private String encoding = "UTF8";
  private static final String ZIP_SUFFIX = ".zip";

  public LlcZipCompress() {}

  @Override
  public boolean compress(
      File[] sourceFiles, String file, String fileName, boolean isDeleteSourceFile) {
    return compress(sourceFiles, new File(file, fileName), isDeleteSourceFile);
  }

  @Override
  public boolean compress(File[] sourceFiles, File file, boolean isDeleteSourceFile) {
    InputStream inputStream = null;
    ZipArchiveOutputStream zipArchiveOutputStream = null;

    if (!file.getName().endsWith(ZIP_SUFFIX)) {
      throw new IllegalArgumentException(
          "Suffix name error, your input filename is: " + file.getName());
    }

    if (sourceFiles == null || sourceFiles.length <= 0) {
      return false;
    }

    try {
      zipArchiveOutputStream = new ZipArchiveOutputStream(file);
      zipArchiveOutputStream.setUseZip64(Zip64Mode.AsNeeded);
      zipArchiveOutputStream.setEncoding(super.getContext().getCompressEncode());
      for (File sourceFile : sourceFiles) {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(sourceFile.getName());
        zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);
        inputStream = new FileInputStream(sourceFile);
        byte[] buffer = new byte[super.getContext().getOutputSize()];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
          zipArchiveOutputStream.write(buffer, 0, length);
        }
      }
      zipArchiveOutputStream.closeArchiveEntry();
      zipArchiveOutputStream.finish();

      if (isDeleteSourceFile) {
        for (File sourceFile : sourceFiles) {
          sourceFile.deleteOnExit();
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        if (null != inputStream) {
          inputStream.close();
        }
        if (null != zipArchiveOutputStream) {
          zipArchiveOutputStream.close();
        }
      } catch (IOException ie) {
        ie.printStackTrace();
      }
    }
    return true;
  }

  @Override
  public boolean decompress(File file, String targetDir) {
    return decompress(file, new File(targetDir));
  }

  @Override
  public boolean decompress(File file, File targetDir) {
    InputStream inputStream = null;
    OutputStream outputStream = null;
    ZipArchiveInputStream zipArchiveInputStream = null;
    ArchiveEntry archiveEntry;
    try {
      inputStream = new FileInputStream(file);
      zipArchiveInputStream = new ZipArchiveInputStream(inputStream, super.getContext().getDecompressEncode());
      while (null != (archiveEntry = zipArchiveInputStream.getNextEntry())) {
        String archiveEntryFileName = archiveEntry.getName();

        File f = new File(targetDir + "/" + extractFolder(archiveEntryFileName));

        if (!f.isDirectory() && !f.mkdirs()) {
          throw new IOException("failed to create directory " + targetDir);
        }

        if (new File(targetDir + "/" + archiveEntryFileName).exists()){
          continue;
        }

        File entryFile = new File(targetDir, archiveEntryFileName);
        byte[] buffer = new byte[super.getContext().getOutputSize()];
        outputStream = new FileOutputStream(entryFile);
        int length;
        while ((length = zipArchiveInputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
      }

      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        if (null != outputStream) {
          outputStream.close();
        }
        if (null != zipArchiveInputStream) {
          zipArchiveInputStream.close();
        }
        if (null != inputStream) {
          inputStream.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public List<String> listFiles(File file) {
    List<String> ret = new LinkedList<>();
    InputStream input = null;
    ZipArchiveInputStream zipArchiveInputStream = null;
    ArchiveEntry archiveEntry;
    try {
      input = new FileInputStream(file);
      zipArchiveInputStream = new ZipArchiveInputStream(input, super.getContext().getDecompressEncode());

      while (null != (archiveEntry = zipArchiveInputStream.getNextEntry())) {
        String archiveEntryFileName = archiveEntry.getName();
        ret.add(archiveEntryFileName);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {

        if (null != zipArchiveInputStream) {
          zipArchiveInputStream.close();
        }
        if (null != input) {
          input.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return ret;
  }
}
