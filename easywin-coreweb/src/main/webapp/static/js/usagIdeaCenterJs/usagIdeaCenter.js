//页面加载初始化
$(function(){
	//任务名筛选
	$("#searchTaskName").blur(function(){
		$("#taskName").val($("#searchTaskName").val());
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#searchTaskName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#searchTaskName").val())){
				$("#taskName").val($("#searchTaskName").val());
				$("#searchForm").submit();
        	}else{
        		showNotification(2,"请输入检索内容！");
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
});
//添加常用意见
function add(sid){
	 tabIndex = layer.open({
	  type: 2,
	  title:false,
	  closeBtn:0,
	  //title: ['+常用意见', 'font-size:18px;'],
	  area: ['500px', '350px'],
	  fix: false, //不固定
	  maxmin: false,
	  scrollbar:false,
	  content: ["/usagIdea/addUsagIdeaPage?sid="+sid+"&rnd=" + Math.random(),'no'],
	  btn: ['确定', '取消'],
	  yes: function(index, layero){
		  var iframeWin = window[layero.find('iframe')[0]['name']];
		  var tag = iframeWin.addIdeaForm();
		  if(tag){
			window.location.reload();   
		  }
	  }
	});
}
//修改常用意见
function update(id,sid){
	tabIndex = layer.open({
	  type: 2,
	  title:false,
	  closeBtn:0,
	  //title: ['常用意见维护', 'font-size:18px;'],
	  area: ['500px', '350px'],
	  fix: false, //不固定
	  maxmin: false,
	  scrollbar:false,
	  content: ["/usagIdea/updateUsagIdeaPage?sid="+sid+"&id="+id+"&rnd=" + Math.random(),'no'],
	  btn: ['确定', '取消'],
	  yes: function(index, layero){
		  var iframeWin = window[layero.find('iframe')[0]['name']];
		  var tag = iframeWin.updateIdeaForm();
		  if(tag){
			window.location.reload();   
		  }
	  }
	});
}
//批量删除分组
function del(){
	if(checkCkBoxStatus('ids')){
		window.top.layer.confirm('确定要删除意见吗？', {icon: 3, title:'提示'}, function(index){
		  $("#delForm input[name='redirectPage']").val(window.location.href);
		  $('#delForm').submit();
		  window.top.layer.close(index);
		});
	}else{
		window.top.layer.msg('请先选择需要删除的常用意见', {icon: 5});
	}
}