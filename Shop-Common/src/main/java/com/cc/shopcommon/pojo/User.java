package com.cc.shopcommon.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/9
 * Time: 18:52
 * Description:
 */

// name: 给表起名(默认类名小写)
@Entity(name = "shop_user")
@Table
// lazy: 延迟加载(懒加载)，默认true；false：立即加载
// 目前不需要的东西不加载到内存中，当要用的时候才加载到内存中
// 可以解决Json格式响应到浏览器加载显示问题
//@Proxy(lazy = false)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Integer userId;
    private String userName;
    private String password;
    private String phone;
    private String address;

}
