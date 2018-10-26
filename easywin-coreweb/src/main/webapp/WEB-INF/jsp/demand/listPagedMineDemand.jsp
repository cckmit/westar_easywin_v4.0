<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
						<div>
                            <form action="/demand/listPagedMineDemand" id="searchForm" class="subform">
                                <input type="hidden" name="sid" id="sid" value="${param.sid}">
                                <input type="hidden" name="pager.pageSize" value="10">
                                <input type="hidden" name="activityMenu" value="${param.activityMenu}">
                                <input type="hidden" name="type" value="${demandProcess.type}">
                                <input type="hidden" name="typeName" value="${demandProcess.typeName}">
                                <input type="hidden" name="state" value="${demandProcess.state}">
                                <input type="hidden" name="stateName" value="${demandProcess.stateName}">
                                <div class="btn-group pull-left searchCond">
                                    <div class="table-toolbar ps-margin margin-right-5">
                                        <div class="btn-group">
                                            <a class="btn btn-default dropdown-toggle btn-xs"  data-toggle="dropdown">
                                            	产品筛选<i class="fa fa-angle-down"></i>
                                            </a>
                                            <ul class="dropdown-menu dropdown-default">
                                                <li>
                                                    <a href="javascript:void(0)" class="clearMoreElement" relateList="product_select">不限条件</a>
                                                </li>
                                                <li>
                                                    <a href="javascript:void(0)" class="relateModSelect" busType="070" isMore="1" typeValue="relateModType" relateList="product_select">产品选择</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                    <div style="float: left;width: 250px;display: none">
                                        <select list="listProduct" listkey="id" listvalue="name" id="product_select" name="listProduct.id" multiple="multiple"
                                                moreselect="true" style="width: 100%; height: 100px;">
	                                            <c:forEach items="${demandProcess.listProduct }" var="obj" varStatus="vs">
	                                                <option selected="selected" value="${obj.id}">${obj.name}</option>
	                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="table-toolbar ps-margin margin-right-5">
                                        <div class="btn-group">
                                            <a class="btn btn-default dropdown-toggle btn-xs"  data-toggle="dropdown">
                                            	项目筛选<i class="fa fa-angle-down"></i>
                                            </a>
                                            <ul class="dropdown-menu dropdown-default">
                                                <li>
                                                    <a href="javascript:void(0)" class="clearMoreElement" relateList="item_select">不限条件</a>
                                                </li>
                                                <li>
                                                    <a href="javascript:void(0)" class="relateModSelect" busType="005" isMore="1" typeValue="relateModType" relateList="item_select">项目选择</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                    <div style="float: left;width: 250px;display: none">
                                        <select list="listItem" listkey="id" listvalue="itemName" id="item_select" name="listItem.id" multiple="multiple"
                                                moreselect="true" style="width: 100%; height: 100px;">
                                            <c:forEach items="${demandProcess.listItem }" var="obj" varStatus="vs">
                                                <option selected="selected" value="${obj.id}">${obj.itemName }</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="table-toolbar ps-margin margin-right-5">
                                         <div class="btn-group">
                                             <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
                                                 <c:choose>
                                                     <c:when test="${not empty demandProcess.stateName}">
                                                         <font style="font-weight:bold;">${demandProcess.stateName}</font>
                                                     </c:when>
                                                     <c:otherwise>进度筛选</c:otherwise>
                                                 </c:choose>
                                                 <i class="fa fa-angle-down"></i>
                                             </a>
                                             <ul class="dropdown-menu dropdown-default demandStateEnumData" relateElement="state" relateElementName="stateName">

                                             </ul>
                                         </div>
                                     </div>
                                    <div class="table-toolbar ps-margin margin-right-5">
								         <div class="btn-group">
								             <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="类型">
								             <c:choose>
								             	<c:when test="${not empty demandProcess.typeName}">
								             		<font style="font-weight:bold;">${demandProcess.typeName}</font>
								             	</c:when>
								             	<c:otherwise>
										             	类型
								             	</c:otherwise>
								             </c:choose>
								             	<i class="fa fa-angle-down"></i>
								            </a>
								             <ul class="dropdown-menu dropdown-default">
								                <li><a href="javascript:void(0)" class="clearThisElement" relateElement="type" relateElementName="typeName">不限条件</a></li>
												<li><a href="javascript:void(0)" class="setElementValue" relateElement="type" dataValue="1" relateElementName="typeName">新增</a></li>
												<li><a href="javascript:void(0)" class="setElementValue" relateElement="type" dataValue="2" relateElementName="typeName">变更</a></li>
												<li><a href="javascript:void(0)" class="setElementValue" relateElement="type" dataValue="3" relateElementName="typeName">BUG</a></li>
								              </ul>
					                     </div>
					                </div>
                                    
                                    <div class="table-toolbar ps-margin margin-right-5">
                                        <div class="btn-group cond" id="moreCondition_Div">
                                            <a class="btn btn-default dropdown-toggle btn-xs"
                                               onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
                                                <c:when
                                                        test="${not empty demandProcess.startDate 
                                                        || not empty demandProcess.endDate}">
                                                    <font style="font-weight:bold;">筛选中</font>
                                                </c:when>
                                                <c:otherwise>
                                                    更多
                                                </c:otherwise>
                                            </c:choose> <i class="fa fa-angle-down"></i>
                                            </a>
                                            <div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
                                                <div class="ps-margin ps-search padding-left-10">
                                                    <span class="btn-xs">起止时间：</span> <input
                                                        class="form-control dpd2 no-padding condDate" type="text"
                                                        readonly="readonly" value="${demandProcess.startDate}"
                                                        id="startDate" name="startDate" placeholder="开始时间"
                                                        onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
                                                    <span>~</span> <input
                                                        class="form-control dpd2 no-padding condDate" type="text"
                                                        readonly="readonly" value="${demandProcess.endDate}" id="endDate"
                                                        name="endDate" placeholder="结束时间"
                                                        onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
                                                </div>
                                                <div class="ps-clear padding-top-10"
                                                     style="text-align: center;">
                                                    <button type="submit" class="btn btn-primary btn-xs">查询</button>
                                                    <button type="button"  class="btn btn-default btn-xs margin-left-10"
                                                            onclick="resetMoreCon('moreCondition_Div')">重置</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>


                                <div class="batchOpt" style="display: none">
                                    <div class="btn-group pull-left">
                                        <c:if test="${empty delete}">
                                            <div class="table-toolbar ps-margin">
                                                <div class="btn-group">
                                                    <a class="btn btn-default dropdown-toggle btn-xs" onclick="delTask()"> 批量删除 </a>
                                                </div>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>

								<div class="ps-margin ps-search searchCond">
									<span class="input-icon"> 
										<input id="serialNum" name="serialNum" value="${demandProcess.serialNum}" 
										class="form-control ps-input formElementSearch" type="text" placeholder="需求编号">
										<a href="javascript:void(0)" class="ps-searchBtn">
											<i class="glyphicon glyphicon-search circular danger"></i>
										</a>
									</span>
								</div>
                            </form>
                            
							<div class="widget-buttons ps-widget-buttons">
								<button class="btn btn-info btn-primary btn-xs margin-left-5" type="button" opt-btn="addDemand">
									<i class="fa fa-plus"></i>新增需求
								</button>
							</div>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty demandProcess.listCreator ? 'none':'block'}">
								<strong >发布人筛选:</strong>
								<c:forEach items="${demandProcess.listCreator }" var="obj" varStatus="vs">
									 <span  style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('creator','${param.sid}','${obj.id }')"
									 class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>	
								</c:forEach>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty demandProcess.listProduct ? 'none':'block'}">
								<strong>
									 产品筛选: 
								</strong>
								<c:forEach items="${demandProcess.listProduct }" var="obj" varStatus="vs">
									 <span  style="cursor:pointer;"  title="双击移除" ondblclick="removeChoose('product','${param.sid}','${obj.id }')"
									 class="label label-default margin-right-5 margin-bottom-5">${obj.name }</span>	
								</c:forEach>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty demandProcess.listItem ? 'none':'block'}">
								<strong>
									 项目筛选: 
								</strong>
								<c:forEach items="${demandProcess.listItem }" var="obj" varStatus="vs">
									 <span  style="cursor:pointer;"  title="双击移除" ondblclick="removeChoose('item','${param.sid}','${obj.id }')"
									 class="label label-default margin-right-5 margin-bottom-5">${obj.itemName }</span>	
								</c:forEach>
							</div>
						</div>
						<c:choose>
						<c:when test="${not empty pageBean.recordList}">
						<div class="widget-body">
							<form action="/task/delTask" method="post" id="delForm">
								<input type="hidden" name="sid" value="${param.sid}"/>
								<input type="hidden" id="redirectPage" name="redirectPage" />
								<table class="table table-striped table-hover fixTable"
									id="editabledatatable">
									<thead>
										<tr role="row">
											<th style="width: 30px">
												序
											</th>
											<th style="width: 150px">需求编号</th>
											<th style="width: 150px">星级等级</th>
											<th style="width: 50px">类型</th>
											<th class="no-padding">关联项目</th>
											<th style="width: 120px">关联产品</th>
											<th style="width: 90px">项目负责人</th>
											<th style="width: 90px">进度</th>
											<th style="width: 90px">发布人</th>
											<th style="width: 90px">发布日期</th>
											<th style="width: 90px">操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${pageBean.recordList}" var="demandObj" varStatus="vs">
											<tr class="optTr" taskID="${taskVo.id}" style="clear: both">
												<td class="optTd" style="height: 47px">
													<label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
												</td>
												<td>
													${ demandObj.serialNum}
												</td>
												<td>
													<span class="rating">
														<c:forEach begin="1" end="${demandObj.startLevel }">
															<span class="fa fa-star blue pull-left"></span>
														</c:forEach>
													</span>
												</td>
												<td>
													<tags:viewDataDic type="demandType" code="${demandObj.type}"></tags:viewDataDic>
												</td>
												<td>
													<a href="javascript:void(0)" class="viewModInfo" busId="${ demandObj.itemId}" busType="005">${demandObj.itemName}</a>
												</td>
												
												<td>
													<a href="javascript:void(0)" class="viewModInfo" busId="${ demandObj.productId}" busType="080">${demandObj.productName}</a>
												</td>
												<td class="no-padding-left no-padding-right" align="center">
													<div class="ticket-user pull-left other-user-box">
														<img class="user-avatar userImg" title="${demandObj.itemOwnerName}" 
															src="/downLoad/userImg/${demandObj.comId}/${demandObj.itemOwnerId}"/>
														<span class="user-name">${demandObj.itemOwnerName}</span>
													</div>
												</td>
												<td>
													${demandObj.stateName}
												</td>
												<td class="no-padding-left no-padding-right" align="center">
													<div class="ticket-user pull-left other-user-box">
														<img class="user-avatar userImg" title="${demandObj.creatorName}" 
															src="/downLoad/userImg/${demandObj.comId}/${demandObj.creator}"/>
														<span class="user-name">${demandObj.creatorName}</span>
													</div>
												</td>
												<td>${fn:substring(demandObj.recordCreateTime,0,10)}</td>
												<td>
													<a href="javascript:void(0)" class="blue" opt-viewDemand="viewDemandForCerator" busId="${demandObj.id}">详情</a>
													|
													<c:choose>
														<c:when test="${demandObj.state eq 4 }">
															<a href="javascript:void(0)" class="gray" disabled="false">催办</a>
														</c:when>
														<c:otherwise>
															<a href="javascript:void(0)" class="blue" opt-busRemindBtn data-demandId="${demandObj.id}">催办</a>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</form>
							<tags:pageBar url="/demand/listPagedMineDemand"></tags:pageBar>
						</div>
						</c:when>
					<c:otherwise>
						<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
							<section class="error-container text-center">
								<h1>
									<i class="fa fa-exclamation-triangle"></i>
								</h1>
								<div class="error-divider">
									<h2>未查询到相关数据！</h2>
									<p class="description">协同提高效率，分享拉近距离。</p>
									<a href="javascript:void(0);" opt-btn="addDemand"
										class="return-btn"><i class="fa fa-plus"></i>新增需求</a>
								</div>
							</section>
						</div>
					</c:otherwise>
				</c:choose>
					</div>
				</div>
				
			</div>
				
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->

	</div>
	<!-- /Page Container -->
	<script type="text/javascript">
	</script>
