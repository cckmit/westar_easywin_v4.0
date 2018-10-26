/**
 * 投票查看页面拼接讨论
 * @param voteTalk
 * @param sid
 * @param sessionUser
 * @return
 */
function getVoteTalkString(voteTalk,sid,sessionUser){
	var html = '';
	if(voteTalk.parentId==-1){
		html+='\n<div id="talk'+voteTalk.id+'" class="ws-shareBox voteTalkInfoP">';
	}else{
		html+='\n<div id="talk'+voteTalk.id+'" class="ws-shareBox ws-shareBox2 voteTalkInfo">';
	}
	html+='\n		<div class="shareHead" data-container="body" data-toggle="popover" data-user='+voteTalk.talker+'  data-busId=0 data-busType=\'004\'>';
	//头像信息
	html += '		<img src="/downLoad/userImg/'+voteTalk.comId+'/'+voteTalk.talker+'?sid='+sid+'" title="'+voteTalk.talkerName+'"></img>\n';
	
	html+='\n 		</div>';
	html+='\n		<div class="shareText">';
	html+='\n			<span class="ws-blue">'+voteTalk.talkerName+'</span>';
	if(voteTalk.parentId>-1){
		html+='\n		<r>回复</r>';
		html+='\n		<span class="ws-blue">'+voteTalk.ptalkerName+'</span>';
	}
	html+='\n			<p class="ws-texts">'+voteTalk.content+'</p>';
	//附件
	if(voteTalk.listVoteTalkFile.length>0){
		html+='\n		<div class="file_div">';
		for(var i=0;i<voteTalk.listVoteTalkFile.length;i++){
			var upfiles = voteTalk.listVoteTalkFile[i];
			html+='\n		<p class="p_text">附件('+(i+1)+')：';
			
			if(upfiles.isPic==1){
				html+='\n			<img onload="AutoResizeImage(250,0,this,\'voteInfo\')"';
				html+='\n			src="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" />';
				html+='\n			&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
				html+='\n			&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic(\'/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'\',\''+sid+'\',\''+upfiles.upfileId+'\',\'004\',\''+voteTalk.voteId+'\')"></a>';
			}else{
				html += '			'+upfiles.filename+'\n';
				var fileExt = upfiles.fileExt;
				if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx'){
					html+='\n		&nbsp;&nbsp;<a class="fa fa-download" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+sid+'\')"></a>';
					html+='\n		&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'004\',\''+voteTalk.voteId+'\')"></a>';
				}else if(fileExt=='txt' ||fileExt=='pdf'){
					html+='\n		&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
					html+='\n		&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'004\',\''+voteTalk.voteId+'\')"></a>';
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
	html+='\n 					<a href="javascript:void(0)" id="delvoteTalk_'+voteTalk.id+'" onclick="delVoteTalk('+voteTalk.id+',1)" class="fa fa-trash-o" title="删除"></a>';
	html+='\n 					<a href="javascript:void(0)" id="img_'+voteTalk.id+'" onclick="showArea(\'addTalk'+voteTalk.id+'\')" class="fa fa-comment-o" title="回复"></a>';
	html+='\n				</span>';
	html+='\n				<span>';
	html+='\n 					<time>'+voteTalk.recordCreateTime+'</time>';
	html+='\n				</span>';
	html+='\n			</div>';
	html+='\n		</div>';
	html+='\n		<div class="ws-clear"></div>';
	html+='\n	</div>';
	
	//回复层
	html+='\n 	<div id="addTalk'+voteTalk.id+'" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk">';
	html+='\n 		<div class="shareText">';
	html+='\n 			<div class="ws-textareaBox" style="margin-top:10px;">';
	html+='\n 				<textarea id="operaterReplyTextarea_'+voteTalk.id+'" name="operaterReplyTextarea_'+voteTalk.id+'" style="height: 55px" class="form-control txtVoteTalk" placeholder="回复……"></textarea>';
	html+='\n				<div class="ws-otherBox" style="position: relative;">';
	html+='\n					<div class="ws-meh">';
	//表情
	html+='\n 						<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_'+voteTalk.id+'" onclick="addBiaoQingObj(\'biaoQingSwitch_'+voteTalk.id+'\',\'biaoQingDiv_'+voteTalk.id+'\',\'operaterReplyTextarea_'+voteTalk.id+'\');"></a>';
	html+= '\n				     	<div id="biaoQingDiv_'+voteTalk.id+'" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 15px">';
	html+= '\n				     		<div class="main">';
	html+= '\n				           		<ul style="padding: 0px">';
	html+= ' \n'+getBiaoqing();
	html+= '\n				           		</ul>';
	html+= '\n				       	 	</div>';
	html+= '\n				     	</div>';
	html+= '\n				  	</div>';
	//常用意见 
	html+='\n 					<div class="ws-plugs">';
	html+='\n 						<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea(\'operaterReplyTextarea_'+voteTalk.id+'\',\''+sid+'\');" title="常用意见"></a>';
	html+= '\n				 	 </div>';
	//提醒方式
	/*html+='\n					<div class="ws-remind">';
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
	html+='\n						<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('+voteTalk.voteId+','+voteTalk.id+',this,'+voteTalk.talker+')">回复</button>';
	html+='\n					</div>';
	//相关附件 
	html+='\n					<div style="clear: both;"></div>';
	html+='\n					<div class="ws-notice">';
	
	html+= '\n 						<div style="clear:both" class="ws-process">';
	html+= '\n							<div id="fileQueuelistUpfiles_'+voteTalk.id+'_upfileId" style="width: 300px">';
	html+= '\n							</div>';
	html+= '\n 							<div class="ws-choice-btn">';
	html+= '\n 					   	 		<input type="file" name="filelistUpfiles_'+voteTalk.id+'_upfileId" id="filelistUpfiles_'+voteTalk.id+'_upfileId"/>';
	html+= '\n							</div>';
	html+= '\n							<script type="text/javascript">\n';
	html+= '\n 								loadUpfiles(\'listUpfiles_'+voteTalk.id+'_upfileId\',\''+sid+'\',\'voteInfo\',\''+voteTalk.comId+'\');';
	html+= '\n 							</script>\n';
	html+= '\n 							<div style="position: relative; width: 350px; height: 90px;display: none">';
	html+= '\n 								<div style="float: left; width: 250px">';
	html+= '\n 									<select  list="listUpfiles_'+voteTalk.id+'" listkey="upfileId" listvalue="filename" id="listUpfiles_'+voteTalk.id+'_upfileId" name="listUpfiles_'+voteTalk.id+'.upfileId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">';
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
/**
 * 投票详情查看页面拼接代码
 * @param voteTalk
 * @param sid
 * @param sessionUser
 * @return
 */
function getVoteTalkStr(voteTalk,sid,sessionUser){
	var html = '';
	if(voteTalk.parentId==-1){
		html+='\n<div id="talk'+voteTalk.id+'" class="ws-shareBox voteTalkInfoP">';
	}else{
		html+='\n<div id="talk'+voteTalk.id+'" class="ws-shareBox ws-shareBox2 voteTalkInfo">';
	}
	html+='\n		<div class="shareHead" data-container="body" data-toggle="popover" data-user='+voteTalk.talker+' data-busId=0 data-busType=\'004\'>';
	//头像信息
	html += '		<img src="/downLoad/userImg/'+voteTalk.comId+'/'+voteTalk.talker+'?sid='+sid+'" title="'+voteTalk.talkerName+'"></img>\n';
	
	html+='\n 		</div>';
	html+='\n		<div class="shareText">';
	html+='\n			<span class="ws-blue">'+voteTalk.talkerName+'</span>';
	if(voteTalk.parentId>-1){
		html+='\n		<r>回复</r>';
		html+='\n		<span class="ws-blue">'+voteTalk.ptalkerName+'</span>';
	}
	html+='\n			<p class="ws-texts">'+voteTalk.content+'</p>';
	//附件
	if(voteTalk.listVoteTalkFile.length>0){
		html+='\n		<div class="file_div">';
		for(var i=0;i<voteTalk.listVoteTalkFile.length;i++){
			var upfiles = voteTalk.listVoteTalkFile[i];
			html+='\n		<p class="p_text">附件('+(i+1)+')：';
			
			if(upfiles.isPic==1){
				html+='\n			<img onload="AutoResizeImage(350,0,this,\'voteInfo\')"';
				html+='\n			src="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" />';
				html+='\n			&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
				html+='\n			&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic(\'/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'\',\''+sid+'\',\''+upfiles.upfileId+'\',\'004\',\''+voteTalk.voteId+'\')"></a>';
			}else{
				html += '			'+upfiles.filename+'\n';
				var fileExt = upfiles.fileExt;
				if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx' ){
					html+='\n		&nbsp;&nbsp;<a class="fa fa-download" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+sid+'\')"></a>';
					html+='\n		&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'004\',\''+voteTalk.voteId+'\')"></a>';
				}else if(fileExt=='txt' ||fileExt=='pdf'){
					html+='\n		&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
					html+='\n		&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'004\',\''+voteTalk.voteId+'\')"></a>';
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
	html+='\n 					<a href="javascript:void(0)" id="delvoteTalk_'+voteTalk.id+'" onclick="delVoteTalk('+voteTalk.id+',1)" class="fa fa-trash-o" title="删除"></a>';
	html+='\n 					<a href="javascript:void(0)" id="img_'+voteTalk.id+'" onclick="showArea(\'addTalk'+voteTalk.id+'\')" class="fa fa-comment-o" title="回复"></a>';
	html+='\n				</span>';
	html+='\n				<span>';
	html+='\n 					<time>'+voteTalk.recordCreateTime+'</time>';
	html+='\n				</span>';
	html+='\n			</div>';
	html+='\n		</div>';
	html+='\n		<div class="ws-clear"></div>';
	html+='\n	</div>';
	
	//回复层
	html+='\n 	<div id="addTalk'+voteTalk.id+'" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk">';
	html+='\n 		<div class="shareText">';
	html+='\n 			<div class="ws-textareaBox" style="margin-top:10px;">';
	html+='\n 				<textarea id="operaterReplyTextarea_'+voteTalk.id+'" name="operaterReplyTextarea_'+voteTalk.id+'" style="height: 55px" class="form-control txtVoteTalk" placeholder="回复……"></textarea>';
	html+='\n				<div class="ws-otherBox" style="position: relative;">';
	html+='\n					<div class="ws-meh">';
	//表情
	html+='\n 						<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_'+voteTalk.id+'" onclick="addBiaoQingObj(\'biaoQingSwitch_'+voteTalk.id+'\',\'biaoQingDiv_'+voteTalk.id+'\',\'operaterReplyTextarea_'+voteTalk.id+'\');"></a>';
	html+= '\n				     	<div id="biaoQingDiv_'+voteTalk.id+'" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 15px">';
	html+= '\n				     		<div class="main">';
	html+= '\n				           		<ul style="padding: 0px">';
	html+= ' \n'+getBiaoqing();
	html+= '\n				           		</ul>';
	html+= '\n				       	 	</div>';
	html+= '\n				     	</div>';
	html+= '\n				  	</div>';
	//常用意见 
	html+='\n 					<div class="ws-plugs">';
	html+='\n 						<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea(\'operaterReplyTextarea_'+voteTalk.id+'\',\''+sid+'\');" title="常用意见"></a>';
	html+= '\n				 	 </div>';
	//提醒方式
	/*html+='\n					<div class="ws-remind">';
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
	html+='\n						<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('+voteTalk.voteId+','+voteTalk.id+',this,'+voteTalk.talker+')">回复</button>';
	html+='\n					</div>';
	//相关附件 
	html+='\n					<div style="clear: both;"></div>';
	html+='\n					<div class="ws-notice">';
	
	html+= '\n 						<div style="clear:both;width: 300px" class="wu-example">';
	//用来存放文件信息
	html+= '\n 							<div id="thelistlistUpfiles_'+voteTalk.id+'_upfileId" class="uploader-list" ></div>';
	html+= '\n 						 	<div class="btns btn-sm">';
	html+= '\n 								<div id="pickerlistUpfiles_'+voteTalk.id+'_upfileId">选择文件</div>';
	html+= '\n 							</div>';
	
	html+= '\n							<script type="text/javascript">\n';
	html+= '\n 								loadWebUpfiles(\'listUpfiles_'+voteTalk.id+'_upfileId\',\''+sid+'\',\'voteInfo\',\'pickerlistUpfiles_'+voteTalk.id+'_upfileId\',\'thelistlistUpfiles_'+voteTalk.id+'_upfileId\',\'filelistUpfiles_'+voteTalk.id+'_upfileId\');';
	html+= '\n 							</script>\n';
	
	html+= '\n 							<div style="position: relative; width: 350px; height: 90px;display: none">';
	html+= '\n 								<div style="float: left; width: 250px">';
	html+= '\n 									<select  list="listUpfiles_'+voteTalk.id+'" listkey="upfileId" listvalue="filename" id="listUpfiles_'+voteTalk.id+'_upfileId" name="listUpfiles_'+voteTalk.id+'.upfileId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">';
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

/**
 * 取得投票拼接字符串
 * @param vote
 * @param sid
 * @return
 */
function getVoteHtml(vote,sid){
	var html='';
    //隐藏起来，用于修改投票
    var voting = '';
    var voted='';
    //还没有投票的  还没有过期的,可以投票
    if(vote.voterChooseNum == 0 && vote.enabled  == 1){
    	voting ='\n <div class="ws-voting" style="display:block;" id="voting'+vote.id+'">';
    	voted='\n <div class="ws-voting" style="display:none;" id="voted'+vote.id+'">';
    }else{
    	voting ='\n <div class="ws-voting" style="display:none;" id="voting'+vote.id+'">';
    	voted='\n <div class="ws-voting" style="display:block;" id="voted'+vote.id+'">';
    }
    
    
    voting+='\n<div class="ws-voting1">';
    voting+='\n<div class="ws-voting-text">';
    voting+='\n	(总计'+vote.voteTotal+'票)';
    //是否匿名
    if(vote.voteType == 1){
    	voting+='	匿名投票';
    }
    voting+='\n 最多可以选择'+vote.maxChoose+'    投票后可以查看结果';
    voting+='\n</div>';
    voting+='\n</div>';
    
  //选项
	for(var i=0;i<vote.voteChooses.length;i++){
		var voteChoose = vote.voteChooses[i];
		voting+='\n<div class="ws-voting1">';
		voting+='\n<div class="ws-voting-text">';
		voting+='\n	<label>';
		//选项文本类型选择
		if(vote.maxChoose==1){
			voting +="<input type=\"radio\" name=\"voteChoose\" value="+voteChoose.id;
			if(voteChoose.chooseState==1){
				voting +=" checked ";
			}
			voting +=">\n";
		}else{
			voting +="<input type=\"checkbox\" name=\"voteChoose\" value="+voteChoose.id;
			if(voteChoose.chooseState==1){
				voting +=" checked ";
			}
			voting +=">\n";
		}
		
		voting+='\n	<span class="text">&nbsp;&nbsp;'+voteChoose.content +'</span>';
		voting+='\n	</label>';
		voting+='\n</div>';
		//选项图片
		if(null!=voteChoose.midImgUuid && null!=voteChoose.largeImgUuid ){
			voting+='\n		<div style="display:block;"id="mid_'+vote.id+'_'+(i+1)+'">';
			voting+='\n			<a href="javascript:void(0)" onclick="$(\'#large_'+vote.id+'_'+(i+1)+'\').show();$(\'#mid_'+vote.id+'_'+(i+1)+'\').hide();resizeVoteH(\'voteInfo\')" >';
			voting+='\n				<img src="/downLoad/down/'+voteChoose.midImgUuid+'/'+voteChoose.midImgName+'?sid='+sid+'" onload="AutoResizeImage(0,0,this,\'voteInfo\')"/><br/>	';
			voting+='\n			</a>';
			voting+='\n		</div>';
			voting+='\n		<div style="display:none;" id="large_'+vote.id+'_'+(i+1)+'">';
			voting+='\n			<a href="javascript:void(0)" onclick="$(\'#large_'+vote.id+'_'+(i+1)+'\').hide();$(\'#mid_'+vote.id+'_'+(i+1)+'\').show();resizeVoteH(\'voteInfo\')" >';
			voting+='\n				<img src="/downLoad/down/'+voteChoose.largeImgUuid+'/'+voteChoose.largeImgName+'?sid='+sid+'" onload="AutoResizeImage(0,0,this,\'voteInfo\')"/><br/>	';
			voting+='\n			</a>';
			voting+='\n		</div>';
		}
		voting+='\n</div>';
	}
	
	voting+='\n<div class="ws-btn" style="padding-top: 10px;clear:both">';
	voting+='\n<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="vote(this);">投票</a>';
	//已经投过票了
	if(vote.voterChooseNum >0){
		voting+='\n	<a href="javascript:void(0);" class="btn btn-default" onclick="$(\'#voting'+vote.id+'\').hide();$(\'#voted'+vote.id+'\').show();resizeVoteH(\'voteInfo\')">取消</a>';
		
	}else{//未投票的查看投票结果 
		voting+='\n	<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="$(\'#voting'+vote.id+'\').hide();$(\'#voted'+vote.id+'\').show();resizeVoteH(\'voteInfo\')">查看结果</a>';
	}
	
	voting+='\n</div>';
    
    
    voting+='\n</div>';
    //进行投票结束
    
    
    //已经投票的 已经过期的 已经过期但未投票的
   voted+='\n<div class="ws-voting1">';
   voted+='\n<div class="ws-voting-text">';
   voted+='\n	(总计'+vote.voteTotal+'票)';
   if(vote.enabled==0){
	   voted+=' 投票已截止';
   }
   voted+='\n</div>';
   voted+='\n</div>';
   //选项
   for(var i=0;i<vote.voteChooses.length;i++){
		var voteChoose = vote.voteChooses[i];
		voted+='\n		<div class="ws-voting1" style="clear: both">';
		voted+='\n		<div class="ws-voting-text">';
		voted+='\n			<label>';
		voted+='\n				<p>选项'+(i+1)+':'+voteChoose.content+'(';
		//投过票或是非匿名
		if(voteChoose.total>0 && vote.voteType==0){
			voted+='\n			<a href="javascript:void(0)" onclick="viewVoter(\'voterPic_'+vote.id+'_'+(i+1)+'\')">';
			voted+='\n				'+voteChoose.total+'票';
			voted+='\n			</a>';
		}else{
			voted+='\n				'+voteChoose.total+'票';
		}
		voted+='\n			)';
		voted+='\n				</p>';
		voted+='\n			</label>';
		voted+='\n		</div>';
		//选项图片
		voted+='\n <div>';
		if(null!=voteChoose.midImgUuid && null!=voteChoose.largeImgUuid ){
			voted+='\n	<div style="display:block;" id="midV_'+vote.id+'_'+(i+1)+'">';
			voted+='\n		<a href="javascript:void(0)" onclick="$(\'#largeV_'+vote.id+'_'+(i+1)+'\').show();$(\'#midV_'+vote.id+'_'+(i+1)+'\').hide();resizeVoteH(\'voteInfo\')" >';
			voted+='\n			<img src="/downLoad/down/'+voteChoose.midImgUuid+'/'+voteChoose.midImgName+'?sid='+sid+'" onload="AutoResizeImage(0,0,this,\'voteInfo\')"/><br/>	';
			voted+='\n		</a>';
			voted+='\n	</div>';
			voted+='\n	<div style="display:none;" id="largeV_'+vote.id+'_'+(i+1)+'">';
			voted+='\n		<a href="javascript:void(0)" onclick="$(\'#largeV_'+vote.id+'_'+(i+1)+'\').hide();$(\'#midV_'+vote.id+'_'+(i+1)+'\').show();resizeVoteH(\'voteInfo\')" >';
			voted+='\n			<img src="/downLoad/down/'+voteChoose.largeImgUuid+'/'+voteChoose.largeImgName+'?sid='+sid+'" onload="AutoResizeImage(0,0,this,\'voteInfo\')"/><br/>	';
			voted+='\n		</a>';
			voted+='\n	</div>';
		}
		voted+='\n</div>';
		//投过票非匿名查看投票人
		if(voteChoose.total>0 && vote.voteType==0){
			voted+='\n<div style="clear:both;display: none" id="voterPic_'+vote.id+'_'+(i+1)+'">';
			for(var j=0;j<voteChoose.voters.length;j++){
				voted+='\n	<div class="ticket-user pull-left other-user-box" style="float: left;margin-top: 5px;">';
				var voter = voteChoose.voters[j];
				voted +="\n<img src=\"/downLoad/userImg/"+voter.comId+"/"+voter.voter+"?sid="+sid+"\" title=\""+voter.voterName+"\" class=\"user-avatar\"></img>\n";
				voted+='\n	<span class="user-name">'+voter.voterName+'</span>';
				
				voted+='\n	</div>';
			}
			voted+='\n</div>';
		}
		voted+='\n</div>';
   }
    
   
   voted+='\n<div class="ws-btn" style="padding-top: 10px">';
   //是否可以修改投票
   if(vote.voterChooseNum == 0 && vote.enabled==1){//未投票的返回投票
		voted+='\n <span style="display: block;">';
		voted+='\n	<a href="javascript:void(0);" class="btn btn-default" onclick="$(\'#voting'+vote.id+'\').show();$(\'#voted'+vote.id+'\').hide();resizeVoteH(\'voteInfo\')">返回投票</a>';
		voted+='\n</span>';
   }else{
	   //投过票的修改投票 
	   if(vote.enabled==0){//已经过期
		   voted+='\n  <span style="float: left;margin-right: 5px;display:none;" id="updateVote">';
		   voted+='\n	<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="$(\'#voting'+vote.id+'\').show();$(\'#voted'+vote.id+'\').hide();resizeVoteH(\'voteInfo\')">修改投票</a>';
		   voted+='\n</span>';
	   }else{
		   voted+='\n  <span style="float: left;margin-right: 5px;display:block;" id="updateVote">';
		   voted+='\n	<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="$(\'#voting'+vote.id+'\').show();$(\'#voted'+vote.id+'\').hide();resizeVoteH(\'voteInfo\')">修改投票</a>';
		   voted+='\n</span>';
	   }
		
	    voted+='\n<span style="float: left;margin-right: 5px;display: block;">';
		voted+='\n	<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="reloadDiv('+vote.id+',\''+sid+'\');">刷新结果</a>';
		voted+='\n</span>';
   }
	voted+='\n</div>';

    voted+='\n</div>';
    html = html+voting;
    html = html+voted;
    return html;
}
//刷新投票结果
function reloadDiv(id,sid){
	//异步提交表单
    $("#voteForm").ajaxSubmit({
        type:"post",
        url:"/vote/getVoteInfo?sid="+sid+"&t="+Math.random(),
        data:{"id":id},
        dataType: "json", 
        success:function(data){
	        if(null!=data.vote){
		        var vote = data.vote;
		        setNum(vote.voteTotal);
	        	$("#voteChooseId").html('');
	        	var html = getVoteHtml(vote,sid );
		        $("#voteChooseId").html(html);
		        showNotification(0,"刷新成功！");
		    }
        },
        error:function(XmlHttpRequest,textStatus,errorThrown){
        }
    });
}

/**
 * 取得投票拼接字符串
 * @param vote
 * @param sid
 * @return
 */
function getVoteStr(vote,sid){
	var html='';
    //隐藏起来，用于修改投票
    var voting = '';
    var voted='';
    //还没有投票的  还没有过期的,可以投票
    if(vote.voterChooseNum == 0 && vote.enabled  == 1){
    	voting ='\n <div class="ws-voting" style="display:block;" id="voting'+vote.id+'">';
    	voted='\n <div class="ws-voting" style="display:none;" id="voted'+vote.id+'">';
    }else{
    	voting ='\n <div class="ws-voting" style="display:none;" id="voting'+vote.id+'">';
    	voted='\n <div class="ws-voting" style="display:block;" id="voted'+vote.id+'">';
    }
    
    
    voting+='\n<div class="ws-voting1">';
    voting+='\n<div class="ws-voting-text">';
    voting+='\n	(总计'+vote.voteTotal+'票)';
    //是否匿名
    if(vote.voteType == 1){
    	voting+='	匿名投票';
    }
    voting+='\n 最多可以选择'+vote.maxChoose+'    投票后可以查看结果';
    voting+='\n</div>';
    voting+='\n</div>';
    
  //选项
	for(var i=0;i<vote.voteChooses.length;i++){
		var voteChoose = vote.voteChooses[i];
		voting+='\n<div class="ws-voting1">';
		voting+='\n<div class="ws-voting-text">';
		voting+='\n	<label>';
		//选项文本类型选择
		if(vote.maxChoose==1){
			voting +="<input type=\"radio\" name=\"voteChoose\" value="+voteChoose.id;
			if(voteChoose.chooseState==1){
				voting +=" checked ";
			}
			voting +=">\n";
		}else{
			voting +="<input type=\"checkbox\" name=\"voteChoose\" value="+voteChoose.id;
			if(voteChoose.chooseState==1){
				voting +=" checked ";
			}
			voting +=">\n";
		}
		voting+='\n	<span class="text">&nbsp;&nbsp;'+voteChoose.content +'</span>';
		voting+='\n	</label>';
		voting+='\n</div>';
		//选项图片
		if(null!=voteChoose.midImgUuid && null!=voteChoose.largeImgUuid ){
			voting+='\n		<div style="display:block;"id="mid_'+vote.id+'_'+(i+1)+'">';
			voting+='\n			<a href="javascript:void(0)" onclick="$(\'#large_'+vote.id+'_'+(i+1)+'\').show();$(\'#mid_'+vote.id+'_'+(i+1)+'\').hide();" >';
			voting+='\n				<img src="/downLoad/down/'+voteChoose.midImgUuid+'/'+voteChoose.midImgName+'?sid='+sid+'" onload="AutoResizeImage(0,0,this,\'\')"/><br/>	';
			voting+='\n			</a>';
			voting+='\n		</div>';
			voting+='\n		<div style="display:none;" id="large_'+vote.id+'_'+(i+1)+'">';
			voting+='\n			<a href="javascript:void(0)" onclick="$(\'#large_'+vote.id+'_'+(i+1)+'\').hide();$(\'#mid_'+vote.id+'_'+(i+1)+'\').show();resizeVoteH(\'\')" >';
			voting+='\n				<img src="/downLoad/down/'+voteChoose.largeImgUuid+'/'+voteChoose.largeImgName+'?sid='+sid+'" onload="AutoResizeImage(0,0,this,\'\')"/><br/>	';
			voting+='\n			</a>';
			voting+='\n		</div>';
		}
		voting+='\n</div>';
	}
	
	voting+='\n<div class="ws-btn" style="padding-top: 10px;clear:both">';
	voting+='\n<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="vote(this);">投票</a>';
	//已经投过票了
	if(vote.voterChooseNum >0){
		voting+='\n	<a href="javascript:void(0);" class="btn btn-default" onclick="$(\'#voting'+vote.id+'\').hide();$(\'#voted'+vote.id+'\').show();">取消</a>';
		
	}else{//未投票的查看投票结果 
		voting+='\n	<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="$(\'#voting'+vote.id+'\').hide();$(\'#voted'+vote.id+'\').show();">查看结果</a>';
	}
	
	voting+='\n</div>';
    
    
    voting+='\n</div>';
    //进行投票结束
    
    
    //已经投票的 已经过期的 已经过期但未投票的
   voted+='\n<div class="ws-voting1">';
   voted+='\n<div class="ws-voting-text">';
   voted+='\n	(总计'+vote.voteTotal+'票)';
   if(vote.enabled==0){
	   voted+=' 投票已截止';
   }
   voted+='\n</div>';
   voted+='\n</div>';
   //选项
   for(var i=0;i<vote.voteChooses.length;i++){
		var voteChoose = vote.voteChooses[i];
		voted+='\n		<div class="ws-voting1" style="clear: both">';
		voted+='\n		<div class="ws-voting-text">';
		voted+='\n			<label>';
		voted+='\n				<p>选项'+(i+1)+':'+voteChoose.content+'(';
		//投过票或是非匿名
		if(voteChoose.total>0 && vote.voteType==0){
			voted+='\n			<a href="javascript:void(0)" onclick="viewVoter(\'voterPic_'+vote.id+'_'+(i+1)+'\')">';
			voted+='\n				'+voteChoose.total+'票';
			voted+='\n			</a>';
		}else{
			voted+='\n				'+voteChoose.total+'票';
		}
		voted+='\n			)';
		voted+='\n				</p>';
		voted+='\n			</label>';
		voted+='\n		</div>';
		//选项图片
		voted+='\n <div>';
		if(null!=voteChoose.midImgUuid && null!=voteChoose.largeImgUuid ){
			voted+='\n	<div style="display:block;" id="midV_'+vote.id+'_'+(i+1)+'">';
			voted+='\n		<a href="javascript:void(0)" onclick="$(\'#largeV_'+vote.id+'_'+(i+1)+'\').show();$(\'#midV_'+vote.id+'_'+(i+1)+'\').hide();" >';
			voted+='\n			<img src="/downLoad/down/'+voteChoose.midImgUuid+'/'+voteChoose.midImgName+'?sid='+sid+'" onload="AutoResizeImage(0,0,this,\'\')"/><br/>	';
			voted+='\n		</a>';
			voted+='\n	</div>';
			voted+='\n	<div style="display:none;" id="largeV_'+vote.id+'_'+(i+1)+'">';
			voted+='\n		<a href="javascript:void(0)" onclick="$(\'#largeV_'+vote.id+'_'+(i+1)+'\').hide();$(\'#midV_'+vote.id+'_'+(i+1)+'\').show();" >';
			voted+='\n			<img src="/downLoad/down/'+voteChoose.largeImgUuid+'/'+voteChoose.largeImgName+'?sid='+sid+'" onload="AutoResizeImage(0,0,this,\'\')"/><br/>	';
			voted+='\n		</a>';
			voted+='\n	</div>';
		}
		voted+='\n</div>';
		//投过票非匿名查看投票人
		if(voteChoose.total>0 && vote.voteType==0){
			voted+='\n<div style="clear:both;display: none" id="voterPic_'+vote.id+'_'+(i+1)+'">';
			for(var j=0;j<voteChoose.voters.length;j++){
				voted+='\n	<div class="ticket-user pull-left other-user-box" style="float: left;margin-top: 5px;">';
				var voter = voteChoose.voters[j];
				voted +="\n<img src=\"/downLoad/userImg/"+voter.comId+"/"+voter.voter+"?sid="+sid+"\" title=\""+voter.voterName+"\" class=\"user-avatar\"></img>\n";
				voted+='\n	<span class="user-name">'+voter.voterName+'</span>';
				
				voted+='\n	</div>';
			}
			voted+='\n</div>';
		}
		voted+='\n</div>';
   }
    
   
   voted+='\n<div class="ws-btn" style="padding-top: 10px;clear:both">';
   //是否可以修改投票
   if(vote.voterChooseNum == 0 && vote.enabled==1){//未投票的返回投票
		voted+='\n <span style="display: block;">';
		voted+='\n	<a href="javascript:void(0);" class="btn btn-default" onclick="$(\'#voting'+vote.id+'\').show();$(\'#voted'+vote.id+'\').hide();">返回投票</a>';
		voted+='\n</span>';
   }else{
	   //投过票的修改投票 
	   if(vote.enabled==0){//已经过期
		   voted+='\n  <span style="float: left;margin-right: 5px;display:none;" id="updateVote">';
		   voted+='\n	<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="$(\'#voting'+vote.id+'\').show();$(\'#voted'+vote.id+'\').hide();">修改投票</a>';
		   voted+='\n</span>';
	   }else{
		   voted+='\n  <span style="float: left;margin-right: 5px;display:block;" id="updateVote">';
		   voted+='\n	<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="$(\'#voting'+vote.id+'\').show();$(\'#voted'+vote.id+'\').hide();">修改投票</a>';
		   voted+='\n</span>';
	   }
		
	    voted+='\n<span style="float: left;margin-right: 5px;display: block;">';
		voted+='\n	<a href="javascript:void(0);" class="btn btn-info ws-btnBlue" onclick="reloadRes('+vote.id+',\''+sid+'\');">刷新结果</a>';
		voted+='\n</span>';
   }
	voted+='\n</div>';

    voted+='\n</div>';
    html = html+voting;
    html = html+voted;
    return html;
}

//刷新投票结果
function reloadRes(id,sid){
	//异步提交表单
	$("#voteForm").ajaxSubmit({
		type:"post",
		url:"/vote/getVoteInfo?sid="+sid+"&t="+Math.random(),
		data:{"id":id},
		dataType: "json", 
		success:function(data){
			if(null!=data.vote){
				var vote = data.vote;
				$("#voteChooseId").html('');
				var html = getVoteStr(vote,sid );
				$("#voteChooseId").html(html);
				showNotification(1,"刷新成功！");
			}
		},
		error:function(XmlHttpRequest,textStatus,errorThrown){
		}
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
	 resizeVoteH('voteInfo')
}

