<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
	<div class="widget radius-bordered" style="clear:both">
		<div class="widget-body no-shadow" style="padding:0 12px;">
			<div class="tickets-container bg-white">
				<div style="font-size:22px;text-align:center; ">
					<strong>${institution.title}
					</strong>
				</div>
				<div style="text-align:center; " class="margin-10">
					<span class="padding-left-20"> 发布人：${institution.creatorName } </span>
					<span class="padding-left-20"> 时间：${fn:substring(institution.recordCreateTime,0,10) } </span>
					<span class="padding-left-20">
						阅读量：
						<a href="javascript:void(0);" class="blue" onclick="viewRecord()" >(${readNum}/${totalNum})</a>
					</span>
				</div>
				<div id="comment">${institution.instituRemark}</div>
			</div>
		</div>
	</div>
	<form id="instituForm" class="subform">
		<div class="widget radius-bordered" style="clear:both">
			<div class="widget-body no-shadow" style="padding:0 12px;">
				<div class="tickets-container bg-white">
					<div class="padding-bottom-20" style="font-size:14px;color: #333 !important;">
						
					</div>
					<ul class="nav nav-tabs tabs-flat" id="myTab11" style="padding-top:0" >
						<li class="active">
							<a href="javaScript:void(0)"  class="ws-blue">留言</a>
						</li>
						<li>
							<a href="javaScript:void(0)" onclick="viewLogs()"  class="ws-blue">操作日志</a>
						</li>
					</ul>
									
					<div id="alltalks" style="clear:both">
						<c:if test="${not empty talks}">
							<c:forEach items="${talks}" var="talk" varStatus="vs">
								<div id="talk${talk.id}" class="ws-shareBox ${talk.parentId==-1?'':'ws-shareBox2'}">
									<div class="shareHead" data-container="body" data-toggle="popover" data-user='${talk.talker}' data-busId='${busId}' data-busType='039'>
										<%--头像信息 --%>
										<img src="/downLoad/userImg/${talk.comId}/${talk.talker}?sid=${param.sid}" title="${talk.talkerName}"></img>
									</div>
									<div class="shareText">
										<span class="ws-blue">${talk.talkerName}</span>
										<c:if test="${talk.parentId>-1}">
											<r>回复</r>
											<span class="ws-blue">${talk.ptalkerName}</span>
										</c:if>
										<p class="ws-texts">${talk.content}</p>
										<%--附件 --%>
										<c:choose>
											<c:when test="${not empty talk.listTalkUpfile}">
												<div class="file_div">
													<c:forEach items="${talk.listTalkUpfile}" var="upfiles" varStatus="vs">
														<c:choose>
															<c:when test="${upfiles.isPic==1}">
																<p class="p_text">
																	附件(${vs.count})：${upfiles.filename} &nbsp;&nbsp;
																	<a class="fa fa-download" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
																	&nbsp;&nbsp;
																	<a class="fa fa-eye" href="javascript:void(0);" title="预览"
																		onclick="showPic('/downLoad/down/${upfiles.uuid}/${upfiles.filename}','${param.sid}','${upfiles.upfileId}','039','${busId}')"></a>
																</p>
															</c:when>
															<c:otherwise>
																<p class="p_text">
																	附件(${vs.count})：${upfiles.filename}
																	<c:choose>
																		<c:when test="${upfiles.fileExt=='doc' || upfiles.fileExt=='docx' || upfiles.fileExt=='xls' || upfiles.fileExt=='xlsx' || upfiles.fileExt=='ppt' || upfiles.fileExt=='pptx' }">
									 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
									 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览"
																				onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','039','${busId}')"></a>
																		</c:when>
																		<c:when test="${upfiles.fileExt=='txt' || upfiles.fileExt=='pdf'}">
							 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
									 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览"
																				onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','039','${busId}')"></a>
																		</c:when>
																		<c:otherwise>
									 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
																		</c:otherwise>
																	</c:choose>
																</p>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</div>
											</c:when>
										</c:choose>
										<div class="ws-type">
											<span class="hideOpt">
												<%--发言人可以删除自己的发言 --%>
												<c:if test="${userInfo.id==talk.talker}">
													<a href="javascript:void(0);" id="delAnnounTalk_${talk.id}" onclick="delTalk(${talk.id},${talk.isLeaf })" class="fa fa-trash-o" title="删除"></a>
												</c:if>
												<a id="img_${talk.id}" name="replyImg" onclick="showArea('addTalk${talk.id}','otherinstituAttrIframe')" class="fa fa-comment-o" title="回复"></a>
											</span>
											<span>
												<time>${talk.recordCreateTime}</time>
											</span>
										</div>
									</div>
									<div class="ws-clear"></div>
								</div>
								<!-- 回复层 -->
								<div id="addTalk${talk.id}" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk">
									<div class="shareText">
										<div class="ws-textareaBox" style="margin-top:10px;">
											<textarea id="operaterReplyTextarea_${talk.id}" name="operaterReplyTextarea_${talk.id}" style="height: 55px" rows="" cols="" class="form-control txtVoteTalk" placeholder="回复……"></textarea>
											<div class="ws-otherBox" style="position: relative;">
												<div class="ws-meh">
													<%--表情 --%>
													<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_${talk.id}"
														onclick="addBiaoQingObj('biaoQingSwitch_${talk.id}','biaoQingDiv_${talk.id}','operaterReplyTextarea_${talk.id}');"></a>
													<div id="biaoQingDiv_${talk.id}" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 15px">
														<!--表情DIV层-->
														<div class="main">
															<ul style="padding: 0px">
																<jsp:include page="/biaoqing.jsp"></jsp:include>
															</ul>
														</div>
													</div>
												</div>
												<%--常用意见 --%>
												<div class="ws-plugs">
													<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('operaterReplyTextarea_${talk.id}','${param.sid}');" title="常用意见"></a>
												</div>
												<%--分享按钮 --%>
												<div class="ws-share">
													<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('${busId}',${talk.id },this,${talk.talker})">回复</button>
												</div>
												<%--相关附件 --%>
												<div style="clear: both;"></div>
												<div class="ws-notice">
													<tags:uploadMore name="listUpfiles_${talk.id}.upfileId" showName="filename" comId="${sessionUser.comId}"></tags:uploadMore>
												</div>
											</div>
										</div>
									</div>
								</div>
							</c:forEach>
						</c:if>
					</div>
					<div class="parentRep">
						<div class="ws-textareaBox" style="margin-top:10px;">
							<textarea id="operaterReplyTextarea_-1" style="height: 55px" class="form-control " placeholder="请输入内容……"></textarea>
							<div class="ws-otherBox" style="position: relative;">
								<div class="ws-meh">
									<%--表情 --%>
									<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch" onclick="addBiaoQingObj('biaoQingSwitch','biaoQingDiv','operaterReplyTextarea_-1');"></a>
									<div id="biaoQingDiv" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 15px">
										<!--表情DIV层-->
										<div class="main">
											<ul style="padding: 0px">
												<jsp:include page="/biaoqing.jsp"></jsp:include>
											</ul>
										</div>
									</div>
								</div>
								<div class="ws-plugs">
									<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('operaterReplyTextarea_-1','${param.sid}');" title="常用意见"></a>
								</div>
								<div class="ws-share">
									<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('${busId}',-1,this,'');">发表</button>
								</div>
								<div class="ws-share margin-right-20">
									<button type="button" class="btn btn-info ws-btnBlue" onclick="viewAllTalk()">查看所有留言</button>
								</div>
								<div style="clear: both;"></div>
								<div class="ws-notice">
									<tags:uploadMore name="listUpfiles_-1.upfileId" showName="filename" comId="${sessionUser.comId}"></tags:uploadMore>
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
