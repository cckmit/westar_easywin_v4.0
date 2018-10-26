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
                    <c:when test="${empty loan.instanceId || loan.instanceId == 0}">
                        <div class="content-item wrapper1-head">
                            <h2 style="margin-top: -10px;">申请进度</h2>
                            <div class="step-wrapper" style="width:680px;margin-left: 5px;">
                                <div class="item-be ${loan.loanApplyState == 0 ? 'step-done' : ''}">申请</div>
                                <div class="item-line"></div>
                                <div class="step-item ${loan.loanApplyState == 1 ? 'step-done' : ''}" id="applySteping">过程审核</div>
                                <div class="item-line"></div>
                                <div class="item-be ${loan.loanApplyState == 4 ? 'step-done' : ''}" id="applyEnd">完结</div>
                            </div>
                        </div>
                        <div class="content-item">
                            <h2>申请详情</h2>
                            <div class="content-item-details">
                                <div class="item">
                                    <div>
	                                    <span class="title">申请名称：</span>
	                                    <span class="desc">
	                                    	<a href="javascript:void(0)" data-insId="${loan.loanApplyInsId}">
	                                    		${loan.loanApplyInsName}
	                                    	</a>
	                                    </span>
	                                </div>
                                    <div>
                                        <span class="title">状态：</span>
                                        <span class="status">
                                                    <c:if test="${loan.loanApplyState == 0}">
                                                      	  待申请
                                                    </c:if>
                                                    <c:if test="${loan.loanApplyState == 1}">
                                                   	     审核中
                                                    </c:if>
                                                    <c:if test="${loan.loanApplyState == 4}">
                                                     	  完结
                                                    </c:if>
                                                    <c:if test="${loan.loanApplyState == -1}">
                                                        <span style="color:red;font-weight:bold;">驳回</span>
                                                    </c:if>
                                                </span>
                                    </div>
                                </div>
                                <div class="item">
                                    <div>
                                        <span class="title">申请人：</span>
                                        <span class="desc">${loan.creatorName}</span>
                                    </div>
                                    <div>
                                        <span class="title">申请时间：</span>
                                        <span class="desc">${loan.recordCreateTime}</span>
                                    </div>
                                </div>
                                <div class="item">
                                    <div>
	                                    <span class="title">开始日期：</span>
	                                    <span class="desc">${loan.loanApplyStartDate}</span>
                                    </div>
                                     <div>
	                                    <span class="title">结束日期：</span>
	                                    <span class="desc">${loan.loanApplyEndDate}</span>
	                                </div>
                                </div>
                                <div class="item">
                                     <span class="title">单号：</span>
                                     <span class="desc">${loan.flowSerialNumber}</span>
                                 </div>
                                
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="content-item wrapper1-head">
                            <h2 style="margin-top: -10px;">借款进度</h2>
                            <div class="step-wrapper" style="width:680px;margin-left: 5px;">
                                <div class="item-be ${loan.status == 0 ? 'step-done' : ''}">借款</div>
                                <div class="item-line"></div>
                                <div class="step-item ${loan.status == 1 ? 'step-done' : ''}" id="steping">过程审核</div>
                                <div class="item-line"></div>
                                <div class="step-item ${loan.balanceState != 1 && loan.status == 4 ? 'step-done' : ''}" id="balancing">财务结算</div>
                                <div class="item-line"></div>
                                <div class="item-be ${(loan.status == 4 && loan.balanceState == 1) ? 'step-done' : ''}" id="end">完结</div>
                            </div>
                        </div>
                        <div class="content-item">
                            <h2>借款详情</h2>
                            <div class="content-item-details">
                           	 <div class="item">
	                           	 <div>
	                           	 	<span class="title">汇报名称：</span>
                                    <span class="desc">
                                    	<a href="javascript:void(0)" data-insId="${loan.instanceId}">${loan.flowName}</a>
                                    </span>
	                           	 </div>
                                  <div>
                                      <span class="title">状态：</span>
                                      <span class="status">
                                           <c:if test="${loan.status == 0}">
                                              	 待汇报
                                           </c:if>
                                           <c:if test="${loan.status == 1}">
                                               	审核中
                                           </c:if>
                                           <c:if test="${loan.status == 4}">
                                              	 完结
                                           </c:if>
                                           <c:if test="${loan.status == -1}">
                                               <span style="color:red;font-weight:bold;">驳回</span>
                                           </c:if>
                                     </span>
                                    </div>
                                </div>
                                <div class="item">
                                    <div>
                                        <span class="title">申请人：</span>
                                        <span class="desc">${loan.creatorName}</span>
                                    </div>
                                    <div>
                                        <span class="title">申请时间：</span>
                                        <span class="desc">${loan.recordCreateTime}</span>
                                    </div>
                                </div>
                                
                                <div class="item">
	                                <div>
	                                    <span class="title">借款金额：</span>
	                                    <span class="desc">${loan.borrowingBalance}元</span>
                                    </div>
                                     <div>
	                                    <span class="title">剩余额度：</span>
	                                    <span class="desc">${loan.allowedQuota}元</span>
	                                </div>
                                </div>
                               <div class="item">
                                    <div>
                                        <span class="title">单号：</span>
                                        <span class="desc">${loan.flowSerialNumber}</span>
                                    </div>
                                    
                                </div>
                                
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
                <div class="content-item" style="display:${loan.balanceState == 1?'block':'none'} ">
                    <h2>结算详情</h2>
                    <div class="content-item-details">
                        <div class="item">
                            <div>
                                <span class="title">借款金额：</span>
                                <span class="desc">
                                        ${loan.borrowingBalance}元
                                </span>
                            </div>
                            <div>
                                <span class="title">支付方式：</span>
                                <span class="desc">
                                        ${loan.payType==0?'现金':'银联转账'}
                                </span>
                            </div>
                        </div>
                        <div class="item">
                            <div>
                                <span class="title">出款人：</span>
                                <span class="desc">
                                    	${loan.balanceUserName}
                                </span>
                            </div>
                             <div>
                                <span class="title">出款时间：</span>
                                <span class="desc">
                                    ${loan.balanceTime}
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="content-item">
                    <h2>${empty loan.instanceId || loan.instanceId <= 0 ? '出差消费记录' : '历史消费记录'}</h2>
                    <div class="details-list">
                        <c:choose>
                            <c:when test="${!empty feeLoans}">
                                <table style="float:left;width: 100%;text-align: center;" border="0" cellpadding="0" cellspacing="0">
                                    <thead>
                                    <tr class="consume-tr tr2">
                                        <th class="consume-td" style="width:10%">序号</th>
                                        <th class="consume-td">名称</th>
                                        <th class="consume-td" style="width:10%">借款金额</th>
                                        <th class="consume-td" style="width:30%">申请日期</th>
                                        <th class="consume-td" style="width:12%">申请状态</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${feeLoans}" var="feeloan" varStatus="status">
                                        <c:choose>
                                            <c:when test="${status.count % 2 == 1}">
                                                <tr class="consume-tr tr1">
                                                    <td class="consume-td">${status.count}</td>
                                                    <td class="consume-td">${feeloan.flowName}</td>
                                                    <td class="consume-td">${feeloan.borrowingBalance}元</td>
                                                    <td class="consume-td">${feeloan.recordCreateTime}<br/>${consume.endDate}</td>
                                                    <td class="consume-td">
                                                        <c:if test="${loan.status == 0}">
                                                            	待审核
                                                        </c:if>
                                                        <c:if test="${loan.status == 1}">
                                                           	 审核中
                                                        </c:if>
                                                        <c:if test="${loan.status == 4}">
                                                            	通过
                                                        </c:if>
                                                        <c:if test="${loan.status == -1}">
                                                            <span style="color:red;font-weight:bold;">驳回</span>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <tr class="consume-tr tr2">
                                                    <td class="consume-td">${status.count}</td>
                                                    <td class="consume-td">${feeloan.flowName}</td>
                                                    <td class="consume-td">${feeloan.borrowingBalance}元</td>
                                                    <td class="consume-td">${feeloan.recordCreateTime}<br/>${consume.endDate}</td>
                                                    <td class="consume-td">
                                                        <c:if test="${loan.status == 0}">
                                                            	待审核
                                                        </c:if>
                                                        <c:if test="${loan.status == 1}">
                                                            	审核中
                                                        </c:if>
                                                        <c:if test="${loan.status == 4}">
                                                            	通过
                                                        </c:if>
                                                        <c:if test="${loan.status == -1}">
                                                            <span style="color:red;font-weight:bold;">驳回</span>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:otherwise>
                                        </c:choose>
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
                <div class="content-item" style="display:${loan.isBusinessTrip == 1?'block':'none'}">
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
            </div>
        </div>
    </div>
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
<script type="text/javascript">
$(function(){
	
	//设置进度样式
	if(${empty loan.instanceId || loan.instanceId == 0}){
		if(${loan.loanApplyState == 0}){
			$("#applySteping").css({border:"1px solid grey",color:"grey"});
			$("#applyEnd").css({border:"1px solid grey",color:"grey"});
		}else if(${loan.loanApplyState == 1}){
			$("#applyEnd").css({border:"1px solid grey",color:"grey"});
		}
	}else{
		if(${loan.status == 0}){
			$("#steping").css({border:"1px solid grey",color:"grey"});
			$("#balancing").css({border:"1px solid grey",color:"grey"});
			$("#end").css({border:"1px solid grey",color:"grey"});
		}else if(${loan.status == 1}){
			$("#balancing").css({border:"1px solid grey",color:"grey"});
			$("#end").css({border:"1px solid grey",color:"grey"});
		}else if(${loan.balanceState != 1 && loan.status == 4}){
			$("#end").css({border:"1px solid grey",color:"grey"});
		} 
	}
	
	$("body").on("click","a[data-insId]",function(){
		var instanceId = $(this).attr("data-insId");
		var url = "/workFlow/viewSpFlow?sid=${param.sid}&instanceId="+instanceId;
		
		parent.openWinWithPams(url, "800px", ($(window.parent).height() - 90) + "px");
		
	});
	var instanceId = "${loan.instanceId}";
	var loanApplyInsId = "${loan.loanApplyInsId}";
	var pTitle = "";
	if(instanceId){
		pTitle = "${loan.flowName}";
	}else{
		pTitle = "${loan.loanApplyInsName}";
	}
	$("body",parent.document).find("#pTitle").html(pTitle);
})
</script>
</body>
</html>
