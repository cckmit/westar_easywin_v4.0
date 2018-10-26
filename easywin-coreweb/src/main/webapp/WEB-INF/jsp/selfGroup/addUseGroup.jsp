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
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
$(function(){
	$('.subform').Validform({
		tiptype : function(msg, o, cssctl) {
			validMsgV2(msg, o, cssctl);
		},
		datatype:{
			"ENcode":function(gets,obj,curform,regxp){
				var text = $(obj).val(); 
				if(!text){
					return false;
				}
				//匹配这些中文标点符号 。 ？ ！ ， 、 ； ： “ ” ‘ ' （ ） 《 》 〈 〉 【 】 『 』 「 」 ﹃ ﹄ 〔 〕 … — ～ ﹏ ￥ 
				var reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5]/;
				 if(reg.test(text)){  
					 return "不能输入中文标点字符";
				}else{ 
					return true;
				}
			}
		},
		showAllError : true
	});
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
});
function checkText(){ 
	var text = document.getElementById('grpName').value; 
	//匹配这些中文标点符号 。 ？ ！ ， 、 ； ： “ ” ‘ ' （ ） 《 》 〈 〉 【 】 『 』 「 」 ﹃ ﹄ 〔 〕 … — ～ ﹏ ￥ 
	var reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5]/;
	 if(reg.test(text)){  
		 layer.alert('是中文标点符号'); 
	}else{ 
		layer.alert('不是中文标点符号'); 
	}
}
	//添加部门成员
	function appendUser(checked,str){
	  var json = eval('(' + str + ')'); 
	  if(checked==true){
	   if($("#listUser_id option[value='"+json.id+"']").length==0){
		   	if(null!=json.name&&""!=json.name){
			     $('#listUser_id').append("<option value='"+json.id+"'>"+json.name+"</option>");
		   	}else{
			     $('#listUser_id').append("<option value='"+json.id+"'>"+json.email+"</option>");
		   	}
	   }
	  }else{
	   var selectobj = document.getElementById("listUser_id");
	   for (var i = 0; i < selectobj.options.length; i++) {
			  if(selectobj.options[i].value==json.id){
			    selectobj.options[i] = null;
			    break;
			  }
	   }
	  }
	}
	function tjGrpForm(){
		 var selectobj = document.getElementById("listUser_id");
		 var grpName = $("#grpName").val();
		 if(strIsNull(grpName)){
			 layer.tips('请填写组群名称！', "#grpName", {tips: 1});
			 $("#grpName").focus()
			 return false;
		}
		 if(selectobj.options.length==0){
			 layer.tips('分组成员未选择！', "#allUser", {tips: 1});
			 return false;
		 }else{
			 var result={"options":$(selectobj).find("option"),"grpName":$("#grpName").val()}
			return result;
		 }
	} 

</script>
<style type="text/css">
body:before{
	background: #fff;
}
.Validform_checktip{
margin-top: 5px
}
</style>
</head>
<body>

<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	<span class="widget-caption themeprimary ps-layerTitle">添加分组</span>
     <div class="widget-buttons">
		<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
			<i class="fa fa-times themeprimary"></i>
		</a>
	</div>
</div>
	             
<div class="panel margin-top-40" id="contentBody">
<form class="subform" id="groupForm">
	<div class="widget-body no-shadow no-padding">
		<div class="tickets-container bg-white">
			<ul class="tickets-list no-margin-bottom">
	        	<li class="ticket-item no-shadow ps-listline" style="border: 0">
				    <div class="pull-left gray ps-left-text">
				    	&nbsp;群组名称
				    	<span style="color: red">*</span>
				    </div>
					<div class="ticket-user pull-left ps-right-box">
						<div style="float: left">
							<input class="form-control pull-left" style="width: 250px;margin-right: 0px" 
							type="text" id="grpName" datatype="ENcode" name="grpName" nullmsg="请填群组名称" value=""/>
						</div>
					</div>
				</li>
			</ul>
			<div style="display:none">
				<%--人员选择 --%>
				<select list="listUser" listkey="id" listvalue="listUser.name" id="listUser_id" name="listUser.id" ondblclick="removeOne(this)" multiple="multiple" moreselect="true">
					<option value="${userInfo.id}" selected="selected">${userInfo.userName}</option>
				</select>
			</div>
			
			
		</div>
	</div>
	<div class="widget-body no-shadow no-padding">
		<iframe style="width:100%;" id="allUser" name="allUser"
			src="/userInfo/listPagedUsedUserForGrp?sid=${param.sid }&grpId=-1&pager.pageSize=8"
			border="0" frameborder="0" allowTransparency="true"
			noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
	</div>
</form>
</body>
</html>
