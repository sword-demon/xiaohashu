package top.wjstar.xiaohashu.oss.biz.service;

import org.springframework.web.multipart.MultipartFile;
import top.wjstar.framework.common.response.Response;

public interface FileService {

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return
     */
    Response<?> uploadFile(MultipartFile file);
}
