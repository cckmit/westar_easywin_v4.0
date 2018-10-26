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
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>报销详情</title>
    <link rel="stylesheet" href="http://at.alicdn.com/t/font_709996_4ox3ph4m8ck.css">
    <link rel="stylesheet" href="/static/css/financial.css">
    <script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
	<script type="text/javascript" src="/static/js/common.js"></script>
    <!--[if lt IE 9]>
    <style>
        /*针对IE8下不支持美化，则隐藏，采用原生的多选框*/
        .check_box label,.radio_box label{display: none}
    </style>
    <![endif]-->
</head>
<body onload="resizeVoteH('${param.ifreamName}')">
<div id="tab1" class="tab-pane in active">
    <div class="panel-body no-padding">
        <div class="wrapper1">
            <div class="content-wrapper1">
                <c:choose>
                    <c:when test="${feeBudget.isBusinessTrip eq 1}">
                        <div class="content-item">
                            <h2>出差计划</h2>
                            <div class="content-item-details">
                                <div class="item">
                                    <div>
                                        <span class="title">
                                           <span>出差申请：</span> 
                                        </span>
                                        <span class="desc">
                                            <a href="javascript:void(0)" data-insId="${feeBudget.instanceId}">${feeBudget.flowName}</a>
                                        </span>
                                    </div>
                                </div>
                                <div class="item">
                                    <div>
                                        <span class="title">出差地点：</span>
                                        <span class="desc">
                                                ${feeBudget.tripPlace}
                                        </span>
                                    </div>
                                    <div>
                                        <span class="title">出差时间：</span>
                                        <span class="desc">
                                                        ${feeBudget.startTime}~${feeBudget.endTime}
                                                </span>
                                    </div>
                                </div>
                                <div class="item">
                                    <div>
                                        <span class="title">借款限额：</span>
                                        <span class="desc">
                                                    ${feeBudget.allowedQuota}元
                                                </span>
                                    </div>
                                    <div>
                                        <span class="title">累计借款：</span>
                                        <span class="desc">
                                                    ${feeBudget.loanFeeTotal}元
                                                </span>
                                    </div>
                                </div>
                                <div class="item">
                                    <div>
                                        <span class="title">累计销帐：</span>
                                        <span class="desc">
                                                    ${feeBudget.loanOffTotal}元
                                                </span>
                                    </div>
                                    <div>
                                        <span class="title">累计报销：</span>
                                        <span class="desc">
                                                    ${feeBudget.loanItemTotal}元
                                                </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                    	<div class="content-item">
                            <h2>借款说明</h2>
                            <div class="content-item-details">
                                <div class="item">
                                    <div>
                                        <span class="title">
                                           <span>借款说明：</span> 
                                        </span>
                                        <span class="desc">
                                            <a href="javascript:void(0)" data-insId="${feeBudget.instanceId}">${feeBudget.flowName}</a>
                                        </span>
                                    </div>
                                    <div>
                                        <span class="title">计划时间：</span>
                                        <span class="desc">
                                                        ${feeBudget.startTime}~${feeBudget.endTime}
                                                </span>
                                    </div>
                                </div>
                                <div class="item">
                                    <div>
                                        <span class="title">借款限额：</span>
                                        <span class="desc">
                                                    ${feeBudget.allowedQuota}元
                                                </span>
                                    </div>
                                    <div>
                                        <span class="title">累计借款：</span>
                                        <span class="desc">
                                                    ${feeBudget.loanFeeTotal}元
                                                </span>
                                    </div>
                                </div>
                                <div class="item">
                                    <div>
                                        <span class="title">累计销帐：</span>
                                        <span class="desc">
                                                    ${feeBudget.loanOffTotal}元
                                                </span>
                                    </div>
                                    <div>
                                        <span class="title">累计报销：</span>
                                        <span class="desc">
                                                    ${feeBudget.loanItemTotal}元
                                                </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
                
                
                <div class="content-item">
                    <h2>借款记录</h2>
                    <div class="details-list">
                        <c:choose>
                            <c:when test="${not empty feeBudget.listFeeLoan}">
                                <table style="float:left;width: 100%;text-align: center;" border="0" cellpadding="0" cellspacing="0">
                                    <thead>
                                    <tr class="consume-tr tr2">
                                        <th class="consume-td">序号</th>
                                        <th class="consume-td">借款申请</th>
                                        <th class="consume-td">借款日期</th>
                                        <th class="consume-td">审核状态</th>
                                        <th class="consume-td">借款金额</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${feeBudget.listFeeLoan}" var="loan" varStatus="status">
                                            <tr class="consume-tr ${status.count % 2 == 1 ? 'tr1':'tr2'} ">
                                                <td class="consume-td">${status.count}</td>
                                                <td class="consume-td">
                                                	<a href="javascript:void(0)" data-insId="${loan.instanceId }">${loan.flowName }</a>
                                                </td>
                                                <td class="consume-td">${loan.recordCreateTime }</td>
                                                <td class="consume-td">
                                                	<c:choose>
                                                		<c:when test="${loan.status eq 4}">
                                                			通过
                                                		</c:when>
                                                		<c:when test="${loan.status eq -1 }">
                                                			失败
                                                		</c:when>
                                                		<c:otherwise>
                                                		审核中
                                                		</c:otherwise>
                                                	</c:choose>
                                                </td>
                                                <td class="consume-td">${loan.borrowingBalance }</td>
                                            </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <div class="list-item list-head"> 未查询到数据！</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
                <div class="content-item">
                    <h2>报销记录</h2>
                    <div class="details-list">
                        <c:choose>
                            <c:when test="${not empty feeBudget.listLoanOff}">
                                <table style="float:left;width: 100%;text-align: center;" border="0" cellpadding="0" cellspacing="0">
                                    <thead>
                                    <tr class="consume-tr tr2">
                                        <th class="consume-td">序号</th>
                                        <th class="consume-td">汇报流程</th>
                                        <th class="consume-td">报销流程</th>
                                        <th class="consume-td">报销时间</th>
                                        <th class="consume-td">报销状态</th>
                                        <th class="consume-td">报销金额</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${feeBudget.listLoanOff}" var="loanOff" varStatus="status">
                                            <tr class="consume-tr ${status.count % 2 == 1 ? 'tr1':'tr2'} ">
                                                <td class="consume-td">${status.count}</td>
                                                <td class="consume-td">
                           	                    	<a href="javascript:void(0)" data-insId="${loanOff.loanRepInsId }">${loanOff.loanReportName }</a>
                                                </td>
                                                <td class="consume-td">
                                                	<c:choose>
                                                		<c:when test="${not empty loanOff.loanOffName }">
                                                			<a href="javascript:void(0)" data-insId="${loanOff.instanceId }">${loanOff.loanOffName }</a>
                                                		</c:when>
                                                	</c:choose>
                                                </td>
                                                <td class="consume-td">
                                                <c:choose>
                                                	<c:when test="${not empty loanOff.recordCreateTime }">
                                                		${loanOff.recordCreateTime }
                                                	</c:when>
                                                	<c:otherwise>
                                                		${loanOff.loanRepDate }
                                                	</c:otherwise>
                                                </c:choose>
                                                </td>
                                                
                                                <td class="consume-td">
                                                	<c:choose>
                                                		<c:when test="${loanOff.status eq 4}">
                                                			通过
                                                		</c:when>
                                                		<c:when test="${loanOff.status eq -1 }">
                                                			失败
                                                		</c:when>
                                                		<c:when test="${loanOff.status eq 1 }">
                                                			审核中
                                                		</c:when>
                                                		<c:otherwise>
                                                			<c:choose>
		                                                		<c:when test="${loanOff.loanRepStatus eq 4}">
		                                                			待提交
		                                                		</c:when>
		                                                		<c:when test="${loanOff.loanRepStatus eq -1 }">
		                                                			失败
		                                                		</c:when>
		                                                		<c:when test="${loanOff.loanRepStatus eq 1 }">
		                                                			审核中
		                                                		</c:when>
		                                                		<c:otherwise>
		                                                			草稿
		                                                		</c:otherwise>
		                                                	</c:choose>
                                                		</c:otherwise>
                                                	</c:choose>
                                                </td>
                                                <td class="consume-td">${loanOff.loanOffBalance }</td>
                                            </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <div class="list-item list-head"> 未查询到数据！</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
                <div class="content-item" style="display:${not empty feeBudget.directBalanceUserName?'block':'none'} ">
                    <h2>销账记录</h2>
                    <div class="content-item-details">
                        <div class="item">
                            <div>
                                <span class="title">销账人：</span>
                                <span class="desc">
                                        ${feeBudget.directBalanceUserName}
                                </span>
                            </div>
                            <div>
                                <span class="title">销账时间：</span>
                                <span class="desc">
                                        ${feeBudget.directBalanceTime}
                                </span>
                            </div>
                        </div>
                        <div class="item">
                            <div style="width: 100%">
                                <span class="title">
                                    	销账说明：
                                </span>
                                <span class="desc">
                                    <tags:viewTextArea>${feeBudget.content}</tags:viewTextArea>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
                
            </div>
        </div>
    </div>
</div>


<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
<script type="text/javascript">
$(function(){

	$("body").on("click","a[data-insId]",function(){
		var instanceId = $(this).attr("data-insId");
		//if(instanceId != '${param.instanceId }'){
		//}
		var url = "/workFlow/viewSpFlow?sid=${param.sid}&instanceId="+instanceId;

		parent.openWinWithPams(url, "900px", ($(window.parent).height() - 90) + "px");
	});
	var instanceId = "${loanOffAccount.instanceId}";
	var loanRepInsId = "${loanReport.loanRepInsId}";
	var pTitle = "";
	if(instanceId){
		pTitle = "${loanOffAccount.loanOffName}";
	}else{
		pTitle = "${loanReport.loanReportName}";
	}
	$("body",parent.document).find("#pTitle").html(pTitle);
})
</script>
</body>
</html>
