package top.wjstar.xiaohashu.auth.alarm;

public interface AlarmInterface {

    /**
     * 发送告警消息
     *
     * @param message 消息
     * @return 是否发送成功
     */
    boolean send(String message);
}
