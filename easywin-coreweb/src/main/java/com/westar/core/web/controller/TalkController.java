package com.westar.core.web.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.westar.core.service.TalkService;

/**
 * 留言操作控制类
 * 描述:
 * @author zzq
 * @date 2018年8月25日 上午11:54:38
 */
@Controller
@RequestMapping("/talk")
public class TalkController extends BaseController{

	@Autowired
	TalkService talkService;
	
	
	
}