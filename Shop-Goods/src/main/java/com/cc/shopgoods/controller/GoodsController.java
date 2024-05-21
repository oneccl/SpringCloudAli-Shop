package com.cc.shopgoods.controller;

import com.cc.shopgoods.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/9
 * Time: 19:39
 * Description:
 */
@RestController
// lombok提供的日志打印
@Slf4j
public class GoodsController {

    @Resource
    private GoodsService goodsService;
    // 当前服务的端口
    @Value("${server.port}")
    Integer port;

    @RequestMapping("/getGoodsById/{gid}")
    // "/getById/{gid}": {gid}相当于占位符
    // @PathVariable("gid"): 用于匹配{gid}，两个值必须一致，传递进来的值将赋给方法形参
    // 前台访问方式如：http://localhost:8859/getGoodsById/10
    public Object getGoodsById(@PathVariable("gid") Integer id){
        // JSON.toJSONString(Object o): 将Java对象转换为Json格式的字符串
        // 日志打印：服务端口
        log.info("Service-Goods服务端口: "+port);
        return goodsService.findById(id);
    }


    // 网关限流（测试）
    // 1、route维度（对某一路由限流，对controller下所有方法统一限流）
    //    限流策略：设置1秒最多只能访问1次（/flowLimiter/GatewayRouteFlowRule.java）
    // 2、自定义API维度（对controller下方法(api)使用不同限流规则）（/flowLimiter/GatewayApiFlowRule.java）

    // api1限流策略：设置1秒最多只能访问1次
    @RequestMapping("/goods/api1/m1")
    public Object m1(){
        return "api1的方法1!";
    }
    @RequestMapping("/goods/api1/m2")
    public Object m2(){
        return "api1的方法2!";
    }
    // api2限流策略：设置1秒最多只能访问3次
    @RequestMapping("/goods/api2/m1")
    public Object m3(){
        return "api2的方法1!";
    }
    @RequestMapping("/goods/api2/m2")
    public Object m4(){
        return "api2的方法2!";
    }

}
