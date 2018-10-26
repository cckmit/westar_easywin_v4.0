<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>

<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script> 
var sid="${param.sid}";
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	$("#qasTalk").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active")
		//问题回答
		$("#otherIframe").attr("src", "/qas/ansQuesPage?sid=${param.sid}&quesId=${ques.id}")
	});
	$("#qasLog").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active")
		//问答日志
		$("#otherIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${ques.id}&busType=011&ifreamName=otherIframe");
	});
	$("#qasFile").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active")
		//问答附件
		$("#otherIframe").attr("src", "/qas/quesFilePage?sid=${param.sid}&pager.pageSize=10&quesId=${ques.id}")
	});
	$("#qasViewRecord").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active")
		//问答附件
		$("#otherIframe").attr("src", "/common/listViewRecord?sid=${param.sid}&busId=${ques.id}&busType=011&ifreamName=otherIframe")
	});
	//任务聊天室
	$("#qasChatLi").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$("#qasChatLi").attr("class","active");
		$("#otherIframe").attr("src","/chat/listBusChat?sid=${param.sid}&busId=${ques.id}&busType=011&ifreamTag=otherIframe");
	});
});

$(document).ready(function() {
	$("#content").autoTextarea({minHeight:65,maxHeight:150});  
	
});
var vali;
$(function() {
	vali=$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError:true
	});
	$("#content").bind("paste cut keydown keyup focus blur",function(event){
    });
});
//删除提问
function delQues(quesId,ts){
	var onclickSrc = $(ts).attr("onclick");
			
	window.top.layer.confirm("确定需要删除提问?", {
		btn: ['确定','取消']//按钮
	  ,title:'询问框'
	  ,icon:3
	}, function(index){
		window.top.layer.close(index);
		 //异步删除提问
	    $("#quesForm").ajaxSubmit({
	        type:"post",
	        url:"/qas/ajaxDelQues?sid=${param.sid}&t="+Math.random(),
	        dataType: "json", 
	        data:{"quesId":quesId},
	        beforeSubmit:function(){
	        	$(ts).removeAttr("onclick");
	        },
	        success:function(data){
		        if(data.state=='y'){
			        window.top.location.reload();
		        }
		        $(ts).attr("onclick",onclickSrc);
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
		        $(ts).attr("onclick",onclickSrc);
	        }
	    });
	   });
}
/**关闭问题
 * state 0 关闭 1开启
 */

function closeQues(quesId,state,ts){
	
	
	var conf = "确定关闭提问?"
	if('0'==state){//问题已经开放
		conf = "确定开放问题?";
	}
	window.top.layer.confirm(conf, {
		  btn: ['确定','取消']//按钮
	  ,title:'询问框'
	  ,icon:3
	}, function(index){
		window.top.layer.close(index)
		 //异步删除提问
	    $("#quesForm").ajaxSubmit({
	        type:"post",
	        url:"/qas/closeQues?sid=${param.sid}&t="+Math.random(),
	        dataType: "json", 
	        data:{"id":quesId,"state":(state+1)%2},
	        beforeSubmit:function(){
	        	$(ts).attr("disabled","disabled")
	        },
	        success:function(data){
		        if(data.state=='y'){
		        	showNotification(1,"操作成功")
		        	if(1==state){//问题已经开放
				 		$(ts).attr("onclick","closeQues("+quesId+",0,this)");
				 		$(ts).addClass("red")
				 		$(ts).removeClass("green")
				 		$(ts).html('<i class="fa fa-lock"></i>重启');
				     }else{//问题已关闭
					 	$(ts).attr("onclick","closeQues("+quesId+",1,this)");
					 	$(ts).removeClass("red")
				 		$(ts).addClass("green")
				 		$(ts).html('<i class="fa fa-unlock"></i>关闭');
				     }
				    //ifream重新加载
			        var src = $("#otherIframe").attr("src");
					 //若是当前的ifream展示为讨论则重新加载讨论
		        	 if(src.indexOf("ansQuesPage")>=0){
		        		 var ansNum = $("#ansNum").val();
		        		 var cnAns = $("#cnAns").val();
		        		 otherIframe.window.changeStyle(state,ansNum,cnAns);
		        	 }else if(src.indexOf("quesLogPage")>=0){
			        	 $("#otherIframe").attr("src", "/qas/quesLogPage?sid=${param.sid}&pager.pageSize=10&quesId=${ques.id}")
			         }
		        }else{
		        	showNotification(2,"操作失败")
		        }
		        $(ts).removeAttr("disabled")
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
		        $(ts).removeAttr("disabled")
	        }
	    });
	   },function(index){
		   if(1==state){
			   $(ts).attr("checked","checked")
		   }else{
			   $(ts).removeAttr("checked") 
		   }
		   $(ts).removeAttr("disabled")
	   });
}
//追加问题
function appQues(){
	var display = $("#questionShow").css("display");
	if(display.indexOf('block')>=0){
		$("#questionShow").hide();
		$("#questionEdit").show();
	}
}
/**
 * 取消问题内容的编辑
 */
function cancleAppQues(){
	$("#questionShow").show();
	$("#questionEdit").hide();
}

function updateQues(ts,id){

	//补充的问题的长度
	var contLen = $('#content').val().replace(/\s+/g,"").length;
	if(contLen>1500){
		layer.tips('补充的问题太长！', "#content", {tips: 1});
		$('#content').focus();
		return;
	}
	//防止重复提交
	var onclick = $(ts).attr("onclick");
	
	 //异步提交表单
    $("#quesForm").ajaxSubmit({
        type:"post",
        url:"/qas/updateQues?sid=${param.sid}&t="+Math.random(),
        beforeSubmit:function(a,o,f){
        	$(ts).removeAttr("onclick");
        },
        dataType: "json", 
        traditional :true,
        data:{"id":id,"fileIds":$("#listQuesFiles_id").val()},
        success:function(data){
	         var state = data.state;
	         if(state=='y'){
		         window.self.location.reload();
	         }
        },
        error:function(XmlHttpRequest,textStatus,errorThrown){
			//可以再次提交
        	$(ts).attr("onclick",onclick);
        	showNotification(2,"系统错误！")
        	
        }
    });
	
}
</script>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">	

		<input id="ansNum" type="hidden" value="${ques.ansNum }"/>
		<input id="cnAns" type="hidden" value="${ques.cnAns}"/>
		
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget no-margin-bottom" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                		<a href="javascript:void(0)" class="widget-caption blue padding-right-5"
               				attentionState="${ques.attentionState}" busType="011" busId="${ques.id}" describe="0" iconSize="sizeMd"
               				onclick="changeAtten('${param.sid}',this)" title="${ques.attentionState==0?"关注":"取消"}">
							<i class="fa ${ques.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>
						</a>
						
                        <span class="widget-caption themeprimary" style="font-size: 20px">问答详情</span>
						<c:if test="${ques.userId==userInfo.id}">
	                        <div class="widget-buttons ps-toolsBtn">
									
									<a href="javascript:void(0)" class="blue" onclick="delQues('${ques.id}',this)">
										<i class="fa fa-trash-o""></i>
										删除
									</a>
									
									<a href="javascript:void(0)" class="blue" onclick="appQues()">
										<i class="fa fa-edit"></i>
										编辑
									</a>
									<c:choose>
										<c:when test="${ques.state==1}">
											<a href="javascript:void(0)" class="green" onclick="closeQues(${ques.id},${ques.state},this)">
												<i class="fa fa-unlock"></i>关闭
											</a>
										</c:when>
										<c:otherwise>
											<a href="javascript:void(0)" class="red" onclick="closeQues(${ques.id},${ques.state},this)">
												<i class="fa fa-lock"></i>重启
											</a>
										</c:otherwise>
									</c:choose>
							</div>
						</c:if>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div>
                     <!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->       
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                     		<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">
									提问人：
                            	</span>
                     			<div class="ticket-user pull-left other-user-box margin-top-5 margin-left-5">
									<img class="user-avatar userImg" title="${ques.userName}" 
										src="/downLoad/userImg/${ques.comId}/${ques.userId}"/>
									<span class="user-name">${ques.userName}</span>
								</div>
                            	<div class="widget-buttons">
                                	时间：${fn:substring(ques.recordCreateTime,0,16)}
                                 </div>
                             </div>
                             
                            <div class="widget-body no-shadow">
                            	<div class="collapse in">
                              		<div class="form-group">
										<label for="xsinput" style="font-weight: bold;">
											<i class="fa fa-question-circle fa-lg blue"></i>问题概要
										</label>
										<div class="padding-left-20">
											${ques.title}
										</div>
									</div>
									<div id="questionShow"  style="clear:both;display: block">
										<c:if test="${not empty ques.content}">
		                              		<div class="form-group">
												<label for="xsinput" style="font-weight: bold;">
												<i class="padding-left-10"></i>
												&nbsp;&nbsp;问题补充</label>
												<div class="padding-left-20">
													<tags:viewTextArea> ${ques.content}</tags:viewTextArea>
												</div>
											</div>
										</c:if>
										<c:choose>
											<c:when test="${not empty ques.listQuesFiles}">
			                              		<div class="form-group">
													<label for="xsinput" style="font-weight: bold;">
													<i class="padding-left-10"></i>
													&nbsp;&nbsp;&nbsp;附件说明</label>
													<div class="padding-left-20">
														<div class="file_div">
															<c:forEach items="${ques.listQuesFiles}" var="quesFile" varStatus="vs">
																<c:choose>
																	<c:when test="${quesFile.isPic==1}">
																		<p class="p_text">
																		附件(${vs.count})：
																			<img onload="AutoResizeImage(300,0,this,'')"
																			src="/downLoad/down/${quesFile.orgFileUuid}/${quesFile.orgFileName}?sid=${param.sid}" />
												 						<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/${quesFile.orgFileUuid}/${quesFile.orgFileName}?sid=${param.sid}" title="下载"></a>
												 						<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);"onclick="showPic('/downLoad/down/${quesFile.orgFileUuid}/${quesFile.orgFileName}','${param.sid}','${quesFile.original}','011','${ques.id }')"></a>
																		</p>
																	</c:when>
																	<c:otherwise>
																		<p class="p_text">
																		附件(${vs.count})：
																			${quesFile.orgFileName}
																		<c:choose>
														 					<c:when test="${quesFile.fileExt=='doc' || quesFile.fileExt=='docx' || quesFile.fileExt=='xls' || quesFile.fileExt=='xlsx' || quesFile.fileExt=='ppt' || quesFile.fileExt=='pptx' }">
																 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${quesFile.orgFileUuid}','${quesFile.orgFileName}','${param.sid}')"></a>
																 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${quesFile.original}','${quesFile.orgFileUuid}','${quesFile.orgFileName}','${quesFile.fileExt}','${param.sid}','011','${ques.id }')"></a>
														 					</c:when>
														 					<c:when test="${quesFile.fileExt=='txt' ||quesFile.fileExt=='pdf'}">
														 						<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/${quesFile.orgFileUuid}/${quesFile.orgFileName}?sid=${param.sid}" title="下载"></a>
																 				<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage('${quesFile.original}','${quesFile.orgFileUuid}','${quesFile.orgFileName}','${quesFile.fileExt}','${param.sid}','011','${ques.id }')"></a>
														 					</c:when>
														 					<c:otherwise>
																 				<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad('${quesFile.orgFileUuid}','${quesFile.orgFileName}','${param.sid}')"></a>
														 					</c:otherwise>
														 				</c:choose>
																		</p>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</div>
													</div>
												</div>
											</c:when>
										</c:choose>
									</div>
									
									<div id="questionEdit" style="clear:both;display: none;">
											<form id="quesForm" class="subform" method="post">
												<div class="form-group">
													<label for="xsinput" style="font-weight: bold;">
													<i class="padding-left-10"></i>
													&nbsp;&nbsp;问题补充</label>
													<div class="padding-left-20">
														<textarea id="content" name="content" class="form-control" style="height:65px;color: #000"
														onpropertychange="handleContent()" onkeyup="handleContent()">${ques.content }</textarea>
													<div id="msgCont" style="float: right"></div>
													<script> 
														//当状态改变的时候执行的函数 
														function handleContent(){
															var value = $('#content').val();
															var len = charLength(value.replace(/\s+/g,""));
															if(len%2==1){
																len = (len+1)/2;
															}else{
																len = len/2;
															}
															if(len>1500){
																$('#msgCont').html("<font color='red'>文字太长，请删减</font>");
															}else{
																$('#msgCont').html("");
															}
														} 
														//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
														if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
															document.getElementById('content').onpropertychange=handleContent 
														}else {//非ie浏览器，比如Firefox 
															document.getElementById('content').addEventListener("input",handleContent,false); 
														} 
													</script>
													</div>
												</div>
												
												<div class="form-group">
													<label for="xsinput" style="font-weight: bold;">
													<i class="padding-left-10"></i>
													&nbsp;&nbsp;&nbsp;附件说明</label>
													<div class="padding-left-20">
													
														<div class="file_div">
															<div style="clear:both" class="ws-process">
																<div id="thelistlistQuesFiles_id" style="width: 300px">
																	<c:choose>
																		<c:when test="${not empty ques.listQuesFiles}">
																			<c:forEach items="${ques.listQuesFiles}" var="quesFile" varStatus="vs">
																			 <div id="wu_file_0_-${quesFile.original }" class="uploadify-queue-item">	
																				<div class="cancel">
																					<a href="javascript:void(0)" fileDone="1" fileId="${quesFile.original}">X</a>
																				</div>	
																					<span class="fileName" title="${quesFile.orgFileName}">
																						<tags:cutString num="25">${quesFile.orgFileName}</tags:cutString>(已有文件)
																					</span>
																					<span class="data"> - 完成</span>
																				<div class="uploadify-progress">
																					<div class="uploadify-progress-bar" style="width: 100%;"></div>
																				</div>	
																			</div>	
																			</c:forEach>
																		</c:when>
																	</c:choose>
																</div>
																<div class="btns btn-sm">
																	<div id="pickerlistQuesFiles_id">选择文件</div>
																</div>
																<script type="text/javascript">
																	loadWebUpfiles('listQuesFiles_id','${param.sid}','','pickerlistQuesFiles_id','thelistlistQuesFiles_id','filelistQuesFiles_id');
																</script>
																<div style="position: relative; width: 350px; height: 90px;display: none">
																	<div style="float: left;">
																		<select list="listQuesFiles" listkey="id" listvalue="fileName" id="listQuesFiles_id" name="listQuesFiles.id" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">
																			<c:choose>
																				<c:when test="${not empty ques.listQuesFiles}">
																					<c:forEach items="${ques.listQuesFiles}" var="quesFile" varStatus="vs">
																						<option selected="selected" value="${quesFile.original}">${quesFile.orgFileName}</option>
																					</c:forEach>
																				</c:when>
																			</c:choose>
																		</select>
																	</div>
																</div>
															</div>
														</div>
													
													</div>
												</div>
												
												<div style="clear: both; text-align: center; padding-top: 20px;">
												<button type="button" class="btn btn-info ws-btnBlue"
													onclick="updateQues(this,${ques.id})" >修改</button>
												<button type="reset" value="取消" class="btn btn-primary date-reset"
													onclick="cancleAppQues()" >取消</button>
											</div>
												
											</form>
										</div>
								</div>
                            </div>
                          </div>
                          
                           <div class="widget-body no-shadow">
                           		<div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat padding-top-5">
                                           <li class="active" id="qasTalk">
												<a href="javascript:void(0)"  data-toggle="tab">问题回答</a>
											</li>
											<li id="qasLog">
												<a href="javascript:void(0)" data-toggle="tab">问答日志</a>
											</li>
											<li id="qasFile">
												<a href="javascript:void(0)" data-toggle="tab">问答附件<c:if test="${ques.fileNum > 0}"><span style="color:red;font-weight:bold;">（${ques.fileNum}）</span></c:if></a>
											</li>
											<%--<li id="qasViewRecord">--%>
                                                 <%--<a data-toggle="tab" href="javascript:void(0)">最近查看</a>--%>
                                             <%--</li>--%>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 <iframe id="otherIframe" name="otherIframe"
												src="/qas/ansQuesPage?sid=${param.sid}&quesId=${ques.id}"
												border="0" frameborder="0" allowTransparency="true"
												noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
                                    	 </div>
                                	</div>
                            	</div>
                            </div> 
                        </div>
					
					</div>
				</div>
			</div>
		</div>
</body>
</html>	
