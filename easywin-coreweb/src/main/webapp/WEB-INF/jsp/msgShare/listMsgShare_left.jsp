<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
 <!-- Page Sidebar -->
      <div class="page-sidebar" id="sidebar">
          <!-- Sidebar Menu -->
          <ul class="nav sidebar-menu" id="leftMenuUl">
              <!--Tables-->
              <li class="open">
                  <a href="javascript:void(0);">
                      <i class="menu-icon fa fa-flag"></i>
                      <span class="menu-text">消息中心 </span>
                  </a>
                  <ul class="submenu">
                      <li class="${empty todayWorks.busType?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('');">
                              <span class="menu-text">全部</span>
                              <span class="pull-right noreadNum" id="allNoReadNumT" 
                              style="background-color: red;margin-top: 10px">0</span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='016'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('016');">
                              <span class="menu-text">日程</span>
                              <span class="pull-right" id="noReadNumT_016" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='003'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('003');">
                              <span class="menu-text">任务</span>
                              <span class="pull-right" id="noReadNumT_003" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='022'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('022');">
                              <span class="menu-text">审批</span>
                              <span class="pull-right" id="noReadNumT_022" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='004'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('004');">
                              <span class="menu-text">投票</span>
                              <span class="pull-right" id="noReadNumT_004" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='005'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('005');">
                              <span class="menu-text">项目</span>
                              <span class="pull-right" id="noReadNumT_005" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='006'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('006');">
                              <span class="menu-text">周报</span>
                              <span class="pull-right" id="noReadNumT_006" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='050'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('050');">
                              <span class="menu-text">分享</span>
                              <span class="pull-right" id="noReadNumT_050"
                                    style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='039'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('039');">
                              <span class="menu-text">公告</span>
                              <span class="pull-right" id="noReadNumT_039" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='011'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('011');">
                              <span class="menu-text">问答</span>
                              <span class="pull-right" id="noReadNumT_011" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='012'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('012');">
                              <span class="menu-text">客户</span>
                              <span class="pull-right" id="noReadNumT_012" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='013'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('013');">
                              <span class="menu-text">文档</span>
                              <span class="pull-right" id="noReadNumT_013" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <c:if test="${userInfo.admin>0}">
	                      <li class="${todayWorks.busType=='015'?'active bg-themeprimary':'' }">
	                          <a href="javascript:void(0);" onclick="setSearchBusType('015');">
	                              <span class="menu-text">加入申请</span>
	                              <span class="pull-right" id="noReadNumT_015" 
                              style="background-color: red;margin-top: 10px"></span>
	                          </a>
	                      </li>
                      </c:if>
                      
                      <li class="${todayWorks.busType=='019'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('019');">
                              <span class="menu-text">普通消息</span>
                              <span class="pull-right" id="noReadNumT_019" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='017'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('017');">
                              <span class="menu-text">会议通知</span>
                              <span class="pull-right" id="noReadNumT_017" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <c:if test="${countRoom>0}">
	                      <li class="${todayWorks.busType=='018'?'active bg-themeprimary':'' }">
	                          <a href="javascript:void(0);" onclick="setSearchBusType('018');">
	                              <span class="menu-text">会议审批</span>
	                              <span class="pull-right" id="noReadNumT_018" 
                              		style="background-color: red;margin-top: 10px"></span>
	                          </a>
	                      </li>
                      </c:if>
                         <li class="${todayWorks.busType=='040'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('040');">
                              <span class="menu-text">制度管理</span>
                              <span class="pull-right" id="noReadNumT_040" 
                             		style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='027010'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('027010');">
                              <span class="menu-text">用品采购</span>
                              <span class="pull-right" id="noReadNumT_027010" 
                             		style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='027020'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('027020');">
                              <span class="menu-text">用品申领</span>
                              <span class="pull-right" id="noReadNumT_027020" 
                             		style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                       <li class="${todayWorks.busType=='047'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('047');">
                              <span class="menu-text">会议纪要审批</span>
                              <span class="pull-right" id="noReadNumT_047" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='100'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('100');">
                              <span class="menu-text">信息分享</span>
                              <span class="pull-right" id="noReadNumT_100" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${todayWorks.busType=='101'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="setSearchBusType('101');">
                              <span class="menu-text">闹铃提示</span>
                              <span class="pull-right" id="noReadNumT_101" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                  </ul>
              </li>
          </ul>
          <!-- /Sidebar Menu -->
      </div>
</body>
</html>

