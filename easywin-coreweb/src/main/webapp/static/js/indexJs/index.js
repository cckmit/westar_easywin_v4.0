//页面加载初始化
$(function(){
	//任务名筛选
	$("#searchUser").blur(function(){
		$("#home11").html('');
	});
	//文本框绑定回车提交事件
	$("#searchUser").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#searchUser").val())){
        		$("#home11").html('');
        	}else{
        		$("#home11").html('');
        	}
        }
    });
	
	$.each($(".indexLanmu"),function(index,item){
		var busType = $(this).attr("lanmuType");
		loadIndexLanmu(busType,$(this));
	});
});
//查询栏目信息
function loadIndexLanmu(busType,lanmuObj){
	$.ajax({
		 type: "post",
 		 url:"/loadIndexLanmu?sid="+sid+"&t="+Math.random(),
 		 dataType: "json",
 		 data:{busType:busType},
 		 beforeSend:function(){
 			$(lanmuObj).html('');
 			 if(!$(lanmuObj).hasClass("layui-layer-load")){
 				$(lanmuObj).addClass("layui-layer-load")
 			 }
 		 },
 		 success:function(data){
 			 if(data.status=='y'){
 				 var list = data.list;
 				 var html = '';
 				 if(busType=='020'){//待办
 					 html = getTodoLanmu(list);
 				 }else if(busType=='021'){//关注
 					 html = getAtteLanmu(list);
 				 }else if(busType=='012'){//客户
 					 html = getCrmLanmu(list);
 				 }else if(busType=='013'){//附件
 					 html = getFileLanmu(list);
 				 }else if(busType=='004'){//投票
 					 html = getVoteLanmu(list);
 				 }else if(busType=='011'){//问答
 					 html = getQasLanmu(list);
 				 }else if (busType == '039') {// 公告
 					getAnnounLanmu(list,lanmuObj);
 				}
 				 
 				 $(lanmuObj).append(html);
 			 }
 			 
 			$(lanmuObj).removeClass("layui-layer-load")
 		 },error:function(){
 		 }
	})
}
//取得公告的栏目信息
function getAnnounLanmu(list,lanmuObj) {
	
	var mod ='\n <li class="order-item">';
	mod +='\n 	<div class="row">';
	mod +='\n 		<div class="col-lg-9 col-md-9 col-sm-9 col-xs-9 item-left">';
	mod +='\n 			<div class="item-booker">';
	mod +='\n 			<a href="javascript:void(0)"  class="ps-textLink " >';
	mod +='\n 				<span style="float: none" class="busName"></span>';
	mod +='\n 			</a>';
	mod +='\n 			</div>';
	mod +='\n 		</div>';
	mod +='\n 		<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 item-right">';
	mod +='\n 			<span class="ps-time">';
	mod +='\n 			</span>';
	mod +='\n 		</div>';
	mod +='\n 	</div>';
	mod +='\n </li>';
	
	if (list && list.length > 0) {
		$.each(list, function(index, vo) {
			var html = $(mod).clone();
			$(html).find("a").on("click", function() {
				readMod($(this), pageTag, vo.id, '039');
				msgShareView(sid, vo.id, '039');
			})
			if(vo.isRead != 1 ){
				$(html).find("a").addClass("noread")
			}
			var busTypeName = "";
			if (vo.type == '1') {
				busTypeName += '[通知]';
			} else if (vo.type == '2') {
				busTypeName += '[通报]';
			} else if (vo.type == '3') {
				busTypeName += '[决定]';
			} else {
				busTypeName += '[其他]';
			}
			var modiferName = vo.creatorName;
			if (modiferName) {
				$(html).find(".busName").html(busTypeName +vo.title+"(推送人员：" + modiferName+")", 30);
			}else{
				$(html).find(".busName").html(busTypeName +vo.title, 30);
			}

			$(html).find(".ps-time").html(vo.recordCreateTime.substring(0, 10));

			$(html).find("a").attr("modId", vo.id);
			$(html).find("a").attr("modType", '039');
			
			$(lanmuObj).append(html)
		})
	}
}

//无数据显示提示
function getNoDataHtml(){
	var html = '<div class="col-md-12 col-xs-12">';
	html +='\n <section class=\"error-container text-center\">';
	html +='\n <h1><i class=\"fa fa-exclamation-triangle\"></i></h1>';
	html +='\n <div class=\"error-divider\">';
	html +='\n <h2>还没有相关数据显示！</h2>';
	html +='\n <p class=\"description\">协同提高效率，分享拉近距离。</p>';
	html +='\n </div>';
	html +='\n </section>';
	html +='\n </div>';
	return html;
}
//取得待办栏目信息
function getTodoLanmu(list){
	var html = '';
	if(list.length>0){
		for(var i=0;i<list.length;i++){
			var vo = list[i];
			
			html +='\n <li class="order-item">';
			html +='\n 	<div class="row">';
			html +='\n 		<div class="col-lg-9 col-md-9 col-sm-9 col-xs-9 item-left">';
			html +='\n 			<div class="item-booker">';
			var readState = vo.readState==0?'noread':'';
			var busTypeName = vo.busTypeName;
			if(vo.isClock==1){
				busTypeName = busTypeName.substring(5,busTypeName.length);
				html +='\n	<a onclick="readMod(this,\''+pageTag+'\','+vo.busId+', \''+vo.busType+'\');viewTodoDetail('+vo.id+','+vo.busId+', \''+vo.busType+'\','+
							'\'1\',\''+vo.clockId+'\',\''+sid+'\',this)" href="javascript:void(0)" '+  
							' class="ps-textLink '+readState+'" title="'+vo.busTypeName+'" modId="'+vo.busId+'" modType="'+vo.busType+'" isClock="1" todoId="'+vo.id+'">';
			}else{
				html +='\n	<a onclick="readMod(this,\''+pageTag+'\','+vo.busId+', \''+vo.busType+'\');msgShareView(\''+sid+'\','+vo.busId+', \''+vo.busType+'\',\'\',\'\','+vo.id+')" href="javascript:void(0)" '+  
							' class="ps-textLink '+readState+'" title="'+vo.busTypeName+'" modId="'+vo.busId+'" modType="'+vo.busType+'" todoId="'+vo.id+'">';
			}
			var modName = vo.moduleTypeName;
			if(vo.isClock=='1'){
				modName ='[闹铃]'; 
			}else if(vo.busType=='1'){
				modName ='[分享]'; 
			}else if(vo.busType=='015'){
				modName ='[加入申请]'; 
			}else if(vo.busType=='017'){
				modName ='[参会确认]'; 
			}else if(vo.busType=='018'){
				modName ='[会议申请]'; 
			}else if(vo.busType=='02201'){
				modName ='[审批]'; 
			}else if(vo.busType=='027010'){
				modName ='[用品采购审核]'; 
			}else if(vo.busType=='027020'){
				modName ='[用品申领审核]'; 
			}else if(vo.busType=='00306'){
				modName ='[任务报延]'; 
			}else if(vo.busType=='040'){
				modName ='[制度]'; 
			}else if(vo.busType=='046'){
				modName ='[会议审批]'; 
			}else if(vo.busType=='047'){
				modName ='[会议纪要审批]'; 
			}else if(vo.busType=='099'){
				modName ='[催办]'; 
			}else if(vo.busType=='0103'){
				modName ='[领款通知]'; 
			}else if(vo.busType=='068'){
				modName ='[联系人]'; 
			}else if(vo.busType=='06602'){
				modName ='[完成结算]'; 
			}else if(vo.busType=='06601'){
				modName ='[财务结算]'; 
			}else if(vo.busType=='067'){
				modName ='[变更审批]'; 
			}else{
				modName = '['+modName.substring(0, 2)+']';
			}
			html +='\n 				<span style="float: none">'+modName+'</span>';
			html += cutstr(busTypeName,40);
			html +='\n 			</a>';
			html +='\n 			</div>';
			html +='\n 		</div>';
			html +='\n 		<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 item-right">';
			html +='\n 			<span class="ps-time">';
			
			html +='\n '+vo.recordCreateTime.substring(0,10);
			html +='\n 			</span>';
			html +='\n 		</div>';
			html +='\n 	</div>';
			html +='\n </li>';
		}
		
	}else{//无数据显示
		html = getNoDataHtml();
    }
	return html;
}
//取得关注栏目信息
function getAtteLanmu(list){
	var html = '';
	if(list.length>0){
		for(var i=0;i<list.length;i++){
			var vo = list[i];
			html +='\n <li class="order-item">';
			html +='\n 	<div class="row">';
			html +='\n 		<div class="col-lg-9 col-md-9 col-sm-9 col-xs-9 item-left">';
			html +='\n 			<div class="item-booker">';
			
			var readState = vo.isRead==0?'noread':'';
			
			html +='\n	<a onclick="readMod(this,\''+pageTag+'\','+vo.busId+', \''+vo.busType+'\');msgShareView(\''+sid+'\','+vo.busId+', \''+vo.busType+'\')" href="javascript:void(0)" '+  
				' class="ps-textLink '+readState+'" title="'+vo.modTitle+'" modId="'+vo.busId+'" modType="'+vo.busType+'">';
			var modName = vo.busTypeName;
			if(vo.busType=='1'){
				modName ='[分享]'; 
			}else{
				modName = '['+modName.substring(0, 2)+']';
			}
			html +='\n 				<span style="float: none">'+modName+'</span>';
			html +=cutstr(vo.modTitle,30);
			var modiferName = vo.modiferName;
			if(modiferName){
				html +='(更新人:'+vo.modiferName+')';
				
			}
			html +='\n 			</a>';
			html +='\n 			</div>';
			html +='\n 		</div>';
			html +='\n 		<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 item-right">';
			html +='\n 			<span class="ps-time">';
			var time = vo.modifTime;
			if(time){
				time = time.substring(0,16)
			}else{
				time = vo.modTime.substring(0,16)
			}
			
			html +='\n '+time;
			html +='\n 			</span>';
			html +='\n 		</div>';
			html +='\n 	</div>';
			html +='\n </li>';
		}
		
	}else{//无数据显示
		html = getNoDataHtml();
    }
	return html;
}
//取得客户栏目信息
function getCrmLanmu(list){
	var html = '';
	if(list.length>0){
		for(var i=0;i<list.length;i++){
			var vo = list[i];
			html +='\n <li class="order-item">';
			html +='\n 	<div class="row">';
			html +='\n 		<div class="col-lg-9 col-md-9 col-sm-9 col-xs-9 item-left">';
			html +='\n 			<div class="item-booker">';
			var readState = vo.isRead==0?'noread':'';
			html +='\n	<a onclick="readMod(this,\''+pageTag+'\','+vo.id+', \'012\');msgShareView(\''+sid+'\','+vo.id+', \'012\')" href="javascript:void(0)" '+  
				' class="ps-textLink '+readState+'" title="'+vo.customerName+'" modId="'+vo.id+'" modType="012">';
			html +=cutstr(vo.customerName,30);
			var modiferName = vo.modifierName;
			if(modiferName){
				html +='(更新人:'+vo.modifierName+')';
			}
			html +='\n 			</a>';
			html +='\n 			</div>';
			html +='\n 		</div>';
			html +='\n 		<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 item-right">';
			html +='\n 			<span class="ps-time">';
			html +='\n '+vo.modifyTime.substring(0,16);
			html +='\n 			</span>';
			html +='\n 		</div>';
			html +='\n 	</div>';
			html +='\n </li>';
		}
		
	}else{//无数据显示
		html = getNoDataHtml();
    }
	return html;
}
//取得附件栏目信息
function getFileLanmu(list){
	var html = '';
	if(list.length>0){
		for(var i=0;i<list.length;i++){
			var vo = list[i];
			html +='\n <li class="order-item">';
			html +='\n 	<div class="row">';
			html +='\n 		<div class="col-lg-9 col-md-9 col-sm-9 col-xs-9 item-left">';
			html +='\n 			<div class="item-booker">';
			if (vo.fileExt=='doc' || vo.fileExt=='docx' || vo.fileExt=='xls' 
				|| vo.fileExt=='xlsx' || vo.fileExt=='ppt' || vo.fileExt=='pptx' 
				|| vo.fileExt=='pdf' || vo.fileExt=='txt'){
				html +='\n <a href="javascript:void(0);" onclick="viewOfficePage(\''+vo.fileId+'\', '+
				' \''+vo.uuid+'\',\''+vo.fileName+'\',\''+vo.fileExt+'\',\''+sid+'\',\''+vo.moduleType+'\',\''+vo.id+'\')" '+
				'	class="ps-textLink" title="'+vo.fileName+'">';
			}else if(vo.fileExt=='jpg'||vo.fileExt=='bmp'||vo.fileExt=='gif'
				||vo.fileExt=='jpeg'||vo.fileExt=='png'){
				
				html +='\n <a href="javascript:void(0);" onclick="showPic(\'/downLoad/down/'+vo.uuid+'/'+vo.fileName+'\', '+
				' \''+sid+'\',\''+vo.fileId+'\',\''+vo.moduleType+'\',\''+vo.id+'\')" '+
				'	class="ps-textLink" title="'+vo.fileName+'">';
			}else{
				html +='\n <a href="javascript:void(0);" class="ps-textLink">';
			}
			html +=cutstr(vo.fileName,30);
			var modiferName = vo.userName;
			if(modiferName){
				html +='(分享人:'+vo.userName+')';
			}
			html +='\n 				</a>';
			html +='\n 			</div>';
			html +='\n 		</div>';
			html +='\n 		<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 item-right">';
			html +='\n 			<span class="ps-time">';
			html +='\n '+vo.fileDate.substring(0,16);
			html +='\n 			</span>';
			html +='\n 		</div>';
			html +='\n 	</div>';
			html +='\n </li>';
		}
		
	}else{//无数据显示
		html = getNoDataHtml();
    }
	return html;
}
//取得投票栏目信息
function getVoteLanmu(list){
	var html = '';
	if(list.length>0){
		for(var i=0;i<list.length;i++){
			var vo = list[i];
			html +='\n <li class="order-item">';
			html +='\n 	<div class="row">';
			html +='\n 		<div class="col-lg-9 col-md-9 col-sm-9 col-xs-9 item-left">';
			html +='\n 			<div class="item-booker">';
			var readState = vo.isRead==0?'noread':'';
			html +='\n	<a onclick="readMod(this,\''+pageTag+'\','+vo.id+', \'004\');msgShareView(\''+sid+'\','+vo.id+', \'004\')" href="javascript:void(0)" '+  
				' class="ps-textLink '+readState+'" title="'+vo.voteContent+'" modId="'+vo.id+'" modType="004">';
			html +=cutstr(vo.voteContent,30);
			var modiferName = vo.ownerName;
			if(modiferName){
				html +='(发起人:'+vo.ownerName+')';
			}
			html +='\n 			</a>';
			html +='\n 			</div>';
			html +='\n 		</div>';
			html +='\n 		<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 item-right">';
			html +='\n 			<span class="ps-time">';
			html +='\n '+vo.recordCreateTime.substring(0,16);
			html +='\n 			</span>';
			html +='\n 		</div>';
			html +='\n 	</div>';
			html +='\n </li>';
		}
		
	}else{//无数据显示
		html = getNoDataHtml();
    }
	return html;
}
//取得问答栏目信息
function getQasLanmu(list){
	var html = '';
	if(list.length>0){
		for(var i=0;i<list.length;i++){
			var vo = list[i];
			html +='\n <li class="order-item">';
			html +='\n 	<div class="row">';
			html +='\n 		<div class="col-lg-9 col-md-9 col-sm-9 col-xs-9 item-left">';
			html +='\n 			<div class="item-booker">';
			var readState = vo.isRead==0?'noread':'';
			html +='\n	<a onclick="readMod(this,\''+pageTag+'\','+vo.id+', \'011\');msgShareView(\''+sid+'\','+vo.id+', \'011\')" href="javascript:void(0)" '+  
				' class="ps-textLink '+readState+'" title="'+vo.title+'" modId="'+vo.id+'" modType="011">';
			html +=cutstr(vo.title,30);
			var modiferName = vo.userName;
			if(modiferName){
				html +='(提问人:'+vo.userName+')';
			}
			html +='\n 			</a>';
			html +='\n 			</div>';
			html +='\n 		</div>';
			html +='\n 		<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 item-right">';
			html +='\n '+vo.recordCreateTime.substring(0,16);
			html +='\n 		</div>';
			html +='\n 	</div>';
			html +='\n </li>';
		}
		
	}else{//无数据显示
		html = getNoDataHtml();
    }
	return html;
}

//查询常用人员
function loadUserdUser(anyNameLike){
	return;
	$.ajax({
		 type: "post",
		 url:"/loadIndexUser?sid="+sid,
		 dataType: "json",
		 data:{anyNameLike:anyNameLike},
		 beforeSend:function(){
			 if(!$("#home11").hasClass("layui-layer-load")){
				$("#home11").addClass("layui-layer-load")
			 }
		 },
		 success:function(data){
			 if(data.status=='y'){
				 var list = data.list;
				 var html = getUserLanmu(list);
				 $("#home11").append(html);
			 }
			 
			$("#home11").removeClass("layui-layer-load")
		 }
	})
}
//取得常用人员信息
function getUserLanmu(list){
	var html = "";
	if(list.length>0){
		for(var i=0;i<list.length;i++){
			var user = list[i];
			html +='\n <div class="databox conacts-group">';
			html +='\n		<div class="databox-left no-padding bordered-left-2 bordered-palegreen conacts-img">';
			var smImgUuid = user.smImgUuid;
			html +='\n 		<img class="user-avatar" style="width:40px; height:40px;" src="/downLoad/userImg/'+user.comId+'/'+user.id+'?sid='+sid+'" title="'+user.userName+'" />';
			html +='\n 		</div>';
			html +='\n 		<div class="databox-right padding-top-20">';
			html +='\n 			<div class="databox-text darkgray">'+user.userName+'</div>';
			html +='\n 			<div class="databox-text darkgray">'+user.depName+'</div>';
			html +='\n	 	</div>';
			html +='\n </div>';
		}
	}
	return html;

}
//查看详情
function msgShareView(sid,id,type,scroll,operator,msgid){
	if('003'==type//任务
		|| '012'==type//客户
		|| '005'==type){//项目
			viewModInfo(id,type,-1)
	}else if('006'==type){//周报
		viewModInfo(id,type,-1);
	}else if('050'==type){//分享
        viewModInfo(id,type,-1);
    }else if('004'==type){//投票
		viewModInfo(id,type,-1);
	}else if('040'==type){//制度
		var url = "/institution/viewInstitu?sid=" + sid + "&id=" + id;
		openWinByRight(url);
	}else if('011'==type){//问答
		var url = "/qas/viewQuesPage?sid="+sid+"&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
		openWinByRight(url);
	}else if('039'==type){//公告
		var url = "/announcement/viewAnnoun?sid=" + sid + "&id=" + id;
		openWinByRight(url);
	}else if('015'==type){//申请
		checkApplyInUser(id)
	}else if('017'==type){//会议申请
		$.post("/meeting/authCheckMeetUser?sid="+sid,{Action:"post",meetingId:id},     
				function (data){
				if(data.status=='y'){
					var meet = data.meeting;
					if(meet.timeOut==0){//会议已结束
						if(operator==meet.recorder){//会议记录人员添加会议纪要
							var url = "/meetSummary/addSummaryPage?sid="+sid+"&meetingId="+id+"&redirectPage=/meeting/listPagedDoneMeeting?sid="+sid+"";
							openWinByRight(url);
						}else{
							var url = "/meetSummary/viewSummaryPage?sid="+sid+"&meetingId="+id+"&redirectPage=/meeting/listPagedDoneMeeting?sid="+sid+"";
							openWinByRight(url);
						}
					}else if(meet.timeOut==1){//会议正在进行
						if(operator==meet.organiser){//会议发布人员邀请参会人员
							var url = "/meeting/invMeetUserPage?sid="+sid+"&meetingId="+id+"&redirectPage=/meeting/listPagedTodoMeeting?sid="+sid+"";
							openWinByRight(url);
						}else{
							var url = "/meeting/viewMeetingPage?sid="+sid+"&meetingId="+id+"&redirectPage=/meeting/listPagedTodoMeeting?sid="+sid+"";
							openWinByRight(url);
						}
					}else if(meet.timeOut==2){//会议未开始
						if(operator==meet.organiser){//会议发布人员邀请参会人员
							var url = "/meeting/updateMeetingPage?sid="+sid+"&id="+id+"&batchUpdate=1&redirectPage="+encodeURIComponent(window.location.href);
							openWinByRight(url);
						}else{
							var url = "/meeting/viewMeetingPage?sid="+sid+"&meetingId="+id+"&redirectPage="+encodeURIComponent(window.location.href);
							openWinByRight(url);
						}
					}
				}else{
					showNotification(1,data.info);
					window.self.location.reload();
				}
		},"json");
	}else if('018'==type){//会议申请
		$.post("/meeting/authCheckMeetRoom?sid="+sid,{Action:"post",meetingId:id},     
				function (data){
				if(data.status=='y'){
					var url = "/meetingRoom/listPagedApplyForCheck?sid="+sid+"&meetingId="+id+"&redirectPage="+encodeURIComponent(window.location.href);
					openWinByRight(url);
				}else{
					showNotification(2,data.info);
					window.self.location.reload();
				}
		},"json");
	}else if('022'==type||'02201'==type){//审批
		var url = "/workFlow/viewSpFlow?sid="+sid+"&instanceId="+id;
		openWinByRight(url);
	}else if('027010'==type||type == '027011'){//采购审核
		var url = "/bgypPurOrder/viewBgypPurOrderPage?sid="+sid+"&purOrderId="+id;
		openWinByRight(url);
	}else if('027020'==type||type == '027021'){//申领审核
		var url = "/bgypApply/viewBgypApplyPage?sid="+sid+"&applyId="+id;
		openWinByRight(url);
	}else if('1'==type){//普通分享
		if(scroll){//直接查看评论
			var url = "/msgShare/msgShareViewPage?sid="+sid+"&id="+id+"&type="+type+"&scroll="+scroll;
			openWinByRight(url);
		}else{
			var url = "/msgShare/msgShareViewPage?sid="+sid+"&id="+id+"&type="+type;
			openWinByRight(url);
		}
	}else if('00306'==type){//任务报延
		var url = "/task/delayApplyDetail?sid="+sid+"&applyId="+id;
		openWinByRight(url);
	}else if('046'== type){//会议审批
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
	}else if('06601'==type){//财务办公结算通知
		var url = "/balance/viewBalanceNotices?sid="+sid+"&id="+msgid + "&busId="+id+"&busType="+type;
		openWinByRight(url);
	}else if('067'==type){//属性变更通知
		var url = "/moduleChangeExamine/viewModChangeApply?sid="+sid+"&id="+msgid + "&busId="+id+"&busType="+type;
		openWinByRight(url);
	}
	
}
/**
 * 查看待办事项的详情
 * @param busId 业务主键
 * @param busType 业务类型
 * @param isClock 是否为闹铃
 * @param clockId 闹铃主键
 * @return
 */
function viewTodoDetail(id,busId,busType,isClock,clockId,sid){
	
	var url ="/msgShare/viewTodoDetailPage?sid="+sid+"&id="+id;
	url += "&busId="+busId+"&busType="+busType+"&isClock="+isClock+"&clockId="+clockId
	url += "&redirectPage="+encodeURIComponent(window.location.href);
	openWinByRight(url);
}
/**
 * 关闭首页展示模块
 * @param sid
 * @param busType
 */
function closeMenuHeadSet(ts,sid,busType){
	$.ajax({
		 type: "post",
  		 url:"/menu/updateMenuHomeSet?sid="+sid,
  		 dataType: "json",
  		 data:{busType:busType,openState:0},
  		 async:false,
  		 success:function(data){
  			if(data.status=='y'){
				showNotification(1,"设置成功");
				
				
				var modLanmu = $(ts).parents(".indexVoDiv");
				
				$(modLanmu).slideUp(300, function() {
	                //移除父级div
	                $(modLanmu).remove();
	                var len = $(".indexVoDiv").length;
	                if(len==0){
	                	$("#noMenuHomeSet").css("display","block")
	                }
	            });
			}else{
				showNotification(2,data.info);
			} 
  		 }
	})
}
//重置待办数
function resetAllTodo(){
	 var allTodoNums = parseInt($.trim($("#allTodoNums").html()))-1;
	 if(allTodoNums <0){
		 allTodoNums = 0;
	 }
	 $("#allTodoNums").html(allTodoNums)
}

/**
 * 移除待办事项
 * @param page 来源页面
 * @param modId 
 * @param modType
 */
function removeTodoTask(page,modId,modType){
	 if(page=='index'){
		 var todayA = $("#allLanmu").find("[lanmuType='020']").find("a");
		 var todoIdArray = new Array();
		 $.each(todayA,function(index,item){
			 todoIdArray.push($(this).attr("todoId"));
		 });
		 
		 $("#allLanmu").find("[lanmuType='020']").find("[modType='"+modType+"'][modId='"+modId+"']").parents(".order-item").remove();
		 
		 resetAllTodo();
		 
		 var lanmuType = $("#allLanmu").find("[lanmuType='020']");
		 //待办事项个数
		 var len = $(lanmuType).children().length;
		 //待办数
		 var allTodoNums = parseInt($.trim($("#allTodoNums").html()));
		 if(len<5 && len<allTodoNums){
			 $.ajax({
				 type: "post",
				 url:"/loadIndexMoreTodo?sid="+sid,
				 dataType: "json",
				 data:{todoIds:todoIdArray.join(","),leftNum:len},
				 success:function(data){
					 if(data.status=='y'){
						 var list = data.list;
						 var html = getTodoLanmu(list);
						 $(lanmuType).append(html);
					 }
				 }
			 });
		 }
		 
	 }
}

//页面引导
$(function(){
	postUrl('/intro/ajaxIntroState', {
		sid: sid,
		busType: '002',
	},function(data) {
		if (data.status == 'y') {
			return;
			if(data.intro){
				pageIntro();
			} 
		} else {
			showNotification(2, data.info);
		}
	});
});

//引导
function pageIntro(){
	return;
  //初始化引导
    var intro = introJs();
    intro.setOptions({
    	prevLabel: '上一步',
    	nextLabel: '下一步',
    	skipLabel: '跳过',
    	doneLabel: '完成',
    	showStepNumbers:false,//步骤序号
    	scrollToElement:true,
    	overlayOpacity:0.6,//遮隐处
		exitOnOverlayClick:true,//点击遮隐处退出
		exitOnEsc:true,//esc建退出
		keyboardNavigation:true,//键盘操作
		showBullets: false,//显示进度,可随意跳转步骤
    	//对应的数组，顺序出现每一步引导提示
    	steps:index_step,
    });
    intro.onafterchange(function(element){
    	for(var i=0;i<index_step.length;i++){
    		var stepId = "#"+ element.id;
    		if(index_step[i].element == stepId){
    			var step = i+1;
    	    	 $.cookie("index_step",step, {expires:30});
    		}
    	}
    });
    intro.onchange(function(element){
    	//团队
    	if('menuOrgDiv' == element.id){
    		 $("#menuOrgDiv").addClass("open");
    	}else{
    		if($("#menuOrgDiv").hasClass("open")){
    			$("#menuOrgDiv").removeClass("open");
    		}
    	}
    	//栏目
    	if('paramDiv' == element.id){
    		$("#indexModelSet").addClass("intro-border")
    		$("#addBusMore").addClass("intro-border")
    		$("#viewBusMore").addClass("intro-border")
    	}else{
    		if($("#indexModelSet").hasClass("intro-border")){
    			$("#indexModelSet").removeClass("intro-border")
        		$("#addBusMore").removeClass("intro-border")
        		$("#viewBusMore").removeClass("intro-border")
    		}
    	}
    	//分享
    	if('indexRight' == element.id){
    		$("#content").focus();
    	}
    });
    intro.onexit(function(){
    	if($("#indexModelSet").hasClass("intro-border")){
			$("#indexModelSet").removeClass("intro-border")
    		$("#addBusMore").removeClass("intro-border")
    		$("#viewBusMore").removeClass("intro-border")
		}
    });
    //完成
    intro.oncomplete(function() {
    	addIntro(sid,'002');
    });
    	
    intro.goToStep(step).start();
}