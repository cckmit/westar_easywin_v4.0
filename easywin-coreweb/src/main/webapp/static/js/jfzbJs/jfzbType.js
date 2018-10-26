var jfzbTypeForm  = {
		initData:function(){
			jfzbTypeForm.loadData();
			jfzbTypeForm.initEvent();
		},
		loadData:function(){
			var url ="/jfzb/jfzbType/ajaxListJfzbType"
			var params = {"sid":pageParam.sid};
			getSelfJSON(url,params,function(data){
				if(data.status == 'y'){
					var list = data.list;
					if(list && list[0]){
						$("#editable-sample").show();
						$("#noData").hide();
						jfzbTypeForm.constrDataHtml(list);
					}else{
						$("#editable-sample").hide();
						$("#noData").show();
					}
				}
			})
			
		},constrDataHtml:function(list){
			$.each(list,function(index,jfzbType){
				var _tr = jfzbTypeForm.lineMod();
				$(_tr).find("td:eq(0)").html((index+1));
				$(_tr).find("td:eq(1)").find("input").val(jfzbType.typeName);
				$(_tr).find("td:eq(2)").find("input").val(jfzbType.dicOrder);
				$(_tr).data("jfzbType",jfzbType);
				$("#editable-sample").find("tbody").append($(_tr));
			})
		},initEvent:function(){
			//添加按钮信息
			$("body").on("click","#addBtn",function(){
				$("#noData").hide();
				$("#editable-sample").show();
				
				var addTr = $("#tempStage");
				if(!addTr || !addTr.get(0)){
					addTr = jfzbTypeForm.addModHtml();
					$(addTr).attr("id","tempStage");
					
					var lineNum = $("#editable-sample tbody").find("tr").length;
					$(addTr).find("td:eq(0)").html(lineNum + 1 );
					
					$("#editable-sample").find("tbody").append($(addTr));
					
					
					$("#cancleDiv").show();
					$("#addBtn").parent().hide();
					
					var div = document.getElementById('contentBody');
					div.scrollTop = div.scrollHeight;
					
					//从后台取数据，取不上也没有关系
					$.ajax({
						 type : "post",
						  url : "/jfzb/jfzbType/ajaxGetJfzbTypeOrder?sid="+pageParam.sid+"&rnd="+Math.random(),
						  dataType:"json",
						  success : function(data){
							if(data.status=='y'){
								$(addTr).find("td:eq(2)").find("input").val(data.orderNum)
							}
					     }
					});
				}else{
					if(jfzbTypeForm.checkInput($(addTr))){
						var typeName = $(addTr).find("td:eq(1)").find("input").val();
						var dicOrder = $(addTr).find("td:eq(2)").find("input").val();
						jfzbTypeForm.addJfbzType(typeName,dicOrder);
					}
				}
			});
			//保存单条数据
			$("body").on("click",".addOneType",function(){
				var addTr = $(this).parents("tr");
				if(jfzbTypeForm.checkInput($(addTr))){
					var typeName = $(addTr).find("td:eq(1)").find("input").val();
					var dicOrder = $(addTr).find("td:eq(2)").find("input").val();
					jfzbTypeForm.addJfbzType(typeName,dicOrder);
				}
			})
			//取消保存
			$("body").on("click","#cancleBtn",function(){
				$("#cancleDiv").hide();
				$("#addBtn").parent().show();
				$("#tempStage").remove();
			});
			//删除数据
			$("body").on("click",".delOpt",function(){
				var _this = $(this);
				var jfzbType = $(this).parents("tr").data("jfzbType");
				jfzbTypeForm.delJfzbType(jfzbType,function(data){
					if(data.status=='y'){
						showNotification(1,"操作成功");
						$(_this).parents("tr").remove();
						var trs = $("#editable-sample tbody").find("tr");
						if(trs && trs.get(0)){
							$.each(trs,function(index,obj){
								$(obj).find("td:eq(0)").html((index+1))
							})
						}
						
						
					}
				});
			})
			//名称修改
			$("body").on("blur",".nameInput",function(){
				var typeName = $(this).val();
				var jfzbType = $(this).parents("tr").data("jfzbType");
				var preName = jfzbType.typeName;
				if(typeName && typeName!=preName){
					jfzbTypeForm.updateTypeName(typeName,jfzbType);
				}else{
					$(this).val(preName);
				}
			})
			//排序修改
			$("body").on("blur",".orderInput",function(){
				var dicOrder = $(this).val();
				var jfzbType = $(this).parents("tr").data("jfzbType");
				
				var preDicOrder = jfzbType.dicOrder;
				if(dicOrder && dicOrder!=preDicOrder){
					jfzbTypeForm.updateDicOrder(dicOrder,jfzbType);
				}else{
					$(this).val(preDicOrder);
				}
			})
		},updateTypeName:function(typeName,jfzbType){
			var url = "/jfzb/jfzbType/typeNameUpdate";
			var param = {
					"id":jfzbType.id,
					"typeName":typeName,
					"dicOrder":jfzbType.dicOrder,
					"sid":pageParam.sid
			}
			postUrl(url,param,function(data){
				if(data.status == 'y'){
					jfzbType.typeName = typeName;
					showNotification(1,"修改成功");
				}
			})
		},updateDicOrder:function(dicOrder,jfzbType){
			var url = "/jfzb/jfzbType/dicOrderUpdate";
			var param = {
					"id":jfzbType.id,
					"typeName":jfzbType.typeName,
					"dicOrder":dicOrder,
					"sid":pageParam.sid
			}
			postUrl(url,param,function(data){
				if(data.status == 'y'){
					jfzbType.dicOrder = dicOrder;
					showNotification(1,"修改成功");
				}
			})
		},checkInput:function(addTr){
			var typeName = $(addTr).find("td:eq(1)").find("input").val();
			var typeOrder = $(addTr).find("td:eq(2)").find("input").val();
			 if(!typeOrder || isNaN(typeOrder)){
				 $(addTr).find("td:eq(2)").find("input").focus();
				 layer.tips('请填写数字！', $(addTr).find("td:eq(2)").find("input"), {tips: 1});
				 return false;
			}else if(!typeName){
				$(addTr).find("td:eq(1)").find("input").focus();
				layer.tips('请填写数据！', $(addTr).find("td:eq(1)").find("input"), {tips: 1});
				 return false;
			}
			if(typeName && typeOrder ){
				return true
			}
			
		},addJfbzType:function(typeName,dicOrder){
			var url = "/jfzb/jfzbType/ajaxAddJfzbType";
			var param = {
					"typeName":typeName,
					"dicOrder":dicOrder,
					"sid":pageParam.sid
			}
			postUrl(url,param,function(data){
				if(data.status == 'y'){
					var id = data.id;
					param.id = id;
					
					var _tr = jfzbTypeForm.lineMod();
					var lineNum = $("#editable-sample tbody").find("tr").length;
					$(_tr).find("td:eq(0)").html(lineNum);
					$(_tr).find("td:eq(1)").find("input").val(typeName);
					$(_tr).find("td:eq(2)").find("input").val(dicOrder);
					$(_tr).data("jfzbType",param);
					$("#editable-sample").find("tbody").append($(_tr));
					
					var div = document.getElementById('contentBody');
					div.scrollTop = div.scrollHeight;
					
					$("#tempStage").remove();
					
					$("#addBtn").trigger("click");
					showNotification(1,"添加成功");
				}
			});
		},delJfzbType:function(jfzbType,callback){
			window.top.layer.confirm("确定删除积分类型\""+jfzbType.typeName+"\"？", {
			  btn: ['确定','取消'] //按钮
			}, function(index){
				window.top.layer.close(index);
				
				var url = "/jfzb/jfzbType/delJfbzType"
				var param = {
						"sid":pageParam.sid,
						"id":jfzbType.id,
						"typeName":jfzbType.typeName
				}
				
				postUrl(url,param,function(data){
					callback(data)
				});
			})
		},addModHtml:function(){
			var _tr = $("<tr></tr>");
			var _indexTd = $("<td></td>");
			$(_indexTd).css("height","28px");
			$(_tr).append($(_indexTd));
			
			var _nameTd = $("<td></td>");
			var _nameInput = $('<input class="form-control small" style="color: #000" type="text"/>');
			$(_nameTd).append(_nameInput);
			$(_tr).append($(_nameTd));
			
			var _orderTd = $("<td></td>");
			var _orderInput = $('<input class="form-control small" style="color: #000" type="text"/>')
			$(_orderTd).append(_orderInput);
			$(_tr).append($(_orderTd));
			
			var _optTd = $("<td></td>");
			var _optA = $('<a class="fa fa-save fa-lg addOneType" href="javascript:void(0)" title="保存"></a>')
			$(_optTd).append(_optA);
			$(_tr).append($(_optTd));
			
			return $(_tr);
		},lineMod:function(){
			var _tr = $("<tr></tr>");
			
			var _indexTd = $("<td></td>");
			$(_indexTd).css("height","28px");
			$(_tr).append($(_indexTd));
			
			var _nameTd = $("<td></td>");
			var _nameInput = $('<input class="form-control small nameInput" style="color: #000" type="text"/>');
			$(_nameTd).append(_nameInput);
			$(_tr).append($(_nameTd));
			
			var _orderTd = $("<td></td>");
			var _orderInput = $('<input class="form-control small orderInput" style="color: #000" type="text"/>')
			$(_orderTd).append(_orderInput);
			$(_tr).append($(_orderTd));
			
			var _optTd = $("<td></td>");
			var _optA = $('<a class="fa fa-times-circle-o fa-lg delOpt" href="javascript:void(0)" title="删除"></a>')
			$(_optTd).append(_optA);
			$(_tr).append($(_optTd));
			
			return $(_tr);
		}
		
	}