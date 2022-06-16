package com.smartcity.service.ucenter.mapper;

import com.smartcity.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户登录表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-11-09
 */
public interface MemberMapper extends BaseMapper<Member> {

    Integer countRegisterDay(@Param("d") String day);
}
