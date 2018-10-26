<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" class="bg-white">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
//状态筛选
function itemStateFilter(stateId){
	$("#state").val(stateId);
	$("#searchForm").submit();
}
//责任人筛选
function userOneForUserIdCallBack(userId,tag){
	$("#owner").val(userId);
	$("#searchForm").submit();
}
$(function(){
	//操作删除和复选框
	$('tr').click(function(){
		var radio = $(this).find("input[type=radio]");
		$(radio).attr("checked","checked")
	});
	//名名称筛选
	$("#itemName").blur(function(){
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#itemName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#itemName").val())){
        		$("#searchForm").submit();
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#itemName").focus();
        	}
        }
    });
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
});
//选择项目出发
function itemSelected(){
	var result;
	var radio = $("input[type=radio]:checked");
	if(radio.length==0){
		window.top.layer.alert("请选择项目信息",{icon:7});
	}else{
		var itemId = $(radio).attr("itemId"); 
		var itemName = $(radio).attr("itemName"); 
		result={"itemId":itemId,"itemName":itemName};
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
<body>
<div class="widget no-margin bg-white">
     	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
			<form action="/item/listItemForRelevance" id="searchForm">
			  <input type="hidden" id="redirectPage" name="redirectPage"/>
			 <input type="hidden" name="sid" value="${param.sid}"/>
			 <input type="hidden" name="pager.pageSize" value="8"/>
			 <input type="hidden" id="state" name="state" value="${item.state}"/>
			 <input type="hidden" id="owner" name="owner" value="${item.owner}"/>
			 <input type="hidden" id="itemId" name="itemId" value="${param.itemId}"/>
								
             
			<div class="btn-group pull-left">
				<div class="table-toolbar ps-margin">
					<div class="pull-left padding-right-40" style="font-size: 18px">
		             	<span class="widget-caption themeprimary ps-layerTitle">项目列表</span>
					</div>
			     	<div class="btn-group">
						<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
							<c:choose>
							<c:when test="${item.state==1}"><font style="font-weight:bold;">进行中</font></c:when>
							<c:when test="${item.state==3}"><font style="font-weight:bold;">挂起中</font></c:when>
							<c:when test="${item.state==4}"><font style="font-weight:bold;">已完结</font></c:when>
							<c:otherwise>项目状态</c:otherwise>
						</c:choose>
							<i class="fa fa-angle-down"></i>
						</a>
					 	<ul class="dropdown-menu dropdown-default">
			            	<li><a href="javascript:void(0)" onclick="itemStateFilter('')">清空条件</a></li>
							<li><a href="javascript:void(0)" onclick="itemStateFilter('1')">进行中</a></li>
							<li><a href="javascript:void(0)" onclick="itemStateFilter('3')">挂起中</a></li>
							<li><a href="javascript:void(0)" onclick="itemStateFilter('4')">已完结</a></li>
			            </ul>
			         </div>
			     </div>
			     <div class="table-toolbar ps-margin searchCond">
			         <div class="btn-group">
			             <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
			             	<c:choose>
								<c:when test="${not empty item.ownerName}">
									<font style="font-weight:bold;">${item.ownerName}</font>	
								</c:when>
								<c:otherwise>责任人</c:otherwise>
							</c:choose>
			             	<i class="fa fa-angle-down"></i>
			            </a>
			             <ul class="dropdown-menu dropdown-default">
			                <li><a href="javascript:void(0)" onclick="userOneForUserIdCallBack('','')">清空条件</a></li>
							<li>
								<a href="javascript:void(0)" onclick="userOneForUserId('${userInfo.id}',
							'${userInfo.userName}','','${param.sid}','owner');">人员选择</a>
							</li>
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
	                      			<c:when test="${not empty item.startDate || not empty item.endDate}">
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
							                              	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${item.startDate}" id="startDate" name="startDate" 
							placeholder="开始时间"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
							<span>~</span>
							<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${item.endDate}" id="endDate"  name="endDate"
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
				<input id="itemName" name="itemName" value="${item.itemName}" class="form-control ps-input" type="text" placeholder="请输入关键字">
				<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
				</span>
			</div>
		</form>
      </div>
      <c:choose>
 		<c:when test="${not empty listItem}">
      <div class="widget-body" id="contentBody" style="overflow-y:auto;position: relative;width:100%;">
         <table class="table table-striped table-hover general-table">
        	<thead>
           		<tr>
                   <th style="width: 10%;" valign="middle">选项</th>
				<th valign="middle" class="hidden-phone">项目名称</th>
				<th style="width: 135px;" valign="middle">责任人</th>
				<th width="14%" valign="middle" style="text-align: center;">状态</th>
				<th width="14%" valign="middle">时间</th>
               </tr>
          </thead>
          <tbody>
			 		<c:forEach items="${listItem}" var="itemVo" varStatus="status" >
			           	<tr >
			           		<td style="text-align: center;">
						 			<label>
					           			<input class="colored-blue" type="radio" name="itemId" ${param.itemId==itemVo.id?'checked="checked"':'' }
					           			value="${itemVo.id}" itemId="${itemVo.id}" itemName="${itemVo.itemName}"/>
										<span class="text">&nbsp;</span>
									</label>
			           		</td>
			           		<td>
			           			<tags:cutString num="26">${itemVo.itemName}</tags:cutString>
			           		</td>
			           		<td>
				           		<div class="ticket-user pull-left other-user-box" style="height:35px"">
									<img class="user-avatar"  src="/downLoad/userImg/${itemVo.comId }/${itemVo.owner}?sid=${param.sid}" title="${itemVo.ownerName }"></img>
									<span class="user-name">${itemVo.ownerName}</span>
								</div>
		           		</td>
			           		<td style="text-align: center;">
			           			<c:choose>
			 						<c:when test="${itemVo.state==3}">已挂起</c:when>
			 						<c:when test="${itemVo.state==4}"><font style="color:#FF0000;">已完成</font></c:when>
			 						<c:otherwise>进行中</c:otherwise>
			 					</c:choose>
			           		</td>
			           		<td>
			           			${fn:substring(itemVo.recordCreateTime,0,10) }
			           		</td>
			             </tr>
			 		</c:forEach>
			 	</c:when>
			 	<c:otherwise>
			 		<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
						<section class="error-container text-center">
							<h1>
								<i class="fa fa-exclamation-triangle"></i>
							</h1>
							<div class="error-divider">
								<h2>您还没有自己的项目哦！</h2>
								<p class="description">协同提高效率，分享拉近距离。</p>
								<!-- <a href="javascript:void(0);" onclick="addItem();"
									class="return-btn"><i class="fa fa-plus"></i>新建项目</a> -->
							</div>
						</section>
					</div>
			 	</c:otherwise>
			 </c:choose>
      	</tbody>
  	</table> 
     <tags:pageBar url="/item/listItemForRelevance"></tags:pageBar>
    </div>
</div>
<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>

</body>
</html>
