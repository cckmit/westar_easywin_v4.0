<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>

<script type="text/javascript">
var sid="${param.sid}";
//附件数据源查看
function viewSource(key,type){
	if("task"==type){
		authCheck(key,"003",-1,function(authState){
			var url = "/task/viewTask?sid=${param.sid}&id="+key;
			var height = $(window.parent).height();
			openWinWithPams(url,"800px",(height-90)+"px");
		})
	}else if("item"==type){
		authCheck(key,"005",-1,function(authState){
			var url = "/item/viewItemPage?sid=${param.sid}&id="+key;
			var height = $(window.parent).height();
			openWinWithPams(url,"800px",(height-90)+"px");
		})
	}else if("crm"==type || "crmTalk"==type){
		authCheck(key,"012",-1,function(authState){
			var url = "/crm/viewCustomer?sid=${param.sid}&id="+key;
			var height = $(window.parent).height();
			openWinWithPams(url,"800px",(height-90)+"px");
		})
	}else if("demand"==type || "demandTalk"==type){
		var url = "/demand/viewDemandPage?sid=${param.sid}&demandId="+key;
		var height = $(window.parent).height();
		openWinWithPams(url,"800px",(height-90)+"px");
	}else if("product"==type || "productTalk"==type){
		var url = "/product/viewProPage?sid=${param.sid}&id="+key;
		var height = $(window.parent).height();
		openWinWithPams(url,"800px",(height-90)+"px");
	}
}
</script>
<script> 
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			}
		}); 
		//初始化名片
		initCard('${param.sid}');
		 //页面刷新
		$("#refreshImg").click(function(){
			window.self.location.reload();
		});
		 
		$("#filename").blur(function(){
			//启动加载页面效果
			layer.load(0, {shade: [0.6,'#fff']});
			$("#searchForm").submit();
		});
		//文本框绑定回车提交事件
		$("#filename").bind("keydown",function(event){
	        if(event.keyCode == "13")    
	        {
	        	if(!strIsNull($("#filename").val())){
	        		//启动加载页面效果
	        		layer.load(0, {shade: [0.6,'#fff']});
					$("#searchForm").submit();
	        	}else{
	        		showNotification(1,"请输入检索内容！");
	    			$("#filename").focus();
	        	}
	        }
	    });
		 
	}); 
	//文件名筛选
	
	//删除项目附件
	function delItemFile(ts,itemId,itemUpFileId,type){
		window.top.layer.confirm("确定删除附件?", {icon: 3, title:'确认对话框'}, function(index){
			window.top.layer.close(index);
			$.post("/item/delItemUpfile?sid=${param.sid}",{Action:"post",itemId:itemId,itemUpFileId:itemUpFileId,type:type},     
					function (data){
					if(data.status=='y'){
						showNotification(1,"操作成功");
						$(ts).parents("tr").remove();
						$.each($(".fileOrder"),function(index,item){
							$(this).html(index+1)
						})
					}else{
						showNotification(2,data.info);
						
					}
			},"json");
		});
		
		
	}
	
</script>
<style type="text/css">
.table tbody>tr>td{
	padding: 5px 0px;
}
</style>
</head>
<body onload="resizeVoteH('otherItemAttrIframe')" style="background-color:#FFFFFF;">
<form action="/item/itemUpfilePage" id="searchForm" class="subform">	
		<input type="hidden" name="pager.pageSize" value="10">
		<input type="hidden" name="sid" value="${param.sid}"> 
		<input type="hidden" name="itemId" value="${param.itemId}">
		<input type="hidden" name="order"  value="${itemUpfile.order}">
		<input type="hidden" name="type"  value="${itemUpfile.type}">
	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary" style="border-bottom: 0">
		<div class="table-toolbar ps-margin">
			<div class="btn-group">
	            <a class="btn btn-default dropdown-toggle btn-xs"
	               data-toggle="dropdown"> <c:choose>
	                <c:when test="${not empty itemUpfile.order}">
	                        <font style="font-weight:bold;">
	                            <c:choose>
	                                <c:when test="${itemUpfile.order=='fileExt'}">按文档类型</c:when>
	                                <c:when test="${itemUpfile.order=='userId'}">按上传人</c:when>
	                            </c:choose>
	                        </font>
	                    </c:when>
	                    <c:otherwise>排序</c:otherwise>
	                </c:choose> <i class="fa fa-angle-down"></i>
	                </a>
	                <ul class="dropdown-menu dropdown-default">
	                    <li><a href="javascript:void(0)" class="clearThisElement" relateElement="order">不限条件</a>
	                    </li>
	                    <li><a href="javascript:void(0)" class="setElementValue" relateElement="order" dataValue="fileExt">按文档类型</a>
	                    </li>
	                     <li><a href="javascript:void(0)" class="setElementValue" relateElement="order" dataValue="userId">按上传人</a>
	                    </li>
	                </ul>
	           	</div>
			</div>
		<div>
			<div class="ps-margin ps-search">
				<span class="input-icon">
					<input name="filename" id="filename" value="${itemUpfile.filename}" class="form-control ps-input formElementSearch" type="text" placeholder="请输入关键词" >
											<a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
				</span>
			</div>
		</div>
	</div>
 </form>
<table class="table table-striped table-hover general-table fixTable">
     	<thead>
        	<tr>
                <th width="5%" align="center">&nbsp;</th>
                <th width="30%" align="center">附件名称</th>
				<th width="25%" align="center">来源</th>
				<th style="width:120px;" align="center">上传时间</th>
				<th style="width:100px;" align="center">上传人</th>
				<th width="10%" align="center">操作</th>
            </tr>
       </thead>
  
       <tbody>
       	<c:choose>
	 	<c:when test="${not empty listItemUpfile}">
	 		<c:forEach items="${listItemUpfile}" var="file" varStatus="status">
	 			<tr>
	 				<td>${ status.count}</td>
	 				<td title="${file.filename}">${ file.filename}</td>
	 				<td title="${file.sourceName}">
	 					<c:choose>
	 						<c:when test="${file.type=='task' || file.type=='taskTalk'}">【任务】</c:when>
	 						<c:when test="${file.type=='file'}">【附件】</c:when>
	 						<c:when test="${file.type=='itemTalk'}">【留言】</c:when>
	 						<c:when test="${file.type=='crm' || file.type=='crmTalk'}">【客户】</c:when>
	 						<c:when test="${file.type=='demand' || file.type=='demandTalk'}">【需求】</c:when>
	 						<c:when test="${file.type=='product' || file.type=='productTalk'}">【产品】</c:when>
	 						<c:otherwise>【项目】</c:otherwise>
	 					</c:choose>
	 					<c:choose>
	 						<c:when test="${file.type eq 'task' or file.type eq 'item'or file.type eq 'crm'or file.type eq 'crmTalk'or file.type eq 'demand'or file.type eq 'demandTalk'or file.type eq 'product'or file.type eq 'productTalk'}">
			 					<a href="javascript:void(0);" onclick="viewSource(${file.key},'${file.type}');" style="color:blue;">
									${file.sourceName}
								</a>
	 						</c:when>
	 						<c:otherwise>
	 							<font color="#757575">${file.sourceName}</font>
	 						</c:otherwise>
	 					</c:choose>
	 				</td>
	 				<td>${fn:substring(file.recordCreateTime,0,10)}</td>
	 				<td style="text-align: left;">
	 					<div class="ticket-user pull-left other-user-box">
							<img src="/downLoad/userImg/${userInfo.comId}/${file.userId}"
								title="${file.creatorName}" class="user-avatar" />
							<span class="user-name">${file.creatorName}</span>
						</div>
	 				</td>
	 				<c:choose>
	 					<c:when test="${itemUpfileVo.type=='task'}">
			 				<c:set var="busType" value="003"></c:set>
			 				<c:set var="busId" value="${itemUpfileVo.key}"></c:set>
	 					</c:when>
	 					<c:otherwise>
			 				<c:set var="busType" value="005"></c:set>
			 				<c:set var="busId" value="${itemUpfile.itemId}"></c:set>
	 					</c:otherwise>
	 				</c:choose>
	 				<td height="30" align="center">
		 				<c:choose>
		 					<c:when test="${file.fileExt=='doc' || file.fileExt=='docx' || file.fileExt=='xls' || file.fileExt=='xlsx' || file.fileExt=='ppt' || file.fileExt=='pptx'}">
				 				<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${file.uuid}','${file.filename}','${param.sid }')"></a>
				 				<a style="margin-left:10px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${file.upfileId}','${file.uuid}','${file.filename}','${file.fileExt}','${param.sid}','${busType}','${busId}')"></a>
		 					</c:when>
		 					<c:when test="${file.fileExt=='txt'  || file.fileExt=='pdf'}">
				 				<a class="fa fa-download" href="/downLoad/down/${file.uuid}/${file.filename}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:10px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${file.upfileId}','${file.uuid}','${file.filename}','${file.fileExt}','${param.sid}','${busType}','${busId}')"></a>
		 					</c:when>
		 					<c:when test="${file.fileExt=='jpg'||file.fileExt=='bmp'||file.fileExt=='gif'||file.fileExt=='jpeg'||file.fileExt=='png'}">
				 				<a class="fa fa-download" href="/downLoad/down/${file.uuid}/${file.filename}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:10px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${file.uuid}/${file.filename}','${param.sid}','${file.upfileId}','${busType}','${busId}')"></a>
		 					</c:when>
		 					<c:otherwise>
				 				<a class="fa fa-download" onclick="downLoad('${file.uuid}','${file.filename}','${param.sid }')" title="下载"></a>
		 					</c:otherwise>
		 				</c:choose>
		 				<c:if test="${empty delete && file.userId==userInfo.id}">
				 			<a style="margin-left:10px;" class="fa  fa-times-circle" href="javascript:void(0);" title="删除" onclick="delItemFile(this,${file.key},${file.id},'${file.type}')"></a>
		 				</c:if>
		 			</td>
	 			</tr>
	 		</c:forEach>
	 	</c:when>
	 	<c:otherwise>
	 		<tr>
	 			<td height="30" colspan="7" align="center"><h3>没有相关信息！</h3></td>
	 		</tr>
	 	</c:otherwise>
	 </c:choose>
       </tbody>
      </table>
<tags:pageBar url="/item/itemUpfilePage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>