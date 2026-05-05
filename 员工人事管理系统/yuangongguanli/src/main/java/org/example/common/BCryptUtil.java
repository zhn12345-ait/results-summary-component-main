package org.example.common;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {
    // 加密
    public static String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    // 校验
    public static boolean matches(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}