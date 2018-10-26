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
										<span class="widget-caption themeprimary pageTitle" style="font-size: 18px">客户操作配置</span>
									</div>
								</div>
							</div>
							
							<div class="widget-body no-shadow no-padding padding-top-5">
			                	<div class="widget-main ">
			                    	<div class="tabbable">
			                            <div class="tabs-flat">
			                            
			                            	<div class="tab-pane" style="min-height: 600px;height:-webkit-fill-available;">
			                            	
			                            		<div style="width: 50%;display: ${param.moduleType=='012'?'block':'none'};min-height: 180px" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">审批人员</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="zhbgSelect_067" busType="067" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="zhbgDiv_067" class="pull-left zhbg" busType="067">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																 style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
													
			                            		</div>
			                            		<div style="width: 50%;display: ${param.moduleType=='012'?'block':'none'};min-height: 180px" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">审批范围</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="zhbgSelect_06701" busType="06701" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="zhbgDiv_06701" class="pull-left zhbg" busType="06701">
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
																 style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
													
			                            		</div>
			                            		
			                            		
			                            		<div style="width: 50%;min-height: 180px" class="pull-left tabPanDiv">
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">监督人员</span>
														<span class="widget-caption red">
												           <small class="ws-active ws-color">(双击移除)</small>
												        </span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
														<div class="online-list" style="cursor: auto;">
															<div style="float: left; width: 250px;display:none;">
																<select id="forceInSelect_${param.moduleType}" busType="${param.moduleType}" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
																</select>
															</div>
															<div id="forceInDiv_${param.moduleType}" class="pull-left forceIn" busType="${param.moduleType}" >
																<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn2" title="人员多选" 
																 style="float: left;"><i class="fa fa-plus"></i>选择</a>
															</div>
														</div>
													</div>
													
			                            		</div>
			                            		
			                            	
			                            		<div style="width: 50%;display: ${param.moduleType=='012'?'block':'none'}" class="pull-left tabPanDiv userConfCode" userConfCode ="sysConfig" >
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue">属性变更</span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
					                            		<ul class="list-group uConf_sysConfig">
															<li class="list-group-item">
																<span class="pull-right">
																	<label>
																		<input class="checkbox-slider slider-icon colored-blue" type="checkbox" id="owner" onchange="updateChangeConfig('012')">
																	<span class="text"></span>
																	</label>
																</span>客户移交审批
															</li>
															<li class="list-group-item">
																<span class="pull-right">
																	<label>
																		<input class="checkbox-slider slider-icon colored-blue" type="checkbox" id="customerTypeId"  onchange="updateChangeConfig('012')">
																	<span class="text"></span>
																	</label>
																</span>客户类型变更审批
															</li>
															<li class="list-group-item">
																<span class="pull-right">
																	<label>
																		<input class="checkbox-slider slider-icon colored-blue" type="checkbox" id="stage"  onchange="updateChangeConfig('012')">
																	<span class="text"></span>
																	</label>
																</span>所属阶段变更审批
															</li>
														</ul>
														
													</div>
			                            		</div>
			                            		<div style="width: 50%;min-height: 200px;" class="pull-left tabPanDiv userConfCode" userConfCode ="sysConfig" >
													<div class="widget-header bg-bluegray no-shadow margin-left-10 margin-right-10" style="clear:both">
														<span class="widget-caption blue ps-layerTitle">操作权限</span>
														<div class="widget-buttons btn-div-full">
														</div>
													</div>
													<div class="widget-body no-shadow">
					                            		<ul class="list-group uConf_sysConfig">
															<c:choose>
																<c:when test="${not empty listModConf}">
																	<c:forEach items="${listModConf}" var="modConf" varStatus="vs">
																		<li class="list-group-item">
																			<span class="pull-right">
																				<label>
																					<input class="checkbox-slider slider-icon colored-blue" id="opTypes" name="opTypes" value="${modConf.operateType }" 
																					${(param.moduleType=='006' && modConf.operateType=='add')?'disabled':'' }
																					${modConf.enabled==1?'checked':'' } type="checkbox" onchange="updateOperateConfig('${param.moduleType }')"/>
																				    <span class="text"></span>
																			    </label>
																		    </span>${modConf.moduleTypeName}
																		</li>
																	</c:forEach>
																</c:when>
															 </c:choose>
														</ul>
														
													</div>
			                            		</div>
			                            		<div style="width: 50%;min-height: 200px;" class="pull-left tabPanDiv userConfCode" userConfCode ="sysConfig" >
													
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
		
	</body>
	<style>
		.ticket-user2 img {
		    float: left;
		}
	</style>
	<script type="text/javascript">
		var type = '${param.moduleType}';
		$(function(){
			//查询相关配置
			$.ajax({
	      		type : "post",
	      		url : "/moduleChangeExamine/ajaxGetModChangeExam?sid="+'${param.sid}' + "&moduleType=${param.moduleType}",
	      		dataType:"json",
	      		traditional :true, 
	      		data:null,
	      		  beforeSend: function(XMLHttpRequest){
	               },
	      		  success : function(data){
	      			  if(data.status == "y"){
	      				  if(data.lists && data.lists[0]){
	      					 for (var i = 0; i < data.lists.length; i++) {
	      						 if(type=='012'){//客户
	      							 if(data.lists[i].field == "customerTypeId"){
	      								 $("#customerTypeId").attr("checked",true);
	      							 }else if(data.lists[i].field == "owner"){
	      								 $("#owner").attr("checked",true);
	      							 }else if(data.lists[i].field == "stage"){
	      								 $("#stage").attr("checked",true);
	      							 }
	      						 }
							}				  
	      				  }
	      			  }
	      		  },
	      		  error:  function(XMLHttpRequest, textStatus, errorThrown){
	      			  window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
	      	      }
	      	}); 
			
			
			//查询已有的综合办公管理员
			$.each($(".zhbg"),function(index,zhbObj){
				//业务类型
				var busType = $(this).attr("busType");
				 $.ajax({
					   type: "POST",
					   dataType: "json",
					   url: "/modAdmin/ajaxListModAdmin?sid="+sid+'&t='+Math.random(),
					   data: {"busType":busType},
					   success: function(data){
						   if(data.status=='f'){
							   showNotification(2,data.info);
						   }else{
							   //构建头像
							  componModAdmin(data.listModAdmin,busType);
						   }
					   }
				 });
			})
			
			//人员选择(修改模块管理员)
			$(".userSetBtn").on("click",function(){
				//业务类型
				var busType = $(this).parents(".zhbg").attr("busType");
				
				//人员多选
		    	userMore('zhbgSelect_'+busType,'',sid,'','zhbgDiv_'+busType,function(result){
		    		 var modAdminUsers =new Array();
		    		 //本次选择的管理人员
		    		 if(result.length>0){
						  for ( var i = 0; i < result.length; i++) {
							  var obj = {"userId":result[i].value,"adminName":result[i].text}
							  modAdminUsers.push(obj);
						  }
		    		 }
		    		 //修改模块管理人员
		    		 $.ajax({
						   type: "POST",
						   dataType: "json",
						   url: "/modAdmin/updateModAdmin?sid="+sid+'&t='+Math.random(),
						   data: {"modAdminStr":JSON.stringify(modAdminUsers),"busType":busType},
						   success: function(data){
				    			 	if(data.status=='f'){
				    			 		showNotification(2,data.info);
				    			 	}else{
				    			 		//构建头像数据
				    			 		componModAdmin(data.listModAdmin,busType);
				    			 		showNotification(1,"设置成功！");
				    			 	}
						   }
						});
		    	});
		    });
			//双击移除数据
			$("body").on("dblclick",".ticket-user",function(){
				//业务类型
				var busType = $(this).data("busType");
				//业务数据
				var modAdmin = $(this).data("modAdmin");
				//当前点击对象
				var e = $(this);
				//移除模块管理人员
				$.ajax({
					   type: "POST",
					   dataType: "json",
					   url: "/modAdmin/delModAdmin?sid="+sid+'&t='+Math.random(),
					   data:{"id":modAdmin.id,
							"busType":busType,
							"userId":modAdmin.userId,
							"adminName":modAdmin.adminName},
					   success: function(data){
		    			 	if(data.status=='f'){
		    			 		showNotification(2,data.info);
		    			 	}else{
		    			 		$(e).removeData("modAdmin");
		    			 		$(e).remove();
		    					var option = $('zhbgSelect_'+busType).find("option[value='"+modAdmin.userId+"']")[0];
		    			 		$(option).remove()
		    			 		selected('zhbgSelect_'+busType);
		    			 		showNotification(1,"删除成功！");
		    			 	}
					   }
					});
			})
			
			
			//查询已有的监督人员
			$.each($(".forceIn"),function(index,zhbObj){
				//业务类型
				var busType = $(this).attr("busType");
				 $.ajax({
					   type: "POST",
					   dataType: "json",
					   url: "/forceIn/ajaxListForceInPerson?sid="+sid+'&t='+Math.random(),
					   data: {"busType":busType},
					   success: function(data){
						   if(data.status=='f'){
							   showNotification(2,data.info);
						   }else{
							   //构建头像
							   componForceInHead(data.listFordeInPerson,busType);
						   }
					   }
				 });
			})
			
			//人员选择(修改模块监督人员)
			$(".userSetBtn2").on("click",function(){
				//业务类型
				var busType = $(this).parents(".forceIn").attr("busType");
				
				//人员多选
		    	userMore('forceInSelect_'+busType,'',sid,'','forceInDiv_'+busType,function(result){
		    		 var forceInUsers =new Array();
		    		 //本次选择的管理人员
		    		 if(result.length>0){
						  for ( var i = 0; i < result.length; i++) {
							  var obj = {"userId":result[i].value,"sharerName":result[i].text}
							  forceInUsers.push(obj);
						  }
		    		 }
		    		 //修改模块管理人员
		    		 $.ajax({
						   type: "POST",
						   dataType: "json",
						   url: "/forceIn/updateForceInPersion?sid="+sid+'&t='+Math.random(),
						   data: {"forceInUserStr":JSON.stringify(forceInUsers),"busType":busType},
						   success: function(data){
				    			 	if(data.status=='f'){
				    			 		showNotification(2,data.info);
				    			 	}else{
				    			 		//构建头像数据
				    			 		componForceInHead(data.listFordeInPerson,busType);
				    			 		showNotification(1,"设置成功！");
				    			 	}
						   }
						});
		    	});
		    });
			//双击移除数据
			$("body").on("dblclick",".ticket-user2",function(){
				//业务类型
				var busType = $(this).data("busType");
				//业务数据
				var forceIn = $(this).data("forceInData");
				//当前点击对象
				var e = $(this);
				//移除模块管理人员
				$.ajax({
					   type: "POST",
					   dataType: "json",
					   url: "/forceIn/delForceInPersion?sid="+sid+'&t='+Math.random(),
					   data:{"id":forceIn.id,
							"type":busType,
							"userId":forceIn.userId,
							"sharerName":forceIn.sharerName},
					   success: function(data){
		    			 	if(data.status=='f'){
		    			 		showNotification(2,data.info);
		    			 	}else{
		    			 		$(e).removeData("forceInData");
		    			 		$(e).remove();
		    					var option = $('#forceInSelect_'+busType).find("option[value='"+forceIn.userId+"']");
		    			 		$(option).remove()
		    			 		selected('forceInSelect_'+busType);
		    			 		showNotification(1,"删除成功！");
		    			 	}
					   }
					});
			})
			
			
			
		})
		
		//更新属性变更配置
		function updateChangeConfig(moduleType) {
			var fields = [];
			if(moduleType == "012"){
				if($('#owner').attr('checked')){
					fields.push("owner");
				}
				if($('#customerTypeId').attr('checked')){
					fields.push("customerTypeId");
				}
				if($('#stage').attr('checked')){
					fields.push("stage");
				}
			}
			$.post("/moduleChangeExamine/updateChangeConfig?sid=${param.sid}",{Action:"post",moduleType:moduleType,fields:fields.toString()},     
 				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
		
		//更新操作权限设置
		function updateOperateConfig(moduleType) {
			var opTypes = [];
			$("input:checkbox[name='opTypes']:checked").each(function() { // 遍历name=opTypes的多选框
				opTypes.push($(this).val()); // 每一个被选中项的值
			});
			$.post("/modOptConf/updateOperateConfig?sid=${param.sid}",{Action:"post",moduleType:moduleType,opTypes:opTypes.toString()},     
 				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
		
		//构造头像（属性变更审批人员）
		function componModAdmin(listModAdmin,busType){
			$("#zhbgSelect_"+busType).html("");
			$("#zhbgDiv_"+busType).find(".ticket-user").remove();
			if(listModAdmin && listModAdmin.length>0){
				$.each(listModAdmin,function(i,obj){
					var option = $('<option selected="selected" value="'+obj.userId+'">'+obj.adminName+'</option>');
					$("#zhbgSelect_"+busType).append(option);
					//头像名称父div
					var divP = $('<div class="ticket-user pull-left other-user-box" title="双击移除"  style="margin-bottom: 5px;"></div>');
					//头像
					var img = $('<img src="/downLoad/userImg/'+obj.comId+'/'+obj.userId+'" class="user-avatar">');
					//名称
					var name = $('<span class="user-name">'+obj.adminName+'</span>')
				
					$(divP).append(img);
					$(divP).append(name);
					
					$("#zhbgDiv_"+busType).find(".userSetBtn").before($(divP))
					
					$(divP).data("busType",busType);
					$(divP).data("modAdmin",obj);
				})
			}
		}
		
		//构造头像(监督人员)
		function componForceInHead(forceIns,busType){
			$("#forceInSelect_"+busType).html("");
			$("#forceInDiv_"+busType).find(".ticket-user2").remove();
			if(forceIns && forceIns.length>0){
				$.each(forceIns,function(i,obj){
					var option = $('<option selected="selected" value="'+obj.userId+'">'+obj.sharerName+'</option>');
					$("#forceInSelect_"+busType).append(option);
					//头像名称父div
					var divP = $('<div class="ticket-user2 pull-left other-user-box" title="双击移除"  style="margin-bottom: 5px;"></div>');
					//头像
					var img = $('<img src="/downLoad/userImg/'+obj.comId+'/'+obj.userId+'" class="user-avatar">');
					//名称
					var name = $('<span class="user-name">'+obj.sharerName+'</span>')
				
					$(divP).append(img);
					$(divP).append(name);
					
					$("#forceInDiv_"+busType).find(".userSetBtn2").before($(divP))
					
					$(divP).data("busType",busType);
					$(divP).data("forceInData",obj);
				})
			}
		}
		
	</script>
</html>
