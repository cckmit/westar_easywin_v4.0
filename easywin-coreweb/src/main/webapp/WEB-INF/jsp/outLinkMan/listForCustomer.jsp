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
		             	<span class="widget-caption themeprimary ps-layerTitle">外部联系人列表</span>
					</div>
			     </div>
			     
				                                    
			</div>
            <div class="ps-margin ps-search searchCond">
				<span class="input-icon">
					<input name="linkManName" id="linkManName" value="" class="form-control ps-input formElementSearch" type="text" placeholder="请输入关键字" >
					<a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
				</span>
			</div>
			<div class="widget-buttons ps-widget-buttons">
				<button class="btn btn-info btn-primary btn-xs" type="button" onclick="addOutLinMan('${param.sid}');">
					<i class="fa fa-plus"></i> 外部联系人
				</button>
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
        <table class="table table-hover general-table fixTable" style="text-align: left;" id="olmTable" >
        	<thead>
           		<tr>
                    <th style="text-align: left;width: 10%" >选项</th>
					<th style="text-align: left;width: 27%" >联系人名称</th>
					<th style="text-align: left;width: 28%" >职务</th>
					<th style="text-align: left;width: 35%" >联系方式</th>
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
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
//选择项目出发
function crmSelected(){
	
	var result;
	var options = $("#crmSelect").find("option");
	if(options.length==0){
		window.top.layer.alert("请选择外部联系人信息",{icon:7});
	}else{
		result = new Array();
		$.each(options,function(index,option){
			var crm = $(option).val();
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


//选择外部联系人弹框
function addOutLinMan(sid){
	
	//是否需要数据保持
	
	window.top.layer.open({
		 type: 2,
		 title:false,
		 closeBtn:0,
		 area: ['800px', '600px'],
		 fix: true, //不固定
		 maxmin: false,
		 move: false,
		 scrollbar:false,
		 content: ['/outLinkMan/addOutLinkManPage?sid='+sid+"&isCrm=1",'no'],
		
		 yes: function(index, layero){
			 
		 }
		,btn2: function(){
			 
		}
	    ,cancel: function(){ 
	    	//右上角关闭回调
	    },success:function(layero,index){
		  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.setWindow(window.document,window);
		  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			  window.top.layer.close(index);
		  });
		  $(iframeWin.document).find("#addOutLinkMan").on("click",function(){
			  if($(iframeWin.document).find("#linkManName").val()){
				  	setTimeout(function(){//200毫秒后关闭当前页面
				  		 window.top.layer.close(index);
						 showNotification(1,"添加成功");
						 queryListData();
					  },200);
			  }
		  });
		

	    }
	});
}

//编辑外部联系人弹框
function viewOlm(id){
	
	//是否需要数据保持
	
	window.top.layer.open({
		 type: 2,
		 title:false,
		 closeBtn:0,
		 area: ['800px', '600px'],
		 fix: true, //不固定
		 maxmin: false,
		 move: false,
		 scrollbar:false,
		 content: ['/outLinkMan/viewOlmPage?sid=' + '${param.sid}' + '&id=' + id+"&isCrm=1",'no'],
		
		 yes: function(index, layero){
			 
		 }
		,btn2: function(){
			 
		}
	    ,cancel: function(){ 
	    	//右上角关闭回调
	    },success:function(layero,index){
		  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.setWindow(window.document,window);
		  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
			  window.top.layer.close(index);
		  });
		  $(iframeWin.document).find("#editOutLinkMan").on("click",function(){
			  if($(iframeWin.document).find("#linkManName").val()){
				  	setTimeout(function(){//200毫秒后关闭当前页面
				  		 window.top.layer.close(index);
						 showNotification(1,"修改成功");
						 queryListData();
					  },200);
			  }
		  });
		

	    }
	});
}

//合并行数据
function olmFormRowSpan(table){
	var trs = $(table).find("tbody").find("tr");
	if(trs && trs.get(0)){
		var preOlmId = 0;
		var rowSpan = 1;
		$.each(trs,function(index,trObj){
			var olmId = $(trObj).attr("olmId");
			if(olmId == preOlmId){
				rowSpan = rowSpan+1;
				//开始合并
				var firstTr = $(table).find("tbody").find("tr[olmId='"+olmId+"']:eq(0)");
				$(firstTr).find("td:eq(0)").attr("rowspan",rowSpan);
				$(firstTr).find("td:eq(1)").attr("rowspan",rowSpan);
				$(firstTr).find("td:eq(2)").attr("rowspan",rowSpan);
				
				var td0 = $(trObj).find("td:eq(0)");
				var td1 = $(trObj).find("td:eq(1)");
				var td2 = $(trObj).find("td:eq(2)");
				
				$(td0).remove();
				$(td1).remove();
				$(td2).remove();
			}else{
				preOlmId = olmId;
				rowSpan = 1;
			}
		});
		
	}
}

var pageNum = 0;     //页面索引初始值   
var pageSize =10;     //每页显示条数初始化，修改显示条数，修改这里即可 
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

$(function(){
	
	//客户名筛选
		$("#linkManName").blur(function(){
			queryListData();
		});
		//文本框绑定回车提交事件
		$("#linkManName").bind("keydown",function(event){
	        if(event.keyCode == "13")    
	        {
	        	if(!strIsNull($("#linkManName").val())){
	        		queryListData();
	        	}else{
	        		showNotification(1,"请输入检索内容！");
	    			$("#linkManName").focus();
	        	}
	        }
	    });
		
		//操作删除和复选框
		$("body").on("click","tr",function(e){
			if(e.target.tagName != 'A'){
				var crm = $(this).data("crmObj")
				var t = JSON.stringify(crm);
				var crmId = crm.id;
				var len = $("#crmSelect").find("option[value='"+t+"']").length;
				
				if(selectWay == 1){//单选
					var radio = $(this).find("input[type=radio]");
					$(radio).attr("checked",true);
					
					$("#crmSelect").html('')
					var _option = $("<option></option>");
					_option.val(crm)
					$("#crmSelect").append(_option)
					
				}else{//多选
					var checkbox = $(this).find("input[type=checkbox]");
					
					
					if(checkbox.attr("checked")){
						$(checkbox).attr("checked",false);
						if(len > 0){
							$("#crmSelect").find("option[value='"+t+"']").remove()
						}				
					}else{
						$(checkbox).attr("checked",true);
						if(len == 0){
							var _option = $("<option></option>");
							_option.val(t)
							$("#crmSelect").append(_option)
						}
					}
				}
			}
		});
		
	
	
})


function queryListData(morePageNum){
	if(morePageNum){
		pageNum = morePageNum;
	}else{
		pageNum = 0;
	}
	var params={"sid":sid,
         	 "pageNum":pageNum,
         	 "pageSize":pageSize,
         	 "linkManName":$("#linkManName").val()
		}
	
		//取得数据
		getSelfJSON("/outLinkMan/ajaxListForSelect",params,function(data){
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
				var $tr = $('<tr olmId="'+crm.id+'"></tr>');
				var $td0 = $("<td ><label class='no-margin-bottom'><input class='colored-blue' name='itemId' type='checkbox'/><span class='text' style='color: inherit;'></span></label></td>");
				var len = $("#crmSelect").find("option[value="+crm.id+"]").length;
				if(len>0){
					$td0.find("input").attr("checked",true);
				}
				
				var $td1 = $("<td >"+'<a href="javascript:void(0);" onclick="viewOlm('+crm.id +');">'+crm.linkManName+'</a>'+"</td>");
				var $td2 = $("<td >"+(crm.post==null?"--":crm.post)+"</td>");
				var $td3 = $("<td >"+(crm.contactWayValue==null?"--":(crm.contactWayValue+"："+crm.contactWay))+"</td>");
				
				var len = $("#crmSelect").find("option[value='"+crm.id+"']").length;
				if(len>0){
					$td0.find(":checkbox").attr("checked",true);
				}
				
				$tr.append($td0);
				$tr.append($td1);
				$tr.append($td2);
				$tr.append($td3);
				
				$tr.attr("id","crm_"+crm.id)
				
				$("#allTodoBody").append($tr);
				$tr.data("crmObj",crm)
			})
		}
	}
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	setTimeout(function() {
		olmFormRowSpan($("#olmTable"));
	}, 200);
	
}

//删除外部联系人
function delOlm(olmId) {
	window.top.layer.confirm('确定要删除该联系人吗？', {
		icon: 3,
		title: '提示'
	},
	function(index) {
		postUrl('/outLinkMan/delOlm', {
			sid: '${param.sid}',
			olmId: olmId
		},
		function(data) {
			if (data.status == 'y') {
				showNotification(1, "删除成功！");
				window.location.reload()
			} else {
				showNotification(2, data.info);
			}
		});
	});
}
</script>


</body>
</html>
