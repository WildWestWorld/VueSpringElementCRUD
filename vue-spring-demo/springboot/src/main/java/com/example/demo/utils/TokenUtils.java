package com.example.demo.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TokenUtils {


    @Resource
    private UserMapper userMapper;

    private static UserMapper staticUserMapper;

    /** token秘钥，请勿泄露，请勿随便修改 */
    public static final String SECRET = "123456";

    @PostConstruct
    public void init() {
        staticUserMapper = userMapper;
    }

    /**
     * 生成token
     * @param user
     * @return
     */
    public static String getToken(User user) {
        //创建Header的Map数据，alg=算法 typ=类型
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        //创建JWT的构造器
        JWTCreator.Builder builder = JWT.create();

        //withExpiresAt(x) = 设置token过期时间为 x
        //DateUtil.offsetHour(new Date(), 1)  创建一个时间，时间为此刻时间的一个小时后
        String token = builder.withExpiresAt(DateUtil.offsetHour(new Date(), 1))

                //JWT的Header（非必要），默认也会有的
                .withHeader(map)

                //JWT的payload（非必要），可以用于携带数据
                .withClaim("userId", user.getId())
                .withClaim("userName", user.getUsername())
                //JWT的sign（必要），Algorithm.HMAC256(xx)=利用算法生成sign,xx可以随便写，但是不能给知道
                .sign(Algorithm.HMAC256(SECRET));

        return token;
    }

    /**
     * 获取token中的用户信息
     * @return
     */
    public static User getUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            String token = request.getHeader("token");
//            String token    ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NDE3NDE2NDgsInVzZXJOYW1lIjoibGlqaWVrYWkxOTk4IiwidXNlcklkIjoxM30.cSWtmZUNpUBlMJ5xDs8VQX5Ei4R-W34f8t_Y8k1mRNs";
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            DecodedJWT verify = jwtVerifier.verify(token);
            Integer userId = verify.getClaim("userId").asInt();
            User userInfo = staticUserMapper.selectById(userId);
            Date expiresTime = verify.getExpiresAt();

            System.out.println(userId);
            System.out.println(expiresTime);
            System.out.println(token);
            return userInfo;

        } catch (Exception e) {
            log.error("解析token失败", e);
            return null;
        }
    }
}
