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
	<head>
	</head>
	<body>
	 <!-- Page Sidebar -->
            <div class="page-sidebar" id="sidebar">
                <!-- Sidebar Menu -->
                <ul class="nav sidebar-menu">
                    <li class="${(empty param.activityMenu || fn:contains(param.activityMenu,'self_m_1'))?'open':''}">
                        <a href="#" class="menu-dropdown">
                            <i class="menu-icon fa fa-bar-chart-o"></i>
                            <span class="menu-text">个人中心</span>

                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(empty param.activityMenu || param.activityMenu=='self_m_1.1')?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/userInfo/selfCenter?pager.pageSize=10&sid=${param.sid}&activityMenu=self_m_1.1');">
                                    <span class="menu-text">工作动态</span>
                                </a>
                            </li>
                           <%--  <li class="${param.activityMenu=='self_m_1.4'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);"
                                onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/financial/loanApply/listLoanApply?sid=${param.sid}&pager.pageSize=10&activityMenu=self_m_1.4");'>
                                    <span class="menu-text">我的出差记录</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='self_m_1.9'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);"
                                onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/financial/loanApply/listLoanApply?sid=${param.sid}&pager.pageSize=10&activityMenu=self_m_1.9");'>
                                    <span class="menu-text">一般借款申请</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='self_m_1.5'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);"
                                onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/financial/loan/listLoan?sid=${param.sid}&pager.pageSize=10&activityMenu=self_m_1.5");'>
                                    <span class="menu-text">我的借款记录</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='self_m_1.6'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);"
                                onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/financial/loanOff/listLoanOff?sid=${param.sid}&pager.pageSize=10&activityMenu=self_m_1.6");'>
                                    <span class="menu-text">我的报销记录</span>
                                </a>
                            </li> --%>

                            <li class="${param.activityMenu=='self_m_1.10'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/busRemind/listPagedSelfBusRemind?sid=${param.sid}&pager.pageSize=15&activityMenu=self_m_1.10');">
                                    <span class="menu-text">催办记录</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='self_m_1.3'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/systemLog/listPagedSelfSysLog?sid=${param.sid}&pager.pageSize=15&activityMenu=self_m_1.3');">
                                    <span class="menu-text">操作日志</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="${(fn:contains(param.activityMenu,'self_m_2'))?'open':''}">
                        <a href="#" class="menu-dropdown">
                            <i class="menu-icon fa fa-trophy"></i>
                            <span class="menu-text">积分中心</span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${param.activityMenu=='self_m_2.1'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/jiFen/jiFenCenter?sid=${param.sid}&pager.pageSize=12&activityMenu=self_m_2.1');">
                                    <span class="menu-text">总积分</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='self_m_2.2'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/jiFen/jiFenCenter?sid=${param.sid}&type=2&pager.pageSize=12&activityMenu=self_m_2.2');">
                                    <span class="menu-text">月积分排名</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='self_m_2.3'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/jiFen/jiFenCenter?sid=${param.sid}&type=1&pager.pageSize=12&activityMenu=self_m_2.3');">
                                    <span class="menu-text">周积分排名</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='self_m_2.4'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/jiFen/listPagedJifenHistory?sid=${param.sid}&pager.pageSize=12&activityMenu=self_m_2.4');">
                                    <span class="menu-text">积分历史</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='self_m_2.5'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/jiFen/listPagedJifenConfig?sid=${param.sid}&pager.pageSize=12&activityMenu=self_m_2.5');">
                                    <span class="menu-text">如何积分</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='self_m_2.6'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/jiFen/listPagedJifenLev?sid=${param.sid}&pager.pageSize=12&activityMenu=self_m_2.6');">
                                    <span class="menu-text">等级说明</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                     <li class="${(fn:contains(param.activityMenu,'self_m_4'))?'open':''}">
                        <a href="#" class="menu-dropdown">
                            <i class="menu-icon fa fa-inbox"></i>
                            <span class="menu-text">个人邮箱</span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${param.activityMenu=='self_m_4.1'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/mail/listPagedReceiveMail?sid=${param.sid}&pager.pageSize=10&activityMenu=self_m_4.1');">
                                    <span class="menu-text">收件箱</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='self_m_4.2'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/mail/listPagedSendMail?sid=${param.sid}&pager.pageSize=10&activityMenu=self_m_4.2');">
                                    <span class="menu-text">已发邮件</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="${(fn:contains(param.activityMenu,'self_m_3'))?'open':''}">
                        <a href="#" class="menu-dropdown">
                            <i class="menu-icon fa fa-gear"></i>
                            <span class="menu-text">个人设置 </span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${param.activityMenu=='self_m_3.1'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/userInfo/editUserInfoPage?sid=${param.sid}&activityMenu=self_m_3.1');">
                                    <span class="menu-text">个人资料</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='self_m_3.2'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/userInfo/editUserHeadImg?sid=${param.sid}&activityMenu=self_m_3.2');">
                                    <span class="menu-text">头像设置</span>
                                </a>
                            </li>
                            <li class="${fn:contains(param.activityMenu,'self_m_3.6')?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/selfGroup/listUserGroup?sid=${param.sid}&pager.pageSize=12&activityMenu=self_m_3.6');">
                                    <span class="menu-text">我的群组</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='self_m_3.3'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/userConf/msgShowSettingPage?sid=${param.sid}&pager.pageSize=12&activityMenu=self_m_3.3');">
                                    <span class="menu-text">显示设置</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='self_m_3.4'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/usagIdea/listPagedUsagIdea?sid=${param.sid}&pager.pageSize=12&activityMenu=self_m_3.4');">
                                    <span class="menu-text">常用意见</span>
                                </a>
                            </li>
                            <li>
                                <a href="javascript:void(0);" onclick="updatePassword('${param.sid}',this);">
                                    <span class="menu-text">修改密码</span>
                                </a>
                            </li>
                            <li class="${fn:contains(param.activityMenu,'self_m_3.5')?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/mailSet/listPagedMailSet?sid=${param.sid}&pager.pageSize=10&activityMenu=self_m_3.5');">
                                    <span class="menu-text">邮件通讯配置</span>
                                </a>
                            </li>
                            <li>
                                <a href="javascript:void(0);" onclick="userOne('handoverUserId','handoverUserName','','${param.sid}','yes');">
                                    <span class="menu-text">离职移交</span>
                                </a>
                            </li>
                            <li class="${fn:contains(param.activityMenu,'self_m_3.7')?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/outLinkMan/listOutLinkMan?sid=${param.sid}&pager.pageSize=10&activityMenu=self_m_3.7');">
                                    <span class="menu-text">外部联系人</span>
                                </a>
                            </li>
                             <li class="${fn:contains(param.activityMenu,'self_m_3.8')?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/personAttention/listPagedPersonAttention?sid=${param.sid}&pager.pageSize=10&activityMenu=self_m_3.8');">
                                    <span class="menu-text">人员关注</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <!--Profile-->
                    <%--<li>
                        <a href="#">
                            <i class="menu-icon fa fa-trash-o"></i>
                            <span class="menu-text">回收站</span>
                        </a>
                    </li>
                --%>
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

