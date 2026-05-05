package org.example.Controller;

import org.example.common.JwtUtil;
import org.example.common.Result;
import org.example.DemoUser.Demouser;
import org.example.DemoUser.Employee;
import org.example.DemoUser.Department;
import org.example.Service.DemouserService;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class DemouserController {

    private final DemouserService userService;

    public DemouserController(DemouserService userService) {
        this.userService = userService;
    }

    // ====================== 登录（JWT） ======================
    @PostMapping("/login")
    public Result<?> login(@RequestBody Demouser user) {

        String username = user.getUsername();
        String password = user.getPassword();

        String res = userService.logins(username, password);
        if ("success".equals(res)) {
            Demouser loginUser = userService.login(username);
            String token = JwtUtil.createToken(loginUser);
            return Result.success(token);
        }
        return Result.error(res);
    }

    // ====================== 注册（任何人都可以注册） ======================
    @PostMapping("/register")
    public Result<?> register(@RequestBody Demouser user) {
        // 注册默认都是普通用户
        user.setRole("user");
        boolean save = userService.addEmp(user);
        return save ? Result.success("注册成功") : Result.error("用户名已存在");
    }

    // ====================== 我的信息 ======================
    @GetMapping("/myInfo")
    public Result<?> myInfo(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if ("admin".equals(role)) {
            return Result.success("管理员身份，请使用管理接口");
        }

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
        return Result.success(userService.getAllEmp());
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
        String username = (String) request.getAttribute("username");
        Employee currentEmp = userService.getEmployeeByUsername(username);

        if (isAdmin(request) || (currentEmp != null && currentEmp.getDeptId() != null && currentEmp.getDeptId().equals(id))) {
            return Result.success(userService.getDoorById(id));
        }
        return Result.forbidden();
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