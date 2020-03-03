/*
 * Licensed to the VOPEN+ Group under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.vopen.mixmicro.components.logger.profile.enumeration;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public abstract class IntegerEnum extends Enum {
  private static final long serialVersionUID = 343392921439669443L;

  public IntegerEnum() {}

  protected static final Enum create(int value) {
    return (Enum) createEnum(new Integer(value));
  }

  protected static final Enum create(Number value) {
    return (Enum) createEnum(new Integer(value.intValue()));
  }

  protected static final Enum create(String name, int value) {
    return (Enum) createEnum(name, new Integer(value));
  }

  protected static final Enum create(String name, Number value) {
    return (Enum) createEnum(name, new Integer(value.intValue()));
  }

  protected static Object createEnumType() {
    return new EnumType() {
      @Override
      protected Class getUnderlyingClass() {
        return Integer.class;
      }

      @Override
      protected Number getNextValue(Number value, boolean flagMode) {
        if (value == null) {
          return flagMode ? new Integer(1) : new Integer(0);
        } else {
          int intValue = ((Integer) value).intValue();
          return flagMode ? new Integer(intValue << 1) : new Integer(intValue + 1);
        }
      }

      @Override
      protected boolean isZero(Number value) {
        return ((Integer) value).intValue() == 0;
      }
    };
  }

  @Override
  public int intValue() {
    return ((Integer) this.getValue()).intValue();
  }

  @Override
  public long longValue() {
    return ((Integer) this.getValue()).longValue();
  }

  @Override
  public double doubleValue() {
    return ((Integer) this.getValue()).doubleValue();
  }

  @Override
  public float floatValue() {
    return ((Integer) this.getValue()).floatValue();
  }

  @Override
  public String toHexString() {
    return Integer.toHexString(((Integer) this.getValue()).intValue());
  }

  @Override
  public String toOctalString() {
    return Integer.toOctalString(((Integer) this.getValue()).intValue());
  }

  @Override
  public String toBinaryString() {
    return Integer.toBinaryString(((Integer) this.getValue()).intValue());
  }
}
