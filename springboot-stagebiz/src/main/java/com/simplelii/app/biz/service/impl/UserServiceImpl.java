package com.simplelii.app.biz.service.impl;

import com.simplelii.app.api.dto.request.UserReqDto;
import com.simplelii.app.biz.service.IUserService;
import com.simplelii.app.dao.das.UserDas;
import com.simplelii.app.dao.eo.UserEo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liXin
 * @description
 * @date 2020/6/10
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserDas userDas;

    @Override
    public Long addUser(UserReqDto userReqDto) {
        UserEo eo = new UserEo();
        BeanUtils.copyProperties(userReqDto, eo);
        userDas.addUser(eo);
        return eo.getId();
    }
}
