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
	
	//人员范围的显示隐藏
	function showMans(){
		
		if($("#pubState").val() == 0){
			$("#mans").show();
		}else{
			$("#mans").hide();
		}
		
	}
	
	$(function() {
		addContactWay();
		addAddress();
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
		$("#addOutLinkMan").click(function(){
			formSub();
		});
	});
	//提交表单
	function formSub(){
		$(".subform").submit();
	}
	
	var contactWayRowNum = 1;
	//添加联系方式
	function addContactWay() {
		//获取联系方式下拉值
		var str = '';
		listDataDic("contactWay",function(data){
			if(data && data[0]){
				for (var i = 0; i < data.length; i++) {
					if(data[i].parentId > 0){
						str +='<option value="'+data[i].code+'">'+data[i].zvalue+'</option>';
					}
				}
			}
		});
		setTimeout(function() {
			var html = '<li class="ticket-item no-shadow ps-listline">'+
			'										    <div class="pull-left gray ps-left-text">'+
			'										    	&nbsp;联系方式'+
			'										    </div>'+
			'											<div class="ticket-user pull-left ps-right-box">'+
			'											  	<div class="pull-left">'+
			'											  		<select class="populate" id="listContactWay['+contactWayRowNum+'].contactWayCode" name="listContactWay['+contactWayRowNum+'].contactWayCode"  style="width: 200px;float: left">'+
			str+
			'											  		</select>'+
			'												</div>'+
			'											</div>'+
			'											<div class="pull-left" style="margin-left: 20px;">'+
			'												<input id="listContactWay['+contactWayRowNum+'].contactWay"  name="listContactWay['+contactWayRowNum+'].contactWay" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">'+
			'											</div>'+
			'											<div class="pull-left" style="margin-left: 20px;'+(contactWayRowNum==1?'display:none;':'')+'">'+
			'												<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delLi(this)" title="删除联系方式"></a>'+
			'											</div>'+
			'											<div class="pull-left" style="margin-left: 20px;">'+
			'			                                   <a class="ps-point btn-a-full" title="添加联系方式" onclick="addContactWay()">'+
			'			                                    	<i class="fa green fa-plus"></i>'+
			'			                                   </a>'+
			'											</div>'+
			'			                                	'+
			'                                         </li>';
		$("#contactWay_-1").before(html);
		
		contactWayRowNum = contactWayRowNum+1;
		
		}, 200);
		
	}
	
	var addressRowNum = 1;
	//添加联系地址
	function addAddress() {
			var html = '<li class="ticket-item no-shadow ps-listline" >'+
			'										    <div class="pull-left gray ps-left-text">'+
			'										    	&nbsp;联系地址'+
			'										    </div>'+
			'											<div class="ticket-user pull-left ps-right-box">'+
			'											  	<div class="pull-left">'+
			'											  		<select class="populate" id="listAddress['+addressRowNum+'].addressCode" name="listAddress['+addressRowNum+'].addressCode"  style="width: 200px;float: left">'+
			'						 								<option value="0">办公地址</option>'+
			'						 								<option value="1">家庭地址</option>'+
			'													</select>'+
			'												</div>'+
			'											</div>'+
			'											<div class="pull-left" style="margin-left: 20px;">'+
			'												<input id="listAddress['+addressRowNum+'].address"  name="listAddress['+addressRowNum+'].address" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">'+
			'											</div>'+
			'											<div class="pull-left" style="margin-left: 20px;'+(contactWayRowNum==1?'display:none;':'')+'">'+
			'												<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delLi(this)" title="删除联系地址"></a>'+
			'											</div>'+
			'											<div class="pull-left" style="margin-left: 20px;" >'+
			'			                                   <a class="ps-point btn-a-full"  title="添加联系地址" onclick="addAddress()">'+
			'			                                    	<i class="fa green fa-plus"></i>'+
			'			                                   </a>'+
			'											</div>'+
			'                                         </li>';
		$("#address_-1").before(html);
		
		addressRowNum = addressRowNum+1;
		
		
	}
	
	
	//删除选中的li
	function delLi(clickLi){
		var li = $(clickLi).parent().parent();
    	li.remove();
	}
	
</script>
</head>
<body>
<form class="subform" method="post" action="/outLinkMan/addOutLinkMan">
<input type="hidden" name="isCrm" value="${isCrm }">
<tags:token></tags:token>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">外部联系人添加</span>
                        <div class="widget-buttons ps-toolsBtn">
                        	<a href="javascript:void(0)" class="blue" id="addOutLinkMan">
								<i class="fa fa-save"></i>添加
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
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">基础信息</span>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;联系人姓名
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="linkManName" datatype="*" name="linkManName" nullmsg="请输入联系人姓名" 
													class="colorpicker-default form-control" type="text" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;性别
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<tags:dataDic type="gender" name="gender" value="" please="t" style="width:200px;"></tags:dataDic>
												</div>
											</div>
											<div class="pull-left gray ps-left-text" style="margin-left: 20px;">
										    	&nbsp;职务
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="post"  name="post" 	class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;移动电话
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="movePhone"  name="movePhone" 	class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
											<div class="pull-left gray ps-left-text" style="margin-left: 20px;">
										    	&nbsp;座机
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="linePhone"  name="linePhone" 	class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li id="contactWay_-1"></li>
                                          <li id="address_-1"></li>
                                          <li class="ticket-item no-shadow ps-listline" style="clear:both;height: 100px">
										     <div class="pull-left gray ps-left-text" style="height: 100px;">
		                                            &nbsp;爱好
		                                    </div>
		                                    <div class="ticket-user pull-left ps-right-box" style="height: auto;">
		                                        <textarea class="colorpicker-default form-control margin-top-0 " id="hobby" name="hobby" rows="" cols="" style="width:510px;height: 80px;" maxlength="200"></textarea>
		                                    </div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both;height: 100px">
										     <div class="pull-left gray ps-left-text" style="height: 100px;">
		                                            &nbsp;备注
		                                    </div>
		                                    <div class="ticket-user pull-left ps-right-box" style="height: auto;">
		                                        <textarea class="colorpicker-default form-control margin-top-0 " id="remarks" name="remarks" rows="" cols="" style="width:510px;height: 80px;" maxlength="200"></textarea>
		                                    </div>
                                         </li>
                                          <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;阅读权限
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
											  		<select class="populate" id="pubState" name="pubState" onchange="showMans()" style="width: 200px;float: left">
						 								<option value="0">私有</option>
						 								<option value="1">公开</option>
													</select>
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow autoHeight no-padding" style="display: block;" id="mans">
										    <div class="pull-left gray ps-left-text padding-top-10">
										    	&nbsp;分享范围
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="height: auto;">
												<div class="pull-left gray ps-left-text padding-top-10">
													<tags:userMore name="listRangeUser.userId" showName="userName" callBackStart="yes" disDivKey="carPersonDiv"></tags:userMore>
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
