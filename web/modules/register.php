<?
require("../setup.php");
$str="select count(*) from zwls_invite_code where name='free' and used=0";
$chk_free=$db->get_var($str);
if($chk_free<=10){
	$free="<font color=red>$chk_free</font>";
}else{
	$free="$chk_free";
}
?>
<html>

<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title>

</head>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<?
include("../html/head.php");
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
<form method="post" action="register_save.php" name="form1" onsubmit="B1.disabled=1">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>帳號申請</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">請詳細填寫申請資料，並妥善保管。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">目前免費註冊碼剩餘：<?=$free?>組。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><a href="javascript://" onClick="window.open('freecode.php','','menubar=no,status=no,scrollbars=yes,top=20,left=50,toolbar=no,width=400,height=400')">索取免費註冊碼</a></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">註冊暱稱：<input type="text" name="username" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" maxlength="12">(辨識用)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">遊戲帳號：<input type="text" name="id" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" maxlength="12">(4位數以上)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">遊戲密碼：<input type="password" name="pass" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" maxlength="13">(4位數以上)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">確認密碼：<input type="password" name="pass_chk" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" maxlength="13">(4位數以上)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">E-mail：<input type="text" name="e_mail" style="width: 150px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value="<?=$_GET[e_mail]?>"></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">註冊碼：<input type="text" name="reg_code" style="width: 150px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value="<?=$_GET[itc]?>"></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
</form>
</table>
</center>
</body>
</html>
