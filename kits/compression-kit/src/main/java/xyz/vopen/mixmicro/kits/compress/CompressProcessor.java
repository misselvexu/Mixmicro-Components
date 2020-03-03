package xyz.vopen.mixmicro.kits.compress;

import xyz.vopen.mixmicro.kits.compress.compressor.FileCompressor;

/**
 * Compress Processor
 *
 * @author Elve.Xu
 */
public interface CompressProcessor {

  /**
   * Compress data
   *
   * @param fileCompressor FileCompressor object
   * @return compressed data byte array
   * @throws Exception exception
   */
  byte[] compressData(FileCompressor fileCompressor) throws Exception;

  /**
   * Read from compressed file
   *
   * @param srcPath path of compressed file
   * @param fileCompressor FileCompressor object
   * @throws Exception exception
   */
  void read(String srcPath, FileCompressor fileCompressor) throws Exception;
}
