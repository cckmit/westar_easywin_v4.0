<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>

	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div class="checkbox ps-margin pull-left">
								<label> <input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'ids')"> <span class="text" style="color: inherit;">全选</span>
								</label>
							</div>
							<div>
							<form action="/personAttention/listPagedPersonAttention" class="subform" id="searchForm">
								<input type="hidden" name="searchTab" id="searchTab" value="${empty param.searchTab?'11':param.searchTab}">
								<input type="hidden" name="sid" value="${param.sid}"> 
								<input type="hidden" id="pageSize" name="pager.pageSize" value="10"> 
								<input type="hidden" name="activityMenu" value="${param.activityMenu}"> 
								<input type="hidden" name="depName" value="${personAttention.depName}">
								<input type="hidden" name="depId" value="${personAttention.depId}">
								<div class="searchCond" style="display: block">
									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="流程状态筛选">
												<c:choose>
													<c:when test="${not empty personAttention.depName}">
														<font style="font-weight:bold;">${personAttention.depName}</font>
													</c:when>
													<c:otherwise>部门筛选</c:otherwise>
												</c:choose>
												<i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li><a href="javascript:void(0)" class="clearThisElement" relateElement="depId" relateElementName="depName">不限条件</a>
												</li>
												<li><a href="javascript:void(0)" class="depOneElementSelect" relateElement="depId" relateElementName="depName">部门选择</a>
												</li>
											</ul>
										</div>
									</div>
									<div class="ps-margin ps-search searchCond">
										<span class="input-icon">
											<input name="userName" id="userName" class="form-control ps-input moreSearch" type="text" placeholder="请输入关键字" value="${personAttention.userName }">
											<a href="javascript:void(0)" class="ps-searchBtn">
												<i class="glyphicon glyphicon-search circular danger"></i>
											</a>
										</span>
									</div>
								</div>
									
								</div>
								<div class="batchOpt" style="display: none">
									<div class="btn-group pull-left">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" onclick="delPersonAttention()"> 批量删除 </a>
											</div>
										</div>
									</div>
								</div>
							</form>
								<div class="widget-buttons ps-widget-buttons">
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-info btn-primary btn-xs" type="button" id="newMeeting" onclick="userMoreForUserId('${param.sid}')">
											<i class="fa fa-plus"></i> 关注人员
										</button>
									</div>
								</div>
							</div>
						
						<c:choose>
							<c:when test="${not empty lists}">
								<div class="widget-body">
									<form method="post" id="delForm">
										<input type="hidden" name="sid" value="${param.sid }" />
										<input type="hidden" name="redirectPage" />
										<table class="table table-striped table-hover">
											<thead>
												<tr role="row">
													<th width="5%" height="45">
														&nbsp;
													</th>
													<th width="20%" height="40">
														<h5>关注人姓名</h5>
													</th>
													<th  width="20%" height="35">
														<h5>部门</h5>
													</th>
													<th  width="20%" height="35">
														<h5>职务</h5>
													</th>
													<th  width="20%" height="35">
														<h5>Email</h5>
													</th>
													<th width="15%" height="40">
														<h5>手机</h5>
													</th>
												</tr>
											</thead>
											<tbody>

												<c:forEach items="${lists}" var="obj" varStatus="vs">
													<tr class="optTr">
														<td class="optTd" style="height: 47px"><label class="optCheckBox" style="display: none;width: 20px"> <input class="colored-blue" type="checkbox" name="ids"
																	value="${obj.id}" /> <span class="text"></span>
															</label> <label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
														</td>
														<td valign="middle">
															<a href="javascript:void(0)" onclick="view(${obj.userId})">
																<div class="ticket-user pull-left other-user-box">
																	<img src="/downLoad/userImg/${userInfo.comId}/${obj.userId}" title="${obj.userName}" class="user-avatar" />
																	<span class="user-name">${obj.userName}</span>
																</div>
															</a>
														</td>
														<td>${obj.depName}</td>
														<td>${obj.job}</td>
														<td>${obj.email}</td>
														<td>${obj.movePhone}</td>
													</tr>
												</c:forEach>
												</c:when>
												<c:otherwise>
													<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
														<section class="error-container text-center">
															<h1>
																<i class="fa fa-exclamation-triangle"></i>
															</h1>
															<div class="error-divider">
																<h2>您还没相关关注人员！</h2>
																<p class="description">协同提高效率，分享拉近距离。</p>
															</div>
														</section>
													</div>
												</c:otherwise>
												</c:choose>
											</tbody>
										</table>
									</form>
									<tags:pageBar url="/personAttention/listPagedPersonAttention"></tags:pageBar>
								</div>
							</div>
					</div>
				</div>
			</div>
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->
	
	</div>
</body>
	<script type="text/javascript">
		var sid = '${param.sid}';
		//关注人更新
		function userMoreForUserIdCallBack(options){
			var userIds =new Array();
			if(options && options[0]){
				for (var i = 0; i < options.length; i++) {
					userIds.push(options[i].value); 
				}
			}
			if(!strIsNull(userIds.toString())){
				$.post("/personAttention/addPersonAttention?sid=${param.sid}",{Action:"post",userIds:userIds.toString()},     
					function (msgObjs){
					showNotification(1,msgObjs);
					 window.self.location.reload();
				});
			} 
		}
		
		$(function(){
			$(".subform").Validform({
				tiptype : function(msg, o, cssctl) {
					validMsgV2(msg, o, cssctl);
				}
			}); 
			
			//客户名筛选
			$("#userName").blur(function(){
				//启动加载页面效果
				layer.load(0, {shade: [0.6,'#fff']});
				$("#searchForm").submit();
			});
			//文本框绑定回车提交事件
			$("#userName").bind("keydown",function(event){
		        if(event.keyCode == "13")    
		        {
		        	if(!strIsNull($("#userName").val())){
		        		//启动加载页面效果
		        		layer.load(0, {shade: [0.6,'#fff']});
						$("#userName").submit();
		        	}else{
		        		showNotification(1,"请输入检索内容！");
		    			$("#userName").focus();
		        	}
		        }
		    });
		})
		
		//删除关注人员
		function delPersonAttention(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要删除关注人员吗？',{icon:3,title:'询问框'}, function(index){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').attr("action","/personAttention/delPersonAttentions");
				$('#delForm').submit();
			}, function(){
			});	
		}else{
			window.top.layer.alert('请先选择要删除的关注人员。',{title:false,closeBtn:0,icon:7,btn:['关闭']});
		}
	}
		
		//人员信息查看
		function view(id){
			var url='/userInfo/viewUserInfo?id='+id+'&sid=${param.sid}';
			window.top.layer.open({
				  type: 2,
				  title: false,
				  closeBtn: 0,
				  shade: 0.5,
				  shift:0,
				  scrollbar:false,
				  fix: true, //固定
				  maxmin: false,
				  move: false,
				  area: ['640px', '500px'],
				  content: [url,'no'], //iframe的url
				  btn:['关闭']
				});
			
		}
	</script>
</html>