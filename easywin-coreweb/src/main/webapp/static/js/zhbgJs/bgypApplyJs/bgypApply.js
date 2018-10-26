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
	modHtml+='			<td class="tabYpApped">'
	modHtml+='			</td>'
	modHtml+=' 			<td class="tabYpStore">'
	modHtml+=' 			</td>'
	modHtml+='			<td class="tabYpApply">'
	modHtml+='			</td>'
	modHtml+=' 			<td>'
	modHtml+='				<a href="javascript:void(0)" class="fa fa-times-circle-o delBgypItem" title="删除本行"></a>'
	modHtml+='			</td>'
	modHtml+='		</tr>';

var optModNum = '<div class="spinner spinner-horizontal spinner-two-sided pull-left">';
	optModNum+='	<div class="spinner-buttons	btn-group spinner-buttons-left">';
	optModNum+='		<button type="button" class="btn spinner-down danger minusNum">';
	optModNum+='			<i class="fa fa-minus"></i>';
	optModNum+='		</button>';
	optModNum+='	</div>';
	optModNum+='	<input  class="spinner-input form-control table_input" maxlength="3" type="text" value="0">';
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
		$.each($("#bgypApplyT0").find(".tabYpName input"),function(index,vo){
			var val = $(this).val();
			if(val){
				preBgypIds.push(val);
			}
		})
		//办公用品条目选择
		bgypStoreSelectMore(preBgypIds,function(data){
			if(data && data.length>0){
				if(data.length == 1 && !preId){
					var bgypStore = data[0];
					$($e).parents("tr").find(".tabYpName").find("input").val(bgypStore.bgypItemId)
					$($e).parents("tr").find(".tabYpName").find("span").eq(0).html(bgypStore.bgypName);
					$($e).parents("tr").find(".tabYpUnit").html(bgypStore.bgypUnitName);
					$($e).parents("tr").find(".tabYpSpace").html(bgypStore.bgypSpace?bgypStore.bgypSpace:'/');
					
					$($e).parents("tr").find(".tabYpStore").html(bgypStore.storeNum);
					$($e).parents("tr").find(".tabYpApped").html(bgypStore.applyingNum?bgypStore.applyingNum:'0');
					$($e).parents("tr").find(".tabYpApply").html($(optModNum).clone());
					
					$($e).parents("tr").attr("bgypItemId",bgypStore.id);
					
				}else{
					$.each(data,function(index,bgypStore){
						var detailHtml =  $(modHtml).clone();
						$(detailHtml).find(".tabYpName").find("input").val(bgypStore.bgypItemId)
						$(detailHtml).find(".tabYpName").find("span").eq(0).html(bgypStore.bgypName);
						$(detailHtml).find(".tabYpUnit").html(bgypStore.bgypUnitName);
						$(detailHtml).find(".tabYpSpace").html(bgypStore.bgypSpace?bgypStore.bgypSpace:'/');
						
						$(detailHtml).find(".tabYpStore").html(bgypStore.storeNum);
						$(detailHtml).find(".tabYpApped").html(bgypStore.applyingNum?bgypStore.applyingNum:'0');
						$(detailHtml).find(".tabYpApply").html($(optModNum).clone());
						
						$(detailHtml).attr("bgypItemId",bgypStore.id)
						
						
						$e.parents("tr").before($(detailHtml))
					})
					//重置索引号
					resetIndex();
				}
			}
		})
		
	})
	//控制输入
	$("body").on("keypress",".tabYpApply input",function(event){
		var eventObj = event || e;
        var keyCode = eventObj.keyCode || eventObj.which;
		if($(this).parents("td").hasClass("tabYpApply")){
	         if ((keyCode >= 48 && keyCode <= 57)){
	        	 return true;
	         }
	         else
	             return false;
		}
	})
	$("body").on("blur",".tabYpApply input",function(){
		var storeNum = $.trim($(this).parents("tr").find(".tabYpStore").html());
		
		var applyNumStr = $(this).parents("td").find("input").val();
		if(applyNumStr){
			var applyNum = Number(applyNumStr);
			if(applyNum > storeNum){
				$(this).parents("td").find("input").val(storeNum)
			}else{
				$(this).parents("td").find("input").val(applyNum)
			}
		}else{
			$(this).parents("td").find("input").val(0)
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
	$("body").bind("paste",".tabYpApply input",function(event){
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
	});
	//添加
	$("body").on("click",".addNum",function(){
		var storeNum = $.trim($(this).parents("tr").find(".tabYpStore").html());
		var applyNumStr = $(this).parents("td").find("input").val();
		if(applyNumStr){
			var applyNum = parseInt(applyNumStr);
			if(applyNum < storeNum){
				applyNum = applyNum+1;
				$(this).parents("td").find("input").val(applyNum)
			}
		}else{
			$(this).parents("td").find("input").val(1)
		}
	})
	
})
	function addBgypApplyDetailTable(bgypApplyDetails){
		if(bgypApplyDetails && bgypApplyDetails.length>0){
			$.each(bgypApplyDetails,function(index,bgypApplyDetail){
				var detailHtml =  $(modHtml).clone();
				$(detailHtml).find(".tabYpName").find("input").val(bgypApplyDetail.bgypItemId)
				$(detailHtml).find(".tabYpName").find("span").eq(0).html(bgypApplyDetail.bgypName);
				$(detailHtml).find(".tabYpUnit").html(bgypApplyDetail.bgypUnitName);
				$(detailHtml).find(".tabYpSpace").html(bgypApplyDetail.bgypSpace?bgypPurDetail.bgypSpace:'/');
				
				var applyNum = $(optModNum).clone();
				$(applyNum).find("input").val(bgypApplyDetail.applyNum);
				
				$(detailHtml).find(".tabYpStore").html(bgypApplyDetail.storeNum);
				$(detailHtml).find(".tabYpApped").html(bgypApplyDetail.applyingNum?bgypApplyDetail.applyingNum:'0');
				$(detailHtml).find(".tabYpApply").html(applyNum);
				
				$(detailHtml).attr("bgypItemId",bgypApplyDetail.bgypItemId);
				
				$(detailHtml).attr("bgypApplyDetailId",bgypApplyDetail.id);
				$(detailHtml).css("color","#5db2ff");
				
				$(detailHtml).data("bgypStore",bgypApplyDetail);
				if($("#bgypApplyT0").find("tr[bgypItemId]").length==0){
					$("#bgypApplyT0").find("tr:first").before($(detailHtml))
				}else{
					$("#bgypApplyT0").find("tr[bgypItemId]:last").after($(detailHtml))
				}
				
			})
		}else{
			for(var i=0;i<5;i++){
				//初始化行数
				$("#bgypApplyT0").append($(modHtml).clone())
			}
		}
	
		//重置索引号
		resetIndex();
		
	}
function initBgypApplyDetailTable(listBgypApplyDetails){
	if(listBgypApplyDetails && listBgypApplyDetails.length>0){
		$.each(listBgypApplyDetails,function(index,bgypApplyDetail){
			var detailHtml =  $(modHtml).clone();
			$(detailHtml).find(".tabYpName").find("input").val(bgypApplyDetail.bgypItemId)
			$(detailHtml).find(".tabYpName").find("span").eq(0).html(bgypApplyDetail.bgypName);
			$(detailHtml).find(".tabYpUnit").html(bgypApplyDetail.bgypUnitName);
			$(detailHtml).find(".tabYpSpace").html(bgypApplyDetail.bgypSpace?bgypApplyDetail.bgypSpace:'/');
			
			$(detailHtml).find(".tabYpStore").html(bgypApplyDetail.storeNum);
			$(detailHtml).find(".tabYpApped").remove();
			$(detailHtml).find(".tabYpApply").html(bgypApplyDetail.applyNum);
			
			$(detailHtml).find(".bgypSelect").remove();
			$(detailHtml).find("td:last").html('');
			$(detailHtml).find("td:last").prev().css("border-right","0");
			$(detailHtml).find("td:last").css("border-left","0");
			
			$(detailHtml).attr("bgypItemId",bgypApplyDetail.bgypItemId);
			
			$(detailHtml).attr("bgypApplyDetailId",bgypApplyDetail.id);
			
			$("#bgypApplyT0").append($(detailHtml))
			
		})
	}
	
	//重置索引号
	resetIndex();
	
}
//重置索引号
	function resetIndex(){
		$.each($("#bgypApplyT0").find(".tabIndex"),function(index,vo){
			$(this).html((index+1));
		});
	}
//验证表单信息
	function checkFormElement(){
		//默认验证通过
		var formState = 1;
		//采购金额
		var remark = $("#remark").val();
		if(!remark){
			layer.tips("请填写申领理由","#remark",{tips:1})
			return !1;
		}
		//验证采购清单
		$.each($("body #bgypApplyT0").find("input").filter(".table_input"),function(index,vo){
			if($(this).parents("td").hasClass("tabYpApply")){
				var bgypName = $(this).parents("tr").find(".tabYpName").find("span").eq(0).html();
				var tabYpStore = $(this).val();
				if(!tabYpStore || parseInt(tabYpStore)==0){
					layer.tips("请填写["+bgypName+"]的申领数",$(this),{tips:1});
					formState = !1;
					return false;
				}
			}
		})
		return formState;
	}
	//构建表单数据
	function constrFormData(){
		$("#formData").html('');
		//遍历选中项
		$.each($("#bgypApplyForm").find("select[multiple]"),function(selectIndex,selectObj){
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
		$.each($("body #bgypApplyT0").find("tr[bgypItemId]"),function(actIndex,vo){
			
			var bgypItemId = $(this).attr("bgypItemId")
			var applyNum = $(this).find("td").eq(6).find("input").val();
			
			var bgypStore =  $(this).data("bgypStore");
			if(bgypStore){
				var preApplyNum = bgypStore.applyNum+'';
				if(applyNum == preApplyNum){
					return true;//continus;
				}else{
					var hiddenId = $("<input type='hidden' name='listBgypApplyDetails["+detailIndex+"].id' value='"+bgypPurDetail.id+"' />")
					$("#formData").append(hiddenId);
				}
			}
			
			var hiddenInputId = $("<input type='hidden' name='listBgypApplyDetails["+detailIndex+"].bgypItemId' value='"+bgypItemId+"' />")
			var hiddenInputNum = $("<input type='hidden' name='listBgypApplyDetails["+detailIndex+"].applyNum' value='"+applyNum+"' />")
			
			$("#formData").append(hiddenInputId)
			$("#formData").append(hiddenInputNum)
			detailIndex = detailIndex+1;
		});
	}
