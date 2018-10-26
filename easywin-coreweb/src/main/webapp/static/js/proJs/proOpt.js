//任务添加界面
var taskOptForm = {
    chooseUser:function(ts,isEdit){
        var relateSelect = $(ts).attr("relateSelect");
        var relateImgDiv = $(ts).attr("relateImgDiv");
        userOne(null, null, pageParam,pageParam.sid,function(options){
            var userIds = taskOptForm.optUserData(options,relateSelect,relateImgDiv,isEdit);
            //异步加载头像信息
            taskOptForm.loadUserImg(userIds);
        })
    },appendUsedUser:function(userObj,relateSelect,relateImgDiv){
        var optObj = $("#"+relateSelect).val();
        if(!optObj){
            $("#"+relateSelect).val(userObj.id);
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
                $(this).remove();
                $("#"+relateSelect).remove();
            })
        }
    },optUserData:function(options,relateSelect,relateImgDiv,isEdit){
        var userIds =new Array();
        //首先清除数据
        $("#"+relateSelect).val('');
        $("#"+relateImgDiv).html('');
        if(options && options[0]){
            $.each(options,function(index,opt){
                userIds.push(opt.value);
                var option = $("<option></option>");
                $(option).attr("value",opt.value);
                $(option).attr("selected","selected");
                $(option).html(opt.text);
                $("#"+relateSelect).val(opt.value);

                //添加头像
                var headImgDiv = $('<div  class="online-list " style="float:left;padding-top:5px" title="双击移除"></div>');
                $(headImgDiv).data("userId",opt.value);
                var headImg = $('<img src="/downLoad/userImg/' + pageParam.comId + '/' + opt.value + '" class="user-avatar"/>')
                $(headImg).attr("id","userImg_"+opt.value);
                var headImgName = $('<span class="user-name"></span>');
                $(headImgName).html(opt.text);

                $(headImgDiv).append($(headImg));
                $(headImgDiv).append($(headImgName));

                $("#"+relateImgDiv).append($(headImgDiv));

                $(headImgDiv).on("dblclick",function(){
                    $(this).remove();
                    $("#"+relateSelect).remove();
                });
                if(isEdit == 1){
                    managerUpdate();
                }else if(isEdit == 0){
                    principalUpdate();
                }
            })
        }
        return userIds;
    },loadUserImg:function(userIds){
        var param = {
            "sid":pageParam.sid,
            "userIds":userIds.toString()
        }
        postUrl("/userInfo/addUsedUser",param,function(result){
            var code = result.code;
            if(code == 0){
                var comId = result.data;
                $.each(userIds,function(index,userId){
                	var imgObj = $("#userImg_"+userId);
                    var imgSrc = "/downLoad/userImg/"+comId+"/"+userId+"?sid="+pageParam.sid;
                    $(imgObj).attr("src",imgSrc);
                })
            }
        })
    }
}