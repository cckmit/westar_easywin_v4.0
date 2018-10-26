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
			<span class="widget-caption blue">基础信息</span>
			<div class="widget-buttons btn-div-full">
				<a class="ps-point btn-a-full" data-toggle="collapse"> <i
					class="fa fa-minus blue"></i> </a>
			</div>
		</div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white">
				<ul class="tickets-list">
					<li class="ticket-item no-shadow ps-listline">
					    <div class="pull-left gray ps-left-text">
					    	项目名称
					    </div>
						<div class="ticket-user pull-left ps-right-box">
						  	<div class="pull-left">
								<input id="itemName" datatype="input,sn" defaultLen="52" name="itemName" nullmsg="请填写项目名称"  value="${item.itemName }"
								class="colorpicker-default form-control" type="text" value="" style="width: 400px;float: left">
								<span id="msgTitle" style="float:left;width: auto;">(0/26)</span>
							</div>
							<div class="pull-left">
								<span id="addItemWarm" style="float:left;margin-left:2px;"></span>
							</div>
						</div>
						<script> 
							//当状态改变的时候执行的函数 
							function handleName(){
								var value = $('#itemName').val();
								var len = charLength(value);
								if(len%2==1){
									len = (len+1)/2;
								}else{
									len = len/2;
								}
								if(len>26){
									$('#msgTitle').html("(<font color='red'>"+len+"</font>/26)");
								}else{
									$('#msgTitle').html("("+len+"/26)");
								}
							} 
							//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
							if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
								document.getElementById('itemName').onpropertychange=handleName 
							}else {//非ie浏览器，比如Firefox 
								document.getElementById('itemName').addEventListener("input",handleName,false); 
							} 
						</script>  
                     </li>
                     <li class="ticket-item no-shadow ps-listline" style="clear:both">
					    <div class="pull-left gray ps-left-text">
					    	项目经理
					    </div>
						<div class="ticket-user pull-left ps-right-box">
							<input type="hidden" id = "owner" value="${item.owner }">
							
									<img style="float: left" src="/downLoad/userImg/${item.comId}/${item.owner}?sid=${param.sid}" title="${item.ownerName}" class="user-avatar" />
							<span class="user-name">${item.ownerName}</span>
						</div>
                      </li>
                      <li class="ticket-item no-shadow ps-listline" style="clear:both">
					    <div class="pull-left gray ps-left-text">
					    	研发负责人
					    </div>
						<div class="ticket-user pull-left ps-right-box" style="min-width: 135px">
						<c:choose>
							<c:when test="${not empty item.developLeader}">
								<tags:userOne datatype="s" name="developLeader"  comId="${userInfo.comId}"  defaultInit="true"
									showValue="${item.developLeaderName}" value="${item.developLeader}" onclick="true"
									showName="developLeaderName" callBackStart="yes"></tags:userOne>
							</c:when>
							<c:otherwise>
								<tags:userOne datatype="s" name="developLeader"  comId="${userInfo.comId}"
									showValue="${item.developLeaderName}" value="${item.developLeader}" onclick="true"
									showName="developLeaderName" callBackStart="yes"></tags:userOne>
							</c:otherwise>
						</c:choose>
						
						</div>
						
                       </li>
                       <li class="ticket-item no-shadow ps-listline" style="clear:both">
					    <div class="pull-left gray ps-left-text">
					    	所属产品
					    </div>
						<div class="ticket-user pull-left ps-right-box">
							<input type="hidden" id="productId" name="productId" value="${item.productId }"/>
							<div style="float: left">
								<input id="productName" name="productName" class="colorpicker-default form-control pull-left" type="text" 
													ondblclick="removePro()" readonly style="cursor:auto;width: 250px;float:left" value="${item.productName }" title="双击移除">
													<a href="javascript:void(0)" class="fa fa-chain pull-left" style="float:left;margin-top: 10px;font-size: 18px;padding-left: 10px"  
													onclick="proSelect();"></a>
							</div>
						</div>  
					    <div class="ticket-user pull-right ps-right-box" style="min-width: 180px;margin-right: 20px;" >
							<input type="text" style="width: 180px;" id="productManager" readonly value="${item.productManagerName }">
						</div>
						<div class="pull-right gray ps-right-text" style="margin-right: 20px;">
					    	产品经理
					    </div>
                      </li>
                       <li class="ticket-item no-shadow ps-listline" style="clear:both">
					    <div class="pull-left gray ps-left-text">
					    	关联客户
					    </div>
						<div class="ticket-user pull-left ps-right-box">
							<input type="hidden" id="partnerId" name="partnerId"  value="${item.partnerId }"/>
							<div style="float: left">
								<input id="partnerName" name="partnerName" class="colorpicker-default form-control pull-left" type="text"  value="${item.partnerName }"
								ondblclick="removeCrm()" readonly style="cursor:auto;width: 250px;float:left"  title="双击选择">
								<a href="javascript:void(0)" class="fa fa-chain pull-left" style="float:left;margin-top: 10px;font-size: 18px;padding-left: 10px"  
								onclick="listCrm('${param.sid}','partnerId');"></a>
							</div>
						</div>               
                      </li>
                      <li class="ticket-item no-shadow ps-listline" style="clear:both">
						<div class="pull-left gray ps-left-text">
							&nbsp;父级项目
						</div>
						<div class="ticket-user pull-left ps-right-box">
							<c:choose>
								<c:when test="${4==item.state || not empty editItem}">
														${item.pItemName}
													</c:when>
								<c:otherwise>
									<input type="hidden" id="pItemId" name="pItemId"
										value="${item.parentId}" />
									<div style="float: left">
										<input id="pItemName" name="pItemName"
											class="colorpicker-default form-control pull-left"
											type="text" ondblclick="removePItem()" readonly
											style="cursor:auto;width: 250px;float:left"
											value="${item.pItemName}" title="双击选择"> <a
											href="javascript:void(0)" class="fa fa-chain pull-left"
											style="float:left;margin-top: 10px;font-size: 18px;padding-left: 10px"
											onclick="listItem('${param.sid}','pItemId');"></a>
									</div>
								</c:otherwise>
							</c:choose>
						</div></li>
                      <li class="ticket-item no-shadow ps-listline" style="clear:both">
					    <div class="pull-left gray ps-left-text">
					    	项目金额
					    </div>
						<div class="ticket-user pull-left ps-right-box" style="min-width: 250px" >
							<input type="text" style="width: 250px;" name="amount" id="amount"  value="${item.amount }">&nbsp;万
						</div>
						<div class="ticket-user pull-right ps-right-box" style="min-width: 180px;margin-right: 20px;">
							<input type="text" class="form-control" placeholder="服务期限" readonly="readonly" id="serviceDate" name="serviceDate" 
							onFocus="WdatePicker({onpicked:function(dp){selectDate(dp.cal.getNewDateStr())},minDate:'%y-%M-{%d}',dateFmt:'yyyy-MM-dd'})"  
							preval="${item.serviceDate }" value="${item.serviceDate }">
						</div>
						 <div class="pull-right gray ps-right-text" style="margin-right: 20px;">
					    	服务期限
					    </div>
                      </li>
                      <li class="ticket-item no-shadow autoHeight no-padding" >
				    	<div class="pull-left gray ps-left-text padding-top-10">
				    		项目备注
				    	</div>
						<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
					  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;margin-left: 0" id="itemRemark" name="itemRemark" rows="" cols="">${item.itemRemark }</textarea>
						</div> 
						<div class="ps-clear"></div>              
                    </li>
                    
                    <li  class="ticket-item no-shadow autoHeight no-padding">
                    	<div class="pull-left gray ps-left-text padding-top-10">
				    		项目进度
				    	</div>
						<div class="pull-left gray ps-left-text padding-top-10" style="height: auto;margin-left: 40px;">
					  		<div class="wedgt">
								<div class="fm_progress">
									<c:forEach items="${item.listItemProgress}" var="obj" varStatus="vs">
										<div class="pull-left itemDiv">
											<div class="quan pull-left" >
												<span ${(not empty obj.startTime )?'class="active"':''}></span>
											</div>
											<c:if test="${item.listItemProgress.size() != vs.count}">
												<div class="line pull-left">
													<span ${(not empty obj.startTime &&  not empty obj.finishTime)?'class="active"':''}></span>
												</div>
											</c:if>
											<c:if test="${item.listItemProgress.size() == vs.count}">
												<div class="line pull-left">
													<span></span>
												</div>
											</c:if>
											<div class="ps-clear"></div>
											<div class="lc_name">
												<c:choose>
													<c:when test="${not empty obj.startTime && empty obj.finishTime}">
														<span class="active">${obj.progressName }</span>
													</c:when>
													<c:otherwise>
														<span ${(not empty obj.startTime )?'class="active"':''} onclick="updateProgress('${obj.id}','${obj.startTime }')" style="cursor: pointer;" onmouseover="this.style.color='#58B5E3'" onmouseout="this.style.color=''">${obj.progressName }</span>
													</c:otherwise>
												</c:choose>
											</div>
											<div class="per_name">
												<span>${obj.userName }</span>
											</div>
											<div class="lc_time">
												<span>${obj.startTime }</span>
											</div>
										</div>
									</c:forEach>
									<div class="pull-left itemDiv">
										<div class="quan pull-left">
											<span></span>
										</div>
										<div class="ps-clear"></div>
										<div class="lc_name">
											<span onclick="remarkItemState(-4,${item.id},${item.state });" style="cursor: pointer;" onmouseover="this.style.color='red'" onmouseout="this.style.color=''">关闭</span>
										</div>
										<div class="per_name">
											<span></span>
										</div>
										<div class="lc_time">
											<span></span>
										</div>
									</div>
								</div>
							</div>
						</div> 
						<div class="ps-clear"></div>   
                    </li>
                    
						<li class="ticket-item no-shadow ps-listline">
					    <div class="pull-left gray ps-left-text">
					    	&nbsp;阅读权限
					    </div>
						<div class="ticket-user pull-left ps-right-box">
						<c:choose>
							<c:when test="${not empty editItem}">
								<c:if test="${item.pubState== 0}">
									私有
								</c:if>
							 	<c:if test="${item.pubState== 1}">
									公开
								</c:if>
							</c:when>
							<c:otherwise>
								<div class="pull-left">
							  		<select class="populate" id="pubState" name="pubState" onchange="showMans()">
										<option value="0" ${(item.pubState== 0)? "selected='selected'":''}>私有</option>
										<option value="1" ${(item.pubState== 1)? "selected='selected'":''}>公开</option>
										</select>
								</div>
							</c:otherwise>
						</c:choose>
						</div>
	                </li>
				
					<li class="ticket-item no-shadow autoHeight no-padding" id="mans" style="display: block;">
						<div class="pull-left gray ps-left-text padding-top-10">
							&nbsp;分享人</div>
						<div class="ticket-user pull-left ps-right-box"
							style="height: auto;">
							<div class="pull-left gray ps-left-text padding-top-10">
								<input type="hidden" id="itemSharerJson" name="itemSharerJson"
									value="${item.itemSharerJson}" />
								<tags:userMore name="listItemSharer.userId" showName="userName"
									disDivKey="itemSharor_div" callBackStart="yes"></tags:userMore>
							</div>
						</div>
						<div class="ps-clear"></div></li>
				</ul>
			</div>
		</div>
	</div>


	<div class="widget radius-bordered collapsed">
		<div class="widget-header bg-bluegray no-shadow">
			<span class="widget-caption blue">子项目</span>

			<div class="widget-buttons btn-div-full">
				<a class="ps-point btn-a-full" data-toggle="collapse"> <i
					class="fa fa-plus blue"></i> </a>
			</div>
		</div>
		<div class="widget-body no-shadow">
			<div class="tickets-container bg-white">
				<table class="table table-hover table-striped table-bordered table0"
					id="allSonItem" style="width: 100%">
					<thead>
						<tr>
							<th width="14%">序号</th>
							<th>项目名称</th>
							<th width="14%">项目经理</th>
							<th width="14%">项目状态</th>
						</tr>
					</thead>
					<tbody id="sonItem">
						<c:choose>
							<c:when test="${not empty listSonItem}">
								<c:forEach items="${listSonItem}" var="sonItem"
									varStatus="status">
									<tr>
										<td>${status.count}</td>
										<td><a href="javascript:void(0)"
											onclick="viewSonItem(${sonItem.id})">${sonItem.itemName}</a>
										</td>
										<td>
											<div class="ticket-user pull-left other-user-box">
														<img
															src="/downLoad/userImg/${userInfo.comId}/${sonItem.owner}?sid=${param.sid}"
															title="${sonItem.ownerName}" class="user-avatar" />
												<span class="user-name">${sonItem.ownerName}</span>
											</div></td>
										<td><input type="hidden" name="sonItemState"
											value="${empty sonItem.state?'1':sonItem.state}"> <c:choose>
												<c:when test="${sonItem.state==1}">进行中</c:when>
												<c:when test="${sonItem.state==3}">挂起中</c:when>
												<c:when test="${sonItem.state==4}">已完结</c:when>
												<c:otherwise>
																			正常
																		</c:otherwise>
											</c:choose></td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="4" style="text-align: center;">没有子项目</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
