package com.yyy.common.utils.pagination;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Mr.Liu
 * @Description
 * @Date Create in 2019-03-01
 * @Modified By:
 */
@Slf4j
public class PageFactory<T> {

    private static final String CURRENT = "current";

    private static final String CURRENT_DEFAULE = "1";

    private static final String SIZE = "size";

    private static final String SIZE_DEFAULE = "15";

    private static final String SORT = "sort";

    private static final String ASC_FLAG = "ascFlag";

    private static final String TRUE = "true";

    public Page<T> defaultPage() {
        HttpServletRequest request = HttpKit.getRequest();
        // 当前第几页（current）
        int current = Integer.valueOf(ServletRequestUtils.getStringParameter(request, CURRENT, CURRENT_DEFAULE));
        log.info("当前页：{}",current);
        // 每页多少条数据（size）
        int size = Integer.valueOf(ServletRequestUtils.getStringParameter(request, SIZE, SIZE_DEFAULE));
        log.info("每页显示多少：{}",size);
        //排序字段名称
        String sort = request.getParameter(SORT);
        String sortColumn = StringUtils.isBlank(sort) ? null : NamingStrategyUtils.toUnderline(sort);
        //asc或desc(升序或降序)
        String ascFlag = request.getParameter(ASC_FLAG);
        Page<T> page = new Page<>(current, size);
        if (sortColumn != null) {
            if (sortColumn.length() == 0||"".equals(sortColumn)) {
                return page;
            }
            if (TRUE.equals(ascFlag)) {
                page.setAsc(sortColumn);
            } else {
                page.setDesc(sortColumn);
            }
        }
        return page;
    }


}

