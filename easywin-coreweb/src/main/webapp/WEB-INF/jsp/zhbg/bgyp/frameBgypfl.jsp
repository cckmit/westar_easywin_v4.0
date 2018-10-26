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
                         		<div class="btn-group pull-left">
									<div class="table-toolbar ps-margin">
                                       	<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick="add()">
												新增分类
											</a>
                                       	</div>
                                   	</div>
									<div class="table-toolbar ps-margin">
                                      	<div class="btn-group">
										<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick="del()">
											删除分类
										</a>
                                      	</div>
                                  	</div>
								</div>
                         		
                            </div>
                            
                            <div class="widget-body" style="clear:both;min-height: 450px">
	                        	<div id="container" style="width: 100%;" align="center" >
									<div id="left" style="width:30%;float:left;">
										<ul id="bgflTree" class="ztree"></ul>
									</div>
									<div id="right" style="width:69%;float:left;border-left:#999999 1px dashed;min-height: 450px">
										<iframe id="rightFrame" name="rightFrame" src="/blank?sid=${param.sid}"
										width=98% border="0" frameborder="0" allowTransparency="true" style="min-height: 450px"
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
     <link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" >
	<link href="/static/assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
     <script type="text/javascript" src="/static/js/zhbgJs/bgflJs/bgflCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
     <script type="text/javascript">
		
</script>
</body>
</html>
