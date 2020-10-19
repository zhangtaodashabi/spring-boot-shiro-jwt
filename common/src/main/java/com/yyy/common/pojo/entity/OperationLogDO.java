package com.yyy.common.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author Mr.liu
 * @since
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("tb_operation_log")
public class OperationLogDO extends Model<OperationLogDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "operation_log_id", type = IdType.AUTO)
    private Integer operationLogId;
    /**
     * 日志描述
     */
    @TableField("log_description")
    private String logDescription;
    /**
     * 方法参数
     */
    @TableField("action_args")
    private String actionArgs;
    /**
     * 用户主键
     */
    @TableField("user_name")
    private String userName;
    /**
     * 类名称
     */
    @TableField("class_name")
    private String className;
    /**
     * 方法名称
     */
    @TableField("method_name")
    private String methodName;
    private String ip;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;
    /**
     * 是否成功 1:成功 2异常
     */
    private Integer succeed;
    /**
     * 异常堆栈信息
     */
    private String message;

    /**
     * 模块名称
     */
    @TableField("model_name")
    private String modelName;

    @TableField("update_time")
    private Date updateTime;

    @TableLogic
    @TableField("del_flag")
    private Boolean delFlag;
    /**
     * 操作
     */
    private String action;

    @Override
    protected Serializable pkVal() {
        return this.operationLogId;
    }

}
