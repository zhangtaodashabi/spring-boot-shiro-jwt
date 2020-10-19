package com.yyy.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Mr.Liu
 * @Classname BaseEntity
 * @Description 基类
 * @Date 2020/4/13
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1550705434158691568L;

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String lastUpdateBy;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdateTime;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 是否删除  -1：已删除  0：正常
     */
    @TableLogic
    private Integer delFlag;

}
