//是否设置显示下周计划
function weekPlane(sid,id,hasPlan,ts){
    $("#weekModForm").ajaxSubmit({
        type:"post",
        url:"/weekReport/updateWeekPlane?sid="+sid+"&t="+Math.random(),
        data:{"id":id,"hasPlan":hasPlan},
        dataType: "json", 
        success:function(data){
            if(data.status=='y'){
                if(hasPlan==1){ //设置为不显示
                	 $(ts).attr("onclick","weekPlane('"+sid+"',"+id+",'0',this)")
                     $(ts).html("<span style='color: green'> 是</span>");
                	 showNotification(1, "操作成功！");
                }else{ //设置为显示
                	 $(ts).attr("onclick","weekPlane('"+sid+"',"+id+",'1',this)")
                    $(ts).html("<span style='color: red'>否</span>");
                	 showNotification(1, "操作成功！");
                }
            }else if(data.status=='f'){
            	window.self.location="/weekReport/listPagedWeekRep?pager.pageSize=10&sid="+sid;
            }
        },
        error:function(XmlHttpRequest,textStatus,errorThrown){
        	showNotification(2,"系统错误，请联系管理人员！")
        }
    });
}
//设置隐藏顺序
function setStyle(){
	$('.teamLevDetail').mouseover(function(){
		var delLine = $(this).find(".teamLevContent").css("text-decoration");
		var input = $(this).find(".teamLevContent").find("input").val();
		if(delLine.indexOf('none')>=0 && undefined==input){//处于显示，且不为修改
			$(this).find(".teamLevUpdate").show();
		}else if(null!=input){//处于修改状态
		}else{//处于显示
			$(this).find(".teamLevShow").show();
		}
	});
	$('.teamLevDetail').mouseout(function(){
		$(this).find(".teamLevUpdate").hide();
		$(this).find(".teamLevShow").hide();
	});
	$('.groupLevDetail').mouseover(function(){
		var delLine = $(this).find(".groupLevContent").css("text-decoration");
		var input = $(this).find(".groupLevContent").find("input").val();
		if(delLine.indexOf('none')>=0 && undefined==input){//处于显示，且不为修改
			$(this).find(".groupLevUpdate").show();
		}else if(null!=input){//处于修改状态
		}else{//处于显示
			$(this).find(".groupLevShow").show();
		}
	});
	$('.groupLevDetail').mouseout(function(){
		$(this).find(".groupLevUpdate").hide();
		$(this).find(".groupLevShow").hide();
	});
	$('.memberLevDetail').mouseover(function(){
		var delLine = $(this).find(".memberLevContent").css("text-decoration");
		var input = $(this).find(".memberLevContent").find("input").val();
		if(delLine.indexOf('none')>=0 && undefined==input){//处于显示，且不为修改
			$(this).find(".memberLevUpdate").show();
		}else if(null!=input){//处于修改状态
		}else{//处于显示
			$(this).find(".memberLevShow").show();
		}
	});
	$('.memberLevDetail').mouseout(function(){
		$(this).find(".memberLevUpdate").hide();
		$(this).find(".memberLevShow").hide();
	});
}

//团队级模板设置删除线
function showDelTeamLine(id,hideState){
	//隐藏
	if(1==hideState){
		//内容样式为删除
		$("#team"+id).find(".teamLevContent").css("text-decoration","line-through");
		$("#team"+id).find(".teamLevContent").css("color","gray");
		$("#team"+id).find(".teamLevUpdate").hide();
		$("#team"+id).find(".teamLevShow").show();
		showNotification(1, "操作成功！");
	}else if(0==hideState){//显示
		//内容样式为正常
		$("#team"+id).find(".teamLevContent").css("text-decoration","none");
		$("#team"+id).find(".teamLevContent").css("color","#000000");
		$("#team"+id).find(".teamLevUpdate").show();
		$("#team"+id).find(".teamLevShow").hide();
		showNotification(1, "操作成功！");
	}
}

//部门级模板设置删除线
function showDelGroupLine(id,hideState){
	//隐藏
	if(1==hideState){
		//内容样式为删除
		$("#team"+id).find(".groupLevContent").css("text-decoration","line-through");
		$("#team"+id).find(".groupLevContent").css("color","gray");
		$("#team"+id).find(".groupLevUpdate").hide();
		$("#team"+id).find(".groupLevShow").show();
		showNotification(1, "操作成功！");
	}else if(0==hideState){//显示
		//内容样式为正常
		$("#team"+id).find(".groupLevContent").css("text-decoration","none");
		$("#team"+id).find(".groupLevContent").css("color","#000000");
		$("#team"+id).find(".groupLevUpdate").show();
		$("#team"+id).find(".groupLevShow").hide();
		showNotification(1, "操作成功！");
	}
}
//成员级模板设置删除线
function showDelMemberLine(id,hideState){
	//隐藏
	if(1==hideState){
		//内容样式为删除
		$("#team"+id).find(".memberLevContent").css("text-decoration","line-through");
		$("#team"+id).find(".memberLevContent").css("color","gray");
		$("#team"+id).find(".memberLevUpdate").hide();
		$("#team"+id).find(".memberLevShow").show();
		showNotification(1, "操作成功！");
	}else if(0==hideState){//显示
		//内容样式为正常
		$("#team"+id).find(".memberLevContent").css("text-decoration","none");
		$("#team"+id).find(".memberLevContent").css("color","#000000");
		$("#team"+id).find(".memberLevUpdate").show();
		$("#team"+id).find(".memberLevShow").hide();
		showNotification(1, "操作成功！");
	}
}
//编辑团队问题内容
function editTeamContent(id,modId){
	var teamContent = $("#team"+id).find(".teamLevSave").find("input").val();
	var html = '<input type="text" class="form-control small" value="'+teamContent+'"/>'
	$("#team"+id).find(".teamLevContent").html(html);
	$("#team"+id).find(".teamLevSave").show();
	$("#team"+id).find(".teamLevUpdate").hide();
	
}
//取消修改团队模板条目
function cancleEditTeam(id){
	var teamContent = $("#team"+id).find(".teamLevSave").find("input").val();
	var html = '<span>'+teamContent+'</span>';
	$("#team"+id).find(".teamLevContent").html(html);
	$("#team"+id).find(".teamLevSave").hide();
	$("#team"+id).find(".teamLevUpdate").show();
	
}

//取消团队模板添加条目
function cancleAddTeam(){
	$('.addTeamLev').hide();$('.addTeamLevContent').show();
	$(".addTeamLev").find(".teamContent").find("input").attr("value","")
}
//取消部门模板添加条目
function  cancleAddGroup(){
	$('.addGroupLev').hide();$('.addGroupLevContent').show();
	$(".addGroupLev").find(".groupContent").find("input").attr("value","")
	$(".addGroupLev").find(".groupContent").find(".selectDep").find("select").html('');
	$("#depMorelistDeps_depId").html('');
}
//取消成员级别的条目
function cancleAddMember(){
	$('.addMemberLev').hide();$('.addMemberLevContent').show();
	$(".addMemberLev").find(".memberContent").find("input").attr("value","")
	$(".addMemberLev").find("#userMoreImglistMembers_memberId").html('');
	$(".addMemberLev").find("#listMembers_memberId").html('');
}

//保存团队模板条目修改
function updateTeam(sid,id,modId,ts){
	var onclick = $(ts).attr("onclick");
	
     var teamContent = $("#team"+id).find(".teamLevContent").find("input").val();
     var preContent = $("#team"+id).find(".teamLevSave").find("input").val();
     if(strIsNull(teamContent)){
    	 $("#team"+id).find(".teamLevContent").find("input").focus();
    	 return;
     }else  if(teamContent==preContent){//内容相同不提交
    	 cancleEditTeam(id);
         return;
     }else{
		 $("#weekModForm").ajaxSubmit({
		        type:"post",
		        url:"/weekReport/updateWeekRepModContent?sid="+sid+"&t="+Math.random(),
		        data:{"id":id,"modContent":teamContent,"modId":modId},
		        dataType: "json", 
		        beforeSubmit:function(a,o,f){
		        	//防止重复提交
		        	$(ts).removeAttr("onclick");
		        },
		        success:function(data){
		            if(data.status=='y'){
		            	//重新赋临时值
		            	$("#team"+id).find(".teamLevSave").find(".tempTeamCont").attr("value",teamContent);
		            	//展现的代码
		            	var html = '<span>'+teamContent+'</span>';
		            	$("#team"+id).find(".teamLevContent").html(html);
		            	$("#team"+id).find(".teamLevSave").hide();
		            	$("#team"+id).find(".teamLevUpdate").show();
		            }else if(data.status=='f'){
		            	window.self.location="/weekReport/listPagedWeekRep?pager.pageSize=10&sid="+sid;
		            }
		            $(ts).attr("onclick",onclick);
		            showNotification(1, "修改成功！");
		            
		        },
		        error:function(XmlHttpRequest,textStatus,errorThrown){
		        	$(ts).attr("onclick",onclick);
		        	showNotification(2,"系统错误，请联系管理人员！")
		        }
		    });
     }
}
//添加团队模板条目
function addTeam(sid,ts,modId){
	var onclick = $(ts).attr("onclick");
	
	var teamContent = $(".addTeamLev").find(".teamContent").find("input").val();
	if(strIsNull(teamContent)){//为空
		$(".addTeamLev").find(".teamContent").find("input").focus();
		return;
	}else{
		$("#weekModForm").ajaxSubmit({
	        type:"post",
	        url:"/weekReport/addWeekModContent?sid="+sid+"&t="+Math.random(),
	        data:{"modContent":teamContent,"modId":modId,"repLev":1,"hideState":0},
	        dataType: "json", 
	        beforeSubmit:function(a,o,f){
	        	//防止重复提交
	        	$(ts).removeAttr("onclick");
	        },
	        success:function(data){
	        	$("#noTeamLev").remove();
	            if(data.status=='y'){
					//公用部分的条目
		            var html = '\n <div id="team'+data.id+'" class="teamLevDetail modContent"> ';
					html += '\n <div  class="teamLevContent" style="text-decoration:none;color:#000000">';
					html += '\n 	<span>'+teamContent+'</span>';
					html += '\n </div>';
					//编辑 
					html += '\n 	<div class="teamLevUpdate">';
					html += '\n 		<div>';
					html += '\n 			<a href="javascript:void(0)" onclick="editTeamContent('+data.id+','+modId+')" style="color:#1E88C7;padding-right: 10px">编辑</a>';
					html += '\n 			<a href="javascript:void(0)" onclick="hideContent(\''+sid+'\','+data.id+',1,this,'+modId+',1)" style="color:#1E88C7;padding-right: 10px">隐藏</a>';
					html += '\n 			<a href="javascript:void(0)" class="setRequire" isRequire="1" contentId="'+data.id+'" modId="'+modId+'" style="color:#1E88C7;padding-right: 10px">必填</a>';
					html += '\n 			<a href="javascript:void(0)" onclick="delContent(\''+sid+'\','+data.id+',this,'+modId+',1)" style="color:#1E88C7;padding-right: 2px">删除</a>';
					html += '\n 		</div>';
					html += '\n 	</div>';
					//显示
					html += '\n 	<div class="teamLevShow">';
					html += '\n 		<div>';
					html += '\n 			已隐藏,<a href="javascript:void(0)" onclick="hideContent(\''+sid+'\','+data.id+',0,this,'+modId+',1)" style="color:#1E88C7;padding-right: 10px"> 显示</a>';
					html += '\n 			<a href="javascript:void(0)" class="setRequire" isRequire="1" contentId="'+data.id+'" modId="'+modId+'" style="color:#1E88C7;padding-right: 10px">必填</a>';
					html += '\n 			<a href="javascript:void(0)" onclick="delContent(\''+sid+'\','+data.id+',this,'+modId+',1)" style="color:#1E88C7;padding-right: 2px">删除</a>';
					html += '\n 		</div>';
					html += '\n 	</div>';
					//修改
					html += '\n 	<div class="teamLevSave">';
					//存放模板临时条目 
					html += '\n 		<input type="hidden" class="tempTeamCont" name="tempTeamCont" value="'+teamContent+'"/>';
					html += '\n 		<div>';
					html += '\n 			<a href="javascript:void(0)" onclick="updateTeam(\''+sid+'\','+data.id+','+modId+',this)" style="color:#1E88C7;padding-right: 10px;">保存</a>';
					html += '\n 			<a href="javascript:void(0)" onclick="cancleEditTeam('+data.id+')" style="color:#1E88C7;padding-right: 10px;">取消</a>';
					html += '\n 			<a href="javascript:void(0)" class="setRequire" isRequire="1" contentId="'+data.id+'" modId="'+modId+'" style="color:#1E88C7;padding-right: 10px">必填</a>';
					html += '\n 			<a href="javascript:void(0)" onclick="delContent(\''+sid+'\','+data.id+',this,'+modId+',1)" style="color:#1E88C7;padding-right: 2px">删除</a>';
					html += '\n 		</div>';
					html += '\n 	</div>';
					html += '\n </div>';
					$(".teamLev").append(html);
					setStyle();
					
	            }else if(data.status=='f'){
	            	window.self.location="/weekReport/listPagedWeekRep?pager.pageSize=10&sid="+sid;
	            }
	            $(ts).attr("onclick",onclick);
	            $('.addTeamLev').show();$('.addTeamLevContent').hide();
	        	$(".addTeamLev").find(".teamContent").find("input").attr("value","");
	        	showNotification(1, "添加成功！");
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	           $(ts).attr("onclick",onclick);
	           $('.addTeamLev').show();$('.addTeamLevContent').hide();
	           $(".addTeamLev").find(".teamContent").find("input").attr("value","")
	           showNotification(2,"系统错误，请联系管理人员！")
	        }
	    });
	}
}
//设置是否必填
function setRequire(isRequire,sid,id,hideState,ts,modId){
	var hideClick = $(ts).attr("onclick");
	$(ts).removeAttr("onclick");
	 $("#weekModForm").ajaxSubmit({
	        type:"post",
	        url:"/weekReport/updateRequireContent?sid="+sid+"&t="+Math.random(),
	        data:{"id":id,"isRequire":isRequire,"modId":modId},
	        dataType: "json", 
	        success:function(data){
	            if(data.status=='y'){
	            	isRequire == 1?$(this).parents(".modContent").find("font").remove():$(this).parents(".modContent").prepend('<font style="color: red;float: left;line-height: 40px">*</font>')
	            }else if(data.status=='f'){
	            	window.self.location="/weekReport/listPagedWeekRep?pager.pageSize=10&sid="+sid;
	            }
            	$(ts).attr("onclick",hideClick);
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	            	$(ts).attr("onclick",hideClick);
	            	showNotification(2,"系统错误，请联系管理人员！")
	        }
	    });
}

//问题的显示与隐藏
function hideContent(sid,id,hideState,ts,modId,repLev){
	var hideClick = $(ts).attr("onclick");
	$(ts).removeAttr("onclick");
	 $("#weekModForm").ajaxSubmit({
	        type:"post",
	        url:"/weekReport/hideContent?sid="+sid+"&t="+Math.random(),
	        data:{"id":id,"hideState":hideState,"modId":modId},
	        dataType: "json", 
	        success:function(data){
	            if(data.status=='y'){
		            if(1==repLev){//团队级模板设置删除线
		            	showDelTeamLine(id,hideState)
		            }else if(2==repLev){//部门级模板设置删除线
		            	showDelGroupLine(id,hideState)
		            }else if(3==repLev){//成员级模板设置删除线
		            	showDelMemberLine(id,hideState)
		            }
	            }else if(data.status=='f'){
	            	window.self.location="/weekReport/listPagedWeekRep?pager.pageSize=10&sid="+sid;
	            }
            	$(ts).attr("onclick",hideClick);
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	            	$(ts).attr("onclick",hideClick);
	            	showNotification(2,"系统错误，请联系管理人员！")
	        }
	    });
}
//添加部门模板条目
function addGroup(sid,ts,modId){
	var onclick = $(ts).attr("onclick");
	var depStr = $(".addGroupLev").find(".groupContent").find(".selectDep").find("select").val();
	var deps = new Array();
	
	var groupContent = $("#groupContent").val()
		
	if(strIsNull(groupContent)){//部门栏要求为空
		$("#groupContent").focus();
	    return;
	}
	
	//值转数组
	if(!strIsNull(depStr)){
		depStr.sort();
		for(var i=0;i<depStr.length;i++){
			deps.push(depStr[i])
		}
	}else{
		art.dialog.alert("请选择部门").time(1);
	    return;
	}
	 $("#weekModForm").ajaxSubmit({
	        type:"post",
	        url:"/weekReport/addWeekModContent?sid="+sid+"&t="+Math.random(),
	        data:{"modContent":groupContent,"modId":modId,"repLev":2,"hideState":0,"deps":deps},
	        dataType: "json", 
	        beforeSubmit:function(a,o,f){
	        	$(ts).removeAttr("onclick");
	        },
	        traditional :true,
	        success:function(data){
	        	$("#noGroupLev").remove();
	            if(data.status=='y'){
	            	var html = '\n <div id="team'+data.id+'" class="groupLevDetail modContent" > ';
					html += '\n <div class="groupLevContent" style="text-decoration:none;color:#000000">';
					
					html += '\n <div class="depLevTxt">'+groupContent;
					html += '\n </div>';
					
					html += '\n <div class="depSelect" style="clear:both;float:left;margin-top: 5px">';
					var selectOpts = $(".addGroupLev").find(".groupContent").find(".selectDep").find("select").find("option");
					for(var i=0;i<selectOpts.length;i++){
						html += '\n <span class="label label-default margin-right-5" >'+$(selectOpts[i]).html()+'</span>';
					}
					html += '\n</div>';
					
					html += '\n </div>';
					//编辑
					html += '\n <div class="groupLevUpdate">';
					html += '\n 	<div>';
					html += '\n 		<a href="javascript:void(0)" onclick="editGroupContent('+data.id+',\''+sid+'\')" style="color:#1E88C7;padding-right: 10px">编辑</a>';
					html += '\n 		<a href="javascript:void(0)" onclick="hideContent(\''+sid+'\','+data.id+',1,this,'+modId+',2)" style="color:#1E88C7;padding-right: 10px">隐藏</a>';
					html += '\n 			<a href="javascript:void(0)" class="setRequire" isRequire="1" contentId="'+data.id+'" modId="'+modId+'" style="color:#1E88C7;padding-right: 10px">必填</a>';
					html += '\n 		<a href="javascript:void(0)" onclick="delContent(\''+sid+'\','+data.id+',this,'+modId+',2)" style="color:#1E88C7;padding-right: 2px">删除</a>';
					html += '\n 	</div>';
					html += '\n </div>';
					//显示
					html += '\n <div class="groupLevShow">';
					html += '\n 	<div>';
					html += '\n 		已隐藏,<a href="javascript:void(0)" onclick="hideContent(\''+sid+'\','+data.id+',0,this,'+modId+',2)" style="color:#1E88C7;padding-right: 10px"> 显示</a>';
					html += '\n 			<a href="javascript:void(0)" class="setRequire" isRequire="1" contentId="'+data.id+'" modId="'+modId+'" style="color:#1E88C7;padding-right: 10px">必填</a>';
					html += '\n 		<a href="javascript:void(0)" onclick="delContent(\''+sid+'\','+data.id+',this,'+modId+',2)" style="color:#1E88C7;padding-right: 2px">删除</a>';
					html += '\n 	</div>';
						html += '\n </div>';
					//修改 
					html += '\n <div class="groupLevSave">';
					
					//存放模板临时条目 
					html += '\n <div class="tempdep" style="display:none">';
					html += '\n <select	 multiple="multiple" moreselect="true">';
					for(var i=0;i<selectOpts.length;i++){
						html += '\n <option value="'+$(selectOpts[i]).val()+'" selected="selected">'+$(selectOpts[i]).html()+'</option>';
					}
					html += '\n </select>';
					html += '\n </div>';
					html += '\n 	<input type="hidden" id="tempGroupCont_'+data.id+'" name="tempGroupCont" value="'+groupContent+'"/>';
					html += '\n 	<div>';
					html += '\n 		<a href="javascript:void(0)" onclick="updateGroup(\''+sid+'\','+data.id+','+modId+',this)" style="color:#1E88C7;padding-right: 10px;">保存</a>';
					html += '\n 		<a href="javascript:void(0)" onclick="cancleEditGroup('+data.id+')" style="color:#1E88C7;padding-right: 10px;">取消</a>';
					html += '\n 			<a href="javascript:void(0)" class="setRequire" isRequire="1" contentId="'+data.id+'" modId="'+modId+'" style="color:#1E88C7;padding-right: 10px">必填</a>';
					html += '\n 		<a href="javascript:void(0)" onclick="delContent(\''+sid+'\','+data.id+',this,'+modId+',2)" style="color:#1E88C7;padding-right: 2px">删除</a>';
					html += '\n 	</div>';
					html += '\n </div>';
					html += '\n </div>';
					$(".groupLev").append(html);
					setStyle();
					
	            }else if(data.status=='f'){
	            	window.self.location="/weekReport/listPagedWeekRep?pager.pageSize=10&sid="+sid;
	            }
	            $(ts).attr("onclick",onclick);
	            $('.addGroupLev').show();$('.addGroupLevContent').hide();
	        	$(".addGroupLev").find(".groupContent").find("input").attr("value","")
	        	$(".addGroupLev").find(".groupContent").find(".selectDep").find("select").html('');
	        	$("#depMorelistDeps_depId").html('');
	        	showNotification(1, "添加成功！");
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	$(ts).attr("onclick",onclick);
	        	$('.addGroupLev').show();$('.addGroupLevContent').hide();
	        	$(".addGroupLev").find(".groupContent").find("input").attr("value","")
	        	$(".addGroupLev").find(".groupContent").find(".selectDep").find("select").html('');
	        	$("#depMorelistDeps_depId").html('');
	        	showNotification(2,"系统错误，请联系管理人员！")
	        }
	    });
}
//编辑部门问题内容
function editGroupContent(id,sid){
	var modContent = $("#tempGroupCont_"+id).val();
	//选项
	var options =$("#team"+id).find(".groupLevSave").find(".tempdep").find("select").find("option");
	var html = '\n <div class="groupLevBody">';
	html +='\n 	<input id="groupContent_'+id+'" type="text" class="form-control small" name="depContent" style="width: 90%" value="'+modContent+'"/>';
	html +='\n  </div> ';
	
	html +='\n <div class="groupLevDep"> ';
	html +='\n 	<div style="float: left; width: 250px;display: none">';
	html +='\n 		<select id="listDeps_depId'+id+'"  ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">';
	for(var i=0;i<options.length;i++){
		html +='\n <option selected=\'selected\' value=\''+$(options[i]).val()+'\'>'+$(options[i]).html()+'</option>';
	}
	html +='\n 	</select>';
	html +='\n 	</div>';
	html +='\n 	<div id="depMorelistDeps_depId'+id+'" class="groupLevDepRemove">';
	
	for(var i=0;i<options.length;i++){
		html +='\n <span title="双击移除" class="label label-default margin-right-5" id="dep_span_'+$(options[i]).val()+'" ondblclick="removeClickDep(\'listDeps_depId'+id+'\','+$(options[i]).val()+')">'+$(options[i]).html()+'</span>'
	}
	html +='\n 	</div>';
	html +='\n 	<a title="部门选择" href="javascript:void(0)" class="btn btn-blue btn-xs margin-top-5" onclick="depMore(\'listDeps_depId'+id+'\',\'\',\''+sid+'\',\'yes\',\'\')"><i class="fa fa-plus"></i>选择</a>';
	html +='\n </div>';
	
	
	 
	 
	$("#team"+id).find(".groupLevContent").html(html);
	$("#team"+id).find(".groupLevSave").show();
	$("#team"+id).find(".groupLevUpdate").hide();
	
}
//取消修改团队模板条目
function cancleEditGroup(id){
	
	var modContent = $("#tempGroupCont_"+id).val();
	var html = '\n <div class="depLevTxt" >'+modContent;
	html +='\n </div>';
	html += '<div class="depSelect" style="clear:both;float:left;margin-top: 5px">';
	//选项
	var options =$("#team"+id).find(".groupLevSave").find(".tempdep").find("select").find("option");
	for(var i=0;i<options.length;i++){
		html +='\n<span class="label label-default margin-right-5" >'+$(options[i]).html()+'</span>';
	}
	html +='\n </div>';
	
	$("#team"+id).find(".groupLevContent").html(html);
	$("#team"+id).find(".groupLevSave").hide();
	$("#team"+id).find(".groupLevUpdate").show();
	
}

//保存团队模板条目修改
function updateGroup(sid,id,modId,ts){
	//防止重复提交
	
	var options = $("#team"+id).find(".groupLevContent").find(".groupLevDep").find("select").val();
	//操作后的值
	var depStr = '';
	var deps = new Array();
	if(!strIsNull(options)){
		options.sort()
		for(var i=0;i<options.length;i++){
			deps.push(options[i]);
			depStr +=options[i]+",";
		}
	}else{
		art.dialog.alert("请选择部门").time(1);
		return;
	}
	
	//操作前的值
	var preOpts = $("#team"+id).find(".groupLevSave").find(".tempdep").find("select").val();
	var preDepStr = '';
	if(!strIsNull(preOpts)){
		preOpts.sort();
		for(var i=0;i<preOpts.length;i++){
			preDepStr += preOpts[i]+",";
		}
	}
	//操作后
	var modContent = $("#groupContent_"+id).val();
	//操作前
 	var preModContent = $("#tempGroupCont_"+id).val();
 	var onclick = $(ts).attr("onclick");
 	if(strIsNull(modContent)){//没写内容
 		$("#groupContent_"+id).focus();
    	return;
    }else if(depStr==preDepStr && modContent==preModContent){//内容相同不提交
    	cancleEditGroup(id);
        return;
     }else{
		 $("#weekModForm").ajaxSubmit({
		        type:"post",
		        url:"/weekReport/updateWeekRepModContent?sid="+sid+"&t="+Math.random(),
		        data:{"id":id,"modContent":modContent,"modId":modId,"deps":deps},
		        dataType: "json",
		        buforeSubmit:function(a,o,f){
		        	$(ts).removeAttr("onclick");
		        },
		        traditional :true,
		        success:function(data){
		            if(data.status=='y'){
		            	
		            	var selectOpts = $("#team"+id).find(".groupLevContent").find(".groupLevDep").find("select").find("option");
		            	$("#team"+id).find(".groupLevSave").find(".tempdep").find("select").html('')
		            	//重新赋临时值
		            	for(var i=0;i<selectOpts.length;i++){
		            		$("#team"+id).find(".groupLevSave").find(".tempdep")
		            		.find("select").append('<option value="'+$(selectOpts[i]).val()+'" selected="selected">'+$(selectOpts[i]).html()+'</option>');
		            	}
		            	$("#tempGroupCont_"+id).attr("value",modContent);
		            	var html ='\n <div class="depLevTxt" >'+modContent;
		            	html +='\n </div>';
		            	//展现的代码
		            	html +='<div class="depSelect" style="clear:both;float:left;margin-top: 5px">';
		            	//选项
		            	for(var i=0;i<selectOpts.length;i++){
		            		html +='\n<span class="label label-default margin-right-5" >'+$(selectOpts[i]).html()+'</span>';
		            	}
		            	html +='\n </div>';
		            	
		            	$("#team"+id).find(".groupLevContent").html(html);
		            	$("#team"+id).find(".groupLevSave").hide();
		            	$("#team"+id).find(".groupLevUpdate").show();
		            	
		            	setStyle();
		            	
		            }else if(data.status=='f'){
		            	window.self.location="/weekReport/listPagedWeekRep?pager.pageSize=10&sid="+sid;
		            }
		            $(ts).attr("onclick",onclick);
		            showNotification(1, "修改成功！");
		        },
		        error:function(XmlHttpRequest,textStatus,errorThrown){
		        	 $(ts).attr("onclick",onclick)
		        	 showNotification(2,"系统错误，请联系管理人员！")
		        }
		    });
     }
}

//添加成员模板条目
function addMember(sid,ts,modId){
	var member = $(".addMemberLev").find("#listMembers_memberId").val();
	var memberContent = $("#memberContent").val();
	
	var members = new Array();
	for(var i=0;i<member.length;i++){
		members.push(member[i]);
	}
	var onclick = $(ts).attr("onclick");
	if(strIsNull(members)){//成员为空
		art.dialog.alert("请选择成员");
	}else if(strIsNull(memberContent)){//成员栏要求为空
		$("#memberContent").focus();
	}else{
		//防止重复提交
		 $("#weekModForm").ajaxSubmit({
		        type:"post",
		        url:"/weekReport/addWeekModContent?sid="+sid+"&t="+Math.random(),
		        data:{"modContent":memberContent,"modId":modId,"repLev":3,"hideState":0,"members":members},
		        dataType: "json", 
		        buforeSubmit:function(a,o,f){
		        	$(ts).removeAttr("onclick")
		        },
		        traditional :true,
		        success:function(data){
		        	$("#noMemberLev").remove();
		            if(data.status=='y'){
		            	var html = '\n <div id="team'+data.id+'" class="memberLevDetail modContent" >';
		            	html +='\n<div class="memberLevContent" style="text-decoration:none;color:#000000">';
		            	
		            	html +='\n <div class="memberLevTxt">';
		            	html +='\n '+memberContent;
		            	html +='\n </div>';
		            
		            	html +='\n <div class="memberLevPic">';
		            	var srcImg = $("#userMoreImglistMembers_memberId").find("img");
		            	for(var i=0;i<srcImg.length;i++){
		            		html +='\n <div style="float: left;margin-top: 5px;margin-left: 5px;">';
		            		html +='\n <img style="display:block;float:left;margin-left:3px;"  src="'+$(srcImg[i]).attr("src")+'" title="'+$(srcImg[i]).attr("title")+'" ></img>';
		            		html +='\n <i class="zbmember-Name" style="float:left;margin-top: 15px">'+$(srcImg[i]).attr("title")+'</i>';
		            		html +='\n </div>';
		            	}
						
						html +='\n </div>';
		            	html +='\n </div>';
		            	//编辑 
		            	html +='\n <div class="memberLevUpdate">';
		            	html +='\n 	<div>';
		            	html +='\n 		<a href="javascript:void(0)" onclick="editMemberContent('+data.id+',\''+sid+'\')" style="color:#1E88C7;padding-right: 10px">编辑</a>';
						html +='\n 		<a href="javascript:void(0)" onclick="hideContent(\''+sid+'\','+data.id+',1,this,'+modId+',3)" style="color:#1E88C7;padding-right: 10px">隐藏</a>';
						html += '\n 	<a href="javascript:void(0)" class="setRequire" isRequire="1" contentId="'+data.id+'" modId="'+modId+'" style="color:#1E88C7;padding-right: 10px">必填</a>';
						html +='\n 		<a href="javascript:void(0)" onclick="delContent(\''+sid+'\','+data.id+',this,'+modId+',3)" style="color:#1E88C7;padding-right: 2px">删除</a>';
						html +='\n 	</div>';
						html +='\n </div>';
						//显示
						html +='\n <div class="memberLevShow">';
						html +='\n 	<div>';
						html +='\n 		已隐藏,<a href="javascript:void(0)" onclick="hideContent(\''+sid+'\','+data.id+',0,this,'+modId+',3)" style="color:#1E88C7;padding-right: 10px"> 显示</a>';
						html += '\n 	<a href="javascript:void(0)" class="setRequire" isRequire="1" contentId="'+data.id+'" modId="'+modId+'" style="color:#1E88C7;padding-right: 10px">必填</a>';
						html +='\n 		<a href="javascript:void(0)" onclick="delContent(\''+sid+'\','+data.id+',this,'+modId+',3)" style="color:#1E88C7;padding-right: 2px">删除</a>';
						html +='\n 	</div>';
						html +='\n </div>';
						//修改 
						html +='\n <div class="memberLevSave">';
						//存放模板临时条目 
						html +='\n <div class="tempInfo" style="display:none">';
						html +='\n <div class="tempUser">';
						html +='\n 	<select	 multiple="multiple" moreselect="true">';
						//选项
						var options = $(".addMemberLev").find("#listMembers_memberId").find("option");
						for(var i=0;i<options.length;i++){
							html +='\n <option value="'+$(options[i]).val()+'" selected="selected">'+$(options[i]).html()+'</option>';
						}
						html +='\n 	</select>';
						html +='\n </div>';
						html +='\n <div class="tempImg">';
						
						for(var i=0;i<srcImg.length;i++){
							var memberId = $(srcImg[i]).attr("id").replace("user_img_","");
							html +='\n<img id="'+data.id+'_'+memberId+'" src="'+$(srcImg[i]).attr("src")+'" title="'+$(srcImg[i]).attr("title")+'" ></img>';
						}
						html +='\n </div>';
						html +='\n </div>';
						html +='\n 	<input type="hidden" id="tempMemberCont_'+data.id+'" name="tempMemberCont" value="'+memberContent+'"/>';
						html +='\n 	<div>';
						html +='\n 		<a href="javascript:void(0)" onclick="updateMember(\''+sid+'\','+data.id+','+modId+',this)" class="green_btn" style="color:#1E88C7;padding-right: 10px;">保存</a>';
						html +='\n 		<a href="javascript:void(0)" onclick="cancleEditMember('+data.id+')" style="color:#1E88C7;padding-right: 10px;">取消</a>';
						html += '\n 	<a href="javascript:void(0)" class="setRequire" isRequire="1" contentId="'+data.id+'" modId="'+modId+'" style="color:#1E88C7;padding-right: 10px">必填</a>';
						html +='\n 		<a href="javascript:void(0)" onclick="delContent(\''+sid+'\','+data.id+',this,'+modId+',3)" style="color:#1E88C7;padding-right: 2px">删除</a>';
						html +='\n 	</div>';
						html +='\n </div>';
						$(".memberLev").append(html);
						setStyle();
						
		            }else if(data.status=='f'){
		            	window.self.location="/weekReport/listPagedWeekRep?pager.pageSize=10&sid="+sid;
		            }
		            $(ts).attr("onclick",onclick);
		            $('.addMemberLev').show();$('.addMemberLevContent').hide();
		        	$(".addMemberLev").find(".memberContent").find("input").attr("value","")
		        	
		        	$(".addMemberLev").find("#userMoreImglistMembers_memberId").html('');
		        	$(".addMemberLev").find("#listMembers_memberId").html('');
		        	showNotification(1, "添加成功！");
		        },
		        error:function(XmlHttpRequest,textStatus,errorThrown){
		        	$(ts).attr("onclick",onclick);
		        	$('.addMemberLev').show();$('.addMemberLevContent').hide();
			        $(".addMemberLev").find(".memberContent").find("input").attr("value","")
			        $(".addMemberLev").find("#userMoreImglistMembers_memberId").html('');
			    	$(".addMemberLev").find("#listMembers_memberId").html('');
			    	showNotification(2,"系统错误，请联系管理人员！")
		        }
		    });
	}
}

//编辑部门问题内容
function editMemberContent(id,sid){
	var modContent = $("#tempMemberCont_"+id).val();
	var html ='\n 	<div class="memberLevTxt">';
	html +='\n 			<input id="memberContent_'+id+'" class="form-control small" type="text" name="memberContent" style="width: 90%" value="'+modContent+'"/>';
	html +='\n 		</div> ';
	html +='\n 		<div class="memberLevPic" style="clear:both;">';
	html +='\n 			<div style="float: left; display:none;">';
	html +='\n 				<select id="listMembers_memberId'+id+'"';
	html +='\n 					name="listMembers.memberId'+id+'" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true"';
	html +='\n 					style="width: 90%;">';
	//选项
	var options = $("#team"+id).find(".memberLevSave").find(".tempInfo").find(".tempUser").find("select").find("option");
	for(var i=0;i<options.length;i++){
		html +='\n				<option value="'+$(options[i]).val()+'" selected="selected">'+$(options[i]).html()+'</option>';
	}
	html +='\n 				</select>';
	html +='\n 			</div>';
	//存放图片
	html +='\n 			<div id="userMoreImglistMembers_memberId'+id+'" style="float:left" >';
	
	//图片
	var pics = $("#team"+id).find(".memberLevSave").find(".tempInfo").find(".tempImg").find("img");
	for(var i=0;i<pics.length;i++){
		var memberId = $(pics[i]).attr("id").replace(id+'_','');
		html +='\n 		<div style="float: left;margin-top: 5px;margin-left: 5px;">';
		html +='\n		<img id="user_img_'+memberId+'" style="display:block;float:left;margin-left:3px;" ondblclick="removeWeekClickUser(\'listMembers_memberId'+id+'\','+memberId+')" src="'+$(pics[i]).attr("src")+'" title="'+$(pics[i]).attr("title")+'" ></img>';
		html +='\n		<i class="zbmember-Name" style="float:left;margin-top: 15px">'+$(pics[i]).attr("title")+'</i>';
		html +='\n 		</div>';
	}
	html +='\n 			</div>';
	html +='\n 	<a href="javascript:void(0)" class="btn btn-blue btn-xs margin-top-5"  title="请选择人员" onclick="userMore4Week(\'listMembers_memberId'+id+'\',\'\',\''+sid+'\',\'\','+id+')" title="请选择人员"><i class="fa fa-plus"></i>选择</a> ';
	html +='\n </div> ';
	

	$("#team"+id).find(".memberLevContent").html(html);
	$("#team"+id).find(".memberLevSave").show();
	$("#team"+id).find(".memberLevUpdate").hide();
	
}


/* 清除下拉框中选择的option */
function removeWeekClickUser(id,selectedUserId) {
	var selectobj = document.getElementById(id);
	for ( var i = 0; i < selectobj.options.length; i++) {
		if (selectobj.options[i].value==selectedUserId) {
			selectobj.options[i] = null;
			break;
		}
	}
	$("#user_img_"+selectedUserId).parent().remove();
	
	selected(id);
}

//取消修改成员模板条目
function cancleEditMember(id){
	var modContent = $("#tempMemberCont_"+id).val();
	
	var srcImg = $("#team"+id).find(".memberLevSave").find(".tempInfo").find(".tempImg").find("img");
	var html ='\n <div class="memberLevTxt">';
	html +='\n '+modContent;
	html +='\n </div>';
	html +='\n <div class="memberLevPic">';
	for(var i=0;i<srcImg.length;i++){
		html+='\n <div style="float: left;margin-top: 5px;margin-left: 5px;">';
		html +='\n 	<img style="display:block;float:left;margin-left:3px;"  src="'+$(srcImg[i]).attr("src")+'" title="'+$(srcImg[i]).attr("title")+'" ></img>';
		html +='\n		<i class="zbmember-Name" style="float:left;margin-top: 15px">'+$(srcImg[i]).attr("title")+'</i>';
		html +='\n</div>';
	}
	
	html +='\n </div>';
	
	$("#team"+id).find(".memberLevContent").html(html);
	$("#team"+id).find(".memberLevSave").hide();
	$("#team"+id).find(".memberLevUpdate").show();
	
}
//保存成员模板条目修改
function updateMember(sid,id,modId,ts){
	var options = $("#team"+id).find(".memberLevContent").find(".memberLevPic").find("select").val();
	//操作后的值
	var memStr = '';
	var members = new Array();
	if(!strIsNull(options)){
		options.sort()
		for(var i=0;i<options.length;i++){
			members.push(options[i]);
			memStr +=options[i]+",";
		}
	}else{
		art.dialog.alert("请选择人员").time(1);
		return;
	}
	//操作前的值
	var preOpts = $("#team"+id).find(".memberLevSave").find(".tempInfo").find(".tempUser").find("select").val();
	var preMemStr = '';
	if(!strIsNull(preOpts)){
		preOpts.sort();
		for(var i=0;i<preOpts.length;i++){
			preMemStr += preOpts[i]+",";
		}
	}
	
	//防止重复提交
	var modContent = $("#memberContent_"+id).val();
     
 	var preModContent = $("#tempMemberCont_"+id).val();
 	if(options.length==0){//没写内容
 		$("#memberContent_"+id).focus();
    	return;
    }else if(memStr==preMemStr && modContent==preModContent){//内容相同不提交
    	cancleEditMember(id);
        return;
     }else{
    	 var pics =  $("#team"+id).find("#userMoreImglistMembers_memberId"+id).find("img");
    	 options = $("#team"+id).find(".memberLevContent").find(".memberLevPic").find("select").find("option")
    	 
    	 var onclick = $(ts).attr("onclick");
    	 
		 $("#weekModForm").ajaxSubmit({
		        type:"post",
		        url:"/weekReport/updateWeekRepModContent?sid="+sid+"&t="+Math.random(),
		        data:{"id":id,"modContent":modContent,"modId":modId,"members":members},
		        dataType: "json",
		        beforeSubmit:function(a,o,f){
		        	$(ts).removeAttr("onclick")
		        },
		        traditional :true,
		        success:function(data){
		            if(data.status=='y'){
		            	//重新赋临时值
		            	//将select清空
		            	var selectTmp = $("#team"+id).find(".memberLevSave").find(".tempInfo").find(".tempUser").find("select");
		            	$(selectTmp).html('');
		            	for(var i=0;i<options.length;i++){
		            		$(selectTmp).append("<option selected='selected' value='"
								+ $(options[i]).val() + "'>" + $(options[i]).html()
								+ "</option>");
		            	}
		            	var imgTmp = $("#team"+id).find(".memberLevSave").find(".tempInfo").find(".tempImg");
		            	$(imgTmp).html('');
		            	for(var i=0;i<pics.length;i++){
		            		var memberId = $(pics[i]).attr("id").replace("user_img_","");
		            		$(imgTmp).append('<img id="'+id+'_'+memberId+'" src="'+$(pics[i]).attr("src")+'" title="'+$(pics[i]).attr("title")+'" ></img>');
		            	}
		            	
		            	$("#tempMemberCont_"+id).attr("value",modContent);
		            	
		            	//展现的代码
		            	var html = '<div class="memberLevTxt" style="clear:both;float:left;margin-top: 5px">'+modContent;
						html +='\n</div>';
						html +='\n <div class="memberLevPic">';
		            	for(var i=0;i<pics.length;i++){
		            		var memberId = $(pics[i]).attr("id").replace("user_img_","");
		            		html+='\n<div style="float: left;margin-top: 5px;margin-left: 5px;">';
		            		html +='\n <img id="user_img_'+memberId+'" style="display:block;float:left;margin-left:3px;"  src="'+$(pics[i]).attr("src")+'" title="'+$(pics[i]).attr("title")+'" ></img> ';
		            		
		            		html +='\n<i class="zbmember-Name" style="float:left;margin-top: 15px">'+$(pics[i]).attr("title")+'</i>';
		            		html +='\n</div>';
		            	}
		            	html +='\n</div>';
					
		            	$("#team"+id).find(".memberLevContent").html(html);
		            	$("#team"+id).find(".memberLevSave").hide();
		            	$("#team"+id).find(".memberLevUpdate").show();
		            	setStyle();
		            	showNotification(1, "修改成功！");
		            	
		            }else if(data.status=='f'){
		            	window.self.location="/weekReport/listPagedWeekRep?pager.pageSize=10&sid="+sid;
		            }
		            $(ts).attr("onclick",onclick)
		        },
		        error:function(XmlHttpRequest,textStatus,errorThrown){
		        	$(ts).attr("onclick",onclick)
		        	showNotification(2,"系统错误，请联系管理人员！")
		        }
		    });
     }
}
//删除条目
function delContent(sid,id,ts,modId,repLev){
	var onClick = $(ts).attr("onclick");
	
	$("#weekModForm").ajaxSubmit({
	        type:"post",
	        url:"/weekReport/delModContent?sid="+sid+"&t="+Math.random(),
	        data:{"id":id,"modId":modId,"repLev":repLev},
	        dataType: "json",
	        buforeSubmit:function(a,o,f){
	        	$(ts).removeAttr("onclick");
	        },
	        success:function(data){
	            if(data.status=='y'){
	            	$("#team"+id).remove();
		            if(1==repLev){//团队级模板设置删除线
		            	var ques = $(".teamLev").find(".teamLevDetail").length;
		            	if(ques==0){
		            		$(".teamLev").append('<div class="noTeamLev" id="noTeamLev">您还没有设置部门模板条目</div>');
		            	}
		            }else if(2==repLev){//部门级模板设置删除线
		            	var ques = $(".groupLev").find(".groupLevDetail").length;
		            	if(ques==0){
		            		$(".groupLev").append('<div class="noGroupLev" id="noGroupLev">您还没有设置部门模板条目</div>');
		            	}
		            }else if(3==repLev){//成员级模板设置删除线
		            	var ques = $(".memberLev").find(".memberLevDetail").length;
		            	if(ques==0){
		            		$(".memberLev").append('<div class="noMemberLev" id="noMemberLev">您还没有设置成员模板条目</div>');
		            	}
		            }
	            }else if(data.status=='f'){
	            	window.self.location="/weekReport/listPagedWeekRep?pager.pageSize=10&sid="+sid;
	            }
	            
	            $(ts).attr("onclick",onclick)
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	$(ts).attr("onclick",onclick);
	        	showNotification(2,"系统错误，请联系管理人员！")
	        }
	    });
 }



function getWeekRepTalkString(weekTalk,sid,sessionUser,delWeek){
	var html = '';
	if(weekTalk.parentId==-1){
		html+='\n <div id="talk_'+weekTalk.id+'" class="ws-shareBox">';
	}else{
		html+='\n <div id="talk_'+weekTalk.id+'" class="ws-shareBox ws-shareBox2">';
	}
	html+='\n 	<div class="shareHead" data-container="body" data-toggle="popover"  data-user='+weekTalk.talker+' data-busId='+weekTalk.weekReportId+' data-busType=\'006\'>';
	html += '	<img src="/downLoad/userImg/'+weekTalk.comId+'/'+weekTalk.talker+'?sid='+sid+'" title="'+weekTalk.talkerName+'"></img>\n';
	html+='\n	</div>';
	html+='\n	<div class="shareText">';
	html+='\n		<span class="ws-blue">'+weekTalk.talkerName+'</span>';
	if(weekTalk.parentId!=-1){
		html+='\n	<r>回复</r>';
		html+='\n	<span class="ws-blue">'+weekTalk.ptalkerName+'</span>';
	}
	html+='\n		<p class="ws-texts">'+weekTalk.content+'</p>';
	//附件
	var files = weekTalk.listWeekRepTalkFile;
	
	if(files.length>0){
		html+='\n	<div class="file_div">';
		for(var i=0;i<files.length;i++){
			var upfiles = files[i];
			if(upfiles.isPic==1){
				html+='\n<p class="p_text">附件('+(i+1)+')：';
				html+='\n	<img onload="AutoResizeImage(350,0,this,\'otherIframe\')"';
				html+='\n		src="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'"/>';
				html+='\n		&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title=""></a>';
				html+='\n		&nbsp;&nbsp;<a class="fa fa-eye" onclick="showPic(\'/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'\',\''+sid+'\',\''+upfiles.upfileId+'\',\'006\',\''+weekTalk.weekReportId+'\')" title="预览"></a>';
				html+='\n</p>';
			}else{
				html+='\n<p class="p_text">附件('+(i+1)+')：';
				html+='\n'+upfiles.filename;
				var fileExt = upfiles.fileExt;
				if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx'){
					html+='\n	&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0)" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+sid+'\')" title="下载"></a>';
					html+='\n	&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'006\',\''+weekTalk.weekReportId+'\')" title="预览"></a>';
				}else if(fileExt=='txt' ||fileExt=='pdf'){
					html+='\n	&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
					html+='\n	&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'006\',\''+weekTalk.weekReportId+'\')" title="预览"></a>';
				}else{
					html+='\n	<a class="fa fa-download" href="javascript:void(0)" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+sid+'\')" title="下载"></a>';
				}
				html+='\n</p>';
			}
		}
		html+='\n		</div>';
	}
	//附件结束 
	html+='\n	 <div class="ws-type">';
	//发言人可以删除自己的发言 
	if(sessionUser.id==weekTalk.talker && !delWeek){
		html+='\n	<a href="javascript:void(0);" id="delWeekTalk_'+weekTalk.id+'" onclick="delWeekTalk('+weekTalk.id+',1)" class="fa fa-trash-o" title="删除"></a>';
	}
	html+='\n 		<a id="img_'+weekTalk.id+'" name="replyImg" href="javascript:void(0);" class="fa fa-comment-o" title="回复" onclick="showArea(\'addTalk'+weekTalk.id+'\')"></a>';
	html+='\n 		<time>'+weekTalk.recordCreateTime+'</time>';
	html+='\n	</div>';
	html+='\n</div>';
	html+='\n<div class="ws-clear"></div>';
	html+='\n</div>';
	//回复层
	html+='\n <div id="addTalk'+weekTalk.id+'" name="replyTalk" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk">';
	html+='\n	<div class="shareText">';
	html+='\n		<div class="ws-textareaBox" style="margin-top:10px;">';
	html+='\n 			<textarea id="operaterReplyTextarea_'+weekTalk.id+'" name="operaterReplyTextarea_'+weekTalk.id+'" rows="" cols="" class="form-control" placeholder="回复……"></textarea>';
	html+='\n			<div class="ws-otherBox">';
	html+='\n				<div class="ws-meh">';
	//表情
	html+='\n 					<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_'+weekTalk.id+'" onclick="addBiaoQingObj(\'biaoQingSwitch_'+weekTalk.id+'\',\'biaoQingDiv_'+weekTalk.id+'\',\'operaterReplyTextarea_'+weekTalk.id+'\');"></a>';
	html += '\n				    <div id="biaoQingDiv_'+weekTalk.id+'" class="blk" style="display:none;position:absolute;width:200px;top:100px;z-index:99;left: 15px">';
	html += '\n				     	<div class="main">';
	html += '\n				           	<ul style="padding: 0px">';
	html += ' \n'+getBiaoqing();
	html += '\n				           	</ul>';
	html += '\n				       	 </div>';
	html += '\n				    </div>';
	html += '\n				 </div>';
	//常用意见
	html+='\n				<div class="ws-plugs">';
	html+='\n					<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea(\'operaterReplyTextarea_'+weekTalk.id+'\',\''+sid+'\');" title="常用意见"></a>';
	html+='\n				</div>';
	//@机制
    html+='\n				<div class="ws-plugs">';
    html+='\n					<a class="btn-icon" href="javascript:void(0)" data-todoUser="yes" data-relateDiv="todoUserDiv_'+weekTalk.id+'" title="告知人员">@</a>';
    html+='\n				</div>';
	//提醒方式
/*	html+='\n				<div class="ws-remind">';
	html+='\n					<span class="ws-remindTex">提醒方式：</span>';
	html+='\n					<div class="ws-checkbox">';
	html+='\n						<label class="checkbox-inline">';
	html+='\n							<input id="inlineCheckbox1" type="checkbox" value="option1"/>短信</label>';
	html+='\n						<label class="checkbox-inline">';
	html+='\n							<input id="inlineCheckbox1" type="checkbox" value="option1"/>邮件</label>';
	html+='\n						<label class="checkbox-inline">';
	html+='\n							<input id="inlineCheckbox1" type="checkbox" value="option1"/>桌面推送</label>';
	html+='\n					</div>';
	html+='\n				</div>';*/
	//分享按钮
	html+='\n				<div class="ws-share">';
	html+='\n					<button type="button" class="btn btn-info ws-btnBlue" data-relateTodoDiv="todoUserDiv_' + weekTalk.id + '" onclick="addTalk('+weekTalk.weekReportId+','+weekTalk.id+',this,'+weekTalk.talker+')">回复</button>';
	html+='\n				</div>';
	//相关附件 
	html+='\n				<div style="clear: both;"></div>';
	//@机制
	html+='\n				<div id="todoUserDiv_' + weekTalk.id + '" class="padding-top-10">\n' ;
    html+='\n        		</div>'
	html+='\n				<div class="ws-notice">';
	
	html+= '\n 						<div style="clear:both;width: 300px" class="wu-example">';
	//用来存放文件信息
	html+= '\n 							<div id="thelistlistUpfiles_'+weekTalk.id+'_upfileId" class="uploader-list" ></div>';
	html+= '\n 						 	<div class="btns btn-sm">';
	html+= '\n 								<div id="pickerlistUpfiles_'+weekTalk.id+'_upfileId">选择文件</div>';
	html+= '\n 							</div>';
	
	html+= '\n							<script type="text/javascript">\n';
	html+= '\n 								loadWebUpfiles(\'listUpfiles_'+weekTalk.id+'_upfileId\',\''+sid+'\',\'otherIframe\',\'pickerlistUpfiles_'+weekTalk.id+'_upfileId\',\'thelistlistUpfiles_'+weekTalk.id+'_upfileId\',\'filelistUpfiles_'+weekTalk.id+'_upfileId\');';
	html+= '\n 							</script>\n';
	html+= '\n 						<div style="position: relative; width: 350px; height: 90px;display: none">';
	html+= '\n 							<div style="float: left; width: 250px">';
	html+= '\n 								<select  list="listUpfiles_'+weekTalk.id+'" listkey="upfileId" listvalue="filename" id="listUpfiles_'+weekTalk.id+'_upfileId" name="listUpfiles_'+weekTalk.id+'.upfileId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">';
	html+= '\n 							 	</select>';
	html+= '\n 							</div>';
	html+= '\n 						</div>';
	html+= '\n 					</div>';
	
	html+='\n				</div>';
	
	html+='\n			</div>';
	html+='\n		</div>';
	html+='\n	</div>';
	html+='\n</div>';
	return html;
}

//设置周报查看权限
function setWeekRepViewScope(sid,ts){
	//当前活动选项
	var preActive = $(".submenu").find(".active");
	//当前活动选项移除背景色
	$(preActive).removeClass();
	//配置设置背景色
	$(ts).parent().addClass("active bg-themeprimary");
	
	window.top.layer.open({
		 type: 2,
		  //title: ['审批人员设置', 'font-size:18px;color:red;'],
		  title:false,
		  closeBtn:0,
		  area: ['600px', '460px'],
		  fix: true, //不固定
		  maxmin: false,
		  scrollbar:false,
		  move: false,
		  content:['/weekReport/weekViewSetPage?sid='+sid+'&t='+Math.random(),'no'],
		  btn: ['关闭'],
		  yes: function(index, layero){
			  window.top.layer.close(index)
		  },cancel: function(){ 
		  },success: function(layero,index){ 
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
				  window.top.layer.close(index);
				})
		  },end:function(index){
			//配置删除背景色
			  $(ts).parent().removeClass();
			  //恢复前一个选项的背景色
			  $(preActive).addClass("active bg-themeprimary");
			  checkLeader(sid,function(data){
				   if(data.status!='y'){
					   window.top.layer.close(index);
					   window.top.layer.close(tabIndex);
				   }
				})
		  }
	 });
}
//窗体点击事件检测
document.onclick = function(e){
	var evt = e?e:window.event;
	var ele = $(evt.srcElement || evt.target);
	if ($(ele).parents("#moreCondition_Div").length == 1) { 
		if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
			return false;
		}else{
			if(!$("#moreCondition_Div").hasClass("open")){
				$(".searchCond").find(".open").removeClass("open");
				$("#moreCondition_Div").addClass("open");
			}
		}
	} else{
		$("#moreCondition_Div").removeClass("open");
	}
}
/**
 * 展示查询条件中更多
 */
function displayMoreCond(divId){
	if($("#"+divId).hasClass("open")){
		$("#"+divId).removeClass("open");
	}else{
		$("#"+divId).addClass("open")
	}
}
/**
 * 清空更多查询条件
 */
function resetMoreCon(divId){
	$("#"+divId).find("input").val('');
	
}
//人员筛选
function userMoreForUserIdCallBack(options,userIdtag){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#"+userIdtag+"_select").find("option").remove();
	for(var i =0;i<options.length;i++){
		$("#"+userIdtag+"_select").append("<option selected='selected' value='"+options[i].value+"' >"+options[i].text+"</option>");
	}
	$("#weekForm").submit();
}
//移除筛选
function removeChoose(userIdtag,sid,userId){
	$("#"+userIdtag+"_select").find("option[value='"+userId+"']").remove();
	$("#weekForm").submit();
}

$(function(){
	$("body").on("click",".setRequire",function(){
		//是否必填
		var isRequire = $(this).attr("isRequire");
		//条目主键
		var contentId = $(this).attr("contentId");
		//模板主键信息
		var modId = $(this).attr("modId");
		if(ReportMod.loadState=='1'){//正在处理中
			return false;
		}
		var _this = $(this);
		//设置是否必填开始
		ReportMod.updateRequire(isRequire,contentId,modId,function(data){
			if(data.status=='y'){
				//移除回或是添加必填信息
				isRequire == '0'?$(_this).parents(".modContent").find("font").remove():$(_this).parents(".modContent").prepend('<font style="color: red;float: left;line-height: 30px">*</font>')
				$(_this).attr("isRequire",isRequire == '0'?"1":'0');
				$(_this).html(isRequire == '1'?"非必填":'必填');
            }else if(data.status=='f'){
            	window.self.location="/weekReport/listPagedWeekRep?pager.pageSize=10&sid="+pageParam.sid;
            }
		});
		//设置是否必填结束
	});
	
})
//周报模板样式信息
var ReportMod = {
	loadState:"0",
	updateRequire:function(isRequire,contentId,modId,callback){
		ReportMod.loadState="1";
		$("#weekModForm").ajaxSubmit({
	        type:"post",
	        url:"/weekReport/updateRequireContent?sid="+pageParam.sid+"&t="+Math.random(),
	        data:{"id":contentId,"isRequire":isRequire,"modId":modId},
	        dataType: "json", 
	        success:function(data){
	        	ReportMod.loadState="0";
	        	callback(data);
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	            	showNotification(2,"系统错误，请联系管理人员！")
	            	ReportMod.loadState="0";
	        }
	    });
	}
}



