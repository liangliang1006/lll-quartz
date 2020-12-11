package com.lll.quartz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lll.quartz.entity.common.Paginator;
import com.lll.quartz.entity.enums.JobStatusEnum;
import com.lll.quartz.entity.pojo.SysJob;
import com.lll.quartz.mapper.SysJobMapper;
import com.lll.quartz.quartz.BaseJob;
import com.lll.quartz.service.ISysJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author: liang
 * @Date: 2020/12/9 12:17
 * @Description
 */
@Slf4j
@Service
@Transactional
public class SysJobServiceImpl implements ISysJobService {

    @Autowired
    private Scheduler scheduler;

    @Resource
    private SysJobMapper sysJobMapper;

    @PostConstruct
    public void startScheduler() {
        try {
            // 启动调度器
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        List<SysJob> sysJobs = sysJobMapper.selectList();
        try {
            for(SysJob sysJob : sysJobs){
                // 构建Job信息
                Class<?> clazz = Class.forName(sysJob.getBeanClass());
                BaseJob baseJob = (BaseJob) clazz.newInstance();
                JobDetail jobDetail = JobBuilder.newJob(baseJob.getClass()).withIdentity(sysJob.getBeanClass(), sysJob.getJobGroup()).build();

                // Cron表达式调度构建器(即任务执行的时间)
                /**
                 * withMisfireHandlingInstructionDoNothing
                 * ——不触发立即执行
                 * ——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
                 */
                CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(sysJob.getCronExpression()).withMisfireHandlingInstructionDoNothing();

                //根据Cron表达式构建一个Trigger
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(sysJob.getBeanClass(), sysJob.getJobGroup()).withSchedule(cron).build();

                scheduler.scheduleJob(jobDetail, trigger);
                if(JobStatusEnum.stop.getStatus().equals(sysJob.getJobStatus())){
                    scheduler.pauseJob(JobKey.jobKey(sysJob.getBeanClass(), sysJob.getJobGroup()));
                }

            }
        } catch (Exception e) {
            log.error("【定时任务】创建失败！", e);
            throw new RuntimeException("【定时任务】创建失败！");
        }
    }

    @Override
    public List<SysJob> list(Paginator paginator) {
        PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
        List<SysJob> sysJobs = sysJobMapper.selectList();
        paginator.setTotal(new PageInfo(sysJobs).getTotal());
        return sysJobs;
    }

    @Override
    public void add(SysJob sysJob) {
        // 持久化数据
        sysJob.setJobStatus(JobStatusEnum.normal.getStatus());
        sysJob.setCreateTime(new Date());
        sysJobMapper.insert(sysJob);

        try {
            // 构建Job信息
            Class<?> clazz = Class.forName(sysJob.getBeanClass());
            BaseJob baseJob = (BaseJob) clazz.newInstance();
            JobDetail jobDetail = JobBuilder.newJob(baseJob.getClass()).withIdentity(sysJob.getBeanClass(), sysJob.getJobGroup()).build();

            // Cron表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(sysJob.getCronExpression()).withMisfireHandlingInstructionDoNothing();

            //根据Cron表达式构建一个Trigger
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(sysJob.getBeanClass(), sysJob.getJobGroup()).withSchedule(cron).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            log.error("【定时任务】创建失败！", e);
            throw new RuntimeException("【定时任务】创建失败！");
        }

    }

    @Override
    public void update(SysJob sysJob) {
        // 持久化数据
        SysJob dbSysJob = sysJobMapper.selectByPrimaryKey(sysJob.getId());
        if(dbSysJob == null){
            return;
        }
        sysJob.setModifyTime(new Date());
        sysJobMapper.updateByPrimaryKeySelective(sysJob);

        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(dbSysJob.getBeanClass(), dbSysJob.getJobGroup());
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysJob.getCronExpression()).withMisfireHandlingInstructionDoNothing();

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 根据Cron表达式构建一个Trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
            // 如果任务之前是暂停状态，rescheduleJob方法会重启该任务，需要手动去pauseJob
            if(JobStatusEnum.stop.getStatus().equals(dbSysJob.getJobStatus())){
                scheduler.pauseJob(JobKey.jobKey(dbSysJob.getBeanClass(), dbSysJob.getJobGroup()));
            }
        } catch (Exception e) {
            log.error("【定时任务】更新失败！", e);
            throw new RuntimeException("【定时任务】创建失败！");
        }
    }

    @Override
    public void pause(Integer id) {
        SysJob dbSysJob = sysJobMapper.selectByPrimaryKey(id);
        if(dbSysJob == null){
            return;
        }
        SysJob sysJob = new SysJob();
        sysJob.setId(id);
        sysJob.setJobStatus(JobStatusEnum.stop.getStatus());
        sysJob.setModifyTime(new Date());
        sysJobMapper.updateByPrimaryKeySelective(sysJob);

        try {
            scheduler.pauseJob(JobKey.jobKey(dbSysJob.getBeanClass(), dbSysJob.getJobGroup()));
        } catch (Exception e) {
            log.error("【定时任务】暂停失败！", e);
            throw new RuntimeException("【定时任务】暂停失败！");
        }
    }

    @Override
    public void resume(Integer id) {
        SysJob dbSysJob = sysJobMapper.selectByPrimaryKey(id);
        if(dbSysJob == null){
            return;
        }
        SysJob sysJob = new SysJob();
        sysJob.setId(id);
        sysJob.setJobStatus(JobStatusEnum.normal.getStatus());
        sysJob.setModifyTime(new Date());
        sysJobMapper.updateByPrimaryKeySelective(sysJob);

        try {
            scheduler.resumeJob(JobKey.jobKey(dbSysJob.getBeanClass(), dbSysJob.getJobGroup()));
        } catch (Exception e) {
            log.error("【定时任务】恢复失败！", e);
            throw new RuntimeException("【定时任务】恢复失败！");
        }
    }

    @Override
    public void delete(Integer id) {
        SysJob dbSysJob = sysJobMapper.selectByPrimaryKey(id);
        if(dbSysJob == null){
            return;
        }
        sysJobMapper.deleteByPrimaryKey(id);

        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(dbSysJob.getBeanClass(), dbSysJob.getJobGroup()));
            scheduler.unscheduleJob(TriggerKey.triggerKey(dbSysJob.getBeanClass(), dbSysJob.getJobGroup()));
            scheduler.deleteJob(JobKey.jobKey(dbSysJob.getBeanClass(), dbSysJob.getJobGroup()));
        } catch (Exception e) {
            log.error("【定时任务】删除失败！", e);
            throw new RuntimeException("【定时任务】删除失败！");
        }
    }
}
