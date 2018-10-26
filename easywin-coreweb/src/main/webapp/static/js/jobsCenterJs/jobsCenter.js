$(function() {
	//刷新
	$("#refreshImg").click(function() {
		$("#searchForm").submit();
	});
	//名称筛选
	$("#content").blur(function() {
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#content").bind("keypress", function(event) {
		if (event.keyCode == "13") {
			if (!strIsNull($("#content").val())) {
				$("#searchForm").submit();
			}
		}
	});
	//文本框绑定回车提交事件
	$("#content").blur(function(event) {
		$("#searchForm").submit();
	});

});
/**
 * 选择日期
 */
function submitForm() {
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
function viewTodoDetail(id, busId, busType, isClock, clockId,sid) {
	var url = "/msgShare/viewTodoDetailPage?sid="+sid+"&id=" + id;
	url += "&busId=" + busId + "&busType=" + busType + "&isClock=" + isClock
			+ "&clockId=" + clockId
	url += "&redirectPage=" + encodeURIComponent(window.location.href);
	if(busType=='015'){
		checkApplyInUser(id);
	}else{
		//办理事项
		openWinByRight(url);
	}
}
/**
 * 查看详情
 *id 业务主键
 *type 业务类型
 *clockId 闹铃主键
 *msgid 提醒主键
 */
function viewTodoDetai(id, type, clockId, msgid,operator,sid,msgid) {
	if(clockId>0 && msgid>0){
		var url ="/msgShare/viewTodoDetailPage?sid="+sid+"&id="+msgid;
		url += "&busId="+id+"&busType="+type+"&isClock=1&clockId="+clockId
		url += "&redirectPage="+encodeURIComponent(window.location.href);
		//办理事项
		openWinByRight(url);
	}else if('003'==type//任务
			|| '012'==type//客户
        	|| '080'==type//产品
			|| '005'==type){//项目
		viewModInfo(id,type,clockId)
	}else if('006'==type){//周报
		viewModInfo(id,type,clockId);
	}else if('050'==type){//分享
        viewModInfo(id,type,clockId);
    }else if('004'==type){//投票
		viewModInfo(id,type,clockId);
	}else if('040'==type){//投票
		var url = "/institution/viewInstitu?sid=" + sid + "&id=" + id;
		openWinByRight(url);
	}else if('011'==type){//问答
		var url="/qas/viewQuesPage?sid="+sid+"&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
		//办理事项
		openWinByRight(url);
	}else if('015'==type){//人员申请
		checkApplyInUser(id);
	}else if('017'==type){//会议申请
		$.post("/meeting/checkMeetTimeOut?sid="+sid,{Action:"post",meetId:id},     
				function (data){
				if(data.status=='y' && data.organiser>0){
					if(data.timeOut==0){//会议已结束
						if(operator==data.recorder){//会议记录人员添加会议纪要
							var url="/meetSummary/addSummaryPage?sid="+sid+"&meetingId="+id+"&redirectPage=/meeting/listPagedDoneMeeting?sid="+sid;
							//办理事项
							openWinByRight(url);
						}else{//非会议记录人员，查看会议+纪要
							var url="/meetSummary/viewSummaryPage?sid="+sid+"&meetingId="+id+"&redirectPage=/meeting/listPagedDoneMeeting?sid="+sid;
							//办理事项
							openWinByRight(url);
						}
					}else if(data.timeOut==1){//会正在进行
						if(operator==data.organiser){//会议发布人员邀请参会人员
							var url="/meeting/invMeetUserPage?sid="+sid+"&meetingId="+id+"&redirectPage=/meeting/listPagedTodoMeeting?sid="+sid;
							//办理事项
							openWinByRight(url);
						}else{//非会议发布人员查看会议
							var url="/meeting/viewMeetingPage?sid="+sid+"&meetingId="+id+"&redirectPage=/meeting/listPagedTodoMeeting?sid="+sid;
							//办理事项
							openWinByRight(url);
						}
					}else{
						if(operator==data.organiser){//会议发布人员修改会议信息
							var url="/meeting/updateMeetingPage?sid="+sid+"&batchUpdate=1&id="+id+"&redirectPage=/msgShare/listPagedMsgNoRead?sid="+sid;
							//办理事项
							openWinByRight(url);
						}else{//非会议发布人员，查看会议信息
							var url="/meeting/viewMeetingPage?sid="+sid+"&meetingId="+id+"&redirectPage=/msgShare/listPagedMsgNoRead?sid="+sid;
							//办理事项
							openWinByRight(url);
						}
					}
				}else if(data.status=='y' && data.organiser==0){
					showNotification(1,"会议已删除");
				}else{
					showNotification(1,data.info);
				}
		},"json");
	}else if('018'==type){//会议申请
		$.post("/meeting/authCheckMeetRoom?sid="+sid,{Action:"post",meetingId:id},     
				function (data){
				if(data.status=='y'){
					var url="/meetingRoom/listPagedApplyForCheck?sid="+sid+"&meetingId="+id+"&redirectPage=/msgShare/listPagedMsgNoRead?sid="+sid;
					//办理事项
					openWinByRight(url);
				}else{
					showNotification(1,data.info);
				}
		},"json");
	}else if('022'==type||type == '02201'){//审批
		var url = "/workFlow/viewSpFlow?sid="+sid+"&instanceId="+id;
		openWinByRight(url);
	}else if('027010'==type||type == '027011'){//采购审核
		var url = "/bgypPurOrder/viewBgypPurOrderPage?sid="+sid+"&purOrderId="+id;
		openWinByRight(url);
	}else if('027020'==type||type == '027021'){//申领审核
		var url = "/bgypApply/viewBgypApplyPage?sid="+sid+"&applyId="+id;
		openWinByRight(url);
	}else if('1'==type){//分享
		var url="/msgShare/msgShareViewPage?sid="+sid+"&id="+id+"&type="+type;
		openWinByRight(url);
	}else if('00306'==type){//任务报延
		var url = "/task/delayApplyDetail?sid="+sid+"&applyId="+id;
		openWinByRight(url);
	}else if('046'==type){//会议审批
		var url = "/meeting/viewMeetingPage?sid="+sid+"&meetingId="+id;
		openWinByRight(url);
	}else if('047'==type){//会议纪要审批
		var url = "/meetSummary/viewSummaryById?sid="+sid+"&summaryId="+id;
		openWinByRight(url);
	}else if('099'==type){//催办
		var url = "/busRemind/viewBusRemindPage?sid="+sid+"&id="+id;
		openWinByRight(url);
	}else if('0103'==type){//领款通知
		var url = "/balance/viewNotices?sid="+sid+"&id="+msgid + "&busId="+id+"&busType="+type;
		openWinByRight(url);
	}else if('068'==type){//外部联系人
		var url = "/outLinkMan/viewOlmPage?sid="+sid+"&id="+id;
		openWinByRight(url);
	}else if('06602'==type){//完成结算通知
		var url = "/balance/viewBalancedNotices?sid="+sid+"&id="+msgid + "&busId="+id+"&busType="+type;
		openWinByRight(url);
	}else if('06601'==type){//财务结算通知
		var url = "/balance/viewBalanceNotices?sid="+sid+"&id="+msgid + "&busId="+id+"&busType="+type;
		openWinByRight(url);
	}else if('067'==type){//属性变更通知
		var url = "/moduleChangeExamine/viewModChangeApply?sid="+sid+"&id="+msgid + "&busId="+id+"&busType="+type;
		openWinByRight(url);
	}
}
//待办事项分类
function jobClassify(busType,activityMenu){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	//清空筛选条件
	$("#content").val("");
	//分类筛选
	$("#modTypes").val(busType);
	$("#busType").val(busType);
	$("#activityMenu").val(activityMenu);
	$("#searchForm").submit();
}
//设置待办未处理数
function changeTodoNum(busType){
	
	var allNoReadNum = $("#allNoReadNumT").data("noReadNum");
	if(allNoReadNum && allNoReadNum>=1){
		allNoReadNum = allNoReadNum-1;
	}else{
		allNoReadNum=0;
	}
	setNoReadNum("allNoReadNumT",allNoReadNum);
	$("#allNoReadNumT").data("noReadNum",allNoReadNum);
	
	if(busType=='022'||busType=='02201'){//审批未读
		busType = '022';
	}else if(busType=='027010'||busType=='027011'){//采购未读
		busType = '027010';
	}else if(busType=='027020'||busType=='027021'){//申领未读
		busType = '027020';
	}else if(busType=='1'){//消息未读
		busType = '100';
	}
	
	var noReadNum = $("#noReadNumT_"+busType).data("noReadNum");
	if (noReadNum && noReadNum>=1) {
		noReadNum = noReadNum-1;
	} else {
		noReadNum = 0;
	}
	setNoReadNum("noReadNumT_"+busType, noReadNum);
	$("#noReadNumT_"+busType).data("noReadNum",noReadNum);
}
//移除任务待办
function removeTaskTodo(modId){
	$("#allTodoBody").find("[modId=\""+modId+"\"][modType=\"003\"][isClock=\"0\"]").parents("tr").remove();
	$.each($("#allTodoBody .rowNum"),function(index,item){
		$(this).html(index+1);
	});
	//修改未读数
	 changeTodoNum('003');
}


/**
 * 加载更多的待办事项
 */
function loadOtherTodo(ts){
	var pageSize = $("#searchForm #pageSize").val();
	if(!pageSize){
		pageSize=15;
	}
	var todoIds = new Array();
	$.each($("#allTodoBody tr").find("a"),function(index,item){
		todoIds.push($(this).attr("todoId"));
	});
	if(ts){//点击查看详情时，判断是否需要异步加载数据
		//是否为闹铃
		var isClock = $(ts).attr("isClock");
		//模块类型
		var modType = $(ts).attr("modType");
		
		if(isClock==1){//移除后加载数据
			$(ts).parents("tr").remove();
			//修改未读数
			changeTodoNum(modType);
			$.each($("#allTodoBody .rowNum"),function(index,item){
				$(this).html(index+1);
			})
			
			//是否有分页
			var isPaged = $(document).find(".ps-page").length>0;
			if(isPaged){//有分页，则修改数据
				var totalRecord = parseInt($(".ps-page").find(".badge").html());
				$(".ps-page").find(".badge").html(totalRecord-1)
			}	
		}else if(modType!="003" //移除后加载数据(非任务)
				&& modType!="015" 
				&& modType!="022" 
				&& modType!="046" 
				&& modType!="047" 
				&& modType!="027020" 
				&& modType!="027010" 
				&& modType!="00306"
				&& modType!="06601"
				&& modType!="067"){
			$(ts).parents("tr").remove();
			//修改未读数
			changeTodoNum(modType);
			$.each($("#allTodoBody .rowNum"),function(index,item){
				$(this).html(index+1);
			})
			
			//是否有分页
			var isPaged = $(document).find(".ps-page").length>0;
			if(isPaged){//有分页，则修改数据
				var totalRecord = parseInt($(".ps-page").find(".badge").html());
				$(".ps-page").find(".badge").html(totalRecord-1)
			}	
		}else{//是任务则不处理
			return;
		}
	}
	//加载一条数据
	$("#searchForm").ajaxSubmit({
		type:"post",
		url:"/msgShare/toDoJobs/ajaxJobsCenter?sid="+sid+"&t="+Math.random(),
		dataType: "json",
		data:{pageNum:nowPageNum,pageSize:pageSize,todoIds:todoIds.join(",")},
		success:function(data){
			if(data.status=='y'){
				var list = data.list;
				var html = getAllTodoHtml(list);
				$("#allTodoBody").append(html);
				$.each($("#allTodoBody .rowNum"),function(index,item){
					$(this).html(index+1);
				});
				$("body").find(".ps-page>.ps-pageText>.badge").html(data.todoNum+todoIds.length-1)
			}
		},
		error:function(XmlHttpRequest,textStatus,errorThrown){
		}
	})
}
/**
 * 补充待办集合
 * @param list
 * @returns {String}
 */
function getAllTodoHtml(list){
	var html = '';
	if(list.length>0){
		//需要添加的行数
		var allShowNum = 10-$("#allTodoBody .rowNum").length;
		for(var i=0;i<list.length;i++){
			if(allShowNum==0){//行数已满，不在添加
				break;
			}
			var obj = list[i];
			var modLen =  $("#allTodoBody").find("[modId=\""+obj.busId+"\"][modType=\""+obj.busType+"\"]").length;
			if(modLen>0){
				continue;
			}
			allShowNum--;
			html +='\n <tr>';
			html +='\n		<td class="rowNum">'+(i+1)+'</td>';
			html +='\n		<td>';
			var readState = obj.readState==0?"noread":"";
			
			var modTypeName;
			if(obj.isClock=='1'){
				modTypeName ='[闹铃]';
			}else if(obj.busType=='1'){
				modTypeName ='[分享]';
			}else if(obj.busType=='015'){
				modTypeName ='[加入申请]';
			}else if(obj.busType=='017'){
				modTypeName ='[会议确认]';
			}else if(obj.busType=='018'){
				modTypeName ='[会议通知]';
			}else if(obj.busType=='027010' ||obj.busType=='027011'){
				modTypeName ='[用品采购]'; 
			}else if(obj.busType=='027020' ||obj.busType=='027021'){
				modTypeName ='[用品申领]'; 
			}else if(obj.busType=='00306'){
				modTypeName ='[任务报延]'; 
			}else if(obj.busType=='02201'){
				modTypeName ='[审批]'; 
			}else if(obj.busType=='040'){
				modTypeName ='[制度]'; 
			}else if(obj.busType=='046'){
				modTypeName ='[会议审批]'; 
			}else if(obj.busType=='047'){
				modTypeName ='[会议纪要审批]'; 
			}else if(obj.busType=='099'){
				modTypeName ='[催办]'; 
			}else if(obj.busType=='0103'){
				modTypeName ='[领款通知]'; 
			}else if(obj.busType=='068'){
				modTypeName ='[联系人]'; 
			}else if(obj.busType=='06602'){
				modTypeName ='[完成结算]'; 
			}else if(obj.busType=='06601'){
				modTypeName ='[财务结算]'; 
			}else if(obj.busType=='067'){
				modTypeName ='[变更审批]'; 
			}else{
				modTypeName = "["+obj.moduleTypeName.substring(0,2)+']';
			}
			var content;
			if(obj.isClock=='1'){
				content = obj.content.substring(5,obj.content.length);
			}else{
				content = obj.busTypeName;
			}
			html +='\n 		<span class="'+readState+'">'+modTypeName+'</span>';
			if(obj.isClock=='1'){
				html +='\n	<a href="javascript:void(0)" onclick="readMod(this,\''+pageTag+'\','+obj.busId+',\''+obj.busType+'\');viewTodoDetail('+obj.id+','+obj.busId+', \''+obj.busType+'\', \''+obj.isClock+'\',\''+obj.clockId+'\',\''+sid+'\')" class="'+readState+'" todoId="'+obj.id+'" isClock="1" modId="'+obj.busId+'" modType="'+obj.busType+'">';
			}else{
				html +='\n 	<a href="javascript:void(0);" onclick="readMod(this,\''+pageTag+'\','+obj.busId+',\''+obj.busType+'\');viewTodoDetai('+obj.busId+',\''+obj.busType+'\',0,0,\''+sessionUserId+'\',\''+sid+'\',\''+obj.id+'\')" class="'+readState+'"  todoId="'+obj.id+'" isClock="0" modId="'+obj.busId+'" modType="'+obj.busType+'">';
			}
			html +='\n				'+cutstr(content,87);
			html +='\n			</a>';
			html +='\n 		</td>';
			html +='\n 		<td>';
			if(obj.grade>0 && obj.isClock==0){
				if(obj.grade==4){
					html +='\n 		<span class="label label-danger">紧急且重要</span>';
				}else if(obj.grade==3){
					html +='\n 		<span class="label label-danger">紧急</span>';
				}else if(obj.grade==2){
					html +='\n 		<span class="label label-orange">重要</span>';
				}else if(obj.grade==1){
					html +='\n 		<span class="label label-success">普通</span>';
				}
			}else{
				html +='\n 		<span class="label">--</span>';
			}
			html +='\n 		</td>';
			html +='\n 		<td >';
			if(obj.dealTimeLimit){
				html +='\n 		'+obj.dealTimeLimit;
			}
			html +='\n 		</td>';
			html +='\n 		<td>';
			html +='\n 			<div class="ticket-user">';
			var uuid = obj.modifyerUuid;
			html +='\n <img class="user-avatar" src="/downLoad/userImg/'+obj.comId+'/'+obj.modifyer+'?sid='+sid+'" title="'+obj.modifyerName+'" />';
			html +='\n				<span class="user-name">'+obj.modifyerName+'</span>';
			html +='\n			</div>';
			html +='\n 		</td>';
			html +='\n 		<td>';
			html +='\n'+obj.recordCreateTime.substring(0,19);
			html +='\n 		</td>';
			html +='\n </tr>';
		
		}
	}
	return html;
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
