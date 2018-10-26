//页面加载初始化
$(function(){
	//任务名筛选
	$("#searchTaskName").blur(function(){
		//启动加载页面效果
		layer.load(0, {shade: [0.6,'#fff']});
		$("#taskName").val($("#searchTaskName").val());
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#searchTaskName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#searchTaskName").val())){
        		//启动加载页面效果
        		layer.load(0, {shade: [0.6,'#fff']});
				$("#taskName").val($("#searchTaskName").val());
				$("#searchForm").submit();
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#searchTaskName").focus();
        	}
        }
    });
	//操作删除和复选框
	$('.optTr').mouseover(function(){
		var display = $(this).find(".optTd .optCheckBox").css("display");
		if(display=='none'){
			$(this).find(".optTd .optCheckBox").css("display","block");
			$(this).find(".optTd .optRowNum").css("display","none");
		}
	});
	$('.optTr ').mouseout(function(){
		var optCheckBox = $(this).find(".optTd .optCheckBox");
			var check = $(optCheckBox).find("input").attr('checked');
			if(check){
				$(this).find(".optTd .optCheckBox").css("display","block");
				$(this).find(".optTd .optRowNum").css("display","none");
			}else{
				$(this).find(".optTd .optCheckBox").css("display","none");
				$(this).find(".optTd .optRowNum").css("display","block");
			}
	});
	
	$(":checkbox[name='ids'][disabled!='disabled']").click(function(){
		var checkLen = $(":checkbox[name='ids'][disabled!='disabled']:checked").length;
		var len = $(":checkbox[name='ids'][disabled!='disabled']").length;
		if(checkLen>0){
			//隐藏查询条件
			$(".searchCond").css("display","none");
			//显示批量操作
			$(".batchOpt").css("display","block");
			if(checkLen==len){
				$("#checkAllBox").attr('checked', true);
			}else{
				$("#checkAllBox").attr('checked', false);
			}
		}else{
			//显示查询条件
			$(".searchCond").css("display","block");
			//隐藏批量操作
			$(".batchOpt").css("display","none");
			
			$("#checkAllBox").attr('checked', false);
		}
	});
	//任务详情弹窗查看
	$("body").on("click",".taskView",function(){
		var actObj = $(this);
		authCheck($(actObj).attr("taskId"),"003",-1,function(authState){
			var url = "/task/viewTask?sid="+sid+"&id="+$(actObj).attr("taskId");
			openWinWithPams(url,"800px",($(window).height()-90)+"px");
		})
	});
});
//任务查看
function viewTask(sid,taskId){
	viewModInfo(taskId,'003',-1);
}


//关注人更新
function userMoreCallBack(){
	var userIds =new Array();
	$("#listRangeUser_userId option").each(function() { 
		userIds.push($(this).val()); 
    });

	$.post("/personAttention/updatePersonAttention?sid="+sid,{Action:"post",userIds:userIds.toString()},     
		function (msgObjs){
		showNotification(1,msgObjs);
		window.self.location.reload();
	});
}


//统计客户信息
function statisticsTask(index,statisticsType,version){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,"#fff"]});
	//var url = '/statistics/task/statisticsTaskPage?sid='+sid+'&searchTab='+index+"&t="+Math.random();
	//url +="&statisticsType="+statisticsType;
	var url = '/task/listTaskOfAllPage?pager.pageSize=10&version='+version+'&activityMenu='+(version==2?'task_promote_1.5':'task_m_1.5')+'&sid='+sid+"&t="+Math.random();
	window.location.href=url;
}

var tabIndex;
//查看子任务
function viewSonTask(sid,taskId){
	authCheck(taskId,'003',-1,function(data){
		var url = "/task/viewTask?sid="+sid+"&id="+taskId;
		openWinWithPams(url,"800px",($(window).height()-90)+"px");
	})
}
//新建任务
function addTask(sid,version){
	var url = "/task/addTaskPage?sid="+sid+"&version="+version;
	openWinByRight(url);
}
//任务删除
function delTask(){
	if(checkCkBoxStatus('ids')){
		window.top.layer.confirm('确定要删除被选择的任务吗？', {icon: 3, title:'提示'}, function(index){
		  var url = reConstrUrl('ids');
		  $("#delForm input[name='redirectPage']").val(url);
		  $('#delForm').submit();
		  window.top.layer.close(index);
		});
	}else{
		window.top.layer.msg('请先选择需要删除的任务', {icon: 6});
	}
}
//任务合并
function taskCompress(sid){
	//多选
	var ckBoxs = $("#delForm").find(":checkbox[name='ids']:checked");
	var ids = new Array();
	for(var i=0;i<ckBoxs.length;i++){
		if($(ckBoxs[i]).attr("state")!=1){
			window.top.layer.msg('请选择正在办理的任务！', {icon:2});
			return false;
		}
		var taskId = $(ckBoxs[i]).val();
		if($.inArray(taskId,ids)==-1){
			ids.push(taskId);
		}
	}
	if(ids.length==0){
		window.top.layer.msg('请先选择需要合并的任务', {icon:2});
	}else if(ids.length==1){
		window.top.layer.msg('请先选择至少两个任务', {icon:2});;
	}else if(ids.length>3){
		window.top.layer.msg('最多选择三个任务', {icon:2});;
	}else{
		var url="/task/taskCompressPage?sid="+sid+"&ids="+ids;
		var height = $(window).height()-45
		layer.open({
		  type: 2,
		  title: ['任务合并', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  shadeClose: false,
		  shade: 0.3,
		  shift:0,
		  fix: true, //固定
		  maxmin: false,
		  move: false,
		  area: ['800px','620px'],
		  content: [url,'no'], //iframe的url
		  btn: ['合并', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window[layero.find('iframe')[0]['name']];
			  iframeWin.formSub();
		  },
		  success:function(layero,index){
		    var iframeWin = window[layero.find('iframe')[0]['name']];
		    iframeWin.setWindow(window.document,window);
		    
		    $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  layer.close(index);
			  })
		  }
		});
	}
}

//部门筛选
function depMoreForDepIdCallBack(options,depIdtag){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#"+depIdtag+"_select").find("option").remove();
	for(var i =0;i<options.length;i++){
		$("#"+depIdtag+"_select").append("<option selected='selected' value='"+options[i].value+"' >"+options[i].text+"</option>");
	}
	$("#searchForm").submit();
}
function removeAll(depIdtag){
	$("#"+depIdtag+"_select").find("option").remove();
	$("#searchForm").submit();
}
//完成状态筛选
function doneStateFilter(stateId,ts){
		$("#overDiv").hide();
		if($(ts).hasClass("btn-primary")){//点击的是去掉
			$(ts).removeClass("btn-primary");//不选中
			if(!$(ts).hasClass("btn-default")){
				$(ts).addClass("btn-default");
			}
			$("#state").val('');
		}else{//点击的是选择
			var stateBtnArray = $(".stateBtn");
			$.each(stateBtnArray,function(index,item){
				$(this).removeClass("btn-primary");//不选中
				if(!$(this).hasClass("btn-default")){//没有默认颜色，则添加一个
					$(this).addClass("btn-default");
				}
			})
			$(ts).addClass("btn-primary");
			//取消逾期选中
			$("#overBtn").removeClass("btn-primary") 
			if(!$("#overBtn").hasClass("btn-default")){
				$("#overBtn").addClass("btn-default");
			}
			$("#state").val(stateId);
			$("#overdue").val('false');
		}
}
//进行中状态筛选
function toDoStateFilter(stateId,ts){
	if($(ts).hasClass("btn-primary")){//点击的是去掉状态
		$("#overDiv").hide();
		//不选中
		$(ts).removeClass("btn-primary");
		if(!$(ts).hasClass("btn-default")){
			$(ts).addClass("btn-default");
		}
		//取消逾期选中
		$("#overBtn").removeClass("btn-primary") 
		if(!$("#overBtn").hasClass("btn-default")){
			$("#overBtn").addClass("btn-default");
		}
		
		$("#state").val('');
		$("#overdue").val('false');
	}else{//点击的是选择
		var stateBtnArray = $(".stateBtn");
		$.each(stateBtnArray,function(index,item){
			$(this).removeClass("btn-primary");//不选中
			if(!$(this).hasClass("btn-default")){//没有默认颜色，则添加一个
				$(this).addClass("btn-default");
			}
		})
		$(ts).addClass("btn-primary");
		
		$("#overDiv").show();
		$("#state").val(stateId);
	}
}

//重要程度筛选
function taskGradeFilter(grade,ts){
	if($(ts).hasClass("btn-primary")){//点击的是去掉
		$(ts).removeClass("btn-primary");//不选中
		if(!$(ts).hasClass("btn-default")){
			$(ts).addClass("btn-default");
		}
		$("#grade").val('');
	}else{//点击的是选择
		var gradeBtnArray = $(".gradeBtn");
		$.each(gradeBtnArray,function(index,item){
			$(this).removeClass("btn-primary");//不选中
			if(!$(this).hasClass("btn-default")){//没有默认颜色，则添加一个
				$(this).addClass("btn-default");
			}
		})
		$(ts).addClass("btn-primary");
		$("#grade").val(grade);
	}
}
//逾期筛选
function taskOverFilte(overdue,ts){
	if($(ts).hasClass("btn-primary")){//点击的是去掉
		$(ts).removeClass("btn-primary");//不选中
		if(!$(ts).hasClass("btn-default")){
			$(ts).addClass("btn-default");
		}
		$("#overdue").val('false');
	}else{//点击的是选择
		var overBtnArray = $(".overBtn");
		$.each(overBtnArray,function(index,item){
			$(this).removeClass("btn-primary");//不选中
			if(!$(this).hasClass("btn-default")){//没有默认颜色，则添加一个
				$(this).addClass("btn-default");
			}
		})
		$(ts).addClass("btn-primary");
		$("#overdue").val(overdue);
	}
}

//统计图穿透筛选
function countClick(type){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#countType").val(type);
	$("#searchForm").submit();
}
//统计图紧急度穿透筛选
function countGradeClick(type){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	if(type=='all'){
		$("#grade").val('');
	}else{
		$("#grade").val(type);
	}
	$("#searchForm").submit();
}

//移除数据
function removeTaskTodo(modId){
	countTodo--;
	$("#allTodoBody").find("[modId='"+modId+"']").parents("tr").remove();
	
	//是否有分页
	var isPaged = $(document).find(".ps-page").length>0;
	if(isPaged){//有分页，则修改数据
		var totalRecord = parseInt($(".ps-page").find(".badge").html());
		$(".ps-page").find(".badge").html(totalRecord-1)
	}	
	
	$.each($("#allTodoBody .rowNum"),function(index,item){
		$(this).html(index+1);
	})
}
/**
 * 待办任务中心，移除不需要待办的事项
 */
function loadTaskTodo(){
	var allLen = $("#allTodoBody tr").length;
	if(allLen==0){
		var url = reConUrlByClz("#allTodoBody tr",0);
		window.self.location = url;
	}else{
		var pageSize = $("#searchForm #pageSize").val();
		if(!pageSize){
			pageSize=15;	
		}
		
		var selectData = {};
		var selects = $("#searchForm").find("select");
		if(selects && selects.get(0)){
			$.each(selects,function(selectIndex,select){
				var list =$(select).attr("list");
				var listkey =$(select).attr("listkey");
				var listvalue =$(select).attr("listvalue");
				var options = $(select).find("option");
				if(options && options.get(0)){
					$.each(options,function(index,option){
						selectData[list+"["+index+"]."+listkey]  = $(option).val();
						selectData[list+"["+index+"]."+listvalue]  = $(option).html();
					})
				}
			})
		}
		selectData.pageNum=nowPageNum;
		selectData.pageSize=pageSize;
		
		$("#searchForm").ajaxSubmit({
			type:"post",
			url:"/task/ajaxListTaskToDoPage?sid="+sid+"&t="+Math.random(),
			dataType: "json",
			data:selectData,
			 beforeSend: function(){
				$("#subDiv .sub-each").remove();
	     		$("#subDiv").css("background","url(/static/plugins/layer/skin/default/loading-0.gif) no-repeat center");
		      },
			success:function(data){
				 $("#subDiv").css("background","");
				if(data.status=='y'){
					var list = data.list;
					var html = getTaskTodoHtml(list);
					$("#allTodoBody").append(html);
					$.each($("#allTodoBody .rowNum"),function(index,item){
						$(this).html(index+1);
					});
					if(data.todoNum<=10){
						$("body").find(".ps-page").hide();
						$("body").find(".ps-page>.ps-pageText>.badge").html(data.todoNum)
					}else{
						$("body").find(".ps-page").show();
						$("body").find(".ps-page>.ps-pageText>.badge").html(data.todoNum)
					}
					
					if(data.countType == 1){
						var subUser = data.subUser;
						var subHtml = getTaskTodoSubCount(subUser);
						$("#subDiv").html(subHtml);
					}else{
						countTaskState(data.toDoCount);
					}
					
				}
			},
			error:function(XmlHttpRequest,textStatus,errorThrown){
			}
		})
	}
	
	
}
/**
 * 下属办理中
 * @param subUser
 * @returns {String}
 */
function getTaskTodoSubCount(subUser){
	var html = '';
	if(subUser.length>0){
		for(var i=0;i<subUser.length;i++){
			var obj = subUser[i];
			html+='<div class="sub-each">';
			html+='<div class="ticket-user pull-left other-user-box sub-head" >';
			var uuid = obj.smImgUuid;
			html +='\n <img class="user-avatar" src="/downLoad/userImg/'+obj.comId+'/'+obj.id+'?sid='+sid+'" title="'+obj.userName+'" />';
			html+='\n <span class="user-name">'+obj.userName+'</span>';
			html+='\n </div>';
			html+='\n <div class="" >';
			html+='\n <div class="pull-left sub-textDiv" style="left:20px" >';
			html+='\n <p  style="text-align:center;">';
			html+='\n <a  class="sub-doingNum" href="javascript:void(0);" executor="'+obj.id+'" userName="'+obj.userName+'" id="'+obj.id+'" >'+obj.taskToDoNum+'</a></p>';
			html+='\n <span style="text-indent:1em;font-size:10px">办理中</span>';
			html+='</div>';
			html+='</div>';
			html+='</div>';
		}
	}
	return html;
}
/**
 * 
 * @param subUser
 * @returns {String}
 */
function getAllTaskSubCount(subUser){
	var html = '';
	if(subUser.length>0){
		for(var i=0;i<subUser.length;i++){
			var obj = subUser[i];
			html+='<div class="sub-each">';
			html+='<div class="ticket-user pull-left other-user-box sub-head" >';
			html +='\n <img class="user-avatar" src="/downLoad/userImg/'+obj.comId+'/'+obj.id+'?sid='+sid+'" title="'+obj.userName+'" />';
			html+='\n <span class="user-name">'+obj.userName+'</span>';
			html+='\n </div>';
			
			html+='\n <div>';
			html+='\n <div class="pull-left sub-textDiv" >';
			html+='\n <p  style="text-align:center;">';
			html+='\n <a  class="sub-doingNum" href="javascript:void(0);" executor="'+obj.id+'" userName="'+obj.userName+'" id="'+obj.id+'" >'+obj.taskToDoNum+'</a></p>';
			html+='\n <span style="text-indent:1em;font-size:10px">办理中</span>';
			html+='</div>';
			html+='\n <div class="pull-left sub-line" ></div>';
			html+='\n <div class="pull-left sub-textDiv" style="left:25px;">';
			html+='\n <p style="text-align:center;">';
			html+='\n <a class="sub-doneNum" href="javascript:void(0);" operatorNotExecutor="'+obj.id+'"  userName="'+obj.userName+'" >'+obj.taskDoneNum+'</a></p>';
			html+='\n <span style="text-indent:1em;font-size:10px;">已办理</span></div>';
			html+='</div>';
			html+='</div>';
		}
	}
	return html;
}
/**
 * 补充待办集合
 * @param list
 * @returns {String}
 */
function getTaskTodoHtml(list){
	var html = '';
	if(list.length>0){
		//需要添加的行数
		var allShowNum = 10-$("#allTodoBody .rowNum").length;
		for(var i=0;i<list.length;i++){
			if(allShowNum==0){//行数已满，不在添加
				break;
			}
			var taskVo = list[i];
			var modLen =  $("#allTodoBody").find("[modId='"+taskVo.id+"']").length;
			if(modLen>0){
				continue;
			}
			allShowNum--;
			
			
			html +='\n <tr>';
			html +='\n		<td class="rowNum">'+(i+1)+'</td>';
			
			html +='\n		<td>';
			var readState = taskVo.isRead==0?"noread":"";
			
			html +='\n 			<a href="javascript:void(0);" class="'+readState+'" onclick="readMod(this,\'taskCenter\','+taskVo.id+', \'003\');viewTask(\''+sid+'\',\''+taskVo.id+'\');"  modId=\''+taskVo.id+'\'>';
			html +='\n				'+cutstr(taskVo.taskName,31);
			html +='\n			</a>';
			html +='\n 		</td>';
			
			html +='\n		<td>';//关联模块TD
			if(!taskVo.busType || taskVo.busType=='0'){
				html +='\n	未关联';
			}else{
				var busTypeName="";
				if(taskVo.busType=='005'){
					busTypeName="[项目]";
				}else if(taskVo.busType=='012'){
					busTypeName="[客户]";
				}else if(taskVo.busType=='022'){
					busTypeName="[审批]";
				}else{
					busTypeName="[未定义]";
				}
				html +='\n	<a href="javascript:void(0)" title="'+taskVo.busName+'" class="viewModInfo" busType="'+taskVo.busType+'" busId="'+taskVo.busId+'">'+busTypeName+taskVo.busName+'</a>';
			}
			html +='\n 		</td>';
			
			html +='\n 		<td>';
			if(taskVo.grade==4){
				html +='\n 		<span class="label label-danger">紧急且重要</span>';
			}else if(taskVo.grade==3){
				html +='\n 		<span class="label label-danger">紧急</span>';
			}else if(taskVo.grade==2){
				html +='\n 		<span class="label label-orange">重要</span>';
			}else if(taskVo.grade==1){
				html +='\n 		<span class="label label-success">普通</span>';
			}
			html +='\n 		</td>';
			html +='\n		<td><img src="/static/images/light_'+taskVo.overDueLevel+'.gif"></td>';
			html +='\n 		<td class="center ">';
			if(taskVo.dealTimeLimit){
				html +='\n 		'+taskVo.dealTimeLimit;
			}
			html +='\n 		</td>';
			html +='\n 		<td class="no-padding-left no-padding-right">';
			html +='\n 			<div class="ticket-user pull-left other-user-box">';
			var imgUrl = "/downLoad/userImg/"+taskVo.comId+"/"+taskVo.fromUser;
			html +='\n 				<img class="user-avatar" src="'+imgUrl+'" title="'+taskVo.fromUserName+'" />';
			html +='\n				<span class="user-name">'+taskVo.fromUserName+'</span>';
			html +='\n			</div>';
			html +='\n 		</td>';
			html +='\n 		<td>';
			html +='\n'+taskVo.recordCreateTime.substring(0,10);
			html +='\n 		</td>';
			html +='\n </tr>';
		
		}
	}
	return html;
}

//窗体点击事件检测
document.onclick = function(e){
	var evt = e?e:window.event;
	var ele = $(evt.srcElement || evt.target);
	if ($(ele).parents("#moreCondition_Div").length == 1) { 
		if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
			return false;
		}else{
			if(!$("#moreCondition_Div").hasClass("open")){
				$(".searchCond").find(".open").removeClass("open");
				$("#moreCondition_Div").addClass("open");
			}
		}
	} else{
		$("#moreCondition_Div").removeClass("open");
	}
}
/**
 * 展示查询条件中更多
 */
function displayMoreCond(divId){
	if($("#"+divId).hasClass("open")){
		$("#"+divId).removeClass("open");
	}else{
		$("#"+divId).addClass("open")
	}
}
/**
 * 清空更多查询条件
 */
function resetMoreCon(divId){
	$("#"+divId).find("input").val('');
	var stateBtnArray = $(".stateBtn");
	$.each(stateBtnArray,function(index,item){
		$(this).removeClass("btn-primary");
		if(!$(this).hasClass("btn-default")){
			$(this).addClass("btn-default");
		}
	})
	
	var gradeBtnArray = $(".gradeBtn");
	$.each(gradeBtnArray,function(index,item){
		$(this).removeClass("btn-primary");
		if(!$(this).hasClass("btn-default")){
			$(this).addClass("btn-default");
		}
	})
	var overBtnArray = $(".overBtn");
		$.each(overBtnArray,function(index,item){
			$(this).removeClass("btn-primary");
			if(!$(this).hasClass("btn-default")){
				$(this).addClass("btn-default");
			}
		})	
	$("#state").val('');
	$("#grade").val('');
	$("#overdue").val('false');
	$("#overDiv").hide();
	
}

/**
 * 任务紧急度统计饼图
 * @param data
 */
function countTaskGrade(data){
	 // 基于准备好的dom，初始化echarts实例
 var myChart = echarts.init(document.getElementById('main'));
   var legendData = new Array()
   var seriesData = new Array()
   var dataList = data;
   if(dataList.length>0){
	   for(var i=0;i<dataList.length;i++){
		 legendData.push(dataList[i].countTypeName+"("+dataList[i].counts+")");
		 seriesData.push({value:dataList[i].counts, name:dataList[i].countTypeName+"("+dataList[i].counts+")",type:dataList[i].countType});
 	   }
   }else{
		 legendData.push("没有相关数据");
		 seriesData.push({value:0,name:"没有相关数据",type:"all"});
   }
   // 指定图表的配置项和数据
   var option = {
	    //title : {
	        //text: '某站点用户访问来源',
	        //subtext: '纯属虚构',
	        //x:'center'
	    //},
	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        orient: 'vertical',
	        left: 'left',
	        data: legendData
	    },
	    series : [
	        {
	            name: '任务',
	            type: 'pie',
	            radius : '55%',
	            center: ['50%', '60%'],
	            data:seriesData,
	            itemStyle: {
	                emphasis: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            }
	        }
	    ]
	};

   // 使用刚指定的配置项和数据显示图表。
   myChart.setOption(option);
   //点击事件绑定
   myChart.on('click', function (params) {
	   countGradeClick(params.data.type);
	  
	});
    window.onresize = myChart.resize;
	}

/**
 * 任务状态统计饼图
 * @param data
 */
function countTaskState(data){
	 // 基于准备好的dom，初始化echarts实例
 var myChart = echarts.init(document.getElementById('main'));
   var legendData = new Array()
   var seriesData = new Array()
   var dataList = data;
   if(dataList.length>0){
	   for(var i=0;i<dataList.length;i++){
		 legendData.push(dataList[i].countTypeName+"("+dataList[i].counts+")");
		 seriesData.push({value:dataList[i].counts, name:dataList[i].countTypeName+"("+dataList[i].counts+")",type:dataList[i].countType});
 	   }
   }else{
		 legendData.push("没有相关数据");
		 seriesData.push({value:0,name:"没有相关数据",type:"all"});
   }
   // 指定图表的配置项和数据
   var option = {
	    //title : {
	        //text: '某站点用户访问来源',
	        //subtext: '纯属虚构',
	        //x:'center'
	    //},
	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        orient: 'vertical',
	        left: 'left',
	        data: legendData
	    },
	    series : [
	        {
	            name: '我的待办',
	            type: 'pie',
	            radius : '55%',
	            center: ['50%', '60%'],
	            data:seriesData,
	            itemStyle: {
	                emphasis: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            }
	        }
	    ]
	};

   // 使用刚指定的配置项和数据显示图表。
   myChart.setOption(option);

   //点击事件绑定
   myChart.on('click', function (params) {
   		//2018-05-16 改动代码后所有条目都使用这部分代码
	   // 但是传入params参数中的type值不同，所以在这里做一个分流。
       if (!isNaN(Number(params.data.type))){
           countGradeClick(params.data.type);
       }else{
           countClick(params.data.type);
	   }
	});
    window.onresize = myChart.resize;
	}

//任务分解后返回的字符串
function sonObjJsonForBack(sonTask){
	if(!sonTask){
		return;
	}
	//var sonTasks = eval("("+sonTaskJson+")");
	var orderNum = $("#sonTaskRow").find("tr").length?$("#sonTaskRow").find("tr").length:0;
	var str="";
	str =str + "<tr>";
	str =str + "<td  class=\"text-center\">"+(orderNum+1)+"</td>";
	str =str + "<td>";
	str =str + "<a href=\"javascriptvoid:(0);\" class=\"taskView\" taskId=\""+sonTask.taskId+"\" >"+sonTask.taskName+"</a>";
	str =str + "</td>";
	str =str + "<td>";
	str =str + "<div class=\"ws-position\">";
	str =str + "<img class=\"user-avatar\" src=\"/downLoad/userImg/"+sonTask.comId+"/"+sonTask.executor+"?sid="+sid+"\" title=\""+sonTask.executorName+"\" />";
	str =str + "<i class=\"ws-smallName\">"+sonTask.executorName+"</i>";
	str =str + "</div>";
	str =str + "</td>";
	str =str + "<td>";
	str =str + "<div class=\"ws-position\">";
	str =str + "<img class=\"user-avatar\" src=\"/downLoad/userImg/"+sonTask.comId+"/"+sonTask.owner+"?sid="+sid+"\" title=\""+sonTask.ownerName+"\" />";
	str =str + "<i class=\"ws-smallName\">"+sonTask.ownerName+"</i>";
	str =str + "</div>";
	str =str + "</td>";
	str =str + "<td>";
	if(sonTask.state==1){
		str =str + '<input type="hidden" name="sonTaskstate" value="1"><font style="color:green;">进行中</font>';
	}else if(sonTask.state==3){
		str =str + '<input type="hidden" name="sonTaskstate" value="3"><font style="color:gray;">已暂停</font>';
	}else if(sonTask.state==4){
		str =str + '<input type="hidden" name="sonTaskstate" value="4"><font style="color:red;">已办结</font>';
	}
	str =str + "</td>";
	str =str + "</tr>";
	$("#sonTaskRow").append(str);
	//信息提示
	showNotification(1,"分解成功！");
}
//任务列表页面刷新
function reloadWhere(taskId,openPageTag){
	if(openPageTag=='index'){//首页
		openWindow.removeTodoTask(openPageTag,taskId,'003');
	}else if(openPageTag=='taskTodo'){//任务待办
		openWindow.removeTaskTodo(taskId);
		openWindow.loadTaskTodo();
	}else if(openPageTag=='allTodo'){//所有待办
		openWindow.removeTaskTodo(taskId);
		openWindow.loadOtherTodo();
	}else{//其他
		openWindow.location.reload();
	}
}
/**
 * 执行任务状态更新操作
 * @param taskId 任务主键
 * @param taskState 将要更新的状态
 * @param childAlso 子集任务是否一样的操作
 * @param openTabIndex
 * @param openPageTag
 * @param winIndex
 */
function remarkTaskState(taskId,taskState,childAlso,openTabIndex,openPageTag,winIndex){
	 $.post("/task/remarkTaskState?sid="+sid+"",{Action:"post",id:taskId,state:taskState,childAlsoRemark:childAlso,version:EasyWin.task.version},
		function (msg){
			if(msg["succ"]){
   				//window.location.reload();
				window.top.layer.msg(msg["promptMsg"],{icon: 1,skin:"showNotification",time:1800});
				window.top.layer.close(openTabIndex);//关闭窗口
   				reloadWhere(taskId,openPageTag);//任务列表页面刷新
			}else{
   				showNotification(2,"更新失败！");
			}
		} 
	,"json");
    window.top.layer.close(winIndex);
}

/**
 * 任务转为日程
 * @param taskId 任务主键用于查询任务信息
 * @param winIndex
 */
function taskConversionSchedule(sid,paramObj){
	var scheStartDate = paramObj.scheStartDate;
	var scheEndDate = paramObj.scheEndDate;
	var title = paramObj.title;
	var content = paramObj.content.replace(/<.*?>+/g, "");
	var url = '/schedule/addSchedulePage?sid='+sid+'&formatEl=0';
	if(scheStartDate){
		url = url+"&scheStartDate=" + scheStartDate;
	}
	if(scheEndDate){
		url = url+"&scheEndDate=" + scheEndDate;
	}
	if(title){
		url = url+"&title=" + encodeURIComponent(title);
	}
	if(content){
		url = url+"&content=" + encodeURIComponent(content);
	}
	var height = $(window.parent).height()-45;
	window.top.layer.open({
		  type: 2,
		  title: false,
		  closeBtn:0,
		  area: ['610px','450px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  scrollbar:false,
		  content: [url,'no'],
		  btn: ['添加日程','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.addschedle(sid,"","");
			  if(result){
				  window.top.layer.close(index);
				  showNotification(1,"添加成功");
			  }
		  }
		});
	
	
   /* $.post("/schedule/taskConversionSchedule?sid="+sid+"",{Action:"post",taskId:taskId},
        function (data){
            if(data.status == "y"){
                showNotification(1,"生成日程成功！");
                window.top.layer.msg(msg["promptMsg"],{icon: 1,skin:"showNotification",time:1800});
                window.top.layer.close(openTabIndex);//关闭窗口
                reloadWhere(taskId,openPageTag);//任务列表页面刷新
            }else{
                showNotification(2,"生成日程失败，您既不是任务负责人也不是任务办理人");
            }
        }
        ,"json");
    window.top.layer.close(winIndex);*/
}

//办结任务
function finishTask(taskId,taskState,openTabIndex,openPageTag){
	var sonTaskstates = $("input[name='sonTaskstate'][value='1'], input[name='sonTaskstate'][value='0']");
	if(taskState==1 || taskState==0){
		if(sonTaskstates && sonTaskstates[0]){
			window.top.layer.confirm("有子任务没有办结，是否将子任务也标记完成?",{icon: 3, title:'确认对话框',btn:['一起完成','仅当前任务']}, function(index){
				remarkTaskState(taskId,4,"yes",openTabIndex,openPageTag,index);//执行更新操作
			},function(index){
				remarkTaskState(taskId,4,"no",openTabIndex,openPageTag,index);//执行更新操作
			});
		}else{
			window.top.layer.confirm("确定完成任务？完成后，任务属性不允许更改。",{icon: 3, title:'确认对话框'}, function(index){
				remarkTaskState(taskId,4,"no",openTabIndex,openPageTag,index);//执行更新操作
			});
		}
	}
}
//重启任务
function restartTask(taskId,taskState){
	var sonTaskForFinish = $("input[name='sonTaskstate'][value='4']");
	var sonTaskForPause = $("input[name='sonTaskstate'][value='3']");
	if(taskState==4 || taskState==3){//只有完成或暂停状态的任务可以重启
		if(sonTaskForFinish.length>0 || sonTaskForPause.length>0){
			window.top.layer.confirm("是否子任务也一起重启？",{icon: 3, title:'确认对话框',btn:['一起重启','仅当前任务']}, function(index){
				remarkTaskState(taskId,1,"yes",null,null,index);//执行更新操作
			},function(index){
				remarkTaskState(taskId,1,"no",null,null,index);//执行更新操作
			});
		}else{
			window.top.layer.confirm("是否重新启动任务？",{icon: 3, title:'确认对话框'}, function(index){
				remarkTaskState(taskId,1,"no",null,null,index);//执行更新操作
			});
		}
	}
}
//暂停任务
function pauseTask(taskId,taskState){
	if(taskState==1 || taskState==0){//只有执行状态中的任务才能暂停
		window.top.layer.confirm("是否暂停任务？",{icon:3, title:'确认对话框'}, function(index){
			remarkTaskState(taskId,3,"yes",null,null,index);//执行更新操作,父任务暂停时，子任务也一起暂停
		});
	}
}
//任务转日程
function conversionSchedule(sid,paramObj,taskState){
    if(taskState!=4){//除开完成的任务都可以转为日程
        window.top.layer.confirm("是否转化任务为日程？",{icon:3, title:'确认对话框'}, function(index){
            taskConversionSchedule(sid,paramObj);//转化为日程
            window.top.layer.close(index);
        });
    }
}

//任务复制
function copyTask(taskId,sid,busType){
    var url = "/task/copyTaskPage?sid="+sid + "&taskId=" + taskId;
    if(EasyWin && EasyWin.task){
    	url = url+"&version="+EasyWin.task.version
    }
    openCopyTaskWindow(url,busType);
}

//合并行数据
function taskFormRowSpan(table,from){
	var trs = $(table).find("tbody").find("tr");
	if(trs && trs.get(0)){
		var preTaskId = 0;
		var rowSpan = 1;
		$.each(trs,function(index,trObj){
			var taskId = $(trObj).attr("taskId");
			if(taskId == preTaskId){
				rowSpan = rowSpan+1;
				//开始合并
				var firstTr = $(table).find("tbody").find("tr[taskId='"+taskId+"']:eq(0)");
				$(firstTr).find("td:eq(0)").attr("rowspan",rowSpan);
				$(firstTr).find("td:eq(1)").attr("rowspan",rowSpan);
				$(firstTr).find("td:eq(2)").attr("rowspan",rowSpan);
				$(firstTr).find("td:eq(3)").attr("rowspan",rowSpan);
				$(firstTr).find("td:eq(4)").attr("rowspan",rowSpan);
				if(from != 'mine'){
					$(firstTr).find("td:eq(5)").attr("rowspan",rowSpan);
				}
				
				var td0 = $(trObj).find("td:eq(0)");
				var td1 = $(trObj).find("td:eq(1)");
				var td2 = $(trObj).find("td:eq(2)");
				var td3 = $(trObj).find("td:eq(3)");
				var td4 = $(trObj).find("td:eq(4)");
				if(from != 'mine'){
					var td5 = $(trObj).find("td:eq(5)");
				}
				
				$(td0).remove();
				$(td1).remove();
				$(td2).remove();
				$(td3).remove();
				$(td4).remove();
				if(from != 'mine'){
					$(td5).remove();
				}
				
			}else{
				preTaskId = taskId;
				rowSpan = 1;
			}
		});
		
		var subIndex = 1;
		var preTaskId = 0;
		//重新索引号
		$.each(trs,function(index,trObj){
			var taskId = $(trObj).attr("taskId");
			
			if(taskId != preTaskId){
				preTaskId = taskId;
				$(trObj).find("td:eq(0)").find(".optRowNum").html(subIndex);
				subIndex = subIndex+1;
			}
		})
	}
}
//任务执行状态修改
function remarkTaskExecuteState(taskId,state){
	window.top.layer.confirm("确定执行该操作？",{icon:3, title:'确认对话框'}, function(index){
		window.top.layer.close(index);
		$.post("/task/remarkTaskExecuteState?sid="+sid,{Action:"post",id:taskId,state:state,version:2},
			function (data){
				if(data.code=='0'){
					showNotification(1,data.data);
					window.self.location.reload();
				}else{
					showNotification(2,data.data);
				}
			},"json");
	});
}
//认领任务
function acceptTask(taskId) {
	window.top.layer.confirm("确定认领任务！<br>认领任务后,将由自己办理",{icon: 3, title:'确认对话框'},function(index){
		window.top.layer.close(index);
		var acceptUrl = "/task/acceptTask";
		var param = {"sid":sid,"taskId":taskId,"version":2}
		postUrl(acceptUrl,param,function(data){
			if(data.status=='y'){
				window.self.location.reload();
			}else{
				showNotification(2,data.info);
				window.self.location.reload();
			}
		});
	})
}