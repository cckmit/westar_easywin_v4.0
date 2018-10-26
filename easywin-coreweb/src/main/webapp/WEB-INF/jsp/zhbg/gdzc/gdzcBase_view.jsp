<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
</head>
<body>
	<div class="widget-body no-shadow" style="padding:0 12px;">
		<div class="widget radius-bordered">
			<div class="widget-header bg-bluegray no-shadow">
				<span class="widget-caption blue">基础信息</span>
				<div class="widget-buttons btn-div-full">
					<a class="ps-point btn-a-full" data-toggle="collapse"> <i class="fa fa-minus blue"></i>
					</a>
				</div>
			</div>
			<div class="widget-body no-shadow">
				<div class="tickets-container bg-white">
					<ul class="tickets-list">
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">
								&nbsp;资产编号
								
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
								${gdzc.gdzcNum }
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">
								&nbsp;资产名称
								
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
								${gdzc.gdzcName }
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline" style="clear:both">
							<div class="pull-left gray ps-left-text">
								&nbsp;资产类型
								
							</div>
							<div class="ticket-user pull-left ps-right-box">
							${gdzc.ssTypeName }
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">
								&nbsp;所属部门
								
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<span id="depName" style="cursor:pointer;" class="label label-default margin-right-5 margin-bottom-5">${gdzc.depName }</span>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">
								&nbsp;保管人
								
							</div>
							<div class="ticket-user pull-left ps-right-box">
							${gdzc.managerName}
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">
								&nbsp;添加日期
								
							</div>
							<div class="ticket-user pull-left ps-right-box">
							${gdzc.addDate }
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline" style="clear:both">
							<div class="pull-left gray ps-left-text">
								&nbsp;添加类型
								
							</div>
							<div class="ticket-user pull-left ps-right-box">
							${gdzc.addTypeName }
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">
								&nbsp;资产原值(元)
								
							</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
								${gdzc.gdzcPrice }
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;资产残值率(%)</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
								${gdzc.residualRate }
								</div>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline">
							<div class="pull-left gray ps-left-text">&nbsp;资产折旧年限</div>
							<div class="ticket-user pull-left ps-right-box">
								<div class="pull-left">
								${gdzc.depreciationYear }
								</div>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
