package com.smartcity.service.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.JwtUtils;
import com.smartcity.common.commonutils.MD5;
import com.smartcity.common.servicebase.exceptionhandler.CustomException;
import com.smartcity.service.ucenter.entity.Member;
import com.smartcity.service.ucenter.entity.vo.LoginVo;
import com.smartcity.service.ucenter.entity.vo.RegisterVo;
import com.smartcity.service.ucenter.mapper.MemberMapper;
import com.smartcity.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 用户登录表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-09
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

/*    @Autowired
    private RedisTemplate<String, String> redisTemplate;*/

    @Override
    public Member register(RegisterVo registerVo) {
        String username = registerVo.getUsername();
        String password = registerVo.getPassword();
        String userId = registerVo.getUserId();
        Integer userType = registerVo.getType();
        if(StringUtils.isEmpty(username)||
                StringUtils.isEmpty(password)||
                StringUtils.isEmpty(userId)||
                userType == null){
            throw new CustomException(20001, "注册失败，请完善注册信息");
        }

        /*
        //校验短信验证码，利用redis
        String mobleCode = redisTemplate.opsForValue().get(username);
        if(!code.equals(mobleCode)) {
            throw new CustomException(20001,"error");
        }*/

        //查询数据库，是否存在相同用户名
        Integer count = baseMapper.selectCount(new QueryWrapper<Member>().eq("username", username));
        if (count.intValue()>0){
            throw new CustomException(20001, "当前用户名已存在");
        }

        //添加注册信息到数据库中
        Member member = new Member();
        member.setUsername(username);
        member.setType(userType);
        member.setPassword(MD5.encrypt(password));
        member.setUserId(userId);
        member.setIsDisabled(false);
        this.save(member);
        if (userId.equals("null")){
            member.setUserId(member.getId());
            this.updateById(member);
        }
        return member;
    }

    @Override
    public String login(LoginVo loginVo) {
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();

        //参数不为空
        if(StringUtils.isEmpty(username)||
                StringUtils.isEmpty(password)){
            throw new CustomException(20001, "请完善当前登录信息");
        }

        //获取用户信息
        Member user = baseMapper.selectOne(new QueryWrapper<Member>().eq("username", username));
        if (user == null){
            throw new CustomException(20001, "用户名不存在");
        }

        //密码
        if(!MD5.encrypt(password).equals(user.getPassword())){
            throw new CustomException(20001, "密码错误");
        }

        //是否被禁用
        if (user.getIsDisabled()){
            throw new CustomException(20001, "当前用户被禁用");
        }

        //使用JWT生成token字符串
        String token = JwtUtils.getJwtToken(user.getId(), user.getUsername());

        return token;
    }

    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
