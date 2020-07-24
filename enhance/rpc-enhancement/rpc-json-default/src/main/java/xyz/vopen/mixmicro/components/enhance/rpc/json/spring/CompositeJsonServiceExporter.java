package xyz.vopen.mixmicro.components.enhance.rpc.json.spring;

import xyz.vopen.mixmicro.components.enhance.rpc.json.server.JsonRpcServer;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A Composite service exporter for spring that exposes
 * multiple services via Mixmicro RPC over HTTP.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@SuppressWarnings("unused")
public class CompositeJsonServiceExporter extends AbstractCompositeJsonServiceExporter implements HttpRequestHandler {

	private JsonRpcServer jsonRpcServer;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void exportService() {
		jsonRpcServer = getJsonRpcServer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
  public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		jsonRpcServer.handle(request, response);
		response.getOutputStream().flush();
	}

}
