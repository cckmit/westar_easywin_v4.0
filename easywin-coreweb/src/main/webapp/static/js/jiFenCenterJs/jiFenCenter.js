$(function(){
	$("#all_jiFen").click(function(){
		$("#type").val('');
		$("[name='jiFenType']").attr("class","btn btn-round btn-default");
		$(this).attr("class","btn btn-round btn-success");
		$("#orderForm").submit();
	});
	$("#week_jiFen").click(function(){
		$("#type").val('1');
		$("[name='jiFenType']").attr("class","btn btn-round btn-default");
		$(this).attr("class","btn btn-round btn-success");
		$("#orderForm").submit();
	});
	$("#month_jiFen").click(function(){
		$("[name='jiFenType']").attr("class","btn btn-round btn-default");
		$(this).attr("class","btn btn-round btn-success");
		$("#type").val('2');
		$("#orderForm").submit();
	});
	//按钮默认色
	var type='${param.type}';
	if('1'==type){
		$("[name='jiFenType']").attr("class","btn btn-round btn-default");
		$("#week_jiFen").attr("class","btn btn-round btn-success");
	}else if('2'==type){
		$("[name='jiFenType']").attr("class","btn btn-round btn-default");
		$("#month_jiFen").attr("class","btn btn-round btn-success");
	}else{
		$("[name='jiFenType']").attr("class","btn btn-round btn-default");
		$("#all_jiFen").attr("class","btn btn-round btn-success");
	}
});