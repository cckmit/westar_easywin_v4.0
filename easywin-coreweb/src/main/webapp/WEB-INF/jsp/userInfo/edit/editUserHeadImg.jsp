<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<!-- Page Container -->
<div class="page-container">
    <!-- /Page Sidebar -->
    <!-- Page Content -->
    <div class="page-content"> 
        <!-- Page Body -->
        <div class="page-body">
        	<div class="widget-body">
        		<div class="row">
            	<div class="col-md-3">
            		<h6 class="row-title before-palegreen">当前头像</h6>
            		<div class="ps-headPic">
						<img src='/downLoad/userImg/${userInfo.comId}/${userInfo.id}?sid=${param.sid}&size=1'/>
            		</div>
            	</div>
            	<div class="col-md-9">
            		<h6 class="row-title before-palegreen">头像预览</h6>
				    <!--用来存放item-->
				    <div class="ps-preview" id="userImgList" style="border:1px solid #ebebeb;width:210px;height:210px;"></div>
            		
            		<div class="buttons-preview padding-top-10">
            		 	<div id="userImgPicker" class="pull-left">选择图片</div>
            			<form id="imgForm">
				      		<input type="hidden" name="id" value="${userInfo.id}"/>
							<input type="hidden" name="orgFilePath" id="orgFilePath" value=""/>
				      		<input type="hidden" name="orgFileName" id="orgFileName" value=""/>
				      		<input type="hidden" name="largeImgPath" id="largeImgPath" value=""/>
            			<a href="javascript:void(0);" class="btn btn-grey pull-left" onclick="updateImg('${param.sid}');" style="margin-left:10px;height:30px;">更换头像</a>
						</form>
            		</div>
            		<p class="text-danger padding-10 pull-left">图片尺寸建议200*200</p>
            	</div>
            </div>
        	</div>
        <!-- /Page Body -->
       </div>
    <!-- /Page Content -->
</div>
<!-- /Page Container -->
<!-- Main Container -->
</div>
</body>
</html>

