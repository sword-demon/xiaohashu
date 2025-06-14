package top.wjstar.xiaohashu.oss.biz.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.wjstar.framework.common.response.Response;
import top.wjstar.xiaohashu.oss.biz.service.FileService;
import top.wjstar.xiaohashu.oss.biz.strategy.FileStrategy;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Resource
    private FileStrategy fileStrategy;

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return
     */
    @Override
    public Response<?> uploadFile(MultipartFile file) {
        fileStrategy.uploadFile(file, "xiaohashu");
        return null;
    }
}
