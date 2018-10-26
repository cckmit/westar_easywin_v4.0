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
	var flag = true;
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
			beforeDrop:function(){return false},
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
		parent.resizeVoteH('${functionList.iframe}');
	}
	//收回
	function zTreeOnCollapse(){
		resizeVoteH('leftFrame');
        parent.resizeVoteH('${functionList.iframe}');
	}

	function onClick(e, treeId, node) {
		if(node.data && node.data.functionDescribe){
			var functionDescribe = toHtml(node.data.functionDescribe);
			parent.$("#rightFrame").hide();
			parent.$("#decribe").show();
			parent.$("#describeValue").html(functionDescribe);
			resizeVoteH('${functionList.iframe}');
		}else{
			parent.$("#decribe").hide();
		}
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
		$.post('/functionList/listTreeFun', {random : Math.random(),sid:"${param.sid}",busType:"${functionList.busType}",busId:"${functionList.busId}"}, function(tree) {
			//创建一个默认的根节点
			var rootNode = new Object;
			rootNode.id = -1;
			rootNode.name='${functionList.busName}';
			rootNode.open = true;
			rootNode.icon="/static/images/folder_open.png";
			zNodes.push(rootNode);
			if (tree != null) {
				for ( var i = 0; i < tree.length; i++) {
					var node = new Object;
					node.id = tree[i].id;
					if (tree[i].parentId != null) {
						node.pId = tree[i].parentId;
					}
					node.name = tree[i].functionName;
					if(tree[i].parentId==-1){
						node.open = true;
					}
					node.data=tree[i];
					node.icon="/static/images/page_white_edit.png";
					zNodes.push(node);
				}
			}
			zTreeObj = $.fn.zTree.init($('#funTree'), setting, zNodes);
			resizeVoteH('leftFrame')
            parent.resizeVoteH('${functionList.iframe}');
		},"json");
	}

	function addDep(treeNode){
		var parentId = treeNode.id;
		var busName = treeNode.name;
		parent.$("#decribe").hide();
		parent.$("#rightFrame").show();
		parent.rightFrame.location = '/functionList/addFunPage?parentId='+parentId+'&iframe=${functionList.iframe}&busName='+busName+'&sid=${param.sid}&busType=${functionList.busType}&busId=${functionList.busId}';
	}
	function editDep(treeNode){
		var id = treeNode.id;
		parent.$("#decribe").hide();
		parent.$("#rightFrame").show();
		parent.rightFrame.location = '/functionList/updateFunPage?sid=${param.sid}&iframe=${functionList.iframe}&id='+id;
	}

	//删除
	function del(){
		var zTree = $.fn.zTree.getZTreeObj('funTree');
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
						if(nodes[i].id==-1){
							window.top.layer.alert('根节点不能删除！');
							flag = false;
							break;
						}else{
							ids.push(nodes[i].id);
						}
					}
				}
				if(flag){
					window.top.layer.confirm('确定要删除选中功能吗？', {
						  btn: ['确定','取消']//按钮
					  ,title:'询问框'
					  ,icon:3
					},  function(index){
						window.top.layer.close(index);
						$.post('/functionList/delFunction',{random:Math.random(),ids:ids.toString(),sid:"${param.sid}"},function(data){
							if(data.code=='0'){
								initZtree();
								parent.$("#rightFrame").hide();
								parent.$("#decribe").hide();
								showNotification(1,"删除成功");
							}else{
								showNotification(2,"删除失败");
								window.parent.location.reload();
							}
						},'json');
					});	
				}
			}else{
				window.top.layer.alert('请勾选需要删除的功能！');
			}
			
		});
	}

	

	//添加树节点
	function addNode(parentId,id,depName,enabled,orderNo) {
		initZtree();
	}

	//更新树节点
	function updateNode(id,depName) {
		var zTree = $.fn.zTree.getZTreeObj('funTree');
		var currentNode = zTree.getNodeByParam("id", id, null);
		currentNode.name=depName;
		zTree.editName(currentNode);
		zTree.cancelEditName();
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
	<ul id="funTree" class="ztree"></ul>	
	<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
