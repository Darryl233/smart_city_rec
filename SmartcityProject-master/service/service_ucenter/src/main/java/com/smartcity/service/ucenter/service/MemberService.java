package com.smartcity.service.ucenter.service;

import com.smartcity.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcity.service.ucenter.entity.vo.LoginVo;
import com.smartcity.service.ucenter.entity.vo.RegisterVo;

/**
 * <p>
 * 用户登录表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-09
 */
public interface MemberService extends IService<Member> {

    Member register(RegisterVo registerVo);

    String login(LoginVo loginVo);

    Integer countRegisterDay(String day);
}
