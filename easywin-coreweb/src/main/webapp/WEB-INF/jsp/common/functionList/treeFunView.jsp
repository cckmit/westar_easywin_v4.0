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
			fontCss : getFont
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onClick : this.onClick,
            onExpand: zTreeOnExpand,
            onCollapse: zTreeOnCollapse
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
			parent.resizeVoteH('${functionList.iframe}')
		}else{
			parent.$("#decribe").hide();
		}
	}
	var actionTreeNode=null;
	

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
			resizeVoteH('leftFrame');
            parent.resizeVoteH('${functionList.iframe}');
		},"json");
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
