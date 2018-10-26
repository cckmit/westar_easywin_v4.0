//添加固定资产
function addGdzc(sid) {
	var url = '/gdzc/addGdzcPage?sid=' + EasyWin.sid;
	openWinByRight(url);
} //查看固定资产
function viewGdzc(sid, gdzcId) {
	var url = '/gdzc/viewGdzcPage?sid=' + EasyWin.sid + '&gdzcId=' + gdzcId;
	openWinByRight(url);
} //添加固定资产维修
function gdzcMaintain(sid, gdzcId) {
	window.top.layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		area: ['500px', '400px'],
		fix: true,
		//不固定
		maxmin: false,
		scrollbar: false,
		move: false,
		zIndex: 1010,
		shade: 0.3,
		scrollbar: false,
		content: ["/gdzc/addMaintainRecordPage?sid=" + EasyWin.sid + "&gdzcId=" + gdzcId, 'no'],
		btn: ['确认', '关闭'],
		yes: function(index, layero) {
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.formSub();
		},
		end: function() {
			window.self.location.reload();
		}
	});
} //添加固定资产减少纪律
function gdzcReduce(sid, gdzcId) {
	window.top.layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		area: ['500px', '400px'],
		fix: true,
		//不固定
		maxmin: false,
		scrollbar: false,
		move: false,
		zIndex: 1010,
		shade: 0.3,
		scrollbar: false,
		content: ["/gdzc/addReduceRecordPage?sid=" + EasyWin.sid + "&gdzcId=" + gdzcId, 'no'],
		btn: ['确认', '关闭'],
		yes: function(index, layero) {
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.formSub();
		},
		end: function() {
			window.self.location.reload();
		}
	});
} //状态筛选
function stateFilter(ts, value) { //启动加载页面效果
	layer.load(0, {
		shade: [0.6, '#fff']
	});
	$("#state").val(value);
	$("#searchForm").submit();
} //删除固定资产
function delGdzc(sid, gdzcId) {
	window.top.layer.confirm('确定要删除该资产吗？', {
		icon: 3,
		title: '提示'
	},
	function(index) {
		postUrl('/gdzc/delGdzc', {
			sid: EasyWin.sid,
			id: gdzcId
		},
		function(data) {
			if (data.status == 'y') {
				showNotification(1, "删除成功！");
				window.location.reload()
			}
		});
	});
} // 完成维修
function gdzcMaintainFinish(sid, gdzcId) {
	window.top.layer.confirm('该资产确定维修完成吗？', {
		icon: 3,
		title: '提示'
	},
	function(index) {
		postUrl('/gdzc/gdzcMaintainFinish', {
			sid: sid,
			id: gdzcId
		},
		function(data) {
			if (data.status == 'y') {
				showNotification(1, "操作成功！");
				window.location.reload()
			}
		});
	});
}
