package org.example;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin  // 跨域
@RestController // 表示这是一个控制器
@RequestMapping("/admin") // 设置访问的路径
public class AdminController {
    
    @Resource // 自动注入依赖
    private AdminMapper adminMapper; // 自动注入UserService依赖
    @PostMapping("/login")
    public Map<String, Object> login(String userName, String passWord) {
        Map<String, Object> result = new HashMap<>();

        Admin user = adminMapper.selectOne(
                new QueryWrapper<Admin>().eq("user_name", userName)
        );

        // 校验账号密码
        if (user == null || !user.getPassWord().equals(passWord)) {
            result.put("code", 400);
            result.put("msg", "用户名或密码错误");
            return result;
        }

        // 登录成功，返回用户信息（包括角色）
        result.put("code", 200);
        result.put("msg", "登录成功");
        result.put("userId", user.getId());
        result.put("username", user.getUserName());
        result.put("role", user.getRole()); // 关键：把角色返回给前端
        return result;
    }

    // 用户注册（自动成为普通用户）
    @PostMapping("/register")
    public Map<String, Object> register(String userName, String passWord) {
        Map<String, Object> result = new HashMap<>();

        if (userName == null || passWord == null || userName.isEmpty() || passWord.isEmpty()) {
            result.put("code", 400);
            result.put("msg", "用户名或密码不能为空");
            return result;
        }

        // 判断用户名是否已存在
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        Admin exist = adminMapper.selectOne(queryWrapper);

        if (exist != null) {
            result.put("code", 400);
            result.put("msg", "用户名已存在");
            return result;
        }

        // 新建用户，角色默认为普通用户 user
        Admin user = new Admin();
        user.setUserName(userName);
        user.setPassWord(passWord);
        user.setRole("user"); // 普通用户

        adminMapper.insert(user);

        result.put("code", 200);
        result.put("msg", "注册成功，请登录！");
        return result;
    }

    @PostMapping("/changePwd")
    public Map<String, Object> changePwd(String userName, String oldPwd, String newPwd) {
        Map<String, Object> result = new HashMap<>();
        Admin user = adminMapper.selectOne(new QueryWrapper<Admin>().eq("user_name", userName));
        if (user == null || !user.getPassWord().equals(oldPwd)) {
            result.put("code", 400);
            result.put("msg", "原密码错误");
            return result;
        }
        user.setPassWord(newPwd);
        adminMapper.updateById(user);
        result.put("code", 200);
        result.put("msg", "密码修改成功");
        return result;
    }

}
