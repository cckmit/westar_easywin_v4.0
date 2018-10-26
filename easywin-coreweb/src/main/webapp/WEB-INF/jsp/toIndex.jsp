<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title>登陆跳转</title>
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/cookieInfo.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">

function chooseInfo(){
	var isSysUser = '${isSysUser}';
	if(isSysUser && isSysUser == '1'){
		addInfoPage();
	}else{
		finishInfoPage();
	}
}
//保存数据 
function addInfoPage(){
	
	var account = '${loginName}';
	sessionStorage.setItem("a_i_p_m_l_c",account); 
	var lastHistory = "ｌａｓｔＨｉｓｔｏｒｙ＿${userInfo.comId}＿${userInfo.id}";
	lastHistory = escape(lastHistory);
	var historyMd = localStorage.getItem(lastHistory);
	var history = null;
	if(historyMd){
		history = unescape(historyMd)
		//是否匹配http://
		if(history.match(/^(\w+:\/\/)?([^\/]+)/i)){
			history = history.replace(/^(\w+:\/\/)?([^\/]+)/i,'');
			//替换sid
			if(history.match(/sid=(\w+)/)){
				history = history.replace(/sid=(\w+)/,'sid=${sid}');
				if(history.match(/pager.offset=(\d+)/)){//查询分页数
					history = history.replace(/pager.offset=(\d+)/,'pager.offset=0');
				}else{
					history = history.replace(/pager.offset=(\d+)/,'');
				}
			}else{
				history = null;
			}
		}else{
			history = null;
		}
	}
	if(!history){
		history = "/index?sid=${sid}";
	}
	
	
	
	var cookieType = '${cookieType}';
	if(!cookieType || cookieType!='no'){//没有从客户端本地取得数据
		var email = localStorage.getItem('l_c_i_p_m'); 
		var comId = localStorage.getItem('c_i_p_m_l');
		var id = localStorage.getItem('i_p_m_l_c');
		var password = getCookie("p_m_l_c_i");
		if('${autoLogin}'=='yes'){//自己设置了保存登录
			if(!id || id!='${id}'|| !comId && comId!='${comId}'){//本地没有保存过信息，或是保存的信息不是自己的
				//设置登录邮箱
				email = '${loginName}';
				localStorage.setItem("l_c_i_p_m",email); 
				//设置登录企业
				comId = '${comId}';
				localStorage.setItem("c_i_p_m_l",comId); 
				//设置人员主键
				id = '${id}';
				localStorage.setItem("i_p_m_l_c",id); 
				//设置自动登录
				localStorage.setItem("autoLogin",'${autoLoginP}'); 
		
				password='${password}';
				setCookie('p_m_l_c_i',password,'d7');
			}
			setCookie('${sid}','${id}');
			setCookie('i_p_m_l_c','${id}');
			//设置自动登录
			localStorage.setItem("autoLogin",'${autoLoginP}');
		}else{//自己设置了保存登录
			if(id && id=='${id}'){//在本地保存过信息，则删除自己的
				localStorage.removeItem('l_c_i_p_m'); 
		    	localStorage.removeItem('c_i_p_m_l');
		    	localStorage.removeItem('i_p_m_l_c');
		    	delCookie("p_m_l_c_i");
		    	
				//取消自动登录
				localStorage.setItem("autoLogin",'${autoLoginP}'); 
			}
		}
	}
	if('${addInfo}'=='y'){
		var url = "/userInfo/addInfoPage?sid=${sid}&comId=${userInfo.comId}&id=${userInfo.id}&page=index";
		layer.open({
			id:'layerConfig',
			//title:'个人配置',
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
			area: ['600px','460px'],
			content: [url,'no'], //iframe的url
			btn:['关闭'],
			yes:function(index, layero){
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.checkLeader() && layer.close(index);
				return false;
			},success: function(layero,index){
				var iframeWin = window[layero.find('iframe')[0]['name']];
				//iframeWin.setWindow(window.document,window,data.list,index);
				 $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					 iframeWin.checkLeader() && layer.close(index);
			     })
			},end:function(index){
				window.location.replace(history);
			}
		});
	}else{
		var toUrl="${toUrl}";
		if(toUrl){
			toUrl = toUrl+"&sid=${sid}";
		}else{
			toUrl = history;
		}
		window.location.replace(toUrl);
	}
}

function finishInfoPage(){
	var url = "/registe/finishInvInfoPage?joinTempId=${joinTemp.id}";
	layer.open({
		id:'layerConfig',
		//title:'个人配置',
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
		area: ['600px','460px'],
		content: [url,'no'], //iframe的url
		success: function(layero,index){
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.setWindow(window.document,window,index);
		}
	});
}

function stop(){ 
	return false; 
} 
document.oncontextmenu=stop;
</script>
</head>
<body onload="chooseInfo()" class="layui-layer-load" style="height: 450px">
</body>
</html>

