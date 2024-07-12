package com.weshopify.platform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weshopify.platform.bean.RoleBean;
import com.weshopify.platform.outbound.RoleMgmtClient;



@Service

public class RoleMgmtServiceImpl implements RoleMgmtService {
	
	@Autowired
	private RoleMgmtClient roleMgmtClient;

	@Override
	public List<RoleBean> getAllRoles() {
		return roleMgmtClient.findAllRoles();
	}

	@Override
	public List<RoleBean> createRole(RoleBean role) {
		
		return roleMgmtClient.createRole(role);
	}

	@Override
	public boolean deleteRoleById(String id) {
		
		return roleMgmtClient.deleteRoleById(id);
	}

	@Override
	public List<RoleBean> updateRole(RoleBean role) {
		
		return roleMgmtClient.updateRole(role);
	}

}
