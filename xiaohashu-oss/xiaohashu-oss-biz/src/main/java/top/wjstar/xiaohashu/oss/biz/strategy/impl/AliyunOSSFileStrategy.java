package top.wjstar.xiaohashu.oss.biz.strategy.impl;

import com.aliyun.oss.OSS;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import top.wjstar.xiaohashu.oss.biz.config.AliyunOssProperties;
import top.wjstar.xiaohashu.oss.biz.strategy.FileStrategy;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Slf4j
public class AliyunOSSFileStrategy implements FileStrategy {

    @Resource
    private AliyunOssProperties aliyunOssProperties;
    @Resource
    private OSS ossClient;

    /**
     * 文件策略的接口
     *
     * @param file       上传的文件对象
     * @param bucketName 存储桶
     * @return 存储链接
     */
    @Override
    @SneakyThrows
    public String uploadFile(MultipartFile file, String bucketName) {
        log.info("## 上传文件至阿里云 OSS ...");

        if (file == null || file.getSize() == 0) {
            log.error("==> 上传文件异常: 文件大小为空...");
            throw new RuntimeException("文件大小不能为空");
        }

        // 文件的原始名称
        String originalFilename = file.getOriginalFilename();
        String key = UUID.randomUUID().toString().replace("-", "");
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = String.format("%s%s", key, suffix);

        log.info("==> 开始上传文件至阿里云 OSS, ObjectName: {}", objectName);
        // 上传文件至阿里云 OSS
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(file.getInputStream().readAllBytes()));

        // 返回文件的访问链接
        String url = String.format("https://%s.%s/%s", bucketName, aliyunOssProperties.getEndpoint(), objectName);
        log.info("==> 上传文件至阿里云 OSS 成功，访问路径: {}", url);
        return url;
    }
}
