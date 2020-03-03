package xyz.vopen.mixmicro.kits.compress.processor;

import xyz.vopen.mixmicro.kits.compress.CompressProcessor;
import xyz.vopen.mixmicro.kits.compress.bean.BinaryFile;
import xyz.vopen.mixmicro.kits.compress.compressor.FileCompressor;
import xyz.vopen.mixmicro.kits.compress.kits.CompressLoggerKits;
import xyz.vopen.mixmicro.kits.compress.kits.FileKits;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/** @author Elve.Xu */
@SuppressWarnings("Duplicates")
public class CpioProcessor implements CompressProcessor {

  @Override
  public byte[] compressData(FileCompressor fileCompressor) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    CpioArchiveOutputStream aos = new CpioArchiveOutputStream(baos);
    try {
      for (BinaryFile binaryFile : fileCompressor.getMapBinaryFile().values()) {
        CpioArchiveEntry entry =
            new CpioArchiveEntry(binaryFile.getDesPath(), binaryFile.getActualSize());
        aos.putArchiveEntry(entry);
        aos.write(binaryFile.getData());
        aos.closeArchiveEntry();
      }
      aos.flush();
      aos.finish();
    } catch (Exception e) {
      FileCompressor.LOGGER.error("Error on compress data", e);
    } finally {
      aos.close();
      baos.close();
    }
    return baos.toByteArray();
  }

  @Override
  public void read(String srcPath, FileCompressor fileCompressor) throws Exception {
    long t1 = System.currentTimeMillis();
    byte[] data = FileKits.convertFileToByte(srcPath);
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    CpioArchiveInputStream ais = new CpioArchiveInputStream(bais);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      byte[] buffer = new byte[1024];
      int readByte;
      CpioArchiveEntry entry = ais.getNextCPIOEntry();
      while (entry != null && entry.getSize() > 0) {
        long t2 = System.currentTimeMillis();
        baos = new ByteArrayOutputStream();
        readByte = ais.read(buffer);
        while (readByte != -1) {
          baos.write(buffer, 0, readByte);
          readByte = ais.read(buffer);
        }
        BinaryFile binaryFile = new BinaryFile(entry.getName(), baos.toByteArray());
        fileCompressor.addBinaryFile(binaryFile);
        CompressLoggerKits.createAddFileLog(
            fileCompressor, binaryFile, t2, System.currentTimeMillis());
        entry = ais.getNextCPIOEntry();
      }
    } catch (Exception e) {
      FileCompressor.LOGGER.error("Error on get compressor file", e);
    } finally {
      baos.close();
      ais.close();
      bais.close();
    }
    CompressLoggerKits.createReadLog(
        fileCompressor, srcPath, data.length, t1, System.currentTimeMillis());
  }
}
