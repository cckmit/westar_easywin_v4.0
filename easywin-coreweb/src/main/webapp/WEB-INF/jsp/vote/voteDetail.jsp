<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.BiaoQing"%>
<%@page import="com.westar.core.web.BiaoQingContext"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@page import="com.westar.base.util.DateTimeUtil"%>
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
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/voteJs/voteTalkStr.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
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
	
	$("#tabTalk").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active")
		//投票讨论
		$("#voteInfo").attr("src", "/vote/voteTalkPage?sid=${param.sid}&pager.pageSize=10&voteId=${vote.id}&enabled="+$("#enabled").attr("value"))
	});
	$("#tabLog").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active")
		//投票日志
		$("#voteInfo").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${vote.id}&busType=004&ifreamName=voteInfo");
	});
	$("#tabFile").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active")
		//相关附件
		$("#voteInfo").attr("src", "/vote/voteFilePage?sid=${param.sid}&pager.pageSize=10&voteId=${vote.id}")
	});
	$("#voteViewRecord").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active")
		//问答附件
		$("#voteInfo").attr("src", "/common/listViewRecord?sid=${param.sid}&busId=${vote.id}&busType=004&ifreamName=voteInfo")
	});
});


//样式设置
function setStyle(){
	$('.voteTime').mouseover(function(){
		var input = $(this).find(".timeInfo").find("input");
		if(input.length==0){
			$(this).find(".edit").show();
		}else{
			$(".edit").hide();
		}
	});
	$('.voteTime').mouseout(function(){
		$(this).find(".edit").hide();
	});
}

//修改时间
function modifyTime(){
	var orgTime = $("#time").val();
	var input = "<span style='float:left'>截止时间：</span>";
	input = input+"<span style='float:left'><input name=\"finishTime\" type=\"text\" onclick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH',minDate:'<%=DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd)+" "+(DateTimeUtil.getHourStr()) %>'})\" style=\"cursor: pointer;width: 110px;padding:0px 5px;height:25px;color:#000\" class=\"form-control\" readonly=\"readonly\" value=\""+orgTime+"\"/></span>时"
	input = input+"<span style='padding-left:20px'><a href=\"javascript:void(0)\" onclick=\"saveTime(this)\">保存</a></span>\n"
	+"	<span style='padding-left:10px'><a href=\"javascript:void(0)\" onclick=\"cancleTime(this)\">取消</a></span>\n";
	
	$(".voteTime").html(input);
}
//保存时间
function saveTime(ts){
	var time = $(ts).parent().parent().find("input").val();
	var oldTime = $("#time").attr("value");
	if(time!=oldTime && null!=time && ""!=time){
		//异步提交表单 修改投票截止时间
	    $("#voteForm").ajaxSubmit({
	        type:"post",
	        url:"/vote/updateVoteTime?sid=${param.sid}&t="+Math.random(),
	        data:{"id":${vote.id}},
	        dataType: "json", 
	        success:function(data){
		         var state = data.status;
		         if(state=='y'){
		        	 showNotification(1,"修改成功！");
		        	 if(${vote.voterChooseNum==0}){
		        		 $('#voting${vote.id}').show();$('#voted${vote.id}').hide();
		        	 }else{
		        		 $('#voting${vote.id}').hide();$('#voted${vote.id}').show();
			        	 $("#voteIsEnd").hide();
			        	 $("#updateVote").show();
		        	 }
		        	 $("#enabled").attr("value","1");
		        	 var src = $("#voteInfo").attr("src");
			     		if(src.indexOf("voteTalkPage")>=0){
			     			window.voteInfo.showInfos();
			     		}
		        	
					$("#time").attr("value",time);
					$("#otherTime").html(time);
					var div = "<span>截止时间："+time+":00:00</span>\n"
						+"<span  class=\"edit\" style=\"display:none;padding-left: 20px\"><a href=\"javascript:void(0)\" onclick=\"modifyTime(this)\">修改</a></span>\n";
					$(".voteTime").html(div);
					setStyle();
					
		         }
	        },
	        error:function(XmlHttpRevotet,textStatus,errorThrown){
	        	showNotification(2,"操作失败！");
	        }
	    });
	}else{
		 cancleTime(this);
	}
}
//取消修改投票截止时间
function cancleTime(ts){
	var time = $("#time").val();
	var div='\n <span style="float: left">';
	div+='\n 截止时间：'+time+':00:00';
	div+='\n</span>';
	div+='\n <span  class="edit" style="display:none;padding-left: 20px"><a href="javascript:void(0)" onclick="modifyTime(this)">修改</a></span>';

	$(".voteTime").html(div);
	setStyle();
	
}
function vote(ts){
	//单选
	var radios = $(':radio[name="voteChoose"]:checked');
	//多选
	var ckBoxs = $(":checkbox[name='voteChoose']:checked");
	var lenr = radios.length;
	var lenc = ckBoxs.length;
	//异步提交表单
    $("#voteForm").ajaxSubmit({
        type:"post",
        url:"/vote/checkVoteTime?sid=${param.sid}&t="+Math.random(),
        data:{"id":${vote.id}},
        beforeSubmit:function(a,o,f){
        	if(lenr==1||lenc>0){
        		if(lenc>${vote.maxChoose}){
        			window.top.layer.alert('最多只能选择${vote.maxChoose}选项。');
        			return false;
        		}
        	}else{
        		window.top.layer.alert('请先选择一个选项。');
       			return false;
            }
        },
        dataType: "json", 
        success:function(data){
        	 if('y'==data.state){
        		//所选项
     			var chooseIds = new Array();
     			if(lenr==1){
     				chooseIds.push($(radios).val());
     			}
     			if(lenc>0){
     				for(var i=0;i<lenc;i++){
     					chooseIds.push($(ckBoxs[i]).val());
     				}
     			}
     			//异步提交表单
    		    $("#voteForm").ajaxSubmit({
    		        type:"post",
    		        url:"/vote/voteChoose?sid=${param.sid}&t="+Math.random(),
    		        data:{"voteChooses":chooseIds,"voteId":${vote.id},"isVote":${vote.voterChooseNum},"backObj":"yes"},
    		        dataType: "json", 
    		        success:function(data){
    		        	 if(null!=data.vote){
     				        var vote = data.vote;
     			        	$("#voteChooseId").html('');
							
     			        	var html = getVoteStr(vote,'${param.sid}');
     				        
     				        $("#voteChooseId").html(html);
     				       showNotification(1,"投票成功！");
     				    }
    		        },
    		        error:function(XmlHttpRevotet,textStatus,errorThrown){
    		        }
    		    });
        	 }else{
        		 showNotification(2,data.info);
        		 reloadRes(${vote.id},'${param.sid}');
             }
        },
        error:function(XmlHttpRevotet,textStatus,errorThrown){
        	 showNotification(2,"操作失败！");
        }
    });
	
}
//查看投票人
function viewVoter(id){
	if($("#"+id).css("display")=="none"){
		$("#"+id).show();
	}else{
		$("#"+id).hide();
	}
	scrollPage();
}

//删除该投票
function delVote(){
	window.top.layer.confirm("确定要删除投票吗?",{
		  btn: ['确定','取消']//按钮
	  ,title:'询问框'
	  ,icon:3
	}, function(index){
		window.top.layer.close(index);
		//异步提交表单
	    $("#voteForm").ajaxSubmit({
	        type:"post",
	        url:"/vote/delVote?sid=${param.sid}&t="+Math.random(),
	        data:{"id":${vote.id}},
	        dataType: "json", 
	        success:function(data){
	        	if(data.status=='y'){
		        	window.top.location.reload();
	        	}else{
	        		showNotification(2,data.info);
	        	}
	        },
	        error:function(XmlHttpRevotet,textStatus,errorThrown){
	        }
	    });
	});
}


</script>
<style>
	.voteTime span{
		font-size: 15px;
	}
</style>
</head>
<body onload="setStyle();">
<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget no-margin-bottom" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <a href="javascript:void(0)" class="widget-caption blue padding-right-5"
               				attentionState="${vote.attentionState}" busType="004" busId="${vote.id}" describe="0" iconSize="sizeMd"
               				onclick="changeAtten('${param.sid}',this)" title="${vote.attentionState==0?"关注":"取消"}">
							<i class="fa ${vote.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>
						</a>
                        <span class="widget-caption themeprimary" style="font-size: 20px">投票详情</span>
                        
						<c:if test="${vote.owner==userInfo.id}">
	                        <div class="widget-buttons ps-toolsBtn">
									<a href="javascript:void(0)" class="blue" onclick="delVote()">
										<i class="fa fa-trash-o""></i>
										删除
									</a>
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
									发起人：
                            	</span>
                     			<div class="ticket-user pull-left other-user-box margin-top-5 margin-left-5">
									<img class="user-avatar userImg" title="${vote.ownerName}" 
										src="/downLoad/userImg/${vote.comId}/${vote.owner}"/>
									<span class="user-name">${vote.ownerName}</span>
								</div>
                            	<div class="widget-buttons">
                                 </div>
                             </div>
                             <input type="hidden" id="enabled" value="${vote.enabled}"/>
							<form id="voteForm" class="subform">
                            <div class="widget-body no-shadow">
                            	<div class="collapse in">
                                	<input type="hidden" id="time" value="${vote.finishTime}"/>
									<p class="voteTime" style="clear:both;height: 30px;padding-top: 5px">
										<span style="float: left">
											截止时间：${vote.finishTime}:00:00
										</span>
										<c:if test="${userInfo.id==vote.owner}">
											<span  class="edit" style="display:none;padding-left: 20px"><a href="javascript:void(0)" onclick="modifyTime(this)">修改</a></span>
										</c:if>
									</p>
									
                              		<div class="form-group">
										<label for="xsinput" style="font-weight: bold;">
											<i></i>投票描述
										</label>
										<div class="padding-left-20">
											<tags:viewTextArea>${vote.voteContent}</tags:viewTextArea>
										</div>
									</div>
									<div class="form-group">
										<div id="voteChooseId">
											<%--还没有投票的  还没有过期的,可以投票--%>
											<div class="ws-voting" style="${(vote.voterChooseNum eq 0 && vote.enabled eq 1)?"display:block":"display:none" };" id="voting${vote.id}">
												<div class="ws-voting1">
													<div class="ws-voting-text">
														(总计${vote.voteTotal}票)${vote.voteType eq 1?'匿名投票':'' }  最多可以选择${vote.maxChoose }项     投票后可以查看结果
													</div>
												</div>
												
												<c:forEach items="${vote.voteChooses}" var="voteChoose" varStatus="vs">
													<div class="ws-voting1">
														<div class="ws-voting-text">
															<label>
															<%--文本类型选择 --%>
																<c:choose>
																	<c:when test="${vote.maxChoose==1}">
																		<input type="radio" name="voteChoose" value="${voteChoose.id}" ${voteChoose.chooseState==1?"checked":"" }/>
																	</c:when>
																	<c:otherwise>
																		<input type="checkbox" name="voteChoose" value="${voteChoose.id}" ${voteChoose.chooseState==1?"checked":"" }/>
																	</c:otherwise>
																</c:choose>
																<span class="text">&nbsp;&nbsp;${voteChoose.content }</span>
															</label>
														</div>
														<%--选项图片 --%>
														<c:if test="${not empty voteChoose.middle && not empty voteChoose.large}">
															<div style="display:block;"id="mid_${vote.id}_${vs.count }">
																<a href="javascript:void(0)" onclick="$('#large_${vote.id}_${vs.count }').show();$('#mid_${vote.id}_${vs.count }').hide();" >
																	<img src="/downLoad/down/${voteChoose.midImgUuid}/${voteChoose.midImgName}?sid=${param.sid}" onload="AutoResizeImage(0,0,this,'')"/><br/>	
																</a>
															</div>
															<div style="display:none;" id="large_${vote.id}_${vs.count }">
																<a href="javascript:void(0)" onclick="$('#large_${vote.id}_${vs.count }').hide();$('#mid_${vote.id}_${vs.count }').show();" >
																	<img src="/downLoad/down/${voteChoose.largeImgUuid}/${voteChoose.largeImgName}?sid=${param.sid}" onload="AutoResizeImage(0,0,this,'')"/><br/>	
																</a>
															</div>
														</c:if>
													</div>
												</c:forEach>
												<div class="ws-btn" style="padding-top: 10px">
													<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="vote(this)">投票</a>
													<c:if test="${vote.voterChooseNum >0}">
														<a href="javascript:void(0);" class="btn btn-default" onclick="$('#voting${vote.id}').hide();$('#voted${vote.id}').show();">取消</a>
													</c:if>
													<%--未投票的查看投票结果 --%>
													<c:if test="${vote.voterChooseNum eq 0}">
														<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="$('#voting${vote.id}').hide();$('#voted${vote.id}').show();">查看结果</a>
													</c:if>
												</div>
											</div>
											<%--已经投票的 已经过期的 已经过期但未投票的--%>
											<div class="ws-voting" style="${((vote.voterChooseNum eq 0 && vote.enabled eq 0)||vote.enabled eq 0||vote.voterChooseNum >0)?"display:block":"display:none" };" id="voted${vote.id}">
												<div class="ws-voting1">
													<div class="ws-voting-text">
														(总计${vote.voteTotal}票)
														<span id="voteIsEnd">
															<c:if test="${vote.enabled==0}">
																投票已截止
															</c:if>
														</span>
													</div>
												</div>
												<c:forEach items="${vote.voteChooses}" var="voteChoose" varStatus="vs">
													<div class="ws-voting1" style="clear: both">
														<div class="ws-voting-text">
															<label>
																<p>选项${vs.count}:${voteChoose.content}(
																<c:choose>
																	<c:when test="${voteChoose.total>0 && vote.voteType==0}">
																		<a href="javascript:void(0)" onclick="viewVoter('voterPic_${vote.id}_${vs.count }')">
																			${voteChoose.total}票
																		</a>
																	</c:when>
																	<c:otherwise>
																		${voteChoose.total}票
																	</c:otherwise>
																</c:choose>
																)
																</p>
															</label>
														</div>
														<%--选项图片 --%>
														<div>
															<c:if test="${not empty voteChoose.middle && not empty voteChoose.large}">
																<div style="display:block;" id="midV_${vote.id}_${vs.count }">
																	<a href="javascript:void(0)" onclick="$('#largeV_${vote.id}_${vs.count }').show();$('#midV_${vote.id}_${vs.count }').hide();" >
																		<img src="/downLoad/down/${voteChoose.midImgUuid}/${voteChoose.midImgName}?sid=${param.sid}" onload="AutoResizeImage(0,0,this,'')"/><br/>	
																	</a>
																</div>
																<div style="display:none;" id="largeV_${vote.id}_${vs.count }">
																	<a href="javascript:void(0)" onclick="$('#largeV_${vote.id}_${vs.count }').hide();$('#midV_${vote.id}_${vs.count }').show();" >
																		<img src="/downLoad/down/${voteChoose.largeImgUuid}/${voteChoose.largeImgName}?sid=${param.sid}" onload="AutoResizeImage(0,0,this,'')"/><br/>	
																	</a>
																</div>
															</c:if>
														</div>
														<c:if test="${voteChoose.total>0 && vote.voteType==0}">
															<div style="clear:both;display: none" id="voterPic_${vote.id}_${vs.count}">
																<c:forEach items="${voteChoose.voters}" var="voter" varStatus="vsVoter">
																	<div class="ticket-user pull-left other-user-box" style="margin-top: 5px;">
														     			<img  src="/downLoad/userImg/${voter.comId}/${voter.voter}?sid=${param.sid}" title="${voter.voterName}" class="user-avatar"></img>
														     			<span class="user-name">${voter.voterName}</span>
												     				</div>
																</c:forEach>
															</div>
														</c:if>
													</div>
												</c:forEach>
												
												<div class="ws-btn" style="padding-top: 10px;clear:both">
													<c:choose>
														<%--是否可以修改投票 --%>
														<c:when test="${vote.voterChooseNum eq 0 && vote.enabled==1}">
														    <%--未投票的返回投票 --%>
															<span style="display: block;float: left;margin-right: 5px">
																<a href="javascript:void(0);" class="btn btn-default" onclick="$('#voting${vote.id}').show();$('#voted${vote.id}').hide();">返回投票</a>
															</span>
														</c:when>
														<c:otherwise>
															<%--投过票的修改投票 --%>
															<span style="float: left;margin-right: 5px; ${vote.enabled==0?"display:none":"display:block"};" id="updateVote">
																<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="$('#voting${vote.id}').show();$('#voted${vote.id}').hide();">修改投票</a>
															</span>
															<span style="float: left;display: block;">
																<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="reloadRes(${vote.id},'${param.sid }');">刷新结果</a>
															</span>
														</c:otherwise>
													</c:choose>
												</div>
											</div>
										</div>
									</div>
								</div>
                            </div>
                            </form>
                          </div>
                          
                           <div class="widget-body no-shadow">
                           		<div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat">
                                           <li class="active" id="tabTalk">
												<a href="javascript:void(0)"  data-toggle="tab">投票留言</a>
											</li>
											<li id="tabLog">
												<a href="javascript:void(0)" data-toggle="tab">投票日志</a>
											</li>
											<li id="tabFile">
												<a href="javascript:void(0)" data-toggle="tab">相关附件<c:if test="${vote.fileNum > 0}"><span style="color:red;font-weight:bold;">（${vote.fileNum}）</span></c:if></a>
											</li>
											<%--<li id="voteViewRecord">--%>
                                                 <%--<a data-toggle="tab" href="javascript:void(0)">最近查看</a>--%>
                                             <%--</li>--%>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 <iframe id="voteInfo"
												src="/vote/voteTalkPage?sid=${param.sid}&pager.pageSize=10&voteId=${vote.id}&enabled=${vote.enabled}"
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
    <script src="/static/assets/js/bootstrap.min.js"></script>
    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
	
</body>
</html>
