
		function beforeClick(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj(treeId);
			zTree.checkNode(treeNode, !treeNode.checked, null, true);
			return false;
		}
		
		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj(treeId);
			zTree.checkNode(treeNode, !treeNode.checked, null, true);
			return false;
		}
		
		function onCheck(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj(treeId);
			if(treeNode.ztype==0 || treeNode.ztype==2 ){
				zTree.checkAllNodes(false);
				zTree.checkNode(treeNode,true);
			}else{
				 var node_all=zTree.getNodesByParam("ztype",0);
				 zTree.checkNode(node_all[0],false);
				 var node_self=zTree.getNodesByParam("ztype",2);
				 zTree.checkNode(node_self[0],false)
			}
			zTree = $.fn.zTree.getZTreeObj(treeId);
			var nodes = zTree.getCheckedNodes(true);
			var v = "";
			var idType="";
			if(nodes.length){
				for (var i=0, l=nodes.length; i<l; i++) {
					v += nodes[i].name + ",";
					idType+=nodes[i].id+"@"+nodes[i].ztype+",";
				}
			}else{
				var node_all=zTree.getNodesByParam("ztype",0);
				zTree.checkNode(node_all[0],true);
				v += "所有同事,";
				idType+=-1+"@"+0+",";
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			var cityObj = $("#"+scopeKey);
			cityObj.attr("value", v);
			if (idType.length > 0 ) idType = idType.substring(0, idType.length-1);
			$("#"+hidenKey).val(idType);
			$("#"+scopeKey).focus();
		}

		function showMenu() {
			var cityObj = $("#"+scopeKey);
			var cityOffset = $("#"+scopeKey).offset();
			$("#"+zTreeDisDiv).slideDown("fast");

			$("body").bind("mousedown", onBodyDown);
		}
		function hideMenu() {
			$("#"+zTreeDisDiv).fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
		function onBodyDown(event) {
			if (!(event.target.id == scopeKey || event.target.id == zTreeDisDiv || $(event.target).parents("#"+zTreeDisDiv).length>0)) {
				hideMenu();
			}
		}
		//显示文本框主键
		var scopeKey;
		//返回值隐藏框主键
		var hidenKey;
		//zTree显示div主键
		var zTreeDisDiv;
		function initZTree(setting,treeId,scopeKey,hidenKey,zTreeDisDiv,selfGroupJson){
			this.scopeKey = scopeKey;
			this.hidenKey = hidenKey;
			this.zTreeDisDiv = zTreeDisDiv;
			$.fn.zTree.destroy();
			$.fn.zTree.init($("#"+treeId), setting, selfGroupJson);
		}