<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

</head>
<body>
	<div class="page-content">
		<div class="page-body">

			<div class="row">
				<div class="col-md-12 col-xs-12 ">
				
					<div class="widget">
						<ul class="nav nav-tabs tabs-flat" id="myTab11" style="padding-top:0;background-color: #FBFBFB;">
                             <li class="active" id="loanOffMenuLi">
                                 <a data-toggle="tab" href="javascript:void(0)">报销审核<span style="color:red;font-weight:bold;" id="countLoanOff"></span></a>
                             </li>
                             <li id="loanMenuLi">
                                 <a data-toggle="tab" href="javascript:void(0)">借款审核<span style="color:red;font-weight:bold;" id="countLoan"></span></a>
                             </li>
                    	</ul>
						
						<div class="widget-body" style="text-align:center;padding-top:5px;overflow: hidden;overflow-y:auto;width:100%;box-shadow: none;" id="contentBody">
							<iframe id="otherAttrIframe" style="display: block;" class="layui-layer-load"
							src="/financial/listLoanOffsPage?sid=${param.sid}&pager.pageSize=10&balanceState=3"
							border="0" frameborder="0" allowTransparency="true"
							noResize scrolling="no" width=100% height=100% vspale="0"></iframe>
						</div>
					</div>
				</div>
			</div>

		</div>

	</div>
</body>

<script type="text/javascript">

$(function(){
	
	queryCounts();
	
	//报销
	$("#loanOffMenuLi").click(function(){
		$("#otherAttrIframe").css("display","block");
		$(this).parent().find("li").removeAttr("class");;
		$("#loanOffMenuLi").attr("class","active");
		$("#otherAttrIframe").attr("src","/financial/listLoanOffsPage?sid=${param.sid}&pager.pageSize=10&balanceState=3");
	});
	//借款
	$("#loanMenuLi").click(function(){
		$("#otherAttrIframe").css("display","block");
		$(this).parent().find("li").removeAttr("class");;
		$("#loanMenuLi").attr("class","active");
		$("#otherAttrIframe").attr("src","/financial/listLoansPage?sid=${param.sid}&pager.pageSize=10&balanceState=3");
	});

	
})

 //显示各模块数量
      function queryCounts() {
      	$.ajax({
      		type : "post",
      		url : "/financial/queryOfficesCounts?sid="+'${param.sid}',
      		dataType:"json",
      		traditional :true, 
      		data:null,
      		  beforeSend: function(XMLHttpRequest){
               },
      		  success : function(data){
      			  if(data.status == "y"){
      				  if(data.countLoanOff > 0){
      					  $("#countLoanOff").text("（"+data.countLoanOff+"）");
      					  $("#countLoanOff").show();				  
      				  }else{
      					 $("#countLoanOff").hide();
      				  }
      				  if(data.countLoan > 0){
      					  $("#countLoan").text("（"+data.countLoan+"）");
      					  $("#countLoan").show();
      				  }else{
      					  $("#countLoan").hide();
      				  }
      			  }
      		  },
      		  error:  function(XMLHttpRequest, textStatus, errorThrown){
      			  window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
      	      }
      	}); 
      }

</script>
</html>

