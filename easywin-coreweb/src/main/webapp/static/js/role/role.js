//任务添加界面
var taskOptForm = {
    chooseExectors:function(ts,sid,id){
        var relateSelect = $(ts).attr("relateSelect");
        var relateImgDiv = $(ts).attr("relateImgDiv");
        userMore(relateSelect, 'yes', sid,null,null,function(options){
            var userIds = taskOptForm.optUserData(options,relateSelect,relateImgDiv);
            //异步加载头像信息
            taskOptForm.loadUserImg(userIds,sid,id);
        })
    },appendUsedUser:function(userObj,relateSelect,relateImgDiv){
        var userId = userObj.id;
        var optObj = $("#"+relateSelect).find("option[value='"+userId+"']");
        if(!optObj || !optObj.get(0)){
            var option = $("<option></option>");
            $(option).attr("value",userObj.id);
            $(option).attr("selected","selected");
            $(option).html(userObj.userName);
            $("#"+relateSelect).append($(option));

            //添加头像
            var headImgDiv = $('<div  class="online-list " style="float:left;padding-top:5px" title="双击移除"></div>');
            $(headImgDiv).data("userId",userObj.id);
            var headImg = $('<img src="/downLoad/userImg/' + userObj.comId + '/' + userObj.id + '" class="user-avatar"/>');
            var headImgName = $('<span class="user-name"></span>');
            $(headImgName).html(userObj.userName);

            $(headImgDiv).append($(headImg));
            $(headImgDiv).append($(headImgName));

            $("#"+relateImgDiv).append($(headImgDiv));

            $(headImgDiv).on("dblclick",function(){
                var userId = $(this).data("userId");
                $(this).remove();
                $("#"+relateSelect).find("option[value='"+userId+"']").remove();
            })

        }
    },optUserData:function(options,relateSelect,relateImgDiv){
        var userIds =new Array();
        //首先清除数据
        $("#"+relateSelect).html('');
        $("#"+relateImgDiv).html('');
        if(options && options[0]){
            $.each(options,function(index,opt){
                userIds.push(opt.value);
                var option = $("<option></option>");
                $(option).attr("value",opt.value);
                $(option).attr("selected","selected");
                $(option).html(opt.text);
                $("#"+relateSelect).append($(option));

                //添加头像
                var headImgDiv = $('<div  class="online-list " style="float:left;padding-top:5px" title="双击移除"></div>');
                $(headImgDiv).data("userId",opt.value);
                var headImg = $('<img src="/downLoad/userImg/'+comId+'/'+opt.value+'" class="user-avatar"/>')
                $(headImg).attr("id","userImg_"+opt.value);
                var headImgName = $('<span class="user-name"></span>');
                $(headImgName).html(opt.text);

                $(headImgDiv).append($(headImg));
                $(headImgDiv).append($(headImgName));

                $("#"+relateImgDiv).append($(headImgDiv));

                $(headImgDiv).on("dblclick",function(){
                    var userId = $(this).data("userId");
                    $(this).remove();
                    $("#"+relateSelect).find("option[value='"+userId+"']").remove();
                })

            })
        }
        return userIds;
    },loadUserImg:function(userIds,sid,id){
        var param = {
            "sid":sid,
            "userIds":userIds.toString()
        }
        postUrl("/userInfo/selectedUsersInfo",param,function(result){
        	
        	var code = result.code;
			if(code == 0){
				var comId = result.data;
				$.each(userIds,function(index,userId){
					var imgObj = $("#userImg_"+userId);
					var imgSrc = "/downLoad/userImg/"+comId+"/"+userId+"?sid="+pageParam.sid;
					$(imgObj).attr("src",imgSrc);
				})
				
				//重新循环避免异步问题
				$.each(users,function(index,userObj){
					$.ajax({
						type : "post",
						url : "/role/addBindingUser",
						data : {userId:userObj.id,roleId:id,sid:sid},
						success : function (data) {
							var d = jQuery.parseJSON(data);
							if(d.flag == 1){
								showNotification(1,"添加成功!");
							}else if(d.flag == 0){
								showNotification(2,"添加失败!");
							}
						}
					});
				});
			}
        })
    }
}