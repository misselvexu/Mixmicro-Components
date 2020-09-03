package xyz.vopen.mixmicro.components.mongo.client.aggregation;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

/** Defines an accumulator for use in an aggregation pipeline. */
public class Accumulator implements AggregationElement {
  private final String operation;
  private final Object value;

  /**
   * Defines an accumulator for use in an aggregation pipeline.
   *
   * @param operation the accumulator operation
   * @param field the field to use
   */
  public Accumulator(final String operation, final String field) {
    this(operation, (Object) ("$" + field));
  }

  /**
   * Defines an accumulator for use in an aggregation pipeline.
   *
   * @param operation the accumulator operation
   * @param field the field to use
   */
  public Accumulator(final String operation, final Object field) {
    this.operation = operation;
    this.value = field;
  }

  /**
   * Defines an accumulator for use in an aggregation pipeline.
   *
   * @param operation the accumulator operation
   * @param field the field to use
   * @return an Accumulator
   */
  public static Accumulator accumulator(final String operation, final String field) {
    return new Accumulator(operation, field);
  }

  /**
   * Defines an accumulator for use in an aggregation pipeline.
   *
   * @param operation the accumulator operation
   * @param field the field to use
   * @return an Accumulator
   */
  public static Accumulator accumulator(final String operation, final Object field) {
    return new Accumulator(operation, field);
  }

  /**
   * @return the value for this accumulator
   * @deprecated use {@link #getValue()}
   */
  @Deprecated
  public Object getField() {
    return getValue();
  }

  /** @return the operation for this accumulator */
  public String getOperation() {
    return operation;
  }

  /** @return the value for this accumulator */
  public Object getValue() {
    return value;
  }

  @Override
  public DBObject toDBObject() {
    BasicDBObject dbObject = new BasicDBObject();
    if (value instanceof List) {
      List<Object> dbValue = new ArrayList<Object>();
      for (Object o : (List) value) {
        if (o instanceof AggregationElement) {
          dbValue.add(((AggregationElement) o).toDBObject());
        } else {
          dbValue.add(o);
        }
      }
      dbObject.put(operation, dbValue);
    } else if (value instanceof AggregationElement) {
      dbObject.put(operation, ((AggregationElement) value).toDBObject());
    } else {
      dbObject.put(operation, value);
    }

    return dbObject;
  }
}
