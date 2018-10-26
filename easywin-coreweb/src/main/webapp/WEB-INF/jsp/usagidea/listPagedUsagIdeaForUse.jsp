<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
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
<title><%=SystemStrConstant.TITLE_NAME %></title>
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript"> 
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
//常用意见
function add(){
	window.top.layer.open({
		  type: 2,
		  //title: ['常用意见', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['400px', '250px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/usagIdea/addUsagIdeaPage?sid=${param.sid}&rnd=" + Math.random(),'no'],
		  btn: ['确定', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var flag = iframeWin.addIdeaForm();
			  if(flag){
				 window.top.layer.close(index); 
				 window.self.location.reload();
			  }
			  
		  }
		  ,cancel: function(){ 
		  }
		});
}
//修改常用意见
function update(id){
	
	
	window.top.layer.open({
		  type: 2,
		  //title: ['常用意见', 'font-size:18px;'],
		  title:false,
		  closeBtn:0,
		  area: ['400px', '250px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  content: ["/usagIdea/updateUsagIdeaPage?sid=${param.sid}&id="+id+"&rnd=" + Math.random(),'no'],
		  btn: ['确定', '取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var flag = iframeWin.updateIdeaForm();
			  if(flag){
				 window.top.layer.close(index); 
				 window.self.location.reload();
			  }
			  
		  }
		  ,cancel: function(){ 
		  }
		});
}
/**
 * 返回意见
 */
function useIdea(){
	var item = $('input[name="ids"]:checked');
	var ideaContent="";
	if(item.length>0){
		ideaContent = item.val();
	}
	var result = {"ideaContent":ideaContent}
	return result;
}
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
});
</script>
<script type="text/javascript">
$(function(){
	//操作删除和复选框
	$('.optTr').mouseover(function(){
		var display = $(this).find(".optTd .optCheckBox").css("display");
		if(display=='none'){
			$(this).find(".optTd .optCheckBox").css("display","block");
			$(this).find(".optTd .optRowNum").css("display","none");
		}
	});
	$('.optTr ').mouseout(function(){
		var optCheckBox = $(this).find(".optTd .optCheckBox");
			var check = $(optCheckBox).find("input").attr('checked');
			if(check){
				$(this).find(".optTd .optCheckBox").css("display","block");
				$(this).find(".optTd .optRowNum").css("display","none");
			}else{
				$(this).find(".optTd .optCheckBox").css("display","none");
				$(this).find(".optTd .optRowNum").css("display","block");
			}
	});
	
	$(":radio[name='ids'][disabled!='disabled']").click(function(){
		$(".optRowNum").css("display","block");
		$(".optCheckBox").css("display","none");
		$(this).parent().parent().find(".optCheckBox").css("display","block");
		$(this).parent().parent().find(".optRowNum").css("display","none");
	});
	$(".optTr").click(function(){
	    $(this).find("[name='ids']").attr("checked","checked");
		$(".optRowNum").css("display","block");
		$(".optCheckBox").css("display","none");
		$(this).find(".optCheckBox").css("display","block");
		$(this).find(".optRowNum").css("display","none");
	});
});
</script>
<style type="text/css">
.panel-body{padding:0px 0px}
.pagination{
margin:10px 0px;
margin-top: 2px
}
label {
	margin-bottom: 0px;
	height: 20px
}
</style>
</head>
<body style="background-color: #fff">
	<c:choose>
		<c:when test="${not empty list}">
		<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget no-margin-bottom" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">常用意见</span>
                        <div class="widget-buttons ps-toolsBtn">
                        
                        	<button class="btn btn-primary btn-xs" type="button" onclick="add()">
                             		<i class="fa fa-plus"></i>
                             		常用意见
                             	</button>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div>
                     <!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->       
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;">
                     
                     		<table class="table table-striped table-hover" style="margin:2px 10px">
                                        <thead>
                                            <tr role="row">
                                                <td width="10%" style="padding: 8px 10px" >
													<h5>选择</h5></td>
												<td width="65%" style="padding: 8px 10px">
													<h5>意见内容</h5></td>
												<td width="15%" style="padding: 8px 10px">
													<h5>操作</h5></td>
                                            </tr>
                                        </thead>
                                        <tbody>
									 		<c:forEach items="${list}" var="obj" varStatus="vs">
										 		<tr class="optTr">
	                                                <td class="optTd">
											 			<label class="optCheckBox" style="display: none;width: 20px">
										 					<input class="colored-blue" type="radio" name="ids" value="${obj.idea}"/>
										 					<span class="text"></span>
											 			</label>
	                                                	<label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
	                                                </td>
	                                                <td style="cursor: pointer;" class="nameRow" title="${obj.idea}">
									 					<tags:cutString num="46">${obj.idea}</tags:cutString>
									 				</td>
									 				<td><a href="javascript:void(0)" onclick="update(${obj.id})">修改</a></td>
	                                            </tr>
									 		</c:forEach>
                                        </tbody>
                                    </table>
						<tags:pageBar url="/usagIdea/listPagedUsagIdeaForUse"></tags:pageBar>
					</div>
				</div>
			</div>
		</div>
		</div>
		</c:when>
		<c:otherwise>
		<div class="container" style="left:50%;top:50%;position: absolute; margin:-110px 0 0 -245px;padding-top:0px;text-align:center;width:488px;">
				<section class="error-container text-center">
					<h1>
						<i class="fa fa-exclamation-triangle"></i>
					</h1>
					<div class="error-divider">
						<h2>您还没有添加过常用意见！</h2>
						<p class="description">协同提高效率，分享拉近距离。</p>
						<a href="javascript:void(0);" onclick="add('${param.sid}');"
							class="return-btn"><i class="fa fa-plus"></i>新增常用意见</a>
					</div>
				</section>
			</div>
		</c:otherwise>
	</c:choose>
    <script src="/static/assets/js/bootstrap.min.js"></script>
    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>

