var pageNum = 0;     //页面索引初始值   
var pageSize = 10;     //每页显示条数初始化，修改显示条数，修改这里即可 

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
var modTalkOpt = {
		//处理的Url
		allUrl:{
			"type003":{"query":"/task/ajaxListPagedTalk","add":"/task/ajaxAddTalk","del":"/task/ajaxDelTalk"}
		    ,"type070":{"query":"/demandTalk/ajaxListPagedTalk","add":"/demandTalk/ajaxAddTalk","del":"/demandTalk/ajaxDelTalk"}
		    ,"type080":{"query":"/productTalk/ajaxListPagedTalk","add":"/productTalk/ajaxAddTalk","del":"/productTalk/ajaxDelTalk"}
		}
		//初始化页面事件
		,initEvent:function(){
			//加载数据
			modTalkOpt.initTalkData(0);
			
			$("#operaterReplyTextarea_-1").autoTextarea({minHeight:70,maxHeight:150});  
			//文本框绑定回车提交事件
			$("#operaterReplyTextarea_-1").bind("paste cut keydown keyup focus blur",function(event){
				resizeVoteH(pageParam.ifreamName);
		    });
			
			 //信息推送
	        $("body").on("click","a[data-todoUser]",function(){
	            var relateDiv = $(this).attr("data-relateDiv");
	            var params = {};
	            //查询所有
	            params.onlySubState = 0;
	            userMore('null',params , pageParam.sid,"null","null",function(options){
	                if(options && options[0]){
	                    $.each(options,function(index,option){
	                        var span = $('<span></span>');
	                        var userId = option.value;
	                        //去重
	                        var preUserSpan = $("body").find("#"+relateDiv).find("span[data-userId='"+userId+"']");
	                        if(preUserSpan && preUserSpan.get(0)){
	                            return true;
	                        }
	                        $(span).attr("data-userId",option.value);
	                        $(span).attr("title","双击移除");
	                        $(span).addClass("blue");
	                        $(span).css("cursor","pointer");
	                        $(span).css("padding-right","5px");
	                        $(span).html('@'+option.text);

	                        var userObj = {}
	                        userObj.userId = option.value;
	                        userObj.userName = option.text;
	                        $(span).data("userObj",userObj);

	                        $("#"+relateDiv).append($(span));
	                    })
	                }else{
	                    $("#"+relateDiv).html("");
	                }
	            })
	        })
			
			$("body").on("click",".replyTalk",function(){
				var ts = $(this);
				modTalkOpt.replyTalkOpt(ts);
			})
		}
		//初始化页面留言数据
		,initTalkData:function(morePageNum){
			if(morePageNum){
				pageNum = morePageNum;
			}else{
				pageNum = 0;
			}
			$("#alltalks").html('');
			var params={"sid":pageParam.sid,
		           	 "pageNum":pageNum,
		           	 "pageSize":pageSize,
		           	 "busId":pageParam.busId
			}
			
			//取得数据
			var url = modTalkOpt.allUrl["type"+pageParam.busType].query;
	 		getSelfJSON(url,params,function(result){
	 			loadDone = 1;
	 			if(result.code=='0'){
	 				var pageBean = result.data;
	 			 	$("#totalNum").html(pageBean.totalCount);
	               	if(pageBean.totalCount<=pageSize){
	               		 $("#totalNum").parent().parent().css("display","none")
	               	}else{
	               		$("#totalNum").parent().parent().css("display","block")
	               	}
	          		//分页，PageCount是总条目数，这是必选参数，其它参数都是可选
	                 $("#pageDiv").pagination(pageBean.totalCount, {
	                     callback: modTalkOpt.PageCallback,  //PageCallback() 为翻页调用次函数。
	                     prev_text: "<<",
	                     next_text: ">>",
	                     items_per_page:pageSize,
	                     num_edge_entries: 0,       //两侧首尾分页条目数
	                     num_display_entries: 5,    //连续分页主体部分分页条目数
	                     current_page: pageNum,   //当前页索引
	                 });
	               	 if(pageBean.totalCount>0){
	               		 $.each(pageBean.recordList,function(index,talk){
	               			var html = modTalkOpt.constrTalkHtml(talk);
	               			$("#alltalks").append(html);
	               		 });
	               	 }else{
	               		 var noneInfo = '<div align="center" id="noTalks"><h3 style="font-size:16px">没有留言！</h3></div>';
	               		 $("body").find("#alltalks").html(noneInfo);
	               	 }
	               	 resizeVoteH(pageParam.ifreamName);
	 			 }
	 		})
		}
		//翻页事件
		,PageCallback(index, jq) {  
			 pageNum = index;
			 modTalkOpt.initTalkData(pageNum);
		}
		//构建留言数据
		,constrTalkHtml:function(talk){
			var html = '';
			if(talk.parentId==-1){
				html+='\n<div id="talk'+talk.id+'" class="ws-shareBox talkInfoP">';
			}else{
				html+='\n<div id="talk'+talk.id+'" class="ws-shareBox ws-shareBox2 talkInfo">';
			}
			
			html+='\n		<div class="shareHead">';
			//头像信息
			html += '		<img src="/downLoad/userImg/'+talk.comId+'/'+talk.speaker+'" title="'+talk.speakerName+'"></img>\n';
			html+='\n 		</div>';
			html+='\n		<div class="shareText">';
			html+='\n			<span class="ws-blue">'+talk.speakerName+'</span>';
			if(talk.parentId>-1){
				html+='\n		<r>回复</r>';
				html+='\n		<span class="ws-blue">'+talk.pSpeakerName+'</span>';
			}
			html+='\n			<p class="ws-texts">'+talk.content+'</p>';
			//附件
			var listTalkFile = talk.listTalkFile;
			if(listTalkFile && listTalkFile[0]){
				html+='\n		<div class="file_div">';
				$.each(listTalkFile,function(i,upfiles){
					html+='\n		<p class="p_text">附件('+(i+1)+')：';
					if(upfiles.isPic==1){
						html+='\n			<img onload="AutoResizeImage(350,0,this,\''+pageParam.ifreamName+'\')"';
						html+='\n			src="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+pageParam.sid+'" />';
						html+='\n			&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.fileName+'?sid='+pageParam.sid+'" title="下载"></a>';
						html+='\n			&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="showPic(\'/downLoad/down/'+upfiles.uuid+'/'+upfiles.fileName+'\',\''+pageParam.sid+'\',\''+upfiles.upfileId+'\',\''+pageParam.busType+'\',\''+talk.busId+'\')"></a>';
					}else{
						html += '			'+upfiles.filename+'\n';
						var fileExt = upfiles.fileExt;
						if(fileExt=='doc'||fileExt=='docx'||fileExt=='xls'||fileExt=='xlsx'||fileExt=='ppt'||fileExt=='pptx' ){
							html+='\n		&nbsp;&nbsp;<a class="fa fa-download" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.fileName+'\',\''+pageParam.sid+'\')"></a>';
							html+='\n		&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.fileName+'\',\''+upfiles.fileExt+'\',\''+pageParam.sid+'\',\''+pageParam.busType+'\',\''+talk.busId+'\')"></a>';
						}else if(fileExt=='txt' ||fileExt=='pdf'){
							html+='\n		&nbsp;&nbsp;<a class="fa fa-download" href="/downLoad/down/'+upfiles.uuid+'/'+upfiles.filename+'?sid='+pageParam.sid+'" title="下载"></a>';
							html+='\n		&nbsp;&nbsp;<a class="fa fa-eye" href="javascript:void(0);" title="预览" onclick="viewOfficePage(\''+upfiles.upfileId+'\',\''+upfiles.uuid+'\',\''+upfiles.fileName+'\',\''+upfiles.fileExt+'\',\''+pageParam.sid+'\',\''+pageParam.busType+'\',\''+talk.busId+'\')"></a>';
						}else{
							html+='\n	&nbsp;&nbsp;<a class="fa fa-download" onclick="downLoad(\''+upfiles.uuid+'\',\''+upfiles.fileName+'\',\''+pageParam.sid+'\')"></a>';
						}
					}
					html+='\n		</p>';
					
				})
				html+='\n		</div>';
			}
			
			html+='\n			<div class="ws-type">';
			//发言人可以删除自己的发言
			html+='\n				<span class="hideOpt" style="display:block">';
			if(talk.speaker == pageParam.userInfo.id){
				html+='\n 					<a href="javascript:void(0)" id="delTalk_'+talk.id+'" onclick="delTalk('+talk.id+')" class="fa fa-trash-o" title="删除"></a>';
			}
			html+='\n 					<a href="javascript:void(0)" id="img_'+talk.id+'" onclick="showArea(\'addTalk'+talk.id+'\')" class="fa fa-comment-o" title="回复"></a>';
			html+='\n				</span>';
			html+='\n				<span>';
			html+='\n 					<time>'+talk.recordCreateTime+'</time>';
			html+='\n				</span>';
			html+='\n			</div>';
			html+='\n		</div>';
			html+='\n		<div class="ws-clear"></div>';
			html+='\n	</div>';
			
			//回复层
			html+='\n 	<div id="addTalk'+talk.id+'" style="display:none;" class="ws-shareBox ws-shareBox2 ws-shareBox3 addTalk" talkParentDiv="'+talk.id+'">';
			html+='\n 		<div class="shareText">';
			html+='\n 			<div class="ws-textareaBox" style="margin-top:10px;">';
			html+='\n 				<textarea id="operaterReplyTextarea_'+talk.id+'" name="operaterReplyTextarea_'+talk.id+'" style="height: 55px" class="form-control txtTalk" placeholder="回复……"></textarea>';
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
			html+='\n 						<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea(\'operaterReplyTextarea_'+talk.id+'\',\''+pageParam.sid+'\');" title="常用意见"></a>';
			html+= '\n				 	 </div>';
			//共享人员
			if(pageParam.shareState && pageParam.shareState=='true'){
				html+='\n 					<div class="ws-plugs">';
				html+='\n 						<a class="btn-icon" href="javascript:void(0)" data-todoUser="yes" data-relateDiv="todoUserDiv_'+talk.id+'" title="告知人员">@</a>';
				html+= '\n				 	 </div>';
			}
			
			
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
			//回复按钮
			html+='\n					<div class="ws-share">';
			html+='\n						<button type="button" class="btn btn-info ws-btnBlue replyTalk">回复</button>';
			html+='\n					</div>';
			//相关附件 
			html+='\n					<div style="clear: both;"></div>';
			
			//共享人员
			html+='\n 					<div id="todoUserDiv_'+talk.id+'" class="padding-top-10"></div>';
			
			html+='\n					<div class="ws-notice">';
			html+= '\n 						<div style="clear:both;width: 300px" class="wu-example">';
			//用来存放文件信息
			html+= '\n 							<div id="thelistlistUpfiles_'+talk.id+'_upfileId" class="uploader-list" ></div>';
			html+= '\n 						 	<div class="btns btn-sm">';
			html+= '\n 								<div id="pickerlistUpfiles_'+talk.id+'_upfileId">选择文件</div>';
			html+= '\n 							</div>';
			html+= '\n							<script type="text/javascript">\n';
			html+= '\n 								loadWebUpfiles(\'listUpfiles_'+talk.id+'_upfileId\',\''+pageParam.sid+'\',\''+pageParam.ifreamName+'\',\'pickerlistUpfiles_'+talk.id+'_upfileId\',\'thelistlistUpfiles_'+talk.id+'_upfileId\',\'filelistUpfiles_'+talk.id+'_upfileId\');';
			html+= '\n 							</script>\n';
			html+= '\n 							<div style="position: relative; width: 350px; height: 90px;display: none">';
			html+= '\n 								<div style="float: left; width: 250px">';
			html+= '\n 									<select  list="listUpfiles_'+talk.id+'" listkey="upfileId" listvalue="filename" id="listUpfiles_'+talk.id+'_upfileId" name="listUpfiles_'+talk.id+'.upfileId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">';
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
		//回复留言操作
		,replyTalkOpt:function(ts){
			var blnCheckState = modTalkOpt.checkForm(ts);
			if(!blnCheckState){
				return;
			}
			var params = modTalkOpt.contrAddParam(ts);
			var url = modTalkOpt.allUrl["type"+pageParam.busType].add;
			postUrl(url,params,function(result){
				if(result.code == 0){
					
					var talkPId = $(ts).parents("div[talkParentDiv]").attr("talkParentDiv");
					$("#operaterReplyTextarea_"+talkPId).val('');
					$("#todoUserDiv_"+talkPId).html('');
					
					var html = modTalkOpt.constrTalkHtml(result.data);
					
			       	if(talkPId==-1){
			       		$("#alltalks").prepend(html);
			       	}else{
			       		//父节点
				     	$("#delTalk_"+talkPId).attr("onclick","delTalk("+talkPId+")")
			       		$("#talk"+talkPId).after(html);
				     	$("#addTalk"+talkPId).hide();
				     	$("#img_"+talkPId).attr("src","/static/images/say.png");
			       	}
			     	$("#listUpfiles_"+talkPId+"_upfileId").html('');

			     	//清除文件序列缓存
					$.each($("#thelistlistUpfiles_"+talkPId+"_upfileId"),function(index,item){
						$(this).find(".cancel").click();
					})
				}else{
					showNotification(2,result.msg);
				}
			})
		}
		//验证必填内容
		,checkForm:function(ts){
			var talkId = $(ts).parents("div[talkParentDiv]").attr("talkParentDiv");
			var content = $("#operaterReplyTextarea_"+talkId).val();
			if(!content){
				showNotification(2,"请编辑回复内容！");
				$("#operaterReplyTextarea_"+talkId).focus();
				return false;
			}
			return true;
		}
		//构建留言的操作数据
		,contrAddParam:function(ts){
			var talkId = $(ts).parents("div[talkParentDiv]").attr("talkParentDiv");
			var params = {
				"sid":pageParam.sid,
	            "content":$("#operaterReplyTextarea_"+talkId).val(),
	            "busId":pageParam.busId,
	            "parentId":talkId
			}
			
			//告知人员
			var relateTodoDiv =  $(ts).parents("div[talkParentDiv]").find("[data-todoUser]").attr("data-relateDiv");
			var todoUsers = $("#"+relateTodoDiv).find("span");
			if(todoUsers && todoUsers.get(0)){
				$.each(todoUsers,function(index,span){
					var user = $(span).data("userObj")
					params["listSharer["+index+"].id"] = user.userId;
					params["listSharer["+index+"].userName"] = user.userName;
				})
			}
			//附件
			var upfileSelect = $("#listUpfiles_"+talkId+"_upfileId").find("option");
			if(upfileSelect && upfileSelect[0]){
				$.each(upfileSelect,function(index,option){
					var upfileId = $(option).val();
					var fileName = $(option).text();
					params["listTalkFile["+index+"].upfileId"] = upfileId;
					params["listTalkFile["+index+"].fileName"] = fileName;
				})
			}
			return params;
		},delTalk:function(talkId){
				var params = {
					"sid":pageParam.sid,
		            "busId":pageParam.busId,
		            "id":talkId
				}
				var url = modTalkOpt.allUrl["type"+pageParam.busType].del;
				postUrl(url,params,function(result){
					if(result.code == 0){
						window.location.reload();
					}
				});
		}
}


//窗体点击事件检测
document.onclick = function(e){
	   var evt = e?e:window.event;
	   var ele = evt.srcElement || evt.target;
	   //表情包失焦关闭
		for(var i=0;i<biaoQingObjs.length;i++){
			if(ele.id != biaoQingObjs[i].switchID){
				$("#"+biaoQingObjs[i].divID).hide();
			}
		}
}
//创建一个表情对象数组
var biaoQingObjs = new Array();
//初始化最新初始化表情对象
var activingBiaoQing;
//表情对象添加；switchID触发器开关，objID返回对象主键,表情显示div层主键
function addBiaoQingObj(switchID,divID,objID){
	//数组是否已经包含此元素标识符
	var haven = false;
	//判断数组是否已经包含此主键元素
	if(isBiaoQingEvent(switchID)){
		haven = true;
	}
	//对象构建
	var biaoQing ={"switchID":switchID,"objID":objID,"divID":divID}
	if(!haven){
		//主键存入数组
		biaoQingObjs[biaoQingObjs.length]=biaoQing;
	}
	//初始化最新初始化表情对象
	activingBiaoQing = biaoQing;
	//打开表情
	biaoQingOpen(biaoQing);
}
//判断页面点击事件事都是表情触发事件
function isBiaoQingEvent(eventId){
	//数组是否已经包含此元素标识符
	var haven = false;
	//判断数组是否已经包含此主键元素
	for(var i=0;i<biaoQingObjs.length;i++){
		if(biaoQingObjs[i].switchID==eventId){
			haven = true;
			break;
		}
	}
	return haven;
}
//表情打开
function biaoQingOpen(obj){
	$("#"+obj.divID).show();
}
//表情包关闭
function biaoQingClose(){
	$("#"+activingBiaoQing.divID).hide();
}
//关闭表情div，并赋值
function divClose(title,path){
	biaoQingClose();
	insertAtCursor(activingBiaoQing.objID,title)
	$("#"+activingBiaoQing.objID).focus();
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
	 resizeVoteH(pageParam.ifreamName)
}

//删除评论
function delTalk(talkId){
	window.top.layer.confirm("确定要删除回复信息?", {icon: 3, title:'确认对话框'}, function(index){
		window.top.layer.close(index);
		modTalkOpt.delTalk(talkId);
	});
}