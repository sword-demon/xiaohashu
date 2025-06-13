package top.wjstar.xiaohashu.gateway.exception;

import cn.dev33.satoken.exception.SaTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.wjstar.framework.common.response.Response;
import top.wjstar.xiaohashu.gateway.enums.ResponseCodeEnum;

/**
 * 网关的全局异常处理
 * 网关需要创建 ErrorWebExceptionHandler 的实现类 注入到 spring 容器中
 */
@Component
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        // 获取响应对象
        ServerHttpResponse response = exchange.getResponse();

        log.error("===> 网关全局异常捕获: ", ex);

        // 响应参数
        Response<?> result;
        // 根据捕获的异常类型,设置不同的响应状态码和响应消息
        if (ex instanceof SaTokenException) {
            // 权限认证失败时,设置 401 状态码
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 构建响应结果
            result = Response.fail(ResponseCodeEnum.UNAUTHORIZED.getErrorCode(), ResponseCodeEnum.UNAUTHORIZED.getErrorMessage());
        } else {
            // 其他异常,统一提示 系统繁忙
            result = Response.fail(ResponseCodeEnum.SYSTEM_ERROR);
        }

        // 设置响应头的内容为 application/json;charset=UTF-8
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // 设置 body 响应体
        return response.writeWith(Mono.fromSupplier(() -> {
            // 使用 Mono.FromSupplier 创建响应体
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                // 使用 ObjectMapper 将 result 对象转换为 JSON 字节数组
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(result));
            } catch (Exception e) {
                // 如果转换过程中出现异常,则返回空字节数组
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
