package xyz.vopen.mixmicro.kits.compress.processor;

import xyz.vopen.mixmicro.kits.compress.CompressProcessor;
import xyz.vopen.mixmicro.kits.compress.bean.BinaryFile;
import xyz.vopen.mixmicro.kits.compress.compressor.FileCompressor;
import xyz.vopen.mixmicro.kits.compress.kits.CompressLoggerKits;
import xyz.vopen.mixmicro.kits.compress.kits.FileKits;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

/** @author Elve.Xu */
@SuppressWarnings("Duplicates")
public class GzipProcessor implements CompressProcessor {

  @Override
  public byte[] compressData(FileCompressor fileCompressor) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    GzipCompressorOutputStream cos = new GzipCompressorOutputStream(baos);
    ZipOutputStream zos = new ZipOutputStream(cos);
    try {
      zos.setLevel(fileCompressor.getLevel().getValue());
      zos.setMethod(ZipOutputStream.DEFLATED);
      zos.setComment(fileCompressor.getComment());
      for (BinaryFile binaryFile : fileCompressor.getMapBinaryFile().values()) {
        zos.putNextEntry(new ZipEntry(binaryFile.getDesPath()));
        zos.write(binaryFile.getData());
        zos.closeEntry();
      }
      zos.flush();
      zos.finish();
    } catch (Exception e) {
      FileCompressor.LOGGER.error("Error on compress data", e);
    } finally {
      zos.close();
      cos.close();
      baos.close();
    }
    return baos.toByteArray();
  }

  @Override
  public void read(String srcPath, FileCompressor fileCompressor) throws Exception {
    long t1 = System.currentTimeMillis();
    byte[] data = FileKits.convertFileToByte(srcPath);
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    GzipCompressorInputStream cis = new GzipCompressorInputStream(bais);
    ZipInputStream zis = new ZipInputStream(cis);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      byte[] buffer = new byte[1024];
      int readByte;
      ZipEntry entry = zis.getNextEntry();
      while (entry != null) {
        long t2 = System.currentTimeMillis();
        baos = new ByteArrayOutputStream();
        readByte = zis.read(buffer);
        while (readByte != -1) {
          baos.write(buffer, 0, readByte);
          readByte = zis.read(buffer);
        }
        zis.closeEntry();
        BinaryFile binaryFile = new BinaryFile(entry.getName(), baos.toByteArray());
        fileCompressor.addBinaryFile(binaryFile);
        CompressLoggerKits.createAddFileLog(fileCompressor, binaryFile, t2, System.currentTimeMillis());
        entry = zis.getNextEntry();
      }
    } catch (Exception e) {
      FileCompressor.LOGGER.error("Error on get compressor file", e);
    } finally {
      baos.close();
      zis.close();
      cis.close();
      bais.close();
    }
    CompressLoggerKits.createReadLog(fileCompressor, srcPath, data.length, t1, System.currentTimeMillis());
  }
}
