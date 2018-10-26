<%@page import="com.westar.base.util.ConstantInterface"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">
	#invUl li a{
		background-color: #fff !important;
	}
	#invUl .active a{
		background-color: #f0f0f0!important;
	}
</style>
</head>
<body>
	
	<!-- Page Content -->
        <div class="page-content">
         	<!-- Page Body -->
            <div class="page-body">
            	<div class="row">
                	<div class="col-md-12 col-xs-12">
                    	<div class="widget">
                         	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                            	<span class="widget-caption themeprimary" style="font-size: 20px">邀请用户</span>
                            </div>
                            <div class="widget-body">
	                            <div class="widget-main ">
		                    		<div class="tabbable">
			                        	<ul id="invUl" class="nav nav-tabs " style="padding-top: 0px;box-shadow:0 0 0" >
			                        		<li class="active" style="width: 50%">
			                                	<a data-toggle="tab" class="themeprimary" href="javascript:void(0)" onclick="changeInvtWay(1,this)"
			                                	style="text-align: center;border:0;box-shadow:0 0 0 rgb(0,0,0);border-bottom:1px solid rgba(0,0,0,0.1);margin-bottom: 1px" >手机号邀请</a>
			                            	</li>
			                        		<li style="width: 50%">
			                                	<a data-toggle="tab" class="themeprimary" href="javascript:void(0)" onclick="changeInvtWay(2,this)"
			                                	style="text-align: center;border:0;box-shadow:0 0 0 rgb(0,0,0);" >邮箱邀请</a>
			                            	</li>
			                            </ul>
			                            <div class="tab-content tabs-flat no-padding-top no-padding-bottom">
		                            		<div class="tab-pane active" id="phoneDiv" style="text-align: middle;margin:0 auto;">
		                            			<div style="width: 50%;text-align:middle;margin:0 auto;">
			                						<div align="center" style="font-size: 20px;padding-bottom: 10px;padding-top: 10px">
												 		手机号邀请
												 	</div>
												 	<div style="clear: both">
													 	<form class="subform" method="post" id="phoneForm">
														 	<input type="hidden" name="invtWay" value="<%=ConstantInterface.GET_BY_PHONE%>"/>
														 	<input type="hidden" name="redirectPage" value="/userInfo/inviteUserPage?sid=${param.sid }&tab=${param.tab}"/>
														    <input type="hidden" name="sid" value="${param.sid}">
														    <table width="100%" align="center" class="table table-striped table-hover dataTable" style="font-size: 15px">
														     	<tr>
														     		<td style="border: 0">
														     				手机号
														     		</td>
														     		<td style="border: 0">
														     			<div style='width:90%;float:left'>
														     				<input type="text" class="form-control small" ajaxurl="/userInfo/checkInvUser?sid=${param.sid}&type=phone" datatype="m" ignore="ignore" name="invUsers"/>
														     			</div>
														     		</td>
														     	</tr>
														     	<tr>
														     		<td style="border: 0">
														     				手机号
														     		</td>
														     		<td style="border: 0">
														     			<div style='width:90%;float:left'>
															     			<input type="text" class="form-control small" ajaxurl="/userInfo/checkInvUser?sid=${param.sid}&type=phone" 
															     			datatype="m" ignore="ignore" name="invUsers"/>
														     			</div>
														     		</td>
														     	</tr>
														    </table>
														    <div style="padding-top: 10px;text-align: center;">
														     	<input type="botton" value="邀请" class="btn btn-info ws-btnBlue" onclick="checkEmail(this.form);return false;"/>
														     </div>
														    </form>
													 	</div>
			                						
			                					</div>
			                					
		                            		</div>
		                            		<div class="tab-pane" id="emailDiv" >
			                					<div  style="width: 50%;text-align:middle;margin:0 auto;">
			                						<div align="center" style="font-size: 20px;padding-bottom: 10px;padding-top: 10px">
												 		邮箱邀请
												 	</div>
												 	<div style="clear: both">
													 	<form class="subform" method="post" id="emailForm">
													 	<input type="hidden" name="invtWay" value="<%=ConstantInterface.GET_BY_EMAIL%>"/>
													 	<input type="hidden" name="comId" value="${userInfo.comId}"/>
													 	<input type="hidden" name="redirectPage" value="/userInfo/inviteUserPage?sid=${param.sid }&tab=${param.tab}"/>
													     <input type="hidden" name="sid" value="${param.sid}">
													    <table width="100%" align="center" class="table table-striped table-hover dataTable" id="invtab" style="font-size: 15px">
													     	<tr>
													     		<td style="border: 0">
													     				Email
													     		</td>
													     		<td style="border: 0">
													     			<div style='width:90%;float:left'>
													     				<input type="text" class="form-control small" ajaxurl="/userInfo/checkInvUser?sid=${param.sid}&type=email" datatype="e" ignore="ignore" name="invUsers"/>
													     			</div>
													     		</td>
													     	</tr>
													     	<tr>
													     		<td style="border: 0">
													     				Email
													     		</td>
													     		<td style="border: 0">
													     			<div style='width:90%;float:left'>
														     			<input type="text" class="form-control small" ajaxurl="/userInfo/checkInvUser?sid=${param.sid}&type=email" datatype="e" ignore="ignore" name="invUsers" />
													     			</div>
													     		</td>
													     	</tr>
													    </table>
													    <div style="padding-top: 10px;text-align: center;">
													     	<input type="botton" value="邀请" class="btn btn-info ws-btnBlue" onclick="checkEmail(this.form);return false;"/>
													     </div>
													    </form>
												 	</div>
			                						
			                					</div>
		                            		</div>
		                            	</div>
		                            </div>
		                          </div>
                            	<div class="row">
                					<div class="col-md-4 col-xs-4 hide">
                						<div align="center" style="font-size: 20px;padding-bottom: 10px">
									 		更多方式邀请
									 	</div>
									 	<div align="center" style="font-size: 12px;padding-bottom: 10px;line-height: 25px;padding-top: 25px">
							      		<table width="100%" align="left" border="0" cellspacing="0" cellpadding="0">
							      			<tr align="center">
							      				<td style="display:block">
							      					<div class="btns btn-sm">
														<div id="pickeruserFile">选择文件</div>
													</div>
													<script type="text/javascript">
														loadUpfilesForInvUser('${param.sid}','pickeruserFile','fileuserFile');
													</script>
													<br />
													<small style="font-size: 12px">
														文件格式：xlsx,xls,txt
														<br/>
														文件内容需含有邮箱地址
													</small>
							      				</td>
							      			</tr>
							      		</table>
								 	</div>
                						
                					</div>
                				</div>
                             </div>
                         </div>
                     </div>   
                     <div class="ps-clear"></div>         
             </div>
           	<c:choose>
				<c:when test="${userInfo.admin>0}">
                          <div class="no-shadow">
                          	<iframe style="width:100%;" id="invUser" name="invUser" 
						src="/userInfo/listPagedInvUser?sid=${param.sid}&pager.pageSize=10"
						border="0" frameborder="0" allowTransparency="true"
						noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
                          </div>
				</c:when>
			</c:choose>
             
         </div>
            
        </div>
</body>
</html>
