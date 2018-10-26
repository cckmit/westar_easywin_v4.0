<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/fileJs/fileCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
var pageTag = 'fileCenter';
function formSub(){
	$("#fileSearchForm").find("input[name='startDate']").val($("#startDate").val());
	$("#fileSearchForm").find("input[name='endDate']").val($("#endDate").val());
	$("#fileSearchForm").submit();
}
$(function(){
	//更多筛选条件显示层
	$("#moreFilterCondition").click(function(){
        var display = $("#moreFilterCondition_div").css("display");
        if("none"==display){
            $(this).html('隐藏');
            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
        }else if("block"==display){
            $(this).html('更多筛选')
            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
        }

      });
	//任务名筛选
	$("#fileName").blur(function(){
		formSub();
	});
	//文本框绑定回车提交事件
	$("#fileName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	formSub();
        }
    });
});

//查询文档类型
function setFileTypes(fileTypes){
	$("#searchAll").val(fileTypes+'');
	$("#fileSearchForm").submit();
}
//模块类型
function setModType(modTypes){
	$("#modTypes").val(modTypes+'');
	$("#fileSearchForm").submit();
}
//返回选中的附件
function returnFiles(){
	var boxes =$("#contentBody").find("input[name='ids']");
	var files = new Array();
	for(i=0;i<boxes.length;i++){
        if(boxes[i].checked == true){
        	var fileId= boxes[i].value;
        	var file = {"id":fileId,
        			"name":$("#filename_"+fileId).val(),
        			"size":$("#filesize_"+fileId).val()}
        	files.push(file);
        }
    }
	return files;
}
</script>
<style>
.btn5 {
	padding: 6px 7px;
}
</style>
<style>
.panel-heading {
	padding: 5px 20px
}

.panel-body {
	padding: 0px;
}

.table {
	margin-bottom: 0px;
}

.table tbody>tr>td {
	padding: 5px 0px;
}

.pagination-lg>li>a {
	font-size: small;
}

.pagination {
	margin: 10px 0px
}
</style>
</head>
<body>
	<div class="widget no-margin bg-white">
		<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
			<form action="/fileCenter/listPagedFileForSelect?pager.pageSize=8" id="fileSearchForm">
				<input type="hidden" id="redirectPage" name="redirectPage" />
				<input type="hidden" name="sid" value="${param.sid}" />
				<input type="hidden" name="pager.pageSize" value="8" />
				<input name="searchAll" type="hidden" id="searchAll" value="${fileDetail.searchAll}" />
				<input name="searchMe" type="hidden" value="${fileDetail.searchMe}" />
				<input name="searchType" type="hidden" value="${fileDetail.searchType}" />
				<div class="btn-group pull-left searchCond">
					<div class="table-toolbar ps-margin">
						<div class="pull-left padding-right-40" style="font-size: 18px">
							<span class="widget-caption themeprimary ps-layerTitle">附件列表</span>
						</div>
					</div>
					<div class="table-toolbar ps-margin">
						<div class="btn-group">
							<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown"> <c:choose>
									<c:when test="${not empty fileDetail.searchAll}">
										<font style="font-weight: bold;">
											<c:if test="${fileDetail.searchAll=='31'}">Office文档</c:if>
											<c:if test="${fileDetail.searchAll=='32'}">文本文档</c:if>
											<c:if test="${fileDetail.searchAll=='33'}">PDF文档</c:if>
											<c:if test="${fileDetail.searchAll=='34'}">图片文档</c:if>
											<c:if test="${fileDetail.searchAll=='35'}">音频文档</c:if>
											<c:if test="${fileDetail.searchAll=='36'}">视频文档</c:if>
											<c:if test="${fileDetail.searchAll=='37'}">其他类型</c:if>
										</font>
									</c:when>
									<c:otherwise>全部类型</c:otherwise>
								</c:choose> <i class="fa fa-angle-down"></i>
							</a>
							<ul class="dropdown-menu dropdown-default">
								<li>
									<a href="javascript:void(0)" onclick="setFileTypes('')">全部类型</a>
								</li>
								<li>
									<a href="javascript:void(0)" onclick="setFileTypes('31')"> Office文档</a>
								</li>
								<li>
									<a href="javascript:void(0)" onclick="setFileTypes('32')">文本文档</a>
								</li>
								<li>
									<a href="javascript:void(0)" onclick="setFileTypes('33')">PDF文档</a>
								</li>
								<li>
									<a href="javascript:void(0)" onclick="setFileTypes('34')">图片文档</a>
								</li>
								<li>
									<a href="javascript:void(0)" onclick="setFileTypes('35')">音频文档</a>
								</li>
								<li>
									<a href="javascript:void(0)" onclick="setFileTypes('36')">视频文档</a>
								</li>
								<li>
									<a href="javascript:void(0)" onclick="setFileTypes('37')">其他类型</a>
								</li>
							</ul>
						</div>
					</div>
					<div class="table-toolbar ps-margin">
						<div class="btn-group">
							<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown"> <c:choose>
									<c:when test="${fileDetail.searchMe=='21'}">自己上传的</c:when>
									<c:when test="${fileDetail.searchMe=='22'}">他人分享的</c:when>
									<c:otherwise>全部范围</c:otherwise>
								</c:choose> <i class="fa fa-angle-down"></i>
							</a>
							<ul class="dropdown-menu dropdown-default">
								<li>
									<a href="javascript:void(0)" onclick="gets_value('',this,'searchMeC','searchMe')">不限条件</a>
								</li>
								<li>
									<a href="javascript:void(0)" onclick="gets_value('21',this,'searchMeC','searchMe')">自己上传的</a>
								</li>
								<li>
									<a href="javascript:void(0)" onclick="gets_value('22',this,'searchMeC','searchMe')">他人分享的</a>
								</li>
							</ul>
						</div>
					</div>

					<div class="table-toolbar ps-margin">
						<div class="btn-group cond" id="moreCondition_Div">
							<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
									<c:when test="${not empty fileDetail.startDate || not empty fileDetail.endDate}">
										<font style="font-weight: bold;">筛选中</font>
									</c:when>
									<c:otherwise>更多</c:otherwise>
								</c:choose> <i class="fa fa-angle-down"></i>
							</a>
							<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
								<div class="ps-margin ps-search padding-left-10">
									<span class="btn-xs">起止时间：</span>
									<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${fileDetail.startDate}" id="startDate" name="startDate" placeholder="开始时间"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
									<span>~</span>
									<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${fileDetail.endDate}" id="endDate" name="endDate" placeholder="结束时间"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
								</div>
								<div class="ps-clear padding-top-10" style="text-align: center;">
									<button type="submit" class="btn btn-primary btn-xs">查询</button>
									<button type="button" class="btn btn-default btn-xs margin-left-10" onclick="resetMoreCon('moreCondition_Div')">重置</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="ps-margin ps-search">
					<span class="input-icon"> <input id="fileName" name="fileName" value="${fileDetail.fileName}" class="form-control ps-input" type="text" placeholder="请输入关键字"> <a
						href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
					</span>
				</div>
			</form>
		</div>
		<div class="widget-body" id="contentBody">
			<table class="table table-striped table-hover general-table fixTable">
				<thead>
					<tr>
						<th width="5%" valign="middle">
							<c:choose>
								<c:when test="${not empty param.fileNum && param.fileNum eq '1'}">
									&nbsp;
								</c:when>
								<c:otherwise>
									<label> 
										<input type="checkbox" name="input10" id="input12" onclick="checkAllPre(this,'ids')" /> <span class="text"></span>
									</label>
								</c:otherwise>
							</c:choose>
						
						</th>
						<th>文件名</th>
						<th width="10%">大小</th>
						<th width="30%">来源</th>
						<th width="12%">创建人</th>
						<th width="13%">创建时间</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty listFiles}">
							<c:forEach items="${listFiles}" var="fileDetialObj" varStatus="status">
								<input type="hidden" id="filename_${fileDetialObj.fileId}" value="${fileDetialObj.fileName}">
								<input type="hidden" id="filesize_${fileDetialObj.fileId}" value="${fileDetialObj.sizem}">
								<tr height="40">
									<td align="center">
										<label>
											<c:choose>
												<c:when test="${not empty param.fileNum && param.fileNum eq '1'}">
													<input type="radio" name="ids" value="${fileDetialObj.fileId}" />
												</c:when>
												<c:otherwise>
													<input type="checkbox" name="ids" value="${fileDetialObj.fileId}" />
												</c:otherwise>
											</c:choose>
											<span class="text"></span>
										</label>
									</td>
									<td>
										<tags:cutString num="36">${fileDetialObj.fileName}</tags:cutString>
									</td>
									<td>${fileDetialObj.sizem}</td>
									<td>
										<tags:cutString num="25">${fileDetialObj.fileDescribe}</tags:cutString>
									</td>
									<td>${fileDetialObj.userName}</td>
									<td>${fn:substring(fileDetialObj.fileDate,0,10)}</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td height="30" colspan="5" align="center">
									<h3>没有附件可选！</h3>
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
			<tags:pageBar url="/fileCenter/listPagedFileForSelect"></tags:pageBar>
		</div>
	</div>
	<!-- 筛选下拉所需 -->
	<script src="/static/assets/js/bootstrap.min.js"></script>
	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>

