package com.westar.core.web.controller;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.pojo.IndexDoc;
import com.westar.base.pojo.IndexView;
import com.westar.core.service.IndexService;
import com.westar.core.web.PaginationContext;

@Controller
@RequestMapping("/index")
public class IndexController extends BaseController {
	@Autowired
	IndexService indexService;

	/**
	 * 为对象创建索引
	 * 
	 * @param indexDoc
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/addIndex")
	public String addIndex(IndexDoc indexDoc) throws Exception {
		if (null == indexDoc){
			return "请输入索引数据";
		}
		boolean flag = indexService.addIndex2_0(null, this.getSessionUser());
		if (flag) {
			return "创建成功";
		} else {
			return "创建失败";
		}
	}

	/**
	 * 在个人权限内检索
	 * @param searchStr
	 * @param busType
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	@RequestMapping("/searchIndexInPersion")
	public ModelAndView searchIndexInPersion(String searchStr, String busType)
			throws IOException, java.text.ParseException, ParseException {
		ModelAndView view = new ModelAndView("/index/listIndexVo");
		// 查询
		List<IndexView> listIndexVo = indexService.searchIndexInPersion(
				this.getSessionUser(), busType, searchStr);
		view.addObject("listIndexVo", listIndexVo);
		view.addObject("searchStr", searchStr);
		view.addObject("busType", busType);
		view.addObject("action","/index/searchIndexInPersion");
		return view;
	}
	/**
	 * 以组范围内索引检索
	 * @param searchStr
	 * @param busType
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	@RequestMapping("/searchIndexInGroup")
	public ModelAndView searchIndexInGroup(String searchStr, String busType)
			throws IOException, java.text.ParseException, ParseException {
		ModelAndView view = new ModelAndView("/index/listIndexVo");
		// 查询
		List<IndexView> listIndexVo = indexService.searchIndexInGroup(
				this.getSessionUser(), busType, searchStr);
		view.addObject("listIndexVo", listIndexVo);
		view.addObject("searchStr", searchStr);
		view.addObject("busType", busType);
		view.addObject("action","/index/searchIndexInGroup");
		return view;
	}


	/**
	 * 在企业内部检索
	 * @param searchStr
	 * @param busType
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	@RequestMapping("/searchIndexInCom")
	public ModelAndView searchIndexInCom(String searchStr)
			throws IOException, java.text.ParseException, ParseException {
		ModelAndView view = new ModelAndView("/index/searchCenter");
		PaginationContext.setPageSize(10);//一页10条数据
		// 查询
		List<IndexView> listIndexVo = indexService.searchIndexInComV2(this.getSessionUser(), searchStr);
		view.addObject("listIndexVo", listIndexVo);
		view.addObject("searchStr", searchStr);
		view.addObject("userInfo",this.getSessionUser());
		view.addObject("action","/index/searchIndexInCom");
		return view;
	}

	/**
	 * 在业务模块下检索
	 * @param searchStr
	 * @param busType
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	@RequestMapping("/searchIndexInModule")
	public ModelAndView searchIndexInModule(String searchStr, String busType)
			throws IOException, java.text.ParseException, ParseException {
		ModelAndView view = new ModelAndView("/index/searchCenter");
		PaginationContext.setPageSize(10);//一页10条数据
		// 查询
		List<IndexView> listIndexVo = indexService.searchIndexInModule(
				this.getSessionUser(), busType, searchStr);
		view.addObject("listIndexVo", listIndexVo);
		view.addObject("searchStr", searchStr);
		view.addObject("busType", busType);
		view.addObject("userInfo",this.getSessionUser());
		view.addObject("action","/index/searchIndexInModule");
		return view;
	}
	/**
	 * 在附件管理中心下进行附件索引查询
	 * @param searchStr
	 * @return
	 * @throws IOException
	 * @throws java.text.ParseException
	 * @throws ParseException
	 */
	@RequestMapping("/searchIndexInFileCenter")
	public ModelAndView searchIndexInFileCenter(String searchStr)
			throws IOException, java.text.ParseException, ParseException {
		ModelAndView view = new ModelAndView("/index/listIndexVo");
		// 查询
		List<IndexView> listIndexVo = indexService.searchIndexInFileCenter(
				this.getSessionUser(), searchStr);
		view.addObject("listIndexVo", listIndexVo);
		view.addObject("searchStr", searchStr);
		view.addObject("action","/index/searchIndexInFileCenter");
		return view;
	}
}
