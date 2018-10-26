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
<body>
	<div id="tab1" class="tab-pane in active">
        <div class="panel-body no-padding">
            <div class="widget no-header">
                <div class="widget-body bordered-radius">
                    <div class="tickets-container tickets-bg tickets-pd clearfix">
                        <ul class="tickets-list clearfix">
                            <li class="ticket-item no-shadow clearfix ticket-normal">
                                <div class="pull-left taskDetails-left-text ">
                                    <div class="ticket-user other-user-box pull-left no-padding">
                                        <img src="/downLoad/userImg/${userInfo.comId}/${task.owner}" 
											class="user-avatar" title="${task.ownerName}" />
										<span class="user-name">${task.ownerName}</span>
                                    </div>
                                </div>
                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7 margin-top-10">
                                    <span>${task.taskName}</span>
                                    <input type="hidden" id="schTaskName" value="${task.taskName}" >
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="widget no-header">
                <div class="widget-body bordered-radius">
                    <div class="task-describe clearfix">
                        <div class="tickets-container tickets-bg tickets-pd clearfix">
                            <ul class="tickets-list clearfix">
                                <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-4 col-sm-4 col-xs-4">
                                	<div class="pull-left taskDetails-left-text task-height">办理类型：</div>
                                       <div class="pull-left col-lg-5 col-sm-5 col-xs-5 margin-top-5">
                                       		<span>
	                                       		<tags:viewDataDic type="taskType" code="${task.taskType}" ></tags:viewDataDic>
                                       		</span>
                                       </div>
                                   </li>
                                <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-4 col-sm-4 col-xs-4">
                                    <div class="pull-left taskDetails-left-text task-height">完成时限：</div>
                                    <div class="pull-left col-lg-5 col-sm-5 col-xs-5 margin-top-5" style="width:100px;">
                                        <span>${task.dealTimeLimit}</span>
                                        <input type="hidden" id="schDealTimeLimit" value="${task.dealTimeLimit}" >
                                    </div>
                                </li>
                                <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-4 col-sm-4 col-xs-4">
                                	<c:set var="gradeColor">
										<c:choose>
											<c:when test="${task.grade==4}">red</c:when>
											<c:when test="${task.grade==3}">red</c:when>
											<c:when test="${task.grade==2}">orange</c:when>
											<c:when test="${task.grade==1}">green</c:when>
										</c:choose>
									</c:set>
                                    <div class="pull-left taskDetails-left-text task-height">
                                                                                                    紧急度：
                                    </div>
                                    <div class="pull-left col-lg-5 col-sm-5 col-xs-5 ${gradeColor} margin-top-5">
                                        <span><tags:viewDataDic type="grade" code="${task.grade}"></tags:viewDataDic></span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="widget no-header" style="display:${empty task.taskRemark?'none':'block'}">
                <div class="widget-body bordered-radius">
                    <div class="tickets-container tickets-bg tickets-pd">
                        <ul class="tickets-list">
                            <li class="ticket-item no-shadow clearfix ticket-normal">
                                <div class="pull-left taskDetails-left-text">
                                                                                         描述：
                                </div>
                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
                                    <div class="row">
                                        <c:choose>
											<c:when
												test="${1==task.state && task.owner==userInfo.id && empty editTask}">
												<textarea class="form-control" id="taskRemark" name="taskRemark"
													rows="" cols="" style="width:620px;height: 110px;display:none;"
													name="taskRemark">${task.taskRemark}</textarea>
												<iframe ID="eWebEditor1"
													src="/static/plugins/ewebeditor/ewebeditor.htm?id=taskRemark&style=blue"
													frameborder="0" scrolling="no" width="620px" height="280"></iframe>
												<button type="button" onclick="saveRemark()"
													class="btn btn-info ws-btnBlue pull-right">保存</button>
											</c:when>
											<c:otherwise>
												<div>
													<div class="comment" id="comment" style="width: 100%">
														${task.taskRemark}</div>
												</div>
											</c:otherwise>
										</c:choose>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            
            <input type="hidden" id="curProgress" value="${task.executeProgress}">
            <c:if test="${task.state ne 4 }">
				<!-- 任务推送人员 -->
				<div class="widget no-header">
	                <div class="widget-body bordered-radius">
	                    <div class="task-describe clearfix">
	                        <div class="tickets-container tickets-bg tickets-pd clearfix">
	                            <ul class="tickets-list clearfix">
	                                <li>
	                                <table width="100%" cellspacing="0" cellpadding="0" border="0">
										<thead>
											<tr class="padding-top-10">
												<th width="10%" class="text-center">序号</th>
												<th width="15%">办理人</th>
												<th  width="15%">办理时限</th>
												<th>进度</th>
												<th width="10%" class="text-center">状态</th>
											</tr>
										</thead>
										<tbody>
												<c:forEach items="${task.listTaskExecutor}" var="taskExecutor" varStatus="status">
													<tr style="height: 45px">
				                                        <td class="text-center">${status.count}</td>
				                                        <td class="text-center">
				                                            <div class="ticket-item no-shadow clearfix ticket-normal">
				                                                <div class="ticket-user pull-left no-padding">
				                                                	<img src="/downLoad/userImg/${taskExecutor.comId}/${taskExecutor.executor}"
																		class="user-avatar" title="${taskExecutor.executorName}" />
																	<span class="user-name">${taskExecutor.executorName}</span>
				                                                </div>
				                                            </div>
				                                        </td>
				                                        <td>${empty taskExecutor.handTimeLimit?'--':taskExecutor.handTimeLimit}</td>
				                                        <td>
				                                        
					                                    	<c:choose>
																<c:when
																	test="${taskExecutor.executor eq userInfo.id && taskExecutor.state eq 1 && empty editTask}">
						                                        	<input type="hidden" id="curProgress" value="${taskExecutor.taskProgress}">
							                                        <!-- todo 可拖拽进度条-->
							                                        <div class="progress-drag pull-left">
							                                            <div class="progress-drag-bg">
							                                                <div class="progress-drag-bar"></div>
							                                            </div>
							                                            <div class="progress-drag-btn" taskProgress="${taskExecutor.taskProgress}"></div>
							                                        </div>
							                                        <div class="progress-drag-text pull-left margin-left-10">${taskExecutor.taskProgress}%</div> 
																</c:when>
																<c:otherwise>
																	<!-- todo 查看进度条-->
							                                      	<div class="progress progress-lg progress-bordered margin-bottom-0 pull-left" style="width: 300px">
							                                            <div class="progress-bar" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width:${taskExecutor.taskProgress}%;height:18px">
							                                            </div>
							                                        </div>
							                                        <div class="pull-left margin-left-10" style="width: 10%;text-align: center;line-height: 30px;font-size: 16px;">
							                                        	${taskExecutor.taskProgress}%
							                                        </div>
																</c:otherwise>
															</c:choose>
				                                        </td>
				                                        <td width="10%" class="text-center">
															<c:choose>
																<c:when test="${taskExecutor.state==0}"><font style="color:blue;">待认领</font></c:when>
																<c:when test="${taskExecutor.state==3}"><font style="color:gray;">已暂停</font></c:when>
																<c:when test="${taskExecutor.state==4}"><font style="color:#FF0000;">已完成</font></c:when>
																<c:otherwise><font style="color:green;">进行中</font></c:otherwise>
															</c:choose>
				                                        </td>
				                                    </tr>
												</c:forEach>
										
										</tbody>
	                                </table>
	                            </li>
	                            </ul>
	                        </div>
	                    </div>
	                </div>
	            </div>
            </c:if>
            
            <div class="widget no-header" style="display:${(not empty listSonTask || task.executeState eq 1 )?'block':'none'}">
                <div class="widget-body bordered-radius">
                    <div class="tickets-container tickets-bg tickets-pd">
                        <ul class="tickets-list">
                            <li class="ticket-item no-shadow clearfix ticket-normal">
                                <div class="pull-left task-left-text task-height">
                                    <i class="fa fa-list padding-right-5"></i>分解任务
                                </div>
                                <div class="pull-right ">
                                    <a href="javascript:void(0);" class="form-control margin-bottom-5 resolveTask" style="display:${(task.state==1 and task.executeState eq 1)?'block':'none'}">+任务分解</a>
                                </div>
                            </li>
                            <li>
                                <table width="100%" cellspacing="0" cellpadding="0" border="0" class="task-table">
                                	<c:choose>
										<c:when test="${not empty listSonTask}">
											<thead>
												<tr class="padding-top-10">
													<th width="10%" class="text-center">序号</th>
													<th>任务</th>
													<th width="15%">办理人</th>
													<th width="15%">进度</th>
													<th width="10%" class="text-center">状态</th>
												</tr>
											</thead>
											<tbody id="sonTaskRow">
											<c:set var="subIndex" value="0"></c:set>
											<c:forEach items="${listSonTask}" var="sonTask"
												varStatus="status">
												<input type="hidden" name="sonTaskstate" value="${empty sonTask.state?'1':sonTask.state}">
													<c:choose>
														<c:when test="${empty sonTask.listTaskExecutor }">
															<c:set var="subIndex" value="${subIndex+1}"></c:set>
															<tr>
						                                        <td width="10%" class="text-center">${subIndex}</td>
						                                        <td><a href="javascript:void(0)" class="taskView" taskId="${sonTask.id}">${sonTask.taskName}</a></td>
			                                        			<td>--</td>
			                                        			<td>--</td>
			                                        			<td><font style="color:#FF0000;">已完成</font></td>
		                                        			</tr>
														</c:when>
														<c:otherwise>
															<c:forEach items="${sonTask.listTaskExecutor}" var="sonTaskExe">
																<c:set var="subIndex" value="${subIndex+1}"></c:set>
																<tr>
							                                        <td width="10%" class="text-center">${subIndex}</td>
							                                        <td><a href="javascript:void(0)" class="taskView" taskId="${sonTask.id}">${sonTask.taskName}</a></td>
							                                        <td width="15%">
							                                            <div class="ticket-item no-shadow clearfix ticket-normal">
							                                                <div class="ticket-user pull-left no-padding">
							                                                	<img src="/downLoad/userImg/${sonTaskExe.comId}/${sonTaskExe.executor}"  
							                                                	title="${sonTaskExe.executorName}" class="user-avatar ">
																				<span class="user-name">${sonTaskExe.executorName}</span>
							                                                </div>
							                                            </div>
							                                        </td>
							                                        
							                                        
							                                        <td>
							                                        	<c:choose>
							                                        		<c:when test="${sonTaskExe.state eq 1 }">
						                                        				<div class="ticket-item no-shadow clearfix ticket-normal" style="height: 30px;vertical-align: middle;line-height: 35px">
										                                            ${empty sonTask.taskProgress?0:sonTask.taskProgress}%
						                                        				</div>
							                                        		</c:when>
							                                        		<c:otherwise>
							                                        			--
							                                        		</c:otherwise>
							                                        	</c:choose>
							                                        </td>
							                                        <td width="10%" class="text-center">
																		<div class="ticket-item no-shadow clearfix ticket-normal" style="height: 30px;vertical-align: middle;line-height: 35px">
																			<c:choose>
																				<c:when test="${sonTaskExe.state==0}"><font style="color:blue;">待认领</font></c:when>
																				<c:when test="${sonTaskExe.state==3}"><font style="color:gray;">已挂起</font></c:when>
																				<c:when test="${sonTaskExe.state==4}">
																					<font style="color:#FF0000;">已完成</font>
																				</c:when>
																				<c:otherwise><font style="color:green;">进行中</font></c:otherwise>
																			</c:choose>
					                                        			</div>
							                                        </td>
							                                    </tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
													
											</c:forEach>
											</tbody>
										</c:when>
									</c:choose>
                                    
                                </table>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="widget no-header" style="display:${(empty task.parentId or task.parentId<=0)?'none':'block'}">
                <div class="widget-body bordered-radius">
                    <div class="tickets-container tickets-bg tickets-pd">
                        <ul class="tickets-list">
                            <li class="ticket-item no-shadow clearfix ticket-normal">
                                <div class="pull-left taskDetails-left-text padding-top-5">
                                                                                            父任务：
                                </div>
                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
                                    <a href="javascript:void(0);" class="pull-left taskView"
										style="font-size:10px;margin-top:8px;" taskId="${task.parentId}">
										${task.pTaskName}
									</a>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="widget no-header relativeRow" busType="${task.busType}" 
            	style="display: ${(not empty task.busType and '0' ne task.busType)?'block':'none'}">
                <div class="widget-body bordered-radius">
                    <div class="tickets-container tickets-bg tickets-pd">
                        <ul class="tickets-list">
                            <li class="ticket-item no-shadow clearfix ticket-normal">
                                <div class="pull-left task-left-text task-height">
									${task.busType=='005'?'关联项目':task.busType=='012'?'关联客户':'关联模块'}：
                                </div>
                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
                                    <div class="row">
                                        <div class="pull-left ps-right-box"
											readonly="readonly" style="margin-top:8px;">
											<a href="javascript:void(0);" class="relateClz" busType="${task.busType}" busId="${task.busId}">${task.busName}</a>
										</div>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
	    /*可拖拽进度条*/
	    $(function(){
	        var tag = false,ox = 0,left = 0,bgleft = 0;
 			var curProgress = $("#curProgress").val();//获取当前任务进度
	        
	        left = Number(curProgress) * 3;
	        $('.progress-drag-btn').mousedown(function(e) {
	            ox = e.pageX - left;     tag = true;    });
	        $(document).mouseup(function() {
	            tag = false;
	        });
	        $('.progress-drag').mousemove(function(e) { //鼠标移动
	            if (tag) {
	                left = e.pageX - ox;
	                if (left <= 0) {
	                    left = 0;
	                }else if (left > 300) {
	                    left = 300;
	                }
	                $('.progress-drag-btn').css('left', left);
	                $('.progress-drag-bar').width(left);
	                $('.progress-drag-text').html(parseInt((left/300)*100) + '%');
	            }
	        });
	        /* $('.progress-drag-bg').click(function(e) {//鼠标点击
	            if (!tag) {      
	            	bgleft = $('.progress-drag-bg').offset().left;
	                left = e.pageX - bgleft;
	                if (left <= 0) {
	                    left = 0;
	                }else if (left > 300) {
	                    left = 300;
	                }
	                $('.progress-drag-btn').css('left', left);
	                $('.progress-drag-bar').animate({width:left},300);
	                $('.progress-drag-text').html(parseInt((left/300)*100) + '%');
	            }
	        }); */
	        var taskProgress = $('.progress-drag-text').html();
	        //任务进度描述
            $('.progress-drag-btn').css('left',taskProgress);
            $('.progress-drag-bar').width(taskProgress);
            $('.progress-drag-text').html(taskProgress);
            
          	//初始化触发事件
			taskOptForm.initEvent();
	    });
	    var taskObj = {
	    		"taskId":"${task.id}",
	    		"state":"${task.state}",
	    		"executState":"${(userInfo.id==task.executor and task.executeState ne 4)?'1':'0'}",
	    		"ownState":"${userInfo.id==task.owner?'1':'0'}"
	    }
	    var taskOptForm = {
	    		taskOptMenu:function(){
	    			//添加留言按钮
	    			taskOptForm.addTaskTalkMenu(taskObj.state);
	    			//添加认领按钮
	    			taskOptForm.addTaskConfirmMenu(taskObj.state,taskObj.executState);
	    			//添加委托按钮
	    			taskOptForm.addTaskNextMenu(taskObj.state,taskObj.executState);
	    			//添加重启按钮
	    			taskOptForm.addTaskRestrtMenu(taskObj.state,taskObj.ownState);
	    			//添加更多按钮
	    			taskOptForm.addTaskMoreMenu(taskObj.state,taskObj.ownState,taskObj.executState);
	    		},addTaskTalkMenu:function(state){
	    			if(state == '0' || state == '1' ){//任务未认领或是办理中留言
	    				var _talkA = $('<a href="javascript:void(0)" class="purple" id="headTaskTalk" title="任务留言"></a>');
	    				$(_talkA).html('<i class="fa fa-comments"></i>留言');
	    				$(".taskOptMenu").append($(_talkA))
	    			}
	    		},addTaskConfirmMenu:function(state,executState){
	    			if(state == '0' && executState=='1'){//任务未认领，且当前人员是办理人员，则添加认领
	    				var _acceptA = $('<a href="javascript:void(0)" class="purple" id="acceptTask" title="任务认领"></a>');
	    				$(_acceptA).html('<i class="fa fa-comments"></i>认领');
	    				$(".taskOptMenu").append($(_acceptA))
	    			}
	    		},addTaskNextMenu:function(state,executState){
	    			if(state == '1' && executState=='1'){//任务办理中，且当前人员是办理人员，则添加委托
	    				
	    				/* var _nextA = $('<a href="javascript:void(0)" class="blue" id="nextExecutor" title="任务转办"></a>');
	    				$(_nextA).html('<i class="fa fa-h-square"></i>转办');
	    				$(".taskOptMenu").append($(_nextA));
	    				
	    				var _finishA = $('<a href="javascript:void(0)" class="red" id="turnBackTask" title="任务完成"></a>');
	    				$(_finishA).html('<i class="fa fa-power-off"></i>完成');
	    				$(".taskOptMenu").append($(_finishA)); */
	    				
	    				var _nextA = $('<a href="javascript:void(0)" class="blue" id="handOverTask" title="任务委托"></a>');
	    				$(_nextA).html('<i class="fa fa-h-square"></i>委托');
	    				$(".taskOptMenu").append($(_nextA));
	    				
	    			}
	    		},addTaskRestrtMenu:function(state,ownState){
	    			if((state == '3' || state == '4')  && ownState=='1'){//任务已办结或是暂停状态
	    				var _restartA = $('<a href="javascript:void(0)" class="green restartTaskBtn" title="重新启动任务"></a>');
	    				$(_restartA).html('<i class="fa fa-power-off"></i>重启');
	    				$(".taskOptMenu").append($(_restartA))
	    			}
	    		},addTaskMoreMenu:function(state,ownState,executState){
	    			var _moreA = $('<a class="green ps-point margin-right-0" data-toggle="dropdown" title="更多操作"></a>');
    				$(_moreA).html(' <i class="fa fa-th"></i>更多');
    				$(".taskOptMenu").append($(_moreA));
    				
    				//更多的下拉
    				var _moreUl = $('<ul class="pull-right dropdown-menu dropdown-arrow ps-layerUl"></ul>');
    				
    				//任务分解
    				if(state == '1' && executState=='1'){//任务办理中，且当前人员是办理人员，则添加任务分解,任务报延
    					var _resolveLi = $('<li></li>');
    				
    					var _resolveA = $('<a href="javascript:void(0)" class="resolveTask"></a>');
	    				$(_resolveA).html('<div class="clearfix"><i class="fa fa-flag"></i><span class="title ps-topmargin">任务分解</span></div>');
	    				
	    				$(_resolveLi).append($(_resolveA))
	    				$(_moreUl).append($(_resolveLi));
	    				
    					var _delayLi = $('<li></li>');
    					var _delayA =$('<a href="javascript:void(0)" class="delayApplyBtn"></a>');
	    				$(_delayA).html('<div class="clearfix"><i class="fa fa-calendar-check-o"></i><span class="title ps-topmargin">任务报延</span></div>');
	    				$(_delayLi).append($(_delayA))
	    				$(_moreUl).append($(_delayLi));
    				}
    				if(state == '1' && ownState=='1'){//任务暂停
    					var _pauseLi = $('<li></li>');
    					var _pauseA = $('<a href="javascript:void(0)" class="pauseTaskBtn"></a>');
	    				$(_pauseA).html('<div class="clearfix"><i class="fa fa-pause-circle-o"></i><span class="title ps-topmargin">任务暂停</span></div>');
	    				
	    				$(_pauseLi).append($(_pauseA))
	    				$(_moreUl).append($(_pauseLi));
    				}
					if(state != '4' && (executState=='1' || ownState=='1') ){//除开已完成的任务都可以转为日程
						var _conversionLi = $('<li></li>');
						var _conversionA = $('<a href="javascript:void(0)" class="conversionBtn"></a>');
						$(_conversionA).html('<div class="clearfix"><i class="fa fa-calendar-o"></i><span class="title ps-topmargin">转为日程</span></div>');

						$(_conversionLi).append($(_conversionA))
						$(_moreUl).append($(_conversionLi));
					}
					
					
                      var _copyLi = $('<li></li>');
                      var _copyA = $('<a href="javascript:void(0)" class="copyBtn"></a>');
                      $(_copyA).html('<div class="clearfix"><i class="fa fa-copy"></i><span class="title ps-topmargin">任务复制</span></div>');

                      $(_copyLi).append($(_copyA))
                      $(_moreUl).append($(_copyLi));
		
					
    				//添加闹铃提示
    				
    				var _clockLi = $('<li></li>');
    				
    				var _clockA = $('<a href="javascript:void(0)"></a>');
    				$(_clockA).attr("onclick","addClock(${task.id},'003','${param.sid}')");
	    			$(_clockA).html('<div class="clearfix"><i class="fa fa-clock-o"></i><span class="title ps-topmargin">定时提醒</span> <span class="title ps-topmargin blue fa fa-plus padding-left-30"></span></div>');
    				$(_clockLi).append($(_clockA));
    				
    				var _subUl = $(' <ul id="busClockList"></ul>');
	    			$(_clockLi).append($(_subUl));
	    			
	    			$(_moreUl).append($(_clockLi));
	    			
	    			$(".taskOptMenu").append($(_moreUl));
    				
	    		},initEvent:function(){
	    			//初始化触发事件
	    			taskOptForm.taskOptMenu();
	    			
    				//认领事项
	    			$("body").on("click","#acceptTask",function(){
	    				taskOptForm.acceptTask();
	    			})
	    		},acceptTask:function(){
	    			window.top.layer.confirm("确定认领任务！<br>认领任务后,将由自己办理",{icon: 3, title:'确认对话框'},function(index){
	    				window.top.layer.close(index);
	    				var acceptUrl = "/task/acceptTask";
	    				var param = {"sid":sid,"taskId":taskObj.taskId}
	    				postUrl(acceptUrl,param,function(data){
	    					if(data.status=='y'){
	    						window.self.location.reload();
	    					}
	    				});
	    			})
	    		},updateTaskProgressEvent:function(progressVal,callback){
	    			var param = {
    					"sid":sid,
    					"id":taskObj.taskId,
    					"taskProgress":progressVal
    				}
    				postUrl("/task/taskProgressReport",param,function(data){
    					//来源页面
   		 				if(data.status=='y'){
   		 					callback(data)
   		 				}else{
   		 					showNotification(1,data.info);
   		 				}
    				})
	    		},updateTaskProgress:function(){
	    			var proText = $(".progress-drag-text").text();
	    			var progressVal = proText.substring(0,proText.indexOf("%"));
	    			var curProgress = $("#curProgress").val();//获取当前任务进度
	    			$("#curProgress").val(progressVal);//更改当前进度值
	    			if(progressVal!=curProgress){//有任务变动才修改进度
	    				if(progressVal==100 ){//任务已完成
	    					$("body").find("#handOverTask").trigger("click");
	    				}else{
			    			taskOptForm.updateTaskProgressEvent(progressVal,function(data){
			    				if(progressVal==100 ){//任务已完成
			    					//nextExcuter(1);
			    					handOverTask();
			    				}else{
			    					showNotification(1,data.info);
			    				}
			    			})
	    				}
	    				
	    			}
	    		}
	    }
    </script>
</body>
</html>
