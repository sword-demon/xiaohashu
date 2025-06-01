package top.wjstar.xiaohashu.auth.alarm.impl;

import lombok.extern.slf4j.Slf4j;
import top.wjstar.xiaohashu.auth.alarm.AlarmInterface;

@Slf4j
public class SmsAlarmHelper implements AlarmInterface {
    /**
     * 发送告警消息
     *
     * @param message 消息
     * @return 是否发送成功
     */
    @Override
    public boolean send(String message) {
        log.info("==> [短信告警]: {}", message);

        // 业务逻辑
        return false;
    }
}
