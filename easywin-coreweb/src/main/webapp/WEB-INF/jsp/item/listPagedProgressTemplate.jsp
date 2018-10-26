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
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			
					<div class="row">
						<div class="col-md-12 col-xs-12 ">
							<div class="widget">
								<div
									class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
									<div class="checkbox ps-margin pull-left">
										<label> <input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'ids')"> <span class="text" style="color: inherit;">全选</span>
										</label>
									</div> 
									<form action="/itemProgress/listPagedProgressTemplate" id="searchForm">
										<input type="hidden" name="sid" value="${param.sid }" >
										<input type="hidden" name="searchTab" value="${param.searchTab }" >
										<input type="hidden" name="pager.pageSize" value="10">
										<div class="ps-margin ps-search searchCond">
											<span class="input-icon"> <input class="form-control ps-input moreSearch" id="templateName" name="templateName"
												value="${itemProgressTemplate.templateName}" type="text"
												placeholder="请输入关键字"> <a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
											</span>
										</div>
										<div class="batchOpt" style="display: none">
											<div class="btn-group pull-left">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs" id="delIpts"> 批量删除 </a>
													</div>
												</div>
											</div>
										</div>
									</form>
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-info btn-primary btn-xs" type="button"
											onclick="addProgressTemplate();">
											<i class="fa fa-plus"></i> 项目进度模板
										</button>
									</div>
								</div>
								<c:choose>
									<c:when test="${not empty lists}">
										<div class="widget-body">
											<form id="delForm">
												<input type="hidden" name="sid" value="${param.sid}">
												<input type="hidden" name="redirectPage" />
												<table class="table table-striped table-hover" id="editabledatatable">
													<thead>
														<tr role="row">
															<th>序号</th>
															<th>模板名称</th>
															<th>创建人</th>
															<th>创建日期</th>
															<th>操作</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach items="${lists}" var="obj" varStatus="vs">
															<tr class="optTr" style="clear: both">
																<td class="optTd" style="height: 47px;"><label class="optCheckBox" style="display: none;width: 20px"> <input class="colored-blue" type="checkbox" name="ids"
																			value="${obj.id}" /> <span class="text"></span>
																	</label><label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
																</td>
																<td><a href="javascript:void(0);" onclick="editProgressTemplate(${obj.id})">${obj.templateName}</a></td>
																<td><span>${obj.creatorName}</span></td>
																<td>${obj.recordCreateTime}</td>
																<td>
																	<a href="javascript:void(0);" onclick="copy(${obj.id})">克隆</a>
																</td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</form>
											<tags:pageBar url="/itemProgress/listPagedProgressTemplate"></tags:pageBar>
										</div>
								</c:when>
								<c:otherwise>
									<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
										<section class="error-container text-center">
											<h1>
												<i class="fa fa-exclamation-triangle"></i>
											</h1>
											<div class="error-divider">
												<h2>还没有配置相关模板！</h2>
												<p class="description">协同提高效率，分享拉近距离。</p>
												<a href="javascript:void(0);" onclick="addProgressTemplate();"
													class="return-btn"><i class="fa fa-plus"></i>项目进度模板</a>
											</div>
										</section>
									</div>
								</c:otherwise>
							</c:choose>
							</div>
						</div>
					</div>
				
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->

	</div>
	<!-- /Page Container -->
</body>

<script type="text/javascript">
$(function(){
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsgV2(msg, o, cssctl);
		}
	}); 
	
	//消费记录删除
	$("#delIpts").click(function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要删除选中模板吗？',{icon:3,title:'询问框'}, function(index){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').attr("action","/itemProgress/delProgressTemplates");
				$('#delForm').submit();
			}, function(){
			});	
		}else{
			window.top.layer.alert('请先选择要删除的模板。',{title:false,closeBtn:0,icon:7,btn:['关闭']});
		}
	});
	
	//模糊查询
	$("body").on("blur",".moreSearch",function(){
		//启动加载页面效果
		layer.load(0, {shade: [0.6,'#fff']});
		$("#searchForm").submit();
	})
})

//添加项目进度模板
function addProgressTemplate() {
	var url = '/itemProgress/addProgressTemplatePage?sid=' + sid;
	openWinByRight(url);
} 

//编辑项目进度模板
function editProgressTemplate(id) {
	var url = '/itemProgress/editProgressTemplatePage?id='+id+'&sid=' + sid;
	openWinByRight(url);
} 

//克隆
function copy(id) {
	window.top.layer.confirm('确定要克隆该模板吗？', {
		icon: 3,
		title: '提示'
	},
	function(index) {
		postUrl('/itemProgress/addCopyProgressTemplate', {
			sid: sid,
			id: id
		},
		function(data) {debugger
			if (data.code == '0') {
				showNotification(1, "克隆成功！");
				window.location.reload()
			} else {
				showNotification(2, data.msg);
			}
		});
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

