package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.westar.base.model.BusAttrMapFormColTemp;
import com.westar.base.model.FormCompon;
import com.westar.base.model.FormComponMaxField;
import com.westar.base.model.FormConf;
import com.westar.base.model.FormLayout;
import com.westar.base.model.FormLayoutHtml;
import com.westar.base.model.FormMod;
import com.westar.base.model.FormModSort;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.TableInfoVo;

/**
 * 
 * 描述:表单设计的dao层
 * @author zzq
 * @date 2018年8月27日 上午10:35:40
 */
@Repository
public class FormDao extends BaseDao {
	/**
	 * 分页查询表单信息
	 * @param formMod
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormMod> listPagedFormMod(FormMod formMod, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select a.id,a.recordCreateTime,a.comId,a.modName,a.enable,");
 		sql.append("\n case when b.id is null then 0 else b.id end formSortId,");
 		sql.append("\n case when b.id is null then '其他' else  b.sortname end modSortName");
 		sql.append("\n from formMod a left join formModSort b on a.comid=b.comid and a.formsortid=b.id");
 		sql.append("\n where 1=1");
 		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
 		this.addSqlWhere(formMod.getFormSortId(), sql, args, " and a.formSortId=?");
 		this.addSqlWhereLike(formMod.getModName(), sql, args, " and a.modName like ?");
		return this.pagedQuery(sql.toString(), " a.enable desc,a.id desc", args.toArray(), FormMod.class);
	}
	/**
	 * 取得表单最新布局
	 * @param formId
	 * @param userInfo
	 * @return
	 */
	public FormLayout queryFormLayoutByModId(Integer formId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select a.* from (");
 		sql.append("\n select a.* from formLayout a where 1=1");
 		this.addSqlWhere(comId, sql, args, " and a.comId=?");
 		this.addSqlWhere(formId, sql, args, " and a.formModId=?");
 		sql.append("\n order by a.version desc");
 		sql.append("\n )a where rownum=1");
		return (FormLayout) this.objectQuery(sql.toString(), args.toArray(), FormLayout.class);
	}
	/**
	 * 查询表单组件 非树形
	 * @param comId
	 * @param formModId
	 * @param formLayoutId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormCompon> listAllFormCompon(Integer comId, Integer formModId,
			Integer formLayoutId) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select a.* from formCompon a where 1=1");
 		this.addSqlWhere(comId, sql, args, " and a.comId=?");
 		this.addSqlWhere(formModId, sql, args, " and a.formModId=?");
 		this.addSqlWhere(formLayoutId, sql, args, " and a.formLayoutId=?");
		return this.listQuery(sql.toString(), args.toArray(), FormCompon.class);
	}
	
	/**
	 * 取得组件的布局 树形
	 * @param formModId
	 * @param formLayoutId
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormCompon> listTreeCompon(Integer formModId,
			Integer formLayoutId, UserInfo userInfo,Integer parentId) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		//TODO 取得表单最新布局
 		sql.append("\n select a.*,connect_by_isleaf as isLeaf from formCompon a where level=1");
 		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
 		this.addSqlWhere(formModId, sql, args, " and a.formModId=?");
 		this.addSqlWhere(formLayoutId, sql, args, " and a.formLayoutId=?");
 		sql.append("\n start with a.parentid="+parentId+" CONNECT BY PRIOR id = parentid");
 		sql.append("\n order siblings by a.id ");
		return this.listQuery(sql.toString(), args.toArray(), FormCompon.class);
	}
	
	/**
	 * 取得组件的布局 树形
	 * @param formModId
	 * @param formLayoutId
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormCompon> listTreeCompon(Integer formModId,
			Integer formLayoutId, Integer comId,Integer parentId) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		//TODO 取得表单最新布局
 		sql.append("\n select a.*,connect_by_isleaf as isLeaf from formCompon a where level=1");
 		this.addSqlWhere(comId, sql, args, " and a.comId=?");
 		this.addSqlWhere(formModId, sql, args, " and a.formModId=?");
 		this.addSqlWhere(formLayoutId, sql, args, " and a.formLayoutId=?");
 		sql.append("\n start with a.parentid="+parentId+" CONNECT BY PRIOR id = parentid");
 		sql.append("\n order siblings by a.id ");
		return this.listQuery(sql.toString(), args.toArray(), FormCompon.class);
	}
	/**
	 * 取得所有组件信息
	 * @param formModId
	 * @param formLayoutId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormCompon> listAllTreeCompon(Integer formModId,
			Integer formLayoutId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.id, a.comId,a.parentId,a.formModId,a.formLayoutId,a.componentKey,a.title,a.fieldId,a.orderNo,");
		sql.append("\n connect_by_isleaf as isLeaf from formCompon a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(formModId, sql, args, " and a.formModId=?");
		this.addSqlWhere(formLayoutId, sql, args, " and a.formLayoutId=?");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR id = parentid");
		sql.append("\n order siblings by a.id ");
		return this.listQuery(sql.toString(), args.toArray(), FormCompon.class);
	}
	
	/**
	 * 取得布局组件配置信息
	 * @param comId 企业号
	 * @param formModId 表单主键
	 * @param formLayoutId 布局主键
	 * @param fieldId 组件标识
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormConf> listLayoutConf(Integer comId, Integer formModId,
			Integer formLayoutId,Integer fieldId) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		//TODO 取得表单最新布局
 		sql.append("\n select a.* from formConf a where 1=1");
 		this.addSqlWhere(comId, sql, args, " and a.comId=?");
 		this.addSqlWhere(formModId, sql, args, " and a.formModId=?");
 		this.addSqlWhere(formLayoutId, sql, args, " and a.formLayoutId=?");
 		this.addSqlWhere(fieldId, sql, args, " and a.fieldId=?");
 		sql.append("\n order by a.fieldId,a.id");
		return this.listQuery(sql.toString(), args.toArray(), FormConf.class);
	}
	/**
	 * 列出用户权限范围内的表单
	 * @param formMod
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormMod> listPagedUserFormMod(FormMod formMod, UserInfo userInfo) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select a.* from formMod a where 1=1");
 		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comId=?");
		return this.pagedQuery(sql.toString(), " a.id desc", args.toArray(), FormMod.class);
	}
	/**
	 * 取得表单模板的最新版本
	 * @param formKey
	 * @param comId
	 * @return
	 */
	public FormMod getFormModVersion(Integer formKey, Integer comId) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select a.* from (");
 		sql.append("\n select a.*,b.version,b.id layoutId,b.formState from formMod a  ");
 		sql.append("\n right join formLayout b on a.comid=b.comid and a.id=b.formModId where 1=1");
 		this.addSqlWhere(comId, sql, args, " and a.comId=?");
 		this.addSqlWhere(formKey, sql, args, " and a.id=?");
 		sql.append("\n order by b.version desc");
 		sql.append("\n )a where rownum=1");
 		return (FormMod) this.objectQuery(sql.toString(), args.toArray(), FormMod.class);
	}
	/**
	 * 取得组件的父级类型
	 * @param comId 团队号
	 * @param formModId 表单模板主键
	 * @param formLayoutId 布局主键
	 * @param fieldId 文本字段
	 * @return
	 */
	public FormCompon getParentCompon(Integer comId, Integer formModId,
			Integer formLayoutId,Integer fieldId){
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select a.*,connect_by_isleaf as isLeaf from formCompon a where level=2");
 		this.addSqlWhere(comId, sql, args, " and a.comId=?");
 		this.addSqlWhere(formModId, sql, args, " and a.formModId=?");
 		this.addSqlWhere(formLayoutId, sql, args, " and a.formLayoutId=?");
 		sql.append("\n start with a.fieldid="+fieldId+" CONNECT BY PRIOR parentid = id");
 		sql.append("\n order siblings by a.id ");
 		return (FormCompon) this.objectQuery(sql.toString(), args.toArray(), FormCompon.class);
	}
	/**
	 * 查询表单所有组件
	 * @param comId 团队号
	 * @param formKey 表单模板主键
	 * @param layoutId 布局主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormCompon> listAllFormComponeNoOption(Integer comId, Integer formKey,
			Integer layoutId,Integer parentId) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select * from(");
 		sql.append("\n select a.*,connect_by_isleaf as isLeaf,");
 		sql.append("\n case when title is null then 0 else 1 end effect");
 		sql.append("\n from formCompon a where 1=1");
 		this.addSqlWhere(comId, sql, args, " and a.comId=?");
 		this.addSqlWhere(formKey, sql, args, " and a.formModId=?");
 		this.addSqlWhere(layoutId, sql, args, " and a.formLayoutId=?");
 		sql.append("\n start with a.parentid="+parentId+" CONNECT BY PRIOR id = parentid");
 		sql.append("\n order siblings by a.id ");
 		sql.append("\n  )a where a.componentkey not in ('Option','subItemTitle')");
		return this.listQuery(sql.toString(), args.toArray(), FormCompon.class);
	}
	/**
	 * 取得表单布局的所有组件
	 * @param comId 团队号
	 * @param formKey 表单模板主键
	 * @param layoutId 布局主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormCompon> listAllTreeFormCompone(Integer comId, Integer formKey,Integer layoutId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from formCompon a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(formKey, sql, args, " and a.formModId=?");
		this.addSqlWhere(layoutId, sql, args, " and a.formLayoutId=?");
		sql.append("\n start with a.parentid=-1 CONNECT BY PRIOR id = parentid");
		sql.append("\n order siblings by a.id ");
		return this.listQuery(sql.toString(), args.toArray(), FormCompon.class);
	}
	
	/**
	 * 将表单模板已归类的重置为其他类
	 * @param formModSortId
	 * @param comId
	 */
	public void delFormRelateModSort(Integer formModSortId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n update formMod set formSortId=0 where formSortId=? and comid=?");
 		args.add(formModSortId);
 		args.add(comId);
 		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 查询团队表单模板排序的最大号
	 * @param comId
	 * @return
	 */
	public FormModSort queryFormModSortOrderMax(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select max(a.orderNo)+1  as orderNo from formModSort a where a.comid =?");
		args.add(comId);
		return (FormModSort)this.objectQuery(sql.toString(), args.toArray(),FormModSort.class);
	}
	/**
	 * 取得所有保单分类
	 * @param formSort
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormModSort> listFormModSort(Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from formModSort a where 1=1");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		sql.append("\n order by a.orderNo,a.id");
		return this.listQuery(sql.toString(), args.toArray(), FormModSort.class);
	}
	/**
	 * 取得动态表单信息
	 * @param comId 团队号
	 * @param layOutId 布局主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormCompon> listDataTables(Integer comId, Integer layOutId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n	select a.* from formcompon a where a.componentkey='DataTable'");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		this.addSqlWhere(layOutId, sql, args, " and a.formlayoutid=?");
		sql.append("\n order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), FormCompon.class);
	}
	/**
	 * 步骤类型查询
	 * @param comId 团队号
	 * @param flowId 流程步骤关联主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormCompon> listSetpCompon(Integer comId, Integer flowId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n	select b.* from (");
		sql.append("\n	select formlayout.comid,formlayout.formmodid,formlayout.id,formlayout.version,");
		sql.append("\n	row_number() over (partition by formlayout.formmodid order by formlayout.version desc) as new_order");
		sql.append("\n	from  spFlowModel inner join formMod on spFlowModel.comid=formMod.comid and spFlowModel.formKey=formMod.id ");
		sql.append("\n	inner join formlayout on spFlowModel.comid=formlayout.comid and formlayout.formmodid=formMod.id");
		sql.append("\n	where spFlowModel.comid="+comId+" and spFlowModel.id="+flowId);
		sql.append("\n	)a inner join formcompon b on a.comid=b.comid and a.formmodid=b.formModId and a.id =b.formLayoutId");
		sql.append("\n	where a.new_order=1");
		return this.listQuery(sql.toString(), args.toArray(), FormCompon.class);
	}
	/**
	 * 取得最新的流程布局
	 * @param comId 团队号
	 * @param formModId 模块主键
	 * @return
	 */
	public FormLayout getLatestLayout(Integer comId, Integer formModId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n	select * from ( ");
		sql.append("\n		select a.*,row_number() over (partition by a.formmodid order by a.version desc) as new_order ");
		sql.append("\n		from formlayout  a where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		this.addSqlWhere(formModId, sql, args, " and a.formModId=?");
		sql.append("\n	)a where new_order=1");
		return (FormLayout) this.objectQuery(sql.toString(), args.toArray(), FormLayout.class);
	}
	/**
	 * 取得最新的流程布局
	 * @param comId 团队号
	 * @param formModId 模块主键
	 * @return
	 */
	public FormLayout getLatestSimpleLayout(Integer comId, Integer formModId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n	select * from ( ");
		sql.append("\n		select a.*,row_number() over (partition by a.formmodid order by a.version desc) as new_order ");
		sql.append("\n		from formlayout a where a.FORMSTATE=0 ");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		this.addSqlWhere(formModId, sql, args, " and a.formModId=?");
		sql.append("\n	)a where new_order=1");
		return (FormLayout) this.objectQuery(sql.toString(), args.toArray(), FormLayout.class);
	}
	
	/**
	 * 取得表单最大的fieldId
	 * @param comId 团队号
	 * @param formModId 表单模板主键
	 * @return
	 */
	public FormComponMaxField getFormMaxField(Integer comId, Integer formModId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n	select a.* from formComponMaxField a where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		this.addSqlWhere(formModId, sql, args, " and a.formModId=?");
		return (FormComponMaxField) this.objectQuery(sql.toString(), args.toArray(), FormComponMaxField.class);
	}
	/**
	 * 克隆配置文件
	 * @param formModId 原来的关联表单主键
	 * @param formLayoutId 原来的关联布局主键
	 * @param formModCloneId 克隆后关联表单主键
	 * @param formLayoutCloneId 克隆后关联布局主键
	 */
	public void formConfClone(Integer comId,Integer formModId, Integer formLayoutId,
			Integer formModCloneId, Integer formLayoutCloneId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n	insert into formConf(comid,formModId,formLayoutId,fieldId ,confName,confValue) ");
		sql.append("\n	 (select comid,"+formModCloneId+" formModId,"+formLayoutCloneId+" formLayoutId,fieldId,confName,confValue ");
		sql.append("\n	 from formConf where comid="+comId+" and  formModId="+formModId+"  and formLayoutId= "+formLayoutId);
		sql.append("\n	 )");
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 克隆配置文件
	 * @param formModId 原来的关联表单主键
	 * @param formLayoutId 原来的关联布局主键
	 * @param formModCloneId 克隆后关联表单主键
	 * @param formLayoutCloneId 克隆后关联布局主键
	 */
	public void formHtmlClone(Integer comId,Integer formModId, Integer formLayoutId,
			Integer formModCloneId, Integer formLayoutCloneId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n	insert into formLayoutHtml(comid,formModId,formLayoutId,layoutHtml) ");
		sql.append("\n	 (select comid,"+formModCloneId+" formModId,"+formLayoutCloneId+" formLayoutId,layoutHtml");
		sql.append("\n	 from formLayoutHtml where comid="+comId+" and  formModId="+formModId+"  and formLayoutId= "+formLayoutId);
		sql.append("\n	 )");
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 云表单使用
	 * @param comId 当前团队号
	 * @param cloudeFormId 云表单主键
	 * @param cloudeFormLayoutId 云表单布局主键
	 * @param formModId 克隆后关联表单主键
	 * @param formLayoutId 克隆后关联布局主键
	 */
	public void cloudFormConfClone(Integer comId,Integer cloudeFormId, Integer cloudeFormLayoutId,
			Integer formModId, Integer formLayoutId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n	insert into formLayoutHtml(comid,formModId,formLayoutId,layoutHtml) ");
		sql.append("\n	 (select "+comId+" comid,"+formModId+" formModId,"+formLayoutId+" formLayoutId,layoutHtml ");
		sql.append("\n	 from formLayoutHtml where comid=0 and  formModId="+cloudeFormId+"  and formLayoutId= "+cloudeFormLayoutId);
		sql.append("\n	 )");
		this.excuteSql(sql.toString(), args.toArray());
	}
	
	/**
	 * 云表单使用
	 * @param comId 当前团队号
	 * @param cloudeFormId 云表单主键
	 * @param cloudeFormLayoutId 云表单布局主键
	 * @param formModId 克隆后关联表单主键
	 * @param formLayoutId 克隆后关联布局主键
	 */
	public void cloudFormHtmlClone(Integer comId,Integer cloudeFormId, Integer cloudeFormLayoutId,
			Integer formModId, Integer formLayoutId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n	insert into formConf(comid,formModId,formLayoutId,fieldId ,confName,confValue) ");
		sql.append("\n	 (select "+comId+" comid,"+formModId+" formModId,"+formLayoutId+" formLayoutId,fieldId,confName,confValue ");
		sql.append("\n	 from formConf where comid=0 and  formModId="+cloudeFormId+"  and formLayoutId= "+cloudeFormLayoutId);
		sql.append("\n	 )");
		this.excuteSql(sql.toString(), args.toArray());
	}
	/**
	 * 分页查询云表单列表
	 * @param formMod
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormMod> listPagedCloudFormMod(FormMod formMod) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select a.id,a.recordCreateTime,a.comId,a.modName,a.enable,");
 		sql.append("\n case when b.id is null then 0 else b.id end formSortId,");
 		sql.append("\n case when b.id is null then '其他' else  b.sortname end modSortName");
 		sql.append("\n from formMod a left join formModSort b on a.comid=b.comid and a.formsortid=b.id");
 		sql.append("\n where a.comId=0");
 		this.addSqlWhere(formMod.getFormSortId(), sql, args, " and a.formSortId=?");
 		this.addSqlWhere(formMod.getEnable(), sql, args, " and a.enable=?");
 		this.addSqlWhereLike(formMod.getModName(), sql, args, " and a.modName like ?");
		return this.pagedQuery(sql.toString(), " a.enable desc,a.id desc", args.toArray(), FormMod.class);
	}
	/**
	 * 验证表单是否同名
	 * @param comId
	 * @param modName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormMod> checkFormModName(Integer comId, String modName) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select * from formmod a where 1=1");
 		this.addSqlWhere(comId, sql, args, " and a.comid=?");
 		this.addSqlWhere(modName, sql, args, " and a.modName=?");
 		return this.listQuery(sql.toString(), args.toArray(), FormMod.class);
	}
	/**
	 * 查询布局基本信息
	 * @param formLayoutId
	 * @return
	 */
	public FormLayoutHtml queryHtmlByLayoutId(Integer formLayoutId) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select * from formLayoutHtml a where 1=1");
 		this.addSqlWhere(formLayoutId, sql, args, " and a.formLayoutId=?");
 		return (FormLayoutHtml) this.objectQuery(sql.toString(), args.toArray(), FormLayoutHtml.class);
 		
	}
	/**
	 * 查询映射关系需要必填的组件
	 * @param instanceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BusAttrMapFormColTemp> listRequireCol(Integer instanceId) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select * from busAttrMapFormColTemp a where isRequire=1");
 		this.addSqlWhere(instanceId, sql, args, " and a.instanceId=?");
		return this.listQuery(sql.toString(), args.toArray(), BusAttrMapFormColTemp.class);
	}
	/**
	 * 查询数据表信息
	 * @param tableName 表的名称
	 * @param columns 需要显示的字段
	 * @return
	 */
	public List<TableInfoVo> listColumnInfo(String tableName, Set<String> columns) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select a.table_name r1, a.column_name columnName, a.comments columnDesc");
 		sql.append("\n from user_col_comments a where 1=1");
 		this.addSqlWhere(tableName.toLowerCase(), sql, args, "\n and lower(Table_Name)=?");
 		this.addSqlWhereIn(columns.toArray(), sql, args, "\n and lower(a.column_name) in ?");
		return this.listQuery(sql.toString(),args.toArray(), TableInfoVo.class);
	}
	/**
	 * 查询字典表数据
	 * @param comId 
	 * @param sysDicName
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> listSysDicInfo(Integer comId, String sysDicName, Class clz) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select a.* ");
 		sql.append("\n from "+sysDicName+" a ");
 		sql.append("\n where 1=1 and a.comId=? ");
 		args.add(comId);
 		sql.append("\n order by a.id desc ");
 		
		return this.listQuery(sql.toString(), args.toArray(), clz);
	}
	/**
	 * 查询子表单对应的数据信息
	 * @param userInfo 当前操作人员
	 * @param instanceId 审批流程主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FormCompon> listDataTableCompone(UserInfo userInfo,
			Integer instanceId) {
		List<Object> args = new ArrayList<Object>();
 		StringBuffer sql = new StringBuffer();
 		sql.append("\n select compone.*   ");
 		sql.append("\n from  spFlowInstance a");
 		sql.append("\n inner join formlayout layout on a.formversion=layout.version and a.formkey=layout.formmodid  ");
 		sql.append("\n inner join formMod on a.comid=formMod.comid and a.formKey=formMod.id and layout.formModId=formMod.id  ");
 		sql.append("\n inner join formcompon compone on a.formkey=compone.formmodid and layout.id=compone.formlayoutid  ");
 		sql.append("\n where a.comId=? and a.id=? and lower(compone.componentkey)='datatable'");
 		args.add(userInfo.getComId());
 		args.add(instanceId);
		return this.listQuery(sql.toString(), args.toArray(), FormCompon.class);
	}
	

}
