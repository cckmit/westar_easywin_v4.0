    //设置全天
	function setAllday(ts){
		var evt = window.event;
		var ele = evt.srcElement || evt.target;
		var type = ele.type;
		if(!type){
			if($("#isAllDay").attr("checked")){//现在不是全天
				$("#isAllDay").attr("checked",false)
			}else{//现在是全天
				$("#isAllDay").attr("checked",true)
			}
		}
		//日程时间起
		var scheStartDate = $("#scheStartDate").val();
		var scheEndDate = $("#scheEndDate").val();
		$("#scheStartDate").unbind('focus');
		$("#scheEndDate").unbind('focus');
		if($("#isAllDay").attr("checked")){//是全天
			if(scheStartDate.length>10){
				scheStartDate = scheStartDate.substring(0,10);
				$("#scheStartDate").val(scheStartDate)
				scheEndDate = scheEndDate.substring(0,10);
				$("#scheEndDate").val(scheEndDate)
			}
			$("#scheStartDate").bind('focus',function(){
				WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'scheEndDate\',{d:-0});}'});
			})
			$("#scheEndDate").bind('focus',function(){
				WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'scheStartDate\',{d:-0});}'});
			})
		}else{//不是全天
			if(scheStartDate.length==10){
				$("#scheStartDate").val(scheStartDate+" 08:00");
				$("#scheEndDate").val(scheEndDatePage+" 18:00");
			}
			$("#scheStartDate").bind('focus',function(){
				WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate: '#F{$dp.$D(\'scheEndDate\',{d:-0});}'});
			})
			$("#scheEndDate").bind('focus',function(){
				WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate: '#F{$dp.$D(\'scheStartDate\',{d:-0});}'});
			})
		}
	}
	//设置是否重复
	function setIsRep(ts){
		var evt = window.event;
		var ele = evt.srcElement || evt.target;
		var type = ele.type;
		$("#repDate").find("input").removeAttr("datatype");
		if(!type){
			if($("#isRepeat").attr("checked")){//现在不需要重复
				$("#isRepeat").attr("checked",false)
			}else{//现在需要重复
				$("#isRepeat").attr("checked",true)
			}
		}
		if($("#isRepeat").attr("checked")){//展示重复参数
			$("#repTypeDiv").css("display","block");
			if(!repType){//若是没有
				var repType = $("#repType").val();
				if(repType==1){//每天
					$("#day").css("display","block");
					$("#day").find("input").attr("datatype","n");
				}else if(repType==2){//每周
					$("#week").css("display","block");
				}else if(repType==3){//每月
					$("#month").css("display","block");
					$("#month").find("input").attr("datatype","n");
				}else if(repType==4){//每年
					$("#year").css("display","block");
					$("#year").find("input").attr("datatype","n");
				}
			}
		}else{//隐藏重复参数
			$("#repTypeDiv").css("display","none")
		}
	}
	/**
	 * 设置重复类型
	 * @param ts
	 */
	function setRepType(ts){
		var repType = $(ts).val();
		$(".repType").css("display","none");
		$("#repDate").find("input").removeAttr("datatype");
		if(repType==1){//每天
			$("#day").css("display","block");
			$("#day").find("input").attr("datatype","n");
		}else if(repType==2){//每周
			$("#week").css("display","block");
		}else if(repType==3){//每月
			$("#month").css("display","block");
			$("#month").find("input").attr("datatype","n");
		}else if(repType==4){//每年
			$("#year").css("display","block");
			$("#year").find("input").attr("datatype","n");
		}
	}
	/**
	 * 设置结束类型
	 * @param ts
	 */
	function setRepEndType(ts){
		var repEndType = $(ts).val();
		$(".repEndType").css("display","none")
		if(repEndType==1){
			$("#times").css("display","block");
		}else if(repEndType==2){
			$("#dates").css("display","block");
			
		}
		
	}
	/**
	 * 添加日程
	 * @param sid
	 */
	function addschedle(sid,viewStart,viewEnd){
		if($("#subState").val()==1){
			return false;
		}
		//标题
		var title = $("#title").val();
		if(!$.trim(title)){
			layer.tips('请填写日程名称', "#title", {tips: 1});
			$("#title").focus();
			return false;
		}
		//开始日期
		var scheStartDate = $("#scheStartDate").val();
		if(!$.trim(scheStartDate)){
			layer.tips('请填写日程开始日期', "#scheStartDate", {tips: 1});
			return false;
		}
		//结束日期
		var scheEndDate = $("#scheEndDate").val();
		if(!$.trim(scheEndDate)){
			layer.tips('请填写日程结束日期', "#scheEndDate", {tips: 1});
			return false;
		}
		//是否为全天
		var isAllDay = 1;
		if(!$("#isAllDay").attr("checked")){
			isAllDay = 0;
		}
		//是否重复 默认不重复
		var isRepeat = 0;
		if($("#isRepeat").attr("checked")){//有重复的数据
			isRepeat = 1;
		}
		//重复类型
		var repType = '';
		if(isRepeat==1){//需要重复，则选出重复类型
			repType=$("#repType").val();
		}
		//重复时间
		var repDate = '';
		if(isRepeat==1){
			if(repType==1){//每天重复
				repDate = $("#day").find("input").val();
				if(!$.trim(repDate)){
					layer.tips('请填写日程执行周期', $("#day").find("input"), {tips: 1});
					$("#day").find("input").focus();
					return false;
				}
			}else if(repType==2){//每周重复
				var checkBox = $("#week").find(":checkbox");
				var repDateArray = new Array();
				if(checkBox.length>0){
					for(var i=0;i<checkBox.length;i++){
						if($(checkBox[i]).attr("checked")){
							repDateArray.push($(checkBox[i]).val());
						}
					}
				}
				if(repDateArray.length>0){
					repDate = repDateArray.join(",");
				}else{
					layer.tips('请填写日程执行周期', "#week", {tips: 1});
					return false;
				}
			}else if(repType==3){//每月重复
				repDate = $("#month").find("input").val();
				if(!$.trim(repDate)){
					layer.tips('请填写日程执行周期', $("#month").find("input"), {tips: 1});
					$("#month").find("input").focus();
					return false;
				}else if(parseInt(repDate)>12){
					$("#month").find("input").focus();
					layer.tips('月份不能大于12', $("#month").find("input"), {tips: 1});
					return false;
				}
			}else if(repType==4){//每年重复
				repDate = $("#year").find("input").val();
				if(!$.trim(repDate)){
					layer.tips('请填写日程执行周期', $("#year").find("input"), {tips: 1});
					$("#year").find("input").focus();
					return false;
				}
			}
		}
		//日程结束类型
		var repEndType = '';
		if(isRepeat==1){
			repEndType = $("#repEndType").val();
		}
		//日程结束时间
		var repEndDate = '';
		if(isRepeat==1 && repEndType>0){
			if(repEndType==1){
				repEndDate = $("#times").find("input").val();
				if(!$.trim(repEndDate)){
					layer.tips('请填写日程执行次数', $("#times").find("input"), {tips: 1});
					$("#times").find("input").focus();
					return false;
				}
			}else if(repEndType==2){
				repEndDate = $("#dates").find("input").val();
				if(!$.trim(repEndDate)){
					layer.tips('请填写日程结束时间', $("#dates").find("input"), {tips: 1});
					return false;
				}
			}
			
		}
		var scheUser = $("#listMembers_memberId").find("option");
		var users=new Array();
		for(var i=0; i<scheUser.length; i++){ 
			users.push($(scheUser[i]).val()); //如果选中，将value添加到变量s中 
		}
		//位置
		var address= $("#address").val();
		//说明
		var content= $("#content").val();
		//公开程度
		var publicType= $("#publicType").val();
		$("#resubmitTag").val('');
		
		var result='';
		$.ajax({
			type:"post",
			 url:"/schedule/addSchedule?sid="+sid+"&t="+Math.random(),
			 dataType: "json",
			 async:false,
			 traditional :true,
			 data:{
				"title":title,
				"scheStartDate":scheStartDate,
				"scheEndDate":scheEndDate,
				"isAllDay":isAllDay,
				"isRepeat":isRepeat,
				"repType":repType,
				"repDate":repDate,
				"repEndType":repEndType,
				"repEndDate":repEndDate,
				"address":address,
				"content":content,
				"publicType":publicType,
				"scheUserIds":users,
				"viewStart":viewStart,
				"viewEnd":viewEnd
				},
				beforeSend:function(){
					$("#subState").val(1);
				},
			 success:function(data){
		   	  $("#resubmitTag").val('');
			   	 var status = data.status;
		         if(status=='y'){
		        	 result=data.scheduleList;
			     }else{
			    	 showNotification(2,data.info);
			     }
			 },
			 error:function(XmlHttpRequest,textStatus,errorThrown){
	        	showNotification(2,"系统错误，请联系管理人员");
	        }
		})
		 $("#subState").val(0)
		 return result;
	}
	/* 清除下拉框中选择的option */
	function removeWeekClickUser(id,selectedUserId) {
		var selectobj = document.getElementById(id);
		for ( var i = 0; i < selectobj.options.length; i++) {
			if (selectobj.options[i].value==selectedUserId) {
				selectobj.options[i] = null;
				break;
			}
		}
		$("#user_img_"+selectedUserId).parent().remove();
		
		selected(id);
	}
	/**
	 * 修改日程
	 * @param sid
	 */
	function updateSchedle(sid,viewStart,viewEnd){
		if($("#subState").val()==1){
			return false;
		}
		//标题
		var title = $("#title").val();
		if(!$.trim(title)){
			layer.tips('请填写日程名称', "#title", {tips: 1});
			$("#title").focus();
			return false;
		}
		//开始日期
		var scheStartDate = $("#scheStartDate").val();
		if(!$.trim(scheStartDate)){
			layer.tips('请填写日程开始日期', "#scheStartDate", {tips: 1});
			return false;
		}
		//结束日期
		var scheEndDate = $("#scheEndDate").val();
		if(!$.trim(scheEndDate)){
			layer.tips('请填写日程结束日期', "#scheEndDate", {tips: 1});
			return false;
		}
		//是否为全天
		var isAllDay = 1;
		if(!$("#isAllDay").attr("checked")){
			isAllDay = 0;
		}
		//是否重复 默认不重复
		var isRepeat = 0;
		if($("#isRepeat").attr("checked")){//有重复的数据
			isRepeat = 1;
		}
		//重复类型
		var repType = '';
		if(isRepeat==1){//需要重复，则选出重复类型
			repType=$("#repType").val();
		}
		//重复时间
		var repDate = '';
		if(isRepeat==1){
			if(repType==1){//每天重复
				repDate = $("#day").find("input").val();
				if(!$.trim(repDate)){
					$("#day").find("input").focus();
					layer.tips('请填写日程执行周期', $("#day").find("input"), {tips: 1});
					return false;
				}
			}else if(repType==2){//每周重复
				var checkBox = $("#week").find(":checkbox");
				var repDateArray = new Array();
				if(checkBox.length>0){
					for(var i=0;i<checkBox.length;i++){
						if($(checkBox[i]).attr("checked")){
							repDateArray.push($(checkBox[i]).val());
						}
					}
				}
				if(repDateArray.length>0){
					repDate = repDateArray.join(",");
				}else{
					layer.tips('请填写日程执行周期', "#week", {tips: 1});
					return false;
				}
			}else if(repType==3){//每月重复
				repDate = $("#month").find("input").val();
				if(!$.trim(repDate)){
					$("#month").find("input").focus();
					layer.tips('请填写日程执行周期', $("#month").find("input"), {tips: 1});
					return false;
				}else if(parseInt(repDate)>12){
					$("#month").find("input").focus();
					layer.tips('月份不能大于12', $("#month").find("input"), {tips: 1});
					return false;
				}
			}else if(repType==4){//每年重复
				repDate = $("#year").find("input").val();
				if(!$.trim(repDate)){
					$("#year").find("input").focus();
					layer.tips('请填写日程执行周期', $("#year").find("input"), {tips: 1});
					return false;
				}
			}
		}
		//日程结束类型
		var repEndType = '';
		if(isRepeat==1){
			repEndType = $("#repEndType").val();
		}
		//日程结束时间
		var repEndDate = '';
		if(isRepeat==1 && repEndType>0){
			if(repEndType==1){
				repEndDate = $("#times").find("input").val();
				if(!$.trim(repEndDate)){
					$("#times").find("input").focus();
					layer.tips('请填写日程结束方式', $("#times").find("input"), {tips: 1});
					return false;
				}
			}else if(repEndType==2){
				repEndDate = $("#dates").find("input").val();
				if(!$.trim(repEndDate)){
					layer.tips('请填写日程结束方式', $("#dates").find("input"), {tips: 1});
					return false;
				}
			}
			
		}
		var scheUser = $("#listMembers_memberId").find("option");
		var users=new Array();
		for(var i=0; i<scheUser.length; i++){ 
			users.push($(scheUser[i]).val()); //如果选中，将value添加到变量s中 
		}
		//位置
		var address= $("#address").val();
		//说明
		var content= $("#content").val();
		//公开程度
		var publicType= $("#publicType").val();
		var result='';
		$.ajax({
			type:"post",
			url:"/schedule/updateSchedule?sid="+sid+"&t="+Math.random(),
			async:false,
			dataType: "json",
			traditional :true,
			data:{
				"id":id,
				"title":title,
				"scheStartDate":scheStartDate,
				"scheEndDate":scheEndDate,
				"isAllDay":isAllDay,
				"isRepeat":isRepeat,
				"repType":repType,
				"repDate":repDate,
				"repEndType":repEndType,
				"repEndDate":repEndDate,
				"address":address,
				"content":content,
				"publicType":publicType,
				"scheUserIds":users,
				"viewStart":viewStart,
				"viewEnd":viewEnd
			},
			beforeSend:function(){
				$("#subState").val(1);
			},
			success:function(data){
				$("#resubmitTag").val('');
				var status = data.status;
				if(status=='y'){
					 result=data.scheduleList;
				}else{
					showNotification(1,data.info);
				}
			},
			error:function(XmlHttpRequest,textStatus,errorThrown){
				showNotification(2,"系统错误，请联系管理人员");
				$("#resubmitTag").val('')
			}
		})
		$("#subState").val(0);
		return result;
	}

	//填充筛选条件
	function fillCalender(userId,userName,sid,countSub){
		
		var subType = $("#subType").val();
		
		var html = '\n<div class="table-toolbar ps-margin no-padding-top">';
		html+='\n 		<div class="btn-group">';
		html+='\n			<a class="btn btn-default dropdown-toggle btn-sm" data-toggle="dropdown" id="userName">';
		if($("#userId").val()>0 && $("#userNameTag").val()){
			html+='\n			<font style="font-weight:bold;">'+$("#userNameTag").val()+'</font>';
		}else{
			html+='\n			人员筛选';
		}
		html+='\n 				<i class="fa fa-angle-down"></i>';
		html+='\n			</a>';
		html+='\n 			<ul class="dropdown-menu dropdown-default" id="usedUl">';
		html+='\n 				<li><a href="javascript:void(0);"  onclick="userOneForUserIdCallBack(\'\',\'userId\',\'\',\'userName\',\'\')">不限条件</a></li>';
		html+='\n 				<li><a href="javascript:void(0);"  onclick="userOneForUserId(\''+userId+'\',\''+userName+'\',\'\',\''+sid+'\',\'userId\',\'userName\');">人员选择</a></li>';
		
		html+='\n			</ul>';
		html+='\n  	</div>';
		html+='\n  </div>';
		if(countSub>0){
			html+= '\n<div class="table-toolbar ps-margin no-padding-top">';
			html+='\n 		<div class="btn-group">';
			html+='\n			<a class="btn btn-default dropdown-toggle btn-sm" data-toggle="dropdown" id="subTypeFilter">';
			if(subType=='0'){
				html+='\n			<font style="font-weight:bold;">我的事件</font>';
			}else if(subType=='1'){
					html+='\n			<font style="font-weight:bold;">下属事件</font>';
			}else{
				html+='\n			全部事件';
			}
			html+='\n 				<i class="fa fa-angle-down"></i>';
			html+='\n			</a>';
			html+='\n 			<ul class="dropdown-menu dropdown-default" id="usedUl">';
			html+='\n 				<li><a href="javascript:void(0);"  onclick="searchTypeFilter(this,\'\')">全部事件</a></li>';
			html+='\n 				<li><a href="javascript:void(0);"  onclick="searchTypeFilter(this,\'0\')">我的事件</a></li>';
			html+='\n 				<li><a href="javascript:void(0);"  onclick="searchTypeFilter(this,\'1\')">下属事件</a></li>';
			html+='\n			</ul>';
			html+='\n  	</div>';
			html+='\n  </div>';
		}
		
		$("#conditionDiv").html(html);
	}
	//查询人员
	function userOneForUserIdCallBack(userId,userIdtag,userName,userNametag){
		if(userId && userName){//有选数值
			$("#"+userIdtag).val(userId);
			$("#userNameTag").val(userName);
			$("#"+userNametag).html('<font style="font-weight:bold;">'+userName+'</font><i class="fa fa-angle-down"></i>');
		}else{
			$("#"+userNametag).html("人员筛选<i class=\"fa fa-angle-down\"></i>");
			$("#"+userIdtag).val(0);
		}
		$("#calendar").fullCalendar("refetchEvents")
	}
	//查询时间模块
	function searchSche(ts,busType){
		$("#allUl").find("li").removeAttr("class");
		$("#allUl").find("#oneLi").attr("class","open");
		
		$(ts).parent().parent().find("li").removeAttr("class");
		$(ts).parent().attr("class","active bg-themeprimary");
		
		$("#busTypes").val(busType);
		$("#calendar").fullCalendar("refetchEvents")
	}
	//事件查询
	function searchTypeFilter(ts,typeFilter){
		if(typeFilter=='0'){
			$("#subTypeFilter").html('<font style="font-weight:bold;">我的事件</font><i class="fa fa-angle-down"></i>');
		}else if(typeFilter=='1'){
			$("#subTypeFilter").html('<font style="font-weight:bold;">下属事件</font><i class="fa fa-angle-down"></i>');
		}else{
			$("#subTypeFilter").html('全部事件<i class="fa fa-angle-down"></i>');
		}
		
		$("#subType").val(typeFilter);
		$("#calendar").fullCalendar("refetchEvents")
	}
	//查看日程
	function viewSchedule(sid,busId){
		var url = '/schedule/updateSchedulePage?sid='+sid+'&id='+busId+'&viewStart='+viewStart+'&viewEnd='+viewEnd;
		var height = $(window).height()-45;
		
		window.top.layer.open({
			  type: 2,
			  title: false,
			  closeBtn:0,
			  area: ['650px','550px'],
			  fix: true, //不固定
			  maxmin: false,
			  move: false,
			  scrollbar:false,
			  content: [url,'no'],
			  btn: ['关闭']
			});
		
		
	}
	//查看日程
	function editSchedulePage(sid,busId,eventId){
		var url = '/schedule/updateSchedulePage?sid='+sid+'&id='+busId+'&viewStart='+viewStart+'&viewEnd='+viewEnd;
		var height = $(window).height()-45;
		window.top.layer.open({
			  type: 2,
			  title: false,
			  closeBtn:0,
			  area: ['650px','550px'],
			  fix: true, //不固定
			  maxmin: false,
			  move: false,
			  scrollbar:false,
			  content: [url,'no'],
			  btn: ['修改日程','删除','关闭'],
			  yes: function(index, layero){
				  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				  var result = iframeWin.updateSchedle(sid,viewStart,viewEnd);
				  if(result){
					  $('#calendar').fullCalendar('removeEvents',eventId);
						  var list=result;
						  for(var i=0;i<list.length;i++){
			             		var schedule =list[i]; 
			             		 var f =new Object();
			             		 f.id=schedule.id+"_016";
			                     f.title="["+schedule.userName+"]"+schedule.title;
			                     f.start = $.fullCalendar.parseDate(schedule.scheStartDate);
			                     f.end = $.fullCalendar.parseDate(schedule.scheEndDate);
			                     f.className = "agenda_event_class";
			                     f.userId=schedule.userId;
			                     f.busType='016';
			                     f.busId=schedule.id;
			                     if(schedule.isAllDay==1){
			                     	f.allDay=true
			                     }else{
				                     f.allDay=false
			                     }
			 	               $("#calendar").fullCalendar('renderEvent', f);  //核心的更新代码  
			             	}
						window.top.layer.close(index);
						showNotification(1,"修改成功");
				  }
			  },btn2:function(index){
					 $.post("/schedule/deleteSchedule?sid="+sid+"&id="+busId,function (data){  
	                     if(data.status=='y'){
	                    	 $('#calendar').fullCalendar('removeEvents',eventId);
	                    	 window.top.layer.close(index);
	                    	 showNotification(1,"删除成功");
	                     }else{
	                    	 showNotification(2,data.info);
	                    	 
	                     }
	                 },"json");  
					 return  false;
				}
			});
	}
	