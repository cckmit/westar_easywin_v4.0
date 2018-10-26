<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" class="bg-white">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
//通过表初始化数据信息
function initTableName(tableName){
	var url = "/form/chooseSysColumn";
	var params = {
			tableName:tableName,
			sid:"${param.sid}",
			t:Math.random()
	}
	postUrl(url,params,function(data){
		if(data.status=='y'){
			constrTable(data.list);
		}
	})
	
	
}

//选择后返回值
function chooseColumn(callback){
	//判断是否有
	var ckBoxs = $(":checkbox[name='compone']:checked");
	if (ckBoxs && ckBoxs.get(0)) {
		var results = new Array();
		var flag = true;
		$.each(ckBoxs,function(){
			var tr = $(this).parents("tr");
			var obj = $(tr).data("obj");
			
			var componeType = $(tr).find("select").val();
			if(componeType){
				var result = {};
				result.title=obj.columnDesc;
				result.column=obj.columnName;
				result.type=componeType;
				results.push(result);
			}else{
				scrollToLocation($("#contentBody"),$(tr).find("select"));
				layer.tips("请选择控件类型！",$(tr).find("select"),{tips:1});
				flag = false;
				return false;
			}
		})
		if(flag){
			callback(results);
		}
	}else{
		showNotification(2,"请选择需要映射的字段");
	}
}


$(function(){
	//操作删除和复选框
	$('tr').click(function(){
		var radio = $(this).find("input[type=radio]");
		$(radio).attr("checked","checked")
	});
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");


});



function constrTable(list){
	if(list && list[0]){
		var compones = new Array();
		compones.push({"componeType":"Text","componeName":"文本框"});
		compones.push({"componeType":"TextArea","componeName":"多行文本"});
		//compones.push({"componeType":"RadioBox","componeName":"单选框"});
		//compones.push({"componeType":"CheckBox","componeName":"复选框"});
		compones.push({"componeType":"Select","componeName":"下拉菜单"});
		compones.push({"componeType":"DateComponent","componeName":"日期"});
		compones.push({"componeType":"NumberComponent","componeName":"数字输入框"});
		
		
		//类型
		var typeTdMod = $('<td></td>');
		var select = $('<select></select>');
		$(select).append('<option value="">类型选择</option>');
		$.each(compones,function(index,obj){
			var option= $('<option></option>');
			$(option).attr("value",obj.componeType);			
			$(option).html(obj.componeName);			
			$(select).append($(option));
		})
		$(typeTdMod).append($(select));
		
		$.each(list,function(index,obj){
			var tr = $('<tr></tr>');
			$(tr).data("obj",obj);
			//序号
			var checkTd = $('<td style="text-align: center;line-height:2em;"></td>');
			var lable = $('<label></lable>')
			var checkBox =$('<input name="compone" class="colored-blue" type="checkbox" /><span class="text">&nbsp;</span>');
			$(lable).append($(checkBox));
			$(checkTd).append($(lable));
			
			$(tr).append($(checkTd));
			//标题
			var titleTd = $('<td></td>');
			$(titleTd).append(obj.columnDesc);
			$(tr).append($(titleTd));
			//类型
			var typeTd = $(typeTdMod).clone();
			$(tr).append($(typeTd));
			$("#allDataBody").append($(tr));
		})
	}
}
</script>
<style>
.table {
	margin-bottom: 0px;
}

.table tbody>tr>td {
	padding: 5px 0px;
}

tr {
	cursor: auto;
}
.optionFill{
	cursor:pointer;
	font-size: 13px !important;
	float:left;
	margin-left: 40px;
	border-style:none;
	background:white;
}
input[fillState]{
	font-size: 20px !important;
}
.optionFill:hover{
	background: #F9F9F9;
}
</style>
</head>
<body>

<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">表单控件候选列表</span>
                    <!-- <div class="widget-buttons">
						<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div> -->
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
				
					<div class="widget no-margin bg-white">
						<div class="widget-body">
							<table id="formComponTable" class="table table-striped table-hover general-table">
								<thead>
									<tr>
										<th style="width: 10%;" valign="middle">
											<label>
												<input type="checkbox"
													class="colored-blue" id="checkAllBox"
													onclick="checkAll(this,'compone')"> <span
													class="text" style="color: inherit;"></span>
											</label>
										</th>
										<th valign="middle" class="hidden-phone">标题</th>
										<th style="width: 30%;" valign="middle">类型</th>
									</tr>
								</thead>
								<tbody id="allDataBody">
									
								</tbody>
							</table>
						</div>
					</div>
	
				</div>
			</div>
		</div>
	</div>
</div>

	
	<!-- 筛选下拉所需 -->
	<script src="/static/assets/js/bootstrap.min.js"></script>
	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>

</body>
</html>
