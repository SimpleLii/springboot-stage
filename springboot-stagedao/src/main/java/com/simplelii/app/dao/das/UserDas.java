package com.simplelii.app.dao.das;

import com.simplelii.app.common.dao.base.BaseDas;
import com.simplelii.app.dao.eo.UserEo;
import com.simplelii.app.dao.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author liXin
 * @description
 * @date 2020/6/10
 */
@Repository
public class UserDas extends BaseDas {

    @Resource
    private UserMapper userMapper;

    @Deprecated
    public Long addUser(UserEo eo) {
        Map mappers = this.getMappers();
        UserMapper userMapper = (UserMapper) this.getMapper();
        return userMapper.addUser(eo);
    }

}
