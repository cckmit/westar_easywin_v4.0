var pageNum = 0;     //页面索引初始值
var pageSize =30;     //每页显示条数初始化，修改显示条数，修改这里即可
var now = new Date();
var nowDate = now.getFullYear() + "-" + ((now.getMonth() + 1) < 10 ? "0" + (now.getMonth() + 1) : (now.getMonth() + 1)) + "-" + (now.getDate() < 10 ? "0" + now.getDate() : now.getDate());

//是否只查询月份数据，默认不是
var maxMonth=-1;

$(function(){
    var height = $(window).height()-120;
    $("#editabledatatable").parent().parent().css("max-height",height + "px");

    $('body').on('click','label',function(){
        $('.event_year>li').removeClass('current');
        $(this).parent('li').addClass('current');
        var year = $(this).attr('for');
        
        $("#startDate").val('');
        $("#endDate").val('');
        
        var month = year.substring(6,year.length);
        //查询指定的月份数据
        maxMonth = month;
        
        initDailyTable(0);
        $('#'+year).parent().prevAll('div').slideUp(800);
        $('#'+year).parent().slideDown(800).nextAll('div').slideDown(800);
    });

    //清空条件
    $("body").on("click",".clearValue",function(){
        var relateElement = $(this).attr("relateElement");
        $("#formTempData").find("input[name='"+relateElement+"']").val('');

        var _i = $('<i class="fa fa-angle-down"></i>')

        $(this).parents("ul").prev().html($(this).parents("ul").prev().attr("title"));
        $(this).parents("ul").prev().append(_i);

        initDailyTable();
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

        initDailyTable();
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

        initDailyTable();
    })

    $("body").on("click","#dailyMore",function(){
        pageNum++;
        initDailyTable(pageNum);
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
            initDailyTable();
        })
    });
    //双击移除
    $("body").on("dblclick",".moreUserListShow span",function(){
        var userId = $(this).data("userId");
        var relateList = $(this).data("relateList");
        if(!userId){
            userId = $(this).attr("data-userId");
            relateList = $(this).attr("relateList");
        }
        $("#"+relateList).find("option[value='"+userId+"']").remove();
        if($(this).parents(".moreUserListShow").find("span").length<=1){
            $(this).parents(".moreUserListShow").hide();
        }
        $(this).remove();
        initDailyTable();
    })
    //部门单选
    $("body").off("click",".depOneElementSelect").on("click",".depOneElementSelect",function(){
        var depId = $(this).attr("relateElement");
        var depName = $(this).attr("relateElementName");
        var _this = $(this);
        depOne(depId, depName, null, sid,function(result){
            $("#formTempData").find("input[name='"+depId+"']").val(result.orgId);
            $("#formTempData").find("input[name='"+depName+"']").val(result.orgName);

            var _font = $('<font style="font-weight:bold;"></font>')
            $(_font).html(result.orgName)
            var _i = $('<i class="fa fa-angle-down"></i>')

            $(_this).parents("ul").prev().html(_font);
            $(_this).parents("ul").prev().append(_i);

            initDailyTable();
        })
    });

    //部门多选
    $("body").on("click",".depMoreSelect",function(){
        var relateList = $(this).attr("relateList");
        var _this = $(this);
        depMoreBack(relateList, null, sid,'yes',null,function(options){
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

                    $(_span).data("depId",$(option).val());
                    $(_span).data("relateList",relateList);

                })
            }else{
                $("#"+relateList+"Div").hide();
            }
            initDailyTable();
        })
    });
    //双击移除
    $("body").on("dblclick",".moreDepListShow span",function(){
        var depId = $(this).data("depId");
        var relateList = $(this).data("relateList");
        $("#"+relateList).find("option[value='"+depId+"']").remove();
        if($(this).parents(".moreDepListShow").find("span").length<=1){
            $(this).parents(".moreDepListShow").hide();
        }
        $(this).remove();
        initDailyTable();
    })
    //查询时间
    $("body").on("click",".moreSearchBtn,.ps-searchBtn",function(){
        initDailyTable();
    })
    //查询时间
    $("body").on("click",".moreClearBtn",function(){
    	maxMonth = -1;
        $("#moreCondition_Div").find("input").val('');
        initDailyTable();
    })
    //查询时间
    $("body").on("blur",".moreSearch",function(){
        initDailyTable();
    })
    //设置年份选择和日数选择
    getSelfJSON('/daily/findOrgInfo',{"sid":sid},function(data){
        //当前时间
        var nowTime = data.nowTime;
        //当前年份
        var nowYear = parseInt(nowTime.substring(0,4));
        //当前月份
        var nowMonth = new Date(nowTime.replace("-","/").replace("-","/")).getMonth() + 1;
        //注册时间
        var registerTime = data.registerTime;
        //注册年份
        var registerYear = parseInt(registerTime.substring(0,4));
        //注册月份
        var registerMonth = new Date(registerTime.replace("-","/").replace("-","/")).getMonth() + 1;
        //设置默认显示年月和其它年月下拉
        for(var index = nowYear ; index >= registerYear ; index--){
            var _li = $('<li></li>');
            var _a = $('<a href="javascript:void(0)" class="yearClick"></a>');
            $(_a).attr("data-dataValue",index);
            $(_a).html(index+'年');
            $(_li).append($(_a));
            $("#dailyYearUl").append($(_li));
            if(index == nowYear){
                //设置默认月份下拉
                for(var month = nowMonth ; month >= 1 ; month--) {
                    var __li = $('<li></li>');
                    var __a = $('<a href="javascript:void(0)" class="monthClick"></a>');
                    $(__a).attr("data-dataValue", month);
                    $(__a).html((month < 10 ? "0" + month : month) + '月');
                    $(__li).append($(__a));
                    $("#dailyMonthUl").append($(__li));
                }
            }
        }

        //年份选择
        $("body").on("click",".yearClick",function(){
            var infoA  = $("#dailyYearUl").prev();
            var dataValue = $(this).attr("data-dataValue");
            $(infoA).html('<font style="font-weight:bold;">'+dataValue+'年</font>')
            $(infoA).append('<i class="fa fa-angle-down"></i>');
            $("body").find("#dailyYear").val(dataValue);
            
            if(dataValue == pageParam.nowYear){
            	 maxMonth =  parseInt(pageParam.nowMonth);
            }else{
            	 maxMonth = 12;
            }
            initYearMonth(dataValue,maxMonth);
            constrWdatePicker(dataValue);
            initDailyTable(0);
            
        });
        initYearMonth(nowYear,nowMonth);
    })
    maxMonth = parseInt(pageParam.nowMonth);
    constrWdatePicker(pageParam.nowYear)
    initDailyTable();

})

function initYearMonth(nowYear,nowMonth){
	var startMonth = 12;
    var dailyYear = $("body").find("#dailyYear").val();
    if(nowYear == dailyYear){
    	startMonth = nowMonth;
    }
    $("body").find(".event_year").empty();
    //初始化左边数据
    for(var i=startMonth;i>=1;i--){
    	 var $li = $('<li></li>');
    	 if(i==startMonth){
    		 $li.addClass("current");
    	 }
    	 var $label = $('<label></label>');
    	 $($label).attr("for","month_"+i);
    	 $($label).html(i+"月");
    	 $($li).append($($label));
    	 $("body").find(".event_year").append($($li));
    }
}
function closeLoad(){
    if(loadDone){
        layui.use('layer', function() {
            layer.closeAll('loading');
        });
        clearInterval(intervalInt);
    }

}

var loadingIndex;
var loadDone=0;
var intervalInt;

//前一个部门主键
var preDepId = 0;
//重复数
var preDepNum = 0;

var depScoreObj = {};

var preMonth = 0;
var preDaily;
var monthTop = {};

//分享
function initDailyTable(morePageNum){
	loadDone=0;
    layui.use('layer', function() {
        loadingIndex = layer.load(0, {
            shade: [0.5,'#fff'] //0.1透明度的白色背景
        });
        intervalInt = setInterval("closeLoad("+loadingIndex+")",500);
    })
    if(morePageNum){
        pageNum = morePageNum;
    }else{
        depScoreObj = {};
        pageNum = 0;
        //前一个部门主键
        preDepId = 0;
        //重复数
        preDepNum = 0;
        preMonth = 0
        preDaily = 0;
        monthTop = {}
        $(".event_list").html('');
        if(maxMonth==-1){
        	$(".event_year").html('');
        }
    }
    if(pageParam.searchTab =='51'){
        pageSize = 10;
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
    if(maxMonth!=-1){
    	params["chooseMonth"] = maxMonth;
    }
    //取得数据
    getSelfJSON("/daily/ajaxListPagedDaily",params,function(data){
        if(data.status=='y'){

            var pageBean =  data.pageBean;
            var totalCount = pageBean.totalCount;
            $("#dailyMore").show();
            var currentPage = pageSize * pageNum + pageBean.recordList.length;
            if(totalCount === currentPage){
                $("#dailyMore").hide();
            }
            if(pageParam.searchTab =='51'){
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
                    constrData(pageBean.recordList);
                }else{
                    var _tr = $("<tr></tr>");
                    var _td = $("<td></td>");
                    var len = $("#allTodoBody").parents("table").find("thead tr th").length;
                    _td.attr("colspan",len);
                    _td.css("text-align","center");
                    _td.html("未查询到相关数据");

                    _tr.append(_td);
                    $("#allTodoBody").append(_tr);

                }
            }else{
                constrDataV2(pageBean.recordList);
            }
        }
        loadDone = 1;
    })

}

//翻页调用
function PageCallback(index, jq) {
    pageNum = index;
    initDailyTable(index);
}
/**
 * 构建数据
 * @param dataList
 */
function constrDataV2(dataList){
    if(dataList && dataList[0]){
        $.each(dataList,function(index,dailyVo){
            var dailyDate = dailyVo.dailyDate;
            var dailyMonth = parseInt(dailyDate.substring(5,7));
            var dailyYear = parseInt(dailyDate.substring(0,4));
            dailyMonth = +dailyMonth;
            if(preMonth == 0 &&  maxMonth == -1 ){
                for(var i=12;i>=1;i--){
                    var $li = $('<li></li>');
                    if(12 == i){
                        $li.addClass("current");
                    }

                    var $label = $('<label></label>');
                    $($label).attr("for","month_"+i);
                    $($label).html(i+"月");
                    $($li).append($($label));

                    $(".event_year").append($($li));
                }
                preMonth = dailyMonth;
            }else if(preMonth == 0){
            	$("body").find(".event_year").find("li").removeClass("current");
            	$("body").find(".event_year").find("[for=month_"+dailyMonth+"]").parent().addClass("current");
            	preMonth = dailyMonth;
            }

            var hTop = monthTop[dailyMonth];
            if(!hTop || !hTop.get(0)){
                var div = $('<div></div>');
                var hTop = $('<h3></h3>');
                $(hTop).attr("id","month_"+dailyMonth);
                $(hTop).html(dailyMonth+"月");
                monthTop[dailyMonth] = $(hTop);
                $(div).append($(hTop));
                $(".event_list").append($(div));
            }
            var div = $(hTop).parent();

            var li = $('<li></li>');
            var lispan = $('<span></span>');
            var indexStart = dailyDate;
            var daily = dailyDate;
            if(!preDaily || preDaily != daily){
                preDaily = indexStart;
                $(lispan).append(dailyDate);
            }
            $(li).append($(lispan));

            var p = $('<p></p>');
            var contentSpan = $('<span></span>');

            var dailyNameA;
            if(dailyVo.state==0){
                dailyNameA = $('<a href="javascript:void(0);" onclick="readMod(this,\'daily\',0,\'050\');viewDaily('+dailyVo.id+');"></a>')
                $(dailyNameA).html(dailyVo.dailyName);
            }else if(dailyVo.year==pageParam.nowYear && dailyVo.dailyNum==pageParam.nowDailyNum
                && dailyVo.reporterId==pageParam.userInfo.id){
                dailyNameA = $('<a href="javascript:void(0);" onclick="readMod(this,\'daily\',0,\'050\');addDaily(\''+dailyVo.dailyDate+'\');"></a>')
                $(dailyNameA).html(dailyVo.dailyName);
                $(contentSpan).css("background-color","#fb6e52");
                $(contentSpan).css("color","#fff");
                $(dailyNameA).css("color","#fff");
            }else if(dailyVo.reporterId==pageParam.userInfo.id){
                dailyNameA = $('<a href="javascript:void(0);" onclick="readMod(this,\'daily\',0,\'050\');addDaily(\''+dailyVo.dailyDate+'\');"></a>')
                $(dailyNameA).html(dailyVo.dailyName);
                $(contentSpan).css("background-color","#fb6e52");
                $(contentSpan).css("color","#fff");
                $(dailyNameA).css("color","#fff");
            }else{
                dailyNameA = dailyVo.dailyName;
                $(contentSpan).css("background-color","#fb6e52");
                $(contentSpan).css("color","#fff");
            }
            $(contentSpan).html(dailyNameA);

            var dateSpan = $('<lable class="pull-right"></lable>');
            if(dailyVo.state==0){
                $(dateSpan).append(dailyVo.recordCreateTime.substring(0,19));
            }else if(dailyVo.dailyDate == nowDate && dailyVo.reporterId==pageParam.userInfo.id){
                var dailyA = $('<a href="javascript:void(0);" style="color: blue" onclick="readMod(this,\'daily\',0,\'006\');addDaily(\''+dailyVo.dailyDate+'\');"> 发布 </a>')
                $(dateSpan).append(dailyA);
                $(dailyA).css("color","#fff");
            }else if(dailyVo.reporterId==pageParam.userInfo.id){
                var dailyA = $('<a href="javascript:void(0);" style="color: blue" onclick="readMod(this,\'daily\',0,\'006\');addDaily(\''+dailyVo.dailyDate+'\');"> 补发 </a>')
                $(dateSpan).append(dailyA);
                $(dailyA).css("color","#fff");
            }else{
                $(dateSpan).append('未发布');
            }
            $(contentSpan).append(dateSpan);

            $(p).append(contentSpan);


            $(li).append($(p));

            $(div).append(li)

        })
    }
}

function constrWdatePicker(year){
	$(document).off("focus","#startDate").on("focus","#startDate",function(){
		if(year == pageParam.nowYear){
			WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0})||\''+pageParam.nowDate+'\';}',minDate:year+'-01-01'  })
		}else{
			WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0})||\''+year+'-12-31\';}',minDate:year+'-01-01'  })
		}
	})
	$(document).off("focus","#endDate").on("focus","#endDate",function(){
		if(year == pageParam.nowYear){
			WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0})||\''+year+'-01-01\';}',maxDate:''+pageParam.nowDate+''})
		}else{
			WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0})||\''+year+'-01-01\';}',maxDate:year+'-12-31'})
		}
	})
}

function constrData(dataList){
    var dailyIndex = 1;
    if(dataList && dataList[0]){
        $.each(dataList,function(index,dailyVo){
            var dailyTr = $('<tr></tr>');
            //序号
            var xhTd = $('<td valign="middle"></td>');
            $(xhTd).append(dailyIndex++);
            $(dailyTr).append(xhTd);

            //名称
            var dailyNameTd = $('<td valign="middle" class="hidden-phone"></td>');

            var dailyNameA;
            var dateTime;
            var dailyScope;

            var dailyDate =dailyVo.dailyDate.substring(0,11);
            if(dailyVo.state==0){
                dailyNameA = $('<a href="javascript:void(0);" onclick="readMod(this,\'daily\',0,\'050\');viewDaily('+dailyVo.id+');"></a>')

                if(dailyVo.isRead==0){
                    $(dailyNameA).addClass('noread');
                }

                dateTime = $(dailyNameA).clone();
                dailyScope = $(dailyNameA).clone();
                $(dailyNameA).html(dailyVo.dailyName);
                dateTime = dailyVo.recordCreateTime.substring(0,19);
                $(dailyScope).append(dailyDate);

            }else if(dailyDate == nowDate && dailyVo.reporterId==pageParam.userInfo.id){
                dailyNameA = $('<a href="javascript:void(0);" onclick="readMod(this,\'daily\',0,\'050\');addDaily(\''+dailyDate+'\');"></a>')
                if(dailyVo.isRead==0){
                    $(dailyNameA).addClass('noread');
                }
                dateTime = $(dailyNameA).clone();
                dailyScope = $(dailyNameA).clone();
                $(dailyNameA).html(dailyVo.dailyName);
                $(dateTime).append('发布');
                $(dailyScope).append(dailyDate);
                $(dailyNameA).css("color","red");
                $(dateTime).css("color","red");
            }else if(dailyVo.reporterId==pageParam.userInfo.id){
                dailyNameA = $('<a href="javascript:void(0);" onclick="readMod(this,\'daily\',0,\'050\');addDaily(\''+dailyDate+'\');"></a>')
                if(dailyVo.isRead==0){
                    $(dailyNameA).addClass('noread');
                }
                dateTime = $(dailyNameA).clone();
                dailyScope = $(dailyNameA).clone();
                $(dailyNameA).html(dailyVo.dailyName);
                $(dateTime).append('补发');
                $(dailyScope).append(dailyDate);
                $(dailyNameA).css("color","red");
                $(dateTime).css("color","red");
            }else{
                dailyNameA = dailyVo.dailyName;
                dateTime = '未发布';
                dailyScope = dailyDate;
                $(dailyNameTd).css("color","red");
            }
            $(dailyNameTd).html(dailyNameA);
            $(dailyTr).append(dailyNameTd);
            //汇报范围
            var dailyScopeTd = $('<td valign="middle"></td>');
            $(dailyScopeTd).append(dailyScope);
            $(dailyTr).append(dailyScopeTd);

            //部门
            var depTd = $('<td valign="middle"></td>');
            $(depTd).append(dailyVo.depName);
            $(dailyTr).append(depTd);

            //汇报时间
            var dateTimeTd = $('<td valign="middle"></td>');
            if(dateTime=='未发布'){
                $(dateTimeTd).css("color","red");
            }
            $(dateTimeTd).append(dateTime);
            $(dailyTr).append(dateTimeTd);

            //头像
            var imgTd = $('<td valign="middle"></td>');
            var imgDiv = $('<div class="ticket-user pull-left other-user-box"></div>');
            var img = $('<img class="user-avatar" />');

            $(img).attr("src","/downLoad/userImg/"+dailyVo.comId+"/"+dailyVo.reporterId);
            $(imgDiv).append(img);
            var spanName = $('<span class="user-name">'+dailyVo.userName+'</span>');
            $(imgDiv).append(spanName);

            $(imgTd).append(imgDiv);
            $(dailyTr).append(imgTd);
            $("#allTodoBody").append($(dailyTr));
        })
    }
}
 



