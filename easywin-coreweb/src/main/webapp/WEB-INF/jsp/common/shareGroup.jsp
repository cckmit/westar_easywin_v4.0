<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.base.cons.SessionKeyConstant"%>
<%@page import="com.westar.base.util.RequestContextHolderUtil"%>
<%@page import="com.westar.base.model.FileViewScope"%>
<%@page import="com.westar.base.model.FileDetail"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" >
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
var list = ${list};
var zTreeObj;
function returnScope() {
	//是否有选中的，默认没有
	var radioFlag = false;
	var scopetypes = $(":radio[name=radioScopetype]");
	
	var scopetype;
	var scopeGroup;
	for(var i=0;i<scopetypes.length;i++){
		if ($(scopetypes[i]).attr('checked')) {
			var radioId = $(scopetypes[i]).attr("id");
			if(radioId.replace('radio_','')!=0){
				radioFlag = true;
				scopetype = radioId.replace('radio_','');
			}else{
				scopetype = radioId.replace('radio_','');
				var scopeGroup = $(":checkbox[name=scopeGroup]");
				var list = new Array();
				for(var i=0;i<scopeGroup.length;i++){
					if ($(scopeGroup[i]).attr('checked')) {
						radioFlag = true;
						list.push($(scopeGroup[i]).attr('id').replace('checkbox_',''));
					}
				}
				scopeGroup = list;
			}
			if(radioFlag){
				break;
			}
		}
	}
	if(!radioFlag){//没有选中的
		window.top.layer.alert('未选择分享范围');
		return false;
	}else{
		if(scopetype==0 && scopeGroup.length==0){
			window.top.layer.alert('未选择分享范围');
			return false;
		}else{
			return {"scopetype":scopetype,"scopeGroup":scopeGroup}	
		}
	}
}

		var IDMark_A = "_a";

		
		
		var setting = {
			view: {
				addDiyDom: addDiyDom,
				selectedMulti: false,
				dblClickExpand: false,
				fontCss: getFontCss
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback : {
				onDblClick : null
			}
		};

		//获取样式
		function getFontCss(treeId, treeNode) {
			return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
		}

		var zNodes =${selfGroupStr};

		function addDiyDom(treeId, treeNode) {
			var aObj = $("#" + treeNode.tId + IDMark_A);
			if (treeNode.level == 1) {
				var editStr = "<input type='radio' class='radioBtn' id='radio_" +treeNode.id+ "' name='radioScopetype' onfocus='this.blur();'></input>";
				if(treeNode.id==${scopeType}){
					editStr = "<input type='radio' class='radioBtn' id='radio_" +treeNode.id+ "' name='radioScopetype' checked='ckecked' onfocus='this.blur();'></input>";
				}
				aObj.before(editStr);
				var btn = $("#radio_"+treeNode.id);
				if (btn) btn.bind("change", function() {checkBrand(treeNode, btn);});
			} else if (treeNode.level == 2) {
				var editStr = "<input type='checkbox' class='checkboxBtn' id='checkbox_" +treeNode.id+ "' name='scopeGroup' onfocus='this.blur();'></input>";
				if(list.in_array(treeNode.id)){
					editStr = "<input type='checkbox' class='checkboxBtn' id='checkbox_" +treeNode.id+ "' name='scopeGroup' checked='checked' onfocus='this.blur();'></input>";
				}
				aObj.before(editStr);
				var btn = $("#checkbox_"+treeNode.id);
				if (btn) btn.bind("click", function() {checkAccessories(treeNode, btn);});
			}
		}
		/**
		*多选
		*/
		function checkAccessories(treeNode, btn) {
			if(!$("#radio_" + treeNode.getParentNode().id).attr("checked")){
				$("#radio_" + treeNode.getParentNode().id).attr("checked",true)
			}
			if (btn.attr("checked")) {
				$("#checkbox_" + treeNode.id).attr("checked", true);
			} else {
				$("#checkbox_" + treeNode.id).attr("checked", false);
			}
		}
		/**
		 * 单选
		 */
		function checkBrand(treeNode, btn) {
			if (btn.attr("checked")) {
				var pObj = $("#radio_" + treeNode.getParentNode().id);
				if (!pObj.attr("checked")) {
					pObj.attr("checked", true);
				}
				$(":checkbox").attr("checked", false);
			}
		}

		function getCheckedRadio(radioName) {
			var r = document.getElementsByName(radioName);
			for(var i=0; i<r.length; i++)    {
				if(r[i].checked)    {
					return $(r[i]);
				}
			}
			return null;
		}

		$(document).ready(function(){
			$.fn.zTree.destroy();
			zTreeObj =  $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		});
	</script>
	<style type="text/css">
	  .radioBtn {height: 16px;vertical-align: middle;}
	  .checkboxBtn {vertical-align: middle;margin-right: 2px;}
	  </style>
 </head>

<body>
<div class="content_wrap">
	<div class="zTreeDemoBackground left">
		<ul id="treeDemo" class="ztree"></ul>
		<ul id="addGrpUl" class="ztree">
			<li style="text-align: center;color: #1c98dc;cursor: pointer;" onclick="addGrp('${param.sid}')">
				+添加分组
			</li>
		</ul>
	</div>
</div>
</body>
</html>