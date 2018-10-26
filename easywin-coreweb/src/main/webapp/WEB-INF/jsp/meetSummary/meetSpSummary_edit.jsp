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
         	<span class="widget-caption blue">会议纪要</span>
		</div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white" id="editSummary">
				<form action="/meetSummary/updateSpSummary" class="subform" id="addSummaryForm" method="post">
					<input type="hidden" name="sid" value="${param.sid}">
					<input type="hidden" name="id" value="${meetSummary.id}">
					<input type="hidden" name="actInstaceId" value="${meetSummary.actInstaceId}">
					<input type="hidden" name="meetingId" value="${meeting.id}">
					<input type="hidden" name="redirectPage" id="redirectPage" >
					<div>
						<div style="clear:both" class="ws-process">
							<div id="thelistlistSummaryFile_upfileId" style="width: 300px">
								<c:choose>
									<c:when test="${not empty meetSummary.listSummaryFile}">
										<c:forEach items="${meetSummary.listSummaryFile}" var="upfile" varStatus="vs">
										 <div id="wu_file_0_-${upfile.upfileId}" class="uploadify-queue-item">	
											<div class="cancel">
												<a href="javascript:void(0)" fileDone="1" fileId="${upfile.upfileId}">X</a>
											</div>	
												<span class="fileName" title="${upfile.filename}">
													<tags:cutString num="25">${upfile.filename}</tags:cutString>(已有文件)
												</span>
												<span class="data"> - 完成</span>
											<div class="uploadify-progress">
												<div class="uploadify-progress-bar" style="width: 100%;"></div>
											</div>	
										</div>	
										</c:forEach>
									</c:when>
								</c:choose>
							</div>
							<div class="btns btn-sm">
								<div id="pickerlistSummaryFile_upfileId">选择文件</div>
							</div>
							<script type="text/javascript">
								loadWebUpfiles('listSummaryFile_upfileId','${param.sid}','','pickerlistSummaryFile_upfileId','thelistlistSummaryFile_upfileId','filelistSummaryFile_upfileId');
							</script>
							<div style="position: relative; width: 350px; height: 90px;display: none">
								<div style="float: left;">
									<select list="listSummaryFile" listkey="upfileId" listvalue="fileName" id="listSummaryFile_upfileId" 
									name="listSummaryFile.upfileId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">
										<c:choose>
											<c:when test="${not empty meetSummary.listSummaryFile}">
												<c:forEach items="${meetSummary.listSummaryFile}" var="upfile" varStatus="vs">
													<option selected="selected" value="${upfile.upfileId}">${upfile.filename}</option>
												</c:forEach>
											</c:when>
										</c:choose>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div style="width: 100%">
						<textarea class="form-control" id="summary" name="summary" rows="" cols="" style="width:98%;display:none;">${meetSummary.summary}</textarea> 
						<iframe ID="eWebEditor1" src="/static/plugins/ewebeditor/ewebeditor.htm?id=summary&style=blue" frameborder="0" scrolling="no" width="100%" height="350"></iframe>
					</div>
				</form>
			</div>
			
               </div>
           </div>
		</div>
</body>
</html>
