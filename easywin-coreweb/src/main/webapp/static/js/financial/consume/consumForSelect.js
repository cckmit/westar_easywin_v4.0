var pageNum = 0;     //页面索引初始值   
var pageSize =8;     //每页显示条数初始化，修改显示条数，修改这里即可 
var nowDate;

var ConsumeSelectOpt = {
		loadData:function(morePageNum){
			var params = ConsumeSelectOpt.constrParam(morePageNum);
			//取得数据
     		getSelfJSON("/consume/listPagedConsumeForSelect",params,function(data){
     			if(data.status=='y'){
     				var pageBean = data.pageBean;
     			 	$("#totalNum").html(pageBean.totalCount);
	               	if(pageBean.totalCount<=pageSize){
	               		 $("#totalNum").parent().parent().css("display","none");
	               	}else{
	               		$("#totalNum").parent().parent().css("display","block");
	               	}
              		//分页，PageCount是总条目数，这是必选参数，其它参数都是可选
                     $("#pageDiv").pagination(pageBean.totalCount, {
                         callback: ConsumeSelectOpt.pageCallback,  //PageCallback() 为翻页调用次函数。
                         prev_text: "<<",
                         next_text: ">>",
                         items_per_page:pageSize,
                         num_edge_entries: 0,       //两侧首尾分页条目数
                         num_display_entries: 3,    //连续分页主体部分分页条目数
                         current_page: pageNum,   //当前页索引
                     });
	               	 $("#allDataTable").html('');
	               	 if(pageBean.totalCount>0){
	               		ConsumeSelectOpt.constrDataTable(pageBean.recordList);
	               	 }else{
	               		 var _tr = $("<tr></tr>");
	               		 var _td = $("<td></td>");
	               		 var len = $("#allDataTable").parents("table").find("thead tr th").length;
	               		_td.attr("colspan",len);
	               		_td.css("text-align","center");
	               		_td.html("未查询到相关数据");
	               		
	               		_tr.append(_td);
	               		$("#allDataTable").append(_tr);
	               		
	               	 }
     			 }
     		})
		},constrParam:function(morePageNum){//构建数据
			if(morePageNum){
				pageNum = morePageNum;
			}else{
				pageNum = 0;
			}
			var params={"sid":sid,
               	 "pageNum":pageNum,
               	 "pageSize":pageSize
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
			
			return params;
		},pageCallback:function(index, jq){//翻页
			pageNum = index;
			$("#checkAllData").attr("checked",false);
			ConsumeSelectOpt.loadData(index);
		},constrDataTable:function(consumes){//构建数据表
			 if(consumes && consumes[0]){
				 $.each(consumes,function(index,consume){
					 var _tr = $('<tr></tr>');
					 
					 $(_tr).data("obj",consume);
					 
					 var checkTd = $("<td style='text-align:center;' class='padding-top-5 padding-bottom-5'><label class='no-margin-bottom'><input class='colored-blue' name='consumId' type='checkbox'/><span class='text' style='color: inherit;'></span></label></td>");
					 if(seletWay == 1){//单选
						$td0.find("input").remove();
						checkTd.find("span").before("<input class='colored-blue' name='consumId' type='radio'/>");
					 }
					 if(map[consume.id]){
						$(checkTd).find("input").attr("checked",true); 
					 }
					 
					 $(_tr).append($(checkTd));
					 
					 //序号
					 var rowNum = index + 1 + pageNum*pageSize;
					 var _rowNumTd = $('<td></td>');
					 $(_rowNumTd).html(rowNum);
					 $(_tr).append($(_rowNumTd));
					 
					 //费用类型
					 var typeNameTd =  $('<td></td>');
					 $(typeNameTd).html(consume.typeName);
					 
					  $(_tr).append($(typeNameTd));
					 
					 //金额
					 var amountTd = $('<td></td>');
					 var amount = consume.amount;
					 $(amountTd).html(toDecimal2(amount?amount:0));
					 
					 $(_tr).append($(amountTd));
					 
					 //消费时间
					 var startDateTd = $('<td></td>');
					 var startDate = consume.startDate;
					 var endDate = consume.endDate;
					 var dateStr = startDate;
					 if(endDate){
						 dateStr = dateStr + "至"+endDate;
					 }
					 
					 $(startDateTd).html(dateStr);
					 $(_tr).append($(startDateTd));
					 
					 //消费地点
					 var leavePlaceTd = $('<td></td>');
					 var leavePlace = consume.leavePlace;
					 var arrivePlace = consume.arrivePlace;
					 
					 var place = leavePlace;
					 if(arrivePlace){
						 place = place + "至"+arrivePlace;
					 }
					 
					 $(leavePlaceTd).html(place);
					 $(_tr).append($(leavePlaceTd));
					 
					 //发票数量
					 var invoiceNumTd = $('<td></td>');
					 $(invoiceNumTd).html(consume.invoiceNum);
					 $(_tr).append($(invoiceNumTd));
					 
					 
					 $("#allDataTable").append(_tr);
				 })
			 }
		}
}


$(function(){
	
	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		ConsumeSelectOpt.loadData(0);
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
		
		ConsumeSelectOpt.loadData(0);
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
		
		
		ConsumeSelectOpt.loadData(0);
	})
	//人员多选
	$("body").on("click",".consumTypeMoreSelect",function(){
		var relateList = $(this).attr("relateList");
		
		
		consumeTypeMoreTree({
			sid:sid,
			preSelectTagId:relateList,
			callback: function(options){
				$("#formTempData").find("#"+relateList).html('');
				$("#"+relateList+"Div").find("span").remove();
				if(options && options[0]){
					$("#"+relateList+"Div").show()
					$.each(options,function(optIndex,option){
						var _option = $("<option selected='selected'></option>")
						$(_option).val($(option).val());
						$(_option).html($(option).text());
						$("#"+relateList).append(_option);
						
						var _span = $("<span></span>");
						$(_span).html($(option).text())
						$(_span).addClass("label label-default margin-right-5 margin-bottom-5");
						$(_span).attr("title","双击移除");
						$(_span).css("cursor","pointer");
						$("#"+relateList+"Div").append(_span);
						
						$(_span).data("consumTypeId",$(option).val());
						$(_span).data("relateList",relateList);
						
					})
				}else{
					$("#"+relateList+"Div").hide();
				}
				ConsumeSelectOpt.loadData(0);
			}
		});
	});
	//双击移除
	$("body").on("dblclick",".moreConsumeTypeListShow span",function(){
		var consumTypeId = $(this).data("consumTypeId");
		var relateList = $(this).data("relateList");
		$("#"+relateList).find("option[value='"+consumTypeId+"']").remove();
		if($(this).parents(".moreConsumeTypeListShow").find("span").length<=1){
			$(this).parents(".moreConsumeTypeListShow").hide();
		}
		$(this).remove();
		ConsumeSelectOpt.loadData(0);
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		ConsumeSelectOpt.loadData(0);
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		ConsumeSelectOpt.loadData(0);
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		ConsumeSelectOpt.loadData(0);
	})
})
