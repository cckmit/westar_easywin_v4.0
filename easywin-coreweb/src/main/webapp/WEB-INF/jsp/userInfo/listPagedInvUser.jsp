<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	//批量取消邀请记录
	function del(){
		layui.use('layer', function(){
			var layer = layui.layer;
			if(checkCkBoxStatus('ids')){
				window.top.layer.confirm('确定要取消邀请吗？', {
					  btn: ['确定','取消']//按钮
				  ,title:'询问框'
				  ,icon:3
				},  function(index){
					window.top.layer.close(index);
					$("#delForm input[name='redirectPage']").val(window.location.href);
					$('#delForm').attr("action","/userInfo/delInvUser");
					$('#delForm').submit();
				});	
			}else{
				window.top.layer.alert('请先选择一条邀请记录。');
			}
		});
	}
	//批量重新邀请
	function invUser(){
		layui.use('layer', function(){
			var layer = layui.layer;
			if(checkCkBoxStatus('ids')){
				window.top.layer.confirm('确定要重新邀请吗？', {
					  btn: ['确定','取消']//按钮
				  ,title:'询问框'
				  ,icon:3
				}, function(index){
					window.top.layer.close(index);
					$("#delForm input[name='redirectPage']").val(window.location.href);
					$('#delForm').attr("action","/userInfo/resendEmail");
					$('#delForm').submit();
				});	
			}else{
				window.top.layer.alert('请先选择一条邀请记录。');
			}
			
		});
	}

</script>
<script type="text/javascript">
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

</head>
<body onload="resizeVoteH('invUser')" >
                	<div class="widget">
                     	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                        	<span class="widget-caption themeprimary margin-top-10" style="font-size: 20px">邀请记录</span>
                        	
                        	<div class="widget-buttons">
                        	<!-- 
                        		<div class="table-toolbar ps-margin">
									<a class="btn btn-default dropdown-toggle btn-sm" data-toggle="dropdown" onclick="invUser()" >
										重新邀请
									</a>
								</div>
                        	 -->
                        		<div class="table-toolbar ps-margin">
									<a class="btn btn-default dropdown-toggle btn-sm" data-toggle="dropdown" onclick="del()" >
										取消邀请
									</a>
								</div>
                        	</div>
                        </div>
                        <c:choose>
									 	<c:when test="${not empty list}">
                        <div class="widget-body">
                        	<form method="post" id="delForm">
							     <tags:token></tags:token>
								 <input type="hidden" name="redirectPage"/>
								<table class="table table-hover general-table">
									<thead>
									  <tr>
									  	<td width="10%">
											<label>
												<input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'ids')">
												<span class="text" style="color: inherit;"></span>
											</label>
										</td>
										<td>
											<h5>邀请账号</h5></td>
										<td width="25%">
											<h5>邀请时间</h5></td>
										<td width="25%">
											<h5>加入状态</h5></td>
									  </tr>
									</thead>
									<tbody>
									
									 		<c:forEach items="${list}" var="joinRecord" varStatus="status">
									 		
									 		<tr class="optTr">
                                                <td class="optTd" style="height: 47px">
										 			<label class="optCheckBox" style="display: none;width: 20px">
									 					<input class="colored-blue" type="checkbox" name="ids" ${joinRecord.state==1?'disabled="disabled"':''} 
									 				value="${joinRecord.id}" state="${joinRecord.state}" />
									 					<span class="text"></span>
										 			</label>
                                                	<label class="optRowNum" style="display: block;width: 20px">${status.count}</label>
                                                </td>
									 				<td>${joinRecord.account }</td>
									 				<td>${ joinRecord.recordCreateTime}</td>
									 				<td>${joinRecord.state==1?"已加入":"待加入"}</td>
									 			</tr>
									 		</c:forEach>
									 	
									</tbody>
								</table>
								 <tags:pageBar url="/userInfo/listPagedInvUser"></tags:pageBar>
							 </form>
                         </div>
                         </c:when>
									 	<c:otherwise>
									 		<div class="widget-body" style="height:300px; text-align:center;padding-top:65px">
							<section class="error-container text-center">
								<h1>
									<i class="fa fa-exclamation-triangle"></i>
								</h1>
								<div class="error-divider">
									<h2>您还没相关邀请记录！</h2>
									<p class="description">协同提高效率，分享拉近距离。</p>
								</div>
							</section>
						</div>
									 	</c:otherwise>
									 </c:choose>
                     </div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
