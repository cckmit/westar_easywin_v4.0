package com.westar.core.web.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.westar.core.service.TodayWorksService;

@Controller
@RequestMapping("/todayWorks")
public class TodayWorksController extends BaseController{

	@Autowired
	TodayWorksService todayWorksService;
	
	
	
}