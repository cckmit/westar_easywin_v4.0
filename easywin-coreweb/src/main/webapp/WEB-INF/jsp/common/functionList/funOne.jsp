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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" >
<link href="/static/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<style>
 body:before{
	background-color: #FBFBFB;
}	
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

<body>
<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">功能单选</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;padding:5px">
					<div style="padding-left: 10px;">
						<img src="/static/images/search.png" width="14" height="18" align="left"/>&nbsp;&nbsp;<input style="height: 18px;" name="queryName" id="queryName" placeholder="请输入功能名"/>&nbsp;<img style="cursor: pointer;" onclick="query();" src="/static/images/cx.png"/>
					</div>
				
					<ul id="funTree" class="ztree" style="padding-left: 10px;"></ul>
	
				</div>
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
var zTreeObj;
var flag = ${userInfo.admin}>0;
var setting = {
	view : {
		selectedMulti : false,
		fontCss : getFont
	},
	data : {
		simpleData : {
			enable : true
		}
	}
	
};

var actionTreeNode=null;



//根节点点击后不打开关闭
function dblClickExpand(treeId, treeNode) {
	return treeNode.level > 0;
}
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
function getFont(treeId, node) {
	return node.font ? node.font : {};
}

var zNodes = new Array();

function initZtree(functionName) {
	//销毁所有的zTree
	$.fn.zTree.destroy();
	zNodes = new Array();
	//通过ajax获取部门树
	$.post('/functionList/listTreeFun', {random : Math.random(),sid:"${param.sid}",busType:"${functionList.busType}",busId:"${functionList.busId}",functionName:functionName}, function(tree) {
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
	},"json");
}


$(function() {
	initZtree();
})

function returnFun() {
	var nodes = zTreeObj.getSelectedNodes();
	if (nodes == null || nodes.length == 0) {
		return null;
	} else {
		if (nodes[0].level > 0 || (nodes[0].level == 0)) {
			return {'parentId':nodes[0].id,'busName': nodes[0].name}
		}
	}
	return false;
}

function query() {
	initZtree($("#queryName").val());
}
</script>
</html>
