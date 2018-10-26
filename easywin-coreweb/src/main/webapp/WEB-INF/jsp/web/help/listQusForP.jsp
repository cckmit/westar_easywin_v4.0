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
$(function(){
	//操作删除和复选框
	$('tr').click(function(){
		var radio = $(this).find("input[type=radio]");
		$(radio).attr("checked","checked")
	});
	//名名称筛选
	$("#nameCheck").blur(function(){
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#nameCheck").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#nameCheck").val())){
        		$("#searchForm").submit();
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#nameCheck").focus();
        	}
        }
    });
});
//选择项目出发
function helpSelected(){
	var result;
	var radio = $("input[type=radio]:checked");
	if(radio.length==0){
		window.top.layer.alert("请选择项目信息",{icon:7});
	}else{
		var helpId = $(radio).attr("helpId"); 
		var helpName = $(radio).attr("helpName"); 
		result={"helpId":helpId,"helpName":helpName};
	}
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
</style>
</head>
<body>
<div class="widget no-margin bg-white">
     	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
			<form action="/web/help/listQusForP" id="searchForm">
			  <input type="hidden" id="redirectPage" name="redirectPage"/>
			 <input type="hidden" name="pager.pageSize" value="7"/>
			 <input type="hidden" name="id" value="${ownerKey}"/>
            <div class="ps-margin ps-search">
				<span class="input-icon">
				<input id="nameCheck" name="nameCheck" value="${nameCheck}" class="form-control ps-input" type="text" placeholder="请输入关键字">
				<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
				</span>
			</div>
		</form>
      </div>
      <div class="widget-body">
         <table class="table table-striped table-hover general-table">
        	<thead>
           	<tr>
                   <th style="width: 10%;" valign="middle">选项</th>
				<th valign="middle" class="hidden-phone">名称</th>
				<th width="14%" valign="middle">时间</th>
               </tr>
          </thead>
          <tbody>
          	<c:choose>
			 	<c:when test="${not empty list}">
			 		<c:forEach items="${list}" var="helpVo" varStatus="status" >
			           	<tr>
			           		<td style="text-align: center;">
						 			<label>
					           			<input class="colored-blue" type="radio" name="helpId" 
					           			value="${helpVo.id}" helpId="${helpVo.id}" helpName="${helpVo.name}"/>
										<span class="text">&nbsp;</span>
									</label>
			           		</td>
			           		<td>
			           			<tags:cutString num="26">${helpVo.name}</tags:cutString>
			           		</td>
			           		<td>
			           			${fn:substring(helpVo.recordCreateTime,0,10) }
			           		</td>
			             </tr>
			 		</c:forEach>
			 	</c:when>
			 	<c:otherwise>
			 		<tr>
			 			<td height="30" colspan="6" align="center"><h3>还是空的！</h3></td>
			 		</tr>
			 	</c:otherwise>
			 </c:choose>
      	</tbody>
  	</table> 
     <tags:pageBar url="/web/help/listQusForP"></tags:pageBar>
    </div>
</div>
<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>

</body>
</html>
