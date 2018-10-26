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
<style>
.modList input{
padding-left: 5px
}
#modTypeId input{
margin-left: 15px
}
</style>
<script type="text/javascript">
$(function(){
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
function reSearch(){
	msgshareFrom.loadData(0)
}
</script>
	<body>
		<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
				<div id="searchForm" class="searchCond" style="display: block">
					<div class="btn-group pull-left">
						<div class="table-toolbar ps-margin">
							<div class="btn-group" id="modTypeDiv">
								<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMenu('modTypeA')"id="modTypeA">
									<c:choose>
							 			<c:when test="${not empty modList }">
							 			<font style="font-weight:bold;">
								 			<c:forEach items="${modList}" var="modTypeObj" varStatus="vs">
								 			<c:if test="${modTypeObj=='003'}">任务模块</c:if>
								 			<c:if test="${modTypeObj=='004'}">投票模块</c:if>
								 			<c:if test="${modTypeObj=='005'}">项目模块</c:if>
								 			<c:if test="${modTypeObj=='006'}">周报模块</c:if>
											<c:if test="${modTypeObj=='050'}">分享模块</c:if>
								 			<c:if test="${modTypeObj=='008'}">个人模块</c:if>
								 			<c:if test="${modTypeObj=='009'}">企业模块</c:if>
								 			<c:if test="${modTypeObj=='011'}">问答模块</c:if>
								 			<c:if test="${modTypeObj=='012'}">客户模块</c:if>
								 			<c:if test="${modTypeObj=='013'}">文档模块</c:if>
								 			<c:if test="${modTypeObj=='100' || modTypeObj=='1'}">分享模块</c:if>
								 			<c:if test="${!vs.last}">,</c:if>
								 			<c:if test="${vs.count mod 3 eq 0 && not vs.last }"><br></c:if>
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
									<li><a href="javascript:void(0);" onfocus="this.blur();">全部模块<label class="margin-left-5"><input type="checkbox" id="allModType" ${fn:length(modList) eq 4?'checked':''}><span class="text"></span></label></a></li>
									<!-- 
									<li><a href="javascript:void(0);" onfocus="this.blur();">任务模块<label class="margin-left-5"><input type="checkbox" name="modTypes" value="003" ${fn:contains(modList,'003')?'checked prechecked="1"':''}><span class="text"></span></label></a></li>
									<li><a href="javascript:void(0);" onfocus="this.blur();">项目模块<label class="margin-left-5"><input type="checkbox" name="modTypes" value="005" ${fn:contains(modList,'005')?'checked prechecked="1"':''}><span class="text"></span></label></a></li>
									<li><a href="javascript:void(0);" onfocus="this.blur();">周报模块<label class="margin-left-5"><input type="checkbox" name="modTypes" value="006" ${fn:contains(modList,'006')?'checked prechecked="1"':''}><span class="text"></span></label></a></li>
									<li><a href="javascript:void(0);" onfocus="this.blur();">个人模块<label class="margin-left-5"><input type="checkbox" name="modTypes" value="008" ${fn:contains(modList,'008')?'checked prechecked="1"':''}><span class="text"></span></label></a></li>
									<li><a href="javascript:void(0);" onfocus="this.blur();">企业模块<label class="margin-left-5"><input type="checkbox" name="modTypes" value="009" ${fn:contains(modList,'009')?'checked prechecked="1"':''}><span class="text"></span></label></a></li>
									<li><a href="javascript:void(0);" onfocus="this.blur();">客户模块<label class="margin-left-5"><input type="checkbox" name="modTypes" value="012" ${fn:contains(modList,'012')?'checked prechecked="1"':''}><span class="text"></span></label></a></li>
									 -->
									<li><a href="javascript:void(0);" onfocus="this.blur();">投票模块<label class="margin-left-5"><input type="checkbox" name="modTypes" value="004" ${fn:contains(modList,'004')?'checked prechecked="1"':''}><span class="text"></span></label></a></li>
									<li><a href="javascript:void(0);" onfocus="this.blur();">问答模块<label class="margin-left-5"><input type="checkbox" name="modTypes" value="011" ${fn:contains(modList,'011')?'checked prechecked="1"':''}><span class="text"></span></label></a></li>
									<li><a href="javascript:void(0);" onfocus="this.blur();">文档模块<label class="margin-left-5"><input type="checkbox" name="modTypes" value="013" ${fn:contains(modList,'013')?'checked prechecked="1"':''}><span class="text"></span></label></a></li>
									<li><a href="javascript:void(0);" onfocus="this.blur();">分享模块<label class="margin-left-5"><input type="checkbox" name="modTypes" value="100" ${fn:contains(modList,'100')?'checked prechecked="1"':''}><span class="text"></span></label></a></li>
                                 </ul>
                             </div>
						</div>
						<div class="table-toolbar ps-margin">
							<div class="btn-group">
								<input type="hidden" name="creatorName" id="creatorName"
									value="${msgShare.creatorName}">
								<input type="hidden" name="creator" id="creator"
									value="${msgShare.creator}">
								<a class="btn btn-default dropdown-toggle btn-xs"
									data-toggle="dropdown"> <c:choose>
										<c:when test="${not empty msgShare.creatorName}">
											<font style="font-weight: bold;">${msgShare.creatorName}</font>
										</c:when>
										<c:otherwise>分享人筛选</c:otherwise>
									</c:choose> <i class="fa fa-angle-down"></i> </a>
								<ul class="dropdown-menu dropdown-default">
									<li>
										<a href="javascript:void(0);"
											onclick="userOneForUserIdCallBack('',
											'creator','','creatorName')">不限条件</a>
									</li>
									<li>
										<a href="javascript:void(0);"
											onclick="userOneForUserId('${userInfo.id}',
											'${userInfo.userName}','','${param.sid}','creator','creatorName');">人员选择</a>
									</li>
									<c:choose>
										<c:when test="${not empty listOwners}">
											<hr style="margin: 8px 0px" />
											<c:forEach items="${listOwners}" var="owner" varStatus="vs">
												<li>
													<a href="javascript:void(0)"
														onclick="userOneForUserIdCallBack(
														'${owner.id}','creator','${owner.userName}','creatorName');">
														${owner.userName}</a>
												</li>
											</c:forEach>
										</c:when>
									</c:choose>
								</ul>
							</div>
						</div>
					</div>
					<div class="ps-margin ps-search">
						<span class="input-icon"> 
							<input id="content" name="content" value="${msgShare.content}"
								class="form-control ps-input" type="text" placeholder="请输入关键字">
							<a href="javascript:void(0)" class="ps-searchBtn">
								<i class="glyphicon glyphicon-search circular danger"></i> 
							</a> 
						</span>
					</div>
				</div>
		</div>
		<div class="ps-clear"></div>
		<div class="widget-body" id="baseDiv">
			<input type="hidden" id="minId" value="0">
			<div class="main" style="min-height: 150px">
			  <div class="history" id="msgsharelist" style="padding-top: 90px">
			  </div>
			</div>
			<div style="text-align: center;font-size: 18px;border-top: 1px solid #ccc;">
				<a href="javascript:void(0)" id="moreMsg">加载更多</a>
			</div>
		</div>
			
	</body>
</html>

