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

 <!--Beyond styles-->
<link href="/static/assets/css/beyond.min.css" rel="stylesheet"  type="text/css">
<link href="/static/assets/css/beyond_2.min.css" rel="stylesheet"  type="text/css">
<link href="/static/assets/css/animate.min.css" rel="stylesheet" type="text/css">
<script src="/static/assets/js/skins.min.js"></script>


<!--Core CSS -->
<link href="/static/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" />
<link href="/static/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="/static/assets/css/new_file.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/plugins/layui/layui.js"></script>
<link rel="stylesheet" href="/static/plugins/layui/css/layui.css">
<script src="/static/plugins/layer/layer.js" type="text/javascript" charset="utf-8"></script>

<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
 <style>
 .ztree li span {
    line-height: 16px;
    margin-right: 10px;
}
#noData a:hover{
	background:rgba(0,0,0,.1);
} 
 </style>
<SCRIPT type="text/javascript">

		//关闭窗口
		function closeWin(){
			var winIndex = window.top.layer.getFrameIndex(window.name);
			closeWindow(winIndex);
		}
		<!--
		var setting = {
			view: {
				addHoverDom: addHoverDom,
				removeHoverDom: removeHoverDom,
				selectedMulti: false
			},
			edit: {
				drag: {
					autoExpandTrigger: true,
					prev: dropPrev,
					inner: dropInner,
					next: dropNext
				},
				enable:${area.enable},
				showRemoveBtn: showRemoveBtn,
				showRenameBtn: showRenameBtn,
				editNameSelectAll: true
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeDrag: beforeDrag,
				beforeDrop: beforeDrop,
				beforeDragOpen: beforeDragOpen,
				onDrag: onDrag,
				onDrop: onDrop,
				onExpand: onExpand,
				//onRemove: zTreeOnRemove,
				beforeRemove: zTreeBeforeRemove,
				onRemove:onRemove
			}
		};

		var zNodes =[
			{ id:1, pId:0, name:"随意拖拽 1", open:true},
			{ id:12, pId:1, name:"随意拖拽 1-2"},
			{ id:121, pId:12, name:"随意拖拽 1-2-1"},
			{ id:122, pId:12, name:"随意拖拽 1-2-2"},
			{ id:123, pId:12, name:"随意拖拽 1-2-3"},
			{ id:11, pId:1, name:"随意拖拽 1-1"},
			{ id:13, pId:1, name:"禁止拖拽 1-3", open:true, drag:false},
			{ id:131, pId:13, name:"禁止拖拽 1-3-1", drag:false},
			{ id:132, pId:13, name:"禁止拖拽 1-3-2", drag:false},
			{ id:132, pId:13, name:"禁止拖拽 1-3-3", drag:false},
			{ id:2, pId:0, name:"禁止子节点移走 2", open:true, childOuter:false},
			{ id:21, pId:2, name:"我不想成为父节点 2-1", dropInner:false},
			{ id:22, pId:2, name:"我不要成为根节点 2-2", dropRoot:false},
			{ id:23, pId:2, name:"拖拽试试看 2-3"},
			{ id:3, pId:0, name:"禁止子节点排序/增加 3", open:true, childOrder:false, dropInner:false},
			{ id:31, pId:3, name:"随意拖拽 3-1"},
			{ id:32, pId:3, name:"随意拖拽 3-2"},
			{ id:33, pId:3, name:"随意拖拽 3-3"}
		];
		//拖拽到目标节点时，设置是否允许移动到目标节点前面的操作
		function dropPrev(treeId, nodes, targetNode) {
			var pNode = targetNode.getParentNode();
			if (pNode && pNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					var curPNode = curDragNodes[i].getParentNode();
					if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}
		//拖拽到目标节点时，设置是否允许成为目标节点的子节点
		function dropInner(treeId, nodes, targetNode) {
			if (targetNode && targetNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					if (!targetNode && curDragNodes[i].dropRoot === false) {
						return false;
					} else if (curDragNodes[i].parentTId && curDragNodes[i].getParentNode() !== targetNode && curDragNodes[i].getParentNode().childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}
		//拖拽到目标节点时，设置是否允许移动到目标节点后面的操作
		function dropNext(treeId, nodes, targetNode) {
			var pNode = targetNode.getParentNode();
			if (pNode && pNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					var curPNode = curDragNodes[i].getParentNode();
					if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}

		var log, className = "dark", curDragNodes, autoExpandNode;
		//用于捕获节点被拖拽之前的事件回调函数，并且根据返回值确定是否允许开启拖拽操作
		function beforeDrag(treeId, treeNodes) {
			/*className = (className === "dark" ? "":"dark");
			//showLog("[ "+getTime()+" beforeDrag ]&nbsp;&nbsp;&nbsp;&nbsp; drag: " + treeNodes.length + " nodes." );
			for (var i=0,l=treeNodes.length; i<l; i++) {
				if (treeNodes[i].drag === false) {
					curDragNodes = null;
					return false;
				} else if (treeNodes[i].parentTId && treeNodes[i].getParentNode().childDrag === false) {
					curDragNodes = null;
					return false;
				}
			}
			curDragNodes = treeNodes;
			return true;
			*/
			return false;
		}
		//用于捕获拖拽节点移动到折叠状态的父节点后，即将自动展开该父节点之前的事件回调函数，并且根据返回值确定是否允许自动展开操作
		function beforeDragOpen(treeId, treeNode) {
			//autoExpandNode = treeNode;
			return false;
		}
		//用于捕获节点拖拽操作结束之前的事件回调函数，并且根据返回值确定是否允许此拖拽操作
		function beforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
			className = (className === "dark" ? "":"dark");
			//showLog("[ "+getTime()+" beforeDrop ]&nbsp;&nbsp;&nbsp;&nbsp; moveType:" + moveType);
			//showLog("target: " + (targetNode ? targetNode.name : "root") + "  -- is "+ (isCopy==null? "cancel" : isCopy ? "copy" : "move"));
			return false;
		}
		//被拖拽者信息事件
		function onDrag(event, treeId, treeNodes) {
			//className = (className === "dark" ? "":"dark");
			//showLog("[ "+getTime()+" onDrag ]&nbsp;&nbsp;&nbsp;&nbsp; drag: " + treeNodes.length + " nodes." );
			return false;
		}
		//拖拽父节点存放事件
		function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
			//if("folder"!=targetNode.type){showNotification(0,"非文件夹不能作为父节点！");return false;}
			/*if(!strIsNull(treeNodes[0].id)&&!strIsNull(targetNode.id)){
				$.post("/crm/zTreeOnDrop?sid=${param.sid}",{Action:"post",nodeId:treeNodes[0].id,pId:targetNode.id},     
	 				function (msgObjs){
					showNotification(0,msgObjs);
				});
			}
			className = (className === "dark" ? "":"dark");
			*/
			//showLog("[ "+getTime()+" onDrop ]&nbsp;&nbsp;&nbsp;&nbsp; moveType:" + moveType);
			//showLog("target: " + (targetNode ? targetNode.name : "root") + "  -- is "+ (isCopy==null? "cancel" : isCopy ? "copy" : "move"))
			return false;
		}
		//用于捕获节点被展开的事件回调函数
		function onExpand(event, treeId, treeNode) {
			if (treeNode === autoExpandNode) {
				className = (className === "dark" ? "":"dark");
				//showLog("[ "+getTime()+" onExpand ]&nbsp;&nbsp;&nbsp;&nbsp;" + treeNode.name);
			}
		}

		function showLog(str) {
			if (!log) log = $("#log");
			log.append("<li class='"+className+"'>"+str+"</li>");
			if(log.children("li").length > 8) {
				log.get(0).removeChild(log.children("li")[0]);
			}
		}
		//JS获取时间
		function getTime() {
			var now= new Date(),
			h=now.getHours(),
			m=now.getMinutes(),
			s=now.getSeconds(),
			ms=now.getMilliseconds();
			return (h+":"+m+":"+s+ " " +ms);
		}
		//设置是否显示删除按钮。
		function showRemoveBtn(treeId, treeNode) {
			//alert("在这里控制删除按钮！");
			//return !treeNode.isFirstNode;
			return true;
		}
		//设置是否显示编辑名称按钮
		function showRenameBtn(treeId, treeNode) {
			//alert("在这里控制编辑按钮！");
			//return !treeNode.isLastNode;
			return false;
		}
		var newCount = 1;
		//TreeNode的进行对象
		var actionTreeNode=null;
		//用于当鼠标移动到节点上时，显示用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮
		function addHoverDom(treeId, treeNode) {
			//判断树形结构是否可编辑，不可编辑直接返回
			if(!${area.enable}){return;}
			actionTreeNode = treeNode;
			var sObj = $("#" + treeNode.tId + "_span");
			if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0 || !treeNode.isParent) return;
			var addStr = "<span class='fa fa-plus-circle fa-lg' style='position: inherit;' id='addBtn_" + treeNode.tId
				+ "' title='导入区域' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var btn = $("#addBtn_"+treeNode.tId);
			if (btn) btn.bind("click", function(){
				//areaId,regionId,level,areaName
				//区域主键作为导入区域的父类主键
				modRegion(treeNode.areaId,treeNode.areaId,treeNode.regionId,treeNode.level,treeNode.name)
				//addArea(treeNode.id);
				//var zTree = $.fn.zTree.getZTreeObj("zTreeArea");
				//zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
				//return false;
			});
		};
		//用于当鼠标移出节点时，隐藏用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮
		function removeHoverDom(treeId, treeNode) {
			$("#addBtn_"+treeNode.tId).unbind().remove();
		};
		
		function setTrigger() {
			var zTree = $.fn.zTree.getZTreeObj("zTreeArea");
			zTree.setting.edit.drag.autoExpandTrigger = $("#callbackTrigger").attr("checked");
		}
		var loadingIndex;
		//初始化树形结构数据
		$(document).ready(function(){
			//启动加载页面效果
			loadingIndex = layer.load(0, {shade: [0.6,'#fff']});
			initZtree()
			/*
			{
			if(${areaJsonStr}.length==0){
				$("#editable-sample").hide();
				$("#noData").show();
			}
			$.fn.zTree.init($("#zTreeArea"), setting,${areaJsonStr});
			var zTree = $.fn.zTree.getZTreeObj("zTreeArea");
			//var n = zTree.getNodes();
			//zTree.expandNode(n[0],true,true,false);
			
			var nodes = zTree.getNodes();
			for(var i=0;i<nodes.length;i++){  
	            zTree.expandNode(nodes[i],null,true,false,false);
	        }  
			
			$("#callbackTrigger").bind("change", {}, setTrigger);
			*/
		});
		
		function initZtree(){
			//销毁所有的zTree
			$.fn.zTree.destroy();
			zNodes = new Array();
			//通过ajax获取部门树
			$.post("/crm/ajaxGetArea4Edit", {
				random : Math.random(),
				sid:"${param.sid}"}, 
				function(data) {
				var tree = data.listArea;
				if(tree!=null && tree.length>0){
					$("#editable-sample").show();
					$("#noData").hide();
					for ( var i = 0; i < tree.length; i++) {
							var node = new Object;
							
							node.id = tree[i].id;
							node.pId = tree[i].parentId;
							node.regionId = tree[i].regionId;
							node.name = tree[i].areaName;
							if(tree[i].isLeaf==0){
								node.open = false;
								node.nocheck = true;
								node.ztype = 1;
							}else{
								node.ztype = 0;
								
							}
							zNodes.push(node);
					}
					
				}else{
					$("#editable-sample").hide();
					$("#noData").show();
				}
				$("#zTreeArea").empty();
				$.fn.zTree.init($("#zTreeArea"), setting, zNodes);
				layer.close(loadingIndex);
				loadingIndex = null;
			},"json");
		}
		
		$(function(){
			$("#updown").on("click",function(){
				var upstate = $(this).attr("upstate");
				if(upstate==0 || upstate=="0"){
					//展开树形结构
					var zTree = $.fn.zTree.getZTreeObj("zTreeArea");
					zTree.expandAll(true);
					$(this).attr("upstate","1")
					$(this).attr("title","收起")
				}else{
					//关闭树形结构
					var zTree = $.fn.zTree.getZTreeObj("zTreeArea");
					zTree.expandAll(false);
					$(this).attr("upstate","0")
					$(this).attr("title","展开")
				}
				$(this).toggleClass("fa-angle-double-down").toggleClass("fa-angle-double-up");
			})
			
			//设置滚动条高度
			var height = $(window).height()-40;
			$("#contentBody").css("height",height+"px");
		})
		//摧毁树形结构
		function zTreeDestroy(){
			var zTree = $.fn.zTree.getZTreeObj("zTreeArea");
			zTree.destroy();
		}
		//根据返回值构建树形数据
		function valueReturn(objJson){
			<%--
			if(!strIsNull(objJson)){
				$.ajax({
				   type: "POST",
				   async:false,
				   url: "/item/addStagedInfo?sid=${param.sid}",
				   data: "itemId=&stagedItemId="+actionTreeNode.realId+"&moduleId="+objJson.id+"&moduleType=task",
				   success: function(msg){showNotification(1,msg);}
				});
			}
			--%>
		}	
		//删除节点前回调函数
		function zTreeBeforeRemove(treeId, treeNode) {
			window.top.layer.confirm("确定删除\""+treeNode.name+"\"?",{icon: 3, title:'提示'},  
			function(index){
				window.top.layer.close(index);
				 var zTree = $.fn.zTree.getZTreeObj("zTreeArea");
				 $.ajax({
					   type: "POST",
					   async:false,
					   dataType : "json", 
					   url: "/crm/zTreeRemove?sid=${param.sid}",
					   data: "nodeId="+treeNode.id+"&delChildren=yes",
					   success: function(data){
						   if(data.status=='y'){
							  zTree.removeNode(treeNode);  
							  showNotification(1,data.info);
						   }else{
							   window.top.layer.alert(data.info, {title:"警告",icon: 5,btn:["关闭"]});  
						   }
					   }
					});
				 
			});
			return false;
		}
		//删除节点后检查是否还有其他节点
		function onRemove(treeId, treeNode){
			var zTree = $.fn.zTree.getZTreeObj("zTreeArea");
			 var nodes = zTree.getNodes();
			 if(nodes.length<1||nodes==null){
				$("#editable-sample").hide();
				$("#noData").show();
			 }
		}
		//区域根目录维护
		function addArea(pId){
			layui.use('layer', function(){
				var layer = layui.layer;
				window.top.layer.open({
					  type: 2,
					  title:false,
					  closeBtn:0,
					  //title: ['添加区域', 'font-size:18px;'],
					  area: ['500px', '275px'],
					  fix: true, //不固定
					  maxmin: false,
					  move: false,
					  content: ["/crm/addAreaPage?sid=${param.sid}&parentId="+pId,"no"],
					  btn: ['添加', '取消'],
					  yes: function(index, layero){
						  var iframeWin =window.top.window[layero.find('iframe')[0]['name']];
						  var flag = iframeWin.formSub();
						  if(flag){
							  window.top.layer.close(index);
							  window.top.layer.msg("添加成功",{icon:1})
							window.self.location.reload();
						  }
					  }
					  ,cancel: function(){ 
					    //右上角关闭回调
					  }
					});
			});
		}
		//-->
		function heightReSet(){
			setTimeout(function(){
				scrollPage();
			},200);
			
		}
		//从模板导入
		/**
		* areaId 区域主键
		* regionId 模板主键
		* level 等级0全国的省 1省级子区域  2区县的子区域 
		* areaName 区域名称
		*/
		function modRegion(parentId,areaId,regionId,level,areaName){
			var areaParam={"areaId":areaId,
					"regionId":regionId,
					"level":level,
					"areaName":areaName,
					"parentId":parentId};
			layui.use('layer', function(){
				var layer = layui.layer;
				window.top.layer.open({
					  type: 2,
					  title:false,
					  closeBtn:0,
					  //title: ['从模板导入区域', 'font-size:18px;'],
					  area: ['450px', '480px'],
					  fix: true, //不固定
					  maxmin: false,
					  move: false,
					  content: ["/crm/modAreaPage?sid=${param.sid}&t="+Math.random(),'no'],
					  btn: ['导入', '关闭'],
					  success:function(layero,index){
						  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
						  
						  iframeWin.initValue(areaParam);
					  },yes: function(index, layero){
						  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
						  var flag = iframeWin.addArea();
						  if(flag){
							  window.top.layer.close(index); 
							window.location.reload();
						  }else{
							  return false;
						  }
					  },
					  //取消导入，判断是否有数据
					 btn2:function(index,layero){
						 var zTree = $.fn.zTree.getZTreeObj("zTreeArea");
						 var nodes = zTree.getNodes();
						 if(nodes.length<1||nodes==null){
							$("#editable-sample").hide();
							$("#noData").show();
						 }

					 },
					 cancel: function(){ 
					    //右上角关闭回调
						window.location.reload();
					  }
					});
			});
				
		}
	</SCRIPT>
	<style type="text/css">
		.layui-layer-loading{
			width:60px !important;
			left: 250px !important;
		}
	</style>
</HEAD>

<body>
<div class="container no-padding" style="width: 100%">
	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">客户区域维护</span>
	             	<div class="widget-buttons ps-toolsBtn">
						<a href="javascript:void(0)" class="blue" onclick="modRegion(-1,0,0,-1,'');">
							<i class="fa fa-arrow-down"></i>从模板导入
						</a>
					</div>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div  id="contentBody" class="widget-body margin-top-40 no-padding padding-left-20" style="position: relative;">
					<div id="editable-sample"  style="display: none">
						<div class="ws-question ws-question-in">
							<div class="ws-other-icon" style="background-color: #fff;width: 100%">
								<div style="float:right;">
									<a href="javascript:void(0)" class="fa fa-angle-double-down right" id="updown" 
									upstate="0" style="color:blue;font-size: 18px" title="收起"></a>
								</div>
							</div>
							<div id="contentBody" class="content_wrap" style="clear:both;overflow: hidden;overflow-y:auto;height: 385px;">
								<div class="zTreeDemoBackground left">
									<ul id="zTreeArea" class="ztree"></ul>
								</div>
							</div>
						</div>
					</div>
					
					<div id="noData" style="display: none;padding-top:100px">
						<section class="error-container text-center">
							<h1>
								<i class="fa fa-exclamation-triangle"></i>
							</h1>
							<div class="error-divider">
								<h2>您还没有相关数据！</h2>
								<p class="description">协同提高效率，分享拉近距离。</p>
								<a href="javascript:void(0);" onclick="modRegion(-1,0,0,-1,'');"
									class="return-btn"><i class="fa fa-plus"></i>从模板导入</a>
							</div>
						</section>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
