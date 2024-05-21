package com.cc.shoporder.service;

import com.cc.shopcommon.pojo.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/12
 * Time: 9:54
 * Description:
 */
// 申明调用提供/生产者的serviceID
// Feign(2.2.x版本)集成了负载均衡Ribbon，使得调用远程服务就像调用本地服务
@FeignClient("Service-Goods")
public interface ServiceGoods {

    // 指定调用提供/生产者的哪个方法
    // @FeignClients+@RequestMapping就拼成了一个完整路径:
    // http://Service-Goods/getGoodsById/{gId}
    @RequestMapping("/getGoodsById/{gId}")
    Goods findById(@PathVariable("gId") Integer id);

}
