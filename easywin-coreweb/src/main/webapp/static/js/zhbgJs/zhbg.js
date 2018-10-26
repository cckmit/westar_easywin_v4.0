$(function() {
	$("#searchGdzcName").blur(function() {
		//启动加载页面效果
		layer.load(0, {
			shade : [ 0.6, '#fff' ]
		});
		$("#gdzcName").val($("#searchGdzcName").val());
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#searchGdzcName").bind("keydown", function(event) {
		if (event.keyCode == "13") {
			if (!strIsNull($("#searchGdzcName").val())) {
				//启动加载页面效果
				layer.load(0, {
					shade : [ 0.6, '#fff' ]
				});
				$("#gdzcName").val($("#searchGdzcName").val());
				$("#searchForm").submit();
			} else {
				showNotification(1, "请输入检索内容！");
				$("#searchGdzcName").focus();
			}
		}
	});

})

//配置
function setting(index, ts) {
	//当前活动选项
	var preActive = $(".submenu").find(".active");
	//当前活动选项移除背景色
	$(preActive).removeClass();
	//配置设置背景色
	$(ts).parent().addClass("active bg-themeprimary");
	var e = $(ts)

	if (index == 401) { //制度类型
		layer.open({
			type : 2,
			title : false,
			closeBtn : 0,
			area : [ '550px', '400px' ],
			fix : false, //不固定
			maxmin : false,
			scrollbar : false,
			content : [ "/institution/listInstituTypePage?sid=" + EasyWin.sid, 'no' ],
			cancel : function() {
				//右上角关闭回调
			},
			end : function(index) {
				//配置删除背景色
				$(ts).parent().removeClass();
				//恢复前一个选项的背景色
				$(preActive).addClass("active bg-themeprimary");
			}
		});
	} else if (index == 251) { //车辆类型
		layer.open({
			type : 2,
			title : false,
			closeBtn : 0,
			area : [ '550px', '400px' ],
			fix : false, //不固定
			maxmin : false,
			scrollbar : false,
			content : [ "/car/listCarTypePage?sid=" + EasyWin.sid, 'no' ],
			cancel : function() {
				//右上角关闭回调
			},
			end : function(index) {
				//配置删除背景色
				$(ts).parent().removeClass();
				//恢复前一个选项的背景色
				$(preActive).addClass("active bg-themeprimary");
			}
		});
	}
}
function settingGdzcType(index, ts) {
	//当前活动选项
	var preActive = $(".submenu").find(".active");
	//当前活动选项移除背景色
	$(preActive).removeClass();
	//配置设置背景色
	$(ts).parent().addClass("active bg-themeprimary");
	var e = $(ts)
		
	layer.open({
		type : 2,
		title : false,
		closeBtn : 0,
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		scrollbar : false,
		content : [ "/gdzc/listGdzcTypePage?sid=" + EasyWin.sid + "&busType=" + index, 'no' ],
		cancel : function() {
			//右上角关闭回调
		},
		end : function(index) {
			//配置删除背景色
			$(ts).parent().removeClass();
			//恢复前一个选项的背景色
			$(preActive).addClass("active bg-themeprimary");
		}
	});
}
//固定资产管理类型配置
function settingGdzcType(index, ts) {
	//当前活动选项
	var preActive = $(".submenu").find(".active");
	//当前活动选项移除背景色
	$(preActive).removeClass();
	//配置设置背景色
	$(ts).parent().addClass("active bg-themeprimary");
	layer.open({
		type : 2,
		title : false,
		closeBtn : 0,
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		scrollbar : false,
		content : [ "/gdzc/listGdzcTypePage?sid=" + EasyWin.sid + "&busType=" + index, 'no' ],
		cancel : function() {
			//右上角关闭回调
		},
		end : function(index) {
			//配置删除背景色
			$(ts).parent().removeClass();
			//恢复前一个选项的背景色
			$(preActive).addClass("active bg-themeprimary");
		}
	});
}

//处理没有管理员的状况
function handleNoAdmin(busType, callback) {
	var layerMsg = {
		"027" : {
			"title" : "办公用品库管员未设定",
			"info" : "请联系管理员,设置办公用品库管员"
		}
	};
	if (EASYWIN.userInfo.admin > 0) { //是系统管理人员
		window.top.layer.confirm(layerMsg[busType].title, {
			title : "警告",
			closeBtn : 0,
			move : false,
			btn : [ "设定", "取消" ],
			icon : 0
		}, function(index) {
			window.top.layer.close(index);
			setModAdmin(busType)
		});
	} else {
		showNotification(2, layerMsg[busType].info);
	}
}
//设置模块管理人员
function setModAdmin(busType, callback) {
	var url = "/modAdmin/editModAdminSinglePage?sid=" + EasyWin.sid;
	if (busType) {
		url = url + "&busType=" + busType
	}
	layer.open({
		type : 2,
		title : false,
		closeBtn : 0,
		//title: [title+'监督人员配置', 'font-size:18px;'],
		area : [ '550px', '400px' ],
		fix : false, //不固定
		maxmin : false,
		scrollbar : false,
		content : [ url, 'no' ],
		btn : [ '关闭' ],
		yes : function(index, layero) {
			layer.close(index)
		},
		cancel : function() {},
		success : function(layero, index) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.setWindow(window.document, window);
		}
	});
}