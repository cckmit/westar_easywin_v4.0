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
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<!-- 筛选下拉所需 -->
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>
<script type="text/javascript">
var sid="${param.sid}";
$(function(){
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
//任务查看权限验证
function viewTask(taskId){
	authCheck(taskId,"003",-1,function(authState){
		var url = "/task/viewTask?sid=${param.sid}&id="+taskId;
		var height = $(window.parent).height();
		openWinWithPams(url,"800px",(height-90)+"px");
	})
}
//删除任务附件
function delTaskFile(ts,taskId,taskFileId,type){
	window.top.layer.confirm("确定删除附件?", {icon: 3, title:'确认对话框'}, function(index){
		window.top.layer.close(index);
		$.post("/task/delTaskUpfile?sid=${param.sid}",{Action:"post",taskId:taskId,taskFileId:taskFileId,type:type},     
				function (data){
				if(data.status=='y'){
					showNotification(1,"操作成功");
					$(ts).parents("tr").remove();
					$.each($(".fileOrder"),function(index,item){
						$(this).html(index+1)
					})
				}else{
					showNotification(2,data.info);
					
				}
		},"json");
	});
	
	
}
</script>
<script> 
	$(function() { 
		$('.grid_tab tr').addClass('odd'); 
		$('.grid_tab tr:even').addClass('even');
	}); 
</script>
</head>
<body onload="resizeVoteH('otherTaskAttrIframe');" style="background-color:#FFFFFF;">
<form action="/task/taskUpfilePage" id="searchForm" class="subform">	
		<input type="hidden" name="pager.pageSize" value="10">
		<input type="hidden" name="sid" value="${param.sid}"> 
		<input type="hidden" name="taskId" value="${param.taskId}">
		<input type="hidden" name="order"  value="${taskUpfile.order}">
	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary" style="border-bottom: 0">
		<div class="table-toolbar ps-margin">
			<div class="btn-group">
	            <a class="btn btn-default dropdown-toggle btn-xs"
	               data-toggle="dropdown"> <c:choose>
	                <c:when test="${not empty taskUpfile.order}">
	                        <font style="font-weight:bold;">
	                            <c:choose>
	                                <c:when test="${taskUpfile.order=='fileExt'}">按文档类型</c:when>
	                                <c:when test="${taskUpfile.order=='userId'}">按上传人</c:when>
	                            </c:choose>
	                        </font>
	                    </c:when>
	                    <c:otherwise>排序</c:otherwise>
	                </c:choose> <i class="fa fa-angle-down"></i>
	                </a>
	                <ul class="dropdown-menu dropdown-default">
	                    <li><a href="javascript:void(0)" class="clearThisElement" relateElement="order">不限条件</a>
	                    </li>
	                    <li><a href="javascript:void(0)" class="setElementValue" relateElement="order" dataValue="fileExt">按文档类型</a>
	                    </li>
	                     <li><a href="javascript:void(0)" class="setElementValue" relateElement="order" dataValue="userId">按上传人</a>
	                    </li>
	                </ul>
	           	</div>
			</div>
		<div>
			<div class="ps-margin ps-search">
				<span class="input-icon">
					<input name="filename" id="filename" value="${taskUpfile.filename}" class="form-control ps-input formElementSearch" type="text" placeholder="请输入关键词" >
											<a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
				</span>
			</div>
		</div>
	</div>
 </form>
<table class="table table-hover general-table fixTable">
	<thead>
		<tr>
			<th width="10%" valign="middle">序号</th>
			<th width="30%" valign="middle">附件名称</th>
			<th width="20%" valign="middle">来源于</th>
			<th style="width:120px;" valign="middle">上传时间</th>
			<th style="width:100px;" valign="middle">上传人</th>
			<th width="12%" valign="middle" style="text-align: center;">操作</th>
		</tr>
	</thead>
	<tbody>
	<c:choose>
	 	<c:when test="${not empty listTaskUpfile}">
	 		<c:forEach items="${listTaskUpfile}" var="taskUpfileVo" varStatus="status">
	 			<tr>
	 				<td class="fileOrder">${ status.count}</td>
	 				<td align="left" title="${ taskUpfileVo.filename}">${ taskUpfileVo.filename}</td>
	 				<td style="font-size: 13px" title="${taskUpfileVo.taskName}">
	 					<c:set var="fileSource">
	 						<c:choose>
								<c:when test="${'task' eq taskUpfileVo.type }">
 									[任务]
								</c:when>
								<c:otherwise>
									[留言]
								</c:otherwise>
							</c:choose>
	 					</c:set>
						${fileSource}
	 					<c:choose>
	 						<c:when test="${taskUpfileVo.taskId==taskUpfile.taskId}">
	 							<font color="#757575">
	 								${taskUpfileVo.taskName}
	 							</font>
	 						</c:when>
	 						<c:otherwise>
	 							<a href="javascript:void(0);" onclick="viewTask(${taskUpfileVo.taskId});" style="color:blue;">
	 								${taskUpfileVo.taskName}
	 							</a>
	 						</c:otherwise>
	 					</c:choose>
	 				</td>
	 				<td>${fn:substring(taskUpfileVo.recordCreateTime,0,10)}</td>
	 				<td style="text-align: left;">
	 					<div class="ws-position" data-container="body" data-toggle="popover" data-placement="left"  
	 					data-user='${taskUpfileVo.userId}' data-busId='${taskUpfileVo.taskId}' data-busType='003'>
						
						<img src="/downLoad/userImg/${taskUpfileVo.comId}/${taskUpfileVo.userId}?sid=${param.sid}"
									title="${taskUpfileVo.creatorName}" class="user-avatar"/>
						<i class="ws-smallName">${taskUpfileVo.creatorName}</i>
						</div>
	 				</td>
	 				<td align="center">
		 				<c:choose>
		 					<c:when test="${taskUpfileVo.fileExt=='doc' || taskUpfileVo.fileExt=='docx' || taskUpfileVo.fileExt=='xls' || taskUpfileVo.fileExt=='xlsx' || taskUpfileVo.fileExt=='ppt' || taskUpfileVo.fileExt=='pptx'}">
				 				<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${taskUpfileVo.uuid}','${taskUpfileVo.filename}','${param.sid }')"></a>
				 				<a style="margin-left:5px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${taskUpfileVo.upfileId}','${taskUpfileVo.uuid}','${taskUpfileVo.filename}','${taskUpfileVo.fileExt}','${param.sid}','003','${taskUpfile.taskId}')"></a>
		 					</c:when>
		 					<c:when test="${taskUpfileVo.fileExt=='txt'  || taskUpfileVo.fileExt=='pdf'}">
				 				<a class="fa fa-download" href="/downLoad/down/${taskUpfileVo.uuid}/${taskUpfileVo.filename}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:5px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${taskUpfileVo.upfileId}','${taskUpfileVo.uuid}','${taskUpfileVo.filename}','${taskUpfileVo.fileExt}','${param.sid}','003','${taskUpfile.taskId}')"></a>
		 					</c:when>
		 					<c:when test="${taskUpfileVo.fileExt=='jpg'||taskUpfileVo.fileExt=='bmp'||taskUpfileVo.fileExt=='gif'||taskUpfileVo.fileExt=='jpeg'||taskUpfileVo.fileExt=='png'}">
				 				<a class="fa fa-download" href="/downLoad/down/${taskUpfileVo.uuid}/${taskUpfileVo.filename}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:5px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${taskUpfileVo.uuid}/${taskUpfileVo.filename}','${param.sid}','${taskUpfileVo.upfileId}','003','${taskUpfile.taskId}')"></a>
		 					</c:when>
		 					<c:otherwise>
				 				<a class="fa fa-download" onclick="downLoad('${taskUpfileVo.uuid}','${taskUpfileVo.filename}','${param.sid }')" title="下载"></a>
		 					</c:otherwise>
		 				</c:choose>
		 				<c:if test="${empty delete && taskUpfileVo.taskId==taskUpfile.taskId && taskUpfileVo.userId==userInfo.id}">
				 			<a style="margin-left:5px;" class="fa  fa-times-circle fa-lg" href="javascript:void(0);" title="删除" onclick="delTaskFile(this,${taskUpfileVo.taskId},${taskUpfileVo.id},'${taskUpfileVo.type}')"></a>
		 				</c:if>
		 			</td>
	 			</tr>
	 		</c:forEach>
	 	</c:when>
	 	<c:otherwise>
	 		<tr>
	 			<td height="25" colspan="9" align="center"><h3>没有相关附件！</h3></td>
	 		</tr>
	 	</c:otherwise>
	 </c:choose>
	</tbody>
</table>
<tags:pageBar url="/task/taskUpfilePage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
