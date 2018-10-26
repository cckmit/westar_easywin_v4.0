   function skin(){
	    var skin = $("input[name='userSkin']").val();
	    $("#"+skin).addClass("selected").siblings().removeClass("selected");
	    var dialog = art.dialog({
		      id: 'skin', 
	       title: '选择皮肤',
	     content: document.getElementById('skin')
		});
	}

   $(function(){
		$('#skin li').click(function(){
			$("#"+this.id).addClass("selected").siblings().removeClass("selected");
			$('#skinCss').attr("href","/static/css/"+(this.id)+".css");
			var sid = $("input[name='sid']").val();
			var d = "sid="+sid+"&skin="+(this.id);
			doajax("/admin/userInfo/updateUserInfoSkin",d,function(data){
				
			});
			$("input[name='userSkin']").val(this.id);
		});
	});
   
	function doajax(url,date,fn){
		$.ajax({
			url: url,
			data:date,
			type: "POST",
			dataType: "json",
			cache: false,
			success: function(data) {
				fn(data);
			}
		});
	};