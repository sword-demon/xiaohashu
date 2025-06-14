package top.wjstar.xiaohashu.oss.biz.strategy;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件策略接口
 */
public interface FileStrategy {

    /**
     * 文件策略的接口
     *
     * @param file       上传的文件对象
     * @param bucketName 存储桶
     * @return 存储链接
     */
    String uploadFile(MultipartFile file, String bucketName);
}
