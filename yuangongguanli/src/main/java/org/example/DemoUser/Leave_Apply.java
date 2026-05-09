package org.example.DemoUser;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Leave_Apply {
    private Integer id;
    private Integer userId;
    private String username;
    private String type;
    private Date startTime;
    private Date endTime;
    private String reason; // 原因
    private String status; // 请假审批的状态
    private LocalDateTime createTime;
    
    Leave_Apply(){}
    
    Leave_Apply(Integer id,Integer userId,String username,String type,Date startTime,Date endTime,String reason,String status){
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = status;
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
