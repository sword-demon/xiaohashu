package top.wjstar.xiaohashu.oss.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.wjstar.framework.common.exception.BaseExceptionInterface;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    SYSTEM_ERROR("OSS-10000", "出错啦,后台小哥正在努力修复中..."),
    PARAM_NOT_VALID("OSS-10001", "参数错误"),
    ;

    private final String errorCode;
    private final String errorMessage;
}
