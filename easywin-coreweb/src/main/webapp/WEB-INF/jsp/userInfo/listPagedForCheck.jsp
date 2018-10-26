<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
	<!-- Page Content -->
        <div class="page-content">
         	<!-- Page Body -->
            <div class="page-body">
            	<div class="row">
                	<div class="col-md-12 col-xs-12 ">
                    	<div class="widget">
                         	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
								<div class="btn-group pull-left">
									<div class="table-toolbar ps-margin">
                                       	<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick="agree()">
												批量同意
											</a>
                                       	</div>
                                   	</div>
									<div class="table-toolbar ps-margin">
                                       	<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick="reject()">
												批量拒绝
											</a>
                                       	</div>
                                   	</div>
								</div>
                            </div>
                            <c:choose>
											 	<c:when test="${not empty list}">
                            <div class="widget-body">
	                        	<form method="post" id="delForm">
									 <input type="hidden" name="sid" value="${param.sid }"/>
									 <input type="hidden" name="redirectPage"/>
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr role="row">
                                                <td width="5%">
													<h5>
													<label>
													<input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'ids')">
													<span class="text" style="color: inherit;"></span>
												</label>
													</h5></td>
												<td width="20%">
													<h5>申请时间</h5></td>
												<td>
													<h5>账号</h5></td>
												<td width="15%">
													<h5>状态</h5></td>
												<td width="30%">
													<h5>留言</h5></td>
												<td width="10%">
													<h5>操作</h5></td>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	
											 		<c:forEach items="${list}" var="joinRecordVo" varStatus="vs">
												 		<tr class="optTr">
			                                                <td class="optTd" style="height: 47px">
													 			<label class="optCheckBox" style="display: none;width: 20px">
												 					<input class="colored-blue" type="checkbox" name="ids" value="${joinRecordVo.id}" checkState='${joinRecordVo.checkState}'/>
												 					<span class="text"></span>
													 			</label>
			                                                	<label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
			                                                </td>
			                                                <td>${fn:substring(joinRecordVo.recordCreateTime,0,16)}</td>
											 				<td>${joinRecordVo.account}</td>
											 				<td style="color: ${joinRecordVo.checkState==2?'red':joinRecordVo.checkState==1?'gray':'blue' }">${ joinRecordVo.checkState==2?'已拒绝':joinRecordVo.checkState==1?'待加入':'待审核'}
											 				</td>
											 				<td>
											 					${ joinRecordVo.joinNote}
											 				</td>
											 				<td>
											 					 <span>
											 					 	<c:choose>
											 					 		<c:when test="${joinRecordVo.checkState==1}">
														 					 <a href="javascript:void(0)" onclick="sendEmail(this,'${joinRecordVo.account}','${joinRecordVo.confirmId }')">重发邮件</a>
											 					 		</c:when>
											 					 		<c:otherwise>
														 					 <a href="javascript:void(0)" onclick="check(${joinRecordVo.id})">${ joinRecordVo.checkState==2?"重新审核":"审核" }</a>
											 					 		</c:otherwise>
											 					 	</c:choose>
											 					 </span>
											 				</td>
			                                            </tr>
											 		</c:forEach>
											 	
                                        </tbody>
                                    </table>
                                    </form>
                                    <tags:pageBar url="/userInfo/listPagedForCheck"></tags:pageBar>
                                </div>
                                </c:when>
								 	<c:otherwise>
								 	<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
								<section class="error-container text-center">
									<h1>
										<i class="fa fa-exclamation-triangle"></i>
									</h1>
									<div class="error-divider">
										<h2>您还没相关申请信息！</h2>
										<p class="description">协同提高效率，分享拉近距离。</p>
									</div>
								</section>
							</div>
								 	</c:otherwise>
								 </c:choose>
                            </div>
                        </div>             
                </div>
                <!-- /Page Body -->
            </div>
            <!-- /Page Content -->
            
        </div>
        <script type="text/javascript">
        
        $(function(){
        	//操作删除和复选框
        	$('.optTr').mouseover(function(){
        		var display = $(this).find(".optTd .optCheckBox").css("display");
        		if(display=='none'){
        			$(this).find(".optTd .optCheckBox").css("display","block");
        			$(this).find(".optTd .optRowNum").css("display","none");
        		}
        	});
        	$('.optTr ').mouseout(function(){
        		var optCheckBox = $(this).find(".optTd .optCheckBox");
        			var check = $(optCheckBox).find("input").attr('checked');
        			if(check){
        				$(this).find(".optTd .optCheckBox").css("display","block");
        				$(this).find(".optTd .optRowNum").css("display","none");
        			}else{
        				$(this).find(".optTd .optCheckBox").css("display","none");
        				$(this).find(".optTd .optRowNum").css("display","block");
        			}
        	});
        	
        	$(":checkbox[name='ids'][disabled!='disabled']").click(function(){
        		var checkLen = $(":checkbox[name='ids'][disabled!='disabled']:checked").length;
        		var len = $(":checkbox[name='ids'][disabled!='disabled']").length;
        		if(checkLen>0){
        			if(checkLen==len){
        				$("#checkAllBox").attr('checked', true);
        			}else{
        				$("#checkAllBox").attr('checked', false);
        			}
        		}else{
        			
        			$("#checkAllBox").attr('checked', false);
        		}
        	});
        });
        
	//审核用户
	function check(id){
		layer.closeAll();
		var url="/userInfo/checkUserInfoPage?sid=${param.sid}&id="+id;
		layui.use('layer', function(){
			var layer = layui.layer;
			window.top.layer.open({
				  id:'layerOpener',
				  type: 2,
				  title: false,
				  closeBtn: 0,
				  shade: 0.5,
				  shift:0,
				  scrollbar:false,
				  fix: true, //固定
				  maxmin: false,
				  move: false,
				  area: ['550px', '400px'],
				  content: [url,'no'], //iframe的url
				  btn:['审核','取消'],
				  yes:function(index,layero){
					  var iframeWin = window[layero.find('iframe')[0]['name']];
					  iframeWin.checkUserInfo();
				  },
				  success: function(layero,index){
					    var iframeWin = window[layero.find('iframe')[0]['name']];
					    iframeWin.setWindow(window.document,window);
					}
				});
		});
		
	}

	//批量拒绝
	function reject(){
		layui.use('layer', function(){
			var layer = layui.layer;
			//不选择审核通过的
			$(":checkbox[name='ids'][checkState='1']").attr("checked",false)
			if(checkCkBoxStatus('ids')){
				window.top.layer.confirm('确定要拒绝这些用户吗？', {
					  btn: ['确定','取消']//按钮
				  ,title:'询问框'
				  ,icon:3
				}, function(index){
					$("#delForm input[name='redirectPage']").val(window.location.href);
					$('#delForm').attr("action","/userInfo/rejectUserInfos");
					$('#delForm').submit();
				});	
			}else{
				window.top.layer.alert('请先选择一个用户。');
			}
		});
		
		
	}
	//批量同意
	function agree(){
		layui.use('layer', function(){
			var layer = layui.layer;
			//不选择审核通过的
			$(":checkbox[name='ids'][checkState='1']").attr("checked",false)
			if(checkCkBoxStatus('ids')){
				window.top.layer.confirm('确定要同意这些用户吗？', {
					  btn: ['确定','取消']//按钮
				  ,title:'询问框'
				  ,icon:3
				},  function(index){
					$("#delForm input[name='redirectPage']").val(window.location.href);
					$('#delForm').attr("action","/userInfo/agreeUserInfos");
					$('#delForm').submit();
				});	
			}else{
				window.top.layer.alert('请先选择一个用户。');
			}
		});
	}
	function searchJoinRecord(checkState){
		$("#checkState").val(checkState);
		$("#searchForm").submit();
	}
	

	
	//重新发送邮件
	function sendEmail(ts,email,confirmId){
		var onclick = $(ts).attr("onclick");
		$.ajax({
			 type : "post",
			  url : "/userInfo/resendEmailForCheck?sid=${param.sid}&rnd="+Math.random(),
			  dataType:"json",
			  data:{email:email,confirmId:confirmId},
			  beforeSend: function(XMLHttpRequest){
				  $(ts).removeAttr("onclick");
	         },
	         success : function(data){
		         if(data.status=='y'){
		        	 showNotification(1,"已发送！")
			     }else{
		        	 showNotification(2,"发送失败！")
				 }
		         var time = 10;
				 var sh;
		         sh = setInterval(function(){
		 			time--;
		 			$(ts).html(time+"秒后操作");
		 			$(ts).attr("style","cursor:default")
		 			if(time==0){
		 			$(ts).html("重发邮件")
			 			$(ts).attr("style","cursor:pointer")
		 				 $(ts).attr("onclick",onclick);
		 				clearInterval(sh);
		 			}
		 		},1000);
	         },
	         error:  function(XMLHttpRequest, textStatus, errorThrown){
		        	 showNotification(2,"系统错误！")
		        	 $(ts).attr("onclick",onclick);
		     }
		});
	}
	
</script>
</body>
</html>
