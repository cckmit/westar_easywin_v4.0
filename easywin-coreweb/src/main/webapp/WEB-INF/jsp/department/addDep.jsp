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
	$(function() {
		$('.subform').Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
		parent.hideSearch();
		
		//设置部门名称失焦事件
		setAddBlurEvent($("#depName"),"${param.parentId}",function(data){
			if(data.status=='y'){
				//重新初始化树形结构
				parent.frames["leftFrame"].initZtree();
				//保存部门名称为临时变量
				$("#depName").data("preDepName",$("#depName").val());
				//板寸部门主键为临时变量
				$("#contentBody").data("depId",data.dep.id);
				showNotification(1,"操作成功！")
			}else{
				showNotification(2,data.info);
				$("#depName").val($("#depName").data("preDepName"))
			}
		});
		//人员展示的克隆模板
		var depUserHtml = $('<span class="label label-default  margin-top-10 margin-left-5 margin-bottom-5 pull-left"></span>');
		//部门人员设置按钮，点击事件
		$("#depUserSetBtn").on("click",function(){
			userMore("depUserList", "", "${param.sid}",null,null,function(options){
				//设置部门人员信息
				updateDepUsers(options,"depUserList",function(data){
					if(data.status=='y'){
					  //重新展示数据
					  removeOptions("depUserList");
					  $("#depUserDiv").html("");
					  var userIds =new Array();
					  for ( var i = 0; i < options.length; i++) {
						  userIds.push(options[i].value);
						  $('#depUserList').append(
								  "<option selected='selected' value='"
								  + options[i].value + "'>" + options[i].text
								  + "</option>");
						  
						  var depUserClone = $(depUserHtml).clone();
						  $(depUserClone).html(options[i].text);
						  $("#depUserDiv").append( $(depUserClone))
					  }
					  $('#depUserList').focus();
					  $('#depUserList').blur();
					  showNotification(1,"操作成功！")
					}else{
						showNotification(2,data.info);
					}
				});
			})
		})
	})
	//部门设置人员
	function updateDepUsers(options,userid,callback){
		  var userIds =new Array();
		  for ( var i = 0; i < options.length; i++) {
			  userIds.push(options[i].value);
		  }
		  var loadState = $("#contentBody").data("loadState");
		  if(loadState){
				return false;
		  }
		  var params={
					"id":$("#contentBody").data("depId"),
					"sid":"${param.sid}",
					"userIds":userIds.join(",")
		  }
		  
		  $("#contentBody").data("loadState","1");
		  postUrl("/department/updateDepUsers",params,function(data){
			callback(data);
			$("#contentBody").removeData("loadState");
		  })
	}
	//添加部门时
	function setAddBlurEvent(depNameObj,parentId,callback){
		$(depNameObj).on("blur",function(event){
			var depName = $(this).val();
			var loadState = $("#contentBody").data("loadState");
			if(depName && !loadState){
				var params={
						"depName":depName,
						"sid":"${param.sid}",
						"parentId":parentId
				}
				var depId = $("#contentBody").data("depId");
				var preDepName = $("#depName").data("preDepName");
				if(!preDepName || preDepName != depName){
					$("#contentBody").data("loadState","1");
					if(!depId){
						postUrl("/department/ajaxAddDep",params,function(data){
							callback(data);
							$("#contentBody").removeData("loadState");
						})
					}else{
						params.id=depId;
						postUrl("/department/ajaxUpdateDep",params,function(data){
							callback(data);
							$("#contentBody").removeData("loadState");
						})
					}
				}
			}else{
				var data = {}
				if(!depName){
					data.status="f";
					data.info="部门名称不能为空";
					callback(data);
				}
			}
	    });
	}
	
</script>
</head>
<body onload="resizeVoteH('rightFrame')">
<div class="widget-body" id="contentBody" style="height: 480px">
	<div class="widget radius-bordered no-border" style="cursor: default;">
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue">新增部门</span>
		 </div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white">
				<ul class="tickets-list">
					<li class="ticket-item no-shadow ps-listline no-border">
						<div class="pull-left gray ps-left-text">
							上级部门名称:
						</div>
						<div class="ticket-user pull-left ps-right-box no-border" style="border-bottom: 0px !important;">
								<div id="parentDepName"  
								class="no-border"
								style="width: 400px;border-bottom: 0px !important;">${param.parentName }</div>
						</div>
					  </li>
					 <li class="ticket-item no-shadow ps-listline">
						<div class="pull-left gray ps-left-text">
							&nbsp;部门名称
							<span style="color: red">*</span>
						</div>
						<div class="ticket-user pull-left ps-right-box">
								<input id="depName" datatype="*" name="depName" nullmsg="请填部门名称" 
								class="colorpicker-default form-control" type="text" value=""
								style="width: 400px;float: left">
						</div>
					  </li>
					  
					  <li class="ticket-item no-shadow autoHeight no-padding">
						    <div class="pull-left gray ps-left-text padding-top-10">
						    	人员设定:
						    </div>
							<div class="ticket-user pull-left ps-right-box" style="height: auto;">
								<div class="pull-left gray ps-left-text padding-top-5">
									<div style="float: left; width: 250px;display:none;">
										<select id="depUserList"  multiple="multiple" moreselect="true">
										</select>
									</div>
									<div id="depUserDiv" class="pull-left" style="max-width:460px">
										
									</div>
									<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5" 
										title="设定人员" id="depUserSetBtn" style="float: left;"><i class="fa fa-plus"></i>选择</a>
									</div>
							</div>
							<div class="ps-clear"></div>                 
                      </li>
				</ul>
			</div>
		</div>
	</div>
</div>
					
					 

<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
