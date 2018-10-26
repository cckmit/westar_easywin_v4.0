<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	
	$(function(){
		var url = "/userInfo/queryTopLeader";
		var param = {
				sid:"${param.sid}"
		}
		postUrl(url,param,function(data){
			//出差报销
			if(data.topLeaderState == 'y'){
				$("body").find("table").find("tr:eq(0)").remove();
				$("body").find("table").find("tr:eq(0)").prepend('<td style="width: 25%">出差报销</td>');
			}else{
				$("body").find("table").find("tr:eq(0)").find("td").attr("rowspan","1");
				$("body").find("table").find("tr:eq(1)").remove();
				
			}
			
			if(data.topLeaderState == 'y'){
				$("body").find("table").find("tr:eq(1)").remove();
				$("body").find("table").find("tr:eq(1)").prepend('<td style="width: 25%">常规报销</td>');
			}
		})
	})
</script>
</head>
<body>
	<div class="container no-padding" style="width: 100%">	
		<div class="row" >
			<div class="col-lg-12 col-sm-12 col-xs-12" >
            	<div class="widget" style="margin-top: 0px;">
	            	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	                	<span class="widget-caption themeprimary" style="font-size: 20px">报销方式选择</span>
	                       <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
	                   </div><!--Widget Header-->
                   
            		<div class="widget-body margin-top-40" style="height: 360px;overflow-y:auto;position: relative;">
                     		<table class="table table-bordered" style="font-size: 16px">
                     			<tr>
                     				<td rowspan="2" style="width: 25%">
                     					出差报销
                     				</td>
                     				<td>
                     					<a href="javascript:void(0)" class="btn btn-info btn-primary btn-sm margin-left-5" tripway tripState="1" needReport="1">出差报销</a>
                     				</td>
                     			</tr>
                     			<tr>
                     				<td>
                     					<a href="javascript:void(0)" class="btn btn-info btn-primary btn-sm margin-left-5" tripway tripState="1" needReport="0">直接出差报销</a>
                     				</td>
                     			</tr>
                     			<tr>
                     				<td rowspan="2">
                     				常规报销
                     				</td>
                     				<td>
                     					<a href="javascript:void(0)" class="btn btn-info btn-primary btn-sm margin-left-5" tripway tripState="0" needReport="1">招待活动报销</a>
                     					
                     				</td>
                     			</tr>
                     			<!-- 
	                     			<tr>
	                     				<td>
	                     					<a href="javascript:void(0)" tripway tripState="0" needReport="0" applyState="1">根据借款报销</a>
	                     				</td>
	                     			</tr>
                     			 -->
                     			<tr>
                     				<td>
                     					<a href="javascript:void(0)" class="btn btn-info btn-primary btn-sm margin-left-5" tripway tripState="0" needReport="0" applyState="0">直接常规报销</a>
                     				</td>
                     			</tr>
                     		</table>
					</div>
					
            	</div>
            </div>
        </div>
     </div>

</body>
</html>
