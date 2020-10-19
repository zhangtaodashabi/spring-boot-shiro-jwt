package com.yyy.common.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mr.Liu
 * @Classname BaseQueryDto
 * @Description BaseQueryDto
 * @Date 2020/4/17
 */
@Data
@ApiModel(value = "BaseQueryDto", description = "基类信息DTO")
public class BaseQueryDto implements Serializable {

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码", name = "pageNum")
    private int pageNum = 1;

    /**
     * 每页数量
     */
    @ApiModelProperty(value = "每页数量", name = "pageSize")
    private int pageSize = 10;

}
