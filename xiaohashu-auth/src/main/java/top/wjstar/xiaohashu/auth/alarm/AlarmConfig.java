package top.wjstar.xiaohashu.auth.alarm;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.wjstar.xiaohashu.auth.alarm.impl.MailAlarmHelper;
import top.wjstar.xiaohashu.auth.alarm.impl.SmsAlarmHelper;

@Configuration
@RefreshScope // 配置动态刷新
public class AlarmConfig {

    @Value(value = "${alarm.type}")
    private String alarmType;

    @Bean
    @RefreshScope
    public AlarmInterface alarmHelper() {
        // 根据配置文件中的告警类型,初始化选择不同的告警实现
        if (StringUtils.equals("sms", alarmType)) {
            return new SmsAlarmHelper();
        } else if (StringUtils.equals("email", alarmType)) {
            return new MailAlarmHelper();
        } else {
            throw new IllegalArgumentException("错误的告警类型...");
        }
    }
}
