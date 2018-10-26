$(function(){
	$('.subform').Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		datatype:{
			"ENcode":function(gets,obj,curform,regxp){
				var text = $(obj).val(); 
				if(!text){
					return false;
				}
				//匹配这些中文标点符号 。 ？ ！ ， 、 ； ： “ ” ‘ ' （ ） 《 》 〈 〉 【 】 『 』 「 」 ﹃ ﹄ 〔 〕 … — ～ ﹏ ￥ 
				var reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5]/;
				 if(reg.test(text)){  
					 return "不能输入中文标点字符";
				}else{ 
					return true;
				}
			}
		},
		callback:function(form){
			return true;
		},
		showAllError : true
	});
	//用户失焦筛选
	$("#condition").blur(function(){
		$("#userList").submit();
	});
	//文本框绑定回车提交事件
	$("#condition").bind("keydown",function(event){
        if(event.keyCode == "13")    
        {
        	$("#userList").submit();
        }
    });
});
//添加分组
function add(sid){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	window.location.href='/selfGroup/addUserGroupPage?sid='+sid+'&activityMenu=self_m_3.6.1&redirectPage='+encodeURIComponent(window.location.href);
}
//修改分组
function update(id,sid){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	window.location.href='/selfGroup/updateUserGroupPage?id='+id+'&activityMenu=self_m_3.6.2&sid='+sid+'&redirectPage='+encodeURIComponent(window.location.href);
}

//批量删除分组
function del(groupId){
	window.top.layer.confirm('确定要删除该组群吗？', {icon: 3, title:'提示'}, function(index){
	  $("#delForm input[name='redirectPage']").val(window.location.href);
	  $("#ids").val(groupId);
	  $('#delForm').submit();
	  window.top.layer.close(index);
	});
}
//删除分组下组员
function delGroupUser(ts,groupId,delUserId){
	var plen = $(ts).parents(".grpUserList").children("div").length;
	if(plen>1){
		window.top.layer.confirm('确定要删除该成员吗？', {icon: 3, title:'提示'}, function(index){
			$("#delForm").attr("action","/selfGroup/delGroupUser");
			$("#delForm input[name='redirectPage']").val(window.location.href);
			$("#groupId").val(groupId);
			$("#delUserId").val(delUserId);
			$('#delForm').submit();
			window.top.layer.close(index);
		});
	}else{
		window.top.layer.alert("分组最后一个成员不能删除",{
			icon:2,
			title:'<font color="red">警告</font>',
			time:1500,
			closeBtn: 0,
			fix: true, //固定
			move: false,
			})
	}
}
//添加部门成员
function appendUser(checked,str){
  var json = eval('(' + str + ')'); 
  if(checked==true){
   if($("#listUser_id option[value='"+json.id+"']").length==0){
	   	if(null!=json.name&&""!=json.name){
		     $('#listUser_id').append("<option value='"+json.id+"'>"+json.name+"</option>");
	   	}else{
		     $('#listUser_id').append("<option value='"+json.id+"'>"+json.email+"</option>");
	   	}
   }
  }else{
   var selectobj = document.getElementById("listUser_id");
   for (var i = 0; i < selectobj.options.length; i++) {
		  if(selectobj.options[i].value==json.id){
		    selectobj.options[i] = null;
		    break;
		  }
   }
  }
}
//移除所选部门成员
 function removeOne(ts){
  var depid="";
  $('#listUser_id').find("option:selected").each(function(index){
	  allUser.uncheck($(this).attr("value"));
  });
  removeClick(ts.id);
}
function tjGrpForm(){
	 var selectobj = document.getElementById("listUser_id");
	 var grpName = $("#grpName").val();
	 if(strIsNull(grpName)){
		 art.dialog({"content":"请填写组群名称"}).time(2);
		 $("#grpName").focus()
		 return false;
	}
	 if(selectobj.options.length==0){
		 art.dialog({"content":"分组成员未选择"}).time(2);
	 }else{
		$('#groupForm').submit();
	 }
}
//被选人员提交
function selfGroupUserSelected(){
	if(!strIsNull($("#listUser_id"))){
		selected("listUser_id");
	}
}