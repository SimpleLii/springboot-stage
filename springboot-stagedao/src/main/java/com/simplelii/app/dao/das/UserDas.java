package com.simplelii.app.dao.das;

import com.simplelii.app.common.dao.base.BaseDas;
import com.simplelii.app.common.dao.sql.SqlCondition;
import com.simplelii.app.dao.eo.UserEo;
import com.simplelii.app.dao.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
        UserEo userEo = new UserEo();
        List<SqlCondition> sqlConditions = new ArrayList<>();
        sqlConditions.add(SqlCondition.Operator.in.setProperty("id", 1245690923534717955L));
        sqlConditions.add(SqlCondition.Operator.like.setProperty("name", "%%updateName"));
        userEo.setSqlConditions(sqlConditions);
        userMapper.updateSelectBySqlCondition(userEo);

    }

    public void updateSelect() {
        UserEo eo = new UserEo();
        eo.setId(1245690923534717955L);
        eo.setName("updateName");
        userMapper.updateSelect(eo);
    }

}
