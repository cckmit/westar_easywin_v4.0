package com.westar.core.service;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.westar.base.model.BiaoQing;
import com.westar.core.dao.BiaoQingDao;
import com.westar.core.web.BiaoQingContext;

@Service
public class BiaoQingService {

	@Autowired
	BiaoQingDao biaoQingDao;

	/**
	 * 初始化表情表（系统字典表）
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void initBiaoQing() throws Exception {
		// 删除数据库中存在的表情表
		List<BiaoQing> listBiaoQing = biaoQingDao.listBiaoQing();
		if (listBiaoQing != null && listBiaoQing.size() > 0) {
			Integer[] ids = new Integer[listBiaoQing.size()];
			for (int i = 0; i < listBiaoQing.size(); i++) {
				ids[i] = listBiaoQing.get(i).getId();
			}
			biaoQingDao.delBiaoQing(ids);
		}

		// 解析XML并将数据表情信息存入数据库
		Resource resource = new ClassPathResource("/biaoQing.xml");
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(resource.getFile());
		List<Element> elistBiaoQing = document.selectNodes("//root/BiaoQing");
		for (Element eBiaoQing : elistBiaoQing) {
			recursiveReadBiaoQing(eBiaoQing);
		}
	}
	/**
	 * 递归读取XML中的BiaoQing并保存字典数据到数据库
	 * @param eBiaoQing
	 */
	private void recursiveReadBiaoQing(Element eBiaoQing) {
		BiaoQingContext biaoQingContext = BiaoQingContext.getInstance();
		Element imgPathObj = (Element)eBiaoQing.selectObject("imgPath");
		Element imgDescribeObj = (Element)eBiaoQing.selectObject("imgDescribe");
		String imgPath = imgPathObj.attributeValue("zvalue");
		String imgDescribe = imgDescribeObj.attributeValue("zvalue");
		//存数据库表
		BiaoQing biaoQing = new BiaoQing();
		biaoQing.setImgPath(imgPath);
		biaoQing.setImgDescribe(imgDescribe);
		biaoQingDao.add(biaoQing);
		//存入内存
		List<BiaoQing> listBiaoQing = biaoQingContext.getBiaoQingList();
		listBiaoQing.add(biaoQing);
		Map<String, BiaoQing> mapBiaoQing = biaoQingContext.getMapBiaoQing();
		mapBiaoQing.put(imgDescribe, biaoQing);
		
	}
}
