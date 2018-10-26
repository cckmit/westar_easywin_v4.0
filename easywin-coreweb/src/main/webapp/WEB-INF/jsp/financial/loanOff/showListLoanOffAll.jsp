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
	function initTable(listLoanOffArray){
		if(listLoanOffArray && listLoanOffArray[0]){
			var totalLoanOff = 0;
			var totalLoanItem = 0;
			$.each(listLoanOffArray,function(index,loanOff){
				var tr = $('<tr></tr>')
				//序号
				var tdXh = $('<td></td>');
				$(tdXh).html((index+1));
				$(tr).append($(tdXh));
				//报销记录
				var tdRecord = $('<td></td>');
				var recordA = $('<a href="javascript:void(0)" loanOff title="点击查看详情"></a>');
				$(recordA).data("loanOff",loanOff);
				$(recordA).html(loanOff.loanOffName);
				$(tdRecord).html($(recordA))
				$(tr).append($(tdRecord));
				//报销时间
				var tdDate = $('<td></td>');
				$(tdDate).html(loanOff.recordCreateTime)
				$(tr).append($(tdDate));
				
				//实报销
				var tdLoanItem = $('<td></td>');
				var spanLoanItem = $('<span class="blue"></span>');
				
				$(spanLoanItem).html("￥"+toDecimal2(loanOff.loanOffItemFee))
				$(tdLoanItem).append($(spanLoanItem));
				$(tr).append($(tdLoanItem));
				totalLoanItem = Number(totalLoanItem)+ Number(loanOff.loanOffItemFee);
				
				//销账
				var tdLoanOff = $('<td></td>');
				var spanLoanOff = $('<span class="green"></span>');
				$(spanLoanOff).html("￥"+toDecimal2(loanOff.loanOffBalance))
				$(tdLoanOff).append($(spanLoanOff));
				$(tr).append($(tdLoanOff));
				totalLoanOff = Number(totalLoanOff)+ Number(loanOff.loanOffBalance);
				
				$("#listTbody").append($(tr));
				
			});
			
			var tr = $('<tr></tr>')
			//序号
			var tdTotal = $('<td colspan="3">合计</td>');
			$(tr).append($(tdTotal));
			
			//序号
			var tdLoanItemToal = $('<td></td>');
			var spanLoanItem = $('<span class="blue"></span>');
			$(spanLoanItem).html("￥"+toDecimal2(totalLoanItem))
			$(tdLoanItemToal).append($(spanLoanItem));
			$(tr).append($(tdLoanItemToal));
			//序号
			var tdLoanToal = $('<td></td>');
			var spanLoanItem = $('<span class="green"></span>');
			$(spanLoanItem).html("￥"+toDecimal2(totalLoanOff))
			$(tdLoanToal).append($(spanLoanItem));
			$(tr).append($(tdLoanToal));
			
			$("#listTbody").append($(tr));
			
		}	
	}
	//选择需要查看报账信息
	function loanOffSelected(ts){
		var loanOff = $(ts).data("loanOff");
		if(loanOff){
			return loanOff;
		}
	}
</script>
</head>
<body>
	<div class="container no-padding" style="width: 100%">	
		<div class="row" >
			<div class="col-lg-12 col-sm-12 col-xs-12" >
            	<div class="widget" style="margin-top: 0px;">
	            	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	                	<span class="widget-caption themeprimary" style="font-size: 20px">累计报账记录</span>
	                       <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
	                   </div><!--Widget Header-->
                   
            		<div class="widget-body margin-top-40" style="height: 360px;overflow-y:auto;position: relative;">
                     		<table class="table table-bordered" style="font-size: 13px">
                     			<thead>
                     				<tr>
                     					<td>
                     						序号
                     					</td>
                     					<td>
                     						报销记录
                     					</td>
                     					<td>
                     						报销时间
                     					</td>
                     					<td>
                     						实报销
                     					</td>
                     					<td>
                     						销账
                     					</td>
                     				</tr>
                     			</thead>
                     			<tbody id="listTbody">
                     			</tbody>
                     		</table>
					</div>
					
            	</div>
            </div>
        </div>
     </div>

</body>
</html>
