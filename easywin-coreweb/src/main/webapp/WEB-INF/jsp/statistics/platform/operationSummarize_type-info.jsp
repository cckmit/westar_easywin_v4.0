<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<div class="type-item">
		<div class="type-title">
			任务分类 <a href="javascript:void(0);" onclick="menuClick('/statistics/platform/task/statisticSupTask?sid=${param.sid}&activityMenu=platform_1.7');">更多</a>
		</div>
		<div class="type-content" id="statisticTaskGradeDiv">
			<!-- initTable -->
			<div class="type-list type-head">
				<div class="item1">序号</div>
				<div class="item0">类型</div>
				<div class="item no-padding" style="border-bottom:0">任务总数</div>
				<div class="item no-padding" style="border-bottom:0">逾期数</div>
			</div>
		</div>
	</div>
	<div class="type-item">
		<div class="type-title">
			客户分类 <a href="javascript:void(0);" onclick="menuClick('/statistics/platform/statisticCrm?sid=${param.sid}&activityMenu=platform_1.3');">更多</a>
		</div>
		<div class="type-charts" id="statisticCrmTypeDiv"></div>
		<div class="type-charts" id="noneCrmType">
			<div class="task-item" style="line-height:369px;font-size:16px;padding: 0px 16% 0px 23%;">未添加客户分类</div>
		</div>
	</div>
	
	
	<script type="text/javascript">
		var myCrmTypeChart;
		//任务分类异步查询
		var taskGradeUrl = "/statistics/platform/task/statisticTaskGrade?sid=${param.sid}&t="+Math.random()
		var crmTypeUrl = "/statistics/platform/crm/statisticCrmType?sid=${param.sid}&t="+Math.random()
		$(function(){
			// 基于准备好的dom，初始化echarts实例
		    myCrmTypeChart = echarts.init(document.getElementById('statisticCrmTypeDiv'));
			//加载两边的数据
			reloadAllInfoData();
		    window.onresize = myCrmTypeChart.resize;
		})
		function reloadAllInfoData(){
			taskInfoOpt.loadTaskGrade();
			crmOpt.loadCrmType();
		}
		//任务类型
		var taskInfoOpt = {
			loadTaskGrade:function(){//加载数据
				var url = taskGradeUrl;
				getSelfJSON(url, null, function(data) {
					taskInfoOpt.initTable(data);
				});
			},initTable:function(result){//渲染页面
				if(result.code==-1){
					console.log(result.msg);
					return;
				}
				var tyleList = $("#statisticTaskGradeDiv").find(".type-list");
				$.each(tyleList,function(index,object){
					if(!$(object).hasClass("type-head")){
						$(object).remove();
					}
				})
				var lineMod = '<div class="type-list">';
					lineMod = lineMod + '<div class="item1">1</div>';
					lineMod = lineMod + '<div class="item0">类型</div>';
					lineMod = lineMod + '<div class="item no-padding" style="border-bottom:0">23</div>';
					lineMod = lineMod + '<div class="item no-padding" style="border-bottom:0">32</div>';
					lineMod = lineMod + '</div>';
				var listStatisticTaskVo = result.data;
				$.each(listStatisticTaskVo,function(index,object){
					var lineData = $(lineMod).clone();
					$(lineData).find("div:eq(0)").html((index+1));
					$(lineData).find("div:eq(1)").html(object.gradeName);
					$(lineData).find("div:eq(2)").html(object.totalNum);
					$(lineData).find("div:eq(3)").html(object.overNums);
					$("#statisticTaskGradeDiv").append($(lineData))
				})
			}
		}
		
		var crmOpt = {
				loadCrmType:function(){
					var url = crmTypeUrl;
					
					myCrmTypeChart.showLoading();
					getSelfJSON(url, null, function(data) {
						crmOpt.renderChart(data);
						myCrmTypeChart.hideLoading();
						
					});
				},renderChart:function(result){
					if(result.code==-1){
						console.log(result.msg);
						return;
					}
					var listStatisticCrmVo = result.data;
					if(listStatisticCrmVo && listStatisticCrmVo[0]){
						
						
						$("#noneCrmType").remove();
						crmOpt.initPie(listStatisticCrmVo);
						
					}else{
						$("#statisticCrmTypeDiv").remove();
					}
				},initPie:function(listStatisticCrmVo){
					
					var legendArray = new Array();
					var dataArray = new Array();
					
					var total = 0;
					var len = listStatisticCrmVo.length;
					
					var maxNum = 5;
					
					$.each(listStatisticCrmVo,function(index,object){
						var crmTypeNum = object.crmTypeNum;
						if(len<=(maxNum +1)){
							legendArray.push(object.crmTypeName);
							dataArray.push({value:crmTypeNum, name:object.crmTypeName});
						}else{
							if(index<maxNum){
								legendArray.push(object.crmTypeName);
								dataArray.push({value:crmTypeNum, name:object.crmTypeName});
							}else{
								total = total + object.crmTypeNum;
							}
							if(index　==　maxNum){
								legendArray.push('其他');
							}
							if(index == (len - 1)){
								dataArray.push({value:total, name:'其他'});
							}
						}
					})
					
					
					var option = {
						    title : {
						        x:'center'
						    },
						    tooltip : {
						        trigger: 'item',
						        formatter: "{a} <br/>{b} : {c} ({d}%)"
						    },
						    legend: {
						    	x:'right',
					            y:70,
					            align:'left',
					            itemWidth:15,
					            itemGap:15,
					            fontSize:18,
					            orient:'vertical',
						        data: legendArray,
						        formatter:  function(name){
						            var total = 0;
						            var target;
						            for (var i = 0, l = dataArray.length; i < l; i++) {
						            total += dataArray[i].value;
						            if (dataArray[i].name == name) {
						                target = dataArray[i].value;
						                }
						            }
						            var arr = [
						                ''+name+'',
						                ''+((target/total)*100).toFixed(2)+'%',
						                ''+target+''
						            ]
						            return arr.join(" \t\t");
						        }
						    },
						    series : [
						        {
						            name: '客户分类',
						            type: 'pie',
						            radius: ['40%', '60%'],
						            center: ['30%', '50%'],
						            data:dataArray,
						            itemStyle: {
						            	normal: {
						            		borderWidth: 4,
						            		borderColor: '#ffffff',
						            	}, 
						                emphasis: {
						                    shadowBlur: 10,
						                    shadowOffsetX: 0,
						                    shadowColor: 'rgba(0, 0, 0, 0.5)'
						                }
						            }
						        }
						    ]
						};
					//渲染页面
					myCrmTypeChart.setOption(option);
					
				}
		}
		
		
		
	</script>
