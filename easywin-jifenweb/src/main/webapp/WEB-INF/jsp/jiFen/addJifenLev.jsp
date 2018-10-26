<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
<%@page import="com.westar.core.web.InitServlet"%>
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
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			callback:function(form){
				var  subFlag = $("#subFlag").val();
				if("0"==subFlag){
					addJifenLev();
				}
				return false;
			},
			showAllError : true
		});
		$(document).ready(function(){
			$("#levName").focus();
		});
	})
	function formSub(){
		$(".subform").submit();
	}
	//添加积分等级
	function addJifenLev(){
		var win = art.dialog.open.origin;
		var configLevNums = parseInt($("#configLevNums", win.document).val())+1;
		$(".subform").ajaxSubmit({
	        type:"post",
	        url:"/jiFen/ajaxAddJifenLev?sid=${param.sid}&t="+Math.random(),
	        dataType: "json",
	        beforeSubmit:function (a,f,o){
		        //防止重复提交
	        	$("#subFlag").attr("value","1");
			},
	        success:function(msgObjs){
				$(".subform").resetForm();
	        	$("#subFlag").attr("value","0");

	        	
		        var obj = msgObjs.jifenLev;
		        var html = '';
		        html +='\n	<tr id="tr_'+obj.id+'">';
				html +='\n	<td height="25">'+configLevNums+'</td>';
 				html +='\n	<td><input type="text" preValue="'+obj.levName+'" value="'+obj.levName+'" onchange="jifenlevNameUpdate(this,\''+obj.id+'\');" onkeydown="if(event.keyCode==13)jifenlevNameUpdate(this,\''+obj.id+'\')"/></td>';
 				html +='\n	<td><input type="text" preValue="'+obj.levMinScore+'" value="'+obj.levMinScore+'" onchange="jifenScoreUpdate(this,\''+obj.id+'\');" onkeydown="if(event.keyCode==13)jifenScoreUpdate(this,\''+obj.id+'\')"/></td>';
 				html +='\n	<td style="width: 30px;"><img src="/static/images/cancel.png" onclick="jifenLevDel(\''+obj.id+'\',\''+obj.levName+'\');"/></td>';
 				html +='\n </tr>';

 				$("#jifenLevList", win.document).append(html);
				$("#configLevNums", win.document).attr("value",configLevNums);

				var offset = $("#bottomDiv",win.document).offset();
				var offsettop = offset.top;
				var obj = win.parent.document.getElementById("iframe_body_right");  //取得父页面IFrame对象
				obj.height = offsettop;  //调整父页面中IFrame的高度为此页面的高度  
				
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	$("#subFlag").attr("value","0");
	        }
	    });
	}
</script>
</head>
<body>
	<div class="block01" style="padding-top: 10px">
		<form class="subform" method="post" style="margin-top:5px;">
		<input type="hidden" id="subFlag" value="0"/>
			<table width="99%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
				<tr style="height: 60px">
					<td class="td1">等级名称：<font style="color: red">*</font>
					</td>
					<td class="td2" colspan="3">
						<input type="text" style="width: 70%" id="levName" datatype="*"  name="levName" value="" ajaxurl="/jiFen/validateJifenLevName?sid=${param.sid}&id=0" />
					</td>
				</tr>
				<tr style="height: 60px">
					<td class="td1">所需分值：<font style="color: red">*</font>
					</td>
					<td class="td2" colspan="3">
						<input type="text" style="width: 70%" datatype="pn" name="levMinScore" value="" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
