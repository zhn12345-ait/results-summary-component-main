package org.example.common;


import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;
    
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMsg("操作成功");
        r.setData(data);
        return r;
    }
    
    public static <T> Result<T> success() {
        return success(null);
    }
    
    public static <T> Result<T> error(String msg) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }
    
    // 未登录
    public static <T> Result<T> unauthorized() {
        Result<T> r = new Result<>();
        r.setCode(401);
        r.setMsg("请先登录");
        return r;
    }
    
    // 无管理员权限
    public static <T> Result<T> forbidden() {
        Result<T> r = new Result<>();
        r.setCode(403);
        r.setMsg("无管理员权限");
        return r;
    }
}
