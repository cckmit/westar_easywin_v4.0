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
//设置本页的ajax为同步状态
$.ajaxSetup({   
    async : false  
}); 

	//获取传递的值
	var selectType="org";
	var queryParam=new Object();
	queryParam["sid"]='${param.sid}';
	function returnOrg(){
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
		callback: {
			onClick : zTreeOnClick
		}
	};

	
	//单机查询人员
	function zTreeOnClick(event, treeId, treeNode) {
	    var zTree = $.fn.zTree.getZTreeObj("ztree");
	    if(treeNode.level>0){
            if(selectType=="org"){
                parent.listuser(treeNode.id);
            }else if(selectType=="role"){
                parent.listUserByRole(treeNode.id);
            }else{
                parent.listUserByGroup(treeNode.id);
            }
	    }
	}
	
	//获取样式
	function getFontCss(treeId, treeNode) {
		return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
	}
	
	var zNodes =new Array();
	function initZtree(){
		selectType="org"
		//销毁所有的zTree
		$.fn.zTree.destroy();
		zNodes =new Array();
		
		//通过ajax获取组织机构树
		queryParam.random=Math.random();
		$.getJSON('/common/listTreeOrganization',queryParam,function(tree){
			//创建一个默认的根节点
			var rootNode = new Object;
			rootNode.id = -1;
			rootNode.name='${sessionUser.orgName}';
			rootNode.open = true;
			rootNode.enabled = 1;
			rootNode.childOuter=false;
			rootNode.icon="/static/images/base.gif";
			zNodes.push(rootNode);
			if (tree != null) {
				for ( var i = 0; i < tree.length; i++) {
					var node = new Object;
					node.id = tree[i].id;
					if (tree[i].parentId != null) {
						node.pId = tree[i].parentId;
					}
					node.name = tree[i].depName;
					node.allSpelling=tree[i].allSpelling;
					node.firstSpelling=tree[i].firstSpelling;
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
		//initGroup();
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
			 if(selectType=="group" || selectType=="role"){
				 initZtree();
			 }
			 parent.listuser(null);
			 updateNodes(nodeList,false);
			 nodeList = new Array();
			 var myNode = zTreeObj.getNodeByParam("id", ${sessionUser.depId}, null);
			 if(myNode!=null){
				 nodeList.push(myNode);
				 zTreeObj.expandNode(myNode,true,true,true);
				 updateNodes(nodeList,true);
				 parent.listuser(${sessionUser.depId});
			 }
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

	//初始化分组
	function initGroup(){
		selectType = "group";
		//销毁所有的zTree
		$.fn.zTree.destroy();
		zNodes = new Array();
		
		//通过ajax获取组织机构树
		queryParam.random=Math.random();
		$.getJSON('/common/listUserGroup',queryParam,function(tree){
			//创建一个默认的根节点
			var rootNode = new Object;
			rootNode.id = -1;
			rootNode.name='个人分组';
			rootNode.open = true;
			rootNode.enabled = 1;
			rootNode.childOuter=false;
			rootNode.icon="/static/images/base.gif";
			zNodes.push(rootNode);
			if (tree != null) {
				for ( var i = 0; i < tree.length; i++) {
					var node = new Object;
					node.id = tree[i].id;
					node.pId = -1;
					node.name = tree[i].grpName;
					node.level=1;
					node.icon="/static/images/office.gif";
					zNodes.push(node);
				}
			}
			zTreeObj=$.fn.zTree.init($('#organizationTree'), setting, zNodes);
			
		});
	}


//初始化角色
function initRole(){
    selectType = "role";
    //销毁所有的zTree
    $.fn.zTree.destroy();
    zNodes =new Array();

    //通过ajax获取组织机构树
    queryParam.random=Math.random();
    $.getJSON('/role/listRole',queryParam,function(tree){
        //创建一个默认的根节点
        var rootNode = new Object;
        rootNode.id = -1;
        rootNode.name='角色列表';
        rootNode.open = true;
        rootNode.enabled = 1;
        rootNode.childOuter=false;
        rootNode.icon="/static/images/base.gif";
        zNodes.push(rootNode);
        //创建普通人员的结点
        var nNode = new Object;
        nNode.id = 0;
        nNode.pId = -1;
        nNode.name='普通人员';
        nNode.level=1;
        nNode.icon="/static/images/office.gif";
        zNodes.push(nNode);
        if (tree != null) {
            for ( var i = 0; i < tree.length; i++) {
                var node = new Object;
                node.id = tree[i].id;
                node.pId = -1;
                node.name = tree[i].roleName;
                node.level=1;
                node.icon="/static/images/office.gif";
                zNodes.push(node);
            }
        }
        zTreeObj=$.fn.zTree.init($('#organizationTree'), setting, zNodes);

    });
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
<body style="background-color: #fff">
	<ul id="organizationTree" class="ztree"></ul>
</body>
</html>
