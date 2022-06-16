package com.smartcity.service.ucenter.controller;


import com.smartcity.common.commonutils.JwtUtils;
import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.Expert;
import com.smartcity.common.commonutils.entity.Institution;
import com.smartcity.service.ucenter.client.ExpertClient;
import com.smartcity.service.ucenter.client.InstitutionClient;
import com.smartcity.service.ucenter.entity.Member;
import com.smartcity.service.ucenter.entity.RegisterExpert;
import com.smartcity.service.ucenter.entity.RegisterInstitution;
import com.smartcity.service.ucenter.entity.vo.LoginVo;
import com.smartcity.service.ucenter.entity.vo.RegisterVo;
import com.smartcity.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户登录表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-09
 */
@Api(description = "登录注册")
@RestController
@RequestMapping("/ucenter/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ExpertClient expertClient;

    @Autowired
    private InstitutionClient institutionClient;

    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public R login(@RequestBody LoginVo loginVo){
        String token = memberService.login(loginVo);
        return R.ok().data("token", token);
    }

    //一般只能由一个requestBody，https://blog.csdn.net/qq_35624642/article/details/103351337
    @ApiOperation(value = "注册普通用户")
    @PostMapping(value = "/register")
    public R register(
            @ApiParam(name = "registerVo", value = "注册信息", required = true)
            @RequestBody RegisterVo registerVo){
        try {
            registerVo.setType(1);
            registerVo.setUserId("null");
            Member member = memberService.register(registerVo);
            return R.ok().data("member", member);
        }catch (Exception e){
            return R.error().message(e.getMessage());
        }

    }


    /**{
     "expert": {
     "avatar": "string",
     "career": "string",
     "code": "string",
     "domain": "string",
     "email": "string",
     "fax": "string",
     "institution": "string",
     "intro": "string",
     "name": "string",
     "phone": "string"
     },
     "registerVo": {
     "password": "string",
     "username": "string123"
     }
     **/
    //一般只能由一个requestBody，https://blog.csdn.net/qq_35624642/article/details/103351337
    @ApiOperation(value = "注册专家")
    @PostMapping(value = "/registerExpert")
    @Transactional
    public R registerExpert(
            @ApiParam(name = "registerExpert", value = "注册专家信息", required = true)
            @RequestBody RegisterExpert registerExpert){
        try{
            Expert expert = registerExpert.getExpert();
            Expert expertByRegister = expertClient.addExpertByRegister(expert);
            RegisterVo registerVo = registerExpert.getRegisterVo();
            System.out.println(expertByRegister);
            registerVo.setUserId(expertByRegister.getId());
            registerVo.setType(2);
            memberService.register(registerVo);
            return R.ok().data("expert", expertByRegister);
        }catch (Exception e){
            return R.error().message(e.getMessage());
        }

    }

    //一般只能由一个requestBody，https://blog.csdn.net/qq_35624642/article/details/103351337
    @ApiOperation(value = "注册单位")
    @PostMapping(value = "/registerInstitution")
    @Transactional
    public R registerInstitution(
            @ApiParam(name = "registerInstitution", value = "注册单位信息", required = true)
            @RequestBody RegisterInstitution registerInstitution){
        try{
            Institution institution = registerInstitution.getInstitution();
            Institution instituionByRegister = institutionClient.addInstitutionByRegister(institution);
            RegisterVo registerVo = registerInstitution.getRegisterVo();
            System.out.println(instituionByRegister);
            registerVo.setUserId(instituionByRegister.getId());
            registerVo.setType(3);
            memberService.register(registerVo);
            return R.ok().data("institution", instituionByRegister);
        }catch (Exception e){
            return R.error().message(e.getMessage());
        }

    }

    //前端需要将token放在request的header中
    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping(value = "/getLoginInfo")
    public R getLoginInfo(HttpServletRequest request){
        //调用jwt工具类，根据request获取头信息，返回用户id,不是user_id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库，根据用户id获取用户信息
        Member member = memberService.getById(memberId);
        return R.ok().data("loginInfo", member);
    }

    //查询某天注册人数
    @ApiOperation(value = "某一天的注册人数")
    @GetMapping("/countRegister/{day}")
    public R countRegister(@PathVariable String day){
        Integer count = memberService.countRegisterDay(day);
        return R.ok().data("countRegister", count);
    }

}

