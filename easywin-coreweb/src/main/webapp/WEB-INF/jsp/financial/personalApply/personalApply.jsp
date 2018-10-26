<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  
   
</head>
<body>
<div class="page-content">
    <div class="page-body" >
        <div class="row">
            <div class="col-md-12 col-xs-12 ">
                <div class="wrapper">
                    <div class="head">
                    	<div class="item">
                            <div class="title">
                                <p><i class="wsfont ws-gouwuche"></i>消费记录</p>
                                <i class="wsfont ws-i"></i>
                            </div>
                            <div class="content">
                                <div class="content-title">
                                    	待报销：￥<span id="consumeOffWaitting" >0</span>
                                </div>
                                <p class="content-details">
                                    <span id="consumeOffing" style="width:100%">报销中：0次</span>
                                </p>
                                <p class="content-details">
                                    <span id="consumeOffed" style="width:100%" >已报销：0次</span>
                                </p>
                                <div class="content-total" style="margin-top:5px;">
                                    	累计消费：<span id="consumeAmount">￥0</span>
                                </div>
                            </div>
                        </div>
                        <div class="item">
                            <div class="title">
                                <p><i class="wsfont ws-baoxiao"></i>报销记录</p>
                                <i class="wsfont ws-i"></i>
                            </div>
                            <div class="content">
                                <div class="content-title">
                                   	 报销中：￥<span id="loanDoingItemTotal" >0</span>
                                </div>
                                <p class="content-details">
                                    <span id="loanOffDoingTimes" >报销中：0次</span><span id="loanOffDoneTimes" >已报销：0次</span>
                                </p>
                                <div class="content-total">
                                   	累计报销：<span id="loanDoneItemTotal" >￥0</span>
                                </div>
                            </div>
                        </div>
                        <div class="item">
                            <div class="title">
                                <p><i class="wsfont ws-jiekuan"></i>借款记录</p>
                                <i class="wsfont ws-i"></i>
                            </div>
                            <div class="content">
                                <div class="content-title">
                                                                                          申请中：￥<span id="loanmoneyDoingTotal" >0</span>
                                </div>
                                <p class="content-details">
                                    <span id="feeLoanDoingTimes" >申请中：0次</span><span id="feeLoanDoneTimes" >已借款：0次</span>
                                </p>
                                <div class="content-total">
                                 	   累计借款：<span id="borrowingBalanceDoneTotal" >￥0</span>
                                </div>
                            </div>
                        </div>
                        <div class="item item-end">
                            <div class="title">
                                <p><i class="wsfont ws-chucha"></i>出差记录</p>
                                <i class="wsfont ws-i"></i>
                            </div>
                            <div class="content">
                                <div class="content-title">
                                                                                           累计出差：<span id="busTripTimes" >0次</span>
                                </div>
                                <p class="content-details">
                                    <span id="busTripOffWaitting"  >未销账：0次</span><span id="busTripOffed" >已销账：0次</span>
                                </p>
                                <div class="content-total">
                                   	 出差累计报销：<span id="busTripLoanItemTotal" >￥0</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="widget">
                        <div class="widget-body" style="text-align:center;padding-top:5px;overflow: hidden;overflow-y:auto;width:100%;box-shadow: none;" id="contentBody">
                            <iframe name="otherIFrame" id="otherAttrIframe" style="display: block;" class="layui-layer-load"
                                    src="/financial/personalApply/listMyLoanOffPage?sid=${param.sid}&pageSize=15&status=6"
                                    border="0" frameborder="0" allowTransparency="true"
                                    scrolling="no" width=100% height=100% vspale="0"></iframe>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
    $(function(){
        setIframeHeight("otherAttrIframe");
    })
    function setIframeHeight(id){
        try{
            var iframe = document.getElementById(id);
            if(iframe.attachEvent){
                iframe.attachEvent("onload", function(){
                    iframe.height =  iframe.contentWindow.document.documentElement.scrollHeight;
                });
                return;
            }else{
                iframe.onload = function(){
                    iframe.height = iframe.contentDocument.body.scrollHeight;
                };
                return;
            }
        }catch(e){
            throw new Error('setIframeHeight Error');
        }
    }
    //添加借款
    function addLoan(feeBudgetId,isBusinessTrip){
		var busType=isBusinessTrip==1?'030':'031';
		getSelfJSON("/financial/checkLoanOffAll",{sid:sid,feeBudgetId:feeBudgetId,busType:busType},function(data){
			if(data.status=='y'){
				listBusMapSelect(data.listBusMapFlows,busType,function(busMapFlow){
					var url = "/busRelateSpFlow/loan/addLoan?sid="+sid;
					url +="&busMapFlowId="+busMapFlow.id
					url +="&feeBudgetId="+feeBudgetId
					url +="&busType="+busType
					openWinByRight(url);
				})
			}else{
				showNotification(2,data.info);
			}
		})
    }
    
    function addLoanOff(feeBudgetId,loanReportId){
    	if(loanReportId && loanReportId>0){
	    	getSelfJSON("/financial/checkLoanRep4Off",{sid:sid,loanReportId:loanReportId,feeBudgetId:feeBudgetId},function(data){
				if(data.status=='y'){
					if(data.flowState == 2){//草稿
						viewSpFlow(data.instanceId);
					}else{
						var busType = data.busType;
						listBusMapSelect(data.listBusMapFlows,busType,function(busMapFlow){
							var url = "/busRelateSpFlow/loanOff/addLoanOff?sid="+sid;
							url +="&busMapFlowId="+busMapFlow.id
							url +="&feeBudgetId="+feeBudgetId
							url +="&loanReportId="+loanReportId
							url +="&busType="+busType;
							url +="&loanWay=1";//额度借款
							url +="&loanReportWay=1";//需要汇报的
							openWinByRight(url);
						})
					}
				}else{
					showNotification(2,data.info);
				}
			})
    	}else{
    		//直接报销03102
			listBusMapFlows('03102',function(busMapFlow){
				var url = "/busRelateSpFlow/loanOff/addLoanOff?sid="+sid;
				url +="&busMapFlowId="+busMapFlow.id
				url +="&feeBudgetId="+feeBudgetId
				url +="&loanReportId=0"
				url +="&busType=03102";
				url +="&loanWay=1";//额度借款
				url +="&loanReportWay=1";//需要汇报的
				openWinByRight(url);
			});
    	}
    }
</script>
 <link rel="stylesheet" href="/static/assets/css/iconfont.css">
 <link rel="stylesheet" href="/static/css/financial.css">
    <!--[if lt IE 9]>
    <style>
        /*针对IE8下不支持美化，则隐藏，采用原生的多选框*/
        .check_box label,.radio_box label{display: none}
    </style>
    <![endif]-->
    <script type="text/javascript">
        var sid="${param.sid}";//sid全局变量
        var EASYWIN = {
            "userInfo":{
                "id":"${userInfo.id}",
                "name":"${userInfo.userName}"
            },
            "sid":"${param.sid}"
        }
        function viewAtRight(url){
            openWinByRight(url);
        }
	
      //添加消费记录
        function addConsume() {
        	var url = '/consume/addConsumePage?sid=' + sid;
        	parent.openWinByRight(url);
        } 
        
        //报销
        function listMyLoanOffPage(size,page,total){
            //如果页码是-1则说明是select过来的
            if(page && page == -1){
                page = window.frames["otherIFrame"].document.getElementById("pageSelect").value
            }
            if(!page || (page >= 0 && page <= total)){
                if(!size || size == 0){
                    size = 15;
                }
                if(!page){
                    page = 0;
                }
                $("#otherAttrIframe").css("display","block");
                $("#loanOffApply").attr("class","active");
                $("#otherAttrIframe").attr("src","/financial/personalApply/listMyLoanOffPage?sid=${param.sid}&status=6&pageSize=" + size + "&pageNum=" + page + "");
            }
        }
        //借款
        function listMyLoanPage(size,page,total){
             $("#otherAttrIframe").css("display","block");
             $(this).parent().find("li").removeAttr("class");;
             $("#loanApply").attr("class","active")
             $("#otherAttrIframe").attr("src","/financial/personalApply/listMyLoanPage?sid=${param.sid}&pageSize=15&status=6");
        }
        //消费记录
        function listMyConsumePage(){
            $("#otherAttrIframe").css("display","block");
            $(this).parent().find("li").removeAttr("class");;
            $("#consumeList").attr("class","active");
            $("#otherAttrIframe").attr("src","/financial/personalApply/listMyConsumePage?sid=${param.sid}&pager.pageSize=15&status=0");
        }
        $(function(){
			//个人报销数据统计展示初始化
			var params={"sid":sid};
			var url = "/financial/loanApply/feeLoanOffForPersonal?rnd="+Math.random();
	    	postUrl(url,params,function(data){
				if(data.status=='f'){
					showNotification(2,data.info);
				}else{
					
					var  loanDoingItemTotal = data.feeLoanOff.loanDoingItemTotal;
					if(loanDoingItemTotal && loanDoingItemTotal>0){
						loanDoingItemTotal = toDecimal2(loanDoingItemTotal);
						$("#loanDoingItemTotal").addClass("go");
					}
					
					$("#loanDoingItemTotal").text(loanDoingItemTotal);
					$("#loanOffDoingTimes").text("报销中："+data.feeLoanOff.loanOffDoingTimes+"次");
					$("#loanOffDoneTimes").text("已报销："+data.feeLoanOff.loanOffDoneTimes+"次");
					if(data.feeLoanOff.loanOffDoingTimes && data.feeLoanOff.loanOffDoingTimes > 0){
						$("#loanOffDoingTimes").addClass("go");
					}
					if(data.feeLoanOff.loanOffDoneTimes && data.feeLoanOff.loanOffDoneTimes > 0){
						$("#loanOffDoneTimes").addClass("go");
					}
					var loanDoneItemTotal = data.feeLoanOff.loanDoneItemTotal;
					if(loanDoneItemTotal && loanDoneItemTotal>0){
						loanDoneItemTotal = toDecimal2(loanDoneItemTotal);
						$("#loanDoneItemTotal").addClass("go");
					}
					$("#loanDoneItemTotal").text("￥"+loanDoneItemTotal);
				}
			});
	    	//个人借款数据统计展示初始化
			var params={"sid":sid};
			var url = "/financial/loanApply/feeLoanForPersonal?rnd="+Math.random();
	    	postUrl(url,params,function(data){
				if(data.status=='f'){
					showNotification(2,data.info);
				}else{
					var loanmoneyDoingTotal = data.feeLoan.loanmoneyDoingTotal;
					if(loanmoneyDoingTotal && loanmoneyDoingTotal>0){
						loanmoneyDoingTotal = toDecimal2(loanmoneyDoingTotal);
						$("#loanmoneyDoingTotal").addClass("go");
					}
					$("#loanmoneyDoingTotal").text(loanmoneyDoingTotal);
					
					$("#feeLoanDoingTimes").text("申请中："+data.feeLoan.feeLoanDoingTimes+"次");
					$("#feeLoanDoneTimes").text("已借款："+data.feeLoan.feeLoanDoneTimes+"次");
					
					if(data.feeLoan.feeLoanDoingTimes && data.feeLoan.feeLoanDoingTimes > 0){
						$("#feeLoanDoingTimes").addClass("go");
					}
					if(data.feeLoan.feeLoanDoneTimes && data.feeLoan.feeLoanDoneTimes > 0){
						$("#feeLoanDoneTimes").addClass("go");
					}
					var borrowingBalanceDoneTotal = data.feeLoan.borrowingBalanceDoneTotal;
					if(borrowingBalanceDoneTotal && borrowingBalanceDoneTotal>0){
						borrowingBalanceDoneTotal = toDecimal2(borrowingBalanceDoneTotal);
						$("#borrowingBalanceDoneTotal").addClass("go");
					}
					
					$("#borrowingBalanceDoneTotal").text("￥"+borrowingBalanceDoneTotal);
				}
			});
	    	//个人的消费记录数据展示初始化
			var params={"sid":sid};
			var url = "/financial/loanApply/consumeForPersonal?rnd="+Math.random();
	    	postUrl(url,params,function(data){
				if(data.status=='f'){
					showNotification(2,data.info);
				}else{
					if(data.listConsume && data.listConsume[0]){
						var consumeAmount = 0;//总消费金额
						$.each(data.listConsume,function(index,consume){
							 var amount = consume.amount;
							consumeAmount = accAdd(consumeAmount,amount);
							 if(amount && amount>0){
								 amount = toDecimal2(amount);
							}
							 if(0==consume.status){//待报销
								$("#consumeOffWaitting").text(amount);
								$("#consumeOffWaitting").addClass("go");
							 }else if(1==consume.status){//报销中
								$("#consumeOffing").text("报销中：￥"+amount);
								$("#consumeOffing").addClass("go");
							 }else if(2==consume.status){//已完成报销
								$("#consumeOffed").text("已报销：￥"+amount);
								$("#consumeOffed").addClass("go");
							 }
						});
						
						if(consumeAmount && consumeAmount>0){
							consumeAmount = toDecimal2(consumeAmount);
							$("#consumeAmount").addClass("go");
						}
						
						$("#consumeAmount").text("￥"+consumeAmount);//总消费金额
					}
				}
			});
	    	//个人的出差数据展示初始化
			var params={"sid":sid};
			var url = "/financial/loanApply/businessTripForPersonal?rnd="+Math.random();
	    	postUrl(url,params,function(data){
				if(data.status=='f'){
					showNotification(2,data.info);
				}else{
					if(data.listBusinessTrip){
						var busTripLoanItemTotal = 0;//出差报销总金额
						var busTripTimes = 0;//出差次数
						$.each(data.listBusinessTrip,function(index,busTrip){
							busTripLoanItemTotal = accAdd(busTripLoanItemTotal,busTrip.loanItemTotal);
							busTripTimes += busTrip.times;
							 if(0==busTrip.loanOffState){//待报销
								$("#busTripOffWaitting").text("未销账："+busTrip.times+"次");
								$("#busTripOffWaitting").addClass("go");
							 }else if(1==busTrip.loanOffState){//已经报销、销账
								$("#busTripOffed").text("已销账："+busTrip.times+"次");
								$("#busTripOffed").addClass("go");
							 }
						});
						if(busTripLoanItemTotal && busTripLoanItemTotal>0){
							busTripLoanItemTotal = toDecimal2(busTripLoanItemTotal);
							$("#busTripLoanItemTotal").addClass("go");
						}
						if(busTripTimes > 0){
							$("#busTripTimes").addClass("go");////出差次数
						}
						$("#busTripLoanItemTotal").text("￥"+busTripLoanItemTotal);//出差报销总金额
						$("#busTripTimes").text(busTripTimes+"次");////出差次数
					}
				}
			});
	    	
	    	
	    	//待报销消费记录穿透
	    	$("#consumeOffWaitting").click(function(){
	    		if($("#consumeOffWaitting").text() && $("#consumeOffWaitting").text() != 0 ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/consume/listConsume?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.7&status=0");
	    		}
	    	});
	    	//报销中消费记录穿透
	    	$("#consumeOffing").click(function(){
	    		if($("#consumeOffing").text() && $("#consumeOffing").text() != '报销中：0次' ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/consume/listConsume?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.7&status=1");
	    		}
	    	});
	    	//已报销消费记录穿透
	    	$("#consumeOffed").click(function(){
	    		if($("#consumeOffed").text() && $("#consumeOffed").text() != "已报销：0次" ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/consume/listConsume?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.7&status=2");
	    		}
	    	});
	    	//累计消费消费记录穿透
	    	$("#consumeAmount").click(function(){
	    		if($("#consumeAmount").text() && $("#consumeAmount").text() != "￥0" ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/consume/listConsume?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.7");
	    		}
	    	});
	    	
	    	//报销中报销记录穿透
	    	$("#loanDoingItemTotal").click(function(){
	    		if($("#loanDoingItemTotal").text() && $("#loanDoingItemTotal").text() != 0 ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/financial/loanOff/listLoanOffOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.4&status=1&creator="+'${userInfo.id}'+"&userName="+encodeURIComponent('${userInfo.userName}'));
	    		}
	    	});
	    	
	    	//报销中报销记录穿透
	    	$("#loanOffDoingTimes").click(function(){
	    		if($("#loanOffDoingTimes").text() && $("#loanOffDoingTimes").text() != "报销中：0次" ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/financial/loanOff/listLoanOffOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.4&status=1&creator="+'${userInfo.id}'+"&userName="+encodeURIComponent('${userInfo.userName}'));
	    		}
	    	});
	    	
	    	//已报销报销记录穿透
	    	$("#loanOffDoneTimes").click(function(){
	    		if($("#loanOffDoneTimes").text() && $("#loanOffDoneTimes").text() != "已报销：0次" ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/financial/loanOff/listLoanOffOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.4&status=5&creator="+'${userInfo.id}'+"&userName="+encodeURIComponent('${userInfo.userName}'));
	    		}
	    	});
	    	
	    	//累计报销报销记录穿透
	    	$("#loanDoneItemTotal").click(function(){
	    		if($("#loanDoneItemTotal").text() && $("#loanDoneItemTotal").text() != "￥0" ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/financial/loanOff/listLoanOffOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.4&status=5&creator="+'${userInfo.id}'+"&userName="+encodeURIComponent('${userInfo.userName}'));
	    		}
	    	});
	    	
	    	//申请中借款记录穿透
	    	$("#loanmoneyDoingTotal").click(function(){
	    		if($("#loanmoneyDoingTotal").text() && $("#loanmoneyDoingTotal").text() != 0 ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/financial/loan/listLoanOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.3&status=1&creator="+'${userInfo.id}'+"&userName="+encodeURIComponent('${userInfo.userName}'));
	    		}
	    	});
	    	
	    	//申请中借款记录穿透
	    	$("#feeLoanDoingTimes").click(function(){
	    		if($("#feeLoanDoingTimes").text() && $("#feeLoanDoingTimes").text() != "申请中：0次" ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/financial/loan/listLoanOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.3&status=1&creator="+'${userInfo.id}'+"&userName="+encodeURIComponent('${userInfo.userName}'));
	    		}
	    	});
	    	
	    	//已借款记录穿透
	    	$("#feeLoanDoneTimes").click(function(){
	    		if($("#feeLoanDoneTimes").text() && $("#feeLoanDoneTimes").text() != "已借款：0次" ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/financial/loan/listLoanOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.3&status=5&creator="+'${userInfo.id}'+"&userName="+encodeURIComponent('${userInfo.userName}'));
	    		}
	    	});
	    	
	    	//累计借款记录穿透
	    	$("#borrowingBalanceDoneTotal").click(function(){
	    		if($("#borrowingBalanceDoneTotal").text() && $("#borrowingBalanceDoneTotal").text() != "￥0" ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/financial/loan/listLoanOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.3&status=5&creator="+'${userInfo.id}'+"&userName="+encodeURIComponent('${userInfo.userName}'));
	    		}
	    	});
	    	
	    	
	    	//累计出差记录穿透
	    	$("#busTripTimes").click(function(){
	    		if($("#busTripTimes").text() && $("#busTripTimes").text() != "0次" ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/financial/loanApply/listLoanApplyOfAuth?sid=${param.sid}&pager.pageSize=10&status=4&activityMenu=m_1.2&creator="+'${userInfo.id}'+"&userName="+encodeURIComponent('${userInfo.userName}'));
	    		}
	    	});
	    	//未销账出差记录穿透
	    	$("#busTripOffWaitting").click(function(){
	    		if($("#busTripOffWaitting").text() && $("#busTripOffWaitting").text() != "未销账：0次" ){
	    			$(".submenu").find(".active").removeClass();
	    			menuClick("/financial/loanApply/listLoanApplyOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.2&loanOffState=0&creator="+'${userInfo.id}'+"&userName="+encodeURIComponent('${userInfo.userName}'));
	    		}
	    	});
	    	
	    	//已销账出差记录穿透
	    	$("#busTripOffed").click(function(){
	    		if($("#busTripOffed").text() && $("#busTripOffed").text() != "已销账：0次" ){
	    			$(".submenu").find(".active").removeClass();menuClick("/financial/loanApply/listLoanApplyOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.2&loanOffState=1&creator="+'${userInfo.id}'+"&userName="+encodeURIComponent('${userInfo.userName}'));
	    		}
	    	});
	    	
	    	//出差累计报销记录穿透
	    	$("#busTripLoanItemTotal").click(function(){
	    		if($("#busTripLoanItemTotal").text() && $("#busTripLoanItemTotal").text() != "￥0" ){
	    			$(".submenu").find(".active").removeClass();menuClick("/financial/loanApply/listLoanApplyOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.2&creator="+'${userInfo.id}'+"&userName="+encodeURIComponent('${userInfo.userName}'));
	    		}
	    	});
	    	
	    	
	    	
		});
    </script>
</html>

