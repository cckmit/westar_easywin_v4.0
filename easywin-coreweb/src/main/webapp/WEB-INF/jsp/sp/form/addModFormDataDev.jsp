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
<!-- 框架样式 -->

<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>

		
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" charset="utf-8" src="/static/plugins/ueditor/form.layout.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" charset="utf-8" src="/static/plugins/form/js/workMobFlowFormDev.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" charset="utf-8" src="/static/js/sp_center/sp_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>

<script type="text/javascript">
var sid="${not empty param.auth_key?param.auth_key:param.sid}";
//打开页面body
var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//打开页面的标签
var openPageTag;
//打开页面的标签
var openTabIndex;
//autoCompleteCallBack回调对象标识
var autoCompleteCallBackVar =null;
var regex = /['|<|>|"]+/;
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	$(document).on('input propertychange', 'textarea', function() {
		$(this).off("paste cut keydown keyup focus blur")
		.bind("paste cut keydown keyup focus blur", function() {
			var height, style = this.style;
			this.style.height = '98%';
			if (this.scrollHeight > 50) {
				height = this.scrollHeight;
				style.overflowY = 'hidden';
				style.height = height + 'px';
			}else{
				style.overflowY = 'hidden';
			}
		});
	});
	
	
});

//页面加载完毕
function webLoadingFish(){//android
	addFormData.isLoadingFish();
}
//页面加载完毕
function webLoadingFishIos(){//ios
	window.webkit.messageHandlers.webLoadingFishIos.postMessage();
}
//初始化页面数据信息
function initSpFlowInstance(){//android
	addFormData.initSpFlowInstance('${spFlowInsStr}');
}
//初始化页面数据信息
function initSpFlowInsforIos(){//ios
	window.webkit.messageHandlers.initSpFlowInsforIos.postMessage('${spFlowInsStr}');
}
var appWay = null;
function initAppWay(appWayParam){
	appWay = appWayParam;
}

var needCheckState;
function checkSpFlowInfo(way,needCheck){
	appWay = way;
	needCheckState = needCheck;
	$("body").trigger("checkSpFlowInfo","#form-info-div");
}

function checkSpFlow(result){//android
	addFormData.checkSpFlow(result);
}
function checkSpFlowIos(result){
	window.webkit.messageHandlers.checkSpFlowIos.postMessage(result);
	//return result;
}

/**
 * isUnique true 单选 false多选
 * fieldId 控件
 */
function selectUser(isUnique,fieldId){
	if(appWay && appWay=='ios'){
		//人员选择
		window.webkit.messageHandlers.addStaffBtnTapped.postMessage('{"isUnique":'+isUnique+',"fieldId":'+fieldId+'}');
	}else{
		//人员选择
		addFormData.selectUser(isUnique,fieldId);
	}
}
/**
 * isUnique true 单选 false多选
 * state  1跨团队部门选择 2当前团队部门选择
 * fieldId 控件
 */
function selectDep(isUnique,fieldId){
	if(appWay && appWay=='ios'){
		//部门选择
		window.webkit.messageHandlers.addDepBtnTapped.postMessage('{"isUnique":'+isUnique+',"fieldId":'+fieldId+'}');
	}else{
		addFormData.selectDep(isUnique,fieldId);
	}
}
//关联模块选择
function relateModChoose(relateType,isUnique,fieldId){
	if(appWay && appWay=='ios'){
		//部门选择
		window.webkit.messageHandlers.relateModChoose.postMessage('{"relateType":"'+relateType+'","isUnique":'+isUnique+',"fieldId":'+fieldId+'}');
	}else{
		addFormData.relateModChoose(relateType,isUnique,fieldId);
	}
}
/**
 * 关联数据选择
 */
function relateTableChoose(relatetable,subformid){
	var isUnique = "false";
	if(appWay && appWay=='ios'){
		//人员选择
		window.webkit.messageHandlers.relateTableChoose.postMessage('{"relatetable":'+relatetable+',"isUnique":'+isUnique+',"subformid":'+subformid+'}');
	}else{
		//人员选择
		addFormData.relateTableChoose(relatetable,isUnique,subformid);
	}
}
/**
 * 选取人员返回
 */
function selectUserResult(app,userJson,fieldId){
	var obj = eval('(' + userJson + ')');
	var _this = $("span[easywincompon='Employee'][fieldid='"+fieldId+"'] .userSelect");
	var _spanUserMod = $("<span class='text tempUser tempShowData'></span>")
	$(_this).parent().find(".tempUser").remove();
	$.each(obj,function(optIndex,optObj){
		var userId = optObj.id
		var userName = optObj.userName;
		
		var _spanUser =  $(_spanUserMod).clone().html(userName);
		_spanUser.append("<br/>");
		var tempShowData = {};
		tempShowData.optionId=userId
		tempShowData.content=userName;
		$(_spanUser).data("tempShowData",tempShowData);
		
		$(_this).before(_spanUser)
	})
}
/**
 * 选取部门返回
 */
function selectDepResult(app,depJson,fieldId){
 	var obj = eval('(' + depJson + ')');
	var _spanDepMod = $("<span class='text tempDep tempShowData'></span>")
	var _this = $("span[easywincompon='Department'][fieldid='"+fieldId+"']").find(".depSelect");
	$(_this).parent().find(".tempDep").remove();
	$.each(obj,function(optIndex,optObj){
		var depId = optObj.id
		var depName = optObj.depName;
		var _spanDep =  $(_spanDepMod).clone().html(depName);
		_spanDep.append("<br/>");
		var tempShowData = {};
		tempShowData.optionId = depId
		tempShowData.content = depName;
		$(_spanDep).data("tempShowData",tempShowData);
		
		$(_this).before(_spanDep)
	})  
}
//关联模块选择
function selectRelateResult(busType,app,relateJson,fieldId){
	if(busType=='005'){//选择项目
		var items = eval('(' + relateJson + ')');
		if (items && items.length > 0) {
			var _this = $("span[easywincompon='RelateItem'][fieldid='"+fieldId+"']").find(".relateModSelect");
			if(!_this || !_this.get(0)){
				_this = $("span[easywincompon='RelateMod'][fieldid='"+fieldId+"']").find(".relateModSelect");
			}
            $(_this).parent().find(".tempItem").remove();
            var _spanItemMod = $("<span class='text tempItem tempShowData'></span>")
            $.each(items, function (optIndex, item) {
                var itemId = item.id;
                var itemName = item.itemName;
                var _spanItem = $(_spanItemMod).clone().html(itemName);

                var tempShowData = {};
                tempShowData.optionId = itemId
                tempShowData.content = itemName;
                $(_spanItem).data("tempShowData", tempShowData);

                $(_this).before(_spanItem)
            })
        }
	}else if(busType=='012'){//选择客户
		var crms = eval('(' + relateJson + ')');
		var _this = $("span[easywincompon='RelateCrm'][fieldid='"+fieldId+"']").find(".relateModSelect");
		if(!_this || !_this.get(0)){
			_this = $("span[easywincompon='RelateMod'][fieldid='"+fieldId+"']").find(".relateModSelect");
		}
		$(_this).parent().find(".tempCrm").remove();
		
		if (crms && crms[0]) {
			var _spanItemMod = $("<span class='text tempCrm tempShowData'></span>")
	        $.each(crms, function (optIndex, crm) {
	            var crmId = crm.id;
	            var crmName = crm.customerName;
	
	            var _spanCrm = $(_spanItemMod).clone().html(crmName);
	
	            var tempShowData = {};
	            tempShowData.optionId = crmId
	            tempShowData.content = crmName;
	            $(_spanCrm).data("tempShowData", tempShowData);
	
	            $(_this).before(_spanCrm)
	        })
		}
	}
}
/**
 * 返回的结果数据
 */
function relateTableResult(relatetable,app,relateDataJson,subformid){
	var datas = eval('(' + relateDataJson + ')');
	
	var subTableBody = $("body").find("[subformid='"+subformid+"']").find(".optAddDataTable").parents(".subTableHead").next();
	 
    var subTableTr = $(subTableBody).find(".subTableTr:eq(0)").clone();
    $(subTableTr).removeAttr("busTableId");
    $(subTableTr).removeAttr("modTr");
    $(subTableTr).find(".tdItem").html('');
    
    var sublayoutDetails = $(subTableBody).data("sublayoutDetail");
    //构建选取关联数据
    EasyWinForm.optSubDataTable(datas,relatetable,subTableBody,$(subTableTr));
}

//关联模块选择
function toastMsg(msg){
	if(appWay && appWay=='ios'){
		//
		window.webkit.messageHandlers.toastMsg.postMessage('{"msg":"'+msg+'"}');
	}else{
		addFormData.toastMsg(msg);
	}
}
//展示审批信息
function viewSpFlowDetail(appWay,instanceId){
	if(appWay && appWay=='ios'){
		window.webkit.messageHandlers.viewSpFlowDetail.postMessage('{"instanceId":"'+instanceId+'"}');
	}else{
		addFormData.viewSpFlowDetail(instanceId);
	}
}
//展示累计借款信息
function showListLoan(appWay,listLoanArray){
	var listLoan = JSON.stringify(listLoanArray);
	if(appWay && appWay=='ios'){
		window.webkit.messageHandlers.showListLoan.postMessage(listLoan);
	}else{
		addFormData.showListLoan(listLoan);
	}
}

function showListLoanOff(appWay,listLoanOffArray){
	var listLoanOff = JSON.stringify(listLoanOffArray);
	if(appWay && appWay=='ios'){
		window.webkit.messageHandlers.showListLoanOff.postMessage(listLoanOff);
	}else{
		addFormData.showListLoanOff(listLoanOff);
	}
}
</script>
<style type="text/css">
input[type="radio"],input[type="checkbox"] {
    opacity: 0 !important;
    width: 15px !important;
    height: 15px !important;
}
.webuploader-pick{
	height: 30px
}
.alert{
	border-color: #fbfbfb !important;
	background: #fbfbfb !important;
	color: #262626 !important;
}
texearea{
	resize: none !important;
	border: 1px solid #DDD !important;
}
input[type="text"] {
	border: 1px solid #DDD !important;
}
.subTableHead .tdItem,.subTableBody .tdItem,.subTableTail .tdItem{
	display:table-cell;
	min-height:30px;
	border-left: 1px solid #000;
	border-bottom:1px solid #000;
	text-align:center;
	vertical-align: middle;
}
.subTableBody .tdItem{
	padding: 5px 5px
}

.subTableHead .tdItem:FIRST-CHILD,.subTableBody .tdItem:FIRST-CHILD,.subTableTail .tdItem{
	border-left:0;
}
p{
margin: 5px 0px;
}
input,textarea{
	margin:3px 0 !important;
}
input[type="text"] {
	border: 1px solid #DDD !important;
	height:28px !important;
	min-width:30px;
	padding:0;
}
.loanApply2View{
	cursor:pointer;
}
.loanRep2View{
	cursor:pointer;
}
.tempCrm,.tempItem{
	display:inline-block;
	margin-top:3px;
	border-radius: 2px!important;
    background-clip: padding-box!important;
}
.inlineblock{
	display:inline-block;
}
</style>
</head>
<body>
<form id="subform" class="subform" method="post">
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="form-preview module-detail-view writeform-content margin-top-40 bg-white" 
                     id="contentBody" style="overflow: hidden;overflow-y:scroll;width: 100%">
                     <div class="wform-title bg-white margin-top-10 margin-bottom-20">
							<span class="clear-fix">标题：</span> 
							<input id="form-name" type="text" class="clear-fix" style="width: 550px;padding: 5px 5px"value="${spFlowInstance.flowName}"
							v-message-name="上报名称" v-maxlength="200">
					</div>
                     <div id="divScope">
                     	<div id="formInfo"></div>
						<!--固定表头-->
						<div id="form-info-div" class="detail-block margin-top-20 margin-left-10" style="width:100%;clear: both;">
						</div>
						
						<div class="js-biaoge-form form-border-view form-view form-view_js" 
						id="formpreview" style="display: none;width: 95%;margin-left: 18px">
						</div>
                     </div>
						
					</div>
					
				</div>
			</div>
		</div>
	</form>
	<script type="text/javascript">
	var sid='${not empty param.auth_key?param.auth_key:param.sid}';
	var formTag = 'addForm';
	var EasyWin={
			"userInfo":{
				"id":"${userInfo.id}",
				"username":"${userInfo.userName}",
				"department":{"name":"${userInfo.depName}","id":"${userInfo.depId}"}
			},
			"sourcePage":"mobile","nowTimeLong":"${nowTimeLong}","sid":sid,
			"spFlow":{"creator":${spFlowInstance.creator},"preStageItemId":${spFlowInstance.stagedItemId},
				"busId":"${spFlowInstance.busId}","busType":"${spFlowInstance.busType}","preFlowName":"${spFlowInstance.flowName}",
				"busName":"${spFlowInstance.busName}","stagedItemId":${spFlowInstance.stagedItemId},
				"stagedItemName":"${spFlowInstance.stagedItemName}"}
	}
	var  formKey = "${spFlowInstance.formKey}";
	var FLOWINFO={"formKey":"${spFlowInstance.formKey}","instanceId":"${spFlowInstance.id}",
			"layoutId":"${spFlowInstance.layoutId}","flowId":"${spFlowInstance.flowId}","saveType":"add"};
	</script>
	
</body>
</html>