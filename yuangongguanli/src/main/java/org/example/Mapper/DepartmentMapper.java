package org.example.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.DemoUser.Department;

import java.util.List;
import java.util.Map;

@Mapper
public interface DepartmentMapper {
    int addDoor(Department newDoor);
    int updateDoor(Department updateDoor);
    int deleteDoor(Integer id);
    Department getDoorById(Integer id);
    List<Department> getAllDoor(Map<String, Object> map);
}
