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

var sid = '${param.sid}'
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
var valid;
var list = [];
var flag = 0;
$(function() {
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	valid = $(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsgV2(msg, o, cssctl);
		},
		callback:function (form){
			//提交前验证是否在上传附件
			return sumitPreCheck(null);
		},
		datatype:{
			"area":function(gets,obj,curform,regxp){
				if(!strIsNull($("#areaIdAndType").val())){
					return true;
				}else{
					return false;
				}
			},
			"input":function(gets,obj,curform,regxp){
				var str = $(obj).val();
				
				if(str){
					var count = str.replace(/[^\x00-\xff]/g,"**").length;
					var len = $(obj).attr("defaultLen");
					if(count>len){
						return "客户名称太长";
					}else{
						return true;
					}
				}else{
					return false;
				}
			},
			"customerType":function(gets,obj,curform,regxp){
				if(!strIsNull($("#customerTypeId").val())){
					return true;
				}else{
					return false;
				}
			},
			"stage":function(gets,obj,curform,regxp){
				if($("#stage").val()){
					return true;
				}else{
					return false;
				}
			},
			"float":function(gets,obj,curform,regxp){
				if(isFloat($("#budget").val())){
					return true;
				}else{
					return "请填入正确的数字";
				}
			},
		},
		showAllError : true
	});
	
	//相似客户数量
	$("#customerName").keyup(function(){
		if(!strIsNull($("#customerName").val())){
			$("#customerName").parent().find("div").css("display","none")
			$.post("/crm/checkSimilarCrmByName?sid=${param.sid}",{Action:"post",crmName:$("#customerName").val()},     
			 function (crmNum){
				if(crmNum > 0){
					$("#addCrmWarm").html("<a href='javascript:void(0);' style='background:none;width:80px;color:#ff0000;' onclick='similarCrmsPage();'>相似客户("+crmNum+")</a>");
				}else{
					$("#addCrmWarm").text("");
				}
			});
		}else{
			$("#customerName").parent().find("div").css("display","block")
			$("#addCrmWarm").text("");
		}
	});
	
	//form表单提交
	$("#addCrm").click(function(){
		//添加完成后查看
		$("#way").val("view");
		$(".subform").submit();
	});
	//form表单提交
	$("#addAndviewCrm").click(function(){
		//添加完成后继续添加
		$("#way").val("add");
		$(".subform").submit();
	});
});
//重写回调函数为空
function artOpenerCallBack(args){}

var linkManRowNum = 1;
//添加联系人
function addLinkMan(){
	//联系人添加
	$("#oneMoreLinkMan").text("再添加一个");;
    var tr="<tr>";
    tr +="<td><input disabled class=\"table_input\" type=\"text\" nullmsg=\"请输入联系人\" id=\"listLinkMan["+linkManRowNum+"].linkManName\" name=\"listLinkMan["+linkManRowNum+"].linkManName\" /></td>";
    tr +="<td><input disabled class=\"table_input\" type=\"text\" id=\"listLinkMan["+linkManRowNum+"].post\" name=\"listLinkMan["+linkManRowNum+"].post\" /></td>";
    tr +="<td><input disabled class=\"table_input\" type=\"text\" ignore='ignore' id=\"listLinkMan["+linkManRowNum+"].linePhone\" name=\"listLinkMan["+linkManRowNum+"].linePhone\" /></td>";
    tr +="<td><a href=\"javascript:void(0)\" class=\"fa fa-times-circle-o\" onclick=\"delTr(this)\" title=\"删除本行\"></a></td>";
    tr +="</tr>";
    $("#linkManTable tbody").find("tr").last().after(tr);
	valid.addRule();
	linkManRowNum = linkManRowNum+1;
}
//删除联系人
function delTr(clickTd,olmId){ 
	var trLen = $("#linkManTable tbody").find("tr").length;
	var tr = $(clickTd).parent().parent().parent().find("tr[olmId='"+olmId+"']"); 
    tr.remove();  
    if(olmId){
    	list.splice($.inArray(olmId,list),tr.length);
    }
	showTable()
}
//设置客户共享组
function initCustomerGroup(groups){
	var grp = eval(groups);
	var grpName="";
	for(var i=0;i<grp.length;i++){
		grpName +="<span id=\"crmGrp_"+grp[i].id+"\" style='cursor:pointer;' title=\"双击移除\" class=\"label label-default margin-top-5 margin-left-5 margin-bottom-5\" ondblclick=\"delCrmShareGroup('"+grp[i].id+"')\">"+grp[i].name;
		grpName +="<input type=\"hidden\" name=\"listShareGroup["+i+"].id\" value=\""+grp[i].id+"\"/>";
		grpName +="</span>";
		
	}
	$("#customerGroup_div").html(grpName);
}
//弹窗显示相似项目
function similarCrmsPage(){
	
	var url = "/crm/similarCrmsPage?pager.pageSize=8&sid=${param.sid}&crmName="+$("#customerName").val()+"&redirectPage="+encodeURIComponent(window.location.href);
	window.top.layer.open({
		 type: 2,
		  //title: ['相似客户列表', 'font-size:18px;'],
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
//移除客户分享组数据
function delCrmShareGroup(groupId){
	removeObjByKey("crmGrp_"+groupId);
	$("#selfGrouplist").find("option[value='"+groupId+"']").remove();
}

var oldList=[];
//选择联系人后添加到页面
function addOlm(result){
	if(list[0]){
		for(var j=0;j<list.length;j++){
			oldList.push(list[j]);
		}
	}
	for(var i=0;i<result.length;i++){
		var colm =  JSON.parse(result[i]);
		$.post("/outLinkMan/queryOlmForShow?sid=${param.sid}",{id:colm.id},     
				function (msgObjs){
				if(msgObjs.status=='y' && msgObjs.lists[0]){
			    	for (var i = 0; i < msgObjs.lists.length; i++) {
			    		var olm = msgObjs.lists[i];
			    		if(list.length > 0){
			    			var count = 0;
			    			for(var j=0;j<list.length;j++){
			    				if(list[j] == olm.id){
			    					count++;
			    				}
			    			}
			    			if(count < msgObjs.lists.length){
			    				list.push(olm.id);
			    				//添加数据到页面
			    				$("#oneMoreLinkMan").text("再添加一个");;
			    			    var tr='<tr olmId="'+olm.id+'">';
			    			    tr +="<td>"+'<a href="javascript:void(0);" onclick="viewOlm('+olm.id +');">'+olm.linkManName+'</a>'+" <input  type=\"hidden\" id=\"listLinkMan["+linkManRowNum+"].id\" name=\"listLinkMan["+linkManRowNum+"].id\" value ='"+ (olm.id==null?"":olm.id) +"'/></td>";
			    			    tr +="<td><input style=\"border: 0;\" disabled class=\"table_input\" type=\"text\" id=\"listLinkMan["+linkManRowNum+"].post\" name=\"listLinkMan["+linkManRowNum+"].post\" value ='"+ (olm.post==null?"":olm.post) +"'/></td>";
			    			    tr +="<td>"+ (olm.contactWayValue==null?"":(olm.contactWayValue+"："+olm.contactWay)) +"</td>";
			    			    tr +="<td><a href=\"javascript:void(0)\" style=\"color: red;\"  class=\"fa fa-times-circle-o\" onclick=\"delTr(this,"+olm.id+")\" title=\"删除本行\"></a></td>";
			    			    tr +="</tr>";
			    			   
			    			    $("#linkManTable tbody").find("tr").last().after(tr);
			    				valid.addRule();
			    				linkManRowNum = linkManRowNum+1;
			    			}
			    		}else{
			    			list.push(olm.id);
			    			//添加数据到页面
			    			$("#oneMoreLinkMan").text("再添加一个");;
			    		    var tr='<tr olmId="'+olm.id+'">';
			    		    tr +="<td>"+'<a href="javascript:void(0);" onclick="viewOlm('+olm.id +');">'+olm.linkManName+'</a>'+" <input  type=\"hidden\" id=\"listLinkMan["+linkManRowNum+"].id\" name=\"listLinkMan["+linkManRowNum+"].id\" value ='"+ (olm.id==null?"":olm.id) +"'/></td>";
			    		    tr +="<td><input style=\"border: 0;\" disabled class=\"table_input\" type=\"text\" id=\"listLinkMan["+linkManRowNum+"].post\" name=\"listLinkMan["+linkManRowNum+"].post\" value ='"+ (olm.post==null?"":olm.post) +"'/></td>";
			    		    tr +="<td>"+ (olm.contactWayValue==null?"":(olm.contactWayValue+"："+olm.contactWay)) +"</td>";
			    		    tr +="<td><a href=\"javascript:void(0)\" style=\"color: red;\"  class=\"fa fa-times-circle-o\" onclick=\"delTr(this,"+olm.id+")\" title=\"删除本行\"></a></td>";
			    		    tr +="</tr>";
			    		   
			    		    $("#linkManTable tbody").find("tr").last().after(tr);
			    			valid.addRule();
			    			linkManRowNum = linkManRowNum+1;
			    		}
					}
				}
			},"json");
		
		
	}
	setTimeout(function() {
		showTable()
		//合并行
		olmFormRowSpan($("#linkManTable"))
	}, 200);
	
}

//外部联系人表格显示隐藏
function showTable() {
	if(list.length>0){
		$("#linkManTable").show();
	}else{
		$("#linkManTable").hide();
	}
}


//选择外部联系人弹框
function showOlm(sid){
	
	//是否需要数据保持
	
	window.top.layer.open({
		 type: 2,
		 title:false,
		 closeBtn:0,
		 area: ['800px', '600px'],
		 fix: true, //不固定
		 maxmin: false,
		 move: false,
		 scrollbar:false,
		 content: ['/outLinkMan/listForCustomer?sid='+sid,'no'],
		 btn: ['选择', '取消'],
		 yes: function(index, layero){
			 var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			 var result = iframeWin.crmSelected();
			 if(result){
				 addOlm(result);
				 window.top.layer.close(index)
			 }
		  }
		,btn2: function(){
			 
		}
	    ,cancel: function(){ 
	    	//右上角关闭回调
	    },success:function(layero,index){
		  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
		  iframeWin.initSelectWay(2,$("#"+sid).find("option"));
			iframeWin.setWindow(window.document,window);
		  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			  window.top.layer.close(index);
		  });
		  //双击选择
			$(iframeWin.document).on("dblclick","tr",function(){
				if(selectWay==1){
                    iframeWin.itemSelectedV2($(this));
                    var result = iframeWin.itemSelected();
                    if(result){
                        callback(result)
                        window.top.layer.close(index)
                    }
				}
			})

	    }
	});
}

//人员范围的显示隐藏
function showMans(){
	
	if($("#pubState").val() == 0){
		$("#mans").show();
	}else{
		$("#mans").hide();
	}
	
}

//外部联系人关联显示隐藏
function showBut(){
	if($("#but").css('display') == 'none'){
		$("#but").css('display','block')
	}else{
		$("#but").css('display','none')
	}
	
}
//合并行数据
function olmFormRowSpan(table){
	var trs = $(table).find("tbody").find("tr");
	if(trs && trs.get(0)){
		var preOlmId = 0;
		var rowSpan = 1;
		$.each(trs,function(index,trObj){
			var olmId = $(trObj).attr("olmId");
			var count = 0
			for (var i = 0; i < oldList.length; i++) {
				if(olmId == oldList[i]){
					count++;
				}
			}
			if(count < 1){
				if(olmId == preOlmId){
					rowSpan = rowSpan+1;
					//开始合并
					var firstTr = $(table).find("tbody").find("tr[olmId='"+olmId+"']:eq(0)");
					$(firstTr).find("td:eq(0)").attr("rowspan",rowSpan);
					$(firstTr).find("td:eq(1)").attr("rowspan",rowSpan);
					$(firstTr).find("td:eq(3)").attr("rowspan",rowSpan);
					
					var td0 = $(trObj).find("td:eq(0)");
					var td1 = $(trObj).find("td:eq(1)");
					var td3 = $(trObj).find("td:eq(3)");
					
					$(td0).remove();
					$(td1).remove();
					$(td3).remove();
				}else{
					preOlmId = olmId;
					rowSpan = 1;
				}
			}
			
		});
		
	}
}
</script>
</head>
<body>
<form class="subform" method="post" action="/crm/addCustomer">
<tags:token></tags:token>
<input type="hidden" name="attentionState" id="attentionState" value="0"/>
<input type="hidden" name="way" id="way">
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">添加客户</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" onclick="setAtten(this)">
								<i class="fa fa-star-o"></i>关注
							</a>
							<a href="javascript:void(0)" class="blue" id="addCrm">
								<i class="fa fa-save"></i>添加
							</a>
							<!-- 
								<a href="javascript:void(0)" class="blue" id="addAndviewCrm">
									<i class="fa fa-save"></i>继续添加
								</a>
							 -->
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 --> 
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">基础信息</span>
                                <div class="widget-buttons btn-div-full">
                                	<a class="ps-point btn-a-full" data-toggle="collapse">
                                    	<i class="fa fa-minus blue"></i>
                                   </a>
                                 </div>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	客户名称<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="customerName" datatype="input,sn" defaultLen="52" name="customerName" nullmsg="请填写客户名称" 
													class="colorpicker-default form-control" type="text" value="" style="width: 400px;float: left">
													<span id="msgTitle" style="float:left;width: auto;">(0/26)</span>
												</div>
												<div class="pull-left">
													<span id="addCrmWarm" style="float:left;margin-left:2px;"></span>
												</div>
											</div>
											<script> 
												//当状态改变的时候执行的函数 
												function handleName(){
													var value = $('#customerName').val();
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
													document.getElementById('customerName').onpropertychange=handleName 
												}else {//非ie浏览器，比如Firefox 
													document.getElementById('customerName').addEventListener("input",handleName,false); 
												} 
											</script>  
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	责任人<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="min-width: 135px">
												<tags:userOne datatype="s" name="owner" defaultInit="true"
													showValue="${userInfo.userName}" value="${userInfo.id}" comId="${userInfo.comId}"
													onclick="true" showName="onwerName"></tags:userOne>
											</div> 
											<c:choose>
												<c:when test="${not empty listUsed}">
													<div>
														<span class="pull-left" style="margin-top:8px">常用人员:</span>
														<c:forEach items="${listUsed}" var="usedUser" varStatus="vs">
															<div class="ticket-user pull-left other-user-box">
																<a href="javascript:void(0);" onclick="setUser('owner','onwerName','${usedUser.id}',this)">
																	<img src="/downLoad/userImg/${usedUser.comId}/${usedUser.id}?sid=${param.sid}"
																		title="${usedUser.userName}" class="usedUserImg margin-left-5"/>
																	<span class="user-name2" style="font-size:6px;">${usedUser.userName}</span>
																</a>
															</div>
														</c:forEach>
													</div>             
												</c:when>
											</c:choose>              
                                        </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear: both">
										    <div class="pull-left gray ps-left-text">
										    	客户区域<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											   <input type="hidden" id="areaIdAndType" name="areaIdAndType"/>
												<div style="float: left">
													<input id="areaName" datatype="area" name="areaName" class="colorpicker-default form-control pull-left" type="text" 
													onclick="areaOne('areaIdAndType','areaName','${param.sid}','1');" readonly style="cursor:auto;width: 200px;float:left" value="" title="双击选择">
													<a href="javascript:void(0)" class="fa fa-map-marker pull-left" style="float:left;margin-top: 10px;font-size: 18px;padding-left: 10px" title="区域选择" 
													onclick="areaOne('areaIdAndType','areaName','${param.sid}','1');"></a>
												</div>
											</div>               
                                          </li>
                                          <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	客户类型<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											   <select class="populate"  datatype="customerType" id="customerTypeId" name="customerTypeId" style="cursor:auto;width: 200px">
													<optgroup label="选择客户类型"></optgroup>
													<c:choose>
														<c:when test="${not empty listCustomerType}">
							 								<c:forEach items="${listCustomerType}" var="customerType" varStatus="status">
							 								<option value="${customerType.id}">${customerType.typeName}</option>
							 								</c:forEach>
														</c:when>
													</c:choose>
												</select>
											</div>               
                                           </li>
                                            <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	所属阶段<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											<div datatype="stage"  class=" pull-left ">
											   <select class="populate"  datatype="stage" id="stage" name="stage" style="cursor:auto;width: 200px">
													<optgroup label="选择所属阶段"></optgroup>
													<c:choose>
														<c:when test="${not empty listCrmStage}">
							 								<c:forEach items="${listCrmStage}" var="stage" varStatus="status">
							 								<option value="${stage.id}">${stage.stageName}</option>
							 								</c:forEach>
														</c:when>
													</c:choose>
												</select>
												</div>
											</div>               
                                           </li>
                                             <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	资金预算(元)<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
													<input id="budget" datatype="float" name="budget" nullmsg="请填写资金预算" 
													class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text">
											    	阅读权限
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												  	<div class="pull-left">
												  		<select class="populate" id="pubState" name="pubState" onchange="showMans()">
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
												  		<tags:userMore name="listCustomerSharer.userId" showName="userName" disDivKey="crmSharor_div"></tags:userMore>
													</div>
												</div>
												<div class="ps-clear"></div> 
	                                         </li>
	                                        <li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		相关附件
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
												<div class="margin-top-10">
											  		<tags:uploadMore name="listUpfiles.upfileId" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
												</div>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                   	</ul>
                                </div>
                            </div>
                          </div>
                          <div class="widget radius-bordered collapsed">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">联系人</span>
                                   <div class="widget-buttons btn-div-full">
                                      <a class="ps-point btn-a-full" data-toggle="collapse" onclick="showBut()">
                                    	<i class="fa fa-plus blue"></i>
                                       </a>
                                   </div>
                                   <button type="button" class="btn btn-info ws-btnBlue"  onclick="showOlm('${param.sid}')" style="margin-top: 3px;margin-right:5% ;display: none;float: right;line-height: 0.8;" id="but"><i class="fa fa-chain"></i> 联系人</button>
                               </div>
                               
                               <div class="widget-body no-shadow">
                               		<div class="tickets-container bg-white">
                               			<%-- <div class="pull-right" style="margin: 10px 0px">
											<button type="button" class="btn btn-info ws-btnBlue"  onclick="showOlm('${param.sid}')">联系人关联</button>
										</div> --%>
                               		 <table class="table table-hover table-striped table-bordered table0" id="linkManTable" style="width: 100%;display: none ">
                               		 	<thead>
                               		 		<tr>
	                               		 		<th width="30%">姓名</th>
												<th width="30%">职务</th>
												<th width="40%">联系方式</th>
												<th>&nbsp;</th>
											</tr>
                               		 	</thead>
                               		 	<tbody>
                               		 		<tr style="display: none">
                               		 			<td>
	                               		 			<input disabled class="table_input" type="text" nullmsg="请输入联系人" id="listLinkMan[0].linkManName" 
	                               		 			name="listLinkMan[0].linkManName" />
                               		 			</td>
                               		 			<td>
                               		 				<input disabled class="table_input" type="text" id="listLinkMan[0].post" name="listLinkMan[0].post" />
                               		 			</td>
                               		 			<td>
                               		 				<input disabled class="table_input" type="text" ignore='ignore' id="listLinkMan[0].contactWayValue" name="listLinkMan[0].contactWayValue" />
                               		 			</td>
                               		 			<td>
                               		 			<a href="javascript:void(0)" class="fa fa-times-circle-o" onclick="delTr(this)" title="删除本行"></a>
                               		 			</td>
                               		 		</tr>
                               		 	</tbody>
                               		 </table>
                                	</div>
                               </div>
                           </div>
                            <%-- <div class="widget radius-bordered collapsed" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">共享范围设置</span>
                                   <div class="widget-buttons btn-div-full">
                                      <a class="ps-point btn-a-full" data-toggle="collapse">
                                    	<i class="fa fa-plus blue"></i>
                                       </a>
                                   </div>
                               </div>
                               <div class="widget-body no-shadow">
                               		<div class="tickets-container bg-white">
                               			<ul class="tickets-list">
                               				<li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text">
											    	阅读权限
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												  	<div class="pull-left">
												  		<select class="populate" id="pubState" name="pubState" onchange="showMans()">
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
												  		<tags:userMore name="listCustomerSharer.userId" showName="userName" disDivKey="crmSharor_div"></tags:userMore>
													</div>
												</div>
												<div class="ps-clear"></div> 
	                                         </li>
											<li class="ticket-item no-shadow autoHeight no-padding">
										    	<div class="pull-left gray ps-left-text padding-top-10">
										    		共享组
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="height: auto;">
													<div class="pull-left gray ps-left-text padding-top-10">
												  		<div id="customerGroup_div" class="pull-left" style="max-width: 460px" >
														</div>
												  		<a href="javascript:void(0)" onclick="querySelfGroupForCustomer(0,'${param.sid}')" class="btn btn-primary btn-xs margin-top-5" title="组选择"><i class="fa fa-plus"></i>添加</a>
													</div>
													用于保持数据
													<div style="display:none" id="tempSelectDiv">
														<select style="display:none" id="selfGrouplist">
														</select>
													</div>
												</div>
												<div class="ps-clear"></div> 
	                                         </li>
                                        </ul>
                               		</div>
                               </div>
                           </div> --%>
                           <div class="widget radius-bordered collapsed" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">更多配置</span>
                                   <div class="widget-buttons btn-div-full">
                                      <a class="ps-point btn-a-full" data-toggle="collapse">
                                    	<i class="fa fa-plus blue"></i>
                                       </a>
                                   </div>
                               </div>
                               <div class="widget-body no-shadow">
                               	<div class="tickets-container bg-white">
									<ul class="tickets-list">
										<li class="ticket-item no-shadow ps-listline">
									    	<div class="pull-left gray ps-left-text">
									    		联系电话
									    	</div>
											<div class="ticket-user pull-left ps-right-box">
										  		<input class="colorpicker-default form-control " type="text" value="" ignore="ignore" id="linePhone" name="linePhone">
											</div>               
                                         </li>
										<li class="ticket-item no-shadow ps-listline">
									    	<div class="pull-left gray ps-left-text">
									    		传真
									    	</div>
											<div class="ticket-user pull-left ps-right-box">
										  		<input class="form-control" type="text" value="" id="fax" name="fax">
											</div>               
                                         </li>
										<li class="ticket-item no-shadow ps-listline">
									    	<div class="pull-left gray ps-left-text">
									    		邮编
									    	</div>
											<div class="ticket-user pull-left ps-right-box">
										  		<input class="colorpicker-default form-control" type="text" value="" ignore="ignore" id="postCode" name="postCode">
											</div>               
                                         </li>
										<li class="ticket-item no-shadow ps-listline" style="clear: both" >
									    	<div class="pull-left gray ps-left-text">
									    		联系地址
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px">
										  		<input class="colorpicker-default form-control" id="address" name="address" type="text" value="">
											</div>               
                                         </li>
										<li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		客户备注
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;" id="customerRemark" name="customerRemark" rows="" cols=""></textarea>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
										<%-- <li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		相关附件
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
												<div class="margin-top-10">
											  		<tags:uploadMore name="listUpfiles.upfileId" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
												</div>
											</div> 
											<div class="ps-clear"></div>              
                                         </li> --%>
                                  	</ul>
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
