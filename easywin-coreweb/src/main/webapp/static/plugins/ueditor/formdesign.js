/*
* 设计器私有的配置说明 
* 一
* UE.easyWinFormDesignUrl  插件路径
* 
* 二
*UE.getEditor('myFormDesign',{
*          toolleipi:true,//是否显示，设计器的清单 tool
*/
UE.easyWinFormDesignUrl = 'formdesign';
/**
 * 文本框
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['text'] = function () {
	var me = this,thePlugins = 'text';
	me.commands[thePlugins] = {
		execCommand:function () {//执行添加控件命令
			var layoutDetail = $("#preFormComponent").data("layoutDetail");
			
			var _input  = $("<input type='text' style='width:50px;' placeholder='文本框'/>");
			_input.attr("easyWinCompon","Text");
			_input.attr("readonly",true);
			_input.attr("conf_size","medium");
			
			var tempId = "field_"+new Date().getTime();
			if(layoutDetail){
				constrPreData(layoutDetail,_input);
				
				var tempIdT = layoutDetail.tempId;
				tempId = "field_"+tempIdT;
				_input.attr("tempId",tempId)
				var required = layoutDetail.required;
				if(required && required=='true'){
					var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
					UE.getEditor('editor').execCommand('insertHtml',$(_spanRequire).prop("outerHTML"));
				}
				UE.getEditor('editor').execCommand('insertHtml',$(_input).prop("outerHTML"));
				
			}else{
				
				_input.attr("tempId",tempId)
				_input.attr("conf_name","文本框");
				_input.attr("conf_require","false");//必填
				UE.getEditor('editor').execCommand('insertHtml',$(_input).prop("outerHTML"))
			}
			
			var range = this.selection.getRange();
			var commonAnchorEl = range.getCommonAncestor();
			var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
			$(anchorEl).css("width","60%");
			confConpone(anchorEl,thePlugins);//编辑控件
		},constrSubHtml:function(type,obj){
			var tempId = "field_"+new Date().getTime();
			var _input  = $("<input type='text' style='width:50px;' placeholder='文本框'/>");
			_input.attr("easyWinCompon","Text");
			_input.attr("readonly",true);
			_input.attr("conf_size","medium");
			_input.attr("tempId",tempId)
			_input.attr("conf_name",obj.title);
			
			_input.attr("conf_require","false");//必填
			
			_input.attr("conf_tableColumn",obj.column);//关联字段
			
			_input.css("width","60%");
			return $(_input).prop("outerHTML");
		}
	};
	var constrPreData = function(layoutDetail,_input){
		var componKey = layoutDetail.componentKey;
    	var fieldId = layoutDetail.fieldId;
    	var tempId = layoutDetail.tempId;
    	
    	var required = layoutDetail.required;
    	var title = layoutDetail.title;
    	
    	_input.attr("fieldId",fieldId)
		_input.attr("conf_name",title);
		_input.attr("conf_require",required);//必填
		_input.attr("placeholder",title);
		
		
	}
	//提示框
	var popup = new baidu.editor.ui.Popup({
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			  confConpone(popup.anchorEl,thePlugins);//编辑控件
			  this.hide();//隐藏操作
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				var pre = $(this.anchorEl).prev();
				if(pre && $(pre).prop("tagName")){
					$(pre).hasClass("tempClz")?$(pre).remove():"";	
				}
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener( 'mouseover', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /input/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				var html = popup.formatHtml(
				'<nobr>文本框: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				
				if(el.getAttribute('conf_tableColumn')){
					html = popup.formatHtml(
					'<nobr>文本框: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
					
				}
				if ( html ) {
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor(popup.anchorEl ,100);
				} else {
					popup.hide();
				}
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	me.addListener( 'click', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /input/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				confConpone(el,thePlugins);//编辑控件
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	var confConpone = function(_input,thePlugins){
		var confHtml= '<div class="form-title-mm">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">标题</p>';
			confHtml+='		</div>';
			confHtml+='		<div class="form-text-mm">';
			confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
			confHtml+='		</div>';
			confHtml+='</div>';
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="checkbox">';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confRequire" type="checkbox">';
			confHtml+='				<span class="text">这个是必填项</span>';
			confHtml+='			</label>';
			confHtml+='		</div>';
			confHtml+='</div>';
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">组件宽度</p>';
			confHtml+='		</div>';
			confHtml+='		<div class="btn-group confSizeDiv">';
			confHtml+='			<a href="javascript:void(0)" class="btn btn-default">小尺寸</a>';
			confHtml+='			<a href="javascript:void(0)" class="btn btn-default">标准尺寸</a>';
			confHtml+='			<a href="javascript:void(0)" class="btn btn-default">大尺寸</a>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
		var confObj = $(confHtml);
		
		$(confObj).find(".confTitle").attr("value",$(_input).attr("conf_name"));
		if($(_input).attr("conf_require") && $(_input).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(_input).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(_input).before("<span style='color:red' class='tempClz'>*</span>");	
			}
			
		}
		
		var conf_size = $(_input).attr("conf_size");
		conf_size = conf_size?conf_size:"medium";
		
		if(conf_size == 'small'){
			$(confObj).find(".confSizeDiv").find("a:eq(0)").removeClass("btn-default").addClass("btn-blue");
		}else if(conf_size == 'medium'){
			$(confObj).find(".confSizeDiv").find("a:eq(1)").removeClass("btn-default").addClass("btn-blue");
		}else if(conf_size == 'large'){
			$(confObj).find(".confSizeDiv").find("a:eq(2)").removeClass("btn-default").addClass("btn-blue");
		}
		
		$("#formConfDiv").html(confObj);
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_input).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_input).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_input).before("<span style='color:red' class='tempClz'>*</span>")
				$(_input).attr("conf_require","true")
			}else{
				$(_input).attr("conf_require","false")
			}
		})
		$(confObj).on("click",".confSizeDiv>a",function(){
			
			$(confObj).find(".confSizeDiv").find("a").removeClass("btn-blue");
			$(confObj).find(".confSizeDiv").find("a").removeClass("btn-default");
			$(confObj).find(".confSizeDiv").find("a").addClass("btn-default");
			$(this).removeClass("btn-default").addClass("btn-blue");
			var index = $(this).index();
			if(index==0){
				$(_input).css("width","30%");
				$(_input).attr("conf_size","small");
			}else if(index==1){
				$(_input).css("width","60%");
				$(_input).attr("conf_size","medium");
			}else if(index==2){
				$(_input).css("width","95%");
				$(_input).attr("conf_size","large");
			}
		})
		
		baidu.editor.plugins[thePlugins].editdom = _input;
	}
};
/**
 * 数字文本框
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['numbercomponent'] = function () {
	var me = this,thePlugins = 'numbercomponent';
	me.commands[thePlugins] = {
			execCommand:function () {//执行添加控件命令
				
				var layoutDetail = $("#preFormComponent").data("layoutDetail");
				
				var _input  = $("<input type='text' style='width:50px' placeholder='数字框'/>");
				_input.attr("easyWinCompon","NumberComponent");
				_input.attr("readonly",true);
				_input.attr("conf_size","medium");
				
				var tempId = "field_"+new Date().getTime();
				if(layoutDetail){
					constrPreData(layoutDetail,_input);
					
					var tempIdT = layoutDetail.tempId;
					tempId = "field_"+tempIdT;
					_input.attr("tempId",tempId)
					var required = layoutDetail.required;
					if(required && required=='true'){
						var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
						UE.getEditor('editor').execCommand('insertHtml',$(_spanRequire).prop("outerHTML"));
					}
					UE.getEditor('editor').execCommand('insertHtml',$(_input).prop("outerHTML"));
					
				}else{
					_input.attr("tempId",tempId)
					
					_input.attr("conf_name","数字框");
					_input.attr("conf_require","false");
					
					UE.getEditor('editor').execCommand('insertHtml',$(_input).prop("outerHTML"))
					
				}
				
				var range = this.selection.getRange();
				var commonAnchorEl = range.getCommonAncestor();
				var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
				$(anchorEl).css("width","60%")
				confConpone(anchorEl,thePlugins);//编辑控件
			},constrSubHtml:function(type,obj){
				var _input  = $("<input type='text' style='width:50px' placeholder='数字框'/>");
				_input.attr("easyWinCompon","NumberComponent");
				_input.attr("readonly",true);
				_input.attr("conf_size","medium");
				var tempId = "field_"+new Date().getTime();
				_input.attr("tempId",tempId)
				
				_input.attr("conf_name",obj.title);
				_input.attr("conf_require","false");
				
				_input.attr("conf_tableColumn",obj.column);//关联字段
				
				_input.css("width","60%");
				return $(_input).prop("outerHTML");
			}
	};
	var constrPreData = function(layoutDetail,_input){
		var componKey = layoutDetail.componentKey;
    	var fieldId = layoutDetail.fieldId;
    	var tempId = layoutDetail.tempId;
    	
    	var required = layoutDetail.required;
    	var title = layoutDetail.title;
    	
    	_input.attr("fieldId",fieldId)
		_input.attr("conf_name",title);
		_input.attr("conf_require",required);//必填
		_input.attr("placeholder",title);
	}
	//提示框
	var popup = new baidu.editor.ui.Popup({
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			confConpone(popup.anchorEl,thePlugins);//编辑控件
			this.hide();//隐藏操作
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				var pre = $(this.anchorEl).prev();
				if(pre && $(pre).prop("tagName")){
					$(pre).hasClass("tempClz")?$(pre).remove():"";	
				}
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener( 'mouseover', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /input/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				var html = popup.formatHtml(
				'<nobr>数字框: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				
				if(el.getAttribute('conf_tableColumn')){
					html = popup.formatHtml(
					'<nobr>数字框: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
				}
				
				if ( html ) {
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor(popup.anchorEl ,100);
				} else {
					popup.hide();
				}
			}else{
				popup.hide();
			}
			
		}catch(e){}
	});
	me.addListener( 'click', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /input/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				confConpone(el,thePlugins);//编辑控件
			}else{
				popup.hide();
			}
			
		}catch(e){}
	});
	
	var confConpone = function(_input,thePlugins){
		var confHtml= '<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">标题</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="form-text-mm">';
		confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
		confHtml+='		</div>';
		confHtml+='</div>';
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confRequire" type="checkbox">';
		confHtml+='				<span class="text">这个是必填项</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">组件宽度</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="btn-group confSizeDiv">';
		confHtml+='			<a href="javascript:void(0)" class="btn btn-default">小尺寸</a>';
		confHtml+='			<a href="javascript:void(0)" class="btn btn-default">标准尺寸</a>';
		confHtml+='			<a href="javascript:void(0)" class="btn btn-default">大尺寸</a>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		var confObj = $(confHtml);
		
		$(confObj).find(".confTitle").attr("value",$(_input).attr("conf_name"));
		if($(_input).attr("conf_require") && $(_input).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(_input).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(_input).before("<span style='color:red' class='tempClz'>*</span>");	
			}
			
		}
		
		var conf_size = $(_input).attr("conf_size");
		conf_size = conf_size?conf_size:"medium";
		
		if(conf_size == 'small'){
			$(confObj).find(".confSizeDiv").find("a:eq(0)").removeClass("btn-default").addClass("btn-blue");
		}else if(conf_size == 'medium'){
			$(confObj).find(".confSizeDiv").find("a:eq(1)").removeClass("btn-default").addClass("btn-blue");
		}else if(conf_size == 'large'){
			$(confObj).find(".confSizeDiv").find("a:eq(2)").removeClass("btn-default").addClass("btn-blue");
		}
		
		$("#formConfDiv").html(confObj);
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_input).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_input).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_input).before("<span style='color:red' class='tempClz'>*</span>")
				$(_input).attr("conf_require","true")
			}else{
				$(_input).attr("conf_require","false")
			}
		})
		
		$(confObj).on("click",".confSizeDiv>a",function(){
			$(confObj).find(".confSizeDiv").find("a").removeClass("btn-blue");
			$(confObj).find(".confSizeDiv").find("a").removeClass("btn-default");
			$(confObj).find(".confSizeDiv").find("a").addClass("btn-default");
			$(this).removeClass("btn-default").addClass("btn-blue");
			var index = $(this).index();
			if(index==0){
				$(_input).css("width","30%");
				$(_input).attr("conf_size","small");
			}else if(index==1){
				$(_input).css("width","60%");
				$(_input).attr("conf_size","medium");
			}else if(index==2){
				$(_input).css("width","95%");
				$(_input).attr("conf_size","large");
			}
		})
		
		baidu.editor.plugins[thePlugins].editdom = _input;
	}
};
/**
 * 数字文本框
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['moneycomponent'] = function () {
	var me = this,thePlugins = 'moneycomponent';
	me.commands[thePlugins] = {
			execCommand:function () {//执行添加控件命令
				var _input  = $("<input type='text' style='width:50px' placeholder='大写金额'/>");
				_input.attr("easyWinCompon","MoneyComponent");
				_input.attr("readonly",true);
				_input.attr("conf_size","medium");
				
				var tempId = "field_"+new Date().getTime();
				
				_input.attr("tempId",tempId)
				
				_input.attr("conf_name","大写金额");
				_input.attr("conf_require","false");
				
				UE.getEditor('editor').execCommand('insertHtml',$(_input).prop("outerHTML"))
				
				var range = this.selection.getRange();
				var commonAnchorEl = range.getCommonAncestor();
				var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
				$(anchorEl).css("width","60%")
				confConpone(anchorEl,thePlugins);//编辑控件
			},constrSubHtml:function(type,obj){
				var _input  = $("<input type='text' style='width:50px' placeholder='大写金额'/>");
				_input.attr("easyWinCompon","MoneyComponent");
				_input.attr("readonly",true);
				_input.attr("conf_size","medium");
				var tempId = "field_"+new Date().getTime();
				_input.attr("tempId",tempId)
				
				_input.attr("conf_name",obj.title);
				_input.attr("conf_require","false");
				
				_input.attr("conf_tableColumn",obj.column);//关联字段
				
				_input.css("width","60%");
				return $(_input).prop("outerHTML");
			}
	};
	var constrPreData = function(layoutDetail,_input){
		var componKey = layoutDetail.componentKey;
		var fieldId = layoutDetail.fieldId;
		var tempId = layoutDetail.tempId;
		
		var required = layoutDetail.required;
		var title = layoutDetail.title;
		
		_input.attr("fieldId",fieldId)
		_input.attr("conf_name",title);
		_input.attr("conf_require",required);//必填
		_input.attr("placeholder",title);
	}
	//提示框
	var popup = new baidu.editor.ui.Popup({
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			confConpone(popup.anchorEl,thePlugins);//编辑控件
			this.hide();//隐藏操作
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				var pre = $(this.anchorEl).prev();
				if(pre && $(pre).prop("tagName")){
					$(pre).hasClass("tempClz")?$(pre).remove():"";	
				}
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener( 'mouseover', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /input/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				var html = popup.formatHtml(
				'<nobr>大写金额: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				
				if(el.getAttribute('conf_tableColumn')){
					html = popup.formatHtml(
					'<nobr>数字框: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
				}
				
				if ( html ) {
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor(popup.anchorEl ,100);
				} else {
					popup.hide();
				}
			}else{
				popup.hide();
			}
			
		}catch(e){}
	});
	me.addListener( 'click', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /input/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				confConpone(el,thePlugins);//编辑控件
			}else{
				popup.hide();
			}
			
		}catch(e){}
	});
	
	var confConpone = function(_input,thePlugins){
		var confHtml= '<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">标题</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="form-text-mm">';
		confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm confTransFieldsDiv">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">待选定字段</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="select moneyField">';
		confHtml+='			<select>';
		confHtml+='			</select>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		
		var transFieldList = function(){
			//候选数据
			var candidates = new Array();
			var compone = "NumberComponent";
			
			var parentDataTable = $(_input).parents("[easyWinCompon='DataTable']");
			var parentTable = $(_input).parents("table");
			var parentNormal = $(_input).parents("p");
			if($(parentDataTable).length>0){//属于子表单数据
				var elements = $(parentDataTable).find("[easyWinCompon='"+compone+"'],[easyWinCompon='Monitor']");
				if(elements && elements.length>0){
					 $.each(elements,function(index,opt){
						 var tempId = $(this).attr("tempId");
						 var fieldId = $(this).attr("fieldId");
						 var optName = $(this).attr("conf_name");
						 var option = {"fieldId":fieldId,"name":optName,"tempId":tempId};
						 candidates.push(option);
					 })
				}
			}else if($(parentTable).length>0){//属于表单数据
				var elements = $(parentTable).find("[easyWinCompon='"+compone+"'],[easyWinCompon='Monitor']").filter(function(){
					return $(this).parents("[easyWinCompon='DataTable']").length == 0 
				})
				if(elements && elements.length>0){
					 $.each(elements,function(index,opt){
						 var tempId = $(this).attr("tempId");
						 var fieldId = $(this).attr("fieldId");
						 var optName = $(this).attr("conf_name");
						 var option = {"fieldId":fieldId,"name":optName,"tempId":tempId};
						 candidates.push(option);
					 })
				 }
			}else{//属于普通数据
				var elements = $(parentNormal).find("[easyWinCompon='"+compone+"'],[easyWinCompon='Monitor']").filter(function(){
					return $(this).parents("[easyWinCompon='DataTable']").length == 0 && $(this).parents("table").length == 0
				})
				if(elements && elements.length>0){
					$.each(elements,function(index,opt){
						var tempId = $(this).attr("tempId");
						 var fieldId = $(this).attr("fieldId");
						 var optName = $(this).attr("conf_name");
						 var option = {"fieldId":fieldId,"name":optName,"tempId":tempId};
						candidates.push(option);
					})
				}
			}
			
			return candidates;
		}
		
		
		
		var confObj = $(confHtml);
		
		$(confObj).find(".confTitle").attr("value",$(_input).attr("conf_name"));

		var moneyTempColumn = $(_input).attr("conf_moneyTempColumn");//计算的控件
		
		var transFields = transFieldList();
		
		if(transFields && transFields.length>0){
			$.each(transFields,function(componeIndex,componeObj){
				var option = $("<option></option>")
				option.attr("fieldId",componeObj.fieldId)
				option.attr("tempId",componeObj.tempId)
				option.html(componeObj.name)
				$(confObj).find(".moneyField>select").append(option)
			})
			if(moneyTempColumn){
				$(confObj).find(".moneyField>select").find("[tempId='"+moneyTempColumn+"']").attr("selected",true);
			}else{
				$(confObj).find(".moneyField>select").find("option:eq(0)").attr("selected",true);
				$(_input).attr("conf_moneyTempColumn",$(confObj).find(".moneyField>select").find("option:eq(0)").attr("tempId"))
			}
		}
		
		$("#formConfDiv").html(confObj);
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_input).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		//计算对象变动
		$(confObj).on("change",".moneyField>select",function(){
			$(_input).attr("conf_moneyTempColumn",$(this).find("option:selected").attr("tempId"))
		})
		baidu.editor.plugins[thePlugins].editdom = _input;
	}
};
/**
 * 文本域
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['textarea'] = function () {
	var me = this,thePlugins = 'textarea';
	me.commands[thePlugins] = {
			execCommand:function () {//执行添加控件命令
				
				var layoutDetail = $("#preFormComponent").data("layoutDetail");
				
				var _textarea  = $("<textarea style='height:95%;min-height:50px'></textarea>");
				_textarea.attr("easyWinCompon","TextArea");
				_textarea.attr("readonly",true);
				_textarea.attr("conf_size","medium");
				
				var tempId = "field_"+new Date().getTime();
				if(layoutDetail){
					constrPreData(layoutDetail,_textarea);
					
					var tempIdT = layoutDetail.tempId;
					tempId = "field_"+tempIdT;
					_textarea.attr("tempId",tempId)
					var required = layoutDetail.required;
					if(required && required=='true'){
						var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
						UE.getEditor('editor').execCommand('insertHtml',$(_spanRequire).prop("outerHTML"));
					}
					UE.getEditor('editor').execCommand('insertHtml',$(_textarea).prop("outerHTML"));
					
				}else{	
					_textarea.attr("tempId",tempId)
					
					_textarea.attr("conf_name","文本域");
					_textarea.attr("conf_require","false");
					
					UE.getEditor('editor').execCommand('insertHtml',$(_textarea).prop("outerHTML"))
				}
				var range = this.selection.getRange();
				var commonAnchorEl = range.getCommonAncestor();
				var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
				$(anchorEl).css("width","60%")
				confConpone(anchorEl,thePlugins);//编辑控件
			},constrSubHtml:function(type,obj){
				var _textarea  = $("<textarea style='height:95%;min-height:50px'></textarea>");
				_textarea.attr("easyWinCompon","TextArea");
				_textarea.attr("readonly",true);
				_textarea.attr("conf_size","medium");
				
				var tempId = "field_"+new Date().getTime();
				_textarea.attr("tempId",tempId)
				
				_textarea.attr("conf_name",obj.title);
				_textarea.attr("conf_require","false");
				
				_textarea.attr("conf_tableColumn",obj.column);//关联字段
				
				_textarea.css("width","60%");
				return $(_textarea).prop("outerHTML");
			}
	};
	
	var constrPreData = function(layoutDetail,_html){
		var componKey = layoutDetail.componentKey;
    	var fieldId = layoutDetail.fieldId;
    	var tempId = layoutDetail.tempId;
    	
    	var required = layoutDetail.required;
    	var title = layoutDetail.title;
    	
    	_html.attr("fieldId",fieldId)
		_html.attr("conf_name",title);
    	_html.attr("conf_require",required);//必填
    	_html.attr("placeholder",title);
	}
	//提示框
	var popup = new baidu.editor.ui.Popup({
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			confConpone(popup.anchorEl,thePlugins);//编辑控件
			this.hide();//隐藏操作
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				var pre = $(this.anchorEl).prev();
				if(pre && $(pre).prop("tagName")){
					$(pre).hasClass("tempClz")?$(pre).remove():"";	
				}
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener( 'mouseover', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /textarea/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				var html = popup.formatHtml(
				'<nobr>文本域: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				
				if(el.getAttribute('conf_tableColumn')){
					html = popup.formatHtml(
					'<nobr>文本域: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
				}
				if ( html ) {
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor(popup.anchorEl ,100);
				} else {
					popup.hide();
				}
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	me.addListener( 'click', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /textarea/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				confConpone(el,thePlugins);//编辑控件
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	
	var confConpone = function(_textarea,thePlugins){
		var confHtml= '<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">标题</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="form-text-mm">';
		confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
		confHtml+='		</div>';
		confHtml+='</div>';
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confRequire" type="checkbox">';
		confHtml+='				<span class="text">这个是必填项</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">组件宽度</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="btn-group confSizeDiv">';
		confHtml+='			<a href="javascript:void(0)" class="btn btn-default">小尺寸</a>';
		confHtml+='			<a href="javascript:void(0)" class="btn btn-default">标准尺寸</a>';
		confHtml+='			<a href="javascript:void(0)" class="btn btn-default">大尺寸</a>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		var confObj = $(confHtml);
		
		$(confObj).find(".confTitle").attr("value",$(_textarea).attr("conf_name"));
		if($(_textarea).attr("conf_require") && $(_textarea).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(_textarea).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(_textarea).before("<span style='color:red' class='tempClz'>*</span>");	
			}
			
		}
		
		var conf_size = $(_textarea).attr("conf_size");
		conf_size = conf_size?conf_size:"medium";
		
		if(conf_size == 'small'){
			$(confObj).find(".confSizeDiv").find("a:eq(0)").removeClass("btn-default").addClass("btn-blue");
		}else if(conf_size == 'medium'){
			$(confObj).find(".confSizeDiv").find("a:eq(1)").removeClass("btn-default").addClass("btn-blue");
		}else if(conf_size == 'large'){
			$(confObj).find(".confSizeDiv").find("a:eq(2)").removeClass("btn-default").addClass("btn-blue");
		}
		
		
		$("#formConfDiv").html(confObj);
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_textarea).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_textarea).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_textarea).before("<span style='color:red' class='tempClz'>*</span>")
				$(_textarea).attr("conf_require","true")
			}else{
				$(_textarea).attr("conf_require","false")
			}
		})
		
		$(confObj).on("click",".confSizeDiv>a",function(){
			
			$(confObj).find(".confSizeDiv").find("a").removeClass("btn-blue");
			$(confObj).find(".confSizeDiv").find("a").removeClass("btn-default");
			$(confObj).find(".confSizeDiv").find("a").addClass("btn-default");
			$(this).removeClass("btn-default").addClass("btn-blue");
			var index = $(this).index();
			if(index==0){
				$(_textarea).css("width","30%");
				$(_textarea).attr("conf_size","small");
			}else if(index==1){
				$(_textarea).css("width","60%");
				$(_textarea).attr("conf_size","medium");
			}else if(index==2){
				$(_textarea).css("width","95%");
				$(_textarea).attr("conf_size","large");
			}
		})
		
		baidu.editor.plugins[thePlugins].editdom = _textarea;
	}
};
/**
 * 单选框组
 * @command radios
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'radio');
 * ```
 */
UE.plugins['radiobox'] = function () {
    var me = this,thePlugins = 'radiobox';
    me.commands[thePlugins] = {
        execCommand:function () {
        	
        	var layoutDetail = $("#preFormComponent").data("layoutDetail");
        	
        	var _radiobox = $("<span class='myDiv'></span>");
        	_radiobox.attr("easyWinCompon","RadioBox");
        	
        	var tempId = "field_"+new Date().getTime();
        	if(layoutDetail){
        		constrPreData(layoutDetail,_radiobox);
        		_radiobox.find("input").attr("disabled",true);
				
				var tempIdT = layoutDetail.tempId;
				tempId = "field_"+tempIdT;
				_radiobox.attr("tempId",tempId)
				var required = layoutDetail.required;
				if(required && required=='true'){
					var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
					UE.getEditor('editor').execCommand('insertHtml',$(_spanRequire).prop("outerHTML"));
				}
				UE.getEditor('editor').execCommand('insertHtml',$(_radiobox).prop("outerHTML"));
				
        	}else{
        		
        		var option1 = $("<lable><input type='radio'><span>选项1</span></lable>")
        		$(option1).find("input").attr("optName","选项1"); 
        		var option2 = $("<lable><input type='radio'><span>选项2</span></lable>")
        		$(option2).find("input").attr("optName","选项2"); 
        		var option3 = $("<lable><input type='radio'><span>选项3</span></lable>")
        		$(option3).find("input").attr("optName","选项3"); 
        		var option4 = $("<lable><input type='radio'><span>选项4</span></lable>")
        		$(option4).find("input").attr("optName","选项4"); 
        		
        		_radiobox.append(option1);
        		_radiobox.append(option2);
        		_radiobox.append(option3);
        		_radiobox.append(option4);
        		
        		_radiobox.find("input").attr("disabled",true);
        		
        		_radiobox.attr("tempId",tempId)
        		_radiobox.attr("conf_name","单选框");
        		_radiobox.attr("conf_require","false");
        		_radiobox.attr("conf_layout","field-hoz");
        		
        		UE.getEditor('editor').execCommand('insertHtml',$(_radiobox).prop("outerHTML"))
        	}
        	
        	var range = this.selection.getRange();
			var commonAnchorEl = range.getCommonAncestor();
			var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
			confConpone(anchorEl,thePlugins);//编辑控件
        }
    };
    
    var constrPreData = function(layoutDetail,_html){
		var componKey = layoutDetail.componentKey;
    	var fieldId = layoutDetail.fieldId;
    	var tempId = layoutDetail.tempId;
    	
    	var required = layoutDetail.required;
    	var title = layoutDetail.title;
    	
    	_html.attr("fieldId",fieldId)
		_html.attr("conf_name",title);
    	_html.attr("conf_require",required);//必填
    	
    	
    	var titleLayout = layoutDetail.titleLayout;
    	_html.attr("conf_layout",titleLayout);
    	
    	var options = layoutDetail.options;
    	$.each(options,function(optIndex,optObj){
    		
    		var optFieldId = optObj.fieldId
    		var optName = optObj.name;
    			
    		var option = $("<lable><input type='radio'><span>"+optName+"</span></lable>")
        	$(option).find("input").attr("optName",optName); 
    		$(option).find("input").attr("fieldId",optFieldId); 
    		_html.append(option);
    	});
    	
    	layoutDetail.titleLayout!="field-hoz"?$(_html).find("lable").append("<br/>"):""; 
	}
    var popup = new baidu.editor.ui.Popup( {
        editor:this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
        	  confConpone(popup.anchorEl,thePlugins);//编辑控件
              this.hide();
        },
        _delete:function(){
            if( window.confirm('确认删除该控件吗？') ) {
                baidu.editor.dom.domUtils.remove(this.anchorEl,false);
            }
            this.hide();
        }
    } );
    popup.render();
    me.addListener( 'mouseover',function( t, evt ) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        try{
        	el = /lable/ig.test( el.tagName )?el.parentNode:el;
        	
        	var easyWinCompon = el.getAttribute('easyWinCompon');
        	easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
        	if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
        		var html = popup.formatHtml(
        		'<nobr>单选框: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
        		if ( html ) {
        			var elInput = el.getElementsByTagName("input");
        			var rEl = elInput.length>0 ? elInput[0] : el;
        			popup.getDom( 'content' ).innerHTML = html;
        			popup.anchorEl = el;
        			popup.showAnchor( rEl,200);
        		} else {
        			popup.hide();
        		}
        	}else{
        		popup.hide();
        	}
        }catch(e){}
    });
    me.addListener( 'click',function( t, evt ) {
    	evt = evt || window.event;
    	var el = evt.target || evt.srcElement;
    	try{
    		el = /lable/ig.test( el.tagName )?el.parentNode:el;
    		
    		var easyWinCompon = el.getAttribute('easyWinCompon');
    		easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
    		if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
    			confConpone(el,thePlugins);//编辑控件
    		}else{
    			popup.hide();
    		}
    	}catch(e){}
    });
    
    var confConpone = function(_radiobox,thePlugins){
		var confHtml= '<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">标题</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="form-text-mm">';
		confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confRequire" type="checkbox">';
		confHtml+='				<span class="text">这个是必填项</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">排列方式</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="radio">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confLayout hoz" type="radio" name="confLayout">';
		confHtml+='				<span class="text">横向</span>';
		confHtml+='			</label>';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confLayout vert" type="radio" name="confLayout">';
		confHtml+='				<span class="text">纵向</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm bordered-bottom-gray">';
		confHtml+='		<div class="gray-title margin-bottom-10">';
		confHtml+='			<p class="form-gray text-name">选项内容</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="">';
		confHtml+='			<div class="optListul margin-right-10" style="border:1px solid #ddd;">';
		
		confHtml+='			</div>';
		confHtml+='			<div class="add-line-box">';
		confHtml+='				<a href="javascript:void(0)" class="text-align-center add-btn-content addOpt"><i class="fa fa-plus blue"></i> &nbsp;添加选项</a>';
		confHtml+='			</div>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		
		var confObj = $(confHtml);
		
		//设置选项
		var options = $(_radiobox).find("input");
		if(options && options.length>0){
			$.each(options,function(optIndex,optObj){
				var _li = $('<div class="checkbox clearfix OptOneDiv"></div>');
				var _span = $("<span class='pull-left form-input form-input-rel rowIndex'>"+(optIndex+1)+"</span>");
				
				var _content = $("<div class='pull-left form-write'></div>");
				_content.append('<input class="line-input" type="text">');
				_content.find("input").attr("value",$(optObj).attr("optName"))
				_content.find("input").attr("fieldId",$(optObj).attr("fieldId"))
				
				$(_li).append(_span);
				$(_li).append(_content);
				
				var opt = $('<div class="pull-left form-iconic text-right"></div>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-down fa-form-icon downOpt padding-right-5"></a>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-up fa-form-icon upOpt padding-right-5"></a>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-times fa-times-circle fa-form-delet red delOpt"></a>');
				$(_li).append(opt);
				
				$(confObj).find(".optListul").append(_li);
			})
		}
		//设置名称
		$(confObj).find(".confTitle").attr("value",$(_radiobox).attr("conf_name"));
		if($(_radiobox).attr("conf_require") && $(_radiobox).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(_radiobox).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(_radiobox).before("<span style='color:red' class='tempClz'>*</span>");	
			}
		}
		//设置布局
		var layout = $(_radiobox).attr("conf_layout");
		if(layout=="field-hoz"){
			$(confObj).find(".confLayout:eq(0)").attr("checked",true);
		}else{
			$(confObj).find(".confLayout:eq(1)").attr("checked",true);
		}
		
		$("#formConfDiv").html(confObj);
		var myScroll = $("#formConfDiv").find(".optListul").slimScroll({
	        height:"148px",
	        alwaysVisible: true,
	        disableFadeOut: false
	    });
		
		//触发事件
		//修改名称
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_radiobox).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		//修改必填
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_radiobox).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_radiobox).before("<span style='color:red' class='tempClz'>*</span>")
				$(_radiobox).attr("conf_require","true")
			}else{
				$(_radiobox).attr("conf_require","false")
			}
		})
		
		//修改布局
		$(confObj).on("click",".confLayout",function(){
			$(_radiobox).find("br").remove();
			var preLayout = $(_radiobox).attr("conf_layout");
			!$(this).hasClass("hoz")?$(_radiobox).find("lable").append("<br/>"):$(_radiobox).find("br").remove();
			
			if(preLayout!='field-hoz'){
				$(_radiobox).attr("conf_layout","field-hoz");
			}else{
				$(_radiobox).attr("conf_layout","field-ver");
			}
		})
		
		//修改选项
		$(confObj).on("blur",".line-input",function(){
			
			var confName = $(this).val();
			if(!confName){
				layer.tips("请填写",$(this),{tips:1});
				return false;
			}
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			var selectLable = $(_radiobox).find("lable:eq("+lableIndex+")");
			var optInput = $(selectLable).find("input").clone();
			$(optInput).attr("optName",confName)
			$(selectLable).html(optInput);
			$(selectLable).append(confName);
			
			$(_radiobox).find("br").remove();
			var preLayout = $(_radiobox).attr("conf_layout");
			if(preLayout != 'field-hoz'){
				$(_radiobox).find("lable").append("<br/>");
			}
		})
		//添加选项
		$(confObj).on("click",".addOpt",function(){
			
			var mod = $(this).parent().prev().find(".OptOneDiv:eq(0)").clone();
			$(mod).find("input").val('选项');
			
			$(confObj).find(".optListul").append(mod);
			
			$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
				$(this).find(".rowIndex").html((index+1));
			})
			
			var option = $("<lable><input type='radio'><span>选项</span></lable>")
			$(option).find("input").attr("optName","选项"); 
			$(_radiobox).append($(option))
			
			$(_radiobox).find("br").remove();
			var preLayout = $(_radiobox).attr("conf_layout");
			preLayout!='field-hoz'?$(_radiobox).find("lable").append("<br/>"):$(_radiobox).find("br").remove();
			$(_radiobox).find("input").attr("disabled",true);
			
			var height = $(confObj).find(".optListul").height();
			myScroll.slimscroll({
		        scrollTo:height+'px',
		        alwaysVisible: true,
		        disableFadeOut: false
		    });
			
		})
		
		//删除选项
		$(confObj).on("click",".delOpt",function(){
			var len = $(".optListul").find(".OptOneDiv").length;
			if(len<=1){
				layer.tips("最后一个选项，不能删除",$(this),{tips:1});
				return;
			}
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			$(this).parents(".OptOneDiv").remove();
			$(_radiobox).find("lable:eq("+lableIndex+")").remove();
			
			$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
				$(this).find(".rowIndex").html((index+1));
			})
			
		})
		
		//上移选项
		$(confObj).on("click",".upOpt",function(){
			
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			if(lableIndex > 0){//不是第一个元素
				var _thisP = $(this).parents(".OptOneDiv");
				_thisP.prev().before(_thisP);
				
				$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
					$(this).find(".rowIndex").html((index+1));
				})
				
				var _optP = $(_radiobox).find("lable:eq("+lableIndex+")");
				_optP.prev().before(_optP);
			}
		})
		$(confObj).on("click",".downOpt",function(){
			
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			var len = $(this).parents(".OptOneDiv").parent().find(".OptOneDiv").length - 1;
			if(lableIndex < len){//不是第一个元素
				var _thisP = $(this).parents(".OptOneDiv");
				_thisP.next().after(_thisP);
				
				$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
					$(this).find(".rowIndex").html((index+1));
				})
				
				var _optP = $(_radiobox).find("lable:eq("+lableIndex+")");
				_optP.next().after(_optP);
			}
		})
		//下移选项
		baidu.editor.plugins[thePlugins].editdom = _radiobox;
	}
};
/**
 * 复选框组
 * @command radios
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'radio');
 * ```
 */
UE.plugins['checkbox'] = function () {
	var me = this,thePlugins = 'checkbox';
	me.commands[thePlugins] = {
			execCommand:function () {
				
				var layoutDetail = $("#preFormComponent").data("layoutDetail");
				
				var _checkbox = $("<span class='myDiv'></span>");
				_checkbox.attr("easyWinCompon","CheckBox");
				
				var tempId = "field_"+new Date().getTime();
				if(layoutDetail){
					constrPreData(layoutDetail,_checkbox);
					_checkbox.find("input").attr("disabled",true);
					
					var tempIdT = layoutDetail.tempId;
					tempId = "field_"+tempIdT;
					_checkbox.attr("tempId",tempId)
					var required = layoutDetail.required;
					if(required && required=='true'){
						var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
						UE.getEditor('editor').execCommand('insertHtml',$(_spanRequire).prop("outerHTML"));
					}
					UE.getEditor('editor').execCommand('insertHtml',$(_checkbox).prop("outerHTML"));
				}else{
					var option1 = $("<lable><input type='checkbox'><span>选项1</span></lable>")
					$(option1).find("input").attr("optName","选项1"); 
					var option2 = $("<lable><input type='checkbox'><span>选项2</span></lable>")
					$(option2).find("input").attr("optName","选项2"); 
					var option3 = $("<lable><input type='checkbox'><span>选项3</span></lable>")
					$(option3).find("input").attr("optName","选项3"); 
					var option4 = $("<lable><input type='checkbox'><span>选项4</span></lable>")
					$(option4).find("input").attr("optName","选项4"); 
					
					_checkbox.append(option1);
					_checkbox.append(option2);
					_checkbox.append(option3);
					_checkbox.append(option4);
					_checkbox.find("input").attr("disabled",true);
					
					_checkbox.attr("tempId",tempId)
					
					_checkbox.attr("conf_name","多选框");
					_checkbox.attr("conf_require","0");
					_checkbox.attr("conf_layout","field-hoz");
					
					
					UE.getEditor('editor').execCommand('insertHtml',$(_checkbox).prop("outerHTML"))
				}
				
				var range = this.selection.getRange();
				var commonAnchorEl = range.getCommonAncestor();
				var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
				confConpone(anchorEl,thePlugins);//编辑控件
				
			}
	};
	
	var constrPreData = function(layoutDetail,_html){
		var componKey = layoutDetail.componentKey;
    	var fieldId = layoutDetail.fieldId;
    	var tempId = layoutDetail.tempId;
    	
    	var required = layoutDetail.required;
    	var title = layoutDetail.title;
    	
    	_html.attr("fieldId",fieldId)
		_html.attr("conf_name",title);
    	_html.attr("conf_require",required);//必填
    	
    	
    	var titleLayout = layoutDetail.titleLayout;
    	_html.attr("conf_layout",titleLayout);
    	
    	var options = layoutDetail.options;
    	$.each(options,function(optIndex,optObj){
    		
    		var optFieldId = optObj.fieldId
    		var optName = optObj.name;
    			
    		var option = $("<lable><input type='checkbox'><span>"+optName+"</span></lable>")
        	$(option).find("input").attr("optName",optName); 
    		$(option).find("input").attr("fieldId",optFieldId); 
    		_html.append(option);
    	});
    	
    	layoutDetail.titleLayout!="field-hoz"?$(_html).find("lable").append("<br/>"):""; 
	}
	var popup = new baidu.editor.ui.Popup( {
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			confConpone(popup.anchorEl,thePlugins);//编辑控件
			this.hide();
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener( 'mouseover',function( t, evt ) {
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			el = /lable/ig.test( el.tagName )?el.parentNode:el;
			
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				var html = popup.formatHtml(
				'<nobr>多选框: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				if ( html ) {
					var elInput = el.getElementsByTagName("input");
					var rEl = elInput.length>0 ? elInput[0] : el;
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor( rEl,200);
				} else {
					popup.hide();
				}
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	me.addListener( 'click',function( t, evt ) {
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			el = /lable/ig.test( el.tagName )?el.parentNode:el;
			
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				confConpone(el,thePlugins);//编辑控件
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	
	var confConpone = function(_checkbox,thePlugins){
		var confHtml= '<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">标题</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="form-text-mm">';
		confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confRequire" type="checkbox">';
		confHtml+='				<span class="text">这个是必填项</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">排列方式</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="radio">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confLayout hoz" type="radio" name="confLayout">';
		confHtml+='				<span class="text">横向</span>';
		confHtml+='			</label>';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confLayout vert" type="radio" name="confLayout">';
		confHtml+='				<span class="text">纵向</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm bordered-bottom-gray">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">选项内容</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="">';
		confHtml+='			<div class="optListul margin-right-10" style="border:1px solid #ddd;">';
		
		confHtml+='			</div>';
		confHtml+='			<div class="add-line-box">';
		confHtml+='				<a href="javascript:void(0)" class="text-align-center add-btn-content addOpt"><i class="fa fa-plus blue"></i> &nbsp;添加选项</a>';
		confHtml+='			</div>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		
		var confObj = $(confHtml);
		
		//设置选项
		var options = $(_checkbox).find("input");
		if(options && options.length>0){
			$.each(options,function(optIndex,optObj){
				var _li = $('<div class="checkbox clearfix OptOneDiv"></div>');
				var _span = $("<span class='pull-left form-input form-input-rel rowIndex'>"+(optIndex+1)+"</span>");
				
				var _content = $("<div class='pull-left form-write'></div>");
				_content.append('<input class="line-input" type="text">');
				_content.find("input").attr("value",$(optObj).attr("optName"))
				_content.find("input").attr("fieldId",$(optObj).attr("fieldId"))
				
				$(_li).append(_span);
				$(_li).append(_content);
				
				var opt = $('<div class="pull-left form-iconic text-right"></div>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-down fa-form-icon downOpt padding-right-5"></a>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-up fa-form-icon upOpt padding-right-5"></a>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-times fa-times-circle fa-form-delet red delOpt"></a>');
				$(_li).append(opt);
				
				$(confObj).find(".optListul").append(_li);
			})
		}
		//设置名称
		$(confObj).find(".confTitle").attr("value",$(_checkbox).attr("conf_name"));
		if($(_checkbox).attr("conf_require") && $(_checkbox).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(_checkbox).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(_checkbox).before("<span style='color:red' class='tempClz'>*</span>");	
			}
		}
		//设置布局
		var layout = $(_checkbox).attr("conf_layout");
		if(layout=="field-hoz"){
			$(confObj).find(".confLayout:eq(0)").attr("checked",true);
		}else{
			$(confObj).find(".confLayout:eq(1)").attr("checked",true);
		}
		
		$("#formConfDiv").html(confObj);
		var myScroll = $("#formConfDiv").find(".optListul").slimScroll({
	        height:"148px",
	        alwaysVisible: true,
	        disableFadeOut: false
	    });
		
		//触发事件
		//修改名称
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_checkbox).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		//修改必填
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_checkbox).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_checkbox).before("<span style='color:red' class='tempClz'>*</span>")
				$(_checkbox).attr("conf_require","true")
			}else{
				$(_checkbox).attr("conf_require","false")
			}
		})
		
		//修改布局
		$(confObj).on("click",".confLayout",function(){
			$(_checkbox).find("br").remove();
			var preLayout = $(_checkbox).attr("conf_layout");
			!$(this).hasClass("hoz")?$(_checkbox).find("lable").append("<br/>"):$(_checkbox).find("br").remove();
			
			if(preLayout!='field-hoz'){
				$(_checkbox).attr("conf_layout","field-hoz");
			}else{
				$(_checkbox).attr("conf_layout","field-ver");
			}
		})
		
		//修改选项
		$(confObj).on("blur",".line-input",function(){
			
			var confName = $(this).val();
			if(!confName){
				layer.tips("请填写",$(this),{tips:1});
				return false;
			}
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			var selectLable = $(_checkbox).find("lable:eq("+lableIndex+")");
			var optInput = $(selectLable).find("input").clone();
			$(optInput).attr("optName",confName)
			$(selectLable).html(optInput);
			$(selectLable).append(confName);
			
			$(_checkbox).find("br").remove();
			var preLayout = $(_checkbox).attr("conf_layout");
			if(preLayout != 'field-hoz'){
				$(_checkbox).find("lable").append("<br/>");
			}
		})
		//添加选项
		$(confObj).on("click",".addOpt",function(){
			
			var mod = $(this).parent().prev().find(".OptOneDiv:eq(0)").clone();
			$(mod).find("input").val('选项');
			
			$(confObj).find(".optListul").append(mod);
			
			$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
				$(this).find(".rowIndex").html((index+1));
			})
			
			var option = $("<lable><input type='checkbox'><span>选项</span></lable>")
			$(option).find("input").attr("optName","选项"); 
			$(_checkbox).append($(option))
			
			$(_checkbox).find("br").remove();
			var preLayout = $(_checkbox).attr("conf_layout");
			preLayout!='field-hoz'?$(_checkbox).find("lable").append("<br/>"):$(_checkbox).find("br").remove();
			$(_checkbox).find("input").attr("disabled",true);
			
			var height = $(confObj).find(".optListul").height();
			myScroll.slimscroll({
		        scrollTo:height+'px',
		        alwaysVisible: true,
		        disableFadeOut: false
		    });
			
		})
		
		//删除选项
		$(confObj).on("click",".delOpt",function(){
			var len = $(".optListul").find(".OptOneDiv").length;
			if(len<=1){
				layer.tips("最后一个选项，不能删除",$(this),{tips:1});
				return;
			}
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			$(this).parents(".OptOneDiv").remove();
			$(_checkbox).find("lable:eq("+lableIndex+")").remove();
			
			$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
				$(this).find(".rowIndex").html((index+1));
			})
			
		})
		
		//上移选项
		$(confObj).on("click",".upOpt",function(){
			
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			if(lableIndex > 0){//不是第一个元素
				var _thisP = $(this).parents(".OptOneDiv");
				_thisP.prev().before(_thisP);
				
				$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
					$(this).find(".rowIndex").html((index+1));
				})
				
				var _optP = $(_checkbox).find("lable:eq("+lableIndex+")");
				_optP.prev().before(_optP);
			}
		})
		//下移选项
		$(confObj).on("click",".downOpt",function(){
			
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			var len = $(this).parents(".OptOneDiv").parent().find(".OptOneDiv").length - 1;
			if(lableIndex < len){//不是第一个元素
				var _thisP = $(this).parents(".OptOneDiv");
				_thisP.next().after(_thisP);
				
				$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
					$(this).find(".rowIndex").html((index+1));
				})
				
				var _optP = $(_checkbox).find("lable:eq("+lableIndex+")");
				_optP.next().after(_optP);
			}
		})
		
		baidu.editor.plugins[thePlugins].editdom = _checkbox;
	}
};




/**
 * 下拉菜单
 * @command select
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'select');
 * ```
 */
UE.plugins['select'] = function () {
    var me = this,thePlugins = 'select';
    me.commands[thePlugins] = {
        execCommand:function () {
        	
        	var layoutDetail = $("#preFormComponent").data("layoutDetail");
        	
        	var _selectDiv = $("<span class='myDiv' style='min-width:40px'></span>");
        	_selectDiv.attr("easyWinCompon","Select");
        	
        	var tempId = "field_"+new Date().getTime();
        	if(layoutDetail){
        		constrPreData(layoutDetail,_selectDiv);
        		var tempIdT = layoutDetail.tempId;
				tempId = "field_"+tempIdT;
				_selectDiv.attr("tempId",tempId)
				var required = layoutDetail.required;
				if(required && required=='true'){
					var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
					UE.getEditor('editor').execCommand('insertHtml',$(_spanRequire).prop("outerHTML"));
				}
				UE.getEditor('editor').execCommand('insertHtml',$(_selectDiv).prop("outerHTML"));
        	}else{
        		var _select = $("<select style='min-width:40px'></select>");
        		var option1 = $("<option value='选项1'>选项1</option>")
        		$(option1).attr("optName","选项1"); 
        		var option2 = $("<option value='选项2'>选项2</option>")
        		$(option2).attr("optName","选项2"); 
        		var option3 = $("<option value='选项3'>选项3</option>")
        		$(option3).attr("optName","选项3"); 
        		var option4 = $("<option value='选项4'>选项4</option>")
        		$(option4).attr("optName","选项4"); 
        		
        		_selectDiv.attr("tempId",tempId)
        		
        		_select.append(option1);
        		_select.append(option2);
        		_select.append(option3);
        		_select.append(option4);
        		_select.attr("disabled",true);
        		
        		
        		_selectDiv.attr("conf_name","下拉框");
        		_selectDiv.attr("conf_require","false");
        		
        		_selectDiv.append(_select);
        		
        		UE.getEditor('editor').execCommand('insertHtml',$(_selectDiv).prop("outerHTML"))
        	}
        	
        	var range = this.selection.getRange();
			var commonAnchorEl = range.getCommonAncestor();
			var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
			confConpone(anchorEl,thePlugins);//编辑控件
        },constrSubHtml:function(type,obj){
        	var _selectDiv = $("<span class='myDiv' style='min-width:40px'></span>");
        	_selectDiv.attr("easyWinCompon","Select");
        	
			var tempId = "field_"+new Date().getTime();
			var _select = $("<select style='min-width:40px'></select>");
    		var option1 = $("<option value='选项1'>选项1</option>")
    		$(option1).attr("optName","选项1"); 
    		var option2 = $("<option value='选项2'>选项2</option>")
    		$(option2).attr("optName","选项2"); 
    		var option3 = $("<option value='选项3'>选项3</option>")
    		$(option3).attr("optName","选项3"); 
    		var option4 = $("<option value='选项4'>选项4</option>")
    		$(option4).attr("optName","选项4"); 
    		
    		_selectDiv.attr("tempId",tempId)
    		
    		_select.append(option1);
    		_select.append(option2);
    		_select.append(option3);
    		_select.append(option4);
    		_select.attr("disabled",true);
    		
    		_selectDiv.attr("conf_name",obj.title);
    		_selectDiv.attr("conf_require","false");
    		
    		_selectDiv.append(_select);
    		
    		_selectDiv.attr("conf_tableColumn",obj.column);//关联字段
    		
			return $(_selectDiv).prop("outerHTML");
        }
    };
    var constrPreData = function(layoutDetail,_html){
		var componKey = layoutDetail.componentKey;
    	var fieldId = layoutDetail.fieldId;
    	var tempId = layoutDetail.tempId;
    	
    	var required = layoutDetail.required;
    	var title = layoutDetail.title;
    	
    	_html.attr("fieldId",fieldId)
		_html.attr("conf_name",title);
    	_html.attr("conf_require",required);//必填
    	
    	
    	var _select = $("<select style='min-width:40px'></select>");
    	var options = layoutDetail.options;
    	$.each(options,function(optIndex,optObj){
    		var optFieldId = optObj.fieldId
    		var optName = optObj.name;
    		var option = $("<option>"+optName+"</option>")
			$(option).attr("optName",optName); 
    		$(option).attr("fieldId",optFieldId); 
    		_select.append(option);
    	});
    	_select.attr("disabled",true);
    	
    	_html.append(_select);
	}
    var popup = new baidu.editor.ui.Popup( {
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			confConpone(popup.anchorEl,thePlugins);//编辑控件
			this.hide();
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
    
    popup.render();
    me.addListener( 'mouseover', function( t, evt ) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        var len  = $(el).parents("[easyWinCompon='Select']").length;
		if(len == 1){
			el = el.parentNode;
		}
        try{
        	
        	var easyWinCompon = el.getAttribute('easyWinCompon');
        	easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
        	if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
        		var html = popup.formatHtml(
        		'<nobr>下拉菜单: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
        		
        		if(el.getAttribute('conf_tableColumn')){//是属于子表单的不允许修改
        			html = popup.formatHtml(
            		'<nobr>下拉菜单: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
        		}
        		
        		if ( html ) {
        			popup.getDom( 'content' ).innerHTML = html;
        			popup.anchorEl = el;
        			popup.showAnchor( popup.anchorEl );
        		} else {
        			popup.hide();
        		}
        	}else{
        		popup.hide();
        	}
        }catch(e){}
    });
    me.addListener( 'click', function( t, evt ) {
    	evt = evt || window.event;
    	var el = evt.target || evt.srcElement;
    	try{
    		
    		var easyWinCompon = el.getAttribute('easyWinCompon');
    		easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
    		if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
    			confConpone(el,thePlugins);//编辑控件
    		}else{
    			popup.hide();
    		}
    	}catch(e){}
    });
    var confConpone = function(_selectDiv,thePlugins){
    	var confHtml= '<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">标题</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="form-text-mm">';
		confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confRequire" type="checkbox">';
		confHtml+='				<span class="text">这个是必填项</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm confSysDataDic">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">采用的字典表</p>';
		confHtml+='		</div>';
		//点击事件在editFormMod.js中
		confHtml+='		<div class="select">';
		confHtml+='			<select id="sysDataDic">';
		confHtml+='				<option value="">不采用</option>';
		confHtml+='				<option value="ConsumeType">费用类型</option>';
		confHtml+='			</select>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm bordered-bottom-gray">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">选项内容</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="">';
		confHtml+='			<div class="optListul margin-right-10" style="border:1px solid #ddd;">';
		
		confHtml+='			</div>';
		confHtml+='			<div class="add-line-box">';
		confHtml+='				<a href="javascript:void(0)" class="text-align-center add-btn-content addOpt"><i class="fa fa-plus blue"></i> &nbsp;添加选项</a>';
		confHtml+='			</div>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		
		var confObj = $(confHtml);
		
		var conf_sysDataDic = $(_selectDiv).attr("conf_sysDataDic");
		//设置采用的字典表
		$(confObj).find("#sysDataDic").data("preVal",conf_sysDataDic);
		$(confObj).find("#sysDataDic").val(conf_sysDataDic);
		
		if(conf_sysDataDic){//采用的字典表，不能修改
			$(confObj).find(".addOpt").parent().hide();
		}
		
		//设置选项
		var options = $(_selectDiv).find("select>option");
		if(options && options.length>0){
			$.each(options,function(optIndex,optObj){
				
				var _li = $('<div class="checkbox clearfix OptOneDiv"></div>');
				var _span = $("<span class='pull-left form-input form-input-rel rowIndex'>"+(optIndex+1)+"</span>");
				
				var _content = $("<div class='pull-left form-write'></div>");
				var _input = $('<input class="line-input" type="text">');
				
				if(conf_sysDataDic){//采用的字典表，不能修改
					$(_input).attr("readonly",true);
				}
				_content.append($(_input));
				_content.find("input").attr("value",$(optObj).attr("optName"))
				_content.find("input").attr("fieldId",$(optObj).attr("fieldId"))
				
				$(_li).append(_span);
				$(_li).append(_content);
				
				var opt = $('<div class="pull-left form-iconic text-right"></div>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-down fa-form-icon downOpt padding-right-5"></a>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-up fa-form-icon upOpt padding-right-5"></a>');
				
				//采用的字点表不能删除
				if(!conf_sysDataDic){
					$(opt).append('<a href="javascript:void(0)" class="fa fa-times fa-times-circle fa-form-delet red delOpt"></a>');
				}
				
				$(_li).append(opt);
				
				$(confObj).find(".optListul").append(_li);
			})
		}
		//设置名称
		$(confObj).find(".confTitle").attr("value",$(_selectDiv).attr("conf_name"));
		//设置必填
		if($(_selectDiv).attr("conf_require") && $(_selectDiv).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(_selectDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(_selectDiv).before("<span style='color:red' class='tempClz'>*</span>");	
			}
		}
		
		$("#formConfDiv").html(confObj);
		var myScroll = $("#formConfDiv").find(".optListul").slimScroll({
	        height:"148px",
	        alwaysVisible: true,
	        disableFadeOut: false
	    });
		//触发事件
		//修改名称
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_selectDiv).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		//修改必填
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_selectDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_selectDiv).before("<span style='color:red' class='tempClz'>*</span>")
				$(_selectDiv).attr("conf_require","true")
			}else{
				$(_selectDiv).attr("conf_require","false")
			}
		})
		
		//修改选项
		$(confObj).on("blur",".line-input",function(){
			
			var confName = $(this).val();
			if(!confName){
				layer.tips("请填写",$(this),{tips:1});
				return false;
			}
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			var selectLable = $(_selectDiv).find("option:eq("+lableIndex+")");
			$(selectLable).attr("optName",confName);
			$(selectLable).attr("value",confName);
			$(selectLable).html(confName);
		})
		//添加选项
		$(confObj).on("click",".addOpt",function(){
			
			var mod = $(this).parent().prev().find(".OptOneDiv:eq(0)").clone();
			$(mod).find("input").val('选项');
			
			$(confObj).find(".optListul").append(mod);
			
			$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
				$(this).find(".rowIndex").html((index+1));
			})
			
			var option = $("<option value='选项'>选项</option>")
			$(option).attr("optName","选项"); 
			$(_selectDiv).find("select").append($(option))
			
			
			var height = $(confObj).find(".optListul").height();
			myScroll.slimscroll({
		        scrollTo:height+'px',
		        alwaysVisible: true,
		        disableFadeOut: false
		    });
			
		})
		
		//修改选项
		$(confObj).on("click",".delOpt",function(){
			var len = $(".optListul").find(".OptOneDiv").length;
			if(len<=1){
				layer.tips("最后一个选项，不能删除",$(this),{tips:1});
				return;
			}
			
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			$(this).parents(".OptOneDiv").remove();
			$(_selectDiv).find("option:eq("+lableIndex+")").remove();
			
			$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
				$(this).find(".rowIndex").html((index+1));
			})
		})
		
		//上移选项
		$(confObj).on("click",".upOpt",function(){
			
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			if(lableIndex > 0){//不是第一个元素
				var _thisP = $(this).parents(".OptOneDiv");
				_thisP.prev().before(_thisP);
				
				$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
					$(this).find(".rowIndex").html((index+1));
				})
				var _optP = $(_selectDiv).find("option:eq("+lableIndex+")");
				_optP.prev().before(_optP);
				
			}
		})
		//下移选项
		$(confObj).on("click",".downOpt",function(){
			
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			var len = $(this).parents(".OptOneDiv").parent().find(".OptOneDiv").length - 1;
			if(lableIndex < len){//不是第一个元素
				var _thisP = $(this).parents(".OptOneDiv");
				_thisP.next().after(_thisP);
				
				$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
					$(this).find(".rowIndex").html((index+1));
				})
				
				var _optP = $(_selectDiv).find("option:eq("+lableIndex+")");
				_optP.next().after(_optP);
			}
		})
		
		
		
		$(confObj).on("change","#sysDataDic",function(){
			var sysDataDic = $("#sysDataDic").val();
			if(sysDataDic){
				//在editFormMod.js中实现选择
				chooseSysDic(sysDataDic,function(data){
					if(data && data[0]){
						constrSelectItem(_selectDiv,confObj,myScroll,data);
						$("#sysDataDic").data("preVal",sysDataDic);
						$(_selectDiv).attr("conf_sysDataDic",sysDataDic);
					}else{
						var preVal = $("#sysDataDic").data("preVal");
						if(!preVal){
							constrSelectItem(_selectDiv,confObj,myScroll,data);
						}
						$(_selectDiv).attr("conf_sysDataDic",preVal);
					}
				})
			}else{
				constrSelectItem(_selectDiv,confObj,myScroll,null);
				$("#sysDataDic").data("preVal",sysDataDic);
				
				$(_selectDiv).attr("conf_sysDataDic",sysDataDic);
			}
		})
		
		baidu.editor.plugins[thePlugins].editdom = _selectDiv;
    };
    
    //重新构建布局信息
    var constrSelectItem = function(_selectDiv,confObj,myScroll,data){
    	//清空选项内容
    	$(confObj).find(".optListul").empty();
		$(_selectDiv).find("select").empty();
		if(data && data[0]){
			$.each(data,function(optIndex,obj){
				//表单配置信息
				var _li = $('<div class="checkbox clearfix OptOneDiv"></div>');
				var _span = $("<span class='pull-left form-input form-input-rel rowIndex'>"+(optIndex+1)+"</span>");
				
				var _content = $("<div class='pull-left form-write'></div>");
				_content.append('<input class="line-input" type="text" readonly="readonly">');
				_content.find("input").attr("value",obj.itemName)
				
				$(_li).append(_span);
				$(_li).append(_content);
				
				var opt = $('<div class="pull-left form-iconic text-right"></div>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-down fa-form-icon downOpt padding-right-5"></a>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-up fa-form-icon upOpt padding-right-5"></a>');
				$(_li).append(opt);
				
				$(confObj).find(".optListul").append(_li);
				
				$(_li).find("input").val(obj.itemName);
				
				$(confObj).find(".optListul").append(_li);
				
				//修改表单设计信息
				var option = $("<option value='"+obj.itemName+"'>"+obj.itemName+"</option>")
				$(option).attr("optName",obj.itemName); 
				$(_selectDiv).find("select").append($(option))
				
				$(confObj).find(".addOpt").parent().hide();
			})
		}else{
			for(var optIndex=0;optIndex<3;optIndex++){
				//表单配置信息
				var _li = $('<div class="checkbox clearfix OptOneDiv"></div>');
				var _span = $("<span class='pull-left form-input form-input-rel rowIndex'>"+(optIndex+1)+"</span>");
				
				var _content = $("<div class='pull-left form-write'></div>");
				_content.append('<input class="line-input" type="text">');
				_content.find("input").attr("value","选项")
				
				$(_li).append(_span);
				$(_li).append(_content);
				
				var opt = $('<div class="pull-left form-iconic text-right"></div>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-down fa-form-icon downOpt padding-right-5"></a>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-up fa-form-icon upOpt padding-right-5"></a>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-times fa-times-circle fa-form-delet red delOpt"></a>');
				$(_li).append(opt);
				
				$(confObj).find(".optListul").append(_li);
				
				$(_li).find("input").val('选项');
				
				$(confObj).find(".optListul").append(_li);
				
				//修改表单设计信息
				var option = $("<option value='选项'>选项</option>")
				$(option).attr("optName","选项"); 
				$(_selectDiv).find("select").append($(option));
			}
			$(confObj).find(".addOpt").parent().show();
		}
		
		var height = $(confObj).find(".optListul").height();
		myScroll.slimscroll({
	        scrollTo:height+'px',
	        alwaysVisible: true,
	        disableFadeOut: false
	    });
    }

};
/**
 * 日期单选
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['datecomponent'] = function () {
	var me = this,thePlugins = 'datecomponent';
	me.commands[thePlugins] = {
		execCommand:function () {//执行添加控件命令
			
			var layoutDetail = $("#preFormComponent").data("layoutDetail");
			
			var _input  = $("<input type='text' style='width:90px' placeholder='yyyy-MM-dd'/>");
			_input.attr("easyWinCompon","DateComponent");
			_input.attr("readonly",true);
			
			var tempId = "field_"+new Date().getTime();
			if(layoutDetail){
				constrPreData(layoutDetail,_input);
				
				var tempIdT = layoutDetail.tempId;
				tempId = "field_"+tempIdT;
				_input.attr("tempId",tempId)
				var required = layoutDetail.required;
				if(required && required=='true'){
					var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
					UE.getEditor('editor').execCommand('insertHtml',$(_spanRequire).prop("outerHTML"));
				}
				UE.getEditor('editor').execCommand('insertHtml',$(_input).prop("outerHTML"));
			}else{
				_input.attr("tempId",tempId)
				
				_input.attr("conf_name","日期");
				_input.attr("conf_require","false");//是否必填
				_input.attr("conf_format","yyyy-MM-dd");//格式化方式
				_input.attr("conf_isDefault","false");//是否默认为当前时间
				_input.attr("conf_isEdit","true");//默认可以编辑
				
				
				UE.getEditor('editor').execCommand('insertHtml',$(_input).prop("outerHTML"))
			}
			
			var range = this.selection.getRange();
			var commonAnchorEl = range.getCommonAncestor();
			var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
			confConpone(anchorEl,thePlugins);//编辑控件
			
		},constrSubHtml:function(type,obj){
			var _input  = $("<input type='text' style='width:90px' placeholder='yyyy-MM-dd'/>");
			_input.attr("easyWinCompon","DateComponent");
			_input.attr("readonly",true);
			var tempId = "field_"+new Date().getTime();
			_input.attr("tempId",tempId)
			
			_input.attr("conf_name",obj.title);
			_input.attr("conf_require","false");//是否必填
			_input.attr("conf_format","yyyy-MM-dd");//格式化方式
			_input.attr("conf_isDefault","false");//是否默认为当前时间
			_input.attr("conf_isEdit","false");//默认可以编辑
			
			_input.attr("conf_tableColumn",obj.column);//关联字段
			
			_input.css("width","60%");
			
			return $(_input).prop("outerHTML");
		}
	};
	
	var constrPreData = function(layoutDetail,_html){
		var componKey = layoutDetail.componentKey;
    	var fieldId = layoutDetail.fieldId;
    	var tempId = layoutDetail.tempId;
    	
    	var required = layoutDetail.required;
    	var title = layoutDetail.title;
    	
    	_html.attr("fieldId",fieldId)
		_html.attr("conf_name",title);
    	_html.attr("conf_require",required);//必填
    	_html.attr("placeholder",title);
    	
    	var isDefault = layoutDetail.isDefault;
    	_html.attr("conf_isDefault",isDefault);//是否默认为当前时间
		
		var isEdit = layoutDetail.isEdit;
		isEdit = isEdit?isEdit:'true';
		_html.attr("conf_isEdit",isEdit);//是否默认为当前时间
			
		
		var confFormat = layoutDetail.format;
		_html.attr("conf_format",confFormat);//格式化方式
		
		if(confFormat=='yyyy-MM'){
			$(_html).css("width","90px");
		}else if(confFormat=='yyyy年MM月'){
			$(_html).css("width","120px");
		}else if(confFormat=='yyyy-MM-dd'){
			$(_html).css("width","90px");
		}else if(confFormat=='yyyy年MM月dd日'){
			$(_html).css("width","120px");
		}else {
			$(_html).css("width","120px");
		}
		
	}
	//提示框
	var popup = new baidu.editor.ui.Popup({
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			  confConpone(popup.anchorEl,thePlugins);//编辑控件
			  this.hide();//隐藏操作
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				var pre = $(this.anchorEl).prev();
				if(pre && $(pre).prop("tagName")){
					$(pre).hasClass("tempClz")?$(pre).remove():"";	
				}
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener( 'mouseover', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			var len  = $(el).parents("[easyWinCompon='DateInterval']").length;
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /input/ig.test( el.tagName ) && easyWinCompon==thePlugins && len==0) {
				var html = popup.formatHtml(
				'<nobr>日期选择: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				
				if(el.getAttribute('conf_tableColumn')){
					html = popup.formatHtml(
					'<nobr>日期选择: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
				}
				if ( html ) {
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor(popup.anchorEl ,100);
				} else {
					popup.hide();
				}
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	me.addListener( 'click', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			var len  = $(el).parents("[easyWinCompon='DateInterval']").length;
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /input/ig.test( el.tagName ) && easyWinCompon==thePlugins && len==0) {
				confConpone(el,thePlugins);//编辑控件
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	
	var confConpone = function(_input,thePlugins){
		var confHtml= '<div class="form-title-mm">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">标题</p>';
			confHtml+='		</div>';
			confHtml+='		<div class="form-text-mm">';
			confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="checkbox">';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confRequire" type="checkbox">';
			confHtml+='				<span class="text">这个是必填项</span>';
			confHtml+='			</label>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">日期格式</p>';
			confHtml+='		</div>';
			confHtml+='		<div style="width:100%">';
			confHtml+='			<label style="width:100%">';
			confHtml+='				<input class="inverted confFormat" type="radio" name="confFormat">';
			confHtml+='				<span class="text">年-月</span>';
			confHtml+='			</label>';
			confHtml+='			<label style="width:100%">';
			confHtml+='				<input class="inverted confFormat" type="radio" name="confFormat">';
			confHtml+='				<span class="text">年-月-日</span>';
			confHtml+='			</label>';
			confHtml+='			<label style="width:100%">';
			confHtml+='				<input class="inverted confFormat" type="radio" name="confFormat">';
			confHtml+='				<span class="text">yyyy-MM</span>';
			confHtml+='			</label>';
			confHtml+='			<label style="width:100%">';
			confHtml+='				<input class="inverted confFormat" type="radio" name="confFormat">';
			confHtml+='				<span class="text">yyyy-MM-dd</span>';
			confHtml+='			</label>';
			confHtml+='			<label style="width:100%">';
			confHtml+='				<input class="inverted confFormat" type="radio" name="confFormat">';
			confHtml+='				<span class="text">yyyy-MM-dd HH:mm</span>';
			confHtml+='			</label>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="checkbox">';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confDefault" type="checkbox">';
			confHtml+='				<span class="text">这个是系统默认</span>';
			confHtml+='			</label>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="checkbox confIsEditDiv">';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confIsEdit" type="checkbox">';
			confHtml+='				<span class="text">可以编辑</span>';
			confHtml+='			</label>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			
		var confObj = $(confHtml);
		
		$(confObj).find(".confTitle").attr("value",$(_input).attr("conf_name"));
		if($(_input).attr("conf_require") && $(_input).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(_input).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(_input).before("<span style='color:red' class='tempClz'>*</span>");	
			}
			
		}
		//配置默认
		if($(_input).attr("conf_isDefault") && $(_input).attr("conf_isDefault")=='true'){
			$(confObj).find(".confDefault").attr("checked",true);
			
			$(confObj).find(".confIsEditDiv").parent().show();
			if($(_input).attr("conf_isEdit") && $(_input).attr("conf_isEdit")=='false'){
				$(confObj).find(".confIsEdit").attr("checked",false);
			}else{
				$(confObj).find(".confIsEdit").attr("checked",true);
				
			}
		}else{
			$(confObj).find(".confIsEditDiv").parent().hide();
		}
		//配置格式化
		var confFormat = $(_input).attr("conf_format");
		if(confFormat=='yyyy年MM月'){
			$(confObj).find(".confFormat:eq(0)").attr("checked",true)
		}else if(confFormat=='yyyy年MM月dd日'){
			$(confObj).find(".confFormat:eq(1)").attr("checked",true)
		}else if(confFormat=='yyyy-MM'){
			$(confObj).find(".confFormat:eq(2)").attr("checked",true)
		}else if(confFormat=='yyyy-MM-dd'){
			$(confObj).find(".confFormat:eq(3)").attr("checked",true)
		}else{
			$(confObj).find(".confFormat:eq(4)").attr("checked",true)
		}
		
		$("#formConfDiv").html(confObj);
		
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_input).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_input).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_input).before("<span style='color:red' class='tempClz'>*</span>")
				$(_input).attr("conf_require","true")
			}else{
				$(_input).attr("conf_require","false")
			}
		})
		//配置日期格式
		$(confObj).on("click",".confFormat",function(){
			var index = $(this).parent().index();
			if(index==0){
				$(_input).attr("conf_format","yyyy年MM月");
				$(_input).css("width","70px");
				$(_input).attr("placeholder","年-月");
			}else if(index==1){
				$(_input).attr("conf_format","yyyy年MM月dd日");
				$(_input).css("width","100px");
				$(_input).attr("placeholder","年-月-日");
			}else if(index==2){
				$(_input).attr("conf_format","yyyy-MM");
				$(_input).css("width","70px");
				$(_input).attr("placeholder","yyyy-MM");
			}else if(index==3){
				$(_input).attr("conf_format","yyyy-MM-dd");
				$(_input).css("width","90px");
				$(_input).attr("placeholder","yyyy-MM-dd");
			}else {
				$(_input).attr("conf_format","yyyy-MM-dd HH:mm");
				$(_input).css("width","120px");
				$(_input).attr("placeholder","yyyy-MM-dd HH:mm");
			}
		})
		
		$(confObj).on("click",".confDefault",function(){
			
			if($(this).attr("checked")){
				$(_input).attr("conf_isDefault","true");//是否默认为当前时间
				
				$(confObj).find(".confIsEditDiv").parent().show();
				$(confObj).find(".confIsEdit").attr("checked",true);
				$(_input).attr("conf_isEdit","true");//可编辑
				
			}else{
				$(_input).attr("conf_isDefault","false");//是否默认为当前时间
				
				$(_input).attr("conf_isEdit",'true');//可以编辑
				$(confObj).find(".confIsEditDiv").parent().hide();
			}
		})
		
		$(confObj).on("click",".confIsEdit",function(){
			if($(this).attr("checked")){
				$(_input).attr("conf_isEdit","true");//是否默认为当前时间
			}else{
				$(_input).attr("conf_isEdit","false");//是否默认为当前时间
			}
		})
		
		
		
		baidu.editor.plugins[thePlugins].editdom = _input;
	}
};
/**
 * 日期区间
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['dateinterval'] = function () {
	var me = this,thePlugins = 'dateinterval';
	me.commands[thePlugins] = {
			execCommand:function () {//执行添加控件命令
				var layoutDetail = $("#preFormComponent").data("layoutDetail");
				
				var _DateInterDiv = $("<span class='myDiv'></span>");
				_DateInterDiv.attr("easyWinCompon","DateInterval");
				
				var tempId = "field_"+new Date().getTime();
				if(layoutDetail){
					constrPreData(layoutDetail,_DateInterDiv);
					
					var tempIdT = layoutDetail.tempId;
					tempId = "field_"+tempIdT;
					_DateInterDiv.attr("tempId",tempId)
					var required = layoutDetail.required;
					if(required && required=='true'){
						var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
						UE.getEditor('editor').execCommand('insertHtml',$(_spanRequire).prop("outerHTML"));
					}
					UE.getEditor('editor').execCommand('insertHtml',$(_DateInterDiv).prop("outerHTML"));
				}else{
					
					_DateInterDiv.attr("conf_name","日期区间");
					_DateInterDiv.attr("conf_require","false");//是否必填
					_DateInterDiv.attr("conf_format","yyyy-MM-dd");//格式化方式
					
					_DateInterDiv.attr("tempId",tempId)
					
					var _startDateInput  = $("<span class='dateTime'><input type='text' style='width:90px' placeholder='yyyy-MM-dd'/></span>");
					var _endDateInput  = $("<span class='dateTime'><input type='text' style='width:90px' placeholder='yyyy-MM-dd'/></span>");
					
					$(_startDateInput).find("input").attr("readonly",true);
					$(_startDateInput).find("input").attr("conf_name","日期区间(起)");
					$(_startDateInput).find("input").attr("easyWinCompon","DateComponent");
					$(_startDateInput).find("input").attr("tempId","field_S"+new Date().getTime());
					
					$(_endDateInput).find("input").attr("readonly",true);
					$(_endDateInput).find("input").attr("conf_name","日期区间(止)");
					$(_endDateInput).find("input").attr("easyWinCompon","DateComponent");
					$(_endDateInput).find("input").attr("tempId","field_E"+new Date().getTime());
					
					_DateInterDiv.append(_startDateInput);
					_DateInterDiv.append("<span style='margin-left:5px;margin-right:5px;'>至</span>");
					_DateInterDiv.append(_endDateInput);
					
					UE.getEditor('editor').execCommand('insertHtml',$(_DateInterDiv).prop("outerHTML"))
				}
				
				var range = this.selection.getRange();
				var commonAnchorEl = range.getCommonAncestor();
				var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
				confConpone(anchorEl,thePlugins);//编辑控件
				
			}
	};
	
	var constrPreData = function(layoutDetail,_html){
		var componKey = layoutDetail.componentKey;
    	var fieldId = layoutDetail.fieldId;
    	var tempId = layoutDetail.tempId;
    	
    	var required = layoutDetail.required;
    	var title = layoutDetail.title;
    	
    	_html.attr("fieldId",fieldId)
		_html.attr("conf_name",title);
    	_html.attr("conf_require",required);//必填
    	
    	var confFormat = layoutDetail.start.format;
    	_html.attr("conf_format",confFormat);//格式化方式
		
		var start = layoutDetail.start;
		var _startDateInput  = $("<span class='dateTime'><input type='text' style='width:90px' placeholder='yyyy-MM-dd'/></span>");
		
		$(_startDateInput).find("input").attr("readonly",true);
		$(_startDateInput).find("input").attr("conf_name",start.title);
		$(_startDateInput).find("input").attr("easyWinCompon","DateComponent");
		$(_startDateInput).find("input").attr("tempId","field_"+start.tempId);
		$(_startDateInput).find("input").attr("fieldId",start.fieldId);
		
		var end = layoutDetail.end;
		var _endDateInput  = $("<span class='dateTime'><input type='text' style='width:90px' placeholder='yyyy-MM-dd'/></span>");
		$(_endDateInput).find("input").attr("readonly",true);
		$(_endDateInput).find("input").attr("conf_name",end.title);
		$(_endDateInput).find("input").attr("easyWinCompon","DateComponent");
		$(_endDateInput).find("input").attr("tempId","field_"+end.tempId);
		$(_endDateInput).find("input").attr("fieldId",end.fieldId);
		
		_html.append(_startDateInput);
		_html.append("<span>~</span>");
		_html.append(_endDateInput);
		
		if(confFormat=='yyyy-MM'){
			$(_html).find("input").css("width","90px");
			$(_html).find("input").attr("placeholder","yyyy-MM");
		}else if(confFormat=='yyyy-MM-dd HH:mm'){
			$(_html).find("input").css("width","120px");
			$(_html).find("input").attr("placeholder","yyyy-MM-dd HH:mm");
		}else{
			$(_html).find("input").css("width","90px");
			$(_html).find("input").attr("placeholder","yyyy-MM-dd");
		}
	}
	
	//提示框
	var popup = new baidu.editor.ui.Popup({
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			confConpone(popup.anchorEl,thePlugins);//编辑控件
			this.hide();//隐藏操作
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				var pre = $(this.anchorEl).prev();
				if(pre && $(pre).prop("tagName")){
					$(pre).hasClass("tempClz")?$(pre).remove():"";	
				}
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener( 'mouseover', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			el = $(el).hasClass("dateTime")?el.parentNode:el;
			var len  = $(el).parents("[easyWinCompon='DateInterval']").length;
			if(len == 1){
				el = el.parentNode.parentNode;
			}
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				var html = popup.formatHtml(
				'<nobr>日期区间: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				if ( html ) {
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor(popup.anchorEl ,100);
				} else {
					popup.hide();
				}
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	me.addListener( 'click', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			el = $(el).hasClass("dateTime")?el.parentNode:el;
			var len  = $(el).parents("[easyWinCompon='DateInterval']").length;
			if(len == 1){
				el = el.parentNode.parentNode;
			}
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				confConpone(el,thePlugins);//编辑控件
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	
	var confConpone = function(_DateInterDiv,thePlugins){
		var confHtml= '<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">标题</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="form-text-mm">';
		confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confRequire" type="checkbox">';
		confHtml+='				<span class="text">这个是必填项</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">日期格式</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="radio">';
		confHtml+='			<label style="width:100%">';
		confHtml+='				<input class="inverted confFormat" type="radio" name="confFormat">';
		confHtml+='				<span class="text">yyyy-MM</span>';
		confHtml+='			</label>';
		confHtml+='			<label style="width:100%">';
		confHtml+='				<input class="inverted confFormat" type="radio" name="confFormat">';
		confHtml+='				<span class="text">yyyy-MM-dd</span>';
		confHtml+='			</label>';
		confHtml+='			<label style="width:100%">';
		confHtml+='				<input class="inverted confFormat" type="radio" name="confFormat">';
		confHtml+='				<span class="text">yyyy-MM-dd HH:mm</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		var confObj = $(confHtml);
		
		$(confObj).find(".confTitle").attr("value",$(_DateInterDiv).attr("conf_name"));
		if($(_DateInterDiv).attr("conf_require") && $(_DateInterDiv).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(_DateInterDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(_DateInterDiv).before("<span style='color:red' class='tempClz'>*</span>");	
			}
			
		}
		//配置默认
		if($(_DateInterDiv).attr("conf_isDefault") && $(_DateInterDiv).attr("conf_isDefault")=='true'){
			$(confObj).find(".confDefault").attr("checked",true);
		}
		//配置格式化
		var confFormat = $(_DateInterDiv).attr("conf_format");
		confFormat=='yyyy-MM'?$(confObj).find(".confFormat:eq(0)").attr("checked",true):confFormat=='yyyy-MM-dd HH:mm'?$(confObj).find(".confFormat:eq(2)").attr("checked",true):$(confObj).find(".confFormat:eq(1)").attr("checked",true)
				
				$("#formConfDiv").html(confObj);
		
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_DateInterDiv).attr("conf_name",conName)
				$(_DateInterDiv).find("input:eq(0)").attr("conf_name",conName+"(起)");
				$(_DateInterDiv).find("input:eq(1)").attr("conf_name",conName+"(止)");
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_DateInterDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_DateInterDiv).before("<span style='color:red' class='tempClz'>*</span>")
				$(_DateInterDiv).attr("conf_require","true")
			}else{
				$(_DateInterDiv).attr("conf_require","false")
			}
		})
		//配置日期格式
		$(confObj).on("click",".confFormat",function(){
			var index = $(this).parent().index();
			if(index==0){
				$(_DateInterDiv).attr("conf_format","yyyy-MM");
				$(_DateInterDiv).find("input").css("width","90px");
				$(_DateInterDiv).find("input").attr("placeholder","yyyy-MM");
			}else if(index==2){
				$(_DateInterDiv).attr("conf_format","yyyy-MM-dd HH:mm");
				$(_DateInterDiv).find("input").css("width","120px");
				$(_DateInterDiv).find("input").attr("placeholder","yyyy-MM-dd HH:mm");
			}else{
				$(_DateInterDiv).attr("conf_format","yyyy-MM-dd");
				$(_DateInterDiv).find("input").css("width","90px");
				$(_DateInterDiv).find("input").attr("placeholder","yyyy-MM-dd");
			}
		})
		baidu.editor.plugins[thePlugins].editdom = _DateInterDiv;
	}
};

/**
 * 人员选择
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['employee'] = function () {
	var me = this,thePlugins = 'employee';
	me.commands[thePlugins] = {
		execCommand:function () {//执行添加控件命令
			
			var layoutDetail = $("#preFormComponent").data("layoutDetail");
			
			var _EmployeeDiv = $("<span class='myDiv'></span>");
			_EmployeeDiv.attr("easyWinCompon","Employee");
			
			var tempId = "field_"+new Date().getTime();
			if(layoutDetail){
				constrPreData(layoutDetail,_EmployeeDiv);
				
				var tempIdT = layoutDetail.tempId;
				tempId = "field_"+tempIdT;
				_EmployeeDiv.attr("tempId",tempId)
				var required = layoutDetail.required;
				if(required && required=='true'){
					var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
					UE.getEditor('editor').execCommand('insertHtml',$(_spanRequire).prop("outerHTML"));
				}
				UE.getEditor('editor').execCommand('insertHtml',$(_EmployeeDiv).prop("outerHTML"));
			}else{
				_EmployeeDiv.attr("tempId",tempId)
				
				_EmployeeDiv.attr("conf_name","人员选择");
				_EmployeeDiv.attr("conf_require","false");//是否必填
				
				_EmployeeDiv.attr("conf_isUnique","true");//是否默认为单选
				_EmployeeDiv.attr("conf_isDefault","false");//是否默认为当前人员
				_EmployeeDiv.attr("conf_isEdit",'true');//可以编辑
				
				var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>人员</span>");
				_EmployeeDiv.append(_input)
				UE.getEditor('editor').execCommand('insertHtml',$(_EmployeeDiv).prop("outerHTML"))
				
			}
			
			var range = this.selection.getRange();
			var commonAnchorEl = range.getCommonAncestor();
			var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
			confConpone(anchorEl,thePlugins);//编辑控件
			
		}
	};
	
	var constrPreData = function(layoutDetail,_html){
		var componKey = layoutDetail.componentKey;
    	var fieldId = layoutDetail.fieldId;
    	var tempId = layoutDetail.tempId;
    	
    	var required = layoutDetail.required;
    	var title = layoutDetail.title;
    	
    	_html.attr("fieldId",fieldId)
		_html.attr("conf_name",title);
    	_html.attr("conf_require",required);//必填
    	
    	var isUnique = layoutDetail.isUnique;
    	_html.attr("conf_isUnique",isUnique);//是否默认为当前人员
		
		var isDefault = layoutDetail.isDefault;
		_html.attr("conf_isDefault",isDefault);//是否默认为当前时间
		
		var isEdit = layoutDetail.isEdit;
		isEdit = isEdit?isEdit:'true';
		_html.attr("conf_isEdit",isEdit);//是否默认为当前时间
		
		var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>人员</span>");
		_html.append(_input)
		
	}
	//提示框
	var popup = new baidu.editor.ui.Popup({
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			  confConpone(popup.anchorEl,thePlugins);//编辑控件
			  this.hide();//隐藏操作
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				var pre = $(this.anchorEl).prev();
				if(pre && $(pre).prop("tagName")){
					$(pre).hasClass("tempClz")?$(pre).remove():"";	
				}
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener( 'mouseover', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			el = $(el).hasClass("fa")?el.parentNode:el;
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				var html = popup.formatHtml(
				'<nobr>人员选择: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				if ( html ) {
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor(popup.anchorEl ,100);
				} else {
					popup.hide();
				}
			}else{
				popup.hide();
			}
		
		}catch(e){}
	});
	me.addListener( 'click', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			el = $(el).hasClass("fa")?el.parentNode:el;
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				confConpone(el,thePlugins);//编辑控件
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	
	var confConpone = function(_EmployeeDiv,thePlugins){
		var confHtml= '<div class="form-title-mm">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">标题</p>';
			confHtml+='		</div>';
			confHtml+='		<div class="form-text-mm">';
			confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="checkbox">';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confRequire" type="checkbox">';
			confHtml+='				<span class="text">这个是必填项</span>';
			confHtml+='			</label>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="checkbox">';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confisUnique" type="checkbox">';
			confHtml+='				<span class="text">这个是单选</span>';
			confHtml+='			</label>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="checkbox confDefaultDiv">';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confDefault" type="checkbox">';
			confHtml+='				<span class="text">这个是系统默认</span>';
			confHtml+='			</label>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="checkbox confIsEditDiv">';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confIsEdit" type="checkbox">';
			confHtml+='				<span class="text">可以编辑</span>';
			confHtml+='			</label>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			
		var confObj = $(confHtml);
		
		$(confObj).find(".confTitle").attr("value",$(_EmployeeDiv).attr("conf_name"));
		if($(_EmployeeDiv).attr("conf_require") && $(_EmployeeDiv).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(_EmployeeDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(_EmployeeDiv).before("<span style='color:red' class='tempClz'>*</span>");	
			}
			
		}
		
		//配置单选
		if($(_EmployeeDiv).attr("conf_isUnique") && $(_EmployeeDiv).attr("conf_isUnique")=='true'){
			$(confObj).find(".confisUnique").attr("checked",true);
			
			//人员默认
			$(confObj).find(".confDefaultDiv").parent().show();
			//配置默认
			if($(_EmployeeDiv).attr("conf_isDefault") && $(_EmployeeDiv).attr("conf_isDefault")=='true'){
				$(confObj).find(".confDefault").attr("checked",true);
				
				$(confObj).find(".confIsEditDiv").parent().show();
				if($(_EmployeeDiv).attr("conf_isEdit") && $(_EmployeeDiv).attr("conf_isEdit")=='false'){
					$(confObj).find(".confIsEdit").attr("checked",false);
				}else{
					$(confObj).find(".confIsEdit").attr("checked",true);
				}
				
			}else{
				//人员多选没有 不可编辑
				$(confObj).find(".confIsEditDiv").parent().hide();
			}
			
		}else{
			//人员多选没有默认
			$(confObj).find(".confDefaultDiv").parent().hide();
			//人员多选没有 不可编辑
			$(confObj).find(".confIsEditDiv").parent().hide();
		}
		
		
		$("#formConfDiv").html(confObj);
		
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_EmployeeDiv).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_EmployeeDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_EmployeeDiv).before("<span style='color:red' class='tempClz'>*</span>")
				$(_EmployeeDiv).attr("conf_require","true")
			}else{
				$(_EmployeeDiv).attr("conf_require","false")
			}
		})
		//人员单选设置
		$(confObj).on("click",".confisUnique",function(){
			if($(this).attr("checked")){
				
				$(_EmployeeDiv).attr("conf_isUnique","true");//是否默认为当前时间
				
				$(_EmployeeDiv).attr("conf_isDefault","false");//没有默认
				$(confObj).find(".confDefaultDiv").parent().show();
				$(confObj).find(".confDefault").attr("checked",false);
				
				$(_EmployeeDiv).attr("conf_isEdit","true");//可以编辑
				$(confObj).find(".confIsEditDiv").parent().hide();
				$(confObj).find(".confIsEdit").attr("checked",true);
				
			}else{
				$(_EmployeeDiv).attr("conf_isUnique","false");//多选
				
				$(_EmployeeDiv).attr("conf_isDefault","false");//没有默认
				$(confObj).find(".confDefaultDiv").parent().hide();
				$(confObj).find(".confDefault").attr("checked",false);
				
				$(_EmployeeDiv).attr("conf_isEdit","false");//可以编辑
				$(confObj).find(".confIsEditDiv").parent().hide();
				$(confObj).find(".confIsEdit").attr("checked",true);
				
			}
		})
		
		$(confObj).on("click",".confDefault",function(){
			if($(this).attr("checked")){
				$(_EmployeeDiv).attr("conf_isDefault","true");//是否默认为当前时间
				
				$(_EmployeeDiv).attr("conf_isEdit",'true');//可以编辑
				$(confObj).find(".confIsEditDiv").parent().show();
				$(confObj).find(".confIsEdit").attr("checked",true);
			}else{
				$(_EmployeeDiv).attr("conf_isDefault","false");//是否默认为当前时间
				
				$(_EmployeeDiv).attr("conf_isEdit",'true');//可以编辑
				$(confObj).find(".confIsEditDiv").parent().hide();
				$(confObj).find(".confIsEdit").attr("checked",true);
			}
			
		})
		
		$(confObj).on("click",".confIsEdit",function(){
			if($(this).attr("checked")){
				$(_EmployeeDiv).attr("conf_isEdit","true");//是否默认为当前时间
			}else{
				$(_EmployeeDiv).attr("conf_isEdit","false");//是否默认为当前时间
			}
		})
		
		baidu.editor.plugins[thePlugins].editdom = _EmployeeDiv;
	}
};
/**
 * 部门选择
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['department'] = function () {
	var me = this,thePlugins = 'department';
	me.commands[thePlugins] = {
			execCommand:function () {//执行添加控件命令
				var layoutDetail = $("#preFormComponent").data("layoutDetail");
				
				var _DepartmentDiv = $("<span class='myDiv'></span>");
				_DepartmentDiv.attr("easyWinCompon","Department");
				
				var tempId = "field_"+new Date().getTime();
				if(layoutDetail){
					constrPreData(layoutDetail,_DepartmentDiv);
					
					var tempIdT = layoutDetail.tempId;
					tempId = "field_"+tempIdT;
					_DepartmentDiv.attr("tempId",tempId)
					var required = layoutDetail.required;
					if(required && required=='true'){
						var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
						UE.getEditor('editor').execCommand('insertHtml',$(_spanRequire).prop("outerHTML"));
					}
					UE.getEditor('editor').execCommand('insertHtml',$(_DepartmentDiv).prop("outerHTML"));
				}else{
					_DepartmentDiv.attr("tempId",tempId)
					
					_DepartmentDiv.attr("conf_name","部门选择");
					_DepartmentDiv.attr("conf_require","false");//是否必填
					_DepartmentDiv.attr("conf_isUnique","true");//单选
					_DepartmentDiv.attr("conf_isDefault","false");//是否默认为当前部门
					_DepartmentDiv.attr("conf_isEdit","true");//可以编辑
					
					var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>部门</span>");
					_DepartmentDiv.append(_input)
					UE.getEditor('editor').execCommand('insertHtml',$(_DepartmentDiv).prop("outerHTML"))
				}
				
				var range = this.selection.getRange();
				var commonAnchorEl = range.getCommonAncestor();
				var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
				confConpone(anchorEl,thePlugins);//编辑控件
			}
	};
	
	var constrPreData = function(layoutDetail,_html){
		var componKey = layoutDetail.componentKey;
    	var fieldId = layoutDetail.fieldId;
    	var tempId = layoutDetail.tempId;
    	
    	var required = layoutDetail.required;
    	var title = layoutDetail.title;
    	
    	_html.attr("fieldId",fieldId)
		_html.attr("conf_name",title);
    	_html.attr("conf_require",required);//必填
    	
    	var isUnique = layoutDetail.isUnique;
    	_html.attr("conf_isUnique",isUnique);//是否默认为当前人员
		
		var isDefault = layoutDetail.isDefault;
		_html.attr("conf_isDefault",isDefault);//是否默认为当前时间
		
		var isEdit = layoutDetail.isEdit;
		_html.attr("conf_isEdit",isEdit);//是否默认为当前时间
		
		
		var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>部门</span>");
		_html.append(_input)
	}
	//提示框
	var popup = new baidu.editor.ui.Popup({
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			confConpone(popup.anchorEl,thePlugins);//编辑控件
			this.hide();//隐藏操作
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				var pre = $(this.anchorEl).prev();
				if(pre && $(pre).prop("tagName")){
					$(pre).hasClass("tempClz")?$(pre).remove():"";	
				}
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener( 'mouseover', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			el = $(el).hasClass("fa")?el.parentNode:el;
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			
			if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				var html = popup.formatHtml(
				'<nobr>部门选择: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				if ( html ) {
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor(popup.anchorEl ,100);
				} else {
					popup.hide();
				}
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	me.addListener( 'click', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			el = $(el).hasClass("fa")?el.parentNode:el;
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			
			if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				confConpone(el,thePlugins);//编辑控件
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	
	var confConpone = function(_DepartmentDiv,thePlugins){
		var confHtml= '<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">标题</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="form-text-mm">';
		confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confRequire" type="checkbox">';
		confHtml+='				<span class="text">这个是必填项</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confisUnique" type="checkbox">';
		confHtml+='				<span class="text">这个是单选</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox confDefaultDiv">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confDefault" type="checkbox">';
		confHtml+='				<span class="text">这个是系统默认</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox confIsEditDiv">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confIsEdit" type="checkbox">';
		confHtml+='				<span class="text">可以编辑</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		var confObj = $(confHtml);
		
		$(confObj).find(".confTitle").attr("value",$(_DepartmentDiv).attr("conf_name"));
		if($(_DepartmentDiv).attr("conf_require") && $(_DepartmentDiv).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(_DepartmentDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(_DepartmentDiv).before("<span style='color:red' class='tempClz'>*</span>");	
			}
			
		}
		
		//配置单选
		if($(_DepartmentDiv).attr("conf_isUnique") && $(_DepartmentDiv).attr("conf_isUnique")=='true'){
			$(confObj).find(".confisUnique").attr("checked",true);
			
			//人员默认
			$(confObj).find(".confDefaultDiv").parent().show();
			//配置默认
			if($(_DepartmentDiv).attr("conf_isDefault") && $(_DepartmentDiv).attr("conf_isDefault")=='true'){
				$(confObj).find(".confDefault").attr("checked",true);
				
				$(confObj).find(".confIsEditDiv").parent().show();
				if($(_DepartmentDiv).attr("conf_isEdit") && $(_DepartmentDiv).attr("conf_isEdit")=='false'){
					$(confObj).find(".confIsEdit").attr("checked",false);
				}else{
					$(confObj).find(".confIsEdit").attr("checked",true);
				}
				
			}else{
				//人员多选没有 不可编辑
				$(confObj).find(".confIsEditDiv").parent().hide();
			}
			
		}else{
			//人员多选没有默认
			$(confObj).find(".confDefaultDiv").parent().hide();
			//人员多选没有 不可编辑
			$(confObj).find(".confIsEditDiv").parent().hide();
		}
		
		
		$("#formConfDiv").html(confObj);
		
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_DepartmentDiv).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_DepartmentDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_DepartmentDiv).before("<span style='color:red' class='tempClz'>*</span>")
				$(_DepartmentDiv).attr("conf_require","true")
			}else{
				$(_DepartmentDiv).attr("conf_require","false")
			}
		})
		
		//人员单选设置
		$(confObj).on("click",".confisUnique",function(){
			if($(this).attr("checked")){
				
				$(_DepartmentDiv).attr("conf_isUnique","true");//是否默认为当前时间
				
				$(_DepartmentDiv).attr("conf_isDefault","false");//没有默认
				$(confObj).find(".confDefaultDiv").parent().show();
				$(confObj).find(".confDefault").attr("checked",false);
				
				$(_DepartmentDiv).attr("conf_isEdit","true");//可以编辑
				$(confObj).find(".confIsEditDiv").parent().hide();
				$(confObj).find(".confIsEdit").attr("checked",true);
				
			}else{
				$(_DepartmentDiv).attr("conf_isUnique","false");//多选
				
				$(_DepartmentDiv).attr("conf_isDefault","false");//没有默认
				$(confObj).find(".confDefaultDiv").parent().hide();
				$(confObj).find(".confDefault").attr("checked",false);
				
				$(_DepartmentDiv).attr("conf_isEdit","false");//可以编辑
				$(confObj).find(".confIsEditDiv").parent().hide();
				$(confObj).find(".confIsEdit").attr("checked",true);
				
			}
			
		})
		
		$(confObj).on("click",".confDefault",function(){
			if($(this).attr("checked")){
				$(_DepartmentDiv).attr("conf_isDefault","true");//是否默认为当前时间
				
				$(_DepartmentDiv).attr("conf_isEdit",'true');//可以编辑
				$(confObj).find(".confIsEditDiv").parent().show();
				$(confObj).find(".confIsEdit").attr("checked",true);
			}else{
				$(_DepartmentDiv).attr("conf_isDefault","false");//是否默认为当前时间
				
				$(_DepartmentDiv).attr("conf_isEdit",'true');//可以编辑
				$(confObj).find(".confIsEditDiv").parent().hide();
				$(confObj).find(".confIsEdit").attr("checked",true);
			}
			
		})
		
		$(confObj).on("click",".confIsEdit",function(){
			if($(this).attr("checked")){
				$(_DepartmentDiv).attr("conf_isEdit","true");//是否默认为当前时间
			}else{
				$(_DepartmentDiv).attr("conf_isEdit","false");//是否默认为当前时间
			}
		})
		baidu.editor.plugins[thePlugins].editdom = _DepartmentDiv;
	}
};
/**
 * 运算控件
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['monitor'] = function () {
	var me = this,thePlugins = 'monitor';
	me.commands[thePlugins] = {
		execCommand:function () {//执行添加控件命令
			
			var layoutDetail = $("#preFormComponent").data("layoutDetail");
			
			var _monitorInput  = $("<input type='text' style='width:50px'/>");
			_monitorInput.attr("easyWinCompon","Monitor");
			_monitorInput.attr("readonly",true);
			$(_monitorInput).attr("conf_size","medium")
			
			var tempId = "field_"+new Date().getTime();
			if(layoutDetail){
				constrPreData(layoutDetail,_monitorInput);
				
				var tempIdT = layoutDetail.tempId;
				tempId = "field_"+tempIdT;
				_monitorInput.attr("tempId",tempId)
				var required = layoutDetail.required;
				if(required && required=='true'){
					var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
					UE.getEditor('editor').execCommand('insertHtml',$(_spanRequire).prop("outerHTML"));
				}
				UE.getEditor('editor').execCommand('insertHtml',$(_monitorInput).prop("outerHTML"));
			}else{
				
				_monitorInput.attr("tempId",tempId)
				
				_monitorInput.attr("conf_name","运算控件");
				_monitorInput.attr("conf_require","false");
				_monitorInput.attr("conf_monitorType","date");//默认监控类型日期
				_monitorInput.attr("conf_calTimeType","1");//默认计算工作时间
				
				
				UE.getEditor('editor').execCommand('insertHtml',$(_monitorInput).prop("outerHTML"))
			}
			
			var range = this.selection.getRange();
			var commonAnchorEl = range.getCommonAncestor();
			var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
			$(anchorEl).css("width","60%");
			confConpone(anchorEl,thePlugins);//编辑控件
			
		}
	};
	
	var constrPreData = function(layoutDetail,_html){
		var componKey = layoutDetail.componentKey;
    	var fieldId = layoutDetail.fieldId;
    	var tempId = layoutDetail.tempId;
    	
    	var required = layoutDetail.required;
    	var title = layoutDetail.title;
    	
    	_html.attr("fieldId",fieldId);
		_html.attr("conf_name",title);
    	_html.attr("conf_require",required);//必填
    	_html.attr("placeholder",title);
    	
    	var monitorFields = layoutDetail.monitorFields;
    	
    	var monitorStr = "field_"+monitorFields[0].value;
    	_html.attr("conf_monitorTempDateFields",monitorStr);
		
    	_html.attr("conf_monitorType",layoutDetail.monitorType);//默认监控类型日期
    	_html.attr("conf_calTimeType",layoutDetail.calTimeType);//默认计算工作时间
	}
	
	//提示框
	var popup = new baidu.editor.ui.Popup({
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			  confConpone(popup.anchorEl,thePlugins);//编辑控件
			  this.hide();//隐藏操作
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				var pre = $(this.anchorEl).prev();
				if(pre && $(pre).prop("tagName")){
					$(pre).hasClass("tempClz")?$(pre).remove():"";	
				}
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener( 'mouseover', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /input/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				var html = popup.formatHtml(
				'<nobr>运算控件: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				if ( html ) {
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor(popup.anchorEl ,100);
				} else {
					popup.hide();
				}
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	me.addListener( 'click', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /input/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				confConpone(el,thePlugins);//编辑控件
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	
	var confConpone = function(_monitorInput,thePlugins){
		var confHtml= '<div>';
		confHtml+='		<div class="form-title-mm">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">标题</p>';
			confHtml+='		</div>';
			confHtml+='		<div class="form-text-mm">';
			confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">组件宽度</p>';
			confHtml+='		</div>';
			confHtml+='		<div class="btn-group confSizeDiv">';
			confHtml+='			<a href="javascript:void(0)" class="btn btn-default">小尺寸</a>';
			confHtml+='			<a href="javascript:void(0)" class="btn btn-default">标准尺寸</a>';
			confHtml+='			<a href="javascript:void(0)" class="btn btn-default">大尺寸</a>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">监控类型</p>';
			confHtml+='		</div>';
			confHtml+='		<div class="radio">';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confMonitorType" value="number" type="radio" name="confMonitorType">';
			confHtml+='				<span class="text">数字之和</span>';
			confHtml+='			</label>';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confMonitorType" value="date" type="radio" name="confMonitorType">';
			confHtml+='				<span class="text">日期区间</span>';
			confHtml+='			</label>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			confHtml+='<div class="form-title-mm confCalFieldsDiv">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">监控字段</p>';
			confHtml+='		</div>';
			confHtml+='		<div class="select dateField">';
			confHtml+='			<select>';
			confHtml+='			</select>';
			confHtml+='		</div>';
			confHtml+='		<div class="select numField">';
			confHtml+='		</div>';
			
			confHtml+='</div>';
			
			
			confHtml+='<div class="form-title-mm confCalTimeTypeDiv">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">计算时段设置</p>';
			confHtml+='		</div>';
			confHtml+='		<div class="radio">';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confCalTimeType" value="1" type="radio" name="confCalTimeType">';
			confHtml+='				<span class="text">工作时段</span>';
			confHtml+='			</label>';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confCalTimeType" value="2" type="radio" name="confCalTimeType">';
			confHtml+='				<span class="text">非工作时段</span>';
			confHtml+='			</label>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			confHtml+='</div>';
			
		var confObj = $(confHtml);
		
		$(confObj).find(".confTitle").attr("value",$(_monitorInput).attr("conf_name"));
		
		//取得所有的日期区间
		var monitorFieldList = function(monitorType){
			//候选数据
			var candidates = new Array();
			
			var compone = monitorType=='date'?"DateInterval":"NumberComponent";
			
			
			if(monitorType=='date'){
				var parentDataTable = $(_monitorInput).parents("[easyWinCompon='DataTable']");
				var parentTable = $(_monitorInput).parents("table");
				var parentNormal = $(_monitorInput).parents("p");
				
				if($(parentDataTable).length>0){//属于子表单数据
					var elements = $(parentDataTable).find("[easyWinCompon='"+compone+"']");
					if(elements && elements.length>0){
						$.each(elements,function(index,opt){
							var tempId = $(this).attr("tempId");
							var fieldId = $(this).attr("fieldId");
							var optName = $(this).attr("conf_name");
							var option = {"fieldId":fieldId,"name":optName,"tempId":tempId};
							candidates.push(option);
						})
					}
				}else if($(parentTable).length>0){//属于表单数据
					var elements = $(parentTable).find("[easyWinCompon='"+compone+"']").filter(function(){
						return $(this).parents("[easyWinCompon='DataTable']").length == 0 
					})
					if(elements && elements.length>0){
						$.each(elements,function(index,opt){
							var tempId = $(this).attr("tempId");
							var fieldId = $(this).attr("fieldId");
							var optName = $(this).attr("conf_name");
							var option = {"fieldId":fieldId,"name":optName,"tempId":tempId};
							candidates.push(option);
						})
					}
					
				}else{//属于普通数据
					var elements = $(parentNormal).find("[easyWinCompon='"+compone+"']").filter(function(){
						return $(this).parents("[easyWinCompon='DataTable']").length == 0 && $(this).parents("table").length == 0
					})
					if(elements && elements.length>0){
						$.each(elements,function(index,opt){
							var tempId = $(this).attr("tempId");
							var fieldId = $(this).attr("fieldId");
							var optName = $(this).attr("conf_name");
							var option = {"fieldId":fieldId,"name":optName,"tempId":tempId};
							candidates.push(option);
						})
					}
					
				}
				
			}else if (monitorType=='number'){
				var parentDataTable = $(_monitorInput).parents("[easyWinCompon='DataTable']");
				if($(parentDataTable).length>0){//属于子表单数据
					var elements = $(parentDataTable).find("[easyWinCompon='"+compone+"']");
					if(elements && elements.length>0){
						$.each(elements,function(index,opt){
							var tempId = $(this).attr("tempId");
							var fieldId = $(this).attr("fieldId");
							var optName = $(this).attr("conf_name");
							var option = {"fieldId":fieldId,"name":optName,"tempId":tempId};
							candidates.push(option);
						})
					}
					return candidates;
				}
				
				var parentTable = $(_monitorInput).parents("body");
				if($(parentTable).length>0){//属于表单数据
					var elements = $(parentTable).find("[easyWinCompon='"+compone+"']");
					if(elements && elements.length>0){
						$.each(elements,function(index,opt){
							var tempId = $(this).attr("tempId");
							var fieldId = $(this).attr("fieldId");
							var optName = $(this).attr("conf_name");
							var option = {"fieldId":fieldId,"name":optName,"tempId":tempId};
							candidates.push(option);
						})
					}
					//计算控件的选取
					elements = $(parentTable).find("[easyWinCompon='Monitor']");
					if(elements && elements.length>0){
						//数据准备
						var allMonitorRelatesub = {};
						$.each(elements,function(index,opt){
							
							var tempId = $(this).attr("tempId");
							
							var monitorNumFields = $(this).attr("conf_monitorTempNumFields");
							
							var monitortype = $(this).attr("conf_monitortype");
							if(monitortype == 'date' || !monitorNumFields){
								allMonitorRelatesub[tempId] = [];
								return true;
							}
							var monitorRelatesub = allMonitorRelatesub[tempId];
							if(!monitorRelatesub || !monitorRelatesub[0]){
								monitorRelatesub = [];
								monitorNumFields = monitorNumFields.replace(/\[|]/g,'')
								var monitorNumFieldArray = monitorNumFields.split(",");
								$.each(monitorNumFieldArray,function(index,tempObjId){
									tempObjId = $.trim(tempObjId);
									var subMonitor = $(parentTable).find("[easyWinCompon='Monitor'][tempId='"+tempObjId+"']");
									if(subMonitor && subMonitor.get(0)){
										monitorRelatesub.push(tempObjId);
									}
								})
								
								var subArray = constrMonitorElement(monitorNumFieldArray,allMonitorRelatesub,$(parentTable));
								monitorRelatesub = monitorRelatesub.concat(subArray);
								allMonitorRelatesub[tempId] = monitorRelatesub
							}
						})
						
						var thisTempId = $(_monitorInput).attr("tempId");
						//可以添加的计算控件
						$.each(elements,function(index,opt){
							var tempId = $(this).attr("tempId");
							if(thisTempId == tempId){
								return true;
							}
							var p = allMonitorRelatesub[tempId];
							if(p.indexOf(thisTempId)>=0){
								return true;
							}else{
								var fieldId = $(this).attr("fieldId");
								var optName = $(this).attr("conf_name");
								var option = {"fieldId":fieldId,"name":optName,"tempId":tempId};
								
								candidates.push(option);
							}
							
						});
					}
				}
				
			}
			return candidates;
		};
		//设置布局
		var monitorType = $(_monitorInput).attr("conf_monitorType");
		if(monitorType=="date"){
			
			var monitorDateFields = $(_monitorInput).attr("conf_monitorTempDateFields");//计算的控件
			
			$(confObj).find(".confMonitorType:eq(1)").attr("checked",true);
			$(confObj).find(".confCalTimeType:eq(0)").attr("checked",true);
			
			$(confObj).find(".confCalFieldsDiv").find(".numField").hide();
			
			$(confObj).find(".confCalFieldsDiv").find(".dateField").show();
			$(confObj).find(".confCalTimeTypeDiv").show();
			
			var DateInternals = monitorFieldList(monitorType);
			if(DateInternals && DateInternals.length>0){
				$.each(DateInternals,function(componeIndex,componeObj){
					var option = $("<option></option>");
					option.attr("fieldId",componeObj.fieldId)
					option.attr("tempId",componeObj.tempId)
					option.html(componeObj.name);
					$(confObj).find(".dateField>select").append(option);
				})
				if(monitorDateFields){
					$(confObj).find(".dateField>select").find("[tempId='"+monitorDateFields+"']").attr("selected",true);
				}else{
					$(confObj).find(".dateField>select").find("option:eq(0)").attr("selected",true);
					$(_monitorInput).attr("conf_monitorTempDateFields",$(confObj).find(".dateField>select").find("option:eq(0)").attr("tempId"))
				}
			}
		}else{
			var monitorNumFields = $(_monitorInput).attr("conf_monitorTempNumFields");//计算的控件
			
			$(confObj).find(".confMonitorType:eq(0)").attr("checked",true);
			
			$(confObj).find(".confCalFieldsDiv").find(".dateField").hide();
			$(confObj).find(".confCalTimeTypeDiv").hide();
			
			$(confObj).find(".confCalFieldsDiv").find(".numField").show();
			
			var NumCompones = monitorFieldList(monitorType);
			
			if(NumCompones && NumCompones.length>0){
				$.each(NumCompones,function(componeIndex,componeObj){
					var checkBox ='	<label>';
					checkBox+='		<input class="inverted confCalNum" type="checkbox" name="confCalNum" fieldId="'+componeObj.fieldId+'" tempId="'+componeObj.tempId+'"/>';
					checkBox+='		<span class="text">'+componeObj.name+'</span>';
					checkBox+='	</label><br/>';
					
					$(confObj).find(".numField").append($(checkBox));
				})
				if(monitorNumFields){
					monitorNumFields = monitorNumFields.replace(/\[|]/g,'')
					var monitorNumFieldArray = monitorNumFields.split(",");
					$.each(monitorNumFieldArray,function(index,tempObjId){
						tempObjId = $.trim(tempObjId);
						$(confObj).find(".numField").find("input[tempId='"+tempObjId+"']").attr("checked",true);
					})
					
				}
				
			}
		}
		
		
		var conf_size = $(_monitorInput).attr("conf_size");
		conf_size = conf_size?conf_size:"medium";
		
		if(conf_size == 'small'){
			$(confObj).find(".confSizeDiv").find("a:eq(0)").removeClass("btn-default").addClass("btn-blue");
		}else if(conf_size == 'medium'){
			$(confObj).find(".confSizeDiv").find("a:eq(1)").removeClass("btn-default").addClass("btn-blue");
		}else if(conf_size == 'large'){
			$(confObj).find(".confSizeDiv").find("a:eq(2)").removeClass("btn-default").addClass("btn-blue");
		}
		
		$("#formConfDiv").html(confObj);
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_monitorInput).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_monitorInput).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_monitorInput).before("<span style='color:red' class='tempClz'>*</span>")
				$(_monitorInput).attr("conf_require","true")
			}else{
				$(_monitorInput).attr("conf_require","false")
			}
		})
		
		$(confObj).on("click",".confSizeDiv>a",function(){
			
			$(confObj).find(".confSizeDiv").find("a").removeClass("btn-blue");
			$(confObj).find(".confSizeDiv").find("a").removeClass("btn-default");
			$(confObj).find(".confSizeDiv").find("a").addClass("btn-default");
			$(this).removeClass("btn-default").addClass("btn-blue");
			var index = $(this).index();
			if(index==0){
				$(_monitorInput).css("width","30%");
				$(_monitorInput).attr("conf_size","small");
			}else if(index==1){
				$(_monitorInput).css("width","60%");
				$(_monitorInput).attr("conf_size","medium");
			}else if(index==2){
				$(_monitorInput).css("width","95%");
				$(_monitorInput).attr("conf_size","large");
			}
		})
		
		//计算时间类型
		$(confObj).on("click",".confCalTimeType",function(){
			var calTimeType = $(this).val();
			$(_monitorInput).attr("conf_calTimeType",calTimeType);
		})
		//计算时段设置
		$(confObj).on("click",".confMonitorType",function(){
			var calTimeType = $(this).val();
			var monitorType = $(_monitorInput).attr("conf_calTimeType");
			if(calTimeType == monitorType){
				return;
			}
			$(_monitorInput).attr("conf_monitorType",calTimeType);//默认监控类型日期
			if('date' == calTimeType){
				confConpone(_monitorInput,thePlugins);//编辑控件
			}else{
				confConpone(_monitorInput,thePlugins);//编辑控件
			}
		})
		
		//计算对象变动
		$(confObj).on("change",".dateField>select",function(){
			$(_monitorInput).attr("conf_monitorTempDateFields",$(this).find("option:selected").attr("tempId"))
		})
		
		
		//计算数字的字段修改
		$(confObj).on("click",".confCalNum",function(){
			var checkObjs= $(this).parents(".numField").find(":checkbox[name='confCalNum']:checked");
			var conf_monitorTempNumFields = "";
			if(checkObjs && checkObjs[0]){
				$.each(checkObjs,function(){
					var tempObjId = $.trim($(this).attr("tempId"));
					conf_monitorTempNumFields = conf_monitorTempNumFields+","+tempObjId;
				})
				conf_monitorTempNumFields = conf_monitorTempNumFields.substring(1);
			}
			$(_monitorInput).attr("conf_monitorTempNumFields",conf_monitorTempNumFields);//计算的控件
			
		});
		
		
		baidu.editor.plugins[thePlugins].editdom = _monitorInput;
	}
	
	var constrMonitorElement = function(monitorNumFieldArray,allMonitorRelatesub,parentTable){
		var result = [];
		$(monitorNumFieldArray,function(index,tempId){
			//计算控件的选取
			var _this = $(parentTable).find("[easyWinCompon='Monitor'][tempId='"+tempId+"']");
			
			var monitorNumFields = $(_this).attr("conf_monitorTempNumFields");
			var monitorRelatesub = allMonitorRelatesub[tempId];
			if(!monitorRelatesub || !monitorRelatesub[0]){
				monitorRelatesub = [];
				monitorNumFields = monitorNumFields.replace(/\[|]/g,'')
				var monitorNumFieldArray = monitorNumFields.split(",");
				$.each(monitorNumFieldArray,function(index,tempObjId){
					tempObjId = $.trim(tempObjId);
					var subMonitor = $(parentTable).find("[easyWinCompon='Monitor'][tempId='"+tempObjId+"']");
					if(subMonitor && subMonitor.get(0)){
						monitorRelatesub.push(tempObjId);
					}
				})
				var subArray = constrMonitorElement(monitorNumFieldArray,allMonitorRelatesub,$(parentTable));
				monitorRelatesub = monitorRelatesub.concat(subArray);
				allMonitorRelatesub[tempId] = monitorRelatesub
				
				result = result.concat(subArray);
			}
		})
		return result;
	}
};

UE.plugins['serialnum'] = function () {
	var me = this,thePlugins = 'serialnum';
	me.commands[thePlugins] = {
		execCommand:function () {//执行添加控件命令
			var layoutDetail = $("#preFormComponent").data("layoutDetail");
			var _input  = $("<input type='text' style='width:50px;' placeholder='序列编号'/>");
			_input.attr("easyWinCompon","SerialNum");
			_input.attr("readonly",true);
			_input.attr("conf_size","medium");
			
			var tempId = "field_"+new Date().getTime();
			if(layoutDetail){
				constrPreData(layoutDetail,_input);
				
				var tempIdT = layoutDetail.tempId;
				tempId = "field_"+tempIdT;
				_input.attr("tempId",tempId)
				var required = layoutDetail.required;
				if(required && required=='true'){
					var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
					UE.getEditor('editor').execCommand('insertHtml',$(_spanRequire).prop("outerHTML"));
				}
				UE.getEditor('editor').execCommand('insertHtml',$(_input).prop("outerHTML"));
				
			}else{
				
				_input.attr("tempId",tempId)
				_input.attr("conf_name","序列编号");
				_input.attr("conf_require","false");//必填
				UE.getEditor('editor').execCommand('insertHtml',$(_input).prop("outerHTML"))
			}
			
			var range = this.selection.getRange();
			var commonAnchorEl = range.getCommonAncestor();
			var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
			$(anchorEl).css("width","60%");
			confConpone(anchorEl,thePlugins);//编辑控件
		}
	};
	var constrPreData = function(layoutDetail,_input){
		var componKey = layoutDetail.componentKey;
    	var fieldId = layoutDetail.fieldId;
    	var tempId = layoutDetail.tempId;
    	
    	var required = layoutDetail.required;
    	var title = layoutDetail.title;
    	
    	_input.attr("fieldId",fieldId)
		_input.attr("conf_name",title);
		_input.attr("conf_require",required);//必填
		_input.attr("placeholder",title);
		
		
	}
	//提示框
	var popup = new baidu.editor.ui.Popup({
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			  confConpone(popup.anchorEl,thePlugins);//编辑控件
			  this.hide();//隐藏操作
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				var pre = $(this.anchorEl).prev();
				if(pre && $(pre).prop("tagName")){
					$(pre).hasClass("tempClz")?$(pre).remove():"";	
				}
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener('mouseover', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /input/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				var html = popup.formatHtml(
				'<nobr>序列编号: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				if ( html ) {
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor(popup.anchorEl ,100);
				} else {
					popup.hide();
				}
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	me.addListener( 'click', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /input/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				confConpone(el,thePlugins);//编辑控件
			}else{
				popup.hide();
			}
		}catch(e){}
	});
	var confConpone = function(_input,thePlugins){
		var confHtml= '<div class="form-title-mm">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">标题</p>';
			confHtml+='		</div>';
			confHtml+='		<div class="form-text-mm">';
			confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
			confHtml+='		</div>';
			confHtml+='</div>';
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="checkbox">';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confRequire" type="checkbox">';
			confHtml+='				<span class="text">这个是必填项</span>';
			confHtml+='			</label>';
			confHtml+='		</div>';
			confHtml+='</div>';
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">编号规则</p>';
			confHtml+='		</div>';
			confHtml+='		<div class="btn-group">';
			confHtml+='			<a href="javascript:void(0)" class="btn btn-primary btn-xs serialNumSelect margin-left-5">规则选择</a>';
			confHtml+='		</div>';
			confHtml+='</div>';
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">组件宽度</p>';
			confHtml+='		</div>';
			confHtml+='		<div class="btn-group confSizeDiv">';
			confHtml+='			<a href="javascript:void(0)" confSize="small" class="btn btn-default" style="padding:6px 6px!important;">小尺寸</a>';
			confHtml+='			<a href="javascript:void(0)" confSize="medium" class="btn btn-default" style="padding:6px 9px!important;">标准尺寸</a>';
			confHtml+='			<a href="javascript:void(0)" confSize="large" class="btn btn-default" style="padding:6px 6px!important;">大尺寸</a>';
			confHtml+='		</div>';
			confHtml+='		<div class="btn-group confWidthDefineDiv">';
			confHtml+='			<a href="javascript:void(0)" confSize="confWidthDefineMinus" class="fa fa-minus confWidthDefineMinus"></a>';
			confHtml+='			<input type="text" name="defWidth"/>';
			confHtml+='			<a href="javascript:void(0)" confSize="confWidhtDefineAdd" class="fa fa-plus confWidhtDefineAdd"></a>';
			confHtml+='			<span>px</span>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
		var confObj = $(confHtml);
		
		$(confObj).find(".confTitle").attr("value",$(_input).attr("conf_name"));
		if($(_input).attr("conf_require") && $(_input).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(_input).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(_input).before("<span style='color:red' class='tempClz'>*</span>");	
			}
			
		}
		var serialNumId = $(_input).attr("serialNumId");
		var serialNumRemark = $(_input).attr("serialNumRemark");
		if(serialNumId){
			
			var serialNumShow = $("<span class='serialNumShow pull-left'></span>");
			$(serialNumShow).html(serialNumRemark);
			
			var serialNumShowObj =$(confObj).find(".serialNumSelect").parent().find(".serialNumShow");
			if(serialNumShowObj && serialNumShowObj.get(0) ){
				$(confObj).find(".serialNumSelect").parent().find(".serialNumShow").replaceWith($(serialNumShow));
			}else{
				$(confObj).find(".serialNumSelect").parent().prepend($(serialNumShow));
			}
		}
		
		
		
		var conf_size = $(_input).attr("conf_size");
		conf_size = conf_size?conf_size:"medium";
		
		var defWidth = $(_input).attr("defWidth");
		defWidth = defWidth?defWidth:80;
		
		$(confObj).find(".confWidthDefineDiv").hide();
		if(conf_size == 'small'){
			$(confObj).find(".confSizeDiv").find("a:eq(0)").removeClass("btn-default").addClass("btn-blue");
		}else if(conf_size == 'medium'){
			$(confObj).find(".confSizeDiv").find("a:eq(1)").removeClass("btn-default").addClass("btn-blue");
		}else if(conf_size == 'large'){
			$(confObj).find(".confSizeDiv").find("a:eq(2)").removeClass("btn-default").addClass("btn-blue");
		}
		
		$("#formConfDiv").html(confObj);
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_input).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_input).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_input).before("<span style='color:red' class='tempClz'>*</span>")
				$(_input).attr("conf_require","true")
			}else{
				$(_input).attr("conf_require","false")
			}
		})
		$(confObj).on("click",".confSizeDiv>a",function(){
			
			$(confObj).find(".confWidthDefineDiv").hide();
			
			$(confObj).find(".confSizeDiv").find("a").removeClass("btn-blue");
			$(confObj).find(".confSizeDiv").find("a").removeClass("btn-default");
			$(confObj).find(".confSizeDiv").find("a").addClass("btn-default");
			$(this).removeClass("btn-default").addClass("btn-blue");
			var index = $(this).index();
			if(index==0){
				$(_input).css("width","30%");
				$(_input).attr("conf_size","small");
			}else if(index==1){
				$(_input).css("width","60%");
				$(_input).attr("conf_size","medium");
			}else if(index==2){
				$(_input).css("width","95%");
				$(_input).attr("conf_size","large");
			}
		})
		//减少宽度
		$(confObj).on("click",".confWidthDefineDiv>.confWidthDefineMinus",function(){
			var preWidth = $(confObj).find(".confWidthDefineDiv").find("input[name='defWidth']").val();
			var preWidthLeft = parseInt(preWidth)%10;
			
			if(preWidthLeft>0){
				preWidth = parseInt(preWidth)-preWidthLeft + 10;
			}
			preWidth = parseInt(preWidth)-10
			$(confObj).find(".confWidthDefineDiv").find("input[name='defWidth']").val(preWidth)
			
			$(_input).css("width",preWidth+"px");
			$(_input).attr("defWidth",preWidth);
		});
		
		//添加宽度
		$(confObj).on("click",".confWidthDefineDiv>.confWidhtDefineAdd",function(){
			var preWidth = $(confObj).find(".confWidthDefineDiv").find("input[name='defWidth']").val();
			var preWidthLeft = parseInt(preWidth)%10;
			
			if(preWidthLeft>0){
				preWidth = parseInt(preWidth)-preWidthLeft;
			}
			preWidth = parseInt(preWidth)+10;
			$(confObj).find(".confWidthDefineDiv").find("input[name='defWidth']").val(preWidth);
			
			$(_input).css("width",preWidth+"px");
			$(_input).attr("defWidth",defWidth);
		})
		//序列号选择
		$(confObj).on("click",".serialNumSelect",function(){
			var exceptId = $(_input).attr("serialNumId");
			var param = {
					"sid":EasyWin.sid,
					"exceptId":exceptId
			}
			serialNumSelect(param,function(serialNum){
				if(serialNum){
					$(_input).attr("serialNumId",serialNum.id);
					$(_input).attr("serialNumRemark",serialNum.remark);
					var serialNumShow = $("<span class='serialNumShow pull-left'></span>");
					$(serialNumShow).html(serialNum.remark);
					
					var serialNumShowObj =$(confObj).find(".serialNumSelect").parent().find(".serialNumShow");
					if(serialNumShowObj && serialNumShowObj.get(0) ){
						$(confObj).find(".serialNumSelect").parent().find(".serialNumShow").replaceWith($(serialNumShow));
					}else{
						$(confObj).find(".serialNumSelect").parent().prepend($(serialNumShow));
					}
					
				}
			})
		})
		
		baidu.editor.plugins[thePlugins].editdom = _input;
	}
};


/**
 * 列表控件
 * @command listctrl
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'qrcode');
 * ```
 */
UE.plugins['datatable'] = function () {
    var me = this,thePlugins = 'datatable';
    me.commands[thePlugins] = {
        execCommand:function () {
        	
        	var layoutDetail = $("#preFormComponent").data("layoutDetail");
        	
        	var _subTableDiv = $("<div class='myDiv'></div>")
        	$(_subTableDiv).attr("easyWinCompon","DataTable");
        	
        	var tempId = "field_"+new Date().getTime();
        	
        	if(layoutDetail){
        		constrPreData(layoutDetail,_subTableDiv);
        		
        		var tempIdT = layoutDetail.tempId;
        		tempId = "field_"+tempIdT;
        		UE.getEditor('editor').execCommand('insertHtml',$(_subTableDiv).prop("outerHTML"))
        	}else{
        		_subTableDiv.attr("tempId",tempId)
        		
        		var _tableHeadDiv = $("<div class='sub subTableHead' style='display:table;border:#DDD 1px solid;width:100%'></div>");
        		var _headTr = $("<div style='display:table-row;'></div>");
        		
        		var _headTdMod = $("<div class='tdItem' style='display:table-cell;height:30px;border: 1px solid #DDD;text-align:center'></div>");
        		for(var i=0;i<3;i++){
        			var _headTd = $(_headTdMod).clone();
        			var _span = $("<span></span>")
        			_span.html("项目")
        			_span.attr("conf_name","项目");
        			$(_span).attr("easyWinCompon","subItemTitle");
        			_span.attr("tempId","field_sub"+i+"_"+new Date().getTime())
        			_headTd.append(_span)
        			
        			$(_headTr).append(_headTd)
        		}
        		
        		$(_tableHeadDiv).append(_headTr);
        		
        		var _tableBodyDiv = $("<div class='sub subTableBody' style='display:table;border:#DDD 1px solid;width:100%;border-top:0px;text-align:center;vertical-align: middle;'></div>");
        		var _bodyTr = $("<div style='display:table-row;border-top:0px'></div>");
        		
        		var _bodyTdMod = $("<div class='tdItem' style='display:table-cell;height:30px;border:1px solid #DDD;border-top:0px;text-align:center;vertical-align: middle;padding: 5px 5px'></div>");
        		for(var i=0;i<3;i++){
        			var _bodyTd = $(_bodyTdMod).clone();
        			$(_bodyTr).append(_bodyTd)
        		}
        		$(_tableBodyDiv).append(_bodyTr);
        		
        		$(_subTableDiv).append(_tableHeadDiv);
        		$(_subTableDiv).append(_tableBodyDiv);
        		
        		var width = (100-100%3)/3;
        		$(_subTableDiv).find(".tdItem").css("width",width+"%");
        		$(_subTableDiv).find(".tdItem").css("word-break","break-all");
        		
        		$(_subTableDiv).attr("conf_name","子列表");
        		
        		UE.getEditor('editor').execCommand('insertHtml',$(_subTableDiv).prop("outerHTML"))
        	}
        	
        	var range = this.selection.getRange();
			var commonAnchorEl = range.getCommonAncestor();
			var anchorEl = $(commonAnchorEl).parent().parent().find("[tempId='"+tempId+"']").get(0);
			confConpone(anchorEl,thePlugins);//编辑控件
        	
        }
    };
    
    var constrPreData = function(layoutDetail,_html){
    	
    	var size = layoutDetail.size;
    	
		var componKey = layoutDetail.componentKey;
    	var fieldId = layoutDetail.fieldId;
    	var tempId = layoutDetail.tempId;
    	
    	var required = layoutDetail.required;
    	var title = layoutDetail.title;
    	title = title?title:"子列表";
    	_html.attr("fieldId",fieldId)
    	_html.attr("subFormId",layoutDetail.subFormId)
    	_html.attr("tempId","field_"+tempId)
		_html.attr("conf_name",title);
    	
    	var _tableHeadDiv = $("<div class='sub subTableHead' style='display:table;border:#DDD 1px solid;width:100%'></div>");
    	var _headTr = $("<div style='display:table-row;'></div>");
    	var _headTdMod = $("<div class='tdItem' style='display:table-cell;height:30px;border: 1px solid #DDD;text-align:center'></div>");
    	
    	var _tableBodyDiv = $("<div class='sub subTableBody' style='display:table;border:#DDD 1px solid;width:100%;border-top:0px'></div>");
    	var _bodyTr = $("<div style='display:table-row;border-top:0px'></div>");
    	var _bodyTdMod = $("<div class='tdItem' style='display:table-cell;height:30px;border:1px solid #DDD;border-top:0px'></div>");
    	
    	var sublayoutDetails = layoutDetail.layoutDetail;
    	$.each(sublayoutDetails,function(index,sublayoutDetail){
    		
    		var _headTd = $(_headTdMod).clone();
    		
    		var _itemTitleDiv = $("<span></span>");
    		_itemTitleDiv.attr("easyWinCompon","subItemTitle");
			_itemTitleDiv.attr("conf_name",sublayoutDetail.title);
			_itemTitleDiv.attr("tempId","field_sub"+index+"_"+new Date().getTime())
    		_itemTitleDiv.html(sublayoutDetail.title);
    		_headTd.html(_itemTitleDiv)
    		
    		$(_headTr).append(_headTd);
    		
    		var _bodyTd = $(_bodyTdMod).clone();
			$(_bodyTr).append(_bodyTd);
    		
    	})
    	$(_tableHeadDiv).append(_headTr);
    	$(_tableBodyDiv).append(_bodyTr);
    	
    	
    	$(_html).append(_tableHeadDiv);
    	$(_html).append(_tableBodyDiv);
    	
    	var width = (100-100%size)/size;
    	$(_html).find(".tdItem").css("width",width+"%");
    	$(_html).find(".tdItem").css("word-break","break-all");
    	
	}
    var popup = new baidu.editor.ui.Popup( {
        editor:this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
        	confConpone(popup.anchorEl,thePlugins);//编辑控件
			this.hide();//隐藏操作
        },
        _delete:function(){
            if( window.confirm('确认删除该控件吗？') ) {
                baidu.editor.dom.domUtils.remove(this.anchorEl,false);
            }
            this.hide();
        }
    } );
    popup.render();
    me.addListener( 'mouseover', function( t, evt ) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        try{
        	rel = $(el).hasClass("tdItem")?el.parentNode.parentNode.parentNode:el;
        	var easyWinCompon = rel.getAttribute('easyWinCompon');
        	easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
        	if ( /div/ig.test( rel.tagName ) && easyWinCompon==thePlugins) {
        		var html = popup.formatHtml(
        		'<nobr>明细子表: <span onclick=$$._edittext() class="edui-clickable">编辑子表</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除子表</span></nobr>' );
        		if ( html ) {
        			popup.getDom( 'content' ).innerHTML = html;
        			popup.anchorEl = rel;
        			popup.anchorSubEl = el;
        			popup.showAnchor( rel );
        		} else {
        			popup.hide();
        		}
        	}else{
        		popup.hide();
        	}
        }catch(e){}
    });
    
    var confConpone = function(_subTableDiv,thePlugins){
    	var confHtml= '<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">标题</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="form-text-mm">';
		confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm confSysTable">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">采用的数据表</p>';
		confHtml+='		</div>';
		//点击事件在editFormMod.js中
		confHtml+='		<div class="select">';
		confHtml+='			<select id="relateTable">';
		confHtml+='				<option value="">不采用</option>';
		confHtml+='				<option value="Consume">消费记录表</option>';
		confHtml+='			</select>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm bordered-bottom-gray">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">选项内容</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="">';
		confHtml+='			<div class="optListul margin-right-10" style="border:1px solid #ddd;">';
		
		confHtml+='			</div>';
		confHtml+='			<div class="add-line-box">';
		confHtml+='				<a href="javascript:void(0)" class="text-align-center add-btn-content addOpt"><i class="fa fa-plus blue"></i> &nbsp;添加选项</a>';
		confHtml+='			</div>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		var confObj = $(confHtml);
		
		var conf_relateTable = $(_subTableDiv).attr("conf_relateTable");
		//设置采用的字典表
		$(confObj).find("#relateTable").data("preVal",conf_relateTable);
		$(confObj).find("#relateTable").val(conf_relateTable);
		
		
		$(confObj).find(".confTitle").attr("value",$(_subTableDiv).attr("conf_name"));
		
		var options = $(_subTableDiv).find(".subTableHead").find("[easyWinCompon]");
		if(options && options.length>0){
			$.each(options,function(optIndex,optObj){
				var _li = $('<div class="checkbox clearfix OptOneDiv"></div>');
				var _span = $("<span class='pull-left form-input form-input-rel rowIndex'>"+(optIndex+1)+"</span>");
				
				var _content = $("<div class='pull-left form-write'></div>");
				var _input = $('<input class="line-input" type="text">');
				$(_input).attr("readonly",true);
				_content.append($(_input));
				_content.find("input").attr("value",$(optObj).attr("conf_name"))
				
				$(_li).append(_span);
				$(_li).append(_content);
				
				var opt = $('<div class="pull-left form-iconic text-right"></div>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-down fa-form-icon downOpt padding-right-5"></a>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-up fa-form-icon upOpt padding-right-5"></a>');
				
				if(!conf_relateTable){//采用的系统表不能删除
					$(opt).append('<a href="javascript:void(0)" class="fa fa-times fa-times-circle fa-form-delet red delOpt"></a>');
				}
				$(_li).append(opt);
				
				$(confObj).find(".optListul").append(_li);
			})
		}
		$("#formConfDiv").html(confObj);
		var myScroll = $("#formConfDiv").find(".optListul").slimScroll({
	        height:"148px",
	        alwaysVisible: true,
	        disableFadeOut: false
	    });
		
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_subTableDiv).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		
		//修改选项
		$(confObj).on("blur",".line-input",function(){
			
			var confName = $(this).val();
			if(!confName){
				layer.tips("请填写",$(this),{tips:1});
				return false;
			}
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			var selectLable = $(_subTableDiv).find(".subTableHead").find(".tdItem:eq("+lableIndex+")").find("[easyWinCompon]");
			
			$(selectLable).attr("conf_name",confName)
			$(selectLable).html(confName)
		})
		//添加选项
		$(confObj).on("click",".addOpt",function(){
			var relateTable = $("#relateTable").val();
			if(relateTable){
				//在editFormMod.js中实现选择
				chooseSysColumn(relateTable,function(data){
					if(data && data[0]){
						constrDataSubTable(_subTableDiv,confObj,myScroll,data);
						$("#relateTable").data("preVal",relateTable);
					}else{
						var preVal = $("#relateTable").data("preVal");
						if(!preVal){
							constrDataSubTable(_subTableDiv,confObj,myScroll,data);
						}
					}
					
				})
				return;
			}
			
			var mod = $(this).parent().prev().find(".OptOneDiv:eq(0)").clone();
			$(mod).find("input").val('项目');
			
			$(confObj).find(".optListul").append(mod);
			
			$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
				$(this).find(".rowIndex").html((index+1));
			})
			
			var headTdItem = $(_subTableDiv).find(".subTableHead").find(".tdItem:last").clone();
			headTdItem.html("")
			var _span = $("<span></span>")
    		_span.html("项目")
    		_span.attr("conf_name","项目");
    		$(_span).attr("easyWinCompon","subItemTitle");
    		_span.attr("tempId","field_sub_"+new Date().getTime())
    		headTdItem.append(_span)
			$(_subTableDiv).find(".subTableHead").find(".tdItem:last").after(headTdItem);
			
			var bodyTdItem = $(_subTableDiv).find(".subTableBody").find(".tdItem:last").clone();
			bodyTdItem.html("")
			$(_subTableDiv).find(".subTableBody").find(".tdItem:last").after(bodyTdItem);
			
			var len = $(_subTableDiv).find(".subTableHead").find(".tdItem").length;
			var width = (100-100%len)/len;
        	$(_subTableDiv).find(".tdItem").css("width",width+"%");
        	
        	var height = $(confObj).find(".optListul").height();
        	myScroll.slimscroll({
		        scrollTo:height+'px',
		        alwaysVisible: true,
		        disableFadeOut: false
		    });
			
		})
		
		//修改选项
		$(confObj).on("click",".delOpt",function(){
			var len = $(".optListul").find(".OptOneDiv").length;
			if(len<=1){
				layer.tips("最后一个选项，不能删除",$(this),{tips:1});
				return;
			}
			var removeIndex = $(this).parents(".OptOneDiv").index();
			$(this).parents(".OptOneDiv").remove();
			
			$(_subTableDiv).find(".subTableHead").find(".tdItem:eq("+removeIndex+")").remove();
			$(_subTableDiv).find(".subTableBody").find(".tdItem:eq("+removeIndex+")").remove();
		})
		
		//上移选项
		$(confObj).on("click",".upOpt",function(){
			
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			if(lableIndex > 0){//不是第一个元素
				var _thisP = $(this).parents(".OptOneDiv");
				_thisP.prev().before(_thisP);
				
				$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
					$(this).find(".rowIndex").html((index+1));
				})
				
				var _headP = $(_subTableDiv).find(".subTableHead").find(".tdItem:eq("+lableIndex+")");
				_headP.prev().before(_headP);
			    var _bodyP = $(_subTableDiv).find(".subTableBody").find(".tdItem:eq("+lableIndex+")");
			    _bodyP.prev().before(_bodyP);
			}
		})
		
		//下移选项
		$(confObj).on("click",".downOpt",function(){
			
			var lableIndex =  $(this).parents(".OptOneDiv").index();
			var len = $(this).parents(".OptOneDiv").parent().find(".OptOneDiv").length - 1;
			if(lableIndex < len){//不是第一个元素
				var _thisP = $(this).parents(".OptOneDiv");
				_thisP.next().after(_thisP);
				
				$.each($(confObj).find(".optListul").find(".OptOneDiv"),function(index,object){
					$(this).find(".rowIndex").html((index+1));
				})
				
				var _headP = $(_subTableDiv).find(".subTableHead").find(".tdItem:eq("+lableIndex+")");
				_headP.next().after(_headP);
			    var _bodyP = $(_subTableDiv).find(".subTableBody").find(".tdItem:eq("+lableIndex+")");
			    _bodyP.next().after(_bodyP);
			}
		})
		
		//采用的数据表变动后
		$(confObj).on("change","#relateTable",function(){
			var relateTable = $(this).val();
			if(relateTable){
				//在editFormMod.js中实现选择
				chooseSysColumn(relateTable,function(data){
					if(data && data[0]){
						constrDataSubTable(_subTableDiv,confObj,myScroll,data);
						$("#relateTable").data("preVal",relateTable);
						$(_subTableDiv).attr("conf_relateTable",relateTable);
						
					}else{
						var preVal = $("#relateTable").data("preVal");
						if(!preVal){
							constrDataSubTable(_subTableDiv,confObj,myScroll,data);
						}
						$(_subTableDiv).attr("conf_relateTable",preVal);
					}
				})
			}else{
				constrDataSubTable(_subTableDiv,confObj,myScroll,null);
				$("#relateTable").data("preVal",relateTable);
				$(_subTableDiv).attr("conf_relateTable",relateTable);
			}
		})
		
		baidu.editor.plugins[thePlugins].editdom = _subTableDiv;
    }
    
    //重新构建布局信息
    var constrDataSubTable = function(_subTableDiv,confObj,myScroll,data){
    	
    	//清空选项内容
		$(confObj).find(".optListul").empty();
		
    	if(data && data[0]){
			//开始渲染
			var length = data.length;
			//添加标题数据
			var headTdItemMod = $(_subTableDiv).find(".subTableHead").find(".tdItem:last").clone();
			headTdItemMod.html("");
			//添加内容数据
			var bodyTdItemMod = $(_subTableDiv).find(".subTableBody").find(".tdItem:last").clone();
			bodyTdItemMod.html("");
			//需要添加标题的位置
			var headTdItemP =  $(_subTableDiv).find(".subTableHead").find(".tdItem:first").parent();
			//需要添加内容的位置
			var bodyTdItemP =  $(_subTableDiv).find(".subTableBody").find(".tdItem:first").parent();
			
			
			//清空标题
			$(_subTableDiv).find(".subTableHead").find(".tdItem").remove();
			//清空内容
			$(_subTableDiv).find(".subTableBody").find(".tdItem").remove();
			
			//遍历本次结果数据
			$.each(data,function(index,obj){
				//标题信息
				var _span = $("<span></span>")
	    		_span.html(obj.title);
	    		_span.attr("conf_name",obj.title);
	    		$(_span).attr("easyWinCompon","subItemTitle");
	    		_span.attr("tempId","field_sub"+index+"_"+new Date().getTime());
	    		var headTdItem = $(headTdItemMod).clone();
	    		headTdItem.append(_span);
	    		//添加标题
				$(headTdItemP).append(headTdItem);
				
	    		var array = new Array();
				array.push(obj.type);
				array.push(obj);
				//内容信息
				var bodyTdItem = $(bodyTdItemMod).clone();
	    		var html = easyWinEditor.constrSubHtml(obj.type,array);
				bodyTdItem.html(html)
				//添加内容
				$(bodyTdItemP).append(bodyTdItem);
				
				//设置选项
				var _li = $('<div class="checkbox clearfix OptOneDiv"></div>');
				var _span = $("<span class='pull-left form-input form-input-rel rowIndex'>"+(index+1)+"</span>");
				
				var _content = $("<div class='pull-left form-write'></div>");
				_content.append('<input class="line-input" type="text" readonly="readonly">');
				_content.find("input").attr("value",obj.title);
				
				$(_li).append(_span);
				$(_li).append(_content);
				
				var opt = $('<div class="pull-left form-iconic text-right"></div>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-down fa-form-icon downOpt padding-right-5"></a>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-up fa-form-icon upOpt padding-right-5"></a>');
//				$(opt).append('<a href="javascript:void(0)" class="fa fa-times fa-times-circle fa-form-delet red delOpt"></a>');
				$(_li).append(opt);
				
				$(confObj).find(".optListul").append(_li);
				
			})
			
			var len = $(_subTableDiv).find(".subTableHead").find(".tdItem").length;
			var width = (100-100%len)/len;
        	$(_subTableDiv).find(".tdItem").css("width",width+"%");
        	
		}else{
			$(confObj).find("#relateTable").find("option:eq(0)").attr("selected",true);
			//初始渲染
			var headTdItemP =  $(_subTableDiv).find(".subTableHead").find(".tdItem:first").parent();
			var bodyTdItemP =  $(_subTableDiv).find(".subTableBody").find(".tdItem:first").parent();
			
			$(_subTableDiv).find(".subTableHead").find(".tdItem").remove();
			$(_subTableDiv).find(".subTableBody").find(".tdItem").remove();
			
			var _headTdMod = $("<div class='tdItem' style='display:table-cell;height:30px;border: 1px solid #DDD;text-align:center'></div>");
    		for(var i=0;i<3;i++){
    			var _headTd = $(_headTdMod).clone();
    			var _span = $("<span></span>")
    			_span.html("项目")
    			_span.attr("conf_name","项目");
    			$(_span).attr("easyWinCompon","subItemTitle");
    			_span.attr("tempId","field_sub"+i+"_"+new Date().getTime())
    			_headTd.append(_span)
    			
    			$(headTdItemP).append(_headTd);
    			
    			//设置选项
				var _li = $('<div class="checkbox clearfix OptOneDiv"></div>');
				var _span = $("<span class='pull-left form-input form-input-rel rowIndex'>"+(i+1)+"</span>");
				
				var _content = $("<div class='pull-left form-write'></div>");
				_content.append('<input class="line-input" type="text">');
				_content.find("input").attr("value","项目");
				
				$(_li).append(_span);
				$(_li).append(_content);
				
				var opt = $('<div class="pull-left form-iconic text-right"></div>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-down fa-form-icon downOpt padding-right-5"></a>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-arrow-up fa-form-icon upOpt padding-right-5"></a>');
				$(opt).append('<a href="javascript:void(0)" class="fa fa-times fa-times-circle fa-form-delet red delOpt"></a>');
				$(_li).append(opt);
				
				$(confObj).find(".optListul").append(_li);
    		}
    		
    		var _bodyTdMod = $("<div class='tdItem' style='display:table-cell;height:30px;border:1px solid #DDD;border-top:0px;text-align:center;vertical-align: middle;padding: 5px 5px'></div>");
    		for(var i=0;i<3;i++){
    			var _bodyTd = $(_bodyTdMod).clone();
    			$(bodyTdItemP).append(_bodyTd);
    		}
		}
    	
    	var height = $(confObj).find(".optListul").height();
    	myScroll.slimscroll({
	        scrollTo:height+'px',
	        alwaysVisible: true,
	        disableFadeOut: false
	    });
    	
    }
};
/**
 * 项目选择
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['relateitem'] = function () {
	var me = this,thePlugins = 'relateitem';
	me.commands[thePlugins] = {
		execCommand:function () {//执行添加控件命令
			var _relateItemDiv = $("<span class='myDiv'></span>");
			_relateItemDiv.attr("easyWinCompon","RelateItem");
			
			var tempId= "field_"+new Date().getTime();
			_relateItemDiv.attr("tempId",tempId)
			
			_relateItemDiv.attr("conf_name","项目选择");
			_relateItemDiv.attr("conf_require","false");//是否必填
			_relateItemDiv.attr("conf_isUnique","true");//是否单选
			
			var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>项目</span>");
			_relateItemDiv.append(_input)
			UE.getEditor('editor').execCommand('insertHtml',$(_relateItemDiv).prop("outerHTML"))
			
			
			var range = this.selection.getRange();
			var commonAnchorEl = range.getCommonAncestor();
			var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
			confConpone(anchorEl,thePlugins);//编辑控件
		}
	};
	//提示框
	var popup = new baidu.editor.ui.Popup({
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			  confConpone(popup.anchorEl,thePlugins);//编辑控件
			  this.hide();//隐藏操作
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				var pre = $(this.anchorEl).prev();
				if(pre && $(pre).prop("tagName")){
					$(pre).hasClass("tempClz")?$(pre).remove():"";	
				}
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener( 'mouseover', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			el = $(el).hasClass("fa")?el.parentNode:el;
			try{}catch(e){}
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				var html = popup.formatHtml(
				'<nobr>项目选择: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				if ( html ) {
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor(popup.anchorEl ,100);
				} else {
					popup.hide();
				}
			}
		}catch(e){}
		
	});
	
	var confConpone = function(_EmployeeDiv,thePlugins){
		var confHtml= '<div class="form-title-mm">';
			confHtml+='		<div class="gray-title">';
			confHtml+='			<p class="form-gray text-name">标题</p>';
			confHtml+='		</div>';
			confHtml+='		<div class="form-text-mm">';
			confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			confHtml+='<div class="form-title-mm">';
			confHtml+='		<div class="checkbox">';
			confHtml+='			<label>';
			confHtml+='				<input class="inverted confRequire" type="checkbox">';
			confHtml+='				<span class="text">这个是必填项</span>';
			confHtml+='			</label>';
			confHtml+='		</div>';
			confHtml+='</div>';
			
			
			
			
		var confObj = $(confHtml);
		
		$(confObj).find(".confTitle").attr("value",$(_EmployeeDiv).attr("conf_name"));
		if($(_EmployeeDiv).attr("conf_require") && $(_EmployeeDiv).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(_EmployeeDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(_EmployeeDiv).before("<span style='color:red' class='tempClz'>*</span>");	
			}
			
		}
		
		$("#formConfDiv").html(confObj);
		
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_EmployeeDiv).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_EmployeeDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_EmployeeDiv).before("<span style='color:red' class='tempClz'>*</span>")
				$(_EmployeeDiv).attr("conf_require","true")
			}else{
				$(_EmployeeDiv).attr("conf_require","false")
			}
		})
		
		baidu.editor.plugins[thePlugins].editdom = _EmployeeDiv;
	}
};
/**
 * 客户选择
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['relatecrm'] = function () {
	var me = this,thePlugins = 'relatecrm';
	me.commands[thePlugins] = {
			execCommand:function () {//执行添加控件命令
				var _relateCrmDiv = $("<span class='myDiv'></span>");
				_relateCrmDiv.attr("easyWinCompon","RelateCrm");
				
				var tempId= "field_"+new Date().getTime();
				_relateCrmDiv.attr("tempId",tempId)
				
				_relateCrmDiv.attr("conf_name","客户选择");
				_relateCrmDiv.attr("conf_require","false");//是否必填
				_relateCrmDiv.attr("conf_isUnique","true");//是否单选
				
				var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>客户</span>");
				_relateCrmDiv.append(_input)
				UE.getEditor('editor').execCommand('insertHtml',$(_relateCrmDiv).prop("outerHTML"))
				
				
				var range = this.selection.getRange();
				var commonAnchorEl = range.getCommonAncestor();
				var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
				confConpone(anchorEl,thePlugins);//编辑控件
			}
	};
	//提示框
	var popup = new baidu.editor.ui.Popup({
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			confConpone(popup.anchorEl,thePlugins);//编辑控件
			this.hide();//隐藏操作
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				var pre = $(this.anchorEl).prev();
				if(pre && $(pre).prop("tagName")){
					$(pre).hasClass("tempClz")?$(pre).remove():"";	
				}
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener( 'mouseover', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			el = $(el).hasClass("fa")?el.parentNode:el;
			try{}catch(e){}
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				var html = popup.formatHtml(
				'<nobr>客户选择: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				if ( html ) {
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor(popup.anchorEl ,100);
				} else {
					popup.hide();
				}
			}
		}catch(e){}
		
	});
	
	var confConpone = function(_EmployeeDiv,thePlugins){
		var confHtml= '<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">标题</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="form-text-mm">';
		confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confRequire" type="checkbox">';
		confHtml+='				<span class="text">这个是必填项</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confisUnique" type="checkbox">';
		confHtml+='				<span class="text">这个是单选</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		
		
		
		var confObj = $(confHtml);
		
		$(confObj).find(".confTitle").attr("value",$(_EmployeeDiv).attr("conf_name"));
		if($(_EmployeeDiv).attr("conf_require") && $(_EmployeeDiv).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(_EmployeeDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(_EmployeeDiv).before("<span style='color:red' class='tempClz'>*</span>");	
			}
			
		}
		
		$("#formConfDiv").html(confObj);
		
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(_EmployeeDiv).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		//配置单选
		if($(_EmployeeDiv).attr("conf_isUnique") && $(_EmployeeDiv).attr("conf_isUnique")=='true'){
			$(confObj).find(".confisUnique").attr("checked",true);
			
			//配置默认
			if($(_EmployeeDiv).attr("conf_isDefault") && $(_EmployeeDiv).attr("conf_isDefault")=='true'){
				$(confObj).find(".confDefault").attr("checked",true);
			}
		}
		
		$(confObj).on("click",".confRequire",function(){
			var pre = $(_EmployeeDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(_EmployeeDiv).before("<span style='color:red' class='tempClz'>*</span>")
				$(_EmployeeDiv).attr("conf_require","true")
			}else{
				$(_EmployeeDiv).attr("conf_require","false")
			}
		})
		
		//人员单选设置
		$(confObj).on("click",".confisUnique",function(){
			if($(this).attr("checked")){
				
				$(_EmployeeDiv).attr("conf_isUnique","true");//是否默认为当前时间
				
				$(confObj).find(".confDefaultDiv").parent().show();
				$(confObj).find(".confDefault").attr("checked",false);
			}else{
				$(_EmployeeDiv).attr("conf_isUnique","false");//多选
				$(confObj).find(".confDefaultDiv").parent().hide();
				$(confObj).find(".confDefault").attr("checked",false);
			}
		})
		
		baidu.editor.plugins[thePlugins].editdom = _EmployeeDiv;
	}
};
/**
 * 关联模块
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['relatemod'] = function () {
	var me = this,thePlugins = 'relatemod';
	me.commands[thePlugins] = {
			execCommand:function () {//执行添加控件命令
				var _relateModDiv = $("<span class='myDiv'></span>");
				_relateModDiv.attr("easyWinCompon","RelateMod");
				
				var tempId= "field_"+new Date().getTime();
				_relateModDiv.attr("tempId",tempId)
				
				_relateModDiv.attr("conf_name","关联模块");
				_relateModDiv.attr("conf_require","false");//是否必填
				_relateModDiv.attr("conf_isUnique","true");//是否单选
				
				_relateModDiv.attr("conf_relateModType","0");//关联模块选择
				
				var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>模块</span>");
				_relateModDiv.append(_input)
				UE.getEditor('editor').execCommand('insertHtml',$(_relateModDiv).prop("outerHTML"))
				
				
				var range = this.selection.getRange();
				var commonAnchorEl = range.getCommonAncestor();
				var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
				confConpone(anchorEl,thePlugins);//编辑控件
			}
	};
	//提示框
	var popup = new baidu.editor.ui.Popup({
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			confConpone(popup.anchorEl,thePlugins);//编辑控件
			this.hide();//隐藏操作
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				var pre = $(this.anchorEl).prev();
				if(pre && $(pre).prop("tagName")){
					$(pre).hasClass("tempClz")?$(pre).remove():"";	
				}
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	me.addListener( 'mouseover', function( t, evt ) {//显示控件
		evt = evt || window.event;
		var el = evt.target || evt.srcElement;
		try{
			el = $(el).hasClass("fa")?el.parentNode:el;
			try{}catch(e){}
			var easyWinCompon = el.getAttribute('easyWinCompon');
			easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
			if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
				var html = popup.formatHtml(
				'<nobr>关联模块: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
				if ( html ) {
					popup.getDom( 'content' ).innerHTML = html;
					popup.anchorEl = el;
					popup.showAnchor(popup.anchorEl ,100);
				} else {
					popup.hide();
				}
			}
		}catch(e){}
		
	});
	
	var confConpone = function(RelateModDiv,thePlugins){
		var confHtml='		<div>';
		confHtml+= '<div class="form-title-mm">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">标题</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="form-text-mm">';
		confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confRequire" type="checkbox">';
		confHtml+='				<span class="text">这个是必填项</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm">';
		confHtml+='		<div class="checkbox">';
		confHtml+='			<label>';
		confHtml+='				<input class="inverted confisUnique" type="checkbox">';
		confHtml+='				<span class="text">这个是单选</span>';
		confHtml+='			</label>';
		confHtml+='		</div>';
		confHtml+='</div>';
		
		confHtml+='<div class="form-title-mm confRelateModDiv">';
		confHtml+='		<div class="gray-title">';
		confHtml+='			<p class="form-gray text-name">关联模块</p>';
		confHtml+='		</div>';
		confHtml+='		<div class="select RelateModType">';
		confHtml+='			<select>';
		confHtml+='				<option value="0">请选择</option>';
		confHtml+='				<option value="003">任务模块</option>';
		confHtml+='				<option value="005">项目模块</option>';
		confHtml+='				<option value="012">客户模块</option>';
		confHtml+='				<option value="04201">事件管理过程</option>';
		confHtml+='				<option value="04202">问题管理过程</option>';
		confHtml+='				<option value="04203">变更管理过程</option>';
		confHtml+='				<option value="04204">发布管理过程</option>';
		confHtml+='			</select>';
		confHtml+='		</div>';
		
		confHtml+='</div>';
		confHtml+='</div>';
		
		var confObj = $(confHtml);
		
		$(confObj).find(".confTitle").attr("value",$(RelateModDiv).attr("conf_name"));
		if($(RelateModDiv).attr("conf_require") && $(RelateModDiv).attr("conf_require")=='true'){
			$(confObj).find(".confRequire").attr("checked",true);
			var pre = $(RelateModDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?"":$(RelateModDiv).before("<span style='color:red' class='tempClz'>*</span>");	
			}
			
		}
		var preRelateModType = $(RelateModDiv).attr("conf_relateModType");
		if(preRelateModType && preRelateModType!=='0'){
			$(confObj).find(".confRelateModDiv select").find("option[value='"+preRelateModType+"']").attr("selected",true);
		}
		
		$("#formConfDiv").html(confObj);
		
		$(confObj).on("blur",".confTitle",function(){
			var conName = $(this).val();
			if(conName){
				$(RelateModDiv).attr("conf_name",conName)
			}else{
				layer.tips("请填写",$(this),{tips:1});
			}
		})
		
		//配置单选
		if($(RelateModDiv).attr("conf_isUnique") && $(RelateModDiv).attr("conf_isUnique")=='true'){
			$(confObj).find(".confisUnique").attr("checked",true);
			
			//配置默认
			if($(RelateModDiv).attr("conf_isDefault") && $(RelateModDiv).attr("conf_isDefault")=='true'){
				$(confObj).find(".confDefault").attr("checked",true);
			}
		}
		
		$(confObj).on("click",".confRequire",function(){
			var pre = $(RelateModDiv).prev();
			if(pre && $(pre).prop("tagName")){
				$(pre).hasClass("tempClz")?$(pre).remove():"";	
			}
			
			if($(this).attr("checked")){
				$(RelateModDiv).before("<span style='color:red' class='tempClz'>*</span>")
				$(RelateModDiv).attr("conf_require","true")
			}else{
				$(RelateModDiv).attr("conf_require","false")
			}
		})
		
		//人员单选设置
		$(confObj).on("click",".confisUnique",function(){
			if($(this).attr("checked")){
				
				$(RelateModDiv).attr("conf_isUnique","true");//是否默认为当前时间
				
				$(confObj).find(".confDefaultDiv").parent().show();
				$(confObj).find(".confDefault").attr("checked",false);
			}else{
				$(RelateModDiv).attr("conf_isUnique","false");//多选
				$(confObj).find(".confDefaultDiv").parent().hide();
				$(confObj).find(".confDefault").attr("checked",false);
			}
		})
		//关联模块设置
		$(confObj).on("change",".confRelateModDiv select",function(){
			var relateModType = $(this).val();
			$(RelateModDiv).attr("conf_relateModType",relateModType);
		})
		
		baidu.editor.plugins[thePlugins].editdom = RelateModDiv;
	}
};




UE.plugins['error'] = function () {
    var me = this,thePlugins = 'error';
    me.commands[thePlugins] = {
        execCommand:function () {
            var dialog = new UE.ui.Dialog({
                iframeUrl:this.options.UEDITOR_HOME_URL + UE.easyWinFormDesignUrl+'/error.html',
                name:thePlugins,
                editor:this,
                title: '异常提示',
                cssRules:"width:400px;height:130px;",
                buttons:[
                {
                    className:'edui-okbutton',
                    label:'确定',
                    onclick:function () {
                        dialog.close(true);
                    }
                }]
            });
            dialog.render();
            dialog.open();
        }
    };
};
//UE.plugins['leipi'] = function () {
//    var me = this,thePlugins = 'leipi';
//    me.commands[thePlugins] = {
//        execCommand:function () {
//            var dialog = new UE.ui.Dialog({
//                iframeUrl:this.options.UEDITOR_HOME_URL + UE.easyWinFormDesignUrl+'/leipi.html',
//                name:thePlugins,
//                editor:this,
//                title: '表单设计器 - 清单',
//                cssRules:"width:620px;height:220px;",
//                buttons:[
//                {
//                    className:'edui-okbutton',
//                    label:'确定',
//                    onclick:function () {
//                        dialog.close(true);
//                    }
//                }]
//            });
//            dialog.render();
//            dialog.open();
//        }
//    };
//};
//UE.plugins['leipi_template'] = function () {
//    var me = this,thePlugins = 'leipi_template';
//    me.commands[thePlugins] = {
//        execCommand:function () {
//            var dialog = new UE.ui.Dialog({
//                iframeUrl:this.options.UEDITOR_HOME_URL + UE.easyWinFormDesignUrl+'/template.html',
//                name:thePlugins,
//                editor:this,
//                title: '表单模板',
//                cssRules:"width:640px;height:380px;",
//                buttons:[
//                {
//                    className:'edui-okbutton',
//                    label:'确定',
//                    onclick:function () {
//                        dialog.close(true);
//                    }
//                }]
//            });
//            dialog.render();
//            dialog.open();
//        }
//    };
//};

//UE.registerUI('button_leipi',function(editor,uiName){
//    if(!this.options.toolleipi)
//    {
//        return false;
//    }
//    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
//    editor.registerCommand(uiName,{
//        execCommand:function(){
//            editor.execCommand('leipi');
//        }
//    });
//    //创建一个button
//    var btn = new UE.ui.Button({
//        //按钮的名字
//        name:uiName,
//        //提示
//        title:"表单设计器",
//        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
//        cssRules :'background-position: -401px -40px;',
//        //点击时执行的命令
//        onclick:function () {
//            //这里可以不用执行命令,做你自己的操作也可
//           editor.execCommand(uiName);
//        }
//    });
///*
//    //当点到编辑内容上时，按钮要做的状态反射
//    editor.addListener('selectionchange', function () {
//        var state = editor.queryCommandState(uiName);
//        if (state == -1) {
//            btn.setDisabled(true);
//            btn.setChecked(false);
//        } else {
//            btn.setDisabled(false);
//            btn.setChecked(state);
//        }
//    });
//*/
//    //因为你是添加button,所以需要返回这个button
//    return btn;
//});
//UE.registerUI('button_template',function(editor,uiName){
//    if(!this.options.toolleipi)
//    {
//        return false;
//    }
//    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
//    editor.registerCommand(uiName,{
//        execCommand:function(){
//            try {
//                easyWinFormDesign.exec('leipi_template');
//                //easyWinFormDesign.fnCheckForm('save');
//            } catch ( e ) {
//                alert('打开模板异常');
//            }
//            
//        }
//    });
//    //创建一个button
//    var btn = new UE.ui.Button({
//        //按钮的名字
//        name:uiName,
//        //提示
//        title:"表单模板",
//        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
//        cssRules :'background-position: -339px -40px;',
//        //点击时执行的命令
//        onclick:function () {
//            //这里可以不用执行命令,做你自己的操作也可
//           editor.execCommand(uiName);
//        }
//    });
//
//    //因为你是添加button,所以需要返回这个button
//    return btn;
//});
UE.registerUI('button_preview',function(editor,uiName){
    if(!this.options.toolleipi)
    {
        return false;
    }
    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
    editor.registerCommand(uiName,{
        execCommand:function(){
            try {
                easyWinFormDesign.fnReview();
            } catch ( e ) {
                alert('easyWinFormDesign.fnReview 预览异常');
            }
        }
    });
    //创建一个button
    var btn = new UE.ui.Button({
        //按钮的名字
        name:uiName,
        //提示
        title:"预览",
        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
        cssRules :'background-position: -420px -19px;',
        //点击时执行的命令
        onclick:function () {
            //这里可以不用执行命令,做你自己的操作也可
           editor.execCommand(uiName);
        }
    });

    //因为你是添加button,所以需要返回这个button
    return btn;
});

UE.registerUI('button_save',function(editor,uiName){
    if(!this.options.toolleipi)
    {
        return false;
    }
    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
    editor.registerCommand(uiName,{
        execCommand:function(){
            try {
            	easyWinFormDesign.fnCheckForm('save');
            } catch ( e ) {
                alert('easyWinFormDesign.fnCheckForm("save") 保存异常');
            }
            
        }
    });
    //创建一个button
    var btn = new UE.ui.Button({
        //按钮的名字
        name:uiName,
        //提示
        title:"保存表单",
        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
        cssRules :'background-position: -481px -20px;',
        //点击时执行的命令
        onclick:function () {
            //这里可以不用执行命令,做你自己的操作也可
           editor.execCommand(uiName);
        }
    });

    //因为你是添加button,所以需要返回这个button
    return btn;
});

/**
 * 附件选择
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['relatefile'] = function () {
    var me = this,thePlugins = 'relatefile';
    me.commands[thePlugins] = {
        execCommand:function () {//执行添加控件命令
            var _relateFileDiv = $("<span class='myDiv'></span>");
            _relateFileDiv.attr("easyWinCompon","RelateFile");

            var tempId= "field_"+new Date().getTime();
            _relateFileDiv.attr("tempId",tempId)

            _relateFileDiv.attr("conf_name","附件上传");
            _relateFileDiv.attr("conf_require","false");//是否必填
            _relateFileDiv.attr("conf_isUnique","true");//是否单选

            var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>附件</span>");
            _relateFileDiv.append(_input)
            UE.getEditor('editor').execCommand('insertHtml',$(_relateFileDiv).prop("outerHTML"))


            var range = this.selection.getRange();
            var commonAnchorEl = range.getCommonAncestor();
            var anchorEl = $(commonAnchorEl).parent().find("[tempId='"+tempId+"']").get(0);
            confConpone(anchorEl,thePlugins);//编辑控件
        }
    };
    //提示框
    var popup = new baidu.editor.ui.Popup({
        editor:this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
            confConpone(popup.anchorEl,thePlugins);//编辑控件
            this.hide();//隐藏操作
        },
        _delete:function(){
            if( window.confirm('确认删除该控件吗？') ) {
                var pre = $(this.anchorEl).prev();
                if(pre && $(pre).prop("tagName")){
                    $(pre).hasClass("tempClz")?$(pre).remove():"";
                }
                baidu.editor.dom.domUtils.remove(this.anchorEl,false);
            }
            this.hide();
        }
    } );
    popup.render();
    me.addListener( 'mouseover', function( t, evt ) {//显示控件
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        try{
            el = $(el).hasClass("fa")?el.parentNode:el;
            try{}catch(e){}
            var easyWinCompon = el.getAttribute('easyWinCompon');
            easyWinCompon = easyWinCompon?easyWinCompon.toLowerCase():easyWinCompon;
            if ( /span/ig.test( el.tagName ) && easyWinCompon==thePlugins) {
                var html = popup.formatHtml(
                    '<nobr>附件上传: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' );
                if ( html ) {
                    popup.getDom( 'content' ).innerHTML = html;
                    popup.anchorEl = el;
                    popup.showAnchor(popup.anchorEl ,100);
                } else {
                    popup.hide();
                }
            }
        }catch(e){}

    });

    var confConpone = function(_UpfileDiv,thePlugins){
        var confHtml= '<div class="form-title-mm">';
        confHtml+='		<div class="gray-title">';
        confHtml+='			<p class="form-gray text-name">标题</p>';
        confHtml+='		</div>';
        confHtml+='		<div class="form-text-mm">';
        confHtml+='			<input class="line-input confTitle" type="text" name=""  value="" />';
        confHtml+='		</div>';
        confHtml+='</div>';

        confHtml+='<div class="form-title-mm">';
        confHtml+='		<div class="checkbox">';
        confHtml+='			<label>';
        confHtml+='				<input class="inverted confRequire" type="checkbox">';
        confHtml+='				<span class="text">这个是必填项</span>';
        confHtml+='			</label>';
        confHtml+='		</div>';
        confHtml+='</div>';




        var confObj = $(confHtml);

        $(confObj).find(".confTitle").attr("value",$(_UpfileDiv).attr("conf_name"));
        if($(_UpfileDiv).attr("conf_require") && $(_UpfileDiv).attr("conf_require")=='true'){
            $(confObj).find(".confRequire").attr("checked",true);
            var pre = $(_UpfileDiv).prev();
            if(pre && $(pre).prop("tagName")){
                $(pre).hasClass("tempClz")?"":$(_UpfileDiv).before("<span style='color:red' class='tempClz'>*</span>");
            }

        }

        $("#formConfDiv").html(confObj);

        $(confObj).on("blur",".confTitle",function(){
            var conName = $(this).val();
            if(conName){
                $(_UpfileDiv).attr("conf_name",conName)
            }else{
                layer.tips("请填写",$(this),{tips:1});
            }
        })

        $(confObj).on("click",".confRequire",function(){
            var pre = $(_UpfileDiv).prev();
            if(pre && $(pre).prop("tagName")){
                $(pre).hasClass("tempClz")?$(pre).remove():"";
            }

            if($(this).attr("checked")){
                $(_UpfileDiv).before("<span style='color:red' class='tempClz'>*</span>")
                $(_UpfileDiv).attr("conf_require","true")
            }else{
                $(_UpfileDiv).attr("conf_require","false")
            }
        })

        baidu.editor.plugins[thePlugins].editdom = _UpfileDiv;
    }
};
