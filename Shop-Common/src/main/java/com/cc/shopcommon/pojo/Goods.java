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
 * Time: 19:10
 * Description:
 */

@Entity(name = "shop_goods")
@Table
//@Proxy(lazy = false)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {

    @Id
    @GeneratedValue
    private Integer goodsId;
    private String goodsName;
    private Double price; // 单价
    private Integer stock; // 库存

}
