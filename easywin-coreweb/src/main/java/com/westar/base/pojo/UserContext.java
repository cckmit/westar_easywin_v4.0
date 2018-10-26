package com.westar.base.pojo;

import java.io.Serializable;
import java.util.List;

import com.westar.base.model.Organic;

/**
 * Created by wang-dev
 */
public class UserContext implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//用户id
    private Integer userId;

    //登录名
    private String loginName;

    //用户名
    private String userName;

    //所在机构
    private Integer orgId;

    //机构名称
    private String orgName;

    //机构全称
    private String orgPathName;

    //所在根机构
    private Integer orgRootId;

    //根机构名称
    private String orgRootName;
    
   //大图片信息
	private String bigImgUuid;
	private String bigImgName;
	
	//中图片信息
	private String midImgUuid;
	private String midImgName;
	
	//小图片信息
	private String smImgUuid;
	private String smImgName;
	
	//LOGO图片信息
	private String logoUuid;
	private String logoName;
	
	//部门名称
	private String depName;
	
	//模糊查询的条件
	private String condition;
	
	//用户所参加团队集合
	private List<Organic> listOrg;
	
	/*企业编号*/
	private Integer comId;
	
	/*移动电话*/
	private String movePhone;
	

    public String getBigImgUuid() {
		return bigImgUuid;
	}

	public void setBigImgUuid(String bigImgUuid) {
		this.bigImgUuid = bigImgUuid;
	}

	public String getBigImgName() {
		return bigImgName;
	}

	public void setBigImgName(String bigImgName) {
		this.bigImgName = bigImgName;
	}

	public String getMidImgUuid() {
		return midImgUuid;
	}

	public void setMidImgUuid(String midImgUuid) {
		this.midImgUuid = midImgUuid;
	}

	public String getMidImgName() {
		return midImgName;
	}

	public void setMidImgName(String midImgName) {
		this.midImgName = midImgName;
	}

	public String getSmImgUuid() {
		return smImgUuid;
	}

	public void setSmImgUuid(String smImgUuid) {
		this.smImgUuid = smImgUuid;
	}

	public String getSmImgName() {
		return smImgName;
	}

	public void setSmImgName(String smImgName) {
		this.smImgName = smImgName;
	}

	public String getLogoUuid() {
		return logoUuid;
	}

	public void setLogoUuid(String logoUuid) {
		this.logoUuid = logoUuid;
	}

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public List<Organic> getListOrg() {
		return listOrg;
	}

	public void setListOrg(List<Organic> listOrg) {
		this.listOrg = listOrg;
	}

	public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgPathName() {
        return orgPathName;
    }

    public void setOrgPathName(String orgPathName) {
        this.orgPathName = orgPathName;
    }

    public Integer getOrgRootId() {
        return orgRootId;
    }

    public void setOrgRootId(Integer orgRootId) {
        this.orgRootId = orgRootId;
    }

    public String getOrgRootName() {
        return orgRootName;
    }

    public void setOrgRootName(String orgRootName) {
        this.orgRootName = orgRootName;
    }

	public Integer getComId() {
		return comId;
	}

	public void setComId(Integer comId) {
		this.comId = comId;
	}

	public String getMovePhone() {
		return movePhone;
	}

	public void setMovePhone(String movePhone) {
		this.movePhone = movePhone;
	}
}
