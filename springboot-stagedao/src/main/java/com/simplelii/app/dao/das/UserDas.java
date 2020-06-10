package com.simplelii.app.dao.das;

import com.simplelii.app.dao.base.BaseDas;
import com.simplelii.app.dao.eo.UserEo;
import com.simplelii.app.dao.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author liXin
 * @description
 * @date 2020/6/10
 */
@Repository
public class UserDas extends BaseDas {

    @Resource
    private UserMapper userMapper;

    // todo 后续单表操作封装base
    public Long addUser(UserEo eo) {
       return userMapper.addUser(eo);
    }
}
