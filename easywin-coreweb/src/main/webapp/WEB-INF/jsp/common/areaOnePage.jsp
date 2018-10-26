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

<SCRIPT type="text/javascript">
var rootNode = '${param.rootNode}';
	var setting = {
		
		data: {
			simpleData: {
				enable: true
			},
		},
		callback: {
			onNodeCreated: onNodeCreated,
			onDblClick: returnArea
			
		}
	};

	var ruler = {
		doc: null,
		ruler: null,
		cursor: null,
		minCount: 5000,
		count: 5000,
		stepCount:500,
		minWidth: 30,
		maxWidth: 215,
		init: function() {
			ruler.doc = $(document);
			ruler.ruler = $("#ruler");
			ruler.cursor = $("#cursor");
			if (ruler.ruler) {
				ruler.ruler.bind("mousedown", ruler.onMouseDown);
				
			}
		},
		onMouseDown: function(e) {
			ruler.drawRuler(e, true);
			ruler.doc.bind("mousemove", ruler.onMouseMove);
			ruler.doc.bind("mouseup", ruler.onMouseUp);
			ruler.doc.bind("selectstart", ruler.onSelect);
			$("body").css("cursor", "pointer");
		},
		onMouseMove: function(e) {
			ruler.drawRuler(e);
			return false;
		},
		onMouseUp: function(e) {
			$("body").css("cursor", "auto");
			ruler.doc.unbind("mousemove", ruler.onMouseMove);
			ruler.doc.unbind("mouseup", ruler.onMouseUp);
			ruler.doc.unbind("selectstart", ruler.onSelect);
			ruler.drawRuler(e);
		},
		onSelect: function (e) {
			return false;
		},
		getCount: function(end) {
			var start = ruler.ruler.offset().left+1;
			var c = Math.max((end - start), ruler.minWidth);
			c = Math.min(c, ruler.maxWidth);
			return {width:c, count:(c - ruler.minWidth)*ruler.stepCount + ruler.minCount};
		},
		drawRuler: function(e, animate) {
			var c = ruler.getCount(e.clientX);
			ruler.cursor.stop();
			if ($.browser.msie || !animate) {
				ruler.cursor.css({width:c.width});
			} else {
				ruler.cursor.animate({width:c.width}, {duration: "fast",easing: "swing", complete:null});
			}
			ruler.count = c.count;
			ruler.cursor.text(c.count);
		}
	}
	var showNodeCount = 0;
	function onNodeCreated(event, treeId, treeNode) {
		showNodeCount++;
	}

		
	$(function() {
		initZtree();
		
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
	});
	var zTreeObj;
	var zNodes = new Array();
	function initZtree(){
		ruler.init("ruler");
		//销毁所有的zTree
		$.fn.zTree.destroy();
		zNodes = new Array();
		//通过ajax获取部门树
		$.post('/common/areaOne', {random : Math.random(),sid:"${param.sid}"}, function(tree) {
			if(tree!=null){
				for ( var i = 0; i < tree.length; i++) {
						var node = new Object;
						node.id = tree[i].id;
						if (tree[i].parentId != null) {
							node.pId = tree[i].parentId;
						}
						node.name = tree[i].areaName;
						node.level = tree[i].level;
						node.allSpelling=tree[i].allSpelling;
						node.firstSpelling=tree[i].firstSpelling;
						//if(tree[i].parentId==-1){
							//node.open = true;
						//}
						node.open = false;
						zNodes.push(node);
				}
				
			}
			showNodeCount = 0;
			$("#treeDemo").empty();
			zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		    //$.fn.zTree.getZTreeObj("treeDemo").expandAll(false);
		},"json");
	}
	/**
	 * 添加区域
	 */
	function returnArea(){
		var zTree = $.fn.zTree.getZTreeObj('treeDemo');
		var nodes=zTree.getSelectedNodes();
		
		if(nodes.length==0){
			window.top.layer.alert("请选择一个区域",{icon:7,title:false,closeBtn:0,btn:["关闭"]});
			return false;
		}else if(nodes.length>1){
			window.top.layer.alert("只能选择一个区域",{icon:7,title:false,closeBtn:0,btn:["关闭"]});
			return false;
		}
		if(nodes[0].level==0 && rootNode==0){
			window.top.layer.alert("不能选择根节点",{icon:7,title:false,closeBtn:0,btn:["关闭"]});
			return false;
		}
		var idAndType; 
		var areaName; 
		if(nodes[0].isParent){
			idAndType = nodes[0].id+"@0";
		}else{
			idAndType = nodes[0].id+"@1";
		}
		if(nodes[0].level<=1){
			areaName = nodes[0].name;
		}else{
			areaName = nodes[0].getParentNode().name+"/"+nodes[0].name;
		}
		var result={"idAndType":idAndType,"areaName":areaName};
		return result;
	}
	</SCRIPT>
 </HEAD>

<BODY style="background-color:#fff;">

<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">客户区域选择</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
					<div>
						<div style="padding-left:10px;">
								助记码：<input style="height: 18px;" onblur="blurName(this);" onfocus="focusName(this);" tip="请输入区域" value="请输入区域" class="gray" name="queryName" id="queryName" onkeydown="enter();"/>&nbsp;
								<img style="cursor: pointer;" onclick="search();" src="/static/images/cx.png"/>
							</div>
							<div >
								<div>
									<ul id="treeDemo" class="ztree"></ul>
								</div>
							</div>
						</div>

				</div>
			</div>
		</div>
	</div>
</div>
</BODY>
<script type="text/javascript">
//查询
 function enter(){ 
	 var nodeList =  nodeList=[];
	 var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	 treeObj.cancelSelectedNode();
	 	 
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
 //查询
 function search(){
	 var nodeList =  nodeList=[];
	 var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	 treeObj.cancelSelectedNode();
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
</HTML>
