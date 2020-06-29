package com.simplelii.app.service.rest;

import com.github.pagehelper.PageInfo;
import com.simplelii.app.api.IUserApi;
import com.simplelii.app.api.dto.request.UserReqDto;
import com.simplelii.app.biz.service.IUserService;
import com.simplelii.app.common.response.RestResponse;
import com.simplelii.app.dao.eo.UserEo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author liXin
 * @description
 * @date 2020/6/10
 */
@RestController
@RequestMapping("/user")
public class UserDemoRest implements IUserApi {

    @Resource
    private IUserService userService;

    @PostMapping("/add")
    public RestResponse<Long> addUser(@RequestBody UserReqDto reqDto) {

        return new RestResponse<>(userService.addUser(reqDto));
    }

    @GetMapping("/query")
    public RestResponse<PageInfo<UserEo>> queryUser() {

        return new RestResponse<>(userService.query());
    }
}
