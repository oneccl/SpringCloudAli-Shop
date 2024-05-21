package com.cc.apigateway.filters;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/15
 * Time: 11:42
 * Description:
 */
// 自定义局部过滤器工厂(控制日志是否开启)
@Component
// 参照内置的响应状态码工厂：SetStatusGatewayFilterFactory类
// 类名格式需要固定
public class LogGatewayFilterFactory extends AbstractGatewayFilterFactory<LogGatewayFilterFactory.Config> {

    // 加载内部配置类Config
    public LogGatewayFilterFactory(){
        super(LogGatewayFilterFactory.Config.class);
    }

    // 重写shortcutFieldOrder()方法：用于从配置文件中获取参数并赋值给Config内部配置类属性
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("consoleLog","cacheLog");
    }

    // 重写apply(Config config)方法：用于编写log是否开启逻辑
    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                if(config.consoleLog){
                    System.out.println("console缓存已开启!");
                }
                if(config.cacheLog){
                    System.out.println("cache缓存已开启!");
                    System.out.println("请求参数: "+exchange.getRequest().getQueryParams());
                }
                // 过滤后请求或响应继续传递
                return chain.filter(exchange);
            }
        };
    }

    // 内部配置类：用于接收yml配置文件过滤器中定义的Log的参数
    // 必须要有空参构造和get/set方法
    @Data
    @NoArgsConstructor
    public static class Config {
        @NotNull
        private Boolean consoleLog;
        @NotNull
        private Boolean cacheLog;
    }

}
