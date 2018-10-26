var preLayouts;
$(function(){
	$("#viewForm").hide();
	$("#backBtn").hide();
	$(".componPreList").slimScroll({
	        height:"148px",
	        alwaysVisible: true,
	        disableFadeOut: false
	    });
	$(".componList").slimScroll({
		height:"300px",
		distance: '0px', //组件与侧边之间的距离
		alwaysVisible: true,
		disableFadeOut: false
	});
	$("#refreshList").on("click",function(){
		$(".componPreList").html('');
		if(easyWinEditor.queryCommandState( 'source' )){
    		easyWinEditor.execCommand('source');//切换到编辑模式才提交，否则有bug
    	}
    	
    	if(easyWinEditor.hasContents()){
    		easyWinEditor.sync();/*同步内容*/
    	}
		easyWinFormDesign.listCompone(preLayouts, null);
	})
	
	easyWinEditor = UE.getEditor('editor',{
		allowDivTransToP: false,//阻止转换div 为p
        toolleipi:true,//是否显示，设计器的 toolbars
        textarea: 'design_content',   
        //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
        toolbars:[[
        'fullscreen', 'source', '|', 
        'undo', 'redo', '|',
        'bold', 'italic', 'underline', 'fontborder', 'strikethrough',  'removeformat', '|',
        'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist','|',
        'fontfamily', 'fontsize', '|', 
        'indent', '|',
        'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 
        'link', 'unlink',  '|', 
        'horizontal',  'spechars',  'wordimage', '|', 
        'inserttable', 'deletetable',  'mergecells',  'splittocells']],
        //focus时自动清空初始化时的内容
        //autoClearinitialContent:true,
        //关闭字数统计
        wordCount:false,
        //关闭elementPath
        elementPathEnabled:false,
        //默认的编辑区域高度
        initialFrameHeight:500
        ,iframeCssUrl:"/static/css/formControl.css" //引入自身 css使编辑器兼容你网站css
        //更多其他参数，请参考ueditor.config.js中的配置项

	});
	easyWinFormDesign = {
		 /*执行控件*/
	    exec : function (method) {
	    	easyWinEditor.execCommand(method);
	    },
	    loadFormLayout:function(){//加载布局
	    	var h = {};
            h.id = EasyWin.formModId;
            h.comId = EasyWin.comId;
            $.ajax({
                type: "POST",
                data: h,
                dataType: "json",
                url: sid ? "/form/findFormCompDev?sid="+sid : "/web/form/findFormCompDev",
                success: function(formMod) {
                	var formLayout = formMod.formLayout;
                	
                	$("#modName").val(formMod.modName)
                	
                	if(formLayout){
                		
                		var formState = formLayout.formState;
                		if(formState && formState == 1){//不是替换成表单2.0的
                			$("#preFormComponent").remove();
                			var layoutHtml= formLayout.formLayoutHtml.layoutHtml;
                			var temp_layoutHtml = $("<div></div>").append(layoutHtml);
                			$(temp_layoutHtml).find(".tempClz").remove()
                			
                			var lastPObj = $(temp_layoutHtml).find("p:last");
                			while($(lastPObj).html()=='<br>'){
                				var lastPrePObj = $(lastPObj).prev();
                				$(lastPObj).remove();
                				lastPObj = lastPrePObj
                			}
                			
                			var layoutDetails = JSON.parse(formLayout.layoutDetail);
                			layoutDetails ? $.each(layoutDetails,function(index,layoutDetail){
                				easyWinFormDesign.analyseComponent(layoutDetail,$(temp_layoutHtml));
                			}):"";
                			EasyWin.layoutId = formLayout.id;
                			easyWinEditor.execCommand('insertHtml',$(temp_layoutHtml).html())
                			
                		}else{
                			
                			$("#rollBtn").remove();
                			$("#preFormComponent").css("display","block");
                			var layoutDetails = JSON.parse(formLayout.layoutDetail);
                			
                			preLayouts = layoutDetails;
                			easyWinFormDesign.listCompone(layoutDetails,null);
                			
                		}
                		
                	}
                	
                },
                error: function() {
                }
            })
	    },
	    listCompone:function(layoutDetails,pLayoutDetail){
	    	if(layoutDetails){
	    		var  formeditor=easyWinEditor.getContent();
	    		var template_Temp;
	    		if(formeditor){
	    			template_Temp = $("<div><div>").append(formeditor);
	    		}
	    		
	    		$.each(layoutDetails,function(index,layoutDetail){
	    			
	    			var componKey = layoutDetail.componentKey;
	    			var title = layoutDetail.title;
	    			var _li = $("<li></li>");
	    			var _icon = $('<p class="fa form-icon"></p>')
	    			var _title = $("<p class='form-text'></p>");
	    			
	    			var flag = 0;
	    			if("Text" == componKey){
	    				_icon.addClass("fa-pencil");
	    				flag = 1;
	    			}else if("TextArea" == componKey){
	    				_icon.addClass("fa-align-center");
	    				flag = 1;
	    			}else if("RadioBox" == componKey){
	    				_icon.addClass("fa-dot-circle-o");
	    				flag = 1;
	    			}else if("CheckBox" == componKey){
	    				_icon.addClass("fa-check-square-o");
	    				flag = 1;
	    			}else if("Select" == componKey){
	    				_icon.addClass("fa-caret-square-o-down");
	    				flag = 1;
	    			}else if("DateComponent" == componKey){
	    				_icon.addClass("fa-calendar");
	    				flag = 1;
	    			}else if("DateInterval" == componKey){
	    				_icon.addClass("fa-calendar-o");
	    				flag = 1;
	    			}else if("NumberComponent" == componKey){
	    				_icon.addClass("fa-sort-numeric-desc");
	    				flag = 1;
	    			}else if("Monitor" == componKey){
	    				_icon.addClass("fa-gears");
	    				flag = 1;
	    			}else if("Employee" == componKey){
	    				_icon.addClass("fa-group");
	    				flag = 1;
	    			}else if("Department" == componKey){
	    				_icon.addClass("fa-sitemap");
	    				flag = 1;
	    			}else if("DataTable" == componKey){
	    				_icon.addClass("fa-clipboard");
	    				title = '子表单';
	    				flag = 1;
	    			}
	    			
	    			if(flag){
	    				
	    				var fieldId = layoutDetail.fieldId;
		    			if(!fieldId || !$(template_Temp).find("[fieldId='"+fieldId+"']").get(0)){
		    				_title.html(title);
		    				$(_li).attr("componType",componKey);
		    				$(_li).append(_icon)
		    				$(_li).append(_title);
		    				$(_li).data("layoutDetail",layoutDetail)
		    				$(".componPreList").append($(_li))
		    			}
		    			
	    			}
	    			
	    			
	    			
	    			
	    			var subLayoutDetails = layoutDetail.layoutDetail;
	    			if(subLayoutDetails && subLayoutDetails.length>0){
	    				easyWinFormDesign.listCompone(subLayoutDetails,layoutDetail);
	    			}
	    			
	    		})
	    	}
	    },
	    analyseComponent:function(layoutDetail,layoutHtml){
	    	var componKey = layoutDetail.componentKey;
	    	var fieldId = layoutDetail.fieldId;
	    	var tempId = layoutDetail.tempId;
	    	
	    	var required = layoutDetail.required;
	    	var title = layoutDetail.title;
	    	
	    	var conf_tableColumn = layoutDetail.tableColumn;
	    	
	    	var _spanRequire=$("<span style='color:red' class='tempClz'>*</span>");
	    	var easywinTag = $(layoutHtml).find("easywin[tempid='"+tempId+"']");
	    	
	    	if('Text'==componKey){
	    		var _input  = $("<input type='text' style='width:50px' placeholder='文本框'/>");
				_input.attr("easyWinCompon",componKey);
				
				_input.attr("fieldId",fieldId)
				_input.attr("tempId",tempId)
				_input.attr("conf_name",title);
				_input.attr("conf_require",required);//必填
				
				var conf_size = layoutDetail.size;
				conf_size = conf_size?conf_size:"medium";
				_input.attr("conf_size",layoutDetail.size);//大小
				
				if(conf_size == 'small'){
					_input.css("width","30%")
				}else if(conf_size == 'medium'){
					_input.css("width","60%")
				}else if(conf_size == 'large'){
					_input.css("width","98%")
				}
				
				$(_input).attr("conf_tableColumn",conf_tableColumn);
				
				_input.attr("readonly",true);
				required!='true'?$(easywinTag).before(_input):($(easywinTag).before(_spanRequire),$(easywinTag).before(_input));
	    	
	    	}else if('NumberComponent'==componKey){
	    		
	    		var _input  = $("<input type='text' style='width:50px' placeholder='数字框'/>");
	    		_input.attr("easyWinCompon",componKey);
	    		
	    		_input.attr("fieldId",fieldId)
	    		_input.attr("tempId",tempId)
	    		_input.attr("conf_name",title);
	    		_input.attr("conf_require",required);//必填
	    		
	    		var conf_size = layoutDetail.size;
				conf_size = conf_size?conf_size:"medium";
				_input.attr("conf_size",layoutDetail.size);//大小
				
				if(conf_size == 'small'){
					_input.css("width","30%")
				}else if(conf_size == 'medium'){
					_input.css("width","60%")
				}else if(conf_size == 'large'){
					_input.css("width","98%")
				}
				
				$(_input).attr("conf_tableColumn",conf_tableColumn);
	    		
	    		_input.attr("readonly",true);
	    		required!='true'?$(easywinTag).before(_input):($(easywinTag).before(_spanRequire),$(easywinTag).before(_input));
	    		
	    		}else if('SerialNum'==componKey){//序列编号
	    		
	    		var _input  = $("<input type='text' style='width:50px' placeholder='序列编号'/>");
	    		_input.attr("easyWinCompon",componKey);
	    		
	    		_input.attr("fieldId",fieldId)
	    		_input.attr("tempId",tempId)
	    		_input.attr("conf_name",title);
	    		_input.attr("conf_require",required);//必填
	    		
	    		var conf_size = layoutDetail.size;
	    		conf_size = conf_size?conf_size:"medium";
	    		_input.attr("conf_size",layoutDetail.size);//大小
	    		
	    		if(conf_size == 'small'){
	    			_input.css("width","30%");
	    		}else if(conf_size == 'medium'){
	    			_input.css("width","60%");
	    		}else if(conf_size == 'large'){
	    			_input.css("width","98%");
	    		}
	    		
	    		var SerialNumId = layoutDetail.serialNumId;
	    		var SerialNumRemark = layoutDetail.serialNumRemark;
	    		
	    		_input.attr("serialNumId",SerialNumId);
	    		_input.attr("serialNumRemark",SerialNumRemark);
	    		
	    		_input.attr("readonly",true);
	    		required!='true'?$(easywinTag).before(_input):($(easywinTag).before(_spanRequire),$(easywinTag).before(_input));
	    	}else if('TextArea'==componKey){
	    		var _textarea = $("<textarea style='height:95%;min-height:50px'></textarea>");
				
	    		_textarea.attr("easyWinCompon",componKey);
	    		_textarea.attr("fieldId",fieldId)
				_textarea.attr("tempId",tempId)
				
				_textarea.attr("conf_name",title);
				_textarea.attr("conf_require",required);
				
				var conf_size = layoutDetail.size;
				conf_size = conf_size?conf_size:"medium";
				_textarea.attr("conf_size",layoutDetail.size);//大小
				
				$(easywinTag).parents("td").css("padding","5px 5px")
				if(conf_size == 'small'){
					_textarea.css("width","30%")
				}else if(conf_size == 'medium'){
					_textarea.css("width","60%")
				}else if(conf_size == 'large'){
					_textarea.css("width","98%")
				}
				
				$(_textarea).attr("conf_tableColumn",conf_tableColumn);
				
				_textarea.attr("readonly",true);
				required!='true'?$(easywinTag).before(_textarea):($(easywinTag).before(_spanRequire),$(easywinTag).before(_textarea));
	    	}else if('RadioBox' == componKey){
	    		var _radiobox = $("<span class='myDiv'></span>");
	    		_radiobox.attr("easyWinCompon",componKey);
	    		_radiobox.attr("fieldId",fieldId)
	    		_radiobox.attr("tempId",fieldId)
				
	        	_radiobox.attr("conf_name",title);
	        	_radiobox.attr("conf_require",required);
	        	var titleLayout = layoutDetail.titleLayout;
	        	_radiobox.attr("conf_layout",titleLayout);
	        	
	        	var sysDataDic = layoutDetail.sysDataDic;
	        	_radiobox.attr("conf_sysDataDic",sysDataDic);
	        	
	        	var options = layoutDetail.options;
	        	$.each(options,function(optIndex,optObj){
	        		
	        		var optFieldId = optObj.fieldId
	        		var optName = optObj.name;
	        			
	        		var option = $("<lable><input type='radio'><span>"+optName+"</span></lable>")
	            	$(option).find("input").attr("optName",optName); 
	        		$(option).find("input").attr("fieldId",optFieldId); 
	        		_radiobox.append(option);
	        	});
				
	        	_radiobox.find("input").attr("disabled",true);
	        	
	        	_radiobox.attr("conf_tableColumn",conf_tableColumn);
	        	
	        	layoutDetail.titleLayout!="field-hoz"?$(_radiobox).find("lable").append("<br/>"):""; 
	        	required!='true'?$(easywinTag).before(_radiobox):($(easywinTag).before(_spanRequire),$(easywinTag).before(_radiobox));
	    	}else if('CheckBox'==componKey){
	    		var _checkbox = $("<span class='myDiv'></span>");
	    		_checkbox.attr("easyWinCompon",componKey);
	    		_checkbox.attr("fieldId",fieldId)
	    		_checkbox.attr("tempId",fieldId)
				
	        	_checkbox.attr("conf_name",title);
	        	_checkbox.attr("conf_require",required);
	        	var titleLayout = layoutDetail.titleLayout;
	        	_checkbox.attr("conf_layout",titleLayout);
	        	
	        	var sysDataDic = layoutDetail.sysDataDic;
	        	_checkbox.attr("conf_sysDataDic",sysDataDic);
	        	
	        	var options = layoutDetail.options;
	        	$.each(options,function(optIndex,optObj){
	        		
	        		var optFieldId = optObj.fieldId
	        		var optName = optObj.name;
	        			
	        		var option = $("<lable><input type='checkbox'><span>"+optName+"</span></lable>")
	            	$(option).find("input").attr("optName",optName); 
	        		$(option).find("input").attr("fieldId",optFieldId); 
	        		_checkbox.append(option);
	        	});
				
	        	_checkbox.find("input").attr("disabled",true);
	        	
	        	_checkbox.attr("conf_tableColumn",conf_tableColumn);
	        	
	        	layoutDetail.titleLayout!="field-hoz"?$(_checkbox).find("lable").append("<br/>"):""; 
	        	required!='true'?$(easywinTag).before(_checkbox):($(easywinTag).before(_spanRequire),$(easywinTag).before(_checkbox));
	    	
	    	}else if('Select' == componKey){
	    		var _selectDiv = $("<span class='myDiv'></span>");
	    		_selectDiv.attr("fieldId",fieldId)
	    		_selectDiv.attr("easyWinCompon",componKey);
	    		_selectDiv.attr("tempId",fieldId)
				
	        	_selectDiv.attr("conf_name",title);
	    		_selectDiv.attr("conf_require",required);
	    		
	    		
	    		var sysDataDic = layoutDetail.sysDataDic;
	    		_selectDiv.attr("conf_sysDataDic",sysDataDic);
	        	
	    		var _select = $("<select></select>");
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
	        	
	        	_selectDiv.attr("conf_tableColumn",conf_tableColumn);
	        	
	        	_selectDiv.append(_select);
	        	
	        	required!='true'?$(easywinTag).before(_selectDiv):($(easywinTag).before(_spanRequire),$(easywinTag).before(_selectDiv));
	    	}else if('DateComponent' == componKey ){
	    		var _input  = $("<input type='text' style='width:90px' placeholder='yyyy-MM-dd'/>");
				_input.attr("easyWinCompon",componKey);
				
				_input.attr("fieldId",fieldId)
				_input.attr("tempId",tempId)
				_input.attr("conf_name",title);
				_input.attr("conf_require",required);//必填
				
				var isDefault = layoutDetail.isDefault;
				_input.attr("conf_isDefault",isDefault);//是否默认为当前时间
				
				var isEdit = layoutDetail.isEdit;
				_input.attr("conf_isEdit",isEdit);//是否默认为当前时间
					
				
				var confFormat = layoutDetail.format;
				_input.attr("conf_format",confFormat);//格式化方式
				$(_input).attr("placeholder",confFormat);
				
				if(confFormat=='yyyy-MM'){
					$(_input).css("width","90px");
				}else if(confFormat=='yyyy年MM月'){
					$(_input).css("width","120px");
				}else if(confFormat=='yyyy-MM-dd'){
					$(_input).css("width","90px");
				}else if(confFormat=='yyyy年MM月dd日'){
					$(_input).css("width","120px");
				}else {
					$(_input).css("width","120px");
				}
				
				_input.attr("conf_tableColumn",conf_tableColumn);
				
				_input.attr("readonly",true);
				required!='true'?$(easywinTag).before(_input):($(easywinTag).before(_spanRequire),$(easywinTag).before(_input));
	    	
	    	}else if('DateInterval' == componKey){
	    		var _DateInterDiv = $("<span class='myDiv'></span>");
				_DateInterDiv.attr("easyWinCompon",componKey);
				_DateInterDiv.attr("tempId",tempId)
				_DateInterDiv.attr("fieldId",fieldId)
				_DateInterDiv.attr("conf_name",title);
				_DateInterDiv.attr("conf_require",required);//是否必填
				
				var confFormat = layoutDetail.start.format;
				_DateInterDiv.attr("conf_format",confFormat);//格式化方式
				
				var start = layoutDetail.start;
				var _startDateInput  = $("<span class='dateTime'><input type='text' style='width:90px' placeholder='yyyy-MM-dd'/></span>");
				
				$(_startDateInput).find("input").attr("readonly",true);
				$(_startDateInput).find("input").attr("conf_name",start.title);
				$(_startDateInput).find("input").attr("easyWinCompon","DateComponent");
				$(_startDateInput).find("input").attr("tempId",start.tempId);
				$(_startDateInput).find("input").attr("fieldId",start.fieldId);
				
				var end = layoutDetail.end;
				var _endDateInput  = $("<span class='dateTime'><input type='text' style='width:90px' placeholder='yyyy-MM-dd'/></span>");
				$(_endDateInput).find("input").attr("readonly",true);
				$(_endDateInput).find("input").attr("conf_name",end.title);
				$(_endDateInput).find("input").attr("easyWinCompon","DateComponent");
				$(_endDateInput).find("input").attr("tempId",end.tempId);
				$(_endDateInput).find("input").attr("fieldId",end.fieldId);
				
				_DateInterDiv.append(_startDateInput);
				_DateInterDiv.append("<span style='margin-left:5px;margin-right:5px;'>至</span>");
				_DateInterDiv.append(_endDateInput);
				
				if(confFormat=='yyyy-MM'){
					$(_DateInterDiv).find("input").css("width","90px");
					$(_DateInterDiv).find("input").attr("placeholder","yyyy-MM");
				}else if(confFormat=='yyyy-MM-dd HH:mm'){
					$(_DateInterDiv).find("input").css("width","120px");
					$(_DateInterDiv).find("input").attr("placeholder","yyyy-MM-dd HH:mm");
				}else{
					$(_DateInterDiv).find("input").css("width","90px");
					$(_DateInterDiv).find("input").attr("placeholder","yyyy-MM-dd");
				}
				
				required!='true'?$(easywinTag).before(_DateInterDiv):($(easywinTag).before(_spanRequire),$(easywinTag).before(_DateInterDiv));
				
	    	}else if('Employee' == componKey){
	    		var _EmployeeDiv = $("<span class='myDiv'></span>");
				_EmployeeDiv.attr("easyWinCompon",componKey);
				
				_EmployeeDiv.attr("tempId",tempId)
				_EmployeeDiv.attr("fieldId",fieldId)
				
				_EmployeeDiv.attr("conf_name",title);
				_EmployeeDiv.attr("conf_require",required);//是否必填
				
				var isUnique = layoutDetail.isUnique;
				_EmployeeDiv.attr("conf_isUnique",isUnique);//是否默认为当前人员
				
				var isDefault = layoutDetail.isDefault;
				_EmployeeDiv.attr("conf_isDefault",isDefault);//是否默认为当前时间
				
				var isEdit = layoutDetail.isEdit;
				_EmployeeDiv.attr("conf_isEdit",isEdit);//是否默认为当前时间
				
				var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>人员</span>");
				
				_EmployeeDiv.append(_input)
				
				required!='true'?$(easywinTag).before(_EmployeeDiv):($(easywinTag).before(_spanRequire),$(easywinTag).before(_EmployeeDiv));
	    	}else if('Department' == componKey){
	    		
	    		var _DepartmentDiv = $("<span class='myDiv'></span>");
				_DepartmentDiv.attr("easyWinCompon",componKey);
				_DepartmentDiv.attr("tempId",tempId)
				_DepartmentDiv.attr("fieldId",fieldId)
				
				_DepartmentDiv.attr("conf_name",title);
				_DepartmentDiv.attr("conf_require",required);//是否必填
				
				var isUnique = layoutDetail.isUnique;
				_DepartmentDiv.attr("conf_isUnique",isUnique);//是否默认为当前人员
				
				var isDefault = layoutDetail.isDefault;
				_DepartmentDiv.attr("conf_isDefault",isDefault);//是否默认为当前时间
				
				var isEdit = layoutDetail.isEdit;
				_DepartmentDiv.attr("conf_isEdit",isEdit);//是否默认为当前时间
				
				
				var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>部门</span>");
				_DepartmentDiv.append(_input)
				
				required!='true'?$(easywinTag).before(_DepartmentDiv):($(easywinTag).before(_spanRequire),$(easywinTag).before(_DepartmentDiv));
	    	}else if('RelateFile' == componKey){

                var _relateUpfileDiv = $("<span class='myDiv'></span>");
                _relateUpfileDiv.attr("easyWinCompon",componKey);

                _relateUpfileDiv.attr("tempId",tempId)
                _relateUpfileDiv.attr("fieldId",fieldId)

                _relateUpfileDiv.attr("conf_name",title);
                _relateUpfileDiv.attr("conf_require",required);//是否必填

                var isUnique = layoutDetail.isUnique;
                _relateUpfileDiv.attr("conf_isUnique",isUnique);//是否单选

                var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>附件</span>");
                _relateUpfileDiv.append(_input)

                required!='true'?$(easywinTag).before(_relateUpfileDiv):($(easywinTag).before(_spanRequire),$(easywinTag).before(_relateUpfileDiv));
            }else if('RelateItem' == componKey){
	    		
	    		var _relateItemDiv = $("<span class='myDiv'></span>");
				_relateItemDiv.attr("easyWinCompon",componKey);
				
				_relateItemDiv.attr("tempId",tempId)
				_relateItemDiv.attr("fieldId",fieldId)
				
				_relateItemDiv.attr("conf_name",title);
				_relateItemDiv.attr("conf_require",required);//是否必填
				
				var isUnique = layoutDetail.isUnique;
				_relateItemDiv.attr("conf_isUnique",isUnique);//是否单选
				
				var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>项目</span>");
				_relateItemDiv.append(_input)
				
				required!='true'?$(easywinTag).before(_relateItemDiv):($(easywinTag).before(_spanRequire),$(easywinTag).before(_relateItemDiv));
	    	}else if('RelateCrm' == componKey){
	    		
	    		var _relateCrmDiv = $("<span class='myDiv'></span>");
	    		_relateCrmDiv.attr("easyWinCompon",componKey);
	    		
	    		_relateCrmDiv.attr("tempId",tempId)
	    		_relateCrmDiv.attr("fieldId",fieldId)
	    		
	    		_relateCrmDiv.attr("conf_name",title);
	    		_relateCrmDiv.attr("conf_require",required);//是否必填
	    		
	    		var isUnique = layoutDetail.isUnique;
	    		_relateCrmDiv.attr("conf_isUnique",isUnique);//是否单选
	    		
	    		var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>客户</span>");
	    		_relateCrmDiv.append(_input)
	    		
	    		required!='true'?$(easywinTag).before(_relateCrmDiv):($(easywinTag).before(_spanRequire),$(easywinTag).before(_relateCrmDiv));
	    	
	    	}else if('RelateMod' == componKey){
	    		
	    		var _relateModDiv = $("<span class='myDiv'></span>");
	    		_relateModDiv.attr("easyWinCompon",componKey);
	    		
	    		_relateModDiv.attr("tempId",tempId)
	    		_relateModDiv.attr("fieldId",fieldId)
	    		
	    		_relateModDiv.attr("conf_name",title);
	    		_relateModDiv.attr("conf_require",required);//是否必填
	    		
	    		var isUnique = layoutDetail.isUnique;
	    		_relateModDiv.attr("conf_isUnique",isUnique);//是否单选
	    		
	    		var relateModType = layoutDetail.relateModType;
	    		_relateModDiv.attr("conf_relateModType",relateModType);//关联模块
	    		
	    		var _input  = $("<span class='btn btn-primary btn-xs userSelect fa fa-plus margin-left-5' style='color:#fff;background-color:blue;padding:2px 3px;font-size:10px'>模块</span>");
	    		_relateModDiv.append(_input)
	    		
	    		required!='true'?$(easywinTag).before(_relateModDiv):($(easywinTag).before(_spanRequire),$(easywinTag).before(_relateModDiv));
	    		
	    	}else if('subItemTitle' == componKey){
	    		var _itemTitleDiv = $("<span></span>");
	    		_itemTitleDiv.attr("easyWinCompon",componKey);
				
	    		_itemTitleDiv.attr("tempId",tempId)
				_itemTitleDiv.attr("fieldId",fieldId)
				
				_itemTitleDiv.attr("conf_name",title);
	    		_itemTitleDiv.html(title);
	    		$(easywinTag).before(_itemTitleDiv);
	    	}else if('DataTable' == componKey){
	    		
	    		var size = layoutDetail.size;
	    		
	    		var _subTableDiv = $("<div class='myDiv'></div>")
	    		_subTableDiv.attr("tempId",tempId)
	    		_subTableDiv.attr("fieldId",fieldId)
	    		$(_subTableDiv).attr("easyWinCompon",componKey);
	    		$(_subTableDiv).attr("conf_name",title);
	    		$(_subTableDiv).attr("subFormId",layoutDetail.subFormId)
	    		
	    		$(_subTableDiv).attr("conf_relateTable",layoutDetail.relateTable);
	    		
	    		
	    		var _tableHeadDiv = $("<div class='sub subTableHead' style='display:table;border:#DDD 1px solid;width:100%'></div>");
	        	var _headTr = $("<div style='display:table-row;'></div>");
	        	var _headTdMod = $("<div class='tdItem' style='display:table-cell;height:30px;border: 1px solid #DDD;text-align:center;vertical-align: middle;'></div>");
	        	
	        	var _tableBodyDiv = $("<div class='sub subTableBody' style='display:table;border:#DDD 1px solid;width:100%;border-top:0px'></div>");
	        	var _bodyTr = $("<div style='display:table-row;border-top:0px'></div>");
	        	var _bodyTdMod = $("<div class='tdItem' style='display:table-cell;height:30px;border:1px solid #DDD;border-top:0px;text-align:center;vertical-align: middle;padding: 5px 5px'></div>");
	        	
	        	var effectIndex = 1;
	        	var sublayoutDetails = layoutDetail.layoutDetail;
	        	$.each(sublayoutDetails,function(index,sublayoutDetail){
	        		var defineTag = $("<easyWin></easyWin>");
	        		defineTag.attr("tempId",sublayoutDetail.tempId);
	        		defineTag.attr("componKey",sublayoutDetail.componentKey);
	        		
	        		if(sublayoutDetail.componentKey == 'subItemTitle'){
	        			effectIndex ++ ;
	        			var _headTd = $(_headTdMod).clone();
	        			_headTd.html(defineTag)
	        			$(_headTr).append(_headTd)
	        		}else{
	        			var _bodyTd = $(_bodyTdMod).clone();
	        			_bodyTd.html(defineTag)
	        			
	        			$(_bodyTr).append(_bodyTd);
	        		}
	        	})
	        	$(_tableHeadDiv).append(_headTr);
	        	$(_tableBodyDiv).append(_bodyTr);
	        	
	        	
	        	$(_subTableDiv).append(_tableHeadDiv);
	        	$(_subTableDiv).append(_tableBodyDiv);
	        	
	        	var width = (100-100%effectIndex)/effectIndex;
	        	$(_subTableDiv).find(".tdItem").css("width",width+"%");
	        	$(_subTableDiv).find(".tdItem").css("word-break","break-all");
	        	
	        	$(easywinTag).before(_subTableDiv)
	        	$(easywinTag).remove();
	        	$.each(sublayoutDetails,function(index,sublayoutDetail){
	        		easyWinFormDesign.analyseComponent(sublayoutDetail,$(layoutHtml));
	        	})
	        	
	    	}else if('Monitor' == componKey){
	    		var _monitorInput  = $("<input type='text' style='width:50px'/>");
				_monitorInput.attr("easyWinCompon","Monitor");
				
				_monitorInput.attr("tempId",tempId);
				_monitorInput.attr("fieldId",fieldId);
				
				_monitorInput.attr("conf_name",title);
				if(layoutDetail.monitorType=='date'){
					_monitorInput.attr("conf_monitorTempDateFields",layoutDetail.monitorTempFields);
				}else if(layoutDetail.monitorType=='number'){
					var monitorTempFields = layoutDetail.monitorTempFields.replace(/\[|]/g,'')
					_monitorInput.attr("conf_monitorTempNumFields",monitorTempFields);
				}
				_monitorInput.attr("conf_require",required);
				
				_monitorInput.attr("conf_monitorType",layoutDetail.monitorType);//默认监控类型日期
				_monitorInput.attr("conf_calTimeType",layoutDetail.calTimeType);//默认计算工作时间
				
				var conf_size = layoutDetail.size;
				conf_size = conf_size?conf_size:"medium";
				_monitorInput.attr("conf_size",layoutDetail.size);//大小
				
				if(conf_size == 'small'){
					_monitorInput.css("width","30%")
				}else if(conf_size == 'medium'){
					_monitorInput.css("width","60%")
				}else if(conf_size == 'large'){
					_monitorInput.css("width","98%")
				}
				
				
				_monitorInput.attr("readonly",true);
				
				required!='true'?$(easywinTag).before(_monitorInput):($(easywinTag).before(_spanRequire),$(easywinTag).before(_monitorInput));
	    	}else if('MoneyComponent' == componKey){
	    		var _monitorInput  = $("<input type='text' style='width:50px' placeholder='大写金额'/>");
	    		_monitorInput.attr("easyWinCompon","MoneyComponent");
	    		
	    		_monitorInput.attr("tempId",tempId)
	    		_monitorInput.attr("fieldId",fieldId)
	    		_monitorInput.css("width","60%")
	    		
	    		_monitorInput.attr("conf_name",title);
	    		
	    		_monitorInput.attr("conf_moneyTempColumn",layoutDetail.moneyTempColumn);//默认监控类型日期
	    		
	    		_monitorInput.attr("readonly",true);
	    		
	    		required!='true'?$(easywinTag).before(_monitorInput):($(easywinTag).before(_spanRequire),$(easywinTag).before(_monitorInput));
	    	}
	    	var subLayout = layoutDetail.layoutDetail;
	    	if(subLayout && subLayout.length>0){
	    		$.each(subLayout,function(index,obj){
	    			easyWinFormDesign.analyseComponent(obj,$(layoutHtml));
	    		})
	    	}
	    	$(easywinTag).remove();
	    },
	    /*
	        Javascript 解析表单
	        template 表单设计器里的Html内容
	        fields 字段总数
	    */
	    parse_formField:function(template,fields){
	    	
	    	var template_parse = template,template_data = new Array(),add_fields=new Object(),checkboxs=0;
	    	
	    	$.each($(template_parse),function(level,levleOne){
	    		var tagName = $(levleOne).prop("tagName");
	    		//移除临时加的数据
	    		$(levleOne).find(".tempClz").remove();
	    		if(/p/ig.test(tagName)){
	    			if($(levleOne).find("[easyWinCompon]").length>0){
	    				$.each($(levleOne).find("[easyWinCompon]"),function(index,compone){
	    					if($(compone).parents("[easyWinCompon='DateInterval']").length==0){
	    						var FormCompon = easyWinFormDesign.constrCompon(compone);
	    						template_data.push(FormCompon)
	    					}
	    				})
	    			}else{
	    				
	    			}
	    		}else if(/table/ig.test(tagName)){
	    			var FormCompons = easyWinFormDesign.parse_table($(levleOne));
	    			template_data.push(FormCompons)
	    		}else if(/div/ig.test(tagName)){
	    			if($(this).attr("easyWinCompon")){
	    				var FormCompon = easyWinFormDesign.constrCompon($(levleOne));
	    				template_data.push(FormCompon)
	    			}else if($(levleOne).find("[easyWinCompon]").length>0){
	    				$.each($(levleOne).find("[easyWinCompon]"),function(index,compone){
	    					var FormCompon = easyWinFormDesign.constrCompon(compone);
	    					template_data.push(FormCompon)
	    				})
	    			}else{
	    				
	    			}
	    		}
	    	})
	    	return template_data;
	    },
	    parse_table:function(table){
	    	
	    	var tableLayoutDetail = new Object();
	    	tableLayoutDetail.componentKey='TableLayout';
	    	
	    	var tbodyLayoutDetails = new Array();
	    	var tbody = $(table).find("tbody");
	    	if(tbody.length>0){
	    		
	    		var tbodyLayoutDetail = new Object();
	    		tbodyLayoutDetail.componentKey='TbodyLayout';
	    		
	    		var trs = $(tbody).find("tr");
	    		var trLayoutDetails = new Array();
	    		$.each(trs,function(rowIndex,rowObj){
	    			
	    			var trLayoutDetail = new Object();
	    			trLayoutDetail.componentKey='TrLayout';
	    			trLayoutDetail.index = rowIndex;
	    			
	    			var tds = $(rowObj).find("td");
	    			var tdLayoutDetails = new Array();
	    			$.each(tds,function(colIndex,colObj){
	    				
	    				var tdLayoutDetail = new Object();
		    			tdLayoutDetail.componentKey='TdLayout';
		    			tdLayoutDetail.index = colIndex;
	    				var rowSpan = $(colObj).attr("rowspan");
	    				var colSpan = $(colObj).attr("colspan");
	    				
	    				rowSpan?tdLayoutDetail.rowSpan = rowSpan:"";
	    				colSpan?tdLayoutDetail.colSpan = colSpan:"";
	    				
	    				var componeArray = new Array();
	    				if($(colObj).find("[easyWinCompon]").length>0){
		    				$.each($(colObj).find("[easyWinCompon]"),function(index,compone){
		    					if($(compone).parents("[easyWinCompon='DateInterval']").length==0 //不是日期区间的子区间
		    							&& $(compone).parents("[easywincompon='DataTable']").length==0){//不是子表单数据
		    						var FormCompon = easyWinFormDesign.constrCompon(compone);
		    						componeArray.push(FormCompon)
		    						
		    					}
		    				})
		    			}else{
		    				var normalLayoutDetail = new Object();
		    				normalLayoutDetail.componentKey='ColumnPanel';
		    				componeArray.push(normalLayoutDetail);
		    			}
	    				tdLayoutDetail.layoutDetail = componeArray;
	    				
	    				tdLayoutDetails.push(tdLayoutDetail);
	    				
	    			})
	    			trLayoutDetail.layoutDetail=tdLayoutDetails;
	    			
	    			trLayoutDetails.push(trLayoutDetail);
	    		})
	    		tbodyLayoutDetail.layoutDetail=trLayoutDetails;
	    		
	    		tbodyLayoutDetails.push(tbodyLayoutDetail);
	    	}
	    	tableLayoutDetail.layoutDetail=tbodyLayoutDetails;
	    	return tableLayoutDetail;
	    },
	    constrCompon:function(compone){
	    	var componKey = $(compone).attr("easyWinCompon");
	    	var LayoutDetail = new Object();
	    	
	    	LayoutDetail.componentKey = componKey;
	    	
	    	LayoutDetail.title = $(compone).attr("conf_name");
	    	LayoutDetail.fieldId = $(compone).attr("fieldId");
	    	LayoutDetail.tempId = $(compone).attr("tempId");
	    	
	    	if(/Text|TextArea|NumberComponent/ig.test(componKey)){
	    		LayoutDetail.size = $(compone).attr("conf_size")?$(compone).attr("conf_size"):"medium";
	    		//默认不是必填
		    	LayoutDetail.required = $(compone).attr("conf_require")?$(compone).attr("conf_require"):"false";
		    	
		    	//关联的字段
		    	LayoutDetail.tableColumn = $(compone).attr("conf_tableColumn");
		    	
	    	}else if(/SerialNum/ig.test(componKey)){
	    		LayoutDetail.size = $(compone).attr("conf_size")?$(compone).attr("conf_size"):"medium";
	    		LayoutDetail.defWidth = $(compone).attr("defWidth")?$(compone).attr("defWidth"):"80";
	    		//默认不是必填
		    	LayoutDetail.required = $(compone).attr("conf_require")?$(compone).attr("conf_require"):"false";
		    	
		    	LayoutDetail.serialNumId = $(compone).attr("serialNumId");
		    	LayoutDetail.serialNumRemark = $(compone).attr("serialNumRemark");
		    	
		    	//关联的字段
		    	LayoutDetail.tableColumn = $(compone).attr("conf_tableColumn");
		    	
	    	}else if(/RadioBox|CheckBox/ig.test(componKey)){
	    		//默认不是必填
		    	LayoutDetail.required = $(compone).attr("conf_require")?$(compone).attr("conf_require"):"false";
		    	
	    		var options = $(compone).find("input");
	    		var FormOptions = new Array();
	    		$.each(options,function(optIndex,optObj){
	    			var  FormOption = new Object();
	    			FormOption.fieldId=$(optObj).attr("fieldId");
	    			FormOption.name=$(optObj).attr("optName");
	    			FormOption.componentKey="Option";
	    			FormOptions.push(FormOption)
	    		})
	    		LayoutDetail.options=FormOptions;
	    		
	    		LayoutDetail.titleLayout=$(compone).attr("conf_layout")?$(compone).attr("conf_layout"):"field-hoz";;
	    		
	    		//关联的字段
		    	LayoutDetail.tableColumn = $(compone).attr("conf_tableColumn");
		    	//采用的字典表
		    	LayoutDetail.sysDataDic = $(compone).attr("conf_sysDataDic");
	    	}else if(/Select/ig.test(componKey)){
	    		//默认不是必填
		    	LayoutDetail.required = $(compone).attr("conf_require")?$(compone).attr("conf_require"):"false";
		    	
	    		var options = $(compone).find("option");
	    		var FormOptions = new Array();
	    		$.each(options,function(optIndex,optObj){
	    			var  FormOption = new Object();
	    			FormOption.fieldId=$(optObj).attr("fieldId");
	    			FormOption.name=$(optObj).attr("optName");
	    			FormOption.componentKey="Option";
	    			FormOptions.push(FormOption);
	    		})
	    		LayoutDetail.options=FormOptions;
	    		
	    		//关联的字段
		    	LayoutDetail.tableColumn = $(compone).attr("conf_tableColumn");
	    		//采用的字典表
		    	LayoutDetail.sysDataDic = $(compone).attr("conf_sysDataDic");
		    	
	    	}else if(/DateComponent/ig.test(componKey)){
	    		//默认不是必填
		    	LayoutDetail.required = $(compone).attr("conf_require")?$(compone).attr("conf_require"):"false";
		    	
	    		LayoutDetail.isDefault = $(compone).attr("conf_isDefault")?$(compone).attr("conf_isDefault"):"false";
	    		LayoutDetail.format = $(compone).attr("conf_format")?$(compone).attr("conf_format"):'yyyy-MM-dd';
	    		
	    		LayoutDetail.isEdit = $(compone).attr("conf_isEdit")?$(compone).attr("conf_isEdit"):"true";
	    		
	    		//关联的字段
		    	LayoutDetail.tableColumn = $(compone).attr("conf_tableColumn");
	    	}else if(/DateInterval/ig.test(componKey)){
	    		//默认不是必填
		    	LayoutDetail.required = $(compone).attr("conf_require")?$(compone).attr("conf_require"):"false";
		    	
	    		var startDate = easyWinFormDesign.constrCompon($(compone).find("input:eq(0)"));
	    		var endDate = easyWinFormDesign.constrCompon($(compone).find("input:eq(1)"));
	    		startDate.format = $(compone).attr("conf_format")?$(compone).attr("conf_format"):'yyyy-MM-dd';
	    		endDate.format = $(compone).attr("conf_format")?$(compone).attr("conf_format"):'yyyy-MM-dd';
	    		
	    		LayoutDetail.start = startDate;
	    		LayoutDetail.end = endDate;
	    		
	    		
	    	}else if(/Employee|Department/ig.test(componKey)){
	    		//默认不是必填
		    	LayoutDetail.required = $(compone).attr("conf_require")?$(compone).attr("conf_require"):"false";
		    	
	    		LayoutDetail.isDefault = $(compone).attr("conf_isDefault")?$(compone).attr("conf_isDefault"):"false";
	    		LayoutDetail.isUnique = $(compone).attr("conf_isUnique")?$(compone).attr("conf_isUnique"):"false";
	    		
	    		LayoutDetail.isEdit = $(compone).attr("conf_isEdit")?$(compone).attr("conf_isEdit"):"true";
	    	}else if(/RelateItem|RelateFile/ig.test(componKey)){
                //默认不是必填
                LayoutDetail.required = $(compone).attr("conf_require")?$(compone).attr("conf_require"):"false";

                LayoutDetail.isUnique = $(compone).attr("conf_isUnique")?$(compone).attr("conf_isUnique"):"false";
            }else if(/RelateCrm/ig.test(componKey)){
	    		//默认不是必填
	    		LayoutDetail.required = $(compone).attr("conf_require")?$(compone).attr("conf_require"):"false";
	    		
	    		LayoutDetail.isUnique = $(compone).attr("conf_isUnique")?$(compone).attr("conf_isUnique"):"false";
            }else if(/RelateMod/ig.test(componKey)){
            	//默认不是必填
            	LayoutDetail.required = $(compone).attr("conf_require")?$(compone).attr("conf_require"):"false";
            	
            	LayoutDetail.isUnique = $(compone).attr("conf_isUnique")?$(compone).attr("conf_isUnique"):"false";
            	//关联模块
            	LayoutDetail.relateModType = $(compone).attr("conf_relateModType");
	    	}else if(/Monitor/ig.test(componKey)){
	    		LayoutDetail.size = $(compone).attr("conf_size")?$(compone).attr("conf_size"):"medium";
	    		LayoutDetail.monitorType = $(compone).attr("conf_monitorType");
	    		if(LayoutDetail.monitorType=='date'){
	    			LayoutDetail.calTimeType = $(compone).attr("conf_calTimeType")?$(compone).attr("conf_calTimeType"):"1";
	    			var  conf_monitorTempDateFields= $(compone).attr("conf_monitorTempDateFields");
	    			//计算的控件的标识
	    			LayoutDetail.monitorTempFields = conf_monitorTempDateFields;
	    		}else{
	    			var  conf_monitorTempDateFields= $(compone).attr("conf_monitorTempNumFields");
	    			conf_monitorTempDateFields = conf_monitorTempDateFields.replace(/\[|]/g,'')
					var monitorNumFieldArray = conf_monitorTempDateFields.split(",");
	    			var componentIds = new Array();
	    			if(monitorNumFieldArray && monitorNumFieldArray[0]){
	    				$.each(monitorNumFieldArray,function(index,tempObjId){
	    					var MonitorField = {};
	    					MonitorField.type = "number";
	    					tempObjId = $.trim(tempObjId);
	    					MonitorField.value = tempObjId;
	    					componentIds.push(MonitorField);
	    				})
	    			}
	    			LayoutDetail.componentIds = componentIds;
	    		}
	    	}else if(/MoneyComponent/ig.test(componKey)){
	    		LayoutDetail.moneyTempColumn = $(compone).attr("conf_moneyTempColumn");
	    	}else if(/DataTable/ig.test(componKey)){
	    		var thItems = $(compone).find(".subTableHead").find(".tdItem");
	    		var tdItems = $(compone).find(".subTableBody").find(".tdItem");
	    		LayoutDetail.size = tdItems.length;
	    		LayoutDetail.subFormId = $(compone).attr("subFormId");
	    		
	    		var dataLayoutDetails = new Array();
	    		$.each($(thItems),function(itemIndex,itemObj){
	    			if($(itemObj).find("[easyWinCompon]").length>0){
	    				$.each($(itemObj).find("[easyWinCompon]"),function(subIndex,subObj){
	    					var subLayoutDetail = new Object();
		    				subLayoutDetail.componentKey='subItemTitle';
		    				subLayoutDetail.title = $(subObj).attr("conf_name");
		    				subLayoutDetail.tempId = $(subObj).attr("tempId");
		    				subLayoutDetail.index=itemIndex;
		    				dataLayoutDetails.push(subLayoutDetail);
	    				})
	    			}
	    		})
	    		
	    		$.each($(tdItems),function(itemIndex,itemObj){
	    			if($(itemObj).find("[easyWinCompon]").length>0){
	    				$.each($(itemObj).find("[easyWinCompon]"),function(subIndex,subObj){
	    					if($(subObj).parents("[easyWinCompon='DateInterval']").length==0){
	    						var subLayoutDetail = easyWinFormDesign.constrCompon($(subObj));
	    						subLayoutDetail.index=itemIndex;
	    						dataLayoutDetails.push(subLayoutDetail);
	    					}
	    				})
	    			}else{
	    				var subLayoutDetail = new Object();
	    				subLayoutDetail.componentKey='ColumnPanel';
	    				subLayoutDetail.index=itemIndex;
	    				dataLayoutDetails.push(subLayoutDetail);
	    			}
	    		})
	    		LayoutDetail.layoutDetail = dataLayoutDetails;
	    		
	    		//关联的表
		    	LayoutDetail.relateTable = $(compone).attr("conf_relateTable");
	    	}
	    	return LayoutDetail;
	    },
	    parse_formHtml:function(template,fields){
	    	var template_Html = template;
	    	var templateObj = $(template);
	    	templateObj = $("<div></div>").html(templateObj)
	    	$(templateObj).find(".tempClz").remove()
	    	$.each($(templateObj),function(level,levleOne){
	    		var tagName = $(levleOne).prop("tagName");
	    		//移除临时加的数据
	    		if(/p/ig.test(tagName)){
	    			if($(levleOne).find("[easyWinCompon]").length>0){
	    				var pCompons = $(levleOne).find("[easyWinCompon]");
	    				$.each(pCompons,function(index,compone){
	    					if($(compone).parents("[easyWinCompon='DateInterval']").length==0){
	    						var componKey = $(compone).attr("easyWinCompon");
	    						var tempId = $(compone).attr("tempId")
	    						if(/DataTable/ig.test(componKey)){
	    							var defineTag = $("<easyWin></easyWin>");
	    							defineTag.attr("tempId",tempId);
	    							defineTag.attr("componKey",componKey);
	    							$(compone).before(defineTag);
	    							$(compone).remove();
	    						}else{
	    							var defineTag = $("<easyWin></easyWin>");
	    							defineTag.attr("tempId",tempId);
	    							defineTag.attr("componKey",componKey);
	    							$(compone).before(defineTag);
	    							$(compone).remove();
	    						}
	    						
	    					}
	    				})
	    			}else{
	    				
	    			}
	    		}else if(/table/ig.test(tagName)){
	    			$.each($(levleOne).find("[easyWinCompon]"),function(index,compone){
    					if($(compone).parents("[easyWinCompon='DateInterval']").length==0){
    						var componKey = $(compone).attr("easyWinCompon");
    						var tempId = $(compone).attr("tempId")
    						if(/DataTable/ig.test(componKey)){
    							var defineTag = $("<easyWin></easyWin>");
    							defineTag.attr("tempId",tempId);
    							defineTag.attr("componKey",componKey);
    							$(compone).before(defineTag);
    							$(compone).remove();
    						}else{
    							var defineTag = $("<easyWin></easyWin>");
    							defineTag.attr("tempId",tempId);
    							defineTag.attr("componKey",componKey);
    							$(compone).before(defineTag);
    							$(compone).remove();
    						}
    						
    					}
    					
    				})
	    		}else if(/div/ig.test(tagName)){
	    			if($(levleOne).attr("easyWinCompon")){
	    				var componKey = $(levleOne).attr("easyWinCompon");
						var tempId = $(levleOne).attr("tempId")
						var defineTag = $("<easyWin></easyWin>");
						defineTag.attr("tempId",tempId);
						defineTag.attr("componKey",componKey);
						$(levleOne).before(defineTag)
						$(levleOne).remove();
	    			}else if($(levleOne).find("[easyWinCompon]").length>0){
	    				
	    				$.each($(levleOne).find("[easyWinCompon]"),function(index,compone){
	    					if($(compone).parents("[easyWinCompon='DateInterval']").length==0){
	    						var componKey = $(compone).attr("easyWinCompon");
	    						var tempId = $(compone).attr("tempId")
	    						if(/DataTable/ig.test(componKey)){
	    							var defineTag = $("<easyWin></easyWin>");
	    							defineTag.attr("tempId",tempId);
	    							defineTag.attr("componKey",componKey);
	    							$(compone).before(defineTag)
	    							$(compone).remove();
	    						}else{
	    							var defineTag = $("<easyWin></easyWin>");
	    							defineTag.attr("tempId",tempId);
	    							defineTag.attr("componKey",componKey);
	    							$(compone).before(defineTag)
	    							$(compone).remove();
	    						}
	    						
	    					}
	    				})
	    			}
	    		}
	    		
	    	});
	    	var html = $(templateObj).html();
	    	return html;
	    	
	    },
	    checkCompone:function(template){
	    	var template_Temp = $("<div><div>").append(template);
	    	
	    	var compones = $(template_Temp).find("[easyWinCompon]").filter(function (){
	    		var easyWinCompon = $(this).attr("easyWinCompon");
	    		if(easyWinCompon=='DataTable' 
	    			|| easyWinCompon=='Monitor' 
	    			|| easyWinCompon=='MoneyComponent' 
	    			|| easyWinCompon=='RelateMod' 
	    			|| easyWinCompon=='SerialNum'){
	    			return !0;
	    		}else{
	    			return 0;
	    		}
	    	})
	    	var a = !0;//默认验证功能通过
	    	
	    	if(compones && compones.length>0){
	    		$.each(compones,function(index,obj){
	    			var easyWinCompon = $(this).attr("easyWinCompon");
	    			if(easyWinCompon=='DataTable'){
	    				a = easyWinFormDesign.checkDataTable(obj);
	    				if(!a){
	    					return false
	    				}
	    			}else if(easyWinCompon=='Monitor'){
	    				a  = easyWinFormDesign.checkMonitor(template_Temp,obj);
						if(!a){
	    					return false
	    				}
	    			}else if(easyWinCompon=='MoneyComponent'){
	    				a  = easyWinFormDesign.checkMoney(template_Temp,obj);
	    				if(!a){
	    					return false
	    				}
	    			}else if(easyWinCompon=='SerialNum'){
	    				a  = easyWinFormDesign.checkSerialNum(obj);
	    				if(!a){
	    					return false
	    				}
	    			}else if(easyWinCompon=='RelateMod'){
	    				a  = easyWinFormDesign.checkRelateMod(obj);
	    				if(!a){
	    					return false
	    				}
	    			}
	    		});
	    	}
	    	return a;
	    },
	    checkDataTable : function(DataTable){//验证子表单
	    	
	    	var a = !0;//默认验证功能通过
	    	$.each($(DataTable).find(".subTableBody").find(".tdItem"),function(index,subItem){
	    		if($(subItem).find("[easyWinCompon]").length==0){
	    			var title = $(subItem).parents("[easyWinCompon='DataTable']").attr("conf_name");
	    			alert(title+"需要全部填充控件,请编辑!")
	    			a = 0;
	    			return false
	    		}
	    	})
	    	return a;
	    },
	    checkMonitor : function(componesParam,Monitor){//验证计算控件
	    	
	    	var compones = $('<div></div>');
	    	compones.append(componesParam);
	    	
	    	var a = !0;//默认验证功能通过
	    	var conf_monitortype = $(Monitor).attr("conf_monitortype");
	    	if(conf_monitortype == 'date'){
	    		var conf_monitorTempDateFields = $(Monitor).attr("conf_monitorTempDateFields");
	    		var title = $(Monitor).attr("conf_name");
	    		if(!conf_monitorTempDateFields){
	    			alert("'"+title+"'需要指定计算控件,请编辑!")
	    			a = 0;
	    		}else{
	    			var tempFileds = $(compones).find("[tempId='"+conf_monitorTempDateFields+"']");
	    			if($(tempFileds).length == 0){
	    				alert("'"+title+"'需要指定计算控件,请编辑!");
	    				a = 0;
	    			}
	    		}
	    	}else{
	    		var conf_monitorTempDateFields = $(Monitor).attr("conf_monitorTempNumFields");
	    		var title = $(Monitor).attr("conf_name");
	    		if(!conf_monitorTempDateFields){
	    			alert("'"+title+"'需要指定计算控件,请编辑!")
	    			a = 0;
	    		}else{
	    			conf_monitorTempDateFields = conf_monitorTempDateFields.replace(/\[|]/g,'')
					var monitorNumFieldArray = conf_monitorTempDateFields.split(",");
	    			if(monitorNumFieldArray && monitorNumFieldArray[0]){
	    				$.each(monitorNumFieldArray,function(index,tempObjId){
	    					tempObjId = $.trim(tempObjId);
	    					var tempFileds = $(compones).find("[tempId='"+tempObjId+"']");
	    					if($(tempFileds).length == 0){
	    						alert("'"+title+"'需要指定计算控件,请编辑!");
	    						a = 0;
	    						return false;
	    					}
	    				});
	    			}else{
	    				alert("'"+title+"'需要指定计算控件,请编辑!")
		    			a = 0;
	    			}
	    			
	    		}
	    	}
	    	return a;
	    },
	    checkMoney : function(componesParam,Money){//验证计算控件
	    	
	    	var compones = $('<div></div>');
	    	compones.append(componesParam);
	    	
	    	var a = !0;//默认验证功能通过
	    	var conf_moneyTempColumn = $(Money).attr("conf_moneyTempColumn");
	    	var title = $(Money).attr("conf_name");
	    	if(!conf_moneyTempColumn){
	    		alert("'"+title+"'需要指定转换的控件,请编辑!")
	    		a = 0;
	    	}else{
	    		var tempFileds = $(compones).find("[tempId='"+conf_moneyTempColumn+"']");
	    		if($(tempFileds).length == 0){
	    			alert("'"+title+"'需要指定转换的控件,请编辑!")
	    			a = 0;
	    		}
	    	}
	    	return a;
	    },
	    checkRelateMod:function(RelateMod){
	    	var a = !0;//默认验证功能通过
	    	
	    	var relateModType = $(RelateMod).attr("conf_relateModType");
	    	if(!relateModType || relateModType =='0'){
	    		var title = $(RelateMod).attr("conf_name");
	    		alert(title+"需要指定关联模块,请编辑!");
	    		a = 0;
	    		return false
	    	}
	    	return a;
	    },
	    checkSerialNum:function(SerialNum){
	    	var a = !0;//默认验证功能通过
	    	
	    	var serialNumId = $(SerialNum).attr("serialNumId");
	    	if(!serialNumId){
	    		var title = $(SerialNum).attr("conf_name");
	    		alert(title+"需要指定编码规则,请编辑!")
    			a = 0;
    			return false
	    	}
	    	return a;
	    },
	    /*type  =  save 保存设计 versions 保存版本  close关闭 */
	    fnCheckForm : function ( type ) {
	    	
	    	
	    	if(easyWinEditor.queryCommandState( 'source' )){
	    		easyWinEditor.execCommand('source');//切换到编辑模式才提交，否则有bug
	    	}
	    	
	    	if(easyWinEditor.hasContents()){
	    		easyWinEditor.sync();/*同步内容*/
	    		
	    		var type_value='',formid=0,fields=$("#fields").val(),formeditor='';
	    		if( typeof type!=='undefined' ){
	    			type_value = type;
	    		}
	    		
	    		var modName = $("#modName").val();
	    		if(!modName){
	    			layer.tips("请填写表单名称",$("#modName"),{tips:1});
	    			return;
	    		}
	    		//获取表单设计器里的内容
	    		formeditor=easyWinEditor.getContent();
	    		if(this.checkCompone(formeditor)){
	    			//解析表单设计器控件
	    			var parse_formField = this.parse_formField(formeditor,fields);
	    			var parse_formHtml = this.parse_formHtml(formeditor,fields);
	    			if(parse_formHtml){
	    				var layoutDetail =  JSON.stringify(parse_formField);
	    				
	    				l = {}
	    				l["formMod.id"] = EasyWin.formModId;
	    				l["formMod.modName"] = modName;
	    				l["formLayout.id"] = EasyWin.layoutId;
	    				l["formLayout.formModId"] = EasyWin.formModId;
	    				l["formLayout.layoutDetail"] = layoutDetail;
	    				l["formLayout.formLayoutHtml.layoutHtml"] = parse_formHtml;
	    				$.ajax({
	    					type: "POST",
	    					data: l,
	    					dataType: "json",
	    					url: sid?"/form/editFormModDev?sid="+sid:"/web/form/editFormModDev",
	    							success: function(b) {
	    								window.self.location.reload();
	    							},
	    							error: function() {}
	    				})
	    			}
	    		}
	    	}
	    	

	    },
	    
	    showHtml:function(formeditor){
	    	
	    	$("#editForm").hide();
	    	$("#viewForm").show();
	    	$("#backBtn").show();
	    	
	    	$("#saveBtn").hide();
			$("#viewBtn").hide();
	    	
	    	//解析表单设计器控件
    		var parse_formField = easyWinFormDesign.parse_formField(formeditor,null);
    		var parse_formHtml = easyWinFormDesign.parse_formHtml(formeditor,null);
    		$("#formLayoutContent").html("")
    		if(parse_formHtml){
    			var layoutHtml= parse_formHtml;
        		var temp_layoutHtml = $("<div></div>").append(layoutHtml);
        		$(temp_layoutHtml).find(".tempClz").remove();
        		var lastPObj = $(temp_layoutHtml).find("p:last");
    			while($(lastPObj).html()=='<br>'){
    				var lastPrePObj = $(lastPObj).prev();
    				$(lastPObj).remove();
    				lastPObj = lastPrePObj
    			}
    			
    			$(temp_layoutHtml).find("table").addClass("table table-bordered")
    			$(temp_layoutHtml).find("td").css("border-bottom-color","#000")
    			$(temp_layoutHtml).find("td").css("border-top-color","#000")
    			$(temp_layoutHtml).find("td").css("border-left-color","#000")
    			$(temp_layoutHtml).find("td").css("border-right-color","#000")
    			
    			parse_formField ? $.each(parse_formField,function(index,layoutDetail){
        			EasyWinViewForm.analyseComponent(layoutDetail,$(temp_layoutHtml));
        		}):"";
        		
        		$(temp_layoutHtml).find("[easyWinCompon='DataTable']").parents("td").css("padding","0px")
        		$("#formLayoutContent").html($(temp_layoutHtml))
        		
        		EasyWinViewForm.initEvent();
        		//构建权重
        		EasyWinViewForm.constrMonitorWeight();
    		}
	    },
	    /*预览表单*/
	    fnReview : function (){
	    	 if(easyWinEditor.queryCommandState( 'source' )){
	    		 easyWinEditor.execCommand('source');/*切换到编辑模式才提交，否则部分浏览器有bug*/
	    	 }
	         if(easyWinEditor.hasContents()){
	        	 easyWinEditor.sync();       /*同步内容*/
	        	 
	        	 var formeditor=easyWinEditor.getContent();
	        	 easyWinFormDesign.showHtml(formeditor)
	         }
	    }

	}
	var height = window.innerHeight-130;
	//设置滚动条高度
	//对编辑器的操作最好在编辑器ready之后再做
	
	var myIfream;
	var aa;
	easyWinEditor.ready(function() {
		easyWinEditor.setHeight(height);
		easyWinFormDesign.loadFormLayout();
		
		myIfream = $(document).find("#middlePlug").find("iframe");
		aa = $(myIfream).contents().find("body");
		
	});
	window.onresize = function(){
		var height = window.innerHeight-130;
		if(myIfream && aa){
			$(myIfream).parent().css("height",height + "px");
			$(aa).css("height",height + "px");
		}
	}
	
	$("#backBtn").on("click",function(){
		$("#editForm").show();
		$("#viewForm").hide();
		$("#backBtn").hide();
		
		$("#saveBtn").show();
		$("#viewBtn").show();
	});
	$("#viewBtn").on("click",function(){
		easyWinFormDesign.fnReview()
	});
	$("#saveBtn").on("click",function(){
		easyWinFormDesign.fnCheckForm('save')
	});
	
	$("body").on("click","#rollBtn",function(){
		window.top.layer.confirm("确定恢复到最新简约版吗?", {icon: 3, title:'确认对话框'}, function(index){
			$.ajax({
				type: "POST",
				data: {"formModId":EasyWin.formModId},
				dataType: "json",
				url: sid?"/form/backFormLayout?sid="+sid:"/web/form/backFormLayout",
						success: function(data) {
							if(data.status=='y'){
								var url = sid?"/form/editFormPage?sid="+sid:"/web/form/editFormPage?t="+Math.random();
								url = url+"&formModId="+EasyWin.formModId;
								window.self.location.replace(url);
							}else{
								showNotification(2,data.info);
							}
						},
						error: function() {}
			})
		});
	})
	
	
	
	$("body").on("click",".componList li",function(){
		var componType = $(this).attr("componType");
		
		$("#preFormComponent").removeData("layoutDetail")
		easyWinFormDesign.exec(componType)
		
	})
	$("body").on("click",".componPreList li",function(){
		var componType = $(this).attr("componType");
		var layoutDetail = $(this).data("layoutDetail");
		
		$("#preFormComponent").data("layoutDetail",layoutDetail);
		easyWinFormDesign.exec(componType)
		$(this).remove();
		
	})
})

function chooseSysColumn(tableName,callback){
	if(!sid){
		showNotification(2,"该功能不支持后台操作！");
		return;
	}
	layer.open({
		type: 2,
	 	title:false,
	 	closeBtn:0,
	 	area: ['550px', '400px'],
	 	fix: false, //不固定
	 	maxmin: false,
	 	scrollbar:false,
	 	content: ["/form/chooseSysColumnPage?sid="+sid+"&t="+Math.random(),'no'],
	 	btn: ['确定','关闭'],
	 	yes: function(index, layero){
	 		var iframeWin = window[layero.find('iframe')[0]['name']];
	 		
	 		iframeWin.chooseColumn(function(data){
	 			callback(data);
	 			layer.close(index)
	 		});
	 	},btn2:function(index, layero){
 			 layer.close(index);
 			 callback();
	 	},success:function(layero,index){
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.initTableName(tableName);
	 	}
 	});
}
//字典表数据查询
function chooseSysDic(sysDicName,callback){
	if(!sid){
		showNotification(2,"该功能不支持后台操作！");
		return;
	}
	var url = "/form/chooseSysDic";
	var params = {
			sid:sid,
			t:Math.random(),
			sysDicName:sysDicName
	}
	postUrl(url,params,function(data){
		if(data.status=='y'){
			var list = data.list;
			var results  = new Array();
			if(sysDicName.toLowerCase()=='consumetype'){
				$.each(list,function(index,obj){
					var result = {itemName:obj.typeName};
					results.push(result);
				})
			}
			callback(results)
		}else{
			showNotification(2,data.info);
		}
	})
}