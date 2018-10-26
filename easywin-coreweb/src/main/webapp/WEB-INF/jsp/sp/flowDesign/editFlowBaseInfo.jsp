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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" charset="utf-8"
	src="/static/js/sp_center/sp_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
var sid="${param.sid}";//全局变量
$(function(){
	//关联表单
	$("#formName").click(function(){
		formModListForSelect("${flowConfig.formKey}");//弹窗打开表单候选列表页面
	});
	//部门选择
	$("#dep").click(function(){
		window.top.layer.open({
		  type: 2,
		  //title: ["部门多选", "font-size:18px;"],
		  title:false,
		  closeBtn:0,
		  area: ["400px", "450px"],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/common/depMoreSelect/orgTreePage?sid="+sid,'no'],
		  btn: ["确定","取消"],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  var deps = iframeWin.returnOrg();
			  if(deps){
				  for(var i=0;i<deps.length;i++){
					    var rows = $("#scopeByDepTable tr").length+1;
					    var same = false;
					    $("#scopeByDepTable [name='depIds']").each(function(){
					        if($(this).val()==deps[i].id){
					        	same = true;
					        }
					      });
					    if(!same){
					    	var row="<tr>";
							row = row+"<td style=\"text-align:center;width:5%;\">["+rows+"]</td>";
							row = row+"<td style=\"padding-left:20px;\">"+deps[i].name;
							row = row+"<input type=\"hidden\" name=\"depIds\" value=\""+deps[i].id+"\"/>";
							row = row+"</td>";
							row = row+"<td style=\"text-align:center;width:10%;\"><a href=\"javascript:void(0);\" onclick=\"delScopeByDep(this)\">删除</a></td>";
							row = row+"</tr>";
							$("#scopeByDepTable").append(row);
					    }
						var defaultH = 50;//默认高度
						$("#scopeByDepTableLi").css("height",(rows*30+defaultH)+"px");//根据候选步骤个数设置候选步骤li高度
						resizeVoteH('otherFlowModelAttrIframe');//设置外层iframe高度
				  }
				  updateFlowAttr("scopeByDep");//根据部门设置流程范围
			  }
			  window.top.layer.close(index)
		  },success:function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
		});
	});
	//人员选择
	$("#user").click(function(){
		window.top.layer.open({
		  type: 2,
		  //title: ["人员多选", "font-size:18px;"],
		  title:false,
		  closeBtn:0,
		  area: ["750px", "530px"],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/common/userMorePage?sid="+sid,'no'],
		  btn: ["确定","取消"],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  var users = iframeWin.returnUser();
			  if(users){
				  for(var i=0;i<users.length;i++){
					  var rows = $("#scopeByUserTable tr").length+1;
					  var same = false;
					    $("#scopeByUserTable [name='userIds']").each(function(){
					        if($(this).val()==users[i].value){
					        	same = true;
					        }
					      });
					    if(!same){
					    	var row="<tr>";
							row = row+"<td style=\"text-align:center;width:5%;\">["+rows+"]</td>";
							row = row+"<td style=\"padding-left:20px;\">"+users[i].text;
							row = row+"<input type=\"hidden\" name=\"userIds\" value=\""+users[i].value+"\"/>";
							row = row+"</td>";
							row = row+"<td style=\"text-align:center;width:10%;\"><a href=\"javascript:void(0);\" onclick=\"delScopeByUser(this)\">删除</a></td>";
							row = row+"</tr>";
							$("#scopeByUserTable").append(row);
					    }
						var defaultH = 50;//默认高度
						$("#scopeByUserTableLi").css("height",(rows*30+defaultH)+"px");//根据候选步骤个数设置候选步骤li高度
						resizeVoteH('otherFlowModelAttrIframe');//设置外层iframe高度
				  }
				  updateFlowAttr("scopeByUser");//根据人员设置流程范围
			  }
			  window.top.layer.close(index)
		  }
		});
	});
});
//删除部门范围
function delScopeByDep(obj){
	window.top.layer.confirm("确定要删除？", {icon: 3, title:"确认对话框"}, function(index){
      //$(obj).parent().parent().remove(); 
	  
	  var tab=scopeByDepTable;
	  var tr=obj.parentNode.parentNode;
	  var no=tr.rowIndex;
	  tab.deleteRow(tr.rowIndex);
	  var defaultH = 50;//默认高度
	 
	  //更新后面的序号
	  for(var i=no;i<tab.rows.length;i++)
	  {
	  	tab.rows[i].cells[0].innerText="["+(tab.rows[i].rowIndex+1)+"]";
	  	$(tab.rows[i]).find("[name='conditionNum']").val("["+tab.rows[i].rowIndex+1+"]");
	  }
	  var rows = $("#scopeByDepTable tr").length;
	  $("#scopeByDepTableLi").css("height",(rows*30+defaultH)+"px");//根据候选步骤个数设置候选步骤li高度
	  updateFlowAttr("scopeByDep");//根据部门设置流程范围
	  window.top.layer.close(index);
	});
}
//删除人员范围
function delScopeByUser(obj){
	window.top.layer.confirm("确定要删除？", {icon: 3, title:"确认对话框"}, function(index){
	  
	  var tab=scopeByUserTable;
	  var tr=obj.parentNode.parentNode;
	  var no=tr.rowIndex;
	  tab.deleteRow(tr.rowIndex);
	  var defaultH = 50;//默认高度
	 
	  //更新后面的序号
	  for(var i=no;i<tab.rows.length;i++)
	  {
	  	tab.rows[i].cells[0].innerText="["+(tab.rows[i].rowIndex+1)+"]";
	  	$(tab.rows[i]).find("[name='conditionNum']").val("["+tab.rows[i].rowIndex+1+"]");
	  }
	  var rows = $("#scopeByUserTable tr").length;
	  $("#scopeByUserTableLi").css("height",(rows*30+defaultH)+"px");//根据候选步骤个数设置候选步骤li高度
	  updateFlowAttr("scopeByUser");//根据部门设置流程范围
	  window.top.layer.close(index);
	});
}
//表单选择后回调
function formModSelectedReturn(formModId,formModName){
	if(!strIsNull(formModId) && !strIsNull(formModName)){
		$("#formName").val(formModName);
		$("#formKey").val(formModId);
		updateFlowAttr("formKey");
	}else{
		window.top.layer.msg("请选择\"表单关联失败\"！formName="+formName+" & formKey="+formKey,{icon:2});
	}
}
$(document).ready(function() {
    var rows = $("#scopeByDepTable tr").length;
    $("#scopeByDepTableLi").css("height",(rows==0?80:(rows*30+50))+"px");//根据候选步骤个数设置候选步骤li高度
    var rows = $("#scopeByUserTable tr").length;
    $("#scopeByUserTableLi").css("height",(rows==0?80:(rows*30+50))+"px");//根据候选步骤个数设置候选步骤li高度
	resizeVoteH('otherFlowModelAttrIframe');
});

//清除关联关系
function delFlowModelRelevance(){
	window.top.layer.confirm("确定清除关联关系？", {icon: 3, title:"确认对话框"}, function(index){
		$("#editFlowForm").attr("action","/flowDesign/delFlowModelRelevance");
		$("#editFlowForm").submit();
	  	window.top.layer.close(index);
	});
}
</script>
</head>
<body>
	<div class="row">
		<div class="col-md-12 col-xs-12 ">
			<div class="container" style="padding: 0px 0px;width: 100%">
				<div class="row" style="margin: 0 0">
					<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
						<div class="widget" style="margin-top: 0px;">
							<div class="widget-main ">
								<div class="tabbable">
									<div class="tab-content">
										<div class="widget radius-bordered">
											<div class="no-shadow">
												<div class="tickets-container bg-white">
													<ul class="tickets-list">
														<form action="/flowDesign/updateFlowAttr" id="editFlowForm">
															<input type="hidden" name="pflowId" id="pflowId" value="${flowConfig.id}">
															<input type="hidden" name="sid" value="${param.sid}">
															<input type="hidden" name="activityMenu"
																value="${activityMenu}" /> <input type="hidden"
																name="attrType" /> <input type="hidden" name="id"
																value="${flowConfig.id}" />
															<li class="ticket-item no-shadow ps-listline">
																<div class="pull-left gray ps-left-text">
																	&nbsp;流程名称</div>
																<div class="ticket-user pull-left ps-right-box">
																	<input id="flowName" datatype="input,sn"
																		defaultLen="52" name="flowName" nullmsg="请填写流程名称"
																		class="colorpicker-default form-control" type="text"
																		title="${flowConfig.flowName}"
																		value="${flowConfig.flowName}"
																		onpropertychange="handleName()" onkeyup="handleName()"
																		style="width: 400px;float: left"
																		onchange="updateFlowAttr('flowName');"> <span
																		id="msgTitle" style="float:left;width: auto;">(0/26)</span>
																</div> 
																	<script>
																		//当状态改变的时候执行的函数 
																		function handleName() {
																				var value = $('#flowName').val();
																				var len = charLength(value.replace(/\s+/g,""));
																				if (len % 2 == 1) {
																					len = (len + 1) / 2;
																				} else {
																					len = len / 2;
																				}
																				if (len > 26) {
																					$('#msgTitle').html("(<font color='red'>"+len+"</font>/26)");
																				} else {
																					$('#msgTitle').html("("+ len+ "/26)");
																				}
																			}
																			//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
																			if (/msie/i.test(navigator.userAgent)) { //ie浏览器 
																				document.getElementById('flowName').onpropertychange = handleName
																			} else {//非ie浏览器，比如Firefox 
																				document.getElementById('flowName').addEventListener("input",handleName,false);
																			}
																	</script>
															</li>
															<li class="ticket-item no-shadow ps-listline">
																<div class="pull-left gray ps-left-text">
																	&nbsp;命名规则</div>
																<div class="ticket-user pull-left ps-right-box">
																	<input id="titleRule" datatype="input,sn"
																		defaultLen="52" name="titleRule" nullmsg="请填写命名规则"
																		class="colorpicker-default form-control" type="text"
																		title="${flowConfig.titleRule}"
																		value="${flowConfig.titleRule}"
																		style="width: 400px;float: left"
																		onchange="updateFlowAttr('titleRule');"> 
																</div> 
																<div class="ps-clear"> 
																说明: {user}:用户、{dept}:部门、{year}:年、{month}:月、{day}:日、{yearweek}:该年份的周数、{premonth}:上月、{nextmonth}:下月
																</div>
															</li>
															
															<li class="ticket-item no-shadow ps-listline"
																style="clear:both">
																<div class="pull-left gray ps-left-text">流程模块</div>
																<div class="ticket-user pull-left ps-right-box">
																	<c:choose>
																		<c:when  test="${flowConfig.flowModBusType eq '046' }">会议模块</c:when>
																		<c:when  test="${flowConfig.flowModBusType eq '047' }">会议纪要</c:when>
																		<c:when  test="${flowConfig.flowModBusType eq '022'}">审批模块</c:when>
																		<c:when  test="${flowConfig.flowModBusType eq '031' }" >公告模块</c:when>
																		<c:when  test="${flowConfig.flowModBusType eq '032' }" >新闻模块</c:when>
																	</c:choose>
																</div>
															</li>
															<li class="ticket-item no-shadow ps-listline"
																style="clear:both">
																<div class="pull-left gray ps-left-text">流程分类</div>
																<div class="ticket-user pull-left ps-right-box">
																	<select class="populate" name="spFlowTypeId" onchange="updateFlowAttr('spFlowType');" style="cursor:auto;width: 200px">
																		<optgroup label="选择流程分类"></optgroup>
																		<c:choose>
																			<c:when test="${not empty listSpFlowType}">
												 								<c:forEach items="${listSpFlowType}" var="spFlowType" varStatus="status">
												 									<option value="${spFlowType.id}" ${spFlowType.id==flowConfig.spFlowTypeId?'selected="selected"':''}>${spFlowType.typeName}</option>
												 								</c:forEach>
												 								<option value="0" ${0==flowConfig.spFlowTypeId?'selected="selected"':''}>无类别</option>
																			</c:when>
																		</c:choose>
																	</select>
																</div>
															</li>
															<li class="ticket-item no-shadow ps-listline"
																style="clear:both;${flowConfig.flowModBusType eq '022' ? '':'display:none'}">
																<div class="pull-left gray ps-left-text">关联表单</div>
																<div class="ticket-user pull-left ps-right-box">
																	<div>
																		<input id="formName" readonly="readonly"
																			placeholder="点击选取表单。"
																			class="colorpicker-default form-control" type="text"
																			value="${flowConfig.formName}"> <input
																			type="hidden" name="formKey" id="formKey"
																			value="${flowConfig.formKey}" />
																	</div>
																</div>
															</li>
															<li class="ticket-item no-shadow autoHeight no-padding"
																style="clear:both">
																<div class="pull-left gray ps-left-text padding-top-10">
																	&nbsp;流程说明</div>
																<div class="ticket-user pull-left ps-right-box"
																	style="width: 400px;height: auto;">
																	<textarea
																		class="colorpicker-default form-control margin-top-10 margin-bottom-10"
																		style="height:150px;" id="remark" name="remark"
																		rows="" cols="" onchange="updateFlowAttr('remark');">${flowConfig.remark }</textarea>
																</div>
																<div class="ps-clear"></div>
															</li>
															<li class="ticket-item no-shadow ps-listline"
																style="clear: both;${flowConfig.flowModBusType eq '022' ? '':'display:none'}">
																<div class="pull-left gray ps-left-text">
																	&nbsp;流程关联</div>
																<div class="ticket-user pull-left ps-right-box">
																	<span id="sonFlowName">${flowConfig.sonFlowName}</span>
																	<a href="javascript:void(0);" onclick="spFlowModelRelevance(${flowConfig.id});">
																		<button class="btn btn-info btn-primary btn-xs" type="button">
																			<i class="fa fa-plus"></i>流程关联
																		</button>
																	</a>
																	<c:if test="${not empty flowConfig.sonFlowId}">
																		<a href="javascript:void(0);" onclick="delFlowModelRelevance();" class="margin-left-5">
																			<button class="btn btn-info btn-primary btn-xs" type="button">
																				<i class="fa fa-times"></i>清除关联
																			</button>
																		</a>
																	</c:if>
																</div>
															</li>
															<li id="scopeByDepTableLi" class="ticket-item no-shadow ps-listline"
																style="clear:both;height:80px;">
																<div class="pull-left gray ps-left-text">流程范围<br >&nbsp;(按部门)</div>
																<div class="ticket-user pull-left ps-right-box">
																	<button id="dep" class="btn btn-info btn-primary btn-xs" type="button">部门选择</button>
																	<table id="scopeByDepTable" style="width:300px;">
																	   	<c:choose>
																	   		<c:when test="${not empty flowConfig.listSpFlowScopeByDep}">
																	   			<c:forEach items="${flowConfig.listSpFlowScopeByDep}" var="dep" varStatus="depVs">
																		   			<tr>
																		   				<td style="text-align:center;width:5%;">[${depVs.count}]</td>
																		   				<td style="padding-left:20px;">
																		   					${dep.depName}
																		   					<input type="hidden" name="depIds" value="${dep.depId}"/>
																		   				</td>
																		   				<td style="text-align:center;width:10%;"><a href="javascript:void(0);"
																		   				 onclick="delScopeByDep(this)">删除</a></td>
																		   			</tr>
																	   			</c:forEach>
																	   		</c:when>
																	   	</c:choose>
																	   </table>
																</div>
															</li>
															<li id="scopeByUserTableLi" class="ticket-item no-shadow ps-listline"
																style="clear:both;height:80px;">
																<div class="pull-left gray ps-left-text">流程范围<br >&nbsp;(按人员)</div>
																<div class="ticket-user pull-left ps-right-box">
																	<button id="user" class="btn btn-info btn-primary btn-xs" type="button">人员选择</button>
																	<table id="scopeByUserTable" style="width:300px;">
																	   	<c:choose>
																	   		<c:when test="${not empty flowConfig.listSpFlowScopeByUser}">
																	   			<c:forEach items="${flowConfig.listSpFlowScopeByUser}" var="user" varStatus="userVs">
																		   			<tr>
																		   				<td style="text-align:center;width:5%;">[${userVs.count}]</td>
																		   				<td style="padding-left:20px;">
																		   					${user.userName}
																		   					<input type="hidden" name="userIds" value="${user.userId}"/>
																		   				</td>
																		   				<td style="text-align:center;width:10%;"><a href="javascript:void(0);"
																		   				 onclick="delScopeByUser(this)">删除</a></td>
																		   			</tr>
																	   			</c:forEach>
																	   		</c:when>
																	   	</c:choose>
																	   </table>
																</div>
															</li>
															<li class="ticket-item no-shadow ps-listline"
																style="clear:both">
																<div class="pull-left gray ps-left-text">
																	&nbsp;创建人</div>
																<div class="ticket-user pull-left ps-right-box">
																	<img src="/downLoad/userimg/${flowConfig.comId}/${flowConfig.creator}?sid=${param.sid}"
																		class="user-avatar"
																		title="${flowConfig.creatorName}" />
																	<span class="user-name">${flowConfig.creatorName}</span>
																</div>
															</li>
															<li class="ticket-item no-shadow ps-listline"
																style="clear:both">
																<div class="pull-left gray ps-left-text">
																	&nbsp;创建于</div>
																<div class="ticket-user pull-left ps-right-box">
																	${flowConfig.recordCreateTime}</div>
															</li>
														</form>
													</ul>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">
		var jq11 = $.noConflict(true);
	</script>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
	<%--用与测量当前页面的高度 --%>
	<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>