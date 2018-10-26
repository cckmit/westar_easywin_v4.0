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
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/schedule/shedule.js"></script>
<script type="text/javascript">
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
var id='${schedule.id}';
var scheStartDatePage = '${schedule.scheStartDate}';
var scheEndDatePage = '${schedule.scheEndDate}';
	$(function() {
		
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
				//设置日期格式
		var scheStartDate = '${schedule.scheStartDate}';
		if(scheStartDate.indexOf(":")<0){
			$("#scheStartDate").bind('focus',function(){
				WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'scheEndDate\',{d:-0});}'});
			})
			$("#scheEndDate").bind('focus',function(){
				WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'scheStartDate\',{d:-0});}'});
			})
		}else{
			$("#scheStartDate").bind('focus',function(){
				WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate: '#F{$dp.$D(\'scheEndDate\',{d:-0});}'});
			})
			$("#scheEndDate").bind('focus',function(){
				WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate: '#F{$dp.$D(\'scheStartDate\',{d:-0});}'});
			})
		}
		
		//日程讨论
		$("#scheTalkLi").click(function(){
			$(this).parent().find("li").removeAttr("class");
			$("#scheTalkLi").attr("class","active");
			$("#otherScheIframe").attr("src","/schedule/listPagedScheTalks?sid=${param.sid}&pager.pageSize=15&scheduleId=${schedule.id}");
		});
		//日程日志记录
		$("#scheLogLi").click(function(){
			$(this).parent().find("li").removeAttr("class");
			$("#scheLogLi").attr("class","active");
			$("#otherScheIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=9&busId=${schedule.id}&busType=016&ifreamName=otherScheIframe");
		});
		//日程查看记录
		$("#scheViewLi").click(function(){
			$(this).parent().find("li").removeAttr("class");
			$("#scheViewLi").attr("class","active");
			$("#otherScheIframe").attr("src","/common/listViewRecord?sid=${param.sid}&busId=${schedule.id}&busType=016&ifreamName=otherScheIframe");
		});
	})
	//初始化重复区域数据
	function initVal(){
		$("#week").find(":checkbox[value='${week}']").attr("checked", true);
		if($("#isRepeat").attr("checked")){//有重复执行的参数
			//显示重复区域
			$("#repTypeDiv").css("display","block");
			//隐藏参数设置，适时展示
			$(".repType").css("display","none");
			//重复类型
			var repType = $("#repType").val();
			//重复参数
			var repDate = '${schedule.repDate}';
			$("#repType").find("option[value="+repType+"]").attr("selected","selected");
			if(repType==1){//每天
				$("#day").find("input").val(repDate);
				$("#day").css("display","block");
				$("#day").find("input").attr("datatype","n");
			}else if(repType==2){//每周
				 $("#week").find(":checkbox").attr("checked", false);
				 var str= new Array();   
				 str=repDate.split(",");
				 for (var i=0;i<str.length ;i++ ){ 
					 $("#week").find(":checkbox[value='"+str[i]+"']").attr("checked", true);
				 }
				 $("#week").css("display","block");
			}else if(repType==3){//每月
				$("#month").find("input").val(repDate);
				$("#month").css("display","block");
				$("#month").find("input").attr("datatype","n");
			}else if(repType==4){//每年
				$("#year").find("input").val(repDate);
				$("#year").css("display","block");
				$("#year").find("input").attr("datatype","n");
			}
			$("#dates").find("input").val('${repEndDate}')
			//结束方式
			var repEndType = '${schedule.repEndType}';
			var repEndDate = '${schedule.repEndDate}';
			$("#repEndType").find("option[value="+repEndType+"]").attr("selected","selected");
			if(repEndType==1){//执行次数的方式结束
				$("#times").find("input").val(repEndDate);
				$("#times").css("display","block")
			}else if(repEndType==2){//执行时间的方式结束
				$("#dates").find("input").val(repEndDate)
				$("#dates").css("display","block")
			}
		}
	}
	
	//日程转任务
	function toTask() {
		if(id){
			window.top.layer.confirm('确定要转任务吗？', {
				icon: 3,
				title: '提示'
			},
			function(index) {
				window.top.layer.close(index);
				
				var taskName = $("#title").val();
				
				var endDate = $("#scheEndDate").val();
				var paramObj = {};
				if(endDate){
					paramObj.dealTimeLimit = endDate.substring(0,10);
				}
				var content = $("#content").val();
				if(content){
					paramObj.taskRemark = content;
				}
				
				if(taskName){
					paramObj.taskName = taskName;
				}
				addBusTaskForMod('${param.sid}',paramObj);
				closeWin();
				/* $.post("/schedule/updateToTask?sid=${param.sid}",{id:id},     
					function (msgObjs){
					if(msgObjs.status == 'y'){
						showNotification(1, "转任务成功！");
						window.location.reload()
					}else{
						showNotification(2,msgObjs.info);
					}
				},"json"); */
				
			});
		}
	}
	
	function toMeet(){
		if(id){
			window.top.layer.confirm('确定要转会议吗？', {
				icon: 3,
				title: '提示'
			},
			function(index) {
				window.top.layer.close(index);
				var startDate = $("#scheStartDate").val();
				var endDate = $("#scheEndDate").val();
				
				var url = "/meeting/addMeetingPage?sid=${param.sid}";
				if(startDate){
					if(startDate.length==10){
						startDate = startDate+" 09:00"
					}
					url = url+"&startDate="+startDate;
				}
				if(endDate){
					if(endDate.length==10){
						endDate = endDate+" 18:00"
					}
					url = url+"&endDate="+endDate;
				}
				
				var content = $("#content").val();
				if(content){
					url = url+"&content="+encodeURIComponent(content);
				}
				var title = $("#title").val();
				if(title){
					url = url+"&title="+encodeURIComponent(title);
				}
				window.top.openWinByRight(url);
				closeWin();
			});
		}
	}
	
</script>
<style type="text/css">
ul>li{
border: 0 !important;
}
#repTypeDiv>li{
border: 0 !important;
}
</style>
</head>
<body style="background-color: #fff" onload="initVal()">
<input type="hidden" id="subState" value="0">
<form class="subform" method="post">
	<input type="hidden" id="formatEl" value="${param.formatEl}"/>
	<div class="container no-padding" style="width: 100%">	
		<div class="row" >
			<div class="col-lg-12 col-sm-12 col-xs-12" >
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead ">
                        <span class="widget-caption themeprimary margin-left-10 margin-top-5" style="font-size: 20px">编辑日程</span>
                        <div class="widget-buttons ps-toolsBtn">
	                        <a href="javascript:void(0)" class="blue" id="toTask" onclick="toTask()">
								<i class="fa fa-h-square"></i>转任务
							</a>
	                        <a href="javascript:void(0)" class="blue" id="toMeet" onclick="toMeet()">
								<i class="fa fa-h-square"></i>转会议
							</a>
                       </div> 
                        <div class="widget-buttons">
                        	
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary" style="font-size: 18px"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->       
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered no-padding-bottom no-margin-bottom">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">日程信息</span>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white" >
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												日程名称：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											<input type="hidden" id="schId" value="${schedule.id }">
											 <input class="colorpicker-default form-control" type="text" id="title" 
											 style="width: 300px;" name="title"  value="${schedule.title }"/>
											</div>
										</li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												开始日期：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											 <input class="colorpicker-default form-control" type="text" id="scheStartDate" readonly="readonly" 
												style="width: 300px;"  name="scheStartDate"  value="${schedule.scheStartDate }"/>
											</div>
										</li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												结束日期：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											 <input class="colorpicker-default form-control" type="text" id="scheEndDate" readonly="readonly" 
											 	style="width: 300px;" name="scheEndDate" 	value="${schedule.scheEndDate}"/>
											</div>
										</li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    &nbsp;
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<label class="no-padding">
													<input type="checkbox" id="isAllDay" readonly="readonly" name="isAllDay" ${schedule.isAllDay==1?'checked':''}  onclick="setAllday(this)"/>
													<span class="text">全天</span>
												</label>
												<label class="padding-left-20">
													<input type="checkbox" onclick="setIsRep(this)" id="isRepeat" name="isRepeat" ${schedule.isRepeat==1?'checked':''}>
													<span class="text">重复</span>
												</label>
											</div>
										</li>
										<div style="display: none" id="repTypeDiv">
											<li class="ticket-item no-shadow autoHeight no-padding">
											    <div class="pull-left gray ps-left-text">
											    &nbsp;
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="width: 410px;height: auto;">
													<label style="vertical-align:middle;">执行类型：</label>
													<label style="vertical-align:middle;">
														<tags:dataDic type="repType" clz="form-control" id="repType" name="repType" 
														onchange="setRepType(this)" style="width: 55px;height:25px;padding:0 0;" value="${schedule.repType}"></tags:dataDic>
													</label> 
													&nbsp;&nbsp;&nbsp;
													<br>
													<label style="vertical-align:middle;">执行频率：</label>
													<label style="vertical-align:middle;" id="repDate">
														<div id="day" class="repType" style="display: block;margin-top: 5px">
															频率<input type="text" class="colorpicker-default form-control" style="width: 35px;padding:0 0;display: inline;height:25px;" 
															maxlength="3" value="1" onpaste="return false;"  onkeypress="keyPress()">
															天
														</div>
														<div id="week" class="repType" style="display: none;margin-top: 5px">
																<label class="no-margin-bottom padding-right-10">
																	<input type="checkbox" value="1" style="width: 20px;" />
																	<span class="text">日</span>
																</label>
																<label class="no-padding-bottom padding-right-10">
																	<input type="checkbox" value="2" style="width: 20px;" />
																	<span class="text">一</span>
																</label>
																<label class="no-margin-bottom padding-right-10">
																	<input type="checkbox" value="3" style="width: 20px;" />
																	<span class="text">二</span>
																</label>
																<label class="no-margin-bottom padding-right-10">
																	<input type="checkbox" value="4" style="width: 20px;" />
																	<span class="text">三</span>
																</label>
																<label class="no-margin-bottom padding-right-10">
																	<input type="checkbox" value="5" style="width: 20px;" />
																	<span class="text">四</span>
																</label>
																<label class="no-margin-bottom padding-right-10">
																	<input type="checkbox" value="6" style="width: 20px;" />
																	<span class="text">五</span>
																</label>
																<label class="no-margin-bottom padding-right-10">
																	<input type="checkbox" value="7" style="width: 20px;" />
																	<span class="text">六</span>
																</label>
														</div>
														<div id="month" class="repType" style="display: none;margin-top: 5px">
															频率<input type="text" class="colorpicker-default form-control" style="width: 35px;padding:0 0;display: inline;height:25px;" 
															maxlength="3" value="1" onpaste="return false;"  onkeypress="keyPress()">
																月
														</div>
														<div id="year" class="repType" style="display: none;margin-top: 5px">
															频率<input type="text" class="colorpicker-default form-control" style="width: 35px;padding:0 0;display: inline;height:25px;" 
															maxlength="3" value="1" onpaste="return false;"  onkeypress="keyPress()">
																年
														</div>
													</label>
													<br>
													<label style="vertical-align:middle;">结束方式：</label> 
													<label  style="vertical-align:middle;">
														<tags:dataDic type="repEndType" clz="form-control" id="repEndType" name="repEndType" onchange="setRepEndType(this)" style="width: 55px;height:25px;padding:0 0;"></tags:dataDic>
													</label> 
													<label style="vertical-align:middle;">
														<div id="times" class="repEndType" style="display: none">
															执行<input type="text" class="colorpicker-default form-control" name="repEndDate" style="width: 35px;padding:0 0;display: inline;height:25px;" 
															maxlength="3" value="1" onpaste="return false;"  onkeypress="keyPress()">
															次后
														</div>
														<div id="dates" class="repEndType" style="display:none">
															<input type="text" class="colorpicker-default form-control" readonly="readonly" name="repEndDate"  style="width:125px;padding:0 0;display: inline;height:25px;" 
															onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${repEndDate}">
														</div>
													</label>
												</div>
												<div class="ps-clear"></div> 
											</li>
										</div>
                                         <li class="ticket-item no-shadow ps-listline autoHeight no-padding">
										    <div class="pull-left gray ps-left-text">
												分享人员：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="width: 360px;height: auto;">
												<div style="width: 250px; float: left; display: none;">
													<select style="width: 100%; height: 100px;" id="listMembers_memberId" ondblclick="removeClick(this.id)" multiple="multiple" name="listMembers.memberId" moreselect="true" listvalue="memberName" listkey="memberId" list="listMembers">
														<c:choose>
															<c:when test="${not empty listUser }">
																<c:forEach items="${listUser}" var="user">
																	<option value="${user.userId}">${user.userName }</option>
																</c:forEach>
															</c:when>
														</c:choose>
													</select>
												</div>
												<div id="listMembers_memberIdDiv">
													<c:choose>
															<c:when test="${not empty listUser }">
																<c:forEach items="${listUser}" var="user">
																	<div class="online-list margin-top-5 margin-left-5" style="float:left" id="user_img_listMembers_memberId_${user.userId}" ondblclick="removeClickUser('listMembers_memberId',${user.userId})" title="双击移除">
																		<img src="/downLoad/userImg/${user.comId}/${user.userId}?sid=${param.sid}"  class="user-avatar">
																		<span class="user-name">${user.userName }</span>
																	</div>
																</c:forEach>
															</c:when>
														</c:choose>
												</div>
												<a class="btn btn-blue btn-xs" title="人员多选" onclick="userMore('listMembers_memberId','','${param.sid}','','listMembers_memberIdDiv');" href="javascript:void(0);"><i class="fa fa-plus"></i>选择</a>
											</div>
											<div class="ps-clear"></div>   
										</li>
										<li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												事件位置：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											 <input class="colorpicker-default form-control" type="text" id="address" 
											 style="width: 300px;" name="address"  value="${schedule.address}"/>
											</div>
										</li>
                                         <li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		&nbsp;日程说明
									    	</div>
											<div class="ticket-user pull-left ps-right-box"  style="width: 300px;height: auto;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" 
										  		style="height:100px;width: 300px" id="content" name="content">${schedule.content}</textarea>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												公开程度：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<tags:dataDic type="publicType" clz="form-control" id="publicType" name="publicType" 
													value="${schedule.publicType}" style="width: 115px;height:25px;padding:0 0;"></tags:dataDic>
											</div>
										</li>
                                   	</ul>
                                </div>
                            </div>
                          </div>
                          <hr class="bordered-dashed">
                          <div class="widget-body no-shadow">
                           		<div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat">
                                            <li class="active" id="scheTalkLi">
												<a href="javascript:void(0)" data-toggle="tab" style="padding-top: 5px;padding-bottom: 5px">留言</a>
											</li>
											<li id="scheLogLi">
												<a href="javascript:void(0)" data-toggle="tab" style="padding-top: 5px;padding-bottom: 5px">操作日志</a>
											</li>
											<li id="scheViewLi">
												<a href="javascript:void(0)" data-toggle="tab" style="padding-top: 5px;padding-bottom: 5px">阅读情况</a>
											</li>
											<%--<li id="scheViewLi">--%>
												<%--<a href="javascript:void(0)" data-toggle="tab" style="padding-top: 5px;padding-bottom: 5px">最近查看</a>--%>
											<%--</li>--%>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 	<iframe id="otherScheIframe"
												src="/schedule/listPagedScheTalks?sid=${param.sid}&pager.pageSize=15&scheduleId=${schedule.id}"
												border="0" frameborder="0" allowTransparency="true"
												noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
                                    	 </div>
                                	</div>
                            	</div>
                            </div> 
                        </div>
					</div>
				</div>
			</div>
		</div>
</form>



</body>
</html>
