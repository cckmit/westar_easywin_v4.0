<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<!-- Page Content -->
<div class="page-content">
	<!-- Page Body -->
	<div class="page-body">
		<div class="row">
			<div class="col-md-12 col-xs-12">
				<div class="profile-container">
					<div class="profile-body">
							<div class="tabbable">
								<div class="tab-content tabs-flat no-margin">
									<div class="tab-pane active">
											<div class="form-title">
												考勤机连接配置
											</div>
											<div class="row">
												<div class="col-sm-7">
													<c:if test="${action eq 'add' }">
													<form action="/attence/addAttenceConnSet" class = "subform" method="post" id="setForm">
													</c:if>
													<c:if test="${action eq 'update' }">
													<form action="/attence/updateAttenceConnSet" class = "subform" method="post" id="setForm">
													</c:if>
														<input type="hidden" name="sid" value="${param.sid }"/>
														<input type="hidden" name="id" value="${attenceConnSet.id }"/>
														<input type="hidden" name="activityMenu" value="${param.activityMenu}">
												   		<input type="hidden" id="redirectPage" name="redirectPage" value="${param.redirectPage}"/>
	                                                    <div class="form-group clearfix">
	                                                        <label for="inputEmail3" class="col-sm-2 control-label no-padding-right">IP地址：</label>
	                                                        <div class="col-sm-10">
	                                                            <input type="text" class="form-control" dataType="*" id="connIP" name="connIP" nullmsg="请填入IP地址！" value ="${attenceConnSet.connIP}" >
	                                                        </div>
	                                                    </div>
	                                                    <div class="form-group clearfix">
	                                                        <label for="inputPassword3" class="col-sm-2 control-label no-padding-right">端口</label>
	                                                        <div class="col-sm-10">
	                                                            <input type="text" style="width:200px;" dataType="n" name="port" id="port" value ="${attenceConnSet.port}" class="form-control" nullmsg="请填入端口号！" errormsg="请填入正确的端口号！">
	                                                        </div>
	                                                    </div>
	                                                    <div class="form-group clearfix">
	                                                        <label for="inputPassword3" class="col-sm-2 control-label no-padding-right">打卡机品牌</label>
	                                                        <div class="col-sm-10">
	                                                        <c:choose>
	                                                        	<c:when test="${not empty attenceConnSet.type}">
	                                                        	<tags:radioDataDic type="attenceConnType" name="type" style="ws-radio"  
																value="${attenceConnSet.type}"></tags:radioDataDic> 
	                                                        	</c:when>
	                                                        	<c:otherwise>
	                                                        	<tags:radioDataDic type="attenceConnType" name="type" style="ws-radio"  
																value="1"></tags:radioDataDic> 
	                                                        	</c:otherwise>
	                                                        </c:choose>
	                                                          
	                                                        </div>
	                                                    </div>
	                                                   <!--  <div class="form-group clearfix">
	                                                        <label for="inputPassword3" class="col-sm-2 control-label no-padding-right">登录帐户</label>
	                                                        <div class="col-sm-10">
	                                                            <input type="text" name="account" id="account" class="form-control" placeholder="登录帐户">
	                                                        </div>
	                                                    </div>
	                                                    <div class="form-group clearfix">
	                                                        <label for="inputPassword3" class="col-sm-2 control-label no-padding-right">密码</label>
	                                                        <div class="col-sm-10">
	                                                            <input type="password" name="passwd" id="passwd" class="form-control" placeholder="密码">
	                                                        </div>
	                                                    </div> -->
	                                                    <div class="form-group clearfix">
	                                                        <label for="inputPassword3" class="col-sm-2 control-label no-padding-right"></label>
	                                                        <div class="col-sm-10">
	                                                           <button  type="button" onclick="checkIP()" class="btn btn-primary pull-right">保 存</button> 
	                                                        </div>
	                                                    </div>
	                                                </form>
												</div>
												<div class="col-sm-3">
													<!-- <div>
														注意：若提示配置有误,请检查以下几点
													</div>
													<div>
														1、账号和密码（或授权密码）是否正确;<br/>
														2、邮箱的SMTP是否开启，端口是否正确;<br/>
														3、邮箱是否要求安全连接(SSL)
													</div>
													<div>
														若是仍然提示有误，请换邮箱
													</div> -->
												</div>
											</div>
									</div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
		<!-- /Page Body -->
	</div>
	<!-- /Page Content -->
</div>
<!-- /Page Container -->
	<script type="text/javascript" >
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			datatype:{
				"connIp":function(gets,obj,curform,regxp){
					if(isValidIP($("#connIp").val())){
						return true;
					}else{
						return "请填入正确的IP地址！";
					}
				}
			},
			callback:function (form){
				return sumitPreCheck(null);
			},
			showAllError : true
		});
	});
		function checkIP(){
			$(".subform").submit();
		}
	
	</script>
</body>
</html>

