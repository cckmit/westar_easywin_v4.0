<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@tag import="com.westar.base.util.StringUtil"%>
<%@tag import="com.westar.base.model.DataDic"%>
<%@tag import="com.westar.core.web.DataDicContext"%>
<%@tag import="java.util.List"%>
<%@attribute name="id" required="false" rtexprvalue="true"%>
<%@attribute name="type" required="true" rtexprvalue="true"%>
<%@attribute name="value" required="false" rtexprvalue="true"%>
<%@attribute name="name" required="false" rtexprvalue="true"%>
<%@attribute name="onchange" required="false" rtexprvalue="true"%>
<%@attribute name="style" required="false" rtexprvalue="true"%>
<%@attribute name="clz" required="false" rtexprvalue="true"%>
<%@attribute name="please" required="false" rtexprvalue="true"%>
<%@attribute name="disabled" required="false" rtexprvalue="true"%>
<%@attribute name="datatype" required="false" rtexprvalue="true"%>
<%
	List<DataDic> listTreeDataDic=DataDicContext.getInstance().listTreeDataDicByType(type);
%>
<select id="<%=name%>" 
	<%=!StringUtil.isBlank(datatype)?"datatype=\""+datatype+"\"":"" %> 
	<%=!StringUtil.isBlank(clz)?"class=\""+clz+"\"":"" %> 
	<%=!StringUtil.isBlank(style)?"style=\""+style+"\"":"" %> 
	<%=!StringUtil.isBlank(name)?"name=\""+name+"\"":"" %> 
	<%=!StringUtil.isBlank(onchange)?"onchange=\""+onchange+"\"":"" %>
	<%=!StringUtil.isBlank(disabled)?"disabled=\""+disabled+"\"":"" %>
	>

<%
 if(please!=null&&(please.equals("t")||please.equals("true"))){ 
 %>
 <option value="">请选择</option>
 <%
 } 
 %>

<%
	if(listTreeDataDic!=null){
		for(int i=0;i<listTreeDataDic.size();i++){
			DataDic dataDic=listTreeDataDic.get(i);
			if(dataDic.getParentId()!=-1){
%>
    
	<option value="<%=dataDic.getCode() %>" <%=value!=null&&value.equals(dataDic.getCode())?"selected=\"selected\"":"" %> >
	<%
		if(dataDic.getLevel()>2){
			for(int j=2;j<dataDic.getLevel();j++){
	%>
	&nbsp;
	<%
			}
		}
	 %>
	<%=dataDic.getZvalue()%>
	</option>
<%
			}
		}
	}
%>
</select>
