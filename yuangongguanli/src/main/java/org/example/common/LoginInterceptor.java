package org.example.common;

import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override //@Override重写方法，表示此方法重写了父类的方法
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // ====================== 【关键修复】放行 OPTIONS 跨域预检 ======================
        String method = request.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true; // 直接放行，不做任何校验
        }

        String token = request.getHeader("token");

        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(Result.unauthorized().toString());
            return false;
        }

        Claims claims = JwtUtil.getClaims(token);
        if (claims == null) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(Result.unauthorized().toString());
            return false;
        }

        request.setAttribute("username", claims.getSubject());
        request.setAttribute("role", claims.get("role"));
        return true;
    }
}