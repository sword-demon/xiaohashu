package top.wjstar.xiaohashu.auth.alarm.impl;

import lombok.extern.slf4j.Slf4j;
import top.wjstar.xiaohashu.auth.alarm.AlarmInterface;

@Slf4j
public class MailAlarmHelper implements AlarmInterface {
    /**
     * 发送告警消息
     *
     * @param message 消息
     * @return 是否发送成功
     */
    @Override
    public boolean send(String message) {
        log.info("==> [邮件告警]: {}", message);
        return false;
    }
}
