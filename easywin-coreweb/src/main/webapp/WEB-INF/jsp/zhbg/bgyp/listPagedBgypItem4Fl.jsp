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
var sid="${param.sid}";
$(function(){
	//添加办公用品条目
	$("body").on("click","#addBgypItem",function(){
		window.self.location="/bgypItem/addBgypItemPage?sid=${param.sid}&flId=${bgypItem.flId}&flName=${bgypItem.flName}&redirectPage="+encodeURIComponent(window.self.location)
	});
	//修改办公用品条目
	$("body").on("click",".updateBgypItem",function(){
		var  bgypItemId = $(this).attr("busId");
		window.self.location="/bgypItem/updateBgypItemPage?sid=${param.sid}&bgypItemId="+bgypItemId+"&redirectPage="+encodeURIComponent(window.self.location)
	});
	//删除办公用品条目
	$("body").on("click",".delBgypItem",function(){
		var  bgypItemId = $(this).attr("busId");
		
		window.top.layer.confirm("确定删除办公用品条目？",function(index,larero){
			window.top.layer.close(index);
			$("#delForm").find("input[name='redirectPage']").val(window.self.location);
			$("#delForm").find(":checkbox[name='bgypItemIds']").attr('checked', false);
			$("#delForm").find(":checkbox[name='bgypItemIds'][value='"+bgypItemId+"']").attr('checked', true);
			$("#delForm").submit();
		})
		
	});
	//删除办公用品条目
	$("body").on("click","#deleteBgypItem",function(){
		if(checkCkBoxStatus('bgypItemIds')){
			window.top.layer.confirm("确定删除办公用品条目？",function(index,larero){
				window.top.layer.close(index);
				$("#delForm").find("input[name='redirectPage']").val(window.self.location);
				$("#delForm").submit();
			})
		}
		
	});
	//删除办公用品条目
	$("body").on("click","#updateBgypItemFl",function(){
		if(checkCkBoxStatus('bgypItemIds')){
			bgflOneSelect(null, null, null, sid,function(data){
				console.log(data.bgflId);
				console.log(data.bgflName);
				if(data.bgflId>0){
					$("#delForm").attr("action","/bgypItem/updateBgypItemFl");
					$("#delForm").append('<input type="hidden" name="bgflId" value="'+data.bgflId+'">');
					$("#delForm").append('<input type="hidden" name="bgFlName" value="'+data.bgflName+'">');
					$("#delForm").find("input[name='redirectPage']").val(window.self.location);
					$("#delForm").submit();
					return true;
				}else{
					window.top.layer.alert('不能选择根节点。');
					return false;
					
				}
				
			});
		}else{
			showNotification(2,"请选择需要修改分类的用品");
		}
		
	});
	
	//操作删除和复选框
	$('.optTr').mouseover(function(){
		var display = $(this).find(".optTd .optCheckBox").css("display");
		if(display=='none'){
			$(this).find(".optTd .optCheckBox").css("display","block");
			$(this).find(".optTd .optRowNum").css("display","none");
		}
	});
	$('.optTr').mouseout(function(){
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
	$(":checkbox[name='bgypItemIds'][disabled!='disabled']").click(function(){
		var checkLen = $(":checkbox[name='bgypItemIds'][disabled!='disabled']:checked").length;
		var len = $(":checkbox[name='bgypItemIds'][disabled!='disabled']").length;
		if(checkLen>0){
			//隐藏查询条件
			$(".searchCond").css("display","none");
			//显示批量操作
			$(".batchOpt").css("display","block");
			if(checkLen==len){
				$("#checkAllBox").attr('checked', true);
			}else{
				$("#checkAllBox").attr('checked', false);
			}
		}else{
			//显示查询条件
			$(".searchCond").css("display","block");
			//隐藏批量操作
			$(".batchOpt").css("display","none");
			
			$("#checkAllBox").attr('checked', false);
		}
	});
})
</script>
<style type="text/css">
td{
	padding: 5px 0px!important;
}
th{
	padding: 5px 0px!important;
}
</style>
</head>
<body onload="resizeVoteH('rightFrame')" style="background:#fff;">
<div class="panel">
<header class="panel-heading">${bgypItem.flName }
	<div class="pull-right ws-search-box">
		<c:if test="${not empty bgypItem.flId}">
			<div class="btn-group">
				<a class="btn btn-default dropdown-toggle btn-xs " data-toggle="dropdown" id="addBgypItem">
					新增用品
				</a>
	       	</div>
		</c:if>
		<div class="btn-group">
			<a class="btn btn-default dropdown-toggle btn-xs margin-left-5" data-toggle="dropdown" id="updateBgypItemFl">
				分类修改
			</a>
       	</div>
		<div class="btn-group">
			<a class="btn btn-default dropdown-toggle btn-xs margin-left-5" data-toggle="dropdown" id="deleteBgypItem">
				删除用品
			</a>
       	</div>
	</div>
</header> 
<div class="panel-body">
<form action="/bgypItem/delBgypItem" id="delForm">
<input type="hidden" name="redirectPage">
<input type="hidden" name="sid" value="${param.sid}">
	<table class="table table-hover general-table">
		<thead>
			<tr>
			  <th width="8%" style="text-align: left;">
			  	<label>
			  		<input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'bgypItemIds')">
			  		<span class="text" style="color: inherit;"></span>
				</label>
			  </th>
			  <th width="15%" valign="middle">办公类别</th>
			  <th valign="middle">用品名称</th>
			  <th width="9%" valign="middle"  style="text-align: center;">规格</th>
			  <th width="8%" valign="middle"  style="text-align: center;">单位</th>
			  <th width="8%" valign="middle"  style="text-align: center;">库存</th>
			  <th width="12%" valign="middle"  style="text-align: center;">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:choose>
		 	<c:when test="${not empty listBgypItem}">
		 		<c:forEach items="${listBgypItem}" var="obj" varStatus="status">
		 			<tr class="optTr">
		 				<td class="optTd" height="30" style="text-align: center;line-height: 20px">
		 					<label class="optCheckBox" style="display: none;width: 15px;">
								<input class="colored-blue" type="checkbox" name="bgypItemIds" value="${obj.id}"/>
								<span class="text"></span>
							</label> 
							<label class="optRowNum" style="display: block;width: 15px">${status.count}</label>
						</td>
		 				<td>${obj.flName}</td>
		 				<td>[${obj.bgypCode}]${ obj.bgypName}</td>
		 				<td  style="text-align: center;">${empty obj.bgypSpec?'/':obj.bgypSpec}</td>
		 				<td valign="middle" style="text-align: center;">
		 					<tags:viewDataDic type="bgypUnit" code="${obj.bgypUnit}"></tags:viewDataDic>
		 				</td>
		 				<td  style="text-align: center;">${obj.storeNum}</td>
		 				<td style="text-align: center;">
		 					<a href="javascript:void(0)" busId="${obj.id }" class="updateBgypItem">编辑</a>
		 					|
		 					<a href="javascript:void(0)" busId="${obj.id }" class="delBgypItem">删除</a>
		 				</td>
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
	 <tags:pageBar url="/bgypItem/listPagedBgypItem4Fl"></tags:pageBar>
</form>
</div>
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
