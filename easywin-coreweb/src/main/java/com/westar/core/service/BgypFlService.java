package com.westar.core.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.westar.base.model.BgypFl;
import com.westar.base.model.BgypItem;
import com.westar.base.model.DataDic;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.BgypFlDao;
import com.westar.core.web.DataDicContext;

@Service
public class BgypFlService {

	@Autowired
	BgypFlDao bgypflDao;

	/**
	 * 取得团队的办公用品分类集合
	 * @param comId 团队号
	 * @return
	 */
	public List<BgypFl> listTreeBgypFl(Integer comId) {
		return bgypflDao.listTreeBgypFl(comId);
	}
	/**
	 * 添加办公用品分类
	 * @param sessionUser 当前操作人员
	 * @param bgypFl 办公用分类
	 * @return
	 */
	public Map<String, Object> addBgypFl(UserInfo sessionUser, BgypFl bgypFl) {
		Map<String, Object> map = new HashMap<String, Object>();
		//办公用品参数信息
		Integer checkBgyp = bgypflDao.countBgypFlByCode(sessionUser.getComId(),0,bgypFl.getFlCode());
		if(checkBgyp.equals(0)){//没有重复的编码
			bgypFl.setComId(sessionUser.getComId());
			bgypflDao.add(bgypFl);
			map.put("status", "y");
		}else{
			map.put("status", "f");
			map.put("info", "编码已存在");
		}
		
		return map;
	}
	
	/**
	 * 根据办公用品分类主键查找信息
	 * @param bgypFlId 办公用品分类主键
	 * @return
	 */
	public BgypFl queryBgypFlById(Integer bgypFlId){
		return  (BgypFl) bgypflDao.objectQuery(BgypFl.class, bgypFlId);
	}
	
	/**
	 * 取得办公用品详情
	 * @param bgypFlId 办公用品分类主键
	 * @return
	 */
	public BgypFl queryBgypFlDetailById(Integer bgypFlId){
		return  bgypflDao.queryBgypFlDetailById(bgypFlId);
	}
	
	
	/**
	 * 修改办公用品分类信息
	 * @param sessionUser 当前操作人员
	 * @param bgypFl 办公用品分类信息
	 * @return
	 */
	public Map<String, Object> updateBgypFl(UserInfo sessionUser, BgypFl bgypFl) {
		Map<String, Object> map = new HashMap<String, Object>();
		//办公用品参数信息
		Integer checkBgyp = bgypflDao.countBgypFlByCode(sessionUser.getComId(),bgypFl.getId(),bgypFl.getFlCode());
		if(checkBgyp.equals(0)){//没有重复的编码
			bgypFl.setComId(sessionUser.getComId());
			bgypflDao.update(bgypFl);
			map.put("status", "y");
		}else{
			map.put("status", "f");
			map.put("info", "编码已存在");
		}
		
		return map;
	}
	/**
	 * 删除办公用品分类
	 * @param sessionUser 当前操作人员
	 * @param bgypFlIds 办公用品分类主键
	 * @return
	 */
	public Map<String, Object> deleteBgypFls(UserInfo sessionUser,
			Integer[] bgypFlIds) {
		Map<String, Object> map = new  HashMap<String, Object>();
		List<BgypFl> listBgypFl4Del = bgypflDao.listBgypFls4Del(sessionUser.getComId(),bgypFlIds);
		if(null!=listBgypFl4Del && !listBgypFl4Del.isEmpty()){
			map.put("status", "f");
			map.put("listBgypFl4Del", listBgypFl4Del);
		}else{
			//将子类的父类关联修改
			bgypflDao.updateSonBgypFl(sessionUser.getComId(),bgypFlIds);
			//删除分类信息
			bgypflDao.delById(BgypFl.class, bgypFlIds);
			map.put("status", "y");
		}
		return map;
	}
	
	public void checkBgFl4Init(Integer comId) throws DocumentException, IOException{
		Integer comBgypFlNum = bgypflDao.countComBgypFl(comId);
		if(0==comBgypFlNum){
			initBgypFl(comId);
		}
	}
	/**
	 * 初始化办公用品分类
	 * @param comId
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void initBgypFl(Integer comId) throws DocumentException, IOException{
		Resource resource = new ClassPathResource("/bgypFlAndDetailMod.xml");
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(resource.getFile());
		List<Element> elistDataDicType = document.selectNodes("//root/bgypfl");
		List<DataDic> dataDics = DataDicContext.getInstance().listTreeDataDicByType("bgypUnit");
		Map<String, String> dataDicsMap = new HashMap<String, String>();
		if(null!=dataDics && !dataDics.isEmpty()){
			for (DataDic dataDic : dataDics) {
				if(dataDic.getParentId()>0){
					dataDicsMap.put(dataDic.getPathZvalue(), dataDic.getCode());
				}
			}
		}
		for (Element eDataDicType : elistDataDicType) {
			String flCode = eDataDicType.attributeValue("flCode");
			String flName = eDataDicType.attributeValue("flName");
			
			BgypFl bgypFl = new BgypFl(); 
			bgypFl.setComId(comId);
			bgypFl.setParentId(-1);
			bgypFl.setFlCode(flCode);
			bgypFl.setFlName(flName);
			Integer bgypFlId = bgypflDao.add(bgypFl);
			recursiveBgypFl(eDataDicType, bgypFlId,comId,dataDicsMap);
			
		}	
	}
	/**
	 * 循环遍历办公分类
	 * @param eDataDic
	 * @param bgypFlPId
	 * @param comId
	 * @param dataDicsMap
	 */
	private void recursiveBgypFl(Element eDataDic, Integer bgypFlPId, 
			Integer comId, Map<String, String> dataDicsMap) {
			
			List<Element> elistDataDicType  = eDataDic.elements();
			if(null!=elistDataDicType && !elistDataDicType.isEmpty()){
				for (Element eDataDicType : elistDataDicType) {
					String flCode = eDataDicType.attributeValue("flCode");
					String flName = eDataDicType.attributeValue("flName");
					if(!StringUtils.isEmpty(flCode)){
						BgypFl bgypFl = new BgypFl(); 
						bgypFl.setComId(comId);
						bgypFl.setParentId(bgypFlPId);
						bgypFl.setFlCode(flCode);
						bgypFl.setFlName(flName);
						Integer bgypFlId = bgypflDao.add(bgypFl);
						recursiveBgypFl(eDataDicType, bgypFlId,comId, dataDicsMap);
					}else{
						String bgypCode = eDataDicType.attributeValue("bgypCode");
						String bgypName = eDataDicType.attributeValue("bgypName");
						String bgypUnitName = eDataDicType.attributeValue("bgypUnit");
						
						BgypItem bgypItem = new BgypItem();
						bgypItem.setComId(comId);
						bgypItem.setBgypCode(bgypCode);
						bgypItem.setBgypName(bgypName);
						bgypItem.setFlId(bgypFlPId);
						bgypItem.setStoreNum(0);
						
						String bgypUnit = dataDicsMap.get(bgypUnitName);
						if(StringUtils.isEmpty(bgypUnit)){
							bgypUnit = "1";
						}
						bgypItem.setBgypUnit(bgypUnit);
						bgypflDao.add(bgypItem);
					}
					
				}	
			}
		}
	
}
