package xyz.vopen.framework.logging.admin.mongodb.response;

import lombok.Data;

import java.util.List;

/**
 * mongodb page query response
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/8
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
     * @param page
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
     * @param list
     * @param pageNum
     * @param pageSize
     * @param total
     * @param pages
     */
    public PageResponse(List<T> list, int pageNum, int pageSize, long total, int pages) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.list = list;
        this.total = total;
        this.pages = pages;
    }
}
