$.extend({
	/**
	 * 1. 设置cookie的值,把name变量的值设为value example $.cookie('name', 'value');
	 * 
	 * 2.新建一个cookie 包括有效期 路径 域名等 example $.cookie('name', 'value',
	 * {expires: 7, path: '/', domain: 'jquery.com', secure: true});
	 * 
	 * 3.新建cookie example $.cookie('name', 'value');
	 * 
	 * 4.删除一个cookie example $.cookie('name', null);
	 * 
	 * 5.取一个cookie(name)值给account example var account= $.cookie('name');
	 * 
	 */
	cookie : function(name, value, options) {
		if (typeof value != 'undefined') { // name and value given, set
			// cookie
			options = options || {};
			if (value == null) {
				value = '';
				options.expires = -1;
			}
			var expires = '';
			if (options.expires
					&& (typeof options.expires == 'number' || options.expires.toUTCString)) {
				var date;
				if (typeof options.expires == 'number') {
					date = new Date();
					date.setTime(date.getTime()
							+ (options.expires * 24 * 60 * 60 * 1000));
				} else {
					date = options.expires;
				}
				expires = '; expires=' + date.toUTCString(); // use
				// expires
				// attribute,
				// max-age
				// is
				// not
				// supported
				// by IE
			}
			var path = options.path ? '; path=' + options.path : '';
			var domain = options.domain ? '; domain=' + options.domain
					: '';
			var secure = options.secure ? '; secure' : '';
			document.cookie = [ name, '=', encodeURIComponent(value),
					expires, path, domain, secure ].join('');
		} else { // only name given, get cookie
			var cookieValue = null;
			if (document.cookie && document.cookie != '') {
				var cookies = document.cookie.split(';');
				for ( var i = 0; i < cookies.length; i++) {
					var cookie = jQuery.trim(cookies[i]);
					// Does this cookie string begin with the name we
					// want?
					if (cookie.substring(0, name.length + 1) == (name + '=')) {
						cookieValue = decodeURIComponent(cookie
								.substring(name.length + 1));
						break;
					}
				}
			}
			return cookieValue;
		}
	}
});
$(function(){
	//查看模块数据
	$("body").on("click",".viewModInfo",function(){
		var busId = $(this).attr("busId");
		var busType = $(this).attr("busType");
		viewModInfo(busId,busType);
	})
})

/**
 * 获得当前窗口的顶级窗口
 */
function getTopWin() {
	var win = window;
	if (window.top != window.self) {
		win = window.top;
	}
	return win;
}

/**
 * 数组内是否包含自定字符串
 * 
 * @param e
 * @returns {Boolean}
 */
Array.prototype.in_array = function(e) {
	for (i = 0; i < this.length && this[i] != e; i++)
		;
	return !(i == this.length);
}

/**
 * 复选框全选
 * 
 * @param ckBoxElement
 * @param ckBoxName
 * @return
 */
function checkAll(ckBoxElement, ckBoxName) {
	var checkStatus = $(ckBoxElement).attr('checked');
	if (checkStatus) {
		$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', true);
	} else {
		$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', false);
	}
}

/**
 * 检查复选框是否至少有一个被选中
 * 
 * @param ckBoxName
 * @return
 */
function checkCkBoxStatus(ckBoxName) {
	var ckFlag = false;
	var ckBoxs = $(":checkbox[name='" + ckBoxName + "']");
	if (ckBoxs != null) {
		for ( var i = 0; i < ckBoxs.length; i++) {
			if ($(ckBoxs[i]).attr('checked')) {
				ckFlag = true;
				break;
			}
		}
	}
	return ckFlag;
}


/**
 * 系统通知
 * 
 * @param icon
 * @param content
 * @return
 */
function showNotification(icon, content) {
	layui.use('layer', function(){
		var layer = layui.layer;
		if(icon){
			window.top.layer.msg(content,{icon: icon,skin:"showNotification",time:1800});
		}else{
			window.top.layer.msg(content,{time:1800});
			
		}
	});
}

/**
 * 查找表单控件的所在的TD（用于验证）
 * 
 * @param p
 * @param tagname
 * @return
 */
function getParent(p, tagname) {
	while (p.get(0).tagName != "TD") {
		p = p.parent();
	}
	return p;
}

/**
 * 表单验证提示
 */
function validMsg(msg, o, cssctl) {
	if (!o.obj.is("form")) {// 验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;
		var objtip = o.obj.siblings(".Validform_checktip");
		if (o.type == 3) {
			setTimeout(function() {
				objtip.show();
			}, 100);
		} else {
			objtip.hide();
		}
		cssctl(objtip, o.type);
		objtip.text(msg);
	}
}

/**
 * 机构单选
 */
function orgOne(orgIdElementId, orgPathNameElementId, queryParam, sid) {
	art.dialog.data('orgId', $('#' + orgIdElementId).val());
	art.dialog.data('orgPathName', $('#' + orgPathNameElementId).val());
	art.dialog.data('queryParam', queryParam);
	art.dialog.open('/common/orgOnePage?sid=' + sid, {
		title : '机构单选',
		lock : true,
		max : false,
		min : false,
		width : 350,
		height : 400,
		close : function() {
			$('#' + orgIdElementId).val(art.dialog.data('orgId'));
			$('#' + orgPathNameElementId).val(art.dialog.data('orgPathName'));
			$('#' + orgPathNameElementId).focus();
			$('#' + orgPathNameElementId).blur();
		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			iframe.returnOrg();
			return false;
		},
		cancel : true
	});
}

/**
 * 部门单选
 * @param orgIdElementId
 * @param orgPathNameElementId
 * @param queryParam
 * @param sid
 */
function depOne(orgIdElementId, orgPathNameElementId, queryParam, sid) {
	art.dialog.data('orgId', $('#' + orgIdElementId).val());
	art.dialog.data('orgName', $('#' + orgPathNameElementId).val());
	art.dialog.data('queryParam', queryParam);
	art.dialog.open('/common/depOnePage?sid=' + sid, {
		title : '部门单选',
		lock : true,
		max : false,
		min : false,
		width : 350,
		height : 400,
		close : function() {
			$('#' + orgIdElementId).val(art.dialog.data('orgId'));
			$('#' + orgPathNameElementId).val(art.dialog.data('orgName'));
			$('#' + orgPathNameElementId).focus();
			$('#' + orgPathNameElementId).blur();
		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			;
			iframe.returnOrg();
			return false;
		},
		cancel : true
	});
}
/**
 * 区域单选
 * @param orgIdElementId
 * @param orgPathNameElementId
 * @param queryParam
 * @param sid
 * @param rootNode 是否可以选择根节点 0 不可以 1 可以
 */
function areaOne(areaIdAndType, areaName, sid,rootNode,callback) {
	window.top.layer.open({
		  type: 2,
		  //title: ['客户区域选择', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['400px', '530px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  scrollbar:false,
		  content: ['/common/areaOnePage?sid=' + sid+'&rootNode='+rootNode,'no'],
		  btn: ['确定', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.returnArea();
			  if(result){
				  //若是有回调
				  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
					  callback(result);
				  }else{
					  $('#' + areaIdAndType).val(result.idAndType);
					  $('#' + areaName).val(result.areaName);
					  
					  $('#' + areaName).focus();
					  $('#' + areaName).blur();
					  artOpenerCallBack(result.idAndType)
				  }
				  window.top.layer.close(index)
			  }
		  }
		  ,cancel: function(){ 
		    //右上角关闭回调
		  },success:function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					 window.top.layer.close(index);
			 })
		  }
		});
}

/**
 * 按指标分组选择部门
 * @param orgIdElementId
 * @param orgPathNameElementId
 * @param perSchemeId
 * @param groupState
 * @param queryParam
 * @param sid
 */
function perGroupDepOne(orgIdElementId, orgPathNameElementId, perSchemeId ,groupState, queryParam, sid) {
	art.dialog.data('orgId', $('#' + orgIdElementId).val());
	art.dialog.data('orgPathName', $('#' + orgPathNameElementId).val());
	art.dialog.data('queryParam', queryParam);
	art.dialog.open('/common/perGroupDepOnePage?perSchemeId='+perSchemeId+'&groupState='+groupState+'&sid=' + sid, {
		title : '部门单选',
		lock : true,
		max : false,
		min : false,
		width : 350,
		height : 400,
		close : function() {
			$('#' + orgIdElementId).val(art.dialog.data('orgId'));
			$('#' + orgPathNameElementId).val(art.dialog.data('orgPathName'));
			$('#' + orgPathNameElementId).focus();
			$('#' + orgPathNameElementId).blur();
		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			;
			iframe.returnOrg();
			return false;
		},
		cancel : true
	});
}

/**
 * 科室单选
 * @param orgIdElementId
 * @param orgPathNameElementId
 * @param queryParam
 * @param sid
 */
function officeOne(orgIdElementId, orgPathNameElementId, queryParam, sid) {
	art.dialog.data('orgId', $('#' + orgIdElementId).val());
	art.dialog.data('orgPathName', $('#' + orgPathNameElementId).val());
	art.dialog.data('queryParam', queryParam);
	art.dialog.open('/common/officeOnePage?sid=' + sid, {
		title : '科室单选',
		lock : true,
		max : false,
		min : false,
		width : 350,
		height : 400,
		close : function() {
			$('#' + orgIdElementId).val(art.dialog.data('orgId'));
			$('#' + orgPathNameElementId).val(art.dialog.data('orgPathName'));
			$('#' + orgPathNameElementId).focus();
			$('#' + orgPathNameElementId).blur();
		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			;
			iframe.returnOrg();
			return false;
		},
		cancel : true
	});
}


// 机构多选
function orgMore(orgid, queryParam, sid) {
	art.dialog.data('orgid', orgid);
	art.dialog.data('queryParam', queryParam);
	var selectobj = document.getElementById(orgid);
	var o = selectobj.options;
	art.dialog.data('o', o);
	art.dialog.open('/common/orgMorePage?sid=' + sid, {
		title : '机构多选',
		lock : true,
		max : false,
		min : false,
		width : 550,
		height : 440,
		close : function() {

		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			;
			iframe.returnOrg();
			var options = art.dialog.data('options');
			removeOptions(orgid);
			for ( var i = 0; i < options.length; i++) {
				$('#' + orgid).append(
						"<option selected='selected' value='"
								+ options[i].value + "'>" + options[i].text
								+ "</option>");
			}
			$('#' + orgid).focus();
			$('#' + orgid).blur();
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
				iframe.removeAll();
				return false;
			}
		} ]
	});
}

function depMoreCallBack(){
	
}
/**
 * 部门多选
 * @param orgid
 * @param queryParam
 * @param sid
 */
function depMore(orgid, queryParam, sid,callback,disDivKey) {
	
	window.top.layer.open({
		  type: 2,
		  //title: ['部门多选', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['540px', '470px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  scrollbar:false,
		  content: ['/common/depMorePage?sid=' + sid,'no'],
		  btn: ['确定','清空','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var options = iframeWin.returnOrg();
			  
			  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
				  callback(options)
			  }else{
				  removeOptions(orgid);
				  
				  var img="";
				  for ( var i = 0; i < options.length; i++) {
					  $('#' + orgid).append(
							  "<option selected='selected' value='"
							  + options[i].value + "'>" + options[i].text
							  + "</option>");
					  
					  img +="<span id=\"dep_span_"+options[i].value+"\" style='cursor:pointer;' title=\"双击移除\" class=\"label label-default margin-right-5 margin-bottom-5\"  ondblclick=\"removeClickDep('"+orgid+"',"+options[i].value+")\">"+ options[i].text+"</span>";
				  }
				  //判断是否启用回调函数
				  if(callback=="yes"){
					  //人选被选择后执行动作函数体
					  depMoreCallBack(orgid,img);
				  }
				  if(!strIsNull(disDivKey)){
					  $("#"+disDivKey).html(img);
				  }
			  }
				window.top.layer.close(index)
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeAll();
			  return false;
		  },cancel: function(){ 
		    //右上角关闭回调
		  },success: function(layero,index){
			  var options = $("#"+orgid).find("option")
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.setOptions(options);
			  
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				 window.top.layer.close(index);
			 })
		  }
		});
}
/**
 * 部门多选
 * @param orgid
 * @param queryParam
 * @param sid
 */
function depMoreBack(orgid, queryParam, sid,callBackStart,disDivKey,callback) {
	window.top.layer.open({
		  type: 2,
		  //title: ['部门多选', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['540px', '470px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  scrollbar:false,
		  content: ['/common/depMorePage?sid=' + sid,'no'],
		  btn: ['确定','清空','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var options = iframeWin.returnOrg();
			  removeOptions(orgid);
			  
			  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
				  callback(options);
			  }else{
				  var img="";
				  for ( var i = 0; i < options.length; i++) {
					  $('#' + orgid).append(
							  "<option selected='selected' value='"
							  + options[i].value + "'>" + options[i].text
							  + "</option>");
					  
					  img +="<span id=\"dep_span_"+options[i].value+"\" style='cursor:pointer;' title=\"双击移除\" class=\"label label-default margin-right-5 margin-bottom-5\"  ondblclick=\"removeClickDep('"+orgid+"',"+options[i].value+")\">"+ options[i].text+"</span>";
				  }
				  //判断是否启用回调函数
				  if(callBackStart=="yes"){
					  //人选被选择后执行动作函数体
					  depMoreCallBack(orgid,img);
				  }
				  if(!strIsNull(disDivKey)){
					  $("#"+disDivKey).html(img);
				  }
			  }
				window.top.layer.close(index)
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeAll();
			  return false;
		  },cancel: function(){ 
		    //右上角关闭回调
		  },success: function(layero,index){
			  var options = $("#"+orgid).find("option")
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.setOptions(options);
			  
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				 window.top.layer.close(index);
			 })
		  }
		});
}


/**
 * 部门多选
 * @param orgid
 * @param queryParam
 * @param sid
 */
function perGroupDepMore(orgid, perSchemeId ,groupState, queryParam, sid) {
	art.dialog.data('orgid', orgid);
	art.dialog.data('perSchemeId',perSchemeId);
	art.dialog.data('queryParam', queryParam);
	var selectobj = document.getElementById(orgid);
	var o = selectobj.options;
	art.dialog.data('o', o);
	art.dialog.open('/common/perGroupDepMorePage?perSchemeId='+perSchemeId+'&groupState='+groupState+'&sid=' + sid, {
		title : '部门多选',
		lock : true,
		max : false,
		min : false,
		width : 550,
		height : 440,
		close : function() {

		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			;
			iframe.returnOrg();
			var options = art.dialog.data('options');
			removeOptions(orgid);
			for ( var i = 0; i < options.length; i++) {
				$('#' + orgid).append(
						"<option selected='selected' value='"
								+ options[i].value + "'>" + options[i].text
								+ "</option>");
			}
			$('#' + orgid).focus();
			$('#' + orgid).blur();
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
				iframe.removeAll();
				return false;
			}
		} ]
	});
}

/**
 * 科室多选
 * @param orgid
 * @param queryParam
 * @param sid
 */
function officeMore(orgid, queryParam, sid) {
	art.dialog.data('orgid', orgid);
	art.dialog.data('queryParam', queryParam);
	var selectobj = document.getElementById(orgid);
	var o = selectobj.options;
	art.dialog.data('o', o);
	art.dialog.open('/common/officeMorePage?sid=' + sid, {
		title : '科室多选',
		lock : true,
		max : false,
		min : false,
		width : 550,
		height : 440,
		close : function() {

		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			;
			iframe.returnOrg();
			var options = art.dialog.data('options');
			removeOptions(orgid);
			for ( var i = 0; i < options.length; i++) {
				$('#' + orgid).append(
						"<option selected='selected' value='"
								+ options[i].value + "'>" + options[i].text
								+ "</option>");
			}
			$('#' + orgid).focus();
			$('#' + orgid).blur();
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
				iframe.removeAll();
				return false;
			}
		} ]
	});
}

/* 清空多行下拉的值 */
function removeOptions(selectObj) {
	var selectobj = document.getElementById(selectObj);
	while (selectobj.options.length > 0) {
		selectobj.options[0] = null;
	}
}

/* 清除下拉框中选择的option */
function removeClick(id) {
	var selectobj = document.getElementById(id);
	for ( var i = 0; i < selectobj.options.length; i++) {
		if (selectobj.options[i].selected) {
			selectobj.options[i] = null;
			break;
		}
	}
	selected(id);
}

/* 清除下拉框中选择的option */
function removeClickUser(id,selectedUserId) {
	var selectobj = document.getElementById(id);
	for ( var i = 0; i < selectobj.options.length; i++) {
		if (selectobj.options[i].value==selectedUserId) {
			selectobj.options[i] = null;
			break;
		}
	}
	$("#user_img_"+id+"_"+selectedUserId).remove();
	
	selected(id);
}
/* 清除下拉框中选择的option */
function removeClickDep(id,selectedDepId) {
	var selectobj = document.getElementById(id);
	for ( var i = 0; i < selectobj.options.length; i++) {
		if (selectobj.options[i].value==selectedDepId) {
			selectobj.options[i] = null;
			break;
		}
	}
	$("#dep_span_"+selectedDepId).remove();
	
	selected(id);
}
/* 清除下拉框中选择的option */
function removeOption(id,value){
	var selectobj = document.getElementById(id);
	for ( var i = 0; i < selectobj.options.length; i++) {
		if (selectobj.options[i].value == value) {
			selectobj.options[i] = null;
			break;
		}
	}
	selected(id);
}
/* 下拉框选项全部选中 */
function selected(id) {
	var selectobj = document.getElementById(id);
	for ( var i = 0; i < selectobj.options.length; i++) {
		selectobj.options[i].selected = true;
	}
}

// 多文件上传
function upfileMore(fileId, fileTypes, fileSize, numberOfFiles, sid) {
	art.dialog.data('fileId', fileId);
	// 允许上传的文件类别
	art.dialog.data('fileTypes', fileTypes);
	// 单个文件大小限制M
	art.dialog.data('fileSize', fileSize);
	// 允许上传的文件数量
	art.dialog.data('numberOfFiles', numberOfFiles);
	art.dialog.open('/upload/uploadFilePage?sid=' + sid, {
		title : '多文件上传',
		lock : true,
		max : false,
		min : false,
		width : 900,
		height : 400,
		close : function() {

		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			;
			iframe.returnFile();
			var ids = art.dialog.data('ids');
			var names = art.dialog.data('names');
			if (ids != undefined) {
				ids.each(function(index) {
					$('#' + fileId).append(
							"<option selected='selected' value='"
									+ $(this).val() + "'>"
									+ $(names[index]).val() + "</option>");
				});
			}
			$('#' + fileId).focus();
			$('#' + fileId).blur();
			return false;
		},
		cancel : true
	});
}

// 单文件上传
function upfileOne(fileId, fileName, fileTypes, fileSize, sid) {
	var numberOfFiles = 1;
	art.dialog.data('fileId', fileId);
	art.dialog.data('fileName', fileName);
	// 允许上传的文件类别
	art.dialog.data('fileTypes', fileTypes);
	// 单个文件大小限制M
	art.dialog.data('fileSize', fileSize);
	// 允许上传的文件数量
	art.dialog.data('numberOfFiles', numberOfFiles);
	art.dialog.open('/upload/uploadFilePage?sid=' + sid, {
		title : '单文件上传',
		lock : true,
		max : false,
		min : false,
		width : 900,
		height : 400,
		close : function() {

		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			;
			iframe.returnFile();
			var ids = art.dialog.data('ids');
			var names = art.dialog.data('names');
			if (ids != undefined) {
				ids.each(function(index) {
					$('#' + fileId).val($(this).val());
					$('#' + fileName).val($(names[index]).val());
				});
			}
			$('#' + fileName).focus();
			$('#' + fileName).blur();
			return false;
		},
		cancel : true
	});
}
//人员多选回调函数
function userMoreCallBack(){
	//函数提保持为空，在外部重写次函数
}


// 人员多选
/**
 * callBackStart 是否启动回调函数
 * disDivKey 显示选择结果div主键
 */
function userMore(userid, params, sid,callBackStart,disDivKey,callback) {
	
	var userMoreUrl = '/common/userMorePage?sid=' + sid;
	//是否为替岗人员
	var forMeDoDis;
	//是否只查询下属
	var onlySubState = 0;
	if(null != params){
		if(typeof (params) == "object"){
			forMeDoDis = params.forMeDoDis;
			onlySubState = params.onlySubState;
		}else{
			forMeDoDis = params;
		}
	}
	//是否为替岗
	if(null!=forMeDoDis){
		userMoreUrl = userMoreUrl+'&forMeDoDis='+forMeDoDis
	}
	//是否只查询下属
	userMoreUrl = userMoreUrl+'&onlySubState='+onlySubState
	
	window.top.layer.open({
		  type: 2,
		  //title: ['人员多选', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['750px', '530px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: [userMoreUrl,'no'],
		  btn: ['确定','清空','取消'],
		  yes: function(index, layero){
			  var body = window.top.layer.getChildFrame('body', index);
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var options = iframeWin.returnUser();
			  //若是有回调
			  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
				  callback(options);
			  }else{
				  removeOptions(userid);
				  var userIds =new Array();
				  for ( var i = 0; i < options.length; i++) {
					  userIds.push(options[i].value);
					  $('#' + userid).append(
							  "<option selected='selected' value='"
							  + options[i].value + "'>" + options[i].text
							  + "</option>");
				  }
				  $('#' + userid).focus();
				  $('#' + userid).blur();
				  if(!strIsNull(disDivKey)){
					  if(options.length>0){
						  //ajax获取用户信息
						  $.post("/userInfo/selectedUsersInfo?sid="+sid, { Action: "post", userIds:userIds.toString()},     
								  function (users){
							  var img="";
							  for (var i=0, l=users.length; i<l; i++) {
								  img = img + "<div class=\"online-list margin-left-5 margin-bottom-5\" " +
								  "style=\"float:left;cursor:pointer;\" id=\"user_img_"+userid+"_"+users[i].id+"\" " +
								  "ondblclick=\"removeClickUser('"+userid+"',"+users[i].id+")\" title=\"双击移除\">";
								  img = img + "<img src=\"/downLoad/userImg/"+users[i].comId+"/"+users[i].id+"?sid="+sid+"\" " +
								  "class=\"user-avatar\"/>"
								  img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
								  img = img + "</div>"
							  }
							  $("#"+disDivKey).html(img);
						  }, "json");
					  }else{
						  $("#"+disDivKey).html('');
					  }
				  }
				  //判断是否启用回调函数
				  if(callBackStart=="yes"){
					  //人选被选择后执行动作函数体
					  userMoreCallBack();
				  }
			  }
				window.top.layer.close(index)
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeOptions('userselect');
			  return false;
		  },cancel: function(){ 
		    //右上角关闭回调
		  },success: function(layero,index){
			  var options = $("#"+userid).find("option")
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.setOptions(options);
			  //设置点击关闭
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
		});
}
//直接选人毁掉
function directUsersSelectCallBack(params){}
/**
 * 获取、并返回人员信息(流程配置专用)
 * @param sid 有效身份令牌
 */
function directUsersSelect(sid,stepKey,processInfo) {
	layer.open({
		  type: 2,
		  //title: ['人员多选', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['750px', '530px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/common/userMorePage?sid=' + sid,'no'],
		  btn: ['确定','清空','取消'],
		  yes: function(index, layero){
			  var body = layer.getChildFrame('body', index);
			  var iframeWin = window[layero.find('iframe')[0]['name']];
			  var options = iframeWin.returnUser();
			  var userIds =new Array();
				for ( var i = 0; i < options.length; i++) {
					userIds.push(options[i].value);
				}
				var userArr  =new Array();
				if(userIds.length>0){
					//ajax获取用户信息
					$.ajax({  
				        url : "/userInfo/selectedUsersInfo?sid="+sid,  
				        async : false, // 注意此处需要同步，因为返回完数据后，下面才能让结果的第一条selected  
				        type : "POST",  
				        dataType : "json", 
				        data:{userIds:userIds.toString()},
				        success : function(users) {   
				        	userIds =new Array();
							for (var i=0, l=users.length; i<l; i++) {
								userArr.push(users[i].id);
							}
				        }  
				    });
				}
				directUsersSelectCallBack({'stepKey':stepKey,"users":userArr,"processInfo":processInfo});
				layer.close(index);
		  },btn2: function(index, layero){
			  var iframeWin = window[layero.find('iframe')[0]['name']];
			  iframeWin.removeOptions('userselect');
			  return false;
		  },cancel: function(){ 
		    //右上角关闭回调
		  },success:function(layero,index){
			  var iframeWin = window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					 layer.close(index);
			})
		  }
		});
}
//人员单选回调函数
function userOneCallBack(tag,length){
	//函数提保持为空，在外部重写次函数
	alert("函数体保持为空，在外部重写次函数");
}
// 人员单选
function userOne(userid, username, params, sid,callBackStart) {
	var userOneUrl = '/common/userOnePage?sid=' + sid;

	var preUserId = $('#' + userid).val();
	var preUserName = $('#' + username).val();
	if(!preUserId){
		preUserId='';
		preUserName = '';
	}else{
		userOneUrl = userOneUrl+'&userId='+preUserId
		userOneUrl = userOneUrl+'&userName='+preUserName
	}

	//是否为替岗人员
	var forMeDoDis;
	//是否只查询下属
	var onlySubState = 0;
	if(null != params){
		if(typeof (params) == "object"){
			forMeDoDis = params.forMeDoDis;
			onlySubState = params.onlySubState;
		}else{
			forMeDoDis = params;
		}
	}
	//是否为替岗
	if(null!=forMeDoDis){
		userOneUrl = userOneUrl+'&forMeDoDis='+forMeDoDis
	}
	//是否只查询下属
	userOneUrl = userOneUrl+'&onlySubState='+onlySubState

	window.top.layer.open({
		  type: 2,
		  //title: ['人员单选', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['750px', '530px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: [userOneUrl,'no'],
		  btn: ['确定','清空','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var options = iframeWin.returnUser();
			  var userIds =new Array();
			  //若是有回调
			  if(callBackStart && Object.prototype.toString.call(callBackStart) === "[object Function]"){
				  callBackStart(options);
			  }else{
				  for ( var i = 0; i < options.length; i++) {
					  userIds.push(options[i].value);
					  $('#' + userid).val(options[i].value);
					  $('#' + username).val(options[i].text);
					  $('#' + username).focus();
					  $('#' + username).blur();
				  }
				  //判断是否启用回调函数
				  if(callBackStart=="yes"){
					  //人选被选择后执行动作函数体
					  userOneCallBack(userid,options.length);
				  }
				  if(options.length>0){
					  //ajax获取用户信息
					  $.post("/userInfo/selectedUsersInfo?sid="+sid, { Action: "post", userIds:userIds.toString()},     
							  function (users){
						  for (var i=0, l=users.length; i<l; i++) {
							  $("#userOneImg"+userid).attr("src","/downLoad/userImg/"+users[i].comId+"/"+users[i].id+"?sid="+sid);
							  $("#userOneImg"+userid).attr("title",users[i].userName);
							  $("#userOneImg"+userid).css("display","block");
							  //人名赋值
							  $("#userOneName_"+userid).text(users[i].userName);
						  }
					  }, "json");
				  }else{
					  var defaultInit = $("#userOneImg"+userid).attr("defaultInit");
					  if(!defaultInit){
						  var comId = $("#userOneImg"+userid).attr("comId");
						  $('#' + userid).val('');
						  $('#' + username).val('');
						  $("#userOneImg"+userid).attr("src","/downLoad/userImg/"+comId+"/"+userid+"?sid="+sid);
						  $("#userOneImg"+userid).attr("title","");
						  $("#userOneImg"+userid).css("display","none");
						  //人名赋值
						  $("#userOneName_"+userid).text('');
					  }
				  }
				  
			  }
				window.top.layer.close(index)
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeOptions('userselect');
			  return false;
		  },cancel: function(){ 
		    //右上角关闭回调
		  },success: function(layero,index){ 
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
		});
}

/**
 * 部门单选
 * @param orgIdElementId
 * @param orgPathNameElementId
 * @param queryParam
 * @param sid
 */
function depOne(orgIdElementId, orgPathNameElementId, queryParam, sid,callback) {
	window.top.layer.open({
		  type: 2,
		  //title: ['部门单选', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['400px', '450px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  scrollbar:false,
		  content: ['/common/depOnePage?sid=' + sid,'no'],
		  btn: ['确定','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.returnOrg();
			  if(result){
				  var orgId = result.orgId;
				  var orgName =result.orgName;
				  //判断是否启用回调函数
				  if(callback=="yes"){
					  //人选被选择后执行动作函数体
					  depOneCallBack(orgIdElementId, orgPathNameElementId,orgId,orgName);
				  }else if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
					  callback(result);
				  }
				  window.top.layer.close(index);
			  }
		  },cancel: function(){ 
		    //右上角关闭回调
		  },success: function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.initData($('#' + orgIdElementId).val(),$('#' + orgPathNameElementId).val());
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
				})
		  }
		});
}

//人员单选回调函数
//userid返回备选人主键，tag标识符
function userOneForUserIdCallBack(userid,userIdtag,userName,userNametag){
	//函数提保持为空，在外部重写次函数
	alert("函数提保持为空，在外部重写次函数");
}
//人员单选且只返回被选人员主见tag标识符
//人员单选
function userOneForUserId(userid,username,queryParam,sid,userIdtag,userNametag) {
	window.top.layer.open({
		  type: 2,
		  //title: ['人员单选', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['750px', '530px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/common/userOnePage?sid=' + sid+'&userId='+userid+'&userName='+username,'no'],
		  btn: ['确定','清空','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var options = iframeWin.returnUser();
				if(options.length>0){
					//启用回调函数
					//人选被选择后执行动作函数体
					userOneForUserIdCallBack(options[0].value,userIdtag,options[0].text,userNametag);
					//ajax获取用户信息
					$.post("/userInfo/addUsedUser?sid="+sid, { Action: "post", userIds:options[0].value},     
					   function (data){
					 }, "json");
					window.top.layer.close(index)
				}else{
					layer.msg("请选择人员")
					return false;
				}
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeOptions('userselect');
			  return false;
		  },cancel: function(){ 
		    //右上角关闭回调
		  },success: function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				 window.top.layer.close(index);
			 })
		  }
		});
}
function userMoreForUserIdCallBack(options,userIdtag){
}
/**
 * 人员多选---模块筛选使用
 * @param sid
 * @param userIdtag 筛选人员类型
 */
function userMoreForUserId(sid,userIdtag){
	window.top.layer.open({
		  type: 2,
		  //title: ['人员多选', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['750px', '530px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/common/userMorePage?sid=' + sid,'no'],
		  btn: ['确定','清空','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var options = iframeWin.returnUser();
				if(options.length>0){
					//人选被选择后执行动作函数体
					userMoreForUserIdCallBack(options,userIdtag);
					window.top.layer.close(index)
				}else{
					layer.msg("请选择人员")
					return false;
				}
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeOptions('userselect');
			  return false;
		  },cancel: function(){ 
		    //右上角关闭回调
		  },success: function(layero,index){
			  var options = $("#"+userIdtag+"_select").find("option")
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.setOptions(options);
			  //设置点击关闭
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
		});
}
function depMoreForDepIdCallBack(options,depIdtag){
}
/**
 * 部门多选
 * @param sid
 */
function depMoreForDepId(sid,depIdtag) {
	window.top.layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  area: ['540px', '470px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  scrollbar:false,
		  content: ['/common/depMorePage?sid=' + sid,'no'],
		  btn: ['确定','清空','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var options = iframeWin.returnOrg();
			  if(options.length>0){
				  	depMoreForDepIdCallBack(options,depIdtag)
					window.top.layer.close(index)
				}else{
					layer.msg("请选择部门")
					return false;
				}
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeAll();
			  return false;
		  },cancel: function(){ 
		  },success: function(layero,index){
			  var options = $("#"+depIdtag+"_select").find("option")
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.setOptions(options);
			  //设置点击关闭
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
		});
}
/* 角色多选 */
function roleMore(roleid, queryParam, sid) {
	art.dialog.data('roleid', roleid);
	art.dialog.data('queryParam', queryParam);
	var selectobj = document.getElementById(roleid);
	var o = selectobj.options;
	art.dialog.data('o', o);
	art.dialog.open('/common/roleMorePage?sid=' + sid, {
		title : '角色多选',
		lock : true,
		max : false,
		min : false,
		width : 550,
		height : 440,
		close : function() {

		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			;
			iframe.returnRole();
			var options = art.dialog.data('options');
			removeOptions(roleid);
			for ( var i = 0; i < options.length; i++) {
				$('#' + roleid).append(
						"<option selected='selected' value='"
								+ options[i].value + "'>" + options[i].text
								+ "</option>");
			}
			$('#' + roleid).focus();
			$('#' + roleid).blur();
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
				iframe.removeAll();
				return false;
			}
		} ]
	});
}

/* 删除值 */
function removeText(str) {
	var json = eval('(' + str + ')');
	$.each(json, function(index, value) {
		$('#' + value).val('');
		$('#' + value).focus();
	});
	$('#' + json[0]).focus();
}

// 获取焦点时，如果value=tip的值 则value=空字符
function focusName(ts) {
	if ($(ts).val() == $(ts).attr("tip")) {
		$(ts).val("");
	}
}

// 失去焦点时，如果value=空字符 则value=tip的值
function blurName(ts) {
	if ($(ts).val() == "") {
		$(ts).val($(ts).attr("tip"));
	}
}

// 超链上加sid的参数
function addSid(url, sid) {
	if (url != undefined && url != null && url != "") {
		if (url.indexOf("?") >= 0) {
			url = url + "&sid=" + sid;
		} else {
			url = url + "?sid=" + sid;
		}
	} else {
		url = "";
	}
	return url;
}

// 合并单元格
function mergeTable(tableid, cellNum) {
	var tab = document.getElementById(tableid);
	var name = "";
	var index=$('thead tr',$(tab)).length; //从第几行开始处理（不处理表头的行）
	for ( var i = index, j = index; i < tab.rows.length; i++, j++) {
		if (name == tab.rows[i].cells[cellNum].innerHTML) {
			tab.rows[i].deleteCell(cellNum);
		} else {
			name = tab.rows[i].cells[cellNum].innerHTML;
			if (i > 0)
				tab.rows[i - j].cells[cellNum].rowSpan = j;
			j = 0;
		}
		if (i == tab.rows.length - 1) {
			tab.rows[i - j].cells[cellNum].rowSpan = j + 1;
		}
	}
}

// 两个数组拼接到一起
function pushArray(arr0, arr1) {
	if (arr1 != undefined && arr1 != null) {
		for ( var i = 0; i < arr1.length; i++) {
			arr0.push(arr1[i]);
		}
	}
}

// 流程发起
function startFlow(thisNodeId, sid) {
	var url = "/flow/startNodePage?thisNodeId=" + thisNodeId + "&sid=" + sid;
	art.dialog.open(url, {
		title : '流程处理',
		lock : true,
		max : false,
		min : false,
		width : 800,
		height : 490,
		close : function() {

		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			;
			iframe.doFlow();
			return false;
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
				iframe.removeAll();
				return false;
			}
		} ]
	});
}
// 流程处理
function doRunNode(thisRunNodeId, logid, sid) {
	var url = "/flow/doRunNodePage?thisRunNodeId=" + thisRunNodeId + "&logid="
			+ logid + "&sid=" + sid;
	art.dialog.open(url, {
		title : '流程处理',
		lock : true,
		max : false,
		min : false,
		width : 800,
		height : 490,
		close : function() {

		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			iframe.doFlow();
			return false;
		},
		cancel : true,
		button : [ {
			name : '清空',
			callback : function() {
				var iframe = this.iframe.contentWindow;
				if (!iframe.document.body) {
					return false;
				}
				iframe.removeAll();
				return false;
			}
		} ]
	});
}

// 流程展示
function showRunNode(runFlowId, sid) {
	var url = "/flow/showRunNode?runFlowId=" + runFlowId + "&sid=" + sid;
	art.dialog.open(url, {
		title : '流程展示',
		lock : true,
		max : false,
		min : false,
		width : 950,
		height : 450,
		close : function() {

		},
		cancel : true
	});
}

//处理会签或者告知  logid当前日志id actionUrl提交成功后父页面跳转路径
function doSignInform(logid, actionUrl, sid) {
	var queryParam = "";
	art.dialog.data('queryParam', queryParam);
	art.dialog.data('actionUrl', actionUrl);
	art.dialog.data('logid', logid);
	art.dialog.open('/flow/doSignInformPage?id='+logid+'&sid=' + sid, {
		title : '#',
		lock : true,
		max : false,
		min : false,
		width : 750,
		height : 470,
		close : function() {

		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			iframe.doFlow();
			return false;
		},
		cancel : true
	});
}

//发起会签或者告知  logid当前日志id actionUrl提交成功后父页面跳转路径
function startSignInform(logid, actionUrl, sid) {
	var queryParam = "";
	art.dialog.data('queryParam', queryParam);
	art.dialog.data('actionUrl', actionUrl);
	art.dialog.data('logid', logid);
	art.dialog.open('/flow/startSignInformPage?sid=' + sid, {
		title : '#',
		lock : true,
		max : false,
		min : false,
		width : 750,
		height : 470,
		close : function() {

		},
		ok : function() {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			iframe.returnUser();
			var options = art.dialog.data('options');
			return false;
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
				iframe.removeOptions('userselect');
				return false;
			}
		} ]
	});
}

//展示单条流转日志
function showLog(title,logid,sid){
	art.dialog.open('/flow/showRunNodeLog?id='+logid+'&title='+title+'&sid=' + sid, {
		title : title,
		lock : true,
		max : false,
		min : false,
		width : 750,
		height : 370,
		close : function() {

		},
		cancel : true
	});
}

//清空文本框的值
function removeText(id){
	$('#'+id).val("");
}
// 选项卡相关
function tabClick(ts) {
	var swap_tab = $(ts).parent().parent().find('.swap_tab');
	$(ts).parent().children().each(function(index) {
		$(this).removeClass();
		$(swap_tab[index]).hide();
	});
	$(ts).attr("class", "cur");
	$(swap_tab[$(ts).index()]).show();
}

//指标项目信息（录入）
function viewTargetItemForRecordDialog(id,sid){
	art.dialog.open('/targetItem/viewTargetItemForRecordDialogPage?id='+id+'&sid='+sid,{
		title:'指标信息',
		max: false,
		min: false,
		width: '80%',
		height: '85%',
	    cancel: true,
	    cancelVal: '关闭'
	});
}

//指标项目信息
function viewTargetItemDialog(id,sid,orgId){
	var url='/targetItem/viewTargetItemDialogPage?id='+id+'&sid='+sid;
	if(orgId!=null){
		url+='&orgId='+orgId;
	}
	art.dialog.open(url,{
		title:'指标信息',
		max: false,
		min: false,
		width: '80%',
		height: '85%',
	    cancel: true,
	    cancelVal: '关闭'
	});
}



//流程节点维护 
function toUpdateNode(id,sid){
	var url = "/flow/updateNodePage?id="+id+"&sid="+sid;
	art.dialog.data('subYes',0);
	art.dialog.open(url,{
		title:'节点维护',
		lock: true,
		max: false,
		min: false,
		width: 800,
	    height: 440,
	    close:function(){
	    //如果是提交后关闭的，设置节点名称
	    	if(art.dialog.data('subYes')==1){
	    		closeNode(art.dialog.data('subNodeName'));
		    }
		},
		ok: function () {
	    	var iframe = this.iframe.contentWindow;
	    	if (!iframe.document.body) {
	        	return false;
	        };
	        iframe.subClick();
	       	return false;
	    },
	    cancel: true
	});
}

//督办
function addSupervise(businessName,businessId,businessType,sid){
	$.dialog.open('/supervise/addSupervisePage?businessId='+businessId+'&businessType='+businessType+'&businessName='+businessName+'&sid='+sid+'&redirectPage='+encodeURIComponent(window.location.href), {
		    lock : true,
		    title : '录入督办',
		    width : '900px',
		    height : '500px',
		    close : function() {
		    	location.replace(window.location.href);
		    },
		    cancel : true
		});
	}
//查看消息信息 并设置为已读
function viewMsg(id,sid,redirectPage){
	if(redirectPage==null){
		redirectPage="";
	}
	var url = "/message/viewmsgInfo?id="+id+"&sid="+sid;
	art.dialog.data('subYes',0);
	art.dialog.open(url,{
		title:'消息详细',
		lock: true,
		max: false,
		min: false,
		width: 800,
	    height: 440,
	    close : function() {
	    	if(redirectPage!=null){
	    		window.location.href=redirectPage;
	    	}
	    },
	    cancel: true
	});
}

//查看消息详细
function showMsgInfo(id,sid){
	var url = "/message/showMsgInfo?id="+id+"&sid="+sid;
	art.dialog.data('subYes',0);
	art.dialog.open(url,{
		title:'消息详细',
		lock: true,
		max: false,
		min: false,
		width: 800,
	    height: 440,
	    cancel: true
	});
}

/**
 * 复选框全选，用于弹出窗口的全选
 * 
 * @param ckBoxElement
 * @param ckBoxName
 * @return
 */
function selectAll(ckBoxElement, ckBoxName){
	var checkStatus = $(ckBoxElement).attr('checked');
	if (checkStatus) {
		$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', true);
	} else {
		$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', false);
	}
	$(":checkbox[name='" + ckBoxName + "']").click();
	if (checkStatus) {
		$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', true);
	} else {
		$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', false);
	}
}

//图片客户端路径
function getPath(obj){
	 if(obj){
		 var path = $(obj).val();
	     return path;
	 }
}
//截取字符串 
function cutString(str, len) {  
    var strlen = 0;  
    var s = "";  
    for (var i = 0; i < str.length; i++) {  
        if (str.charCodeAt(i) > 128) {  
            strlen += 2;  
        }else {  
         strlen++;  
        }  
        s += str.charAt(i);  
        if (strlen >= len) {  
            return s;  
        }  
    }  
    return s;  
}  

//截取字符串
function cutstr(str, len) {
   var str_length = 0;
   var str_len = 0;
   str_cut = new String();
   str_len = str.length;
   for (var i = 0; i < str_len; i++) {
       a = str.charAt(i);
       str_length++;
       if (escape(a).length > 4) {
           //中文字符的长度经编码之后大于4  
           str_length++;
       }
       str_cut = str_cut.concat(a);
       if (str_length >= len) {
           str_cut = str_cut.concat("..");
           return str_cut;
       }
   }
   //如果给定字符串小于指定长度，则返回源字符串；  
   if (str_length < len) {
       return str;
   }
}
//字符串长度
function charLength(str){
	var realLength = 0, len = str.length, charCode = -1;
   for (var i = 0; i < len; i++) {
       charCode = str.charCodeAt(i);
       if (charCode >= 0 && charCode <= 128) realLength += 1;
       else realLength += 2;
   }
   return realLength;
}

//ifream自适应高度
function IFrameResize(id){  
	 var obj = parent.document.getElementById(id);  //取得父页面IFrame对象
	 obj.height = this.document.body.scrollHeight;  //调整父页面中IFrame的高度为此页面的高度  
} 
//ifream嵌套的单个ifream
function resizeVoteH(id){
	/*暂时停止使用
	setTimeout(function(){
		var offset = $("#bottomDiv").offset();
		var offsettop = offset.top;
		var obj1 = parent.document.getElementById(id);  //取得父页面IFrame对象
		obj1.height = offsettop;  //调整父页面中IFrame的高度为此页面的高度  
		var a2 = $(parent.document.getElementById("bottomDiv")).offset();
		var obj2 =parent.parent.document.getElementById("iframe_body_right");  //取得父页面IFrame对象
		obj2.height = a2.top;  //调整父页面中IFrame的高度为此页面的高度  
	},200);
	*/
	var offset = $("#bottomDiv").offset();
	if(undefined==offset){
		return;
	}
	var offsettop = offset.top;
	var obj1 = parent.document.getElementById(id);  //取得父页面IFrame对象
	if(obj1){
		obj1.height = offsettop;
	}
	  //调整父页面中IFrame的高度为此页面的高度  
	//var a2 = $(parent.document.getElementById("bottomDiv")).offset();
	//var obj2 =parent.parent.document.getElementById("iframe_body_right");  //取得父页面IFrame对象
	//obj2.height = a2.top;  //调整父页面中IFrame的高度为此页面的高度  
}
//在指定的位置插入文字
function insertAtCursor(id,myValue) {
	var myField = document.getElementById(id);
	//IE support
	if (document.selection) {
		myField.focus();
		sel = document.selection.createRange();
		sel.text = myValue;
		sel.select();
	}
	//MOZILLA/NETSCAPE support 
	else if (myField.selectionStart || myField.selectionStart == '0') {
	var startPos = myField.selectionStart;
	var endPos = myField.selectionEnd;
	// save scrollTop before insert www.keleyi.com
	var restoreTop = myField.scrollTop;
	myField.value = myField.value.substring(0, startPos) + myValue + myField.value.substring(endPos, myField.value.length);
	if (restoreTop > 0) {
	myField.scrollTop = restoreTop;
	}
	myField.focus();
	myField.selectionStart = startPos + myValue.length;
	myField.selectionEnd = startPos + myValue.length;
	} else {
	myField.value += myValue;
	myField.focus();
	}
}
//ifream内
function scrollPage(){ 
	var offset = $("#bottomDiv").offset();
	if(undefined==offset){
		return;
	}
	var offsettop = offset.top;
	var obj = window.parent.document.getElementById("iframe_body_right");  //取得父页面IFrame对象
	if(obj){
		obj.height = offsettop;  //调整父页面中IFrame的高度为此页面的高度  
	}
}


//字符串是否为空判断
function strIsNull(obj){
	if(obj!="" && obj!="null" && obj!=null && obj!="undefined" && obj!=undefined && obj!=0){
		return false;
	}else{
		return true;
	}
}
//定义返回值接口函数
function valueReturn(objJson){
	window.location.reload();
}
//定义返回值接口函数
function sonTaskJsonForBack(objJson){
	alert("要怎么写，怎么在外面定义");
}
//图片等比缩放
function AutoResizeImage(maxWidth,maxHeight,objImg,ifream){
	var img = new Image();
	img.src = objImg.src;
	var hRatio;
	var wRatio;
	var Ratio = 1;
	var w = img.width;
	var h = img.height;
	wRatio = maxWidth / w;
	hRatio = maxHeight / h;
	if (maxWidth ==0 && maxHeight==0){
	Ratio = 1;
	}else if (maxWidth==0){//
	if (hRatio<1) Ratio = hRatio;
	}else if (maxHeight==0){
	if (wRatio<1) Ratio = wRatio;
	}else if (wRatio<1 || hRatio<1){
	Ratio = (wRatio<=hRatio?wRatio:hRatio);
	}
	if (Ratio<1){
	w = w * Ratio;
	h = h * Ratio;
	}
	objImg.height = h;
	objImg.width = w;
	//调整页面
	if(strIsNull(ifream)){
		scrollPage()
	}else{
		resizeVoteH(ifream);
	}
} 
/**
 * 展开收缩效果；（暂时）
 * @param keyID
 */
function openAndClose(keyID){
	//var nowImg = $("#changeSerchImg").attr("src");//默认是图片是向上的箭头
	//伸缩查询表单
	$("."+keyID).animate({height:"toggle", opacity:"toggle"},"fast");
	//改变图片箭头的方向
	//if(nowImg=="/static/skin2/img/div_arrUp.png"){
		//$("#changeSerchImg").attr("src","/static/skin2/img/div_arrDown.png")
	//}else{
		//$("#changeSerchImg").attr("src","/static/skin2/img/div_arrUp.png")
	//}
	scrollPage();
}


/**
 * 移除附件
 * @param ts
 * @param fileId
 * @param selectId
 * @return
 */
  function removeMoreFile(sid,ts,fileId,selectId,delFormSys,ifream){
	  if('true'==delFormSys){
		  //异步提交表单
		  $(".subform").ajaxSubmit({
			  type:"post",
			  url:"/upload/delFileById?sid="+sid+"&t="+Math.random(),
			  dataType: "json", 
			  data:{"upfileId":fileId},
			  success:function(data){
				  if(data.status=='y'){
					  $(ts).parent().remove();
					  $("#"+selectId+" option[value='"+fileId+"']").remove();
				  }
				  //调整页面
				  if(null!=ifream && ""!=ifream){
					  resizeVoteH(ifream);
      			  }else{
      				  scrollPage();
      			  }
			  },
			  error:function(XmlHttpRequest,textStatus,errorThrown){
			  }
		  });
	  }else{
		  $(ts).parent().remove();
		  $("#"+selectId+" option[value='"+fileId+"']").remove();
		  //调整页面
		  if(null!=ifream && ""!=ifream){
			  resizeVoteH(ifream);
		  }else{
			  scrollPage();
		  }
	  }
  }
  
  /**
   * 正在上传文件不能提交表单
   * @param id
   * @return
   */
  function sumitPreCheck(id){
  	//是否有正在上传的附件
  	var len = $(".subform").find("input[isDone='no']").length;
  	if(null!=id){
  		len = $("#"+id).find("input[isDone='no']").length;
  	}
  	if(len==0){//没有
  		return true;
  	}else{
  		art.dialog({"content":"正在上传文件，请稍后.."}).time(1);
  		return false;
  	}
  }
  
  function getBiaoqing(){
	  var html = "";
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/am.gif" title="[傲慢]" width="24" height="24" onclick="divClose(\'[傲慢]\',\'/static/biaoQing/am.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/baiy.gif" title="[白眼]" width="24" height="24" onclick="divClose(\'[白眼]\',\'/static/biaoQing/baiy.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/bs.gif" title="[鄙视]" width="24" height="24" onclick="divClose(\'[鄙视]\',\'/static/biaoQing/bs.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/ch.gif" title="[擦汗]" width="24" height="24" onclick="divClose(\'[擦汗]\',\'/static/biaoQing/ch.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/cy.gif" title="[龇牙]" width="24" height="24" onclick="divClose(\'[龇牙]\',\'/static/biaoQing/cy.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/db.gif" title="[大兵]" width="24" height="24" onclick="divClose(\'[大兵]\',\'/static/biaoQing/db.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/fd.gif" title="[发呆]" width="24" height="24" onclick="divClose(\'[发呆]\',\'/static/biaoQing/fd.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/fendou.gif" title="[奋斗]" width="24" height="24" onclick="divClose(\'[奋斗]\',\'/static/biaoQing/fendou.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/fn.gif" title="[愤怒]" width="24" height="24" onclick="divClose(\'[愤怒]\',\'/static/biaoQing/fn.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/gz.gif" title="[鼓掌]" width="24" height="24" onclick="divClose(\'[鼓掌]\',\'/static/biaoQing/gz.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/hanx.gif" title="[憨笑]" width="24" height="24" onclick="divClose(\'[憨笑]\',\'/static/biaoQing/hanx.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/huaix.gif" title="[坏笑]" width="24" height="24" onclick="divClose(\'[坏笑]\',\'/static/biaoQing/huaix.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/hx.gif" title="[害羞]" width="24" height="24" onclick="divClose(\'[害羞]\',\'/static/biaoQing/hx.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/jk.gif" title="[惊恐]" width="24" height="24" onclick="divClose(\'[惊恐]\',\'/static/biaoQing/jk.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/jy.gif" title="[惊讶]" width="24" height="24" onclick="divClose(\'[惊讶]\',\'/static/biaoQing/jy.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/kel.gif" title="[可怜]" width="24" height="24" onclick="divClose(\'[可怜]\',\'/static/biaoQing/kel.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/ku.gif" title="[酷]" width="24" height="24" onclick="divClose(\'[酷]\',\'/static/biaoQing/ku.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/kun.gif" title="[困]" width="24" height="24" onclick="divClose(\'[困]\',\'/static/biaoQing/kun.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/lengh.gif" title="[冷汗]" width="24" height="24" onclick="divClose(\'[冷汗]\',\'/static/biaoQing/lengh.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/lh.gif" title="[流汗]" width="24" height="24" onclick="divClose(\'[流汗]\',\'/static/biaoQing/lh.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/ll.gif" title="[流泪]" width="24" height="24" onclick="divClose(\'[流泪]\',\'/static/biaoQing/ll.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/pz.gif" title="[撇嘴]" width="24" height="24" onclick="divClose(\'[撇嘴]\',\'/static/biaoQing/pz.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/qiao.gif" title="[敲打]" width="24" height="24" onclick="divClose(\'[敲打]\',\'/static/biaoQing/qiao.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/se.gif" title="[色]" width="24" height="24" onclick="divClose(\'[色]\',\'/static/biaoQing/se.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/shuai.gif" title="[衰]" width="24" height="24" onclick="divClose(\'[衰]\',\'/static/biaoQing/shuai.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/tp.gif" title="[调皮]" width="24" height="24" onclick="divClose(\'[调皮]\',\'/static/biaoQing/tp.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/tx.gif" title="[偷笑]" width="24" height="24" onclick="divClose(\'[偷笑]\',\'/static/biaoQing/tx.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/wx.gif" title="[微笑]" width="24" height="24" onclick="divClose(\'[微笑]\',\'/static/biaoQing/wx.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/yiw.gif" title="[疑问]" width="24" height="24" onclick="divClose(\'[疑问]\',\'/static/biaoQing/yiw.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/yun.gif" title="[晕]" width="24" height="24" onclick="divClose(\'[晕]\',\'/static/biaoQing/yun.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/yx.gif" title="[阴险]" width="24" height="24" onclick="divClose(\'[阴险]\',\'/static/biaoQing/yx.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/zhenm.gif" title="[折磨]" width="24" height="24" onclick="divClose(\'[折磨]\',\'/static/biaoQing/zhenm.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/zm.gif" title="[咒骂]" width="24" height="24" onclick="divClose(\'[咒骂]\',\'/static/biaoQing/zm.gif\');"/></a></li>';
	  html +='\n<li><a href="javascript:void(0)"><img src="/static/biaoQing/zt.gif" title="[猪头]" width="24" height="24" onclick="divClose(\'[猪头]\',\'/static/biaoQing/zt.gif\');"/></a></li>';
	  return html;
  }
  	
  	//附件上传的配置文件
  	var WebUpfileConfig = {
            chunkSize : 5*1024*1024,        //分块大小
            fileLimitSize : 2000*1024*1024,//分块大小
            swf:'/static/plugins/webuploader/Uploader.swf',
            title:'rar,zip,doc,docx,xls,xlsx,ppt,pptx,txt,pdf,mp3,gif,jpg,jpeg,png,bmp,3gp,avi,flv,mkv,mov,mp4,mpg,rmvb,swf,mid,wmv',
            extensions:'rar,zip,doc,docx,xls,xlsx,ppt,pptx,txt,pdf,mp3,gif,jpg,jpeg,png,bmp,3gp,avi,flv,mkv,mov,mp4,mpg,rmvb,swf,mid,wmv',
            mimeTypes:'.rar,.zip,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,.pdf,.mp3,.gif,.jpg,.jpeg,.png,.bmp,.3gp,.avi,.flv,.mkv,.mov,.mp4,.mpg,.rmvb,.swf,.mid,.wmv',
            fileNumLimit:0
  	}

	/**
	 * 选择上传附件方式
	 * @param fileIdList 上传后附件的主键集合
	 * @param sid session标识
	 * @param iframe 所在ifream
	 * @param filePicker 附件上传按钮
	 * @param fileShowList 附件上传展示界面
	 * @return
	 */
  	function chooseLoadUpfile(fileIdList,sid,iframe,filePicker,fileShowList,fileVal,fileNum){
        // 询问框
  		 window.top.layer.confirm('选择上传方式？', {
            btn: ['文档库上传','本地上传'] // 按钮
  	 		,title:'询问框'
  	  	 	  ,icon:3
        }, function(index){
        	 window.top.layer.open({
        		 type: 2,
	       		  title:false,
	       		  closeBtn:0,
	       		  area: ['800px', '530px'],
	       		  fix: true, // 不固定
	       		  maxmin: false,
	       		  move: false,
	       		  content: ['/fileCenter/listPagedFileForSelect?pager.pageSize=8&sid='+sid+'&fileNum='+fileNum,'no'],
	       		  btn: ['选择','取消'],
	       		  yes: function(index, layero){
	       			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
	       			  var files = iframeWin.returnFiles();
	       			  if(files){
	       				if(fileNum && fileNum == 1){
	       					addFileViewOne(fileIdList,iframe,fileShowList,files)
	       				}else{
	       					addFileViewMore(fileIdList,iframe,fileShowList,files)
	       					
	       				}
	       				  
	       				  window.top.layer.close(index)
	       			  }
	       		  }
	       		  ,btn2: function(){
	       		  }
	       	});
        	 window.top.layer.close(index)
        }, function(){
            $("#"+filePicker+" :file").click();
        });
	}
  	//添加附件显示
  	function addFileViewMore(fileIdList,iframe,fileShowList,files){
  		   var $list = $("#"+fileShowList);
  		   for(var i=0;i<files.length;i++){
  			   var file = files[i];
  			   // 判断附件主键集合中是否已上传
  			   var optHtml = $("#"+fileIdList+" option[value='"+file.id+"']").html();
  			   if(strIsNull(optHtml) ){// 没有上传，则添加一条数据
  				   $("#"+fileIdList).append('<option selected="selected" value='+file.id+'>'+file.name+'</option>')
  				   var fileSize = file.size;
  				   var dw = '';
  				   // 可以自定义样式
  				   var html = '<div id="' + file.id + '" class="uploadify-queue-item">';
  				   html+='			<div class="cancel">';
  				   html+='				<a href="javascript:void(0)" fileId="' + file.id + '" fileDone="y">X</a>'
  				   html+='			</div>';
  				   var fileName = cutstr(file.name,25);
  				   html+='			<span class="fileName" title="'+file.name+'">' + fileName + ' ('+fileSize+')</span>';
  				   html+=' 			<span class="data">完成</span>';
  				   html+='			<div class="uploadify-progress">';
  				   html+='				<div class="uploadify-progress-bar" style="width:100%;"><!--Progress Bar--></div>';
  				   html+='			</div>';
  				   html+='		</div>';
  				   
  				   $list.append(html);
  				   // 调整页面
  				   if(iframe && ""!=iframe){
  					   resizeVoteH(iframe);
  				   }else{
  					   scrollPage();
  				   }
  				   file.showDiv=fileIdList;
  			   }
  		   }
  	}
  	//添加附件显示
  	function addFileViewOne(fileIdList,iframe,fileShowList,files){
  		var $list = $("#"+fileShowList);
  		$list.html('');
  		for(var i=0;i<files.length;i++){
  			var file = files[i];
  			$("#"+fileIdList).val(file.id);
  			// 判断附件主键集合中是否已上传
  			var fileSize = file.size;
  			var dw = '';
  			// 可以自定义样式
  			var html = '<div id="' + file.id + '" class="uploadify-queue-item">';
  			html+='			<div class="cancel">';
  			html+='				<a href="javascript:void(0)" fileId="' + file.id + '" fileDone="y">X</a>'
  			html+='			</div>';
  			var fileName = cutstr(file.name,25);
  			html+='			<span class="fileName" title="'+file.name+'">' + fileName + ' ('+fileSize+')</span>';
  			html+=' 			<span class="data">完成</span>';
  			html+='			<div class="uploadify-progress">';
  			html+='				<div class="uploadify-progress-bar" style="width:100%;"><!--Progress Bar--></div>';
  			html+='			</div>';
  			html+='		</div>';
  			$list.html(html);
  			// 调整页面
  			if(iframe && ""!=iframe){
  				resizeVoteH(iframe);
  			}else{
  				scrollPage();
  			}
  			file.showDiv=fileIdList;
  		}
  	}
  	/**
  	 * 百度控件上传附件
  	 * @param fileIdList 上传后附件的主键集合
  	 * @param sid session标识
  	 * @param iframe 所在ifream
  	 * @param filePicker 附件上传按钮
  	 * @param fileShowList 附件上传展示界面
  	 * @return
  	 */
  	function loadWebUpfiles(fileIdList,sid,iframe,filePicker,fileShowList,fileVal,fileNum){
  		//上传文件的URL
		var backEndUrl = '/upload/addDepartFile?sid='+sid+'&t='+Math.random();
        //各个不同的文件的MD5,用于上传
        var destMD5Dir = {md5:"",fileVal:fileVal};   //附件MD5信息
		WebUploader.Uploader.register({//注册插件
            "before-send-file": "beforeSendFile"
            , "before-send": "beforeSend"
            , "after-send-file": "afterSendFile"
        }, {
        beforeSendFile: function(file){
            //秒传验证
            var task = new $.Deferred();
        	if(file.showDiv == fileIdList){
        		(new WebUploader.Uploader()).md5File(file).progress(function(percentage){
        			
        		}).then(function(val){
        			//用于上传
        			destMD5Dir.md5 = val;
        			$.ajax({
        				type: "POST"
        					, url: backEndUrl
        					, data: {
        						status: "md5Check",
                          		name: file.name,
								ext: file.ext,
        						md5: val
        					}
        			, cache: false
        			, timeout: 1000 //todo 超时的话，只能认为该文件不曾上传过
        			, dataType: "json"
        			}).then(function(data, textStatus, jqXHR){
        				if(data.ifExist=='1'){   //若存在，这返回失败给WebUploader，表明该文件不需要上传
        					
        					task.reject();
        					
        					//是否原有附件（不包括后来上传的）
        					var fileDone = $("#" + fileShowList).find("div.cancel>a[fileId="+data.fileId+"]");
        					if(fileDone.length==0){//页面有附件
        						uploader.skipFile(file);
        						UploadComlate(file,data,fileShowList,fileIdList,fileNum);
        					}else{
        						var pageFileId = file.id;
        						var obj =  $("#"+fileShowList).find("#"+pageFileId);
        						$(obj).fadeOut();
        						$(obj).remove();
        					}
        					
        				}else{
        					task.resolve();
        				}
        			}, function(jqXHR, textStatus, errorThrown){    //任何形式的验证失败，都触发重新上传
        				task.resolve();
        			});
        		});
        	}else{
        		task.resolve();
        	}
            return $.when(task);
        }
        , beforeSend: function(block){
            //分片验证是否已传过，用于断点续传
            var task = new $.Deferred();
            var file = block.file;
            var fileName = file.name;
            var fileMd5 = uploader.options.formData().md5;
            $.ajax({
                type: "POST"
                , url: backEndUrl
                , data: {
                    status: "chunkCheck",
                        name: file.name,
                        ext: file.ext
                    , chunkIndex: block.chunk
                    , size: block.end - block.start
                    , md5:fileMd5
                }
                , cache: false
                , async:false
                , timeout: 1000 //todo 超时的话，只能认为该分片未上传过
                , dataType: "json"
            }).then(function(data, textStatus, jqXHR){
                if(data.ifExist=='1'){   //若存在，返回失败给WebUploader，表明该分块不需要上传
                    task.reject();
                }else{
                    task.resolve();
                }
            }, function(jqXHR, textStatus, errorThrown){    //任何形式的验证失败，都触发重新上传
                task.resolve();
            });
            return $.when(task);
        }
    });
		
		var WebUpfileConfigT = $.extend(WebUpfileConfig,{});
		if(fileNum && fileNum == 1){
			WebUpfileConfigT.fileNumLimit=1
		}
		var uploader = WebUploader.create({
		    swf: WebUpfileConfigT.swf+'?t='+Math.random(),
		    server: backEndUrl,
		    pick: {id:'#'+filePicker,multiple: true},
		    duplicate: false,//不能选择重复的文件
		    resize: false,
		    auto: false,
		    fileNumLimit: WebUpfileConfigT.fileNumLimit,
		    fileVal:fileVal,   //指明参数名称，后台也用这个参数接收文件
		    fileSingleSizeLimit: WebUpfileConfigT.fileLimitSize,
		    //fileType:'rar,zip,doc,xls,docx,xlsx,pdf'
		    accept: {
		        title: WebUpfileConfigT.title,
		        extensions: WebUpfileConfigT.extensions,
		        mimeTypes: WebUpfileConfigT.mimeTypes
		    },
		    chunked:true,
		    chunkSize:WebUpfileConfigT.chunkSize,
		    thread:2,
		    formData:function(){return $.extend(true, {}, destMD5Dir);}
		});
		uploader.on("error",function(type,reason){
        	if (type=="Q_TYPE_DENIED"){
        		window.top.layer.alert(reason.ext+"格式不支持")
        	}else if(type=="Q_EXCEED_NUM_LIMIT"){
        		window.top.layer.alert('上传的文件数量已经超出系统限制的'+reason+"个");
        	}else if(type=="F_EXCEED_SIZE"){
        		window.top.layer.alert('文件太大不支持');
        	}
		});
	    uploader.on('fileQueued', function( file ) {  // webuploader事件.当选择文件后，文件被加载到文件队列中，触发该事件。等效于 uploader.onFileueued = function(file){...} ，类似js的事件定义。
		   var $list = $("#"+fileShowList);
		   var fileSize = WebUploader.formatSize(file.size);
		   var dw = '';
		   //可以自定义样式
		   var html = '<div id="' + file.id + '" class="uploadify-queue-item">';
		   html+='			<div class="cancel">';
		   html+='				<a href="javascript:void(0)" fileId=0>X</a>'
		   html+='			</div>';
		   var fileName = cutstr(file.name,25);
		   html+='			<span class="fileName" title="'+file.name+'">' + fileName + ' ('+fileSize+')</span>';
		   html+=' 			<span class="data"> - 0%</span>';
		   html+='			<div class="uploadify-progress">';
		   html+='				<div class="uploadify-progress-bar" style="width:0%;"><!--Progress Bar--></div>';
		   html+='			</div>';
		   html+='		</div>';
		   $list.append(html);
		 //调整页面
			if(iframe && ""!=iframe){
				resizeVoteH(iframe);
			}else{
				scrollPage();
			}
			
			 file.showDiv=fileIdList;
			 uploader.upload(file);
	   });
		 uploader.on('uploadSuccess', function (file, data) {
		      var chunksTotal = 0;
	            if((chunksTotal = Math.ceil(file.size/WebUpfileConfigT.chunkSize)) >= 1){
	                //合并请求
	                $.ajax({
	                    type: "POST"
	                    , url: backEndUrl
	                    , data: {
	                        status: "chunksMerge"
	                        , name: file.name
	                        , chunks: chunksTotal
	                        , ext: file.ext
	                        , md5: data.fileMd5
	                        , size:file.size
	                    }
	                    , cache: false
	                    , dataType: "json"
	                }).then(function(data, textStatus, jqXHR){
	                	UploadComlate(file,data,fileShowList,fileIdList,fileNum)
	                }, function(jqXHR, textStatus, errorThrown){
	                	$('#' + file.id).find('.data').text(' 上传出错');
	                });
	            }
		      
	    });
		 //上传进度
	    uploader.on( 'uploadProgress', function( file, percentage ) {
		    var $li = $( '#'+file.id ),
	        $percent = $li.find('.uploadify-progress-bar');
		    var pre = ((percentage-0).toFixed(2)) * 100;
		    $('#' + file.id).find('.data').html(' - '+pre+'%')
		    $percent.css( 'width', percentage * 100 + '%' );
	    });
	    uploader.on('uploadError', function (file,reason) {
		      $('#' + file.id).find('.data').text(' 上传出错');
		});
	    
	    //修复model内部点击不会触发选择文件的BUG
	    $("#"+filePicker+" .webuploader-pick").click(function () {
            $("#"+filePicker+" :file").click();
        });
	  	//todo 如果要删除的文件正在上传（包括暂停），则需要发送给后端一个请求用来清除服务器端的缓存文件
		$("#"+fileShowList).on("click", ".cancel", function(){
			var fileDone = $(this).parents(".uploadify-queue-item").find("div.cancel>a").attr("fileDone");
			if(!fileDone){
				//插件自带的附件主键
				var pageFileId = $(this).parents(".uploadify-queue-item").attr("id");
				if(!strIsNull(pageFileId)){
                    //从上传文件列表中删除
                    uploader.removeFile(pageFileId);
				}
			}
			var fileId = $(this).parents(".uploadify-queue-item").find("div.cancel>a").attr("fileId");
			$("#"+fileIdList+" option[value='"+fileId+"']").remove();
			//页面移除附件
			var obj =  $(this).parents(".uploadify-queue-item");
		    $(obj).fadeOut();
		    $(obj).remove();

		   //调整页面
			if(iframe &&  ""!=iframe){
				resizeVoteH(iframe);
			}else{
				scrollPage();
			}
		});
		
  	 }
  	/**
  	 * md5 秒传文件
  	 * @param file 待上传附件
  	 * @param data 后台返回的结果
  	 * @param fileShowList 结果集合所在位置
  	 * @return
  	 */
  	function UploadComlate(file,data,fileShowList,fileIdList,fileNum){
  		//附件展示的div
  		var fileDiv = $("#"+fileShowList).find("#"+file.id);
  		//设置进度为100%,随后设置成完成
  		$(fileDiv).find(".data").html(' - '+100+'%');
  		$(fileDiv).find(".data").text(' 完成');
  		$percent = $(fileDiv).find('.uploadify-progress-bar');
		$percent.css( 'width', 1 * 100 + '%' );
		
		if(fileNum && fileNum == 1){
			$("#"+fileIdList+"").val(data.fileId);
		}else{
			//判断附件主键集合中是否已上传
			var optHtml = $("#"+fileIdList+" option[value='"+data.fileId+"']").html();
			if(strIsNull(optHtml)){//没有上传，则添加一条数据
				$("#"+fileIdList).append('<option selected="selected" value='+data.fileId+'>'+data.fileName+'</option>')
			}
		}
		//设置附件主键
		$("#" + file.id).find("div.cancel>a").attr("fileId",data.fileId)
  	}
  	
  	
  	//art.dialog弹出默认回调函数
  	function artOpenerCallBack(args){alert("请重写回调函数！");}
//任务更多配置页面
function moreConfigDialog(taskId,sid){
	art.dialog.open("/task/addTaskMoreConfigPage?sid="+sid+"&id="+taskId, {
		title :"更多配置",
		lock : true,
		max : false,
		min : false,
		width : 500,
		height :460,
		ok: function () {
			var iframe = this.iframe.contentWindow;
			if (!iframe.document.body) {
				return false;
			}
			;
			iframe.formSub();
			return false;
	    },
	    cancelVal: '取消',
	    cancel: true,
	    close:function(){
	    	$.post("/task/checkTaskState?sid="+sid,{Action:"post",taskId:taskId},     
			 function (msgObjs){
				
			});
	    }
	});
}
/**
 *关联项目（单选用）
 * @param sid
 * @param itemIdTag 项目主键存放位置标识
 */
function listItem(sid,itemIdTag,callback) {
	var itemId = itemIdTag?$("#"+itemIdTag).val():0;
	window.top.layer.open({
		 type: 2,
		  //title: ['项目列表', 'font-size:18px;'],
		 title:false,
		  closeBtn:0,
		  area: ['650px', '500px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/item/listItemForRelevance?pager.pageSize=8&itemId='+itemId+'&sid='+sid,'no'],
		  btn: ['选择', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.itemSelected();
			  if(result){
				  itemSelectedReturn(result.itemId,result.itemName)
				  window.top.layer.close(index)
			  }
		  }
		,btn2: function(){
			 //右上角关闭回调
			  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
				  callback(false);
			  }
		}
		  ,cancel: function(){ 
		    //右上角关闭回调
		  }
	});
}
/**
 * 项目单选回调（若需要，则页面上自己写）
 * @param itemId项目主键 
 * @param itemName项目名称
 */
function itemSelectedReturn(itemId,itemName){
	
}
/**
 * 项目多选
 * @param selectWay 选择方式 默认多选 1单选
 * @param itemIdTag 
 * @param callback
 */
function itemMoreSelect(selectWay,itemIdTag,callback){
	if(!selectWay || selectWay!=1){
		selectWay = 2;
	}
	//是否需要数据保持
	
	window.top.layer.open({
		 type: 2,
		 title:false,
		 closeBtn:0,
		 area: ['650px', '500px'],
		 fix: true, //不固定
		 maxmin: false,
		 move: false,
		 scrollbar:false,
		 content: ['/item/listMoreItemForRelevance?sid='+sid,'no'],
		 btn: ['选择', '取消'],
		 yes: function(index, layero){
			 var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			 var result = iframeWin.itemSelected();
			 if(result){
				 callback(result)
				 window.top.layer.close(index)
			 }
		  }
		,btn2: function(){
			 
		}
	    ,cancel: function(){ 
	    	//右上角关闭回调
	    },success:function(layero,index){
		  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		  iframeWin.initSelectWay(selectWay,$("#"+itemIdTag).find("option"));
		  iframeWin.setWindow(window.document,window);
		  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			  window.top.layer.close(index);
		  });
		  //双击选择
			$(iframeWin.document).on("dblclick","tr",function(){
				if(selectWay==1){
                    iframeWin.itemSelectedV2($(this));
                    var result = iframeWin.itemSelected();
                    if(result){
                        callback(result)
                        window.top.layer.close(index)
                    }
				}
			})

	    }
	});
}

function functionSelect(selectWay,param,callback){
	var url = "/functionList/funOnePage?sid="+param.sid;
	$.each(param,function(key,val){
		url = url + "&"+key+"="+val
	})
	window.top.layer.open({
        type: 2,
        title:false,
        closeBtn:0,
        area: ['400px', '450px'],
        fix: true, //不固定
        maxmin: false,
        move: false,
        scrollbar:false,
        content: [url,'no'],
        btn: ['确定','取消'],
        yes: function(index, layero){
            var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
            var result = iframeWin.returnFun();
            callback(result);
            window.top.layer.close(index);
        },cancel: function(){
            //右上角关闭回调
        },success: function(layero,index){
            var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
            $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
                window.top.layer.close(index);
            })
        }
    });
}
/**
 * 项目选择用于需求选择
 * @param selectWay 选择方式 默认多选 1单选
 * @param itemIdTag 
 * @param callback
 */
function itemSelectForDemand(selectWay,itemIdTag,callback){
	if(!selectWay || selectWay!=1){
		selectWay = 2;
	}
	window.top.layer.open({
		 type: 2,
		 title:false,
		 closeBtn:0,
		 area: ['750px', '500px'],
		 fix: true, //不固定
		 maxmin: false,
		 move: false,
		 scrollbar:false,
		 content: ['/item/listItemForDemand?sid='+sid,'no'],
		 btn: ['选择', '取消'],
		 yes: function(index, layero){
			 var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			 var result = iframeWin.itemSelected();
			 if(result){
				 callback(result)
				 window.top.layer.close(index)
			 }
		  }
		,btn2: function(){
			 
		}
	    ,cancel: function(){ 
	    	//右上角关闭回调
	    },success:function(layero,index){
		  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		  iframeWin.initSelectWay(selectWay,$("#"+itemIdTag).find("option"));
		  iframeWin.setWindow(window.document,window);
		  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			  window.top.layer.close(index);
		  });
		  //双击选择
			$(iframeWin.document).on("dblclick","tr",function(){
				if(selectWay==1){
                   iframeWin.itemSelectedV2($(this));
                   var result = iframeWin.itemSelected();
                   if(result){
                       callback(result)
                       window.top.layer.close(index)
                   }
				}
			})

	    }
	});
}

/**
 * 审批多选
 * @param selectWay 选择方式 默认多选 1单选
 * @param itemIdTag 
 * @param callback
 */
function spFlowMoreSelect(selectWay,spFlowIdTag,callback){
	if(!selectWay || selectWay!=1){
		selectWay = 2;
	}
	//是否需要数据保持
	
	window.top.layer.open({
		type: 2,
		title:false,
		closeBtn:0,
		area: ['650px', '500px'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		scrollbar:false,
		content: ['/workFlow/listMoreSpFlowForRelevance?sid='+sid,'no'],
		btn: ['选择', '取消'],
		yes: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			var result = iframeWin.spFlowSelected();
			if(result){
				callback(result)
				window.top.layer.close(index)
			}
		}
	,btn2: function(){
		
	}
	,cancel: function(){ 
		//右上角关闭回调
	},success:function(layero,index){
		var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		iframeWin.initSelectWay(selectWay,$("#"+spFlowIdTag).find("option"));
		iframeWin.setWindow(window.document,window);
		$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			window.top.layer.close(index);
		});
		//双击选择
		$(iframeWin.document).on("dblclick","tr",function(){
			if(selectWay==1){
				iframeWin.spFlowSelectedV2($(this));
				var result = iframeWin.spFlowSelected();
				if(result){
					callback(result)
					window.top.layer.close(index)
				}
			}
		})
		
	}
	});
}


/**
 * 审批多选
 * @param selectWay 选择方式 默认多选 1单选
 * @param itemIdTag 
 * @param callback
 */
function demandSelect(selectWay,demandIdTag,callback){
	if(!selectWay || selectWay!=1){
		selectWay = 2;
	}
	//是否需要数据保持
	
	window.top.layer.open({
		type: 2,
		title:false,
		closeBtn:0,
		area: ['800px', '500px'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		scrollbar:false,
		content: ['/demand/demandRelevancePage?sid='+sid,'no'],
		btn: ['选择', '取消'],
		yes: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			var result = iframeWin.demandSelected();
			if(result){
				callback(result)
				window.top.layer.close(index)
			}
		}
	,btn2: function(){
		
	}
	,cancel: function(){ 
		//右上角关闭回调
	},success:function(layero,index){
		var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		iframeWin.initSelectWay(selectWay,$("#"+demandIdTag).find("option"));
		iframeWin.setWindow(window.document,window);
		$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			window.top.layer.close(index);
		});
		//双击选择
		$(iframeWin.document).on("dblclick","tr",function(){
			if(selectWay==1){
				iframeWin.demandSelectedV2($(this));
				var result = iframeWin.demandSelected();
				if(result){
					callback(result)
					window.top.layer.close(index)
				}
			}
		})
		
	}
	});
}


/**
 * 产品多选
 * @param selectWay 选择方式 默认多选 1单选
 * @param productId 
 * @param callback
 */
function productSelect(selectWay,productIdTag,callback){
	if(!selectWay || selectWay!=1){
		selectWay = 2;
	}
	//是否需要数据保持
	
	window.top.layer.open({
		type: 2,
		title:false,
		closeBtn:0,
		area: ['800px', '500px'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		scrollbar:false,
		content: ['/product/productRelevancePage?sid='+sid,'no'],
		btn: ['选择', '取消'],
		yes: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			var result = iframeWin.productSelected();
			if(result){
				callback(result)
				window.top.layer.close(index)
			}
		}
	,btn2: function(){
		
	}
	,cancel: function(){ 
		//右上角关闭回调
	},success:function(layero,index){
		var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		iframeWin.initSelectWay(selectWay,$("#"+productIdTag).find("option"));
		iframeWin.setWindow(window.document,window);
		$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			window.top.layer.close(index);
		});
		//双击选择
		$(iframeWin.document).on("dblclick","tr",function(){
			if(selectWay==1){
				iframeWin.productSelectedV2($(this));
				var result = iframeWin.productSelected();
				if(result){
					callback(result)
					window.top.layer.close(index)
				}
			}
		})
		
	}
	});
}


/**
 *关联客户（单选用）
 * @param sid
 * @param crmIdTag 客户主键存放位置标识
 */
function listCrm(sid,crmIdTag,callback){
	var crmId = crmIdTag ? $("#"+crmIdTag).val():0;
	window.top.layer.open({
		 type: 2,
		  //title: ['客户列表', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['650px', '500px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/crm/listCrmForRelevance?pager.pageSize=8&crmId='+crmId+'&sid='+sid,'no'],
		  btn: ['选择', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.crmSelected();
			  if(result){
				  crmSelectedReturn(result.crmId,result.crmName);
				  window.top.layer.close(index)
			  }
		  },btn2: function(){ 
				 //右上角关闭回调
			  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
				  callback(false);
			  }
		 }
		  ,cancel: function(){ 
		    //右上角关闭回调
		  }
	});
}
/**
 * 客户单选回调函数（若需要，则在页面填写）
 * @param crmId 客户主键 
 * @param crmName 客户名称
 */
function crmSelectedReturn(crmId,crmName){
	
}

/**
 * 客户多选
 * @param selectWay 选择方式 默认多选 1单选
 * @param itemIdTag 
 * @param callback
 */
function crmMoreSelect(selectWay,crmIdTag,callback){
	if(!selectWay || selectWay!=1){
		selectWay = 2;
	}
	//是否需要数据保持
	
	window.top.layer.open({
		type: 2,
		title:false,
		closeBtn:0,
		area: ['650px', '500px'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		scrollbar:false,
		content: ['/crm/listMoreCrmForRelevance?sid='+sid,'no'],
		btn: ['选择', '取消'],
		yes: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			var result = iframeWin.crmSelected();
			if(result){
				callback(result)
				window.top.layer.close(index)
			}
		}
	,btn2: function(){
		
	}
	
	,cancel: function(){ 
		//右上角关闭回调
	},success:function(layero,index){
		var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		iframeWin.initSelectWay(selectWay,$("#"+crmIdTag).find("option"));
            iframeWin.setWindow(window.document,window);
		$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			window.top.layer.close(index);
		})
            //双击选择
            $(iframeWin.document).on("dblclick","tr",function(){
                if(selectWay==1){
                    iframeWin.crmSelectedV2($(this));
                    var result = iframeWin.crmSelected();
                    if(result){
                        callback(result)
                        window.top.layer.close(index)
                    }
                }
            })
	}
	});
}

/**
 * 事件过程管理多选
 * @param selectWay 选择方式 默认多选 1单选
 * @param eventIdTag 
 * @param callback
 */
function eventSelect(selectWay,eventIdTag,callback){
	if(!selectWay || selectWay!=1){
		selectWay = 2;
	}
	//是否需要数据保持
	
	window.top.layer.open({
		type: 2,
		title:false,
		closeBtn:0,
		area: ['650px', '500px'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		scrollbar:false,
		content: ['/iTOm/eventPm/listEventForRelevance?sid='+sid,'no'],
		btn: ['选择', '取消'],
		yes: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			var result = iframeWin.eventSelected();
			if(result){
				callback(result)
				window.top.layer.close(index)
			}
		}
	,btn2: function(){
		
	}
	
	,cancel: function(){ 
		//右上角关闭回调
	},success:function(layero,index){
		var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		iframeWin.initSelectWay(selectWay,$("#"+eventIdTag).find("option"));
            iframeWin.setWindow(window.document,window);
		$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			window.top.layer.close(index);
		})
        //双击选择
        $(iframeWin.document).on("dblclick","tr",function(){
            if(selectWay==1){
                iframeWin.eventSelectedV2($(this));
                var result = iframeWin.eventSelected();
                if(result){
                    callback(result)
                    window.top.layer.close(index)
                }
            }
        })
	}
	});
}
/**
 * 问题过程管理多选
 * @param selectWay 选择方式 默认多选 1单选
 * @param issueIdTag 
 * @param callback
 */
function issueSelect(selectWay,issueIdTag,callback){
	if(!selectWay || selectWay!=1){
		selectWay = 2;
	}
	//是否需要数据保持
	
	window.top.layer.open({
		type: 2,
		title:false,
		closeBtn:0,
		area: ['650px', '500px'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		scrollbar:false,
		content: ['/iTOm/issuePm/listIssueForRelevance?sid='+sid,'no'],
		btn: ['选择', '取消'],
		yes: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			var result = iframeWin.issueSelected();
			if(result){
				callback(result)
				window.top.layer.close(index)
			}
		}
	,btn2: function(){
		
	}
	
	,cancel: function(){ 
		//右上角关闭回调
	},success:function(layero,index){
		var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		iframeWin.initSelectWay(selectWay,$("#"+issueIdTag).find("option"));
		iframeWin.setWindow(window.document,window);
		$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			window.top.layer.close(index);
		})
		//双击选择
		$(iframeWin.document).on("dblclick","tr",function(){
			if(selectWay==1){
				iframeWin.issueSelectedV2($(this));
				var result = iframeWin.issueSelected();
				if(result){
					callback(result)
					window.top.layer.close(index)
				}
			}
		})
	}
	});
}
/**
 * 变更过程管理多选
 * @param selectWay 选择方式 默认多选 1单选
 * @param issueIdTag 
 * @param callback
 */
function modifySelect(selectWay,modifyIdTag,callback){
	if(!selectWay || selectWay !=1 ){
		selectWay = 2;
	}
	//是否需要数据保持
	
	window.top.layer.open({
		type: 2,
		title:false,
		closeBtn:0,
		area: ['650px', '500px'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		scrollbar:false,
		content: ['/iTOm/modifyPm/listModifyForRelevance?sid='+sid,'no'],
		btn: ['选择', '取消'],
		yes: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			var result = iframeWin.modifySelected();
			if(result){
				callback(result)
				window.top.layer.close(index)
			}
		}
	,btn2: function(){
		
	}
	
	,cancel: function(){ 
		//右上角关闭回调
	},success:function(layero,index){
		var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		iframeWin.initSelectWay(selectWay,$("#"+modifyIdTag).find("option"));
		iframeWin.setWindow(window.document,window);
		$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			window.top.layer.close(index);
		})
		//双击选择
		$(iframeWin.document).on("dblclick","tr",function(){
			if(selectWay==1){
				iframeWin.modifySelectedV2($(this));
				var result = iframeWin.modifySelected();
				if(result){
					callback(result)
					window.top.layer.close(index)
				}
			}
		})
	}
	});
}
/**
 * 变更过程管理多选
 * @param selectWay 选择方式 默认多选 1单选
 * @param issueIdTag 
 * @param callback
 */
function releaseSelect(selectWay,releaseIdTag,callback){
	if(!selectWay || selectWay !=1 ){
		selectWay = 2;
	}
	//是否需要数据保持
	
	window.top.layer.open({
		type: 2,
		title:false,
		closeBtn:0,
		area: ['650px', '500px'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		scrollbar:false,
		content: ['/iTOm/releasePm/listReleaseForRelevance?sid='+sid,'no'],
		btn: ['选择', '取消'],
		yes: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			var result = iframeWin.releaseSelected();
			if(result){
				callback(result)
				window.top.layer.close(index)
			}
		}
	,btn2: function(){
		
	}
	
	,cancel: function(){ 
		//右上角关闭回调
	},success:function(layero,index){
		var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		iframeWin.initSelectWay(selectWay,$("#"+releaseIdTag).find("option"));
		iframeWin.setWindow(window.document,window);
		$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			window.top.layer.close(index);
		})
		//双击选择
		$(iframeWin.document).on("dblclick","tr",function(){
			if(selectWay==1){
				iframeWin.releaseSelectedV2($(this));
				var result = iframeWin.releaseSelected();
				if(result){
					callback(result)
					window.top.layer.close(index)
				}
			}
		})
	}
	});
}

//关联任务选择页面
function listTaskForRelevance(sid,callback) {
	window.top.layer.open({
		 type: 2,
		  //title: ['任务列表', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['650px', '550px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/task/listTaskForRelevance?pager.pageSize=8&sid='+sid,'no'],
		  btn: ['选择','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.taskSelected();
			  if(result){
				  //taskSelectedReturn(result);
				  callback(result);
				  window.top.layer.close(index)
			  }
		  }
		  ,btn2: function(){
		    //右上角关闭回调
			  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
				  callback(false);
			  }
		  },success:function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
            	iframeWin.setWindow(window.document,window);
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
            //双击选择
            $(iframeWin.document).on("dblclick","tr",function(){
                var radio = $(this).find("input[type=radio]")
                $(radio).attr("checked","checked")
                var result = iframeWin.taskSelected();
                if(result){
                    //taskSelectedReturn(result);
                    callback(result);
                    window.top.layer.close(index)
                }
            })
		  }
	});
}
//任务选择回调函数
function taskSelectedReturn(result){}
//项目阶段选择（项目阶段里面的所有文件夹）
function stagedItemSelectedPage(sid,itemId,callback) {
	window.top.layer.open({
		 type: 2,
		  //title: ['项目阶段树形', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['600px', '500px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/item/stagedItemForRelevance?sid='+sid+'&itemId='+itemId,'no'],
		  btn: ['选择', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.returnItemStage();
			  if(result){
				  //若是有回调
				  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
					  callback(result);
				  }else{
					  stagedItemSelectedReturn(result)
				  }
				  window.top.layer.close(index)
			  }
		  }
		  ,cancel: function(){ 
		    //右上角关闭回调
		  },success:function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
	});
}
//项目阶段选择回调
function stagedItemSelectedReturn(obj){}
/**
 * 收展样式控制
 * @param id
 */
function disSwitch(id){
	var nowImg = $("#switch_img_"+id).attr("src");//默认是图片是向上的箭头
	//伸缩查询表单
	$("#ul_"+id).animate({height: 'toggle', opacity: 'toggle'},800);
	//改变图片箭头的方向
	if(nowImg=="/static/images/down.png"){
		$("#switch_img_"+id).attr("src","/static/images/up.png")
	}else{
		$("#switch_img_"+id).attr("src","/static/images/down.png")
	}
}
/**
 * 讨论的文本域添加常用意见
 * @param id
 * @return
 */
function addIdea(id,sid){
	window.top.layer.open({
		  type: 2,
		  title: false,
		  closeBtn:0,
		  area: ['550px', '500px'],
		  fix: false, //不固定
		  maxmin: false,
		  content: ['/usagIdea/listPagedUsagIdeaForUse?pager.pageSize=8&sid='+sid+'&rnd='+Math.random(),'no'],
		  btn: ['确定', '关闭'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.useIdea();
			    if(result){
			    	var ideaContent = result.ideaContent;
					insertAtCursor(id,ideaContent);
					$("#"+id).focus();
				}else{
					insertAtCursor(id,'');
				}
			    //延时关闭，否则报错
			    setTimeout(function(){
			    	window.top.layer.close(index);
			    },200);
		  }
		  ,cancel: function(){ 
		    //右上角关闭回调
		  },success: function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find(".nameRow").on("dblclick",function(){
			    	var ideaContent = $(this).attr("title");
					insertAtCursor(id,ideaContent);
					$("#"+id).focus();
				    //延时关闭，否则报错
				    setTimeout(function(){
				    	window.top.layer.close(index);
				    },200);
			  })
		  }
	});
	
}
/**
 * 项目模块选择关联私有组列表页面

 * @param itemId
 * @param sid
 */
function querySelfGroup(itemId,sid){
	window.top.layer.open({
		  type: 2,
		  //title: ['个人私有组列表', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['500px', '500px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: '/item/querySelfGroup?pager.pageSize=8&itemId='+itemId+'&sid='+sid+'&rnd='+Math.random(),
		  btn: ['确定','关闭'],
		  yes: function(index, layero){
			  var body = window.top.layer.getChildFrame('body', index);
			  var iframeWin =  window.top.window[layero.find('iframe')[0]['name']];
			  var groups = iframeWin.returnVal();
			  initItemGroup("["+groups+"]");
			  window.top.layer.close(index)
		  },cancel: function(){ 
		  },btn2:function(){
		  },success: function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.setWindow(window.document);
			  
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
		});
}

/**
 * 下载文件
 * @param uuid
 * @param filename
 */
function downLoad(uuid, filename,sid) {
	window.location.href="/downLoad/down/" + uuid + "/" + filename+"?sid="+sid;
}
/**
 * 查看图片附件
 * @param {Object} filepath
 * @param {Object} sid
 */
function showPic(filepath,sid,fileid,busType,busId) {
	var url = "/fileCenter/showPic?sid="+sid+"&filepath=" + filepath;
	url = url+"&busId="+busId+"&busType="+busType+"&fileId="+fileid;
	window.top.layer.open({
		type: 2,
		//title: ['附件预览', 'font-size:18px;'],
		title:false,
		closeBtn:0,
		area: ['1150px', '90%'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		scrollbar:false,
		content: [url,'no'],
		//btn: ['关闭'],
		 success: function(layero,index){
			    var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			    iframeWin.setWindow(window.document,window);
			    
			    $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					  window.top.layer.close(index);
				  })
		  },
		cancel: function(){ 
		    //右上角关闭回调
		  }
	});
		
}
/**
 * 预览offcie附件
 * @param {Object} fileid 附件ID
 * @param {Object} uuid
 * @param {Object} filename 附件名称
 * @param {Object} fileExt 附件后缀名
 * @param {Object} sid
 */
function viewOfficePage(fileid,uuid,filename,fileExt,sid,busType,busId){
	filename = encodeURIComponent(filename);
	var url = "/fileCenter/viewOfficePage?id="+fileid+"&uuid="+uuid+"&filename="+filename+"&fileExt="+fileExt+"&sid="+sid;
	url = url+"&busId="+busId+"&busType="+busType;
	window.top.layer.open({
		type: 2,
		//title: ['附件预览', 'font-size:18px;'],
		title:false,
		closeBtn:0,
		area: ['1150px', '90%'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		scrollbar:false,
		content: [url,'no'],
		//btn: ['关闭'],
		cancel: function(){ 
		    //右上角关闭回调
		},success:function(layero,index){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		}
	});
}


//视频播放页面
function viewVideoPage(uuid,sid,fileId,busId){
	var url = "/onlineLearn/viewVideoPage?uuid="+uuid+"&sid="+sid+"&fileId="+fileId+"&busId="+busId;
	window.open(url,"newwindow");
//	window.top.layer.open({
//		type: 2,
//		title:false,
//		closeBtn:0,
//		area: ['950px', '90%'],
//		fix: true, //不固定
//		zIndex:9999999999, //层优先级
//		maxmin: false,
//		move: false,
//		content: [url,'no'],
//		cancel: function(){ 
//		},success:function(layero,index){
//			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
//			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
//				  window.top.layer.close(index);
//			  })
//		}
//	});
}
/**
 * 客户模块选择关联私有组列表页面
 * @param customerId
 * @param sid
 */
function querySelfGroupForCustomer(customerId,sid){
	window.top.layer.open({
		  type: 2,
		  //title: ['个人私有组列表', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['500px', '500px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/crm/querySelfGroup?pager.pageSize=8&customerId='+customerId+'&sid='+sid+'&rnd='+Math.random(),'no'],
		  btn: ['确定','关闭'],
		  yes: function(index, layero){
			  var body = window.top.layer.getChildFrame('body', index);
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var groups = iframeWin.returnVal();
			  initCustomerGroup("["+groups+"]");
			  window.top.layer.close(index)
		  },cancel: function(){ 
		  },btn2:function(){
		  },success: function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.setWindow(window.document);
			  
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
		});
}
/**
 * 根据主键删除对象
 * @param key
 */
function removeObjByKey(key) {
	$("#"+key).remove();
}
//修改关注状态
function changeAtten(sid,ts){
	var onclick = $(ts).attr("onclick");
	//业务主键
	var busId = $(ts).attr("busId");
	//业务类型
	var busType = $(ts).attr("busType");
	//当前关注状态1关注0取消
	var attentionState = $(ts).attr("attentionState");
	//是否有描述0没有1有
	var describe = $(ts).attr("describe");
	$.ajax({
		 type : "post",
		 url : '/attention/ajaxchangeAtten?sid='+sid+'&rnd='+Math.random(),
		 dataType:"json",
		 data:{busType:busType,busId:busId,attentionState:attentionState},
		 beforeSend: function(XMLHttpRequest){
			 $(ts).removeAttr("onclick");
         },
         success : function(data){
        	 if(data.status=='y'){
        		var $att = $('body').find("a[attentionState="+attentionState+"][busType="+busType+"][busId="+busId+"]");
        		 
        		 //设置标题
        		$att.attr("title",attentionState==1?"关注":"取消");
        		 //重置点击事件
        		$att.attr("onclick","changeAtten('"+sid+"',this)");
        		 //设置关注状态
        		$att.attr("attentionState",attentionState==0?1:0);
        		$(ts).find("i").toggleClass("fa-star-o").toggleClass("fa-star ws-star");
        		 var i = $(ts).find("i");
        		 if(attentionState==0){//原来没有关注
        			 $att.html(i);
        			 if(describe && describe=='1'){//有描述
        				 $att.append("取消");
        			 }
        			 showNotification(1,"关注成功");
        		 }else{
        			 $att.html(i);
        			 if(describe && describe=='1'){//有描述
        				 $att.append("关注");
        			 }
        			 showNotification(1,"取消成功");
        		 }
        	 }else{
        		 $(ts).attr("onclick",onclick);
        		 showNotification(2,data.info);
        	 }
         },
         error:  function(XMLHttpRequest, textStatus, errorThrown){
        	 $(ts).attr("onclick",onclick);
        	 showNotification(2,"系统错误，请联系管理人员！")
	    }
	});
}


/**
 * 设置关注状态(添加的时候)
 * @return
 */
function setAtten(ts){
	//是否有描述0没有1有
	var describe = $(ts).attr("describe");
	
	var attentionState = $("#attentionState").val();
	 if(attentionState==0){//原来没有关注
		 var html = '<i class="fa fa-star ws-star"></i>取消';
		 if(describe =='0'){
			 html = '<i class="fa fa-star ws-star"></i>';
		 }
		 $(ts).html(html)
		 $("#attentionState").val(1)
	 }else{
		 var html = '<i class="fa fa-star-o"></i>关注';
		 if(describe =='0'){
			 html = '<i class="fa fa-star-o"></i>';
		 }
		 $(ts).html(html)
		 $("#attentionState").val(0)
	 }
}
/**
 * 添加分组,用于树形列表
 * @param scopeTypeSel
 * @param menuContent
 * @param idType
 * @param treeDemo
 * @param sid
 * @return
 */
function addGrpForTree(scopeTypeSel,menuContent,idType,treeDemo,sid){
	
	window.top.layer.open({
		  type: 2,
		  title: false,
		  area: ['550px', '535px'],
		  closeBtn :0,
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/selfGroup/addUseGroupPage?sid='+sid+'&rnd='+Math.random(),'no'],
		  btn: ['添加', '取消'],
		  success:function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					 window.top.layer.close(index);
			 })
		  },
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  
			  var result = iframeWin.tjGrpForm();
			  if(result){
				var options = result.options;
				var grpName = result.grpName;
				var userIds =new Array();
				for ( var i = 0; i < options.length; i++) {
					userIds.push(options[i].value);
				}
				$.ajax({
					 type : "post",
					  url : '/selfGroup/ajaxAddGroup?sid='+sid+'&rnd='+Math.random(),
					  dataType:"json",
					  traditional :true,
					  async:false,
				      data:{"grpName":grpName,userIds:userIds},
				      success:function(data){
				    	  if(data.status=='y'){
				    		  window.top.layer.close(index);
				    		  var selfGroupStr = eval("("+data.selfGroupStr+")")
				    		  initSelfGroupTree(selfGroupStr);
				    		  showMenu();
				    		  showNotification(1,"添加成功");
				    	  }
				      },
				      error:function(XmlHttpRequest,textStatus,errorThrown){
				        	showNotification(2,"系统错误，请联系管理人员");
				        }
				    });
			  }
			  
		  }
		  ,cancel: function(){ 
		  }
		});
}
/**
 * 添加分组,用于树形列表
 * @param sid
 * @return
 */
function addGrp(sid){
	window.top.layer.open({
		  type: 2,
		  //title: ['添加分组', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['550px', '535px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/selfGroup/addUseGroupPage?sid='+sid+'&rnd='+Math.random(),'no'],
		  btn: ['添加', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.tjGrpForm();
			  if(result){
				var options = result.options;
				var grpName = result.grpName;
				var userIds =new Array();
				for ( var i = 0; i < options.length; i++) {
					userIds.push(options[i].value);
				}
				$.ajax({
					type : "post",
					url : '/selfGroup/ajaxAddGroup?sid='+sid+'&rnd='+Math.random(),
					dataType:"json",
					traditional :true,
					async:false,
					data:{"grpName":grpName,userIds:userIds},
					success:function(data){
						if(data.status=='y'){
							window.top.layer.close(index);
							window.self.location.reload();
						}
					},
					error:function(XmlHttpRequest,textStatus,errorThrown){
						showNotification(2,"系统错误，请联系管理人员");
					}
				});
			  }
			  
		  }
		  ,cancel: function(){ 
		  },success:function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
		});
}
/**
 * 添加闹钟提醒
 * @param busId 业务主键
 * @param busType 业务类型
 * @param sid 
 * @return
 */
function addClock(busId,busType,sid){
	window.top.layer.open({
		 type: 2,
		 title:false,
		 closeBtn: 0,
		  area: ['550px', '450px'],
		  fix: true, //不固定
		  maxmin: false,
		  scrollbar:false,
		  content: ['/clock/ajaxAddClockPage?busId='+busId+'&busType='+busType+'&sid='+sid+'&rnd='+Math.random(),"no"],
		  btn: ['保存','关闭'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.formSub();
			  if(!strIsNull(result)){
				  if(busId==0 && busType==0){
					  window.top.layer.close(index);
					  window.location.reload();
				  }else{
					var html = conStrClockHtml(busType,busId,sid,result)
					$("#busClockList").append(html);
					window.top.layer.msg("操作成功！",{icon:1});
					window.top.layer.close(index);
				  }
			  }
		  },cancel: function(){
		  }
		});
	
}
/**
 * 编辑闹钟
 * @param busId 业务主键
 * @param busType 业务类型
 * @param clockId 闹钟主键
 * @param sid
 * @return
 */
function editClock(busId,busType,clockId,sid,ts){
	//编辑界面来源默认是闹铃列表页面
	var srcType = '0';
	if(ts){
		//编辑界面来源是模块界面
		srcType = '1';
	}
	window.top.layer.open({
		 type: 2,
		 title:false,
		 closeBtn: 0,
		  area: ['550px', '450px'],
		  fix: true, //不固定
		  maxmin: false,
		  content: ['/clock/ajaxUpdateClockPage?id='+clockId+'&srcType='+srcType+'&sid='+sid+'&rnd='+Math.random(),'no'],
		  btn: ['修改','删除','关闭'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.formSub();
			  if(!strIsNull(result)){
				  if((busId==0 && busType==0)|| srcType=='0'){
					  window.top.layer.close(index);

					  //判断父页面的页码是否需要调整
					  var url = reConUrlByClz("tbody tr",1);
					  window.self.location = url;
				  }else{
					var clockRepType = result.clockRepType;
					var clockRepDate = result.clockRepDate;
					var clockTime = result.clockTime;
					var clockDate = result.clockDate;
					$(ts).parent().removeAttr("title");
					
					if(clockRepType==0){//仅一次
						var showTime = clockDate.substr(2,10)+" "+clockTime;
						var html ='\n <i class="fa fa-clock-o clockGreen" style="background-color: #fff"></i>'+clockTime;
						html+='\n (单次'+clockDate.substr(2,10)+')';
						$(ts).html(html);
					}else if(clockRepType==1){//每天
						var html ='\n <i class="fa fa-clock-o clockGreen" style="background-color: #fff"></i>'+clockTime;
						html+='\n (每天)';
						$(ts).html(html);
					}else if (clockRepType==2){
						var len = clockRepDate.length;
						var repDate0 = clockRepDate.substr(0,len-1);
						var repDate1 = repDate0.replace("1","周日");
						var repDate2 = repDate1.replace("2","周一");
						var repDate3 = repDate2.replace("3","周二");
						var repDate4 = repDate3.replace("4","周三");
						var repDate5 = repDate4.replace("5","周四");
						var repDate6 = repDate5.replace("6","周五");
						repDate7= repDate6.replace("7","周六");
						$(ts).parent().attr("title",repDate7);
						
						var html = '\n <i class="fa fa-clock-o clockGreen" style="background-color: #fff"></i>'+clockTime;
						html+='\n (每周)';
						$(ts).html(html);
					}else if(clockRepType==3){//每月
						var html='\n <i class="fa fa-clock-o clockGreen" style="background-color: #fff"></i>'+clockTime;
						html+='\n (每月'+clockRepDate+'日)';
						$(ts).html(html);
					}else if(clockRepType==4){//每年
						var showTime = clockDate.substr(5,10);
						var html='\n <i class="fa fa-clock-o clockGreen" style="background-color: #fff"></i>'+clockTime;
						html+='\n (每年'+showTime+')';
						$(ts).html(html);
					}
                 		
					window.top.layer.msg("操作成功！",{icon:1});
					window.top.layer.close(index);
				  }
			  }
		  },btn2: function(index,layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.delClock(busId,busType,clockId,index);
			  return false;
		  },cancel: function(){
		  },success: function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.setClockForRemove($("#clock"+clockId));
			  iframeWin.setWindow(window.document,window);
		  }
		});
}
function alertClock(sid){
	art.dialog.notice({
	    title: '闹铃',
	    width: 220,// 必须指定一个像素宽度值或者百分比，否则浏览器窗口改变可能导致artDialog收缩
	    content: '闹铃提示，赶快关掉',
	    icon: 'warning',
	    time: 30
	});
}
/**
*初始化名片
*/
function initCard(sid){
	return;
	 $('[data-toggle="popover"]').each(function () {
         var element = jq11(this);
         var txt = element.html();
         //用户主键
         var userId=jq11(this).attr("data-user");
         //指定了存放的位置
         var place = jq11(this).attr("data-placement");
         //默认存放在右边
         if(!place){
        	place='right';
         }
         element.popover({
           trigger: 'manual',
           placement: place, //top, bottom, left or right
           html: 'true',
           content: '正在加载，请稍后..'
         }).on("mouseenter", function () {
           var _this = this;
           //是否设置了
           var content = jq11(this).attr("data-content");
           if(!content && userId){
           	ContentMethod(element,sid,userId);
	        }
           jq11(this).popover("show");
           jq11(this).siblings(".popover").on("mouseleave", function () {
        	   jq11(_this).popover('hide');
           });
         }).on("click", function () {
        	 if(userId && sid){
        		 var busId = jq11(this).attr("data-busId");
        		 var busType = jq11(this).attr("data-busType");
        		 art.dialog.open("/userInfo/userInfoDetailPage?sid="+sid+"&id="+userId+"&busType="+busType+"&busId="+busId, {
        				title :"个人详情",
        				id:userId,
        				lock : true,
        				max : false,
        				min : false,
        				fixed:true,
        				width : 620,
        				height :430,
        			    cancelVal: '关闭',
        			    cancel: true
        			});
        	 }
         }).on("mouseleave", function () {
           var _this = this;
           jq11(_this).popover("hide");
         });
       });
}
/**
 * 构建名片
 * @param element
 * @param sid
 * @param userId
 * @return
 */
function ContentMethod(element,sid,userId) {
    var content = jq11(element).attr("data-content");
    if(!content){
      	$.getJSON('/userInfo/userDetail?sid='+sid+'&userId='+userId,function(data) {
          	var user = data.user;
          	var src ='';
          	src+='		/downLoad/userImg/'+user.comId+'/'+user.id+'?sid='+sid+'';
			  var html='<table style="min-width:150px">'
			  html+='<tr>';
			  html+='	<td rowspan=4 style="width:40px">';
			  html+='		<div class="shareHead" style="text-align: center;margin-top:15px">'
			  html+=' 			<img src='+src+' width="60" height="60"/>'
			  html+='		</div>';
			  html+='	</td>';
			  html+='	<td>';
			  html+='		<span class="ws-blue">姓名：'+user.userName+'</span><br>'
			  html+='	</td>';
			  html+='</tr>';
			  html+='<tr>';
			  html+='	<td>';
			  html+='		<span class="ws-blue">部门：'+user.depName+'</span><br>'
			  html+='	</td>';
			  html+='</tr>';
			  html+='<tr>';
			  html+='	<td>';
			  var movePhone =''; 
			  if(user.movePhone){
				  movePhone = user.movePhone;
			  }
			  html+='		<span class="ws-blue">手机：'+movePhone+'</span><br>'
			  html+='	</td>';
			  html+='</tr>';
			  html+='<tr>';
			  html+='	<td>';
			  html+='		<span class="ws-blue">邮箱：'+user.email+'</span><br>'
			  html+='	</td>';
			  html+='</tr>';
			  
			  html+='</table>';
			  
			  jq11('[data-user="'+userId+'"]').each(function () {
				  var _this = jq11(this);
				  var content = jq11(this).attr("data-content");
				  if(!content){
					  jq11(this).attr("data-content",html);
					  jq11(this).siblings(".popover").on("mouseleave", function () {
						  jq11(this).popover('hide');
					  });
					}
			  });
			  jq11(element).popover("show");
			  jq11(element).siblings(".popover").on("mouseleave", function () {
				  jq11(element).popover('hide');
		      });
          });
	   }
}

/**
 * 模块权限控制
 * @param type
 * @return
 */
function modOperateConfig(sid,type,ts,preActiveObj){
	var preActive = preActiveObj;
	if(!preActive){
		//当前活动选项
		preActive = $(".submenu").find(".active");
		//当前活动选项移除背景色
		$(preActive).removeClass();
		//配置设置背景色
		$(ts).parent().addClass("active bg-themeprimary");
	}
	var title = "";
	if(type=='003'){
		title = "任务中心操作配置";
	}else if(type=='005'){
		title = "项目中心操作配置";
	}else if(type=='080'){
        title = "产品中心操作配置";
    }else if(type=='006'){
		title = "周报中心操作配置";
	}else if(type=='012'){
		title = "客户中心操作配置";
	}
	layer.open({
		type: 2,
		title:false,
		closeBtn:0,
		//title: [title, 'font-size:18px;'],
		area: ['500px', '300px'],
		fix: false, //不固定
		maxmin: false,
		scrollbar:false,
		content: ["/modOptConf/modOperateConfigPage?sid="+sid+"&moduleType="+type,'no'],
		btn: ['确定', '关闭'],
		yes: function(index, layero){
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.formSub();
			return false;
		},btn2:function(index){
			//window.self.location.reload();
		},cancel: function(){ 
			//window.self.location.reload();
		},end:function(index){
			//配置删除背景色
			$(ts).parent().removeClass();//恢复前一个选项的背景色
			$(preActive).addClass("active bg-themeprimary");
		}
	});	
}
function sortNumber(a,b){
	return a - b
}
/**
 * 1对1聊天
 * @param busId
 * @param busType
 * @param sessionUser
 * @param userId
 * @param chatType
 * @param sid
 */
function one2OneChat(busId,busType,sessionUser,userId,chatType,sid){
	var arr = new Array(sessionUser,userId);
	arr.sort(sortNumber);
	var url = "/chat/toChat?sid="+sid+"&busId="+busId+"&busType="+busType+"&chatType="+chatType+"&chater="+arr.join(",");
	var title ="聊天室";
	if(busType=='1'){
		title = "[信息分享]"+title;
	}else if(busType=='012'){
		title = "[客户]"+title;
	}else if(busType=='005'){
		title = "[项目]"+title;
	}else if(busType=='080'){
        title = "[产品]"+title;
    }else if(busType=='003'){
		title = "[任务]"+title;
	}else if(busType=='004'){
		title = "[投票]"+title;
	}else if(busType=='006'){
		title = "[周报]"+title;
	}else if(busType=='050'){
        title = "[分享]"+title;
    }else if(busType=='011'){
		title = "[问答]"+title;
	}
	art.dialog.open(url,{
		title:title,
		height:460,
		width:650,
		lock:true,
		fixed:true,
		close:function(){
			var iframe = this.iframe.contentWindow;
			iframe.closeChat();
          }
		}
	)
}

/**
 * 菜单多选设置菜单栏显示
 * @param id
 */
function displayMenu(id){
	var pObj = $("#"+id).parent();
	if($(pObj).hasClass("open")){
		$(pObj).removeClass("open");
	}else{
		$(pObj).addClass("open");
	}
}
/**
 * 设置是否全选
 * searchForm 所在表单
 * allModType 全选的复选框
 * modTypeA 下拉菜单的触发id
 * modTypeId 下拉菜单的区域id
 */
function setIsCheckAll(searchForm,allModType,modTypeA,modTypeId,modTypes){
	var ischecked = $("#"+allModType).attr("checked");//以前没有选中，则为空
	var array = $("#"+searchForm).find(":checkbox[name='"+modTypes+"']");
	if(ischecked){
		$.each(array, function(i) {     
			$(array[i]).attr("checked",true);          
		});
	}else{
		
		$.each(array, function(i) {     
			$(array[i]).attr("checked",false);          
		});
	}
}

/**
 * 控制数字输入
 */
function keyPress() {  
     var keyCode = event.keyCode;  
     if ((keyCode >= 48 && keyCode <= 57))  
    {  
         event.returnValue = true;  
     } else {  
           event.returnValue = false;  
    }  
 } 
  
try{
//layer打开窗口关闭
var tabIndex = window.top.layer.getFrameIndex(window.name); //获取窗口索引
}catch(e){
	console.log("没有引用lay组件");
}
//关闭弹窗
function closeWindow(winIndex){
	layui.use('layer', function(){
		var layer = layui.layer;
		window.top.layer.close(winIndex);
	})
}
//重新加载当前页面
function ReLoad(){
	window.self.location.reload()
}
function setWindow(winBody,win){
	
}
//全选、序号变checkbox
function checkAll(ckBoxElement, ckBoxName){
	var checkStatus = $(ckBoxElement).attr('checked');
	var obj = $(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']");
	if (checkStatus) {
		$(obj).attr('checked', true);
		//显示复选框
		$(obj).parent().parent().find(".optCheckBox").css("display","block");
		//隐藏序号
		$(obj).parent().parent().find(".optRowNum").css("display","none");
		
		//隐藏查询条件
		$(".searchCond").css("display","none");
		//显示批量操作
		$(".batchOpt").css("display","block");
	} else {
		$(obj).attr('checked', false);
		//隐藏复选框
		$(obj).parent().parent().find(".optCheckBox").css("display","none");
		//显示序号
		$(obj).parent().parent().find(".optRowNum").css("display","block");
		
		//显示查询条件
		$(".searchCond").css("display","block");
		//隐藏批量操作
		$(".batchOpt").css("display","none");
	}
}
//取得滚动距离
function getScrollTop()
{
    var scrollTop=0;
    if(document.documentElement&&document.documentElement.scrollTop)
    {
        scrollTop=document.documentElement.scrollTop;
    }
    else if(document.body)
    {
        scrollTop=document.body.scrollTop;
    }
    return scrollTop;
}
//右侧弹窗
function openWinByRight(url,param){
	layui.use('layer', function(){
		if(strIsNull(url)){
			window.top.layer.msg('参数url不能为空', {icon:2});
			return false;
		}
		var layer = layui.layer;
		layer.close(tabIndex);
		
		var len = $(document).find(".navbar-fixed-top").length;
		
		var height = getWindowHeight();
		
		var option = {
				id:'layerOpener',
				type: 2,
				title: false,
				closeBtn: 0,
				shadeClose: true,
				shade: 0,
				shift:0,
				zIndex:299,
				offset: 'rb',
				scrollbar:false,
				fix: true, //固定
				maxmin: false,
				move: false,
				area: ['800px', height+'px'],
				content: [url,'no'], //iframe的url
				success: function(layero,index){
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.setWindow(window.document,window);
					
					$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
						layer.close(index);
					})
					
				},end:function(index){
					$("#headDiv").removeAttr("style");
				}
			};
		option = $.extend(option,param);
		tabIndex = layer.open(option);
	})
}

function getWindowHeight(){
	var len = $(document).find(".navbar-fixed-top").length;
	var height = $(window).height()
	if(len>0){//已被固定
		height = $(window).height()-45;
	}else{//没有被固定
		var scrolltop = getScrollTop();
		var divHeight = scrolltop-45;
		if(divHeight<0){
			height = $(window).height()+divHeight;
		}else{
			height = $(window).height();
		}
		$("#headDiv").attr("style","z-index:1003");
	}
	return height;
}
//右侧弹窗
function openWinTopByRight(url){
	layui.use('layer', function(){
		if(strIsNull(url)){
			window.top.layer.msg('参数url不能为空', {icon:2});
			return false;
		}
		var layer = layui.layer;
		layer.close(tabIndex);
		
		var len = $(document).find(".navbar-fixed-top").length;
		
		var height = $(window).height()
		if(len>0){//已被固定
			height = $(window).height()-45;
		}else{//没有被固定
			var scrolltop = getScrollTop();
			var divHeight = scrolltop-45;
			if(divHeight<0){
				height = $(window).height()+divHeight;
			}else{
				height = $(window).height();
			}
			
			$("#headDiv").attr("style","z-index:1003");
			
		}
		tabIndex = window.top.layer.open({
			id:'layerOpener',
			type: 2,
			title: false,
			closeBtn: 0,
			shadeClose: true,
			shade: 0,
			shift:0,
			zIndex:299,
			offset: 'rb',
			scrollbar:false,
			fix: true, //固定
			maxmin: false,
			move: false,
			area: ['800px', height+'px'],
			content: [url,'no'], //iframe的url
			success: function(layero,index){
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.setWindow(window.document,window);
				
				$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					layer.close(index);
				})
				
			},end:function(index){
				$("#headDiv").removeAttr("style");
			}
		});
	})
}
//右侧弹窗
function openWinByLeft(url){
	layui.use('layer', function(){
		
		if(strIsNull(url)){
			window.top.layer.msg('参数url不能为空', {icon:2});
			return false;
		}
		var layer = layui.layer
		layer.close(tabIndex);
		
		var len = $(document).find(".navbar-fixed-top").length;
		//窗口的高度
		var height = $(window).height();
		//默认距离顶部45
		var paddingtop = 45;
		
		if(len>0){//已被固定
			height = $(window).height()-45;
		}else{//没有被固定
			var scrolltop = getScrollTop();
			var divHeight = scrolltop-45;
			if(divHeight<0){
				height = $(window).height()+divHeight;
			}else{
				height = $(window).height();
			}
			if(paddingtop-scrolltop>0){
				paddingtop = paddingtop-scrolltop;
			}else{
				paddingtop = 0;
			}
			
			$("#headDiv").attr("style","z-index:1003");
			
		}
		tabIndex = layer.open({
			id:'layerOpener',
			type: 2,
			title: false,
			closeBtn: 0,
			shadeClose: true,
			shade: 0,
			shift:0,
			zIndex:299,
			offset: [paddingtop+'px', '0px'],
			scrollbar:false,
			fix: true, //固定
			maxmin: false,
			move: false,
			area: ['800px', height+'px'],
			content: [url,'no'], //iframe的url
			success: function(layero,index){
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.setWindow(window.document,window);
			},end:function(index){
				$("#headDiv").removeAttr("style");
			}
		});
	})
}



//人员多选
/**
 * callBackStart 是否启动回调函数
 * disDivKey 显示选择结果div主键
 */
function userMore4Week(userid, queryParam, sid,callBackStart,disDivKey) {
	
	window.top.layer.open({
		  type: 2,
		  //title: ['人员多选', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['750px', '530px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  scrollbar:false,
		  content: ['/common/userMorePage?sid=' + sid,'no'],
		  btn: ['确定','清空','取消'],
		  yes: function(index, layero){
			  var body = window.top.layer.getChildFrame('body', index);
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var options = iframeWin.returnUser();
			  removeOptions(userid);
			  
			  var userIds =new Array();
				for ( var i = 0; i < options.length; i++) {
					userIds.push(options[i].value);
					$('#' + userid).append(
							"<option selected='selected' value='"
									+ options[i].value + "'>" + options[i].text
									+ "</option>");
				}
				//判断是否启用回调函数
				if(callBackStart=="yes"){
					//人选被选择后执行动作函数体
					userMoreCallBack();
				}
				if(options.length>0){
					//ajax获取用户信息
					$.post("/userInfo/selectedUsersInfo?sid="+sid, { Action: "post", userIds:userIds.toString()},     
							function (users){
						var img="";
						for (var i=0, l=users.length; i<l; i++) {
							img +="\n 		<div style=\"float: left;margin-top: 5px;margin-left: 5px;\">";
							img +="\n		<img id=\"user_img_"+users[i].id+"\" style=\"display:block;float:left;margin-left:3px;\" ondblclick=\"removeWeekClickUser('listMembers_memberId"+disDivKey+"',"+users[i].id+")\" src=\"/downLoad/userImg/"+users[i].comId+"/"+users[i].id+"?sid="+sid+"\" title="+users[i].userName+" ></img>";
							img +="\n		<i class=\"zbmember-Name\" style=\"float:left;margin-top: 15px\">"+users[i].userName+"</i>";
							img +="\n 		</div>";
						}
						if(disDivKey){
							$("#userMoreImglistMembers_memberId"+disDivKey).html(img);
						}else{
							$("#userMoreImglistMembers_memberId").html(img);
						}
						setStyle();
					}, "json");
				}
				window.top.layer.close(index)
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeOptions('userselect');
			  return false;
		  },cancel: function(){ 
		    //右上角关闭回调
		  },success: function(layero){
			  var options = $("#"+userid).find("option")
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.setOptions(options);
			  
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.toplayer.close(index);
			  })
		  }
		});
}
//添加页首部分的业务
function addHeadBus(busType,sid){
	layui.use('layer', function(){
		var layer = layui.layer;
		var url;
		if(busType=='1'){//分享
			var shareUrl = '/msgShare/addMsgSharePage?sid='+sid;
			tabIndex = layer.open({
				type: 2,
				title: false,
				closeBtn: 0,
				shadeClose: true,
				shade: 0.3,
				shift:0,
				btn:['分享','关闭'],
				scrollbar:false,
				fix: true, //固定
				maxmin: false,
				move: false,
				zIndex:1010,
				area: ['650px', '450px'],
				content: [shareUrl,'no'], //iframe的url
				yes:function(index,layero){
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.addMsgShares();
				},
				success: function(layero,index){
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.setWindow(window.document,window);
				},end:function(index){
					//个人中心界面重新加载数据
					if(pageTag){
						if(pageTag.indexOf('selfCenter')>=0){
							window.self.location.reload();
						}else if(pageTag.indexOf('index')>=0){
							
						}
					}else{
						layer.close(index);
					}
					
				}
			});
		}else if(busType=='003'){//任务
			url = "/task/addTaskBySimple?sid="+sid;
		}else if(busType=='004'){//投票
			url = "/vote/addVotePage?sid="+sid;
		}else if(busType=='011'){//问题
			url = "/qas/addQuesPage?sid="+sid;
		}else if(busType=='101'){//闹铃
			addClock(0,0,sid)
		}else if(busType=='005'){//项目
			url = "/item/addItemBySimple?sid="+sid;
		}else if(busType=='080'){//产品
            url = "/product/addProBySimple?sid="+sid;
        }else if(busType=='006'){//周报
			url = "/weekReport/addWeekRepPage?sid="+sid;
		}else if(busType=='050'){//分享
            url = "/daily/addDailyPage?sid="+sid + "&pageTag='1'";
        }else if(busType=='012'){//客户
			url = "/crm/addCustomerBySimple?sid="+sid;
		}else if(busType=='016'){//日程
			var nowd = new Date();  
			var year = nowd.getFullYear(); 
			var month = nowd.getMonth()+1;
			var date = nowd.getDate();
			
			var sDate = year+"-"+month+"-"+date;
			if(month<10 && date>=10){//月份小于10，日期大于10，月份加0
				sDate = year+"-0"+month+"-"+date;
			}else if(month<10 && date<10){//月份小于10，日期小于10，月份和日期都加0
				sDate = year+"-0"+month+"-0"+date;
			}else if(month>=10 && date<10){//月份大于10，日期小于10，日期加0
				sDate = year+"-"+month+"-0"+date;
			}
			edate = sDate;
			viewStart = sDate;
			viewEnd = sDate;
			
			var urlA = '/schedule/addSchedulePage?sid='+sid+'&scheStartDate='+sDate+'&scheEndDate='+edate+'&formatEl=0';
			window.top.layer.open({
				type: 2,
				title: false,
				closeBtn:0,
				area: ['610px','450px'],
				fix: true, //不固定
				maxmin: false,
				scrollbar:false,
				move: false,
				zIndex:1010,
				shade:0.3,
				scrollbar:false,
				content: [urlA,'no'],
				btn: ['添加日程','取消'],
				yes: function(index, layero){
					var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
					var result = iframeWin.addschedle(sid,viewStart,viewEnd);
					if(result){
						window.top.layer.close(index);
						showNotification(1,"添加成功");
					}
				},end:function(){
					window.self.location.reload();
				}
			});
		}else if(busType=='017'){//会议
			url="/meeting/addMeetingPage?sid="+sid;
		}else if(busType=='022'){//审批
			var url = "/flowDesign/listSpFlow?pager.pageSize=10&sid="+sid;
			openWinByRight(url);
			return;
		}else if(busType=='029' || busType=='035'){//出差申请、借款申请
			listBusMapFlows(busType,function(busMapFlow){
				var url = "/busRelateSpFlow/loanApply/addLoanApply?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType="+busType;
				openWinByRight(url);
			})
			return;
		}else if(busType=='030'){//借款申请
			loanWayChoose(sid,function(tripState,needApply){
				if(tripState=='1' && needApply == "0"){//差旅直接借款
					listBusMapFlows('030',function(busMapFlow){
						var url = "/busRelateSpFlow/loan/addLoan?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType=030&isBusinessTrip=1";
						openWinByRight(url);
					})
				}else if(tripState=='1' && needApply == "1"){//差旅计划借款
					loanApplyChoose(tripState,sid,function(loanApply){
						getSelfJSON("/financial/checkLoanOffAll",{sid:sid,feeBudgetId:loanApply.id,busType:'030'},function(data){
							if(data.status=='y'){
								listBusMapFlows('030',function(busMapFlow){
									var url = "/busRelateSpFlow/loan/addLoan?sid="+sid;
									url = url+"&busMapFlowId="+busMapFlow.id;
									url = url+"&busType=030&isBusinessTrip=1";
									url = url+"&feeBudgetId="+loanApply.id;
									openWinByRight(url);
								})
							}else{
								showNotification(2,data.info);
							}
						})
					})
				}else if(tripState=='0' && needApply == "0"){//常规直接借款
					listBusMapFlows('031',function(busMapFlow){
						var url = "/busRelateSpFlow/loan/addLoan?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType=031&isBusinessTrip=0";
						openWinByRight(url);
					})
				}else if(tripState=='0' && needApply == "1"){//常规计划借款
					
					 listBusMapFlows('035',function(busMapFlow){
						var url = "/busRelateSpFlow/loanApply/addLoanApply?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType=035";
						openWinByRight(url);
					 })
				}
			});
			return;
		}else if(busType=='031'){//报销申请
			loanOffWayChoose(sid,function(tripState,needReport,applyState){
				if(tripState=='1' && needReport == "0"){//差旅直接报销
					//直接报销03101
					listBusMapFlows('03101',function(busMapFlow){
						var url = "/busRelateSpFlow/loanOff/addLoanOff?sid="+sid;
						url = url+"&busMapFlowId="+busMapFlow.id;
						url = url+"&busType=03101";
						url = url+"&loanReportId=0";
						url = url+"&feeBudgetId=0";
						url = url+"&loanWay=2";//直接借款
						url = url+"&loanReportWay=3";//需要关联项目的
						openWinByRight(url);
					});
				}else if(tripState=='1' && needReport == "1"){//汇报后报销
					//根据汇报说明报销03101
					loanApplyForOffChoose(tripState,sid,function(feeBudgetId,loanReportId){
						getSelfJSON("/financial/checkLoanRep4Off",{sid:sid,loanReportId:loanReportId,feeBudgetId:feeBudgetId},function(data){
							if(data.status=='y'){
								var busType = data.busType;
								listBusMapSelect(data.listBusMapFlows,busType,function(busMapFlow){
									var url = "/busRelateSpFlow/loanOff/addLoanOff?sid="+sid;
									url +="&busMapFlowId="+busMapFlow.id
									url +="&feeBudgetId="+feeBudgetId
									url +="&loanReportId="+loanReportId
									url +="&busType=03101";
									url +="&loanWay=1";//额度借款
									url +="&loanReportWay=1";//需要汇报的
									openWinByRight(url);
								})
							}else{
								showNotification(2,data.info);
							}
						})
					});
				}else if(tripState=='0' && needReport == "1"){//常规汇报后报销
					//根据汇报说明报销03102
					loanApplyForOffChoose(tripState,sid,function(feeBudgetId,loanReportId){
						if(loanReportId && loanReportId>0){
							getSelfJSON("/financial/checkLoanRep4Off",{sid:sid,loanReportId:loanReportId,feeBudgetId:feeBudgetId},function(data){
								if(data.status=='y'){
									var busType = data.busType;
									listBusMapSelect(data.listBusMapFlows,busType,function(busMapFlow){
										var url = "/busRelateSpFlow/loanOff/addLoanOff?sid="+sid;
										url +="&busMapFlowId="+busMapFlow.id
										url +="&feeBudgetId="+feeBudgetId
										url +="&loanReportId="+loanReportId
										url +="&busType=03102";
										url +="&loanWay=1";//额度借款
										url +="&loanReportWay=1";//需要汇报的
										openWinByRight(url);
									})
								}else{
									showNotification(2,data.info);
								}
							})
						}else{
							//直接报销03102
							listBusMapFlows('03102',function(busMapFlow){
								var url = "/busRelateSpFlow/loanOff/addLoanOff?sid="+sid;
								url +="&busMapFlowId="+busMapFlow.id
								url +="&feeBudgetId="+feeBudgetId
								url +="&loanReportId=0"
								url +="&busType=03102";
								url +="&loanWay=1";//额度借款
								url +="&loanReportWay=1";//需要汇报的
								openWinByRight(url);
							});
						}
					});
				}else if(tripState=='0' && needReport == "0"){//直接报销
					if(applyState ==='1'){
						//根据借款记录报销03101
						loanForOffChoose(tripState,sid,function(feeBudgetId,loanReportId){
							//直接报销03102
							listBusMapFlows('03102',function(busMapFlow){
								var url = "/busRelateSpFlow/loanOff/addLoanOff?sid="+sid;
								url = url+"&busMapFlowId="+busMapFlow.id;
								url = url+"&busType=03102";
								url = url+"&loanReportId=0";
								url = url+"&feeBudgetId="+feeBudgetId;
								url = url+"&loanWay=2";//直接借款
								url = url+"&loanReportWay=1";//汇报后报销
								openWinByRight(url);
							});
						});
					}else{
						//直接报销03102
						listBusMapFlows('03102',function(busMapFlow){
							var url = "/busRelateSpFlow/loanOff/addLoanOff?sid="+sid;
							url = url+"&busMapFlowId="+busMapFlow.id;
							url = url+"&busType=03102";
							url = url+"&loanReportId=0";
							url = url+"&feeBudgetId=0";
							url = url+"&loanWay=2";//直接借款
							url = url+"&loanReportWay=2";//直接报销
							openWinByRight(url);
						});
					}
				}
			});
		}else if(busType=='036'){//请假申请
			listBusMapFlows(busType,function(busMapFlow){
				var url = "/busRelateSpFlow/attence/addLeaveApply?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType="+busType;
				openWinByRight(url);
			})
			return;
		}else if(busType=='037'){//加班申请
			listBusMapFlows(busType,function(busMapFlow){
				var url = "/busRelateSpFlow/attence/addOverTimeApply?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType="+busType;
				openWinByRight(url);
			})
			return;
		}else if(busType=='04201'){//IT运维管理-事件管理过程
			listBusMapFlows(busType,function(busMapFlow){
				var url = "/iTOm/eventPm/addEventPm?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType="+busType;
				openWinByRight(url);
			})
			return;
		}else if(busType=='013'){//文档分享
			var fileUrl="/fileCenter/addFilePage?sid="+sid+"&classifyId=-1&t="+Math.random();
			
			layer.open({
				type: 2,
				title: false,
				closeBtn: 0,
				shadeClose: true,
				shade: 0.3,
				shift:0,
				btn:['上传','关闭'],
				scrollbar:false,
				fix: true, //固定
				maxmin: false,
				move: false,
				area: ['650px', '450px'],
				content: [fileUrl,'no'], //iframe的url
				yes:function(index,layero){
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.addFiles(function(){
						layer.close(index);
					});
				},
				success: function(layero,index){
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.setWindow(window.document,window);
				},end:function(index){
					layer.close(index);
					if('index'==pageTag){
						$.each($(".indexLanmu"),function(index,item){
							var busType = $(this).attr("lanmuType");
							if(busType=='013'){
								loadIndexLanmu(busType,$(this));
							}
						});
					}else if('fileCenter'==pageTag){
						window.self.location.reload();
					}
				}
			});
		}
		if(url){
			tabIndex = layer.open({
				type: 2,
				title: false,
				closeBtn: 0,
				shadeClose: false,
				shade: 0.3,
				shift:0,
				zIndex:1010,
				scrollbar:false,
				fix: true, //固定
				maxmin: false,
				move: false,
				area: ['800px', '550px'],
				content: [url,'no'], //iframe的url
				success:function(layero,index){
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.setWindow(window.document,window);
				},end:function(){
					if(busType=='101'){
						// window.self.location.reload();
					}
				}
			});
		}
	})
}
//模块回收站
function listRecyBin(busType,sid){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	window.location.href='/recycleBin/listPagedPreDel?sid='+sid+'&busType='+busType+"&t="+Math.random();;
}
//上传新文件
 function addFiles(sid){
	var url = "/fileCenter/addFilePage?sid="+sid+"&classifyId=-1&t="+Math.random();
	layer.closeAll();
	layer.open({
	  type: 2,
	  title: false,
	  closeBtn: 0,
	  shadeClose:false,
	  shade: 0.3,
	  shift:0,
	  btn:['上传','取消'],
	  scrollbar:false,
	  fix: true, //固定
	  maxmin: false,
	  move: false,
	  area: ['650px', '450px'],
	  content: [url,'no'], //iframe的url
	  yes:function(index,layero){
		  var iframeWin = window[layero.find('iframe')[0]['name']];
		  
		  iframeWin.addFiles(function(){
			  //添加附件成功
			  layer.close(index);
		  });
	  },
	  success: function(layero,index){
		    var iframeWin = window[layero.find('iframe')[0]['name']];
		    iframeWin.setWindow(window.document,window);
	  },end:function(index){
		  //layer.close(index);
		  //window.self.location.reload();
	  }
	});
 }
//注销
 function exitSys(sid){
 	window.top.layer.confirm('确定要注销吗？', {
 		  btn: ['确定','取消']//按钮
 	  ,title:'询问框'
 	  ,icon:3
 	}, function(index){
 		window.top.location.href='/exit?sid='+sid
 	});
 }
 
 /**
  * 查看原图
  * @param path
  */
 function viewOrgByPath(path){
	layer.ready(function(){ //为了layer.ext.js加载完毕再执行
			layer.photos({
			    photos: {
			    	  "title": "", //相册标题
			    	  "id": 1, //相册id
			    	  "start": 0, //初始显示的图片序号，默认0
			    	  "data": [   //相册包含的图片，数组格式
			    	    {
			    	      "alt": "图片",
			    	      "pid": 1, //图片id
			    	      "src": path, //原图地址
			    	      "thumb": path //缩略图地址
			    	    }
			    	  ]
			    	}
			  });
			});  
	 
 }
 /**
  * 添加菜单使用频率
  * @param busType 业务主键
  * @param sid session标识
  */
 function addMenuRate(busType,sid,url){
	 //启动加载页面效果
	 layer.load(0, {shade: [0.6,'#fff']});
	 $.ajax({
		 type: "post",
		 url:"/menu/addMenuRate?sid="+sid,
		 data:{busType:busType},
		 dataType: "json",
		 success:function(data){
		 }
	 })
	 window.self.location = url;
 } 
 
 //头部菜单菜单设置(暂时没有使用)
 function menuSet(sid,position){
	 var url = "/menu/listMenuHomeSet?sid="+sid;
	 var width = '550px';
	 var height = '380px';
	 if(position=='head'){
		 url = "/menu/listMenuSet?sid="+sid;
		 width = '650px';
		 height = '500px';
	 }
	 
	 layer.open({
		  type: 2,
		  title: false,
		  closeBtn: 0,
		  shadeClose: false,
		  shade: 0.3,
		  shift:0,
		  zIndex:1010,
		  scrollbar:false,
		  fix: true, //固定
		  btn:['恢复默认','关闭'],
		  maxmin: false,
		  move: false,
		  area: [width, height],
		  content: [url,'no'], //iframe的url
		  yes:function(index,layero){
			  var iframeWin = window[layero.find('iframe')[0]['name']];
			  iframeWin.resetMenuHome();
		  },
		  btn2:function(index,layero){
			  var iframeWin = window[layero.find('iframe')[0]['name']];
			  iframeWin.closeWindow();
		  },
		  success:function(layero,index){
		    var iframeWin = window[layero.find('iframe')[0]['name']];
		    iframeWin.setWindow(window.document,window,index);
		  }
		});
 }
//菜单点击事件绑定
 function menuClick(url){
 	 //启动加载页面效果
 	 layer.load(0, {shade: [0.6,'#fff']});
 	 window.self.location = url+"&t="+Math.random();
 }
 //设置模块已读
 function readMod(ts,page,modId,modType){
	 if(page=='index'){//首页
		 var isClock = $(ts).attr("isClock");
		 var array = new Array();
		 
		 var todayA = $("#allLanmu").find("[lanmuType='020']").find("a");
		 var todoIdArray = new Array();
		 $.each(todayA,function(index,item){
			 todoIdArray.push($(this).attr("todoId"))
		 });
			
		 if(isClock==1){//是闹铃,暂时只有待办里面才有
			var lanmuType =  $(ts).parents(".indexLanmu").attr("lanmuType");
			array.push(lanmuType);
			
			 if($(ts).hasClass("noread")){
				 $(ts).removeClass("noread");
				 changeNoReadNum('101');
				 $(ts).parents(".order-item").remove();
				 
				 resetAllTodo();
				 
				 //修改未读数
				 changeNoReadNum(modType);
			 }
		 }else{
			 var modArray = $("#allLanmu").find("[modType='"+modType+"'][modId='"+modId+"']");
			 var flag = false;
			 $.each(modArray,function(index,item){
				 if($(this).hasClass("noread")){
					 //移除未读表示
					 $(this).removeClass("noread");
					 if($(this).attr("modType")!='003' //非任务模块移除
						 && $(this).attr("modType")!='015' 
						 && $(this).attr("modType")!='022' 
						 && $(this).attr("modType")!='027010' 
						 && $(this).attr("modType")!='027020' 
						 && $(this).attr("modType")!='046' 
						 && $(this).attr("modType")!='047' 
						 && $(this).attr("modType")!='00306'
						 && $(this).attr("modType")!='06601'
						 && $(this).attr("modType")!='067'){
						 //栏目类型
						 var lanmuType = $(this).parents(".indexLanmu").attr("lanmuType");
						 //栏目类型去重
						 if($.inArray(lanmuType, array)<0){
							 array.push(lanmuType);
						 }
						 if(lanmuType=='020'){
							 $(this).parents(".order-item").remove();
							 resetAllTodo();
						 }
						 if(!flag){
							 //修改未读数
							 changeNoReadNum(modType);
							 flag = true;
						 }
					 }else{
						 changeNoReadNum(modType);
					 }
				 }else{
					 //栏目类型
					 var lanmuType = $(this).parents(".indexLanmu").attr("lanmuType");
					 if(lanmuType=='020'){
						 if($(this).attr("modType")!='003' 
							 && $(this).attr("modType")!='015' 
							 && $(this).attr("modType")!='022' 
							 && $(this).attr("modType")!='027010' 
							 && $(this).attr("modType")!='027020' 
							 && $(this).attr("modType")!='046' 
							 && $(this).attr("modType")!='047' 
							 && $(this).attr("modType")!='00306'
							 && $(this).attr("modType")!='06601'
							 && $(this).attr("modType")!='067'){//非任务模块移除
							 $(this).parents(".order-item").remove();
							 resetAllTodo();
						 }
						 
					 }
				 }
			 });
		 }
		 if($.inArray('020', array)>=0){//待办事项有移除的数据
			var lanmuType = $("#allLanmu").find("[lanmuType='020']");
			//待办事项个数
			var len = $(lanmuType).children().length;
			//待办数
			var allTodoNums = parseInt($.trim($("#allTodoNums").html()));
			if(len<5 && len<allTodoNums){
				$.ajax({
					 type: "post",
			 		 url:"/loadIndexMoreTodo?sid="+sid,
			 		 dataType: "json",
			 		 data:{todoIds:todoIdArray.join(","),leftNum:len},
			 		 success:function(data){
			 			 if(data.status=='y'){
			 				 var list = data.list;
			 				 var html = getTodoLanmu(list);
			 				 $(lanmuType).append(html);
			 			 }
			 		 }
			 	});
			}
		 }
	 }else if(page=='allTodo'){//待办中心
		 if($(ts).hasClass("noread")){
			 var modId = $(ts).attr("modId");
			 var modType = $(ts).attr("modType");
			 var isClock = $(ts).attr("isClock");
			 //移除未读表示
			 $(ts).removeClass("noread");
			 $(ts).parent().find("span").removeClass("noread");
			 //修改未读数
			 changeNoReadNum(modType);
		 }
		 loadOtherTodo(ts);
	 }else if(page=='msgShare'){//信息分享
		 if($(ts).parents(".message-content").find("span").hasClass("noread")){
			 $(ts).parents(".message-content").find("span").removeClass("noread");
		 }
		 if($(ts).parents(".message-content").find("a").hasClass("noread")){
			 $(ts).parents(".message-content").find("a").removeClass("noread");
			 var modType = $(ts).attr("modType");
			 var isClock = $(ts).attr("isClock");
			 if(isClock==1){
				 changeNoReadNum('101');
				 changeMsgNoReadNum('101')
			 }else{
				 changeNoReadNum(modType);
				 changeMsgNoReadNum(modType)
			 }
		 }
		 var readState = $("#readState").val();
		 if(readState && readState==0){
			 getNoReadList(ts)
		 }
		 
	 }else if(page=='atten'){//关注
		 if($(ts).parents(".message-content").find("span").hasClass("noread")){
			 $(ts).parents(".message-content").find("span").removeClass("noread");
		 }
		 if($(ts).parents(".message-content").find("a").hasClass("noread")){
			 $(ts).parents(".message-content").find("a").removeClass("noread");
			 if(modType==1){
				 changeNoReadNum('100');
				 changeAttNoReadNum('100')
			 }else{
				 changeNoReadNum(modType);
				 changeAttNoReadNum(modType)
			 }
		 }
		 
	 }else if(page=='crm'){//客户中心
		 if($(ts).parents(".message-content").find("a").hasClass("noread")){
			 $(ts).parents(".message-content").find("a").removeClass("noread");
			 changeNoReadNum(modType);
		 }
	 }else if(page=='week'){//周报中心
		 if($(ts).parents("tr").find("a").hasClass("noread")){
			 $(ts).parents("tr").find("a").removeClass("noread");
			 changeNoReadNum(modType);
		 }
	 }else if(page=='daily'){//分享中心
         if($(ts).parents("tr").find("a").hasClass("noread")){
             $(ts).parents("tr").find("a").removeClass("noread");
             changeNoReadNum(modType);
         }
     }else{
		 if($(ts).hasClass("noread")){
			 $(ts).removeClass("noread");
			 changeNoReadNum(modType);
		 }
	 }
}
 
 /**
  * 构造跳转地址,防止页面有页码，没有数据
  * @param url
  * @returns
  */
 function reConstrUrl(ckBoxName){
	var url = window.self.location+'';
	//页面所有数据总和
	var allLen = $(":checkbox[name='"+ckBoxName+"']").length;
	//页面数据选中项数目
	var chooseLen = $(":checkbox[name='"+ckBoxName+"']:checked").length;
	
	if(allLen==chooseLen){//选中的是页面所有数据
		//若是第一页，则不处理，否则处理数据
		var urlOffset = location.search.match(/pager.offset=(\d+)/);
		var urlPageSize = location.search.match(/pager.pageSize=(\d+)/);
		
		//当前分页索引号，默认为0
		var offset = 0;
		if(urlOffset){
			offset = urlOffset[1];
		}
		//分页数，默认15
		var pageSize = 15;
		if(urlPageSize){
			pageSize = urlPageSize[1];
		}
		
		if(offset>0){
			offset = offset-pageSize;
			url = url.replace(/pager.offset=(\d+)/,'pager.offset='+offset);
		}
	}
	return url;
 }
 //跳转到第一页
 function urlPageFirst(){
	var url = window.self.location+'';
	if(location.search.match(/pager.offset=(\d+)/)){//查询分页数
		url = url.replace(/pager.offset=(\d+)/,'pager.offset=0');
	}
	return url;
 }
/**
 * 页面移除指定个数的数据，重构页面跳转url,防止页面有页码，没有数据
 * @param listClz 条目的class
 * @param num 本次移除的数据条数
 * @return
 */
 function reConUrlByClz(listClz,num){
 	var url = window.self.location+'';
 	//页面所有数据总和
 	var allLen = $(listClz).length-num;
 	if(allLen<=0){//没有展示的数据了，需要重构跳转页面url
 		//若是第一页，则不处理，否则处理数据
 		var urlOffset = location.search.match(/pager.offset=(\d+)/);
 		var urlPageSize = location.search.match(/pager.pageSize=(\d+)/);
 		
 		//当前分页索引号，默认为0
 		var offset = 0;
 		if(urlOffset){
 			offset = urlOffset[1];
 		}
 		//分页数，默认15
 		var pageSize = 15;
 		if(urlPageSize){
 			pageSize = urlPageSize[1];
 		}
 		
 		if(offset>0){
 			offset = offset-pageSize;
 			url = url.replace(/pager.offset=(\d+)/,'pager.offset='+offset);
 		}
 	}
 	return url;
 }
 
//设置人员名称
 function setUser(userIdTag, userNameTag,userId,ts){
	var tagName = $(ts).get(0).tagName;
	var img = $(ts).find("img");
	if(tagName == 'IMG'){
		img = $(ts);
	}
	
	
 	$('#'+userIdTag).val(userId);
 	$("#userOneImg"+userIdTag).attr("src",$(img).attr("src"));
 	$("#userOneImg"+userIdTag).attr("title",$(img).attr("title"));
 	$("#userOneImg"+userIdTag).css("display","block");
 	//人名赋值
 	$("#userOneName_"+userIdTag).text($(img).attr("title"));
 }
 //申请人员审核
 function checkApplyInUser(busId){
	 layer.closeAll();
		var url="/userInfo/checkUserInfoPage?sid="+sid+"&id="+busId;
		layui.use('layer', function(){
			var layer = layui.layer;
			window.top.layer.open({
				  id:'layerOpener',
				  type: 2,
				  title: false,
				  closeBtn: 0,
				  shade: 0.5,
				  shift:0,
				  scrollbar:false,
				  fix: true, //固定
				  maxmin: false,
				  move: false,
				  area: ['550px', '400px'],
				  content: [url,'no'], //iframe的url
				  btn:['审核','取消'],
				  yes:function(index,layero){
					  var iframeWin = window[layero.find('iframe')[0]['name']];
					  iframeWin.checkUserInfo();
				  },
				  success: function(layero,index){
					    var iframeWin = window[layero.find('iframe')[0]['name']];
					    iframeWin.setWindow(window.document,window);
					}
				});
		});
	 
 }
 //验证是否有上级
 function checkLeader(sid,callback){
	 $.ajax({
		   type: "POST",
		   dataType: "json",
		   url: "/userInfo/checkLeader?sid="+sid,
		   success: function(data){
			  //若是有回调
			  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
				  callback(data);
			  }
		   }
	})
 }
 //强制参与人的的设置头部
 var forceTypeNames = {
		 "type003":"任务中心"
		,"type005":"项目中心"
		,"type006":"周报中心"
		,"type012":"客户中心"
		,"type022":"审批中心"
		,"type033":"差旅管理"
		,"type050":"分享中心"
		,"type080":"产品中心"
 }
//督察人员维护
 function forceIn(sid,ts,busType){
 	//当前活动选项
 	var preActive = $(".submenu").find(".active");
 	//当前活动选项移除背景色
 	$(preActive).removeClass();
 	//配置设置背景色
 	$(ts).parent().addClass("active bg-themeprimary");
 	
 	var title = forceTypeNames['type'+busType];
 	if(!title){
 		title = "";
 	}
 	
 	tabIndex = layer.open({
 	  type: 2,
 	  title:false,
 	  closeBtn:0,
 	  //title: [title+'监督人员配置', 'font-size:18px;'],
 	  area: ['550px', '400px'],
 	  fix: false, //不固定
 	  maxmin: false,
 	  scrollbar:false,
 	  content: ["/forceIn/editSingleForceInPersionPage?sid="+sid+"&busType="+busType,'no'],
 	  btn: ['关闭'],
 	  yes: function(index, layero){
 		 layer.close(index)
 		 //跳转到第一页
 		  window.self.location = urlPageFirst();
 	  },cancel: function(){ 
 		  //跳转到第一页
 		  window.self.location = urlPageFirst();
 	  },success: function(layero,index){
 		    var iframeWin = window[layero.find('iframe')[0]['name']];
 		    iframeWin.setWindow(window.document,window);
 	  },end:function(index){
 		//配置删除背景色
 		  $(ts).parent().removeClass();
 		  //恢复前一个选项的背景色
 		  $(preActive).addClass("active bg-themeprimary");
 	  }
 	});
 }
 
 function pfSubSet(sid,ts){
	//当前活动选项
	 	var preActive = $(".submenu").find(".active");
	 	//当前活动选项移除背景色
	 	$(preActive).removeClass();
	 	//配置设置背景色
	 	$(ts).parent().addClass("active bg-themeprimary");
	 	
	 	var title ="评分范围设置"
	 	
	 	tabIndex = layer.open({
	 	  type: 2,
	 	  title:false,
	 	  closeBtn:0,
	 	  //title: [title+'监督人员配置', 'font-size:18px;'],
	 	  area: ['550px', '400px'],
	 	  fix: false, //不固定
	 	  maxmin: false,
	 	  scrollbar:false,
	 	  content: ["/jfMod/editJfSubScopePage?sid="+sid,'no'],
	 	  btn: ['关闭'],
	 	  yes: function(index, layero){
	 		 layer.close(index)
	 		 //跳转到第一页
	 		  window.self.location = urlPageFirst();
	 	  },cancel: function(){ 
	 		  //跳转到第一页
	 		  window.self.location = urlPageFirst();
	 	  },success: function(layero,index){
	 		    var iframeWin = window[layero.find('iframe')[0]['name']];
	 		    iframeWin.setWindow(window.document,window);
	 	  },end:function(index){
	 		//配置删除背景色
	 		  $(ts).parent().removeClass();
	 		  //恢复前一个选项的背景色
	 		  $(preActive).addClass("active bg-themeprimary");
	 	  }
	 	});
 }
 
//设置周报查看权限
 function setWeekRepViewScope(sid){
 	window.top.layer.open({
 		 type: 2,
 		  //title: ['汇报范围设置', 'font-size:18px;'],
 		 title:false,
 		 closeBtn:0,
 		  area: ['600px', '460px'],
 		  fix: true, //不固定
 		  maxmin: false,
 		  scrollbar:false,
 		  move: false,
 		  content:['/weekReport/weekViewSetPage?sid='+sid+'&t='+Math.random(),'no'],
 		  btn: ['关闭'],
 		  yes: function(index, layero){
 			  window.top.layer.close(index)
 		  },cancel: function(){ 
 		  },success:function(layero,index){
 				var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
 				  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
 					  window.top.layer.close(index);
 				  })
 		 },end:function(index){
 			  checkLeader(sid,function(data){
 				   if(data.status!='y'){
 					   window.top.layer.close(tabIndex);
 				   }
 				})
 		  }
 	 });
 }

 /**
  * 取得模块的闹铃
  * @param busType业务类型
  * @param busId 阢主键
  * @param sid session标识
  * @param callback 回调函数
  */
 function ajaxListClockForOne(busType,busId,sid,callback){
	 $.ajax({
		   type: "POST",
		   dataType: "json",
		   url: "/clock/ajaxListClockForOne?sid="+sid,
		   data:{busType:busType,busId:busId},
		   success: function(data){
			  //若是有回调
			  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
				  callback(data);
			  }
		   }
	})
 }
 //构造已有闹铃html
 function conStrClockHtml(busType,busId,sid,result){
	var clockRepType = result.clockRepType;
	var clockRepDate = result.clockRepDate;
	var clockTime = result.clockTime;
	var clockDate = result.clockDate;
	var clockId = result.id;
	var repDate7='';
	if(clockRepType==2){
		var len = clockRepDate.length;
		var repDate0 = clockRepDate.substr(0,len-1);
		var repDate1 = repDate0.replace("1","周日");
		var repDate2 = repDate1.replace("2","周一");
		var repDate3 = repDate2.replace("3","周二");
		var repDate4 = repDate3.replace("4","周三");
		var repDate5 = repDate4.replace("5","周四");
		var repDate6 = repDate5.replace("6","周五");
		repDate7= repDate6.replace("7","周六");
	}
	var html ='\n <li title="'+repDate7+'" id="clock'+clockId+'">';
	html+='\n <a href="javascript:void(0)" style="color: #000000;margin-right: 0px" onclick="editClock('+busId+',\''+busType+'\','+clockId+',\''+sid+'\',this)">';
	if(clockRepType==0){//仅一次
		html+='\n <i class="fa fa-clock-o clockGreen" style="background-color: #fff" title="'+clockDate.substr(2,10)+'"></i>'+clockTime;
		html+='\n (单次)';
	}else if(clockRepType==1){//每天
		html+='\n <i class="fa fa-clock-o clockGreen" style="background-color: #fff"></i>'+clockTime;
		html+='\n (每天)';
	}else if(clockRepType==2){//每周
		html+='\n <i class="fa fa-clock-o clockGreen" style="background-color: #fff"></i>'+clockTime;
		html+='\n (每周)';
	}else if(clockRepType==3){//每月
		html+='\n <i class="fa fa-clock-o clockGreen" style="background-color: #fff"></i>'+clockTime;
		html+='\n (每月'+clockRepDate+'日)';
	}else if(clockRepType==4){//每年
		var showTime = clockDate.substr(5,10);
		html+='\n <i class="fa fa-clock-o clockGreen" style="background-color: #fff"></i>'+clockTime;
		html+='\n (每年'+showTime+')';
	}
	html+='\n</a>';
	html+='\n </li>';
	return html;
		
 }
 //构建编辑对话框
 function constrDialog(){
	 var html='<div id="pContentLayerBody" class="container no-padding" style="width: 100%">	';
	 html +='		<div class="widget" style="border-bottom: 0px">';
	 html +='			<div class="widget-header bordered-bottom bordered-themeprimary detailHead">';
	 html +='				<span class="widget-caption themeprimary ps-layerTitle"></span>';
	 html +='				<div class="widget-buttons">';
	 html +='					<a href="javascript:void(0)" id="dislogCloseBtn" title="关闭">';
	 html +='						<i class="fa fa-times themeprimary"></i>';
	 html +='					</a>';
	 html +='				</div>';
	 html +='			</div>';
	 html +='			<div id="contentLayerBody" class="widget-body margin-top-40" style="border-bottom:0">';
	 html +='			</div>';
	 html +='		</div>';
	 html +='	</div>';
	 return html;
 }
 //取得自定义返回json数据
 function getSelfJSON(url,params,callback){
	 if(params){
		 //设置时间随机数
		 params.t =  Math.random();
		 
		 $.getJSON(url,params).done(function (data) {
			 callback(data)
		 }).error(function() {console.log('url:'+url)});;
		 
	 }else{
		 $.getJSON(url).done(function (data) {
			 callback(data)
		 });
	 }
 }
 
 //取得自定义返回json数据
 function postUrl(url,params,callback){
	 if(params){
		 var params = $.extend(params,{"t":Math.random()})
		 $.post(url,params,function (data) {
			 callback(data)
		 },"json");
		 //.error(function() {showNotification(2,"系统错误，请联系管理人员！");});
		 
	 }else{
		 $.postJSON(url,function (data) {
			 callback(data)
		 },"json");
	 }
 }
 
 /**
  * 弹窗设置直属上级
  * @param sid 身份验证关键字
  */
 function setDirectLeader(sid){
 	 window.top.layer.open({
 		 type: 2,
 		  //title: ['审批人员设置', 'font-size:18px;color:red;'],
 		  title:false,
 		  closeBtn:0,
 		  area: ['550px', '320px'],
 		  fix: true, //不固定
 		  maxmin: false,
 		  scrollbar:false,
 		  move: false,
 		  content:['/userInfo/setDirectLeaderPage?sid='+sid+'&t='+Math.random(),'no'],
 		  btn: ['关闭'],
 		  yes: function(index, layero){
 			  window.top.layer.close(index)
 		  },cancel: function(){ 
 		  },success: function(layero,index){ 
 			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
 			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
 				  window.top.layer.close(index);
 				})
 		  },end:function(index){
 			  checkLeader(sid,function(data){
 				   if(data.status!='y'){
 					   window.top.layer.close(index);
 				   }
 				})
 		  }
 	 });
 }
 
 //暂停执行 毫米级别
 function sleep(numberMillis) { 
 	var now = new Date(); 
 	var exitTime = now.getTime() + numberMillis; 
 	while (true) { 
	 	now = new Date(); 
	 	if (now.getTime() > exitTime)return; 
 	} 
}
 
 /**
  * 查看组织购买服务信息
  * @param sid
  */
 function organicSpaceCfgInfo(sid){
	 layui.use('layer', function(){
		var layer = layui.layer;
		var url = "/organic/organicSpaceCfgInfo?sid="+sid;
		tabIndex = window.top.layer.open({
			type: 2,
			title: false,
			closeBtn: 0,
			shadeClose: false,
			shift:0,
			zIndex:1010,
			scrollbar:false,
			fix: true, //固定
			maxmin: false,
			move: false,
			area: ['500px',"60%"],
			content: [url,'no'], //iframe的url
			success:function(layero,index){
				
			},end:function(){
				
			}
		});
	})
 }
 
//关联流程选择
 function spFlowModelForSelect(sid){
 	window.top.layer.open({
 		 type: 2,
 		  title:false,
 		  closeBtn:0,
 		  area: ['600px', '500px'],
 		  fix: true, //不固定
 		  maxmin: false,
 		  move: false,
 		  content: ['/flowDesign/listFlowModelForSelect?pager.pageSize=10&sid='+sid,'no'],
 		  btn: ['选择', '取消'],
 		  yes: function(index, layero){
 			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
 			  var result = iframeWin.flowSelected();
 			  if(result){
 				  flowSelectedReturn(result.flowId,result.flowName);//页面回调赋值
 				  window.top.layer.close(index)
 			  }
 		  }
 	});
 }
 
 //查看公文正文
 function viewOfficalDoc(uuid,sid){
	 var mhtmlHeight = window.screen.availHeight;//获得窗口的垂直位置;
		var mhtmlWidth = window.screen.availWidth; //获得窗口的水平位置; 
		var iTop = 0; //获得窗口的垂直位置;
		var iLeft = 0; //获得窗口的水平位置;
	    var url = '/iWebOffice/viewIWebDocPage?sid='+sid+'&fileUuid='+uuid;
	    var aa =  window.open(url,'公文正文','height='+mhtmlHeight+',width='+mhtmlWidth+',top='+iTop+',left='+iLeft+',toolbar=no,menubar=yes,scrollbars=no,resizable=no, location=no,status=no');  
 }
 
 /**
  * 归档文件夹选择
  * @param sid session标识
  * @param dirId 需要排除的文件夹主键 0 不需要排除
  * @param dirName 需要排除的文件夹名称
  * @param callback 回调函数
  */
 function officalDirSelect(sid,dirId,dirName,callback){
	 window.top.layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  area: ['400px', '450px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  scrollbar:false,
		  content: ['/officalDir/officalDirSelectOnePage?sid='+sid+'&t=' + Math.random(),'no'],
		  btn: ['确定','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.returnDir();
			  if(result){
				  callback(result)
				  window.top.layer.close(index);
			  }
		  },cancel: function(){ 
		    //右上角关闭回调
		  },success: function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.initData(dirId,dirName);
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
				})
		  }
	});
 }
 /**
  * 组织多选树
  */
 function orgMoreTree(sid,callBackStart){
	 window.top.layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  area:  ["450px", "530px"],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/common/orgMoreSelect/orgMorePag?sid="+sid,'no'],
		  btn: ['确定','清空','取消'],
		  success:function(layero,index){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  });
		  },
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  var orgs = iframeWin.returnOrg();
			  if(callBackStart && Object.prototype.toString.call(callBackStart) === "[object Function]"){
				  callBackStart(orgs);
			  }
			  window.top.layer.close(index);
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeAll();
			  return false;
		  }
		});
 }
 /**
  *  组织部门多选树
  */
 function orgDepMoreTree(sid,callBackStart){
	 window.top.layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  area:  ["500px", "530px"],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/common/depMoreSelect/orgDepMore?sid="+sid,'no'],
		  btn: ['确定','清空','取消'],
		  success:function(layero,index){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  });
		  },
		  yes: function(index,layero){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  var deps = iframeWin.returnDep();
			  if(callBackStart && Object.prototype.toString.call(callBackStart) === "[object Function]"){
				  callBackStart(deps);
			  }
			  window.top.layer.close(index);
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeAll();
			  return false;
		  }
		});
 }
 /**
  * 组织人员多选树
  */
 function orgUserMoreTree(sid,callBackStart){
	 window.top.layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  area: ["750px", "530px"],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/common/userMoreSelect/orgUserMore?sid="+sid,'no'],
		  btn: ['确定','清空','取消'],
		  success:function(layero,index){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  });
		  },
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  var users = iframeWin.returnUser();
			  if(callBackStart && Object.prototype.toString.call(callBackStart) === "[object Function]"){
				  callBackStart(users);
			  }
			  window.top.layer.close(index);
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeAll();
			  return false;
		  }
		});
 }
 function crmTypeMoreTreeCallBack(options,tag){
 	
 }
  /**
  * 客户类型多选树
  */
 function crmTypeMoreTree(sid,tag){
	 window.top.layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  area: ["500px", "470px"],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/common/crmTypeMorePage?sid="+sid,'no'],
		  btn: ['确定','清空','取消'],
		  success:function(layero,index){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  var options = $("#"+tag+"_select").find("option")
			  iframeWin.setOptions(options);
			   
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  });
		  },
		   yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var options = iframeWin.returnCrmType();
				if(options.length>0){
					//人选被选择后执行动作函数体
					crmTypeMoreTreeCallBack(options,tag);
					window.top.layer.close(index)
				}else{
					layer.msg("请选择客户类型")
					return false;
				}
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeAll();
			  return false;
		  }
		});
 }
 /*post请求下载文件
  * options:{
  * url:'',  //下载地址
  * data:{name:value}, //要发送的数据
  * method:'post'
  * }
  */
 var postDownLoadFile = function (options) {
	    var config = $.extend(true, { method: 'post' }, options);
	    var $iframe = $('<iframe id="down-file-iframe" />');
	    var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');
	    $form.attr('action', config.url);
	    for (var key in config.data) {
	        $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
	    }
	    $iframe.append($form);
	    $(document.body).append($iframe);
	    $form[0].submit();
	    $iframe.remove();
	}
 //页面表格导出excel
function tableToXls(tableHtml,fileName,sid){
	tableHtml = tableHtml.replace(/"/g,'&quot;');
	tableHtml = tableHtml.replace(/'/g,"&quot;");
	var url ='/common/tableToXls?sid='+sid;
        var param={
    		"fileName":fileName,
    		"tableHtml":tableHtml,
        };
        postDownLoadFile({
          url:url,
          data:param,
          method:'post'
        });
}

/**
 * 跳转业务关联配置页面
 * @param sid 身份验证令牌
 * @param busType 业务类型
 * @param callBackUrl 回调地址
 */
function busMapFlowCfg(sid,busType,callBackUrl,actionId){
	window.top.layer.open({
		type: 2,
		title: false,
		closeBtn:0,
		area: ['950px','550px'],
		fix: true, //不固定
		maxmin: false,
		scrollbar:false,
		move: false,
		zIndex:1010,
		shade:0.3,
		scrollbar:false,
		content: ["/adminCfg/busMapFlowCfg?sid="+sid+"&busType="+busType+"&actionId="+actionId,'no'],
		btn: ['确认','关闭'],
		yes: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.formSubmit(callBackUrl);
		},end:function(){
			window.self.location.reload();
		}
	});
}
//浮点数判定
function isFloat(str){
	 var r =/^(([0-9]+)|([0-9]+\.[0-9]{1,2}))$/
	 return r.test(str);
}
//数字转换成大写的人民币
function changeNumMoneyToChinese(money) {
	  var cnNums = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"); //汉字的数字
	  var cnIntRadice = new Array("", "拾", "佰", "仟"); //基本单位
	  var cnIntUnits = new Array("", "万", "亿", "兆"); //对应整数部分扩展单位
	  var cnDecUnits = new Array("角", "分", "毫", "厘"); //对应小数部分单位
	  var cnInteger = "整"; //整数金额时后面跟的字符
	  var cnIntLast = "元"; //整型完以后的单位
	  var maxNum = 999999999999999.9999; //最大处理的数字
	  var IntegerNum; //金额整数部分
	  var DecimalNum; //金额小数部分
	  var ChineseStr = ""; //输出的中文金额字符串
	  var parts; //分离金额后用的数组，预定义
	  if (money == "") {
	    return "";
	  }
	  money = parseFloat(money);
	  if (money >= maxNum) {
	    alert('超出最大处理数字');
	    return "";
	  }
	  if (money == 0) {
	    ChineseStr = cnNums[0] + cnIntLast + cnInteger;
	    return ChineseStr;
	  }
	  money = money.toString(); //转换为字符串
	  if (money.indexOf(".") == -1) {
	    IntegerNum = money;
	    DecimalNum = '';
	  } else {
	    parts = money.split(".");
	    IntegerNum = parts[0];
	    DecimalNum = parts[1].substr(0, 4);
	  }
	  if (parseInt(IntegerNum, 10) > 0) { //获取整型部分转换
	    var zeroCount = 0;
	    var IntLen = IntegerNum.length;
	    for (var i = 0; i < IntLen; i++) {
	      var n = IntegerNum.substr(i, 1);
	      var p = IntLen - i - 1;
	      var q = p / 4;
	      var m = p % 4;
	      if (n == "0") {
	        zeroCount++;
	      } else {
	        if (zeroCount > 0) {
	          ChineseStr += cnNums[0];
	        }
	        zeroCount = 0; //归零
	        ChineseStr += cnNums[parseInt(n)] + cnIntRadice[m];
	      }
	      if (m == 0 && zeroCount < 4) {
	        ChineseStr += cnIntUnits[q];
	      }
	    }
	    ChineseStr += cnIntLast;
	    //整型部分处理完毕
	  }
	  if (DecimalNum != '') { //小数部分
	    var decLen = DecimalNum.length;
	    for (var i = 0; i < decLen; i++) {
	      var n = DecimalNum.substr(i, 1);
	      if (n != '0') {
	        ChineseStr += cnNums[Number(n)] + cnDecUnits[i];
	      }
	    }
	  }
	  if (ChineseStr == '') {
	    ChineseStr += cnNums[0] + cnIntLast + cnInteger;
	  } else if (DecimalNum == '') {
	    ChineseStr += cnInteger;
	  }
	  return ChineseStr;
	 
	}
//办公用品
function bgypSelectMore(preBgypIds,callback){
	window.top.layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  area: ['750px', '530px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/common/bgypMorePage?sid=' + sid,'no'],
		  btn: ['确定','清空','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var options = iframeWin.returnBgypItems();
			  //若是有回调
			  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
				  callback(options);
			  }else{
			  }
				window.top.layer.close(index)
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeAllOptions();
			  return false;
		  },cancel: function(){ 
		    //右上角关闭回调
		  },success: function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.initData(preBgypIds);
			  //设置点击关闭
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
		});
}
//办公用品库存选择
function bgypStoreSelectMore(preBgypIds,callback){
	window.top.layer.open({
		type: 2,
		title:false,
		closeBtn:0,
		area: ['750px', '530px'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		content: ['/common/bgypStoreMorePage?sid=' + sid,'no'],
		btn: ['确定','清空','取消'],
		yes: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			var options = iframeWin.returnBgypItems();
			//若是有回调
			if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
				callback(options);
			}else{
			}
			window.top.layer.close(index)
		},btn2: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.removeAllOptions();
			return false;
		},cancel: function(){ 
			//右上角关闭回调
		},success: function(layero,index){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.initData(preBgypIds);
			//设置点击关闭
			$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				window.top.layer.close(index);
			})
		}
	});
}
//办公用品分类主键
function bgflOneSelect(elementId, elementName, queryParam, sid,callBack){
	window.top.layer.open({
		type: 2,
		title:false,
		closeBtn:0,
		area: ['400px', '450px'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		scrollbar:false,
		content: ['/common/bgflOnePage?sid=' + sid,'no'],
		btn: ['确定','取消'],
		yes: function(index, layero){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			var result = iframeWin.returnBgfl();
			if(result){
				var flag = true;
				if(callBack && Object.prototype.toString.call(callBack) === "[object Function]"){
					flag = callBack(result);
				}else if(callBack=="yes"){
					bgflOneCallBack(result);
				}
				if(flag){
					window.top.layer.close(index)
				}
			}
		},cancel: function(){ 
			//右上角关闭回调
		},success: function(layero,index){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				window.top.layer.close(index);
			})
		}
	});
}

//流程查看页面
function viewSpFlow(instanceId){
	var url = "/workFlow/viewSpFlow?sid="+sid+"&instanceId="+instanceId;
	openWinByRight(url);
}
//车牌号
function isVehicleNumber(str) {
    var result = false;
    if (str.length == 7){
      var express = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/;
      result = express.test(str);
    }
    return result;
}
//正整数
function isPInt(str){ 
  var reg = /^[1-9]\d*$/;
  return reg.test(str);
}
//检测IP
function isValidIP(ip)     
{     
	 var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
    return reg.test(ip);     
}    

//取得字典表信息
function listDataDic(type,callback){
	getSelfJSON("/common/listDataDic",{sid:sid,type:type},function(dataDics){
	  //若是有回调
	  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
		  callback(dataDics);
	  }
	})
}

//常用人员异步查询
function listUsedUser(userNum,callback){
	getSelfJSON("/userInfo/listUsedUser",{sid:sid,userNum:userNum},function(data){
		  //若是有回调
		  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
			  if(data.status=='y'){
				  callback(data)
			  }
		  }
	})
}
var BusMapFlowName = {
		"type03401":"出差汇报流程"
		,"type03402":"一般汇报流程"
		,"type029":"出差流程"
		,"type030":"借款流程"
		,"type031":"报销流程"
		,"type035":"一般借款流程"
		,"type036":"请假申请"
		,"type037":"加班申请"
		,"type070":"需求处理流程"
}
//取得需要简历映射关系的数据集合
function listBusMapFlows(type,callback){
	var url = "/adminCfg/listBusMapFlowByAuth";
	var param ={sid:sid,busType:type};
	//为兼容之前版本；所以stype参数分字符串对象和OBJECT对象
	if(Object.prototype.toString.call(type) === "[object Object]"){
		param ={sid:sid,busType:type.busType,feeBudgetId:type.feeBudgetId};
	}
	getSelfJSON(url,param,function(data){
		  //若是有回调
		  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
			  if(data.status=='y'){
				  var listBusMapFlows = data.listBusMapFlows;
				  if(listBusMapFlows && listBusMapFlows.length>=1){
					  listBusMapFlows.length == 1 ? callback(listBusMapFlows[0]) : listBusMapSelect(listBusMapFlows,type,callback)
				  }else{
					  var typeName =  BusMapFlowName["type"+type];
					  typeName = typeName?typeName:type
					  window.top.layer.confirm("请联系管理员配置“"+typeName+"”！", {
						  btn: ['确定']//按钮
					  ,title:'提示框'
						  ,icon:2
					  });
				  }
			  }else{
				  showNotification(2, data.info);
			  }
		  }
		})
}
//取得需要简历映射关系的数据集合
function listBusMapSelect(listBusMapData,type,callback){
	if(!listBusMapData){
		 var typeName =  BusMapFlowName["type"+type];
		  typeName = typeName?typeName:type
		  window.top.layer.confirm("请联系管理员配置“"+typeName+"”！", {
			  btn: ['确定']//按钮
		  ,title:'提示框'
			  ,icon:2
		  });
	}else if(listBusMapData.length == 1){
		callback(listBusMapData[0]);
	}else{
		window.top.layer.open({
			type : 2,
			title : false,
			closeBtn : 0,
			area : [ '500px', '450px' ],
			fix : true, // 不固定
			maxmin : false,
			move : false,
			scrollbar:false,
			content : [
			           '/adminCfg/listBusMapSelect?sid=' + sid + "&busType=" + type, 'no' ],
			           btn : [ '选择', '取消' ],
			           yes : function(index, layero) {
			        	   var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			        	   var result = iframeWin.flowSelected();
			        	   if (result) {
			        		   window.top.layer.close(index)
			        		   callback(result);
			        	   }
			        	   //右上角关闭回调
			           },success:function(layero,index){
			        	   var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			        	   iframeWin.initListData(listBusMapData);
			        	   $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			        		   window.top.layer.close(index);
			        	   })
			        	   $(iframeWin.document).find("#dataBody tr").on("dblclick",function(){
			        		   var result = iframeWin.flowSelected();
			        		   if (result) {
			        			   window.top.layer.close(index)
			        			   callback(result);
			        		   }
			        	   })
			        	   
			           }
		});
	}
}
//计算乘法
function accMul(arg1,arg2){   
	  
    var m=0,s1=arg1.toString(),s2=arg2.toString();   
  
    try{m+=s1.split(".")[1].length}catch(e){}   
  
    try{m+=s2.split(".")[1].length}catch(e){}   
  
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)   
  
} 

//加法函数，用来得到精确的加法结果 
//说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。 
//调用：accAdd(arg1,arg2) 
//返回值：arg1加上arg2的精确结果 
function accAdd(arg1,arg2){ 
	var r1,r2,m; 
	try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
	try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
	m=Math.pow(10,Math.max(r1,r2)) 
	return (arg1*m+arg2*m)/m 
}

//除法函数
 function accDiv(arg1, arg2) {
     var t1 = 0, t2 = 0, r1, r2;
     try {
         t1 = arg1.toString().split(".")[1].length;
     }
     catch (e) {
     }
     try {
         t2 = arg2.toString().split(".")[1].length;
     }
     catch (e) {
     }
     with (Math) {
         r1 = Number(arg1.toString().replace(".", ""));
         r2 = Number(arg2.toString().replace(".", ""));
         return (r1 / r2) * pow(10, t2 - t1);
     }
 } 
//乘法函数
 function accMul(arg1, arg2) {
	 var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
	 try {
	 m += s1.split(".")[1].length;
	}
	 catch (e) {
	}
	try {
	 m += s2.split(".")[1].length;
	}
	catch (e) {
	}
	return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
	} 

//操作函数
function doCloseFun(type){
	console.log(1232)
}
//获取引导状态
function queryIntroState(sid,busType){
	var st = false ;
	postUrl('/intro/ajaxIntroState', {
		sid: sid,
		busType: busType,
	},function(data) {
		if (data.status == 'y') {
			st =  data.intro;
		} else {
			showNotification(2, data.info);
		}
	});
	return st;
}
//添加引导
function addIntro(sid,busType){
	postUrl('/intro/ajaxAddIntro', {
		sid: sid,
		busType: busType,
	},
	function(data) {
		if (data.status == 'y') {
		} else {
			showNotification(2, data.info);
		}
	});
}
/**
 * 计算时间差
 * @param sDate1 开始时间
 * @param sDate2 结束时间爱你
 * @param type d
 */
function DateDiff(dateStrS,dateStrE,type){
	var iDays = 0;
	try{
		var dateStart = new Date(dateStrS);
		var dateEnd = new Date(dateStrE);
		if(type=='d'){
			iDays  =  parseInt(Math.abs(dateEnd  -  dateStart)  /  1000  /  60  /  60  /24)    //把相差的毫秒数转换为天数 
			iDays = iDays+1;
		}
	}catch(e){
		
	}
	return iDays;
}

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
//转换金额
function toDecimal2(s) {
	if(s==0){
		return 0;
	}
	s = s+'';
	var re=/(?=(?!(\b))(\d{3})+$)/g;
	
    var rs = s.indexOf('.');  
    if (rs < 0) {   
    	s = s.replace(re,",");
        rs = s.length;    
        s += '.'; 
    }else{
    	var preInt = s.substring(0,rs).replace(re,",");
    	var preFloat = s.substring(rs,s.length);
    	s = preInt+preFloat;
    	rs = s.indexOf('.');
    }    
    while (s.length <= rs + 2) {    
        s += '0';    
    }    
    return s;    
}  
//查看模块信息
function viewModInfo(busId,busType,clockId){
	authCheck(busId,busType,clockId,function(authState){
		if(busType=='005'){
			var url = "/item/viewItemPage?sid="+sid+"&id="+busId;
			openWinByRight(url);
		}else if(busType=='080'){
            var url = "/product/viewProPage?sid="+sid+"&id="+busId;
            openWinByRight(url);
        }else if(busType=='012'){
			var url = "/crm/viewCustomer?sid="+sid+"&id="+busId;
			openWinByRight(url);
		}else if(busType=='003'){
			var url = "/task/viewTask?sid="+sid+"&id="+busId;
			openWinByRight(url);
		}else if(busType=='006'){
			var url="/weekReport/viewWeekReport?sid="+sid+"&id="+busId;
			openWinByRight(url);
		}else if(busType=='050'){
            var url="/daily/viewDaily?sid="+sid+"&id="+busId;
            openWinByRight(url);
        }else if(busType=='004'){
			var url="/vote/voteDetail?sid="+sid+"&id="+busId;
			openWinByRight(url);
		}else if(busType=='022'){
			url = "/workFlow/viewSpFlow?sid="+sid+"&instanceId="+busId;
			openWinByRight(url);
		}else if(busType=='070'){
			var url = "/demand/viewDemandPage?sid="+sid;
			url = url + "&demandId="+busId;
			
			var height = getWindowHeight();
			var option = {area: ['65%', height+'px']}
			openWinByRight(url,option);
			
		}
	})
	
}

//模块权限验证
function authCheck(busId,busType,clockId,callback){
	clockId = clockId?clockId:-1
	getSelfJSON("/common/authorCheck",{sid:sid,busId:busId,busType:busType,clockId:clockId},function(data){
		if(data.status=='y'){
		  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
			  data.authState==true?callback(true):showNotification(2,"对不起，你没有查看权限")
		  }
		}else{
			showNotification(2,data.info);
		}
	});
}
function authBaseCheck(paramBase,callback){
	var param = {sid:sid};
	$.extend(param,paramBase);
	getSelfJSON("/common/authorCheck",param,function(data){
		if(data.status=='y'){
		  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
			  data.authState==true?callback(true):showNotification(2,"对不起，你没有查看权限")
		  }
		}else{
			showNotification(2,data,info);
		}
	});
}

//获取任务详情
function getTaskDetail (taskId){
    var taskInfo;
	$.ajax({
	 type : "post",
	  url : '/task/taskDetailByAjax?sid='+sid+'&rnd='+Math.random(),
	  dataType:"json",
	  traditional :true,
	  async:false,
      data:{"taskId":taskId},
      success:function(data){
    	  if(data.status=='y'){
    		  taskInfo = data.data;
    	  }else{
    		  showNotification(2,data.info);  
    	  }
      },
      error:function(XmlHttpRequest,textStatus,errorThrown){
        	showNotification(2,"系统错误，请联系管理人员");
        }
    });
    return taskInfo;
}

//构建关联样式
function initModRelateStyle(busType){
	var rowDivHtml ="<div class=\"widget no-header relativeRow\" busType=\""+busType+"\" >";
	rowDivHtml +="<div class=\"widget-body bordered-radius\">";
	rowDivHtml +="<div class=\"tickets-container tickets-bg tickets-pd\">";
	rowDivHtml +="<ul class=\"tickets-list\">";
	rowDivHtml +="<li class=\"ticket-item no-shadow clearfix ticket-normal\"></li>";
	rowDivHtml +="</ul>";
	rowDivHtml +="</div>";
	rowDivHtml +="</div>";
	rowDivHtml +="</div>";
	var rowDiv = $(rowDivHtml);
	var nameRowDiv=$("<div class=\"pull-left  ps-left-text padding-top-10\" style=\"text-align: right;\"></div>");
	var colRowDiv=$("<div class=\"pull-left col-lg-7 col-sm-7 col-xs-7\"></div>");
	var rowName = busType=="012"?"关联客户：":busType=="005"?"关联项目：":busType=="017"?"关联会议：":busType=="016"?"关联日程：":busType=="003"?"父任务：":busType=="022"?"关联审批：":busType=="070"?"关联需求：": "无效定义：";
	$(nameRowDiv).append(rowName);
	$(rowDiv).find("li").append(nameRowDiv);
	$(colRowDiv).append("<input class=\"form-control pull-left colInput\" type=\"text\" " +
			"readonly=\"readonly\" value=\"\" busType=\""+busType+"\" " +
			"title=\"双击移除\" style=\"cursor:pointer;width:75%;\">");
	$(colRowDiv).append("<a href=\"javascript:void(0);\" class=\"pull-left margin-top-10 padding-left-5 colAdd\" " +
	"busType=\""+busType+"\" style=\"font-size:10px;\">选择</a>");
	$(colRowDiv).append("<a href=\"javascript:void(0);\" class=\"pull-left margin-top-10 padding-left-5 colDel\" " +
			"busType=\""+busType+"\" style=\"font-size:10px;\">删除</a>");
	$(rowDiv).find("li").append(colRowDiv);
	return rowDiv;
}

/**
 * 弹窗查看
 * @param url 访问地址
 * @param params；width-窗口宽度；height-窗口高度；scroll；是否有滚动条；btnYes-是否需要添加按钮;closeBtn -- 是否需要关闭按钮
 * @param callback 回调函数
 * @returns
 */
function openWinWithPamsV2(url,params,callback){
	var width = params.width;
	var height = params.height;
	var scroll = params.scroll;
	scroll = scroll?scroll:"yes";
	var layerParam = {
			type: 2,
			title:false,
			//skin: 'layui-layer-molv',
			closeBtn: 0,
			shadeClose: true,
			shift:0,
			fix: true, //固定
			maxmin: false,
			move: false,
			scrollbar:false,
			area: [width, height],
			content: [url,scroll] 
		};
	
	layerParam.success = function(layero,index){
		var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		  //设置点击关闭
		  $(iframeWin.document).find(".windowClosBtn").on("click",function(){
			  window.top.layer.close(index);
		  })
		  
		  var param = {
				"index":index,
				"layero":layero,
				"fun":"success"
		  }
		  callback(param)
	}
	
	if(!params.closeBtn || params.closeBtn =='yes'){
		layerParam.btn = ["关闭"]
	}
	
	layerParam.cancel = function(index, layero){ 
		 window.top.layer.close(index);
	}
	
	if(params.btnYes){
		var btnName = params.btnYesName;
		layerParam.btn = btnName?   [btnName,"关闭"] : ["确定","关闭"];
		layerParam.yes = function(index, layero){
			var param = {
					"index":index,
					"layero":layero,
					"fun":"yes"
			}
			callback(param)
		};
		layerParam.btn2 = function(index, layero){
			 window.top.layer.close(index);
		}
	}
	window.top.layer.open(layerParam);
}

//弹窗查看
function openWinWithPams(url,width,height){
	tabIndex = window.top.layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		shadeClose: true,
		shade: 0.1,
		shift:0,
		fix: true, //固定
		maxmin: false,
		move: false,
		scrollbar:false,
		//btn:["关闭"],
		area: [width, height],
		content: [url,'no'],//iframe的url
		success: function(layero,index){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.setWindow(window.document,window);
		}
	});
}
//发起业务任务
function addBusTask(busId,busType,version){
	layui.use('layer', function(){
		window.top.layer.open({
			type: 2,
			title:false,
			closeBtn: 0,
			area: ['800px', '550px'],
			fix: true, //不固定
			maxmin: false,
			zIndex:1010,
			scrollbar:false,
			content: ["/task/addBusTaskPage?sid="+sid+"&busId="+busId+"&busType="+busType+"&version="+version,'no'],
			btn: ['发布','关闭'],
			yes: function(index, layero){
				var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				var flag = iframeWin.formSub();
				if(flag){
					window.top.layer.close(index);
					window.self.location.reload();
				}
			},cancel: function(){}
		});
	})
}

//任务复制
function openCopyTaskWindow(url,busType){
    layui.use('layer', function(){
        window.top.layer.open({
            type: 2,
            title:false,
            closeBtn: 0,
            area: ['800px', '550px'],
            fix: true, //不固定
            maxmin: false,
            //zIndex:1010,
            scrollbar:false,
            content: [url,'no'],
            btn: ['发布','关闭'],
            yes: function(index, layero){
               if(busType == "0"){
                   var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
                   iframeWin.formSub();
			   }else{
                   var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
                   var flag = iframeWin.formSub();
                   if(flag){
                       window.top.layer.close(index);
                       window.top.location.reload();
                   }
			   }
            },cancel: function(){}
        });
    })
}

/**
 * 积分标准选择
 * @param selectWay 1 默认单选
 * @param callback
 */
function jfzbChoose(selectWay,params,callBack){

	if(!selectWay || selectWay==1){
		selectWay = 1;
	}else{
		selectWay = 2;
	}
	window.top.layer.open({
		type: 2,
		title:false,
		closeBtn:0,
		area: ['750px', '530px'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		content: ['/jfzb/lisJfzbForRelevance?sid=' + sid,'no'],
		btn: ['确定','取消'],
		yes: function(index, layero){
            var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
            iframeWin.dbClickFunction();
		},cancel: function(){
			//右上角关闭回调
		},success: function(layero,index){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			 iframeWin.initSelectWay(selectWay,params);
			$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				window.top.layer.close(index);
			})
			iframeWin.dbClickFunction = function(){
                var result = iframeWin.returnJfzb();
                if(result){
                    var flag = true;
                    if(callBack && Object.prototype.toString.call(callBack) === "[object Function]"){
                        flag = callBack(result);
                    }
                    if(flag){
                        window.top.layer.close(index)
                    }
                }
			}
		}
	});
}
/**
 * 积分指标查看
 * @param params
 */
function jfzbDetailView(jfzbId){
	window.top.layer.open({
		type: 2,
		title:false,
		closeBtn:0,
		area: ['700px', '500px'],
		fix: true, //不固定
		maxmin: false,
		move: false,
		content: ['/jfzb/viewJfzbPage?sid=' + sid+'&jfzbId='+jfzbId,'no'],
		cancel: function(){ 
			//右上角关闭回调
		},success: function(layero,index){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			 iframeWin.initSelectWay(selectWay,params);
			$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				window.top.layer.close(index);
			})
		}
	});
}

var ewebeditorPopId;
var height = $(window.top).height();
var width = $(window.top).width();
function ewebMaxMin(ewebeditorId,tabUrl){
	//当前构造的url的查询参数
	var nowSearch = getSearchParams(tabUrl);
	for(key in nowSearch){
		if(key == 'id'){
			ewebeditorPopId = window.open("/static/plugins/ewebeditor/popup.htm?style=popup&link="+key+"&ewebeditorId="+ewebeditorId, "", "width="+width+",height="+height+",toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no");
			return false;
		}
	}
}
function callbackValue(ewebeditorId,val){
	document.getElementById(ewebeditorId).contentWindow.setHTML(val)
}

//取得url查询参数
function getSearchParams(url){
	 
	   /*去除？*/
	   var info = url.length > 0 ? url.substring(url.indexOf('?')+1) : " ";
	   /*以&分割字符串*/
	   var result1 = info.split("&");
	   /*存储key和value的数组*/
	   var key,value;
	   var data = [];
	   for(var i=0;i<result1.length;i++){
	       /*以=分割字符串*/
	       var result2 = result1[i].split("=");
	       key = result2[0];
	       value = result2[1];
	       data[key] = value;
	   }
	  return data;
	}
/**
 * 制定滚到到指定对象位置
 * @param pEl 容器
 * @param el 位置对象
 * @returns
 */
function scrollToLocation(pEl,el) {
	  var mainContainer = $(pEl),scrollToContainer = $(el);
	  $(mainContainer).scrollTop(
	    scrollToContainer.offset().top - mainContainer.offset().top + mainContainer.scrollTop()-120
	  );
	  //动画效果
	  //$("body").animate({
	 //   scrollTop: scrollToContainer.offset().top - mainContainer.offset().top + mainContainer.scrollTop()
	  //}, 10);//2秒滑动到指定位置
	}

/**
 * 制定滚到到指定对象位置(定格显示)
 * @param pEl 容器
 * @param el 位置对象
 * @returns
 */
function scrollToLocationV2(pEl,el) {
	  var mainContainer = $(pEl),scrollToContainer = $(el);
	  alert("mainContainer.offset().top="+mainContainer.offset().top+" &mainContainer.scrollTop()="+mainContainer.scrollTop()+" &  scrollToContainer.offset().top="+ scrollToContainer.offset().top);
	  alert("滚动："+(scrollToContainer.offset().top - mainContainer.offset().top + mainContainer.scrollTop()));
	  $(mainContainer).scrollTop(
		 scrollToContainer.offset().top - mainContainer.offset().top + mainContainer.scrollTop()-60
	  );
	  //动画效果
	  //$("body").animate({
	 //   scrollTop: scrollToContainer.offset().top - mainContainer.offset().top + mainContainer.scrollTop()
	  //}, 10);//2秒滑动到指定位置
}
//借款方式选择
function loanWayChoose(sid,callback){
 	layer.open({
 	  type: 2,
 	  title:false,
 	  closeBtn:0,
 	  area: ['550px', '400px'],
 	  fix: false, //不固定
 	  maxmin: false,
 	  scrollbar:false,
 	  content: ["/financial/loan/loanWayChoosePage?sid="+sid,'no'],
 	  btn: ['关闭'],
 	  yes: function(index, layero){
 		 layer.close(index)
 	  },success: function(layero,index){
 		   var iframeWin = window[layero.find('iframe')[0]['name']];
 		   iframeWin.setWindow(window.document,window);
 		   $(iframeWin.document).on("click","a[tripway]",function(){
 			   var tripState = $(this).attr("tripState");
 			   var needApply = $(this).attr("needApply");
 			   layer.close(index);
 			   callback(tripState,needApply)
		   })
 	  }
 	});
}
//借款方式
function loanApplyChoose(isBusinessTrip,sid,callback){
	layer.open({
	 	  type: 2,
	 	  title:false,
	 	  closeBtn:0,
	 	  area: ['670px', '510px'],
	 	  fix: false, //不固定
	 	  maxmin: false,
	 	  scrollbar:true,
	 	  content: ["/financial/loanApply/loanApplyForStartSelect?sid="+sid+"&isBusinessTrip="+isBusinessTrip,'no'],
	 	  btn: ['取消'],
	 	  yes: function(index, layero){
	 		 layer.close(index)
	 	  },success: function(layero,index){
	 		   var iframeWin = window[layero.find('iframe')[0]['name']];
	 		   iframeWin.setWindow(window.document,window);
	 		   $(iframeWin.document).on("click","a[selectBtn]",function(){
	 			  var loanApply = iframeWin.selectObj($(this));
	 			  if(loanApply){
	 				  callback(loanApply)
	 				  layer.close(index);
	 			  }
			   });
	 		   if(isBusinessTrip && isBusinessTrip === '1' ){
	 			  $(iframeWin.document).find('button[name="addLoanApply"]').html("我要出差")
	 		   }else{
	 			   $(iframeWin.document).find('button[name="addLoanApply"]').find("span").html("一般费用说明")
	 		   }
	 		  $(iframeWin.document).on("click",'button[name="addLoanApply"]',function(){
	 			 layer.close(index);
	 			 var busType = isBusinessTrip ==='1'?'029':'035';
	 			 listBusMapFlows(busType,function(busMapFlow){
					var url = "/busRelateSpFlow/loanApply/addLoanApply?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType="+busType;
					openWinByRight(url);
				})
			  });
	 	  }
	 	});
}

//报销方式选择
function loanOffWayChoose(sid,callback){
	window.top.layer.open({
 	  type: 2,
 	  title:false,
 	  closeBtn:0,
 	  area: ['550px', '400px'],
 	  fix: false, //不固定
 	  maxmin: false,
 	  scrollbar:false,
 	  content: ["/financial/loanOff/loanOffWayChoosePage?sid="+sid,'no'],
 	  btn: ['关闭'],
 	  yes: function(index, layero){
 		 layer.close(index)
 	  },success: function(layero,index){
 		    var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
 		    iframeWin.setWindow(window.document,window);
 		   $(iframeWin.document).find("a[tripway]").on("click",function(){
 			   var tripState = $(this).attr("tripState");
 			   var needReport = $(this).attr("needReport");
 			   var applyState = $(this).attr("applyState");
 			   window.top.layer.close(index);
 			   callback(tripState,needReport,applyState)
		   })
 	  }
 	});
};

//差旅直接借款
function loanForOffChoose(isBusinessTrip,sid,callback){
	window.top.layer.open({
	 	  type: 2,
	 	  title:false,
	 	  closeBtn:0,
	 	  area: ['670px', '510px'],
	 	  fix: false, //不固定
	 	  maxmin: false,
	 	  scrollbar:true,
	 	  content: ["/financial/loan/loanForOffSelect?sid="+sid+"&isBusinessTrip="+isBusinessTrip,'no'],
	 	  btn: ['关闭'],
	 	  yes: function(index, layero){
	 		window.top.layer.close(index) 
	 	  },success: function(layero,index){
	 		   var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
	 		   iframeWin.setWindow(window.document,window);
	 		   $(iframeWin.document).on("click","a[selectBtn]",function(){
	 			  var loan = iframeWin.selectObj($(this));
	 			  if(loan){
	 				 loanOffExplain(loan,isBusinessTrip,"2",sid,callback)
	 				 window.top.layer.close(index)
	 			  }
			   });
	 	  }
	 });
}


//借款方式
function loanApplyForOffChoose(isBusinessTrip,sid,callback){
	window.top.layer.open({
	 	  type: 2,
	 	  title:false,
	 	  closeBtn:0,
	 	  area: ['820px', '510px'],
	 	  fix: false, //不固定
	 	  maxmin: false,
	 	  scrollbar:true,
	 	  content: ["/financial/loanApply/loanApplyForOffSelect?sid="+sid+"&isBusinessTrip="+isBusinessTrip,'no'],
	 	  btn: ['关闭'],
	 	  yes: function(index, layero){
	 		 window.top.layer.close(index)
	 	  },success: function(layero,index){
	 		   var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
	 		   iframeWin.setWindow(window.document,window);
	 		   $(iframeWin.document).on("click","a[selectBtn]",function(){
	 			  var loanApply = iframeWin.selectObj($(this));
	 			  if(loanApply){
	 				  var loanOffDoing = loanApply.loanOffDoing;
	 				  
	 				  var loanReportId = 0;
	 				  if(loanOffDoing){
	 					  loanReportId = loanOffDoing.loanReportId;
	 				  }
	 				  loanReportId = loanReportId?loanReportId:0;
	 				  
	 				  if(isBusinessTrip=='0'){//招待活动报销
	 					 
	 					 callback(loanApply.id,loanReportId);//新增报销申请 
	 				  }else{//出差报销
	 					 var stepth = 0;
	 					 if(loanOffDoing){//没有正在报销申请
	 						stepth = loanOffDoing.stepth;
	 					 }
	 					 if(loanApply.canLoanOffRepId){//有审批通过的报告能提交报销单
	 						loanReportId = loanApply.canLoanOffRepId;
	 						stepth = 1;
	 					 }
	 					 if(stepth===0){//没有正在报销申请
	 						  var busType = '03401';
	 						  listBusMapFlows({busType:'03401',feeBudgetId:loanApply.id}, function(busMapFlow) {
	 							  var url = "/busRelateSpFlow/loanReport/addLoanReport?sid=" + sid;
	 							  url = url + "&busMapFlowId=" +  busMapFlow.id
	 							  url = url + "&feeBudgetId=" + loanApply.id
	 							  url = url + "&busType=" + busType
	 							  openWinByRight(url);
	 						  })
	 					  }else if(stepth===1){
	 						 callback(loanApply.id,loanReportId);//新增报销申请
	 					  }
	 				  }
	 				 window.top.layer.close(index)
	 			  }
			   });
	 	  }
	 	});
}

//发起审批关联任务业务
function addBusTaskForMod(sid,paramObj){
		
		var busId = paramObj.busId;
		var busType = paramObj.busType;
		var busName = paramObj.busName;
		var taskRemark = paramObj.taskRemark;
		var dealTimeLimit = paramObj.dealTimeLimit;
		var taskName = paramObj.taskName;
		var version = paramObj.version;
		var url = "/task/addTaskOfMod?sid="+sid;
		if(busId){
			url = url+"&busId="+busId;
		}
		if(busType){
			url = url+"&busType="+busType;
		}
		if(busName){
			url = url+"&busName="+encodeURIComponent(busName);
		}
		if(taskRemark){
			url = url+"&taskRemark="+encodeURIComponent(taskRemark);
		}
		if(dealTimeLimit){
			url = url+"&dealTimeLimit="+dealTimeLimit;
		}
		if(taskName){
			url = url+"&taskName="+encodeURIComponent(taskName);
		}
		if(version){
			url = url+"&version="+version;
		}
		if(paramObj.fileLists && paramObj.fileLists [0]){
			var fileIds = [];
			for (var i = 0; i < paramObj.fileLists.length; i++) {
				fileIds.push(paramObj.fileLists[i].upfileId)
			}
			var checkids =fileIds.join(",");
			url = url+"&checkids="+checkids;
		}
		
		if(url){
			window.top.layer.open({
				type: 2,
				title: false,
				closeBtn: 0,
				shadeClose: false,
				shade: 0.3,
				shift:0,
				//zIndex:1010,
				scrollbar:false,
				fix: true, //固定
				maxmin: false,
				move: false,
				area: ['800px', '550px'],
				content: [url,'no'], //iframe的url
				success:function(layero,index){
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.setWindow(window.document,window);
				},end:function(){
				}
			});
		}
}

/**
 * 选择序列编号
 * @param sid
 * @param callback
 */
function serialNumSelect(param,callback){
	var sid = param.sid;
	var url = '/serialNum/listPagedSerialNumSelect?pager.pageSize=8&sid='+sid;
	var exceptId = param.exceptId;
	exceptId = exceptId?exceptId:"0";
	url = url+"&exceptId="+exceptId;
	
	window.top.layer.open({
		 type: 2,
		 title:false,
		  closeBtn:0,
		  area: ['650px', '500px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: [url,'no'],
		  btn: ['选择', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var serialNum = iframeWin.serialNumSelected();
			  if(serialNum){
				  callback(serialNum);
				  window.top.layer.close(index)
			  }
		  }
		,btn2: function(){
			 //右上角关闭回调
			  if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
				  callback(null);
			  }
		}
		  ,cancel: function(){ 
		    //右上角关闭回调
		  }
	});
}


String.format = function() {
    if (arguments.length == 0)
        return null;
    var str = arguments[0];
    for ( var i = 1; i < arguments.length; i++) {
        var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
        str = str.replace(re, arguments[i]);
    }
    return str;
};
//取得一个月的最后一天
function getLastDay(year,month){   
 var new_year = year;  //取当前的年份   
 var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）   
 if(month>12){      //如果当前大于12月，则年份转到下一年   
	 new_month -=12;    //月份减   
	 new_year++;      //年份增   
 }   
 var new_date = new Date(new_year,new_month,1);        //取当年当月中的第一天   
 return (new Date(new_date.getTime()-1000*60*60*24)).getDate();//获取当月最后一天日期   
} 
//添加催办
function addBusRemind(busType,busId){
	var url = "/busRemind/addBusRemindPage?sid="+sid;
	url = url+"&busId="+busId;
	url = url+"&busType="+busType;
	url = url+"&rnd="+Math.random();
	url = url+"&redirectPage="+encodeURIComponent(window.location.href);
	window.top.layer.open({
		 type: 2,
		 title:false,
		 closeBtn: 0,
		  area: ['550px', '450px'],
		  fix: true, //不固定
		  maxmin: false,
		  content: [url,"no"],
		  btn: ['保存','关闭'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.formSub();
			  if(!strIsNull(result)){
				  if(busId==0 && busType==0){
					  window.top.layer.close(index);
					  window.location.reload();
				  }else{
					var html = conStrClockHtml(busType,busId,sid,result)
					$("#busClockList").append(html);
					window.top.layer.msg("操作成功！",{icon:1});
					window.top.layer.close(index);
				  }
			  }
		  },cancel: function(){
		  },success:function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				iframeWin.setWindow(window.document,window);
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  });
			 
		    }
		});
}
//批量催办
function addBusRemindBatch(busType,ids){
    var url = "/busRemind/addBusRemindsPage?sid="+sid;
    url = url+"&ids="+ids;
    url = url+"&busType="+busType;
    url = url+"&rnd="+Math.random();
    url = url+"&redirectPage="+encodeURIComponent(window.location.href);
    window.top.layer.open({
        type: 2,
        title:false,
        closeBtn: 0,
        area: ['550px', '450px'],
        fix: true, //不固定
        maxmin: false,
        content: [url,"no"],
        btn: ['保存','关闭'],
        yes: function(index, layero){
            var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
            var result = iframeWin.formSub();
            if(!strIsNull(result)){
                if(busId==0 && busType==0){
                    window.top.layer.close(index);
                    window.location.reload();
                }else{
                    var html = conStrClockHtml(busType,busId,sid,result)
                    $("#busClockList").append(html);
                    window.top.layer.msg("操作成功！",{icon:1});
                    window.top.layer.close(index);
                }
            }
        },cancel: function(){
        },success:function(layero,index){
  		  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.setWindow(window.document,window);
		  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			  window.top.layer.close(index);
		  });
		 
	    }
    });
}
var layerTipIndex = 0;
//验证提示
function validMsgV2(msg, o, cssctl){
	if (!o.obj.is("form")) {
		var type = o.type;
		if(type==3 && layerTipIndex==0){
			var relateMsgDiv = $(o.obj).attr('relateMsgDiv');
			if(relateMsgDiv){
				var contentBody = $("body").find("#contentBody");
				if(contentBody && contentBody.get(0)){
					scrollToLocation($("#contentBody"),$("#"+relateMsgDiv));
				}else{
					scrollToLocation($(".subform"),$("#"+relateMsgDiv));
				}
				layerTipIndex = layer.tips(msg,$("#"+relateMsgDiv),{"tips":1,end:function(){
					layerTipIndex = 0;
				}})
			}else{
				
				var contentBody = $("body").find("#contentBody");
				if(contentBody && contentBody.get(0)){
					scrollToLocation($("#contentBody"),$(o.obj));
				}else{
					scrollToLocation($(".subform"),$(o.obj));
				}
				layerTipIndex = layer.tips(msg,$(o.obj),{"tips":1,end:function(){
					layerTipIndex = 0;
				}})
			}
		}
	}
}
//选取字段用于任务关联发布
function selectDataForTask(showDatas,callback,spId){
	//没有数据的
	if(!showDatas || !showDatas[0]){
		callback('');
		return;
	}
	
	window.top.layer.open({
		 type: 2,
		 title:false,
		  closeBtn:0,
		  area: ['550px', '450px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ['/common/selectDataForTask?sid='+sid,'no'],
		  btn: ['创建', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var content = iframeWin.formComponSelected();
			  var listFiles = iframeWin.formFileSelected();
			  window.top.layer.close(index);
			  callback(content,listFiles);
		  }
		  ,cancel: function(){ 
		    //右上角关闭回调
		  },success:function(layero,index,data){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.initData(showDatas,spId);
		  }
	});
}
/**
 * 存档位置
 * @param sid
 * @param params
 * @param callback
 */
function chooseDir(sid,params,callback){
	var preDirs = params.preDirs;
	var oldPId = params.oldPId;
	var type = params.type;
	
	var url = '/common/dirOnePage?sid=' + sid+'&t='+Math.random();
	if(type){
		url = url+"&type="+type;
	}
	window.top.layer.open({
		 type: 2,
		  title: ['存档位置', 'font-size:18px;'],
		  area: ['400px', '350px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: url,
		  btn: ['确定', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var dir = iframeWin.returnDir();
			  if(dir){
				  callback(dir)
			  }
			  window.top.layer.close(index);
		  }
		  ,cancel: function(){ 
		    //右上角关闭回调
		  },success: function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.initData(preDirs,oldPId);
		  }
	 });
}
/**
 * 消费类型选择
 * @param params{
 * 				sid:session唯一标识
 * 				preSelectTagId:先前选中的数据
 * 				callback：回调(记账类型)
 *  }
 */
function consumeTypeMoreTree(params){
	var sid = params.sid;
	var callback = params.callback;
	var preSelectTagId = params.preSelectTagId;
	window.top.layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  area: ["500px", "470px"],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/common/consumeTypeMorePage?sid="+sid,'no'],
		  btn: ['确定','清空','取消'],
		  success:function(layero,index){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  if(preSelectTagId){
				  var options = $("#"+preSelectTagId).find("option")
				  iframeWin.setOptions(options);
				  
				  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					  window.top.layer.close(index);
				  });
			  }
		  },
		   yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var consumeTypes = iframeWin.returnConsumeType();
			  if(consumeTypes && consumeTypes[0]){
				  callback(consumeTypes);
				  window.top.layer.close(index);
			  }
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  iframeWin.removeAll();
			  return false;
		  }
		});
}

//跳转新增角色页面
function addRole(){
    layui.use('layer', function(){
        window.top.layer.open({
            type: 2,
            title:false,
            closeBtn: 0,
            area: ['800px', '550px'],
            fix: true, //不固定
            maxmin: false,
            zIndex:1010,
            scrollbar:false,
            content: ["/role/addRolePage?sid="+sid,'no'],
            // btn: ['发布','关闭'],
            yes: function(index, layero){
                var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
                var flag = iframeWin.formSub();
                if(flag){
                    window.top.layer.close(index);
                    window.self.location.reload();
                }
            },cancel: function(){}
        });
    })
}

//跳转修改角色页面
function updateRole(id){
    layui.use('layer', function(){
        window.top.layer.open({
            type: 2,
            title:false,
            closeBtn: 0,
            area: ['800px', '550px'],
            fix: true, //不固定
            maxmin: false,
            zIndex:1010,
            scrollbar:false,
            content: ["/role/updateRolePage?sid=" + sid + '&id=' + id,'no'],
            // btn: ['修改','关闭'],
            yes: function(index, layero){
                var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
                var flag = iframeWin.formSub();
                if(flag){
                    window.top.layer.close(index);
                    window.self.location.reload();
                }
            },cancel: function(){}
        });
    })
}

//编辑外部联系人弹框
function viewOlm(id){
	
	//是否需要数据保持
	
	window.top.layer.open({
		 type: 2,
		 title:false,
		 closeBtn:0,
		 area: ['800px', '600px'],
		 fix: true, //不固定
		 maxmin: false,
		 move: false,
		 scrollbar:false,
		 content: ['/outLinkMan/viewOlmPage?sid=' + sid + '&id=' + id,'no'],
		
		 yes: function(index, layero){
			 
		 }
		,btn2: function(){
			 
		}
	    ,cancel: function(){ 
	    	//右上角关闭回调
	    },success:function(layero,index){
		  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.setWindow(window.document,window);
		  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			  window.top.layer.close(index);
		  });

	    }
	});
}


/**
 * 构建模块名称
 * @param busType
 */
function constrBusName(busType){
	var busName = '';
	switch(busType){
		case '003':
			busName = '任务';
			break;
		case '005':
			busName = '项目';
			break;
        case '080':
            busName = '产品';
            break;
		case '012':
			busName = '客户';
			break;
		case '022':
			busName = '审批';
			break;
		default:
			break;
	}
	return busName;
}

var checkOpt = {
	money:function(money){
		var test = /^-?\d+\.?\d{0,2}$/;
		return test.test(money);
	}
}

function toHtml(str){
	str = str.replace("<", "&lt;");
	str = str.replace(">", "&gt;");
	str = str.replace("\t", "    ");
	str = str.replace("\r\n", "<br>");
	str = str.replace( "\n", "<br>");
	str = str.replace(" ", "&nbsp;");
	return str;
}
