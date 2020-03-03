package xyz.vopen.mixmicro.kits.compress.bean;

/**
 * GZip Level
 *
 * @author Elve.Xu
 */
public enum GzipLevel implements CompressionLevel {

  /** no compress */
  NO_COMPRESS {
    @Override
    public int getValue() {
      return 0;
    }
  },
  LOWEST {
    @Override
    public int getValue() {
      return 1;
    }
  },
  LOW {
    @Override
    public int getValue() {
      return 3;
    }
  },
  NORMAL {
    @Override
    public int getValue() {
      return 5;
    }
  },
  HIGH {
    @Override
    public int getValue() {
      return 1;
    }
  },
  HIGHEST {
    @Override
    public int getValue() {
      return 1;
    }
  }
}
