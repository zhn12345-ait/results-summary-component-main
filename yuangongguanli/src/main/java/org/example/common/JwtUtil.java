package org.example.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.DemoUser.Demouser;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "mySuperSecretKeyForJWTThatIsLongEnough1234567890";
    private static final long EXPIRE = 1000 * 60 * 60 * 24;

    public static String createToken(Demouser user) {
        return Jwts.builder() // 使用jwt构建器
                .setSubject(user.getUsername()) // 设置主题(就是登录的用户名)
                .claim("role", user.getRole()) //自定义存放数据（角色/权限)
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE)) // 设置过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET) // 设置签名使用的签名算法和秘钥
                .compact(); // 生成token字符串
    }
    
    // 解析token字符串，获取Claims对象
    public static Claims getClaims(String token) {
        try {
            return Jwts.parser() // 使用jwt解析器
                    .setSigningKey(SECRET) // 设置签名的秘钥，用来验证token是否被篡改
                    .parseClaimsJws(token) // 解析token字符串
                    .getBody(); // 从token中解析出Claims对象
        } catch (Exception e) {
            return null;
        }
    }
}