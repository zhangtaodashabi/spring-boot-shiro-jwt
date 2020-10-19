package com.yyy.common.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyy.common.dao.OperationLogDAO;
import com.yyy.common.pojo.entity.OperationLogDO;
import com.yyy.common.service.IOperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogDAO, OperationLogDO> implements IOperationLogService {

}
