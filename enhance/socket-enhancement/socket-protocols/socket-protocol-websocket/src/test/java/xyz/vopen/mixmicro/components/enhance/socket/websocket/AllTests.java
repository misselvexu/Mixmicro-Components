/*
 * Copyright (c) 2009-2020 VOPEN.XYZ
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

package xyz.vopen.mixmicro.components.enhance.socket.websocket;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.client.AllClientTests;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.drafts.AllDraftTests;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.exceptions.AllExceptionsTests;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.framing.AllFramingTests;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.issues.AllIssueTests;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.misc.AllMiscTests;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.protocols.AllProtocolTests;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.util.ByteBufferUtilsTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        ByteBufferUtilsTest.class,
        AllClientTests.class,
        AllDraftTests.class,
        AllIssueTests.class,
        AllExceptionsTests.class,
		AllMiscTests.class,
		AllProtocolTests.class,
        AllFramingTests.class
})
/**
 * Start all tests
 */
public class AllTests {
}
