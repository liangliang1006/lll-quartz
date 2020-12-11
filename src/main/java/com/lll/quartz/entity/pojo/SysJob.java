package com.lll.quartz.entity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SysJob {
    private Integer id;

    private String jobGroup;

    private String jobName;

    private String cronExpression;

    private String beanClass;

    private String jobStatus;

    private String description;

    private Date createTime;

    private Date modifyTime;

}