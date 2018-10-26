$(function(){
	if(PageData.tab==21 || PageData.activityMenu=='m_4.1'){
		$(":radio[name=\"ruleType\"][value='2']").parent().remove();
		//从后台取数据，取不上也没有关系
		$.ajax({
			type : "post",
			url : "/attence/ajaxListAttenceType?sid="+PageData.sid+"&rnd="+Math.random(),
			dataType:"json",
			data:{attenceRuleId:PageData.attenceRule.attenceRuleId},
			success : function(data){
				var ruleType = $(":radio[name=\"ruleType\"]:checked").val();
				$("#ruleTypeDiv").data("ruleType",ruleType);
				
				var listAttenceType = data.listAttenceType;
				if(ruleType==1){//是标准
					$("#rule1").show();
					$("#rule3").hide();
					setRule1(listAttenceType);
				}else if(ruleType==2){//单双周
					
				}else if(ruleType==3){//灵活考勤
					$("#rule1").hide();
					$("#rule3").show();
					setRule3(listAttenceType);
				}
			}
		});
		//团队开关
		$.ajax({
			type:"post",
			url:"/organic/listOrgCfg?sid="+PageData.sid+"&t="+Math.random(),
		    dataType: "json",
		    success:function(data){
		    	if(data.status=='y'){
		    		var list = data.list;
		    		$.each(list,function(index,vo){
		    			$("#orgCfgDiv").find("input[name=\""+vo.cfgType+"\"]").attr("checked",vo.cfgValue=='1')
		    		})
		    	}else{
		    	}
		    },error:function(){
		    }
		})
		//单选框点击事件
		$(":radio[name=\"ruleType\"]").on("click",function(){
			var ruleType = $("#ruleTypeDiv").data("ruleType");
			//点中的选项
			var thisVal = $(this).val();
			$("#ruleTypeDiv").data("ruleType",thisVal);
			if(ruleType != thisVal){
				if(thisVal==1){//是标准
					$("#rule1").show();
					$("#rule3").hide();
				}else if(thisVal==2){//单双周
				}else if(thisVal==3){//灵活考勤
					$("#rule1").hide();
					$("#rule3").show();
				}
			}
		})
		
		initDatePicker();
		
		//添加工作时段按钮
		$("body").on("click",".attenceTimeDiv .optSpan .addBtn",function(){
			var pDiv = $(this).parents(".attenceTimeDiv").parent();
			var childLen = $(pDiv).children().length;
			if(childLen==3){
				layer.tips("工作时段不超过三个",$(pDiv),{tips:1})
			}else{
				var $html = $(this).parents(".attenceTimeDiv").clone();
				$html.find("input").val('');
				
				rowNum++;
				
				$html.find(".dayTimeS").attr("id","dayTimeS"+rowNum);
				$html.find(".dayTimeS").attr("rowNum",rowNum);
				
				$html.find(".dayTimeE").attr("id","dayTimeE"+rowNum);
				$html.find(".dayTimeE").attr("rowNum",rowNum);
				
				pDiv.append($html);
			}
			
		})
		//删除工作时段按钮
		$("body").on("click",".attenceTimeDiv .optSpan .delBtn",function(){
			var pDiv = $(this).parents(".attenceTimeDiv").parent();
			var childLen = $(pDiv).children().length;
			if(childLen==1){
				layer.tips("至少保留一个",$(pDiv),{tips:1})
			}else{
				$(this).parents(".attenceTimeDiv").remove();
			}
		})
		
		$("#rule3 button").on("click",function(){
			var ruleType = $(":radio[name=\"ruleType\"]:checked").val();
			if(ruleType==3){//设置灵活考勤
				window.top.layer.open({
			 		  type: 2,
			 		  title:false,
			 		  closeBtn:0,
			 		  area: ['720px', '500px'],
			 		  fix: true, //不固定
			 		  maxmin: false,
			 		  scrollbar:false,
			 		  move: false,
			 		  content:['/attence/attenceRuleSetPage?sid='+sid+'&t='+Math.random(),'no'],
			 		  success:function(layero,index){
			 			 var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			 			 var ruleType = $(":radio[name=\"ruleType\"]:checked").val();
			 			 if(ruleType==2){//单双周
			 				var attenceTypes = $("#rule2").data("attenceTypes");
			 				iframeWin.initData(attenceTypes,2);
			 			 }else if(ruleType==3){//灵活考勤
			 				var attenceTypes = $("#rule3").data("attenceTypes");
			 				iframeWin.initData(attenceTypes,3);
			 			 }
			 			 
						  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
							  window.top.layer.close(index);
						  })
						  $(iframeWin.document).find("#saveRuleBtn").on("click",function(){
							 if(iframeWin.checkRule(ruleType)){
								 var result =  iframeWin.saveRule();
								 setRule3(result);
								 window.top.layer.close(index);
							 }
						  })
			 		  }
			 	 });
			}
		});
		
		//验证并保存数据
		$("#saveAtttenceRule").on("click",function(){
			checkRules()&&updateRules();
		})
		
	}
})

var rowNum=0;
//设置标准考勤
function setRule1(listAttenceType){
	
	$("#rule1").data("listAttenceType",listAttenceType);
	//单个考勤时段
	var attenceTimeDiv = $("#rule1").find(".attenceTimeDiv");
	//考勤时段存放位置
	var p = $("#rule1").find(".attenceTimeDiv").parent();
	
	if(listAttenceType && listAttenceType.length>0){//有一个类型
		$.each(listAttenceType,function(typeIndex,attenceType){
			var listAttenceTime = attenceType.listAttenceTimes;
			if(listAttenceTime && listAttenceTime.length>0){
				if(typeIndex==0){
					$("#rule1").find(".attenceTimeDiv").remove();
				}
				$.each(listAttenceTime,function(timeIndex,attenceTime){
					//克隆一个考勤时段
					var detail = $(attenceTimeDiv).clone();
					
					rowNum++;
					
					$(detail).find("input").val('');
					$(detail).find(".dayTimeS").attr("id","dayTimeS"+rowNum);
					$(detail).find(".dayTimeS").attr("rowNum",rowNum);
					
					$(detail).find(".dayTimeE").attr("id","dayTimeE"+rowNum);
					$(detail).find(".dayTimeE").attr("rowNum",rowNum);
					
					$(detail).find(".dayTimeS").val(attenceTime.dayTimeS);
					$(detail).find(".dayTimeE").val(attenceTime.dayTimeE);
					$(p).append(detail);
					
				});
			}
		})
	}
}
//灵活考勤的
function setRule3(listAttenceType){
	
	$("#rule3").data("attenceTypes",listAttenceType);
	
	var modDiv = $('<tr class="attenceTr"><td class="weekDayNameTd">星期一</td><td class="attenceTimeTd"><div class="attenceTimeDiv"><span><span>签到</span><span class="dayTimeSTime">09:00</span></span><span><span>签退</span><span class="dayTimeETime">12:00</span></span></div></td></tr>');
	
	if(listAttenceType && listAttenceType.length>0){//至少有一个类型
		
		$("#rule3Details").data("hasData","yes");
		//展示列表信息
		$("#rule3 #rule3Details").show();
		var tbody = $("#rule3 #rule3Details").find("table tbody");
		$(tbody).html('');
		$.each(listAttenceType,function(typeIndex,attenceType){
			
			var attenceTr = modDiv.clone();
			//星期数
			var listAttenceWeeks = attenceType.listAttenceWeeks;
			var weekDayNameTd = "";
			$.each(listAttenceWeeks,function(typeIndex,attenceWeek){
				weekDayNameTd += " 星期"+ (attenceWeek.weekDay==7?"日":attenceWeek.weekDay==6?"六":attenceWeek.weekDay==5?"五":attenceWeek.weekDay==4?"四":attenceWeek.weekDay==3?"三":attenceWeek.weekDay==2?"二":"一");
			});
			$(attenceTr).find(".weekDayNameTd").html(weekDayNameTd);
			$(attenceTr).find(".attenceTimeTd").html('');
			//工作时段
			var listAttenceTimes = attenceType.listAttenceTimes;
			$.each(listAttenceTimes,function(timeIndex,attenceTime){
				var attenceTimeDiv = $(modDiv).find(".attenceTimeDiv").clone();
				$(attenceTimeDiv).find(".dayTimeSTime").html(attenceTime.dayTimeS)
				$(attenceTimeDiv).find(".dayTimeETime").html(attenceTime.dayTimeE);
				$(attenceTr).find(".attenceTimeTd").append($(attenceTimeDiv));
			});
			
			$(tbody).append($(attenceTr));
		});
	}else{
		$("#rule3 #rule3Details").hide();
	}
}
//初始化日期选择控件
function initDatePicker(){
	$(document).on("focus",".attenceTimeDiv .dayTimeSSpan input",function(){
		//本行数据
		var rowNum = $(this).attr("rowNum");
		var prev = $(this).parents(".attenceTimeDiv").prev();
		if(prev.length==0){
			//日期小于本次签退时间，大于上次签退时间
			WdatePicker({dateFmt:'HH:mm',maxDate: '#F{$dp.$D(\'dayTimeE'+rowNum+'\',{m:-1});}'})
		}else{
			var preRowNum = $(prev).find(".dayTimeSSpan input").attr("rowNum");
			//日期小于本次签退时间，大于上次签退时间
			WdatePicker({dateFmt:'HH:mm',maxDate: '#F{$dp.$D(\'dayTimeE'+rowNum+'\',{m:-1});}',minDate: '#F{$dp.$D(\'dayTimeE'+preRowNum+'\',{m:+1});}'})
		}
	})
	$(document).on("focus",".attenceTimeDiv .dayTimeESpan input",function(){
		//本行数据
		var rowNum = $(this).attr("rowNum");
		var next = $(this).parents(".attenceTimeDiv").next();
		if(next.length==0){
			//日期大于本次签到时间，小于下次签退时间
			WdatePicker({dateFmt:'HH:mm',minDate: '#F{$dp.$D(\'dayTimeS'+rowNum+'\',{m:+1});}'});
		}else{
			var nextRowNum = $(next).find(".dayTimeESpan input").attr("rowNum");
			//日期大于本次签到时间，小于下次签退时间
			WdatePicker({dateFmt:'HH:mm',maxDate: '#F{$dp.$D(\'dayTimeS'+nextRowNum+'\',{m:-1});}',minDate: '#F{$dp.$D(\'dayTimeS'+rowNum+'\',{m:+1});}'});
			
		}
	})
}
//验证规则是否正确
function checkRules(){
	//默认验证通过
	var a = 1;
	var ruleType = $(":radio[name=\"ruleType\"]:checked").val();
	
	var attenceTimeArray = new Array();
	if(ruleType==1){//是标准的
		var attenceTypeArray = new Array();
		var attenceType = {"weekType":-1};
		//遍历规则1
		$.each($("#rule1").find(".attenceTimeDiv"),function(timeIndex,vo){
			
			var dayTimeS = $(vo).find(".dayTimeS").val();
			if(!dayTimeS){
				layer.tips("请填写完整时间",$(vo).find(".dayTimeS"),{"tips":1});
				a = !1;
				return a;
			}
			var dayTimeE = $(vo).find(".dayTimeE").val();
			if(!dayTimeE){
				layer.tips("请填写完整时间",$(vo).find(".dayTimeE"),{"tips":1});
				a = !1;
				return a;
			}
			
			//考勤时间
			var attenceTime = {"dayTimeS":dayTimeS,"dayTimeE":dayTimeE};
			attenceTimeArray.push(attenceTime);
		})
		attenceType.listAttenceTimes = attenceTimeArray;
		attenceTypeArray.push(attenceType);
		$("#rule1").data("attenceTypes",attenceTypeArray);
	}else if(ruleType==3){
		//判断是否有考勤规则
		var hasData = $("#rule3Details").data("hasData");
		if(!hasData){
			layer.tips("请设置考勤规则",$("#rule3").find("button"),{"tips":1});
			a = !1;
			return a;
		}
	}
	return a;
}
//异步保存规则
function updateRules(){
	//初始化数据对象
	var attenceRule = {"id":PageData.attenceRule.attenceRuleId};
	//本次选中的标准
	var ruleType = $(":radio[name=\"ruleType\"]:checked").val();
	attenceRule.ruleType = ruleType;
	if(ruleType==1){//是标准的
		var data = $("#rule1").data("attenceTypes");
		attenceRule.listAttenceTypes = data;
	}else if(ruleType==3){
		var data = $("#rule3").data("attenceTypes");
		attenceRule.listAttenceTypes = data;
	}
	//从后台取数据，取不上也没有关系
	$.ajax({
		type : "post",
		url : "/attence/ajaxUpdateAttenceRule?sid="+PageData.sid+"&rnd="+Math.random(),
		dataType:"json",
		data:{"attenceRuleStr":JSON.stringify(attenceRule)},
		success : function(data){
			if(data.status=='f'){
				showNotification(2,data.info);
			}else{
				if(ruleType=='1'){
					//移除规则3
					$("#rule3Details").find("tbody").html('');
					$("#rule3").removeData("attenceTypes");
					
					$("#rule3Details").removeData("hasData");
					//展示列表信息
					$("#rule3 #rule3Details").hide();
					
				}else if(ruleType=='3'){
					
					//移除规则1
					$("#rule1").removeData("attenceTypes");
					var attenceTimeDiv = $("#rule1").find(".attenceTimeDiv:first");
					var newDiv = $(attenceTimeDiv).clone();
					
					$(newDiv).find("input:first").attr("id","dayTimeS0");
					$(newDiv).find("input:last").attr("id","dayTimeE0");
					
					$(newDiv).find("input:first").attr("rowNum","0");
					$(newDiv).find("input:last").attr("rowNum","0");
					
					$(newDiv).find("input:first").val('');
					$(newDiv).find("input:last").val('');
					
					var p = $("#rule1").find(".attenceTimeDiv").parent();
					$(p).html("");
					$(p).append($(newDiv));
					
				}
				showNotification(1,"操作成功!");
			}
		}
	});
	
}


function switchSet(ts,sid){
	var cfgType = $(ts).attr("name");
	var cfgValue = $(ts).attr("checked")?"1":"0";
	$.ajax({
		type:"post",
		url:"/organic/updateOrgCfg?sid="+PageData.sid+"&t="+Math.random(),
	    dataType: "json",
	    data:{"cfgType":cfgType,"cfgValue":cfgValue},
	    success:function(data){
	    	if(data.status=='y'){
	    		showNotification(1,data.info);
	    	}else{
	    		showNotification(2,data.info);
	    		if(cfgValue=='0'){
	    			$(ts).attr("checked",true);
	    		}else{
	    			$(ts).removeAttr("checked");
	    		}
	    	}
	    },error:function(){
	    	if(cfgValue=='0'){
    			$(ts).attr("checked",true);
    		}else{
    			$(ts).removeAttr("checked");
    		}
	    }
	})
	
}