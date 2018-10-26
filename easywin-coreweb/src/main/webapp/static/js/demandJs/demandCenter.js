$(function(){
	$("body").on("click","[opt-btn]",function(){
		var opt = $(this).attr("opt-btn");
		var f = eval("demandCenterOpt."+opt + "()");
	});
	$("body").on("click","[opt-viewDemand]",function(){
		var opt = $(this).attr("opt-viewDemand");
		var busId = $(this).attr("busId");
		var f = eval("demandCenterOpt."+opt + "("+busId+")");
	});
	
	$("body").on("click","a[opt-busRemindBtn]",function(){
		var demandId =  $(this).attr("data-demandId");
		//添加催办信息
		addBusRemind('070',demandId,function(){
			
		})
	});
	
	//遍历需要设置字典表的字段
	$.each($("body").find(".demandStateEnumData"),function(index,obj){
		var relateElement = $(this).attr("relateElement");
		var relateElementName = $(this).attr("relateElementName");
		var _this = $(this);
		
		if(relateElementName){
			$(this).append('<li><a href="javascript:void(0)" class="clearThisElement" relateElement="'+relateElement+'" relateElementName="'+relateElementName+'">不限条件</a></li>')
		}else{
			$(this).append('<li><a href="javascript:void(0)" class="clearThisElement" relateElement="'+relateElement+'">不限条件</a></li>')
		}
		
		var dataDics = [];
		dataDics.push({"code":"0","zvalue":"待签收"});
		dataDics.push({"code":"1","zvalue":"待处理"});
		dataDics.push({"code":"-1","zvalue":"拒绝"});
		dataDics.push({"code":"2","zvalue":"处理中"});
		dataDics.push({"code":"3","zvalue":"待确认"});
		dataDics.push({"code":"4","zvalue":"结束"});
		
		$.each(dataDics,function(key,dataDic){
			if(relateElementName){
				$(_this).append('<li><a href="javascript:void(0)" class="setElementValue" relateElement="'+relateElement+'" dataValue="'+dataDic.code+'" relateElementName="'+relateElementName+'">'+dataDic.zvalue+'</a></li>')
			}else{
				$(_this).append('<li><a href="javascript:void(0)" class="setElementValue" relateElement="'+relateElement+'" dataValue="'+dataDic.code+'">'+dataDic.zvalue+'</a></li>')
			}
		})
	})
})

var demandCenterOpt = {
		//发布需求信息
		addDemand:function(){
			var url = "/demand/addDemandPage?sid="+pageParam.sid;
			openWinByRight(url);
		},
		//查看需求信息用于
		viewDemandForCerator:function(demandId){
			var url = "/demand/viewDemandPage?sid="+pageParam.sid;
			url = url + "&demandId="+demandId;
			
			var height = getWindowHeight();
			var option = {area: ['65%', height+'px']}
			openWinByRight(url,option);
		},
		//添加需求模板
		addDemandModuleCfg:function(){
			var url = "/demandModuleCfg/addDemandModuleCfgPage?sid="+pageParam.sid;
			openWinByRight(url);
		}
}