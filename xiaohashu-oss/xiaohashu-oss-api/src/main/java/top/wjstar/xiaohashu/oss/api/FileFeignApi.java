package top.wjstar.xiaohashu.oss.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import top.wjstar.framework.common.response.Response;
import top.wjstar.xiaohashu.oss.config.FeignFormConfig;
import top.wjstar.xiaohashu.oss.constant.ApiConstants;

/**
 * 标记这个接口是一个 Feign 客户端的注解
 * 指定这个 Feign 客户端所调用的服务名称 这个名称通常在注册中心
 */
@FeignClient(name = ApiConstants.SERVICE_NAME, configuration = FeignFormConfig.class)
public interface FileFeignApi {

    String PREFIX = "/file";

    /**
     * 标记这个方法将执行一个 HTTP POST 请求,请求路径为 /file/test
     */
    @PostMapping(value = PREFIX + "/test")
    Response<?> test();

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping(value = PREFIX + "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response<?> uploadFile(@RequestPart(value = "file") MultipartFile file);
}
