package com.westar.core.service;

import java.util.ArrayList;
import java.util.List;

import com.westar.base.model.FileDetail;
import com.westar.base.model.ProTalkUpfile;
import com.westar.base.model.ProVerTree;
import com.westar.base.model.Upfiles;
import com.westar.core.dao.FileCenterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.ProLog;
import com.westar.base.model.ProUpFiles;
import com.westar.base.model.Product;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;
import com.westar.core.dao.ProductDao;

@Service
public class ProductService {

	@Autowired
	ProductDao productDao;

	@Autowired
	FileCenterService fileCenterService;

    public Integer addProLog(Integer comId,Integer itemId,Integer userId,String userName,String content){
		return 0;
	}

	public List<ProUpFiles> listPagedProUpfile(ProUpFiles itemUpfile, Integer comId){
		return null;
	}

	public List<ProLog> listProLog(Integer proId, Integer comId){
		return null;
	}
	public boolean authorCheck(Integer comId,Integer proId,Integer userId){
		return true;
	}

	public List<UserInfo> listProCreatorsNoForce(Integer comId, Integer modId){
		return null;
	}

	public boolean authorCheck(UserInfo userInfo,Integer itemId){
		return true;
	}

	/**
	 * 分页获取产品
	 * @param product
	 * @param userInfo
	 * @return
	 */
	public List<Product> listPagedPro(Product product, UserInfo userInfo,boolean isAuthorized){
		return productDao.listPagedPro(product,userInfo,isAuthorized);
	}


	/**
	 * 添加产品
	 * @param product
	 * @param userInfo
	 * @return
	 */
	public Integer addPro(Product product,UserInfo userInfo){

		product.setComId(userInfo.getComId());
		product.setCreator(userInfo.getId());
		product.setState(0);
		Integer id =  productDao.add(product);

		if(null != id && id > 0){

			//版本升级添加到树结构
			if(null != product.getParentVer()){
				ProVerTree proVerTree = new ProVerTree();
				proVerTree.setParent(product.getParentVer());
				proVerTree.setVersion(product.getVersion());
				proVerTree.setComId(userInfo.getComId());
				proVerTree.setProId(id);
				productDao.add(proVerTree);
			}
			//产品插入版本
			if(null != product.getChildVer()){
				ProVerTree childVer = new ProVerTree();
				childVer.setProId(id);
				childVer.setParent(product.getVersion());
				childVer.setVersion(product.getChildVer());
				childVer.setComId(userInfo.getComId());
				productDao.add(childVer);
			}

			//存放产品附件
			List<Upfiles> upFiles = product.getListUpfiles();
			List<Integer> listFileId = null;
			if(null != upFiles && upFiles.size() > 0){
				ProUpFiles proUpFiles = null;
				listFileId = new ArrayList<>();
				for(Upfiles upfiles : upFiles){
					proUpFiles = new ProUpFiles();
					proUpFiles.setProId(id);
					proUpFiles.setComId(userInfo.getComId());
					proUpFiles.setUpFileId(upfiles.getId());
					proUpFiles.setUploader(userInfo.getId());
					productDao.add(proUpFiles);
				}
			}
			//归档到文档中心
			fileCenterService.addModFile(userInfo,listFileId,product.getName());

			//添加日志
			ProLog log = new ProLog();
			log.setProId(id);
			log.setOperator(userInfo.getId());
			log.setComId(userInfo.getComId());
			log.setContent("\"" + userInfo.getUserName() + "\"创建了产品\"" + product.getName() + "\"");
			productDao.add(log);
		}

		return id;
	}

	/**
	 * 根据id获取产品
	 * @param id
	 * @return
	 */
	public Product getProductById(Integer id){
		return (Product) productDao.objectQuery(Product.class,id);
	}
	
	/**
	 * 分页查询用于选择的产品
	 * @author hcj 
	 * @date: 2018年10月16日 上午10:00:40
	 * @param product
	 * @param userInfo
	 * @return
	 */
	public PageBean<Product> listPagedProductForSelect(Product product, UserInfo userInfo){
		return productDao.listPagedProductForSelect(product,userInfo);
	}

	/**
	 * 根据id获取需要展示的详情
	 * @param id
	 * @return
	 */
	public Product getProDetailById(Integer id,Integer comId){
		return productDao.getProductById(id,comId);
	}

	/**
	 * 修改页面产品更新
	 * @param product
	 */
	public void proAjaxUpdate(Product product,UserInfo userInfo){
		productDao.update(product);
        //添加日志
        ProLog log = new ProLog();
        log.setProId(product.getId());
        log.setOperator(userInfo.getId());
        log.setComId(userInfo.getComId());
        log.setContent("\"" + userInfo.getUserName() + "\"更新了产品\"" + product.getName() + "\"");
        productDao.add(log);
	}

	/**
	 * 获取所有文档
	 * @param proUpFiles
	 * @param comId
	 * @return
	 */
	public List<ProUpFiles> listProUpFiles(ProUpFiles proUpFiles,Integer comId){
		return productDao.listProUpFiles(proUpFiles,comId);
	}

	/**
	 * 删除附件
	 * @param id
	 * @param type 该附件来源
	 */
	public void delProUpFile(Integer id,String type,UserInfo userInfo){
        Upfiles upfiles = null;
	    if("talk".equals(type)){
	        ProTalkUpfile proTalkUpfile = (ProTalkUpfile) productDao.objectQuery(ProTalkUpfile.class,id);
            upfiles = (Upfiles) productDao.objectQuery(Upfiles.class,proTalkUpfile.getUpfileId());
			//删除关于留言的附件
			productDao.delById(ProTalkUpfile.class,id);
            //添加日志
            ProLog log = new ProLog();
            log.setProId(proTalkUpfile.getProId());
            log.setOperator(userInfo.getId());
            log.setComId(userInfo.getComId());
            log.setContent("\"" + userInfo.getUserName() + "\"移除了附件\"" + upfiles.getFilename() + "\"");
            productDao.add(log);
		}else{
            ProUpFiles proUpFiles = (ProUpFiles) productDao.objectQuery(ProUpFiles.class,id);
            upfiles = (Upfiles) productDao.objectQuery(Upfiles.class,proUpFiles.getUpFileId());
			//删除关于产品的附件
			productDao.delById(ProUpFiles.class,id);
            //添加日志
            ProLog log = new ProLog();
            log.setProId(proUpFiles.getProId());
            log.setOperator(userInfo.getId());
            log.setComId(userInfo.getComId());
            log.setContent("\"" + userInfo.getUserName() + "\"移除了附件\"" + upfiles.getFilename() + "\"");
            productDao.add(log);
		}
	}
}
