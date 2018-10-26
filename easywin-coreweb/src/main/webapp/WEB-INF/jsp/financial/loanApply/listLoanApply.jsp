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
<body>
<link href="/static/css/personalLoanApply.css" rel="stylesheet" type="text/css">
<!-- Page Content -->
<div class="page-content">
	<!-- Page Body -->
	<div class="page-body">
		<div class="row">
			<div class="loanApply-card-div card-div-first">
				<div class="card-grayline">
					<div class="card-title-div">
						<i class="fa fa-css fa-check-square-o"></i>
						<span class="card-title-span">报销记录</span>
					</div>
					<div class="card-info-strong">
						<span class="card-strong-info">报销中：</span>
						<i class="fa fa-css fa-rmb" style="margin-top:8px;">&nbsp;1,0000</i>
					</div>
					<div class="card-info-gray">
						<div class="card-gray-span-div">
							<span class="card-strong-info" style="font-size:16px;">报销中：</span>
							<i class="fa fa-css fa-rmb" style="margin-top:4px;font-size:16px;">&nbsp;25次</i>
						</div>
						<div class="card-gray-span-div">
							<span class="card-strong-info" style="font-size:16px;">已报销：</span>
							<i class="fa fa-css fa-rmb" style="margin-top:4px;font-size:16px;">&nbsp;20次</i>
						</div>
					</div>
				</div>
				<div class="card-bottom-div">
					<div class="card-bottom-info">
						<span class="card-bottom-span" style="font-size:16px;">累计报销：</span>
						<i class="fa fa-css fa-rmb" style="margin-top:4px;font-size:16px;">&nbsp;2,0000</i>
					</div>
				</div>
			</div>
			<div class="loanApply-card-div card-div-other">
				<div class="card-grayline">
					<div class="card-title-div">
						<i class="fa fa-css fa-shopping-cart"></i>
						<span class="card-title-span">消费记录</span>
					</div>
					<div class="card-info-strong">
						<span class="card-strong-info">待报销：</span>
						<i class="fa fa-css fa-rmb" style="margin-top:8px;">&nbsp;1,0000</i>
					</div>
					<div class="card-info-gray">
						<div class="card-gray-span-div">
							<span class="card-strong-info" style="font-size:16px;">报销中：</span>
							<i class="fa fa-css fa-rmb" style="margin-top:4px;font-size:16px;">&nbsp;1,0000</i>
						</div>
						<div class="card-gray-span-div">
							<span class="card-strong-info" style="font-size:16px;">已报销：</span>
							<i class="fa fa-css fa-rmb" style="margin-top:4px;font-size:16px;">&nbsp;1,0000</i>
						</div>
					</div>
				</div>
				<div class="card-bottom-div">
					<div class="card-bottom-info">
						<span class="card-bottom-span" style="font-size:16px;">累计消费：</span>
						<i class="fa fa-css fa-rmb" style="margin-top:4px;font-size:16px;">&nbsp;2,0000</i>
					</div>
				</div>
			</div>
			<div class="loanApply-card-div card-div-other">
				<div class="card-grayline">
					<div class="card-title-div">
						<i class="fa fa-css fa-money"></i>
						<span class="card-title-span">借款记录</span>
					</div>
					<div class="card-info-strong">
						<span class="card-strong-info">申请中：</span>
						<i class="fa fa-css fa-rmb" style="margin-top:8px;">&nbsp;1,0000</i>
					</div>
					<div class="card-info-gray">
						<div class="card-gray-span-div">
							<span class="card-strong-info" style="font-size:16px;">申请中：&nbsp;25次</span>
						</div>
						<div class="card-gray-span-div">
							<span class="card-strong-info" style="font-size:16px;">已借款：&nbsp;20次</span>
						</div>
					</div>
				</div>
				<div class="card-bottom-div">
					<div class="card-bottom-info">
						<span class="card-bottom-span" style="font-size:16px;">累计借款：</span>
						<i class="fa fa-css fa-rmb" style="margin-top:4px;font-size:16px;">&nbsp;2,0000</i>
					</div>
				</div>
			</div>
			<div class="loanApply-card-div card-div-other">
				<div class="card-grayline">
					<div class="card-title-div">
						<i class="fa fa-css fa-plane"></i>
						<span class="card-title-span">出差记录</span>
					</div>
					<div class="card-info-strong">
						<span class="card-strong-info">累计出差：&nbsp;10次</span>
					</div>
					<div class="card-info-gray">
						<div class="card-gray-span-div">
							<span class="card-strong-info" style="font-size:16px;">申请中：&nbsp;25次</span>
						</div>
						<div class="card-gray-span-div">
							<span class="card-strong-info" style="font-size:16px;">已出差：&nbsp;20次</span>
						</div>
					</div>
				</div>
				<div class="card-bottom-div">
					<div class="card-bottom-info">
						<span class="card-bottom-span" style="font-size:16px;">累计项目借款：</span>
						<i class="fa fa-css fa-rmb" style="margin-top:4px;font-size:16px;">&nbsp;2,0000</i>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="page-content">
	<!-- Page Body -->
	<div class="page-body">
		<div class="row">
			<div class="custom-progress" id="customProgress">
				<div class="custom-progress-percent" id="progressPercent">
					0%
				</div>
			</div>
		</div>
	</div>
</div>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12" id="infoList">
					<div class="widget">
						<ul class="nav nav-tabs tabs-flat" id="myTab11" style="padding-top:0;background-color: #FBFBFB;">
							<li class="active" id="loanOffMenuLi">
								<a data-toggle="tab" href="javascript:void(0)">报销申请</a>
							</li>
							<li id="loanMenuLi">
								<a data-toggle="tab" href="javascript:void(0)">借款申请<c:if test="${consume.fileNum > 0}"><span style="color:red;font-weight:bold;">（${consume.fileNum}）</span></c:if></a>
							</li>
							<li id="comsumeMenuLi">
								<a data-toggle="tab" href="javascript:void(0)">待报销消费记录<c:if test="${consume.fileNum > 0}"><span style="color:red;font-weight:bold;">（${consume.fileNum}）</span></c:if></a>
							</li>
						</ul>

						<div class="widget-body" style="text-align:center;padding-top:5px;overflow: hidden;overflow-y:auto;width:100%;box-shadow: none;" id="contentBody">
							<iframe id="otherAttrIframe" style="display: block;" class="layui-layer-load"
									src="/financial/listLoansPage?sid=${param.sid}&pager.pageSize=10"
									border="0" frameborder="0" allowTransparency="true"
									scrolling="no" width=100% height=100% vspale="0"></iframe>
						</div>
					</div>
			</div>
			
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->

	</div>
	<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
	<style type="text/css">
		#infoList table{
			table-layout: fixed;
		}
		#infoList td,#infoList th{
			text-overflow: ellipsis;
			white-space: nowrap;
			overflow: hidden; 
		}
	</style>
</body>
</html>
