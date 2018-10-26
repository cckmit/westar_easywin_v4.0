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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	$(function(){
		/**
		*添加项目阶段
		*/
		$("#addBtn").click(function(){
			var lineNum = $("#editable-sample tbody").find("tr").length;
			$("#sortOrder").html(lineNum);
			if($("#tempStage").attr("class")=='hidden'){//已经隐藏的,显示的时候从后取数据
				$("#tempStage").removeAttr("class");
			
				$("#cancleDiv").show();
				$("#addBtn").parent().hide();
				
				var div = document.getElementById('contentBody');
				div.scrollTop = div.scrollHeight;
				

				//从后台取数据，取不上也没有关系
				$.ajax({
					 type : "post",
					  url : "/item/ajaxGetStageOrder?sid=${param.sid}&rnd="+Math.random(),
					  dataType:"json",
					  data:{"itemId":${stagedItem.itemId},"id":-1},
					  success : function(data){
						if(data.status=='y'){
							$("#stageOrder").val(data.stageOrder)
						}
				     }
				});
			}else{//已经在显示的时候了
				if($("#stageOrder").val() && $("#stageName").val()){
						additemStaged();
				}else{
					 if(!$("#stageOrder").val()){
						 $("#stageOrder").focus();
						 layer.tips('请填写数据！', "#stageOrder", {tips: 1});
					}else if(!$("#stageName").val()){
						 $("#stageName").focus();
						 layer.tips('请填写数据！', "#stageName", {tips: 1});
					}
						
				}
				
			}
			
		});
		/**
		*取消添加项目阶段
		*/
		$("#cancleBtn").click(function(){
			$("#stageName").val('');
			$("#tempStage").attr("class","hidden");

			$("#cancleDiv").hide();
			$("#addBtn").parent().show();
			
			
		});
		
		//设置滚动条高度
		var height = $(window).height()-80;
		$("#contentBody").css("height",height+"px");
		
	});
	//更新项目阶段名称
	function stagedNameUpdate(obj,id){
		if(!strIsNull($(obj).val()) && !strIsNull(id)){
			$.post("/item/stagedNameUpdate?sid=${param.sid}",{Action:"post",itemId:${item.id},stagedName:$(obj).val(),id:id},     
 				function (msgObjs){
				$(obj).attr("preValue",$(obj).val());
				showNotification(0,msgObjs);
			});
		}else{
			$(obj).val($(obj).attr("preValue"))
		}
	}
	/**
	 * 项目阶段添加
	 */
	var regE = /^\d+$/
	function additemStaged(){
		if($("#stageOrder").val() && $("#stageName").val()){
			var stageOrder = $("#stageOrder").val();
			var stageName = $("#stageName").val();
			if(!regE.test(stageOrder)){//匹配就添加
				layer.tips('请填写数字！', "#stageOrder", {tips: 1});
				$("#stageOrder").focus();
				return;
			}
			
			$.ajax({
				 type : "post",
				  url : "/item/ajaxAddStagedFolder?sid=${param.sid}&rnd="+Math.random(),
				  dataType:"json",
				  data:{itemId:${stagedItem.itemId},
					  parentId:-1,
					  stagedOrder:stageOrder,
					  stagedName:stageName
					  },
				  success : function(data){
					if(data.status=='y'){
						var lineNum = $("#editable-sample tbody").find("tr").length;
						$("#sortOrder").html(lineNum+1);
						var vo = data.stagedItem;
						var html = '\n';
						html+='\n<tr id="tr_'+vo.id+'">';
						html+='\n	<td class="sorting_1">'+lineNum+'</td>';
						html+='\n	<td><input class="form-control small" style="color: #000" preValue="'+stageName+'" value="'+stageName+'" onchange="stagedNameUpdate(this,\''+vo.id+'\');" type="text"/></td>';
						html+='\n	<td><input class="form-control small" style="color: #000" type="text" preValue="'+stageOrder+'" value="'+stageOrder+'" onchange="stagedOrderUpdate(this,\''+vo.id+'\');"/></td>';
						html+='\n	<td> <a class="fa fa-times-circle-o" href="javascript:void(0)" onclick="itemStagedDel(\''+vo.id+'\',\''+vo.stagedName+'\');" title="删除"></a></td>';
						html+='\n </tr>';
						$("#tempStage").before(html);
						$("#stageName").val('')
						$("#stageOrder").val(vo.stagedOrder);
			        	showNotification(1,"添加成功");
			        	
			        	var div = document.getElementById('contentBody');
						div.scrollTop = div.scrollHeight;
						
					}
			     },
			     error: function(XMLHttpRequest, textStatus, errorThrown){
		        	 showNotification(2,"系统错误！请联系管理人员");
			     }
			});
		}else{
			 if(!$("#stageOrder").val()){
				 $("#stageOrder").focus();
				 layer.tips('请填写数据！', "#stageOrder", {tips: 1});
			}else if(!$("#stageName").val()){
				 $("#stageName").focus();
				 layer.tips('请填写数据！', "#stageName", {tips: 1});
			}
		}
	}
	//更新项目阶段排序
	function stagedOrderUpdate(obj,id){
		if(!strIsNull($(obj).val()) && !strIsNull(id)){
			$.post("/item/stagedOrderUpdate?sid=${param.sid}",{Action:"post",itemId:${item.id},stagedOrder:$(obj).val(),id:id},     
 				function (msgObjs){
				$(obj).attr("preValue",$(obj).val());
				showNotification(0,msgObjs);
			});
		}else{
			$(obj).val($(obj).attr("preValue"))
		}
	}
	//项目阶段删除
	function itemStagedDel(id,stagedName){
		
		window.top.layer.confirm("确定删除\"${item.itemName}\"的项目阶段\""+stagedName+"\"", {
			  btn: ['确定','取消'] //按钮
			}, function(index){
				window.top.layer.close(index);
				window.top.layer.confirm("项目阶段\""+stagedName+"\"下数据是否一起删除？", {
					  btn: ['是','否'] //按钮
					}, function(index){
						if(!strIsNull(id)){
							$.post("/item/itemStagedDel?sid=${param.sid}&delChildren=yes",{Action:"post",itemId:${item.id},id:id},     
				 				function (msgObjs){
								if(msgObjs.succ){
									$("#tr_"+id).remove();
								}
								showNotification(0,msgObjs.promptMsg);
							},"json");
						}
						window.top.layer.close(index);
					}, function(){
						if(!strIsNull(id)){
							$.post("/item/itemStagedDel?sid=${param.sid}&delChildren=no",{Action:"post",itemId:${item.id},id:id},     
				 				function (msgObjs){
								if(msgObjs.succ){
									$("#tr_"+id).remove();
								}
								showNotification(0,msgObjs.promptMsg);
							},"json");
						}  
						window.top.layer.close(index);
					});
			}, function(){
				  
			});
	}
	
</script>
<style type="text/css">
#editable-sample tbody>tr>td{
	padding: 1px 10px;
}
body:before{
	background: #fff
}
</style>
</head>
<body style="background-color:#fff;">

<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">项目阶段维护</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
					
				 	<input type="hidden" id="totalNum" value="${fn:length(listStagedItem) }" title="现有的阶段数"/>
					<table id="editable-sample" class="table table-striped table-hover table-bordered dataTable" style="width: 97%;margin-left: 10px">
				     <thead>
				         <tr role="row">
				         	<th style="width: 55px;" class="sorting_disabled" >
				         		序号
				         	</th>
				         	<th  class="sorting" >
				         		阶段名称
				         	</th>
				         	<th style="width: 70px" class="sorting" >
				         		排序号
				         	</th>
				         	<th style="width: 55px;" class="sorting" >
				         		操作
				         	</th>
				         </tr>
				     </thead>
				     
				 	<tbody align="center">
				 	<c:choose>
						<c:when test="${not empty listStagedItem}">
							<c:forEach items="${listStagedItem}" var="vo" varStatus="status">
							 	<tr id="tr_${vo.id}">
							       <td class="sorting_1">${status.count}</td>
							       <td><input class="form-control small" style="color: #000" preValue="${vo.stagedName}" value="${vo.stagedName}" onchange="stagedNameUpdate(this,'${vo.id}');" type="text"/></td>
							       <td><input class="form-control small" style="color: #000" type="text" preValue="${vo.stagedOrder}" value="${vo.stagedOrder}" onchange="stagedOrderUpdate(this,'${vo.id}');"/></td>
							       <td>
							       	<a class="fa fa-times-circle-o" href="javascript:void(0)" onclick="itemStagedDel('${vo.id}','${vo.stagedName}');" title="删除"></a>
							       	</td>
							     </tr>
							</c:forEach>
						</c:when>
					</c:choose>
						<tr id="tempStage" class="hidden">
					       <td class="sorting_1" id="sortOrder"></td>
					       <td><input class="form-control small" style="color: #000" id="stageName" type="text"/></td>
					       <td><input class="form-control small" style="color: #000" type="text" id="stageOrder"/></td>
					       <td>
					       		<a class="fa fa-save" href="javascript:void(0)" onclick="additemStaged()" title="保存"></a>
					       	</td>
					     </tr>
				    </tbody>
				 </table>
				</div>
				
				 <div class="align-center clearfix" style="padding:6px 10px;">
				 	<div  class="pull-right" style="margin-left: 15px;display: none" id="cancleDiv">
						<button type="button" class="btn btn-default" style="background-color: #ccc;" id="cancleBtn">取消</button>
				 	</div>
				 	<div class="pull-right">
						<button type="button" class="btn btn-info ws-btnBlue" id="addBtn">添加</button>
				 	</div>
				</div>
				
			</div>
		</div>
	</div>
</div>
</body>
</html>
