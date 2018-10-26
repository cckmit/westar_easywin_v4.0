$(function(){
	$("#searchOrgName").blur(function(){
		$("#searhOrganic").submit();
	});
	
	$("#searchOrgName").focus(function(){
		$("#searchOrgName").val('');
	});
	//团队名称模糊查询
	$("#searhOrganic").find(".ps-searchBtn").off("click").on("click",function(){
		$("#searhOrganic").submit();
	})
});
//提交
function submitForm(){
	$("#searhOrganic").submit();
}
//查询详情
function showOrganicInfo(orgNum,userId){
	var url ="/web/count/showOrganicInfo?orgNum="+orgNum+"&userId="+userId;
	openWinByRight(url);
}
//团队分页展示
function listPagedOrgSysLog(orgNum){
	var url ="/web/count/listPagedOrgSysLog?orgNum="+orgNum+"&pager.pageSize=10";
	openWinByRight(url);
}
//按激活状态查询
function ogranicEnabledSearch(enabled){
	$("#searhOrganic").find("input[name='enabled']").val(enabled);
	$("#searhOrganic").submit();
}
//清空条件
function resetMoreCon(divId){
	$("#"+divId).find("input").val('');
}
