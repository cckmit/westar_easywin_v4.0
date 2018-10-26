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
	var vali;
	$(function() {
		$("#itemRemark").focus();
		//设置滚动条高度
		var height = $(window).height() - 45;
		$("#contentBody").css("height", height + "px");

		vali = $(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			},
			datatype : {
				"input" : function(gets, obj, curform, regxp) {
					var str = $(obj).val();
					if (str) {
						var count = str.replace(/[^\x00-\xff]/g, "**").length;
						var len = $(obj).attr("defaultLen");
						if (count > len) {
							return "项目名称太长";
						} else {
							return true;
						}
					} else {
						return false;
					}
				},
				"itemRemark" : function(gets, obj, curform, regxp) {
					var str = $(obj).val();
					if (str) {
					} else {
						return false;
					}
				}
			},
			callback : function(form) {
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		
		//说明改变，同时改变名称
		$("#itemRemark").bind('input propertychange', function() {
			var value = $('#itemRemark').val();
			value = cutString(value,52);
			var len = charLength(value);
			if(len%2==1){
				len = (len+1)/2;
			}else{
				len = len/2;
			}
			
           	$("#msgTitle").html("("+len+"/26)");
			$("#itemName").val(value);
			
			// 相似客户数量
			checkSimilarItemName();
		});
		
		//展开跟多的区域
		$(".inner").click(function(){
			var moreOptShow = $("#moreOpt").css("display");
			if(moreOptShow=='none'){
				$("#moreOpt").slideDown("slow");
				$("#moreOptImg").removeClass("fa-angle-down");
				$("#moreOptImg").addClass("fa-angle-up");
			}else{
				$("#moreOpt").slideUp("slow");
				$("#moreOptImg").removeClass("fa-angle-up");
				$("#moreOptImg").addClass("fa-angle-down");
			}
		});
	})
	var fileTypes = "*.rar;*.zip;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;*.pdf;*.mp3;*.gif;*.jpg;*.jpeg;*.png;*.bmp;*.avi;*.wma;*.rmb;*.rm;*.flash;*.flv;*.mp4;*.mid;*.3gp";
	var fileSize = "200MB";
	var numberOfFiles = 200;
	//项目组关联
	function initItemGroup(groups) {
		var grp = eval(groups);
		var grpName = "";
		for ( var i = 0; i < grp.length; i++) {

			grpName += "<span id=\"itemGrp_"
					+ grp[i].id
					+ "\" style='cursor:pointer;' title=\"双击移除\" class=\"label label-default  margin-top-5 margin-left-5 margin-bottom-5\" ondblclick=\"removeItemGrp('"
					+ grp[i].id + "')\">" + grp[i].name;
			grpName += "<input type=\"hidden\" name=\"listShareGroup["+i+"].id\" value=\""+grp[i].id+"\"/>";
			grpName += "</span>";

		}
		$("#itemGroupName").html(grpName);
		vali.addRule();
	}
	//移除项目的共享分组
	function removeItemGrp(grpId) {
		removeObjByKey("itemGrp_" + grpId)
		$("#selfGrouplist").find("option[value='" + grpId + "']").remove();
	}

	$(function() {
		$("#itemName").keydown(function(event) {
			if (event.keyCode == 13) {
				return false;
			}
		});
		
		// 验证与验证项目名称相似的项目
		
	$("#itemName").keyup(function() {
		checkSimilarItemName()
	});

		//保存并查看
		$("#addItem").click(function() {
			$("#way").val("view");
			$(".subform").submit();
		});
		$("#addAndviewItem").click(function() {
			$("#way").val("add");
			$(".subform").submit();
		});

	});
	// 验证与验证项目名称相似的项目
	function checkSimilarItemName(){
		if (!strIsNull($("#itemName").val())) {
			$("#itemName").parent().find("div").css("display", "none")
			$.post("/item/checkSimilarItemByItemName?sid=${param.sid}",{Action : "post",itemName : $("#itemName").val()},
				function(itemNum) {
					if (itemNum > 0) {
						$("#addItemWarm").html("<a href='javascript:void(0);' style='background:none;width:80px;color:#ff0000;' onclick='similarItemsPage();'>相似项目("+ itemNum+ ")</a>");
					} else {
						$("#addItemWarm").text("");
					}
				});
		} else {
			$("#addItemWarm").text("");
			$("#itemName").parent().find("div").css("display", "block")
		}
	}
	//弹窗显示相似项目
	function similarItemsPage() {
		var url = "/item/similarItemsPage?pager.pageSize=8&sid=${param.sid}&itemName="
				+ $("#itemName").val();
		window.top.layer
				.open({
					type : 2,
					//title: ['相似项目列表', 'font-size:18px;'],
					title : false,
					closeBtn : 0,
					area : [ '580px', '500px' ],
					fix : true, //不固定
					maxmin : false,
					move : false,
					content : [ url, 'no' ],
					success : function(layero, index) {
						var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
						$(iframeWin.document).find("#titleCloseBtn").on(
								"click", function() {
									window.top.layer.close(index);
								})
					}
				});
	}
	//关联客户返回函数
	function crmSelectedReturn(crmId, name) {
		var oldId = $("#partnerId").val();
		if (oldId != crmId) {//不是同一个项目则阶段主键就不一样,且不是自己
			if (!strIsNull(crmId)) {
				//项目来源关联
				$("#partnerName").val(name);
				$("#partnerId").val(crmId);
			}
		}
	}
	//移除客户关联
	function removeCrm() {
		var crmId = $("#partnerId").val();
		if (crmId) {
			//项目来源关联
			$("#partnerName").val('');
			$("#partnerId").val('');
		}
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

<style type="text/css">
.outer,.inner{
	height: 40px;
}
.bg-line{
	height: 22px;border-bottom: 2px solid #f0f0f0;
}
.outer{
	position: absolute;width: 100%;top: 0;left: 0;z-index: 10;
}
.inner{
	width: 95px;
	line-height: 40px;
	background:#fff;
	font-weight: bold;
	text-align: center;
	margin: 0 auto;
	z-index: 20;
	font-size: 18px;
}
</style>

</head>
<body>
	<form class="subform" method="post" action="/item/addItem">
		<tags:token></tags:token>
		<input type="hidden" name="redirectPage" value="${param.redirectPage}" />
		<input type="hidden" name="parentId" value="-1" /> <input
			type="hidden" name="attentionState" id="attentionState" value="0" />
		<input type="hidden" name="way" id="way"> 
		<div class="container" style="padding: 0px 0px;width: 100%">
			<div class="row" style="margin: 0 0">
				<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
					<div class="widget" style="margin-top: 0px;">
						<div
							class="widget-header bordered-bottom bordered-themeprimary detailHead">
							<span class="widget-caption themeprimary" style="font-size: 20px">添加项目</span>
							<div class="widget-buttons ps-toolsBtn">
								<a href="javascript:void(0)" class="blue"
									onclick="setAtten(this)"> <i class="fa fa-star-o"></i>关注 </a> <a
									href="javascript:void(0)" class="blue" id="addItem"> <i
									class="fa fa-save"></i>添加 </a>
							</div>
							<div class="widget-buttons">
								<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
									<i class="fa fa-times themeprimary"></i> </a>
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
													项目说明 <span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box"
													style="width: 80%;height: auto;">
													<textarea
														class="colorpicker-default form-control margin-top-10 margin-bottom-10"
														style="width:80%;height:150px;float:left" id="itemRemark" name="itemRemark"
														rows="" cols="" datatype="itemRemark" nullmsg="请填写项目说明"></textarea>
												</div>
												<div class="ps-clear"></div></li>
												 <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	项目名称
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="itemName" datatype="input,sn" defaultLen="52" name="itemName" nullmsg="请填写项目名称" 
													class="colorpicker-default form-control" type="text" value="" style="width: 400px;float: left">
													<span id="msgTitle" style="float:left;width: auto;">(0/26)</span>
												</div>
												<div class="pull-left">
													<span id="addItemWarm" style="float:left;margin-left:2px;"></span>
												</div>
											</div>
											<script> 
												//当状态改变的时候执行的函数 
												function handleName(){
													var value = $('#itemName').val();
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
													document.getElementById('itemName').onpropertychange=handleName 
												}else {//非ie浏览器，比如Firefox 
													document.getElementById('itemName').addEventListener("input",handleName,false); 
												} 
											</script>  
                                         </li>
											<li class="ticket-item no-shadow ps-listline"
												style="clear:both;border-bottom:0;">
												<div class="pull-left gray ps-left-text">
													责任人 <span style="color: red">*</span>
												</div>
												<div class="ticket-user pull-left ps-right-box"
													style="min-width: 135px">
													<tags:userOne datatype="s" name="owner" defaultInit="true"
														showValue="${userInfo.userName}" value="${userInfo.id}"
														uuid="${userInfo.smImgUuid}"
														filename="${userInfo.smImgName}"
														gender="${userInfo.gender}" onclick="true"
														showName="onwerName"></tags:userOne>
												</div> <c:choose>
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
											<!-- <div style="position: relative;">
												<div class="bg-line"></div>
												<div class="outer">
													<div class="inner ps-point themeprimary">
														更多配置<i class="fa fa-angle-down padding-left-5" id="moreOptImg"></i>
													</div>
												</div>
											</div> -->
											<div id="moreOpt" style="display:block;clear: both">
												<li class="ticket-item no-shadow ps-listline" style="clear:both">
												    <div class="pull-left gray ps-left-text">
												    	关联客户
												    </div>
													<div class="ticket-user pull-left ps-right-box">
														<input type="hidden" id="partnerId" name="partnerId" value=""/>
														<div style="float: left">
															<input id="partnerName" name="partnerName" class="colorpicker-default form-control pull-left" type="text" 
															ondblclick="removeCrm()" readonly style="cursor:auto;width: 400px;float:left" value="" title="双击选择">
															<a href="javascript:void(0)" class="fa fa-chain pull-left" style="float:left;margin-top: 10px;font-size: 18px;padding-left: 10px"  
															onclick="listCrm('${param.sid}','partnerId');"></a>
														</div>
														 
													</div>               
		                                        </li>
												
												<%-- <li class="ticket-item no-shadow autoHeight no-padding">
											    	<div class="pull-left gray ps-left-text padding-top-20">
											    		分享人
											    	</div>
													<div class="ticket-user pull-left ps-right-box" style="height: auto;">
														<div class="pull-left gray ps-left-text padding-top-10">
													  		<tags:userMore name="listItemSharer.userId" showName="userName" disDivKey="itemSharor_div"></tags:userMore>
														</div>
													</div>
													<div class="ps-clear"></div>
		                                         </li> --%>
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
														  		<tags:userMore name="listItemSharer.userId" showName="userName" disDivKey="itemSharor_div"></tags:userMore>
															</div>
														</div>
														<div class="ps-clear"></div>
			                                         </li>
												<%-- <li class="ticket-item no-shadow autoHeight no-padding">
											    	<div class="pull-left gray ps-left-text padding-top-20">
											    		共享组
											    	</div>
													<div class="ticket-user pull-left ps-right-box" style="height: auto;">
														<div class="pull-left gray ps-left-text padding-top-10">
													  		<div id="itemGroupName" class="pull-left" style="max-width: 460px" >
															</div>
													  		<a href="javascript:void(0)" onclick="querySelfGroup(0,'${param.sid}')" class="btn btn-primary btn-xs margin-top-5 margin-bottom-5 margin-left-5" title="组选择"><i class="fa fa-plus"></i>添加</a>
															用于保持数据
															<div style="display:none" id="tempSelectDiv">
																<select style="display:none" id="selfGrouplist">
																</select>
															</div>
														</div>
													</div>
													<div class="ps-clear"></div>
		                                         </li> --%>
												<li class="ticket-item no-shadow ps-listline" style="clear:both">
													<div class="pull-left gray ps-left-text">
														相关附件</div>
													<div class="ticket-user pull-left ps-right-box"
														style="width: 400px;height: auto;">
														<tags:uploadMore name="listUpfiles.id" showName="filename"
																ifream="" comId="${userInfo.comId}"></tags:uploadMore>
													</div>
													<div class="ps-clear"></div>
												</li>
											</div>
										</ul>
									</div>
								</div>
							</div>
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
