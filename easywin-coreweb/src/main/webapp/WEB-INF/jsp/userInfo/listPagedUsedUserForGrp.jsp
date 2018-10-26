<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script> 
	
	function clickUser(ts,id,email,name){
		var json="{id:\'"+id+"\',email:\'"+email+"\',name:\'"+name+"\'}";
		parent.appendUser(ts.checked,json);
	}
	
	$(document).ready(function(){
		var o = parent.document.getElementById("listUser_id").options;
		var oArray = new Array();
		for(var i=0;i<o.length;i++){
		    oArray[i]=o[i].value;
		}
		$(":checkbox[name='id']").each(function(index){
			if(oArray.in_array($(this).val())){
				$(this).attr("checked","checked");
				
				$(this).parent().parent().find(".optCheckBox").css("display","block");
				$(this).parent().parent().find(".optRowNum").css("display","none");
			}
		});
	});
	//ifream嵌套的单个ifream
	function adjustHeight(id){
		var offset = $("#bottomDiv").offset();
		if(undefined==offset){
			return;
		}
		var offsettop = offset.top;
		var obj1 = parent.document.getElementById(id);  //取得父页面IFrame对象
		obj1.height = 375;  //调整父页面中IFrame的高度为此页面的高度  
	}
	
	/**
	 * 复选框全选，用于弹出窗口的全选
	 * 
	 * @param ckBoxElement
	 * @param ckBoxName
	 * @return
	 */
	function selectAll(ckBoxElement, ckBoxName){
		var checkStatus = $(ckBoxElement).attr('checked');
		var obj = $(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']");
		if (checkStatus) {
			//显示复选框
			$(obj).parent().parent().find(".optCheckBox").css("display","block");
			//隐藏序号
			$(obj).parent().parent().find(".optRowNum").css("display","none");
			
			$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', true);
		} else {
			//隐藏复选框
			$(obj).parent().parent().find(".optCheckBox").css("display","none");
			//显示序号
			$(obj).parent().parent().find(".optRowNum").css("display","block");
			
			$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', false);
		}
		$(":checkbox[name='" + ckBoxName + "']").click();
		if (checkStatus) {
			$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', true);
		} else {
			$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', false);
		}
		
	}
	
</script>

<script type="text/javascript">
$(function(){
	//操作删除和复选框
	$('.optTr').mouseover(function(){
		var display = $(this).find(".optTd .optCheckBox").css("display");
		if(display=='none'){
			$(this).find(".optTd .optCheckBox").css("display","block");
			$(this).find(".optTd .optRowNum").css("display","none");
		}
	});
	$('.optTr ').mouseout(function(){
		var optCheckBox = $(this).find(".optTd .optCheckBox");
			var check = $(optCheckBox).find("input").attr('checked');
			if(check){
				$(this).find(".optTd .optCheckBox").css("display","block");
				$(this).find(".optTd .optRowNum").css("display","none");
			}else{
				$(this).find(".optTd .optCheckBox").css("display","none");
				$(this).find(".optTd .optRowNum").css("display","block");
			}
	});
});
</script>

<style type="text/css">
.panel-body{padding:0px 0px}
.pagination{
margin:10px 0px;
margin-top: 2px
}
label {
	margin-bottom: 0px;
	height: 20px
}
</style>
</head>
<body style="background-color: #fff" onload="adjustHeight('allUser');">
<div class="panel">
<div class="panel-body ">
	<table class="table table-hover general-table" style="margin:2px 10px">
		<thead>
			<tr>
			  <th width="50px" valign="middle" style="padding: 8px 10px">
				  <label>
				  		<input type="checkbox" id="checkAllBox" class="colored-blue" onclick="selectAll(this,'id')" />
				  		<span class="text"></span>
				  </label>
			  </th>
			  <th width="150px" valign="middle" style="padding: 8px 10px">姓名</th>
			  <th valign="middle" style="padding: 8px 10px">Email</th>
			  <th width="150px" valign="middle" style="padding: 8px 10px">隶属部门</th>
			</tr>
		</thead>
		<tbody>
		<c:choose>
		 	<c:when test="${not empty list}">
		 		<c:forEach items="${list}" var="user" varStatus="status">
		 			<tr class="optTr">
		 				<td valign="middle" style="padding: 8px 10px;" class="optTd">
		 					<label class="optCheckBox" style="display: none; ">
			 					<input type="checkbox" class="colored-blue" name="id" onclick="clickUser(this,${user.id},'${user.email}','${user.userName}')" value="${user.id}"/>
			 					<span class="text no-margin" ></span>
				 			</label>
                             <label class="optRowNum" style="display: block;">${status.count}</label>
		 				</td>
		 				<td valign="middle" style="padding: 8px 10px">${ user.userName}</td>
		 				<td valign="middle" style="padding: 8px 10px">${ user.email}</td>
		 				<td valign="middle" style="padding: 8px 10px">${user.depName}</td>
		 			</tr>
		 		</c:forEach>
		 	</c:when>
		 	<c:otherwise>
		 		<tr>
		 			<td height="35" colspan="5" align="center"><h3>没有相关信息！</h3></td>
		 		</tr>
		 	</c:otherwise>
		 </c:choose>
		</tbody>
	</table>
	<div  style="margin-left: 10px">
		 <tags:pageBar url="/userInfo/listPagedUsedUserForGrp"></tags:pageBar>
	</div>
</div>
</div>	
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
