<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.PaginationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!--待办事项栏目-->

<div class="${param.width eq '1'?'col-lg-12':'col-lg-6'} col-sm-12 col-xs-12 indexVoDiv" >
	<input type="hidden" id="width" value="${param.width }">
	<div class="widget radius-bordered">
		<div class="widget-header bg-themeprimary" >
			<i class="widget-icon fa fa-calendar"></i>
			<span class="widget-caption ">下属逾期任务统计</span>
			<div class="widget-buttons">
				<a data-toggle="config" href="javascript:void(0)" onclick="menuSet('${param.sid}','home')">
					<i class="fa fa-cog"></i>
				</a>
				<a data-toggle="collapse" href="javascript:void(0)">
					<i class="fa fa-minus"></i>
				</a>
				<a data-toggle="dispose" href="javascript:void(0)" onclick="closeMenuHeadSet(this,'${param.sid}','00305')">
					<i class="fa fa-times "></i>
				</a>
			</div>
		</div>
		<div class="widget-body orders-container ps-height"  >
                <div class="panel-body" style="text-align: center;"  >
                <div id="main"   style="height:240px ;" >
                
                
                </div>
			 	<div class="col-lg-12 col-sm-12" >
					<a class="btn btn-default btn-xs margin-top-10" href="javascript:void(0);" onclick="menuClick('/task/listTaskOfAllPage?pager.pageSize=10&sid=${param.sid}&activityMenu=task_m_1.5');">
					<i class="fa fa-eye"></i>
					查看更多
					</a>
				</div>
              </div>
		</div>
	</div>
</div>
<script type="text/javascript" charset="utf-8" src="/static/plugins/echarts/echarts.js"></script>
<script type="text/javascript">
// 基于准备好的dom，初始化echarts实例
   var myChart = echarts.init(document.getElementById('main'));
   var url = '/loadIndexLanmu?sid=${param.sid}&busType=00305';
   //缓存数据用户穿透
   var caseData = {};
	    
//异步取得数据
     getSelfJSON(url,null,function (data) {
	   initChart(data);
	});
   	 window.onresize = myChart.resize;
       
        function getRandom(min, max){
            var r = Math.random() * (max - min);
            var re = Math.round(r + min);
            re = Math.max(Math.min(re, max), min)
             
            return re;
        }
	  
   //初始化表单数据
   function initChart(data){
		var indicatorArray = new Array();
		//类型
		var legendArray = new Array();
		//类型统计数据
		var dataArray = new Array();
	
	    var listBusReport = data.listBusReport;
	   
	   $.each(listBusReport,function(index,obj){
		   indicatorArray.push({text: obj.name})
		    //图例
    		legendArray.push(obj.name);
	   		dataArray.push(obj.value);
	   		caseData[obj.name]=obj.type;
		  });
	   
	    var coler = ['#2f4554', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3'];
	    var random = getRandom(0, 9);
	    var colorArray = new Array();
	    colorArray.push(coler[random]);
	    
	 	 option = {
	    	color:colorArray,
	        tooltip : {
	        	 trigger: 'item'
	            //trigger: 'axis',
	           // axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            //    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	            //}
	        },
	        grid: {
	        	y:18,
	        	left: '1%',
	            right: '1%',
	            containLabel: true
	        },
	        xAxis : [
	            {
	                type : 'category',
	                data : legendArray,
	                axisLabel :{  
	    	        	show:true,
	    	        	 interval: 0,
	    	        	 rotate: -20
	    	        },
	                axisTick: {
	                    alignWithLabel: true
	                }
	            }
	        ],
	        yAxis : [
	            {
	                type : 'value'
	            }
	        ],
	        series : [
	            {
	                name:'逾期数',
	                type:'bar',
	                barWidth: '28',
	                barMinHeight :1,
	             	radius: '100%',
	                label: {
	                    normal: {
	                        show: true,
	                        position: 'top'
	                    }
	                },
	                data:dataArray
	            }
	        ],
	    }
	 	 //图例个数
     	 var legengLen = legendArray.length;
     	 //根据图例个数取得起始值
     	 var scopeValue = getDataZoomScope(legengLen);
     	 if(scopeValue){
     		 option.dataZoom ={}
 	    	 option.dataZoom.show =true
     		 option.dataZoom.startValue=scopeValue.startValue
     		 option.dataZoom.endValue=scopeValue.endValue
     	 }else{
     		 option.dataZoom ={}
 	    	 option.dataZoom.show =false 
     	 }
 	  	//渲染页面
 	    myChart.setOption(option); 
   }
   
   
   //取得显示范围
   function getDataZoomScope(legengLen){
	  var dataZoomValue={};
	  if((legengLen<=10 && $("#width").val()==0) ||(legengLen<=20 &&$("#width").val()==1)){
	  return null;
	  }
	 //取得开头数值
  	   var tempStartIndex = 0;
	  //取首值
	   $.each(caseData,function(name,type){
		 if(tempStartIndex==0){
		 dataZoomValue['startValue']=name
		 return false;
	 }
	 })
 //取得结尾数值 
	if($("#width").val()==1){
		var userNum = 20;
		 var tempEndIndex = 0;
	  		 $.each(caseData,function(name,type){
	  			 tempEndIndex ++;
			 if(tempEndIndex==userNum){
				 dataZoomValue['endValue']=name
				 return false;
			 }
		 })
	}else{
		var userNum = 10;
		var tempEndIndex = 0;
	  		 $.each(caseData,function(name,type){
	  			 tempEndIndex ++;
			 if(tempEndIndex==userNum){
				 dataZoomValue['endValue']=name
				 return false;
			 }
		 })
	}	 
		   return dataZoomValue;
	}
   
   //点击事件，跳转到
      myChart.on("click",function(params){
      	var executor =  caseData[params.name];
      	var ctrState = '';
      		
		var otherUrl = "/task/listTaskOfAllPage?sid=${param.sid}&pager.pageSize=10&activityMenu=task_m_1.5";
	 	    otherUrl +="&listExecutor[0].id="+executor;
	 	    otherUrl +="&listExecutor[0].userName="+encodeURIComponent(params.name);
	 	    
	 	    otherUrl +="&overdue=true";
	 	    otherUrl +="&ctrState="+ctrState;
	 	    otherUrl +="&state=1";
	 	    window.self.location=otherUrl;
			
	      })
    </script>
