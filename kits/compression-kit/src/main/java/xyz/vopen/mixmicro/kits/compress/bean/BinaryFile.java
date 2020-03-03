package xyz.vopen.mixmicro.kits.compress.bean;

import xyz.vopen.mixmicro.kits.compress.kits.FileKits;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * BinaryFile
 *
 * @author Elve.Xu
 */
@Getter
@Setter
public class BinaryFile implements Serializable {
  private static final long serialVersionUID = 1L;

  private String desPath;
  private String srcPath;
  private byte[] data;
  private long actualSize = 0;
  private long compressedSize = 0;

  /**
   * Initialize BinaryFile object
   *
   * @param desPath destination path includes file name
   * @param data byte data
   */
  public BinaryFile(String desPath, byte[] data) {
    desPath = FileKits.getSafePath(desPath);
    this.desPath = FileKits.getSafePath(desPath);
    this.data = data;
    this.actualSize = data.length;
  }

  /**
   * Initialize BinaryFile object
   *
   * @param srcPath source path includes file name
   * @param desPath destination path includes file name
   * @param deleteSrc delete source after convert to binary file or not
   * @throws Exception
   */
  public BinaryFile(String srcPath, String desPath, boolean deleteSrc) throws Exception {
    srcPath = FileKits.getSafePath(srcPath);
    desPath = FileKits.getSafePath(desPath);
    this.srcPath = srcPath;
    this.desPath = desPath;
    this.data = FileKits.convertFileToByte(srcPath);
    this.actualSize = data.length;
    if (deleteSrc) {
      FileKits.delete(srcPath);
    }
  }

  /**
   * Create new instance of BinaryFile from file
   *
   * @param srcPath source path includes file name
   * @param desPath destination path includes file name
   * @param deleteSrc delete source after convert to binary file or not
   * @throws Exception
   */
  public static BinaryFile newInstance(String srcPath, String desPath, boolean deleteSrc)
      throws Exception {
    return new BinaryFile(srcPath, desPath, deleteSrc);
  }
}
