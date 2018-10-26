<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">	
  <head>
    
    <title>My JSP 'MyJsp.jsp' starting page</title>
    
	<script type="text/javascript" src="/static/FlexPaper/js/jquery.js"></script>
	<script type="text/javascript" src="/static/FlexPaper/js/flexpaper_flash.js"></script>

  </head>
  
  <body>
  <div style="position:absolute;left:10px;top:10px;">
	        <a id="viewerPlaceHolder" style="width:660px;height:480px;display:block"></a>
	        
	        <script type="text/javascript"> 
				var fp = new FlexPaperViewer(	
						 '/static/FlexPaper/FlexPaperViewer',
						 'viewerPlaceHolder', { config : {
						 SwfFile : escape('/upload/down/1111/12212'),
						 Scale : 0.6, 
						 ZoomTransition : 'easeOut',
						 ZoomTime : 0.5,
						 ZoomInterval : 0.2,
						 FitPageOnLoad : true,
						 FitWidthOnLoad : false,
						 PrintEnabled : true,
						 FullScreenAsMaxWindow : false,
						 ProgressiveLoading : false,
						 MinZoomSize : 0.2,
						 MaxZoomSize : 5,
						 SearchMatchAll : false,
						 InitViewMode : 'Portrait',
						 
						 ViewModeToolsVisible : true,
						 ZoomToolsVisible : true,
						 NavToolsVisible : true,
						 CursorToolsVisible : true,
						 SearchToolsVisible : true,
  						
  						 localeChain: 'en_US'
						 }});
	        </script>
        </div>
  </body>
</html>
