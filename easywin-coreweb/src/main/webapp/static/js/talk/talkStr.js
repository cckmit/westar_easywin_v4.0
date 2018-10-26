/*
* 拼接讨论
 * @param talk
 * @param sid
 * @param sessionUser
 * @return
 */
function getTalkStr(talk,sid,sessionUser,busType,iframe){
	var html = '';
	if(talk.parentId==-1){
		html+='\n<div id="talk'+talk.id+'" class="ws-shareBox talkInfoP">';
	}else{
		html+='\n<div id="talk'+talk.id+'" class="ws-shareBox ws-shareBox2 talkInfo">';
	}
	html+='\n		<div class="shareHead" data-container="body" data-toggle="popover" data-user='+talk.talker+'  data-busId=0 data-busType="'+busType+'">';
	//头像信息
	html += '		<img src="/downLoad/userImg/'+talk.comId+'/'+talk.talker+'?sid='+sid+'" title="'+talk.talkerName+'"></img>\n';
	
	html+='\n 		</div>';
	html+='\n		<div class="shareText">';
	html+='\n			<span class="ws-blue">'+talk.talkerName+'</span>';
	if(talk.parentId>-1){
		html+='\n		<r>回复</r>';
		html+='\n		<span class="ws-blue">'+talk.ptalkerName+'</span>';
	}
	html+='\n			<p class="ws-texts">'+talk.content+'</p>';
	//附件
	if(talk.listTalkUpfile.length>0){
		html+='\n		<div class="file_div">';
		for(var i=0;i<talk.listTalkUpfile.length;i++){
			var upfiles = talk.listTalkUpfile[i];
			html+='\n		<p class="p_text">附件('+(i+1)+')：';
			
			if(upfiles.isPic==1){
				html+='\n			<img onload="AutoResizeImage(250,0,this,\''+iframe+'\')"';
				html+='\n			src="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'"  style="width: 100%;height: 100%;"/>';
				html+='\n			&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
				html+='\n			&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic(\'/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'\',\''+sid+'\',\''+upfiles.upfileId+'\',\''+busType+'\',\''+talk.busId+'\')"></a>';
			}else{
				html += '			'+upfiles.filename+'\n';
				var fileExt = upfiles.fileExt;
				if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx'){
					html+='\n		&nbsp;&nbsp;<a class="fa fa-download" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+sid+'\')"></a>';
					html+='\n		&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\''+busType+'\',\''+talk.busId+'\')"></a>';
				}else if(fileExt=='txt' ||fileExt=='pdf'){
					html+='\n		&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
					html+='\n		&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\''+busType+'\',\''+talk.busId+'\')"></a>';
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
	html+='\n 					<a href="javascript:void(0)" id="delTalk_'+talk.id+'" onclick="delTalk('+talk.id+',1)" class="fa fa-trash-o" title="删除"></a>';
	html+='\n 					<a href="javascript:void(0)" id="img_'+talk.id+'" onclick="showArea(\'addTalk'+talk.id+'\',\''+iframe+'\')" class="fa fa-comment-o" title="回复"></a>';
	html+='\n				</span>';
	html+='\n				<span>';
	html+='\n 					<time>'+talk.recordCreateTime+'</time>';
	html+='\n				</span>';
	html+='\n			</div>';
	html+='\n		</div>';
	html+='\n		<div class="ws-clear"></div>';
	html+='\n	</div>';
	
	//回复层
	html+='\n 	<div id="addTalk'+talk.id+'" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk">';
	html+='\n 		<div class="shareText">';
	html+='\n 			<div class="ws-textareaBox" style="margin-top:10px;">';
	html+='\n 				<textarea id="operaterReplyTextarea_'+talk.id+'" name="operaterReplyTextarea_'+talk.id+'" style="height: 55px" class="form-control txttalk" placeholder="回复……"></textarea>';
	html+='\n				<div class="ws-otherBox" style="position: relative;">';
	html+='\n					<div class="ws-meh">';
	//表情
	html+='\n 						<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_'+talk.id+'" onclick="addBiaoQingObj(\'biaoQingSwitch_'+talk.id+'\',\'biaoQingDiv_'+talk.id+'\',\'operaterReplyTextarea_'+talk.id+'\');"></a>';
	html+= '\n				     	<div id="biaoQingDiv_'+talk.id+'" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 15px">';
	html+= '\n				     		<div class="main">';
	html+= '\n				           		<ul style="padding: 0px">';
	html+= ' \n'+getBiaoqing();
	html+= '\n				           		</ul>';
	html+= '\n				       	 	</div>';
	html+= '\n				     	</div>';
	html+= '\n				  	</div>';
	//常用意见 
	html+='\n 					<div class="ws-plugs">';
	html+='\n 						<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea(\'operaterReplyTextarea_'+talk.id+'\',\''+sid+'\');" title="常用意见"></a>';
	html+= '\n				 	 </div>';
	//分享按钮
	html+='\n					<div class="ws-share">';
	html+='\n						<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('+talk.busId+','+talk.id+',this,'+talk.talker+')">回复</button>';
	html+='\n					</div>';
	//相关附件 
	html+='\n					<div style="clear: both;"></div>';
	html+='\n					<div class="ws-notice">';
	
	html+='\n 						<div style="clear:both;width: 300px" class="wu-example">';
	html+='\n 						<div id="thelistlistUpfiles_'+talk.id+'_upfileId" class="uploader-list"></div>';
	html+='\n 						<div class="btns btn-sm">';
	html+='\n 							<div id="pickerlistUpfiles_'+talk.id+'_upfileId">选择文件</div>';
	html+=' \n						</div>';
	html+='\n 						<script type="text/javascript">';
	html+='\n 							loadWebUpfiles(\'listUpfiles_'+talk.id+'_upfileId\', \''+sid+'\', \''+iframe+'\', \'pickerlistUpfiles_'+talk.id+'_upfileId\', \'thelistlistUpfiles_'+talk.id+'_upfileId\', \'filelistUpfiles_'+talk.id+'_upfileId\' )';
	html+='\n 						</script>';
	html+='\n 						<div style="position: relative; width: 350px; height: 90px;display: none">';
	html+='\n 						<div style="float: left; width: 250px">';
	html+='\n 								<select list="listUpfiles_'+talk.id+'" listkey="upfileId" listvalue="filename" id="listUpfiles_'+talk.id+'_upfileId" name="listUpfiles_'+talk.id+'.upfileId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">';
	html+='\n 								</select>';
	html+='\n 							</div>';
	html+=' \n						</div>';
	html+='\n 					</div>';
	html+='\n					</div>';
	html+='\n				</div>';
	html+='\n			</div>';
	html+='\n		</div>';
	html+='\n	</div>';
	//针对回复的回复 结束
	return html;
}

/**
 * 显示回复的回复
 */
function showArea(priId,iframe){
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

	if(iframe){
		 resizeVoteH(iframe);
	}
}