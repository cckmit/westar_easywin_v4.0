<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<head runat="server">
		<title>Ext.form.field.HtmlEditor示例</title>
		<link href="/static/plugins/ext/resources/css/ext-all.css" rel="stylesheet" type="text/css" />
		<script src="/static/plugins/ext/bootstrap.js" type="text/javascript"></script>
		<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="/static/plugins/artDialog/jquery.artDialog.js?skin=aero"></script>
		<script type="text/javascript" src="/static/plugins/artDialog/plugins/iframeTools.js"></script>
		<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript">
		var websocket;
		var output;
		//用于展示用户的聊天信息
		Ext.define('MessageContainer', {
			extend : 'Ext.view.View',
			trackOver : true,
			multiSelect : false,
			itemCls : 'l-im-message',
			itemSelector : 'div.l-im-message',
			overItemCls : 'l-im-message-over',
			selectedItemCls : 'l-im-message-selected',
			style : {
				overflow : 'auto',
				backgroundColor : '#fff'
			},
			tpl : [
					'<div class="l-im-message-warn" style="text-align:center;display:none" id="showMoreMsg"><a href="javascript:void(0)" onclick="getHistoryMsg()">查看更多</a></div>',
					'<tpl for=".">',
					'<div class="l-im-message">',
					'<div class="l-im-message-header l-im-message-header-{source}" style="font-size:15px">{from}  {timestamp}</div>',
					'<div class="l-im-message-body" style="margin-top:5px;margin-bottom:10px;margin-left:10px">{content}</div>', '</div>',
					'</tpl>'],

			messages : [],
			

			initComponent : function() {
				var me = this;
				me.messageModel = Ext.define('Leetop.im.MessageModel', {
							extend : 'Ext.data.Model',
							fields : ['from', 'timestamp', 'content', 'source','isToday','station']
						});
				me.store = Ext.create('Ext.data.Store', {
							model : 'Leetop.im.MessageModel',
							data : me.messages
						});
				me.callParent();
			},

			//将服务器推送的信息展示到页面中
			receive : function(message) {
				var me = this;
				var isToday = message['isToday'];
				if(isToday && isToday==0){
					message['timestamp'] = Ext.Date.format(new Date(message['timestamp']),
							'Y/n/j H:i:s');
				}else{
					message['timestamp'] = Ext.Date.format(new Date(message['timestamp']),
							'H:i:s');
				}
				var user="${userInfo.userName}"
				if(message.from == user){
					message.source = 'self';
				}else{
					message.source = 'remote';
				}
				var station = message['station'];
				if(station && station=='0'){
					me.store.insert(0,message);
					var minId = $("#minId").val();
					if (me.el.dom) {
						if(minId==0){
							me.el.dom.scrollTop = me.el.dom.scrollHeight;
						}else{
							me.el.dom.scrollTop = 0;
						}
					}
				}else{
					me.store.add(message);
					if (me.el.dom) {
						me.el.dom.scrollTop = me.el.dom.scrollHeight;
					}
				}
			}
		});
				
		Ext.onReady(function() {
			Ext.define("reMod", {
                extend: "Ext.data.Model",
                fields: [
                    { name: "modIndex" },
                    { name: "modType" },
                    { name: "modName" }
                ]
            });
			var store = Ext.create("Ext.data.Store", {
                model: "reMod",
                data: [
                    { "modIndex": 0, "modType": "0", "modName": "不关联模块" },
                    { "modIndex": 1, "modType": "003", "modName": "任务模块" },
                    { "modIndex": 2, "modType": "005", "modName": "项目模块" },
                    { "modIndex": 3, "modType": "012", "modName": "客户模块" },
                    { "modIndex": 4, "modType": "007", "modName": "周报模块" }
                ]
            });
			Ext.define("Ext.ux.XHtmlEditor",{
		        extend:"Ext.form.field.HtmlEditor",
		        alias:"widget.xhtmleditor",
		        initComponent : function() {
					this.callParent(arguments);
					var me = this;
					
					var disabled = false;
					if('${chatRoom.busType}'!=0){
						disabled = true;
					}
		            var select = Ext.create('Ext.form.FormPanel', {
						region : 'north',
						items: [{
		                    xtype: "combo",
		                    id:"Province",
		                    name: "Province",
		                    store: store,
		                    displayField: "modName",
		                    valueField: "modType",
		                    queryMode: "local",
		                    disabled:true,
		                    value: "${chatRoom.busType}" // 默认选中
		                }]
					});
		          //  me.getToolbar().add(select);
		            
				}
			});
			var input = Ext.create('Ext.ux.XHtmlEditor', {
				region : 'south',
				height : 120,
				enableFont : false,//字体
				enableSourceEdit : false,//不编辑
				enableAlignments : false,//不设置对齐
				enableLinks: false,//链接不可用
				enableLists: false,//不使用编号
				listeners : {
					initialize : function() {
						Ext.EventManager.on(me.input.getDoc(), {
									keyup : function(e) {
										if (e.ctrlKey === true
												&& e.keyCode == 13) {
											e.preventDefault();
											e.stopPropagation();
											send();
										}
									}
								});
					}
				}
			});
			if (!window.WebSocket) {
				input.disabled=true;
			}
			//创建消息展示容器
			output = Ext.create('MessageContainer', {
						region : 'center'
					});
			var login = new Ext.Button({
				   text : "邀请人员",
				   id:"loginBar",
				   handler : function() {
					   if (websocket != null && websocket.readyState==1) {
						   invUser('${userInfo.comId}','${chatRoom.id}','${userInfo.id}','${userInfo.userName}','${chatRoom.busId}','${chatRoom.busType}');
					   }else{
						   Ext.Msg.alert('提示', '您已经掉线，不能邀请人员!'); 
					   }
				    }
				  });
			var settting = new Ext.Button({
				   text : "设置",
				   id:"settingBar",
				   handler : function() {
				    }
				  });
			var dialog = Ext.create('Ext.panel.Panel', {
				region : 'center',
				layout : 'border',
				id:'dialogA',
				items : [input,output],
				buttons:[{text:"发送",handler : send}],
				tbar : [login,settting]
			});
			var loginBar = Ext.getCmp("loginBar");
			var settingBar = Ext.getCmp("settingBar");
			settingBar.hide();
			
			//初始话WebSocket
			function initWebSocket() {
				if (window.WebSocket) {
					var host = window.location.host;
					var url = "ws://"+host+"/chatRoom/${userInfo.comId}/${chatRoom.id}/${userInfo.id}/${userInfo.userName}/${chatRoom.busId}/${chatRoom.busType}?sid=${param.sid}"
					websocket = new WebSocket(encodeURI(url));
					websocket.onopen = function() {
						//连接成功
						win.setTitle(title + '&nbsp;&nbsp;(已连接)');
					}
					websocket.onclose = function() {
						//连接断开
						win.setTitle(title + '&nbsp;&nbsp;(已经断开连接)');
					}
					websocket.onerror = function() {
						//连接失败
						win.setTitle(title + '&nbsp;&nbsp;(连接发生错误)');
					}
					//消息接收
					websocket.onmessage = function(message) {
						var message = JSON.parse(message.data);
						//接收用户发送的消息
						if (message.type == 'message') {
							output.receive(message);
						} else if (message.type == 'get_online_user') {
							//获取在线用户列表
							var root = onlineUser.getRootNode();
							root.removeAll();
							removeOptions('users');
							Ext.each(message.list,function(user){
								var text = user.name;
								if(user.online==0){
									text = "<font color=red>"+user.name+"</font>";
								}
								var node = root.createNode({
									id : user.id,
									text : text,
									iconCls : 'user',
									leaf : true
								});
								root.appendChild(node);
								$('#users').append(
										"<option selected='selected' value='"
												+ user.id + "'>" + user.name
												+ "</option>");
							});
						} else if (message.type == 'user_join') {
							//用户上线
							var root = onlineUser.getRootNode();
							var user = message.user;
							var oldNode = root.findChild('id',user.id);
							if(oldNode){
								var newNode = root.createNode({
									id : user.id,
									text : user.name,
									iconCls : 'user',
									leaf : true
								});
								
								root.replaceChild(newNode,oldNode)
							}else{
								var node = root.createNode({
									id : user.id,
									text : user.name,
									iconCls : 'user',
									leaf : true
								});
								root.appendChild(node);
							}
						} else if (message.type == 'user_leave') {
							//用户下线
							var root = onlineUser.getRootNode();
							var user = message.user;
							var oldNode = root.findChild('id',user.id);
							
							var newNode = root.createNode({
								id : user.id,
								text : "<font color=red>"+user.name+"</font>",
								iconCls : 'user',
								leaf : true
							});
							
							root.replaceChild(newNode,oldNode)
						}
					}
				}else{
					//浏览器不支持
					$.ajax({
						 type : "post",
						  url : "/chat/delTodayWork?sid=${param.sid}&rnd="+Math.random(),
						  dataType:"json",
						  data:{roomId:${chatRoom.id}},
						  success : function(data){
							  win.setTitle(title + '&nbsp;&nbsp;(浏览器不支持)');
						  }
					});
					
					//获取在线用户列表
					var root = onlineUser.getRootNode();
					root.removeAll();
					removeOptions('users');
					var userList = '${userList}';
					Ext.each(JSON.parse(userList).list,function(user){
						var text = user.name;
						if(user.online==0){
							text = "<font color=red>"+user.name+"</font>";
						}
						var node = root.createNode({
							id : user.id,
							text : text,
							iconCls : 'user',
							leaf : true
						});
						root.appendChild(node);
						$('#users').append(
								"<option selected='selected' value='"
										+ user.id + "'>" + user.name
										+ "</option>");
					});
				}
			};
			//在线用户树
			var onlineUser = Ext.create('Ext.tree.Panel', {
				title : '聊天室成员',
				rootVisible : false,
				region : 'east',
				width : 150,
				lines : false,
				useArrows : true,
				autoScroll : true,
				split : true,
				iconCls : 'user-online',
				store : Ext.create('Ext.data.TreeStore', {
					root : {
						text : '聊天室成员',
						expanded : true,
						children : []
					}
				})
			});
			var title = '欢迎您：';
			var userInfo = '${userInfo}';
			var uuid = '${userInfo.smImgUuid}'
			title += '		<img src="/downLoad/userImg/${userInfo.comId}/${userInfo.id}?sid=${param.sid}" width=20 height=20></img>\n';
			title+='${userInfo.userName}'
			//展示窗口
			var win = Ext.create('Ext.window.Window', {
				title : title + '&nbsp;&nbsp;<b>(未连接)</b>',
				layout : 'border',
				iconCls : 'user-win',
				minWidth : 650,
				modal: true,
				minHeight : 460,
				width : 650,
				x:0,y:0,
				maximizable: false,
				//minimizable: true,
				//resizable:false,         //不允许改变大小
				//collapsible:true,            //允许折叠
				animateTarget : 'websocket_button',
				height : 460,
				items:[dialog,onlineUser],
				border : false,
				draggable:false,
				listeners : {
					render : function() {
						initWebSocket();
					},
					close:function(){
						if(null!=websocket){
							websocket.close();
						}
						art.dialog.close();
						websocket = null;
					}
				}
			});

			win.show();
			getHistoryMsg()
			//发送消息
			function send() {
				var message = {};
				if (websocket != null && websocket.readyState==1) {
					var val = input.getValue();
					if (val && val.length>0) {
						Ext.apply(message, {
									from : '${userInfo.userName}',
									content : val,
									timestamp : new Date().getTime(),
									type : 'message'
								});
						websocket.send(JSON.stringify(message));
						input.setValue('');
					}else{
						Ext.Msg.alert('提示', '消息内容不能为空!');
					}
				} else {
					Ext.Msg.alert('提示', '您已经掉线，无法发送消息!');
				}
			}
		});
		//关闭聊天
		function closeChat(){
			if(null!=websocket){
				websocket.close();
			}
			websocket = null;
		}
		//取得历史消息
		function getHistoryMsg(){
			var minId = $("#minId").val();
			if(minId==-1){
				return;
			}
			$.ajax({
				 type : "post",
				  url : "/chat/ajaxListMsg?sid=${param.sid}&rnd="+Math.random(),
				  dataType:"json",
				  data:{
					  roomId:${chatRoom.id},
					  minId:minId,
					  busId:${chatRoom.busId},
					  busType:${chatRoom.busType},
					  chatType:${chatRoom.chatType}
				  },
				  beforeSend: function(XMLHttpRequest){
					  $("#showMoreMsg").css("display","none");
		        },
		        success : function(data){
		        	if(data.status=='y'){
		        		var list = data.list;
		        		for(var i=0;i<list.length;i++){
		        			var chatRoomA = list[i];
							var message = {};
							Ext.apply(message, {
								from: chatRoomA.userName,
								content:chatRoomA.chatContent,
								timestamp: new Date(Date.parse(chatRoomA.recordCreateTime.replace(/-/g,"/"))).getTime(),
								isToday:chatRoomA.isToday,
								station:'0',
								type: 'message'
							});
							output.receive(message);
		        		}
		        		
		        		$("#minId").val(data.newMinId);
		        		if(data.count>0){
        					$("#showMoreMsg").css("display","block")
        				}else{
        					$("#showMoreMsg").css("display","none")
        				}
		        	}
		        }
			});
		}
		//邀请人员
		function invUser(comId,roomId,userId,userName,busId,budType){
			art.dialog.data('userid', userId);
			art.dialog.data('queryParam', null);
			
			var selectobj = document.getElementById("users");
			var o = selectobj.options;
			art.dialog.data('o', o);
			
			art.dialog.open('/chat/userMorePage?sid=${param.sid}', {
				title : '人员多选',
				lock : true,
				max : false,
				min : false,
				width : 750,
				height : 440,
				close : function() {

				},
				ok : function() {
					var iframe = this.iframe.contentWindow;
					if (!iframe.document.body) {
						return false;
					}
					iframe.returnUser();
					var options = art.dialog.data('options');
					var userIds =new Array();
					var countUser = 0;
					for ( var i = 0; i < options.length; i++) {
						userIds.push(options[i].value);
						if(!options[i].disabled){
							$('#users').append(
									"<option selected='selected' value='"
											+ options[i].value + "'>" + options[i].text
											+ "</option>");
							countUser = countUser+1;
						}
					}
					if(countUser>0){
						userIds.sort(sortNumber);
						invUserMsg(userIds.join(","))
					}
					return true;
				},
				cancel : true,
				button : [ {
					name : '清空',
					callback : function() {
						var iframe = this.iframe.contentWindow;
						if (!iframe.document.body) {
							return false;
						}
						;
						iframe.initSelect('userselect');
						return false;
					}
				} ]
			});
		}
		
		//发送消息
		function invUserMsg(userIds) {
			var message = {};
			if (websocket != null) {
				Ext.apply(message, {
					from: '${userInfo.userName}',
					content: userIds,
					timestamp: new Date().getTime(),
					type: 'userInv'
				});
				websocket.send(JSON.stringify(message));
			} else {
				Ext.Msg.alert('提示', '您已经掉线，无法发送消息!');
			}
		}
</script>
	</head>
	<body>
	<input type="hidden" id="minId" value="0"/>
	<div id="websocket_button"></div> 
	<div style="display:none">
		<select id="users" multiple="multiple">
		</select>
	</div>
	</body>
</html>
