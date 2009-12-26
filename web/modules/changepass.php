<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$err_count=0;
$msg="";

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

if($err_count==0){
	$code="";
	$codestr='ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	for($i=0;$i<10;$i++){
		$code.=$codestr[rand(0, 25)];
	}

	$str="Delete from zwls_code where account='$id'";
	$db->query($str);
	$str="insert into `zwls_code` (`account` , `code` , `time` , `ip`) values ('$id','$code','$time','$ip')";
	$db->query($str);

	mysql_select_db($sql_dbname, $login_on);
	$straa =sprintf("SELECT * FROM user_register WHERE `name` LIKE '$id' ORDER BY `name` DESC", $str);
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);
	$serial=$strac['serial'];
	$username=$strac['username'];
	$email=$strac['e_mail'];
	$register_time=$strac['register_time'];
	$event_point=$strac['event_point'];
	$bonus_point=$strac['bonus_point'];
	$invited=$strac['invited'];
	$logintime=$strac['logintime'];
	$loginip=$strac['loginip'];

	$strba =sprintf("SELECT * FROM user_register WHERE `name` LIKE '$invited' ORDER BY `name` DESC", $str);
	$strbb=mysql_query($strba, $login_on) or die(mysql_error());
	$strbc=mysql_fetch_assoc($strbb);
	$inviter=$strac['username'];
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
<?if($err_count==0){?>
<form method="post" action="changepass_save.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="code" value="<?=$code?>">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>資料修改</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">帳號編號：<?=$serial?></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">註冊暱稱：<input type="text" name="username" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" maxlength="12" value="<?=$username?>">(辨識用)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">遊戲帳號：<input type="text" name="id" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" maxlength="12" value="<?=$id?>" readonly=readonly>(4位數以上)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">遊戲密碼：<input type="password" name="pass" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" maxlength="13">(4位數以上)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">更改密碼：<input type="password" name="new_pass" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" maxlength="13">(4位數以上，如不修改免輸入)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">確認密碼：<input type="password" name="new_pass_chk" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" maxlength="13">(4位數以上，如不修改免輸入)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">E-mail：<input type="text" name="e_mail" style="width: 150px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value="<?=$email?>"></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">註冊時間：<?=$register_time?></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">最後登入時間：<?=$logintime?></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">最後登入位置：<?=$loginip?></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">帳號邀請者：<?=$inviter?></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
</form>
<?}else{?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
<?}?>
</table>
</center>
</body>
</html>
