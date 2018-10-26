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
</head>
<body scroll="no">
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr bgcolor="#FFFFFF">
			<td height="27" align="left">
				<table width="90%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td align="left">
						<img src="/static/images/ico_p.gif" width="20" height="14" align="left"> 
						 <input style="height: 18px;" name="queryName" id="queryName" onkeydown="enter();"/>&nbsp;<img style="cursor: pointer;" onclick="enter();" src="/static/images/cx.png"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td style="padding-right: 10px;">
				<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr bgcolor="#FFFFFF">
						<td style="padding-left: 1px;height: 400px"></td>
						<td width="40%"><iframe id="roletree" name="roletree" style="width: 100%;height:400px; border: 1px solid #86B1E8;" frameborder="0" src="/common/roleMoreSelect/roleTreePage?sid=${param.sid }"></iframe></td>
						<td style="padding-left: 1px;"></td>
						<td style="width: 100%">
							<div style="border: 1px solid #86B1E8; width: 100%;">
								<form name="form1" style="margin: 0 auto; padding: 0px;">
									<table width="100%">
										<tr>
											<td bgcolor="#FFFFFF" valign="top">
												<div style="border: 1px solid #ffffff; width: 100%; text-align: center; padding-bottom: 0px">
													<select name="roleselect" id="roleselect" ondblclick="removeOne(this)" style="width: 100%;height:400px;  margin: -2 -10 -10 -10;" multiple="multiple">
													</select>
												</div>
											</td>
										</tr>
									</table>
								</form>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
<script type="text/javascript">
//页面加载后初始化 如果父页面文本框中已经有值，则添加到下拉框中
var o = art.dialog.data('o');
$(document).ready(function(){
  for(var i=0;i<o.length;i++){
    $('#roleselect').append("<option value='"+o[i].value+"'>"+o[i].text+"</option>");
  }
});

//选择后追加到多行下拉框
function appendRole(checked,str){
	  var json = eval('(' + str + ')'); 
	  if(checked==true){
	   if($("#roleselect option[value='"+json.roleid+"']").length==0){
	     $('#roleselect').append("<option value='"+json.roleid+"'>"+json.rolename+"</option>");
	   }
	  }else{
	   var selectobj = document.getElementById("roleselect");
	   for (var i = 0; i < selectobj.options.length; i++) {
			  if(selectobj.options[i].value==json.roleid){
			    selectobj.options[i] = null;
			    break;
			  }
	   }
	  }
	}

//点击确定后处理
function returnRole() {
	var selectobj = document.getElementById("roleselect");
	var options = selectobj.options;
	art.dialog.data('options', options);
}


//回车后模糊检索树
 function enter(){
	  if(event.keyCode==13){
		 var q = $('#queryName').val();
		 roletree.enter(q);
	  }
	  $('#queryName').select();
  }
 
 //清空所有
 function removeAll(){
	  removeOptions('roleselect');
	  roletree.uncheckAll();
	}
 //清楚指定
 function removeOne(ts){
	  var depid="";
	  $('#roleselect').find("option:selected").each(function(index){
	    roletree.uncheck($(this).attr("value"));
	  });
	  removeClick(ts.id);
	}
</script>
</html>
