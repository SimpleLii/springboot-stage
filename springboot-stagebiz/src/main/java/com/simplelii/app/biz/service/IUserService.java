package com.simplelii.app.biz.service;

import com.simplelii.app.api.dto.request.UserReqDto;

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
}
