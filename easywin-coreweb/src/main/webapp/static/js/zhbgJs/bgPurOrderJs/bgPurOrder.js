var modHtml = '<tr><td class="tabIndex" style="height: 38px"></td>'
	modHtml+=' 			<td class="tabYpName">'
	modHtml+=' 				<input type="hidden" />'
	modHtml+=' 				<span></span>'
	modHtml+=' 				<span class="pull-right fa fa-plus blue ps-point bgypSelect" title="办公用品选择"></span>'
	modHtml+=' 			</td>'
	modHtml+=' 			<td class="tabYpUnit">'
	modHtml+=' 			</td>'
	modHtml+='			<td class="tabYpSpace">'
	modHtml+='			</td>'
	modHtml+=' 			<td class="tabYpPrice">'
	modHtml+=' 			</td>'
	modHtml+='			<td class="tabYpNum">'
	modHtml+='			</td>'
	modHtml+='			<td class="tabYpTotal">'
	modHtml+='			</td>'
	modHtml+=' 			<td>'
	modHtml+='				<a href="javascript:void(0)" class="fa fa-times-circle-o delBgypItem" title="删除本行"></a>'
	modHtml+='			</td>'
	modHtml+='		</tr>'
var optModNum = '<div class="spinner spinner-horizontal spinner-two-sided pull-left">';
	optModNum+='	<div class="spinner-buttons	btn-group spinner-buttons-left">';
	optModNum+='		<button type="button" class="btn spinner-down danger minusNum">';
	optModNum+='			<i class="fa fa-minus"></i>';
	optModNum+='		</button>';
	optModNum+='	</div>';
	optModNum+='	<input  class="spinner-input form-control table_input" maxlength="10" type="text" value="0">';
	optModNum+='	<div class="spinner-buttons	btn-group spinner-buttons-right">';
	optModNum+='		<button type="button" class="btn spinner-up blue addNum">';
	optModNum+='			<i class="fa fa-plus"></i>';
	optModNum+='		</button>';
	optModNum+='	</div>';
	optModNum+='</div>';
$(function(){
	//办公用品选择
	$("body").on("click",".bgypSelect",function(index,bgypSelectObj){
		var $e = $(this);
		var preId = $(this).parents("tr").find(".tabYpName input").val();
		
		var preBgypIds = new Array();
		$.each($("#bgypPurDetailT0").find(".tabYpName input"),function(index,vo){
			var val = $(this).val();
			if(val){
				preBgypIds.push(val);
			}
		})
		//办公用品条目选择
		bgypSelectMore(preBgypIds,function(data){
			if(data && data.length>0){
				if(data.length == 1 && !preId){
					var bgypItem = data[0];
					$($e).parents("tr").find(".tabYpName").find("input").val(bgypItem.id)
					$($e).parents("tr").find(".tabYpName").find("span").eq(0).html(bgypItem.bgypName);
					$($e).parents("tr").find(".tabYpUnit").html(bgypItem.bgypUnitName);
					$($e).parents("tr").find(".tabYpSpace").html(bgypItem.bgypSpace?bgypItem.bgypSpace:'/');
					
					$($e).parents("tr").find(".tabYpPrice").html('<input class="table_input" maxlength="10" type="text"/>');
					$($e).parents("tr").find(".tabYpNum").html($(optModNum).clone());
					
					$($e).parents("tr").attr("bgypItemId",bgypItem.id);
					
				}else{
					
					$.each(data,function(index,bgypItem){
						var detailHtml =  $(modHtml).clone();
						$(detailHtml).find(".tabYpName").find("input").val(bgypItem.id)
						$(detailHtml).find(".tabYpName").find("span").eq(0).html(bgypItem.bgypName);
						$(detailHtml).find(".tabYpUnit").html(bgypItem.bgypUnitName);
						$(detailHtml).find(".tabYpSpace").html(bgypItem.bgypSpace?bgypItem.bgypSpace:'/');
						
						$(detailHtml).find(".tabYpPrice").html('<input class="table_input"  maxlength="10" type="text"/>');
						$(detailHtml).find(".tabYpNum").html($(optModNum).clone());
						
						$(detailHtml).attr("bgypItemId",bgypItem.id)
						
						$e.parents("tr").after($(detailHtml))
					})
					//重置索引号
					resetIndex();
				}
			}
		})
		
	})
	
	//计算总价
	$("body").on("blur","input[name='bgypTotalPrice']",function(){
		var bgypTotalPrice = $(this).val();
		if(!bgypTotalPrice){
			bgypTotalPrice = 0;
		}else{ 
			bgypTotalPrice = Number(bgypTotalPrice)
		}
		$(this).val(bgypTotalPrice);
	})
	//计算总价
	$("body").on("blur",".tabYpPrice,.tabYpNum",function(){
		var tabYpPrice = $(this).parents("tr").find(".tabYpPrice").find("input").val();
		var tabYpNum = $(this).parents("tr").find(".tabYpNum").find("input").val();
		if(!tabYpPrice){
			tabYpPrice = 0;
		}else{ 
			tabYpPrice = Number(tabYpPrice)
			$(this).parents("tr").find(".tabYpPrice").find("input").val(tabYpPrice);
		}
		if(!tabYpNum){
			tabYpNum = 0;
		}else{
			tabYpNum = Number(tabYpNum)
			$(this).parents("tr").find(".tabYpNum").find("input").val(tabYpNum);
		}
		$(this).parents("tr").find(".tabYpTotal").html(accMul(tabYpPrice,tabYpNum));
		
		consumToTalPrice();
		
	})
	//控制输入
	$("body").on("keypress",".tabYpPrice input,.tabYpNum input,input[name='bgypTotalPrice']",function(event){
		 var eventObj = event || e;
         var keyCode = eventObj.keyCode || eventObj.which;
         
		if($(this).parents("td").hasClass("tabYpPrice")){
	         if ((keyCode >= 48 && keyCode <= 57) || keyCode == 46){//输入数字或小数点
	        	 var val = $(this).parents("td").find("input").val()
	        	 if(val && val.indexOf(".")>=0 && keyCode == 46){//有小数点则不能再次输入
	        		 return false;
	        	 }else if(!val && keyCode == 46){//直接输入小数点，添加首位0
	        		 $(this).parents("td").find("input").val(0)
	        		 return true;
	        	 }else{
	        		 return true;
	        	 }
	         }else
	             return false;
		}else if($(this).parents("td").hasClass("tabYpNum")){
	         if ((keyCode >= 48 && keyCode <= 57))//输入数字
	             return true;
	         else
	             return false;
		}else if($(this).parents("li").hasClass("ps-listline")){
			if ((keyCode >= 48 && keyCode <= 57) || keyCode == 46){
				 var val = $(this).parents("li").find("input[name='bgypTotalPrice']").val()
	        	 if(val && val.indexOf(".")>=0 && keyCode == 46){//有小数点则不能再次输入
	        		 return false;
	        	 }else if(!val && keyCode == 46){//直接输入小数点，添加首位0
	        		 $(this).parents("li").find("input[name='bgypTotalPrice']").val(0);
	        		 return true;
	        	 }else{
	        		 return true;
	        	 }
			}else
	             return false;
		}
	})
	//控制输入
	$("body").on("keydown",".ps-listline input",function(event){
		 var eventObj = event || e;
         var keyCode = eventObj.keyCode || eventObj.which;
		if($(this).parents("li").hasClass("ps-listline")){
			if (keyCode == 13) return false;
		}
	})
	
	//禁止粘贴
	$("body").bind("paste",".tabYpPrice input,.tabYpNum input,input[name='bgypTotalPrice']",function(event){
		return false;
	})
	
	//减少
	$("body").on("click",".minusNum",function(){
		var applyNumStr = $(this).parents("td").find("input").val();
		if(applyNumStr){
			var applyNum = parseInt(applyNumStr);
			if(applyNum > 0){
				applyNum = applyNum-1;
				$(this).parents("td").find("input").val(applyNum)
			}
		}else{
			$(this).parents("td").find("input").val(0)
		}
		$(this).parents("tr").find(".tabYpNum").trigger("blur")
	});
	//添加
	$("body").on("click",".addNum",function(){
		var applyNumStr = $(this).parents("td").find("input").val();
		if(applyNumStr){
			var applyNum = parseInt(applyNumStr)+1;
			$(this).parents("td").find("input").val(applyNum);
		}else{
			$(this).parents("td").find("input").val(1)
		}
		$(this).parents("tr").find(".tabYpNum").trigger("blur")
	})
	
	
})
	function addBgypPurDetailTable(listBgypPurDetails){
		if(listBgypPurDetails && listBgypPurDetails.length>0){
			$.each(listBgypPurDetails,function(index,bgypPurDetail){
				var detailHtml =  $(modHtml).clone();
				$(detailHtml).find(".tabYpName").find("input").val(bgypPurDetail.bgypItemId)
				$(detailHtml).find(".tabYpName").find("span").eq(0).html(bgypPurDetail.bgypName);
				$(detailHtml).find(".tabYpUnit").html(bgypPurDetail.bgypUnitName);
				$(detailHtml).find(".tabYpSpace").html(bgypPurDetail.bgypSpace?bgypPurDetail.bgypSpace:'/');
				
				$(detailHtml).find(".tabYpPrice").html('<input class="table_input" type="text"  maxlength="10" value="'+bgypPurDetail.bgypPrice+'"/>');
				
				var optNum = $(optModNum).clone();
				$(optNum).find("input").val(bgypPurDetail.bgypNum)
				$(detailHtml).find(".tabYpNum").html((optNum));
				
				$(detailHtml).attr("bgypItemId",bgypPurDetail.bgypItemId);
				
				$(detailHtml).attr("bgypDetailId",bgypPurDetail.id);
				$(detailHtml).css("color","#5db2ff");
				
				$(detailHtml).data("bgypPurDetail",bgypPurDetail);
				
				var tabYpPrice = bgypPurDetail.bgypPrice;
				var tabYpNum = bgypPurDetail.bgypNum;
				if(!tabYpPrice){
					tabYpPrice = 0;
				}
				if(!tabYpNum){
					tabYpNum = 0;
				}
				$(detailHtml).find(".tabYpTotal").html(accMul(tabYpPrice,tabYpNum));
				if($("#bgypPurDetailT0").find("tr[bgypItemId]").length==0){
					$("#bgypPurDetailT0").find("tr:first").before($(detailHtml))
				}else{
					$("#bgypPurDetailT0").find("tr[bgypItemId]:last").after($(detailHtml))
				}
				
			})
		}else{
			for(var i=0;i<5;i++){
				//初始化行数
				$("#bgypPurDetailT0").append($(modHtml).clone())
			}
		}
	
		//重置索引号
		resetIndex();
		
	}
function initBgypPurDetailTable(listBgypPurDetails){
	if(listBgypPurDetails && listBgypPurDetails.length>0){
		$.each(listBgypPurDetails,function(index,bgypPurDetail){
			var detailHtml =  $(modHtml).clone();
			$(detailHtml).find(".tabYpName").find("input").val(bgypPurDetail.bgypItemId)
			$(detailHtml).find(".tabYpName").find("span").eq(0).html(bgypPurDetail.bgypName);
			$(detailHtml).find(".tabYpUnit").html(bgypPurDetail.bgypUnitName);
			$(detailHtml).find(".tabYpSpace").html(bgypPurDetail.bgypSpace?bgypPurDetail.bgypSpace:'/');
			
			$(detailHtml).find(".tabYpPrice").html(bgypPurDetail.bgypPrice);
			$(detailHtml).find(".tabYpNum").html(bgypPurDetail.bgypNum);
			
			$(detailHtml).find(".bgypSelect").remove();
			$(detailHtml).find("td:last").html('');
			$(detailHtml).find("td:last").prev().css("border-right","0");
			$(detailHtml).find("td:last").css("border-left","0");
			
			$(detailHtml).attr("bgypItemId",bgypPurDetail.bgypItemId);
			
			$(detailHtml).attr("bgypDetailId",bgypPurDetail.id);
			
			
			var tabYpPrice = bgypPurDetail.bgypPrice;
			var tabYpNum = bgypPurDetail.bgypNum;
			if(!tabYpPrice){
				tabYpPrice = 0;
			}
			if(!tabYpNum){
				tabYpNum = 0;
			}
			$(detailHtml).find(".tabYpTotal").html(accMul(tabYpPrice,tabYpNum));
			
			$("#bgypPurDetailT0").append($(detailHtml))
			
		})
	}
	
	//重置索引号
	resetIndex();
	
}
//重置索引号
function resetIndex(){
	$.each($("#bgypPurDetailT0").find(".tabIndex"),function(index,vo){
		$(this).html((index+1));
	});
	consumToTalPrice();
}
//计算总价
function consumToTalPrice(){
	var total = 0;
	$.each($("#bgypPurDetailT0").find(".tabYpTotal"),function(index,vo){
		var content = $.trim($(this).html());
		if(content){
			total = accAdd(total,content);
		}
	});
	$("#bgypPurDetailT1").find("tr").find("td").eq(2).html(total+"元")
	$("#bgypPurDetailT1").find("tr").find("td").eq(1).html(changeNumMoneyToChinese(total))
}

//验证表单信息
	function checkFormElement(){
		//默认验证通过
		var formState = 1;
		
		//单据编号
		var purOrderNum = $("#purOrderNum").val();
		if(!purOrderNum){
			layer.tips("请填写单据编号","#purOrderNum",{tips:1})
			return !1;
		}
		//采购日期
		var bgypPurDate = $("#bgypPurDate").val();
		if(!bgypPurDate){
			layer.tips("请填写采购日期","#bgypPurDate",{tips:1})
			return !1;
		}
		//采购金额
		var bgypTotalPrice = $("#bgypTotalPrice").val();
		if(!bgypTotalPrice){
			layer.tips("请填写采购金额","#bgypTotalPrice",{tips:1})
			return !1;
		}
		//验证采购清单
		$.each($("body #bgypPurDetailT0").find("input[class='table_input']"),function(index,vo){
			if($(this).parents("td").hasClass("tabYpPrice")){
				var bgypName = $(this).parents("tr").find(".tabYpName").find("span").eq(0).html();
				var tabYpPrice = $(this).val();
				if(!tabYpPrice){
					layer.tips("请填写["+bgypName+"]采购单价",$(this),{tips:1});
					formState = !1;
					return !1;
				}
			}else if($(this).parents("td").hasClass("tabYpNum")){
			var bgypName = $(this).parents("tr").find(".tabYpName").find("span").eq(0).html();
				var tabYpNum = $(this).val();
				if(!tabYpNum){
					layer.tips("请填写["+bgypName+"]采购数量",$(this),{tips:1});
					formState = !1;
					return !1;
				}
			}
		})
		return formState;
	}
	//构建表单数据
	function constrFormData(){
		$("#formData").html('');
		//遍历选中项
		$.each($("#bgypPurOrderForm").find("select[multiple]"),function(selectIndex,selectObj){
			var list = $(this).attr("list");
			var listkey = $(this).attr("listkey");
			var listvalue = $(this).attr("listvalue");
			
			$.each($(selectObj).find("option"),function(optionIndex,optionObj){
				var hiddenInputId = $("<input type='hidden' name='"+list+"["+optionIndex+"]."+listkey+"' value='"+$(optionObj).val()+"' />")
				var hiddenInputName = $("<input type='hidden' name='"+list+"["+optionIndex+"]."+listvalue+"' value='"+$(optionObj).text()+"' />")
				$("#formData").append(hiddenInputId)
				$("#formData").append(hiddenInputName)
			})
		});
		var detailIndex = 0;
		//遍历采购单条目
		$.each($("body #bgypPurDetailT0").find("tr[bgypItemId]"),function(actIndex,vo){
			
			var bgypItemId = $(this).find("td").eq(1).find("input").val();
			var bgypPrice = $(this).find("td").eq(4).find("input").val();
			var bgypNum = $(this).find("td").eq(5).find("input").val();
			
			var bgypPurDetail =  $(this).data("bgypPurDetail");
			if(bgypPurDetail){
				var preBgypPrice = bgypPurDetail.bgypPrice+'';
				var preBgypNum = bgypPurDetail.bgypNum+'';
				if(preBgypPrice == bgypPrice && bgypNum == preBgypNum){
					return true;
				}else{
					var hiddenId = $("<input type='hidden' name='listBgypPurDetails["+detailIndex+"].id' value='"+bgypPurDetail.id+"' />")
					$("#formData").append(hiddenId);
				}
			}
			
			var hiddenInputId = $("<input type='hidden' name='listBgypPurDetails["+detailIndex+"].bgypItemId' value='"+bgypItemId+"' />")
			var hiddenInputPrice = $("<input type='hidden' name='listBgypPurDetails["+detailIndex+"].bgypPrice' value='"+bgypPrice+"' />")
			var hiddenInputNum = $("<input type='hidden' name='listBgypPurDetails["+detailIndex+"].bgypNum' value='"+bgypNum+"' />")
			
			$("#formData").append(hiddenInputId)
			$("#formData").append(hiddenInputPrice)
			$("#formData").append(hiddenInputNum)
			detailIndex = detailIndex+1;
		});
	}
