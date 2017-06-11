package com.hqu.model;

import org.springframework.stereotype.Component;

@Component("responseModel")
public class ResponseModel {

	public boolean status = true;
	public String msg = "";
	public Object data = null;
	
	public boolean hasLogin = true;
	
	public void setHasLogin(boolean hasLogin){
		this.hasLogin = hasLogin;
	}

	public boolean isHasLogin(){
		return hasLogin;
	}
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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

   

	public ResponseModel() {
        super();
    }

    public ResponseModel(boolean status, String msg, Object data) {
        super();
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

}
