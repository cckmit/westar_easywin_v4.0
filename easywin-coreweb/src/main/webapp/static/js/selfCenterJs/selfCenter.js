//页面加载完成初始化事件定义
$(function(){
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsgV2(msg, o, cssctl);
		},
		showAllError : true
	});
	//文本框绑定回车提交事件
	$("#content").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#content").val())){
        		$("#searchForm").submit();
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#searchForm").focus();
        	}
        }
    });
	$("#content").blur(function(){
		$("#searchForm").submit();
	});
});
//查看详情
function msgShareView(id, type,sid) {
	if ('012' == type) {//客户
		$.post("/crm/authorCheck?sid="+sid, {
			Action : "post",
			customerId : id
		}, function(msgObjs) {
			if (!msgObjs.succ) {
				showNotification(2, msgObjs.promptMsg);
			} else {
				var url = "/crm/viewCustomer?sid="+sid+"&id="
						+ id + "&redirectPage="
						+ encodeURIComponent(window.location.href);
				openWinByRight(url);
			}
		}, "json");
	} else if ('005' == type) {//项目
		$.post(
						"/item/authorCheck?sid="+sid,
						{
							Action : "post",
							itemId : id
						},
						function(msgObjs) {
							if (!msgObjs.succ) {
								showNotification(2, msgObjs.promptMsg);
							} else {
								var url = "/item/viewItemPage?sid="+sid+"&id="
										+ id
										+ "&redirectPage="
										+ encodeURIComponent(window.location.href);
								openWinByRight(url);
							}
						}, "json");
	} else if ('080' == type) {//项目
        $.post(
            "/product/authorCheck?sid="+sid,
            {
                Action : "post",
                itemId : id
            },
            function(msgObjs) {
                if (!msgObjs.succ) {
                    showNotification(2, msgObjs.promptMsg);
                } else {
                    var url = "/product/viewProPage?sid="+sid+"&id="
                        + id
                        + "&redirectPage="
                        + encodeURIComponent(window.location.href);
                    openWinByRight(url);
                }
            }, "json");
    } else if ('003' == type) {//任务
		$.post("/task/authorCheck?sid="+sid, {
			Action : "post",
			taskId : id
		}, function(msgObjs) {
			if (!msgObjs.succ) {
				showNotification(2, msgObjs.promptMsg);
			} else {
				var url = "/task/viewTask?sid="+sid+"&id="
						+ id + "&redirectPage="
						+ encodeURIComponent(window.location.href);
				openWinByRight(url);
			}
		}, "json");
	} else if ('006' == type) {//周报
		$.post("/weekReport/authorCheck?sid="+sid,
						{
							Action : "post",
							weekReportId : id
						},
						function(msgObjs) {
							if (!msgObjs.succ) {
								showNotification(2, msgObjs.promptMsg);
							} else {
								var url = "/weekReport/viewWeekReport?sid="+sid+"&id="
										+ id
										+ "&redirectPage="
										+ encodeURIComponent(window.location.href);
								openWinByRight(url);
							}
						}, "json");
	} else if ('050' == type) {//分享
        $.post("/daily/authorCheck?sid="+sid,
            {
                Action : "post",
                dailyId : id
            },
            function(msgObjs) {
                if (!msgObjs.succ) {
                    showNotification(2, msgObjs.promptMsg);
                } else {
                    var url = "/daily/viewDaily?sid="+sid+"&id="
                        + id
                        + "&redirectPage="
                        + encodeURIComponent(window.location.href);
                    openWinByRight(url);
                }
            }, "json");
    }  else if ('004' == type) {//投票
		$.post("/vote/authorCheck?sid="+sid, {
			Action : "post",
			voteId : id
		}, function(msgObjs) {
			if (!msgObjs.succ) {
				showNotification(2, msgObjs.promptMsg);
			} else {
				var url = "/vote/voteDetail?sid="+sid+"&id="
						+ id + "&redirectPage="
						+ encodeURIComponent(window.location.href);
				openWinByRight(url);
			}
		}, "json");
	} else if ('011' == type) {//问答
		var url = "/qas/viewQuesPage?sid="+sid+"&id=" + id
				+ "&redirectPage=" + encodeURIComponent(window.location.href);
		openWinByRight(url);
	} else if ('1' == type) {//普通分享
		var url = "/msgShare/msgShareViewPage?sid="+sid+"&id="
				+ id
				+ "&type="
				+ type
				+ "&redirectPage="
				+ encodeURIComponent(window.location.href);
		openWinByRight(url);
	}

}
//初始化数据
function initData() {
	$(".msgTalk").bind("focus", function(event) {
		//清空其他的文本域内容
			var textAreas = $(".msgTalk");
			//让所有快速评论按钮显示初始状态
			for ( var i = 0; i < textAreas.length; i++) {
				var val = $(textAreas[i]).val();
				//原有内容文本域
				var otherId = $(textAreas[i]).attr("id");
				//当前指定文本域
				var thisId = $(this).attr("id");
				//数值不为空，并且非自己
				if (val && $(textAreas[i]).attr("id") != $(this).attr("id")) {
					//清空原有文本域内容
					$("#" + otherId).val("")
					//聚焦当前文本域
					$("#" + thisId).focus();

				}
			}

		});
	//更多筛选条件显示层
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
	//鼠标移到modTypeDiv区域时，显示下拉菜单
	$("#modTypeDiv").mouseover(function(e) {
		evt = window.event || e;
		var obj = evt.toElement || evt.relatedTarget;
		var pa = this;
		if (pa.contains(obj)) {
			clearTimeout(timeoutProcess);
			return false;
		}
	});
	//鼠标移出modTypeDiv区域时，隐藏下拉菜单
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
	//点击<li>的时候不考虑checkbox
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
					if ($(checkbox).attr("id")) {//点击的是全选
						if ($(checkbox).attr("checked")) {//复选框是全选已选中，则取消选中
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
	//modTypeId区域移除后若有变动查询
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
				//所选项
				var modTypes = new Array();
				if (array && array.length > 0) {
					for ( var i = 0; i < array.length; i++) {
						modTypes.push($(array[i]).val());
						if (!$(array[i]).attr("prechecked")) {//本次有上次没有选择的
							diff += 1;
						}

					}
				}
				var arrayPre = $("#searchForm").find(
						":checkbox[name='modTypes'][prechecked='1']");
				//所选项
				var modTypePre = new Array();
				if (arrayPre && arrayPre.length > 0) {
					for ( var i = 0; i < arrayPre.length; i++) {
						if (!$(array[i]).attr("checked")) {//本次有上次没有选择的
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
}
/**
 * 显示快速评论
 * @param id
 * @param type
 * @return
 */
function showTalk(id, type) {
	var sty = $("#msgTalk_" + type + "_" + id).css("display")
	$(".fa-comment").attr("class", "fa fa-comment-o");
	if (sty == 'none') {
		$(".allMsgTalk").css("display", "none");
		$("#msgTalkOpt_" + id).attr("class", "fa fa-comment");
		$("#msgTalk_" + type + "_" + id).css("display", "block");
		$("#textarea_" + type + "_" + id).focus();

	} else {
		$(".allMsgTalk").css("display", "none");
	}
}
/**
 * 选择日期
 */
function submitForm() {
	$("#searchForm").submit();
}
//人员筛选
function userOneForUserIdCallBack(userId, userIdtag, userName, userNametag) {
	var creatorType = '${msgShare.creatorType}';
	if (creatorType && creatorType == 0 && userId) {//若是有选定下级，则清除
		$("#creatorType").val('');
	}
	$("#" + userIdtag).val(userId);
	$("#" + userNametag).val(userName);
	$("#searchForm").submit();

}
//负责人类型
function creatorTypeFilter(obj, typeId) {
	var style = $(obj).attr("style");//当前点击的负责人类型
	if (style) {//原来选中，现在清除
		$("#creatorType").val('');
	} else {//现在选中
		if (typeId == 0) {//选中的是自己的动态
			$("#creatorType").val(0);
			$("#creator").val('');
			$("#creatorName").val('');
		} else {
			$("#creatorType").val(1);
		}
	}
	$("#searchForm").submit();
}
//记录访问地址
function setLocation(){
	$("#redirectPage").val(window.self.location);
}
//被选人员提交
function usersSelected(){
	if(!strIsNull($("#listUserInfo_id"))){
		selected("listUserInfo_id");
	}
}
//修改个人密码
function updatePassword(sid,ts){
	//当前活动选项
	var preActive = $(".submenu").find(".active");
	//当前活动选项移除背景色
	$(preActive).removeClass();
	//配置设置背景色
	$(ts).parent().addClass("active bg-themeprimary");
	
	tabIndex = layer.open({
	  type: 2,
	  //title: ['密码修改', 'font-size:18px;'],
	  title:false,
	  closeBtn:0,
	  area: ['550px', '350px'],
	  fix: false, //不固定
	  maxmin: false,
	  scrollbar:false,
	  content: ["/userInfo/updateUserPasswordPage?sid="+sid,'no'],
	  yes: function(index, layero){
		  var iframeWin = window[layero.find('iframe')[0]['name']];
		  iframeWin.formSub();
	  },end:function(index){
		//配置删除背景色
		  $(ts).parent().removeClass();
		  //恢复前一个选项的背景色
		  $(preActive).addClass("active bg-themeprimary");
	  },success:function(layero,index){
		  var iframeWin = window[layero.find('iframe')[0]['name']];
		  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			  layer.close(index);
		  })
	  }
	  
	});
}
//平台开关设置
function switchSet(obj,sid){
	getSelfJSON("/userConf/switchSet?sid="+sid,{"sysConfCode":$(obj).val(),"type":"sysConfig"},function(data){
		if(data.status=='y'){
			layer.msg("操作成功",{icon: 6}); 
		}
	})
}
//平台开关设置
function updateUserConf(menuNum,sid,callback){
	getSelfJSON("/userConf/updateUserConf?sid="+sid,{"openState":menuNum,"type":"menuNum"},function(data){
		callback(data)
	})
}
//个人信息保存
function infoSave(){
	$(".subform").submit();
}

//帐号更新权限验证
function updateAuthorityValidate(noticeType){
	if(strIsNull(noticeType)){
		window.top.layer.msg("验证方式noticeType："+noticeType+"不能为空！", {icon:2});
	}else{
		layer.open({
		  type: 2,
		  title: ['操作权限验证', 'font-size:18px;'],
		  area: ['550px', '350px'],
		  fix: false, //不固定
		  maxmin: false,
		  scrollbar:false,
		  content: "/userInfo/updateAuthorityValidatePage?sid="+sid+"&noticeType="+noticeType,
		  yes: function(index, layero){
			  var iframeWin = window[layero.find('iframe')[0]['name']];
			  iframeWin.formSub();
		  }
		});
	}
}

//帐号更新
function updateUserAccount(noticeType){
	if(strIsNull(noticeType)){
		window.top.layer.msg("更新方式noticeType："+noticeType+"不能为空！", {icon:2});
	}else{
		layer.open({
		  type: 2,
		  //title: [title, 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['550px', '350px'],
		  fix: false, //不固定
		  maxmin: false,
		  scrollbar:false,
		  content: ["/userInfo/updateUserAccountPage?sid="+sid+"&noticeType="+noticeType,'no'],
		  yes: function(index, layero){
			  var iframeWin = window[layero.find('iframe')[0]['name']];
			  iframeWin.formSub();
		  },success:function(layero,index){
			  var iframeWin = window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  layer.close(index);
			  })
		  }
		});
	}
}

//个人信息更新
function updateUserAttr(attrType,oldAttr,newAttrObj,callback){
	if(!strIsNull(attrType)){
		if(attrType == 'nickname'){
			var regixEmail = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
			var regixMovePhone = /^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|18[0-9]{9}$/
			var nickName = $(newAttrObj).val();
			if(nickName.indexOf('@')>=0){
				if(nickName.match(regixEmail)){
					layer.tips("不能将邮箱作为别名",$(newAttrObj),{tips:1});
					return false;
				}
			}else if(nickName.match(regixMovePhone)){
				layer.tips("不能将手机号作为别名",$(newAttrObj),{tips:1});
				return false;
			}
		}
		//alert(oldAttr+" & "+$(newAttrObj).val());
		if(true){//当前修改属性值与现有属性值不一致时，请求修改oldAttr!=$(newAttrObj).val()
			$("#userInfoForm").ajaxSubmit({
				type:"post",
				url:"/userInfo/updateUserAttr?t="+Math.random(),
				dataType: "json",
				data:{attrType:attrType},
				success:function(data){
					if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
						 callback(data)
					}else{
						if(data.isSucc=="yes"){
							window.top.layer.msg(data.msg, {icon:1});
						}else if(data.isSucc=="no"){
							window.top.layer.msg(data.msg, {icon:2});
							if(attrType == 'nickname'){
								$(newAttrObj).val(data.preValue)
							}
						}else if(data.isSucc=="noChanged"){
							//属性没变更
						}
					}
				},
				error:function(XmlHttpRequest,textStatus,errorThrown){}
			})
		}
	}else{
		window.top.layer.msg("不知道你要更新的是谁！", {icon:2});
	}
}