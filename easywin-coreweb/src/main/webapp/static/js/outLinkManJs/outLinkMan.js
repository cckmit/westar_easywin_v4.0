

$(function(){
	//合并行
	olmFormRowSpan($("#editabledatatable"))
	//客户名筛选
	$("#linkManName").blur(function(){
		//启动加载页面效果
		layer.load(0, {shade: [0.6,'#fff']});
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#linkManName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#linkManName").val())){
        		//启动加载页面效果
        		layer.load(0, {shade: [0.6,'#fff']});
				$("#searchForm").submit();
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#linkManName").focus();
        	}
        }
    });
	
});


//删除外部联系人
function delOlm(sid, olmId) {
	window.top.layer.confirm('确定要删除该联系人吗？', {
		icon: 3,
		title: '提示'
	},
	function(index) {
		postUrl('/outLinkMan/delOlm', {
			sid: sid,
			olmId: olmId
		},
		function(data) {
			if (data.status == 'y') {
				showNotification(1, "删除成功！");
				window.location.reload()
			} else {
				showNotification(2, data.info);
			}
		});
	});
}

//批量删除外部联系人
function delOlms(){
if(checkCkBoxStatus('ids')){
	window.top.layer.confirm('确定要删除联系人吗？',{icon:3,title:'询问框'}, function(index){
		var url = reConstrUrl('ids');
		$("#delForm input[name='redirectPage']").val(url);
		$('#delForm').attr("action","/outLinkMan/delOlms");
		$('#delForm').submit();
	}, function(){
	});	
}else{
	window.top.layer.alert('请先选择要删除的联系人。',{title:false,closeBtn:0,icon:7,btn:['关闭']});
}
}


//添加外部联系人
function addOutLinMan(sid) {
	var url = '/outLinkMan/addOutLinkManPage?sid=' + sid;
	openWinByRight(url);
} 

//修改外部联系人
function editOlm(sid,id) {
	var url = '/outLinkMan/editOlmPage?sid=' + sid + '&id=' + id;
	openWinByRight(url);
}

//修改外部联系人
function viewOlm(sid,id) {
	var url = '/outLinkMan/viewOlmPage?sid=' + sid + '&id=' + id;
	openWinByRight(url);
}


//公有私有筛选
function choosePubState(obj,val){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#pubState").val(val);
	$("#searchForm").submit();
}

//合并行数据
function olmFormRowSpan(table){
	var trs = $(table).find("tbody").find("tr");
	if(trs && trs.get(0)){
		var preOlmId = 0;
		var rowSpan = 1;
		$.each(trs,function(index,trObj){
			var olmId = $(trObj).attr("olmId");
			if(olmId == preOlmId){
				rowSpan = rowSpan+1;
				//开始合并
				var firstTr = $(table).find("tbody").find("tr[olmId='"+olmId+"']:eq(0)");
				$(firstTr).find("td:eq(0)").attr("rowspan",rowSpan);
				$(firstTr).find("td:eq(1)").attr("rowspan",rowSpan);
				$(firstTr).find("td:eq(2)").attr("rowspan",rowSpan);
				$(firstTr).find("td:eq(4)").attr("rowspan",rowSpan);
				$(firstTr).find("td:eq(5)").attr("rowspan",rowSpan);
				
				var td0 = $(trObj).find("td:eq(0)");
				var td1 = $(trObj).find("td:eq(1)");
				var td2 = $(trObj).find("td:eq(2)");
				var td4 = $(trObj).find("td:eq(4)");
				var td5 = $(trObj).find("td:eq(5)");
				
				$(td0).remove();
				$(td1).remove();
				$(td2).remove();
				$(td4).remove();
				$(td5).remove();
			}else{
				preOlmId = olmId;
				rowSpan = 1;
			}
		});
		
		var subIndex = 1;
		var preOlmId = 0;
		//重新索引号
		$.each(trs,function(index,trObj){
			var olmId = $(trObj).attr("olmId");
			
			if(olmId != preOlmId){
				preOlmId = olmId;
				$(trObj).find("td:eq(0)").find(".optRowNum").html(subIndex);
				subIndex = subIndex+1;
			}
		})
	}
}

