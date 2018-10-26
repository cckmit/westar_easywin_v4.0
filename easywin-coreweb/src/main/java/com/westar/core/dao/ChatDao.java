package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.ChatMsg;
import com.westar.base.model.ChatRoom;
import com.westar.base.model.ChatUser;
import com.westar.base.model.ChatsMsg;
import com.westar.base.model.ChatsNoRead;
import com.westar.base.model.ChatsRoom;
import com.westar.base.model.UserInfo;
import com.westar.base.util.DateTimeUtil;

@Repository
public class ChatDao extends BaseDao {

	/**
	 * 查询聊天室是否已存在
	 * @param comId 企业号
	 * @param busType 业务类型
	 * @param busId 业务主键
	 * @param chatType 是否群聊
	 * @param chater 聊天成员
	 * @return
	 */
	public ChatRoom getChatRoomByType(Integer comId, String busType,
			Integer busId, String chatType, String chater) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select a.* from chatRoom a where 1=1 ");
		this.addSqlWhere(busId,sql,args," and a.busId=?");
		this.addSqlWhere(busType,sql,args," and a.busType=?");
		if(null!=chatType && chatType.equals("1")){//是群聊
			this.addSqlWhere(chatType,sql,args," and a.chatType=?");
		}else{//不是群聊
			this.addSqlWhere(chater,sql,args," and a.chater=?");
		}
		return (ChatRoom) this.objectQuery(sql.toString(),args.toArray(),ChatRoom.class);
	}

	/**
	 * 查询业务的聊天室
	 * @param userInfo
	 * @param chatRoom
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ChatRoom> listBusChat(UserInfo userInfo, ChatRoom chatRoom) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,b.noreadnum from chatroom a inner join chatuser b on a.comid=b.comid ");
		sql.append("\n and a.id=b.roomid and b.userid="+userInfo.getId()+"  where 1=1 ");
		this.addSqlWhere(userInfo.getComId(),sql,args," and a.comId=?");
		this.addSqlWhere(chatRoom.getBusId(),sql,args," and a.busId=?");
		this.addSqlWhere(chatRoom.getBusType(),sql,args," and a.busType=?");
		sql.append("\n order by a.id desc");
		return this.listQuery(sql.toString(),args.toArray(),ChatRoom.class);
	}

	/**
	 * 取得聊天记录
	 * @param userInfo 操作员
	 * @param roomId 房间号
	 * @param minId 页面显示的最小主键
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param num 展示的条数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ChatMsg> listChatMsg(UserInfo userInfo, Integer roomId,
			Integer minId, Integer busId, String busType, Integer num) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,rownum from ( ");
		sql.append("\n select a.*,c.username, ");
		sql.append("\n case when a.recordcreatetime>='"+DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd)+"' then 1 else 0end istoday ");
		sql.append("\n from chatMsg a inner join chatroom b on a.comid=b.comid and a.roomid=b.id");
		sql.append("\n left join userinfo c on a.userid=c.id ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		this.addSqlWhere(roomId, sql, args, " and a.roomId=?");
		sql.append("\n order by a.id desc");
		sql.append("\n )a where rownum<="+num);
		if(null!=minId && minId>0){
			sql.append("\n and  a.id<"+minId+"");
		}
		sql.append("\n order by rownum");
		return this.listQuery(sql.toString(), args.toArray(), ChatMsg.class);
	}

	/**
	 * 记录没有加载数
	 * @param userInfo操作员
	 * @param roomId 房间号
	 * @param newMinId 新的最小主键
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param chatType 聊天类型
	 * @return
	 */
	public Integer countLeft(UserInfo userInfo, Integer roomId,
			Integer newMinId, Integer busId, String busType) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select count(*) ");
		sql.append("\n from chatMsg a inner join chatroom b on a.comid=b.comid and a.roomid=b.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(userInfo.getComId(), sql, args, " and a.comid=?");
		this.addSqlWhere(roomId, sql, args, " and a.roomId=?");
		this.addSqlWhere(newMinId, sql, args, " and a.id<?");
		return this.countQuery(sql.toString(), args.toArray());
	}

	/**
	 * 聊天室成员
	 * @param comId 企业号
	 * @param roomId 房间号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ChatUser> getChatUserByRoomId(Integer comId, Integer roomId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,d.username from chatuser a ");
		sql.append("\n inner join chatroom b on a.roomid=b.id and a.comid=b.comid");
		sql.append("\n inner join userorganic c on a.comid=c.comid and a.userid=c.userid and c.enabled=1");
		sql.append("\n left join userinfo d on c.userid=d.id");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(roomId, sql, args, " and a.roomId=?");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		return this.listQuery(sql.toString(), args.toArray(), ChatUser.class);
	}

	/**
	 * 需要生成日志的对话信息
	 * @param comId 企业号
	 * @param roomId 房间号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ChatMsg> listChatMsgForLog(Integer comId, Integer roomId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*,c.username ");
		sql.append("\n from chatMsg a inner join chatroom b on a.comid=b.comid and a.roomid=b.id");
		sql.append("\n left join userinfo c on a.userid=c.id ");
		sql.append("\n where a.islogged=0 ");
		this.addSqlWhere(comId, sql, args, " and a.comid=?");
		this.addSqlWhere(roomId, sql, args, " and a.roomId=?");
		sql.append("\n order by a.id");
		return this.listQuery(sql.toString(), args.toArray(), ChatMsg.class);
	}

	/**
	 * 将聊天记录设置成
	 * @param comId 企业号
	 * @param roomId 房间号
	 */
	public void updateMsgLog(Integer comId, Integer roomId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("update chatMsg set isLogged=1 where comid=? and roomId=? and  isLogged=0");
		args.add(comId);
		args.add(roomId);
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 取得聊天人员信息
	 * @param comId 企业号
	 * @param roomId 房间号
	 * @param userId 聊天人员主键
	 * @return
	 */
	public ChatUser getChatUserByUserId(Integer comId, Integer roomId,
			Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from chatUser a where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(roomId, sql, args, " and a.roomId=?");
		this.addSqlWhere(userId, sql, args, " and a.userId=?");
		return (ChatUser) this.objectQuery(sql.toString(), args.toArray(), ChatUser.class);
	}

	/**
	 * 取得业务的所有聊天室
	 * @param comId 企业号
	 * @param busType 业务类型
	 * @param busId 业务主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ChatRoom> listChatRoomByType(Integer comId, String busType,
			Integer busId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.*  from chatroom a ");
		sql.append("\n where 1=1 ");
		this.addSqlWhere(comId,sql,args," and a.comId=?");
		this.addSqlWhere(busId,sql,args," and a.busId=?");
		this.addSqlWhere(busType,sql,args," and a.busType=?");
		sql.append("\n order by a.id desc");
		return this.listQuery(sql.toString(), args.toArray(), ChatRoom.class);
	}

	/**
	 * 合并聊天室
	 * @param comId
	 * @param oldBusId
	 * @param newBusId
	 * @param busType
	 * @param oldRoomId 
	 */
	public void comPressChat(Integer comId, Integer oldBusId, Integer newBusId,
			String busType, Integer oldRoomId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并聊天室
		sql.append("update chatRoom set busId=? where comid=? and busId=? and busType=?");
		if(null!=oldRoomId ){
			sql.append(" and id=?");
		}
		args.add(newBusId);
		args.add(comId);
		args.add(newBusId);
		args.add(busType);
		if(null!=oldRoomId ){
			args.add(oldRoomId);
		}
		this.excuteSql(sql.toString(), args.toArray());
	}

	/**
	 * 合并聊天内容
	 * @param comId
	 * @param oldRoomId
	 * @param newRoomId
	 */
	public void comPressChatMsg(Integer comId,Integer oldRoomId,
			Integer newRoomId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//合并聊天内容
		sql.append("update chatMsg set roomId=? where comid=? and roomId=?");
		args.add(newRoomId);
		args.add(comId);
		args.add(oldRoomId);
		this.excuteSql(sql.toString(), args.toArray());
	}
/****************************************************************************/
	/**
	 * 根据聊天类型查询聊天室
	 * @param comId 
	 * @param chatsType 聊天类型 friend为单聊 group为群聊
	 * @param fromUser 单聊则为
	 * @param toUser
	 */
	public ChatsRoom getChatsRoomId(Integer comId, String chatsType, Integer fromUser,
			Integer toUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		if("friend".equals(chatsType)){
			sql.append("select a.* from chatsroom a inner join chatsGrpUser b on a.id=b.roomid and a.comid=b.comid");
			sql.append(" where b.chatsgrpid=0 ");
			this.addSqlWhere(comId, sql, args, " and a.comid=?");
			this.addSqlWhere(fromUser, sql, args, " and a.creater=?");
			this.addSqlWhere(toUser, sql, args, " and  b.memberid=?");
		}else{
			sql.append("select a.* from chatsroom a inner join chatsGrp b on a.id=b.roomid and a.comid=b.comid ");
			sql.append(" where 1=1 ");
			this.addSqlWhere(comId, sql, args, " and a.comid=?");
			this.addSqlWhere(toUser, sql, args, " and b.id=?");
		}
		return (ChatsRoom) this.objectQuery(sql.toString(), args.toArray(), ChatsRoom.class);
	}
	/**
	 * 取得人员消息未读
	 * @param comId 企业号
	 * @param roomId 聊天室主键
	 * @param toUser 人员主键
	 * @return
	 */
	public ChatsNoRead getChatsNoReadByUserId(Integer comId, Integer roomId,
			Integer toUser) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("\n select a.* from chatsNoRead a where 1=1 ");
		this.addSqlWhere(comId, sql, args, " and a.comId=?");
		this.addSqlWhere(roomId, sql, args, " and a.roomId=?");
		this.addSqlWhere(toUser, sql, args, " and a.userId=?");
		return (ChatsNoRead) this.objectQuery(sql.toString(), args.toArray(), ChatsNoRead.class);
	}

	/**
	 * 查询连续三天的数据
	 * @param roomId
	 * @param comId
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public List<ChatsMsg> listChatsMsg(Integer roomId, Integer comId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		 
		sql.append("\n SELECT A.RECORDCREATETIME,A.ROOMID,A.CONTENT,A.MSGFROM,U.GENDER,U.USERNAME,B.UUID,B.FILENAME ");
		sql.append("\n FROM CHATSMSG A INNER JOIN  USERORGANIC AA ON A.COMID=AA.COMID AND A.MSGFROM=AA.USERID ");
		sql.append("\n INNER JOIN  USERINFO U ON AA.USERID=U.ID ");
		sql.append("\n LEFT JOIN UPFILES B ON AA.BIGHEADPORTRAIT=B.ID AND B.COMID=AA.COMID ");
		sql.append("\n WHERE A.RECORDCREATETIME>=(");
		sql.append("\n SELECT MIN(A.DATEA) LASTDATE FROM (");
		sql.append("\n SELECT DISTINCT SUBSTR(A.RECORDCREATETIME,0,10) AS DATEA  FROM CHATSMSG A");
		sql.append("\n WHERE COMID=? AND ROOMID=?");
		sql.append("\n ORDER BY DATEA DESC");
		sql.append("\n ) A WHERE ROWNUM<=3");
		sql.append("\n ) AND A.COMID=? AND A.ROOMID=?");
		sql.append("\n ORDER BY A.ID ASC");
		
		args.add(comId);
		args.add(roomId);
		args.add(comId);
		args.add(roomId);
		return this.listQuery(sql.toString(), args.toArray(), ChatsMsg.class);
		
	}
	/**
	 * 分页取得聊天历史记录
	 * @param roomId 聊天室主键
	 * @param comId 企业号
	 * @param content 模糊查询
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ChatsMsg> listPagedChatsMsg(Integer roomId, Integer comId, ChatsMsg chatsMsg) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		 
		sql.append("\n SELECT A.RECORDCREATETIME,A.ROOMID,A.CONTENT,A.MSGFROM,U.GENDER,U.USERNAME,B.UUID,B.FILENAME ");
		sql.append("\n FROM CHATSMSG A INNER JOIN  USERORGANIC AA ON A.COMID=AA.COMID AND A.MSGFROM=AA.USERID ");
		sql.append("\n INNER JOIN  USERINFO U ON AA.USERID=U.ID ");
		sql.append("\n LEFT JOIN UPFILES B ON AA.BIGHEADPORTRAIT=B.ID AND B.COMID=AA.COMID ");
		sql.append("\n WHERE 1=1");
		sql.append("\n AND A.COMID=? AND A.ROOMID=?");
		args.add(comId);
		args.add(roomId);
		this.addSqlWhereLike(chatsMsg.getContent(), sql, args, " and A.CONTENT like ?");
		return this.pagedQuery(sql.toString(), " A.ID　DESC", args.toArray(), ChatsMsg.class);
	}
	/**
	 * 取得聊天室没有读取的消息
	 * @param comId 企业号
	 * @param userId 当前操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ChatsNoRead> listChatsNoRead(Integer comId, Integer userId) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//单聊未读消息
		sql.append("\n SELECT A.*,C.MEMBERID AS FROMUSER,B.CHATTYPE FROM CHATSNOREAD A ");
		sql.append("\n INNER JOIN CHATSROOM B ON A.COMID=B.COMID AND A.ROOMID=B.ID ");
		sql.append("\n INNER JOIN CHATSGRPUSER C ON A.COMID=C.COMID AND A.ROOMID=C.ROOMID"); 
		sql.append("\n WHERE A.NOREADNUM>0 AND A.COMID=? AND C.CHATSGRPID=0 AND A.USERID=? AND B.CREATER=?");
		sql.append("\n UNION ALL");
		//单聊未读消息
		sql.append("\n SELECT A.*,B.CREATER AS FROMUSER,B.CHATTYPE FROM CHATSNOREAD A"); 
		sql.append("\n INNER JOIN CHATSROOM B ON A.COMID=B.COMID AND A.ROOMID=B.ID ");
		sql.append("\n INNER JOIN CHATSGRPUSER C ON A.COMID=C.COMID AND A.ROOMID=C.ROOMID ");
		sql.append("\n WHERE A.NOREADNUM>0 AND A.COMID=? AND C.CHATSGRPID=0 AND A.USERID=? AND C.MEMBERID=?");
		sql.append("\n UNION ALL");
		//群聊未读消息
		sql.append("\n SELECT A.*,C.ID AS FROMUSER,B.CHATTYPE FROM CHATSNOREAD A INNER JOIN CHATSROOM B ON A.COMID=B.COMID AND A.ROOMID=B.ID");
		sql.append("\n INNER JOIN CHATSGRP C ON A.COMID=C.COMID AND A.ROOMID=C.ROOMID ");
		sql.append("\n WHERE A.NOREADNUM>0 AND A.COMID=? AND A.USERID=?");
		
		args.add(comId);
		args.add(userId);
		args.add(userId);
		args.add(comId);
		args.add(userId);
		args.add(userId);
		args.add(comId);
		args.add(userId);
		return this.listQuery(sql.toString(), args.toArray(), ChatsNoRead.class);
	}
}
