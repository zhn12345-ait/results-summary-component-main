package org.example.DemoUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;


@TableName("employee")
public class Employee {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String phone;
    private String idCard;
    private LocalDate entryTime;
    private Integer deptId;
    @TableField("deptname")
    private String deptName; //部门名称
    private String username; //绑定登陆账号
    private String password; //临时存储密码，用于创建用户账号
    private String status;   // 员工状态
    private BigDecimal salary; // 基本工资
    
    public Employee() {}
    
    public Employee(Integer id, String name, String phone, String idCard, LocalDate entryTime, Integer deptId, String username, String status, BigDecimal salary) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.idCard = idCard;
        this.entryTime = entryTime;
        this.deptId = deptId;
        this.username = username;
        this.status = status;
        this.salary = salary;
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getIdCard() {
        return idCard;
    }
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    
    public LocalDate getEntryTime(){
        return entryTime;
    }
    public void setEntryTime(LocalDate entryTime){
        this.entryTime = entryTime;
    }

    public Integer getDeptId() {
        return deptId;
    }
    public void setDeptId(Integer deptId){
        this.deptId = deptId;
    }
    
    public String getDeptName() {
        return deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
