<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<%@taglib prefix="t" uri="/WEB-INF/tld/t.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script>
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
var sid = '${param.sid}';
$(function() {
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
});

function downTemp(filename) {
	window.location.href="/downLoad/downTemp?filename="+ filename+"&sid="+sid;
}


</script>
</head>
<body>
<form class="subform" method="post" action="/moduleChangeExamine/updateApply">
<input type="hidden" name="sid" value="${param.sid}"/>
<input type="hidden" name="id" value="${mail.id}"/>
<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget no-margin-bottom" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">邮件详情</span>
                        <div class="widget-buttons ps-toolsBtn">
							<!-- <a href="javascript:void(0)" class="purple" id="reply">
								<i class="fa fa-comments"></i>回复
							</a>
							<a href="javascript:void(0)" class="blue" id="relay">
								<i class="fa fa-h-square"></i>转发
							</a> -->
							&nbsp;
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div>
                     <!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->       
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                        	
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	主题
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<span style="font-weight: 900">${mail.subject}</span>
											</div>               
                                        </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	${type == "sendMail"?"发送账号":"发送人"}
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												${mail.account}
											</div>               
                                        </li>
                                        <li class="ticket-item no-shadow ps-listline" style="clear:both;">
										    <div class="pull-left gray ps-left-text">
										    	接收账号
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<c:forEach items="${mail.listAddresseeTo}" var="addr" varStatus="vsa">
													${vsa.count>1?';':''}${addr.personal}${not empty addr.personal?'（':''}${addr.address}${not empty addr.personal?'）':''}
												</c:forEach>
											</div>               
                                        </li>
                                        <li class="ticket-item no-shadow ps-listline" style="clear:both;display: ${not empty mail.listAddresseeCc[0]?'block':'none'}">
										    <div class="pull-left gray ps-left-text">
										    	抄送账号
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<c:forEach items="${mail.listAddresseeCc}" var="addr" varStatus="vsa">
													${vsa.count>1?';':''}${addr.personal}${not empty addr.personal?'（':''}${addr.address}${not empty addr.personal?'）':''}
												</c:forEach>
											</div>               
                                        </li>
                                        <li class="ticket-item no-shadow ps-listline" style="clear:both;;display: ${not empty mail.listAddresseeBcc[0]?'block':'none'}">
										    <div class="pull-left gray ps-left-text">
										    	密送账号
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<c:forEach items="${mail.listAddresseeBcc}" var="addr" varStatus="vsa">
													${vsa.count>1?';':''}${addr.personal}${not empty addr.personal?'（':''}${addr.address}${not empty addr.personal?'）':''}
												</c:forEach>
											</div>               
                                        </li>
                                        <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	发送时间  
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												${mail.sendTime}
											</div>               
                                        </li>
                                        <li class="ticket-item no-shadow ps-listline" style="clear:both;overflow: auto;">
										    <div class="pull-left gray ps-left-text" >
										    	邮件内容
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="word-break: break-all;white-space: normal;width: 600px;height: auto;">
											    ${mail.body} 
											</div>
                                          </li>
                                          <c:if test="${not empty mail.files[0]}">
											   	<li class="ticket-item no-shadow ps-listline" style="clear:both;overflow: auto;">
												   	<c:forEach items="${mail.files}" var="obj" varStatus="vs">
												   		 <div class="pull-left gray ps-left-text" >
														    	${vs.count > 1?"&nbsp;":"附件信息"}
														   </div>
														   <div class="ticket-user pull-left ps-right-box" style="word-break: break-all;white-space: normal;width: 600px;height: auto;">
															    <a href="javascript:void(0);" onclick="downTemp('${obj}');" title="下载附件">${obj}</a>
															</div>
												   	</c:forEach>
	                                          </li>
										</c:if>
										<c:if test="${not empty mail.upfiles[0]}">
											   	<li class="ticket-item no-shadow ps-listline" style="clear:both;overflow: auto;">
												   	<c:forEach items="${mail.upfiles}" var="obj" varStatus="vs">
												   		 <div class="pull-left gray ps-left-text" >
														    	${vs.count > 1?"&nbsp;":"附件信息"}
														   </div>
														   <div class="ticket-user pull-left ps-right-box" style="word-break: break-all;white-space: normal;width: 600px;height: auto;">
															    <a href="javascript:void(0);" onclick="downLoad('${obj.uuid}','${obj.filename}','${param.sid }')" title="下载附件">${obj.filename}</a>
															</div>
												   	</c:forEach>
	                                          </li>
										</c:if>
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
		
		<script src="/static/assets/js/bootstrap.min.js"></script>
    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
