<%@tag import="com.westar.base.util.StringUtil"%>
<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="name" required="true" rtexprvalue="true"%>
<%@attribute name="showName" required="true" rtexprvalue="true"%>
<%@attribute name="datatype" required="false" rtexprvalue="true"%>
<%@attribute name="queryParam" required="false" rtexprvalue="true"%>
<%@attribute name="style" required="false" rtexprvalue="true"%>
<%@attribute name="callBackStart" required="false" rtexprvalue="true" description="回调"%>
<%@attribute name="disDivKey" required="false" rtexprvalue="true" description="结果显示DIV主键"%>

<%
String[] ns = name.split("\\.");
String key = ns[ns.length-1];
%>
<div style="float: left;width: 250px;display: none">
	<select  <%if(!StringUtil.isBlank(datatype)){ %> datatype="<%=StringUtil.delNull(datatype) %>"<%} %> list="<%=name.replace("."+key, "") %>" listkey="<%=key %>" listvalue="<%=showName %>" id="<%=name.replace(".", "_") %>" name="<%=name %>" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
	<jsp:doBody></jsp:doBody>
	</select>
</div>
<div id="<%=disDivKey%>" class="pull-left" <%=!StringUtil.isBlank(style)?"style=\""+style+"\"":"style=\"max-width:460px\"" %>></div>
<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-5" title="部门多选" onclick="depMore('<%=name.replace(".", "_") %>','<%=StringUtil.delNull(queryParam).replace("'", "\\'") %>','${param.sid }','<%=callBackStart%>','<%=disDivKey%>');"><i class="fa fa-plus"></i>选择</a>