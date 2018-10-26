<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
var sid="${param.sid}"
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
		
		$("#updateRsdaLeave").on("click",function(){
			$(".subform").submit();
		})
	})
</script>
</head>
<body>
<form class="subform" method="post" action="/rsdaLeave/updateRsdaLeave">
<input type="hidden" name="id" value="${rsdaLeave.id}">
<input type="hidden" name="iframeTag" value="${param.iframeTag}">
<tags:token></tags:token>
	<div class="container" style="padding:0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">编辑离职信息</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="updateRsdaLeave">
								<i class="fa fa-save"></i>保存
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 --> 
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">离职信息</span>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
										<li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	<i class="fa fa-user blue"></i>&nbsp;离职人员
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="min-width: 135px">
												<tags:userOne datatype="s" name="userId" defaultInit="false"
													showValue="${rsdaLeave.userName}" value="${rsdaLeave.userId}" comId="${rsdaLeave.comId }"
													onclick="true" showName="userName"></tags:userOne>
											</div>  
	                                      </li>
                                         <li class="ticket-item no-shadow ps-listline">
	                                         <div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-star blue"></i>&nbsp;离职类型
											    	<span style="color: red">*</span>
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												   <tags:dataDic type="leaveType" name="leaveType" datatype="*"  
												   please="t" clz="pull-left" value="${rsdaLeave.leaveType}" ></tags:dataDic>
												</div> 
											</div>
											<div class="pull-left" style="width: 50%">
												<div class="pull-left gray ps-left-text">
											    	<i class="fa fa-bookmark blue"></i>&nbsp;担任职务
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												  	<div class="pull-left">
														<input name="userDuty" class="colorpicker-default form-control pull-left" type="text"
														style="width: 150px;" value="${rsdaLeave.userDuty}">
													</div>
												</div> 
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
                                         	<div class="pull-left" style="width: 50%">
											    <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-clock-o blue"></i>&nbsp;合同到期日
											    	<span style="color: red">*</span>
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left" style="line-height: 40px;vertical-align: middle;">
													   <input class="colorpicker-default form-control pull-left" datatype="*" readonly="readonly"
														 name="pactEndDate" onClick="WdatePicker()" type="text" 
														value="${rsdaLeave.pactEndDate}" style="width: 150px" nullmsg="&nbsp;">
													</div>
												</div>             
                                         	</div>
                                         	<div class="pull-left" style="width: 50%">
											     <div class="pull-left gray ps-left-text">
											    	<i class="fa fa-clock-o blue"></i>&nbsp;拟离职日期
											    	<span style="color: red">*</span>
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													<div class="pull-left" style="line-height: 40px;vertical-align: middle;">
													   <input class="colorpicker-default form-control pull-left" datatype="*" readonly="readonly"
														name="leaveDate" onClick="WdatePicker()" type="text" 
														value="${rsdaLeave.leaveDate}" style="width: 150px" nullmsg="&nbsp;">
													</div>
												</div>                
                                         	</div>
                                          </li>
										<li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		<i class="fa fa-file blue"></i>&nbsp;离职原因
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;"
										  		 name="leaveReason" rows="" cols="">${rsdaLeave.leaveReason}</textarea>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
										<li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		<i class="fa fa-file blue"></i>&nbsp;备注
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;"
										  		 id="remark" name="remark" rows="" cols="">${rsdaLeave.remark}</textarea>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
											<div class="pull-left gray ps-left-text">
												<i class="fa fa-file blue"></i>&nbsp;相关附件</div>
												<div class="ticket-user pull-left ps-right-box"
													style="width: 400px;height: auto;">
													<div class="margin-top-10">
														<div style="clear:both;width: 300px" class="wu-example">
														    <!--用来存放文件信息-->
														    <div id="thelistlistRsdaLeaveFiles_upfileId" class="uploader-list">
														    	<c:choose>
																	<c:when test="${not empty rsdaLeave.listRsdaLeaveFiles}">
																		<c:forEach items="${rsdaLeave.listRsdaLeaveFiles}" var="upfile"
																			varStatus="vs">
																			<div id="wu_file_0_-${upfile.upfileId}"
																				class="uploadify-queue-item">
																				<div class="cancel">
																					<a href="javascript:void(0)" fileDone="1"
																						fileId="${upfile.upfileId}">X</a>
																				</div>
																				<span class="fileName" title="${upfile.fileName}"> <tags:cutString
																						num="25">${upfile.fileName}</tags:cutString>(已有文件) </span> <span
																					class="data"> - 完成</span>
																				<div class="uploadify-progress">
																					<div class="uploadify-progress-bar" style="width: 100%;"></div>
																				</div>
																			</div>
																		</c:forEach>
																	</c:when>
																</c:choose>
														    </div>
														    <div class="btns btn-sm">
														        <div id="pickerlistRsdaLeaveFiles_upfileId" class="webuploader-container">
														        	选择文件
														        </div>
														    </div>
															<script type="text/javascript">
																loadWebUpfiles('listRsdaLeaveFiles_upfileId','${param.sid}','',"pickerlistRsdaLeaveFiles_upfileId","thelistlistRsdaLeaveFiles_upfileId",'filelistRsdaLeaveFiles_upfileId')
															</script>
															<div style="position: relative; width: 350px; height: 90px;display: none">
																<div style="float: left; width: 250px">
																	<select list="listRsdaLeaveFiles" listkey="upfileId" listvalue="fileName" id="listRsdaLeaveFiles_upfileId" name="listRsdaLeaveFiles.upfileId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">
																		<c:choose>
																			<c:when test="${not empty rsdaLeave.listRsdaLeaveFiles}">
																				<c:forEach items="${rsdaLeave.listRsdaLeaveFiles}"
																					var="upfile" varStatus="vs">
																					<option selected="selected" value="${upfile.upfileId}">${upfile.fileName}</option>
																				</c:forEach>
																			</c:when>
																		</c:choose>
																	 </select>
																</div>
															</div>
														</div>
													</div>
												</div>
												<div class="ps-clear"></div>
											</li>
                                   	</ul>
                                </div>
                            </div>
                          </div>
                           <div class="widget-body no-shadow">
                            </div> 
                        </div>
					
					</div>
				</div>
			</div>
		</div>
	</form>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
