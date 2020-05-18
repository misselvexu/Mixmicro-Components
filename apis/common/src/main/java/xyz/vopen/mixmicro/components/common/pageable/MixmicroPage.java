package xyz.vopen.mixmicro.components.common.pageable;

import com.google.common.collect.Lists;
import lombok.*;
import xyz.vopen.mixmicro.components.common.SerializableResponse;

import java.util.List;

/**
 * {@link MixmicroPage}
 *
 * @author <a href="mailto:iskp.me@gmail.com">ZhouShu.HUI</a>
 * @version ${project.version} - 2020-05-18.
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MixmicroPage<T> implements SerializableResponse {

  /** 分页查询到的数据列表 参与分页后的分页数据 */
  @Builder.Default private List<T> records = Lists.newArrayList();

  /** 获取总页数 */
  @Builder.Default private int size = 0;

  /** 获取总条数 */
  @Builder.Default private int total = 0;

  /** 当前页码 */
  @Builder.Default private int current = 1;

  // CONVERTERS FOR BUILD MIXMICRO PAGE INSTANCE

  /**
   * Constructor For Build {@link MixmicroPage} instance
   *
   * <p><font style="color:red;">WARNING: Do not use this constructor if the value exceeds the
   * maximum value of Int</font>
   *
   * @param records query result list
   * @param total total record size
   */
  public MixmicroPage(List<T> records, long total) {
    this(records, 0, total);
  }

  /**
   * Constructor For Build {@link MixmicroPage} instance
   *
   * <p><font style="color:red;">WARNING: Do not use this constructor if the value exceeds the
   * maximum value of Int</font>
   *
   * @param records query result list
   * @param size total page size
   * @param total total record size
   */
  public MixmicroPage(List<T> records, long size, long total) {
    this(records, size, total, 0);
  }

  /**
   * Constructor For Build {@link MixmicroPage} instance
   *
   * <p><font style="color:red;">WARNING: Do not use this constructor if the value exceeds the
   * maximum value of Int</font>
   *
   * @param records query result list
   * @param size total page size
   * @param total total record size
   * @param current current page
   */
  public MixmicroPage(List<T> records, long size, long total, long current) {
    this.records = records;
    // RISK: losing accuracy
    this.size = (int) size;
    this.total = (int) total;
    this.current = (int) current;
  }
}
