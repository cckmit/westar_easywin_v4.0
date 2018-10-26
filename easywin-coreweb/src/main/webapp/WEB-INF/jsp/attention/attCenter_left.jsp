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
          <ul class="nav sidebar-menu">
              <!--Tables-->
              <li class="open">
                  <a href="javascript:void(0);">
                      <i class="menu-icon fa fa-flag"></i>
                      <span class="menu-text">关注分类 </span>
                  </a>
                  <ul class="submenu">
                      <li class="${(empty param.activityMenu || param.activityMenu=='job_m_1.1')?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="attClassify('','job_m_1.1');">
                              <span class="menu-text">全部</span>
                              <span class="pull-right ps-bage noreadNum" id="allNoReadNumT" 
                              style="background-color: red;margin-top: 10px;display: none"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.2'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="attClassify('003','job_m_1.2');">
                              <span class="menu-text">任务</span>
                              <span class="pull-right" id="taskNoReadNumT" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.3'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="attClassify('022','job_m_1.3');">
                              <span class="menu-text">审批</span>
                              <span class="pull-right" id="spNoReadNumT" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.4'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="attClassify('005','job_m_1.4');">
                              <span class="menu-text">项目</span>
                              <span class="pull-right" id="itemNoReadNumT" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.5'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="attClassify('012','job_m_1.5');">
                              <span class="menu-text">客户</span>
                              <span class="pull-right" id="crmNoReadNumT" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <%--<li class="${param.activityMenu=='job_m_1.6'?'active':'' }">
                          <a href="javascript:void(0);" onclick="attClassify('','job_m_1.6');">
                              <span class="menu-text">分享</span>
                          </a>
                      </li>
                      --%>
                      <li class="${param.activityMenu=='job_m_1.7'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="attClassify('004','job_m_1.7');">
                              <span class="menu-text">投票</span>
                              <span class="pull-right" id="voteNoReadNumT" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <%--<li class="${param.activityMenu=='job_m_1.8'?'active':'' }">
                          <a href="javascript:void(0);" onclick="attClassify('','job_m_1.8');">
                              <span class="menu-text">公文</span>
                          </a>
                      </li>
                      --%>
                      <li class="${param.activityMenu=='job_m_1.9'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="attClassify('011','job_m_1.9');">
                              <span class="menu-text">问答</span>
                              <span class="pull-right" id="qasNoReadNumT" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.10'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="attClassify('100','job_m_1.10');">
                              <span class="menu-text">分享</span>
                              <span class="pull-right" id="shareNoReadNumT" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <%--<li class="${param.activityMenu=='job_m_1.11'?'active':'' }">
                          <a href="javascript:void(0);" onclick="attClassify('101','job_m_1.11');">
                              <span class="menu-text">闹铃提示</span>
                          </a>
                      </li>
                  	 --%>
                  </ul>
              </li>
          </ul>
          <!-- /Sidebar Menu -->
      </div>
</body>
</html>

