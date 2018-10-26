<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript">

//设置本页的ajax为同步状态
$.ajaxSetup({   
    async : false  
}); 

var selectType="org";

	//获取传递的值
	var orgId = art.dialog.data('orgId');
	var orgPathName = art.dialog.data('orgPathName');
	var pq = art.dialog.data('queryParam');
	var queryParam;
	if(pq==""){
		queryParam=new Object();
	}else{
		queryParam = eval('(' + pq + ')');
	}
	queryParam["sid"]="${param.sid}";
	function returnOrg() {
		var nodes = zTreeObj.getSelectedNodes();
		if (nodes == null || nodes.length == 0) {
			art.dialog.alert('请先选择一个机构。');
		} else {
			if (nodes[0].level > 1) {
				art.dialog.data('orgId', nodes[0].id);
				art.dialog.data('orgPathName', nodes[0].pathName);
				art.dialog.close();
			}
		}
		return false;
	}

	var zTreeObj;

	var setting = {
		view : {
			selectedMulti : false,
			dblClickExpand : false,
			fontCss: getFontCss
		},
		data : {
			keep : {
				parent : true,
				leaf : true
			},
			simpleData : {
				enable : true
			}
		},
		callback : {
			onDblClick : returnOrg
		}
	};

	function getFontCss(treeId, treeNode) {
		return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
	}
	
	var zNodes = new Array();
	function initZtree() {
		selectType = "org";
		//销毁所有的zTree
		$.fn.zTree.destroy();
		zNodes = new Array();

		//通过ajax获取组织机构树

		queryParam.random = Math.random();
		$.getJSON('/common/listTreeOrganization', queryParam,
				function(tree) {
					//创建一个默认的根节点
					var rootNode = new Object;
					rootNode.id = -1;
					rootNode.name = '<%=SystemStrConstant.ROOT_ORG_NAME%>';
					rootNode.open = true;
					rootNode.enabled = 1;
					rootNode.childOuter = false;
					rootNode.icon="/static/images/base.gif";
					zNodes.push(rootNode);
					if (tree != null) {
						for ( var i = 0; i < tree.length; i++) {
							var node = new Object;
							node.id = tree[i].id;
							if (tree[i].parentId != null) {
								node.pId = tree[i].parentId;
							}
							node.name = tree[i].orgName;
							node.pathName = tree[i].pathName;
							node.allSpelling=tree[i].allSpelling;
							node.firstSpelling=tree[i].firstSpelling;
							if (tree[i].parentId == -1) {
								node.open = true;
							}
							if(tree[i].orgLevel==1){
								node.icon="/static/images/dep.gif";
							}else{
								node.icon="/static/images/office.gif";
							}
							zNodes.push(node);
						}
					}
					zTreeObj = $.fn.zTree.init($('#organizationTree'), setting,
							zNodes);
					selectOrg();

				});
	}

	function selectOrg() {
		if (orgId != null && orgId != "") {
			var selectedOrgNode = zTreeObj.getNodeByParam("id", orgId);
			zTreeObj.selectNode(selectedOrgNode);
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
</style>
</head>
<body>
	<div style="padding-left: 10px;">
		<a href="javascript:selectAll();">所有</a> 
		&nbsp;&nbsp;<a href="javascript:selectGroup();">分组</a> 
		&nbsp;&nbsp;<a href="javascript:myRootOrg();">本部门</a> 
		&nbsp;&nbsp;<input style="height: 18px;" onblur="blurName(this);" onfocus="focusName(this);" tip="请输入机构名称" value="请输入机构名称" class="gray" name="queryName" id="queryName" onkeydown="enter();"/>&nbsp;<img style="cursor: pointer;" onclick="enter();" src="/static/images/cx.png"/>
	</div>
	<ul id="organizationTree" class="ztree"></ul>
</body>
<script type="text/javascript">
var nodeList;
 function enter(){
	  if(event.keyCode==13){
		 var q = $('#queryName').val();
		 if(q!=""){
			 updateNodes(nodeList,false);
			 nodeList = zTreeObj.getNodesByParamFuzzy("name", q, null);
			 pushArray(nodeList,zTreeObj.getNodesByParamFuzzy("allSpelling", q, null));
			 pushArray(nodeList,zTreeObj.getNodesByParamFuzzy("firstSpelling", q, null));
			 updateNodes(nodeList,true);
		 }
		 $('#queryName').select();
	  }
  }
 
 function updateNodes(nodeList,highlight) {
	 if(nodeList!=undefined&&nodeList!=null&&nodeList.length>0){
			for( var i=0, l=nodeList.length; i<l; i++) {
				nodeList[i].highlight = highlight;
				zTreeObj.updateNode(nodeList[i]);
				if(i==0){
					zTreeObj.selectNode(nodeList[i],true);
				}
				openParent(nodeList[i]);
			}
	 }
}

 function openParent(node){
	 var p = node.getParentNode();
	 zTreeObj.expandNode(p,true,false,false,false);
	 if(p!=null){
		 openParent(p);
	 }
 }
 
//本部门高亮
function myRootOrg(){
	//如果当前是按组选择，则先按机构，再定位本部门
	 if(selectType=="group"){
		 initZtree();
	 }
		 updateNodes(nodeList,false);
		 nodeList = new Array();
		 var myNode = zTreeObj.getNodeByParam("id", ${sessionUser.orgRootId}, null);
		 nodeList.push(myNode);
		 zTreeObj.expandNode(myNode,true,true,true);
		 updateNodes(nodeList,true);
	}
	
function selectGroup(){
	var url = "/organization/listOrgGroupCheck?sid=${param.sid}";
	art.dialog.open(url, {
	title : '分组选择',
	lock : true,
	max : false,
	min : false,
	width : 300,
	height : 380,
	close : function() {
		var groupId = art.dialog.data('groupId');
		var groupName = art.dialog.data('groupName');
		showGroup(groupId,groupName);
	},
	ok : function() {
		var iframe = this.iframe.contentWindow;
		if (!iframe.document.body) {
			return false;
		}
		iframe.ok();
		return false;
	},
	cancel : true
	});
}

//所有机构
function selectAll(){
	initZtree();
}

//按分组显示机构
function showGroup(groupid,groupName){
	selectType = "group";
	//销毁所有的zTree
	$.fn.zTree.destroy();
	zNodes = new Array();

	//通过ajax获取组织机构树
	queryParam.random = Math.random();
	queryParam.orgGroupId = groupid;
	$.getJSON('/common/listOrgTreeByGroup', queryParam,
			function(tree) {
				//创建一个默认的根节点
				var rootNode = new Object;
				rootNode.id = -1;
				rootNode.name = groupName;
				rootNode.open = true;
				rootNode.enabled = 1;
				rootNode.childOuter = false;
				rootNode.icon="/static/images/base.gif";
				zNodes.push(rootNode);
				if (tree != null) {
					for ( var i = 0; i < tree.length; i++) {
						var node = new Object;
						node.id = tree[i].id;
						if (tree[i].parentId != null) {
							node.pId = tree[i].parentId;
						}
						node.name = tree[i].orgName;
						node.pathName = tree[i].pathName;
						node.allSpelling=tree[i].allSpelling;
						node.firstSpelling=tree[i].firstSpelling;
						if (tree[i].parentId == -1) {
							node.open = true;
						}
						if(tree[i].orgLevel==1){
							node.icon="/static/images/dep.gif";
						}else{
							node.icon="/static/images/office.gif";
						}
						zNodes.push(node);
					}
				}
				zTreeObj = $.fn.zTree.init($('#organizationTree'), setting,
						zNodes);
				selectOrg();

			});
}
</script>
</html>
