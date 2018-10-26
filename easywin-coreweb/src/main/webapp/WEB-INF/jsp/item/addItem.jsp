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
var sid="${param.sid}";
var pageParam= {
    "sid":"${param.sid}"
}
var progressRowNum=2;
var vali;
	$(function() {
		//添加默认进度
		addDefaltProgress();
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		vali = $(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			},
			datatype:{
				"input":function(gets,obj,curform,regxp){
					var str = $(obj).val();
					if(str){
						var count = str.replace(/[^\x00-\xff]/g,"**").length;
						var len = $(obj).attr("defaultLen");
						if(count>len){
							return "项目名称太长";
						}else{
							return true;
						}
					}else{
						return false;
					}
				},
				"amount":function(gets,obj,curform,regxp){
					var str = $(obj).val();
					if(str){
						var test = /^-?\d+\.?\d{0,2}$/;
						if(!test.test(str)){
							return "请填写数字（最多2位小数）！";
						}else{
							return true;
						}
					}else{
						return true;
					}
				}
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		
		
		//列出常用人员信息
        listUsedUser(5,function(data){
            if(data.status=='y'){
                var usedUser = data.usedUser;
                $.each(usedUser,function(index,userObj){
                    //添加头像
                    var headImgDiv = $('<div class="ticket-user pull-left other-user-box usedUser"></div>');
                    $(headImgDiv).data("userObj",userObj);
                    var imgObj = $('<img src="/downLoad/userImg/${userInfo.comId}/'+userObj.id+'" class="margin-left-5 usedUserImg"/>');
                    $(imgObj).attr("title",userObj.userName)
                    var headImgName = $('<span class="user-name2" style="font-size:6px;display:inline-block"></span>');
                    $(headImgName).html(userObj.userName);

                    $(headImgDiv).append($(imgObj));
                    $(headImgDiv).append($(headImgName));

                    $("#usedUserDiv").append($(headImgDiv));
                })
            }
        });
		
		$("body").on("click",".usedUser",function(){
			var usedUser = $(this).data("userObj");
			setUser('owner','onwerName',usedUser.id,$(this));
		})
	})
//项目组关联
function initItemGroup(groups){
	var grp = eval(groups);
	var grpName="";
	for(var i=0;i<grp.length;i++){
		
		grpName +="<span id=\"itemGrp_"+grp[i].id+"\" style='cursor:pointer;' title=\"双击移除\" class=\"label label-default  margin-top-5 margin-left-5 margin-bottom-5\" ondblclick=\"removeItemGrp('"+grp[i].id+"')\">"+grp[i].name;
		grpName +="<input type=\"hidden\" name=\"listShareGroup["+i+"].id\" value=\""+grp[i].id+"\"/>";
		grpName +="</span>";
		
	}
	$("#itemGroupName").html(grpName);
	vali.addRule();
}
//移除项目的共享分组
function removeItemGrp(grpId){
	removeObjByKey("itemGrp_"+grpId)
	$("#selfGrouplist").find("option[value='"+grpId+"']").remove();
}

$(function(){
	$("#itemName").keydown(function(event){	
		if(event.keyCode==13) {
			return false;
		}
	});
	// 验证与验证项目名称相似的项目
	$("#itemName").keyup(function(){
		if(!strIsNull($("#itemName").val())){
			$("#itemName").parent().find("div").css("display","none")
			$.post("/item/checkSimilarItemByItemName?sid=${param.sid}",{Action:"post",itemName:$("#itemName").val()},     
			 function (itemNum){
				if(itemNum > 0){
					$("#addItemWarm").html("<a href='javascript:void(0);' style='background:none;width:80px;color:#ff0000;' onclick='similarItemsPage();'>相似项目("+itemNum+")</a>");
				}else{
					$("#addItemWarm").text("");
				}
			});
		}else{
			$("#addItemWarm").text("");
			$("#itemName").parent().find("div").css("display","block")
		}
	});

	//保存并查看
	$("#addItem").click(function (){
		var list = [];
		for (var i = 0; i <= progressRowNum; i++) {
			var progressOrder = $("input[name='listItemProgress["+ i +"].progressOrder']").val();
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
		$("#way").val("view");
		$(".subform").submit();
	});
	$("#addAndviewItem").click(function (){
		$("#way").val("add");
		$(".subform").submit();
	});

});
//弹窗显示相似项目
function similarItemsPage(){
	var url = "/item/similarItemsPage?pager.pageSize=8&sid=${param.sid}&itemName="+$("#itemName").val();
	window.top.layer.open({
		 type: 2,
		  //title: ['相似项目列表', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['580px', '500px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: [url,'no'],
		  success: function(layero,index){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
			  })
		  }
	});
}
//关联客户返回函数
function crmSelectedReturn(crmId,name){
	var oldId = $("#partnerId").val();
	if(oldId!=crmId ){//不是同一个项目则阶段主键就不一样,且不是自己
		if(!strIsNull(crmId)){
			//项目来源关联
			$("#partnerName").val(name);
			$("#partnerId").val(crmId);
		}
	}
}
//移除客户关联
function removeCrm(){
	var crmId = $("#partnerId").val();
	if(crmId){
		//项目来源关联
		$("#partnerName").val('');
		$("#partnerId").val('');
	}
}

//移除产品关联
function removePro(){
	$("#productId").val('');
	$("#productName").val('');
	$("#productManager").val('');
}

//人员范围的显示隐藏
function showMans(){
	
	if($("#pubState").val() == 0){
		$("#mans").show();
	}else{
		$("#mans").hide();
	}
	
}
//显示模板进度
function showProgress(obj){
	 $(".templateProgress").remove();
	id = obj.value;
	if(id && id != "0"){
		$.post("/itemProgress/queryItemProgressTemplateById?sid=${param.sid}",{Action:"post",id:id},     
				function (data){
			if(data.data){
				progressRowNum = 0;
				for (var i = 0; i < data.data.listsItemTemplateProgress.length; i++) {
					var html = '<li class="ticket-item no-shadow ps-listline templateProgress" style="border-bottom: 0">'+
					'										    <div class="pull-left gray ps-left-text">'+
					'										    	&nbsp;'+
					'										    </div>'+
					'											<div class="pull-left">'+
					'												<input placeholder="阶段名" datatype="*" value="'+data.data.listsItemTemplateProgress[i].progressName+'" name="listItemProgress['+progressRowNum+'].progressName" class="colorpicker-default form-control" type="text" style="width: 200px;float: left" >'+
					'											</div>'+
					'											<div class="pull-left" style="margin-left: 20px;">'+
					'												<input placeholder="排序号" datatype="pn" value="'+data.data.listsItemTemplateProgress[i].progressOrder+'"  name="listItemProgress['+progressRowNum+'].progressOrder" class="colorpicker-default form-control" type="text" style="width: 200px;float: left">'+
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
					$("#progress_-1").before(html);
					progressRowNum = progressRowNum+1;
				}
			}
		},"json");
	}else{
		addDefaltProgress();
	}
	
}

//添加进度
function addProgress(a) {
		var html = '<li class="ticket-item no-shadow ps-listline templateProgress" style="border-bottom: 0">'+
		'										    <div class="pull-left gray ps-left-text">'+
		'										    	&nbsp;'+
		'										    </div>'+
		'											<div class="pull-left">'+
		'												<input placeholder="阶段名" datatype="*"  name="listItemProgress['+progressRowNum+'].progressName" class="colorpicker-default form-control" type="text" style="width: 200px;float: left" >'+
		'											</div>'+
		'											<div class="pull-left" style="margin-left: 20px;">'+
		'												<input placeholder="排序号" datatype="pn"  name="listItemProgress['+progressRowNum+'].progressOrder" class="colorpicker-default form-control" type="text" style="width: 200px;float: left">'+
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

//添加默认进度
function addDefaltProgress() {
		var html = '<li class="ticket-item no-shadow ps-listline templateProgress" style="border-bottom: 0">'+
		'										    <div class="pull-left gray ps-left-text">'+
		'										    	&nbsp;'+
		'										    </div>'+
		'											<div class="pull-left">'+
		'												<input placeholder="阶段名" datatype="*"  name="listItemProgress['+progressRowNum+'].progressName" class="colorpicker-default form-control" type="text" style="width: 200px;float: left" >'+
		'											</div>'+
		'											<div class="pull-left" style="margin-left: 20px;">'+
		'												<input placeholder="排序号" datatype="pn"  name="listItemProgress['+progressRowNum+'].progressOrder" class="colorpicker-default form-control" type="text" style="width: 200px;float: left">'+
		'											</div>'+
		'											<div class="pull-left" style="margin-left: 20px;">'+
		'												<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delLi(this)" title="删除阶段"></a>'+
		'											</div>'+
		'											<div class="pull-left" style="margin-left: 20px;">'+
		'				                               <a class="ps-point btn-a-full" title="添加" onclick="addProgress(this)">'+
		'				                                	<i class="fa green fa-plus"></i>'+
		'				                               </a>'+
		'											</div>'+
		'				                  		</li>'+
		'<li class="ticket-item no-shadow ps-listline templateProgress" style="border-bottom: 0">'+
		'										    <div class="pull-left gray ps-left-text">'+
		'										    	&nbsp;'+
		'										    </div>'+
		'											<div class="pull-left">'+
		'												<input placeholder="阶段名" datatype="*"  name="listItemProgress['+(progressRowNum+1)+'].progressName" class="colorpicker-default form-control" type="text" style="width: 200px;float: left" >'+
		'											</div>'+
		'											<div class="pull-left" style="margin-left: 20px;">'+
		'												<input placeholder="排序号" datatype="pn"  name="listItemProgress['+(progressRowNum+1)+'].progressOrder" class="colorpicker-default form-control" type="text" style="width: 200px;float: left">'+
		'											</div>'+
		'											<div class="pull-left" style="margin-left: 20px;">'+
		'												<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delLi(this)" title="删除阶段"></a>'+
		'											</div>'+
		'											<div class="pull-left" style="margin-left: 20px;">'+
		'				                               <a class="ps-point btn-a-full" title="添加" onclick="addProgress(this)">'+
		'				                                	<i class="fa green fa-plus"></i>'+
		'				                               </a>'+
		'											</div>'+
		'				                  		</li>'
	$("#progress_-1").before(html);
	progressRowNum = progressRowNum+2;
}

//删除选中的li
function delLi(clickLi){
	var li = $(clickLi).parent().parent();
	if(li.attr("class").indexOf("templateProgress") != -1 && $(".templateProgress").length == 2){
		addProgress(clickLi);
		li.remove();
	}else{
		li.remove();
	}
}

//产品选择
function proSelect() {
	productSelect('1','productId',function(product){
		productCallback(product[0])
	});
}
//产品回调
function productCallback(obj) {
	$("#productId").val(obj.id);
	$("#productName").val(obj.name+"_V"+obj.version);
	$("#productManager").val(obj.managerName);
}

</script>
</head>
<body>
<form class="subform" method="post" action="/item/addItem">
<tags:token></tags:token>
<input type="hidden" name="redirectPage" value="${param.redirectPage}"/>
<input type="hidden" name="parentId" value="-1"/>
<input type="hidden" name="attentionState" id="attentionState" value="0"/>
<input type="hidden" name="way" id="way">
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">新建项目</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" onclick="setAtten(this)">
								<i class="fa fa-star-o"></i>关注
							</a>
							<a href="javascript:void(0)" class="blue" id="addItem">
								<i class="fa fa-save"></i>添加
							</a>
							<!-- 
								<a href="javascript:void(0)" class="blue" id="addAndviewItem">
									<i class="fa fa-save"></i>继续添加
								</a>
							 -->
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
										    	项目名称<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="itemName" datatype="input,sn" defaultLen="52" name="itemName" nullmsg="请填写项目名称" 
													class="colorpicker-default form-control" type="text" value="" style="width: 400px;float: left">
													<span id="msgTitle" style="float:left;width: auto;">(0/26)</span>
												</div>
												<div class="pull-left">
													<span id="addItemWarm" style="float:left;margin-left:2px;"></span>
												</div>
											</div>
											<script> 
												//当状态改变的时候执行的函数 
												function handleName(){
													var value = $('#itemName').val();
													var len = charLength(value);
													if(len%2==1){
														len = (len+1)/2;
													}else{
														len = len/2;
													}
													if(len>26){
														$('#msgTitle').html("(<font color='red'>"+len+"</font>/26)");
													}else{
														$('#msgTitle').html("("+len+"/26)");
													}
												} 
												//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
												if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
													document.getElementById('itemName').onpropertychange=handleName 
												}else {//非ie浏览器，比如Firefox 
													document.getElementById('itemName').addEventListener("input",handleName,false); 
												} 
											</script>  
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	项目经理<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="min-width: 135px">
												<tags:userOne datatype="s" name="owner" defaultInit="true" comId="${userInfo.comId}"
													showValue="${userInfo.userName}" value="${userInfo.id}" onclick="true"
													showName="onwerName"></tags:userOne>
													
												<div id="usedUserDiv" style="width: 450px;display: inline-block;">
													<span class="pull-left" style="padding-top:8px;display: inline-block;padding-left: 10px">常用人员:</span>
												</div>
											</div>
                                        </li>
                                        <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	研发负责人
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="min-width: 135px">
												<tags:userOne name="developLeader" showValue="" value="" showName="developLeaderName" onclick="true" comId="${userInfo.comId}"></tags:userOne>
											</div>
											
                                        </li>
                                        <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	所属产品
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input type="hidden" id="productId" name="productId" value=""/>
												<div style="float: left">
													<input id="productName" name="productName" class="colorpicker-default form-control pull-left" type="text" 
													ondblclick="removePro()" readonly style="cursor:auto;width: 250px;float:left" value="" title="双击移除">
													<a href="javascript:void(0)" class="fa fa-chain pull-left" style="float:left;margin-top: 10px;font-size: 18px;padding-left: 10px"  
													onclick="proSelect();"></a>
												</div>
											</div>  
										    <div class="ticket-user pull-right ps-right-box" style="min-width: 180px;margin-right: 100px;" >
												<input type="text" style="width: 180px;" id="productManager" readonly>
											</div>
											<div class="pull-right gray ps-right-text" style="margin-right: 20px;">
										    	产品经理
										    </div>
                                        </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	关联客户
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input type="hidden" id="partnerId" name="partnerId" value=""/>
												<div style="float: left">
													<input id="partnerName" name="partnerName" class="colorpicker-default form-control pull-left" type="text" 
													ondblclick="removeCrm()" readonly style="cursor:auto;width: 250px;float:left" value="" title="双击移除">
													<a href="javascript:void(0)" class="fa fa-chain pull-left" style="float:left;margin-top: 10px;font-size: 18px;padding-left: 10px"  
													onclick="listCrm('${param.sid}','partnerId');"></a>
												</div>
											</div>               
                                        </li>
                                        <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	项目金额
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="min-width: 250px" >
												<input type="text" style="width: 250px;" name="amount" datatype="amount">&nbsp;万
											</div>
											<div class="ticket-user pull-right ps-right-box" style="min-width: 180px;margin-right: 100px;">
												<input type="text" class="form-control" placeholder="服务期限" readonly="readonly" id="serviceDate" name="serviceDate" onclick="WdatePicker({})">
											</div>
											 <div class="pull-right gray ps-right-text" style="margin-right: 20px;">
										    	服务期限
										    </div>
                                        </li>
                                        <li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		项目备注
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;" id="itemRemark" name="itemRemark" rows="" cols=""></textarea>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                          <li class="ticket-item no-shadow ps-listline" style="border-bottom: 0">
										    <div class="pull-left gray ps-left-text">
										    	项目进度<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
											  		<select datatype="*" class="populate" onchange="showProgress(this)" style="width: 135px;">
						 								<option value="0">自定义</option>
						 								<c:forEach items="${listTemplates}" var="obj" varStatus="vs">
						 									<option value="${obj.id }">${obj.templateName}</option>
						 								</c:forEach>
													</select>
												</div>
											</div>
                                         </li>
                                         <li id="progress_-1"  class="ticket-item no-shadow ps-listline" style="min-height: 10px !important;"></li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	阅读权限
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
											  		<select class="populate" id="pubState" name="pubState" onchange="showMans()" style="width: 135px;">
					 								<option value="0">私有</option>
					 								<option value="1">公开</option>
														</select>
												</div>
											</div>
                                         </li>
                                         
                                         <li class="ticket-item no-shadow autoHeight no-padding" style="display: block;" id="mans">
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		分享人
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="height: auto;">
												<div class="pull-left gray ps-left-text padding-top-10">
											  		<tags:userMore name="listItemSharer.userId" showName="userName" disDivKey="itemSharor_div"></tags:userMore>
												</div>
											</div>
											<div class="ps-clear"></div>
                                         </li>
										<li class="ticket-item no-shadow autoHeight no-padding">
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		相关附件
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
												<div class="margin-top-10">
											  		<tags:uploadMore name="listUpfiles.id" showName="filename" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
												</div>
											</div> 
											<div class="ps-clear"></div>              
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
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
