$(function(){
	//客户名筛选
	$("#customerName").blur(function(){
		//启动加载页面效果
		layer.load(0, {shade: [0.6,'#fff']});
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#customerName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#customerName").val())){
        		//启动加载页面效果
        		layer.load(0, {shade: [0.6,'#fff']});
				$("#searchForm").submit();
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#customerName").focus();
        	}
        }
    });
	$(function(){
		$("body").on("click","#forceInBtn",function(){
			forceIn(sid,$(this),'012')
		})
	})
});
//查询客户信息
function searchCrm(index){
 	//启动加载页面效果
 	layer.load(0, {shade: [0.6,"#fff"]});
	var url = '/crm/customerListPage?sid='+sid+'&searchTab='+index+"&t="+Math.random();
	if(index==11){//我的客户
		url +="&ownerType=0"
		
	}else if(index==12){//关注的客户
		url +="&attentionState=1"
	}else if(index==13){//下属客户
		url +="&ownerType=1"
	}else if(index==14){//已移交的客户
	}else if(index==15){//全部客户
	}else if(index==16){
		url +="&orderBy=customerType"
	}
	window.location.href=url;
}
//统计客户信息
function statisticsCrm(index,statisticsType){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,"#fff"]});
	var url = '/statistics/crm/statisticsCrmPage?sid='+sid+'&searchTab='+index+"&t="+Math.random();
	url +="&statisticsType="+statisticsType;
	window.location.href=url;
}
//客户类型筛选
function customerTypeFilter(obj,typeId){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#customerTypeId").val(typeId);
	if(!typeId){
		$("#crmType_select").find("option").remove();
	}
	$("#searchForm").submit();
}
//客户阶段筛选
function customerStageFilter(obj,stage){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#stage").val(stage);
	$("#searchForm").submit();
}

//客户区域筛选
function artOpenerCallBack(idAndType){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#areaIdAndType").val(idAndType)
	$("#searchForm").submit();
}
//责任人筛选
function userOneForUserIdCallBack(userId,userIdtag,userName,userNametag){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#"+userIdtag+"_select").find("option").remove();
	$("#owner").val(userId);
	var owmerType = '${customer.ownerType}';
	if(owmerType && owmerType==0){
		$("#ownerType").val('')
	}
	$("#searchForm").submit();
}
//添加客户
function addCustomer(){
	var url = "/crm/addCustomerPage?sid="+sid;
	openWinByRight(url);

}
//查看客户
function viewCustomer(customerId){
	viewModInfo(customerId,'012',-1);
}
//客户基本信息配置
function settingCrm(index,ts){
	
	//当前活动选项
	var preActive = $(".submenu").find(".active");
	//当前活动选项移除背景色
	$(preActive).removeClass();
	//配置设置背景色
	$(ts).parent().addClass("active bg-themeprimary");
	
	if(index==31){//客户类型维护
		layer.open({
			  type: 2,
			  title: false,
			  closeBtn:0,
			  area: ['600px', '400px'],
			  fix: false, //不固定
			  maxmin: false,
			  scrollbar:false,
			  content: ["/crm/listCustomerTypePage?sid="+sid,'no'],
			  cancel: function(){ 
			    //右上角关闭回调
			  },end:function(index){
				//配置删除背景色
				  $(ts).parent().removeClass();
				  //恢复前一个选项的背景色
				  $(preActive).addClass("active bg-themeprimary");
			  }
			});
	}else if(index==32){//反馈类型维护
		layer.open({
			  type: 2,
			  title: false,
			  closeBtn:0,
			  area: ['550px', '400px'],
			  fix: false, //不固定
			  maxmin: false,
			  scrollbar:false,
			  content: ["/crm/listFeedBackTypePage?sid="+sid,'no'],
			  cancel: function(){ 
			    //右上角关闭回调
			  },end:function(index){
				//配置删除背景色
				  $(ts).parent().removeClass();
				  //恢复前一个选项的背景色
				  $(preActive).addClass("active bg-themeprimary");
			  }
			});
	}else if(index==33){//区域维护
		layer.open({
			  type: 2,
			  title: false,
			  closeBtn:0,
			  //title: ['客户区域维护', 'font-size:18px;'],
			  area: ['550px', '500px'],
			  fix: false, //不固定
			  maxmin: false,
			  btn:['关闭'],
			  scrollbar:false,
			  content: ["/crm/editAreaPage?sid="+sid,'no'],
			  cancel: function(){ 
			    //右上角关闭回调
			  },end:function(index){
				//配置删除背景色
				  $(ts).parent().removeClass();
				  //恢复前一个选项的背景色
				  $(preActive).addClass("active bg-themeprimary");
			  }
			});
	}else if(index==34){//监督设置
		
	}else if(index==35){//操作权限
		modOperateConfig(sid,'012',ts,preActive)
	}else if(index==36){//客户阶段
		layer.open({
			  type: 2,
			  title: false,
			  closeBtn:0,
			  area: ['550px', '400px'],
			  fix: false, //不固定
			  maxmin: false,
			  scrollbar:false,
			  content: ["/crm/listCrmStage?sid="+sid,'no'],
			  cancel: function(){ 
			    //右上角关闭回调
			  },end:function(index){
				//配置删除背景色
				  $(ts).parent().removeClass();
				  //恢复前一个选项的背景色
				  $(preActive).addClass("active bg-themeprimary");
			  }
			});
	}else if(index==38){//客户变更审批配置window.location.href=url;
		layer.open({
			type: 2,
			title:false,
			closeBtn:0,
			area: ['500px', '300px'],
			fix: false, //不固定
			maxmin: false,
			scrollbar:false,
			content: ["/moduleChangeExamine/modChangeExamPage?sid="+sid+"&moduleType=012",'no'],
			btn: ['确定', '关闭'],
			yes: function(index, layero){
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.formSub();
				return false;
			},btn2:function(index){
				
			},cancel: function(){ 
				
			},end:function(index){
				//配置删除背景色
				$(ts).parent().removeClass();//恢复前一个选项的背景色
				$(preActive).addClass("active bg-themeprimary");
			}
		});	
	}else if(index==39){//模块配置
		layer.load(0, {shade: [0.6,"#fff"]});
		var url = "/moduleChangeExamine/modConfigPage?sid="+sid+"&moduleType=012"+'&searchTab='+index+"&t="+Math.random();
		window.location.href=url;
	}
}

$(function(){
	$("#batchDelCrm").click(function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要删除选中客户吗？',{title:false,closeBtn:0,icon:3}, function(){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').submit();
			}, function(){
			});	
		}else{
			window.top.layer.msg('请先选择需要删除的客户。',{title:false,closeBtn:0,icon:7});
		}
	});
	//批量移交
	$("#batchCrmHandOver").click(function(){
		if(checkCkBoxStatus('ids')){
			//获取选择值
			var ids=new Array();
			$('input[name="ids"]:checked').each(function(i){
				ids[i]=$(this).val();
            });
			window.top.layer.confirm('确定要移交吗？',{title:false,closeBtn:0,icon:3,shift:7}, function(index){
				window.top.layer.close(index);
				
				//查询相关配置
				$.ajax({
		      		type : "post",
		      		url : "/moduleChangeExamine/ajaxGetModChangeExam?sid="+sid + "&moduleType=012&field=owner",
		      		dataType:"json",
		      		traditional :true, 
		      		data:null,
		      		  beforeSend: function(XMLHttpRequest){
		               },
		      		  success : function(data){
		      			if(data.lists && data.lists[0] && data.needExam == "y"){//需要审批
		      				var url = '/moduleChangeExamine/moreChangeApplyPage?sid='+sid
		      						+'&field=owner&moduleType=012&ids='+ids.join("&ids=");
							window.top.layer.open({
								type: 2,
								title:false,
								closeBtn:0,
								area: ['550px', '515px'],
								fix: false, //不固定
								maxmin: false,
								scrollbar:false,
								content: [url,'no'],
								btn: ['提交','关闭'],
								yes: function(index, layero){
									var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
									var flag = iframeWin.formSub();
									if(flag){
										window.top.layer.close(index);
										showNotification(1,"申请成功");
										//本业面全部数据已移交，跳转到前一页
										var url = reConstrUrl('ids');
										window.self.location = url;
									  }
								  },cancel: function(){
								  },success:function(layero,index){
									  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
									  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
										  window.top.layer.close(index);
									  })
								  }
								});
						}else{
							window.top.layer.open({
								  type: 2,
								  title: false,
								  closeBtn:0,
								  area: ['550px', '400px'],
								  fix: false, //不固定
								  maxmin: false,
								  scrollbar:false,
								  content: ["/crm/customerHandOverPage?sid="+sid+"&handType=batch&ids="+ids.join("&ids="),'no'],
								  btn: ['移交', '关闭'],
								  yes: function(index, layero){
									  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
									  var flag = iframeWin.formSub();
									  if(flag){
										  window.top.layer.close(index);
										  
										  //本业面全部数据已移交，跳转到前一页
										  var url = reConstrUrl('ids');
										  window.self.location = url;
									  }
								  }
								});
						}
		      		  },
		      		  error:  function(XMLHttpRequest, textStatus, errorThrown){
		      			  window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
		      	      }
		      	}); 
				
				
			});
		}else{
			window.top.layer.msg('请选择需要移交的客户。',{title:false,closeBtn:0,icon:7});
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
});
//客户合并
function crmCompress(sid){
	//多选
	var ckBoxs = $("#delForm").find(":checkbox[name='ids']:checked");
	if(ckBoxs.length==0){
		layer.alert('请先选择需要合并的客户。');
	}else if(ckBoxs.length==1){
		layer.alert('请先选择至少两个客户');
	}else if(ckBoxs.length>3){
		layer.alert('最多选择三个客户');
	}else{
		var ids = new Array();
		for(var i=0;i<ckBoxs.length;i++){
			ids.push($(ckBoxs[i]).val());
		}
		var url="/crm/crmCompressPage?sid="+sid+"&ids="+ids;
		var height = $(window).height()-45
		layer.open({
		  type: 2,
		  //title: ['客户合并', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  shadeClose: false,
		  shade: 0.3,
		  shift:0,
		  scrollbar:false,
		  fix: true, //固定
		  maxmin: false,
		  move: false,
		  area: ['800px',height+'px'],
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


function checkAllCrm(ckBoxElement, ckBoxName){
	var checkStatus = $(ckBoxElement).attr('checked');
	var obj = $(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']");
	if (checkStatus) {
		$(obj).attr('checked', true);
		//隐藏查询条件
		$(".searchCond").css("display","none");
		//显示批量操作
		$(".batchOpt").css("display","block");
	} else {
		$(obj).attr('checked', false);
		//显示查询条件
		$(".searchCond").css("display","block");
		//隐藏批量操作
		$(".batchOpt").css("display","none");
	}
}
//移除数据
function removeMyCrm(crmId){
	$("#crmListUl").find("input[type='checkbox'][value='"+crmId+"']").parents("li").remove();
	//是否有分页
	var isPaged = $(document).find(".ps-page").length>0;
	if(isPaged){//有分页，则修改数据
		var totalRecord = parseInt($(".ps-page").find(".badge").html());
		$(".ps-page").find(".badge").html(totalRecord-1)
	}
}

/**
 * 异步加载客户数据
 */
function loadCrm(){
	var allLen = $("#crmListUl li").length;
	if(allLen==0){
		var url = reConUrlByClz("#crmListUl li",0);
		window.self.location = url;
	}else{
		var pageSize = $("#searchForm #pageSize").val();
		
		var crmIds = new Array();
		$.each($("#crmListUl").find("input[type='checkbox']"),function(index,item){
			crmIds.push($(this).val());
		});
		$("#searchForm").ajaxSubmit({
			type:"post",
			url:"/crm/ajaxCustomerList?sid="+sid+"&t="+Math.random(),
			dataType: "json",
			data:{pageNum:nowPageNum,pageSize:pageSize,crmIds:crmIds.join(",")},
			success:function(data){
				if(data.status=='y'){
					var list = data.list;
					var html = getCrmHtml(list);
					$("#crmListUl").append(html);
					$("body").find(".ps-page>.ps-pageText>.badge").html(data.crmNum)
					
				}
			},
			error:function(XmlHttpRequest,textStatus,errorThrown){
			}
		});
	}
	
	
}
/**
 * 补充待办集合
 * @param list
 * @returns {String}
 */
function getCrmHtml(list){
	var html = '';
	if(list.length>0){
		//需要添加的行数
		var allShowNum = 15-$("#crmListUl li").length;
		for(var i=0;i<list.length;i++){
			if(allShowNum==0){//行数已满，不在添加
				break;
			}
			var obj = list[i];
			var modLen =  $("#crmListUl").find("input[type='checkbox'][value='"+obj.id+"']").length;
			if(modLen>0){
				continue;
			}
			allShowNum--;
			
			html +='\n <li class="item first-item">';
			html +='\n 	<div class="message-content">';
			
			var uuid = obj.uuid;
			html +='\n <img class="message-head" width="50" height="50" src="/downLoad/userImg/'+obj.comId+'/'+obj.owner+'?sid='+sid+'" title="'+obj.ownerName+'" />';
			html +='\n 		<div class="content-headline">';
			html +='\n 			<div class="checkbox pull-left ps-chckbox">';
			html +='\n 				<label>';
			html +='\n 					<input type="checkbox" class="colored-blue" value="'+obj.id+'" name="ids" '+(sessionUserId==obj.owner)?"disabled":""+'>';
			html +='\n 					<span class="text"></span>';
			html +='\n 				</label>';
			html +='\n 			</div>';
			var readState = obj.isRead==0?"noread":"";
			html +='\n 			<a href="javascript:void(0)" class="item-box '+readState+'" onclick="readMod(this,\'crm\','+obj.id+',\'012\');viewCustomer('+obj.id+')">';
			html +='\n 				'+obj.customerName;
			html +='\n 			</a>';
			html +='\n 		</div>';
			html +='\n 		<a href="javascript:void(0)" class="item-box '+readState+'" onclick="readMod(this,\'crm\','+obj.id+',\'012\');viewCustomer('+obj.id+')">';
			html +='\n 			<div class="content-text">';
			html +='\n 			'+obj.modifyContent;
			html +='\n 			</div>';
			html +='\n 		</a>';
			html +='\n 		<div class="item-more">';
			html +='\n 			<span class="label label-default">'+obj.typeName+'</span>';
			html +='\n 			<span class="label label-default">'+obj.areaName+'</span>';
			html +='\n 			<a class="btn btn-default btn-xs" style="cursor: pointer;" attentionState="'+obj.attentionState+'" busType="012" busId="'+obj.id+'" describe="1" iconSize="sizeMd" onclick="changeAtten(\''+sid+'\',this)">';
			if(obj.attentionState==0){
				
				html +='\n 			<i class="fa fa-star-o"></i>关注';
			}else{
				html +='\n 			<i class="fa fa-star ws-star"></i>取消';
			}
			html +='\n 			</a>';
			html +='\n 			<span class="time"><i class="fa fa-clock-o"></i>更新时间：'+obj.modifyTime+'</span>';
			html +='\n 			<span class="time"><i class="fa fa-user"></i>更新人：'+obj.modifierName+'</span>';
			html +='\n 			<span class="time"><i class="fa fa-clock-o"></i>创建于：'+obj.recordCreateTime+'</span>';
			html +='\n 		</div>';
			html +='\n 	</div>';
			html +='\n </li>';
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
	$("#frequeType").val('');
	
}
//客户更新频率查询
function crmFrequeFilter(stateId,ts){
	if($(ts).hasClass("btn-primary")){//点击的是去掉
		$(ts).removeClass("btn-primary");//不选中
		if(!$(ts).hasClass("btn-default")){
			$(ts).addClass("btn-default");
		}
		$("#frequeType").val('');
	}else{//点击的是选择
		var stateBtnArray = $(".stateBtn");
		$.each(stateBtnArray,function(index,item){
			$(this).removeClass("btn-primary");//不选中
			if(!$(this).hasClass("btn-default")){//没有默认颜色，则添加一个
				$(this).addClass("btn-default");
			}
		})
		$(ts).addClass("btn-primary");
		$("#frequeType").val(stateId);
	}
}
//人员筛选
function userMoreForUserIdCallBack(options,userIdtag){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#"+userIdtag).val('');
	$("#"+userIdtag+"_select").find("option").remove();
	for(var i =0;i<options.length;i++){
		$("#"+userIdtag+"_select").append("<option selected='selected' value='"+options[i].value+"' >"+options[i].text+"</option>");
	}
	$("#searchForm").submit();
}
//移除筛选
function removeChoose(tag,sid,value){
	$("#"+tag+"_select").find("option[value='"+value+"']").remove();
	$("#searchForm").submit();
}
//客户类型筛选
 function crmTypeMoreTreeCallBack(options,tag){
 	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#"+tag).val('');
	$("#"+tag+"_select").find("option").remove();
	for(var i =0;i<options.length;i++){
		$("#"+tag+"_select").append("<option selected='selected' value='"+options[i].value+"' >"+options[i].text+"</option>");
	}
	$("#searchForm").submit();
 }