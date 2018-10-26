<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" class="bg-white">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
</script>
<style>

tr{
cursor: auto;
}
</style>
</head>
<body>
<div class="widget no-margin bg-white">

	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
		<span class="widget-caption themeprimary" style="font-size: 20px">催办记录列表</span>
		<div class="widget-buttons ps-toolsBtn">
			<a href="javascript:void(0)" class="blue">&nbsp;</a>
		</div>
		<div class="widget-buttons">
			<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
				<i class="fa fa-times themeprimary"></i> </a>
		</div>
	</div>
      <div class="widget-body margin-top-40" id="contentBody" style="overflow-y:auto;position: relative;width:100%;">
         <table class="table table-hover general-table fixTable">
        	<thead>
           		<tr valign="middle">
	                <th style="width: 50px;" valign="middle">序号</th>
					<th style="width: 150px" class="text-center">催办时间</th>
					<th style="width: 120px" valign="middle" class="text-center">催办人员</th>
					<th valign="middle">催办内容</th>
					<th style="width: 90px" valign="middle">操作</th>
               </tr>
          </thead>
          <tbody id="allTodoBody">
          	<c:choose>
          		<c:when test="${not empty busRemindList}">
          			<c:forEach items="${busRemindList}" var="object" varStatus="vs">
          				<tr>
          					<td>
          						${vs.count}
          					</td>
          					<td>
          						${object.recordCreateTime}
          					</td>
          					<td class="text-center">
          						${object.userName}
          					</td>
          					<td>
          						${ object.content}
          					</td>
          					<td>
          						<a href="javascript:void(0)" busRemind="${object.id}">查看</a>
          					</td>
          				</tr>
          			</c:forEach>
          		</c:when>
          	</c:choose>
      	  </tbody>
  	   </table>
  	   <tags:pageBar url="/busRemind/listPagedBusRemindForUserBus"></tags:pageBar>
    </div>
</div>
<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>
<script type="text/javascript">

var pageNum = 0;     //页面索引初始值   
var pageSize =8;     //每页显示条数初始化，修改显示条数，修改这里即可 
var selectWay;
var sid = "${param.sid}";
//打开页面body
var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//打开页面的标签
var openPageTag;
//打开页面的标签
var openTabIndex;
//注入父页面信息
function setWindow(winDoc,win){
    openWindowDoc = winDoc;
    openWindow = win;
    openPageTag = openWindow.pageTag;
    openTabIndex = openWindow.tabIndex;
}

//窗体点击事件检测
document.onclick = function(e){
	var evt = e?e:window.event;
	var ele = $(evt.srcElement || evt.target);
	if ($(ele).parents("#moreCondition_Div").length == 1) { 
		if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
			return false;
		}else if($(ele).hasClass("noMoreShow")){
			$("#moreCondition_Div").removeClass("open");
			$("#moreCondition_Div").trigger("hideMoreDiv")
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

$(function(){
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	$('body').on('click','a[busRemind]',function(){
		var busRemind = $(this).attr("busRemind");
    	var url = "/busRemind/viewBusRemindPage?sid=${param.sid}&id="+busRemind;
		window.top.layer.open({
			 type: 2,
			 title:false,
			 closeBtn:0,
			 area: ['800px', '550px'],
			 fix: true, //不固定
			 maxmin: false,
			 move: false,
			 scrollbar:false,
			 content: [url,'no']
		});
		
	})
})

    
</script>


</body>
</html>
