package com.cc.shoporder.controller;

import com.alibaba.fastjson.JSON;
import com.cc.shopcommon.pojo.Goods;
import com.cc.shopcommon.pojo.Order;
import com.cc.shoporder.service.OrderService;
import com.cc.shoporder.service.ServiceGoods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/13
 * Time: 16:45
 * Description:
 */
// OrderController高并发下的压力测试
// 高并发带来的问题
/*
微服务架构中，服务与服务间相互调用，但由于网络延迟或服务自身性能原因，服务可能会出现问题
例如：模拟网络延时，如果有大量的线程(21个)涌入某个服务(设置最大支持10个)，就会形成任务堆积
导致请求阻塞(线程无法释放)，如果线程过多最终可能导致服务雪崩(服务间具有依赖性，故障可传播)
 */

//@RestController
@Slf4j
public class OrderControllerParallelJmeter {
    @Resource
    private OrderService orderService;
    @Resource
    private ServiceGoods serviceGoods;

    @RequestMapping("/save")
    public Object save(Integer gId) {
        log.info("用户下单，调用商品微服务获得商品信息以创建订单!");
        Goods goods = serviceGoods.findById(gId);

        // 模拟网络延时
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("商品微服务返回的商品信息: "+ JSON.toJSONString(goods));

        Order order = new Order(null,1,1000,"aa",gId,goods.getGoodsName(),goods.getPrice());
        return orderService.saveOrder(order);
    }

    // 访问测试
    @RequestMapping("/message")
    public Object message(){
        log.info("压力测试方法执行啦!");
        return "高并发下的压力测试!";
    }

    // 测试步骤：
    // 本地文件：E:\cloudSoftware\sentinel\Sentinel服务容错.md
    /*
    1.在yml文件中设置Tomcat的最大线程数为10(启动Goods服务和Order服务)
    2.使用并发压力测试工具Jmeter对请求进行压力测试
      1)下载Jmeter: https://jmeter.apache.org/
      本地：E:\cloudSoftware\sentinel\压力测试工具\apache-jmeter-5.3
      2)进入bin目录，修改jmeter.properties文件中的语言支持为language=zh_CN，然后点击jmeter.bat启动软件
      3)添加线程组：测试计划(右键)->添加/线程(用户)->线程组
      4)配置线程并发数：设置线程数20，循环次数100(其它默认)
      5)添加http取样：线程组(右键)->添加/取样器/http请求
      6)配置取样：填写协议、IP、端口、请求方式、路径(资源名+参数)
      7)配置结果树：http请求->添加/监听器/察看结果树
      8)点击启动按钮(绿三角)，并在浏览器访问一次/message资源
     */
    // 结论：20个线程并发+访问一次/message资源共21个线程访问Order服务
    // 由于save(资源名/save)方法堆积了大量请求，导致/message资源的访问大大延迟
    // 这是服务雪崩的雏形，为了防止服务雪崩的发生，就要做好服务的容错

}
