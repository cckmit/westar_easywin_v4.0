$(function() {
	
	FileCenter.initEvent();
	FolderObj.initZtree();
	FolderObj.initEvent();
	FileObj.initData(0);
	FileObj.initEvent();
	
	$("body").on("click","a[data-dirName='1']",function(){
		var folder = $(this).parents('tr').data("classify");
		//重设阶段主键
		var classifyId = folder.id;
		$("#classifyId").val(classifyId);
		//重设页码
		FileObj.initData(0);
		
		constrTabInfo();
	})
	$("body").on("click","a[data-folderTab]",function(){
		//重设阶段主键
		var classifyId = $(this).attr("data-folderTab");
		$("#classifyId").val(classifyId);
		//重设页码
		FileObj.initData(0);
		
		constrTabInfo();
	});
	
	setStyle();
});
var setting = {
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick: onClick
		}
};

function dirFile(classifyId){
	$("#classifyId").val(classifyId);
	//重设页码
	FileObj.initData(0);
	constrTabInfo();
}
//查询指定项目阶段
function onClick(event, treeId, treeNode, clickFlag){
	//重设阶段主键
	var classifyId = treeNode.id;
	$("#classifyId").val(classifyId);
	//重设页码
	FileObj.initData(0);
	constrTabInfo();
	return
}
//构建顶部信息
function constrTabInfo(){
	var AMod =$('<a href="javascript:void(0)" style="font-size:15px;"></a>');
	var classifyId = $("#classifyId").val();
	var node = zTreeObj.getNodeByParam("id",classifyId );
	$("#folderSpan").empty();
	if(!node || node.id == -1){
		var aa = $(AMod).clone();
		$(aa).html("全部文件 ");
		$("#folderSpan").prepend($(aa));
		return;
	}
	var nodeName = node.name;
	$("#folderSpan").html(nodeName)
	var level = node.level;
	for(var i=level;i>=1;i--){
		node = zTreeObj.getNodeByParam("id",node.pId);
		if(node && node.id>0){
			zTreeObj.expandNode(node, true, false);//将指定ID节点展开
			
			nodeName = node.name;
			console.log(nodeName)
			$("#folderSpan").prepend('&gt;&gt;');
			var aa = $(AMod).clone();
			$(aa).attr("data-folderTab",node.id);
			$(aa).html(nodeName);
			$("#folderSpan").prepend($(aa));
		}else{
			$("#folderSpan").prepend('&gt;&gt;');
			var aa = $(AMod).clone();
			$(aa).attr("data-folderTab",-1);
			$(aa).html("全部文件 ");
			$("#folderSpan").prepend($(aa));
		}
	}
}

var pageNum = 0;
var pageSize = 10;
var zTreeObj;
var zNodes = new Array();
//操作文件夹
var FolderObj = {
		initEvent:function(){
			//编辑文件夹
			$("body").on("click",".editDir",function(){
				var _this = $(this);
				FolderObj.Opt.editDirPre($(_this),function(){
					
				});
			});
			//移动文件夹
			$("body").on("click",".moveDir",function(){
				var _this = $(this);
				FolderObj.Opt.moveDir($(_this),function(){
					$(_this).parents('tr').remove();
					FolderObj.initZtree();
				});
			});
			//删除文件夹
			$("body").on("click",".delDir",function(){
				var _this = $(this);
				FolderObj.Opt.delDir($(_this),function(){
					$(_this).parents('tr').remove();
					FolderObj.initZtree();
					 //重新加载文件数据
					FileObj.initData(0);
				});
			});
			
			//取消修改文件夹
			$("body").on("click",".cancleUpdateDir",function(){
				var classify = $(this).parents('tr').data('classify');
				var floderTr = FolderObj.constrFolder(classify);
				$(this).parents('tr').replaceWith($(floderTr));
				setStyle();
			});
			//修改文件夹
			$("body").on("click",".updateDir",function(){
				var _this = $(this);
				FolderObj.Opt.updateDir(_this,function(classify){
					var floderTr = FolderObj.constrFolder(classify);
					$(_this).parents('tr').replaceWith($(floderTr));
					setStyle();
					FolderObj.initZtree();
				});
			});
			
		},initZtree:function(classifyId){//构架树形结构
			//销毁所有的zTree
			$.fn.zTree.destroy();
			zNodes = new Array();
			var params = {
					random : Math.random(),
					sid:EasyWin.sid,
					id:classifyId?classifyId:-1,
					type:'023'
			}
			//通过ajax获取部门树
			$.post('/fileCenter/ajaxListTreeFolder', params, function(tree) {
				
				//创建一个默认的根节点
				var rootNode = new Object;
				rootNode.id = -1;
				rootNode.name = '全部文件';
				rootNode.open = true;
				rootNode.enabled = 1;
				rootNode.childOuter = false;
				rootNode.icon="/static/images/folder_open.png";
				zNodes.push(rootNode);				
				
				if(tree!=null){
					for ( var i = 0; i < tree.length; i++) {
							var node = new Object;
							node.id = tree[i].id;
							if (tree[i].parentId != null) {
								node.pId = tree[i].parentId;
							}
							node.name = tree[i].typeName;
							node.level = tree[i].level;
							node.userId = tree[i].userId;
							node.pubState = tree[i].pubState;
							node.icon="/static/images/folder_open.png";
							node.open = false;
							zNodes.push(node);
					}
					
				}
				$("#zTreeFolder").empty();
				zTreeObj = $.fn.zTree.init($("#zTreeFolder"), setting, zNodes);
			},"json");
		},constrFolders:function(folders){
			$("#listDirs").empty();
			if(!folders || !folders[0]){//没有文件夹
				return;
			}
			$.each(folders,function(index,classify){
				 var floderTr = FolderObj.constrFolder(classify);
				 $("#listDirs").append($(floderTr));
				 setStyle();
			});
			
			setStyle();
		},constrFolder:function(classify,callback){
			var floderTr = $('<tr class="dirInfo"></tr>');
			$(floderTr).data("classify",classify);
			
			var indexTd = $('<td height="40"></td>');
			var indexLable = $('<label></label>')
			var indexCheckbox = $('<input type="checkbox" name="ids" fileType="dir" />');
			var indexName = $('<span class="text"></span>');
			
			$(indexLable).append($(indexCheckbox));
			$(indexLable).append($(indexName));
			
			$(indexTd).append($(indexLable));
			
			$(floderTr).append($(indexTd));
			
			var dirNameTd = $('<td height="40"></td>');
			var img = $('<img src="/static/images/folder_open.png" style="clear: both;border-radius: 0px;height: auto;width: auto;border:0px" />');
			var dirNameA = $('<a href="javascript:void(0)" data-dirName="1"></a>');
			$(dirNameA).html(classify.typeName)
			$(dirNameTd).append(img);
			$(dirNameTd).append(dirNameA);
			$(floderTr).append($(dirNameTd));
			
			var dirFromTd = $('<td height="40">--</td>');
			$(floderTr).append($(dirFromTd));
			
			var dirSizeTd = $('<td height="40">--</td>');
			$(floderTr).append($(dirSizeTd));
			
			var dirExtTd = $('<td height="40">--</td>');
			$(floderTr).append($(dirExtTd));
			
			var userTd = $('<td height="40"></td>');
			var canEdit= (classify.isSys == 0 && classify.userId == EasyWin.userInfo.userId);
			if(canEdit){
				$(userTd).addClass("infoDetail");
			}
			var userDiv = $('<div class="ticket-user pull-left other-user-box"></div>');
			var userImg = $('<img src="/downLoad/userImg/'+EasyWin.userInfo.comId+'/'+classify.userId+'" class="user-avatar" />');
			var userName = $('<span class="user-name"></span>');
			$(userName).html(classify.userName);
			$(userDiv).append($(userImg));
			$(userDiv).append($(userName));
			
			$(userTd).append($(userDiv));
			$(userTd).css("padding",'2px 2px');
			$(floderTr).append($(userTd));
			
			var dateTimeTd = $('<td height="40"></td>');
			if(canEdit){
				$(dateTimeTd).addClass("infoDetail");
			}
			$(dateTimeTd).html(classify.recordCreateTime.substring(0,10));
			$(floderTr).append($(dateTimeTd));
			
			if(canEdit){
				var optTd = FolderObj.constrOptTd(classify);
				$(optTd).css("padding",'2px 2px');
				$(floderTr).append($(optTd));
			}
			return $(floderTr);
		},constrOptTd:function(classify){
			var optTd = $('<td colspan="2" style="display: none;text-align:center;" class="opt"></td>');
			
			var modA = $('<a href="javascript:void(0)" class="btn btn-info ws-btnBlue btn5 margin-left-5" style="color:#fff"></a>');
			var canEdit= (classify.isSys == 0 && classify.userId == EasyWin.userInfo.userId);
			if(canEdit){
				var editA = $(modA).clone();
				$(editA).html('修改');
				$(editA).addClass('editDir');
				$(optTd).append($(editA));
			}
			
			if(canEdit){
				var moveA = $(modA).clone();
				$(moveA).html('移动');
				$(moveA).addClass('moveDir');
				$(optTd).append($(moveA));
			}
			
			if(canEdit){
				var delA = $(modA).clone();
				$(delA).html('删除');
				$(delA).addClass('delDir');
				$(optTd).append($(delA));
			}
			
			return optTd;
		},getCheckedDirs:function(ckBoxName){
			 var dirs = new Array();
			 var ckBoxs = $(":checkbox[name='" + ckBoxName + "'][fileType='dir']");
			 if (ckBoxs != null) {
				 $.each(ckBoxs,function(index,ckBox){
					 if ($(this).attr('checked')) {
						 var classify = $(this).parents('tr').data("classify");
						 dirs.push(classify);
					 }
				 })
			 }
		 	 return dirs;
		}
}
FolderObj.Opt = {
		editDirPre:function(ts,calback){//编辑文件夹
			var classify = $(ts).parents('tr').data('classify');
			//首先清空原有数据信息
			var dirInfos = $("body").find(".dirInfo");
			if(dirInfos && dirInfos[0]){
				$.each(dirInfos,function(index,dirInfo){
					var classifyT = $(this).data('classify');
					var floderTr = FolderObj.constrFolder(classifyT);
					if(classify.id!=classifyT.id){
						$(this).replaceWith($(floderTr));
					}
				})
			}
			var html='\n		<td height="40" colspan="4">';
			html+='\n			<div>';
			html+='\n				<div style="width: 75%;float:left;margin-left: 40px" class="dirName">';
			html+='\n					<div style="float:left;width:100%"><input type="text" class="form-control" name="dirName" style="width: 100%;height: 30px" /></div>';
			html+='\n				</div>';
			html+='\n				<div style="width: 10%;float:left;margin-left: 40px">';
			html+='\n					<div style="float:left;">';
			html+='\n					<select class="populate" id="pubState" name="pubState">';
			html+='\n					<option value="1">公开</option>';
			html+='\n					<option value="0">私有</option>';
			html+='\n					</select>';
			html+='\n					</div>';
			html+='\n				</div>';
			html+='\n			</div>';
			html+='\n		</td>';
			var _html = $(html);
			$(_html).find('[name="dirName"]').val(classify.typeName);
			var pubState = classify.pubState;
			$(_html).find('#pubState').find("option[value='"+pubState+"']").attr("selected",true);
			
			var dirName = $(ts).parents('tr').find('td:eq(1)');
			var dirSize = $(ts).parents('tr').find('td:eq(2)');
			var sourceName = $(ts).parents('tr').find('td:eq(3)');
			var dirExt = $(ts).parents('tr').find('td:eq(4)');
			$(dirName).after($(_html))
			
			$(dirName).remove();
			$(dirSize).remove();
			$(sourceName).remove();
			$(dirExt).remove();
			
			
			var optTd = $(ts).parents('tr').find('.opt');
			$(optTd).empty();
			
			var modA = $('<a href="javascript:void(0)" class="btn btn-info ws-btnBlue btn5 margin-left-5" style="color:#fff"></a>');
			var editA = $(modA).clone();
			$(editA).html('保存');
			$(editA).addClass('updateDir');
			$(optTd).append($(editA));
			
			
			var cancleA = $(modA).clone();
			$(cancleA).html('取消');
			$(cancleA).addClass('cancleUpdateDir');
			$(optTd).append($(cancleA));
			
			setStyle();
		},moveDir:function(ts,calback){//移动文件夹
			var classify = $(ts).parents('tr').data('classify');
			FileObj.Opt.moveFile(classify.id,$("#classifyId").val(),function(parentId){
				var url = "/fileCenter/moveDir?sid="+EasyWin.sid+"&t="+Math.random()
				var params = {
					id:classify.id,
					parentId:parentId
				}
				postUrl(url,params,function(data){
					if(data.status=='y'){
						calback()
						showNotification(1,"操作成功！")
					  }
				})
			})
		},delDir:function(ts,callback){//删除文件夹
			var classify = $(ts).parents('tr').data('classify');
			
			var url = "/fileCenter/delDir?sid="+EasyWin.sid+"&t="+Math.random();
			var params = {
					id:classify.id,
					parentId:classify.parentId
				}
			var option = {
					 type:0,
					 icon:3,
					 title:'询问框'
			}
			option.content="删除文件夹以及其下所有文件？";
			option.btn=['不留文件','保留文件','取消'];
			option.yes = function(index){
				window.top.layer.close(index);
				params.isAll ='yes';
					postUrl(url,params,function(data){
						callback(data)
					})
			}
			option.btn2 = function(index){
				window.top.layer.close(index);
				params.isAll = 'no';
				postUrl(url,params,function(data){
					callback(data)
				})
			}
			window.top.layer.open(option);
		},updateDir:function(ts,callback){
			var classify = $(ts).parents('tr').data('classify');
			var dirName = $(ts).parents('tr').find(".dirName").find("input").val().replace(/\s+/g,"");
		 	if(!dirName){
	 		    layer.tips("请先填写文件夹名称",$(ts).parents('tr').find("input"),{tips:1});
	 		    $(ts).parents('tr').find("input").focus();
	 			return;
		 	}else{
		 		//默认能够修改
		 		var falg = true;
		 		if(classify.pubState!=pubState && pubState==0){//修改为私有的时候
		 			var node = zTreeObj.getNodeByParam("id",classify.id);
		 			var nodes = node.children;
		 			if(nodes){
		 				$.each(nodes,function(index,subNode){
		 					//没有其他人员建立的文件夹 且 没有公开文件夹
		 					if(subNode.userId != classify.userId){//有其他人员创建的文件夹
		 						falg = false;
		 						layer.tips("有其他人员创建文件夹，不能修改为私有的！",$(ts).parents('tr').find("#pubState"),{tips:1});
		 						return true;
		 					}else if(subNode.pubState==1){//子文件夹有公开的
		 						falg = false;
		 						layer.tips("有公开的子文件夹，不能修改为私有的！",$(ts).parents('tr').find("#pubState"),{tips:1});
		 						return true;
		 					}
		 					
		 				})
		 			}
		 			
		 		}
		 		if(!falg){//不能修改
		 			return;
		 		}
		 		
		 		var url="/fileCenter/updateDir?sid="+EasyWin.sid+"&t="+Math.random();
		 		
		 		var params = {
		 				"typeName":dirName,
		 				"parentId":$("#classifyId").val(),
		 				"pubState":pubState,
		 				"id":classify.id
		 		}
		 		postUrl(url,params,function(data){
		 			if(data.status=='y'){
		 				classify.typeName = dirName;
		 				classify.pubState = pubState;
		 				callback(classify);
		 			}else{
		 				showNotification(2, data.info);
		 			}
		 		})
		 	} 
		}
}
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

//操作文件
var FileObj = {
		initEvent:function(){
			//移动文件
			$("body").on("click",".moveFile",function(){
				var fileDetail = $(this).parents('tr').data("fileDetail");
				var ts = $(this);
				FileObj.Opt.moveFile(0,$("#classifyId").val(),function(parentId){
					var url = "/fileCenter/moveFile?sid="+EasyWin.sid+"&t="+Math.random()
					var params = {
						id:fileDetial.id,
						classifyId:parentId,
						moduleType:fileDetial.moduleType,
						fileId:fileDetial.fileId,
						userId:fileDetial.userId,
						fileDescribe:fileDetial.fileDescribe
					}
					postUrl(url,params,function(data){
						if(data.status=='y'){
							$(ts).parents('tr').remove();
							var len = $(".fileInfoOne").length;
							if(len==0){
								var html= '\n <tr class="noInfo">';
								html+='\n	<td height="25" colspan="7"><h3>没有文档！</h3></td>';
								html+='\n	</tr>';
								$(".listFiles").html(html);
								  
								setStyle();
							}
							showNotification(1,"操作成功！")
						  }
					})
					  
				})
			});
			//分享文件
			$("body").on("click",".shareFile",function(){
				var fileDetail = $(this).parents('tr').data("fileDetail");
				var ts = $(this);
				FileObj.Opt.shareFile(fileDetail,function(fileShareScope){
					var url = "/fileCenter/shareFile?sid="+EasyWin.sid+"&t="+Math.random()
					var params = {
						"id":fileDetail.id,
						"fileId":fileDetail.fileId,
						"fileShareScopeStr":JSON.stringify(fileShareScope)
					}
					postUrl(url,params,function(data){
						if(data.status=='y'){
							showNotification(1,"操作成功！")
						  }
					})
				});
			});
			//分享文件
			$("body").on("click",".viewShareFile",function(){
				var fileDetail = $(this).parents('tr').data("fileDetail");
				var ts = $(this);
				FileObj.Opt.shareFile(fileDetail,function(fileShareScope){
				});
			});
			//删除文件
			$("body").on("click",".delFile",function(){
				var fileDetail = $(this).parents('tr').data("fileDetail");
				var ts = $(this);
				window.top.layer.confirm('删除文件？', {
					btn: ['确定','取消'],//按钮
					title:'询问框',
					icon:3
				},function(index){
					 window.top.layer.close(index);
					 var url = "/fileCenter/delFile?sid="+sid+"&t="+Math.random();
					 var params = {"id":fileDetail.id,
							 "moduleType":fileDetail.moduleType,
							 "fileId":fileDetail.fileId,
							 "userId":fileDetail.userId}
					 postUrl(url,params,function(data){
						if(data.status=='y'){
							$(ts).parents('tr').remove();
							var len = $(".fileInfoOne").length;
							if(len==0){
								var html= '\n <tr class="noInfo">';
								html+='\n	<td height="25" colspan="7" style="text-align: center;"><h3>没有文档！</h3></td>';
								html+='\n	</tr>';
								$(".listFiles").html(html);
							}
						}
					 })
				});
			});
			//查看非图片附件
			$("body").on("click",".viewVideoFile",function(){
				var fileDetialObj = $(this).parents('tr').data("fileDetail");
				viewVideoPage(fileDetialObj.uuid,EasyWin.sid,fileDetialObj.fileId,fileDetialObj.id);
			});
			
			//查看非图片附件
			$("body").on("click",".viewOfficeFile",function(){
				var fileDetialObj = $(this).parents('tr').data("fileDetail");
				viewOfficePage(fileDetialObj.fileId,fileDetialObj.uuid,fileDetialObj.fileName,fileDetialObj.fileExt,EasyWin.sid,fileDetialObj.moduleType,fileDetialObj.id);
			});
			//查看图片
			$("body").on("click",".viewPicFile",function(){
				var fileDetialObj = $(this).parents('tr').data("fileDetail");
				showPic('/downLoad/down/'+fileDetialObj.uuid+'/'+fileDetialObj.fileName,EasyWin.sid,fileDetialObj.fileId,fileDetialObj.moduleType,fileDetialObj.id)
			});
			//下载
			$("body").on("click",".dowmLoadFile",function(){
				var fileDetialObj = $(this).parents('tr').data("fileDetail");
				var url = "/downLoad/down/"+fileDetialObj.uuid+"/"+fileDetialObj.fileName+"?sid="+EasyWin.sid+"&addLogstate=1";
				window.self.location.href=url;
			});
		},initData:function(pageNumParam){
			if(pageNumParam){
				pageNum = pageNumParam;
				
			}else{
				loadDone=0
				layui.use('layer', function() {
					loadingIndex = layer.load(0, {
						shade: [0.5,'#fff'] //0.1透明度的白色背景
					});
					
					intervalInt = setInterval("closeLoad("+loadingIndex+")",500);
					
				})
				pageNum = 0;
			}
			var params={"sid":EasyWin.sid,
		           	 "pageNum":pageNum,
		           	 "pageSize":pageSize,
		           	 "moduleType":'023'
		 		}
			
			$.each($(".searchCond").find("input"),function(){
				var name =  $(this).attr("name");
				var val =  $(this).val();
				if(val){
					params[name]=val;
				}
			})
			$.each($("#formTempData").find("input"),function(index,obj){
				var name =  $(this).attr("name");
				var val =  $(this).val();
				if(val){
					params[name]=val;
				}
			})
			$.each($("#formTempData").find("select"),function(index,obj){
				
				var list =  $(this).attr("list");
				var listkey =  $(this).attr("listkey");
				var listvalue =  $(this).attr("listvalue");
				
				var options = $(this).find("option");
				if(options && options.length>0){
					$.each(options,function(optIndex,option){
						var nameKey = list+"["+optIndex+"]."+listkey;
						var nameValue = list+"["+optIndex+"]."+listvalue;
						params[nameKey]=$(option).val();
						params[nameValue]=$(option).text();
					})
				}
			})
			
			var url = "/onlineLearn/ajaxListPagedFolderAndFile?sid="+EasyWin.sid;
			postUrl(url,params,function(data){
				loadDone = 1;
				if(pageNum == 0){
					var folders = data.folders;
					FolderObj.constrFolders(folders);
				}
				var pageBean = data.pageBean;
 			 	$("#totalNum").html(pageBean.totalCount);
				if(pageBean.totalCount<=pageSize){
              		 $("#totalNum").parent().parent().css("display","none")
              	}else{
              		$("#totalNum").parent().parent().css("display","block")
              	}
         		//分页，PageCount是总条目数，这是必选参数，其它参数都是可选
                $("#pageDiv").pagination(pageBean.totalCount, {
                    callback: PageCallback,  //PageCallback() 为翻页调用次函数。
                    prev_text: "<<",
                    next_text: ">>",
                    items_per_page:pageSize,
                    num_edge_entries: 0,       //两侧首尾分页条目数
                    num_display_entries: 3,    //连续分页主体部分分页条目数
                    current_page: pageNum,   //当前页索引
                });
                
                $("#listFiles").empty();
              	if(pageBean.totalCount>0){
              		var files = pageBean.recordList;
              		FileObj.constrFile(files);
              		 
              	}
			})
		},constrFile:function(files){
			$("#listFiles").empty();
			if(!files || !files[0]){//没有文件夹
				return;
			}
			$.each(files,function(index,fileDetialObj){
				var fileTr = $('<tr class="fileInfoOne"></tr>');
				$(fileTr).data("fileDetail",fileDetialObj);
				//序号
				var indexTd = $('<td height="40"></td>');
				var indexLable = $('<label></label>')
				var indexCheckbox = $('<input type="checkbox" name="ids" fileType="file" />');
				var indexName = $('<span class="text"></span>');
				
				$(indexLable).append($(indexCheckbox));
				$(indexLable).append($(indexName));
				
				$(indexTd).append($(indexLable));
				
				$(fileTr).append($(indexTd));
				//文件名称
				var fileNameTd = $('<td height="40"></td>');
				var fileNameA = $('<a href="javascript:void(0)" data-fileName="1"></a>');
				$(fileNameA).html(fileDetialObj.fileName);
				$(fileNameTd).append(fileNameA);
				$(fileTr).append($(fileNameTd));
				//文件描述
				var fileFromTd = $('<td height="40"></td>');
				fileFrom = fileDetialObj.fileDescribe;
				$(fileFromTd).html(fileFrom);
				$(fileTr).append($(fileFromTd));
				//文件大小
				var fileSizeTd = $('<td height="40"></td>');
				$(fileSizeTd).html(fileDetialObj.sizem);
				$(fileTr).append($(fileSizeTd));
				//文件后缀
				var fileExtTd = $('<td height="40"></td>');
				$(fileExtTd).html(fileDetialObj.fileExt);
				$(fileTr).append($(fileExtTd));
				
				//上传人员
				var userTd = $('<td height="40" class="infoDetail"></td>');
				var userDiv = $('<div class="ticket-user pull-left other-user-box"></div>');
				var userImg = $('<img src="/downLoad/userImg/'+EasyWin.userInfo.comId+'/'+fileDetialObj.userId+'" class="user-avatar" />');
				var userName = $('<span class="user-name"></span>');
				$(userName).html(fileDetialObj.userName);
				$(userDiv).append($(userImg));
				$(userDiv).append($(userName));
				
				$(userTd).append($(userDiv));
				$(userTd).css("padding",'2px 2px');
				$(fileTr).append($(userTd));
				//上传日期
				var dateTimeTd = $('<td height="40" class="infoDetail"></td>');
				$(dateTimeTd).html(fileDetialObj.recordCreateTime.substring(0,10));
				$(fileTr).append($(dateTimeTd));
				
				var optTd = FileObj.constrOptTd(fileDetialObj);
				$(optTd).css("padding",'2px 2px');
				$(fileTr).append($(optTd));
				$("#listFiles").append($(fileTr));
				
			});
			
			setStyle();
		},constrOptTd:function(fileDetialObj){
			var optTd = $('<td colspan="2" style="display: none;text-align:center;" class="opt"></td>');
			
			var modA = $('<a href="javascript:void(0)" class="btn btn-info ws-btnBlue btn5 margin-left-5" style="color:#fff"></a>');
			var canMove = (fileDetialObj.userId == EasyWin.userInfo.userId)
			if(canMove){
				var moveA = $(modA).clone();
				$(moveA).html('移动');
				$(moveA).addClass('moveFile');
				$(optTd).append($(moveA));
			}
			
			
			var canShare = (fileDetialObj.userId == EasyWin.userInfo.userId);
			if(canShare){
				var shareA = $(modA).clone();
				$(shareA).html('属性');
				$(shareA).addClass('shareFile');
				$(optTd).append($(shareA));
			}else{
				var shareA = $(modA).clone();
				$(shareA).html('属性');
				$(shareA).addClass('viewShareFile');
				$(optTd).append($(shareA));
			}
			
			var canDel = (fileDetialObj.userId == EasyWin.userInfo.userId);
			if(canDel){
				var delA = $(modA).clone();
				$(delA).html('删除');
				$(delA).addClass('delFile');
				$(optTd).append($(delA));
			}
			
			var canViewVideo = (fileDetialObj.fileExt=='mp4' 
									|| fileDetialObj.fileExt=='mp3' 
									|| fileDetialObj.fileExt=='m4a' 
									|| fileDetialObj.fileExt=='mov' 
									|| fileDetialObj.fileExt=='mkv');
			if(canViewVideo){
				var viewOfficeA = $(modA).clone();
				$(viewOfficeA).html('播放');
				$(viewOfficeA).addClass('viewVideoFile');
				$(optTd).append($(viewOfficeA));
			}
			
			var canViewOffice = (fileDetialObj.fileExt=='doc' 
							|| fileDetialObj.fileExt=='docx' 
							|| fileDetialObj.fileExt=='xls' 
							|| fileDetialObj.fileExt=='xlsx' 
							|| fileDetialObj.fileExt=='ppt' 
							|| fileDetialObj.fileExt=='pptx'
							|| fileDetialObj.fileExt=='txt' 
							|| fileDetialObj.fileExt=='pdf');
			if(canViewOffice){
				var viewOfficeA = $(modA).clone();
				$(viewOfficeA).html('预览');
				$(viewOfficeA).addClass('viewOfficeFile');
				$(optTd).append($(viewOfficeA));
			}
				
			var canViewPic = (fileDetialObj.fileExt=='jpg'
						||fileDetialObj.fileExt=='bmp'
						||fileDetialObj.fileExt=='gif'
						||fileDetialObj.fileExt=='jpeg'
						||fileDetialObj.fileExt=='png');
			if(canViewPic){
			var viewPicA = $(modA).clone();
			$(viewPicA).html('预览');
			$(viewPicA).addClass('viewPicFile');
			$(optTd).append($(viewPicA));
			}			
			return optTd;
		},getCheckedFiles:function(ckBoxName,type){
			 var files = new Array();
			 if(1==type){//移动
				 var ckBoxs = $(":checkbox[name='" + ckBoxName + "'][fileType='file']");
				 if (ckBoxs != null) {
					 $.each(ckBoxs,function(index,ckBox){
						 var fileDetail = $(this).parents('tr').data("fileDetail");
						 if ($(this).attr('checked')) {
							 files.push(fileDetail);
						 }
					 })
				 }
			 }else if(2==type){//删除
				 var ckBoxs = $(":checkbox[name='" + ckBoxName + "'][fileType='file']");
				 if (ckBoxs != null) {
					 $.each(ckBoxs,function(index,ckBox){
						 var fileDetail = $(this).parents('tr').data("fileDetail");
						 var ownState = (fileDetail.userId==EasyWin.userInfo.userId);
						 if($(this).attr('checked') && ownState ){
							 var moduleType = fileDetail.moduleType;
							 if(moduleType == '023'){//只有文件系统数据
								 files.push(fileDetail);
							 }else{
								 $(this).attr('checked',false);
							 }
						 }else{
							 $(this).attr('checked',false);
						 }
					 });
				 }
			 }else if(3==type){//分享
				 var ckBoxs = $(":checkbox[name='" + ckBoxName + "'][fileType='file']");
				 if (ckBoxs != null) {
					 $.each(ckBoxs,function(index,ckBox){
						 var fileDetail = $(this).parents('tr').data("fileDetail");
						 var ownState = (fileDetail.userId==EasyWin.userInfo.userId);
						 if($(this).attr('checked') && ownState ){
							 var moduleType = fileDetail.moduleType;
							 if(moduleType == '023'){//只有文件系统数据
								 files.push(fileDetail);
							 }else{
								 $(this).attr('checked',false);
							 }
						 }else{
							 $(this).attr('checked',false);
						 }
					 });
				 }
				$(":checkbox[name='" + ckBoxName + "'][fileType='dir']").attr('checked',false);
			 }
			 return files;
		}
}

FileObj.Opt = {
		moveFile:function(preDirs,oldPId,callback){//移动文件
			 window.top.layer.open({
				 type: 2,
				  title: ['移动到', 'font-size:18px;'],
				  area: ['400px', '350px'],
				  fix: true, //不固定
				  maxmin: false,
				  move: false,
				  content: '/common/dirOnePage?sid=' + EasyWin.sid+'&t='+Math.random()+"&type=023",
				  btn: ['确定', '取消'],
				  yes: function(index, layero){
					  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
					  var parentId = iframeWin.returnDir();
					  if(parentId){
						  window.top.layer.close(index);
						  callback(parentId);
					  }
				  }
				  ,cancel: function(){ 
				    //右上角关闭回调
				  },success: function(layero,index){
					  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
					  iframeWin.initData(preDirs,oldPId);
				  }
			 });
		},shareFile:function(fileDetial,callback){
			
			var options = {
					 type: 2,
					  title: false,
					  closeBtn:0,
					  area: ['400px', '350px'],
					  fix: true, //不固定
					  maxmin: false,
					  move: false,
					  scrollbar:false,
					  content:'/common/listShareUsersPage?sid=' + EasyWin.sid+'&t='+Math.random(),
					  area: ['550px', '400px'],
					 success: function(layero,index){
						  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
						  var id = 0;
						  if(fileDetial){
							  id =fileDetial.id; 
						  }
						  iframeWin.initData(id,'023');
					  }
				 };
			var canShare = (fileDetial && fileDetial.userId != EasyWin.userInfo.userId);
			if(canShare){//分享人员没有修改权限
				options.btn=['关闭'];
				options.yes = function(index, layero){
					window.top.layer.close(index);
				}
			}else{
				options.btn=['确定', '取消'];
				options.yes = function(index, layero){
					var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
					var result = iframeWin.returnUsers();
					if(result){
						window.top.layer.close(index);
						callback(result);
					}
					//右上角关闭回调
				}
				
			}
			
			window.top.layer.open(options);
		}
}

var FileCenter = {
		initEvent:function(){
			
			//清空条件
			$("body").on("click",".clearValue",function(){
				var relateElement = $(this).attr("relateElement");
				$("#formTempData").find("input[name='"+relateElement+"']").val('');
				
				var _i = $('<i class="fa fa-angle-down"></i>')
				
				$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
				$(this).parents("ul").prev().append(_i);
				
				FileObj.initData(0);
			})
			$("#moreCondition_Div").bind("hideMoreDiv",function(){
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				
				if(startDate || endDate){
					var _font = $('<font style="font-weight:bold;"></font>')
					$(_font).html("筛选中")
					var _i = $('<i class="fa fa-angle-down"></i>')
					
					$(this).find("a").html(_font);
					$(this).find("a").append(_i);
				}else{
					var _i = $('<i class="fa fa-angle-down"></i>')
					$(this).find("a").html("更多");
					$(this).find("a").append(_i);
				}
			})
			//清空条件
			$("body").on("click",".clearMoreValue",function(){
				var relateList = $(this).attr("relateList");
				$("#formTempData").find("#"+relateList).html('');
				$("#"+relateList+"Div").find("span").remove();
				$("#"+relateList+"Div").hide();
				
				FileObj.initData(0);
			})
			//单个赋值
			$("body").on("click",".setValue",function(){
				var relateElement = $(this).attr("relateElement");
				var dataValue = $(this).attr("dataValue");
				$("#formTempData").find("input[name='"+relateElement+"']").val(dataValue);
				
				var _font = $('<font style="font-weight:bold;"></font>')
				$(_font).html($(this).html())
				var _i = $('<i class="fa fa-angle-down"></i>')
				
				$(this).parents("ul").prev().html(_font);
				$(this).parents("ul").prev().append(_i);
				
				FileObj.initData(0);
			})
			
			
			//查询时间
			$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
				FileObj.initData(0);
			})
			//查询时间
			$("body").on("click",".moreClearBtn",function(){
				$("#moreCondition_Div").find("input").val('');
				FileObj.initData(0);
			})
			//模糊查询
			$("body").on("blur",".moreSearch",function(){
				FileObj.initData(0);
			})
			
			//批量移动
			$("body").on("click","[data-optType='move']",function(){
				//选中的文件夹
				var dirs = FolderObj.getCheckedDirs('ids');
				//选中的文件
				var files = FileObj.getCheckedFiles('ids',1);
				if((dirs.length+files.length)==0){
					window.top.layer.alert("请选择要移动的文件夹或文件");
					return;
					
				}else{
					FileCenter.batchMove(dirs,files,function(data){
						if(data.status=='y'){
							 var ckBoxs = $(":checkbox[name='ids'][fileType='dir']");
							 if (ckBoxs != null) {
								 $.each(ckBoxs,function(index,ckBox){
									 if ($(this).attr('checked')) {
										$(this).parents('tr').remove();
									 }
								 })
							 }
							 FolderObj.initZtree();
							 //重新加载文件数据
							 FileObj.initData(0);
							 
							 showNotification(1, "操作成功");
						}
					});
				}
			});
			//批量删除
			$("body").on("click","[data-optType='del']",function(){
				//选中的文件夹
				var dirs = FolderObj.getCheckedDirs('ids');
				//选中的文件
				var files = FileObj.getCheckedFiles('ids',2);
				
				if((dirs.length+files.length)==0){
					window.top.layer.alert("请确认文件来源是文档模块,且是自己上传的附件！");
					return;
				}else{
					FileCenter.batchDel(dirs,files,function(data){
						if(data.status=='y'){
							 var ckBoxs = $(":checkbox[name='ids'][fileType='dir']");
							 if (ckBoxs != null) {
								 $.each(ckBoxs,function(index,ckBox){
									 if ($(this).attr('checked')) {
										$(this).parents('tr').remove();
									 }
								 })
							 }
							 //重新加载文件数据
							 FileObj.initData(0);
							 
							 showNotification(1, "操作成功");
						}
					});
				}
			});
			//批量分享
			$("body").on("click","[data-optType='share']",function(){
				//选中的文件
				var files = FileObj.getCheckedFiles('ids',3);
				if(files.length==0){
					window.top.layer.alert("请确认文件来源是文档模块,且是自己上传的附件！");
					return;
				}else{
					FileCenter.batchShare(files,function(data){
						if(data.status=='y'){
							$(":checkbox[name='ids']").attr("checked",false);
							showNotification(1, "操作成功");
						}
					});
					
				}
			});
			
			//上传文件页面
			$("body").on("click","[data-btn='addFiles']",function(){
				var classifyId = $("#classifyId").val();
				if(!classifyId){
					classifyId = -1;
				}
				FileCenter.addVideoFiles(classifyId,function(){
					 //重新加载文件数据
					 FileObj.initData(0);
					 showNotification(1, "操作成功");
				});
			})
			//预添加文件夹
			$("body").on("click","[data-btn='addDirPre']",function(){
				var classifyId = $("#classifyId").val();
				if(!classifyId){
					classifyId =-1;
				}else{
					var node = zTreeObj.getNodeByParam("id",classifyId );
					if(node.pubState==0 && node.userId!=EasyWin.userInfo.userId){//别人私有的
						showNotification(2, "他人私有文件夹，不能创建文件夹！");
						return;
					}
				}
				
				FileCenter.addDirPre();
			})
			//取消添加文件夹
			$("body").on("click","[data-btn='cancelAddDir']",function(){
				$(this).parents('tr').remove();
			})
			//添加文件夹
			$("body").on("click","[data-btn='saveDir']",function(){
				var _this = $(this);
				FileCenter.saveDir($(_this),function(data){
					if(data.status == 'y'){
						 var classify = data.fileClassify;
						 var floderTr = FolderObj.constrFolder(classify);
						 $(_this).parents('tr').replaceWith($(floderTr));
						 setStyle();
						 FolderObj.initZtree();
					}
				});
			})
			//设置滚动条高度
			var height = $(window).height()-140;
			$("#zTreeFolder").parent().css("height",height+"px");
			
		},batchMove:function(dirs,files,callback){
			var preDirs = '0';
			$.each(dirs,function(index,obj){
				 preDirs += ','+obj.id;
			})
			FileObj.Opt.moveFile(preDirs, $("#classifyId").val(), function(parentId){
				var url = "/fileCenter/batchMove?sid="+sid+"&t="+Math.random()+"&type=023";
				var params = {
						dirsStr:JSON.stringify(dirs),
						filesStr:JSON.stringify(files),
						classifyId:parentId
				}
				postUrl(url,params,function(data){
					callback(data)
				})
			})
		},batchShare:function(files,callback){
			var fileDetail;
			if(files && files.length==1){
				fileDetail = files[0]
			}
			FileObj.Opt.shareFile(fileDetail,function(fileShareScope){
				var url = "/fileCenter/batchShare?sid="+EasyWin.sid+"&t="+Math.random()
				var params = {
					"fileDetailStr":JSON.stringify(files),
					"fileShareScopeStr":JSON.stringify(fileShareScope)
				}
				postUrl(url,params,function(data){
					callback(data)
				})
			})
		},batchDel:function(dirs,files,callback){
			var url = "/fileCenter/batchDel?sid="+EasyWin.sid+"&t="+Math.random();
			var params = {
					dirsStr:JSON.stringify(dirs),
					filesStr:JSON.stringify(files),
					classifyId:$("#classifyId").val()
			}
			var option = {
					 type:0,
					 icon:3,
					 title:'询问框'
			}
			
			 if(dirs.length>0){
				 option.content="删除文件夹以及其下所有文件？";
				 option.btn=['不留文件','保留文件','取消'];
				 option.yes = function(index){
					window.top.layer.close(index);
					params.isAll = 'yes'
					postUrl(url,params,function(data){
						callback(data)
					})
				 }
				 option.btn2 = function(index){
						window.top.layer.close(index);
						params.isAll == 'no'
						postUrl(url,params,function(data){
							callback(data)
						})
					 }
			 }else{
				 option.content="删除选中文件？" ;
				 option.btn=['确定','取消'];
				 option.yes = function(index){
						window.top.layer.close(index);
						params.isAll = 'no'
						postUrl(url,params,function(data){
							callback(data)
						})
					 }
			 }
			 window.top.layer.open(option);
						 
		},addVideoFiles:function(classifyId,callback){
			var url = "/onlineLearn/addVideoFilePage?sid="+EasyWin.sid+'&classifyId=-1&t='+Math.random();
			layer.close(tabIndex);
			tabIndex = layer.open({
			  type: 2,
			  title: false,
			  closeBtn: 0,
			  shadeClose: true,
			  shade: 0.3,
			  shift:0,
			  btn:['上传','关闭'],
			  scrollbar:false,
			  fix: true, //固定
			  maxmin: false,
			  move: false,
			  area: ['650px', '450px'],
			  content: [url,'no'], //iframe的url
			  yes:function(index,layero){
				  var iframeWin = window[layero.find('iframe')[0]['name']];
				  iframeWin.addVideoFiles(function(){
					  layer.close(index);
					  callback()
				  });
			  },
			  success: function(layero,index){
				    var iframeWin = window[layero.find('iframe')[0]['name']];
				    iframeWin.setWindow(window.document,window);
			  }
			});
		},addDirPre:function(){
			 var len = $(".dirTr").find("td").length;
			 if(len>0){
				 layer.tips("请先填写文件夹名称",$(".dirTr").find("input"),{tips:1});
				 $(".dirTr").find(".dirName input").focus();
				 return;
			 }
			var html = '\n <tr class="dirTr">';
			html+='\n		<td height="40" colspan="6">';
			html+='\n			<div>';
			html+='\n				<div style="width: 65%;float:left;margin-left: 40px" class="dirName">';
			html+='\n					<div style="float:left;width:100%"><input type="text" class="form-control" name="dirName" style="width: 100%;height: 30px" /></div>';
			html+='\n				</div>';
			html+='\n				<div style="width: 10%;float:left;margin-left: 40px">';
			html+='\n					<div style="float:left;">';
			html+='\n					<select class="populate" id="pubState" name="pubState">';
			html+='\n					<option value="1" selected>公开</option>';
			html+='\n					<option value="0">私有</option>';
			html+='\n					</select>';
			html+='\n					</div>';
			html+='\n				</div>';
			html+='\n				<div style="float:left;" class="opt">';
			html+='\n					<a class="btn btn-info ws-btnBlue" data-btn="saveDir">保存</a>';
			html+='\n					<a class="btn btn-default" data-btn="cancelAddDir">取消</a>';
			html+='\n				</div>';
			html+='\n			</div>';
			html+='\n		</td>';
			html+='\n	</tr>';
			$("#listDirs").prepend(html);
		},saveDir:function(ts,callback){
			var dirName = $(".dirTr").find(".dirName").find("input").val().replace(/\s+/g,"");
		 	if(!dirName){
	 		    layer.tips("请先填写文件夹名称",$(".dirTr").find("input"),{tips:1});
	 			$(".dirName").find("input").focus();
	 			return;
		 	}else{
		 		
		 		var pubState = $(".dirTr").find("#pubState").val();
		 		var parentId = $("#classifyId").val();
		 		if(parentId && parentId>0){
		 			var node = zTreeObj.getNodeByParam("id",parentId);
		 			if(node.pubState==0 && pubState==1){//别人私有的
		 				layer.tips("私有文件夹不能创建公开文件夹！",$(".dirTr").find("#pubState"),{tips:1});
		 	 			return;
		 			}
		 		}
		 		
		 		var url="/fileCenter/addDir?sid="+EasyWin.sid+"&t="+Math.random();
		 		
		 		var params = {
		 				"typeName":dirName,
		 				"type":'023',
		 				"parentId":parentId,
		 				"pubState":pubState
		 		}
		 		postUrl(url,params,function(data){
		 			if(data.status=='y'){
		 				callback(data)
		 			}else{
		 				showNotification(2, data.info);
		 			}
		 		})
		 	}
		}
}

//翻页调用   
function PageCallback(index, jq) {  
 pageNum = index;
 FileObj.initData(index);
}

//样式设置
function setStyle(){
	$('.dirInfo').mouseover(function(){
			$(this).find(".infoDetail").hide();
			$(this).find(".opt").show();
	});
	$('.dirInfo').mouseout(function(){
			$(this).find(".infoDetail").show();
			$(this).find(".opt").hide();
	});
	$('.optspan').mouseover(function(){
		$(this).parent().find("span").css("background-color","#f0f0f0");
		$(this).css("background-color","gray");
	});
	$('.optspan').mouseout(function(){
		$(this).parent().find("span").css("background-color","#f0f0f0");
	});
	
	$('.fileInfoOne').mouseover(function(){
			$(this).find(".infoDetail").hide();
			$(this).find(".opt").show();
	});
	$('.fileInfoOne').mouseout(function(){
			$(this).find(".infoDetail").show();
			$(this).find(".opt").hide();
	});
	$('.optspan').mouseover(function(){
		$(this).parent().find("span").css("background-color","#f0f0f0");
		$(this).css("background-color","gray");
	});
	$('.optspan').mouseout(function(){
		$(this).parent().find("span").css("background-color","#f0f0f0");
	});
	
	//操作删除和复选框
	$('.optTr').mouseover(function(){
		var display = $(this).find(".optTd .optCheckBox").css("display");
		if(display=='none'){
			$(this).find(".optTd .optCheckBox").css("display","block");
			$(this).find(".optTd .optRowNum").css("display","none");
		}
	});
	$('.optTr ').mouseout(function(){
		var optCheckBox = $(this).find(".optTd .optCheckBox");
			var check = $(optCheckBox).find("input").attr('checked');
			if(check){
				$(this).find(".optTd .optCheckBox").css("display","block");
				$(this).find(".optTd .optRowNum").css("display","none");
			}else{
				$(this).find(".optTd .optCheckBox").css("display","none");
				$(this).find(".optTd .optRowNum").css("display","block");
			}
	});
	
	$(":checkbox[name='ids'][disabled!='disabled']").click(function(){
		var checkLen = $(":checkbox[name='ids'][disabled!='disabled']:checked").length;
		var len = $(":checkbox[name='ids'][disabled!='disabled']").length;
		if(checkLen>0){
			//隐藏查询条件
			$(".searchCond").css("display","none");
			//显示批量操作
			$(".batchOpt").css("display","block");
			if(checkLen==len){
				$("#checkAllBox").attr('checked', true);
			}else{
				$("#checkAllBox").attr('checked', false);
			}
		}else{
			//显示查询条件
			$(".searchCond").css("display","block");
			//隐藏批量操作
			$(".batchOpt").css("display","none");
			
			$("#checkAllBox").attr('checked', false);
		}
	});
	
}
