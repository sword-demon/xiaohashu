package top.wjstar.xiaohashu.gateway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.wjstar.framework.common.exception.BaseExceptionInterface;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    SYSTEM_ERROR("500", "系统繁忙,请稍后再试"),
    UNAUTHORIZED("401", "权限不足"),
    ;

    private final String errorCode;
    private final String errorMessage;
}
