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


	//查看详细
	function view(id){
		var url = "/message/listPagedMsgByMainId?mainId="+id+"&sid=${param.sid}";
		art.dialog.open(url,{
			title:'消息详细',
			lock: true,
			max: false,
			min: false,
			width: 1000,
		    height: 440,
		    cancel: true
		});
	}
	
</script>

</head>
<body>
<div class="tit"><span>当前位置：系统消息</span></div>
	<div class="tab_content" class="tab_content" style="margin-top: 5px;margin-left: auto;margin-right: auto;width: 99%;">
		<div class="swap_top">
			<div class="swap_tit">
				<a href="/message/listPagedMessage?sid=${param.sid }">接收消息</a><a class="cur" href="/message/listPagedMeesageMain?sid=${param.sid }">已发送</a>
			</div>
			<div class="swap_tab">
				<div class="block01">
					<div class="oper_btn">
						<span> <a href="javascript:void(0)" onclick="add()" class="add">发送</a> 
						</span>
					</div>
				</div>
				<div class="block01">
					<form action="/message/delmsgInfo" method="post" id="delForm">
						<tags:token></tags:token>
						<input type="hidden" name="redirectPage" />
						<table style="table-layout: fixed;" width="100%" border="0" class="grid_tab" cellpadding="0" cellspacing="0">
							<thead>
								<tr>
									<th style="width:50px;">序号</th>
									<th style="width: 30%;">内容摘要</th>
									<th style="width: 120px;">发送时间</th>
									<th>数量</th>
									<th>已读</th>
									<th>未读</th>
									<th style="width: 25%">接收人</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty list}">
										<c:forEach items="${list}" var="msg" varStatus="st">
											<tr>
												<td height="25">${st.count}</td>
												<td style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;padding-left: 5px;padding-right: 5px;" align="left"><a href="javascript:void(0)" onclick="view(${msg.id})">${msg.title }</a></td>
												<td>${msg.sendtime }</td>
												<td>${msg.allNum }</td>
												<td>${msg.yesNum }</td>
												<td style="color: red;">${msg.noNum }</td>
												<td style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;" title="${msg.receiverNames }">${msg.receiverNames }</td>
												<td><span><a href="javascript:void(0)" onclick="view(${msg.id})">查看</a></span></td>
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
						<tags:pageBar url="/message/listPagedMsgByMainId"></tags:pageBar>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
