package com.lll.quartz.controller;


import com.lll.quartz.entity.common.Paginator;
import com.lll.quartz.entity.common.ReturnResult;
import com.lll.quartz.entity.pojo.SysJob;
import com.lll.quartz.service.ISysJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: liang
 * @Date: 2020/12/9 12:02
 * @Description
 */
@RestController
@RequestMapping("/sysJob")
public class SysJobController {

    @Autowired
    private ISysJobService sysJobService;


    @GetMapping(value = "/list")
    public ReturnResult<List<SysJob>> list(Paginator paginator){
        return ReturnResult.ok(sysJobService.list(paginator), paginator);
    }

    @PostMapping(value = "/add")
    public ReturnResult add(SysJob sysJob){
        sysJobService.add(sysJob);
        return ReturnResult.ok();
    }

    @PostMapping(value = "/update")
    public ReturnResult update(SysJob sysJob){
        sysJobService.update(sysJob);
        return ReturnResult.ok();
    }

    @PostMapping(value = "/pause")
    public ReturnResult pause(Integer id){
        sysJobService.pause(id);
        return ReturnResult.ok();
    }

    @PostMapping(value = "/resume")
    public ReturnResult resume(Integer id){
        sysJobService.resume(id);
        return ReturnResult.ok();
    }

    @PostMapping(value = "/delete")
    public ReturnResult delete(Integer id){
        sysJobService.delete(id);
        return ReturnResult.ok();
    }

}
