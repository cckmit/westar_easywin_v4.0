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
                      <span class="menu-text">待办分类 </span>
                  </a>
                  <ul class="submenu">
                      <li class="${(empty param.activityMenu || param.activityMenu=='job_m_1.1')?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('','job_m_1.1');">
                              <span class="menu-text">总待办</span>
                              <span class="pull-right noreadNum" id="allNoReadNumT" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.2'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('003','job_m_1.2');">
                              <span class="menu-text">任务</span>
                              <span class="pull-right" id="noReadNumT_003" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.3'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('022','job_m_1.3');">
                              <span class="menu-text">审批</span>
                              <span class="pull-right" id="noReadNumT_022" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.9'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('017','job_m_1.9');">
                              <span class="menu-text">会议通知</span>
                              <span class="pull-right" id="noReadNumT_017" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.6'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('050','job_m_1.6');">
                              <span class="menu-text">分享</span>
                              <span class="pull-right" id="noReadNumT_050"
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.7'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('006','job_m_1.7');">
                              <span class="menu-text">周报</span>
                              <span class="pull-right" id="noReadNumT_006" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                       <c:if test="${userInfo.admin>0}">
                        <li class="${param.activityMenu=='job_m_1.12'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('015','job_m_1.12');">
                              <span class="menu-text">加入申请</span>
                              <span class="pull-right" id="noReadNumT_015" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                       </c:if>
                       <li class="${param.activityMenu=='job_m_1.18'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('9999','job_m_1.18');">
                              <span class="menu-text">其它</span>
                              <span class="pull-right" id="noReadNumT_9999" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <%-- <li class="${param.activityMenu=='job_m_1.4'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('005','job_m_1.4');">
                              <span class="menu-text">项目</span>
                              <span class="pull-right" id="noReadNumT_005" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.5'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('012','job_m_1.5');">
                              <span class="menu-text">客户</span>
                              <span class="pull-right" id="noReadNumT_012" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                        <li class="${param.activityMenu=='job_m_1.15'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('040','job_m_1.15');">
                              <span class="menu-text">制度</span>
                              <span class="pull-right" id="noReadNumT_040" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <!-- 
	                      <li class="${param.activityMenu=='job_m_1.8'?'active bg-themeprimary':'' }">
	                          <a href="javascript:void(0);" onclick="jobClassify('','job_m_1.8');">
	                              <span class="menu-text">公文</span>
	                          </a>
	                      </li>
                       -->
                      <li class="${param.activityMenu=='job_m_1.17'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('047','job_m_1.17');">
                              <span class="menu-text">会议纪要审批</span>
                              <span class="pull-right" id="noReadNumT_047" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.10'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('018','job_m_1.10');">
                              <span class="menu-text">会议审批</span>
                              <span class="pull-right" id="noReadNumT_018" 
                              		style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.13'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('027010','job_m_1.13');">
                              <span class="menu-text">用品采购</span>
                              <span class="pull-right" id="noReadNumT_027010" 
                              		style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.14'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('027020','job_m_1.14');">
                              <span class="menu-text">用品申领</span>
                              <span class="pull-right" id="noReadNumT_027020" 
                              		style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.11'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('101','job_m_1.11');">
                              <span class="menu-text">闹铃提示</span>
                              <span class="pull-right" id="noReadNumT_101" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                      <li class="${param.activityMenu=='job_m_1.16'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('00306','job_m_1.16');">
                              <span class="menu-text">任务报延</span>
                              <span class="pull-right" id="noReadNumT_00306" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li>
                       <li class="${param.activityMenu=='job_m_1.18'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="jobClassify('099','job_m_1.18');">
                              <span class="menu-text">催办</span>
                              <span class="pull-right" id="noReadNumT_099" 
                              style="background-color: red;margin-top: 10px"></span>
                          </a>
                      </li> --%>
                  </ul>
              </li>
          </ul>
          <!-- /Sidebar Menu -->
      </div>
</body>
</html>

