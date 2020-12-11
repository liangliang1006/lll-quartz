package com.lll.quartz.quartz.job;

import com.lll.quartz.quartz.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: liang
 * @Date: 2020/12/11 14:14
 * @Description
 */
@Slf4j
public class TestOneJob implements BaseJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("嘤嘤嘤，人家是第一个定时任务！，现在的时间是：{}", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
    }
}
