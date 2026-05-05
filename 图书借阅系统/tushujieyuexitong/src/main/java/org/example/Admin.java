package org.example;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("admin") // 指定数据库表名与当前实体类对应
public class Admin {
    @TableId(type = IdType.AUTO)
    private  Integer id; // 主键id
    private  String userName; // 姓名
    private  String passWord;
    private String role;

    // 无参构造（MyBatis-Plus 必须要有）
    public Admin() {
    }

    // Getter 和 Setter 方法（一个都不能少）
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

