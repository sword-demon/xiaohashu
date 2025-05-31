package top.wjstar.xiaohashu.auth.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.wjstar.framework.common.exception.BizException;
import top.wjstar.framework.common.response.Response;
import top.wjstar.xiaohashu.auth.enums.ResponseCodeEnum;

import java.util.Optional;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获自定义异常
     * @param request 请求信息
     * @param e 自定义异常
     * @return 自定义响应内容
     */
    @ExceptionHandler(value = BizException.class)
    public Response<Object> handleBizException(HttpServletRequest request, BizException e) {
        log.warn("{} request fail, errorCode: {}, errorMessage: {}", request.getRequestURI(), e.getErrorCode(), e.getErrorMessage());
        return Response.fail(e);
    }

    /**
     * 捕获参数校验异常
     * @param request 请求信息
     * @param e 参数校验异常
     * @return 自定义响应内容
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Response<Object> handleMethodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException e) {
        // 参数错误异常码
        String errorCode = ResponseCodeEnum.PARAM_NOT_VALID.getErrorCode();

        // 捕获 BindingResult
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder sb = new StringBuilder();

        // 获取校验不通过的字段，并组合综合错误信息
        Optional.of(bindingResult.getFieldErrors()).ifPresent(
                errors -> errors.forEach(error ->
                        sb.append(error.getField())
                                .append(" ")
                                .append(error.getDefaultMessage())
                                .append(", 当前值: '")
                                .append(error.getRejectedValue())
                                .append("'; ")
                )
        );

        // 错误信息
        String errorMessage = sb.toString();

        log.warn("{} request fail, errorCode: {}, errorMessage: {}", request.getRequestURI(), errorCode, errorMessage);

        return Response.fail(errorCode, errorMessage);
    }

    /**
     * 其他类型异常
     * @param request 请求信息
     * @param e 其他异常
     * @return 自定义响应内容
     */
    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public Response<Object> handleOtherException(HttpServletRequest request, Exception e) {
        log.error("{} request error, ", request.getRequestURI(), e);
        return Response.fail(ResponseCodeEnum.SYSTEM_ERROR);
    }
}
