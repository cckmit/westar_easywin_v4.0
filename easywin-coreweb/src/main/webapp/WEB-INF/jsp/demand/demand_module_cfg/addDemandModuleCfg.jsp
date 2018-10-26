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
		callback:function (form){
			//提交前验证是否在上传附件
			return sumitPreCheck(null);
		},
		showAllError : true
	});
	
	//初始化第一行数据
	DemandCfgOpt.initFirstLine();
	DemandCfgOpt.initEvent();
	
})
var DemandCfgOpt = {
		initFirstLine:function(){
			var modhtml = $("#modTbody").find("tr").clone();
			$("#listTbody").append($(modhtml));
		},initEvent:function(){
			//添加一行
			$("body").on("click",".addLine",function(){
				var modhtml = $("#modTbody").find("tr").clone();
				$("#listTbody").append($(modhtml));
			});
			//删除一行
			$("body").on("click",".delLine",function(){
				var len = $(this).parents("tbody").find("tr").length;
				if(len == 1){
					showNotification(2,"至少需要保留一行！")
				}else{
					$(this).parents("tr").remove();
				}
			});
			
			$("body").on("click","#addProgressTemplate",function(){
				var blnCheckState = DemandCfgOpt.checkData();
				if(blnCheckState){
					DemandCfgOpt.constrData();
					//$(".subform").submit();
				}
			})
		},checkData:function(){
			var blnCheckState = true;
			
			var modName = $("#modName").val();
			if(!modName){
				blnCheckState = false;
				layer.tips("请填写模板名称！",$("#modName"),{"tips":1})
				return false;
			}
			var trs = $("#listTbody").find("tr");
			if(trs && trs[0]){
				$.each(trs,function(index,tr){
					//排序
					var stepOrder = $(tr).find("td:eq(0)").find("input");
					if(!$(stepOrder).val()){
						blnCheckState = false;
						layer.tips("请填写排序号！",$(stepOrder),{"tips":1})
						return false;
					}
					//名称
					var stepName = $(tr).find("td:eq(1)").find("input");
					if(!$(stepName).val()){
						blnCheckState = false;
						layer.tips("请填写步骤名称！",$(stepName),{"tips":1})
						return false;
					}
					//类型
					var stepType = $(tr).find("td:eq(2)").find("select");
					if(!$(stepType).val()){
						blnCheckState = false;
						layer.tips("请选择类型！",$(stepType),{"tips":1})
						return false;
					}
				})
			}
			return blnCheckState;
		},constrData:function(){
			$("#formData").html('');
			var trs = $("#listTbody").find("tr");
			if(trs && trs[0]){
				$.each(trs,function(index,tr){
					//排序
					var stepOrder = $(tr).find("td:eq(0)").find("input");
					var stepOrderInput = $('<input type="hidden" name="listDemandHandleStepCfg["+index+"].stepOrder"/>');
					$(stepOrderInput).val(stepOrder);
					$("#formData").append($(stepOrderInput));
					//名称
					var stepName = $(tr).find("td:eq(1)").find("input");
					var stepNameInput = $('<input type="hidden" name="listDemandHandleStepCfg["+index+"].stepName"/>');
					$(stepNameInput).val(stepName);
					$("#formData").append($(stepNameInput));
					//类型
					var stepType = $(tr).find("td:eq(2)").find("select");
					var stepTypeInput = $('<input type="hidden" name="listDemandHandleStepCfg["+index+"].stepType"/>');
					$(stepTypeInput).val(stepType);
					$("#formData").append($(stepTypeInput));
				})
			}
		}
}
</script>
</head>
<body>
<form class="subform" method="post" action="/demandModuleCfg/addDemandModuleCfg">
<tags:token></tags:token>
<div id="formData" style="display: none">
</div>
<input type="hidden" name="redirectPage" value="${param.redirectPage}"/>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">新建需求处理模板</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="addProgressTemplate">
								<i class="fa fa-save"></i>添加
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
													<input id="modName" placeholder="模板名称" datatype="*" name="modName" 
													class="colorpicker-default form-control" type="text" style="width: 200px;float: left" maxlength="100">
												</div>
											</div>
                                         </li>
                                   	</ul>
                                </div>
                                
                            </div>
                            
                            <div>
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">需求处理步骤</span>
                                   <button type="button" class="btn btn-info ws-btnBlue"  style="margin-top: 3px;margin-right:5% ;display: none;float: right;line-height: 0.8;" id="but"><i class="fa fa-chain"></i> 联系人</button>
                               </div>
                               
                               <div class="widget-body no-shadow">
                               		<div class="tickets-container bg-white">
                               		 <table class="table table-hover table-striped table-bordered table0" id="linkManTable" style="width: 100%;">
                               		 	<thead>
                               		 		<tr>
	                               		 		<th width="10%">步骤排序<span style="color: red">*</span></th>
												<th width="60%">步骤名称<span style="color: red">*</span></th>
												<th width="20%">处理人员<span style="color: red">*</span></th>
												<th width="10%">操作</th>
											</tr>
                               		 	</thead>
                               		 	<tbody id="listTbody">
                               		 		
                               		 	</tbody>
                               		 	<tbody style="display: none" id="modTbody">
                               		 		<tr>
                               		 			<td>
	                               		 			<input class="table_input" type="text" nullmsg="请输入步骤排序" />
                               		 			</td>
                               		 			<td>
                               		 				<input class="table_input" type="text" nullmsg="请输入步骤名称"/>
                               		 			</td>
                               		 			<td>
                               		 				<select>
														<option value="">请选择</option>
														<option value="1">需求发布人</option>
														<option value="2">项目负责人</option>
														<option value="3">产品经理</option>
													</select>
                               		 			</td>
                               		 			<td>
                               		 				<div class="pull-left" style="margin-left: 20px;">
	                               		 				<a href="javascript:void(0)" class="fa green fa-plus addLine" title="添加一行"></a>
                               		 				</div>
                               		 				<div class="pull-left" style="margin-left: 20px;">
	                               		 				<a href="javascript:void(0)" class="fa red fa-times-circle-o delLine" title="删除本行"></a>
                               		 				</div>
                               		 			</td>
                               		 		</tr>
                               		 	</tbody>
                               		 </table>
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
