package org.example;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

@TableName("borrow")
public class Borrow {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer bookId;
    private String bookName;
    private String username;
    private Date borrowTime;
    private Date returnTime;
    private Integer status; // 0在借 1已还

    public Borrow() {}

    // getter + setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }
    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Date getBorrowTime() { return borrowTime; }
    public void setBorrowTime(Date borrowTime) { this.borrowTime = borrowTime; }
    public Date getReturnTime() { return returnTime; }
    public void setReturnTime(Date returnTime) { this.returnTime = returnTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}