package com.smartcity.service.ucenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.R;
import com.smartcity.common.commonutils.entity.CommonHistory;
import com.smartcity.service.ucenter.client.*;
import com.smartcity.service.ucenter.entity.UserHistory;
import com.smartcity.service.ucenter.service.UserHistoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户浏览记录 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-30
 */
@RestController
@RequestMapping("/ucenter/userhistory")
public class UserHistoryController {
    @Autowired
    private UserHistoryService userHistoryService;

//    @Autowired
//    private AchievementClient achievementClient;
//
//    @Autowired
//    private CaseClient caseClient;
//
//    @Autowired
//    private ExpertClient expertClient;
//
//    @Autowired
//    private InstitutionClient institutionClient;
//
//    @Autowired
//    private PaperClient paperClient;
//
//    @Autowired
//    private PatentClient patentClient;
//
//    @Autowired
//    private RequirementClient requirementClient;
//
//    @Autowired
//    private SolutionClient solutionClient;

    @ApiOperation(value = "记录用户浏览记录")
    @PostMapping(value = "/saveHistory")
    public boolean saveHistory(@RequestBody CommonHistory history){

        UserHistory userHistory = new UserHistory();
        if (history.getLogId()=="" || StringUtils.isEmpty(history.getKgId()))
            return false;
        userHistory.setLogId(history.getLogId());
        userHistory.setResourceType(history.getResourceType());
        userHistory.setKgId(history.getKgId());

        QueryWrapper<UserHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("log_id", history.getLogId());
        wrapper.eq("resource_type", history.getResourceType());
        int count = userHistoryService.count(wrapper);
        if (count >= 100){
            wrapper.orderByAsc("gmt_modified");
            wrapper.last("limit 1");
            userHistoryService.remove(wrapper);
        }
        wrapper.eq("kg_id", history.getKgId());
        UserHistory one = userHistoryService.getOne(wrapper);
        if (one != null){
            userHistory.setId(one.getId());
        }
        boolean save = userHistoryService.saveOrUpdate(userHistory);
        return save;
    }

    @ApiOperation(value = "按resource类型获取用户浏览记录")
    @PostMapping(value = "/getHistory")
    public List<CommonHistory> getHistory(
            @RequestParam(value = "logid") String logid,
            @RequestParam(value = "resourceType", required = false) Integer resourceType){
        List<CommonHistory> historyList = new ArrayList<>();
        QueryWrapper<UserHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("log_id", logid);
        if (resourceType != null)
           wrapper.eq("kg_id", resourceType);
        List<UserHistory> userHistoryList = userHistoryService.list(wrapper);
        for (int i = 0; i<userHistoryList.size(); i++){
            UserHistory userHistory = userHistoryList.get(i);
            CommonHistory history = new CommonHistory();
            history.setLogId(userHistory.getLogId());
            history.setResourceType(userHistory.getResourceType());
            history.setKgId(userHistory.getKgId());
            historyList.add(history);
        }
        return historyList;
    }

    @ApiOperation(value = "所有用户浏览记录")
    @GetMapping(value = "/getAllHistory")
    public List<CommonHistory> getAllHistory(){
        List<CommonHistory> historyList = new ArrayList<>();
        List<UserHistory> userHistoryList = userHistoryService.list(null);
        for (int i = 0; i < userHistoryList.size(); i++){
            UserHistory userHistory = userHistoryList.get(i);
            CommonHistory history = new CommonHistory();
            history.setLogId(userHistory.getLogId());
            history.setResourceType(userHistory.getResourceType());
            history.setKgId(userHistory.getKgId());
            historyList.add(history);
        }
        return historyList;
    }
}

