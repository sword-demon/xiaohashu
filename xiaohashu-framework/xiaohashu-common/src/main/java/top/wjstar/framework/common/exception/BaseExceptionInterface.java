package top.wjstar.framework.common.exception;

public interface BaseExceptionInterface {

    /**
     * 获取异常码
     * @return 异常码
     */
    String getErrorCode();

    /**
     * 获取异常信息
     * @return 异常信息
     */
    String getErrorMessage();
}
