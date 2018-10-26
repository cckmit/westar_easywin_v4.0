<%@tag import="org.springframework.context.ApplicationContext"%>
<%@tag import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@tag import="com.westar.base.util.StringUtil"%>
<%@tag import="com.westar.base.model.DataDic"%>
<%@tag import="com.westar.base.model.ModuleOperateConfig"%>
<%@tag import="com.westar.core.web.DataDicContext"%>
<%@tag import="com.westar.core.service.DataDicService"%>

<%@tag import="java.util.List"%>
<%@attribute name="type" required="true" rtexprvalue="true"%>
<%@attribute name="value" required="false" rtexprvalue="true"%>
<%@attribute name="name" required="false" rtexprvalue="true"%>
<%@attribute name="style" required="false" rtexprvalue="true"%>
<%@attribute name="datatype" required="false" rtexprvalue="true"%>
<%@attribute name="disabled" required="false" rtexprvalue="true"%>
<%@attribute name="check" required="true" rtexprvalue="true"%>
<%@attribute name="comId" required="false" rtexprvalue="true"%>
<%@attribute name="moduleType" required="false" rtexprvalue="true"%>
<%
	List<DataDic> listTreeDataDic=DataDicContext.getInstance().listTreeDataDicByType(type);
	if(listTreeDataDic!=null){
		//获取验证数据集合
		List<ModuleOperateConfig> listConfig = null;
		/* if(!StringUtil.isBlank(check) && null!=moduleType && !StringUtil.isBlank(moduleType) && null!=comId && !StringUtil.isBlank(comId)){
				ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(application);
				DataDicService dataDicService = (DataDicService) applicationContext.getBean(DataDicService.class);
				listConfig = dataDicService.listModuleOperateConfig(Integer.parseInt(comId), moduleType);
		} */
		for(int i=0;i<listTreeDataDic.size();i++){
			DataDic dataDic=listTreeDataDic.get(i);
			if(dataDic.getParentId()!=-1){
				if(null!=listConfig){
					boolean unCheck = false;
					for(ModuleOperateConfig vo:listConfig){
						if(dataDic.getCode().equals(vo.getOperateType())){
							%>
						    <input type="checkbox" <%=!StringUtil.isBlank(style)?"style=\""+style+"\"":"" %> value="<%=dataDic.getCode() %>" <%=!StringUtil.isBlank(disabled)?"disabled=\""+disabled+"\"":"" %> checked="checked"  <%=!StringUtil.isBlank(datatype)?" datatype=\""+datatype+"\"":"" %> <%=!StringUtil.isBlank(style)?"style=\""+style+"\"":"" %> <%=!StringUtil.isBlank(name)?"name=\""+name+"\"":"" %> <%=!StringUtil.isBlank(name)?" id=\""+name.replace(".", "_")+"\"":"" %> >
						    <label style="width:50px;"><%=dataDic.getZvalue()%></label>
							<%
							unCheck = true;
							break;
						}
					}
					if(!unCheck){
							%>
						    <input type="checkbox"  <%=!StringUtil.isBlank(style)?"style=\""+style+"\"":"" %>  value="<%=dataDic.getCode() %>" <%=!StringUtil.isBlank(disabled)?"disabled=\""+disabled+"\"":"" %> <%=!StringUtil.isBlank(datatype)?" datatype=\""+datatype+"\"":"" %> <%=!StringUtil.isBlank(style)?"style=\""+style+"\"":"" %> <%=!StringUtil.isBlank(name)?"name=\""+name+"\"":"" %> <%=!StringUtil.isBlank(name)?" id=\""+name.replace(".", "_")+"\"":"" %> >
						    <label style="width:50px;"><%=dataDic.getZvalue()%></label>
							<%
					}
				}else{
				%>
				    <%-- <input type="checkbox" <%=!StringUtil.isBlank(style)?"style=\""+style+"\"":"" %> value="<%=dataDic.getCode() %>" <%=!StringUtil.isBlank(disabled)?"disabled=\""+disabled+"\"":"" %> <%=!StringUtil.isBlank(datatype)?" datatype=\""+datatype+"\"":"" %> <%=!StringUtil.isBlank(style)?"style=\""+style+"\"":"" %> <%=!StringUtil.isBlank(name)?"name=\""+name+"\"":"" %> <%=!StringUtil.isBlank(name)?" id=\""+name.replace(".", "_")+"\"":"" %> >
				    <label style="width:50px;"><%=dataDic.getZvalue()%></label> --%>
				    <label class="padding-left-5">
					 	<input type="checkbox" class="colored-blue" value="<%=dataDic.getCode() %>" <%=!StringUtil.isBlank(disabled)?"disabled=\""+disabled+"\"":"" %> <%=!StringUtil.isBlank(datatype)?" datatype=\""+datatype+"\"":"" %> <%=!StringUtil.isBlank(name)?"name=\""+name+"\"":"" %> <%=!StringUtil.isBlank(name)?" id=\""+name.replace(".", "_")+"\"":"" %>/>
					 	<span class="text" style="color:inherit;"><%=dataDic.getZvalue()%></span>
				    </label>
				<%
				}
			}
		}
	}
%>
