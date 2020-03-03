package xyz.vopen.mixmicro.kits.servlet.filter.compression;

/** Created by Elve.Xu on 3/11/14. */
import java.io.Serializable;

/**
 * A simple enum used by {@link OutputStatsCallback} to select a field in this class. This is
 * getting a little messy but somehow better than defining a bunch of inner classes?
 *
 * @since 1.6
 */
enum StatsField implements Serializable {
  RESPONSE_INPUT_BYTES,
  RESPONSE_COMPRESSED_BYTES,
  REQUEST_INPUT_BYTES,
  REQUEST_COMPRESSED_BYTES
}
