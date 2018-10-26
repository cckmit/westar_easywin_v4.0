<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">	
	<head>
		<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<script type="text/javascript" src="/static/FlexPaper/js/flexpaper_flash_debug.js"></script>
		<script type="text/javascript" src="/static/FlexPaper/js/flexpaper_flash.js"></script>
		<script type="text/javascript" src="/static/FlexPaper/js/jquery.js"></script>
		<script>
		
		//任务属性菜单切换
		$(function(){
			
			var height = $(window).height()-40;
			$("#contentBody").css("height",height+"px");
			
			//附件讨论
			$("#fileTalkMenuLi").click(function(){
				$(this).parent().find("li").removeAttr("class");
				$("#fileTalkMenuLi").attr("class","active");
				$("#otherFileIframe").attr("src","/fileCenter/listPagedFileTalks?sid=${param.sid}&pager.pageSize=15&fileId=${file.id}&busId=${busId}&busType=${busType}");
			});
			//附件查看记录
			$("#fileViewRecordLi").click(function(){
				$(this).parent().find("li").removeAttr("class");
				$("#fileViewRecordLi").attr("class","active");
				$("#otherFileIframe").attr("src","/common/listViewRecord?sid=${param.sid}&busId=${file.id}&busType=013&ifreamName=otherFileIframe");
			});
			//分享信息日志
			$("#fileLogMenuLi").click(function(){
				$(this).parent().find("li").removeAttr("class");
				$("#fileLogMenuLi").attr("class","active");
				$("#otherFileIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${file.id}&busType=013&ifreamName=otherFileIframe");
			});
			var height = $(window).height()-40;
			$("#ifreamSrc").css("height",height+"px")
		})
		</script>
		<style type="text/css">
			body:before{
				background: #fff
			}
			
		</style>
	</head>
	<body>
			<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
             	<span class="widget-caption themeprimary ps-layerTitle">附件预览</span>
                   <div class="widget-buttons">
					<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
						<i class="fa fa-times themeprimary"></i>
					</a>
				</div>
             </div>
	             
			 <div style="position:fixed;left:10px;top:40px;width: 62.5%;height: 90%;">
		        <a id="viewerPlaceHolder" style="display:block;width: 100%;height: 100%;"></a>
		        <script type="text/javascript"> 
					var fp = new FlexPaperViewer(	
						 '/static/FlexPaper/FlexPaperViewernew',
						 'viewerPlaceHolder', { config : {
						 SwfFile : escape('/upload/viewOfficeFile/${file.uuid}/${file.fileExt}'),
						 Scale : 0.9, 
						 ZoomTransition : 'easeOut',
						 ZoomTime : 0.5,
						 ZoomInterval : 0.2,
						 FitPageOnLoad : true,
						 FitWidthOnLoad : true,
						 PrintEnabled : true,
						 FullScreenAsMaxWindow : false,
						 ProgressiveLoading : true,
						 MinZoomSize : 0.2,
						 MaxZoomSize : 5,
						 SearchMatchAll : true,
						 InitViewMode : 'Portrait',
						 
						 ViewModeToolsVisible : true,
						 ZoomToolsVisible : true,
						 NavToolsVisible : true,
						 CursorToolsVisible : true,
						 SearchToolsVisible : true,
	 						
	 						 localeChain: 'zh_CN'
						 }
					});
		        </script>
	        </div>
		<div style="position:absolute;right:0px;top:10px;;width: 37%;height: 90%;overflow-y:auto" id="contentBody">
			 <div class="widget-body no-shadow padding-top-5 no-padding-bottom" id="viewerTab">
             	<div class="widget-main ">
                	<div class="tabbable">
                    	<ul class="nav nav-tabs tabs-flat" id="myTab11">
                        	<li class="active" id="fileTalkMenuLi">
                             	 <a data-toggle="tab" href="javascript:void(0)">留言</a>
                             </li>
                       		<li id="fileViewRecordLi">
                             	 <a data-toggle="tab" href="javascript:void(0)">阅读情况</a>
                             </li>
                             <li id="fileLogMenuLi">
	                             	<a href="javascript:void(0)" data-toggle="tab">日志记录</a>
							</li>
                        </ul>
                        
                         <div class="tab-content tabs-flat no-padding bg-white" id="ifreamSrc" >
                              <iframe style="width:100%;" id="otherFileIframe"
								src="/fileCenter/listPagedFileTalks?sid=${param.sid}&pager.pageSize=15&fileId=${file.id}&busId=${busId}&busType=${busType}"
								border="0" frameborder="0" allowTransparency="true"
								noResize  scrolling="no" vspale="0"></iframe>
                         </div>
                    </div>
                 </div>
              </div>
         </div>
	</body>
</html>