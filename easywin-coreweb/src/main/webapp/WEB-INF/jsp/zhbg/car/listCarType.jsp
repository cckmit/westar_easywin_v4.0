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
	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引

	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//反馈类型数据是否为空
		if(${empty listCarType}){
			$("#editable-sample").hide();
			$("#noData").show();
		}else{
			$("#noData").hide();
			$("#editable-sample").show();
		}
		/**
		*添加类型准备
		*/
		$("#addBtn").click(function(){
			$("#noData").hide();
			$("#editable-sample").show();
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
					  url : "/car/ajaxGetCarTypeOrder?sid=${param.sid}&rnd="+Math.random(),
					  dataType:"json",
					  success : function(data){
						if(data.status=='y'){
							$("#orderNum").val(data.orderNum)
						}
				     }
				});
			}else{//已经在显示的时候了
				if($("#typeName").val() && $("#orderNum").val()){
						addCarType();
				}else{
					 if(!$("#orderNum").val()){
						 $("#orderNum").focus();
						 layer.tips('请填写数据！', "#orderNum", {tips: 1});
					}else if(!$("#typeName").val()){
						 $("#typeName").focus();
						 layer.tips('请填写数据！', "#typeName", {tips: 1});
					}
						
				}
				
			}
			
		});

		/**
		*取消添加
		*/
		$("#cancleBtn").click(function(){
			$("#typeName").val('');
			$("#tempStage").attr("class",'hidden');
			
			$("#cancleDiv").hide();
			$("#addBtn").parent().show();
			
			var delStr = $("#editable-sample").find(".fa-times-circle-o").attr("title");
			//无数据
			 if(typeof(delStr) == "undefined"){
				$("#editable-sample").hide();
				$("#noData").show();
			}
			
		});
		
		//设置滚动条高度
		var height = $(window).height()-40-45;
		$("#contentBody").css("height",height+"px");
	})
	/**
	 *车辆类型添加
	 */
	var regE = /^\d+$/
	function addCarType(){
			
		if($("#orderNum").val() && $("#typeName").val()){
			var orderNum = $("#orderNum").val();
			var typeName = $("#typeName").val();
			if(!regE.test(orderNum)){//匹配就添加
				layer.tips('请填写数字！', "#orderNum", {tips: 1});
				$("#orderNum").focus();
				return;
			}
			
			$.ajax({
				 type : "post",
				  url : "/car/ajaxAddCarType?sid=${param.sid}&rnd="+Math.random(),
				  dataType:"json",
				  data:{orderNum:orderNum,
					  typeName:typeName
					  },
				  success : function(data){
					if(data.status=='y'){
						var lineNum = $("#editable-sample tbody").find("tr").length;
						$("#sortOrder").html(lineNum+1);
						var vo = data.carType;
						var html = '\n';
						html+='\n<tr id="tr_'+vo.id+'">';
						html+='\n	<td class="sorting_1">'+lineNum+'</td>';
						html+='\n	<td><input class="form-control small" style="color: #000" preValue="'+typeName+'" value="'+typeName+'" onchange="updateTypeName(this,\''+vo.id+'\');" type="text"/></td>';
						html+='\n	<td><input class="form-control small" style="color: #000" type="text" preValue="'+orderNum+'" value="'+orderNum+'" onchange="updateOrderNum(this,\''+vo.id+'\');"/></td>';
						html+='\n	<td> <a class="fa fa-times-circle-o fa-lg" href="javascript:void(0)" onclick="delCarType(\''+vo.id+'\',\''+typeName+'\');" title="删除"></a></td>';
						html+='\n </tr>';
						$("#tempStage").before(html);
						$("#typeName").val('')
						$("#orderNum").val(vo.orderNum);
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
			 if(!$("#orderNum").val()){
				 $("#orderNum").focus();
				 layer.tips('请先填写数据！', "#orderNum", {tips: 1});
			}else if(!$("#typeName").val()){
				 $("#typeName").focus();
				 layer.tips('请先填写数据！', "#typeName", {tips: 1});
			}
		}
	}
	
	//更新反馈类型名称
	function updateTypeName(obj,id){
		if(!strIsNull($(obj).val()) && !strIsNull(id)){
			$.post("/car/updateCarTypeName?sid=${param.sid}",{Action:"post",typeName:$(obj).val(),id:id},     
 				function (msgObjs){
				$(obj).parent().parent().find("a").attr("onclick","delCarType('"+id+"','"+$(obj).val()+"');");
				showNotification(1,msgObjs);
			});
		}
	}
	//更新反馈类型排序
	function updateOrderNum(obj,id){
		if(!strIsNull($(obj).val()) && !strIsNull(id)){
			$.post("/car/updateCarTypeOrder?sid=${param.sid}",{Action:"post",orderNum:$(obj).val(),id:id},     
 				function (msgObjs){
				showNotification(0,msgObjs);
			});
		}
	}
	//反馈类型删除
	function delCarType(id,typeName){
		parent.layer.confirm("确定删除车辆类型\""+typeName+"\"？", {
			  btn: ['确定','取消'] //按钮
			}, function(index){
				if(id){
					$.post("/car/delCarType?sid=${param.sid}",{Action:"post",id:id},     
		 				function (msgObjs){
						if(msgObjs.succ){
							$("#tr_"+id).remove();
							showNotification(1,msgObjs.promptMsg);
							var delStr = $("#editable-sample").find(".fa-times-circle-o").attr("title");
							//无数据并且取消按钮隐藏
							  if(typeof(delStr) == "undefined" && $("#cancleDiv").is(':hidden')){
								$("#editable-sample").hide();
								$("#noData").show();
							} 
						}else{
							showNotification(2,msgObjs.promptMsg);
						}
						parent.layer.close(index)
					},"json");
				}
			}, function(){
				  
			});
	}
</script>
<style type="text/css">
#editable-sample tbody>tr>td{
	padding: 1px 10px;
}
body:before{ background:#fff;}
</style>
</head>
<body>
<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">车辆类型维护</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
					<input type="hidden" id="totalNum" value="${fn:length(listCarType) }" title="现有的类型数"/>
					<table id="editable-sample" class="table table-striped table-hover table-bordered dataTable" style="width: 97%;margin-left: 10px">
				     <thead>
				         <tr role="row">
				         	<th style="width: 55px;" class="sorting_disabled" >
				         		序号
				         	</th>
				         	<th class="sorting" >
				         		类型名称
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
							<c:when test="${not empty listCarType}">
							<c:forEach items="${listCarType}" var="vo" varStatus="status">
							<tr id="tr_${vo.id}">
									<td height="28">${status.count}</td>
									<td><input class="form-control small" style="color: #000" preValue="${vo.typeName}" value="${vo.typeName}" onchange="updateTypeName(this,'${vo.id}');" type="text"/></td>
									 <td><input class="form-control small" style="color: #000" type="text" preValue="${vo.orderNum}" value="${vo.orderNum}" onchange="updateOrderNum(this,'${vo.id}');"/></td>
									 
									<td>
										<a class="fa fa-times-circle-o fa-lg" href="javascript:void(0)" onclick="delCarType('${vo.id}','${vo.typeName}');" title="删除">
									</a>
								</tr>
							</c:forEach>
							</c:when>
						</c:choose>
						<tr id="tempStage" class="hidden">
					       <td class="sorting_1" id="sortOrder"></td>
					       <td><input class="form-control small" style="color: #000" id="typeName" type="text"/></td>
					       <td><input class="form-control small" style="color: #000" type="text" id="orderNum"/></td>
					       <td>
					       		<a class="fa fa-save fa-lg" href="javascript:void(0)" onclick="addCarType()" title="保存"></a>
					       	</td>
					     </tr>
				 	</tbody>
				 </table>
				  <div id="noData" class="container" style="left:50%;top:50%;
					margin:150 0 0 235px;padding-top:60px;text-align:center;width:500px;">
					<section class="error-container text-center">
						<h1>
							<i class="fa fa-exclamation-triangle"></i>
						</h1>
						<div class="error-divider">
							<h2>您还没有相关数据！</h2>
							<p class="description">协同提高效率，分享拉近距离。</p>
						</div>
					</section>
				</div>
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
