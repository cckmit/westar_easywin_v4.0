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
<script type="text/javascript">
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
//选择项目出发
function modifySelected(){
	var result;
	var options = $("#modifySelect").find("option");
	if(options.length==0){
		window.top.layer.alert("请选择变更管理信息",{icon:7});
	}else{
		result = new Array();
		$.each(options,function(index,option){
			var modifyPm = new Object();
			modifyPm.id=$(option).val();
			modifyPm.modifyId=$(option).text();
			result.push(modifyPm)
		})
	}
	return result;
}

function modifySelectedV2(ts){
    var modifyPm = $(ts).data("modifyPmObj");
    var modifyId = modifyPm.id;
    var modifySerialId = modifyPm.modifyId;
    var len = $("#modifySelect").find("option[value="+modifyId+"]").length;

	var radio = $(ts).find("input[type=radio]");
	$(radio).attr("checked",true);

	$("#modifySelect").html('')
	var _option = $("<option></option>");
	_option.val(modifyId)
	_option.html(modifySerialId)
	$("#modifySelect").append(_option)

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
		             	<span class="widget-caption themeprimary ps-layerTitle">变更管理过程列表</span>
					</div>
			     </div>
			     <div class="table-toolbar ps-margin">
			         <div class="btn-group">
			             <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="创建人">
			             	创建人<i class="fa fa-angle-down"></i>
			            </a>
			             <ul class="dropdown-menu dropdown-default">
			                <li><a href="javascript:void(0)" class="clearValue" relateElement="owner" relateElementName="ownerName">不限条件</a>
								</li>
								<li><a href="javascript:void(0)" class="userOneElementSelect" relateElement="owner" relateElementName="ownerName">人员选择</a>
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
								<span class="btn-xs">起止日期：</span>
							                              	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${item.startDate}" id="startDate" name="startDate" 
							placeholder="开始日期"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
							<span>~</span>
							<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${item.endDate}" id="endDate"  name="endDate"
							placeholder="结束日期" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})"/>
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
				<input  name="flowName" class="form-control ps-input moreSearch" type="text" placeholder="请输入关键字">
				<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
				</span>
			</div>
		
		<div class="ps-clear" id="formTempData">
			<input type="hidden" id="owner" name="owner"/>
			 <input type="hidden" id="ownerName" name="ownerName"/>
			 
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
					<th valign="middle" class="hidden-phone">变更编号</th>
					<th valign="middle" class="hidden-phone">变更流程</th>
					<th style="width: 90px" valign="middle">创建人</th>
					<th style="width: 90px" valign="middle">创建日期</th>
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
    <select id="modifySelect" style="display: none;">
    	
    </select>
</div>
<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
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
			$("#itemSelect").append(_option)
		})
	}
	queryListData();
}
//窗体点击事件检测
document.onclick = function(e){
	var evt = e?e:window.modify;
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
		if(relateElement==='owner'){
			$("#applyUser_select").html('');
		}
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
	//查询日期
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		queryListData();
	})
	//查询日期
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		queryListData();
	})
	//查询日期
	$("body").on("blur",".moreSearch",function(){
		queryListData();
	})
	//文本框绑定回车提交事件
	$("[name='itemName']").bind("keydown",function(modify){
        if(modify.keyCode == "13") {
        	queryListData();
        }
    });
	//人员单选
	$("body").on("click",".userOneElementSelect",function(){
		var userId = $(this).attr("relateElement");
		var userName = $(this).attr("relateElementName");
		var _this = $(this);
		userOne(userId, userName, null, sid,function(options){
			$("#applyUser_select").html('');
			if(options && options.length>0){
				$.each(options,function(index,object){
					
					var _font = $('<font style="font-weight:bold;"></font>')
					$(_font).html(object.text)
					var _i = $('<i class="fa fa-angle-down"></i>')
					
					$(_this).parents("ul").prev().html(_font);
					$(_this).parents("ul").prev().append(_i);
					
					$("#formTempData").find("input[name='"+userId+"']").val(object.value);
					$("#formTempData").find("input[name='"+userName+"']").val(object.text);
					
					var _option = $("<option selected='selected'></option>")
					$(_option).val(object.value);
					$(_option).html(object.text);
					$("#applyUser_select").append(_option);
				})
			}else{
				$(_this).parents("form").find("input[name='"+userId+"']").val('');
				$(_this).parents("form").find("input[name='"+userName+"']").val('');
			}
			queryListData();
		});
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
		getSelfJSON("/iTOm/modifyPm/ajaxListModifyForSelect",params,function(data){
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
               		 constrDataTable(pageBean.recordList);
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
	
	function constrDataTable(modifyPms){
		if(modifyPms && modifyPms.length>0){
			$.each(modifyPms,function(index,modifyPm){
				var $tr = $("<tr ></tr>");
				var $td0 = $("<td valign='middle' style='padding-left:5px !important;' class='padding-top-5 padding-bottom-5 no-margin'><label class='no-margin-bottom'><input class='colored-blue' name='itemId' type='checkbox'/><span class='text' style='color: inherit;'></span></label></td>");
				if(selectWay == 1){//单选
					$td0.find("input").remove();
					$td0.find("span").before("<input class='colored-blue' name='modifyId' type='radio'/>");
				}
				
				var len = $("#modifySelect").find("option[value="+modifyPm.id+"]").length;
				if(len>0){
					$td0.find("input").attr("checked",true);
				}
				
				var $td1 = $("<td valign='middle' style='text-align:left;'>"+modifyPm.modifyId+"</td>");
				var $td2 = $("<td valign='middle' style='text-align:left;'>"+modifyPm.flowName+"</td>");
				var $td3 = $("<td valign='middle' style='text-align:left;'>"+modifyPm.creatorName+"</td>");
				var $td4 = $("<td valign='middle' style='text-align:center;'>"+cutString(modifyPm.recordCreateTime,10)+"</td>");
				
				var len = $("#modifySelect").find("option[value='"+modifyPm.id+"']").length;
				if(len>0){
					$td0.find(":checkbox").attr("checked",true);
				}
				
				$tr.append($td0);
				$tr.append($td1);
				$tr.append($td2);
				$tr.append($td3);
				$tr.append($td4);
				
				$tr.attr("id","modifyPm_"+modifyPm.id)
				$tr.attr("title","双击确认")
				$("#allTodoBody").append($tr);
				
				
				$tr.data("modifyPmObj",modifyPm)
			})
		}
	}

	//操作删除和复选框
	$("body").on("click","tr",function(){
		var modifyPm = $(this).data("modifyPmObj")
		var modifyId = modifyPm.id;
		var modifySerialId = modifyPm.modifyId;
		var len = $("#modifySelect").find("option[value="+modifyId+"]").length;
		
		if(selectWay == 1){//单选
			var radio = $(this).find("input[type=radio]");
			$(radio).attr("checked",true);
			
			$("#modifySelect").html('')
			var _option = $("<option></option>");
			_option.val(modifyId)
			_option.html(modifySerialId)
			$("#modifySelect").append(_option)
			
		}else{//多选
			var checkbox = $(this).find("input[type=checkbox]");
			
			
			if(checkbox.attr("checked")){
				$(checkbox).attr("checked",false);
				if(len > 0){
					$("#modifySelect").find("option[value="+modifyId+"]").remove()
				}				
			}else{
				$(checkbox).attr("checked",true);
				if(len == 0){
					var _option = $("<option></option>");
					_option.val(modifyId)
					_option.html(modifySerialId)
					$("#modifySelect").append(_option)
				}
			}
		}
	});
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");

    
}
</script>


</body>
</html>
