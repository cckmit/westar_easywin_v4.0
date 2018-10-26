<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>
<link rel="stylesheet" href="/static/css/financial.css">
<style type="text/css">
td，th{
white-space: nowrap;
text-overflow: ellipsis;
overflow: hidden;
}
a{
color: #2a6496;
}
</style>

</head>
<body onload="resizeVoteH('otherAttrIframe');">
<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
	<div class="checkbox ps-margin pull-left">
		<label> <input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'ids')"> <span class="text" style="color: inherit;">全选</span>
		</label>
	</div>
	<div>
	<form action="/financial/listLoansPage" class="subform" id="searchForm">
		<input type="hidden" name="searchTab" id="searchTab" value="${empty param.searchTab?'11':param.searchTab}">
		<input type="hidden" name="sid" value="${param.sid}"> 
		<input type="hidden" id="pageSize" name="pager.pageSize" value="10"> 
		<input type="hidden" name="isBusinessTrip" id="isBusinessTrip" value="${loan.isBusinessTrip}">
		<input type="hidden" name="sendNotice" id="sendNotice" value="${loan.sendNotice}">
		<input type="hidden" name="balanceState" id="balanceState" value="${loan.balanceState}">
		
			<div class="btn-group pull-left searchCond">
				<div class="table-toolbar ps-margin">
					<div class="btn-group">
						<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="申请人筛选">
							申请人筛选
							<i class="fa fa-angle-down"></i>
						</a>
						<ul class="dropdown-menu dropdown-default">
							<li>
								<a href="javascript:void(0)" class="clearMoreValue" relateList="applyUser_select">不限条件</a>
							</li>
							<li>
								<a href="javascript:void(0)" class="userMoreSelect" relateList="applyUser_select">人员选择</a>
							</li>
						</ul>
					</div>
				</div>
				<div class="table-toolbar ps-margin">
					<div class="btn-group">
						<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="借款类型">
							<c:choose>
                                <c:when test="${not empty loan.isBusinessTrip}">
                                    <font style="font-weight:bold;">
                                        <c:choose>
                                            <c:when test="${loan.isBusinessTrip==1}">出差借款</c:when>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${loan.isBusinessTrip==0}">一般借款</c:when>
                                        </c:choose>
                                    </font>
                                </c:when>
                                <c:otherwise>借款类型</c:otherwise>
                            </c:choose> 
							<i class="fa fa-angle-down"></i>
						</a>
						<ul class="dropdown-menu dropdown-default">
							<li>
								<a href="javascript:void(0)"  class="clearValue" relateElement="isBusinessTrip" >不限条件</a>
							</li>
							<li>
								<a href="javascript:void(0)" class="setValue" relateElement="isBusinessTrip" dataValue="1">出差借款</a>
							</li>
							<li>
								<a href="javascript:void(0)" class="setValue" relateElement="isBusinessTrip" dataValue="0">一般借款</a>
							</li>
						</ul>
					</div>
				</div>
				<div class="table-toolbar ps-margin">
					<div class="btn-group">
						<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="通知状态">
							<c:choose>
                                <c:when test="${not empty loan.sendNotice}">
                                    <font style="font-weight:bold;">
                                        <c:choose>
                                            <c:when test="${loan.sendNotice==1}">已通知</c:when>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${loan.sendNotice==0}">未通知</c:when>
                                        </c:choose>
                                    </font>
                                </c:when>
                                <c:otherwise>通知状态</c:otherwise>
                            </c:choose> 
							<i class="fa fa-angle-down"></i>
						</a>
						<ul class="dropdown-menu dropdown-default">
							<li>
								<a href="javascript:void(0)"  class="clearValue" relateElement="sendNotice" >不限条件</a>
							</li>
							<li>
								<a href="javascript:void(0)" class="setValue" relateElement="sendNotice" dataValue="1">已通知</a>
							</li>
							<li>
								<a href="javascript:void(0)" class="setValue" relateElement="sendNotice" dataValue="0">未通知</a>
							</li>
						</ul>
					</div>
				</div>
				<div class="table-toolbar ps-margin">
					<div class="btn-group">
						<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="借款状态">
							<c:choose>
                                <c:when test="${not empty loan.balanceState}">
                                    <font style="font-weight:bold;">
                                    	<c:choose>
                                            <c:when test="${loan.balanceState==2}">审批中</c:when>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${loan.balanceState==1}">已结算</c:when>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${loan.balanceState==0}">待结算</c:when>
                                        </c:choose>
                                         <c:choose>
                                            <c:when test="${loan.balanceState==3}">待办理</c:when>
                                        </c:choose>
                                    </font>
                                </c:when>
                                <c:otherwise>借款状态</c:otherwise>
                            </c:choose> 
							<i class="fa fa-angle-down"></i>
						</a>
						<ul class="dropdown-menu dropdown-default">
							<li>
								<a href="javascript:void(0)"  class="clearValue" relateElement="balanceState" >不限条件</a>
							</li>
							<li>
								<a href="javascript:void(0)" class="setValue" relateElement="balanceState" dataValue="2">审批中</a>
							</li>
							<li>
								<a href="javascript:void(0)" class="setValue" relateElement="balanceState" dataValue="0">待结算</a>
							</li>
							<li>
								<a href="javascript:void(0)" class="setValue" relateElement="balanceState" dataValue="1">已结算</a>
							</li>
							<li>
								<a href="javascript:void(0)" class="setValue" relateElement="balanceState" dataValue="3">待办理</a>
							</li>
						</ul>
					</div>
				</div>
				<div class="table-toolbar ps-margin">
					<div class="btn-group cond" id="moreCondition_Div">
						<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
							<c:choose>
								<c:when test="${not empty loan.startDate || not empty loan.endDate}">
									<font style="font-weight:bold;">筛选中</font>
								</c:when>
								<c:otherwise>
									更多
								</c:otherwise>
							</c:choose>
							<i class="fa fa-angle-down"></i>
						</a>
						<div class="dropdown-menu dropdown-default padding-bottom-10 subMore" style="min-width: 330px;">
							<div class="ps-margin ps-search padding-left-10">
								<span class="btn-xs">借款时间：</span>
								<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="startDate" name="startDate" placeholder="开始时间"
									onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" value="${loan.startDate}"/>
								<span>~</span>
								<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="endDate" name="endDate" placeholder="结束时间"
									onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" value="${loanOff.endDate}"/>
							</div>
							<div class="ps-clear padding-top-10" style="text-align: center;">
								<button type="button" class="btn btn-primary btn-xs moreSearchBtn noMoreShow">查询</button>
								<button type="button" class="btn btn-default btn-xs margin-left-10 moreClearBtn noMoreShow">重置</button>
							</div>
						</div>
					</div>
				</div>
				
		<div class="ps-margin ps-search searchCond">
			<span class="input-icon">
				<input name="flowName" class="form-control ps-input moreSearch" type="text" placeholder="请输入关键字" value="${loan.flowName }">
				<a href="javascript:void(0)" class="ps-searchBtn">
					<i class="glyphicon glyphicon-search circular danger"></i>
				</a>
			</span>
		</div>
			
		</div>
		<div class="batchOpt" style="display: none">
			<div class="btn-group pull-left">
				<div class="table-toolbar ps-margin">
					<div class="btn-group">
						<button class="btn btn-info btn-primary btn-xs" type="button"  id="addNotices">
							批量通知
						</button>
						<button class="btn btn-info btn-primary btn-xs margin-left-10" type="button"  id="addBalances">
							批量结算
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="ps-clear" id="formTempData">
			<select list="listCreator" listkey="id" listvalue="userName" id="applyUser_select" 
				multiple="multiple" moreselect="true" style="display: none">
				<c:forEach items="${loan.listCreator }" var="user" varStatus="">
					<option value="${user.id}">${user.userName }</option>
				</c:forEach>
			</select>
		</div>
		<!-- 筛选条件显示 -->
		<div class="padding-top-5 padding-bottom-5 text-left moreUserListShow" style="display:${fn:length(loan.listCreator)>0?'block':'none'}" id="applyUser_selectDiv">
			<strong>申请人:</strong>
				<c:forEach items="${loan.listCreator }" var="user" varStatus="">
					<span class="label label-default margin-right-5 margin-bottom-5" data-userId="${user.id}"
					relateList="applyUser_select" title="双击移除" style="cursor: pointer;">${user.userName}</span>
				</c:forEach>
		</div>
	</form>
	</div>
</div>
<c:choose>
	<c:when test="${not empty listLoans}">
	 <div class="wrapper" style="min-height: 135px;background-color: #FFFFFF;">
       <div class="content-wrapper">
		 <div class="content-list">
		 <form id="delForm">
		 <input type="hidden" name="redirectPage"> 
		 <input type="hidden" name="sid" value="${param.sid }">
		 <input type="hidden" name="type" value="0">
		 <c:forEach items="${listLoans}" var="obj" varStatus="vs">
			<div class="content-list-item" onmouseover="showCheck('${obj.id}')" onmouseout="hideCheck('${obj.id}')">
                    <c:choose>
						<c:when test="${obj.spState == 1 && obj.flowState == 4 && obj.balanceState != 1}">
							<div class="description" style="width: 2%" >
		                    	<label class="optCheckBox" style="display: none;width: 20px;margin-top: 15px;" id="ch_${obj.id}"> <input class="colored-blue" id="box_${obj.id}" type="checkbox" name="ids"
										value="${obj.id}" /><span class="text"></span>
								</label><label class="optRowNum" style="display: block;width: 20px;margin-top: 15px;" id="op_${obj.id}">${vs.count}</label>
		                    </div>
						</c:when>
						<c:otherwise>
							<div class="description" style="width: 2%">
								<label class="optRowNum" style="display: block;width: 20px;margin-top: 15px;">${vs.count}</label>
							</div>
						</c:otherwise>
					</c:choose>
                    <img src="/downLoad/userImg/${userInfo.comId}/${obj.creator}?sid=${param.sid}"class="avatar" title="${obj.creatorName }">
                    <div class="description" style="width: 40%">
                        <p class="type">${obj.isBusinessTrip == 1?"出差借款":"一般借款" }</p>
                        <p><a href="javascript:void(0)" class="spFlowIns" insId="${obj.instanceId }" style="color: #2a6496" title="${obj.flowName }">${obj.flowName }</a></p>
                    </div>
                    <c:choose>
						<c:when test="${obj.spState == 1 && obj.flowState == 4 && obj.balanceState != 1}">
							 <div class="status pass">
		                     	通过
		                    </div>
						</c:when>
						<c:when test="${obj.balanceState == 1}">
							<div class="status">
		                    	--
		                    </div>
						</c:when>
						<c:otherwise>
							<div class="status">
		                    	审批人：
		                        <p>${obj.executorName }</p>
		                    </div>
						</c:otherwise>
					</c:choose>
                    <c:choose>
                    	<c:when test="${obj.balanceState == 1}">
                   			 <div class="progress p-ok" style="background-color: #FFFFFF;margin-bottom: 0;">
		                        <div class="bar-box">
		                            <div class="bar"></div>
		                        </div>
		                        <div class="icon">✔</div>
		                    </div>
                   		</c:when>
                   		<c:when test="${obj.spState == 1 && obj.flowState == 4 && obj.balanceState != 1}">
                   			<div class="progress" style="background-color: #FFFFFF;margin-bottom: 0;">
		                        <div class="bar-box">
		                            <div class="bar" style="width: 90%"></div>
		                        </div><span>90%</span>
		                    </div>
                   		</c:when>
                   		<c:otherwise>
                   			<div class="progress" style="background-color: #FFFFFF;margin-bottom: 0;">
		                        <div class="bar-box">
		                            <div class="bar" style="width: 30%"></div>
		                        </div><span>30%</span>
		                    </div>
                   		</c:otherwise>
                   	</c:choose>
                    <c:choose>
                    	<c:when test="${obj.balanceState == 1}">
                   			<div class="operation">
		                     	 <span style="color: red;">已结算</span>
		                  	</div>
                   		</c:when>
                   		<c:when test="${obj.spState == 1 && obj.flowState == 4 && obj.balanceState != 1}">
                   			 <div class="operation">
		                     	  <a href="javascript:void(0)" onclick="addNotice('${obj.id}','${obj.borrowingBalance}','${obj.creatorName }','${obj.creator }','0','${obj.id }','${obj.flowName }','${obj.instanceId }')" >
			                     	  <c:choose>
			                     	  	<c:when test="${obj.sendNotice == 1}">
			                     	  		再次通知
			                     	  	</c:when>
			                     	  	<c:otherwise>
			                     	  		领款通知
			                     	  	</c:otherwise>
			                     	  </c:choose>
		                     	  </a>&nbsp;|
								  <a href="javascript:void(0)" onclick="addBanlance('${obj.id}','${obj.borrowingBalance}','${obj.creatorName }','${obj.creator }','030','${obj.id }','${obj.instanceId }')" >完成结算</a>
		                  	</div>
                   		</c:when>
                   		<c:otherwise>
                   			<div class="operation">
		                     	  <a href="javascript:void(0)" class="spFlowIns" insId="${obj.instanceId }">审批</a>
		                  	</div>
                   		</c:otherwise>
                   	</c:choose>
               </div>
              </c:forEach>
              </form>
			</div>
		</div>
		<tags:pageBar url="/financial/listLoansPage"></tags:pageBar>
	  </div>
	</c:when>
	<c:otherwise>
		<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
			<section class="error-container text-center">
				<h1>
					<i class="fa fa-exclamation-triangle"></i>
				</h1>
				<div class="error-divider">
					<h2>暂无相关借款审核信息！</h2>
					<p class="description">协同提高效率，分享拉近距离。</p>
				</div>
			</section>
		</div>
	</c:otherwise>
</c:choose>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>		
</body>
<script type="text/javascript">
var sid = '${param.sid}'
$(function(){
	resizeVoteH('otherAttrIframe');
	parent.queryCounts();
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsgV2(msg, o, cssctl);
		}
	}); 
	
	//批量通知
	$("#addNotices").click(function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定批量通知吗？',{icon:3,title:'询问框'}, function(index){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').attr("action","/balance/addMoreNotices");
				$('#delForm').submit();
			}, function(){
			});	
		}else{
			window.top.layer.alert('请先选择通知的记录。',{title:false,closeBtn:0,icon:7,btn:['关闭']});
		}
	});
	
	//批量完成借款
	$("#addBalances").click(function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定批量结算吗？',{icon:3,title:'询问框'}, function(index){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').attr("action","/balance/addBalances");
				$('#delForm').submit();
			}, function(){
			});	
		}else{
			window.top.layer.alert('请先选择结算的记录。',{title:false,closeBtn:0,icon:7,btn:['关闭']});
		}
	});
	
	
	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#searchForm").find("input[name='"+relateElement+"']").val('');
		
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		$("#searchForm").submit();
	})
	
	$("#moreCondition_Div").bind("hideMoreDiv",function(){
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		
		if(startDate || endDate){
			var _font = $('<font style="font-weight:bold;"></font>')
			$(_font).html("筛选中")
			var _i = $('<i class="fa fa-angle-down"></i>')
			
			$(this).find("a").html(_font);
			$(this).find("a").append(_i);
		}else{
			var _i = $('<i class="fa fa-angle-down"></i>')
			$(this).find("a").html("更多");
			$(this).find("a").append(_i);
		}
	})
	//清空条件
	$("body").on("click",".clearMoreValue",function(){
		var relateList = $(this).attr("relateList");
		$("#searchForm").find("#"+relateList).html('');
		$("#"+relateList+"Div").find("span").remove();
		$("#"+relateList+"Div").hide();
		
		$("#searchForm").submit();
	})
	//单个赋值
	$("body").on("click",".setValue",function(){
		var relateElement = $(this).attr("relateElement");
		var dataValue = $(this).attr("dataValue");
		$("#searchForm").find("input[name='"+relateElement+"']").val(dataValue);
		
		var _font = $('<font style="font-weight:bold;"></font>')
		$(_font).html($(this).html())
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html(_font);
		$(this).parents("ul").prev().append(_i);
		
		
		$("#searchForm").submit();
	})
	//人员多选
	$("body").on("click",".userMoreSelect",function(){
		var relateList = $(this).attr("relateList");
		userMore(relateList, null, sid, null, null, function(options){
			$("#formTempData").find("#"+relateList).html('');
			$("#"+relateList+"Div").find("span").remove();
			if(options && options.length>0){
				$("#"+relateList+"Div").show()
				$.each(options,function(optIndex,option){
					var _option = $("<option selected='selected'></option>")
					$(_option).val($(option).val());
					$(_option).html($(option).text());
					$("#"+relateList).append(_option);
					
					var _span = $("<span></span>");
					$(_span).html($(option).text())
					$(_span).addClass("label label-default margin-right-5 margin-bottom-5");
					$(_span).attr("title","双击移除");
					$(_span).css("cursor","pointer");
					$("#"+relateList+"Div").append(_span);
					
					$(_span).data("userId",$(option).val());
					$(_span).data("relateList",relateList);
					
				})
			}else{
				$("#"+relateList+"Div").hide();
			}
			$("#searchForm").submit();
		})
	});
	//双击移除
	$("body").on("dblclick",".moreUserListShow span",function(){
		var userId = $(this).data("userId");
		var relateList = $(this).data("relateList");
		if(!userId){
			userId = $(this).attr("data-userId");
			relateList = $(this).attr("relateList");
		}
		$("#"+relateList).find("option[value='"+userId+"']").remove();
		if($(this).parents(".moreUserListShow").find("span").length<=1){
			$(this).parents(".moreUserListShow").hide();
		}
		$(this).remove();
		$("#searchForm").submit();
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		$("#searchForm").submit();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		$("#searchForm").submit();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		$("#searchForm").submit();
	})
	
	//查看审批
	$("body").on("click",".spFlowIns",function(){
		var insId = $(this).attr("insId");
		if(insId){
			viewSpFlow(insId);
		}
	})
	
})


//流程查看页面
function viewSpFlow(instanceId){
	var url = "/workFlow/viewSpFlow?sid="+'${param.sid}'+"&instanceId="+instanceId;
	parent.openWinByRight(url)
}



//选择完成借款页面
function addBanlance(sid,amount,creatorName,creator,type,busId,instanceId){
	//是否需要数据保持
	window.top.layer.open({
		 type: 2,
		 title:false,
		 closeBtn:0,
		 area: ['550px', '425px'],
		 fix: true, //不固定
		 maxmin: false,
		 move: false,
		 scrollbar:false,
		 content: ['/balance/banlancePage?sid='+'${param.sid}'+"&amount="+amount+"&creatorName="+creatorName+"&creator="+creator+"&type="+type+"&busId="+busId+"&instanceId="+instanceId,'no'],
		 btn:['确定','关闭'],
		 yes: function(index, layero){
			 var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			 var payType = $(iframeWin.document).find("input[name='payType']:checked").val();
			 $.post("/balance/addBanlance?sid=${param.sid}&random="+Math.random(),{Action:"post",type:type,busId:busId,instanceId:instanceId,payType:payType},     
				   function (msgObjs){
					if(msgObjs.status == "y"){
						showNotification(1,msgObjs.msg);
						 window.top.layer.close(index);
						 window.self.location.reload();
					}else{
						showNotification(2,msgObjs.msg);
					}
				},"json");
			 
		 }
		,btn2: function(){
			 
		}
	    ,cancel: function(){ 
	    	//右上角关闭回调
	    },success:function(layero,index){
		  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.setWindow(window.document,window);
		  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			  window.top.layer.close(index);
		  });
		 
	    }
	});
}

//领款通知页面
function addNotice(sid,amount,creatorName,creator,type,busId,applyName,instanceId){
	//是否需要数据保持
	window.top.layer.open({
		 type: 2,
		 title:false,
		 closeBtn:0,
		 area: ['600px', '400px'],
		 fix: true, //不固定
		 maxmin: false,
		 move: false,
		 scrollbar:false,
		 content: ['/balance/noticePage?sid='+'${param.sid}'+"&amount="+amount+"&creatorName="+creatorName+"&creator="+creator+"&type="+type+"&applyName="+applyName,'no'],
		 btn:['确定','关闭'],
		 yes: function(index, layero){
			 var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			 var checked = $(iframeWin.document).find("input[name='usePhone']:checked").val();
			 var content = $(iframeWin.document).find("#content").val();
			 var usePhone = "";
			 if(checked){
				 usePhone = checked;
			 }
			 iframeWin.formSub();
			 if(content){
				 $.post("/balance/addNotice?sid=${param.sid}&random="+Math.random(),{Action:"post",content:content,type:type,proposer:creator,busId:busId,usePhone:usePhone,instanceId:instanceId},     
					   function (msgObjs){
						if(msgObjs.status == "y"){
							showNotification(1,msgObjs.msg);
							 window.top.layer.close(index);
							 window.self.location.reload();
						}else{
							showNotification(2,msgObjs.msg);
						}
					},"json"); 
			 }
			
		 }
		,btn2: function(){
			 
		}
	    ,cancel: function(){ 
	    	//右上角关闭回调
	    },success:function(layero,index){
		  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.setWindow(window.document,window);
		  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			  window.top.layer.close(index);
		  });
		 
	    }
	});
}

//显示多选框
function showCheck(id) {
	$("#ch_"+id).show();
	$("#op_"+id).hide();
}

//隐藏多选框
function hideCheck(id) {
	if(!$("#box_"+id).prop("checked")){
		$("#ch_"+id).hide();
		$("#op_"+id).show();
	}
}




</script>
</html>

