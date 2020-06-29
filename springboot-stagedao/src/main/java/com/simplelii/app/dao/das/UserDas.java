package com.simplelii.app.dao.das;

import com.google.common.collect.Lists;
import com.simplelii.app.common.dao.base.BaseDas;
import com.simplelii.app.common.dao.sql.SqlCondition;
import com.simplelii.app.common.dao.sql.SqlOrderBy;
import com.simplelii.app.dao.eo.UserEo;
import com.simplelii.app.dao.mapper.UserMapper;
import org.apache.catalina.User;
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
        sqlConditions.add(SqlCondition.eq("id", 1245690923534717955L));
        userEo.setSqlConditions(sqlConditions);
        userMapper.deleteLogicByEo(userEo);
    }

    public void updateSelect() {
        UserEo eo = new UserEo();
        eo.setId(1245690923534717955L);
        eo.setName("updateName");
        userMapper.updateSelect(eo);
    }

    public UserEo queryById(long l) {
        List<Long> ids = Lists.newArrayList(l);
        ids.add(4546465465464L);
        List<UserEo> userEos = userMapper.queryByIdsDr(UserEo.class, ids, Boolean.TRUE, "updateTime", "id");
        return null;
    }

    public List<UserEo> queryByEo() {
        UserEo eo = new UserEo();
        eo.setName("zhangsan");
//        List<SqlCondition> sqlConditions = Lists.newArrayList(SqlCondition.eq("name", "zhangsan"));
//        eo.setSqlConditions(sqlConditions);
//        List<SqlOrderBy> sqlOrderByList = new ArrayList<>();
//        sqlOrderByList.add(SqlOrderBy.ASC("id"));
//        sqlOrderByList.add(SqlOrderBy.DESC("name"));
//        eo.setSqlOrderBys(sqlOrderByList);
//          userMapper.queryAll(UserEo.class , "id", "name");
        userMapper.countByCondition(eo);
        List<UserEo> userEos = userMapper.queryByEo(eo);
        return userEos;

    }
}
