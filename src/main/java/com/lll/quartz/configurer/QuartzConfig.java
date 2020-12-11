package com.lll.quartz.configurer;

import com.lll.quartz.quartz.JobFactory;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @Author: liang
 * @Date: 2020/12/9 17:39
 * @Description
 */
@Configuration
public class QuartzConfig {

    @Autowired
    private JobFactory jobFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        try {
            //设置覆盖已存在的任务
            schedulerFactoryBean.setOverwriteExistingJobs(true);
            //延迟1秒执行
            schedulerFactoryBean.setStartupDelay(1);
            //将spring管理job自定义工厂交由调度器维护
            schedulerFactoryBean.setJobFactory(jobFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedulerFactoryBean;
    }


    @Bean(name = "scheduler")
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }


}
