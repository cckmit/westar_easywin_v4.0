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
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<link href="/static/assets/css/task.css" rel="stylesheet" type="text/css">
<style type="text/css">
	.noScoreBtn{
		color: #fff !important;
		background: red !important;
		border-color: red !important;
		cursor: pointer !important;
	    vertical-align: middle !important;
	    margin: 0 !important;
	    position: relative !important;
	    display: inline-block !important;
	    transition: all .15s ease !important;
	    border-radius: 2px !important;
    	background-clip: padding-box !important;
    	width: 50px;
	}
</style>
<script type="text/javascript">
var sid="${param.sid}"
//打开页面body
var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//打开页面的标签
var openPageTag;
//打开页面的标签
var openTabIndex;
//注入父页面信息
function setWindow(winDoc,win){
	openWindowDoc = winDoc;
	openWindow = win;
	openPageTag = openWindow.pageTag;
	openTabIndex = openWindow.tabIndex;
}
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
//autoCompleteCallBack回调对象标识
var autoCompleteCallBackVar =null;
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	//提交表单
	$("body").on("click","#addJfScore",function(){
		addJfScoreForm.checkForm() && (addJfScoreForm.assimbData(),$("#addJfScoreForm").submit());
	})
	//关联模块选择
	$("body").on("click",".relateMod",function(){
		$(".relativeRow").remove();//暂时先单选关联
		var actObj = $(this);
		var busType = $(actObj).attr("busType");
		if(busType=="012"){
			crmMoreSelect(1,null,function(crms){
				$("#addJfScoreForm [name='busType']").val(busType);
				$("#addJfScoreForm [name='busId']").val(crms[0].id);
				var rowObj = initModRelateStyle(busType);
				$(rowObj).find("input[busType='"+busType+"']").val(crms[0].crmName);
				$("#moreOpt").parent().before(rowObj);
			})
		}else if(busType=="005"){
			itemMoreSelect(1, null,function(items){
				$("#addJfScoreForm [name='busType']").val(busType);
				$("#addJfScoreForm [name='busId']").val(items[0].id);
				var rowObj = initModRelateStyle(busType);
				$(rowObj).find("input[busType='"+busType+"']").val(items[0].itemName);
				$("#moreOpt").parent().before(rowObj);
			})
		}
	});
	//关联控件点击绑定
	$("body").on("click",".colAdd",function(){
		var actObj = $(this);
		var busType = $(actObj).attr("busType");
		$("#addJfScoreForm [name='busType']").val(busType);
		if(busType=="012"){
			crmMoreSelect(1,null,function(crms){
				$("#addJfScoreForm [name='busId']").val(crms[0].id);
				$(actObj).prev().val(crms[0].crmName);
			})
		}else if(busType=="005"){
			itemMoreSelect(1, null,function(items){
				$("#addJfScoreForm [name='busId']").val(items[0].id);
				$(actObj).prev().val(items[0].itemName);
			})
		}
	});
	//关联控件点击删除绑定
	$("body").on("click",".colDel",function(){
		$("#addJfScoreForm [name='busType']").val("");
		$("#addJfScoreForm [name='busId']").val("");
		var actObj = $(this);
		$(actObj).parents(".relativeRow[busType='"+$(actObj).attr("busType")+"']").remove();
	});
	
	//人员选择
	$("body").on("click",".dfUserChooseBtn",function(){
		var params = {
				"onlySubState":"0",//默认是全部人员
		}
		//人员多选
		userMore(null, params, sid,null,null,function(options){
			if(options && options[0]){
				$.each(options,function(index,option){
					var userId = option.value;
					var userName = option.text;
					var _tr = $("#jfScoreList").find("tr[dfUserId="+userId+"]");
					if(_tr && _tr.get(0)){
						return true;
					}
					addJfScoreForm.addScoreUser(userId,userName)
					addJfScoreForm.resetIndex();
					addJfScoreForm.loadImg();
				})
			}
			
		});
	});
	//删除人员信息
	$("body").on("click",".delData",function(){
		var tr = $(this).parents("tr");
		var nextTr = $(tr).next();
		$(nextTr).remove();
		$(tr).remove();
		addJfScoreForm.resetIndex();
	});
	//评分标注选择
	$("body").on("click",".jfzbChooseBtn",function(){
		var dfUserId = $(this).attr("dfUserId");
		var _this = $(this);
		var params = {"scopeUserId":dfUserId}
		jfzbChoose(1,params,function(jfzbs){
			
			var jfzb = jfzbs[0];
			
			$(_this).parents("tr").find("td:eq(2)").find("button").html(jfzb.jfzbTypeName);
			
			
			//二级指标
			var leveTwoSpan = $("<span></span>");
			$(leveTwoSpan).html(jfzb.leveTwo+"分");
			$(_this).parents("tr").find("td:eq(3)").html($(leveTwoSpan));
			
			//打分上线
			var jfTopSpan = $("<span></span>");
			$(jfTopSpan).html(jfzb.jfTop+"分");
			$(_this).parents("tr").find("td:eq(4)").html($(jfTopSpan));
			
			//打分下线
			var jfBottomSpan = $("<span></span>");
			$(jfBottomSpan).html(jfzb.jfBottom+"分");
			$(_this).parents("tr").find("td:eq(5)").html($(jfBottomSpan));
			
			var jfzbInput = $('<input type="hidden" class="form-control jfzbIdInput"/>');
			$(jfzbInput).attr("value",jfzb.id)
			$(_this).parents("tr").find("td:eq(6)").html($(jfzbInput));
			//打分信息
			var _score = $('<input type="text" class="form-control scoreInput"/>');
			$(_score).css("width","50px");
			$(_score).css("text-align","center");
			$(_score).css("padding","0 0");
			$(_score).attr("maxlength","5");
			
			$(_score).attr("jfTop",jfzb.jfTop);
			$(_score).attr("jfBottom",jfzb.jfBottom);
			
			$(_this).parents("tr").find("td:eq(6)").append($(_score));
			
			return true;
		});
	})
	
});
var addJfScoreForm = {
		addScoreUser:function(userId,userName){
			var _tr =$("<tr></tr>");
			//设置样式
			$(_tr).css("height","45px");
			$(_tr).css("clear","both");
			$(_tr).css("border-top","1px dashed #ccc");
			
			$(_tr).attr("dfUserId",userId);
			
			//序号
			var _xhTd = $("<td>1</td>");
			$(_xhTd).addClass("text-center");
			$(_xhTd).attr("rowspan","2");
			(_tr).append($(_xhTd));
			
			//头像
			var _imgTd = $("<td></td>");
			$(_imgTd).addClass("text-center");
			$(_imgTd).attr("rowspan","2");
			
			var _imgDiv1 = $('<div class="ticket-item no-shadow clearfix ticket-normal"></td>');
			var _imgDiv2 = $('<div class="ticket-user pull-left no-padding"></td>');
			
			var _img = $('<img class="user-avatar userImg" src="/downLoad/userImg/${userInfo.comId}/'+userId+'" />');
			$(_img).attr("userId",userId);
			$(_img).attr("comId","${userInfo.comId}");
			$(_img).attr("title",userName);
			
			var _userName = $('<span class="user-name"></span>');
			$(_userName).html(userName);
			
			$(_imgDiv2).append($(_img));
			$(_imgDiv2).append($(_userName));
			
			$(_imgDiv1).append($(_imgDiv2));
			$(_imgTd).append($(_imgDiv1));
			$(_tr).append($(_imgTd));
			
			//标准选择
			var _scoreBtnTd = $("<td></td>");
			$(_scoreBtnTd).attr("rowspan","2");
			
			var _scoreBtn = $('<button class="btn btn-info btn-primary btn-xs jfzbChooseBtn" type="button"></button>');
			$(_scoreBtn).attr("dfUserId",userId);
			$(_scoreBtn).html("标准选择");
			$(_scoreBtnTd).append($(_scoreBtn));
			$(_tr).append($(_scoreBtnTd));
			
			//评分指标
			var _leveTwoTd = $("<td></td>");
			$(_leveTwoTd).html("--");
			$(_tr).append($(_leveTwoTd));
			
			//得分上限	
			var _jfTopTd = $("<td></td>");
			$(_jfTopTd).html("--");
			$(_tr).append($(_jfTopTd));
			
			//得分下限	
			var _jfLowTd = $("<td></td>");
			$(_jfLowTd).html("--");
			$(_tr).append($(_jfLowTd));
			
			//得分
			var _scoreTd = $("<td></td>");
			$(_scoreTd).html("--");
			$(_tr).append($(_scoreTd));
			
			//删除
			var _optTd = $("<td></td>");
			var _optA = $('<a class="fa fa-times-circle-o fa-lg delData" href="javascript:void(0)" title="删除">')
			$(_optTd).html(_optA);
			$(_tr).append($(_optTd));
			
			$("#dataShow").append($(_tr));
			
			//评分意见
			var _ideaTr = $("<tr></tr>");
			var _ideaNameTd = $("<td></td>");
			$(_ideaNameTd).html("评分意见");
			
			var _ideaA = $('<a href="javascript:void(0);" class="fa fa-comments-o" title="常用意见"></a>')
			$(_ideaA).attr("onclick","addIdea('remarkTextarea_"+userId+"','${param.sid}')");
			$(_ideaNameTd).append(_ideaA);
			
			$(_ideaTr).append(_ideaNameTd);
			
			var _ideaTd = $("<td></td>");
			$(_ideaTd).attr("colspan","4");
			var _remark = $('<textarea rows="" cols="" class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:80px;" ></textarea>')
			$(_remark).attr("id","remarkTextarea_"+userId);
			$(_ideaTd).append($(_remark));
			
			$(_ideaTr).append(_ideaTd);
			
			$("#dataShow").append($(_ideaTr));

			
		},
		loadImg:function(){
			//头像设置
			$.each($("body").find("img").filter(".userImg"),function(index,imgObj){
				var title = $(this).attr("title");
				
				var comId = $(this).attr("comId");
				var userId = $(this).attr("userId");
				 if(comId && userId){
					var imgSrc = "/downLoad/userImg/"+comId+"/"+userId+"?sid="+sid;
					$(this).attr("src",imgSrc);
				}
			})
		},
		resetIndex:function(){
			var dfUserTrs = $("#dataShow").find("tr[dfUserId]");
			if(dfUserTrs && dfUserTrs.get(0)){
				$.each(dfUserTrs,function(index,tr){
					$(tr).find("td:eq(0)").html((index+1));
				})
			}
		},
		checkScore:function(score,ts){
			if(!score){
				layer.tips("请填写得分！",$(ts),{tips:1});
				return false	
			}
			if(isNaN(score)){
				layer.tips("请填写数字！",$(ts),{tips:1});
				return false	
			}
			
			var regFloat = /^[-]?((0|([1-9]\d*))(\.\d{1})?)?$/
			if(!regFloat.test(score)){
				layer.tips("请填写至多一位小数！",$(ts),{tips:1});
				return false
			}
			var jfTop = $(ts).attr("jfTop");
			var jfBottom = $(ts).attr("jfBottom");
			if(jfTop && jfBottom){
				jfTop = Number(jfTop);
				jfBottom = Number(jfBottom);
				if(jfTop == 0 && jfBottom == 0){//自定义分数
					return true;
				}
				var score = Number(score);
				if(jfTop<score){
					layer.tips("得分不能超过最高分！",$(ts),{tips:1});
					return false
				}
				if(jfBottom>score){
					layer.tips("得分不能低于过最低分！",$(ts),{tips:1});
					return false
				}
				
			}
			
			return true;
		},checkForm:function(){
			var scoreInputs = $("#dataShow").find(".scoreInput");
			if(!scoreInputs || !scoreInputs.get(0)){
				showNotification(2,"没有评分操作，不能提交评分！")
				return false;
			}
			
			var trs = $("#dataShow").find("tr[dfUserId]");
			if($(scoreInputs).length != $(trs).length){
				
				$.each(trs,function(index,tr){
					var scoreInput = $(tr).find(".scoreInput");
					if(!scoreInput || !scoreInput.get(0)){
						layer.tips("请完成评分！",$(tr).find("td:eq(2)").find("button"),{tips:1});
						return false;
					}
				})
				return false;
			}
			var flag = 1;
			$.each(trs,function(index,tr){
				var scoreInput = $(tr).find(".scoreInput");
				if(scoreInput && scoreInput[0]){
					var score = $(scoreInput).val();
					addJfScoreForm.checkScore(score,$(scoreInput)) || (flag = 0)
					return flag == 0?false:true;
				}
			})
			return flag;
	
		},assimbData:function(){
			var trs = $("#dataShow").find("tr");
			$("#addJfScoreForm").find("#assimbData").remove();
			
			var assimbDataDiv = $("<div></div>");
			$(assimbDataDiv).attr("id","assimbData");
			
		
			var jfScoreIndex = 0;
			$.each(trs,function(index,tr){
				
				var jfzbIdInput = $(tr).find(".jfzbIdInput");
				if(jfzbIdInput && jfzbIdInput[0]){
					//积分人员
					var dfUserId = $('<input type="hidden" class="form-control" name ="listJfScores['+jfScoreIndex+'].dfUserId"/>');
					$(dfUserId).attr("value",$(tr).attr("dfUserId"));
					$(assimbDataDiv).append($(dfUserId));
					//积分指标的标准
					var jfzbId = $(jfzbIdInput).val();
					var jfScoreZbId = $('<input type="hidden" class="form-control" name ="listJfScores['+jfScoreIndex+'].jfzbId"/>');
					$(jfScoreZbId).attr("value",jfzbId);
					$(assimbDataDiv).append(jfScoreZbId);
					
					//得分情况
					var jfScoreVal = $('<input type="hidden" class="form-control" name ="listJfScores['+jfScoreIndex+'].score"/>');
					var score = $(tr).find(".scoreInput").val();
					$(jfScoreVal).attr("value",score);
					$(assimbDataDiv).append($(jfScoreVal))
					
					
					var remarks = $(tr).next().find("textarea");
					if(remarks && remarks[0]){
						var val = $(remarks).val();
						//得分情况
						var remarkArea = $('<textarea type="hidden" name ="listJfScores['+jfScoreIndex+'].remark"></textarea>');
						$(remarkArea).html(val)
						$(assimbDataDiv).append($(remarkArea))
					}
					
					jfScoreIndex++;
				}
			})
			$("#addJfScoreForm").append($(assimbDataDiv));
		}
}

</script>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">常规评分</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="addJfScore">
								<i class="fa fa-save"></i>
								评分
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
						<form action="/jfMod/normal/addNormalJf" method="post" id="addJfScoreForm">
							<input type="hidden" name="sid" value="${param.sid}"/>
							<input type="hidden" name="busId" value=""/>
							<input type="hidden" name="busType" value=""/>
						</form>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                           <div class="widget-body no-shadow">
	                           <div class="widget no-header">
				                    <div id="moreOpt">
				                        <div class="clearfix">
				                            <ul class="pull-left task-rel">
				                                <li class="relateMod" busType="012" style="cursor: pointer;"><span><i class="fa fa-thumb-tack padding-right-10"></i>关联客户</span></li>
				                                <li class="relateMod" busType="005" style="cursor: pointer;"><span><i class="fa fa-thumb-tack padding-right-10"></i>关联项目</span></li>
				                            </ul>
				                        </div>
				                    </div>
				                </div>
			                
                           		<div class="widget no-header">
					                <div class="widget-body bordered-radius">
					                    <div class="task-describe clearfix">
					                        <div class="tickets-container tickets-bg tickets-pd clearfix">
					                        	 <ul class="tickets-list clearfix">
											       <li>
					                                <table width="100%" cellspacing="0" cellpadding="0" border="0" id="jfScoreList">
					                                	<thead>
															<tr class="padding-top-10">
																<th width="10%" class="text-center">序号</th>
																<th width="15%">被评分人</th>
																<th width="15%">评分标准</th>
																<th width="20%">评分指标</th>
																<th width="12%">得分上限</th>
																<th width="12%">得分下限</th>
																<th width="12%">得分</th>
																<th>操作</th>
															</tr>
														</thead>
														<tbody id="dataShow">
															
														</tbody>
														<tbody id="dataOpt">
															<tr style="height: 50px;">
																<td colspan="8" style="text-align: center;vertical-align:bottom; ;border-top:1px dashed #ccc;">
																	<button class="btn btn-info btn-primary btn-xs margin-left-5 dfUserChooseBtn" 
																	type="button">被评分人选择
																	</button>
																</td>
															</tr>
														</tbody>
														
													</table>
													</li>
				                              </div>
			                             </div>
		                            </div>
	                            </div>
                            </div> 
                        </div>
					</div>
				</div>
			</div>
		</div>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
