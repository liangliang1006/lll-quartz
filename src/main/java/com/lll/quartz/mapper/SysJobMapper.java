package com.lll.quartz.mapper;

import com.lll.quartz.entity.pojo.SysJob;

import java.util.List;

public interface SysJobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysJob record);

    SysJob selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysJob record);

    List<SysJob> selectList();
}