$(function(){
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	//查询已有的综合办公管理员
	$.each($(".zhbg"),function(index,zhbObj){
		//业务类型
		var busType = $(this).attr("busType");
		 $.ajax({
			   type: "POST",
			   dataType: "json",
			   url: "/modAdmin/ajaxListModAdmin?sid="+sid+'&t='+Math.random(),
			   data: {"busType":busType},
			   success: function(data){
				   if(data.status=='f'){
					   showNotification(2,data.info);
				   }else{
					   //构建头像
					  componModAdmin(data.listModAdmin,busType);
				   }
			   }
		 });
	})
	
	//人员选择(修改模块管理员)
	$(".userSetBtn").on("click",function(){
		//业务类型
		var busType = $(this).parents(".zhbg").attr("busType");
		
		//人员多选
    	userMore('zhbgSelect_'+busType,'',sid,'','zhbgDiv_'+busType,function(result){
    		 var modAdminUsers =new Array();
    		 //本次选择的管理人员
    		 if(result.length>0){
				  for ( var i = 0; i < result.length; i++) {
					  var obj = {"userId":result[i].value,"adminName":result[i].text}
					  modAdminUsers.push(obj);
				  }
    		 }
    		 //修改模块管理人员
    		 $.ajax({
				   type: "POST",
				   dataType: "json",
				   url: "/modAdmin/updateModAdmin?sid="+sid+'&t='+Math.random(),
				   data: {"modAdminStr":JSON.stringify(modAdminUsers),"busType":busType},
				   success: function(data){
		    			 	if(data.status=='f'){
		    			 		showNotification(2,data.info);
		    			 	}else{
		    			 		//构建头像数据
		    			 		componModAdmin(data.listModAdmin,busType);
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
		var modAdmin = $(this).data("modAdmin");
		//当前点击对象
		var e = $(this);
		//移除模块管理人员
		$.ajax({
			   type: "POST",
			   dataType: "json",
			   url: "/modAdmin/delModAdmin?sid="+sid+'&t='+Math.random(),
			   data:{"id":modAdmin.id,
					"busType":busType,
					"userId":modAdmin.userId,
					"adminName":modAdmin.adminName},
			   success: function(data){
    			 	if(data.status=='f'){
    			 		showNotification(2,data.info);
    			 	}else{
    			 		$(e).removeData("modAdmin");
    			 		$(e).remove();
    					var option = $('zhbgSelect_'+busType).find("option[value='"+modAdmin.userId+"']")[0];
    			 		$(option).remove()
    			 		selected('zhbgSelect_'+busType);
    			 		showNotification(1,"删除成功！");
    			 	}
			   }
			});
	})
	
	
})

//构造头像
function componModAdmin(listModAdmin,busType){
	$("#zhbgSelect_"+busType).html("");
	$("#zhbgDiv_"+busType).find(".ticket-user").remove();
	if(listModAdmin && listModAdmin.length>0){
		$.each(listModAdmin,function(i,obj){
			var option = $('<option selected="selected" value="'+obj.userId+'">'+obj.adminName+'</option>');
			$("#zhbgSelect_"+busType).append(option);
			//头像名称父div
			var divP = $('<div class="ticket-user pull-left other-user-box" title="双击移除"></div>');
			//头像
			var img = $('<img src="/downLoad/userImg/'+obj.comId+'/'+obj.userId+'" class="user-avatar">');
			//名称
			var name = $('<span class="user-name">'+obj.adminName+'</span>')
		
			$(divP).append(img);
			$(divP).append(name);
			
			$("#zhbgDiv_"+busType).find(".userSetBtn").before($(divP))
			
			$(divP).data("busType",busType);
			$(divP).data("modAdmin",obj);
		})
	}
}