var autoCompleteCallBackVar =null;
$(function(){
	initCard(EasyWin.sid);
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		datatype:{
			"input":function(gets,obj,curform,regxp){
				var str = $(obj).val();
				if(str){
					var count = str.replace(/[^\x00-\xff]/g,"**").length;
					var len = $(obj).attr("defaultLen");
					if(count>len){
						return false;
					}else{
						return true;
					}
				}else{
					return false;
				}
			}
		},
		callback:function (form){
			//提交前验证是否在上传附件
			return sumitPreCheck(null);
		},
		showAllError : true
	}); 

	//公告描述的图片查看
	var imgArray = $("#comment").find("img");
	$.each(imgArray,function(index,item){
		var src = $(this).attr("src");
		$(this).click(function(){
			window.top.viewOrgByPath(src);
		});
	});
});
//菜单切换
$(function(){
	$("#otherannounAttrIframe").attr("src","/announcement/announTalkPage?sid="+EasyWin.sid+"&pager.pageSize=10&announId="+EasyWin.busId);
	$("#announTalkMenuLi").attr("class","active");
	//公告详情
	$("#announbaseMenuLi").click(function(){
		//$("#myTab11").hide();
		$("#otherannounAttrIframe").css("display","block");
		$("#announBase").css("display","block");
		$(this).parent().find("li").removeAttr("class");
		$("#announbaseMenuLi").attr("class","active");
	});
	//讨论
	$("#announTalkMenuLi").click(function(){
		$("#otherannounAttrIframe").css("display","block");
		//$("#announBase").css("display","block");
		$(this).parent().find("li").removeAttr("class");
		$("#announTalkMenuLi").attr("class","active");
		$("#otherannounAttrIframe").attr("src","/announcement/announTalkPage?sid="+EasyWin.sid+"&pager.pageSize=10&announId="+EasyWin.busId);
	});
	//公告浏览记录
	$("#announViewRecord").click(function(){
		$("#otherannounAttrIframe").css("display","block");
		//$("#announBase").css("display","block");
		$(this).parent().find("li").removeAttr("class");
		$("#announViewRecord").attr("class","active");
		$("#otherannounAttrIframe").attr("src","/common/listViewRecord?sid="+EasyWin.sid+"&busId="+EasyWin.busId+"&busType=039&ifreamName=otherannounAttrIframe&pager.pageSize=10");
	});
});
////查看评论
//function viewAllTalk(){
//	$("#myTab11").show();
//	$("#otherannounAttrIframe").css("display","block");
//	$("#announBase").css("display","none");
//	$("#myTab11").find("li").removeAttr("class");
//	$("#announTalkMenuLi").attr("class","active");
//	$("#otherannounAttrIframe").attr("src","/announcement/announTalkPage?sid="+EasyWin.sid+"&pager.pageSize=10&announId="+EasyWin.busId);
//}
////查看记录
//function viewRecord(){
//	$("#myTab11").show();
//	$("#otherannounAttrIframe").show();
//	$("#announBase").hide();
//	$("#myTab11").find("li").removeAttr("class");
//	$("#announViewRecord").attr("class","active");
//	$("#otherannounAttrIframe").attr("src","/common/listViewRecord?sid="+EasyWin.sid+"&busId="+EasyWin.busId+"&busType=039&ifreamName=otherannounAttrIframe&pager.pageSize=10");
//}