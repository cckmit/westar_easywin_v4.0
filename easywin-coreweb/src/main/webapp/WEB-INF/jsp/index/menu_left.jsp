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
 <!-- Page Sidebar -->
      <div class="page-sidebar" id="sidebar">
          <!-- Sidebar Menu -->
          <ul class="nav sidebar-menu">
              <!--Tables-->
              <li class="open">
                  <a href="javascript:void(0);">
                      <i class="menu-icon fa fa-list-ul"></i>
                      <span class="menu-text">事项分类</span>
                  </a>
                  <ul class="submenu">
                      <li class="${empty busType?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('');">
                              <span class="menu-text">全部</span>
                          </a>
                      </li>
                      <li class="${busType=='003'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('003');">
                              <span class="menu-text">任务</span>
                          </a>
                      </li>
                      <li class="${busType=='022'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('022');">
                              <span class="menu-text">审批</span>
                          </a>
                      </li>
                      <li class="${busType=='005'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('005');">
                              <span class="menu-text">项目</span>
                          </a>
                      </li>
                      <li class="${busType=='012'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('012');">
                              <span class="menu-text">客户</span>
                          </a>
                      </li>
                      <li class="${busType=='006'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('006');">
                              <span class="menu-text">周报</span>
                          </a>
                      </li>
                      <li class="${busType=='050'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('050');">
                              <span class="menu-text">分享</span>
                          </a>
                      </li>
                      <li class="${busType=='011'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('011');">
                              <span class="menu-text">问答</span>
                          </a>
                      </li>
                      <li class="${busType=='004'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('004');">
                              <span class="menu-text">投票</span>
                          </a>
                      </li>
                      <li class="${busType=='013'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('013');">
                              <span class="menu-text">文档</span>
                          </a>
                      </li>
                      <li class="${busType=='080'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('080');">
                              <span class="menu-text">产品</span>
                          </a>
                      </li>
                      <!-- 
                      <c:if test="${userInfo.admin>0}">
	                      <li class="${busType=='015'?'active bg-themeprimary':'' }">
	                          <a href="javascript:void(0);" onclick="searchIndexInModule('015');">
	                              <span class="menu-text">个人申请</span>
	                          </a>
	                      </li>
                      </c:if>
                       -->
                      
                      <li class="${busType=='017'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('017');">
                              <span class="menu-text">会议通知</span>
                          </a>
                      </li>
                      <!-- 
                      <c:if test="${countRoom>0}">
	                      <li class="${busType=='018'?'active bg-themeprimary':'' }">
	                          <a href="javascript:void(0);" onclick="searchIndexInModule('018');">
	                              <span class="menu-text">会议审批</span>
	                          </a>
	                      </li>
                      </c:if>
                       -->
                      <li class="${busType=='1'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('1');">
                              <span class="menu-text">信息分享</span>
                          </a>
                      </li>
                      <!-- 
                      <li class="${busType=='101'?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="searchIndexInModule('101');">
                              <span class="menu-text">闹铃提示</span>
                          </a>
                      </li>
                       -->
                  </ul>
              </li>
          </ul>
          <!-- /Sidebar Menu -->
      </div>
</html>

