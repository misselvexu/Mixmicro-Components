package xyz.vopen.mixmicro.kits.task;

import xyz.vopen.mixmicro.kits.task.tuples.Tuple;
import xyz.vopen.mixmicro.kits.task.tuples.TupleAction;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Data extends Tuple {

  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  @Override
  public int size() {
    lock.readLock().lock();
    try {
      return super.size();
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean contains(String name) {
    lock.readLock().lock();
    try {
      return super.contains(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean containsValue(Object value) {
    lock.readLock().lock();
    try {
      return super.containsValue(value);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Object getValue(int index) {
    lock.readLock().lock();
    try {
      return super.getValue(index);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Object getValue(String name) {
    lock.readLock().lock();
    try {
      return super.getValue(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public byte[] getBytes(int index) {
    lock.readLock().lock();
    try {
      return super.getBytes(index);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public byte[] getBytes(String name) {
    lock.readLock().lock();
    try {
      return super.getBytes(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Character getChar(int index) {
    lock.readLock().lock();
    try {
      return super.getChar(index);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Character getChar(String name) {
    lock.readLock().lock();
    try {
      return super.getChar(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Boolean getBoolean(int index) {
    lock.readLock().lock();
    try {
      return super.getBoolean(index);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Boolean getBoolean(String name) {
    lock.readLock().lock();
    try {
      return super.getBoolean(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Byte getByte(int index) {
    lock.readLock().lock();
    try {
      return super.getByte(index);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Byte getByte(String name) {
    lock.readLock().lock();
    try {
      return super.getByte(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Short getShort(int index) {
    lock.readLock().lock();
    try {
      return super.getShort(index);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Short getShort(String name) {
    lock.readLock().lock();
    try {
      return super.getShort(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Integer getInt(int index) {
    lock.readLock().lock();
    try {
      return super.getInt(index);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Integer getInt(String name) {
    lock.readLock().lock();
    try {
      return super.getInt(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Long getLong(int index) {
    lock.readLock().lock();
    try {
      return super.getLong(index);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Long getLong(String name) {
    lock.readLock().lock();
    try {
      return super.getLong(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Float getFloat(int index) {
    lock.readLock().lock();
    try {
      return super.getFloat(index);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Float getFloat(String name) {
    lock.readLock().lock();
    try {
      return super.getFloat(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Double getDouble(int index) {
    lock.readLock().lock();
    try {
      return super.getDouble(index);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Double getDouble(String name) {
    lock.readLock().lock();
    try {
      return super.getDouble(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public BigDecimal getBigDecimal(int index) {
    lock.readLock().lock();
    try {
      return super.getBigDecimal(index);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public BigDecimal getBigDecimal(String name) {
    lock.readLock().lock();
    try {
      return super.getBigDecimal(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public String getString(int index) {
    lock.readLock().lock();
    try {
      return super.getString(index);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public String getString(String name) {
    lock.readLock().lock();
    try {
      return super.getString(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public List<Object> getValues() {
    lock.readLock().lock();
    try {
      return super.getValues();
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public void add(Object value) {
    lock.writeLock().lock();
    try {
      super.add(value);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void add(int index, Object value) {
    lock.writeLock().lock();
    try {
      super.add(index, value);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void add(String name, Object value) {
    lock.writeLock().lock();
    try {
      super.add(name, value);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public boolean remove(Object value) {
    lock.writeLock().lock();
    try {
      return super.remove(value);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public Object removeName(String name) {
    lock.writeLock().lock();
    try {
      return super.removeName(name);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public Object removeIndex(int index) {
    lock.writeLock().lock();
    try {
      return super.removeIndex(index);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void forEach(TupleAction<String, Integer, Object> action) {
    lock.readLock().lock();
    try {
      super.forEach(action);
    } finally {
      lock.readLock().unlock();
    }
  }
}
