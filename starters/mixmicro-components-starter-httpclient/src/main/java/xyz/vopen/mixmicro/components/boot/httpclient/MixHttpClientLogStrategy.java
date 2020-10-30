package xyz.vopen.mixmicro.components.boot.httpclient;

public enum MixHttpClientLogStrategy {

  /** No logs. */
  NONE,

  /** Logs request and response lines. */
  BASIC,

  /** Logs request and response lines and their respective headers. */
  HEADERS,

  /** Logs request and response lines and their respective headers and bodies (if present). */
  BODY
}
