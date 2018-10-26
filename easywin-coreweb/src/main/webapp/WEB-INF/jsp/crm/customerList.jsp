<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="utf-8"/>
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<meta name="description" content="Dashboard" />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<!-- 框架样式 -->
		<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/echarts.js"></script>
		<c:if test="${param.statisticsType==3 }">
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/china.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/xinjiang.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/taiwan.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/aomen.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/chongqing.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/xianggang.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/ningxia.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/beijing.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/tianjin.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/shanghai.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/gansu.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/qinghai.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/shanxi1.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/xizang.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/guizhou.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/yunnan.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/hainan.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/sichuan.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/guangdong.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/guangxi.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/hunan.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/henan.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/hubei.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/jiangxi.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/shandong.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/heilongjiang.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/jiangsu.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/anhui.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/fujian.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/zhejiang.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/jilin.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/liaoning.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/neimenggu.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/hebei.js"></script>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/map/shanxi.js"></script>
		</c:if>
		<c:if test="${not empty recycleTab}">
			<script type="text/javascript" src="/static/js/recyleJs/recycleBin.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
			<script type="text/javascript" src="/static/js/crmJs/crmCenter.js"></script>
		
		<script type="text/javascript">
			//session标识
			var sid = '${param.sid}';
			//默认显示界面标识
			var pageTag = 'myCrm';
			var searchTab = '${param.searchTab}';
			if(searchTab==12){
				pageTag="attCrm";
			}else if(searchTab==13){
				pageTag="subCrm";
			}else if(searchTab==15){
				pageTag="allCrm";
			}
			//默认当前第1页
			var nowPageNum = 0;
			//默认总待办数为0
			var countTodo=0;
			
			var pageLoc = location.search.match(/pager.offset=(\d+)/);
			if(pageLoc){
				//计算当前页数
				nowPageNum = pageLoc[1]/10;
			}
			var tabIndex;
			var sessionUserId = ${userInfo.id};
			//返回客户负责人集合
			function returnListOwner(options){
				var listOwner =[];
				$("#listOwner_show").find('span').remove();
				$("#owner_select").find("option").remove();
				
				if(options.length>0){
					$("#listOwner_show").show();
					for(var i =0;i<options.length;i++){
						$("#owner_select").append("<option selected='selected' value='"+options[i].value+"' >"+options[i].text+"</option>");
					
						var userInfo = new Object();  
			 			userInfo.id = options[i].value;
			 			userInfo.userName =  options[i].text;
			 			listOwner.push(userInfo);
			 			
				 		var html = "<span  style=\"cursor:pointer;\"  title=\"双击移除\" ondblclick=\"removeChoose('owner','"+sid+"','"+options[i].value+"',this)\" ";
				 		html += " class=\"label label-default margin-right-5 margin-bottom-5\">"+options[i].text+"</span>";
			 			$("#listOwner_show").append(html);
					}
				}else{
					$("#listOwner_show").hide();
				}
				return listOwner;
			}
			//返回客户类型集合
			function returnListCrmType(options){
				var listCrmType =[];
				$("#listCrmType_show").find('span').remove();
				$("#crmType_select").find("option").remove();
				
				if(options.length>0){
					$("#listCrmType_show").show();
					for(var i =0;i<options.length;i++){
						$("#crmType_select").append("<option selected='selected' value='"+options[i].value+"' >"+options[i].text+"</option>");
					
						var customerType = new Object();  
			 			customerType.id = options[i].value;
			 			customerType.typeName =  options[i].text;
			 			listCrmType.push(customerType);
			 			
				 		var html = "<span  style=\"cursor:pointer;\"  title=\"双击移除\" ondblclick=\"removeChoose('crmType','"+sid+"','"+options[i].value+"',this)\" ";
				 		html += " class=\"label label-default margin-right-5 margin-bottom-5\">"+options[i].text+"</span>";
			 			$("#listCrmType_show").append(html);
					}
				}else{
					$("#listCrmType_show").hide();
				}
				return listCrmType;
			}
			//导出Excel
			 function excelExport(fileName){
				var html = $("#tableView").html();
				tableToXls(html,fileName,sid);
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
				<jsp:include page="/WEB-INF/jsp/crm/listCrm_left.jsp"></jsp:include>
				<c:choose>
					<c:when test="${not empty recycleTab }">
						<jsp:include page="/WEB-INF/jsp/recycleBin/listPagedPreDel.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.statisticsType==1}">
						<!--1  分类统计-->
						<jsp:include page="/WEB-INF/jsp/statistics/crm/crmStatic_crmType.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.statisticsType==2}">
						<!--2 更新统计-->
						<jsp:include page="/WEB-INF/jsp/statistics/crm/crmStatic_crmFrequency.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.statisticsType==3}">
						<!--3分布统计 -->
						<jsp:include page="/WEB-INF/jsp/statistics/crm/crmStatic_crmArea.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.statisticsType==4}">
						<!--4归属统计-->
						<jsp:include page="/WEB-INF/jsp/statistics/crm/crmStatic_crmOwner.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.statisticsType==5}">
						<!--5增量统计-->
						<jsp:include page="/WEB-INF/jsp/statistics/crm/crmStatic_crmAddNum.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.statisticsType==6}">
						<!--6阶段统计-->
						<jsp:include page="/WEB-INF/jsp/statistics/crm/crmStatic_crmStage.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.statisticsType==7}">
						<!--7 资金预算统计-->
						<jsp:include page="/WEB-INF/jsp/statistics/crm/crmStatic_crmBudget.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.searchTab==16}">
						<!--7 资金预算统计-->
						<jsp:include page="/WEB-INF/jsp/crm/crmBudgetList.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.searchTab==39}">
						<!--客户操作配置-->
						<jsp:include page="/WEB-INF/jsp/modChangeExam/modConfig.jsp"></jsp:include>
					</c:when>
					<c:otherwise>
						<!-- 列表-->
						<jsp:include page="/WEB-INF/jsp/crm/listCrm_middle.jsp"></jsp:include>
					</c:otherwise>
				</c:choose>
	        </div>
	   		 <!--主题颜色设置按钮-->
			<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	    </div>
	</body>
</html>

