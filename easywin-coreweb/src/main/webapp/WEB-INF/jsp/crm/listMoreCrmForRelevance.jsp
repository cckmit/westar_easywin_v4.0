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
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
//选择项目出发
function crmSelected(){
	
	var result;
	var options = $("#crmSelect").find("option");
	if(options.length==0){
		window.top.layer.alert("请选择项目信息",{icon:7});
	}else{
		result = new Array();
		$.each(options,function(index,option){
			var crm = new Object();
			crm.id=$(option).val();
			crm.crmName=$(option).text();
			result.push(crm)
		})
	}
	return result;
}
//选择客户
function crmSelectedV2(ts){
    var crm = $(ts).data("crmObj")
    var crmId = crm.id;
    var crmName = crm.customerName;
    var len = $("#crmSelect").find("option[value="+crmId+"]").length;

	var radio = $(ts).find("input[type=radio]");
	$(radio).attr("checked",true);

	$("#crmSelect").html('')
	var _option = $("<option></option>");
	_option.val(crmId)
	_option.html(crmName)
	$("#crmSelect").append(_option)
}

</script>
<style>

tr{
cursor: auto;
}
</style>
</head>
<body>
<div class="widget no-margin bg-white">
     	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary searchCond">
			<div class="btn-group pull-left">
				<div class="table-toolbar ps-margin">
					<div class="pull-left padding-right-40" style="font-size: 18px">
		             	<span class="widget-caption themeprimary ps-layerTitle">客户列表</span>
					</div>
			     	<div class="btn-group">
						<a class="btn btn-default dropdown-toggle btn-xs" title="区域筛选"
						data-toggle="dropdown">
							区域筛选
							<i class="fa fa-angle-down"></i>
						</a>
					 	<ul class="dropdown-menu dropdown-default">
					 		<li>
								<a href="javascript:void(0)" class="clearValue" relateElement="areaIdAndType">不限条件</a>
							</li>
							<li>
								<a href="javascript:void(0)" class="areaOneSelect" relateElement="areaIdAndType">区域选择</a>
							</li>
			            </ul>
			         </div>
			     </div>
			     <div class="table-toolbar ps-margin">
			         <div class="btn-group">
			             <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="责任人">
			             	责任人<i class="fa fa-angle-down"></i>
			            </a>
			             <ul class="dropdown-menu dropdown-default">
			                <li><a href="javascript:void(0)" class="clearValue" relateElement="owner">不限条件</a>
								</li>
								<li><a href="javascript:void(0)" class="userOneElementSelect" relateElement="owner">人员选择</a>
								</li>
			              </ul>
                     </div>
                </div>
                
                <div class="table-toolbar ps-margin">
	                  <div class="btn-group cond" id="moreCondition_Div">
	                      <a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
	                      		更多
                              	<i class="fa fa-angle-down"></i>
                              </a>
                              <div class="dropdown-menu dropdown-default padding-bottom-10" 
                              style="min-width: 330px;">
							<div class="ps-margin ps-search padding-left-10">
								<span class="btn-xs">起止时间：</span>
							                              	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${item.startDate}" id="startDate" name="startDate" 
							placeholder="开始时间"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
							<span>~</span>
							<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${item.endDate}" id="endDate"  name="endDate"
							placeholder="结束时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})"/>
                      	</div>
                      	<div class="ps-clear padding-top-10" style="text-align: center;">
                      		<button type="button" class="btn btn-primary btn-xs moreSearchBtn noMoreShow">查询</button>
                      		<button type="button" class="btn btn-default btn-xs margin-left-10 moreClearBtn noMoreShow">重置</button>
	                	</div>
	                      </div>
	                  </div>
	              </div>
				                                    
			</div>
            <div class="ps-margin ps-search">
				<span class="input-icon">
				<input  name="customerName" class="form-control ps-input moreSearch" type="text" placeholder="请输入关键字">
				<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
				</span>
			</div>
		
		<div class="ps-clear" id="formTempData">
			 <input type="hidden" id="areaIdAndType" name="areaIdAndType" value=""/>
			 <input type="hidden" id="owner" name="owner" value=""/>
			 
			<select list="listCreator" listkey="id" listvalue="userName" id="applyUser_select" 
				multiple="multiple" moreselect="true" style="display: none">
			</select>
		</div>
      </div>
      <div class="widget-body no-padding-top no-padding-bottom" id="contentBody" style="overflow-y:auto;position: relative;width:100%;">
         <table class="table table-hover general-table fixTable">
        	<thead>
           		<tr valign="middle">
                   <th style="width: 10%;" valign="middle">选项</th>
					<th valign="middle" class="hidden-phone">客户名称</th>
					<th style="width: 90px" valign="middle">责任人</th>
					<th style="width: 90px" valign="middle" class="no-padding">区域</th>
					<th style="width: 90px" valign="middle">时间</th>
               </tr>
          </thead>
          <tbody id="allTodoBody">
      	  </tbody>
  	   </table> 
  		<div class="panel-body ps-page bg-white padding-top-5" style="font-size: 12px">
			 <p class="pull-left ps-pageText">共<b class="badge" id="totalNum">11</b>条记录</p>
			 <ul class="pagination pull-right" id="pageDiv"></ul>
		</div>
    </div>
    <select id="crmSelect" style="display: none;">
    	
    </select>
</div>
<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>
<script type="text/javascript">

var pageNum = 0;     //页面索引初始值   
var pageSize =8;     //每页显示条数初始化，修改显示条数，修改这里即可 
var selectWay;
var sid = "${param.sid}";
//打开页面body
var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//打开页面的标签
var openPageTag;
//打开页面的标签
var openTabIndex;
//注入父页面信息
function setWindow(winDoc,win){
    openWindowDoc = winDoc;
    openWindow = win;
    openPageTag = openWindow.pageTag;
    openTabIndex = openWindow.tabIndex;
}

function initSelectWay(selectWayObj,preItemOptions){
	selectWay = selectWayObj;
	if(preItemOptions && preItemOptions.length>0){
		$.each(preItemOptions,function(index,optionObj){
			var _option = $("<option></option>");
			_option.val($(optionObj).val())
			_option.html($(optionObj).text())
			$("#crmSelect").append(_option)
		})
	}
	queryListData();
}
//窗体点击事件检测
document.onclick = function(e){
	var evt = e?e:window.event;
	var ele = $(evt.srcElement || evt.target);
	if ($(ele).parents("#moreCondition_Div").length == 1) { 
		if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
			return false;
		}else if($(ele).hasClass("noMoreShow")){
			$("#moreCondition_Div").removeClass("open");
			$("#moreCondition_Div").trigger("hideMoreDiv")
		}else{
			if(!$("#moreCondition_Div").hasClass("open")){
				$(".searchCond").find(".open").removeClass("open");
				$("#moreCondition_Div").addClass("open");
			}
		}
	} else{
		$("#moreCondition_Div").removeClass("open");
	}
}
/**
 * 展示查询条件中更多
 */
function displayMoreCond(divId){
	if($("#"+divId).hasClass("open")){
		$("#"+divId).removeClass("open");
	}else{
		$("#"+divId).addClass("open")
	}
}
$(function(){
	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		queryListData();
	})
	//单个赋值
	$("body").on("click",".setValue",function(){
		var relateElement = $(this).attr("relateElement");
		var dataValue = $(this).attr("dataValue");
		$("#formTempData").find("input[name='"+relateElement+"']").val(dataValue);
		
		var _font = $('<font style="font-weight:bold;"></font>')
		$(_font).html($(this).html())
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html(_font);
		$(this).parents("ul").prev().append(_i);
		
		queryListData();
	})
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		queryListData();
	})
	//文本框绑定回车提交事件
	$("[name='customerName']").bind("keydown",function(event){
        if(event.keyCode == "13") {
        	queryListData();
        }
    });
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		queryListData();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		queryListData();
	})
	//人员单选
	$("body").on("click",".userOneElementSelect",function(){
		var userId = $(this).attr("relateElement");
		var userName = $(this).attr("relateElementName");
		var _this = $(this);
		userOne(userId, userName, null, sid,function(options){
			if(options && options.length>0){
				$.each(options,function(index,object){
					
					var _font = $('<font style="font-weight:bold;"></font>')
					$(_font).html(object.text)
					var _i = $('<i class="fa fa-angle-down"></i>')
					
					$(_this).parents("ul").prev().html(_font);
					$(_this).parents("ul").prev().append(_i);
					
					$("#formTempData").find("input[name='"+userId+"']").val(object.value);
					$("#formTempData").find("input[name='"+userName+"']").val(object.text);
				})
			}else{
				$(_this).parents("form").find("input[name='"+userId+"']").val('');
				$(_this).parents("form").find("input[name='"+userName+"']").val('');
			}
			queryListData();
		});
	});
	//区域单选
	$("body").on("click",".areaOneSelect",function(){
		var _this = $(this);
		var relateElement = $(this).attr("relateElement")
		areaOne(null, null, sid,1,function(result){
			
			var _font = $('<font style="font-weight:bold;"></font>')
			$(_font).html(result.areaName)
			var _i = $('<i class="fa fa-angle-down"></i>')
			
			$(_this).parents("ul").prev().html(_font);
			$(_this).parents("ul").prev().append(_i);
			
			$("#formTempData").find("input[name='"+relateElement+"']").val(result.idAndType);
			
			queryListData();
		})
	});
	$("#moreCondition_Div").bind("hideMoreDiv",function(){
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		
		if(startDate || endDate){
			var _font = $('<font style="font-weight:bold;"></font>')
			$(_font).html("筛选中")
			var _i = $('<i class="fa fa-angle-down"></i>')
			
			$(this).find("a").html(_font);
			$(this).find("a").append(_i);
		}else{
			var _i = $('<i class="fa fa-angle-down"></i>')
			$(this).find("a").html("更多");
			$(this).find("a").append(_i);
		}
	})
})

function queryListData(morePageNum){
	if(morePageNum){
		pageNum = morePageNum;
	}else{
		pageNum = 0;
	}
	var params={"sid":sid,
         	 "pageNum":pageNum,
         	 "pageSize":pageSize	
		}
	$.each($(".searchCond").find("input"),function(){
		var name =  $(this).attr("name");
		var val =  $(this).val();
		if(val){
			params[name]=val;
		}
	})
	$.each($("#formTempData").find("input"),function(index,obj){
		var name =  $(this).attr("name");
		var val =  $(this).val();
		if(val){
			params[name]=val;
		}
	})
	$.each($("#formTempData").find("select"),function(index,obj){
		
		var list =  $(this).attr("list");
		var listkey =  $(this).attr("listkey");
		var listvalue =  $(this).attr("listvalue");
		
		var options = $(this).find("option");
		if(options && options.length>0){
			$.each(options,function(optIndex,option){
				var nameKey = list+"["+optIndex+"]."+listkey;
				var nameValue = list+"["+optIndex+"]."+listvalue;
				params[nameKey]=$(option).val();
				params[nameValue]=$(option).text();
			})
		}
	})
	//取得数据
		getSelfJSON("/crm/ajaxListCrmForSelect",params,function(data){
			if(data.status=='y'){
				var pageBean = data.pageBean;
				$("#totalNum").html(pageBean.totalCount);
               	$("#totalNum").parent().parent().css("display","block")
               	if(pageBean.totalCount<=pageSize){
               		 $("#totalNum").parent().parent().css("display","none")
               	}
          		//分页，PageCount是总条目数，这是必选参数，其它参数都是可选
                 $("#pageDiv").pagination(pageBean.totalCount, {
                     callback: PageCallback,  //PageCallback() 为翻页调用次函数。
                     prev_text: "<<",
                     next_text: ">>",
                     items_per_page:pageSize,
                     num_edge_entries: 0,       //两侧首尾分页条目数
                     num_display_entries: 3,    //连续分页主体部分分页条目数
                     current_page: pageNum,   //当前页索引
                 });
               	 $("#allTodoBody").html('');
               	 if(pageBean.totalCount>0){
               		 constrItemDataTable(pageBean.recordList);
               	 }else{
               		 var _tr = $("<tr></tr>");
               		 var _td = $("<td></td>");
               		 var len = $("#allTodoBody").parents("table").find("thead tr th").length;
               		_td.attr("colspan",len);
               		_td.css("text-align","center");
               		_td.html("未查询到相关数据");
               		
               		_tr.append(_td);
               		$("#allTodoBody").append(_tr);
               		
               	 }
			}
		});
	
	//翻页调用   
    function PageCallback(index, jq) {  
	   	pageNum = index;
	   	queryListData(index);
    } 
	
	function constrItemDataTable(crms){
		if(crms && crms.length>0){
			$.each(crms,function(itemIndex,crm){
				var $tr = $("<tr></tr>");
				var $td0 = $("<td valign='middle' style='padding-left:5px !important;' class='padding-top-5 padding-bottom-5 no-margin'><label class='no-margin-bottom'><input class='colored-blue' name='itemId' type='checkbox'/><span class='text' style='color: inherit;'></span></label></td>");
				if(selectWay == 1){//单选
					$td0.find("input").remove();
					$td0.find("span").before("<input class='colored-blue' name='itemId' type='radio'/>");
				}
				
				var len = $("#crmSelect").find("option[value="+crm.id+"]").length;
				if(len>0){
					$td0.find("input").attr("checked",true);
				}
				
				var $td1 = $("<td valign='middle' style='text-align:left;'>"+crm.customerName+"</td>");
				var $td2 = $("<td valign='middle' style='text-align:left;'>"+crm.ownerName+"</td>");
				var $td3 = $("<td valign='middle' style='text-align:center;'>"+crm.areaName+"</td>");
				var $td4 = $("<td valign='middle' style='text-align:center;'>"+cutString(crm.recordCreateTime,10)+"</td>");
				
				var len = $("#crmSelect").find("option[value='"+crm.id+"']").length;
				if(len>0){
					$td0.find(":checkbox").attr("checked",true);
				}
				
				$tr.append($td0);
				$tr.append($td1);
				$tr.append($td2);
				$tr.append($td3);
				$tr.append($td4);
				
				$tr.attr("id","crm_"+crm.id)
				$tr.attr("title","双击确认")
				
				$("#allTodoBody").append($tr);
				
				
				$tr.data("crmObj",crm)
			})
		}
	}
	
	//操作删除和复选框
	$("body").on("click","tr",function(){
		var crm = $(this).data("crmObj")
		var crmId = crm.id;
		var crmName = crm.customerName;
		var len = $("#crmSelect").find("option[value="+crmId+"]").length;
		
		if(selectWay == 1){//单选
			var radio = $(this).find("input[type=radio]");
			$(radio).attr("checked",true);
			
			$("#crmSelect").html('')
			var _option = $("<option></option>");
			_option.val(crmId)
			_option.html(crmName)
			$("#crmSelect").append(_option)
			
		}else{//多选
			var checkbox = $(this).find("input[type=checkbox]");
			
			
			if(checkbox.attr("checked")){
				$(checkbox).attr("checked",false);
				if(len > 0){
					$("#crmSelect").find("option[value="+crmId+"]").remove()
				}				
			}else{
				$(checkbox).attr("checked",true);
				if(len == 0){
					var _option = $("<option></option>");
					_option.val(crmId)
					_option.html(crmName)
					$("#crmSelect").append(_option)
				}
			}
		}
	});
	//操作删除和复选框
	// $("body").on("dblclick","tr",function(){
	// 	var crm = $(this).data("crmObj")
	// 	crm.crmName = crm.customerName;
	// 	if(selectWay == 1){//单选
	// 		var result = new Array();
	// 		result.push(crm)
	// 		if(result){
	// 			window.top.window[openWindow.name].crmMoreSelectBack(result,"012");
	// 			 closeWin();
	// 		}
	// 	}
	// });
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
}
</script>


</body>
</html>
