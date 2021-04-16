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
package xyz.vopen.mixmicro.kits.llc;


import xyz.vopen.mixmicro.kits.llc.Llc.CompressType;

/**
 * {@link LlcBuilder}
 *
 * @author <a href="mailto:siran0611@gmail.com">Elias.Yao</a>
 * @version ${project.version} - 2021/4/13
 */
public class LlcBuilder {

  // true if need to parallel.
  private boolean isParallel;
  // size of single block.
  private long blockSize;
  // the block number.
  private int blocks;
  private CompressType typ;
  private int outputSize;
  private String outputName;
  private boolean ignoreFolder;

  public LlcBuilder() {

  }

  public LlcBuilder isParallel(boolean isParallel) {
    this.isParallel = isParallel;
    return this;
  }

  public LlcBuilder blockSize(long blockSize) {
    this.blockSize = blockSize;
    return this;
  }

  public LlcBuilder blocks(int blocks) {
    this.blocks = blocks;
    return this;
  }

  public LlcBuilder type(CompressType typ) {
    this.typ = typ;
    return this;
  }

  public LlcBuilder outputSiz(int outputSize) {
    this.outputSize = outputSize;
    return this;
  }

  public LlcBuilder outputName(String outputName) {
    this.outputName = outputName;
    return this;
  }

  public LlcBuilder ignoreFolder(boolean ignoreFolder) {
    this.ignoreFolder = ignoreFolder;
    return this;
  }

  public Llc build() {
    return new Llc(
        isParallel,
        blockSize,
        blocks,
        typ,
        outputSize,
        outputName,
        ignoreFolder,
        new LlcContext(blockSize, blocks, outputSize, outputName,ignoreFolder));
  }
}
