package com.simplelii.app.biz.service;

import com.github.pagehelper.PageInfo;
import com.simplelii.app.api.dto.request.UserReqDto;
import com.simplelii.app.api.dto.response.UserRespDto;
import com.simplelii.app.dao.eo.UserEo;

/**
 * @author liXin
 * @description
 * @date 2020/6/10
 */
public interface IUserService {
    /**
     *  新增user
     * @param userReqDto
     * @return
     */
    Long addUser(UserReqDto userReqDto);

    PageInfo<UserEo> query();
}
