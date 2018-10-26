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
		
		<c:if test="${param.activityMenu=='sp_m_2.1'}">
			<script type="text/javascript" charset="utf-8" src="/static/js/webJs/formCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
		<c:if test="${param.activityMenu=='sp_m_2.2'}">
			<script type="text/javascript" charset="utf-8" src="/static/js/webJs/organicCount.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		</c:if>
		
		<script type="text/javascript">
		function addQus(pId){
			layui.use('layer', function(){
				var layer = layui.layer;
				var url="/web/help/addQusBySimple?pId="+pId;
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
					maxmin: false,
					move: false,
					area: ['800px', '550px'],
					content: [url,'no'], //iframe的url
					success:function(layero,index){
						var iframeWin = window[layero.find('iframe')[0]['name']];
						iframeWin.setWindow(window.document,window);
					},end:function(){
					}
				});
			})
		}
		function viewHelp(id,sunNum){
			if(sunNum>0){
				$("#pId").val(id);
				$("#searchHelpForm").submit();
			}else{
				var url="/web/help/queryQus?qusId="+id
					+"&redirectPage="+encodeURIComponent(window.location.href);
				openWinByRight(url)
			}
		}
		//配置信息维护
		function editHelp(id){
			var url="/web/help/queryQus?qusId="+id
				+"&redirectPage="+encodeURIComponent(window.location.href);
			openWinByRight(url)
		}
		function checkAll(ckBoxElement, ckBoxName){
			var checkStatus = $(ckBoxElement).attr('checked');
			var obj = $(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']");
			if (checkStatus) {
				$(obj).attr('checked', true);
				//隐藏查询条件
				$(".searchCond").css("display","none");
				//显示批量操作
				$(".batchOpt").css("display","block");
			} else {
				$(obj).attr('checked', false);
				//显示查询条件
				$(".searchCond").css("display","block");
				//隐藏批量操作
				$(".batchOpt").css("display","none");
			}
		}
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
			$("#batchDel").click(function(){
				if(checkCkBoxStatus('ids')){
					window.top.layer.confirm('确定要删除选中帮助说明吗？',{title:false,closeBtn:0,icon:3}, function(){
						var url = reConstrUrl('ids');
						$("#delForm input[name='redirectPage']").val(url);
						$('#delForm').submit();
					}, function(){
					});	
				}else{
					window.top.layer.msg('请先选择需要删除的帮助说明。',{title:false,closeBtn:0,icon:7});
				}
			});
			//帮助筛选
			$("#nameCheck").blur(function(){
				//启动加载页面效果
				layer.load(0, {shade: [0.6,'#fff']});
				$("#searchHelpForm").submit();
			});
			//文本框绑定回车提交事件
			$("#nameCheck").bind("keydown",function(event){
		        if(event.keyCode == "13")    
		        {
		        	if(!strIsNull($("#nameCheck").val())){
		        		//启动加载页面效果
		        		layer.load(0, {shade: [0.6,'#fff']});
						$("#searchHelpForm").submit();
		        	}else{
		        		showNotification(1,"请输入检索内容！");
		    			$("#nameCheck").focus();
		        	}
		        }
		    });
			
			
			
			
		});
		</script>
	</head>
	
	<body>
		<!-- 系统头部装载 -->
		<jsp:include page="head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
	        	<!-- 大条件 -->
				<jsp:include page="helpCenter_left.jsp"></jsp:include>
				<c:choose>
					<c:when test="${param.activityMenu=='sp_m_2.1'}">
						<!-- 云表单 -->
						<jsp:include page="../form/cludeFormModList.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='sp_m_2.2'}">
						<!-- 团队统计 -->
						<jsp:include page="../count/listPagedOrgCount.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='sp_m_2.3'}">
						<!-- 支付信息 -->
						<jsp:include page="../orders/listPagedWebOrder.jsp"></jsp:include>
					</c:when>
					<c:when test="${param.activityMenu=='sul_m_1.1'}">
						<!-- 系统升级日志 -->
						<jsp:include page="../upLog/listSysUpLog.jsp"></jsp:include>
					</c:when>
					<c:otherwise>
						<jsp:include page="helpCenter_middle.jsp"></jsp:include>
					</c:otherwise>
				</c:choose>
	        </div>
	    </div>
	    <!--主题颜色设置按钮 end-->
	<script src="/static/assets/js/bootstrap.min.js"></script>
	<script src="/static/assets/js/demo.js" type="text/javascript"
		charset="utf-8"></script>
	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
	</body>
</html>

