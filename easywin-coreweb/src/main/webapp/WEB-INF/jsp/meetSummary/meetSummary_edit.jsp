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
			<div class="tickets-container bg-white" style="display:${((empty meetSummary.listSummaryFile && empty meetSummary.summary) || meetSummary.spState eq -1)?'block':'none'}" id="editSummary">
				<form action="/meetSummary/addSummary" class="subform" id="addSummaryForm" method="post">
					<input type="hidden" name="sid" value="${param.sid}">
					<input type="hidden" name="meetingId" value="${meeting.id}">
					<input type="hidden" name="id" value="${meetSummary.id}">
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
			<div class="tickets-container bg-white" style="display:${((empty meetSummary.listSummaryFile && empty meetSummary.summary) or meetSummary.spState eq -1)?'none':'block'}" id="viewSummary">
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
