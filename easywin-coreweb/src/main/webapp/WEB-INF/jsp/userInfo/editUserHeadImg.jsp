<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script type="text/javascript"> 

var fileTypes="*.gif;*.jpg;*.jpeg;*.png;*.bmp;";
var fileSize="200MB";
var	numberOfFiles = 200;
function loadUpfilesForHead(fileId,sid,userId,comId){
		$("#"+fileId).uploadify( {
			//开启调试
			'debug' : false,
			//是否自动上传
			'auto' : true,
			//支持多文件上传
			'multi' : true,
			'method':'POST',
			//超时时间
			'successTimeout' : 99999,
			//flash
			'swf' : "/static/plugins/uploadify/uploadify.swf?t="+Math.random(),
			//不执行默认的onSelect事件
			'overrideEvents' : [ 'onDialogClose' ],
			//文件选择后的容器ID
			'queueID' : 'queue',
			//服务器端脚本使用的文件对象的名称 $_FILES个['upload']
			'fileObjName' : 'pic',
			//上传处理程序
			'uploader' : '/upload/getHeadImg?sid=${param.sid}&t='+Math.random(),
			//浏览按钮的背景图片路径
			// 'buttonImage':'${pageContext.request.contextPath}/js/jquery.uploadify/uploadify-cancel.png',
			//浏览按钮的宽度
			'width' : '80',
			//浏览按钮的高度
			'height' : '30',
			'rollover' : true,
			'removeCompleted' : false,
			'removeTimeout' : 1,
			//expressInstall.swf文件的路径。
			//'expressInstall':'expressInstall.swf',
			//在浏览窗口底部的文件类型下拉菜单中显示的文本
			//'fileDesc' : '支持格式:xls/xlsx/doc/docx/ppt/pptx/txt/dwg/zip/rar/jpg/gif/jpeg/png/bmp/ASF/nAVI/AVI/MPEG/MPG/WMV/flv/MP4/3GP/MOV/ASX/RMVB/pdf.', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
			//'simUploadLimit' : 2,//每次最大上传文件数   
			//'uploadLimit' : 2,//每次最大上传文件数   
			'displayData' : 'percentage',//有speed和percentage两种，一个显示速度，一个显示完成百分比 
			//允许上传的文件后缀
			'fileTypeExts' : fileTypes,
			//上传文件的大小限制
			'fileSizeLimit' : fileSize,
			'buttonText' : '选择文件',//浏览按钮
			//上传数量
			//'queueSizeLimit' : 2,
			//每次更新上载的文件的进展
			'onUploadProgress' : function(file,
					bytesUploaded, bytesTotal,
					totalBytesUploaded,
					totalBytesTotal) {
				//有时候上传进度什么想自己个性化控制，可以利用这个方法
				//使用方法见官方说明
			},
			//选择上传文件后调用
			'onSelect' : function(file) {
			},
			//返回一个错误，选择文件的时候触发
			'onSelectError' : function(file,
					errorCode, errorMsg) {
				switch (errorCode) {
				case -100:
					art.dialog({"content":"上传的文件数量已经超出系统限制的"
							+ $('#'+fileId)
									.uploadify(
											'settings',
											'queueSizeLimit')
							+ "个文件！"}).time(2);
					break;
				case -110:
					art.dialog({"content":"文件 ["
							+ file.name
							+ "] 大小超出系统限制的"
							+ $('#'+fileId)
									.uploadify(
											'settings',
											'fileSizeLimit')
							+ "大小！"}).time(2);
					break;
				case -120:
					art.dialog({"content":"文件 [" + file.name
							+ "] 大小异常！"}).time(2);
					break;
				case -130:
					art.dialog({"content":"文件 [" + file.name
							+ "] 类型不正确！"}).time(2);
					break;
				}
			},
			//检测FLASH失败调用
			'onFallback' : function() {
				art.dialog({"content":"您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。"}).time(2);
			},
			//上传到服务器，服务器返回相应信息到data里
			'onUploadSuccess' : function(file,
					data, response) {
				var rs = eval('(' + data + ')');
				 var state = rs.state;
				 if(state=='y'){
						// 原图
						$("#orgFilePath").val(rs.orgImgPath);
						$("#orgFileName").val(rs.orgImgName);
						$("#largeImgPath").val(rs.largeImgPath);
						var org =rs.largeImgPath+"?t="+Math.random();
						var headHtml ='<img src="'+org+'" style="width:210px;height:210px"></img>' ;
						$("#viewPic").html(headHtml);
			         }
			},
			//把文件名传给后台
			'onUploadStart' : function(file) {
				$("#"+fileId)
						.uploadify(
								"settings",
								'formData',
								{
									'fileInputFileName' : file.name,
									'pageFileName':fileId,
									'comId':comId,
									'userId':userId
								});
			},
			'onQueueComplete' : function(stats) {
				//alert("文件上传成功");
          } 

		})
	}

//设置访问成功后的跳转地址
function setLocation(){
	$("#redirectPage").val(window.self.location);
}
function updateImg(){
	 $("#imgForm").ajaxSubmit({
	        type:"post",
	        url:"/userInfo/editPic?sid=${param.sid}&t="+Math.random(),
	        dataType: "json", 
	        beforeSubmit:function(a,f,o){
		        if(!$("#largeImgPath").val()){
		        	showNotification(1,"请选择头像！")
		        	return false;
			    }
	    	},
	        success:function(data){
		        if('y'==data.status){
		        	showNotification(0,"修改成功！");
		        	var org = "/downLoad/down/"+data.bigImgUuid+"/"+data.bigImgName+"?sid=${param.sid}&t="+Math.random();
					var headHtml ='<img src="'+org+'"></img>' ;
		        	$("#nowPic").html(headHtml);
					//右边大头像
		       		$(window.parent.bigheadImg).html(headHtml);
		       		//左边方头像
		       		$(window.parent.userInfo_Pic).html(headHtml);

		        	$("#viewPic").html('');

		        	$("#orgFilePath").val('');
					$("#orgFileName").val('');
					$("#largeImgPath").val('');
		        	
				}
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	showNotification(1,"系统错误，请联系管理人员！")
	        }
	    });
}
//设置等级
function setLev(){
	var jifenScore = ${sessionUser.jifenScore};
	//用户现有积分
	$(window.parent.jifenScore).html(jifenScore);
	//本阶段最少分数
	var minLevJifen = ${sessionUser.minLevJifen};
	//下一阶段的最低分数
	var nextLevJifen = ${sessionUser.nextLevJifen};
	//设计需要的积分
	$(window.parent.needJifen).html(nextLevJifen-jifenScore);
	//积分等级标准
	$(window.parent.s2).html(jifenScore+'/'+nextLevJifen);

	//等级名称
	var levName = '${sessionUser.levName}';
	$(window.parent.levName).html(levName);
	var headImgHtml = '';
	var bigImgUuid = '${sessionUser.bigImgUuid}';
	var bigImgName = '${sessionUser.bigImgName}';
	var gender = '${sessionUser.gender}';
	headImgHtml +='<img src="/downLoad/userImg/${sessionUser.comId}/${sessionUser.id}?sid=${param.sid}&size=1" onclick="explainAddJifen(1,2)"></img>';
	$(window.parent.imgTd).html(headImgHtml);
	var percent = parseInt(((jifenScore-minLevJifen)/(nextLevJifen-minLevJifen)*100)+'');
	$(window.parent.s1).css("width",percent+"%");
}
</script>
</head>
<body style="background-color: #fff" onload="resizeVoteH('userCenter');setLocation();setLev()">
<div id="home" class="tab-pane active">
	<div id="tabs-1" class="panel-collapse collapse in">
		<!--nav-tabs style 1-->
		<div class="tc-tabs">
			<!-- Nav tabs style 2-->
			<ul class="nav nav-tabs tab-color-dark background-dark">
				<li><a href="/userInfo/editUserBaseInfo?sid=${param.sid}">基本信息</a></li>
				<li class="active"><a href="/userInfo/editUserHeadImg?sid=${param.sid}">头像设置</a></li>
				<li><a href="/userInfo/editUserTel?sid=${param.sid}">联系信息</a></li>
				<li><a href="/userInfo/immediateSuper?sid=${param.sid}">直属上级</a></li>
				<li><a href="/userInfo/handOverPage?sid=${param.sid}">离职交接</a></li>
				<li><a href="/usagIdea/listPagedUsagIdea?sid=${param.sid}">常用意见</a></li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="row">
						<div class="col-sm-12">
							<div class="panel-body">
								<div class="ws-now pull-left">
									<h6>当前头像</h6>
									<div class="panel-body" id="nowPic">
										<img src='/downLoad/userImg/${sessionUser.comId}/${sessionUser.id}?sid=${param.sid}&size=1'/>
									</div>
								</div>
								<div class="ws-current pull-left" style="padding-top: 0px">
									<h6>头像预览</h6>
									<div class="panel-body ws-choicePic pull-left" id="viewPic" style="padding: 0px">
									</div>
									<div class="panel-body pull-left">
											<div>
									      		<div>
										      		<div id="queue" style="width: 300px;display: none">
													</div>
							 						<div>
									   					 <input type="file" name="pic" id="pic"/>
													</div>
													<script type="text/javascript">
													loadUpfilesForHead('pic','${param.sid}','${sessionUser.id}','${sessionUser.comId}');
													</script>
									      		</div>
												<br />
												<small>图片像素建议210*210</small>
											</div>
											<div style="margin-top: 55px">
												<form id="imgForm">
										      		<input type="hidden" name="id" value="${sessionUser.id }"/>
													<input type="hidden" name="orgFilePath" id="orgFilePath" value=""/>
										      		<input type="hidden" name="orgFileName" id="orgFileName" value=""/>
										      		<input type="hidden" name="largeImgPath" id="largeImgPath" value=""/>
													<button class="btn btn-info ws-btnBlue2 pull-left" type="button" onclick="updateImg()">确定</button>
												</form>
											</div>
									</div>
									
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--nav-tabs style 2-->
	</div>
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>

