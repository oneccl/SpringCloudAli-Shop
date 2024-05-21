package com.cc.apigateway.flowLimiter;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/16
 * Time: 13:06
 * Description:
 */
// 网关限流
// Sentinel提供了两种资源维度的限流
// 方式1：route维度：在yml配置文件中配置路由，资源名为对应的routeID
// 方式2：自定义API维度：根据Sentinel提供的API自定义API分组
// 使用：
// 1）导入sentinel-spring-cloud-gateway-adapter依赖
// 2）编写配置类：基于Sentinel的Gateway限流是通过其提供的Filter来完成的，使用时需要注入
//   SentinelGatewayFilter实例及SentinelGatewayBlockExceptionHandler实例

// 1、route维度（对某一路由限流，对controller下所有方法统一限流）
//@Configuration
public class GatewayRouteFlowRule {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public GatewayRouteFlowRule(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    // 注入SentinelGatewayFilter实例
    // 初始化一个限流的过滤器
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    // 配置初始化的限流参数
    @PostConstruct
    public void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        // Service-Goods服务路由ID:r-GoodsService
        // 设置1秒最多只能访问1次；测试：1秒访问多次
        rules.add(new GatewayFlowRule("r-GoodsService") // 资源名称,对应routeID
                .setCount(1) // 限流阈值
                .setIntervalSec(1) // 统计时间窗口,单位秒,默认是1秒
        );
        GatewayRuleManager.loadRules(rules);
    }

    // 注入SentinelGatewayBlockExceptionHandler实例
    // 配置限流的异常处理器
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    // 自定义限流异常响应页面
    @PostConstruct
    public void initBlockHandlers() {
        BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                Map<String,Object> map = new HashMap<>();
                // 响应码504：网关超时
                map.put("code", 504);
                map.put("message", "服务器繁忙!请稍后重试!");
                // HttpStatus.GATEWAY_TIMEOUT：504，需要和map.put("code", 504)中一致
                return ServerResponse.status(HttpStatus.GATEWAY_TIMEOUT).
                        contentType(MediaType.APPLICATION_JSON_UTF8).
                        body(BodyInserters.fromObject(map));
            }
        };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

}
