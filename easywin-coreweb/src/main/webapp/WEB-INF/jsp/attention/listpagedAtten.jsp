<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
		<!-- 框架样式 -->
		<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
		<script type="text/javascript" src="/static/js/shareCode.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<script type="text/javascript">
		$(function(){
			//初始化名片
			initCard('${param.sid}');
			//项目名筛选
			$("#modTitle").blur(function(){
				$("#searchForm").submit();
			});
			//文本框绑定回车提交事件
			$("#modTitle").bind("keydown",function(event){
		        if(event.keyCode == "13")    
		        {
		        	if(!strIsNull($("#modTitle").val())){
		        		$("#searchForm").submit();
		        	}
		        }
		    });
			//更多筛选条件显示层
			$("#moreFilterCondition").click(function(){
		        var display = $("#moreFilterCondition_div").css("display");
		        if("none"==display){
		            $(this).html('隐藏');
		            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
		        }else if("block"==display){
		            $(this).html('更多筛选')
		            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
		        }

		      });
			var timeoutProcess;
			//鼠标移到modTypeDiv区域时，显示下拉菜单
			$("#modTypeDiv").mouseover(function(e){
				evt = window.event || e; 
				var obj = evt.toElement || evt.relatedTarget; 
				var pa = this; 
				if(pa.contains(obj)){
					clearTimeout(timeoutProcess);
					return false; 
				} 
			});
			//鼠标移出modTypeDiv区域时，隐藏下拉菜单
			$("#modTypeDiv").mouseout(function(e){
				evt = window.event || e; 
				var obj = evt.toElement || evt.relatedTarget; 
				var pa = this; 
				if(pa.contains(obj)){
					return false; 
				} 
				timeoutProcess = setTimeout(function(){
					var pObj = $("#modTypeA").parent();
					$(pObj).removeClass("open");
				},200)
			});
			//点击<li>的时候不考虑checkbox
			$("#modTypeId li").click(function(e){
				var evt = e?e:window.event;
				var ele = evt.srcElement || evt.target;
				var type = ele.type;
				if(type && type.toLowerCase()=='checkbox'){
					var id = ele.id;
					if(id && id=='allModType'){
						setIsCheckAll('searchForm','allModType','modTypeA','modTypeId','modTypes');
					}else{
						var checkbox = $(this).find("input[type='checkbox']");
						if(!$(checkbox).attr("checked")){
							$("#allModType").attr("checked",false);
						}
					}
					return;
				}else{
					$("#modTypeA").parent().addClass("open");
					var checkbox = $(this).find("input[type='checkbox']");
					if($(checkbox).attr("id")){//点击的是全选
						if($(checkbox).attr("checked")){//复选框是全选已选中，则取消选中
							$(checkbox).attr("checked",false);
						}else{
							$(checkbox).attr("checked",true);
						}
						setIsCheckAll('searchForm','allModType','modTypeA','modTypeId','modTypes');
					}else{
						if($(checkbox).attr("checked")){
							$(checkbox).attr("checked",false);
							$("#allModType").attr("checked",false);
						}else{
							$(checkbox).attr("checked",true);
						}
					}
				}
			})
			//modTypeId区域移除后若有变动查询
			$("#modTypeId").mouseout(function(e){ 
				evt = window.event || e; 
				var obj = evt.toElement || evt.relatedTarget; 
				var pa = this; 
				if(pa.contains(obj)) return false; 
				var diff=0;
				var array = $("#searchForm").find(":checkbox[name='modTypes']:checked");
				//所选项
				var modTypes = new Array();
				if(array && array.length>0){
					for(var i=0;i<array.length;i++){
						modTypes.push($(array[i]).val());
						if(!$(array[i]).attr("prechecked")){//本次有上次没有选择的
							diff+=1;
						}
						
					}
				}
				var arrayPre = $("#searchForm").find(":checkbox[name='modTypes'][prechecked='1']");
				//所选项
				var modTypePre = new Array();
				if(arrayPre && arrayPre.length>0){
					for(var i=0;i<arrayPre.length;i++){
						if(!$(array[i]).attr("checked")){//本次有上次没有选择的
							diff+=1;
						}
					}
				}
				if(diff>0){
					submitForm();
				}else{
					$("#modTypeA").parent().removeClass("open");
				}
			});
		});
		/**
		 * 选择日期
		 */
		function submitForm(){
			$("#searchForm").submit();
		}
		//查看详情
		function viewDetailMod(id,type){
			if('012'==type){//客户
				$.post("/crm/authorCheck?sid=${param.sid}",{Action:"post",customerId:id},     
						function (msgObjs){
						if(!msgObjs.succ){
							showNotification(1,msgObjs.promptMsg);
						}else{
							window.location.href="/crm/viewCustomer?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
						}
				},"json");
			}else if('005'==type){//项目
				$.post("/item/authorCheck?sid=${param.sid}",{Action:"post",itemId:id},     
						function (msgObjs){
						if(!msgObjs.succ){
							showNotification(1,msgObjs.promptMsg);
						}else{
							window.location.href="/item/viewItemPage?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
						}
				},"json");
			}else if('003'==type){//任务
				$.post("/task/authorCheck?sid=${param.sid}",{Action:"post",taskId:id},     
						function (msgObjs){
						if(!msgObjs.succ){
							showNotification(1,msgObjs.promptMsg);
						}else{
							window.location.href="/task/viewTask?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
						}
				},"json");
			}else if('004'==type){//投票
				$.post("/vote/authorCheck?sid=${param.sid}",{Action:"post",voteId:id},     
						function (msgObjs){
						if(!msgObjs.succ){
							showNotification(1,msgObjs.promptMsg);
						}else{
							window.location.href="/vote/voteDetail?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
						}
				},"json");
			}else if('011'==type){//问答
				window.location.href="/qas/viewQuesPage?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
			}else if('1'==type){//普通分享
				window.location.href="/msgShare/msgShareViewPage?sid=${param.sid}&id="+id+"&type="+type+"&redirectPage="+encodeURIComponent(window.location.href);
			}
			
		}
		//人员筛选
		function userOneForUserIdCallBack(userId,userIdtag,userName,userNametag){
			$("#"+userIdtag).val(userId);
			$("#"+userNametag).val(userName);
			$("#searchForm").submit();
			
		}

</script>
<style type="text/css">
#modTypeId input{
margin-left: 8px
}
</style>
</head>
<body>
<section id="container">
<!-- 系统头部装载 -->
<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<!-- 左边菜单 -->
<jsp:include page="/WEB-INF/jsp/include/left_menu.jsp"></jsp:include>
<!--main content start-->
<section id="main-content">
<!--任务按钮-->
<section class="wrapper wrapper2">
	<div class="ws-wrapIn">
		<div class="ws-tabTit">
			<ul class="ws-tabBar">
				<li>个人中心</li>
			</ul>
		</div>
		<div class="ws-projectBox">
			<div class="row">
				<div class="col-sm-12">
					<!--任务概述-->
					<section class="panel">
                        <header class="panel-heading tab-bg-dark-navy-blue ">
                            <ul class="nav nav-tabs">
                                <li class="">
                                    <a data-toggle="tab" href="javascript:void(0)" onclick="window.self.location='/userInfo/aboutUserInfoPage?sid=${param.sid }'">工作动态</a>
                                </li>
                                <li class="active">
                                    <a data-toggle="tab"  href="javascript:void(0)" onclick="window.self.location='/attention/listpagedAtten?sid=${param.sid}&pager.pageSize=15'">关注动态</a>
                                </li>
                                 <li>
									<a data-toggle="tab" href="javascript:void(0);" onclick="window.self.location='/jiFen/jiFenTabPage?sid=${param.sid}&viewType=341'">积分与等级</a>
								</li>
								<li>
									<a data-toggle="tab" href="javascript:void(0);" id="useStaticTab">使用统计</a>
								</li>
                                <li>
                                    <a data-toggle="tab" href="javascript:void(0)" onclick="window.self.location='/systemLog/listPagedSelfSysLog?sid=${param.sid}&pager.pageSize=15'">个人系统日志</a>
                                </li>
                            </ul>
                        </header>
                        <header class="panel-heading">
                        <form id="searchForm" action="/attention/listpagedAtten">
						 <input type="hidden" name="redirectPage"/>
						 <input type="hidden" name="pager.pageSize" value="15"/>
						 <input type="hidden" id="busType" name="busType" value="${atten.busType}"/>
						 <input type="hidden" id="owner" name="owner" value="${atten.owner}"/>
						 <input type="hidden" id="userName" name="userName" value="${atten.userName}"/>
						 <input type="hidden" name="sid" value="${param.sid}"/>
							<div class="ws-headBox">
								<div class="pull-left ws-toolbar">
									<div class="dropdown pull-left" id="modTypeDiv">
									 <a class="dropdown-toggle" href="javascript:void(0)" onclick="displayMenu('modTypeA')"id="modTypeA">
									 		<c:choose>
									 			<c:when test="${not empty modList }">
									 			<font style="font-weight:bold;">
										 			<c:forEach items="${modList}" var="modTypeObj" varStatus="vs">
										 			<c:if test="${modTypeObj=='003'}">任务模块</c:if>
										 			<c:if test="${modTypeObj=='004'}">投票模块</c:if>
										 			<c:if test="${modTypeObj=='005'}">项目模块</c:if>
										 			<c:if test="${modTypeObj=='011'}">问答模块</c:if>
										 			<c:if test="${modTypeObj=='012'}">客户模块</c:if>
										 			<c:if test="${modTypeObj=='100'}">分享模块</c:if>
										 			<c:if test="${!vs.last}">,</c:if>
										 			<c:if test="${vs.count mod 3 eq 0 && not vs.last }"><br></c:if>
										 			</c:forEach>
										 			</font>
									 			</c:when>
									 			<c:otherwise>
									 			全部模块
									 			</c:otherwise>
									 		</c:choose>
												<b class="caret"></b>
											</a>
											<ul class="dropdown-menu extended logout" id="modTypeId">
												<li><a href="javascript:void(0);" onfocus="this.blur();">全部模块<input type="checkbox" id="allModType" ${fn:length(modList) eq 6?'checked':''}></a></li>
												<li><a href="javascript:void(0);" onfocus="this.blur();">任务模块<input type="checkbox" name="modTypes" value="003" ${fn:contains(modList,'003')?'checked  prechecked="1"':''}></a></li>
												<li><a href="javascript:void(0);" onfocus="this.blur();">投票模块<input type="checkbox" name="modTypes" value="004" ${fn:contains(modList,'004')?'checked  prechecked="1"':''}></a></li>
												<li><a href="javascript:void(0);" onfocus="this.blur();">项目模块<input type="checkbox" name="modTypes" value="005" ${fn:contains(modList,'005')?'checked  prechecked="1"':''}></a></li>
												<li><a href="javascript:void(0);" onfocus="this.blur();">问答模块<input type="checkbox" name="modTypes" value="011" ${fn:contains(modList,'011')?'checked  prechecked="1"':''}></a></li>
												<li><a href="javascript:void(0);" onfocus="this.blur();">客户模块<input type="checkbox" name="modTypes" value="012" ${fn:contains(modList,'012')?'checked  prechecked="1"':''}></a></li>
												<li><a href="javascript:void(0);" onfocus="this.blur();">分享模块<input type="checkbox" name="modTypes" value="100" ${fn:contains(modList,'100')?'checked  prechecked="1"':''}></a></li>
											</ul>
									</div>
									<div class="dropdown pull-left">
											<a data-toggle="dropdown" class="dropdown-toggle" href="javascript:void(0);">
											<c:choose>
												<c:when test="${not empty atten.userName}">
													<font style="font-weight:bold;">${atten.userName}</font>	
												</c:when>
												<c:otherwise>责任人筛选</c:otherwise>
											</c:choose>
											<b class="caret"></b>
											</a>
											<ul class="dropdown-menu extended logout">
												<li><a href="javascript:void(0);" onclick="userOneForUserIdCallBack('','owner','','userName')">清空条件</a></li>
												<li><a href="javascript:void(0);" onclick="userOneForUserId('${userInfo.id}','${userInfo.userName}','','${param.sid}','owner','userName');">人员选择</a></li>
											</ul>
										</div>
									<a href="javascript:void(0);" class="pull-left" id="moreFilterCondition">${(not empty atten.startDate || not empty atten.endDate)?'隐藏':'更多筛选'}</a>
								</div>
								<div class="ws-floatRight">
									<ul class="ws-icon">
											<li>
												<input id="modTitle" name="modTitle" value="${atten.modTitle}" type="text" class="form-control search ws-search-one" placeholder="名称检索" title="名称检索"/>
											</li>
										</ul>
								</div>
								<div class="ws-clear"></div>
							</div>
							<div class="ws-none" id="moreFilterCondition_div" style="display:${(not empty atten.startDate || not empty atten.endDate)?'block':'none'};">
								<div class="form-inline">
									<div class="form-group">
										<div class="input-group input-large">
											<input class="form-control dpd1" type="text" readonly="readonly" value="${atten.startDate}" id="startDate" name="startDate" placeholder="开始时间"  onFocus="WdatePicker({onpicked:function(){submitForm()},oncleared: function(){submitForm()},dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
											<span class="input-group-addon">To</span>
											<input class="form-control dpd2" type="text" readonly="readonly" value="${atten.endDate}" id="endDate"  name="endDate" placeholder="结束时间" onFocus="WdatePicker({onpicked:function(){submitForm()},oncleared: function(){submitForm()},dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})"/>
										</div>
									</div>
								</div>
							</div>
						</form>
                        </header>
                        
                        
                        <div class="panel-body">
	                        <div class="ws-listContent">
		                        <form id="delForm">
		                        	<c:choose>
								 	<c:when test="${not empty list}">
								 		<c:forEach items="${list}" var="obj" varStatus="status">
								 			<div class="ws-shareBox">
													<div class="shareHead" style="text-align: center;" data-container="body" data-toggle="popover" 
													data-user='${obj.owner}' data-busId='${obj.busId}' data-busType='${obj.busType}'>
														<img src="/downLoad/userImg/${obj.comId}/${obj.userId}?sid=${param.sid}"
															title="${obj.userName}" />
														<span style="position: relative;top: 10px;" class="ws-blue">${obj.userName}</span>
													</div>
													<div class="shareText">
														<div class="ws-box-list">
															<div class="ws-type ws-listQues">
																<h5>
																<a href="javascript:void(0);" class="fa fa-star ws-star" style="margin:0px 5px" title="取消关注" 
																onclick="changeAtten('${obj.busType}',${obj.busId},1,'${param.sid}',this)"></a>
																	<span style="font-size: 15px">
																		<c:choose>
																			<c:when test="${obj.busType=='1'}">
																				[信息]
																			</c:when>
																			<c:otherwise>
																				[${fn:substring(obj.busTypeName,0,2)}]
																			</c:otherwise>
																		</c:choose>
																	</span>
																	<a href="javascript:void(0)" onclick="viewDetailMod(${obj.busId},'${obj.busType}')" class="${obj.isRead==0?'noread':'readed'}">
																		<tags:cutString num="80">${obj.modTitle}</tags:cutString> 	
																	</a>
																</h5>
															</div>
																<c:if test="${not empty obj.content}">
																	<div>
																		<p class="ws-texts ws-listQues">
																			<a href="javascript:void(0);" onclick="viewDetailMod(${obj.busId},'${obj.busType}')">
																			<tags:viewTextArea>
																				<tags:cutString num="302">${obj.content}</tags:cutString>
																			</tags:viewTextArea>
																			</a>
																		</p>
																	</div>
																</c:if>
															<div class="ws-type">
																<c:choose>
																	<c:when test="${not empty obj.modiferName}">
																		<i>更新人：${obj.modiferName}</i>
																	</c:when>
																	<c:otherwise>
																		<i>责任人：${obj.userName}</i>
																	</c:otherwise>
																</c:choose>
																
																<c:choose>
																	<c:when test="${not empty obj.modifTime}">
																		<i>更新时间：${fn:substring(obj.modifTime,0,16)}</i>
																	</c:when>
																	<c:otherwise>
																		<i>更新时间：${fn:substring(obj.modTime,0,16)}</i>
																	</c:otherwise>
																</c:choose>
															</div>
														</div>
													</div>
													<div class="ws-clear"></div>
												</div>
									 		</c:forEach>
									 	</c:when>
									 	<c:otherwise>
									 		<div style="text-align: center;">
									 			<h3>没有相关信息！</h3>
									 		</div>
									 	</c:otherwise>
									 </c:choose>
								</form>
								<tags:pageBar url="/attention/listpagedAtten"></tags:pageBar>
	                        </div>
                        </div>
                    </section>
				</div>
			</div>
		</div>
	</div>
</section>
</section>
<!--main content end-->
<!--right sidebar start-->
<div class="right-sidebar">
	<div class="ws-transactor">
		<div class="ws-bigheadImg">
    		<img src="/downLoad/userImg/${userInfo.comId}/${userInfo.id}?sid=${param.sid}&size=1"></img>
		</div>
		<div class="ws-personalNews">
			<p>姓名：${userInfo.userName}</p>
			<p>部门：${userInfo.depName}</p>
			<p>移动电话：${userInfo.movePhone}</p>
			<a href="/userInfo/editUserInfoPage?sid=${param.sid}" class="ws-personalSet">个人设置</a>
		</div>
		
	</div>
	<section class="panel">
		<header class="panel-heading">
			其他信息						
			<span class="tools pull-right">
               <a href="javascript:;" class="fa fa-chevron-down"></a>
            </span>
		</header>
		<div class="panel-body">
			<div class="online-list"><i class="fa fa-volume-down"></i>待办任务<span class="ws-color">+3</span></div>
			<div class="online-list"><i class="fa fa-volume-down"></i>待办任务<span class="ws-color">+3</span></div>
			<div class="online-list"><i class="fa fa-volume-down"></i>待办任务<span class="ws-color">+3</span></div>						
		</div>
	</section>
</div>
<!--right sidebar end-->
</section>
</body>
</html>

