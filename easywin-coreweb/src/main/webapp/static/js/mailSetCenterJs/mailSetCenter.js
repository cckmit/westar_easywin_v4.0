//添加邮箱设置
function add(sid){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	window.location.href='/mailSet/addMailSetPage?pager.pageSize=10&activityMenu=self_m_3.5.1&sid='+sid+'&redirectPage='+encodeURIComponent(window.location.href);
}
//修改邮箱设置
function update(id,sid){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	window.location.href='/mailSet/updateMailSetPage?pager.pageSize=10&activityMenu=self_m_3.5.2&id='+id+'&sid='+sid+'&redirectPage='+encodeURIComponent(window.location.href);
}
//删除邮箱设置
function del(id,sid){
	window.top.layer.confirm('确定要删除该配置吗？',{icon:3,title:'询问框'}, function(index){
		//启动加载页面效果
		layer.load(0, {shade: [0.6,'#fff']});
		window.location.href='/mailSet/delMailSet?pager.pageSize=10&activityMenu=self_m_3.5&id='+id+'&sid='+sid+'&redirectPage='+encodeURIComponent(window.location.href);
	}, function(){
	});	
}
var mails={};
mails['163.com']=new Array('pop.163.com',110,0,'smtp.163.com',25,0,0,0,"imap.163.com",143);
mails['vip.163.com']=new Array('pop.vip.163.com',110,0,'smtp.vip.163.com',25,0,0,0,"imap.vip.163.com",143);
mails['188.com']=new Array('pop.188.com',110,0,'smtp.188.com',25,0,0,0,"imap.188.com",143);
mails['126.com']=new Array('pop.126.com',110,0,'smtp.126.com',25,0,0,0,"imap.126.com",143);
mails['yeah.net']=new Array('pop.yeah.net',110,0,'smtp.yeah.net',25,0,0,0,"imap.yeah.net",143);
mails['qq.com']=new Array('pop.qq.com',995,1,'smtp.qq.com',465,1,0,0,"imap.qq.com",143);
mails['vip.qq.com']=new Array('pop.qq.com',995,1,'smtp.qq.com',465,1,1,0,"imap.qq.com",143);
mails['sina.com']=new Array('pop.sina.com',110,0,'smtp.sina.com',25,0,0,0,"imap.sina.com",993);
mails['vip.sina.com']=new Array('pop3.vip.sina.com',110,0,'smtp.vip.sina.com',25,0,0,0,"imap.vip.sina.com",993);
mails['sohu.com']=new Array('pop.sohu.com',110,0,'smtp.sohu.com',25,0,0,0,"imap.sohu.com",993);
mails['tom.com']=new Array('pop.tom.com',110,0,'smtp.tom.com',25,0,0,0,"imap.tom.com",993);
mails['gmail.com']=new Array('pop.gmail.com',995,1,'smtp.gmail.com',465,1,1,0,"imap.gmail.com",993);
mails['yahoo.com.cn']=new Array('pop.mail.yahoo.com.cn',995,1,'smtp.mail.yahoo.com.cn',465,1,1,0,'imap.mail.yahoo.com.cn',993);
mails['yahoo.cn']=new Array('pop.mail.yahoo.cn',995,1,'smtp.mail.yahoo.cn',465,1,1,0,'imap.mail.yahoo.cn',993);
mails['21cn.com']=new Array('pop.21cn.com',110,0,'smtp.21cn.com',25,0,0,0,'imap.21cn.com',993);
mails['21cn.net']=new Array('pop.21cn.net',110,0,'smtp.21cn.net',25,0,0,0,'imap.21cn.net',993);
mails['263.net']=new Array('263.net',110,0,'smtp.263.net',25,0,0,0,'smtp.263.net',993);
mails['x263.net']=new Array('pop.x263.net',110,0,'smtp.x263.net',25,0,0,0,'smtp.x263.net',993);
mails['263.net.cn']=new Array('263.net.cn',110,0,'263.net.cn',25,0,0,0,'263.net.cn',993);
mails['263xmail.com']=new Array('pop.263xmail.com',110,0,'smtp.263xmail.com',25,0,0,0,'imap.263xmail.com',993);
mails['foxmail.com']=new Array('pop.foxmail.com',110,0,'smtp.foxmail.com',25,0,0,0,'imap.foxmail.com',993);
mails['hotmail.com']=new Array('pop3.live.com',995,1,'smtp.live.com',25,1,1,0,'imap.live.com',993);
mails['live.com']=new Array('pop3.live.com',995,1,'smtp.live.com',25,1,1,0,'imap.live.com',993);

function FillSettings(email)
{
   $('#smtpSSL').attr("checked",false);
   $('#serverHost').val('');
   $('#serverPort').val('');
   $('#account').val(email)
   if(email.trim()=="" || email.indexOf("@")<0) return;
   var email = email.substr(email.indexOf("@")+1).trim();
   if(!mails[email]) return;

   //$('POP_SERVER').value=mails[email][0];
   //$('POP3_PORT').value=mails[email][1];
   //$('POP3_SSL').checked=mails[email][2];
   $('#serverHost').val(mails[email][3]);
   $('#serverPort').val(mails[email][4]);
   $('#serverImapHost').val(mails[email][8]);
   $('#serverImapPort').val(mails[email][9]);
   if(mails[email][5]==1){
	   $('#smtpSSL').attr("checked",true);
	}else{
	   $('#smtpSSL').attr("checked",false);
	}
//   $('LOGIN_TYPE').selectedIndex=mails[email][6];
if(mails[email][7]==0){
	var isValidate = $(':radio[name="isValidate"]')[0];
	$(isValidate).attr("checked",true);
}else{
	var isValidate = $(':radio[name="isValidate"]')[1];
	$(isValidate).attr("checked",true);
}
   //$('SMTP_PASS').selectedIndex=mails[email][7];
  // $('EMAIL_PASS') && $('EMAIL_PASS').focus();
}
//邮箱验证
var regE = /^([a-zA-Z0-9]+[_|\_|\.|\-]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
function checkEmail(ts,sid){
	var isValidate = $(':radio[name="isValidate"]:checked').val();
	//邮箱
	var email = $('#email').val();
	if(!email ){
		$('#email').focus();
		return;
	}
	//邮箱格式验证
	if(!regE.test(email)){
		$('#email').focus();
		return;
	}
	//smpt服务
	var serverHost = $('#serverHost').val();
	if(!serverHost){
		$('#serverHost').focus();
		return;
	}
	//smpt端口
	var serverPort = $('#serverPort').val();
	if(!serverPort){
		$('#serverPort').focus();
		return;
	}
	//imap服务
	var serverImapHost = $('#serverImapHost').val();
	if(!serverImapHost){
		$('#serverImapHost').focus();
		return;
	}
	//Imap端口
	var serverImapPort = $('#serverImapPort').val();
	if(!serverImapPort){
		$('#serverImapPort').focus();
		return;
	}
	//邮箱账户
	var account = $('#account').val();
	if(!account){
		$('#account').focus();
		return;
	}
	//邮箱密码
	var passwd = $('#passwd').val();
	if(!passwd){
		$('#passwd').focus();
		return;
	}
	var smtpSSL = 0;
	if($('#smtpSSL').attr("checked")){
		smtpSSL = 1;
	}
	if(email && serverHost && serverPort && serverImapHost && serverImapPort && account && passwd){
		//邮箱密码
		$.ajax({
			type : "post",
			url : "/mailSet/valiMail?sid="+sid+"&rnd="+Math.random(),
			dataType:"json",
			data:{
				serverHost:serverHost,
				serverPort:serverPort,
				serverImapHost:serverImapHost,
				serverImapPort:serverImapPort,
				account:account,
				passwd:passwd,
				isValidate:isValidate,
				smtpSSL:smtpSSL
			},
			beforeSend: function(XMLHttpRequest){
				showNotification(0,"正在验证邮箱");
				$(ts).attr("disabled","disabled");
	        },
	        success : function(data){
		        if(data.status=='y'){
			        $('#mailSetForm').submit();
			    }else{
				    showNotification(2,"配置有误，请重新设置");
				}
		        $(ts).removeAttr("disabled");
	        },
	        error:  function(XMLHttpRequest, textStatus, errorThrown){
	        	showNotification(2,"系统错误");
			}
		});
	}
	return false;
}