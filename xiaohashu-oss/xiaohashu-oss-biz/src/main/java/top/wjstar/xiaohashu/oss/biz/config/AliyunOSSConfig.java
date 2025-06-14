package top.wjstar.xiaohashu.oss.biz.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunOSSConfig {

    @Resource
    private AliyunOssProperties aliyunOssProperties;

    @Bean
    public OSS aliyunOSSClient() {
        // 设置访问凭证
        DefaultCredentialProvider credentialProvider = CredentialsProviderFactory.newDefaultCredentialProvider(
                aliyunOssProperties.getAccessKey(), aliyunOssProperties.getSecretKey()
        );

        // 创建 OSSClient 实例
        return new OSSClientBuilder().build(aliyunOssProperties.getEndpoint(), credentialProvider);
    }
}
