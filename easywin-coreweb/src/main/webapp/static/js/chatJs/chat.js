var  inited = false;
WEB_SOCKET_SWF_LOCATION = "/static/plugins/layui/WebSocketMain.swf";
WEB_SOCKET_DEBUG = false;
var etiger = {};
etiger.socket = {
		socket:"",
		socketSid:'',
		socketComId:'',
		socketmine:'',
		init:function(sid,comId,userId,userName){
			this.socketSid = sid;
			this.socketComId = comId;
			this.socketmine = userId;
			var host = window.location.host;
			var url = "ws://"+host+"/chatRoom/"+comId+"/"+userId
			socket = new WebSocket(encodeURI(url));
			socket.onopen = function(){
				socket.send(JSON.stringify({type: 'init'}));
		    };
		    
		    socket.onmessage = function(e){
		        var msg = JSON.parse(e.data);
		        switch(msg.type) {
		            case 'init':
		                initIm(sid,comId,userId,userName);
		                return;
		            case 'addList':
		                if($('#layim-friend'+msg.data.id).length == 0 && userId != msg.data.id){
		                    return layui.layim.addList(msg.data);
		                }
		                $('#layim-friend'+msg.data.id+' img'). removeClass('gray_icon');
		                return;
		            case 'chatMessage':
		            	if(userId != msg.data.id){
		            		layui.layim.getMessage(msg.data);
		            	}
		                return;
		            case 'systemMessage':
		            	if(userId != msg.data.id){
		            		layui.layim.getMessage(msg.data);
		            	}
		            	return;
		            case 'logout':
		            case 'hide':
		            case 'online':
		                var status = msg.type;
		                change_online_status(msg.id, status);
		                return;
		        }
		    }
		},send:function(data){
			this.waitForConnection(function () {//连接建立才能发送消息
				socket.send(data);
		    }, 500);
		},
		sendData:function(data){
			this.waitForConnection(function () {
				socket.send(data);
		    }, 500);
		},
		waitForConnection : function (callback, interval) {//判断连接是否建立
			
		    if (socket.readyState === 1) {
		        callback();
		    } else {
		        var that = this;
		        setTimeout(function () {
		            that.waitForConnection(callback, interval);
		        }, interval);
		    }
		},getChatHistory:function(chatsType,toUser){
			$.ajax({
				type : "post",
				url : "/chat/initChatsHistory?rnd="+Math.random(),
				data:{sid:this.socketSid,chatsType:chatsType,fromUser:this.socketmine,toUser:toUser,comId:this.socketComId},
				dataType:"json",
				success : function(data){
					if(data.code==0){
						var history_message =data.history_message;
						for(var key in history_message){
							if(key!='in_array'){
								layui.layim.getMessage(history_message[key]);
							}
					    }
					}
				}
			});
		}
}

function initIm(sid,comId,userId,userName){
	
	layui.use('layim', function(layim){
		  layim.config({
			brief: false, //是否简约模式（如果true则不显示主面板）
		   //初始化接口
		    init: {
		        url: '/chat/getChatUserList?sid='+sid,
		        data:{comId:comId,userId:userId}
		    }
		   //查看群员接口
		    //,members: {
		       // url: 'http://laychat.workerman.net/get_members.php'
		       // url: '/registe/get_members'
		  // }

		    // 上传图片
		    //,uploadImage: {
		       // url: '/registe/upload_img'
		  //  }

		    // 上传文件
		   // ,uploadFile: {
		      //  url: '/registe/upload_file'
		   // }

		    //聊天记录地址
		    ,chatLog: '/chat/listPagedChatHistory?sid='+sid+'&comId='+comId+'&fromUser='+userId

		    ,find: ''

		    ,copyright: true //是否授权

		    ,title: userName
		    ,min: true
		    ,isfriend: true //是否开启好友（默认true，即开启）
		    ,isgroup: false //是否开启群组（默认true，即开启）
		  });
		  
		  layim.on('ready', function(res){
			  $.ajax({
					type : "post",
					url : "/chat/getChatsNoRead?rnd="+Math.random(),
					data:{sid:sid,sessionUser:userId,comId:comId},
					dataType:"json",
					success : function(data){
						if(data.code==0){
							var history_message =data.history_message;
							for(var key in history_message){
								if(key!='in_array'){
									layui.layim.getMessage(history_message[key]);
								}
						    }
						}
					}
				});
		  });
		  
		  layim.on('sendMessage', function(res){
			  if (socket != null && socket.readyState==1) {
				  var msg={ type: 'chatMessage',data: JSON.stringify(res)}
				  socket.send(JSON.stringify(msg));
			  }else{
				  setTimeout(function () {
			            that.waitForConnection(callback, interval);
			        }, interval);
			  }
		  })
		  
		  layim.on('chatChange', function(data){
		  })
		  
		  
		});
	
}