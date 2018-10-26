$(function(){
	$('.ws-side-menu li').mouseover(function(){
		$(this).children("span").show();
	}).mouseout(function(){
		$(".ws-side-menu span").hide();
	})
})
