package org.example.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.DemoUser.Leave_Apply;

import java.util.List;

@Mapper
public interface Leave_ApplyMapper {
    // 提交请假
    void insertLeave(Leave_Apply leaveApply);
    
    // 查询个人请假记录
    List<Leave_Apply> getByUserId(@Param("userId") Integer userId);
    
    // 根据用户名查询请假记录
    List<Leave_Apply> getByUsername(@Param("username") String username);
    
    // 查询所有请假记录
    List<Leave_Apply> listAll();

    // 审批修改状态
    void updateStatus(@Param("id") Integer id, @Param("status") String status);
}
