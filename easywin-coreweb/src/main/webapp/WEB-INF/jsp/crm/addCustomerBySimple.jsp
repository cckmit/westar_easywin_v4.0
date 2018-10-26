<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.TokenManager"%>
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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	//关闭窗口
	function closeWin() {
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	var valid;
	$(function() {
		$("#customerRemark").focus();
		//设置滚动条高度
		var height = $(window).height() - 40;
		$("#contentBody").css("height", height + "px");

		valid = $(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			},
			callback : function(form) {
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			datatype : {
				"area" : function(gets, obj, curform, regxp) {
					if (!strIsNull($("#areaIdAndType").val())) {
						return true;
					} else {
						return false;
					}
				},
				"input" : function(gets, obj, curform, regxp) {
					var str = $(obj).val();

					if (str) {
						var count = str.replace(/[^\x00-\xff]/g, "**").length;
						var len = $(obj).attr("defaultLen");
						if (count > len) {
							return "客户名称太长";
						} else {
							return true;
						}
					} else {
						return false;
					}
				},
				"customerType" : function(gets, obj, curform, regxp) {
					if (!strIsNull($("#customerTypeId").val())) {
						return true;
					} else {
						return false;
					}
				},
				"stage":function(gets,obj,curform,regxp){
					if($("#stage").val()){
						return true;
					}else{
						return false;
					}
				},
				"customerRemark" : function(gets, obj, curform, regxp) {
					var str = $(obj).val();
					if (str) {
					} else {
						return false;
					}
				},
				"float":function(gets,obj,curform,regxp){
					if(isFloat($("#budget").val())){
						return true;
					}else{
						return "请填入正确的数字";
					}
				},
			},
			showAllError : true
		});
		
		//说明改变，同时改变名称
		$("#customerRemark").bind('input propertychange', function() {
			var value = $('#customerRemark').val();
			value = cutString(value,52);
			
			var len = charLength(value);
			if(len%2==1){
				len = (len+1)/2;
			}else{
				len = len/2;
			}
            $("#msgTitle").html("("+len+"/26)");
			$("#customerName").val(value);
			
			// 相似客户数量
			checkSimilarCustomerName();
		});
		
		//相似客户数量
		$("#customerName").keyup(function() {
			checkSimilarCustomerName();
		});

		//form表单提交
		$("#addCrm").click(function() {
			//添加完成后查看
			$("#way").val("view");
			$(".subform").submit();
		});
		//form表单提交
		$("#addAndviewCrm").click(function() {
			//添加完成后继续添加
			$("#way").val("add");
			$(".subform").submit();
		});
	});
	//重写回调函数为空
	function artOpenerCallBack(args) {
	}

	var linkManRowNum = 1;
	//添加联系人
	function addLinkMan() {
		//联系人添加
		$("#oneMoreLinkMan").text("再添加一个");
		;
		var tr = "<tr>";
		tr += "<td><input class=\"table_input\" type=\"text\" nullmsg=\"请输入联系人\" id=\"listLinkMan["+linkManRowNum+"].linkManName\" name=\"listLinkMan["+linkManRowNum+"].linkManName\" /></td>";
		tr += "<td><input class=\"table_input\" type=\"text\" id=\"listLinkMan["+linkManRowNum+"].post\" name=\"listLinkMan["+linkManRowNum+"].post\" /></td>";
		tr += "<td><input class=\"table_input\" type=\"text\" ignore='ignore' id=\"listLinkMan["+linkManRowNum+"].linePhone\" name=\"listLinkMan["+linkManRowNum+"].linePhone\" /></td>";
		tr += "<td><input class=\"table_input\" type=\"text\" ignore='ignore' nullmsg=\"请输入正确的手机号码\" id=\"listLinkMan["+linkManRowNum+"].movePhone\" name=\"listLinkMan["+linkManRowNum+"].movePhone\"/></td>";
		tr += "<td><a href=\"javascript:void(0)\" class=\"fa fa-times-circle-o\" onclick=\"delTr(this)\" title=\"删除本行\"></a></td>";
		tr += "</tr>";
		$("#linkManTable tbody").find("tr").last().after(tr);
		valid.addRule();
		linkManRowNum = linkManRowNum + 1;
	}
	//删除联系人
	function delTr(clickTd) {
		var trLen = $("#linkManTable tbody").find("tr").length;
		if (trLen > 1) {
			var tr = $(clickTd).parent().parent();
			tr.remove();
		} else {
			layer.msg("最后一行，不能删除")
		}
	}
	//设置客户共享组
	function initCustomerGroup(groups) {
		var grp = eval(groups);
		var grpName = "";
		for ( var i = 0; i < grp.length; i++) {
			grpName += "<span id=\"crmGrp_"
					+ grp[i].id
					+ "\" style='cursor:pointer;' title=\"双击移除\" class=\"label label-default margin-top-5 margin-left-5 margin-bottom-5\" ondblclick=\"delCrmShareGroup('"
					+ grp[i].id + "')\">" + grp[i].name;
			grpName += "<input type=\"hidden\" name=\"listShareGroup["+i+"].id\" value=\""+grp[i].id+"\"/>";
			grpName += "</span>";

		}
		$("#customerGroup_div").html(grpName);
	}
	//相似客户
	function checkSimilarCustomerName(){
		if (!strIsNull($("#customerName").val())) {
			$("#customerName").parent().find("div").css("display", "none")
			$.post("/crm/checkSimilarCrmByName?sid=${param.sid}",{Action : "post",crmName : $("#customerName").val()},
			function(crmNum) {
				if (crmNum > 0) {
					$("#addCrmWarm")
					.html("<a href='javascript:void(0);' style='background:none;width:80px;color:#ff0000;' onclick='similarCrmsPage();'>相似客户("+ crmNum+ ")</a>");
				} else {
					$("#addCrmWarm").text("");
				}
			});
		} else {
			$("#customerName").parent().find("div").css("display", "block")
			$("#addCrmWarm").text("");
		}
	}
	//弹窗显示相似客户
	function similarCrmsPage() {
		var url = "/crm/similarCrmsPage?pager.pageSize=8&sid=${param.sid}&crmName="+$("#customerName").val()+"&redirectPage="+encodeURIComponent(window.location.href);
		window.top.layer.open({
			 type: 2,
			  //title: ['相似客户列表', 'font-size:18px;'],
			  title:false,
			  closeBtn:0,
			  area: ['580px', '500px'],
			  fix: true, //不固定
			  maxmin: false,
			  move: false,
			  content: [url,'no'],
			  success: function(layero,index){
				  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					  window.top.layer.close(index);
				  })
			  }
		});
	}
	//移除客户分享组数据
	function delCrmShareGroup(groupId) {
		removeObjByKey("crmGrp_" + groupId);
		$("#selfGrouplist").find("option[value='" + groupId + "']").remove();
	}
	//人员范围的显示隐藏
	function showMans(){
		
		if($("#pubState").val() == 0){
			$("#mans").show();
		}else{
			$("#mans").hide();
		}
		
	}
</script>
</head>
<body>
	<form class="subform" method="post" action="/crm/addCustomer">
		<tags:token></tags:token>
		<input type="hidden" name="attentionState" id="attentionState"
			value="0" /> <input type="hidden" name="way" id="way"> 
		<div class="container" style="padding: 0px 0px;width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div
							class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary" style="font-size: 20px">添加客户</span>
							<div class="widget-buttons ps-toolsBtn">
								<a href="javascript:void(0)" class="blue"
									onclick="setAtten(this)"> <i class="fa fa-star-o"></i>关注 </a> <a
									href="javascript:void(0)" class="blue" id="addCrm"> <i
									class="fa fa-save"></i>添加 </a>
							</div>
							<div class="widget-buttons">
								<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
									<i class="fa fa-times"></i> </a>
							</div>
						</div>
						<!--Widget Header-->
						<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
						<div class="widget-body margin-top-40" id="contentBody"
							style="overflow: hidden;overflow-y:scroll;">
							<div class="widget radius-bordered">
								<div class="widget-body no-shadow">
									<div class="tickets-container bg-white">
										<ul class="tickets-list">
											<li class="ticket-item no-shadow autoHeight no-padding">
												<div class="pull-left gray ps-left-text padding-top-10">
													客户说明 <span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box"
													style="width:80%;height: auto;">
													<textarea
														class="colorpicker-default form-control margin-top-10 margin-bottom-10"
														style="width:80%;height:150px;float:left"" id="customerRemark"
														name="customerRemark" rows="" cols="" datatype="customerRemark" nullmsg="请填写客户说明"></textarea>
												</div>
												<div class="ps-clear"></div>
												</li>
												  <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	客户名称<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="customerName" datatype="input,sn" defaultLen="52" name="customerName" nullmsg="请填写客户名称" 
													class="colorpicker-default form-control" type="text" value="" style="width: 400px;float: left">
													<span id="msgTitle" style="float:left;width: auto;">(0/26)</span>
												</div>
												<div class="pull-left">
													<span id="addCrmWarm" style="float:left;margin-left:2px;"></span>
												</div>
											</div>
											<script> 
												//当状态改变的时候执行的函数 
												function handleName(){
													var value = $('#customerName').val();
													var len = charLength(value);
													if(len%2==1){
														len = (len+1)/2;
													}else{
														len = len/2;
													}
													if(len>26){
														$('#msgTitle').html("(<font color='red'>"+len+"</font>/26)");
													}else{
														$('#msgTitle').html("("+len+"/26)");
													}
												} 
												//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
												if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
													document.getElementById('customerName').onpropertychange=handleName 
												}else {//非ie浏览器，比如Firefox 
													document.getElementById('customerName').addEventListener("input",handleName,false); 
												} 
											</script>  
                                         </li>
											<li class="ticket-item no-shadow ps-listline"
												style="clear:both">
												<div class="pull-left gray ps-left-text">
													责任人 <span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box"
													style="min-width: 135px">
													<tags:userOne datatype="s" name="owner" defaultInit="true"
														showValue="${userInfo.userName}" value="${userInfo.id}"
														comId="${userInfo.comId}"  onclick="true"
														showName="onwerName"></tags:userOne>
												</div> 
												<c:choose>
													<c:when test="${not empty listUsed}">
														<div>
															<span class="pull-left" style="margin-top:8px">常用人员:</span>
															<c:forEach items="${listUsed}" var="usedUser"
																varStatus="vs">
																<div class="ticket-user pull-left other-user-box">
																	<a href="javascript:void(0);" onclick="setUser('owner','onwerName','${usedUser.id}',this)">
																		<img src="/downLoad/userImg/${usedUser.comId}/${usedUser.id}?sid=${param.sid}"
																			title="${usedUser.userName}" class="usedUserImg margin-left-5"/>
																		<span class="user-name2" style="font-size:6px;">${usedUser.userName}</span>
																	</a>
																</div>
															</c:forEach>
														</div>
													</c:when>
												</c:choose>
											</li>
											<li class="ticket-item no-shadow ps-listline"
												style="clear: both">
												<div class="pull-left gray ps-left-text">
													客户区域 <span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<input type="hidden" id="areaIdAndType"
														name="areaIdAndType" />
													<div style="float: left">
														<input id="areaName" datatype="area" name="areaName"
															class="colorpicker-default form-control pull-left"
															type="text"
															onclick="areaOne('areaIdAndType','areaName','${param.sid}','0');"
															readonly style="cursor:auto;width: 200px;float:left"
															value="" title="双击选择"> <a
															href="javascript:void(0)"
															class="fa fa-map-marker pull-left"
															style="float:left;margin-top: 10px;font-size: 18px;padding-left: 10px"
															title="区域选择"
															onclick="areaOne('areaIdAndType','areaName','${param.sid}','0');"></a>
													</div>
												</div>
											</li>
											<li class="ticket-item no-shadow ps-listline"
												style="clear:both">
												<div class="pull-left gray ps-left-text">
													客户类型 <span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box">
													<select class="populate" datatype="customerType"
														id="customerTypeId" name="customerTypeId"
														style="cursor:auto;width: 200px">
														<optgroup label="选择客户类型"></optgroup>
														<c:choose>
															<c:when test="${not empty listCustomerType}">
																<c:forEach items="${listCustomerType}"
																	var="customerType" varStatus="status">
																	<option value="${customerType.id}">${customerType.typeName}</option>
																</c:forEach>
															</c:when>
														</c:choose>
													</select>
												</div>
											</li>
											    <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	所属阶段<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											<div datatype="stage"  class=" pull-left ">
											   <select class="populate"   id="stage" name="stage" style="cursor:auto;width: 200px">
													<optgroup label="选择所属阶段"></optgroup>
													<c:choose>
														<c:when test="${not empty listCrmStage}">
							 								<c:forEach items="${listCrmStage}" var="stage" varStatus="status">
							 								<option value="${stage.id}">${stage.stageName}</option>
							 								</c:forEach>
														</c:when>
													</c:choose>
												</select>
												</div>
											</div>               
                                           </li>
                                             <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	资金预算(元)<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="budget" datatype="float" name="budget" nullmsg="请填写资金预算" 
													class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         
                                         <li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text">
											    	阅读权限
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												  	<div class="pull-left">
												  		<select class="populate" id="pubState" name="pubState" onchange="showMans()">
						 								<option value="0">私有</option>
						 								<option value="1">公开</option>
															</select>
													</div>
												</div>
	                                         </li>
                               			
											<li class="ticket-item no-shadow autoHeight no-padding" style="display: block;" id="mans">
										    	<div class="pull-left gray ps-left-text padding-top-10">
										    		分享人
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="height: auto;">
													<div class="pull-left gray ps-left-text padding-top-10">
												  		<tags:userMore name="listCustomerSharer.userId" showName="userName" disDivKey="crmSharor_div"></tags:userMore>
													</div>
												</div>
												<div class="ps-clear"></div> 
	                                         </li>
                                         
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													相关附件
												</div>
												<div class="ticket-user pull-left ps-right-box"
													style="width: 450px;height: auto;">
													<tags:uploadMore name="listUpfiles.upfileId"
														showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
												</div>
												<div class="ps-clear"></div>
											</li>
										</ul>
									</div>
								</div>
							</div>
							<div class="widget-body no-shadow"></div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</form>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
