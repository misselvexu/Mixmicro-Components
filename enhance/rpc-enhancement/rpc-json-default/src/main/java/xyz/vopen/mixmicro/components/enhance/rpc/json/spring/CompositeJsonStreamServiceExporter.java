package xyz.vopen.mixmicro.components.enhance.rpc.json.spring;

import xyz.vopen.mixmicro.components.enhance.rpc.json.server.StreamServer;
import org.springframework.beans.factory.DisposableBean;
import xyz.vopen.mixmicro.components.enhance.rpc.json.utils.Util;

import javax.net.ServerSocketFactory;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * A stream service exporter that exports multiple
 * services as a single service.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@SuppressWarnings("unused")
public class CompositeJsonStreamServiceExporter extends AbstractCompositeJsonServiceExporter implements DisposableBean {

	private static final int DEFAULT_MAX_THREADS = 50;
	private static final int DEFAULT_PORT = 10420;
	private static final int DEFAULT_BACKLOG = 0;
	private static final int DEFAULT_MAX_CLIENT_ERRORS = 5;

	private ServerSocketFactory serverSocketFactory;
	private int maxThreads = DEFAULT_MAX_THREADS;
	private int port = DEFAULT_PORT;
	private int backlog = DEFAULT_BACKLOG;
	private int maxClientErrors = DEFAULT_MAX_CLIENT_ERRORS;
	private String hostName = Util.DEFAULT_HOSTNAME;

	private StreamServer streamServer;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void exportService() throws Exception {
		if (streamServer == null) {
			if (serverSocketFactory == null) {
				serverSocketFactory = ServerSocketFactory.getDefault();
			}
			ServerSocket serverSocket = serverSocketFactory.createServerSocket(port, backlog, InetAddress.getByName(hostName));
			streamServer = new StreamServer(getJsonRpcServer(), maxThreads, serverSocket);
			streamServer.setMaxClientErrors(maxClientErrors);
		}
		streamServer.start();
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroy() throws Exception {
		streamServer.stop();
	}

	/**
	 * @param serverSocketFactory the serverSocketFactory to set
	 */
	public void setServerSocketFactory(ServerSocketFactory serverSocketFactory) {
		this.serverSocketFactory = serverSocketFactory;
	}

	/**
	 * @param maxThreads the maxThreads to set
	 */
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @param backlog the backlog to set
	 */
	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @param streamServer the streamServer to set
	 */
	public void setStreamServer(StreamServer streamServer) {
		this.streamServer = streamServer;
	}

	/**
	 * @param maxClientErrors the maxClientErrors to set
	 */
	public void setMaxClientErrors(int maxClientErrors) {
		this.maxClientErrors = maxClientErrors;
	}

}
