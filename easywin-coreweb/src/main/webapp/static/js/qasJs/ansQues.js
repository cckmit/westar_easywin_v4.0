//页面刷新
$(function(){
	$("#refreshImg").click(function(){
		var win = art.dialog.open.origin;//来源页面
		// 如果父页面重载或者关闭其子对话框全部会关闭
		win.location.reload();
	});
});
//当状态改变的时候执行的函数 
function handleAnsContent(){
	var value = $('#ansContent4Add').val();
	var len = charLength(value.replace(/\s+/g,""));
	if(len%2==1){
		len = (len+1)/2;
	}else{
		len = len/2;
	}
	/*
	if(len>1500){
		$('#ansContent4Add').html("<font color='red'>文字太长，请删减</font>");
	}else{
		$('#ansMsgCont').html("");
	}
	*/
	resizeVoteH('otherIframe');
}
//样式设置
function setStyle(){
	$('.singleAnsCning').mouseover(function(){
		$(this).find(".cnAns").show();
	});
	$('.singleAnsCning').mouseout(function(){
		$(this).find(".cnAns").hide();
	});
	//文本框绑定回车提交事件
	$(".txtTalk").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('otherIframe')
    });
	//文本框绑定回车提交事件
	$(".ptalk").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('otherIframe')
    });
	$("#ansContent").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('otherIframe');
    });
	
	$(".txtTalk").autoTextarea({minHeight:60,maxHeight:150});
	$(".ptalk").autoTextarea({minHeight:60,maxHeight:150});
	$("#ansContent4Add").autoTextarea({minHeight:60,maxHeight:150}); 
	$("#ansContent4update").autoTextarea({minHeight:60,maxHeight:150}); 
	resizeVoteH('otherIframe');
}
/**
 * 回答问题
 * @param ts
 * @param quesId 问题
 * @param sid session标志
 * @param quesUser 提问人
 * @return
 */
function addAns(ts,quesId,sid,quesUser){
	//补充的问题的长度
	var contLen = $('#ansContent4Add').val().replace(/\s+/g,"").length;
	if(contLen>1500){
		layer.tips('回答内容太长！', "#ansContent4Add", {tips: 1});
		$('#ansContent4Add').focus();
		return;
	}else if(contLen==0){
		layer.tips('请填写回答内容！', "#ansContent4Add", {tips: 1});
		$('#ansContent4Add').focus();
		return;
	}
	var onclick=$(ts).attr("onclick");
	//防止重复提交
	 //异步提交表单
    $("#ansForm").ajaxSubmit({
        type:"post",
        url:"/qas/addAns?sid="+sid+"&t="+Math.random(),
        dataType: "json",
        beforeSubmit:function (a,f,o){
	    	if(!sumitPreCheck('ansForm')){
	    		return false;
	    	}else{
	    		$(ts).removeAttr("onclick");
	    	}
    	},
        traditional :true,
        data:{"fileIds":$("#listAnsFiles_id").val(),"content":$('#ansContent4Add').val(),"quesId":quesId},
        success:function(data){
	         var state = data.state;
	         $('#ansContent4Add').val(''); 
	         $("#listAnsFiles_id").html('');
	       //清除文件序列缓存
			$.each($("#thelistlistAnsFiles_id"),function(index,item){
				$(this).find(".cancel").click();
			})
	         if(state=='y'){
	        	 //回答
	        	 var answer = data.answer;
	        	 var sessionUser = data.sessionUser;
	        	 
	        	//清除准备回答
   		         $(".preAnsQues").css("display","none");
   		         
	        	 //用于拼接的回答HTML
	        	 var html=getAddAnsHtml(answer,sessionUser,sid,quesUser,data.content);
	        	 
	        	 
	        	 var cnAns = $("#cnAns").val();
	        	 //待采纳的答案数
	        	 var singleAnsCning = $(".singleAnsCning").length;
	        	 if(cnAns>0&& singleAnsCning==0){
	        		 $(".cningHead").show();
	        	 }
	        	 $(".cningHead").after(html);
	        		
	        	 setStyle();
	   		     resizeVoteH('otherIframe')
	   		     
	   		     initCard(sid);
	   		     
	         }
			//可以再次提交
			$(ts).attr("onclick",onclick);
        },
        error:function(XmlHttpRequest,textStatus,errorThrown){
			//可以再次提交
        	$(ts).attr("onclick",onclick);
        }
    });
}
/**
 * 删除回答
 * @param ts
 * @param ansId 回答主键
 * @param quesId提问主键
 * @param sid session标志
 * @param quesUser 提问人
 * @return
 */
function delAns(ts,ansId,quesId,sid,quesUser){
	var onclickSrc = $(ts).attr("onclick");
	window.top.layer.confirm("确定删除回答?",{
		btn: ['确定','取消']//按钮
	  ,title:'询问框'
	  ,icon:3
	}, function(index){
		window.top.layer.close(index);
		 //异步删除提问
	    $("#ansForm").ajaxSubmit({
	        type:"post",
	        url:"/qas/delAns?sid="+sid+"&t="+Math.random(),
	        dataType: "json", 
	        data:{"ansId":ansId,"quesId":quesId},
	        beforeSubmit:function(){
	        	$(ts).removeAttr("onclick");
	        },
	        success:function(data){
		        if(data.state=='y'){
		        	 $(window.parent.ansNum).val(0)
		        	 
		        	$("#singleAns"+ansId).remove();
		        	$(".preAnsQues").css("display","block");
		        	
		        	 var cnings = $(".singleAnsCning").lenght;
		        	 if(cnings==0){//原来有采纳，当前页没有待采纳答案
		        		 $(".cningHead").css("display","none");
		        	 }
		        	 setStyle();
		        	 resizeVoteH('otherIframe')
		        }else{
			        $(ts).attr("onclick",onclickSrc);
		        }
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
		        $(ts).attr("onclick",onclickSrc);
	        }
	    });
	   });
}

/**
 * 修改回答代码
 * @param ansId 回答主键
 * @return
 */
function appandAns(ansId){
	var display = $(".updateContent").css("display");
	if(display=='none'){
		$(".updateContent").show();
		$("#ansContent4update").focus();
	}else{
		$(".updateContent").hide();
	}
	$("#talk4Ques"+ansId).hide();
	resizeVoteH('otherIframe')
	setStyle();
	
	
}
/**
 * 修改回答
 * @param ts
 * @param quesId 问题主键
 * @param ansId 回答主键
 * @param sid session标志
 * @param quesUser 提问人
 * @return
 */
function updateAns(ts,quesId,ansId,sid,quesUser){
	//补充的问题的长度
	var contLen = $('#ansContent4update').val().replace(/\s+/g,"").length;
	if(contLen>1500){
		layer.tips('回答内容太长！', "#ansContent4Add", {tips: 1});
		$('#ansContent4update').focus();
		return;
	}else if(contLen==0){
		layer.tips('请填写回答内容！', "#ansContent4Add", {tips: 1});
		$('#ansContent4update').focus();
		return;
	}
	var onclick = $(ts).attr("onclick");
	 //异步提交表单
    $("#ansForm").ajaxSubmit({
        type:"post",
        url:"/qas/updateAns?sid="+sid+"&t="+Math.random(),
        dataType: "json",
        beforeSubmit:function (a,f,o){
	    	if(!sumitPreCheck('ansForm')){
	    		return false;
	    	}else{
	    		$(ts).removeAttr("onclick");
	    		return true;
	    	}
		},
        data:{"id":ansId,"quesId":quesId,"fileIds":$("#listAnsFilesUpdate_id").val(),content:$('#ansContent4update').val()},
        traditional :true,
        success:function(data){
	         var state = data.state;
	         if(state=='y'){
	        	 //回答
	        	 var answer = data.answer;
	        	 var sessionUser = data.sessionUser;
	        	 //用于拼接的回答HTML
	        	var ansHtml=getAnsHtml(answer,sessionUser,sid,quesUser,data.content);
	        	 //显示回答的
		         $("#singleAns"+answer.id).html(ansHtml);
		         //对回答的评论
		         var talkHtml = getAnsTalkHtml(answer,sessionUser,sid);
		         $("#alltalks"+answer.id).html(talkHtml);
		         setStyle();
		         resizeVoteH('otherIframe');
		         
		         initCard(sid);
	         }
			//可以再次提交
			$(ts).attr("onclick",onclick);
        },
        error:function(XmlHttpRequest,textStatus,errorThrown){
			//可以再次提交
			$(ts).attr("onclick",onclick);
        }
    });
}

/**
 * 开始评价
 * @param ansId 回答主键
 * @param ts
 * @return
 */
function talk4Ques(ansId,ts){
	var talk4QuesP = $(".talk4Ques").parent();
	
	$(".fa-comment").attr("title","评论");
	$(".fa-comment").attr("class","fa fa-comment-o");
	
	for(var i=0;i<talk4QuesP.length;i++){
		var id = $(talk4QuesP[i]).attr("id").replace("talk4Ques","");
		if(ansId==id){
			var display = $("#talk4Ques"+ansId).css("display");
			if(display=='none'){
				$("#talk4Ques"+ansId).show();
				$("#img_"+id).attr("title","隐藏");
				$("#img_"+id).attr("class","fa fa-comment");
			}else{
				$("#talk4Ques"+ansId).hide();
				$("#img_"+id).attr("title","评论");
				$("#img_"+id).attr("class","fa fa-comment-o");
			}
		}else{
			$("#talk4Ques"+id).hide();
			$("#img_"+id).attr("title","评论");
			$("#img_"+id).attr("class","fa fa-comment-o");
			
		}
	}
	
	$(".updateContent").hide()
	resizeVoteH('otherIframe')
}
/**
 * 显示文本域用于回复
 * @param ansId
 * @param talkId
 * @return
 */
function showArea(ansId,talkId){
	var addtalks = $(".addtalk");
	
	$(".fa-comment").attr("title","回复");
	$(".fa-comment").attr("class","fa fa-comment-o");
	
	for(var i=0;i<addtalks.length;i++){
		var anstalkId = $(addtalks[i]).attr("id");
		var imgId = anstalkId.replace("addTalk","");
		if(anstalkId=="addTalk_"+ansId+"_"+talkId){
			if($("#addTalk_"+ansId+"_"+talkId).css('display')=="none"){
				$("#addTalk_"+ansId+"_"+talkId).show();
				$("#img"+imgId).attr("title","隐藏");
		     	$("#img"+imgId).attr("class","fa fa-comment");
				resizeVoteH('otherIframe')
			}else{
				$("#addTalk_"+ansId+"_"+talkId).hide();
				$("#img"+imgId).attr("title","回复");
		     	$("#img"+imgId).attr("class","fa fa-comment-o");
				resizeVoteH('otherIframe')
			}
		}else{
			$("#addTalk"+imgId).hide();
			$("#img"+imgId).attr("title","回复");
			$("#img"+imgId).attr("class","fa fa-comment-o");
			resizeVoteH('otherIframe')
		}
		
	}
}
/**
 * 提交评论
 * @param quesId 问题主键
 * @param ansId 回答主键
 * @param talkPId 评论的fuID
 * @param ts
 * @param ptalker 针对的评论人
 * @param sid session标志
 * @return
 */
function addTalk(quesId,ansId,talkPId,ts,ptalker,sid){
	
	var textarea = $("#operaterReplyTextarea_"+ansId+"_"+talkPId);
	var content = $(textarea).val();
	if(""==content){
		layer.tips('请填写内容！', $("#operaterReplyTextarea_"+ansId+"_"+talkPId), {tips: 1});
		$(textarea).focus();
		return;
	}else{
		//异步提交表单 讨论
	    $("#ansForm").ajaxSubmit({
	        type:"post",
	        url:"/qas/addAnsTalk?sid="+sid+"&t="+Math.random(),
	        data:{"quesId":quesId,
		    	"ansId":ansId,
		    	"parentId":talkPId,
		    	"talkContent":content,
		    	"ptalker":ptalker,
		    	"upfilesId":$("#listUpfiles_"+ansId+"_"+talkPId+"_upfileId").val()
	    	},
	    	traditional :true,
	        dataType: "json", 
	        beforeSubmit:function(a,o,f){
	    		$(ts).attr("disabled","disabled");
	    	},
	        success:function(data){
	        	if(data.status=='y'){
	        		$(textarea).val('');
	        		
	        		var ansTalk = data.ansTalk;
	        		var html = getAnsTalkString(ansTalk,sid,data.sessionUser);
	        		var talkNumStr = $("#img_"+ansId).html();
	        		if(talkNumStr){
	        			var talkNum = parseInt(talkNumStr.substring(talkNumStr.indexOf("[")+1,talkNumStr.indexOf("]")))+1;
	        			$("#img_"+ansId).html("["+talkNum+"]");
	        		}else{
	        			$("#img_"+ansId).html("[1]");
	        		}
	        		if(talkPId==-1){
	        			$("#alltalks"+ansId).prepend(html);
	        		}else{
	        			//父节点
	        			$("#delTalk_"+ansId+"_"+talkPId).attr("onclick","delAnsTalk("+quesId+","+ansId+","+talkPId+",0,'"+sid+"')")
	        			$("#talk"+talkPId).after(html);
	        			$("#addTalk_"+ansId+"_"+talkPId).hide();
	        			$("#img_"+ansId+"_"+talkPId).attr("src","/static/images/say.png");
	        		}
	        		setStyle();
	        		$(ts).removeAttr("disabled");
	        		$("#listUpfiles_"+ansId+"_"+talkPId+"_upfileId").html('');
	        		//清除文件序列缓存
					$.each($("#thelistlistUpfiles_"+ansId+"_"+talkPId+"_upfileId"),function(index,item){
						$(this).find(".cancel").click();
					})
	        		resizeVoteH('otherIframe');
	        		$(".fa-comment").attr("title","回复");
	        		$(".fa-comment").attr("class","fa fa-comment-o");
	        		
	        		initCard(sid);
	        	}
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
				$(textarea).html('');
				$(ts).removeAttr("disabled");
	        }
	    });
	}
}

/**
 * 删除评论
 * @param quesId 问题主键
 * @param ansId 回答主键
 * @param talkId 评论主键
 * @param isLeaf 是否为叶子 0 是1 非
 * @param sid
 * @return
 */
function delAnsTalk(quesId,ansId,talkId,isLeaf,sid){
	window.top.layer.confirm("确定要删除该评论吗?", {
		btn: ['确定','取消']//按钮
	  ,title:'询问框'
	  ,icon:3
	}, function(index){//删除叶子节点
		window.top.layer.close(index);
		if(isLeaf==1){//是子节点，直接删除，不用重新加载页面
			//异步提交表单删除评论
		    $("#ansForm").ajaxSubmit({
		        type:"post",
		        url:"/qas/delAnsTalk?sid="+sid+"&t="+Math.random(),
		        data:{"id":talkId,"quesId":quesId,"ansId":ansId},
		        dataType: "json", 
		        success:function(data){
		        	if(data.status=='y'){
		        		var clz = $("#talk"+talkId).attr("class");
		        		if(clz.indexOf('voteTalkInfoP')>=0){//是评论，直接删除
		        			$("#talk"+talkId).remove();
		        			$("#addTalk_"+ansId+"_"+talkId).remove();
		        			resizeVoteH('otherIframe')
		        			
		        			var talkNumStr = $("#img_"+ansId).html();
			        		var talkNum = parseInt(talkNumStr.substring(talkNumStr.indexOf("[")+1,talkNumStr.indexOf("]")))-1;
			        		$("#img_"+ansId).html("["+talkNum+"]")
		        		}else{
		        			var parentId = $("#talk"+talkId).attr("parentId");
		        			$("#talk"+talkId).remove();
		        			$("#addTalk_"+ansId+"_"+talkId).remove()
		        			var alltalks = $("#alltalks"+ansId).find(".voteTalkInfo");
		        			//父节点是否还有子节点，默认没有了
		        			var flag = false;
		        			for(var i=0;i<alltalks.length;i++){
		        				if($(alltalks[i]).attr("parentId")==parentId){
		        					flag = true;
		        					break;
		        				}
		        			}
		        			if(!flag){//没有了子节点，修改父节点状态
		        				$("#delTalk_"+ansId+"_"+parentId).attr("onclick","delAnsTalk("+quesId+","+ansId+","+parentId+",1,'"+sid+"')");;
		        			}
		        			resizeVoteH('otherIframe');
		        			
		        			var talkNumStr = $("#img_"+ansId).html();
			        		var talkNum = parseInt(talkNumStr.substring(talkNumStr.indexOf("[")+1,talkNumStr.indexOf("]")))-1;
			        		$("#img_"+ansId).html("["+talkNum+"]")
		        		}
		        	}
		        	
		        },
		        error:function(XmlHttpRequest,textStatus,errorThrown){
		        }
		    });
		}else{//不是子节点
			setTimeout(function(){
				window.top.layer.confirm("是否需要删除此节点下的评论信息?",{
					btn: ['是','否']//按钮
				  ,title:'询问框'
				  ,icon:3
				}, function(index){//删除自己和子节点
					window.top.layer.close(index);
					//异步提交表单删除评论
				    $("#ansForm").ajaxSubmit({
				        type:"post",
				        url:"/qas/delAnsTalk?sid="+sid+"&t="+Math.random(),
				        data:{"id":talkId,"quesId":quesId,"ansId":ansId,"delChildNode":"yes"},
				        dataType: "json", 
				        success:function(data){
				        	if(data.status=='y'){
				        		for(var i=0;i<data.childIds.length;i++){
				        			$("#talk"+data.childIds[i]).remove();
				        		}
				        		resizeVoteH('otherIframe');
				        		
				        		var talkNumStr = $("#img_"+ansId).html();
				        		var talkNum = parseInt(talkNumStr.substring(talkNumStr.indexOf("[")+1,talkNumStr.indexOf("]")))-data.childIds.length;
				        		$("#img_"+ansId).html("["+talkNum+"]")
				        	}
				        },
				        error:function(XmlHttpRequest,textStatus,errorThrown){
				        }
				    });
				},function(){
					//异步提交表单删除评论
				    $("#ansForm").ajaxSubmit({//删除自己
				        type:"post",
				        url:"/qas/delAnsTalk?sid="+sid+"&t="+Math.random(),
				        data:{"id":talkId,"quesId":quesId,"ansId":ansId,"delChildNode":"no"},
				        dataType: "json", 
				        success:function(data){
				        	if(data.status=='y'){
				        		var clz = $("#talk"+talkId).attr("class");
				        		var parentId = $("#talk"+talkId).attr("parentId");
				        		$("#talk"+talkId).remove();
				        		resizeVoteH('otherIframe')
				        		if(clz.indexOf('voteTalkInfoP')>=0){//是评论，直接删除
				        			var alltalks = $("#alltalks"+ansId).find(".voteTalkInfo");
				        			for(var i=0;i<alltalks.length;i++){//调整节点结构
				        				if($(alltalks[i]).attr("parentId")==talkId){
				        					$(alltalks[i]).attr("class","ws-wrap ws-wrap-width voteTalkInfoP");
				        					$(alltalks[i]).attr("parentId",-1);
				        					
				        					
				        					$(alltalks[i]).find(".ws-wrap-in .ws-wrap-right .repSpan").remove();
				        					setStyle();
				        				}
				        			}
				        			
				        		}else{//调整借节点结构
				        			var alltalks = $("#alltalks"+ansId).find(".voteTalkInfo");
				        			for(var i=0;i<alltalks.length;i++){
				        				if($(alltalks[i]).attr("parentId")==talkId){
				        					$(alltalks[i]).attr("parentId",parentId);
				        				}
				        			}
				        		}
				        		resizeVoteH('otherIframe');
				        		 
				        		var talkNumStr = $("#img_"+ansId).html();
					        	var talkNum = parseInt(talkNumStr.substring(talkNumStr.indexOf("[")+1,talkNumStr.indexOf("]")))-1;
					        	$("#img_"+ansId).html("["+talkNum+"]")
				        	}
				        },
				        error:function(XmlHttpRequest,textStatus,errorThrown){
				        }
				    });
				});
			},200);
		}
	});	
}

/**
 * 采纳答案
 * @param quesId 问题主键
 * @param ansId 回答主键
 * @param sid
 * @return
 */
function cnAnsFun(quesId,ansId,sid){
	window.top.layer.confirm("确定要采纳该答案吗?", {
		btn: ['确定','取消']//按钮
	  ,title:'询问框'
	  ,icon:3
	}, function(index){
		window.top.layer.close(index);
		//异步提交表单删除评论
	    $("#ansForm").ajaxSubmit({
	        type:"post",
	        url:"/qas/cnAns?sid="+sid+"&t="+Math.random(),
	        data:{"quesId":quesId,"ansId":ansId},
	        dataType: "json", 
	        success:function(data){
	        	if(data.state=='y'){
	        		$(window.parent.cnAns).val(ansId)
	        		
	        		var cnAns = $("#cnAns").val();
	        		var cnedHead = $(".cnedHead").css("display");
	        		//待采纳的答案数
	        		var singleAnsCning = $(".singleAnsCning").length;
	        		if(cnAns==0){//没有采纳的答案
	        			if(singleAnsCning==1){//只有一个答案
	        				$(".cnedHead").show();
	        				//本次的
	        				$("#singleAns"+ansId).find(".wsOpt").hide();
	        				$("#singleAns"+ansId).find(".updateContent").hide();
	        				$("#singleAns"+ansId).find(".cnAns").hide();
	        				$("#singleAns"+ansId).find(".cnAns").attr("class","cnAnsYes");
	        				$("#singleAns"+ansId).attr("class","singleAnsCned");
	        			}else{
	        				$(".cnedHead").show();
	        				$(".cningHead").show();
	        				
	        				//本次的
	        				$("#singleAns"+ansId).find(".wsOpt").hide();
	        				$("#singleAns"+ansId).find(".updateContent").hide();
	        				$("#singleAns"+ansId).find(".cnAns").hide();
	        				$("#singleAns"+ansId).find(".cnAns").attr("class","cnAnsYes");
	        				$("#singleAns"+ansId).attr("class","singleAnsCned");
	        				
	        				var cnAnsHtmling='\n'+$("#singleAns"+ansId).prop('outerHTML');
	        				$("#singleAns"+ansId).remove();
	        				$(".cnedHead").after(cnAnsHtmling);
	        			}
	        			
	        		}else{
	        			if(cnedHead=='none'){//有采纳的答案，但是没有显示
	        				if(singleAnsCning==1){//只有一个答案
	        					$(".cnedHead").show();
	        					$(".cningHead").hide();
	        					//本次的
	        					$("#singleAns"+ansId).find(".wsOpt").hide();
	        					$("#singleAns"+ansId).find(".updateContent").hide();
	        					$("#singleAns"+ansId).find(".cnAns").hide();
	        					$("#singleAns"+ansId).find(".cnAns").attr("class","cnAnsYes");
	        					$("#singleAns"+ansId).attr("class","singleAnsCned");
	        					
	        					var cnAnsHtmling='\n'+$("#singleAns"+ansId).prop('outerHTML');
	        					$("#singleAns"+ansId).remove();
	        					$(".cnedHead").after(cnAnsHtmling);
	        				}else{
	        					$(".cnedHead").show();
	        					//本次的
	        					$("#singleAns"+ansId).find(".wsOpt").hide();
	        					$("#singleAns"+ansId).find(".updateContent").hide();
	        					$("#singleAns"+ansId).find(".cnAns").hide();
	        					$("#singleAns"+ansId).find(".cnAns").attr("class","cnAnsYes");
	        					$("#singleAns"+ansId).attr("class","singleAnsCned");
	        					
	        					var cnAnsHtmling=$("#singleAns"+ansId).prop('outerHTML');
	        					$("#singleAns"+ansId).remove();
	        					$(".cnedHead").after(cnAnsHtmling);
	        					
	        				}
	        			}else{//采纳的答案显示，并且有待选答案
	        				var orgId = $(".singleAnsCned").attr("id")
	        				
	        				//本次的
	        				//不能删除和修改操作
        					$("#singleAns"+ansId).find(".wsOpt").hide();
	        				//隐藏修改
        					$("#singleAns"+ansId).find(".updateContent").hide();
        					$("#singleAns"+ansId).find(".cnAns").hide();
        					$("#singleAns"+ansId).find(".cnAns").attr("class","cnAnsYes");
        					$("#singleAns"+ansId).attr("class","singleAnsCned");
	        					
	    	        		//以前的
        					//恢复删除和修改操作
        					$("#"+orgId).find(".wsOpt").show();
        					$("#"+orgId).find(".cnAnsYes").attr("class","cnAns");
        					$("#"+orgId).attr("class","singleAnsCning");
        					
        					var cnAnsHtmling=$("#singleAns"+ansId).prop('outerHTML')
        					var cnAnsHtmled=$("#"+orgId).prop('outerHTML');
        					$("#singleAns"+ansId).remove();
        					$("#"+orgId).remove();
        					
        					$(".cnedHead").after(cnAnsHtmling);
        					$(".cningHead").after(cnAnsHtmled);
	        			}
	        		}
	        		$("#cnAns").val(ansId);
	        		
	        		setStyle();
	        		resizeVoteH('otherIframe');
	        		
	        	}
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        }
	    });
	});
}

/**
 * 用于拼接的回答HTML
 * @param ans 回答对象
 * @param sessionUser 操作员对象
 * @param sid
 * @param quesUser 提问人
 * @param content 评论内容
 * @return
 */
function getAddAnsHtml(ans,sessionUser,sid,quesUser,content){
	var html='';
	html+='\n <div id="singleAns'+ans.id+'" class="singleAnsCning">';
	html+='\n'+getAnsHtml(ans,sessionUser,sid,quesUser,content);
	html+='\n </div>';
	return html;
	
}
/**
 * 删除自己的回答后。可以重新回答
 * @param quesId 问题主键
 * @param sid
 * @param quesUser提问人
 * @return
 */
function getPreAnsHtml(quesId,sid,quesUser){
	ansNum=0;
	var html='';
	html+='\n<div class="ws-clear">';
	html+='\n		<textarea id="ansContent" name="content" class="ws-write-area" style="overflow-y: hidden;"></textarea>';
	html+='\n		<a href="javascript:void(0);" onclick="addAns(this,'+quesId+',\''+sid+'\','+quesUser+')" class="ws-green ws-area-btn">回答</a>';
	html+='\n	</div>';
	html+='\n	<div class="ws-other-select">';
	html+='\n		<div class="ws-expression">';
	html+='\n			<a href="javascript:void(0);" id="biaoQingSwitch" class="tigger" onclick="addBiaoQingObj(\'biaoQingSwitch\',\'biaoQingDiv\',\'ansContent\');">添加表情</a>';
	html+='\n		</div>';
/*	html+='\n		<div class="ws-range ws-range3-1">';
	html+='\n			<span style="float:left;">提醒方式：</span>';
	html+='\n			<label style="float:left;">短信<input style="margin-left:3px;" type="checkbox" name="" id="" value="" /></label>';
	html+='\n			<label style="float:left;margin-left:10px;">邮件<input style="margin-left:3px;" type="checkbox" name="" id="" value="" /></label>';
	html+='\n		</div>';*/
	html+='\n		<div style="clear:both;position: relative;">';
	//附件层
	html+='\n			<div class="ws-fileUpload" style="overflow:hidden;">';
	html+='\n				<div style="float:left">';
	html+='\n					附件:';
	html+='\n				</div>';
	html+='\n				<div style="float:left">';
	//存放上传的附件，系统默认 
	html+='\n					 <div class="upfileList_filelistAnsFiles_id">';
	html+='\n					 </div>';
	
	html +='\n 					<div style="clear:both">';
	html +='\n						<div id="fileQueuelistAnsFiles_id" style="width: 300px">';
	html +='\n						</div>';
	html +='\n						<div style="float:left">';
	html +='\n		   					<input type="file" name="filelistAnsFiles_id" id="filelistAnsFiles_id"/>';
	html +='\n						</div>';
	html +='\n						<script type="text/javascript">';
	html +='\n							loadUpfiles(\'listAnsFiles_id\',\''+sid+'\',\'otherIframe\',\''+$("#comId").val()+'\');';
	html +='\n						</script>';
	html +='\n						<div style="position: relative; width: 350px; height: 90px;display: none">';
	html +='\n							<div style="float: left; width: 250px">';
	html +='\n								<select  list="listAnsFiles" listkey="id" listvalue="filName" id="listAnsFiles_id" name="listAnsFiles.id" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">';
	html +='\n			 					</select>';
	html +='\n							</div>';
	html +='\n						</div>';
	html +='\n					</div>';
	
	
	html+='\n				</div>';
	html+='\n			</div>';
	//表情DIV层
	html+='\n			<div id="biaoQingDiv" class="blk" style="display:none;position: absolute;top: -40px;left: 10px">';
	html+='\n		        <div class="main">';
	html+='\n		            <ul>';
	html+='\n		            '+getBiaoqing();
	html+='\n		            </ul>';
	html+='\n		        </div>';
	html+='\n		    </div>';
	html+='\n		</div>';
	html+='\n	</div>';
	return html;
}

/**
 * 对回答的评论
 * @param answer
 * @param sessionUser
 * @param sid
 * @return
 */
function getAnsHtml(ans,sessionUser,sid,quesUser,content){
	var html='';
	html+='\n	<div class="ws-shareBox">';
	//头像信息
	html+='\n		<div class="shareHead" data-container="body" data-toggle="popover" data-user='+ans.userId+' data-busId='+ans.quesId+' data-busType=\'011\'>';
	html += '		<img src="/downLoad/userImg/'+ans.comId+'/'+ans.userId+'?sid='+sid+'" title="'+ans.userName+'"></img>\n';
	html+='\n		</div>';
	//头像信息结束
	html+='\n 		<div class="shareText">';
	html+='\n 			<div style="position: relative;">';
	html+='\n				<span class="ws-blue">'+ans.userName;
	html+='\n					<font color="#000" style="padding-left:10px">[回答]</font>';
	html+='\n				</span>';
	if(quesUser==sessionUser.id){
		html+='\n				<span style="position: absolute;right:0;top:0;display:none" class="cnAns">';
		html+='\n					<a href="javascript:void(0)" style="color:green" onclick="cnAnsFun('+ans.quesId+','+ans.id+',\''+sid+'\')">采纳 </a>';
		html+='\n				</span>';
	}
	html+='\n			</div>';
	html+='\n			<p class="ws-texts" style="clear: both;">'+content+'</p>';
	//附件
	if(ans.listAnsFiles.length>0){
		html+='\n 		<div class="file_div">';
		for(var i=0;i<ans.listAnsFiles.length;i++){
			var upfiles = ans.listAnsFiles[i];
			html+='\n		<p class="p_text">附件('+(i+1)+')';
			if(upfiles.isPic==1){
				html+='\n		<img onload="AutoResizeImage(350,0,this,\'otherIframe\')"';
				html+='\n			src="/downLoad/down/'+upfiles.orgFileUuid+'/'+upfiles.orgFileName+'?sid='+sid+'" />';
				html+='\n		<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/'+upfiles.orgFileUuid+'/'+upfiles.orgFileName+'?sid='+sid+'" title="下载"></a>';
				html+='\n		<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="showPic(\'/downLoad/down/'+upfiles.orgFileUuid+'/'+upfiles.orgFileName+'\',\''+sid+'\',\''+upfiles.original+'\',\'011\',\''+ans.quesId+'\')"></a>';
			}else{
				html+='\n		'+upfiles.orgFileName;
				var fileExt = upfiles.fileExt;
				if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx'){
					html+='\n	<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad(\''+upfiles.orgFileUuid+'\',\''+upfiles.orgFileName+'\',\''+sid+'\')"></a>';
					html+='\n	<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.original+'\',\''+upfiles.orgFileUuid+'\',\''+upfiles.orgFileName+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'011\',\''+ans.quesId+'\')"></a>';
				}else if(fileExt=='txt' ||fileExt=='pdf'){
					html+='\n	<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/'+upfiles.orgFileUuid+'/'+upfiles.orgFileName+'?sid='+sid+'" title="下载"></a>';
					html+='\n	<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.original+'\',\''+upfiles.orgFileUuid+'\',\''+upfiles.orgFileName+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'011\',\''+ans.quesId+'\')"></a>';
				}else{
					html+='\n	<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad(\''+upfiles.orgFileUuid+'\',\''+upfiles.orgFileName+'\',\''+sid+'\')"></a>';
				}
				
			}
			html+='\n		</p>';
		}
		html+='\n		</div>';
	}
	//附件结束
	html+='\n 			<div class="ws-type" >';
	html+='\n 				<span class="wsOpt" style="float:left; display:block">';
	html+='\n					<a href="javascript:void(0);" style="padding-right: 15px" onclick="appandAns('+ans.id+')" class="fa fa-edit" title="修改回答"></a>';
	html+='\n					<a href="javascript:void(0);" style="padding-right: 15px" onclick="delAns(this,'+ans.id+','+ans.quesId+',\''+sid+'\','+quesUser+')" class="fa fa-trash-o" title="删除"></a>';
	html+='\n 				</span>';
	html+='\n 				<span>';
	html+='\n 					<a id="img_'+ans.id+'" name="replyImg" onclick="talk4Ques('+ans.id+',this)" href="javascript:void(0);" class="fa fa-comment-o" title="评论"></a>';
	html+='\n					<time>'+ans.recordCreateTime.substr(0,16)+'</time>';
	html+='\n 				</span>';
	html+='\n			</div>';
	html+='\n		</div>';
	//只有问题的回答人，在非采纳的状态下才可以修改
	html+='\n 		<div class="ws-clear"></div>';
	html+='\n  		<div class="shareText updateContent" style="display: none">';
	html+='\n			<div class="ws-textareaBox" style="margin-top:10px;">';
	html+='\n 				<textarea id="ansContent4update" class="form-control" style="height: 60px">'+ans.content+'</textarea>';
	html+='\n			</div>';
	html+='\n			<div class="ws-otherBox">';
	html+='\n				<div class="ws-meh" style="position: relative;">';
	//表情
	html+='\n 					<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitchUpdate" onclick="addBiaoQingObj(\'biaoQingSwitchUpdate\',\'biaoQingDivUpdate\',\'ansContent4update\');"></a>';
	html+='\n 					<div id="biaoQingDivUpdate" class="blk" style="display:none;position:absolute;width:200px;top:10px;z-index:99;left: 1px">';
	//表情DIV层
	html+='\n				     	<div class="main">';
	html+='\n				           	<ul style="padding: 0px">';
	html+=' \n'+getBiaoqing();
	html+='\n				           	</ul>';
	html+='\n				       	</div>';
	html+='\n				     </div>';
	html+='\n				</div>';
	//常用意见
	html+='\n				<div class="ws-plugs">';
	html+='\n					<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea(\'ansContent4update\',\''+sid+'\');" title="常用意见"></a>';
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
	html+='\n				</div>';
	html+='\n				<div style="clear: both;"></div>';*/
	//附件开始
	html+='\n				<div class="ws-notice">';
	
	html+='\n					<div style="clear:both;width: 300px" class="wu-example">';
	html+='\n						<div style="width: 300px;" id="thelistlistAnsFilesUpdate_id">';
	if(ans.listAnsFiles.length>0){
		for(var i=0;i<ans.listAnsFiles.length;i++){
			//已有附件
			var ansFile = ans.listAnsFiles[i];
			
			html+='\n					<div id="wu_file_0_-'+ansFile.original+'" class="uploadify-queue-item">	';
			html+='\n						<div class="cancel">';
			html+='\n							<a href="javascript:void(0)" fileDone="1" fileId="'+ansFile.original+'">X</a>';
			html+='\n						</div>	';
			var fileName = cutstr(ansFile.orgFileName,25);
			html+='\n						<span class="fileName" title="'+ansFile.orgFileName+'">'+fileName+'(已有文件)</span><span class="data"> - 完成</span>';
			html+='\n						<div class="uploadify-progress">';
			html+='\n							<div class="uploadify-progress-bar" style="width: 100%;"></div>';
			html+='\n						</div>	';
			html+='\n					</div>	';
		}
	}
	html+='\n						</div>';
	html+='\n						<div class="btns btn-sm">';
	html+='\n							<div id="pickerlistAnsFilesUpdate_id">选择文件</div>';
	html+='\n						</div>';
	
	html+='\n						<script type="text/javascript">';
	html+='\n							loadWebUpfiles(\'listAnsFilesUpdate_id\',\''+sid+'\',\'otherIframe\',\'pickerlistAnsFilesUpdate_id\',\'thelistlistAnsFilesUpdate_id\',\'filelistAnsFilesUpdate_id\');';
	html+='\n						</script>';
	html+='\n						<div style="width: 350px; height: 90px; display: none; position: relative;">';
	html+='\n							<div style="width: 250px; float: left;">';
	html+='\n 								<select style="width: 100%; height: 90px;" id="listAnsFilesUpdate_id" ondblclick="removeClick(this.id)" multiple="multiple" name="listAnsFilesUpdate.id" moreselect="true" listvalue="filename" listkey="id" list="listAnsFilesUpdate">';
	if(ans.listAnsFiles.length>0){
		for(var i=0;i<ans.listAnsFiles.length;i++){
			//已有附件
			var ansFile = ans.listAnsFiles[i];
			html+='\n 							<option selected="selected" value="'+ansFile.original+'">'+ansFile.orgFileName+'</option>';
		}
	}
	html+='\n  								</select>';
	html+='\n							</div>';
	html+='\n 						</div>';
	html+='\n 					</div>';
	html+='\n				</div>';
	//附件结束
	html+='\n 				<div style="text-align: center">';
	html+='\n 					<button type="button" class="btn btn-info ws-btnBlue" onclick="updateAns(this,'+ans.quesId+','+ans.id +',\''+sid+'\','+quesUser+')" ">修改</button>';
	html+='\n 					<button type="reset" class="btn btn-default" onclick="appandAns('+ans.id+')">取消</button>';
	html+='\n 				</div>';
	html+='\n 				<div style="clear: both;"></div>';
	html+='\n 			</div>';
	html+='\n 		</div>';
	//只有问题的回答人，在非采纳的状态下才可以修改结束
	html+='\n 	</div>';
	html+='\n	<div class="ws-clear"></div>';
	html+='\n 	<div id="talk4Ques'+ans.id+'" style="display:none">';
	//针对回答的评论
	html+='\n 		<div style="display:block" class="ws-shareBox ws-shareBox2 talk4Ques">';
	html+='\n			<div class="shareText">';
	html+='\n 				<div class="ws-textareaBox" style="margin-top:10px;">';
	html+='\n 					<textarea id="operaterReplyTextarea_'+ans.id+'_-1" class="form-control ptalk" placeholder="评论……"></textarea>';
	html+='\n					<div class="ws-otherBox" style="position: relative;">';
	html+='\n						<div class="ws-meh">';
	//表情
	html+='\n 							<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_'+ans.id+'" class="tigger" onclick="addBiaoQingObj(\'biaoQingSwitch_'+ans.id+'\',\'biaoQingDiv_'+ans.id+'\',\'operaterReplyTextarea_'+ans.id+'_-1\');"></a>';
	html+='\n							<div id="biaoQingDiv_'+ans.id+'" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 5px">';
	//表情DIV层
	html+='\n				     			<div class="main">';
	html+='\n				           			<ul style="padding: 0px">';
	html+=' \n'+getBiaoqing();
	html+='\n				           			</ul>';
	html+='\n				       			</div>';
	html+='\n				       		</div>';
	html+='\n				       	</div>';
	//常用意见
	html+='\n 						<div class="ws-plugs">';
	html+='\n 							<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea(\'operaterReplyTextarea_'+ans.id+'_-1\',\''+sid+'\');" title="常用意见"></a>';
	html+='\n				       	</div>';
	//提醒方式
	/*html+='\n						<div class="ws-remind">';
	html+='\n							<span class="ws-remindTex">提醒方式：</span>';
	html+='\n							<div class="ws-checkbox">';
	html+='\n								<label class="checkbox-inline">';
	html+='\n									<input id="inlineCheckbox1" type="checkbox" value="option1"/>短信</label>';
	html+='\n								<label class="checkbox-inline">';
	html+='\n									<input id="inlineCheckbox1" type="checkbox" value="option1"/>邮件</label>';
	html+='\n								<label class="checkbox-inline">';
	html+='\n									<input id="inlineCheckbox1" type="checkbox" value="option1"/>桌面推送</label>';
	html+='\n							</div>';
	html+='\n						</div>';*/
	//分享按钮
	html+='\n						<div class="ws-share">';
	html+='\n							<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('+ans.quesId+','+ans.id+',-1,this,null,\''+sid+'\')">评论</button>';
	html+='\n						</div>';
	html+='\n						<div style="clear: both;"></div>';
	//相关附件
	html+='\n 						<div class="ws-notice">';
	
	html+= '\n 						<div style="clear:both;width: 300px" class="wu-example">';
	//用来存放文件信息
	html+= '\n 							<div id="thelistlistUpfiles_'+ans.id+'_-1_upfileId" class="uploader-list" ></div>';
	html+= '\n 						 	<div class="btns btn-sm">';
	html+= '\n 								<div id="pickerlistUpfiles_'+ans.id+'_-1_upfileId">选择文件</div>';
	html+= '\n 							</div>';
	
	html+= '\n							<script type="text/javascript">\n';
	html+= '\n 								loadWebUpfiles(\'listUpfiles_'+ans.id+'_-1_upfileId\',\''+sid+'\',\'otherIframe\',\'pickerlistUpfiles_'+ans.id+'_-1_upfileId\',\'thelistlistUpfiles_'+ans.id+'_-1_upfileId\',\'filelistUpfiles_'+ans.id+'_-1_upfileId\');';
	html+= '\n 							</script>\n';
	
	html+='\n 								<div style="width: 350px; height: 90px; display: none; position: relative;">';
	html+='\n 									<div style="width: 250px; float: left;">';
	html+='\n 										<select style="width: 100%; height: 90px;" id="listUpfiles_'+ans.id+'_-1_upfileId" ondblclick="removeClick(this.id)" multiple="multiple" name="listUpfiles_'+ans.id+'_-1.upfileId" moreselect="true" listvalue="filename" listkey="upfileId" list="listUpfiles_'+ans.id+'_-1">';
	html+='\n 									 	</select>';
	html+='\n 									</div>';
	html+='\n 								</div>';
	html+='\n 							</div>';
	
	html+='\n						</div>';
	
	
	html+='\n					</div>';
	html+='\n				</div>';
	html+='\n			</div>';
	//针对回答的评论结束
	html+='\n 			<div class="ws-clear"></div>';
	html+='\n 		</div>';
	//针对评论的回复
	html+='\n 		<div id="alltalks'+ans.id+'">';
	html+='\n 		</div>';
	//针对评论的回复结束
	html+='\n 	</div>';
	html+='\n <div class="ws-clear"></div>';
	
	return html;
}

/**
 * 对回答的评论
 * @param answer
 * @param sessionUser
 * @param sid
 * @return
 */
function getAnsTalkHtml(ans,sessionUser,sid){
	var html='';
	$("#img_"+ans.id).html("["+ans.listAnsTalks.length+"]");
	//有评论,列出有回复内容的
	if(ans.listAnsTalks.length>0){
		for(var i=0;i<ans.listAnsTalks.length;i++){
			var ansTalk =ans.listAnsTalks[i];
			html +='\n '+getAnsTalkString(ansTalk,sid,sessionUser);
		}
	}
				
				
	return html;
}

function getAnsTalkString(ansTalk,sid,sessionUser){
	var html = '';
	if(ansTalk.ptalker>0){
		html+='\n <div class="ws-shareBox ws-shareBox2 voteTalkInfoP" id="talk'+ansTalk.id+'" parentId="'+ansTalk.parentId+'">';
	}else{
		html+='\n <div class="ws-shareBox ws-shareBox2 voteTalkInfo" id="talk'+ansTalk.id+'" parentId="'+ansTalk.parentId+'">';
	}
	html+='\n 		<div class="shareHead" data-container="body" data-toggle="popover" data-user='+ansTalk.talker+' data-busId='+ansTalk.quesId+' data-busType=\'011\'>';
	html += '		<img src="/downLoad/userImg/'+ansTalk.comId+'/'+ansTalk.talker+'?sid='+sid+'" title="'+ansTalk.talkerName+'"></img>\n';
	html+='\n 		</div>';
	html+='\n 		<div class="shareText">';
	html+='\n 			<span class="ws-blue">'+ansTalk.talkerName+'</span>';
	if(ansTalk.ptalker>0){
		html+='\n 		<r>回复</r>';
		html+='\n 		<span class="ws-blue">'+ansTalk.ptalkerName+'</span>';
	}
	html+='\n 			<p class="ws-texts">'+ansTalk.talkContent+'</p>';
	//附件
	if(ansTalk.listQasTalkFile.length>0){
		html+='\n 		<div class="file_div">';
		for(var i=0;i<ansTalk.listQasTalkFile.length;i++){
			var upfiles = ansTalk.listQasTalkFile[i];
			html+='\n		<p class="p_text">附件('+(i+1)+')';
			if(upfiles.isPic=='1'){
				html+='\n		<img onload="AutoResizeImage(350,0,this,\'otherIframe\')"';
				html+='\n			src="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" />';
				html+='\n		<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
				html+='\n		<a class="fa fa-eye"  style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="showPic(\'/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'\',\''+sid+'\',\''+upfiles.upfileId+'\',\'011\',\''+ansTalk.quesId+'\')"></a>';
			}else{
				html+='\n		'+upfiles.filename;
				var fileExt = upfiles.fileExt;
				if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx' ){
					html+='\n	<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+sid+'\')"></a>';
					html+='\n	<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'011\',\''+ansTalk.quesId+'\')"></a>';
				}else if(fileExt=='txt' ||fileExt=='pdf'){
					html+='\n	<a class="fa fa-download" style="padding-left: 15px" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+sid+'" title="下载"></a>';
					html+='\n	<a class="fa fa-eye" style="padding-left: 15px" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+upfiles.fileExt+'\',\''+sid+'\',\'011\',\''+ansTalk.quesId+'\')"></a>';
				}else{
					html+='\n	<a class="fa fa-download" style="padding-left: 15px" href="javascript:void(0);" title="下载" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.filename+'\',\''+sid+'\')"></a>';
				}
			}
			html+='\n		</p>';
		}
		html+='\n		</div>';
	}
	//附件结束
	html+='\n 			<div class="ws-type">';
	html+='\n 				<span class="hideOpt" style="float:left;display:block">';
	html+='\n					<a href="javascript:void(0);" id="delTalk_'+ansTalk.ansId+'_'+ansTalk.id+'" onclick="delAnsTalk('+ansTalk.quesId+','+ansTalk.ansId+','+ansTalk.id+','+ansTalk.isLeaf+',\''+sid+'\')"  class="fa fa-trash-o" title="删除"></a>';
	html+='\n 					<a id="img_'+ansTalk.ansId+'_'+ansTalk.id+'"  href="javascript:void(0);" class="fa fa-comment-o" title="回复" onclick="showArea('+ansTalk.ansId+','+ansTalk.id+')"></a>';
	html+='\n				</span>';
	html+='\n 				<span style="float:left;">';
	html+='\n 					<time>'+ansTalk.recordCreateTime+'</time>';
	html+='\n				</span>';
	html+='\n			</div>';
	html+='\n		</div>';
	html+='\n 		<div class="ws-clear"></div>';
	html+='\n 	</div>';
	html+='\n 	<div id="addTalk_'+ansTalk.ansId+'_'+ansTalk.id+'"  class="ws-shareBox ws-shareBox2 addtalk" style="display:none;">';
	html+='\n 		<div class="shareText">';
	html+='\n 			<div class="ws-textareaBox" style="margin-top:10px;">';
	html+='\n 				<textarea id="operaterReplyTextarea_'+ansTalk.ansId+'_'+ansTalk.id+'" class="form-control txtTalk" placeholder="回复……"></textarea>';
	html+='\n				 <div class="ws-otherBox" style="position: relative;">';
	html+='\n 					<div class="ws-meh">';
	//表情开始
	html+='\n						<a href="javascript:void(0);" class="fa fa-meh-o tigger" id="biaoQingSwitch_'+ansTalk.ansId+'_'+ansTalk.id+'" class="tigger" onclick="addBiaoQingObj(\'biaoQingSwitch_'+ansTalk.ansId+'_'+ansTalk.id+'\',\'biaoQingDiv_'+ansTalk.ansId+'_'+ansTalk.id+'\',\'operaterReplyTextarea_'+ansTalk.ansId+'_'+ansTalk.id+'\')"></a>';
	html+='\n 						<div id="biaoQingDiv_'+ansTalk.ansId+'_'+ansTalk.id+'" class="blk" style="display:none;position:absolute;width:200px;top:30px;z-index:99;left: 5px">';
	//表情DIV层
	html+='\n 							<div class="main">';
	html+='\n 								<ul>';
	html+='\n								'+getBiaoqing();
	html+='\n 								</ul>';
	html+='\n								</div>';
	html+='\n						</div>';
	html+='\n					</div>';
	//表情结束
	//常用意见
	html+='\n 					<div class="ws-plugs">';
	html+='\n 						<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea(\'operaterReplyTextarea_'+ansTalk.ansId+'_'+ansTalk.id+'\',\''+sid+'\');" title="常用意见"></a>';
	html+='\n					 </div>';
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
	html+='\n 					<div class="ws-share">';
	html+='\n 						<button type="button" class="btn btn-info ws-btnBlue" onclick="addTalk('+ansTalk.quesId+','+ansTalk.ansId+','+ansTalk.id+',this,'+ansTalk.talker+',\''+sid+'\')">回复</button>';
	html+='\n					</div>';
	//相关附件
	html+='\n					<div style="clear: both;"></div>';
	html+='\n 						<div class="ws-notice">';
	
	html+= '\n 							<div style="clear:both;width: 300px" class="wu-example">';
	//用来存放文件信息
	html+= '\n 								<div id="thelistlistUpfiles_'+ansTalk.ansId+'_'+ansTalk.id+'_upfileId" class="uploader-list" ></div>';
	html+= '\n 						 		<div class="btns btn-sm">';
	html+= '\n 								<div id="pickerlistUpfiles_'+ansTalk.ansId+'_'+ansTalk.id+'_upfileId">选择文件</div>';
	html+= '\n 								</div>';
	
	html+= '\n								<script type="text/javascript">\n';
	html+= '\n 									loadWebUpfiles(\'listUpfiles_'+ansTalk.ansId+'_'+ansTalk.id+'_upfileId\',\''+sid+'\',\'otherIframe\',\'pickerlistUpfiles_'+ansTalk.ansId+'_'+ansTalk.id+'_upfileId\',\'thelistlistUpfiles_'+ansTalk.ansId+'_'+ansTalk.id+'_upfileId\',\'filelistUpfiles_'+ansTalk.ansId+'_'+ansTalk.id+'_upfileId\');';
	html+= '\n 								</script>\n';
	
	html+='\n 								<div style="width: 350px; height: 90px; display: none; position: relative;">';
	html+='\n 									<div style="width: 250px; float: left;">';
	html+='\n 										<select style="width: 100%; height: 90px;" id="listUpfiles_'+ansTalk.ansId+'_'+ansTalk.id+'_upfileId" ondblclick="removeClick(this.id)" multiple="multiple" name="listUpfiles_'+ansTalk.ansId+'_'+ansTalk.id+'.upfileId" moreselect="true" listvalue="filename" listkey="upfileId" list="listUpfiles_'+ansTalk.ansId+'_'+ansTalk.id+'">';
	html+='\n 									 	</select>';
	html+='\n 									</div>';
	html+='\n 								</div>';
	html+='\n 							</div>';
	
	html+='\n					</div>';
	html+='\n				</div>';
	html+='\n			</div>';
	html+='\n		</div>';
	//针对回答的评论结束
	html+='\n 		<div class="ws-clear"></div>';
	html+='\n 	</div>';
	return html;
}