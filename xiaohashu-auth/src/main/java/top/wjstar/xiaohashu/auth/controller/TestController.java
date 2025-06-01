package top.wjstar.xiaohashu.auth.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wjstar.xiaohashu.auth.alarm.AlarmInterface;

@RestController
@Slf4j
public class TestController {

    @NacosValue(value = "${rate-limit.api.limit}", autoRefreshed = true)
    private Integer limit;

    @Resource
    private AlarmInterface alarm;

    @GetMapping("/test")
    public String test() {
        return "当前限流为: " + limit;
    }

    @GetMapping("/alarm")
    public String sendAlarm() {
        alarm.send("系统出错啦,速度上线解决问题!");
        return "alarm success";
    }
}
