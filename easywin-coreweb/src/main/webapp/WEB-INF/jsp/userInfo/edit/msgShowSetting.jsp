<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<body>
		<div class="page-content">
			<!-- Page Body -->
			<div class="page-body">
			
				<div class="row">
					<div class="col-md-12 col-xs-12 ">
						<div class="widget">
							<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
								<div>
									<div class="ps-margin ps-search searchCond">
										<span class="widget-caption themeprimary pageTitle" style="font-size: 18px">显示设置</span>
									</div>
								</div>
							</div>
							
							<div class="widget-body no-shadow no-padding padding-top-5">
			                	<div class="widget-main ">
			                    	<div class="tabbable">
			                            <div class="tabs-flat">
			                            
			                            	<div class="tab-pane" style="min-height: 450px">
			                            	
			                            		<div style="width: 50%;" class="pull-left tabPanDiv userConfCode" userConfCode ="sysConfig">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">消息通知开关</span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
					                            		<ul class="list-group uConf_sysConfig">
															
														</ul>
														
													</div>
			                            		</div>
			                            		<div style="width: 50%;" class="pull-left tabPanDiv userConfCode" userConfCode ="menuNum">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">菜单显示数</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(默认显示使用常用三个,至多六个)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="spinner spinner-horizontal spinner-two-sided pull-left" style="vertical-align:middle;line-height: 35px">
															<span>快捷菜单显示数：</span>
														</div>
														<div class="spinner spinner-horizontal spinner-two-sided pull-left">	
															<div class="spinner-buttons	btn-group spinner-buttons-left">		
																<button type="button" class="btn spinner-down danger minusNum">			
																	<i class="fa fa-minus"></i>		
																</button>	
															</div>	
															<input class="spinner-input form-control table_input" maxlength="3" 
																type="text" value="3" disabled="disabled">	
															<div class="spinner-buttons	btn-group spinner-buttons-right">		
																<button type="button" class="btn spinner-up blue addNum">			
																	<i class="fa fa-plus"></i>
																</button>	
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
					</div>
				</div>
				<!-- /Page Body -->
			</div>
			<!-- /Page Content -->
		</div>
		<script type="text/javascript">
			$(function(){
				$.each($(".userConfCode"),function(){
					var type = $(this).attr("userConfCode");
					var _this = $(this);
					getSelfJSON("/userConf/ajacListUserConfig",{"sid":sid,"type":type},function(data){
						if(data.status=='y'){
							if(type=="sysConfig"){
								//消息通知开关
								conStrSysConfig(data.listUserConfig)
							}else if(type=="menuNum"){
								conStrMenuNum(data.userConf,_this)
							}
						}
					})
				})
			})
			//消息通知开关
			function conStrSysConfig(list){
				if(list && list.length>0){
					$.each(list,function(confInde,userConf){
						if(userConf.sysConfCode == 2){
							return true;
						}else{
							var _li = $("<li class='list-group-item'></li>")
							var _span = $("<span class='pull-right'></span>")
							var _lable = $("<label></label>");
							
							var _input = $("<input class='checkbox-slider slider-icon colored-blue' type='checkbox'/>")
							userConf.openState=='1'?$(_input).attr("checked",true):"";
							_input.attr("value",userConf.sysConfCode);
							_lable.append(_input)
							_lable.append('<span class="text"></span>')
							
							_span.append(_lable);
							_li.append(_span);
							_li.append(userConf.sysConfigName);
							
							$(".uConf_sysConfig").append(_li);
							
							
							_input.on("click",function(){
								switchSet($(this),sid)
							})
						}
					})
				}
			}
			
			function conStrMenuNum(userConf,_this){
				var menuNum = 3;
				if(userConf){
					menuNum =  userConf.openState;
				}
				$(_this).find("input").val(menuNum);
				
				$(_this).on("click",".minusNum",function(){
					if(menuNum > 3){
						var tempMenuNum = menuNum-1
						updateUserConf(tempMenuNum,sid,function(data){
							if(data.status=='y'){
								menuNum = menuNum-1
								$(_this).find("input").val(menuNum);
								
								var memuNumObj = {memuNum:menuNum};
								var memuNumStr = JSON.stringify(memuNumObj);
								sessionStorage.firstMenuNum = escape(memuNumStr);
								
								initHeadMenu('${userInfo.id}','${userInfo.comId}')
								
								layer.msg("操作成功",{icon: 6}); 
							}
						})
						
					}
					
					
				})
				$(_this).on("click",".addNum",function(){
					if(menuNum < 6){
						var tempMenuNum = menuNum+1
						updateUserConf(tempMenuNum,sid,function(data){
							if(data.status=='y'){
								menuNum = menuNum+1
								$(_this).find("input").val(menuNum);
								
								var memuNumObj = {memuNum:menuNum};
								var memuNumStr = JSON.stringify(memuNumObj);
								sessionStorage.firstMenuNum = escape(memuNumStr);
								
								initHeadMenu('${userInfo.id}','${userInfo.comId}')
								
								layer.msg("操作成功",{icon: 6}); 
							}
						})
					}
				})
				
			}
		</script>
	</body>
</html>
