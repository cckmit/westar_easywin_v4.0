<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
<%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script src="/static/js/jfzbJs/jfzbType.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
	var pageParam = {
			"sid":"${param.sid}"
	}
	
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40-45;
		$("#contentBody").css("height",height+"px");
		//加载数据
		jfzbTypeForm.initData();
		
	})
</script>
<style type="text/css">
#editable-sample tbody>tr>td{
	padding: 1px 10px;
}
body:before{ background:#fff;}
</style>
</head>
<body>

<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">积分类型维护</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" class="windowClosBtn" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
					<table id="editable-sample" class="table table-striped table-hover table-bordered dataTable" style="width: 97%;margin-left: 10px">
				     <thead>
				         <tr role="row">
				         	<th style="width: 55px;" class="sorting_disabled" >
				         		序号
				         	</th>
				         	<th class="sorting" >
				         		积分类型
				         	</th>
				         	<th style="width: 70px" class="sorting" >
				         		排序号
				         	</th>
				         	<th style="width: 55px;" class="sorting" >
				         		操作
				         	</th>
				         </tr>
				     </thead>
				     <tbody align="center">
				 	</tbody>
				 </table>
				 <div id="noData" class="container" style="left:50%;top:50%;
					margin:150 0 0 250px;padding-top:65px;text-align:center;width:500px;">
					<section class="error-container text-center">
						<h1>
							<i class="fa fa-exclamation-triangle"></i>
						</h1>
						<div class="error-divider">
							<h2>您还没有相关数据！</h2>
							<p class="description">协同提高效率，分享拉近距离。</p>
						</div>
					</section>
				</div>
				</div>
				
				<div class="align-center clearfix" style="padding:6px 10px;">
				 	<div  class="pull-right" style="margin-left: 15px;display: none" id="cancleDiv">
						<button type="button" class="btn btn-default" style="background-color: #ccc;" id="cancleBtn">取消</button>
				 	</div>
				 	<div class="pull-right">
						<button type="button" class="btn btn-info ws-btnBlue" id="addBtn">添加</button>
				 	</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
