function getSubmit(){
			$("#searchForm").submit();
		}
		function listAttenceRecord(userId){
			var url = '/attence/listAttenceRecord?activityMenu=038&searchTab=03802&sid='+EasyWin.sid+'&userId='+userId;
			url+='&startDate='+$('#startDate').val();
			url +='&endDate='+$('#endDate').val();
			window.location.href = url;
		}
		$(function() {

			$(".subform").Validform({
				tiptype: function(msg, o, cssctl) {
					validMsg(msg, o, cssctl);
				},
				showAllError: true
			});
			
			$(".viewListLeave").click(function(){
				var creator = $(this).attr("userid");
				var userName = $(this).attr("username");
				var url = "/attence/viewListLeave?sid="+EasyWin.sid+"&spState=1&creator="+creator+"&creatorName="+userName+"&startTime="+startDate+"&endTime="+endDate+"&pager.pageSize=5";
				window.top.layer.open({
						title:false,
						closeBtn:0,
						type: 2,
						shadeClose: true,
						shade: 0.1,
						shift:0,
						zIndex:299,
						fix: true, //固定
						maxmin: false,
						move: false,
						border:1,
					  	btn:['确定','关闭'],
						area: ['600px','400px'],
						content: [url,'no'], //iframe的url
						yes:function(index,layero){
							  var iframeWin = window[layero.find('iframe')[0]['name']];
							  iframeWin.closeWin();
					  	},
					  	 success:function(layero,index){
							  },end:function(index){
						}
				 });
				
			});
			$(".viewListOverTime").click(function(){
				var creator = $(this).attr("userid");
				var userName = $(this).attr("username");
				var url = "/attence/viewListOverTime?sid="+EasyWin.sid+"&spState=1&creator="+creator+"&creatorName="+userName+"&startTime="+startDate+"&endTime="+endDate+"&pager.pageSize=5";
				window.top.layer.open({
						title:false,
						closeBtn:0,
						type: 2,
						shadeClose: true,
						shade: 0.1,
						shift:0,
						zIndex:299,
						fix: true, //固定
						maxmin: false,
						move: false,
						border:1,
					  	btn:['确定','关闭'],
						area: ['600px','400px'],
						content: [url,'no'], //iframe的url
						yes:function(index,layero){
							  var iframeWin = window[layero.find('iframe')[0]['name']];
							  iframeWin.closeWin();
					  	},
				  	 	success:function(layero,index){
					  },end:function(index){
						}
				 });
				
			});
		});
		//同步考勤记录
		function addAttence(sid){
			//检查是否配置同步信息
			getSelfJSON("/attence/checkAttenceConntSet",{sid:EasyWin.sid},function(data){
				if(data.status == 'f'){
					showNotification(2,data.info);
				}else if(data.status == 'n'){
					if(userAdmin>0){
						window.top.layer.confirm("请先配置“考勤机连接信息”！",{
							btn : [ '确定', '取消' ],
							title : '提示框',
							icon : 2
						},function(index) {
						window.location.href="/attence/attenceConnSet?pager.pageSize=10&sid="+EasyWin.sid+"&activityMenu=m_4.5";
						});
					}else{
						window.top.layer.confirm("请联系管理员配置“考勤机连接信息”！", {
					 		  btn: ['确定']//按钮
					 	  ,title:'提示框'
					 	  ,icon:2
					 	});
					}
				}else if(data.status == 'y'){
					var index = layer.load(3, {
					  shade: [0.5,'#fff']
					});
					 getSelfJSON("/attence/uploadAttenceRecord",{sid:EasyWin.sid},function(dataT){
						 layer.close(index);
						if(dataT.status == 'n'){
							showNotification(2,dataT.info);
						}else{
							showNotification(1,dataT.info);
							window.location.reload();
						}
					}); 
				}
			});
		}
		//同步考勤人员
		function addAttenceUser(sid){
			//检查是否配置同步信息
			getSelfJSON("/attence/checkAttenceConntSet",{sid:EasyWin.sid},function(data){
				if(data.status == 'f'){
					showNotification(2,data.info);
				}else if(data.status == 'n'){
					if(userAdmin>0){
						window.top.layer.confirm("请先配置“考勤机连接信息”！",{
							btn : [ '确定', '取消' ],
							title : '提示框',
							icon : 2
						},function(index) {
							window.location.href="/attence/attenceConnSet?pager.pageSize=10&sid="+EasyWin.sid+"&activityMenu=m_4.5";
						});
					}else{
						window.top.layer.confirm("请联系管理员配置“考勤机连接信息”！", {
					 		  btn: ['确定']//按钮
					 	  ,title:'提示框'
					 	  ,icon:2
					 	});
					}
				}else if(data.status == 'y'){
					var index = layer.load(3, {
					  shade: [0.5,'#fff']
					});
					 getSelfJSON("/attence/uploadAttenceUser",{sid:EasyWin.sid},function(dataT){
						 layer.close(index);
						if(dataT.status == 'n'){
							showNotification(2,dataT.info);
						}else{
							showNotification(1,dataT.info);
							window.location.reload();
						}
					}); 
				}
			});
		}