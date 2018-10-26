package com.westar.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.UsagIdea;
import com.westar.core.dao.UsagIdeaDao;

@Service
public class UsagIdeaService {

	@Autowired
	UsagIdeaDao usagIdeaDao;

	/**
	 * 分页查询常用意见
	 * @param usagIdea
	 * @return
	 */
	public List<UsagIdea> listPagedUsagIdea(UsagIdea usagIdea) {
		List<UsagIdea> list = usagIdeaDao.listPagedUsagIdea(usagIdea);
		return list;
	}

	/**
	 * 通过主键取得常用意见信息
	 * @param id
	 * @return
	 */
	public UsagIdea getUsagIdeaById(Integer id) {
		UsagIdea usagIdea = (UsagIdea) usagIdeaDao.objectQuery(UsagIdea.class, id);
		return usagIdea;
	}

	/**
	 * 修改常用意见
	 * @param usagIdea
	 */
	public void updateUsagIdea(UsagIdea usagIdea) {
		usagIdeaDao.update(usagIdea);
	}

	/**
	 * 
	 * @param usagIdea
	 */
	public void addUsagIdea(UsagIdea usagIdea) {
		usagIdeaDao.add(usagIdea);
		
	}

	/**
	 * 删除常用意见
	 * @param ids
	 */
	public void delUsagIdea(Integer[] ids) {
		for (Integer id : ids) {
			usagIdeaDao.delById(UsagIdea.class, id);
		}
	}
	
	
	
}
