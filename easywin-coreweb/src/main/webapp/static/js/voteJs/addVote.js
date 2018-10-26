/**
 * 上传头像
 * @param sid
 * @param filePicker
 * @param fileShowList
 * @param fileVal
 * @param tempDir
 * @param meetingId
 * @return
 */
function loadUpfilesForVote(sid,filePicker,fileShowList,fileVal,tempDir,index){
	//上传文件的URL
	var backEndUrl = '/upload/addDepartImgFile?sid='+sid+'&t='+Math.random();
    //各个不同的文件的MD5,用于上传
    var destMD5Dir = {md5:"",fileVal:fileVal,tempDir:tempDir,meetingRoomId:0};   //附件MD5信息
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
                        ,meetingRoomId:0
                    }
                    , cache: false
                    , dataType: "json"
                }).then(function(rs, textStatus, jqXHR){
	   				 if(rs.status=='y'){
	   					var fileName = rs.fileName;
						//中图片
						var middle =rs.midImgPath+"?t="+Math.random();
						var imgInfo = "<span class='showpic' style='float: left;' title='"+rs.fileName+"'><img width='120' height='90' src='"+middle+"'/></span>";
			        	if(charLength(fileName)>30){
			        		 fileName =cutstr(fileName,30);
							 imgInfo = imgInfo + "<span class='showinfo' style='float: left;' title='"+rs.fileName+"'>"+fileName+"<br><a href=\"javascript:void(0)\" onclick=\"removeImg('showpic"+index+"',this)\" class='fa fa-trash-o' style='margin-top: 25px;margin-left: 25px' title='删除'></a><span>";
			        	}else{
							 imgInfo = imgInfo + "<span class='showinfo' style='float: left;'>"+fileName+"<br><a href=\"javascript:void(0)\" onclick=\"removeImg('showpic"+index+"',this)\" title='删除' class='fa fa-trash-o' style='margin-top: 25px;margin-left: 25px'></a><span>";
			        	}
						$("#showpic"+index).html(imgInfo);
						
						$("#voteChooses"+index+"_orgImgPath").val(rs.orgImgPath);
						$("#voteChooses"+index+"_largeImgPath").val(rs.largeImgPath);
						$("#voteChooses"+index+"_midImgPath").val(rs.midImgPath);
						resizeVoteH('voteInfo')
			         }
	   				 window.top.layer.close(loadImgIndex);
                	uploader.removeFile(file, true);
                	
                }, function(jqXHR, textStatus, errorThrown){
                	window.top.layer.close(loadImgIndex);
                	uploader.removeFile(file, true);
                	
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

//添加一个选项
var count=1;
function addline(sid,userId,comId){
	count = count+1;
	
	var html ='\n <div id="option1" class="singleOption" style="clear: both">';
	html+='\n 		<div class="optionHead" style="float: left;margin-top: 8px">';
	html+='\n			<span>投票选项：</span>';
	html+='\n		</div>';
	html+='\n		<div class="optionBody" style="float: left;border: 1px solid #f0f0f0;">';
	html+='\n			<div style="width: 350px" >';
	html+='\n				<input class="form-control-new" type="text" name="voteChooses['+count+'].content" id="voteChoose1_content"/>';
	html+='\n			</div>';
	html+='\n			<div class="optionPicShow">';
	html+='\n 				<input type="hidden" name="voteChooses['+count+'].orgImgPath" id="voteChooses'+count+'_orgImgPath"/>';
	html+='\n 				<input type="hidden" name="voteChooses['+count+'].largeImgPath" id="voteChooses'+count+'_largeImgPath"/>';
	html+='\n				<input type="hidden" name="voteChooses['+count+'].midImgPath" id="voteChooses'+count+'_midImgPath"/>';
	html+='\n				<div id="showpic'+count+'">';
	html+='\n				</div>';
	html+='\n			</div>';
	html+='\n		</div>';
	html+='\n		<div class="optionFile" style="float:left;margin: 5px;font-size: 12px">';
	html+='\n			<div id="imgPicker'+count+'" class="pull-left">选择图片</div>';
	html+='\n			<script type="text/javascript">';
	html+='\n				loadUpfilesForVote(\''+sid+'\',\'imgPicker'+count+'\',\'showpic'+count+'\',\'pic'+count+'\',\'voteImg\','+count+');';
	html+='\n			</script>';
	html+='\n		</div>';
	html+='\n 		<div style="float: left;width: 60px" id="opt'+count+'">';
	html+='\n 			<div class="optdel" style="float: left;margin-top: 10px;margin-left: 10px">';
	html+='\n				<a href="javascript:void(0)" onclick="removeopt(this,'+count+')" title="移除" class="fa fa-times fa-lg"></a>';
	html+='\n			</div>';
	html+='\n			<div class="optadd" style="float: left; margin-top: 10px;margin-left: 10px">';
	html+='\n				<a href="javascript:void(0)" onclick="addline(\''+sid+'\',\''+userId+'\',\''+comId+'\')" title="添加" class="fa fa-plus-square-o fa-lg"></a>';
	html+='\n			</div>';
	html+='\n		</div>';
	html+='\n	</div>';
	
	$(".optadd").remove();
	$("#options").append(html);
	var opts = $("#countopt").html();
	$("#countopt").html(parseInt(opts)+1);

	var maxChoose = $("#maxChoose").find("option").length+1;
	$("#maxChoose").append("<option value=\""+maxChoose+"\">"+maxChoose+"</option>");
	resizeVoteH('voteInfo')
}

/**
 * 发起投票后左边页面添加数据
 * @param vote
 * @param userInfo
 * @param sid
 * @return
 */
function getAddVoteHtml(vote,userInfo,sid){
	var html = '<div class="ws-shareBox">';
	html +='\n 		<div class="shareHead" style="text-align: center;">';
	html +='\n		<img src="/downLoad/userImg/'+userInfo.comId+'/'+userInfo.id+'?sid='+sid+'&size=1" />';
	html +='\n 			<span style="position: relative;top: 10px;" class="ws-blue">'+userInfo.userName+'</span>';
	html +='\n		</div>';
	html +='\n 		<div class="shareText ws-listQues">';
	if(null!=vote.attentionState && vote.attentionState==1){
		html +='\n  		<a href="javascript:void(0)" class="fa fa-star ws-star" title="取消关注" onclick="changeAtten(\'004\','+vote.id+',1,\''+sid+'\',this)"></a>';
	}else{
		html +='\n  		<a href="javascript:void(0)" class="fa fa-star-o" title="标识关注" onclick="changeAtten(\'004\','+vote.id+',0,\''+sid+'\',this)"></a>';
	}
	html +='\n 			<span style="margin-left: 10px">发起了投票</span>';
	html +='\n 			<div class="ws-type" style="clear: both">';
	html +='\n 				<time>'+vote.recordCreateTime+'</time>';
	html +='\n 				<span style="margin-left: 15px">';
	html +='\n 					已投票：<b id="vote'+vote.id+'">0</b>';
	html +='\n 				</span>';
	html +='\n 			</div>';
	html +='\n 			<div class="ws-voteWrap">';
	html +='\n 			<p>';
	html +='\n 				<a href="javascript:void(0)" onclick="setSrc(this,'+vote.id+')">'+vote.voteContent+'</a>';
	html +='\n 			</p>';
	html +='\n 			<div class="ws-clear"></div>';
	html +='\n 				<small> 投票还在开放 </small>';
	html +='\n 				<span class="optSpan hidden">';
	html +='\n 				<a class="fa fa-trash-o" style="margin:0px 15px" href="javascript:void(0)" onclick="delVote('+vote.id+')" title="删除"></a>';
	html +='\n 				</span>';
	html +='\n 			</div>';
	html +='\n 			</div>';
	html +='\n 		<div class="ws-clear"></div>';
	html +='\n </div>';
	
	return html;
}