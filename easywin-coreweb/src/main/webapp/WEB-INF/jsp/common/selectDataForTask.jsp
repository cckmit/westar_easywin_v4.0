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

//注入父页面信息
function initData(showDatas,spId){
	if(showDatas && showDatas[0]){
		$.each(showDatas,function(index,showData){
			var tr = $('<tr></tr>');
			var checkTd = $('<td style="text-align: center;line-height:2em;"></td>');
			var checkLable = $('<label></label>');
			var checkInput = $('<input class="colored-blue" type="checkbox" name="formComponId" />');
			var checkText = $('<span class="text">&nbsp;</span>');
			$(checkLable).append(checkInput);
			$(checkLable).append(checkText);
			$(checkTd).append(checkLable);
			$(tr).append(checkTd);
			
			var titleTd = $('<td></td>');
			$(titleTd).html(showData.title);
			$(tr).append(titleTd);
			var contentTd = $('<td class="padding-left-10"></td>');
			$(contentTd).html(showData.content);
			$(tr).append(contentTd);
			
			$(tr).data("showData",showData);
			$("#allDataList").append($(tr));
		})
	}
	if(spId){
		//获取审批附件
		$.ajax({
			type : "post",
			url : "/workFlow/selectSpFiles?sid="+'${param.sid}',
			dataType:"json",
			traditional :true, 
			data:{spId:spId
			},
			  beforeSend: function(XMLHttpRequest){
	         },
			  success : function(data){
				  
				  if(data && data[0]){
					    var tr = $('<tr></tr>');
						var checkTd = $('<td style="line-height:2em;font-weight:600;font-size: 13px;" colspan="3">&nbsp;审批附件:</td>');
						$(tr).append(checkTd);
						$("#allDataList").append($(tr));
					  for (var i = 0; i < data.length; i++) {
						  var tr = $('<tr></tr>');
							var checkTd = $('<td style="text-align: center;line-height:2em;"></td>');
							var checkLable = $('<label></label>');
							var checkInput = $('<input class="colored-blue" type="checkbox" name="formComponId" />');
							var checkText = $('<span class="text">&nbsp;</span>');
							$(checkLable).append(checkInput);
							$(checkLable).append(checkText);
							$(checkTd).append(checkLable);
							$(tr).append(checkTd);
							
							var contentTd = $('<td colspan="2"></td>');
							$(contentTd).html(data[i].filename);
							$(tr).append(contentTd);
							
							$(tr).data("showData",data[i]);
							$("#allDataList").append($(tr));
						}
					  
						
					}
				  
			  },
			  error:  function(XMLHttpRequest, textStatus, errorThrown){
				  window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
		      }
		});
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
//选择表单控件触发
function formComponSelected(){
    var content = "";
    if($("input[name='formComponId']:checked").length>0){
        $("[name='formComponId']:checked").each(function(){
        	var showData = $(this).parents("tr").data("showData");
        	if(showData.title){
        		var subContent = showData.title+"："+showData.content;
            	if(content){
    	        	content = content+"<br>"+subContent;
            	}else{
    	        	content = subContent;
            	}
        	}
        	
        });
    }
    return content;
}

//选择表单控件触发
function formFileSelected(){
    var content = [];
    if($("input[name='formComponId']:checked").length>0){
        $("[name='formComponId']:checked").each(function(){
        	var showData = $(this).parents("tr").data("showData");
        	if(showData.filename){
        		content.push(showData)
        	}
        	
        });
    }
    return content;
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
	             	<span class="widget-caption themeprimary ps-layerTitle">数据关联</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
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
													onclick="checkAll(this,'formComponId')"> <span
													class="text" style="color: inherit;"></span>
											</label>
										</th>
										<th style="width: 30%;" valign="middle" class="hidden-phone">标题</th>
										<th valign="middle">内容</th>
									</tr>
								</thead>
								<tbody id="allDataList">
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
