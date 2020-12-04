package xyz.vopen.mixmicro.kits.task.tuples;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * {@link TupleApi}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
public interface TupleApi extends Serializable {

  /**
   * Returns the number of elements in a tuple.
   *
   * @return Number of elements in the tuple.
   * @see List#size()
   */
  int size();

  /**
   * Returns true if the specified name is included in the tuple, false otherwise.
   *
   * @param name The name of the element.
   * @return Returns true if the tuple contains the specified name.
   */
  boolean contains(String name);

  /**
   * Returns true if the specified element is included in the tuple, false otherwise.
   *
   * @param value The element to look up.
   * @return Returns true if the specified element is included in the tuple.
   */
  boolean containsValue(Object value);

  /**
   * Get the element of the specified position in the tuple.
   *
   * @param index The index of the element to be returned.
   * @return The element in the tuple at the specified position.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;=
   *     size()</tt>)
   */
  Object getValue(int index);

  /**
   * Get the element of the tuple with the specified name, and return null if the element with the
   * name is not found.
   *
   * @param name The name of the element to be returned.
   * @return The element of the tuple with the specified name, returns null if not found.
   */
  Object getValue(String name);

  /**
   * Gets the element at the specified location in the tuple and converts it to a byte array if
   * possible.
   *
   * @param index The index of the element to be returned.
   * @return The element in the tuple at the specified position.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;=
   *     size()</tt>)
   * @throws TupleException If an element cannot be converted to a byte array
   */
  byte[] getBytes(int index);

  /**
   * Gets the element with the specified name in the tuple and converts it to a byte array if
   * possible. If no element of that name is found, null is returned.
   *
   * @param name The name of the element to be returned.
   * @return The element of the tuple with the specified name, returns null if not found.
   * @throws TupleException If an element cannot be converted to a byte array
   */
  byte[] getBytes(String name);

  /**
   * Gets the element at the specified location in the tuple and converts it to Character if
   * possible.
   *
   * @param index The index of the element to be returned.
   * @return The element in the tuple at the specified position.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;=
   *     size()</tt>)
   * @throws TupleException If an element cannot be converted to Character
   */
  Character getChar(int index);

  /**
   * Returns null if no element with that name is found.
   *
   * @param name The name of the element to be returned.
   * @return The element of the tuple with the specified name, returns null if not found.
   * @throws TupleException If an element cannot be converted to Character
   */
  Character getChar(String name);

  /**
   * Get the element at the specified position in the tuple and convert it to Boolean if possible.
   *
   * @param index The index of the element to be returned.
   * @return The element in the tuple at the specified position.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;=
   *     size()</tt>)
   * @throws TupleException If the element cannot be converted to Boolean
   */
  Boolean getBoolean(int index);

  /**
   * Returns null if no element with that name is found.
   *
   * @param name The name of the element to be returned.
   * @return The element of the tuple with the specified name, returns null if not found.
   * @throws TupleException If the element cannot be converted to Boolean
   */
  Boolean getBoolean(String name);

  /**
   * Gets the element at the specified location in the tuple and converts it to Byte if possible.
   *
   * @param index The index of the element to be returned.
   * @return The element in the tuple at the specified position.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;=
   *     size()</tt>)
   * @throws TupleException If the element cannot be converted to Byte
   */
  Byte getByte(int index);

  /**
   * If no element of that name is found, null is returned.
   *
   * @param name The name of the element to be returned.
   * @return The element of the tuple with the specified name, returns null if not found.
   * @throws TupleException If the element cannot be converted to Byte
   */
  Byte getByte(String name);

  /**
   * Gets the element at the specified location in the tuple and converts it to Short if possible.
   *
   * @param index The index of the element to be returned.
   * @return The element in the tuple at the specified position.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;=
   *     size()</tt>)
   * @throws TupleException If an element cannot be converted to Short
   */
  Short getShort(int index);

  /**
   * If no element of that name is found, null is returned.
   *
   * @param name The name of the element to be returned.
   * @return The element of the tuple with the specified name, returns null if not found.
   * @throws TupleException If an element cannot be converted to Short
   */
  Short getShort(String name);

  /**
   * Gets the element at the specified location in the tuple and converts it to Integer if possible.
   *
   * @param index The index of the element to be returned.
   * @return The element in the tuple at the specified position.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;=
   *     size()</tt>)
   * @throws TupleException If the element cannot be converted to Integer
   */
  Integer getInt(int index);

  /**
   * If no element of that name is found, null is returned.
   *
   * @param name The name of the element to be returned.
   * @return The element of the tuple with the specified name, returns null if not found.
   * @throws TupleException If the element cannot be converted to Integer
   */
  Integer getInt(String name);

  /**
   * Gets the element at the specified location in the tuple and converts it to Long if possible.
   *
   * @param index The index of the element to be returned.
   * @return The element in the tuple at the specified position.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;=
   *     size()</tt>)
   * @throws TupleException If an element cannot be converted to Long
   */
  Long getLong(int index);

  /**
   * If no element of that name is found, null is returned.
   *
   * @param name The name of the element to be returned.
   * @return The element of the tuple with the specified name, returns null if not found.
   * @throws TupleException If an element cannot be converted to Long
   */
  Long getLong(String name);

  /**
   * Gets the element at the specified position in the tuple and converts it to Float if possible.
   *
   * @param index The index of the element to be returned.
   * @return The element in the tuple at the specified position.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;=
   *     size()</tt>)
   * @throws TupleException If an element cannot be converted to Float
   */
  Float getFloat(int index);

  /**
   * If no element of that name is found, null is returned.
   *
   * @param name The name of the element to be returned.
   * @return The element of the tuple with the specified name, returns null if not found.
   * @throws TupleException If an element cannot be converted to Float
   */
  Float getFloat(String name);

  /**
   * Gets the element at the specified position in the tuple and converts it to Double if possible.
   *
   * @param index The index of the element to be returned.
   * @return The element in the tuple at the specified position.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;=
   *     size()</tt>)
   * @throws TupleException If the element cannot be converted to Double
   */
  Double getDouble(int index);

  /**
   * If no element of that name is found, null is returned.
   *
   * @param name The name of the element to be returned.
   * @return The element of the tuple with the specified name, returns null if not found.
   * @throws TupleException If the element cannot be converted to Double
   */
  Double getDouble(String name);

  /**
   * Gets the element at the specified location in the tuple and converts it as far as possible to
   * BigDecimal.
   *
   * @param index The index of the element to be returned.
   * @return The element in the tuple at the specified position.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;=
   *     size()</tt>)
   * @throws TupleException If the element cannot be converted to BigDecimal
   */
  BigDecimal getBigDecimal(int index);

  /**
   * Returns null if no element with that name is found.
   *
   * @param name The name of the element to be returned.
   * @return The element of the tuple with the specified name, returns null if not found.
   * @throws TupleException If the element cannot be converted to BigDecimal
   */
  BigDecimal getBigDecimal(String name);

  /**
   * Gets the element at the specified location in the tuple, converts it to String, and calls the
   * toString method of Object.
   *
   * @param index The index of the element to be returned.
   * @return The element in the tuple at the specified position.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;=
   *     size()</tt>)
   */
  String getString(int index);

  /**
   * Gets the element at the specified location in the tuple, converts it to String, and calls
   * Object's toString method. If no element of that name is found, null is returned.
   *
   * @param name The name of the element to be returned.
   * @return The element of the tuple with the specified name, returns null if not found.
   */
  String getString(String name);

  /**
   * Get all the values in the tuple.
   *
   * <p>All values in the @return tuple.
   */
  List<Object> getValues();

  /**
   * Add the specified element to the end of the tuple.
   *
   * @param value New element.
   */
  void add(Object value);

  /**
   * Specifies the location in the tuple where the specified element is to be inserted.
   *
   * @param index Desired element inserted by the element.
   * @param value New element.
   */
  void add(int index, Object value);

  /**
   * To add an element to a tuple with a specified name, the element name cannot be null.
   *
   * @param name The name of the element.
   * @param value element
   */
  void add(String name, Object value);

  /**
   * Remove the first occurrence of the specified element from the tuple.
   *
   * @param value The element to remove.
   * @return Returns true if the specified element is included in the tuple.
   */
  boolean remove(Object value);

  /**
   * Remove the element with the specified name from the ancestor, the name cannot be null.
   *
   * @param name The name of the element to be removed.
   * @return Returns the element with the specified name if the tuple contains the specified name,
   *     otherwise returns null.
   */
  Object removeName(String name);

  /**
   * Remove elements from a tuple with a specified index position.
   *
   * @param index You want to remove the index value of the element.
   * @return Element of the tuple at that index position.
   */
  Object removeIndex(int index);

  /**
   * loops sequentially through the operations performed for each element in the tuple.
   *
   * @param action Action to perform for each element
   */
  void forEach(TupleAction<String, Integer, Object> action);
}
