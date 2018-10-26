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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	
	var sid = "${param.sid}";
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	
	$(function() {
		//判断是否是发送邮件后返回当前页面
		if('${addState}'=='1'){
			closeWin();
			parent.pulldata();
		}else if('${addState}'=='0'){
			closeWin();
			showNotification(2,"发送失败！");
		}
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			},
			datatype:{
				"email":function(gets,obj,curform,regxp){
					var str = $(obj).val();
					if(str){
						var test = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
						if(!test.test(str)){
							return "邮箱格式不正确";
						}
					}
				}
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		$("#addMail").click(function(){
			formSub();
		});
	});
	//提交表单
	function formSub(){
		if(checkCkBoxStatus("upfiles.upfileId")){
			var ckBoxs = $(":checkbox[name='upfiles.upfileId']");
			if (ckBoxs != null) {
				for ( var i = 0; i < ckBoxs.length; i++) {
					var fileId = $(ckBoxs[i]).val();
					var option = $("#upfiles_upfileId").find("option[value='"+fileId+"']")
					if(option && option[0]){
						continue;
					}
					var option = $('<option></option>');
					if ($(ckBoxs[i]).attr('checked') ) {
						$(option).attr("value",fileId);
						$(option).html($(ckBoxs[i]).attr("data-fileName"));
						$("#upfiles_upfileId").append($(option));
					}
				}
			}
			
		}
		$(".subform").submit();
		
	}
	var copyAccountsRowNum = 1;
	//添加抄送人
	function addCopyAccounts() {
		var html ='<div>'+
			'												<div class="pull-left gray ps-left-text">&nbsp;</div>'+
			'												<div class="ticket-user pull-left ps-right-box">'+
			'												  	<div class="pull-left">'+
			'														<input datatype="email" name="copyAccounts['+copyAccountsRowNum+']" '+
			'														class="colorpicker-default form-control" type="text" style="width: 200px;float: left" value="">'+
			'													</div>'+
			'													<div class="pull-left" style="margin-left: 20px;">'+
			'														<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delCopyAccounts(this)" title="删除"></a>'+
			'													</div>'+
			'													<div class="pull-left" style="margin-left: 20px;">'+
			'						                               <a class="ps-point btn-a-full" title="添加" onclick="addCopyAccounts()"><i class="fa green fa-plus"></i></a>'+
			'													</div>'+
			'												</div>'+
			'												<div class="ps-clear"></div>'+
			'											</div>'; 
		$("#copy_-1").before(html);
		copyAccountsRowNum = copyAccountsRowNum +1;
	}
	//删除抄送人
	function delCopyAccounts(click){
		var div = $(click).parent().parent().parent();
    	div.remove();
	}
</script>
</head>
<body>
<form class="subform" method="post" action="/mail/addMail">
<input type="hidden" name="sid" value="${param.sid }">
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">添加邮件</span>
                        <div class="widget-buttons ps-toolsBtn">
                        	<a href="javascript:void(0)" class="blue" id="addMail">
								<i class="fa fa-save"></i>发送
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;发送账号
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<select class="populate"  datatype="*" id="accountId" name="accountId" style="cursor:auto;width: 200px">
														<optgroup label="选择发送账号"></optgroup>
														<c:choose>
															<c:when test="${not empty listMs}">
								 								<c:forEach items="${listMs}" var="obj" varStatus="status">
								 									<option value="${obj.id}" ${obj.id==mail.accountId?'selected':''}>${obj.account}</option>
								 								</c:forEach>
															</c:when>
														</c:choose>
													</select>
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;接收账号
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="recipients" datatype="e" name="recipients" 
													class="colorpicker-default form-control" type="text" style="width: 200px;float: left" value="${mail.recipients}">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow autoHeight ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;抄送账号
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="recipients_0" datatype="email" name="copyAccounts[0]" 
													class="colorpicker-default form-control" type="text" style="width: 200px;float: left" value="">
												</div>
												<div class="pull-left" style="margin-left: 20px;">
					                               <a class="ps-point btn-a-full" title="添加" onclick="addCopyAccounts()"><i class="fa green fa-plus"></i></a>
												</div>
											</div>
											<div class="ps-clear"></div>
											<div id="copy_-1"></div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;主题
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="subject" datatype="*" name="subject" value="${mail.subject}"
													class="colorpicker-default form-control" type="text" style="width: 200px;float: left" >
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline clearfix" style="clear:both;min-height: 260px">
										     <div class="pull-left gray ps-left-text" style="min-height: 260px;">
		                                            &nbsp;正文
		                                    </div>
		                                    <div class="ticket-user pull-left ps-right-box" style="height: auto;">
		                                        <textarea class="colorpicker-default form-control margin-top-0 " id="body" name="body" rows="" cols="" style="width:610px;height: 240px;display: none" maxlength="4000">${mail.body}</textarea>
		                                        <iframe ID="eWebEditor1" src="/static/plugins/ewebeditor/ewebeditor.htm?id=body&style=expand600" 
														frameborder="0" scrolling="no" width="610" height="240"></iframe>
		                                    </div>
		                                    <div class="ps-clear"></div>     
                                         </li>
                                         
                                         <li class="ticket-item no-shadow autoHeight ps-listline" style="display:${not empty mail.upfiles[0]?'block':'none' } " >
									    	<div class="pull-left gray ps-left-text" style="height: 40px;">
		                                            &nbsp;附件关联
		                                    </div>
											<div class="pull-left task-left-text task-height">
												<label class="padding-left-5"> 
													<input type="checkbox" class="colored-blue" onclick="checkAll(this,'upfiles.upfileId')" checked="true"/> <span class="text" style="color: inherit;">全选</span>
												</label>
											 </div>
											  <div class="ps-clear"></div>
											 <c:forEach items="${mail.upfiles }" var="upfile" varStatus="vst">
												 <div class="pull-left  ps-left-text padding-top-10" style="text-align: right;">
										    		&nbsp;
										    	</div>
												<div class="pull-left task-left-text task-height">
													<label class="padding-left-5"> 
														<input type="checkbox" class="colored-blue" name="upfiles.upfileId" value="${upfile.upfileId}" data-fileName="${upfile.filename}" id="file_${upfile.upfileId}" checked/> <span class="text" style="color: inherit;">${upfile.filename}</span>
													</label>
												 </div>
												 <div class="ps-clear"></div>
											 </c:forEach>
                                         </li>
                                         
                                         <li class="ticket-item no-shadow autoHeight ps-listline" >
									    	<div class="pull-left gray ps-left-text" style="height: 40px;">
		                                            &nbsp;附件
		                                    </div>
											<div class="pull-left ps-right-box" style="width: 400px;height: auto;">
												<div class="margin-top-10">
											  		<tags:uploadMore name="upfiles.upfileId" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
												</div>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                         
                                   	</ul>
                                </div>
                            </div>
                          </div>
                            
                              
                           </div>
                            
                           <div class="widget-body no-shadow">
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
