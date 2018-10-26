$(function(){
	$("#addFormModDev").click(function(){
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
					content: ['/web/form/addFormModPage?t='+Math.random(),'no'],
					btn: ['确定', '取消'],
					yes: function(index, layero){
						 var iframeWin = window[layero.find('iframe')[0]['name']];
						 var result = iframeWin.addFormMod(isCloude);
						 if(result){
							 if(result.formType=='0'){
								 window.open("/web/form/editFormDevPage?formModId="+result.id,"newwindow")
							 }else{
								 window.open("/web/form/editFormPage?formModId="+result.id,"newwindow")
							 }
							 layer.close(index);
						 }
					}
				}
			)
			
		});
	});
	binAbleOnclick();
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
			
			
			layui.use('layer', function(){
				var layer = layui.layer;
				window.top.layer.open({
					title: '修改表单信息',
					type:0,
					area:'450px',
					content:content,
					btn:['修改','关闭'],
					success:function(){
						
						$.ajax({type:"post",
							dataType:"json",
							url:'/web/form/ajaxListFormModSort',
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
							url:'/web/form/ajaxUpdateFormMod',
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
	getSelfJSON("/web/form/checkFormState",{"formModId":formModId},function(data){
		if(data.status=='y'){
			var formLayout = data.formLayout;
			if(formLayout){
				if(formLayout.formState && formLayout.formState==1){
					win = window.open("/web/form/editFormDevPage?formModId="+formModId)
				}else{
					 window.top.layer.open({
						 type:0,
						 content:'替换控件重新编辑？',
						 btn: ['替换','不替换','关闭']//按钮
						 ,icon:3
						 ,title:'询问框'
						 ,yes:function(index){
							 window.top.layer.close(index);
								win = window.open("/web/form/editFormDevPage?formModId="+formModId)
						 },btn2:function(index){
							 window.top.layer.close(index);
								win = window.open("/web/form/editFormPage?formModId="+formModId)
						 }
					 })
					
				}
				
			}else{
				win = window.open("/web/form/editFormDevPage?formModId="+formModId)
			}
		}
	})
}
//列出表单分类
function listFormModSort(){
	layer.open({
		  type: 2,
		  title: ['表单类型维护', 'font-size:18px;'],
		  area: ['550px', '400px'],
		  fix: false, //不固定
		  btn:['关闭'],
		  maxmin: false,
		  scrollbar:false,
		  content: "/web/form/editFormModSortPage",
		  cancel: function(){ 
		    //右上角关闭回调
		  },end:function(index){
			  window.self.location.reload();
		  }
		});
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
			url:'/web/form/updateFormModEnabled',
			data:{"id":formModId,"enable":enabled},
			success:function(data){
				if(data.status=='f'){
					showNotification(2,data.info);
				}else{
					e.toggleClass("red").toggleClass("green");
					if(enabled==1 || enabled=="1"){//现启用
						e.attr("enableState","0");
						e.html("启用");
						var html = '<a href="javascript:void(0)" onclick="editFormMod('+formModId+')">编辑</a>\|  ';
						html += '<a href="javascript:void(0)" onclick="cloneFormMod('+formModId+')">克隆</a>\| ';
						html += $(e).context.outerHTML;
						
						e.parent().html(html);
						$(editSort).removeAttr("title");
						$(editSort).find("a").attr("enableState",0);
					}else{//原禁用
						e.attr("enableState","1")
						e.html("禁用");
						var html = '<a href="javascript:void(0)" onclick="editFormMod('+formModId+')">编辑</a>\|  ';
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
		url:'/web/form/addFormModClone',
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