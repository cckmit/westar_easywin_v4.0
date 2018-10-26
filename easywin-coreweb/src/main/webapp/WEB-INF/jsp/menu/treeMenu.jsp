<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
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
			beforeDrop:this.beforeDrop,
			onDrop:  this.onDrop		
		},
		check : {
			enable: true
		},
		edit : {
			enable: true,
			showRemoveBtn: false,
			showRenameBtn: false,
			drag : {
				isCopy:false
			}
		}
	};

	function onClick(e, treeId, node) {
		if (node.level != 0) {
			parent.rightFrame.location = '/menu/updateMenuPage?id=' + node.id+'&sid=${param.sid}';
		} else {
			parent.rightFrame.location = '/menu/addMenuPage?parentId='+node.id+'&parentName='+node.name+'&sid=${param.sid}';
		}
	}

	

	//根节点点击后不打开关闭
	function dblClickExpand(treeId, treeNode) {
		return treeNode.level > 0;
	}
	
	function getFont(treeId, node) {
		return node.font ? node.font : {};
	}

	var zNodes = new Array();

	function initZtree() {
		//销毁所有的zTree
		$.fn.zTree.destroy();
		zNodes = new Array();
		//通过ajax获取菜单树
		$.post('/menu/listTreeMenu', {random : Math.random(),sid:"${param.sid}"}, function(tree) {
			//创建一个默认的根节点
			var rootNode = new Object;
			rootNode.id = -1;
			rootNode.name='根菜单';
			rootNode.open = true;
			rootNode.enabled = 1;
			zNodes.push(rootNode);
			if (tree != null) {
				for ( var i = 0; i < tree.length; i++) {
					var node = new Object;
					node.id = tree[i].id;
					if (tree[i].parentId != null) {
						node.pId = tree[i].parentId;
					}
					node.name = tree[i].menuName;
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
					zNodes.push(node);
				}
			}
			zTreeObj = $.fn.zTree.init($('#menuTree'), setting, zNodes);
		},"json");
	}

	//新增
	function add() {
	    var zTree = $.fn.zTree.getZTreeObj("menuTree"), nodes = zTree.getSelectedNodes();
	    if(nodes.length>0){
	    	var node = nodes[0];
	    	if(node.enabled==1){
	    		window.parent.rightFrame.location.href = '/menu/addMenuPage?businessType=${param.businessType}&parentId='+node.id+'&parentName='+node.name+"&sid=${param.sid}";
			}else{
				art.dialog.alert('该菜单已经被禁用，请先启用。');
			}
	    }else{
	    	art.dialog.alert('请先选择一个菜单目录。');
		}
	}

	//启用
	function enabled(){
		var zTree = $.fn.zTree.getZTreeObj('menuTree');
		var nodes=zTree.getCheckedNodes();
		if(nodes.length>0){
			var ids=new Array();
			for(var i=0;i<nodes.length;i++){
				ids.push(nodes[i].id);
			}
			$.post('/menu/updateMenuEnabled',{random:Math.random(),ids:ids.toString(),enabled:1,sid:"${param.sid}"},function(bool){
				if(bool){
					initZtree();
				}
			},'json');
		}else{
			art.dialog.alert('请先勾选一个菜单。');
		}
	}

	//禁用
	function disEnabled(){
		var zTree = $.fn.zTree.getZTreeObj('menuTree');
		var nodes=zTree.getCheckedNodes();
		if(nodes.length>0){
			var ids=new Array();
			for(var i=0;i<nodes.length;i++){
				var halfCheck = nodes[i].getCheckStatus();
				if(!halfCheck.half){
					ids.push(nodes[i].id);
				}
			}
			$.post('/menu/updateMenuEnabled',{random:Math.random(),ids:ids.toString(),enabled:0,sid:"${param.sid}"},function(bool){
				if(bool){
					initZtree();
				}
			},'json');
		}else{
			art.dialog.alert('请先勾选一个菜单。');
		}
	}

	//删除
	function del(){
		var zTree = $.fn.zTree.getZTreeObj('menuTree');
		var nodes=zTree.getCheckedNodes();
		if(nodes.length>0){
			art.dialog.confirm('确定要删除该菜单吗？', function(){
				var ids=new Array();
				for(var i=0;i<nodes.length;i++){
					var halfCheck = nodes[i].getCheckStatus();
					if(!halfCheck.half){
						ids.push(nodes[i].id);
					}
				}

				$.post('/menu/delMenu',{random:Math.random(),ids:ids.toString(),sid:"${param.sid}"},function(bool){
					if(bool){
						initZtree();
					}
				},'json');
				
			}, function(){
				art.dialog.tips('已取消');
			});	
		}else{
			art.dialog.alert('请先勾选一个菜单。');
		}
	}

	

	//添加树节点
	function addNode(parentId,id,menuName,enabled,orderNo) {
		var zTree = $.fn.zTree.getZTreeObj('menuTree');
		var parentNode = zTree.getNodeByParam("id", parentId, null);
		zTree.addNodes(parentNode, [{id:id, pId:parentId, name:menuName,enabled:enabled,orderNo:orderNo}]);
	}

	//更新树节点
	function updateNode(id,menuName) {
		var zTree = $.fn.zTree.getZTreeObj('menuTree');
		var currentNode = zTree.getNodeByParam("id", id, null);
		currentNode.name=menuName;
		zTree.editName(currentNode);
		zTree.cancelEditName();
	}


	function beforeDrop(treeId, treeNodes, targetNode, moveType,isCopy){
		if(targetNode.id==-1&&(moveType=='prev'||moveType=='next')){
			art.dialog.alert('不允许拖拽为根节点！');
			return false;
		}
		return true;
	}
	
	//拖拽菜单
	function onDrop(event, treeId, treeNodes, targetNode, moveType,isCopy){
		var moveNode=treeNodes[0];
		if(moveType!=null){
			var id=moveNode.id;
			var parentId=(moveNode.getParentNode()).id;
			var orderNo=1;
			if(moveNode.getPreNode()!=null){
				orderNo=(moveNode.getPreNode()).orderNo+1;
			}
			$.post('/menu/dragMenu',{random:Math.random(),id:id,parentId:parentId,orderNo:orderNo,sid:"${param.sid}"},function(bool){
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
	<ul id="menuTree" class="ztree"></ul>	
</body>
</html>
