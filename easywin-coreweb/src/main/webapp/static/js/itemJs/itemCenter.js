$(function(){
	//名称筛选
	$("#itemName").blur(function(){
		//启动加载页面效果
		layer.load(0, {shade: [0.6,'#fff']});
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#itemName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#itemName").val())){
        		//启动加载页面效果
        		layer.load(0, {shade: [0.6,'#fff']});
				$("#searchForm").submit();
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#itemName").focus();
        	}
        }
    });
	//监督人员设置
	$("body").on("click","#forceInBtn",function(){
		forceIn(sid,$(this),'005')
	})
});
function searchItem(index){
 	//启动加载页面效果
 	layer.load(0, {shade: [0.6,"#fff"]});
	var url ="/item/listItemPage?sid="+sid+"&pager.pageSize=12&searchTab="+index+"&t="+Math.random();;
	if(index==11){//我的客户
		url +='&ownerType=0'
	}else if(index==12){//关注的客户
		url +='&attentionState=1'
	}else if(index==13){//下属客户
		url +='&ownerType=1'
	}else if(index==14){//已移交的客户
	}else if(index==15){//全部客户
	}else if(index==33){//进度模板配置
		url ="/itemProgress/listPagedProgressTemplate?sid="+sid+"&pager.pageSize=10&searchTab="+index+"&t="+Math.random();;
	}
	window.location.href=url;
}
//管理员设置
function settingItem(index,ts){
	//当前活动选项
	var preActive = $(".submenu").find(".active");
	//当前活动选项移除背景色
	$(preActive).removeClass();
	//配置设置背景色
	$(ts).parent().addClass("active bg-themeprimary");
	
	if(index==32){//操作权限
		modOperateConfig(sid,'005',ts,preActive)
	}
}
//添加项目
function addItem(){
	var url = "/item/addItemPage?sid="+sid;
	openWinByRight(url);
}
//查看客户
function viewItem(itemId){
	viewModInfo(itemId,'005',-1);
}

//状态筛选
function itemStateFilter(stateId){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#state").val(stateId)
	$("#searchForm").submit();
}
//责任人筛选
function userOneForUserIdCallBack(userId,userIdtag,userName,userNametag){
	$("#"+userIdtag+"_select").find("option").remove();
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	var ownerType = '${item.ownerType}';
	$("#owner").val(userId)
	if(ownerType && ownerType==0){//清除自己负责
		$("#ownerType").val('')
	}
	$("#searchForm").submit();
}

$(function(){
	$("#batchDelItem").click(function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要删除该项目吗？',{title:false,closeBtn:0,icon:3}, function(){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').submit();
			}, function(){
			});	
		}else{
			window.top.layer.msg('请先选择需要删除的项目。',{title:false,closeBtn:0,icon:7});
		}
	});
	//批量移交
	$("#batchItemHandOver").click(function(){
		if(checkCkBoxStatus('ids')){
			//获取选择值
			var ids=new Array();
			$('input[name="ids"]:checked').each(function(i){
				ids[i]=$(this).val();
            });
			window.top.layer.confirm('确定要移交吗？',{title:false,closeBtn:0,icon:3,shift:7}, function(index){
				window.top.layer.close(index);
				window.top.layer.open({
					  type: 2,
					  title: false,
					  closeBtn:0,
					  area: ['550px', '400px'],
					  fix: false, //不固定
					  maxmin: false,
					  scrollbar:false,
					  content: ["/item/itemHandOverPage?sid="+sid+"&fromUser="+sessionUserId+"&handType=batch&ids="+ids.join("&ids="),'no'],
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
			});
		}else{
		}
	});
	//操作删除和复选框
	$('.optDiv').mouseover(function(){
		var display = $(this).find(".optA").css("display");
		if(display=='none'){
			$(this).find(".optA").css("display","block");
		}
	});
	$('.optDiv').mouseout(function(){
		var optCheckBox = $(this).find(".optA");
			var check = $(optCheckBox).find(":checkbox").attr('checked');
			if(check){
				$(this).find(".optA").css("display","block");
			}else{
				$(this).find(".optA").css("display","none");
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
//合并项目
function itemCompress(sid){
	//多选
	var ckBoxs = $("#delForm").find(":checkbox[name='ids']:checked");
	if(ckBoxs.length==0){
		layer.alert('请先选择需要合并的项目。');
	}else if(ckBoxs.length==1){
		layer.alert('请先选择至少两个项目');
	}else if(ckBoxs.length>3){
		layer.alert('最多选择三个项目');
	}else{
		var ids = new Array();
		for(var i=0;i<ckBoxs.length;i++){
			ids.push($(ckBoxs[i]).val());
		}
		var url="/item/itemCompressPage?sid="+sid+"&ids="+ids;
		var height = $(window).height()-45
		layer.open({
		  type: 2,
		  //title: ['项目合并', 'font-size:18px;'],
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
//选择页面所有的项目
function checkAllItem(ckBoxElement, ckBoxName){
	var checkStatus = $(ckBoxElement).attr('checked');
	var obj = $(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']");
	if (checkStatus) {
		$(obj).attr('checked', true);
		//显示复选框
		$(obj).parent().parent().css("display","block");
		
		//隐藏查询条件
		$(".searchCond").css("display","none");
		//显示批量操作
		$(".batchOpt").css("display","block");
	} else {
		$(obj).attr('checked', false);
		//隐藏复选框
		$(obj).parent().parent().css("display","none");
		//显示查询条件
		$(".searchCond").css("display","block");
		//隐藏批量操作
		$(".batchOpt").css("display","none");
	}
}

//移除数据
function removeMyItem(itemId){
	var delDiv = $("#listItemDiv").find("[itemId="+itemId+"]");
	
	var rowObj0;
	var rowObj1;
	var rowObj2;
	
	var allRow = $("#listItemDiv").find(".row");
	
	$.each(allRow,function(index,item){
		if(index==0){
			rowObj0 = item;
			
		}else if(index==1){
			rowObj1 = item;
		}else if(index==2){
			rowObj2 = item;
		}
	});
	
	//默认所在行是第一行
	var preRow = 0;
	
	if($(rowObj1).find("[itemId="+itemId+"]").length>0){//在第二行
		preRow = 1;
	}else if($(rowObj2).find("[itemId="+itemId+"]").length>0){//在第三行
		preRow = 2;
	}
	
	$(delDiv).remove();
	
	//是否有分页
	var isPaged = $(document).find(".ps-page").length>0;
	if(isPaged){//有分页，则修改数据
		var totalRecord = parseInt($(".ps-page").find(".badge").html());
		$(".ps-page").find(".badge").html(totalRecord-1)
	}	
	
	
	if(allRow.length==1){//总共只有一行，不在页面补充数据
	}else if(allRow.length==2){//总共有两行数据
		if(preRow==0){//移除的是第一行的数据
			var row1Div = $(rowObj1).children("div:first");
			$(rowObj1).children("div:first").remove();
			$(rowObj0).append(row1Div);
		}
	}else{//总共有三行数据
		if(preRow==0){//移除的是第一行的数据
			//将第二行第一个数据填充到第一行
			var row1Div = $(rowObj1).children("div:first");
			$(rowObj1).children("div:first").remove();
			$(rowObj0).append(row1Div);
			
			//将第三行第一个数据填充到第二行
			var row2Div = $(rowObj2).children("div:first");
			$(rowObj2).children("div:first").remove();
			$(rowObj1).append(row2Div);
		}else if(preRow==1){
			//将第三行第一个数据填充到第二行
			var row2Div = $(rowObj2).children("div:first");
			$(rowObj2).children("div:first").remove();
			$(rowObj1).append(row2Div);
		}
	}
}
/**
 * 加载更多还是刷新父页面
 */
function loadOtherItem(){
	var allLen = $("#listItemDiv .optDiv").length;
	if(allLen==0){//当前页面没有没数据展示，加载数据
		var url = reConUrlByClz("#listItemDiv .optDiv",0);
		window.self.location = url;
	}else{//当前页面还有数据，则加载数据
		var pageSize = $("#searchForm #pageSize").val();
		
		var itemIds = new Array();
		$.each($("#listItemDiv").find(".optDiv"),function(index,item){
			itemIds.push($(this).attr("itemId"));
		});
		$("#searchForm").ajaxSubmit({
			type:"post",
			url:"/item/ajaxListItemPage?sid="+sid+"&t="+Math.random(),
			dataType: "json",
			data:{pageNum:nowPageNum,pageSize:pageSize,itemIds:itemIds.join(",")},
			success:function(data){
				if(data.status=='y'){
					var list = data.list;
					setItemHtml(list);
					$("body").find(".ps-page>.ps-pageText>.badge").html(data.itemNum)
				}
			},
			error:function(XmlHttpRequest,textStatus,errorThrown){
			}
		})
	}
	
}
//设置项目列表数据
function setItemHtml(list){
	if(list.length>0){
		var rowObj0;
		var rowObj1;
		var rowObj2;
		var allRow = $("#listItemDiv").find(".row");
		$.each(allRow,function(index,item){
			if(index==0){//遍历第一行元素
				rowObj0 = item;
			}else if(index==1){//遍历第二行元素
				rowObj1 = item;
			}else if(index==2){//遍历第三行元素
				rowObj2 = item;
			}
		});
		
		if(!rowObj1){//没有第二行元素，则自己添加一行
			rowObj1 = $('<div class="row margin-bottom-20"></div>');
			$("#listItemDiv").append(rowObj1);
		}
		if(!rowObj2){//没有第三行元素，则自己添加一行
			rowObj2 = $('<div class="row margin-bottom-20"></div>')
			$("#listItemDiv").append(rowObj2);
		}
		for(var i=0;i<list.length;i++){
			var lenRow0 = $(rowObj0).children().length;
			var lenRow1 = $(rowObj1).children().length;
			var lenRow2 = $(rowObj2).children().length;
			if($("#listItemDiv").find("[itemId="+list[i].id+"]").length==0){//没有展示
				var html = getItemHtml(list[i]);
				if(lenRow0<4){//第一行元素不够4个
					$(rowObj0).append(html);
				}else if(lenRow1<4){//第二行元素不够4个
					$(rowObj1).append(html);
				}else if(lenRow2<4){//第三行元素不够4个
					$(rowObj2).append(html);
				}
			}
		}
		
		var lenRow0 = $(rowObj0).children().length;
		var lenRow1 = $(rowObj1).children().length;
		var lenRow2 = $(rowObj2).children().length;
		if(lenRow2==0){//没有第三行
			$(rowObj2).remove();
		}
		if(lenRow1==0){//没有第二行
			$(rowObj1).remove();
		}
	}
	
}
function getItemHtml(itemVo){
	var html='';
	html +='\n <div class="col-lg-3 col-md-6 col-sm-6 optDiv" itemId="'+itemVo.id+'">';
	html +='\n 	<div class="widget-body no-shadow ps-project-border no-padding">';
	html +='\n 		<div class="panel-body">';
	var readState = itemVo.readState==0?"noread":"";
	html +='\n 			<a href="javascript:void(0)" class="project-box '+readState+'" onclick="readMod(this,\'item\','+itemVo.id+',\'005\');viewItem('+itemVo.id+');">';
	html +='\n 				<span class="fa fa-folder-o ps-project-icon"></span>';
	html +='\n				<p class="no-margin">'+itemVo.itemName+'</p>';
	html +='\n 				<small>'+itemVo.modifyDate.substring(0,10)+'</small>';
	html +='\n			</a>';
	html +='\n		</div>';
	html +='\n		<div class="project-box-ultrafooter clearfix">';
	html +='\n			<span class="pull-left ps-name">';
	var uuid = itemVo.uuid;
	html +='\n 			<img class="project-img-owner"  src="/downLoad/userImg/'+itemVo.comId+'/'+itemVo.owner+'?sid='+sid+'" title="'+itemVo.ownerName+'" />';
	html +='\n'+itemVo.ownerName;
	html +='\n 			</span>';
	html +='\n			<a href="javascript:void(0)" class="link pull-right margin-left-10" attentionState="'+itemVo.attentionState+'" busType="005" busId="'+itemVo.id+'" describe="0" iconSize="sizeMd" onclick="changeAtten(\''+sid+'\',this)">';
	var clz = itemVo.attentionState==0?'fa-star-o':'fa-star ws-star';
	html +='\n 				<i class="fa fa-lg sizeLg '+clz+'"></i>';
	html +='\n 			</a>';
	if (itemVo.owner==sessionUserId && itemVo.state!=4){
		html +='\n 		<a href="javascript:void(0)" class="link pull-right margin-left-10 optA" style="display:none">';
		html +='\n 			<label>';
		html +='\n 				<input type="checkbox" class="colored-blue" name="ids" value="'+itemVo.id+'">';
		html +='\n				<span class="text"></span>';
		html +='\n 			</label>';
		html +='\n		</a>';
		
	}
	html +='\n		</div>';
	html +='\n	</div>';
	html +='\n</div>';
			
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
	
}
//人员筛选
function userMoreForUserIdCallBack(options,userIdtag){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#"+userIdtag+"_select").find("option").remove();
	for(var i =0;i<options.length;i++){
		$("#"+userIdtag+"_select").append("<option selected='selected' value='"+options[i].value+"' >"+options[i].text+"</option>");
	}
	$("#searchForm").submit();
}
//移除筛选
function removeChoose(userIdtag,sid,userId){
	$("#"+userIdtag+"_select").find("option[value='"+userId+"']").remove();
	$("#searchForm").submit();
}