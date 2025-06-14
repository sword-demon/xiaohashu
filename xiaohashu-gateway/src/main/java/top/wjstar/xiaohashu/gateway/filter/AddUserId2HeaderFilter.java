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
        log.info("=======================> TokenConvertFilter");

        // 用户 id
        Long userId = null;
        try {
            // 获取当前登录用户的 id
            userId = StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            // 若没有登录,则直接放行
            return chain.filter(exchange);
        }

        log.info("## 当前登录用户 id: {}", userId);

        Long finalUserId = userId;
        // 创建一个新的 ServerWebExchange 对象,用于修改当前请求
        ServerWebExchange newExchange = exchange.mutate()
                .request(builder -> builder.header(HEADER_USER_ID, String.valueOf(finalUserId))).build();

        // 将请求传递给过滤器中的下一个过滤器进行处理.没有对请求进行任何修改
        return chain.filter(newExchange);
    }
}
