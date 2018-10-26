package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westar.base.model.Customer;
import com.westar.base.model.DataDic;
import com.westar.base.model.Item;
import com.westar.base.model.Organic;
import com.westar.base.model.Role;
import com.westar.base.model.RoleBindingUser;
import com.westar.base.model.Task;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.UserInfoService;
import com.westar.core.web.DataDicContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.core.service.RoleService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{

	@Autowired
	RoleService roleService;

	@Autowired
	ForceInPersionService forceInService;

    @Autowired
    UserInfoService userInfoService;

    /**
     * 角色分页
     * @param role
     * @param request
     * @return
     */
    @RequestMapping("/listPagedRole")
	public ModelAndView listPagedRole(Role role,HttpServletRequest request){
		ModelAndView view = new ModelAndView("/organic/organicCenter");
		UserInfo userInfo = this.getSessionUser();

		List<Role> list = roleService.listPagedRole(userInfo,role);
		view.addObject("list",list);

		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_ATTENCE);

		view.addObject("userInfo",userInfo);
		view.addObject("isForceIn", isForceIn);

		return view;
	}


    /**
     * 角色列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/listRole")
    public List<Role> listRole(){
        UserInfo userInfo = this.getSessionUser();
        List<Role> roles = roleService.listRole(userInfo);
        return roles;
    }

    /**
     * 启用角色
     * @param ids 要启用的角色的主键ID
     * @param redirectPage
     * @return
     */
    @RequestMapping("/enableRole")
    public ModelAndView enableRole(Integer[] ids, String redirectPage) {
        UserInfo sessionUser = this.getSessionUser();
        if(sessionUser.getAdmin().equals("0")){//没有管理权限的
            this.setNotification(Notification.ERROR, "没有管理权限!");
        }else{
            roleService.enabledRole(ids,sessionUser,1);
            this.setNotification(Notification.SUCCESS, "启用成功!");
        }
        return new ModelAndView("redirect:" + redirectPage);
    }

    /**
     * 禁用角色
     * @param ids 要禁用的角色的主键id
     * @param redirectPage
     * @return
     */
    @RequestMapping("/disableRole")
    public ModelAndView disableRole(Integer[] ids, String redirectPage){
        UserInfo sessionUser = this.getSessionUser();
        //没有管理权限的
        if(sessionUser.getAdmin().equals("0")){
            this.setNotification(Notification.ERROR, "没有管理权限!");
        }else{
            roleService.enabledRole(ids,sessionUser,0);
            this.setNotification(Notification.SUCCESS, "禁用成功!");
        }
        return new ModelAndView("redirect:" + redirectPage);
    }

    /**
     * 删除角色
     * @param ids 要删除的角色的主键id
     * @param redirectPage
     * @return
     */
    @RequestMapping("/deleteRole")
    public ModelAndView deleteRole(Integer[] ids, String redirectPage){
        UserInfo sessionUser = this.getSessionUser();
        //没有管理权限的
        if(sessionUser.getAdmin().equals("0")){
            this.setNotification(Notification.ERROR, "没有管理权限!");
        }else{
            roleService.deleteRole(ids,sessionUser);
            this.setNotification(Notification.SUCCESS, "删除成功!");
        }
        return new ModelAndView("redirect:" + redirectPage);
    }


    /**
     * 跳转角色新增页面
     * @return
     */
    @RequestMapping("/addRolePage")
    public ModelAndView addRolePage(){
        UserInfo userInfo = this.getSessionUser();
        ModelAndView view = new ModelAndView("/role/addRole");

        //取得常用人员列表
        List<UserInfo> listUsed = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),10);
        view.addObject("listUsed",listUsed);

        //消息推送方式
        List<DataDic> listClockWay=DataDicContext.getInstance().listTreeDataDicByType("clockWay");
        view.addObject("listClockWay", listClockWay);
        return view;
    }

    /**
     * 跳转角色修改页面
     * @param id
     * @return
     */
    @RequestMapping("/updateRolePage")
    public ModelAndView updateRolePage(Integer id){
        UserInfo userInfo = this.getSessionUser();
        ModelAndView view = new ModelAndView("/role/updateRole","userInfo",userInfo);

        //取得常用人员列表
        List<UserInfo> listUsed = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),10);
        view.addObject("listUsed",listUsed);

        Role role = roleService.getRoleById(id);
        view.addObject("role",role);

        //获取角色下绑定的用户
        List<RoleBindingUser> users = roleService.listRoleBindingUserByRoleId(id,userInfo);
        view.addObject("users",users);

        //消息推送方式
        List<DataDic> listClockWay=DataDicContext.getInstance().listTreeDataDicByType("clockWay");
        view.addObject("listClockWay", listClockWay);
        return view;
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @RequestMapping("/addRole")
    public ModelAndView addRole(Role role,Integer[] roleBindingUsers){
        ModelAndView view = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
        UserInfo userInfo = this.getSessionUser();

        role.setEnable(1);
        role.setCreator(userInfo.getId());
        role.setComId(userInfo.getComId());

        Integer id = roleService.addObject(role);

        if(!CommonUtil.isNull(roleBindingUsers)){
            for(int i=0;i<roleBindingUsers.length;i++){
                RoleBindingUser roleBindingUser = new RoleBindingUser();
                roleBindingUser.setComId(userInfo.getComId());
                roleBindingUser.setRoleId(id);
                roleBindingUser.setUserId(roleBindingUsers[i]);
                roleService.addObject(roleBindingUser);
            }
        }

        this.setNotification(Notification.SUCCESS, "添加成功!");

        return view;
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @RequestMapping("/updateRole")
    public ModelAndView updateRole(Role role,Integer[] roleBindingUsers){
        ModelAndView view = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
        UserInfo userInfo = this.getSessionUser();

        roleService.updateRole(role,roleBindingUsers);

        this.setNotification(Notification.SUCCESS, "添加成功!");

        return view;
    }

    /**
     * 获取绑定角色的用户列表
     * @param id
     * @return
     */
    @RequestMapping("/listRoleBindingUsers")
    public ModelAndView listRoleBindingUsers(Integer id){
        ModelAndView view = new ModelAndView("/role/listRoleBindingUsers");
        UserInfo userInfo = this.getSessionUser();
        //获取角色下绑定的用户
        List<RoleBindingUser> users = roleService.listRoleBindingUserByRoleId(id,userInfo);
        view.addObject("users",users);
        view.addObject("id",id);
        return view;
    }

    /**
     * 添加角色绑定用户
     * @param roleId
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/addBindingUser")
    public Map<String,Object> addBindingUser(Integer roleId, Integer userId){
        UserInfo userInfo = this.getSessionUser();
        Map<String,Object> map = new HashMap<String, Object>();

        RoleBindingUser roleBindingUserT = roleService.getRoleBindingUserByKey(roleId,userId,userInfo.getComId());

        //防止首页提示信息多次出现，只有出现1和0时才表明为有效的添加
        map.put("flag",-1);
       if(CommonUtil.isNull(roleBindingUserT)){
           RoleBindingUser roleBindingUser = new RoleBindingUser();
           roleBindingUser.setRoleId(roleId);
           roleBindingUser.setUserId(userId);
           roleBindingUser.setComId(userInfo.getComId());

           Integer id = roleService.addBindingUser(roleBindingUser);

           if(null != id && id > 0){
               map.put("flag",1);
           }else{
               map.put("flag",0);
           }
       }
        return map;
    }

    /**
     * 删除角色绑定用户
     * @param roleId
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/deleteBindingUser")
    public Map<String,Object> deleteBindingUser(Integer roleId, Integer userId){
        roleService.deleteBindingUser(roleId,userId);
        return null;
    }
}