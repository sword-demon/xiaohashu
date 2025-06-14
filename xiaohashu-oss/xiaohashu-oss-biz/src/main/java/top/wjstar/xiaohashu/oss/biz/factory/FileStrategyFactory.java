package top.wjstar.xiaohashu.oss.biz.factory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.wjstar.xiaohashu.oss.biz.strategy.FileStrategy;
import top.wjstar.xiaohashu.oss.biz.strategy.impl.MinioFileStrategy;

@Configuration
public class FileStrategyFactory {

    @Value("${storage.type}")
    private String strategyType;

    @Bean
    public FileStrategy getFileStrategy() {
        if (StringUtils.equals(strategyType, "minio")) {
            return new MinioFileStrategy();
        } else if (StringUtils.equals(strategyType, "aliyun")) {
            return new MinioFileStrategy();
        }

        throw new IllegalArgumentException("不可用的存储类型");
    }
}
