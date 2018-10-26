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
<script type="text/javascript" src="/static/js/jquery.slimscroll.min.js"></script>
<script type="text/javascript" src="/static/plugins/layui/layui.js"></script>
<link rel="stylesheet" href="/static/plugins/layui/css/layui.css">
<script src="/static/plugins/layer/layer.js" type="text/javascript" charset="utf-8"></script>

<script type="text/javascript">

	
	function returnBgfl() {
		var nodes = zTreeObj.getSelectedNodes();
		if (nodes == null || nodes.length == 0) {
			window.top.layer.alert('请先选择一个分类。');
		} else {
			return {'bgflId':nodes[0].id,'bgflName': nodes[0].realName}
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
			ondblClick : returnBgfl
		}
	};

	function getFontCss(treeId, treeNode) {
		return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
	}
	
	var zNodes = new Array();
	function initZtree() {
		//销毁所有的zTree
		$.fn.zTree.destroy();
		zNodes = new Array();

		//通过ajax获取组织机构树

		$.getJSON('/bgypFl/listTreeBgypFl', {sid:'${param.sid}'},
				function(tree) {
					//创建一个默认的根节点
					var rootNode = new Object;
					rootNode.id = -1;
					rootNode.name = '办公用品分类';
					rootNode.realName = '办公用品分类';
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
							node.name = '['+tree[i].flCode+']'+tree[i].flName;
							node.realName = tree[i].flName;
							if (tree[i].parentId == -1) {
								node.open = true;
							}
							if(tree[i].depLevel==1){
								node.icon="/static/images/dep.gif";
							}else{
								node.icon="/static/images/office.gif";
							}
							zNodes.push(node);
						}
					}
					zTreeObj = $.fn.zTree.init($('#organizationTree'), setting,
							zNodes);

				});
	}


	$(function() {
		initZtree();
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#organizationTree").slimScroll({
	        height:"350px"
	    });
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

<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">办公用品分类单选</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="padding:5px">
					<ul id="organizationTree" class="ztree"></ul>
	
				</div>
			</div>
		</div>
	</div>
</div>
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
 //所有机构
 function selectAll(){
 	initZtree();
 }
</script>
</html>
