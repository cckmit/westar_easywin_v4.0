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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript"> 
var selfGrouplist;
var openWindow;
function setWindow(win){
	openWindow = win;
	initData();
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
}
/**
 * 返回意见
 */
function returnVal(){
	var groups='';
	//保存的数据从父页面临时数据中取得
	var selfGrps = $(selfGrouplist).find("option");
	if(selfGrps.length>0){
		for(var i=0;i<selfGrps.length;i++){
			groups +="{id:"+$(selfGrps[i]).val()+",name:'"+$(selfGrps[i]).text().trim()+"'},";
		}
	}
	if(!strIsNull(groups)){
		groups = groups.substr(0,groups.length-1);
	}else{
		layer.msg("请选择分组信息",{icon: 7});
	}
	return groups;
	
}
function initData(){
	//给行添加复选框选择事件
	$(".nameRow").click(function(){
		//分组主键
		var id =$(this).parent().find("input[name='ids']").val(); 
		//分组名称
		var grpName =$(this).parent().find("input[name='ids']").attr("grpName"); 
		
		if($(this).parent().find("input[name='ids']").attr("checked")=="checked"){
			//取消选中
			$(this).parent().find("input[name='ids']").removeAttr("checked");
			//父页面临时数据移除
			$(openWindow).find("#selfGrouplist").find("option[value='"+id+"']").remove();
		}else{
			//父页面添加一条临时数据
			var option='\n<option value="'+id+'">'+grpName+'</option>';
			$(openWindow).find("#selfGrouplist").append(option);
			//选中一条数据
			$(this).parent().find("input[name='ids']").attr("checked","checked");
		}
	});
	
	selfGrouplist = $(openWindow).find("#selfGrouplist");
	var selfGrps = $(selfGrouplist).find("option");//父页面临时数据
	if(selfGrps.length>0){
		for(var i=0;i<selfGrps.length;i++){
			//选中的数据主键
			var id = $(selfGrps[i]).val();
			//翻页后也能保持数据
			$("input[name='ids'][value='"+id+"']").attr("checked","checked");
		}
	}
};
//修改父页面临时数据
function setval(ts){
	var checkStatus = $(ts).attr('checked');
	if(checkStatus){//添加
		var option='\n<option value="'+$(ts).val()+'">'+$(ts).attr("grpName")+'</option>';
		$(selfGrouplist).append(option);
	}else{//删除
		$(selfGrouplist).find("option[value='"+$(ts).val()+"']").remove();
	}
}

//宣布选中
function checkAll(ckBoxElement, ckBoxName){
	var checkStatus = $(ckBoxElement).attr('checked');
	if (checkStatus) {//选中
		$.each($("input[name='ids']"),function(){
			var option='\n<option value="'+$(this).val()+'">'+$(this).attr("grpName")+'</option>';
			$(selfGrouplist).append(option);
		});
		$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', true);
	} else {//没有选中
		$.each($("input[name='ids']"),function(){
			$(selfGrouplist).find("option[value='"+$(this).val()+"']").remove();
		});
		$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', false);
	}
}
</script>
<style>
.panel-heading{
	padding: 5px 20px
}
.panel-body{
	padding: 0px;
}
.pagination-lg >li>a{
font-size: small;
}
.pagination{
margin: 10px 0px
}

.table{
margin-bottom:0px;
}
.table tbody>tr>td{
	padding: 5px 0px;
}
tr{
cursor: auto;
}

</style>
</head>
<body>

<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	<span class="widget-caption themeprimary ps-layerTitle">个人私有组列表</span>
	<div class="widget-buttons ps-toolsBtn">
		<a href="javascript:void(0)" class="blue" onclick="addGrp('${param.sid}')">
			<i class="fa fa-plus"></i>添加
		</a>
	</div>
     <div class="widget-buttons">
		<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
			<i class="fa fa-times themeprimary"></i>
		</a>
	</div>
</div>
<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
	<div class="panel-body">
	<table  class="table table-striped table-hover general-table">
		<thead>
           		<tr>
                   <th style="text-align: center;width: 50px">
                   		<label>
                   			<input type="checkbox" onclick="checkAll(this,'ids')" />
                   			<span class="text">&nbsp;</span>
                   		</label>
                   </th>
				<th valign="middle" style="width: 50px">序号</th>
				<th  valign="middle">候选组</th>
               </tr>
          </thead>
           <tbody>
			 <c:choose>
			 	<c:when test="${not empty listSelfGroup}">
			 		<c:forEach items="${listSelfGroup}" var="obj" varStatus="status">
			 			<tr>
			 				<td height="30" style="text-align: center;">
			 					<label>
				 					<input type="checkbox" name="ids" value="${obj.id}" grpName="${obj.grpName}" onclick="setval(this)"/>
									<span class="text">&nbsp;</span>
								</label>
										
			 				</td>
			 				<td align="center">${status.count}</td>
			 				<td><a href="javascript:void(0);">&nbsp;&nbsp;&nbsp;${obj.grpName}</a></td>
			 			</tr>
			 		</c:forEach>
			 	</c:when>
			 </c:choose>
		 </tbody>
	</table>
	<tags:pageBar url="/crm/querySelfGroup"></tags:pageBar>
</div>
</div>
</body>
</html>

