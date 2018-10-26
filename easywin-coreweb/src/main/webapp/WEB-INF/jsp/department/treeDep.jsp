<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.web.controller.BaseController"%>
<%@page import="com.westar.base.cons.SessionKeyConstant"%>
<%@page import="com.westar.core.web.controller.UserInfoController"%>
<%@page import="com.westar.base.util.RequestContextHolderUtil"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">

<title><%=SystemStrConstant.TITLE_NAME %></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" >
<link href="/static/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<style>
body { 
     overflow-x : auto;  
     overflow-y : hidden; 
} 

</style>
<script type="text/javascript">
	var zTreeObj;
	var flag = ${sessionUser.admin}>0;
	var setting = {
		view : {
			selectedMulti : false,
			fontCss : getFont,
			addHoverDom: addHoverDom,
			removeHoverDom: removeHoverDom,
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
			onDrop:  this.onDrop,
			onExpand: zTreeOnExpand,
			onCollapse: zTreeOnCollapse
		},
		check : {
			enable: flag,
			chkboxType:{"Y":"s","N":"p"}
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
	//展开
	function zTreeOnExpand(){
			resizeVoteH('leftFrame');
	}
	//收回
	function zTreeOnCollapse(){
			resizeVoteH('leftFrame');
	}

	function onClick(e, treeId, node) {
		window.parent.rightFrame.location.href = '/department/listPagedUserTreeForDep?depId='+node.id+'&depName='+node.name+'&sid=${param.sid}';
		/*
		if(flag){
			if (node.level != 0) {
				parent.rightFrame.location = '/department/updateDepPage?pager.pageSize=10&id=' + node.id+'&sid=${param.sid}';
			} else {
				parent.rightFrame.location = '/department/addDepPage?parentId='+node.id+'&parentName='+node.name+'&sid=${param.sid}';
			}
		}else{
		}
		*/
	}
	var actionTreeNode=null;
	//用于当鼠标移动到节点上时，显示用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮
	function addHoverDom(treeId, treeNode) {
		actionTreeNode = treeNode;
		
		var sObj = $("#" + treeNode.tId + "_span");
		
		if($("#editBtn_"+treeNode.tId).length==0 && flag && treeNode.id>0){
			var editStr = "<span class='fa fa-edit fa-lg ' style='position: inherit;width:15px;height:15px;' id='editBtn_" + treeNode.tId
			+ "' title='修改' onfocus='this.blur();'></span>";
			sObj.after(editStr);
			
			var editBtn = $("#editBtn_"+treeNode.tId);
			if(editBtn){
				editBtn.bind("click", function(){
					editDep(treeNode);
					return false;
				});
			}
			
		}
		
		if($("#addBtn_"+treeNode.tId).length==0 && flag){
			var addStr = "<span class='fa fa-plus-circle fa-lg' style='position: inherit;width:15px;height:15px;padding:0px 5px;' id='addBtn_" + treeNode.tId
			+ "' title='添加' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var addBtn = $("#addBtn_"+treeNode.tId);
			if(addBtn){
				addBtn.bind("click", function(){
					addDep(treeNode);
					return false;
				});
			}
		}
	}
	//用于当鼠标移出节点时，隐藏用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮
	function removeHoverDom(treeId, treeNode) {
		$("#editBtn_"+treeNode.tId).unbind().remove();
		$("#addBtn_"+treeNode.tId).unbind().remove();
	};

	

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
		//通过ajax获取部门树
		$.post('/department/listTreeDep', {random : Math.random(),sid:"${param.sid}"}, function(tree) {
			//创建一个默认的根节点
			var rootNode = new Object;
			rootNode.id = -1;
			rootNode.name='${orgName}';
			rootNode.open = true;
			rootNode.enabled = 1;
			rootNode.defDep = 0;
			zNodes.push(rootNode);
			if (tree != null) {
				for ( var i = 0; i < tree.length; i++) {
					var node = new Object;
					node.id = tree[i].id;
					if (tree[i].parentId != null) {
						node.pId = tree[i].parentId;
					}
					node.name = tree[i].depName;
					node.defDep = tree[i].defDep;
					if(tree[i].parentId==-1){
						node.open = true;
					}
					node.enabled = tree[i].enabled;
					if (tree[i].enabled == 0) {
						node.font = {
							'color' : 'red'
						};
					}
					node.icon="/static/images/dep.gif";
					zNodes.push(node);
				}
			}
			zTreeObj = $.fn.zTree.init($('#depTree'), setting, zNodes);
			resizeVoteH('leftFrame')
		},"json");
	}
	//
	function addDep(treeNode){
		var depId = treeNode.id;
		var depName = treeNode.name;
		parent.rightFrame.location = '/department/addDepPage?parentId='+depId+'&parentName='+depName+'&sid=${param.sid}';
	}
	function editDep(treeNode){
		var depId = treeNode.id;
		var depName = treeNode.name;
		var defDep = treeNode.defDep;
		parent.rightFrame.location = '/department/updateDepPage?id='+depId+'&depName='+depName+'&defDep='+defDep+'&sid=${param.sid}';
	}

	//新增
	function add() {
	    var zTree = $.fn.zTree.getZTreeObj("depTree"), nodes = zTree.getSelectedNodes();
	    window.top.layui.use('layer', function(){
			var layer = window.top.layui.layer;
			
		    if(nodes.length>0){
		    	var node = nodes[0];
		    	if(node.enabled==1){
		    		window.parent.rightFrame.location.href = '/department/addDepPage?parentId='+node.id+'&parentName='+node.name+"&sid=${param.sid}";
				}else{
					window.top.layer.alert('该部门已经被禁用，请先启用。');
				}
		    }else{
		    	window.top.layer.alert('请先选择新增部门的父级部门！');
			}
		});
	    
	}

	//启用
	function enabled(){
		var zTree = $.fn.zTree.getZTreeObj('depTree');
		var nodes=zTree.getCheckedNodes();
		
		window.top.layui.use('layer', function(){
			var layer = window.top.layui.layer;
			
			if(nodes.length>0){
				var ids=new Array();
				for(var i=0;i<nodes.length;i++){
					ids.push(nodes[i].id);
				}
				$.post('/department/updateDepEnabled',{random:Math.random(),ids:ids.toString(),enabled:1,sid:"${param.sid}"},function(data){
					if(data.statue=='y'){
						initZtree();
						showNotification(1,"操作成功");
					}else if(data.statue=='n'){
						showNotification(2,data.info);
					}else{
						window.parent.location.reload();
					}
				},'json');
			}else{
				window.top.layer.alert('请勾选需要启用的部门！');
			}
		});
	}

	//禁用
	function disEnabled(){
		var zTree = $.fn.zTree.getZTreeObj('depTree');
		var nodes=zTree.getCheckedNodes();
		window.top.layui.use('layer', function(){
			var layer = window.top.layui.layer;
			
			if(nodes.length>0){
				//默认可删除信息
				var flag = true;
				var ids=new Array();
				for(var i=0;i<nodes.length;i++){
					var halfCheck = nodes[i].getCheckStatus();
					if(!halfCheck.half){
						if(nodes[i].defDep==1){
							window.top.layer.alert('默认部门不能禁用！');
							flag = false;
							break;
						}else if(nodes[i].id==-1){
							window.top.layer.alert('根节点不能禁用！');
							flag = false;
							break;
						}else{
							ids.push(nodes[i].id);
						}
					}
				}
				if(flag){
					$.post('/department/updateDepEnabled',{random:Math.random(),ids:ids.toString(),enabled:0,sid:"${param.sid}"},function(data){
						if(data.statue=='y'){
							initZtree();
							showNotification(1,"操作成功");
						}else if(data.statue=='n'){
							showNotification(2,data.info);
						}else{
							window.parent.location.reload();
						}
					},'json');
				}
			}else{
				window.top.layer.alert('请勾选需要禁用的部门！');
			}
		});
		
	}

	//删除
	function del(){
		var zTree = $.fn.zTree.getZTreeObj('depTree');
		var nodes=zTree.getCheckedNodes();
		
		window.top.layui.use('layer', function(){
			var layer = window.top.layui.layer;
			if(nodes.length>0){
				//默认可删除信息
				var flag = true;
				var ids=new Array();
				for(var i=0;i<nodes.length;i++){
					var halfCheck = nodes[i].getCheckStatus();
					if(!halfCheck.half){
						if(nodes[i].defDep==1){
							window.top.layer.alert('默认部门不能删除！');
							flag = false;
							break;
						}else if(nodes[i].id==-1){
							window.top.layer.alert('根节点不能删除！');
							flag = false;
							break;
						}else{
							ids.push(nodes[i].id);
						}
					}
				}
				if(flag){
					window.top.layer.confirm('确定要删除该部门吗？', {
						  btn: ['确定','取消']//按钮
					  ,title:'询问框'
					  ,icon:3
					},  function(index){
						window.top.layer.close(index);
						$.post('/department/delDep',{random:Math.random(),ids:ids.toString(),sid:"${param.sid}"},function(data){
							if(data.statue=='y'){
								initZtree();
								parent.rightFrame.location ="/department/listPagedUserForDep?sid=${param.sid }&pager.pageSize=10"
							}else if(data.statue=='n'){
								showNotification(2,"该部门已被使用。");
							}else{
								window.parent.location.reload();
							}
						},'json');
					});	
				}
			}else{
				window.top.layer.alert('请勾选需要删除的部门！');
			}
			
		});
	}

	

	//添加树节点
	function addNode(parentId,id,depName,enabled,orderNo) {
		initZtree();
	}

	//更新树节点
	function updateNode(id,depName) {
		var zTree = $.fn.zTree.getZTreeObj('depTree');
		var currentNode = zTree.getNodeByParam("id", id, null);
		currentNode.name=depName;
		zTree.editName(currentNode);
		zTree.cancelEditName();
	}


	function beforeDrop(treeId, treeNodes, targetNode, moveType,isCopy){
		//if(targetNode.id==-1&&(moveType=='prev'||moveType=='next')){
		//	art.dialog.alert('不允许拖拽为根节点！');
			//return false;
		//}
		return false;
	}
	
	//拖拽部门
	function onDrop(event, treeId, treeNodes, targetNode, moveType,isCopy){
		var moveNode=treeNodes[0];
		if(moveType!=null){
			var id=moveNode.id;
			var parentId=(moveNode.getParentNode()).id;
			var orderNo=1;
			if(moveNode.getPreNode()!=null){
				orderNo=(moveNode.getPreNode()).orderNo+1;
			}
			$.post('/department/dragDep',{random:Math.random(),id:id,parentId:parentId,orderNo:orderNo,sid:"${param.sid}"},function(bool){
				if(!bool){
					window.top.layer.alert('拖拽失败！');
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
span{
clear: both;color: inherit;
}

.ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
.ztree li ul.level0 {padding:0; background:none;}
</style>
</head>
<body style="background-color: #FFFFFF;width: 100px">
	<ul id="depTree" class="ztree"></ul>	
	<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
