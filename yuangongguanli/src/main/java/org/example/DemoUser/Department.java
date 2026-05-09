package org.example.DemoUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


import java.time.LocalDate;

@TableName("department")
public class Department {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String deptNo;
    private String deptName;
    private String managerName;
    private LocalDate createTime;

    public Department() {
    }

    public Department(Integer id, String deptNo, String deptName, String managerName, LocalDate createTime) {
        this.id = id;
        this.deptNo = deptNo;
        this.deptName = deptName;
        this.managerName = managerName;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getDeptNo() {
        return deptNo;
    }
    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }
    
    public String getDeptName() {
        return deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
    public String getManagerName() {
        return managerName;
    }
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    
    public LocalDate getCreateTime() {
        return createTime;
    }
    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }
}    
