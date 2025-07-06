package top.wjstar.xiaohashu.oss.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import top.wjstar.framework.common.response.Response;
import top.wjstar.xiaohashu.oss.constant.ApiConstants;

/**
 * 标记这个接口是一个 Feign 客户端的注解
 * 指定这个 Feign 客户端所调用的服务名称 这个名称通常在注册中心
 */
@FeignClient(name = ApiConstants.SERVICE_NAME)
public interface FileFeignApi {

    String PREFIX = "/file";

    /**
     * 标记这个方法将执行一个 HTTP POST 请求,请求路径为 /file/test
     */
    @PostMapping(value = PREFIX + "/test")
    Response<?> test();
}
