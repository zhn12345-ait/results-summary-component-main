package org.example.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.DemoUser.Attendance;

import java.util.List;

@Mapper
public interface AttendanceMapper extends BaseMapper<Attendance> {
    // 插入考勤记录
    int insertAttendance(Attendance attendance);
    // 查询自己的记录
    List<Attendance> selectAllAttendanceByUserId(Integer userId);
    // 查询所有考勤记录
    List<Attendance> selectAll();
    // 根据ID查询考勤记录
    Attendance selectById(Integer id);
    // 更新考勤记录
    int updateAttendance(Attendance attendance);
}
