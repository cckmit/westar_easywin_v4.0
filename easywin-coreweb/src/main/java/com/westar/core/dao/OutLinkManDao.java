package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.westar.base.model.LinkMan;
import com.westar.base.model.OlmAddress;
import com.westar.base.model.OlmContactWay;
import com.westar.base.model.OlmTalk;
import com.westar.base.model.OlmTalkUpfile;
import com.westar.base.model.OutLinkMan;
import com.westar.base.model.OutLinkManRange;
import com.westar.base.model.UserInfo;

@Repository
public class OutLinkManDao extends BaseDao {
	
	/**
	 * 分页查询外部联系人
	 * @param outLinkMan
	 * @param user 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OutLinkMan> listPagedOutLinkMan(OutLinkMan outLinkMan, UserInfo user) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,case when b.count > 0 then 1 else 0 end used");
		
		sql.append("\n from (select a.* from outLinkMan a where a.pubState =1 ");
		sql.append("\n union all select a.* from outLinkMan a ");
		sql.append("\n where a.pubState =0 and a.id in ");
		sql.append("\n (select outLinkManId from outLinkManRange where userId = ?))a  ");
		sql.append("\n left join (select count(1) count,outLinkManId from olmAndCus group by outLinkManId) b  ");
		sql.append("\n on b.outLinkManId = a.id where 1=1 ");
		
		args.add(user.getId());
		this.addSqlWhere(user.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(outLinkMan.getId(), sql, args, " and a.id=?");
		this.addSqlWhere(outLinkMan.getPubState(), sql, args, " and a.pubState=?");
		this.addSqlWhereLike(outLinkMan.getLinkManName(), sql, args, " and( a.linkManName like ? \n");
		this.addSqlWhereLike(outLinkMan.getLinkManName(), sql, args, " or a.post like ? \n");
		this.addSqlWhereLike(outLinkMan.getLinkManName(), sql, args, " or a.movePhone like ? \n");
		this.addSqlWhereLike(outLinkMan.getLinkManName(), sql, args, " or a.email like ? \n");
		this.addSqlWhereLike(outLinkMan.getLinkManName(), sql, args, " or a.wechat like ? \n");
		this.addSqlWhereLike(outLinkMan.getLinkManName(), sql, args, " or a.linePhone like ? \n");
		this.addSqlWhereLike(outLinkMan.getLinkManName(), sql, args, " or a.qq like ? )\n");
		return this.pagedQuery(sql.toString(), " a.id desc", args.toArray(), OutLinkMan.class);
	}
	
	/**
	 * 查询所有外部联系人范围
	 * @param outLinkManId
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OutLinkManRange> listOutLinkManRanges(Integer outLinkManId, UserInfo user) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,b.userName from outLinkManRange a");
		sql.append("\n left join userInfo b on b.id = a.userId where 1=1");
		this.addSqlWhere(user.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(outLinkManId, sql, args, " and a.outLinkManId=?");
		sql.append("\n order by a.id desc ");
		return this.listQuery(sql.toString() , args.toArray(), OutLinkManRange.class);
	}
	
	
	/**
	 * 导入外部联系人数据用
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LinkMan> listPagedLinkMan(Integer comId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*, crm.owner creatorId from linkMan a");
		sql.append("\n inner join customer crm on a.customerId = crm.id");
		sql.append("\n where 1=1 and a.comId=?");
		args.add(comId);
		return this.pagedQuery(sql.toString(), " a.id", args.toArray(), LinkMan.class);
	}
	/**
	 * 导入外部联系人数据用查询是否导入了该联系人
	 * @param outLinkMan
	 * @return
	 */
	public int countOlm(OutLinkMan outLinkMan) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select count(1) from  outLinkMan a where 1=1");
		this.addSqlWhere(outLinkMan.getComId(), sql, args, " and a.comId=?");
		this.addSqlWhere(outLinkMan.getLinkManName(), sql, args, " and a.linkManName=?");
		this.addSqlWhere(outLinkMan.getPost(), sql, args, " and a.post=?");
		this.addSqlWhere(outLinkMan.getMovePhone(), sql, args, " and a.movePhone=?");
		this.addSqlWhere(outLinkMan.getEmail(), sql, args, " and a.email=?");
		this.addSqlWhere(outLinkMan.getWechat(), sql, args, " and a.wechat=?");
		this.addSqlWhere(outLinkMan.getQq(), sql, args, " and a.qq=?");
		this.addSqlWhere(outLinkMan.getLinePhone(), sql, args, " and a.linePhone=?");
		this.addSqlWhere(outLinkMan.getPubState(), sql, args, " and a.pubState=?");
		this.addSqlWhere(outLinkMan.getCreator(), sql, args, " and a.creator=?");
		return this.countQuery(sql.toString() , args.toArray());
	}
	
	/**
	 * 查询外部联系人联系地址
	 * @param olmId
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OlmAddress> listOlmAddress(Integer olmId, UserInfo user) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from olmAddress a where a.outLinkManId=? and comId=? order by a.id");
		args.add(olmId);
		args.add(user.getComId());
		return this.listQuery(sql.toString() , args.toArray(), OlmAddress.class);
	}
	
	/**
	 * 查询外部联系人联系方式
	 * @param olmId
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OlmContactWay> listOlmContactWay(Integer olmId, UserInfo user) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from olmContactWay a where a.outLinkManId=? and comId=? order by a.id");
		args.add(olmId);
		args.add(user.getComId());
		return this.listQuery(sql.toString() , args.toArray(), OlmContactWay.class);
	}
	
	/**
	 * 查看联系方式详情
	 * @param id
	 * @return
	 */
	public OlmContactWay queryContactWayById(Integer id) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.*,b.ZVALUE contactWayValue from olmContactWay a ");
		sql.append("\n left join DATADIC b on b.CODE = a.contactWayCode and b.type='contactWay'  ");
		sql.append("\n  where a.id = ?");
		args.add(id);
		return (OlmContactWay) this.objectQuery(sql.toString() , args.toArray(), OlmContactWay.class);
	}
	
	/**
	 * 分页查询留言信息
	 * @param outLinkManId
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OlmTalk> listPagedOlmTalk(Integer outLinkManId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select connect_by_isleaf as isLeaf,PRIOR a.speaker as pSpeaker,PRIOR a.content as pContent, \n");
		sql.append("PRIOR b.username as pSpeakerName,a.*,b.username as speakerName,b.gender,c.uuid,c.filename \n");
		sql.append("from olmTalk a \n");
		sql.append("inner join userinfo b on a.speaker = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on  bb.mediumHeadPortrait = c.id \n");
		sql.append("where a.comId=? and a.outLinkManId = ? \n");
		sql.append("start with a.parentid=-1 CONNECT BY PRIOR a.id = a.parentid \n");
		sql.append("order siblings by a.recordcreatetime desc,a.id");
		args.add(comId);
		args.add(outLinkManId);
		return this.pagedQuery(sql.toString(), null, args.toArray(), OlmTalk.class);
	}
	
	/**
	 * 查询所有的留言附件
	 * @param comId
	 * @param outLinkManId
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OlmTalkUpfile> listOlmTalkFile(Integer comId, Integer outLinkManId,Integer olmTalkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.outLinkManId,a.olmTalkId,a.userId,a.upfileId,b.filename,b.uuid,a.recordcreatetime,c.username as creatorName, \n");
		sql.append("c.gender,d.uuid as userUuid,d.filename userFileName,b.fileext,\n");
		sql.append("case when b.fileext in ('gif', 'jpg', 'jpeg', 'png', 'bmp')then 1 else 0 end as isPic \n");
		sql.append("from olmTalkUpfile a inner join upfiles b on a.comid = b.comid and a.upfileid = b.id \n");
		sql.append("left join userinfo c on  a.userid = c.id \n");
		sql.append("inner join userOrganic cc on c.id =cc.userId and a.comId=cc.comId\n");
		sql.append("left join upfiles d on  cc.mediumHeadPortrait = d.id \n");
		sql.append("where a.comid =? and a.outLinkManId = ? \n");
		args.add(comId);
		args.add(outLinkManId);
		this.addSqlWhere(olmTalkId, sql, args, " and a.olmTalkId=?");
		sql.append("order by isPic asc, a.id desc \n");
		return this.listQuery(sql.toString(), args.toArray(), OlmTalkUpfile.class);
	}
	
	/**
	 * 查询是否存在在分享范围内
	 * @param userId
	 * @param outLinkManId
	 * @return
	 */
	public Integer queryOlmRangeCount(Integer userId, Integer outLinkManId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from outLinkManRange where outLinkManId=? and userId=?  \n");
		args.add(outLinkManId);
		args.add(userId);
		return this.countQuery(sql.toString(), args.toArray());
	}
	
	/**
	 * 查询消息推送人员
	 * @param outLinkManId
	 * @param comId
	 * @param pushUserIdSet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listOutLinkManRanges(Integer outLinkManId, Integer comId, Set<Integer> pushUserIdSet) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//分享发布人员
		sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName ");
		sql.append("\n from outLinkMan d ");
		sql.append("\n inner join userinfo b on d.creator=b.id");
		sql.append("\n inner join userorganic c on c.userid=b.id and c.comid=d.comid and c.enabled=1 ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and d.comid=?");
		this.addSqlWhere(outLinkManId, sql, args, " and d.id=?");
		//本次推送的人员
		if(null!=pushUserIdSet && !pushUserIdSet.isEmpty()){
			sql.append("\n union");
			sql.append("\n select b.id,b.email,b.wechat,b.qq,b.userName  ");
			sql.append("\n from userinfo b ");
			sql.append("\n where 1=1 ");
			this.addSqlWhereIn(pushUserIdSet.toArray(new Integer[pushUserIdSet.size()]), sql, args, "\n and b.id in ?");
		}
		return this.listQuery(sql.toString(), args.toArray(), UserInfo.class);
	}
	
	/**
	 * 根据id查询留言详情
	 * @param id
	 * @param comId
	 * @return
	 */
	public OlmTalk queryOlmTalk(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select d.speaker as pSpeaker,d.content as pContent,e.username as pSpeakerName,a.*,b.username as speakerName,b.gender,c.uuid,c.filename from olmTalk a \n");
		sql.append("inner join userinfo b on a.speaker = b.id \n");
		sql.append("inner join userOrganic bb on b.id =bb.userId and a.comId=bb.comId\n");
		sql.append("left join upfiles c on  bb.mediumHeadPortrait = c.id \n");
		sql.append("left join olmTalk d on a.parentid = d.id and a.comid = d.comid \n");
		sql.append("left join userinfo e on d.speaker = e.id \n");
		sql.append("where a.comId=? and a.id = ?");
		args.add(comId);
		args.add(id);
		return (OlmTalk)this.objectQuery(sql.toString(), args.toArray(), OlmTalk.class);
	}
	
	/**
	 * 查询留言
	 * @param comId
	 * @param talkId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OlmTalk> listOlmTalkUpfileForDel(Integer comId, Integer talkId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from olmTalk where comid=? start with id=? connect by parentid = prior id");
		args.add(comId);
		args.add(talkId);
		return this.listQuery(sql.toString(), args.toArray(), OlmTalk.class);
	}
	
	/**
	 * 删除节点讨论及其回复
	 * @param id
	 * @param comId
	 */
	public void delOlmTalk(Integer id,Integer comId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from olmTalk a where a.comid =? and a.id in \n");
		sql.append("(select id from olmTalk start with id=? connect by parentid = prior id)");
		args.add(comId);
		args.add(id);
		this.excuteSql(sql.toString(), args.toArray());
	}
	
	/**
	 * 更新留言及父节点
	 * @param id
	 * @param comId
	 */
	public void updateOlmTalkParentId(Integer id, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update olmTalk set parentId=(select c.parentid \n");
		sql.append("from olmTalk c \n");
		sql.append("where c.id=?) where parentid = ? and comId = ? \n");
		args.add(id);
		args.add(id);
		args.add(comId);
		this.excuteSql(sql.toString(), args.toArray());
	}
	
	/**
	 * 查询留言总数
	 * @param olmId
	 * @param comId
	 * @return
	 */
	public Integer countTalks(Integer olmId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from olmTalk where outLinkManId=? and comId=? \n");
		args.add(olmId);
		args.add(comId);
		return this.countQuery(sql.toString(), args.toArray());
	}
	
	/**
	 * 查询联系方式用于列表显示
	 * @param olmId
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OlmContactWay> listContactWayForShow(Integer olmId, UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.outLinkManId,a.contactWay,b.ZVALUE contactWayValue,a.contactWayCode from olmContactWay a ");
		sql.append("\n left join DATADIC b on b.CODE = a.contactWayCode and b.type='contactWay'  ");
		sql.append("\n where a.outLinkManId=? and comId=?");
		args.add(olmId);
		args.add(userInfo.getComId());
		sql.append("\n union all select a.id outLinkManId,a.linePhone contactWay,'座机' contactWayValue,'1' contactWayCode ");
		sql.append("\n from outLinkMan a ");
		sql.append("\n  where a.id = ? and a.comId = ? and a.linePhone is not null ");
		args.add(olmId);
		args.add(userInfo.getComId());
		sql.append("\n union all select a.id outLinkManId,a.movePhone contactWay,'移动电话' contactWayValue,'3' contactWayCode ");
		sql.append("\n from outLinkMan a ");
		sql.append("\n where a.id = ? and a.comId = ? and a.movePhone is not null ");
		args.add(olmId);
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString() , args.toArray(), OlmContactWay.class);
	}

	/**
	 * 查询联系地址
	 * @param olmId
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OlmAddress> listOlmAddressForShow(Integer olmId, UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.outLinkManId,a.address ");
		sql.append("\n ,case when a.addressCode=0 then '办公地址' when a.addressCode=1 then '家庭地址' else '其他' end  addressValue ");
		sql.append("\n from OlmAddress a ");
		sql.append("\n where a.outLinkManId=? and comId=?");
		args.add(olmId);
		args.add(userInfo.getComId());
		return this.listQuery(sql.toString() , args.toArray(), OlmAddress.class);
	}
}
