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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>
<script type="text/javascript">
var sid="${param.sid}";
function viewMod(busType,busId){
	if("003"==busType){
		authCheck(busId,"003",-1,function(authState){
			var url = "/task/viewTask?sid=${param.sid}&id="+busId;
			var height = $(window.parent).height();
			openWinWithPams(url,"800px",(height-90)+"px");
		})
	}else if("005"==busType){
		authCheck(busId,"005",-1,function(authState){
			var url = "/item/viewItemPage?sid=${param.sid}&id="+busId;
			var height = $(window.parent).height();
			openWinWithPams(url,"800px",(height-90)+"px");
		})
	}
}

//删除客户附件
function delCrmFile(ts,crmId,crmUpFileId,type){
	window.top.layer.confirm("确定删除附件?", {icon: 3, title:'确认对话框'}, function(index){
		window.top.layer.close(index);
		$.post("/crm/delCrmUpfile?sid=${param.sid}",{Action:"post",crmId:crmId,crmUpFileId:crmUpFileId,type:type},     
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
</script>
</head>
<body onload="resizeVoteH('otherCustomerAttrIframe');" style="background-color:#FFFFFF;">
	<form action="/crm/customerUpfilePage" id="searchForm" class="subform">	
		<input type="hidden" name="pager.pageSize" value="10">
		<input type="hidden" name="sid" value="${param.sid}"> 
		<input type="hidden" name="customerId" value="${param.customerId}">
		<input type="hidden" name="order"  value="${customerUpfile.order}">
	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary" style="border-bottom: 0">
		<div class="table-toolbar ps-margin">
			<div class="btn-group">
	            <a class="btn btn-default dropdown-toggle btn-xs"
	               data-toggle="dropdown"> <c:choose>
	                <c:when test="${not empty customerUpfile.order}">
	                        <font style="font-weight:bold;">
	                            <c:choose>
	                                <c:when test="${customerUpfile.order=='fileExt'}">按文档类型</c:when>
	                            	<c:when test="${customerUpfile.order=='userId'}">按上传人</c:when>
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
					<input name="filename" id="filename" value="${customerUpfile.filename}" class="form-control ps-input formElementSearch" type="text" placeholder="请输入关键词" >
											<a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
				</span>
			</div>
		</div>
	</div>
 </form>
	<table class="table table-striped table-hover general-table fixTable">
     	<thead>
        	<tr>
                <th width="5%" valign="middle">&nbsp;</th>
                <th valign="middle">附件名称</th>
				<th valign="middle" class="hidden-phone">附件来源</th>
				<th width="13%" valign="middle" style="text-align: center;">上传时间</th>
				<th width="15%" valign="middle">上传人</th>
				<th width="13%" valign="middle">操作</th>
            </tr>
       </thead>
       <tbody>
       	<c:choose>
	 	<c:when test="${not empty listCustomerUpfile}">
	 		<c:forEach items="${listCustomerUpfile}" var="file" varStatus="status">
	 			<tr>
	 				<td height="40">${ status.count}</td>
	 				<td align="left" title="${ file.filename}">${file.filename}</td>
	 				<td align="left" title="${file.busName}">
	 					<c:choose>
	 						<c:when test="${file.type=='012'}">[客户]${file.busName}</c:when>
	 						<c:when test="${file.type=='005'}">
	 							[项目]<a href="javascript:void(0)" onclick="viewMod('${file.type}',${file.busId})">${file.busName}</a>
	 						</c:when>
	 						<c:when test="${file.type=='003'}">
	 							[任务]<a href="javascript:void(0)" onclick="viewMod('${file.type}',${file.busId})">${file.busName}</a>
	 						</c:when>
	 					</c:choose>
	 				</td>
	 				<td>${fn:substring(file.recordCreateTime,0,10)}</td>
	 				<td style="text-align: left;">
	 					<div class="ticket-user pull-left other-user-box">
							<img src="/downLoad/userImg/${file.comId}/${file.userId}?sid=${param.sid}"
								title="${file.creatorName}" class="user-avatar" />
							<span class="user-name">${file.creatorName}</span>
						</div>
	 				</td>
	 				<td height="30" align="center">
		 				<c:choose>
		 					<c:when test="${file.fileExt=='doc' || file.fileExt=='docx' || file.fileExt=='xls' || file.fileExt=='xlsx' || file.fileExt=='ppt' || file.fileExt=='pptx'}">
				 				<a class="fa fa-download" href="javascript:void(0);" title="下载" onclick="downLoad('${file.uuid}','${file.filename}','${param.sid }')"></a>
				 				<a style="margin-left:10px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${file.upfileId}','${file.uuid}','${file.filename}','${file.fileExt}','${param.sid}','${file.type}','${file.busId}')"></a>
		 					</c:when>
		 					<c:when test="${file.fileExt=='txt'  || file.fileExt=='pdf'}">
				 				<a class="fa fa-download" href="/downLoad/down/${file.uuid}/${file.filename}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:10px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${file.upfileId}','${file.uuid}','${file.filename}','${file.fileExt}','${param.sid}','${file.type}','${file.busId}')"></a>
		 					</c:when>
		 					<c:when test="${file.fileExt=='jpg'||file.fileExt=='bmp'||file.fileExt=='gif'||file.fileExt=='jpeg'||file.fileExt=='png'}">
				 				<a class="fa fa-download" href="/downLoad/down/${file.uuid}/${file.filename}?sid=${param.sid}" title="下载"></a>
				 				<a style="margin-left:10px;" class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic('/downLoad/down/${file.uuid}/${file.filename}','${param.sid}','${file.upfileId}','${file.type}','${file.busId}')"></a>
		 					</c:when>
		 					<c:otherwise>
				 				<a class="fa fa-download" onclick="downLoad('${file.uuid}','${file.filename}','${param.sid }')" title="下载"></a>
		 					</c:otherwise>
		 				</c:choose>
		 				<c:if test="${empty delete && file.userId==userInfo.id}">
				 			<a style="margin-left:10px;" class="fa  fa-times-circle" href="javascript:void(0);" title="删除" onclick="delCrmFile(this,${file.customerId},${file.id},'${file.upType}')"></a>
		 				</c:if>
		 			</td>
	 			</tr>
	 		</c:forEach>
	 	</c:when>
	 	<c:otherwise>
	 		<tr>
	 			<td height="30" colspan="6" align="center"><h3>没有相关信息！</h3></td>
	 		</tr>
	 	</c:otherwise>
	 </c:choose>
       </tbody>
      </table>
<tags:pageBar url="/crm/customerUpfilePage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
