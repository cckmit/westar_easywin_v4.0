<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	
	var sid = "${param.sid}";
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	
	//人员范围的显示隐藏
	function showMans(){
		
		if($("#pubState").val() == 0){
			$("#mans").show();
		}else{
			$("#mans").hide();
		}
		
	}
	
	$(function() {
		if(!'${olm.listContactWay[0]}'){
			addContactWay();
		}
		if(!'${olm.listAddress[0]}'){
			addAddress();
		}
		
		showMans();
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			},
			datatype:{
				
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		
		//联系人姓名更新
		$("#linkManName").change(function(){
			if($("#linkManName").val()){
				$.post("/outLinkMan/ajaxUpdateOlm?sid=${param.sid}",{Action:"post",id:'${olm.id}',linkManName:$("#linkManName").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		
		//性别更新
		$("#gender").change(function(){
			if($("#gender").val()){
				$.post("/outLinkMan/ajaxUpdateOlm?sid=${param.sid}",{Action:"post",id:'${olm.id}',gender:$("#gender").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		
		//职务更新
		$("#post").change(function(){
			if($("#post").val()){
				$.post("/outLinkMan/ajaxUpdateOlm?sid=${param.sid}",{Action:"post",id:'${olm.id}',post:$("#post").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		//手机更新
		$("#movePhone").change(function(){
			if($("#movePhone").val()){
				$.post("/outLinkMan/ajaxUpdateOlm?sid=${param.sid}",{Action:"post",id:'${olm.id}',movePhone:$("#movePhone").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		//座机更新
		$("#linePhone").change(function(){
			if($("#linePhone").val()){
				$.post("/outLinkMan/ajaxUpdateOlm?sid=${param.sid}",{Action:"post",id:'${olm.id}',linePhone:$("#linePhone").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		//爱好更新
		$("#hobby").change(function(){
			if($("#hobby").val()){
				$.post("/outLinkMan/ajaxUpdateOlm?sid=${param.sid}",{Action:"post",id:'${olm.id}',hobby:$("#hobby").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		//备注更新
		$("#remarks").change(function(){
			if($("#remarks").val()){
				$.post("/outLinkMan/ajaxUpdateOlm?sid=${param.sid}",{Action:"post",id:'${olm.id}',remarks:$("#remarks").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		//阅读权限更新
		$("#pubState").change(function(){
			if($("#pubState").val()){
				$.post("/outLinkMan/ajaxUpdateOlm?sid=${param.sid}",{Action:"post",id:'${olm.id}',pubState:$("#pubState").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		
		
		
	});
	//提交表单
	function formSub(){
		$(".subform").submit();
	}
	
	var contactWayRowNum = 1;
	//添加联系方式
	function addContactWay() {
		//获取联系方式下拉值
		var str = '';
		listDataDic("contactWay",function(data){
			if(data && data[0]){
				for (var i = 0; i < data.length; i++) {
					if(data[i].parentId > 0){
						str +='<option value="'+data[i].code+'">'+data[i].zvalue+'</option>';
					}
				}
			}
		});
		setTimeout(function() {
			var html = '<li class="ticket-item no-shadow ps-listline contactWay">'+
			'										    <div class="pull-left gray ps-left-text">'+
			'										    	&nbsp;联系方式'+
			'										    </div>'+
			'											<div class="ticket-user pull-left ps-right-box">'+
			'											  	<div class="pull-left">'+
			'											  		<select class="populate" id="listContactWay_'+contactWayRowNum+'_contactWayCode" name="listContactWay['+contactWayRowNum+'].contactWayCode"  style="width: 200px;float: left" onchange="saveContactWay('+contactWayRowNum+',this)">'+
			str+
			'											  		</select>'+
			'												</div>'+
			'											</div>'+
			'											<div class="pull-left" style="margin-left: 20px;">'+
			'												<input id="listContactWay_'+contactWayRowNum+'_contactWay"  name="listContactWay['+contactWayRowNum+'].contactWay" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left" onchange="saveContactWay('+contactWayRowNum+',this)">'+
			'											</div>'+
			'											<div class="pull-left" style="margin-left: 20px;">'+
			'												<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delLi(this)" title="删除联系方式"></a>'+
			'											</div>'+
			'											<div class="pull-left" style="margin-left: 20px;">'+
			'			                                   <a class="ps-point btn-a-full" title="添加联系方式" onclick="addContactWay()">'+
			'			                                    	<i class="fa green fa-plus"></i>'+
			'			                                   </a>'+
			'											</div>'+
			'			                                	'+
			'                                         </li>';
		$("#contactWay_-1").before(html);
		
		contactWayRowNum = contactWayRowNum+1;
		
		}, 200);
		
	}
	
	var addressRowNum = 1;
	//添加联系地址
	function addAddress() {
			var html = '<li class="ticket-item no-shadow ps-listline address" >'+
			'										    <div class="pull-left gray ps-left-text">'+
			'										    	&nbsp;联系地址'+
			'										    </div>'+
			'											<div class="ticket-user pull-left ps-right-box">'+
			'											  	<div class="pull-left">'+
			'											  		<select class="populate" id="listAddress_'+addressRowNum+'_addressCode" name="listAddress['+addressRowNum+'].addressCode"  style="width: 200px;float: left" onchange="saveAddress('+addressRowNum+',this)">'+
			'						 								<option value="0">办公地址</option>'+
			'						 								<option value="1">家庭地址</option>'+
			'													</select>'+
			'												</div>'+
			'											</div>'+
			'											<div class="pull-left" style="margin-left: 20px;">'+
			'												<input id="listAddress_'+addressRowNum+'_address"  name="listAddress['+addressRowNum+'].address" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left" onchange="saveAddress('+addressRowNum+',this)">'+
			'											</div>'+
			'											<div class="pull-left" style="margin-left: 20px;">'+
			'												<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delLi(this)" title="删除联系地址"></a>'+
			'											</div>'+
			'											<div class="pull-left" style="margin-left: 20px;" >'+
			'			                                   <a class="ps-point btn-a-full"  title="添加联系地址" onclick="addAddress()">'+
			'			                                    	<i class="fa green fa-plus"></i>'+
			'			                                   </a>'+
			'											</div>'+
			'                                         </li>';
		$("#address_-1").before(html);
		
		addressRowNum = addressRowNum+1;
		
		
	}
	
	
	//删除选中的li
	function delLi(clickLi){
		var li = $(clickLi).parent().parent();
		if((li.attr("class").indexOf("contactWay") != -1 && $(".contactWay").length == 1) || (li.attr("class").indexOf("address") != -1 && $(".address").length == 1)){
			showNotification(2,"最后一行不能移除");
		}else{
			li.remove();
		}
	}
	
	//分享人员更新
	function userMoreCallBack(){
		var userIds =new Array();
		$("#listRangeUser_userId option").each(function() { 
			userIds.push($(this).val()); 
	    });
		if(!strIsNull(userIds.toString())){
			$.post("/outLinkMan/ajaxUpdateOlmRange?sid=${param.sid}",{Action:"post",outLinkManId:'${olm.id}',userIds:userIds.toString()},     
				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
	}
	
	//删除分享人
	function removeClickUser(id,selectedUserId) {
		if(selectedUserId == '${userInfo.id}'){
			showNotification(2,"不能移除自己");
		}else{
			var selectobj = document.getElementById(id);
			for ( var i = 0; i < selectobj.options.length; i++) {
				if (selectobj.options[i].value==selectedUserId) {
					selectobj.options[i] = null;
					break;
				}
			}
			$("#user_img_"+id+"_"+selectedUserId).remove();
			$.post("/outLinkMan/delOlmRange?sid=${param.sid}",{Action:"post",outLinkManId:'${olm.id}',userId:selectedUserId},     
					function (msgObjs){
					showNotification(1,msgObjs);
			});
			selected(id);
		}
		
	}
	
	//更新联系地址
	function updateAddress(id) {
		if($("#addressCode_" + id).val() && $("#address_" + id).val()){
			$.post("/outLinkMan/updateAddress?sid=${param.sid}",{Action:"post",id:id,address:$("#address_" + id).val(),addressCode:$("#addressCode_" + id).val()},     
 				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
	}
	
	//更新联系方式
	function updateContactWay(id) {
		if($("#contactWayCode_" + id).val() && $("#contactWay_" + id).val()){
			$.post("/outLinkMan/updateContactWay?sid=${param.sid}",{Action:"post",id:id,contactWay:$("#contactWay_" + id).val(),contactWayCode:$("#contactWayCode_" + id).val()},     
 				function (msgObjs){
				showNotification(1,msgObjs);
			});
		}
	}
	
	//添加联系方式保存
	function saveContactWay(row,cli) {
		if($("#listContactWay_" + row+"_contactWay").val() && $("#listContactWay_" + row+"_contactWayCode").val()){
			$.post("/outLinkMan/addContactWay?sid=${param.sid}",{Action:"post",outLinkManId:'${olm.id}',contactWay:$("#listContactWay_" + row+"_contactWay").val() ,contactWayCode:$("#listContactWay_" + row+"_contactWayCode").val() },     
				function (map){
					var data = JSON.parse(map);
					if(data.status == "t"){
						showNotification(1,data.msg);
						var li = $(cli).parent().parent();
						//获取联系方式下拉值
						var str = '';
						listDataDic("contactWay",function(datas){
							if(datas && datas[0]){
								for (var i = 0; i < datas.length; i++) {
									if(datas[i].parentId > 0){
										if(data.olmContactWay.contactWay == datas[i].code){
											str +='<option value="'+datas[i].code+'" selected>'+datas[i].zvalue+'</option>';
										}else{
											str +='<option value="'+datas[i].code+'">'+datas[i].zvalue+'</option>';
										}
										
									}
								}
							}
						});
						setTimeout(function() {
							var html = '<li class="ticket-item no-shadow ps-listline contactWay">'+
							'						    <div class="pull-left gray ps-left-text">'+
							'						    	&nbsp;联系方式'+
							'						    </div>'+
							'							<div class="ticket-user pull-left ps-right-box">'+
							'							  	<div class="pull-left">'+
							'											  		<select class="populate" id="contactWayCode_'+data.olmContactWay.id+'"  style="width: 200px;float: left" onchange="updateContactWay('+data.olmContactWay.id+')">'+
							str+
							'											  		</select>'+
							'								</div>'+
							'							</div>'+
							'							<div class="pull-left" style="margin-left: 20px;">'+
							'								<input id="contactWay_'+data.olmContactWay.id+'"  name="contactWay" class="colorpicker-default form-control" type="text" value="'+data.olmContactWay.contactWay+'" style="width: 200px;float: left" onchange="updateContactWay('+data.olmContactWay.id+')">'+
							'							</div>'+
							'							<div class="pull-left" style="margin-left: 20px;">'+
							'								<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delContactWay('+data.olmContactWay.id+',this)" title="删除"></a>'+
							'							</div>'+
							'							<div class="pull-left" style="margin-left: 20px;">'+
							'                               <a class="ps-point btn-a-full" title="添加" onclick="addContactWay()">'+
							'                                	<i class="fa green fa-plus"></i>'+
							'                               </a>'+
							'							</div>'+
							'                  		</li> ';
							
							$(li).before(html);
							$(li).remove();
						}, 200);
						
					}else{
						showNotification(2,data.msg);
				}
			});
		}
	}
	
	//添加联系地址保存
	function saveAddress(row,cli) {
		if($("#listAddress_" + row+"_address").val() && $("#listAddress_" + row+"_addressCode").val()){
			$.post("/outLinkMan/addAddress?sid=${param.sid}",{Action:"post",outLinkManId:'${olm.id}',address:$("#listAddress_" + row+"_address").val() ,addressCode:$("#listAddress_" + row+"_addressCode").val() },     
 				function (map){
					var data = JSON.parse(map); 
					if(data.status == "t"){
						showNotification(1,data.msg);
						var li = $(cli).parent().parent();
						var html = '<li class="ticket-item no-shadow ps-listline address">'+
							'						    <div class="pull-left gray ps-left-text">'+
							'						    	&nbsp;联系地址'+
							'						    </div>'+
							'							<div class="ticket-user pull-left ps-right-box">'+
							'							  	<div class="pull-left">'+
							'							  		<select class="populate" style="width: 200px;float: left" id="addressCode_'+data.olmAddress.id+'" onchange="updateAddress('+data.olmAddress.id+')">'+
							'										<option value="0" '+(data.olmAddress.addressCode==0?'selected':'')+'>办公地址</option>'+
							'										<option value="1" '+(data.olmAddress.addressCode==1?'selected':'')+'>家庭地址</option>'+
							'									</select>'+
							'								</div>'+
							'							</div>'+
							'							<div class="pull-left" style="margin-left: 20px;">'+
							'								<input id="address_'+data.olmAddress.id+'"  name="address" class="colorpicker-default form-control" type="text" value="'+data.olmAddress.address+'" style="width: 200px;float: left" onchange="updateAddress('+data.olmAddress.id+')">'+
							'							</div>'+
							'							<div class="pull-left" style="margin-left: 20px;">'+
							'								<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delAddress('+data.olmAddress.id+',this)" title="删除"></a>'+
							'							</div>'+
							'							<div class="pull-left" style="margin-left: 20px;">'+
							'                               <a class="ps-point btn-a-full" title="添加" onclick="addAddress()">'+
							'                                	<i class="fa green fa-plus"></i>'+
							'                               </a>'+
							'							</div>'+
							'                  		</li> ';
							
						$(li).before(html);
						$(li).remove();
						
					}else{
						showNotification(2,data.msg);
					}
				
			});
		}
	}
	
	//删除联系方式
	function delContactWay(id,clickLi) {
		window.top.layer.confirm('确定要删除该联系方式吗？', {
			icon: 3,
			title: '提示'
		},
		function(index) {
			$.post("/outLinkMan/delContactWay?sid=${param.sid}",{Action:"post",id:id},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
				var li = $(clickLi).parent().parent();
				if(li.attr("class").indexOf("contactWay") != -1 && $(".contactWay").length == 1){
					addContactWay();
				}
				li.remove();
		});
	}
	
	//删除联系地址
	function delAddress(id,clickLi) {
		window.top.layer.confirm('确定要删除该联系地址吗？', {
			icon: 3,
			title: '提示'
		},
		function(index) {
			$.post("/outLinkMan/delAddress?sid=${param.sid}",{Action:"post",id:id},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
				var li = $(clickLi).parent().parent();
				if(li.attr("class").indexOf("address") != -1 && $(".address").length == 1){
					addAddress();
				}
				li.remove();
		});
	}
	
	
</script>
</head>
<body>
   	<div class="tickets-container bg-white">
		<ul class="tickets-list">
           <li class="ticket-item no-shadow ps-listline">
			    <div class="pull-left gray ps-left-text">
			    	&nbsp;联系人姓名
			    </div>
				<div class="ticket-user pull-left ps-right-box">
				  	<div class="pull-left">
						<input id="linkManName" " name="linkManName" value="${olm.linkManName }"
						class="colorpicker-default form-control" type="text" style="width: 200px;float: left" >
					</div>
				</div>
           </li>
           <li class="ticket-item no-shadow ps-listline">
			    <div class="pull-left gray ps-left-text">
			    	&nbsp;性别
			    </div>
				<div class="ticket-user pull-left ps-right-box">
				  	<div class="pull-left">
						<tags:dataDic type="gender" id="gender" name="gender" value="${olm.gender }" please="t" style="width:200px;"></tags:dataDic>
					</div>
				</div>
				<div class="pull-left gray ps-left-text" style="margin-left: 20px;">
			    	&nbsp;职务
			    </div>
				<div class="ticket-user pull-left ps-right-box">
				  	<div class="pull-left">
						<input id="post"  name="post" value="${olm.post }"	class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
					</div>
				</div>
             </li>
             <li class="ticket-item no-shadow ps-listline">
			    <div class="pull-left gray ps-left-text">
			    	&nbsp;移动电话
			    </div>
				<div class="ticket-user pull-left ps-right-box">
				  	<div class="pull-left">
						<input id="movePhone"  name="movePhone" value="${olm.movePhone }"	class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
					</div>
				</div>
				<div class="pull-left gray ps-left-text" style="margin-left: 20px;">
			    	&nbsp;座机
			    </div>
				<div class="ticket-user pull-left ps-right-box">
				  	<div class="pull-left">
						<input id="linePhone"  name="linePhone" value="${olm.linePhone }"	class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
					</div>
				</div>
              </li>
               <c:if test="${not empty olm.listContactWay }">
               		<c:forEach items="${olm.listContactWay}" var="cw" varStatus="vs">
                		<li class="ticket-item no-shadow ps-listline contactWay">
						    <div class="pull-left gray ps-left-text">
						    	&nbsp;联系方式
						    </div>
							<div class="ticket-user pull-left ps-right-box">
							  	<div class="pull-left">
							  		<tags:dataDic type="contactWay" id="contactWayCode_${cw.id}" name="contactWayCode_${cw.id}" value="${cw.contactWayCode}" style="width:200px;" onchange="updateContactWay(${cw.id})"></tags:dataDic>
								</div>
							</div>
							<div class="pull-left" style="margin-left: 20px;">
								<input id="contactWay_${cw.id}"  name="contactWay" class="colorpicker-default form-control" type="text" value="${cw.contactWay}" style="width: 200px;float: left" onchange="updateContactWay(${cw.id})">
							</div>
							<div class="pull-left" style="margin-left: 20px;">
								<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delContactWay(${cw.id},this)" title="删除"></a>
							</div>
							<div class="pull-left" style="margin-left: 20px;">
                               <a class="ps-point btn-a-full" title="添加" onclick="addContactWay()">
                                	<i class="fa green fa-plus"></i>
                               </a>
							</div>
                  		</li> 
                     </c:forEach>
                 </c:if>
                 <li id="contactWay_-1"></li>
                  <c:if test="${not empty olm.listAddress }">
               		<c:forEach items="${olm.listAddress}" var="cw" varStatus="vs">
                		<li class="ticket-item no-shadow ps-listline address">
						    <div class="pull-left gray ps-left-text">
						    	&nbsp;联系地址
						    </div>
							<div class="ticket-user pull-left ps-right-box">
							  	<div class="pull-left">
							  		<select class="populate" style="width: 200px;float: left" id="addressCode_${cw.id}" onchange="updateAddress(${cw.id})">
										<option value="0" ${(cw.addressCode== 0)? "selected='selected'":''}>办公地址</option>
										<option value="1" ${(cw.addressCode== 1)? "selected='selected'":''}>家庭地址</option>
									</select>
								</div>
							</div>
							<div class="pull-left" style="margin-left: 20px;">
								<input id="address_${cw.id}"  name="address" class="colorpicker-default form-control" type="text" value="${cw.address}" style="width: 200px;float: left" onchange="updateAddress(${cw.id})">
							</div>
							<div class="pull-left" style="margin-left: 20px;">
								<a href="javascript:void(0)" style="color: red;" class="fa fa-times-circle-o" onclick="delAddress(${cw.id},this)" title="删除"></a>
							</div>
							<div class="pull-left" style="margin-left: 20px;">
                               <a class="ps-point btn-a-full" title="添加" onclick="addAddress()">
                                	<i class="fa green fa-plus"></i>
                               </a>
							</div>
                  		</li> 
                     </c:forEach>
                 </c:if>
         <li id="address_-1"></li>
         <li class="ticket-item no-shadow ps-listline" style="clear:both;height: 100px">
		     <div class="pull-left gray ps-left-text" style="height: 100px;">
                      &nbsp;爱好
              </div>
              <div class="ticket-user pull-left ps-right-box" style="height: auto;">
                  <textarea  class="colorpicker-default form-control margin-top-0 " id="hobby" name="hobby" rows="" cols="" style="width:510px;height: 80px;" maxlength="200">${olm.hobby }</textarea>
              </div>
         </li>
         <li class="ticket-item no-shadow ps-listline" style="clear:both;height: 100px">
		     <div class="pull-left gray ps-left-text" style="height: 100px;">
                     &nbsp;备注
             </div>
             <div class="ticket-user pull-left ps-right-box" style="height: auto;">
                 <textarea class="colorpicker-default form-control margin-top-0 " id="remarks" name="remarks" rows="" cols="" style="width:510px;height: 80px;" maxlength="200">${olm.remarks }</textarea>
             </div>
          </li>
          <li class="ticket-item no-shadow ps-listline">
		    <div class="pull-left gray ps-left-text">
		    	&nbsp;阅读权限
		    </div>
			<div class="ticket-user pull-left ps-right-box">
			  	<div class="pull-left">
			  		<select class="populate" id="pubState" name="pubState" onchange="showMans()" style="width: 200px;float: left">
							<option value="0" ${(olm.pubState== 0)? "selected='selected'":''}>私有</option>
							<option value="1" ${(olm.pubState== 1)? "selected='selected'":''}>公开</option>
					</select>
				</div>
			</div>
          </li>
          <li class="ticket-item no-shadow autoHeight no-padding" style="display: block;" id="mans">
		    <div class="pull-left gray ps-left-text padding-top-10">
		    	&nbsp;分享范围
		    </div>
			<div class="ticket-user pull-left ps-right-box" style="height: auto;">
				<div class="pull-left gray ps-left-text padding-top-10">
					<div style="float: left; width: 250px;display:none;">
						<select list="listRangeUser" listkey="userId" listvalue="userName" id="listRangeUser_userId" name="listRangeUser.userId" ondblclick="removeClick(this.id)" multiple="multiple"
							moreselect="true" style="width: 100%; height: 100px;">
							<c:forEach items="${olm.listRangeUser}" var="scopeUser" varStatus="status">
								<option selected="selected" value="${scopeUser.userId }">${scopeUser.userName }</option>
							</c:forEach>
						</select>
					</div>
					<div id="carPersonDiv" class="pull-left" style="max-width:460px">
						<c:forEach items="${olm.listRangeUser}" var="scopeUser" varStatus="status">
							<div class="online-list margin-left-5 margin-bottom-5" style="float:left;cursor:pointer;" id="user_img_listRangeUser_userId_${scopeUser.userId }"
								ondblclick="removeClickUser('listRangeUser_userId','${scopeUser.userId }')" title="双击移除">
								<img src="/downLoad/userImg/${scopeUser.comId }/${scopeUser.userId }.jpg" class="user-avatar">
								<span class="user-name">${scopeUser.userName }</span>
							</div>
						</c:forEach>
					</div>
					<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5" title="人员多选"
						onclick="userMore('listRangeUser_userId','','${param.sid}','yes','carPersonDiv');" style="float: left;"><i class="fa fa-plus"></i>选择</a>

				</div>
			</div>
			<div class="ps-clear"></div>                   
         </li>
                                 
      </ul>
    </div>
</div>
					
</body>
</html>
