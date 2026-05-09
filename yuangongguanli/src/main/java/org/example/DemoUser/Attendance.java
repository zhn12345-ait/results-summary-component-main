package org.example.DemoUser;

import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.time.LocalDateTime;

@Data
public class Attendance {
    private Integer id;
    private Integer userId;
    private LocalDateTime clockTime; // 打卡时间
    private String type; // 上下班打卡
    private String status; // 打卡状态
    
    public Attendance(){}
    
    public Attendance(Integer id, Integer userId, LocalDateTime clockTime, String type, String status){
        this.id = id;
        this.userId = userId;
        this.clockTime = clockTime;
        this.type = type;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public Integer getUserId(){
        return userId;
    }
    public void setUserId(Integer userId){
        this.userId = userId;
    }

    public LocalDateTime getClockTime() {
        return clockTime;
    }
    public void setClockTime(LocalDateTime clockTime){
        this.clockTime = clockTime;
    }

    public String getType(){
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
