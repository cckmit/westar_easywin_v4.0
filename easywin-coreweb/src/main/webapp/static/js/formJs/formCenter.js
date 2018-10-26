$(function(){
	$("#addFormMod").click(function(){
		//是否为云表单
		var isCloude = $(this).attr("isCloude")
		layui.use('layer', function(){
			var layer = layui.layer;
			layer.open({
					type: 2,
					title: false,
					closeBtn:0,
					area: ['500px', '450px'],
					fix: true, //不固定
					maxmin: false,
					move: false,
					content: ['/form/addFormModPage?sid=' + sid,'no'],
					btn: ['确定', '取消'],
					yes: function(index, layero){
						 var iframeWin = window[layero.find('iframe')[0]['name']];
						 var result = iframeWin.addFormMod(isCloude);
						 if(result){
							 if(result.formType=='0'){
								 window.open("/form/editFormDevPage?sid="+sid+'&formModId='+result.id,"newwindow")
							 }else{
								 window.open("/form/editFormPage?sid="+sid+'&formModId='+result.id,"newwindow")
							 }
							 layer.close(index);
						 }
					}
				}
			)
			
		});
	});
	$("#addFormSort").click(function(){
		var content = '<div class="tickets-container bg-white" id="addFormSortLayer">';
			content+='		<ul class="tickets-list">';
			content+='			<li class="ticket-item no-shadow ps-listline">';
			content+='				<div class="pull-left gray ps-left-text">';
			content+='					&nbsp;分类名称';
			content+='					<span style="color: red">*</span>';
			content+='				</div>';
			content+='				<div class="ticket-user pull-left ps-right-box">';
			content+='					<input id="sortName" class="colorpicker-default form-control type="text" value="" style="width: 250px;float: left"> ';
			content+='				</div>';
			content+='			</li>';
			content+='			<li class="ticket-item no-shadow ps-listline">';
			content+='				<div class="pull-left gray ps-left-text">';
			content+='					&nbsp;排序';
			content+='					<span style="color: red">*</span>';
			content+='				</div>';
			content+='				<div class="ticket-user pull-left ps-right-box">';
			content+='					<input id="orderNo" class="colorpicker-default form-control type="text" value="" style="width: 250px;float: left"> ';
			content+='				</div>';
			content+='			</li>';
			content+='		</ul>';
			content+='	</div>';
		layui.use('layer', function(){
			var layer = layui.layer;
			
			window.top.layer.open({
				title: '快速添加分类',
				type:0,
				area:'450px',
				content:content,
				btn:['添加','关闭'],
				success:function(){
					$.ajax({type:"post",
						dataType:"json",
						url:'/form/ajaxGetFormModSortOrder?sid='+sid,
						success:function(data){
							if(data.status=='y'){
								$("#addFormSortLayer").find("#orderNo").val(data.orderNum)
							}
						}
						
					});
					$("#addFormSortLayer").find("#orderNo").on('keypress',function(event){
						 var keyCode = event.which; 
				            if (keyCode >= 48 && keyCode <=57) //只能输入数字
				                return true; 
				            else 
				                return false; 
					}).on("cut paste",function(){//不能剪切和粘贴
						return false;
					});
				},yes:function(index,layero){
					var sortName = $("#addFormSortLayer").find("#sortName").val();
					sortName = $.trim(sortName)
					if(sortName.length==0){
						layer.tips("请填写分类名称",$("#addFormSortLayer").find("#sortName"),{tips:1});
						return;
					}
					var orderNo = $("#addFormSortLayer").find("#orderNo").val();
					orderNo = $.trim(orderNo)
					if(orderNo.length==0){
						layer.tips("请填写分类排序",$("#addFormSortLayer").find("#orderNo"),{tips:1});
						return;
					}
					
					$.ajax({type:"post",
						dataType:"json",
						url:'/form/addFormModSort?sid='+sid,
						data:{sortName:sortName,orderNo:orderNo},
						success:function(data){
							if(data.status=='y'){
								showNotification(1,"添加成功！")
								window.top.layer.close(index);
							}else{
								showNotification(2,data.info)
							}
						}
						
					});
				}
			})
		});
	});
	binAbleOnclick();
	if(searchTab==21){
		$("#allTodoBody").find(".optUpdataSort").off("click","a").on("click","a",function(){
			var dataId = $(this).attr("dataId");
			var enableState = $(this).attr("enableState");
			var formSortId = $(this).attr("formSortId");
			var e = $(this);
			if(enableState==1 || enableState=='1'){//跳转修改表单
				
				var preModName = $(this).parents("tr").find("td").eq(1).html();
				var content = '<div class="tickets-container bg-white" id="updateFormSort">';
				content+='		<ul class="tickets-list">';
				content+='			<li class="ticket-item no-shadow ps-listline">';
				content+='				<div class="pull-left gray ps-left-text">';
				content+='					&nbsp;表单名称';
				content+='					<span style="color: red">*</span>';
				content+='				</div>';
				content+='				<div class="ticket-user pull-left ps-right-box">';
				content+='					<input id="modName" class="colorpicker-default form-control type="text" value="'+$.trim(preModName)+'" style="width: 250px;float: left"> ';
				content+='				</div>';
				content+='			</li>';
				content+='			<li class="ticket-item no-shadow ps-listline">';
				content+='				<div class="pull-left gray ps-left-text">';
				content+='					&nbsp;分类';
				content+='					<span style="color: red">*</span>';
				content+='				</div>';
				content+='				<div class="ticket-user pull-left ps-right-box">';
				content+='					<select class="populate" style="width: 250px;float: left">';
				content+='					</select>';
				content+='				</div>';
				content+='			</li>';
				content+='		</ul>';
				content+='	</div>';
				
				//定义对话框
				var html = constrDialog();
				var $html = $(html);
				$html.find(".ps-layerTitle").html("修改表单信息");
				$html.find("#contentLayerBody").html(content);
				var contentT = $html.prop("outerHTML");
				
				layui.use('layer', function(){
					var layer = layui.layer;
					var dialogIndex = window.top.layer.open({
						//title: '修改表单信息',
						title:false,
						closeBtn:0,
						type:0,
						area:'450px',
						content:contentT,
						btn:['修改','关闭'],
						success:function(){
							//设置样式
							$("#pContentLayerBody").parent().css("padding","0px");
							$("#dislogCloseBtn").on("click",function(){
								window.top.layer.close(dialogIndex)
							})
							
							$.ajax({type:"post",
								dataType:"json",
								url:'/form/ajaxListFormModSort?sid='+sid,
								success:function(data){
									if(data.status=='y'){
										var listFormSort = data.listFormSort;
										$.each(listFormSort,function(index,vo){
											var $option =$('<option value="'+vo.id+'" >'+vo.sortName+'</option>') 
											if(formSortId==vo.id){
												$option.attr("selected","selected")
											}
											$("#updateFormSort").find("select").append($option);
										})
										var $option =$('<option value="0">其他</option>') 
										if(formSortId==0){
											$option.attr("selected","selected");
										}
										$("#updateFormSort").find("select").append($option);
									}else{
										var $option =$('<option value="0">其他</option>') 
										if(formSortId==0){
											$option.attr("selected","selected");
										}
										$("#updateFormSort").find("select").append($option);
									}
								}
								
							});
						},yes:function(index,layero){
							var select =  $("#updateFormSort").find("select").val();
							var selectName =  $("#updateFormSort").find("select").find("option:selected").html();
							var input = $("#updateFormSort").find("input").val();
							if($.trim(input).length==0){
								layer.tips("请填写表单名称!",$("#updateFormSort").find("input"),{tips:1});
								return;
							}
							$.ajax({type:"post",
								dataType:"json",
								url:'/form/ajaxUpdateFormMod?sid='+sid,
								data:{"id":dataId,"modName":input,"formSortId":select},
								success:function(data){
									if(data.status=='y'){
										$(e).parents("tr").find("td").eq(1).html(input);
										$(e).html(selectName);
										$(e).attr("formSortId",select);
										showNotification(1,"修改成功");
										window.top.layer.close(index);
									}else if(data.status=='f1'){
										layer.tips(data.info,$("#updateFormSort").find("input"),{tips:1});
									}else{
										showNotification(2,data.info);
									}
								}
							});
						}
					}
				)
				});
			}
			
		})
	}
	
	//查询表单列表
	$("#searhForm").find("#modName").off("blur").on("blur",function(){
		$("#searhForm").submit();
	})
	//查询表单列表
	$("#searhForm").find(".ps-searchBtn").off("click").on("click",function(){
		$("#searhForm").submit();
	})
	
	
})
var win = null;
function editFormMod(formModId){
//	layui.use('layer', function(){
//		var layer = layui.layer;
//		//弹出即全屏
//		var indexT = layer.open({
//			type: 2,
//			title:false,
//			closeBtn:1,
//			content: "/form/editFormPage?sid="+sid+'&formModId='+formModId,
//			area: ['320px', '195px'],
//			maxmin: false
//		});
//		layer.full(indexT);
//	});
	
	getSelfJSON("/form/checkFormState",{"sid":sid,"formModId":formModId},function(data){
		if(data.status=='y'){
			var formLayout = data.formLayout;
			if(formLayout){
				if(formLayout.formState && formLayout.formState==1){
					win = window.open("/form/editFormDevPage?sid="+sid+'&formModId='+formModId)
				}else{
					 window.top.layer.open({
						 type:0,
						 content:'替换控件重新编辑？',
						 btn: ['替换','不替换','关闭']//按钮
						 ,icon:3
						 ,title:'询问框'
						 ,yes:function(index){
							 window.top.layer.close(index);
								win = window.open("/form/editFormDevPage?sid="+sid+'&formModId='+formModId)
						 },btn2:function(index){
							 window.top.layer.close(index);
								win = window.open("/form/editFormPage?sid="+sid+'&formModId='+formModId)
						 }
					 })
					
				}
				
			}else{
				win = window.open("/form/editFormDevPage?sid="+sid+'&formModId='+formModId)
			}
		}
	})
	
}
//预览表单模板
function viewFormMod(formModId){
	window.open("/form/viewFormModPage?sid="+sid+'&formModId='+formModId)
}
/**
 * 填写审批
 * @param formModId
 */
function addSpFormWork(formModId){
	var url="/workFlow/addSpFormWorkPage?sid="+sid+'&formModId='+formModId;
	openWinByRight(url)
}
/**
 * 查看审批
 * @param id
 * @param flowState
 */
function viewSpFormWork(id,flowState){
	var url="/workFlow/viewSpFormWorkPage?sid="+sid+'&spFormId='+id+'&flowState='+flowState;
	openWinByRight(url);
}
//启用和禁用事件绑定
function binAbleOnclick(){
	$("#allTodoBody").find(".optTd").find(".enabled").on("click",function(){
		var formModId = $(this).attr("dataId")
		var enabled = $(this).attr("enableState")
		var e = $(this);
		$(this).off("click");
		var editSort = $(this).parents("tr").find("td").eq(2);
		//修改表单启用状态
		$.ajax({type:"post",
			dataType:"json",
			url:'/form/updateFormModEnabled?sid='+sid,
			data:{"id":formModId,"enable":enabled},
			success:function(data){
				if(data.status=='f'){
					showNotification(2,data.info);
				}else{
					e.toggleClass("red").toggleClass("green");
					if(enabled==1 || enabled=="1"){//现启用
						e.attr("enableState","0");
						e.html("启用");
						var html = '<a href="javascript:void(0)" onclick="viewFormMod('+formModId+')">预览</a>\|  ';
						html += '<a href="javascript:void(0)" onclick="cloneFormMod('+formModId+')">克隆</a>\| ';
						html += $(e).context.outerHTML;
						
						e.parent().html(html);
						$(editSort).removeAttr("title");
						$(editSort).find("a").attr("enableState",0);
					}else{//原禁用
						e.attr("enableState","1")
						e.html("禁用");
						var html = '<a href="javascript:void(0)" onclick="viewFormMod('+formModId+')">预览</a>\|  ';
						html += '<a href="javascript:void(0)" onclick="cloneFormMod('+formModId+')">克隆</a>\| ';
						html += $(e).context.outerHTML;
						e.parent().html(html);
						$(editSort).attr("title","修改分类");
						$(editSort).find("a").attr("enableState",1);
					}
					showNotification(1,"操作成功");
					binAbleOnclick();
				}
			},error:function(){
				binAbleOnclick();
			}
			
		});
	})
}

//列出表单分类
function listFormModSort(){
	layer.open({
		  type: 2,
		  title:false,
		  closeBtn:0,
		  //title: ['表单类型维护', 'font-size:18px;'],
		  area: ['550px', '400px'],
		  fix: false, //不固定
		  //btn:['关闭'],
		  maxmin: false,
		  scrollbar:false,
		  content: ["/form/editFormModSortPage?sid="+sid,'no'],
		  cancel: function(){ 
		    //右上角关闭回调
		  },end:function(index){
//			//配置删除背景色
//			  $(ts).parent().removeClass();
//			  //恢复前一个选项的背景色
//			  $(preActive).addClass("active bg-themeprimary");
		  }
		});
}

function formModSortSearch(formSortId,ts){
	var modSortName = $(ts).html();
	if(formSortId==0){
		modSortName = '其他';
	}else if(formSortId==-1){
		modSortName = '';
		
	}
	modSortName = $.trim(modSortName);
	$("#searhForm").find("input[name='formSortId']").val(formSortId>-1?formSortId:"")
	$("#searhForm").find("input[name='modSortName']").val(formSortId>-1?modSortName:"")
	$("#searhForm").submit();
}
//克隆表单模板
function cloneFormMod(formModId){
	var index = layer.load(0,{shade: [0.3, '#f0f0f0']});
	//修改表单启用状态
	$.ajax({type:"post",
		dataType:"json",
		url:'/form/addFormModClone?sid='+sid,
		data:{"formModId":formModId},
		success:function(data){
			layer.close(index)
			if(data.status=='f'){
				showNotification(2,data.info);
			}
			window.location.reload();
		},error:function(){
			layer.close(index)
		}
	});
}
//使用云表单
function downLoadFormMod(formModId,ts){
	var preModName = $(ts).parents("tr").find("td").eq(1).html();
	var content = '<div class="tickets-container bg-white" id="updateFormSort">';
	content+='		<ul class="tickets-list">';
	content+='			<li class="ticket-item no-shadow ps-listline">';
	content+='				<div class="pull-left gray ps-left-text">';
	content+='					&nbsp;表单名称';
	content+='					<span style="color: red">*</span>';
	content+='				</div>';
	content+='				<div class="ticket-user pull-left ps-right-box">';
	content+='					<input id="modName" class="colorpicker-default form-control type="text" value="'+$.trim(preModName)+'" style="width: 250px;float: left"> ';
	content+='				</div>';
	content+='			</li>';
	content+='			<li class="ticket-item no-shadow ps-listline">';
	content+='				<div class="pull-left gray ps-left-text">';
	content+='					&nbsp;分类';
	content+='					<span style="color: red">*</span>';
	content+='				</div>';
	content+='				<div class="ticket-user pull-left ps-right-box">';
	content+='					<select class="populate" style="width: 250px;float: left">';
	content+='					</select>';
	content+='				</div>';
	content+='			</li>';
	content+='		</ul>';
	content+='	</div>';
	layui.use('layer', function(){
		var layer = layui.layer;
		window.top.layer.open({
			title: '使用的表单名称',
			type:0,
			area:'450px',
			content:content,
			btn:['保存','关闭'],
			success:function(){
				$.ajax({type:"post",
					dataType:"json",
					url:'/form/ajaxListFormModSort?sid='+sid,
					success:function(data){
						if(data.status=='y'){
							var listFormSort = data.listFormSort;
							$.each(listFormSort,function(index,vo){
								var $option =$('<option value="'+vo.id+'" >'+vo.sortName+'</option>') 
								$("#updateFormSort").find("select").append($option);
							})
						}
						var $option =$('<option value="0">其他</option>') 
						$option.attr("selected","selected");
						$("#updateFormSort").find("select").append($option);
					}
					
				});
			},yes:function(index,layero){
				var select =  $("#updateFormSort").find("select").val();
				var selectName =  $("#updateFormSort").find("select").find("option:selected").html();
				var input = $("#updateFormSort").find("input").val();
				if($.trim(input).length==0){
					layer.tips("请填写表单名称!",$("#updateFormSort").find("input"),{tips:1});
					return;
				}
				$.ajax({type:"post",
					dataType:"json",
					url:'/form/addFormModFormCloud?sid='+sid,
					data:{"formModId":formModId,"modName":input,"formSortId":select},
					success:function(data){
						if(data.status=='y'){
							window.self.location='/form/formModList?sid='+sid+'&activityMenu=sp_m_2.1&searchTab=21'
						}else if(data.status=='f1'){
							layer.tips(data.info,$("#updateFormSort").find("input"),{tips:1});
						}else{
							showNotification(2,data.info);
						}
					}
				});
			}
		}
	)
	});
	
}