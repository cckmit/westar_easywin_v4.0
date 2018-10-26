<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
var sid = '${param.sid}'
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
$(document).ready(function(){
	if(!strIsNull($("#sharerJson").val())){
		var users = eval("("+$("#sharerJson").val()+")");
		  var img="";
		  for (var i=0, l=users.length; i<l; i++) {
				//数据保持
				img = img + "<div class=\"online-list margin-top-5 margin-left-5 margin-bottom-5\" style=\"float:left\">";
				img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}\" class=\"user-avatar\"/>"
				img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
				img = img + "</div>"
			}
			$("#crmSharor_div").html(img);
	}
	
	//取得闹铃集合信息
	ajaxListClockForOne('012','${customer.id}','${param.sid}',function(data){
		var listClocks = data.listClocks;
		if(listClocks && listClocks.length>0){
			$.each(listClocks,function(index,clockObj){
				var  clockHtml= conStrClockHtml('012','${customer.id}','${param.sid}',clockObj);
				$("#busClockList").append(clockHtml)
			})
		}
	});
});
//任务属性菜单切换
$(function(){
	//合并行
	olmFormRowSpan($("#linkManTable"))
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	//维护记录
	$("#customerFeedBackInfoMenuLi").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#customerFeedBackInfoMenuLi").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/crm/customerFeedBackInfoPage?sid=${param.sid}&pager.pageSize=10&customerId=${customer.id}");
	});
	//维护记录
	$("#headCrmFeedback").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$("#customerFeedBackInfoMenuLi").parent().find("li").removeAttr("class");
		$("#customerFeedBackInfoMenuLi").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/crm/customerFeedBackInfoPage?sid=${param.sid}&pager.pageSize=10&customerId=${customer.id}&talkFocus='1'");
	});
	//客户客户配置
	$("#customerBaseMenuLi").click(function(){
		$("#customerBase").css("display","block");
		$("#otherCustomerAttrIframe").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#customerBaseMenuLi").attr("class","active");
	});
	//移交记录
	$("#crmFlowRecordLi").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#crmFlowRecordLi").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/crm/crmFlowRecord?sid=${param.sid}&crmId=${customer.id}");
	});
	//客户日志
	$("#customerLogMenuLi").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#customerLogMenuLi").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${customer.id}&busType=012&ifreamName=otherCustomerAttrIframe");
	});
	//客户文档
	$("#customerUpfileMenuLi").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");;
		$("#customerUpfileMenuLi").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/crm/customerUpfilePage?sid=${param.sid}&pager.pageSize=10&customerId=${customer.id}&redirectPage="+encodeURIComponent(window.location.href));
	});
	//客户聊天室
	$("#crmChatLi").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#crmChatLi").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/chat/listBusChat?sid=${param.sid}&busId=${customer.id}&busType=012&ifreamTag=otherCustomerAttrIframe");
	});
	//客户项目
	$("#crmItem").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#crmItem").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/crm/crmItemListPage?sid=${param.sid}&pager.pageSize=10&partnerId=${customer.id}&redirectPage="+encodeURIComponent(window.location.href));
	});
	//客户任务
	$("#crmTask").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");;
		$("#crmTask").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/task/busModTaskList?sid=${param.sid}&pager.pageSize=10&ifreamName=otherCustomerAttrIframe&busId=${customer.id}&busType=012&redirectPage="+encodeURIComponent(window.location.href));
	});
	//客户查看记录
	$("#crmVireRecord").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");;
		$("#crmVireRecord").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/common/listViewRecord?sid=${param.sid}&busId=${customer.id}&busType=012&ifreamName=otherCustomerAttrIframe");
	});
});
//合并行数据
function olmFormRowSpan(table){
	var trs = $(table).find("tbody").find("tr");
	if(trs && trs.get(0)){
		var preOlmId = 0;
		var rowSpan = 1;
		$.each(trs,function(index,trObj){
			var olmId = $(trObj).attr("olmId");
			if(olmId == preOlmId){
				rowSpan = rowSpan+1;
				//开始合并
				var firstTr = $(table).find("tbody").find("tr[olmId='"+olmId+"']:eq(0)");
				$(firstTr).find("td:eq(0)").attr("rowspan",rowSpan);
				$(firstTr).find("td:eq(1)").attr("rowspan",rowSpan);
				
				var td0 = $(trObj).find("td:eq(0)");
				var td1 = $(trObj).find("td:eq(1)");
				
				$(td0).remove();
				$(td1).remove();
			}else{
				preOlmId = olmId;
				rowSpan = 1;
			}
		});
		
	}
}
</script>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                	
                		<a href="javascript:void(0)" class="widget-caption blue padding-right-5"
               				attentionState="${customer.attentionState}" busType="012" busId="${customer.id}" describe="0" iconSize="sizeMd"
               				onclick="changeAtten('${param.sid}',this)" title="${customer.attentionState==0?"关注":"取消"}">
							<i class="fa ${customer.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>
						</a>
						
                        <span class="widget-caption themeprimary" style="font-size: 20px">客户</span>
                        <span class="widget-caption themeprimary" style="font-size: 15px;margin-top: 2px">
                        	<c:choose>
                        		<c:when test="${fn:length(customer.customerName)>20 }">
		                        	--${fn:substring(customer.customerName,0,20)}..
                        		</c:when>
                        		<c:otherwise>
                        			--${customer.customerName}
                        		</c:otherwise>
                        	</c:choose>
                        </span>
                        <div class="widget-buttons ps-toolsBtn">
                        	
                        
							<a href="javascript:void(0)" class="purple" id="headCrmFeedback" title="反馈">
								<i class="fa fa-comments"></i>留言
							</a>
							<a class="green ps-point margin-right-0" data-toggle="dropdown" title="更多操作">
	                                  <i class="fa fa-th"></i>更多
	                              	</a>
                             
                              <!--Notification Dropdown-->
                              <ul class="pull-right dropdown-menu dropdown-arrow ps-layerUl">
                                  <li>
                                      <a href="javascript:void(0)" onclick="addClock(${customer.id},'012','${param.sid}')">
                                          <div class="clearfix">
                                           	 <i class="fa fa-clock-o"></i>
                                             <span class="title ps-topmargin">定时提醒</span>
                                             <span class="title ps-topmargin blue fa fa-plus padding-left-30"></span>
                                          </div>
                                      </a>
                                      <ul id="busClockList">
                                      </ul>
                                  </li>
                              </ul>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->        
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                           <div class="widget-body no-shadow">
                           		<div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat" id="myTab11" style="padding-top:0;">
                                             <li class="active" id="customerFeedBackInfoMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">客户留言</a>
                                             </li>
                                             <li id="customerBaseMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">客户详情</a>
                                             </li>
                                             <li id="crmItem">
                                                 <a data-toggle="tab" href="javascript:void(0)">关联项目<c:if test="${customer.itemNum > 0}"><span style="color:red;font-weight:bold;">（${customer.itemNum}）</span></c:if></a>
                                             </li>
                                             <li id="crmTask">
                                                 <a data-toggle="tab" href="javascript:void(0)">关联任务<c:if test="${customer.taskNum > 0}"><span style="color:red;font-weight:bold;">（${customer.taskNum}）</span></c:if></a>
                                             </li>
                                             <li id="customerLogMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">操作日志</a>
                                             </li>
                                             <li id="customerUpfileMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">客户文档<c:if test="${customer.fileNum > 0}"><span style="color:red;font-weight:bold;">（${customer.fileNum}）</span></c:if></a>
                                             </li>

                                             <li id="crmVireRecord">
                                                 <a data-toggle="tab" href="javascript:void(0)">阅读情况</a>
                                             </li>

                                             <%--<li id="crmVireRecord">--%>
                                                 <%--<a data-toggle="tab" href="javascript:void(0)">最近查看</a>--%>
                                             <%--</li>--%>

                                             <!-- <li id="crmFlowRecordLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">移交记录</a>
                                             </li> -->
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 	<div id="customerBase" style="display:none;">
												<jsp:include page="./customerBase_view.jsp"></jsp:include>
											</div>
                                    	 	<iframe id="otherCustomerAttrIframe" style="display:block;" class="layui-layer-load"
												src="/crm/customerFeedBackInfoPage?sid=${param.sid}&pager.pageSize=10&customerId=${customer.id}"
												border="0" frameborder="0" allowTransparency="true"
												noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
                                    	 </div>
                                	</div>
                            	</div>
                            </div> 
                        </div>
					
					</div>
				</div>
			</div>
		</div>
	</form>
    <script src="/static/assets/js/bootstrap.min.js"></script>
    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
