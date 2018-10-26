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
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script> 
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
$(function() {
	//设置滚动条高度
	var height = $(window).height()-10;
	$("#contentBody").css("height",height+"px");
});
/**
 * 添加附件验证
 */
function addFile(){
	if($("#subState").val()==1){
		return false;
	}
	$("#fileDivAjax").html('');
	 $("select[multiple=multiple]").each(function(){
	      var index = 0;
	      var pp = $(this);
	      $(this).children().each(function(i){
	        var input = $('<input>');  
           input.attr("name",pp.attr("list")+"["+index+"]."+pp.attr("listkey"));  
           input.attr("type","hidden");  
           input.attr("value",$(this).val());  
           $("#fileDivAjax").append(input); 
           
           var inputname = $('<input>');  
           inputname.attr("name",pp.attr("list")+"["+index+"]."+pp.attr("listvalue"));  
           inputname.attr("type","hidden");  
           inputname.attr("value",$(this).text());  
          $("#fileDivAjax").append(inputname); 
           index ++;  
	      });
	  });
	 
	 var flag = false;
		$(".subform").ajaxSubmit({
		        type:"post",
		        url:"/item/addStageInfoFile?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        async: false,
		        beforeSubmit:function (a,f,o){
		        	
		        	var len = $("#listFiles_id").find("option").length;
		        	if(len==0){
		        		window.top.layer.msg("请上传文档");
		            	return false;
		        	}else{
		            	return true;
		        	}
				}, 
		        success:function(data){
			        if('y'==data.status){
			        	flag = true;
			        }else{
			        	window.top.layer.msg(data.info);
		        		
			        }
		        },error:function(XmlHttpRequest,textStatus,errorThrown){
		        	showNotification(2,"系统错误，请联系管理人员");
		        }
		 });
		 $("#subState").val(0)
		 return flag;
  
}

var fileTypes="*.rar;*.zip;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;*.pdf;*.mp3;*.gif;*.jpg;*.jpeg;*.png;*.bmp;*.avi;*.wma;*.rmb;*.rm;*.flash;*.flv;*.mp4;*.mid;*.3gp";
var fileSize="200MB";
var	numberOfFiles = 200;
</script>
</head>
<body style="background-color: #fff" >
<input type="hidden" id="subState" value="0">
<form class="subform"  method="post" action="">
	<tags:token></tags:token>
	<input type="hidden" name="itemId" value="${stagedInfo.itemId}"/>
	<input type="hidden" name="stagedItemId" value="${stagedInfo.stagedItemId}"/>
	<input type="hidden" name="parentId" value="${item.parentId}"/>
	
	<div id="fileDivAjax">
	</div>
	
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget no-margin-top no-margin-bottom no-padding-top no-padding-bottom" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">上传附件</span>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="widget-body margin-top-20" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
										<tags:uploadMore name="listFiles.id" showName="fileName" ifream="" comId="${sessionUser.comId}"></tags:uploadMore>
                                   	</ul>
                                </div>
                            </div>
                          </div>
                        </div>
					
					</div>
				</div>
			</div>
		</div>
	</form>
</body>
</html>
