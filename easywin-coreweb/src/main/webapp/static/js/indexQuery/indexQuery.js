
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
	}else if('012'==type){//客户
		$.post("/crm/authorCheck?sid="+sid,{Action:"post",customerId:busId},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(2,msgObjs.promptMsg);
				}else{
					var url = "/crm/viewCustomer?sid="+sid+"&id="+busId+"&clockId="+clockId;
					openWinByRight(url);
				}
		},"json");
	}else if('005'==type){//项目
        $.post("/item/authorCheck?sid="+sid,{Action:"post",itemId:busId},
            function (msgObjs){
                if(!msgObjs.succ){
                    showNotification(2,msgObjs.promptMsg);
                }else{
                    var url="/item/viewItemPage?sid="+sid+"&id="+busId+"&clockId="+clockId;
                    openWinByRight(url);
                }
            },"json");
    }else if('080'==type){//产品
        $.post("/product/authorCheck?sid="+sid,{Action:"post",itemId:busId},
            function (msgObjs){
                if(!msgObjs.succ){
                    showNotification(2,msgObjs.promptMsg);
                }else{
                    var url="/product/viewProPage?sid="+sid+"&id="+busId+"&clockId="+clockId;
                    openWinByRight(url);
                }
            },"json");
    }else if('040'==type){//公告
		var url = "/institution/viewInstitu?sid=" + sid + "&id=" + busId;
		openWinByRight(url);
	}else if('003'==type){//任务
		$.post("/task/authorCheck?sid="+sid,{Action:"post",taskId:busId},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(2,msgObjs.promptMsg);
				}else{
					var url ="/task/viewTask?sid="+sid+"&id="+busId+"&clockId="+clockId;
					openWinByRight(url);
				}
		},"json");
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
	}else if('015'==type){//申请
		window.location.href="/userInfo/listPagedForCheck?sid="+sid+"&id="+busId+"&tab=11";
	}else if('017'==type){//会议
		$.post("/meeting/authorCheck?sid="+sid,{Action:"post",meetingId:busId},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(2,msgObjs.promptMsg);
				}else{
					$.post("/meeting/checkMeetTimeOut?sid="+sid,{Action:"post",meetId:busId},     
							function (data){
							if(data.status=='y' && data.organiser>0){
								if(data.timeOut==0){//会议已结束
									if(userId==data.recorder){//会议记录人员添加会议纪要
										var url="/meetSummary/addSummaryPage?sid="+sid+"&meetingId="+busId;
										openWinByRight(url);
									}else{//非会议记录人员，查看会议+纪要
										var url ="/meetSummary/viewSummaryPage?sid="+sid+"&meetingId="+busId;
										openWinByRight(url);
									}
								}else if(data.timeOut==1){//会正在进行
									if(userId==data.organiser){//会议发布人员邀请参会人员
										var url ="/meeting/invMeetUserPage?sid="+sid+"&meetingId="+busId;
										openWinByRight(url);
									}else{//非会议发布人员查看会议
										var url="/meeting/viewMeetingPage?sid="+sid+"&meetingId="+busId;
										openWinByRight(url);
									}
								}else{
									if(userId==data.organiser){//会议发布人员修改会议信息
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
	}else if('1'==type){//分享
		$.post("/msgShare/authorCheck?sid="+sid,{Action:"post",msgId:busId},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(2,msgObjs.promptMsg);
				}else{
					var url ="/msgShare/msgShareViewPage?sid="+sid+"&id="+busId+"&type="+type;
					openWinByRight(url)
				}
		},"json");
	}else if('0'==type){//闹铃
		var url ="/msgShare/viewTodoDetailPage?sid="+sid+"&id="+id;
		url += "&busId="+clockId+"&busType=0&isClock=1&clockId="+clockId;
		openWinByRight(url);
	}else if('022'==type){//审批
		$.post("/workFlow/authorCheck?sid="+sid,{Action:"post",instanceId:busId},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(2,msgObjs.promptMsg);
				}else{
					var url = "/workFlow/viewSpFlow?sid="+sid+"&instanceId="+busId;
					openWinByRight(url);
				}
		},"json");
	}
	
}
//设置模块类型
function searchIndexInModule(busType){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#indexBusType").val(busType);
	$("#indexSearchForm").attr("action","/index/searchIndexInModule");
	$("#indexSearchForm").submit();
}
/**
 * 附件查看
 * @param fileId
 */
function showFile(fileId,busType,busId){
	$.post("/fileCenter/authorCheck?sid="+sid,{Action:"post",busId:busId,busType:busType},     
			function (msgObjs){
			if(!msgObjs.succ){
				showNotification(2,msgObjs.promptMsg);
			}else{
				$.ajax({
					 type : "post",
					 url : '/fileCenter/getFile?sid='+sid+'&rnd='+Math.random(),
					 dataType:"json",
					 data:{fileId:fileId},
					 beforeSend: function(XMLHttpRequest){},
			         success : function(data){
			        	 if(data.succ=="yes"){
			        		 if(data.fileExt=='doc' || data.fileExt=='docx' || data.fileExt=='xls' || data.fileExt=='xlsx' 
			        			 || data.fileExt=='ppt' || data.fileExt=='pptx' || data.fileExt=='txt' || data.fileExt=='pdf'){
			        			 viewOfficePage(fileId,data.uuid,data.filename,data.fileExt,sid,busType,busId)
			        		 }else if(data.fileExt=='jpg'||data.fileExt=='bmp'||data.fileExt=='gif'||data.fileExt=='jpeg'||data.fileExt=='png'){
			        			 var filepath ="/downLoad/down/"+data.uuid+"/"+data.filename;
			        			 showPic(filepath,sid,fileId,busType,busId);
			        		 }else{
			        			 showNotification(2,"不支持此类文档在线查看。");
			        		 }
			        	 }
			         },
			         error:  function(XMLHttpRequest, textStatus, errorThrown){
			       	  showNotification(2,"系统错误，请联系管理人员！")
				     }
				});
			}
	},"json");
	
}