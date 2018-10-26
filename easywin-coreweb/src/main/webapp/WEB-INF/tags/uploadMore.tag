<%@tag import="com.westar.base.util.StringUtil"%>
<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@attribute name="name" required="true" rtexprvalue="true"%>
<%@attribute name="showName" required="true" rtexprvalue="true"%>
<%@attribute name="fileTypes" required="false" rtexprvalue="true"%>
<%@attribute name="fileSize" required="false" rtexprvalue="true"%>
<%@attribute name="numberOfFiles" required="false" rtexprvalue="true"%>
<%@attribute name="datatype" required="false" rtexprvalue="true"%>
<%@attribute name="width" required="false" rtexprvalue="true"%>
<%@attribute name="ifream" required="false" rtexprvalue="true"%>
<%@attribute name="comId" required="false" rtexprvalue="true"%>
<%@attribute name="selectDivId" required="false" rtexprvalue="true"%>

<%
	String[] ns = name.split("\\.");
	String key = ns[ns.length - 1];
	if (width == null) {
		width = "300px";
	}
%>
<div style="clear: both; width: <%=width%>" class="wu-example">
	<!--用来存放文件信息-->
	<div id="thelist<%=name.replace(".", "_")%>" class="uploader-list"></div>
	<div class="btns btn-sm">
		<div style="position: relative;">
			<div style="position: relative; display: inline-block; cursor: pointer; color: rgb(255, 255, 255); text-align: center; background: rgb(0, 183, 238); padding: 6px 10px; border-radius: 3px; overflow: hidden;"
				onclick="chooseLoadUpfile('<%=name.replace(".", "_")%>','${param.sid}','<%=StringUtil.delNull(ifream)%>','picker<%=name.replace(".", "_")%>','thelist<%=name.replace(".", "_")%>','file<%=name.replace(".", "_")%>')">选择文件</div>
		</div>
		<div id="picker<%=name.replace(".", "_")%>" style="display: none">选择文件</div>
	</div>
	<script type="text/javascript">
		loadWebUpfiles('<%=name.replace(".", "_")%>','${param.sid}','<%=StringUtil.delNull(ifream)%>',"picker<%=name.replace(".", "_")%>","thelist<%=name.replace(".", "_")%>",'file<%=name.replace(".", "_")%>')
	</script>
	<div style="position: relative; width:<%=width%>; height: 90px; display: none" id="<%=StringUtil.delNull(selectDivId)%>">
		<div style="float: left; width: <%=width%>">
			<select <%if (!StringUtil.isBlank(datatype)) {%> datatype="<%=StringUtil.delNull(datatype)%>" <%}%> list="<%=name.replace("." + key, "")%>" listkey="<%=key%>" listvalue="<%=showName%>"
				id="<%=name.replace(".", "_")%>" name="<%=name%>" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">
				<jsp:doBody></jsp:doBody>
			</select>
		</div>
	</div>
</div>