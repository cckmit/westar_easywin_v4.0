<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.base.cons.SessionKeyConstant"%>
<%@page import="com.westar.base.util.RequestContextHolderUtil"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>

<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" >
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
var oldParentId;
var selectType="org";
var type ='${param.type}'
var queryParam =new Object();
//初始化数据
function initData(preDirs,oldPId){
	oldParentId = oldPId;
	queryParam["sid"]="${param.sid}";
	queryParam["preDirs"]= preDirs;
	queryParam["type"]= type;
	initZtree();
}
//返回结果值
	function returnDir() {
		var nodes = zTreeObj.getSelectedNodes();
		if (nodes == null || nodes.length == 0) {
			window.top.layer.alert('未选择文件夹。');
		} else {
			if(nodes[0].id==oldParentId){
				window.top.layer.alert('已在该文件夹。');
			}else{
				var dir = {};
				dir.id = nodes[0].id;
				dir.dirName = nodes[0].name;
				return dir;
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
			onDblClick : null
			//onDblClick : returnDir
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
		if(queryParam.type=="023"){
			var url = "/common/listVideoTreeDir"
		}else{
			var url = "/common/listTreeDir"
		}
		//通过ajax获取组织机构树
		queryParam.random = Math.random();
		$.getJSON(url, queryParam,
				function(tree) {
					//创建一个默认的根节点
					var rootNode = new Object;
					rootNode.id = -1;
					rootNode.name = '全部文件';
					rootNode.open = true;
					rootNode.enabled = 1;
					rootNode.childOuter = false;
					rootNode.icon="/static/images/folder_open.png";
					zNodes.push(rootNode);
					if (tree != null) {
						for ( var i = 0; i < tree.length; i++) {
							var node = new Object;
							node.id = tree[i].id;
							if (tree[i].parentId != null) {
								node.pId = tree[i].parentId;
							}
							node.name = tree[i].typeName;
							if (tree[i].parentId == -1) {
								node.open = true;
							}
							if(tree[i].level==1){
								node.icon="/static/images/folder_open.png";
							}else{
								node.icon="/static/images/folder_open.png";
							}
							zNodes.push(node);
						}
					}
					zTreeObj = $.fn.zTree.init($('#organizationTree'), setting,zNodes);

				});
	}
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
		&nbsp;&nbsp;<input style="height: 18px;" onblur="blurName(this);" onfocus="focusName(this);" tip="请输入文件夹名称" value="请输入文件夹名称" class="gray" name="queryName" id="queryName" onkeydown="enter();"/>&nbsp;<img style="cursor: pointer;" onclick="enter();" src="/static/images/cx.png"/>
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

</script>
</html>
