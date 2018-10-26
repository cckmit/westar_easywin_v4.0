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
//积分等级配置页面
function jifenLevConfig(){
	window.location.href='/jiFen/listJifenLevConfigPage?sid=${param.sid}';
}
//添加积分配置项
$(function(){
	$("#addJiFenType").click(function(){
		art.dialog.open("/jiFen/addJifenTypePage?sid=${param.sid}", {
			title : '添加积分类别',
			lock : true,
			max : false,
			min : false,
			width : 600,
			height : 450,
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
 * 积分类别代码
 */
function jifenCodeUpdate(ts,id){
	$(".subform").ajaxSubmit({
        type:"post",
        url:"/jiFen/ajaxUpdateJifenConfig?sid=${param.sid}&t="+Math.random(),
        dataType: "json",
        beforeSubmit:function (a,f,o){
        	var preValue = $(ts).attr("preValue");
        	var nowValue = $(ts).val();
        	if(preValue==nowValue){
        		return false;
        	}
			var reg = new RegExp("^\\d+$");  
		    var objVal = $(ts).val();  
		    if(!reg.test(objVal)){  
		        art.dialog({"content":"请填写数字"}).time(2);  
                return false;
		    }  
        },
        data:{"id":id,"jifenCode":$(ts).val()},
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
 * 积分类别名称
 */
function jifenTitleUpdate(ts,id){
	$(".subform").ajaxSubmit({
        type:"post",
        url:"/jiFen/ajaxUpdateJifenConfig?sid=${param.sid}&t="+Math.random(),
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
		        art.dialog({"content":"不能为空"}).time(2);  
                return false;
		    }  
        },
        data:{"id":id,"title":$(ts).val()},
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
 * 积分类别分值
 */
function jifenScoreUpdate(ts,id){
	$(".subform").ajaxSubmit({
        type:"post",
        url:"/jiFen/ajaxUpdateJifenConfig?sid=${param.sid}&t="+Math.random(),
        dataType: "json",
        beforeSubmit:function (a,f,o){
        	var preValue = $(ts).attr("preValue");
        	var nowValue = $(ts).val();
        	if(preValue==nowValue){
        		return false;
        	}
			var reg = new RegExp("^-?(0|[1-9][0-9]*)$");  
		    var objVal = $(ts).val();  
		    if(!reg.test(objVal)){  
		        art.dialog({"content":"请填写整数!"}).time(2);  
                return false;
		    }  
        },
        data:{"id":id,"typeScore":$(ts).val()},
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
 * 积分类别每日最高分值
 */
function jifenDayScoreUpdate(ts,id){
	$(".subform").ajaxSubmit({
        type:"post",
        url:"/jiFen/ajaxUpdateJifenConfig?sid=${param.sid}&t="+Math.random(),
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
		        art.dialog({"content":"请填写正整数!"}).time(2);  
                return false;
		    }  
        },
        data:{"id":id,"dayMaxScore":$(ts).val()},
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
 * 积分类别每日最高分值
 */
function jifenSysScoreUpdate(ts,id){
	$(".subform").ajaxSubmit({
        type:"post",
        url:"/jiFen/ajaxUpdateJifenConfig?sid=${param.sid}&t="+Math.random(),
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
		        art.dialog({"content":"请填写正整数!"}).time(2);  
                return false;
		    }  
        },
        data:{"id":id,"sysMaxScore":$(ts).val()},
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
 * 积分类别描述
 */
function jifenContentUpdate(ts,id){
	$(".subform").ajaxSubmit({
        type:"post",
        url:"/jiFen/ajaxUpdateJifenConfig?sid=${param.sid}&t="+Math.random(),
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
        data:{"id":id,"content":$(ts).val()},
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
 * 积分类别排序
 */
function jifenOrderNoUpdate(ts,id){
	$(".subform").ajaxSubmit({
        type:"post",
        url:"/jiFen/ajaxUpdateJifenConfig?sid=${param.sid}&t="+Math.random(),
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
        data:{"id":id,"orderNo":$(ts).val()},
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
function jifenConfigDel(id,title){
	art.dialog.confirm("确定删除积分类型\""+title+"\"？", function(){
		$(".subform").ajaxSubmit({
	        type:"post",
	        url:"/jiFen/jifenConfigDel?sid=${param.sid}&t="+Math.random(),
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
			<li style="float:left;height:30px;line-height:30px;position:relative;width:100px;text-align:center;background-color: #f0f000" >
				<a onclick="javascript:void(0)" href="javascript:void(0);">积分类别配置</a>
			</li>
			<li style="float:left;height:30px;line-height:30px;position:relative;width:100px;text-align:center;padding-left: 5px" >
				<a onclick="jifenLevConfig()" href="javascript:void(0);">积分等级配置</a>
			</li>
		</ul>
	</div>
</div>
<div class="block01">
	<form class="subform" method="post" style="margin-top:5px;">
			<input type="hidden" id="configNums" value="${fn:length(list)}"/>
			<table width="99%" align="center" border="0" cellspacing="0" cellpadding="0" class="tab1">
				<tr>
					<td align="left"><h4>积分类别配置</h4></td>
					<td width="15%"><a href="#" id="addJiFenType"><font color="blue">添加</font></a></td>
				</tr>
			</table>
			<table width="99%" align="center" border="0" cellspacing="0" id="jifenList" cellpadding="0" class="tab1">
			     <tr>
			     <th style="width: 30px;" height="30">序号</th>
			     <th align="center" width="8%">代码</th>
			     <th align="center">类别</th>
			     <th align="center" width="8%">分值</th>
			     <th align="center" width="8%">每日封顶</th>
			     <th align="center" width="8%">封顶</th>
			     <th align="center">积分项描述</th>
			     <th width="width: 30px;">排序</th>
			     <th style="width: 30px;">删除</th>
			     </tr>
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach items="${list}" var="obj" varStatus="status">
						<tr id="tr_${obj.id}">
			 				<td height="25">${status.count}</td>
			 				<td><input type="text" style="width: 100%" preValue="${obj.jifenCode}" value="${obj.jifenCode}" onchange="jifenCodeUpdate(this,'${obj.id}');" onkeydown="if(event.keyCode==13)jifenCodeUpdate(this,'${obj.id}')"/></td>
			 				<td><input type="text" preValue="${obj.title}" value="${obj.title}" onchange="jifenTitleUpdate(this,'${obj.id}');" onkeydown="if(event.keyCode==13)jifenTitleUpdate(this,'${obj.id}')"/></td>
			 				<td><input type="text" style="width: 90%;text-align: center;" preValue="${obj.typeScore}" value="${obj.typeScore}" onchange="jifenScoreUpdate(this,'${obj.id}');" onkeydown="if(event.keyCode==13)jifenScoreUpdate(this,'${obj.id}')"/></td>
			 				<td><input type="text" style="width: 90%;text-align: center;" preValue="${obj.dayMaxScore}" value="${obj.dayMaxScore}" onchange="jifenDayScoreUpdate(this,'${obj.id}');" onkeydown="if(event.keyCode==13)jifenDayScoreUpdate(this,'${obj.id}')"/></td>
			 				<td><input type="text" style="width: 90%;text-align: center;" preValue="${obj.sysMaxScore}" value="${obj.sysMaxScore}" onchange="jifenSysScoreUpdate(this,'${obj.id}');" onkeydown="if(event.keyCode==13)jifenSysScoreUpdate(this,'${obj.id}')"/></td>
			 				<td><input type="text" preValue="${obj.content}" value="${obj.content}" onchange="jifenContentUpdate(this,'${obj.id}');" onkeydown="if(event.keyCode==13)jifenContentUpdate(this,'${obj.id}')"/></td>
			 				<td style="width: 30px;"><input type="text" style="text-align: center" size="3" preValue="${obj.orderNo}" value="${obj.orderNo}" onchange="jifenOrderNoUpdate(this,'${obj.id}');" onkeydown="if(event.keyCode==13)jifenOrderNoUpdate(this,'${obj.id}')"/></td>
			 				<td style="width: 30px;"><img src="/static/images/cancel.png" onclick="jifenConfigDel('${obj.id}','${obj.title}');"/></td>
			 			</tr>
						</c:forEach>
					</c:when>
				</c:choose>
			</table>
		</form>
		
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>

