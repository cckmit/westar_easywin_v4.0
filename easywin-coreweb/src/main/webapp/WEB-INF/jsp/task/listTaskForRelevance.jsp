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
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
    //打开页面body
    var openWindowDoc;
    //打开页面,可调用父页面script
    var openWindow;
    //打开页面的标签
    var openPageTag;
    //打开页面的标签
    var openTabIndex;
    //注入父页面信息
    function setWindow(winDoc,win){
        openWindowDoc = winDoc;
        openWindow = win;
        openPageTag = openWindow.pageTag;
        openTabIndex = openWindow.tabIndex;
    }
    //关闭窗口
    function closeWin(){
        var winIndex = window.top.layer.getFrameIndex(window.name);
        closeWindow(winIndex);
    }

$(function(){
	$(".searchForm").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError : true
	});
});
function formSub(){
	$(".searchForm").submit();
}
//状态筛选
function taskStateFilter(stateId){
	$("#searchForm").attr("action","/task/listTaskForRelevance?pager.pageSize=7&sid=${param.sid}");
	$("#state").val(stateId);
	$("#searchForm").submit();
}
//发起人筛选
function userOneForUserIdCallBack(userId,tag){
	$("#searchForm").attr("action","/task/listTaskForRelevance?pager.pageSize=7&sid=${param.sid}");
	$("#owner").val(userId);
	$("#searchForm").submit();
}
$(function(){
	//操作删除和复选框
	$('tr').click(function(){
		var radio = $(this).find("input[type=radio]");
		$(radio).attr("checked","checked")
	});

    //操作删除和复选框
    // $("body").on("dblclick","tr",function(){
    //     var radio = $(this).find("input[type=radio]");
    //     $(radio).attr("checked","checked")
    //
		// window.top.window[openWindow.name].taskForRelevanceBack(taskSelected());
		// closeWin();
    // });

	//名名称筛选
	$("#searchTaskName").blur(function(){
		$("#searchForm").attr("action","/task/listTaskForRelevance?pager.pageSize=7&sid=${param.sid}");
		$("#taskName").val($("#searchTaskName").val());
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#searchTaskName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#searchTaskName").val())){
        		var url="/task/listTaskForRelevance?pager.pageSize=7&sid=${param.sid}";
        		url+="&taskName="+$("#searchTaskName").val();
        		window.location.href=url;
        	}else{
        		showNotification(2,"请输入检索内容！");
    			$("#searchTaskName").focus();
        	}
        }
    });
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
});
//任务选择确认
function taskSelected(){
	var result;
	var radio = $("input[type=radio]:checked");
	if(radio.length==0){
		window.top.layer.alert("请选择任务",{icon:7});
	}else{
		var taskId = $(radio).attr("taskId"); 
		var taskName = $(radio).attr("taskName"); 
		var taskState = $(radio).attr("taskState"); 
		result={"id":taskId,"taskName":taskName,"state":taskState};
	}
	return result;
}

//窗体点击事件检测
document.onclick = function(e){
	var evt = e?e:window.event;
	var ele = $(evt.srcElement || evt.target);
	if ($(ele).parents("#moreCondition_Div").length == 1) { 
		if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
			return false;
		}else{
			if(!$("#moreCondition_Div").hasClass("open")){
				$(".searchCond").find(".open").removeClass("open");
				$("#moreCondition_Div").addClass("open");
			}
		}
	} else{
		$("#moreCondition_Div").removeClass("open");
	}
}
/**
 * 展示查询条件中更多
 */
function displayMoreCond(divId){
	if($("#"+divId).hasClass("open")){
		$("#"+divId).removeClass("open");
	}else{
		$("#"+divId).addClass("open")
	}
}
/**
 * 清空更多查询条件
 */
function resetMoreCon(divId){
	$("#"+divId).find("input").val('');
	
}
</script>
<style>
.panel-heading{
	padding: 5px 20px
}
.panel-body{
	padding: 0px;
}
.table{
margin-bottom:0px;
}
.table tbody>tr>td{
	padding: 5px 0px;
}
.pagination-lg >li>a{
font-size: small;
}
.pagination{
margin: 10px 0px
}
</style>
</head>
<body>
<div class="widget no-margin bg-white">
     	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
		<form action="/task/listTaskForRelevance?pager.pageSize=7" id="searchForm">
		 <input type="hidden" id="redirectPage" name="redirectPage"/>
		 <input type="hidden" name="sid" value="${param.sid}"/>
		 <input type="hidden" name="pager.pageSize" value="8"/>
		 <input type="hidden" id="state" name="state" value="${task.state}"/>
		 <input type="hidden" id="owner" name="owner" value="${task.owner}"/>
		 <input type="hidden" id="taskName" name="taskName" value="${task.taskName}"/>
								
			<div class="btn-group pull-left searchCond">
				<div class="table-toolbar ps-margin">
					<div class="pull-left padding-right-40" style="font-size: 18px">
		             	<span class="widget-caption themeprimary ps-layerTitle">任务列表</span>
					</div>
					
			     	<div class="btn-group">
						<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
							<c:choose>
								<c:when test="${task.state==1}"><font style="font-weight:bold;">进行中</font></c:when>
								<c:when test="${task.state==3}"><font style="font-weight:bold;">挂起中</font></c:when>
								<c:when test="${task.state==4}"><font style="font-weight:bold;">已完结</font></c:when>
								<c:otherwise>状态筛选</c:otherwise>
							</c:choose>
							<i class="fa fa-angle-down"></i>
						</a>
					 	<ul class="dropdown-menu dropdown-default">
			            	<li><a href="javascript:void(0)" onclick="taskStateFilter('')">清空条件</a></li>
							<li><a href="javascript:void(0)" onclick="taskStateFilter('1')">进行中</a></li>
							<li><a href="javascript:void(0)" onclick="taskStateFilter('3')">挂起中</a></li>
							<li><a href="javascript:void(0)" onclick="taskStateFilter('4')">已完结</a></li>
			            </ul>
			         </div>
			     </div>
			     <div class="table-toolbar ps-margin">
			         <div class="btn-group">
			             <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
			             	<c:choose>
								<c:when test="${not empty task.ownerName}">
									<font style="font-weight:bold;">${task.ownerName}</font>	
								</c:when>
								<c:otherwise>发起人筛选</c:otherwise>
							</c:choose>
			             	<i class="fa fa-angle-down"></i>
			            </a>
			             <ul class="dropdown-menu dropdown-default">
			                <li><a href="javascript:void(0)" onclick="userOneForUserIdCallBack('','')">清空条件</a></li>
						<li><a href="javascript:void(0)" onclick="userOneForUserId('${userInfo.id}','${userInfo.userName}','','${param.sid}','owner');">人员选择</a></li>
			              </ul>
                     </div>
                </div>
                
                <div class="table-toolbar ps-margin">
	                  <div class="btn-group cond" id="moreCondition_Div">
	                      <a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
	                      	<c:choose>
	                      			<c:when test="${not empty task.startDate || not empty task.endDate}">
	                      				<font style="font-weight:bold;">筛选中</font>
	                      			</c:when>
	                      			<c:otherwise>
	                         	更多
	                      			</c:otherwise>
	                      	</c:choose>
                              	<i class="fa fa-angle-down"></i>
                              </a>
                              <div class="dropdown-menu dropdown-default padding-bottom-10" 
                              style="min-width: 330px;">
							<div class="ps-margin ps-search padding-left-10">
								<span class="btn-xs">起止时间：</span>
							                              	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${task.startDate}" id="startDate" name="startDate" 
							placeholder="开始时间"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
							<span>~</span>
							<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${task.endDate}" id="endDate"  name="endDate"
							placeholder="结束时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})"/>
                      	</div>
                      	<div class="ps-clear padding-top-10" style="text-align: center;">
                      		<button type="submit" class="btn btn-primary btn-xs">查询</button>
                      		<button type="button" class="btn btn-default btn-xs margin-left-10" onclick="resetMoreCon('moreCondition_Div')">重置</button>
	                	</div>
	                      </div>
	                  </div>
	              </div>
			</div>
            <div class="ps-margin ps-search">
				<span class="input-icon">
				<input id="searchTaskName" name="searchTaskName" value="${task.taskName}" class="form-control ps-input" type="text" placeholder="请输入关键字">
				<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
				</span>
			</div>
		</form>
      </div>
      <div class="widget-body" id="contentBody">
         <table class="table table-striped table-hover general-table fixTable">
        	<thead>
           	<tr>
                <th style="width: 10%;" valign="middle">选项</th>
				<th valign="middle" class="hidden-phone">任务名称</th>
				<th style="width: 80px;" valign="middle">发起人</th>
				<th width="14%" valign="middle" style="text-align: center;">状态</th>
				<th width="14%" valign="middle">时间</th>
               </tr>
          </thead>
          <tbody>
          	<c:choose>
			 	<c:when test="${not empty listTask}">
			 		<c:forEach items="${listTask}" var="taskVo" varStatus="status" >
			           	<tr title="双击确认">
			           		<td style="text-align: center;">
					 			<label>
				           			<input class="colored-blue" type="radio" name="taskId" ${param.itemId==taskVo.id?'checked="checked"':'' }
				           			value="${taskVo.id}" taskId="${taskVo.id}" taskName="${taskVo.taskName}" taskState="${taskVo.state}"/>
									<span class="text">&nbsp;</span>
								</label>
			           		</td>
			           		<td title="${taskVo.taskName}">${taskVo.taskName}</td>
			           		<td>
			           			<div class="ticket-user pull-left other-user-box">
									<img src="/downLoad/userImg/${taskVo.comId}/${taskVo.owner}?sid=${param.sid}"
										title="${taskVo.ownerName}" class="user-avatar"/>
									<span class="user-name">${taskVo.ownerName}</span>
								</div>
			           		</td>
			           		<td style="text-align: center;">
			           			<c:choose>
			 						<c:when test="${taskVo.state==3}">已挂起</c:when>
			 						<c:when test="${taskVo.state==4}"><font style="color:#FF0000;">已完成</font></c:when>
			 						<c:otherwise>进行中</c:otherwise>
			 					</c:choose>
			           		</td>
			           		<td>
			           			${fn:substring(taskVo.recordCreateTime,0,10) }
			           		</td>
			             </tr>
			 		</c:forEach>
			 	</c:when>
			 	<c:otherwise>
			 		<tr>
			 			<td height="30" colspan="5" align="center"><h3>没有任务可选！</h3></td>
			 		</tr>
			 	</c:otherwise>
			 </c:choose>
      	</tbody>
  	</table> 
     <tags:pageBar url="/task/listTaskForRelevance"></tags:pageBar>
    </div>
</div>
<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
