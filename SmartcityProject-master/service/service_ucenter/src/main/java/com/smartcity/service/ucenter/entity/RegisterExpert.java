package com.smartcity.service.ucenter.entity;

import com.smartcity.common.commonutils.entity.Expert;
import com.smartcity.service.ucenter.entity.vo.RegisterVo;
import lombok.Data;

@Data
public class RegisterExpert {
    private Expert expert;

    private RegisterVo registerVo;
}
