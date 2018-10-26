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
$(function() {
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError : true
	});
});
function chooseAll(cls){
	$("."+cls).click()
}
function selectOne(){
	var len0 = $(".item0").length;
	var len1 = $(".item1").length;
	var len2 = $(".item2").length;
	if(len0>0){
		$(".item0").click();
	}else if(len1>0){
		$(".item1").click();
	}else if(len2>0){
		$(".item2").click();
	}
	
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
<body onload="selectOne()">

<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	<span class="widget-caption themeprimary ps-layerTitle">项目合并</span>
    <div class="widget-buttons">
		<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
			<i class="fa fa-times themeprimary"></i>
		</a>
	</div>
</div>
<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">

	<section id="container">
	
	<form id="subform" class="subform" method="post" action="/item/itemCompress">
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
							合并项目信息(<font color="red">项目合并后将不能还原</font>)
						</h6>
						</div>
							<div class="panel-body">
	                                <table class="table  table-bordered general-table">
	                                       <tr>
	                                           <td class="title" style="width: 19%;">项目名称 &nbsp;&nbsp;</td>
	                                           <td style="width: 27%">
	                                           	${item0.itemName}
	                                           	<c:choose>
	                                           		<c:when test="${itemComPress0 ==0}">(子项目)</c:when>
	                                           		<c:otherwise>
			                                           	<a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseAll('item0')">[全选]</a>
	                                           		</c:otherwise>
	                                           	</c:choose>
	                                           </td>
	                                           <td style="width: 27%">
	                                           	${item1.itemName }
	                                           		<c:choose>
		                                           		<c:when test="${itemComPress1 ==0}">(子项目)</c:when>
		                                           		<c:otherwise>
				                                           	<a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseAll('item1')">[全选]</a>
		                                           		</c:otherwise>
		                                           	</c:choose>
	                                           </td>
	                                           <c:if test="${index==3}">
	                                           	<td style="width:27%">
	                                           		${item2.itemName }
	                                           		<c:choose>
		                                           		<c:when test="${itemComPress1 ==0}">(子项目)</c:when>
		                                           		<c:otherwise>
				                                           	<a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseAll('item2')">[全选]</a>
		                                           		</c:otherwise>
		                                           	</c:choose>
	                                           	</td>
	                                           </c:if>
	                                       </tr>
	                                       <tr>
	                                           <td class="title" style="width: 19%;">合并对象 <font color="red">*</font></td>
	                                           <td style="width: 27%">
	                                           	<c:if test="${itemComPress0 ==1}">
	                                           		<label>
			                                           	<input type="radio" name="id" class="item0" value="${item0.id}">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           	</c:if>
	                                           </td>
	                                           <td style="width: 27%">
	                                           	<c:if test="${itemComPress1 ==1}">
	                                           		<label>
			                                           	<input type="radio" name="id" class="item1" value="${item1.id }">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           	</c:if>
	                                           </td>
	                                           <c:if test="${index==3}">
	                                           	<td style="width:27%">
	                                         		<c:if test="${itemComPress2 ==1}">
		                                         		<label>
			                                           		<input type="radio" name="id" class="item2" value="${item2.id }">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
	                                         		</c:if>
	                                           	</td>
	                                           </c:if>
	                                       </tr>
	                                       <tr>
	                                           <td class="title">项目状态&nbsp;&nbsp;</td>
	                                           <td>	
		                                           <c:if test="${itemComPress0 ==1}">
			                                           <label>
				                                           	<input type="radio" name="state" class="item0" value="${item0.state}">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
		                                           	
		                                           </c:if>
		                                           	<c:set var="item0State">
														<c:choose>
															<c:when test="${item0.state==1}">进行中</c:when>
															<c:when test="${item0.state==3}">挂起中</c:when>
															<c:when test="${item0.state==4}">已办结</c:when>
															<c:otherwise>
																正常
															</c:otherwise>
														</c:choose>
													</c:set>
		                                           	${item0State} 
		                                       </td>
	                                           <td>
	                                           		<c:if test="${itemComPress1 ==1}">
		                                           		<label>
			                                           		<input type="radio" name="state" class="item1" value="${item1.state}">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
	                                           		</c:if>
	                                           			<c:set var="item1State">
														<c:choose>
															<c:when test="${item1.state==1}">进行中</c:when>
															<c:when test="${item1.state==3}">挂起中</c:when>
															<c:when test="${item1.state==4}">已办结</c:when>
															<c:otherwise>
																正常
															</c:otherwise>
														</c:choose>
													</c:set>
		                                           	${item1State} 
	                                           </td>
	                                           <c:if test="${index==3}">
	                                           		<td>
	                                         			<c:if test="${itemComPress2 ==1}">
	                                         				<label>
			                                           			<input type="radio" name="state" class="item2" value="${item2.state}">
					                                           	<span class="text">&nbsp;</span>
				                                           	</label>
	                                         			</c:if>
	                                           			<c:set var="item2State">
														<c:choose>
															<c:when test="${item2.state==1}">进行中</c:when>
															<c:when test="${item2.state==3}">挂起中</c:when>
															<c:when test="${item2.state==4}">已办结</c:when>
															<c:otherwise>
																正常
															</c:otherwise>
														</c:choose>
													</c:set>
		                                           	${item2State} 
	                                           		</td>
	                                           </c:if>
	                                           
	                                       </tr>
	                                       <tr>
	                                           <td class="title">项目来源 &nbsp;</td>
	                                           <td>
	                                           		<c:if test="${itemComPress0 ==1}">
		                                           		<label>
				                                           	<input type="radio" name="partnerId" class="item0" value="${item0.partnerId }">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
	                                           		</c:if>
		                                           	${item0.partnerName} 
		                                       </td>
	                                           <td>
	                                           		<c:if test="${itemComPress1 ==1}">
		                                           		<label>
			                                           		<input type="radio" name="partnerId" class="item1" value="${item1.partnerId }">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
	                                           		</c:if>
	                                           		${item1.partnerName }
	                                           </td>
	                                           <c:if test="${index==3}">
	                                           		<td>
	                                           				<c:if test="${itemComPress2 ==1}">
		                                           				<label>
				                                           			<input type="radio" name="partnerId" class="item2" value="${item2.partnerId }">
						                                           	<span class="text">&nbsp;</span>
					                                           	</label>
	                                           				</c:if>
	                                           			${item2.partnerName }
	                                           		</td>
	                                           </c:if>
	                                           
	                                       </tr>
	                                       <tr>
	                                           <td class="title">上级项目&nbsp;&nbsp;</td>
	                                           <td>
	                                           	<c:if test="${itemComPress0 ==1}">
	                                           	
	                                           		<label>
			                                           	<input type="radio" name="parentId" class="item0" value="${item0.parentId }">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           	</c:if>
		                                           	${item0.pItemName} 
		                                       </td>
	                                           <td>
	                                           	<c:if test="${itemComPress1 ==1}">
	                                           		<label>
		                                           		<input type="radio" name="parentId" class="item1" value="${item1.parentId }">
			                                           	<span class="text">&nbsp;</span>
		                                           	</label>
	                                           	</c:if>
	                                           		${item1.pItemName }
	                                           </td>
	                                           <c:if test="${index==3}">
	                                           		<td>
                                           				<c:if test="${itemComPress2 ==1}">
	                                           				<label>
			                                           			<input type="radio" name="parentId" class="item2" value="${item2.parentId }">
					                                           	<span class="text">&nbsp;</span>
				                                           	</label>
                                           				</c:if>
	                                           			${item2.pItemName }
	                                           		</td>
	                                           </c:if>
	                                           
	                                       </tr>
	                                       <tr>
	                                           <td class="title">项目简介&nbsp;&nbsp;</td>
	                                           <td style="word-break:break-all">
	                                           		<c:if test="${itemComPress0 ==1}">
		                                           		<label>
				                                           	<input type="radio" name="itemRemark" class="item0" value="${item0.itemRemark}">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
	                                           		</c:if>
	                                           			${item0.itemRemark}
		                                         </td>
	                                           <td style="word-break:break-all">
	                                           		<c:if test="${itemComPress1 ==1}">
		                                           		<label>
			                                           	 	<input type="radio" name="itemRemark" class="item1" value="${item1.itemRemark}">
				                                           	<span class="text">&nbsp;</span>
			                                           	</label>
	                                           		</c:if>
		                                           			${item1.itemRemark}
	                                           	</td>
	                                            <c:if test="${index==3}">
	                                           		<td style="word-break:break-all">
	                                           			<c:if test="${itemComPress2 ==1}">
		                                           			<label>
				                                           		<input type="radio" name="itemRemark" class="item2" value="${item2.itemRemark}">
					                                           	<span class="text">&nbsp;</span>
				                                           	</label>
		                                           			</span>
	                                           			</c:if>
		                                           		${item2.itemRemark}
	                                           		</td>
	                                            </c:if>
	                                       </tr>
	                                </table>
	                              	  <div class="discripe">
											<p>
												注：合并后，项目信息只保留选中项，若没有指定合并信息，则默认保留选中的项目名称对应的信息。 参与合并的项目信息，最终只会保留选中的项目。<br/>
												项目的阶段信息、项目留言、文档以及共享情况会自动合并到选中的合并对象中。
											</p>
	                               	 </div>
	                            </div>
					</div>
				</div>
	</form>
	</section>
	<!--main content end-->
</div>
</body>
</html>
