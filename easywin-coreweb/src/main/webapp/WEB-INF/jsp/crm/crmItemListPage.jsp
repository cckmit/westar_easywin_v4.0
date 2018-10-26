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

<script type="text/javascript">
var sid = "${param.sid}"
//项目查看权限验证
function viewItem(itemId){
	authCheck(itemId,"005",-1,function(authState){
		var url = "/item/viewItemPage?sid=${param.sid}&id="+itemId;
		var height = $(window.parent).height();
		openWinWithPams(url,"800px",(height-90)+"px");
	})
}
</script>
</head>
<body onload="resizeVoteH('otherCustomerAttrIframe');" style="background-color:#FFFFFF;">
	<div class="widget-body">
    	<table class="table table-striped table-hover general-table">
         	<thead>
            	<tr>
                    <th width="8%" valign="middle">序号</th>
					<th valign="middle" class="hidden-phone">项目名称</th>
					<th width="14%" valign="middle">项目进度</th>
					<th width="15%" valign="middle">责任人</th>
					<th width="14%" valign="middle">创建时间</th>
                </tr>
           </thead>
           <tbody>
           	<c:choose>
			 	<c:when test="${not empty itemList}">
			 		<c:forEach items="${itemList}" var="item" varStatus="status">
			           	<tr>
			            	<td>
			            		<%-- <a href="javascript:void(0)" class="fa ${item.attentionState==0?'fa-star-o':'fa-star ws-star'}" 
			            		title="${item.attentionState==0?'标识关注':'取消关注'}" 
			            		onclick="changeAtten('005',${item.id},${item.attentionState},'${param.sid}',this)"></a> --%>
			            		<label class="optRowNum" style="display: block;width: 20px">${status.count}</label>
			            	</td>
			                <td class="hidden-phone">
				                <a href="javascript:void(0)" onclick="viewItem(${item.id});" title="${item.itemName}">
			 						<tags:cutString num="24">${item.itemName}</tags:cutString>
			 					</a>
			                </td>
			                <td>
			                <c:choose>
		 						<c:when test="${item.state==3}">已挂起</c:when>
			 						<c:when test="${item.state==4}"><font style="color:#FF0000;">已完成</font></c:when>
			 						<c:otherwise>
			 							<c:choose>
			 								<c:when test="${empty item.itemProgress}">
			 									0%
			 								</c:when>
			 								<c:otherwise>
					 							<tags:viewDataDic type="progress" code="${item.itemProgress}"></tags:viewDataDic>
			 								</c:otherwise>
			 							</c:choose>
			 						</c:otherwise>
			 					</c:choose>
			                </td>
			                <td>
			                	<c:choose>
			 						<c:when test="${item.state==3 || item.state==4}">
			 							--
			 						</c:when>
			 						<c:otherwise>
					                	<div class="ticket-user pull-left other-user-box">
											<img src="/downLoad/userImg/${item.comId}/${item.owner}?sid=${param.sid}"
												title="${item.ownerName}" class="user-avatar"/>
											<span class="user-name">${item.ownerName}</span>
										</div>
			 						</c:otherwise>
			 					</c:choose>
			                 </td>
			               	 <td>${fn:substring(item.recordCreateTime,0,10)}</td>
			             </tr>
			 		</c:forEach>
			 	</c:when>
			 	<c:otherwise>
			 		<tr>
			 			<td height="30" colspan="6" align="center"><h3>没有关联项目！</h3></td>
			 		</tr>
			 	</c:otherwise>
			 </c:choose>
        	</tbody>
    	</table>
	</div>
<tags:pageBar url="/crm/crmItemListPage"></tags:pageBar>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
