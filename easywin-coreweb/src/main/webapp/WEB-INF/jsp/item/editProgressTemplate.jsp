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
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
$(function() {
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsgV2(msg, o, cssctl);
		},
		datatype:{
			
		},
		callback:function (form){
			//提交前验证是否在上传附件
			return sumitPreCheck(null);
		},
		showAllError : true
	});
	
	$("#updateProgressTemplate").click(function(){
		var list = [];
		for (var i = 0; i <= progressRowNum; i++) {
			var progressOrder = $("input[name='listsItemTemplateProgress["+ i +"].progressOrder']").val();
			if(progressOrder){
				if(list.length > 0){
					var count = 0;
					for (var j = 0; j < list.length; j++) {
						if(list[j]==progressOrder){
							count++;
						}
					}
					if(count > 0){
						showNotification(2,"排序号不能重复");
						return false;
					}
				}
				list.push(progressOrder);
			}
		}
		
		$(".subform").submit();
	});
	
})
var progressRowNum = '${itemProgressTemplate.listsItemTemplateProgress.size()}';
//添加进度
function addProgress(a) {
		var html = '<li class="ticket-item no-shadow ps-listline templateProgress" style="border-bottom: 0">'+
		'										    <div class="pull-left gray ps-left-text">'+
		'										    	&nbsp;'+
		'										    </div>'+
		'											<div class="pull-left">'+
		'												<input placeholder="阶段名" datatype="*"  name="listsItemTemplateProgress['+progressRowNum+'].progressName" class="colorpicker-default form-control" type="text" style="width: 200px;float: left" >'+
		'											</div>'+
		'											<div class="pull-left" style="margin-left: 20px;">'+
		'												<input placeholder="排序号" datatype="pn"  name="listsItemTemplateProgress['+progressRowNum+'].progressOrder" class="colorpicker-default form-control" type="text" style="width: 200px;float: left">'+
		'											</div>'+
		'											<div class="pull-left" style="margin-left: 20px;">'+
		'												<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delLi(this)" title="删除阶段"></a>'+
		'											</div>'+
		'											<div class="pull-left" style="margin-left: 20px;">'+
		'				                               <a class="ps-point btn-a-full" title="添加" onclick="addProgress(this)">'+
		'				                                	<i class="fa green fa-plus"></i>'+
		'				                               </a>'+
		'											</div>'+
		'				                  		</li>';
	$(a).parent().parent().after(html);
	progressRowNum = progressRowNum+1;
}

//删除选中的li
function delLi(clickLi){
	var li = $(clickLi).parent().parent();
	if(li.attr("class").indexOf("templateProgress") != -1 && $(".templateProgress").length == 2){
		addProgress(clickLi);
		li.remove();
		$($(".templateProgress")[0]).find("div").eq(0).html('&nbsp;模板进度<span style="color: red">*</span>');
	}else{
		li.remove();
		$($(".templateProgress")[0]).find("div").eq(0).html('&nbsp;模板进度<span style="color: red">*</span>');
	}
}
</script>
</head>
<body>
<form class="subform" method="post" action="/itemProgress/updateProgressTemplate">
<input type="hidden" name="id" value="${itemProgressTemplate.id}"/>
<input type="hidden" name="sid" value="${param.sid}"/>
<input type="hidden" name="redirectPage" value="${param.redirectPage}"/>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">项目进度模板详情</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="updateProgressTemplate">
								<i class="fa fa-save"></i>更新
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                        <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;模板名称
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="templateName" placeholder="模板名称" datatype="*" name="templateName" value="${itemProgressTemplate.templateName}"
													class="colorpicker-default form-control" type="text" style="width: 200px;float: left" maxlength="100">
												</div>
											</div>
                                         </li>
                                         <c:forEach items="${itemProgressTemplate.listsItemTemplateProgress}" var="obj" varStatus="vs">
	                                         <li class="ticket-item no-shadow ps-listline templateProgress" style="border-bottom: 0">
											    <div class="pull-left gray ps-left-text">
											    	&nbsp;<c:if test="${vs.count<2 }">模板进度<span style="color: red">*</span></c:if>
											    </div>
												<div class="pull-left">
													<input placeholder="阶段名" value="${obj.progressName }" datatype="*"  name="listsItemTemplateProgress[${vs.count-1}].progressName" class="colorpicker-default form-control" type="text" style="width: 200px;float: left" >
												</div>
												<div class="pull-left" style="margin-left: 20px;">
													<input placeholder="排序号" value="${obj.progressOrder }" datatype="pn"  name="listsItemTemplateProgress[${vs.count-1}].progressOrder" class="colorpicker-default form-control" type="text" style="width: 200px;float: left">
												</div>
												<div class="pull-left" style="margin-left: 20px;">
													<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delLi(this)" title="删除阶段"></a>
												</div>
												<div class="pull-left" style="margin-left: 20px;">
					                               <a class="ps-point btn-a-full" title="添加" onclick="addProgress(this)">
					                                	<i class="fa green fa-plus"></i>
					                               </a>
												</div>
					                  		</li> 
                                         </c:forEach>
                                   	</ul>
                                </div>
                            </div>
                          </div>
                           <div class="widget-body no-shadow"></div> 
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
