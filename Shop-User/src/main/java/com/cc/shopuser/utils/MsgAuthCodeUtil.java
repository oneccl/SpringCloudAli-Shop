package com.cc.shopuser.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.models.*;


/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2023/2/2
 * Time: 20:08
 * Description:
 */

// 阿里云：手机短信验证码功能
/*
1、注册阿里云账号，并完成实名认证；找到短信服务功能，开通短信服务
2、申请签名和模板（不支持个人申请）
3、查看Secret，获取个人AccessKeyID与AccessKeySecret
4、添加官方提供的Maven SDK依赖到pom.xml文件中
5、创建一个类，复制官方提供的代码，进行修改
*/
public class MsgAuthCodeUtil {

    // 自己账号的AccessKeyId
    private static final String ACCESS_KEY_ID = "";
    // 自己账号的AccessKeySecret
    private static final String ACCESS_KEY_SECRET = "";
    // 自己账号的短信签名名称
    private static final String SIGN_NAME = "";
    // 自己账号的短信模板ID
    private static final String TEMPLATE_CODE = "";

    public static void sent(String tel) throws Exception {
        // 随机生成6位验证码
        String validateCode = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        // 配置自己的AccessKeyID和AccessKeySecret
        Config config = new Config()
                .setAccessKeyId(ACCESS_KEY_ID)
                .setAccessKeySecret(ACCESS_KEY_SECRET);
        // 访问的域名：阿里云短信服务器
        config.endpoint = "dysmsapi.aliyuncs.com";
        // 获取客户端对象
        Client client = new Client(config);
        // 给指定手机发送验证码
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(SIGN_NAME)
                .setTemplateCode(TEMPLATE_CODE)
                .setPhoneNumbers(tel)
                .setTemplateParam("{\"code\":"+validateCode+"}");
        // 访问API的返回值(模板短信内容)
        client.sendSms(sendSmsRequest);
    }

}
