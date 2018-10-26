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
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
	<!-- Page Content -->
	<div>
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
						<div class="col-xs-3 no-padding-right" style="text-align: left;line-height: 40px">
							 <span class="widget-caption themeprimary padding-top-5" style="font-size: 20px">在线学习</span>
						</div>
						<div class="col-xs-9 no-padding-right">
							<div class="searchCond" style="display: block">
								<div class="btn-group pull-left">
									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs"
												data-toggle="dropdown" title="全部类型">
												全部类型
											<i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li>
													<a href="javascript:void(0)" class="clearValue" relateElement="searchAll">全部类型</a>
												</li>
												<li>
													<a href="javascript:void(0)" class="setValue" relateElement="searchAll" dataValue='31'>Office文档</a>
												</li>
												<li>
													<a href="javascript:void(0)" class="setValue" relateElement="searchAll" dataValue='32'>文本文档</a>
												</li>
												<li>
													<a href="javascript:void(0)" class="setValue" relateElement="searchAll" dataValue='33'>PDF文档</a>
												</li>
												<li>
													<a href="javascript:void(0)" class="setValue" relateElement="searchAll" dataValue='34'>图片文档</a>
												</li>
												<li>
													<a href="javascript:void(0)" class="setValue" relateElement="searchAll" dataValue='35'>音频文档</a>
												</li>
												<li>
													<a href="javascript:void(0)" class="setValue" relateElement="searchAll" dataValue='36'>视频文档</a>
												</li>
												<li>
													<a href="javascript:void(0)" class="setValue" relateElement="searchAll" dataValue='37'>其他类型</a>
												</li>
											</ul>
										</div>
									</div>
									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs"
												data-toggle="dropdown" title="全部范围">
												全部范围
												<i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li>
													<a href="javascript:void(0)" class="clearValue" relateElement="searchMe">不限条件</a>
												</li>
												<li>
													<a href="javascript:void(0)" class="setValue" relateElement="searchMe" dataValue='21'>自己上传的</a>
												</li>
												<li>
													<a href="javascript:void(0)" class="setValue" relateElement="searchMe" dataValue='22'>他人分享的</a>
												</li>
											</ul>
										</div>
									</div>

									<div class="table-toolbar ps-margin">
										<div class="btn-group cond" id="moreCondition_Div">
											<a class="btn btn-default dropdown-toggle btn-xs"
												onclick="displayMoreCond('moreCondition_Div')">
												更多
												<i class="fa fa-angle-down"></i>
											</a>
											<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
												<div class="ps-margin ps-search padding-left-10">
													<span class="btn-xs">起止时间：</span>
													<input class="form-control dpd2 no-padding condDate" type="text"
														readonly="readonly" value="${fileDetail.startDate}"
														id="startDate" name="startDate" placeholder="开始时间"
														onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
													<span>~</span>
													<input class="form-control dpd2 no-padding condDate" type="text"
														readonly="readonly" value="${fileDetail.endDate}"
														id="endDate" name="endDate" placeholder="结束时间"
														onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
												</div>
												<div class="ps-clear padding-top-10" style="text-align: center;">
													<button type="button" class="btn btn-primary btn-xs moreSearchBtn">查询</button>
													<button type="button" class="btn btn-default btn-xs margin-left-10 moreClearBtn">重置</button>
												</div>
											</div>
										</div>
									</div>

								</div>
								<div class="ps-margin ps-search">
									<span class="input-icon"> 
										<input id="fileName" name="fileName" value="${fileDetail.fileName}"
											class="form-control ps-input moreSearch" type="text" placeholder="请输入关键字"> 
										<a href="javascript:void(0)" class="ps-searchBtn">
											<i class="glyphicon glyphicon-search circular danger"></i>
										</a>
									</span>
								</div>
							</div>
							<div class="batchOpt" style="display: none">
								<div class="btn-group pull-left">
									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-optType="move">
												批量移动 </a>
										</div>
									</div>

									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-optType="del">
												批量删除 </a>
										</div>
									</div>

									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-optType="share">
												批量分享 </a>
										</div>
									</div>
								</div>
							</div>
							<div class="widget-buttons ps-widget-buttons">
								<button class="btn btn-primary btn-xs" type="button" data-btn="addFiles">
									<i class="fa fa-upload fa-lg"></i> 资料上传
								</button>
							</div>
							<div class="widget-buttons ps-widget-buttons">
								<button class="btn btn-primary btn-xs" type="button" data-btn="addDirPre">
									<i class="fa fa-folder-o fa-lg"></i> 新建文件夹
								</button>
							</div>
							<div class="ps-clear" id="formTempData">
									<input type="hidden" name="classifyId" id="classifyId" value="-1">
									<input name="searchAll" type="hidden" value="${fileDetail.searchAll}" />
									<input name="searchMe" type="hidden" value="${fileDetail.searchMe}" />
									<input name="searchType" type="hidden" value="${fileDetail.searchType}" /> 
									<input type="hidden" name="fileTypes" id="fileTypes" value="${param.fileTypes}" /> 
									<input type="hidden" name="modTypes" id="modTypes" value="${param.modTypes}" />
									<select list="listOwner" listkey="id" listvalue="userName" id="applyUser_select" 
										multiple="multiple" moreselect="true" style="display: none">
									</select>
								</div>
						</div>
						</div>
						
						<div class="widget-body" style="min-height: 450px">
							<div class="row" style="margin-left: 0px;margin-right: 0px">
								<div class="col-xs-3 no-padding-right" style="border-right: 1px solid #ccc;">
									<div class="zTreeDemoBackground" style="overflow:hidden ;overflow-y:scroll;">
										<ul id="zTreeFolder" class="ztree"></ul>
									</div>
								</div>
								<div class="col-xs-9">
									<div class="widget-header bg-bluegray no-shadow">
										<span class="widget-caption blue" id="folderSpan"> 
												全部文件
										</span>
									</div>
									<div class="left">
										<table class="table table-striped table-hover fixTable" id="editabledatatable">
											<thead>
												<tr>
													<th width="5%" valign="middle">
														<label> 
															<input type="checkbox" class="colored-blue"
															id="checkAllBox" onclick="checkAll(this,'ids')"> 
															<span class="text" style="color: inherit;">&nbsp;</span>
														</label>
													</th>
													<th valign="middle" class="hidden-phone">文件名</th>
													<th width="20%" valign="middle">来源</th>
													<th width="10%" valign="middle">大小</th>
													<th width="10%" valign="middle">类型</th>
													<th width="15%" valign="middle">创建人</th>
													<th width="13%" valign="middle">创建时间</th>
		
												</tr>
											</thead>
											<tbody id="listDirs">
											</tbody>
											<tbody id="listFiles">
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
					</div>
				</div>
				<!-- /Page Body -->
			</div>
			
			<!-- /Page Content -->
		</div>
		<!-- /Page Container -->
		<!-- Main Container -->
<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>

