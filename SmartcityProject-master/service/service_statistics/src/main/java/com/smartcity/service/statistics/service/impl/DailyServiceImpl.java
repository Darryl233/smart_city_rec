package com.smartcity.service.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.common.commonutils.R;
import com.smartcity.service.statistics.client.UcenterClient;
import com.smartcity.service.statistics.entity.Daily;
import com.smartcity.service.statistics.mapper.DailyMapper;
import com.smartcity.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-10
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {


    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void registerCount(String day) {

        //添加记录之前先删除相同日期的记录
        QueryWrapper<Daily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        baseMapper.delete(wrapper);

        R countR = ucenterClient.countRegister(day);
        Integer countRegister = (Integer) countR.getData().get("countRegister");

        Daily statisticDaily = new Daily();
        statisticDaily.setDateCalculated(day);
        statisticDaily.setRegisterNum(countRegister);

        //TODO: 日登陆人数， 日新增论文数， 日新增专家数
        statisticDaily.setLoginNum(RandomUtils.nextInt(100,200));
        statisticDaily.setPaperNum(RandomUtils.nextInt(100,200));
        statisticDaily.setExpertNum(RandomUtils.nextInt(100,200));

        baseMapper.insert(statisticDaily);
    }

    @Override
    public Map<String, Object> getChartData(String begin, String end, String type) {

        QueryWrapper<Daily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.select(type, "date_calculated");
        dayQueryWrapper.between("date_calculated", begin, end);
        List<Daily> dayList = baseMapper.selectList(dayQueryWrapper);
        Map<String, Object> map = new HashMap<>();
        List<Integer> dataList = new ArrayList<Integer>();
        List<String> dateList = new ArrayList<String>();
        map.put("dataList", dataList);
        map.put("dateList", dateList);
        for (int i = 0; i < dayList.size(); i++) {
            Daily daily = dayList.get(i);
            dateList.add(daily.getDateCalculated());
            switch (type) {
                case "register_num":
                    dataList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(daily.getLoginNum());
                    break;
                case "paper_num":
                    dataList.add(daily.getPaperNum());
                    break;
                case "expert_num":
                    dataList.add(daily.getExpertNum());
                    break;
                default:
                    break;
            }
        }
        return map;
    }
}
