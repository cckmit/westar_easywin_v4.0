<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" >
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="/static/js/selfGroupTree.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script>
//var winWidth = 0; 
var winHeight = 0; 
//获取窗口宽度 
//if (window.innerWidth) 
//winWidth = window.innerWidth; 
//else if ((document.body) && (document.body.clientWidth)) 
//winWidth = document.body.clientWidth; 
//获取窗口高度 
if (window.innerHeight) 
winHeight = window.innerHeight; 
else if ((document.body) && (document.body.clientHeight)) 
winHeight = document.body.clientHeight; 
//通过深入Document内部对body进行检测，获取窗口大小 
if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth) 
{ 
winHeight = document.documentElement.clientHeight; 
//winWidth = document.documentElement.clientWidth; 
} 
var leftHeight = $("#indexLeft").height();
$("#indexRight").css("height",((winHeight>leftHeight?winHeight:leftHeight)-30)+"px")
/**
 * 初始化分享范围
 */
function initSelfGroupTree(selfGroupStr){
	var setting = {
		check: {
			enable: true,
			chkboxType: {"Y":"", "N":""}
		},
		view: {
			dblClickExpand: false
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			beforeClick: beforeClick,
			onCheck: onCheck
		}
	};
	initZTree(setting,"treeDemo","scopeTypeSel","idType","menuContent",selfGroupStr);
}

window.onload = function(){
	initSelfGroupTree(${selfGroupStr});
	//聚焦时设置设置宽一点
	$("#content").focus(function(){
		$(this).css("height","150px");
	}) 
	//失焦时恢复宽度设置
	$("#content").blur(function(){
		if(strIsNull($(this).val())){
			$(this).css("height","30px");
		}
	}) 
	
}
/**
 * 添加附件
 */
function addFiles(){
	if($("#subState").val()==1){
		return;
	}
	//防止重复提交
	$("#shareForm").ajaxSubmit({
        type:"post",
        url:"/msgShare/ajaxAddMsgShare?sid=${param.sid}&t="+Math.random(),
        dataType: "json",
        beforeSubmit:function (a,f,o){
        	$("#subState").val(1);
		},
        traditional :true,
        success:function(data){
        	$("#subState").val(0);
            if(data.status=='y'){
           		showNotification(1,"分享成功");
            	closeWindow();
            }
        },
        error:function(XmlHttpRequest,textStatus,errorThrown){
        	$("#subState").val(0);
            //可以再次提交
			showNotification(2,"系统错误，请联系管理人员！");
        }
    });
}

//窗体点击事件检测
document.onclick = function(e){
	   var evt = e?e:window.event;
	   var ele = evt.srcElement || evt.target;
	   //表情包失焦关闭
		for(var i=0;i<biaoQingObjs.length;i++){
			if(ele.id != biaoQingObjs[i].switchID){
				$("#"+biaoQingObjs[i].divID).hide();
			}
		}
}
//创建一个表情对象数组
var biaoQingObjs = new Array();
//初始化最新初始化表情对象
var activingBiaoQing;
//表情对象添加；switchID触发器开关，objID返回对象主键,表情显示div层主键
function addBiaoQingObj(switchID,divID,objID){
	//数组是否已经包含此元素标识符
	var haven = false;
	//判断数组是否已经包含此主键元素
	if(isBiaoQingEvent(switchID)){
		haven = true;
	}
	//对象构建
	var biaoQing ={"switchID":switchID,"objID":objID,"divID":divID}
	
	if(!haven){
		//主键存入数组
		biaoQingObjs[biaoQingObjs.length]=biaoQing;
	}
	//初始化最新初始化表情对象
	activingBiaoQing = biaoQing;
	//打开表情
	biaoQingOpen(biaoQing);
}
//判断页面点击事件事都是表情触发事件
function isBiaoQingEvent(eventId){
	//数组是否已经包含此元素标识符
	var haven = false;
	//判断数组是否已经包含此主键元素
	for(var i=0;i<biaoQingObjs.length;i++){
		if(biaoQingObjs[i].switchID==eventId){
			haven = true;
			break;
		}
	}
	return haven;
}
//表情打开
function biaoQingOpen(obj){
	$("#"+obj.divID).show();
}
//表情包关闭
function biaoQingClose(){
	$("#"+activingBiaoQing.divID).hide();
}
//关闭表情div，并赋值
function divClose(title,path){
	biaoQingClose();
	insertAtCursor(activingBiaoQing.objID,title)
	$("#"+activingBiaoQing.objID).focus();
}
</script>

		<div class="widget-body">
			<div class="widget radius-bordered">
            	<div class="widget-body no-shadow">
                	<div class="tickets-container bg-white">
						<div>
                             	<!--直接反馈-->
					    			<span class="input-icon icon-right">
					                	<textarea class="form-control" id="content" name="content"
					                	placeholder="分享信息……" style="height:30px;" onpropertychange="handleTitle()" onkeyup="handleTitle()"></textarea>
					                </span>
					                <script type="text/javascript">
					              //当状态改变的时候执行的函数 
					                function handleTitle(){
					                	var value = $('#content').val();
					                	var len = charLength(value.replace(/\s+/g,""));
					                	if(len%2==1){
					                		len = (len+1)/2;
					                	}else{
					                		len = len/2;
					                	}
					                	if(len>150){
					                		$('#msgTitle').html("<font color='red' title='内容过长,弹窗编辑' style='cursor:pointer' onclick='popAddMsgShare()'>弹窗编辑</font>");
					                	}else{
					                		$('#msgTitle').html(len+"/150");
					                	}
					                } 
					                //firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
					                if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
					                	document.getElementById('content').onpropertychange=handleTitle 
					                }else {//非ie浏览器，比如Firefox 
					                	document.getElementById('content').addEventListener("input",handleTitle,false); 
					                } 
					                </script>
					                <div id="msgTitle" style="float:right;padding-right: 10px">0/150</div> 
					                <div class="panel-body no-padding" style="line-height: 25px">
					                	<%--<div class="buttons-preview pull-left" style="position: relative;">--%>
					                    	<%--<a class="btn-icon fa fa-meh-o fa-lg" href="javascript:void(0)" id="biaoQingSwitch" --%>
					                    		<%--onclick="addBiaoQingObj('biaoQingSwitch','biaoQingDiv','content');">--%>
											<%--</a>--%>
											<%--<div id="biaoQingDiv" class="blk" style="display:none;position:absolute;width:200px;top:15px;z-index:99;left: 15px">--%>
												<%--<!--表情DIV层-->--%>
										        <%--<div class="main">--%>
										            <%--<ul style="padding: 0px">--%>
										            <%--<jsp:include page="/biaoqing.jsp"></jsp:include>--%>
										            <%--</ul>--%>
										        <%--</div>--%>
										    <%--</div>--%>
					                    <%--</div>--%>
					                    
									    <div class="ps-clear pull-left">
											<div class="fx" style="position: relative;">
												 <div style="float: left;font-size: 15px" class="blue padding-left-5">
													范围:<input id="scopeTypeSel" type="text" readonly="readonly" value="${scopeTypeSel}" style="width:150px;height: 30px" onclick="showMenu();" />
												</div>
												<div id="menuContent" style="display:none; position: absolute;top:30px;left: 40px;z-index: 999">
														<input type="hidden" id="idType" name="idType" value="${idType}"/>
													<ul id="treeDemo" class="ztree" style="clear:both;margin-top:0;z-index:1;background-color:#f0f0f0;width: 150px;padding-bottom: 0px"></ul>
													<ul id="addGrpUl" class="ztree" style="z-index:1;background-color:#f0f0f0;width: 150px;padding-bottom: 10px;">
														<li style="text-align: center;margin-top: 5px;color: #1c98dc;cursor: pointer;" onclick="addGrpForTree('scopeTypeSel','menuContent','idType','treeDemo','${param.sid}')">
															+添加分组
														</li>
													</ul>
												</div>
											 </div>
										</div>
										<div class="pull-left padding-left-5" >
											<button class="btn btn-info btn-primary btn-xs" type="button" id="ajaxSubmit" onclick="addMsgShare()">发布</button>
										</div>
											
					                </div>
                             	</div>
                               </div>
                           </div>
                         </div>
				<ul class="messages-list" id="allmsgContainer">
					<c:choose>
						<c:when test="${not empty listMsgShare}">
							<c:forEach items="${listMsgShare}" var="msg" varStatus="vs">
								<c:choose>
									<c:when test="${msg.type eq '004' && msg.isDel==0}">
										<!-- 投票展示样式替换 -->
										<li class="item first-item" id="msg${msg.id}">
											<div class="message-content" style="margin-left: 35px">
												<img src="/downLoad/userImg/${msg.comId}/${msg.creator}.jpg"
													 class="message-head" width="30" height="30"
													 title="${msg.creatorName}" />
												<c:set var="vote" value="${msg.vote}"></c:set>
												<div class="content-headline">
													<div class="checkbox pull-left ps-chckbox">
														<label>
															<a href="javascript:void(0)" attentionState="${msg.attentionState}" busType="${msg.type}"  busId="${msg.type==1?msg.id:msg.modId}" describe="0" iconSize="sizeMd"
															 onclick="changeAtten('${param.sid}',this)"  style="font-size:18px;margin-right:5px;" >
																<i class="sizeMd fa ${msg.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>
															</a>
														</label>
													</div>
													[${msg.typeName}]
													<time>
													${msg.createDate}
													</time>
												</div>
												<div class="content-text">
													<a href="javascript:void(0)" onclick="indexMsgShareView(${msg.type==1?msg.id:msg.modId},'${msg.type}','${param.sid}');">
														<tags:viewTextArea><tags:cutString num="140">${vote.voteContent}</tags:cutString></tags:viewTextArea>
													</a>
												</div>
												<!-- 投票动作描述 -->
												<form id="voteForm${vote.id}">
												<%--还没有投票的  还没有过期的,可以投票--%>
												<div class="ws-voting" style="${(vote.voterChooseNum eq 0 && vote.enabled=='1')?'display:block':'display:none' };" id="voting${vote.id}">
													<div class="ws-type">
															(总计${vote.voteTotal}票)${vote.voteType eq 0?"匿名":"" }  最多选${vote.maxChoose }项 <br/>
															截止时间<span id="otherTime">${vote.finishTime}</span> :00
													</div>
													<%--投票选项 --%>
													<c:forEach items="${vote.voteChooses}" var="voteChoose" varStatus="vs">
														<div class="ws-voteBox">
															<div class="radio">
																<label class="ws-check">
																	<%--文本类型选择 --%>
																	<c:choose>
																		<c:when test="${vote.maxChoose==1}">
																			<lable>
																			<input type="radio" name="voteChoose" value="${voteChoose.id}" ${voteChoose.chooseState==1?"checked":"" }/>
																			<span class="text">&nbsp;&nbsp;${voteChoose.content }</span>
																			</lable>
																		</c:when>
																		<c:otherwise>
																			<lable>
																			<input type="checkbox" name="voteChoose" value="${voteChoose.id}" ${voteChoose.chooseState==1?"checked":"" }/>
																			<span class="text">&nbsp;&nbsp;${voteChoose.content }</span>
																			</lable>
																		</c:otherwise>
																	</c:choose>
																	</label>
																	<%--选项图片 --%>
																	<c:if test="${not empty voteChoose.middle && not empty voteChoose.large}">
																		<div class="ws-voteImg">
																			<img src="/downLoad/down/${voteChoose.midImgUuid}/${voteChoose.midImgName}?sid=${param.sid}"
																			onclick="viewOrgByPath('/downLoad/down/${voteChoose.largeImgUuid}/${voteChoose.largeImgName}')"
																			/>
																		</div>
																	</c:if>
															</div>
														</div>
													</c:forEach>
													<%--投票选项结束 --%>
													<div class="ws-type">
														<button class="btn btn-info ws-btnBlue" style="margin-right:10px;" onclick="startVote(this,${vote.maxChoose},${vote.id },${vote.voterChooseNum},'${param.sid }')" type="button">投票</button>
														<c:if test="${vote.voterChooseNum >0}">
															<button class="btn btn-default" onclick="$('#voting${vote.id}').hide();$('#voted${vote.id}').show();" type="button">取消</button>
														</c:if>
														<%--未投票的查看投票结果 --%>
														<c:if test="${vote.voterChooseNum eq 0}">
															<button class="btn btn-info ws-btnBlue" style="margin-right:10px;" type="button" onclick="$('#voting${vote.id}').hide();$('#voted${vote.id}').show();">查看结果</button>
														</c:if>
													</div>
												</div>
												<div class="ws-clear"></div>
												
												
												<%--已经投票的 已经过期的 已经过期但未投票的--%>
												<div class="ws-voting" style="${((vote.voterChooseNum eq 0 && vote.enabled eq 0)||vote.enabled eq 0||vote.voterChooseNum >0)?"display:block":"display:none" };" id="voted${vote.id}">
													<div class="ws-voting1">
														<div class="ws-voting-text">
															(总计${vote.voteTotal}票)
															<c:if test="${vote.enabled==0}">
																投票已截止
															</c:if>
														</div>
													</div>
													<%--投票选项开始 --%>
													<c:forEach items="${vote.voteChooses}" var="voteChoose" varStatus="vs">
													<div class="ws-voteBox" style="clear:both">
														<div class="radio">
															<label class="ws-check">选项${vs.count}:${voteChoose.content}(
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
															</label>
															<c:if test="${not empty voteChoose.middle && not empty voteChoose.large}">
																<%--是否需要修改成查看的形式 --%>
																<div class="ws-voteImg">
																	<img src="/downLoad/down/${voteChoose.midImgUuid}/${voteChoose.midImgName}?sid=${param.sid}"
																	onclick="viewOrgByPath('/downLoad/down/${voteChoose.largeImgUuid}/${voteChoose.largeImgName}')"
																	/>
																</div>
															</c:if>
															<%--投过票，非匿名，可以查看投票的信息 --%>
															<c:if test="${voteChoose.total>0 && vote.voteType==0}">
																<div style="clear:both;display: none" id="voterPic_${vote.id}_${vs.count}">
																	<c:forEach items="${voteChoose.voters}" var="voter" varStatus="vsVoter">
																		<div class="ws-position-vote" style="float: left;margin-top: 2px;padding-left: 20px">
															     			<img  src="/downLoad/userImg/${voter.comId}/${voter.voter}?sid=${param.sid}" title="${voter.voterName}" class="user-avatar"></img>
															     			<i class="user-name">${voter.voterName}</i>
													     				</div>
													     				
																	</c:forEach>
																</div>
															</c:if>
														</div>
													</div>
													</c:forEach>
													<%--投票选项结束--%>
													
													<div class="ws-type">
														<c:choose>
															<%--是否可以修改投票 --%>
															<c:when test="${vote.voterChooseNum eq 0 && vote.enabled==1}">
															    <%--未投票的返回投票 --%>
																<button class="btn btn-default" onclick="$('#voting${vote.id}').show();$('#voted${vote.id}').hide();" type="button">返回投票</button>
															</c:when>
															<c:otherwise>
																<%--投过票的修改投票 --%>
																<c:if test="${vote.enabled!=0}">
																	<button class="btn btn-info ws-btnBlue" style="margin-right:5px;padding: 6px 6px" onclick="$('#voting${vote.id}').show();$('#voted${vote.id}').hide();" type="button">修改投票</button>
																</c:if>
																	<button class="btn btn-default" style="padding: 6px 6px"  onclick="reloadDiv(${vote.id},'${param.sid }');" type="button">刷新结果</button>
															</c:otherwise>
														</c:choose>
													</div>
												</div>
												<div class="ws-clear"></div>
											</form>
											</div>
										</li>
									</c:when>
									<c:otherwise>
										<li class="item first-item" id="msg${msg.id}">
											<div class="message-content" style="margin-left: 35px">
												<img src="/downLoad/userImg/${msg.comId}/${msg.creator}.jpg"
													 class="message-head" width="30" height="30"
													 title="${msg.creatorName}" />
												<div class="content-headline">
													<c:if test="${((msg.type eq '1' || msg.type eq '003' || msg.type eq '004' || msg.type eq '005'
														|| msg.type eq '011' || msg.type eq '012')&&msg.isDel==0  && msg.traceType==0 )  || msg.type eq '050'}">
													<div class="checkbox pull-left ps-chckbox">
														<label>
															<a href="javascript:void(0)" attentionState="${msg.attentionState}" busType="${msg.type}"  busId="${msg.type==1?msg.id:msg.modId}" describe="0" iconSize="sizeMd"
															onclick="changeAtten('${param.sid}',this)"  style="font-size:18px;margin-right:5px;">
																<i class="fa sizeMd ${msg.attentionState==0?'fa-star-o':'fa-star ws-star'}"
																	 title="${msg.attentionState==0?'关注':'取消'}"></i>
															</a>
														</label>
													</div>
													</c:if>
													[${msg.typeName}]
													<time>
													${msg.createDate}
													</time>
												</div>
												<div class="content-text" style="word-wrap: break-word; word-break: break-all;">
													<c:choose>
														<c:when test="${msg.isDel==0}">
															<a href="javascript:void(0)" class="item-box" onclick="indexMsgShareView(${(msg.type==1 &&
															 msg.traceType==0)?msg.id:msg.modId},'${msg.type}','${param.sid}');">
																<tags:cutString num="100">${msg.content}</tags:cutString>
															</a>
														</c:when>
														<c:otherwise>
															<i style="color:gray"><tags:viewTextArea><tags:cutString num="100">${msg.content}&nbsp;[已删除]</tags:cutString></tags:viewTextArea></i>
														</c:otherwise>
													</c:choose>
												</div>
											</div>
										</li>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<li style="margin-top:10px;display: ${shareNum>5?'block':'none'}" align="center" id="moreMsg">
								<div style="margin:0 0;" align="center">
									<a href="javascript:void(0)" style="font-size:15px;" onclick="loadIndexMeinv('${param.sid}',this,'right');">查看更多</a>
								</div>
							</li>
						</c:when>
					</c:choose>
				</ul>
				<c:choose>
					<c:when test="${empty listMsgShare}">
						<section class="error-container text-center">
					            <h1><i class="fa fa-exclamation-triangle"></i></h1>
					            <div class="error-divider">
					                <h2>都太腼腆了，暂无分享信息。</h2>
					                <p class="description">协同提高效率，分享拉近距离。</p>
					            </div>
					        </section>
					</c:when>
				</c:choose>
		</div>
	</div>

