package xyz.vopen.mixmicro.kits.compress.bean;

/**
 * Zip Level
 *
 * @author Elve.Xu
 */
public enum ZipLevel implements CompressionLevel {

  /** zip file no compress level */
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
