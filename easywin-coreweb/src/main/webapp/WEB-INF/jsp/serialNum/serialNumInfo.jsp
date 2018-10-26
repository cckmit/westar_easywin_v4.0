<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
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
	var EasyWin = {"sid":"${param.sid}",
   			"userInfo":{
   				"userId":"${userInfo.id}",
    			"userName":"${userInfo.userName}",
    			"comId":"${userInfo.comId}",
    			"orgName":"${userInfo.orgName}",
    			"isAdmin":"${userInfo.admin}",
   				},
			"homeFlag":"${homeFlag}",
			"ifreamName":"${param.ifreamName}"
   		};
</script>
<script type="text/javascript">
	var sid="${param.sid}";
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		$("#startNum").keydown(function(event){	
			if(event.keyCode==13) {
				return false;
			}
		});
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		serialNumOpt.initVal();
		serialNumOpt.initEvent();
		
	});
function formSub(callBack){
	var checkstate = serialNumOpt.checkForm();
	if(checkstate){
		//异步提交表单
	    $(".subform").ajaxSubmit({
	        type:"post",
	        url:"/serialNum/updateSerialNum?sid=${param.sid}&t="+Math.random(),
	        dataType: "json",
	        async:false,
	        beforeSubmit:function (a,f,o){
	        	$("#subState").val(1);
			}, 
			traditional :true,
	        success:function(data){
		         var state = data.state;
		         if(state=='y'){
		         	callBack();
			     }
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	window.top.layer.alert("系统错误，请联系管理人员",{icon:7,title:false,closeBtn:0,btn:["关闭"]})
	        	
	        }
	    });
	}
    $("#subState").val(0)
}

var  serialNumOpt = {
		checkForm:function(){
			if(!$("#serialType").val()){
	            layer.tips('请设置编码规则 ', $("#serialType"), {tips: 1});
	            return false;
			}
			if(!$("#serialFormat").val()){
	            layer.tips('请设置编码格式', $("#serialFormat"), {tips: 1});
	            return false;
			}
			if(!$("#startNum").val()){
	            layer.tips('请设置初始值', $("#startNum"), {tips: 1});
	            return false;
			}else{
				var serialType = $("#serialType").val();
				var serialFormat = $("#serialFormat").val();
				var startNum = $("#startNum").val();
				var newstartNum =  serialNumOpt.constrStartNum(serialType,serialFormat,startNum);
				if(startNum != newstartNum){
					 layer.tips('初始值不满足编码格式', $("#startNum"), {tips: 1});
			         return false;
				}
			}
			
			
			if(!$("#remark").val()){
	            layer.tips('请填说明', $("#remark"), {tips: 1});
	            return false;
			}
			
			return true;
		},initVal:function(){
			//编码规则设定
			var serialType = "${serialNum.serialType}";
			$("body").find("#serialType").val(serialType);
			//编码格式化
			var serialFormat = "${serialNum.serialFormat}";
			$("body").find("#serialFormat").val(serialFormat);
		},initEvent:function(){
			//编码规则修改
			$("body").on("change","#serialType",function(){
				var serialType = $("#serialType").val();
				var serialFormat = $("#serialFormat").val();
				if(serialType && serialFormat){
					var startNum = $("#startNum").val();
					var newstartNum =  serialNumOpt.constrStartNum(serialType,serialFormat,startNum);
					$("#startNum").val(newstartNum);
				}
			});
			//编码规则修改
			$("body").on("change","#serialFormat",function(){
				var serialType = $("#serialType").val();
				var serialFormat = $("#serialFormat").val();
				if(serialType && serialFormat){
					var startNum = $("#startNum").val();
					var newstartNum =  serialNumOpt.constrStartNum(serialType,serialFormat,startNum);
					$("#startNum").val(newstartNum);
				}
			});
		},constrStartNum:function(serialType,serialFormat,startNum){
			serialFormat = serialFormat.replace(/yyyy/,'{0}');
			serialFormat = serialFormat.replace(/MM/,'{1}');
			serialFormat = serialFormat.replace(/XX/,'{2}');
			var levelOne= serialType.split("-")[0];
			var levelTwo= serialType.split("-")[1];
			var levelThree= serialType.split("-")[2];
			if(startNum){
				startNum= startNum.replace(/[^0-9]/ig,"");
				//第一位数年份默认
				var levelOneNum = new Date().getFullYear();
				if(startNum.length>levelOne){
					levelOneNum = startNum.substring(0,levelOne);
				}
				//第二位数月份默认
				var levelTwoNum = new Date().getMonth()+1;
				if(startNum.length>(Number(levelOne) + Number(levelTwo))){
					levelTwoNum = startNum.substring(levelOne,(Number(levelOne) + Number(levelTwo)));
				}
				if(Number(levelTwoNum)===0){
					levelTwoNum = new Date().getMonth()+1;
				}
				if(Number(levelTwoNum)<10){
					levelTwoNum = '0'+Number(levelTwoNum);
				}
				//第三位数序列号默认
				var levelThreeNum = startNum.substring((Number(levelOne) + Number(levelTwo)),startNum.length);
				if(levelThreeNum.length>levelThree){
					levelThreeNum = startNum.substring(startNum.length-levelThree,startNum.length);
				}
				if(Number(levelThreeNum)===0){
					levelThreeNum = "1";
				}
				levelThreeNum = +levelThreeNum;
				levelThreeNum = levelThreeNum+'';
				var leftzero = levelThree - levelThreeNum.length;
				if(leftzero>0){
					for(var i=0;i<leftzero;i++){
						levelThreeNum = '0'+levelThreeNum;
					}
				}
				return  String.format(serialFormat,levelOneNum,levelTwoNum,levelThreeNum);
			}else{
				var levelOneNum = new Date().getFullYear();
				var levelTwoNum = new Date().getMonth()+1;
				if(levelTwoNum<10){
					levelTwoNum = '0'+levelTwoNum;
				}
				var levelThreeNum = '1';
				for(var i=1;i<levelThree;i++){
					levelThreeNum = '0'+levelThreeNum;
				}
				return  String.format(serialFormat,levelOneNum,levelTwoNum,levelThreeNum);
			}
		}
}
</script>
<style type="text/css">
.txt-red{color: #d72323 !important}
</style>
</head>
<body>
<form class="subform" method="post">
<tags:token></tags:token>
<input type="hidden" name="activityMenu" value="${activityMenu}"/>
<input type="hidden" name="subState" value=""/>
<input type="hidden" name="id" value="${serialNum.id}">
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary menu-topic ps-layerTitle" style="font-size: 20px">编辑编号规则</span>
                        <div class="widget-buttons ps-toolsBtn">
							&nbsp;
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
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
										    	编码规则
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	 <select class="populate" id="serialType" name="serialType" style="cursor:auto;width: 200px">
					 								<option value="">请选择编码规则</option>
					 								<option value="4-2-2">4-2-2</option>
					 								<option value="4-2-3">4-2-3</option>
					 								<option value="4-2-4">4-2-4</option>
												</select>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	编码格式
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<div class="pull-left">
												  	 <select class="populate"  id="serialFormat" name="serialFormat" style="cursor:auto;width: 200px">
														<option value="">请选择编码格式</option>
						 								<option value="yyyyMMXX">yyyyMMXX</option>
						 								<option value="yyyy年MM月XX">yyyy年MM月XX</option>
													</select>
												</div>
											</div>
                                         </li>
                                         
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	初始值
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="startNum" defaultLen="52" name="startNum" 
													class="colorpicker-default form-control" type="text" value="${serialNum.startNum}" style="float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" style="height:165px;">
											<div class="pull-left gray ps-left-text">
												说明 <span style="color: red">*</span>
											</div>
											<div class="ticket-user pull-left ps-right-box padding-bottom-5" style="width:80%;">
												<textarea class="form-control" id="remark"
														name="remark" rows="" cols=""
														style="width:90%;height: 150px;float:left"
														name="remark" >${serialNum.remark}</textarea>
											</div> 
										</li>
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
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
