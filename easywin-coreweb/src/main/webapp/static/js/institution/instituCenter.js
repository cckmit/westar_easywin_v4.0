	function viewInstitu(sid, institutionId) { //制度查看
		var url = "/institution/viewInstitu?sid=" + EasyWin.sid + "&id=" + institutionId;
		openWinByRight(url);
	}
	//制度查询
	function searchInstituTab(index) {
		//启动加载页面效果
		layer.load(0, {
			shade : [ 0.6, "#fff" ]
		});
		var url = "/institution/listPagedInstitu?sid=" + EasyWin.sid + "&pager.pageSize=10&searchTab=" + index;
		window.location.href = url;
	}

	function setStyle() {
		//操作删除和复选框
		$('.ws-shareBox').mouseover(function() {
			var optSpan = $(this).find(".optSpan");
			if (optSpan) {
				$(optSpan).attr("class", "optSpan");
			}
		});
		$('.ws-shareBox').mouseout(function() {
			var optSpan = $(this).find(".optSpan");
			if (optSpan) {
				$(optSpan).attr("class", "optSpan hidden");
			}
		});
	}
	function formSub() {
		$(".searchForm").submit();
	}
	//为隐藏字段赋值
	function gets_value(index, ts, list, div) {
		$("input[name='" + div + "']").val(index);
		searchInstitu();
	}
	//添加制度
	function addInstituPage() {
		var url = "/institution/addInstituPage?sid=" + EasyWin.sid + "&rnd="
		+ Math.random()
		openWinByRight(url);
	}
	//查询制度
	function searchInstitu() {
		$("#searchForm").submit();
	}
	//为查询是否延期做准备
	function enabledInstitu(type) {
		if (2 == type) {
			type = '';
		}
		$("#enabled").attr("value", type);
		searchForm();
	}

	//删除制度
	function delInstitu() {
		if (checkCkBoxStatus('ids')) {
			window.top.layer.confirm("删除制度?", {
				btn : [ '确定', '取消' ], //按钮
				title : '询问框',
				icon : 3
			}, function(index) { //删除制度
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').submit();
			}, function() {});
		}
	}
	//排序
	function orderBy(type) {
		//启动加载页面效果
		layer.load(0, {
			shade : [ 0.6, '#fff' ]
		});
		$("#orderBy").val(type);
		$("#searchForm").submit();
	}
	//人员筛选
	function userOneForUserIdCallBack(userId, userIdtag, userName, userNametag) {
		//启动加载页面效果
		layer.load(0, {
			shade : [ 0.6, '#fff' ]
		});
		if (!strIsNull(userIdtag) && "creator" == userIdtag) {
			$("#creator").val(userId);
			$("#searchForm").submit();
		}

	}
	//类型筛选
	function instituType(type) {
		$("#instituType").val(type);
		$("#searchForm").submit();
	}
	//制度查看
	function viewInstitu(sid, institutionId) {
		var url = "/institution/viewInstitu?sid=" + EasyWin.sid + "&id=" + institutionId;
		openWinByRight(url);
	}
	//制度编辑
	function editInstitu(sid, institutionId) {
		var url = "/institution/editInstitu?sid=" + EasyWin.sid + "&id=" + institutionId;
		openWinByRight(url);
	}
	var pageTag = 'institution'
		
	$(function() {
		$("#institutionTitle").blur(function() {
			if ($("#institutionTitle").val() != $("#institutionTitle").attr("prevalue")) {
				searchInstitu();
			}
		});
		//文本框绑定回车提交事件
		$("#institutionTitle").bind("keydown", function(event) {
			if (event.keyCode == "13") {
				if (!strIsNull($("#institutionTitle").val())) {
					searchInstitu();
				} else {
					$("#institutionTitle").focus();
				}
			}
		});
	});