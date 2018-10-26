<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<%pageContext.setAttribute("requestURI",request.getRequestURI().replace("/","_").replace(".","_"));%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
$(function(){
	initCard('${param.sid}');
	//刷新
	$("#refreshImg").click(function(){
		$("#searchForm").submit();
	});
	//名称筛选
	$("#content").blur(function(){
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#content").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#content").val())){
        		$("#searchForm").submit();
        	}
        }
    });
	//文本框绑定回车提交事件
	$("#content").blur(function(event){
		$("#searchForm").submit();
    });
	//更多筛选条件显示层
	$("#moreFilterCondition").click(function(){
        var display = $("#moreFilterCondition_div").css("display");
        if("none"==display){
            $(this).html('隐藏');
            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
        }else if("block"==display){
            $(this).html('更多筛选')
            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
        }

      });
	var timeoutProcess;
	//鼠标移到modTypeDiv区域时，显示下拉菜单
	$("#modTypeDiv").mouseover(function(e){
		evt = window.event || e; 
		var obj = evt.toElement || evt.relatedTarget; 
		var pa = this; 
		if(pa.contains(obj)){
			clearTimeout(timeoutProcess);
			return false; 
		} 
	});
	//点击<li>的时候不考虑checkbox
	$("#modTypeId li").click(function(e){
		var evt = e?e:window.event;
		var ele = evt.srcElement || evt.target;
		var type = ele.type;
		if(type && type.toLowerCase()=='checkbox'){
			var id = ele.id;
			if(id && id=='allModType'){
				setIsCheckAll('searchForm','allModType','modTypeA','modTypeId','modTypes');
			}else{
				var checkbox = $(this).find("input[type='checkbox']");
				if(!$(checkbox).attr("checked")){
					$("#allModType").attr("checked",false);
				}
			}
			return;
		}else{
			$("#modTypeA").parent().addClass("open");
			var checkbox = $(this).find("input[type='checkbox']");
			if($(checkbox).attr("id")){//点击的是全选
				if($(checkbox).attr("checked")){//复选框是全选已选中，则取消选中
					$(checkbox).attr("checked",false);
				}else{
					$(checkbox).attr("checked",true);
				}
				setIsCheckAll('searchForm','allModType','modTypeA','modTypeId','modTypes');
			}else{
				if($(checkbox).attr("checked")){
					$(checkbox).attr("checked",false);
					$("#allModType").attr("checked",false);
				}else{
					$(checkbox).attr("checked",true);
				}
			}
		}
	})
	//鼠标移出modTypeDiv区域时，隐藏下拉菜单
	$("#modTypeDiv").mouseout(function(e){
		evt = window.event || e; 
		var obj = evt.toElement || evt.relatedTarget; 
		var pa = this; 
		if(pa.contains(obj)){
			return false; 
		} 
		timeoutProcess = setTimeout(function(){
			var pObj = $("#modTypeA").parent();
			$(pObj).removeClass("open");
		},200)
	});
	//modTypeId区域移除后若有变动查询
	$("#modTypeId").mouseout(function(e){ 
		evt = window.event || e; 
		var obj = evt.toElement || evt.relatedTarget; 
		var pa = this; 
		if(pa.contains(obj)) return false; 
		var diff=0;
		var array = $("#searchForm").find(":checkbox[name='modTypes']:checked");
		//所选项
		var modTypes = new Array();
		if(array && array.length>0){
			for(var i=0;i<array.length;i++){
				modTypes.push($(array[i]).val());
				if(!$(array[i]).attr("prechecked")){//本次有上次没有选择的
					diff+=1;
				}
				
			}
		}
		var arrayPre = $("#searchForm").find(":checkbox[name='modTypes'][prechecked='1']");
		//所选项
		var modTypePre = new Array();
		if(arrayPre && arrayPre.length>0){
			for(var i=0;i<arrayPre.length;i++){
				if(!$(array[i]).attr("checked")){//本次有上次没有选择的
					diff+=1;
				}
			}
		}
		if(diff>0){
			submitForm();
		}else{
			$("#modTypeA").parent().removeClass("open");
		}
	});
    
});
/**
 * 选择日期
 */
function submitForm(){
	$("#searchForm").submit();
}

/**
 * 查看待办事项的详情
 * @param busId 业务主键
 * @param busType 业务类型
 * @param isClock 是否为闹铃
 * @param clockId 闹铃主键
 * @return
 */
function viewTodoDetail(id,busId,busType,isClock,clockId){
	var url ="/msgShare/viewTodoDetailPage?sid=${param.sid}&id="+id;
	url += "&busId="+busId+"&busType="+busType+"&isClock="+isClock+"&clockId="+clockId
	url += "&redirectPage="+encodeURIComponent(window.location.href);
	window.location.href=url;
}
/**
 * 查看详情
 *id 业务主键
 *type 业务类型
 *clockId 闹铃主键
 *msgid 提醒主键
 */
function viewTodoDetai(id,type,clockId,msgid){
	if(clockId>0 && msgid>0){
		var url ="/msgShare/viewTodoDetailPage?sid=${param.sid}&id="+msgid;
		url += "&busId="+id+"&busType="+type+"&isClock=1&clockId="+clockId
		url += "&redirectPage="+encodeURIComponent(window.location.href);
		window.location.href=url;
	}else if('012'==type){//客户
		$.post("/crm/authorCheck?sid=${param.sid}",{Action:"post",customerId:id},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
				}else{
					window.location.href="/crm/viewCustomer?sid=${param.sid}&id="+id+"&clockId="+clockId+"&redirectPage="+encodeURIComponent(window.location.href);
				}
		},"json");
	}else if('005'==type){//项目
		$.post("/item/authorCheck?sid=${param.sid}",{Action:"post",itemId:id},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
				}else{
					window.location.href="/item/viewItemPage?sid=${param.sid}&id="+id+"&clockId="+clockId+"&redirectPage="+encodeURIComponent(window.location.href);
				}
		},"json");
	}else if('003'==type){//任务
		$.post("/task/authorCheck?sid=${param.sid}",{Action:"post",taskId:id},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
				}else{
					window.location.href="/task/viewTask?sid=${param.sid}&id="+id+"&clockId="+clockId+"&redirectPage="+encodeURIComponent(window.location.href);
				}
		},"json");
	}else if('006'==type){//周报
		$.post("/weekReport/authorCheck?sid=${param.sid}",{Action:"post",weekReportId:id},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
				}else{
					window.location.href="/weekReport/viewWeekReport?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
				}
		},"json");
	}else if('050'==type){//分享
        $.post("/daily/authorCheck?sid=${param.sid}",{Action:"post",weekReportId:id},
            function (msgObjs){
                if(!msgObjs.succ){
                    showNotification(1,msgObjs.promptMsg);
                }else{
                    window.location.href="/daily/viewDaily?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
                }
            },"json");
    }else if('004'==type){//投票
		$.post("/vote/authorCheck?sid=${param.sid}",{Action:"post",voteId:id},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
				}else{
					window.location.href="/vote/voteDetail?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
				}
		},"json");
	}else if('011'==type){//问答
		window.location.href="/qas/viewQuesPage?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
	}else if('015'==type){//人员申请
		window.location.href="/userInfo/listPagedForCheck?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
	}else if('017'==type){//会议申请
		$.post("/meeting/checkMeetTimeOut?sid=${param.sid}",{Action:"post",meetId:id},     
				function (data){
				if(data.status=='y' && data.organiser>0){
					if(data.timeOut==0){//会议已结束
						if(${userInfo.id}==data.recorder){//会议记录人员添加会议纪要
							window.location.href="/meeting/addSummaryPage?sid=${param.sid}&meetingId="+id+"&redirectPage=/meeting/listPagedDoneMeeting?sid=${param.sid}";
						}else{//非会议记录人员，查看会议+纪要
							window.location.href="/meeting/viewSummaryPage?sid=${param.sid}&meetingId="+id+"&redirectPage=/meeting/listPagedDoneMeeting?sid=${param.sid}";
						}
					}else if(data.timeOut==1){//会正在进行
						if(${userInfo.id}==data.organiser){//会议发布人员邀请参会人员
							window.location.href="/meeting/invMeetUserPage?sid=${param.sid}&meetingId="+id+"&redirectPage=/meeting/listPagedTodoMeeting?sid=${param.sid}";
						}else{//非会议发布人员查看会议
							window.location.href="/meeting/viewMeetingPage?sid=${param.sid}&meetingId="+id+"&redirectPage=/meeting/listPagedTodoMeeting?sid=${param.sid}";
						}
					}else{
						if(${userInfo.id}==data.organiser){//会议发布人员修改会议信息
							window.location.href="/meeting/updateMeetingPage?sid=${param.sid}&batchUpdate=1&id="+id+"&redirectPage=/msgShare/listPagedMsgNoRead?sid=${param.sid}";
						}else{//非会议发布人员，查看会议信息
							window.location.href="/meeting/viewMeetingPage?sid=${param.sid}&meetingId="+id+"&redirectPage=/msgShare/listPagedMsgNoRead?sid=${param.sid}";
						}
					}
				}else if(data.status=='y' && data.organiser==0){
					showNotification(1,"会议已删除");
				}else{
					showNotification(1,data.info);
				}
		},"json");
	}else if('018'==type){//会议申请
		$.post("/meeting/authCheckMeetRoom?sid=${param.sid}",{Action:"post",meetingId:id},     
				function (data){
				if(data.status=='y'){
					window.location.href="/meetingRoom/listPagedApplyForCheck?sid=${param.sid}&meetingId="+id+"&redirectPage=/msgShare/listPagedMsgNoRead?sid=${param.sid}";
				}else{
					showNotification(1,data.info);
				}
		},"json");
	}else if('1'==type){//分享
		window.location.href="/msgShare/msgShareViewPage?sid=${param.sid}&id="+id+"&type="+type;
	}
}
</script>
<style type="text/css">
#modTypeId input{
margin-left: 8px
}
</style>
</head>
<body>
<!-- 系统头部装载 -->
<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<!-- 左边菜单 -->
<jsp:include page="/WEB-INF/jsp/include/left_menu.jsp"></jsp:include>
<!--main content start-->
<section id="main-content" style="margin-right:20px;">
	<!--任务按钮-->
	<div class="wrapper wrapper3">
		<div class="ws-wrapIn">
			<h5 class="ws-title2">待办事项列表</h5>
			<div class="ws-wrapContent">
				<header class="panel-heading">
					<form id="searchForm" action="/msgShare/listPagedMsgTodo">
					 <input type="hidden" name="redirectPage"/>
					 <input type="hidden" name="pager.pageSize" value="15"/>
					 <input type="hidden" id="busType" name="busType" value="${todayWorks.busType }"/>
					 <input type="hidden" name="sid" value="${param.sid}"/>
						<div class="ws-headBox">
							<div class="pull-left ws-toolbar">
								<div class="dropdown pull-left" id="modTypeDiv">
								 <a id="modTypeA" class="dropdown-toggle" href="javascript:void(0)" onclick="displayMenu('modTypeA')">
									 		<c:choose>
									 			<c:when test="${not empty modList }">
									 			<font style="font-weight:bold;">
										 			<c:forEach items="${modList}" var="modTypeObj" varStatus="vs">
										 			<c:if test="${modTypeObj=='003'}">任务模块</c:if>
										 			<c:if test="${modTypeObj=='005'}">项目模块</c:if>
										 			<c:if test="${modTypeObj=='006'}">周报模块</c:if>
													<c:if test="${modTypeObj=='050'}">分享模块</c:if>
										 			<c:if test="${modTypeObj=='012'}">客户模块</c:if>
										 			<c:if test="${modTypeObj=='015'}">人员申请</c:if>
										 			<c:if test="${modTypeObj=='017'}">会议确认</c:if>
										 			<c:if test="${modTypeObj=='018'}">会议申请</c:if>
										 			<c:if test="${modTypeObj=='101'}">闹铃模块</c:if>
										 			<c:if test="${!vs.last}">,</c:if>
										 			<c:if test="${vs.count mod 3 eq 0 && not vs.last }"><br></c:if>
										 			</c:forEach>
										 			</font>
									 			</c:when>
									 			<c:otherwise>
									 			全部模块
									 			</c:otherwise>
									 		</c:choose>
												<b class="caret"></b>
											</a>
											<c:set var="isAll">
												<c:choose>
													<c:when test="${userInfo.admin>0 && fn:length(modList)==6 }">1</c:when>
													<c:when test="${userInfo.admin eq 0 && fn:length(modList)==5}">1</c:when>
													<c:otherwise>0</c:otherwise>
												</c:choose>
											</c:set>
											<ul class="dropdown-menu extended logout" id="modTypeId">
												<li><a href="javascript:void(0);" onfocus="this.blur();">全部模块<input type="checkbox" id="allModType" ${isAll eq 1?'checked':''}></a></li>
												<li><a href="javascript:void(0);" onfocus="this.blur();">任务模块<input type="checkbox" name="modTypes" value="003" ${fn:contains(modList,'003')?'checked prechecked="1"':''}></a></li>
												<li><a href="javascript:void(0);" onfocus="this.blur();">项目模块<input type="checkbox" name="modTypes" value="005" ${fn:contains(modList,'005')?'checked prechecked="1"':''}></a></li>
												<li><a href="javascript:void(0);" onfocus="this.blur();">周报模块<input type="checkbox" name="modTypes" value="006" ${fn:contains(modList,'006')?'checked prechecked="1"':''}></a></li>
												<li><a href="javascript:void(0);" onfocus="this.blur();">分享模块<input type="checkbox" name="modTypes" value="050" ${fn:contains(modList,'050')?'checked prechecked="1"':''}></a></li>
												<li><a href="javascript:void(0);" onfocus="this.blur();">客户模块<input type="checkbox" name="modTypes" value="012" ${fn:contains(modList,'012')?'checked prechecked="1"':''}></a></li>
												<c:if test="${userInfo.admin>0}">
												<li><a href="javascript:void(0);" onfocus="this.blur();">人员申请<input type="checkbox" name="modTypes" value="015" ${fn:contains(modList,'015')?'checked prechecked="1"':''}></a></li>
												</c:if>
												<li><a href="javascript:void(0);" onfocus="this.blur();">会议申请<input type="checkbox" name="modTypes" value="017" ${fn:contains(modList,'017')?'checked prechecked="1"':''}></a></li>
												<c:if test="${countRoom>0 }">
												<li><a href="javascript:void(0);" onfocus="this.blur();">会议申请<input type="checkbox" name="modTypes" value="018" ${fn:contains(modList,'018')?'checked prechecked="1"':''}></a></li>
												</c:if>
												<li><a href="javascript:void(0);" onfocus="this.blur();">闹铃模块<input type="checkbox" name="modTypes" value="101" ${fn:contains(modList,'101')?'checked prechecked="1"':''}></a></li>
											</ul>
								</div>
								<a href="javascript:void(0);" class="pull-left" id="moreFilterCondition">${(not empty todayWorks.startDate || not empty todayWorks.endDate)?'隐藏':'更多筛选'}</a>
							</div>
							<div class="ws-floatRight">
								<ul class="ws-icon">
										<li>
											<input id="content" name="content" value="${todayWorks.content}" type="text" class="form-control search ws-search-one" placeholder="名称检索" title="名称检索"/>
										</li>
									</ul>
							</div>
							<div class="ws-clear"></div>
						</div>
						<div class="ws-none" id="moreFilterCondition_div" style="display:${(not empty todayWorks.startDate || not empty todayWorks.endDate)?'block':'none'};">
							<div class="form-inline">
								<div class="form-group">
									<div class="input-group input-large">
										<input class="form-control dpd1" type="text" readonly="readonly" value="${todayWorks.startDate}" id="startDate" name="startDate" placeholder="开始时间"  onFocus="WdatePicker({onpicked:function(){submitForm()},oncleared: function(){submitForm()},dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
										<span class="input-group-addon">To</span>
										<input class="form-control dpd2" type="text" readonly="readonly" value="${todayWorks.endDate}" id="endDate"  name="endDate" placeholder="结束时间" onFocus="WdatePicker({onpicked:function(){submitForm()},oncleared: function(){submitForm()},dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})"/>
									</div>
								</div>
							</div>
						</div>
						</form>
				</header>
				<div class="panel-body">
				 	<form  method="post" id="delForm">
					 <input type="hidden" name="redirectPage"/>
					 <input type="hidden" name="sid" value="${param.sid}"/>
					<table class="table table-hover general-table">
						<thead>
							<tr>
							  <th width="6%" valign="middle">&nbsp;</th>
							  <th width="6%" valign="middle" style="text-align: center;">序号</th>
							  <th width="9%" valign="middle" style="text-align: center;">紧急程度</th>
							  <th width="9%" valign="middle">办理时限</th>
							  <th valign="middle">事项名称</th>
							  <th width="11%" valign="middle">推送人</th>
							  <th width="15%" valign="middle" style="text-align: center;">推送时间</th>
							</tr>
						</thead>
						<tbody>
						<c:choose>
					 	<c:when test="${not empty list}">
					 		<c:forEach items="${list}" var="obj" varStatus="status">
					 			<tr>
					 				<td valign="middle">
					 					<c:choose>
					 						<c:when test="${obj.isClock==1}">
					 							<a href="javascript:void(0);" class="fa fa-clock-o" style="margin:0px 17px;width: 20px;cursor:default"></a>
					 						</c:when>
											<c:when test="${obj.busType=='003' || obj.busType=='004' || obj.busType=='005'  || obj.busType=='011' || obj.busType=='012'}">
												<a href="javascript:void(0);" class="fa ${obj.attentionState==0?'fa-star-o':'fa-star ws-star' } " style="margin:0px 15px;width: 20px" title="${obj.attentionState==0?'标识关注':'取消关注'}" 
												attentionState="${obj.attentionState }" busType="${obj.busType}"  busId="${obj.busId}" describe="1" iconSize="sizeMd"
												onclick="changeAtten('${param.sid}',this)"></a>
											</c:when>
										</c:choose>
					 				</td>
					 				<td valign="middle" style="text-align: center;">${status.count}</td>
					 				<td valign="middle" style="text-align: center;">
					 					<c:set var="gradeColor">
								 			<c:choose>
								 				<c:when test="${obj.grade==4}">#FF0000</c:when>
								 				<c:when test="${obj.grade==3}">#ffa500</c:when>
								 				<c:when test="${obj.grade==2}">#00bfff</c:when>
								 				<c:when test="${obj.grade==1}">#008000</c:when>
								 				<c:otherwise>
								 					#008000
								 				</c:otherwise>
								 			</c:choose>
								 		</c:set>
								 		<div style="text-align: center; color: ${gradeColor};">
								 			<c:choose>
								 				<c:when test="${obj.grade>0 && obj.isClock==0}">
								 					<tags:viewDataDic type="grade" code="${obj.grade}"></tags:viewDataDic>
								 				</c:when>
								 				<c:otherwise>
								 					--
								 				</c:otherwise>
								 			</c:choose>
										</div>
					 				</td>
					 				<td valign="middle">${obj.dealTimeLimit}</td>
					 				<td valign="middle">
					 					<c:choose>
											<c:when test="${obj.isClock=='1'}">
												[闹铃]
											</c:when>
											<c:when test="${obj.busType=='015'}">
												[加入申请]
											</c:when>
											<c:when test="${obj.busType=='017'}">
												[会议确认]
											</c:when>
											<c:when test="${obj.busType=='018'}">
												[会议申请]
											</c:when>
											<c:otherwise>
												[${fn:substring(obj.moduleTypeName,0,2)}]
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${obj.isClock=='1'}">
						 					<a href="javascript:void(0)" onclick="viewTodoDetail(${obj.id},${obj.busId}, '${obj.busType}', '${obj.isClock}','${obj.clockId}')" class="${obj.readState==0?'noread':'' }">
												<tags:cutString num="57">
													${fn:substring(obj.content,5,fn:length(obj.content))}
												</tags:cutString> 	
											</a>
											</c:when>
											<c:otherwise>
							 					<a href="javascript:void(0)" onclick="viewTodoDetai(${obj.busId},'${obj.busType}',0)" class="${obj.readState==0?'noread':'' }">
													<tags:cutString num="57">${obj.busTypeName}</tags:cutString> 	
												</a>
											</c:otherwise>
										</c:choose>
					 				</td>
					 				<td valign="middle">
					 					<div class="ticket-user pull-left other-user-box" data-container="body" data-toggle="popover" data-placement="left" 
		 								 data-user='${obj.modifyer}' data-busId='${obj.busId}' data-busType='${obj.busType}'>
										<img src="/downLoad/userImg/${obj.comId}/${obj.modifyer}?sid=${param.sid}"
											title="${obj.modifyerName}" class="user-avatar" />
										<i class="user-name">${obj.modifyerName}</i>
										</div>
					 				</td>
					 				<td valign="middle" style="text-align: center;">
					 					${fn:substring(obj.recordCreateTime,0,20)}
					 				</td>
					 			</tr>
					 		</c:forEach>
					 	</c:when>
					 	<c:otherwise>
					 		<tr>
					 			<td height="25" colspan="9" align="center"><h3>没有相关信息！</h3></td>
					 		</tr>
					 	</c:otherwise>
					 </c:choose>
						</tbody>
					</table>
					</form>
               		<tags:pageBar url="/msgShare/listPagedMsgTodo"></tags:pageBar>
				</div>
			</div>
		</div>
	</div>

</section>
</body>
</html>
