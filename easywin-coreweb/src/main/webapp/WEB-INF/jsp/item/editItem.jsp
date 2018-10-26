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
	<link href="/static/assets/css/demand.css" rel="stylesheet" type="text/css">
<style type="text/css">
	.fm_progress div.quan{width: 14px;margin:auto;}
	.fm_progress{height:auto;border-bottom: 0;}
	.fm_progress div.lc_time span{min-height: 45px;}
	.title{font-size: 14px;font-weight: 100;}
	.nav>li>a {padding: 10px 5px;}
</style>
<script type="text/javascript">

var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//打开页面的标签
var openPageTag;
//打开页面的标签
var openTabIndex;
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
var pageTag = 'viewItem';
var vali;
	$(function() {
		
		showMans();//人员范围显示隐藏
		
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		vali = $(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
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
				}
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		
		//项目分解
		$("#resolveItem").click(function(){
			window.top.layer.open({
				 type: 2,
				 title:false,
				 closeBtn: 0,
				  area: ['800px', '550px'],
				  fix: true, //不固定
				  maxmin: false,
				  content: ["/item/resolveItemPage?sid=${param.sid}&parentId=${item.id}&pItemName=${item.itemName}",'no'],
				  btn: ['分解','关闭'],
				  yes: function(index, layero){
					  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
					  var result = iframeWin.formSub();
					  if(result){
						  sonObjJsonForBack(result)
						  window.top.layer.close(index);
						  showNotification(1,"创建成功");
					  }
				  },cancel: function(){
				  }
				});
		});
		//项目移交
		$("#itemHandOver").click(function(){
			window.top.layer.open({
				type: 2,
				//title: ['项目移交', 'font-size:18px'],
				title:false,
				closeBtn:0,
				area: ['550px', '410px'],
				fix: false, //不固定
				maxmin: false,
				content: ['/item/itemHandOverByOnePage?sid=${param.sid}&fromUser=${userInfo.id}&itemId=${item.id}','no'],
				btn: ['移交','关闭'],
				yes: function(index, layero){
					var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
					var flag = iframeWin.formSub();
					if(flag){
						window.top.layer.close(index);
						showNotification(1,"移交成功");
						if(openPageTag=='myItem'){//我的项目
							//移除项目列表数据
							openWindow.removeMyItem(${item.id});
							//异步加载更多的项目数据
							openWindow.loadOtherItem();
							//关闭弹窗
							window.top.layer.close(openTabIndex);
						}else if(openPageTag=='allTodo'){//所有待办
							window.top.layer.close(openTabIndex);
						}else if(openPageTag=='index'){//首页
							window.top.layer.close(openTabIndex);
						}else if(openPageTag!=pageTag && openPageTag.indexOf("Item")>=0){//项目列表的其他界面
							//判断父页面是否需要向前调整
							var url= openWindow.reConUrlByClz("#listItemDiv .optDiv",1);
							openWindow.self.location = url;
							//关闭弹窗
							window.top.layer.close(openTabIndex);
						}else{
							openWindow.location.reload();
							window.top.layer.close(openTabIndex);
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
		});
		
		//项目名称更新
		$("#itemName").change(function(){
			//项目名称
			var itemName = $("#itemName").val();
			//项目名称的长度，汉字算两个长度
			var count = itemName.replace(/[^\x00-\xff]/g,"**").length;
			var len = $("#itemName").attr("defaultLen");
			//项目名称长度超过指定的长度
			if(count>len){
				return false;
			}else{
				if(!strIsNull(itemName)){
					$.post("/item/itemNameUpdate?sid=${param.sid}",{Action:"post",id:${item.id},itemName:itemName},     
		 				function (msgObjs){
						showNotification(1,msgObjs);
						$("#itemName").attr("title",itemName);
						
						itemName = '--'+cutstr(itemName,40);
						$("#titleItemName").html(itemName);
					});
				}
			}
		});
		//是否为空判断
		function isNull(obj){
			if(obj!="" && obj!="null" && obj!=null && obj!="undefined" && obj!=undefined){
				return false;
			}else{
				return true;
			}
		}
		//进度汇报
		$("#progress").change(function(){
			var progress = $("#progress option:selected").val();
			if(!isNull(progress)){
				$.post("/item/itemProgressReport?sid=${param.sid}",{Action:"post",id:${item.id},itemProgress:progress},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
					if(progress==0){
						window.self.location.reload();
						if(openPageTag=='viewItem'){
							openWindow.location.reload();
					  	}
					}
				});
			}
		});
		
		//完成时限更新
		$("#dealTimeLimit").blur(function(){
			if(!strIsNull($("#dealTimeLimit").val())){
				$.post("/item/itemDealTimeLimitUpdate?sid=${param.sid}",{Action:"post",id:${item.id},dealTimeLimit:$("#dealTimeLimit").val()},     
			 			function (msgObjs){
							showNotification(1,msgObjs);
						});
			}
		});
		//项目说明更新
		$("#itemRemark").change(function(){
			if(!strIsNull($("#itemRemark").val())){
				$.post("/item/itemItemRemarkUpdate?sid=${param.sid}",{Action:"post",id:${item.id},itemRemark:$("#itemRemark").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		
		//项目金额更新
		$("#amount").change(function(){
			if(!strIsNull($("#amount").val()) && checkOpt.money($("#amount").val())){
				$.post("/item/updateAmount?sid=${param.sid}",{Action:"post",id:${item.id},amount:$("#amount").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		
		//项目公开私有更新
		$("#pubState").change(function(){
			if($("#pubState").val() != null){
				$.post("/item/itemPubStateUpdate?sid=${param.sid}",{Action:"post",id:${item.id},pubState:$("#pubState").val(),owner:$("#owner").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		
		//初始化项目参与人
		$(document).ready(function(){
			if(!strIsNull($("#itemSharerJson").val())){
				var users = eval("("+$("#itemSharerJson").val()+")");
				var img="";
				var editItem = '${editItem}';
				if(strIsNull(editItem)){
					 for (var i=0, l=users.length; i<l; i++) {
						//数据保持
						$("#listItemSharer_userId").append('<option selected="selected" value='+users[i].userID+'>'+users[i].userName+'</option>');
						img = img + "<div class=\"online-list \" style=\"float:left\" id=\"user_img_listItemSharer_userId_"+users[i].userID+"\" ondblclick=\"removeClickUser('listItemSharer_userId',"+users[i].userID+")\" title=\"双击移除\">";
						img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}\" class=\"user-avatar\"/>"
						img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
						img = img + "</div>"
					 }
				}else{
					 for (var i=0, l=users.length; i<l; i++) {
							img = img + "<div class=\"online-list margin-top-5 margin-left-5 margin-bottom-5\" style=\"float:left\">";
							img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}\" class=\"user-avatar\"/>"
							img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
							img = img + "</div>"
						}	  
				}
				$("#itemSharor_div").html(img);
			}
		});
		
		//取得闹铃列表信息
		ajaxListClockForOne('005','${item.id}','${param.sid}',function(data){
			var listClocks = data.listClocks;
			if(listClocks && listClocks.length>0){
				$.each(listClocks,function(index,clockObj){
					var  clockHtml= conStrClockHtml('005','${item.id}','${param.sid}',clockObj);
					$("#busClockList").append(clockHtml)
				})
			}
		});
	})
	//关联项目返回函数//关联项目更新
	function itemSelectedReturn(id,name){
		var oldId = $("#pItemId").val();
		if(id==${item.id}){
			showNotification(1,"不能与自己关联");
		}else if(oldId!=id ){//不是同一个项目则阶段主键就不一样,且不是自己
			if(!strIsNull(id)){
				//项目来源关联
				$.post("/item/itemParentIdUpdate?sid=${param.sid}",{Action:"post",id:${item.id},parentId:id},     
					function (msgObjs){
						if(msgObjs.status=='y'){
							showNotification(1,msgObjs.info);
							$("#pItemName").val(name);
							$("#pItemId").val(id);
							if(openPageTag=='viewItem'){
								openWindow.location.reload();
						  	}
						}else{
							showNotification(2,msgObjs.info);
						}
				},"json");
			}
		}
	}
	function removePItem(){
		var pItemId = $("#pItemId").val();
		if(!strIsNull(pItemId) && pItemId>0){
			//项目来源关联
			$.post("/item/delpItemRelation?sid=${param.sid}",{Action:"post",id:${item.id},parentId:pItemId},     
				function (msgObjs){
					if(msgObjs.status=='y'){
						showNotification(1,msgObjs.info);
						$("#pItemName").val('');
						$("#pItemId").val(-1);
						if(openPageTag=='viewItem'){
							openWindow.location.reload();
					  	}
					}else{
						showNotification(2,msgObjs.info);
					}
			},"json");
		}
	}

	//关联客户返回函数
	function crmSelectedReturn(crmId,name){
		var oldId = $("#partnerId").val();
		if(oldId!=crmId ){//不是同一个项目则阶段主键就不一样,且不是自己
			if(!strIsNull(crmId)&&　crmId>0){
				//项目来源关联
				$.post("/item/updateItemPartner?sid=${param.sid}",{Action:"post",id:${item.id},partnerId:crmId},     
					function (msgObjs){
						if(msgObjs.status=='y'){
							showNotification(1,msgObjs.info);
							$("#partnerName").val(name);
							$("#partnerId").val(crmId);
						}else{
							showNotification(2,msgObjs.info);
						}
				},"json");
			}
		}
	}
	//移除客户关联
	function removeCrm(){
		var crmId = $("#partnerId").val();
		if(!strIsNull(crmId) && crmId>0){
			//项目来源关联
			$.post("/item/delCrmRelation?sid=${param.sid}",{Action:"post",id:${item.id},partnerId:crmId},     
				function (msgObjs){
					if(msgObjs.status=='y'){
						showNotification(1,msgObjs.info);
						$("#partnerName").val('');
						$("#partnerId").val(0);
					}else{
						showNotification(2,msgObjs.info);
					}
			},"json");
		}
	}

	//项目参与人更新
	function userMoreCallBack(){
		var userIds =new Array();
		$("#listItemSharer_userId option").each(function() { 
			userIds.push($(this).val()); 
	    });
		if(!strIsNull(userIds.toString())){
			$.post("/item/itemSharerUpdate?sid=${param.sid}",{Action:"post",itemId:${item.id},userIds:userIds.toString()},     
				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
	}
	
	//项目研发负责人更新
	function userOneCallBack(id,length){
		if(length > 0 && !strIsNull($("#developLeader").val())){
			$.post("/item/updateDevelopLeader?sid=${param.sid}",{Action:"post",id:${item.id},developLeader:$("#developLeader").val()},     
				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
	}
	/* 清除下拉框中选择的option */
	function removeClickUser(id,selectedUserId) {
		var selectobj = document.getElementById(id);
		for ( var i = 0; i < selectobj.options.length; i++) {
			if (selectobj.options[i].value==selectedUserId) {
				selectobj.options[i] = null;
				break;
			}
		}
		$("#user_img_"+id+"_"+selectedUserId).remove();
		$.post("/item/delItemSharer?sid=${param.sid}",{Action:"post",itemId:${item.id},userId:selectedUserId},     
				function (msgObjs){
				showNotification(0,msgObjs);
		});
		selected(id);
	}

	//项目分解后返回的字符串
	function sonObjJsonForBack(sonItems){
		var str="";
		for (var i=0, l=sonItems.length; i<l; i++) {
			str =str + "\n	<tr>";

			str =str + "\n		<td>"+(parseInt(i)+1)+"</td>";
			str =str + '\n		<td><a href="javascript:void(0)" onclick="viewSonItem('+sonItems[i].id+')">'+sonItems[i].itemName+'</a></td>';
			str =str + '\n		<td>';
			str =str + '\n			<div class="ticket-user pull-left other-user-box">';
			str =str + "\n			<img src=\"/downLoad/userImg/"+sonItems[i].comId+"/"+sonItems[i].owner+"?sid=${param.sid}\" title=\""+sonItems[i].ownerName+"\" class=\"user-avatar\"/>";
			str =str + '\n				<span class="user-name">'+sonItems[i].ownerName+'</i>';
			str =str + '\n			</div>';
			str =str + '\n		</td>';
			str =str + "\n		<td>";
			if(sonItems[i].state==1){
				str =str + '\n		<input type="hidden" name="sonItemState" value="1">进行中';
			}else if(sonItems[i].state==3){
				str =str + '\n		<input type="hidden" name="sonItemState" value="3">挂起中';
			}else if(sonItems[i].state==4){
				str =str + '\n		<input type="hidden" name="sonItemState" value="4">已完结';
			}else{
				str =str + '\n		<input type="hidden" name="sonItemState" value="1">正常';
			}
			str =str + '\n		</td>';
			str =str + '\n	</tr>';
		}
		$("#sonItem").html(str);
	}
	
	var sid= "${param.sid}"
	//查看子项目
	function viewSonItem(sonItemId){
	
		authCheck(sonItemId,"005",-1,function (msgObjs){
					msgObjs = eval('(' + msgObjs + ')');
					if(!msgObjs){
						//showNotification(2,msgObjs.promptMsg)
					}else{
						var url = "/item/viewItemPage?sid=${param.sid}&id="+sonItemId;
						var height = $(window).height()
						tabIndex = window.top.layer.open({
							  type: 2,
							  title: false,
							  closeBtn: 0,
							  shadeClose: true,
							  shade: 0.1,
							  shift:0,
							  fix: true, //固定
							  maxmin: false,
							  move: false,
							  btn:["关闭"],
							  area: ['800px', height+'px'],
							  content: [url,'no'], //iframe的url
							  success: function(layero,index){
								    var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
								    iframeWin.setWindow(window.document,window);
								},end:function(index){
									tabIndex=null;
								}
							});
					}
		});
	}

	//项目标记完成
	function remarkItemState(sonItemNum,itemId,state,ts){
		var  sonItemStates = $("input[name='sonItemState']");
		if(state==1){//项目是开启的
			//默认可以直接完成
			var flag = true;
			if(sonItemStates.length>0){
				for(var i=0;i<sonItemStates.length;i++){
					var sonItemState = $(sonItemStates[i]).val();
					if(sonItemState==1){
						flag = false;
						break;
					}
				}
			}
			//提示内容
			var content = "";
			//子任务是否联动完成，默认否
			var tag = "no";
			if(flag){
				content = "标记完成后，项目其他属性将不允许改变";
			}else{
				content = "有子项目没有办结，确定将子项目也标记完成?";
				tag="yes";
			}
			//询问框
			window.top.layer.confirm(content, {
			  btn: ['确定','取消'] //按钮
			}, function(index){
				$.post("/item/remarkItemState?sid=${param.sid}",{Action:"post",id:itemId,state:4,childAlsoRemark:tag},
					function (msgObjs){
						showNotification(1,"操作成功！");
						if(openPageTag=='viewItem'){//子项目界面标记完成
							window.self.location.reload();//刷新当前页面
							openWindow.location.reload();
					  	}else if(openPageTag!=pageTag && openPageTag.indexOf("Item")>=0){
					  		//判断父页面是否需要向前调整
							var url= openWindow.reConUrlByClz("#listItemDiv .optDiv",1);
							openWindow.self.location = url;
							//关闭弹窗
							window.top.layer.close(openTabIndex);
					  	}else{
							window.self.location.reload();//刷新当前页面
					  	}
					} 
				)
				window.top.layer.close(index)
			});
		}else{//项目已办结
			//询问框
			window.top.layer.confirm("是否重新启动项目？", {
			  btn: ['确定','取消'] //按钮
			}, function(index){
				window.top.layer.close(index);
				if(sonItemStates.length>0){
					window.top.layer.open({
						  content: "是否子项目也一起重启？"
						  ,btn: ['是', '否']
						  ,yes: function(index, layero){
							  $.post("/item/remarkItemState?sid=${param.sid}",{Action:"post",id:itemId,state:1,childAlsoRemark:"yes"},     
						 			function (msgObjs){
								  		window.top.layer.msg("操作成功！")
								  		window.self.location.reload();//刷新当前界面
								  		if(openPageTag=='viewItem'){//若是子项目，则需要刷新父界面
											openWindow.location.reload();
									  	}
									});
							  window.top.layer.close(index)
						  },btn2:function(index, layero){
							  $.post("/item/remarkItemState?sid=${param.sid}",{Action:"post",id:itemId,state:1,childAlsoRemark:"no"},     
						 			function (msgObjs){
								 		window.top.layer.msg("操作成功！")
								 		window.self.location.reload();//刷新当前界面
								 		if(openPageTag=='viewItem'){//若是子项目，则需要刷新父界面
											openWindow.location.reload();
									  	}
									});
							  window.top.layer.close(index)
						  },cancel: function(){
						  }
					});
				}else{
					$.post("/item/remarkItemState?sid=${param.sid}",{Action:"post",id:itemId,state:1,childAlsoRemark:"no"},     
			 			function (msgObjs){
							showNotification(1,"操作成功！");
							window.self.location.reload();
							if(openPageTag=='viewItem'){
								openWindow.location.reload();
						  	}
						}
					);
					window.top.layer.close(index)
				}
			},function(){
			});
		}
	}
	//项目属性菜单切换
	$(function(){
		//项目讨论
		$("#itemTalkMenuLi").click(function(){
			$("#otherItemAttrIframe").css("display","block");
			$("#itemBase").css("display","none");
			$(this).parent().find("li").removeAttr("class");
			$("#itemTalkMenuLi").attr("class","active");
			$("#otherItemAttrIframe").attr("src","/item/itemTalkPage?sid=${param.sid}&pager.pageSize=10&itemId=${item.id}&itemState=${item.state}");
		});
		
		//项目讨论
		$("#headItemTalk").click(function(){
			$("#otherItemAttrIframe").css("display","block");
			$("#itemBase").css("display","none");
			$("#itemTalkMenuLi").parent().find("li").removeAttr("class");
			$("#itemTalkMenuLi").attr("class","active");
			$("#otherItemAttrIframe").attr("src","/item/itemTalkPage?sid=${param.sid}&pager.pageSize=10&itemId=${item.id}&itemState=${item.state}&talkFocus='1'");
		});
		//项目配置信息
		$("#itemBaseMenuLi").click(function(){
			$("#otherItemAttrIframe").css("display","none");
			$("#itemBase").css("display","block");
			$(this).parent().find("li").removeAttr("class");
			$("#itemBaseMenuLi").attr("class","active");
		});
		//移交记录
		$("#itemFlowRecordLi").click(function(){
			$("#otherItemAttrIframe").css("display","block");
			$("#itemBase").css("display","none");
			$(this).parent().find("li").removeAttr("class");
			$("#itemFlowRecordLi").attr("class","active");
			$("#otherItemAttrIframe").attr("src","/item/itemFlowRecord?sid=${param.sid}&itemId=${item.id}");
		});
		//项目日志
		$("#itemLogMenuLi").click(function(){
			$("#otherItemAttrIframe").css("display","block");
			$("#itemBase").css("display","none");
			$(this).parent().find("li").removeAttr("class");
			$("#itemLogMenuLi").attr("class","active");
			$("#otherItemAttrIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${item.id}&busType=005&ifreamName=otherItemAttrIframe");
		});
		//项目聊天室
		$("#itemChatLi").click(function(){
			$("#otherItemAttrIframe").css("display","block");
			$("#itemBase").css("display","none");
			$(this).parent().find("li").removeAttr("class");
			$("#itemChatLi").attr("class","active");
			$("#otherItemAttrIframe").attr("src","/chat/listBusChat?sid=${param.sid}&busId=${item.id}&busType=005&ifreamTag=otherItemAttrIframe");
		});
		//项目附件
		$("#itemUpfileMenuLi").click(function(){
			$("#otherItemAttrIframe").css("display","block");
			$("#itemBase").css("display","none");
			$(this).parent().find("li").removeAttr("class");
			$("#itemUpfileMenuLi").attr("class","active");
			$("#otherItemAttrIframe").attr("src","/item/itemUpfilePage?sid=${param.sid}&pager.pageSize=10&itemId=${item.id}&type=other");
		});
		//销售材料
		$("#itemSaleUpfileMenuLi").click(function(){
			$("#otherItemAttrIframe").css("display","block");
			$("#itemBase").css("display","none");
			$(this).parent().find("li").removeAttr("class");
			$("#itemSaleUpfileMenuLi").attr("class","active");
			$("#otherItemAttrIframe").attr("src","/item/itemUpfilePage?sid=${param.sid}&pager.pageSize=10&itemId=${item.id}&type=sale");
		});
		//项目阶段明细
		$("#stagedItemMenuLi").click(function(){
			$("#otherItemAttrIframe").css("display","block");
			$("#itemBase").css("display","none");
			$(this).parent().find("li").removeAttr("class");
			$("#stagedItemMenuLi").attr("class","active");
			$("#otherItemAttrIframe").attr("src","/item/stagedItemPage?sid=${param.sid}&pager.pageSize=10&itemId=${item.id}");
		});
		//客户查看记录
		$("#itemViewRecord").click(function(){
			$("#otherItemAttrIframe").css("display","block");
			$("#itemBase").css("display","none");
			$(this).parent().find("li").removeAttr("class");;
			$("#itemViewRecord").attr("class","active");
			$("#otherItemAttrIframe").attr("src","/common/listViewRecord?sid=${param.sid}&busId=${item.id}&busType=005&ifreamName=otherItemAttrIframe");
		});
		//维护记录
		$("#maintenanceRecord").click(function(){
			$("#otherItemAttrIframe").css("display","block");
			$("#itemBase").css("display","none");
			$(this).parent().find("li").removeAttr("class");;
			$("#maintenanceRecord").attr("class","active");
			$("#otherItemAttrIframe").attr("src","/item/maintenanceRecord?sid=${param.sid}&itemId=${item.id}&pager.pageSize=10");
		});
		//功能清单
		$("#functionList").click(function(){
			$("#otherItemAttrIframe").css("display","block");
			$("#itemBase").css("display","none");
			$(this).parent().find("li").removeAttr("class");;
			$("#functionList").attr("class","active");
			$("#otherItemAttrIframe").attr("src","/functionList/functionListPage?sid=${param.sid}&busId=${item.id}&busType=005&busName=${item.itemName}&iframe=otherItemAttrIframe");
		});
	});
	//项目组关联
	function initItemGroup(groups){
		var grpName="";
		var ids="";
		
		if(!strIsNull(groups)){
			var grp = eval(groups);
			var grpName="";
			for(var i=0;i<grp.length;i++){
				grpName +="<span id=\"itemGrp_"+grp[i].id+"\" style='cursor:pointer;' title=\"双击移除\" class=\"label label-default margin-top-5 margin-left-5 margin-bottom-5\" ondblclick=\"removeItemGrp('"+grp[i].id+"')\">"+grp[i].name;
				grpName +="<input type=\"hidden\" name=\"listShareGroup["+i+"].id\" value=\""+grp[i].id+"\"/>";
				grpName +="</span>";
				
				ids +="grpId="+grp[i].id+"&";
			}
			ids = ids.substr(0,ids.length-1);
		}
		$.post("/item/editItemGroup?sid=${param.sid}&"+ids,{Action:"post",itemId:${item.id}},     
			function (msgObjs){
				if(msgObjs.succ){
					showNotification(0,msgObjs.promptMsg);
				}else{
					showNotification(1,msgObjs.promptMsg);
				}
			},"json");
		$("#itemGroupName").html(grpName);
	}

	function removeItemGrp(plId,grpId,itemId,grpName){
		$.ajax({
			type : "post",
			url : "/item/delItemGroup?sid=${param.sid}&rnd="+Math.random(),
			dataType:"json",
			data:{itemId:itemId,
				grpId:grpId,
				grpName:grpName},
	        success : function(data){
			        showNotification(0,data.info);
			        $("#"+plId).remove();
			        $("#selfGrouplist").find("option[value='"+grpId+"']").remove();

			},
		    error:  function(XMLHttpRequest, textStatus, errorThrown){
		   	 showNotification(2,"系统错误，请联系管理人员！");
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
	//项目服务期限更新
	function selectDate(val){
		var serviceDate = $("#serviceDate").attr("preval");
		if(val && serviceDate!=val){
			$.post("/item/updateServiceDate?sid=${param.sid}",{Action:"post",id:${item.id},serviceDate:$("#serviceDate").val()},     
 				function (msgObjs){
				showNotification(1,msgObjs);
				$("#serviceDate").attr("preval",val)
			});
		}
	}
	
	//项目进度更新
	function updateProgress(id,startTime){
		if(startTime ){
			window.top.layer.confirm('确定项目进度还原到该阶段吗？',{icon:3,title:'询问框'}, function(index){
				$.post("/item/updateItemProgress?sid=${param.sid}",{Action:"post",id:id,startTime:startTime},     
						function (data){
					showNotification(1,"还原成功");
					window.top.layer.close(index);
					window.location.reload();
				},"json");
			});	
		}else{
			window.top.layer.confirm('确定要更新项目进度到该阶段吗？',{icon:3,title:'询问框'}, function(index){
				$.post("/item/updateItemProgress?sid=${param.sid}",{Action:"post",id:id},     
						function (data){
					showNotification(1,data.data);
					window.top.layer.close(index);
					window.location.reload();
				},"json");
			});	
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
		if($("#productId").val() != obj.id){
			window.top.layer.confirm('更新所属产品后会重置项目功能清单，确定更新？',{icon:3,title:'询问框'}, function(index){
				$("#productId").val(obj.id);
				$("#productName").val(obj.name+"_V"+obj.version);
				$("#productManager").val(obj.managerName);
				$.post("/item/updateProduct?sid=${param.sid}",{Action:"post",id:'${item.id}',productId:obj.id},     
					function (data){
					showNotification(1,data.data);
				},"json");
			});	
		}
	}
	//移除产品关联
	function removePro(){
		$("#productId").val('');
		$("#productName").val('');
		$("#productManager").val('');
		$.post("/item/updateProduct?sid=${param.sid}",{Action:"post",id:'${item.id}',productId:''},     
			function (data){
			showNotification(1,data.data);
		},"json");
	}
</script>
</head>
<body onload="handleName()">
<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget no-margin-bottom" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                	
                		<a href="javascript:void(0)" class="widget-caption blue padding-right-5"
               				attentionState="${item.attentionState}" busType="005" busId="${item.id}" describe="0" iconSize="sizeMd"
               				onclick="changeAtten('${param.sid}',this)" title="${item.attentionState==0?"关注":"取消"}">
							<i class="fa ${item.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>
						</a>
						
                        <span class="widget-caption themeprimary" style="font-size: 20px">项目</span>
                        <span class="widget-caption themeprimary" style="font-size: 15px;margin-top: 2px" id="titleItemName">
                        	<c:choose>
                        		<c:when test="${fn:length(item.itemName)>20 }">
		                        	--${fn:substring(item.itemName,0,20)}..
                        		</c:when>
                        		<c:otherwise>
                        			--${item.itemName}
                        		</c:otherwise>
                        	</c:choose>
                        </span>
                        <div class="widget-buttons ps-toolsBtn">
                        	<c:choose>
									<c:when test="${item.state==1}">
									
										<a href="javascript:void(0)" class="purple" id="headItemTalk" title="留言">
											<i class="fa fa-comments"></i>留言
										</a>
										
			                        	<a href="javascript:void(0)" class="blue" id="itemHandOver">
											<i class="fa fa-h-square"></i>移交
										</a>
									</c:when>
									<c:otherwise>
										<a href="javascript:void(0)" class="blue" onclick="remarkItemState(-1,${item.id},${item.state });">
												<i class="fa fa-power-off"></i>重启
											</a>
									</c:otherwise>
								</c:choose>
								
								<!-- 判断显示按钮 -->
								<a class="green ps-point margin-right-0" data-toggle="dropdown" title="更多">
	                               <i class="fa fa-th"></i>更多
	                           	</a>
	                           	  <ul class="pull-right dropdown-menu dropdown-arrow ps-layerUl">
		                           	  <c:choose>
										<c:when test="${item.state==1}">
											<li>
		                                     <a href="javascript:void(0)" onclick="remarkItemState(-4,${item.id},${item.state });">
		                                         <div class="clearfix">
		                                          	<i class="fa fa-power-off"></i>
		                                           <span class="title ps-topmargin">完成项目</span>
		                                         </div>
		                                     </a>
		                                 </li>
		                                 <li>
		                                     <a href="javascript:void(0)" onclick="addBusTask('${item.id}','005');">
		                                         <div class="clearfix">
		                                          	<i class="fa fa-flag"></i>
		                                           <span class="title ps-topmargin">任务安排</span>
		                                         </div>
		                                     </a>
		                                 </li>
										<li>
		                                     <a href="javascript:void(0)" id="resolveItem">
		                                         <div class="clearfix">
		                                          	<i class="fa fa-random"></i>
		                                           <span class="title ps-topmargin">分解项目</span>
		                                         </div>
		                                     </a>
		                                 </li>
										</c:when>
									</c:choose>
									<li>
                                      <a href="javascript:void(0)" onclick="addClock(${item.id},'005','${param.sid}')">
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
                     </div>
                     <!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->       
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                           <div class="widget-body no-shadow">
                           		<div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat" style="padding-top:0;">
											<li id="stagedItemMenuLi">
												<a href="javascript:void(0)"  data-toggle="tab">阶段管理</a>
											</li>
											<li  id="itemBaseMenuLi" class="active">
												<a href="javascript:void(0)"  data-toggle="tab">项目详情</a>
											</li>
											<li  id="functionList">
												<a href="javascript:void(0)"  data-toggle="tab">功能清单</a>
											</li>
                                            <li id="itemTalkMenuLi">
												<a href="javascript:void(0)" data-toggle="tab">留言<c:if test="${item.talkNum > 0}"><span style="color:red;font-weight:bold;">（${item.talkNum}）</span></c:if></a>
											</li>
											<li id="maintenanceRecord">
												<a href="javascript:void(0)" data-toggle="tab">维护记录<c:if test="${item.maintenanceRecordNum > 0}"><span style="color:red;font-weight:bold;">（${item.maintenanceRecordNum}）</span></c:if></a>
											</li>
											<li id="itemLogMenuLi">
												<a href="javascript:void(0)" data-toggle="tab">操作日志</a>
											</li>
											<li id="itemUpfileMenuLi">
												<a href="javascript:void(0)" data-toggle="tab">文档<c:if test="${item.fileNum > 0}"><span style="color:red;font-weight:bold;">（${item.fileNum}）</span></c:if></a>
											</li>
											<li id="itemSaleUpfileMenuLi">
												<a href="javascript:void(0)" data-toggle="tab">销售材料<c:if test="${item.saleFileNum > 0}"><span style="color:red;font-weight:bold;">（${item.saleFileNum}）</span></c:if></a>
											</li>
											<li id="itemViewRecord">
                                                 <a data-toggle="tab" href="javascript:void(0)">阅读情况</a>
                                            </li>
											<%--<li id="itemViewRecord">--%>
                                                 <%--<a data-toggle="tab" href="javascript:void(0)">最近查看</a>--%>
                                            <%--</li>--%>
											<!-- <li id="itemFlowRecordLi">
												<a href="javascript:void(0)" data-toggle="tab">移交记录</a>
											</li> -->
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 	<div id="itemBase" style="display:block;">
												<jsp:include page="./itemBase_edit.jsp"></jsp:include>
											</div>
                                    	 	<iframe id="otherItemAttrIframe" style="display:none;" class="layui-layer-load"
												src=""
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
    <script src="/static/assets/js/bootstrap.min.js"></script>
    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
