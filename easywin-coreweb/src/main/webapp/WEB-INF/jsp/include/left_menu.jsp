<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.util.MathExtend" %>
<%@page import="com.westar.base.model.UserInfo" %>
<%@page import="com.westar.base.util.CommonUtil" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
//实时计算个人积分信息
ApplicationContext applicationContext=WebApplicationContextUtils.getWebApplicationContext(application);
UserInfo user = CommonUtil.jiFenOfUser(applicationContext,request.getParameter("sid"));
%>
<head>
</head>
<body>
<!--sidebar start-->
<aside>
	<div id="sidebar" class="nav-collapse">
		<div class="ws-side-box">
		<div class="sidebar-toggle-box">
            <div class="fa fa-bars" title="左菜单收展"></div>
        </div>
		<div class="ws-help">
		<!-- 
			<a href="http://wpa.qq.com/msgrd?v=3&uin=2151541168&site=qq&menu=yes" target="_blank" class="fa fa-comments-o" title="在线客服"></a>
		 -->
	<a href="#" class="fa fa-comments-o" title="在线客服"></a>
	<a href="#" class="fa fa-exclamation-circle" title="反馈建议"></a><a href="#" class="fa fa-question-circle" title="常见问题"></a>
</div>
		<div class="ws-peopleNews">
			<div class="ws-peopleHead">
				<div class="ws-picture">
					<a href="/userInfo/aboutUserInfoPage?sid=${param.sid}" title="个人中心" id="userInfo_Pic">
			     		<img src="/downLoad/userImg/${userInfo.comId}/${userInfo.id}?sid=${param.sid}&size=1"></img>
					</a>
				</div>
				<div class="ws-peopleName">
					<h3 class="ws-name">${userInfo.userName }</h3>
					<p><%=(user.getLevName())%></p>
				</div>
				<a href="/userInfo/editUserInfoPage?sid=${param.sid}" class="fa fa-cog" title="个人设置"></a>
			</div>
			<div class="ws-integral">
				<div class="ws-progress" title="还差<%=(user.getNextLevJifen()-user.getTotalJifen())%>分升级">
					<span class="ws-green" style="width:<%=user.getJiFenPercent()%>;"><span><%=user.getJiFenPercent()%></span></span>
				</div>
				<div class="ws-integralText">
					<div style="float: left;width:50%">
						<div>
							<p>
								周积分：<a href="/jiFen/jiFenTabPage?sid=${param.sid}&viewType=342"><%=user.getWeekJifen()%></a>
							</p>
						</div>
						<div>	
							<p>
								总积分：<a href="/jiFen/jiFenTabPage?sid=${param.sid}&viewType=341"><%=user.getTotalJifen()%></a>
							</p>
						</div>
					</div>
					<div style="float: left;width: 50%">
						<div>
							<p>
								 周排名：<a href="/jiFen/jiFenTabPage?sid=${param.sid}&viewType=342"><%=user.getWeekRank()%></a>
							</p>
						</div>
						<div>
							<p>
								  总排名：<a href="/jiFen/jiFenTabPage?sid=${param.sid}&viewType=341"><%=user.getTotalRank()%></a>
							</p>
						</div>
					</div>
					<a href="/jiFen/jiFenTabPage?sid=${param.sid}&viewType=341" class="fa fa-trophy" title="积分中心"></a>
				</div>
			</div>
		</div>
		<!-- sidebar menu start-->
		<div class="leftside-navigation">
			<ul class="sidebar-menu" id="nav-accordion">
				<li>
					<a href="/workFlow/test?pager.pageSize=10&sid=${param.sid}">
						<i class="fa fa-group"></i>
						<span>流程审批</span>
					</a>
				</li>
				<li>
					<a href="/crm/customerListPage?pager.pageSize=10&sid=${param.sid}">
						<i class="fa fa-group"></i>
						<span>客户中心</span>
					</a>
				</li>
				<li>
					<a href="/item/listItemPage?pager.pageSize=11&sid=${param.sid}">
						<i class="fa fa-laptop"></i>
						<span>项目中心</span>
					</a>
				</li>
				<li>
					<a href="/task/taskToDoListPage?pager.pageSize=10&sid=${param.sid}">
						<i class="fa fa-flag"></i>
						<span>任务中心</span>
					</a>
				</li>
				<li>
					<a href="/weekReport/listPagedWeekRep?pager.pageSize=10&sid=${param.sid}">
						<i class="fa fa-th"></i>
						<span>周报中心</span>
					</a>
				</li>
				<li>
					<a href="/daily/listPagedDaily?pager.pageSize=12&sid=${param.sid}">
						<i class="fa fa-th"></i>
						<span>分享中心</span>
					</a>
				</li>
				<li>
					<a href="/meeting/listPagedTodoMeeting?pager.pageSize=10&sid=${param.sid}">
						<i class="fa fa-th"></i>
						<span>会议中心</span>
					</a>
				</li>
				<li>
					<a href="/qas/listPagedQas?pager.pageSize=10&sid=${param.sid}">
						<i class="fa fa-book"></i>
						<span>知识库</span>
					</a>
				</li>
				<li>
					<a href="javascript:void(0)" id="recyleBin" onclick="window.self.location='/recycleBin/listPagedPreDel?sid=${param.sid}'">
						<i class="fa fa-trash-o"></i>
						<span>回收箱</span>
					</a>
				</li>
			</ul>
		</div>
		</div>
		<!-- sidebar menu end-->
	</div>
</aside>
<!--sidebar end-->
</body>
