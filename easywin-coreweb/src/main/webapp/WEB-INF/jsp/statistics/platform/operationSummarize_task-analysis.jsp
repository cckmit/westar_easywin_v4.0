<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<body>
	<div class="task-head">
		<span>任务分析</span>
		<div class="pull-right" style="line-height: 50px">
			<div class="widget-buttons ps-widget-buttons">
				<button class="btn btn-sm" type="button" taskDateTimeType="day" >
					今日
				</button>
				<button class="btn btn-sm margin-left-5" type="button" taskDateTimeType="week">
					本周
				</button>
				<button class="btn btn-sm margin-left-5 btn-primary btn-info" type="button" taskDateTimeType="month">
					本月
				</button>
				<button class="btn btn-sm margin-left-5" type="button" taskDateTimeType="year">
					全年
				</button>
				<div class="pull-right padding-left-20">
	                <input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="startDate" name="startDate" 
							placeholder="开始时间"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}',onpicked:function(dp){reloadAllData(1);},oncleared:function(){reloadAllData(1);} })"/>
							~
					<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="endDate"  name="endDate"
							placeholder="结束时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}',onpicked:function(dp){reloadAllData(1);},oncleared:function(){reloadAllData(1);} })"/>
				</div>
				
			</div>
		</div>
	</div>
	<div class="task-content">
		<div class="task-charts" id="statisticTaskForDepDiv" style="height: 340px"></div>
		<div class="task-rank"  style="border-left: 1px solid #e9e9e9">
			<div class="task-title no-margin-bottom">任务王</div>
			<div id="topTaskList" class="padding-top-20">
				<!-- 异步获取数据 initTable -->
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		var myChartTaskForDep;
		var statisticTaskForDepUrl = '/statistics/platform/task/statisticTaskForDep?sid=${param.sid}&t='+Math.random();
		var statisticTaskTopUrl = '/statistics/platform/task/statisticTaskTop?sid=${param.sid}&t='+Math.random();
		$(function(){
			//任务分析
			// 基于准备好的dom，初始化echarts实例
		    myChartTaskForDep = echarts.init(document.getElementById('statisticTaskForDepDiv'));
			
			//加载两边的数据
			reloadAllData();
		    
			window.onresize = myChartTaskForDep.resize;
			
			$("body").on("click","[taskDateTimeType]",function(){
				
				$(this).parent().find("button").removeClass("btn-info");
				$(this).parent().find("button").removeClass("btn-primary");
				$(this).addClass("btn-info btn-primary");
				
				$("#startDate").val('');
				$("#endDate").val('');
				
				//加载图形报销数据
				var taskDateTimeType = $(this).attr("taskDateTimeType");
				var url = statisticTaskForDepUrl;
				url = url + "&taskDateTimeType="+taskDateTimeType;
				
				myChartTaskForDep.showLoading();
			    getSelfJSON(url, null, function(data) {
					initChart(data);
					myChartTaskForDep.hideLoading();
				});
			  //加载图形报销数据
			    url = statisticTaskTopUrl;
				url = url + "&taskDateTimeType="+taskDateTimeType;
				
				
			    getSelfJSON(url, null, function(data) {
			    	initTable(data);
				});
			  
			})
			
			//点击事件，跳转到
			myChartTaskForDep.on("click", function(params) {
				var dataInfo = params.data;
				var val = dataInfo.value;
				if(!val || val == 0){
					return false;
				}
				
				var depId = dataInfo.depId;
				var depName = dataInfo.depName;
				var otherUrl = "/statistics/platform/task/statisticSupTask?sid=${param.sid}&activityMenu=platform_1.7";
				otherUrl = otherUrl + "&listExecuteDep[0].id="+depId
				otherUrl = otherUrl + "&listExecuteDep[0].depName="+encodeURIComponent(depName)
				//window.self.location = otherUrl
			})
		})
		
		//加载两边的数据
		function reloadAllData(state){
			if(state){
				$("#startDate").parents(".widget-buttons").find("button").removeClass("btn-info");
				$("#startDate").parents(".widget-buttons").find("button").removeClass("btn-primary");
			}
			//加载图形报销数据
			reloadData();
			//加载任务王数据
			loadToptask()
		}
		//加载图形报销数据
		function reloadData(){
			var url = statisticTaskForDepUrl;
			var startDate = $("#startDate").val();
			if(startDate){
				url = url + "&startDate="+startDate;
			}
			var endDate = $("#endDate").val();
			if(endDate){
				url = url + "&endDate="+endDate;
			}
			myChartTaskForDep.showLoading();
		    getSelfJSON(url, null, function(data) {
				initChart(data);
				myChartTaskForDep.hideLoading();
			});
		}
		//加载任务王数据
		function loadToptask(){
			var url = statisticTaskTopUrl;
			var startDate = $("#startDate").val();
			if(startDate){
				url = url + "&startDate="+startDate;
			}
			var endDate = $("#endDate").val();
			if(endDate){
				url = url + "&endDate="+endDate;
			}
			
		    getSelfJSON(url, null, function(data) {
				initTable(data);
			});
		}
		
		//初始化表单数据
		function initChart(result) {
			if(result.code==-1){
				showNotification(2, result.msg);
				return;
			}
			//客户阶段类型
			var legendArray = new Array();
			//类型统计数据
			var seriesArray = new Array();
			//纵坐标
			var yAxisArray = new Array();

			var listStatisticTaskVo = result.data;
			
			var dataCreateArray = new Array();
			var dataFinishArray = new Array();
			$.each(listStatisticTaskVo,function(index,object){
				yAxisArray.push(object.depName);
				dataCreateArray.push({"value":object.createNum,"depId":object.depId,"depName":object.depName,"taskFlag":"add"});
				dataFinishArray.push({"value":object.finishNum,"depId":object.depId,"depName":object.depName,"taskFlag":"finish"});
			})
			var createsSeries = {
				name: '新增',
				type: 'bar',
				barWidth: 30, //固定柱子宽度
				barMinHeight: 1,
				label: {
					normal: {
						show: true,
						position: 'top'
					}
				},
				data: dataCreateArray
			};
			seriesArray.push(createsSeries);
			
			var finishSeries = {
				name: '完成',
				type: 'bar',
				barWidth: 30, //固定柱子宽度
				barMinHeight: 1,
				label: {
					normal: {
						show: true,
						position: 'top'
						
					}
				},
				data: dataFinishArray
			};
			seriesArray.push(finishSeries);
			
			//全局配置项
			option = {
				tooltip: {
					trigger: 'item',
					axisPointer: { // 坐标轴指示器，坐标轴触发有效
						type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
					}
				},
				legend: {
					data: ['新增','完成']
				},
				dataZoom: {
					show: true
				},
				grid: {
					left: '40px',
					right: '40px',
					bottom: '15%',
					containLabel: true
				},
				color:['#117c11','#ed4e2a'],
				xAxis: {
					type: 'category',
					axisLabel: {
						show: true,
						interval: 0,
						rotate: 0
					},
					data: yAxisArray
				},
				yAxis: {
					type: 'value'
				},
				series: seriesArray
			}
			//根据图例个数取得起始值
			var scopeValue = getDataZoomScope(2,yAxisArray);
			if(scopeValue) {
				option.dataZoom = {}
				option.dataZoom.show = true
				option.dataZoom.start = scopeValue.startValue
				option.dataZoom.end = scopeValue.endValue
			} else {
				option.dataZoom = {}
				option.dataZoom.show = false
			}
			option.dataZoom.height = 20//组件高度
			//渲染页面
			myChartTaskForDep.setOption(option);
		}
		
		//取得显示范围
		function getDataZoomScope(legengLen,yAxisArray) {
			var dataZoomValue = {};
			//取得开头数值
			if(yAxisArray.length == 0) { //没有部门,则不显示滚动条
				return null;
			}

			dataZoomValue['startValue'] = 0
			//取得设定的人员个数
			var userNum = 6;
			//人员总数
			var userLen = yAxisArray.length;
			if(userNum > userLen) { //设定个数大于总数,全部显示
				dataZoomValue['endValue'] = 100
			} else { //人员总数超过设定总数,取设定数
				var value = ((userNum - 1) * 100 / yAxisArray.length);
				dataZoomValue['endValue'] = value
			}
			return dataZoomValue;
		}
		
		//任务王数据显示
		function initTable(result){
			if(result.code==-1){
				showNotification(2, result.msg);
				return;
			}
			$("#topTaskList").find(".task-item").remove();
			var listStatisticTaskVo = result.data;
			if(listStatisticTaskVo && listStatisticTaskVo[0]){
				$("#topTaskList").html('');
				var lineMod = '<div class="task-item">';
				lineMod = lineMod+ '<div class="task-no">7</div>';
				lineMod = lineMod+ '<div class="task-name">张飞</div>';
				lineMod = lineMod+ '<div class="task-sum">22</div>';
				lineMod = lineMod+ '</div>';
				
				$.each(listStatisticTaskVo,function(index,object){
					var lineData = $(lineMod).clone();
					if(index<3){
						$(lineData).find(".task-no").addClass("task-no0")
					}
					$(lineData).find(".task-no").html((index + 1))
					$(lineData).find(".task-name").html(object.userName);
					$(lineData).find(".task-sum").html(object.totalNum);
					$("#topTaskList").append($(lineData));
				})
				$("#topTaskList").css("background-color","");
			}else{
				var lineMod = '<div style="font-size:16px;padding: 90px 6% 0px 23%;">无人员办理任务</div>';
				$("#topTaskList").html($(lineMod));
				$("#topTaskList").css("height","260px")
				$("#topTaskList").css("background-color","#ccc")
			}
		}
	</script>
</body>