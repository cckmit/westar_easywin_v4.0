function UnderOrg(len){
	var _this = this;
	var numArr = [];
	var num = 1;
	
	//封装下标  用作修改时做初始化
	if(len>0){
		for(var l=0;l<len;l++){
			numArr.push(l);
		}
		num = len;
	}else {
		numArr.push(0);
	}
	
	
	_this.add=function(sid,lastId){
		//分开写是处理ie兼容性
		var html_tr = '<tr id="tr_'+num+'"></tr>';
		var tr = $(html_tr);
		
		var  html_td1 = '<td id="td_1_'+num+'"></td>';
		var td1 = $(html_td1);
		
		var html_td2 = '<td id="td_2_'+num+'"><input datatype="*" readonly="readonly" type="text" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\'});" style="width: 100px;cursor: hand;" name="endTimes" class="Wdate" /></td>';
		var td2 = $(html_td2)
		
		var html_td3 = '<td id="td_3_'+num+'"></td>';
		var td3 = $(html_td3)
		
		var html_addBtn = '<input type="button" value="新增" class="blue_btn" onclick="add(\''+sid+'\','+num+')" id="addBtn_'+num+'"/>';
		var addBtn = $(html_addBtn);
		
		var html_delBtn = '<input type="button" value="删除" class="blue_btn" onclick="del(\''+sid+'\','+num+')" id="delBtn_'+num+'"/>';
		var delBtn = $(html_delBtn);
		
		td3.append(addBtn);
		td3.append(delBtn);
		td1 = createOrgEl(td1,num,sid)
		tr.append(td1);
		tr.append(td2);
		tr.append(td3);
		
		//获取td_3下的按钮个数，（就删除和新增）
		var btnLen = $("#td_3_"+lastId).find("input[type='button']").length;
		//删除新增按钮
		$("#addBtn_"+lastId).remove();
		if(btnLen==1){
			//就新增按钮换成删除按钮
			html_delBtn = '<input type="button" value="删除" class="blue_btn" onclick="del(\''+sid+'\','+lastId+')" id="delBtn_'+lastId+'"/>';
			delBtn = $(html_delBtn);
			$("#td_3_"+lastId).append(delBtn);
		}
		//将坐标push进数组
		numArr.push(num);
		num++;
		return tr;
	}
	
	//删除一行
	_this.del=function(sid,id){
		$("#tr_"+id).remove();
		numArr.splice(jQuery.inArray(id,numArr),1);
		var lastId = numArr[numArr.length-1];
		var btnLen = $("#td_3_"+lastId).find("input[type='button']").length;
		//目前只有一行 将删除按钮换成添加按钮
		if(numArr.length==1){
			$("#delBtn_"+lastId).remove();
		}
		
		//如果最后一个td里面只有一个按钮
		if(btnLen<2){
			//添加一个添加按钮到td中
			var html_addBtn = '<input type="button" value="新增" class="blue_btn" onclick="add(\''+sid+'\','+lastId+')" id="addBtn_'+lastId+'"/>';
			var addBtn = $(html_addBtn);
			 $("#td_3_"+lastId).prepend(addBtn);
		}
		
		
	}
	// 创建部门选择的元素
	function createOrgEl(td,id,sid){
		var value = "['orgId_'+id,'orgName_'+id]";
		var html_hidInput = '<input type="hidden" name="orgId_'+id+'" id="orgId_'+id+'" value="">';
		var hidInput = $(html_hidInput);
		
		var html_input = '<input onchange="removeText(\'orgId_'+id+'\');" datatype="*" value="" type="text" readonly="readonly" ondblclick="removeText('+value+');" name="orgName_'+id+'" id="orgName_'+id+'">'; 
		var input = $(html_input);
		
		var html_img = '<img style="margin-left: 5px;" src="/static/img/check.png" style="cursor: pointer;" onclick="depOne(\'orgId_'+id+'\',\'orgName_'+id+'\',\'\',\''+sid+'\');">';
		var img = $(html_img);
		
		var html_div = '<div style="display: none;" class="Validform_checktip Validform_right">通过信息验证！</div>';
		var div = $(html_div);
		
		td.append(hidInput);
		td.append(input);
		td.append(img);
		td.append(div);
		
		return td;
	}
	
	//表单提交前数据处理
	_this.handleRes = function(){
		//提交前进行数据处理
		var html = "";
		for(var i=0;i<numArr.length;i++){
			var value = $("#orgId_"+numArr[i]).val();
			html = '<input type="hidden" name="orgIds"  value="'+value+'">';
			$("#td_3_"+numArr[i]).append(html);
		}
	}
}