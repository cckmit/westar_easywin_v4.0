package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import com.westar.base.model.FeeLoanOff;
import com.westar.base.model.Role;
import com.westar.base.model.RoleAuthority;
import com.westar.base.model.RoleBindingUser;
import com.westar.base.model.UserInfo;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDao extends BaseDao {

    /**
     * 分页获取角色列表
     * @param userInfo 当前登录用户
     * @param role 查询参数列表
     * @return
     */
    public List<Role> listPagedRole(UserInfo userInfo,Role role){
        StringBuffer sql = new StringBuffer();
        List<Object> args = new ArrayList<Object>();

        sql.append("\n select * from role ");
        sql.append("\n where 1 = 1 ");
        sql.append("\n and comId = ? ");
        args.add(userInfo.getComId());
        this.addSqlWhereLike(role.getRoleName(),sql,args," and roleName like ?");

        return pagedQuery(sql.toString()," id desc ",args.toArray(),Role.class);
    }

    /**
     * 根据角色主键获取该角色下的所有权限
     * @param roleId
     * @param userInfo
     * @return
     */
    public List<RoleAuthority> listRoleAuthorityByRoleId(Integer roleId,UserInfo userInfo){
        StringBuffer sql = new StringBuffer();
        List<Object> args = new ArrayList<Object>();

        sql.append("\n select * from roleAuthority where roleId = ? and comId = ? ");
        args.add(roleId);
        args.add(userInfo.getComId());

        return listQuery(sql.toString(),args.toArray(),RoleAuthority.class);
    }

    /**
     * 根据角色主键获取绑定的用户
     * @param roleId
     * @param userInfo
     * @return
     */
    public List<RoleBindingUser> listRoleBindingUserByRoleId(Integer roleId,UserInfo userInfo){
        StringBuffer sql = new StringBuffer();
        List<Object> args = new ArrayList<Object>();

        sql.append("\n select a.*,b.userName,b.id as userId from roleBindingUser a left join userInfo b on a.userId = b.id where a.roleId = ? and a.comId = ? ");
        args.add(roleId);
        args.add(userInfo.getComId());

        return listQuery(sql.toString(),args.toArray(),RoleBindingUser.class);
    }

    /**
     * 根据角色主键获取绑定的用户的信息
     * @param roleId
     * @param userInfo
     * @return
     */
    public List<UserInfo> listUserByRoleId(Integer roleId,UserInfo userInfo){
        StringBuffer sql = new StringBuffer();
        List<Object> args = new ArrayList<Object>();

        if(null != roleId && roleId != -1 && roleId != 0){
            sql.append("\n select b.* from roleBindingUser a left join userInfo b on a.userId = b.id where a.roleId = ? and a.comId = ? ");
            args.add(roleId);
        }else if(roleId == 0){
            //roleId为0时查询所有人员
            sql.append("\n select a.* from userInfo a left join userOrganic b on a.id = b.userId where b.comId = ?");
        }
        args.add(userInfo.getComId());

        return listQuery(sql.toString(),args.toArray(),UserInfo.class);
    }

    /**
     * 根据唯一字段获取用户绑定
     * @param roleId
     * @param userId
     * @param comId
     * @return
     */
    public RoleBindingUser getRoleBindingUserByKey(Integer roleId,Integer userId,Integer comId){
        StringBuffer sql = new StringBuffer();
        List<Object> args = new ArrayList<Object>();

        sql.append("\n select * from (select * from roleBindingUser where roleId = ? and userId = ? and comId = ? ) where rownum <= 1");
        args.add(roleId);
        args.add(userId);
        args.add(comId);

        return (RoleBindingUser) this.objectQuery(sql.toString(), args.toArray(),RoleBindingUser.class);
    }

    /**
     * 角色列表
     * @param userInfo
     * @return
     */
    public List<Role> listRole(UserInfo userInfo){
        StringBuffer sql = new StringBuffer();
        List<Object> args = new ArrayList<Object>();

        sql.append("\n select * from role where comId = ?");
        args.add(userInfo.getComId());

        return listQuery(sql.toString(),args.toArray(),Role.class);
    }
}
