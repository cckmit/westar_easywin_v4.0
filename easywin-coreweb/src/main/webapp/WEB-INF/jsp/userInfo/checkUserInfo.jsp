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
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
		
		//设置滚动条高度
		var height = $(window).height()-30;
		$("#contentBody").css("height",height+"px");
	})
	function checkUserInfo(){
		//审核结果
		var checkState = $("input[name='checkState']:checked").val();
		if(!checkState){
			$("#msg").show();
			layer.tips("请审核","#checkDiv",{tips:1})
			return false;
		}else if('1'==checkState ||'2'==checkState){
			$(".subform").submit();
			return true;
		}else{
			return false;
		}
	}
</script>
</head>
<body>
	<form class="subform" method="post" action="/userInfo/checkUserInfo" style="margin-top:15px;">
	<input type="hidden" name="account" value="${joinRecord.account}"/>
	<input type="hidden" name="id" value="${joinRecord.id}"/>
	<input type="hidden" name="confirmId" value="${joinRecord.confirmId}"/>
	<tags:token></tags:token>
		<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">用户审核</span>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 --> 
                     <div class="widget-body margin-top-20" id="contentBody" style="overflow-y:auto;position: relative;">
                     	<div class="widget radius-bordered no-margin">
                            <div class="widget-body no-shadow" style="padding:0px 12px">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline" style="border: 0">
										    <div class="pull-left gray ps-left-text" style="font-size: 15px">
											   &nbsp;申请账号:
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly" style="width: 350px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${joinRecord.account}</span>
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both;border: 0">
										    <div class="pull-left gray ps-left-text" style="font-size: 15px">
											   &nbsp;申请时间:
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly" style="width: 350px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${fn:substring(joinRecord.recordCreateTime,0,16)}</span>
												</div>
											</div>
                                        </li>
                                        <li class="ticket-item no-shadow margin-top-5 autoHeight no-padding" style="border: 0 ">
										    <div class="pull-left gray ps-left-text padding-top-10" style="font-size: 15px">
										    	&nbsp;验证信息
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="height: auto;">
												<div readonly="readonly" class="padding-top-10" style="width: 350px;border: 1px solid #d5d5d5;min-height: 50px">
													<span class="padding-left-10"><tags:viewTextArea>${joinRecord.joinNote}</tags:viewTextArea></span>
												</div>
											</div>
											<div class="ps-clear"></div>
                                         </li>
                                         <li class="clearfix">
										    <div class="pull-left gray ps-left-text" style="font-size: 15px">
											   &nbsp;审核:
										    </div>
											<div class="ticket-user pull-left ps-right-box" id="checkDiv">
												<div>
													<span>
														<label>
															<input type="radio" name="checkState" class="" value="1" onclick="$('#reason').hide()"/>
															<span class="text">同意加入</span>
														</label>
													</span>
													<span style="${joinRecord.checkState==2?'display:none':'display:bolck'};">
														<label>
															<input type="radio" name="checkState" value="2" onclick="$('#reason').show();$('#msg').hide();"/>
															<span class="text">拒绝</span>
														</label>
													</span>
												</div>
												<div id="reason" title="请输入理由" class="clearfix" style="display:none;width: 350px;">
													<textarea class="bordered-themeprimary" id="reason" name="rejectReson"  name="rejectReson" id="rejectReson" 
													style="height:80px;width: 350px"></textarea>
												</div>
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
