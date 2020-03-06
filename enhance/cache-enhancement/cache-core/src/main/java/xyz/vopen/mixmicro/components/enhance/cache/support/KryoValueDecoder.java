package xyz.vopen.mixmicro.components.enhance.cache.support;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import java.io.ByteArrayInputStream;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class KryoValueDecoder extends AbstractValueDecoder {

    public static final KryoValueDecoder INSTANCE = new KryoValueDecoder(true);

    public KryoValueDecoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    @Override
    public Object doApply(byte[] buffer) {
        ByteArrayInputStream in;
        if (useIdentityNumber) {
            in = new ByteArrayInputStream(buffer, 4, buffer.length - 4);
        } else {
            in = new ByteArrayInputStream(buffer);
        }
        Input input = new Input(in);
        Kryo kryo = (Kryo) KryoValueEncoder.kryoThreadLocal.get()[0];
        kryo.setClassLoader(Thread.currentThread().getContextClassLoader());
        return kryo.readClassAndObject(input);
    }
}
