/*
 * Copyright 2017 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.vopen.mixmicro.components.mongo.client.converters.experimental;

import xyz.vopen.mixmicro.components.mongo.client.converters.TypeConverter;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.experimental.MorphiaReference;

/**
 * A converter for MorphiaReference values
 *
 * @see MorphiaReference
 *
 */
public class ReferenceConverter extends TypeConverter {
  private Mapper mapper;

  /**
   * Creates the Converter.
   *
   * @param mapper the mapper
   */
  public ReferenceConverter(final Mapper mapper) {
    super(MorphiaReference.class);
    this.mapper = mapper;
  }

  @Override
  public Object encode(final Object value, final MappedField optionalExtraInfo) {
    return value == null
        ? null
        : ((MorphiaReference) value).encode(mapper, value, optionalExtraInfo);
  }

  @Override
  public Object decode(
      final Class<?> targetClass, final Object idValue, final MappedField optionalExtraInfo) {
    throw new UnsupportedOperationException("should never get here");
  }
}
