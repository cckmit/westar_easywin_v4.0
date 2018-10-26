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
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" id="holiBtn">
												<c:choose>
													<c:when test="${festMod.status=='1'}">
														<font style="font-weight:bold;">查询休假</font>
													</c:when>
													<c:otherwise>
														查询休假
													</c:otherwise>
												</c:choose>
											</a>
                                       	</div>
                                   	</div>
									<div class="table-toolbar ps-margin">
                                       	<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" id="workBtn">
												<c:choose>
													<c:when test="${festMod.status=='2'}">
														<font style="font-weight:bold;">查询换班</font>
													</c:when>
													<c:otherwise>
														查询换班
													</c:otherwise>
												</c:choose>
											</a>
                                       	</div>
                                   	</div>
								</div>
								
								<div class="widget-buttons ps-widget-buttons">
                                   	<button class="btn btn-info btn-primary btn-xs" type="button" id="addFestBtn">
                                   		<i class="fa fa-plus"></i>
                                   		日期维护
                                   	</button>
                                </div>
                                <form action="/festMod/listFestMod" id="searchForm">
 									<input type="hidden" name="sid" value="${param.sid }"/>
 									<input type="hidden" name="tab" value="${param.tab}"/>
 									<input type="hidden" name="year" value="${festMod.year}"/>
 									<input type="hidden" name="status" value="${festMod.status}"/>
 									
 									 <input type="hidden" name="activityMenu" value="${param.activityMenu }"/>
									 <input type="hidden" name="pager.pageSize" value="10">
 								</form>
                            </div>
                            <div class="widget-body">
	                        	<form method="post" id="delForm">
									 <input type="hidden" name="sid" value="${param.sid }"/>
									 <input type="hidden" name="redirectPage"/>
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr role="row">
                                            	<th width="8%" height="40">
															<h5>序号</h5></th>
												<th width="8%" height="40">
													<h5>年份</h5></th>
												<th width="8%" height="40">
													<h5>状态</h5></th>
												<th width="10%" height="40">
													<h5>开始时间</h5></th>
												<th width="10%" height="40">
													<h5>结束时间</h5></th>
												<th height="40">
													<h5>描述</h5></th>
											  	<c:if test="${userInfo.admin>0}">
												<th width="10%" height="40" style="text-align: center;">
													<h5>操作</h5></th>
												</c:if>
	                                           </tr>
                                        </thead>
                                        <tbody>
                                        	<c:choose>
											 	<c:when test="${not empty listFestMod}">
											 		<c:forEach items="${listFestMod}" var="festModVo" varStatus="vs">
												 		<tr class="optTr">
												 			 <td style="height: 47px">
			                                                	<label style="display: block;width: 20px">${vs.count}</label>
			                                                </td>
			                                                <td>${festModVo.year}年</td>
			                                                <td>${festMod.status=='1'?"休假":"上班"}</td>
											 				<td valign="middle">
											 					${festModVo.dayTimeS}
											 				</td>
											 				<td valign="middle">
											 					${festModVo.dayTimeE}
											 				</td>
											 				<td valign="middle">
											 					${festModVo.describe}
											 				</td>
											 				<td align="center">
											 					<c:choose>
											 						<c:when test="${festModVo.comId == 0}">
													 					--
											 						</c:when>
											 						<c:otherwise>
													 					<a href="javascript:void(0)" comId="${festModVo.comId}" dataId="${festModVo.id }" class="optUpdateBtn">修改</a> |
													 					<a href="javascript:void(0)" comId="${festModVo.comId}" dataId="${festModVo.id }" class="optDelBtn">删除</a>
											 						</c:otherwise>
											 					</c:choose>
											 				</td>
			                                            </tr>
											 		</c:forEach>
											 	</c:when>
											 	<c:otherwise>
											 		<td colspan="9" align="center" align="center"><h3>没有相关信息！</h3></td>
											 	</c:otherwise>
											 </c:choose>
                                        </tbody>
                                    </table>
                                    </form>
                                </div>
                            </div>
                        </div>             
                </div>
                <!-- /Page Body -->
            </div>
            <!-- /Page Content -->
            
        </div>
        
        
        
        <script type="text/javascript">
$(function(){
	//文本框绑定回车提交事件
	$("#userName").blur(function(){
      	$("#searchForm").submit();
    });
});
//禁用用户
function del(){
	var checkboxObj = $(":checkbox[name='ids'][enabled='0']");
	$.each(checkboxObj,function(index){
		$(this).attr("checked",false);
		$(this).parent().parent().find(".optCheckBox").css("display","none");
		$(this).parent().parent().find(".optRowNum").css("display","block");
		$("#checkAllBox").attr('checked', false);
	});
	
	layui.use('layer', function(){
		var layer = layui.layer;
		
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要禁用这些用户吗？', {
				  btn: ['确定','取消']//按钮
			  ,title:'询问框'
			  ,icon:3
			}, function(index){
				$("#delForm").attr("action","/userInfo/disableUserInfo?sid=${param.sid}")
				$("#delForm input[name='redirectPage']").val(window.location.href);
				$('#delForm').submit();
			});	
		}else{
			window.top.layer.alert('请先勾选需要禁用的用户！');
		}
		
	});
}
//启用用户
function add(){
	var checkboxObj = $(":checkbox[name='ids'][enabled='1']");
	$.each(checkboxObj,function(index){
		$(this).attr("checked",false);
		$(this).parent().parent().find(".optCheckBox").css("display","none");
		$(this).parent().parent().find(".optRowNum").css("display","block");
		$("#checkAllBox").attr('checked', false);
	})
	
	layui.use('layer', function(){
		var layer = layui.layer;
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要启用这些用户吗？', {
				  btn: ['确定','取消']//按钮
			  ,title:'询问框'
			  ,icon:3
			}, function(index){
				$("#delForm").attr("action","/userInfo/enableUserInfo?sid=${param.sid}")
				$("#delForm input[name='redirectPage']").val(window.location.href);
				$('#delForm').submit();
			});	
		}else{
			window.top.layer.alert('请先勾选需要启用的用户！');
		}
	});
}
//授权
function grant(){
	var checkboxObj = $(":checkbox[name='ids'][adminState='2']");
	$.each(checkboxObj,function(index){
		$(this).attr("checked",false);
		$(this).parent().parent().find(".optCheckBox").css("display","none");
		$(this).parent().parent().find(".optRowNum").css("display","block");
		$("#checkAllBox").attr('checked', false);
	})
	
	layui.use('layer', function(){
		var layer = layui.layer;
		
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要授管理权限给这些用户吗？', {
				  btn: ['确定','取消']//按钮
			  ,title:'询问框'
			  ,icon:3
			}, function(index){
				$("#delForm").attr("action","/userInfo/updateGrant?sid=${param.sid}")
				$("#delForm input[name='redirectPage']").val(window.location.href);
				$('#delForm').submit();
			});	
		}else{
			window.top.layer.alert('请先勾选需要授管理权限的用户！');
		}
	});
}
//回收权限
function revoke(){
	
	var checkboxObj = $(":checkbox[name='ids'][adminState='0']");
	$.each(checkboxObj,function(index){
		$(this).attr("checked",false);
		$(this).parent().parent().find(".optCheckBox").css("display","none");
		$(this).parent().parent().find(".optRowNum").css("display","block");
		$("#checkAllBox").attr('checked', false);
	})
	layui.use('layer', function(){
		var layer = layui.layer;
		
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要回收这些用户的权限吗？', {
				  btn: ['确定','取消']//按钮
			  ,title:'询问框'
			  ,icon:3
			}, function(){
				$("#delForm").attr("action","/userInfo/updateRevoke?sid=${param.sid}")
				$("#delForm input[name='redirectPage']").val(window.location.href);
				$('#delForm').submit();
			});	
		}else{
			window.top.layer.alert('请先勾选需要回收管理权限的用户！');
		}
	});
}

function updateEnabled(ts,id,enabled,isadmin){
	if(isadmin>0){
		return;
	}
	var onclick = $(ts).attr("onclick");
	$.ajax({
		  type : "post",
		  url : "/userInfo/updateEnabled?sid=${param.sid}&rnd="+Math.random(),
		  dataType:"json",
		  data:{id:id,enabled:enabled},
		  beforeSend: function(XMLHttpRequest){
			  $(ts).removeAttr("onclick");
           },
		  success : function(data){
     		  if(data.status=='y'){
         		  if(enabled==1){
	     			$(ts).attr("onclick","updateEnabled(this,"+id+",0,"+isadmin+")");
	     			$(ts).css("color","red");
	     			$(ts).html("已禁用");
         		  }else if(enabled==0){
	     			$(ts).attr("onclick","updateEnabled(this,"+id+",1,"+isadmin+")");
	     			$(ts).css("color","green");
	     			$(ts).html("已启用");
         		  }
     			 showNotification(1,data.info);
     		  }else{
     			 $(ts).attr("onclick",onclick);
     			 showNotification(2,data.info);
     		  }
		  },
		  error:  function(XMLHttpRequest, textStatus, errorThrown){
     			 $(ts).attr("onclick",onclick);
     			showNotification(2,"系统错误,请联系管理人员！")
	      }
	});
	
}

//人员信息查看
function view(id){
	var url='/userInfo/viewUserInfo?id='+id+'&sid=${param.sid}';
	window.top.layer.open({
		  type: 2,
		  title: false,
		  closeBtn: 0,
		  shade: 0.5,
		  shift:0,
		  scrollbar:false,
		  fix: true, //固定
		  maxmin: false,
		  move: false,
		  area: ['640px', '500px'],
		  content: [url,'no'], //iframe的url
		  btn:['关闭']
		});
	
}
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
</script>
</body>
</html>
