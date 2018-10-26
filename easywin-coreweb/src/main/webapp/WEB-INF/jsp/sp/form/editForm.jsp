<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html>
  <head>
    <meta charset="utf-8" />
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<meta name="description" content="Dashboard" />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/easywin.ui.min.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/font-icons.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/colpick.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form-select.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form-view.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/form-typeahead.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/org.css" />
		<link type="text/css" rel="stylesheet" href="/static/plugins/form/css/plugins.min.css" />
		<script src="/static/plugins/form/js/libs.js"></script>
		<script src="/static/plugins/form/js/plugins.js"></script>
		<script type="text/javascript" src="/static/plugins/form/js/colpick.js"></script>
  </head>
  
  <body class="edit edit-form">
  	<input type="hidden" id="formId" value="${formMod.id}">
	<input type="hidden" id="formTenantKey" value="T5Y8PA3O0L">
	<input type="hidden" id="nowTime" value="">
	<input type="hidden" id="introCount" value="0">
	<input type="hidden" id="module" value="biaoge">
	<span id="jsInjectionFilter" class="hide"></span>
	<textarea id="filterName" class="hide" >${formMod.modName }</textarea>
	<textarea id="filterDescription" class="hide">${formMod.modDescrib}</textarea>
	
	<header class="header">
		<figure class="pull-left logo"><a href="javascript:void(0);"></a></figure>
	    <h1 class="pull-left fs-20">自定义表单</h1>
		  <div class="pull-right userbtns">
			<a class="btn btn-sm btn-lake preview_js">预览</a>
			<a class="btn btn-sm submit_js" submitType="enable">
			保存并发布</a>
			</div>
	</header>
	
	
	<section class="js_wrapperSize wrapper clearfix">
		<div class="js_wrapperConSize wrapper-content">
			<div id="form-widget" class="pull-left form-widget">
				<div class="form-lead">
					<ul class="form-lead-tab">
						<li><a class="j_widget_tab" widget-type="layout">布局控件</a></li>
						<li><a class="j_widget_tab active" widget-type="form">字段控件</a></li>
						<!-- 
							<li><a class="j_widget_tab" widget-type="deletefeilds">已删除<span class="ignore">字段</span></a></li>
						 -->
					</ul>
				</div>
				<div id="form-widget-list">
					<div class="j_scroll_sideBar widget-list-left scroll-wrapper">
						<ul class="widget-list bg-white drag-into-layout" >
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Text">
								<span class="title">文本输入框</span>
								<span class="widgetMark"><i class="icon-type"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="TextArea">
								<span class="title">多行文本框</span>
								<span class="widgetMark"><i class="icon-pilcrow"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="RadioBox">
								<span class="title">单选框</span>
								<span class="widgetMark"><i class="icon-radio-checked"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="CheckBox">
								<span class="title">复选框</span>
								<span class="widgetMark"><i class="icon-checkbox-checked"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Select">
								<span class="title">下拉菜单</span>
								<span class="widgetMark"><i class="icon-caret-down"></i></span>
							</div>
							<!-- 
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="ComboSelect">
	                            <span class="title">多级下拉框</span>
	                             <span class="widgetMark"><i class="graph-16-complex"></i></span>
	                        </div>
							 -->
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="DateComponent">
								<span class="title">日期</span>
								<span class="widgetMark"><i class="icon-calendar"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="DateInterval">
								<span class="title">日期区间</span>
								<span class="widgetMark"><i class="icon-calendar-empty"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="NumberComponent">
								<span class="title">数字输入框</span>
								<span class="widgetMark"><i class="icon-sort-numerically"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Monitor">
	                            <span class="title">运算控件</span>
	                             <span class="widgetMark"><i class="icon-spinner2"></i></span> 
	                        </div>
							<!-- 
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Money">
								<span class="title">金额</span>
								<span class="widgetMark"><i class="icon-database"></i></span>
							</div>
							 -->
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Employee">
								<span class="title">用户选择</span>
								<span class="widgetMark"><i class="icon-user-add"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Department">
								<span class="title">部门选择</span>
								<span class="widgetMark"><i class="icon-users"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="DividingLine">
								<span class="title">分割线</span>
								<span class="widgetMark"><i class="icon-minus"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Paragraph">
								<span class="title">描述文字</span>
								<span class="widgetMark"><i class="icon-paragraph-justify"></i></span>
							</div>
							<!-- 
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Email">
								<span class="title">邮箱</span>
								<span class="widgetMark"><i class="icon-mail"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Phone">
								<span class="title">电话</span>
								<span class="widgetMark"><i class="icon-phone"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Mobile">
								<span class="title">手机</span>
								<span class="widgetMark"><i class="icon-mobile"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="FileComponent">
								<span class="title">附件</span>
								<span class="widgetMark"><i class="icon-paperclip"></i></span>
							</div>
							<div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="ImageComponent">
								<span class="title">图片</span>
								<span class="widgetMark"><i class="icon-images"></i></span>
							</div>
							
	                        <div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Raty">
	                            <span class="title">评分控件</span>
	                             <span class="widgetMark"><i class="icon-stack"></i></span> 
	                        </div>
	                        <div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Mainline">
	                            <span class="title">关联项目</span>
	                             <span class="widgetMark"><i class="graph graph-16 graph-16-pie"></i></span> 
	                        </div>
	                        <div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Task">
	                            <span class="title">关联任务</span>
	                             <span class="widgetMark"><i class="graph graph-16 graph-16-task"></i></span> 
	                        </div>
	                        <div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Document">
	                            <span class="title">关联文档</span>
	                             <span class="widgetMark"><i class="graph graph-16 graph-16-document"></i></span>
	                        </div>
	                        <div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="Workflow">
	                            <span class="title">关联审批</span>
	                             <span class="widgetMark"><i class="graph graph-16 graph-16-workflow"></i></span>
	                        </div>
	                        <div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="FormComponent">
	                            <span class="title">关联表单数据</span>
	                             <span class="widgetMark"><i class="graph graph-16 graph-16-form"></i></span>
	                        </div>
	                        <div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="CustomerComponent">
	                            <span class="title">关联客户</span>
	                             <span class="widgetMark"><i class="graph graph-16 graph-16-customer"></i></span>
	                        </div>
	                        <div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="ContactComponent">
	                            <span class="title">客户联系人</span>
	                             <span class="widgetMark"><i class="graph graph-16 graph-16-contact"></i></span>
	                        </div>
	                        <div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="DataSource">
	                            <span class="title">数据源</span>
	                             <span class="widgetMark"><i class="icon-dns"></i></span>
	                        </div>
	                        <div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="ImageRadioBox">
	                            <span class="title">图片单选</span>
	                             <span class="widgetMark"><i class="icon-image3"></i></span>
	                        </div>
	                        
	                        <div class="widget-item general_js" title="拖拽至页面中间区域" componentKey="ImageCheckBox">
	                            <span class="title">图片多选</span>
	                             <span class="widgetMark"><i class="icon-images2"></i></span>
	                        </div>
	                         -->
						</ul>
					</div>
				</div>
				<div id="layout-widget-list" class="hide">
					<div class="j_scrollWrapper">
						<ul class="widget-list widget-layout bg-white">
							<div class="widget-item form-layout_js" title="拖拽至页面中间区域" componentKey="ColumnPanel" componentSize="2">
								<span class="title">一行两列布局</span>
								<span class="widgetMark layout-1"></span>
							</div>
							<div class="widget-item form-layout_js" title="拖拽至页面中间区域" componentKey="ColumnPanel" componentSize="3">
								<span class="title">一行三列布局</span>
								<span class="widgetMark layout-2"></span>
							</div>
							<div class="widget-item subform_js" title="拖拽至页面中间区域" componentKey="DataTable" componentSize="3">
								<span class="title">明细子表</span>
								<span class="widgetMark layout-databox"></span>
							</div>
							<div class="widget-item table_layout_js" title="拖拽至页面中间区域" componentKey="TableLayout">
	                            <span class="title">表格布局控件</span>
	                            <span class="widgetMark layout-databox"></span>
	                        </div>
						</ul>
					</div>
				</div>
				<div id="deletefeilds-widget-list" class="del-widget-list">
					<div class="j_scrollWrapper j_scroll_deletefield">
						<ul class="widget-list widget-layout bg-white j_deletefeild_ul"></ul>
						<div id="no_delete_feild" class="hide c-999 text-center"><span>暂无删除字段</span></div>
					</div>
				</div>
			</div>
			<div id="formContainer_js" class="form-design pull-left">
				<div class="form-view-bg"></div>
				<div class="form-view-wrapper j_scroll_formView scroll-wrapper">
					<div id="formContent" class="form-view form-edit-region">
				      <div class="form-head form-head_js">
							<p class="form-name"></p>
							<div class="form-description"></div>
						</div>
						<div id="widget-control" class="widget-control form-view_js"></div>
					</div>
				</div>
			</div>
			<div id="formEdit_js" class="form-widgetEdit">
				<div class="form-lead">
					<ul class="form-lead-tab">
						<li><a class="j_edit_tab" edit-type="widget">控件设置</a></li>
						<li><a class="j_edit_tab active" edit-type="form">表单设置</a></li>   
		                </ul>
				</div>
				<div id="edit-widget" class="j_scroll_edit scroll-wrapper hide edit-widget">
					<div class="form-Edit-content bg-white">
						<div id="editor-component">
							<div class="alert alert-danger">
								<i class="icon-exclamation-sign"></i> 请先选择控件
							</div>
						</div>
					</div>
				</div>
				<div id="edit-form" class="j_scroll_sideBar scroll-wrapper">
					<div class="form-Edit-content bg-white">
						<div class="form-group">
							<label>标题</label>
							<div class="c-danger"></div>
							<input id="name-form" type="text" class="form-control ds-b w-full" value="${formMod.modName }" maxlength="100">
						</div>
						<div class="form-group">
							<label>描述</label>
							<textarea id="description-form" class="form-control ds-b w-full" maxlength="1000">${formMod.modDescrib}</textarea>
						</div>
						<div class="form-group js_formownership hide">
						    <label class="control-label">表单所属权：</label>
	                          <div class="controls js_formowner">
								<label class="radio-inline">
									<input type="radio" value="company" name="ownership">团队表单
			                          </label>
								<label class="radio-inline">
									<input type="radio" value="personal" name="ownership" checked="checked" >我的表单
	                                  </label>
							</div>
						</div>
						<div class="form-group hide">
							<label>标签</label>
							<div class="form-tag d-inlineb-child">
								<span class="form-tagitem"><span>示例标签</span><a class="btn-removetag">×</a></span>
								<input type="text" class="form-control form-w70">
								<span class="form-addtag"><i class="icon-plus-thin"></i></span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
	
	<div class="form-preview-wrapper hide">
		<div id="form-preview" class="form-preview clearfix">
			<div class="form-view">
				<div class="form-head">
					<p class="form-name"></p>
					<div class="form-description"></div>
					<a class="btn btn-warning btn-sm back-to-edit" onclick='$("#formpreview").parents(".form-preview-wrapper ").addClass("hide");'><i class="glyphicon icon-undo"></i>返回</a>
				</div>
				<div id="formpreview" class="widget-control form-view_js">
					
				</div>
			</div>
		</div>
	</div>
	
	<!-- 选项批量编辑模态窗 -->
	<div id="optBatcEdit" class="modal fade chooselist-batch">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span>
					</button>	
	   		        <i class="icon-help fr mr-10 mt-2 hide" data-toggle="tooltip" data-html="true" data-placement="bottom" data-template="<div class=&quot;tooltip i-tooltip&quot; role=&quot;tooltip&quot;><div class=&quot;tooltip-arrow i-tooltip-arrow&quot;></div><div class=&quot;tooltip-inner i-tooltip-inner&quot;></div></div>"
						 title='<div class="e-row ta-l">1.每个选项请单列显示</div><div class="e-row ta-l">2.二级选项请加前缀"-"</div><div class="e-row ta-l">3.三级选项请加前缀"—"</div><div class="e-row ta-l">4.每个父级选项至少有一个子级选项</div>' >
				    </i>
					<h5 class="modal-title">批量编辑</h5>
				</div>
				<div class="modal-body">
					<p class="mb-10">每个选项请单列一行<span class="j_optBatcEditEr ml-10 c-danger icon-warning-sign hide">不能为空</span></p>
					<div><textarea id="optContent" class="form-control w-full rs-n"></textarea></div>
				</div>
				<div class="modal-footer">
					<a class="btn btn-sm btn-primary j_saveOptBatc">确定</a>
					<a class="btn btn-sm j_close">取消</a>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 关联表单,客户 组件批量编辑模态框 -->
	<div id="fieldBatcEdit" class="modal fade chooselist-batch">
		<div class="modal-dialog w-600">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span>
					</button>
					<h5 class="modal-title">批量编辑</h5>
				</div>
				<div class="modal-body nopd">
				    <p class="mb-10 mb-10 ph-15 pt-15 j_title">下方字段为表单信息中的字段，拖拽字段可以进行排序，以勾选的字段作为表单显示的字段<span class="j_fieldBatcEditEr mt-5 c-danger icon-warning-sign hide"></span></p>
	                  <div class="j_sortfieldScr" height="348">
						<ul class="j_field_ul field-ul ph-15">
							
						</ul>
					</div>
					<div class="j_clone hide">
						<li class="j_field_li checkbox">
						   <label>
							<span class="j_num num"></span>
							<input name="field_select" value="" type="checkbox" />
							<span class="j_field"></span>
							<span class="tip j_bewrite"></span>
						   </label>
						</li>
					</div>
					
				</div>
				<div class="modal-footer">
				    <label class="checkbox-inline  mt-5">
				       <input class="j_select_all" name="select_all" value="" type="checkbox" />全选
				    </label>
					<a class="btn btn-sm fr j_close ml-5">取消</a>
					<a class="btn btn-sm fr btn-primary j_saveBatc">确定</a>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		seajs.config({base:"/static/plugins/form/js",preload:["form.js?v=88f3d7c56c32a31026b3f37492c9287f"],charset:"utf-8",debug:!1}),seajs.use("form/formlayout");  
	</script>
	<script type="text/javascript">
	var sid='${param.sid}'
	var formTag = 'addMod';
	var formKey='${formMod.id}';
	var tempId=0;
	var TEAMS={
			"currentUser":{"userId":"${userInfo.id}",
				"id":"${userInfo.id}",
				"username":"${userInfo.userName}",
				"comId":"${userInfo.comId}",
				"department":{"name":"${userInfo.depName}","id":"${userInfo.depId}"}
			},
			"currentTenant":{"tenantKey":"t5y8pa3o0l"}
	}
	</script>
  </body>
</html>
