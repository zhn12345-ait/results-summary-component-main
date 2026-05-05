package org.example;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("tushujieyuebiao")
public class Book {

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 关键！强制指定数据库列名，不让它自动转下划线
    private String bookName;

    @TableField("author")
    private String author;

    @TableField("category")
    private String category;

    @TableField("status")
    private Integer status;

    @TableField("type")
    private String type;

    public Book() {
    }

    public Book(Integer id,String bookName, String author, String category, Integer status, String type) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.category = category;
        this.status = status;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {this.id = id;}

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) { this.status = status;}

    public String getType() { return type;}
    
    public void setType(String type) { this.type = type;}
}