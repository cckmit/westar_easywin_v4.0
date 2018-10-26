//提交前验证
function validClockForm(){
	//开始日期
	var clockDate = $("#clockDate").val();
	if(strIsNull(clockDate)){
		layer.tips("请填写开始日期","#clockDate",{tips:1})
		return false;
	}
	//提醒时间
	var clockTime = $("#clockTime").val();
	if(strIsNull(clockTime)){
		layer.tips("请填写提醒时间","#clockTime",{tips:1})
		return false;
	}
	//提醒周期
	var clockRepType = $("#clockRepType").val();
	if(clockRepType=='2'){//每周
		var obj = $(":checkbox[name='weekRepDate']:checked");
		if(obj.length==0){
			layer.tips("请填写提醒周数","#weekDate",{tips:1})
			return false;
		}
	}
	//提醒说明
	var content = $("#content").val();
	if(strIsNull(content)){//每周
		layer.tips("请填写提醒说明","#content",{tips:1})
		return false;
	}
	return true;
}
/**
*取得提醒周期，改变时间
*/
function getClockRepDate(){
	var clockRepType = $("#clockRepType").val();
	if(clockRepType=='0' || clockRepType=='1'||clockRepType=='4' ){//不重复 或是每一天 或是每年
		$("#clockRepDateDiv").css("display","none");
	}else{
		$("#clockRepDateDiv").css("display","block");
		if(clockRepType=='2'){//每周
			$("#dayDate").css("display","none");
			$("#weekDate").css("display","block");
			$("#monthDate").css("display","none");
			$("#yearDate").css("display","none");
		}else if(clockRepType=='3'){//每月
			$("#dayDate").css("display","none");
			$("#weekDate").css("display","none");
			$("#monthDate").css("display","block");
			$("#yearDate").css("display","none");
		}
	}
}
//初始化值
function initVal(dayMonth,dayWeek,clockRepDate){
	var clockRepType = $("#clockRepType").val();
	if(clockRepType=='0' || clockRepType=='1'||clockRepType=='4' ){//不重复 或是每一天 或是每年
		$("#monthRepDate option[value='"+dayMonth+"']").attr("selected", true);
		$(":checkbox[name='weekRepDate'][value='"+dayWeek+"']").attr("checked", true);
	}else if(clockRepType=='2'){//每周
		$("#monthRepDate option[value='"+dayMonth+"']").attr("selected", true);
		 var str= new Array();   
		 str=clockRepDate.split(",");
		 for (var i=0;i<str.length ;i++ ){   
			$(":checkbox[name='weekRepDate'][value='"+str[i]+"']").attr("checked", true);
		    }
	}else if(clockRepType=='3'){//每月
		$(":checkbox[name='weekRepDate'][value='"+dayWeek+"']").attr("checked", true);
		$("#monthRepDate option[value='"+clockRepDate+"']").attr("selected", true);
	}
}