package com.simplelii.app.dao.das;

import com.google.common.collect.Lists;
import com.simplelii.app.common.dao.base.BaseDas;
import com.simplelii.app.dao.eo.UserEo;
import com.simplelii.app.dao.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @author liXin
 * @description
 * @date 2020/6/10
 */
@Repository
public class UserDas extends BaseDas {

    @Resource
    private UserMapper userMapper;

    public void addUser(UserEo eo) {
        ArrayList<UserEo> userEos = Lists.newArrayList(eo);
        UserEo eo1 = new UserEo();
        eo1.setAddress("ewrqw");
        eo1.setName("dsf");
        userEos.add(eo1);
        userMapper.insertBatch(userEos);
    }

}
