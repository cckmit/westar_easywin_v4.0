//为隐藏字段赋值
 function gets_value(index,ts,list,div){
     $("input[name='"+div+"']").val(index); 
     formSub()
 } 
var videoId=-1; 
$(function(){
	
	//更多筛选条件显示层
	$("#moreFilterCondition").click(function(){
        var display = $("#moreFilterCondition_div").css("display");
        if("none"==display){
            $(this).html('隐藏');
            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
        }else if("block"==display){
            $(this).html('更多筛选')
            $("#moreFilterCondition_div").animate({height: 'toggle', opacity: 'toggle'},320);
        }

      });
	//任务名筛选
	$("#fileName").blur(function(){
		formSub();
	});
	//文本框绑定回车提交事件
	$("#fileName").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	formSub();
        }
    });
	
	setStyle()
});
function formSub(){
	$("#fileSearchForm").find("input[name='startDate']").val($("#startDate").val());
	$("#fileSearchForm").find("input[name='endDate']").val($("#endDate").val());
	$("#fileSearchForm").submit();
}
/**
 * 展开文件夹
 * @param classifyId 当前文件夹主键
 * @return
 */
 function dirFile(classifyId){
	 $("#classifyId").attr("value",classifyId)
	 $("#fileSearchForm").submit();
 }
//添加文件夹准备
	 function addDirPre(sid){
		 var len = $(".dirTr").find("td").length;
		 if(len>0){
			 layer.tips("请先填写文件夹名称",$(".dirTr").find("input"),{tips:1});
			 $(".dirTr").find(".dirName input").focus();
			 return;
		 }
		var html = '\n <tr class="dirTr">';
		html+='\n		<td height="40" colspan="6">';
		html+='\n			<div>';
		html+='\n				<div style="width: 75%;float:left;margin-left: 40px" class="dirName">';
		html+='\n					<div style="float:left;width:95%"><input type="text" class="form-control" name="dirName" style="width: 100%;height: 30px" /></div>';
		html+='\n				</div>';
		html+='\n				<div style="float:left;" class="opt">';
		html+='\n					<a class="btn btn-info ws-btnBlue" onclick="saveDir(this,\''+sid+'\')">保存</a>';
		html+='\n					<a class="btn btn-default" onclick="cancelAddDir(this)">取消</a>';
		html+='\n				</div>';
		html+='\n			</div>';
		html+='\n		</td>';
		html+='\n	</tr>';
		$(".listDirs").prepend(html);
	 }
	 
	//取消添加文件夹
	 function cancelAddDir(ts){
		 $(".dirTr").remove();
	 }
	 
	//保存文件夹
	 function saveDir(ts,sid){
	 	var dirName = $(".dirTr").find(".dirName").find("input").val().replace(/\s+/g,"");
	 	if(dirName.length==0){
	 		    layer.tips("请先填写文件夹名称",$(".dirTr").find("input"),{tips:1});
	 			$(".dirName").find("input").focus();
	 			return;
	 	}else{
	 		var onclick = $(ts).attr("onclick");
	 		 //异步提交表单
	 		$.ajax({
	 	        type:"post",
	 	        url:"/onlineLearn/addVideoDir?sid="+sid+"&t="+Math.random(),
	 	        dataType: "json", 
	 	        beforeSend:function(){
	 				$(ts).removeAttr("onclick");
	 			},
	 	        data:{"typeName":dirName,"classifyId":$("#classifyId").val()},
	 	        success:function(data){
	 		         var status = data.status;
	 		         if(status=='y'){
	 			        var classify = data.fileClassify;
	 			        var sessionUser = data.sessionUser;
	 		        	$(".dirTr").remove();
	 		        	var html ='\n<tr class="dirInfo">';
	 		        	html+='\n<td height="40">';
	 		        	html+='\n	<label>';
	 		        	html+='\n		<input type="checkbox" name="ids"  fileType="dir" value="'+classify.id+'"/>';
	 		        	html+='\n		<span class="text"></span>';
	 		        	html+='\n	</label>';
	 		        	html+='\n	</td>';
	 		        	html+='\n<td height="40" class="dirName">';
	 		        	html +='\n		<img src="/static/images/folder_open.png" style="clear: both;border-radius: 0px;height: auto;width: auto;border:0px"/>';
	 		        	html += '\n 	<input type="hidden" name="tempDirName" value="'+dirName+'"/>';
	 		        	html += '\n 	<a href="javascript:void(0)" onclick="dirFile('+classify.id+')">'+dirName+'</a></td>';
						html+='\n<td>--</td>';
						html+='\n<td height="40">--</td>';
						html+='\n<td height="40" class="infoDetail">';
						html+='\n <div class="ticket-user pull-left other-user-box">';
						html +='\n<img src="/downLoad/userImg/'+sessionUser.comId+'/'+sessionUser.id+'?sid='+sid+'" title="'+sessionUser.userName+'" class="user-avatar"></img>';
						html+='\n	<span class="user-name">'+sessionUser.userName+'</span>';
						html+='\n</div>';
						html+='\n</td>';
						html+='\n<td height="40" class="infoDetail">'+classify.recordCreateTime.substr(0,10)+'</td>';
						html+='\n<td height="40" colspan="2" style="display: none;text-align: center;" class="opt">';
						html+='\n	<a href="javascript:void(0)" class="btn btn-info ws-btnBlue"  onclick="modifyDirName(this,'+classify.id+',\''+sid+'\')">修改</a>';
						html+='\n	<a href="javascript:void(0)" class="btn btn-info ws-btnBlue"  onclick="moveDir(this,'+classify.id+',\''+sid+'\')">移动</a>';
						html+='\n	<a href="javascript:void(0)" class="btn btn-info ws-btnBlue" style="background-color: red;" onclick="delDir(this,'+classify.id+',\''+sid+'\')">删除</a>';
						html+='\n</td>';
						html+='\n</tr>';
	 					$(".listDirs").prepend(html);
	 					
	 		         }else{
	 			         var info = data.info;
	 			         art.dialog.alert(info);
	 			         //可以再次提交
	 			         $(ts).attr("onclick",onclick);
	 		         }
	 		         setStyle()
	 	        },
	 	        error:function(XmlHttpRequest,textStatus,errorThrown){
	 				//可以再次提交
	 				$(ts).attr("onclick",onclick);
	 	        }
	 	    });
	 	}
	 }
	 /**
	  * 批量操作
	  * @param index
	  * @param ts
	  * @param list
	  * @param div
	  * @param sid
	  * @return
	  */
	  function batchOpt(index,ts,list,div,sid){
	 	 if(1==index){//批量移动
	 		//选中的文件夹
	 		var dirs = getCheckedDirs('ids');
	 		//选中的文件
	 		var files = getCheckedFiles('ids',1);
	 		if((dirs.length+files.length)==0){
	 			window.top.layer.alert("请选择要移动的文件夹或文件");
	 			return;
	 			
	 		}else{
	 			batchMove(sid,dirs,files);
	 		}
	 	 }else if(2==index){//批量删除
	 		//选中的文件夹
	 		var dirs = getCheckedDirs('ids');
	 		//选中的文件
	 		var files = getCheckedFiles('ids',2);
	 		if((dirs.length+files.length)==0){
	 			window.top.layer.alert("请确认文件是自己上传的！");
	 			return;
	 		}else{
	 			batchDel(sid,dirs,files);
	 		}
	 	 }else if(3==index){//批量分享
	 		//选中的文件
	 		var files = getCheckedFiles('ids',3);
	 		if(files.length==0){
	 			window.top.layer.alert("请确认文件是自己上传的！");
	 			return;
	 		}else{
	 			batchShare(sid,files);
	 			
	 		}
	 	 }
	 	 $("#"+div).html($(ts).html());
	 	 //sh(list);
	  }
	  /**
	   * 选中的文件夹
	   * @param ckBoxName
	   * @param type 
	   * @return
	   */
	  function getCheckedDirs(ckBoxName){
	 	 var dirs = new Array();
	 	 var ckBoxs = $(":checkbox[name='" + ckBoxName + "'][fileType='dir']");
	 	 if (ckBoxs != null) {
	 		 for ( var i = 0; i < ckBoxs.length; i++) {
	 			 if ($(ckBoxs[i]).attr('checked')) {
	 				 dirs.push($(ckBoxs[i]).val());
	 			 }
	 		 }
	 	 }
	  	 return dirs;
	  }
	  /**
	   * 选中的文件
	   * @param ckBoxName
	   * @param type 1移动 2删除 3分享
	   * @return
	   */
	  function getCheckedFiles(ckBoxName,type){
	 	 var files = new Array();
	 	 if(1==type){
	 		 var ckBoxs = $(":checkbox[name='" + ckBoxName + "'][fileType='file']");
	 		 if (ckBoxs != null) {
	 			 for ( var i = 0; i < ckBoxs.length; i++) {
	 				 if ($(ckBoxs[i]).attr('checked')) {
	 					 files.push($(ckBoxs[i]).val());
	 				 }
	 			 }
	 		 }
	 	 }else if(2==type){
	 		 var ckBoxs = $(":checkbox[name='" + ckBoxName + "'][fileType='file']");
	 		 if (ckBoxs != null) {
	 			 for ( var i = 0; i < ckBoxs.length; i++) {
	 				 if ($(ckBoxs[i]).attr('checked') && $(ckBoxs[i]).attr('owner')=='true') {
	 					 files.push($(ckBoxs[i]).val());
	 				 }else{
	 					 $(ckBoxs[i]).attr('checked',false);
	 				 }
	 			 }
	 		 }
	 	 }else if(3==type){
	 		 var ckBoxs = $(":checkbox[name='" + ckBoxName + "'][fileType='file']");
	 		 if (ckBoxs != null) {
	 			 for ( var i = 0; i < ckBoxs.length; i++) {
	 				 if ($(ckBoxs[i]).attr('checked') && $(ckBoxs[i]).attr('owner')=='true' ) {
	 					 files.push($(ckBoxs[i]).val());
	 				 }else{
	 					 $(ckBoxs[i]).attr('checked',false);
	 				 }
	 			 }
	 		 }
	 		$(":checkbox[name='" + ckBoxName + "'][fileType='dir']").attr('checked',false);
	 	 }
	 	 return files;
	  }
	  /**
	   * 批量移动
	   * @param sid
	   * @param dirs 文件夹主键
	   * @param files 文件主键
	   * @return
	   */
	  function batchMove(sid,dirs,files){
	 	 var preDirs = '0';
	 	 for(var i=0;i<dirs.length;i++){
	 		 preDirs += ','+dirs[i];
	 	 }
	 	 window.top.layer.open({
	 		 type: 2,
	 		  title: ['移动到', 'font-size:18px;'],
	 		  area: ['400px', '350px'],
	 		  fix: true, //不固定
	 		  maxmin: false,
	 		  move: false,
	 		  content: '/common/dirOnePage?sid=' + sid+'&t='+Math.random()+"&type=023",
	 		  btn: ['确定', '取消'],
	 		  yes: function(index, layero){
	 			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
	 			  var parentId = iframeWin.returnDir();
	 			  if(parentId){
	 				  window.top.layer.close(index)
	 				  $("#listFileForm").ajaxSubmit({
	 						type:"post",
	 						url:"/fileCenter/batchMove?sid="+sid+"&t="+Math.random(),
	 						data:{"dirs":dirs,"files":files,"classifyId":parentId},
	 						dataType: "json", 
	 						traditional :true,
	 						success:function(data){
	 							if(data.status=='y'){
	 								window.self.location.reload();
	 							}
	 						},
	 						error:function(XmlHttpRequest,textStatus,errorThrown){
	 							showNotification(2,"系统错误请联系管理人员！")
	 						}
	 					});
	 			  }
	 		  }
	 		  ,cancel: function(){ 
	 		    //右上角关闭回调
	 		  },success: function(layero,index){
	 			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
	 			  iframeWin.initData(preDirs,$("#classifyId").val());
	 		  }
	 	 });
	  }
	  
	  /**
	   * 批量共享
	   * @param sid
	   * @param files 文件主键
	   * @return
	   */
	  function batchShare(sid,files){
	 	 window.top.layer.open({
	 		 type: 2,
	 		  title: ['分享文件', 'font-size:18px;'],
	 		  area: ['400px', '350px'],
	 		  fix: true, //不固定
	 		  maxmin: false,
	 		  move: false,
	 		  content:'/common/shareGroupPage?sid=' + sid+'&id=0&busType=023&t='+Math.random(),
	 		  btn: ['确定', '取消'],
	 		  yes: function(index, layero){
	 			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
	 			  var result = iframeWin.returnScope();
	 			  if(result){
	 				  window.top.layer.close(index)
	 				  var scopetype = result.scopetype;
	 				  var scopeGroup = result.scopeGroup;
	 				  if(scopetype==0){
	 					  $("#listFileForm").ajaxSubmit({
	 							type:"post",
	 							url:"/fileCenter/batchShare?sid="+sid+"&t="+Math.random(),
	 							data:{"files":files,"type":scopetype,"scopeGroups":scopeGroup},
	 							dataType: "json", 
	 							 traditional :true,
	 							success:function(data){
	 								if(data.status=='y'){
	 									showNotification(1,"分享成功");
	 									$(":checkbox[name='ids'][disabled!='disabled']").attr('checked', false);
	 								}
	 							},
	 							error:function(XmlHttpRequest,textStatus,errorThrown){
	 							}
	 						});
	 				  }else{
	 					  $("#listFileForm").ajaxSubmit({
	 							type:"post",
	 							url:"/fileCenter/batchShare?sid="+sid+"&t="+Math.random(),
	 							data:{"files":files,"type":scopetype,"scopeGroups":null},
	 							dataType: "json", 
	 							traditional :true,
	 							success:function(data){
	 								if(data.status=='y'){
	 									showNotification(1,"分享成功");
	 									$(":checkbox[name='ids'][disabled!='disabled']").attr('checked', false);
	 								}
	 							},
	 							error:function(XmlHttpRequest,textStatus,errorThrown){
	 							}
	 						});
	 				  }
	 				  
	 				  
	 			  }
	 		  }
	 		  ,cancel: function(){ 
	 		  }
	 	 });
	  }
	  
	  /**
	   * 批量删除
	   * @param sid
	   * @param dirs 文件夹主键
	   * @param files 文件主键
	   * @return
	   */
	  function batchDel(sid,dirs,files){
	 	 if(dirs.length>0){
	 		 window.top.layer.open({
	 			 type:0,
	 			 content:'删除文件夹以及其下所有文件？',
	 			 btn: ['不留文件','保留文件','关闭']//按钮
	 			 ,icon:3
	 			 ,title:'询问框'
	 			 ,yes:function(index){
	 				 window.top.layer.close(index);
	 				 $("#listFileForm").ajaxSubmit({
	 	        		  type:"post",
	 	        		  url:"/fileCenter/batchDel?sid="+sid+"&t="+Math.random(),
	 	        		  data:{"dirs":dirs,"files":files,"parentId":$("#classifyId").val(),"isAll":"yes"},
	 	        		  dataType: "json",
	 	        		  traditional :true,
	 	        		  success:function(data){
	 	        			  if(data.status=='y'){
	 	        				  window.self.location.reload();
	 	        			  }
	 	        		  },
	 	        		  error:function(XmlHttpRequest,textStatus,errorThrown){
	 	        		  }
	 	        	  });
	 			 },btn2:function(index){
	 				 window.top.layer.close(index);
	 				 $("#listFileForm").ajaxSubmit({
	 	        		  type:"post",
	 	        		  url:"/fileCenter/batchDel?sid="+sid+"&t="+Math.random(),
	 	        		  data:{"dirs":dirs,"files":files,"parentId":$("#classifyId").val(),"isAll":"no"},
	 	        		  dataType: "json", 
	 	        		  traditional :true,
	 	        		  success:function(data){
	 	        			  if(data.status=='y'){
	 	        				  window.self.location.reload();
	 	        			  }
	 	        		  },
	 	        		  error:function(XmlHttpRequest,textStatus,errorThrown){
	 	        		  }
	 	        	  });
	 			 }
	 			 
	 		 
	 		 });
	 	 }else{
	 		 window.top.layer.open({
	 			 type:0,
	 			 content:'删除文件夹以及其下所有文件？',
	 			 btn: ['删除文件？','关闭']//按钮
	 			 ,icon:3
	 			 ,title:'询问框'
	 			 ,yes:function(index){
	 				 window.top.layer.close(index);
	 				 $("#listFileForm").ajaxSubmit({
	 				        type:"post",
	 				        url:"/fileCenter/batchDel?sid="+sid+"&t="+Math.random(),
	 				       data:{"dirs":dirs,"files":files,"parentId":$("#classifyId").val(),"isAll":"no"},
	 				        dataType: "json",
	 				       traditional :true,
	 				        success:function(data){
	 					        if(data.status=='y'){
	 					        	window.self.location.reload();
	 					        }
	 				        },
	 				        error:function(XmlHttpRequest,textStatus,errorThrown){
	 				        }
	 				    });
	 			 }
	 		 });
	 	 }
	 	 
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
	//样式设置
	  function setStyle(){
	  	$('.dirInfo').mouseover(function(){
	  			$(this).find(".infoDetail").hide();
	  			$(this).find(".opt").show();
	  	});
	  	$('.dirInfo').mouseout(function(){
	  			$(this).find(".infoDetail").show();
	  			$(this).find(".opt").hide();
	  	});
	  	$('.optspan').mouseover(function(){
	  		$(this).parent().find("span").css("background-color","#f0f0f0");
	  		$(this).css("background-color","gray");
	  	});
	  	$('.optspan').mouseout(function(){
	  		$(this).parent().find("span").css("background-color","#f0f0f0");
	  	});
	  	
	  	$('.fileInfoOne').mouseover(function(){
	  			$(this).find(".infoDetail").hide();
	  			$(this).find(".opt").show();
	  	});
	  	$('.fileInfoOne').mouseout(function(){
	  			$(this).find(".infoDetail").show();
	  			$(this).find(".opt").hide();
	  	});
	  	$('.optspan').mouseover(function(){
	  		$(this).parent().find("span").css("background-color","#f0f0f0");
	  		$(this).css("background-color","gray");
	  	});
	  	$('.optspan').mouseout(function(){
	  		$(this).parent().find("span").css("background-color","#f0f0f0");
	  	});
	  	
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
	 			//隐藏查询条件
	 			$(".searchCond").css("display","none");
	 			//显示批量操作
	 			$(".batchOpt").css("display","block");
	 			if(checkLen==len){
	 				$("#checkAllBox").attr('checked', true);
	 			}else{
	 				$("#checkAllBox").attr('checked', false);
	 			}
	 		}else{
	 			//显示查询条件
	 			$(".searchCond").css("display","block");
	 			//隐藏批量操作
	 			$(".batchOpt").css("display","none");
	 			
	 			$("#checkAllBox").attr('checked', false);
	 		}
	 	});
	 	
	  }
	  
	  /**
	   * 修改文件夹名称准备
	   * @param ts
	   * @param classifyId 当前文件夹主键
	   * @param sid
	   * @return
	   */
	   function modifyDirName(ts,classifyId,sid){
	   	var dirInfos = $(".dirInfo");
	   	for(var i=0;i<dirInfos.length;i++){
	   		var alen = $(dirInfos[i]).find(".dirName a").length;
	   		var inputlen = $(dirInfos[i]).find(".dirName input[type='text']").length;
	   		if(alen==0 && inputlen>0){
	   			var id = $(dirInfos[i]).find(".dirName input[name='classifyId']").val();
	   			var name = $(dirInfos[i]).find(".dirName input[name='tempDirName']").val();
	   			var html ='\n <img src="/static/images/folder_open.png" style="clear: both;border-radius: 0px;height: auto;width: auto;border:0px"/>';
	   			html += '\n	<input type="hidden" name="tempDirName" value="'+name+'"/>';
	   			html += '\n	<a href="javascript:void(0)" onclick="dirFile('+id+')">';
	   			html += '\n		'+name;
	   			html += '\n	</a>';
	   			$(dirInfos[i]).find(".dirName").html(html);
	   			
	   			var optHtml='\n<a href="javascript:void(0)" class="btn btn-info ws-btnBlue" style="width: 55px;height: 30px;" onclick="modifyDirName(this,'+id+',\''+sid+'\')">修改</a>';
	   			optHtml+='\n<a href="javascript:void(0)" class="btn btn-info ws-btnBlue" style="width: 55px;height: 30px" onclick="moveDir(this,'+id+',\''+sid+'\')">移动</a>';
	   			optHtml+='\n<a href="javascript:void(0)" class="btn btn-info ws-btnBlue" style="background-color: red;width: 55px;height: 30px" onclick="delDir(this,'+id+',\''+sid+'\')">删除</a>';
	   			$(dirInfos[i]).find(".opt").html(optHtml);
	   		}
	   	}
	   	var tempName = $(ts).parent().parent().find(".dirName input[name='tempDirName']").val();
	   	var html = '<div style="width:100%">';
	   	html += '<input type="hidden" value="'+classifyId+'" name="classifyId"/>';
	   	html += '<input type="hidden" value="'+tempName+'" name="tempDirName"/>';
	   	html += '<input class="form-control" style="width:90%;color:#000" type="text" value="'+tempName+'" onfocus="this.select();" name="dirName" onkeydown="updateDirNameByKey(this,\''+sid+'\')"/>';
	   	html += '</div>';
	   	$(ts).parent().parent().find(".dirName").html(html);
	   	$(ts).parent().parent().find(".dirName input[name='dirName']").focus();
	   	
	   	var optHtml = '<a class="btn btn-info ws-btnBlue" onclick="updateDirBySave(this,\''+sid+'\')">保存</a>';
	   	optHtml+='\n<a class="btn btn-default" onclick="cancleUpdateDirName(this,\''+sid+'\')">取消</a>';
	   	$(ts).parent().parent().find(".opt").html(optHtml);
	   	setStyle()
	   }
	   /**
	    * 取消修改文件夹名称
	    * @param ts
	    * @param sid
	    * @return
	    */
	   function cancleUpdateDirName(ts,sid){
	  	 
	  	var dirInfo =$(ts).parent().parent();
	  	var alen = $(dirInfo).find(".dirName a").length;
	  	
	  	var inputlen = $(dirInfo).find(".dirName input[type='text']").length;
	  	if(alen==0 && inputlen>0){
	  		var id = $(dirInfo).find(".dirName input[name='classifyId']").val();
	  		var name = $(dirInfo).find(".dirName input[name='tempDirName']").val();
	  		var html ='\n <img src="/static/images/folder_open.png" style="clear: both;border-radius: 0px;height: auto;width: auto;border:0px"/>';
	  		html += '\n	<input type="hidden" name="tempDirName" value="'+name+'"/>';
	  		html += '\n	<a href="javascript:void(0)" onclick="dirFile('+id+')">';
	  		html += '\n		'+name;
	  		html += '\n	</a>';
	  		$(dirInfo).find(".dirName").html(html);
	  		
	  		var optHtml='\n<a href="javascript:void(0)" class="btn btn-info ws-btnBlue" style="width: 55px;height: 30px;" onclick="modifyDirName(this,'+id+',\''+sid+'\')">修改</a>';
	  		optHtml+='\n<a href="javascript:void(0)" class="btn btn-info ws-btnBlue" style="width: 55px;height: 30px" onclick="moveDir(this,'+id+',\''+sid+'\')">移动</a>';
	  		optHtml+='\n<a href="javascript:void(0)" class="btn btn-info ws-btnBlue" style="background-color: red;width: 55px;height: 30px" onclick="delDir(this,'+id+',\''+sid+'\')">删除</a>';
	  		$(dirInfo).find(".opt").html(optHtml);
	  	}
	   }
	   
	  /**
	   * 回车修改文件夹
	   * @param ts
	   * @param sid
	   * @return
	   */
	   function updateDirNameByKey(ts,sid){
	     if(event.keyCode==13){
	  	   updateDir(ts,sid,0)
	     }
	   }
	   /**
	    * 保存修改文件夹
	    * @return
	    */
	   function updateDirBySave(ts,sid){
	  	 updateDir(ts,sid,1)
	   }
	   
	   /**
	    * 修改文件夹
	    * @param ts
	    * @param sid
	    * @param type 0回车 1 按钮
	    * @return
	    */
	   function updateDir(ts,sid,type){
	  		 var dirInfoA = $(ts).parent().parent();
	  		 var dirNameA = $(dirInfoA).find(".dirName input[name='dirName']");
	  		 if(1==type){
	  			 ts = dirNameA;
	  		 }
	  		var id = $(ts).parent().find("input[name='classifyId']").val();
	  		var tttt = $(ts).parent().parent().parent();
	  		
	  	 	var dirName = $(ts).val().replace(/\s+/g,"");
	  	 	var tempName = $(ts).parent().parent().parent().find(".dirName input[name='tempDirName']").val();
	  	 	if(tempName==dirName){
	  	 		 var html ='\n <img src="/static/images/folder_open.png" style="clear: both;border-radius: 0px;height: auto;width: auto;border:0px" />';
	  			 html += '\n	<input type="hidden" name="tempDirName" value="'+tempName+'"/>';
	  			 html += '\n	<a href="javascript:void(0)" onclick="dirFile('+id+')">';
	  			 html += '\n	'+tempName;
	  			 html += '\n	</a>';
	  			 $(ts).parent().parent().html(html);
	  			 
	  			 setStyle();
	  			 
	  			 var optHtml='\n<a href="javascript:void(0)" class="btn btn-info ws-btnBlue" style="width: 55px;height: 30px;" onclick="modifyDirName(this,'+id+',\''+sid+'\')">修改</a>';
	  			 optHtml+='\n<a href="javascript:void(0)" class="btn btn-info ws-btnBlue" style="width: 55px;height: 30px" onclick="moveDir(this,'+id+',\''+sid+'\')">移动</a>';
	  			 optHtml+='\n<a href="javascript:void(0)" class="btn btn-info ws-btnBlue" style="background-color: red;width: 55px;height: 30px" onclick="delDir(this,'+id+',\''+sid+'\')">删除</a>';
	  			 if(1==type){
	  				$(dirInfoA).find(".opt").html(optHtml);
	  			}else{
	  				$(tttt).find(".opt").html(optHtml);
	  			}
	  			 
	  	 		return;
	  	 	}
	  	 	if(dirName.length==0){
	  	 			layer.tips("文件夹没有名称",ts,{tips:1});
	  	 			$(".dirName").find("input").focus();
	  	 			return;
	  	 	}else{
	  	 		$.ajax({
	  				 type : "post",
	  				  url : "/fileCenter/updateDirName?sid="+sid+"&t="+Math.random(),
	  				  dataType:"json",
	  				  data:{"typeName":dirName,"id":id,"parentId":$("#classifyId").val()},
	  		         success : function(data){
	  					  var status = data.status;
	  		 		         if(status=='y'){
	  		 		        	showNotification(1,"修改成功");
	  		 		        	var html ='\n <img src="/static/images/folder_open.png" style="clear: both;border-radius: 0px;height: auto;width: auto;border:0px" />';
	  		 		        	html += '\n	<input type="hidden" name="tempDirName" value="'+dirName+'"/>';
	  		 		 			html += '\n	<a href="javascript:void(0)" onclick="dirFile('+id+')">';
	  		 		 			html += '\n		'+dirName;
	  		 		 			html += '\n	</a>';
	  		 		        	$(ts).parent().parent().html(html);
	  		 		         }else{
	  		 		        	showNotification(2,data.info);
	  		 			         var tempName = $(ts).parent().parent().parent().find(".dirName input[name='tempDirName']").val();
	  		 			         var html ='\n <img src="/static/images/folder_open.png" style="clear: both;border-radius: 0px;height: auto;width: auto;border:0px"/>';
	  		 			         html += '\n	<input type="hidden" name="tempDirName" value="'+tempName+'"/>';
	  		 			 		 html += '\n	<a href="javascript:void(0)" onclick="dirFile('+id+')">';
	  		 			 		 html += '\n	'+tempName;
	  		 			 		 html += '\n	</a>';
	  		 			 		$(ts).parent().parent().parent().html(html);
	  		 		         }
	  		 				setStyle();
	  		 				
	  		 				var optHtml='\n<a href="javascript:void(0)" class="btn btn-info ws-btnBlue" style="width: 55px;height: 30px;" onclick="modifyDirName(this,'+id+',\''+sid+'\')">修改</a>';
	  		 				optHtml+='\n<a href="javascript:void(0)" class="btn btn-info ws-btnBlue" style="width: 55px;height: 30px" onclick="moveDir(this,'+id+',\''+sid+'\')">移动</a>';
	  		 				optHtml+='\n<a href="javascript:void(0)" class="btn btn-info ws-btnBlue" style="background-color: red;width: 55px;height: 30px" onclick="delDir(this,'+id+',\''+sid+'\')">删除</a>';
	  		 				if(1==type){
	  		 					$(dirInfoA).find(".opt").html(optHtml);
	  		 				}else{
	  		 					$(tttt).find(".opt").html(optHtml);
	  		 				}
	  		         },
	  		         error:  function(XMLHttpRequest, textStatus, errorThrown){
	  		        	 showNotification(2,"系统错误，请联系管理人员！")
	  			     }
	  			});
	  	 		
	  	 	}
	  	 
	   }
	   
	  /**
	   * 删除文件夹
	   * @param ts
	   * @param id 文件夹主键
	   * @param sid
	   * @return
	   */
	   function delDir(ts,id,sid){
	  	 window.top.layer.open({
	  		 type:0,
	  		 content:'删除文件夹以及其下所有文件？',
	  		 btn: ['不留文件','保留文件','关闭']//按钮
	  		 ,icon:3
	  		 ,title:'询问框'
	  		 ,yes:function(index){
	  			 window.top.layer.close(index);
	  			 $.ajax({
	  			        type:"post",
	  			        url:"/fileCenter/delDir?sid="+sid+"&t="+Math.random(),
	  			        data:{"id":id,"parentId":$("#classifyId").val(),"isAll":"yes"},
	  			        dataType: "json", 
	  			        success:function(data){
	  				        if(data.status=='y'){
	  				        	window.location.reload()
	  				        }
	  			        },
	  			        error:function(XmlHttpRequest,textStatus,errorThrown){
	  			        }
	  			    });
	  		 },btn2:function(index){
	  			 window.top.layer.close(index);
	  			 $.ajax({
	  			        type:"post",
	  			        url:"/fileCenter/delDir?sid="+sid+"&t="+Math.random(),
	  			        data:{"id":id,"parentId":$("#classifyId").val(),"isAll":"no"},
	  			        dataType: "json", 
	  			        success:function(data){
	  				        if(data.status=='y'){
	  					        if(data.delNum==0){//没有移动子文件
	  					        	window.location.reload()
	  					        }else{//有子文件移动
	  					        	window.self.location.reload();
	  					        }
	  					    }
	  			        },
	  			        error:function(XmlHttpRequest,textStatus,errorThrown){
	  			        }
	  			    });
	  		 }
	  		 
	  	 
	  	 });
	   }
	   /**
	    * 移动文件夹
	    * @param ts
	    * @param id 文件夹主键
	    * @param sid
	    * @return
	    */
	   function moveDir(ts,id,sid){
	  	 
	  	 window.top.layer.open({
	  		 type: 2,
	  		  title: ['移动到', 'font-size:18px;'],
	  		  area: ['400px', '350px'],
	  		  fix: true, //不固定
	  		  maxmin: false,
	  		  move: false,
	  		  content: '/common/dirOnePage?sid=' + sid+'&t='+Math.random()+'&type=023',
	  		  btn: ['确定', '取消'],
	  		  yes: function(index, layero){
	  			  
	  			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
	  			  var parentId = iframeWin.returnDir();
	  			  if(parentId){
	  				  window.top.layer.close(index)
	  				  $("#listFileForm").ajaxSubmit({
	  						type:"post",
	  						url:"/fileCenter/moveDir?sid="+sid+"&t="+Math.random(),
	  						data:{"id":id,"parentId":parentId},
	  						dataType: "json", 
	  						success:function(data){
	  							if(data.status=='y'){
	  								window.location.reload()
	  							}
	  						},
	  						error:function(XmlHttpRequest,textStatus,errorThrown){
	  						}
	  					});
	  				  
	  			  }
	  		  }
	  		  ,cancel: function(){ 
	  		    //右上角关闭回调
	  		  },success: function(layero,index){
	  			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
	  			  iframeWin.initData(id,$("#classifyId").val());
	  		  }
	  	 });
	   }
	   /**
	    * 移动文件
	    * @param ts
	    * @param id 模块主键
	    * @param moduleType 业务类型
	    * @param fileId 文件主键
	    * @param userId 文件查看人主键
	    * @param fileDescribe 文件描述
	    * @param sid
	    * @return
	    */
	   function moveFile(ts,id,moduleType,fileId,userId,fileDescribe,sid){
	  	 window.top.layer.open({
	  		 type: 2,
	  		  title: ['移动到', 'font-size:18px;'],
	  		  area: ['400px', '350px'],
	  		  fix: true, //不固定
	  		  maxmin: false,
	  		  move: false,
	  		  content: '/common/dirOnePage?sid=' + sid+'&t='+Math.random()+'&type=023',
	  		  btn: ['确定', '取消'],
	  		  yes: function(index, layero){
	  			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
	  			  var parentId = iframeWin.returnDir();
	  			  if(parentId){
	  				  window.top.layer.close(index)
	  				  $("#listFileForm").ajaxSubmit({
	  						type:"post",
	  						url:"/fileCenter/moveFile?sid="+sid+"&t="+Math.random(),
	  						data:{"id":id,"classifyId":parentId,"moduleType":moduleType,"fileId":fileId,"userId":userId,"fileDescribe":fileDescribe},
	  						dataType: "json", 
	  						success:function(data){
	  							if(data.status=='y'){
	  								window.location.reload()
	  							}
	  						},
	  						error:function(XmlHttpRequest,textStatus,errorThrown){
	  						}
	  					});
	  				  
	  			  }
	  		  }
	  		  ,cancel: function(){ 
	  		    //右上角关闭回调
	  		  },success: function(layero,index){
	  			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
	  			  iframeWin.initData(0,$("#classifyId").val());
	  		  }
	  	 });
	  	 
	   }
	   /**
	    * 删除文件
	    * @param ts
	    * @param id 模块主键
	    * @param moduleType 业务类型
	    * @param fileId 文件主键
	    * @param userId 文件查看人主键
	    * @param sid
	    * @return
	    */
	   function delFile(ts,id,moduleType,fileId,userId,sid){
	  	 window.top.layer.confirm('删除文件？', {
	  		  btn: ['确定','取消']//按钮
	  	  ,title:'询问框'
	  	  ,icon:3
	  	},function(index){
	  		window.top.layer.close(index);
	  		$("#listFileForm").ajaxSubmit({
	  		        type:"post",
	  		        url:"/fileCenter/delFile?sid="+sid+"&t="+Math.random(),
	  		       data:{"id":id,"moduleType":moduleType,"fileId":fileId,"userId":userId},
	  		        dataType: "json", 
	  		        success:function(data){
	  			        if(data.status=='y'){
	  			        	$(ts).parent().parent().remove();
	  						var len = $(".fileInfoOne").length;
	  						if(len==0){
	  							var html= '\n <tr class="noInfo">';
	  							html+='\n	<td height="25" colspan="7" style="text-align: center;"><h3>没有文档！</h3></td>';
	  							html+='\n	</tr>';
	  							$(".listFiles").html(html);

	  							setStyle()
	  						}
	  			        }
	  		        },
	  		        error:function(XmlHttpRequest,textStatus,errorThrown){
	  		        	showNotification(2,"系统错误，请联系管理人员！");
	  		        }
	  		    });
	  	 });
	   }
	   /**
	    * 分享文件
	    * @param ts
	    * @param id 模块类型
	    * @param fileId 文件主键
	    * @param sid
	    * @return
	    */
	   function shareFile(ts,id,fileId,sid){
	  	 window.top.layer.open({
	  		 type: 2,
	  		  title: ['分享文件', 'font-size:18px;'],
	  		  area: ['400px', '350px'],
	  		  fix: true, //不固定
	  		  maxmin: false,
	  		  move: false,
	  		  content:'/common/shareGroupPage?sid=' + sid+'&id='+id+'&busType=023&t='+Math.random(),
	  		  btn: ['确定', '取消'],
	  		  yes: function(index, layero){
	  			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
	  			  var result = iframeWin.returnScope();
	  			  if(result){
	  				  var scopetype = result.scopetype;
	  				  var scopeGroup = result.scopeGroup;
	  				  
	  				  window.top.layer.close(index);
	  				  if(scopetype==0){
	  					  $("#listFileForm").ajaxSubmit({
	  							type:"post",
	  							url:"/fileCenter/shareFile?sid="+sid+"&t="+Math.random(),
	  							data:{"id":id,"fileId":fileId,"type":scopetype,"scopeGroups":scopeGroup},
	  							dataType: "json", 
	  							 traditional :true,
	  							success:function(data){
	  								if(data.status=='y'){
	  									showNotification(1,"分享成功");
	  								}
	  							},
	  							error:function(XmlHttpRequest,textStatus,errorThrown){
	  							}
	  						});
	  				  }else{
	  					  $("#listFileForm").ajaxSubmit({
	  							type:"post",
	  							url:"/fileCenter/shareFile?sid="+sid+"&t="+Math.random(),
	  							data:{"id":id,"fileId":fileId,"type":scopetype,"scopeGroups":null},
	  							dataType: "json", 
	  							 traditional :true,
	  							success:function(data){
	  								if(data.status=='y'){
	  									showNotification(1,"分享成功");
	  								}
	  							},
	  							error:function(XmlHttpRequest,textStatus,errorThrown){
	  							}
	  						});
	  				  }
	  				  
	  				  
	  			  }
	  		  }
	  		  ,cancel: function(){ 
	  		  }
	  	 });
	   }
	   
 //视频播放页面
   function viewVideoPage(uuid,sid,fileId){
	   	var url = "/onlineLearn/viewVideo?uuid="+uuid+"&sid="+sid;
	   	$.ajax({
		type:"post",
		url:url,
		dataType: "json", 
		success:function(data){
			videoId = fileId;
			var file = data.file;
			$(".list").hide();
			$(".palyerDiv").show();
			$("#contentBody").css("display","block");
			$(".controlbar-playtime").show();
			$(".controlbar-progress").css("width","80%")
			
			if(file.fileExt == "mp3" ||file.fileExt == "mp4" ){
				var playType = "mp4"
			}else{
				var playType = "flv"
			}
				 SewisePlayer.setup({
		            server: "vod",
		            type:"mp4",
		            skin: "vodFoream",
		            lang: 'zh_CN',
		            title:file.filename,
		            autoStart:"true",
		            videourl: data.url,
		        });
			},
			error:function(XmlHttpRequest,textStatus,errorThrown){
			}
	   	})
   }
   
 //上传新文件
   function updateFile(id,sid){
  	var url = "/onlineLearn/updateVideoFilePage?sid="+sid+"&id="+id+"&t="+Math.random();
  	layer.closeAll();
  	layer.open({
  	  type: 2,
  	  title: false,
  	  closeBtn: 0,
  	  shadeClose:false,
  	  shade: 0.3,
  	  shift:0,
  	  btn:['修改','取消'],
  	  scrollbar:false,
  	  fix: true, //固定
  	  maxmin: false,
  	  move: false,
  	  area: ['650px', '450px'],
  	  content: [url,'no'], //iframe的url
  	  yes:function(index,layero){
		  var iframeWin = window[layero.find('iframe')[0]['name']];
		  iframeWin.onSubmit();
  	  },
  	  success: function(layero,index){
  		  var iframeWin = window[layero.find('iframe')[0]['name']];
  	  },end:function(index){
  	  }
  	});
   }