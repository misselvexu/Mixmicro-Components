package xyz.vopen.mixmicro.components.enhance.socket.websocket.server;

import xyz.vopen.mixmicro.components.enhance.socket.websocket.WebSocket;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.WebSocketAdapter;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.WebSocketImpl;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.drafts.Draft;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.drafts.Draft_6455;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.handshake.Handshakedata;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class DefaultSSLWebSocketServerFactoryTest {

    @Test
    public void testConstructor() throws NoSuchAlgorithmException {
        try {
            new DefaultSSLWebSocketServerFactory(null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Good
        }
        try {
            new DefaultSSLWebSocketServerFactory(null, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Good
        }
        try {
            new DefaultSSLWebSocketServerFactory(SSLContext.getDefault(), null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }
        try {
            new DefaultSSLWebSocketServerFactory(SSLContext.getDefault());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        }
        try {
            new DefaultSSLWebSocketServerFactory(SSLContext.getDefault(), Executors.newCachedThreadPool());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        }
    }
    @Test
    public void testCreateWebSocket() throws NoSuchAlgorithmException {
        DefaultSSLWebSocketServerFactory webSocketServerFactory = new DefaultSSLWebSocketServerFactory(SSLContext.getDefault());
        CustomWebSocketAdapter webSocketAdapter = new CustomWebSocketAdapter();
        WebSocketImpl webSocketImpl = webSocketServerFactory.createWebSocket(webSocketAdapter, new Draft_6455());
        assertNotNull("webSocketImpl != null", webSocketImpl);
        webSocketImpl = webSocketServerFactory.createWebSocket(webSocketAdapter, Collections.<Draft>singletonList(new Draft_6455()));
        assertNotNull("webSocketImpl != null", webSocketImpl);
    }

    @Test
    public void testWrapChannel() throws IOException, NoSuchAlgorithmException {
        DefaultSSLWebSocketServerFactory webSocketServerFactory = new DefaultSSLWebSocketServerFactory(SSLContext.getDefault());
        SocketChannel channel =  SocketChannel.open();
        try {
            ByteChannel result = webSocketServerFactory.wrapChannel(channel, null);
        } catch (NotYetConnectedException e) {
            //We do not really connect
        }
        channel.close();
    }

    @Test
    public void testClose() {
        DefaultWebSocketServerFactory webSocketServerFactory = new DefaultWebSocketServerFactory();
        webSocketServerFactory.close();
    }

    private static class CustomWebSocketAdapter extends WebSocketAdapter {
        @Override
        public void onWebsocketMessage(WebSocket conn, String message) {

        }

        @Override
        public void onWebsocketMessage(WebSocket conn, ByteBuffer blob) {

        }

        @Override
        public void onWebsocketOpen(WebSocket conn, Handshakedata d) {

        }

        @Override
        public void onWebsocketClose(WebSocket ws, int code, String reason, boolean remote) {

        }

        @Override
        public void onWebsocketClosing(WebSocket ws, int code, String reason, boolean remote) {

        }

        @Override
        public void onWebsocketCloseInitiated(WebSocket ws, int code, String reason) {

        }

        @Override
        public void onWebsocketError(WebSocket conn, Exception ex) {

        }

        @Override
        public void onWriteDemand(WebSocket conn) {

        }

        @Override
        public InetSocketAddress getLocalSocketAddress(WebSocket conn) {
            return null;
        }

        @Override
        public InetSocketAddress getRemoteSocketAddress(WebSocket conn) {
            return null;
        }
    }
}
