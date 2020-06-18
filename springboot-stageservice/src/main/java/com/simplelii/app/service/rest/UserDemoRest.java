package com.simplelii.app.service.rest;

import com.simplelii.app.api.IUserApi;
import com.simplelii.app.api.dto.request.UserReqDto;
import com.simplelii.app.biz.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liXin
 * @description
 * @date 2020/6/10
 */
@RestController
@RequestMapping("/user")
public class UserDemoRest {

    @Resource
    private IUserService userService;

    @PostMapping("/add")
    public Long addUser(@RequestBody UserReqDto reqDto) {

        return userService.addUser(reqDto);
    }
}
