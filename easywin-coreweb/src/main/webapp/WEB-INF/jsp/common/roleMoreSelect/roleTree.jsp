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
	function returnRole(){
		return false;
	}

	var zTreeObj;
	var setting = {
		view: {
			selectedMulti: false,
			dblClickExpand: false,
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
			url : "/common/listRole"
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
	//单机展开
	function zTreeOnClick(event, treeId, treeNode) {
	    var callbackFlag = $("#callbackTrigger").attr("checked");
		if(treeNode.nocheck==false){
			zTreeObj.checkNode(treeNode, !treeNode.checked, false, false);
		  ckNode(treeNode);
		}
		zTreeObj.cancelSelectedNode();
	}
	
	/*取消选中*/
	function uncheck(roleid){
	  var node = zTreeObj.getNodeByParam("id",roleid, null);
	  zTreeObj.checkNode(node, false, false, false);
	}
	
	/*勾选操作*/
	function ckNode(treeNode){
	  var json="{roleid:\'"+treeNode.id+"\',rolename:\'"+treeNode.name+"\'}";
	  parent.appendRole(treeNode.checked,json);
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
		var o = parent.o;
		var oArray = new Array();
		for(var i=0;i<o.length;i++){
		    oArray[i]=o[i].value;
		}
		//销毁所有的zTree
		$.fn.zTree.destroy();
		zNodes =new Array();
		//通过ajax获取角色树
		queryParam.random=Math.random();
		$.getJSON('/common/listRole',queryParam,function(tree){
			//创建一个默认的根节点
			var rootNode = new Object;
			rootNode.id = -1;
			rootNode.name='角色';
			rootNode.open = true;
			rootNode.enabled = 1;
			rootNode.childOuter=false;
			rootNode.nocheck=true;
			zNodes.push(rootNode);
			if (tree != null) {
				for ( var i = 0; i < tree.length; i++) {
					var node = new Object;
					node.id = tree[i].id;
					node.pId = -1;
					node.name = tree[i].roleName;
					if(oArray.in_array(node.id)){
						node.checked=true;
					}
					zNodes.push(node);
				}
			}
			zTreeObj=$.fn.zTree.init($('#roleTree'), setting, zNodes);
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
			 updateNodes(nodeList,true);
		 }
	  }
	
	//检索树后高亮显示
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
</script>
<style type="text/css">
	.ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
	.ztree li ul.level0 {padding:0; background:none;}
</style>
</head>
<body>
	<ul id="roleTree" class="ztree"></ul>
</body>
</html>
