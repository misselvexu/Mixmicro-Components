package xyz.vopen.mixmicro.kits.compress.bean;

import xyz.vopen.mixmicro.kits.compress.CompressProcessor;
import lombok.Getter;
import xyz.vopen.mixmicro.kits.compress.processor.*;

/**
 * Compression Type Defined
 *
 * @author Elve.Xu
 */
@Getter
public enum CompressionType {

  /** tar gz package */
  TAR_GZ("Tape archive with Gzip", "tar.gz", "application/x-gtar", new TarGzProcessor()),
  TAR_BZ2("Tape archive with Bzip2", "tar.bz2", "application/x-gtar", new TarBz2Processor()),
  TAR("Tape archive", "tar", "application/x-tar", new TarProcessor()),
  ZIP("ZIP", "zip", "application/zip", new ZipProcessor()),
  GZ("GZIP", "gz", "application/x-gzip", new GzipProcessor()),
  BZ2("BZIP2", "bz2", "application/x-bzip2", new Bzip2Processor()),
  AR("Unix Archiver", "ar", null, new ArProcessor()),
  CPIO("CPIO", "cpio", "application/x-cpio", new CpioProcessor()),
  SEVENZIP("7Z", "7z", "application/x-7z-compressed", new SevenZipProcessor()),
  XZ("XZ", "xz", "application/x-xz", new XzProcessor()),
  JAR("JAR", "jar", null, new JarProcessor());

  private String name;
  private String extension;
  private String mimeType;
  private CompressProcessor compressProcessor;

  CompressionType(
      String name, String extension, String mimeType, CompressProcessor compressProcessor) {
    this.name = name;
    this.extension = extension;
    this.mimeType = mimeType;
    this.compressProcessor = compressProcessor;
  }
}
