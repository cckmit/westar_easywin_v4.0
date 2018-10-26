package com.westar.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.westar.base.model.DataDic;
import com.westar.base.util.StringUtil;
import com.westar.core.dao.DataDicDao;
import com.westar.core.web.DataDicContext;

@Service
public class DataDicService {

	@Autowired
	DataDicDao dataDicDao;

	/**
	 * 增加字典表
	 * @param dataDic
	 * @return 返回主键ID
	 */
	public Integer addDataDic(DataDic dataDic) {
		return dataDicDao.addDataDic(dataDic);
	}

	/**
	 * 根据id删除字典表
	 * @param id 字典表主键ID
	 */
	public void delDataDic(Integer id) {
		dataDicDao.delDataDic(id);
	}

	/**
	 * 根据id批量删除字典表
	 * @param ids  字典表主键ID
	 */
	public void delDataDic(Integer[] ids) {
		dataDicDao.delDataDic(ids);
	}

	/**
	 * 更新字典表
	 * @param dataDic
	 */
	public void updateDataDic(DataDic dataDic) {
		dataDicDao.updateDataDic(dataDic);
	}

	/**
	 * 查询字典表数量
	 * @return 查询的数量
	 */
	public int countDataDic() {
		return dataDicDao.countDataDic();
	}

	/**
	 * 根据id查询字典表
	 * @param id 主键ID
	 * @return  字典表信息
	 */
	public DataDic getDataDicById(Integer id) {
		return dataDicDao.getDataDicById(id);
	}

	/**
	 * 查询字典表列表
	 * @param dataDic
	 * @return 字典表信息集合
	 */
	public List<DataDic> listDataDic(DataDic dataDic) {
		return dataDicDao.listDataDic(dataDic);
	}

	/**
	 * 查询字典表树形列表
	 * @return 字典表信息集合
	 */
	public List<DataDic> listTreeDataDic() {
		return dataDicDao.listTreeDataDic();
	}

	/**
	 * 根据类型和代码查询字典表树形列表
	 * @param type 类别
	 * @param code 字典代码
	 * @return  字典表信息集合
	 */
	public List<DataDic> listTreeDataDicByTypeAndCode(String type, String code) {
		return dataDicDao.listTreeDataDicByTypeAndCode(type, code);
	}

	/**
	 * 根据类型和代码获得子级所有代码
	 * @param type 类别
	 * @param code  字典代码
	 * @return  子级所有字典代码
	 */
	public String[] getChildrenCodeByTypeAndCode(String type, String code) {
		String[] childrenCode = null;
		List<DataDic> listDataDic = this.listTreeDataDicByTypeAndCode(type, code);
		if (listDataDic != null && listDataDic.size() > 0) {
			childrenCode = new String[listDataDic.size()];
			for (int i = 0; i < listDataDic.size(); i++) {
				childrenCode[i] = listDataDic.get(i).getCode();
			}
		}
		return childrenCode;
	}

	/**
	 * 初始化字典表（系统字典表）
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void initDataDic() throws Exception {
		// 删除数据库中存在的字典表
		DataDic temp = new DataDic();
		temp.setMaintainable("0");
		List<DataDic> listDataDic = this.listDataDic(temp);
		if (listDataDic != null && listDataDic.size() > 0) {
			Integer[] ids = new Integer[listDataDic.size()];
			for (int i = 0; i < listDataDic.size(); i++) {
				ids[i] = listDataDic.get(i).getId();
			}
			dataDicDao.delDataDic(ids);
		}

		// 解析XML并将数据字典信息存入数据库
		Resource resource = new ClassPathResource("/datadic.xml");
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(resource.getFile());
		List<Element> elistDataDicType = document.selectNodes("//root/DataDicType");
		for (Element eDataDicType : elistDataDicType) {
			String type = eDataDicType.attributeValue("type");
			String zdescribe = eDataDicType.attributeValue("zdescribe");
			recursiveReadDataDic(eDataDicType, -1, "_p", 1, type, zdescribe);
		}
	}

	/**
	 * 递归读取XML并保存字典数据到数据库
	 * @param eDataDic
	 * @param parentId 父级字典信息的主键ID
	 * @param type  类别
	 * @param zdescribe  描述
	 */
	@SuppressWarnings("unchecked")
	private void recursiveReadDataDic(Element eDataDic, Integer parentId, String parentCode, int level, String type, String zdescribe) {
		DataDicContext dataDicContext = DataDicContext.getInstance();
		String code = eDataDic.attributeValue("code");
		String zvalue = eDataDic.attributeValue("zvalue");
		DataDic dataDic = new DataDic();
		dataDic.setMaintainable("0"); // 不可维护（系统字典表）
		dataDic.setParentId(parentId);
		dataDic.setType(type);
		dataDic.setZdescribe(zdescribe);
		dataDic.setCode(code);
		dataDic.setZvalue(zvalue);
		Integer id = dataDicDao.addDataDic(dataDic);
		dataDic.setLevel(level);
		dataDic.setId(id);

		// 初始化字典表内容到内存中
		Map<String, List<DataDic>> mapList = dataDicContext.getMapList();
		List<DataDic> ls = mapList.get(type);
		if (ls == null) {
			ls = new ArrayList<DataDic>();
		}
		ls.add(dataDic);
		mapList.put(type, ls);
		/****/
		Map<String, Map<String, List<DataDic>>> mapTree = dataDicContext.getMapTree();
		Map<String, List<DataDic>> map = mapTree.get(type);
		if (map == null) {
			map = new HashMap<String, List<DataDic>>();
		}
		List<DataDic> list = map.get(parentCode);
		if (list == null) {
			list = new ArrayList<DataDic>();
		}
		list.add(dataDic);
		map.put(parentCode, list);
		mapTree.put(type, map);
		/****/
		Map<String, Map<String, DataDic>> mapMap = dataDicContext.getMapMap();
		Map<String, DataDic> mapStr = mapMap.get(type);
		if (mapStr == null) {
			mapStr = new HashMap<String, DataDic>();
		}
		mapStr.put(code, dataDic);
		mapMap.put(type, mapStr);

		List<Element> children = eDataDic.selectNodes("DataDic");
		if (children != null && children.size() > 0) {
			level++;
			if (StringUtil.isBlank(code) && level == 1) {
				code = "_m";
			}
			for (Element eDataDic_child : children) {
				recursiveReadDataDic(eDataDic_child, id, code, level, type, zdescribe);
			}
		}
	}

	/**
	 * 初始化数据字典上下文
	 */
	// public void initDataDicContext() {
	// DataDicContext dataDicContext = DataDicContext.getInstance();
	// // dataDicContext.setListTreeDataDic(this.listTreeDataDic());
	// }
	
}
