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
function formSub(){
	$(".searchForm").submit();
}
//责任人筛选
function userOneForUserIdCallBack(userId,tag){
	$("#owner").val(userId);
	$("#searchForm").submit();
}
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	//操作删除和复选框
	$('tr').click(function(){
		var radio = $(this).find("input[type=radio]");
		$(radio).attr("checked","checked")
	});
	//名名称筛选
	$("#customerName").blur(function(){
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#customerName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#customerName").val())){
        		$("#searchForm").submit();
        	}else{
    			$("#customerName").focus();
        	}
        }
    });
});
//客户区域筛选
function artOpenerCallBack(idAndType){
	$("#areaIdAndType").val(idAndType);
	$("#searchForm").submit();
}
//返回值
function crmSelected(crmId,crmName){
	var result;
	var radio = $("input[type=radio]:checked");
	if(radio.length==0){
		window.top.layer.alert("请选择客户信息",{icon:7});
	}else{
		var crmId = $(radio).attr("crmId"); 
		var crmName = $(radio).attr("crmName"); 
		result={"crmId":crmId,"crmName":crmName};
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
<body style="background-color: #fff">
	<div class="widget no-margin">
     	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary" >
			<form action="/crm/listCrmForRelevance" id="searchForm">
			 <input type="hidden" id="redirectPage" name="redirectPage"/>
			 <input type="hidden" name="sid" value="${param.sid}"/>
			 <input type="hidden" name="pager.pageSize" value="8"/>
			 <input type="hidden" id="areaIdAndType" name="areaIdAndType" value="${customer.areaIdAndType}"/>
			 <input type="hidden" id="owner" name="owner" value="${customer.owner}"/>
			 <input type="hidden" id="crmId" name="crmId" value="${param.crmId}"/>
								
			<div class="btn-group pull-left searchCond">
				<div class="table-toolbar ps-margin">
			     	<div class="btn-group">
						<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
							<c:choose>
								<c:when test="${not empty customer.areaName}">
									<font style="font-weight:bold;">${customer.areaName}</font>	
								</c:when>
								<c:otherwise>区域筛选</c:otherwise>
							</c:choose>
							<i class="fa fa-angle-down"></i>
						</a>
					 	<ul class="dropdown-menu dropdown-default">
			            	<li><a href="javascript:void(0);"  onclick="artOpenerCallBack('')">清空条件</a></li>
						 	<li><a href="javascript:void(0);"  onclick="areaOne('areaIdAndType','areaName','${param.sid}','1')">区域选择</a></li>
			            </ul>
			         </div>
			     </div>
			     <div class="table-toolbar ps-margin">
			         <div class="btn-group">
			             <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
                            <c:choose>
								<c:when test="${not empty customer.ownerName}">
									<font style="font-weight:bold;">${customer.ownerName}</font>	
								</c:when>
								<c:otherwise>责任人</c:otherwise>
							</c:choose>
			             <i class="fa fa-angle-down"></i>
			             </a>
			             <ul class="dropdown-menu dropdown-default">
			                <li><a href="javascript:void(0);"  onclick="userOneForUserIdCallBack('','')">清空条件</a></li>
							<li><a href="javascript:void(0);"  onclick="userOneForUserId('${userInfo.id}','${userInfo.userName}','','${param.sid}','owner');">人员选择</a></li>
							<li class="divider"></li>
							<c:choose>
								<c:when test="${not empty listOwners}">
									<c:forEach items="${listOwners}" var="owner" varStatus="vs">
										<li><a href="javascript:void(0)"  onclick="userOneForUserIdCallBack('${owner.id}','');">${owner.userName}</a>
										</li>
									</c:forEach>
								</c:when>
							</c:choose>
			              </ul>
                     </div>
                </div>
                
                <div class="table-toolbar ps-margin">
                	<div class="btn-group cond" id="moreCondition_Div">
	                	<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
                          	<c:choose>
                          			<c:when test="${not empty customer.startDate || not empty customer.endDate}">
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
                                   	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${customer.startDate}" id="startDate" name="startDate" 
							placeholder="开始时间"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
							<span>~</span>
							<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${customer.endDate}" id="endDate"  name="endDate"
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
				<input id="customerName" name="customerName" value="${customer.customerName}" class="form-control ps-input" type="text" placeholder="请输入关键字">
				<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
				</span>
			</div>
		</form>
      </div>
      	<c:choose>
			 	<c:when test="${not empty listCustomer}">
      <div class="widget-body" id="contentBody" style="overflow-y:auto;position: relative;">
         <table class="table table-striped table-hover general-table">
        	<thead>
           	<tr>
                <th style="width: 10%;" valign="middle">选项</th>
				<th valign="middle" class="hidden-phone">客户名称</th>
				<th style="width: 120px;" valign="middle">责任人</th>
				<th width="20%" valign="middle" style="text-align: center;">区域</th>
				<th width="14%" valign="middle">时间</th>
               </tr>
          </thead>
          <tbody>
			 		<c:forEach items="${listCustomer}" var="crmVo" varStatus="status" >
			           	<tr>
			           		<td style="text-align: center;">
						 			<label>
					           			<input class="colored-blue" type="radio" name="crmId" ${param.crmId==crmVo.id?'checked="checked"':'' }
					           			value="${crmVo.id}" crmId="${crmVo.id}" crmName="${crmVo.customerName}"/>
										<span class="text">&nbsp;</span>
									</label>
			           		</td>
			           		<td>
			           			<tags:cutString num="26">${crmVo.customerName}</tags:cutString>
			           		</td>
			           		<td >
			           			<div class="ticket-user pull-left other-user-box" style="height:35px;padding-top: 2px !important;">
									<img class="user-avatar"  src="/downLoad/userImg/${crmVo.comId }/${crmVo.owner}?sid=${param.sid}" title="${crmVo.ownerName }"></img>
									<span class="user-name">${crmVo.ownerName }</span>
								</div>
			           		
			           		</td>
			           		<td style="text-align: center;" title="${crmVo.areaName }">
			           			<c:choose>
			           				<c:when test="${fn:length(crmVo.areaName)>6}">
					           			${fn:substring(crmVo.areaName,0,6)}...
			           				</c:when>
			           				<c:otherwise>
					           			${crmVo.areaName}
			           				</c:otherwise>
			           			</c:choose>
			           		</td>
			           		<td>
			           			${fn:substring(crmVo.recordCreateTime,0,10) }
			           		</td>
			             </tr>
			 		</c:forEach>
			 	</c:when>
			 	<c:otherwise>
			 		<div class="widget-body" style="height:550px; text-align:center;padding-top:155px">
						<section class="error-container text-center">
							<h1>
								<i class="fa fa-exclamation-triangle"></i>
							</h1>
							<div class="error-divider">
								<h2>您还没有相关的客户数据！</h2>
								<p class="description">协同提高效率，分享拉近距离。</p>
								<!-- <a href="javascript:void(0);" onclick="addCustomer();"
									class="return-btn"><i class="fa fa-plus"></i>新建客户</a> -->
							</div>
						</section>
					</div>
			 	</c:otherwise>
			 </c:choose>
      	</tbody>
  	</table> 
     <tags:pageBar url="/crm/listCrmForRelevance"></tags:pageBar>
    </div>
</div>
    <script src="/static/assets/js/bootstrap.min.js"></script>
    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
