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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript"> 
/**
 * 返回意见
 */
//项目查看权限验证
 function viewTask(id){
	 $.post("/task/authorCheck?sid=${param.sid}",{Action:"post",taskId:id},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(2,msgObjs.promptMsg);
				}else{
					var url="/task/viewTask?sid=${param.sid}&id="+id
					+"&redirectPage="+encodeURIComponent('${param.redirectPage}')
					window.top.layer.open({
						  type: 2,
						  title: false,
						  closeBtn: 0,
						  shadeClose: true,
						  shade: 0.1,
						  shift:0,
						  fix: true, //固定
						  maxmin: false,
						  move: false,
						  area: ['800px', '550px'],
						  content: [url,"no"] //iframe的url
						});
					}
		},"json");
 }
 
 $(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
 })
</script>
<style type="text/css">
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

<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	<span class="widget-caption themeprimary ps-layerTitle">相似任务列表</span>
    <div class="widget-buttons">
		<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
			<i class="fa fa-times themeprimary"></i>
		</a>
	</div>
</div>
<div id="contentBody" class="widget-body margin-top-40 no-padding" style="overflow-y:auto;position: relative;">
	 <table class="table table-striped table-hover general-table">
        	<thead>
           		<tr>
                   <th style="width: 10%;" valign="middle">序号</th>
				<th valign="middle" class="hidden-phone">名称</th>
				<th style="width: 80px;" valign="middle">发起人</th>
				<th width="14%" valign="middle">创建日期</th>
               </tr>
          </thead>
          <tbody>
          	<c:choose>
			 	<c:when test="${not empty listTask}">
			 		<c:forEach items="${listTask}" var="obj" varStatus="status" >
			           	<tr>
			           		<td style="text-align: center;">
						 			${status.count}
			           		</td>
			           		<td>
			           			<a href="javascript:void(0);" onclick="viewTask(${obj.id});">${obj.taskName}</a>
			           		</td>
			           		<td>
								<img src="/downLoad/userImg/${obj.comId}/${obj.owner}?sid=${param.sid}"
									title="${obj.ownerName}" class="user-avatar"/>
								<span>${obj.ownerName}</span>
			           		</td>
			           		<td>
			           			${fn:substring(obj.recordCreateTime,0,10) }
			           		</td>
			             </tr>
			 		</c:forEach>
			 	</c:when>
			 </c:choose>
          </tbody>
    </table>
	<tags:pageBar url="/task/similarTasksPage"></tags:pageBar></div>
	
</div>


</body>
</html>

