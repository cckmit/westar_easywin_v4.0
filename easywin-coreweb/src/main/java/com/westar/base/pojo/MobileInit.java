package com.westar.base.pojo;

import java.util.List;

/**
 * 移动端主页初始化所需POJO
 * @author lj
 *
 * @param <T>
 */
public class MobileInit<T> {
	
	//分享信息未读
	private int unread_shareMsg;
	//关注更新未读
	private int unread_atten;
	//待办工作
	private int undone_job;
	//数据集
	private List<T> data;
	
	public int getUnread_shareMsg() {
		return unread_shareMsg;
	}
	public void setUnread_shareMsg(int unreadShareMsg) {
		unread_shareMsg = unreadShareMsg;
	}
	public int getUnread_atten() {
		return unread_atten;
	}
	public void setUnread_atten(int unreadAtten) {
		unread_atten = unreadAtten;
	}
	public int getUndone_job() {
		return undone_job;
	}
	public void setUndone_job(int undoneJob) {
		undone_job = undoneJob;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	

}
