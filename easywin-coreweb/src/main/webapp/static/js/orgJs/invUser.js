	var vali;
	var formId='';
	$(function() {
	vali=$('.subform').Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
	},callback:function(form){
		ajaxInvUsers(formId);
		return false;
	},
	showAllError:true
		});
	})
	var trNum=1;
	
	//手机号模板
	var phoneTrHtml=$('<tr><td style="border: 0">手机号</td><td style="border: 0"><div style="width:90%;float:left"><input type="text" class="form-control small" ajaxurl="/userInfo/checkInvUser?sid='+sid+'&type=phone" datatype="m" ignore="ignore" name="invUsers"/></div></td></tr>');
	//邮箱模板
	var emailTrHtml=$('<tr><td style="border: 0">Email</td><td style="border: 0"><div style="width:90%;float:left"><input type="text" class="form-control small" ajaxurl="/userInfo/checkInvUser?sid='+sid+'&type=email" datatype="e" ignore="ignore" name="invUsers"/></div></td></tr>');
	
	//若是点击的时最后一个input则自动添加
	$(document).on("focus","#phoneForm input[type='text']:last",function(){
		var aa = $("#phoneForm input[type='text']").last();
		var  phoneTr = $(phoneTrHtml).clone();
		
		var delHtml = $("<span style='float:left;margin-top:9px;margin-left:10px'><a href=\"javascript:void(0)\" title='删除' class='fa fa-times-circle-o'></a></span>");
		$(phoneTr).find("td").eq(1).append(delHtml)
		//卸载聚焦事件
		$("#phoneForm table").append(phoneTr);
		vali.addRule();
		initInputKeyDown();
		
	})
	//若是点击的时最后一个input则自动添加
	$(document).on("focus","#emailForm input[type='text']:last",function(){
		var aa = $("#emailForm input[type='text']").last();
		var  emailTr = $(emailTrHtml).clone();
		
		var delHtml = $("<span style='float:left;margin-top:9px;margin-left:10px'><a href=\"javascript:void(0)\" title='删除' class='fa fa-times-circle-o'></a></span>");
		$(emailTr).find("td").eq(1).append(delHtml)
		//卸载聚焦事件
		$("#emailForm table").append(emailTr);
		vali.addRule();
		initInputKeyDown();
	})
	//移除数据
	$(document).on("click",".subform a",function(){
		$(this).parents("tr").remove();
	})
	//屏蔽回车事件
	$(document).ready(function() {
		initInputKeyDown();
	});
	
	//屏蔽回车事件
	function initInputKeyDown(){
		$(document).find("input[type='text']").keydown(function(e) {
	        if (e.keyCode == 13) {
	        	return false;
	        }
	    });
	}
	//异步邀请人员加入
	function ajaxInvUsers(formId){
		 //异步提交表单
	    $("#"+formId).ajaxSubmit({
	        type:"post",
	        url:"/userInfo/inviteUser?t="+Math.random(),
	        dataType: "json",
	        async:false,
	        beforeSubmit:function (a,f,o){
	        	$("#subState").val(1)
			}, 
			traditional :true,
	        success:function(data){
	        	if(data.status=='y'){
	        		window.self.location.reload();
	        	}else{
	        		layer.alert(data.info, {title:"警告",icon: 5,btn:["关闭"]});  
	        	}
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	window.top.layer.alert("系统错误，请联系管理人员",{icon:7,title:false,closeBtn:0,btn:["关闭"]})
	        	return false;
	        }
	    });
	}
	//修改邀请方式
	function changeInvtWay(index,ts){
		$(ts).parent().parent().find("li").removeAttr("class");
		$(ts).parent().parent().find("li a").css("border-bottom","");
		$(ts).parent().parent().find("li a").css("margin-bottom","");
		$(ts).parent().attr("class","active")
		$(".tab-pane").removeClass("active");
		if(index==1){//手机邀请
			$("#phoneDiv").addClass("active");
			$(ts).css("border-bottom","1px solid rgba(0,0,0,0.1)");
			$(ts).css("margin-bottom","1px");
		}else if(index==2){//邮箱邀请
			$("#emailDiv").addClass("active")
			$(ts).css("border-bottom","1px solid rgba(0,0,0,0.1)");
			$(ts).css("margin-bottom","1px");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//一定要写一个邮箱
function checkEmail(form){
	var emails = $(form).find("input[name='invUsers']");
	var flag = true;
	formId = $(form).attr("id")
	for(var i=0;i<emails.length;i++){
		var val = $(emails[i]).val();
		if(''!=val){
			$(form).submit();
			flag = false;
			break;
		}
	}
	if(flag){
		$(emails[0]).focus();
		layui.use('layer', function(){
			var layer = layui.layer;
			window.top.layer.alert("请至少邀请一位不在系统的同事");
		});
	}
	return false;
}
	/**
  	 * 百度控件上传附件
  	 * @param fileIdList 上传后附件的主键集合
  	 * @param sid session标识
  	 * @param iframe 所在ifream
  	 * @param filePicker 附件上传按钮
  	 * @param fileShowList 附件上传展示界面
  	 * @return
  	 */
  	 function loadUpfilesForInvUser(sid,filePicker,fileVal){
        //各个不同的文件的MD5,用于上传
        var destMD5Dir = {md5:"",fileVal:fileVal};//附件MD5信息
		WebUploader.Uploader.register({//注册插件
            "before-send-file": "beforeSendFile"
        }, {
        beforeSendFile: function(file){
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
		    pick: {id:'#'+filePicker,multiple: false},
		    server: '/upload/loadUserFile?sid='+sid+'&t='+Math.random(),
		    duplicate: false,//不能选择重复的文件
		    resize: false,
		    auto: false,
		    fileNumLimit: 1,
		    fileVal:fileVal,   //指明参数名称，后台也用这个参数接收文件
		    fileSingleSizeLimit: 200*1024*1024,
		    //fileType:'rar,zip,doc,xls,docx,xlsx,pdf'
		    accept: {
		        title: "*.xls;*.xlsx;*.txt;",
		        extensions: "xls,xlsx,txt",
		        mimeTypes: ".xls,.xlsx,.txt"
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
	                    , url: '/upload/loadUserFile?sid='+sid+'&t='+Math.random()
	                    , data: {
	                        status: "chunksMerge"
	                        , name: file.name
	                        , chunks: chunksTotal
	                        , ext: file.ext
	                        , md5: data.fileMd5
	                        , size:file.size
	                    }
	                    , cache: false
	                    , dataType: "json"
	                }).then(function(data, textStatus, jqXHR){
	                	if(data.status=='y'){
	                		invUser(sid,data)
	                	}
	                	uploader.removeFile(file, true);
	                }, function(jqXHR, textStatus, errorThrown){
	                	uploader.removeFile(file, true);
	                });
	            }
		      
	    });
	    uploader.on('uploadError', function (file,reason) {
	    	showNotification(2,"上传出错");
	    	uploader.removeFile(file, true);
		});
	    
	    //修复model内部点击不会触发选择文件的BUG
	    $("#"+filePicker+" .webuploader-pick").click(function () {
            $("#"+filePicker+" :file").click();
        });
  	 }