package com.westar.core.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.DemandTalk;
import com.westar.base.model.DemandTalkUpfile;
import com.westar.base.model.ProTalk;
import com.westar.base.model.ProTalkUpfile;
import com.westar.base.model.Product;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.BaseTalk;
import com.westar.base.pojo.BaseUpfile;
import com.westar.base.pojo.PageBean;
import com.westar.core.dao.ProductTalkDao;
import com.westar.core.web.PaginationContext;

@Service
public class ProductTalkService {
	
	private static final Logger logger = LoggerFactory .getLogger(ProductTalkService.class);

	@Autowired
	ProductTalkDao productTalkDao;
	
	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	ProductService productService;
	/**
	 * 分页查询留言信息
	 * 
	 * @param sessionUser
	 *            当前操作人员
	 * @param demandId
	 *            需求主键
	 * @return
	 */
	public PageBean<BaseTalk> listPagedProductTalk(UserInfo sessionUser,
			Integer demandId) {
		List<BaseTalk> result = new ArrayList<>();
		//查询留言信息
		List<BaseTalk> recordList = productTalkDao.listProductTalk(demandId,sessionUser.getComId());
		if(null != recordList && !recordList.isEmpty()){
			for (BaseTalk baseTalk : recordList) {
				//查询留言附件
				List<BaseUpfile> listTalkFile = productTalkDao.listTalkFile(sessionUser, demandId, baseTalk.getId());
				baseTalk.setListTalkFile(listTalkFile);
				result.add(baseTalk);
			}
		}
		//封装结果
		PageBean<BaseTalk> pageBean = new PageBean<BaseTalk>();
		pageBean.setRecordList(result);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}

	/**
	 * 添加留言信息
	 * 
	 * @param sessionUser
	 *            当前操作人员
	 * @param baseTalk
	 *            留言信息
	 * @return 
	 */
	public Integer addTalk(UserInfo sessionUser, BaseTalk baseTalk) {
		ProTalk proTalk = new ProTalk();
		BeanUtils.copyProperties(baseTalk,proTalk);
		// 属性复制
		proTalk.setProId(baseTalk.getBusId());
		proTalk.setComId(sessionUser.getComId());
		proTalk.setSpeaker(sessionUser.getId());

		Integer talkId = productTalkDao.add(proTalk);

		List<BaseUpfile> listTalkFile = baseTalk.getListTalkFile();
		if (null != listTalkFile && !listTalkFile.isEmpty()) {
			List<Integer> listUpfile = new ArrayList<Integer>(
					listTalkFile.size());
			for (BaseUpfile baseUpfile : listTalkFile) {
				listUpfile.add(baseUpfile.getUpfileId());

				ProTalkUpfile proTalkUpfile = new ProTalkUpfile();
				proTalkUpfile.setComId(sessionUser.getComId());
				proTalkUpfile.setProId(baseTalk.getBusId());
				proTalkUpfile.setTalkId(talkId);
				proTalkUpfile.setUpfileId(baseUpfile.getUpfileId());
				proTalkUpfile.setUserId(sessionUser.getId());

				productTalkDao.add(proTalkUpfile);

			}
			// 需求查询
			Product product = (Product) productTalkDao.objectQuery(Product.class, baseTalk.getBusId());
			// 添加到文档中心
			fileCenterService.addModFile(sessionUser, listUpfile, product.getName()+product.getVersion());
		}
		// 模块日志添加
		productService.addProLog(sessionUser.getComId(), baseTalk.getBusId(),
				sessionUser.getId(), sessionUser.getUserName(), "添加新评论");
					
		return talkId;

	}
	/**
	 * 查询留言信息
	 * @param sessionUser 当前操作人员
	 * @param talkId 留言主键
	 * @return
	 */
	public BaseTalk queryTalk(UserInfo sessionUser,Integer talkId){
		BaseTalk baseTalk = productTalkDao.queryTalk(sessionUser,talkId);
		//查询留言附件
		List<BaseUpfile> listTalkFile = productTalkDao.listTalkFile(sessionUser, baseTalk.getBusId(), baseTalk.getId());
		baseTalk.setListTalkFile(listTalkFile);
		return baseTalk;
	}
	/**
	 * 删除留言信息
	 * 
	 * @param sessionUser 当前操作人员
	 * @param baseTalk
	 */
	public void delTalk(UserInfo sessionUser, BaseTalk baseTalk) {
		// 产品主键
		Integer proId = baseTalk.getBusId();
		Integer talkId = baseTalk.getId();
		// 查询用于删除的留言信息
		List<ProTalk> listTalk = productTalkDao.listTreeTalkForDel(
				sessionUser, talkId);
		if (null != listTalk && !listTalk.isEmpty()) {
			for (ProTalk demandTalk : listTalk) {
				productTalkDao.delByField("proTalkUpfile",
						new String[] { "comId", "talkId" },
						new Object[] { sessionUser.getComId(),demandTalk.getId() });
				
				productTalkDao.delById(ProTalk.class, demandTalk.getId());
			}
		}
	}
	
	
}
