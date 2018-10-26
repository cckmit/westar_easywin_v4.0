<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">

<title><%=SystemStrConstant.TITLE_NAME %></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />

<!--Beyond styles-->
<link href="/static/assets/css/beyond.min.css" rel="stylesheet"  type="text/css">
<link href="/static/assets/css/beyond_2.min.css" rel="stylesheet"  type="text/css">
<link href="/static/assets/css/animate.min.css" rel="stylesheet" type="text/css">
<script src="/static/assets/js/skins.min.js"></script>


<!--Core CSS -->
<link href="/static/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" />
<link href="/static/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="/static/assets/css/new_file.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/plugins/layui/layui.js"></script>
<link rel="stylesheet" href="/static/plugins/layui/css/layui.css">
<script src="/static/plugins/layer/layer.js" type="text/javascript" charset="utf-8"></script>

<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
</head>
<body scroll="no" style="background-color: #fff">

<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">客户类型多选</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;padding:5px">
					<table width="100%" border="0" align="center" >
						<tr bgcolor="#FFFFFF">
							<td align="left">
								<table width="90%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td align="left">
										<img src="/static/images/ico_p.gif" width="20" height="14" align="left"/> 
										<a href="javascript:selectAll();">所有</a> 
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td style="padding-right: 10px;">
								<table width="100%" border="0">
									<tr bgcolor="#FFFFFF">
										<td width="50%"><iframe id="crmTypetree" name="crmTypetree" style="width: 100%;height:350px; border: 1px solid #86B1E8;" frameborder="0"></iframe></td>
										<td style="padding-left: 1px;"></td>
										<td style="width: 48%">
											<div style="border: 1px solid #86B1E8; width: 100%;">
												<form name="form1" style="margin: 0 auto; padding: 0px;">
													<table width="100%">
														<tr>
															<td bgcolor="#FFFFFF" valign="top">
																<div style="border: 1px solid #ffffff; width: 100%; text-align: center; padding-bottom: 0px">
																	<select name="crmTypeSelect" id="crmTypeSelect" ondblclick="removeOne(this)" style="width: 100%;height:350px;  margin: -2 -10 -10 -10;" multiple="multiple">
																	</select>
																</div>
															</td>
														</tr>
													</table>
												</form>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
	
				</div>
			</div>
		</div>
	</div>
</div>

</body>
<script type="text/javascript">
//页面加载后初始化 如果父页面文本框中已经有值，则添加到下拉框中

//设置选项
function setOptions(o){
	if(o){
		for(var i=0;i<o.length;i++){
		    $('#crmTypeSelect').append("<option value='"+o[i].value+"'>"+o[i].text+"</option>");
		}
	}
	$("#crmTypetree").attr("src","/common/crmTypeMoreTreePage?sid=${param.sid }");
}
function appendCrmType(checked,str){
	  var json = eval('(' + str + ')'); 
	  if(checked==true){
	   if($("#crmTypeSelect option[value='"+json.id+"']").length==0){
	     $('#crmTypeSelect').append("<option value='"+json.id+"'>"+json.typeName+"</option>");
	   }
	  }else{
	   var selectobj = document.getElementById("crmTypeSelect");
	   for (var i = 0; i < selectobj.options.length; i++) {
			  if(selectobj.options[i].value==json.id){
			    selectobj.options[i] = null;
			    break;
			  }
	   }
	  }
	}
function returnCrmType() {
	var selectobj = document.getElementById("crmTypeSelect");
	var options = selectobj.options;
	return options;
}

 function removeAll(){
	  removeOptions('crmTypeSelect');
	  crmTypetree.uncheckAll();
	}
 
 function removeOne(ts){
	  $('#crmTypeSelect').find("option:selected").each(function(index){
	    crmTypetree.uncheck($(this).attr("value"));
	  });
	  removeClick(ts.id);
	}
//所有机构
 function selectAll(){
	 crmTypetree.initZtree();
 }
</script>
</html>
