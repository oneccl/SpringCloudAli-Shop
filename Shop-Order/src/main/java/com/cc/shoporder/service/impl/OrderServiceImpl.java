package com.cc.shoporder.service.impl;

import com.cc.shopcommon.pojo.Order;
import com.cc.shoporder.dao.OrderDao;
import com.cc.shoporder.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/9
 * Time: 20:13
 * Description:
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Override
    public Order saveOrder(Order order) {
        return orderDao.save(order);
    }

}
