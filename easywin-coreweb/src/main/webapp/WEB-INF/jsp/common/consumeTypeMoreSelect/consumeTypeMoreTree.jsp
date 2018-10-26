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

<!--Beyond styles-->
<link href="/static/assets/css/beyond.min.css" rel="stylesheet"  type="text/css">
<link href="/static/assets/css/beyond_2.min.css" rel="stylesheet"  type="text/css">
<link href="/static/assets/css/animate.min.css" rel="stylesheet" type="text/css">
<script src="/static/assets/js/skins.min.js"></script>


<!--Core CSS -->
<link href="/static/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" />
<link href="/static/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="/static/assets/css/new_file.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/plugins/layui/layui.js"></script>
<link rel="stylesheet" href="/static/plugins/layui/css/layui.css">
<script src="/static/plugins/layer/layer.js" type="text/javascript" charset="utf-8"></script>

<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
//设置本页的ajax为同步状态
$.ajaxSetup({   
  async : false  
}); 

var selectType="crmType";

	//获取传递的值
	var queryParam=new Object();
	queryParam["sid"]="${param.sid}";
	
	function returnCrmType(){
    var crmType = new Array();
		var treeObj=$.fn.zTree.getZTreeObj("listCrmTypeTree");
        nodes=treeObj.getCheckedNodes(true);
        for(var i=0;i<nodes.length;i++){
	        crmType.push({'id':nodes[i].id,'name':nodes[i].name});
        }
		return crmType;
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
			url : "/common/listConsumeType"
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
	/*勾选操作*/
	function ckNode(treeNode){
	  var json="{id:\'"+treeNode.id+"\',typeName:\'"+treeNode.name+"\'}";
	  parent.appendCrmType(treeNode.checked,json);
	}
	
	/*取消选中*/
	function uncheck(id){
	  var node = zTreeObj.getNodeByParam("id",id, null);
	  zTreeObj.checkNode(node, false, false, false);
	}
	/*取消所有选中*/
	function uncheckAll(){
		zTreeObj.checkAllNodes(false);
	}
	function checkAll(){
		zTreeObj.checkAllNodes(true);
		
		
	}
	//获取样式
	function getFontCss(treeId, treeNode) {
		return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
	}
	
	var zNodes =new Array();
	function initZtree(){
		selectType = "crmType";
		var o =$("#crmTypeSelect",window.parent.document).find("option");
		var oArray = new Array();
		for(var i=0;i<o.length;i++){
		    oArray[i]=o[i].value;
		}
		//销毁所有的zTree
		$.fn.zTree.destroy();
		zNodes =new Array();
		//通过ajax获取组织机构树
		queryParam.random=Math.random();
		$.getJSON('/common/listConsumeType',queryParam,function(tree){
			//创建一个默认的根节点
			var rootNode = new Object;
			rootNode.id = -1;
			rootNode.name='${empty sessionUser.orgName?sessionUser.comId:sessionUser.orgName}';
			rootNode.open = true;
			rootNode.enabled = 1;
			rootNode.childOuter=false;
			rootNode.nocheck=true;
			rootNode.icon="/static/images/base.gif";
			zNodes.push(rootNode);
			var tree = tree.list;
			if (tree != null) {
				for ( var i = 0; i < tree.length; i++) {
					var node = new Object;
					node.id = tree[i].id;
					node.name = tree[i].typeName;
					if(oArray.in_array(node.id)){
						node.checked=true;
					}
					zNodes.push(node);
				}
			}
			zTreeObj=$.fn.zTree.init($('#listCrmTypeTree'), setting, zNodes);
		});
	}


	$(function(){
		initZtree();
		//设置滚动条高度
		var height = $(window).height();
		$("#contentBody").css("height",height+"px");
	});
</script>
<style type="text/css">
	.ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
	.ztree li ul.level0 {padding:0; background:none;}
</style>
</head>
<body style="background-color: #fff">
	<div id="contentBody" class="widget-body no-padding padding-left-10" style="overflow-y:auto;position: relative;">
		<ul id="listCrmTypeTree" class="ztree"></ul>
	</div>
</body>
</html>
