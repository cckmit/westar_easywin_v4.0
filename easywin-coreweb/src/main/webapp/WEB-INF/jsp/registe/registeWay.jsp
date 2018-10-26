<%@page import="com.westar.core.web.InitServlet"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<%pageContext.setAttribute("requestURI",request.getRequestURI().replace("/","_").replace(".","_"));%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<link rel="stylesheet" type="text/css" href="/static/plugins/Validform/css/validform.css" >
<script type="text/javascript" src="/static/plugins/Validform/js/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>

<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script src="/static/plugins/layer/layer.js" type="text/javascript" charset="utf-8"></script>

<link rel="stylesheet" type="text/css" href="/static/css/web/style.css">
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">

var account = '${joinTemp.account}';
var joinTempId = '${joinTemp.id}';

$(function(){
	//加入团队
	$("#joinOrgBtn").click(function(){
		//展示操作区域
		$("#orgOptDiv").css("display","block");
		//创建团队按钮出现
		$("#createOrgDiv").css("display","block");
		//加入团队按钮隐藏
		$("#joinOrgDiv").css("display","none");
		//创建团队操作
		$("#createOrgInfo").css("display","none");
		//团队查询隐藏
		$("#joinOrg").css("display","block");
	})
	//创建团队按钮
	$("#createOrgBtn").click(function(){
		//展示操作区域
		$("#orgOptDiv").css("display","block");
		//创建团队按钮隐藏
		$("#createOrgDiv").css("display","none");
		//加入团队按钮出现
		$("#joinOrgDiv").css("display","block");
		//创建团队操作隐藏
		$("#createOrgInfo").css("display","block");
		//团队查询出现
		$("#joinOrg").css("display","none");
		
	})
	$(".orgName").click(function(){
		var comId = $(this).attr("orgNum");
		$.ajax({  
	        url : "/registe/redirectLogIn?t="+Math.random(),  
	        type : "POST",  
	        dataType : "json",
	        data:{account:account,joinTempId:joinTempId,comId:comId},
	        success : function(data) { 
	        	if(data.status=='y'){
	        		window.location.replace("/index?sid="+data.sid);
	        	}else{
	        		layer.alert(data.info,{icon:5}); 
	        	}
	        },error:function(){
	        	layer.alert("系统错误，请联系客服",{icon:5}); 
	        } 
	    });
	})
	//创建团队
	$("#createOrg").click(function(){
		var orgName = $("#orgName").val();
		if(orgName){
			$.ajax({  
		        url : "/registe/createOrg?t="+Math.random(),  
		        type : "POST",  
		        dataType : "json",
		        data:{account:account,joinTempId:joinTempId,orgName:orgName},
		        success : function(data) { 
		        	if(data.status=='y'){
		        		var account = data.account;
		        		localStorage.setItem("a_i_p_m_l_c",account); 
		        		window.location.replace("/registe/toIndexPage?sid="+data.sid);
		        	}else{
		        		layer.alert(data.info,{icon:5}); 
		        	}
		        },error:function(){
		        	layer.alert("系统错误，请联系客服",{icon:5}); 
		        } 
		    });
		}else{
			layer.alert("请设置团队名称"); 
		}
		
	});
	
	$("#searchOrg").click(function(){
		var searchName = $("#searchName").val();
		if(searchName){//需要搜索
			$.ajax({  
		        url : "/registe/searchOrgList?t="+Math.random(),  
		        type : "POST",  
		        dataType : "json",
		        data:{account:account,joinTempId:joinTempId,searchName:searchName},
		        success : function(data) {
		        	if(data.status=='f'){
		        		layer.alert(data.info,{icon:5});
		        	}else{
		        		$("#tipMsg").hide();
		        		var orgList = data.orgList;
		        		var resultMore = data.resultMore;
		        		if(orgList.length>0){
		        			$("#orgListTable").show();
		        			var len = orgList.length;
		        			if(resultMore=='yes'){
		        				len=8;
		        				$("#tipMsg").show();
			        			$("#tipMsg").find(".tip-info").html("企业名称太模糊！");
		        			}
		        			var html = "";
		        			for(var i=0;i<len;i++){
		        				var organic = orgList[i];
		        				html += "<tr>";
		        				html += "<td>"+organic.orgNum+"</td>";
		        				html += "<td>"+organic.orgName+"</td>";
		        				html += "<td>"+organic.linkerName+"</td>";
		        				console.log(organic.linkerName)
		        				var recordCreateTime = organic.recordCreateTime;
		        				recordCreateTime = recordCreateTime.substring(0,10)
		        				html += "<td>"+recordCreateTime+"</td>";
		        				if(organic.isSysUser=='1'){//已经是系统用户
			        				html += "<td class='optTd'><a href='javascript:void(0)'>系统用户</a></td>";
		        				}else if(organic.isApplying=='1'){//正在申请
			        				html += "<td class='optTd'><a href='javascript:void(0)' class='exit' onclick=\"applyInOrg('"+organic.orgNum+"',1,this)\">取消申请</button></td>";
		        				}else{
			        				html += "<td class='optTd'><a href='javascript:void(0)' class='applyIn' onclick=\"applyInOrg('"+organic.orgNum+"',0,this)\">申请加入</button></td>";
		        				}
		        				html += "</tr>";
		        			}
		        			$("#orgList").html(html);
		        		}else{
		        			$("#tipMsg").show();
		        			$("#orgListTable").hide();
		        			$("#tipMsg").find(".tip-info").html("没查询到您需要的企业！");
		        		}
		        	}
		        }  
		    });
		}
	})
	
});
//申请加入或取消申请
function applyInOrg(comId,applyState,ts){
	$(ts).attr("disabled","disabled");
	$.ajax({  
        url : "/registe/applyInOrg?t="+Math.random(),  
        type : "POST",  
        dataType : "json",
        async:false,
        data:{account:account,joinTempId:joinTempId,comId:comId,applyState:applyState},
        success : function(data) { 
        	if(data.status=='f'){
        		layer.alert(data.info,{icon:5});
        	}else if(data.status=='f1'){//已申请加入该团队
        		$(ts).attr("onclick","applyInOrg('"+comId+"',1,this)");
        		$(ts).attr("class","exit");
        		$(ts).html("取消申请");
        		layer.alert(data.info,{icon:5});
        	}else if(data.status=='f2'){//已取消申请加入该团队
        		$(ts).attr("onclick","applyInOrg('"+comId+"',0,this)");
        		$(ts).attr("class","applyIn");
        		$(ts).html("申请加入");
        		layer.alert(data.info,{icon:5});
        	}else{
        		if(applyState==0){
	        		$(ts).attr("onclick","applyInOrg('"+comId+"',1,this)");
	        		$(ts).attr("class","exit");
	        		$(ts).html("取消申请");
        		}else{
	        		$(ts).attr("onclick","applyInOrg('"+comId+"',0,this)");
	        		$(ts).attr("class","applyIn");
	        		$(ts).html("申请加入");
        		}
        	}
        },error:function(){
        	layer.alert("系统错误，请联系客服",{icon:5}); 
        }  
    });
	$(ts).removeAttr("disabled");
}
</script>
<style>
	.pop-content dl{
		line-height: 30px
	}
	.stepLi{
		width: 290px;
		height: 52px;
		text-align: center;
		line-height: 52px;
		color: #fff;
	}
	.step1{background: url(/static/images/web/regist-step-1-1.png) no-repeat;}
	.step2{background: url(/static/images/web/regist-step-2-1.png) no-repeat;}
	.step3{background: url(/static/images/web/regist-step-3-1.png) no-repeat;}
	 .center-table{ width:100%; border:1px solid #ddd; border-bottom: 0; border-right:0; margin:20px 0;}
	 .center-table td{ text-align: left;border:1px solid #ddd; border-top: 0; border-left:0; padding:5px 15px;}
	 .center-table a{ width:60px;height:30px; line-height:30px; display:block; text-align:center;color:#fff; font-size: 12px; border-radius:4px;background:#2298DB;}
	 a.applyIn{ background:#090;}
	 a.exit{ background:#aaa;}
	 .browsertip{ margin-top:50px;}
	 .next-btn{width:200px; margin:0 auto;}
	 .finish-btn{ background:#2298DB;width:145px; height: 42px;line-height: 42px;color:#fff;text-align:center;float:left;}
	 .tip-info{
	  background-color:inherit;color:blue;text-align: center;border: 0;line-height: 10px;height: 10px;
	 }
	 .orgName{
	 	cursor: pointer;
	 }
	 .orgName:HOVER{
	 	color:#24a3ed;cursor: pointer;
	 }
</style>
</head>
<body>
	<!----头部开始---->
	<div class="top">
		<jsp:include page="/WEB-INF/jsp/web/head.jsp"></jsp:include>
	</div>
	<!--注册 S-->
    <div class="register-box" >
    	<div class="flow-box">
    		<ul class="flow-menu">
    			<li class="stepLi step1">1、账号注册 </li>
    			<li class="stepLi step2">2、账号验证</li>
    			<li class="stepLi step3">3、创建或加入团队</li>
    		</ul>
    	</div>
    	
    	<div class="browsertip" style="width: 780px;color:gray">
    		<c:choose>
    			<c:when test="${not empty listUserAllOrg }">
	    			<div style="font-size: 15px;line-height: 30px">
	    				<div style="font-size: 15px;">您的账号<b style="padding: 0 2px">${joinTemp.account}</b>已加入以下团队。${isCreator?'您还可以加入其他团队':'您可以加入或创建团队'}</div>
	    				<table class="center-table">
			    				<tr>
			    					<td>团队号</td>
			    					<td>团队名称</td>
			    					<td>创建人</td>
			    					<td>创建日期</td>
			    				</tr>
		    				
		    					<c:forEach items="${listUserAllOrg}" var="orgObj" varStatus="vs">
		    						<tr>
		    							<td>${orgObj.orgNum}</td>
		    							<td class="orgName" orgNum ='${orgObj.orgNum}' title="进入该团队">${orgObj.orgName}</td>
		    							<td>${orgObj.linkerName}</td>
		    							<td>${fn:substring(orgObj.recordCreateTime,0,10)}</td>
		    						</tr>
		    					</c:forEach>
		    				
		    			</table>
					</div>
    			</c:when>
    			<c:otherwise>
					<div style="font-size: 15px;line-height: 30px">
	    				<div style="font-size: 15px;">您的账号<b style="padding: 0 2px">${joinTemp.account}</b>未加入任何团队。您可以加入或创建团队</div>
					</div>
    			</c:otherwise>
    		</c:choose>
	    	<div style="font-size: 15px;padding:10px 0 15px 0;" id="joinOrgDiv">
		    	<a href="javascript:void(0)" class="next-btn" id="joinOrgBtn">加入团队</a>
		    	<p style="text-align:center;padding-top:10px;">(搜索团队名称，申请加入已有团队)</p>
    		</div>
    		<c:if test="${not isCreator}">
		    	<div style="font-size: 15px;padding:10px 0 15px 0;text-align: center;" id="createOrgDiv">
			    	<a href="javascript:void(0)" class="next-btn" style="background-color:#090" id="createOrgBtn">创建团队</a>
			    	<p style="text-align:center;padding-top:10px;">(没有需要的团队，自己创建团队)</p>
	    		</div>
    		</c:if>
    		
    		<div id="orgOptDiv" style="display: none">
    			<hr  width="780px" style= "border: 1px dashed #ccc;">
    			<c:if test="${not isCreator}">
	    			<div id="createOrgInfo" style="padding:20px 0 20px 50px;display:none" align="center">
	    				<div class="form-group" style="height: 42px;">
			    			<label>团队名称:<span style="color: red;padding:0px 5px">*</span></label>
			    			<div style="float:left;">
				    			<input class="input-form" id="orgName" name="orgName" value="" placeholder="请输入团队名称"/>
			    			</div>
			    			<a href="javascript:void(0)" class="finish-btn" id="createOrg">完成，开始使用</a>
			    		</div>
			    		
	    			</div>
    			</c:if>
    			
    			<div id="joinOrg" style="display: block;padding-top:20px;">
    				<div class="form-group" style="height: 42px;">
		    			<div style="float:left;padding-left:150px">
			    			<input class="input-form" id="searchName" name="searchName" value="" placeholder="请输入团队名称"/>
		    			</div>
		    			<a href="javascript:void(0)" class="finish-btn" id="searchOrg">搜索团队</a>
		    			
		    		</div>
		    		<div style="padding-left:100px;padding-top: 0px;display: none" id="tipMsg">
			    		<div class="input-form tip-info"></div>
		    		</div>
	    			<table class="center-table" id="orgListTable" style="display: none">
		    			<thead>
		    				<tr>
		    					<td>团队号</td>
		    					<td>团队名称</td>
		    					<td>创建人</td>
		    					<td>创建日期</td>
		    					<td>操作</td>
		    				</tr>
		    			</thead>
	    				<tbody id="orgList">
			    				
	    				</tbody>
	    				
	    			</table>
    			</div>
    			
    		</div>
		</div>
    </div>
	<!--登录框 E-->
	<div class="bottom">
		<jsp:include page="/WEB-INF/jsp/web/bottom.jsp"></jsp:include>
	</div>
</body>

</html>
