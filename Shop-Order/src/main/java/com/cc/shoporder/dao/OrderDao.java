package com.cc.shoporder.dao;

import com.cc.shopcommon.pojo.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/9
 * Time: 20:09
 * Description:
 */
@Repository
public interface OrderDao extends JpaRepository<Order,Integer> {

}
