function viewAnnoun(sid, announId) {//公告查看
		var url = "/announcement/viewAnnoun?sid=" + sid + "&id=" + announId;
		openWinByRight(url);
	}
//公告编辑
function editAnnoun(sid, announId) {
	var url = "/announcement/editAnnoun?sid=" + sid + "&id=" + announId;
	openWinByRight(url);
}
//公告查询
function searchAnnounTab(index) {
	//启动加载页面效果
	layer.load(0, {
		shade : [ 0.6, "#fff" ]
	});
	var url = "/announcement/listPagedAnnoun?sid=" + EasyWin.sid+ "&pager.pageSize=10&searchTab=" + index;
	if (index == 11) {//所有
	} else if (index == 12) {//我的
		url += "&searchAll=11&creator=" + EasyWin.userInfo.userId;
	} else if (index == 13) {//关注
		url += "&attentionState=1";
	}
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
	searchAnnoun();
}
//添加公告
function addAnnounPage() {
	var url = "/announcement/addAnnounPage?sid=" + EasyWin.sid + "&rnd="
			+ Math.random()
	openWinByRight(url);
}
//查询公告
function searchAnnoun() {
	$("#searchForm").submit();
}
//为查询是否延期做准备
function enabledAnnoun(type) {
	if (2 == type) {
		type = '';
	}
	$("#enabled").attr("value", type);
	searchForm();
}

//删除公告
function delAnnoun() {
	if (checkCkBoxStatus('ids')) {
		window.top.layer.confirm("删除公告?", {
			btn : [ '确定', '取消' ],//按钮
			title : '询问框',
			icon : 3
		}, function(index) {//删除公告
			var url = reConstrUrl('ids');
			$("#delForm input[name='redirectPage']").val(url);
			$('#delForm').submit();
		}, function() {
		});
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
//重要程度筛选
function announGradeFilter(grade, ts) {
	if ($(ts).hasClass("btn-primary")) {//点击的是去掉
		$(ts).removeClass("btn-primary");//不选中
		if (!$(ts).hasClass("btn-default")) {
			$(ts).addClass("btn-default");
		}
		$("#grade").val('');
	} else {//点击的是选择
		var gradeBtnArray = $(".gradeBtn");
		$.each(gradeBtnArray, function(index, item) {
			$(this).removeClass("btn-primary");//不选中
			if (!$(this).hasClass("btn-default")) {//没有默认颜色，则添加一个
				$(this).addClass("btn-default");
			}
		})
		$(ts).addClass("btn-primary");
		$("#grade").val(grade);
	}
}
//类型筛选
function announType(type) {
	$("#type").val(type);
	$("#searchForm").submit();
}
//公告查看
function viewAnnoun(sid, announId) {
	var url = "/announcement/viewAnnoun?sid=" + sid + "&id=" + announId;
	openWinByRight(url);
}

var pageTag = 'announ'
$(function() {
	$("#announTitle").blur(function() {
		if ($("#announTitle").val() != $("#announTitle").attr("prevalue")) {
			searchAnnoun();
		}
	});
	//文本框绑定回车提交事件
	$("#announTitle").bind("keydown", function(event) {
		if (event.keyCode == "13") {
			if (!strIsNull($("#announTitle").val())) {
				searchAnnoun();
			} else {
				$("#announTitle").focus();
			}
		}
	});
	setStyle();

	//操作删除和复选框
	$('.optTr').mouseover(function() {
		var display = $(this).find(".optTd .optCheckBox").css("display");
		if (display == 'none') {
			$(this).find(".optTd .optCheckBox").css("display", "block");
			$(this).find(".optTd .optRowNum").css("display", "none");
		}
	});
	$('.optTr ').mouseout(function() {
		var optCheckBox = $(this).find(".optTd .optCheckBox");
		var check = $(optCheckBox).find("input").attr('checked');
		if (check) {
			$(this).find(".optTd .optCheckBox").css("display", "block");
			$(this).find(".optTd .optRowNum").css("display", "none");
		} else {
			$(this).find(".optTd .optCheckBox").css("display", "none");
			$(this).find(".optTd .optRowNum").css("display", "block");
		}
	});

});
$(function() {
	$(":checkbox[name='ids'][disabled!='disabled']")
			.click(
					function() {
						var checkLen = $(":checkbox[name='ids'][disabled!='disabled']:checked").length;
						var len = $(":checkbox[name='ids'][disabled!='disabled']").length;
						if (checkLen > 0) {
							//隐藏查询条件
							$(".searchCond").css("display", "none");
							//显示批量操作
							$(".batchOpt").css("display", "block");
							if (checkLen == len) {
								$("#checkAllBox").attr('checked', true);
							} else {
								$("#checkAllBox").attr('checked', false);
							}
						} else {
							//显示查询条件
							$(".searchCond").css("display", "block");
							//隐藏批量操作
							$(".batchOpt").css("display", "none");

							$("#checkAllBox").attr('checked', false);
						}
					});
});

//窗体点击事件检测
document.onclick = function(e) {
	var evt = e ? e : window.event;
	var ele = $(evt.srcElement || evt.target);
	if ($(ele).parents("#moreCondition_Div").length == 1) {
		if ($(ele).prop("tagName").toLowerCase() == 'a'
				|| $(ele).parent("a").length == 1) {
			return false;
		} else {
			if (!$("#moreCondition_Div").hasClass("open")) {
				$(".searchCond").find(".open").removeClass("open");
				$("#moreCondition_Div").addClass("open");
			}
		}
	} else {
		$("#moreCondition_Div").removeClass("open");
	}
}
/**
 * 展示查询条件中更多
 */
function displayMoreCond(divId) {
	if ($("#" + divId).hasClass("open")) {
		$("#" + divId).removeClass("open");
	} else {
		$("#" + divId).addClass("open")
	}
}
/**
 * 清空更多查询条件
 */
function resetMoreCon(divId) {
	$("#" + divId).find("input").val('');
	$("#grade").val('');
	$(".gradeBtn").removeClass("btn-primary");
}