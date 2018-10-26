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
    <script type="text/javascript">
        var sid="${param.sid}";//sid全局变量
        var EASYWIN = {
                "userInfo":{
                    "id":"${userInfo.id}",
                    "name":"${userInfo.userName}"
                },
                "sid":"${param.sid}"
            }
    </script>
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
                <div class="content-item wrapper1-head">
                    <h2 style="margin-top: -10px;">报销进度</h2>
                    <div class="step-wrapper" style="width:95%;margin-left: 5px;">
                        <div class="item-be">汇报</div>
                        <div class="item-line"></div>
                        <c:choose>
                            <c:when test="${loanOffAccount.isBusinessTrip == 1}">
                                <div class="step-item ${(loanOffAccount.status == 4 && loanReport.loanRepStatus == 4 ) ? '' : 'step-done'}">过程审核</div>
                                <div class="item-line"></div>
                                <div class="step-item ${loanOffAccount.balanceState != 1 && loanReport.loanRepStatus == 4 && loanOffAccount.status == 4 ? 'step-done' : ''}" id="balancing">财务结算</div>
                                <div class="item-line"></div>
                                <div class="item-be ${(loanOffAccount.status == 4 && loanReport.loanRepStatus == 4 && loanOffAccount.balanceState == 1) ? 'step-done' : ''}" id="end">完结</div>
                            </c:when>
                            <c:otherwise>
                                <div class="step-item ${(loanOffAccount.status == 4) ? '' : 'step-done'}">过程审核</div>
                                <div class="item-line"></div>
                                <div class="step-item ${loanOffAccount.balanceState != 1 && loanOffAccount.status == 4 ? 'step-done' : ''}" id="balancing">财务结算</div>
                                <div class="item-line"></div>
                                <div class="item-be ${(loanOffAccount.status == 4 && loanOffAccount.balanceState == 1) ? 'step-done' : ''}" id="end">完结</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <c:choose>
                    <c:when test="${!empty businessApply}">
                        <div class="content-item">
                            <h2>报销依据</h2>
                            <div class="content-item-details">
                                <div class="item">
                                    <div>
                                        <span class="title">
                                          	  出差申请：
                                        </span>
                                        <span class="desc">
                                            <a href="javascript:void(0)" data-insId="${businessApply.instanceId}">${businessApply.flowName}</a>
                                        </span>
                                    </div>
                                </div>
                                <div class="item">
                                    <div>
                                        <span class="title">出差地点：</span>
                                        <span class="desc">
                                                ${businessApply.tripPlace}
                                        </span>
                                    </div>
                                    <div>
                                        <span class="title">出差时间：</span>
                                        <span class="desc">
                                                        ${businessApply.startTime}~${businessApply.endTime}
                                                </span>
                                    </div>
                                </div>
                                <div class="item">
                                    <div>
                                        <span class="title">借款限额：</span>
                                        <span class="desc">
                                                ${businessApply.allowedQuota}元
                                        </span>
                                    </div>
                                    <div>
                                        <span class="title">累计借款：</span>
                                        <span class="desc">
                                                    ${businessApply.loanFeeTotal}元
                                                </span>
                                    </div>
                                </div>
                                <div class="item">
                                    <div>
                                        <span class="title">累计销帐：</span>
                                        <span class="desc">
                                                    ${businessApply.loanOffTotal}元
                                                </span>
                                    </div>
                                    <div>
                                        <span class="title">累计报销：</span>
                                        <span class="desc">
                                                    ${businessApply.loanItemTotal}元
                                                </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${!empty loanApply && loanApply.instanceId != 0}">
                        <div class="content-item">
                            <h2>报销依据</h2>
                            <div class="content-item-details">
                                <div class="item">
                                    <div>
                                        <span class="title">
                                            	借款申请：
                                        </span>
                                        <span class="desc">
                                            <c:choose>
                                                <c:when test="${loanApply.instanceId == 0}">
                                                    <a>直接报销无借款申请</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="javascript:void(0)" data-insId="${loanApply.instanceId}">${loanApply.flowName}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                </div>
                                <div class="item">
                                    <div>
                                        <span class="title">借款限额：</span>
                                        <span class="desc">
                                                    ${empty loanApply.allowedQuota ? 0 : loanApply.allowedQuota}元
                                                </span>
                                    </div>
                                    <div>
                                        <span class="title">累积借款：</span>
                                        <span class="desc">
                                                    ${loanApply.loanFeeTotal}元
                                                </span>
                                    </div>
                                </div>
                                <div class="item">
                                    <div>
                                        <span class="title">累积销帐：</span>
                                        <span class="desc">
                                                    ${loanApply.loanOffTotal}元
                                                </span>
                                    </div>
                                    <div>
                                        <span class="title">累积报销：</span>
                                        <span class="desc">
                                                    ${loanApply.loanItemTotal}元
                                                </span>
                                    </div>
                                </div>
                            </div>
                        </div> 
                    </c:when>
                </c:choose>
                <div class="content-item">
                    <h2>过程审核</h2>
                    <div class="content-item-details">
                        <c:choose>
                            <c:when test="${loanReport.loanRepInsId != null && loanReport.loanRepInsId != 0}">
                                <div class="item">
                                    <div>
                                        <span class="title">第一步：</span>
                                        <span class="desc">
                                            <a href="javascript:void(0)" data-insId="${loanReport.loanRepInsId}">${loanReport.loanReportName}</a>
                                        </span>
                                    </div>
                                    <div>
                                        <span class="title"></span>
                                        <span class="status">
                                                <c:if test="${loanReport.loanRepStatus == 0}">
                                                    	待汇报
                                                </c:if>
                                                <c:if test="${loanReport.loanRepStatus == 1}">
                                                   	 审核中
                                                </c:if>
                                                <c:if test="${loanReport.loanRepStatus == 4}">
                                                    	完结
                                                </c:if>
                                                <c:if test="${loanReport.loanRepStatus == -1}">
                                                    <span style="color:red;">驳回</span>
                                                </c:if>
                                            </span>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${loanOffAccount.isBusinessTrip == 1}">
                                        <div class="item">
                                            <div>
                                                <span class="title" style="color:gray;">第一步：</span>
                                                <span class="desc" style="color:gray;">未提交成果汇报</span>
                                            </div>
                                        </div>
                                    </c:when>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${loanOffAccount.instanceId != null && loanOffAccount.instanceId != 0}">
                                <div class="item">
                                    <div>
                                        <span class="title"> ${loanOffAccount.isBusinessTrip == 1 || loanReport.loanWay eq '1' ? '第二步' : '报销申请'}：</span>
                                        <span class="desc">
                                            <a href="javascript:void(0)" data-insId="${loanOffAccount.instanceId}">${loanOffAccount.loanOffName}</a>
                                        </span>
                                    </div>
                                    <div>
                                        <span class="title"></span>
                                        <span class="status">
                                                <c:if test="${loanOffAccount.status == 0}">
                                                   	 待报销
                                                </c:if>
                                                <c:if test="${loanOffAccount.status == 1}">
                                                    	审核中
                                                </c:if>
                                                <c:if test="${loanOffAccount.status == 4}">
                                                   	完结
                                                </c:if>
                                                <c:if test="${loanOffAccount.status == -1}">
                                                    <span style="color:red;">驳回</span>
                                                </c:if>
                                            </span>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="item">
                                    <div>
                                        <span class="title" style="color:gray;">${loanOffAccount.isBusinessTrip == 1 || loanReport.loanWay eq '1' ? '第二步' : '报销申请'}：</span>
                                        <span class="desc" style="color:gray;">未提交报销单</span>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
                <div class="content-item" style="display:${loanOffAccount.balanceState == 1?'block':'none'} ">
                    <h2>结算详情</h2>
                    <div class="content-item-details">
                        <div class="item">
                            <div>
                                <span class="title">报销金额：</span>
                                <span class="desc">
                                        ${loanOffAccount.loanOffItemFee}元
                                </span>
                            </div>
                            <div>
                                <span class="title">销账金额：</span>
                                <span class="desc">
                                       ${loanOffAccount.loanOffBalance}元
                                </span>
                            </div>
                        </div>
                        <div class="item">
                            <div>
                                <span class="title">支付方式：</span>
                                <span class="desc">
                                        ${loanOffAccount.payType==0?'现金':'银联转账'}
                                </span>
                            </div>
                            <div>
                                <span class="title">出款人：</span>
                                <span class="desc">
                                    	${loanOffAccount.balanceUserName}
                                </span>
                            </div>
                        </div>
                        <div class="item">
                            <div>
                                <span class="title">出款时间：</span>
                                <span class="desc">
                                    ${loanOffAccount.balanceTime}
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
             
                <div class="content-item">
                    <h2>消费记录</h2>
                    <div class="details-list">
                        <c:choose>
                            <c:when test="${!empty consumes}">
                                <table style="float:left;width: 100%;text-align: center;" border="0" cellpadding="0" cellspacing="0">
                                    <thead>
                                    <tr class="consume-tr tr2">
                                        <th class="consume-td" style="width:10%">序号</th>
                                        <th class="consume-td" style="width:10%">类型</th>
                                        <th class="consume-td" style="width:10%">发票数量</th>
                                        <th class="consume-td">消费描述</th>
                                        <th class="consume-td" style="width:15%">消费日期</th>
                                        <th class="consume-td" style="width:12%">消费金额</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${consumes}" var="consume" varStatus="status">
                                        <tr class="consume-tr ${status.count % 2 == 1 ? 'tr1' : 'tr1'}">
                                            <td class="consume-td">${status.count}</td>
                                            <td class="consume-td">${empty consume.typeName ? '<span style="color:red;">未填写</span>' : consume.typeName}</td>
                                            <td class="consume-td">${empty consume.invoiceNum ? '<span style="color:red;">未填写</span>' : consume.invoiceNum}</td>
                                            <td class="consume-td">${empty consume.describe ? '<span style="color:red;">未填写</span>' : consume.describe}</td>
                                            <td class="consume-td">${empty consume.startDate && empty consume.endDate ? '<span style="color:red;">未填写</span>': consume.startDate.concat('<br/>').concat(consume.endDate)}</td>
                                            <td class="consume-td">${empty consume.amount ? '<span style="color:red;">未填写</span>' : consume.amount}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <div class="list-item list-head">
                                    	未查询到数据！
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="content-item" style="display:${loanOffAccount.isBusinessTrip == 1?'block':'none'} ">
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
                                            <td class="consume-td">${loanOff.recordCreateTime }</td>

                                            <td class="consume-td">
                                                <c:choose>
                                                    <c:when test="${loanOff.status eq 4}">
                                                        	通过
                                                    </c:when>
                                                    <c:when test="${loanOff.status eq -1 }">
                                                       	 失败
                                                    </c:when>
                                                    <c:otherwise>
                                                      	  审核中
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
            </div>
        </div>
    </div>
</div>


<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
<script type="text/javascript">
$(function(){
	
	//设置进度样式
	if(${loanOffAccount.isBusinessTrip == 1}){
		if(!${(loanOffAccount.status == 4 && loanReport.loanRepStatus == 4 )}){
			$("#balancing").css({border:"1px solid grey",color:"grey"});
			$("#end").css({border:"1px solid grey",color:"grey"});
		}else if(${loanOffAccount.balanceState != 1 && loanReport.loanRepStatus == 4 && loanOffAccount.status == 4}){
			$("#end").css({border:"1px solid grey",color:"grey"});
		} 
	}else{
		if(!${(loanOffAccount.status == 4)}){
			$("#balancing").css({border:"1px solid grey",color:"grey"});
			$("#end").css({border:"1px solid grey",color:"grey"});
		}else if(${loanOffAccount.balanceState != 1 && loanOffAccount.status == 4}){
			$("#end").css({border:"1px solid grey",color:"grey"});
		} 
	}
	
	$("body").on("click","a[data-insId]",function(){
		var instanceId = $(this).attr("data-insId");
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
