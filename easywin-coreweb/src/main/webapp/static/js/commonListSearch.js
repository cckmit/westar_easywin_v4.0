$(function(){
	//清空条件
	$("body").on("click",".clearThisElement",function(){
		$(this).parents("form").find("input[name='"+$(this).attr("relateElement")+"']").val('');
		
		var relateElementName = $(this).attr("relateElementName");
		if(relateElementName){
			$(this).parents("form").find("input[name='"+relateElementName+"']").val('');
		}
		$(this).parents("form").submit();
	});
	//清空多选信息
	$("body").on("click",".clearMoreElement",function(){
		
		var relateList = $(this).attr("relateList");
		$(this).parents("form").find("select[multiple]").filter("#"+relateList).html('');
		$(this).parents("form").submit();
	});
	
	//模糊查询
	$("body").on("blur",".formElementSearch",function(){
	    var val = $(this).val();
	    if(val.length < 1000 || !val){
            $(this).parents("form").submit();
        }else{
            window.top.layer.msg('关键词长度过长！', {icon:2});
        }
	})
	//单个赋值
	$("body").on("click",".setElementValue",function(){
		$(this).parents("form").find("input[name='"+$(this).attr("relateElement")+"']").val($(this).attr("dataValue"));
		
		var relateElementName = $(this).attr("relateElementName");
		if(relateElementName){
			$(this).parents("form").find("input[name='"+relateElementName+"']").val($.trim($(this).html()));
			
		}
		$(this).parents("form").submit();
	});
	//项目多选
	$("body").on("click",".relateModSelect",function(){
		//关联的业务模块
		var busType=$(this).attr("busType");
		//业务类型代码
		var typeValue=$(this).attr("typeValue");
		//存放的位置
		var relateList = $(this).attr("relateList");
		//是否多选
		var isMore = $(this).attr("isMore");
		isMore = (isMore && isMore=='0')?1:2;
		var _this = $(this);
		
		var preBusType = $(this).parents("form").find("input[name='"+typeValue+"']").val();
		if(busType=='005'){
			var relateListTemp = relateList;
			if(preBusType && preBusType != '005'){
				relateListTemp = 'null';
			}
			itemMoreSelect(isMore, relateListTemp,function(items){
				$("#"+relateList).html('');
				$(_this).parents("form").find("input[name='"+typeValue+"']").val('');
				
				if(items && items.length>0){
					$(_this).parents("form").find("input[name='"+typeValue+"']").val(busType);
					$.each(items,function(index,item){
						var option = $("<option value='"+item.id+"'>"+item.itemName+"</option>");
						$("#"+relateList).append(option)
					})
				}
				$(_this).parents("form").submit();
			})
		}else if(busType=='012'){
			var relateListTemp = relateList;
			if(preBusType && preBusType != '012'){
				relateListTemp = 'null';
			}
			crmMoreSelect(isMore, relateListTemp,function(crms){
				$("#"+relateList).html('');
				$(_this).parents("form").find("input[name='"+typeValue+"']").val('');
				
				if(crms && crms.length>0){
					$(_this).parents("form").find("input[name='"+typeValue+"']").val(busType);
					$.each(crms,function(index,crm){
						var option = $("<option value='"+crm.id+"'>"+crm.crmName+"</option>");
						$("#"+relateList).append(option)
					})
				}
				$(_this).parents("form").submit();
			})
			
		}else if(busType=='070'){
			var relateListTemp = relateList;
			if(preBusType && preBusType != '070'){
				relateListTemp = 'null';
			}
			productSelect(isMore, relateListTemp,function(products){
				$("#"+relateList).html('');
				$(_this).parents("form").find("input[name='"+typeValue+"']").val('');
				
				if(products && products.length>0){
					$(_this).parents("form").find("input[name='"+typeValue+"']").val(busType);
					$.each(products,function(index,product){
						var option = $("<option value='"+product.id+"'>"+product.name+"</option>");
						$("#"+relateList).append(option)
					})
				}
				$(_this).parents("form").submit();
			})
		}
	});
	//人员多选
	$("body").on("click",".userMoreElementSelect",function(){
		var relateList = $(this).attr("relateList");
		var _this = $(this);
		userMore(relateList, null, sid,'yes',null,function(options){
			$("#"+relateList).html('');
			if(options && options.length>0){
				$.each(options,function(index,object){
					var option = $("<option value='"+$(object).val()+"'>"+$(object).text()+"</option>");
					$("#"+relateList).append(option)
				})
			}
			$(_this).parents("form").submit();
		})
	});
	//人员单选
	$("body").on("click",".userOneElementSelect",function(){
		var userId = $(this).attr("relateElement");
		var userName = $(this).attr("relateElementName");
		var _this = $(this);
		userOne(userId, userName, null, sid,function(options){
			if(options && options.length>0){
				$.each(options,function(index,object){
					$(_this).parents("form").find("input[name='"+userId+"']").val(object.value);
					$(_this).parents("form").find("input[name='"+userName+"']").val(object.text);
				})
			}else{
				$(_this).parents("form").find("input[name='"+userId+"']").val('');
				$(_this).parents("form").find("input[name='"+userName+"']").val('');
			}
			$(_this).parents("form").submit();
		});
	});
	//部门单选
	$("body").on("click",".depOneElementSelect",function(){
		var depId = $(this).attr("relateElement");
		var depName = $(this).attr("relateElementName");
		var _this = $(this);
		depOne(depId, depName, null, sid,function(result){
			 $(_this).parents("form").find("input[name='"+depId+"']").val(result.orgId);
			 $(_this).parents("form").find("input[name='"+depName+"']").val(result.orgName);
			 $(_this).parents("form").submit();
		})
	});
	
	//部门多选
	$("body").on("click",".depMoreElementSelect",function(){
		var relateList = $(this).attr("relateList");
		var _this = $(this);
		depMoreBack(relateList, null, sid,'yes',null,function(options){
			$("#"+relateList).html('');
			if(options && options.length>0){
				$.each(options,function(index,object){
					var option = $("<option value='"+$(object).val()+"'>"+$(object).text()+"</option>");
					$("#"+relateList).append(option)
				})
			}
			$(_this).parents("form").submit();
		})
	});
	
	//办公用品类别单选
	$("body").on("click",".bgflOneElementSelect",function(){
		var bgflId = $(this).attr("relateElement");
		var bgflName = $(this).attr("relateElementName");
		var _this = $(this);
		bgflOneSelect(null, null, null, sid,function(data){
			if(data.bgflId==-1){
				$(_this).parents("form").find("input[name='"+bgflId+"']").val('');
			}else{
				$(_this).parents("form").find("input[name='"+bgflId+"']").val(data.bgflId);
			}
			$(_this).parents("form").find("input[name='"+bgflName+"']").val(data.bgflName);
			
			$(_this).parents("form").submit();
		})
	});
	
	//遍历需要设置字典表的字段
	$.each($("body").find(".dataDicClz"),function(index,obj){
		var relateElement = $(this).attr("relateElement");
		var relateElementName = $(this).attr("relateElementName");
		var _this = $(this);
		var type=$(this).attr("dataDic");
		if(relateElementName){
			$(this).append('<li><a href="javascript:void(0)" class="clearThisElement" relateElement="'+relateElement+'" relateElementName="'+relateElementName+'">不限条件</a></li>')
		}else{
			$(this).append('<li><a href="javascript:void(0)" class="clearThisElement" relateElement="'+relateElement+'">不限条件</a></li>')
		}
		//取得字典表数据
		listDataDic(type,function(dataDics){
			if(dataDics){
				$.each(dataDics,function(key,dataDic){
					if(dataDic.parentId==-1){
						return true;
					}
					if(relateElementName){
						$(_this).append('<li><a href="javascript:void(0)" class="setElementValue" relateElement="'+relateElement+'" dataValue="'+dataDic.code+'" relateElementName="'+relateElementName+'">'+dataDic.zvalue+'</a></li>')
					}else{
						$(_this).append('<li><a href="javascript:void(0)" class="setElementValue" relateElement="'+relateElement+'" dataValue="'+dataDic.code+'">'+dataDic.zvalue+'</a></li>')
					}
				})
			}
		})
	})
	//操作删除和复选框
	$('.optTr').mouseover(function(){
		var display = $(this).find(".optTd .optCheckBox").css("display");
		if(display=='none'){
			$(this).find(".optTd .optCheckBox").css("display","block");
			$(this).find(".optTd .optRowNum").css("display","none");
		}
	});
	$('.optTr').mouseout(function(){
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
	
});

//窗体点击事件检测
document.onclick = function(e){
	var evt = e?e:window.event;
	var ele = $(evt.srcElement || evt.target);
	if ($(ele).parents("#moreCondition_Div").length == 1) { 
		if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
			return false;
		}else if($(ele).hasClass("noMoreShow")){
			$("#moreCondition_Div").removeClass("open");
			$("#moreCondition_Div").trigger("hideMoreDiv")
		}else{
			if(!$("#moreCondition_Div").hasClass("open")){
				$(".searchCond").find(".open").removeClass("open");
				$("#moreCondition_Div").addClass("open");
			}
		}
	} else{
		$("#moreCondition_Div").removeClass("open");
		
	}
}
/**
 * 展示查询条件中更多
 */
function displayMoreCond(divId){
	if($("#"+divId).hasClass("open")){
		$("#"+divId).removeClass("open");
	}else{
		$("#"+divId).addClass("open")
	}
}

/**
 * 清空更多查询条件
 */
function resetMoreCon(divId){
	$("#"+divId).find("input").val('');
	var stateBtnArray = $(".stateBtn");
	$.each(stateBtnArray,function(index,item){
		$(this).removeClass("btn-primary");
		if(!$(this).hasClass("btn-default")){
			$(this).addClass("btn-default");
		}
	})
	
	var gradeBtnArray = $(".gradeBtn");
	$.each(gradeBtnArray,function(index,item){
		$(this).removeClass("btn-primary");
		if(!$(this).hasClass("btn-default")){
			$(this).addClass("btn-default");
		}
	})
	var overBtnArray = $(".overBtn");
		$.each(overBtnArray,function(index,item){
			$(this).removeClass("btn-primary");
			if(!$(this).hasClass("btn-default")){
				$(this).addClass("btn-default");
			}
		})	
	$("#state").val('');
	$("#grade").val('');
	$("#overdue").val('false');
	$("#overDiv").hide();
	
	//清空经办人
	$("#operator_select").html('');
	$(".operatorSpanDiv").find("span").remove();
	$(".operatorSpanDiv").hide();
	//清空执行部门
	$("#executeDep_select").html('');
	$(".executeDepSpanDiv").find("span").remove();
	$(".executeDepSpanDiv").hide();
}

//移除筛选(人员多选)
function removeChoose(userIdtag,sid,userId){
	$("#"+userIdtag+"_select").find("option[value='"+userId+"']").remove();
	$("#searchForm").submit();
}

/**
 * 功能清单导入
 * @param id 结点id
 * @param busType
 */
function functionImport(busId,busType,sid){
    layui.use('layer', function(){
        window.top.layer.open({
            type: 2,
            title:false,
            closeBtn: 0,
            area: ['920px', '550px'],
            fix: true, //不固定
            maxmin: false,
            zIndex:1010,
            scrollbar:false,
            content: ["/functionList/functionImportPage?sid="+sid+ "&busId=" + busId + "&busType="+busType,'no'],
            btn: ['导入','关闭'],
            yes: function(index, layero){
                var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
                var flag = iframeWin.formSub();
                if(flag){
                    window.top.layer.close(index);
                    window.self.location.reload();
                }
            },cancel: function(){}
        });
    })
}

