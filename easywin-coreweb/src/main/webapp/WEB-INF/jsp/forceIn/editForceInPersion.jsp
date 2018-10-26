<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>

	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div>
								<div class="ps-margin ps-search searchCond">
									<span class="widget-caption themeprimary pageTitle" style="font-size: 18px">监督人员设定</span>
								</div>
							</div>
						</div>
						<div class="widget-body no-shadow no-padding padding-top-5">
			                	<div class="widget-main ">
			                    	<div class="tabbable">
			                            <div class="tabs-flat">
			                            
			                            	<div class="tab-pane" style="min-height: 900px">
			                            	
			                            		<div style="width: 50%;" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">客户模块监督人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="forceInSelect_012" busType="012" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="forceInDiv_012" class="pull-left forceIn" busType="012" >
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																 style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
			                            		</div>
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">项目模块监督人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="forceInSelect_005" busType="005" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="forceInDiv_005" class="pull-left forceIn" busType="005" >
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																 style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
			                            		</div>
			                            		
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">任务模块监督人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="forceInSelect_003" busType="003" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="forceInDiv_003" class="pull-left forceIn" busType="003">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
			                            		</div>
			                            		
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">周报模块监督人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow" style="clear:both">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="forceInSelect_006" busType="006" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="forceInDiv_006" class="pull-left forceIn" busType="006">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
			                            		</div>
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">审批模块监督人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="forceInSelect_022" busType="022" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="forceInDiv_022" class="pull-left forceIn" busType="022">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																 style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
													
			                            		</div>
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">差旅模块监督人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="forceInSelect_033" busType="033" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="forceInDiv_033" class="pull-left forceIn" busType="033">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																 style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
			                            		</div>
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">考勤模块监督人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="forceInSelect_038" busType="038" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="forceInDiv_038" class="pull-left forceIn" busType="038">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																 style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
													
			                            		</div>
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">会议模块监督人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="forceInSelect_017" busType="017" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="forceInDiv_017" class="pull-left forceIn" busType="017">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																 style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>

			                            		</div>
												<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">分享模块监督人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="forceInSelect_050" busType="050" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="forceInDiv_050" class="pull-left forceIn" busType="050">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选"
																   style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>

												</div>
												<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">需求模块监督人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="forceInSelect_070" busType="070" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="forceInDiv_070" class="pull-left forceIn" busType="070">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选"
																   style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
												</div>
												<div style="width: 100%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">产品模块监督人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="forceInSelect_080" busType="080" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="forceInDiv_080" class="pull-left forceIn" busType="080">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选"
																   style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
												</div>
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
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
	<script type="text/javascript">
		//session标识
		var sid = '${param.sid}';
	</script>
	<script type="text/javascript" src="/static/js/forceInJs/forceIn.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
	<style>
		.tabPanDiv{
			height: 150px;
		}
		.ticket-user{
			cursor: pointer;
		}
	</style>