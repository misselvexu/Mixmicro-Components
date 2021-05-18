package xyz.vopen.framework.logging.admin.mongodb.helper;

import org.springframework.data.domain.PageRequest;

/**
 * {@link MongoPageHelper} mongodb page query helper
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
public class MongoPageHelper {

  /** default start page */
  private static final int DEFAULT_PAGE_NUM = 1;

  /** max page size */
  private static final int MAX_PAGE_SIZE = 2000;

  /** default page size */
  private static final int DEFAULT_PAGE_SIZE = 20;
  /**
   * start page by page param
   *
   * @param pageNum page number
   * @param pageSize single page size
   * @return spring data PageRequest
   */
  public static PageRequest startPage(int pageNum, int pageSize) {
    int num = getReasonablePageNum(pageNum) - 1;
    int size = getReasonablePageSize(pageSize);
    return PageRequest.of(num, size);
  }

  /**
   * get total pages
   *
   * @param totalAmount result amount
   * @param pageSize single page size
   * @return total pages
   */
  public int getPages(int totalAmount, int pageSize) {
    return totalAmount / pageSize + (totalAmount % pageSize > 0 ? 1 : 0);
  }

  /**
   * get limited page number
   *
   * @param pageNum page number
   * @return page number
   */
  private static int getReasonablePageNum(Integer pageNum) {
    if (pageNum == null || pageNum <= 0) {
      pageNum = DEFAULT_PAGE_NUM;
    }
    return pageNum;
  }

  /**
   * get limited page size
   *
   * @param pageSize one page size
   * @return page size
   */
  private static int getReasonablePageSize(Integer pageSize) {
    if (pageSize == null || pageSize <= 0) {
      pageSize = DEFAULT_PAGE_SIZE;
    } else if (pageSize > MAX_PAGE_SIZE) {
      pageSize = MAX_PAGE_SIZE;
    }
    return pageSize;
  }
}
