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

<SCRIPT type="text/javascript">
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
var areaParam;
function initValue(area){
	areaParam = area;
	if(area.level>-1){
		$("#modTitle").html(area.areaName+"待导入数据");
	}
	initZtree();
}
	var setting = {
		check: {
			enable: true,
			ckeStyle:"checkbox",
			chkboxType:{"Y":"ps","N":"ps"}
		},
		
		data: {
			simpleData: {
				enable: true
			},
		},
		callback: {
			onNodeCreated: onNodeCreated
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
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
	});
	var zNodes = new Array();
	function initZtree(){
		ruler.init("ruler");
		//销毁所有的zTree
		$.fn.zTree.destroy();
		zNodes = new Array();
		//通过ajax获取部门树
		$.post('/crm/modArea', {
			random : Math.random(),
			sid:"${param.sid}",
			"areaId":areaParam.areaId,
			"regionId":areaParam.regionId,
			"level":areaParam.level,
			"areaName":areaParam.areaName
			}, 
			function(tree) {
			if(tree!=null && tree.length>0){
				$("#showData").show();
				$("#hideData").hide();
				for ( var i = 0; i < tree.length; i++) {
					var needMod = tree[i].needMod;
					var needLeafMod = tree[i].needLeafMod;
					if(needMod>=1 || needLeafMod>=1){
						var node = new Object;
						
						node.id = tree[i].id;
						node.pId = tree[i].parentId;
						node.name = tree[i].regionName;
						node.level = tree[i].regionLevel;
						node.needMod = needMod;
						node.areaId = tree[i].areaId;
						node.needLeafMod = needLeafMod;
						if(tree[i].parentId==-1){
							node.open = true;
						}
						zNodes.push(node);
					}
				}
				
			}else{
				$("#showData").hide();
				$("#hideData").show();
			}
			showNodeCount = 0;
			$("#treeDemo").empty();
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		},"json");
		
	}
	/**
	 * 添加区域
	 */
	function addArea(){
		var flag = false;
		if($("#done").val()=='0'){
			$("#done").attr("value","1");
			var zTree = $.fn.zTree.getZTreeObj('treeDemo');
			var nodes=zTree.getCheckedNodes();
	
			var saveData=[];  
	        
			for(var i=0;i<nodes.length;i++){
		        var data={"areaName":nodes[i].name,
					"id":nodes[i].areaId,
					"regionId":nodes[i].id,
					"parentId":areaParam.parentId,//只有第一级需要设定父类
					"modParentId":nodes[i].pId,
					"needMod":nodes[i].needMod,
					"needLeafMod":nodes[i].needLeafMod,
					"isLeaf":nodes[i].isParent?0:1}; 
		        saveData.push(data); 
			}
			if(saveData.length==0){
				parent.layer.alert("请选择需要导入的区域！",{title:'警告',icon:0});
				$("#done").attr("value","0");
			}else{
				$.ajax({ 
			        type: "post",
			        async: false,
			        dataType: "json", 
			        contentType:"application/json",
			        data:JSON.stringify(saveData),
			        url: "/crm/ajaxAddArea?sid=${param.sid}&t="+Math.random(),
			      	success: function (data) {
				      	$("#done").attr("value","0");
				      	flag = true;
			        },
			        error: function (e) {
				      	$("#done").attr("value","0");
				      	flag = false;
			        }
			    });
			}
		}else{
			parent.layer.alert("请不要重复设置！");
			flag = false;
		}
		return flag;
	}
	</SCRIPT>
 </HEAD>

<BODY>

<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">从模板导入区域</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
					<div style="padding-left:10px;display: none;" id="showData">
						<input type="hidden" id="done" value="0"/>
						<h1 id="modTitle">全国省市区</h1>
						<div>
							<div>
								<ul id="treeDemo" class="ztree"></ul>
							</div>
						</div>
					</div>
					<div id="hideData" class="padding-top-50" style="display: none;">
						<section class="error-container text-center">
							<h1>
								<i class="fa fa-exclamation-triangle"></i>
							</h1>
							<div class="error-divider">
								<h2>模板区域数据已全部导入！</h2>
							</div>
						</section>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</BODY>
</HTML>
