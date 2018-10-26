<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<title><%=SystemStrConstant.TITLE_NAME %></title>

<script type="text/javascript">

var regN = /^\+?[1-9][0-9]*$/
		
function resortNo(){
	var index = 0;
	$("#contentBody").find("p").each(function(){
		var html =$(this).html();
		html = html.replace(/<br\s*\/?>/g, "");
		var indexNo = html.indexOf("、");
		if(indexNo>0){
			var order = html.substring(0,indexNo);
			order = order.replace(/&nbsp;/ig,'').replace(" ","");
			if(regN.test(order)){
				var head = html.substring(0,indexNo+1);
				html = html.replace(head,'');
			}
		}
		if(html.length==0){
			$(this).remove();
			return;
		}
		html = (index+1)+"、"+html;
		$(this).html(html)
		index = index+1;
		
	});
}
function reSearch(){
	$("#searchForm").submit();
}
$(function(){
	
	//设置滚动条高度
	$("#contentBody").css("height","350px");
	
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
			reSearch();
		}else{
			$("#modTypeA").parent().removeClass("open");
		}
	});
})
</script>
<style>
#contentBody p{
font-size: 15px !important;
}
.modList input{
padding-left: 5px
}
#modTypeId input{
margin-left: 15px
}
</style>
</head>
<body>
<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <div class="widget-caption themeprimary" style="font-size: 20px">
                        	<form action="/systemLog/exportWorkTrace" id="searchForm">
								<input type="hidden" name="sid" value="${param.sid }">
								<input type="hidden" name="businessType" value="${systemLog.businessType}">
								<div class="btn-group pull-left">
									<div class="table-toolbar ps-margin">
                                       	<div class="btn-group" id="modTypeDiv">
											<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMenu('modTypeA')"id="modTypeA">
												<c:choose>
										 			<c:when test="${not empty modList }">
										 			<font style="font-weight:bold;">
											 			<c:forEach items="${modList}" var="modTypeObj" varStatus="vs" >
											 			<c:if test="${modTypeObj=='003'}">任务模块</c:if>
											 			<c:if test="${modTypeObj=='004'}">投票模块</c:if>
											 			<c:if test="${modTypeObj=='005'}">项目模块</c:if>
											 			<c:if test="${modTypeObj=='011'}">问答模块</c:if>
											 			<c:if test="${modTypeObj=='012'}">客户模块</c:if>
											 			<c:if test="${modTypeObj=='100'}">分享模块</c:if>
											 			<c:if test="${!vs.last}">,</c:if>
											 			</c:forEach>
											 			</font>
										 			</c:when>
										 			<c:otherwise>
										 			全部模块
										 			</c:otherwise>
										 		</c:choose>
												<i class="fa fa-angle-down"></i>
											</a>
											 <ul class="dropdown-menu dropdown-default" id="modTypeId">
												 <li>
												 	<a href="javascript:void(0);" onfocus="this.blur();">全部模块
												 		<label>
													 		<input type="checkbox" id="allModType" ${fn:length(modList) eq 6?'checked':''}>
													 		<span class="text"></span>
												 		</label>
												 	</a>
												 </li>
													<li>
														<a href="javascript:void(0);" onfocus="this.blur();">任务模块
															<lable>
																<input type="checkbox" name="modTypes" value="003" ${fn:contains(modList,'003')?'checked  prechecked="1"':''}>
																<span class="text"></span>
															</lable>
														</a>
													</li>
													<li>
														<a href="javascript:void(0);" onfocus="this.blur();">投票模块
															<lable>
																<input type="checkbox" name="modTypes" value="004" ${fn:contains(modList,'004')?'checked  prechecked="1"':''}>
																<span class="text"></span>
															</lable>
														</a>
													</li>
													<li>
														<a href="javascript:void(0);" onfocus="this.blur();">项目模块
															<lable>
																<input type="checkbox" name="modTypes" value="005" ${fn:contains(modList,'005')?'checked  prechecked="1"':''}>
																<span class="text"></span>
															</lable>
														</a>
													</li>
													<li>
														<a href="javascript:void(0);" onfocus="this.blur();">问答模块
															<lable>
																<input type="checkbox" name="modTypes" value="011" ${fn:contains(modList,'011')?'checked  prechecked="1"':''}>
																<span class="text"></span>
															</lable>
														</a>
													</li>
													<li>
														<a href="javascript:void(0);" onfocus="this.blur();">客户模块
															<lable>
																<input type="checkbox" name="modTypes" value="012" ${fn:contains(modList,'012')?'checked  prechecked="1"':''}>
																<span class="text"></span>
															</lable>
														</a>
													</li>
													<li>
														<a href="javascript:void(0);" onfocus="this.blur();">分享模块
															<lable>
																<input type="checkbox" name="modTypes" value="100" ${fn:contains(modList,'100')?'checked  prechecked="1"':''}>
																<span class="text"></span>
															</lable>
														</a>
													</li>
	                                            </ul>
	                                        </div>
                                   		</div>
	                                    <div class="table-toolbar ps-margin padding-top-5 padding-bottom-5">
	                                        	<input class="form-control dpd2 no-padding" type="text" readonly="readonly" value="${systemLog.startDate}" id="startDate" name="startDate" style="width:100px;display: inline;padding:0px 5px;height: 25px"
													placeholder="开始时间"  onFocus="WdatePicker({onpicked:function(){reSearch()},oncleared: function(){reSearch()},dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
													<span>到</span>
													<input class="form-control dpd2 no-padding" type="text" readonly="readonly" value="${systemLog.endDate}" id="endDate"  name="endDate" style="width:100px;display: inline;padding:0px 5px;height: 25px"
													placeholder="结束时间" onFocus="WdatePicker({onpicked:function(){reSearch()},oncleared: function(){reSearch()},dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})"/>
	                                    </div>
	                                    
									</div>
							</form>
                        </div>
                         <div class="widget-buttons">
                         	<a href="javascript:void(0)" onclick="reSearch()">刷新</a>
							<a href="javascript:void(0)" onclick="resortNo()">重新排序</a>
                         </div>
                        
                     </div><!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 --> 
                      <div class="widget-body margin-top-50 no-padding-right no-padding-top" id="contentBody" style="overflow: hidden;overflow-y:scroll" contenteditable="true">
							<c:choose>
								<c:when test="${not empty list}">
									<c:forEach items="${list}" var="obj" varStatus="vs">
										<p>${vs.count}、${obj.content}</p>
									</c:forEach>
								</c:when>
							</c:choose>
					</div>
				</div>
			</div>
		</div>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>



</body>
</html>

