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

//改变填充选项的值和onclick
function resetOptionOnclick(){
    $.each($("input[name='formComponId']"),function(){
        var id = $(this).attr("id");
        if($(this).is(":checked")){
            $("#li-optionFill-"+id+"").attr("onclick","changeOption("+id+")");
        }else{
            $("#li-optionFill-"+id+"").attr("onclick","");
            $("#li-optionFill-"+id+"").val("×");

            $("#li-optionFill-"+id+"").attr("fillstate","0");
            $("#li-optionFill-"+id+"").css("color","red");
        }
    });
}

$(function(){
    resetOptionOnclick();
	//操作删除和复选框
	$('tr').click(function(){
		var radio = $(this).find("input[type=radio]");
		$(radio).attr("checked","checked")
	});


	//子表单权限统一
	$("input[name='formComponId']").on("click",function(){
		var subFormId = $(this).attr("subFormId");
		if(subFormId>0){
			var checked = $(this).attr("checked");
			if(checked){//选中了
				$("input[subFormId='"+subFormId+"']").attr("checked","checked");
			}else{
				$("input[subFormId='"+subFormId+"']").removeAttr("checked");
			}
		}
	})

	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");


});
//选择表单控件触发
function formComponSelected(){
    var formCompons = new Array();
    if($("input[name='formComponId']:checked").length==0){
        window.top.layer.alert("请选择授权表单控件！",{icon:7});
    }else{
        $("[name='formComponId']:checked").each(function(){
            var isFill = 0;
            var id = $(this).attr("id");
            var val = $("#li-optionFill-"+id+"").val()
            if(!strIsNull(val)){
                if(val=="×"){
                    isFill = 0;
                }else if(val=="✔"){
                    isFill = 1;
                }
            }else{
                isFill = -1;
            }
            formCompons.push({"formComponId":$(this).attr("formComponId"),"formComponName":$(this).attr("formComponName"),"option":isFill});
        });
    }
    return formCompons;
}
function changeOption(index){
    var val = $("#li-optionFill-"+index+"").val();
    if(val.trim()=="×"){
        $("#li-optionFill-"+index+"").val("✔")
        $("#li-optionFill-"+index+"").attr("fillstate","1");
        $("#li-optionFill-"+index+"").css("color","green");
    }else{
        $("#li-optionFill-"+index+"").val("×")
        $("#li-optionFill-"+index+"").attr("fillstate","0");
        $("#li-optionFill-"+index+"").css("color","red");
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
													onclick="checkAll(this,'formComponId');resetOptionOnclick();"> <span
													class="text" style="color: inherit;"></span>
											</label>
										</th>
										<th style="width: 20%;" valign="middle" class="hidden-phone">表单控件</th>
										<th style="width: 10%;" valign="middle">类型</th>
										<th style="width: 110px;" valign="middle"> 是否填充意见</th>
										<th style="width: 90px;" valign="middle">创建于</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${not empty formComponlist}">
											<c:forEach items="${formComponlist}" var="formComponVo" varStatus="vs">
												<tr name="formComponTableTr">
													<td style="text-align: center;line-height:2em;">
														<label name="formComponCheckBos">
															<input onclick="resetOptionOnclick();" id="${vs.index}" class="colored-blue" type="checkbox" name="formComponId" subFormId = '${formComponVo.subFormId>0?formComponVo.subFormId:0}'
																${1==formComponVo.checked?'checked="checked"':'' }
																   value="${formComponVo.fieldId}" formComponId="${formComponVo.fieldId}"
																   formComponName="${formComponVo.title}" />
															<span class="text">&nbsp;</span>
														</label>
													</td>
													<td><tags:cutString num="26">${formComponVo.title}</tags:cutString>
													<c:if test="${formComponVo.subFormId>0}">(子表单${formComponVo.subFormIndex})</c:if>
													</td>
													<td>
														${formComponVo.componentKey}

													</td>
													<td>
														<c:choose>
															<c:when test="${formComponVo.componentKey eq 'TextArea' }">
																<c:choose>
																	<c:when test="${formComponVo.isFill=='1'}">
																		<input name="option" type="button" class="optionFill" style="font-size:30px;color: green" fillState="1"
																			   id="li-optionFill-${vs.index}"  onclick="changeOption(${vs.index})"  value="✔" >
																	</c:when>
																	<c:otherwise>
																		<input name="option" type="button" class="optionFill" style="font-size:30px;color: red" fillState="0"
																			   id="li-optionFill-${vs.index}" onclick="changeOption(${vs.index})" value="×" >
																	</c:otherwise>
																</c:choose>
															</c:when>
															<c:otherwise>
																<input name="option" type="button" class="optionFill" style="margin-left:25px;color:#444;" readonly="readonly" value="不可用"/>
															</c:otherwise>
														</c:choose>
													</td>
													<td>${fn:substring(formComponVo.recordCreateTime,0,10) }</td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td height="30" colspan="6" align="center"><h3>没有可用表单控件！</h3></td>
											</tr>
										</c:otherwise>
									</c:choose>
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
