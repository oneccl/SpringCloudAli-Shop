package com.cc.apigateway.predicates;

import com.alibaba.nacos.common.utils.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/15
 * Time: 10:35
 * Description:
 */
// 自定义路由断言工厂(限定年龄)
@Component
// 参照内置的时间断言工厂：AfterRoutePredicateFactory类
// 类名格式需要固定
public class AgeRoutePredicateFactory extends AbstractRoutePredicateFactory<AgeRoutePredicateFactory.Config> {

    // 加载内部配置类Config
    public AgeRoutePredicateFactory() {
        super(AgeRoutePredicateFactory.Config.class);
    }

    // 重写shortcutFieldOrder()方法：用于从配置文件中获取参数并赋值给Config内部配置类属性
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("minAge","maxAge");
    }

    // 重写apply(Config config)方法：用于编写Age断言逻辑
    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        // ServerWebExchange接口: 服务网络交换器，是一个http请求-响应交互的契约
        // 存放着请求和响应参数、请求实例和响应实例等，公开给额外的服务器对http请求和响应进行处理
        return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                // 获取请求路径中参数名为age的值
                String ageStr = serverWebExchange.getRequest().getQueryParams().getFirst("age");
                // ageStr!=null && ageStr!=""
                if (StringUtils.isNotEmpty(ageStr)){
                    int age = Integer.parseInt(ageStr);
                    return age>=config.minAge && age<=config.maxAge;
                }
                return false;
            }
        };
    }

    // 内部配置类：用于接收yml配置文件路由断言中定义的Age的参数
    // 必须要有空参构造和get/set方法
    @Data
    @NoArgsConstructor
    public static class Config {
        @NotNull
        private Integer minAge;
        @NotNull
        private Integer maxAge;
    }

}
