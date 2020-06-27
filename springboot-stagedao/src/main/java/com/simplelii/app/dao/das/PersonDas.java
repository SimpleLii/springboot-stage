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
public class PersonDas extends BaseDas {

    @Resource
    private UserMapper userMapper;

    @Deprecated
    public Long addUser(UserEo eo) {
        UserMapper userMapper = (UserMapper) this.getMapper();
        return userMapper.addUser(eo);
    }

}
