<%@tag import="com.westar.base.util.StringUtil"%>
<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@attribute name="name" required="true" rtexprvalue="true"%>
<%@attribute name="value" required="true" rtexprvalue="true"%>
<%@attribute name="showValue" required="true" rtexprvalue="true"%>
<%@attribute name="showName" required="true" rtexprvalue="true"%>
<%@attribute name="uuid" required="false" rtexprvalue="true" description="头像下载uuid"%>
<%@attribute name="filename" required="false" rtexprvalue="true" description="头像附件名称"%>
<%@attribute name="gender" required="false" rtexprvalue="true" description="人员性别"%>
<%@attribute name="datatype" required="false" rtexprvalue="true"%>
<%@attribute name="nullmsg" required="false" rtexprvalue="true"%>
<%@attribute name="onclick" required="false" rtexprvalue="true"%>
<%@attribute name="errormsg" required="false" rtexprvalue="true"%>
<%@attribute name="queryParam" required="false" rtexprvalue="true"%>
<%@attribute name="comId" required="false" rtexprvalue="true" description="团队号"%>
<%@attribute name="defaultInit" required="false" rtexprvalue="true" description="是否默认初始化选择人员"%>
<%@attribute name="callBackStart" required="false" rtexprvalue="true" description="是否启用回调函数；yes表示启动"%>
<%	
	//是否显示头像
	String display="none";
	if(!StringUtil.isBlank(defaultInit)){
		display = "block";
	}
 %>
<input type="hidden" name="<%=name%>" id="<%=name.replace(".", "_")%>"
	value="<%=StringUtil.delNull(value)%>">
<input type="hidden" <%if (!StringUtil.isBlank(datatype)) {%>
	datatype="<%=StringUtil.delNull(datatype)%>" <%}%> 
	<%if (!StringUtil.isBlank(nullmsg)) {%>
	nullmsg="<%=StringUtil.delNull(nullmsg)%>" <%}%> 
	<%if (!StringUtil.isBlank(errormsg)) {%>
	errormsg="<%=StringUtil.delNull(errormsg)%>" <%}%> 
	type="text" relateMsgDiv="userOneImgDiv<%=name.replace(".", "_")%>"
	readonly="readonly"
	ondblclick="removeText('[\'<%=showName.replace(".", "_")%>\',\'<%=name.replace(".", "_")%>\']');"
	name="<%=showName%>" id="<%=showName.replace(".", "_")%>"
	value="<%=StringUtil.delNull(showValue)%>">
<div id="userOneImgDiv<%=name.replace(".", "_")%>" class="ticket-user pull-left">
<img id="userOneImg<%=name.replace(".", "_")%>" style="display:<%=display%>;float: left" class="user-avatar"   defaultInit="<%=StringUtil.delNull(defaultInit)%>"

<%if(!StringUtil.isBlank(comId) && !StringUtil.isBlank(value)){%> 
	src='/downLoad/userImg/<%=comId%>/<%=value%>?sid=${param.sid}'
<%}else if(StringUtil.isBlank(uuid)){%> 
	src='/static/headImg/3<%=StringUtil.isBlank(gender)?2:gender%>.jpg' 
<%}else{%> 
	src='/downLoad/down/<%=uuid%>/<%=filename%>?sid=${param.sid}'
<%}%> 
<%if (!StringUtil.isBlank(onclick)) {%>
onclick="userOne('<%=name.replace(".", "_")%>','<%=showName.replace(".", "_")%>','yes','${param.sid }','<%=callBackStart%>');"
<%}%> 
src='' title='<%=StringUtil.delNull(showValue)%>'/>
<span class="user-name" style="font-size:10px;" id="userOneName_<%=name.replace(".", "_")%>"><%=StringUtil.delNull(showValue)%></span>
</div>
<a href="javascript:void(0);" class="fa fa-user" style="padding: 0px 10px;float: left;margin-top:4px;font-size:24px;" title="人员单选" onclick="userOne('<%=name.replace(".", "_")%>','<%=showName.replace(".", "_")%>','yes','${param.sid }','<%=callBackStart%>');"></a>
