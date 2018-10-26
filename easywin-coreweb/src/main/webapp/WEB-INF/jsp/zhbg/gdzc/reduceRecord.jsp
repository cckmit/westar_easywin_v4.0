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
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
</script>
<script> 
	$(function() { 
		$('.grid_tab tr').addClass('odd'); 
		$('.grid_tab tr:even').addClass('even');
	}); 
	//添加固定资产维修
	function updateGdzcReduce(sid,id){
		window.top.layer.open({
			type: 2,
			title: false,
			closeBtn:0,
			area: ['500px','400px'],
			fix: true, //不固定
			maxmin: false,
			scrollbar:false,
			move: false,
			zIndex:1010,
			shade:0.3,
			scrollbar:false,
			content: ["/gdzc/updateReduceRecordPage?sid="+sid+"&id="+id,'no'],
			btn: ['确认','关闭'],
			yes: function(index, layero){
				var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				iframeWin.formSub();
			},end:function(){
				window.self.location.reload();
			}
		});
	}
	//删除记录
	function delGdzcReduce(id,sid,ts){
		var url = "/gdzc/delReduceRecord?sid="+sid;
		window.top.layer.confirm('确定要删除该记录？', {icon: 3, title:'提示'}, function(index){
			$.post(url,{Action:"post",id:id},function (data){
				showNotification(1,"删除成功！");
				$(ts).parents("tr").remove();
				
				$.each($(".orderNum"),function(index,item){
					$(this).html(index+1);
				})
			});
		})
	}
</script>
</head>
<body onload="resizeVoteH('otherGdzcAttrIframe');" style="background-color:#FFFFFF;">
<table class="table table-hover general-table">
	<thead>
		<tr>
			<th width="8%" valign="middle">序号</th>
			<th width="30%"valign="middle" class="hidden-phone">减少原因</th>
			<th width="12%" valign="middle">减少类型</th>
			<th width="12%" valign="middle">减少人</th>
			<c:if test="${isModAdmin }">
				<th width="12%" valign="middle" style="text-align: center;">操作</th>
			</c:if>
		</tr>
	</thead>
	<tbody>
	<c:choose>
	 	<c:when test="${not empty listReduce}">
	 		<c:forEach items="${listReduce}" var="obj" varStatus="status">
	 			<tr>
	 				<td class="orderNum">${ status.count}</td>
	 				<td align="left" style="font-size: 13px">
	 				<tags:cutString num="20">${ obj.reduceReason}</tags:cutString></td>
	 				<td>${obj.reduceTypeName}</td>
	 				<td style="text-align: left;">
	 					<div class="ws-position" data-container="body" data-toggle="popover" data-placement="left">
						<img src="/downLoad/userImg/${obj.comId}/${obj.executor}?sid=${param.sid}"
							title="${obj.executorName}" class="user-avatar"/>
						<i class="ws-smallName">${obj.executorName}</i>
						</div>
	 				</td>
	 				<c:if test="${isModAdmin }">
	 				<td align="center">
	 				<a href="javascript:void(0)" onclick="updateGdzcReduce('${param.sid}','${obj.id }')">修改</a>
	 				|<a href="javascript:void(0)" onclick="delGdzcReduce('${obj.id }','${param.sid}',this)">删除</a>
		 			</td>
		 			</c:if>
	 			</tr>
	 		</c:forEach>
	 	</c:when>
	 	<c:otherwise>
	 		<tr>
	 			<td height="25" colspan="9" align="center"><h3>没有相关减少记录！</h3></td>
	 		</tr>
	 	</c:otherwise>
	 </c:choose>
	</tbody>
</table>
<tags:pageBar url="/gdzc/gdzcReduceRecordPage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
