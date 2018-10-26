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
<link href="/static/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" />
<link href="/static/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="/static/assets/css/new_file.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="/static/plugins/layui/layui.js"></script>
<link rel="stylesheet" href="/static/plugins/layui/css/layui.css">
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script src="/static/plugins/layer/layer.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>

<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
 <style>
 .ztree li span {
    line-height: 16px;
    margin-right: 10px;
}
 </style>
<SCRIPT type="text/javascript">
		<!--
		var sid="${param.sid}";
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
				enable:${editItem eq "yes"},//不能编辑
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
				onClick: onClick,
				beforeRemove: zTreeBeforeRemove
			}
		};
		//查询指定项目阶段
		function onClick(event, treeId, treeNode, clickFlag){
			if(treeNode.type=='folder'){
				//重设阶段主键
				stagedId = treeNode.realId;
				//重设页码
				pageNum = 0;
				initTable();
			}
			return
		}

		var zTreeObj;
		
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
			//拖拽权限验证
			$.post("/item/zTreeBeforeDropCheckForItem?sid=${param.sid}",{Action:"post",itemId:${stagedItem.itemId},childId:treeNodes[0].realId,parentId:targetNode.realId},     
 				function (msgObjs){
				if(msgObjs.succ){
					return true;
				}else{
					showNotification(0,msgObjs.promptMsg);
					return false;
				}
			},"json");
		}
		//被拖拽者信息事件
		function onDrag(event, treeId, treeNodes) {
			className = (className === "dark" ? "":"dark");
			//showLog("[ "+getTime()+" onDrag ]&nbsp;&nbsp;&nbsp;&nbsp; drag: " + treeNodes.length + " nodes." );
		}
		//拖拽父节点存放事件
		function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
			if(targetNode && "folder"!=targetNode.type){showNotification(0,"非文件夹不能作为父节点！");return false;}
			
			if(moveType){
				var moveNode=treeNodes[0];//拖拽节点
				var preNode = moveNode.getPreNode();//拖拽后前节点
				
				var nodeId=moveNode.realId;
				if(preNode){//不是拖拽成第一个节点
					var preNodeId = preNode.realId;
				}else{
					var preNodeId = 0;
				}
				 if(moveNode.pId){
					var pId =(moveNode.getParentNode()).realId;
				}else{
					var pId =  -1;
				}
			 
				$.post("/item/zTreeOnDrop?sid=${param.sid}",
						{Action:"post",nodeId:nodeId,pId:pId,itemId:${stagedItem.itemId},nodeType:moveNode.type,
					moduleId:moveNode.moduleId},     
		 				function (msgObjs){
						if(msgObjs.succ){
							showNotification(1,msgObjs.promptMsg);
						}else{
							showNotification(2,msgObjs.promptMsg);
						}
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
			if(${item.owner}==${userInfo.id} && ${delItem eq "yes"})//是项目负责人,并且具有删除权限
				return true;
			return false;
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
			
			actionTreeNode = treeNode;
			var sObj = $("#" + treeNode.tId + "_span");
			if (("file"==treeNode.type ||"itemUpFile"==treeNode.type ||"itemTalkFile"==treeNode.type)
					&& $("#downBtn_"+treeNode.tId).length==0){
				var name=treeNode.name.replace("【阶段附件】","");
				name=name.replace("【留言附件】","");
				name=name.replace("【项目附件】","");
				
				//$("#" + treeNode.tId + "_a").attr("href","/downLoad/down/"+treeNode.uuid+"/"+name+"?sid=${param.sid}")
				var	downStr="<a href=\"/downLoad/down/"+treeNode.uuid+"/"+name+"?sid=${param.sid}\" style='margin-top:3px' class='fa fa-download fa-lg' id='downBtn_" + treeNode.tId + "'";
				downStr+="title='下载'>";
				downStr += "<span onfocus='this.blur();'>";
				downStr+='</span></a>';
				sObj.after(downStr);
				
				var fileExt = treeNode.fileExt;
				if($("#viewBtn_"+treeNode.tId).length==0){
					if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx' || fileExt=='txt' || fileExt=='pdf'){
						var viewStr = "<span class='fa fa-eye fa-lg' id='viewBtn_" + treeNode.tId
						+ "' title='预览' onfocus='this.blur();' onclick=\"viewOfficePage('"+treeNode.moduleId+"','"+treeNode.uuid+"','"+name+"','"+fileExt+"','${param.sid}','005','${stagedItem.itemId}')\"></span>";
						sObj.after(viewStr);
					}else if(fileExt=='jpg'||fileExt=='bmp'||fileExt=='gif'||fileExt=='jpeg'||fileExt=='png'){
						var viewStr = "<span class='fa fa-eye fa-lg' id='viewBtn_" + treeNode.tId
						+ "' title='预览' onfocus='this.blur();' onclick=\"showPic('/downLoad/down/"+treeNode.uuid+"/"+name+"','${param.sid}','"+treeNode.moduleId+"','005','${stagedItem.itemId}')\"></span>";
						sObj.after(viewStr);
					}
				}
				

			};
			//判断树形结构是否可编辑，不可编辑直接返回
			if(!${editItem eq "yes"} ||  ${item.owner}!=${userInfo.id} ){return;}
			if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0 || "folder"!=treeNode.type) return;
			var addStr = "<span class='fa fa-plus-circle fa-lg' style='position: inherit;width:15px;height:15px' id='addBtn_" + treeNode.tId
				+ "' title='添加' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var btn = $("#addBtn_"+treeNode.tId);
			if (btn) btn.bind("click", function(){
				window.top.layer.open({
					  type: 2,
					  //title: ['数据类型选择', 'font-size:18px;'],
					  title:false,
					  closeBtn:0,
					  area: ['350px', '250px'],
					  fix: true, //不固定
					  maxmin: false,
					  move: false,
					  content: ["/item/addSelectPage?sid=${param.sid}",'no'],
					  //btn: ['关闭'],
					  success: function(layero,index){
						  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
						  iframeWin.setWindow(window.document,window);
						  
						  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
							  window.top.layer.close(index);
						  })
					  }
				});
			});
		};
		//用于当鼠标移出节点时，隐藏用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮
		function removeHoverDom(treeId, treeNode) {
			$("#addBtn_"+treeNode.tId).unbind().remove();
			$("#downBtn_"+treeNode.tId).unbind().remove();
			$("#viewBtn_"+treeNode.tId).unbind().remove();
		};
		
		function setTrigger() {
			var zTree = $.fn.zTree.getZTreeObj("zTreeStagedItem");
			zTree.setting.edit.drag.autoExpandTrigger = $("#callbackTrigger").attr("checked");
		}

		//初始化树形结构数据
		$(document).ready(function(){
			zTreeObj = $.fn.zTree.init($("#zTreeStagedItem"), setting,${itemStagedJsonStr});
			var zTree = $.fn.zTree.getZTreeObj("zTreeStagedItem");
			var n = zTree.getNodes();
			zTree.expandNode(n[0],true,true,false);
			$("#callbackTrigger").bind("change", {}, setTrigger);
			setTimeout(function(){
				resizeVoteH('otherItemAttrIframe');
			},200);
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
		//设置创建类型
		function addType(addType){
			if(!strIsNull(addType)){
				if("newTask"==addType){
					window.top.layer.open({
						 type: 2,
						 title:false,
						 closeBtn: 0,
						  area: ['800px', '550px'],
						  fix: true, //不固定
						  maxmin: false,
						  content: ["/task/addBusTaskPage?sid=${param.sid}&busId=${stagedItem.itemId}&busType=005&stagedItemId="+actionTreeNode.realId,'no'],
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
				}else if("newFolder"==addType){
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
				}else if("newFile"==addType){
					window.top.layer.open({
						 type: 2,
						 title:false,
						 closeBtn: 0,
						  area: ['400px', '350px'],
						  fix: true, //不固定
						  maxmin: false,
						  content: ["/item/addStagedFilePage?sid=${param.sid}&itemId=${stagedItem.itemId}&stagedItemId="+actionTreeNode.realId,'no'],
						  btn: ['添加','关闭'],
						  yes: function(index, layero){
							  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
							  var flag = iframeWin.addFile();
							  if(flag){
								  window.top.layer.close(index);
								  window.top.layer.msg("添加成功",{icon:1})
								  window.self.location.reload();
							  }
						  },cancel: function(){
						  }
						});
				}
			} 
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
		var tabIndex;
		//项目阶段明细查看
		function stagedInfoView(busId,busType){
			if(busType=='003'){
				authCheck(busId,busType,-1,function(){
					var url = "/task/viewTask?sid=${param.sid}&id="+busId;
					var height = $(window.parent).height();
					openWinWithPams(url,"800px",(height-90)+"px");
				})
				
			}
		}
		//删除节点后回调函数
		//function zTreeOnRemove(event, treeId, treeNode) {
			//alert(treeNode.tId + ", " + treeNode.name);
		//}
		//删除节点前回调函数
		function zTreeBeforeRemove(treeId, treeNode) {
			var canRemove = false;
			var nodeList = new Array();
			nodeList = zTreeObj.getNodesByParamFuzzy("type", "folder", null);
			if("folder"==treeNode.type && nodeList.length==1){
				showNotification(1,"最后一个文件夹不能删除!");
				return canRemove;
			}
			if(confirm("确定删除\""+treeNode.name+"\"?")){
				if(!strIsNull(treeNode.type)){
					if("folder"==treeNode.type){
						if(confirm("是否删除文件夹\""+treeNode.name+"\"下的数据文件?")){
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
						}else{
							$.ajax({
								   type: "POST",
								   async:false,
								   url: "/item/zTreeRemove?sid=${param.sid}",
								   data: "nodeId="+treeNode.realId+"&itemId=${stagedItem.itemId}&delChildren=no&nodeType="+treeNode.type+"&moduleId="+treeNode.moduleId,
								   success: function(msg){
									   canRemove = true;
									   showNotification(0,msg);
								   }
								});
						}
					}else{
						$.ajax({
							   type: "POST",
							   async:false,
							   url: "/item/zTreeRemove?sid=${param.sid}",
							   data: "nodeId="+treeNode.realId+"&itemId=${stagedItem.itemId}&nodeType="+treeNode.type+"&moduleId="+treeNode.moduleId,
							   success: function(msg){
								   canRemove = true;
								   showNotification(0,msg);
							   }
							});
					}
				}
			}
			return canRemove;
		}
		//-->
		//添加一级目录
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
			
			$("#allTodoBody").on("mouseover","tr",function(){
				$(this).find(".stageUserName").hide();
				$(this).find(".stagePro").hide();
				$(this).find(".optMod").show();
			})
			$("#allTodoBody").on("mouseout","tr",function(){
				$(this).find(".stageUserName").show();
				$(this).find(".stagePro").show();
				$(this).find(".optMod").hide();
				
			})
		});
		
		 var pageNum = 0;     //页面索引初始值   
         var pageSize = 10;     //每页显示条数初始化，修改显示条数，修改这里即可 
         var stagedId = -1;
         var relateMod = new Array();
         var modName = "";
         var taskState="-1";//任务状态
         $(function(){
     			initTable();//列表初始化
        		//点击<li>的时候不考虑checkbox
        		$("#modTypeId li").click(function(e){
        			pageNum = 0;
        			var liId = $(this).find("a").attr("id");
					var text = $(this).find("a").html();
					var i = $('<i class="fa fa-angle-down"></i>');
					var selectedMod = new Array();
					if('allType'==liId){
						$("#modTypeA").html("类型筛选");
						taskState = -1;//默认值
						$("#taskStateA").html("任务状态筛选");
					}else{
						var font = $('<font style="font-weight:bold;"></font>');
						font.append($.trim(text))
						$("#modTypeA").html(font);
						 if('taskType'==liId){//任务
							 selectedMod.push("task")
						 }else if("itemFileType"==liId){//项目附件
							  selectedMod.push("file")
							  selectedMod.push("itemUpFile")
							  selectedMod.push("itemTalkFile")
							  $("#taskStateA").html("任务状态筛选");
							  taskState = -1;//默认值
						 }else if("spFlowType"==liId){//审批附件
							  selectedMod.push("sp_flow");
							  $("#taskStateA").html("任务状态筛选");
							  taskState = -1;//默认值
						 }
					}
					relateMod = selectedMod;
					$("#modTypeA").append(i);
        			var pObj = $("#modTypeA").parent();
    				$(pObj).removeClass("open");
					initTable();
        		});
        		//任务状态筛选
        		$("#taskState li").click(function(e){
        			pageNum = 0;
        			var state = $(this).find("a").attr("state");
					var text = $(this).find("a").html();
					var i = $('<i class="fa fa-angle-down"></i>');
					var selectedMod = new Array();
					if(-1!=state){
						var font = $('<font style="font-weight:bold;"></font>');
						font.append($.trim(text))
						$("#taskStateA").html(font);
						$("#modTypeA").html($('<font style="font-weight:bold;">任务</font>'));
						selectedMod.push("task")//筛选条件
						taskState = state;//筛选条件
					}else{
						taskState = -1;//默认值
						$("#taskStateA").html("任务状态筛选");
						$("#modTypeA").html("类型筛选");
					}
					relateMod = selectedMod;
					$("#taskStateA").append(i);
        			var pObj = $("#taskStateA").parent();
    				$(pObj).removeClass("open");
					initTable();
        		})
        		
        		//查看
        		$("body").on("click","#allTodoBody .modNameA",function(){
        			var data = $(this).parents("tr").data("data");
        			if(data.upfileId>0){
        				var fileExt = data.fileExt;
        				if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx' || fileExt=='txt' || fileExt=='pdf'){
        					viewOfficePage(data.upfileId,data.fileUuid,data.fileName,fileExt,'${param.sid}','005','${stagedItem.itemId}')
    					}else if(fileExt=='jpg'||fileExt=='bmp'||fileExt=='gif'||fileExt=='jpeg'||fileExt=='png'){
    						var url = '/downLoad/down/'+data.fileUuid+'/'+data.fileName+'';
    						showPic(url,'${param.sid}',data.upfileId,'005','${stagedItem.itemId}')
    					}
        			}else{
        				if(data.moduleType=='task'){
        					stagedInfoView(data.moduleId,'003')
        				}
        			}
        			
        		});
        		//移动
        		$("body").on("click","#allTodoBody .moveA",function(){
					var data = $(this).parents("tr").data("data");
        			stagedItemSelectedPage("${param.sid}",'${stagedItem.itemId}',function(result){
        				var stagedInfo = data;
        				stagedInfo.newStagedName = result.name;
        				stagedInfo.newStagedId = result.id;
        				$.ajax({   
        	                 type: "POST",  
        	                 dataType: "json",  
        	                 url: '/item/ajaxUpdateStagedItem?sid=${param.sid}',
        	                  data:{"moduleType":stagedInfo.moduleType,
							 "infoId":stagedInfo.infoId,
							 "itemId":stagedInfo.itemId,
							  "newStagedName": result.name,
							 "newStagedId": result.id
							 },
        	                 success:function(data){
								 if(data.status=='f'){
									 showNotification(2,msg);
								 }else{
									 showNotification(1,"操作成功");
									  initTable();
								 }
       	                	 }
       	                 });
        			});
        		});
				
				//删除
        		$("body").on("click","#allTodoBody .deleteA",function(){
        			var stagedInfo = $(this).parents("tr").data("data");
        			window.top.layer.confirm("确定删除数据吗?", {icon: 3, title:'确认对话框'}, function(index){
        				window.top.layer.close(index);
	        			var data = JSON.stringify(stagedInfo);
						$.ajax({   
							 type: "POST",  
							 dataType: "json",  
							 url: '/item/ajaxDeleteStagedItem?sid=${param.sid}',
							 data:{"moduleType":stagedInfo.moduleType,
							 "infoId":stagedInfo.infoId,
							 "itemId":stagedInfo.itemId
							 },
							 success:function(data){
								 if(data.status=='f'){
									 showNotification(2,msg);
								 }else{
									 showNotification(1,"操作成功");
									  initTable();
								 }
							 }
						 });
        			});
        		});
				
				$("#moduleName").blur(function(){
					modName = $("#moduleName").val();
					pageNum = 0;
					initTable();
				})
				//文本框绑定回车提交事件
				$("#moduleName").bind("keydown",function(event){
			        if(event.keyCode == "13"){
			        	modName = $("#moduleName").val();
			        	pageNum = 0;
						initTable();
			        }
			    });
				
         });
         
         function closeLoad(){
        		if(loadDone){
        			layui.use('layer', function() {
        				layer.closeAll('loading');
        			});
        			clearInterval(intervalInt);
        		}
        		
        	}
         var loadingIndex;
         var loadDone=0;
         var intervalInt;
         //数据初始化
         function initTable(){
        	 
        	 layui.use('layer', function() {
     			loadingIndex = layer.load(0, {
     				shade: [0.5,'#fff'] //0.1透明度的白色背景
     			});
     			
     			intervalInt = setInterval("closeLoad("+loadingIndex+")",500);
     			
     		})
     		
        	 $.ajax({   
                 type: "POST",  
                 dataType: "json",  
                 url: '/item/ajaxListStaged?sid=${param.sid}',
                 traditional :true,
                 data: {"itemId":${stagedItem.itemId},
                	 "stagedId":stagedId,
                	 "pageNum":pageNum,
                	 "pageSize":pageSize,
                	 "relateMod":relateMod,
                	 "moduleName":modName,
                	 "taskState":taskState
                },                 
                 success: function(data) {
                	 loadDone=1;
                     if(data.status=='f'){
                    	 
                     }else{
                    	 $("#stagedTotel").html(data.total);
                    	 $("#stagedTotel").parent().parent().css("display","block")
                    	 if(data.total<=pageSize){
                    		 $("#stagedTotel").parent().parent().css("display","none")
                    	 }
                   		//分页，PageCount是总条目数，这是必选参数，其它参数都是可选
                          $("#pageDiv").pagination(data.total, {
                              callback: PageCallback,  //PageCallback() 为翻页调用次函数。
                              prev_text: "<<",
                              next_text: ">>",
                              items_per_page:pageSize,
                              num_edge_entries: 0,       //两侧首尾分页条目数
                              num_display_entries: 3,    //连续分页主体部分分页条目数
                              current_page: pageNum,   //当前页索引
                          });
                    	 $("#allTodoBody").html('');
                    	 constrStagedInfoTable(data.stagedInfos);
                     }
                 }  
             }); 
         }
         
       //翻页调用   
         function PageCallback(index, jq) {  
    	   	 pageNum = index;
        	 initTable();
         } 
       	 //列表构建
         function constrStagedInfoTable(stagedInfos){
        	   if(stagedInfos && stagedInfos.length>0){
        		   $.each(stagedInfos,function(index,data){
        			   var tr=$("<tr></tr>");
        			   var xhTd = $('<td class="stageOrderNum"><label  style="display: block;width: 20px">'+(index+1)+'</label></td>')
        			   var modNameA = $('<a href="javascript:void(0)" title="'+data.moduleName+'" class="modNameA">'+cutstr(data.moduleName,30)+'</a>')
        			   var nameTd = $('<td class="stageModName"></td>');
        			   nameTd.append(modNameA)
        			   var html ='\n 			<div class="ticket-user pull-left other-user-box">';
        				html +='\n <img class="user-avatar" src="/downLoad/userImg/${userInfo.comId}/'+data.creator+'?sid=${param.sid}" title="'+data.userName+'" />';
        					
        				html +='\n				<span class="user-name">'+data.userName+'</span>';
        				html +='\n			</div>';
        			 
        			   var userTd = $('<td class="stageUserName"></td>');
        			   userTd.append(html)
        			   var type= data.moduleType;
        			   var typeName = type=='task'?'任务':type=='file'?"附件":type=='itemUpFile'?"附件":type=='itemTalkFile'?"附件":type=='sp_flow'?"审批":"未知";
        			   var typeTd = $('<td class="stageDate">'+typeName+'</td>');
        			   //进度描述
        			   var taskprogressDescribe="/";
        			   var progressTd = $('<td class="stagePro"></td>');
        			   if(type && type=='task'){
        				   var taskInfo = getTaskDetail (data.moduleId);
        				   if(taskInfo){
                			   taskprogressDescribe = taskInfo.state==3?'暂停':(taskInfo.state==4?'完成':(taskInfo.taskProgress?taskInfo.taskProgress+"%":"0%"));
                			   $(progressTd).css("color",(taskInfo.state==3?"yello":(taskInfo.state==4?"red":"green")));
        				   }
        			   }
        			   progressTd.append(taskprogressDescribe)

        			   var optTd = $('<td class="optMod" style="display:none" colspan="2"></td>');
        			   var modView = $('<a href="javascript:void(0)" class="modNameA">查看</a>')
        			   if(data.upfileId>0){
        					
        					var fileExt = data.fileExt;
        					if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx' || fileExt=='txt' || fileExt=='pdf'){
        					 optTd.append(modView);
        					 optTd.append("|");
        					}else if(fileExt=='jpg'||fileExt=='bmp'||fileExt=='gif'||fileExt=='jpeg'||fileExt=='png'){
        					 optTd.append(modView);
        					 optTd.append("|");
        					}
        					if(${delItem eq "yes"} && ${item.owner}==${userInfo.id}){//具有删除权限
        					   var moveA = $('<a href="javascript:void(0)" class="deleteA">删除</a>')
        					   optTd.append(moveA);
        					   optTd.append("|");
        					}
        				   var downloadurl ="/downLoad/down/"+data.fileUuid+"/"+data.fileName+"?sid=${param.sid}";
        				   var downLoadA = $('<a href="'+downloadurl+'" class="downLoadA">下载</a>')
        				   optTd.append(downLoadA);
        				   
        				   if(${editItem eq "yes"} && ${item.owner}==${userInfo.id}){
        					   optTd.append("|");
        					   var moveA = $('<a href="javascript:void(0)" class="moveA">移动</a>')
        					   optTd.append(moveA);
        				   }
        			   }else{
        					optTd.append(modView);
        				   if(${editItem eq "yes"} && ${item.owner}==${userInfo.id}){
        					   optTd.append("|");
        					   var moveA = $('<a href="javascript:void(0)" class="moveA">移动</a>')
        					   optTd.append(moveA);
        				   }
        			   }
        			   
        			   tr.append(xhTd);
        			   tr.append(nameTd);
        			   tr.append(typeTd);
        			   tr.append(progressTd);
        			   tr.append(userTd);
        			   tr.append(optTd);
        			   
        			   
        			   tr.data("data",data);
        			   
        			   
        			   $("#allTodoBody").append(tr);
        		   })
        	   }
        	}
       
       
	</SCRIPT>
	<style type="text/css">
		.stageOrderNum{height: 40px}
		#allTodoBody td{
			font-size: 12px;
			padding: 5px 0px !important;
			text-align: center;
		}
		#editabledatatable th{
			font-size: 15px;
			padding: 5px 0px !important;
			text-align: center;
		}
		#allTodoBody .stageModName{
			text-align: left;
		}
		.pagination{
			margin: 10px 0;
		}
		#modTypeId {
			min-width: 100px;
		}
		#modTypeId li{
			font-size: 12px;
		}
		#modTypeId li>a{
			padding: 5px 10px;
		}
		.user-avatar{
			width: 25px !important;
			height: 25px !important;
		}
		.optMod a{
			padding:0px 3px;
		}
		.user-name{
			margin: 8px 0 0 5px !important;
		}
	</style>
</HEAD>

<body style="background-color:#FFFFFF;" onload="resizeVoteH('otherItemAttrIframe')">
<div class="ws-content ws-content-in" style="width: 100%">
	<div class="ws-question ws-question-in">
		<div class="ws-other-icon" style="background-color:#f0f0f0">
			<div class="clearfix">
				<div class="pull-left" style="padding-top:5px;">
					 <a href="javascript:void(0)" onclick="zTreeOpen();" class="fa fa-arrow-circle-down"  title="展开阶段" style="width:25px;background:none;color:#1c98dc;margin: 5px 5px">
					 </a>
					 <a href="javascript:void(0)" onclick="zTreeClose();" class="fa fa-arrow-circle-up" title="收起阶段" style="width:25px;background:none;color:#1c98dc;margin: 5px 5px">
					 </a>
					 <c:if test="${item.owner==userInfo.id}">
					 <a href="javascript:void(0)" id="itemStagedEdite" class="fa fa-plus-circle" title="一级目录添加" style="width:25px;background:none;color:#1c98dc;position: static;margin: 5px 5px">
					 </a>
					 </c:if>
					 <a href="javascript:void(0)" onclick="window.self.location.reload();" class="fa fa-refresh" title="刷新" style="width:25px;background:none;color:#1c98dc;margin: 5px 5px">
					 </a>
				</div>
				<div class="pull-right">
					<div class="table-toolbar" id="searchForm">
						<div class="table-toolbar  pull-left" style="padding-top:5px;padding-right:20px;">
							<div class="btn-group" id="modTypeDiv">
								<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMenu('modTypeA')" id="modTypeA">
									类型筛选
									<i class="fa fa-angle-down"></i>
								</a>
								 <ul class="dropdown-menu dropdown-default" id="modTypeId">
									 <li><a href="javascript:void(0);" id="allType">不限条件</a></li>
									 <li><a href="javascript:void(0);" id="taskType">任务</a></li>
									 <li><a href="javascript:void(0);" id="itemFileType">附件</a> </li>
									 <li><a href="javascript:void(0);" id="spFlowType">审批</a> </li>
	                       		</ul>
	                   		</div>
	                   		<div class="btn-group">
								<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMenu('taskStateA')" id="taskStateA">
									任务状态筛选
									<i class="fa fa-angle-down"></i>
								</a>
								 <ul class="dropdown-menu dropdown-default" id="taskState">
									 <li><a href="javascript:void(0);" state="-1">不限条件</a></li>
									 <li><a href="javascript:void(0);" state="1">进行中</a></li>
									 <li><a href="javascript:void(0);" state="3">挂起中</a></li>
									 <li><a href="javascript:void(0);" state="4">已完成</a></li>
	                       		</ul>
	                   		</div>
	                   	</div>
                   		
                   		<div class="pull-left searchCond" style="padding-top:2px;">
							<span class="input-icon"> <input id="moduleName"
								class="form-control ps-input" type="text" placeholder="请输入关键字">
								<a href="JavaScript:void(0)" class="ps-searchBtn"><i
									class="glyphicon glyphicon-search circular danger"></i> </a>
							</span>
						</div>
             		</div>
				</div>
			</div>
			</div>
		</div>
		<div class="content_wrap" style="height: 500px;clear: both">
			<div class="row" style="margin-left: 0px;margin-right: 0px">
				<div class="col-xs-4">
					<div class="zTreeDemoBackground" style="overflow:hidden ;overflow-y:scroll;height: 500px">
						<ul id="zTreeStagedItem" class="ztree"></ul>
					</div>
				</div>
				<div class="col-xs-8">
					<div class=" left" >
						<table class="table table-striped table-hover fixTable" id="editabledatatable">
							<thead>
								<tr role="row">
									<th style="width:50px">
										序号
									</th>
									<th style="text-align: left">
										名称
									</th>
									<th style="width: 80px">
										类别
									</th>
									<th style="width: 80px">
										进度
									</th>
									<th style="width: 80px">
										创建人
									</th>
								</tr>
							</thead>
							<tbody id="allTodoBody">
							</tbody>
						</table>
						<div class="panel-body ps-page bg-white" style="padding-top: 0px;font-size: 12px">
							 <p class="pull-left ps-pageText">共<b class="badge" id="stagedTotel">0</b>条记录</p>
							 <ul class="pagination pull-right" id="pageDiv">
							 </ul>
						</div>
				</div>
				</div>
			</div>
	</div>
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
