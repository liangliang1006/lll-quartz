package com.lll.quartz.service;

import com.lll.quartz.entity.common.Paginator;
import com.lll.quartz.entity.pojo.SysJob;

import java.util.List;

/**
 * @Author: liang
 * @Date: 2020/12/11 13:44
 * @Description
 */
public interface ISysJobService {
    void init();

    List<SysJob> list(Paginator paginator);

    void add(SysJob sysJob);

    void update(SysJob sysJob);

    void pause(Integer id);

    void resume(Integer id);

    void delete(Integer id);
}
