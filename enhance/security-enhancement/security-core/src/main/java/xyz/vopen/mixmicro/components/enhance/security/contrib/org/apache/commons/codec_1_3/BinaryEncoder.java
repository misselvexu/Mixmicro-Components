/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * IMPORTANT NOTE: This class has been included into Mixsecurity's source tree from
 * Apache Commons-Codec version 1.3 [see http://commons.apache.org/codec],
 * licensed under Apache License 2.0 [see http://www.apache.org/licenses/LICENSE-2.0].
 * No modifications have been made to the code of this class except the package name.
 */

package xyz.vopen.mixmicro.components.enhance.security.contrib.org.apache.commons.codec_1_3;

/**
 * Defines common encoding methods for byte array encoders.
 *
 * @author Apache Software Foundation
 */
public interface BinaryEncoder extends Encoder {

  /**
   * Encodes a byte array and return the encoded data as a byte array.
   *
   * @param pArray Data to be encoded
   * @return A byte array containing the encoded data
   * @throws EncoderException thrown if the Encoder encounters a failure condition during the
   *     encoding process.
   */
  byte[] encode(byte[] pArray) throws EncoderException;
}
