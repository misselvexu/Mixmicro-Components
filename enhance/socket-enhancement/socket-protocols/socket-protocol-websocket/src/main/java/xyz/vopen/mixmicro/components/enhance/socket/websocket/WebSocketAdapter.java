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

import xyz.vopen.mixmicro.components.enhance.socket.websocket.drafts.Draft;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.exceptions.InvalidDataException;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.framing.Framedata;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.framing.PingFrame;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.framing.PongFrame;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.handshake.ClientHandshake;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.handshake.HandshakeImpl1Server;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.handshake.ServerHandshake;
import xyz.vopen.mixmicro.components.enhance.socket.websocket.handshake.ServerHandshakeBuilder;

/**
 * This class default implements all methods of the WebSocketListener that can be overridden optionally when advances functionalities is needed.<br>
 **/
public abstract class WebSocketAdapter implements WebSocketListener {

	private PingFrame pingFrame;

	/**
	 * This default implementation does not do anything. Go ahead and overwrite it.
	 *
	 * @see WebSocketListener#onWebsocketHandshakeReceivedAsServer(WebSocket, Draft, ClientHandshake)
	 */
	@Override
	public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request ) throws InvalidDataException {
		return new HandshakeImpl1Server();
	}

	@Override
	public void onWebsocketHandshakeReceivedAsClient( WebSocket conn, ClientHandshake request, ServerHandshake response ) throws InvalidDataException {
		//To overwrite
	}

	/**
	 * This default implementation does not do anything which will cause the connections to always progress.
	 *
	 * @see WebSocketListener#onWebsocketHandshakeSentAsClient(WebSocket, ClientHandshake)
	 */
	@Override
	public void onWebsocketHandshakeSentAsClient( WebSocket conn, ClientHandshake request ) throws InvalidDataException {
		//To overwrite
	}

	/**
	 * This default implementation will send a pong in response to the received ping.
	 * The pong frame will have the same payload as the ping frame.
	 *
	 * @see WebSocketListener#onWebsocketPing(WebSocket, Framedata)
	 */
	@Override
	public void onWebsocketPing( WebSocket conn, Framedata f ) {
		conn.sendFrame( new PongFrame( (PingFrame)f ) );
	}

	/**
	 * This default implementation does not do anything. Go ahead and overwrite it.
	 *
	 * @see WebSocketListener#onWebsocketPong(WebSocket, Framedata)
	 */
	@Override
	public void onWebsocketPong( WebSocket conn, Framedata f ) {
		//To overwrite
	}

	/**
	 * Default implementation for onPreparePing, returns a (cached) PingFrame that has no application data.
	 * @see WebSocketListener#onPreparePing(WebSocket)
	 *
	 * @param conn The <tt>WebSocket</tt> connection from which the ping frame will be sent.
	 * @return PingFrame to be sent.
	 */
	@Override
	public PingFrame onPreparePing(WebSocket conn) {
		if(pingFrame == null)
			pingFrame = new PingFrame();
		return pingFrame;
	}
}
