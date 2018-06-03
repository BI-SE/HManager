package com.wjq.model;

/**
 * Created by deior on 2018/6/3.
 */
public class Result {


    private Boolean bizSucc =true;

    private String msg;

    public Result(Boolean bizSucc,String msg){
        this.bizSucc =bizSucc;
        this.msg = msg;
    }

    public Boolean getBizSucc() {
        return bizSucc;
    }

    public void setBizSucc(Boolean bizSucc) {
        this.bizSucc = bizSucc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
