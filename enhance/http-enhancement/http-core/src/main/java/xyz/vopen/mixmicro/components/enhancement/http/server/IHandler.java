package xyz.vopen.mixmicro.components.enhancement.http.server;

public interface IHandler {

  void handle(HttpRequest request, RespCallback callback);

  void handle(AsyncChannel channel, Frame frame);

  void clientClose(AsyncChannel channel, int status);

  // close any resource with this handler
  void close(int timeoutMs);
}
