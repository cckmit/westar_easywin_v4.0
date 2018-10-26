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
var sid = '${param.sid}'
var vali;
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
	});
	//初始化项目参与人
	$(document).ready(function(){
		if(!strIsNull($("#itemSharerJson").val())){
			var users = eval("("+$("#itemSharerJson").val()+")");
			var img="";
			 for (var i=0, l=users.length; i<l; i++) {
				 var userImgSrc="";
					userImgSrc = "/downLoad/userImg/${userInfo.comId}/"+users[i].userID+"?sid=${param.sid}";
					img = img + "<div class=\"online-list margin-top-5 margin-left-5 margin-bottom-5\" style=\"float:left\">";
					img = img + "<img  src=\""+userImgSrc+"\" class=\"user-avatar\"/>"
					img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
					img = img + "</div>"
				}	
			$("#itemSharor_div").html(img);
		}
		
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
	});

	//查看子项目
	function viewSonItem(sonItemId){
		$.post("/item/authorCheck?sid=${param.sid}",{Action:"post",itemId:sonItemId},    
				function (msgObjs){
					msgObjs = eval('(' + msgObjs + ')');
					if(!msgObjs.succ){
						showNotification(2,msgObjs.promptMsg)
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
			$("#otherItemAttrIframe").attr("src","/functionList/functionListPage?sid=${param.sid}&busId=${item.id}&busType=005&busName=${item.itemName}&fromType=view&iframe=otherItemAttrIframe");
		});
	});

</script>
</head>
<body >
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget no-margin-bottom" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px;">项目</span>
                        <span class="widget-caption themeprimary" style="font-size: 15px;margin-top: 2px">
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
							
							
							<!-- 判断显示按钮 -->
							<c:if test="${item.state==1}">
								<a href="javascript:void(0)" class="purple" id="headItemTalk" title="留言">
									<i class="fa fa-comments"></i>留言
								</a>
                        	</c:if>
                        	<c:if test="${item.state==4 && reStartSate==1}">
                        		<a href="javascript:void(0)" class="blue" onclick="remarkItemState(-1,${item.id},${item.state });">
									<i class="fa fa-power-off"></i>重启
								</a>
                        	</c:if>
                        	<!-- 判断显示按钮 -->
							<a class="green ps-point margin-right-0" data-toggle="dropdown" title="更多">
                               <i class="fa fa-th"></i>更多
                           	</a>
                          	<ul class="pull-right dropdown-menu dropdown-arrow ps-layerUl">
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
                                      	<ul class="nav nav-tabs tabs-flat no-padding" style="padding-top:0;">
											<li id="stagedItemMenuLi">
												<a href="javascript:void(0)"  data-toggle="tab">阶段管理</a>
											</li>
                                           
											<li  id="itemBaseMenuLi" class="active">
												<a href="javascript:void(0)"  data-toggle="tab">项目详情</a>
											</li>
											<li  id="functionList">
												<a href="javascript:void(0)"  data-toggle="tab">功能清单</a>
											</li>
											 <li id="itemTalkMenuLi" >
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
												<jsp:include page="./itemBase_view.jsp"></jsp:include>
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
