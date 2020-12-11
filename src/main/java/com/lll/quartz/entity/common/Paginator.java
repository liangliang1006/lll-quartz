package com.lll.quartz.entity.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author liang
 * @Date 2019/11/13 11:22
 * @description 分页对象
 */
@Data
public class Paginator implements Serializable {
    /**
     * 当前页
     */
    private int pageNum;

    /**
     * 每页的条数
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 总条数
     */
    private long total;

    public void setTotal(long total) {
        this.total = total;
        this.pages = total % pageSize == 0 ? (int) total / pageSize : (int) total / pageSize + 1;
    }
}
