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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">

function initListData(listBusMapData){
	
	if(listBusMapData && listBusMapData.length>0){
		$.each(listBusMapData,function(objIndex,busMapObj){
			
			var tr = $("<tr></tr>")
			
			var _tdXh = $("<td></td>");
			_tdXh.css("text-align","center")
			var _lable = $("<lable></lable>");
			var _input = $("<input type='radio' class='colored-blue' name='busMapId'/>");
			var _span = $("<span class='text'>&nbsp;</span>");
			
			_lable.append(_input);
			_lable.append(_span);
			//_input.val(busMapObj.id);
			_input.val(objIndex);
			_tdXh.append(_lable);
			tr.append(_tdXh);
			
			var _tdType =$("<td></td>");
			_tdType.css("text-align","center")
			var busType = busMapObj.busType;
			//busType=="029"?"出差":
			var busTypeName = busType=="029"?"出差申请":busType=="035"?"一般借款":busType=="03401"?"出差汇报":busType=="03402"?"一般说明":busType=="030"?"借款":busType=="03101"?"出差报销":busType=="03102"?"一般报销":busType;
			
			_tdType.html(busTypeName);
			tr.append(_tdType)
			
			var _tdDescribe =$("<td></td>");
			_tdDescribe.html(busMapObj.description);
			tr.append(_tdDescribe)
			
			$("#dataBody").append(tr);
			
			$(tr).data("busMapObj",busMapObj)
			
		})
	}
	
	
	
}
$(function(){
	
	$("body").off("click","tr").on("click","tr",function(){
		var radio = $(this).find("input[type=radio]");
		$(radio).attr("checked","checked")
	});
	
	
	$('tr:even:not(#tr1)').addClass("ramify-bg");//选择所有偶数行，但不包括第一行(标题行)
	
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
});
//返回值
function flowSelected(){
	var result;
	var radio = $("input[type=radio]:checked");
	if(radio.length==0){
		window.top.layer.alert("请先选择！",{icon:7});
	}else{
		result=$(radio).parents("tr").data("busMapObj");
	}
	return result;
}
</script>
<style type="text/css">
.table-content table tr th{height: 30px}
.table-content table tr td{height: 30px}
tr{cursor: pointer;}
.table-page {
    margin: 10px 0px;
}
</style>
</head>
<body>
<div class="widget no-margin bg-white">
	<div class="widget-header bordered-bottom bordered-red detailHead">
	      <i class="widget-icon fa fa-bars txt-red menu-icons"></i>
	      <span class="widget-caption txt-red menu-topic ps-layerTitle">审批流程列表</span>
	      <div class="widget-buttons">
	          <a data-toggle="dispose" href="javascript:void(0)" id="titleCloseBtn">
	              <i class="fa fa-times"></i>
	          </a>
	      </div>
	</div>
       <div class="official-pd margin-top-40" id="contentBody">
              <div class="table-content margin-bottom-10">
                  <table width="100%" cellpadding="0" cellspacing="0"  class="table table-hover general-table" >
                      <thead>
                       <tr role="row">
						<th  style="text-align:center;width:10%">序号</th>
						<th style="text-align:center;width:20%">类型</th>
							<th>描述</th>
						</tr>
					 </thead>
                       <tbody id="dataBody">
                       </tbody>
            </table>
        </div>
	</div>
</div>
</body>
</html>
