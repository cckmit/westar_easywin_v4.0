/**
 * 上传头像
 * @param sid
 * @param filePicker
 * @param fileShowList
 * @param fileVal
 * @param tempDir
 * @param meetingRoomId
 * @return
 */
function loadUpfilesForHead(sid,filePicker,fileShowList,fileVal,tempDir,meetingRoomId){
	//上传文件的URL
	var backEndUrl = '/upload/addDepartImgFile?sid='+sid+'&t='+Math.random();
    //各个不同的文件的MD5,用于上传
    var destMD5Dir = {md5:"",fileVal:fileVal,tempDir:tempDir,meetingRoomId:meetingRoomId};   //附件MD5信息
	WebUploader.Uploader.register({//注册插件
        "before-send-file": "beforeSendFile"
    },{
	    beforeSendFile: function(file){
    	loadImgIndex = window.top.layer.load(0);
	        //秒传验证
	        var task = new $.Deferred();
	        (new WebUploader.Uploader()).md5File(file).progress(function(percentage){
	        }).then(function(val){
	           //用于上传
	            destMD5Dir.md5 = val;
	            task.resolve();
	        });
	        return $.when(task);
	    }
	});
	var uploader = WebUploader.create({
	    swf: '/static/plugins/webuploader/Uploader.swf?t='+Math.random(),
	    server: backEndUrl,
	    pick: {id:'#'+filePicker,multiple: false},
	    duplicate: false,//不能选择重复的文件
	    resize: false,
	    auto: false,
	    fileNumLimit: null,
	    fileVal:fileVal,   //指明参数名称，后台也用这个参数接收文件
	    fileSingleSizeLimit: 200*1024*1024,
	    //fileType:'rar,zip,doc,xls,docx,xlsx,pdf'
	    accept: {
	    	title: 'Images',
    	    extensions: 'gif,jpg,jpeg,bmp,png',
    	    mimeTypes: 'image/gif,image/jpg,image/jpeg,image/bmp,image/png'
	    },
	    chunked:true,
	    chunkSize:5*1024*1024,
	    thread:2,
	    formData:function(){return $.extend(true, {}, destMD5Dir);}
	});
	uploader.on("error",function(type,reason){
    	if (type=="Q_TYPE_DENIED"){
    		window.top.layer.alert(reason.ext+"格式不支持")
    	}else if(type=="Q_EXCEED_NUM_LIMIT"){
    		window.top.layer.alert('上传的文件数量已经超出系统限制的'+reason+"个");
    	}else if(type=="F_EXCEED_SIZE"){
    		window.top.layer.alert('文件太大不支持');
    	}
	});
    uploader.on('fileQueued', function( file ) {  // webuploader事件.当选择文件后，文件被加载到文件队列中，触发该事件。等效于 uploader.onFileueued = function(file){...} ，类似js的事件定义。
	  uploader.upload(file);
   });
	 uploader.on('uploadSuccess', function (file, data) {
	      var chunksTotal = 0;
            if((chunksTotal = Math.ceil(file.size/(5*1024*1024))) >= 1){
                //合并请求
                $.ajax({
                    type: "POST"
                    , url: backEndUrl
                    , data: {
                        status: "chunksMerge"
                        , name: file.name
                        , chunks: chunksTotal
                        , ext: file.ext
                        , md5: data.fileMd5
                        , size:file.size
                        ,tempDir:tempDir
                        ,meetingRoomId:meetingRoomId
                    }
                    , cache: false
                    , dataType: "json"
                }).then(function(data, textStatus, jqXHR){
	   				 if(data.status=='y'){
	   					var $list = $("#"+fileShowList);
	   				   if($list.find("img").length==0){
	   					   var $li = $('<img id="' + file.id + ' style="width:210px;height:210px">');
	   					   $list.html( $li );
	   				   }
						// 原图
						$("#orgFilePath").val(data.orgImgPath);
						$("#orgFileName").val(data.orgImgName);
						$("#largeImgPath").val(data.largeImgPath);
						var org =data.largeImgPath+"?t="+Math.random();
						var $list = $("#"+fileShowList);
						$list.find("img").attr("src",org)
						$list.find("img").attr("style","width:210px;height:210px")
			         }
                	
                	uploader.removeFile(file, true);
                	window.top.layer.close(loadImgIndex);
                	
                }, function(jqXHR, textStatus, errorThrown){
                	uploader.removeFile(file, true);
                	window.top.layer.close(loadImgIndex);
                	
                });
            }
	      
    });
    uploader.on('uploadError', function (file,reason) {
    	 window.top.layer.close(loadImgIndex);
    	 uploader.removeFile(file, true);
	});
    
    //修复model内部点击不会触发选择文件的BUG
    $("#"+filePicker+" .webuploader-pick").click(function () {
        $("#"+filePicker+" :file").click();
    });
}
//上传图片
function updateImg(sid){
	 $("#imgForm").ajaxSubmit({
	        type:"post",
	        url:"/userInfo/editPic?sid="+sid+"&t="+Math.random(),
	        dataType: "json", 
	        beforeSubmit:function(a,f,o){
		        if(!$("#largeImgPath").val()){
		        	showNotification(2,"请选择头像！")
		        	return false;
			    }
	    	},
	        success:function(data){
		        if('y'==data.status){
		        	window.location.reload();
				}else{
					showNotification(2,"系统错误，请联系管理人员！")
				}
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	showNotification(2,"系统错误，请联系管理人员！")
	        }
	    });
}