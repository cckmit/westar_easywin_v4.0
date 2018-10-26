<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
//提交 验证
function addUpgradeLog(){
	var versionName = $("#versionName").val();
	var upgardeContent =$("#upgardeContent").val();
	if(!$(document).find("input[type='radio'][id='Android']").is(':checked')){
		if(!$.trim(versionName)){
				var a = $("#versionName").offset().top;
			    if(a<0){
				  $("#versionName").parents("div").scrollTop(0);
			    }
			  layer.tips('请填写版本名', "#versionName", {tips: 3});
				return false;
			}
			if(!$.trim(upgardeContent)){
				var a = $("#upgardeContent").offset().top;
			    if(a<0){
				  $("#upgardeContent").parents("div").scrollTop(0);
			    }
				 layer.tips('请填写更新内容', "#upgardeContent", {tips: 3});
				return false;
			 }
		 $(".subform").submit();
	  }else{
		  var version = $("#version").val();
		  if(!$.trim(version)){
			  var a = $("#version").offset().top;
			  if(a<0){
				  $("#version").parents("div").scrollTop(0);
			  }
			  
			  layer.tips('请填写版本', "#version", {tips: 3});
				return false;
		  }
		  if(!$.trim(versionName)){
			  var a = $("#versionName").offset().top;
			  if(a<0){
				  $("#versionName").parents("div").scrollTop(0);
			  }
			  layer.tips('请填写版本名', "#versionName", {tips: 3});
				return false;
			}
			if(!$.trim(upgardeContent)){
				var a = $("#upgardeContent").offset().top;
			    if(a<0){
				  $("#upgardeContent").parents("div").scrollTop(0);
			    }
				  
				 layer.tips('请填写更新内容', "#upgardeContent", {tips: 3});
				return false;
			 }
		  if($("#tempFilePath").val().length>0){
			  $(".subform").submit();
		  }else{
			  window.scrollTo(0,$("#contentBody").Height);
			  layer.tips('请添加App附件', "#file", {tips: 3});
				return false;
		  }
	  } 
}
$(function() {
	//上传附件隐藏
	$("#file").hide();
	$("#versionLi").hide();
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	$("#Android").click(function(){
		$("#file").show();
		$("#versionLi").show();
		$("#version").val("${version}");
		$("#versionName").val("${versionName}");
	});
	$("#PC").click(function(){
		$("#file").hide();
		$("#versionLi").hide();
		$("#version").val('');
		$("#versionName").val('');
	});
	$("#IOS").click(function(){
		$("#file").hide();
		$("#versionLi").hide();
		$("#version").val('');
		$("#versionName").val('');
	});
	//版本名聚焦
	$("#versionName").focus();
});

var fileTypes="*.apk;";
var fileSize="200MB";
var	numberOfFiles = 200;

	
/**
 * 百度控件上传app附件
 * @param fileId 上传后附件的主键
 * @param iframe 所在ifream
 * @param filePicker 附件上传按钮
 * @param fileShow 附件上传展示界面
 * @return
 */
function loadWebUpApp(fileId,iframe,filePicker,fileShow,fileVal){
	//上传文件的URL
	var backEndUrl = '/web/addDepartApp';
	//各个不同的文件的MD5,用于上传
    var destMD5Dir = {md5:"",fileVal:fileVal};   //附件MD5信息
	WebUploader.Uploader.register({//注册插件
        "before-send-file": "beforeSendFile"
        , "before-send": "beforeSend"
        , "after-send-file": "afterSendFile"
    });
	var uploader = WebUploader.create({
	    swf: '/static/plugins/webuploader/Uploader.swf?t='+Math.random(),
	    server: backEndUrl,
	    pick: {id:'#'+filePicker,multiple: true},
	    duplicate: false,//不能选择重复的文件
	    resize: false,
	    auto: false,
	    fileNumLimit: null,
	    fileVal:fileVal,   //指明参数名称，后台也用这个参数接收文件
	    fileSingleSizeLimit: WebUpfileConfig.fileLimitSize,
	    //fileType:'rar,zip,doc,xls,docx,xlsx,pdf'
	    accept: {
	        title: 'apk',
	        extensions: 'apk',
	        mimeTypes: '.apk',
	    },
	    chunked:false,
	    formData:function(){return $.extend(true, {}, destMD5Dir);}
	});
	uploader.on("error",function(type,reason){
    	if (type=="Q_TYPE_DENIED"){
    		window.top.layer.alert(reason.ext+"格式不支持")
    	}else if(type=="Q_EXCEED_NUM_LIMIT"){
    		window.top.layer.alert('上传的文件数量已经超出系统限制的'+reason+"个");
    	}else if(type=="F_EXCEED_SIZE"){
    		window.top.layer.alert('文件太大不支持');
    	}
	});
    uploader.on('fileQueued', function( file ) {  
    	// webuploader事件.当选择文件后，文件被加载到文件队列中，触发该事件。等效于 uploader.onFileueued = function(file){...} ，类似js的事件定义。
	   //正在上传 隐藏上传控件
    	$("#"+filePicker).hide();
    	var $list = $("#"+fileShow);
	   var fileSize = WebUploader.formatSize(file.size);
	   var dw = '';
	   //载入文件 
	   var html = '<div id="' + file.id + '" class="uploadify-queue-item">';
	   html+='			<div class="cancel">';
	   html+='				<a href="javascript:void(0)" fileId=0>X</a>'
	   html+='			</div>';
	   var fileName = cutstr(file.name,25);
	   html+='			<span class="fileName" title="'+file.name+'">' + fileName + ' ('+fileSize+')</span>';
	   html+=' 			<span class="data"> - 0%</span>';
	   html+='			<div class="uploadify-progress">';
	   html+='				<div class="uploadify-progress-bar" style="width:0%;"><!--Progress Bar--></div>';
	   html+='			</div>';
	   html+='		</div>';
	   $list.append(html);
	   
	 //调整页面
		if(iframe && ""!=iframe){
			resizeVoteH(iframe);
		}else{
			scrollPage();
		}
	
	   uploader.upload(file);
   });
    //加载成功
	 uploader.on('uploadSuccess', function (file, data) {
		 UploadComlateApp(file,fileShow,fileId);
		 $("#"+fileId).val(data.tempFilePath);
		 
    });
	 //上传进度
    uploader.on( 'uploadProgress', function( file, percentage ) {
	    var $li = $( '#'+file.id ),
        $percent = $li.find('.uploadify-progress-bar');
	    var pre = ((percentage-0).toFixed(2)) * 100;
	    $('#' + file.id).find('.data').html(' - '+pre+'%')
	    $percent.css( 'width', percentage * 100 + '%' );
    });
    uploader.on('uploadError', function (file,reason) {
	      $('#' + file.id).find('.data').text(' 上传出错');
	});
    
    //修复model内部点击不会触发选择文件的BUG
    $("#"+filePicker+" .webuploader-pick").click(function () {
        $("#"+filePicker+" :file").click();
    });
    
  // 如果要删除的文件正在上传（包括暂停），则需要发送给后端一个请求用来清除服务器端的缓存文件
	$("#"+fileShow).on("click", ".cancel", function(){
		$("#"+fileId).val('');
		var fileDone = $(this).parents(".uploadify-queue-item").find("div.cancel>a").attr("fileDone");
   		if(!fileDone){
   			//插件自带的附件主键
   			var pageFileId = $(this).parents(".uploadify-queue-item").attr("id");
   			//从上传文件列表中删除
   			uploader.removeFile(pageFileId);	
   		}
   		//页面移除附件
   		var obj =  $(this).parents(".uploadify-queue-item");
   	    $(obj).fadeOut();
   	    $(obj).remove();
   	    
   	    //显示上传控件
   	    $("#"+filePicker).show();
   	    
   	   //调整页面
   		if(iframe &&  ""!=iframe){
   			resizeVoteH(iframe);
   		}else{
   			scrollPage();
   		}
   	   
	 	//已上传完成 删除文件
	   $.ajax({
             type: "POST"
             , url: backEndUrl
             , data: {
               status: "del", 
             }
             , cache: false
             , dataType: "json"
         }).then(function(data, textStatus, jqXHR){
       	   
         }, function(jqXHR, textStatus, errorThrown){
         	$('#' + file.id).find('.data').text(' 删除出错');
         });
 	});
}
	/**
	 * 传文件
	 * @param file 待上传附件
	 * @param fileShow 结果集合所在位置
	 * @return
	 */
	function UploadComlateApp(file,fileShow,fileId){
		//附件展示的div
		var fileDiv = $("#"+fileShow).find("#"+file.id);
		
		//设置进度为100%,随后设置成完成
		$(fileDiv).find(".data").html(' - '+100+'%');
		$(fileDiv).find(".data").text(' 完成');
		$percent = $(fileDiv).find('.uploadify-progress-bar');
		$percent.css( 'width', 1 * 100 + '%' );
	}
  	
  	
</script>
</head>
<body>
<div class="container" style="padding: 0px 0px;width: 100%">	
	<div class="row" style="margin: 0 0">
		<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
   		<div class="widget" style="margin-top: 0px;">
   			<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
            <div class="widget-caption">
				<span class="themeprimary ps-layerTitle">更新日志添加</span>
			</div>
		 		<div class="widget-buttons">
				<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
					<i class="fa fa-times themeprimary"></i>
				</a>
				</div> 
             </div>
             
            <div class="widget-body no-shadow  margin-top-40"  id="contentBody" style="overflow: hidden;overflow-y:scroll;">
             	<div class="tickets-container bg-white margin-left-20">
             		<form class="subform" method="post" action="/web/upLog/addUpgradeLog">
					<ul class="tickets-list">
						 <li class="ticket-item no-shadow ps-listline no-margin"  style="height: 70px;" id="versionLi">
						    <div class="pull-left gray ps-left-text" >
						    	<i class="fa fa-bookmark blue"></i>&nbsp;更新版本
						    	<span style="color: red">*</span>
						    </div>
							<div class="ticket-user pull-left ps-right-box"style="height: auto;" >
							  	<div class="pull-left" >
									<input id="version" datatype="*"  name="version" nullmsg="请填写更新版本"
									class="colorpicker-default form-control " type="text" readonly="readonly" style="width:350px;">
								</div>
							</div>
							
	                    </li>
	                    <li class="ticket-item no-shadow ps-listline margin-top-10"  style="height: 70px;">
						    <div class="pull-left gray ps-left-text" >
						    	<i class="fa fa-bookmark blue"></i>&nbsp;更新版本名
						    	<span style="color: red">*</span>
						    </div>
							<div class="ticket-user pull-left ps-right-box"style="height: auto;">
							  	<div class="pull-left" >
									<input id="versionName" datatype="*"  name="versionName" nullmsg="请填写更新版本名"
									class="colorpicker-default form-control " type="text"  style="width:350px;">
								</div>
							</div>
	                    </li>
	                    
	                    <li class="ticket-item no-shadow ps-listline">
						    <div class="pull-left gray ps-left-text">
								<i class="fa fa-volume-off blue" style="font-size: 15px"></i>&nbsp;更新平台
								<span style="color: red">*</span>
						    </div>
							<div class="ticket-user pull-left ps-right-box">
								<label class="padding-left-5">
								 	<input type="radio" class="colored-blue" name="terraceType" checked="checked" id="PC" value="0"/>
								 	<span class="text" style="color:inherit;">电脑版</span>
							    </label>
							    <label class="padding-left-5">
								 	<input type="radio" class="colored-blue" name="terraceType"  id="IOS" value="1"/>
								 	<span class="text" style="color:inherit;">苹果版</span>
							    </label>
							    <label class="padding-left-5">
								 	<input type="radio" class="colored-blue" name="terraceType"  id="Android" value="2"/>
								 	<span class="text" style="color:inherit;">安卓版</span>
							    </label>
							</div>
						</li>
						<!-- <li class="ticket-item no-shadow ps-listline" style="clear:both">
						    <div class="pull-left gray ps-left-text">
						    	<i class="fa fa-clock-o blue"></i>&nbsp;更新时间
						    	<span style="color: red">*</span>
						    </div>
							<div class="ticket-user pull-left ps-right-box">
							   <input class="colorpicker-default form-control" datatype="*" readonly="readonly"  nullmsg="请填写更新时间"
								 name="upgradeTime" onClick="WdatePicker()" type="text" >
							</div>               
                         </li> -->
	                     <li class="ticket-item no-shadow ps-listline">
						    <div class="pull-left gray ps-left-text">
						    	<i class="fa fa-bookmark blue"></i>&nbsp;更新内容
						    	<span style="color: red">*</span>
						    </div>
							<div class="ticket-user pull-left ps-right-box padding-bottom-5" style="height: auto;width:350px;">
						  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" 
						  		style="height:150px;font-size:12px;float:left;" datatype="*" id="upgardeContent" nullmsg="请填写更新内容"
						  		name="content" placeholder="更新内容……"></textarea>
							</div> 
							<div class="ps-clear"></div>
	                    </li>
	                    <li class="ticket-item no-shadow ps-listline" id="file">
							<div class="pull-left gray ps-left-text">
								<i class="fa fa-file blue"></i>&nbsp;相关附件</div>
							<div class="ticket-user pull-left ps-right-box"
								style="width: 400px;height: auto;">
								<div class="margin-top-10">
									<tags:uploadApp name="tempFilePath" showName="fileName"
										ifream=""></tags:uploadApp>
								</div>
							</div>
							<div class="ps-clear"></div>
						</li>
                    </ul>
                   </form>
               </div>
          </div>
        </div>
        </div>
	</div>
</div>
</body>
</html>
