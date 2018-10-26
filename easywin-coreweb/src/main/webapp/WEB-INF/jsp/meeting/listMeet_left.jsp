<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	</head>
	<body>
		 <!-- Page Sidebar -->
            <div class="page-sidebar" id="sidebar">
                <!-- Sidebar Menu -->
                <ul class="nav sidebar-menu">
                 	<li class="${(empty param.searchTab || (param.searchTab - param.searchTab mod 10)/10 eq 1)?'open':'' }">
                        <a href="javascript:void(0)" class="menu-dropdown">
                           	<i class="menu-icon fa fa-users"></i>
                            <span class="menu-text">会议中心</span>
                            <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu">
                            <li class="${(empty param.searchTab || param.searchTab==11)?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0)" onclick="menuClick('/meeting/listPagedTodoMeeting?sid=${param.sid}&searchTab=11')">
                                    <span class="menu-text">待开会议</span>
                                </a>
                            </li>
                            <li class="${(param.searchTab==15)?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0)" onclick="menuClick('/meeting/listPagedSpFlowMeeting?sid=${param.sid}&searchTab=15')">
                                    <span class="menu-text">审批会议</span>
                                </a>
                            </li>
                            <li class="${param.searchTab==12?'active bg-themeprimary':'' }">
                                 <a href="javascript:void(0)" onclick="menuClick('/meeting/listPagedMeeting?sid=${param.sid}&searchTab=12&meetingState=0')">
                                    <span class="menu-text">待发会议</span>
                                </a>
                            </li>
                            <c:if test="${userInfo.countSub>0}">
	                            <li class="${param.searchTab==13?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0)" onclick="menuClick('/meeting/listPagedMeeting?sid=${param.sid}&searchTab=13&meetingState=1')">
	                                    <span class="menu-text">已发会议</span>
	                                </a>
	                            </li>
                            </c:if>
                            <li class="${param.searchTab==14?'active bg-themeprimary':'' }">
                                 <a href="javascript:void(0)" onclick="menuClick('/meeting/listPagedDoneMeeting?sid=${param.sid}&searchTab=14')">
                                    <span class="menu-text">已开会议</span>
                                </a>
                            </li>
                            <li class="${param.searchTab==16?'active bg-themeprimary':'' }">
                                 <a href="javascript:void(0)" onclick="menuClick('/meeting/listPagedMeetWithSpSummary?sid=${param.sid}&searchTab=16&summaryNeedSpState=1')">
                                    <span class="menu-text">审批纪要会议</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                   <li class="${(param.searchTab - param.searchTab mod 10)/10 eq 2?'open':'' }">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-gear"></i>
                            <span class="menu-text">会议室</span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                         	<c:if test="${userInfo.admin>0}">
	                            <li class="${param.searchTab==23?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0)" onclick="menuClick('/meetingRoom/listPagedMeetingRoom?sid=${param.sid}&searchTab=23')">
	                                    <span class="menu-text">会议室管理</span>
	                                </a>
	                            </li>
                            </c:if>
                            <li class="${param.searchTab==21?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0)" onclick="menuClick('/meetingRoom/listPagedRoomApply?sid=${param.sid}&searchTab=21')">
                                    <span class="menu-text">撤销申请</span>
                                </a>
                            </li>
                            <c:if test="${countManageRoom>0 }">
	                            <li class="${param.searchTab==22?'active bg-themeprimary':'' }">
	                                 <a href="javascript:void(0)" onclick="menuClick('/meetingRoom/listPagedApplyForCheck?sid=${param.sid}&searchTab=22')">
	                                    <span class="menu-text">会议室审核</span>
	                                </a>
	                            </li>
                            </c:if>
                           
                        </ul>
                    </li>
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

