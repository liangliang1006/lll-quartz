package com.lll.quartz.quartz;

import com.lll.quartz.service.ISysJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: liang
 * @Date: 2020/12/9 17:45
 * @Description
 */
@Component
public class ScheduleJobInitListener implements CommandLineRunner {

    @Autowired
    private ISysJobService sysJobService;

    @Override
    public void run(String... args) throws Exception {
        sysJobService.init();
    }
}
