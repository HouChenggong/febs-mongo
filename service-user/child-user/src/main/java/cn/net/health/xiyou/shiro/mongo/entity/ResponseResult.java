package cn.net.health.xiyou.shiro.mongo.entity;

import lombok.*;

import java.io.Serializable;


/**
 * @author xiyou
 * @version 1.2
 * @date 2019/11/6 11:45
 * 工具类，JSON返回的统一格式，success表示
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ResponseResult<T> implements Serializable {

    private boolean success;

    private int code;

    private String msg = "";

    private T data ;

    private T msgMap;


    public ResponseResult(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public ResponseResult(boolean success, int code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(boolean success, String msg, T data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(boolean success) {
        this.success = success;
    }
}
