<%@tag import="com.westar.base.util.StringUtil"%>
<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="name" required="true" rtexprvalue="true"%>
<%@attribute name="showName" required="true" rtexprvalue="true"%>
<%@attribute name="value" required="false" rtexprvalue="true"%>
<%@attribute name="showValue" required="false" rtexprvalue="true"%>
<%@attribute name="datatype" required="false" rtexprvalue="true"%>
<%@attribute name="readOnly" required="false" rtexprvalue="true"%>
<%@attribute name="queryParam" required="false" rtexprvalue="true"%>


<input type="hidden" name="<%=name%>" id="<%=name.replace(".", "_")%>" value="<%=StringUtil.delNull(value)%>">
<input onchange="removeText('<%=name.replace(".", "_")%>');" <%if(!StringUtil.isBlank(datatype)){ %>datatype="<%=StringUtil.delNull(datatype) %>"<%} %> value="<%=StringUtil.delNull(showValue) %>" type="text" <%if(readOnly==null||readOnly.equals("true")){ %>readonly="readonly"<%} %> ondblclick="removeText('[\'<%=showName.replace(".", "_") %>\',\'<%=name.replace(".", "_") %>\']');" name="<%=showName%>" id="<%=showName.replace(".", "_")%>">
<img src="/static/img/check.png" style="cursor: pointer;" onclick="officeOne('<%=name.replace(".", "_") %>','<%=showName.replace(".", "_") %>','<%=StringUtil.delNull(queryParam).replace("'", "\\'") %>','${param.sid }');" />