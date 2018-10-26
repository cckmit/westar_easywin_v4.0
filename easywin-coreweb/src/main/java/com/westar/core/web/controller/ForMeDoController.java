package com.westar.core.web.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.westar.core.service.ForMeDoService;

/**
 * 
 * 描述: 替岗操作控制类
 * @author zzq
 * @date 2018年8月25日 上午11:50:48
 */
@Controller
@RequestMapping("/ForMeDo")
public class ForMeDoController extends BaseController{

	@Autowired
	ForMeDoService forMeDoService;
	
	
	
}