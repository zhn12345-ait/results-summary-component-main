package org.example.Service;

import org.example.DemoUser.Demouser;
import org.example.DemoUser.Employee;
import org.example.DemoUser.Department;
import org.example.common.BCryptUtil;
import org.example.Mapper.DepartmentMapper;
import org.example.Mapper.EmployeeMapper;
import org.example.Mapper.DemouserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class) // 出现异常就回滚
public class DemouserService {

    @Autowired
    private DemouserMapper userMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    // ====================== 登录 ======================
    public String logins(String username, String password) {
        Demouser user = userMapper.login(username);
        if (Objects.isNull(user)) {
            return "用户名不存在";
        }
        // 检查密码是否为空
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "用户密码未设置";
        }
        if (!BCryptUtil.matches(password, user.getPassword())) {
            return "密码错误";
        }
        return "success";
    }

    public boolean addEmp(Demouser newEmp) {
        // 检查用户名是否已存在
        Demouser existingUser = userMapper.login(newEmp.getUsername());
        if (existingUser != null) {
            return false; // 用户名已存在
        }
        // 加密！
        String encoded = BCryptUtil.encode(newEmp.getPassword());
        newEmp.setPassword(encoded);
        return userMapper.insertEmp(newEmp) > 0;
    }
    
    // 重置密码
    public boolean resetPassword(String username, String newPassword) {
        Demouser existingUser = userMapper.login(username);
        if (existingUser == null) {
            return false; // 用户不存在
        }
        // 加密新密码
        String encoded = BCryptUtil.encode(newPassword);
        return userMapper.updatePassword(username, encoded) > 0;
    }

    public Demouser login(String username) {
        return userMapper.login(username);
    }

    // ====================== 账号管理 ======================
    public List<Demouser> getAlldemouser() {
        return userMapper.getAlldemouser(new HashMap<>());
    }


    public Demouser getEmpById(Integer id) {
        List<Demouser> emList = userMapper.getAlldemouser(null);
        for (Demouser emp : emList) {
            if (emp.getId().equals(id)) {
                return emp;
            }
        }
        return null;
    }

    public boolean updateEmp(Demouser updateEmp) {
        int rows = userMapper.updateEmp(updateEmp);
        return rows > 0;
    }

    public boolean deleteEmp(Integer id) {
        int rows = userMapper.deleteEmp(id);
        return rows > 0;
    }

    // ====================== 员工信息 ======================
    public List<Employee> getAllEmp() {
        return employeeMapper.getAllEmployee(null);
    }

    public boolean addEmployee(Employee newPlo) {
        int count = employeeMapper.addEmployee(newPlo);
        return count > 0;
    }

    public Employee getploById(Integer id) {
        List<Employee> empList = employeeMapper.getAllEmployee(null);
        for (Employee emp : empList) {
            if (emp.getId().equals(id)) {
                return emp;
            }
        }
        return null;
    }

    public boolean updatePlo(Employee updatePlo) {
        int rows = employeeMapper.updateEmployee(updatePlo);
        return rows > 0;
    }

    public boolean deletePlo(Integer id) {
        int rows = employeeMapper.deleteEmployee(id);
        return rows > 0;
    }

    public Employee getEmployeeByUsername(String username) {
        return employeeMapper.getEmployeeByUsername(username);
    }

    // ====================== 部门管理 ======================
    public List<Department> getAllDoor() {
        return departmentMapper.getAllDoor(null);
    }

    public boolean addDoor(Department newDoor) {
        int count = departmentMapper.addDoor(newDoor);
        return count > 0;
    }

    public Department getDoorById(Integer deptId) {
        System.out.println("Service查询部门ID: " + deptId);
        // 先尝试直接查询
        Department dept = departmentMapper.getDoorById(deptId);
        if (dept != null) {
            System.out.println("直接查询成功: " + dept.getDeptName());
            return dept;
        }
        // 如果直接查询失败，尝试遍历所有部门
        System.out.println("直接查询返回null，尝试遍历方式");
        List<Department> deptList = departmentMapper.getAllDoor(null);
        System.out.println("部门总数: " + deptList.size());
        for (Department d : deptList) {
            System.out.println("部门: id=" + d.getId() + ", name=" + d.getDeptName());
            if (d.getId() != null && d.getId().equals(deptId)) {
                return d;
            }
        }
        return null;
    }

    public boolean updateDoor(Department updateDoor) {
        int rows = departmentMapper.updateDoor(updateDoor);
        return rows > 0;
    }

    public boolean deleteDoor(Integer id) {
        int rows = departmentMapper.deleteDoor(id);
        return rows > 0;
    }
}