<%@page import="com.westar.core.web.InitServlet"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>My JSP 'flowStepcfg.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="/static/plugins/FlowdesignV3_0/Public/css/bootstrap/css/bootstrap.css?2025" rel="stylesheet" type="text/css" />
    <!--[if lte IE 6]>
    <link rel="stylesheet" type="text/css" href="/static/plugins/FlowdesignV3_0/Public/css/bootstrap/css/bootstrap-ie6.css?2025">
    <![endif]-->
    <!--[if lte IE 7]>
    <link rel="stylesheet" type="text/css" href="/static/plugins/FlowdesignV3_0/Public/css/bootstrap/css/ie.css?2025">
    <![endif]-->
    <link href="/static/plugins/FlowdesignV3_0/Public/css/site.css?2025" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="/static/plugins/FlowdesignV3_0/Public/js/flowdesign/flowdesign.css"/>
	<!--select 2-->
	<link rel="stylesheet" type="text/css" href="/static/plugins/FlowdesignV3_0/Public/js/jquery.multiselect2side/css/jquery.multiselect2side.css"/>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
 </head>
<body>
<!-- fixed navbar -->
<div class="navbar navbar-inverse navbar-fixed-top">
  <div class="navbar-inner">
    <div class="container">
       <div class="pull-right">
        <button class="btn btn-info" type="button" id="leipi_save">保存设计</button>
        <!-- <button class="btn btn-danger" type="button" id="leipi_clear">清空连接</button> -->
      </div>
    </div>
  </div>
</div>

<!-- Modal -->
<div id="alertModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3>消息提示</h3>
  </div>
  <div class="modal-body">
    <p>提示内容</p>
  </div>
  <div class="modal-footer">
    <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">我知道了</button>
  </div>
</div>

<!-- attributeModal -->
<div id="attributeModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="width:800px;margin-left:-350px">
  <div class="modal-body" style="max-height:500px;"><!-- body --></div>
  <div class="modal-footer" style="padding:5px;">
    <a href="http://www.leipi.org" target="_blank"><img src="http://www.leipi.org/wp-content/themes/leipi/images/leipi.png" alt="雷劈认证 icon" style="width:40px"></a>
    <!--a href="#" class="btn btn-danger" data-dismiss="modal" aria-hidden="true"><i class="icon-remove icon-white"></i></a-->
  </div>
</div>



<!--contextmenu div-->
<div id="processMenu" style="display:none;">
  <ul>
    <!--li id="pm_begin"><i class="icon-play"></i>&nbsp;<span class="_label">设为第一步</span></li-->
    <!--li id="pm_addson"><i class="icon-plus"></i>&nbsp;<span class="_label">添加子步骤</span></li-->
    <!--li id="pm_copy"><i class="icon-check"></i>&nbsp;<span class="_label">复制</span></li-->
    <li id="nextStep"><i class="icon-cog"></i>&nbsp;<span class="_label">下一节点</span></li>
    <li id="stepReplace"><i class="icon-th"></i>&nbsp;<span class="_label">替换节点</span></li>
    <li id="inCondition"><i class="icon-share-alt"></i>&nbsp;<span class="_label">转入条件</span></li>
    <li id="stepAttr"><i class=" icon-wrench"></i>&nbsp;<span class="_label">属性配置</span></li>
    <li id="stepDel"><i class="icon-trash"></i>&nbsp;<span class="_label">删除节点</span></li>
    
  </ul>
</div>
<div id="canvasMenu" style="display:none;">
  <ul>
    <li id="cmSave"><i class="icon-ok"></i>&nbsp;<span class="_label">保存设计</span></li>
    <li id="cmAdd"><i class="icon-plus"></i>&nbsp;<span class="_label">添加步骤</span></li>
    <li id="cmRefresh"><i class="icon-refresh"></i>&nbsp;<span class="_label">刷新 F5</span></li>
    <!--li id="cmPaste"><i class="icon-share"></i>&nbsp;<span class="_label">粘贴</span></li-->
    <li id="cmHelp"><i class="icon-search"></i>&nbsp;<span class="_label">帮助</span></li>
  </ul>
</div>
<!--end div--> 

<div class="container mini-layout" id="flowdesign_canvas">
    <!--div class="process-step btn" style="left: 189px; top: 340px;"><span class="process-num badge badge-inverse"><i class="icon-star icon-white"></i>3</span> 步骤3</div-->
</div> <!-- /container -->
    
<div class="navbar navbar-fixed-bottom" style="color:#666;text-align:right;padding-right:10px">
  <a href="http://www.leipi.org" title="雷劈网"><img src="http://www.leipi.org/wp-content/themes/leipi/images/leipi.png" alt="雷劈认证 icon" style="height:25px"></a> &copy;2014 Flowdesign.leipi.org <a href="http://www.miitbeian.gov.cn/" target="_blank"  style="color:#666">粤ICP备13051130号</a>、<a target="_blank" style="color:#f30" href="http://www.leipi.org/commit-code/"><i class="icon-leaf icon-white"></i>我要贡献示例</a>
</div>

<script type="text/javascript" src="/static/plugins/FlowdesignV3_0/Public/js/jquery-1.7.2.min.js?2025"></script>
<script type="text/javascript" src="/static/plugins/FlowdesignV3_0/Public/css/bootstrap/js/bootstrap.min.js?2025"></script>
<script type="text/javascript" src="/static/plugins/FlowdesignV3_0/Public/js/jquery-ui/jquery-ui-1.9.2-min.js?2025" ></script>
<script type="text/javascript" src="/static/plugins/FlowdesignV3_0/Public/js/jsPlumb/jquery.jsPlumb-1.3.16-all-min.js?2025"></script>
<script type="text/javascript" src="/static/plugins/FlowdesignV3_0/Public/js/jquery.contextmenu.r2.js?2025"></script>
<!--select 2-->
<script type="text/javascript" src="/static/plugins/FlowdesignV3_0/Public/js/jquery.multiselect2side/js/jquery.multiselect2side.js?2025" ></script>
<!--flowdesign-->
<script type="text/javascript" src="/static/plugins/FlowdesignV3_0/Public/js/flowdesign/leipi.flowdesign.v3.js?2025"></script>
<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
//去除页面的默认右键点击事件
$(document).bind("contextmenu",function(){return false;});
var the_flow_id = '4';

/*页面回调执行    callbackSuperDialog
    if(window.ActiveXObject){ //IE  
        window.returnValue = globalValue
    }else{ //非IE  
        if(window.opener) {  
            window.opener.callbackSuperDialog(globalValue) ;  
        }
    }  
    window.close();
*/
function callbackSuperDialog(selectValue){
     var aResult = selectValue.split('@leipi@');
     $('#'+window._viewField).val(aResult[0]);
     $('#'+window._hidField).val(aResult[1]);
    //document.getElementById(window._hidField).value = aResult[1];
    
}
/**
 * 弹出窗选择用户部门角色
 * showModalDialog 方式选择用户
 * URL 选择器地址
 * viewField 用来显示数据的ID
 * hidField 隐藏域数据ID
 * isOnly 是否只能选一条数据
 * dialogWidth * dialogHeight 弹出的窗口大小
 */
function superDialog(URL,viewField,hidField,isOnly,dialogWidth,dialogHeight)
{
    dialogWidth || (dialogWidth = 620)
    ,dialogHeight || (dialogHeight = 520)
    ,loc_x = 500
    ,loc_y = 40
    ,window._viewField = viewField
    ,window._hidField= hidField;
    // loc_x = document.body.scrollLeft+event.clientX-event.offsetX;
    //loc_y = document.body.scrollTop+event.clientY-event.offsetY;
    if(window.ActiveXObject){ //IE  
        var selectValue = window.showModalDialog(URL,self,"edge:raised;scroll:1;status:0;help:0;resizable:1;dialogWidth:"+dialogWidth+"px;dialogHeight:"+dialogHeight+"px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
        if(selectValue){
            callbackSuperDialog(selectValue);
        }
    }else{  //非IE 
        var selectValue = window.open(URL, 'newwindow','height='+dialogHeight+',width='+dialogWidth+',top='+loc_y+',left='+loc_x+',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');  
    
    }
}

var flowProcess;

$(function(){
    var alertModal = $('#alertModal'),attributeModal =  $("#attributeModal");
    //消息提示
    mAlert = function(messages,s)
    { 
        if(!messages) messages = "";
        if(!s) s = 2000;
        alertModal.find(".modal-body").html(messages);
        alertModal.modal('toggle');
        setTimeout(function(){alertModal.modal("hide")},s);
    }

    //属性设置
    attributeModal.on("hidden", function() {
        $(this).removeData("modal");//移除数据，防止缓存
    });
    ajaxModal = function(url,fn)
    {
        url += url.indexOf('?') ? '&' : '?';
        url += '_t='+ new Date().getTime();
        attributeModal.find(".modal-body").html('<img src="Public/images/loading.gif"/>');
        attributeModal.modal({
            remote:url
        });
        
        //加载完成执行
        if(fn)
        {
            attributeModal.on('shown',fn);
        }

      
    }
    //刷新页面
    function page_reload()
    {
        location.reload();
    }
    
    /*
    php 命名习惯 单词用下划线_隔开
    js 命名习惯：首字母小写 + 其它首字线大写
    */
    /*步骤数据*/
    var processData =JSON.parse("${process}".replace(/\'/g, '"').replace(/\orgHighInit/g, ''+(document.body.clientHeight/2)+'px'));
    /*创建流程设计器*/
    var _canvas = $("#flowdesign_canvas").Flowdesign({
                      "processData":processData
                      /*,mtAfterDrop:function(params)
                      {
                          //alert("连接："+params.sourceId +" -> "+ params.targetId);
                      }*/
                      /*画面右键*/
                      ,canvasMenus:{
                        "cmAdd": function(t) {
                            var mLeft = $("#jqContextMenu").css("left"),mTop = $("#jqContextMenu").css("top");
							
							/*重要提示 start*/
							alert("这里使用ajax提交，请参考官网示例，可使用Fiddler软件抓包获取返回格式");
							/*重要提示 end */

                            var url = "/index.php?s=/Flowdesign/add_process.html";
                            $.post(url,{"flow_id":the_flow_id,"left":mLeft,"top":mTop},function(data){
                                
                                if(!data.status)
                                {
                                    mAlert(data.msg);
                                }else if(!_canvas.addProcess(data.info))//添加
                               {
                                    mAlert("添加失败");
                               }
                               
                            },'json');

                        },
                        "cmSave": function(t) {
                            var processInfo = _canvas.getProcessInfo();//连接信息

							/*重要提示 start*/
							alert("这里使用ajax提交，请参考官网示例，可使用Fiddler软件抓包获取返回格式");
							/*重要提示 end */

                            var url = "/index.php?s=/Flowdesign/save_canvas.html";
                            $.post(url,{"flow_id":the_flow_id,"process_info":processInfo},function(data){
                                mAlert(data.msg);
                            },'json');
                        },
                         //刷新
                        "cmRefresh":function(t){
                            location.reload();//_canvas.refresh();
                        },
                        /*"cmPaste": function(t) {
                            var pasteId = _canvas.paste();//右键当前的ID
                            if(pasteId<=0)
                            {
                              alert("你未复制任何步骤");
                              return ;
                            }
                            alert("粘贴:" + pasteId);
                        },*/
                        "cmHelp": function(t) {
                            mAlert('<ul><li><a href="http://flowdesign.leipi.org/doc.html" target="_blank">流程设计器 开发文档</a></li><li><a href="http://formdesign.leipi.org/doc.html" target="_blank">表单设计器 开发文档</a></li><li><a href="http://formdesign.leipi.org/demo.html" target="_blank">表单设计器 示例DEMO</a></li></ul>',20000);
                        }
                       
                      }
                      /*步骤右键*/
                      ,processMenus: {
                          /*
                          "pmBegin":function(t)
                          {
                              var activeId = _canvas.getActiveId();//右键当前的ID
                              alert("设为第一步:"+activeId);
                          },
                          "pmAddson":function(t)//添加子步骤
                          {
                                var activeId = _canvas.getActiveId();//右键当前的ID
                          },
                          "pmCopy":function(t)
                          {
                              //var activeId = _canvas.getActiveId();//右键当前的ID
                              _canvas.copy();//右键当前的ID
                              alert("复制成功");
                          },*/
                          "stepDel":function(t)
                          {
                              if(confirm("你确定删除步骤吗？"))
                              {
                                    var activeId = _canvas.getActiveId();//右键当前的ID

									/*重要提示 start*/
									alert("这里使用ajax提交，请参考官网示例，可使用Fiddler软件抓包获取返回格式");
									/*重要提示 end */

                                    var url = "/index.php?s=/Flowdesign/delete_process.html";
                                    $.post(url,{"flow_id":the_flow_id,"process_id":activeId},function(data){
                                        if(data.status==1)
                                        {
                                            //清除步骤
                                            //_canvas.delProcess(activeId);
                                            //清除连接   暂时先保存设计 + 刷新 完成
                                            var processInfo = _canvas.getProcessInfo();//连接信息
                                            var url = "/index.php?s=/Flowdesign/save_canvas.html";
                                            $.post(url,{"flow_id":the_flow_id,"process_info":processInfo},function(data){
                                                location.reload();
                                            },'json');
                                            
                                        }
                                        mAlert(data.msg);
                                    },'json');
                              }
                          },
                          "nextStep":function(t)
                          {
                              var activeId = _canvas.getActiveId();//右键当前的ID
					          var processInfo = _canvas.getProcessInfo();//连接信息
							  //先打开人员选择页面，当点击确认后，启动回调函数进行画图更新
							 directUsersSelect("${param.sid}",activeId,processInfo);
                             //var url = "/index.php?s=/Flowdesign/attribute/id/"+activeId+".html";
							   //var url = '/Public/js/flowdesign/attribute.html?id='+activeId;
                               //ajaxModal(url,function(){
                                //alert('加载完成执行')
                              // });
                          },
                          "stepReplace": function(t) {
                                var activeId = _canvas.getActiveId();//右键当前的ID

								/*重要提示 start*/
								alert("这里使用ajax提交，请参考官网示例，可使用Fiddler软件抓包获取返回格式");
								/*重要提示 end */

                                var url = "/index.php?s=/Flowdesign/attribute/op/form/id/"+activeId+".html";
                                ajaxModal(url,function(){
                                    //alert('加载完成执行')
                                });
                          },
                          "inCondition": function(t) {
                                var activeId = _canvas.getActiveId();//右键当前的ID

								/*重要提示 start*/
								alert("这里使用ajax提交，请参考官网示例，可使用Fiddler软件抓包获取返回格式");
								/*重要提示 end */

                                var url = "/index.php?s=/Flowdesign/attribute/op/judge/id/"+activeId+".html";
                                ajaxModal(url,function(){
                                    //alert('加载完成执行')
                                });
                          },
                          "stepAttr": function(t) {
                                var activeId = _canvas.getActiveId();//右键当前的ID

								/*重要提示 start*/
								alert("这里要使用程序处理，并非简单html页面，如果无法显示，请建立虚拟站点");
								/*重要提示 end */
								
                                //var url = "/index.php?s=/Flowdesign/attribute/op/style/id/"+activeId+".html";
								var url = 'Public/js/flowdesign/attribute.html?id='+activeId;
                                ajaxModal(url,function(){
                                    //alert('加载完成执行')
                                });
                          }
                      }
                      ,fnRepeat:function(){
                        //alert("步骤连接重复1");//可使用 jquery ui 或其它方式提示
                        mAlert("步骤连接重复了，请重新连接");
                        
                      }
                      ,fnClick:function(){
                          var activeId = _canvas.getActiveId();
                          mAlert("查看步骤信息 " + activeId);
                      }
                      ,fnDbClick:function(){
                          //和 nextStep 一样
                          var activeId = _canvas.getActiveId();//右键当前的ID

						  /*重要提示 start*/
							alert("这里使用ajax提交，请参考官网示例，可使用Fiddler软件抓包获取返回格式");
							/*重要提示 end */

                              var url = "/index.php?s=/Flowdesign/attribute/id/"+activeId+".html";
                              ajaxModal(url,function(){
                                //alert('加载完成执行')
                              });
                      }
                  });
    //开始按钮配置时所需参数
    flowProcess = _canvas.getProcessInfo();//连接信息
    
    /*保存*/
    $("#leipi_save").bind('click',function(){
    	var processInfo = JSON.parse(_canvas.getProcessInfo());//连接信息
    	var steps = processInfo["steps"];
    	//流程步骤数组
    	var flowSteps  =new Array();
    	for(var i=0;i<steps.length;i++){
    		var newStep = {'stepKey':steps[i],'top':processInfo[steps[i]]["top"],
    				'left':processInfo[steps[i]]["left"],'process_to':processInfo[steps[i]]["process_to"]};
    		flowSteps.push(newStep);
    	}
    	layer.prompt({
   		  title: "取个名称",
   		  formType: 0 //prompt风格，支持0-2
   		}, function(processName,index){
   			//layer.msg('演示完毕！您的名称：'+ processName );
	   		var url = "/flowDesign/flowCfgSave?sid=${param.sid}";
	        $.post(url,{"flowSteps":JSON.stringify(flowSteps),"processName":processName},function(data){
	        	if(data.succ){
	            	layer.msg(data.msg);
		   			layer.close(index);
	            }else{
	            	layer.alert("创建失败！"); 
	            }
	        },'json');
   		});
    });
    /*清除*/
    $("#leipi_clear").bind('click',function(){
        if(_canvas.clear())
        {
            //alert("清空连接成功");
            mAlert("清空连接成功，你可以重新连接");
        }else
        {
            //alert("清空连接失败");
            mAlert("清空连接失败");
        }
    });


  
});
/**
 * 开始节点配置下步骤
 */
function stepOfStart(){
	//先打开人员选择页面，当点击确认后，启动回调函数进行画图更新
	directUsersSelect("${param.sid}","firstStep",flowProcess);
}
/**
 * 节点选择下一步办理人后的回调函数
 */
function directUsersSelectCallBack(params){
	//被点击步骤主键
	var stepKey = params.stepKey;
	//流程原始布局参数
	var processInfo = JSON.parse(params.processInfo);
	//节点部署人员节点
	var users = params.users;
	//被点击步骤对象
	var activity_step = processInfo[stepKey];
	//获取当前被点击节点的process_to数组
	var org_process_to = activity_step["process_to"];
	//被点击节点的process_to定位
	activity_step["process_to"]=[];
	var steps = processInfo["steps"];
	for(var i=0;i<users.length;i++){
		var uuid="";
		//UUID生成
		$.ajax({  
	        url : "/flowDesign/uuid?sid=${param.sid}",  
	        async : false, // 注意此处需要同步，因为返回完数据后，下面才能让结果的第一条selected  
	        type : "POST",  
	        dataType : "text", 
	        success : function(data) { 
	        	uuid="step-"+data;
	        }  
	    });
		processInfo[uuid+"_"+users[i]] = {"top":(document.body.clientHeight/2),"left":(activity_step["left"]+200),"process_to":org_process_to};
		steps.push(uuid+"_"+users[i]);
		activity_step["process_to"].push(uuid+"_"+users[i]);
	}
	processInfo["endStep"]["left"] = processInfo["endStep"]["left"]+200;
	processInfo["steps"]=[];
	processInfo["steps"]=steps;
	//构建流程新布局
	var newProcessInfo = {};
	newProcessInfo["total"]= steps.length;
	//流程步骤数组
	var flowSteps  =new Array();
	for(var i=0;i<steps.length;i++){
		var aStepKey = steps[i];
		var aProcess_to = processInfo[steps[i]]["process_to"];
		var aLeft = processInfo[steps[i]]["left"];
		var aTop = processInfo[steps[i]]["top"];
		var newStep;
		if("firstStep"==aStepKey){
			newStep = {'id':aStepKey,'process_name':'开始','process_to':''+aProcess_to+'',
					'icon':'icon-ok','style':'width:100px;height:30px;line-height:30px;color:#0e76a8;left:'+aLeft+';top:'+aTop+';'};
		}else if("endStep"==aStepKey){
			newStep = {'id':aStepKey,'process_name':'结束','process_to':''+aProcess_to+'',
					'icon':'icon-ok','style':'width:100px;height:30px;line-height:30px;color:#0e76a8;left:'+aLeft+';top:'+aTop+';'};
		}else{
			var userName ="";
			//ajax获取用户信息
			$.ajax({  
		        url : "/userInfo/userDetail?sid=${param.sid}",  
		        async : false, // 注意此处需要同步，因为返回完数据后，下面才能让结果的第一条selected  
		        type : "POST",  
		        dataType : "json", 
		        data:{userId:aStepKey.split("_")[1]},
		        success : function(data) {   
		        	userName = data.user.userName;
		        }  
		    });
			newStep = {'id':aStepKey,'process_name':''+userName+'','process_to':''+aProcess_to+'',
			'icon':'icon-ok','style':'width:100px;height:30px;line-height:30px;color:#0e76a8;left:'+aLeft+';top:'+aTop+';'};
		}
		flowSteps.push(newStep);
	}
	newProcessInfo["list"]= flowSteps;
	//alert(JSON.stringify(newProcessInfo));
	window.location.href="/flowDesign/flowStepCfg?sid=${param.sid}&process="+encodeURIComponent(JSON.stringify(newProcessInfo).replace(/\"/g,"'"));
}
</script>
  </body>
</html>
