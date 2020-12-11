package com.lll.quartz.entity.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author liang
 * @Date 2019/11/13 11:25
 * @description 返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnResult<T> implements Serializable {
    private int httpCode;

    @JsonIgnore
    private String code;

    private String msg;

    private T data;

    private Paginator paginator;

    public static <T> ReturnResult<T> ok() {
        return new ReturnResult(Constant.HTTP_OK, null, "操作成功！", null, null);
    }

    public static <T> ReturnResult<T> ok(T data) {
        return new ReturnResult(Constant.HTTP_OK, null, "操作成功！", data, null);
    }

    public static <T> ReturnResult<T> ok(T data, Paginator paginator) {
        return new ReturnResult(Constant.HTTP_OK, null, "操作成功！", data, paginator);
    }

}
