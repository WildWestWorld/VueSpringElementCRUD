package com.example.demo;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {

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
				.withClaim("userId", 123)
				.withAudience("123")

				//JWT的sign（必要），Algorithm.HMAC256(xx)=利用算法生成sign,xx可以随便写，但是不能给知道
				.sign(Algorithm.HMAC256("123456"));


		System.out.println(token);
	}
	@Test
	void test() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String token = request.getHeader("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxMjMiLCJleHAiOjE2NDE3Mzk5MDcsInVzZXJJZCI6MTIzfQ.Wbcj4TUkmiw01-D7p-U3vBnn5cGgSt_99KEKL7ofgoY");

		JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("123456")).build();
		DecodedJWT verify = jwtVerifier.verify(token);
		Integer userId = verify.getClaim("userId").asInt();
		System.out.println(userId);
		Date expiresTime = verify.getExpiresAt();


		System.out.println(expiresTime);
	}
}
