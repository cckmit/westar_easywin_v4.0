$(function(){
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	//查询已有的综合办公管理员
	$.each($(".forceIn"),function(index,zhbObj){
		//业务类型
		var busType = $(this).attr("busType");
		 $.ajax({
			   type: "POST",
			   dataType: "json",
			   url: "/forceIn/ajaxListForceInPerson?sid="+sid+'&t='+Math.random(),
			   data: {"busType":busType},
			   success: function(data){
				   if(data.status=='f'){
					   showNotification(2,data.info);
				   }else{
					   //构建头像
					   componForceInHead(data.listFordeInPerson,busType);
				   }
			   }
		 });
	})
	
	//人员选择(修改模块管理员)
	$(".userSetBtn").on("click",function(){
		//业务类型
		var busType = $(this).parents(".forceIn").attr("busType");
		
		//人员多选
    	userMore('forceInSelect_'+busType,'',sid,'','forceInDiv_'+busType,function(result){
    		 var forceInUsers =new Array();
    		 //本次选择的管理人员
    		 if(result.length>0){
				  for ( var i = 0; i < result.length; i++) {
					  var obj = {"userId":result[i].value,"sharerName":result[i].text}
					  forceInUsers.push(obj);
				  }
    		 }
    		 //修改模块管理人员
    		 $.ajax({
				   type: "POST",
				   dataType: "json",
				   url: "/forceIn/updateForceInPersion?sid="+sid+'&t='+Math.random(),
				   data: {"forceInUserStr":JSON.stringify(forceInUsers),"busType":busType},
				   success: function(data){
		    			 	if(data.status=='f'){
		    			 		showNotification(2,data.info);
		    			 	}else{
		    			 		//构建头像数据
		    			 		componForceInHead(data.listFordeInPerson,busType);
		    			 		showNotification(1,"设置成功！");
		    			 	}
				   }
				});
    	});
    });
	//双击移除数据
	$("body").on("dblclick",".ticket-user",function(){
		//业务类型
		var busType = $(this).data("busType");
		//业务数据
		var forceIn = $(this).data("forceInData");
		//当前点击对象
		var e = $(this);
		//移除模块管理人员
		$.ajax({
			   type: "POST",
			   dataType: "json",
			   url: "/forceIn/delForceInPersion?sid="+sid+'&t='+Math.random(),
			   data:{"id":forceIn.id,
					"type":busType,
					"userId":forceIn.userId,
					"sharerName":forceIn.sharerName},
			   success: function(data){
    			 	if(data.status=='f'){
    			 		showNotification(2,data.info);
    			 	}else{
    			 		$(e).removeData("forceInData");
    			 		$(e).remove();
    					var option = $('#forceInSelect_'+busType).find("option[value='"+forceIn.userId+"']");
    			 		$(option).remove()
    			 		selected('forceInSelect_'+busType);
    			 		showNotification(1,"删除成功！");
    			 	}
			   }
			});
	})
	
	
})

//构造头像
function componForceInHead(forceIns,busType){
	$("#forceInSelect_"+busType).html("");
	$("#forceInDiv_"+busType).find(".ticket-user").remove();
	if(forceIns && forceIns.length>0){
		$.each(forceIns,function(i,obj){
			var option = $('<option selected="selected" value="'+obj.userId+'">'+obj.sharerName+'</option>');
			$("#forceInSelect_"+busType).append(option);
			//头像名称父div
			var divP = $('<div class="ticket-user pull-left other-user-box" title="双击移除"></div>');
			
			//头像
			var img = $('<img src="/downLoad/userImg/'+(obj.comId)+'/'+(obj.userId)+'" class="user-avatar">');
			//名称
			var name = $('<span class="user-name">'+obj.sharerName+'</span>')
		
			$(divP).append(img);
			$(divP).append(name);
			
			$("#forceInDiv_"+busType).find(".userSetBtn").before($(divP))
			
			$(divP).data("busType",busType);
			$(divP).data("forceInData",obj);
		})
	}
}