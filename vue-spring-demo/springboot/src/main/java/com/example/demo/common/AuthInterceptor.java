package com.example.demo.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.User;

import com.example.demo.exception.CustomException;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;
    /** token秘钥，请勿泄露，请勿随便修改 */
    public static final String SECRET = "123456";

    //拦截器本尊
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpServletRequest MyRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //取出请求中的token
        String token = MyRequest.getHeader("token");
        System.out.println(token);
        //如果token是空的，抛出自定义的异常 发送 code ="401" + msg ="未获取到token, 请重新登录" 给Springboot
        //自定义的异常是在exception中文件，就是一个消息盒子，状态码和错误消息
        if (StrUtil.isBlank(token)) {
            throw new CustomException("401", "未获取到token, 请重新登录");
        }


        //查询登陆的用户信息 ，注释在utils/tokenUtils里面
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT verify = jwtVerifier.verify(token);
        Integer userId = verify.getClaim("userId").asInt();
        User user = userMapper.selectById(userId);

         //如果利用请求头中的token查询出来的数据是空的，就抛出自定义的异常
        if (user == null) {
            throw new CustomException("401", "token不合法");
        }

        // 验证 token 。如果验证失败就抛出自定义的异常
        try {
            jwtVerifier.verify(token);
        } catch (Exception e) {
            throw new CustomException("401", "token不合法");
        }
        return true;
    }
}
