package com.cc.shoporder.controller;

import com.alibaba.fastjson.JSON;
import com.cc.shopcommon.pojo.Goods;
import com.cc.shopcommon.pojo.Order;
import com.cc.shoporder.service.ServiceGoods;
import com.cc.shoporder.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/9
 * Time: 20:17
 * Description:
 */
@RestController
// lombok提供的日志打印
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;
    // 服务调用对象，需要在启动类中将RestTemplate对象注入Spring容器
    // RestTemplate是Spring框架提供的基于REST的服务组件
    @Resource
    private RestTemplate restTemplate;
    // 服务发现对象，自定义实现负载均衡
    @Resource
    private DiscoveryClient discoveryClient;
    // Feign：注入调用的提供/生产者的service接口
    @Resource
    private ServiceGoods serviceGoods;

    @RequestMapping("/save")
    public Object save(Integer gId) {
        // 1、直接拼接url

        // restTemplate调用Shop-Goods服务获取商品信息
//        Goods goods = restTemplate.getForObject("http://localhost:8859/getGoodsById/" + gId, Goods.class);

        // 2、动态拼接url：自定义负载均衡：随机

//        // 拉取Service-Goods服务列表
//        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("Service-Goods");
//        // 随机获取一个Service-Goods服务实例
//        ServiceInstance serviceInstance = serviceInstances.get(new Random().nextInt(serviceInstances.size()));
//        // uri: 协议+IP/主机+端口
//        String uri = serviceInstance.getUri().toString();
//        // 端口分配日志打印
//        log.info("Service-Goods服务URI: "+uri);
//        Goods goods = restTemplate.getForObject(uri+"/getGoodsById/"+gId, Goods.class);
//        // 商品日志打印
//        log.info("商品信息: "+ JSON.toJSONString(goods));

        // 3、nacos.ribbon(cloud ali 2.2.x版本)负载均衡：默认轮循
        // 如果只有一个服务，可不用在启动类的RestTemplate对象注入方法上添加注解@LoadBalanced

//        Goods goods = restTemplate.getForObject("http://Service-Goods/getGoodsById/" + gId, Goods.class);
//        log.info("商品信息: "+ JSON.toJSONString(goods));

        // 4、基于Feign(2.2.x版本集成了Ribbon)的远程服务调用(步骤)
        // 1)添加Feign依赖，在本服务启动类上使用注解@EnableFeignClients开启服务调用Feign
        // 2)在本服务中创建一个提供/生产者service的接口GoodsService
        // 3)在生产者service接口上使用@FeignClients("serviceID")声明生产者的serviceID
        //   并使用请求映射注解指定调用提供/生产者的哪个方法，拼接成一个完整的路径
        // 4)在本服务注入生产者service接口，实现像调用本地一样调用远程服务

        Goods goods = serviceGoods.findById(gId);
        log.info("商品信息: "+ JSON.toJSONString(goods));

        Order order = new Order(null,1,1000,"aa",gId,goods.getGoodsName(),goods.getPrice());
        return orderService.saveOrder(order);
    }

}
