<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
	<!-- Page Content -->
        <div class="page-content">
         	<!-- Page Body -->
            <div class="page-body">
            	<div class="row">
                	<div class="col-md-12 col-xs-12" >
                    	<div class="widget">
                         	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                         		<c:choose>
                         			<c:when test="${userInfo.admin>0}">
                         				<div class="btn-group pull-left">
                         				<!-- 
											<div class="table-toolbar ps-margin">
		                                       	<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick="add()">
														新增部门
													</a>
		                                       	</div>
		                                   	</div>
                         				 -->
										<div class="table-toolbar ps-margin">
	                                       	<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick="enabled()">
													部门启用
												</a>
	                                       	</div>
	                                   	</div>
										<div class="table-toolbar ps-margin">
	                                       	<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick="disEnabled()">
													部门禁用
												</a>
	                                       	</div>
	                                   	</div>
										<div class="table-toolbar ps-margin">
	                                       	<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick="del()">
													部门删除
												</a>
	                                       	</div>
	                                   	</div>
									</div>
                         			</c:when>
                         			<c:otherwise>
		                         		<span class=" widget-caption themeprimary margin-top-5" style="font-size: 18px">部门信息</span>
                         			</c:otherwise>
                         		</c:choose>
                         		<div class="ps-margin ps-search pull-right margin-right-10" id="searchDiv">
										<span class="input-icon">
											<input name="condition" id="condition"  class="form-control ps-input" type="text" placeholder="请输入关键字">
											<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
										</span>
									</div>
                            </div>
                            <div class="widget-body" style="clear:both">
	                        	<div id="container" style="width: 100%;" align="center" >
									<div id="left" style="width:20%;float:left;">
										<iframe id="leftFrame" name="leftFrame" src="/department/treeDepPage?sid=${param.sid }" 
											width=98% height=100% frameborder="0"  scrolling="auto"></iframe>
									</div>
									<div id="right" style="width:79%;float:left;border-left:#999999 1px dashed">
										<iframe id="rightFrame" name="rightFrame" src="/department/listPagedUserTreeForDep?sid=${param.sid }&pager.pageSize=10" 
										width=98% border="0" frameborder="0" allowTransparency="true"
										noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
									</div>
								</div>
	                             <div style="clear:both"></div>
                             </div>
                         </div>
                     </div>             
             </div>
             <!-- /Page Body -->
         </div>
         <!-- /Page Content -->
     </div>
     <script type="text/javascript">
     	//添加部门
		function add(){
			leftFrame.add();
		}
		//启用部门
		function enabled(){
			leftFrame.enabled();
		}
		//禁用部门
		function disEnabled(){
			leftFrame.disEnabled();
		}
		//删除部门
		function del(){
			leftFrame.del();
		}
		
		$(function() { 
			//任务名筛选
			$("#condition").blur(function(){
				var val = $("#condition").val();
				rightFrame.setCondition(val);
			});
			//文本框绑定回车提交事件
			$("#condition").bind("keydown",function(event){
		        if(event.keyCode == "13")    
		        {
		        	var val = $("#condition").val();
		        	rightFrame.setCondition(val);
		        }
		    });
		}); 
		//隐藏搜索
		function hideSearch(){
			$("#searchDiv").hide();
		}
		//显示搜索
		function showSearch(){
			$("#searchDiv").show();
		}
</script>
</body>
</html>
