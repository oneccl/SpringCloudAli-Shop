package com.cc.shopgoods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// 开启服务发现，注册到Spring-Cloud-Ali注册中心组件Nacos
@EnableDiscoveryClient
// @EntityScan: 当路径不一致时，需要扫描剥离实体所在的包
@EntityScan("com.cc.shopcommon")
@SpringBootApplication
public class ShopGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopGoodsApplication.class, args);
    }

}
