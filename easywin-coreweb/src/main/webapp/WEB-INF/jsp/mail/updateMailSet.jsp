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
												邮箱配置维护
											</div>
											<div class="row">
												<div class="col-sm-7">
													<form action="/mailSet/updateMailSet" method="post" id="mailSetForm">
														<input type="hidden" name="sid" value="${param.sid }"/>
														<input type="hidden" name="id" value="${mailSet.id}"/>
												   		<input type="hidden" id="redirectPage" name="redirectPage" value="${param.redirectPage}"/>
	                                                    <div class="form-group clearfix">
	                                                        <label for="inputEmail3" class="col-sm-2 control-label no-padding-right">邮箱</label>
	                                                        <div class="col-sm-10">
	                                                            <input type="text" class="form-control" placeholder="Email"id="email" name="email" value="${mailSet.email}" onkeyup="FillSettings(this.value)">
	                                                        </div>
	                                                    </div>
	                                                    <div class="form-group clearfix">
	                                                    	<label for="inputPassword3" class="col-sm-2 control-label no-padding-right">服务器(SMTP)</label>
	                                                        <div class="col-md-7">
	                                                            <input type="text" name="serverHost" id="serverHost" value="${mailSet.serverHost}" placeholder="SMTP" class="form-control">
	                                                        </div>
	                                                    </div>
	                                                    <div class="form-group clearfix">
	                                                        <label for="inputPassword3" class="col-sm-2 control-label no-padding-right">端口(SMTP)</label>
	                                                        <div class="col-sm-10">
	                                                            <input type="text" style="width:200px;" name="serverPort" id="serverPort" value="${mailSet.serverPort}" class="form-control" placeholder="端口">
	                                                        </div>
	                                                    </div>
	                                                    <div class="form-group clearfix">
	                                                    	<label for="inputPassword3" class="col-sm-2 control-label no-padding-right">服务器(IMAP)</label>
	                                                        <div class="col-md-7">
	                                                            <input type="text" name="serverImapHost" id="serverImapHost" value="${mailSet.serverImapHost}" placeholder="POP" class="form-control">
	                                                        </div>
	                                                    </div>
	                                                    <div class="form-group clearfix">
	                                                        <label for="inputPassword3" class="col-sm-2 control-label no-padding-right">端口(IMAP)</label>
	                                                        <div class="col-sm-10">
	                                                            <input type="text" style="width:200px;" name="serverImapPort" id="serverImapPort" value="${mailSet.serverImapPort}" class="form-control" placeholder="端口">
	                                                        </div>
	                                                    </div>
	                                                    <div class="form-group clearfix">
	                                                        <label for="inputPassword3" class="col-sm-2 control-label no-padding-right">安全连接(SSL)</label>
	                                                        <div class="col-sm-10">
	                                                            <label class="optCheckBox" style="width: 20px">
												 					<input class="colored-blue" type="checkbox" id="smtpSSL" name="smtpSSL" value="1" ${mailSet.smtpSSL=='1'?'checked':''}/>
												 					<span class="text"></span>
													 			</label>
	                                                        </div>
	                                                    </div>
	                                                    <div class="form-group clearfix">
	                                                        <label for="inputPassword3" class="col-sm-2 control-label no-padding-right">登录帐户</label>
	                                                        <div class="col-sm-10">
	                                                            <input type="text" name="account" id="account" value="${mailSet.account}" class="form-control" placeholder="登录帐户">
	                                                        </div>
	                                                    </div>
	                                                    <div class="form-group clearfix">
	                                                        <label for="inputPassword3" class="col-sm-2 control-label no-padding-right">邮箱授权码</label>
	                                                        <div class="col-sm-10">
	                                                            <input type="password" name="passwd" id="passwd" value="${mailSet.passwd}" class="form-control" placeholder="邮箱授权码">
	                                                        </div>
	                                                    </div>
	                                                    <div class="form-group clearfix">
	                                                        <label for="inputPassword3" class="col-sm-2 control-label no-padding-right"></label>
	                                                        <div class="col-sm-10">
	                                                           <button  type="button" onclick="checkEmail(this,'${param.sid}')" class="btn btn-primary pull-right">更新</button> 
	                                                        </div>
	                                                    </div>
	                                                </form>
												</div>
												<div class="col-sm-3">
													<div>
														注意：若提示配置有误,请检查以下几点
													</div>
													<div>
														1、账号和授权密码是否正确;<br/>
														2、邮箱的服务器、端口是否正确，SMTP、IMAP是否开启;<br/>
														3、邮箱是否要求安全连接(SSL);<br/>
														4、邮箱安全限制是否开放
													</div>
													<div>
														若是仍然提示有误，请换邮箱
													</div>
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
</body>
</html>

