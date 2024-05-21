package com.cc.shopuser.service.impl;

import com.cc.shopcommon.pojo.User;
import com.cc.shopuser.dao.UserDao;
import com.cc.shopuser.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/9
 * Time: 19:16
 * Description:
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public List<User> queryAll() {
        return userDao.findAll();
    }

}
