package com.cc.shopuser.dao;

import com.cc.shopcommon.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/9
 * Time: 19:14
 * Description:
 */
@Repository
public interface UserDao extends JpaRepository<User,Integer> {

}
