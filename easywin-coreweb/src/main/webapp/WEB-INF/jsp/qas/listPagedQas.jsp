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
 <c:if test="${not empty recycleTab}">
			<script type="text/javascript" src="/static/js/recyleJs/recycleBin.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
<script type="text/javascript">
var pageTag = 'ques';
$(function(){
	//任务名筛选
	$("#title").blur(function(){
		if($("#title").val()!=$("#title").attr("prevalue")){
			searchQues();
		}
	});
	//文本框绑定回车提交事件
	$("#title").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	if(!strIsNull($("#title").val())){
        		searchQues();
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#title").focus();
        	}
        }
    });
});
//查询提问的
function searchQas(index){
 	//启动加载页面效果
 	layer.load(0, {shade: [0.6,"#fff"]});
	var url="/qas/listPagedQas?sid=${param.sid}&pager.pageSize=8&searchTab="+index
	if(index==11){//所有提问
	}else if(index==12){//我的提问
		url +="&searchAll=11";
	}else if(index==13){//我已回答
		url +="&searchAll=12";
	}else if(index==14){//我未回答
		url +="&searchAll=13";
	}else if(index==15){//提问关注
		url +="&attentionState=1";
	}
    window.location.href=url;
}

//为隐藏字段赋值
  function gets_value(index,ts,list,div){
      $("input[name='"+div+"']").val(index); 
      searchQues();
  }
//提问
 function addQues(){
	var url='/qas/addQuesPage?sid=${param.sid}';
  	openWinByRight(url);
 }
 //查询问题
 function searchQues(){
     $("#qasSearchForm").submit();
 }
//为查询是否延期做准备
  function enabledQues(type){
     	if(2==type){
     		type = '';
         }
      	$("#state").attr("value",type);
      	searchQues();
  }

//查看问题
 function viewQues(id){
	var url="/qas/viewQuesPage?sid=${param.sid}&id="+id;
  	openWinByRight(url);
 }

/**
 * 删除问题
 */
 function delQues(id,sid){
	
	 if(checkCkBoxStatus('ids')){
			window.top.layer.confirm("确定需要删除提问?",function(index){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').submit();
			})
		}else{
			window.top.layer.alert('请先选择需要删除的提问。')
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
				<jsp:include page="/WEB-INF/jsp/qas/listQas_left.jsp"></jsp:include>
				
				<c:choose>
					<c:when test="${not empty recycleTab }">
						<jsp:include page="/WEB-INF/jsp/recycleBin/listPagedPreDel.jsp"></jsp:include>
					</c:when>
					<c:otherwise>
						<!-- 列表-->
						<jsp:include page="/WEB-INF/jsp/qas/listQas_middle.jsp"></jsp:include>
					</c:otherwise>
				</c:choose>
				
				
	        </div>
	   		 <!--主题颜色设置按钮-->
			<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	    </div>
 
</body>
</html>
