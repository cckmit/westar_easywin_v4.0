<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">

<title><%=SystemStrConstant.TITLE_NAME %></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
</head>
<script type="text/javascript">
	var zTreeObj;
	var rootId = "${param.rootId}";
	var setting = {
		view : {
			selectedMulti : false,
			fontCss : getFont,
			dblClickExpand : dblClickExpand
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onClick : this.onClick,
			onDrop:  this.onDrop,
			beforeDrag:this.beforeDrag
		},
		check : {
			enable: true
		},
		edit : {
			enable: true,
			showRemoveBtn: false,
			showRenameBtn: false,
			drag : {
				isCopy:false,
				prev: this.dropPrev,
				inner: this.dropInner,
				next: this.dropNext
			}
		}
	};

	function onClick(e, treeId, node) {
		//如果是部门管理员进入，则根节点是本部门，点击后进入修改页面，如果是系统管理员进入，点击根节点则跳转到部门新增页
		if (node.level == 0 && rootId=="") {
			parent.rightFrame.location = '/organization/addOrganizationPage?parentId='+node.id+'&parentName='+node.name+'&sid=${param.sid}';
		} else {
			parent.rightFrame.location = '/organization/updateOrganizationPage?id=' + node.id+'&sid=${param.sid}';
		}
	}

	

	//根节点点击后不打开关闭
	function dblClickExpand(treeId, treeNode) {
		return treeNode.level > 0;
	}
	
	function getFont(treeId, node) {
		return node.font ? node.font : {};
	}

	var curDragNodes;
	function beforeDrag(treeId, treeNodes) {
		for (var i=0,l=treeNodes.length; i<l; i++) {
			if (treeNodes[i].drag == false) {
				curDragNodes = null;
				return false;
			} else if (treeNodes[i].parentTId && treeNodes[i].getParentNode().childDrag == false) {
				curDragNodes = null;
				return false;
			}
		}
		curDragNodes = treeNodes;
		return true;
	}
	

	function dropPrev(treeId, nodes, targetNode) {
		var pNode = targetNode.getParentNode();
		if (pNode && pNode.dropInner == false) {
			return false;
		} else {
			for (var i=0,l=curDragNodes.length; i<l; i++) {
				var curPNode = curDragNodes[i].getParentNode();
				if (curPNode && curPNode != targetNode.getParentNode() && curPNode.childOuter == false) {
					return false;
				}
			}
		}
		return true;
	}
	
	function dropInner(treeId, nodes, targetNode) {
		if (targetNode && targetNode.dropInner == false) {
			return false;
		} else {
			for (var i=0,l=curDragNodes.length; i<l; i++) {
				if (!targetNode && curDragNodes[i].dropRoot == false) {
					return false;
				} else if (curDragNodes[i].parentTId && curDragNodes[i].getParentNode() != targetNode && curDragNodes[i].getParentNode().childOuter == false) {
					return false;
				}
			}
		}
		return true;
	}
	
	function dropNext(treeId, nodes, targetNode) {
		var pNode = targetNode.getParentNode();
		if (pNode && pNode.dropInner == false) {
			return false;
		} else {
			for (var i=0,l=curDragNodes.length; i<l; i++) {
				var curPNode = curDragNodes[i].getParentNode();
				if (curPNode && curPNode != targetNode.getParentNode() && curPNode.childOuter == false) {
					return false;
				}
			}
		}
		return true;
	}

	

	var zNodes = new Array();

	function initZtree() {
		//销毁所有的zTree
		$.fn.zTree.destroy();
		zNodes = new Array();
		//通过ajax获取组织机构树
		$.post('/organization/listTreeOrganization', {random : Math.random(),rootId:rootId,sid:'${param.sid}'}, function(tree) {
			//如果不是部门管理员维护 则 创建一个默认的根节点
			if(rootId==""){
				var rootNode = new Object;
				rootNode.id = -1;
				rootNode.name='<%=SystemStrConstant.ROOT_ORG_NAME%>';
				rootNode.open = true;
				rootNode.enabled = 1;
				rootNode.childOuter=false;
				rootNode.icon="/static/images/base.gif";
				zNodes.push(rootNode);
			}
			if (tree != null) {
				for ( var i = 0; i < tree.length; i++) {
					var node = new Object;
					node.id = tree[i].id;
					if (tree[i].parentId != null) {
						node.pId = tree[i].parentId;
					}
					node.name = tree[i].orgName;
					if(tree[i].parentId==-1){
						node.open = true;
					}
					node.enabled = tree[i].enabled;
					if (tree[i].enabled == 0) {
						node.font = {
							'color' : 'red'
						};
					}
					node.orderNo= tree[i].orderNo;
					node.childOuter=false;
					if(tree[i].orgLevel==1){
						node.icon="/static/images/dep.gif";
					}else{
						node.icon="/static/images/office.gif";
					}
					zNodes.push(node);
				}
			}
			zTreeObj = $.fn.zTree.init($('#organizationTree'), setting, zNodes);
		},"json");
	}

	//新增
	function add() {
	    var zTree = $.fn.zTree.getZTreeObj("organizationTree"), nodes = zTree.getSelectedNodes();
	    if(nodes.length>0){
	    	var node = nodes[0];
	    	if(node.enabled==1){
	    		window.parent.rightFrame.location.href = '/organization/addOrganizationPage?parentId='+node.id+'&parentName='+node.name+'&sid=${param.sid}';
			}else{
				art.dialog.alert('该组织机构已经被禁用，请先启用。');
			}
	    }else{
	    	art.dialog.alert('请先选择一个组织机构。');
		}
	}

	//启用
	function enabled(){
		var zTree = $.fn.zTree.getZTreeObj('organizationTree');
		var nodes=zTree.getCheckedNodes();
		if(nodes.length>0){
			var ids=new Array();
			for(var i=0;i<nodes.length;i++){
				ids.push(nodes[i].id);
			}
			$.post('/organization/updateOrganizationEnabled',{random:Math.random(),ids:ids.toString(),enabled:1,sid:'${param.sid}'},function(bool){
				if(bool){
					initZtree();
				}
			},'json');
		}else{
			art.dialog.alert('请先勾选一个组织机构。');
		}
	}

	//禁用
	function disEnabled(){
		var zTree = $.fn.zTree.getZTreeObj('organizationTree');
		var nodes=zTree.getCheckedNodes();
		if(nodes.length>0){
			var ids=new Array();
			for(var i=0;i<nodes.length;i++){
				var halfCheck = nodes[i].getCheckStatus();
				if(!halfCheck.half){
					ids.push(nodes[i].id);
				}
			}
			$.post('/organization/updateOrganizationEnabled',{random:Math.random(),ids:ids.toString(),enabled:0,sid:'${param.sid}'},function(bool){
				if(bool){
					initZtree();
				}
			},'json');
		}else{
			art.dialog.alert('请先勾选一个组织机构。');
		}
	}

	//删除
	function del(){
		var zTree = $.fn.zTree.getZTreeObj('organizationTree');
		var nodes=zTree.getCheckedNodes();
		if(nodes.length>0){
			art.dialog.confirm('确定要删除该组织机构吗？', function(){
				var ids=new Array();
				for(var i=0;i<nodes.length;i++){
					var halfCheck = nodes[i].getCheckStatus();
					if(!halfCheck.half){
						ids.push(nodes[i].id);
					}
				}

				$.post('/organization/delOrganization',{random:Math.random(),ids:ids.toString(),sid:'${param.sid}'},function(bool){
					if(bool){
						initZtree();
					}
				},'json');
				
			}, function(){
				art.dialog.tips('已取消');
			});	
		}else{
			art.dialog.alert('请先勾选一个组织机构。');
		}
	}

	

	//添加树节点
	function addNode(parentId,id,orgName,enabled,orderNo) {
		var zTree = $.fn.zTree.getZTreeObj('organizationTree');
		var parentNode = zTree.getNodeByParam("id", parentId, null);
		zTree.addNodes(parentNode, [{id:id, pId:parentId, name:orgName,enabled:enabled,orderNo:orderNo}]);
	}

	//更新树节点
	function updateNode(id,orgName) {
		var zTree = $.fn.zTree.getZTreeObj('organizationTree');
		var currentNode = zTree.getNodeByParam("id", id, null);
		currentNode.name=orgName;
		zTree.editName(currentNode);
		zTree.cancelEditName();
	}
	
	//拖拽组织机构
	function onDrop(event, treeId, treeNodes, targetNode, moveType,isCopy){
		var moveNode=treeNodes[0];
		if(moveType!=null){
			var id=moveNode.id;
			var parentId=(moveNode.getParentNode()).id;
			var orderNo=1;
			if(moveNode.getPreNode()!=null){
				orderNo=(moveNode.getPreNode()).orderNo+1;
			}
			$.post('/organization/dragOrganization',{random:Math.random(),id:id,parentId:parentId,orderNo:orderNo,sid:'${param.sid}'},function(bool){
				if(!bool){
					art.dialog.alert('拖拽失败！');
					initZtree();
				}
			},'json');
		}
	}

	$(function() {
		initZtree();
	});
</script>
<style type="text/css">
.ztree li span.button.switch.level0 {
	visibility: hidden;
	width: 1px;
}
.ztree li ul.level0 {
	padding: 0;
	background: none;
}
.ztree li span.demoIcon{padding:0 2px 0 10px;}

.ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
.ztree li ul.level0 {padding:0; background:none;}
</style>
<body style="background-color: #FFFFFF">
	<ul id="organizationTree" class="ztree"></ul>	
</body>
</html>
