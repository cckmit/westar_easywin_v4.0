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
    <script type="text/javascript" charset="utf-8" src="/static/js/sp_center/sp_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
	var sid="${param.sid}";//sid全局变量
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
			datatype:{
				"input":function(gets,obj,curform,regxp){
					var str = $(obj).val();
					if(str){
						var count = str.replace(/[^\x00-\xff]/g,"**").length;
						var len = $(obj).attr("defaultLen");
						if(count%2==1){
							count = (count+1)/2;
						}else{
							count = count/2;
						}
						if(count>len){
							return "步骤名称太长";
						}else{
							return true;
						}
					}else{
						return false;
					}
				}
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		//form表单提交
		$("#addFlowStepConditions").click(function(){
			//条件解析
			var trs = $("#conditionTable tr");
			if(trs.length>1){//除去表头
				var conditions="";
				for(var i=1;i<trs.length;i++){
					var conditionVar = $(trs[i]).find("[name='conditionVar']").val();
					var conditionType = $(trs[i]).find("[name='conditionType']").val();
					var conditionValue = $(trs[i]).find("[name='conditionValue']").val();
					var conditionNum = $(trs[i]).find("[name='conditionNum']").val();
					conditions=conditions+"<input type=\"hidden\" name=\"listSpStepCondition["+(i-1)+"].conditionVar\" value=\""+conditionVar+"\"/><br />";
					conditions=conditions+"<input type=\"hidden\" name=\"listSpStepCondition["+(i-1)+"].conditionType\" value=\""+conditionType+"\"/><br />";
					conditions=conditions+"<input type=\"hidden\" name=\"listSpStepCondition["+(i-1)+"].conditionValue\" value=\""+conditionValue+"\"/><br />";
					conditions=conditions+"<input type=\"hidden\" name=\"listSpStepCondition["+(i-1)+"].conditionNum\" value=\""+conditionNum+"\"/><br />";
				}
				$(".subform").append(conditions);
			}
			$(".subform").submit();
			//关闭当前页面
			//closeWindow();
		}); 
	})
$(function(){
	//条件生成
	$("#conditionAdd").click(function(){
		//生成条件
		addCondition();
	});
	//人员选择
	$("#user").click(function(){
		window.top.layer.open({
		  type: 2,
		  title: false,
		  closeBtn:0,
		  area: ["750px", "530px"],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/common/userMorePage?sid="+sid,'no'],
		  btn: ["确定","清空","取消"],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  var options = iframeWin.returnUser();
			  if(options){
				  var usersName="";
				  for ( var i = 0; i < options.length; i++) {
					  usersName = usersName+","+options[i].text; 
				  }
				  usersName = usersName.substring(1,usersName.length); 
				  $("#conditionValue").val(usersName)
				  $("#conditionValue").attr("readonly","readonly");
			  }
			  window.top.layer.close(index)
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  iframeWin.removeOptions("userselect");
			  return false;
		  },success: function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  //设置点击关闭
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
		});
	});
	//部门选择
	$("#dep").click(function(){
		window.top.layer.open({
		  type: 2,
		  title: false,
		  closeBtn:0,
		  area: ["550px", "460px"],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/common/depMorePage?sid="+sid,'no'],
		  btn: ["确定","清空","取消"],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  var options = iframeWin.returnOrg();
			  if(options){
				  var depNames="";
				  for ( var i = 0; i < options.length; i++) {
					  depNames = depNames+","+options[i].text; 
				  }
				  depNames = depNames.substring(1,depNames.length); 
				  $("#conditionValue").val(depNames)
				  $("#conditionValue").attr("readonly","readonly");
				  window.top.layer.close(index)
			  }
		  },btn2: function(index, layero){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  iframeWin.removeAll();
			  return false;
		  },success: function(layero,index){
			  var iframeWin = window.top.window[layero.find("iframe")[0]["name"]];
			  iframeWin.setOptions();
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					 window.top.layer.close(index);
				 })
		  }
		});
	});
});
//生成条件
function addCondition(){
	//条件变量conditionVar
	//条件运算符conditionType
	//条件比较值conditionValue
	//条件列表conditionTable
	//条件比对表达式conditionExp
	if(strIsNull($("#conditionVar option:selected").val()) || $("#conditionVar option:selected").val()==-1){
		window.top.layer.msg("请选择\"条件变量\"！",{icon:2});
		return;
	}else if(strIsNull($("#conditionType option:selected").val()) || $("#conditionType option:selected").val()==-1){
		window.top.layer.msg("请选择\"条件运算符\"！",{icon:2});
		return;
	}
	var conditionValue=$("#conditionValue").val();
	if(conditionValue.indexOf(",") >= 0){//比较多个值
		var values = conditionValue.split(",");
		for(var i=0;i<values.length;i++){
			addConditionTr(values[i]);
		}
	}else{//一个比较值
		addConditionTr(conditionValue);
	}
    $("#conditionValue").removeAttr("readonly");
}
//添加条件行
function addConditionTr(conditionValue){
	var conditionVar=$("#conditionVar option:selected").val();
	var conditionType=$("#conditionType option:selected").val();
	var rows = $("#conditionTable tr").length;
	var row="<tr>";
	row = row+"<td style=\"text-align:center;\">["+rows+"]</td>";
	row = row+"<td>\""+$("#conditionVar option:selected").text()+"\""+
						conditionType+"\""+conditionValue+"\"";
	row = row+"<input type=\"hidden\" name=\"conditionVar\" value=\""+conditionVar+"\"/>";
	row = row+"<input type=\"hidden\" name=\"conditionType\" value=\""+conditionType+"\"/>";
	row = row+"<input type=\"hidden\" name=\"conditionValue\" value=\""+conditionValue+"\"/>";
	row = row+"<input type=\"hidden\" name=\"conditionNum\" value=\"["+rows+"]\"/>";
	row = row+"</td>";
	row = row+"<td style=\"text-align:center;\"><a href=\"javascript:void(0);\" onclick=\"delCondition(this)\">删除</a></td>";
	row = row+"</tr>";
	$("#conditionTable").append(row);
	$("#conditionValue").val("");//比较值清空
	$("#conditionTableLi").css("height",((rows+1)*35)+"px");//根据候选步骤个数设置候选步骤li高度
}
//删除条件行
function delCondition(obj){
	window.top.layer.confirm("确定要删除？", {icon: 3, title:"确认对话框"}, function(index){
      //$(obj).parent().parent().remove(); 
	  
	  var tab=conditionTable;
	  var tr=obj.parentNode.parentNode;
	  var no=tr.rowIndex;
	  tab.deleteRow(tr.rowIndex);
	  //更新后面的序号
	  for(var i=no;i<tab.rows.length;i++)
	  {
	  	tab.rows[i].cells[0].innerText="["+(tab.rows[i].rowIndex)+"]";
	  	$(tab.rows[i]).find("[name='conditionNum']").val("["+tab.rows[i].rowIndex+"]");
	  }
	  var rows = $("#conditionTable tr").length;
	  $("#conditionTableLi").css("height",((rows+1)*35)+"px");//根据候选步骤个数设置候选步骤li高度
	  window.top.layer.close(index);
	});
}
$(document).ready(function(){
	$("#conditionTableLi").css("height",(${fn:length(listSpStepCondition)>0?(fn:length(listSpStepCondition)+1):1}*40)+"px");//根据候选步骤个数设置候选步骤li高度
	//设置滚动条高度
	var height = $(window).height()-60;
	$("#contentBody").css("height",height+"px");
});
</script>
</head>
<body>
<form class="subform" method="post" action="/flowDesign/addFlowStepConditions">
<tags:token></tags:token>
<input type="hidden" name="activityMenu" value="${activityMenu}"/>
<input type="hidden" name="flowId" value="${stepVo.flowId}"/>
<input type="hidden" name="id" value="${stepVo.id}"/>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">设置条件</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="addFlowStepConditions">
								<i class="fa fa-save"></i>保存
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
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	步骤
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="padding:0 10px;">
											  	<span style="color:red;font-weight:bold;">${stepVo.stepName}</span>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	条件生成
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="padding:0 10px;">
											   <select class="populate" id="conditionVar" style="max-width:300px;">
													<c:choose>
														<c:when test="${not empty listFormCompont}">
															<option value="-1">变量对象</option>
							 								<c:forEach items="${listFormCompont}" var="formCompont" varStatus="status">
							 									<option value="${formCompont.fieldId}" title="${formCompont.title}">${formCompont.title}</option>
							 								</c:forEach>
														</c:when>
													</c:choose>
												</select>
											   <select class="populate" id="conditionType">
													<option value="-1">运算符</option>
													<option value="=">等于</option>
													<option value="!=">不等于</option>
													<option value=">">大于</option>
													<option value=">=">大于等于</option>
													<option value="<">小于</option>
													<option value="<=">小于等于</option>
												</select>
												<input id="conditionValue" class="colorpicker-default" style="height:31px;" type="text" placeholder="比较对象">
												<button id="dep" class="btn btn-info btn-primary btn-xs" type="button">部门</button>
												<button id="user" class="btn btn-info btn-primary btn-xs" type="button">人员</button>
												<button id="conditionAdd" class="btn btn-info btn-primary btn-xs" type="button">条件生成</button>
											</div>               
                                         </li>
                                         <li id="conditionTableLi" class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	条件列表
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="padding:0 10px;">
											   <table id="conditionTable" style="width:500px;">
											   	<tr>
											   		<td style="width:10%;text-align:center;">序号</td>
											   		<td>条件描述</td>
											   		<td style="width:15%;text-align:center;">操作</td>
											   	</tr>
											   	<c:choose>
											   		<c:when test="${not empty listSpStepCondition}">
											   			<c:forEach items="${listSpStepCondition}" var="spStepCondition">
												   			<tr>
												   				<td style="text-align:center;">${spStepCondition.conditionNum}</td>
												   				<td>
												   					"${spStepCondition.conditionVarName}"${spStepCondition.conditionType}"${spStepCondition.conditionValue}"
												   					<input type="hidden" name="conditionVar" value="${spStepCondition.conditionVar}"/>
																	<input type="hidden" name="conditionType" value="${spStepCondition.conditionType}"/>
																	<input type="hidden" name="conditionValue" value="${spStepCondition.conditionValue}"/>
																	<input type="hidden" name="conditionNum" value="${spStepCondition.conditionNum}"/>
												   				</td>
												   				<td style="text-align:center;"><a href="javascript:void(0);"
												   				 onclick="delCondition(this)">删除</a></td>
												   			</tr>
											   			</c:forEach>
											   		</c:when>
											   	</c:choose>
											   </table>
											</div>               
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both;">
										    <div class="pull-left gray ps-left-text">
										    	运算关系
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="padding:0 10px;">
											   <input id="conditionExp" name="conditionExp" class="colorpicker-default form-control" type="text" placeholder="如：[1] and [2]"
													style="width:572px;" value="${stepVo.conditionExp}">
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
