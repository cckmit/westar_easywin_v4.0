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
					<strong> <c:choose>
							<c:when test="${announ.type=='1'}">[通知]</c:when>
							<c:when test="${announ.type=='2'}">[通报]</c:when>
							<c:when test="${announ.type=='3'}">[决定]</c:when>
						</c:choose>${announ.title}
					</strong>
				</div>
				<div style="text-align:center; " class="margin-10">
					<span class="padding-left-20"> 发布单位：${announ.orgName } </span>
					<span class="padding-left-20"> 发布人：${announ.creatorName } </span>
					<span class="padding-left-20"> 时间：${fn:substring(announ.recordCreateTime,0,10) } </span>
					<span class="padding-left-20">
						阅读量：
						<a href="javascript:void(0);" onclick="viewRecord()" style="color:#428bca">${announ.readTime }</a>
					</span>
				</div>
				<div id="comment">${announ.announRemark}</div>

				<div id="file">
					<c:choose>
						<c:when test="${not empty announ.listAnnounFiles}">
							<div class="file_div">
								<c:forEach items="${announ.listAnnounFiles}" var="upfiles" varStatus="vs">
									<p class="p_text">
										附件(${vs.count})： ${upfiles.filename}
										<c:choose>
											<c:when test="${upfiles.fileExt=='doc' || upfiles.fileExt=='docx' || upfiles.fileExt=='xls' || upfiles.fileExt=='xlsx' || upfiles.fileExt=='ppt' || upfiles.fileExt=='pptx' }">
						 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
						 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览"
													onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','039','${announ.id}')"></a>
											</c:when>
											<c:when test="${upfiles.fileExt=='txt' || upfiles.fileExt=='pdf'}">
				 						&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${upfiles.uuid}/${upfiles.filename}?sid=${param.sid}" title="下载"></a>
						 				&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览"
													onclick="viewOfficePage('${upfiles.upfileId}','${upfiles.uuid}','${upfiles.filename}','${upfiles.fileExt}','${param.sid}','039','${announ.id}')"></a>
											</c:when>
											<c:otherwise>
						 				&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${upfiles.uuid}','${upfiles.filename}','${param.sid}')"></a>
											</c:otherwise>
										</c:choose>
									</p>
								</c:forEach>
							</div>
						</c:when>
					</c:choose>
				</div>
				<!-- <div class="">下一条：
				
				</div> -->
			</div>
		</div>
	</div>
</body>
</html>
