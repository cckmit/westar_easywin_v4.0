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

 <style>
 .ztree li span {
    line-height: 16px;
    margin-right: 10px;
}
 </style>
<SCRIPT type="text/javascript">
function returnItemStage(){
	var zTree = $.fn.zTree.getZTreeObj("zTreeStagedItem");
	var nodes=zTree.getCheckedNodes();
	if(nodes.length==1){
		var node={"id":nodes[0].realId,"name":nodes[0].name};
		return node;
	}else{
		return null;
	}
}
		<!--
		var setting = {
			check:{
				enable :true ,
				chkStyle : "radio",
				radioType: "all"
			},
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
				enable:${stagedItem.stagedEnable},
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
				beforeRemove: zTreeBeforeRemove
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
			className = (className === "dark" ? "":"dark");
			showLog("[ "+getTime()+" beforeDrag ]&nbsp;&nbsp;&nbsp;&nbsp; drag: " + treeNodes.length + " nodes." );
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
		}
		//用于捕获拖拽节点移动到折叠状态的父节点后，即将自动展开该父节点之前的事件回调函数，并且根据返回值确定是否允许自动展开操作
		function beforeDragOpen(treeId, treeNode) {
			autoExpandNode = treeNode;
			return true;
		}
		//用于捕获节点拖拽操作结束之前的事件回调函数，并且根据返回值确定是否允许此拖拽操作
		function beforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
			className = (className === "dark" ? "":"dark");
			//showLog("[ "+getTime()+" beforeDrop ]&nbsp;&nbsp;&nbsp;&nbsp; moveType:" + moveType);
			//showLog("target: " + (targetNode ? targetNode.name : "root") + "  -- is "+ (isCopy==null? "cancel" : isCopy ? "copy" : "move"));
			return true;
		}
		//被拖拽者信息事件
		function onDrag(event, treeId, treeNodes) {
			className = (className === "dark" ? "":"dark");
			//showLog("[ "+getTime()+" onDrag ]&nbsp;&nbsp;&nbsp;&nbsp; drag: " + treeNodes.length + " nodes." );
		}
		//拖拽父节点存放事件
		function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
			if("folder"!=targetNode.type){showNotification(0,"非文件夹不能作为父节点！");return false;}
			if(!strIsNull(treeNodes[0].type)){
				$.post("/item/zTreeOnDrop?sid=${param.sid}",{Action:"post",nodeId:treeNodes[0].realId,pId:targetNode.realId,itemId:${stagedItem.itemId},nodeType:treeNodes[0].type,moduleId:treeNodes[0].moduleId},     
	 				function (msgObjs){
					showNotification(0,msgObjs.promptMsg);
				},"json");
			}
			className = (className === "dark" ? "":"dark");
			//showLog("[ "+getTime()+" onDrop ]&nbsp;&nbsp;&nbsp;&nbsp; moveType:" + moveType);
			//showLog("target: " + (targetNode ? targetNode.name : "root") + "  -- is "+ (isCopy==null? "cancel" : isCopy ? "copy" : "move"))
		}
		//用于捕获节点被展开的事件回调函数
		function onExpand(event, treeId, treeNode) {
			if (treeNode === autoExpandNode) {
				className = (className === "dark" ? "":"dark");
				//showLog("[ "+getTime()+" onExpand ]&nbsp;&nbsp;&nbsp;&nbsp;" + treeNode.name);
			}
			setTimeout(function(){
				resizeVoteH('otherItemAttrIframe');
			},200);
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
			if(!${stagedItem.stagedEnable}){return;}
			actionTreeNode = treeNode;
			var sObj = $("#" + treeNode.tId + "_span");
			if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0 || "folder"!=treeNode.type) return;
			var addStr = "<span class='fa fa-plus-circle fa-lg' style='position: inherit;width:15px;height:15px' id='addBtn_" + treeNode.tId
				+ "' title='添加' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var btn = $("#addBtn_"+treeNode.tId);
			if (btn) btn.bind("click", function(){
				window.top.layer.open({
					 type: 2,
					 title:false,
					 closeBtn: 0,
					  area: ['400px', '250px'],
					  fix: true, //不固定
					  maxmin: false,
					  content: ["/item/addStagedFolderPage?sid=${param.sid}&itemId=${stagedItem.itemId}&id="+actionTreeNode.realId,'no'],
					  btn: ['添加','关闭'],
					  yes: function(index, layero){
						  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
						  var flag = iframeWin.formSub();
						  if(flag){
							  window.top.layer.close(index);
							  window.top.layer.msg("添加成功",{icon:1})
							  window.self.location.reload();
						  }
					  },cancel: function(){
					  }
					});
				//var zTree = $.fn.zTree.getZTreeObj("zTreeStagedItem");
				//zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
				//return false;
			});
		};
		//用于当鼠标移出节点时，隐藏用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮
		function removeHoverDom(treeId, treeNode) {
			$("#addBtn_"+treeNode.tId).unbind().remove();
		};
		
		function setTrigger() {
			var zTree = $.fn.zTree.getZTreeObj("zTreeStagedItem");
			zTree.setting.edit.drag.autoExpandTrigger = $("#callbackTrigger").attr("checked");
		}

		//初始化树形结构数据
		$(document).ready(function(){
			$.fn.zTree.init($("#zTreeStagedItem"), setting,${itemStagedJsonStr});
			var zTree = $.fn.zTree.getZTreeObj("zTreeStagedItem");
			var n = zTree.getNodes();
			zTree.expandNode(n[0],true,true,false);
			$("#callbackTrigger").bind("change", {}, setTrigger);
			setTimeout(function(){
				resizeVoteH('otherItemAttrIframe');
			},200);
			
			//设置滚动条高度
			var height = $(window).height()-40;
			$("#contentBody").css("height",height+"px");
		});
		//展开树形结构
		function zTreeOpen(){
			var zTree = $.fn.zTree.getZTreeObj("zTreeStagedItem");
			zTree.expandAll(true);
			setTimeout(function(){
				resizeVoteH('otherItemAttrIframe');
			},200);
		}
		//关闭树形结构
		function zTreeClose(){
			var zTree = $.fn.zTree.getZTreeObj("zTreeStagedItem");
			zTree.expandAll(false);
			setTimeout(function(){
				resizeVoteH('otherItemAttrIframe');
			},200);
		}
		//摧毁树形结构
		function zTreeDestroy(){
			var zTree = $.fn.zTree.getZTreeObj("zTreeStagedItem");
			zTree.destroy();
			setTimeout(function(){
				resizeVoteH('otherItemAttrIframe');
			},200);
		}
		//根据返回值构建树形数据
		function valueReturn(objJson){
			if(!strIsNull(objJson)){
				$.ajax({
				   type: "POST",
				   async:false,
				   url: "/item/addStagedInfo?sid=${param.sid}",
				   data: "itemId=${stagedItem.itemId}&stagedItemId="+actionTreeNode.realId+"&moduleId="+objJson.id+"&moduleType=task",
				   success: function(msg){showNotification(1,msg);}
				});
			}
		}
		function zTreeBeforeRemove(treeId, treeNode) {
			var canRemove = false;
			var nodeList = new Array();
			nodeList = zTreeObj.getNodesByParamFuzzy("type", "folder", null);
			if(nodeList.length==1){
				showNotification(1,"最后一个文件夹不能删除!");
				return canRemove;
			}
			var id = treeNode.realId;
			$.ajax({
				   type: "POST",
				   async:false,
				   dataType:"json",
				   url: "/item/countItemStageChildren?sid=${param.sid}",
				   data: {id:id,
					   itemId:${stagedItem.itemId}
					},
				   success: function(data){
						  if(data.count==0){
							  if(confirm("确定删除\""+treeNode.name+"\"?")){
									$.ajax({
										   type: "POST",
										   async:false,
										   url: "/item/zTreeRemove?sid=${param.sid}",
										   data: "nodeId="+treeNode.realId+"&itemId=${stagedItem.itemId}&delChildren=yes&nodeType="+treeNode.type+"&moduleId="+treeNode.moduleId,
										   success: function(msg){
											   canRemove = true;
											   showNotification(0,msg);
										   }
										});
								}
							}else if(data.count>0){
								art.dialog({"content":"请先将文件夹下数据移动到其他文件夹"}).time(2);
								 canRemove = false;
							} 
				   }
				});
			return canRemove;
		}
		//-->
		$(function(){
			$("#itemStagedEdite").click(function(){
				window.top.layer.open({
					  type: 2,
					  //title: ['项目阶段维护', 'font-size:18px;'],
					  title:false,
					  closeBtn:0,
					  area: ['550px', '400px'],
					  fix: false, //不固定
					  maxmin: false,
					  content: ["/item/itemStagedEditePage?sid=${param.sid}&itemId=${stagedItem.itemId}",'no'],
					  //btn: ['关闭'],
					  success:function(layero,index){
						  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
						  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
							  window.top.layer.close(index);
						  })
					  },end: function(){
						  window.self.location.reload();
					  }
					});
			});
		});
	</SCRIPT>
</HEAD>

<body style="background-color:#FFFFFF;">

<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">项目阶段树形</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
				
					<div>
						<div class="ws-question ws-question-in">
							<div class="ws-other-icon">
							 <a href="javascript:void(0)" onclick="zTreeOpen();" class="fa fa-arrow-circle-down"  title="展开阶段" style="width:25px;background:none;color:#1c98dc;margin: 0px 5px">
							 </a>
							 <a href="javascript:void(0)" onclick="zTreeClose();" class="fa fa-arrow-circle-up" title="收起阶段" style="width:25px;background:none;color:#1c98dc;margin: 0px 5px">
							 </a>
							 <c:if test="${item.owner==userInfo.id && empty editItem}">
							 <a href="javascript:void(0)" id="itemStagedEdite" class="fa fa-plus-circle" title="一级目录添加" style="width:25px;background:none;color:#1c98dc;position: static;margin: 0px 5px">
							 </a>
							 </c:if>
							</div>
							<div class="content_wrap">
								<div class="zTreeDemoBackground left">
									<ul id="zTreeStagedItem" class="ztree"></ul>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>
</div>

</body>
</html>
