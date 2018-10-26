<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/msgShareJs/msgCenter.js"></script>
<script type="text/javascript">
var sid='${param.sid }';
var pageTag = 'msgShare';
var sessionUserId = ${userInfo.id};

//默认当前第1页
var nowPageNum = 0;
var pageLoc = location.search.match(/pager.offset=(\d+)/);
if(pageLoc){
	//计算当前页数
	nowPageNum = pageLoc[1]/10;
}


//消息中心数据展示
function showModNoRead(data){
	var sessionUser = data.userInfo;
	var allNoReadNums = 0;
	var listNoReadNum = data.listNoReadNum;
	
	for(var i=0;i<listNoReadNum.length;i++){
		var busType = listNoReadNum[i].busType;
		var noReadNum = listNoReadNum[i].noReadNum;
		allNoReadNums +=noReadNum;
		
		if(busType=='022'||busType=='02201'){//审批未读
			busType = '022'
		}else if(busType=='027010'||busType=='027011'){//采购未读
			busType = '027010'
		}else if(busType=='027020'||busType=='027021'){//申领未读
			busType = '027020'
		}
		
		var noReadNumT = $("#noReadNumT_"+busType).data("noReadNum");
		if(!noReadNumT){
			noReadNumT = 0;
		}
		//noReadNum = noReadNumT+noReadNum;
		
		setNoReadNum("noReadNumT_"+busType,noReadNum);
		$("#noReadNumT_"+busType).data("noReadNum",noReadNum);
		
	}
	setNoReadNum("allNoReadNumT",allNoReadNums);
	$("#allNoReadNumT").data("noReadNum",allNoReadNums);
}

$(function(){
	//设置滚动条高度
	var height = $(window).height()-50;
	
	$("#leftMenuUl").slimScroll({
        height:height+"px",
        width:"220px"
    });
	
	var height = $(document).height()-170;
	$("#noMessage").css("height",height)
})
</script>
	<style type="text/css">
		#modTypeId input{
			margin-left: 8px
		}
	</style>
</head>
<body>
	<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
	        	<!-- 大条件 -->
				<jsp:include page="/WEB-INF/jsp/msgShare/listMsgShare_left.jsp"></jsp:include>
				<!-- 列表-->
				<jsp:include page="/WEB-INF/jsp/msgShare/listMsgShare_middle.jsp"></jsp:include>
	        </div>
	   		 <!--主题颜色设置按钮-->
			<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	    </div>
</body>
</html>
