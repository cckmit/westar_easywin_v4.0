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
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>

<script type="text/javascript">
var sid="${param.sid}";
//附件数据源查看
function viewSource(id,type){
	if("task"==type){
		authCheck(id,"003",-1,function(authState){
			var url = "/task/viewTask?sid=${param.sid}&id="+id;
			var height = $(window.parent).height();
			openWinWithPams(url,"800px",(height-90)+"px");
		})
	}else if("demand"==type){
		var url = "/demand/viewDemandPage?sid=${param.sid}&demandId="+id;
		var height = $(window.parent).height();
		openWinWithPams(url,"800px",(height-90)+"px");
	}
}
</script>
<script> 
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			}
		}); 
		//初始化名片
		initCard('${param.sid}');
		 //页面刷新
		$("#refreshImg").click(function(){
			window.self.location.reload();
		});
		 
		$("#filename").blur(function(){
			//启动加载页面效果
			layer.load(0, {shade: [0.6,'#fff']});
			$("#searchForm").submit();
		});
		//文本框绑定回车提交事件
		$("#filename").bind("keydown",function(event){
	        if(event.keyCode == "13")    
	        {
	        	if(!strIsNull($("#filename").val())){
	        		//启动加载页面效果
	        		layer.load(0, {shade: [0.6,'#fff']});
					$("#searchForm").submit();
	        	}else{
	        		showNotification(1,"请输入检索内容！");
	    			$("#filename").focus();
	        	}
	        }
	    });
		 
	}); 
	
</script>
<style type="text/css">
.table tbody>tr>td{
	padding: 5px 0px;
}
</style>
</head>
<body onload="resizeVoteH('otherItemAttrIframe')" style="background-color:#FFFFFF;">
<form action="/item/maintenanceRecord" id="searchForm" class="subform">	
	<input type="hidden" name="pager.pageSize" value="10">
	<input type="hidden" name="sid" value="${param.sid}"> 
	<input type="hidden" name="itemId" value="${param.itemId}">
	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary" style="border-bottom: 0">
		<div>
			<div class="ps-margin ps-search">
				<span class="input-icon">
					<input name="sourceName" id="filename" value="${itemUpfile.sourceName}" class="form-control ps-input formElementSearch" type="text" placeholder="请输入关键词" >
											<a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
				</span>
			</div>
		</div>
	</div>
 </form>
<table class="table table-striped table-hover general-table fixTable">
     	<thead>
        	<tr>
                <th style="width:50px;text-align: center;" align="center">序号</th>
				<th style="width:150px;" align="center">所属</th>
				<th style="width:100px;" align="center">创建人</th>
				<th style="width:120px;" align="center">创建时间</th>
            </tr>
       </thead>
  
       <tbody>
       	<c:choose>
	 	<c:when test="${not empty lists}">
	 		<c:forEach items="${lists}" var="file" varStatus="status">
	 			<tr>
	 				<td style="text-align: center;">${status.count}</td>
	 				<td title="${file.type}">
	 					<c:choose>
	 						<c:when test="${file.type=='task'}">【任务】</c:when>
	 						<c:when test="${file.type=='demand'}">【需求】</c:when>
	 					</c:choose>
	 					<a href="javascript:void(0);" onclick="viewSource(${file.id},'${file.type}');" style="color:blue;">
							${file.sourceName}
						</a>
	 				</td>
	 				<td style="text-align: left;">
	 					<div class="ticket-user pull-left other-user-box">
							<img src="/downLoad/userImg/${userInfo.comId}/${file.userId}"
								title="${file.creatorName}" class="user-avatar" />
							<span class="user-name">${file.creatorName}</span>
						</div>
	 				</td>
	 				<td>${fn:substring(file.recordCreateTime,0,16)}</td>
	 			</tr>
	 		</c:forEach>
	 	</c:when>
	 	<c:otherwise>
	 		<tr>
	 			<td height="30" colspan="4" align="center"><h3>没有相关信息！</h3></td>
	 		</tr>
	 	</c:otherwise>
	 </c:choose>
       </tbody>
      </table>
<tags:pageBar url="/item/maintenanceRecord"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>