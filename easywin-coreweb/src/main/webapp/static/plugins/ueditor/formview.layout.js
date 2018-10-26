//存放时间控件对应计算控件
var calFields = {};
//存放数字控件对应计算控件
var calNumFields = {};
//存放计算控件对应的数字控件
var calNumBackFields = {};

//存放计算控件对应的数字控件
var calNumWeight = [];

var calElementRelate = [];

var moneyRelate = [];

var tempSubFormId = 0;
EasyWinViewForm = {
		loadFormLayout:function(e){
			var h = {};
            h.id = EasyWin.formModId;
            h.comId = EasyWin.comId;
            $.ajax({
                type: "POST",
                data: h,
                dataType: "json",
                url: sid ? "/form/findFormCompDev?sid="+sid : "/web/form/findFormCompDev",
                success: function(formMod) {
                	var formLayout = formMod.formLayout;
                	
                	$("#modName").html(formMod.modName)
                	
                	if(formLayout){
                		var layoutHtml= formLayout.formLayoutHtml.layoutHtml;
                		var temp_layoutHtml = $("<div></div>").append(layoutHtml);
                		$(temp_layoutHtml).find(".tempClz").remove();
                		var lastPObj = $(temp_layoutHtml).find("p:last");
            			while($(lastPObj).html()=='<br>'){
            				var lastPrePObj = $(lastPObj).prev();
            				$(lastPObj).remove();
            				lastPObj = lastPrePObj
            			}
            			
            			$(temp_layoutHtml).find("table").addClass("table table-bordered")
            			$(temp_layoutHtml).find("td").css("border-bottom-color","#000")
            			$(temp_layoutHtml).find("td").css("border-top-color","#000")
            			$(temp_layoutHtml).find("td").css("border-left-color","#000")
            			$(temp_layoutHtml).find("td").css("border-right-color","#000")
            			
                		var layoutDetails = JSON.parse(formLayout.layoutDetail);
                		layoutDetails ? $.each(layoutDetails,function(index,layoutDetail){
                			EasyWinViewForm.analyseComponent(layoutDetail,$(temp_layoutHtml));
                		}):"";
                		
                		EasyWin.layoutId = formLayout.id;
                		
                		$(temp_layoutHtml).find("[easyWinCompon='DataTable']").parents("td").css("padding","0px")
                		
                		$("#formLayoutContent").html($(temp_layoutHtml))
                		
                		EasyWinViewForm.initEvent();
                		
                		EasyWinViewForm.constrMonitorWeight();
                		
                	}
                	
                },
                error: function() {
                }
            })
		},
		constrMonitorWeight:function(){
			calNumWeight = [];
			calElementRelate = [];
			//计算控件的集合
			var monitorObjs = $("body").find("[easyWinCompon='Monitor'][monitortype='number']");
			var numComponeArray = new Array();
			if(monitorObjs && monitorObjs.get(0)){
				$.each(monitorObjs,function(index,monitorObj){
					var tempId = $(this).attr("tempId");
					var componentIds = calNumBackFields[tempId];
					
					var calNumers = calNumWeight[tempId];
					if(!calNumers){
						EasyWinViewForm.constWeight(tempId,componentIds);
					}
					
				});
				for(var tempId in calNumWeight){
					if(tempId != 'in_array'){
						var monitorFileds = calNumWeight[tempId];
						for(var conSumTempId in monitorFileds){
							if(conSumTempId != 'in_array'){
								numComponeArray.push(conSumTempId);
								var relateArray = calElementRelate[conSumTempId];
								relateArray = relateArray ?relateArray:new Array();
								relateArray.push(tempId);
								calElementRelate[conSumTempId] = relateArray;
							}
						}
					}
				  }
				
			}
			for(var tempMoneyId in moneyRelate){
				if(tempMoneyId != 'in_array'){
					if(numComponeArray.indexOf(tempMoneyId)<0){
						$("body").off("keyup","[easyWinCompon='NumberComponent'][tempId='"+tempMoneyId+"']")
						.on("keyup","[easyWinCompon='NumberComponent'][tempId='"+tempMoneyId+"']",function(){
							$(this).trigger("transNumMoney",$(this))
						});
					}
				}
			  }
		},
		constWeight:function(tempId,componentIds){
			//是否计算过权重
			var calNumers = calNumWeight[tempId];
			if(calNumers){
				return calNumers;
			}
			calNumers = [];
			if(componentIds && componentIds.length>0){
				$.each(componentIds,function(index,monitorFieldObj){
					//被计算控件
					var monitorField = monitorFieldObj.value;
					var element = $("body").find("[tempId='"+monitorField+"']");
					var easyWinCompon = $(element).attr("easyWinCompon");
					
					if(easyWinCompon == 'Monitor'){
						var monitortype = $(element).attr("monitortype");
						if(monitortype == 'date'){
							var key = $(element).attr("tempId");
							var subNum = calNumers[key];
							if(!subNum){
								subNum = 0;
							}
							var value = 1;
							calNumers[key] = accAdd(subNum,value);
							return true;
						}
						//计算关联的控件
						var subComponentIds = calNumBackFields[monitorField];
						//每一个关联控件的权重
						var calSubNumerWeight = EasyWinViewForm.constWeight(monitorField,subComponentIds);
						if(calSubNumerWeight){
							for(var key in calSubNumerWeight){
								if(key == 'in_array' ){
									return true;
								}
								var subNum = calNumers[key];
								if(!subNum){
									subNum = 0;
								}
								var value = calSubNumerWeight[key];
								
								calNumers[key] = accAdd(subNum,value);
							}
							
						}
						
					}else if(easyWinCompon == 'NumberComponent'){
						//权重元素占比
						var subNum = calNumers[monitorField];
						if(!subNum){
							subNum = 0;
						}
						
						calNumers[monitorField] = accAdd(subNum,1);
					}
				})
				calNumWeight[tempId] = calNumers;
			}
			return calNumers;
		},
		initEvent:function(){
			$("body").on("keyup",".justNum",function(){
				var val = $(this).val();
				//得到第一个字符是否为负号
				var t = val.charAt(0); 
				//先把非数字的都替换掉，除了数字和. 
				val = val.replace(/[^\d\.]/g,''); 
				//必须保证第一个为数字而不是. 
				val = val.replace(/^\./g,''); 
				//保证只有出现一个.而没有多个. 
				val = val.replace(/\.{2,}/g,'.'); 
				//保证.只出现一次，而不能出现两次以上 
				val = val.replace('.','$#$').replace(/\./g,'').replace('$#$','.');
				//如果第一位是负号，则允许添加
				if(t == '-'){
					val = '-'+val;
				}
				$(this).val(val);
			})
			$("body").on("keypress",".justNum",function(event){
				var eventObj = event || e;
		        var keyCode = eventObj.keyCode || eventObj.which;
		        if((keyCode >= 48 && keyCode <= 57) || keyCode==46 || keyCode==45){
		        	return true
		        }else{
		        	return false;
		        }
			})
			
			$("body").off("click",".WdatePicker").on("click",".WdatePicker",function(obj){
				var formart = $(this).attr("formart");
				var _this = $(this);
				if($(this).parent().hasClass("dateTime")){
					var preId = $(this).parents("[easywincompon='DateInterval']").find(".dateTimeS").attr("id")
					var nextId = $(this).parents("[easywincompon='DateInterval']").find(".dateTimeE").attr("id");
					var pFieldId = $(this).attr("pFieldId");
					
					var DateObj = {};
					DateObj.el = $(_this);
					DateObj.startId = preId;
					DateObj.endId = nextId;
					
					DateObj.pFieldId = pFieldId;
					if($(this).hasClass("dateTimeS")){
						WdatePicker({dateFmt:formart,maxDate: '#F{$dp.$D(\''+nextId+'\',{m:-0});}',onpicked:function(dp){$(_this).trigger("calDateTime",DateObj);}})
					}else{
						WdatePicker({dateFmt:formart,minDate: '#F{$dp.$D(\''+preId+'\',{m:+0});}',onpicked:function(dp){$(_this).trigger("calDateTime",DateObj);}})
					}
				}else{
					WdatePicker({dateFmt:formart})
				}
				
			})
			
			//人员选择
			$("body").off("click",".userSelect").on("click",".userSelect",function(){
				var isUnique = $(this).attr("isUnique");
				var _this = $(this);
				
				
				var _spanUserMod = $("<span class='text tempUser tempShowData'></span>")
				
				if(isUnique == 'false'){
					userMore('null', null, sid,'no','null',function(options){
						if(options && options.length>0){
							$(_this).parent().find(".tempUser").remove();
							$.each(options,function(optIndex,optObj){
								var userId = optObj.value
								var userName = optObj.text;
								var _spanUser =  $(_spanUserMod).clone().html(userName);
								
								var tempShowData = {};
								tempShowData.optionId=userId
								tempShowData.content=userName;
								$(_spanUser).data("tempShowData",tempShowData);
								
								$(_this).before(_spanUser)
							})
						}
					})
				}else{//默认单选
					userOne('null', 'null', null, sid,function(options){
						if(options && options.length>0){
							$.each(options,function(optIndex,optObj){
								$(_this).parent().find(".tempUser").remove();
								var userId = optObj.value
								var userName = optObj.text;
								
								var _spanUser =  $(_spanUserMod).clone().html(userName);
								
								var tempShowData = {};
								tempShowData.optionId=userId
								tempShowData.content=userName;
								$(_spanUser).data("tempShowData",tempShowData);
								
								$(_this).before(_spanUser)
							})
						}
					})
				}
			})
			//部门选择
			$("body").off("click",".depSelect").on("click",".depSelect",function(){
				var isUnique = $(this).attr("isUnique");
				var _this = $(this);
				
				var _spanDepMod = $("<span class='text tempDep tempShowData'></span>")
				if(isUnique == 'false'){
					depMore('null', null, sid,function(options){
						if(options && options.length>0){
							$(_this).parent().find(".tempDep").remove();
							$.each(options,function(optIndex,optObj){
								var depId = optObj.value
								var depName = optObj.text;
								
								var _spanDep =  $(_spanDepMod).clone().html(depName);
								
								var tempShowData = {};
								tempShowData.optionId = depId
								tempShowData.content = depName;
								$(_spanDep).data("tempShowData",tempShowData);
								
								$(_this).before(_spanDep)
							})
						}
					},null) 
				}else{//默认单选
					depOne('null', 'null', null, sid,function(result){
						if(result){
							$(_this).parent().find(".tempDep").remove();
							var depId = result.orgId
							var depName = result.orgName;
							
							var _spanDep =  $(_spanDepMod).clone().html(depName);
							
							var tempShowData = {};
							tempShowData.optionId = depId
							tempShowData.content = depName;
							$(_spanDep).data("tempShowData",tempShowData);
							
							$(_this).before(_spanDep)
						}
					})
					
				}
			})
			//动态添加行
			$("body").off("click",".optAddDataTable").on("click",".optAddDataTable",function(){
				var subTableBody = $(this).parents(".subTableHead").next();
				
				var subTableTr = $(subTableBody).find(".subTableTr:eq(0)").clone();
				$(subTableTr).find(".tdItem").html('');
				
				var sublayoutDetails = $(subTableBody).data("sublayoutDetail")
				var effectCpmponents = new Array();
				var effectIndex = 0;
				$.each(sublayoutDetails,function(index,sublayoutDetail){
					if(sublayoutDetail.componentKey != 'subItemTitle'){
						effectCpmponents.push(sublayoutDetail);
						var defineTag = $("<easyWin></easyWin>");
						defineTag.attr("tempId",sublayoutDetail.tempId);
						defineTag.attr("componKey",sublayoutDetail.componentKey);
						
						$(subTableTr).find(".tdItem:eq("+effectIndex+")").append(defineTag);
						effectIndex ++ ;
					}
		    	})
		    	$.each(effectCpmponents,function(index,sublayoutDetail){
		    		EasyWinViewForm.analyseComponent(sublayoutDetail,$(subTableTr));
		    	})
				
				$(subTableBody).append(subTableTr);
				
			});
			//动态删除行
			$("body").off("click",".optDelDataTable").on("click",".optDelDataTable",function(){
				var len = $(this).parents(".subTableBody").find(".subTableTr").length;
				 //删除元素中有数字的
	            var numCompones = $(this).parents(".subTableTr").find("[easyWinCompon='NumberComponent'],[easyWinCompon='Monitor']");
				if(len >1){
					$(this).parents(".subTableTr").remove();
				}else{
					layer.tips("需要保留一行数据",$($(this).parents(".subTableBody").find(".subTableTr")),{tips:1})
				}
				//存在数字计算控件
	            if(numCompones && numCompones.get(0)){
	            	$.each(numCompones,function(index,numCompone){
	            		var tempId = $(numCompone).attr("tempId");
						var _this = $(this);
						var calBackFields = calElementRelate[tempId];
						if(!calBackFields){
							return true;
						}
						$.each(calBackFields,function(index,relateTempId){
							var _htmlScope = $("body");
							var calComponent = $("body").find("[easyWinCompon='Monitor'][tempId='"+relateTempId+"']");
							var calSubInDataTable = $(calComponent).parents("[easyWinCompon='DataTable']");
							if(calSubInDataTable && calSubInDataTable.get(0)){
								return true;
							}
							var total = 0;
							var subArray = calNumWeight[relateTempId];
							for(var key in subArray){
								if(key != 'in_array'){
									var weight = subArray[key];
									var subTotal = EasyWinViewForm.calTotalNum(key,_htmlScope);
									total = accAdd(total,accMul(subTotal,weight));
								}
							}
							
							var calComponent = $(_htmlScope).find("[easyWinCompon='Monitor'][tempId='"+relateTempId+"']");
							$(calComponent).val(total);
							
							$(calComponent).trigger("transMonitorMoney",$(calComponent));
						});
	            	})
	            }
			});
			
			//关联模块选择
			$("body").off("click",".relateModSelect").on("click",".relateModSelect",function(){
				var busType = $(this).attr("busType")
				var isUnique = $(this).attr("isUnique");
				var _this = $(this);
				
				if(busType == '005'){//项目选择
					var selectWay = isUnique =='true'?1:2;
					
					var _spanItemMod = $("<span class='text tempItem tempShowData'></span>")
					itemMoreSelect(selectWay,"null",function(items){
						if(items && items.length>0){
							$(_this).parent().find(".tempItem").remove();
							$.each(items,function(optIndex,item){
								var itemId = item.id;
								var itemName = item.itemName;
								var _spanItem =  $(_spanItemMod).clone().html(itemName);
								
								var tempShowData = {};
								tempShowData.optionId = itemId
								tempShowData.content = itemName;
								$(_spanItem).data("tempShowData",tempShowData);
								
								$(_this).before(_spanItem)
							})
						}
					})
				}else if(busType == '012'){//客户选择
					var selectWay = isUnique =='true'?1:2;
					var _spanItemMod = $("<span class='text tempCrm tempShowData'></span>")
					crmMoreSelect(selectWay,"null",function(items){
						if(items && items.length>0){
							$(_this).parent().find(".tempCrm").remove();
							$.each(items,function(optIndex,crm){
								var crmId = crm.id;
								var crmName = crm.crmName;
								var _spanCrm =  $(_spanItemMod).clone().html(crmName);
								
								var tempShowData = {};
								tempShowData.optionId = crmId
								tempShowData.content = crmName;
								$(_spanCrm).data("tempShowData",tempShowData);
								
								$(_this).before(_spanCrm)
							})
						}
					})
				}else if(busType == '04201'){//事件过程管理选择
					var selectWay = isUnique =='true'?1:2;
					var _spanEventMod = $("<span class='text tempEvent tempShowData'></span>");
					eventSelect(selectWay,"null",function(eventPms){
						if(eventPms && eventPms.length>0){
							$(_this).parent().find(".tempEvent").remove();
							$.each(eventPms,function(optIndex,eventPm){
								var eventId = eventPm.id;
								var eventSerialId = eventPm.eventId;
								var _spanEvent =  $(_spanEventMod).clone().html(eventSerialId);
								
								var tempShowData = {};
								tempShowData.optionId = eventId
								tempShowData.content = eventSerialId;
								$(_spanEvent).data("tempShowData",tempShowData);
								
								$(_this).before(_spanEvent);
							})
						}
					})
				}else if(busType == '04202'){//问题过程管理选择
					var selectWay = isUnique =='true'?1:2;
					var _spanIssueMod = $("<span class='text tempIssue tempShowData'></span>");
					issueSelect(selectWay,"null",function(issuePms){
						if(issuePms && issuePms.length>0){
							$(_this).parent().find(".tempIssue").remove();
							$.each(issuePms,function(optIndex,issuePm){
								var issueId = issuePm.id;
								var issueSerialId = issuePm.issueId;
								var _spanIssue =  $(_spanIssueMod).clone().html(issueSerialId);
								
								var tempShowData = {};
								tempShowData.optionId = issueId
								tempShowData.content = issueSerialId;
								$(_spanIssue).data("tempShowData",tempShowData);
								
								$(_this).before(_spanIssue);
							})
						}
					})
				}else if(busType == '04203'){//变更过程管理选择
					var selectWay = isUnique =='true'?1:2;
					var _spanModifyMod = $("<span class='text tempModify tempShowData'></span>");
					modifySelect(selectWay,"null",function(modifyPms){
						if(modifyPms && modifyPms.length>0){
							$(_this).parent().find(".tempModify").remove();
							$.each(modifyPms,function(optIndex,modifyPm){
								var modifyId = modifyPm.id;
								var modifySerialId = modifyPm.modifyId;
								var _spanModify =  $(_spanModifyMod).clone().html(modifySerialId);
								
								var tempShowData = {};
								tempShowData.optionId = modifyId
								tempShowData.content = modifySerialId;
								$(_spanModify).data("tempShowData",tempShowData);
								
								$(_this).before(_spanModify);
							})
						}
					})
				}else if(busType == '04204'){//发布过程管理选择
					var selectWay = isUnique =='true'?1:2;
					var _spanReleaseMod = $("<span class='text tempRelease tempShowData'></span>");
					releaseSelect(selectWay,"null",function(releasePms){
						if(releasePms && releasePms.length>0){
							$(_this).parent().find(".tempRelease").remove();
							$.each(releasePms,function(optIndex,releasePm){
								var releaseId = releasePm.id;
								var releaseSerialId = releasePm.releaseId;
								var _spanRelease =  $(_spanreleaseMod).clone().html(releaseSerialId);
								
								var tempShowData = {};
								tempShowData.optionId = releaseId
								tempShowData.content = releaseSerialId;
								$(_spanRelease).data("tempShowData",tempShowData);
								
								$(_this).before(_spanRelease);
							})
						}
					})
				}
			})
			
			// 保存常用意见
			$("body").off("dblclick", "#spIdeaText").on("dblclick", "#spIdeaText",function(){
				var value = $(this).val();
				var len = charLength(value.replace(/\s+/g,""));
				if(len==0){
					return;
				}
				if(len%2==1){
					len = (len+1)/2;
				}else{
					len = len/2;
				}
				if(len>30){
					value = cutstr(value,30);
				}
				$.ajax({
					type : "post",
					url : "/usagIdea/ajaxAddUsagIdea?sid="+sid+"&t="+Math.random(),
					dataType : "json",
					data:{"idea":value},
					success : function(data) {
						if (data.status == 'y') {
							showNotification(1, "已存入常用意见")
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						showNotification(2, "系统错误，请联系管理员！")
					}
				});
			});
		},
		analyseComponent:function(layoutDetail,layoutHtml){
			var componKey = layoutDetail.componentKey;
	    	var fieldId = layoutDetail.fieldId;
	    	var tempId = layoutDetail.tempId;
	    	
	    	var required = layoutDetail.required;
	    	var title = layoutDetail.title;
	    	
	    	var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
	    	var easywinTag = $(layoutHtml).find("easywin[tempid='"+tempId+"']");
	    	if('Text'==componKey){
	    		var _html;
				_html  = $("<input type='text' class='easywinInput' style='width:90px'/>");
				if(required=='true'){//是必填
					_html.addClass("check_js")
					$(easywinTag).before(_spanRequire);
				}
				_html.attr("easyWinCompon",componKey);
				_html.attr("tempId",tempId);
				
				var conf_size = layoutDetail.size;
				if(conf_size == 'small'){
					_html.css("width","30%");
				}else if(conf_size == 'medium'){
					_html.css("width","60%");
				}else if(conf_size == 'large'){
					_html.css("width","95%");
				}
				
				_html.data("componentData",layoutDetail);
				
				$(easywinTag).before(_html);
	    		
	    	}else if('NumberComponent'==componKey){
	    		var _html;
				_html  = $("<input type='text' class='easywinInput justNum' style='width:90px'/>");
				if(required=='true'){//是必填
					_html.addClass("check_js")
					$(easywinTag).before(_spanRequire);
				}
				_html.attr("easyWinCompon",componKey);
				_html.attr("tempId",tempId);
				
				var conf_size = layoutDetail.size;
				if(conf_size == 'small'){
					_html.css("width","30%");
				}else if(conf_size == 'medium'){
					_html.css("width","60%");
				}else if(conf_size == 'large'){
					_html.css("width","95%");
				}
				
				_html.data("componentData",layoutDetail);
				
				$(easywinTag).before(_html);
	    	}else if('SerialNum'==componKey){
	    		var _html;
	    		_html  = $("<input type='text' class='easywinInput easywinSerialNum' style='width:90px'/>");
	    		if(required=='true'){//是必填
	    			_html.addClass("check_js")
	    			$(easywinTag).before(_spanRequire);
	    		}
	    		_html.attr("easyWinCompon",componKey);
	    		_html.attr("tempId",tempId);
	    		
	    		var conf_size = layoutDetail.size;
	    		if(conf_size == 'small'){
	    			_html.css("width","30%");
	    		}else if(conf_size == 'medium'){
	    			_html.css("width","60%");
	    		}else if(conf_size == 'large'){
	    			_html.css("width","95%");
	    		}
	    		
	    		_html.data("componentData",layoutDetail);
	    		
	    		$(easywinTag).before(_html);
	    	}else if('TextArea'==componKey){
	    		var _html = $("<textarea resize='none' style='height:95%;min-height:50px'></textarea>");
				var len = $(easywinTag).parents("td").length;
				if(len == 1){
					$(easywinTag).parents("td").css( "padding","5px 5px");
					$(easywinTag).parents("td").css( "margin","0px 0px");
				}
				
				var conf_size = layoutDetail.size;
				if(conf_size == 'small'){
					_html.css("width","30%");
				}else if(conf_size == 'medium'){
					_html.css("width","60%");
				}else if(conf_size == 'large'){
					_html.css("width","95%");
				}
				
				if(required=='true'){//是必填
					_html.addClass("check_js")
					$(easywinTag).before(_spanRequire);
				}
				_html.attr("easyWinCompon",componKey);
				_html.attr("tempId",tempId)
				
				_html.data("componentData",layoutDetail);
				
				$(easywinTag).before(_html);
				
	    	}else if('RadioBox' == componKey){
	    		
	    		var _html = $("<span class='myDiv' style='text-align:center'></span>");
				var options = layoutDetail.options;
				
				var optName = "field_"+new Date().getTime();
				if($(easywinTag).parents("[easyWinCompon='DataTable']").length>0){
					var subIndex = $(easywinTag).parents(".subTableTr").index();
					optName = "field_sub"+subIndex+"_"+new Date().getTime();
				}
	        	$.each(options,function(optIndex,optObj){
	        		var optFieldId = optObj.fieldId
	        		var option = $("<lable style='position:relative;'><input style='position:absolute;left:11px !important;top:0 !important' type='radio'/><span class='text' style='margin-left:10px;'>"+optObj.name+"</span></lable>")
	            	$(option).find("input").attr("name",optName); 
	        		_html.append(option);
	        	})
	        	var titleLayout = layoutDetail.titleLayout;
	        	
	        	'field-hoz' == titleLayout?"":$(_html).find("lable").append("<br/>");
	        	
				if(required=='true'){//是必填
					_html.addClass("check_js")
					$(easywinTag).before(_spanRequire);
				}
				_html.attr("easyWinCompon",componKey);
				_html.attr("tempId",tempId)
				
				_html.data("componentData",layoutDetail);
				
	        	$(easywinTag).before(_html);
	        	
	    	}else if('CheckBox'==componKey){
	    		
	    		
	    		var _html = $("<span class='myDiv'></span>");
				var options = layoutDetail.options;
	        	$.each(options,function(optIndex,optObj){
	        		var optName = optObj.name;
	        		var option = $("<lable style='position:relative;'><input style='position:absolute;left:11px !important;top:0 !important;' type='checkbox'/><span class='text' style='margin-left:10px;'>"+optName+"</span></lable>")
	            	$(option).find("input").attr("name","field_"+fieldId); 
	        		_html.append(option);
	        	})
	        	var titleLayout = layoutDetail.titleLayout;
	        	'field-hoz' == titleLayout?"":$(_html).find("lable").append("<br/>");
	        	
				
				if(required=='true'){//是必填
					_html.addClass("check_js")
					$(easywinTag).before(_spanRequire);
				}
				_html.attr("easyWinCompon",componKey);
				_html.attr("tempId",tempId)
				
				_html.data("componentData",layoutDetail);
				
	        	$(easywinTag).before(_html);
	    	}else if('Select' == componKey){
	    		
	    		var _html = $("<span class='myDiv'></span>");
				
				var _selectHtml = $("<select></select>");
				
				var option = $("<option>请选择</option>")
				$(option).attr("fieldId",0); 
				$(option).attr("value",""); 
				$(_selectHtml).append(option);
				
				var options = layoutDetail.options;
				$.each(options,function(optIndex,optObj){
					var optName = optObj.name;
					var option = $("<option>"+optName+"</option>")
					$(_selectHtml).append(option);
				});
				if(required=='true'){//是必填
					_html.addClass("check_js")
					$(easywinTag).before(_spanRequire);
				}
				_html.append(_selectHtml);
				
				_html.attr("easyWinCompon",componKey);
				_html.attr("tempId",tempId)
				
				_html.data("componentData",layoutDetail);
				
				$(easywinTag).before(_html);
	    		
	    	}else if('DateComponent' == componKey ){
	    		var _html;
				var confFormat = layoutDetail.format;
				
				var isDefault = layoutDetail.isDefault;
				var isEdit = layoutDetail.isEdit;//是否可以编辑（默认可以）
				
				var nowDate = EasyWin.nowTimeLong?new Date(Number(EasyWin.nowTimeLong)):new Date();
				var nowDateStr = nowDate.pattern(confFormat);
				
				var canOpt = 1;
				if(isDefault == 'true' && isEdit == 'false'){
					_html = $("<span class='easywinView myDiv'><span>")
					_html.html(nowDateStr);
					_html.addClass("fieldReadOnly defVal");
					
					$(_html).data("oldContent",nowDateStr)
					canOpt = 0;
				}
				
				if(canOpt){//可以编辑
					_html = $("<input type='text' class='WdatePicker easywinInput' readonly='readonly' style='width:50px'>");
					_html.attr("formart",confFormat);
					
					confFormat=='yyyy-MM'?_html.css("width","90px"):confFormat=='yyyy年MM月'?_html.css("width","90px"):confFormat=='yyyy-MM-dd'?_html.css("width","90px"):confFormat=='yyyy年MM月dd日'?_html.css("width","120px"):_html.css("width","150px");
					
					if(required=='true'){//是必填
						_html.addClass("check_js")
						$(easywinTag).before(_spanRequire);
					}
				}
				_html.attr("easyWinCompon",componKey);
				_html.attr("tempId",tempId)
				
				_html.data("componentData",layoutDetail);
				
				$(easywinTag).before(_html);
				
	    	}else if('DateInterval' == componKey){
	    		
	    		var _html = $("<span class='myDiv'></span>");
				
				var confFormat = layoutDetail.start.format;
				
				var start = layoutDetail.start;
				var _startDateInput  = $("<span class='dateTime'><input class='WdatePicker dateTimeS' type='text' style='width:90px' placeholder='yyyy-MM-dd'/></span>");
				$(_startDateInput).find("input").data("componentData",start)
						
				$(_startDateInput).find("input").attr("easyWinCompon","DateComponent");
				$(_startDateInput).find("input").attr("id","fieldIdS_"+new Date().getTime());
				
				var end = layoutDetail.end;
				var _endDateInput  = $("<span class='dateTime'><input class='WdatePicker dateTimeE' type='text' style='width:90px' placeholder='yyyy-MM-dd'/></span>");
				
				$(_endDateInput).find("input").data("componentData",end)
				
				$(_endDateInput).find("input").attr("easyWinCompon","DateComponent");
				$(_endDateInput).find("input").attr("id","fieldIdE_"+new Date().getTime());
				
				_html.append(_startDateInput);
				_html.append("<span style='margin-left:5px;margin-right:5px;'>至</span>");
				_html.append(_endDateInput);
				
				if(confFormat=='yyyy-MM'){
					$(_html).find("input").css("width","90px");
					$(_html).find("input").attr("placeholder","yyyy-MM");
				}else if(confFormat=='yyyy-MM-dd HH:mm'){
					$(_html).find("input").css("width","120px");
					$(_html).find("input").attr("placeholder","yyyy-MM-dd HH:mm");
				}else{
					$(_html).find("input").css("width","90px");
					$(_html).find("input").attr("placeholder","yyyy-MM-dd");
				}
				
				$(_html).find("input").attr("pFieldId",tempId);
				$(_html).find("input").attr("formart",confFormat);
				
				
				if(required=='true'){//是必填
					_html.addClass("check_js")
					$(easywinTag).before(_spanRequire);
				}
				
				_html.attr("easyWinCompon",componKey);
				_html.attr("tempId",tempId)
				
				_html.data("componentData",layoutDetail);
				
				$(easywinTag).before(_html);
			
	    	}else if('Employee' == componKey){
	    		
				var _html = $("<span class='myDiv'></span>");
				
				var isUnique = layoutDetail.isUnique;
				var isDefault = layoutDetail.isDefault;
				var isEdit = layoutDetail.isEdit;//是否可以编辑（默认可以）
				var canOpt = 1;//默认可以操作
				if(isUnique == 'true' && isDefault == 'true' && isEdit=='false'){//人员单选
					_html.addClass("fieldReadOnly");
					var _spanUser = $("<span class='text tempUser tempShowData'></span>")
					_spanUser.html(EasyWin.userInfo.username)
					var tempShowData = {};
					tempShowData.optionId=EasyWin.userInfo.id;
					tempShowData.content=EasyWin.userInfo.username;
					$(_spanUser).data("tempShowData",tempShowData);
					_html.append(_spanUser)
					
					canOpt =0;
				}
				if(canOpt){
					var _OptA  = $("<a href='javascript:void(0)' class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5'>人员</a>");
					_OptA.attr("title","人员选择")
					_OptA.attr("isUnique",isUnique)
					_html.append(_OptA)
					
					if(isUnique == 'true' && isDefault == 'true' ){
						var _spanUser = $("<span class='text tempUser tempShowData'></span>")
						$(_spanUser).html(EasyWin.userInfo.username)
						var tempShowData = {};
						tempShowData.optionId=EasyWin.userInfo.id
						tempShowData.content=EasyWin.userInfo.username;
						$(_spanUser).data("tempShowData",tempShowData);
						
						$(_OptA).before(_spanUser)
					}
						
					
				}
				
				if(required=='true'){//是必填
					_html.addClass("check_js")
					$(easywinTag).before(_spanRequire);
				}
				_html.attr("easyWinCompon",componKey);
				_html.attr("tempId",tempId);
				
				_html.data("componentData",layoutDetail);
				
				$(easywinTag).before(_html);
				
	    		
	    	}else if('Department' == componKey){
	    		
	    		var _html = $("<span class='myDiv'></span>");
				
				var isDefault = layoutDetail.isDefault;
				var isUnique = layoutDetail.isUnique;
				var isEdit = layoutDetail.isEdit;//是否可以编辑（默认可以）
				
				var canOpt = 1;//默认可以操作
				if(isUnique == 'true' && isDefault == 'true' && isEdit=='false'){//部门单选
					_html.addClass("fieldReadOnly");
					var _spanDep = $("<span class='text tempDep tempShowData'></span>")
					_spanDep.html(EasyWin.userInfo.department.name)
					var tempShowData = {};
					tempShowData.optionId=EasyWin.userInfo.department.id;
					tempShowData.content=EasyWin.userInfo.department.name;
					$(_spanDep).data("tempShowData",tempShowData);
					
					_html.append(_spanDep)
					canOpt =0;
				}
				if(canOpt){
					var _OptA  = $("<a href='javascript:void(0)' class='btn btn-primary btn-xs depSelect fa fa-plus margin-left-5'>部门</a>");
					_OptA.attr("title","部门选择")
					_OptA.attr("isUnique",isUnique)
					_html.append(_OptA)
				}
				
				
				if(required=='true'){//是必填
					_html.addClass("check_js")
					$(easywinTag).before(_spanRequire);
				}
				_html.attr("easyWinCompon",componKey);
				_html.attr("tempId",tempId);
				
				_html.data("componentData",layoutDetail);
				
				$(easywinTag).before(_html);
	    		
	    	}else if('RelateFile' == componKey){
                var _html = $("<span class='myDiv'></span>");
                var isUnique = layoutDetail.isUnique;

                var _OptA  = $("<a href='javascript:void(0)' class='btn btn-primary btn-xs relateUpfile fa fa-plus margin-left-5' >附件</a>");
                _OptA.attr("title","附件上传")
                _OptA.attr("isUnique",isUnique)
                _html.append(_OptA)
                if(required=='true'){//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }
                _html.attr("easyWinCompon",componKey);
                _html.attr("fieldId",fieldId);

                _html.data("componentData",layoutDetail);

                $(easywinTag).before(_html);
            }else if('RelateItem' == componKey){
				var _html = $("<span class='myDiv'></span>");
				var isUnique = layoutDetail.isUnique;
				
				var _OptA  = $("<a href='javascript:void(0)' class='btn btn-primary btn-xs relateModSelect fa fa-plus margin-left-5' busType='005'>项目</a>");
				_OptA.attr("title","项目选择")
				_OptA.attr("isUnique",isUnique)
				_html.append(_OptA)
				if(required=='true'){//是必填
					_html.addClass("check_js")
					$(easywinTag).before(_spanRequire);
				}
				_html.attr("easyWinCompon",componKey);
				_html.attr("fieldId",fieldId);
				
				_html.data("componentData",layoutDetail);
				
				$(easywinTag).before(_html);
	    	}else if('RelateCrm' == componKey){
	    		var _html = $("<span class='myDiv'></span>");
	    		var isUnique = layoutDetail.isUnique;
	    		
	    		var _OptA  = $("<a href='javascript:void(0)' class='btn btn-primary btn-xs relateModSelect fa fa-plus margin-left-5' busType='012'>客户</a>");
	    		_OptA.attr("title","客户选择")
	    		_OptA.attr("isUnique",isUnique)
	    		_html.append(_OptA)
	    		if(required=='true'){//是必填
	    			_html.addClass("check_js")
	    			$(easywinTag).before(_spanRequire);
	    		}
	    		_html.attr("easyWinCompon",componKey);
	    		_html.attr("fieldId",fieldId);
	    		
	    		_html.data("componentData",layoutDetail);
	    		
	    		$(easywinTag).before(_html);
	    	}else if('RelateMod' == componKey){
	    		var _html = $("<span class='myDiv'></span>");
	    		var isUnique = layoutDetail.isUnique;
	    		var relateModType = layoutDetail.relateModType;
	    		var _OptA  = $("<a href='javascript:void(0)' class='btn btn-primary btn-xs relateModSelect fa fa-plus margin-left-5'>模块</a>");
	    		$(_OptA).attr("busType",relateModType);
	    		_OptA.attr("title","关联模块")
	    		_OptA.attr("isUnique",isUnique)
	    		_html.append(_OptA)
	    		if(required=='true'){//是必填
	    			_html.addClass("check_js")
	    			$(easywinTag).before(_spanRequire);
	    		}
	    		_html.attr("easyWinCompon",componKey);
	    		_html.attr("fieldId",fieldId);
	    		
	    		_html.data("componentData",layoutDetail);
	    		
	    		$(easywinTag).before(_html);
	    	}else if('subItemTitle' == componKey){
	    		
	    		var _itemTitleDiv = $("<span></span>");
	    		_itemTitleDiv.html(title);
	    		$(easywinTag).before(_itemTitleDiv)
	    	}else if('DataTable' == componKey){
	    		var len = $(easywinTag).parents("td").length;
				if(len == 1){
					$(easywinTag).parents("td").attr( "cellpadding","0");
					$(easywinTag).parents("td").attr( "cellspacing","0");
					$(easywinTag).parents("td").css( "padding","0px 0px");
					$(easywinTag).parents("td").css( "margin","0px 0px");
					$(easywinTag).parents("td").css( "vertical-align","top");
				}
				var size = layoutDetail.size;
	    		
	    		var _subTableDiv = $("<div class='myDiv' style='width:100%;min-height:150px;margin-bottom:5px'></div>")
	    		_subTableDiv.attr("tempId",tempId)
	    		$(_subTableDiv).attr("easyWinCompon",componKey);
	    		$(_subTableDiv).attr("conf_name",title);
	    		
	    		
	    		var _tableHeadDiv = $("<div class='sub subTableHead' style='display:table;width:100%'></div>");
	        	var _headTr = $("<div style='display:table-row;'></div>");
	        	var _headTdMod = $("<div class='tdItem'></div>");
	        	
	        	var _tableBodyDiv = $("<div class='sub subTableBody' style='display:table;width:100%;'></div>");
	        	var _bodyTr = $("<div style='display:table-row;' class='subTableTr'></div>");
	        	var _bodyTdMod = $("<div class='tdItem'></div>");
	        	
	        	
	        	var sublayoutDetails = layoutDetail.layoutDetail;
	        	
	        	$.each(sublayoutDetails,function(index,sublayoutDetail){
	        		
	        		var defineTag = $("<easyWin></easyWin>");
	        		defineTag.attr("tempId",sublayoutDetail.tempId);
	        		defineTag.attr("componKey",sublayoutDetail.componentKey);
	        		if(sublayoutDetail.componentKey == 'subItemTitle'){
	        			var _headTd = $(_headTdMod).clone();
	        			_headTd.html(defineTag)
	        			$(_headTr).append(_headTd)
	        		}else{
	        			var _bodyTd = $(_bodyTdMod).clone();
	        			_bodyTd.html(defineTag)
	        			$(_bodyTr).append(_bodyTd);
	        			
	        		}
	        		
	        	})
	        	$(_tableHeadDiv).append(_headTr);
	        	$(_tableBodyDiv).append(_bodyTr);
	        	$(_tableBodyDiv).data("sublayoutDetail",sublayoutDetails);
	        	
	        	
	        	$(_subTableDiv).append(_tableHeadDiv);
	        	$(_subTableDiv).append(_tableBodyDiv);
	        	
	        	var width = (100-100%size)/size;
	        	$(_subTableDiv).find(".tdItem").css("width",width+"%");
	        	$(_subTableDiv).find(".tdItem").css("word-break","break-all");
	        	
	        	var _headTdOpt = $("<div style='display:table-cell;height:30px;border-left: 1px solid #000;border-bottom: 1px solid #000;text-align:center;vertical-align: middle;'></div>");
        		_headTdOpt.html("<a href='javascript:void(0)' class='fa fa-plus-circle green optAddDataTable fa-lg' style='width:20px;font-size:18px;text-align:center;vertical-align: middle;' title='添加'></a>");
        		$(_subTableDiv).find(".subTableHead").find(".tdItem:last").after(_headTdOpt);
        		
        		$.each($(_subTableDiv).find(".subTableBody").find(".subTableTr"),function(subItemIndex,subItemObj){
        			var _bodyTdOpt = $("<div style='display:table-cell;height:30px;border-left: 1px solid #000;border-bottom: 1px solid #000;text-align:center;vertical-align: middle;'></div>");
        			_bodyTdOpt.html("<a href='javascript:void(0)' class='fa fa-times-circle red optDelDataTable fa-lg' style='width:20px;font-size:18px;text-align:center;vertical-align: middle;'  title='删除'></a>");
        			$(subItemObj).find(".tdItem:last").after(_bodyTdOpt);
        			
        		})
	        		
	        	_subTableDiv.data("componentData",layoutDetail);
	        	_subTableDiv.attr("subFormId",tempSubFormId++);
	        	
	        	$(easywinTag).before(_subTableDiv)
	        	$(easywinTag).remove();
	        	
	        	$.each(sublayoutDetails,function(index,sublayoutDetail){
	        		EasyWinViewForm.analyseComponent(sublayoutDetail,$(layoutHtml));
	        	})
	        	
	    		
	        	
	    	}else if('Monitor' == componKey){
	    		var _html  = $("<input type='text' class='easywinInput' style='width:90px'/>");
	    		
	    		$(_html).attr("readonly",true)
	    		
	    		$(_html).data("calTimeType",layoutDetail.calTimeType);
	    		
	    		
	    		var conf_size = layoutDetail.size;
	    		if(conf_size == 'small'){
	    			_html.css("width","30%");
	    		}else if(conf_size == 'medium'){
	    			_html.css("width","60%");
	    		}else if(conf_size == 'large'){
	    			_html.css("width","95%");
	    		}
	    		_html.attr("monitorType",layoutDetail.monitorType);
	    		
				var monitorFields = layoutDetail.monitorFields;
				if(layoutDetail.monitorType == 'date'){
					if(layoutDetail.monitorTempFields){
						
						//被计算控件
						var calFieldId = layoutDetail.monitorTempFields;
						//取得关联控件
						var calBackFields = calFields[calFieldId];
						//被关联控件暂无其他被计算控件
						calBackFields = calBackFields?calBackFields:new Array();
						//存放被计算控件
						calBackFields.push(tempId);
						
						//被计算控件关联的所有控件
						calFields[calFieldId]= calBackFields;
						
						$("body").bind("calDateTime",$("[easyWinCompon='DateInterval'][tempId='"+tempId+"']").find(".WdatePicker"),function(event,DateObj){
							var el = DateObj.el;
							var startDate = $("#"+DateObj.startId).val();
							var endDate = $("#"+DateObj.endId).val();
							if(startDate && endDate){
								var hour = EasyWinViewForm.calDateTimeNum(startDate,endDate,layoutDetail.calTimeType);
								var pFieldId = DateObj.pFieldId;
								var calBackFields = calFields[pFieldId];
								if(calBackFields && calBackFields.length>0){
									var _htmlScope = $("body");
									
									if($(el).parents("[easyWinCompon='DataTable']").length>0){
										_htmlScope = $(el).parents(".subTableTr");
									}
									$.each(calBackFields,function(calFileIndex,calFileId){
										var calComponent = $(_htmlScope).find("[easyWinCompon='Monitor'][tempId='"+calFileId+"']");
										$(calComponent).val(hour);
										
										$(calComponent).trigger("calDateSum",$(calComponent));
									})
									
								}
								
							}
						})
					}
				}else if(layoutDetail.monitorType == 'number'){
					var componentIds = layoutDetail.componentIds;
					
					var conf_monitorTempDateFields = layoutDetail.monitorTempFields;
					if(conf_monitorTempDateFields){
						conf_monitorTempDateFields = conf_monitorTempDateFields.replace(/\[|]/g,'')
						var monitorNumFieldArray = conf_monitorTempDateFields.split(",");
						if(monitorNumFieldArray && monitorNumFieldArray[0]){
							componentIds = new Array();
							$.each(monitorNumFieldArray,function(index,tempObjId){
								var MonitorField = {};
		    					MonitorField.type = "number";
		    					tempObjId = $.trim(tempObjId);
		    					MonitorField.value = tempObjId;
		    					componentIds.push(MonitorField);
							})
						}
					}
					if(componentIds){
						calNumBackFields[tempId]=componentIds;
						$.each(componentIds,function(index,monitorFieldObj){
							//被计算控件
							var monitorField = monitorFieldObj.value;
							//被计算控件的关联控件
							var calBackFields = calNumFields[monitorField];
							//关联控件若是不存在，则创建一个数组
							calBackFields = calBackFields?calBackFields:new Array();
							//添加一份关联数据
							calBackFields.push(tempId);
							
							calNumFields[monitorField]= calBackFields;
							
							$("body").off("keyup","[easyWinCompon='NumberComponent'][tempId='"+monitorField+"']")
								 	 .on("keyup","[easyWinCompon='NumberComponent'][tempId='"+monitorField+"']",function(){
								var tempId = $(this).attr("tempId");
								var _this = $(this);
								var subInDataTable = $(this).parents("[easyWinCompon='DataTable']");
								var calBackFields = calElementRelate[tempId];
								if(!calBackFields){
									return true;
								}
								$.each(calBackFields,function(index,relateTempId){
									//默认是全局的
									var _htmlScope = $("body");
									var calComponent = $("body").find("[easyWinCompon='Monitor'][tempId='"+relateTempId+"']");
									var calSubInDataTable = $(calComponent).parents("[easyWinCompon='DataTable']");
									
									//计算区域为子表单，则区域为子表单
									if(subInDataTable && subInDataTable.get(0)
											&& calSubInDataTable && calSubInDataTable.get(0)){
										_htmlScope = $(_this).parents(".subTableTr");
									}
									var total = 0;
									var subArray = calNumWeight[relateTempId];
									for(var key in subArray){
										if(key != 'in_array'){
											var weight = subArray[key];
											var subTotal = EasyWinViewForm.calTotalNum(key,_htmlScope);
											total = accAdd(total,accMul(subTotal,weight));
										}
									}
									var calComponent = $(_htmlScope).find("[easyWinCompon='Monitor'][tempId='"+relateTempId+"']");
									$(calComponent).val(total);
									$(calComponent).trigger("transMonitorMoney",$(calComponent));
								});
								
								$(this).trigger("transNumMoney",$(this))
			    			});
							
							$("body").bind("calDateSum",$("[easyWinCompon='Monitor'][monitorType='date'][tempId='"+tempId+"']"),function(event,DateObj){
								var tempId = $(DateObj).attr("tempId");
								var _this = $(DateObj);
								var subInDataTable = $(DateObj).parents("[easyWinCompon='DataTable']");
								var calBackFields = calElementRelate[tempId];
								if(calBackFields){
									$.each(calBackFields,function(index,relateTempId){
										//默认是全局的
										var _htmlScope = $("body");
										var calComponent = $("body").find("[easyWinCompon='Monitor'][tempId='"+relateTempId+"']");
										var calSubInDataTable = $(calComponent).parents("[easyWinCompon='DataTable']");
										
										//计算区域为子表单，则区域为子表单
										if(subInDataTable && subInDataTable.get(0)
												&& calSubInDataTable && calSubInDataTable.get(0)){
											_htmlScope = $(_this).parents(".subTableTr");
										}
										var total = 0;
										var subArray = calNumWeight[relateTempId];
										for(var key in subArray){
											if(key != 'in_array'){
												var weight = subArray[key];
												var subTotal = EasyWinViewForm.calTotalNum(key,_htmlScope);
												total = accAdd(total,accMul(subTotal,weight));
											}
										}
										var calComponent = $(_htmlScope).find("[easyWinCompon='Monitor'][tempId='"+relateTempId+"']");
										$(calComponent).val(total);
										
										$(calComponent).trigger("transMonitorMoney",$(calComponent));
									});
								}
							})
							
							
						});
					}
				}
				if(required=='true'){//是必填
					_html.addClass("check_js")
					$(easywinTag).before(_spanRequire);
				}
				_html.attr("easyWinCompon",componKey);
				_html.attr("tempId",tempId);
				
				_html.data("componentData",layoutDetail);
				
				$(easywinTag).before(_html);
	    	}else if('MoneyComponent' == componKey){
	    		var _html  = $("<span class='easywinInput' style='width:100%'/></span>");
	    		var moneyTempColumn = layoutDetail.moneyTempColumn;
	    		if(moneyTempColumn){
	    			moneyRelate[moneyTempColumn] = tempId;
	    			$("body").bind("transNumMoney","[easyWinCompon='NumberComponent'][tempId='"+moneyTempColumn+"']",function(event,DateObj){
	    				var tempId = $(DateObj).attr("tempId");
	    				var relateTempId = moneyRelate[tempId];
	    				if(relateTempId){
	    					var subInDataTable = $(DateObj).parents("[easyWinCompon='DataTable']");
	    					//默认是全局的
	    					var _htmlScope = $("body");
	    					var calComponent = $("body").find("[easyWinCompon='Monitor'][tempId='"+relateTempId+"']");
	    					var calSubInDataTable = $(calComponent).parents("[easyWinCompon='DataTable']");
	    					
	    					//计算区域为子表单，则区域为子表单
	    					if(subInDataTable && subInDataTable.get(0)
	    							&& calSubInDataTable && calSubInDataTable.get(0)){
	    						_htmlScope = $(_this).parents(".subTableTr");
	    					}
	    					
	    					var money = $(DateObj).val();
	    					
	    					var calComponent = $(_htmlScope).find("[easyWinCompon='MoneyComponent'][tempId='"+relateTempId+"']");
	    					if(money && !isNaN(money)){
	    						$(calComponent).html(changeNumMoneyToChinese(money));
	    					}else{
	    						$(calComponent).html('');
	    					}
	    				}
	    			})
	    			$("body").bind("transMonitorMoney","[easyWinCompon='Monitor'][tempId='"+moneyTempColumn+"']",function(event,DateObj){
	    				var tempId = $(DateObj).attr("tempId");
	    				var relateTempId = moneyRelate[tempId];
	    				if(relateTempId){
	    					var subInDataTable = $(DateObj).parents("[easyWinCompon='DataTable']");
	    					//默认是全局的
	    					var _htmlScope = $("body");
	    					var calComponent = $("body").find("[easyWinCompon='Monitor'][tempId='"+relateTempId+"']");
	    					var calSubInDataTable = $(calComponent).parents("[easyWinCompon='DataTable']");
	    					
	    					//计算区域为子表单，则区域为子表单
	    					if(subInDataTable && subInDataTable.get(0)
	    							&& calSubInDataTable && calSubInDataTable.get(0)){
	    						_htmlScope = $(_this).parents(".subTableTr");
	    					}
	    					
	    					var money = $(DateObj).val();
	    					var calComponent = $(_htmlScope).find("[easyWinCompon='MoneyComponent'][tempId='"+relateTempId+"']");
	    					if(money && !isNaN(money)){
	    						$(calComponent).html(changeNumMoneyToChinese(money));
	    					}else{
	    						$(calComponent).html('');
	    					}
	    				}
	    			})
	    		}
	    		
	    		_html.attr("easyWinCompon",componKey);
	    		_html.attr("tempId",tempId);
	    		
	    		_html.data("componentData",layoutDetail);
	    		
	    		$(easywinTag).before(_html);
	    	}
	    	
	    	var subLayout = layoutDetail.layoutDetail;
	    	if(subLayout && subLayout.length>0){
	    		if('DataTable' != componKey && 'DateInterval' != componKey){
	    			$.each(subLayout,function(index,obj){
	    				EasyWinViewForm.analyseComponent(obj,$(layoutHtml));
	    			})
	    		}
	    	}
	    	$(easywinTag).remove();
		
		},calTotalNum:function(tempId,_htmlScope){
			var total = 0;
			var calObjs = $(_htmlScope).find("[easyWinCompon='NumberComponent'][tempId='"+tempId+"']");
			if(calObjs && calObjs.get(0)){
				$.each(calObjs,function(subIndex,subObj){
					var val = $(this).val();
					if(val && !isNaN(val)){
						total = accAdd(total,val);
					}
				});
			}else {
				calObjs = $(_htmlScope).find("[easyWinCompon='Monitor'][monitortype='date'][tempId='"+tempId+"']");
				if(calObjs && calObjs.get(0)){
					$.each(calObjs,function(subIndex,subObj){
						var val = $(this).val();
						if(val && !isNaN(val)){
							total = accAdd(total,val);
						}
					});
				}
			}
			return total;
			
		},calDateTimeNum:function(startDate,endDate,calTimeType){
			var a = {};
			a.dateS = startDate;
			a.dateE = endDate;
			a.calTimeType = calTimeType;
			var hour = 0.0;
        	$.ajax({
                type: "post",
                url: sid?"/festMod/calDateTime?sid="+sid+"&t="+Math.random():"/web/festMod/calDateTime?t="+Math.random(),
                dataType: "json",
                async: !1,
                data: a,
                success: function(d) {
                   if(d.status=='y'){
                	   hour = d.hour;
                   }
                }
            })
            return hour;	
		}
};



Date.prototype.pattern=function(fmt) {         
    var o = {         
    "M+" : this.getMonth()+1, //月份         
    "d+" : this.getDate(), //日         
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时         
    "H+" : this.getHours(), //小时         
    "m+" : this.getMinutes(), //分         
    "s+" : this.getSeconds(), //秒         
    "q+" : Math.floor((this.getMonth()+3)/3), //季度         
    "S" : this.getMilliseconds() //毫秒         
    };         
    var week = {         
    "0" : "/u65e5",         
    "1" : "/u4e00",         
    "2" : "/u4e8c",         
    "3" : "/u4e09",         
    "4" : "/u56db",         
    "5" : "/u4e94",         
    "6" : "/u516d"        
    };         
    if(/(y+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));         
    }         
    if(/(E+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);         
    }         
    for(var k in o){         
        if(new RegExp("("+ k +")").test(fmt)){         
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));         
        }         
    }         
    return fmt;         
}  
