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
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<!-- 自动补全js -->
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
	//打开页面body
	var openWindowDoc;
	//打开页面,可调用父页面script
	var openWindow;
	//打开页面的标签
	var openPageTag;
	//注入父页面信息
	function setWindow(winDoc, win) {
		openWindowDoc = winDoc;
		openWindow = win;
		openPageTag = openWindow.pageTag;
	}

	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}

	var val;
	$(function() {

		//设置滚动条高度
		var height = $(window).height() - 40;
		$("#contentBody").css("height", height + "px");
		
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			callback : function(form) {
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		
		//汇报内容
		$("#reportContentMenuLi").click(function(){
			$("#otherIframe").css("display","none");
			$("#reportContent").css("display","block");
			$(this).parent().parent().find("li").removeAttr("class");;
			$(this).parent().attr("class","active");
			
			$(".optViewWeekReport").parent().show();
		});
		$("#weekTalk").click(function() {
							$("#otherIframe").css("display","block");
							$("#reportContent").css("display","none");
							$(this).parent().parent().find("li").removeAttr(
									"class");
							$(this).parent().attr("class", "active");
							$(".optViewWeekReport").parent().hide()
							//留言
							$("#otherIframe")
									.attr(
											"src",
											"/weekReport/weekRepTalkPage?sid=${param.sid}&pager.pageSize=10&weekReportId=${weekReport.id}")
						});
		$("#weekLog")
				.click(
						function() {
							$("#otherIframe").css("display","block");
							$("#reportContent").css("display","none");
							$(this).parent().parent().find("li").removeAttr(
									"class");
							$(this).parent().attr("class", "active")
							$(".optViewWeekReport").parent().hide()
							//周报日志
							$("#otherIframe")
									.attr(
											"src",
									"/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${weekReport.id}&busType=006&ifreamName=otherIframe");
						});
		$("#weekFile")
				.click(
						function() {
							$("#otherIframe").css("display","block");
							$("#reportContent").css("display","none");
							$(this).parent().parent().find("li").removeAttr(
									"class");
							$(this).parent().attr("class", "active")
							$(".optViewWeekReport").parent().hide()
							//周报附件
							$("#otherIframe")
									.attr(
											"src",
											"/weekReport/weekRepFilePage?sid=${param.sid}&pager.pageSize=10&weekReportId=${weekReport.id}")
						});

		//查看记录
		$("#weekViewRecord")
				.click(
						function() {
							$("#otherIframe").css("display","block");
							$("#reportContent").css("display","none");
							$(this).parent().parent().find("li").removeAttr(
									"class");
							;
							$("#weekViewRecord").parent().attr("class",
									"active");
							$(".optViewWeekReport").parent().hide()
							$("#otherIframe")
									.attr(
											"src",
											"/common/listViewRecord?sid=${param.sid}&busId=${weekReport.id}&busType=006&ifreamName=otherIframe");
						});
	})
	$(document).ready(function() {
		$(".taskarea").autoTextarea({
			minHeight : 55,
			maxHeight : 80
		});
		$(".taskareaAdd").autoTextarea({
			minHeight : 55,
			maxHeight : 150
		});
		//验证周报日期是否正确
		$("#weekBunList").data("isWeekOk",{"status":"f","info":"正在后台验证周报发布时间"}).status='f'
		$("#weekBunList").data("isWeekOk").info='正在后台验证周报发布时间';
		 var weekData = '${firstDayOfWeek}';
		 checkWeekNum(weekData,function(data){
			if(data.status=='y'){
				var isWeekOk = {"status":"yes"};
				$("#weekBunList").data("isWeekOk",isWeekOk);
			}else{
				showNotification(2,data.info);
				var isWeekOk = {"status":"no","info":data.info};
				$("#weekBunList").data("isWeekOk",isWeekOk);
				   
			}
		 })
		
		
		checkLeader('${param.sid}',function(data){
			   if(data.status=='f'){
				   showNotification(2,data.info);
			   }else if(data.status=='f1'){
				   window.top.layer.confirm("直属上级未设定", {
						title : "警告",
						closeBtn:0,
						move:false,
						btn : [ "设定", "取消" ],
						icon : 0
					}, function(index) {
						window.top.layer.close(index);
						if(openPageTag=='week'){
							$(openWindowDoc).find("#weekRepSetBtn").click();
						}else{
							openWindow.setWeekRepViewScope('${param.sid}');
						}
					},function(index){
						window.top.layer.close(index);
						var winIndex = window.top.layer.getFrameIndex(window.name);
						closeWindow(winIndex);
					});
				  
			   }
			})
	});
	/**
	 * 添加下周计划
	 */
	function addPlan(ts, index) {
		//当前行的文本域数据
		var textarea = $("#planBody" + index).find("textarea").val();
		//当前行的文本框数据
		var input = $("#planBody" + index).find("input").val();
		if (strIsNull(textarea)) {//文本域数据数据为空
			layer.tips("请填写计划内容",$("#planBody" + index),{tips: 1});
			$("#planBody" + index).find("textarea").focus();
			return;
		}
		var num = index + 1;
		var buttons = $(ts).parent().find("button").length;
		if (1 == buttons) {
			var html = '<button class="btn btn-default" type="button" onclick="delPlan(this,'
					+ index + ')">删除</button>';
			$(ts).parent().html(html);
		} else {
			$(ts).parent().find(".ws-btnBlue").remove();
		}

		var html = '';
		html += '\n	<div class="row ws-plan-line" id="planBody'+num+'" style="padding-bottom: 10px">';
		html += '\n		<div class="control-label col-xs-7">';
		html += '\n			<textarea name="weekReportPlans['+num+'].planContent" class="form-control taskarea" style="color: #000"></textarea>';
		html += '\n		</div>';
		html += '\n		<div class="col-xs-2">';
		html += '\n			<input type="text" name="weekReportPlans['
				+ num
				+ '].planTime" onclick="WdatePicker({dateFmt:\'yyyy年MM月dd日\'});" style="cursor: pointer;width: 125px;padding: 0px 5px" class="form-control" readonly="readonly" />';
		html += '\n		</div>';
		html += '\n		<div class="col-xs-3 ws-plan-btn" style="text-align:center"> ';
		html += '\n			<button class="btn btn-info ws-btnBlue" type="button" onclick="addPlan(this,'
				+ num + ')">添加</button>';
		html += '\n			<button class="btn btn-default" type="button" onclick="delPlan(this,'
				+ num + ')">删除</button>';
		html += '\n		</div>';
		html += '\n	</div>';
		$("#weekPlan").append(html);
		$(".taskarea").autoTextarea({
			minHeight : 55,
			maxHeight : 80
		});

	}
	//删除计划
	function delPlan(ts, index) {
		//当前按钮是否为最后一行
		var buttons = $(ts).parent().find("button").length;
		var plans = $("#planBody" + index).parent().find(".ws-plan-line").length;
		if (plans == 2) {//还有两个计划,只有一个计划的时候就没有删除按钮了
			if (buttons == 2) {//删除的是最后一行
				//前一个节点的id
				var preId = $("#planBody" + index).prev().attr("id");
				var indexPre = preId.replace('planBody', '');
				var html = '<button class="btn btn-info ws-btnBlue" type="button" onclick="addPlan(this,'
						+ indexPre + ')">添加</button>';
				//前一个节点设置为添加按钮
				$("#planBody" + indexPre).find(".ws-plan-btn").html(html);
			} else {//删除的是第一个计划
				//最后一个节点保留添加按钮
				$("#planBody" + index).next().find(".ws-plan-btn").find(
						".btn-default").remove();
			}
			$("#planBody" + index).remove()
		} else {
			if (buttons == 2) {//删除的是最后一个
				//前一个节点的id
				var preId = $("#planBody" + index).prev().attr("id");
				var indexPre = preId.replace('planBody', '');
				var html = '<button class="btn btn-info ws-btnBlue" type="button" onclick="addPlan(this,'
						+ indexPre + ')">添加</button>';
				html += '\n<button class="btn btn-default" type="button" onclick="delPlan(this,'
						+ indexPre + ')">删除</button>';
				//前一个节点设置添加和删除按钮
				$("#planBody" + indexPre).find(".ws-plan-btn").html(html);
			}
			$("#planBody" + index).remove()
		}
	}
	//编辑周报
	function editWeekReport(state) {
		if (state == 1) {//修改界面
			$(".addWeekReport").show();
			$(".viewWeekReport").hide();
			$("#workTrace").css("display", "block")

			$(".optAddWeekReport").css("display", "block");
			$(".optViewWeekReport").css("display", "none");

		} else if (state == 0) {//不修改了
			$(".addWeekReport").hide();
			$(".viewWeekReport").show();
			$("#workTrace").css("display", "none")

			$(".optAddWeekReport").css("display", "none");
			$(".optViewWeekReport").css("display", "block");

		}
	}
	//保存周报 1是存草稿 0是发布
	function save(state, ts) {
		//若是一个都没有填写，不能发布
		if (state == 0) {
			//是否能发布，默认不能
			var flag = true;
			var areas = $(".addWeekReport").find("textarea[isRequire='1']");
			if(areas && areas.get(0)){
				$.each(areas,function(index,obj){
					var val = $(obj).val();
					if(!val || val.replace(/\s+/g, "").length == 0){
						scrollToLocation($("#contentBody"),$(obj))
						layer.tips("请填写内容！",$(obj),{tips:1});
						$(obj).focus();
						flag = false ;
						return false;
					}
				})
			}
			//下周计划信息
			var plans = $("#weekPlan").find("textarea");
			if(plans && plans.get(0) && flag){
				$.each(plans,function(index,obj){
					var val = $(obj).val();
					if(!val || val.replace(/\s+/g, "").length == 0){
						scrollToLocation($("#contentBody"),$(obj))
						layer.tips("请填写下周计划！",$(obj),{tips:1});
						$(obj).focus();
						flag = false ;
						return false;
					}
				})
			}
			var isWeekOk = $("#weekBunList").data("isWeekOk");
			if(isWeekOk.status=='yes'){
			}else if(isWeekOk.status=='no'){
				 showNotification(2,isWeekOk.info);
				 return;
			}else{
				 showNotification(2,isWeekOk.info);
				 return;
			}
			
			if (!flag) {
				return false;
				window.top.layer.confirm("周报未填写！<br>不能发布！<br>确定保存？", {
					title : "询问框",
					btn : [ "确定", "取消" ],
					icon : 3
				}, function(index) {
					window.top.layer.close(index);
					$("#state").attr("value", 1);
					$(ts).attr("disabled", "disabled")
					$("#weekRepForm").submit();
				});
			} else {
				checkLeader('${param.sid}',function(data){
					 if(data.status=='f'){
						   showNotification(2,data.info);
					   }else if(data.status=='f1'){
						   window.top.layer.confirm("直属上级未设定", {
								title : "警告",
								btn : [ "设定", "取消" ],
								icon : 0
							}, function(index) {
								window.top.layer.close(index);
								if(openPageTag=='week'){
									$(openWindowDoc).find("#weekRepSetBtn").click();
								}else{
									openWindow.setWeekRepViewScope('${param.sid}');
								}
							});
						  
					   }else{
								$("#state").attr("value", 0);
								$(ts).attr("disabled", "disabled");
								$("#weekRepForm").submit();
					   }
				});
			}
		} else {//存草稿无所谓填没填
			$("#state").attr("value", 1);
			$(ts).attr("disabled", "disabled")
			$("#weekRepForm").submit();
		}

	}
	//查看工作轨迹
	function viewWorkTrace(dateS, dateE) {
		var d1 = dateS.replace('年', '-').replace('月', '-').replace('日', '');
		var d2 = dateE.replace('年', '-').replace('月', '-').replace('日', '');
		var url = '/systemLog/exportWorkTrace?sid=${param.sid}&type=006&startDate='
				+ d1 + '&endDate=' + d2;
		window.top.layer.open({
			type : 2,
			title : false,
			closeBtn : 0,
			shadeClose : true,
			shade : 0.3,
			btn : [ '关闭' ],
			shift : 0,
			fix : true, //固定
			maxmin : false,
			move : false,
			area : [ '750px', '450px' ],
			content : [ url, 'no' ]
		//iframe的url
		});
	}
	function addWeekRep(ts) {
		if(openPageTag=='week'){
			openWindow.addWeekReport($(ts).val())
		}else{
			window.location.replace('/weekReport/addWeekRepPage?sid=${param.sid}&chooseDate='+encodeURIComponent($(ts).val()));
		}
	}
	//验证周报数是否正确
	function checkWeekNum(weekData,callback){
		 $.ajax({
		   type: "POST",
		   dataType: "json",
		   url: "/weekReport/checkWeekNum?sid=${param.sid}",
		   data:{"chooseDateStr":weekData},
		   success: function(data){
			  //若是有回调
			  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
				  callback(data);
			  }
		   }
		})
	}
</script>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
				<div class="widget no-margin-bottom" style="margin-top: 0px;">
					<div
						class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption"> <strong
							class="ps-font-size padding-right-10">
							<a>${weekReport.userName} </a>
								${weekReport.year}年第${weekReport.weekNum}周周报 </strong>
							<select class="no-padding" id="weekBunList" style="cursor:auto;width: 150px" onchange="addWeekRep(this)">
									<c:if test="${not empty preWeekS}">
										<option value="${preWeekS}">
												${fn:substring(preWeekS,5,fn:length(preWeekS))} ~ ${fn:substring(preWeekE,5,fn:length(preWeekE))}
										</option>
									</c:if>
									<option value="${firstDayOfWeek}" selected="selected">
										${fn:substring(firstDayOfWeek,5,fn:length(firstDayOfWeek))} ~ ${fn:substring(lastDayOfWeek,5,fn:length(lastDayOfWeek))}
									</option>
									<c:if test="${not empty nextWeekS}">
										<option value="${nextWeekS}">
												${fn:substring(nextWeekS,5,fn:length(nextWeekS))} ~ ${fn:substring(nextWeekE,5,fn:length(nextWeekE))}
										</option>
									</c:if>
							</select>
						</span>
						<div class="widget-buttons ps-toolsBtn">
							<c:choose>
								<c:when test="${weekReport.countVal>0 && weekReport.state==0}">
									<!--添加 -->
									<div class="optAddWeekReport"
										style="clear:both;text-align: center;display: none">
										<input type="button" class="btn btn-primary btn-sm "
											onclick="save(0,this);" value="发布周报" />
										<c:if test="${weekReport.state==1}">
											<input type="button" class="btn btn-warning btn-sm"
												onclick="save(1,this);" value="存草稿" />
										</c:if>
										<c:if test="${weekReport.state==0}">
											<input type="reset" class="btn btn-default btn-sm"
												onclick="editWeekReport(0);" value="取消" />
										</c:if>


										<c:set var="display">
											<c:choose>
												<c:when
													test="${not empty weekReport && (weekReport.state!=0 || (weekReport.countVal==0 && weekReport.state==0))}">
												block
												</c:when>
												<c:otherwise>
												none
												</c:otherwise>
											</c:choose>
										</c:set>
										<div class="pull-right padding-left-20" id="workTrace"
											style="clear:both;text-align: center;display: ${display}">
											<button class="btn bg-blue btn-sm white" style="border: 0 0"
												type="button"
												onclick="viewWorkTrace('${firstDayOfWeek}','${lastDayOfWeek}')">本周工作</button>
										</div>

									</div>

									<c:if test="${empty editWeek}">
										<div class="optViewWeekReport"
											style="clear:both;text-align: center;">
											<input type="button" class="btn btn-warning btn-sm"
												onclick="editWeekReport(1)" value="修改周报" />
										</div>
									</c:if>


								</c:when>
								<c:otherwise>
									<!-- 查看 -->
									<div class="optAddWeekReport"
										style="clear:both;text-align: center;">
										<input type="button" class="btn btn-primary btn-sm "
											onclick="save(0,this);" value="发布周报" />
										<c:if test="${weekReport.state==1}">
											<input type="button" class="btn btn-warning btn-sm"
												onclick="save(1,this);" value="存草稿" />
										</c:if>
										<c:if test="${weekReport.state==0}">
											<input type="reset" class="btn btn-default btn-sm"
												onclick="editWeekReport(0);" value="取消" />
										</c:if>

										<c:set var="display">
											<c:choose>
												<c:when
													test="${not empty weekReport && (weekReport.state!=0 || (weekReport.countVal==0 && weekReport.state==0))}">
												block
												</c:when>
												<c:otherwise>
												none
												</c:otherwise>
											</c:choose>
										</c:set>
										<div class="pull-right padding-left-20" id="workTrace"
											style="clear:both;text-align: center;display: ${display}">
											<button class="btn bg-blue btn-sm white" style="border: 0 0"
												type="button"
												onclick="viewWorkTrace('${firstDayOfWeek}','${lastDayOfWeek}')">本周工作</button>
										</div>

									</div>
									<c:if test="${empty editWeek}">
										<div class="optViewWeekReport"
											style="clear:both;text-align: center;display: none">
											<input type="button" class="btn btn-warning date-set btn-sm"
												onclick="editWeekReport(1)" value="修改周报" />
										</div>
									</c:if>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i> </a>

						</div>
					</div>
					<!--Widget Header-->
					<c:choose>
						<c:when test="${weekReport.state==0}">
							<!-- 已经汇报 -->
							<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
							<div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;padding:0 12px;">
								<div class="widget-body no-shadow"
									style="display:${weekReport.state==0?'block':'none' }">
									<div class="widget-main ">
										<div class="tabbable">
											<ul class="nav nav-tabs tabs-flat">
		                                            <li class="active">
		                                            	<a href="javascript:void(0)" id="reportContentMenuLi" data-toggle="tab">汇报内容</a>
													</li>
		                                            <li>
		                                            	<a href="javascript:void(0)" id="weekTalk" data-toggle="tab">留言<c:if test="${weekReport.talkNum > 0}"><span style="color:red;font-weight:bold;">（${weekReport.talkNum}）</span></c:if></a>
													</li>
												<li>
													<a href="javascript:void(0)" data-toggle="tab"
														id="weekLog">周报日志</a>
												</li>
												<li>
													<a href="javascript:void(0)" data-toggle="tab"
														id="weekFile">周报文档<c:if test="${weekReport.fileNum > 0}"><span style="color:red;font-weight:bold;">（${weekReport.fileNum}）</span></c:if></a>
												</li>
												<li>
													<a data-toggle="tab" href="javascript:void(0)"
														id="weekViewRecord">阅读情况</a>
												</li>
												<%--<li>--%>
													<%--<a data-toggle="tab" href="javascript:void(0)"--%>
														<%--id="weekViewRecord">最近查看</a>--%>
												<%--</li>--%>
											</ul>
											<div class="tab-content tabs-flat">
		                                    	 	<div id="reportContent" style="display:block;">
														<jsp:include page="./reportBase_edit.jsp"></jsp:include>
													</div>
		                                    	 	<iframe id="otherIframe" style="display:none;" class="layui-layer-load"
													src="/weekReport/weekRepTalkPage?sid=${param.sid}&pager.pageSize=10&weekReportId=${weekReport.id}"
													border="0" frameborder="0" allowTransparency="true" noResize
													scrolling="no" width=100% height=100% vspale="0"></iframe>
											</div>
										</div>
									</div>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<!-- 未汇报 -->
							<jsp:include page="./reportBase_edit.jsp"></jsp:include>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
	<script src="/static/assets/js/bootstrap.min.js"></script>
	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>

