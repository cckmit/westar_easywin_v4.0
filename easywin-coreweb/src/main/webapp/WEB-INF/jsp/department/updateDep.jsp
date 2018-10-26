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
		//隐藏搜索框
		parent.hideSearch();
		//保存部门主键为临时变量
		$("#contentBody").data("depId","${dep.id}");
		//保存部门名称为临时变量
		$("#depName").data("preDepName","${dep.depName}");
		//设置部门名称失焦事件
		setUpdateBlurEvent($("#depName"),function(data){
			if(data.status=='y'){
				//重新初始化树形结构
				parent.frames["leftFrame"].initZtree();
				//保存部门名称为临时变量
				$("#depName").data("preDepName",$("#depName").val());
				showNotification(1,"操作成功！")
			}else{
				showNotification(2,data.info);
				$("#depName").val($("#depName").data("preDepName"))
			}
		});
		//人员展示模板
		var depUserHtml = $('<span class="label label-default  margin-top-10 margin-left-5 margin-bottom-5 pull-left"></span>');
		//人员设置按钮
		$("#depUserSetBtn").on("click",function(){
			userMore("depUserList", "", "${param.sid}",null,null,function(options){
				//设置部门人员信息
				updateDepUsers(options,"depUserList",function(data){
					if(data.status=='y'){
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
				return;
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
	function setUpdateBlurEvent(depNameObj,callback){
		$(depNameObj).on("blur",function(event){
			var depName = $(this).val();
			var loadState = $("#contentBody").data("loadState");
			if(depName && !loadState){
				var params={
						"depId":$("#contentBody").data("depId"),
						"depName":depName,
						"sid":"${param.sid}"
				}
				var depId = $("#contentBody").data("depId");
				var preDepName = $("#depName").data("preDepName");
				if(!preDepName || preDepName != depName){
					$("#contentBody").data("loadState","1");
					params.id=depId;
					postUrl("/department/ajaxUpdateDep",params,function(data){
						callback(data);
						$("#contentBody").removeData("loadState");
					})
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
	$(function(){
	    $("#depChooseBtn").on("click",function(){
            window.top.layer.open({
                type: 2,
                //title: ['部门单选', 'font-size:18px;'],
                title:false,
                closeBtn:0,
                area: ['400px', '450px'],
                fix: true, //不固定
                maxmin: false,
                move: false,
                scrollbar:false,
                content: ['/common/depOnePage?sid=${param.sid}&rootId=' + ${dep.id},'no'],
                btn: ['确定','取消'],
                yes: function(index, layero){
                    var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
                    var result = iframeWin.returnOrg();
                    if(result){
                        //修改当前部门的parentId
						$.post("/department/updateDepStructure?sid=${param.sid}",{Action:"post",depId:${dep.id},parentId:result.orgId},
							function (data){
								if(data.status=='y'){
									$("#parentDepName").html("");
									$("#parentDepName").html(result.orgName);
									showNotification(1,"设置成功");
								}else{
									showNotification(2,data.info);
								}
								//刷新部门树
								parent.leftFrame.initZtree();
							},"json");
                        window.top.layer.close(index);
                    }
                },cancel: function(){
                    //右上角关闭回调
                },success: function(layero,index){
                    var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
                    $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
                        window.top.layer.close(index);
                    })
                }
            });
		});
	})
</script>
</head>
<body onload="resizeVoteH('rightFrame')">
<div class="widget-body" id="contentBody" style="height: 480px">
	<div class="widget radius-bordered no-border" style="cursor: default;">
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue">修改部门</span>
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
								style="border-bottom: 0px !important;">${dep.parentName }</div>
						</div>
						<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-bottom-5 margin-left-5"
						   title="设定人员" id="depChooseBtn" style="float: left;"><i class="fa fa-plus"></i>部门选择</a>
					  </li>
					  <c:choose>
					  	<c:when test="${param.defDep eq 1 }">
							 <li class="ticket-item no-shadow ps-listline">
								<div class="pull-left gray ps-left-text">
									&nbsp;部门名称
								</div>
								<div class="ticket-user pull-left ps-right-box">
								<div class="no-border"
								style="width: 400px;border-bottom: 0px !important;">${dep.depName }</div>
								</div>
							  </li>
					  	</c:when>
					  	<c:otherwise>
							 <li class="ticket-item no-shadow ps-listline">
								<div class="pull-left gray ps-left-text">
									&nbsp;部门名称
									<span style="color: red">*</span>
								</div>
								<div class="ticket-user pull-left ps-right-box">
										<input id="depName" datatype="*" name="depName" nullmsg="请填部门名称" 
										class="colorpicker-default form-control" type="text" value="${dep.depName }"
										style="width: 400px;float: left">
								</div>
							  </li>
					  	</c:otherwise>
					  </c:choose>
					  
					  <li class="ticket-item no-shadow autoHeight no-padding">
						    <div class="pull-left gray ps-left-text padding-top-10">
						    	人员设定:
						    </div>
							<div class="ticket-user pull-left ps-right-box" style="height: auto;">
								<div class="pull-left gray ps-left-text padding-top-5">
									<div style="float: left; width: 250px;display:none;">
										<select id="depUserList"  multiple="multiple" moreselect="true">
											<c:choose>
												<c:when test="${not empty dep.listUser }">
													<c:forEach items="${dep.listUser}" var="obj" varStatus="vs">
														<option selected="selected" value="${obj.id }">${obj.userName}</option>
													</c:forEach>
												</c:when>
											</c:choose>
										</select>
									</div>
									<div id="depUserDiv" class="pull-left" style="max-width:460px">
										<c:choose>
											<c:when test="${not empty dep.listUser }">
												<c:forEach items="${dep.listUser}" var="obj" varStatus="vs">
													<span class="label label-default  margin-top-10 margin-left-5 margin-bottom-5 pull-left">${obj.userName}</span>
												</c:forEach>
											</c:when>
										</c:choose>
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
