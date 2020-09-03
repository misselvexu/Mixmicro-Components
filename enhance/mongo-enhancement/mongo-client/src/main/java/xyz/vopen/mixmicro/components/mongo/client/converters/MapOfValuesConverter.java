package xyz.vopen.mixmicro.components.mongo.client.converters;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.utils.IterHelper;
import xyz.vopen.mixmicro.components.mongo.client.utils.IterHelper.MapIterCallback;
import xyz.vopen.mixmicro.components.mongo.client.utils.ReflectionUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class MapOfValuesConverter extends TypeConverter {
  @Override
  @SuppressWarnings("unchecked")
  public Object decode(final Class targetClass, final Object fromDBObject, final MappedField mf) {
    if (fromDBObject == null) {
      return null;
    }

    final Map values = getMapper().getOptions().getObjectFactory().createMap(mf);
    new IterHelper<Object, Object>()
        .loopMap(
            fromDBObject,
            new MapIterCallback<Object, Object>() {
              @Override
              public void eval(final Object k, final Object val) {
                final Object objKey =
                    getMapper().getConverters().decode(mf.getMapKeyClass(), k, mf);
                values.put(
                    objKey,
                    val != null
                        ? getMapper().getConverters().decode(mf.getSubClass(), val, mf)
                        : null);
              }
            });

    return values;
  }

  @Override
  public Object encode(final Object value, final MappedField mf) {
    if (value == null) {
      return null;
    }

    final Map<?, ?> map = (Map<?, ?>) value;
    if (!map.isEmpty() || getMapper().getOptions().isStoreEmpties()) {
      final Map<Object, Object> mapForDb = new LinkedHashMap<Object, Object>();
      for (final Map.Entry<?, ?> entry : map.entrySet()) {
        final String strKey = getMapper().getConverters().encode(entry.getKey()).toString();
        mapForDb.put(strKey, getMapper().getConverters().encode(entry.getValue()));
      }
      return mapForDb;
    }
    return null;
  }

  @Override
  protected boolean isSupported(final Class<?> c, final MappedField optionalExtraInfo) {
    if (optionalExtraInfo != null) {
      return optionalExtraInfo.isMap();
    } else {
      return ReflectionUtils.implementsInterface(c, Map.class);
    }
  }
}
