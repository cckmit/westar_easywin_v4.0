var index_step = [
	/*	{
			intro : "<div style='width:300px;'><h2 class='padding-left-50 noread padding-bottom-10' style='font-size:16px;'>欢迎使用捷成办公系统！</h2><p>1.可以通过方向键进行引导步骤控制。</p><p>2.可通过ESC键、点击遮隐处和点击跳过暂时退出引导。</p><div>",
			position : "bottom",
		},*/
		{
			element : "#menuOrgDiv",
			intro : "<div style='width:300px'>点击此处可以对团队进行快捷管理！进入团队设置,邀请同事等..</div>",
			position : "right",
		},
		{
			element : "#headMenuLi",
			intro : "<div class='padding-left-40 padding-bottom-20' style='color:#2dc3e8'>系统主要应用模块,选择进入相应的应用。</div><img src='/static/images/intro/headMoreMenu.jpg' />",
			position : "right",

		},
		{
			element : "#addMoreBusLi",
			intro : "<div  class='padding-bottom-10'  style='color:#2dc3e8'>此处可以快捷办公。可以快捷发布任务,审批以及其他办公！</div><img src='/static/images/intro/addSimple.jpg' />",
			position : "right",
		},
		{
			element : "#busMsgLi",
			intro : "<div  class='padding-bottom-10' style='color:#2dc3e8'>此处显示各项未读消息的数目。</div><img src='/static/images/intro/noReadMsg.jpg' />",
			position : "right",
		},
		{
			element : "#headSearch",
			intro : "通过关键字可以搜索系统相关的信息。",
			position : "bottom",
		},
		{
			element : "#menuUserLi",
			intro : "<div  class='padding-bottom-10'  style='color:#2dc3e8'>此处快捷的进行个人头像和其他信息的设置;</div><img src='/static/images/intro/headImg.jpg' />",
			position : "left"
		},
		{
			element : "#todoTask",
			intro : "<div style='width:260px;'>待办事项数目,较重要事项,需要及时处理。点击进入待办中心进行处理。</div>",
			position : "right",
		}, {
			element : "#overDueTask",
			intro : "<div style='width:260px;'>已经逾期的任务数目,点击进入任务中心更新任务情况。</div>",
			position : "right",

		}, {
			element : "#attenNums",
			intro : "<div style='width:260px;'>关注信息未看的数目,点击进入关注中心进行查看。</div>",
			position : "right",
		}, {
			element : "#paramDiv",
			intro : "<h5  class='margin-bottom-10' style='font-size: 14px;font-weight: bold  !important;'>栏目管理<h5><div style='width:260px;'><p>1.显示各栏目的前几条数据,点击可以查看详情。</p><p>2.头部对栏目进行设置,设置栏目的显示情况,对该栏目进行隐藏和删除。</p><p>3.下方操作可以进行快捷发布和进入该栏目中心查看更多数据。</p></div>",
			position : "right",
		}, {
			element : "#indexRight",
			intro : "消息分享",
			position : "left",
		}, ]

var introStep = {
	"index_page" : index_step,
}
