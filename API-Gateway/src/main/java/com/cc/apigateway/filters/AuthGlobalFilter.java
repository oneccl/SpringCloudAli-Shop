package com.cc.apigateway.filters;


import com.alibaba.nacos.common.utils.StringUtils;
import io.jsonwebtoken.*;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Author: CC
 * E-mail: 203717588@qq.com
 * Date: 2022/12/15
 * Time: 21:31
 * Description:
 */

// 鉴权逻辑
/*
1)当客户端第一次请求服务时，服务端对用户进行信息认证(登录)
2)认证通过，将用户的信息进行加密形成Token，返回给客户端，作为登录凭证
3)客户端收到Token，会将其存储到cookie或localStorage中，之后每次请求，客户端都会携带认证的Token
4)服务端对Token进行解密，并判断是否有效，是否过期
*/
// Access Token(令牌): 访问资源所需的凭证
/*
1)组成：uId(用户的唯一标识)+time(当前时间戳)+sign(签名：Token的前几位以hash算法压缩成一定长度的16进制字符串)
2)特点：
 ①客户端每次请求都要携带token，需要把token放到Http的Header中
 ②服务端不用存放token，用解析token的计算时间换取session的存储空间，从而减轻服务器的压力，减少频繁的查询数据库
 ③服务端无状态变化(多服务间共享)，可扩展性好；token完全由应用管理，可以避开同源策略，支持跨域调用；安全
3)无状态变化：所有信息都在token上，服务器不用保存token，但是服务器还需要认证token有效(服务器不知道其是否登录，只校验通行证)
4)同源策略：同源：指协议、域名/IP、端口相同；浏览器出于安全考虑，只允许本域名下的接口交互
  不同源(跨域)的资源访问需要对方授权
*/
// Jwt: Json Web Token: 是用于生成token的一种组件
/*
1)Jwt组成：标头(header).有效载荷(playLoad).签名(signature)
2)Jwt生成公式：Base64(Header).Base64(Payload).HMACSHA256(base64UrlEncode(header)+"."+base64UrlEncode(payload),secret)
3)使用：导入Jwt依赖，创建token生成工具类
*/
// 全局过滤器：作用于所有路由，无需在yml文件中配置
// 参考SpringCloud Gateway内置的全局过滤器：LoadBalanceClientFilter类（负载均衡）
// 自定义全局过滤器：完成统一的权限校验
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    // 鉴权
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if(!StringUtils.equals(token,"admin")){
            System.out.println("鉴权失败!");
            // HttpStatus.UNAUTHORIZED: 响应状态码401(没有访问权限)
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            // 拦截停止，执行立即执行请求返回
            return exchange.getResponse().setComplete();
        }

//        // 请求中的token是否有效
//        String uId = JWTUtil.getVal(token,"uId").toString();
//        if(StringUtils.isEmpty(uId)){
//            return fail(exchange.getResponse(),"token不合法!");
//        }
//        // 校验token是否过期
//        if(JWTUtil.isExpiration(token)){
//            return fail(exchange.getResponse(),"token已过期!");
//        }

        // 鉴权成功，放行继续访问
        return chain.filter(exchange);
    }

    // 优先级：越小优先级越高
    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> fail(ServerHttpResponse response, String message){
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type","application/json;charset=UTF-8");
        DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(buffer));
    }


    // Jwt-token生成工具类
    public static class JWTUtil{

        // jwt加解密类型：HS256算法（header部分，jjwt包已封装，直接调用）
        private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;

        // jwt生成密钥使用的密码(私钥)
        private static final String SECRET = "jwt";

        // jwt添加至http-header中的前缀
        private static final String SEPARATOR = "Bearer-";

        // 添加到PAYLOAD的签发者
        private static final String ISSUE = "xxx";

        // 添加至PAYLOAD的有效期(毫秒)
        private static final long TIMEOUT = 24*60*60*1000;

        // 登录成功后，token生成
        public static String generateToken(String uId, Integer day){
            // 创建PAYLOAD的私有声明（要放入token中的信息）
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("uId",uId);
            long currentTime = System.currentTimeMillis();
            return Jwts.builder()
                    .setId(UUID.randomUUID().toString()) // 唯一id
                    .setIssuedAt(new Date(currentTime)) // 签发时间
                    .setIssuer(ISSUE) // 签发人
                    .signWith(ALGORITHM,SECRET) // 加密算法，私钥
                    .setExpiration(new Date(currentTime + TIMEOUT * day)) // 过期时间(天)
                    .addClaims(claims)
                    .compact();
        }

        // 解析token，获取key对应的value
        public static Object getVal(String token, String key){
            return getClaimsBody(token).get(key);
        }

        // token是否过期
        public static boolean isExpiration(String token){
            try{
                return getClaimsBody(token).getExpiration().before(new Date());
            }catch (ExpiredJwtException e){
                return true;
            }
        }

        // Token解密
        // 参数token 加密后的token
        // 返回：解密后的token
        public static Claims getClaimsBody(String token){
            return Jwts.parser()  // JWT解析器
                    .setSigningKey(SECRET)  // 秘钥
                    .parseClaimsJws(token).getBody();
        }

    }

}
