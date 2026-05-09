package org.example.Controller;

import org.example.common.JwtUtil;
import org.example.common.Result;
import org.example.common.BCryptUtil;
import org.example.DemoUser.Demouser;
import org.example.DemoUser.Employee;
import org.example.DemoUser.Department;
import org.example.Service.DemouserService;
import org.example.Service.SystemLogService;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class DemouserController {

    private final DemouserService userService;
    private final SystemLogService systemLogService;

    public DemouserController(DemouserService userService, SystemLogService systemLogService) {
        this.userService = userService;
        this.systemLogService = systemLogService;
    }

    // ====================== 登录（JWT） ======================
    @PostMapping("/login")
    public Result<?> login(@RequestBody Demouser user, HttpServletRequest request) {

        String username = user.getUsername();
        String password = user.getPassword();

        String res = userService.logins(username, password);
        if ("success".equals(res)) {
            Demouser loginUser = userService.login(username);
            // 确保角色不为null
            if (loginUser.getRole() == null) {
                loginUser.setRole("user");
            }
            String token = JwtUtil.createToken(loginUser);
            
            // 记录登录日志
            String ipAddress = getClientIpAddress(request);
            systemLogService.addLog(username, "用户登录", ipAddress);
            
            return Result.success(token);
        }
        return Result.error(res);
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    // ====================== 注册（任何人都可以注册） ======================
    @PostMapping("/register")
    public Result<?> register(@RequestBody Demouser user) {
        // 注册默认都是普通用户
        user.setRole("user");
        boolean save = userService.addEmp(user);
        return save ? Result.success("注册成功") : Result.error("用户名已存在");
    }
    
    // ====================== 重置密码（用于修复密码为空的问题） ======================
    @PostMapping("/resetPassword")
    public Result<?> resetPassword(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String newPassword = params.get("newPassword");
        
        if (username == null || username.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return Result.error("用户名和密码不能为空");
        }
        
        boolean success = userService.resetPassword(username, newPassword);
        return success ? Result.success("密码重置成功") : Result.error("用户不存在");
    }
    
    // ====================== 修改密码（普通员工修改自己的密码） ======================
    @PostMapping("/changePassword")
    public Result<?> changePassword(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        
        if (oldPassword == null || oldPassword.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return Result.error("参数不能为空");
        }
        
        // 验证原密码
        Demouser user = userService.login(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        if (!BCryptUtil.matches(oldPassword, user.getPassword())) {
            return Result.error("原密码错误");
        }
        
        // 修改密码
        boolean result = userService.resetPassword(username, newPassword);
        return result ? Result.success("密码修改成功") : Result.error("修改失败");
    }

    // ====================== 我的信息 ======================
    @GetMapping("/myInfo")
    public Result<?> myInfo(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        Demouser user = userService.login(username);
        Employee emp = userService.getEmployeeByUsername(username);

        Map<String, Object> myInfo = new HashMap<>();
        myInfo.put("user", user);
        myInfo.put("employee", emp);

        if (emp != null && emp.getDeptId() != null) {
            Department dept = userService.getDoorById(emp.getDeptId());
            myInfo.put("department", dept);
        }

        return Result.success(myInfo);
    }

    // ====================== 管理员校验 ======================
    private boolean isAdmin(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        return "admin".equals(role);
    }

    // ====================== 账号管理 ======================
    @GetMapping("/alldemouser")
    public Result<?> getAll(HttpServletRequest request) {
        if (!isAdmin(request)) return Result.forbidden();
        return Result.success(userService.getAlldemouser());
    }

    @GetMapping("/getUserById")
    public Result<?> getUserById(@RequestParam Integer id, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        Demouser currentUser = userService.login(username);

        if (isAdmin(request) || (currentUser != null && currentUser.getId().equals(id))) {
            return Result.success(userService.getEmpById(id));
        }
        return Result.forbidden();
    }

    @PostMapping("/adddemouser")
    public Result<?> addUser(@RequestBody Demouser newEmp, HttpServletRequest request) {
        if (!isAdmin(request)) return Result.forbidden();
        return userService.addEmp(newEmp) ? Result.success("添加成功") : Result.error("添加失败");
    }

    @PostMapping("/updatedemouser")
    public Result<?> updateUser(@RequestBody Demouser updateEmp, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        Demouser currentUser = userService.login(username);

        if (isAdmin(request) || (currentUser != null && currentUser.getId().equals(updateEmp.getId()))) {
            return userService.updateEmp(updateEmp) ? Result.success("修改成功") : Result.error("修改失败");
        }
        return Result.forbidden();
    }

    @GetMapping("/deletedemouser")
    public Result<?> deleteUser(@RequestParam Integer id, HttpServletRequest request) {
        if (!isAdmin(request)) return Result.forbidden();
        return userService.deleteEmp(id) ? Result.success("删除成功") : Result.error("删除失败");
    }

    // ====================== 员工管理 ======================
    @GetMapping("/allemployee")
    public Result<?> getAllEmployee(HttpServletRequest request) {
        if (!isAdmin(request)) return Result.forbidden();
        
        List<Employee> employees = userService.getAllEmp();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Employee emp : employees) {
            Map<String, Object> empMap = new HashMap<>();
            empMap.put("id", emp.getId());
            empMap.put("name", emp.getName());
            empMap.put("phone", emp.getPhone());
            empMap.put("idCard", emp.getIdCard());
            empMap.put("deptId", emp.getDeptId());
            empMap.put("username", emp.getUsername());
            empMap.put("salary", emp.getSalary());
            empMap.put("entryTime", emp.getEntryTime());
            empMap.put("status", emp.getStatus());
            
            // 添加部门名称：优先使用数据库中已保存的deptname，否则通过deptId查询
            if (emp.getDeptName() != null && !emp.getDeptName().isEmpty()) {
                empMap.put("deptName", emp.getDeptName());
            } else if (emp.getDeptId() != null) {
                Department dept = userService.getDoorById(emp.getDeptId());
                if (dept != null) {
                    empMap.put("deptName", dept.getDeptName());
                }
            }
            
            result.add(empMap);
        }
        
        return Result.success(result);
    }

    @GetMapping("/getEmployeeById")
    public Result<?> getEmployeeById(@RequestParam Integer id, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        Employee currentEmp = userService.getEmployeeByUsername(username);

        if (isAdmin(request) || (currentEmp != null && currentEmp.getId().equals(id))) {
            return Result.success(userService.getploById(id));
        }
        return Result.forbidden();
    }

    @PostMapping("/addemployee")
    public Result<?> addEmployee(@RequestBody Employee newPlo, HttpServletRequest request) {
        if (!isAdmin(request)) return Result.forbidden();
        return userService.addEmployee(newPlo) ? Result.success("添加成功") : Result.error("添加失败");
    }

    @PostMapping("/updateemployee")
    public Result<?> updateEmployee(@RequestBody Employee updatePlo, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        Employee currentEmp = userService.getEmployeeByUsername(username);

        if (isAdmin(request) || (currentEmp != null && currentEmp.getId().equals(updatePlo.getId()))) {
            return userService.updatePlo(updatePlo) ? Result.success("修改成功") : Result.error("修改失败");
        }
        return Result.forbidden();
    }

    @GetMapping("/deleteemployee")
    public Result<?> deleteEmployee(@RequestParam Integer id, HttpServletRequest request) {
        if (!isAdmin(request)) return Result.forbidden();
        return userService.deletePlo(id) ? Result.success("删除成功") : Result.error("删除失败");
    }

    // ====================== 部门管理 ======================
    @GetMapping("/alldepartment")
    public Result<?> getAllDepartment(HttpServletRequest request) {
        if (!isAdmin(request)) return Result.forbidden();
        return Result.success(userService.getAllDoor());
    }

    @GetMapping("/getDepartmentById")
    public Result<?> getDepartmentById(@RequestParam Integer id, HttpServletRequest request) {
        // 管理员可以查询所有部门
        if (!isAdmin(request)) return Result.forbidden();
        
        // 直接使用getAllDoor返回的数据，因为前端列表能正常显示
        List<Department> allDepts = userService.getAllDoor();
        
        // 遍历查找匹配的部门
        for (Department dept : allDepts) {
            // 确保ID不为空且类型匹配
            if (dept.getId() != null && id != null && dept.getId().intValue() == id.intValue()) {
                return Result.success(dept);
            }
        }
        
        // 如果没找到，返回空数据
        return Result.success(null);
    }

    @PostMapping("/adddepartment")
    public Result<?> addDepartment(@RequestBody Department newDoor, HttpServletRequest request) {
        if (!isAdmin(request)) return Result.forbidden();
        return userService.addDoor(newDoor) ? Result.success("添加成功") : Result.error("添加失败");
    }

    @PostMapping("/updatedepartment")
    public Result<?> updateDepartment(@RequestBody Department updateDoor, HttpServletRequest request) {
        if (!isAdmin(request)) return Result.forbidden();
        return userService.updateDoor(updateDoor) ? Result.success("修改成功") : Result.error("修改失败");
    }

    @GetMapping("/deletedepartment")
    public Result<?> deleteDepartment(@RequestParam Integer id, HttpServletRequest request) {
        if (!isAdmin(request)) return Result.forbidden();
        return userService.deleteDoor(id) ? Result.success("删除成功") : Result.error("删除失败");
    }
}