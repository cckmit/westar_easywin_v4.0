<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" class="bg-white">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>

<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" >
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="/static/js/cookieInfo.js"></script>
<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/jfzbJs/jfzb.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
var result;
//选择项目出发
function returnJfzb(){

	if(!result || result.length==0){
		window.top.layer.alert("请选择指标信息",{icon:7});
	}
	var results = new Array();
	$.each(result,function(key,obj){
		results.push(obj);
	})
	return results;
}
</script>
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
<body>
<body  scroll="no" style="background-color: #fff">
      	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
           	<span class="widget-caption themeprimary ps-layerTitle">评分标准</span>
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
												<ul id="jfzbTypeTree" class="ztree"></ul>
											</div>
										</td>
										<td style="padding-left: 1px;"></td>
										<td bgcolor="#FFFFFF" id="infoList"
										style="vertical-align: top;border: 1px solid #86B1E8;">

											<div style="width: 100%">
													<table class="table table-hover general-table">
														<thead>
															<tr style="display:block;">
															    <th style="width:50px; text-align: center;" valign="middle">选项</th>
																<th style="width:90px" class="hidden-phone">评分类别</th>
																<th style="width:50px" valign="middle" class="no-padding">最高分</th>
																<th style="width:50px" valign="middle" class="no-padding">最低分</th>
																<th style="width:100px" valign="middle" class="no-padding">评分指标</th>
																<th valign="middle" class="no-padding">描述</th>
															</tr>
														</thead>
													</table>
												</div>
												<div style="width: 100%">
													<table class="table table-hover general-table">
														<tbody id="allTodoBody" style="padding-left: 10px !important;">
														</tbody>
													</table>
												</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
    </div>
<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>
<script type="text/javascript">

var selectWay;
var sid = "${param.sid}";
var pageParams;
function initSelectWay(selectWayObj,preParams){
	selectWay = selectWayObj;
	pageParams = preParams;
	jfzbSecelct.initZtree();
}
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
			onClick :zTreeOnClick
		}
	};
var zTreeObj;
var dbClickFunction;
var zNodes =new Array();
var jfzbSecelct = {
		initZtree:function(){
			//销毁所有的zTree
			$.fn.zTree.destroy();
			zNodes =new Array();
            var jfzbTypeId = getCookie('jfzbTypeId');
			jfzbListForm.loadJfzbType(function(list){
					//创建一个默认的根节点
					var rootNode = new Object;
					rootNode.id = -1;
					rootNode.name='评分指标分类';
					rootNode.open = true;
					rootNode.enabled = 1;
					rootNode.childOuter=false;
					rootNode.icon="/static/images/base.gif";
					zNodes.push(rootNode);
				if(list && list[0]){
					$.each(list,function(index,jfzbType){
						var node = new Object;
						node.id = jfzbType.id;
						node.pId = -1;
						node.name =jfzbType.typeName;
						node.icon="/static/images/office.gif";
						zNodes.push(node);
					});
					zTreeObj=$.fn.zTree.init($('#jfzbTypeTree'), setting, zNodes);
				}

                if(jfzbTypeId){
                    var node = zTreeObj.getNodeByParam('id', jfzbTypeId, null);
                    if (node!=null) {
                        zTreeObj.selectNode(node, false, false);
                    }
                }

			})

			jfzbSecelct.loadJfzbTypeDetail(jfzbTypeId);
		},loadJfzbTypeDetail:function(jfzbTypeId){
			var params={"sid":sid}

			if(jfzbTypeId && jfzbTypeId>0){
				params.jfzbTypeId=jfzbTypeId;
			}
			params = $.extend(params,pageParams);

			getSelfJSON("/jfzb/ajaxListJfzbForSelect",params,function(data){
				if(data.status=='y'){
					var list = data.lists;
					jfzbSecelct.constrItemDataTable(list)
				}
			})
		},constrItemDataTable:function(jfzbs){
			$("#allTodoBody").html('')
			if(jfzbs && jfzbs[0]){
				$.each(jfzbs,function(jfzbIndex,jfzb){
					var $tr = $("<tr></tr>");
					var $td0 = $("<td valign='middle' style='padding-left:5px !important;width:50px;text-align: center;' class='padding-top-5 padding-bottom-5 no-margin'><label class='no-margin-bottom'><input class='colored-blue' name='jfzbId' type='checkbox'/><span class='text' style='color: inherit;'></span></label></td>");
					if(selectWay == 1){//单选
						$td0.find("input").remove();
						$td0.find("span").before("<input class='colored-blue' name='jfzbId' type='radio'/>");
					}


					var $td1 = $("<td valign='middle' style='text-align:left;width:90px;'>"+jfzb.jfzbTypeName+"</td>");
					var $td2 = $("<td valign='middle' style='text-align:left;width:50px;'>"+jfzb.jfTop+"分</td>");

					var $td3 = $("<td valign='middle' style='text-align:center;width:50px;'>"+jfzb.jfBottom+"分</td>");
					var $td4 = $("<td valign='middle' style='width:100px'>"+jfzb.leveTwo+"</td>");
					var $td5 = $("<td valign='middle' class='jfzbTip'>"+jfzb.describe+"</td>");
					$($td5).data("jfzbTip",jfzb.describe)

					$tr.append($td0);
					$tr.append($td1);
					$tr.append($td2);
					$tr.append($td3);
					$tr.append($td4);
					$tr.append($td5);

					$tr.attr("id","jfzb_"+jfzb.id)
					$tr.addClass("jfzbTr")

					$("#allTodoBody").append($tr);


					$tr.data("jfzbObj",jfzb);
				})
			}
		}
}
function zTreeOnClick(event, treeId, treeNode){
	 var zTree = $.fn.zTree.getZTreeObj("jfzbTypeTree");
	 myScroll.slimscroll({
        scrollTo:'0px'
    });
	 var jfzbTypeId = treeNode.id;
	 jfzbSecelct.loadJfzbTypeDetail(jfzbTypeId);
    setCookie("jfzbTypeId",jfzbTypeId);
}
function getFontCss(treeId, treeNode){
	return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
}


var myScroll;
$(function () {
	myScroll =  $("#allTodoBody").parent().parent().slimScroll({
        height:"370px"
    });
    $("#jfzbTypeTree").parent().slimScroll({
        height:"430px",
        width:"190px"
    });

    $("body").on("mouseover",".jfzbTip",function(){
    	var jfzbTip = $(this).data("jfzbTip");
    	layer.tips("<span style='width:30px;color:white'>"+jfzbTip+"</span>",$(this),{tips:1});
    });
    $("body").on("mouseout",".jfzbTip",function(){
    	layer.closeAll("tips");
    });

  //操作删除和复选框
	$("body").on("click","tr[class='jfzbTr']",function(){
		var jfzbObj = $(this).data("jfzbObj");
		var jfzbId = jfzbObj.id;
		var jfzbTypeName = jfzbObj.jfzbTypeName;
		if(selectWay == 1){//单选
			var radio = $(this).find("input[type=radio]");
			$(radio).attr("checked",true);
			result = {};
			result[jfzbId+"_"]=jfzbObj;

		}else{//多选
			var checkbox = $(this).find("input[type=checkbox]");


			if(checkbox.attr("checked")){
				$(checkbox).attr("checked",false);
				if(result[jfzbId+"_"]){
					result[jfzbId+"_"]=null;
				}
			}else{
				$(checkbox).attr("checked",true);
				if(!result[jfzbId+"_"]){
					result[jfzbId+"_"]=jfzbObj;
				}
			}
		}
	});

    $("body").live("dblclick","tr[class='jfzbTr']",function(){
        var index = parent.layer.getFrameIndex(window.name);
        dbClickFunction();
    });


});


</script>


</body>
</html>
