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
                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
                                    <div class="row">
                                        <c:choose>
											<c:when test="${empty editTask && (1==task.state || 0==task.state) && task.owner==userInfo.id}">
												<input id="taskName" datatype="input,sn" defaultLen="52" name="taskName" nullmsg="请填写任务名称" class="colorpicker-default form-control pull-left" type="text" title="${task.taskName}"
													value="${task.taskName}" onpropertychange="handleName()" onkeyup="handleName()" style="width:85%">
												<span id="msgTitle" class="pull-left margin-top-5 padding-left-5" style="width:auto;">(0/26)</span>
											</c:when>
											<c:otherwise>
												<strong>${task.taskName}</strong>
											</c:otherwise>
										</c:choose>
										<input type="hidden" id="schTaskName" value="${task.taskName}" >
										<c:if test="${empty editTask && (1==task.state || 0==task.state) && task.owner==userInfo.id}">
										<script>
											//当状态改变的时候执行的函数 
											function handleName() {
												var value = $('#taskName').val();
												var len = charLength(value.replace(/\s+/g,""));
												if (len % 2 == 1) {
													len = (len + 1) / 2;
												} else {
													len = len / 2;
												}
												if (len > 26) {
													$('#msgTitle').html(
															"(<font color='red'>" + len
																	+ "</font>/26)");
												} else {
													$('#msgTitle').html("(" + len + "/26)");
												}
											}
											//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
											if (/msie/i.test(navigator.userAgent)) { //ie浏览器 
												document.getElementById('taskName').onpropertychange = handleName
											} else {//非ie浏览器，比如Firefox 
												document.getElementById('taskName')
														.addEventListener("input",
																handleName, false);
											}
										</script>
									</c:if>
                                    </div>
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
                                        <div class="pull-left task-left-text task-height">
                                                                                                                    完成时限<span style="color: red">*</span>：
                                        </div>
                                        <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
                                        	<div class="row">
                                        	 <c:choose>
												<c:when test="${(1==task.state || 0==task.state) && task.owner==userInfo.id && empty editTask}">
		                                            <input type="text" class="form-control" placeholder="完成时限"  id="expectTime" name="expectTime" value="${task.expectTime }"/>
													</c:when>
												<c:otherwise>
												${task.expectTime }
												</c:otherwise>
											</c:choose>
											</div>
											
                                        </div>
	                                     <span style="line-height: 30px">小时</span>
                                    </li>
                                <li class="ticket-item no-shadow clearfix ticket-normal pull-left col-lg-4 col-sm-4 col-xs-4">
                                    <div class="pull-left taskDetails-left-text task-height">
                                                                                                    紧急度：
                                    </div>
                                    <div class="pull-left col-lg-5 col-sm-5 col-xs-5">
                                        <div class="row">
                                            <c:choose>
												<c:when
													test="${(1==task.state || 0==task.state) && task.owner==userInfo.id && empty editTask}">
													<tags:dataDic type="grade" name="grade" id="grade"
														value="${task.grade}"></tags:dataDic>
												</c:when>
												<c:otherwise>
													<span class="label label-default"><tags:viewDataDic
															type="grade" code="${task.grade}"></tags:viewDataDic>
													</span>
												</c:otherwise>
											</c:choose>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="widget no-header ">
                <div class="widget-body bordered-radius">
                    <div class="tickets-container tickets-bg tickets-pd">
                        <ul class="tickets-list">
                            <li class="ticket-item no-shadow clearfix ticket-normal">
                                <div class="pull-left taskDetails-left-text">
                                                                                         描述：
                                </div>
                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
                                    <div class="row" style="width:620px;">
                                        <c:choose>
											<c:when
												test="${(1==task.state || 0==task.state) && task.owner==userInfo.id && empty editTask}">
												<textarea class="form-control" id="taskRemark" name="taskRemark"
													rows="" cols="" style="width:620px;height: 110px;display:none;"
													name="taskRemark">${task.taskRemark}</textarea>
												<iframe ID="eWebEditor1"
													src="/static/plugins/ewebeditor/ewebeditor.htm?id=taskRemark&style=expand600"
													frameborder="0" scrolling="no" width="620px" height="280"></iframe>
												<button type="button" onclick="saveRemark()"
													class="btn btn-info ws-btnBlue pull-right">保存</button>
											</c:when>
											<c:otherwise>
												<div>
													<div class="comment" style="width: 100%" id="comment">
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
            
            <div class="widget no-header ">
                <div class="widget-body bordered-radius">
                    <div class="tickets-container tickets-bg tickets-pd">
                        <ul class="tickets-list">
                            <li class="ticket-item no-shadow clearfix ticket-normal">
                                <div class="pull-left taskDetails-left-text padding-top-5">
                                                                                            进度：
                                </div>
                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
                                    <div class="row clearfix">
										<!-- todo 查看进度条-->
                                      	<div class="progress progress-lg progress-bordered margin-bottom-0">
                                            <div class="progress-bar" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width:${task.taskProgress}%">
                                            	<span>${task.taskProgress}%</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            
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
												<th class="text-center">序</th>
												<th >办理人</th>
												<th >接收时间</th>
												<th >办理时限</th>
												<th style="width: 350px">进度</th>
												<th  class="text-center">状态</th>
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
																		class="user-avatar" title="${taskExecutor.executorName}"/>
																	<span class="user-name">${taskExecutor.executorName}</span>
				                                                </div>
				                                            </div>
				                                        </td>
				                                        <td>${fn:substring(taskExecutor.recordCreateTime,0,16)}</td>
				                                        <td>
				                                        	<c:choose>
				                                        		<c:when test="${not empty taskExecutor.expectTime }">
				                                        			${taskExecutor.expectTime }小时
				                                        		</c:when>
				                                        		<c:otherwise>
				                                        			--
				                                        		</c:otherwise>
				                                        	</c:choose>
				                                        </td>
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
				                                        <td class="text-center">
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
           
            <!-- 有子任务或是自己是办理人员，则可以分解任务 -->
            <div class="widget no-header " style="display:${(not empty listSonTask || task.executeState eq '1')?'block':'none'}">
                <div class="widget-body bordered-radius">
                    <div class="tickets-container tickets-bg tickets-pd">
                        <ul class="tickets-list">
                            <li class="ticket-item no-shadow clearfix ticket-normal">
                                <div class="pull-left task-left-text task-height">
                                    <i class="fa fa-list padding-right-5"></i>分解任务
                                </div>
                                <div class="pull-right ">
                                    <a href="javascript:void(0);" class="form-control margin-bottom-5 resolveTask" style="display:${task.executeState eq '1' ?'block':'none'}">+任务分解</a>
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
												<c:forEach items="${listSonTask}" var="sonTask" varStatus="status">
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
							                                                		title="${sonTaskExe.executorName}" class="user-avatar userImg">
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
            <div class="widget no-header" style="display: ${(not empty task.parentId and task.parentId >0)?'block':'none'}">
                <div class="widget-body bordered-radius">
                    <div class="tickets-container tickets-bg tickets-pd">
                        <ul class="tickets-list">
                            <li class="ticket-item no-shadow clearfix ticket-normal">
                                <div class="pull-left taskDetails-left-text padding-top-5">
                                                                                            父任务：
                                </div>
                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
                                    <c:choose>
										<c:when
											test="${(1==task.state || 0==task.state) && task.owner==userInfo.id && empty editTask}">
											<input class="colorpicker-default form-control pull-left pTask"
												style="cursor:auto;width:75%;" taskId="${task.parentId}" name="pTaskName"
												type="text" value="${task.pTaskName}" id="pTaskName" title="双击移除"
												${((1==task.state || 0==task.state) && task.owner==userInfo.id)?
												'':'disabled'} readonly="readonly">
											<a href="javascript:void(0);" class="pull-left margin-top-10 padding-left-5 pTaskRalation"
												style="font-size: 10px;" taskId="${task.id}" title="任务关联">选择</a>
											<a href="javascript:void(0);" class="pull-left margin-top-10 padding-left-5 taskView"
												style="font-size:10px;display:${(not empty task.parentId and task.parentId>0)?'block':'none'}" taskId="${task.parentId}">查看</a>
										</c:when>
										<c:otherwise>
											<a href="javascript:void(0);" class="pull-left margin-top-10 padding-left-5 taskView"
												style="font-size: 10px;" taskId="${task.parentId}">
												${task.pTaskName}
											</a>
										</c:otherwise>
									</c:choose>
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
									${task.busType=='005'?'关联项目':task.busType=='012'?'关联客户':task.busType=='022'?'关联审批':task.busType=='070'?'关联需求':'关联模块'}：
                                </div>
                                <div class="pull-left col-lg-7 col-sm-7 col-xs-7">
                                    <div class="row">
                                        <c:choose>
											<c:when
												test="${(1==task.state || 0==task.state) and task.owner==userInfo.id and empty editTask and task.parentId==-1}">
												<%--自己没有关联父任务,或是父任务没有关联项目,或是副项目关联的项目已删除,或是 可以选择项目--%>
												<div style="float:left;display:${(empty ptask.busId  
													|| ptask.busDelState==1 )?'block':'none'}"
													class="ticket-user other-user-box pull-left ps-right-box">
													<input class="colorpicker-default form-control pull-left colInput"
														type="text" value="${task.busName}" busType="${task.busType}" busId="${task.busId}"
														readonly="readonly" title="双击移除" style="cursor:auto;width:75%;">
													<a href="javascript:void(0);" class="pull-left margin-top-10 padding-left-5 colAdd"
														title="${task.busType=='012'?'客户':task.busType=='005'?'项目':task.busType=='022'?'审批':'未定义'}关联" busType="${task.busType}"
														style="font-size: 10px;display:${((task.parentId eq -1) and (task.owner eq userInfo.id))?'block':'none'}">选择</a>
													<a href="javascript:void(0);" class="pull-left margin-top-10 padding-left-5 relateClz"
														 busType="${task.busType}" busId="${task.busId}" 
														 style="font-size:10px;">查看</a>
												</div>
											</c:when>
											<c:otherwise>
												<div class="ticket-user other-user-box pull-left ps-right-box relateClz"
													readonly="readonly">
													<a href="javascript:void(0);" class="pull-left margin-top-10 padding-left-5 relateClz"
														 busType="${task.busType}" busId="${task.busId}" 
														 style="font-size:10px;">${task.busName}
													 </a>
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
            <div class="widget no-header">
                <div id="moreOpt" class="task-shrink-body">
                    <div class="clearfix">
                        <ul class="pull-left task-rel">
                        	<!--没有父级任务的前提下才能业务数据关联-->
                        	<c:if test="${(task.parentId==-1 and task.owner eq userInfo.id)}">
	                            <li busType="012"><span><i class="fa fa-thumb-tack padding-right-10"></i>关联客户</span></li>
	                            <li busType="005"><span><i class="fa fa-thumb-tack padding-right-10"></i>关联项目</span></li>
	                            <li busType="022"><span><i class="fa fa-thumb-tack padding-right-10"></i>关联审批</span></li>
	                            <li busType="070"><span><i class="fa fa-thumb-tack padding-right-10"></i>关联需求</span></li>
                            </c:if>
                            <li busType="003"><span><i class="fa fa-thumb-tack padding-right-10"></i>关联任务</span></li>
                            <!-- <li busType="017"><span><i class="fa fa-thumb-tack padding-right-10"></i>关联会议</span></li>
                            <li busType="016"><span><i class="fa fa-thumb-tack padding-right-10"></i>关联日程</span></li>
                            <li><span><i class="fa fa-thumb-tack padding-right-10"></i>关联审批</span></li> -->
                        </ul>
                    </div>
                </div>
                <div class="task-shrink-head">
                    <div class="inner text-center">
                        <span id="task-shrink-name">更多及事件关联</span><i class="fa padding-left-10 fa-angle-down task-shrink-icon" id="moreOptImg"></i>
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
	        var taskProgress = $('.progress-drag-text').html();
	        //任务进度描述
            $('.progress-drag-btn').css('left',taskProgress);
            $('.progress-drag-bar').width(taskProgress);
            $('.progress-drag-text').html(taskProgress);
            
            taskOptForm.initEvent();
	    });
	    
	    var taskObj = {
	    		"taskId":"${task.id}",//任务主键
	    		"state":"${task.state}",//任务状态
	    		"executState":"${task.executeState}",//任务执行状态
	    		"ownState":"${userInfo.id==task.owner?'1':'0'}"//任务的负责人，编辑界面一定是负责人
	    		,"version":"${task.version}"//任务的版本
	    }
	    var taskOptForm = {
	    		taskOptMenu:function(){
	    			//添加留言按钮
	    			taskOptForm.addTaskTalkMenu(taskObj.state);
	    			//添加认领按钮
	    			taskOptForm.addTaskConfirmMenu(taskObj.state,taskObj.executState);
	    			//添加转办按钮
	    			taskOptForm.addTaskNextMenu(taskObj.state,taskObj.executState);
	    			//添加办结按钮
	    			taskOptForm.addTaskPauseOrStartMenu(taskObj.state,taskObj.executState);
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
	    			if(state == '0' && executState=='0'){//任务未认领，且当前人员是办理人员，则添加认领
	    				var _acceptA = $('<a href="javascript:void(0)" class="purple" id="acceptTask" title="任务认领"></a>');
	    				$(_acceptA).html('<i class="fa fa-comments"></i>认领');
	    				$(".taskOptMenu").append($(_acceptA))
	    			}
	    		},addTaskNextMenu:function(state,executState){
	    			if(state == '1' && (executState=='1')){//任务办理中，且当前人员是办理人员，则添加转办
	    				/* var _nextA = $('<a href="javascript:void(0)" class="blue" id="nextExecutor" title="任务转办"></a>');
	    				$(_nextA).html('<i class="fa fa-h-square"></i>转办');
	    				$(".taskOptMenu").append($(_nextA)); */
	    				
	    				var _nextA = $('<a href="javascript:void(0)" class="blue" id="handOverTask" title="任务委托"></a>');
	    				$(_nextA).html('<i class="fa fa-h-square"></i>委托');
	    				$(".taskOptMenu").append($(_nextA));
	    				
	    			}
	    		},addTaskPauseOrStartMenu:function(state,executState){
	    			if(state =='1'){//任务办理中，且当前人员是发起人员，则添加办结
	    				if(executState=='1'){
		    				var _executePauseA = $('<a href="javascript:void(0)" class="red pauseExexuteTaskBtn" title="暂停执行"></a>');
		    				$(_executePauseA).html('<i class="fa fa-power-off"></i>暂停');
		    				$(".taskOptMenu").append($(_executePauseA));
	    				}else if(executState == '3'){
		    				var _executeStartA = $('<a href="javascript:void(0)" class="green startExexuteTaskBtn" title="开始办理"></a>');
		    				$(_executeStartA).html('<i class="fa fa-power-off"></i>办理');
		    				$(".taskOptMenu").append($(_executeStartA));
	    				}
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
	    				
	    				$(_resolveLi).append($(_resolveA));
	    				$(_moreUl).append($(_resolveLi));
	    				
    					/* var _delayLi = $('<li></li>');
    					var _delayA =$('<a href="javascript:void(0)" class="delayApplyBtn"></a>');
	    				$(_delayA).html('<div class="clearfix"><i class="fa fa-calendar-check-o"></i><span class="title ps-topmargin">任务报延</span></div>');
	    				
	    				$(_delayLi).append($(_delayA))
	    				$(_moreUl).append($(_delayLi)); */
    				}
    				if(ownState=='1'){//任务办理中，且当前人员是发起人员，则添加办结
    					var _finishLi = $('<li></li>');
	    				var _finishA = $('<a href="javascript:void(0)" class="finishTaskBtn"></a>');
	    				$(_finishA).html('<div class="clearfix"><i class="fa fa-power-off"></i><span class="title ps-topmargin">任务办结</span></div>');
	    				
	    				$(_finishLi).append($(_finishA))
	    				$(_moreUl).append($(_finishLi));
	    			}
    				if((state == '1' || state == '0') && ownState == '1'){//任务暂停
    					var _pauseLi = $('<li></li>');
    					var _pauseA = $('<a href="javascript:void(0)" class="pauseTaskBtn"></a>');
	    				$(_pauseA).html('<div class="clearfix"><i class="fa fa-pause-circle-o"></i><span class="title ps-topmargin">任务暂停</span></div>');
	    				
	    				$(_pauseLi).append($(_pauseA))
	    				$(_moreUl).append($(_pauseLi));
    				}
    				if(state != '4'){//除开已完成的任务都可以转为日程
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
	    				var param = {"sid":sid,"taskId":taskObj.taskId,"version":taskObj.version}
	    				postUrl(acceptUrl,param,function(data){
	    					if(data.status=='y'){
	    						window.self.location.reload();
	    					}else{
	    						showNotification(2,data.info);
	    						window.self.location.reload();
	    					}
	    				});
	    			})
	    		},updateTaskProgressEvent:function(progressVal,callback){
	    			var param = {
    					"sid":sid,
    					"id":taskObj.taskId,
    					"taskProgress":progressVal
    					,"version":taskObj.version
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
		    			taskOptForm.updateTaskProgressEvent(progressVal,function(data){
		    				if(data.confirm=='y'){//任务已办结
		    					finishTask(taskObj.taskId,1,openTabIndex,openPageTag);//办结任务
		    				}else{
		    					showNotification(1,data.info);
		    					if(progressVal==100){
		    						window.self.location.reload();	
		    					}
		    				}
		    			})
	    				
	    			}
	    		}
	    		
	    }
    </script>
</body>
</html>
