<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="t" uri="/WEB-INF/tld/t.tld"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<script type="text/javascript">
		//打开页面body
		var openWindowDoc;
		//打开页面,可调用父页面script
		var openWindow;
		//注入父页面信息
		function setWindow(winDoc,win){
			openWindowDoc = winDoc;
			openWindow = win;
		}
		//任务属性菜单切换
		$(function(){
			//任务讨论
			$("#fileTalkMenuLi").click(function(){
				$(this).parent().find("li").removeAttr("class");
				$("#fileTalkMenuLi").attr("class","active");
				$("#otherFileIframe").attr("src","/fileCenter/listPagedFileTalks?sid=${param.sid}&pager.pageSize=15&fileId=${fileId}&busId=${busId}&busType=${busType}");
			});
			//阅读情况
			$("#fileViewRecordLi").click(function(){
				$(this).parent().find("li").removeAttr("class");
				$("#fileViewRecordLi").attr("class","active");
				$("#otherFileIframe").attr("src","/common/listViewRecord?sid=${param.sid}&busId=${fileId}&busType=013&ifreamName=otherFileIframe");
			});
			//分享信息日志
			$("#fileLogMenuLi").click(function(){
				$(this).parent().find("li").removeAttr("class");
				$("#fileLogMenuLi").attr("class","active");
				$("#otherFileIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${fileId}&busType=013&ifreamName=otherFileIframe");
			});
			//设置滚动条高度
			var height = $(window).height()-40;
			$("#contentBody").css("height",height+"px");
			$("#ifreamSrc").css("height",height+"px")
		})
		//设置图片显示大小
		function resetImg(objImg){
			
			var img = new Image();
			img.src = objImg.src;
			
			//宽度
			var w = img.width;
			//高度
			var h = img.height;
			if(w>h){//横长
				$(objImg).css("max-width","650px")
				$(objImg).css("height","auto")
			}else{//竖长
				var height = $(window).height();
				$(objImg).css("width","auto")
				$(objImg).css("max-height",height)
			}
		}
		//查看原图
		function viewOrg(){
			console.log("${filepath}")
			window.top.viewOrgByPath("${filepath}?sid=${param.sid}");
		}
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
          <div id="contentBody">
	          <div style="position:fixed;left:10px;top:40px;;width: 62.5%;height: 90%;text-align: center;">
				<img src="${filepath}?sid=${param.sid}" title="点击查看原图" onload="resetImg(this)" onclick="viewOrg()"/>
			</div>
			<div style="position:absolute;right:0px;top:10px;;width: 37%;height: 90%;overflow: auto;">
				<div class="widget-body no-shadow padding-top-5 padding-bottom-5">
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
	                        
	                         <div class="tab-content tabs-flat no-padding bg-white" id="ifreamSrc">
	                               <iframe style="width:100%;" id="otherFileIframe"
									src="/fileCenter/listPagedFileTalks?sid=${param.sid}&pager.pageSize=15&fileId=${fileId}&busId=${busId}&busType=${busType}"
									border="0" frameborder="0" allowTransparency="true"
									noResize  scrolling="no" vspale="0"></iframe>
	                         </div>
	                    </div>
	                 </div>
	              </div>
			</div>
		
         </div>  
		 
	</body>
</html>
