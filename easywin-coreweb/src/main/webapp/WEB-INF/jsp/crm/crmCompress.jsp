<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>

<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>

<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>

<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
function chooseAll(cls){
	$("."+cls).click()
}

function formSub(){
	$("#subform").submit();
}

$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
})
</script>
<style type="text/css">
	.title{background-color:#B2DFEE;
	text-align: right;
	}
	.discripe{
		padding:10px 0px; 
		padding-left: 50px;
		background-color: #B2DFEE;
	}
</style>
</head>
<body>
<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	<span class="widget-caption themeprimary ps-layerTitle">客户合并</span>
    <div class="widget-buttons">
		<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
			<i class="fa fa-times themeprimary"></i>
		</a>
	</div>
</div>
<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">

	<section id="container">
	<form id="subform" class="subform" method="post" action="/crm/crmCompress">
		<input type="hidden" name="redirectPage" value="${redirectPage}">
		<tags:token></tags:token>
		<c:choose>
			<c:when test="${not empty ids}">
			<c:forEach 	items="${ids}" var="preId" varStatus="">
				<input type="hidden" name="ids" value="${preId}">
			</c:forEach>
			</c:when>
		</c:choose>
	<div class="ws-projectBox">
				<div class="ws-projectIn">
					<div class="ws-titleBox">
						<h6 class="ws-title">
							合并客户信息(<font color="red">客户合并后将不能还原</font>)
						</h6>
						</div>
							<div class="panel-body">
	                                <table class="table  table-bordered general-table">
	                                       <tr>
	                                           <td class="title" style="width: 19%;">客户名称&nbsp;&nbsp;</td>
	                                           <td style="width: 27%">
	                                           	${crm0.customerName}
	                                           	<a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseAll('crm0')">[全选]</a>
	                                           </td>
	                                           <td style="width: 27%">
	                                           		${crm1.customerName}
	                                           		<a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseAll('crm1')">[全选]</a>
	                                           </td>
	                                           <c:if test="${index==3}">
	                                           	<td style="width:27%">
	                                           		${crm2.customerName}
	                                           		<a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseAll('crm2')">[全选]</a>
	                                           	</td>
	                                           </c:if>
	                                       </tr>
	                                       <tr>
	                                           <td class="title" style="width: 19%;">合并对象 <font color="red">*</font></td>
	                                           <td style="width: 27%">
		                                           	<label>
			                                           	<input type="radio" name="id" class="crm0" value="${crm0.id}" checked="checked">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           </td>
	                                           <td style="width: 27%">
	                                           		<label>
			                                           	<input type="radio" name="id" class="crm1" value="${crm1.id }">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           </td>
	                                           <c:if test="${index==3}">
	                                           	<td style="width:27%">
	                                           		<label>
		                                           		<input type="radio" name="id" class="crm2" value="${crm2.id }">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           	</td>
	                                           </c:if>
	                                       </tr>
	                                       <tr>
	                                           <td class="title">客户区域 <font color="red">*</font></td>
	                                           <td>
	                                           		<label>
			                                           	<input type="radio" name="areaId" class="crm0" value="${crm0.areaId }" checked="checked">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
		                                           	${crm0.areaName} 
		                                       </td>
	                                           <td>
	                                           		<label>
		                                           		<input type="radio" name="areaId" class="crm1" value="${crm1.areaId }">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           		${crm1.areaName }
	                                           </td>
	                                           <c:if test="${index==3}">
	                                           		<td>
	                                           			<label>
		                                           			<input type="radio" name="areaId" class="crm2" value="${crm2.areaId }">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
	                                           			${crm2.areaName }
	                                           		</td>
	                                           </c:if>
	                                           
	                                       </tr>
	                                       <tr>
	                                           <td class="title">客户类型 <font color="red">*</font></td>
	                                           <td>
	                                           		<label>
	                                           			<input type="radio" name="customerTypeId" class="crm0" value="${crm0.customerTypeId }" checked="checked">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           		${crm0.typeName }
	                                           	</td>
	                                           <td>
	                                           		<label>
		                                           		<input type="radio" name="customerTypeId" class="crm1" value="${crm1.customerTypeId }">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                          		${crm1.typeName }
	                                          	</td>
	                                            <c:if test="${index==3}">
	                                           		<td>
	                                           			<label>
		                                           			<input type="radio" name="customerTypeId" class="crm2" value="${crm2.customerTypeId }">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
	                                           			${crm2.typeName }
	                                           		</td>
	                                            </c:if>
	                                       </tr>
	                                       <tr>
	                                           <td class="title">联系电话&nbsp;&nbsp; </td>
	                                           <td>
	                                           		<label>
		                                           		<input type="radio" name="linePhone" class="crm0" value="${crm0.linePhone}" checked="checked">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
		                                           	${crm0.linePhone}
	                                           	</td>
	                                           <td>
	                                           		<label>
			                                           <input type="radio" name="linePhone" class="crm1" value="${crm1.linePhone}">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
		                                           	${crm1.linePhone}
	                                           </td>
	                                            <c:if test="${index==3}">
	                                           		<td>
	                                           			<label>
		                                           			<input type="radio" name="linePhone" class="crm2" value="${crm2.linePhone}">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
		                                           		${crm2.linePhone}
	                                           		</td>
	                                            </c:if>
	                                       </tr>
	                                       <tr>
	                                           <td class="title">传真&nbsp;&nbsp; </td>
	                                           <td>
	                                           		<label>
		                                           		<input type="radio" name="fax" class="crm0" value="${crm0.fax}" checked="checked">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           		${crm0.fax}
	                                           	</td>
	                                           <td>
	                                           		<label>
	                                           			<input type="radio" name="fax" class="crm1" value="${crm1.fax}">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           		${crm1.fax}
	                                           	</td>
	                                            <c:if test="${index==3}">
	                                          		 <td>
	                                          		 	<label>
		                                          		 	<input type="radio" name="fax" class="crm2" value="${crm2.fax}">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
	                                           			${crm2.fax}
	                                          		 </td>
	                                            </c:if>
	                                       </tr>
	                                       <tr>
	                                           <td class="title">邮编&nbsp;&nbsp; </td>
	                                           <td>
	                                           		<label>
		                                           		<input type="radio" name="postCode" class="crm0" value="${crm0.postCode}" checked="checked">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           		${crm0.postCode}
	                                           	</td>
	                                           <td>
	                                           		<label>
		                                           		<input type="radio" name="postCode" class="crm1" value="${crm1.postCode}">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           		${crm1.postCode}
	                                           	</td>
	                                            <c:if test="${index==3}">
	                                          		 <td>
	                                          		 	<label>
		                                          		 	<input type="radio" name="postCode" class="crm2" value="${crm2.postCode}">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
	                                          		 	${crm2.postCode}
	                                          		 </td>
	                                            </c:if>
	                                       </tr>
	                                       <tr>
	                                           <td class="title">联系地址&nbsp;&nbsp; </td>
	                                           <td>
	                                           		<label>
		                                           		<input type="radio" name="address" class="crm0" value="${crm0.address}" checked="checked">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           		${crm0.address}
	                                           	</td>
	                                           <td>
	                                           		<label>
		                                           		<input type="radio" name="address" class="crm1" value="${crm1.address}">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           		${crm1.address}
	                                           </td>
	                                            <c:if test="${index==3}">
	                                           		<td>
	                                           			<label>
		                                           			<input type="radio" name="address" class="crm2" value="${crm2.address}">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
	                                           			${crm2.address}
	                                           		</td>
	                                            </c:if>
	                                       </tr>
	                                       <tr>
	                                           <td class="title">客户备注&nbsp;&nbsp; </td>
	                                           <td style="word-break:break-all">
	                                           		<label>
			                                           	<input type="radio" name="customerRemark" class="crm0" value="${crm0.customerRemark}" checked="checked">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           			${crm0.customerRemark}
		                                         </td>
	                                           <td style="word-break:break-all">
	                                           		<label>
		                                           	 	<input type="radio" name="customerRemark" class="crm1" value="${crm1.customerRemark}">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
		                                           			${crm1.customerRemark}
	                                           	</td>
	                                            <c:if test="${index==3}">
	                                           		<td style="word-break:break-all">
	                                           			<label>
	                                           			 	<input type="radio" name="customerRemark" class="crm2" value="${crm2.customerRemark}">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
		                                           		${crm2.customerRemark}
	                                           		</td>
	                                            </c:if>
	                                       </tr>
	                                </table>
	                                <div class="discripe">
										<p>
											注：合并后，客户信息只保留选中项，若没有指定合并信息，则默认保留选中的客户名称对应的信息。 参与合并的客户信息，最终只会保留选中的客户。<br/>
											客户的联系人、反馈追踪、文档以及共享情况会自动合并到选中的合并对象中。
										</p>
	                                </div>
	                            </div>
					</div>
				</div>
	</section>
	<!--main content end-->
	</form>

</div>
</body>
</html>
