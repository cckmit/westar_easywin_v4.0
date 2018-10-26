<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
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
	
		var pageParam = {
				"sid":"${param.sid}",
				"isBusinessTrip":"${isBusinessTrip}"
		}
		var sid="${param.sid}";//sid全局变量
		$(function(){
			$(".optTr").click(function(){
				$(this).find("[type='radio']").attr("checked","checked");
			});
			//设置滚动条高度
			var height = $(window).height() -30;
			$("#contentBody").css("height",height+"px");
		});
		//选择关联的出差记录
		function selectObj(ts){
			var loanApply = $(ts).data("loanApply");
			if(loanApply){
				return loanApply;
			}else{
				layer.tips("审核中不能借款！",$(ts),{tips:1})
				return null;
			}
		}
	</script>
	<style type="text/css">
		#infoList table{
			table-layout: fixed;
		}
		#infoList td,#infoList th{
			text-overflow: ellipsis;
			white-space: nowrap;
			overflow: hidden; 
		}
		.panel-heading{
			padding: 5px 20px
		}
		.panel-body{
			padding: 0px;
		}
		.table{
		margin-bottom:0px;
		}
		.pagination-lg >li>a{
		font-size: small;
		}
		.pagination{
		margin: 10px 0px
		}
		.spFlowIns{
			cursor: pointer;
		}
	</style>
</head>
<body>
	<!-- Page Content -->
	<div>
		<!-- Page Body -->
		<div class="page-body" style="padding:1px;">
			<div class="row">
				<div class="col-md-12 col-xs-12 " id="infoList">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div class="pull-left padding-right-40" style="font-size: 20px;line-height: 40px">
				             	<span class="widget-caption themeprimary ps-layerTitle">借款选择列表</span>
							</div>
							<div class="widget-buttons ps-widget-buttons">
								<button class="btn btn-info btn-primary btn-xs margin-left-5" type="button" 
								 name="addLoanApply" onclick="javascript:void(0);"><i class="fa fa-plus"></i><span>一般费用说明</span></button>
							</div>
						</div>
						<div class="widget-body" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
								<table class="table table-bordered " id="editabledatatable">
									<thead>
										<tr role="row">
											<th style="width: 5%;padding: 0 0;text-align: center;">序</th>
											<th>出差记录</th>
											<th style="width: 14%">出差地点</th>
											<th style="width: 15%">起止时间</th>
											<th style="width: 12%">借款限额</th>
											<th style="width: 12%">累计借款</th>
											<th style="width: 12%">累计销账</th>
											<!-- <th style="width: 11%;text-align: center;">状态</th> -->
											<th style="width: 7%;padding: 0 0;text-align: center;">操作</th>
										</tr>
									</thead>
									<tbody id="allTodoBody">
										
									</tbody>
								</table>
								<div class="panel-body ps-page bg-white" style="font-size: 12px">
										 <p class="pull-left ps-pageText">共<b class="badge" id="totalNum">11</b>条记录</p>
										 <ul class="pagination pull-right" id="pageDiv">
										 </ul>
									</div>
						</div>
					</div>
				</div>
			</div>
			
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->

	</div>
	<!-- /Page Container -->
</body>
<!--主题颜色设置按钮 end-->
<script src="/static/assets/js/bootstrap.min.js"></script>
<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" charset="utf-8" src="/static/js/financial/loanApplyForStartSelect.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
</html>
