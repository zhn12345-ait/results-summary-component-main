package org.example.Service;

import org.example.DemoUser.SystemLog;
import org.example.Mapper.SystemLogMapper;
import org.example.Mapper.DemouserMapper;
import org.example.DemoUser.Demouser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SystemLogService {
    @Resource
    private SystemLogMapper systemLogMapper;
    
    @Resource
    private DemouserMapper demouserMapper;
    
    public void addLog(String username, String action, String ipAddress) {
        Demouser user = demouserMapper.login(username);
        if (user == null) return;
        
        SystemLog log = new SystemLog();
        log.setUserId(user.getId());
        log.setUsername(username);
        log.setAction(action);
        log.setIpAddress(ipAddress);
        log.setCreateTime(LocalDateTime.now());
        systemLogMapper.insertLog(log);
    }
    
    public List<SystemLog> getLogsByUsername(String username) {
        return systemLogMapper.getLogsByUsername(username);
    }
    
    public List<SystemLog> getAllLogs() {
        return systemLogMapper.getAllLogs();
    }
}