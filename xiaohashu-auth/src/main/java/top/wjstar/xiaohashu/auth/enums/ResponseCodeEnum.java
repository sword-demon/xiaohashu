package top.wjstar.xiaohashu.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.wjstar.framework.common.exception.BaseExceptionInterface;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    SYSTEM_ERROR("AUTH_10000", "出错啦，后台小哥正在努力修复中..."),
    PARAM_NOT_VALID("AUTH-10001", "参数错误"),
    VERIFICATION_CODE_SEND_FREQUENTLY("AUTH-20000", "请求太频繁，请3分钟后再试"),
    VERIFICATION_CODE_ERROR("AUTH_20001", "验证码错误"),
    ;

    private final String errorCode;
    private final String errorMessage;
}
