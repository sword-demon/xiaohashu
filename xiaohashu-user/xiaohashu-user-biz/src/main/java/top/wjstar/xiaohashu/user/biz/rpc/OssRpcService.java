package top.wjstar.xiaohashu.user.biz.rpc;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.wjstar.framework.common.response.Response;
import top.wjstar.xiaohashu.oss.api.FileFeignApi;

/**
 * 对象存储服务调用
 */
@Component
public class OssRpcService {

    @Resource
    private FileFeignApi fileFeignApi;

    public String uploadFile(MultipartFile file) {
        Response<?> response = fileFeignApi.uploadFile(file);

        if (!response.isSuccess()) {
            return null;
        }

        // 返回图片访问链接
        return (String) response.getData();
    }
}
