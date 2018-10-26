<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@tag import="com.westar.base.util.StringUtil"%>
<%@tag import="com.westar.base.model.DataDic"%>
<%@tag import="com.westar.core.web.DataDicContext"%>
<%@tag import="java.util.List"%>
<%@attribute name="type" required="true" rtexprvalue="true"%>
<%@attribute name="value" required="false" rtexprvalue="true"%>
<%@attribute name="name" required="false" rtexprvalue="true"%>
<%@attribute name="style" required="false" rtexprvalue="true"%>
<%@attribute name="datatype" required="false" rtexprvalue="true"%>
<%@attribute name="disabled" required="false" rtexprvalue="true"%>
<%
	List<DataDic> listTreeDataDic=DataDicContext.getInstance().listTreeDataDicByType(type);
	if(listTreeDataDic!=null){
	%>
	<div class="pull-left">
	<%
	boolean flg = true;
		for(int i=0;i<listTreeDataDic.size();i++){
			DataDic dataDic=listTreeDataDic.get(i);
			if(dataDic.getParentId()!=-1){
%>
	<label class="margin-right-10 no-margin-bottom">
    <input type="radio" value="<%=dataDic.getCode() %>" <%=!StringUtil.isBlank(disabled)?"disabled=\""+disabled+"\"":"" %> 
    <%=value!=null&&value.equals(dataDic.getCode())?"checked=\"checked\"":"" %>  
    <%=!StringUtil.isBlank(datatype)&&flg?" datatype=\""+datatype+"\"":"" %> 
    <%=!StringUtil.isBlank(style)?"class=\""+style+"\"":"" %> 
    <%=!StringUtil.isBlank(name)?"name=\""+name+"\"":"" %> 
    <%=!StringUtil.isBlank(name)?" id=\""+name.replace(".", "_")+"\"":"" %> >
    <%=dataDic.getZvalue()%>
    <span class="text"></span>
	</label>
	<%flg = false; %>
<%
			}
		}
		%>
		</div>
		<%
	}
%>
