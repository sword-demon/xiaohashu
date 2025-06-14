package top.wjstar.xiaohashu.oss.biz.strategy.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import top.wjstar.xiaohashu.oss.biz.strategy.FileStrategy;

@Slf4j
public class AliyunOSSFileStrategy implements FileStrategy {
    /**
     * 文件策略的接口
     *
     * @param file       上传的文件对象
     * @param bucketName 存储桶
     * @return 存储链接
     */
    @Override
    public String uploadFile(MultipartFile file, String bucketName) {
        log.info("## 上传文件至阿里云 OSS ...");
        return "";
    }
}
