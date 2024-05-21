package com.cc.apigateway;


/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/15
 * Time: 9:58
 * Description:
 */
public class DemoGateway {
    // Gateway网关
    /*
    1、API网关：系统的统一访问接口，它封装了应用程序的内部结构，为客户端提供认证、鉴权、监控、路由转发等统一服务
    2、常见的网关：
     1）Nginx:使用Nginx反向代理、负载均衡和lua脚本语言实现网关
     2）Zuul:Netflix开源的网关，组件较多，无法动态配置，处理http请求依赖Web容器，性能不如Nginx
     3）Spring Cloud Gateway:Spring为代替Zuul开发的网关服务
     Spring Cloud Ali技术栈没有提供自己的网关，因此使用Spring cloud的Gateway网关
    3、Gateway优缺点
     1）优点：性能强大：是第一代网关Zuul的1.6倍；功能强大：内置了限流、监控等功能，结合nacos可实现负载均衡
     2）缺点：不能将其部署在Tomcat、Jetty等Servlet容器，只能打成jar包执行
    */
    // Gateway的核心架构
    /*
    1、路由(Route)：一个具体的信息载体(Path路径)，路由参数：
     1）id: 路由的唯一标识
     2）uri: 路由指向的地址(协议+IP+Port)
     3）order: 路由的优先级，越小优先级越高
     4）predicates: 断言：条件判断，满足条件进行路由
     5）filters: 过滤器：用于路由前修改请求和响应信息
    2、断言(predicate)：在什么条件下进行路由转发
     1）Gateway内置的路由断言工厂（部分）
     (1)日期时间：AfterRoutePredicateFactory、BeforeRoutePredicateFactory
     例：- After=2022-12-10T00:00:00.000+08:00
     (2)请求方式：MethodRoutePredicateFactory
     例：- Method=GET
     (3)路径：PathRoutePredicateFactory
     例：- Path=/OrderService/**
     (4)路由权重：WeightRoutePredicateFactory
     例：- Weight=组名，权重
     2）自定义断言工厂（/predicates/）
    3、过滤器(filter)：请求传递过程中，对请求和响应信息进行修
     1）分类：局部过滤器(作用在某一个路由上)、全局过滤器(作用全部路由上)
     2）生命周期：pre和post
     (1)pre：在请求被路由之前调用；可以利用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息等
     (2)post：在路由到微服务之后执行；可用来为响应添加标准的Http Header、收集统计信息和指标、将响应从微服务发送给客户端
     3）Gateway内置的局部过滤器（部分）
     (1)截掉请求路径前几层：StripPrefixGatewayFilterFactory
     例：- StripPrefix=1
     (2)修改响应状态码：SetStatusGatewayFilterFactory
     例：- SetStatus=2000
     3）自定义局部和全局过滤工厂（/filters/）
    */

}
