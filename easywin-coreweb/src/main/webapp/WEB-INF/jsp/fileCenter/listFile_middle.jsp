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
<head>

</head>
<body>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div class="checkbox ps-margin pull-left">
								<label> <input type="checkbox" class="colored-blue"
									id="checkAllBox" onclick="checkAll(this,'ids')"> <span
									class="text" style="color: inherit;">全选</span>
								</label>
							</div>
							<form id="fileSearchForm" action="/fileCenter/listPagedFile">
								<input type="hidden" name="sid" value="${param.sid }" /> <input
									type="hidden" id="classifyId" name="classifyId"
									value="${fileDetail.classifyId}" /> <input name="searchAll"
									type="hidden" value="${fileDetail.searchAll}"
									readonly="readonly" /> <input name="searchMe" type="hidden"
									value="${fileDetail.searchMe}" readonly="readonly" /> <input
									name="searchType" type="hidden"
									value="${fileDetail.searchType}" /> <input type="hidden"
									name="fileTypes" id="fileTypes" value="${param.fileTypes}" /> <input
									type="hidden" name="modTypes" id="modTypes"
									value="${param.modTypes}" />
								<div class="searchCond" style="display: block">
									<div class="btn-group pull-left">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs"
													data-toggle="dropdown"> <c:choose>
														<c:when test="${not empty fileTypeList}">
															<font style="font-weight:bold;"> <c:forEach
																	items="${fileTypeList}" var="fileTypeObj"
																	varStatus="vs">
																	<c:if test="${fileTypeObj=='31'}">Office文档</c:if>
																	<c:if test="${fileTypeObj=='32'}">文本文档</c:if>
																	<c:if test="${fileTypeObj=='33'}">PDF文档</c:if>
																	<c:if test="${fileTypeObj=='34'}">图片文档</c:if>
																	<c:if test="${fileTypeObj=='35'}">音频文档</c:if>
																	<c:if test="${fileTypeObj=='36'}">视频文档</c:if>
																	<c:if test="${fileTypeObj=='37'}">其他类型</c:if>
																</c:forEach>
															</font>
														</c:when>
														<c:otherwise>
																	全部类型
																</c:otherwise>
													</c:choose> <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li><a href="javascript:void(0)"
														onclick="setFileTypes('')">全部类型</a></li>
													<li><a href="javascript:void(0)"
														onclick="setFileTypes('31')"> Office文档</a></li>
													<li><a href="javascript:void(0)"
														onclick="setFileTypes('32')">文本文档</a></li>
													<li><a href="javascript:void(0)"
														onclick="setFileTypes('33')">PDF文档</a></li>
													<li><a href="javascript:void(0)"
														onclick="setFileTypes('34')">图片文档</a></li>
													<li><a href="javascript:void(0)"
														onclick="setFileTypes('35')">音频文档</a></li>
													<li><a href="javascript:void(0)"
														onclick="setFileTypes('36')">视频文档</a></li>
													<li><a href="javascript:void(0)"
														onclick="setFileTypes('37')">其他类型</a></li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs"
													data-toggle="dropdown"> <c:choose>
														<c:when test="${fileDetail.searchMe=='21'}">自己上传的</c:when>
														<c:when test="${fileDetail.searchMe=='22'}">他人分享的</c:when>
														<c:otherwise>全部范围</c:otherwise>
													</c:choose> <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li><a href="javascript:void(0)"
														onclick="gets_value('',this,'searchMeC','searchMe')">不限条件</a></li>
													<li><a href="javascript:void(0)"
														onclick="gets_value('21',this,'searchMeC','searchMe')">自己上传的</a></li>
													<li><a href="javascript:void(0)"
														onclick="gets_value('22',this,'searchMeC','searchMe')">他人分享的</a></li>
												</ul>
											</div>
										</div>

										<div class="table-toolbar ps-margin">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs"
													onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
														<c:when
															test="${not empty fileDetail.startDate || not empty fileDetail.endDate}">
															<font style="font-weight:bold;">筛选中</font>
														</c:when>
														<c:otherwise>
							                                            	更多
				                                            			</c:otherwise>
													</c:choose> <i class="fa fa-angle-down"></i>
												</a>
												<div
													class="dropdown-menu dropdown-default padding-bottom-10"
													style="min-width: 330px;">
													<div class="ps-margin ps-search padding-left-10">
														<span class="btn-xs">起止时间：</span> <input
															class="form-control dpd2 no-padding condDate" type="text"
															readonly="readonly" value="${fileDetail.startDate}"
															id="startDate" name="startDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
														<span>~</span> <input
															class="form-control dpd2 no-padding condDate" type="text"
															readonly="readonly" value="${fileDetail.endDate}"
															id="endDate" name="endDate" placeholder="结束时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
													</div>
													<div class="ps-clear padding-top-10"
														style="text-align: center;">
														<button type="submit" class="btn btn-primary btn-xs">查询</button>
														<button type="button"
															class="btn btn-default btn-xs margin-left-10"
															onclick="resetMoreCon('moreCondition_Div')">重置</button>
													</div>
												</div>
											</div>
										</div>

									</div>
									<div class="ps-margin ps-search">
										<span class="input-icon"> <input id="fileName"
											name="fileName" value="${fileDetail.fileName}"
											class="form-control ps-input" type="text"
											placeholder="请输入关键字"> <a href="javascript:void(0)"
											class="ps-searchBtn"><i
												class="glyphicon glyphicon-search circular danger"></i></a>
										</span>
									</div>
								</div>
								<div class="batchOpt" style="display: none">
									<div class="btn-group pull-left">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs"
													onclick="batchOpt('1',this,'batchTypeC','batchType','${param.sid}')">
													批量移动 </a>
											</div>
										</div>

										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs"
													onclick="batchOpt('2',this,'batchTypeC','batchType','${param.sid}')">
													批量删除 </a>
											</div>
										</div>

										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs"
													onclick="batchOpt('3',this,'batchTypeC','batchType','${param.sid}')">
													批量分享 </a>
											</div>
										</div>
									</div>
								</div>
							</form>
							<div class="widget-buttons ps-widget-buttons">
								<button class="btn btn-primary btn-xs" type="button"
									onclick="addFiles('${param.sid}')">
									<i class="fa fa-upload fa-lg"></i> 资料上传
								</button>
							</div>
							<div class="widget-buttons ps-widget-buttons">
								<button class="btn btn-primary btn-xs" type="button"
									onclick="addDirPre('${param.sid}')">
									<i class="fa fa-folder-o fa-lg"></i> 新建文件夹
								</button>
							</div>
						</div>
						<div class="widget-header bg-bluegray no-shadow">
							<span class="widget-caption blue"> <c:if
									test="${not empty leadLine}">
									<a href="javascript:void(0)" onclick="dirFile(-1)"
										style="font-size:15px;"> 所有文件 </a>
								</c:if> <c:if test="${empty leadLine}">
									<c:set var="classifyUser" value="${userInfo.id}"></c:set>
										所有文件
									</c:if> <c:choose>
									<c:when test="${not empty leadLine}">
										<c:forEach items="${leadLine}" var="classify"
											varStatus="status">
											<c:choose>
												<c:when test="${status.last}">
														&gt;&gt;${classify.typeName}
														<c:set var="classifyUser" value="${classify.userId}"></c:set>
												</c:when>
												<c:otherwise>
														&gt;&gt;
														<a href="javascript:void(0)"
														onclick="dirFile(${classify.id})" style="font-size:15px;">
														${classify.typeName} </a>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:when>
								</c:choose>
							</span>
						</div>
						<c:choose>
			<c:when test="${(not empty listFiles) or (not empty listClassifys)}">
						<div class="widget-body">
							<form id="listFileForm" class="subform" onsubmit="return false;">
								<table class="table table-hover general-table">
									<thead>
										<tr>
											<th width="5%" valign="middle"><input type="checkbox"
												name="input10" id="input12"
												onclick="checkAllPre(this,'ids')" /></th>
											<th valign="middle" class="hidden-phone">文件名</th>
											<th width="10%" valign="middle">大小</th>
											<th width="30%" valign="middle">来源</th>
											<th width="12%" valign="middle">创建人</th>
											<th width="13%" valign="middle">创建时间</th>

										</tr>
									</thead>
									<tbody class="listDirs">
										<c:choose>
											<c:when test="${not empty listClassifys}">
												<c:forEach items="${listClassifys}" var="classify"
													varStatus="status">
													<tr class="dirInfo">
														<td height="40"><label> <input
																type="checkbox" name="ids"
																${(classify.isSys==1||classify.userId!=userInfo.id)?'disabled="disabled"':'' }
																fileType="dir" value="${classify.id}" /> <span
																class="text"></span>
														</label></td>
														<td height="40"
															${(classify.isSys==1||classify.userId!=userInfo.id)?'':'class="dirName"'}>
															<img src="/static/images/folder_open.png"
															style="clear: both;border-radius: 0px;height: auto;width: auto;border:0px" />
															<input type="hidden" name="tempDirName"
															value="${classify.typeName}" /> <a
															href="javascript:void(0)"
															onclick="dirFile(${classify.id})">${classify.typeName}</a>
														</td>
														<td>--</td>
														<td height="40">--</td>
														<td height="40"
															${(classify.isSys==1||classify.userId!=userInfo.id)?'':'class="infoDetail"' }
															style="text-align: left;">
															<div class="ticket-user pull-left other-user-box">
																<img src="/downLoad/userImg/${classify.comId}/${classify.userId}?sid=${param.sid}"
																	title="${classify.userName}" class="user-avatar" />
																<span class="user-name">${classify.userName}</span>
															</div>
														</td>
														<td height="40"
															${(classify.isSys==1||classify.userId!=userInfo.id)?'':'class="infoDetail"'}>${fn:substring(classify.recordCreateTime,0,10)}</td>
														<td height="40" colspan="2"
															style="display: none;text-align: center;"
															${(classify.isSys==1||classify.userId!=userInfo.id)?'':'class="opt"'}>
															<a href="javascript:void(0)"
															class="btn btn-info ws-btnBlue"
															style="width: 55px;height: 30px;"
															onclick="modifyDirName(this,${classify.id},'${param.sid }')">修改</a>
															<a href="javascript:void(0)"
															class="btn btn-info ws-btnBlue"
															style="width: 55px;height: 30px"
															onclick="moveDir(this,${classify.id},'${param.sid }')">移动</a>
															<a href="javascript:void(0)"
															class="btn btn-info ws-btnBlue"
															style="background-color: red;width: 55px;height: 30px"
															onclick="delDir(this,${classify.id},'${param.sid }')">删除</a>
														</td>
													</tr>
												</c:forEach>
											</c:when>
										</c:choose>
									</tbody>
									<tbody class="listFiles">
										<c:choose>
											<c:when test="${not empty listFiles}">
												<c:forEach items="${listFiles}" var="fileDetialObj"
													varStatus="status">
													<tr class="fileInfoOne">
														<td height="40" title="${fileDetialObj.moduleTypeName}">
															<label> <input type="checkbox" name="ids"
																fileType="file"
																value="${fileDetialObj.fileId}@${fileDetialObj.moduleType}@${fileDetialObj.modulePrim}@${fileDetialObj.userId}"
																owner='${userInfo.id==fileDetialObj.userId}' /> <span
																class="text"></span>
														</label>
														</td>
														<td height="40" title="${fileDetialObj.fileName}"><tags:cutString
																num="36">${fileDetialObj.fileName}</tags:cutString></td>
														<td height="40">${fileDetialObj.sizem}</td>
														<td title="${fileDetialObj.fileDescribe }"><c:choose>
																<c:when test="${fileDetialObj.moduleType != '013'}">
																</c:when>
															</c:choose> <a href="javascript:void(0)"
															onclick="viewDetail(${fileDetialObj.modulePrim},'${fileDetialObj.moduleType }','${param.sid}')">
																[${fn:substring(fileDetialObj.moduleTypeName,0,2) }] <tags:cutString
																	num="25">${fileDetialObj.fileDescribe}</tags:cutString>
														</a></td>
														<td height="40" class="infoDetail">
															<div class="ticket-user pull-left other-user-box">
																<img src="/downLoad/userImg/${fileDetialObj.comId}/${fileDetialObj.userId}?sid=${param.sid}"
																	title="${fileDetialObj.userName}" class="user-avatar" />
																<span class="user-name">${fileDetialObj.userName}</span>
														</td>
														</div>
														<td height="40" class="infoDetail">${fn:substring(fileDetialObj.fileDate,0,10)}</td>
														<td colspan="2" style="display: none;text-align: center;"
															class="opt"><c:choose>
																<%--是 自己上传的，并且为附件管理模块，可分享、删除、移动--%>
																<c:when
																	test="${userInfo.id==fileDetialObj.userId && fileDetialObj.moduleType=='013'}">
																	<c:choose>
																		<c:when
																			test="${fileDetialObj.fileExt=='doc' || fileDetialObj.fileExt=='docx' || fileDetialObj.fileExt=='xls' || fileDetialObj.fileExt=='xlsx' || fileDetialObj.fileExt=='ppt' || fileDetialObj.fileExt=='pptx'}">
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue btn5"
																				onclick="moveFile(this,${fileDetialObj.modulePrim},'${fileDetialObj.moduleType}',${fileDetialObj.fileId},${fileDetialObj.userId},'${fileDetialObj.fileDescribe}','${param.sid}')">移动</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue btn5"
																				onclick="shareFile(this,${fileDetialObj.modulePrim},${fileDetialObj.fileId},'${param.sid}')">共享</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue btn5"
																				style="background-color: red;"
																				onclick="delFile(this,${fileDetialObj.modulePrim},'${fileDetialObj.moduleType}',${fileDetialObj.fileId},${fileDetialObj.userId},'${param.sid}')">删除</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue btn5"
																				onclick="viewOfficePage('${fileDetialObj.fileId}','${fileDetialObj.uuid}','${fileDetialObj.fileName}','${fileDetialObj.fileExt}','${param.sid}','${fileDetialObj.moduleType}','${fileDetialObj.modulePrim}')">预览</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue btn5"
																				style="background-color: red;"
																				onclick="downLoad('${fileDetialObj.uuid}','${fileDetialObj.fileName}','${param.sid}')">下载</a>
																		</c:when>
																		<c:when
																			test="${fileDetialObj.fileExt=='txt' || fileDetialObj.fileExt=='pdf'}">
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue btn5"
																				onclick="moveFile(this,${fileDetialObj.modulePrim},'${fileDetialObj.moduleType}',${fileDetialObj.fileId},${fileDetialObj.userId},'${fileDetialObj.fileDescribe}','${param.sid}')">移动</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue btn5"
																				onclick="shareFile(this,${fileDetialObj.modulePrim},${fileDetialObj.fileId},'${param.sid}')">共享</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue btn5"
																				style="background-color: red;"
																				onclick="delFile(this,${fileDetialObj.modulePrim},'${fileDetialObj.moduleType}',${fileDetialObj.fileId},${fileDetialObj.userId},'${param.sid}')">删除</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue btn5"
																				onclick="viewOfficePage('${fileDetialObj.fileId}','${fileDetialObj.uuid}','${fileDetialObj.fileName}','${fileDetialObj.fileExt}','${param.sid}','${fileDetialObj.moduleType}','${fileDetialObj.modulePrim}')">预览</a>
																			<a class="btn btn-info ws-btnBlue btn5"
																				style="background-color: red;"
																				href="/downLoad/down/${fileDetialObj.uuid}/${fileDetialObj.fileName}?sid=${param.sid}"
																				title="下载">下载</a>
																		</c:when>
																		<c:when
																			test="${fileDetialObj.fileExt=='jpg'||fileDetialObj.fileExt=='bmp'||fileDetialObj.fileExt=='gif'||fileDetialObj.fileExt=='jpeg'||fileDetialObj.fileExt=='png'}">
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue btn5"
																				onclick="moveFile(this,${fileDetialObj.modulePrim},'${fileDetialObj.moduleType}',${fileDetialObj.fileId},${fileDetialObj.userId},'${fileDetialObj.fileDescribe}','${param.sid}')">移动</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue btn5"
																				onclick="shareFile(this,${fileDetialObj.modulePrim},${fileDetialObj.fileId},'${param.sid}')">共享</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue btn5"
																				style="background-color: red;"
																				onclick="delFile(this,${fileDetialObj.modulePrim},'${fileDetialObj.moduleType}',${fileDetialObj.fileId},${fileDetialObj.userId},'${param.sid}')">删除</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue btn5"
																				onclick="showPic('/downLoad/down/${fileDetialObj.uuid}/${fileDetialObj.fileName}','${param.sid}','${fileDetialObj.fileId}','${fileDetialObj.moduleType}','${fileDetialObj.modulePrim}')">预览</a>
																			<a class="btn btn-info ws-btnBlue btn5"
																				style="background-color: red;"
																				href="/downLoad/down/${fileDetialObj.uuid}/${fileDetialObj.fileName}?sid=${param.sid}"
																				title="下载">下载</a>
																		</c:when>
																		<c:otherwise>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				onclick="moveFile(this,${fileDetialObj.modulePrim},'${fileDetialObj.moduleType}',${fileDetialObj.fileId},${fileDetialObj.userId},'${fileDetialObj.fileDescribe}','${param.sid}')">移动</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				onclick="shareFile(this,${fileDetialObj.modulePrim},${fileDetialObj.fileId},'${param.sid}')">共享</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				style="background-color: red;"
																				onclick="delFile(this,${fileDetialObj.modulePrim},'${fileDetialObj.moduleType}',${fileDetialObj.fileId},${fileDetialObj.userId},'${param.sid}')">删除</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				style="background-color: red;"
																				onclick="downLoad('${fileDetialObj.uuid}','${fileDetialObj.fileName}','${param.sid}')">下载</a>
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:when
																	test="${userInfo.id!=fileDetialObj.userId && fileDetialObj.moduleType=='013'}">
																	<%--非 自己上传的，并且为本模块，已放在自己的文件下，可移动，不能删除--%>
																	<a href="javascript:void(0)"
																		class="btn btn-info ws-btnBlue"
																		style="width: 55px;height: 30px"
																		onclick="moveFile(this,${fileDetialObj.modulePrim},'${fileDetialObj.moduleType}',${fileDetialObj.fileId},${fileDetialObj.userId},'${fileDetialObj.fileDescribe}','${param.sid}')">移动</a>
																	<c:choose>
																		<c:when
																			test="${fileDetialObj.fileExt=='doc' || fileDetialObj.fileExt=='docx' || fileDetialObj.fileExt=='xls' || fileDetialObj.fileExt=='xlsx' || fileDetialObj.fileExt=='ppt' || fileDetialObj.fileExt=='pptx' }">
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				style="width: 55px;height: 30px"
																				onclick="viewOfficePage('${fileDetialObj.fileId}','${fileDetialObj.uuid}','${fileDetialObj.fileName}','${fileDetialObj.fileExt}','${param.sid}','${fileDetialObj.moduleType}','${fileDetialObj.modulePrim}')">预览</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				style="background-color: red;width: 55px;height: 30px"
																				onclick="downLoad('${fileDetialObj.uuid}','${fileDetialObj.fileName}','${param.sid}')">下载</a>
																		</c:when>
																		<c:when
																			test="${fileDetialObj.fileExt=='txt' || fileDetialObj.fileExt=='pdf'}">
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				style="width: 55px;height: 30px"
																				onclick="viewOfficePage('${fileDetialObj.fileId}','${fileDetialObj.uuid}','${fileDetialObj.fileName}','${fileDetialObj.fileExt}','${param.sid}','${fileDetialObj.moduleType}','${fileDetialObj.modulePrim}')">预览</a>
																			<a class="btn btn-info ws-btnBlue"
																				style="background-color: red;width: 55px;height: 30px"
																				href="/downLoad/down/${fileDetialObj.uuid}/${fileDetialObj.fileName}?sid=${param.sid}"
																				title="下载">下载</a>
																		</c:when>
																		<c:when
																			test="${fileDetialObj.fileExt=='jpg'||fileDetialObj.fileExt=='bmp'||fileDetialObj.fileExt=='gif'||fileDetialObj.fileExt=='jpeg'||fileDetialObj.fileExt=='png'}">
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				style="width: 55px;height: 30px"
																				onclick="showPic('/downLoad/down/${fileDetialObj.uuid}/${fileDetialObj.fileName}','${param.sid}','${fileDetialObj.fileId}','${fileDetialObj.moduleType}','${fileDetialObj.modulePrim}')">预览</a>
																			<a class="btn btn-info ws-btnBlue"
																				style="background-color: red;width: 55px;height: 30px"
																				href="/downLoad/down/${fileDetialObj.uuid}/${fileDetialObj.fileName}?sid=${param.sid}"
																				title="下载">下载</a>
																		</c:when>
																		<c:otherwise>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				style="background-color: red;width: 55px;height: 30px"
																				onclick="downLoad('${fileDetialObj.uuid}','${fileDetialObj.fileName}','${param.sid}')">下载</a>
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:when test="${fileDetialObj.moduleType!='013'}">
																	<%--是 其他模块的，没有归档，只能移动--%>
																	<a href="javascript:void(0)"
																		class="btn btn-info ws-btnBlue"
																		style="width: 55px;height: 30px"
																		onclick="moveFile(this,${fileDetialObj.modulePrim},'${fileDetialObj.moduleType}',${fileDetialObj.fileId},${fileDetialObj.userId},'${fileDetialObj.fileDescribe}','${param.sid}')">移动</a>
																	<c:choose>
																		<c:when
																			test="${fileDetialObj.fileExt=='doc' || fileDetialObj.fileExt=='docx' || fileDetialObj.fileExt=='xls' || fileDetialObj.fileExt=='xlsx' || fileDetialObj.fileExt=='ppt' || fileDetialObj.fileExt=='pptx' }">
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				style="width: 55px;height: 30px"
																				onclick="viewOfficePage('${fileDetialObj.fileId}','${fileDetialObj.uuid}','${fileDetialObj.fileName}','${fileDetialObj.fileExt}','${param.sid}','${fileDetialObj.moduleType}','${fileDetialObj.modulePrim}')">预览</a>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				style="background-color: red;width: 55px;height: 30px"
																				onclick="downLoad('${fileDetialObj.uuid}','${fileDetialObj.fileName}','${param.sid}')">下载</a>
																		</c:when>
																		<c:when
																			test="${fileDetialObj.fileExt=='txt' || fileDetialObj.fileExt=='pdf'}">
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				style="width: 55px;height: 30px"
																				onclick="viewOfficePage('${fileDetialObj.fileId}','${fileDetialObj.uuid}','${fileDetialObj.fileName}','${fileDetialObj.fileExt}','${param.sid}','${fileDetialObj.moduleType}','${fileDetialObj.modulePrim}')">预览</a>
																			<a class="btn btn-info ws-btnBlue"
																				style="background-color: red;width: 55px;height: 30px"
																				href="/downLoad/down/${fileDetialObj.uuid}/${fileDetialObj.fileName}?sid=${param.sid}"
																				title="下载">下载</a>
																		</c:when>
																		<c:when
																			test="${fileDetialObj.fileExt=='jpg'||fileDetialObj.fileExt=='bmp'||fileDetialObj.fileExt=='gif'||fileDetialObj.fileExt=='jpeg'||fileDetialObj.fileExt=='png'}">
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				style="width: 55px;height: 30px"
																				onclick="showPic('/downLoad/down/${fileDetialObj.uuid}/${fileDetialObj.fileName}','${param.sid}','${fileDetialObj.fileId}','${fileDetialObj.moduleType}','${fileDetialObj.modulePrim}')">预览</a>
																			<a class="btn btn-info ws-btnBlue"
																				style="background-color: red;width: 55px;height: 30px"
																				href="/downLoad/down/${fileDetialObj.uuid}/${fileDetialObj.fileName}?sid=${param.sid}"
																				title="下载">下载</a>
																		</c:when>
																		<c:otherwise>
																			<a href="javascript:void(0)"
																				class="btn btn-info ws-btnBlue"
																				style="background-color: red;width: 55px;height: 30px"
																				onclick="downLoad('${fileDetialObj.uuid}','${fileDetialObj.fileName}','${param.sid}')">下载</a>
																		</c:otherwise>
																	</c:choose>
																</c:when>
															</c:choose></td>
													</tr>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr class="noInfo">
													<td height="40" colspan="7" style="text-align: center;"><h3>没有文档！</h3></td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</form>
							<tags:pageBar url="/fileCenter/listPagedFile"></tags:pageBar>
						</div>
						</c:when>
					<c:otherwise>
						<form id="fileSearchForm" action="/fileCenter/listPagedFile">
							<input type="hidden" name="sid" value="${param.sid }" />
							<input type="hidden" name="modTypes" id="modTypes" value="${param.modTypes}" />
							<input type="hidden" id="classifyId" name="classifyId" value="${fileDetail.classifyId}" />
							<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
								<section class="error-container text-center">
									<h1>
										<i class="fa fa-exclamation-triangle"></i>
									</h1>
									<div class="error-divider">
										<h2>暂无相关附件文档！</h2>
										<p class="description">协同提高效率，分享拉近距离。</p>
										<a href="javascript:void(0);" onclick="addFiles('${param.sid}');"
											class="return-btn"><i class="fa fa-plus"></i>资料上传</a>
									</div>
								</section>
							</div>
						</form>
					</c:otherwise>
					</c:choose>
					</div>
				</div>
				<!-- /Page Body -->
			</div>
			
			<!-- /Page Content -->
		</div>
		<!-- /Page Container -->
		<!-- Main Container -->
</body>
</html>

