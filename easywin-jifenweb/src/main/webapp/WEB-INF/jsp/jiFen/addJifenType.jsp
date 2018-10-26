<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
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
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			callback:function(form){
				var  subFlag = $("#subFlag").val();
				var dayMaxScore = $("#dayMaxScore").val();
				var sysMaxScore = $("#sysMaxScore").val();
				var typeScore = $("#typeScore").val();
				
				if(typeScore==0 && (dayMaxScore!=0 || sysMaxScore !=0)){
					if(dayMaxScore!=0){
						art.dialog({"content":"类别分值为0,每日封顶分数不能非0!"}).time(2);
						$("#dayMaxScore").focus();
						return false;
					}else if( sysMaxScore !=0){
						art.dialog({"content":"类别分值为0,封顶分数不能非0!"}).time(2);
						$("#sysMaxScore").focus();
						return false;
					}
				}
					
				if(dayMaxScore*sysMaxScore!=0){
					art.dialog({"content":"每日封顶不能与分等分数同时非0"}).time(2);
					return false;
				}
				
				//防止重复提交
				if("0"==subFlag){
					addJifenType();
				}else{
					return false;
				}
			},
			showAllError : true
		});
		$(document).ready(function(){
			$("#jifenCode").focus();
		});
	})
	function formSub(){
		$(".subform").submit();
	}
	//添加积分类别
	function addJifenType(){
		var win = art.dialog.open.origin;
		var configNums = parseInt($("#configNums", win.document).val())+1;
		$(".subform").ajaxSubmit({
	        type:"post",
	        url:"/jiFen/ajaxAddJifenType?sid=${param.sid}&t="+Math.random(),
	        dataType: "json",
	        beforeSubmit:function (a,f,o){
		        //防止重复提交
	        	$("#subFlag").attr("value","1");
			},
	        success:function(msgObjs){
				$(".subform").resetForm();
	        	$("#subFlag").attr("value","0");
	        	$("#orderNo").attr("value",msgObjs.orderMax);

	        	
		        var obj = msgObjs.jifenConfig;
		        var html = '';
		        html +='\n <tr id="tr_'+obj.id+'"> ';
		        html +='\n	<td height="25">'+configNums+'</td>';
 				html +='\n	<td><input type="text" style="width: 100%" preValue="'+obj.jifenCode+'" value="'+obj.jifenCode+'" onchange="jifenCodeUpdate(this,\''+obj.id+'\');" onkeydown="if(event.keyCode==13)jifenCodeUpdate(this,\''+obj.id+'\')"/></td>';
 				html +='\n	<td><input type="text" preValue="'+obj.title+'" value="'+obj.title+'" onchange="jifenTitleUpdate(this,\''+obj.id+'\');" onkeydown="if(event.keyCode==13)jifenTitleUpdate(this,\''+obj.id+'\')"/></td>';
 				html +='\n	<td><input type="text" style="width: 90%;text-align: center;" preValue="'+obj.typeScore+'" value="'+obj.typeScore+'" onchange="jifenScoreUpdate(this,\''+obj.id+'\');" onkeydown="if(event.keyCode==13)jifenScoreUpdate(this,\''+obj.id+'\')"/></td>';
 				html +='\n	<td><input type="text" style="width: 90%;text-align: center;" preValue="'+obj.dayMaxScore+'" value="'+obj.dayMaxScore+'" onchange="jifenDayScoreUpdate(this,\''+obj.id+'\');" onkeydown="if(event.keyCode==13)jifenDayScoreUpdate(this,\''+obj.id+'\')"/></td>';
 				html +='\n	<td><input type="text" style="width: 90%;text-align: center;" preValue="'+obj.sysMaxScore+'" value="'+obj.sysMaxScore+'" onchange="jifenSysScoreUpdate(this,\''+obj.id+'\');" onkeydown="if(event.keyCode==13)jifenSysScoreUpdate(this,\''+obj.id+'\')"/></td>';
 				html +='\n	<td><input type="text" preValue="'+obj.content+'" value="'+obj.content+'" onchange="jifenContentUpdate(this,\''+obj.id+'\');" onkeydown="if(event.keyCode==13)jifenContentUpdate(this,\''+obj.id+'\')"/></td>';
 				html +='\n	<td style="width: 30px;"><input type="text" style="text-align: center" size="3" preValue="'+obj.orderNo+'" value="'+obj.orderNo+'" onchange="jifenOrderNoUpdate(this,\''+obj.id+'\');" onkeydown="if(event.keyCode==13)jifenOrderNoUpdate(this,\''+obj.id+'\')"/></td>';
 				html +='\n	<td style="width: 30px;"><img src="/static/images/cancel.png" onclick="jifenConfigDel(\''+obj.id+'\',\''+obj.title+'\');"/></td>';
 				html +='\n</tr>';
		        
				$("#jifenList", win.document).append(html);
				$("#configNums", win.document).attr("value",configNums);

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
					<td class="td1">类别代码：<font style="color: red">*</font>
					</td>
					<td class="td2" colspan="3">
						<input type="text" id="jifenCode" datatype="n" name="jifenCode" value="" ajaxurl="/jiFen/validateJifenCode?sid=${param.sid}&id=0" />
					</td>
				</tr>
				<tr style="height: 60px">
					<td class="td1">类别名称：<font style="color: red">*</font>
					</td>
					<td class="td2" colspan="3"><input type="text"
						style="width: 90%" datatype="*" name="title" value="" /></td>
				</tr>
				<tr style="height: 60px">
					<td class="td1">类别分值：<font style="color: red">*</font>
					</td>
					<td class="td2" colspan="3">
						<input type="text" id="typeScore" style="width: 72%" datatype="zs" name="typeScore" value="0" />
						<font color="gray">
							(0为自定义)
						</font>
					</td>
				</tr>
				<tr style="height: 60px">
					<td class="td1">每日顶值：<font style="color: red">*</font>
					</td>
					<td class="td2" colspan="3"><input type="text" id="dayMaxScore"
						style="width: 90%" datatype="pn" name="dayMaxScore" value="0" /></td>
				</tr>
				<tr style="height: 60px">
					<td class="td1">顶值：<font style="color: red">*</font>
					</td>
					<td class="td2" colspan="3"><input type="text" id="sysMaxScore"
						style="width: 90%" datatype="pn" name="sysMaxScore" value="0" /></td>
				</tr>
				<tr style="height: 60px">
					<td class="td1">类别描述：<font style="color: red">*</font>
					</td>
					<td class="td2" colspan="3"><input type="text"
						style="width: 90%" datatype="*" name="content" value="" /></td>
				</tr>
				<tr style="height: 60px">
					<td class="td1">排序：<font style="color: red">*</font></td>
					<td class="td2" colspan="3"><input id="orderNo" datatype="zs" type="text" name="orderNo" value="${orderMax}" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
