package com.smartcity.service.ucenter.entity;

import com.smartcity.common.commonutils.entity.Institution;
import com.smartcity.service.ucenter.entity.vo.RegisterVo;
import lombok.Data;

@Data
public class RegisterInstitution {
    private Institution institution;

    private RegisterVo registerVo;
}