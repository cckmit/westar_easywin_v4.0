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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<c:if test="${not empty recycleTab}">
			<script type="text/javascript" src="/static/js/recyleJs/recycleBin.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
<script type="text/javascript">

var pageTag = 'vote'

$(function(){
	$("#voteContent").blur(function(){
		if($("#voteContent").val()!=$("#voteContent").attr("prevalue")){
			searchVote();
		}
	});
	//文本框绑定回车提交事件
	$("#voteContent").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#voteContent").val())){
        		searchVote();
        	}else{
    			$("#voteContent").focus();
        	}
        }
    });
	setStyle();
	
	
});
//投票查询
function searchVoteTab(index){
 	//启动加载页面效果
 	layer.load(0, {shade: [0.6,"#fff"]});
	var url="/vote/listPagedVote?sid=${param.sid }&pager.pageSize=8&searchTab="+index;
	if(index==11){//所有提问
	}else if(index==12){//我的提问
		url+="&searchAll=11";
	}else if(index==13){//我已回答
		url+="&searchAll=12";
	}else if(index==14){//提问关注
		url+="&attentionState=1";
	}
	window.location.href=url;

}

function setStyle(){
	//操作删除和复选框
	$('.ws-shareBox').mouseover(function(){
		var optSpan = $(this).find(".optSpan");
		if(optSpan){
			$(optSpan).attr("class","optSpan");
		}
	});
	$('.ws-shareBox').mouseout(function(){
		var optSpan = $(this).find(".optSpan");
		if(optSpan){
			$(optSpan).attr("class","optSpan hidden");
		}
	});
}
function formSub(){
	$(".searchForm").submit();
}
//为隐藏字段赋值
 function gets_value(index,ts,list,div){
     $("input[name='"+div+"']").val(index); 
     searchVote();
 }
 //添加投票
 function addVote(){
	 var url="/vote/addVotePage?sid=${param.sid}&rnd="+ Math.random()
	 openWinByRight(url);
  }
//查询投票
function searchVote(){
    $("#voteSearchForm").submit();
}
//为右边ifeam注入链接
  function viewVote(id){
   $.post("/vote/authorCheck?sid=${param.sid}",{Action:"post",voteId:id},     
			function (msgObjs){
			if(!msgObjs.succ){
				showNotification(2,msgObjs.promptMsg);
			}else{
				var url = "/vote/voteDetail?sid=${param.sid}&id="+id;
				openWinByRight(url);
			}
	},"json");
  }
//为查询是否延期做准备
  function enabledVote(type){
     	if(2==type){
     		type = '';
         }
      	$("#enabled").attr("value",type);
      	searchVote();
  }

//删除投票
function delVote(){
	 if(checkCkBoxStatus('ids')){
		window.top.layer.confirm("删除投票?",{
			  btn: ['确定','取消']//按钮
		  ,title:'询问框'
		  ,icon:3
		}, function(index){//删除投票
			var url = reConstrUrl('ids');
			$("#delForm input[name='redirectPage']").val(url);
			$('#delForm').submit();
		},function(){
		});
	 }
}
</script>
<script type="text/javascript">
$(function(){
	$(":checkbox[name='ids'][disabled!='disabled']").click(function(){
		var checkLen = $(":checkbox[name='ids'][disabled!='disabled']:checked").length;
		var len = $(":checkbox[name='ids'][disabled!='disabled']").length;
		if(checkLen>0){
			//隐藏查询条件
			$(".searchCond").css("display","none");
			//显示批量操作
			$(".batchOpt").css("display","block");
			if(checkLen==len){
				$("#checkAllBox").attr('checked', true);
			}else{
				$("#checkAllBox").attr('checked', false);
			}
		}else{
			//显示查询条件
			$(".searchCond").css("display","block");
			//隐藏批量操作
			$(".batchOpt").css("display","none");
			
			$("#checkAllBox").attr('checked', false);
		}
	});
});

//窗体点击事件检测
document.onclick = function(e){
	var evt = e?e:window.event;
	var ele = $(evt.srcElement || evt.target);
	if ($(ele).parents("#moreCondition_Div").length == 1) { 
		if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
			return false;
		}else{
			if(!$("#moreCondition_Div").hasClass("open")){
				$(".searchCond").find(".open").removeClass("open");
				$("#moreCondition_Div").addClass("open");
			}
		}
	} else{
		$("#moreCondition_Div").removeClass("open");
	}
}
/**
 * 展示查询条件中更多
 */
function displayMoreCond(divId){
	if($("#"+divId).hasClass("open")){
		$("#"+divId).removeClass("open");
	}else{
		$("#"+divId).addClass("open")
	}
}
/**
 * 清空更多查询条件
 */
function resetMoreCon(divId){
	$("#"+divId).find("input").val('');
	
}
</script>
</head>
<body>
	<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
	        	<!-- 大条件 -->
				<jsp:include page="/WEB-INF/jsp/vote/listVote_left.jsp"></jsp:include>
				<c:choose>
					<c:when test="${not empty recycleTab }">
						<jsp:include page="/WEB-INF/jsp/recycleBin/listPagedPreDel.jsp"></jsp:include>
					</c:when>
					<c:otherwise>
						<!-- 列表-->
						<jsp:include page="/WEB-INF/jsp/vote/listVote_middle.jsp"></jsp:include>
					</c:otherwise>
				</c:choose>
				
	        </div>
	   		 <!--主题颜色设置按钮-->
			<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	    </div>
</body>
</html>
