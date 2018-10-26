$(function(){
	initZtree();
})

var zTreeObj;
var flag = true;
var setting = {
	view : {
		selectedMulti : false,
		fontCss : getFont,
		addHoverDom: addHoverDom,
		removeHoverDom: removeHoverDom,
		dblClickExpand : dblClickExpand
	},
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		onClick : this.onClick,
		beforeDrop:this.beforeDrop
	},
	check : {
		enable: flag,
		chkboxType:{"Y":"ps","N":"ps"}
	},
	edit : {
		enable: true,
		showRemoveBtn: false,
		showRenameBtn: false,
		drag : {
			isCopy:false
		}
	}
};
	//查看办公用品条目列表
	function onClick(e, treeId, node) {
		var url = '/bgypItem/listPagedBgypItem4Fl?flName='+node.realName+'&sid='+sid;
		if(node.id>0){
			url = url+'&flId='+node.id
		}
		window.rightFrame.location.href =url;
	}
	var actionTreeNode=null;
	//用于当鼠标移动到节点上时，显示用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮
	function addHoverDom(treeId, treeNode) {
		actionTreeNode = treeNode;
		
		var sObj = $("#" + treeNode.tId + "_span");
		
		if($("#editBtn_"+treeNode.tId).length==0 && flag && treeNode.id>0){
			var editStr = "<span class='fa fa-edit fa-lg ' style='position: inherit;width:15px;height:15px;' id='editBtn_" + treeNode.tId
			+ "' title='修改' onfocus='this.blur();'></span>";
			sObj.after(editStr);
			
			var editBtn = $("#editBtn_"+treeNode.tId);
			if(editBtn){
				editBtn.bind("click", function(){
					editBgypFl(treeNode);
					return false;
				});
			}
			
		}
		
		if($("#addBtn_"+treeNode.tId).length==0 && flag){
			var addStr = "<span class='fa fa-plus-circle fa-lg margin-right-10' style='position: inherit;width:15px;height:15px;padding:0px 5px;' id='addBtn_" + treeNode.tId
			+ "' title='添加' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var addBtn = $("#addBtn_"+treeNode.tId);
			if(addBtn){
				addBtn.bind("click", function(){
					addBgypFl(treeNode);
					return false;
				});
			}
		}
	}
	//用于当鼠标移出节点时，隐藏用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮
	function removeHoverDom(treeId, treeNode) {
		$("#editBtn_"+treeNode.tId).unbind().remove();
		$("#addBtn_"+treeNode.tId).unbind().remove();
	};

	

	//根节点点击后不打开关闭
	function dblClickExpand(treeId, treeNode) {
		return treeNode.level > 0;
	}
	
	function getFont(treeId, node) {
		return node.font ? node.font : {};
	}

	var zNodes = new Array();

	function initZtree() {
		//销毁所有的zTree
		$.fn.zTree.destroy();
		zNodes = new Array();
		//通过ajax获取部门树
		$.post('/bgypFl/listTreeBgypFl', {random : Math.random(),sid:sid}, function(tree) {
			//创建一个默认的根节点
			var rootNode = new Object;
			rootNode.id = -1;
			rootNode.name= "办公用品分类";
			rootNode.realName= "办公用品分类";
			rootNode.open = true;
			rootNode.enabled = 0;
			rootNode.checkable = false;
			rootNode.nocheck = true;
			zNodes.push(rootNode);
			if (tree != null) {
				for ( var i = 0; i < tree.length; i++) {
					var node = new Object;
					node.id = tree[i].id;
					if (tree[i].parentId != null) {
						node.pId = tree[i].parentId;
					}
					node.name ='['+tree[i].flCode+']'+ tree[i].flName;
					node.realName = tree[i].flName;
					if(tree[i].parentId==-1){
						node.open = true;
					}
					node.icon="/static/images/folder_add.png";
					zNodes.push(node);
				}
			}
			zTreeObj = $.fn.zTree.init($('#bgflTree'), setting, zNodes);
		},"json");
	}
	//新增办公用品分类
	function addBgypFl(treeNode){
		//父类主键
		var bgypFlId = treeNode.id;
		//父类名称
		var bgypFlName = treeNode.realName;
		var url = '/bgypFl/addBgypFlPage?parentId='+bgypFlId+'&pFlName='+bgypFlName+'&sid='+sid;
		
		openLayerWindow(url,0,function(data){
			if(data.status=='y'){
				initZtree();
			}
		})
	}
	//编辑办公用品分类
	function editBgypFl(treeNode){
		//分类主键
		var bgypFlId = treeNode.id;
		//分类名称
		var bgypFlName = treeNode.realName;
		var url = '/bgypFl/updateBgypFlPage?bgypFlId='+bgypFlId+'&sid='+sid;
		
		openLayerWindow(url,1,function(data){
			if(data.status=='y'){
				initZtree();
			}
		})
	}
	
	function openLayerWindow(url,flag,callBack){
		window.top.layui.use('layer', function(){
			var layer = window.top.layui.layer;
			layer.open({
				type: 2,
				title:false,
				closeBtn:0,
				area: ['550px', '400px'],
				fix: false, //不固定
				maxmin: false,
				scrollbar:false,
				content: [url,'no'],
				btn: [flag==1?'提交':'新增','关闭'],
				yes: function(index, layero){
					var iframeWin = window[layero.find('iframe')[0]['name']];
					var result = iframeWin.optBgypFl();
					if(result){
						callBack({"fun":"yes","status":"y"})
						layer.close(index);
					}
					
				},cancel: function(){ 
				},success: function(layero,index){
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.setWindow(window.document,window);
				},end:function(index){
				}
			});
		});
	}

	//新增
	function add() {
	    var zTree = $.fn.zTree.getZTreeObj("bgflTree"), nodes = zTree.getSelectedNodes();
	    if(nodes.length>0){
	    	var node = nodes[0];
	    	addBgypFl(node)
	    }else{
	    	var node = zTree.getNodeByParam("id", -1, null);
			addBgypFl(node)
	    }
	    
	}
	//删除
	function del(){
		var zTree = $.fn.zTree.getZTreeObj('bgflTree');
		var nodes=zTree.getCheckedNodes();
		
		window.top.layui.use('layer', function(){
			var layer = window.top.layui.layer;
			if(nodes.length>0){
				//默认可删除信息
				var flag = true;
				var bgypFlIds=new Array();
				for(var i=0;i<nodes.length;i++){
					var halfCheck = nodes[i].getCheckStatus();
					if(halfCheck.checked){
						if(nodes[i].id==-1){
							window.top.layer.alert('根节点不能删除！');
							flag = false;
							break;
						}else if(!halfCheck.half){
							bgypFlIds.push(nodes[i].id);
						}
					}
				}
				if(flag){
					window.top.layer.confirm('确定要删除用品分类吗？', {
						  btn: ['确定','取消']//按钮
					  ,title:'询问框'
					  ,icon:3
					},  function(index){
						window.top.layer.close(index);
						$.post('/bgypFl/ajaxDeleteBgypFl',{random:Math.random(),bgypFlIds:bgypFlIds.toString(),sid:sid},function(data){
							if(data.status=='y'){
								initZtree();
								window.rightFrame.location ="/blank?sid="+sid
							}else if(data.status=='f'){
								var listBgypFl4Del = data.listBgypFl4Del;
								var info ="以下分类已有办公用品条目：" 
								$.each(listBgypFl4Del,function(flIndex,obj){
									info +="<br>"+(flIndex+1)+'、['+obj.flCode+']'+obj.flName;
								})
								window.top.layer.alert(info,{icon: 2,title:"错误"});
							}
						},'json');
					});	
				}
			}else{
				window.top.layer.alert('请勾选需要删除的部门！');
			}
			
		});
	}
	function beforeDrop(treeId, treeNodes, targetNode, moveType,isCopy){
		return false;
	}
