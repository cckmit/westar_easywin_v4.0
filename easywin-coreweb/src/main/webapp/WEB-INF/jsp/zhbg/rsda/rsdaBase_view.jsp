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
					<table class="table table-hover table-striped table-bordered"  style="width: 100%;border: 0;">
	               		 	<tbody id="bgypPurDetailT0" style="border: 0;">
	               		 		<tr>
	               		 			<td style="width: 15%;height: 40px">OA用户名</td>
									<td style="width: 65%" colspan="3">${rsdaBase.nickName}</td>
	               		 			<td style="width: 20%" colspan="2" rowspan="4">
						 				<img src="/downLoad/userImg/${userInfo.comId}/${rsdaBase.userId}?sid=${param.sid}&size=1"  onload="AutoResizeImage(150,0,this,'')"></img>
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
										${rsdaBase.jobNumber }
									</td>
	               		 			<td style="width: 15%;height: 40px">婚姻状况</td>
									<td style="width: 25%">
										${rsdaBase.marryStatusName}
									</td>
								</tr>
	               		 		<tr>
	               		 			<td style="width: 15%;height: 40px">性别</td>
									<td style="width: 25%">
										<c:choose>
											<c:when test="${empty rsdaBase.gender}">
												保密
											</c:when>
											<c:when test="${rsdaBase.gender==1}">
												男
											</c:when>
											<c:when test="${rsdaBase.gender==0}">
												女
											</c:when>
										</c:choose>
									</td>
	               		 			<td style="width: 15%;height: 40px">民族</td>
									<td style="width: 25%">
										${rsdaBase.nationName}
									</td>
								</tr>
	               		 		<tr>
	               		 			<td style="width: 15%;height: 40px">政治面貌</td>
									<td style="width: 25%">
										${rsdaBase.politStatusName}
									</td>
	               		 			<td style="width: 15%;height: 40px">入党时间</td>
									<td colspan="3">
										${rsdaBase.politDate}
									</td>
								</tr>
	               		 		<tr>
	               		 			<td style="width: 15%;height: 40px">身份证号</td>
									<td style="width: 25%">
										${rsdaBase.idCardNum}
									</td>
	               		 			<td style="width: 15%;height: 40px">籍贯</td>
									<td colspan="3">
										${rsdaBase.nativeProName}${rsdaBase.nativeCity}
									</td>
								</tr>
	               		 		<tr>
	               		 			<td style="width: 15%;height: 40px">现住地址</td>
									<td colspan="5">
										${rsdaBase.homeAdress}
									</td>
	               		 			
								</tr>
	               		 </table>
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
					<table class="table table-hover table-striped table-bordered"  style="width: 100%;border: 0;">
               		 	<tbody id="bgypPurDetailT0" style="border: 0;">
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">入职时间</td>
								<td style="width: 30%">
									${fn:substring(rsdaBase.hireDate,0,10)}
								</td>
               		 			<td style="width: 20%;height: 40px">员工类型</td>
								<td style="width: 30%">
									${rsdaBase.employeeTypeName}
								</td>
							</tr>
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">职务</td>
								<td style="width: 30%">
									${rsdaBase.dutyName }
								</td>
               		 			<td style="width: 20%;height: 40px">在职状态</td>
								<td style="width: 30%">
									${rsdaBase.payrollTypeName}
								</td>
							</tr>
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">岗位</td>
								<td style="width: 30%"> 
									${rsdaBase.gwName}
								</td>
               		 			<td style="width: 20%;height: 40px">职称</td>
								<td style="width: 30%">
									${rsdaBase.zcName}
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
									${rsdaBase.urgenterName}
								</td>
               		 			<td style="width: 20%;height: 40px">紧急联系人电话</td>
								<td style="width: 30%">
									${rsdaBase.urgenterTel}
								</td>
							</tr>
               		 </table>
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
					<table class="table table-hover table-striped table-bordered"  style="width: 100%;border: 0;">
               		 	<tbody id="bgypPurDetailT0" style="border: 0;">
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">学历</td>
								<td style="width: 30%">
									${rsdaBase.eduFormalName}
								</td>
               		 			<td style="width: 20%;height: 40px">学位</td>
								<td style="width: 30%">
									${rsdaBase.degreeName}
								</td>
							</tr>
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">毕业时间</td>
								<td style="width: 30%">
									${rsdaBase.graduateDate }
								</td>
               		 			<td style="width: 20%;height: 40px">毕业学校</td>
								<td style="width: 30%">
									${rsdaBase.schoolName}
								</td>
							</tr>
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">专业</td>
								<td style="width: 30%">
									${rsdaBase.major}
								</td>
               		 			<td style="width: 20%;height: 40px">特长</td>
								<td style="width: 30%">
									${rsdaBase.special}
								</td>
							</tr>
               		 </table>
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
					<table class="table table-hover table-striped table-bordered"  style="width: 100%;border: 0;">
               		 	<tbody id="bgypPurDetailT0" style="border: 0;">
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">备注</td>
								<td >
									${rsdaBase.remark}
								</td>
							</tr>
               		 		<tr>
               		 			<td style="width: 20%;height: 40px">附件</td>
								<td>
									<c:choose>
										<c:when test="${not empty rsdaBase.listRsdaBaseFiles}">
											<div class="file_div">
											<c:forEach items="${rsdaBase.listRsdaBaseFiles}" var="rsdaBaseFiles" varStatus="vs">
												<c:choose>
													<c:when test="${rsdaBaseFiles.isPic==1}">
														<p class="p_text">
														附件(${vs.count})：
															<img onload="AutoResizeImage(300,0,this,'')"
															src="/downLoad/down/${rsdaBaseFiles.fileUuid}/${rsdaBaseFiles.fileName}?sid=${param.sid}" />
								 						<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/${rsdaBaseFiles.fileUuid}/${rsdaBaseFiles.fileName}?sid=${param.sid}" title="下载"></a>
								 						<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);"onclick="showPic('/downLoad/down/${rsdaBaseFiles.fileUuid}/${rsdaBaseFiles.fileName}','${param.sid}','${rsdaBaseFiles.upfileId}','02801','${rsdaBase.id }')"></a>
														</p>
													</c:when>
													<c:otherwise>
														<p class="p_text">
														附件(${vs.count})：
															${rsdaBaseFiles.fileName}
														<c:choose>
										 					<c:when test="${rsdaBaseFiles.fileExt=='doc' || rsdaBaseFiles.fileExt=='docx' || rsdaBaseFiles.fileExt=='xls' || rsdaBaseFiles.fileExt=='xlsx' || rsdaBaseFiles.fileExt=='ppt' || rsdaBaseFiles.fileExt=='pptx' }">
												 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${rsdaBaseFiles.fileUuid}','${rsdaBaseFiles.fileName}','${param.sid}')"></a>
												 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${rsdaBaseFiles.upfileId}','${rsdaBaseFiles.fileUuid}','${rsdaBaseFiles.fileName}','${rsdaBaseFiles.fileExt}','${param.sid}','02801','${rsdaBase.id }')"></a>
										 					</c:when>
										 					<c:when test="${rsdaBaseFiles.fileExt=='txt' ||rsdaBaseFiles.fileExt=='pdf'}">
										 						<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/${rsdaBaseFiles.fileUuid}/${rsdaBaseFiles.fileName}?sid=${param.sid}" title="下载"></a>
												 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${rsdaBaseFiles.upfileId}','${rsdaBaseFiles.fileUuid}','${rsdaBaseFiles.fileName}','${rsdaBaseFiles.fileExt}','${param.sid}','02801','${rsdaBase.id}')"></a>
										 					</c:when>
										 					<c:otherwise>
												 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${rsdaBaseFiles.fileUuid}','${rsdaBaseFiles.fileName}','${param.sid}')"></a>
										 					</c:otherwise>
										 				</c:choose>
														</p>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</div>
										</c:when>
										<c:otherwise>
											未上传附件
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
               		 </table>
				</div>
			</div>
		</div>
</body>
</html>
