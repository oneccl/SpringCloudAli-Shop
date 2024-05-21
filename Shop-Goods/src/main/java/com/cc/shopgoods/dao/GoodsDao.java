package com.cc.shopgoods.dao;

import com.cc.shopcommon.pojo.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/9
 * Time: 19:34
 * Description:
 */
@Repository
public interface GoodsDao extends JpaRepository<Goods,Integer> {

}
