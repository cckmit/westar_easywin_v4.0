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
	var scheStartDatePage = '${schedule.scheStartDate}';
	var scheEndDatePage = '${schedule.scheEndDate}';
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		if($("#formatEl").val()=='0'){
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
		$("#week").find(":checkbox[value='${week}']").attr("checked", true);
	})
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
<body style="background-color: #fff">
<input type="hidden" id="subState" value="0">
<form class="subform" method="post">
	<input type="hidden" id="formatEl" value="${formatEl}"/>
	<div id="fileDivAjax">
	</div>
	<div class="container no-padding" style="width: 100%">	
		<div class="row" >
			<div class="col-lg-12 col-sm-12 col-xs-12" >
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead ">
                        <span class="widget-caption themeprimary margin-left-5" style="font-size: 20px">添加日程</span>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary" style="font-size: 18px"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                                
                     <div class="widget-body margin-top-40 no-padding-bottom no-margin-bottom" style="height: 360px;overflow-y:auto;">
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
											 <input class="colorpicker-default form-control" type="text" id="title" 
											 style="width: 300px;" name="title"  value="${schedule.title}"/>
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
											 	style="width: 300px;" name="scheEndDate" 	value="${schedule.scheEndDate }"/>
											</div>
										</li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    &nbsp;
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<label class="no-padding">
													<input type="checkbox" id="isAllDay" readonly="readonly" name="isAllDay" ${formatEl==0?'checked':''} onclick="setAllday(this)"/>
													<span class="text">全天</span>
												</label>
												<label class="padding-left-20">
													<input type="checkbox" onclick="setIsRep(this)" id="isRepeat" name="isRepeat" >
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
														<tags:dataDic type="repType" clz="form-control" id="repType" name="repType" onchange="setRepType(this)" style="width: 55px;height:25px;padding:0 0;"></tags:dataDic>
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
												<tags:userMore name="listMembers.memberId" showName="memberName" disDivKey="listMembers_memberIdDiv"></tags:userMore>
											</div>
											<div class="ps-clear"></div>   
										</li>
										<li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												事件位置：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											 <input class="colorpicker-default form-control" type="text" id="address" 
											 style="width: 300px;" name="address"  value=""/>
											</div>
										</li>
                                         <li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		&nbsp;日程说明
									    	</div>
											<div class="ticket-user pull-left ps-right-box"  style="width: 300px;height: auto;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" 
										  		style="height:100px;width: 300px" id="content" name="content" rows="" cols="">${schedule.content}</textarea>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												公开程度：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<tags:dataDic type="publicType" clz="form-control" id="publicType" name="publicType" 
													style="width: 115px;height:25px;padding:0 0;"></tags:dataDic>
											</div>
										</li>
                                   	</ul>
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
