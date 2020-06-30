package com.simplelii.app.biz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simplelii.app.api.dto.request.UserReqDto;
import com.simplelii.app.api.dto.response.UserRespDto;
import com.simplelii.app.biz.service.IUserService;
import com.simplelii.app.common.dao.sql.SqlCondition;
import com.simplelii.app.dao.das.UserDas;
import com.simplelii.app.dao.eo.UserEo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

        int count = userDas.count();
        return eo.getId();
    }

    @Override
    public PageInfo<UserEo> query() {
//        Page page = PageHelper.startPage(1, 10);
        List<UserEo> userEos = userDas.queryByEo();
        return new PageInfo(userEos);
    }
}
