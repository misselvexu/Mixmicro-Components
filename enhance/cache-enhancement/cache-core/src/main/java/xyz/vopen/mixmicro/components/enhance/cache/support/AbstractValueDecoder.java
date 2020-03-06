package xyz.vopen.mixmicro.components.enhance.cache.support;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public abstract class AbstractValueDecoder implements Function<byte[], Object> {

  protected boolean useIdentityNumber;

  public AbstractValueDecoder(boolean useIdentityNumber) {
    this.useIdentityNumber = useIdentityNumber;
  }

  protected int parseHeader(byte[] buf) {
    int x = 0;
    x = x | (buf[0] & 0xFF);
    x <<= 8;
    x = x | (buf[1] & 0xFF);
    x <<= 8;
    x = x | (buf[2] & 0xFF);
    x <<= 8;
    x = x | (buf[3] & 0xFF);
    return x;
  }

  protected abstract Object doApply(byte[] buffer) throws Exception;

  @Override
  public Object apply(byte[] buffer) {
    try {
      if (useIdentityNumber) {
        DecoderMap.registerBuildInDecoder();
        int identityNumber = parseHeader(buffer);
        AbstractValueDecoder decoder = DecoderMap.getDecoder(identityNumber);
        Objects.requireNonNull(decoder, "no decoder for identity number:" + identityNumber);
        return decoder.doApply(buffer);
      } else {
        return doApply(buffer);
      }
    } catch (Exception e) {
      throw new CacheEncodeException("decode error", e);
    }
  }

  public boolean isUseIdentityNumber() {
    return useIdentityNumber;
  }
}
