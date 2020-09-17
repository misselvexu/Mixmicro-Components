package xyz.vopen.framework.logging.admin.mongodb.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * mongodb page query help class
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/9
 */
public class MongoPageHelper {

    private static final String ORDER_BY_SEPARATOR = ",";
    private static final String DIRECTION_SEPARATOR = " ";

    /**
     * default start page
     */
    private static final int DEFAULT_PAGE_NUM = 1;

    /**
     * max page size
     */
    private static final int MAX_PAGE_SIZE = 2000;

    /**
     * default page size
     */
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * start page by page param
     *
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    public static PageRequest startPage(int pageNum, int pageSize, Map<String, String> orderBy) {
        int num = getReasonablePageNum(pageNum) - 1;
        int size = getReasonablePageSize(pageSize);
        if (!CollectionUtils.isEmpty(orderBy)) {
            List<Sort.Order> orderList = new ArrayList<>();
            orderBy.forEach((key, value) -> {
                Sort.Order order;
                if (Sort.Direction.DESC.toString().equalsIgnoreCase(value)) {
                    order = Sort.Order.desc(key);
                } else {
                    order = Sort.Order.asc(key);
                }
                orderList.add(order);
            });
            Sort sort = Sort.by(orderList);
            return PageRequest.of(num, size, sort);
        }
        return PageRequest.of(num, size);
    }

    /**
     * start page by page param
     *
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    public static PageRequest startPage(int pageNum, int pageSize, String orderBy) {
        int num = getReasonablePageNum(pageNum) - 1;
        int size = getReasonablePageSize(pageSize);
        // no orderBy
        if (StringUtils.isBlank(orderBy)) {
            return PageRequest.of(num, size);
        }
        Map<String, String> orderMap = new LinkedHashMap<>();
        String[] orderByArr = orderBy.split(ORDER_BY_SEPARATOR);
        for (String ob : orderByArr) {
            if (StringUtils.isBlank(ob)) {
                continue;
            }
            String key = ob;
            String value = null;
            int i = ob.lastIndexOf(DIRECTION_SEPARATOR);
            if (i >= 0) {
                key = ob.substring(0, i);
                value = ob.substring(i + 1);
            }
            if (StringUtils.isNotBlank(key)) {
                orderMap.put(key, value);
            }
        }
        List<Sort.Order> orderList = new ArrayList<>();
        orderMap.forEach((key, value) -> {
            Sort.Order order;
            if (Sort.Direction.DESC.toString().equalsIgnoreCase(value)) {
                order = Sort.Order.desc(key);
            } else {
                order = Sort.Order.asc(key);
            }
            orderList.add(order);
        });
        Sort sort = Sort.by(orderList);
        return PageRequest.of(num, size, sort);
    }

    /**
     * get total pages
     *
     * @param totalAmount
     * @param pageSize
     * @return
     */
    public int getPages(int totalAmount, int pageSize) {
        return (int) (totalAmount / pageSize + (totalAmount % pageSize > 0 ? 1 : 0));
    }

    /**
     * get usable page
     *
     * @param pageNum
     * @return
     */
    private static int getReasonablePageNum(Integer pageNum) {
        if (pageNum == null || pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        return pageNum;
    }

    /**
     * page size handler
     *
     * @param pageSize
     * @return
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
