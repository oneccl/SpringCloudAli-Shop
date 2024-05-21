package com.cc.shopuser.controller;

import com.cc.shopuser.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/9
 * Time: 19:18
 * Description:
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/getAll")
    public Object getAll(){
        return userService.queryAll();
    }

}
