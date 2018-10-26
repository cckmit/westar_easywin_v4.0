<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="fieldCode" required="true" rtexprvalue="true"%>
${fieldCode==1||thisLog.logType!='0'?'readonly="readonly" style="background-color: #e1e1e6;"':''}
${fieldCode==2&&thisLog.logType=='0'?'datatype="*"':''}