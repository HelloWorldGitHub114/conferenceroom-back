package com.xd11z.myserver.data;
import java.io.Serializable;

/**
 * 标准的服务器响应类
 * code+data+msg
 * 后端向前端的响应一定为该类型
 */
public class ServerResponse implements Serializable {

    private int code; // 200是正常，非200表示异常
    private String msg; //返回的提示信息
    private Object data; //返回的数据

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 操作成功，用这个函数生成一个应答对象
     * @param data 应答包括的数据对象
     * @return
     */
    public static ServerResponse success(Object data) {
        return success(200, "操作成功", data);
    }

    public static ServerResponse success(int code, String msg, Object data) {
        ServerResponse r = new ServerResponse();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    /**
     * 操作失败时返回该结果
     * @param msg 操作失败的信息提示
     * @return 返回数据对象
     */
    public static ServerResponse fail(String msg) {
        return fail(400, msg, null);
    }

    public static ServerResponse fail(String msg, Object data) {
        return fail(400, msg, data);
    }

    public static ServerResponse fail(int code, String msg) {
        return fail(code, msg, null);
    }

    public static ServerResponse fail(int code, String msg, Object data) {
        ServerResponse r = new ServerResponse();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
