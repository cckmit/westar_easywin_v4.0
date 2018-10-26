<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">

<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>

<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" >
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<style type="text/css">
#infoList table{
table-layout: fixed;
}
#infoList td{
	padding: 8px 0 !important;
	text-overflow: ellipsis;
	white-space: nowrap;
	overflow: hidden; 
}
#infoList th{
	padding: 8px 0 !important;
}
</style>
</head>
<body  scroll="no" style="background-color: #fff">
        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
             	<span class="widget-caption themeprimary ps-layerTitle">办公用品选择</span>
                   <div class="widget-buttons">
					<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
						<i class="fa fa-times themeprimary"></i>
					</a>
				</div>
             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;padding:5px">
					
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr bgcolor="#FFFFFF">
							<td style="padding-right:0px;">
								<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
									<tr bgcolor="#FFFFFF">
										<td style="padding-left: 1px;height: 430px"></td>
										<td width="180px;" style="vertical-align: top;border: 1px solid #86B1E8;">
											<div class="padding-right-10"
												style="overflow:hidden;overflow-y:auto;position: relative;height: 430px;width: 190px">
												<ul id="bgypFlTree" class="ztree"></ul>
											</div>
										</td>
										<td style="padding-left: 1px;"></td>
										<td bgcolor="#FFFFFF" width="430px" id="infoList"
										style="vertical-align: top;border: 1px solid #86B1E8;">
											
											<div style="width: 100%">
													<table class="table table-hover general-table">
														<thead>
															<tr style="display:block;">
															  <th style="text-align:left;width:48px;padding-left:5px !important;">选择</th>
															  <th style="text-align:left;width:176px">用品名称</th>
															  <th style="text-align:left;width:88px">类别</th>
															  <th style="text-align:center;width:48px">单位</th>
															  <th style="text-align:center;width:48px">库存</th>
															</tr>
														</thead>
													</table>
												</div>
												<div style="width: 100%">
													<table class="table table-hover general-table">
														<tbody id="bgypItemList" style="padding-left: 10px !important;">
														</tbody>
													</table>
												</div>
										</td>
										<td style="padding-left: 1px;"></td>
										<td height="100%" width="110px" style="vertical-align: top;border: 1px solid #86B1E8;">
											<div style=" width: 100%;">
												<select name="bgypItemSelect" id="bgypItemSelect" ondblclick="removeClick(this.id)" style="width: 99%;height:430px;  margin: -10 -10 -10 -10;" multiple="multiple">
												</select>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
</body>
<script type="text/javascript">

var pageBgypIds;
function initData(preBgypIds){
	pageBgypIds = preBgypIds;
}

var sid='${param.sid}'
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
    var zTree = $.fn.zTree.getZTreeObj("bgypFlTree");
    
    myScroll.slimscroll({
        scrollTo:'0px'
    });
    if(treeNode.level>0){
    	var flId = treeNode.id;
    	$.getJSON('/bgypItem/ajaxListBgypStore4FlWithSub', {random : Math.random(),sid:sid,flId:flId},function(data){
    		var listBgypStore = data.listBgypStore;
    		
    		$("#bgypItemList").html('');
    		if(listBgypStore && listBgypStore.length>0){
    			$.each(listBgypStore,function(index,obj){
    				var $tr = $("<tr style='width:430px'></tr>");
    				var $td0 = $("<td valign='middle' style='width:48px !important;padding-left:5px !important;' class='no-padding-top no-padding-bottom no-margin'><label class='no-margin-bottom'><input class='colored-blue' type='checkbox'/><span class='text' style='color: inherit;'></span></label></td>");
    				var $td1 = $("<td valign='middle' style='text-align:left;width:width:176px !important;'>["+obj.bgypCode+"]"+obj.bgypName+"</td>");
    				var $td2 = $("<td valign='middle' style='text-align:left;width:80px !important;'>"+obj.flName+"</td>");
    				var $td3 = $("<td valign='middle' style='text-align:center;width:48px !important;'>"+obj.bgypUnitName+"</td>");
    				var $td4 = $("<td valign='middle' style='text-align:center;width:48px !important;'>"+obj.storeNum+"</td>");
    				
    				var len = $("#bgypItemSelect").find("option[value='"+obj.id+"']").length;
    				if(len>0){
    					$td0.find(":checkbox").attr("checked",true);
    				}
    				if($.inArray(obj.id+'',pageBgypIds)>=0){
    					$td0.find(":checkbox").attr("disabled","disabled");
    					$tr.attr("title","已选");
    				}
    				$tr.append($td0);
    				$tr.append($td1);
    				$tr.append($td2);
    				$tr.append($td3);
    				$tr.append($td4);
    				
    				$tr.attr("id","bgypItem_"+obj.id)
    				
    				$("#bgypItemList").append($tr);
    				
    				
    				$tr.data("bgypItem",obj)
    			})
    		}else{
    			
    		}
    	});
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
	$.getJSON('/bgypFl/listTreeBgypFl', {random : Math.random(),sid:sid},function(tree){
		//创建一个默认的根节点
		var rootNode = new Object;
		rootNode.id = -1;
		rootNode.name='办公用品分类';
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
				node.name = '['+tree[i].flCode+']'+tree[i].flName;
				node.realName = tree[i].flName;
				if(tree[i].orgLevel==1){
					node.icon="/static/images/dep.gif";
				}else{
					node.icon="/static/images/office.gif";
				}
				zNodes.push(node);
			}
		}
		zTreeObj=$.fn.zTree.init($('#bgypFlTree'), setting, zNodes);
		
	});
}


$(function(){
	initZtree();
	
	$("body").on("click","#bgypItemList :checkbox",function(){
		var checkedState = $(this).attr("checked");
		
		var bgypItem = $(this).parents("tr").data("bgypItem");
		if(checkedState){
			var $option = $("<option value='"+bgypItem.id+"'>"+bgypItem.bgypName+"</option>");
			 $option.data("bgypItem",bgypItem)
			$("#bgypItemSelect").append($option);
		}else{
			$("#bgypItemSelect").find("option[value='"+bgypItem.id+"']").remove();
		}
	})
});
//双击移除
function removeClick(id){
	var $option = $("#bgypItemSelect").find("option:selected");
	var trId = "bgypItem_"+$option.val();
	
	$option.remove();
	
	$("#"+trId).find(":checkbox").attr("checked",false);
}
var myScroll;
$(function () {
	myScroll =  $("#bgypItemList").parent().parent().slimScroll({
        height:"370px"
    });
    $("#bgypFlTree").parent().slimScroll({
        height:"430px",
        width:"190px"
    });
});
//移除所有数据
function removeAllOptions(){
	$("#bgypItemSelect").html('');
	$("#bgypItemList").find(":checkbox").attr("checked",false);
}
//返回结果
function returnBgypItems(){
	var bgypItems = new Array();
	$.each($("#bgypItemSelect").find("option"),function(index,obj){
		bgypItems.push($(this).data("bgypItem"))
	})
	return bgypItems;
}
</script>
</html>
