function getMagShareTalkString(msgTalk,sid,sessionUser){
	var html = '';
	/*if(msgTalk.parentId==-1){
		html+='\n <div id="talk_'+msgTalk.id+'" class="ws-shareBox ">';
	}else{
		html+='\n <div id="talk_'+msgTalk.id+'" class="ws-shareBox ws-shareBox2">';
		
	}*/
	html+='\n <div id="talk_'+msgTalk.id+'" class="ws-shareBox ">';
	html+='\n 		<div class="shareHead" data-container="body" data-toggle="popover"  data-user='+msgTalk.speaker+' data-busId="0" data-busType="1">';
	html += '		<img src="/downLoad/userImg/'+msgTalk.comId+'/'+msgTalk.talker+'?sid='+sid+'" title="'+msgTalk.talkerName+'"></img>\n';

	html+='\n 		</div>';
	html+='\n 		<div class="shareText">';
	html+='\n 			<span class="ws-blue">'+msgTalk.talkerName+'</span>';
	if(msgTalk.parentId>-1){
		
		html+='\n 		<r>回复</r>';
		html+='\n 		<span class="ws-blue">'+msgTalk.ptalkerName+'</span>';
	}
	html+='\n			<p class="ws-texts">'+msgTalk.content+'</p>';
	//附件
	var files = msgTalk.msgShareTalkUpfiles;
	if(files.length>0){
		html+='\n		<div class="file_div">';
		for(var i=0;i<files.length;i++){
			var upfiles = files[i];
			var upfiles = files[i];
			if(upfiles.isPic==1){
				html+='\n	<p class="p_text">附件('+(i+1)+')：';
				html+='\n		<img onload="AutoResizeImage(350,0,this,\'otherIframe\')"';
				html+='\n			src="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" style="width: 100%;height: 100%;"/>';
				html+='\n			&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
				html+='\n			&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="showPic(\'/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'\',\''+sid+'\',\''+upfiles.upfileId+'\',\'1\',\''+msgTalk.msgId+'\')" title="预览"></a>';
				html+='\n	</p>';
			}else{
				html+='\n	<p class="p_text">附件('+(i+1)+')：';
				html+='\n'+upfiles.filename;
				var fileExt = upfiles.fileExt;
				if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx'){
					html+='\n	&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0)" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+sid+'\')" title="下载"></a>';
					html+='\n	&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'1\',\''+msgTalk.msgId+'\')" title="预览"></a>';
				}else if(fileExt=='txt' ||fileExt=='pdf'){
					html+='\n	&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
					html+='\n	&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'1\',\''+msgTalk.msgId+'\')" title="预览"></a>';
				}else{
					html+='\n	<a class="fa fa-download" href="javascript:void(0)" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+sid+'\')" title="下载"></a>';
				}
				html+='\n	</p>';
			}
		}
		html+='\n		</div>';
	}
	html+='\n 			<div class="ws-type">';
	//发言人可以删除自己的发言
	html+='\n 				<a href="javascript:void(0);" id="delMsgTalk_'+msgTalk.id+'" onclick="delMsgTalk('+msgTalk.id+',1)"  class="fa fa-trash-o" title="删除"></a>';
	html+='\n 				<a id="img_'+msgTalk.id+'" name="replyImg" href="javascript:void(0);" class="fa fa-comment-o" title="回复" onclick="showArea(\'addTalk'+msgTalk.id+'\')"></a>';
	html+='\n 				<time>'+msgTalk.recordCreateTime+'</time>';
	html+='\n 			</div>';
	html+='\n 		</div>';
	html+='\n		<div class="ws-clear"></div>';
	html+='\n 	</div>';
	// 回复层
	html+='\n 	<div id="addTalk'+msgTalk.id+'" name="replyTalk" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk">';
	html+='\n 		<div class="shareText">';
	html+='\n 			<div class="ws-textareaBox" style="margin-top:10px;">';
	html+='\n				 <textarea id="operaterReplyTextarea_'+msgTalk.id+'" name="operaterReplyTextarea_'+msgTalk.id+'" rows="" cols="" class="form-control txtTalk" placeholder="回复……"></textarea>';
	html+='\n 				<div class="ws-otherBox">';
	html+='\n 					<div class="ws-meh" style="position: relative;">';
	//表情
	html+='\n 						<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_'+msgTalk.id+'" onclick="addBiaoQingObj(\'biaoQingSwitch_'+msgTalk.id+'\',\'biaoQingDiv_'+msgTalk.id+'\',\'operaterReplyTextarea_'+msgTalk.id+'\');"></a>';
	html+='\n 						<div id="biaoQingDiv_'+msgTalk.id+'" class="blk" style="display:none;position:absolute;width:200px;top:0px;z-index:99;left: 20px">';
	//表情DIV层
	html += '\n				     	<div class="main">';
	html += '\n				           	<ul style="padding: 0px">';
	html += ' \n'+getBiaoqing();
	html += '\n				           	</ul>';
	html += '\n				       	 </div>';
	html += '\n				    </div>';
	html += '\n				 </div>';
	//常用意见
	html+='\n				<div class="ws-plugs">';
	html+='\n					<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea(\'operaterReplyTextarea_'+msgTalk.id+'\',\''+sid+'\');" title="常用意见"></a>';
	html+='\n				</div>';
	html+='\n				<div class="ws-plugs">';
	html+='\n					<a class="btn-icon" href="javascript:void(0)" data-todoUser="yes" title="告知人员" data-relateDiv="todoUserDiv_'+msgTalk.id+'">@</a>';
	html+='\n				</div>';
	//提醒方式
	/*html+='\n				<div class="ws-remind">';
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
	
	html+='\n 				<div class="ws-share">';
	html+='\n					 <button type="button" class="btn btn-info ws-btnBlue" data-relateTodoDiv="todoUserDiv_'+msgTalk.id+'" onclick="addTalk('+msgTalk.msgId+','+msgTalk.id+',this,'+msgTalk.speaker+')"">回复</button>';
	html+='\n 				</div>';
	//相关附件
	html+='\n 				<div style="clear: both;"></div>';
	html+='\n 				<div id="todoUserDiv_'+msgTalk.id+'" class="padding-top-10"></div>';
	html+='\n 				<div class="ws-notice">';
	
	html+= '\n 					<div style="clear:both;width: 300px" class="wu-example">';
	//用来存放文件信息
	html+= '\n 						<div id="thelistlistUpfiles_'+msgTalk.id+'_upfileId" class="uploader-list" ></div>';
	html+= '\n 						 <div class="btns btn-sm">';
	html+= '\n 							<div id="pickerlistUpfiles_'+msgTalk.id+'_upfileId">选择文件</div>';
	html+= '\n 						</div>';
	
	html+= '\n						<script type="text/javascript">\n';
	html+= '\n 							loadWebUpfiles(\'listUpfiles_'+msgTalk.id+'_upfileId\',\''+sid+'\',\'otherIframe\',\'pickerlistUpfiles_'+msgTalk.id+'_upfileId\',\'thelistlistUpfiles_'+msgTalk.id+'_upfileId\',\'filelistUpfiles_'+msgTalk.id+'_upfileId\');';
	html+= '\n 						</script>\n';
	
	html+= '\n 						<div style="position: relative; width: 350px; height: 90px;display: none">';
	html+= '\n 							<div style="float: left; width: 250px">';
	html+= '\n 								<select  list="listUpfiles_'+msgTalk.id+'" listkey="upfileId" listvalue="filename" id="listUpfiles_'+msgTalk.id+'_upfileId" name="listUpfiles_'+msgTalk.id+'.upfileId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">';
	html+= '\n 							 	</select>';
	html+= '\n 							</div>';
	html+= '\n 						</div>';
	html+= '\n 					</div>';
		
	html+='\n 				</div>';
	
	html+='\n			 </div>';
	html+='\n 		</div>';
	html+='\n 	</div>';
	html+='\n </div>';
	return html;
}

//在线学习讨论
function getVideoTalkString(msgTalk,sid,sessionUser){
	var html = '';
	/*if(msgTalk.parentId==-1){
		html+='\n <div id="talk_'+msgTalk.id+'" class="ws-shareBox ">';
	}else{
		html+='\n <div id="talk_'+msgTalk.id+'" class="ws-shareBox ws-shareBox2">';
		
	}*/
	html+='\n <div id="talk_'+msgTalk.id+'" class="ws-shareBox ">';
	html+='\n 		<div class="shareHead" data-container="body" data-toggle="popover"  data-user='+msgTalk.speaker+' data-busId="0" data-busType="1">';
	html += '		<img src="/downLoad/userImg/'+msgTalk.comId+'/'+msgTalk.talker+'?sid='+sid+'" title="'+msgTalk.talkerName+'"></img>\n';

	html+='\n 		</div>';
	html+='\n 		<div class="shareText">';
	html+='\n 			<span class="ws-blue">'+msgTalk.talkerName+'</span>';
	if(msgTalk.parentId>-1){
		
		html+='\n 		<r>回复</r>';
		html+='\n 		<span class="ws-blue">'+msgTalk.ptalkerName+'</span>';
	}
	html+='\n			<p class="ws-texts">'+msgTalk.content+'</p>';
	//附件
	var files = msgTalk.videoTalkUpfiles;
	if(files != null && files.length>0){
		html+='\n		<div class="file_div">';
		for(var i=0;i<files.length;i++){
			var upfiles = files[i];
			var upfiles = files[i];
			if(upfiles.isPic==1){
				html+='\n	<p class="p_text">附件('+(i+1)+')：';
				html+='\n		<img onload="AutoResizeImage(350,0,this,\'otherIframe\')"';
				html+='\n			src="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" style="width: 100%;height: 100%;"/>';
				html+='\n			&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
				html+='\n			&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="showPic(\'/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'\',\''+sid+'\',\''+upfiles.upfileId+'\',\'1\',\''+msgTalk.fileId+'\')" title="预览"></a>';
				html+='\n	</p>';
			}else{
				html+='\n	<p class="p_text">附件('+(i+1)+')：';
				html+='\n'+upfiles.filename;
				var fileExt = upfiles.fileExt;
				if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx'){
					html+='\n	&nbsp;&nbsp;<a class="fa fa-download" href="javascript:void(0)" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+sid+'\')" title="下载"></a>';
					html+='\n	&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'1\',\''+msgTalk.fileId+'\')" title="预览"></a>';
				}else if(fileExt=='txt' ||fileExt=='pdf'){
					html+='\n	&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
					html+='\n	&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'1\',\''+msgTalk.fileId+'\')" title="预览"></a>';
				}else{
					html+='\n	<a class="fa fa-download" href="javascript:void(0)" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+sid+'\')" title="下载"></a>';
				}
				html+='\n	</p>';
			}
		}
		html+='\n		</div>';
	}
	html+='\n 			<div class="ws-type">';
	//发言人可以删除自己的发言
	html+='\n 				<a href="javascript:void(0);" id="delMsgTalk_'+msgTalk.id+'" onclick="delMsgTalk('+msgTalk.id+',1)"  class="fa fa-trash-o" title="删除"></a>';
	html+='\n 				<a id="img_'+msgTalk.id+'" name="replyImg" href="javascript:void(0);" class="fa fa-comment-o" title="回复" onclick="showArea(\'addTalk'+msgTalk.id+'\')"></a>';
	html+='\n 				<time>'+msgTalk.recordCreateTime+'</time>';
	html+='\n 			</div>';
	html+='\n 		</div>';
	html+='\n		<div class="ws-clear"></div>';
	html+='\n 	</div>';
	// 回复层
	html+='\n 	<div id="addTalk'+msgTalk.id+'" name="replyTalk" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk">';
	html+='\n 		<div class="shareText">';
	html+='\n 			<div class="ws-textareaBox" style="margin-top:10px;">';
	html+='\n				 <textarea id="operaterReplyTextarea_'+msgTalk.id+'" name="operaterReplyTextarea_'+msgTalk.id+'" rows="" cols="" class="form-control txtTalk" placeholder="回复……"></textarea>';
	html+='\n 				<div class="ws-otherBox">';
	html+='\n 					<div class="ws-meh" style="position: relative;">';
	//表情
	html+='\n 						<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_'+msgTalk.id+'" onclick="addBiaoQingObj(\'biaoQingSwitch_'+msgTalk.id+'\',\'biaoQingDiv_'+msgTalk.id+'\',\'operaterReplyTextarea_'+msgTalk.id+'\');"></a>';
	html+='\n 						<div id="biaoQingDiv_'+msgTalk.id+'" class="blk" style="display:none;position:absolute;width:200px;top:0px;z-index:99;left: 20px">';
	//表情DIV层
	html += '\n				     	<div class="main">';
	html += '\n				           	<ul style="padding: 0px">';
	html += ' \n'+getBiaoqing();
	html += '\n				           	</ul>';
	html += '\n				       	 </div>';
	html += '\n				    </div>';
	html += '\n				 </div>';
	//常用意见
	html+='\n				<div class="ws-plugs">';
	html+='\n					<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea(\'operaterReplyTextarea_'+msgTalk.id+'\',\''+sid+'\');" title="常用意见"></a>';
	html+='\n				</div>';
	html+='\n				<div class="ws-plugs">';
	html+='\n					<a class="btn-icon" href="javascript:void(0)" data-todoUser="yes" data-relateDiv="todoUserDiv_'+msgTalk.id+'">@</a>';
	html+='\n				</div>';
	//提醒方式
	/*html+='\n				<div class="ws-remind">';
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
	
	html+='\n 				<div class="ws-share">';
	html+='\n					 <button type="button" class="btn btn-info ws-btnBlue" data-relateTodoDiv="todoUserDiv_'+msgTalk.id+'" onclick="addTalk('+msgTalk.fileId+','+msgTalk.id+',this,'+msgTalk.speaker+')"">回复</button>';
	html+='\n 				</div>';
	//相关附件
	html+='\n 				<div style="clear: both;"></div>';
	html+='\n 				<div id="todoUserDiv_'+msgTalk.id+'" class="padding-top-10"></div>';
	html+='\n 				<div class="ws-notice">';
	
	html+= '\n 					<div style="clear:both;width: 300px" class="wu-example">';
	//用来存放文件信息
	html+= '\n 						<div id="thelistlistUpfiles_'+msgTalk.id+'_upfileId" class="uploader-list" ></div>';
	html+= '\n 						 <div class="btns btn-sm">';
	html+= '\n 							<div id="pickerlistUpfiles_'+msgTalk.id+'_upfileId">选择文件</div>';
	html+= '\n 						</div>';
	
	html+= '\n						<script type="text/javascript">\n';
	html+= '\n 							loadWebUpfiles(\'listUpfiles_'+msgTalk.id+'_upfileId\',\''+sid+'\',\'otherIframe\',\'pickerlistUpfiles_'+msgTalk.id+'_upfileId\',\'thelistlistUpfiles_'+msgTalk.id+'_upfileId\',\'filelistUpfiles_'+msgTalk.id+'_upfileId\');';
	html+= '\n 						</script>\n';
	
	html+= '\n 						<div style="position: relative; width: 350px; height: 90px;display: none">';
	html+= '\n 							<div style="float: left; width: 250px">';
	html+= '\n 								<select  list="listUpfiles_'+msgTalk.id+'" listkey="upfileId" listvalue="filename" id="listUpfiles_'+msgTalk.id+'_upfileId" name="listUpfiles_'+msgTalk.id+'.upfileId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">';
	html+= '\n 							 	</select>';
	html+= '\n 							</div>';
	html+= '\n 						</div>';
	html+= '\n 					</div>';
		
	html+='\n 				</div>';
	
	html+='\n			 </div>';
	html+='\n 		</div>';
	html+='\n 	</div>';
	html+='\n </div>';
	return html;
}