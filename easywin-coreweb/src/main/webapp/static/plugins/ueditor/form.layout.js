//需要计算的时间字段
var calFields = {};
//需要计算的数字字段
var calNumFields = {};
//缓存计算控件对应的需要计算的组件
var calNumBackFields = {};

//计算控件对应的计算元素集合
var calNumWeight = [];
//被计算的元素变动需要触发的计算控件集合
var calElementRelate = [];

//被计算的元素变动需要触发的计算控件集合
var moneyRelate = [];

EasyWinForm = {
    renderFormDataByLayout: function (e) {
        var d = e.callback;
        //开始布局以及渲染页面
        getSelfJSON("/workFlow/findFormDataDev", {
                sid: e.sid,
                instanceId: e.instanceId,
                layoutId: e.layoutId
            },
            function (data) {
                var formLayout = data.formLayout;
                var formData = data.formData;
                EasyWinForm.constrHtml(formLayout);
                EasyWinForm.initValue(formData);
                EasyWinForm.initEvent();
                EasyWinForm.initUpload();
                //初始化数字计算控件的权重
                EasyWinForm.constrMonitorWeight();
                d && d(data);
                var b = new Date().getTime();
            })
    },
    initUpload: function () {
        var RelateFileCompones = $("body").find("[easyWinCompon=RelateFile]");
        if (RelateFileCompones && RelateFileCompones.get(0)) {
            $.each(RelateFileCompones, function (RelateFileIndex, RelateFileDiv) {
                var loadState = $(RelateFileDiv).attr("loadState");
                var upfileConf = $(RelateFileDiv).data("upfileConf");
                if (upfileConf && upfileConf.pickerDivId && !loadState) {
                    var selectId = upfileConf.selectId;
                    var showDivId = upfileConf.showDivId;
                    var pickerDivId = upfileConf.pickerDivId;
                    loadWebUpfiles(selectId, sid, '', pickerDivId, showDivId, null);

                    if (!loadState) {
                        $(RelateFileDiv).attr("loadState", 1)
                    }
                }
            })
        }
    },
    constrMonitorWeight:function(){//初始化数字计算控件的权重
    	//计算控件对应的计算权重元素集合
		calNumWeight = [];
		//权重的元素变动需要触发的计算控件集合
		calElementRelate = [];
		
		var numComponeArray = new Array();
		
		//计算控件的集合
		var monitorObjs = $("body").find("[easyWinCompon='Monitor'][monitortype='number']");
		if(monitorObjs && monitorObjs.get(0)){
			$.each(monitorObjs,function(index,monitorObj){
				//计算控件对应的唯一标识
				var fieldId = $(this).attr("fieldId");
				//计算控件对应的原始元素集合
				var componentIds = calNumBackFields[fieldId];
				//该控件的对应的原始元素是否已拆分成权重元素
				var calNumers = calNumWeight[fieldId];
				if(!calNumers){//没有分解成权重元素，需要处理
					EasyWinForm.constWeight(fieldId,componentIds);
				}
			});
			//遍历计算控件对应的计算权重元素集合，生成权重元素变动要触发的计算控件
			for(var fieldId in calNumWeight){
				if(fieldId != 'in_array'){
					var monitorFileds = calNumWeight[fieldId];
					//遍历每个计算控件，找出权重元素
					for(var conSumFieldId in monitorFileds){
						if(conSumFieldId != 'in_array'){
							numComponeArray.push(conSumFieldId);
							//权重元素需要触发的计算控件集合
							var relateArray = calElementRelate[conSumFieldId];
							relateArray = relateArray ?relateArray:new Array();
							//权重元素需要触发的计算控件集合添加一条数据
							relateArray.push(fieldId);
							calElementRelate[conSumFieldId] = relateArray;
						}
					}
				}
			  }
		}
		
		for(var tempMoneyId in moneyRelate){
			if(tempMoneyId != 'in_array'){
				if(numComponeArray.indexOf(tempMoneyId)<0){
					$("body").off("keyup","[easyWinCompon='NumberComponent'][fieldId='"+tempMoneyId+"']")
					.on("keyup","[easyWinCompon='NumberComponent'][fieldId='"+tempMoneyId+"']",function(){
						$(this).trigger("transNumMoney",$(this))
					});
				}
			}
		}
	},
	/**
	 * 构建计算控件的权重
	 * @param fieldId 计算控件的组件
	 * @param componentIds 计算控件对应的原始被计算的元素
	 * @returns {Array}
	 */
	constWeight:function(fieldId,componentIds){
		//是否计算过权重
		var calNumers = calNumWeight[fieldId];
		if(calNumers){//计算过，则返回权重信息
			return calNumers;
		}
		//转换后的权重元素集合
		calNumers = [];
		//存在计算控件对应的原始被计算的元素
		if(componentIds && componentIds.length>0){
			//遍历原始被计算的元素
			$.each(componentIds,function(index,monitorFieldObj){
				//被计算控件的唯一标识
				var monitorField = monitorFieldObj.value;
				var element = $("body").find("[fieldId='"+monitorField+"']");
				//被计算控件的组件类型
				var easyWinCompon = $(element).attr("easyWinCompon");
				
				if(easyWinCompon == 'Monitor'){//计算控件
					//计算类型
					var monitortype = $(element).attr("monitortype");
					//计算的日期，则该元素为权重元素
					if(monitortype == 'date'){
						var key = $(element).attr("fieldId");
						//已有权重
						var subNum = calNumers[key];
						if(!subNum){
							subNum = 0;
						}
						//权重加1，重构权重元素的比重
						calNumers[key] = accAdd(subNum,1);
						return true;
					}
					//计算关联的控件
					var subComponentIds = calNumBackFields[monitorField];
					//每一个关联控件的权重
					var calSubNumerWeight = EasyWinForm.constWeight(monitorField,subComponentIds);
					if(calSubNumerWeight){
						//key为权重元素， calSubNumerWeight为权重元素对应的权重
						for(var key in calSubNumerWeight){
							if(key == 'in_array' ){
								return true;
							}
							//已有的权重元素占比
							var subNum = calNumers[key];
							if(!subNum){
								subNum = 0;
							}
							//本次获得的权重元素占比
							var value = calSubNumerWeight[key];
							//重构权重元素的比重
							calNumers[key] = accAdd(subNum,value);
						}
						
					}
					
				}else if(easyWinCompon == 'NumberComponent'){//是权重元素，不需要遍历，直接构建
					//权重元素占比
					var subNum = calNumers[monitorField];
					if(!subNum){
						subNum = 0;
					}
					//重构权重元素的比重
					calNumers[monitorField] = accAdd(subNum,1);
				}
			})
			//计算控件解析后的权重元素以及占比情况
			calNumWeight[fieldId] = calNumers;
		}
		return calNumers;
	},
    initEvent: function () {
        $("body").on("keyup", ".justNum", function () {
            var val = $(this).val();
            //得到第一个字符是否为负号
            var t = val.charAt(0);
            //先把非数字的都替换掉，除了数字和.
            val = val.replace(/[^\d\.]/g, '');
            //必须保证第一个为数字而不是.
            val = val.replace(/^\./g, '');
            //保证只有出现一个.而没有多个.
            val = val.replace(/\.{2,}/g, '.');
            //保证.只出现一次，而不能出现两次以上
            val = val.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.');
            //如果第一位是负号，则允许添加
            if (t == '-') {
                val = '-' + val;
            }
            $(this).val(val);
        })
        $("body").on("keypress", ".justNum", function (event) {
            var eventObj = event || e;
            var keyCode = eventObj.keyCode || eventObj.which;
            if ((keyCode >= 48 && keyCode <= 57) || keyCode == 46 || keyCode == 45) {
                return true
            } else {
                return false;
            }
        })

        $("body").on("click", ".WdatePicker", function (obj) {
            var formart = $(this).attr("formart");
            var _this = $(this);
            if ($(this).parent().hasClass("dateTime")) {
                var preId = $(this).parents("[easywincompon='DateInterval']").find(".dateTimeS").attr("id")
                var nextId = $(this).parents("[easywincompon='DateInterval']").find(".dateTimeE").attr("id");
                var pFieldId = $(this).attr("pFieldId");

                var DateObj = {};
                DateObj.el = $(_this);
                DateObj.startId = preId;
                DateObj.endId = nextId;

                DateObj.pFieldId = pFieldId;

                if ($(this).hasClass("dateTimeS")) {
                    WdatePicker({
                        dateFmt: formart,
                        maxDate: '#F{$dp.$D(\'' + nextId + '\',{m:-0});}',
                        onpicked: function (dp) {
                            $(_this).trigger("calDateTime", DateObj);
                        }
                    })
                } else {
                    WdatePicker({
                        dateFmt: formart,
                        minDate: '#F{$dp.$D(\'' + preId + '\',{m:+0});}',
                        onpicked: function (dp) {
                            $(_this).trigger("calDateTime", DateObj);
                        }
                    })
                }
            } else {
                WdatePicker({dateFmt: formart})
            }

        })
        //人员选择
        $("body").on("click", ".userSelect", function () {
        	
            var isUnique = $(this).attr("isUnique");
            var _this = $(this);
            if(EasyWin.sourcePage=='mobile'){
            	selectUser(isUnique,$(_this).parent().attr("fieldid"));
            }else{
            	var _spanUserMod = $("<span class='text tempUser tempShowData'></span>")
            	
            	if (isUnique == 'false') {
            		userMore('null', null, sid, 'no', 'null', function (options) {
            			if (options && options.length > 0) {
            				$(_this).parent().find(".tempUser").remove();
            				$.each(options, function (optIndex, optObj) {
            					var userId = optObj.value
            					var userName = optObj.text;
            					var _spanUser = $(_spanUserMod).clone().html(userName);
            					if (options.length > optIndex + 1) {
            						_spanUser.append("<br/>");
            					}
            					var tempShowData = {};
            					tempShowData.optionId = userId
            					tempShowData.content = userName;
            					$(_spanUser).data("tempShowData", tempShowData);
            					
            					$(_this).before(_spanUser);
            				})
            			}
            		})
            	} else {//默认单选
            		userOne('null', 'null', null, sid, function (options) {
            			if (options && options.length > 0) {
            				$.each(options, function (optIndex, optObj) {
            					$(_this).parent().find(".tempUser").remove();
            					var userId = optObj.value
            					var userName = optObj.text;
            					
            					var _spanUser = $(_spanUserMod).clone().html(userName);
            					
            					var tempShowData = {};
            					tempShowData.optionId = userId
            					tempShowData.content = userName;
            					$(_spanUser).data("tempShowData", tempShowData);
            					
            					$(_this).before(_spanUser)
            				})
            			}
            		})
            	}
            }


        })
        //部门选择
        $("body").on("click", ".depSelect", function () {
            var isUnique = $(this).attr("isUnique");
            var _this = $(this);
            
            if(EasyWin.sourcePage=='mobile'){
            	selectDep(isUnique,$(_this).parent().attr("fieldid"));
            }else{
            	var _spanDepMod = $("<span class='text tempDep tempShowData'></span>")
            	if (isUnique == 'false') {
            		depMore('null', null, sid, function (options) {
            			if (options && options.length > 0) {
            				$(_this).parent().find(".tempDep").remove();
            				$.each(options, function (optIndex, optObj) {
            					var depId = optObj.value
            					var depName = optObj.text;
            					
            					var _spanDep = $(_spanDepMod).clone().html(depName);
            					if (options.length > optIndex + 1) {
            						_spanDep.append("<br/>");
            					}
            					var tempShowData = {};
            					tempShowData.optionId = depId
            					tempShowData.content = depName;
            					$(_spanDep).data("tempShowData", tempShowData);
            					
            					$(_this).before(_spanDep)
            				})
            			}
            		}, null)
            	} else {//默认单选
            		depOne('null', 'null', null, sid, function (result) {
            			if (result) {
            				$(_this).parent().find(".tempDep").remove();
            				var depId = result.orgId
            				var depName = result.orgName;
            				
            				var _spanDep = $(_spanDepMod).clone().html(depName);
            				
            				var tempShowData = {};
            				tempShowData.optionId = depId
            				tempShowData.content = depName;
            				$(_spanDep).data("tempShowData", tempShowData);
            				
            				$(_this).before(_spanDep)
            			}
            		})
            		
            	}
            }

        })
        //动态添加行
        $("body").on("click", ".optAddDataTable,[opt-btn='optAddDataTable']",function () {
        	
        	 var subTableBody = $(this).parents("[easywincompon='DataTable']").find(".optAddDataTable").parents(".subTableHead").next();
        	 
             var subTableTr = $(subTableBody).find(".subTableTr:eq(0)").clone();
             $(subTableTr).removeAttr("busTableId");
             $(subTableTr).removeAttr("modTr");
             $(subTableTr).find(".tdItem").html('');
             
             
             var sublayoutDetails = $(subTableBody).data("sublayoutDetail");
             var optbtn = $(this).attr("opt-btn");
             var relatetable = $(this).parents("[easywincompon='DataTable']").attr("conf_relatetable");
             if(!optbtn && relatetable){//判断是否采用关联表单数据
        		var preBusTableObjs = $(this).parents("[easywincompon='DataTable']").find("[busTableId]");
        		var preBusTableIds = new Array();
        		if(preBusTableObjs && preBusTableObjs.get(0)){
        			$.each(preBusTableObjs,function(index,obj){
        				var busTableId = $(this).attr("busTableId");
        				preBusTableIds.push(busTableId);
        			})
        		}
        		if(EasyWin.sourcePage=='mobile'){
        			//手机端选择关联表数据
        			relateTableChoose(relatetable,$(subTableBody).parent().attr("subformid"));
        			return; 
        		}
        		EasyWinForm.relateTableChoose({
    				relatetable:relatetable,
    				preBusTableIds:preBusTableIds,
    				callback:function(data){
    					EasyWinForm.optSubDataTable(data,relatetable,subTableBody,$(subTableTr));
            		}
        		})
        		return;
        	}
            var effectCpmponents = new Array();
            var effectIndex = 0;
            $.each(sublayoutDetails, function (index, sublayoutDetail) {
                if (sublayoutDetail.componentKey != 'subItemTitle') {
                    effectCpmponents.push(sublayoutDetail);
                    var defineTag = $("<easyWin></easyWin>");
                    defineTag.attr("tempId", sublayoutDetail.tempId);
                    defineTag.attr("componKey", sublayoutDetail.componentKey);
                    $(subTableTr).find(".tdItem:eq(" + effectIndex + ")").append(defineTag);
                    effectIndex++;
                }
            })
            $.each(effectCpmponents, function (index, sublayoutDetail) {
            	var sublayoutDetailObj= {}
            	$.extend(sublayoutDetailObj,sublayoutDetail);
            	sublayoutDetailObj.tableColumn = null;
                EasyWinForm.analyseComponent(sublayoutDetailObj, $(subTableTr));
            })

            $(subTableBody).append(subTableTr);

        });
        //动态删除行
        $("body").on("click", ".optDelDataTable", function () {
            var len = $(this).parents(".subTableBody").find(".subTableTr").length;
            //删除元素中有数字的
            var numCompones = $(this).parents(".subTableTr").find("[easyWinCompon='NumberComponent'],[easyWinCompon='Monitor']");
            if (len > 1) {
                $(this).parents(".subTableTr").remove();
            } else {
                scrollToLocation($("#contentBody"), $(this));
                $(this).focus();//控件聚焦
                layer.tips("需要保留一行数据", $($(this).parents(".subTableBody").find(".subTableTr")), {tips: 1})
            }
            
            EasyWinForm.optDataTableConsume(numCompones);
        });

      //序列编号
		var easywinSerialNums = $("body").find(".easywinSerialNum");
		if(easywinSerialNums && easywinSerialNums.get(0)){
			$.each(easywinSerialNums,function(index,easywinSerialNum){
				var componentData = $(this).data("componentData");
				var _this = $(this);
				EasyWinForm.optForm.constrSerialNum(componentData.serialNumId,function(result){
					$(_this).val(result.serialNum);
					$(_this).data("serialNumObj",result.serialNumObj);
				});
			});
		}
		
        //关联模块选择
        $("body").on("click", ".relateModSelect", function () {
            var busType = $(this).attr("busType")
            var isUnique = $(this).attr("isUnique");
            var _this = $(this);
            var selectWay = isUnique == 'true' ? 1 : 2;
            if(EasyWin.sourcePage=='mobile'){
            	relateModChoose(busType,isUnique,$(_this).parent().attr("fieldid"));
            	return;
            }
            if (busType == '005') {
                var _spanItemMod = $("<span class='text tempItem tempShowData'></span>")
                itemMoreSelect(selectWay, "null", function (items) {
                    if (items && items.length > 0) {
                        $(_this).parent().find(".tempItem").remove();
                        $.each(items, function (optIndex, item) {
                            var itemId = item.id;
                            var itemName = item.itemName;
                            var _spanItem = $(_spanItemMod).clone().html(itemName);
                            if (items.length > optIndex + 1) {
                            	_spanItem.append("<br/>");
        					}
                            var tempShowData = {};
                            tempShowData.optionId = itemId
                            tempShowData.content = itemName;
                            $(_spanItem).data("tempShowData", tempShowData);

                            $(_this).before(_spanItem)
                        })
                    }
                })
            } else if (busType == '012') {
                var _spanItemMod = $("<span class='text tempCrm tempShowData'></span>")
                crmMoreSelect(selectWay, "null", function (crms) {
                    if (crms && crms.length > 0) {
                        $(_this).parent().find(".tempCrm").remove();
                        $.each(crms, function (optIndex, crm) {
                            var crmId = crm.id;
                            var crmName = crm.crmName;

                            var _spanCrm = $(_spanItemMod).clone().html(crmName);
                            if (crms.length > optIndex + 1) {
                            	_spanCrm.append("<br/>");
        					}
                            var tempShowData = {};
                            tempShowData.optionId = crmId
                            tempShowData.content = crmName;
                            $(_spanCrm).data("tempShowData", tempShowData);

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
							if (eventPms.length > optIndex + 1) {
								 _spanEvent.append("<br/>");
	        				}
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
							if (issuePms.length > optIndex + 1) {
								_spanIssue.append("<br/>");
	        				}
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
							if (modifyPms.length > optIndex + 1) {
								_spanModify.append("<br/>");
	        				}
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
							var _spanRelease =  $(_spanReleaseMod).clone().html(releaseSerialId);
							if (releasePms.length > optIndex + 1) {
								_spanRelease.append("<br/>");
	        				}
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
        $("body").on("click", ".spUsedIdea", function () {
            addIdea('spIdeaText', sid);
        })
        // 保存常用意见
        $("body").on("dblclick", "#spIdeaText", function () {
            var value = $(this).val();
            var len = charLength(value.replace(/\s+/g, ""));
            if (len == 0) {
                return;
            }
            if (len % 2 == 1) {
                len = (len + 1) / 2;
            } else {
                len = len / 2;
            }
            if (len > 30) {
                value = cutstr(value, 30);
            }
            $.ajax({
                type: "post",
                url: "/usagIdea/ajaxAddUsagIdea?sid=" + sid + "&t=" + Math.random(),
                dataType: "json",
                data: {"idea": value},
                success: function (data) {
                    if (data.status == 'y') {
                        showNotification(1, "已存入常用意见")
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    showNotification(2, "系统错误，请联系管理员！")
                }
            });
        });

        $("body").on("click", ".viewRelateModInfo", function () {
            var busId = $(this).attr("busId");
            var busType = $(this).attr("busType");
            if(busType.substr(0,3)=='042'){
        		url = "/iTOm/viewItomDetail?sid=" + sid + "&busId=" + busId+" &busType="+busType;
        		openWinWithPams(url, "800px", ($(window).height() - 10) + "px");
        	}else{
        		authCheck(busId, busType, null, function (authState) {
        			var url;
        			if (busType == '005') {
        				url = "/item/viewItemPage?sid=" + sid + "&id=" + busId;
        			} else if (busType == '012') {
        				url = "/crm/viewCustomer?sid=" + sid + "&id=" + busId;
        			}
        			if (url) {
        				openWinWithPams(url, "800px", ($(window).height() - 10) + "px");
        			}
        		})
        	}
            
        })
    },
    constrHtml: function (formLayout) {
        var layoutHtml = formLayout.formLayoutHtml.layoutHtml;

        var temp_layoutHtml = $("<div></div>").append(layoutHtml);
        $(temp_layoutHtml).find(".tempClz").remove();

        $(temp_layoutHtml).find("tr").css("min-height", "30px");
        $(temp_layoutHtml).find("td").css("padding", "5px 5px");
        var lastPObj = $(temp_layoutHtml).find("p:last");
        while ($(lastPObj).html() == '<br>') {
            var lastPrePObj = $(lastPObj).prev();
            $(lastPObj).remove();
            lastPObj = lastPrePObj
        }
        
        var firstPObj = $(temp_layoutHtml).find("p:first");
        while ($(firstPObj).html() == '<br>' || $(firstPObj).html() == '&nbsp;') {
        	var firstPrePObj = $(firstPObj).next();
        	$(firstPObj).remove();
        	firstPObj = firstPrePObj
        }

        $(temp_layoutHtml).find("table").addClass("table table-bordered")
        $(temp_layoutHtml).find("td").css("border-bottom-color", "#000")
        $(temp_layoutHtml).find("td").css("border-top-color", "#000")
        $(temp_layoutHtml).find("td").css("border-left-color", "#000")
        $(temp_layoutHtml).find("td").css("border-right-color", "#000")

        var layoutDetails = JSON.parse(formLayout.layoutDetail);
        layoutDetails ? $.each(layoutDetails, function (index, layoutDetail) {
            EasyWinForm.analyseComponent(layoutDetail, $(temp_layoutHtml));
        }) : "";
        $("#form-info-div").html($(temp_layoutHtml));
    },
    constrSonRelate: function (sonFlow) {
        var html = '<div class="field field_js form-field-textarea field-hoz">'
            + '			<div class="pull-left" style="width"50%">'
            + '				<label class="widget-title">'
            + '					<span class="widget-title_js">关联流程</span>'
            + '				</label>'
            + '				<div class="widget-content">'
            + '					<div class="input-instead j_readOnly"><a href="javascript:void(0)" id="relateSonFlow">' + sonFlow.sonFlowName + '</a></div>'
            + '				</div>'
            + '			</div>'
            + '			<div class="pull-left" style="width"50%">'
            + '				<label class="widget-title" style="width:90px!important">'
            + '					<span class="widget-title_js">流水号</span>'
            + '				</label>'
            + '				<div class="widget-content" style="width:190px!important">'
            + '					<div class="input-instead j_readOnly">' + sonFlow.sonFlowSerialNum + '</div>'
            + '				</div>'
            + '			</div>'
            + '	</div>';
        $("#formpreview").append(html);

        $("#relateSonFlow").off("click").on("click", function () {
            var url = "/workFlow/viewSpFlow?sid=" + sid + "&instanceId=" + sonFlow.sonInstanceId
            window.location.replace(url);
        })
    },
    analyseComponent: function (layoutDetail, layoutHtml) {
        var componKey = layoutDetail.componentKey;
        var fieldId = layoutDetail.fieldId;
        var tempId = layoutDetail.tempId;

        var required = layoutDetail.required;

        var title = layoutDetail.title;
        
        var conf_tableColumn = layoutDetail.tableColumn;
        
        var isReadOnly = layoutDetail.isReadOnly;
        
        var _spanRequire = $("<span style='color:red' class='tempClz'>*</span>");
        var easywinTag = $(layoutHtml).find("easywin[tempid='" + tempId + "']");
        if (conf_tableColumn && $(easywinTag).parents("[bustableid]").length > 0) {
        	isReadOnly = 'false';
        }
        if ("TableLayout" == componKey
            || "TbodyLayout" == componKey
            || "TdLayout" == componKey
            || "ColumnPanel" == componKey
            || "TrLayout" == componKey) {
            var subLayout = layoutDetail.layoutDetail;
            if (subLayout && subLayout.length > 0) {
                $.each(subLayout, function (index, obj) {
                    EasyWinForm.analyseComponent(obj, $(layoutHtml));
                })
            }

            $(easywinTag).remove();
            return false;
        }
        if ("Text" == componKey) {//文本框
            var _html;
            if (isReadOnly == 'false') {//只读
                _html = $("<span style='width:80px;display:inline-bolck' class='easywinView'><span>")
                _html.addClass("fieldReadOnly");
            } else {
                _html = $("<input type='text' class='easywinInput' style='width:80px'/>");
                if (required == 'true') {//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }
            }
            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId);

            var conf_size = layoutDetail.size;
            conf_size = conf_size ? conf_size : 'medium';
            if (conf_size == 'small') {
                _html.css("width", "30%");
            } else if (conf_size == 'medium') {
                _html.css("width", "60%");
            } else if (conf_size == 'large') {
                _html.css("width", "95%");
            }
            
            $(_html).attr("conf_tableColumn",conf_tableColumn);

            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);
        } else if ("TextArea" == componKey) {
            var _html;
            if (isReadOnly == 'false') {
                $(easywinTag).parents("td").css("word-wrap", "break-word");
                $(easywinTag).parents("td").css("word-break", "break-all");

                _html = $("<span class='easywinView'><span>")
                _html.addClass("fieldReadOnly");
            } else {
                _html = $("<textarea resize='none' style='height:95%;min-height:50px'></textarea>");

                var len = $(easywinTag).parents("td").length;
                if (len == 1) {
                    $(easywinTag).parents("td").attr("cellpadding", "0");
                    $(easywinTag).parents("td").attr("cellspacing", "0");
                    $(easywinTag).parents("td").css("margin", "0px 0px");
                }

                var conf_size = layoutDetail.size;
                conf_size = conf_size ? conf_size : 'medium'
                if (conf_size == 'small') {
                    _html.css("width", "30%");
                } else if (conf_size == 'medium') {
                    _html.css("width", "60%");
                } else if (conf_size == 'large') {
                    _html.css("width", "95%");
                }

                if (required == 'true') {//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }

            }
            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId);

            $(_html).attr("conf_tableColumn",conf_tableColumn);
            
            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);

        } else if ('NumberComponent' == componKey) {
            var _html;
            if (isReadOnly == 'false') {//只读
                _html = $("<span style='width:80px;' class='easywinView'><span>")
                _html.addClass("fieldReadOnly inlineblock");
            } else {
                _html = $("<input type='text' class='easywinInput justNum' style='width:80px'/>");
                if (required == 'true') {//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }
            }
            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId);


            var conf_size = layoutDetail.size;
            conf_size = conf_size ? conf_size : 'medium';
            if (conf_size == 'small') {
                _html.css("width", "30%");
            } else if (conf_size == 'medium') {
                _html.css("width", "60%");
            } else if (conf_size == 'large') {
                _html.css("width", "95%");
            }

            $(_html).attr("conf_tableColumn",conf_tableColumn);
            
            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);
        }else if('SerialNum' == componKey){
			var _html;
			if(isReadOnly  == 'false'){//只读
				_html = $("<span style='width:80px;' class='easywinView'></span>")
				_html.addClass("fieldReadOnly inlineblock");
			}else{
				_html  = $("<input type='text' class='easywinInput easywinSerialNum' style='width:80px'/>");
				if(required=='true'){//是必填
					_html.addClass("check_js")
					$(easywinTag).before(_spanRequire);
				}
			}
			_html.attr("easyWinCompon",componKey);
			_html.attr("fieldId",fieldId);
			
			var conf_size = layoutDetail.size;
			conf_size = conf_size?conf_size:'medium';
			if(conf_size == 'small'){
				_html.css("width","30%");
			}else if(conf_size == 'medium'){
				_html.css("width","60%");
			}else if(conf_size == 'large'){
				_html.css("width","95%");
			}
			
			$(_html).attr("conf_tableColumn",conf_tableColumn);
			
			_html.data("componentData",layoutDetail);
			
			$(easywinTag).before(_html);

        } else if ("RadioBox" == componKey) {

            var len = $(easywinTag).parents("td").length;
            if (len == 1) {
                $(easywinTag).parents("td").attr("cellpadding", "0");
                $(easywinTag).parents("td").attr("cellspacing", "0");
                $(easywinTag).parents("td").css("padding", "0px 0px");
                $(easywinTag).parents("td").css("margin", "0px 0px");

                $(easywinTag).parents("td").css("word-wrap", "break-word");
                $(easywinTag).parents("td").css("word-break", "break-all");

                var height = $(easywinTag).parents("td").height();
            }

            var _html = $("<span class='myDiv' style='text-align:center;'></span>");
            var options = layoutDetail.options;

            _html.css("word-break", "normal");
            _html.css("width", "auto");

            var optName = "field_" + fieldId + "" + new Date().getTime();
            if ($(easywinTag).parents("[easyWinCompon='DataTable']").length > 0) {
                var subIndex = $(easywinTag).parents(".subTableTr").index();
                optName = "field_sub" + subIndex + "_" + fieldId + "" + new Date().getTime();
            }
            $.each(options, function (optIndex, optObj) {
                var optFieldId = optObj.fieldId
                var option = $("<lable style='clear:both;position:relative;'><input style='position:absolute;left:11px !important;top:0px !important' type='" + (isReadOnly == 'false' ? "checkbox" : "radio") + "'/><span class='text' style='margin-left:10px;'>" + optObj.name + "</span></lable>")

                $(option).find("input").attr("name", optName);
                $(option).find("input").attr("value", optFieldId);

                $(option).find("input").data("componentData", optObj);

                _html.append(option);
            })
            var titleLayout = layoutDetail.titleLayout;

            'field-hoz' == titleLayout ? "" : $(_html).find("lable").append("<br/>");

            if (isReadOnly == 'false') {//只读
                _html.addClass("fieldReadOnly");
                _html.find("input[type='checkbox']").on("click", function () {
                    return false
                });
            } else {
                if (required == 'true') {//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }
            }
            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId)

            $(_html).attr("conf_tableColumn",conf_tableColumn);
            $(_html).attr("conf_sysDataDic",layoutDetail.sysDataDic);
            
            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);
        } else if ("CheckBox" == componKey) {

            var _html = $("<span class='myDiv'></span>");
            var options = layoutDetail.options;
            $.each(options, function (optIndex, optObj) {
                var optFieldId = optObj.fieldId
                var optName = optObj.name;
                var option = $("<lable style='position:relative;'><input style='position:absolute;left:11px !important;top:0 !important;' type='checkbox'/><span class='text' style='margin-left:10px;'>" + optName + "</span></lable>")
                $(option).find("input").attr("name", "field_" + fieldId);
                $(option).find("input").attr("value", optFieldId);

                $(option).find("input").data("componentData", optObj);

                _html.append(option);
            })
            var titleLayout = layoutDetail.titleLayout;
            'field-hoz' == titleLayout ? "" : $(_html).find("lable").append("<br/>");

            if (isReadOnly == 'false') {//只读
                _html.addClass("fieldReadOnly");
                _html.find("input[type='checkbox']").on("click", function () {
                    return false
                });
            } else {
                if (required == 'true') {//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }
            }
            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId);
            
            $(_html).attr("conf_tableColumn",conf_tableColumn);
            $(_html).attr("conf_sysDataDic",layoutDetail.sysDataDic);

            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);
        } else if ("Select" == componKey) {
            var _html;
            if (isReadOnly == 'false') {//只读
                _html = $("<span style='width:80px' class='easywinView'><span>")
                _html.addClass("fieldReadOnly");
            } else {
                _html = $("<span class='myDiv'></span>");

                var _selectHtml = $("<select></select>");

                var option = $("<option>请选择</option>")
                $(option).attr("fieldId", 0);
                $(option).attr("value", "");
                $(_selectHtml).append(option);

                var options = layoutDetail.options;
                $.each(options, function (optIndex, optObj) {
                    var optFieldId = optObj.fieldId
                    var optName = optObj.name;

                    var option = $("<option>" + optName + "</option>")
                    $(option).attr("fieldId", optFieldId);
                    $(option).attr("value", optFieldId);

                    $(_selectHtml).append(option);
                });
                if (required == 'true') {//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }
                _html.append(_selectHtml);
            }

            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId);
            
            $(_html).attr("conf_tableColumn",conf_tableColumn);
            $(_html).attr("conf_sysDataDic",layoutDetail.sysDataDic);

            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);

        } else if ("DateComponent" == componKey) {
            var _html;
            var confFormat = layoutDetail.format;
            if (isReadOnly == 'false') {//只读
                _html = $("<span style='width:90px' class='easywinView myDiv'><span>")
                _html.addClass("fieldReadOnly");
            } else {

                var isDefault = layoutDetail.isDefault;
                var isEdit = layoutDetail.isEdit;//是否可以编辑（默认可以）

                var nowDate = EasyWin.nowTimeLong ? new Date(Number(EasyWin.nowTimeLong)) : new Date();
                var nowDateStr = nowDate.pattern(confFormat);

                var canOpt = 1;
                if (isDefault == 'true' && isEdit == 'false') {
                    _html = $("<span class='easywinView myDiv'><span>")
                    _html.html(nowDateStr);
                    _html.addClass("fieldReadOnly defVal");

                    $(_html).data("oldContent", nowDateStr)
                    canOpt = 0;
                }

                if (canOpt) {//可以编辑
                    _html = $("<input type='text' class='WdatePicker easywinInput' readonly='readonly' style='width:50px'>");
                    _html.attr("formart", confFormat);
                    
                    if(isDefault == 'true'){
                    	$(_html).attr("value",nowDateStr);
                    }
                    
                    confFormat == 'yyyy-MM' ? _html.css("width", "90px") : confFormat == 'yyyy年MM月' ? _html.css("width", "90px") : confFormat == 'yyyy-MM-dd' ? _html.css("width", "90px") : confFormat == 'yyyy年MM月dd日' ? _html.css("width", "120px") : _html.css("width", "150px");

                    if (required == 'true') {//是必填
                        _html.addClass("check_js")
                        $(easywinTag).before(_spanRequire);
                    }
                }

            }
            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId);
            
            $(_html).attr("conf_tableColumn",conf_tableColumn);

            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);

        } else if ('DateInterval' == componKey) {
            var _html;
            var confFormat = layoutDetail.format;
            if (isReadOnly == 'false') {//只读
                _html = $("<span class='easywinView'><span>")
                _html.addClass("fieldReadOnly");

                var start = layoutDetail.start;
                var _startDateInput = $("<span class='dateTime'></span>");
                $(_startDateInput).addClass("fieldReadOnly");
                start.isReadOnly = 'false';
                $(_startDateInput).data("componentData", start)

                $(_startDateInput).attr("title", start.title);
                $(_startDateInput).attr("easyWinCompon", "DateComponent");
                $(_startDateInput).attr("fieldId", start.fieldId);
                $(_startDateInput).attr("id", fieldId + "_fieldIdS_" + new Date().getTime());

                var end = layoutDetail.end;
                var _endDateInput = $("<span class='dateTime'></span>");
                $(_endDateInput).addClass("fieldReadOnly");
                end.isReadOnly = 'false';
                $(_endDateInput).data("componentData", end)

                $(_endDateInput).attr("title", end.title);
                $(_endDateInput).attr("easyWinCompon", "DateComponent");
                $(_endDateInput).attr("fieldId", end.fieldId);
                $(_endDateInput).attr("id", fieldId + "_fieldIdE_" + new Date().getTime());

                _html.append(_startDateInput);
                _html.append("<span style='margin-left:5px;margin-right:5px;'>至</span>");
                _html.append(_endDateInput);

            } else {
                _html = $("<span class='myDiv'></span>");

                var confFormat = layoutDetail.start.format;

                var start = layoutDetail.start;
                var _startDateInput = $("<span class='dateTime'><input class='WdatePicker dateTimeS' type='text' style='width:90px' placeholder='yyyy-MM-dd'/></span>");
                start.isReadOnly = 'true';
                $(_startDateInput).find("input").data("componentData", start)

                $(_startDateInput).find("input").attr("readonly", true);
                $(_startDateInput).find("input").attr("title", start.title);
                $(_startDateInput).find("input").attr("easyWinCompon", "DateComponent");
                $(_startDateInput).find("input").attr("fieldId", start.fieldId);
                $(_startDateInput).find("input").attr("id", fieldId + "_fieldIdS_" + new Date().getTime());

                var end = layoutDetail.end;
                var _endDateInput = $("<span class='dateTime'><input class='WdatePicker dateTimeE' type='text' style='width:90px' placeholder='yyyy-MM-dd'/></span>");
                end.isReadOnly = 'true';
                $(_endDateInput).find("input").data("componentData", end)

                $(_endDateInput).find("input").attr("readonly", true);
                $(_endDateInput).find("input").attr("title", end.title);
                $(_endDateInput).find("input").attr("easyWinCompon", "DateComponent");
                $(_endDateInput).find("input").attr("fieldId", end.fieldId);
                $(_endDateInput).find("input").attr("id", fieldId + "_fieldIdE_" + new Date().getTime());

                _html.append(_startDateInput);
                _html.append("<span style='margin-left:5px;margin-right:5px;'>至</span>");
                _html.append(_endDateInput);

                if (confFormat == 'yyyy-MM') {
                    $(_html).find("input").css("width", "80px");
                    $(_html).find("input").attr("placeholder", "yyyy-MM");
                } else if (confFormat == 'yyyy-MM-dd HH:mm') {
                    $(_html).find("input").css("width", "120px");
                    $(_html).find("input").attr("placeholder", "yyyy-MM-dd HH:mm");
                } else {
                    $(_html).find("input").css("width", "90px");
                    $(_html).find("input").attr("placeholder", "yyyy-MM-dd");
                }

                $(_html).find("input").attr("pFieldId", fieldId);
                $(_html).find("input").attr("formart", confFormat);


                if (required == 'true') {//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }

            }
            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId)

            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);

        } else if ("Monitor" == componKey) {
            var _html;
            if (isReadOnly == 'false') {//只读
                _html = $("<span style='width:80px' class='easywinView defVal'><span>")
                _html.addClass("fieldReadOnly");
                
                //计算类型属性
                _html.attr("monitorType",layoutDetail.monitorType);
                //需要计算的组件
                var monitorFields = layoutDetail.monitorFields;
                //计算的是数字
                if(layoutDetail.monitorType == 'number'){
               	 if (monitorFields && monitorFields.length > 0) {//有需要计算的组件
               		 //缓存计算控件对应的需要计算的组件
               		calNumBackFields[fieldId]=monitorFields;
               		//遍历原始元素
               		 $.each(monitorFields,function(index,monitorFieldObj){
               			//原始元素的唯一标识
               			var monitorField = monitorFieldObj.value;
               			
               			//原始元素便恭候需要触发的计算控件集合
						var calBackFields = calNumFields[monitorField];
						calBackFields = calBackFields?calBackFields:new Array();
						
						//添加一份关联数据
						calBackFields.push(fieldId);
						
						//缓存原始元素便恭候需要触发的计算控件集合
						calNumFields[monitorField]= calBackFields;
						
						//权重元素变动后需要触发的计算控件
						$("body").off("keyup","[easyWinCompon='NumberComponent'][fieldId='"+monitorField+"']")
							 	 .on("keyup","[easyWinCompon='NumberComponent'][fieldId='"+monitorField+"']",function(){
							//权重元素的唯一标识
							var fieldId = $(this).attr("fieldId");
							//权重元素需要触发的计算控件集合
							var calBackFields = calElementRelate[fieldId];
							if(!calBackFields){//没有需要触发的元素，则再次循环
								return true;
							}
							
							//权重元素的对象
							var _this = $(this);
							//该元素是否在子表单中
							var subInDataTable = $(this).parents("[easyWinCompon='DataTable']");
							//遍历触发的计算控件集合
							$.each(calBackFields,function(index,relateTempId){
								//计算控件的查找范围
								var _htmlScope = $("body");
								//该计算控件的组件信息
								var calComponent = $("body").find("[easyWinCompon='Monitor'][fieldId='"+relateTempId+"']");
								//该计算控件是否在子表单中
								var calSubInDataTable = $(calComponent).parents("[easyWinCompon='DataTable']");
								if(subInDataTable && subInDataTable.get(0)
										&& calSubInDataTable && calSubInDataTable.get(0)){
									//计算控件和权重元素在子表单中，则查询范围为同一行元素
									_htmlScope = $(_this).parents(".subTableTr");
								}
								
								//计算结果，默认0
								var total = 0;
								//计算控件的权重元素集合
								var subArray = calNumWeight[relateTempId];
								//遍历权重元素集合
								for(var key in subArray){
									if(key != 'in_array'){
										//权重
										var weight = subArray[key];
										//权重元素求和
										var subTotal = EasyWinForm.calTotalNum(key,_htmlScope);
										//计算结果
										total = accAdd(total,accMul(subTotal,weight));
									}
								}
								//查找计算对应的控件
								var calComponent = $(_htmlScope).find("[easyWinCompon='Monitor'][fieldId='"+relateTempId+"']");
								//是否只读
								var isReadOnly = $(calComponent).data("componentData").isReadOnly;
								if (isReadOnly == 'false') {
                					 $(calComponent).addClass("defVal");
                                     $(calComponent).html(total);
                                 } else {
                                     $(calComponent).val(total);
                                 }

							});
		    			});
               		 })
               	 }
               }
                
            } else {
                _html = $("<input type='text' class='easywinInput' style='width:80px'/>");
                //禁止修改
                $(_html).on("focus",function(){
                	this.blur();
                })
                var conf_size = layoutDetail.size;
                conf_size = conf_size ? conf_size : 'medium';
                if (conf_size == 'small') {
                    _html.css("width", "30%");
                } else if (conf_size == 'medium') {
                    _html.css("width", "60%");
                } else if (conf_size == 'large') {
                    _html.css("width", "95%");
                }
                _html.attr("monitorType",layoutDetail.monitorType);
                var monitorFields = layoutDetail.monitorFields;
                if (layoutDetail.monitorType == 'date') {
                	//计算时间类型
                	$(_html).data("calTimeType", layoutDetail.calTimeType);
                    if (monitorFields && monitorFields.length > 0) {
                        var calFieldId = monitorFields[0].value;

                        var calBackFields = calFields["cal_" + calFieldId];
                        calBackFields = calBackFields ? calBackFields : new Array();
                        calBackFields.push(fieldId);

                        calFields["cal_" + calFieldId] = calBackFields;

                        $("body").bind("calDateTime", $("[easyWinCompon='DateInterval'][fieldId='" + calFieldId + "']").find(".WdatePicker"), function (event, DateObj) {
                            var el = DateObj.el;
                            var startDate = $("#" + DateObj.startId).val();
                            var endDate = $("#" + DateObj.endId).val();
                            if (startDate && endDate) {
                                var hour = EasyWinForm.calDateTimeNum(startDate, endDate, layoutDetail.calTimeType);

                                var pFieldId = DateObj.pFieldId;
                                var calBackFields = calFields["cal_" + pFieldId];
                                if (calBackFields && calBackFields.length > 0) {
                                    var _htmlScope = $("body");

                                    if ($(el).parents("[easyWinCompon='DataTable']").length > 0) {
                                        _htmlScope = $(el).parents(".subTableTr");
                                    }
                                    $.each(calBackFields, function (calFileIndex, calFileId) {
                                        var calComponent = $(_htmlScope).find("[easyWinCompon='Monitor'][fieldId='" + calFileId + "']");

                                        var isReadOnly = $(calComponent).data("componentData").isReadOnly;
                                        if (isReadOnly == 'false') {
                                            $(calComponent).addClass("defVal");
                                            $(calComponent).html(hour);
                                        } else {
                                            $(calComponent).val(hour);
                                        }
                                        //触发计算求和
                                        $(calComponent).trigger("calDateSum",$(calComponent));
                                    })
                                }

                            }
                        })
                    }
                }else if(layoutDetail.monitorType == 'number'){//数字求和
                	 if (monitorFields && monitorFields.length > 0) {
                		 //计算控件对应的原始求和元素
                		 calNumBackFields[fieldId]=monitorFields;
                		 //被计算控件
                		 $.each(monitorFields,function(index,monitorFieldObj){
                			var monitorField = monitorFieldObj.value;
 							//被计算控件的关联控件
 							var calBackFields = calNumFields[monitorField];
 							//关联控件若是不存在，则创建一个数组
 							calBackFields = calBackFields?calBackFields:new Array();
 							//添加一份关联数据
 							calBackFields.push(fieldId);
 							
 							calNumFields[monitorField]= calBackFields;
 							//监听权重元素的键盘事件
 							$("body").off("keyup","[easyWinCompon='NumberComponent'][fieldId='"+monitorField+"']")
 								 	 .on("keyup","[easyWinCompon='NumberComponent'][fieldId='"+monitorField+"']",function(){
 								//权重元素的weiyi9标识
 								var fieldId = $(this).attr("fieldId");
 								//需要触发的计算控件
 								var calBackFields = calElementRelate[fieldId];
 								if(!calBackFields){//没有需要触发的元素
 									return true;
 								}
 								//权重元素
 								var _this = $(this);
 								//权重元素的父类子表单
 								var subInDataTable = $(this).parents("[easyWinCompon='DataTable']");
 								//遍历需要触发的计算控件
 								$.each(calBackFields,function(index,relateTempId){
 									//计算控件所在区域
 									var _htmlScope = $("body");
 									//计算控件
 									var calComponent = $("body").find("[easyWinCompon='Monitor'][fieldId='"+relateTempId+"']");
 									//计算控件的父类子表单
 									var calSubInDataTable = $(calComponent).parents("[easyWinCompon='DataTable']");
 									
 									if(subInDataTable && subInDataTable.get(0)
 											&& calSubInDataTable && calSubInDataTable.get(0)){
 										//计算控件与权重元素在同一行子表单中
 										_htmlScope = $(_this).parents(".subTableTr");
 									}
 									//求和结果
 									var total = 0;
 									//计算控件的权重元素集合
 									var subArray = calNumWeight[relateTempId];
 									for(var key in subArray){//遍历计算控件的权重元素集合
 										if(key != 'in_array'){
 											//权重
 											var weight = subArray[key];
 											//权重元素求和信息
 											var subTotal = EasyWinForm.calTotalNum(key,_htmlScope);
 											//总的求和元素信息
 											total = accAdd(total,accMul(subTotal,weight));
 										}
 									}
 									//需要赋值的计算控件
 									var calComponent = $(_htmlScope).find("[easyWinCompon='Monitor'][fieldId='"+relateTempId+"']");
 									//是否只读
 									var isReadOnly = $(calComponent).data("componentData").isReadOnly;
 									//赋值
 									if (isReadOnly == 'false') {
 	                					 $(calComponent).addClass("defVal");
 	                                     $(calComponent).html(total);
 	                                 } else {
 	                                     $(calComponent).val(total);
 	                                 }
 									$(calComponent).trigger("transMonitorMoney",$(calComponent));

 								});
 								
 								$(this).trigger("transNumMoney",$(this));
 			    			});
 							
 							//计算控件中包含日期计算的，触发事件
 							$("body").bind("calDateSum",$("[easyWinCompon='Monitor'][monitorType='date'][fieldId='"+monitorField+"']"),function(event,DateObj){
 								//时间计算控件的唯一标识
								var fieldId = $(DateObj).attr("fieldId");
								var _this = $(DateObj);
								//时间计算控件在子表单的父类
								var subInDataTable = $(DateObj).parents("[easyWinCompon='DataTable']");
								//时间计算控件关联的计算控件
								var calBackFields = calElementRelate[fieldId];
								if(calBackFields){
									//遍历关联的计算控件
									$.each(calBackFields,function(index,relateTempId){
										//默认是全局的
										var _htmlScope = $("body");
										var calComponent = $("body").find("[easyWinCompon='Monitor'][fieldId='"+relateTempId+"']");
										var calSubInDataTable = $(calComponent).parents("[easyWinCompon='DataTable']");
										
										//计算区域为子表单，则区域为子表单
										if(subInDataTable && subInDataTable.get(0)
												&& calSubInDataTable && calSubInDataTable.get(0)){
											_htmlScope = $(_this).parents(".subTableTr");
										}
										//求和结果
										var total = 0;
										var subArray = calNumWeight[relateTempId];
										for(var key in subArray){
											if(key != 'in_array'){
												var weight = subArray[key];
												var subTotal = EasyWinForm.calTotalNum(key,_htmlScope);
												total = accAdd(total,accMul(subTotal,weight));
											}
										}
										var calComponent = $(_htmlScope).find("[easyWinCompon='Monitor'][fieldId='"+relateTempId+"']");
										var isReadOnly = $(calComponent).data("componentData").isReadOnly;
										if (isReadOnly == 'false') {
	 	                					 $(calComponent).addClass("defVal");
	 	                                     $(calComponent).html(total);
	 	                                 } else {
	 	                                     $(calComponent).val(total);
	 	                                 }
										 $(calComponent).trigger("transMonitorMoney",$(calComponent));
									});
								}
							})
                		 })
                	 }
                }
                if (required == 'true') {//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }
            }
            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId);

            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);
        } else if ("MoneyComponent" == componKey) {
        	var _html;
        	_html = $("<span style='width:80px' class='easywinView'><span>");
        	
        	 var moneyColumn = layoutDetail.moneyColumn;
        	 if(moneyColumn){
        		 moneyRelate[moneyColumn] = fieldId;
        		 
        		 $("body").bind("transNumMoney","[easyWinCompon='NumberComponent'][fieldId='"+moneyColumn+"']",function(event,DateObj){
    				var fieldId = $(DateObj).attr("fieldId");
    				var relateTempId = moneyRelate[fieldId];
    				if(relateTempId){
    					var subInDataTable = $(DateObj).parents("[easyWinCompon='DataTable']");
    					//默认是全局的
    					var _htmlScope = $("body");
    					var calComponent = $("body").find("[easyWinCompon='Monitor'][fieldId='"+relateTempId+"']");
    					var calSubInDataTable = $(calComponent).parents("[easyWinCompon='DataTable']");
    					
    					//计算区域为子表单，则区域为子表单
    					if(subInDataTable && subInDataTable.get(0)
    							&& calSubInDataTable && calSubInDataTable.get(0)){
    						_htmlScope = $(_this).parents(".subTableTr");
    					}
    					
    					var money = $(DateObj).val();
    					
    					var calComponent = $(_htmlScope).find("[easyWinCompon='MoneyComponent'][fieldId='"+relateTempId+"']");
    					if(money && !isNaN(money)){
    						$(calComponent).html(changeNumMoneyToChinese(money));
    					}else{
    						$(calComponent).html('');
    					}
    				}
    			})
	    			
    			$("body").bind("transMonitorMoney","[easyWinCompon='Monitor'][fieldId='"+moneyColumn+"']",function(event,DateObj){
    				var fieldId = $(DateObj).attr("fieldId");
    				var relateTempId = moneyRelate[fieldId];
    				if(relateTempId){
    					var subInDataTable = $(DateObj).parents("[easyWinCompon='DataTable']");
    					//默认是全局的
    					var _htmlScope = $("body");
    					var calComponent = $("body").find("[easyWinCompon='Monitor'][fieldId='"+relateTempId+"']");
    					var calSubInDataTable = $(calComponent).parents("[easyWinCompon='DataTable']");
    					
    					//计算区域为子表单，则区域为子表单
    					if(subInDataTable && subInDataTable.get(0)
    							&& calSubInDataTable && calSubInDataTable.get(0)){
    						_htmlScope = $(_this).parents(".subTableTr");
    					}
    					
    					var money = $(DateObj).val();
    					var calComponent = $(_htmlScope).find("[easyWinCompon='MoneyComponent'][fieldId='"+relateTempId+"']");
    					if(money && !isNaN(money)){
    						$(calComponent).html(changeNumMoneyToChinese(money));
    					}else{
    						$(calComponent).html('');
    					}
    				}
    			})
        	 }
        	 
        	_html.addClass("fieldReadOnly");
        	_html.attr("easyWinCompon", componKey);
        	_html.attr("fieldId", fieldId);
        	_html.data("componentData", layoutDetail);
        	$(easywinTag).before(_html);
        } else if ("Employee" == componKey) {

            var _html;
            if (isReadOnly == 'false') {//只读
                _html = $("<span style='width:80px' class='easywinView'><span>")
                _html.addClass("fieldReadOnly");

                var _spanUser = $("<span class='text tempUser tempShowData'></span>")
                var tempShowData = {};
                tempShowData.optionId = EasyWin.userInfo.id;
                tempShowData.content = EasyWin.userInfo.username;
                $(_spanUser).data("tempShowData", tempShowData);

                _html.append(_spanUser)

            } else {
                _html = $("<span class='myDiv'></span>");

                var isDefault = layoutDetail.isDefault;
                var isUnique = layoutDetail.isUnique;
                var isEdit = layoutDetail.isEdit;//是否可以编辑（默认可以）

                var canOpt = 1;//默认可以操作
                if (isUnique == 'true' && isDefault == 'true' && isEdit == 'false') {//人员单选
                    _html.addClass("fieldReadOnly");
                    var _spanUser = $("<span class='text tempUser tempShowData'></span>")
                    _spanUser.html(EasyWin.userInfo.username)
                    var tempShowData = {};
                    tempShowData.optionId = EasyWin.userInfo.id;
                    tempShowData.content = EasyWin.userInfo.username;
                    $(_spanUser).data("tempShowData", tempShowData);
                    _html.append(_spanUser)

                    canOpt = 0;
                }
                if (canOpt) {
                    _OptA = $("<a href='javascript:void(0)' class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5'>人员</a>");
                    _OptA.attr("title", "人员选择")
                    _OptA.attr("isUnique", isUnique)
                    _html.append(_OptA)

                    if (isUnique == 'true' && isDefault == 'true') {
                        var _spanUser = $("<span class='text tempUser tempShowData'></span>")
                        $(_spanUser).html(EasyWin.userInfo.username)
                        var tempShowData = {};
                        tempShowData.optionId = EasyWin.userInfo.id
                        tempShowData.content = EasyWin.userInfo.username;
                        $(_spanUser).data("tempShowData", tempShowData);

                        $(_OptA).before(_spanUser)
                    }
                }

                if (required == 'true') {//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }
            }
            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId);

            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);

        } else if ("Department" == componKey) {
            var _html;
            if (isReadOnly == 'false') {//只读
                _html = $("<span style='width:80px' class='easywinView'><span>")
                _html.addClass("fieldReadOnly");
            } else {
                _html = $("<span class='myDiv'></span>");

                var isDefault = layoutDetail.isDefault;
                var isUnique = layoutDetail.isUnique;
                var isEdit = layoutDetail.isEdit;//是否可以编辑（默认可以）

                var canOpt = 1;//默认可以操作
                if (isUnique == 'true' && isDefault == 'true' && isEdit == 'false') {//部门单选
                    _html.addClass("fieldReadOnly");
                    var _spanDep = $("<span class='text tempDep tempShowData'></span>")
                    _spanDep.html(EasyWin.userInfo.department.name)
                    var tempShowData = {};
                    tempShowData.optionId = EasyWin.userInfo.department.id;
                    tempShowData.content = EasyWin.userInfo.department.name;
                    $(_spanDep).data("tempShowData", tempShowData);

                    _html.append(_spanDep)
                    canOpt = 0;
                }
                if (canOpt) {
                    _OptA = $("<a href='javascript:void(0)' class='btn btn-primary btn-xs depSelect fa fa-plus margin-left-5'>部门</a>");
                    _OptA.attr("title", "部门选择")
                    _OptA.attr("isUnique", isUnique)
                    _html.append(_OptA)
                }


                if (required == 'true') {//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }
            }
            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId);

            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);
        } else if ("RelateFile" == componKey) {
            var _html;
            if (isReadOnly == 'false') {//只读
                _html = $("<span style='width:80px' class='easywinView'><span>")
                _html.addClass("fieldReadOnly");

                var result = EasyWinForm.util.constrUploadHtml(fieldId, layoutDetail.title);
                var _OptA = result.html;
                _html.append(_OptA);
                $(_OptA).find("#" + result.pickerDivId).parent().remove();
                result.pickerDivId = null;

                _html.data("upfileConf", result);
            } else {
                _html = $("<span class='myDiv'></span>");
                var isUnique = layoutDetail.isUnique;

                var result = EasyWinForm.util.constrUploadHtml(fieldId, layoutDetail.title);
                var _OptA = result.html;
                _html.append(_OptA);

                _html.data("upfileConf", result);

                if (required == 'true') {//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }
            }
            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId);

            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);
        } else if ("RelateItem" == componKey) {
            var _html;
            if (isReadOnly == 'false') {//只读
                _html = $("<span style='width:80px' class='easywinView'><span>")
                _html.addClass("fieldReadOnly");
            } else {
                _html = $("<span class='myDiv'></span>");
                var isUnique = layoutDetail.isUnique;

                _OptA = $("<a href='javascript:void(0)' class='btn btn-primary btn-xs relateModSelect fa fa-plus margin-left-5' busType='005'>项目</a>");
                _OptA.attr("title", "项目选择")
                _OptA.attr("isUnique", isUnique)
                _html.append(_OptA)
                if (required == 'true') {//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }
            }
            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId);

            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);
        } else if ("RelateCrm" == componKey) {
            var _html;
            if (isReadOnly == 'false') {//只读
                _html = $("<span style='width:80px' class='easywinView'><span>")
                _html.addClass("fieldReadOnly");
            } else {
                _html = $("<span class='myDiv'></span>");
                var isUnique = layoutDetail.isUnique;

                _OptA = $("<a href='javascript:void(0)' class='btn btn-primary btn-xs relateModSelect fa fa-plus margin-left-5' busType='012'>客户</a>");
                _OptA.attr("title", "客户选择")
                _OptA.attr("isUnique", isUnique)
                _html.append(_OptA)
                if (required == 'true') {//是必填
                    _html.addClass("check_js")
                    $(easywinTag).before(_spanRequire);
                }
            }
            _html.attr("easyWinCompon", componKey);
            _html.attr("fieldId", fieldId);

            _html.data("componentData", layoutDetail);

            $(easywinTag).before(_html);
        } else if ("RelateMod" == componKey) {
        	var _html;
        	if (isReadOnly == 'false') {//只读
        		_html = $("<span style='width:80px' class='easywinView'><span>")
        		_html.addClass("fieldReadOnly");
        	} else {
        		_html = $("<span class='myDiv'></span>");
        		var isUnique = layoutDetail.isUnique;
        		
        		var _OptA = $("<a href='javascript:void(0)' class='btn btn-primary btn-xs relateModSelect fa fa-plus margin-left-5'>模块</a>");
        		$(_OptA).attr("busType",layoutDetail.relateModType);
        		_OptA.attr("title", "关联模块")
        		_OptA.attr("isUnique", isUnique)
        		_html.append(_OptA)
        		if (required == 'true') {//是必填
        			_html.addClass("check_js")
        			$(easywinTag).before(_spanRequire);
        		}
        	}
        	_html.attr("easyWinCompon", componKey);
        	_html.attr("fieldId", fieldId);
        	
        	_html.data("componentData", layoutDetail);
        	
        	$(easywinTag).before(_html);

        } else if ('subItemTitle' == componKey) {
            var _itemTitleDiv = $("<span></span>");
            _itemTitleDiv.html(title);
            $(easywinTag).before(_itemTitleDiv)

        } else if ("DataTable" == componKey) {
        	
            var len = $(easywinTag).parents("td").length;
            if (len == 1) {
                $(easywinTag).parents("td").attr("cellpadding", "0");
                $(easywinTag).parents("td").attr("cellspacing", "0");
                $(easywinTag).parents("td").css("padding", "0px 0px");
                $(easywinTag).parents("td").css("margin", "0px 0px");
                $(easywinTag).parents("td").css("vertical-align", "top");
            }

            var _subTableDiv = $("<div class='myDiv' style='width:100%;min-height:130px;margin-bottom:5px'></div>")
            _subTableDiv.attr("tempId", tempId);
            _subTableDiv.attr("fieldId", fieldId);
            $(_subTableDiv).attr("easyWinCompon", componKey);
            $(_subTableDiv).attr("conf_name", title);
            
            $(_subTableDiv).attr("conf_relateTable", layoutDetail.relateTable);

            //头部
            var _tableHeadDiv = $("<div class='sub subTableHead' style='display:table;width:100%'></div>");
            var _headTr = $("<div style='display:table-row;'></div>");
            var _headTdMod = $("<div class='tdItem'></div>");
            //内容
            var _tableBodyDiv = $("<div class='sub subTableBody' style='display:table;width:100%;'></div>");
            var _bodyTr = $("<div style='display:table-row;border-top:0px' class='subTableTr'></div>");
            if(layoutDetail.relateTable && isReadOnly != 'false'){//有关联的数据时候
            	$(_bodyTr).attr("modTr","1");
            }
            var _bodyTdMod = $("<div class='tdItem'></div>");
            //底部操作区域
            var _tableTailDiv = $("<div class='sub subTableTail' style='display:table;width:100%;'></div>");
            var _tailTr = $("<div style='display:table-row;'></div>");
            var _tailTdMod = $("<div class='tdItem'></div>");
            var _optA = $('<a href="javascript:void(0)" opt-btn="optAddDataTable">添加一行数据</a>');
            $(_tailTdMod).append(_optA);
            $(_tailTr).append(_tailTdMod);
            $(_tableTailDiv).append(_tailTr);

            var sublayoutDetails = layoutDetail.layoutDetail;
            var effectIndex = 0;
            $.each(sublayoutDetails, function (index, sublayoutDetail) {

                var defineTag = $("<easyWin></easyWin>");
                defineTag.attr("tempId", sublayoutDetail.tempId);
                defineTag.attr("componKey", sublayoutDetail.componentKey);
                if (sublayoutDetail.componentKey == 'subItemTitle') {
                    effectIndex++;
                    var _headTd = $(_headTdMod).clone();
                    _headTd.html(defineTag)
                    $(_headTr).append(_headTd)
                } else {
                    var _bodyTd = $(_bodyTdMod).clone();
                    _bodyTd.html(defineTag)
                    $(_bodyTr).append(_bodyTd);

                }

            })
            $(_tableHeadDiv).append(_headTr);
            $(_tableBodyDiv).append(_bodyTr);
            $(_tableBodyDiv).data("sublayoutDetail", sublayoutDetails);


            $(_subTableDiv).append(_tableHeadDiv);
            $(_subTableDiv).append(_tableBodyDiv);
            if(layoutDetail.relateTable && isReadOnly != 'false'){
            	$(_subTableDiv).append(_tableTailDiv);
            }

            var width = (100 - 100 % effectIndex) / effectIndex;
            $(_subTableDiv).find(".tdItem").css("width", width + "%");
            $(_subTableDiv).find(".tdItem").css("word-break", "break-all");
            if (isReadOnly == 'true') {//只读
                var _headTdOpt = $("<div style='display:table-cell;height:30px;border-left: 1px solid #000;border-bottom: 1px solid #000;text-align:center;vertical-align: middle;'></div>");
                var optA = $("<a href='javascript:void(0)' class='fa fa-plus-circle green optAddDataTable fa-lg' style='width:20px;font-size:18px;text-align:center;vertical-align: middle;' title='添加'></a>")
                
                if(layoutDetail.relateTable && isReadOnly != 'false'){
                	optA = $("<a href='javascript:void(0)' class='glyphicon glyphicon-save green optAddDataTable fa-lg' style='width:20px;font-size:18px;text-align:center;vertical-align: middle;' title='数据导入'></a>")
                }
                _headTdOpt.html(optA);
                
                $(_subTableDiv).find(".subTableHead").find(".tdItem:last").after(_headTdOpt);

                $.each($(_subTableDiv).find(".subTableBody").find(".subTableTr"), function (subItemIndex, subItemObj) {
                    var _bodyTdOpt = $("<div style='display:table-cell;height:30px;border-left: 1px solid #000;border-bottom: 1px solid #000;text-align:center;vertical-align: middle;'></div>");
                    _bodyTdOpt.html("<a href='javascript:void(0)' class='fa fa-times-circle red optDelDataTable fa-lg' style='width:20px;font-size:18px;text-align:center;vertical-align: middle;'  title='删除'></a>");
                    $(subItemObj).find(".tdItem:last").after(_bodyTdOpt);

                })
            }


            _subTableDiv.data("componentData", layoutDetail);
            _subTableDiv.attr("subFormId", layoutDetail.subFormId);

            $(easywinTag).before(_subTableDiv)
            $(easywinTag).remove();

            $.each(sublayoutDetails, function (index, sublayoutDetail) {
                EasyWinForm.analyseComponent(sublayoutDetail, $(layoutHtml));
            })
        }

        var subLayout = layoutDetail.layoutDetail;
        if (subLayout && subLayout.length > 0) {
            if ('DataTable' != componKey && 'DateInterval' != componKey) {
                $.each(subLayout, function (index, obj) {
                    EasyWinForm.analyseComponent(obj, $(layoutHtml));
                })
            }
        }

        $(easywinTag).remove();
    }, initValue: function (formData) {
        var dataDetails = formData ? formData.dataDetails : null;
        if (dataDetails && dataDetails.length > 0) {
            $.each(dataDetails, function (dataDetailIndex, dataDetail) {
                //所属子表单数据信息
                var subForm = dataDetail.subForm;
                //存值控件
                var formField = dataDetail.formField;
                //选项信息
                var dataOptions = dataDetail.dataOptions;
                //组件名称
                var componentKey = formField.componentKey;
                //组件标识
                var fielId = formField.id;

                var _scopeObj = $("#form-info-div");
                if (subForm && subForm.id > 0) {//是子表单的数据
                    var dataIndex = dataDetail.dataIndex;
                    _scopeObj = $(_scopeObj).find("[easywincompon='DataTable'][subformid='" + subForm.id + "']");
                    
                    var subTableBody = $(_scopeObj).find(".subTableBody");
                    var subTableTr = $(subTableBody).find(".subTableTr:eq(0)").clone();
                    $(subTableTr).removeAttr("busTableId");
                    $(subTableTr).removeAttr("modTr");
                    $(subTableTr).find(".tdItem").html('');
                    
                    if(dataDetail.busTableId){
                    	$(subTableTr).attr("busTableId",dataDetail.busTableId);
                    }
                    
                    $(subTableBody).find(".subTableTr[modTr=1]").remove();
                    
                    var len = $(_scopeObj).find(".subTableBody").find(".subTableTr:eq(" + dataIndex + ")").length;
                    if (len == 0) {

                        var sublayoutDetails = $(subTableBody).data("sublayoutDetail");
                        var effectCpmponents = new Array();
                        var effectIndex = 0;
                        $.each(sublayoutDetails, function (index, sublayoutDetail) {
                            if (sublayoutDetail.componentKey != 'subItemTitle') {
                                effectCpmponents.push(sublayoutDetail);
                                var defineTag = $("<easyWin></easyWin>");
                                defineTag.attr("tempId", sublayoutDetail.tempId);
                                defineTag.attr("componKey", sublayoutDetail.componentKey);

                                $(subTableTr).find(".tdItem:eq(" + effectIndex + ")").append(defineTag);
                                effectIndex++;
                            }
                        })
                        $.each(sublayoutDetails, function (index, sublayoutDetail) {
                            EasyWinForm.analyseComponent(sublayoutDetail, $(subTableTr));
                        })

                        $(subTableBody).append(subTableTr)
                    }
                    _scopeObj = $(_scopeObj).find(".subTableTr:last");

                }
                var preCompone = $(_scopeObj).find("[easywincompon='" + componentKey + "'][fieldId='" + fielId + "']");
            	if(!preCompone || !preCompone.get(0)){
            		if(componentKey == 'DateInterval'){
            			preCompone = $(_scopeObj).find("[easywincompon='DateComponent'][fieldId='" + fielId + "']");
            		}
            		if(!preCompone || !preCompone.get(0)){
            			console.error("[easywincompon='" + componentKey + "'][fieldId='" + fielId + "']not found")
            			return true; 
            		}
            	}
            	
            	
                if ("CheckBox" == componentKey
                    || "RadioBox" == componentKey) {
                    var compone = $(_scopeObj).find("[easywincompon='" + componentKey + "'][fieldId='" + fielId + "']");
                    if (dataOptions && dataOptions.length > 0) {
                        $.each(dataOptions, function (optIndex, optObj) {
                            var optionId = optObj.optionId;
                            $(compone).find("input[value='" + optionId + "']").attr("checked", true);
                        })
                    }
                } else if ("Text" == componentKey
                    || "DateComponent" == componentKey
                    || "Monitor" == componentKey
                    || "SerialNum" == componentKey
                    || "NumberComponent" == componentKey) {
                	var compone = $(_scopeObj).find("[easywincompon='" + componentKey + "'][fieldId='" + fielId + "']");
                	var content = dataDetail.content;
                	$(compone).data("oldContent", content);
                	var isReadOnly = $(compone).data("componentData").isReadOnly;
                	//有关联数据的的默认只读
                	var conf_tableColumn = $(compone).data("componentData").tableColumn;
                	if (conf_tableColumn && $(compone).parents("[bustableid]").length > 0) {
                		isReadOnly = 'false';
                	}
                	
                	if (isReadOnly == 'false') {
                		$(compone).html(content);
                	} else {
                		$(compone).val(content);
                	}
                } else if ("MoneyComponent" == componentKey) {
                	 var compone = $(_scopeObj).find("[easywincompon='" + componentKey + "'][fieldId='" + fielId + "']");
                	 var content = dataDetail.content;
                     $(compone).data("oldContent", content);
                     $(compone).html(content);
                } else if ("RelateFile" == componentKey) {
                    if (dataOptions && dataOptions.length > 0) {
                        var compone = $(_scopeObj).find("[easywincompon='" + componentKey + "'][fieldId='" + fielId + "']");

                        var upfileConf = $(compone).data("upfileConf");

                        var pickerDivId = upfileConf.pickerDivId;
                        var picker = $("body").find("#" + pickerDivId);

                        var showDivId = upfileConf.showDivId;
                        var selectId = upfileConf.selectId;

                        if (picker && picker.get(0)) {//具有修改权限
                            //设置已经上传的
                            $.each(dataOptions, function (dataOptionIndex, dataOption) {

                                var divOuter = $('<div class="uploadify-queue-item"></div>');
                                $(divOuter).attr("id", showDivId + "_" + dataOption.optionId);

                                var cancelDiv = $('<div class="cancel"></div>');
                                var cancelOpt = $('<a href="javascript:void(0)" fileDone="1" >X</a>');
                                $(cancelOpt).attr("fileId", dataOption.optionId);
                                $(cancelDiv).append(cancelOpt);

                                $(divOuter).append(cancelDiv);

                                var fileNameSpan = $('<span class="fileName"></span>');
                                $(fileNameSpan).attr("title", dataOption.content);
                                $(fileNameSpan).html(dataOption.content);
                                $(divOuter).append(fileNameSpan);
                                var tipSpan = $('<span class="data"> - 完成</span>');

                                $(divOuter).append(tipSpan);

                                var proDiv = $('<div class="uploadify-progress"></div>');
                                var proBar = $('<div class="uploadify-progress-bar" style="width: 100%;"></div>');
                                $(proDiv).append(proBar);
                                $(divOuter).append(proDiv);

                                $("#" + showDivId).append(divOuter);


                                var option = $('<option selected="selected"></option>');
                                $(option).attr("value", dataOption.optionId)
                                $(option).html(dataOption.content);
                                $("#" + selectId).append(option);


                            })
                        } else {//只有查看权限
                            var _spanHtmlMod = $("<span class='text tempShowData'></span>")
                            //设置已经上传的
                            $.each(dataOptions, function (dataOptionIndex, dataOption) {
                                var _spanHtml = $(_spanHtmlMod).clone().html(dataOption.content);
                                $(_spanHtml).html(dataOption.content);
                                if (dataOptions.length > dataOptionIndex + 1) {
                                    $(_spanHtml).append("、");
                                }
                                $("#" + showDivId).append(_spanHtml);

                                var option = $('<option selected="selected"></option>');
                                $(option).attr("value", dataOption.optionId)
                                $(option).html(dataOption.content);
                                $("#" + selectId).append(option);
                            });
                        }
                    }
                } else if ("Employee" == componentKey
                    || "Department" == componentKey
                    || "RelateItem" == componentKey
                    || "RelateCrm" == componentKey
                    || "RelateMod" == componentKey) {
                    var compone = $(_scopeObj).find("[easywincompon='" + componentKey + "'][fieldId='" + fielId + "']");
                    if (dataOptions && dataOptions.length > 0) {
                        var _spanHtmlMod = $("<span class='text tempShowData'></span>")
                        if ("Employee" == componentKey) {
                            $(compone).find(".tempUser").remove();
                            _spanHtmlMod.addClass("tempUser")
                        } else if ("Department" == componentKey) {
                            $(compone).find(".tempDep").remove();
                            _spanHtmlMod.addClass("tempDep")
                        } else if ("RelateItem" == componentKey) {
                            $(compone).find(".tempItem").remove();
                            $(_spanHtmlMod).addClass("tempItem")
                            $(_spanHtmlMod).addClass("viewRelateModInfo")
                        } else if ("RelateCrm" == componentKey) {
                            $(compone).find(".tempCrm").remove();
                            $(_spanHtmlMod).addClass("tempCrm")
                            $(_spanHtmlMod).addClass("viewRelateModInfo")
                        } else if ("RelateMod" == componentKey) {
                        	var modType = compone.find(".relateModSelect").attr("busType");
                        	var clzName;
        	       			if(modType =='003'){
        	       				clzName = 'tempTask';
        	       			}else if(modType =='005'){
        	       				clzName = 'tempItem';
        	       			}else if(modType =='012'){
        	       				clzName = 'tempCrm';
        	       			}else if(modType =='04201'){
        	       				clzName = 'tempEvent';
        	       			}else if(modType =='04202'){
        	       				clzName = 'tempIssue';
        	       			}else if(modType =='04203'){
        	       				clzName = 'tempModify';
        	       			}else if(modType =='04204'){
        	       				clzName = 'tempRelease';
        	       			}
	        	       		$(compone).find("."+clzName).remove();
	                        $(_spanHtmlMod).addClass(clzName);
	                        $(_spanHtmlMod).addClass("viewRelateModInfo");
                        }
                        
                        var componentData =	$(compone).data("componentData");
                        var relateModType = componentData.relateModType;
                        $.each(dataOptions, function (optIndex, optObj) {
                            var optionId = optObj.optionId;
                            var content = optObj.content;
                            var _spanHtml = $(_spanHtmlMod).clone().html(content);
                            $(_spanHtml).attr("busId", optionId);

                            if ("RelateItem" == componentKey) {
                                $(_spanHtml).attr("busType", "005")
                            } else if ("RelateCrm" == componentKey) {
                                $(_spanHtml).attr("busType", "012")
                            } else {
                            	$(_spanHtml).attr("busType", relateModType);
                            }

                            var tempShowData = {};
                            tempShowData.optionId = optionId
                            tempShowData.content = content;

                            $(_spanHtml).data("tempShowData", tempShowData);
                            
                            var moreState = componentData.isUnique=='false';
                            if(moreState){
                            	$(_spanHtml).append("<br/>");
                            }
                            $(compone).prepend(_spanHtml);
                        })
                    }
                } else if ("Select" == componentKey) {
                    var compone = $(_scopeObj).find("[easywincompon='" + componentKey + "'][fieldId='" + fielId + "']");
                    var content = dataDetail.content;
                    var isReadOnly = $(compone).data("componentData").isReadOnly;
                    
                    //有关联数据的的默认只读
                    var conf_tableColumn = $(compone).data("componentData").tableColumn;
                    if (conf_tableColumn && $(compone).parents("[bustableid]").length > 0) {
                    	isReadOnly = 'false';
                    }
                    
                    if (isReadOnly == 'false') {
                        if (dataOptions && dataOptions.length > 0) {
                            $.each(dataOptions, function (optIndex, optObj) {
                                var optionId = optObj.optionId;
                                var content = optObj.content;
                                if(optionId && optionId>0){
                                	$(compone).append(optObj.content);
                                }
                                $(compone).data("oldContent", content);

                                var tempShowData = {};
                                tempShowData.optionId = optionId
                                tempShowData.content = content;

                                $(compone).data("tempShowData", tempShowData);

                            });
                        }
                    } else {
                        if (dataOptions && dataOptions.length > 0) {
                            $.each(dataOptions, function (optIndex, optObj) {
                                var optionId = optObj.optionId;
                                var content = optObj.content;

                                $(compone).find("option[value='" + optionId + "']").attr("selected", true);

                                $(compone).data("oldContent", optObj.content);
                                var tempShowData = {};
                                tempShowData.optionId = optionId
                                tempShowData.content = content;
                                $(compone).data("tempShowData", tempShowData);
                            });
                        }
                    }
                } else if ("DateInterval" == componentKey) {
                    var compone = $(_scopeObj).find("[easywincompon='DateComponent'][fieldId='" + fielId + "']");
                    var content = dataDetail.content;
                    var isReadOnly = $(compone).data("componentData").isReadOnly;

                    $(compone).data("oldContent", content);
                    if (isReadOnly == 'false') {
                        $(compone).html(content);
                    } else {
                        $(compone).val(content);
                    }
                } else if ("TextArea" == componentKey) {
                    var compone = $(_scopeObj).find("[easywincompon='" + componentKey + "'][fieldId='" + fielId + "']");
                    var content = dataDetail.dataText.content;
                    var isReadOnly = $(compone).data("componentData").isReadOnly;
                    //有关联数据的的默认只读
                    var conf_tableColumn = $(compone).data("componentData").tableColumn;
                    if (conf_tableColumn && $(compone).parents("[bustableid]").length > 0) {
                    	isReadOnly = 'false';
                    }
                    
                    $(compone).data("oldContent", content);

                    if (isReadOnly == 'false') {
                        if (content) {
                            //文本内容换行
                            content = content.replace(/(\r\n)|(\n)/g, '<br/>');
                            content = content.replace(/\s/g, '&nbsp;&nbsp;');
                        }
                        $(compone).html(content);
                    } else {
                        $(compone).val(content);
                    }
                }

            })
        }
    }, calTotalNum: function (fieldId,_htmlScope) {
		var total = 0;
		var calObjs = $(_htmlScope).find("[easyWinCompon='NumberComponent'][fieldId='"+fieldId+"']");
		if(calObjs && calObjs.get(0)){
			$.each(calObjs,function(subIndex,subObj){
				var isReadOnly = $(subObj).data("componentData").isReadOnly;
				var val = $(this).val();
				if(isReadOnly == 'false'){
					val = $(this).html();
				}
				if(val && !isNaN(val)){
					total = accAdd(total,val);
				}
			});
			
		}else{
			calObjs = $(_htmlScope).find("[easyWinCompon='Monitor'][monitortype='date'][fieldId='"+fieldId+"']");
			if(calObjs && calObjs.get(0)){
				$.each(calObjs,function(subIndex,subObj){
					var isReadOnly = $(subObj).data("componentData").isReadOnly;
					var val = $(this).val();
					if(isReadOnly == 'false'){
						val = $(this).html();
					}
					if(val && !isNaN(val)){
						total = accAdd(total,val);
					}
				});
			}
		}
		return total;
    }, calDateTimeNum: function (startDate, endDate, calTimeType) {
        var a = {};
        a.dateS = startDate;
        a.dateE = endDate;
        a.calTimeType = calTimeType;
        var hour = 0.0;
        $.ajax({
            type: "post",
            url: sid ? "/festMod/calDateTime?sid=" + sid + "&t=" + Math.random() : "/web/festMod/calDateTime?t=" + Math.random(),
            dataType: "json",
            async: !1,
            data: a,
            success: function (d) {
                if (d.status == 'y') {
                    hour = d.hour;
                }
            }
        })
        return hour;

    }, checkForAdd: function (param) {
        var a = 1;
        //验证名称
        var instanceName = $("#form-name").val();
        if (!instanceName || $.trim(instanceName).length == 0) {
            a = !1;
            $("#form-name").focus();
            layer.tips("请输入流程名称", $("#form-name"), {tips: 1});
            return a;
        }
        $("#form-info-div").data("flowInfo").flowName = instanceName;
        // 关注状态
        var attentionState = $("#attentionState").val();
        $("#form-info-div").data("flowInfo").attentionState = attentionState;
        $("#form-info-div").data("flowInfo").saveType = "add";//提交类型变更

        $("#spBtnDiv").data("spResult", {spState: 1, spIdea: '[下一步]'})

        if (a && param.dataStatus == 1) {
            a = EasyWinForm.submitCheck($("#contentBody"), 0, function () {
            });
        }
        if (a && param.dataStatus == 1) {
            a = EasyWinForm.checkloanInfo({
                callback: function (state) {
                    a = state;
                }
            })
        }
        return a;
    }, passYzmCheck: function () {
        var flowInfo = $("#form-info-div").data("flowInfo");
        var saveType = flowInfo.saveType;
        if (saveType == 'update' || saveType == 'back') {
            var spCheckCfg = $("#spBtnDiv").data("spCheckCfg");
            if (spCheckCfg.status == 'f') {
                showNotification("2", spCheckCfg.info);
                return !1
            } else if (spCheckCfg.status == 'f1') {//需要设定审批密码
                if (spCheckCfg.phoneStatus && spCheckCfg.phoneStatus == 'f2') {//需要设定手机号
                    EasyWinForm.bandMovePhone();
                    return !1
                }
                //判断验证码是否输入正确
                var passYzmObj = $("#spBtnDiv").data("passYzm");
                if (!passYzmObj || passYzmObj.status == 'f') {//验证码验证没通过了
                    EasyWinForm.sendYzmForSp();
                    return !1
                }
            }
        } else if (saveType == 'add') {//发起流程的时候

        }
        return 1;
    },submitMobCheck:function(e,precheckState, c){
    	//precheckState 0 预先 1正式
		//检测必填
		var a = !0;
		var checkInfo = {};
		checkInfo.code = 0;//默认审核成功
        e.find("[easywincompon]").not(".fieldReadOnly").each(function(f){
        	 var componentData = $(this).data("componentData");
        	 if(componentData && componentData.required=='true'){//是必填
        		 var  componentKey = componentData.componentKey;
        		 if('DataTable' == componentKey){
        			 return false;
        		 }else if('DateInterval' == componentKey){
        			var dateTimeS = $(this).find(".dateTimeS").val();
        			var dateTimeE = $(this).find(".dateTimeE").val();
        			if(!dateTimeS && !dateTimeE){
        				a = 0;
        				checkInfo.code = -1;
        				checkInfo.message = "请填写"+componentData.title+"！"
        				return false;
        			}else if(!dateTimeS){
        				a = 0;
        				checkInfo.code = -1;
        				checkInfo.message = "请填写"+componentData.start.title+"！"
        				return false;
        			}else if(!dateTimeE){
        				a = 0;
        				checkInfo.code = -1;
        				checkInfo.message = "请填写"+componentData.end.title+"！"
        				return false;
        			}
        		 }else if("Employee" == componentKey){
        			 var tempUsers = $(this).find(".tempUser");
        			 if(!tempUsers || !tempUsers.get(0)){
        				 a = 0;
        				 checkInfo.code = -1;
        				 checkInfo.message = "请填写"+componentData.title+"！"
        				 return false; 
        			 }
        		 }else if("Department" == componentKey){
        			 var tempDeps = $(this).find(".tempDep");
        			 if(!tempDeps || !tempDeps.get(0)){
        				 a = 0;
        				 checkInfo.code = -1;
        				 checkInfo.message = "请填写"+componentData.title+"！"
        				 return false; 
        			 }
        		 }else if("RelateFile" == componentKey){
        			 var upfileConf =$(this).data("upfileConf");
        			 var selectId = upfileConf.selectId;
        			 var options = $("body").find("#"+selectId).find("option");
        			 if(!options || !options.get(0)){
        				 a = 0;
        				 checkInfo.code = -1;
        				 checkInfo.message = "请填写"+componentData.title+"！"
        				 return false; 
        			 }
        		 }else if("RelateItem" == componentKey){
        			 var tempItems = $(this).find(".tempItem");
        			 if(!tempItems || !tempItems.get(0)){
        				 a = 0;
        				 checkInfo.code = -1;
        				 checkInfo.message = "请填写"+componentData.title+"！"
        				 return false; 
        			 }
        		 }else if("RelateCrm" == componentKey){
        			 var tempCrms = $(this).find(".tempCrm");
        			 if(!tempCrms || !tempCrms.get(0)){
        				 a = 0;
        				 checkInfo.code = -1;
        				 checkInfo.message = "请填写"+componentData.title+"！"
        				 return false; 
        			 }
        		 }else if("RelateMod" == componentKey){
        			 var modType = componentData.relateModType;
        			 var clzName;
        			 if(modType =='003'){
        				 clzName = 'tempTask';
        			 }else if(modType =='005'){
        				 clzName = 'tempItem';
        			 }else if(modType =='012'){
        				 clzName = 'tempCrm';
        			 }else if(modType =='04201'){
        				 clzName = 'tempEvent';
        			 }else if(modType =='04202'){
        				 clzName = 'tempIssue';
        			 }else if(modType =='04203'){
        				 clzName = 'tempModify';
        			 }else if(modType =='04204'){
        				 clzName = 'tempRelease';
        			 }
        			 var tempMods = $(this).find("."+clzName);
        			 if(!tempMods || !tempMods.get(0)){
        				 a = 0;
        				 checkInfo.code = -1;
        				 checkInfo.message = "请填写"+componentData.title+"！"
        				 return false; 
        			 }
        		 }else if("Select" == componentKey){
        			 var val = $(this).find("option:selected").val();
        			 if(!val || val == 0){
        				a = 0;
        				checkInfo.code = -1;
        				checkInfo.message = "请填写"+componentData.title+"！"
        				return false;
        			 }
        		 }else if("RadioBox" == componentKey 
        				 || "CheckBox" == componentKey){
        			 var selected = $(this).find("input:checked");
        			 if(!selected || selected.length==0){
        				 a = 0;
        				 checkInfo.code = -1;
        				 checkInfo.message = "请填写"+componentData.title+"！"
        				 return false; 
        			 }
        		 }else{
        			 var componeVal = $(this).val();
        			 if(!componeVal){
        				 a = 0;
        				 checkInfo.code = -1;
        				 checkInfo.message = "请填写"+componentData.title+"！"
        				 return false; 
        			 }
        		 }
        	 }
        	 
        	 if(componentData && componentData.componentKey=='SerialNum'){//是必填
        		 var componeVal = $(this).val();
        		 if(!componeVal){
        			 a = 0;
        			 checkInfo.code = -1;
        			 checkInfo.message = "请填写"+componentData.title+"！"
        			 return false; 
        		 }else{
        			 var serialNumObj = $(this).data("serialNumObj");
        			 if(serialNumObj){
        				 var serialFormat = serialNumObj.serialFormat;
        				 var serialType = serialNumObj.serialType;
        				 
        				 serialFormat = serialFormat.replace(/yyyy/,'{0}');
        				 serialFormat = serialFormat.replace(/MM/,'{1}');
        				 serialFormat = serialFormat.replace(/XX/,'{2}');
        					
        				 var leveOne= serialType.split("-")[0];
        				 var levelTwo= serialType.split("-")[1];
        				 var levelThree= serialType.split("-")[2];
        				 
        				 var startNum= componeVal.replace(/[^0-9]/ig,"");
        				 var leveOneNum = startNum.substring(0,leveOne);
        				 
        				 var levelTwoNum = startNum.substring(leveOne,Number(leveOne)+Number(levelTwo));
        				 var levelThreeNum = startNum.substring(Number(leveOne)+Number(levelTwo),startNum.length);
        				 
        				 var shoudNum = String.format(serialFormat,leveOneNum,levelTwoNum,levelThreeNum);
        				 if(shoudNum!=componeVal){
        					 a = 0;
    	        			 checkInfo.code = -1;
    	        			 checkInfo.message = "不满足编码格式"+serialNumObj.serialFormat+"！"
    	        			 return false; 
        				 }
        				 
        			 }
        				
        			 var result = EasyWinForm.optForm.checkSerialNum(componentData.serialNumId,componeVal);
        			 if(result.status!='y'){
        				 checkInfo.code = -1;
        				 checkInfo.message = result.info;
        				 return false; 
        			 }
        		 }
        	 }
        });
		c(checkInfo);
    }, submitCheck: function (e, precheckState, c) {
        //precheckState 0 预先 1正式
        //检测必填
        var a = !0;

        //驳回和回退不需要审批条件验证;spState:0终止；2转办；1下一步；-1回退
        var spState = $("#spBtnDiv").data("spResult").spState;
        if (!spState || spState != 1) {
            if (precheckState && spState ==1) {
                return EasyWinForm.passYzmCheck();
            } else {
                return a;
            }
        }
        e.find("[easywincompon]").not(".fieldReadOnly").each(function (f) {
            var componentData = $(this).data("componentData");
            if (componentData && componentData.required == 'true') {//是必填
                var componentKey = componentData.componentKey;
                if ('DataTable' == componentKey) {
                    return false;
                } else if ('DateInterval' == componentKey) {
                    var dateTimeS = $(this).find(".dateTimeS").val();
                    var dateTimeE = $(this).find(".dateTimeE").val();
                    if (!dateTimeS && !dateTimeE) {
                        a = 0;
                        scrollToLocation($("#contentBody"), $(this));
                        precheckState ? null : $(this).focus();//控件聚焦
                        layer.tips("请填写" + componentData.title + "！", $(this), {tips: 1});
                        return false;
                    } else if (!dateTimeS) {
                        a = 0;
                        scrollToLocation($("#contentBody"), $(this));
                        precheckState ? null : $(this).focus();//控件聚焦
                        layer.tips("请填写" + componentData.start.title + "！", $(this).find(".dateTimeS"), {tips: 1});
                        return false;
                    } else if (!dateTimeE) {
                        a = 0;
                        scrollToLocation($("#contentBody"), $(this));
                        precheckState ? null : $(this).focus();//控件聚焦
                        layer.tips("请填写" + componentData.end.title + "！", $(this).find(".dateTimeE"), {tips: 1});
                        return false;
                    }
                } else if ("Employee" == componentKey) {
                    var tempUsers = $(this).find(".tempUser");
                    if (!tempUsers || !tempUsers.get(0)) {
                        a = 0;
                        scrollToLocation($("#contentBody"), $(this));
                        precheckState ? null : $(this).focus();//控件聚焦
                        layer.tips("请填写" + componentData.title + "！", $(this), {tips: 1});
                        return false;
                    }
                } else if ("Department" == componentKey) {
                    var tempDeps = $(this).find(".tempDep");
                    if (!tempDeps || !tempDeps.get(0)) {
                        a = 0;
                        scrollToLocation($("#contentBody"), $(this));
                        precheckState ? null : $(this).focus();//控件聚焦
                        layer.tips("请填写" + componentData.title + "！", $(this), {tips: 1});
                        return false;
                    }
                } else if ("RelateFile" == componentKey) {
                    var upfileConf = $(this).data("upfileConf");
                    var selectId = upfileConf.selectId;
                    var options = $("body").find("#" + selectId).find("option");
                    if (!options || !options.get(0)) {
                        a = 0;
                        scrollToLocation($("#contentBody"), $(this));
                        precheckState ? null : $(this).focus();//控件聚焦
                        layer.tips("请填写" + componentData.title + "！", $(this), {tips: 1});
                        return false;
                    }
                } else if ("RelateItem" == componentKey) {
                    var tempItems = $(this).find(".tempItem");
                    if (!tempItems || !tempItems.get(0)) {
                        a = 0;
                        scrollToLocation($("#contentBody"), $(this));
                        precheckState ? null : $(this).focus();//控件聚焦
                        layer.tips("请填写" + componentData.title + "！", $(this), {tips: 1});
                        return false;
                    }
                } else if ("RelateCrm" == componentKey) {
                    var tempCrms = $(this).find(".tempCrm");
                    if (!tempCrms || !tempCrms.get(0)) {
                        a = 0;
                        scrollToLocation($("#contentBody"), $(this));
                        precheckState ? null : $(this).focus();//控件聚焦
                        layer.tips("请填写" + componentData.title + "！", $(this), {tips: 1});
                        return false;
                    }
                } else if ("RelateMod" == componentKey) {
                	
                	var modType = componentData.relateModType;
	       			var clzName;
	       			if(modType =='003'){
	       				clzName = 'tempTask';
	       			}else if(modType =='005'){
	       				clzName = 'tempItem';
	       			}else if(modType =='012'){
	       				clzName = 'tempCrm';
	       			}else if(modType =='04201'){
	       				clzName = 'tempEvent';
	       			}else if(modType =='04202'){
	       				clzName = 'tempIssue';
	       			}else if(modType =='04203'){
	       				clzName = 'tempModify';
	       			}else if(modType =='04204'){
	       				clzName = 'tempRelease';
	       			}
	       			var tempMods = $(this).find("."+clzName);
	       			if(!tempMods || !tempMods.get(0)){
	       				a = 0;
                		scrollToLocation($("#contentBody"), $(this));
                		precheckState ? null : $(this).focus();//控件聚焦
                		layer.tips("请填写" + componentData.title + "！", $(this), {tips: 1});
                		return false;
                	}
                } else if ("Select" == componentKey) {
                    var val = $(this).find("option:selected").val();
                    if (!val || val == 0) {
                        a = 0;
                        scrollToLocation($("#contentBody"), $(this));
                        precheckState ? null : $(this).focus();//控件聚焦
                        layer.tips("请填写" + componentData.title + "！", $(this), {tips: 1});
                        return false;
                    }
                } else if ("RadioBox" == componentKey
                    || "CheckBox" == componentKey) {
                    var selected = $(this).find("input:checked");
                    if (!selected || selected.length == 0) {
                        a = 0;
                        scrollToLocation($("#contentBody"), $(this));
                        precheckState ? null : $(this).focus();//控件聚焦
                        layer.tips("请填写" + componentData.title + "！", $(this), {tips: 1});
                        return false;
                    }
                } else {
                    var componeVal = $(this).val();
                    if (!componeVal) {
                        a = 0;
                        scrollToLocation($("#contentBody"), $(this));
                        precheckState ? null : $(this).focus();//控件聚焦
                        layer.tips("请填写" + componentData.title + "！", $(this), {tips: 1});
                        return false;
                    }
                }
                
            }
            if(componentData && componentData.componentKey=='SerialNum'){//是必填
            	var componeVal = $(this).val();
            	if(!componeVal){
            		a = 0;
            		scrollToParentLocation($("#contentBody"),$(this));
            		precheckState?null:$(this).focus();//控件聚焦
            		layer.tips("请填写"+componentData.title+"！",$(this),{tips:1});
            		return false; 
            	}else{
            		
            		var serialNumObj = $(this).data("serialNumObj");
            		if(serialNumObj){
            			var serialFormat = serialNumObj.serialFormat;
       				 var serialType = serialNumObj.serialType;
       				 
       				 serialFormat = serialFormat.replace(/yyyy/,'{0}');
       				 serialFormat = serialFormat.replace(/MM/,'{1}');
       				 serialFormat = serialFormat.replace(/XX/,'{2}');
       					
       				 var leveOne= serialType.split("-")[0];
       				 var levelTwo= serialType.split("-")[1];
       				 var levelThree= serialType.split("-")[2];
       				 
       				 var startNum= componeVal.replace(/[^0-9]/ig,"");
       				 var leveOneNum = startNum.substring(0,leveOne);
       				 var levelTwoNum = startNum.substring(leveOne,Number(leveOne)+Number(levelTwo));
       				 var levelThreeNum = startNum.substring(Number(leveOne)+Number(levelTwo),startNum.length);
       				 var shoudNum = String.format(serialFormat,leveOneNum,levelTwoNum,levelThreeNum);
	        			if(shoudNum!=componeVal){
	        				a = 0;
	        				scrollToLocation($("#contentBody"), $(this));
	        				precheckState?null:$(this).focus();//控件聚焦
	        				layer.tips("不满足编码格式\""+serialNumObj.serialFormat+"\"！",$(this),{tips:1});
	        				return false; 
	        			}
            			
            		}
            		
            		var result = EasyWinForm.optForm.checkSerialNum(componentData.serialNumId,componeVal);
            		if(result.status!='y'){
            			a = 0;
            			scrollToLocation($("#contentBody"), $(this));
            			precheckState?null:$(this).focus();//控件聚焦
            			layer.tips(result.info,$(this),{tips:1});
            			return false; 
            		}
            	}
            }
        });
        if (a) {
            //验证自由流程的步骤配置
            if (strIsNull(FLOWINFO.flowId)) {
                $("#sp-steps-div").css("display", "block");// 展开步骤配置
                $("#spStepTable .stepTr").each(function (i) {
                    if (strIsNull($(this).find("[name='spUser']").val())) {
                        a = !1;
                        layer.tips("请先配置步骤审批人！", $(this).find(".selectUser"), {tips: 1});
                        return false;
                    }
                });
            }
        }
        if (a && precheckState) {
            a = EasyWinForm.passYzmCheck();
        }

        return a;


    }, bandMovePhone: function () {

        window.top.layer.open({
            type: 2,
            //title: [title, 'font-size:18px;'],
            title: false,
            closeBtn: 0,
            area: ['550px', '350px'],
            fix: false, //不固定
            maxmin: false,
            scrollbar: false,
            content: ["/userInfo/updateUserMovePhonePage?sid=" + sid + "&pageSource=022", 'no'],
            success: function (layero, index) {
                var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
                $(iframeWin.document).find("#titleCloseBtn").on("click", function () {
                    window.top.layer.close(index);
                })
                $(iframeWin.document).find("#addBtn").on("click", function () {
                    var result = iframeWin.updateUserMovePhone();
                    if (result && result.status == 'y') {
                        var pageSource = result.pageSource;
                        if (pageSource && pageSource == '022') {//跳转到审批验证界面
                            $("#spBtnDiv").data("spCheckCfg").phoneStatus = null;//不需要在验证手机号是否存在
                            var url = "/workFlow/sendSpYzmPage?sid=" + sid;
                            iframeWin.location.replace(url);
                        }
                    }
                })
                $(iframeWin.document).find("#cancleBtn").on("click", function () {
                    window.top.layer.close(index);
                })
                $(iframeWin.document).find("#checkBtn").on("click", function () {//开始验证
                    var result = iframeWin.checkSpYzm();
                    if (result && result.status == 'y') {
                        //默认验证码没有输入正确
                        $("#spBtnDiv").data("passYzm", {"status": "y", "val": result.spYzm});
                        $("#submitFlowForm").trigger("click");
                    }
                })


            }
        });

    }, sendYzmForSp: function () {

        window.top.layer.open({
            type: 2,
            title: false,
            closeBtn: 0,
            area: ['550px', '350px'],
            fix: false, //不固定
            maxmin: false,
            scrollbar: false,
            content: ["/workFlow/sendSpYzmPage?sid=" + sid, 'no'],
            success: function (layero, index) {
                var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
                $(iframeWin.document).find("#titleCloseBtn").on("click", function () {
                    window.top.layer.close(index);
                })
                $(iframeWin.document).find("#cancleBtn").on("click", function () {
                    window.top.layer.close(index);
                })
                $(iframeWin.document).find("#checkBtn").on("click", function () {//开始验证
                    var result = iframeWin.checkSpYzm();
                    if (result && result.status == 'y') {
                        //默认验证码没有输入正确
                        $("#spBtnDiv").data("passYzm", {"status": "y", "val": result.spYzm});
                      //开始必填验证
                    	EasyWinForm.saveFormData({
                    		parentEl: $("#contentBody"),
                    		dataStatus: 1,
                    		isDel: 0,
                    		module: "freeform",
                    		callback: function (data) {
                    			var status = data.status;
                    			if (status == 'f') {
                    				showNotification(2, data.info);
                    			} else {
                    				var nextStepConfig = $("#contentBody").data("nextStepConfig");
                    				if(nextStepConfig && nextStepConfig.addTaskWay=='003'){
                    					$("#addBusTaskForSp").data("reloadState","1");
                    					$("#addBusTaskForSp").trigger("click");
                    				}else{
                    					openWindow.ReLoad();
                    					closeWin();
                    				}
                    			}
                    		}
                    	})
                    	
                    }
                })


            }
        });

    }, submitAssembleForm: function (e, d) {
        var c = e.parentEl,
            a = 1 == e.dataStatus ? "submit" : "temporary";
        if (null != c.get(0)) {
            var f = $("#form-info-div").data("form");
            if (null != f) {
                var flowInfo = $("#form-info-div").data("flowInfo")

                var b = f.formLayout.formModId,
                    h = flowInfo.instanceId,
                    f = f.formLayout.id;

                a = {
                    formData: {
                        instanceId: h,
                        form: {
                            id: b
                        },
                        formLayout: {
                            id: f
                        },
                        dataStatus: a,
                        flowId: flowInfo.flowId,//关联流程步骤设定主键
                        attentionState: flowInfo.attentionState//关注状态
                    },
                    isDel: e.isDel,
                    module: e.module,
                    clientSource: "pc"
                };
                b = EasyWinForm.assembleFormFieldData(c, d);
                a.formData.dataDetails = b;

                var n = EasyWinForm.assembleSubFormData(c);
                a.formData.subFormLogs = n;
                //流程实例化名称
                var instanceName = flowInfo.flowName;
                if (instanceName && $.trim(instanceName).length > 0) {
                    a.formData.instanceName = instanceName;
                }

                var spFlow = $("#form-info-div").data("spFlow");
                if (spFlow) {
                    a.formData.busId = spFlow.busId;
                    a.formData.busName = spFlow.busName;
                    a.formData.busType = spFlow.busType;
                    a.formData.stagedItemId = spFlow.stagedItemId;
                    a.formData.stagedItemName = spFlow.stagedItemName;
                    a.formData.creator = spFlow.creator;
                    a.formData.preStageItemId = spFlow.preStageItemId;

                    a.formData.stepId = spFlow.stepId;

                }
                var spResult = $("#spBtnDiv").data("spResult");
                if (spResult) {
                    a.formData.spState = spResult.spState;
                }
                //如果是自定义审批流程；则封装配置步骤信息
                if (strIsNull(flowInfo.flowId)) {
                    a.formData.spUser = $("#spStepTable [name='spUser']");
                    var spUser = new Array();
                    $("#spStepTable .stepTr").each(function (i) {
                        spUser.push($(this).find("[name='spUser']").val());
                    });
                    a.formData.spUser = spUser;
                }
                //如果是自定义审批流程；则封装配置步骤信息
                if (strIsNull(flowInfo.flowId)) {
                    a.formData.spUser = $("#spStepTable [name='spUser']");
                    var spUser = new Array();
                    $("#spStepTable .stepTr").each(function (i) {
                        spUser.push($(this).find("[name='spUser']").val());
                    });
                    a.formData.spUser = spUser;
                }
                
                var nextStepConfig = $("#contentBody").data("nextStepConfig");
                if(nextStepConfig){
                	//审批附件信息
                	var spFlowUpfiles = nextStepConfig.spFlowUpfiles;
                    if (spFlowUpfiles) {
                        a.formData.spFlowUpfiles = spFlowUpfiles;
                    }
                    //审批意见
                    var spIdea = nextStepConfig.spIdea;
                    if(spIdea){
                    	a.formData.spIdea = spIdea;
                    }
                    
                    //由于退回到选定步骤信息
                    a.formData.activitiTaskId = nextStepConfig.activitiTaskId;
                     
                	//下一步是否为结束
                	var stepType = nextStepConfig.stepType;
                	
                	var noticeUsers = nextStepConfig.noticeUsers;
            		if(noticeUsers){
            			a.formData.noticeUsers = noticeUsers;
            		}
            		
                	if(stepType && stepType=='end'){//不是结束步骤
                		var nextStepId = nextStepConfig.stepId;
                		a.formData.nextStepId = nextStepId ? nextStepId:0;
                	}else{
                		var nextStepUsers = nextStepConfig.nextStepUsers;
                		if(nextStepUsers){
                			a.formData.nextStepUsers = nextStepUsers;
                		}
                		
                		
                		//发送短信标识
                		var msgSendFlag = nextStepConfig.msgSendFlag;
                		a.formData.msgSendFlag = msgSendFlag ? msgSendFlag:"0";
                		
                		var nextStepId = nextStepConfig.stepId;
                		a.formData.nextStepId = nextStepId ? nextStepId:0;
                	}
                }

                var spCheckCfg = $("#spBtnDiv").data("spCheckCfg");
                if (spCheckCfg && spCheckCfg.status == 'f1') {//需要验证码
                    //判断验证码是否输入正确
                    var passYzmObj = $("#spBtnDiv").data("passYzm");
                    if (passYzmObj && passYzmObj.status == 'y') {//验证码验证通过了
                        a.formData.spYzm = passYzmObj.val;
                    }

                }
                return a
            }
        }

    }, assembleFormFieldData: function (e, d) {

        var c = [],
            a = 0,
            f = e.find("[easywincompon]");
        if (f) {
            f.each(function (b) {
                var componentData = $(this).data("componentData");
                var componentKey = componentData.componentKey;

                var dataDetail = {};
                dataDetail.formField = {};
                dataDetail.formField.title = componentData.title;

                dataDetail.formField.componentKey = componentData.componentKey;
                dataDetail.formField.id = componentData.fieldId;

                if ("Text" == componentKey
                    || "NumberComponent" == componentKey
                    || "SerialNum" == componentKey
                    || "Monitor" == componentKey) {
                    var oldContent = $(this).data("oldContent");

                    if ($(this).hasClass("defVal")) {
                        dataDetail.oldContent = "";
                    } else {
                        dataDetail.oldContent = oldContent ? oldContent : "";
                    }
                    if (!$(this).hasClass("fieldReadOnly")) {
                        dataDetail.content = $(this).val();
                    } else {
                    	var monitorType = $(this).attr("monitorType")
                    	if("Monitor" == componentKey && monitorType == 'number'){
                    		dataDetail.content = $(this).html();
                    	}else{
                    		dataDetail.content = oldContent;
                    	}
                    }
                    if ($(this).parents("[easywincompon='DataTable']").length > 0) {
                        var subFormId = $(this).parents("[easywincompon='DataTable']").data("subFormId");
                        dataDetail.subFormId = subFormId;
                        dataDetail.dataIndex = $(this).parents(".subTableTr").index();
                        
                        var conf_relateTable = $(this).parents("[easywincompon='DataTable']").attr("conf_relateTable");
                        if(conf_relateTable){
                        	dataDetail.busTableId = $(this).parents(".subTableTr").attr("busTableId");
                        	if(/consume/ig.test(conf_relateTable)){
                        		dataDetail.busTableType = "060";
                        	}
                        	
                        }
                        
                        
                    }
                    c.push(dataDetail);
                } else if ("MoneyComponent" == componentKey) {
                	 var oldContent = $(this).data("oldContent");
                     dataDetail.oldContent = oldContent ? oldContent : "";
                     dataDetail.content = $(this).html();
                     
                     if ($(this).parents("[easywincompon='DataTable']").length > 0) {
                         var subFormId = $(this).parents("[easywincompon='DataTable']").data("subFormId");
                         dataDetail.subFormId = subFormId;
                         dataDetail.dataIndex = $(this).parents(".subTableTr").index();
                         
                         var conf_relateTable = $(this).parents("[easywincompon='DataTable']").attr("conf_relateTable");
                         if(conf_relateTable){
                         	dataDetail.busTableId = $(this).parents(".subTableTr").attr("busTableId");
                         	if(/consume/ig.test(conf_relateTable)){
                         		dataDetail.busTableType = "060";
                         	}
                         	
                         }
                         
                         
                     }
                     c.push(dataDetail);
                } else if ("DateComponent" == componentKey) {

                    if ($(this).parents("[easywincompon='DateInterval']").length > 0) {
                        dataDetail.formField.componentKey = 'DateInterval';
                    }
                    var oldContent = $(this).data("oldContent");

                    if ($(this).hasClass("defVal")) {
                        dataDetail.oldContent = "";
                    } else {
                        dataDetail.oldContent = oldContent ? oldContent : "";
                    }

                    if (!$(this).hasClass("fieldReadOnly")) {
                        dataDetail.content = $(this).val();
                    } else {
                        dataDetail.content = oldContent;
                    }
                    if ($(this).parents("[easywincompon='DataTable']").length > 0) {
                        var subFormId = $(this).parents("[easywincompon='DataTable']").data("subFormId");
                        dataDetail.subFormId = subFormId;
                        dataDetail.dataIndex = $(this).parents(".subTableTr").index();
                        
                        var conf_relateTable = $(this).parents("[easywincompon='DataTable']").attr("conf_relateTable");
                        if(conf_relateTable){
                        	dataDetail.busTableId = $(this).parents(".subTableTr").attr("busTableId");
                        	if(/consume/ig.test(conf_relateTable)){
                        		dataDetail.busTableType = "060";
                        	}
                        	
                        }
                    }
                    c.push(dataDetail);

                } else if ("TextArea" == componentKey) {
                    var oldContent = $(this).data("oldContent");
                    dataDetail.oldContent = oldContent;
                    dataDetail.dataText = {}
                    if (!$(this).hasClass("fieldReadOnly")) {
                        dataDetail.dataText.content = $(this).val();
                    } else {
                        dataDetail.dataText.content = oldContent;
                    }
                    if ($(this).parents("[easywincompon='DataTable']").length > 0) {
                        var subFormId = $(this).parents("[easywincompon='DataTable']").data("subFormId");
                        dataDetail.subFormId = subFormId;
                        dataDetail.dataIndex = $(this).parents(".subTableTr").index();

                        var conf_relateTable = $(this).parents("[easywincompon='DataTable']").attr("conf_relateTable");
                        if(conf_relateTable){
                        	dataDetail.busTableId = $(this).parents(".subTableTr").attr("busTableId");
                        	if(/consume/ig.test(conf_relateTable)){
                        		dataDetail.busTableType = "060";
                        	}
                        	
                        }
                    }
                    c.push(dataDetail);
                } else if ("Select" == componentKey) {
                    var oldContent = $(this).data("oldContent");

                    dataDetail.oldContent = oldContent ? oldContent : "";

                    if (!$(this).hasClass("fieldReadOnly")) {
                        var dataOptions = [];
                        var dataOption = {};
                        dataOption.optionId = $(this).find("select").find("option:selected").val();
                        dataOption.content = $(this).find("select").find("option:selected").text();

                        dataOptions.push(dataOption);

                        dataDetail.dataOptions = dataOptions;
                        
                    } else {
                        var dataOptions = [];
                        var dataOption = $(this).data("tempShowData");
                        if(dataOption){
                        	dataOptions.push(dataOption);
                        }
                        dataDetail.dataOptions = dataOptions;
                    }
                    if ($(this).parents("[easywincompon='DataTable']").length > 0) {
                        var subFormId = $(this).parents("[easywincompon='DataTable']").data("subFormId");
                        dataDetail.subFormId = subFormId;
                        dataDetail.dataIndex = $(this).parents(".subTableTr").index();
                        
                        var conf_relateTable = $(this).parents("[easywincompon='DataTable']").attr("conf_relateTable");
                        if(conf_relateTable){
                        	dataDetail.busTableId = $(this).parents(".subTableTr").attr("busTableId");
                        	if(/consume/ig.test(conf_relateTable)){
                        		dataDetail.busTableType = "060";
                        	}
                        	
                        }
                    }
                    c.push(dataDetail);
                } else if ("RadioBox" == componentKey || "CheckBox" == componentKey) {
                    var selected = $(this).find("input:checked");
                    var dataOptions = [];
                    if (selected && selected.length > 0) {
                        $.each(selected, function (optIndex, optObj) {
                            var optData = $(optObj).data("componentData");
                            var dataOption = {};
                            dataOption.optionId = optData.fieldId
                            dataOption.content = optData.name;

                            dataOptions.push(dataOption);
                        })
                    }
                    dataDetail.dataOptions = dataOptions;

                    if ($(this).parents("[easywincompon='DataTable']").length > 0) {
                        var subFormId = $(this).parents("[easywincompon='DataTable']").data("subFormId");
                        dataDetail.subFormId = subFormId;
                        dataDetail.dataIndex = $(this).parents(".subTableTr").index();
                        
                        var conf_relateTable = $(this).parents("[easywincompon='DataTable']").attr("conf_relateTable");
                        if(conf_relateTable){
                        	dataDetail.busTableId = $(this).parents(".subTableTr").attr("busTableId");
                        	if(/consume/ig.test(conf_relateTable)){
                        		dataDetail.busTableType = "060";
                        	}
                        	
                        }
                    }
                    c.push(dataDetail);
                } else if ("RelateFile" == componentKey) {
                    var upfileConf = $(this).data("upfileConf");
                    var dataOptions = [];
                    if (upfileConf) {
                        var selectId = upfileConf.selectId;
                        var selectOptions = $("#" + selectId).find("option");
                        if (selectOptions && selectOptions.get(0)) {
                            $.each(selectOptions, function (optIndex, option) {
                                var dataOption = {};
                                dataOption.optionId = $(option).val()
                                dataOption.content = $(option).text();
                                dataOptions.push(dataOption);
                            })
                        }
                    }
                    dataDetail.dataOptions = dataOptions;

                    if ($(this).parents("[easywincompon='DataTable']").length > 0) {
                        var subFormId = $(this).parents("[easywincompon='DataTable']").data("subFormId");
                        dataDetail.subFormId = subFormId;
                        dataDetail.dataIndex = $(this).parents(".subTableTr").index();
                        
                        var conf_relateTable = $(this).parents("[easywincompon='DataTable']").attr("conf_relateTable");
                        if(conf_relateTable){
                        	dataDetail.busTableId = $(this).parents(".subTableTr").attr("busTableId");
                        	if(/consume/ig.test(conf_relateTable)){
                        		dataDetail.busTableType = "060";
                        	}
                        	
                        }
                    }
                    c.push(dataDetail);

                } else if ("Employee" == componentKey
                    || "Department" == componentKey
                    || "RelateItem" == componentKey
                    || "RelateCrm" == componentKey
                    || "RelateMod" == componentKey) {
                    var tempShowDatas = $(this).find(".tempShowData");
                    var dataOptions = [];
                    if (tempShowDatas && tempShowDatas.length > 0) {
                        $.each(tempShowDatas, function (optIndex, optObj) {
                            var tempShowData = $(optObj).data("tempShowData");
                            var dataOption = {};
                            dataOption.optionId = tempShowData.optionId
                            dataOption.content = tempShowData.content;
                            if("RelateMod" == componentKey){
                            	dataOption.dataBustype = componentData.relateModType;
                            }
                            dataOptions.push(dataOption);
                        })
                    }
                    dataDetail.dataOptions = dataOptions;
                    

                    if ($(this).parents("[easywincompon='DataTable']").length > 0) {
                        var subFormId = $(this).parents("[easywincompon='DataTable']").data("subFormId");
                        dataDetail.subFormId = subFormId;
                        dataDetail.dataIndex = $(this).parents(".subTableTr").index();
                        
                        var conf_relateTable = $(this).parents("[easywincompon='DataTable']").attr("conf_relateTable");
                        if(conf_relateTable){
                        	dataDetail.busTableId = $(this).parents(".subTableTr").attr("busTableId");
                        	if(/consume/ig.test(conf_relateTable)){
                        		dataDetail.busTableType = "060";
                        	}
                        	
                        }
                    }
                    c.push(dataDetail);
                }
            });
        }

        return c;
    }, constrDataForShow: function (e) {
    	
    	var c = [],
    	a = 0,
    	f = e.find("[easywincompon]");
    	if (f) {
    		f.each(function (b) {
    			var componentData = $(this).data("componentData");
    			var componentKey = componentData.componentKey;
    			
    			var dataDetail = {};
    			dataDetail.title = componentData.title;
    			
    			if ("Text" == componentKey
    					|| "NumberComponent" == componentKey
    					|| "SerialNum" == componentKey
    					|| "Monitor" == componentKey) {
    				var oldContent = $(this).data("oldContent");
    				if (!$(this).hasClass("fieldReadOnly")) {
    					dataDetail.content = $(this).val();
    				} else {
    					dataDetail.content = oldContent;
    				}
    				if(dataDetail.content){
    					c.push(dataDetail);
    				}
    			} else if ("DateComponent" == componentKey) {
    				
    				var oldContent = $(this).data("oldContent");
    				if (!$(this).hasClass("fieldReadOnly")) {
    					dataDetail.content = $(this).val();
    				} else {
    					dataDetail.content = oldContent;
    				}
    				if(dataDetail.content){
    					c.push(dataDetail);
    				}
    				
    			} else if ("TextArea" == componentKey) {
    				var oldContent = $(this).data("oldContent");
    				if (!$(this).hasClass("fieldReadOnly")) {
    					dataDetail.content = $(this).val();
    				} else {
    					dataDetail.content = oldContent;
    				}
    				if(dataDetail.content){
    					c.push(dataDetail);
    				}
    			} else if ("Select" == componentKey) {
    				var oldContent = $(this).data("oldContent");
    				if (!$(this).hasClass("fieldReadOnly")) {
    					dataDetail.content = $(this).find("select").find("option:selected").text();
    				} else {
    					var dataOptions = [];
    					var dataOption = $(this).data("tempShowData");
    					if(dataOption && dataOption.id>0){
    						dataDetail.content = dataOption.content;
    					}
    				}
    				if(dataDetail.content){
    					c.push(dataDetail);
    				}
    			} else if ("RadioBox" == componentKey || "CheckBox" == componentKey) {
    				var selected = $(this).find("input:checked");
    				var content = "";
    				if (selected && selected.length > 0) {
    					$.each(selected, function (optIndex, optObj) {
    						var optData = $(optObj).data("componentData");
    						content = content+","+optData.name;
    					})
    					content = content.substring(1);
    				}
    				dataDetail.content = content;
    				if(dataDetail.content){
    					c.push(dataDetail);
    				}
    			} else if ("RelateFile" == componentKey) {
    				var upfileConf = $(this).data("upfileConf");
    				var content = "";
    				if (upfileConf) {
    					var selectId = upfileConf.selectId;
    					var selectOptions = $("#" + selectId).find("option");
    					if (selectOptions && selectOptions.get(0)) {
    						$.each(selectOptions, function (optIndex, option) {
    							content = content+","+$(option).text();
    						})
    						content = content.substring(1);
    					}
    				}
    				dataDetail.content = content;
    				if(dataDetail.content){
    					c.push(dataDetail);
    				}
    				
    			} else if ("Employee" == componentKey
    					|| "Department" == componentKey
    					|| "RelateItem" == componentKey
    					|| "RelateCrm" == componentKey
    					|| "RelateMod" == componentKey) {
    				var tempShowDatas = $(this).find(".tempShowData");
    				var content = "";
    				if (tempShowDatas && tempShowDatas.length > 0) {
    					$.each(tempShowDatas, function (optIndex, optObj) {
    						var tempShowData = $(optObj).data("tempShowData");
    						content = content+","+ tempShowData.content;
    					})
    					content = content.substring(1);
    				}
    				dataDetail.content = content;
    				if(dataDetail.content){
    					c.push(dataDetail);
    				}
    			}
    		});
    	}
    	return c;

    }, assembleSubFormData: function (e) {
        var c = [],
            a = 0,
            f = e.find("[easywincompon='DataTable']");
        if (f) {
            f.each(function (b) {
                var subFormId = $(this).attr("subformId");
                var FormDataOption = {}
                FormDataOption.subFormId = subFormId;
                c.push(FormDataOption)
            });
        }
        return c;
    },spFlowPage: function (spState,callback) {//取得审批意见信息
    	window.top.layer.open({
			  type: 2,
			  title:false,
			  closeBtn:0,
			  area: ['550px', '400px'],
			  fix: true, //不固定
			  maxmin: false,
			  move: false,
			  scrollbar:false,
			  content: ['/workFlow/spFlowIdeaPage?sid='+sid,'no'],//跳转到提交步骤配置界面
			  btn: ['确定','取消'],
			  yes: function(index, layero){
				  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				  var nextStepConfig = iframeWin.returnStepConfig();
				  if(nextStepConfig){
					  window.top.layer.close(index);
					  callback(nextStepConfig);
					  return true;
				  }
				  return false;
			  },success: function(layero,index){ 
				  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				  iframeWin.initSpFlowIdea(spState,window.document,window);
			  }
		});
    },spflowNextStep: function (callback) {//取得下一步骤信息
    	var params={"instanceId":FLOWINFO.instanceId,"sid":sid};
		var url = "/workFlow/ajaxSpFlowNextStep";
		postUrl(url,params,function(data){
			if(data.status=='f'){
				showNotification(2,data.info);
			}else if(data.stepInfo.stepType == 'huiqianing'){
    			showNotification(2,"会签完成后，才能继续提交！");
			}else{
				var stepInfo = data.stepInfo;
				//选择下一步骤
				EasyWinForm.SpFlowNextStepConf(stepInfo,function(nextStepConfig){
					callback(nextStepConfig,stepInfo);
				});
			}
		});
    }, SpFlowNextStepConf: function (stepInfo,callback) {
    	window.top.layer.open({
			  type: 2,
			  title:false,
			  closeBtn:0,
			  area: ['550px', '480px'],
			  fix: true, //不固定
			  maxmin: false,
			  move: false,
			  scrollbar:false,
			  content: ['/workFlow/spFlowNextStepPage?sid='+sid,'no'],//跳转到提交步骤配置界面
			  btn: ['确定','取消'],
			  yes: function(index, layero){
				  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				  var nextStepConfig = iframeWin.returnStepConfig();
				  if(nextStepConfig){
					  window.top.layer.close(index);
					  callback(nextStepConfig);
					  return true;
				  }
				  return false;
			  },success: function(layero,index){ 
				  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				  iframeWin.initSpFlowNextStep(stepInfo,$(window.document),window);
			  }
		});
    }, saveFormData: function (e) {
        var d = e.parentEl,
            c = e.callback;
        if (null != d.get(0)) {
            var flowInfo = $("#form-info-div").data("flowInfo");
            var saveType = flowInfo.saveType;

            ((1 == e.dataStatus && EasyWinForm.submitCheck(d, 1, c)) || 0 == e.dataStatus)
            && (e = EasyWinForm.submitAssembleForm(e),
                $.ajax({
                    type: "POST",
                    dataType: "text",
                    url: "/workFlow/addFormData?sid=" + sid,
                    data: {"formDataStr": JSON.stringify(e), "saveType": saveType},
                    beforeSend: function () {
                        var submitState = $(d).data("submitState");
                        if (submitState && submitState.state == 1) {
                            var b = {"status": 'f', "info": "不能重复提交"};
                            c(b)
                            return false;
                        } else {
                            $(d).data("submitState", {"state": "1"});
                            if (saveType == 'update' || saveType == 'back') {//是审批动作
                                $("#submitFlowForm").addClass("btn-default");
                                $("#submitFlowForm").removeClass("btn-success");
                            }
                        }
                    }, success: function (b) {
                        //b.formData && (f.dataId = b.formData.id);
                        $(d).data("submitState", {"state": "0"});
                        if (saveType == 'update' || saveType == 'back') {//是审批动作
                            $("#submitFlowForm").removeClass("btn-default");
                            $("#submitFlowForm").addClass("btn-success");
                        }
                        c(JSON.parse(b))
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        $(d).data("submitState", {"state": "0"});
                        if (saveType == 'update' || saveType == 'back') {//是审批动作
                            $("#submitFlowForm").removeClass("btn-default");
                            $("#submitFlowForm").addClass("btn-success");
                        }

                        var b = {"status": 'f', "info": "系统错误，请联系管理人员"};
                        c(b)
                    }

                }))
        }
    }
};

EasyWinForm.optForm = {
			constrSerialNum:function(serialNumId,callback){//构建序列号
				var url = "/serialNum/constrSerialNum";
				var param = {
						"sid":EasyWin.sid,
						"serialNumId":serialNumId,
						"busId":FLOWINFO.instanceId,
						"busType":"022"
				}
				postUrl(url,param,function(data){
					if(data.status=='y'){
						callback(data)
					}else{
						showNotification(2, data.info);
					}
				});
			},checkSerialNum:function(serialNumId,serialNum){//序列号验重
				var result= null;
				$.ajax({
					 url:"/serialNum/checkSerialNum",
				     type:"POST",
				     data:{
							"sid":EasyWin.sid,
							"serialNumId":serialNumId,
							"serialNum":serialNum,
							"busId":FLOWINFO.instanceId,
							"busType":"022"
					},
				     dataType:"json",
				     async:false,
				     success:function(data){
				    	 result = data;
				     }
				}); 
				return result; 
			},constrContent:function(callback,spId){
				var showDatas = EasyWinForm.constrDataForShow($("#contentBody"));
				//全局公用在comon.js中
				selectDataForTask(showDatas,function(content,fileLists){
					callback(content,fileLists);
				},spId)
			}
}

//工具包
EasyWinForm.util = {
    fileIndex: 0,
    constrUploadHtml: function (fieldId, discribe) {
        EasyWinForm.util.fileIndex = EasyWinForm.util.fileIndex + 1;
        var result = {
            showDivId: "thelistrelateFile_id_" + fieldId + "_" + EasyWinForm.util.fileIndex,
            pickerDivId: "pickerrelateFile_id_" + fieldId + "_" + EasyWinForm.util.fileIndex,
            selectId: "relateFile_id_" + fieldId + "_" + EasyWinForm.util.fileIndex
        };

        //显示的区域包一层
        var listAllDiv = $('<div style="clear:both;width: 300px" class="wu-example"></div>');
        //显示历史数据
        var listShowFileDiv = $('<div class="uploader-list"></div>');
        $(listShowFileDiv).attr("id", result.showDivId);
        $(listAllDiv).append(listShowFileDiv);

        //显示按钮
        var btnIncludeDiv = $('<div class="btns btn-sm"></div>');
        var btnDiv = $('<div>选择文件</div>');
        if (discribe) {
            $(btnDiv).html(discribe);
        }
        $(btnDiv).attr("id", result.pickerDivId);
        $(btnIncludeDiv).append(btnDiv);
        $(listAllDiv).append(btnIncludeDiv);

        //存放隐形数据
        var selectDiv = $('<div style="float: left; width: 250px;display:none"></div>');
        var selectObj = $('<select multiple="multiple" moreselect="true"></select>');
        $(selectObj).attr("id", result.selectId);
        $(selectDiv).append(selectObj);
        $(listAllDiv).append(selectDiv);

        result.html = $(listAllDiv)
        return result;
    }
}
EasyWinForm.initLoanInfo = function (param) {
    //不需要验证借款
    $("#formInfo").data("loanCheckState", "0");
    var busId = param.busId;
    var loanSpInsId = param.loanSpInsId;
    var busType = param.busType;
    if(busType === '029' || busType === '035'){//借款计划
    	EasyWinForm.finacalObj.loadFinacalInfo(busType,busId,loanSpInsId,param);
    }else if(param.busType == '030' || param.busType == '031'){//借款
    	EasyWinForm.finacalObj.loadFinacalInfo(busType,busId,loanSpInsId,param);
    }else if(param.busType == '03401' || param.busType == '03402'){//汇报
    	EasyWinForm.finacalObj.loadFinacalInfo(busType,busId,loanSpInsId,param);
    }else if(param.busType == '03101' || param.busType == '03102'){//报销
    	EasyWinForm.finacalObj.loadFinacalInfo(busType,busId,loanSpInsId,param);
    }
    
}
//加载费用管理流程数据
EasyWinForm.finacalObj = {
		loadFinacalInfo:function(busType,busId,spInsId,param){
			var saveType = param.saveType;
			getSelfJSON("/financial/queryLoanBaseInfo", 
					{"busType":busType,
					"busId": busId,
					"spInsId":spInsId, 
					"sid": sid}, function (data) {
						if(data.status=='y'){
							var loanApply = data.loanApply
		                    $("#formInfo").data("loanApply", loanApply);
		                    if(!loanApply){
		                    	return;
		                    }
							EasyWinForm.finacalObj.showFinacalInfo(busType,busId,spInsId,data,saveType)
						}else{
							if(busType == '030' || busType == '031'){//借款
								 $("#formInfo").data("loanCheckState", "-1");
							}
						}
				
			});
		},showFinacalInfo:function(busType,busId,spInsId,data,saveType){//展示借款系列信息
			var loanApply = data.loanApply;
			if(busType === '029' || busType === '035'){//借款计划
			}else if(busType == '030' || busType == '031'){//借款
				 if (saveType == 'add') {
					var isBusinessTrip = loanApply.isBusinessTrip;
                    if (isBusinessTrip == 1) {//是差旅
                        //需要验证借款
                        $("#formInfo").data("loanCheckState", "1");
                        $("#formInfo").data("loanFieldId", data.loanFieldId);
                    }
				 }
			}else if(busType == '03401' || busType == '03402'){//汇报
			}else if(busType == '03101' || busType == '03102'){//报销
			}
			if(loanApply.isBusinessTrip === -1){
				return;
			}
			
			
			var tableHtml = $(EasyWinForm.finacalObj.htmlMod());
			
			//出差计划
			var lonApplyTd = $(tableHtml).find("tr:eq(0)").find("td:eq(1)");
			var lonApplyFlowName = $('<span class="blue loanApply2View" title="点击查看详情"></a>');
			$(lonApplyFlowName).html(loanApply.flowName);
			$(lonApplyTd).html($(lonApplyFlowName));
			
			//出差限额
			var loanApplyTopTd = $(tableHtml).find("tr:eq(1)").find("td:eq(1)");
			var allowedQuota = loanApply.allowedQuota;
			if(loanApply.status===4){
				allowedQuota = allowedQuota?allowedQuota:0;
				$(loanApplyTopTd).html(toDecimal2(allowedQuota)+"元")
			}else{
				$(loanApplyTopTd).html('--')
			}
			//已用额度
			var loanTd = $(tableHtml).find("tr:eq(1)").find("td:eq(3)");
			var listLoan = loanApply.listLoan;
			
			var loanTotal = 0;
			if(listLoan && listLoan[0]){
				$.each(listLoan,function(index,loan){
					if(loan.status==4){
						loanTotal = Number(loanTotal) + Number(loan.loanMoney);
					}
				})
			}
			
			var loanUsed = loanApply.loanFeeTotal;
			loanUsed = loanUsed?loanUsed:0;
			if(loanUsed>0){
				var loanAll = $('<a href="javascript:void(0)" class="blue loanAll"></a>');
				$(loanAll).data("listLoan",listLoan);
				$(loanAll).html(toDecimal2(loanUsed)+"元");
				$(loanTd).html($(loanAll));
			}else{
				$(loanTd).html(toDecimal2(loanUsed)+"元");
			}
			
			//总的未销账
			var loanAllNoTd = $(tableHtml).find("tr:eq(2)").find("td:eq(1)");
			var totalBorrow = loanApply.totalBorrow;
            var totalOff = loanApply.totalOff;
            if (totalBorrow > totalOff) {
            	var loanAllNo = totalBorrow - totalOff;
            	$(loanAllNoTd).html(toDecimal2(loanAllNo)+"元")
            }else{
            	$(loanAllNoTd).html(toDecimal2(0)+"元")
            }
             
			//累计销账
			var loanOffTd = $(tableHtml).find("tr:eq(2)").find("td:eq(3)");
			var listLoanOff = loanApply.listLoanOff;
			var loanOff = 0;
			var curloanOff;
			if(listLoanOff && listLoanOff[0]){
				$.each(listLoanOff,function(index,loanOffObj){
					if(loanOffObj.status==4){
						var loanOffBalance = loanOffObj.loanOffBalance;
						loanOffBalance = loanOffBalance?loanOffBalance:0;
						loanOff = Number(loanOff) + Number(loanOffBalance);
					}
					if(loanOffObj.instanceId == spInsId){
						curloanOff = loanOffObj;
					}
				})
			}
			
			if(loanOff>0){
				var loanOffAll = $('<a href="javascript:void(0)" class="blue loanOffAll"></a>');
				$(loanOffAll).data("listLoanOff",listLoanOff);
				$(loanOffAll).html(toDecimal2(loanOff)+"元");
				$(loanOffTd).html($(loanOffAll));
			}else{
				$(loanOffTd).html(toDecimal2(loanOff)+"元");
			}
			
			if(curloanOff && busType == '03101'){//出差报销
				
				var repTr = $("<tr></tr>");
				var repTdPre = $("<td>汇报记录</td>");
				$(repTr).append(repTdPre);
				var repTdRep = $('<td colspan="3"></td>');
				var repA = $('<span class="blue loanRep2View" title="点击查看详情"></a>');
				$(repA).append(curloanOff.loanReportName);
				$(repTdRep).append($(repA));
				$(repTr).append($(repTdRep));
				
				$(tableHtml).find("tr:eq(1)").before($(repTr));
				$("#formInfo").data("loanOff",curloanOff);
			}
			
			$("#formInfo").html($(tableHtml));
		},htmlMod:function(){
			var html = '<table class="table table-bordered" style="font-size: 13px;width:560px" >';
			html+='			<tr>';
			html+='				<td style="width: 80px">';
			html+='					出差计划';
			html+='				</td>';
			html+='				<td colspan="3">';
			html+='				</td>';
			html+='			</tr>';
			html+='			<tr id="loanApplyTr">';
			html+='				<td style="width: 80px;">';
			html+='					本次限额';
			html+='				</td>';
			html+='				<td>';
			html+='				</td>';
			html+='				<td style="width: 80px">';
			html+='					累计借款';
			html+='				</td>';
			html+='				<td>';
			html+='			</tr>';
			html+='			<tr>';
			html+='				<td style="width: 80px">';
			html+='					总未销账';
			html+='				</td>';
			html+='				<td>';
			html+='				</td>';
			html+='				</td>';
			html+='				<td style="width: 80px;">';
			html+='					累计销账';
			html+='				</td>';
			html+='				<td>';
			html+='				</td>';
			html+='			</tr>';
			html+='		</table>';
			return html;
		}
}
//任务转办
EasyWinForm.updateSpInsAssignV2 = function (instanceId, actInstanceId) {


    var assign = $("#assimgImgDiv").data("assign");
    if (assign) {
        var userId = assign.userId;

        var url = "/workFlow/updateSpInsAssignV2";
        var params = {
            "sid": sid,
            "instanceId": instanceId,
            "newAssignerId": userId,
            "actInstanceId": actInstanceId
        };
        var formData = {};
        var spResult = $("#spBtnDiv").data("spResult");
        if (spResult) {
            formData.spIdea = spResult.spIdea;
        }
        //TODO 转办
//        var spFilesObj = $("#spFileRelate").find("select");
//        var spFlowUpfiles = new Array();
//        if ($(spFilesObj).find("option").length > 0) {
//
//            $.each($(spFilesObj).find("option"), function (i, vo) {
//                var file = {"upfileId": $(vo).val(), "filename": $(vo).text()}
//                spFlowUpfiles.push(file);
//            });
//            formData.spFlowUpfiles = spFlowUpfiles;
//        }
        
        params.formDataStr = JSON.stringify(formData);
        postUrl(url, params, function (data) {
            if (data.status == 'y') {
                window.self.location.reload();
            } else {
                showNotification(2, data.info);
            }
        })
    } else {
        scrollToLocation($("#contentBody"), $("#sp-container").find(".assignBtnChoose"));
        layer.tips("请选择转办人员", $("#sp-container").find(".assignBtnChoose"), {tips: 1});
    }
}
//任务转办
EasyWinForm.updateSpInsAssign = function (instanceId, actInstanceId) {
    userOne('null', 'null', null, sid, function (options) {
        if (options && options.length > 0) {
            var userId = options[0].value
            var userName = options[0].text;
            if (userId == EasyWin.userInfo.userId) {
                showNotification(2, "不能转办给自己！");
            } else {
                var url = "/workFlow/updateSpInsAssign";
                var params = {
                    "sid": sid,
                    "instanceId": instanceId,
                    "newAssignerId": userId,
                    "actInstanceId": actInstanceId
                };
                postUrl(url, params, function (data) {
                    if (data.status == 'y') {
                        //window.self.location.reload();
						openWindow.ReLoad();
						closeWin();
                    } else {
                        showNotification(2, data.info);
                    }
                })
            }
        }
    });
}

//任务拾取
EasyWinForm.pickSpIns = function (instanceId, actInstanceId) {
    window.top.layer.confirm("确定要办理该审批任务?", {icon: 3, title: '确认对话框'}, function (index) {
        window.top.layer.close(index);
        var url = "/workFlow/pickSpInstance";
        var params = {
            "sid": sid,
            "instanceId": instanceId,
            "actInstanceId": actInstanceId
        };
        postUrl(url, params, function (data) {
            if (data.status == 'y') {
                window.self.location.reload();
            } else {
                showNotification(2, data.info);
            }
        })
    })
}
EasyWinForm.optSubDataTable = function(data,relatetable,subTableBody,subTableTr){
	
	 var sublayoutDetails = $(subTableBody).data("sublayoutDetail");
	 
	$.each(data,function(index,obj){
		var subTableTrMod = $(subTableTr).clone();
		
		
		var objId = obj.id;
		var preObj = $(subTableBody).find("[busTableId='"+objId+"']");
		if(preObj && preObj.get(0)){
			return true;
		}
		//添加关联表的主键
		$(subTableTrMod).attr("busTableId",obj.id);
		
		var effectCpmponents = new Array();
		var effectIndex = 0;
		$.each(sublayoutDetails, function (index, sublayoutDetail) {
			if (sublayoutDetail.componentKey != 'subItemTitle') {
				effectCpmponents.push(sublayoutDetail);
				var defineTag = $("<easyWin></easyWin>");
				defineTag.attr("tempId", sublayoutDetail.tempId);
				defineTag.attr("componKey", sublayoutDetail.componentKey);
				$(subTableTrMod).find(".tdItem:eq(" + effectIndex + ")").append(defineTag);
				effectIndex++;
			}
		})
		var resultObj = {};
		$.each(obj,function(key,val){
			key = key.toLowerCase();
			resultObj[key] = val;
		});
		
		$.each(effectCpmponents, function (index, sublayoutDetailObj) {
			
			var sublayoutDetail= {}
        	$.extend(sublayoutDetail,sublayoutDetailObj);
			sublayoutDetail.isReadOnly="false";
			
			var tableColumn = sublayoutDetail.tableColumn;
			if(tableColumn){
				tableColumn = tableColumn.toLowerCase();
			}
			EasyWinForm.analyseComponent(sublayoutDetail, $(subTableTrMod));
			
			var val = resultObj[tableColumn];
			
			//记账功能的数据修改一下
			if(/consume/ig.test(relatetable)){
				if(tableColumn == 'type'){
					val = resultObj['typename'];
				}
			}
			
			//赋值显示
			var subCompone = $(subTableTrMod).find("[conf_tablecolumn='"+sublayoutDetail.tableColumn+"']");
			
			var subComponentKey = sublayoutDetail.componentKey;
			if(subComponentKey == "Select"){
				 var options = sublayoutDetail.options;
				 var optionId = 0;
				 $.each(options,function(optIndex,opt){
					 if(opt.name==val){
						 optionId =opt.fieldId; 
						 return false;
					 }
				 })
				 $(subCompone).data("oldContent", val);
				 var tempShowData = {};
                 tempShowData.optionId = optionId;
                 tempShowData.content = val;
                 
                 $(subCompone).append(val);

                 $(subCompone).data("tempShowData", tempShowData);
			}else if("Text" == subComponentKey
                    || "DateComponent" == subComponentKey
                    || "Monitor" == subComponentKey
                    || "SerialNum" == subComponentKey
                    || "NumberComponent" == subComponentKey){
				 $(subCompone).html(val);
				 $(subCompone).data("oldContent", val);
			}else if("TextArea" == subComponentKey){
				$(subCompone).data("oldContent", val);
				if (val) {
                    //文本内容换行
					val = val.replace(/(\r\n)|(\n)/g, '<br/>');
					val = val.replace(/\s/g, '&nbsp;&nbsp;');
                }
                $(subCompone).html(val);
			}else if("RadioBox" == subComponentKey){
				
				 var options = sublayoutDetail.options;
				 var optionId = 0;
				 $.each(options,function(optIndex,opt){
					 if(opt.name==val){
						 optionId =opt.fieldId; 
						 $(subCompone).find("input[value='" + optionId + "']").attr("checked", true);
						 return false;
					 }
				 })
			}else if("CheckBox" == subComponentKey){
				console.log("请联系周智强，需要根据不同的集合来显示数据！");
			}else{
				console.log("请联系周智强在表单设计时添加组件！")
			}
		})
		var lastObj = $(subTableBody).find("[busTableId]:last");
		if(lastObj && lastObj.get(0)){
			$(subTableBody).find("[busTableId]:last").after(subTableTrMod);
		}else{
			$(subTableBody).prepend(subTableTrMod);
		}
		//删除元素中有数字的
        var numCompones = $(subTableTrMod).find("[easyWinCompon='NumberComponent'],[easyWinCompon='Monitor']");
        EasyWinForm.optDataTableConsume(numCompones);
	})
}

EasyWinForm.optDataTableConsume = function(numCompones){
	 //存在数字计算控件，删除后需要重新计算
    if(numCompones && numCompones.get(0)){
    	//遍历删除的元素 可能包含计算元素的
    	$.each(numCompones,function(index,numCompone){
    		//计算控件的唯一标识
    		var fieldId = $(numCompone).attr("fieldId");
    		console.log(fieldId)
			var _this = $(this);
			//当前元素需要触发的计算控件
			var calBackFields = calElementRelate[fieldId];
			//没有需要触发的计算控件
			if(!calBackFields){
				return true;
			}
			
			//遍历需要触发的计算控件
			$.each(calBackFields,function(index,relateTempId){
				//查询范围，默认整个界面
				var _htmlScope = $("body");
				//需要触发的元素
				var calComponent = $("body").find("[easyWinCompon='Monitor'][fieldId='"+relateTempId+"']");
				//是否在子表单里面
				var calSubInDataTable = $(calComponent).parents("[easyWinCompon='DataTable']");
				if(calSubInDataTable && calSubInDataTable.get(0)){
					//字表单的计算控件可以直接删除
					return true;
				}
				//默认求和为0
				var total = 0;
				//计算控件的权重元素
				var subArray = calNumWeight[relateTempId];
				//遍历计算控件的权重元素
				for(var key in subArray){
					if(key != 'in_array'){
						//权重
						var weight = subArray[key];
						//权重元素的总数
						var subTotal = EasyWinForm.calTotalNum(key,_htmlScope);
						//增加权重元素的求和
						total = accAdd(total,accMul(subTotal,weight));
					}
				}
				//取得需要计算的控件
				var calComponent = $(_htmlScope).find("[easyWinCompon='Monitor'][fieldId='"+relateTempId+"']");
				//是否只读
				var isReadOnly = calComponent.data("componentData").isReadOnly;
				
				//赋值
				if (isReadOnly == 'false') {
					    $(calComponent).addClass("defVal");
                    $(calComponent).html(total);
                } else {
                    $(calComponent).val(total);
                }
				
				$(calComponent).trigger("transMonitorMoney",$(calComponent));
			});
    	})
    }
}
/**
 * 子表单关联数据选择后回调
 * param{
 * 	callback:回调函数
 * 	relatetable：使用的数据表
 * 	preBusTableIds：已有数据
 * 	seletWay：是否多选 默认多选2  单选1
 * }
 */
EasyWinForm.relateTableChoose = function(param){
	var callback = param.callback;
	var relatetable = param.relatetable;
	var instanceId = FLOWINFO.instanceId;
	//默认多选
	var seletWay = param.seletWay;
	seletWay = seletWay?seletWay:2;
	var url = "";
	if(/consume/ig.test(relatetable) ){
		url = "/consume/consumeSelectPage";
	}
	url = url+"?sid="+sid+"&t="+Math.random();
	window.top.layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		shadeClose: false,
		shade: 0.3,
		scrollbar:false,
		fix: true, //固定
		maxmin: false,
		move: false,
		area: ['800px', '550px'],
		content: [url,'no'], //iframe的url
		btn: ['选择', '取消'],
		yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var results = iframeWin.selectResults();
			  if(results){
				  callback(results);
				  window.top.layer.close(index);
			  }
		  }
		,btn2: function(index, layero){
			window.top.layer.close(index)
		},
		success:function(layero,index){
			var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			iframeWin.initData(relatetable,seletWay,instanceId);
		}
	});
	
}
//验证借款信息
EasyWinForm.checkloanInfo = function (param) {
    var callback = param.callback;
    var loanCheckState = $("#formInfo").data("loanCheckState");
    if (loanCheckState == "1") {//需要借款验证
        var loanFieldId = $("#formInfo").data("loanFieldId");
        //本次借款
        var loanMoney = $("#form-info-div").find("[fieldid='" + loanFieldId + "']").val();
        var loanApply = $("#formInfo").data("loanApply");
        if (loanMoney && loanMoney > 0) {//填写了本次借款
            if (loanMoney > 4000) {//单词借款不能超过四千
                window.top.layer.alert("单次借款不能超过4000?", {icon: 7, title: '提示对话框'}, function (index) {
                    window.top.layer.close(index);
                    callback(0);
                });
                return false;
            }

            //本次理想剩余额度  审批批准借款金额 - 总借款
            var totalLeft = loanApply.allowedQuota - loanApply.loanFeeTotal;

            //本次出差总借款
            var totalBorrow = loanApply.totalBorrow;
            //总报销
            var totalOff = loanApply.totalOff;
            //总的未报销的
            var totalNoOff = totalBorrow - totalOff;
            //本次的剩余额度
            var actLeftQuato = 4000 - totalNoOff;

            if (loanMoney <= actLeftQuato //本次借款没超过单次最高额度
                && loanMoney <= totalLeft) {//本次借款没超过审批最高额度
                callback(1);
                return true;
            } else if (loanMoney > actLeftQuato) {//借款超过单次最高额度
                if (loanMoney > totalLeft) {//借款超过审批最高额度
                    window.top.layer.alert("借款超过审批剩余额度:" + totalLeft + "元，请重新填写借款金额", {
                        icon: 7,
                        title: '提示对话框'
                    }, function (index) {
                        window.top.layer.close(index);
                        callback(0);
                    });
                    return false;
                } else {
                    window.top.layer.alert("借款超过单次未销账的最高额度：" + actLeftQuato + "元，请重新填写借款金额", {
                        icon: 7,
                        title: '提示对话框'
                    }, function (index) {
                        window.top.layer.close(index);
                        callback(0);
                    });
                    return false;
                }

            } else {
                if (loanMoney > totalLeft) {//借款超过审批最高额度
                    window.top.layer.alert("借款超过审批剩余额度:" + totalLeft + "元，请重新填写借款金额", {
                        icon: 7,
                        title: '提示对话框'
                    }, function (index) {
                        window.top.layer.close(index);
                        callback(0);
                    });
                    return false;
                }
            }
        } else {
            scrollToLocation($("#contentBody"), $("#form-info-div").find("[fieldid='" + loanFieldId + "']"));
            $("#form-info-div").find("[fieldid='" + loanFieldId + "']").focus();//控件聚焦
            layer.tips("请填写借款金额！", $("#form-info-div").find("[fieldid='" + loanFieldId + "']"), {tips: 1});
            callback(0);
            return false;
        }
    } else {
        callback(1);
        return true;
    }
}

Date.prototype.pattern = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    var week = {
        "0": "/u65e5",
        "1": "/u4e00",
        "2": "/u4e8c",
        "3": "/u4e09",
        "4": "/u56db",
        "5": "/u4e94",
        "6": "/u516d"
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""]);
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}  
