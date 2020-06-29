package com.simplelii.app.api.dto.response;

import com.simplelii.app.common.response.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liXin
 * @description
 * @date 2020/6/29
 */
@Data
@ApiModel(description = "用户RespDto", value = "UserRespDto")
public class UserRespDto extends BaseDto {

    @ApiModelProperty(name = "姓名", value = "name")
    private String name;

}
