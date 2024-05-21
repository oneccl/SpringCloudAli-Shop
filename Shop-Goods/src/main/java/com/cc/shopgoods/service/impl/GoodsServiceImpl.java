package com.cc.shopgoods.service.impl;

import com.cc.shopcommon.pojo.Goods;
import com.cc.shopgoods.dao.GoodsDao;
import com.cc.shopgoods.service.GoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/9
 * Time: 19:36
 * Description:
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsDao goodsDao;

    @Override
    public Goods findById(Integer id) {
        return goodsDao.findById(id).get();
    }

}
