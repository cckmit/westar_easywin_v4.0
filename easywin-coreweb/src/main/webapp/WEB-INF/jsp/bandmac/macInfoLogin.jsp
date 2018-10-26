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
var pageIndex;

//关闭窗口
function closeWin(){
	window.parent.location.replace("/")
}
	function setWindow(data,indexParam){
		pageIndex = indexParam;
		$(function(){
			$("#stepTwoDiv").on("click","#startLogin",function(){
				var params = constrParam(data.mac);
				if(params){
					getSelfJSON("/bandMAC/macValidatlogin", params, function(data){
						if(data.status=='y'){
							window.self.location.replace("/bandMAC/macValidatSetPage")
						}else{
							showNotification(2,data.info);
						}
					})
				}
			})
		})
	}
	//构建返回数据
	function constrParam(mac){
		var account = $("#stepTwoDiv").find("input[name='account']").val();
		if(!account){
			layer.tips("请填写账号！",$("#stepTwoDiv").find("input[name='account']"),{tips:1});
			return null;
		}
		var passwd = $("#stepTwoDiv").find("input[name='passwd']").val();
		if(!passwd){
			layer.tips("请填写密码！",$("#stepTwoDiv").find("input[name='passwd']"),{tips:1});
			return null;
		}
		
		var params = {};
		params.account = account;
		params.password = passwd;
		params.macStr = mac;
		return params;
	}
	
	
</script>
</head>
<body>
	<div class="container no-padding" style="width: 100%">	
		<div class="row">
			<div class="col-lg-12 col-sm-12 col-xs-12" >
            	<div class="widget" style="margin-top: 0px;">
	            	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	                	<span class="widget-caption themeprimary" style="font-size: 20px" id="titleHead">服务管理员登录</span>
	                       <div class="widget-buttons">
							<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
	                   </div><!--Widget Header-->
                   
            		<div class="widget-body margin-top-40" style="height: 360px;overflow-y:auto;position: relative;">
	            		<div id="stepTwoDiv" style="text-align: center;">
	            			<div class="margin-bottom-10" style="font-size: 25px">服务管理员登录 </div>
	            			<div class="widget radius-bordered" style="height:200px;text-align:left;">
	            				<div class="clear-fix margin-top-10 padding-left-20" style="height: 100px">
									<div style="line-height: 55px">
										<span style="font-size: 18px;width: 150px;">管理员账号:</span>
										<span style="font-size: 15px;line-height: 25px;">
											<input type="text" name="account" style="width: 250px">
										</span>
									</div>
									<div style="line-height: 55px">
										<span style="font-size: 18px">管理员密码:</span>
										<span style="font-size: 15px;line-height: 25px;">
											<input type="password" name="passwd" style="width: 250px;">
										</span>
									</div>
									<div class="clearfix margin-top-10" style="vertical-align: middle;text-align: center;">
										<button type="button" class="btn btn-info ws-btnBlue margin-right-50" id="startLogin">登录</button>
										<button type="button" class="btn btn-default" style="background-color: #ccc;" onclick="closeWin()" >关闭</button>
									</div>
								</div>
	            			</div>
	            			<div style="text-align: left;">
	            				<span style="color: red;">注：服务管理员登录功能仅提供给供应商的实施人员！</span>
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
