package com.simplelii.app.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author liXin
 * @description
 * @date 2020/6/10
 */
@ApiModel(description = "DTO基类", value = "BaseDto")
@Data
public class BaseDto implements Serializable {

    private static final long serialVersionUID = 4650830117825660133L;

    @ApiModelProperty(name = "extensionMap", value = "扩展字段extensionMap")
    private HashMap<String, Object> extensionMap;

    @ApiModelProperty(name = "def1", value = "扩展字段def1")
    private String def1;

    @ApiModelProperty(name = "def2", value = "扩展字段def2")
    private String def2;

    @ApiModelProperty(name = "def3", value = "扩展字段def3")
    private String def3;


}
