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
		$(function(){
			//设置滚动条高度
			var height = $(window).height()-40;
			$("#contentBody").css("height",height+"px");
			
			var status = '${festMod.status}';
			$(":radio[name=\"status\"][value='"+status+"']").attr("checked","checked");
		})
		
		function checkForm(){
			//默认验证通过
			var a = 1;
			//日期类型
			var status = $(":radio[name=\"status\"]:checked").val();
			if(!status){
				layer.tips("请选择日期类型",$("input[name=\"status\"]").parent(),{tips:1});
				a = !1;
				return a;
			}
			//开始时间
			var dayTimeS = $("#dayTimeS").val();
			if(!dayTimeS){
				layer.tips("请填写开始时间",$("#dayTimeS"),{tips:1});
				a = !1;
				return a;
			}
			//结束时间
			var dayTimeE = $("#dayTimeE").val();
			if(!dayTimeE){
				layer.tips("请填写结束时间",$("#dayTimeE"),{tips:1});
				a = !1;
				return a;
			}
			//日期描述
			var describe = $("#describe").val();
			if(!describe){
				layer.tips("请填写日期描述",$("#describe"),{tips:1});
				a = !1;
				return a;
			}
			return a;
		}
		//修改日期维护
		function updateFestMod(){
			
			if($("#subState").val()==1){
				return false;
			}
			var result;
			 //异步提交表单
		    $("#festModForm").ajaxSubmit({
		        type:"post",
		        url:"/festMod/updateFestMod?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        async:false,
		        beforeSubmit:function (a,f,o){
		        	$("#subState").val(1)
				}, 
		        success:function(data){
		        	 var status = data.status;
			         if(status == 'y'){
			        	 result=true;
				     }else{
				    	 showNotification(2,data.info);
				    	 result=false;
				     }
		        },
		        error:function(XmlHttpRequest,textStatus,errorThrown){
		        	window.top.layer.alert("系统错误，请联系管理人员",{icon:7,title:false,closeBtn:0,btn:["关闭"]})
		        	return false;
		        	
		        }
		    });
		    $("#subState").val(0);
		    return result;
		}
</script>
</head>
<body>
<input type="hidden" id="subState" value="0">
<form class="subform" id="festModForm">
<input type="hidden" id="subState" name="id" value="${festMod.id}">
	<div class="container no-padding" style="width: 100%">	
		<div class="row" >
			<div class="col-lg-12 col-sm-12 col-xs-12" >
            	<div class="widget" style="margin-top: 0px;">
	            	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	                	<span class="widget-caption themeprimary" style="font-size: 20px">修改日期维护</span>
	                	<div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="updateFestModBtn">
								<i class="fa fa-save"></i>修改
							</a>
						</div>
	                       <div class="widget-buttons">
							<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
	                   </div><!--Widget Header-->
                   
            		<div class="widget-body margin-top-40" id="contentBody" style="overflow:false;overflow-y:auto;position: relative;">
                     	<div class="widget radius-bordered">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">日期信息</span>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white" >
									<ul class="tickets-list">
									
										<li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												日期类型：
												<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<label class="padding-left-5">
												 	<input type="radio" class="colored-blue" name="status" value="1"></input>
												 	<span class="text" style="color:inherit;">休假</span>
											    </label>
												<label class="padding-left-20">
												 	<input type="radio" class="colored-blue" name="status" value="2"/>
												 	<span class="text" style="color:inherit;">上班</span>
											    </label>
											</div>
										</li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												开始日期：
												<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input class="colorpicker-default form-control ps-pointer" name="dayTimeS" type="text" id="dayTimeS" readonly="readonly" style="width: 300px;float: left" 
												name="clockDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'dayTimeE\',{d:-0});}'})" nullmsg="请选择日期" value="${festMod.dayTimeS}"/>
											</div>
										</li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												结束日期：
												<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input class="colorpicker-default form-control ps-pointer" name="dayTimeE"  type="text" id="dayTimeE" readonly="readonly" style="width: 300px;float: left" 
												name="clockDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'dayTimeS\',{d:+0});}'})" nullmsg="请选择日期" value="${festMod.dayTimeE}"/>
											</div>
										</li>
                                         
										<li class="ticket-item no-shadow autoHeight no-padding" style="border-bottom: 0px">
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		日期描述：
									    		<span style="color: red">*</span>
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 300px;height: auto;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10"
										  		placeholder="日期描述……"  style="height:80px;width: 300px" id="describe" name="describe" rows="" cols="">${festMod.describe}</textarea>
											</div> 
											<div class="ps-clear"></div>              
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
