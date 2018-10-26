$(function(){
	//初始化名片
	$(".operaterReplyTextarea_-1").autoTextarea({minHeight:55,maxHeight:160});  
	$(".operaterReplyTextarea_-1").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('otherannounAttrIframe');
    });
});
//样式设置
function setStyle(){
	$(".operaterReplyTextarea_-1").autoTextarea({minHeight:55,maxHeight:160});  
}

/**
 * announId 主键
 * talkPId 回复的父节点
 */
function addTalk(announId,talkPId,ts,ptalker){
	var textarea = $("#operaterReplyTextarea_"+talkPId);
	var content = $(textarea).val();
	if(""==content){
		layer.tips('请填写内容！', $("#operaterReplyTextarea_"+talkPId), {tips: 1});
		$(textarea).focus();
		return;
	}else{
		var onclick = $(ts).attr("onclick");
		//异步提交表单 投票讨论
	    $("#announForm").ajaxSubmit({
	        type:"post",
	        url:"/announcement/addAnnounTalk?sid="+EasyWin.sid+"&t="+Math.random(),
	        data:{"busId":announId,
		        "parentId":talkPId,
		        "content":content,
		        "ptalker":ptalker,
		        "upfilesId":$("#listUpfiles_"+talkPId+"_upfileId").val()},
	        dataType: "json", 
	        traditional :true,
	        success:function(data){
	        	var talk = data.talk;
			    var html = getTalkStr(talk,EasyWin.sid,data.userInfo,'039','otherannounAttrIframe');
			    $(textarea).val('');
		       	if(talkPId==-1){
		       		$("#alltalks").prepend(html);
		       	}else{
		       		//父节点
		       		$("#addTalk"+talkPId).hide();
		       		$("#img_"+talkPId).attr("class","fa fa-comment-o");
			     	$("#delTalk_"+talkPId).attr("onclick","delTalk("+talkPId+",0)");
		       		$("#talk"+talkPId).after(html);
		       	}
		     	$("#listUpfiles_"+talkPId+"_upfileId").html('');

		     	//清除文件序列缓存
				$.each($("#thelistlistUpfiles_"+talkPId+"_upfileId"),function(index,item){
					$(this).find(".cancel").click();
				});
				
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	showNotification(2,"操作失败");
				$(textarea).html('');
			    $(ts).attr("onclick",onclick);
	        }
	    });
	}
}
//删除评论
function delTalk(id,isLeaf){
	window.top.layer.confirm("确定要删除该回复吗?", {
		  btn: ['确定','取消']//按钮
	  ,title:'询问框'
	  ,icon:3
	}, function(index){//删除叶子节点
		window.top.layer.close(index);
		if(isLeaf==1){
			//异步提交表单删除评论
		    $("#announForm").ajaxSubmit({
		        type:"post",
		        url:"/announcement/delAnnounTalk?sid="+EasyWin.sid+"&t="+Math.random(),
		        data:{"id":id,"busId":EasyWin.busId},
		        dataType: "json", 
		        success:function(data){
		        	window.self.location.reload();
		        },
		        error:function(XmlHttpRequest,textStatus,errorThrown){
		        	showNotification(2,"操作失败");
		        }
		    });
		}else{
			setTimeout(function(){
				window.top.layer.confirm("是否需要删除此节点下的回复信息?",{
					  btn: ['是','否']//按钮
				  ,title:'询问框'
				  ,icon:3
				},  function(index){//删除自己和子节点
					window.top.layer.close(index);
					//异步提交表单删除评论
				    $("#announForm").ajaxSubmit({
				        type:"post",
				        url:"/announcement/delAnnounTalk?sid="+EasyWin.sid+"&t="+Math.random(),
				        data:{"id":id,"busId":EasyWin.busId,"delChildNode":"yes"},
				        dataType: "json", 
				        success:function(data){
				        	window.self.location.reload();
				        },
				        error:function(XmlHttpRequest,textStatus,errorThrown){
				        	showNotification(2,"操作失败");
				        }
				    });
				},function(index){
					window.top.layer.close(index);
					//异步提交表单删除评论
				   $.ajax({//删除自己
				        type:"post",
				        url:"/announcement/delAnnounTalk?sid="+EasyWin.sid+"&t="+Math.random(),
				        data:{"id":id,"busId":EasyWin.busId,"delChildNode":"no"},
				        dataType: "json", 
				        success:function(data){
				        	window.self.location.reload();
				        },
				        error:function(XmlHttpRequest,textStatus,errorThrown){
				        	showNotification(2,"操作失败");
				        }
				    });
				});
			},200);
		}
	});	
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
};
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
	var biaoQing ={"switchID":switchID,"objID":objID,"divID":divID};
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
	insertAtCursor(activingBiaoQing.objID,title);
	$("#"+activingBiaoQing.objID).focus();
}
//显示回复
function showInfos(){
	$("#finishTalkMsg").css("display","none");
	$(".hideOpt").css("display","block");
	$(".parentRep").css("display","block");
}
var fileTypes="*.rar;*.zip;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;*.pdf;*.mp3;*.gif;*.jpg;*.jpeg;*.png;*.bmp;*.avi;*.wma;*.rmb;*.rm;*.flash;*.flv;*.mp4;*.mid;*.3gp";
var fileSize="200MB";
var	numberOfFiles = 200;