package xyz.vopen.framework.logging.admin.mongodb.response;

import lombok.Data;

import java.util.List;

/**
 * {@link PageResponse} mongodb page query response
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
@Data
public class PageResponse<T> {
  private Integer pageNum;
  private Integer pageSize;
  private List<T> list;
  private long total;
  private int pages;

  /**
   * domain page over to PageResponse
   *
   * @param page spring data page
   */
  public PageResponse(org.springframework.data.domain.Page<T> page) {
    // Mongo page number start from 0
    this.pageNum = page.getNumber() + 1;
    this.pageSize = page.getSize();
    this.list = page.getContent();
    this.total = page.getTotalElements();
    this.pages = page.getTotalPages();
  }

  /**
   * query result page over to PageResponse
   *
   * @param list data list
   * @param pageNum page number
   * @param pageSize one page size
   * @param total data total amount
   * @param pages total pages
   */
  public PageResponse(List<T> list, int pageNum, int pageSize, long total, int pages) {
    this.pageNum = pageNum;
    this.pageSize = pageSize;
    this.list = list;
    this.total = total;
    this.pages = pages;
  }
}
