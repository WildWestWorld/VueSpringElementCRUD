package com.example.demo.common.config;

import com.example.demo.common.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //注册拦截器
        registry.addInterceptor(authInterceptor())

                /**
                 - /**： 匹配所有路径
                 - /admin/**：匹配 /admin/ 下的所有路径
                 - /secure/*：只匹配 /secure/user，不匹配 /secure/user/info
                 */
                // addPathPatterns 用于添加拦截规则 ，addPathPatterns("/**")=拦截所有接口路径
                .addPathPatterns("/**")

                //excludePathPatterns排除拦截 ，让拦截器不再拦截这些接口
                .excludePathPatterns("/user/login", "/user/register", "/imserver/**", "/files/**", "/alipay/**",
                        "/doc.html", "/webjars/**", "/swagger-resources/**");

    }


    //配置拦截器访问静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //获取系统的名字，如window系统
        String os = System.getProperty("os.name");
        //将名字变成小写，如果是win 开头的 ，那就是window ，然后用配置window的方法配置windows
        if (os.toLowerCase().startsWith("win")){  //如果是Windows系统
            //配置拦截器访问静态资源
            //addResourceHandler("页面路径xx")用于配置页面中的路径，你访问"http://localhost:9090/页面路径xx"时就会可以访问到你addResourceLocations配置的本地文件夹的位置
            registry.addResourceHandler("doc.html")

//                    .addResourceLocations = 你具体的文件存放的位置
//                    .addResourceLocations("file:G:/itemsource/smallapple/")

//        classpath :
//        1.  src 路径下的文件 在编译后都会放到 WEB-INF/classes 路径下。默认classpath 就是指这里。
//
//        2. 用maven构建 项目时，resources 目录就是默认的classpath
                    .addResourceLocations("classpath:/META-INF/resources/");

            registry.addResourceHandler("/favicon.ico")
                    .addResourceLocations("classpath:/META-INF/resources/");

            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");

        }else {  //linux 和mac
            registry.addResourceHandler("doc.html")
//                    .addResourceLocations("file:/resources/smallapple/") //Linux系统中资源的位置
                    .addResourceLocations("classpath:/META-INF/resources/");
        }


    }

    //引入拦截器
    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }
}
