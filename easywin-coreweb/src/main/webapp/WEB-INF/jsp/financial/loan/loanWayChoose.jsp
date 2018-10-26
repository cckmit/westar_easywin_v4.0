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
			//出差借款
			var _tripA = $('<a href="javascript:void(0)" class="btn btn-info btn-primary btn-sm margin-left-5" tripway tripState="1"></a>');
			if(data.topLeaderState == 'y'){
				$(_tripA).attr("needApply","0");
				$(_tripA).html("直接出差借款");
			}else{
				$(_tripA).attr("needApply","1");
				$(_tripA).html("出差申请借款");
			}
			$("body").find("table").find("tr:eq(0)").find("td:eq(1)").html($(_tripA));
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
	                	<span class="widget-caption themeprimary" style="font-size: 20px">借款方式选择</span>
	                       <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
	                   </div><!--Widget Header-->
                   
            		<div class="widget-body margin-top-40" style="height: 360px;overflow-y:auto;position: relative;">
                     		<table class="table table-bordered" style="font-size: 16px">
                     			<tr>
                     				<td style="width: 25%">
                     					出差借款
                     				</td>
                     				<td>
                     					
                     				</td>
                     			</tr>
                     			<tr>
                     				<td rowspan="2">
                     				常规借款
                     				</td>
                     				<td>
                     					<a href="javascript:void(0)" class="btn btn-info btn-primary btn-sm margin-left-5" tripway tripState="0" needApply="1">招待活动借款</a>
                     				</td>
                     			</tr>
                     			<tr>
                     				<td>
                     					<a href="javascript:void(0)" class="btn btn-info btn-primary btn-sm margin-left-5" tripway tripState="0" needApply="0">直接常规借款</a>
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
