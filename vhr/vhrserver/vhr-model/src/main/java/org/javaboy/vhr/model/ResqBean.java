package org.javaboy.vhr.model;

/**
 * 响应信息
 * Created by candy on 2020/10/25.
 */
public class ResqBean {
    private Integer status;
    private String msg;
    private Object obj;

    public static ResqBean build() {
        return new ResqBean();
    }

    public static ResqBean ok(String msg) {
        return new ResqBean(200, msg, null);
    }

    public static ResqBean ok(String msg, Object obj) {
        return new ResqBean(200, msg, obj);
    }

    public static ResqBean error(String msg) {
        return new ResqBean(500, msg, null);
    }

    public static ResqBean error(String msg, Object obj) {
        return new ResqBean(500, msg, obj);
    }

    public ResqBean() {
    }

    public ResqBean(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public Integer getStatus() {
        return status;
    }

    public ResqBean setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResqBean setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public ResqBean setObj(Object obj) {
        this.obj = obj;
        return this;
    }
}
