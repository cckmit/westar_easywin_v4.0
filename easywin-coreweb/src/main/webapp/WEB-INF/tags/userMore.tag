<%@tag import="com.westar.base.util.StringUtil"%>
<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@attribute name="name" required="true" rtexprvalue="true"%>
<%@attribute name="showName" required="true" rtexprvalue="true"%>
<%@attribute name="datatype" required="false" rtexprvalue="true"%>
<%@attribute name="style" required="false" rtexprvalue="true"%>
<%@attribute name="queryParam" required="false" rtexprvalue="true"%>
<%@attribute name="callBackStart" required="false" rtexprvalue="true" description="是否启用回调函数；yes表示启动"%>
<%@attribute name="disDivKey" required="false" rtexprvalue="true" description="结果显示DIV主键"%>

<%
	String[] ns = name.split("\\.");
	String key = ns[ns.length - 1];
%>
<div style="float: left; width: 250px;display:none;">
	<select <%if (!StringUtil.isBlank(datatype)) {%>
		datatype="<%=StringUtil.delNull(datatype)%>" <%}%>
		list="<%=name.replace("." + key, "")%>" listkey="<%=key%>"
		listvalue="<%=showName%>" id="<%=name.replace(".", "_")%>"
		name="<%=name%>" ondblclick="removeClick(this.id)"
		multiple="multiple" moreselect="true"
		style="width: 100%; height: 100px;">
		<jsp:doBody></jsp:doBody>
	</select>
</div>
<div id="<%=disDivKey%>" class="pull-left" <%=!StringUtil.isBlank(style)?"style=\""+style+"\"":"style=\"max-width:460px\"" %>></div>
<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-bottom-5 margin-left-5" title="人员多选" 
onclick="userMore('<%=name.replace(".", "_")%>','yes','${param.sid }',
'<%=callBackStart%>','<%=disDivKey%>');" style="float: left;"><i class="fa fa-plus"></i>选择</a>
<!-- <img id="userMoreClear_png" src="/static/img/clear.png" style="cursor: pointer;float:left;margin-left:3px;"
			onclick="removeOptions('<%=name.replace(".", "_")%>');" /> -->