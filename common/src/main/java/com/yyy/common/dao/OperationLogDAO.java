package com.yyy.common.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yyy.common.pojo.entity.OperationLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OperationLogDAO extends BaseMapper<OperationLogDO> {

}
