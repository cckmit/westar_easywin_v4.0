package com.westar.core.web.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.westar.core.service.ViewRecordService;

/**
 * 
 * 描述:查看记录控制类
 * @author zzq
 * @date 2018年8月25日 上午11:53:44
 */
@Controller
@RequestMapping("/viewRecord")
public class ViewRecordController extends BaseController{

	@Autowired
	ViewRecordService viewRecordService;
	
	
	
}