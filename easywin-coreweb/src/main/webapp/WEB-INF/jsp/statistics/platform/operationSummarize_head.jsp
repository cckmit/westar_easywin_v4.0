<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<body>
	<div class="item">
		<p class="item-title">
			今日新增任务 <img class="item-title-icon" src="/static/assets/img/icon0.png">
		</p>
		<p class="new-info">
			新增：<span id="newTaskNum">0</span>
		</p>
		<p class="end-info">
			已完结：<span class="left-span" id="finishTaskNum">0</span>
			完结率：<span id="taskPercent">0%</span>
		</p>
		<p class="sum-info">
			累计任务：<span id="allTaskNum">0</span>
		</p>
	</div>
	<div class="item">
		<p class="item-title">
			今日新增客户 <img class="item-title-icon" src="/static/assets/img/icon0.png">
		</p>
		<p class="new-info">
			新增：<span id="newCrmNum">0</span>
		</p>
		<p class="end-info">
			今日维护：<span class="left-span" id="modifyCrmNum">0</span>
			超期：<span class="left-span" id="overTimeCrmNum">0</span>
		</p>
		<p class="sum-info">
			累计客户：<span id="totalCrmNum">0</span>
		</p>
	</div>
	<div class="item">
		<p class="item-title">
			今日日报 <img class="item-title-icon" src="/static/assets/img/icon0.png">
		</p>
		<div>
			<p class="new-info pull-left">
				应交：<span id="dailyShouldNum">0人</span>
			</p>
			<p class="end-info pull-right no-margin">
				已交：<span id="dailyActNum">0人</span>
			</p>
		</div>
		<div class="ps-clear"></div>
		<div>
			<p class="end-info pull-left">
				迟交：<span class="left-span" id="dailyOverNum">0人</span>
			</p>
			<p class="end-info pull-right">
				未交：<span id="dailyNoneNum">0人</span>
			</p>
		</div>
		<p class="sum-info">
			及时率：<span id="dailyOnTimePercent">0%</span>
		</p>
	</div>
	<div class="item">
		<p class="item-title">
			第<span id="weekNumSpan">1</span>周
			周报汇报 <img class="item-title-icon" src="/static/assets/img/icon0.png">
		</p>
		<div>
			<p class="new-info pull-left">
				应交：<span id="weekShouldNum">0人</span>
			</p>
			<p class="end-info pull-right no-margin">
				已交：<span class="left-span" id="weekActNum">0人</span> 
			</p>
		</div>
		<div class="ps-clear"></div>
		<div class="ps-clear"></div>
		<div>
			<p class="end-info pull-left">
				迟交：<span class="left-span" id="weekOverNum">0人</span>
			</p>
			<p class="end-info pull-right">
				未交：<span class="left-span" id="weekNoneNum">0人</span>
			</p>
		</div>
		<p class="sum-info">
			及时率：<span id="weekOnTimePercent">0%</span>
		</p>
	</div>
	
	<script type="text/javascript">
		$(function(){
			var ajaxUrl = "/statistics/platform/statisticTodayByType?sid=${param.sid}&t="+Math.random();
			var array = ['003','006','012','050'];
			$.each(array,function(index,type){
				var url = ajaxUrl;
				getSelfJSON(url,{type:type},function(result){
					if(result.code==-1){
						console.log(result.msg);
						return;
					}
					var data = result.data;
					headDataOpt.optData(type,data)
				})
			})
		})
		
		var headDataOpt = {
			optData:function(type,data){
				if(type == '003'){//任务
					headDataOpt.optTask(data);
				}else if(type == '012'){//客户
					headDataOpt.optCrm(data);
				}else if(type == '050'){//日报
					headDataOpt.optDaily(data);
				}else if(type == '006'){//周报
					headDataOpt.optWeek(data);
				}

			},
			optTask:function(data){//今日新增任务
				//新增任务
				var NEWTASKNUM = data.NEWTASKNUM;
				$("#newTaskNum").html(NEWTASKNUM);
				//已完结
				var FINISHTASKNUM = data.FINISHTASKNUM;
				$("#finishTaskNum").html(FINISHTASKNUM);
				//累计任务数
				var ALLTASKNUM = data.ALLTASKNUM;
				$("#allTaskNum").html(ALLTASKNUM);
				
				var percent = 0 ;
				if(NEWTASKNUM == 0){
					if(FINISHTASKNUM > 0){
						percent = 100
					}
				}else if(FINISHTASKNUM == 0){
					percent = 0
				}else{
					percent = accDiv(FINISHTASKNUM,NEWTASKNUM);
					percent = accMul(percent,100);
					percent = new Number(percent).toFixed(2);
				}
				$("#taskPercent").html(percent + "%");
			},optCrm:function(data){//客户
				//今日新增客户
				var NEWCRMNUM = data.NEWCRMNUM;
				$("#newCrmNum").html(NEWCRMNUM);
				//今日维护客户
				var MODIFYCRMNUM = data.MODIFYCRMNUM;
				$("#modifyCrmNum").html(MODIFYCRMNUM);
				//超期维护客户
				var OVERTIMECRMNUM = data.OVERTIMECRMNUM;
				$("#overTimeCrmNum").html(OVERTIMECRMNUM);
				//累计客户
				var  ALLCRMNUM = data.ALLCRMNUM;
				$("#totalCrmNum").html(ALLCRMNUM);
				
				//重点客户
				$("#vipCrmNum").html(0);
			},optDaily:function(data){//日报
				headDataOpt.initDailyClick(data);
				//应交
				var DAILYSHOULDNUM = data.DAILYSHOULDNUM;
				$("#dailyShouldNum").html(DAILYSHOULDNUM + "人");
				//迟交
				var DAILYOVERNUM = data.DAILYOVERNUM;
				$("#dailyOverNum").html(DAILYOVERNUM + "人");
				//未交
				var DAILYNONENUM = data.DAILYNONENUM;
				$("#dailyNoneNum").html(DAILYNONENUM + "人");
				//已交
				var DAILYACTNUM = accAdd(data.DAILYACTNUM,data.DAILYOVERNUM);
				$("#dailyActNum").html(DAILYACTNUM + "人");
				
				//及时率
				var percent = 0 ;
				if(DAILYSHOULDNUM == 0){
					percent = 0
				}else{
					percent = accDiv(DAILYACTNUM,DAILYSHOULDNUM);
					percent = accMul(percent,100);
					percent = new Number(percent).toFixed(2);
				}
				$("#dailyOnTimePercent").html(percent + "%");
			},initDailyClick:function(data){
				var urlMod = "/statistics/platform/dailyRepStatistics?sid=${param.sid}&activityMenu=platform_1.9"
				//应交
				var DAILYSHOULDNUM = data.DAILYSHOULDNUM;
				if(DAILYSHOULDNUM > 0){
					$("body").find("#dailyShouldNum").css("cursor","pointer");
					$("body").off("click","#dailyShouldNum").on("click","#dailyShouldNum",function(){
						var url = urlMod;
						window.self.location = url;
					})
				}
				//迟交
				var DAILYOVERNUM = data.DAILYOVERNUM;
				if(DAILYOVERNUM > 0){
					$("body").find("#dailyOverNum").css("cursor","pointer");
					$("body").off("click","#dailyOverNum").on("click","#dailyOverNum",function(){
						var url = urlMod;
						url = url+"&submitState=2"
						window.self.location = url;
					})
				}
				//未交
				var DAILYNONENUM = data.DAILYNONENUM;
				if(DAILYNONENUM > 0){
					$("body").find("#dailyNoneNum").css("cursor","pointer");
					$("body").off("click","#dailyNoneNum").on("click","#dailyNoneNum",function(){
						var url = urlMod;
						url = url+"&submitState=0"
						window.self.location = url;
					})
				}
				//已交
				var DAILYACTNUM = accAdd(data.DAILYACTNUM,data.DAILYOVERNUM);
				if(DAILYACTNUM > 0){
					$("body").find("#dailyActNum").css("cursor","pointer");
					$("body").off("click","#dailyActNum").on("click","#dailyActNum",function(){
						var url = urlMod;
						url = url+"&submitState=1"
						window.self.location = url;
					})
				}
			},optWeek:function(data){//周报
				headDataOpt.initWeekOnClick(data);
				//周数
				var NOWWEEKNUM = data.NOWWEEKNUM;
				$("#weekNumSpan").html(NOWWEEKNUM);
				//应交
				var WEEKSHOULDNUM = data.WEEKSHOULDNUM;
				$("#weekShouldNum").html(WEEKSHOULDNUM + "人");
				//迟交
				var WEEKOVERNUM = data.WEEKOVERNUM;
				$("#weekOverNum").html(WEEKOVERNUM + "人");
				//未交
				var WEEKNONENUM = data.WEEKNONENUM;
				$("#weekNoneNum").html(WEEKNONENUM + "人");
				//已交
				var WEEKACTNUM = accAdd(data.WEEKACTNUM,data.WEEKOVERNUM);
				$("#weekActNum").html(WEEKACTNUM + "人");
				
				//及时率
				var percent = 0 ;
				if(WEEKSHOULDNUM == 0 || WEEKACTNUM == 0 ){
					percent = 0;
				}else{
					percent = accDiv(WEEKACTNUM,WEEKSHOULDNUM);
					percent = accMul(percent,100);
					percent = new Number(percent).toFixed(2);
				}
				$("#weekOnTimePercent").html(percent + "%");
			},initWeekOnClick:function(data){
				var urlMod = "/statistics/platform/statisticWeekReportPage?sid=${param.sid}&activityMenu=platform_2.0"
					urlMod =  urlMod + "&weekNum="+data.NOWWEEKNUM
					urlMod =  urlMod + "&weekYear="+data.NOWWEEKYEAR
				//周数
				var NOWWEEKNUM = data.NOWWEEKNUM;
				$("body").find("#weekNumSpan").css("cursor","pointer");
				$("body").off("click","#weekNumSpan").on("click","#weekNumSpan",function(){
					var url = urlMod;
					window.self.location = url;
				})
				//应交
				var WEEKSHOULDNUM = data.WEEKSHOULDNUM;
				if(WEEKSHOULDNUM > 0){
					$("body").find("#weekShouldNum").css("cursor","pointer");
					$("body").off("click","#weekShouldNum").on("click","#weekShouldNum",function(){
						var url = urlMod;
						window.self.location = url;
					})
				}
				//迟交
				var WEEKOVERNUM = data.WEEKOVERNUM;
				if(WEEKOVERNUM > 0){
					$("body").find("#weekOverNum").css("cursor","pointer");
					$("body").off("click","#weekOverNum").on("click","#weekOverNum",function(){
						var url = urlMod;
						url = url+"&submitState=2"
						window.self.location = url;
					})
				}
				//未交
				var WEEKNONENUM = data.WEEKNONENUM;
				if(WEEKNONENUM > 0){
					$("body").find("#weekNoneNum").css("cursor","pointer");
					$("body").off("click","#weekNoneNum").on("click","#weekNoneNum",function(){
						var url = urlMod;
						url = url+"&submitState=0"
						window.self.location = url;
					})
				}
				//已交
				var WEEKACTNUM = accAdd(data.WEEKACTNUM,data.WEEKOVERNUM);
				if(WEEKNONENUM > 0){
					$("body").find("#weekActNum").css("cursor","pointer");
					$("body").off("click","#weekActNum").on("click","#weekActNum",function(){
						var url = urlMod;
						url = url+"&submitState=1"
						window.self.location = url;
					})
				}
			}
		}
	</script>
</body>