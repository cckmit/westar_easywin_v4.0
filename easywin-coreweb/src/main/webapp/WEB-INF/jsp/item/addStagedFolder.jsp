<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
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
var regE = /^\d+$/
	function formSub(){
		
		if($("#subState").val()==1){
			return false;
		}
		var flag = false;
		$(".subform").ajaxSubmit({
		        type:"post",
		        url:"/item/ajaxAddStagedFolder?sid=${param.sid}",
		        dataType: "json",
		        async: false,
		        beforeSubmit:function (a,f,o){
		        	var stagedName = $("#stagedName").val();
		        	if(strIsNull(stagedName)){
		        		layer.tips('请填写项目阶段名称', "#stagedName", {tips: 1});
						$("#stagedName").focus();
		        		return false;
		        	}
					var stagedOrder = $("#stagedOrder").val();
					if(strIsNull(stagedOrder)){
						layer.tips('请填写阶段排序！', "#stagedOrder", {tips: 1});
						$("#stagedOrder").focus();
		        		return false;
					}
					if(!regE.test(stagedOrder)){//匹配就添加
						layer.tips('阶段排序，请填写数字！', "#stagedOrder", {tips: 1});
						$("#stagedOrder").focus();
						return false;
					}
		        	$("#subState").val(1)
				}, 
		        success:function(data){
			        if('y'==data.status){
			        	flag = true;
			        }else{
		        		window.top.layer.msg(data.info)
			        }
		        },error:function(XmlHttpRequest,textStatus,errorThrown){
		        	window.top.layer.msg("系统错误，请联系管理人员");
		        }
		 });
		 $("#subState").val(0)
		 return flag;
	}
</script>
</head>
<body style="background-color: #fff">
<input type="hidden" id="subState" value="0">
	<form class="subform" method="post">
	<input type="hidden" name="itemId" value="${stagedItem.itemId}"/>
	<input type="hidden" name="parentId" value="${stagedItem.id}"/>
	
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">添加阶段文件夹</span>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="widget-body margin-top-40">
                     	<div class="widget radius-bordered">
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline" style="border: 0">
										    <div class="pull-left gray ps-left-text" style="clear:both;min-width:10px">
										    	&nbsp;名称
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input class="colorpicker-default form-control" style="width: 250px" type="text" 
												id="stagedName"  name="stagedName" />
											</div>               
                                        </li>
                                         <li class="ticket-item no-shadow ps-listline" style="border: 0">
										    <div class="pull-left gray ps-left-text" style="clear:both;min-width:10px">
										    	&nbsp;排序
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input class="colorpicker-default form-control" style="width: 250px" type="text" id="stagedOrder" 
												name="stagedOrder" value="${initStagedFolderOrder.stagedOrder}"/>
											</div>               
                                        </li>
                                   	</ul>
                                </div>
                            </div>
                          </div>
                        </div>
					
					</div>
				</div>
			</div>
		</div>
	</form>
</body>
</html>
