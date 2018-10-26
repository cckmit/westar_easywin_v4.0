package com.westar.base.pojo;

import java.io.Serializable;

import com.westar.base.model.UserInfo;

/**
 * Created by wang-dev
 */
public class UserAuth implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//用户信息
    private UserInfo userInto;

    //授权码
    private String authKey;
    
    public UserAuth(UserInfo uc,String authKey){
    	this.userInto=uc;
    	this.authKey=authKey;
    }

    public UserInfo getUserInfo() {
        return userInto;
    }

    public void setUserInfo(UserInfo userInto) {
        this.userInto = userInto;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
