package top.wjstar.xiaohashu.oss.config;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignFormConfig {

    /**
     * 用于处理表单提交 对象编码为表单数据格式 application/x-www-form-urlencoded 或 multipart/form-data
     *
     * @return
     */
    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder();
    }
}
