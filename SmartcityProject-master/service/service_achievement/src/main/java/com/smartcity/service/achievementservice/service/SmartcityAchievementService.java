package com.smartcity.service.achievementservice.service;

import com.smartcity.service.achievementservice.entity.SmartcityAchievement;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 技术成果 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-15
 */
public interface SmartcityAchievementService extends IService<SmartcityAchievement> {
    public void saveByExcel(MultipartFile file, SmartcityAchievementService service);
}
