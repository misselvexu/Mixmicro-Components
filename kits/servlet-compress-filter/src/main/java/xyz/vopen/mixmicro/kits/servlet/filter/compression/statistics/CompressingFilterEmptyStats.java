package xyz.vopen.mixmicro.kits.servlet.filter.compression.statistics;

public class CompressingFilterEmptyStats implements CompressingFilterStats {

  private static final String STATS_KEY =
      "xyz.vopen.mixmicro.kits.servlet.filter.compression.statistics.CompressingFilterEmptyStatsImpl";

  @Override
  public void incrementNumResponsesCompressed() {
    return;
  }

  @Override
  public void incrementTotalResponsesNotCompressed() {
    return;
  }

  @Override
  public void incrementNumRequestsCompressed() {
    return;
  }

  @Override
  public void incrementTotalRequestsNotCompressed() {
    return;
  }

  @Override
  public void notifyRequestBytesRead(long read) {
    return;
  }

  @Override
  public void notifyCompressedRequestBytesRead(long read) {
    return;
  }

  @Override
  public void notifyResponseBytesWritten(long written) {
    return;
  }

  @Override
  public void notifyCompressedResponseBytesWritten(long written) {
    return;
  }

  @Override
  public String getStatsKey() {
    return STATS_KEY;
  }
}
