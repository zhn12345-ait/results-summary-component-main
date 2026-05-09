package org.example.Mapper;

import org.example.DemoUser.SystemLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SystemLogMapper {
    @Insert("INSERT INTO system_log (user_id, username, action, ip_address, create_time) VALUES (#{userId}, #{username}, #{action}, #{ipAddress}, #{createTime})")
    int insertLog(SystemLog log);
    
    @Select("SELECT * FROM system_log WHERE username = #{username} ORDER BY create_time DESC")
    List<SystemLog> getLogsByUsername(@Param("username") String username);
    
    @Select("SELECT * FROM system_log ORDER BY create_time DESC")
    List<SystemLog> getAllLogs();
}