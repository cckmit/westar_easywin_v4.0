<%@tag import="com.westar.base.util.StringUtil"%>
<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="name" required="true" rtexprvalue="true"%>
<%@attribute name="showName" required="true" rtexprvalue="true"%>
<%@attribute name="fileTypes" required="false" rtexprvalue="true"%>
<%@attribute name="fileSize" required="false" rtexprvalue="true"%>
<%@attribute name="numberOfFiles" required="false" rtexprvalue="true"%>
<%@attribute name="datatype" required="false" rtexprvalue="true"%>
<%@attribute name="width" required="false" rtexprvalue="true"%>
<%@attribute name="ifream" required="false" rtexprvalue="true"%>
<%@attribute name="comId" required="false" rtexprvalue="true"%>

<%
String[] ns = name.split("\\.");
String key = ns[ns.length-1];
if(width==null){
  width="250px";
}
 %>
 <div style="clear:both;width: 300px" class="wu-example">
    <!--用来存放文件信息-->
    <div id="thelist<%=name.replace(".", "_") %>" class="uploader-list" ></div>
    <div class="btns btn-sm">
        <div id="picker<%=name.replace(".", "_") %>">选择文件</div>
    </div>
	<script type="text/javascript">
		loadWebUpApp('<%=name.replace(".", "_") %>','<%=StringUtil.delNull(ifream) %>',"picker<%=name.replace(".", "_") %>","thelist<%=name.replace(".", "_") %>",'file<%=name.replace(".", "_") %>')
	</script>
	
	<div style="position: relative; width: 350px; height: 90px;display: none">
		<div style="float: left; width: <%=width%>" >
			<input <%if(!StringUtil.isBlank(datatype)){ %>datatype="<%=StringUtil.delNull(datatype) %>"<%} %>  type="text" readonly="readonly" 
			name="<%=name%>" id="<%=name.replace(".", "_")%>" >
		</div>
	</div>
</div>