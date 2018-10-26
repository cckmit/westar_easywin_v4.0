$(function() {
	// 操作删除和复选框
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

	$(":checkbox[name='ids'][disabled!='disabled']")
			.click(
					function() {
						var checkLen = $(":checkbox[name='ids'][disabled!='disabled']:checked").length;
						var len = $(":checkbox[name='ids'][disabled!='disabled']").length;
						if (checkLen > 0) {
							if (checkLen == len) {
								$("#checkAllBox").attr('checked', true);
							} else {
								$("#checkAllBox").attr('checked', false);
							}
						} else {
							$("#checkAllBox").attr('checked', false);
						}
					});
});

$(function() {
	// 项目名筛选
	$("#content").blur(function() {
		$("#searchForm").submit();
	});
	// 文本框绑定回车提交事件
	$("#content").bind("keypress", function(event) {
		if (event.keyCode == "13") {
			if (!strIsNull($("#content").val())) {
				$("#searchForm").submit();
			}
		}
	});
	// 彻底删除数据
	$("#delAll").click(
			function() {
				var url = "/recycleBin/optAllObject";
				layui.use('layer', function(){
					var layer = layui.layer;
					window.top.layer.confirm('删除后数据不可恢复，确定要删除？', {
						btn : [ '确定', '取消' ]// 按钮
					,
					title : '询问框',
					icon : 3
					}, function(index) {
						window.top.layer.close(index);
						//全部删除后，页面跳转到第一页
						var urlRec = urlPageFirst();
						$("#searchForm input[name='redirectPage']").val(urlRec);
						$("#searchForm").find('#state').val(1);
						$('#searchForm').attr("action", url);
						$('#searchForm').submit();
					});
				});
			});
	// 彻底删除数据
	$("#delChoose").click(
			function() {
				layui.use('layer', function(){
					var layer = layui.layer;
					
					if (checkCkBoxStatus('ids')) {
						window.top.layer.confirm('删除后数据不可恢复，确定要删除？', {
							btn : [ '确定', '取消' ]// 按钮
						,
						title : '询问框',
						icon : 3
						}, function(index) {
							window.top.layer.close(index);
							//删除选中的数据，页面跳转设置页码是否向前跳
							var urlRec = reConstrUrl('ids');
							$("#delForm input[name='redirectPage']").val(urlRec);
							$("#delForm").find('#state').val(1);
							$('#delForm').attr("action", "/recycleBin/optObject")
							$('#delForm').submit();
						});
					} else {
						layer.alert('请先选择一条数据。');
					}
				});
			});
	// 还原数据
	$("#recoverChoose").click(
			function() {
				layui.use('layer', function(){
					var layer = layui.layer;
					
					if (checkCkBoxStatus('ids')) {
						window.top.layer.confirm('确定要还原数据？', {
							btn : [ '确定', '取消' ]// 按钮
						,
						title : '询问框',
						icon : 3
						}, function(index) {
							window.top.layer.close(index);
							//删除选中的数据，页面跳转设置页码是否向前跳
							var urlRec = reConstrUrl('ids');
							
							$("#delForm input[name='redirectPage']").val(urlRec);
							$("#delForm").find('#state').val(0);
							$('#delForm').attr("action", "/recycleBin/optObject")
							$('#delForm').submit();
						});
					} else {
						layer.alert('请先选择一条数据。');
					}
				});
				
			});
	// 还原数据
	$("#recoverAll").click(
			function() {
				layui.use('layer', function(){
					var layer = layui.layer;
					
					var url = "/recycleBin/optAllObject";
					window.top.layer.confirm('确定要还原数据？', {
						btn : [ '确定', '取消' ]// 按钮
					,
					title : '询问框',
					icon : 3
					}, function(index) {
						window.top.layer.close(index);
						//全部删除后，页面跳转到第一页
						var urlRec = urlPageFirst();
						$("#searchForm input[name='redirectPage']").val(urlRec);
						$("#searchForm").find('#state').val(0);
						$('#searchForm').attr("action", url);
						$('#searchForm').submit();
					});
				});
			});
	// 更多筛选条件显示层
	$("#moreFilterCondition").click(function() {
		var display = $("#moreFilterCondition_div").css("display");
		if ("none" == display) {
			$(this).html('隐藏');
			$("#moreFilterCondition_div").animate( {
				height : 'toggle',
				opacity : 'toggle'
			}, 320);
		} else if ("block" == display) {
			$(this).html('更多筛选')
			$("#moreFilterCondition_div").animate( {
				height : 'toggle',
				opacity : 'toggle'
			}, 320);
		}

	});
	var timeoutProcess;
	// 鼠标移到modTypeDiv区域时，显示下拉菜单
	$("#modTypeDiv").mouseover(function(e) {
		evt = window.event || e;
		var obj = evt.toElement || evt.relatedTarget;
		var pa = this;
		if (pa.contains(obj)) {
			clearTimeout(timeoutProcess);
			return false;
		}
	});
	// 鼠标移出modTypeDiv区域时，隐藏下拉菜单
	$("#modTypeDiv").mouseout(function(e) {
		evt = window.event || e;
		var obj = evt.toElement || evt.relatedTarget;
		var pa = this;
		if (pa.contains(obj)) {
			return false;
		}
		timeoutProcess = setTimeout(function() {
			var pObj = $("#modTypeA").parent();
			$(pObj).removeClass("open");
		}, 200)
	});
	// 点击<li>的时候不考虑checkbox
	$("#modTypeId li").click(
			function(e) {
				var evt = e ? e : window.event;
				var ele = evt.srcElement || evt.target;
				var type = ele.type;
				if (type && type.toLowerCase() == 'checkbox') {
					var id = ele.id;
					if (id && id == 'allModType') {
						setIsCheckAll('searchForm', 'allModType', 'modTypeA',
								'modTypeId', 'modTypes');
					} else {
						var checkbox = $(this).find("input[type='checkbox']");
						if (!$(checkbox).attr("checked")) {
							$("#allModType").attr("checked", false);
						}
					}
					return;
				} else {
					$("#modTypeA").parent().addClass("open");
					var checkbox = $(this).find("input[type='checkbox']");
					if ($(checkbox).attr("id")) {// 点击的是全选
						if ($(checkbox).attr("checked")) {// 复选框是全选已选中，则取消选中
							$(checkbox).attr("checked", false);
						} else {
							$(checkbox).attr("checked", true);
						}
						setIsCheckAll('searchForm', 'allModType', 'modTypeA',
								'modTypeId', 'modTypes');
					} else {
						if ($(checkbox).attr("checked")) {
							$(checkbox).attr("checked", false);
							$("#allModType").attr("checked", false);
						} else {
							$(checkbox).attr("checked", true);
						}
					}
				}
			})
	// modTypeId区域移除后若有变动查询
	$("#modTypeId").mouseout(
			function(e) {
				evt = window.event || e;
				var obj = evt.toElement || evt.relatedTarget;
				var pa = this;
				if (pa.contains(obj))
					return false;
				var diff = 0;
				var array = $("#searchForm").find(
						":checkbox[name='modTypes']:checked");
				// 所选项
				var modTypes = new Array();
				if (array && array.length > 0) {
					for ( var i = 0; i < array.length; i++) {
						modTypes.push($(array[i]).val());
						if (!$(array[i]).attr("prechecked")) {// 本次有上次没有选择的
							diff += 1;
						}

					}
				}
				var arrayPre = $("#searchForm").find(
						":checkbox[name='modTypes'][prechecked='1']");
				// 所选项
				var modTypePre = new Array();
				if (arrayPre && arrayPre.length > 0) {
					for ( var i = 0; i < arrayPre.length; i++) {
						if (!$(array[i]).attr("checked")) {// 本次有上次没有选择的
							diff += 1;
						}
					}
				}
				if (diff > 0) {
					submitForm();
				} else {
					$("#modTypeA").parent().removeClass("open");
				}
			});
});
/**
 * 选择日期
 */
function submitForm() {
	$("#searchForm").submit();
}