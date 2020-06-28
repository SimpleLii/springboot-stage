package com.simplelii.app.dao.das;

import com.simplelii.app.common.dao.base.BaseDas;
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


    public Long addUser(UserEo eo) {
      this.updateSelect();
      return 12L;
    }

    public void updateSelect(){
        UserEo eo = new UserEo();
        eo.setId(1245690923534717955L);
        eo.setName("updateName");
        userMapper.updateSelect(eo);
    }

}
