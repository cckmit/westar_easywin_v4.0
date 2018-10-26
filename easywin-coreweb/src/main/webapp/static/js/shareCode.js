//拼接投票代码
	function getHtml(vote,sid){
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
    voting+='\n <div class="ws-type">';
    voting+='\n	(总计'+vote.voteTotal+'票)';
  //是否匿名
    if(vote.voteType == 1){
    	voting+='	匿名';
    }
    voting+='\n 最多选'+vote.maxChoose+'项 <br/>  截止时间<span id="otherTime">'+vote.finishTime+'</span> :00';
    voting+='\n</div>';
    //选项
    for(var i=0;i<vote.voteChooses.length;i++){
		var voteChoose = vote.voteChooses[i];
		voting+='\n	<div class="ws-voteBox" style="clear:both">';
		voting+='\n		<div class="radio">';
		voting+='\n			<label class="ws-check">';
		//文本类型选择
		if(vote.maxChoose==1){
			voting +="\n <lable>";
			voting +="<input type=\"radio\" name=\"voteChoose\" value="+voteChoose.id;
			if(voteChoose.chooseState==1){
				voting +=" checked ";
			}
			voting +="\n >";
			voting +="\n <span class=\"text\">&nbsp;&nbsp;"+voteChoose.content +"</span>";
			voting +="\n </lable>";
		}else{
			voting +="<input type=\"checkbox\" name=\"voteChoose\" value="+voteChoose.id;
			if(voteChoose.chooseState==1){
				voting +=" checked ";
			}
			voting +="\n >";
			voting +="\n <span class=\"text\">&nbsp;&nbsp;"+voteChoose.content +"</span>";
			voting +="\n </lable>";
		}
		voting+='\n	</label>';
		
		//选项图片
		if(null!=voteChoose.midImgUuid && null!=voteChoose.largeImgUuid ){
			voting+='\n	<div class="ws-voteImg">';
			voting+='\n		<img src="/downLoad/down/'+voteChoose.midImgUuid+'/'+voteChoose.midImgName+'?sid='+sid+'"';
			voting+='\n 	onclick="viewOrgByPath(\'/downLoad/down/'+voteChoose.largeImgUuid+'/'+voteChoose.largeImgName+'\')"';
			voting+='\n />';
			voting+='\n </div>';
		}
		voting+='\n		</div>';
		voting+='\n	</div>';
    }
    //投票选项结束
    voting+='\n <div class="ws-type">';
    voting+='\n <button class="btn btn-info ws-btnBlue" style="margin-right:10px;" onclick="startVote(this,'+vote.maxChoose+','+vote.id+','+vote.voterChooseNum+',\''+sid+'\')" type="button">投票</button>';
  //已经投过票了
	if(vote.voterChooseNum >0){
		voting+='\n	<button class="btn btn-default" onclick="$(\'#voting'+vote.id+'\').hide();$(\'#voted'+vote.id+'\').show();" type="button">取消</button>';
		
	}else{//未投票的查看投票结果 
		voting+='\n	<button class="btn btn-info ws-btnBlue" style="margin-right:10px;" type="button" onclick="$(\'#voting'+vote.id+'\').hide();$(\'#voted'+vote.id+'\').show();">查看结果</button>';
	}
    voting+='\n </div>';
    
    voting+='\n </div>';
    voting+='\n <div class="ws-clear"></div>';
    voting+='\n';
    voting+='\n';
	
	
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
 		voted+='\n<div class="ws-voteBox" style="clear:both">';
 		voted+='\n<div class="radio">';
 		voted+='\n<label class="ws-check">选项'+(i+1)+':'+voteChoose.content+'(';
 		//投过票或是非匿名
		if(voteChoose.total>0 && vote.voteType==0){
			voted+='\n			<a href="javascript:void(0)" onclick="viewVoter(\'voterPic_'+vote.id+'_'+(i+1)+'\')">';
			voted+='\n				'+voteChoose.total+'票';
			voted+='\n			</a>';
		}else{
			voted+='\n				'+voteChoose.total+'票';
		}
		voted+='\n			)';
 		voted+='\n</label>';
 		if(null!=voteChoose.midImgUuid && null!=voteChoose.largeImgUuid ){
 			voted+='\n <div class="ws-voteImg">';
 			voted+='\n		<img src="/downLoad/down/'+voteChoose.midImgUuid+'/'+voteChoose.midImgName+'?sid='+sid+'"';
 			voted+='\n 	onclick="viewOrgByPath(\'/downLoad/down/'+voteChoose.largeImgUuid+'/'+voteChoose.largeImgName+'\')"';
 			voted+='\n />';
 			voted+='\n</div>';
 		}
 		//投过票非匿名查看投票人
		if(voteChoose.total>0 && vote.voteType==0){
			voted+='\n<div style="clear:both;display: none" id="voterPic_'+vote.id+'_'+(i+1)+'">';
			for(var j=0;j<voteChoose.voters.length;j++){
				var voter = voteChoose.voters[j];
				
				voted+='\n	<div class="ticket-user pull-left other-user-box" style="margin-top: 2px;padding-left:20px">';
				var voter = voteChoose.voters[j];
				voted +="\n<img class=\"user-avatar\" src=\"/downLoad/userImg/"+voter.comId+"/"+voter.voter+"?sid="+sid+"\" title=\""+voter.voterName+"\"></img>\n";
				voted+='\n	<i class="user-name">'+voter.voterName+'</i>';
				
				voted+='\n	</div>';
				
			}
			voted+='\n</div>';
		}
		voted+='\n</div>';
		voted+='\n</div>';
		
    }
    voted+='\n<div class="ws-type">';
		//投票选项结束
    //是否可以修改投票
    if(vote.voterChooseNum == 0 && vote.enabled==1){//未投票的返回投票
    	voted+='\n <button class="btn btn-default" onclick="$(\'#voting'+vote.id+'\').show();$(\'#voted'+vote.id+'\').hide();" type="button">返回投票</button>';
    }else{
 	   //投过票的修改投票 
 	   if(vote.enabled!=0){
 		   voted+='\n <button class="btn btn-info ws-btnBlue" style="margin-right:5px;padding: 6px 6px" onclick="$(\'#voting'+vote.id+'\').show();$(\'#voted'+vote.id+'\').hide();" type="button">修改投票</button>';
 	   }
 	   
 	   voted+='\n <button class="btn btn-default" style="padding: 6px 6px" onclick="reloadDiv('+vote.id+',\''+sid+'\');" type="button">刷新结果</button>';
 	   
 	  }
		
    voted+='\n	</div>';
    voted+='\n</div>';
    voted+='\n<div class="ws-clear"></div>';
    html = html+voting;
    html = html+voted;
    return html;
}

/**
 * 投票
 * @param maxChoose 投票最多选几项
 * @param id 投票主键
 * @param voterChooseNum 是否已投票
 * @param sid
 * @return
 */
function startVote(ts,maxChoose,id,voterChooseNum,sid){
	//异步提交表单
	//单选
	var radios = $("#voteForm"+id).find(':radio[name="voteChoose"]:checked');
	//多选
	var ckBoxs = $("#voteForm"+id).find(":checkbox[name='voteChoose']:checked");
	var lenr = radios.length;
	var lenc = ckBoxs.length;
    $("#voteForm"+id).ajaxSubmit({//先进行时间验证是否过期
        type:"post",
        url:"/vote/checkVoteTime?sid="+sid+"&t="+Math.random(),
        data:{"id":id},
        dataType: "json", 
        beforeSubmit:function(a,f,o){
	    	if(lenr+lenc==0){
	    		window.top.layer.msg('请先选择一个选项', {icon: 6}); 
	    		return false;
	    	}else if(lenc>maxChoose){
	    		window.top.layer.msg('最多只能选择'+maxChoose+'选项', {icon: 6}); 
	    		return false;
	    	}
    	},
        success:function(data){
	        if('y'==data.state){
	        	//所选项
    			var chooseIds = new Array();
    			if(lenr==1){
    				chooseIds.push($(radios).val());
    			}
    			if(lenc>0){
    				for(var i=0;i<lenc;i++){
    					chooseIds.push($(ckBoxs[i]).val());
    				}
    			}
    			//异步提交表单
    		    $("#voteForm"+id).ajaxSubmit({
    		        type:"post",
    		        url:"/vote/voteChoose?sid="+sid+"&t="+Math.random(),
    		        beforeSubmit:function(a,f,o){
    		    	},
    		        data:{"voteChooses":chooseIds,"voteId":id,"isVote":voterChooseNum,"backObj":"yes"},
    		        dataType: "json", 
    		        success:function(data){
    			        if(null!=data.vote){
    				        var vote = data.vote;
    			        	$("#voteForm"+vote.id).html('');

    			        	var html = getHtml(vote,sid);
    				        
    				        $("#voteForm"+id).html(html);
    				        
    				        window.top.layer.msg("投票成功！", {icon: 6}); 
    				    }
    		        },
    		        error:function(XmlHttpRequest,textStatus,errorThrown){
    		        }
    		    });
		    }else{
	    		window.top.layer.msg(data.info, {icon: 6}); 
		    	reloadDiv(id,sid);
			}
        },
        error:function(XmlHttpRequest,textStatus,errorThrown){
        }
    });
	
}

//刷新投票结果
function reloadDiv(id,sid){
	//异步提交表单
    $("#voteForm"+id).ajaxSubmit({
        type:"post",
        url:"/vote/getVoteInfo?sid="+sid+"&t="+Math.random(),
        data:{"id":id},
        dataType: "json", 
        success:function(data){
	        if(null!=data.vote){
		        var vote = data.vote;
	        	$("#voteForm"+vote.id).html('');
	        	var html = getHtml(vote,sid );
		        $("#voteForm"+id).html(html);
		    }
        },
        error:function(XmlHttpRequest,textStatus,errorThrown){
        }
    });
}

//查看投票人
function viewVoter(id){
	if($("#"+id).css("display")=="none"){
		$("#"+id).show();
	}else{
		$("#"+id).hide();
	}
}


/**
 * 查看更多(个人中心)
 * @param sid session标识
 * @param ts
 * @param creator session用户主键
 * @return
 */
function loadMeinv(sid,ts,cardPlace) {
	var array = $("#searchForm").find(":checkbox[name='modTypes']:checked");
	//所选项
	var modTypes = new Array();
	if(array && array.length>0){
		for(var i=0;i<array.length;i++){
			modTypes.push($(array[i]).val());
		}
	}
	$.ajax({
		type : "post",
		url : "/msgShare/nextPageSizeMsgs?sid="+sid+"&rnd="+Math.random(),
		dataType:"json",
		traditional :true,
		data:{pageSize:15,
		pageNum:++pageNum,
		content:$("#content").val(),
		type:$("#type").val(),
		startDate:$("#startDate").val(),
		endDate:$("#endDate").val(),
		modTypes:modTypes,
		creatorType:$("#creatorType").val()
		},
		  beforeSend: function(XMLHttpRequest){
         },
		  success : function(data){
        	if(data.length==0){
        		$("#moreMsg").hide();
        		return;
        	}
        	if(data.length<5){
     			$("#moreMsg").hide();
     		}else{
     			$("#moreMsg").show();
     		}
        	 for(var i=0;i<data.length;i++){
        		 var msg = data[i];
        		 if(undefined != $("#msg"+msg.id).attr("class")){
     				return;
        		 }else{
        			 var modId = msg.modId;
        			 if(msg.type=='1' && msg.traceType=='0'){//普通
        				 modId = msg.id;
        			 }
        			 var html = '';
        			 html +='\n <li class="item first-item" id="msg'+msg.id+'">';
        			 html +='\n  <div class="message-content">';
        			 html+='\n <img src="/downLoad/userImg/'+msg.comId+'/'+msg.creator+'?sid='+sid+'" class="message-head" width="50" height="50" title="' + msg.creatorName + '"/>';
        			 if(msg.type=='004' && msg.isDel=='0'){//投票
        				 var vote = msg.vote;
        				 html +='\n	<div class="content-headline">';
        				 html +='\n		<div class="checkbox pull-left ps-chckbox">';
        				 html +='\n			<label>';
        				 html +='\n				<a href="javascript:void(0)" attentionState="'+msg.attentionState+'" busType="'+msg.type+'" busId="'+msg.modId+'" describe="0" iconSize="sizeMd" onclick="changeAtten(\''+sid+'\',this)" style="font-size:18px;margin-right:5px;" >';
        				 if(msg.attentionState==0){
    						 html +='\n 		<i class="fa fa-star-o sizeMd"></i>';
    					 }else{
    						 html +='\n 			<i class="fa fa-star ws-star sizeMd"></i> ';
    					 }
    					 html +='\n				</a>';
    					 html +='\n	 		</label>';
    					 html +='\n	 	</div>';
        				 html +='\n	 	['+msg.typeName +']';
        				 html +='\n		<time>'+msg.createDate+'</time>';
        				 html +='\n	</div>';
        				 html +='\n <div class="content-text">';
        				 html +='\n     <a href="javascript:void(0)" onclick="msgShareView('+msg.modId+',\''+msg.type+'\',\''+sid+'\');">';
						 html +='\n		  '+vote.voteContent+'';
						 html +='\n		</a>';
						 html +='\n	</div>';
        				 
        				 html +='\n	 <form id="voteForm'+vote.id+'">';
        				 html +=getHtml(vote,sid);
        				 html +='\n	 </form>';
        				 html +='\n	</div>';
        				 html +='\n</li>';
        				 
        			 }else{
        				 html +='\n	<div class="content-headline">';
        				 html +='\n		<div class="checkbox pull-left ps-chckbox">';
        				 html +='\n			<label>';
        				 if((msg.type=='1' || msg.type=='003' || msg.type=='004' || msg.type=='005' || msg.type=='080'  || msg.type=='011' || msg.type=='012')&& msg.isDel=='0' && msg.traceType=='0'){
        					 if(msg.type=='1'){
        						 html +='\n	 <a href="javascript:void(0)"  attentionState="'+msg.attentionState+'" busType="'+msg.type+'" busId="'+msg.id+'" describe="0" iconSize="sizeMd" onclick="changeAtten(\''+sid+'\',this)" style="font-size:18px;margin-right:5px;" >';
        					 }else{
        						 html +='\n	 <a href="javascript:void(0)"  attentionState="'+msg.attentionState+'" busType="'+msg.type+'" busId="'+msg.modId+'" describe="0" iconSize="sizeMd" onclick="changeAtten(\''+sid+'\',this)" style="font-size:18px;margin-right:5px;" >';
        					 }
        					 if(msg.attentionState==0){
        						 html +='\n <i class="fa fa-star-o sizeMd"></i>';
        					 }else{
        						 html +='\n <i class="fa fa-star ws-star sizeMd"></i>';
        					 }
        					 html +='\n		</a>';
        				 }
        				 html +='\n	 		</label>';
    					 html +='\n	 	</div>';
        				 html +='\n	 		['+msg.typeName +']';
        				 html +='\n			<time>'+msg.createDate+'</time>';
        				 html +='\n	</div>';
        				 html +='\n <div class="content-text">';
        				 if(msg.isDel=='0'){
        					 if(msg.type=='1' && msg.traceType=='0'){//普通
        						 html +='\n<a href="javascript:void(0)" onclick="msgShareView('+msg.id+',\''+msg.type+'\',\''+sid+'\');">';
        					 }else{
        						 html +='\n<a href="javascript:void(0)" onclick="msgShareView('+msg.modId+',\''+msg.type+'\',\''+sid+'\');">';
        					 }
        					 html +='\n'+HTMLDecode(msg.content);
        					 html +='\n	</a>';
        				 }else{
        					 html +='\n <i style="color:gray">'+HTMLDecode(msg.content)+'&nbsp;[已删除]</i>';
        				 }
 						 html +='\n		</div>';
        				 html +='\n	</div>';
        				 html +='\n</li>';
        			 }
        			 $("#moreMsg").before(html);
        			 //设置名片
        			 initCard(sid);
        		 }
        	 }
        	 
		  },
		  error:  function(XMLHttpRequest, textStatus, errorThrown){
			  window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
	      }
	});
}

var pageNum = 0;

/**
 * 查看更多(首页)
 * @param sid session标识
 * @param ts
 * @param creator session用户主键
 * @return
 */
function loadIndexMeinv(sid,ts,cardPlace) {
	$.ajax({
		type : "post",
		url : "/msgShare/nextPageSizeMsgs?sid="+sid+"&rnd="+Math.random(),
		dataType:"json",
		traditional :true, 
		data:{
		    pageSize:15,
            pageNum:++pageNum
		},
		  beforeSend: function(XMLHttpRequest){
         },
		  success : function(data){
        	if(data.length==0){
        		$("#moreMsg").hide();
        		return;
        	}
        	if(data.length<5){
     			$("#moreMsg").hide();
     		}else{
     			$("#moreMsg").show();
     		}
        	 for(var i=0;i<data.length;i++){
        		 var msg = data[i];

        		 if(undefined != $("#msg"+msg.id).attr("class")){
     				continue;
        		 }else{
        			 var modId = msg.modId;
        			 if(msg.type=='1' && msg.traceType=='0'){//普通
        				 modId = msg.id;
        			 }
        			 var html = '';
        			 html +='\n <li class="item first-item" id="msg'+msg.id+'">';
        			 html +='\n  <div class="message-content" style="margin-left: 35px">';
        			 html+='\n <img src="/downLoad/userImg/' + msg.comId + '/' + msg.creator + '.jpg" class="message-head" width="30" height="30" title="' + msg.creatorName + '"/>';
        			 if(msg.type=='004' && msg.isDel=='0'){//投票
        				 var vote = msg.vote;
        				 html +='\n	<div class="content-headline">';
        				 html +='\n		<div class="checkbox pull-left ps-chckbox">';
        				 html +='\n			<label>';
        				 html +='\n				<a href="javascript:void(0)" attentionState="'+msg.attentionState+'" busType="'+msg.type+'" busId="'+msg.modId+'" describe="0" iconSize="sizeMd" onclick="changeAtten(\''+sid+'\',this)" style="font-size:18px;margin-right:5px;" >';
        				 if(msg.attentionState==0){
    						 html +='\n 		<i class="fa fa-star-o sizeMd"></i>';
    					 }else{
    						 html +='\n 			<i class="fa fa-star ws-star sizeMd"></i> ';
    					 }
    					 html +='\n				</a>';
    					 html +='\n	 		</label>';
    					 html +='\n	 	</div>';
        				 html +='\n	 	['+msg.typeName +']';
        				 html +='\n		<time>'+msg.createDate+'</time>';
        				 html +='\n	</div>';
        				 html +='\n <div class="content-text" style="word-wrap: break-word; word-break: break-all;">';
        				 html +='\n     <a href="javascript:void(0)" onclick="indexMsgShareView('+msg.modId+',\''+msg.type+'\',\''+sid+'\');">';
						 html +='\n		  '+vote.voteContent+'';
						 html +='\n		</a>';
						 html +='\n	</div>';
        				 
        				 html +='\n	 <form id="voteForm'+vote.id+'">';
        				 html +=getHtml(vote,sid);
        				 html +='\n	 </form>';
        				 html +='\n	</div>';
        				 html +='\n</li>';
        				 
        			 }else{
        				 html +='\n	<div class="content-headline">';
        				 html +='\n		<div class="checkbox pull-left ps-chckbox">';
        				 html +='\n			<label>';
        				 if((msg.type=='1' || msg.type=='003' || msg.type=='004' || msg.type=='005' || msg.type=='080' || msg.type=='050'  || msg.type=='011' || msg.type=='012')&& msg.isDel=='0' && msg.traceType=='0'){
        					 if(msg.type=='1'){
        						 html +='\n				<a href="javascript:void(0)" attentionState="'+msg.attentionState+'" busType="1" busId="'+msg.modId+'" describe="0" iconSize="sizeMd" onclick="changeAtten(\''+sid+'\',this)" style="font-size:18px;margin-right:5px;" >';
        					 }else{
        						 html +='\n				<a href="javascript:void(0)" attentionState="'+msg.attentionState+'" busType="'+msg.type+'" busId="'+msg.modId+'" describe="0" iconSize="sizeMd" onclick="changeAtten(\''+sid+'\',this)" style="font-size:18px;margin-right:5px;" >';
        					 }
        					 if(msg.attentionState==0){
        						 html +='\n <i class="fa fa-star-o sizeMd"></i>';
        					 }else{
        						 html +='\n <i class="fa fa-star ws-star sizeMd"></i>';
        					 }
        					 html +='\n		</a>';
        				 }
        				 html +='\n	 		</label>';
    					 html +='\n	 	</div>';
        				 html +='\n	 		['+msg.typeName +']';
        				 html +='\n			<time>'+msg.createDate+'</time>';
        				 html +='\n	</div>';
        				 html +='\n <div class="content-text" style="word-wrap: break-word; word-break: break-all;">';
        				 if(msg.isDel=='0'){
        					 if(msg.type=='1' && msg.traceType=='0'){//普通
        						 html +='\n<a href="javascript:void(0)" onclick="indexMsgShareView('+msg.id+',\''+msg.type+'\',\''+sid+'\');">';
        					 }else{
        						 html +='\n<a href="javascript:void(0)" onclick="indexMsgShareView('+msg.modId+',\''+msg.type+'\',\''+sid+'\');">';
        					 }
        					 html +='\n'+HTMLDecode(msg.content);
        					 html +='\n	</a>';
        				 }else{
        					 html +='\n <i style="color:gray">'+HTMLDecode(msg.content)+'&nbsp;[已删除]</i>';
        				 }
 						 html +='\n		</div>';
        				 html +='\n	</div>';
        				 html +='\n</li>';
        			 }
        			 $("#moreMsg").before(html);
        			 //设置名片
        			 initCard(sid);
        		 }
        	 }
        	 
		  },
		  error:  function(XMLHttpRequest, textStatus, errorThrown){
			  window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
	      }
	});
}
/**
 * 删除消息分享
 * @param id
 * @param type
 * @param sid
 * @return
 */
function delShareMsg(id,type,sid){
	art.dialog.confirm("确定要删除该分享吗?", function(){//删除分享信息
		$.ajax({
			type : "post",
			url:"/msgShare/delShareMsg?sid="+sid+"&t="+Math.random(),
	        data:{"id":id,"type":type },
	        dataType: "json", 
			success : function(data){
	        	if(data.status=='y'){
	        		$("#msg"+id).remove();
	        		showNotification(0, "删除成功！");
	        	}
		    },
		    error:function(XmlHttpRequest,textStatus,errorThrown){
		    	showNotification(1, "系统错误，请联系管理人员！");
	        }
		});
	});
}

//添加评论
function addMsgTalk(msgId,talkPId,ts,type,sid){
	var value = $("#textarea_"+type+"_"+msgId).val();
	if(!value){
		art.dialog({"content":"请填写内容！"}).time(2);
		$("#textarea_"+type+"_"+msgId).focus();
		return;
	}
	if(type=='1'){//信息
		$.ajax({
			type : "post",
			url:"/msgShare/addMsgShareTalk?sid="+sid+"&t="+Math.random(),
		    beforeSend:function(XMLHttpRequest){
		        	$(ts).attr("disabled","disabled")
			},
	        data:{"msgId":msgId,
		        "parentId":talkPId,
		        "content":value
		       },
	        dataType: "json", 
	        traditional :true,
			success : function(data){
		    	   if(data.status=='y'){
		    		   showNotification(0,"评论成功！");
		    		   var msgTalk = data.msgTalk;
				       var html = talkStr(msgTalk,sid,data.sessionUser);
				       
				       var talkCount = $("#msgcount_"+type+"_"+msgId).html();
				       $("#msgcount_"+type+"_"+msgId).html(parseInt(talkCount)+1);
				       
				       $("#msgTalk_"+type+"_"+msgId).prepend(html);
		    		   $("#textarea_"+type+"_"+msgId).val('')
		    	   }else{
		    		   showNotification(1,"评论失败！");
		    	   }
		    	   $(ts).removeAttr("disabled");
	        },
	        error:  function(XMLHttpRequest, textStatus, errorThrown){
	        	showNotification(1,"系统错误，请联系管理员！");
	        	$(ts).removeAttr("disabled");
	        }
		});
	}
}
//拼接讨论的代码
function talkStr(msg,sid,sessionUser){
	var html = '';
	//存放讨论集合 
	html+='\n<div class="singleTalk" style="padding-top: 5px">';
	html+='\n	<div class="shareText" style="background-color: #f0f0f0;margin-right:0%">';
	html+='\n		<div class="ws-type" style="padding: 0px">';
	html+='\n			<span class="ws-blue" style="float: left">'+sessionUser.userName+'</span>';
	html+='\n			<time style="float:right">'+msg.recordCreateTime+'</time>';
	html+='\n		</div>';
	html+='\n		<p class="ws-texts">';
	html+='\n			'+HTMLDecode(msg.content);
	html+='\n		</p>';
	html+='\n	</div>	';
	html+='\n</div>';
	
	return html;
}

//字符串转义html
function HTMLDecode(text) {
    var temp = document.createElement("div");
    temp.innerHTML = text;
    var output = temp.innerText || temp.textContent;
    temp = null;
    return output;
}

//查看详情
function indexMsgShareView(id,type,sid) {
	if ('012' == type) {//客户
		authCheck(id,type,-1,function(authState){
			var url = "/crm/viewCustomer?sid="+sid+"&id="
			+ id + "&redirectPage="
			+ encodeURIComponent(window.location.href);
			openWinWithPams(url,"800px",($(window).height()-180)+"px");
		});
	} else if ('005' == type) {//项目
		authCheck(id,type,-1,function(authState){
			var url = "/item/viewItemPage?sid="+sid+"&id="
			+ id
			+ "&redirectPage="
			+ encodeURIComponent(window.location.href);
			openWinWithPams(url,"800px",($(window).height()-180)+"px");
		});
	} else if ('080' == type) {//产品
        authCheck(id,type,-1,function(authState){
            var url = "/product/viewProPage?sid="+sid+"&id="
                + id
                + "&redirectPage="
                + encodeURIComponent(window.location.href);
            openWinWithPams(url,"800px",($(window).height()-180)+"px");
        });
    } else if ('003' == type) {//任务
		authCheck(id,type,-1,function(authState){
			var url = "/task/viewTask?sid="+sid+"&id="
			+ id + "&redirectPage="
			+ encodeURIComponent(window.location.href);
			openWinWithPams(url,"800px",($(window).height()-180)+"px");
		});
	} else if ('006' == type) {//周报
		authCheck(id,type,-1,function(authState){
			var url = "/weekReport/viewWeekReport?sid="+sid+"&id="
			+ id
			+ "&redirectPage="
			+ encodeURIComponent(window.location.href);
			openWinWithPams(url,"800px",($(window).height()-180)+"px");
		});
	} else if ('050' == type) {//分享
        authCheck(id,type,-1,function(authState){
            var url = "/daily/viewDaily?sid="+sid+"&mouldViewType=1&id="
                + id
                + "&redirectPage="
                + encodeURIComponent(window.location.href);
            openWinWithPams(url,"800px",($(window).height()-180)+"px");
        });
    } else if ('004' == type) {//投票
		authCheck(id,type,-1,function(authState){
			var url = "/vote/voteDetail?sid="+sid+"&id="
			+ id + "&redirectPage="
			+ encodeURIComponent(window.location.href);
			openWinWithPams(url,"800px",($(window).height()-180)+"px");
		});
	} else if ('011' == type) {//问答
		var url = "/qas/viewQuesPage?sid="+sid+"&id=" + id
				+ "&redirectPage=" + encodeURIComponent(window.location.href);
		openWinWithPams(url,"800px",($(window).height()-180)+"px");
	} else if ('1' == type) {//普通分享
		var url = "/msgShare/msgShareViewPage?sid="+sid+"&id="
				+ id
				+ "&type="
				+ type
				+ "&redirectPage="
				+ encodeURIComponent(window.location.href);
		openWinWithPams(url,"800px",($(window).height()-180)+"px");
	}

}
/**
 * 添加分享
 * @return
 */
function addMsgShare(){
	$.ajax({
		 type : "post",
		  url : "/msgShare/ajaxAddMsgShare?sid="+sid+"&rnd="+Math.random(),
		  dataType:"json",
		  data:{content:$("#content").val(),idType:$("#idType").val()},
		  beforeSend: function(XMLHttpRequest){
			 	if(strIsNull($("#content").val())){
					showNotification(2,"请编辑分享内容！");
					$("#content").focus();
					return false;
				}else{
					var value = $('#content').val();
					var len = charLength(value.replace(/\s+/g,""));
					if(len%2==1){
						len = (len+1)/2;
					}else{
						len = len/2;
					}
					if(len>150){
						showNotification(2,"分享内容过长，请弹窗编辑！");
						return false;
					}
				}
			 	
				if(strIsNull($("#scopeTypeSel").val())){
					showNotification(2,"请编辑分享范围！");
					$("#scopeTypeSel").focus();
					return false;
				}
				//防止重复提交
				$("#ajaxSubmit").attr("disabled","disabled");
        },
        success : function(data){
       	 if(null!=data.msgShare){
       		var msg= data.msgShare;
       		//清除原来的
       		$("#msg" + msg.id).remove();
       		var html = getMsgShareHtml(msg)
       		$("#allmsgContainer").children(":first").before(html);
			$("#content").val("");
			$("#content").blur();
       	 }
       	 showNotification(1,"发布成功！");
       	 //可以再次提交
       	 $("#ajaxSubmit").removeAttr("disabled");
        },
        error:  function(XMLHttpRequest, textStatus, errorThrown){
       	 showNotification(2,"发布失败！");
       	 //可以再次提交
       	 $("#ajaxSubmit").removeAttr("disabled");
	     }
	});
}
/**
 *异步添加数据形式
 * @param msg
 * @return
 */
function getMsgShareHtml(msg){
	
	var html = '';
	var modId = msg.modId;
	var	id = msg.id;
	 html +='\n <li class="item first-item" id="msg'+msg.id+'">';
	 html +='\n  <div class="message-content" style="margin-left: 35px">';
	 html +='\n	<img src="/downLoad/userImg/' + msg.comId + '/' + msg.creator + '.jpg"';
     html +='\n 	class="message-head" width="30" height="30"';
     html +='\n 	title="' + msg.creatorName + '" />';
	 html +='\n	<div class="content-headline">';
	 html +='\n		<div class="checkbox pull-left ps-chckbox">';
	 html +='\n			<label>';
	 html +='\n	 <a href="javascript:void(0)" onclick="changeAtten(\'1\','+msg.id+','+msg.attentionState+',\''+sid+'\',this)" style="font-size:18px;margin-right:5px;" >';
	 html +='\n <i class="fa fa-star-o sizeMd"></i>';
	 html +='\n		</a>';
	 html +='\n	 		</label>';
	 html +='\n	 	</div>';
	 html +='\n	 		['+msg.typeName +']';
	 html +='\n			<time>'+msg.createDate+'</time>';
	 html +='\n	</div>';
	 html +='\n <div class="content-text"  style="word-wrap: break-word; word-break: break-all;">';
	 html +='\n <a href="javascript:void(0)" onclick="indexMsgShareView('+((msg.type==1 && msg.traceType==0)?id:modId)+',\''+msg.type+'\',\''+sid+'\');">';
	 html +='\n '+ HTMLDecode(msg.content) + '';
	 html +='\n	</a>';
	 html +='\n		</div>';
	 html +='\n	</div>';
	 html +='\n</li>';
	return html;
}
/**
 * 异步添加分享数据
 * @param msg
 * @return
 */
function appendMsgShare(msg){
	var html = getMsgShareHtml(msg)
	$("#allmsgContainer").children(":first").before(html);
}
/**
 * 弹窗添加信息分享
 * @return
 */
function popAddMsgShare(){
	var shareUrl = '/msgShare/addMsgSharePage?sid='+sid+'&idType='+$("#idType").val();
	tabIndex = layer.open({
	  type: 2,
	  title: false,
	  closeBtn: 0,
	  shadeClose: true,
	  shade: 0.3,
	  shift:0,
	  btn:['分享','关闭'],
	  scrollbar:false,
	  fix: true, //固定
	  maxmin: false,
	  move: false,
	  zIndex:1010,
	  area: ['650px', '450px'],
	  content: [shareUrl,'no'], //iframe的url
	  yes:function(index,layero){
		  var iframeWin = window[layero.find('iframe')[0]['name']];
		  iframeWin.addMsgShares();
	  },
	  success: function(layero,index){
		    var iframeWin = window[layero.find('iframe')[0]['name']];
		    iframeWin.setWindow(window.document,window);
		    iframeWin.setContent($("#content").val(),$("#scopeTypeSel").val());
	  },end:function(index){
		  //个人中心界面重新加载数据
		  if(pageTag){
			  if(pageTag.indexOf('selfCenter')>=0){
				  window.self.location.reload();
			  }else if(pageTag.indexOf('index')>=0){
				  $("#content").val('');
				  $("#content").blur();
				  $('#msgTitle').html("0/150");
			  }
		  }else{
			  layer.close(index);
		  }
		  
	  }
	});
}

