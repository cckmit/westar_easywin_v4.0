$(function(){
	msgshareFrom.loadData(0);
	msgshareFrom.initEvent();
	
})
var minId = $("#minId").val();
var preyear = 0;

function closeLoad(){
	if(loadDone){
		layui.use('layer', function() {
			layer.closeAll('loading');
		});
		clearInterval(intervalInt);
	}
	
}

var loadingIndex;
var loadDone=0;
var intervalInt;
var pageNum = 0;

var msgshareFrom = {
		initEvent:function(){
			$("body").on("click","#moreMsg",function(){
				msgshareFrom.loadData(1);
			});
			
			//文本框绑定回车提交事件
			$("#content").bind("keydown",function(event){
		        if(event.keyCode == "13"){
		        	if(!strIsNull($("#content").val())){
		        		msgshareFrom.loadData(0)
		        	}else{
		        		showNotification(1,"请输入检索内容！");
		    			$("#searchForm").focus();
		        	}
		        }
		    });
			$("#content").blur(function(){
				msgshareFrom.loadData(0)
			});
		},
		loadData:function(morestate){
			
			layui.use('layer', function() {
				loadingIndex = layer.load(0, {
					shade: [0.5,'#fff'] //0.1透明度的白色背景
				});
				
				intervalInt = setInterval("closeLoad("+loadingIndex+")",500);
			})
			
			if(!morestate){
				$("#minId").val(0)
				$("#msgsharelist").html('');
				preyear = 0;
				pageNum = 0;
			}
			msgshareFrom.loadMeinv(sid,function(data){
				
				$("#baseDiv").show();
				$("#noneDataDiv").remove();
				
				if(preyear == 0 && data.length==0){
					$("#baseDiv").hide();
					//没有查询到数据
					var nodataDiv = $('<div class="widget-body" style="height:400px; text-align:center;padding-top:100px"></div>');
					$(nodataDiv).attr("id","noneDataDiv");
					var section = $('<section class="error-container text-center"></section>');
					var info1 = $('<h1><i class="fa fa-exclamation-triangle"></i></h1>');
					var info2 = $('<div class="error-divider"><h2>都太腼腆了，暂无分享信息。</h2><p class="description">协同提高效率，分享拉近距离。</p></div>');
					
					$(section).append(info1);
					$(section).append(info2);
					$(nodataDiv).append(section);
					
					$("#baseDiv").after($(nodataDiv));
				}
				if(data.length==0){
					$("#moreMsg").hide();
					return;
				}
				if(data.length<5){
					$("#moreMsg").hide();
				}else{
					$("#moreMsg").show();
				}
				msgshareFrom.conStrHtml(data);
			});
		},
		loadMeinv:function (sid,callback) {
			loadDone = 0;
			var array = $("#searchForm").find(":checkbox[name='modTypes']:checked");
			//所选项
			var modTypes = new Array();
			if(array && array.length>0){
				for(var i=0;i<array.length;i++){
					modTypes.push($(array[i]).val());
				}
			}
			var minId = $("#minId").val();
			$.ajax({
				type : "post",
				url : "/msgShare/nextPageSizeMsgs?sid="+sid+"&rnd="+Math.random(),
				dataType:"json",
				traditional :true, 
				data:{pageSize:15,
                    pageNum:pageNum++,
					content:$("#content").val(),
					type:$("#type").val(),
					startDate:$("#startDate").val(),
					endDate:$("#endDate").val(),
					modTypes:modTypes,
					creator:$("#creator").val(),
					creatorType:$("#creatorType").val()
				},
				beforeSend: function(XMLHttpRequest){
				},
				success : function(data){
					loadDone = 1;
					callback(data)
				},
				error:  function(XMLHttpRequest, textStatus, errorThrown){
					window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
				}
			});
		},conStrHtml:function(data){
			var first = data[0];
			
			var firstyear = 2018;
			if(first.recordCreateTime){
				firstyear = first.recordCreateTime.substring(0,4);
			};
				
			var outUl = $("#msgsharelist").find("#historyUl_"+firstyear);
			if(!outUl || !outUl.get(0)){//没有指定年份的
				var outDiv = $('<div class="history-date"></div>');
				var outUl = $('<ul></ul>');
				$(outUl).attr("id","#historyUl_"+firstyear);
				$(outDiv).append($(outUl));
				$("#msgsharelist").append($(outDiv))
			}
			$.each(data,function(index,msg){
				if( minId ==0 ){
					$("#minId").val(msg.id);
				}else if(msg.id<minId ){
					minId = msg.id;
					$("#minId").val(minId); 
				}
				var msgYear = 2018;
				if(msg.recordCreateTime){
					msgYear = msg.recordCreateTime.substring(0,4);
				};
				if(preyear === 0 ){
					var showYear = $('<h2 class="first"><a href="javascript:void(0)">'+msgYear+'年</a></h2>');
					$(outUl).append(showYear);
					preyear = msgYear;
				}else if(preyear != msgYear){
					var showYear = $('<h2 class="date02"><a href="javascript:void(0)">'+msgYear+'年</a></h2>');
					$(outUl).append(showYear);
					preyear = msgYear;
				}
				var li = $('<li></li>');
				
				var h3 = $('<h3></h3>');
				var monthDay = '07';
				if(msg.recordCreateTime){
					monthDay = msg.recordCreateTime.substring(5,10).replace("-",".");
				}
				$(h3).append(monthDay);
				var subspan = $('<span></span>');
				$(subspan).append(msgYear);
				$(h3).append($(subspan));
				
				$(li).append($(h3));
				
				var dl = $('<dl></dl>');
				var dt = $('<dt></dt>');
				$(dt).css("word-break","break-all");
				$(dt).css("width","550px");
				
				$(dt).append(msg.creatorName+'['+msg.typeName +']');
				
				
				if(msg.type=='004' && msg.isDel=='0'){//投票
					 var vote = msg.vote;
					 var html ='\n <span class="content-text">';
    				 html +='\n     <a href="javascript:void(0)" onclick="msgShareView('+msg.modId+',\''+msg.type+'\',\''+sid+'\');">';
					 html +='\n		  '+vote.voteContent+'';
					 html +='\n		</a>';
					 html +='\n	</span>';
    				 
    				 html +='\n	 <form id="voteForm'+vote.id+'">';
    				 html +=getHtml(vote,sid);
    				 html +='\n	 </form>';
    				 $(dt).append($(html));
 					$(dl).append($(dt));
 					$(li).append($(dl));
 					$(outUl).append($(li));
				}else{
					var content = $('<span></span>');
					if(msg.isDel=='0'){
						var viewa = $('<a href="javascript:void(0)"></a>');
						$(viewa).addClass("blue");
						if(msg.type=='1' && msg.traceType=='0'){//普通
							$(viewa).attr("onclick",'msgShareView('+msg.id+',\''+msg.type+'\',\''+sid+'\')')
						}else{
							$(viewa).attr("onclick",'msgShareView('+msg.modId+',\''+msg.type+'\',\''+sid+'\')')
						}
						$(viewa).html(HTMLDecode(msg.content));
						$(content).append($(viewa));
					}
					$(content).append("<br/>"+msg.createDate);
					$(dt).append($(content));
					$(dl).append($(dt));
					$(li).append($(dl));
					$(outUl).append($(li));
				}
				
				
			})
		}	
}

//字符串转义html
function HTMLDecode(text) {
    var temp = document.createElement("div");
    temp.innerHTML = text;
    var output = temp.innerText || temp.textContent;
    temp = null;
    return output;
}

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
    voting+='\n <span class="ws-type">';
    voting+='\n	(总计'+vote.voteTotal+'票)';
  //是否匿名
    if(vote.voteType == 1){
    	voting+='	匿名';
    }
    voting+='\n 最多选'+vote.maxChoose+'项 <br/> 截止时间 &nbsp;'+vote.finishTime+':00';
    voting+='\n</span>';
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
//人员筛选
function userOneForUserIdCallBack(userId, userIdtag, userName, userNametag) {
	$("#" + userIdtag).val(userId);
	$("#" + userNametag).val(userName);
	msgshareFrom.loadData(0)
}


