package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.westar.base.model.DepTree;
import com.westar.base.model.Department;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.PinyinToolkit;
import com.westar.base.util.StringUtil;
import com.westar.core.dao.DepartmentDao;

@Service
public class DepartmentService {

	@Autowired
	DepartmentDao departmentDao;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	SystemLogService systemLogService;

	/**
	 * 树形查找部门信息
	 * @param id
	 * @param enabled
	 * @param comId 公司主键
	 * @return
	 */
	public List<Department> listTreeDep(Integer id, String enabled, Integer comId) {
		List<Department> list = departmentDao.listTreeDep(id,enabled,comId);
		return list;
	}
	/**
	 * 查询本部门和下级部门信息
	 * @param depId 本门主键
	 * @param enabled 是否启用
	 * @param comId 团队号
	 * @return
	 */
	public List<Department> listTreeSonDep(Integer depId, String enabled, Integer comId) {
		List<Department> list = departmentDao.listTreeSonDep(depId,enabled,comId);
		return list;
	}
	/**
	 * 查询本部门和下级部门信息
	 * @param depId 本门主键
	 * @param enabled 是否启用
	 * @param comId 团队号
	 * @return
	 */
	public List<Department> listTreeSonDep(Integer[] depIds, String enabled, Integer comId) {
		List<Department> list = departmentDao.listTreeSonDep(depIds,enabled,comId);
		return list;
	}


	/**
	 * 新增部门
	 * @param dep
	 * @return
	 */
	public Integer addDepartment(Department dep) {
		//部门主键
		Integer depId = departmentDao.add(dep);
		
		//初始化部门树形结构
		this.initDepTree(dep.getComId());
				
		return depId;
	}
	
	/**
	 * 设置部门成员
	 * @param dep
	 */
	public void updateDepUsers(Department dep,UserInfo sessionUser){
		Integer comId = sessionUser.getComId();
		//取得默认的部门信息
		Department defaultDep = departmentDao.getDefalutDep(comId);
		if(!dep.getId().equals(defaultDep.getId())){
			//设置为默认部门
			departmentDao.updateUserDefDep(defaultDep.getId(),dep.getId(),comId);
		}
		//重新添加部门成员
		String userInfos = dep.getUserIds();
		if(!StringUtil.isBlank(userInfos)){
			for (String userId : userInfos.split(",")) {
				//重新设置部门成员
				departmentDao.updateUserDep(dep.getId(), comId, Integer.parseInt(userId));
			}
		}
	}

	/**
	 * 取得部门信息
	 * @param id
	 * @param comId 公司主键
	 * @return
	 */
	public Department getDep(Integer id, Integer comId) {
		Department dep= departmentDao.getDep(id,comId);
		if(null!=dep){
			dep.setListUser(userInfoService.listDepUser(id,comId));
		}
		return dep;
	}
	
	/**
	 * 取得在职用户信息
	 * @param id 部门信息
	 * @param comId 团队主键
	 * @return
	 */
	public Department getDepWitnEnabledUser(Integer id, Integer comId) {
		Department dep= departmentDao.getDep(id,comId);
		if(null!=dep){
			dep.setListUser(userInfoService.listEnabledUser(id,comId));
		}
		return dep;
	}


	/**
	 * 修改部门信息
	 * @param dep
	 * @param comId
	 */
	public void updateDep(Department dep,Integer comId) {
		departmentDao.update(dep);
		
		//初始化部门树形结构
		this.initDepTree(comId);
	}

	/**
	 * 获得组织机构树JSON数据
	 * @param dep
	 * @return
	 */
	public List<Department> listTreeOrganization(Department dep){
		return departmentDao.listTreeOrganization(dep);
	}
	
	/**
	 * 禁用启用部门
	 * @param ids  部门主键id
	 * @param enabled  启用状态 0禁用 1启用
	 * @param comId  公司主键
	 */
	public void updateDepEnabled(Integer[] ids, String enabled, Integer comId) {
		departmentDao.updateDepEnabled(ids,enabled,comId);
		
	}
	/**
	 * 删除部门
	 * @param ids
	 * @param comId 
	 */
	public void delDep(Integer[] ids, Integer comId) {
		//取得默认的部门信息
		Department defaultDep = departmentDao.getDefalutDep(comId);
		for (Integer depId : ids) {
			//设置为默认部门
			departmentDao.updateUserDefDep(defaultDep.getId(),depId,comId);
			
			departmentDao.delByField("depTree", new String[]{"comId","depId"}, new Object[]{comId,depId});
		}
		departmentDao.delById(Department.class, ids);
		
		//初始化部门树形结构
		this.initDepTree(comId);
		
	}
	/**
	 * 取得团队默认部门
	 * @param comId团队号
	 * @return
	 */
	public Department getDefalutDep(Integer comId) {
		return departmentDao.getDefalutDep(comId);
	}
	
	/**
	 * 添加默认部门
	 * @param creator
	 * @param userInfo
	 * @param optIP
	 */
	public Integer addDefaultDep(Integer creator,UserInfo userInfo,String optIP){
		// 设立默认部门
		Department dep = new Department();
		// 企业编号
		dep.setComId(userInfo.getComId());
		// 创建人
		dep.setCreator(creator);
		// 默认父节点为-1
		dep.setParentId(-1);
		// 启用
		dep.setEnabled("1");
		// 部门名称
		dep.setDepName("默认部门");
		dep.setAllSpelling(PinyinToolkit.cn2Spell("默认部门"));
		dep.setFirstSpelling(PinyinToolkit.cn2FirstSpell("默认部门"));
		Integer depId = departmentDao.add(dep);
		//初始化部门树形结构
		this.initDepTree(userInfo.getComId());

		// 部门添加日志
		systemLogService.addSystemLog(creator, userInfo.getUserName(), "创建默认部门",
				ConstantInterface.TYPE_DEP, userInfo.getComId(),optIP);
		return depId;
	}
	
	/**
	 * 初始化部门树形结构
	 * @param orgNum
	 */
	public void initDepTree(Integer comId) {
		//删除部门树
		departmentDao.delByField("depTree", new String[]{"comId"}, new Object[]{comId});
		//初始化部门树
		departmentDao.initDepTree(comId);
	}
	
}
