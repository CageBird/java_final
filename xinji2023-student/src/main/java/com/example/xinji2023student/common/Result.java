// 新建 com/example/xinji2023student/common/Result.java
package com.example.xinji2023student.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code; // 0:成功，1:失败
    private String msg;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMsg("成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(1);
        result.setMsg(msg);
        return result;
    }
}