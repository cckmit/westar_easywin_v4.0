/**
 * 新增团队升级订单
 */
function addOrder(orgBalanceMoney){
	var order = calculatePrice($("#usersNum").val(),$("#orderForm [name='discountStandardId']").val());
	if(order){
		if(order.orderCost > orgBalanceMoney){
			$("#orderForm").submit();
		}else{
			window.top.layer.confirm('团队结余大于交易额；团队结余充足，无需升级。', {icon:3,btn: ['查看','取消'],title:'提示'}, function(index){
			  organicSpaceCfgInfo(sid);
			  window.top.layer.close(index);
			});
		}
	}
}
/**
 * 日期筛选条件重置
 */
function dateReset(){
	$("#searchForm [name='startDate']").val("");
	$("#searchForm [name='endDate']").val("");
	$("#searchForm").submit();
}
/**
 * 查看订单详情
 * @param orderId 订单主键
 */
function orderDetail(orderId){
	var url = "/order/orderDetail?sid="+sid+"&orderId="+orderId;
	openWinByRight(url);
}

/**
 * 计算订单价格
 * @param usersNum 使用人数
 * @param discountStandardId 折扣主键
 */
function calculatePrice(usersNum,discountStandardId){
	var originalTotalPrice,orderCost=0;
	usersNum = (usersNum-(usersNum%10))+((usersNum%10)==0?0:10);
	$.ajax({  
        url : "/order/calculatePrice?sid="+sid,  
        async : false, // 注意此处需要同步，因为返回完数据后，下面才能让结果的第一条selected  
        type : "POST",  
        dataType : "json", 
        data:{usersNum:usersNum,discountStandardId:discountStandardId},
        success : function(order) {   
        	if(order.succ){
				originalTotalPrice=order.originalTotalPrice;
				orderCost=order.orderCost
        	}else{
        		showNotification(2,order.promptMsg);
        	}
        }  
    });
	return {"originalTotalPrice":originalTotalPrice,"orderCost":orderCost};
}

/**
 * 订单计算初始化
 */
function initOrder(){
	var order = calculatePrice($("#usersNum").val(),$("#orderForm [name='discountStandardId']").val());
	if(order){
		$("#orderParamDisspan").text("当前已选择"+$("#usersNum").val()+"人，使用年限"+$("#orderForm [name='discountStandardId']").attr("years")+"年");
		$("#originalTotalPriceSpan").text("原价：￥"+order.originalTotalPrice+"元");
		$("#discountTotalPriceSpan").text("优惠：-￥"+(order.originalTotalPrice-order.orderCost)+"元");	
		$("#orderCostSpan").text("￥"+order.orderCost+"元");
	}
}
/**
 * 弹窗展示平台使用条约
 */
function easyWinRules(){
	var url="/order/easyWinRules?sid="+sid;
	layer.open({
		  type: 2,
		  title: ['使用协议', 'font-size:18px;'],
		  shadeClose: true,
		  shade: 0.8,
		  area: ['850px', '75%'],
		  content: url //iframe的url
		}); 
}

/**
 * 订单支付
 * @param orderId订单主键
 */
function orderToPay(orderId){
	var url = "/order/orderToPay?sid="+sid+"&orderId="+orderId;
	window.location.href=url;
}

/**
 * 订单支付
 * @param orderId订单主键
 */
function orderToPayByparent(orderId,sid){
	var url = "/order/orderToPay?sid="+sid+"&orderId="+orderId;
	parent.window.location.href=url;
}

/**
 * 订单支付
 */
function toPay(orderId,sid){
	var payWay = $("[name='payWay']:checked").val();
	window.open("/order/weChatPay?sid="+sid+"&orderId="+orderId+"&payWay="+payWay,"payMoneyPage");//订单支付
	//弹窗等待支付反馈结果
	layui.use('layer', function(){
		var layer = layui.layer;
		var url = "/order/toPay?sid="+sid+"&orderId="+orderId+"&payWay="+payWay;
		tabIndex = layer.open({
			type: 2,
			title: false,
			closeBtn: 0,
			shadeClose: false,
			shift:0,
			zIndex:1010,
			scrollbar:false,
			fix: true, //固定
			maxmin: false,
			move: false,
			area: ['460px', '186px'],
			content: [url,'no'], //iframe的url
			success:function(layero,index){
				
			},end:function(){
				
			}
		});
	})
}

/**
 * 支付回掉刷新订单列表
 * @param sid
 */
function paidCallBackRefeshOrders(sid){
	var url = "/order/listOrders?pager.pageSize=10&activityMenu=order_m_1.2&sid="+sid;
	parent.window.location.href=url;
}