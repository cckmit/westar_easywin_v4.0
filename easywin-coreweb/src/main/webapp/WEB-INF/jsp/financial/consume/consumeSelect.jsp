<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<style>

</style>
</head>
<body>
<div class="widget no-margin bg-white">
     	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
			<div class="btn-group pull-left searchCond">
				<div class="table-toolbar ps-margin">
					<div class="pull-left padding-right-40" style="font-size: 18px">
		             	<span class="widget-caption themeprimary ps-layerTitle">记账数据</span>
					</div>
			     </div>
                <div class="table-toolbar ps-margin">
					<div class="btn-group">
						<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
							费用类型筛选
							<i class="fa fa-angle-down"></i>
						</a>
						<ul class="dropdown-menu dropdown-default">
							<li>
								<a href="javascript:void(0);" class="clearMoreValue" relateList="consumType_select">不限条件</a>
							</li>
							<li>
								<a href="javascript:void(0);" class="consumTypeMoreSelect" relateList="consumType_select">类型选择</a>
							</li>
						</ul>
					</div>
				</div>
                
                <div class="table-toolbar ps-margin">
	                  <div class="btn-group cond" id="moreCondition_Div">
	                      <a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
	                      	<c:choose>
	                      			<c:when test="${not empty task.startDate || not empty task.endDate}">
	                      				<font style="font-weight:bold;">筛选中</font>
	                      			</c:when>
	                      			<c:otherwise>
	                         	更多
	                      			</c:otherwise>
	                      	</c:choose>
                              	<i class="fa fa-angle-down"></i>
                              </a>
                              <div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
								<div class="ps-margin ps-search padding-left-10">
									<span class="btn-xs">消费日期：</span>
									<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="consumeStartDate" name="consumeStartDate" placeholder="开始时间"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'consumeEndDate\',{d:-0});}'})" />
									<span>~</span>
									<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="consumeEndDate" name="consumeEndDate" placeholder="结束时间"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'consumeStartDate\',{d:+0});}'})" />
								</div>
                      	<div class="ps-clear padding-top-10" style="text-align: center;">
                      		<button type="botton" class="btn btn-primary btn-xs moreSearchBtn">查询</button>
                      		<button type="button" class="btn btn-default btn-xs margin-left-10 moreClearBtn" onclick="resetMoreCon('moreCondition_Div')">重置</button>
	                	</div>
	                      </div>
	                  </div>
	              </div>
			</div>
			
            <div class="ps-margin ps-search searchCond">
				<span class="input-icon">
				<input name="describe" class="form-control ps-input moreSearch" type="text" placeholder="请输入关键字">
				<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
				</span>
			</div>
			
			<div class="ps-clear" id="formTempData">
				<input type="hidden" name="instanceId">
				<select style="display:none;" list="listConsumeType" listkey="id" listvalue="typeName" id="consumType_select" name="listConsumeType_id" 
					multiple="multiple" moreselect="true">
				</select>
			</div>
								
      </div>
      
      <div id="consumType_selectDiv" class="padding-top-10 text-left moreConsumeTypeListShow" style="display:none">
		<strong>费用类型筛选:</strong>
	</div>
							
      <div class="widget-body" id="contentBody">
         <table class="table table-striped table-hover general-table fixTable">
        	<thead>
	           	<tr>
	                <th style='text-align:center;width:50px;' class='padding-top-5 padding-bottom-5'>
	                	<label>
							<input type="checkbox" class="colored-blue" id="checkAllData" onclick="checkAllData(this,'consumId')">
							<span class="text"></span>
						</label>
	                </th>
	                <th valign="middle">序号</th>
	                <th valign="middle">类型</th>
					<th valign="middle" class="hidden-phone">金额</th>
					<th valign="middle">消费日期</th>
					<th valign="middle" style="text-align: center;">地点</th>
					<th valign="middle">发票数量</th>
               	</tr>
          	</thead>
          	<tbody id="allDataTable">
          	
      	 	</tbody>
  		</table>
  		
  		<div class="panel-body ps-page bg-white" style="font-size: 12px">
			 <p class="pull-left ps-pageText">共<b class="badge" id="totalNum">11</b>条记录</p>
			 <ul class="pagination pull-right" id="pageDiv">
			 </ul>
		</div>
  	 
    </div>
</div>
<script type="text/javascript" src="/static/js/financial/consume/consumForSelect.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>

<!-- 筛选下拉所需 -->
<script src="/static/assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="/static/assets/js/beyond.min.js"></script>

<script type="text/javascript">
	var sid = "${param.sid}";
	var relatetable;
	var seletWay;
	var instanceId;
    //注入父页面信息
    function initData(relatetableParam,seletWayParam,instanceIdParam){
		relatetable = relatetableParam;
		seletWay = seletWayParam;
		instanceId = instanceIdParam;
		$("#formTempData").find("input[name='instanceId']").val(instanceId);
		ConsumeSelectOpt.loadData();
    }
    //关闭窗口
    function closeWin(){
        var winIndex = window.top.layer.getFrameIndex(window.name);
        closeWindow(winIndex);
    }
	$(function(){
		//操作删除和复选框
		$('tr').click(function(){
			var radio = $(this).find("input[type=radio]");
			$(radio).attr("checked","checked");
		});
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		$("body").on("click","#allDataTable tr",function(){
			var checked = $(this).find(":checkbox[name='consumId']").attr('checked');
			var obj =  $(this).data("obj");
			var consumId = obj.id;
			if(checked){
				map[consumId]=obj;
			}else{
				delete map[consumId];
			}
		})
	});
	//窗体点击事件检测
	document.onclick = function(e){
		var evt = e?e:window.event;
		var ele = $(evt.srcElement || evt.target);
		if ($(ele).parents("#moreCondition_Div").length == 1) { 
			if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
				return false;
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
	
	var map = {}; 
	function checkAllData(ckBoxElement, ckBoxName) {
		var checkStatus = $(ckBoxElement).attr('checked');
		var allData = $("#allDataTable").find(":checkBox");
		if (checkStatus) {
			$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', true);
			
			//添加数据
			$.each(allData,function(index,data){
				var obj =  $(this).parents("tr").data("obj");
				var consumId = obj.id;
				map[consumId]=obj;
			})
		} else {
			$(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").attr('checked', false);
			
			//移除数据
			//添加数据
			$.each(allData,function(index,data){
				var obj =  $(this).parents("tr").data("obj");
				var consumId = obj.id;
				delete map[consumId];
			})
			
		}
	}
	//返回结果集合
	function selectResults(){
		var result = new Array();
		$.each(map,function(consumId,obj){
			result.push(obj);
		})
		if(result && result[0]){
			return result;
		}else{
			showNotification(2,"请选择一条数据！");
			return null;
		}
	}
</script>
</body>
</html>
