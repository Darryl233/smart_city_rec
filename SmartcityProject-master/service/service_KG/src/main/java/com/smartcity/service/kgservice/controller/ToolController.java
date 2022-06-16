package com.smartcity.service.kgservice.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(description = "知识图谱工具服务")
@Controller
@RequestMapping("/kg/tool")
public class ToolController {

    @GetMapping("/testGraph")
    public String testGraph(){
        System.out.println("this is ToolsController.testGraph...");
        return "testGraph";
    }

    //@GetMapping(value = "/test", produces="application/json,charset=utf-8")
    @GetMapping(value = "/test")
    public String testString(){
        String testString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>第一个 ECharts 实例</title>\n" +
                "    <!-- 引入 echarts.js -->\n" +
                "    <script src=\"https://cdn.staticfile.org/echarts/4.3.0/echarts.min.js\"></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->\n" +
                "    <div id=\"main\" style=\"width: 600px;height:400px;\"></div>\n" +
                "    <script type=\"text/javascript\">\n" +
                "        // 基于准备好的dom，初始化echarts实例\n" +
                "        var myChart = echarts.init(document.getElementById('main'));\n" +
                " \n" +
                "        // 指定图表的配置项和数据\n" +
                "        var option = {\n" +
                "            title: {\n" +
                "                text: '第一个 ECharts 实例'\n" +
                "            },\n" +
                "            tooltip: {},\n" +
                "            legend: {\n" +
                "                data:['销量']\n" +
                "            },\n" +
                "            xAxis: {\n" +
                "                data: [\"衬衫\",\"羊毛衫\",\"雪纺衫\",\"裤子\",\"高跟鞋\",\"袜子\"]\n" +
                "            },\n" +
                "            yAxis: {},\n" +
                "            series: [{\n" +
                "                name: '销量',\n" +
                "                type: 'bar',\n" +
                "                data: [5, 20, 36, 10, 10, 20]\n" +
                "            }]\n" +
                "        };\n" +
                " \n" +
                "        // 使用刚指定的配置项和数据显示图表。\n" +
                "        myChart.setOption(option);\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
        return testString;
    }
}
