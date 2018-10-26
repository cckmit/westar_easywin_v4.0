<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">

<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>

<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
</head>
<body  scroll="no" style="background-color: #fff">

<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">人员多选</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;padding:5px">
					
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr>
							<td height="27" align="left">
								<table width="98%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td align="left">
											<form id="subform" action="/common/userMoreSelect/userTable" target="usertree">
											    <input type="hidden" name="sid" value="${param.sid }" />
												<img src="/static/images/ico_p.gif" width="20" height="14" align="left"/> 
												&nbsp;&nbsp;<a href="javascript:selectAll();">按团队</a> 
												&nbsp;&nbsp;<a href="javascript:selectGroup();">按分组</a>
												&nbsp;&nbsp;<a href="javascript:selectRole();">按角色</a>
												&nbsp;&nbsp;<a href="javascript:myRootOrg();">本部门</a>
												&nbsp;&nbsp;<input style="height: 20px;border:1px solid #dddddd;" name="queryName" id="queryName" onblur="blurName(this);" onfocus="focusName(this);" tip="请输入机构名称" value="请输入机构名称" class="gray" onkeydown="enter();" sty/>
											    <img style="cursor: pointer;" onclick="enterClick();" src="/static/images/cx.png"/>
												&nbsp;&nbsp; <input datatype="*" onkeydown="enterUserName();" onblur="blurName(this);" onfocus="focusName(this);" tip="请输入姓名" value="请输入姓名" class="gray" id="anyNameLike" name="anyNameLike" style="height: 20px;border:1px solid #dddddd;" />
												<img style="cursor: pointer;" onclick="submitForm();" src="/static/images/cx.png"/>
										   </form>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td style="padding-right: 10px;">
								<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
									<tr bgcolor="#FFFFFF">
										<td style="padding-left: 1px;height: 400px"></td>
										<td width="220px;"><iframe id="deptree" name="deptree" style="width: 100%;height:400px; border: 1px solid #86B1E8;" frameborder="0" src="/common/userMoreSelect/orgTreePage?sid=${param.sid }"></iframe></td>
										<td style="padding-left: 1px;"></td>
										<td bgcolor="#FFFFFF" width="370px">
											<div style="border: 1px solid #86B1E8; width: 99%; height: 100%;">
												<iframe id="usertree" name="usertree" style="height: 100%;height:400px; width: 100%" frameborder="0" src=""></iframe>
											</div>
										</td>
										<td style="padding-left: 1px;"></td>
										<td height="100%" width="110px">
											<div style="border: 1px solid #86B1E8; width: 100%;">
												<form name="form1" style="margin: 0 auto; padding: 0px;">
													<table width="100%">
														<tr>
															<td bgcolor="#FFFFFF" valign="top">
																<div style="border: 1px solid #ffffff; width: 100%; text-align: center; padding-bottom: 0px">
																	<select name="userselect" id="userselect" ondblclick="removeClick(this.id)" style="width: 99%;height:400px;  margin: -10 -10 -10 -10;" multiple="multiple">
																	</select>
																</div>
															</td>
														</tr>
													</table>
												</form>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>

	
</body>
<script type="text/javascript">
    //根据机构查询人员
	function listuser(anyDepId) {
		usertree.location.href = "/common/userMoreSelect/userTable?anyDepId="+anyDepId+"&sid=${param.sid}&forMeDoDis=${param.forMeDoDis}&onlySubState=${param.onlySubState}";
	}
	
	//根据分组查询人员
    function listUserByGroup(groupId){
    	usertree.location.href = "/common/userMoreSelect/group/userTable?groupId="+ groupId+"&sid=${param.sid}&forMeDoDis=${param.forMeDoDis}&onlySubState=${param.onlySubState}";
    }

    //根据角色查询人员
    function listUserByRole(roleId){
        usertree.location.href = "/common/role/userTable?roleId="+ roleId+"&sid=${param.sid}&forMeDoDis=${param.forMeDoDis}&onlySubState=${param.onlySubState}&isMore=1";
    }
	
	function appendUser(str) {
		var json = eval('(' + str + ')');
		if ($("#userselect option[value='" + json.userid + "']").length == 0) {
			$('#userselect').append(
					"<option value='"+json.userid+"'>" + json.username
							+ "</option>");
		}
	}
	function returnUser() {
		var selectobj = document.getElementById("userselect");
		var options = selectobj.options;
		return options;
	}
	//设置选项
	function setOptions(o){
		removeOptions("userselect");
		if(o!=null){
			for ( var i = 0; i < o.length; i++) {
				$('#userselect')
						.append(
								"<option value='"+o[i].value+"'>" + o[i].text
										+ "</option>");
			}
		}
	}
	
	$(function() {
		usertree.location.href = "/common/userMoreSelect/group/userTable?sid=${param.sid}&forMeDoDis=${param.forMeDoDis}&onlySubState=${param.onlySubState}";
	})
	
	//查询提交
	function submitForm(){
		$('#subform').submit();
	}
	
	//姓名查询 回车
	function enterUserName(){
		if(event.keyCode==13&&$.trim($('#anyNameLike').val())!=""){
			  $('#subform').submit();
		  }
	  }
	
  function enter(){
	  if(event.keyCode==13){
		 var q = $('#queryName').val();
		 deptree.enter(q);
		 $('#queryName').select();
	  }
  }
  
  function enterClick(){
	 var q = $('#queryName').val();
	 deptree.enter(q);
	  $('#queryName').select();
  }
  
  //本部门
  function myRootOrg(){
	  deptree.myRootOrg();
  }
  //所有
  function selectAll(){
	  deptree.initZtree();
  }
  //分组
  function selectGroup(){
	  deptree.initGroup();
  }
  //角色
	function selectRole(){
        deptree.initRole();
	}
</script>
</html>
