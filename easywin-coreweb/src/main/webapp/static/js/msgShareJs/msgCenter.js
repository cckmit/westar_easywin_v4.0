$(function(){
	initCard(sid);
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
	//标识已读（选中的数据）
	$("#readWork").click(function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要标识已查看？',{
				  btn: ['确定','取消']//按钮
			  ,title:'询问框'
			  ,icon:3
			}, function(index){
				window.top.layer.close(index);
				
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				
				$('#delForm').attr("action","/msgShare/delWorks?sid="+sid)
				$('#delForm').submit();
			});	
		}
	});
	//全部已读
	$("#readAll").click(function(){
		var ckBoxs = $(":checkbox[name='ids']");
		if (ckBoxs.length>0) {
			var array = $("#searchForm").find(":checkbox[name='modTypes']:checked");
			
			//一定是未读的消息
			var url = "/msgShare/delAllWorks";
			window.top.layer.confirm('确定要全部标识？',{
				  btn: ['确定','取消']//按钮
			  ,title:'询问框'
			  ,icon:3
			},  function(){
				//默认刷新本页面
				var urlRedirect = window.location.href+'';
				
				var readState = $("#readState").val();
				//查询条件是未读消息，则跳转到第一页
				if(readState==0){
					urlRedirect = urlPageFirst();
				}
				 
				$("#searchForm input[name='redirectPage']").val(urlRedirect);
				$("#searchForm input[name='readState']").val(0);
				$('#searchForm').attr("action",url)
				$('#searchForm').submit();
			});	
		}else{
			window.top.layer.alert('消息已全部阅读');
		}
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
});
/**
 * 选择日期
 */
function submitForm(){
	$("#searchForm").submit();
}
//查看详情
function viewDetailMod(id,busId,type,clockId,busSpec){
	if(clockId>0 && '017'!=type){//是闹铃（非定时会议）
		if('0'==type){//普通闹铃
			var url ="/msgShare/viewTodoDetailPage?sid="+sid+"&id="+id;
			url += "&busId="+busId+"&busType=0&isClock=1&clockId="+clockId;
			openWinByRight(url);
		}else{
			var url ="/msgShare/viewTodoDetailPage?sid="+sid+"&id="+id;
			url += "&busId="+busId+"&busType="+type+"&isClock=1&clockId="+clockId;
			openWinByRight(url);
			
		}
	}else if('003'==type//任务
			|| '012'==type//客户
        	|| '080'==type//产品
			|| '005'==type){//项目
		viewModInfo(busId,type,clockId)
		
		
	}else if('006'==type){//周报
		$.post("/weekReport/authorCheck?sid="+sid,{Action:"post",weekReportId:busId},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(2,msgObjs.promptMsg);
				}else{
					var url ="/weekReport/viewWeekReport?sid="+sid+"&id="+busId;
					openWinByRight(url);
				}
		},"json");
	}else if('050'==type){//分享
        $.post("/daily/authorCheck?sid="+sid,{Action:"post",dailyId:busId},
            function (msgObjs){
                if(!msgObjs.succ){
                    showNotification(2,msgObjs.promptMsg);
                }else{
                    var url ="/daily/viewDaily?sid="+sid+"&id="+busId;
                    openWinByRight(url);
                }
            },"json");
    }else if('039'==type){//公告
		var url = "/announcement/viewAnnoun?sid=" + sid + "&id=" + busId;
		openWinByRight(url);
	}else if('040'==type){//公告
		var url = "/institution/viewInstitu?sid=" + sid + "&id=" + busId;
		openWinByRight(url);
	}else if('004'==type){//投票
		$.post("/vote/authorCheck?sid="+sid,{Action:"post",voteId:busId},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(2,msgObjs.promptMsg);
				}else{
					var url ="/vote/voteDetail?sid="+sid+"&id="+busId;
					openWinByRight(url);
				}
		},"json");
	}else if('011'==type){//问答
		var url ="/qas/viewQuesPage?sid="+sid+"&id="+busId;
		openWinByRight(url);
	}else if('022'==type||'02201'==type){//审批
		var url = "/workFlow/viewSpFlow?sid="+sid+"&instanceId="+busId;
		openWinByRight(url);
	}else if('013'==type){//附件
		$.post("/fileCenter/getFileDetail?sid="+sid,{Action:"post",id:busId},     
			function (data){
				if(data.status=='y'){
					var fileDetail = data.fileDetail;
					//后缀
					var fileExt = fileDetail.fileExt;
					//uuid
					var uuid  = fileDetail.uuid;
					//附件名称
					var filename = fileDetail.fileName;
					//业务主键
					var busIdT = fileDetail.id
					//附件主键
					var fileId = fileDetail.fileId;
					if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx'||fileExt=='txt' ||fileExt=='pdf'){
						var url = "/fileCenter/viewOfficePage?id="+fileId+"&uuid="+uuid+"&filename="+filename+"&fileExt="+fileExt+"&sid="+sid+"&busId="+busIdT+"&busType="+fileDetail.moduleType;
						
						window.top.layer.open({
							type: 2,
							//title: ['附件预览', 'font-size:18px;'],
							title:false,
							area: ['950px', '90%'],
							closeBtn:0,
							fix: true, //不固定
							maxmin: false,
							move: false,
							content: [url,'no'],
							btn: ['关闭'],
							success:function(layero,index){
							    var iframeWin = window[layero.find('iframe')[0]['name']];
							    
							    $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
									  layer.close(index);
								  })
							  },
							
						});
					}else if(fileExt=='gif' ||fileExt=='jpg'||fileExt=='jpeg' ||fileExt=='png'||fileExt=='bmp'){
						var filepath = '/downLoad/down/'+uuid+'/'+filename
						var url = "/fileCenter/showPic?sid="+sid+"&filepath=" + filepath+"&fileExt="+fileExt+"&busType="+fileDetail.moduleType+"&busId="+busIdT+"&fileId="+fileId;
						
						window.top.layer.open({
							type: 2,
							//title: ['附件预览', 'font-size:18px;'],
							title:false,
							area: ['950px', '90%'],
							closeBtn:0,
							fix: true, //不固定
							maxmin: false,
							move: false,
							content: [url,'no'],
							btn: ['关闭'],
							success:function(layero,index){
							    var iframeWin = window[layero.find('iframe')[0]['name']];
							    
							    $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
									  layer.close(index);
								  })
							  },
							
						});
					}else{
						window.self.location.reload();
					}
				}
		},"json");
	}else if('015'==type){//申请
		
		var url="/userInfo/checkUserInfoPage?sid="+sid+"&id="+busId;
		layui.use('layer', function(){
			var layer = layui.layer;
			window.top.layer.open({
				  id:'layerOpener',
				  type: 2,
				  title: false,
				  closeBtn: 0,
				  shade: 0.5,
				  shift:0,
				  scrollbar:false,
				  fix: true, //固定
				  maxmin: false,
				  move: false,
				  area: ['550px', '400px'],
				  content: [url,'no'], //iframe的url
				  btn:['审核','取消'],
				  yes:function(index,layero){
					  var iframeWin = window[layero.find('iframe')[0]['name']];
					  iframeWin.checkUserInfo();
				  },
				  success: function(layero,index){
					    var iframeWin = window[layero.find('iframe')[0]['name']];
					    iframeWin.setWindow(window.document,window);
					}
				});
		});
		
	}else if('017'==type){//会议
		$.post("/meeting/checkMeetTimeOut?sid="+sid,{Action:"post",meetId:busId},     
				function (data){
				if(data.status=='y' && data.organiser>0){
					if(data.timeOut==0){//会议已结束
						if(sessionUserId==data.recorder){//会议记录人员添加会议纪要
							var url="/meetSummary/addSummaryPage?sid="+sid+"&meetingId="+busId;
							openWinByRight(url);
						}else{//非会议记录人员，查看会议+纪要
							var url ="/meetSummary/viewSummaryPage?sid="+sid+"&meetingId="+busId;
							openWinByRight(url);
						}
					}else if(data.timeOut==1){//会正在进行
						if(sessionUserId==data.organiser){//会议发布人员邀请参会人员
							var url ="/meeting/invMeetUserPage?sid="+sid+"&meetingId="+busId;
							openWinByRight(url);
						}else{//非会议发布人员查看会议
							var url="/meeting/viewMeetingPage?sid="+sid+"&meetingId="+busId;
							openWinByRight(url);
						}
					}else{
						if(sessionUserId==data.organiser){//会议发布人员修改会议信息
							var url ="/meeting/updateMeetingPage?sid="+sid+"&id="+busId+"&batchUpdate=1";
							openWinByRight(url);
						}else{//非会议发布人员，查看会议信息
							var url ="/meeting/viewMeetingPage?sid="+sid+"&meetingId="+busId;
							openWinByRight(url);
						}
					}
				}else if(data.status=='y' && data.organiser==0){
					showNotification(2,"会议已删除");
				}else{
					showNotification(2,data.info);
				}
		},"json");
	}else if('018'==type){//会议申请
		if(busSpec==1){
			$.post("/meeting/authCheckMeetRoom?sid="+sid,{Action:"post",meetingId:busId},     
					function (data){
					if(data.status=='y'){
						window.location.href="/meetingRoom/listPagedApplyForCheck?sid="+sid+"&meetingId="+busId+"&searchTab=22";
					}else{
						showNotification(2,data.info);
					}
			},"json");
		}else{
			viewDetailMod(id,busId,'017',clockId,busSpec)
		}
	}else if('019'==type){//分享
		
		$.post('/msgShare/doReadOne?sid='+sid,{id:id,random : Math.random()},function(rs){
			
		})
	}else if('027010'==type||type == '027011'){//采购审核
		var url = "/bgypPurOrder/viewBgypPurOrderPage?sid="+sid+"&purOrderId="+busId;
		openWinByRight(url);
	}else if('027020'==type||type == '027021'){//申领审核
		var url = "/bgypApply/viewBgypApplyPage?sid="+sid+"&applyId="+busId;
		openWinByRight(url);
	}else if('1'==type){//分享
		var url ="/msgShare/msgShareViewPage?sid="+sid+"&id="+busId+"&type="+type;
		openWinByRight(url);
	}else if('0'==type){//闹铃
		var url ="/msgShare/viewTodoDetailPage?sid="+sid+"&id="+id;
		url += "&busId="+clockId+"&busType=0&isClock=1&clockId="+clockId;
		openWinByRight(url);
	}else if('046'==type){//会议审批
		var url = "/meeting/viewMeetingPage?sid="+sid+"&meetingId="+busId;
		openWinByRight(url);
	}else if('047'==type){//会议纪要审批
		var url = "/meetSummary/viewSummaryById?sid="+sid+"&summaryId="+busId;
		openWinByRight(url);
	}else if('099'==type){//催办
		var url = "/busRemind/viewBusRemindPage?sid="+sid+"&id="+busId;
		openWinByRight(url);
	}else if('0103'==type){//领款通知
		var url = "/balance/viewNotices?sid="+sid+"&id="+id + "&busId="+busId+"&busType="+type;
		openWinByRight(url);
	}else if('068'==type){//外部联系人
		viewOlm(busId);
	}else if('06602'==type){//完成结算通知
		var url = "/balance/viewBalancedNotices?sid="+sid+"&id="+id + "&busId="+busId+"&busType="+type;
		openWinByRight(url);
	}else if('06601'==type){//财务结算
		var url = "/balance/viewBalanceNotices?sid="+sid+"&id="+id + "&busId="+busId+"&busType="+type;
		openWinByRight(url);
	}else if('067'==type){//属性变更
		var url = "/moduleChangeExamine/viewModChangeApply?sid="+sid+"&id="+id + "&busId="+busId+"&busType="+type;
		openWinByRight(url);
	}else if('016'==type){//日程
		var url = "/schedule/updateSchedulePage?sid="+sid+"&id="+busId;
		openWinByRight(url);
	}
	
}
function setReadState(type){
	$("#readState").val(type);
	submitForm();
}
//设置模块类型
function  setSearchBusType(busType){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#busType").val(busType);
	submitForm();
}

//设置待办未处理数
function changeMsgNoReadNum(busType){
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
//加载更多数据
function getNoReadList(ts){
	
	var msgObjs = $("#msgListUl").find("input[type=\"checkbox\"]");
	var todoIds = new Array();
	$.each(msgObjs,function(index,item){
		todoIds.push($(this).val());
	})
	$(ts).parents("li").remove();
	
	//是否有分页
	var isPaged = $(document).find(".ps-page").length>0;
	if(isPaged){//有分页，则修改数据
		var totalRecord = parseInt($(".ps-page").find(".badge").html());
		$(".ps-page").find(".badge").html(totalRecord-1)
	}	
	
	var pageSize = $("#searchForm #pageSize").val();
	$("#searchForm").ajaxSubmit({
		type:"post",
        url:"/msgShare/ajaxListMsgNoRead?sid="+sid+"&t="+Math.random(),
        dataType: "json",
        data:{pageNum:nowPageNum,pageSize:pageSize,todoIds:todoIds.join(",")},
        success:function(data){
        	if(data.status=='y'){
        		var list = data.list;
        		var html = getMsgNoReadHtml(list);
        		$("#msgListUl").append(html);
        	}
        },
        error:function(error){
        }
	})
}
//渲染页面
function getMsgNoReadHtml(list){
	var html='';
	if(list.length>0){
		var pageSize = $("#searchForm #pageSize").val();
		//需要添加的行数
		var allShowNum = pageSize-$("#msgListUl").find("li").length;
		for(var i=0;i<list.length;i++){
			if(allShowNum==0){//行数已满，不在添加
				break;
			}
			allShowNum--;
			var obj = list[i];
			var modLen =  $("#allTodoBody").find("input[type='checkbox'][value='"+obj.id+"']").length;
			if(modLen>0){
				continue;
			}
			html+='\n <li class="item first-item">';
			html+='\n 	<div class="message-content">';
			
			var uuid = obj.modifyerUuid;
			html +='\n 	<img class="message-head" width="50" height="50" src="/downLoad/userImg/'+obj.comId+'/'+obj.modifyer+'?sid='+sid+'" title="'+obj.modifyerName+'" />';
			
			html+='\n 		<div class="content-headline">';
			html+='\n 			<div class="checkbox pull-left ps-chckbox">';
			html+='\n 				<label>';
			var readState = obj.readState==0?"noread":"";
			var disabled = obj.readState==0?"disabled":"";
			html+='\n 					<input type="checkbox" class="colored-blue" value="'+obj.id+'" name="ids" '+disabled+' >';
			html+='\n 					<span class="text"></span>';
			html+='\n 				</label>';
			html+='\n 			</div>';
			html+='\n 			<span class="'+readState+'">';
			if(obj.isClock=='1'){
				html+='\n[闹铃]';
			}else if(obj.busType=='1'){
				html+='\n[分享]';
			}else if(obj.busType=='015'){
				html+='\n[加入申请]';
			}else if(obj.busType=='017' && obj.busSpec==1){
				html+='\n[参会确认]';
			}else if(obj.busType=='017'){
				html+='\n[会议]';
			}else if(obj.busType=='018'){
				html+='\n[会议申请]';
			}else if(obj.busType=='019'){
				html+='\n[普通消息]';
			}else if(obj.busType=='02201'){
				html+='\n[审批]';
			}else if(obj.busType=='027010'){
				html+='\n[用品采购审核]';
			}else if(obj.busType=='027011'){
				html+='\n[采购审核通知]';
			}else if(obj.busType=='027020'){
				html+='\n[用品申领审核]';
			}else if(obj.busType=='027021'){
				html+='\n[用品申领通知]';
			}else if(obj.busType=='039'){
				html+='\n[公告]';
			}else if(obj.busType=='040'){
				html+='\n[公告]';
			}else{
				html+='\n['+obj.moduleTypeName.substring(0,2)+']';
			}
			html+='\n 			</span>';
			html+='\n <a href="javascript:void(0)" class="item-box '+readState+'" isClock="'+obj.isClock+'" modId="'+obj.busId+'" modType="'+obj.busType+'"';
			html+='\n onclick="readMod(this,\'msgShare\','+obj.busId+',\''+obj.busType+'\');viewDetailMod('+obj.id+','+obj.busId+',\''+obj.busType+'\','+obj.clockId+','+obj.busSpec+')">';
			html+='\n'+obj.busTypeName;
			html+='\n </a>';
			html+='\n </div>';
			var content = obj.content;
			if(content){
				if(obj.roomId>0){
					
					html+='\n <a href="javascript:void(0);" class="item-box '+readState+'" onclick="toChat(\''+(obj.busType==0?obj.id:obj.busId)+'\',\''+obj.busType+'\',\''+obj.roomId+'\')">';
					html+='\n 	<div class="content-text">';
					html+='\n'+obj.content;
					html+='\n 	</div>';
					html+='\n </a>';
				}else{
					html+='\n <a href="javascript:void(0);" class="item-box '+readState+'" isClock="'+obj.isClock+'" modId="'+obj.busId+'" modType="'+obj.busType+'"';
					html+='\n onclick="readMod(this,\'msgShare\','+obj.busId+',\''+obj.busType+'\');viewDetailMod('+obj.id+','+obj.busId+',\''+obj.busType+'\','+obj.clockId+','+obj.busSpec+')">';
					html+='\n 	<div class="content-text">';
					html+='\n'+obj.content;
					html+='\n 	</div>';
					html+='\n </a>';
				}
			}
			html+='\n <div class="item-more">';
			html+='\n <span class="time"><i class="fa fa-clock-o"></i>更新时间：'+obj.recordCreateTime.substring(0,20)+'</span>';
			var modifyerName = obj.modifyerName;
			if(modifyerName){
				html+='\n <span class="time"><i class="fa fa-user"></i>更新人：'+modifyerName+'</span>';
			}
			if(obj.busType=='1' || obj.busType=='003' || obj.busType=='004' || obj.busType=='005' || obj.busType=='080' || obj.busType=='011' || obj.busType=='012'){
				
				html+='\n <a class="btn btn-default btn-xs" style="cursor: pointer; " ';
				html+='\n describe="1"  attentionState='+obj.attentionState+' busType='+obj.busType +'busId='+obj.busId+'iconSize="sizeMd"';
				html+='\n onclick="changeAtten(\''+sid+'\',this)">';
				if(obj.attentionState==0){
					html+='\n <i class="fa fa-star-o"></i>关注';
				}else{
					html+='\n <i class="fa fa-star ws-star"></i>取消';
				}
				html+='\n </a>';
			}
			html+='\n</div>';
			html+='\n</div>';
			html+='\n</li>';
			
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
