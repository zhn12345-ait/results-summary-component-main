package org.example.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.DemoUser.Demouser;

import java.util.List;
import java.util.Map;

@Mapper
public interface DemouserMapper extends BaseMapper<Demouser> {
    int insertEmp(Demouser newEmp);
    int updateEmp(Demouser updateEmp);
    int deleteEmp(Integer id);
    
    // 查询所有员工
    List<Demouser> getAlldemouser(Map<String, Object> map);
    
    // 根据ID查询员工
    Demouser getEmpById(Integer id);
    
    // 登录
    Demouser login(@Param("username") String name); // @Param 注解用于给参数命名
}
