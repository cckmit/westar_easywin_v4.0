<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<body>
<div class="page-content"> 
<!-- Page Body -->
  <div class="page-body padding-5">
      <div class="row">
          <div class="col-md-12 col-xs-12">
          	 <div class="profile-container">
                  <div class="profile-header row">
                      <div class="col-lg-2 col-md-4 col-sm-12 text-center">
			     			<img class="header-avatar" src="/downLoad/userImg/${userInfo.comId}/${userInfo.id}?size=1"></img>
                      </div>
                      <div class="col-lg-5 col-md-8 col-sm-12 profile-info">
                          <div class="header-fullname">${userInfo.userName}</div>
                          <!-- 个性签名 -->
                          <script type="text/javascript">
	                      	$(function(){
	                        	//鼠标移上显示边框
	                        	$("#selfIntroView").mouseenter(function(){
	                        		$(this).css("border","1px solid #A9A9A9");
	                        	});
	                        	//鼠标移除后移除边框
	                        	$("#selfIntroView").mouseleave(function(){
	                        		$(this).css("border","1px solid #fff");
	                        	})
	                        	//点击查看
	                        	$("#selfIntroView").click(function(){
	                        		$(this).css("display","none");
	                        		$(this).css("border","1px solid #fff");
	                        		$("#selfIntroEdit").css("display","block");
	                        		$("#selfIntro").focus();
	                        		var selfIntr = $(this).text();
	                        		if(selfIntr=="个人签名"){
	                        			selfIntr = '';
	                        		}
	                        		$("#selfIntro").val(selfIntr)
	                        	})
	                       		$("#selfIntro").keydown(function(e){
	                        		if (e.keyCode == 13) {
	                        	    	return false;
	                        	    }
	                        	  });
	                        	  //鼠标移除后移除边框
	                        	  $("#selfIntro").blur(function(){
	                        		  var selfIntr = $("#selfIntro").val();
	                        		  if($("#selfIntroView").text()!=selfIntr){
			                  				$.ajax({
			                  					 type : "post",
			                  					  url : "/userInfo/updateUserIntr?sid=${param.sid}&rnd="+Math.random(),
			                  					  dataType:"json",
			                  					  data:{selfIntr:selfIntr},
			                  					  success:function(data){
			                  						  if(data.status=='y'){
			                  							 if(selfIntr==""){
				  	                        			     selfIntr = '个人签名';
					  	                        		  }
					  	                        		  $("#selfIntroView").html(selfIntr);
			                  							showNotification(1,"设置成功");
			                  						  }else{
			                  							showNotification(2,data.info);
			                  						  }
			                  						  
				  	                        		  
				  	                        		  $("#selfIntroEdit").css("display","none");
				  	                        		  $("#selfIntroView").css("display","block");
				  	                        		  $("#selfIntroView").css("border","1px solid #fff");
			                  					  }
			                  				});
	                        		  }else{
	                        			  $("#selfIntroEdit").css("display","none");
	  	                        		  $("#selfIntroView").css("display","block");
	  	                        		  $("#selfIntroView").css("border","1px solid #fff");
	                        		  }
	                        		  
	                        	  })
	                          })
                          </script>
                          <div>
                          	<div class="header-information" id="selfIntroView" 
                          		style="display: block;min-height: 25px;border:1px solid #fff">${empty userInfo.selfIntr?'个人签名':userInfo.selfIntr }</div>
                          	<div class="header-information" id="selfIntroEdit" style="display: none;min-height: 25px">
                             	<textarea  id="selfIntro" style="width: 100%;overflow: hidden;" rows="3">
                             	</textarea>
                          	</div>
                          </div>
                          
                          
                          
                      </div>
                      <div class="col-lg-5 col-md-12 col-sm-12 col-xs-12 profile-stats">
                          <div class="row">
                              <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 stats-col">
                              	<a href="javascript:void(0)" onclick="menuClick('/msgShare/toDoJobs/jobsCenter?sid=${param.sid}&pager.pageSize=10')">
                                  <div class="stats-value pink">${todoNums}</div>
                                  <div class="stats-title">待办事项</div>
                                  </a>
                              </div>
                              <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 stats-col">
                              	<a href="javascript:void(0)"  onclick="menuClick('/attention/attCenter?sid=${param.sid}&pager.pageSize=10')">
                                  <div class="stats-value pink">${attenNums}</div>
                                  <div class="stats-title">关注更新</div>
                                  </a>
                              </div>
                              <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 stats-col">
                              	<a href="javascript:void(0)" onclick="menuClick('/task/listTaskToDoPage?pager.pageSize=10&sid=${param.sid}&activityMenu=task_m_1.1&overdue=true');">
                                  <div class="stats-value pink">${overdueNums}</div>
                                  <div class="stats-title">超期事项</div>
                                 </a>
                              </div>
                          </div>
                          <div class="row">
                              <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 inlinestats-col">
                                  <i class="glyphicon glyphicon-map-marker"></i>${userInfo.depName}
                              </div>
                              <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 inlinestats-col">
                                 <i class="fa fa-mobile fa-lg"></i>${userInfo.movePhone}&nbsp;
                              </div>
                              <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 inlinestats-col">
                               <i class="fa fa-tag fa-lg"></i>${userInfo.levName}
                              </div>
                          </div>
                      </div>
                  </div>
                  <div class="profile-body">
                      <div class="col-lg-12">
                          <div class="tabbable">
                              <div class=" tabs-flat" style="margin-top: 20px;">
                              	<jsp:include page="listShareMsg.jsp"></jsp:include>
                              </div>
                      	</div>
                  	</div>
             	 </div>
					
              
          </div>             
  	</div>
 <!-- /Page Body -->
</div>
<!-- /Page Content -->
</div>
<!-- /Page Container -->
</body>
</html>

