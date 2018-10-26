<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
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
			<div class="col-md-12 col-xs-12 ">
				<div class="widget">
					<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
						<form action="/userInfo/listPagedUserForGrp" method="post" target="allUser" id="userList">
							<input type="hidden" name="redirectPage" />
							<input type="hidden" name="pager.pageSize" value="10">
							<input type="hidden" id="activityMenu" name="activityMenu" value="${param.activityMenu}">
						 	<input type="hidden" name="sid" value="${param.sid}"/>
							<div class="searchCond" style="display: block">
								<div class="ps-margin ps-search pull-right margin-right-10">
									<span class="input-icon"> <input name="condition" id="condition" 
									value="${userInfo.condition}" placeholder="用户检索……"
											class="form-control ps-input" type="text"> <a href="javascript:void(0)"
										class="ps-searchBtn"><i
											class="glyphicon glyphicon-search circular danger"></i>
									</a> </span>
								</div>
							</div>
						</form>
					</div>
					<div class="widget-header ps-titleHeader bordered-bottom">
						<form class="subform" method="post" action="/selfGroup/addGroup" id="groupForm" onsubmit="return tjGrpForm()">
						<input type="hidden" name="redirectPage" value="${param.redirectPage}" />
						<tags:token></tags:token>
						<div class="clearfix" style=" width:50%; margin:0 auto; padding:20px 0;">
							<div class="pull-left margin-top-7 margin-right-10">
						    	群组名称<span style="color: red">*</span>
						    </div>
							<div class="pull-left" style="text-align: left;height: 40px">
									<input datatype="ENcode" id="grpName" name="grpName"" nullmsg="请填群组名称" 
									class="colorpicker-default form-control pull-left" type="text" value=""
									style="width: 300px;">
									<div class="ps-clear"></div>
							</div>
							<button type="submit" class="btn btn-info pull-left margin-left-10">添加</button>
						</div>
						<div style="display:none">
						<%--人员选择 --%>
						<select list="listUser" listkey="id" listvalue="listUser.name" id="listUser_id" name="listUser.id" 
						ondblclick="removeOne(this)" multiple="multiple" moreselect="true">
							<option value="${userInfo.id}" selected="selected">${userInfo.userName}</option>
						</select>
						</div>
						</form>
					</div>
					<div class="widget-body">
						<iframe style="width:100%;" id="allUser" name="allUser"
						src="/userInfo/listPagedUserForGrp?sid=${param.sid }&grpId=-1"
						border="0" frameborder="0" allowTransparency="true"
						noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
					</div>
				</div>
			</div>
		</div>
		<!-- /Page Body -->
	</div>
	<!-- /Page Content -->
</div>
<!-- /Page Container -->
<!-- Main Container -->

<script type="text/javascript">
function tjGrpForm(){
	 var selectobj = document.getElementById("listUser_id");
	 if(selectobj.options.length==0){
		window.top.layer.alert("分组成员未选择",{
			icon:2,
			title:'<font color="red">警告</font>',
			time:1500,
			closeBtn: 0,
			fix: true, //固定
			move: false,
			})
			
		 return false;
	 }else{
		return true;
	 }
} 
</script>
</body>
</html>
