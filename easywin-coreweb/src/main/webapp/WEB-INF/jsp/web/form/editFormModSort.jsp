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
	$(function() {
		//填充列表数据
		$.ajax({type:"post",
			dataType:"json",
			url:'/web/form/ajaxListFormModSort?rnd='+Math.random(),
			success:function(data){
				if(data.status=='y'){
					var listFormSort = data.listFormSort;
					if(listFormSort && listFormSort.length>0){
						
						$.each(listFormSort,function(i,vo){
							i = i+1;
							initHtml(i,vo)
						})
						
					}
				}
			}
			
		});

		/**
		*取消添加项目阶段
		*/
		$("#cancleBtn").click(function(){
			$("#typeName").val('');
			$("#tempStage").attr("class",'hidden');
			$("#cancleDiv").css("display","none");
			
		});
		//排序只能输入数字
		$("#tempStage").find("td").eq(2).find("input").on('keypress',function(event){
			 var keyCode = event.which; 
	            if (keyCode >= 48 && keyCode <=57) //只能输入数字
	                return true; 
	            else 
	                return false; 
		}).on("cut paste",function(){//不能剪切和粘贴
			return false;
		});
		 binaddFormModSort();
		 binaddBtn();
	})
	//初始化页面数据
	function initHtml(i,vo){
		var tr = $('<tr></tr>');
		$(tr).attr("id","tr_"+vo.id);
		var td1 = $('<td class="sorting_1"></td>');
		$(td1).html(i);
		var td2 = $('\n	<td><input class="form-control small" style="color: #000" type="text"/></td>');
		$(td2).find("input").val(vo.sortName);
		$(td2).find("input").off("change").on("change",function(){
			var formModSort = $(this).parents("tr").data("formModSort");
			var sortName = $(this).val();
			var e=$(this);
			if($.trim(sortName).length>0){
				//填充列表数据
				$.ajax({type:"post",
					dataType:"json",
					url:'/web/form/updateFormModSort?rnd='+Math.random(),
					 data:{id:formModSort.id,
						 sortName:sortName},
					success:function(data){
						if(data.status=='y'){
							$(e).parents("tr").data("formModSort").sortName=sortName;
							showNotification(1,"修改成功!");
						}else{
							showNotification(2,data.info);
						}
					}
					
				});
			}
		}).on("blur",function(){
			var sortName = $(this).val();
			if($.trim(sortName).length==0){
				var formModSort = $(this).parents("tr").data("formModSort");
				$(this).val(formModSort.sortName)
			}
		});
		var td3 = $('\n	<td><input class="form-control small" style="color: #000" type="text"/></td>');
		$(td3).find("input").val(vo.orderNo);
		$(td3).find("input").on('keypress',function(event){
			 var keyCode = event.which; 
	            if (keyCode >= 48 && keyCode <=57) //只能输入数字
	                return true; 
	            else 
	                return false; 
		}).on("cut paste",function(){//不能剪切和粘贴
			return false;
		}).off("change").on("change",function(){
			var formModSort = $(this).parents("tr").data("formModSort");
			var orderNo = $(this).val();
			var e=$(this);
			if($.trim(orderNo).length>0){
				//填充列表数据
				$.ajax({type:"post",
					dataType:"json",
					url:'/web/form/updateFormModSort?rnd='+Math.random(),
					 data:{id:formModSort.id,
						 orderNo:orderNo},
					success:function(data){
						if(data.status=='y'){
							$(e).parents("tr").data("formModSort").orderNo=orderNo;
							showNotification(1,"修改成功!");
						}else{
							showNotification(2,data.info);
						}
					}
					
				});
			}
		}).on("blur",function(){
			var orderNo = $(this).val();
			var e=$(this);
			if($.trim(orderNo).length==0){
				var formModSort = $(this).parents("tr").data("formModSort");
				$(this).val(formModSort.orderNo)
			}
		});
		var td4 = $('<td> <a class="fa fa-times-circle-o fa-lg" href="javascript:void(0)" title="删除"></a></td>')
		$(td4).find("a").off("click").on("click",function(){
			var formModSort = $(this).parents("tr").data("formModSort");
			//填充列表数据
			$.ajax({type:"post",
				dataType:"json",
				url:'/web/form/delFormModSort?rnd='+Math.random(),
				 data:{formSortId:formModSort.id},
				success:function(data){
					if(data.status=='y'){
						$("#tr_"+formModSort.id).parents("tr").removeData("formModSort")
						$("#tr_"+formModSort.id).remove();
						showNotification(1,"删除成功!");
						$.each($("#editable-sample").find("tbody").find("tr"),function(index,obj){
							$(obj).find("td").eq(0).html(index+1)
						})
						
					}else{
						showNotification(2,data.info);
					}
				}
				
			});
		});
		$(tr).append($(td1))
		$(tr).append($(td2))
		$(tr).append($(td3))
		$(tr).append($(td4))
		$("#tempStage").before($(tr));
		$(tr).data("formModSort",{"id":vo.id,"sortName":vo.sortName,"orderNo":vo.orderNo});
	}
	//半丁保存
	function binaddFormModSort(){
		
		$("#tempStage").find("td").eq(3).find("a").off("click").on("click",function(){
			var $sortName = $(this).parents("tr").find("td").eq(1).find("input");
			var $orderNo = $(this).parents("tr").find("td").eq(2).find("input");
			var sortName = $sortName.val();
			if($.trim(sortName).length==0){
				layer.tips('请填写分类名称！', $sortName, {tips: 1});
				return;
			}
			var orderNo = $orderNo.val();
			if($.trim(orderNo).length==0){
				layer.tips('请填写分类排序！', $orderNo, {tips: 1});
				return;
			}
			$(this).off("click")
			$.ajax({
				 type : "post",
				  url : "/web/form/addFormModSort?rnd="+Math.random(),
				  dataType:"json",
				  data:{sortName:sortName,
					  orderNo:orderNo},
				  success : function(data){
					  if(data.status=='y'){
						  var formSort = data.formSort;
						  var lineNum = $("#editable-sample tbody").find("tr").length;
						  initHtml(lineNum,formSort);
						  $sortName.val('')
						  $orderNo.val(formSort.orderNo+1);
						  $("#tempStage").find("td").eq(0).html(lineNum+1);
				          showNotification(1,"添加成功");
					  }else{
				          showNotification(2,data.info);
					  }
					  binaddFormModSort()
				  },
				  error: function(XMLHttpRequest, textStatus, errorThrown){
			        	 showNotification(2,"系统错误！请联系管理人员");
			        	 binaddFormModSort()
				  }
			});
		})
	}
	function  binaddBtn(){
		/**
		*添加客户类型准备
		*/
		$("#addBtn").off("click").on("click",function(){
			var lineNum = $("#editable-sample tbody").find("tr").length;
			$("#tempStage").find("td").eq(0).html(lineNum);
			if($("#tempStage").attr("class")=='hidden'){//已经隐藏的,显示的时候从后取数据
				$("#tempStage").removeAttr("class");
				$("#cancleDiv").css("display","block");
				$.ajax({type:"post",
					dataType:"json",
					url:'/web/form/ajaxGetFormModSortOrder?rnd='+Math.random(),
					success:function(data){
						if(data.status=='y'){
							$("#tempStage").find("td").eq(2).find("input").val(data.orderNum)
						}
					}
					
				});
			}else{//已经在显示的时候了
				var $sortName = $("#tempStage").find("td").eq(1).find("input");
				var $orderNo = $("#tempStage").find("td").eq(2).find("input");
				var sortName = $sortName.val();
				if($.trim(sortName).length==0){
					layer.tips('请填写分类名称！', $sortName, {tips: 1});
					return;
				}
				var orderNo = $orderNo.val();
				if($.trim(orderNo).length==0){
					layer.tips('请填写分类排序！', $orderNo, {tips: 1});
					return;
				}
				$(this).off("click")
				$.ajax({
					 type : "post",
					  url : "/web/form/addFormModSort?rnd="+Math.random(),
					  dataType:"json",
					  data:{sortName:sortName,
						  orderNo:orderNo},
					  success : function(data){
						  if(data.status=='y'){
							  var formSort = data.formSort;
							  var lineNum = $("#editable-sample tbody").find("tr").length;
							  initHtml(lineNum,formSort);
							  $sortName.val('')
							  $orderNo.val(formSort.orderNo+1);
							  $("#tempStage").find("td").eq(0).html(lineNum+1);
					          showNotification(1,"添加成功");
						  }else{
					          showNotification(2,data.info);
						  }
						  binaddBtn()
					  },
					  error: function(XMLHttpRequest, textStatus, errorThrown){
				        	 showNotification(2,"系统错误！请联系管理人员");
				        	 binaddBtn()
					  }
				});
			}
		});
	}
</script>
<style type="text/css">
#editable-sample tbody>tr>td{
	padding: 1px 10px;
}
</style>
</head>
<body style="background-color:#fff;">
<div style="clear:both;padding-top: 5px"></div>
	<table id="editable-sample" class="table table-striped table-hover table-bordered dataTable" style="width: 97%;margin-left: 10px">
     <thead>
         <tr role="row">
         	<th style="width: 55px;" class="sorting_disabled" >
         		序号
         	</th>
         	<th class="sorting" >
         		分类名称
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
		<tr id="tempStage" class="hidden">
	       <td class="sorting_1" id="sortOrder"></td>
	       <td><input class="form-control small" style="color: #000" type="text"/></td>
	       <td><input class="form-control small" style="color: #000" type="text"/></td>
	       <td>
	       		<a class="fa fa-save fa-lg" href="javascript:void(0)" title="保存"></a>
	       	</td>
	     </tr>
 	</tbody>
 </table>
 <div style="text-align: center;">
 	<div style="float: left;margin-left: 200px">
		<button type="button" class="btn btn-info ws-btnBlue" id="addBtn">添加</button>
 	</div>
 	<div style="float: left;padding-left: 15px;display: none" id="cancleDiv">
		<button type="button" class="btn btn-info ws-btnBlue" style="background-color: #ccc;" id="cancleBtn">取消</button>
 	</div>
</div>
</body>
</html>
