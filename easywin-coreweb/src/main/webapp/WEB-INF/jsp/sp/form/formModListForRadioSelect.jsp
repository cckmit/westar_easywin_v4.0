<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" class="bg-white">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
//状态筛选
function formModStateFilter(stateId){
	$("#state").val(stateId);
	$("#searchForm").submit();
}
//责任人筛选
function userOneForUserIdCallBack(userId,tag){
	$("#owner").val(userId);
	$("#searchForm").submit();
}
$(function(){
	//操作删除和复选框
	$('tr').click(function(){
		var radio = $(this).find("input[type=radio]");
		$(radio).attr("checked","checked")
	});
	//名名称筛选
	$("#formModName").blur(function(){
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#formModName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#formModName").val())){
        		$("#searchForm").submit();
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#formModName").focus();
        	}
        }
    });
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
});
//选择表单出发
function formModSelected(){
	var result;
	var radio = $("input[type=radio]:checked");
	if(radio.length==0){
		window.top.layer.alert("请选择表单信息",{icon:7});
	}else{
		var formModId = $(radio).attr("formModId"); 
		var formModName = $(radio).attr("formModName"); 
		result={"formModId":formModId,"formModName":formModName};
	}
	return result;
}

//窗体点击事件检测
document.onclick = function(e){
	var evt = e?e:window.event;
	var ele = $(evt.srcElement || evt.target);
	if ($(ele).parents("#moreCondition_Div").length == 1) { 
		if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
			return false;
		}else{
			if(!$("#moreCondition_Div").hasClass("open")){
				$(".searchCond").find(".open").removeClass("open");
				$("#moreCondition_Div").addClass("open");
			}
		}
	} else{
		$("#moreCondition_Div").removeClass("open");
	}
}
/**
 * 展示查询条件中更多
 */
function displayMoreCond(divId){
	if($("#"+divId).hasClass("open")){
		$("#"+divId).removeClass("open");
	}else{
		$("#"+divId).addClass("open")
	}
}
/**
 * 清空更多查询条件
 */
function resetMoreCon(divId){
	$("#"+divId).find("input").val('');
	
}
$(function(){
	$("body").on("click",".formSortSelect",function(){
		var actObj = $(this);
		var formSortId = $(actObj).attr("formSortId");
		var modSortName = $(actObj).text();
		if(formSortId==0){
			modSortName = '其他';
		}else if(formSortId==-1){
			modSortName = '';
			
		}
		modSortName = $.trim(modSortName);
		$("#searchForm").find("input[name='formSortId']").val(formSortId>-1?formSortId:"")
		$("#searchForm").find("input[name='modSortName']").val(formSortId>-1?modSortName:"")
		$("#searchForm").submit();
	});
});
</script>
<style>
.table {
	margin-bottom: 0px;
}

.table tbody>tr>td {
	padding: 5px 0px;
}

tr {
	cursor: auto;
}
.ps-page{
	padding-top: 20px !important;
}
</style>
</head>
<body>
	<div class="widget no-margin bg-white">
		<div
			class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
			<form action="/form/formModListForSelect" id="searchForm">
				<input type="hidden" id="redirectPage" name="redirectPage" /> 
				<input type="hidden" name="sid" value="${param.sid}" /> 
				<input type="hidden" name="pager.pageSize" value="9" />
				<input type="hidden" id="formModId" name="formModId" value="${param.formModId}" />

				<div class="btn-group pull-left">
					<div class="table-toolbar ps-margin">
						<div class="pull-left padding-right-20" style="font-size: 18px">
			             	<span class="widget-caption themeprimary ps-layerTitle">表单候选列表</span>
						</div>
					</div>
					<div class="btn-group pull-left">
						<div class="table-toolbar ps-margin">
							<div class="btn-group">
								<a class="btn btn-default dropdown-toggle btn-xs"
									data-toggle="dropdown"> <c:choose>
										<c:when test="${not empty formMod.formSortId}">
											<font style="font-weight:bold;">${formMod.modSortName}
											</font>
										</c:when>
										<c:otherwise>分类</c:otherwise>
									</c:choose> <i class="fa fa-angle-down"></i>
								</a>
								<ul class="dropdown-menu dropdown-default">
									<input type="hidden" name="formSortId" value="${formMod.formSortId}" />
									<input type="hidden" name="modSortName" value="${formMod.modSortName}" />
									<li><a href="javascript:void(0)" class="formSortSelect" formSortId="-1">不限条件</a></li>
									<c:choose>
										<c:when test="${not empty listFormSort}">
											<c:forEach items="${listFormSort}" var="sortObj"
												varStatus="vs">
												<li>
													<a href="javascript:void(0)" class="formSortSelect" formSortId="${sortObj.id}">${sortObj.sortName}</a>
												</li>
											</c:forEach>
										</c:when>
									</c:choose>
									<li><a href="javascript:void(0)" class="formSortSelect" formSortId="0">其他</a></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<div class="ps-margin ps-search">
					<span class="input-icon"> <input id="formModName"
						name="modName" value="${formMod.modName}"
						class="form-control ps-input" type="text" placeholder="请输入关键字">
						<a href="javascript:void(0)" class="ps-searchBtn"><i
							class="glyphicon glyphicon-search circular danger"></i></a>
					</span>
				</div>
				
				<div class="widget-buttons">
					<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
						<i class="fa fa-times themeprimary"></i>
					</a>
				</div>
			</form>
		</div>
		<div class="widget-body" id="contentBody">
			<table class="table table-striped table-hover general-table">
				<thead>
					<tr>
						<th style="width: 10%;" valign="middle">选项</th>
						<th valign="middle" class="hidden-phone">表单</th>
						<th style="width: 80px;" valign="middle">创建于</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty formModlist}">
							<c:forEach items="${formModlist}" var="formModVo" varStatus="status">
								<tr>
									<td style="text-align: center;">
										<label> 
											<input
												class="colored-blue" type="radio" name="formModId"
												${formModId==formModVo.id?'checked="checked"':'' }
												value="${formModVo.id}" formModId="${formModVo.id}"
												formModName="${formModVo.modName}" /> 
											<span class="text">&nbsp;</span>
										</label>
									</td>
									<td><tags:cutString num="26">${formModVo.modName}</tags:cutString>
									</td>
									<td>${fn:substring(formModVo.recordCreateTime,0,10) }</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td height="30" colspan="6" align="center"><h3>没有可用表单！</h3></td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
			<tags:pageBar url="/form/formModListForSelect"></tags:pageBar>
		</div>
	</div>
	<!-- 筛选下拉所需 -->
	<script src="/static/assets/js/bootstrap.min.js"></script>
	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>

</body>
</html>
