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
    <script src="/static/assets/js/jquery-ui.js"></script>
<script type="text/javascript">
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	//打开页面body
	var openWindowDoc;
	//打开页面,可调用父页面script
	var openWindow;
	var index;
	//注入父页面信息
	function setWindow(winDoc,win,pIndex){
		openWindowDoc = winDoc;
		openWindow = win;
		index = pIndex;
	}
	
	
	//关闭弹窗
	function closeWindow(){
		var changeState = $("#changeState").val();
		if(changeState=='1'){
			openWindow.ReLoad();
		}
		openWindow.layer.close(index);
	}
	
	//修改模块展示开启状态
	function updateAddSoonSet(ts,busType,sid){
		var openState = 0;
		if($(ts).attr("checked")){
			openState =1;
		}
		$.post("/menu/updateAddSoonSet",{Action:"post",sid:sid,busType:busType,openState:openState},    
				function (data){
				if(data.status=='y'){
					$("#changeState").val(1);
					showNotification(1,"设置成功,刷新后生效");
				}else{
					showNotification(2,data.info);
					if($(ts).attr("checked")){
						$(ts).attr("checked",false);
					}else{
						$(ts).attr("checked",true);
					}
				}
			},"json");
	}
	//修改模块展示大的大小
	function updateMenuWidth(ts,busType,sid){
		var width = $(ts).val();
		$.post("/menu/updateMenuWidth",{Action:"post",sid:sid,busType:busType,width:width},    
				function (data){
				if(data.status=='y'){
					showNotification(1,"设置成功,刷新后生效");
				}else{
					showNotification(2,data.info);
				}
			},"json");
	}
	
	var preOrder;
	//排序
	$(function() {
		 //设置滚动条高度
		var height = $(window).height()-45;
		$("#contentBody").css("height",height+"px"); 
		
        $(".sortable" ).sortable({
        cursor: "move",
        items :"li",                        //只是li可以拖动
        opacity: 0.9,                       //拖动时，透明度为0.6
        axis: 'y',							//只在纵向拖拽
        containment: '#contentBody',		//拖拽区域
        revert: false,						//释放时，增加动画
        start: function(event, ui){			//拖拽前保存原始数据
        	preOrder = $(this).sortable("toArray");
        	$.each(preOrder, function(i, addSoonSet){
        		$("#"+addSoonSet[i]).attr("style","cursor:move");
        	});
        }, 
        update : function(event, ui){       //更新排序之后
        	var nowOrder = $(this).sortable("toArray");
        	var result = new Array();
        	for(var i=0;i<nowOrder.length;i++){
        		if(nowOrder[i]!=preOrder[i]){
        			var openState = $("#"+nowOrder[i]).find(":checkbox").attr("checked");
        			$("#"+nowOrder[i]).attr("style","cursor:move");
        			$("#"+nowOrder[i]).find(".lanmu-order").html((i+1));
        			
        			var opened = 0;
        			if(openState){
        				opened = 1;
        			}
        			result.push("{'busType':'"+nowOrder[i]+"','orderNo':"+(i+1)+",'opened':'"+opened+"'}");
        		}
        	}
        	$.ajax({
	       		 type: "post",
	       		 url:"/menu/updateAddSoonOrder?sid=${param.sid}",
	       		 data:{addSoonSetStr:result.join("@")},
	       		 dataType: "json",
	       		 async:false,//同步	
	       		 success:function(data){
	       			if(data.status=='y'){
	       				$("#changeState").val(1);
    					showNotification(1,"设置成功,刷新后生效");
    					return true;
    				}else{
    					showNotification(2,data.info);
    					return false;
    				}
	       		 }
	       	 });
        },stop:function(event, ui){
        	$.each(preOrder, function(i, addSoonSet){
        		$("#"+addSoonSet[i]).attr("style","cursor:move");
        	});
        }
       });
     });
	//重置个人首页栏目设置
	function resetAddSoon(){
		$.ajax({
      		 type: "post",
      		 url:"/menu/updateAddSoonReset?sid=${param.sid}",
      		 dataType: "json",
      		 success:function(data){
      			if(data.status=='y'){
      				$("#changeState").val(1);
					showNotification(1,"设置成功,刷新后生效");
					//取得的首页栏目
					var listAddSoon = data.listAddSoon;
					if(listAddSoon.length>0){
						var html ='';
						var setIndex = 1;
						$.each(listAddSoon, function(i, addSoonSet){   
							html+='\n <li class="list-group-item ui-state-default" id="'+addSoonSet.busType+'" style="cursor:move;">';
							html+='\n 	<span class="margin-right-10 lanmu-order badge bg-themeprimary badge-square pull-left">'+(setIndex++)+'</span>';
							html+='\n 	<span class="pull-right">';
							html+='\n 		<label>';
							html+='\n 			<input class="checkbox-slider slider-icon colored-blue" name="openState" type="checkbox"';
							if(addSoonSet.openState =='1'){
								html+='\n checked ';
							}
								html+='\n  onclick="updateAddSoonSet(this,\''+addSoonSet.busType+'\',\'${param.sid }\')">';
							html+='\n 			<span class="text"></span>';
							html+='\n		</label>';
							html+='\n	</span>'+addSoonSet.menuTitle;
							html+='\n</li>';
						}); 
						$("#menuSetList").html(html);
					}
				}else{
					showNotification(2,data.info);
				}
      		 }
      	 });
	}
</script>
</head>
<body>
	
<input type="hidden" id="changeState" value="0">
	<div class="container" style="padding: 0px 0px;width: 100%;background-color: #fff;">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px;margin: 0 0">
            	<div class="widget" style="padding: 0px 0px;margin: 0 0" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">快捷添加设置</span>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 --> 
                 	<div class="widget margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<ul class="list-group sortable" id="menuSetList" style="margin-bottom:10px;margin-top:10px" >
                     		<c:choose>
                     			<c:when test="${not empty listAddSoon}">
                     				<c:forEach items="${listAddSoon}" var="addSoonSet" varStatus="vs">
                     					<li class="list-group-item ui-state-default" id="${addSoonSet.busType}" style="cursor:move;">
                     						<span class="margin-right-10 lanmu-order badge bg-themeprimary badge-square pull-left">${vs.count}</span>
			                           		<span class="pull-right">
			                           			<label class=" no-margin-bottom">
													<input class="checkbox-slider slider-icon colored-blue" name="openState"
													type="checkbox" ${addSoonSet.openState eq '0'?'':'checked'} onclick="updateAddSoonSet(this,'${addSoonSet.busType}','${param.sid }')">
													<span class="text" ></span>
												</label>
			                           		</span>${addSoonSet.menuTitle}
			                           	</li>
                     				</c:forEach>
                     			</c:when>
                     		</c:choose>
                          </ul> 
                     </div>
					
				</div>
			</div>
		</div>
	</div>
</body>
</body>
</html>
