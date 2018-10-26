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
<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
<div class="widget-body">
	<div class="widget radius-bordered">
    	 <div class="widget-header bg-bluegray no-shadow">
         	<span class="widget-caption blue">会议纪要
         		<c:choose>
         			<c:when test="${meetSummary.spState eq -1}"><span class="red">[审批不通过]</span></c:when>
         			<c:when test="${meetSummary.spState eq 1}"><span class="blue">[审批中]</span></c:when>
         			<c:when test="${meetSummary.spState eq 4}"><span class="green">[审批通过]</span></c:when>
         		</c:choose>
         	</span>
		</div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white" id="viewSummary">
				<div>
					<c:choose>
						<c:when test="${not empty meetSummary.listSummaryFile}">
							<div class="file_div" style="margin-top: 6px">
								<c:forEach items="${meetSummary.listSummaryFile}" var="fileObj" varStatus="vs">
									<c:choose>
										<c:when test="${fileObj.isPic==1}">
											<p class="p_text">
											附件(${vs.count})：
												${fileObj.filename}
					 						<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/${fileObj.uuid}/${fileObj.filename}?sid=${param.sid}" title="下载"></a>
					 						<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);"onclick="showPic('/downLoad/down/${fileObj.uuid}/${fileObj.filename}','${param.sid}','${fileObj.upfileId}','017','${meeting.id }')"></a>
											</p>
										</c:when>
										<c:otherwise>
											<p class="p_text">
												附件(${vs.count})：
													${fileObj.filename}
												<c:choose>
								 					<c:when test="${fileObj.fileExt=='doc' || fileObj.fileExt=='docx' || fileObj.fileExt=='xls' || fileObj.fileExt=='xlsx' || fileObj.fileExt=='ppt' || fileObj.fileExt=='pptx' }">
										 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${fileObj.uuid}','${fileObj.filename}','${param.sid}')"></a>
										 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${fileObj.upfileId}','${fileObj.uuid}','${fileObj.filename}','${fileObj.fileExt}','${param.sid}','017','${meeting.id }')"></a>
								 					</c:when>
								 					<c:when test="${fileObj.fileExt=='txt' ||fileObj.fileExt=='pdf'}">
								 						<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/${fileObj.uuid}/${fileObj.filename}?sid=${param.sid}" title="下载"></a>
										 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${fileObj.upfileId}','${fileObj.uuid}','${fileObj.filename}','${fileObj.fileExt}','${param.sid}','017','${meeting.id }')"></a>
								 					</c:when>
								 					<c:otherwise>
										 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${fileObj.uuid}','${fileObj.filename}','${param.sid}')"></a>
								 					</c:otherwise>
								 				</c:choose>
												</p>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									</div>
								</c:when>
							</c:choose>
						</div>
						<div>
							<div class="comment" style="width: 100%">
								<c:choose>
									<c:when test="${empty meetSummary.summary}">
										<font color="red">记录员未上传会议纪要</font>
									</c:when>
									<c:otherwise>
										${meetSummary.summary}
									</c:otherwise>
								</c:choose>
							</div>	
						</div>
               		</div>
               </div>
           </div>
		</div>
</body>
</html>
