package org.example.Service;

import org.example.DemoUser.Attendance;
import org.example.DemoUser.Demouser;
import org.example.Mapper.AttendanceMapper;
import org.example.Mapper.DemouserMapper;
import org.example.Mapper.Leave_ApplyMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AttendanceService {
    @Resource // 自动注入Mapper接口
    private AttendanceMapper attendanceMapper;
    
    @Resource
    private DemouserMapper demouserMapper;
    
    // 上班时间（小时）
    private static final int WORK_START_HOUR = 9;
    // 下班时间（小时）
    private static final int WORK_END_HOUR = 18;
    // 迟到阈值（分钟）
    private static final int LATE_THRESHOLD_MINUTES = 30;
    
    // 根据打卡时间判断状态
    private String determineStatus(String type, LocalDateTime clockTime) {
        int hour = clockTime.getHour();
        int minute = clockTime.getMinute();
        int totalMinutes = hour * 60 + minute;
        
        if ("上班".equals(type)) {
            int workStartMinutes = WORK_START_HOUR * 60;
            int lateThresholdMinutes = workStartMinutes + LATE_THRESHOLD_MINUTES;
            
            if (totalMinutes <= workStartMinutes) {
                return "正常";
            } else if (totalMinutes <= lateThresholdMinutes) {
                return "迟到";
            } else {
                return "严重迟到";
            }
        } else {
            int workEndMinutes = WORK_END_HOUR * 60;
            
            if (totalMinutes >= workEndMinutes) {
                return "正常";
            } else {
                return "早退";
            }
        }
    }
    
    //
    @Transactional
    public void clock(String username, String type) {
        try{
            // 1. 查询用户是否存在
            Demouser user = demouserMapper.login(username);
            System.out.println("=== 打卡操作 ===");
            System.out.println("输入用户名: " + username);
            System.out.println("查询到用户: " + (user != null ? "存在，用户ID=" + user.getId() : "不存在"));

            if (!Objects.isNull(user)){
                // 2. 如果存在，则打卡
                Attendance attendance = new Attendance();
                attendance.setUserId(user.getId());
                LocalDateTime now = LocalDateTime.now();
                attendance.setClockTime(now);
                attendance.setType(type);
                // 根据打卡时间判断状态
                attendance.setStatus(determineStatus(type, now));
                // 3. 插入打卡记录
                int result = attendanceMapper.insertAttendance(attendance);
                System.out.println("打卡成功！用户ID: " + user.getId() + ", 用户名: " + username + ", 类型: " + type + ", 时间: " + now + ", 插入结果: " + result);
            } else {
                System.out.println("打卡失败：用户不存在，用户名: " + username);
            }
        }catch (Exception e){
            System.out.println("打卡异常：用户名: " + username + ", 错误: " + e.getMessage());
            throw new RuntimeException("打卡失败", e);
        }
    }
    
    // 获取自己打卡记录
    public List<Attendance> getMyAttendance(String username) {
        if (username == null) {
            throw new RuntimeException("用户名为空");
        }
        System.out.println("=== 开始查询考勤记录 ===");
        System.out.println("输入用户名: " + username);
        Demouser user = demouserMapper.login(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        System.out.println("查询到用户信息: 用户ID=" + user.getId() + ", 用户名=" + user.getUsername());
        List<Attendance> records = attendanceMapper.selectAllAttendanceByUserId(user.getId());
        System.out.println("查询到 " + records.size() + " 条考勤记录");
        for (Attendance record : records) {
            System.out.println("记录：ID=" + record.getId() + ", 用户ID=" + record.getUserId() + ", 时间=" + record.getClockTime() + ", 类型=" + record.getType() + ", 状态=" + record.getStatus());
        }
        System.out.println("=== 查询结束 ===");
        return records;
    } 
    
    // 查询所有人（管理员）
    public List<Attendance> getAll(){
        return attendanceMapper.selectAll();
    }
    
    // 按用户名查询考勤记录
    public List<Attendance> getAttendanceByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("用户名为空");
        }
        Demouser user = demouserMapper.login(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return attendanceMapper.selectAllAttendanceByUserId(user.getId());
    }
    
    // 更新考勤状态
    @Transactional
    public boolean updateStatus(Integer id, String status) {
        Attendance attendance = attendanceMapper.selectById(id);
        if (attendance == null) {
            return false;
        }
        attendance.setStatus(status);
        int result = attendanceMapper.updateAttendance(attendance);
        return result > 0;
    }
}
