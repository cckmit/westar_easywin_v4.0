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
	var userid=art.dialog.data('userid');
	var pq = art.dialog.data('queryParam');
	var queryParam;
	if(pq==""){
		queryParam=new Object();
	}else{
		queryParam = eval('(' + pq + ')');
	}
	queryParam["sid"]="${param.sid}";
	function returnOrg(){
		return false;
	}

	var zTreeObj;
	var setting = {
		view: {
			selectedMulti: false,
			dblClickExpand: true,
			fontCss: getFontCss
		},
		data: {
			keep: {
				parent:true,
				leaf:true
			},
			simpleData: {
				enable: true
			}
		},
		async : {
			enable : true,
			url : "/common/listTreeOrganization"
		},
		check : {
			enable : true,
			chkStyle: "checkbox",
		    radioType: "all",
		    chkboxType: { "Y": "", "N": "" }
		},
		callback: {
			onClick : zTreeOnClick,
			onCheck: onCheck
		}
	};
	
	//勾选时的操作
    function onCheck(e, treeId, treeNode) {
        ckNode(treeNode);
	}
	//单机
	function zTreeOnClick(event, treeId, treeNode) {
	    var callbackFlag = $("#callbackTrigger").attr("checked");
		if(treeNode.nocheck==false){
			zTreeObj.checkNode(treeNode, !treeNode.checked, false, false);
		  ckNode(treeNode);
		}
		zTreeObj.cancelSelectedNode();
	}
	/*勾选操作*/
	function ckNode(treeNode){
	  var json="{depid:\'"+treeNode.id+"\',depname:\'"+treeNode.name+"\',pathName:\'"+treeNode.pathName+"\'}";
	  parent.appendDep(treeNode.checked,json);
	}
	
	/*取消选中*/
	function uncheck(depid){
	  var node = zTreeObj.getNodeByParam("id",depid, null);
	  zTreeObj.checkNode(node, false, false, false);
	}
	/*取消所有选中*/
	function uncheckAll(){
		zTreeObj.checkAllNodes(false);
	}
	//获取样式
	function getFontCss(treeId, treeNode) {
		return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
	}
	
	var zNodes =new Array();
	function initZtree(){
		selectType = "org";
		var o = parent.o;
		var oArray = new Array();
		for(var i=0;i<o.length;i++){
		    oArray[i]=o[i].value;
		}
		//销毁所有的zTree
		$.fn.zTree.destroy();
		zNodes =new Array();
		//通过ajax获取组织机构树
		queryParam.random=Math.random();
		$.getJSON('/common/listTreeOrganization',queryParam,function(tree){
			//创建一个默认的根节点
			var rootNode = new Object;
			rootNode.id = -1;
			rootNode.name='<%=SystemStrConstant.ROOT_ORG_NAME%>';
			rootNode.open = true;
			rootNode.enabled = 1;
			rootNode.childOuter=false;
			rootNode.nocheck=true;
			rootNode.icon="/static/images/base.gif";
			zNodes.push(rootNode);
			if (tree != null) {
				for ( var i = 0; i < tree.length; i++) {
					var node = new Object;
					node.id = tree[i].id;
					if (tree[i].parentId != null) {
						node.pId = tree[i].parentId;
					}
					if(tree[i].orgLevel<=1){
						node.nocheck=true;
					}
					node.name = tree[i].orgName;
					node.pathName= tree[i].pathName;
					node.allSpelling=tree[i].allSpelling;
					node.firstSpelling=tree[i].firstSpelling;
					if(oArray.in_array(node.id)){
						node.checked=true;
					}
					if(tree[i].orgLevel==1){
						node.icon="/static/images/dep.gif";
					}else{
						node.icon="/static/images/office.gif";
					}
					zNodes.push(node);
				}
			}
			zTreeObj=$.fn.zTree.init($('#organizationTree'), setting, zNodes);
		});
	}


	$(function(){
		initZtree();
	});
	
	
	var nodeList;
	//回车后模糊检索树
	 function enter(q){
		 if(q!=""){
			 updateNodes(nodeList,false);
			 nodeList = zTreeObj.getNodesByParamFuzzy("name", q, null);
			 pushArray(nodeList,zTreeObj.getNodesByParamFuzzy("allSpelling", q, null));
			 pushArray(nodeList,zTreeObj.getNodesByParamFuzzy("firstSpelling", q, null));
			 updateNodes(nodeList,true);
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
	
	
	//检索后树节点高亮显示
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

	//展开指定节点的所有父节点
	 function openParent(node){
		 var p = node.getParentNode();
		 zTreeObj.expandNode(p,true,false,false,false);
		 if(p!=null){
			 openParent(p);
		 }
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
	 				rootNode.nocheck=true;
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
	 				//selectOrg();

	 			});
	 }
</script>
<style type="text/css">
	.ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
	.ztree li ul.level0 {padding:0; background:none;}
</style>
</head>
<body>
	<ul id="organizationTree" class="ztree"></ul>
</body>
</html>
