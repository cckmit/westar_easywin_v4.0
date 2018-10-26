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
						<a href="javascript:selectAll();">所有</a> 
							&nbsp;&nbsp;<a href="javascript:selectGroup();">分组</a> 
							&nbsp;&nbsp;<a href="javascript:myRootOrg();">本部门</a> 
							&nbsp;&nbsp;
						 <input style="height: 18px;" name="queryName" id="queryName" onblur="blurName(this);" onfocus="focusName(this);" tip="请输入机构名称" value="请输入机构名称" class="gray" onkeydown="enter();"/>&nbsp;<img style="cursor: pointer;" onclick="enter();" src="/static/images/cx.png"/>
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
						<td width="40%"><iframe id="deptree" name="deptree" style="width: 100%;height:400px; border: 1px solid #86B1E8;" frameborder="0" src="/common/orgMoreSelect/orgTreePage?sid=${param.sid }"></iframe></td>
						<td style="padding-left: 1px;"></td>
						<td style="width: 100%">
							<div style="border: 1px solid #86B1E8; width: 100%;">
								<form name="form1" style="margin: 0 auto; padding: 0px;">
									<table width="100%">
										<tr>
											<td bgcolor="#FFFFFF" valign="top">
												<div style="border: 1px solid #ffffff; width: 100%; text-align: center; padding-bottom: 0px">
													<select name="depselect" id="depselect" ondblclick="removeOne(this)" style="width: 100%;height:400px;  margin: -2 -10 -10 -10;" multiple="multiple">
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
    $('#depselect').append("<option value='"+o[i].value+"'>"+o[i].text+"</option>");
  }
});

function appendDep(checked,str){
	  var json = eval('(' + str + ')'); 
	  if(checked==true){
	   if($("#depselect option[value='"+json.depid+"']").length==0){
	     $('#depselect').append("<option value='"+json.depid+"'>"+json.pathName+"</option>");
	   }
	  }else{
	   var selectobj = document.getElementById("depselect");
	   for (var i = 0; i < selectobj.options.length; i++) {
			  if(selectobj.options[i].value==json.depid){
			    selectobj.options[i] = null;
			    break;
			  }
	   }
	  }
	}
function returnOrg() {
	var selectobj = document.getElementById("depselect");
	var options = selectobj.options;
	art.dialog.data('options', options);
}



 function enter(){
	  if(event.keyCode==13){
		 var q = $('#queryName').val();
		 deptree.enter(q);
		 $('#queryName').select();
	  }
  }
 
 function removeAll(){
	  removeOptions('depselect');
	  deptree.uncheckAll();
	}
 
 function removeOne(ts){
	  var depid="";
	  $('#depselect').find("option:selected").each(function(index){
	    deptree.uncheck($(this).attr("value"));
	  });
	  removeClick(ts.id);
	}
 
//所有机构
 function selectAll(){
	 deptree.initZtree();
 }
 //本部门
 function myRootOrg(){
	  deptree.myRootOrg();
 }
 //按分组显示机构
 function showGroup(groupid,groupName){
	 deptree.showGroup(groupid,groupName);
 }
 //选择分组
 function selectGroup(){
		var url = "/organization/listOrgGroupCheck?sid=${param.sid}";
		art.dialog.open(url, {
		title : '分组选择',
		lock : true,
		max : false,
		min : false,
		width : 300,
		height : 380,
		close : function() {
			var groupId = art.dialog.data('groupId');
			var groupName = art.dialog.data('groupName');
			showGroup(groupId,groupName);
		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			iframe.ok();
			return false;
		},
		cancel : true
		});
	}
</script>
</html>
