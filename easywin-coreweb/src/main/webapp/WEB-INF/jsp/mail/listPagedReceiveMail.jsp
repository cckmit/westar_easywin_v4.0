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
							<form action="/mail/listPagedReceiveMail" class="subform" id="searchForm">
								<input type="hidden" name="searchTab" id="searchTab" value="${empty param.searchTab?'11':param.searchTab}">
								<input type="hidden" name="sid" value="${param.sid}"> 
								<input type="hidden" id="accountId" name="accountId" value="${mail.accountId}">
								<input type="hidden" id="pageSize" name="pager.pageSize" value="10"> 
								<input type="hidden" name="activityMenu" value="${param.activityMenu}"> 
								<div class="searchCond" style="display: block">
									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="接收账号">
												<c:choose>
													<c:when test="${not empty mail.accountId && not empty listMs[0]}">
														<font style="font-weight:bold;">
																<c:forEach items="${listMs}" var="obj" varStatus="vs">
																	<c:if test="${obj.id ==  mail.accountId}">
																		${obj.account}
																	</c:if>
																</c:forEach>
														</font>
													</c:when>
													<c:otherwise>
														接收账号
													</c:otherwise>
												</c:choose>
												
												<i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li>
													<a href="javascript:void(0)" class="clearThisElement" relateElement="accountId">不限条件</a>
	                                            </li>
												<c:if test="${not empty listMs[0] }">
													<c:forEach items="${listMs}" var="obj" varStatus="vs">
														<li>
															<a href="javascript:void(0)" class="setElementValue" relateElement="accountId" dataValue="${obj.id}">${obj.account}</a>
														</li>
													</c:forEach>
												</c:if>
											</ul>
										</div>
									</div>
									<div class="ps-margin ps-search searchCond">
										<span class="input-icon">
											<input name="subject" class="form-control ps-input moreSearch" type="text" placeholder="请输入主题" value="${mail.subject }">
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
												<a class="btn btn-default dropdown-toggle btn-xs" id="delSendMail">全部删除</a>
											</div>
										</div>
									</div>
								</div>
							</form>
								<div class="widget-buttons ps-widget-buttons">
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-info btn-primary btn-xs" type="button" onclick="savePulldata('${param.sid}')">
											<i class="fa fa-circle-o"></i> 同步最近
										</button>
									</div>
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-info btn-primary btn-xs" type="button" onclick="savePulldata('${param.sid}',1)">
											<i class="fa fa-circle-o"></i> 同步所有
										</button>
									</div>
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-info btn-primary btn-xs" type="button" id="newMeeting" onclick="addMail('${param.sid}')">
											<i class="fa fa-plus"></i> 发送邮件
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
													<th width="25%" height="40">
														<h5>标题</h5>
													</th>
													<th  width="20%" height="35">
														<h5>接收账号</h5>
													</th>
													<th  width="20%" height="35">
														<h5>发送人</h5>
													</th>
													<th  width="20%" height="35">
														<h5>发送时间</h5>
													</th>
													<th  width="10%" height="35">
														<h5>操作</h5>
													</th>
												</tr>
											</thead>
											<tbody>

												<c:forEach items="${lists}" var="obj" varStatus="vs">
													<tr class="optTr">
														<td class="optTd" style="height: 47px"> <label class="optCheckBox" style="display: none;width: 20px"> <input class="colored-blue" type="checkbox" name="ids"
																		value="${obj.id}" /> <span class="text"></span>
																</label> <label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
														</td>
														<td>
															<a href="javascript:void(0);" onclick="viewMail('${obj.id}');">${obj.subject}</a>
														</td>
														<td>${obj.account}</td>
														<td>${obj.personal} ${not empty obj.personal?'（':''} ${obj.fromAddress} ${not empty obj.personal?'）':''}</td>
														<td>${obj.sendTime}</td>
														<td><a href="javascript:void(0);" onclick="back('${obj.id}')">回复</a>
														|
														<a href="javascript:void(0);" onclick="sendTurn('${obj.id}');">转发</a></td>
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
																<h2>暂无邮件信息！</h2>
																<p class="description">协同提高效率，分享拉近距离。</p>
															</div>
														</section>
													</div>
												</c:otherwise>
												</c:choose>
											</tbody>
										</table>
									</form>
									<tags:pageBar url="/mail/listPagedReceiveMail"></tags:pageBar>
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
		
		$(function(){
			$(".subform").Validform({
				tiptype : function(msg, o, cssctl) {
					validMsgV2(msg, o, cssctl);
				}
			}); 
			$("body").on("blur",".moreSearch",function(){
				$("#searchForm").submit();
			})
			
			//删除本地发送记录
			$("#delSendMail").click(function(){
				if(checkCkBoxStatus('ids')){
					window.top.layer.confirm('确定要删除本地邮件记录吗？',{icon:3,title:'询问框'}, function(index){
						var url = reConstrUrl('ids');
						$("#delForm input[name='redirectPage']").val(url);
						$('#delForm').attr("action","/mail/delSendMail");
						$('#delForm').submit();
					}, function(){
					});	
				}else{
					window.top.layer.alert('请先选择要删除本地邮件记录!',{title:false,closeBtn:0,icon:7,btn:['关闭']});
				}
			});
			
			
		})
		
		//查看邮件详情
		function viewMail(id) {
			var url = '/mail/viewMail?sid=' + sid + '&id=' + id+"&accountId=${mail.accountId}";
			openWinByRight(url);
		}
		
		//添加邮件
		function addMail(sid) {
			var url = '/mail/addMailPage?sid=' + sid;
			openWinByRight(url);
		}
		//转发邮件
		function sendTurn(id) {
			var url = '/mail/addMailPage?sid=' + sid+"&type=sendTurn&id="+id;
			openWinByRight(url);
		}
		
		//回复邮件
		function back(id) {
			var url = '/mail/addMailPage?sid=' + sid+"&type=back&id="+id;
			openWinByRight(url);
		}
		//同步邮件
		function savePulldata(sid,pullType) {
			window.top.layer.confirm('确定同步邮件到本地？',{icon:3,title:'询问框'}, function(index){
				var accountId = "";
				if($("#accountId").val()){
					accountId = $("#accountId").val();
				}
				window.top.layer.alert("正在同步邮件记录,请耐心等待！",{title:'提示框',closeBtn:0,icon:16,btn:[]});
				$.post("/mail/addPulldata?sid=${param.sid}&pullType="+pullType,{Action:"post",folder:'INBOX',accountId:accountId,},     
	 				function (msgObjs){
					if(msgObjs=="同步成功"){
						showNotification(1,msgObjs);
						window.self.location.reload();
					}else{
						showNotification(2,msgObjs);
						window.self.location.reload();
					}
					window.top.layer.close();
					
				});
			}, function(){
			});	
		}
		
		//添加邮件后同步邮件
		function pulldata() {
			window.top.layer.alert("发送成功！正在同步邮件发送记录,请耐心等待！",{title:'提示框',closeBtn:0,icon:16,btn:[]});
			$.post("/mail/addPulldata?sid=${param.sid}",{Action:"post",folder:'已发送',},     
 				function (msgObjs){
				if(msgObjs=="同步成功"){
					showNotification(1,msgObjs);
					window.self.location.reload();
				}else{
					showNotification(2,msgObjs);
					window.self.location.reload();
				}
				window.top.layer.close();
			});
		}
	</script>
<style type="text/css">
td,th{
white-space: nowrap;
text-overflow: ellipsis;
overflow: hidden;
}
table{
table-layout: fixed;
}
</style>
</html>