package top.wjstar.xiaohashu.gateway.filter;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 转发请求时,将用户 id 添加到 Header 请求头中,透传给下游服务
 */
@Component
@Slf4j
public class AddUserId2HeaderFilter implements GlobalFilter {

    /**
     * 请求头中,用户 id 的键
     */
    private static final String HEADER_USER_ID = "userId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 记录进入 TokenConvertFilter 过滤器的日志
        log.info("=======================> TokenConvertFilter");

        // 声明一个变量用于存储用户的id，初始值为null
        Long userId = null;

        try {
            // 尝试获取当前登录用户的id，如果用户已登录，则userId会被赋值
            userId = StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            // 如果捕获到异常（例如用户未登录），则直接调用链中的下一个过滤器，放行当前请求
            return chain.filter(exchange);
        }

        // 记录当前登录用户的id
        log.info("## 当前登录用户 id: {}", userId);

        // 由于Java的lambda表达式不能直接使用局部变量，所以将userId赋值给finalUserId
        Long finalUserId = userId;

        // 创建一个新的ServerWebExchange对象，目的是为了修改当前请求，在请求头中添加用户的id信息
        ServerWebExchange newExchange = exchange.mutate()
                .request(builder -> builder.header(HEADER_USER_ID, String.valueOf(finalUserId))).build();

        // 使用新的ServerWebExchange对象调用过滤器链中的下一个过滤器，继续处理请求
        return chain.filter(newExchange);
    }

}
