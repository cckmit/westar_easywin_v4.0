$(function(){
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	//加载数据信息
	jfSubUserScopeSet.loadData();
	
	//人员选择(修改模块管理员)
	$(".userSetBtn").on("click",function(){
		var params = {"onlySubState":"1"}
		var relateSelect = $(this).attr("relateSelect");
		var _this = $(this);

		var needScore = $(_this).attr("needScore");
		//人员多选
    	userMore(relateSelect,params,sid,'',null,function(result){
    		 var subUsers =new Array();
    		 //本次选择的管理人员
    		 if(result.length>0){
				  for ( var i = 0; i < result.length; i++) {
					  var obj = {"subUserId":result[i].value,"subUserName":result[i].text};
					  obj.needScore=needScore;
					  subUsers.push(obj);
				  }
    		 }
    		 //修改模块管理人员
    		 $.ajax({
				   type: "POST",
				   dataType: "json",
				   url: "/jfMod/updateJfSubScope?sid="+sid+'&t='+Math.random(),
				   data: {"jfSubScopeStr":JSON.stringify(subUsers),"needScore":needScore},
				   success: function(data){
	    			 	if(data.status=='f'){
	    			 		showNotification(2,data.info);
	    			 	}else{
	    			 		
	    			 		var listJfSubScope = subUsers;
	  					   
	  					   $("#"+relateSelect).html('');
	  					   $("body").find(".jfSubUserScopeDiv_"+needScore).remove();
	  					   
	  					   if(listJfSubScope && listJfSubScope[0]){
	  						   $.each(listJfSubScope,function(index,jfSubUserScope){
	  							   
	  							   var option = $('<option selected="selected" value="'+jfSubUserScope.subUserId+'">'+jfSubUserScope.subUserName+'</option>');
	  							   $("#"+relateSelect).append(option);
	  							   
	  							   jfSubUserScopeSet.constrUserImg(jfSubUserScope.subUserId,jfSubUserScope.subUserName,_this);
	  						   });
	  						   
	  						   jfSubUserScopeSet.loadImg();
	  					   }
	    			 		//构建头像数据
	    			 		showNotification(1,"设置成功！");
	    			 	}
				   }
				});
    	});
    });
	//双击移除数据
	$("body").on("dblclick",".ticket-user",function(){
		//业务类型
		var subUserId = $(this).data("subUserId");
		//业务数据
		var subUserName = $(this).data("subUserName");
		var needScore = $(this).data("needScore");
		//当前点击对象
		var e = $(this);
		//移除模块管理人员
		$.ajax({
			   type: "POST",
			   dataType: "json",
			   url: "/jfMod/delJfSubUserScope?sid="+sid+'&t='+Math.random(),
			   data:{"subUserId":subUserId,
					"subUserName":subUserName,
					"needScore":needScore,
					"sid":sid},
			   success: function(data){
    			 	if(data.status=='f'){
    			 		showNotification(2,data.info);
    			 	}else{
    			 		$(e).remove();
    					var option = $('#jfSubUserScopeSelect'+(needScore=='1'?'In':'Out')).find("option[value='"+subUserId+"']")[0];
    			 		$(option).remove()
    			 		showNotification(1,"删除成功！");
    			 	}
			   }
			});
	})
	
	
})


var jfSubUserScopeSet = {
	loadData:function(){
		//业务类型
		 $.ajax({
			   type: "POST",
			   dataType: "json",
			   url: "/jfMod/ajaxListJfSubScope?sid="+sid+'&t='+Math.random(),
			   success: function(data){
				   if(data.status=='f'){
					   showNotification(2,data.info);
				   }else{
					   var listJfSubScope = data.listJfSubScope;
					   
					   $("#jfSubUserScopeSelectIn").html('');
					   $("#jfSubUserScopeSelectOut").html('');
					   $("body").find(".jfSubUserScopeDiv_0").remove();
					   $("body").find(".jfSubUserScopeDiv_1").remove();
					   
					   if(listJfSubScope && listJfSubScope[0]){
						   $.each(listJfSubScope,function(index,jfSubUserScope){
							   var option = $('<option selected="selected" value="'+jfSubUserScope.subUserId+'">'+jfSubUserScope.subUserName+'</option>');
							   $("#jfSubUserScopeSelect"+(jfSubUserScope.needScore=='1'?'In':'Out')).append(option);
							   
							   var _this = $("body").find("a[relateSelect='jfSubUserScopeSelect"+(jfSubUserScope.needScore=='1'?'In':'Out')+"']");
							   jfSubUserScopeSet.constrUserImg(jfSubUserScope.subUserId,jfSubUserScope.subUserName,_this);
						   });
						   
						   jfSubUserScopeSet.loadImg();
					   }
				   }
			   }
		 });
	},constrUserImg:function(userId,userName,_this){
		
		var needScore = $(_this).attr("needScore");
		//头像名称父div
		var divP = $('<div class="ticket-user pull-left other-user-box jfSubUserScopeDiv_'+needScore+'" title="双击移除"></div>');
		$(divP).data("subUserId",userId);
		$(divP).data("subUserName",userName);
		$(divP).data("needScore",needScore);
		//头像
		var img = $('<img src="/downLoad/userImg/'+comId+'/'+userId+'" class="user-avatar userImg">');
		$(img).attr("userId",userId);
		$(img).attr("comId",comId);
		//名称
		var name = $('<span class="user-name">'+userName+'</span>');
	
		$(divP).append(img);
		$(divP).append(name);
		
		$(_this).before($(divP));
		
	},loadImg:function(){
		//头像设置
		$.each($("body").find("img").filter(".userImg"),function(index,imgObj){
			if($(this).attr("loadState")=='1'){
				return true;
			}
			var title = $(this).attr("title");
			
			var comId = $(this).attr("comId");
			var userId = $(this).attr("userId");
			 if(comId && userId){
				var imgSrc = "/downLoad/userImg/"+comId+"/"+userId+"?sid="+sid;
				$(this).attr("src",imgSrc);
				$(this).attr("loadState","1");
			}
		})
	}
}
