<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
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
								<form action="/busRemind/listPagedSelfBusRemind" id="sysLogForm">
									<input type="hidden" name="sid" value="${param.sid }">
									<input type="hidden" name="activityMenu" value="${param.activityMenu }">
									
										<div class="btn-group pull-left searchCond">
	                                    	<div class="table-toolbar ps-margin">
							                	<div class="btn-group cond" id="moreCondition_Div">
								                	<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
							                          	<c:choose>
							                          			<c:when test="${not empty busRemind.startDate || not empty busRemind.endDate}">
							                          				<font style="font-weight:bold;">筛选中</font>
							                          			</c:when>
							                          			<c:otherwise>
							                             			更多
							                          			</c:otherwise>
							                          	</c:choose>
							                        	<i class="fa fa-angle-down"></i>
							                        </a>
							                 <div class="dropdown-menu dropdown-default padding-bottom-10" 
							                    style="min-width: 330px;">
												<div class="ps-margin ps-search padding-left-10">
													<span class="btn-xs">起止时间：</span>
							                                   	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${busRemind.startDate}" id="startDate" name="startDate" 
														placeholder="开始时间"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
														<span>~</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${busRemind.endDate}" id="endDate"  name="endDate"
														placeholder="结束时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})"/>
							                    	</div>
							                    	<div class="ps-clear padding-top-10" style="text-align: center;">
							                    		<button type="submit" class="btn btn-primary btn-xs">查询</button>
							                    		<button type="button" class="btn btn-default btn-xs margin-left-10" onclick="resetMoreCon('moreCondition_Div')">重置</button>
							                    	</div>
							                          </div>
							                      </div>
							                  </div>
										</div>
	                                    <div class="ps-margin ps-search">
											<span class="input-icon">
												<input id="content" name="busModName" value="${busRemind.busModName}" class="form-control ps-input" type="text" placeholder="请输入关键字">
												<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
											</span>
										</div>
								</form>
                            </div>
                            <c:choose>
								<c:when test="${not empty list}">
									<div class="widget-body" id="infoList">
											<table class="table table-striped table-hover">
												<thead>
													<tr role="row">
														<th style="width:50px" valign="middle">序号</th>
														  <th style="width:150px" valign="middle" class="text-center">催办时间</th>
														  <th style="width:100px" valign="middle" class="text-center">催办人员</th>
														  <th style="width:300px" valign="middle">催办模块</th>
														  <th valign="middle">催办内容</th>
														  <th style="width:50px" valign="middle">操作</th>
													</tr>
												</thead>
												<tbody>
											 		<c:forEach items="${list}" var="obj" varStatus="vs">
												 		<tr class="optTr">
			                                                <td valign="middle">
																${vs.count}
															</td>
															<td valign="middle" class="text-center">
																${obj.recordCreateTime }
															</td>
															<td valign="middle" class="text-center">
																${obj.userName }
															</td>
															<td valign="middle">
															<c:if test="${not empty obj.busType }"></c:if>
																
																<c:set var="busTypeName">
																	<tags:viewDataDic type="moduleType" code="${obj.busType }"></tags:viewDataDic>
																</c:set>
																<c:if test="${not empty busTypeName}">
																	[${fn:substring(busTypeName,0,2)}]
																</c:if>
															
																<a href="javascript:void(0)" busId="${obj.busId}" busType="${obj.busType}" busForTitle="1" class="viewModInfo"> ${obj.busModName}</a>
															</td>
															<td valign="middle">
																<a href="javascript:void(0)" busForTitle="1" busRemind="${obj.id}">${obj.content}</a>
															</td>
															<td valign="middle">
																<a href="javascript:void(0)" busRemind="${obj.id}">查看</a>
															</td>
			                                            </tr>
											 		</c:forEach>
											 	
												</tbody>
											</table>
											<tags:pageBar url="/busRemind/listPagedSelfBusRemind"></tags:pageBar>
										</div>
										</c:when>
										<c:otherwise>
											 <div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
												<section class="error-container text-center">
													<h1>
														<i class="fa fa-exclamation-triangle"></i>
													</h1>
													<div class="error-divider">
														<h2>暂无催办记录！</h2>
														<p class="description">协同提高效率，分享拉近距离。</p>
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
        <!-- Main Container -->
        
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
	
        <script type="text/javascript">
      //页面加载初始化
        $(function(){
        	//任务名筛选
        	$("#content").blur(function(){
        		//启动加载页面效果
        		layer.load(0, {shade: [0.6,'#fff']});
        		$("#sysLogForm").submit();
        	});
        	
        	$.each($("body").find("[busForTitle='1']"),function(index,object){
        		var content = $(this).html();
        		$(this).attr("title",content)
        	})
        	
        	//文本框绑定回车提交事件
        	$("#content").bind("keydown",function(event){
                if(event.keyCode == "13")    
                {
                	if(!strIsNull($("#content").val())){
                		//启动加载页面效果
                		layer.load(0, {shade: [0.6,'#fff']});
        				$("#sysLogForm").submit();
                	}else{
                		showNotification(2,"请输入检索内容！");
            			$("#searchTaskName").focus();
                	}
                }
            });
        
        	$('body').on('click','a[busRemind]',function(){
        		var busRemind = $(this).attr("busRemind");
	        	var url = "/busRemind/viewBusRemindPage?sid=${param.sid}&id="+busRemind;
	    		openWinByRight(url);
        	})
        	
        	
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
</body>
</html>
