$(function(){
	$("#clearPic").click(function(){
		$("#showpic0").html('');
		$("#clearPic").css("display","none")
		if($("#id").attr("id") && $("#id").val()>0){
			$.post("/meetingRoom/updateRoomPicId?sid="+$("#sid").val(),{Action:"post",id:$("#id").val(),roomPicId:0},     
 				function (msgObjs){
				showNotification(1,msgObjs.info);
			},"json").error(function() {showNotification(2,"系统错误,请联系管理人员")});
		}
	});
})


/**
 * 上传头像
 * @param sid
 * @param filePicker
 * @param fileShowList
 * @param fileVal
 * @param tempDir
 * @param meetingId
 * @return
 */
function loadUpfilesForMeetingRoom(sid,filePicker,fileShowList,fileVal,tempDir,meetingRoomId){
	//上传文件的URL
	var backEndUrl = '/upload/addDepartImgFile?sid='+sid+'&t='+Math.random();
    //各个不同的文件的MD5,用于上传
    var destMD5Dir = {md5:"",fileVal:fileVal,tempDir:tempDir,meetingRoomId:meetingRoomId};   //附件MD5信息
	WebUploader.Uploader.register({//注册插件
        "before-send-file": "beforeSendFile"
    },{
	    beforeSendFile: function(file){
    	loadImgIndex = window.top.layer.load(0);
	        //秒传验证
	        var task = new $.Deferred();
	        (new WebUploader.Uploader()).md5File(file).progress(function(percentage){
	        }).then(function(val){
	           //用于上传
	            destMD5Dir.md5 = val;
	            task.resolve();
	        });
	        return $.when(task);
	    }
	});
	var uploader = WebUploader.create({
	    swf: '/static/plugins/webuploader/Uploader.swf?t='+Math.random(),
	    server: backEndUrl,
	    pick: {id:'#'+filePicker,multiple: false},
	    duplicate: false,//不能选择重复的文件
	    resize: false,
	    auto: false,
	    fileNumLimit: null,
	    fileVal:fileVal,   //指明参数名称，后台也用这个参数接收文件
	    fileSingleSizeLimit: 200*1024*1024,
	    //fileType:'rar,zip,doc,xls,docx,xlsx,pdf'
	    accept: {
	    	title: 'Images',
    	    extensions: 'gif,jpg,jpeg,bmp,png',
    	    mimeTypes: 'image/*'
	    },
	    chunked:true,
	    chunkSize:5*1024*1024,
	    thread:2,
	    formData:function(){return $.extend(true, {}, destMD5Dir);}
	});
	uploader.on("error",function(type,reason){
    	if (type=="Q_TYPE_DENIED"){
    		window.top.layer.alert(reason.ext+"格式不支持")
    	}else if(type=="Q_EXCEED_NUM_LIMIT"){
    		window.top.layer.alert('上传的文件数量已经超出系统限制的'+reason+"个");
    	}else if(type=="F_EXCEED_SIZE"){
    		window.top.layer.alert('文件太大不支持');
    	}
	});
    uploader.on('fileQueued', function( file ) {  // webuploader事件.当选择文件后，文件被加载到文件队列中，触发该事件。等效于 uploader.onFileueued = function(file){...} ，类似js的事件定义。
	  uploader.upload(file);
   });
	 uploader.on('uploadSuccess', function (file, data) {
	      var chunksTotal = 0;
            if((chunksTotal = Math.ceil(file.size/(5*1024*1024))) >= 1){
                //合并请求
                $.ajax({
                    type: "POST"
                    , url: backEndUrl
                    , data: {
                        status: "chunksMerge"
                        , name: file.name
                        , chunks: chunksTotal
                        , ext: file.ext
                        , md5: data.fileMd5
                        , size:file.size
                        ,tempDir:tempDir
                        ,meetingRoomId:meetingRoomId
                    }
                    , cache: false
                    , dataType: "json"
                }).then(function(rs, textStatus, jqXHR){
	   				 if(rs.status=='y'){
	   					 var fileName = rs.fileName;
	   					 //原图片
	   					 var middle =rs.orgImgPath;
	   					 if(rs.orgImgPath.indexOf("?")>0){
	   						 middle = middle+"&t="+Math.random();
	   					 }else{
	   						 middle = middle+"?t="+Math.random();
	   					 }
	   					 var imgInfo = "<span><img src='"+middle+"' onload=\"AutoResizeImage(430,0,this,'')\"/></span>";
	   					 imgInfo+='\n<input type="hidden" name="filePathP" value="'+rs.orgImgPath+'">';
	   					 imgInfo+='\n<input type="hidden" name="fileNameP" value="'+rs.fileName+'">';
	   					 $("#"+fileShowList).html(imgInfo);
	   					 $("#clearPic").css("display","block");
	   					 if(meetingRoomId>0){
	   						 showNotification(1,"修改成功");
	   					 }
			         }
	   				 window.top.layer.close(loadImgIndex);
                	uploader.removeFile(file, true);
                	
                }, function(jqXHR, textStatus, errorThrown){
                	window.top.layer.close(loadImgIndex);
                	uploader.removeFile(file, true);
                	
                });
            }
	      
    });
    uploader.on('uploadError', function (file,reason) {
    	 window.top.layer.close(loadImgIndex);
    	 uploader.removeFile(file, true);
	});
    
    //修复model内部点击不会触发选择文件的BUG
    $("#"+filePicker+" .webuploader-pick").click(function () {
        $("#"+filePicker+" :file").click();
    });
}


/**
 * 显示回复的回复
 */
function showArea(priId){
	var addtalks = $(".addTalk");
	$(".fa-comment").attr("title","回复");
	$(".fa-comment").attr("class","fa fa-comment-o");

	for(var i=0;i<addtalks.length;i++){
		var talkId = $(addtalks[i]).attr("id");
		var imgId = "img_"+talkId.replace("addTalk","");
		if(talkId==priId){
			 if($("#"+priId).css('display')=="none"){
		     	$("#"+imgId).attr("title","隐藏");
		     	$("#"+imgId).attr("class","fa fa-comment");
			 	$("#"+priId).show();
		     }else{
		     	$("#"+priId).hide();
		     	$("#"+imgId).attr("title","回复");
		     	$("#"+imgId).attr("class","fa fa-comment-o");
		     }
		}else{
			$("#"+talkId).hide();
	     	$("#"+imgId).attr("title","回复");
	     	$("#"+imgId).attr("class","fa fa-comment-o");
		}
	}
	 resizeVoteH('otherAttrIframe')
}
/**
 * 投票详情查看页面拼接代码
 * @param meetTalk
 * @param sid
 * @param userInfo
 * @return
 */
function getMeetTalkStr(meetTalk,sid,userInfo){
	var html = '';
	if(meetTalk.parentId==-1){
		html+='\n<div id="talk'+meetTalk.id+'" class="ws-shareBox meetTalkInfoP">';
	}else{
		html+='\n<div id="talk'+meetTalk.id+'" class="ws-shareBox ws-shareBox2 meetTalkInfo">';
	}
	html+='\n		<div class="shareHead" data-container="body" data-toggle="popover" data-user='+meetTalk.talker+' data-busId=0 data-busType=\'017\'>';
	//头像信息
	html += '		<img src="/downLoad/userImg/'+meetTalk.comId+'/'+meetTalk.talker+'?sid='+sid+'" title="'+meetTalk.talkerName+'"></img>\n';
	
	html+='\n 		</div>';
	html+='\n		<div class="shareText">';
	html+='\n			<span class="ws-blue">'+meetTalk.talkerName+'</span>';
	if(meetTalk.parentId>-1){
		html+='\n		<r>回复</r>';
		html+='\n		<span class="ws-blue">'+meetTalk.ptalkerName+'</span>';
	}
	html+='\n			<p class="ws-texts">'+meetTalk.content+'</p>';
	//附件
	if(meetTalk.listMeetTalkFile.length>0){
		html+='\n		<div class="file_div">';
		for(var i=0;i<meetTalk.listMeetTalkFile.length;i++){
			var upfiles = meetTalk.listMeetTalkFile[i];
			html+='\n		<p class="p_text">附件('+(i+1)+')：';
			
			if(upfiles.isPic==1){
				html+='\n			<img onload="AutoResizeImage(350,0,this,\'otherAttrIframe\')"';
				html+='\n			src="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" />';
				html+='\n			&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
				html+='\n			&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic(\'/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'\',\''+sid+'\',\''+upfiles.upfileId+'\',\'017\',\''+meetTalk.meetingId+'\')"></a>';
			}else{
				html += '			'+upfiles.filename+'\n';
				var fileExt = upfiles.fileExt;
				if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx' ){
					html+='\n		&nbsp;&nbsp;<a class="fa fa-download" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+sid+'\')"></a>';
					html+='\n		&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'017\',\''+meetTalk.meetingId+'\')"></a>';
				}else if(fileExt=='txt' ||fileExt=='pdf'){
					html+='\n		&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
					html+='\n		&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'017\',\''+meetTalk.meetingId+'\')"></a>';
				}else{
					html+='\n	&nbsp;&nbsp;<a class="fa fa-download" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+sid+'\')"></a>';
				}
			}
			html+='\n		</p>';
		}
		html+='\n		</div>';
	}
	
	
	html+='\n			<div class="ws-type">';
	//发言人可以删除自己的发言
	html+='\n				<span class="hideOpt" style="display:block">';
	html+='\n 					<a href="javascript:void(0)" id="delMeetTalk_'+meetTalk.id+'" onclick="delMeetTalk('+meetTalk.id+',1)" class="fa fa-trash-o" title="删除"></a>';
	html+='\n 					<a href="javascript:void(0)" id="img_'+meetTalk.id+'" onclick="showArea(\'addTalk'+meetTalk.id+'\')" class="fa fa-comment-o" title="回复"></a>';
	html+='\n				</span>';
	html+='\n				<span>';
	html+='\n 					<time>'+meetTalk.recordCreateTime+'</time>';
	html+='\n				</span>';
	html+='\n			</div>';
	html+='\n		</div>';
	html+='\n		<div class="ws-clear"></div>';
	html+='\n	</div>';
	
	//回复层
	html+='\n 	<div id="addTalk'+meetTalk.id+'" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk">';
	html+='\n 		<div class="shareText">';
	html+='\n 			<div class="ws-textareaBox" style="margin-top:10px;">';
	html+='\n 				<textarea id="operaterReplyTextarea_'+meetTalk.id+'" name="operaterReplyTextarea_'+meetTalk.id+'" style="height: 55px" class="form-control txtmeetTalk" placeholder="回复……"></textarea>';
	html+='\n				<div class="ws-otherBox" style="position: relative;">';
	html+='\n					<div class="ws-meh">';
	//表情
	html+='\n 						<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_'+meetTalk.id+'" onclick="addBiaoQingObj(\'biaoQingSwitch_'+meetTalk.id+'\',\'biaoQingDiv_'+meetTalk.id+'\',\'operaterReplyTextarea_'+meetTalk.id+'\');"></a>';
	html+= '\n				     	<div id="biaoQingDiv_'+meetTalk.id+'" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 15px">';
	html+= '\n				     		<div class="main">';
	html+= '\n				           		<ul style="padding: 0px">';
	html+= ' \n'+getBiaoqing();
	html+= '\n				           		</ul>';
	html+= '\n				       	 	</div>';
	html+= '\n				     	</div>';
	html+= '\n				  	</div>';
	//常用意见 
	html+='\n 					<div class="ws-plugs">';
	html+='\n 						<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea(\'operaterReplyTextarea_'+meetTalk.id+'\',\''+sid+'\');" title="常用意见"></a>';
	html+= '\n				 	 </div>';
	//@
	html+='\n 					<div class="ws-plugs">';
	html+='\n 						<a class="btn-icon" href="javascript:void(0)" title="学习人员" data-todoUser="yes" data-relateDiv="todoUserDiv_'+meetTalk.id+'">@</a>';
	html+= '\n				 	 </div>';
	//提醒方式
/*	html+='\n					<div class="ws-remind">';
	html+='\n						<span class="ws-remindTex">提醒方式：</span>';
	html+='\n						<div class="ws-checkbox">';
	html+='\n							<label class="checkbox-inline">';
	html+='\n								<input id="inlineCheckbox1" type="checkbox" value="option1"/>短信</label>';
	html+='\n							<label class="checkbox-inline">';
	html+='\n								<input id="inlineCheckbox1" type="checkbox" value="option1"/>邮件</label>';
	html+='\n							<label class="checkbox-inline">';
	html+='\n								<input id="inlineCheckbox1" type="checkbox" value="option1"/>桌面推送</label>';
	html+='\n						</div>';
	html+='\n					</div>';*/
	//分享按钮
	html+='\n					<div class="ws-share">';
	html+='\n						<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('+meetTalk.meetingId+','+meetTalk.id+',this,'+meetTalk.talker+')" data-relateTodoDiv="todoUserDiv_'+meetTalk.id+'">回复</button>';
	html+='\n					</div>';
	//相关附件 
	html+='\n					<div style="clear: both;"></div>';
	html+='\n					<div id="todoUserDiv_'+meetTalk.id+'" class="padding-top-10">';
	html+='\n					</div>';
	html+='\n					<div class="ws-notice">';
	
	html+= '\n 						<div style="clear:both;width: 300px" class="wu-example">';
	//用来存放文件信息
	html+= '\n 							<div id="thelistlistUpfiles_'+meetTalk.id+'_upfileId" class="uploader-list" ></div>';
	html+= '\n 						 	<div class="btns btn-sm">';
	html+= '\n 								<div id="pickerlistUpfiles_'+meetTalk.id+'_upfileId">选择文件</div>';
	html+= '\n 							</div>';
	
	html+= '\n							<script type="text/javascript">\n';
	html+= '\n 								loadWebUpfiles(\'listUpfiles_'+meetTalk.id+'_upfileId\',\''+sid+'\',\'otherAttrIframe\',\'pickerlistUpfiles_'+meetTalk.id+'_upfileId\',\'thelistlistUpfiles_'+meetTalk.id+'_upfileId\',\'filelistUpfiles_'+meetTalk.id+'_upfileId\');';
	html+= '\n 							</script>\n';
	
	html+= '\n 							<div style="position: relative; width: 350px; height: 90px;display: none">';
	html+= '\n 								<div style="float: left; width: 250px">';
	html+= '\n 									<select  list="listUpfiles_'+meetTalk.id+'" listkey="upfileId" listvalue="filename" id="listUpfiles_'+meetTalk.id+'_upfileId" name="listUpfiles_'+meetTalk.id+'.upfileId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">';
	html+= '\n 							 		</select>';
	html+= '\n 								</div>';
	html+= '\n 							</div>';
	html+= '\n 						</div>';
	
	html+='\n					</div>';
	html+='\n				</div>';
	html+='\n			</div>';
	html+='\n		</div>';
	html+='\n	</div>';
	//针对回复的回复 结束
	return html;
}
