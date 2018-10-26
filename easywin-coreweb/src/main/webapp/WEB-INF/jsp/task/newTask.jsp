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
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
	})
	
</script>
</head>
<body>
<div class="block01" style="padding-top: 10px">
	<form class="subform" method="post" action="/task/addTask" style="margin-top:5px;">
				<tags:token></tags:token>
				<table width="99%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
					<tr><td colspan="4" align="left"><h2>创建新任务</h2></td></tr>
					<tr>
						<td class="td1">任务名称：<font style="color: red">*</font></td>
						<td class="td2" colspan="3">
								<input type="text" id="taskName" style="width: 90%" datatype="*" name="taskName" value=""/>
						</td>
					</tr>
					<tr>
						<td class="td1">发起人：<font style="color: red">*</font></td>
						<td class="td2">
								<tags:userOne datatype="s" name="owner" showValue="" value="" showName="onwerName"></tags:userOne>
								<input type="hidden" id="owner" datatype="*" name="owner"/>
						</td>
						<td class="td1">办理时限：</td>
						<td class="td2">
								<input type="text" readonly="readonly" id="dealTimeLimit" name="dealTimeLimit" onClick="WdatePicker(,minDate:'%y-%M-{%d}')"/>
						</td>
					</tr>
					<tr>
						<td class="td1">任务成员：</td>
						<td class="td2" colspan="3">
								<input type="text" style="width: 50%" name="taskSharer" value=""/>
						</td>
					</tr>
					<tr>
						<td class="td1">关联项目：</td>
						<td class="td2" colspan="3">
						<input style="width: 50%" id="itemName" type="text" name="itemName" value=""/>
						</td>
					</tr>
					<tr>
						<td class="td1">上传附件：</td>
						<td class="td2" colspan="3">
							<a href="javascript:void(0);">+添加附件</a>
						</td>
					</tr>
					<tr>
						<td class="td2" colspan="4">任务详情</td>
					</tr>
					<tr>
						<td class="td2" colspan="4" align="center">
							<textarea id="taskReamark" name="taskReamark" rows="4" cols="4" style="width:90%;height:120px;"></textarea>
						</td>
					</tr>
					<tr>
						<td class="td2" colspan="2">
							<input type="checkbox"/>分享到动态更新
						</td>
						<td colspan="2" align="right">
							<input type="submit" value="创建任务" class="green_btn"/>
						</td>
					</tr>
				</table>
			</form>	
</div>
</body>
</html>
