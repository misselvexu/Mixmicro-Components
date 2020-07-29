package xyz.vopen.mixmicro.components.enhancement.http.server;

public class WsAtta extends ServerAtta {

  final public WSDecoder decoder;

  public WsAtta(AsyncChannel channel, int maxSize) {
    this.decoder = new WSDecoder(maxSize);
    this.channel = channel;
  }
}
