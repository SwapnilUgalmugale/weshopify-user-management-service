package com.weshopify.platform.service;

import java.util.List;

import com.weshopify.platform.bean.UserBean;
;

public interface UserMgmtService {
	
	public List<UserBean> getAllUsers();
	
	public List<UserBean> createUser(UserBean user) ;

}
