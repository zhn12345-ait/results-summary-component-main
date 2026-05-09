package org.example.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.DemoUser.Employee;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmployeeMapper {
    int addEmployee(Employee employee);
    int deleteEmployee(Integer id);
    int updateEmployee(Employee employee);
    
    Employee getEmployeeById(Integer id);
    List<Employee> getAllEmployee(Map<String, Object> map);
    // 员工：根据用户名查自己信息
    Employee getEmployeeByUsername(String username);
    // 管理员：修改员工状态
    int updateEmployeeStatus(@Param("id") Integer id, @Param("status") String status);
}
