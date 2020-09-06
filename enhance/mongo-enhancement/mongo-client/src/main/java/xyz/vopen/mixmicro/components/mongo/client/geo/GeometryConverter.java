package xyz.vopen.mixmicro.components.mongo.client.geo;

import com.mongodb.DBObject;
import xyz.vopen.mixmicro.components.mongo.client.converters.SimpleValueConverter;
import xyz.vopen.mixmicro.components.mongo.client.converters.TypeConverter;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;

/**
 * A MixMongo TypeConverter that knows how to turn things that are labelled with the Geometry
 * interface into the correct concrete class, based on the GeoJSON type.
 *
 * <p>Only implements the decode method as the concrete classes can encode themselves without
 * needing a converter. It's when they come out of the database that there's not enough information
 * for MixMongo to automatically create Geometry instances.
 */
public class GeometryConverter extends TypeConverter implements SimpleValueConverter {
  /** Sets up this converter to work with things that implement the Geometry interface */
  public GeometryConverter() {
    super(Geometry.class);
  }

  @Override
  public Object decode(
      final Class<?> targetClass, final Object fromDBObject, final MappedField optionalExtraInfo) {
    DBObject dbObject = (DBObject) fromDBObject;
    String type = (String) dbObject.get("type");
    return getMapper()
        .getConverters()
        .decode(GeoJsonType.fromString(type).getTypeClass(), fromDBObject, optionalExtraInfo);
  }
}
