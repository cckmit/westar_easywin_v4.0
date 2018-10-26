<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- Force latest IE rendering engine or ChromeFrame if installed -->
<!--[if IE]>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<![endif]-->
<meta charset="utf-8">
<title>jQuery File Upload Demo</title>
<meta name="description" content="File Upload widget with multiple file selection, drag&amp;drop support, progress bars, validation and preview images, audio and video for jQuery. Supports cross-domain, chunked and resumable file uploads and client-side image resizing. Works with any server-side platform (PHP, Python, Ruby on Rails, Java, Node.js, Go etc.) that supports standard HTML form file uploads.">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap styles -->
<link rel="stylesheet" href="/static/plugins/bootstrap/css/bootstrap.min.css">
<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
<link rel="stylesheet" href="/static/plugins/jQuery-File-Upload-9.5.7/css/jquery.fileupload.css">
<link rel="stylesheet" href="/static/plugins/jQuery-File-Upload-9.5.7/css/jquery.fileupload-ui.css">
<!-- CSS adjustments for browsers with JavaScript disabled -->
<noscript><link rel="stylesheet" href="/static/plugins/jQuery-File-Upload-9.5.7/css/jquery.fileupload-noscript.css"></noscript>
<noscript><link rel="stylesheet" href="/static/plugins/jQuery-File-Upload-9.5.7/css/jquery.fileupload-ui-noscript.css"></noscript>
</head>
<body style="padding-top: 5px;">

<div class="container">
    <!-- The file upload form used as target for the file upload widget -->
    <form id="fileupload" action="/upload/addFile" method="post" enctype="multipart/form-data">
        <input type="hidden" name="sid" value="${param.sid}" />
        <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
        <div class="row fileupload-buttonbar">
            <div class="col-lg-7">
                <!-- The fileinput-button span is used to style the file input field as button -->
                <span class="btn btn-success fileinput-button">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span>选择</span>
                    <input type="file" name="files" multiple>
                </span>
                <!-- <button type="submit" class="btn btn-primary start">
                    <i class="glyphicon glyphicon-upload"></i>
                    <span>上传</span>
                </button> -->
                <button type="reset" class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>取消</span>
                </button>
                <button type="button" class="btn btn-danger delete">
                    <i class="glyphicon glyphicon-trash"></i>
                    <span>删除</span>
                </button>
                <font id="remark" style="color: red"></font>
                <!-- The global file processing state -->
                <span class="fileupload-process"></span>
            </div>
            <!-- The global progress state -->
            <div class="col-lg-5 fileupload-progress fade">
                <!-- The global progress bar -->
                <div style="height: 5px;" class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
                    <div class="progress-bar progress-bar-success" style="width:0%;"></div>
                </div>
                <!-- The extended global progress state -->
                <div class="progress-extended">&nbsp;</div>
            </div>
        </div>
        
        <!-- The table listing the files available for upload/download -->
        <table role="presentation" class="table table-striped">
       <tr>
        <td style="width: 10px;">
            <input type="checkbox" class="toggle" id="titleToggle">          
        </td>
        <td>
                         文件名
        </td>
        <td>
                       文件大小
        </td>
        <td style="width:170px;">
                      操作
        </td>
    </tr>
        <tbody class="files"></tbody></table>
    </form>
</div>

<!-- The template to display files available for upload -->
<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
        <td>
            
        </td>
        <td>
            <p class="name">{%=file.name%}</p>
            <strong class="error text-danger"></strong>
        </td>
        <td>
            <p class="size">Processing...</p>
            <div style="height:5px;" class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>
        </td>
        <td style="width:170px;">
            {% if (!i && !o.options.autoUpload) { %}
                <!--<button class="btn btn-primary start" disabled>
                    <i class="glyphicon glyphicon-upload"></i>
                    <span>上传</span>
                </button>-->
            {% } %}
            {% if (!i) { %}
                <button class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>取消</span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>
<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        <td>
            <input type="checkbox" name="delete" value="1" class="toggle">
        </td>
         <td>
            <p class="name">
                {% if (file.url) { %}
                    <a style="color:green" href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" {%=file.thumbnailUrl?'data-gallery':''%}>{%=file.name%}</a>
                {% } else { %}
                    <span>{%=file.name%}</span>
                {% } %}
            </p>
            {% if (file.error) { %}
                <div><span class="label label-danger">Error</span> {%=file.error%}</div>
            {% } %}
        </td>
        <td>
            <span class="size">{%=o.formatFileSize(file.size)%}</span>
        </td>
        <td style="width:170px;">
            {% if (file.deleteUrl) { %}
                <button class="btn btn-danger delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"{% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                    <i class="glyphicon glyphicon-trash"></i>
                    <span>删除</span>
                </button>
                <input type="hidden" name="resultId" id="resultId" value="{%=file.id%}">
                <input type="hidden" name="resultName" id="resultName" value="{%=file.name%}">
            {% } else { %}
                <button class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>取消</span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
<script src="/static/plugins/jQuery-File-Upload-9.5.7/js/vendor/jquery.ui.widget.js"></script>
<!-- The Templates plugin is included to render the upload/download listings -->
<script src="/static/plugins/templates-master/js/tmpl.min.js"></script>
<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<script src="/static/plugins/load-Image-master/js/load-image.min.js"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<script src="/static/plugins/Canvas-to-Blob-master/js/canvas-to-blob.min.js"></script>
<!-- Bootstrap JS is not required, but included for the responsive demo navigation -->
<script src="/static/plugins/bootstrap/js/bootstrap.min.js"></script>
<!-- blueimp Gallery script -->
<script src="/static/plugins/Gallery-master/js/jquery.blueimp-gallery.min.js"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="/static/plugins/jQuery-File-Upload-9.5.7/js/jquery.iframe-transport.js"></script>
<!-- The basic File Upload plugin -->
<script src="/static/plugins/jQuery-File-Upload-9.5.7/js/jquery.fileupload.js"></script>
<!-- The File Upload processing plugin -->
<script src="/static/plugins/jQuery-File-Upload-9.5.7/js/jquery.fileupload-process.js"></script>
<!-- The File Upload image preview & resize plugin -->
<script src="/static/plugins/jQuery-File-Upload-9.5.7/js/jquery.fileupload-image.js"></script>
<!-- The File Upload audio preview plugin -->
<script src="/static/plugins/jQuery-File-Upload-9.5.7/js/jquery.fileupload-audio.js"></script>
<!-- The File Upload video preview plugin -->
<script src="/static/plugins/jQuery-File-Upload-9.5.7/js/jquery.fileupload-video.js"></script>
<!-- The File Upload validation plugin -->
<script src="/static/plugins/jQuery-File-Upload-9.5.7/js/jquery.fileupload-validate.js"></script>
<!-- The File Upload user interface plugin -->
<script src="/static/plugins/jQuery-File-Upload-9.5.7/js/jquery.fileupload-ui.js"></script>
<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE 8 and IE 9 -->
<!--[if (gte IE 8)&(lt IE 10)]>
<script src="/static/plugins/jQuery-File-Upload-9.5.7/js/cors/jquery.xdr-transport.js"></script>
<![endif]-->
</body> 
<script type="text/javascript">
var fileTypes=art.dialog.data('fileTypes');
var fileSize=art.dialog.data('fileSize');
var numberOfFiles=art.dialog.data('numberOfFiles');
var remark = "";
if(fileSize!=undefined&&fileSize!=""){
	remark="&nbsp;&nbsp;大小限制:"+fileSize+"M";
	fileSize=1024*1024*parseInt(fileSize);
}else{
	//10M
	fileSize=1024*1024*10;
	remark="&nbsp;&nbsp;大小限制:10M";
}

if(numberOfFiles!=undefined&&numberOfFiles!=""){
	
}else {
	numberOfFiles = 200;
}
remark+="&nbsp;&nbsp;数量限制:"+numberOfFiles;

var t = "";
if(fileTypes!=undefined&&fileTypes!=""){
	t=fileTypes;
	fileTypes="/(\\.|\\/)("+t+")$/i";
}else {
	t="gif|jpe?g|png|bmp|docx?|xlsx?|pptx?|txt|rar|zip|avi|mp4|flv";
	fileTypes="/(\\.|\\/)("+t+")$/i";
}
remark+="&nbsp;&nbsp;文件类型:"+t;
$('#remark').html(remark);
$(function () {
    'use strict';
    // Initialize the jQuery File Upload widget:
    $('#fileupload').fileupload({
        // Uncomment the following to send cross-domain cookies:
        //xhrFields: {withCredentials: true},
        url: '/upload/addFile?sid=${param.sid}'
    });
    // Enable iframe cross-domain access via redirect option:
    $('#fileupload').fileupload(
        'option',
        'redirect',
        window.location.href.replace(
            /\/[^\/]*$/,
            '/cors/result.html?%s'
        )
    );

 // Load existing files:
    $('#fileupload').fileupload('option', {
        //url: '//jquery-file-upload.appspot.com/',
        // Enable image resizing, except for Android and Opera,
        // which actually support image resizing, but fail to
        // send Blob objects via XHR requests:
        disableImageResize: /Android(?!.*Chrome)|Opera/
            .test(window.navigator.userAgent),
        minFileSize:1,
        maxFileSize: fileSize,
        maxNumberOfFiles:numberOfFiles,
        acceptFileTypes: eval(fileTypes)
    });
    
    // Upload server status check for browsers with CORS support:
    	$.ajax({
            // Uncomment the following to send cross-domain cookies:
            //xhrFields: {withCredentials: true},
            url: $('#fileupload').fileupload('option', 'url'),
            dataType: 'json',
            context: $('#fileupload')[0]
        }).always(function () {
            $(this).removeClass('fileupload-processing');
        }).done(function (result) {
            $(this).fileupload('option', 'done')
                .call(this, $.Event('done'), {result: result});
        });
});



var fileId=art.dialog.data('fileId');
 function returnFile(){
	 var ids = $("[name='resultId']");
	 var names = $("[name='resultName']");
	 art.dialog.data('names',names);
	 art.dialog.data('ids',ids);
	 art.dialog.close();
	 return false;
 }
</script>
</html>
