package com.cc.shopcommon.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/9
 * Time: 19:03
 * Description:
 */

@Entity(name = "shop_order")
@Table
//@Proxy(lazy = false)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue
    private Integer orderId; // 订单号
    private Integer num; // 数量

    // 订单的用户信息
    private Integer userId;
    private String userName;

    // 订单的商品信息
    private Integer goodsId;
    private String goodsName;
    private Double price; // 单价


}
