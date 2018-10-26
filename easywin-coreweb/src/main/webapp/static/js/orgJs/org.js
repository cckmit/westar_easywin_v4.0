   $(document).ready(function() {
    $("#orgForm").keydown(function(e) {
        if (e.keyCode == 13) {
        	return false;
        }
    });
	//设置跳转地址
	$("#orgForm input[name='redirectPage']").val(window.location.href);
 });