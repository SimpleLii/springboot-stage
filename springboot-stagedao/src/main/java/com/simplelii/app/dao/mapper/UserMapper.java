package com.simplelii.app.dao.mapper;

import com.simplelii.app.common.dao.base.BaseMapper;
import com.simplelii.app.dao.eo.UserEo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @author liXin
 * @description
 * @date 2020/6/10
 */
public interface UserMapper extends BaseMapper<UserEo> {

    @Insert({
            "insert into user values(#{eo.id}, #{eo.name}, #{eo.address})"
    })
    public Long addUser(@Param("eo") UserEo eo);
}
