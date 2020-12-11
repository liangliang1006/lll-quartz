package com.lll.quartz.entity.enums;

/**
 * @Author: liang
 * @Date: 2020/12/9 13:48
 * @Description
 */
public enum JobStatusEnum {
    /**
     * 正常
     */
    normal("normal", "正常"),
    /**
     * 停止
     */
    stop("stop", "停止");

    private String status;

    private String show;

    JobStatusEnum(String status, String show) {
        this.status = status;
        this.show = show;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}
