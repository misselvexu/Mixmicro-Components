package xyz.vopen.mixmicro.kits.task.tuples;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link Tuple}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
public class Tuple implements TupleApi {

  private final List<Object> values;
  private final List<String> names = new LinkedList<>();

  public Tuple() {
    values = new ArrayList<>();
  }

  public Tuple(int initialCapacity) {
    values = new ArrayList<>(initialCapacity);
  }

  public Tuple(Tuple otherTuple) {
    this.values = new ArrayList<>(otherTuple.values.size() + 8);
    this.values.addAll(otherTuple.values);
    this.names.addAll(otherTuple.names);
  }

  @Override
  public int size() {
    return values.size();
  }

  @Override
  public boolean contains(String name) {
    return names.contains(name);
  }

  @Override
  public boolean containsValue(Object value) {
    return values.contains(value);
  }

  @Override
  public Object getValue(int index) {
    return values.get(index);
  }

  @Override
  public Object getValue(String name) {
    if (name == null) {
      throw new IllegalArgumentException("not contains empty or null name.");
    }
    int index = names.indexOf(name);
    if (index < 0) {
      return null;
    }
    return values.get(index);
  }

  @Override
  public byte[] getBytes(int index) {
    return TupleConverts.asBytes(getValue(index));
  }

  @Override
  public byte[] getBytes(String name) {
    return TupleConverts.asBytes(getValue(name));
  }

  @Override
  public Character getChar(int index) {
    return TupleConverts.asCharacter(getValue(index));
  }

  @Override
  public Character getChar(String name) {
    return TupleConverts.asCharacter(getValue(name));
  }

  @Override
  public Boolean getBoolean(int index) {
    return TupleConverts.asBoolean(getValue(index));
  }

  @Override
  public Boolean getBoolean(String name) {
    return TupleConverts.asBoolean(getValue(name));
  }

  @Override
  public Byte getByte(int index) {
    return TupleConverts.asByte(getValue(index));
  }

  @Override
  public Byte getByte(String name) {
    return TupleConverts.asByte(getValue(name));
  }

  @Override
  public Short getShort(int index) {
    return TupleConverts.asShort(getValue(index));
  }

  @Override
  public Short getShort(String name) {
    return TupleConverts.asShort(getValue(name));
  }

  @Override
  public Integer getInt(int index) {
    return TupleConverts.asInteger(getValue(index));
  }

  @Override
  public Integer getInt(String name) {
    return TupleConverts.asInteger(getValue(name));
  }

  @Override
  public Long getLong(int index) {
    return TupleConverts.asLong(getValue(index));
  }

  @Override
  public Long getLong(String name) {
    return TupleConverts.asLong(getValue(name));
  }

  @Override
  public Float getFloat(int index) {
    return TupleConverts.asFloat(getValue(index));
  }

  @Override
  public Float getFloat(String name) {
    return TupleConverts.asFloat(getValue(name));
  }

  @Override
  public Double getDouble(int index) {
    return TupleConverts.asDouble(getValue(index));
  }

  @Override
  public Double getDouble(String name) {
    return TupleConverts.asDouble(getValue(name));
  }

  @Override
  public BigDecimal getBigDecimal(int index) {
    return TupleConverts.asBigDecimal(getValue(index));
  }

  @Override
  public BigDecimal getBigDecimal(String name) {
    return TupleConverts.asBigDecimal(getValue(name));
  }

  @Override
  public String getString(int index) {
    return TupleConverts.asString(getValue(index));
  }

  @Override
  public String getString(String name) {
    return TupleConverts.asString(getValue(name));
  }

  @Override
  public List<Object> getValues() {
    List<Object> result = new LinkedList<>();
    Collections.copy(result, values);
    return result;
  }

  @Override
  public void add(Object value) {
    values.add(value);
    names.add(null);
  }

  @Override
  public void add(int index, Object value) {
    values.add(index, value);
    names.add(index, null);
  }

  @Override
  public void add(String name, Object value) {
    if (name == null) {
      throw new IllegalArgumentException("name cannot be null.");
    }
    // 如果有相同名称的值，先将其移除
    int index = names.indexOf(name);
    if (index > -1) {
      names.remove(index);
      values.remove(index);
    }
    values.add(value);
    names.add(name);
  }

  @Override
  public boolean remove(Object value) {
    int index = values.indexOf(value);
    boolean result = values.remove(value);
    if (result) {
      names.remove(index);
    }
    return result;
  }

  @Override
  public Object removeName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("name cannot be null.");
    }
    int index = names.indexOf(name);
    if (index >= 0) {
      return values.remove(index);
    }
    return null;
  }

  @Override
  public Object removeIndex(int index) {
    Object result = values.remove(index);
    names.remove(index);
    return result;
  }

  public void forEach(TupleAction<String, Integer, Object> action) {
    if (action == null) {
      return;
    }
    for (int i = 0, len = values.size(); i < len; i++) {
      action.apply(names.get(i), i, values.get(i));
    }
  }

  @Override
  public int hashCode() {
    return System.identityHashCode(this);
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("[");
    int limit = size() - 1;
    forEach(
        (name, index, value) -> {
          result.append("{");
          if (name != null) {
            result.append("\"").append(name).append("\"");
          } else {
            result.append(index);
          }
          result.append(":").append(value).append("}");
          if (index < limit) {
            result.append(",");
          }
        });
    result.append("]");
    return result.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Tuple other = (Tuple) obj;
    return values.equals(other.values) && names.equals(other.names);
  }
}
