package top.wjstar.xiaohashu.gateway.auth;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token 权限验证 配置类
 */
@Configuration
@Slf4j
public class SaTokenConfigure {

    /**
     * 注册 Sa-Token 全局过滤器
     *
     * @return
     * @Order(-100) 优先级非常高 保证校验在前
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")    /* 拦截全部path */
                // 开放地址
                .addExclude("/favicon.ico")
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    log.info("======================> SaReactorFilter, Path: {}", SaHolder.getRequest().getRequestPath());
                    // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
                    SaRouter.match("/**")
                            .notMatch("/auth/user/login") // 排除登录接口
                            .notMatch("/auth/verification/code/send")// 排除验证码发送接口
                            .check(r -> StpUtil.checkLogin()); // 校验是否登录

                    SaRouter.match("/auth/user/logout", r -> StpUtil.checkRole("admin"));
                    // 权限认证 -- 不同模块, 校验不同权限
//                    SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
//                    SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
//                    SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
//                    SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));

                    // 更多匹配 ...  */
                })
                .setError(e -> {
                    if (e instanceof NotLoginException) {
                        throw new NotLoginException(e.getMessage(), null, null);
                    } else if (e instanceof NotPermissionException || e instanceof NotRoleException) {
                        // 权限不足或不具备角色
                        throw new NotPermissionException(e.getMessage());
                    } else {
                        // 其他异常,则抛出一个运行异常
                        throw new RuntimeException(e.getMessage());
                    }
                })
                ;
    }
}
