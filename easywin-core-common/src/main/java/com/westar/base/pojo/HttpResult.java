package com.westar.base.pojo;

import java.io.Serializable;

/**
 * Created by H87 on 2016/4/13.
 */
public class HttpResult<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5842940441092188539L;

    /**
     * 成功
     */
	public static int CODE_OK = 0;
    /**
     * 失败
     */
    public static int CODE_ERROR = -1;

    //返回码
    private int code;

    //返回的消息
    private String msg;

    //返回的数据
    private T data;

    public HttpResult(String errormsg) {
        this.code = CODE_ERROR;
        this.msg = errormsg;
    }

    public HttpResult(T data) {
        this.code = CODE_OK;
        this.data = data;
    }

    public HttpResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public HttpResult(){
        this.code = CODE_OK;
    }
    
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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	/**
	 * 返回错误
	 * @param msg
	 * @return
	 */
	public HttpResult<T> error(String msg){
		HttpResult<T> httpResult = new HttpResult<T>();
		httpResult.setCode(CODE_ERROR);
		httpResult.setMsg(msg);
		return httpResult;
	}
	/**
	 * 返回正确
	 * @param data
	 * @return
	 */
	public HttpResult<T> ok(T data){
		HttpResult<T> httpResult = new HttpResult<T>();
		httpResult.setCode(CODE_OK);
		httpResult.setData(data);
		httpResult.setMsg("success");
		return httpResult;
	}
    
}
