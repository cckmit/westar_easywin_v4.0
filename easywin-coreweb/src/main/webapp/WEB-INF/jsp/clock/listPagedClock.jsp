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
	<meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
	<title><%=SystemStrConstant.TITLE_NAME%></title>
	<meta name="description" content="Dashboard" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!-- 框架样式 -->
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
var pageTag = 'clock';
//筛选栏筛选选项初始化
$(document).ready(function(){
	//更多筛选字样显示
    if(${(not empty weekReport.startDate || not empty weekReport.endDate)}){
        $("#moreFilterCondition").html("隐藏");
    }
});
$(function(){
	//更多筛选条件显示层
	$("#moreFilterCondition").click(function(){
        var display = $("#moreFilterCondition_div").css("display");
        if("none"==display){
            $(this).html('隐藏');
            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
        }else if("block"==display){
            $(this).html('更多筛选')
            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
        }

      });
	//提示类型
	$("#allRepType").click(function(){
		$("#clockRepType").val('');
		$("#searchForm").submit();
	});
	//仅一次
	$("#once").click(function(){
		$("#clockRepType").val('0');
		$("#searchForm").submit();
	});
	//每天
	$("#day").click(function(){
		$("#clockRepType").val('1');
		$("#searchForm").submit();
	});
	//每周
	$("#week").click(function(){
		$("#clockRepType").val('2');
		$("#searchForm").submit();
	});
	//每月
	$("#month").click(function(){
		$("#clockRepType").val('3');
		$("#searchForm").submit();
	});
	//每年
	$("#year").click(function(){
		$("#clockRepType").val('4');
		$("#searchForm").submit();
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
	//文本框绑定回车提交事件
	$("#content").blur(function(){
        $("#searchForm").submit();
    });
});
function delClock(){
	if(checkCkBoxStatus('ids')){
		window.top.layer.confirm('确定要删选中数据吗？',function(index){
			var url = reConstrUrl('ids');
			$("#delForm input[name='redirectPage']").val(url);
			$('#delForm').submit();
		},function(){
			
		})
	}else{
		window.top.layer.alert('请先选择需要删除的闹铃。')
	}
}
//模块查询
function searchClock(busType){
 	//启动加载页面效果
 	layer.load(0, {shade: [0.6,"#fff"]});
	if(busType){
		$("#modTypes").val(busType);
		$("#busType").val(busType);
		
	}else{
		$("#modTypes").val('');
		$("#busType").val(busType);
	}
	 $("#searchForm").submit();
}
</script>
<script type="text/javascript">
$(function(){
	//操作删除和复选框
	$('.optTr').mouseover(function(){
		var display = $(this).find(".optTd .optCheckBox").css("display");
		if(display=='none'){
			$(this).find(".optTd .optCheckBox").css("display","block");
			$(this).find(".optTd .optRowNum").css("display","none");
		}
	});
	$('.optTr ').mouseout(function(){
		var optCheckBox = $(this).find(".optTd .optCheckBox");
			var check = $(optCheckBox).find("input").attr('checked');
			if(check){
				$(this).find(".optTd .optCheckBox").css("display","block");
				$(this).find(".optTd .optRowNum").css("display","none");
			}else{
				$(this).find(".optTd .optCheckBox").css("display","none");
				$(this).find(".optTd .optRowNum").css("display","block");
			}
	});
	
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
				<jsp:include page="/WEB-INF/jsp/clock/listClock_left.jsp"></jsp:include>
				<!-- 列表-->
				<jsp:include page="/WEB-INF/jsp/clock/listClock_middle.jsp"></jsp:include>
	        </div>
	   		 <!--主题颜色设置按钮-->
			<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	    </div>
</body>
</html>

