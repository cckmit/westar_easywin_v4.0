//添加车辆
function addCar(sid) {
	var url = '/car/addCarPage?sid=' + EasyWin.sid;
	openWinByRight(url);
} // 查看车辆
function viewCar(sid, carId) {
	var url = '/car/viewCarPage?sid=' + EasyWin.sid + '&carId=' + carId;
	openWinByRight(url);
} // 车辆申请
function applyCar(sid) {
	var url = "/car/carApplyPage?sid=" + EasyWin.sid;
	openWinByRight(url);
} //同意申请
function agreeApply(sid, applyId, carId, startDate, endDate) {
	postUrl('/car/checkCarApply', {
		sid: EasyWin.sid,
		carId: carId,
		startDate: startDate,
		endDate: endDate,
	},
	function(data) {
		if (data.status == 'y') {
			postUrl('/car/agreeCarApply', {
				sid: EasyWin.sid,
				id: applyId
			},
			function(data) {
				if (data.status == 'y') {
					showNotification(1, "操作成功！");
					window.location.reload()
				}
			});
		} else {
			showNotification(2, "该申请已与其他申请冲突,无法确认申请！");
		}
	});
} //查看申请
function viewApplyRecord(sid, applyId) {
	var url = "/car/viewApplyPage?sid=" + EasyWin.sid + "&carApplyId=" + applyId;
	openWinByRight(url);
} //车辆预约情况弹窗
function carStatusPage(sid) {
	window.top.layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		area: ['800px', '400px'],
		fix: false,
		//不固定
		maxmin: false,
		scrollbar: false,
		content: ["/car/carStatusPage?sid=" + EasyWin.sid, 'no'],
		cancel: function() { //右上角关闭回调
		},
		end: function(index) {}
	});
}; //拒绝申请
function voteApply(sid, applyId) {
	window.top.layer.prompt({
		formType: 2,
		area: '400px',
		closeBtn: 0,
		move: false,
		title: '拒绝车辆申请事由描述'
	},
	function(reason, index, elem) {
		if (reason) {
			postUrl('/car/voteCarApply', {
				sid: EasyWin.sid,
				id: applyId,
				voteReason: reason
			},
			function(data) {
				if (data.status == 'y') {
					showNotification(1, "操作成功！");
					window.location.reload()
				}
			});
		}
	})
} //撤回申请
function backoutApply(sid, applyId) {
	window.top.layer.confirm('确定撤回该申请？', {
		title: false,
		closeBtn: 0,
		icon: 3
	},
	function() {
		postUrl('/car/backoutApply', {
			sid: EasyWin.sid,
			id: applyId
		},
		function(data) {
			if (data.status == 'y') {
				showNotification(1, "撤回成功！");
				window.location.reload()
			}
		});
	},
	function() {});
} //归还车辆
function returnCar(sid, applyId) {
	var url = '/car/returnCarPage?sid=' + EasyWin.sid + '&carApplyId=' + applyId;
	window.top.layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		area: ['500px', '400px'],
		fix: true,
		// 不固定
		maxmin: false,
		scrollbar: false,
		move: false,
		zIndex: 1010,
		shade: 0.3,
		scrollbar: false,
		content: [url, 'no'],
		btn: ['确认', '关闭'],
		yes: function(index, layero) {
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.formSub();
		},
		end: function() {
			window.self.location.reload();
		}
	});
} //车辆预约情况弹窗
function carStatusPage(sid) {
	window.top.layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		area: ['800px', '400px'],
		fix: false,
		//不固定
		maxmin: false,
		scrollbar: false,
		content: ["/car/carStatusPage?sid=" + EasyWin.sid, 'no'],
		cancel: function() { //右上角关闭回调
		},
		end: function(index) {}
	});
}; //删除车辆
function delCar(sid, carId) {
	window.top.layer.confirm('确定要删除该车辆吗？', {
		icon: 3,
		title: '提示'
	},
	function(index) {
		postUrl('/car/delCar', {
			sid: EasyWin.sid,
			carId: carId
		},
		function(data) {
			if (data.status == 'y') {
				showNotification(1, "删除成功！");
				window.location.reload()
			} else {
				showNotification(2, data.info);
			}
		});
	});
}
/*****************************车辆维修******************************/
// 添加车辆维修
function carMaintain(sid, carId) {
	window.top.layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		area: ['500px', '400px'],
		fix: true,
		// 不固定
		maxmin: false,
		scrollbar: false,
		move: false,
		zIndex: 1010,
		shade: 0.3,
		scrollbar: false,
		content: ["/car/addMaintainRecordPage?sid=" + EasyWin.sid + "&carId=" + carId, 'no'],
		btn: ['确认', '关闭'],
		yes: function(index, layero) {
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.formSub();
		},
		end: function() {
			window.self.location.reload();
		}
	});
} // 添加车险记录
function addInsuranceRecord(sid, carId, busType) {
	window.top.layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		area: ['500px', '400px'],
		fix: true,
		// 不固定
		maxmin: false,
		scrollbar: false,
		move: false,
		zIndex: 1010,
		shade: 0.3,
		scrollbar: false,
		content: ["/car/addInsuranceRecordPage?sid=" + EasyWin.sid + "&carId=" + carId + "&busType=" + busType, 'no'],
		btn: ['确认', '关闭'],
		yes: function(index, layero) {
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.formSub();
		},
		end: function() {
			window.self.location.reload();
		}
	});
}
/*****************************车辆保险******************************/
//修改开始时间
function updateStartDate(id, sid, ts) {
	var url = "/car/updateInsurance?sid=" + EasyWin.sid;
	$.post(url, {
		Action: "post",
		id: id,
		startDate: $(ts).val()
	},
	function(data) {
		showNotification(1, "修改成功！");
	});
} //修改结束日期
function updateEndDate(id, sid, ts) {
	var url = "/car/updateInsurance?sid=" + EasyWin.sid;
	$.post(url, {
		Action: "post",
		id: id,
		endDate: $(ts).val()
	},
	function(data) {
		showNotification(1, "修改成功！");
	});
} //修改保险金额
function updateInsurancePrice(id, sid, ts, price) {
	if ($(ts).val() && isFloat($(ts).val())) {
		if (price != $(ts).val()) {
			var url = "/car/updateInsurance?sid=" + EasyWin.sid;
			$.post(url, {
				Action: "post",
				id: id,
				insurancePrice: $(ts).val()
			},
			function(data) {
				showNotification(1, "修改成功！");
			});
		}
	} else {
		layer.tips("请输入正确的数字！", $(ts), {
			tips: [1, '#3595CC'],
			time: 4000
		});
	}
} //删除记录
function delInsurance(id, sid, ts, type) {
	var url = "/car/delInsurance?sid=" + EasyWin.sid;
	window.top.layer.confirm('确定要删除该记录？', {
		icon: 3,
		title: '提示'
	},
	function(index) {
		$.post(url, {
			Action: "post",
			id: id,
			insuranceType: type
		},
		function(data) {
			showNotification(1, "删除成功！");
			$(ts).parents("tr").remove();
			if (type == '02501') {
				$.each($(".qxOrderNum"),
				function(index, item) {
					$(this).html(index + 1);
				})
			} else {
				$.each($(".syOrderNum"),
				function(index, item) {
					$(this).html(index + 1);
				})
			}
		});
	})
}
/*****************************车辆保险******************************/
/*****************************加车辆维修******************************/
//添加车辆维修
function updateCarMaintain(sid, id) {
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
		content: ["/car/updateMaintainRecordPage?sid=" + EasyWin.sid + "&id=" + id, 'no'],
		btn: ['确认', '关闭'],
		yes: function(index, layero) {
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.formSub();
		},
		end: function() {
			window.self.location.reload();
		}
	});
} //删除记录
function delCarMaintain(id, sid, ts) {
	var url = "/car/delCarMaintain?sid=" + EasyWin.sid;
	window.top.layer.confirm('确定要删除该记录？', {
		icon: 3,
		title: '提示'
	},
	function(index) {
		$.post(url, {
			Action: "post",
			id: id
		},
		function(data) {
			showNotification(1, "删除成功！");
			$(ts).parents("tr").remove();
			$.each($(".orderNum"),
			function(index, item) {
				$(this).html(index + 1);
			})
		});
	})
}
/*****************************加车辆维修******************************/

