package top.wjstar.framework.biz.context.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import top.wjstar.framework.biz.context.filter.HeaderUserId2ContextFilter;

@AutoConfiguration
public class ContextAutoConfiguration {

    @Bean
    public FilterRegistrationBean<HeaderUserId2ContextFilter> filterFilterRegistrationBean() {
        HeaderUserId2ContextFilter filter = new HeaderUserId2ContextFilter();
        return new FilterRegistrationBean<>(filter);
    }
}
