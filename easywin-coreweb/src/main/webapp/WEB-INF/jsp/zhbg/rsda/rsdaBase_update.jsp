<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
	<div class="widget radius-bordered">
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue">基本信息</span>
			<div class="widget-buttons btn-div-full">
				<a class="ps-point btn-a-full" data-toggle="collapse">
				<i class="fa fa-minus blue"></i> </a>
			</div>
		</div>
		<div class="widget-body no-shadow no-padding">
			<div class="tickets-container bg-white no-padding padding-top-10">
				<form action="/rsdaBase/updateRsdaBaseInfo" method="post" id="baseInfoForm" class="subform">
					<input type="hidden" name="userId" value="${rsdaBase.userId}"/>
					<input type="hidden" name="id" value="${rsdaBase.id}"/>
					<input type="hidden" name="sid" value="${param.sid}"/>
					<input type="hidden" name="redirectPage">
					<table class="table table-hover table-striped table-bordered"  style="width: 100%;border: 0;">
	               		 	<tbody id="bgypPurDetailT0" style="border: 0;">
	               		 		<tr>
	               		 			<td style="width: 15%;height: 40px">OA用户名</td>
									<td style="width: 65%" colspan="3">${rsdaBase.nickName}</td>
	               		 			<td style="width: 20%" colspan="2" rowspan="4">
						 				<img src="/downLoad/userImg/${userInfo.comId}/${rsdaBase.userId}?sid=${param.sid}&size=1" onload="AutoResizeImage(150,0,this,'')"></img>
	               		 			</td>
									
								</tr>
	               		 		<tr>
	               		 			<td style="width: 15%;height: 40px">姓名</td>
									<td style="width: 25%">${rsdaBase.userName}</td>
	               		 			<td style="width: 15%;height: 40px">部门</td>
									<td style="width: 25%">${rsdaBase.depName}</td>
								</tr>
	               		 		<tr>
	               		 			<td style="width: 15%;height: 40px">工号</td>
									<td style="width: 25%">
										<input type="text" name="jobNumber" class="form-control" value="${rsdaBase.jobNumber }">
									</td>
	               		 			<td style="width: 15%;height: 40px">婚姻状况</td>
									<td style="width: 25%">
										<tags:dataDic type="marryStatus" name="marryStatus" value="${rsdaBase.marryStatus}" please="t"></tags:dataDic>
									</td>
								</tr>
	               		 		<tr>
	               		 			<td style="width: 15%;height: 40px">性别</td>
									<td style="width: 25%">
										<tags:dataDic type="gender" name="gender" value="${rsdaBase.gender}" please="t"></tags:dataDic>
									</td>
	               		 			<td style="width: 15%;height: 40px">民族</td>
									<td style="width: 25%">
										<tags:dataDic type="nation" name="nation" value="${rsdaBase.nation}" please="t"></tags:dataDic>
									</td>
								</tr>
	               		 		<tr>
	               		 			<td style="width: 15%;height: 40px">政治面貌</td>
									<td style="width: 25%">
										<tags:dataDic type="politStatus" name="politStatus" value="${rsdaBase.politStatus}" please="t"></tags:dataDic>
									</td>
	               		 			<td style="width: 15%;height: 40px">入党时间</td>
									<td colspan="3">
										<input class="colorpicker-default form-control" readonly="readonly"
											name="politDate" onClick="WdatePicker()" type="text" 
											value="${rsdaBase.politDate}" style="width: 150px">
									</td>
								</tr>
	               		 		<tr>
	               		 			<td style="width: 15%;height: 40px">身份证号</td>
									<td style="width: 25%">
										<input type="text" name="idCardNum" class="form-control" value="${rsdaBase.idCardNum}">
									</td>
	               		 			<td style="width: 15%;height: 40px">籍贯</td>
									<td colspan="3">
										<tags:dataDic type="nativePro" name="nativePro" value="${rsdaBase.nativePro}" 
										please="t" clz="pull-left"></tags:dataDic>
										<input type="text" name="nativeCity" class="form-control" style="width: 200px" value="${rsdaBase.nativeCity}">
									</td>
								</tr>
	               		 		<tr>
	               		 			<td style="width: 15%;height: 40px">现住地址</td>
									<td colspan="5">
										<input type="text" name="homeAdress" class="form-control" style="width: 500px" value="${rsdaBase.homeAdress}">
									</td>
	               		 			
								</tr>
	               		 </table>
	               		 <div class="pull-right">
						    <a href="javascript:void(0);" class="btn btn-primary margin-top-5 margin-right-20" id="updateRsdaBaseInfoBtn">保存</a>
	                	</div>
					</form>
                </div>
		</div>
	</div>
		<div class="widget radius-bordered" style="clear:both">
			<div class="widget-header bg-bluegray no-shadow">
				<span class="widget-caption blue">职位情况及联系方式</span>
				<div class="widget-buttons btn-div-full">
					<a class="ps-point btn-a-full" data-toggle="collapse"> <i
						class="fa fa-minus blue"></i> </a>
				</div>
			</div>
			<div class="widget-body no-shadow no-padding">
				<div class="tickets-container bg-white no-padding">
				<form action="/rsdaBase/updateRsdaBaseJob" method="post" id="baseJobForm" class="subform">
					<input type="hidden" name="rsdaOther.userId" value="${rsdaBase.userId}"/>
					<input type="hidden" name="rsdaOther.id" value="${rsdaBase.id}"/>
					<input type="hidden" name="sid" value="${param.sid}"/>
					<input type="hidden" name="redirectPage">
					<table class="table table-hover table-striped table-bordered"  style="width: 100%;border: 0;">
               		 	<tbody id="bgypPurDetailT0" style="border: 0;">
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">入职时间</td>
								<td style="width: 30%">
									<input class="colorpicker-default form-control" readonly="readonly"
										name="hireDate" onClick="WdatePicker()" type="text" 
										value="${fn:substring(rsdaBase.hireDate,0,10)}" style="width: 150px">
								</td>
               		 			<td style="width: 20%;height: 40px">员工类型</td>
								<td style="width: 30%">
									<tags:dataDic type="employeeType" name="rsdaOther.employeeType" value="${rsdaBase.employeeType}" please="t"></tags:dataDic>
								</td>
							</tr>
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">职务</td>
								<td style="width: 30%">
									<input type="text" name="rsdaOther.dutyName" class="form-control" value="${rsdaBase.dutyName }">
								</td>
               		 			<td style="width: 20%;height: 40px">在职状态</td>
								<td style="width: 30%">
									<tags:dataDic type="payrollType" name="rsdaOther.payrollType" value="${rsdaBase.payrollType }" please="t"></tags:dataDic>
								</td>
							</tr>
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">岗位</td>
								<td style="width: 30%"> 
									<input type="text" name="rsdaOther.gwName" class="form-control" value="${rsdaBase.gwName}">
								</td>
               		 			<td style="width: 20%;height: 40px">职称</td>
								<td style="width: 30%">
									<input type="text" name="rsdaOther.zcName" class="form-control" value="${rsdaBase.zcName}">
								</td>
							</tr>
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">联系电话</td>
								<td style="width: 30%">
									${rsdaBase.userTel }
								</td>
               		 			<td style="width: 20%;height: 40px">邮箱</td>
								<td style="width: 30%">
									${rsdaBase.email }
								</td>
							</tr>
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">紧急联系人</td>
								<td style="width: 30%">
									<input type="text" name="rsdaOther.urgenterName" class="form-control" value="${rsdaBase.urgenterName}">
								</td>
               		 			<td style="width: 20%;height: 40px">紧急联系人电话</td>
								<td style="width: 30%">
									<input type="text" name="rsdaOther.urgenterTel" class="form-control" value="${rsdaBase.urgenterTel}">
								</td>
							</tr>
               		 </table>
               		  <div class="pull-right">
						    <a href="javascript:void(0);" class="btn btn-primary margin-top-5 margin-right-20" id="updateRsdaBaseJobBtn">保存</a>
	                	</div>
					</form>
				</div>
			</div>
		</div>
		<div class="widget radius-bordered" style="clear:both">
			<div class="widget-header bg-bluegray no-shadow">
				<span class="widget-caption blue">教育经历</span>
				<div class="widget-buttons btn-div-full">
					<a class="ps-point btn-a-full" data-toggle="collapse"> <i
						class="fa fa-minus blue"></i> </a>
				</div>
			</div>
			<div class="widget-body no-shadow no-padding">
				<div class="tickets-container bg-white no-padding">
				<form action="/rsdaBase/updateRsdaBaseEdu" method="post" id="baseEduForm" class="subform">
					<input type="hidden" name="userId" value="${rsdaBase.userId}"/>
					<input type="hidden" name="id" value="${rsdaBase.id}"/>
					<input type="hidden" name="sid" value="${param.sid}"/>
					<input type="hidden" name="redirectPage">
					<table class="table table-hover table-striped table-bordered"  style="width: 100%;border: 0;">
               		 	<tbody id="bgypPurDetailT0" style="border: 0;">
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">学历</td>
								<td style="width: 30%">
									<tags:dataDic type="eduFormalType" name="eduFormal" value="${rsdaBase.eduFormal}" please="t"></tags:dataDic>
								</td>
               		 			<td style="width: 20%;height: 40px">学位</td>
								<td style="width: 30%">
									<tags:dataDic type="degreeType" name="degree" value="${rsdaBase.degree}" please="t"></tags:dataDic>
								</td>
							</tr>
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">毕业时间</td>
								<td style="width: 30%">
									<input class="colorpicker-default form-control" readonly="readonly"
											name="graduateDate" onClick="WdatePicker()" type="text" 
											value="${rsdaBase.graduateDate }" style="width: 150px">
								</td>
               		 			<td style="width: 20%;height: 40px">毕业学校</td>
								<td style="width: 30%">
									<input type="text" name="schoolName" class="form-control" value="${rsdaBase.schoolName}">
								</td>
							</tr>
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">专业</td>
								<td style="width: 30%">
									<input type="text" name="major" class="form-control" value="${rsdaBase.major}">
								</td>
               		 			<td style="width: 20%;height: 40px">特长</td>
								<td style="width: 30%">
									<input type="text" name="special" class="form-control" value="${rsdaBase.special}">
								</td>
							</tr>
               		 </table>
               		 <div class="pull-right">
						    <a href="javascript:void(0);" class="btn btn-primary margin-top-5 margin-right-20" id="updateRsdaBaseEduBtn">保存</a>
	                	</div>
					</form>
				</div>
			</div>
		</div>
		<div class="widget radius-bordered" style="clear:both">
			<div class="widget-header bg-bluegray no-shadow">
				<span class="widget-caption blue">其他补充</span>
				<div class="widget-buttons btn-div-full">
					<a class="ps-point btn-a-full" data-toggle="collapse"> <i
						class="fa fa-minus blue"></i> </a>
				</div>
			</div>
			<div class="widget-body no-shadow no-padding">
				<div class="tickets-container bg-white no-padding">
				<form action="/rsdaBase/updateRsdaBaseOhter" method="post" id="baseOtherForm" class="subform">
					<input type="hidden" name="userId" value="${rsdaBase.userId}"/>
					<input type="hidden" name="id" value="${rsdaBase.id}"/>
					<input type="hidden" name="sid" value="${param.sid}"/>
					<input type="hidden" name="redirectPage">
					<div id="formData">
					</div>
					<table class="table table-hover table-striped table-bordered"  style="width: 100%;border: 0;">
               		 	<tbody id="bgypPurDetailT0" style="border: 0;">
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">备注</td>
								<td >
									<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;" 
									id="remark" name="remark" rows="" cols="">${rsdaBase.remark}</textarea>
								</td>
							</tr>
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">附件</td>
								<td>
									<div style="clear:both;width: 300px" class="wu-example">
									    <!--用来存放文件信息-->
									    <div id="thelistlistRsdaBaseFiles_upfileId" class="uploader-list">
									    	<c:choose>
												<c:when test="${not empty rsdaBase.listRsdaBaseFiles}">
													<c:forEach items="${rsdaBase.listRsdaBaseFiles}" var="upfile"
														varStatus="vs">
														<div id="wu_file_0_-${upfile.upfileId}"
															class="uploadify-queue-item">
															<div class="cancel">
																<a href="javascript:void(0)" fileDone="1"
																	fileId="${upfile.upfileId}">X</a>
															</div>
															<span class="fileName" title="${upfile.fileName}"> <tags:cutString
																	num="25">${upfile.fileName}</tags:cutString>(已有文件) </span> <span
																class="data"> - 完成</span>
															<div class="uploadify-progress">
																<div class="uploadify-progress-bar" style="width: 100%;"></div>
															</div>
														</div>
													</c:forEach>
												</c:when>
											</c:choose>
									    </div>
									    <div class="btns btn-sm">
									        <div id="pickerlistRsdaBaseFiles_upfileId" class="webuploader-container">
									        	选择文件
									        </div>
									    </div>
										<script type="text/javascript">
											loadWebUpfiles('listRsdaBaseFiles_upfileId','${param.sid}','',"pickerlistRsdaBaseFiles_upfileId","thelistlistRsdaBaseFiles_upfileId",'filelistRsdaBaseFiles_upfileId')
										</script>
										<div style="position: relative; width: 350px; height: 90px;display: none">
											<div style="float: left; width: 250px">
												<select list="listRsdaBaseFiles" listkey="upfileId" listvalue="fileName" id="listRsdaBaseFiles_upfileId" 
													ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">
													<c:choose>
														<c:when test="${not empty rsdaBase.listRsdaBaseFiles}">
															<c:forEach items="${rsdaBase.listRsdaBaseFiles}"
																var="upfile" varStatus="vs">
																<option selected="selected" value="${upfile.upfileId}">${upfile.fileName}</option>
															</c:forEach>
														</c:when>
													</c:choose>
												 </select>
											</div>
										</div>
									</div>
								</td>
							</tr>
               		 </table>
               		 <div class="pull-right">
						    <a href="javascript:void(0);" class="btn btn-primary margin-top-5 margin-right-20" id="updateRsdaBaseOtherBtn">保存</a>
	                	</div>
					</form>
				</div>
			</div>
		</div>
</body>
</html>
