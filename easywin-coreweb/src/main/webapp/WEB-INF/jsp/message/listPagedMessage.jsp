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
<title><%=SystemStrConstant.TITLE_NAME%></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	function add(){
		window.location.href='/message/addMessagePage?sid=${param.sid}&redirectPage='+encodeURIComponent(window.location.href);
	}

	function answer(answerMsgId,userid,username){
		window.location.href='/message/addMessagePage?answerMsgId='+answerMsgId+'&receiver='+userid+'&receiverName='+username+'&sid=${param.sid}&redirectPage='+encodeURIComponent(window.location.href);
	}
	
	//批量删除
	function del(){
		if(checkCkBoxStatus('ids')){
			art.dialog.confirm('确定要删除该消息吗？', function(){
				$("#delForm input[name='redirectPage']").val(window.location.href);
				$('#delForm').submit();
			}, function(){
				art.dialog.tips('已取消');
			});	
		}else{
			art.dialog.alert('请先选择一条记录。');
		}
	}

	//查看详细
	function view(id){
		viewMsg(id,'${param.sid}',window.location.href);
		setTimeout(function(){parent.setMsgNum()},900);
	}
	
	
	
	//标记已读
	function doRead(id){
		if(checkCkBoxStatus('ids')){
			$("#delForm input[name='redirectPage']").val(window.location.href);
			$("#delForm").attr("action","/message/doRead");
			$('#delForm').submit();
		}else{
			art.dialog.alert('请先选择一条记录。');
		}
	}
	
	$(document).ready(function(){
		parent.setMsgNum();
	});
</script>

</head>
<body>
<div class="tit"><span>当前位置：系统消息</span></div>
	<div class="tab_content" style="margin-top: 5px;margin-left: auto;margin-right: auto;width: 99%;">
		<div class="swap_top">
			<div class="swap_tit">
				<a href="/message/listPagedMessage?sid=${param.sid }" class="cur">接收消息</a><a href="/message/listPagedMeesageMain?sid=${param.sid }">已发送</a>
			</div>
			<div class="swap_tab">
				<div class="block01">
					<div class="oper_btn">
						<span> <a href="javascript:void(0)" onclick="add()" class="add">发送</a> <a style="display: none;" href="javascript:void(0)" onclick="del()" class="del">删除</a> <a href="javascript:void(0)" onclick="doRead()" class="del">已读</a>
						</span>
					</div>
				</div>
				<div class="block01">
					<form action="/message/delmsgInfo" method="post" id="delForm">
						<tags:token></tags:token>
						<input type="hidden" name="redirectPage" />
						<table width="100%" border="0" class="grid_tab" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
							<thead>
								<tr>
									<th style="width: 50px;"><input type="checkbox" onclick="checkAll(this,'ids')" /></th>
									<th style="width: 50px;">序号</th>
									<th style="width: 50%;">内容摘要</th>
									<th>发送人</th>
									<th style="width: 120px;">发送时间</th>
									<th>是否已读</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty list}">
										<c:forEach items="${list}" var="msg" varStatus="st">
											<tr>
												<td height="25"><input type="checkbox" name="ids" value="${msg.id}" /></td>
												<td>${st.count}</td>
												<td style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;padding-left: 5px;padding-right: 5px;" align="left">${msg.title }</td>
												<td>${msg.senderName }</td>
												<td>${msg.sendtime }</td>
												<td><font ${msg.isread==0?'color="red"':'' }${msg.isread!=0?'color="black"':'' }>${msg.isreadName }</font></td>
												<td><span><a href="javascript:void(0)" onclick="view(${msg.id})">查看</a></span>
												&nbsp;&nbsp;
												<span><a href="javascript:void(0)" style="color: blue;" onclick="answer(${msg.id },${msg.sender},'${msg.receiverName }')">回复</a></span>
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td height="25" colspan="7" align="center"><h3>没有相关信息！</h3></td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</form>
					<div class="page">
						<tags:pageBar url="/message/listPagedMessage"></tags:pageBar>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
