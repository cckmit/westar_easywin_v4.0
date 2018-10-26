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
    <script type="text/javascript">

        $(document).ready(function() {
            resizeVoteH('otherIframe1');
            //初始化名片
            initCard('${param.sid}');
        });
        
      //删除分享附件
    	function delDailyFile(ts,dailyId,dailyUpFileId,type){
    		window.top.layer.confirm("确定删除附件?", {icon: 3, title:'确认对话框'}, function(index){
    			window.top.layer.close(index);
    			$.post("/daily/delDailyUpfile?sid=${param.sid}",{Action:"post",dailyId:dailyId,dailyUpFileId:dailyUpFileId,type:type},     
    					function (data){
    					if(data.status=='y'){
    						showNotification(1,"操作成功");
    						$(ts).parents("tr").remove();
    						$.each($(".fileOrder"),function(index,item){
    							$(this).html(index+1)
    						})
    					}else{
    						showNotification(2,data.info);
    						
    					}
    			},"json");
    		});
    		
    	}
        

    </script>
    <style type="text/css">
        #infoList table{
            table-layout: fixed;
        }
        #infoList td,#infoList th{
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden;
        }
    </style>

</head>
<body style="background-color:#FFFFFF">
<div id="infoList">
    <table  class="table table-hover general-table" style="background-color:#FFFFFF;width: 100%">
        <tr>
            <td width="10%" height="40">
                <h5>序号</h5></td>
            <td width="20%" height="40">
                <h5>名称</h5></td>
            <td width="35%" height="40">
                <h5>上传时间</h5></td>
            <td width="20%" height="40">
                <h5>上传人</h5></td>
            <td width="100px" height="40" align="center"><h5>操作</h5></td>
        </tr>
        <c:choose>
            <c:when test="${not empty dailyFiles}">
                <c:forEach items="${dailyFiles}" var="fileVo" varStatus="status">
                    <tr>
                        <td height="40">${ status.count}</td>
                        <td align="left">${ fileVo.fileName}</td>
                        <td>${fileVo.upTime}</td>
                        <td style="text-align: left;">
                            <div class="ws-position" data-container="body" data-toggle="popover" data-placement="left"
                                 data-user='${fileVo.userId}' data-busId='${fileVo.dailyId}' data-busType='1'>
                                <img src="/downLoad/userImg/${userInfo.comId }/${fileVo.userId}"
                                     title="${fileVo.username}" class="user-avatar"/>
                                <i class="ws-smallName">${fileVo.username}</i>
                            </div>
                        </td>
                        <td height="30" align="center">
                            <c:choose>
                                <c:when test="${fileVo.fileExt=='doc' || fileVo.fileExt=='docx' || fileVo.fileExt=='xls' || fileVo.fileExt=='xlsx' || fileVo.fileExt=='ppt' || fileVo.fileExt=='pptx' }">
                                    &nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0)" onclick="downLoad('${fileVo.uuid}','${fileVo.fileName}','${param.sid}')" title="下载"></a>
                                    &nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage('${fileVo.upfileId}','${fileVo.uuid}','${fileVo.fileName}','${fileVo.fileExt}','${param.sid}','1','${fileVo.dailyId}')" title="预览">
                                    </a>
                                </c:when>
                                <c:when test="${fileVo.fileExt=='txt' || fileVo.fileExt=='pdf'}">
                                    &nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/${fileVo.uuid}/${fileVo.fileName}?sid=${param.sid}" title="下载"></a>
                                    &nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage('${fileVo.upfileId}','${fileVo.uuid}','${fileVo.fileName}','${fileVo.fileExt}','${param.sid}','1','${fileVo.dailyId}')" title="预览">
                                    </a>
                                </c:when>
                                <c:when test="${fileVo.fileExt=='jpg'||fileVo.fileExt=='bmp'||fileVo.fileExt=='gif'||fileVo.fileExt=='jpeg'||fileVo.fileExt=='png'}">
                                    &nbsp;&nbsp;<a class="fa fa-download"  href="/downLoad/down/${fileVo.uuid}/${fileVo.fileName}?sid=${param.sid}" title="下载"></a>
                                    &nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="showPic('/downLoad/down/${fileVo.uuid}/${fileVo.fileName}','${param.sid}','${fileVo.upfileId}','1','${fileVo.dailyId}')" title="预览"></a>
                                </c:when>
                                <c:otherwise>
                                    &nbsp;&nbsp;<a class="fa fa-download"  onclick="downLoad('${fileVo.uuid}','${fileVo.fileName}','${param.sid}')" title="下载"></a>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${empty delete && fileVo.userId==userInfo.id}">
					 			<a style="margin-left:5px;" class="fa  fa-times-circle fa-lg" href="javascript:void(0);" title="删除" onclick="delDailyFile(this,${fileVo.dailyId},${fileVo.id},'${fileVo.type}')"></a>
			 				</c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td height="25" colspan="7" align="center"><h3>没有相关信息！</h3></td>
                </tr>
            </c:otherwise>
        </c:choose>
    </table>
</div>

<tags:pageBar url="/msgShare/msgShareUpfilePage" maxIndexPages="3"></tags:pageBar>

<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
