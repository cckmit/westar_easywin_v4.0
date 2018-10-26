/*只用于登录页面
*/

$(function(){
	sessionStorage.removeItem("headMenu");
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError:true
	});
	
	$(".subform").keydown(function(e) {
        if (e.keyCode == 13) {
        	return false;
        }
    });
	//找回密码
	$("#findPassword").click(function(){
		window.self.location="/registe/updatePasswordPage?rnd=" + Math.random();
	});
})

//提交前邮箱验证
function checkInfo(form){
	var email = $("#loginName").val();
	var password = $("#password").val();
	if(strIsNull(password)){
		$("#password").focus();
		$("#password").blur();
		$("#password").focus();
		return;
	}
	var yzm = $("#yzm").val();
	if(strIsNull(yzm)){
		$("#yzm").focus();
		$("#yzm").blur();
		$("#yzm").focus();
		return;
	}
	var md5 = "";
	if(!strIsNull($("#password").val())){
		md5 = $.md5(password);
		$("#passwordMD5").val(md5);
	}
	if(!strIsNull(email) && !strIsNull(password)){
		$.ajax({
			  type : "POST",
			  url : "/login/listOrg?rnd=" + Math.random(),
			  dataType:"json",
			  data:{email:email,passwordMD5:md5,yzm:yzm},
			  success : function(data){
				$("#cookieType").val('');
				  if(data.status=='n'){
					if(data.input=='emailF'){
						 $("#loginName").focus()
						 showNotification(2,data.info);
					}else if(data.input=='passF'){
						$("#password").focus();
						$("#cookieType").val('');
						showNotification(2,data.info);
					}else if(data.input=='yzmF'){
						$("#yzm").focus();
						showNotification(2,data.info);
					}else if(data.input=='licDate'){
						showRegistInfo()
					}
					return;
				  }
				  if(data.list.length<1){
					  showNotification(2,"该账号没有激活或不存在！");
				  }else  if(data.list.length==1){
					  //只有一个则直接登陆
					  $("#comId").val(data.list[0].orgNum);
					  $("#isSysUser").val(data.list[0].isSysUser)
					  $("#loginForm").submit();
				  }else{
					 var comId =  $("#comId").val();
					 if(!strIsNull(comId)){
						 $("#loginForm").submit();
					 }else{
						var url = "/login/listChooseOrg?rnd=" + Math.random();
						layer.open({
								id:'layerOpener',
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
									iframeWin.setWindow(window.document,window,data.list,index,email);
									
									$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					 					  layer.close(index);
					 				  })
								},end:function(index){
								}
						 });
					 }
				  }
			  }
			});
	}else{
		return false;
	}
}
//保存数据 
function getInfoBs(){
	if(window.localStorage){
		var l_c_i_p_mv = localStorage.getItem('l_c_i_p_m'); 
		var c_i_p_m_lv = localStorage.getItem('c_i_p_m_l');
		var i_p_m_l_cv = localStorage.getItem('i_p_m_l_c');
		
		var autologin = localStorage.getItem('autoLogin');
		
		var i_p_m_l_c_session = sessionStorage.getItem("i_p_m_l_c");
		var c_i_p_m_l_cookie = getCookie("c_i_p_m_l");
		//默认不可以自动登录
		var flag;
		if(i_p_m_l_c_session){//不为空，则在当前页面
			if(c_i_p_m_l_cookie){
				//还在原来的页面上或是新打开的页面，还有其他页面存在
				flag = false;
			}else if(!c_i_p_m_l_cookie){//是新打开的页面，没有其他页面
				flag = true;
			}
		}else{
			flag = true;
		}
		if(flag){
			var isreload = sessionStorage.getItem("reload");
			if(isreload && isreload == 'false'){
				flag = false;
				localStorage.removeItem('l_c_i_p_m'); 
				localStorage.removeItem('c_i_p_m_l');
				localStorage.removeItem('i_p_m_l_c');
				
				localStorage.removeItem('autoLogin');
				sessionStorage.removeItem("i_p_m_l_c");
				sessionStorage.removeItem("reload");
			}
		}
		
		if(l_c_i_p_mv && c_i_p_m_lv && i_p_m_l_cv && autologin && flag){
			var p_m_l_c_iv = getCookie("p_m_l_c_i");
			$.ajax({
				type : "POST",
				url : "/login/checkCookies?rnd=" + Math.random(),
				dataType:"json",
				data:{l_c_i_p_m:l_c_i_p_mv,
				c_i_p_m_l:c_i_p_m_lv,
				i_p_m_l_c:i_p_m_l_cv,
				p_m_l_c_i:p_m_l_c_iv,
				autologin:autologin},
				success : function(data){
					if(data.state=='y'){
						var loginName = data.email;
						var password = data.password;
						var cookieType = data.cookieType;
						if(data.list.length==1){
							//只有一个则直接登陆
							var organic = data.list[0];
							var comId= organic.orgNum;
							var isSysUser = organic.isSysUser;
							if(organic.isSysUser==1){
								if(organic.inService==1){//正常服务状态
									sessionStorage.setItem("reload","false");
									window.self.location='/login?isSysUser='+isSysUser+'&loginName='+loginName+'&password='+password+'&comId='+comId+'&cookieType='+cookieType;
								}else{
									showNotification(2,"登录失败，你的账号超出团队人数上限，请联系你们的团队管理员。");
									sessionStorage.setItem("reload","false");
									
								}
							}else{
								sessionStorage.setItem("reload","false");
								window.self.location='/login?isSysUser='+isSysUser+'&loginName='+loginName+'&password='+password+'&comId='+comId+'&cookieType='+cookieType;
							}
						}else if(data.list.length>1){
							$("#body").css("display","block")
							$("#loginName").val(loginName);
							$("#password").val(password);
							$("#passwordMD5").val(password);
							$("#cookieType").val(cookieType);
							
							var url = "/login/listChooseOrg?rnd=" + Math.random();
							layer.open({
									id:'layerOpener',
									//title:'团队选择',
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
									content: url, //iframe的url
									success: function(layero,index){
										var iframeWin = window[layero.find('iframe')[0]['name']];
										iframeWin.setWindow(window.document,window,data.list,index,loginName);
										$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
						 					  layer.close(index);
						 				})
						 				  
									},end:function(index){
										$("#cookieType").val('');
										$("#password").val('');
									}
							 });
						}
						
					}else if(data.state == 'valiDate'){
						 showRegistInfo();
					}else{
						$("#body").css("display","block");
						//$("#loginName").focus();
					}
				}
			},"json");
		}else{
			$("#body").css("display","block");
			macValidatCheck();
			//$("#loginName").focus();
		}
	}else{
		$("#rememberPass").remove();
		$("#body").css("display","block");
		//$("#loginName").focus();
		macValidatCheck();
		
	}
}

function macValidatCheck(){
	//验证服务器MAC
	 $.getJSON("/bandMAC/macValidatCheck").done(function (data) {
		 if(data.status!='y'){
			 showRegistInfo();
		 }
	 });
}
/**
 * 浏览器版本
 * @return
 */
function getBrowserVersion(pageType,ieEngine){
	var browser = {};
	var userAgent = navigator.userAgent.toLowerCase();
	var s;
	    (s = userAgent.match(/msie ([\d.]+)/))
	           ? browser.ie = s[1]
	            : (s = userAgent.match(/firefox\/([\d.]+)/))
	                    ? browser.firefox = s[1]
	                    : (s = userAgent.match(/chrome\/([\d.]+)/))
	                            ? browser.chrome = s[1]
	                           : (s = userAgent.match(/opera.([\d.]+)/))
	                                    ? browser.opera = s[1]
	                                    : (s = userAgent
	                                            .match(/version\/([\d.]+).*safari/))
	                                            ? browser.safari = s[1]
	                                            : 0;
	   var version = "";
	   var support = true;
	   if (browser.ie) {
	       version = 'msie ' + browser.ie;
	       if(browser.ie < 9){
	    	   support = false;
	       }else if(browser.ie>9){
	       }else if(ieEngine==0){
	    	   support = false;
	       }
	   } else if (browser.firefox) {
	        version = 'firefox ' + browser.firefox;
	   } else if (browser.chrome) {
	        version = 'chrome ' + browser.chrome;
	    } else if (browser.opera) {
	        version = 'opera ' + browser.opera;
	    } else if (browser.safari) {
	        version = 'safari ' + browser.safari;
	    } else {
	        version = '未知的浏览器类型';
	    }
	   if(pageType=='login'){
		   if(support){
			   $("#body").find("#browsertip").remove();
		   }else{
			   $("#body").find(".ws-head-right").remove();
			   $("#body").find("#contentForm").remove();
			   $("#body").find("#browsertip").show();
		   }
	   }
	    return support;
	}

//字符串是否为空判断
function strIsNull(str){
	if(str!="" && str!="null" && str!=null && str!="undefined" && str!=undefined){
		return false;
	}else{
		return true;
	}
}
/**
 * 系统通知
 * 
 * @param icon
 * @param content
 * @return
 */
function showNotification(icon, content) {
	if(icon){
		window.top.layer.msg(content,{icon: icon,skin:"showNotification"});
	}else{
		window.top.layer.msg(content);
		
	}
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

function setRegistInfo(data){
	var url = "/bandMAC/macInfoLogin?rnd=" + Math.random();
	layer.open({
			id:'layerOpener',
			//title:['团队选择', 'font-size:20px;color:#000;text-align: center;'],
			title:false,
			closeBtn:0,
			type: 2,
			shade: 0.5,
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
				iframeWin.setWindow(data,index);
				
				$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
 					  layer.close(index);
 				})
 				$(iframeWin.document).find("#close").on("click",function(){
 					layer.close(index);
 				})
			},end:function(index){
			}
	 });
}
function showRegistInfo(){
	var url = "/bandMAC/macInfoTip?rnd=" + Math.random();
	layer.open({
		id:'layerOpener',
		//title:['团队选择', 'font-size:20px;color:#000;text-align: center;'],
		title:false,
		closeBtn:0,
		type: 2,
		shade: 0.5,
		shift:0,
		zIndex:299,
		fix: true, //固定
		maxmin: false,
		move: false,
		border:1,
		area: ['450px','320px'],
		btn: ['关闭'],
		content: [url,'no'], //iframe的url
		yes:function(index){
			layer.close(index)
		},
		success: function(layero,index){
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.setWindow();
			
			$(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				layer.close(index);
			})
		},end:function(index){
		}
	});
}