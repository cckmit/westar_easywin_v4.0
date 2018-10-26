<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

</head>
<body>
<!-- Page Content -->
       <div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
						<div>
							<form action="/jfzb/listPagedJfzb" id="searchForm" class="subform">
							
								<input type="hidden" name="searchTab" id="searchTab" value="${param.searchTab }" />
								<input type="hidden" name="activityMenu" id="activityMenu" value="${param.activityMenu }" />
								<input type="hidden" name="sid" value="${param.sid }" />
								<input type="hidden" name="pager.pageSize" value="10">
									
								<input type="hidden" name="jfzbTypeId" value="${jfzb.jfzbTypeId }">
								<input type="hidden" name="jfzbTypeName" value="${jfzb.jfzbTypeName}">
								
								<div class="btn-group pull-left searchCond">
										<div class="table-toolbar ps-margin margin-right-5">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs"
													data-toggle="dropdown">
														<c:choose>
															<c:when test="${not empty jfzb.jfzbTypeName }">
																<font style="font-weight:bold;">
																	${jfzb.jfzbTypeName}
																</font>
															</c:when>
															<c:otherwise>
																评分类别筛选
															</c:otherwise>
														</c:choose>
													 <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default" id="jfzbTypeUl">
													<li><a href="javascript:void(0)" class="clearThisElement" relateElement="jfzbTypeId" relateElementName="jfzbTypeName">不限条件</a></li>
												</ul>
											</div>
										</div>
								</div>
								<div class="batchOpt" style="display: none">
									<div class="btn-group pull-left">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs batchDelJfzb"> 批量删除 </a>
											</div>
										</div>
									</div>
								</div>
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon"> <input id="describe"
										name="describe" value="${jfzb.describe}"
										class="form-control ps-input" type="text"
										placeholder="指标描述关键字">
										<a href="javascript:void(0)" class="ps-searchBtn">
											<i class="glyphicon glyphicon-search circular danger"></i>
										</a>
									</span>
								</div>
							</form>
								<c:if test="${userInfo.admin>0 }">
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-info btn-primary btn-xs" type="button" id="addJfzbTypeBtn">
		                                  		<i class="fa fa-plus"></i>
		                                  		指标类型
		                                  	</button>
		                                  	<button class="btn btn-info btn-primary btn-xs" type="button" id="addJfzbBtn">
		                                  		<i class="fa fa-plus"></i>
		                                  		评分指标
		                                  	</button>
									</div>
								</c:if>
							</div>
						</div>
						<c:choose>
						<c:when test="${not empty list}">
						<div class="widget-body">
							<form action="/jfzb/deleteJfzb" method="post" id="delForm">
								<input type="hidden" name="sid" value="${param.sid}"/>
								<input type="hidden" id="redirectPage" name="redirectPage" />
								<table class="table table-striped table-hover fixTable"
									id="editabledatatable">
									<thead>
										<tr role="row">
											<c:choose>
												<c:when test="${userInfo.admin>0 }">
													<th style="width: 30px"><label><input type="checkbox"
															class="colored-blue" id="checkAllBox"
															onclick="checkAll(this,'ids')"> <span
															class="text" style="color: inherit;"></span>
													</label></th>
												</c:when>
												<c:otherwise>
													<th style="width: 30px">序</th>
												</c:otherwise>
											</c:choose>
											<th style="width: 90px">录入时间</th>
											<th style="width: 110px">录入人员</th>
											<th style="width: 130px">积分类别</th>
											<th style="width: 70px;">最高分</th>
											<th style="width: 70px">最低分</th>
											<th style="width: 170px">积分指标</th>
											<th >描述</th>
											<th style="width: 90px">操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${list}" var="jfzbVo" varStatus="vs">
											<tr class="optTr">
												<td class="optTd" style="height: 47px"><label
													class="optCheckBox" style="display: none;width: 10px">
														<input class="colored-blue" type="checkbox" name="ids"
														value="${jfzbVo.id}" /> <span
														class="text"></span>
												</label> <label class="optRowNum"
													style="display: block;width: 20px">${vs.count}</label></td>
												<td>
													${fn:substring(jfzbVo.recordCreateTime,0,10)}
												</td>
												<td>
													<div class="ticket-user pull-left other-user-box" data-container="body"
															data-toggle="popover" data-placement="left">
																<img class="user-avatar userImg" title="${jfzbVo.recorderName}" 
																	src="/downLoad/userImg/${jfzbVo.comId}/${jfzbVo.recorderId}"/>
															<span class="user-name">${jfzbVo.recorderName}</span>
														</div>
												</td>
												<td>
													${jfzbVo.jfzbTypeName}
												</td>
												<td >
													${jfzbVo.jfTop}分
												</td>
												<td>
													${jfzbVo.jfBottom}分
												</td>
												<td>
													${jfzbVo.leveTwo}
												</td>
												<td>
													${jfzbVo.describe}
												</td>
												<td >
													<c:if test="${userInfo.admin>0 }">
													<a href="javascript:void(0)" comId="${jfzbVo.comId}" dataId="${jfzbVo.id }" class="optUpdateBtn green">修改</a> |
													</c:if>
													 	<a href="javascript:void(0)" comId="${jfzbVo.comId}" dataId="${jfzbVo.id }" class="optViewBtn blue">查看</a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</form>
							<tags:pageBar url="/jfzb/listPagedJfzb"></tags:pageBar>
						</div>
						</c:when>
					<c:otherwise>
						<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
							<section class="error-container text-center">
								<h1>
									<i class="fa fa-exclamation-triangle"></i>
								</h1>
								<div class="error-divider">
									<h2>您还没有录入过指标信息！</h2>
									<p class="description">协同提高效率，分享拉近距离。</p>
									<a href="javascript:void(0);" id="addJfzbBtn"
										class="return-btn "><i class="fa fa-plus"></i>指标录入</a>
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
        
        
        
        <script type="text/javascript">
			$(function(){
				//文本框绑定回车提交事件
				$("#describe").blur(function(){
			      	$("#searchForm").submit();
			    });
				//添加积分标准
				$("body").on("click","#addJfzbTypeBtn",function(){
					jfzbListForm.addJfzbType();
				});
				$("body").on("click",".optUpdateBtn",function(){
					var jfzbId = $(this).attr("dataId");
					jfzbListForm.updateJfzb(jfzbId);
				});
				$("body").on("click",".optViewBtn",function(){
					var jfzbId = $(this).attr("dataId");
					jfzbListForm.viewJfzb(jfzbId);
				});
				//添加积分标准
				$("body").on("click","#addJfzbBtn",function(){
					jfzbListForm.addJfzb();
				});
				
				$("body").on("click",".batchDelJfzb",function(){
					if(checkCkBoxStatus('ids')){
						window.top.layer.confirm('确定要删除被选择的数据吗？', {icon: 3, title:'提示'}, function(index){
						  window.top.layer.close(index);
						  var url = reConstrUrl('ids');
						  $("#delForm input[name='redirectPage']").val(url);
						  $('#delForm').submit();
						});
					}else{
						window.top.layer.msg('请先选择需要删除的数据', {icon: 6});
					}
				});
				
				jfzbListForm.loadJfzbType();
			});




$(function(){
	//操作删除和复选框
	$('.optTr').mouseover(function(){
		var display = $(this).find(".optTd .optCheckBox").css("display");
		if(display=='none'){
			$(this).find(".optTd .optCheckBox").css("display","block");
			$(this).find(".optTd .optRowNum").css("display","none");
		}
	});
	$('.optTr ').mouseout(function(){
		var optCheckBox = $(this).find(".optTd .optCheckBox");
			var check = $(optCheckBox).find("input").attr('checked');
			if(check){
				$(this).find(".optTd .optCheckBox").css("display","block");
				$(this).find(".optTd .optRowNum").css("display","none");
			}else{
				$(this).find(".optTd .optCheckBox").css("display","none");
				$(this).find(".optTd .optRowNum").css("display","block");
			}
	});
	
	$(":checkbox[name='ids'][disabled!='disabled']").click(function(){
		var checkLen = $(":checkbox[name='ids'][disabled!='disabled']:checked").length;
		var len = $(":checkbox[name='ids'][disabled!='disabled']").length;
		if(checkLen>0){
			if(checkLen==len){
				$("#checkAllBox").attr('checked', true);
			}else{
				$("#checkAllBox").attr('checked', false);
			}
		}else{
			
			$("#checkAllBox").attr('checked', false);
		}
	});
});
</script>
</body>
</html>
