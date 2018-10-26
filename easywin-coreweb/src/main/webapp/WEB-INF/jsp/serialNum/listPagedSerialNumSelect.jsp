<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" class="bg-white">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
//状态筛选
function itemStateFilter(stateId){
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
	$("#remark").blur(function(){
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#remark").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#remark").val())){
        		$("#searchForm").submit();
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#remark").focus();
        	}
        }
    });
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
});
//选择项目出发
function serialNumSelected(){
	var result;
	var radio = $("input[type=radio]:checked");
	if(radio.length==0){
		window.top.layer.alert("请选择编号信息",{icon:7});
	}else{
		var serialNumId = $(radio).attr("serialNumId"); 
		var remark = $(radio).attr("serialNumRemark"); 
		var startNum = $(radio).attr("startNum"); 
		result={"id":serialNumId,"remark":remark,"startNum":startNum};
	}
	console.log(result)
	return result;
}

</script>
<style>
.table{
margin-bottom:0px;
}
.table tbody>tr>td{
	padding: 5px 0px;
}
tr{
cursor: auto;
}
.table thead>tr>th{
	line-height: 25px
}
.table tbody>tr>td{
	line-height: 25px
}
</style>
</head>
<body>
<div class="widget no-margin bg-white">
     	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
			<form action="/serialNum/listPagedSerialNumSelect" id="searchForm">
			  <input type="hidden" id="redirectPage" name="redirectPage"/>
			 <input type="hidden" name="sid" value="${param.sid}"/>
			 <input type="hidden" name="pager.pageSize" value="8"/>
								
             
			<div class="btn-group pull-left">
				<div class="table-toolbar ps-margin">
					<div class="pull-left padding-right-40" style="font-size: 18px">
		             	<span class="widget-caption themeprimary ps-layerTitle">编号列表</span>
					</div>
			     </div>
			</div>
            <div class="ps-margin ps-search">
				<span class="input-icon">
				<input id="remark" name="remark" value="${serialNum.remark}" class="form-control ps-input" type="text" placeholder="请输入关键字">
				<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
				</span>
			</div>
		</form>
      </div>
      <c:choose>
 		<c:when test="${not empty serialNums}">
		
      <div class="official-table padding-10" id="contentBody" style="overflow-y:auto;position: relative;width:100%;">
      <div class="table-content">
         <table class="table table-striped table-hover general-table">
        	<thead>
           		<tr>
                   <th style="width: 10%;" valign="middle">选项</th>
				<th valign="middle" style="width: 10%;"  class="hidden-phone">起始值</th>
				<th valign="middle" class="hidden-phone">说明</th>
				<th width="19%" valign="middle">时间</th>
               </tr>
          </thead>
          <tbody>
			 		<c:forEach items="${serialNums}" var="serialNumVo" varStatus="status" >
			           	<tr>
			           		<td style="text-align: center;">
						 			<label>
					           			<input class="colored-blue" type="radio" name="serialNumId"
					           			value="${serialNumVo.id}" serialNumId="${serialNumVo.id}" 
					           			startNum="${serialNumVo.startNum}"
					           			serialNumRemark="${serialNumVo.remark}"/>
										<span class="text">&nbsp;</span>
									</label>
			           		</td>
			           		<td>
			           			${serialNumVo.startNum}
			           		</td>
			           		<td>
			           			<tags:cutString num="26">${serialNumVo.remark}</tags:cutString>
			           		</td>
			           		<td>
			           			${fn:substring(serialNumVo.recordCreateTime,0,16) }
			           		</td>
			             </tr>
			 		</c:forEach>
			 	</c:when>
			 	<c:otherwise>
			 		<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
						<section class="error-container text-center">
							<h1>
								<i class="fa fa-exclamation-triangle"></i>
							</h1>
							<div class="error-divider">
								<h2>未设定序列编号！</h2>
								<p class="description">协同提高效率，分享拉近距离。</p>
							</div>
						</section>
					</div>
			 	</c:otherwise>
			 </c:choose>
      	</tbody>
  	</table> 
    </div>
     <tags:pageBar url="/serialNum/listPagedSerialNumSelect"></tags:pageBar>
     </div>
</div>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>

</html>
