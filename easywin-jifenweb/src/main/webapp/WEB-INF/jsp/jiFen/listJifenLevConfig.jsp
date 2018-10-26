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
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script type="text/javascript">
//积分项配置页面
function jifenConfig(){
	window.location.href='/jiFen/listJifenConfigPage?sid=${param.sid}';
}
//添加积分等级页面
$(function(){
	$("#addJiFenLev").click(function(){
		art.dialog.open("/jiFen/addJifenLevPage?sid=${param.sid}", {
			title : '添加积分等级',
			lock : true,
			max : false,
			min : false,
			width : 400,
			height : 150,
			ok: function () {
				var iframe = this.iframe.contentWindow;
				if (!iframe.document.body) {
					return false;
				}
				;
				iframe.formSub();
				return false;
		    },
		    cancelVal: '取消',
		    cancel: true
		});
	});
});
/**
 * 等级最小值修改
 */
function jifenlevNameUpdate(ts,id){
	$(".subform").ajaxSubmit({
        type:"post",
        url:"/jiFen/ajaxUpdateJifenLev?sid=${param.sid}&t="+Math.random(),
        dataType: "json",
        beforeSubmit:function (a,f,o){
        	var preValue = $(ts).attr("preValue");
        	var nowValue = $(ts).val();
        	if(preValue==nowValue){
        		return false;
        	}
        	var reg = new RegExp("[\\w\\W]+");   
		    var objVal = $(ts).val();  
		    if(!reg.test(objVal)){  
		        art.dialog({"content":"不能为空！"}).time(2);  
                return false;
		    }  
        },
        data:{"id":id,"levName":$(ts).val()},
        success:function(msgObjs){
            if(msgObjs.status=='y'){
                $(ts).attr("preValue",$(ts).val());
                showNotification(0,"操作成功！");
            }else{
                $(ts).attr("value",$(ts).attr("preValue"));
                showNotification(1,msgObjs.info);
            }
        },
        error:function(XmlHttpRequest,textStatus,errorThrown){
	        art.dialog({"content":"操作失败！"}).time(1);
        }
    });
}
/**
 * 等级最小值修改
 */
function jifenScoreUpdate(ts,id){
	$(".subform").ajaxSubmit({
        type:"post",
        url:"/jiFen/ajaxUpdateJifenLev?sid=${param.sid}&t="+Math.random(),
        dataType: "json",
        beforeSubmit:function (a,f,o){
        	var preValue = $(ts).attr("preValue");
        	var nowValue = $(ts).val();
        	if(preValue==nowValue){
        		return false;
        	}
			var reg = new RegExp("^(0|[1-9][0-9]*)$");  
		    var objVal = $(ts).val();  
		    if(!reg.test(objVal)){  
		        art.dialog({"content":"请填写正整数"}).time(2);  
                return false;
		    }  
        },
        data:{"id":id,"levMinScore":$(ts).val()},
        success:function(msgObjs){
            if(msgObjs.status=='y'){
                $(ts).attr("preValue",$(ts).val());
                showNotification(0,"操作成功！");
            }else{
                $(ts).attr("value",$(ts).attr("preValue"));
                showNotification(1,msgObjs.info);
            }
        },
        error:function(XmlHttpRequest,textStatus,errorThrown){
	        art.dialog({"content":"操作失败！"}).time(1);
        }
    });
}

//积分类型删除
function jifenLevDel(id,title){
	art.dialog.confirm("确定删除积分类型\""+title+"\"？", function(){
		$(".subform").ajaxSubmit({
	        type:"post",
	        url:"/jiFen/jifenLevDel?sid=${param.sid}&t="+Math.random(),
	        dataType: "json",
	        beforeSubmit:function (a,f,o){
			    if(strIsNull(id)){  
	                return false;
			    }  
	        },
	        data:{"id":id},
	        success:function(msgObjs){
	            if(msgObjs.status=='y'){
	            	$("#tr_"+id).remove();
					showNotification(0,"删除成功！");
	            }else{
	                showNotification(1,msgObjs.info);
	            }
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
		        art.dialog({"content":"操作失败！"}).time(1);
	        }
	    });
	    
	}, function(){
		art.dialog.tips("已取消");
	});	
}

</script>
</head>
<body style="width: 95%;padding-left: 20px" onload="scrollPage()">
<div class="tit">
	<div style="float:left;" id="navmenu">
		<ul  style="float:left;">
			<li style="float:left;height:30px;line-height:30px;position:relative;width:100px;text-align:center;" >
				<a onclick="jifenConfig()" href="javascript:void(0);">积分类别配置</a>
			</li>
			<li style="float:left;height:30px;line-height:30px;position:relative;width:100px;text-align:center;padding-left: 5px;background-color: #f0f000" >
				<a onclick="javascript:void(0)" href="javascript:void(0);">积分等级配置</a>
			</li>
		</ul>
	</div>
</div>
<div class="block01">
<form class="subform" method="post" style="margin-top:5px;">
			<input type="hidden" id="configLevNums" value="${fn:length(list)}"/>
			<table width="99%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
				<tr>
					<td align="left"><h4>积分等级配置</h4></td>
					<td width="15%"><a href="#" id="addJiFenLev"><font color="blue">添加</font></a></td>
				</tr>
			</table>
			<table width="99%" align="center" border="0" cellspacing="0" id="jifenLevList" cellpadding="0" class="tab1">
			     <tr>
			     <th style="width: 30px;" height="30">序号</th>
			     <th align="center">等级名称</th>
			     <th align="center">所需分值</th>
			     <th style="width: 30px;">删除</th>
			     </tr>
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach items="${list}" var="obj" varStatus="status">
						<tr id="tr_${obj.id}">
			 				<td height="25">${status.count}</td>
			 				<td><input type="text" preValue="${obj.levName}" value="${obj.levName}" onchange="jifenlevNameUpdate(this,'${obj.id}');" onkeydown="if(event.keyCode==13)jifenlevNameUpdate(this,'${obj.id}')"/></td>
			 				<td><input type="text" preValue="${obj.levMinScore}" value="${obj.levMinScore}" onchange="jifenScoreUpdate(this,'${obj.id}');" onkeydown="if(event.keyCode==13)jifenScoreUpdate(this,'${obj.id}')"/></td>
			 				<td style="width: 30px;"><img src="/static/images/cancel.png" onclick="jifenLevDel('${obj.id}','${obj.levName}');"/></td>
			 			</tr>
						</c:forEach>
					</c:when>
				</c:choose>
			</table>
		</form>

<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</div>
</body>
</html>