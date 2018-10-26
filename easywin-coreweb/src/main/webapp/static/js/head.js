/*只用于头部菜单信息
*/
var soonAddModHtml = '<li>';
soonAddModHtml += '	<a href="javascript:void(0)" style="padding: 8px 2px">';
soonAddModHtml += '		<div class="clearfix">';
soonAddModHtml += '			<i class="fa icon-small"></i>';
soonAddModHtml += '			<span class="title ps-topmargin"></span>';
soonAddModHtml += '		</div>';
soonAddModHtml += '		</a>';
soonAddModHtml += '	</li>';
	
var soonAddModArray = [
	// {"busType":'1',"title":"我要分享","enabled":true,"mod":1,"clz":"add-roport"},
	{"busType":'003',"title":"发布任务","enabled":true,"mod":1,"clz":"add-work"},
	{"busType":'029',"title":"出差申请","enabled":true,"mod":1,"clz":"add-approval"},
	{"busType":'035',"title":"一般借款申请","enabled":true,"mod":1,"clz":"add-approval"},
	{"busType":'030',"title":"借款申请","enabled":false,"mod":1,"clz":"add-approval"},
	{"busType":'031',"title":"报销申请","enabled":false,"mod":1,"clz":"add-approval"},
	{"busType":'022',"title":"创建审批","enabled":true,"mod":1,"clz":"add-approval"},
	{"busType":'005',"title":"添加项目","enabled":true,"mod":1,"clz":"add-project"},
    {"busType":'080',"title":"添加产品","enabled":true,"mod":1,"clz":"add-project"},
	{"busType":'012',"title":"新增客户","enabled":true,"mod":1,"clz":"add-customer"},
	{"busType":'006',"title":"周报汇报","enabled":true,"mod":1,"clz":"add-week"},
    {"busType":'050',"title":"分享汇报","enabled":true,"mod":1,"clz":"add-daily","style":"background: url(\"\");font-size: 18px"},
	{"busType":'017',"title":"新建会议","enabled":true,"mod":1,"clz":"add-meeting"}	,
	{"busType":'016',"title":"添加日程","enabled":false,"mod":1,"clz":"add-schedule"},
	{"busType":'101',"title":"添加闹钟","enabled":false,"mod":1,"clz":"mes-remind"},
	{"busType":'013',"title":"文档分享","enabled":false,"mod":1,"clz":"add-share"},
	{"busType":'004',"title":"发起投票","enabled":false,"mod":2,"clz":"fa icon-small fa-bar-chart-o orange","style":"background: url('');font-size: 18px"},	
	{"busType":'011',"title":"我要提问","enabled":false,"mod":2,"clz":"fa fa-question-circle fa-lg blue icon-small","style":"background: url('');margin-top: 3px;margin-left: 1px;font-size: 21px"}	
];

$(function(){
	//常用顶部菜单点击
	$("body").on("click",".menuFirstLi",function(){
		var menuHead = $(this).data("menuHead");
		//跳转地址
		var url = menuHead.action;
		if (url.indexOf("?") > 0) {
			url += "&sid=" + sid;
		} else {
			url += "?sid=" + sid;
		}
		var busType = menuHead.busType;
		addMenuRate(busType,sid,url);
	})
	
	//平台统计分析
	$("body").on("click","#platFormStatistic",function(){
		window.top.location.href="/statistics/platform/statisticCenter?sid="+sid;
	});
	
	
	//更多菜单点击
	$("body").on("click",".menuMoreA",function(){
		var menuHead = $(this).data("menuHead");
		//跳转地址
		var url = menuHead.action;
		if (url.indexOf("?") > 0) {
			url += "&sid=" + sid;
		} else {
			url += "?sid=" + sid;
		}
		var busType = menuHead.busType;
		if(busType=='028' || busType=='026'){
			//验证档案管理的权限
			getSelfJSON("/modAdmin/authCheckModAdmin",{sid:sid,busType:busType},function(data){
				if(data.modAdminFlag){
					addMenuRate(busType,sid,url);
				}else{
					showNotification(2, "非档案管理人员,没有权限！")
				}
			})
		}else{
			addMenuRate(busType,sid,url);
		}
	})
	getSelfJSON("/menu/listAddSoonSetBySelf",{sid:sid},function(data){
		if(data.status == 'y'){
			//添加快捷方式
			$.each(data.listAddSoon,function(index,obj){
					var soonHtml = $(soonAddModHtml).clone();
					
					if(obj.mod && obj.mod==2){
						$(soonHtml).find("i").attr("class",obj.clz);
						obj.style ? ($(soonHtml).find("i").attr("style",obj.style)):'';
					}else{
						$(soonHtml).find("i").addClass(obj.clz);
					}
					$(soonHtml).find("span").html(obj.menuTitle);
					$("#soonAddModUl").append($(soonHtml));
					$(soonHtml).find("a").on("click",function(){
						addHeadBus(obj.busType,sid);
					})
			});
			var soonHtml = '<li class="no-padding"><a href="javascript:void(0)"  style="color:#428bca !important" class="ps-set" onclick="addSoonSet()"><i class="fa fa-cogs"></i>&nbsp;快捷设置</a></li>'	
			$("#soonAddModUl").append(soonHtml);	
		}else{
			showNotification(2, data.info)
		}
	});
	
	//查询未读消息
	showCountTodo();
	
	//文本框绑定回车提交事件
	$("#searchStr").on("keydown", function(event) {
		if (event.keyCode == "13") {
			//启动加载页面效果
			layer.load(0, {
				shade : [ 0.6, '#fff' ]
			});
			$("#indexSearchForm").submit();
		}
	});
	$("#searchStr").parents().find(".searchicon").on("click",function(){
		layer.load(0, {
			shade : [ 0.6, '#fff' ]
		});
		$("#indexSearchForm").submit();
	});
	//头像点击事件绑定
	$("[name='selfImg']").click(function(){
		window.top.location.href="/userInfo/selfCenter?pager.pageSize=10&sid="+sid+"&activityMenu=self_m_1.1";
	});
	
	$("body").on("click","#headMenuFirst a",function(){
		//启动加载页面效果
		layer.load(0, {
			shade : [ 0.6, "#fff" ]
		});
		window.self.location = "/index?sid="+sid+"&t=" + Math.random();
	})
	//android点击下载页面
	$("#androidApp").click(function(){
		var url = "/organic/erWeiMa?sid="+sid;
		tabIndex = window.top.layer.open({
			  type: 2,
			  title:"android移动端扫描下载",
			  closeBtn:2,
			  shadeClose: true,
			  shade: 0.1,
			  shift:0,
			  fix: true, //固定
			  maxmin: false,
			  move: false,
			  area: ["400px","350px"],
			  content: [url,'no'],//iframe的url
			  success: function(layero,index){
			  	//var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				//iframeWin.setWindow(window.document,window);
			  }
			});
	});
	
	
	//定时器
	var headMenuLiClock;
	var addMoreBusLiClock;
	var busMsgLiClock;
	var menuUserLiClock;
	var menuOrgDivClock;

	//鼠标移动到更多菜单
	$("#headMenuLi").mouseover(function(e) {
		if (!$("#headMenuLi").hasClass("open")) {
			$("#headMenuLi").addClass("open");
		}
		//清除定时器
		clearTimeout(headMenuLiClock);
		clearTimeout(addMoreBusLiClock);
		clearTimeout(busMsgLiClock);
		clearTimeout(menuUserLiClock);
		clearTimeout(menuOrgDivClock);
		//关闭其他的
		$("#addMoreBusLi").removeClass("open");
		$("#busMsgLi").removeClass("open");
		$("#menuUserLi").removeClass("open");
		$("#menuOrgDiv").removeClass("open");
	})
	//鼠标移除更多菜单
	$("#headMenuLi").mouseout(function(e) {
		evt = window.event || e;
		var obj = evt.toElement || evt.relatedTarget;
		var pa = this;
		if (pa.contains(obj)) {
			return false;
		}
		headMenuLiClock = setTimeout(function() {
			$("#headMenuLi").removeClass("open");
		}, 300);
	})
	//鼠标移动到快捷添加
	$("#addMoreBusLi").mouseover(function(e) {
		if (!$("#addMoreBusLi").hasClass("open")) {
			$("#addMoreBusLi").addClass("open");
		}
		//清除定时器
		clearTimeout(headMenuLiClock);
		clearTimeout(addMoreBusLiClock);
		clearTimeout(busMsgLiClock);
		clearTimeout(menuUserLiClock);
		clearTimeout(menuOrgDivClock);
		//关闭其他的
		$("#headMenuLi").removeClass("open");
		$("#busMsgLi").removeClass("open");
		$("#menuUserLi").removeClass("open");
		$("#menuOrgDiv").removeClass("open");

	})
	//鼠标移除快捷添加
	$("#addMoreBusLi").mouseout(function(e) {
		evt = window.event || e;
		var obj = evt.toElement || evt.relatedTarget;
		var pa = this;
		if (pa.contains(obj)) {
			return false;
		}
		addMoreBusLiClock = setTimeout(function() {
			$("#addMoreBusLi").removeClass("open");
		}, 300);
	})
	//鼠标移动到消息
	$("#busMsgLi").mouseover(function(e) {
		if (!$("#busMsgLi").hasClass("open")) {
			$("#busMsgLi").addClass("open");
		}
		//清除定时器
		clearTimeout(headMenuLiClock);
		clearTimeout(addMoreBusLiClock);
		clearTimeout(busMsgLiClock);
		clearTimeout(menuUserLiClock);
		clearTimeout(menuOrgDivClock);
		//关闭其他的
		$("#headMenuLi").removeClass("open");
		$("#addMoreBusLi").removeClass("open");
		$("#menuUserLi").removeClass("open");
		$("#menuOrgDiv").removeClass("open");
	})
	//鼠标移除消息
	$("#busMsgLi").mouseout(function(e) {
		evt = window.event || e;
		var obj = evt.toElement || evt.relatedTarget;
		var pa = this;
		if (pa.contains(obj)) {
			return false;
		}

		busMsgLiClock = setTimeout(function() {
			$("#busMsgLi").removeClass("open")
		}, 300);
	})

	//鼠标移动到人员信息
	$("#menuUserLi").mouseover(function(e) {
		if (!$("#menuUserLi").hasClass("open")) {
			//$("#menuUserLi").addClass("open");
		}
		//清除定时器
		clearTimeout(headMenuLiClock);
		clearTimeout(addMoreBusLiClock);
		clearTimeout(busMsgLiClock);
		clearTimeout(menuUserLiClock);
		clearTimeout(menuOrgDivClock);
		//关闭其他的
		$("#headMenuLi").removeClass("open");
		$("#addMoreBusLi").removeClass("open");
		$("#busMsgLi").removeClass("open");
		$("#menuOrgDiv").removeClass("open");
	})
	//鼠标移除人员信息
	$("#menuUserLi").mouseout(function(e) {
		evt = window.event || e;
		var obj = evt.toElement || evt.relatedTarget;
		var pa = this;
		if (pa.contains(obj)) {
			return false;
		}
		menuUserLiClock = setTimeout(function() {
			$("#menuUserLi").removeClass("open");
		}, 300);
	})

	//鼠标移动到企业信息
	$("#menuOrgDiv").mouseover(function(e) {
		if (!$("#menuOrgDiv").hasClass("open")) {
			//$("#menuOrgDiv").addClass("open");
		}
		//清除定时器
		clearTimeout(headMenuLiClock);
		clearTimeout(addMoreBusLiClock);
		clearTimeout(busMsgLiClock);
		clearTimeout(menuUserLiClock);
		clearTimeout(menuOrgDivClock);
		//关闭其他的
		$("#headMenuLi").removeClass("open");
		$("#addMoreBusLi").removeClass("open");
		$("#busMsgLi").removeClass("open");
		$("#menuUserLi").removeClass("open");
	})
	//鼠标移除企业信息
	$("#menuOrgDiv").mouseout(function(e) {
		evt = window.event || e;
		var obj = evt.toElement || evt.relatedTarget;
		var pa = this;
		if (pa.contains(obj)) {
			return false;
		}
		menuOrgDivClock = setTimeout(function() {
			$("#menuOrgDiv").removeClass("open");
		}, 300);
	})

	$("#menuHeadImg").mouseover(function(e) {
		$(this).find("a").show();
	})
	$("#menuHeadImg").mouseout(function(e) {
		$(this).find("a").hide();
	})
})
//添加头部菜单
function addHeadMenu(menuHeadList) {
	if (null != menuHeadList && menuHeadList.length > 0) {
		//模块类型的数据类型集合
		var typeArray = new Array();
		
		var menuNum = 3;
		var firstMenu = sessionStorage.firstMenuNum
		if(firstMenu){
			var firstMenuObj = eval("(" + unescape(firstMenu)+ ")");
			var menuNumT = firstMenuObj.memuNum
			if(menuNumT){
				menuNum = menuNumT>=5?5:menuNumT<=3?3:menuNumT;
			}
		}
		
		var memuNumObj = {memuNum:menuNum};
		var memuNumStr = JSON.stringify(memuNumObj);
		sessionStorage.firstMenuNum = escape(memuNumStr);
		
		$(".menuFirstLi").remove();
		
		$("#headMenuLi").find("ul").find("dd").html('');
		$.each(menuHeadList,function(index,menuHead){
			//菜单归类(1,2,3,4,5,6,7,8)
			var menuType = menuHead.menuType;
			//模块(003)
			var busType = menuHead.busType;
			//首要的三个
			if(typeArray.length<menuNum){
				if(busType && busType!='028'&& busType!='026' ){
					$li = $('<li class="ps-white menuFirstLi"></li>');
					$a = $('<a href="javascript:void(0)"></a>');
					if (homeFlag == busType) {
						$a.addClass("home-active");
					}
					$ai = $('<i class="' + menuHead.clzHead + '" title="'+ menuHead.menuName.substring(0, 2) + '"></i>');
					$a.append($ai);
					$a.append(menuHead.menuName.substring(0, 2));
					$li.append($a);
					$("#headMenuLi").before($li);
					
					$li.data("menuHead",menuHead);
					
					typeArray.push(busType);
				}
			}
			if ($.inArray(homeFlag, typeArray) < 0 && homeFlag == busType) {
				$li = $('<li class="ps-white"></li>');
				$a = $('<a href="javascript:void(0)"></a>');
				$a.addClass("home-active");
				$ai = $('<i class="' + menuHead.clzHead + '" title="'+ menuHead.menuName.substring(0, 2) + '"></i>');
				$a.append($ai);
				$a.append(menuHead.menuName.substring(0, 2));
				$li.append($a);
				$("#headMenuLi").before($li);
				$li.data("menuHead",menuHead);
			}
			
			
			
			var $a = $('<a href="javascript:void(0)" class="menuMoreA"></a>');
			$a.attr("menuBusType",busType)
			var $ai = $('<i"></i>');
			
			var clzMenu = menuHead.clzMenu;
			if (clzMenu.indexOf("fa-lg") > 0) {
				$ai = $('<i style="background: url(\'\');"></i>');
			}
			$ai.addClass(menuHead.clzMenu)
			if (menuHead.enabled == '1') {
				$ai.addClass(menuHead.enableClz)
			}
			$a.append($ai);
			$a.append(menuHead.menuName);
			$("#menuHead_" + menuType).append($a);
			if(busType && (busType=='028'||busType=='026')){
				//验证档案管理的权限
				getSelfJSON("/modAdmin/authCheckModAdmin",{sid:sid,busType:busType},function(data){
					if(!data.modAdminFlag){
						$a.hide()
					}
				})
			}
			
			
			$a.data("menuHead",menuHead)
		})
	}
}
function initHeadMenu(userId,comId){
	//取得头部菜单信息
	var headMenu = sessionStorage.headMenu;
	if (headMenu) {//已有菜单信息，则直接
		//读取 
		try {
			var menuHeadList = eval("(" + unescape(sessionStorage.headMenu)+ ")");
			if (null != menuHeadList && menuHeadList.length > 0) {
				var menuHead = menuHeadList[0];
				if (menuHead.userId == userId && menuHead.comId == comId) {
					addHeadMenu(menuHeadList);
				} else {
					getHeadMenu();
				}
			}
		} catch (e) {
			getHeadMenu();
		}

	} else {
		getHeadMenu();
	}
}
//从数据库取得数据
function getHeadMenu() {
	$.ajax({
		type : "post",
		url : "/menu/listHeadMenu?sid=" + sid + "&rnd=" + Math.random(),
		dataType : "json",
		async : true,
		success : function(data) {
			if (data.status == 'y') {
				
				var memuNumObj = {memuNum:data.menuNum};
				var memuNumStr = JSON.stringify(memuNumObj);
				sessionStorage.firstMenuNum = escape(memuNumStr);
				
				var menuHeadList = data.menuHeadList;
				addHeadMenu(menuHeadList);
				var str = JSON.stringify(menuHeadList);
				//存入 
				sessionStorage.headMenu = escape(str);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			showNotification(2, "系统错误，请联系管理员！")
		}
	});
}

//展示系统未读
function showCountTodo() {
	$.ajax({
		type : "post",
		url : "/msgShare/countTodo?sid="+sid+"&rnd=" + Math.random(),
		dataType : "json",
		async : false,
		success : function(data) {
			if (data.status == 'y') {
				var showtime = sessionStorage.getItem("showtime");
				if (!showtime) {//系统中没有存放时间
					sessionStorage.setItem("showtime", data.serverTime);
				}
				ifMsg();
				//设置未读数目
				setFlagNum(data)
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			showNotification(2, "系统错误，请联系管理员！")
		}
	});
}
//设置未读消息显示
function setNoReadNum(objId, noReadNum) {
	if (noReadNum <= 0) {
		$("#" + objId).html('');
		$("#" + objId).removeClass("ps-bage");
		$("#" + objId).removeClass("badge");
		$("#" + objId).removeClass("noreadNum");
	} else {
		$("#" + objId).html(noReadNum>99?'99+':noReadNum);
		if (!$("#" + objId).hasClass("ps-bage")) {
			if(noReadNum>99){
				$("#" + objId).addClass("badge ps-bage")
			}else{
				$("#" + objId).addClass("ps-bage")
			}
		}
		if (!$("#" + objId).hasClass("noreadNum")) {
			$("#" + objId).addClass("noreadNum")
		}
	}
}
//设置未读消息
function setFlagNum(data) {
	if (showModNoReadP) {
		showModNoRead(data);
	}
	var sessionUser = data.userInfo;
	//总提醒数
	var allNoReadNums = 0;
	var spNoReadNums = 0;
	var listNoReadNum = data.listNoReadNum;
	for (var i = 0; i < listNoReadNum.length; i++) {
		var busType = listNoReadNum[i].busType;
		var noReadNum = listNoReadNum[i].noReadNum;
		//总提醒数
		allNoReadNums = allNoReadNums + noReadNum;
		if(busType=='022'||busType=='02201'){//审批未读
			busType = '022'
		}else if(busType=='027010'||busType=='027011'){//采购未读
			busType = '027010'
		}else if(busType=='027020'||busType=='027021'){//申领未读
			busType = '027020'
		}
		
		var noReadNumT = $("#noReadNum_"+busType).data("noReadNum");
		if(!noReadNumT){
			noReadNumT = 0;
		}
		//noReadNum = noReadNumT+noReadNum;
		
		setNoReadNum("noReadNum_"+busType,noReadNum);
		$("#noReadNum_"+busType).data("noReadNum",noReadNum);
		
	}
	allNoReadNums=allNoReadNums>99?'99+':allNoReadNums;
	setNoReadNum("modAllNoReadNum", allNoReadNums);
	$("#modAllNoReadNum").data("noReadNum",allNoReadNums);
	$("#allNoReadNum").html(allNoReadNums);
}

//验证数字正则表达式
var checkn = /^\d+$/
//设置模块未读消息
function changeNoReadNum(type) {
	var allNoReadNum = $("#modAllNoReadNum").data("noReadNum");
	if(allNoReadNum && allNoReadNum>=1){
		allNoReadNum = allNoReadNum - 1;
	}else{
		allNoReadNum = 0;
	}
	
	$("#allNoReadNum").html(allNoReadNum)
	setNoReadNum("modAllNoReadNum", allNoReadNum);
	$("#modAllNoReadNum").data("noReadNum",allNoReadNum)
	
	if(type=='022'||type=='02201'){//审批未读
		type = '022';
	}else if(type=='027010'||type=='027011'){//采购未读
		type = '027010';
	}else if(type=='027020'||type=='027021'){//申领未读
		type = '027020';
	}else if(type=='1'){//消息未读
		type = '100';
	}
	try{
		var noReadNum = $("#noReadNum_"+type).data("noReadNum");
		if (noReadNum && noReadNum>=1) {
			noReadNum = noReadNum-1;
		} else {
			noReadNum = 0;
		}
		setNoReadNum("noReadNum_"+type, noReadNum);
		$("#noReadNum_"+type).data("noReadNum",noReadNum);
	}catch(e){
		console.log(type+"----未定义")
	}
}

//团队切换
function orgChange(){
	var a_i_p_m_l_cm = sessionStorage.getItem('a_i_p_m_l_c'); 
	
	var url = "/organic/listChooseOrganic?sid="+sid+"&account="+a_i_p_m_l_cm;
	layer.open({
			//title:['团队选择', 'font-size:20px;color:#000;text-align: center;'],
			title:false,
			closeBtn:0,
			type: 2,
			shadeClose: true,
			shade: 0.1,
			shift:0,
			zIndex:299,
			fix: true, //固定
			maxmin: false,
			move: false,
			border:1,
			area: ['450px','400px'],
			content: [url,'no'], //iframe的url
			success: function(layero,index){
				var iframeWin = window[layero.find('iframe')[0]['name']];
				//关闭窗口
				$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					  window.top.layer.close(index);
				  });
				
				$(iframeWin.document).on("click",".singleOrg",function(){
					window.location.replace("/organic/changeOrg?sid="+sid+"&comId="+$(this).attr("data")+"&account="+a_i_p_m_l_cm);
				});
			},end:function(index){
			}
	 });
}

/*弹出消息提醒*/
function showMsg(num) {
	layui.use('layer', function() {
		var url = '/msgShare/showMsgNoReadPage?sid='+sid;
		//iframe窗
		showMsgIndex = window.top.layer
				.open({
					id : 'msg',
					title : false,
					closeBtn : 0,
					type : 2,
					closeBtn : 0, //不显示关闭按钮
					shade : 0,
					border : 1,
					area : [ '370px', '260px' ],
					offset : 'rb', //右下角弹出
					shift : 2,
					zIndex : 33,
					fix : true, //固定
					maxmin : false,
					move : false,
					shadeClose : true,
					skin : 'bordered-themeprimary', //加上边框
					content : [ url, 'no' ], //iframe的url，no代表不显示滚动条
					success : function(layero, index) { //此处用于演示
						var iframeWin = window.top.window[layero
								.find('iframe')[0]['name']];
						iframeWin.setWindow(window.document, window);
					}
				});
	})
}

/*显示所有消息*/
function otherMsg() {
	window.self.location = '/msgShare/listPagedMsgNoRead?sid='+sid;
}

/*半小时内不提示*/
function doAfterShow() {
	$.post("/msgShare/doAfterShow?sid="+sid, {}, function(rs) {
		if (rs.ifLogin == true) {
			sessionStorage.setItem("showtime", rs.nextShowMsg);
		}
	}, "json");
}

/*关闭页面后失效*/
function noShow() {
	sessionStorage.setItem("ifshow", 'n');
}
/*关闭页面后失效*/
function readAll() {
	$.post("/msgShare/readAll?sid="+sid, {}, function(rs) {
		if (rs.ifLogin == true) {
			$("#busMsgLi").find(".noreadNum").html(0);
			$(document).find(".noread").removeClass("noread");
			$("#allNoReadNum").html(0);
			setNoReadNum("modAllNoReadNum", 0);
			if (pageTag == 'attrCent') {
				$(".submenu").find(".noreadNum").html(0);
			}

		}
	}, "json");
}
//菜单设置
function menuNumSet(sid){
	window.self.location='/userConf/msgShowSettingPage?activityMenu=self_m_3.3&sid='+sid;
}
//快捷发布设置 
function addSoonSet(){
	var url = '/menu/listAddSoonSetOfAll?sid='+sid
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
		  area: ['550px', '550px'],
		  content: [url,'no'], //iframe的url
		  yes:function(index,layero){
			  var iframeWin = window[layero.find('iframe')[0]['name']];
			  iframeWin.resetAddSoon();
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