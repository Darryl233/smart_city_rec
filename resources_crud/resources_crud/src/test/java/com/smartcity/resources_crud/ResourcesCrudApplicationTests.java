package com.smartcity.resources_crud;

import com.smartcity.resources_crud.controller.achievement.SmartcityAchievementController;
import com.smartcity.resources_crud.entity.achievement.SmartcityAchievement;
import com.smartcity.resources_crud.mapper.achievement.SmartcityAchievementMapper;
import com.smartcity.resources_crud.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ResourcesCrudApplicationTests {

    @Autowired
    private SmartcityAchievementMapper achievementMapper;

    @Autowired
    private ResourceService<SmartcityAchievement> service;


    @Test
    void contextLoads() {
        SmartcityAchievementController controller = new SmartcityAchievementController();
        List<SmartcityAchievement> achievements = service.list(null);
        achievements.forEach(System.out::println);
    }

}
