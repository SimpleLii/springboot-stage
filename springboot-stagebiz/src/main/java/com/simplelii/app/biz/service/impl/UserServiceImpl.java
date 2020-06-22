package com.simplelii.app.biz.service.impl;

import com.google.common.base.Splitter;
import com.simplelii.app.api.dto.request.UserReqDto;
import com.simplelii.app.biz.service.IUserService;
import com.simplelii.app.biz.utils.IdUtil;
import com.simplelii.app.dao.das.UserDas;
import com.simplelii.app.dao.eo.UserEo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetAddress;

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
        Long id = null;
        Long workId = 0L;
        try {
            String localHost = InetAddress.getLocalHost().getHostAddress();
            Iterable<String> ipSplit = Splitter.on(".").split(localHost);
            for (String anIpSplit : ipSplit) {
                workId += Long.parseLong(anIpSplit);
            }
            id = IdUtil.nextId(workId, 1L);
            UserEo eo = new UserEo();
            BeanUtils.copyProperties(userReqDto, eo);
            eo.setId(id);
            userDas.addUser(eo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
