var jfzbListForm = {
		addJfzbType:function(){//添加指标分类
			var url = "/jfzb/jfzbType/listJfzbTypePage?sid="+sid;
			var params={width:"550px",height:"400px",scroll:'no',closeBtn:'no'};
			openWinWithPamsV2(url,params,function(param){
				var index = param.index;
				var layero = param.layero;
				var fun = param.fun;
				if(fun == 'success'){
				  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				}
			});
		},addJfzb:function(){
			var url = "/jfzb/addJfzbPage?sid="+sid;
			openWinTopByRight(url)
		},updateJfzb:function(jfzbId){
			var url = "/jfzb/updateJfzbPage?sid="+sid+"&jfzbId="+jfzbId;
			openWinTopByRight(url)
		},viewJfzb:function(jfzbId){
			var url = "/jfzb/viewJfzbPage?sid="+sid+"&jfzbId="+jfzbId;
			openWinTopByRight(url)
		},loadJfzbType:function(callback){
			//取得标注分类信息
			getSelfJSON("/jfzb/jfzbType/ajaxListJfzbType",{"sid":sid},function(data){
				if(data.status == 'y'){
					var list = data.list;
					//若是有回调
					if(callback && Object.prototype.toString.call(callback) === "[object Function]"){
						callback(list);
						return ;
					}
					  
					if(list && list[0]){
						$.each(list,function(index,jfzbType){
							var _li = $("<li></li>");
							var _A = $('<a href="javascript:void(0)" class="setElementValue" relateElement="orderBy" dataValue="readState"></a>')
							$(_A).attr("relateElement","jfzbTypeId")
							$(_A).attr("relateElementName","jfzbTypeName");
							$(_A).attr("dataValue",jfzbType.id);
							$(_A).html(jfzbType.typeName);
							$(_li).append($(_A))
							$("#jfzbTypeUl").append($(_li));
						});
					}
				}
			});
		}
}

var jfzbOptForm = {
		initForm:function(){
			jfzbOptForm.initEvent();
			jfzbOptForm.loadJfzbType();
		},
		loadJfzbType:function(){
			//取得标注分类信息
			getSelfJSON("/jfzb/jfzbType/ajaxListJfzbType",{"sid":EasyWin.sid},function(data){
				if(data.status == 'y'){
					var list = data.list;
					if(list && list[0]){
						$.each(list,function(index,jfzbType){
							var option = $("<option></option>")
							$(option).attr("value",jfzbType.id);
							$(option).html(jfzbType.typeName);
							if(EasyWin.jfzbTypeId && jfzbType.id == EasyWin.jfzbTypeId){
								$(option).attr("selected","selected");
							}
							$("#contentBody").find("select[name='jfzbTypeId']").append($(option));
						});
					}
				}
			})
		},initEvent:function(){
			
			$("body").on("click",".depSelectBtn",function(){
				var relateSelect = $(this).attr("relateSelect");
				var relateDiv = $(this).attr("relateDiv");
				var ismult = $(this).attr("ismult");
				
				if(ismult && ismult=='1'){
					depMore(relateSelect, null, EasyWin.sid,function(options){
						$("#"+relateDiv).html('');
						$("#"+relateSelect).append('');
						if(options && options[0]){
							$.each(options,function(index,option){
								var depId = option.value;
								var depName = option.text;
								
								var depSpan = $('<span  style="cursor:pointer;" class="label label-default margin-right-5 margin-bottom-5 pull-left" title="双击移除"></span>')
								$(depSpan).html(depName);
								$("#"+relateDiv).append($(depSpan));
								
								var option = $("<option></option>")
								$(option).attr("value",depId);
								$(option).html(depName);
								$("#"+relateSelect).append($(option));
								
								
								$(depSpan).on("dblclick",function(){
									$(this).remove();
									$("#"+relateSelect).find("option[value='"+depId+"']").remove();
								})
							})
						}
					},null)
				}else{
					depOne('null', 'null', null, EasyWin.sid,function(data){
						$("#"+relateDiv).html('');
						$("#"+relateSelect).append('');
						if(data){
							var depId = data.orgId;
							var depName = data.orgName;
							
							var depSpan = $('<span  style="cursor:pointer;" class="label label-default margin-right-5 margin-bottom-5" title="双击移除"></span>')
							$(depSpan).html(depName);
							$("#"+relateDiv).append($(depSpan));
							
							var option = $("<option></option>")
							$(option).attr("value",depId);
							$(option).html(depName);
							$("#"+relateSelect).append($(option));
							
							
							$(depSpan).on("dblclick",function(){
								$(this).remove();
								$("#"+relateSelect).find("option[value='"+depId+"']").remove();
							})
							
						}
					})
				}
				
				
			})
		}
}