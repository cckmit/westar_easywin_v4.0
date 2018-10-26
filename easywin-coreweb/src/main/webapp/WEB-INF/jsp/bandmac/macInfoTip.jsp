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
<script type="text/javascript">
	function setWindow(){
		$("#infoTips").html("系统软件已过有效使用期<br>请联系厂商！");
	}
</script>
</head>
<body>
	<div class="container no-padding" style="width: 100%">	
		<div class="row">
			<div class="col-lg-12 col-sm-12 col-xs-12" >
            	<div class="widget" style="margin-top: 0px;">
	            	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	                	<span class="widget-caption themeprimary" style="font-size: 20px" id="titleHead">提示</span>
	                       <div class="widget-buttons">
							<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
	                   </div><!--Widget Header-->
                   
            		<div class="widget-body margin-top-40" style="height: 360px;overflow-y:auto;position: relative;">
	            		<div id="stepOneDiv" style="display:block">
	                     	<div class="widget radius-bordered">
	                            <div style="width: 100%" class="widget-body no-shadow padding-top-20 padding-bottom-20">
	                            	<div class="clearfix" style="clear:both;width: 100%;vertical-align:middle;text-align: center;">
		                            	<span id="infoTips" style="font-size: 30px;;color: red;"></span>
	                            	</div>
								</div>
								
							</div>
							
							<div style="text-align:center; ;">
	            				<span style="line-height: 25px;font-size: 13px">
	            					成都西辰软件有限公司   联系电话：028-85139468<br>
									地址:成都市高新区科园三路4号火炬时代大厦B座7层
	            				</span><br>
	            			</div>
	            		</div>
						
					</div>
					
            	</div>
            </div>
        </div>
     </div>
</form>

</body>
</html>
