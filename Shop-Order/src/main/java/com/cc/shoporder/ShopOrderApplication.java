package com.cc.shoporder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

// 开启服务调用组件Feign
@EnableFeignClients
// 开启服务发现，注册到Spring-Cloud-Ali注册中心组件Nacos
@EnableDiscoveryClient
// @EntityScan: 当路径不一致时，需要扫描剥离实体所在的包
@EntityScan("com.cc.shopcommon")
@SpringBootApplication
public class ShopOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopOrderApplication.class, args);
    }

    // 将RestTemplate对象注入Spring容器
    @Bean
    // 开启nacos.ribbon(cloud ali 2.2.x版本)负载均衡(默认轮循)
    // 可在yml文件中配置其它负载均衡策略(cloud ali 2.2.x版本)
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
