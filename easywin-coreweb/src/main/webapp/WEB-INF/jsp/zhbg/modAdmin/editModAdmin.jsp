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
									<span class="widget-caption themeprimary pageTitle" style="font-size: 18px">综合办公管理员</span>
								</div>
							</div>
						</div>
						
						
						<div class="widget-body no-shadow no-padding padding-top-5">
			                	<div class="widget-main ">
			                    	<div class="tabbable">
			                            <div class="tabs-flat">
			                            
			                            	<div class="tab-pane" style="min-height: 600px">
			                            	
			                            		<div style="width: 50%;" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">车辆管理人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="zhbgSelect_025" busType="025" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="zhbgDiv_025" class="pull-left zhbg" busType="025" >
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																 style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
			                            		</div>
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">固定资产管理人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="zhbgSelect_026" busType="026" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="zhbgDiv_026" class="pull-left zhbg" busType="026" >
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																 style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
			                            		</div>
			                            		
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">办公用品管理人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="zhbgSelect_027" busType="027" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="zhbgDiv_027" class="pull-left zhbg" busType="027">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
			                            		</div>
			                            		
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">办公用品采购员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow" style="clear:both">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="zhbgSelect_02701" busType="02701" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="zhbgDiv_02701" class="pull-left zhbg" busType="02701">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
			                            		</div>
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">人事档案管理人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="zhbgSelect_028" busType="028" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="zhbgDiv_028" class="pull-left zhbg" busType="028">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																 style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
													
			                            		</div>
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">制度管理人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="zhbgSelect_040" busType="040" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="zhbgDiv_040" class="pull-left zhbg" busType="040" >
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																 style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
			                            		</div>
			                            		<div style="width: 50%" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">财务办公人员设定</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="zhbgSelect_066" busType="066" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="zhbgDiv_066" class="pull-left zhbg" busType="066" >
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
	<script type="text/javascript" src="/static/js/zhbgJs/modAdminJs/modAdmin.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
	<style>
		.tabPanDiv{
			height: 150px;
		}
	</style>