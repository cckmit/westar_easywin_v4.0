//任务添加界面
	var taskOptForm = {
		chooseExectors:function(ts){
			var relateSelect = $(ts).attr("relateSelect");
			var relateImgDiv = $(ts).attr("relateImgDiv");
			userMore(relateSelect, 'yes', pageParam.sid,null,null,function(options){
				var userIds = taskOptForm.optUserData(options,relateSelect,relateImgDiv);
				//异步加载头像信息
				taskOptForm.loadUserImg(userIds);
			})
		},appendUsedUser:function(userObj,relateSelect,relateImgDiv){
			var userId = userObj.id;
			var optObj = $("#"+relateSelect).find("option[value='"+userId+"']");
			if(!optObj || !optObj.get(0)){
				var option = $("<option></option>");
				$(option).attr("value",userObj.id);
				$(option).attr("selected","selected");
				$(option).html(userObj.userName);
				$("#"+relateSelect).append($(option));
				
				//添加头像
				var headImgDiv = $('<div  class="online-list " style="float:left;padding-top:5px" title="双击移除"></div>');
				$(headImgDiv).data("userId",userObj.id);
				var headImg = $('<img src="/downLoad/userImg/' + userObj.comId + '/' + userObj.id + '" class="user-avatar"/>');
				var headImgName = $('<span class="user-name"></span>');
				$(headImgName).html(userObj.userName);
				
				$(headImgDiv).append($(headImg));
				$(headImgDiv).append($(headImgName));
				
				$("#"+relateImgDiv).append($(headImgDiv));
				
				$(headImgDiv).on("dblclick",function(){
					var userId = $(this).data("userId");
					$(this).remove();
					$("#"+relateSelect).find("option[value='"+userId+"']").remove();
				})
				
			}
		},optUserData:function(options,relateSelect,relateImgDiv){
			var userIds =new Array();
			//首先清除数据
			$("#"+relateSelect).html('');
			$("#"+relateImgDiv).html('');
			if(options && options[0]){
				$.each(options,function(index,opt){
					userIds.push(opt.value);
					var option = $("<option></option>");
					$(option).attr("value",opt.value);
					$(option).attr("selected","selected");
					$(option).html(opt.text);
					$("#"+relateSelect).append($(option));
					
					//添加头像
					var headImgDiv = $('<div  class="online-list " style="float:left;padding-top:5px" title="双击移除"></div>');
					$(headImgDiv).data("userId",opt.value);
					var headImg = $('<img src="/downLoad/userImg/'+pageParam.comId+'/'+opt.value+'" class="user-avatar"/>')
					$(headImg).attr("id","userImg_"+opt.value);
					var headImgName = $('<span class="user-name"></span>');
					$(headImgName).html(opt.text);
					
					$(headImgDiv).append($(headImg));
					$(headImgDiv).append($(headImgName));
					
					$("#"+relateImgDiv).append($(headImgDiv));
					
					$(headImgDiv).on("dblclick",function(){
						var userId = $(this).data("userId");
						$(this).remove();
						$("#"+relateSelect).find("option[value='"+userId+"']").remove();
					})
					
				})
			}
			return userIds;
		},loadUserImg:function(userIds){
			var param = {
				 "sid":pageParam.sid,
				 "userIds":userIds.toString()
		 	}
			postUrl("/userInfo/addUsedUser",param,function(result){
				var code = result.code;
				if(code == 0){
					var comId = result.data;
					$.each(userIds,function(index,userId){
						var imgObj = $("#userImg_"+userId);
						var imgSrc = "/downLoad/userImg/"+comId+"/"+userId+"?sid="+pageParam.sid;
						$(imgObj).attr("src",imgSrc);
					})
				}
			})
		}
	}
	
	var taskEditForm = {
			init:function(){
				//任务分解
				$("body").on("click",".resolveTask",function(){
					taskEditForm.resolveTask();
				})
				
				//关联li点击事件定义
				$("body").on("click","#moreOpt li",function(){
					var actObj = $(this);
					var busType = $(actObj).attr("busType");
					if(busType=="012"){
						$(".relativeRow").remove();//暂时先单选关联
						crmMoreSelect(1,null,function(crms){
							var busId = crms[0].id;
							var modName = crms[0].crmName;
							taskEditForm.taskBusIdUpdate(busId,busType,modName)
						})
					}else if(busType=="005"){
						$(".relativeRow").remove();//暂时先单选关联
						itemMoreSelect(1, null,function(items){
							var busId = items[0].id;
							var modName = items[0].itemName;
							taskEditForm.taskBusIdUpdate(busId,busType,modName)
						})
					}else if(busType=="022"){
						$(".relativeRow").remove();//暂时先单选关联
						spFlowMoreSelect(1, null,function(spFlow){
						    var busId = spFlows[0].id;
							var modName = spFlows[0].flowName;
							taskEditForm.taskBusIdUpdate(busId,busType,modName)
						})
					}else if(busType=="070"){
						$(".relativeRow").remove();//暂时先单选关联
						demandSelect(1, null,function(demand){
							var busId = demand[0].id;
							var modName = demand[0].serialNum;
							taskEditForm.taskBusIdUpdate(busId,busType,modName)
			        	})
					}else if(busType=="003"){//任务关联
						var oldId = EasyWin.task.id;
						listTaskForRelevance(EasyWin.sid,function(pTask){
			                taskForRelevanceBack(pTask);
						})
					}
				});
				//关联控件点击删除绑定
				$("body").on("click",".colDel",function(){
					$(".subform [name='busType']").val("");
					$(".subform [name='busId']").val("");
					var actObj = $(this);
					$(actObj).parents(".relativeRow[busType='"+$(actObj).attr("busType")+"']").remove();
				});
				
				//任务报延申请
				$("body").on("click",".delayApplyBtn",function(){
					taskEditForm.delayApply();
				});
				
				//任务名称更新
				$("#taskName").change(function(){
					//任务名称
					var taskName = $("#taskName").val();
					if(regex.test(taskName)){
						return false;
					}
					//关联项目的长度，汉字算两个长度
					var count = taskName.replace(/[^\x00-\xff]/g,"**").length;
					var len = $("#taskName").attr("defaultLen");
					//关联项目长度超过指定的长度
					if(count>len ||　!taskName){
						return false;
					}else{
						var url = "/task/taskNameUpdate"
							var param = {
								"sid":EasyWin.sid
								,"id":EasyWin.task.id
								,"taskName":taskName
						}
						postUrl(url,param,function(msgObjs){
							showNotification(1,msgObjs);
							$("#taskName").attr("title",taskName);
							taskName = '--'+cutstr(taskName,32);
							$("#titleTaskName").html(taskName);
						})
					}
				});
				//任务名称更新
				$("#expectTime").change(function(){
					//任务名称
					var expectTime = $("#expectTime").val();
					if(regex.test(expectTime)){
						return false;
					}
					if(checkOpt.money(expectTime)){
						var url = "/task/taskExpectTimeUpdate"
							var param = {
								"sid":EasyWin.sid
								,"id":EasyWin.task.id
								,"expectTime":expectTime
						}
						postUrl(url,param,function(result){
							if(result.code == 0){
								showNotification(1,result.data);
							}else{
								showNotification(2,result.msg);
							}
						})
					}
					
				});
				
				//任务紧急度更新
				$("#grade").change(function(){
					//任务紧急度
					if($("#grade").val()){
						var url = "/task/taskGradeUpdate"
						var param = {
				    			"sid":EasyWin.sid
				    			,"id":EasyWin.task.id
				    			,"grade":$("#grade").val()
				    	}
						postUrl(url,param,function(data){
							if(data.status=='y'){
								showNotification(1,data.info);
				 			}else{
								showNotification(2,data.info);
					 		}
						})
					}
				});
				
				//任务说明更新
				$("#taskRemark").change(function(){
					if(!strIsNull($("#taskRemark").val())){
						var url = "/task/taskTaskRemarkUpdate"
						var param = {
				    			"sid":EasyWin.sid
				    			,"id":EasyWin.task.id
				    			,"taskRemark":$("#taskRemark").val()
				    	}
						postUrl(url,param,function(data){
							if(data.status=='y'){
								showNotification(1,data.info);
				 			}else{
								showNotification(2,data.info);
					 		}
						})
					}
				});
				
				//任务协同
				$("body").on("click","#nextExecutor",function(){
					var url = "/task/checkTaskStateForNextExecutor?sid="+EasyWin.sid
					url = url + "&id="+EasyWin.task.id
					$.post(url,{Action:"post"},     
					 	function (msgObjs){
						if(!msgObjs["succ"]){
							showNotification(2,msgObjs.promptMsg);
						}else{
							taskEditForm.nextExcuter();
						}
					},"json");
					
				})
				
				//暂停执行该任务
				$("body").on("click",".pauseExexuteTaskBtn",function(){
					taskEditForm.pauseExexuteTaskBtn();
				})
				//开始办理该任务
				$("body").on("click",".startExexuteTaskBtn",function(){
					taskEditForm.startExexuteTaskBtn();
				})
				
				//任务协同
				$("body").on("click","#handOverTask",function(){
					$.post("/task/checkTaskStateForNextExecutor?sid="+EasyWin.sid+"&id="+EasyWin.task.id,{Action:"post"},     
					 	function (msgObjs){
						if(!msgObjs["succ"]){
							showNotification(2,msgObjs.promptMsg);
						}else{
							taskEditForm.handOverTask();
						}
					},"json");
					
				})
				
				//查看关联
				$("body").on("click",".relateClz",function(){
					var actObj = $(this);
					var busId = $(actObj).attr("busId");
					var busType = $(actObj).attr("busType");
					var param = {
							busId:busId,
							busType:busType,
							clockId:-1,
							baseId:EasyWin.task.id
					}
					authBaseCheck(param,function(authState){
						var url = "";
						if(busType=='005'){
							url = "/item/viewItemPage?sid="+EasyWin.sid+"&id="+busId;
						}else if(busType=='012'){
							url = "/crm/viewCustomer?sid="+EasyWin.sid+"&id="+busId;
						}else if(busType=='003'){
							url = "/task/viewTask?sid="+EasyWin.sid+"&id="+busId;
						}else if(busType=='022'){
							url = "/workFlow/viewSpFlow?sid="+EasyWin.sid+"&instanceId="+busId;
						}else if(busType=='070'){
							var url = "/demand/viewDemandPage?sid="+EasyWin.sid;
							url = url + "&demandId="+busId;
						}
						openWinWithPams(url,"800px",($(window).height()-90)+"px");
					})
				});
				
				//展开跟多的区域
				$(".inner").click(function(){
			        var moreOptShow = $("#moreOpt").css("display");
			        if(moreOptShow=='none'){
			            $("#moreOpt").slideDown("fast");
			            $("#moreOptImg").removeClass("fa-angle-down");
			            $("#moreOptImg").addClass("fa-angle-up");
			            document.getElementById('task-shrink-name').innerHTML="收起"
			        }else{
			            $("#moreOpt").slideUp("fast");
			            $("#moreOptImg").removeClass("fa-angle-up");
			            $("#moreOptImg").addClass("fa-angle-down");
			            document.getElementById('task-shrink-name').innerHTML="更多及事件关联"
			        }
			    });
				//输入文本框双击删除绑定
				$("body").on("dblclick",".colInput",function(){
					var actObj = $(this);
					var busType = $(actObj).attr("busType");
					var busId = $(actObj).attr("busId");
					var taskId = EasyWin.task.id;
					
					var url = "/task/delTaskBusRelation";
					var param = {
						"id":EasyWin.task.id	
						,"sid":EasyWin.sid
						,"busId":busId
						,"busType":busType
					}
					postUrl(url,param,function(data){
						if(data.status=='y'){
							showNotification(1,data.info);
							$(actObj).parents(".relativeRow[busType='"+$(actObj).attr("busType")+"']").remove();
						}else{
							showNotification(2,data.info);
						}
					})
				});
				
				//父级任务关联
				$("body").on("click",".pTaskRalation",function(){
					var actObj = $(this);
					var oldId = EasyWin.task.id;
					listTaskForRelevance(EasyWin.sid,function(pTask){
						if(pTask.id==oldId){
							showNotification(2,"不能与自己关联");
						}else if(oldId!=pTask.id){
							if(!strIsNull(pTask.id) && pTask.id>0){
								$.post("/task/taskParentIdUpdate?sid="+EasyWin.sid,{Action:"post",id:EasyWin.task.id,parentId:pTask.id},     
									function (data){
										if(data.status=='y'){
											window.self.location.reload();//因此项是其它关联事项关联的前提，所以需要刷新页面
										}else{
											showNotification(2,data.info);
										}
								},"json");
							}
						}
					})
				});
				//父级任务关联删除
				$("body").on("dblclick",".pTask",function(){
					var actObj = $(this);
					var pTaskId = $(actObj).attr("taskId");
					var taskId = EasyWin.task.id;
					if(pTaskId){
						$.post("/task/delpTaskRelation?sid="+EasyWin.sid,{Action:"post",id:taskId,parentId:pTaskId},     
							function (msgObjs){
							if(msgObjs["succ"]){
								window.self.location.reload();//因此项是其它关联事项关联的前提，所以需要刷新页面
							}else{
								showNotification(2,msgObjs.promptMsg);
							}
						},"json");
					}
				});
				
				
			},resolveTask:function(){
				var url = "/task/resolveTaskPage?sid="+EasyWin.sid;
				url = url+"&parentId="+EasyWin.task.id
				window.top.layer.open({
					 type: 2,
					 title:false,
					 closeBtn: 0,
					  area: ['800px', '550px'],
					  fix: true, //不固定
					  maxmin: false,
					  content: [url,'no'],
					  btn: ['发布','取消'],
					  yes: function(index, layero){
						  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
						  var result = iframeWin.formSub();
						  if(result){
							  window.self.location.reload();
							  window.top.layer.close(index);
							  window.top.layer.msg("创建成功",{icon:1})
						  }
					  },cancel: function(){
					  }
				});
			},taskBusIdUpdate:function(busId,busType,modName){
				var rowObj = initModRelateStyle(busType);
			    $(rowObj).find("input[busType='"+busType+"']").val(modName);
			    $(rowObj).find("input[busType='"+busType+"']").attr("busId",busId);
			    $(rowObj).find(".colDel").remove();
			    $(rowObj).find("li").find("div:last").append("<a href=\"javascript:void(0);\" class=\"pull-left margin-top-10 padding-left-5 relateClz\" " +
			        "busType=\""+busType+"\" busId=\""+busId+"\" style=\"font-size:10px;\">查看</a>");
			    if(busId){
			    	var url = "/task/taskBusIdUpdate?sid="+EasyWin.sid;
			    	var param = {
			    			"sid":EasyWin.sid
			    			,"id":EasyWin.task.id
			    			,"busId":busId
			    			,"busType":busType
			    	}
			    	postUrl(url,param,function(data){
			    		 if(data.status=='y'){
			                    $("#moreOpt").parent().before(rowObj);
			                    showNotification(1,data.info);
			                }else{
			                    showNotification(2,data.info);
			                }
			    	})
			    }
			},delayApply:function(){
				
				var url = "/task/delayApplyPage?sid="+EasyWin.sid;
				url = url+"&taskId="+EasyWin.task.id
				window.top.layer.open({
					 type: 2,
					 title:false,
					 closeBtn: 0,
					 area: ['750px', '550px'],
					 fix: true, //不固定
					 maxmin: false,
					 content: [url,'no'],
					 btn: ['申请','取消'],
					 yes: function(index, layero){
					  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
					  var result = iframeWin.formSub();
					  if(result){
					 	  window.top.layer.close(index);
					 	  window.self.location.reload();
					  }
					 },cancel: function(){}
				});
			},nextExcuter:function(){
				
				var url = "/task/nextExecutorPage?sid="+EasyWin.sid;
				url = url+"&id="+EasyWin.task.id
				url = url+"&redirectPage="+encodeURIComponent(EasyWin.redirectPage)
				
				var userId = EasyWin.userInfo.id
				
				window.top.layer.open({
					 type: 2,
					  title: false,
					  closeBtn:0,
					  area: ['600px', '450px'],
					  fix: true, //不固定
					  maxmin: false,
					  move: false,
					  content: [url,'no'],
					  btn: ['确定', '取消'],
					  success: function(layero, index){
						  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
							//关闭窗口
							$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
								  window.top.layer.close(index);
							  });
					  },
					  yes: function(index, layero){
						  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
						  if(iframeWin){
							  var info = iframeWin.formSub();
							  var status = info.status;
							  var nextExecutorId = info.executor;
							  if(status=='f2'){
								  window.top.layer.msg(info.info,{icon: 2,skin:"showNotification",time:1800});
								  return;
							  }
							  if(status == 'y' || status=='f1'){
								window.top.layer.msg(info.info,{icon: (status == 'y'?1:2),skin:"showNotification",time:1800});
							   	window.top.layer.close(index);
							   	if(openPageTag=='index'){//首页
									window.top.layer.close(openTabIndex);
									if(nextExecutorId!=userId){
										openWindow.removeTodoTask(openPageTag,EasyWin.task.id,'003');
									}
							   	}else if(openPageTag=='taskTodo'){//任务待办
							   		window.top.layer.close(openTabIndex);
							   		if(nextExecutorId!=userId){
							   			openWindow.removeTaskTodo(EasyWin.task.id);
										openWindow.loadTaskTodo();
									}
							   	}else if(openPageTag=='allTodo'){//说所有待办
							   		window.top.layer.close(openTabIndex);
							   		if(nextExecutorId!=userId){
										openWindow.removeTaskTodo(EasyWin.task.id);
										openWindow.loadOtherTodo();
									}
								}else{
									window.top.layer.close(openTabIndex);
									openWindow.location.reload();
								}
							  }
						  }
					  }
					  ,cancel: function(){ 
					    //右上角关闭回调
					  }
				});
			},handOverTask:function(){
				//委托给别人执行
				
				var url = "/task/handOverTaskPage?sid="+EasyWin.sid;
				url = url+"&id="+EasyWin.task.id
				url = url+"&redirectPage="+encodeURIComponent(EasyWin.redirectPage)
				
				var userId = EasyWin.userInfo.id
				
				window.top.layer.open({
					 type: 2,
					  //title: ['任务协同', 'font-size:18px;'],
					  title:false,
					  closeBtn:0,
					  area: ['600px', '450px'],
					  fix: true, //不固定
					  maxmin: false,
					  move: false,
					  content: [url,'no'],
					  btn: ['确定', '取消'],
					  success: function(layero, index){
						  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
							//关闭窗口
							$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
								  window.top.layer.close(index);
							  });
					  },
					  yes: function(index, layero){
						  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
						  
						  if(iframeWin){
							  var info = iframeWin.formSub();
							  var status = info.status;
							  var nextExecutorId = info.executor;
							  if(status == 'y' || status=='f1'){
								window.top.layer.msg(info.info,{icon: (status == 'y'?1:2),skin:"showNotification",time:1800});
							   	window.top.layer.close(index);
							   	if(openPageTag=='index'){//首页
									window.top.layer.close(openTabIndex);
									if(nextExecutorId!=userId){
										openWindow.removeTodoTask(openPageTag,EasyWin.task.id,'003');
									}
							   	}else if(openPageTag=='taskTodo'){//任务待办
							   		window.top.layer.close(openTabIndex);
							   		if(nextExecutorId!=userId){
							   			openWindow.removeTaskTodo(EasyWin.task.id);
										openWindow.loadTaskTodo();
									}
							   	}else if(openPageTag=='allTodo'){//说所有待办
							   		window.top.layer.close(openTabIndex);
							   		if(nextExecutorId!=userId){
										openWindow.removeTaskTodo(EasyWin.task.id);
										openWindow.loadOtherTodo();
									}
								}else{
									window.top.layer.close(openTabIndex);
									openWindow.location.reload();
								}
							  }
						  }
					  },cancel: function(){ 
					    //右上角关闭回调
					  }
				});
			},pauseExexuteTaskBtn:function(){
				window.top.layer.confirm("是否暂停自己执行该任务？",{icon:3, title:'确认对话框'}, function(index){
					window.top.layer.close(index);
					taskEditForm.remarkTaskExecuteState({"state":3},function(msg){
						showNotification(1, msg);
						window.self.location.reload();
					});
				});
			},startExexuteTaskBtn:function(){
				window.top.layer.confirm("是否开始执行该任务？",{icon:3, title:'确认对话框'}, function(index){
					window.top.layer.close(index);
					taskEditForm.remarkTaskExecuteState({"state":1},function(msg){
						showNotification(1, msg);
						window.self.location.reload();
					})
				});
			},remarkTaskExecuteState:function(param,callback){
				var params = {
						"sid":EasyWin.sid
						,"id":EasyWin.task.id
						,"version":EasyWin.task.version
				}
				params = $.extend(params,param);
				var url = "/task/remarkTaskExecuteState";
				postUrl(url,params,function(result){
					if(result.code == '0'){
						callback(result.data)
					}else{
						showNotification(2, result.msg);
					}
				})
			}
			
	}