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
//打开页面body
var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//打开页面的标签
var openPageTag;
//打开页面的标签
var openTabIndex;
var list = [];//保存选择的外部联系人id
var sid = '${param.sid}'

//注入父页面信息
function setWindow(winDoc,win){
	openWindowDoc = winDoc;
	openWindow = win;
	openPageTag = openWindow.pageTag;
	openTabIndex = openWindow.tabIndex;
}
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
var valid;
$(function() {
	//合并行
	olmFormRowSpan($("#linkManTable"))
	showMans();//根据pubState显示或者隐藏参与人范围
	
	//获取listLinkMan里面的外部联系人id并装入list
	var listLen = "${listLinkMan.size()}";
	if(listLen > 0){
		for(var i=0;i<listLen;i++){
			var outLinkManId = $("input[name='listLinkMan["+ i +"].olmId']").val();
			list.push(outLinkManId)
		}
		showTable()
	}
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	valid = $(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
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
				return isFloat($("#budget").val());
			},
		},
		showAllError : true
	});
});
$(function(){
	<%--客户重要信息更新--%>
	//客户名称更新
	$("#customerName").change(function(){
		
		//客户名称
		var crmName = $("#customerName").val();
		//客户名称的长度，汉字算两个字符长度
		var count = crmName.replace(/[^\x00-\xff]/g,"**").length;
		var len = $("#customerName").attr("defaultLen");
		//客户名称长度超过指定长度
		if(count>len){
			return false;
		}else{
			if(!strIsNull(crmName)){
				$.post("/crm/customerNameUpdate?sid=${param.sid}",{Action:"post",id:${customer.id},customerName:crmName},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
					$("#customerName").attr("title",crmName);
					crmName = '--'+cutstr(crmName,40);
					$("#titleCrmName").html(crmName);
				});
			}
		}
	});
	
	
	//客户类型称更新
	$("#customerTypeId").change(function(){
		if(!strIsNull($("#customerTypeId option:selected").val())){
			//查询相关配置
			$.ajax({
	      		type : "post",
	      		url : "/moduleChangeExamine/ajaxGetModChangeExam?sid="+'${param.sid}' + "&moduleType=012&field=customerTypeId",
	      		dataType:"json",
	      		traditional :true, 
	      		data:null,
	      		  beforeSend: function(XMLHttpRequest){
	               },
	      		  success : function(data){
	      			if(data.lists && data.lists[0] && data.needExam == "y"){//需要审批
	      				var url = '/moduleChangeExamine/modChangeApplyPage?sid=${param.sid}&busId=${customer.id}'
	      						+'&field=customerTypeId&moduleType=012&oldName=${customer.typeName}&busName=${customer.customerName}&newValue='
	      						+$("#customerTypeId option:selected").val()+'&oldValue=${customer.customerTypeId}';
						window.top.layer.open({
							type: 2,
							title:false,
							closeBtn:0,
							area: ['550px', '465px'],
							fix: false, //不固定
							maxmin: false,
							content: [url,'no'],
							btn: ['提交','关闭'],
							yes: function(index, layero){
								var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
								var flag = iframeWin.formSub();
								if(flag){
									window.top.layer.close(index);
									showNotification(1,"申请成功");
									
								  }
							  },cancel: function(){
							  },success:function(layero,index){
								  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
								  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
									  window.top.layer.close(index);
								  })
							  }
							});
						$("#customerTypeId").val('${customer.customerTypeId}');
					}else{
						$.post("/crm/customerTypeIdUpdate?sid=${param.sid}",{Action:"post",id:${customer.id},customerTypeId:$("#customerTypeId option:selected").val()},     
			 				function (msgObjs){
							showNotification(1,msgObjs);
						});
					}
	      		  },
	      		  error:  function(XMLHttpRequest, textStatus, errorThrown){
	      			  window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
	      	      }
	      	}); 
			
		}
	});
	//客户阶段更新
	$("#stage").change(function(){
		if($("#stage option:selected").val()){
			//查询相关配置
			$.ajax({
	      		type : "post",
	      		url : "/moduleChangeExamine/ajaxGetModChangeExam?sid="+'${param.sid}' + "&moduleType=012&field=stage",
	      		dataType:"json",
	      		traditional :true, 
	      		data:null,
	      		  beforeSend: function(XMLHttpRequest){
	               },
	      		  success : function(data){
	      			if(data.lists && data.lists[0] && data.needExam == "y"){//需要审批
	      				var url = '/moduleChangeExamine/modChangeApplyPage?sid=${param.sid}&busId=${customer.id}'
	      						+'&field=stage&moduleType=012&oldName=${customer.stageName}&busName=${customer.customerName}&newValue='
	      						+$("#stage option:selected").val()+'&oldValue=${customer.stage}';
						window.top.layer.open({
							type: 2,
							title:false,
							closeBtn:0,
							area: ['550px', '465px'],
							fix: false, //不固定
							maxmin: false,
							content: [url,'no'],
							btn: ['提交','关闭'],
							yes: function(index, layero){
								var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
								var flag = iframeWin.formSub();
								if(flag){
									window.top.layer.close(index);
									showNotification(1,"申请成功");
									
								  }
							  },cancel: function(){
							  },success:function(layero,index){
								  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
								  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
									  window.top.layer.close(index);
								  })
							  }
							});
						$("#stage").val('${customer.stage}');
					}else{
						$.post("/crm/updateCustomerStage?sid=${param.sid}",{Action:"post",id:${customer.id},stage:$("#stage option:selected").val()},     
			 				function (msgObjs){
							showNotification(1,msgObjs);
						});
					}
	      		  },
	      		  error:  function(XMLHttpRequest, textStatus, errorThrown){
	      			  window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
	      	      }
	      	}); 
			
		}
	});
	//预算更新
	$("#budget").change(function(){
		if($("#budget").val()){
			$.post("/crm/updateCustomerBudget?sid=${param.sid}",{Action:"post",id:${customer.id},budget:$("#budget").val()},     
 				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
	});
	//客户联系电话更新
	$("#linePhone").change(function(){
		var value = $("#linePhone").val();
		if(!strIsNull(value)){
			$.post("/crm/updateCustomerLinePhone?sid=${param.sid}",{Action:"post",id:${customer.id},linePhone:$("#linePhone").val()},     
 				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
	});
	//客户传真更新
	$("#fax").change(function(){
		if(!strIsNull($("#fax").val())){
			$.post("/crm/updateCustomerFax?sid=${param.sid}",{Action:"post",id:${customer.id},fax:$("#fax").val()},     
 				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
	});
	//客户联系地址更新
	$("#address").change(function(){
		if(!strIsNull($("#address").val())){
			$.post("/crm/updateCustomerAddress?sid=${param.sid}",{Action:"post",id:${customer.id},address:$("#address").val()},     
 				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
	});
	//客户邮编更新
	$("#postCode").change(function(){
		var value = $("#postCode").val();
		if(!strIsNull(value)){
			$.post("/crm/updateCustomerPostCode?sid=${param.sid}",{Action:"post",id:${customer.id},postCode:$("#postCode").val()},     
					function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
	});
	//客户备注更新
	$("#customerRemark").change(function(){
		if(!strIsNull($("#customerRemark").val())){
			$.post("/crm/updateCustomerRemark?sid=${param.sid}",{Action:"post",id:${customer.id},customerRemark:$("#customerRemark").val()},     
 				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
	});
	
	//客户公开私有更新
	$("#pubState").change(function(){
		if($("#pubState").val() != null){
			$.post("/crm/updateCustomerPubState?sid=${param.sid}",{Action:"post",id:${customer.id},pubState:$("#pubState").val(),owner:$("#owner").val()},     
 				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
	});
	
	//客户移交
	$("#customerHandOver").click(function(){
		
		//查询相关配置
		$.ajax({
      		type : "post",
      		url : "/moduleChangeExamine/ajaxGetModChangeExam?sid="+'${param.sid}' + "&moduleType=012&field=owner",
      		dataType:"json",
      		traditional :true, 
      		data:null,
      		  beforeSend: function(XMLHttpRequest){
               },
      		  success : function(data){
      			if(data.lists && data.lists[0] && data.needExam == "y"){//需要审批
      				var url = '/moduleChangeExamine/modChangeApplyPage?sid=${param.sid}&busId=${customer.id}'
      						+'&field=owner&moduleType=012&busName=${customer.customerName}&oldName=${customer.ownerName}'
      						+'&oldValue=${customer.owner}';
					window.top.layer.open({
						type: 2,
						title:false,
						closeBtn:0,
						area: ['550px', '515px'],
						fix: false, //不固定
						maxmin: false,
						content: [url,'no'],
						btn: ['提交','关闭'],
						yes: function(index, layero){
							var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
							var flag = iframeWin.formSub();
							if(flag){
								window.top.layer.close(index);
								showNotification(1,"申请成功");
								
							  }
						  },cancel: function(){
						  },success:function(layero,index){
							  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
							  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
								  window.top.layer.close(index);
							  })
						  }
						});
				}else{
					window.top.layer.open({
						type: 2,
						//title: ['客户移交', 'font-size:18px'],
						title:false,
						closeBtn:0,
						area: ['550px', '410px'],
						fix: false, //不固定
						maxmin: false,
						content: ['/crm/customerHandOverByOnePage?sid=${param.sid}&customerId=${customer.id}','no'],
						btn: ['移交','关闭'],
						yes: function(index, layero){
							var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
							var flag = iframeWin.formSub();
							if(flag){
								window.top.layer.close(index);
								showNotification(1,"移交成功");
								  
								if(openPageTag=='myCrm'){//我的客户
									//移交后移除父页面数据
									openWindow.removeMyCrm(${customer.id});
									//父页面异步加载数据
									openWindow.loadCrm();
									//关闭弹窗
									window.top.layer.close(openTabIndex);
								}else if(openPageTag=='allTodo'){//所有待办
									//关闭弹窗
									window.top.layer.close(openTabIndex);
								}else if(openPageTag=='index'){//首页
									//关闭弹窗
									window.top.layer.close(openTabIndex);
								}else if(openPageTag.indexOf("Crm")>=0){//是客户列表
									//判断父页面是否需要向前调整
									var url= openWindow.reConUrlByClz("#crmListUl li",1);
									openWindow.self.location = url;
									//关闭弹窗
									window.top.layer.close(openTabIndex);
								}else{
									openWindow.location.reload();
								}
							  }
						  },cancel: function(){
						  },success:function(layero,index){
							  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
							  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
								  window.top.layer.close(index);
							  })
						  }
						});
				}
      		  },
      		  error:  function(XMLHttpRequest, textStatus, errorThrown){
      			  window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
      	      }
      	}); 
		
		
	});
});
//重写回调函数为空
function artOpenerCallBack(areaIdAndType){
	if($("#areaIdAndType").attr("preValue")!=$("#areaIdAndType").val()){
		$("#areaIdAndType").attr("preValue",$("#areaIdAndType").val());
		$.post("/crm/areaIdUpdate?sid=${param.sid}",{Action:"post",id:${customer.id},areaIdAndType:$("#areaIdAndType").val()},     
			function (msgObjs){
			showNotification(1,msgObjs);
		});
	}
}
var linkManRowNum=parseInt('${customer.linkManSum}');
//客户联系添加行
function addLinkMan(){
	//当前联系人行数$("#linkManTable tr").length-3，3：表头，表尾有个固定化
	if(!linkManRowNum){
		linkManRowNum = ($("#linkManTable tbody tr").length);
	}else{
		linkManRowNum = linkManRowNum+1;
	}
	//联系人添加
    var tr="<tr style=\"display:none \">";
    tr +="<td><input style=\"border: 0;\" class=\"table_input\" type=\"text\" datatype=\"*\" nullmsg=\"请输入客户名称\" id=\"listLinkMan["+linkManRowNum+"].linkManName\" name=\"listLinkMan["+linkManRowNum+"].linkManName\" /></td>";
    tr +="<td><input style=\"border: 0;\" class=\"table_input\" type=\"text\" id=\"listLinkMan["+linkManRowNum+"].post\" name=\"listLinkMan["+linkManRowNum+"].post\"/></td>";
    tr +="<td><input style=\"border: 0;\" class=\"table_input\" type=\"text\" ignore='ignore' id=\"listLinkMan["+linkManRowNum+"].linePhone\" name=\"listLinkMan["+linkManRowNum+"].linePhone\"/></td>";
    tr +="<td><a href=\"javascript:;\" style=\"color: red;\" class=\"fa fa-times-circle-o\" onclick=\"delLinkManTr(this,0)\" title=\"删除本行\"></a></td>";
    tr +="</tr>";
    if(linkManRowNum==0){
	    $("#linkManTable tbody").html(tr);
    }else{
	    $("#linkManTable tbody").find("tr").last().after(tr);
    }
	valid.addRule();
}
//更新客户联系人
function customerLinkManUpdate(){
	var flag = true;
	var pramaNames =$("input[name^='listLinkMan[']");
	if(pramaNames.length==0){
		flag = false;
	}else{
	}
	if(flag){//验证通过
		$.post("/crm/customerLinkManUpdate?sid=${param.sid}",$("#subform").serialize(),     
				function (msgObj){
				 if(msgObj.succ){
					$("#linkManTable tbody").html(msgObj.linkManStr);
					showNotification(1,msgObj.promptMsg);
					linkManRowNum = msgObj.linkManSum;
					if(linkManRowNum>0){
						$("#linkManNum").html("("+linkManRowNum+")")
					}else{
						$("#linkManNum").html("")
					}
					//合并行
					olmFormRowSpan($("#linkManTable"));
				 }else{
					showNotification(2,msgObj.promptMsg);
				 }
		}, "json");
	}
}
//删除联系人行
function delLinkManTr(clickTd,linkManId,olmId){
	window.top.layer.confirm('确定要删除本行?',{
		  btn: ['确定','取消'] //按钮
		}, function(index){
			if(!strIsNull(olmId) && olmId > 0){
				list.splice($.inArray(olmId,list),1);
			}
			
			if(!strIsNull(linkManId) && linkManId > 0){
				$.post("/crm/delLinkMan?sid=${param.sid}",{customerId:${customer.id},id:linkManId},     
					function (msgObjs){
					if(msgObjs.delSucc){
				    	var tr = $(clickTd).parent().parent().parent().find("tr[olmId='"+olmId+"']"); 
				    	tr.remove();
						showNotification(1,msgObjs.promptMsg);
						linkManRowNum--;
						if(linkManRowNum==0){
							addLinkMan();
							$("#linkManNum").html("(0)")
						}else{
							$("#linkManNum").html("("+linkManRowNum+")")
						}
					}else{
						showNotification(2,msgObjs.promptMsg);
					}
				},"json");
			}else{
		    	var tr = $(clickTd).parent().parent();  
		    	tr.remove();
				linkManRowNum--;
				if(linkManRowNum==0){
					addLinkMan();
					$("#linkManNum").html("(0)")
				}else{
					$("#linkManNum").html("("+linkManRowNum+")")
				}
			}
			window.top.layer.close(index)
			showTable()
		})
		
}

$(document).ready(function(){
	if(!strIsNull($("#sharerJson").val())){
		var users = eval("("+$("#sharerJson").val()+")");
		  var img="";
		  var editCrm = '${editCrm}';
		  
		  if(strIsNull(editCrm)){
			  for (var i=0, l=users.length; i<l; i++) {
					//数据保持
					$("#listCustomerSharer_userId").append('<option selected="selected" value='+users[i].userID+'>'+users[i].userName+'</option>');
					img = img + "<div class=\"online-list margin-top-5 margin-left-5 margin-bottom-5\" style=\"float:left\" id=\"user_img_listCustomerSharer_userId_"+users[i].userID+"\" ondblclick=\"removeClickUser('listCustomerSharer_userId',"+users[i].userID+")\" title=\"双击移除\">";
					img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}\" class=\"user-avatar\"/>"
					img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
					img = img + "</div>"
				}
			}else{
			  for (var i=0, l=users.length; i<l; i++) {
					img = img + "<div class=\"online-list\" style=\"float:left\">";
					img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}\" class=\"user-avatar\"/>"
					img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
					img = img + "</div>"
				}
				
			}
			$("#crmSharor_div").html(img);
	}
	//取得闹铃集合信息
	ajaxListClockForOne('012','${customer.id}','${param.sid}',function(data){
		var listClocks = data.listClocks;
		if(listClocks && listClocks.length>0){
			$.each(listClocks,function(index,clockObj){
				var  clockHtml= conStrClockHtml('012','${customer.id}','${param.sid}',clockObj);
				$("#busClockList").append(clockHtml)
			})
		}
	});
});
//设置客户共享组
function initCustomerGroup(groups){
	var grp = eval(groups);
	var grpName="";
	var ids="";
	if(!strIsNull(groups)){
		for(var i=0;i<grp.length;i++){
			grpName +="<span id=\"crmGrp_"+grp[i].id+"\" style='cursor:pointer;' title=\"双击移除\" class=\"label label-default margin-top-5 margin-left-5 margin-bottom-5\" ondblclick=\"delCrmShareGroup('"+grp[i].id+"')\">"+grp[i].name;
			grpName +="</span>";
			ids +="grpId="+grp[i].id+"&";
		}
		ids = ids.substr(0,ids.length-1);
		$.post("/crm/editCustomerGroup?sid=${param.sid}&"+ids,{Action:"post",customerId:${customer.id}},          
			function (msgObjs){
				if(msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
				}else{
					showNotification(2,msgObjs.promptMsg);
				}
		},"json");
		$("#customerGroup_div").html(grpName);
	}
}
//删除客户分享组
function delCrmShareGroup(groupId){
	if(!strIsNull(groupId)){
		$.post("/crm/delCrmShareGroup?sid=${param.sid}",{Action:"post",customerId:${customer.id},groupId:groupId},     
				function (msgObjs){
			if(msgObjs.succ){
				showNotification(1,msgObjs.promptMsg);
				removeObjByKey("crmGrp_"+groupId);
				 $("#selfGrouplist").find("option[value='"+groupId+"']").remove();
			}else{
				showNotification(2,msgObjs.promptMsg);
			}
		},"json");
	}
}

//任务属性菜单切换
$(function(){
	//维护记录
	$("#customerFeedBackInfoMenuLi").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#customerFeedBackInfoMenuLi").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/crm/customerFeedBackInfoPage?sid=${param.sid}&pager.pageSize=10&customerId=${customer.id}");
	});
	//维护记录
	$("#headCrmFeedback").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$("#customerFeedBackInfoMenuLi").parent().find("li").removeAttr("class");
		$("#customerFeedBackInfoMenuLi").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/crm/customerFeedBackInfoPage?sid=${param.sid}&pager.pageSize=10&customerId=${customer.id}&talkFocus='1'");
	});
	//客户客户配置
	$("#customerBaseMenuLi").click(function(){
		$("#customerBase").css("display","block");
		$("#otherCustomerAttrIframe").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#customerBaseMenuLi").attr("class","active");
	});
	//移交记录
	$("#crmFlowRecordLi").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#crmFlowRecordLi").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/crm/crmFlowRecord?sid=${param.sid}&crmId=${customer.id}");
	});
	//客户日志
	$("#customerLogMenuLi").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#customerLogMenuLi").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${customer.id}&busType=012&ifreamName=otherCustomerAttrIframe");
	});
	//客户文档
	$("#customerUpfileMenuLi").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");;
		$("#customerUpfileMenuLi").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/crm/customerUpfilePage?sid=${param.sid}&pager.pageSize=10&customerId=${customer.id}&redirectPage="+encodeURIComponent(window.location.href));
	});
	//客户聊天室
	$("#crmChatLi").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#crmChatLi").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/chat/listBusChat?sid=${param.sid}&busId=${customer.id}&busType=012&ifreamTag=otherCustomerAttrIframe");
	});
	//客户项目
	$("#crmItem").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");
		$("#crmItem").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/crm/crmItemListPage?sid=${param.sid}&pager.pageSize=10&partnerId=${customer.id}&redirectPage="+encodeURIComponent(window.location.href));
	});
	//客户任务
	$("#crmTask").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");;
		$("#crmTask").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/task/busModTaskList?sid=${param.sid}&pager.pageSize=10&ifreamName=otherCustomerAttrIframe&busId=${customer.id}&busType=012&redirectPage="+encodeURIComponent(window.location.href));
	});
	//客户查看记录
	$("#crmViewRecord").click(function(){
		$("#otherCustomerAttrIframe").css("display","block");
		$("#customerBase").css("display","none");
		$(this).parent().find("li").removeAttr("class");;
		$("#crmViewRecord").attr("class","active");
		$("#otherCustomerAttrIframe").attr("src","/common/listViewRecord?sid=${param.sid}&busId=${customer.id}&busType=012&ifreamName=otherCustomerAttrIframe");
	});
});
//客户参与人更新
function userMoreCallBack(){
	var userIds =new Array();
	$("#listCustomerSharer_userId option").each(function() { 
		userIds.push($(this).val()); 
    });
	if(!strIsNull(userIds.toString())){
		$.post("/crm/customerSharerUpdate?sid=${param.sid}",{Action:"post",customerId:${customer.id},userIds:userIds.toString()},     
			function (msgObjs){
			showNotification(1,msgObjs);
		});
	}
}
//删除参与人
function removeClickUser(id,selectedUserId) {
	var selectobj = document.getElementById(id);
	for ( var i = 0; i < selectobj.options.length; i++) {
		if (selectobj.options[i].value==selectedUserId) {
			selectobj.options[i] = null;
			break;
		}
	}
	$("#user_img_"+id+"_"+selectedUserId).remove();
	$.post("/crm/delCustomerSharer?sid=${param.sid}",{Action:"post",customerId:${customer.id},userId:selectedUserId},     
			function (msgObjs){
			showNotification(1,msgObjs);
	});
	selected(id);
}

//添加关联模块
function addRelMod(type){
	if(type=='003'){
		window.top.layer.open({
			 type: 2,
			 title:false,
			 closeBtn: 0,
			  area: ['800px', '550px'],
			  fix: true, //不固定
			  maxmin: false,
			  content: ["/task/addBusTaskPage?sid=${param.sid}&busId=${customer.id}&busType=012",'no'],
			  btn: ['发布','关闭'],
			  yes: function(index, layero){
				  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				  var flag = iframeWin.formSub();
				  if(flag){
					  window.top.layer.close(index);
					  window.top.layer.msg("发布成功",{icon:1})
					  window.self.location.reload();
				  }
			  },cancel: function(){
			  }
			});
	}else if(type=='005'){
		window.top.layer.open({
			 type: 2,
			 title:false,
			 closeBtn: 0,
			  area: ['600px', '450px'],
			  fix: true, //不固定
			  maxmin: false,
			  content: ["/item/addCrmItemPage?sid=${param.sid}&partnerId=${customer.id}",'no'],
			  btn: ['创建','关闭'],
			  yes: function(index, layero){
				  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				  var flag = iframeWin.formSub();
				  if(flag){
					  window.top.layer.close(index);
					  window.top.layer.msg("创建成功",{icon:1})
					  window.self.location.reload();
				  }
			  },cancel: function(){
			  }
			});
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
				 customerLinkManUpdate();
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


//选择联系人后添加到页面
function addOlm(result){
	
	for(var i=0;i<result.length;i++){
		var olm =  JSON.parse(result[i]);
		if(list.length > 0){
			var count = 0;
			for(var j=0;j<list.length;j++){
				if(list[j] == olm.id){
					count++;
				}
			}
			if(count < 1){
				list.push(olm.id);
				//添加数据到页面
				$("#oneMoreLinkMan").text("再添加一个");
				 if(!linkManRowNum){
						linkManRowNum = ($("#linkManTable tbody tr").length);
					}else{
						linkManRowNum = linkManRowNum+1;
					}
				   
			    var tr="<tr>";
			    tr +="<td>"+'<a href="javascript:void(0);" onclick="viewOlm('+olm.id +');">'+olm.linkManName+'</a>'+"  <input  type=\"hidden\" id=\"listLinkMan["+linkManRowNum+"].outLinkManId\" name=\"listLinkMan["+linkManRowNum+"].outLinkManId\" value ='"+ (olm.id==null?"":olm.id) +"'/></td>";
			    tr +="<td><input style=\"border: 0;\" disabled class=\"table_input\" type=\"text\" id=\"listLinkMan["+linkManRowNum+"].post\" name=\"listLinkMan["+linkManRowNum+"].post\" value ='"+ (olm.post==null?"":olm.post) +"'/></td>";
			    tr +="<td>"+ (olm.contactWayValue==null?"":(olm.contactWayValue+"："+olm.contactWay)) +"</td>";
			    tr +="<td><a href=\"javascript:void(0)\" style=\"color: red;\" class=\"fa fa-times-circle-o\" onclick=\"delLinkManTr(this,0, "+olm.id+")\" title=\"删除本行\"></a></td>";
			    tr +="</tr>";
			   
			    if(linkManRowNum==0){
				    $("#linkManTable tbody").html(tr);
			    }else{
				    $("#linkManTable tbody").find("tr").last().after(tr);
			    }
			}
		}else{
			list.push(olm.id);
			//添加数据到页面
			$("#oneMoreLinkMan").text("再添加一个");;
			if(!linkManRowNum){
				linkManRowNum = ($("#linkManTable tbody tr").length);
			}else{
				linkManRowNum = linkManRowNum+1;
			}
		    var tr="<tr>";
		    tr +="<td>"+'<a href="javascript:void(0);" onclick="viewOlm('+olm.id +');">'+olm.linkManName+'</a>'+"  <input  type=\"hidden\" id=\"listLinkMan["+linkManRowNum+"].outLinkManId\" name=\"listLinkMan["+linkManRowNum+"].outLinkManId\" value ='"+ (olm.id==null?"":olm.id) +"'/></td>";
		    tr +="<td><input style=\"border: 0;\" disabled class=\"table_input\" type=\"text\" id=\"listLinkMan["+linkManRowNum+"].post\" name=\"listLinkMan["+linkManRowNum+"].post\" value ='"+ (olm.post==null?"":olm.post) +"'/></td>";
		    tr +="<td>"+ (olm.contactWayValue==null?"":(olm.contactWayValue+"："+olm.contactWay)) +"</td>";
		    tr +="<td><a href=\"javascript:void(0)\" style=\"color: red;\" class=\"fa fa-times-circle-o\" onclick=\"delLinkManTr(this,0,"+ olm.id+")\" title=\"删除本行\"></a></td>";
		    tr +="</tr>";
		    
		    
		    if(linkManRowNum==0){
			    $("#linkManTable tbody").html(tr);
		    }else{
			    $("#linkManTable tbody").find("tr").last().after(tr);
		    }
		}
		
		
		
	}
	showTable()
}


//人员范围的显示隐藏
function showMans(){
	
	if($("#pubState").val() == 0){
		$("#mans").show();
	}else{
		$("#mans").hide();
	}
	
}

//外部联系人表格显示隐藏
function showTable() {
	if(list.length>0){
		$("#linkManTable").show();
	}else{
		$("#linkManTable").hide();
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
		});
		
	}
}
</script>
</head>
<body onload="${empty editCrm?'handleName()':'' }">
<form id="subform" class="subform" method="post">
<input type="hidden" name="id" value="${customer.id}"/>
<input type="hidden" name="linkManSum" id="linkManSum" value="${customer.linkManSum}"/>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                		<a href="javascript:void(0)" class="widget-caption blue padding-right-5"
               				attentionState="${customer.attentionState}" busType="012" busId="${customer.id}" describe="0" iconSize="sizeMd"
               				onclick="changeAtten('${param.sid}',this)" title="${customer.attentionState==0?"关注":"取消"}">
							<i class="fa ${customer.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>
						</a>
						
                        <span class="widget-caption themeprimary" style="font-size: 20px">客户</span>
                        <span class="widget-caption themeprimary" style="font-size: 15px;margin-top: 2px" id="titleCrmName">
                        	<c:choose>
                        		<c:when test="${fn:length(customer.customerName)>20 }">
		                        	--${fn:substring(customer.customerName,0,20)}..
                        		</c:when>
                        		<c:otherwise>
                        			--${customer.customerName}
                        		</c:otherwise>
                        	</c:choose>
                        </span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="purple" id="headCrmFeedback" title="反馈">
								<i class="fa fa-comments"></i>留言
							</a>
							<a href="javascript:void(0)" class="blue" id="customerHandOver">
								<i class="fa fa-h-square"></i>移交
							</a>
                              <a class="green ps-point margin-right-0" data-toggle="dropdown" title="关联模块">
                                  <i class="fa fa-th"></i>更多
                              </a>
                              <!--Notification Dropdown-->
                              <ul class="pull-right dropdown-menu dropdown-arrow ps-layerUl">
                                  <li>
                                      <a href="javascript:void(0)" onclick="addRelMod('005')">
                                          <div class="clearfix">
                                           	<i class="fa fa-pencil-square"></i>
                                            <span class="title ps-topmargin">添加项目</span>
                                          </div>
                                      </a>
                                  </li>
                                  <li>
                                      <a href="javascript:void(0)" onclick="addRelMod('003')">
                                          <div class="clearfix">
                                           	<i class="fa fa-flag"></i>
                                              <span class="title ps-topmargin">任务安排</span>
                                          </div>
                                      </a>
                                  </li>
                                  
                                  <li>
                                      <a href="javascript:void(0)" onclick="addClock(${customer.id},'012','${param.sid}')">
                                          <div class="clearfix">
                                           	 <i class="fa fa-clock-o"></i>
                                             <span class="title ps-topmargin">定时提醒</span>
                                             <span class="title ps-topmargin blue fa fa-plus padding-left-30"></span>
                                          </div>
                                      </a>
                                      <ul id="busClockList">
                                      </ul>
                                  </li>
                                  
                                </ul>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->             
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                           <div class="widget-body no-shadow">
                           		<div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat" id="myTab11" style="padding-top:0;">
                                             <li class="active" id="customerFeedBackInfoMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">客户留言</a>
                                             </li>
                                             <li id="customerBaseMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">客户详情</a>
                                             </li>
                                             <li id="crmItem">
                                                 <a data-toggle="tab" href="javascript:void(0)">关联项目<c:if test="${customer.itemNum > 0}"><span style="color:red;font-weight:bold;">（${customer.itemNum}）</span></c:if></a>
                                             </li>
                                             <li id="crmTask">
                                                 <a data-toggle="tab" href="javascript:void(0)">关联任务<c:if test="${customer.taskNum > 0}"><span style="color:red;font-weight:bold;">（${customer.taskNum}）</span></c:if></a>
                                             </li>
                                             <li id="customerLogMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">操作日志</a>
                                             </li>
                                             <li id="customerUpfileMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">客户文档<c:if test="${customer.fileNum > 0}"><span style="color:red;font-weight:bold;">（${customer.fileNum}）</span></c:if></a>
                                             </li>

                                             <li id="crmViewRecord">
                                                 <a data-toggle="tab" href="javascript:void(0)">阅读情况</a>
                                             </li>

                                             <%--<li id="crmViewRecord">--%>
                                                 <%--<a data-toggle="tab" href="javascript:void(0)">最近查看</a>--%>
                                             <%--</li>--%>

                                            <!--  <li id="crmFlowRecordLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">移交记录</a>
                                             </li> -->
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 	<div id="customerBase" style="display:none;">
												<jsp:include page="./customerBase_edit.jsp"></jsp:include>
											</div>
                                    	 	<iframe id="otherCustomerAttrIframe" style="display:block;" class="layui-layer-load"
												src="/crm/customerFeedBackInfoPage?sid=${param.sid}&pager.pageSize=10&customerId=${customer.id}"
												border="0" frameborder="0" allowTransparency="true"
												noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
                                    	 </div>
                                	</div>
                            	</div>
                            </div> 
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
