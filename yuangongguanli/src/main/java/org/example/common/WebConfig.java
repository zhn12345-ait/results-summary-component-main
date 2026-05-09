package org.example.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 和上面 CorsConfig 里的代码完全一样
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                // 拦截的路径（支持通配符）
                .addPathPatterns("/user/**", "/api/**", "/attendance/**", "/leave/**")
                // 放行的路径（优先级高于拦截路径）
                .excludePathPatterns(
                        "/user/login",       // 登录
                        "/user/register",    // 新增：注册放行
                        "/user/resetPassword", // 新增：重置密码放行（用于修复密码为空的问题）
                        "/static/**",        // 新增：静态资源放行
                        "/user/refreshToken"  // 新增：token刷新放行
                );
    }
}