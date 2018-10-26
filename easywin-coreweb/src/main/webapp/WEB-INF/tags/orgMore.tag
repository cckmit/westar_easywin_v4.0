<%@tag import="com.westar.base.util.StringUtil"%>
<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="name" required="true" rtexprvalue="true"%>
<%@attribute name="showName" required="true" rtexprvalue="true"%>
<%@attribute name="datatype" required="false" rtexprvalue="true"%>
<%@attribute name="queryParam" required="false" rtexprvalue="true"%>

<%
String[] ns = name.split("\\.");
String key = ns[ns.length-1];
 %>
<div style="position: relative; width: 350px; height: 100px;">
	<div style="float: left; width: 250px">
		<select  <%if(!StringUtil.isBlank(datatype)){ %> datatype="<%=StringUtil.delNull(datatype) %>"<%} %> list="<%=name.replace("."+key, "") %>" listkey="<%=key %>" listvalue="<%=showName %>" id="<%=name.replace(".", "_") %>" name="<%=name %>" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
		<jsp:doBody></jsp:doBody>
		</select>
	</div>
	<div style="position: absolute; bottom: 0px; padding-left: 10px;margin-left:5px; display: inline;">
		<div style="position: absolute; bottom: 40px">
			<img src="/static/img/check.png" style="cursor: pointer;" onclick="orgMore('<%=name.replace(".", "_") %>','<%=StringUtil.delNull(queryParam).replace("'", "\\'") %>','${param.sid }');" />
		</div>
		<div>
			<img src="/static/img/clear.png" style="cursor: pointer;" onclick="removeOptions('<%=name.replace(".", "_") %>');" />
		</div>
	</div>
</div>