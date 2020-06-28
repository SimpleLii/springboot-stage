package com.simplelii.app.api.dto.request;

import com.simplelii.app.common.response.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liXin
 * @description
 * @date 2020/6/10
 */
@ApiModel(value = "UserReqDto", description = "UserReqDto")
@Data
public class UserReqDto extends BaseDto {

    private static final long serialVersionUID = -6417739753855455407L;

    @ApiModelProperty(name = "name", value = "姓名")
    private String name;

    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    @ApiModelProperty(name = "address", value = "地址")
    private String address;

}
