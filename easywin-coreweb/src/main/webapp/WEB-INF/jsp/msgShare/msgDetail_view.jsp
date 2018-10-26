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
	<div class="widget radius-bordered">
		<div class="widget-body no-shadow">
			<div class="collapse in">
				<div class="form-group">
					<%--<label for="xsinput" style="font-weight: bold;"> 分享内容 </label>
					--%>
					<div class="padding-left-20" style="word-wrap: break-word; word-break: break-all;">
						<tags:viewTextArea>${msg.content}</tags:viewTextArea>
					</div>
				</div>

			</div>
		</div>
	</div>
</body>
</html>
