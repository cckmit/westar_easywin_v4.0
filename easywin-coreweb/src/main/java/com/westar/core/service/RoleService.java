package com.westar.core.service;

import java.util.List;

import com.westar.base.model.Role;
import com.westar.base.model.RoleAuthority;
import com.westar.base.model.RoleBindingUser;
import com.westar.base.model.UserInfo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.core.dao.RoleDao;

@Service
public class RoleService {

	@Autowired
	RoleDao roleDao;

	@Autowired
	SystemLogService systemLogService;

	/**
	 * 分页获取角色列表
	 * @param userInfo 当前用户
	 * @param role 查询参数列表
	 * @return
	 */
	public List<Role> listPagedRole(UserInfo userInfo,Role role){
		return roleDao.listPagedRole(userInfo,role);
	}

	/**
	 * 启用角色
	 * @param ids
	 * @param userInfo
	 */
	public void enabledRole(Integer[] ids,UserInfo userInfo,Integer state){
		if(ids.length > 0){
			for(int i=0;i<ids.length;i++){
				Role role = (Role) roleDao.objectQuery(Role.class,ids[i]);
				role.setEnable(state);
				roleDao.update(role);
			}
			//添加系统日志
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), state == 1 ? "启用角色" : "禁用角色",
					ConstantInterface.TYPE_ROLE,userInfo.getComId(),userInfo.getOptIP());
		}
	}

	/**
	 * 删除角色
	 * @param ids 需要被删除角色的id
	 * @param userInfo
	 */
	public void deleteRole(Integer[] ids,UserInfo userInfo){
		if(ids.length > 0){
			for(int i=0;i<ids.length;i++){
				//获取该角色下的所有权限，并删除
				List<RoleAuthority> authorities = roleDao.listRoleAuthorityByRoleId(ids[i],userInfo);
				for(RoleAuthority roleAuthority : authorities){
					roleDao.delById(RoleAuthority.class,roleAuthority.getId());
				}

				//获取该角色关联的用户，并删除
				List<RoleBindingUser> roleBindingUsers = roleDao.listRoleBindingUserByRoleId(ids[i],userInfo);
				for(RoleBindingUser roleBindingUser : roleBindingUsers){
					roleDao.delById(RoleBindingUser.class,roleBindingUser.getId());
				}

				//最后才删除角色表的内容
				roleDao.delById(Role.class,ids[i]);
			}
			//添加系统日志
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除角色",
					ConstantInterface.TYPE_ROLE,userInfo.getComId(),userInfo.getOptIP());
		}
	}

	/**
	 * 添加
	 * @param object
	 * @return
	 */
	public Integer addObject(Object object){
		return roleDao.add(object);
	}

	/**
	 * 根据id获取角色信息
	 * @param id
	 * @return
	 */
	public Role getRoleById(Integer id){
		return (Role) roleDao.objectQuery(Role.class,id);
	}

	/**
	 * 获取该角色下的所有人员
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public List<RoleBindingUser> listRoleBindingUserByRoleId(Integer id,UserInfo userInfo){
		return roleDao.listRoleBindingUserByRoleId(id,userInfo);
	}

	/**
	 * 获取该角色下的所有人员信息
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public List<UserInfo> listUserByRoleId(Integer id,UserInfo userInfo){
		return roleDao.listUserByRoleId(id,userInfo);
	}

	/**
	 * 更新角色
	 * @param role
	 * @param roleBindingUsers
	 */
	public void updateRole(Role role,Integer[] roleBindingUsers){
		//只能修改三个属性，所以需要查询原来的属性
		Role roleT = (Role) roleDao.objectQuery(Role.class,role.getId());
		roleT.setRoleName(role.getRoleName());
		roleT.setRoleRemark(role.getRoleRemark());
		//更新
		roleDao.update(roleT);
		if(!CommonUtil.isNull(roleBindingUsers)){
			//移除原来绑定的人员
			roleDao.delByField("roleBindingUser","roleId",role.getId());
			for(int i=0;i<roleBindingUsers.length;i++){
				RoleBindingUser roleBindingUser = new RoleBindingUser();
				roleBindingUser.setComId(roleT.getComId());
				roleBindingUser.setRoleId(role.getId());
				roleBindingUser.setUserId(roleBindingUsers[i]);
				roleDao.add(roleBindingUser);
			}
		}
	}

	/**
	 * 添加用户绑定
	 * @param roleBindingUser
	 * @return
	 */
	public Integer addBindingUser(RoleBindingUser roleBindingUser){
		return roleDao.add(roleBindingUser);
	}

	/**
	 * 删除用户绑定
	 * @param roleId
	 * @param userId
	 * @return
	 */
	public void deleteBindingUser(Integer roleId,Integer userId){
		roleDao.delByField("RoleBindingUser",new String[]{"roleId","userId"},new Object[]{roleId,userId});
	}

	/**
	 * 根据唯一字段获取用户绑定
	 * @param roleId
	 * @param userId
	 * @param comId
	 * @return
	 */
	public RoleBindingUser getRoleBindingUserByKey(Integer roleId,Integer userId,Integer comId){
		return roleDao.getRoleBindingUserByKey(roleId,userId,comId);
	}

	/**
	 * 角色列表
	 * @param userInfo
	 * @return
	 */
	public List<Role> listRole(UserInfo userInfo){
		return roleDao.listRole(userInfo);
	}
}
