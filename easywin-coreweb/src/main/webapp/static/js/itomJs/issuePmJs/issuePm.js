var pageNum = 0;     //页面索引初始值   
var pageSize =10;     //每页显示条数初始化，修改显示条数，修改这里即可 
var nowDate;
$(function(){
	//清空条件
	$("body").on("click",".clearValue",function(){
		var relateElement = $(this).attr("relateElement");
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		$("#formTempData").find("input[name='"+relateElement+"']").val('');
		if(relateElement=='busId'){
			$("#formTempData").find("input[name='busType']").val('');
		}
		var _i = $('<i class="fa fa-angle-down"></i>')
		
		$(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
		$(this).parents("ul").prev().append(_i);
		
		initIssuePmTable();
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
		
		initIssuePmTable();
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
		
		
		initIssuePmTable();
	})
	//人员多选
	$("body").on("click",".userMoreSelect",function(){
		var relateList = $(this).attr("relateList");
		userMore(relateList, null, sid, null, null, function(options){
			$("#formTempData").find("#"+relateList).html('');
			$("#"+relateList+"Div").find("span").remove();
			if(options && options.length>0){
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
					
					$(_span).data("userId",$(option).val());
					$(_span).data("relateList",relateList);
					
				})
			}else{
				$("#"+relateList+"Div").hide();
			}
			initIssuePmTable();
		})
	});
	//项目多选
	$("body").off("click",".relateModSelect").on("click",".relateModSelect",function(){
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
		
		if(busType=='005'){
			var relateListTemp = relateList;
			itemMoreSelect(isMore, relateListTemp,function(items){
				$("#formTempData").find("#"+relateList).html('');
				$("#"+relateList+"Div").find("span").remove();
				
				if(items && items.length>0){
					
					$.each(items,function(index,item){
						if(isMore === 1){
							$("#formTempData").find("input[name='busId']").val(item.id);
							$("#formTempData").find("input[name='busType']").val(busType);
							
							var _font = $('<font style="font-weight:bold;"></font>')
							$(_font).html(item.itemName)
							var _i = $('<i class="fa fa-angle-down"></i>')
							
							$(_this).parents("ul").prev().html(_font);
							$(_this).parents("ul").prev().append(_i);
							
							initIssuePmTable();
							
						}else{
							
							var _option = $("<option selected='selected'></option>")
							$(_option).val(item.id);
							$(_option).html(item.itemName);
							$("#"+relateList).append(_option);
							
							var _span = $("<span></span>");
							$(_span).html(item.itemName)
							$(_span).addClass("label label-default margin-right-5 margin-bottom-5");
							$(_span).attr("title","双击移除");
							$(_span).css("cursor","pointer");
							$("#"+relateList+"Div").append(_span);
							
							$(_span).data("busId",item.id);
							$(_span).data("relateList",relateList);
						}
					})
				}
			})
		}
	});
	//双击移除
	$("body").on("dblclick",".moreUserListShow span",function(){
		var userId = $(this).data("userId");
		var relateList = $(this).data("relateList");
		$("#"+relateList).find("option[value='"+userId+"']").remove();
		if($(this).parents(".moreUserListShow").find("span").length<=1){
			$(this).parents(".moreUserListShow").hide();
		}
		$(this).remove();
		initIssuePmTable();
	})
	
	//查询时间
	$("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
		initIssuePmTable();
	})
	//查询时间
	$("body").on("click",".moreClearBtn",function(){
		$("#moreCondition_Div").find("input").val('');
		initIssuePmTable();
	})
	//查询时间
	$("body").on("blur",".moreSearch",function(){
		initIssuePmTable();
	})
	//查看审批
	$("body").on("click",".spFlowIns",function(){
		var insId = $(this).attr("insId");
		if(insId && insId>0){
			viewSpFlow(insId);
		}
	})
	//查看模块
	$("body").on("click","a[busmodname]",function(){
		var busId = $(this).attr("busId");
		var busType = $(this).attr("busType");
		if(busId){
			viewModInfo(busId,busType);
		}
	})
	//添加问题管理过程
	$("body").on("click","a[addIssuePms]",function(){
		listBusMapFlows('04202',function(busMapFlow){
			var url = "/busRelateSpFlow/issuePm/addIssuePm?sid="+sid+"&busMapFlowId="+busMapFlow.id+"&busType=04202";
			openWinByRight(url);
		})
	})
	initIssuePmTable();
})
		//出差管理
	 	function initIssuePmTable(morePageNum){
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
	     		//取得数据
	     		getSelfJSON("/iTOm/issuePm/ajaxListPagedIssuePm",params,function(data){
	     			if(data.status=='y'){
	     				var pageBean = data.pageBean;
	     				nowDate = data.nowDate;
	     			 	$("#totalNum").html(pageBean.totalCount);
		               	if(pageBean.totalCount<=pageSize){
		               		 $("#totalNum").parent().parent().css("display","none")
		               	}else{
		               		$("#totalNum").parent().parent().css("display","block")
		               	}
	              		//分页，PageCount是总条目数，这是必选参数，其它参数都是可选
	                     $("#pageDiv").pagination(pageBean.totalCount, {
	                         callback: PageCallback,  //PageCallback() 为翻页调用次函数。
	                         prev_text: "<<",
	                         next_text: ">>",
	                         items_per_page:pageSize,
	                         num_edge_entries: 0,       //两侧首尾分页条目数
	                         num_display_entries: 3,    //连续分页主体部分分页条目数
	                         current_page: pageNum,   //当前页索引
	                     });
		               	 $("#allTodoBody").html('');
		               	 if(pageBean.totalCount>0){
		               		 constrDataTable(pageBean.recordList);
		               	 }else{
		               		 var tr = $("<tr></tr>");
		               		 var td = $("<td></td>");
		               		 var len = $("#allTodoBody").parents("table").find("thead tr th").length;
		               		td.attr("colspan",len);
		               		td.css("text-align","center");
		               		td.html("未查询到相关数据");
		               		
		               		tr.append(td);
		               		$("#allTodoBody").append(tr);
		               		
		               	 }
	     			 }
	     		})
	 	}
		 //翻页调用   
        function PageCallback(index, jq) {  
   	   	 pageNum = index;
   	  	 initIssuePmTable(index);
        } 
		 
		 function constrDataTable(issuePms){
			 if(issuePms && issuePms[0]){
				 var rowNumIndex = 1;
				 $.each(issuePms,function(objIndex,obj){
					 //一行数据
					 var tr = $("<tr></tr>");
					 $(tr).data("obj",obj);
					 var tdMod = $("<td></td>");
					 tdMod.css("text-align","center")
					 
					 //序号
					 var tdXh = $(tdMod).clone();
					 $(tdXh).html(rowNumIndex++)
					 $(tr).append(tdXh)
					 //创建日期
					 var tdCreateDate = $(tdMod).clone();
					 $(tdCreateDate).html(obj.recordCreateTime.substring(0,10));
					 $(tr).append(tdCreateDate);
					 //人员
					 var tdUser = $(tdMod).clone();
					 $(tdUser).html(obj.creatorName);
					 $(tr).append(tdUser);
					 //问题单流水号
					 var tdIssueId = $(tdMod).clone();
					 $(tdIssueId).html(obj.issueId);
					 $(tr).append(tdIssueId);
					 
					 //问题过程流程
					 var tdFlowName = $(tdMod).clone();
					 var flownameA = $('<a href="javascript:void(0)" class="spFlowIns"></a>');
					 $(flownameA).html(obj.flowName);
					 $(flownameA).attr("insId",obj.instanceId);
					 $(tdFlowName).html($(flownameA));
					 $(tr).append(tdFlowName);
					 
					 //审批状态
					 var tdFlowState = $(tdMod).clone();
					 var  status = obj.status;
					 if(status === 0 && obj.flowState==2){
						 status = 2;
					 }
					 $(tdFlowState).html(constrTagHtml(status,''));
					 $(tr).append(tdFlowState);
					 
					 //问题来源
					 var tdIssueAttr = $(tdMod).clone();
					 $(tdIssueAttr).html(obj.issueSource);
					 $(tr).append(tdIssueAttr);
					 
					 //所属系统类型
					 var tdBusModName = $(tdMod).clone();
					 var busModNameA = $('<a href="javascript:void(0)" busModName></a>');
					 $(busModNameA).attr("busId",obj.busId);
					 $(busModNameA).attr("busType",obj.busType);
					 $(busModNameA).html(obj.busModName);
					 $(tdBusModName).html($(busModNameA));
					 $(tr).append(tdBusModName);
					 
					 //所属系统类型
					 var tdIssueEndCode = $(tdMod).clone();
					 $(tdIssueEndCode).html(obj.issueEndCode);
					 $(tr).append(tdIssueEndCode);
					 
					 //所属系统类型
					 var tdIssueStatus = $(tdMod).clone();
					 $(tdIssueStatus).html(obj.issueStatus);
					 $(tr).append(tdIssueStatus);
					 
					 
					 $("#allTodoBody").append($(tr));
					 
				 })
			 }
		 }
		 
		 function constrTagHtml(status,title){
			 var html = "";
			 if(status == 4){//通过
				 html = '<i class="fa fa-check green" title="'+title+'成功"></i>';
			 }else if (status == 1){//审核中
				 html = '<i class="fa fa-gavel blue" title="'+title+'中"></i>';
			 }else if (status == 2){//草稿
				 html = '<i class="fa fa-pencil purple" title="草稿"></i>';
			 }else if (status == -1){//未通过
				 html = '<i class="fa fa-times red" title="'+title+'失败"></i>';
			 }
			 return html;
		 }
