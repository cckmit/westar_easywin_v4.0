<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="fieldCode" required="true" rtexprvalue="true"%>
${fieldCode==2&&thisLog.logType=='0'?'<font style="color:red">*</font>':''}