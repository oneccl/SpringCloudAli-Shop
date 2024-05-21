package com.cc.shoporder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/13
 * Time: 19:27
 * Description:
 */
// Sentinel服务容错
/*
我们没法预防雪崩效应的发生，只能尽可能去做好容错；服务容错的3个核心思想是：
1、不被服务外界环境影响(服务器、CPU、内存等环境)
2、不被上游服务请求压垮(当前服务被上游服务多线程并发请求时)
3、不被下游服务响应拖垮(当前服务调用下游服务时发现其异常/故障)
 */
// 常见的服务容错方案
/*
1、隔离：指将服务按照一定原则划分为若干个子服务模块，各个模块间相对独立，无强依赖；当有故障发生时
  能将问题和影响隔离在某个子服务模块内部，而不扩散风险，不波及其它子服务模块，不影响系统其它服务
  常见的隔离方式有：线程池隔离和信号量隔离
2、超时：在上游服务调用下游服务的时候，设置一个最大响应时间，如果超过这个时间，下游未作出响应
  就断开与下游服务的请求，释放线程(直接返回)
3、限流：给服务的输入流量(如请求线程)和输出流量设置阈值，保证服务总是执行<=阈值的请求或响应
4、熔断：当下游服务因访问压力过大而响应变慢或失败时，上游服务为了保护系统整体的可用性，可以暂时切断对下游服务的调用
5、降级：为服务提供一个备用方案，一旦服务无法正常调用，就使用备用方案
 */
// Sentinel(分布式系统的流量防卫兵)
/*
1、是阿里开源的一款断路器，是一套用于服务容错的综合性解决方案组件；功能比Netflix Hystrix强大
  1)相同点：原则一致：当一个资源出现问题时，让其快速失败，不要波及到其它服务
  2)区别：容错方案/策略不同：
   Hystrix(隔离): 采用线程池隔离的方式，优点是做到了资源之间的隔离，缺点是增加了线程切换的成本
   Sentinel(限流、熔断降级、超时): 采用通过并发线程的数量和响应时间来对资源进行限制，同时提供系统维度的自适应保护能力
2、主要分为两部分：核心库(Java客户端依赖)和控制台(Dashboard，基于Spring Boot开发，不需要Tomcat)
3、Sentinel支持的容错规则：流控(限流)规则、降级(熔断)规则、系统规则、热点规则、授权规则
4、概念/术语：资源:Sentinel要保护的服务或方法或代码块 规则:定义保护资源的容错方案/策略
*/
// Sentinel的使用
/*
1、安装Sentinel控制台
  1)下载控制台jar包(Spring Boot项目)：https://github.com/alibaba/Sentinel/releases
  2)启动控制台：E:\cloudSoftware\sentinel\控制台Dashboard\dashboard-startup.bat
2、导入Spring-Cloud-Ali服务容错组件依赖(Sentinel的Java客户端)
3、在yml文件中配置Sentinel控制台通信端口和Sentinel控制台服务注册地址
4、修改日志生成路径(可选项)：使用WinRAR打开sentinel-dashboard-1.7.0.jar，修改BOOT-INF/classes/application.properties
5、运行Service-Order服务(8869)，访问/message资源(懒加载：当访问服务资源时，该服务会注册到控制台)，刷新控制台
*/
// 流控(限流策略/方案，开口向左的漏斗)
/*
1.控制台->簇点链路->给资源/message添加流控规则
2.阈值类型：
  QPS(Query Pre Second):单台服务器每秒响应的查询次数(数据库每秒执行的查询次数)，达到阈值进行限流
  线程数：当调用该接口的线程数达到阈值时，进行限流
3.例如使用QFS：设置单机阈值(设置阈值)为2
*/
// 降级(熔断策略/方案)
/*
1.控制台->簇点链路->给资源/message添加降级规则
2.降级策略：RT:响应时间(超过设置时间降级) 异常比例:响应失败数/请求数(达到则降级) 异常数:规定时间窗口(s)内出现的异常次数
3.例如使用异常比例策略：设置异常比例为0.5
4.高级选项(可选)：流控模式：直接:单一资源/方法 关联:单一资源/方法调用了一个其它方法 链路:单一资源链路调用了多个方法
*/

//@RestController
@Slf4j
public class OrderControllerSentinel {

    @RequestMapping("/message")
    public Object message(){
        return "message!";
    }

}
