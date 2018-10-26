$(function(){
	if(PageData.tab==22 || PageData.activityMenu=='m_4.2'){
		//查询放假
		$("#holiBtn").on("click",function(){
			$("#searchForm").find("input[name='status']").val(1);
			$("#searchForm").submit();
		});
		//查询换班
		$("#workBtn").on("click",function(){
			$("#searchForm").find("input[name='status']").val(2);
			$("#searchForm").submit();
		})
		//节日维护添加
		$("#addFestBtn").on("click",function(){
			window.top.layer.open({
		 		  type: 2,
		 		  title:false,
		 		  closeBtn:0,
		 		  area: ['550px', '450px'],
		 		  fix: true, //不固定
		 		  maxmin: false,
		 		  scrollbar:false,
		 		  move: false,
		 		  content:['/festMod/addFestModPage?sid='+sid+'&t='+Math.random(),'no'],
		 		  success:function(layero,index){
		 			 var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		 			 
					  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
						  window.top.layer.close(index);
					  })
					  $(iframeWin.document).find("#saveFestModBtn").on("click",function(){
						 if(iframeWin.checkForm()){
							 var result =  iframeWin.saveFestMod();
							 window.location.reload();
							 window.top.layer.close(index);
						 }
					  })
		 		  }
		 	 });
		})
		
		$(document).on("click",".optUpdateBtn",function(){
			var comId = $(this).attr("comId");
			var dataId = $(this).attr("dataId");
			if(comId>0){
				window.top.layer.open({
			 		  type: 2,
			 		  title:false,
			 		  closeBtn:0,
			 		  area: ['550px', '450px'],
			 		  fix: true, //不固定
			 		  maxmin: false,
			 		  scrollbar:false,
			 		  move: false,
			 		  content:['/festMod/updateFestModPage?sid='+sid+'&festModId='+dataId+'&t='+Math.random(),'no'],
			 		  success:function(layero,index){
			 			 var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			 			 
						  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
							  window.top.layer.close(index);
						  })
						  $(iframeWin.document).find("#updateFestModBtn").on("click",function(){
							 if(iframeWin.checkForm()){
								 var result =  iframeWin.updateFestMod();
								 window.location.reload();
								 window.top.layer.close(index);
							 }
						  })
			 		  }
			 	 });
			}
		})
		$(document).on("click",".optDelBtn",function(){
			var comId = $(this).attr("comId");
			var dataId = $(this).attr("dataId");
			if(comId>0){
				window.top.layer.confirm('确定要删除本条数据吗？', {icon: 3, title:'提示'}, function(index){
					$.ajax({
				        type:"post",
				        url:"/festMod/delFestMod?sid="+sid+"&t="+Math.random(),
				        dataType: "json",
				        data:{"comId":comId,"festModId":dataId},
				        success:function(data){
				        	 var status = data.status;
					         if(status == 'y'){
					        	 window.location.reload();
								 window.top.layer.close(index);
						     }else{
						    	 showNotification(2,data.info);
						     }
				        },
				        error:function(XmlHttpRequest,textStatus,errorThrown){
				        	window.top.layer.alert("系统错误，请联系管理人员",{icon:7,title:false,closeBtn:0,btn:["关闭"]})
				        	return false;
				        	
				        }
				    });
				});
			}
		});
		
	}
})
