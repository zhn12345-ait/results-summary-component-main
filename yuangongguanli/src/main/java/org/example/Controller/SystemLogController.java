package org.example.Controller;

import org.example.common.Result;
import org.example.DemoUser.SystemLog;
import org.example.Service.SystemLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/log")
public class SystemLogController {
    @Resource
    private SystemLogService systemLogService;
    
    @GetMapping("/all")
    public Result<?> getAllLogs() {
        List<SystemLog> logs = systemLogService.getAllLogs();
        return Result.success(logs);
    }
    
    @GetMapping("/search")
    public Result<?> getLogsByUsername(@RequestParam String username) {
        List<SystemLog> logs = systemLogService.getLogsByUsername(username);
        return Result.success(logs);
    }
}