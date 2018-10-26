<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
        contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
        errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
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
    <title>个人报销</title>
    <!-- 框架样式 -->
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
	<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
    <link rel="stylesheet" href="/static/css/financial.css">
    <script type="text/javascript">
        var sid="${param.sid}";//sid全局变量
        var EASYWIN = {
            "userInfo":{
                "id":"${userInfo.id}",
                "name":"${userInfo.userName}"
            },
            "sid":"${param.sid}"
        }
        function allCheck(){
            //此次点击完成后的状态
            var checked = $("#check").is(":checked");
            if(checked){
            	 $.each($(".checkClass"),function(){
                     var dis = $(this).attr("id").split("_")[1];
                     if(dis == 1){
                         $(this).attr("checked","checked");
                     }
                 });
            	//隐藏查询条件
         		$(".searchCond").css("display","none");
         		//显示批量操作
         		$(".batchOpt").css("display","block");
            }else{
                $(".checkClass").attr("checked",false);
              	//显示查询条件
        		$(".searchCond").css("display","block");
        		//隐藏批量操作
        		$(".batchOpt").css("display","none");
            }
        }
        function batchWithdraw(){
            var ids = [];
            $.each($(".checkClass"),function(){
                var id = $(this).attr("id").split("_")[0];
                var dis = $(this).attr("id").split("_")[1];
                if($(this).is(":checked") && dis == 1){
                    ids.push(id);
                }
            })
            if(ids.length >= 1){
                addBusRemindBatch('022',ids);
            }else{
                window.top.layer.alert("请选择需要催办的记录！");
            }
        }
        function checkOrNO(id,dis){
            if(dis == 1){
                if($("#" + id + "_1").is(":checked")){
                    $("#" + id + "_1").attr("checked",false);
                  	//显示查询条件
            		$(".searchCond").css("display","block");
            		//隐藏批量操作
            		$(".batchOpt").css("display","none");
                }else{
                    $("#" + id + "_1").attr("checked","checked");
                  	//隐藏查询条件
             		$(".searchCond").css("display","none");
             		//显示批量操作
             		$(".batchOpt").css("display","block");
                }
            }else{
                window.top.layer.alert("该记录不符合催办条件！");
            }
        }
        $(function(){
        	queryCounts();
        })
      //显示各模块数量
        function queryCounts() {
        	$.ajax({
        		type : "post",
        		url : "/financial/personalApply/queryCounts?sid="+'${param.sid}',
        		dataType:"json",
        		traditional :true, 
        		data:null,
        		  beforeSend: function(XMLHttpRequest){
                 },
        		  success : function(data){
        			  if(data.status == "y"){
        				  if(data.countUrConsume > 0){
        					  $("#countUrConsume").text(data.countUrConsume);
        					  $("#countUrConsume").show();
        				  }
        				  if(data.countLoanOff > 0){
        					  $("#countLoanOff").text(data.countLoanOff);
        					  $("#countLoanOff").show();				  
        				  }
        				  if(data.countLoan > 0){
        					  $("#countLoan").text(data.countLoan);
        					  $("#countLoan").show();
        				  }
        			  }
        		  },
        		  error:  function(XMLHttpRequest, textStatus, errorThrown){
        			  window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
        	      }
        	}); 
        }

    </script>

    <style type="text/css">
		input[type=radio]+.text,input[type=checkbox]+.text{
			cursor: pointer;
		}
		input[type=checkbox].colored-blue:checked+.text:before, input[type=radio].colored-blue:checked+.text:before {
			border-color:#1890ff;
			color:#fff
		}
		input[type=checkbox]:checked+.text:before, input[type=radio]:checked+.text:before {
		  content: '✔';
		  width: 14px;
		  height: 14px;
		  line-height: 14px;
		  text-align: center;
		  color: #fff;
		  background-color: #1890ff;
		}
	</style>
</head>
<body>
<div class="content-wrapper" style="min-height: 450px;">
    <div class="content-head">
        <div class="tab-nav">
            <div class="item" id="loanApply" onclick="parent.listMyLoanOffPage()">报销申请
               <div class="num" style="display: none" id="countLoanOff"></div>
            </div>
            <div class="item item-active" onclick="parent.listMyLoanPage()">借款申请
                <div class="num" style="display: none" id="countLoan"></div>
            </div>
            <div class="item" onclick="parent.listMyConsumePage()">待销消费记录
                <div class="num" style="display: none" id="countUrConsume"></div>
            </div>
        </div>
        <div class="tab-button-wrapper">
            <button type="button" onclick="parent.addHeadBus('029','${param.sid}');">我要出差</button>
            <button type="button" onclick="parent.addHeadBus('030','${param.sid}');">我要借款</button>
            <button type="button" onclick="parent.addHeadBus('031','${param.sid}');">我要报销</button>
        	<button type="button" onclick="parent.addConsume();">添加消费记录</button>
        </div>
    </div>
    <div>
	    <div class="checkbox ps-margin pull-left"  style="padding-left: 20px">
			<label>
				<input type="checkbox" class="colored-blue" id="check" onclick="allCheck()">
				<span class="text" style="color: inherit;">全选</span>
			</label>
		</div>
       	<form action="/financial/personalApply/listMyLoanPage" method="get" class="subform">
          		<input name="sid" value="${param.sid}" type="hidden"/>
          		<input type="hidden" name="pager.pageSize" value="15"/>
          		
          		<input type="hidden" name="isBusinessTrip" value="${feeLoan.isBusinessTrip }">
          		<input type="hidden" name="status" value="${feeLoan.status }">
          		
          		<div class="btn-group pull-left searchCond">
          			<div class="table-toolbar ps-margin">
						<div class="btn-group">
							<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="借款类型">
							<c:choose>
								<c:when test="${not empty feeLoan.isBusinessTrip}">
									<font style="font-weight:bold;">
										<c:choose>
											<c:when test="${feeLoan.isBusinessTrip==1}">出差借款</c:when>
											<c:when test="${feeLoan.isBusinessTrip==0}">一般借款</c:when>
										</c:choose>
									</font>
								</c:when>
								<c:otherwise>
									借款类型
								</c:otherwise>
							</c:choose>
								<i class="fa fa-angle-down"></i>
							</a>
							<ul class="dropdown-menu dropdown-default">
								<li>
									<a href="javascript:void(0)"  class="clearThisElement" relateElement="isBusinessTrip" >不限条件</a>
								</li>
								<li>
									<a href="javascript:void(0)" class="setElementValue" relateElement="isBusinessTrip" dataValue="1">出差借款</a>
								</li>
								<li>
									<a href="javascript:void(0)" class="setElementValue" relateElement="isBusinessTrip" dataValue="0">一般借款</a>
								</li>
							</ul>
						</div>
					</div>
					
					
					<div class="table-toolbar ps-margin">
						<div class="btn-group">
							<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="借款流程状态">
								<c:choose>
									<c:when test="${not empty feeLoan.status}">
										<font style="font-weight:bold;">
											<c:choose>
												<c:when test="${feeLoan.status==5}">已领款</c:when>
												<c:when test="${feeLoan.status==4}">待领款</c:when>
												<c:when test="${feeLoan.status==3}">待借款</c:when>
												<c:when test="${feeLoan.status==1}">借款审批中</c:when>
												<c:when test="${feeLoan.status==-1}">借款失败</c:when>
												<c:when test="${feeLoan.status==6}">申请中</c:when>
											</c:choose>
										</font>
									</c:when>
									<c:otherwise>
										借款流程状态
									</c:otherwise>
								</c:choose>
								
								<i class="fa fa-angle-down"></i>
							</a>
							<ul class="dropdown-menu dropdown-default">
								<li>
									<a href="javascript:void(0)" class="clearThisElement" relateElement="status">不限条件</a>
								</li>
								<li>
									<a href="javascript:void(0)" class="setElementValue" relateElement="status" dataValue="5">已领款</a>
								</li>
								<li>
									<a href="javascript:void(0)" class="setElementValue" relateElement="status" dataValue="4">待领款</a>
								</li>
								<li>
									<a href="javascript:void(0)" class="setElementValue" relateElement="status" dataValue="3">待借款</a>
								</li>
								<li>
									<a href="javascript:void(0)" class="setElementValue" relateElement="status" dataValue="-1">借款失败</a>
								</li>
								<li>
									<a href="javascript:void(0)" class="setElementValue" relateElement="status" dataValue="1">借款审批中</a>
								</li>
								<li>
									<a href="javascript:void(0)" class="setElementValue" relateElement="status" dataValue="6">申请中</a>
								</li>
							</ul>
						</div>
					</div>
					
					
					<div class="table-toolbar ps-margin">
						<div class="btn-group cond" id="moreCondition_Div">
							<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
								<c:choose>
									<c:when
										test="${not empty feeLoan.startDate || not empty feeLoan.endDate}">
										<font style="font-weight:bold;">筛选中</font>
									</c:when>
									<c:otherwise>
	                                         	更多
	                                </c:otherwise>
								</c:choose>
								<i class="fa fa-angle-down"></i>
							</a>
							<div class="dropdown-menu dropdown-default padding-bottom-10 subMore" style="min-width: 330px;">
								<div class="ps-margin ps-search padding-left-10">
									<span class="btn-xs">起止时间：</span>
									<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="startDate" name="startDate" placeholder="开始时间"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" value="${feeLoan.startDate }"/>
									<span>~</span>
									<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="endDate" name="endDate" placeholder="结束时间"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" value="${feeLoan.endDate }"/>
								</div>
								<div class="ps-clear padding-top-10" style="text-align: center;">
									<button type="submit" class="btn btn-primary btn-xs">查询</button>
									<button type="button" class="btn btn-default btn-xs margin-left-10 " onclick="resetMoreCon('moreCondition_Div')">重置</button>
								</div>
							</div>
						</div>
					</div>
					
					<div class="ps-margin ps-search searchCond">
						<span class="input-icon">
							<input name="flowName" class="form-control ps-input formElementSearch" 
							type="text" placeholder="请输入关键字" value="${feeLoan.flowName }">
							<a href="javascript:void(0)" class="ps-searchBtn">
								<i class="glyphicon glyphicon-search circular danger"></i>
							</a>
						</span>
					</div>
					
					
					
          		</div>
					<div class="widget-buttons ps-widget-buttons pull-left batchOpt" style="display: none">
						<button class="btn btn-info btn-primary btn-xs" type="button" onclick="batchWithdraw()">
							批量催办
						</button>
					</div>
          	</form>
    </div>
    <div class="content-list">
        <div class="content-list-head">
            
            <div class="fr">
            </div>
        </div>
        <div class="ps-clear">
        </div>
        <c:choose>
            <c:when test="${not empty pageBean.recordList}">
                <c:forEach items="${pageBean.recordList}" var="loan" varStatus="status">
                    <div class="content-list-item">
                                
                        <c:choose>
                            <c:when test="${empty loan.instanceId or loan.instanceId eq 0 or loan.status == 0}">
                            	<span class="check_box">
                                    <input class="checkClass" type="checkbox" id="${loan.loanApplyInsId}_${loan.loanApplyState == 1 && loan.executor != userInfo.id ? 1 : 0}">
                                    <label for="check${status.count}" onclick="checkOrNO(${loan.loanApplyInsId},${loan.loanApplyState == 1 && loan.executor != userInfo.id ? 1 : 0})"></label>
                                </span>
                       			 <img src="/downLoad/userImg/${userInfo.comId}/${loan.creator}" class="avatar">
                                <div class="description">
                                    <p class="type">${(loan.isBusinessTrip == 1) ? '出差借款' : '一般借款'}</p>
                                    <p>
                                    	<a href="javascript:void(0)" onclick="parent.viewSpFlow(${loan.loanApplyInsId});">
                                    		${loan.loanApplyInsName}
                                    	</a>
                                   </p>
                                </div>
                                <div class="description">
                                    <p class="type"></p>
                                    <p>
                                    	
                                   </p>
                                </div>
                                
                                <c:choose>
                                	<c:when test="${loan.loanApplyState eq 1 }">
                                		<div class="status">
	                                    	<span>审批人：</span>
	                                        <p>${loan.executorName}</p>
	                                    </div>
	                                    <div class="progress">
		                                      <div class="bar-box">
		                                          <div class="bar" style="width: 10%"></div>
		                                      </div>
		                                      <span>10%</span>
	                                    </div>
	                                    <div class="operation">
	                                         <a style="cursor:pointer" onclick="parent.viewAtRight(
	                                                '/financial/personalApply/detailsLoanPage?sid=${param.sid}&loanApplyInsId=${loan.loanApplyInsId}');">
	                                            	查看
	                                        </a>
	                                        <c:if test="${loan.executor ne  userInfo.id}">
	                                            |
	                                            <a style="cursor:pointer" onclick="addBusRemind('022',${loan.loanApplyInsId})">催办</a>
	                                        </c:if>
	                                    </div>
                                	</c:when>
                                	<c:when test="${loan.loanApplyState eq -1 }">
                                		<div class="status pass1">
		                                      	  驳回
		                                    </div>
		                                    <div class="progress p-rebut">
		                                        <div class="bar-box">
		                                            <div class="bar" style="width: 10%"></div>
		                                        </div>
		                                        <div class="icon">x</div>
		                                    </div>
		                                    <div class="operation">
		                                        <a style="cursor:pointer" onclick="parent.viewAtRight(
		                                                '/financial/personalApply/detailsLoanPage?sid=${param.sid}&id=${loan.id}&flowName=${loan.flowName}&instanceId=${loan.instanceId}');">
		                                            	查看详情</a>
		                                    </div>
                                	</c:when>
                                	<c:otherwise>
                                		<c:choose>
                                			<c:when test="${loan.loanApplyState == 0}">
                                				<div class="status">
				                                     <div class="status pass">待借款</div>
				                                 </div>
				                                 <div class="progress">
						                                      <div class="bar-box">
						                                          <div class="bar" style="width: 1%"></div>
						                                      </div>
						                                      <span>1%</span>
					                                    </div>
				                                 <div class="operation">
				                                     <a href="javascript:void(0)" onclick="parent.viewSpFlow(${loan.loanApplyInsId});">待提交申请单</a>
				                                 </div>
                                			</c:when>
                                			<c:otherwise>
                                				<div class="status">
				                                     <div class="status pass">待借款</div>
				                                 </div>
				                                 <div class="progress">
						                                      <div class="bar-box">
						                                          <div class="bar" style="width: 30%"></div>
						                                      </div>
						                                      <span>30%</span>
					                                    </div>
				                                 <div class="operation">
				                                     <a href="javascript:void(0)" onclick="parent.addLoan('${loan.feeBudgetId}','${loan.isBusinessTrip}')">待提交借款单</a>
				                                 </div>
                                			</c:otherwise>
                                		</c:choose>
		                                 
                                	</c:otherwise>
                                </c:choose>
                                
                            </c:when>
                            <c:otherwise>
                            	<span class="check_box">
                                    <input class="checkClass" type="checkbox" id="${loan.instanceId}_${loan.status == 1 && loan.executor != userInfo.id ? 1 : 0}">
                                    <label for="check${status.count}" onclick="checkOrNO(${loan.instanceId},${loan.status == 1 && loan.executor != userInfo.id ? 1 : 0})"></label>
                                </span>
                       			 <img src="/downLoad/userImg/${userInfo.comId}/${loan.creator}" class="avatar">
                                <div class="description">
                                    <p class="type">${(loan.isBusinessTrip == 1) ? '出差借款' : '一般借款'}</p>
                                    <p>
                                    	<a href="javascript:void(0)" onclick="parent.viewSpFlow(${loan.instanceId});">
                                    		${loan.flowName}
                                    	</a>
                                    </p>
                                </div>
                                 <div class="description">
                                    <p class="type" style="display:${(not empty loan.loanApplyInsId && loan.loanApplyInsId >0) ?'block':'none'} ">${(loan.isBusinessTrip == 1) ? '出差申请' : '一般申请'}</p>
                                    <p>
                                    	<a href="javascript:void(0)" onclick="parent.viewSpFlow(${loan.loanApplyInsId});">
                                    		${loan.loanApplyInsName}
                                    	</a>
                                    </p>
                                </div>
                                <c:choose>
                                	<c:when test="${loan.status == 1}">
	                                    <div class="status">
	                                    	<span>审批人：</span>
	                                        <p>${loan.executorName}</p>
	                                    </div>
	                                    <div class="progress">
		                                      <div class="bar-box">
		                                          <div class="bar" style="width: 60%"></div>
		                                      </div>
		                                      <span>60%</span>
	                                    </div>
	                                    <div class="operation">
	                                        <a style="cursor:pointer" onclick="parent.viewAtRight(
	                                                '/financial/personalApply/detailsLoanPage?sid=${param.sid}&id=${loan.id}&instanceId=${loan.instanceId}');">
	                                            查看
	                                        </a>
	                                        <c:choose>
	                                        	<c:when test="${ loan.executor eq  userInfo.id}">
		                                            |
		                                            <a style="cursor:pointer" onclick="parent.viewSpFlow(${loan.instanceId});">审批</a>
	                                        	</c:when>
	                                        	<c:otherwise>
		                                            |
		                                            <a style="cursor:pointer" onclick="addBusRemind('022',${loan.instanceId})">催办</a>
	                                        	</c:otherwise>
	                                        </c:choose>
	                                    </div>
                                	</c:when>
                                	<c:when test="${loan.status == 4}">
	                                    <c:choose>
	                                    	<c:when test="${loan.balanceState==1 }">
			                                    <div class="status pass2">已领款</div>
			                                    <div class="progress p-ok">
			                                        <div class="bar-box">
			                                            <div class="bar"></div>
			                                        </div>
			                                        <div class="icon">✔</div>
			                                    </div>
	                                    	</c:when>
		                                    <c:otherwise>
			                                    <div class="status pass">待领款</div>
			                                    <div class="progress">
				                                      <div class="bar-box">
				                                          <div class="bar" style="width: 90%"></div>
				                                      </div>
				                                      <span>90%</span>
			                                    </div>
		                                    </c:otherwise>
	                                    </c:choose>
	                                    <div class="operation">
	                                        <a style="cursor:pointer" onclick="parent.viewAtRight(
	                                                '/financial/personalApply/detailsLoanPage?sid=${param.sid}&id=${loan.id}&flowName=${loan.flowName}&instanceId=${loan.instanceId}');">
	                                            查看详情</a>
	                                    </div>
                                	</c:when>
                                	<c:when test="${loan.status == -1}">
		                                    <div class="status pass1">驳回</div>
		                                    <div class="progress p-rebut">
		                                        <div class="bar-box">
		                                            <div class="bar" style="width: 30%"></div>
		                                        </div>
		                                        <div class="icon">x</div>
		                                    </div>
		                                    <div class="operation">
		                                        <a style="cursor:pointer" onclick="parent.viewAtRight(
		                                                '/financial/personalApply/detailsLoanPage?sid=${param.sid}&id=${loan.id}&flowName=${loan.flowName}&instanceId=${loan.instanceId}');">
		                                            查看详情</a>
		                                    </div>
                                	</c:when>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>
                <%--分页--%>
                
                 <tags:pageFeeBar url="/financial/personalApply/listMyLoanPage"></tags:pageFeeBar>
            </c:when>
            <c:otherwise>
                <div style="height:70px;text-align: center;font-size: 20px;margin-top:20px;">
                    <p style="color:#467aff;">未查询到数据！</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>
</html>
