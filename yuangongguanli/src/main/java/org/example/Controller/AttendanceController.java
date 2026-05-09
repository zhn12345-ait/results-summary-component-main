package org.example.Controller;

import org.example.DemoUser.Attendance;
import org.example.DemoUser.Demouser;
import org.example.Service.AttendanceService;
import org.example.Service.DemouserService;
import org.example.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin // 跨域
@RestController
@RequestMapping("/attendance") // 访问路径
public class AttendanceController {
    @Resource
    private AttendanceService attendanceService;
    
    @Resource
    private DemouserService demouserService;
    
    //上班打卡
    @PostMapping("/in")
    public Result<?> clockIn(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        attendanceService.clock(username, "上班");
        return Result.success("上班打卡成功");
    }
    
    //下班打卡
    @PostMapping("/out")
    public Result<?> clockOut(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        attendanceService.clock(username, "下班");
        return Result.success("下班打卡成功");
    }
    
    // 我的打卡记录
    @GetMapping("/my")
    public Result<?> myAttendance(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        String token = request.getHeader("token");
        System.out.println("========================================");
        System.out.println("收到我的考勤请求");
        System.out.println("请求头中的token: " + (token != null ? token.substring(0, Math.min(20, token.length())) + "..." : "null"));
        System.out.println("解析出的用户名: " + username);
        System.out.println("========================================");
        List<Attendance> records = attendanceService.getMyAttendance(username);
        
        // 按日期分组，合并上下班记录
        Map<String, Map<String, Object>> result = new HashMap<>();
        for (Attendance record : records) {
            String date = record.getClockTime().toLocalDate().toString();
            if (!result.containsKey(date)) {
                result.put(date, new HashMap<>());
            }
            Map<String, Object> dayRecord = result.get(date);
            if ("上班".equals(record.getType())) {
                dayRecord.put("checkInTime", record.getClockTime().toLocalTime().toString());
                dayRecord.put("checkInStatus", record.getStatus());
            } else {
                dayRecord.put("checkOutTime", record.getClockTime().toLocalTime().toString());
                dayRecord.put("checkOutStatus", record.getStatus());
            }
            dayRecord.put("date", date);
        }
        
        // 确定每天的综合状态
        for (Map<String, Object> dayRecord : result.values()) {
            String checkInStatus = (String) dayRecord.get("checkInStatus");
            String checkOutStatus = (String) dayRecord.get("checkOutStatus");
            
            // 根据上下班状态确定综合状态
            String status = "正常";
            if ("严重迟到".equals(checkInStatus) || "严重迟到".equals(checkOutStatus)) {
                status = "严重迟到";
            } else if ("迟到".equals(checkInStatus)) {
                status = "迟到";
            } else if ("早退".equals(checkOutStatus)) {
                status = "早退";
            }
            
            dayRecord.put("status", status);
        }
        
        List<Map<String, Object>> list = new ArrayList<>(result.values());
        // 按日期倒序排序
        list.sort((a, b) -> b.get("date").toString().compareTo(a.get("date").toString()));
        
        return Result.success(list);
    }

    // 管理员查询所有
    @GetMapping("/all")
    public Result<?> getAll(HttpServletRequest request, @RequestParam(required = false) String username) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.forbidden();
        }
        
        List<Attendance> records;
        if (username != null && !username.isEmpty()) {
            // 按用户名搜索
            records = attendanceService.getAttendanceByUsername(username);
        } else {
            records = attendanceService.getAll();
        }
        
        // 不分组，显示所有打卡记录
        List<Map<String, Object>> list = new ArrayList<>();
        for (Attendance record : records) {
            Map<String, Object> dayRecord = new HashMap<>();
            dayRecord.put("id", record.getId());
            dayRecord.put("userId", record.getUserId());
            // 查询用户名
            Demouser user = demouserService.getEmpById(record.getUserId());
            if (user != null) {
                dayRecord.put("username", user.getUsername());
            }
            dayRecord.put("date", record.getClockTime().toLocalDate().toString());
            dayRecord.put("clockTime", record.getClockTime().toLocalTime().toString());
            dayRecord.put("type", record.getType());
            dayRecord.put("status", record.getStatus());
            list.add(dayRecord);
        }
        
        return Result.success(list);
    }
    
    // 管理员更新考勤状态
    @PostMapping("/updateStatus")
    public Result<?> updateStatus(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.forbidden();
        }
        
        Object idObj = params.get("id");
        Integer id = null;
        if (idObj instanceof Integer) {
            id = (Integer) idObj;
        } else if (idObj instanceof String) {
            id = Integer.parseInt((String) idObj);
        }
        String status = (String) params.get("status");
        
        if (id == null || status == null || status.isEmpty()) {
            return Result.error("参数不能为空");
        }
        
        boolean success = attendanceService.updateStatus(id, status);
        if (success) {
            return Result.success("状态更新成功");
        } else {
            return Result.error("更新失败，记录不存在");
        }
    }
}
