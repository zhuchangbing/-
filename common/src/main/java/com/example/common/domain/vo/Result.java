package com.example.common.domain.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private String message;
    private Object data;

    public static Result success(Integer code, String message, Object data) {
        return new Result(code, message, data);
    }

    public static Result success(Integer code, Object data) {
        return Result.success(code, "操作成功", data);
    }

    public static Result success(Object data) {
        return Result.success(200, data);
    }

    public static Result success() {
        return Result.success(null);
    }

    public static Result error(Integer code, String message, Object data) {
        return new Result(code, message, data);
    }

    public static Result error(Integer code, String message) {
        return Result.error(code, message, null);
    }

    public static Result error(String message) {
        return Result.error(201, message, null);
    }

    /**
     * 将当前对象转换成字符串
     *
     * @return
     */
    public String toJson() throws JsonProcessingException {
        // 当前对象是this
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}