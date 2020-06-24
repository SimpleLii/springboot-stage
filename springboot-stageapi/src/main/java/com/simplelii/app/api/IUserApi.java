package com.simplelii.app.api;

import com.simplelii.app.api.dto.RestResponse;
import com.simplelii.app.api.dto.request.UserReqDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *  服务接口
 * @author liXin
 * @description
 * @date 2020/6/9
 */
@ApiModel(value = "IUserApi", description = "用户增删改API,改接口不使用Fein")
public interface IUserApi {

    @ApiModelProperty(value = "新增 user", notes = "新增 user")
    public RestResponse<Long> addUser(UserReqDto userReqDto);


}
